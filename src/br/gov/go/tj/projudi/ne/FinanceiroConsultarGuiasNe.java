package br.gov.go.tj.projudi.ne;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.GuiaEmissaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class FinanceiroConsultarGuiasNe extends Negocio {

	private static final long serialVersionUID = -7398071698272406791L;
	
	public static final String AGRUPAMENTO_COMARCA 		= "_c9x9";
	public static final String AGRUPAMENTO_SERVENTIA 	= "_c9w8";
	
	//url feita pelo Leandro Prezoto
	public static final String URL_VALIDA_DATA_PAGAMENTO = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-guia/FORPSPGI/SPG0099P";

	public FinanceiroConsultarGuiasNe() {
		obLog = new LogNe();
	}
	
	/**
	 * Método para validar e atulizar a guia de acordo com a data do SPG.
	 * A url foi o Leandro Prezoto que fez.
	 * @param String numeroGuia
	 * @param String dataAnalise
	 * @return String dataPagamento
	 */
	public String validaGuiaPagaSPG(String numeroGuia, String dataAnalise) throws Exception {
		String dataPagamento = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
			GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
			
			if( numeroGuia != null && numeroGuia.length() > 4 && !guiaEmissaoNe.isGuiaPaga(numeroGuia) ) {
				
				//Consulta no SPG pra ver se está paga, se sim retorna a data do pagamento
				try(DefaultHttpClient httpclient = new DefaultHttpClient()){
					HttpGet httpget = new HttpGet(URL_VALIDA_DATA_PAGAMENTO + "?guia=" + numeroGuia);
				
					HttpResponse response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						dataPagamento = EntityUtils.toString(entity);
					}
					
					//Atualiza pagamento da guia pelo número
					if( dataPagamento != null && dataPagamento.length() == 10 ) {
						
						Integer idGuiaStatus = GuiaStatusDt.PAGO;
						Date dataPagamentoDate = Funcoes.StringToDate(dataPagamento);
						
						if( guiaEmissaoNe.isGuiaCancelada(numeroGuia, obFabricaConexao) ) {
							idGuiaStatus = GuiaStatusDt.CANCELADA_PAGA;
						}
						if( guiaEmissaoNe.isGuiaStatusAguardandoPagamento(numeroGuia, obFabricaConexao) ) {
							idGuiaStatus = GuiaStatusDt.PAGO;
						}
						
						Long longDataPagamento = dataPagamentoDate.getTime();
						Date dataVencimentoGuia = guiaEmissaoNe.consultarDataVencimento(numeroGuia);
						dataVencimentoGuia.setHours( 23 );
						dataVencimentoGuia.setMinutes( 59 );
						dataVencimentoGuia.setSeconds( 59 );
						Long longDataVencimento = dataVencimentoGuia.getTime();
						if( longDataVencimento < longDataPagamento ) {
							idGuiaStatus = GuiaStatusDt.PAGO_APOS_VENCIMENTO;
						}
						
						GuiaEmissaoDt guiaAntesAtualizar = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuia);
						boolean atualizado = obPersistencia.atualizarPagamento(numeroGuia, idGuiaStatus, dataPagamentoDate);
						GuiaEmissaoDt guiaAposAtualizar = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuia);
						
						//**********
						//Gerar a movimentação
						if( atualizado ) {
							//log
							LogDt logDt = new LogDt("GuiaEmissao", guiaAposAtualizar.getId(), UsuarioDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.Alterar), guiaAntesAtualizar.getPropriedades(), guiaAposAtualizar.getPropriedades());
							new LogNe().salvar(logDt, obFabricaConexao);
							
							//Guia Complementar: Processo de alteração dos dados do processo
							guiaEmissaoNe.alterarDadosProcessoViaNumeroGuiaComplementar(numeroGuia, obFabricaConexao);
						}
					}
				}
			}
			
			if( dataAnalise != null && dataAnalise.length() == 10 ) {
				Date dataAnaliseDate = Funcoes.StringToDate(dataAnalise);
				
				List<GuiaEmissaoDt> listGuiaEmissaoDta = guiaEmissaoNe.consultarGuiaEmissaoPorDataEmissao(dataAnalise);
				if( listGuiaEmissaoDta != null ) {
					try(DefaultHttpClient httpclient = new DefaultHttpClient()){
						for( GuiaEmissaoDt guiaEmissaoDt: listGuiaEmissaoDta ) {
							if( !guiaEmissaoNe.isGuiaPaga(guiaEmissaoDt) ) {
								HttpGet httpget = new HttpGet(URL_VALIDA_DATA_PAGAMENTO + "?guia=" + guiaEmissaoDt.getNumeroGuiaCompleto());
								HttpResponse response = httpclient.execute(httpget);
								HttpEntity entity = response.getEntity();
								if (entity != null) {
									dataPagamento = EntityUtils.toString(entity);
								}
								//Atualiza pagamento da guia pelo número
								if( dataPagamento != null && dataPagamento.length() == 10 ) {
									
									Integer idGuiaStatus = GuiaStatusDt.PAGO;
									Date dataPagamentoDate = Funcoes.StringToDate(dataPagamento);
									
									if( guiaEmissaoNe.isGuiaCancelada(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao) ) {
										idGuiaStatus = GuiaStatusDt.CANCELADA_PAGA;
									}
									if( guiaEmissaoNe.isGuiaStatusAguardandoPagamento(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao) ) {
										idGuiaStatus = GuiaStatusDt.PAGO;
									}
									
									Long longDataPagamento = dataPagamentoDate.getTime();
									Date dataVencimentoGuia = guiaEmissaoNe.consultarDataVencimento(guiaEmissaoDt.getNumeroGuiaCompleto());
									Long longDataVencimento = dataVencimentoGuia.getTime();
									if( longDataVencimento < longDataPagamento ) {
										idGuiaStatus = GuiaStatusDt.PAGO_APOS_VENCIMENTO;
									}
									
									GuiaEmissaoDt guiaAntesAtualizar = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuia);
									boolean atualizado = obPersistencia.atualizarPagamento(guiaEmissaoDt.getNumeroGuiaCompleto(), idGuiaStatus, dataPagamentoDate);
									GuiaEmissaoDt guiaAposAtualizar = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuia);
									
									//**********
									//Gerar a movimentação
									if( atualizado ) {
										//log
										LogDt logDt = new LogDt("GuiaEmissao", guiaAposAtualizar.getId(), UsuarioDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.Alterar), guiaAntesAtualizar.getPropriedades(), guiaAposAtualizar.getPropriedades());
										new LogNe().salvar(logDt, obFabricaConexao);
										
										//Guia Complementar: Processo de alteração dos dados do processo
										guiaEmissaoNe.alterarDadosProcessoViaNumeroGuiaComplementar(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao);
									}
								}
							}
						}
					}
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(MalformedURLException ex) {
			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
			throw ex;
		}
		catch(IOException ex) {
			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
			throw ex;
		}
		catch(Exception e) {
			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return dataPagamento;
	}
}
