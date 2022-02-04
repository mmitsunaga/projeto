package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoCompletaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoGuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteGuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ps.GuiaEmissaoPs;
import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaProcessoTJGO;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.InterfaceJasper;

public class GuiaEmissaoConsultaNe extends GuiaEmissaoNe {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5931116266577191254L;

	public ProcessoGuiaEmissaoConsultaDt obtenhaProcesso(NumeroProcessoDt numeroProcessoDt) throws MensagemException, Exception {
		
		ProcessoGuiaEmissaoConsultaDt processoGuiaEmissaoConsultaDt = new ProcessoGuiaEmissaoConsultaProjudiNe().obtenhaProcesso(numeroProcessoDt);
		
		if (processoGuiaEmissaoConsultaDt != null) return processoGuiaEmissaoConsultaDt;
		
		processoGuiaEmissaoConsultaDt = new ProcessoGuiaEmissaoConsultaSPGNe().obtenhaProcesso(numeroProcessoDt);
		
		if (processoGuiaEmissaoConsultaDt != null) return processoGuiaEmissaoConsultaDt;
		
		processoGuiaEmissaoConsultaDt = new ProcessoGuiaEmissaoConsultaPJENe().obtenhaProcesso(numeroProcessoDt);
		
		return processoGuiaEmissaoConsultaDt;
	}

	public void obtenhaGuiasEmitidas(GuiaEmissaoConsultaDt guiaEmissaoConsultaDt) throws Exception {
		 List listaId_GuiaTipoConsideradas = super.consultarListaId_GuiaTipo(null);
		 
		 guiaEmissaoConsultaDt.getListaDeGuiasEmitidas().clear();
		 
		 if (guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().equals(EnumSistemaProcessoTJGO.projudi)) {
			 List listaGuiaEmissao = this.consultarGuiaEmissao(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId(), listaId_GuiaTipoConsideradas);
			 
			 if (listaGuiaEmissao != null) {
				 for(Object guiaEmissaoDtObject : listaGuiaEmissao) {
					 GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)guiaEmissaoDtObject;
					 guiaEmissaoConsultaDt.adicioneGuiaEmitida(new GuiaEmissaoCompletaDt(guiaEmissaoDt));
				 }
			 }			 
			 
		 } else {
			 
		 }
	}
	
	/**
	 * Metodo para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia.
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissao(String idProcesso, List listaId_GuiaTipo) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissao(idProcesso, listaId_GuiaTipo);
			
			if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
				for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
					GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
					
					if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
						guiaEmissaoDt.setGuiaTipo( this.consultarGuiaTipo( null, guiaEmissaoDt.getId_GuiaTipo() ) );
					}
				}
			}
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public List consultarGuiaItens(String idGuiaEmissao, String idGuiaTipo) throws Exception{
		return new GuiaEmissaoNe().consultarGuiaItens(idGuiaEmissao, idGuiaTipo);
	}
	
	public GuiaEmissaoCompletaDt consultarGuiaEmissaoCompleta(String idGuiaEmissao) throws Exception{
		GuiaEmissaoDt guiaEmissaoDt = super.consultarGuiaEmissao(idGuiaEmissao);
		return new GuiaEmissaoCompletaDt(guiaEmissaoDt);
	}
	
	public void obtenhaPartesProcesso(GuiaEmissaoConsultaDt guiaEmissaoConsultaDt, int nivelAcessoUsuario) throws Exception{
		 if (guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().equals(EnumSistemaProcessoTJGO.projudi)) {			 			 
			 
			 ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId());
			 
			 if(processoDt.getListaPolosAtivos() != null && processoDt.getListaPolosAtivos().size() == 0 && processoDt.getListaPolosPassivos() != null && processoDt.getListaPolosPassivos().size() == 0 ) 
			 {
				 processoDt = new ProcessoNe().consultarDadosProcesso(guiaEmissaoConsultaDt.getId_Processo(), guiaEmissaoConsultaDt.getUsuarioDt(), false, false , nivelAcessoUsuario );
			 }
			 
			 if (processoDt.getListaPolosAtivos() != null) {
				 for(Object promoventeObj : processoDt.getListaPolosAtivos()) {
					 ProcessoParteDt promovente = (ProcessoParteDt) promoventeObj;
					 if (promovente != null) guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().addPromovente(new ProcessoParteGuiaEmissaoConsultaDt(promovente));
				 }
			 }
			 
			 if (processoDt.getListaPolosPassivos() != null) {
				 for(Object promovidoObj : processoDt.getListaPolosPassivos()) {
					 ProcessoParteDt promovido = (ProcessoParteDt) promovidoObj;
					 if (promovido != null) guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().addPromovido(new ProcessoParteGuiaEmissaoConsultaDt(promovido));
				 }	 
			 } 
			 			 
			 if (processoDt.getListaOutrasPartes() != null) {
				 for(Object outrasPartesObj : processoDt.getListaOutrasPartes()) {
					 ProcessoParteDt outrasPartes = (ProcessoParteDt) outrasPartesObj;
					 if (outrasPartes != null) guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().addOutrasPartes(new ProcessoParteGuiaEmissaoConsultaDt(outrasPartes));
				 }	 
			 }
			 
			 List<ProcessoParteDt> listaPartesLitisconsorteAtivoPassivo = this.consultarPartesLitisconsorteAtivoPassivo(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId());
			 
			 for(Object litisconsorteAtivoPassivoObj : listaPartesLitisconsorteAtivoPassivo) {
				 ProcessoParteDt litisconsorteAtivoPassivo = (ProcessoParteDt) litisconsorteAtivoPassivoObj;
				 if (litisconsorteAtivoPassivo != null) guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().addPromovido(new ProcessoParteGuiaEmissaoConsultaDt(litisconsorteAtivoPassivo));
			 }
			 
		 } else {
			 
		 }
	}
	
	/**
	 * Método para setar o nome do apelante e apelado através da lista de requerentes e requeridos presente neste Dt.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void setNomeApelanteNomeApelado(GuiaEmissaoCompletaDt guiaEmissaoDt){
		
		if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 && guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
			if( guiaEmissaoDt.getListaRequerentes() != null && guiaEmissaoDt.getListaRequerentes().size() > 0 ) {
				for(ProcessoParteGuiaEmissaoConsultaDt processoParteDt : guiaEmissaoDt.getListaRequerentes()) {
					if( guiaEmissaoDt.getId_Apelante().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelante(processoParteDt.getNome());
					}
					if( guiaEmissaoDt.getId_Apelado().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelado(processoParteDt.getNome());
					}
				}
			}
			if( guiaEmissaoDt.getListaRequeridos() != null && guiaEmissaoDt.getListaRequeridos().size() > 0 ) {
				for(ProcessoParteGuiaEmissaoConsultaDt processoParteDt : guiaEmissaoDt.getListaRequeridos()) {
					if( guiaEmissaoDt.getId_Apelante().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelante(processoParteDt.getNome());
					}
					if( guiaEmissaoDt.getId_Apelado().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelado(processoParteDt.getNome());
					}
				}
			}
		}
				
	}
	
	/**
	 * Método para retornar boolean que definir se botão de impressão e de pagamento deve aparecer ou não.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return Boolean
	 */
	public Boolean visualizarBotaoImpressaoBotaoPagamento(GuiaEmissaoCompletaDt guiaEmissaoDt) {
		Boolean retorno = false;
		
		if( guiaEmissaoDt != null ) {
			if( guiaEmissaoDt.getId_GuiaStatus() != null && 
				(guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.AGUARDANDO_PAGAMENTO)) ||
				 guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.ESTORNO_BANCARIO)))) {
				retorno = true;
			}
			//Caso seja guia inicial
			if( guiaEmissaoDt.getId_GuiaStatus() == null ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para imprimir guia em PDF.
	 * @param String stCaminho
	 * @param String totalGuia
	 * @param String areaDistribuicao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String Id_GuiaTipo
	 * @param String guiaTipo
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] imprimirGuia(String stCaminho, String totalGuia, String areaDistribuicao, GuiaEmissaoCompletaDt guiaEmissaoDt, String idGuiaTipo, String guiaTipo) throws Exception {
		byte[] temp = null;
		ByteArrayOutputStream baos = null;
		try{
			//Geracao do Codigo de Barras
			String codigoBarra = super.gerarCodigoBarra(totalGuia, guiaEmissaoDt.getNumeroGuiaCompleto(), guiaEmissaoDt.getDataVencimento(), idGuiaTipo);
			
			InterfaceJasper ei = null;
			if( guiaEmissaoDt.getListaGuiaItemDt() != null && guiaEmissaoDt.getListaGuiaItemDt().size() > 0 ) {
				List lista = new ArrayList();
				lista.add( guiaEmissaoDt.getListaGuiaItemDt().get(0) );
				ei = new InterfaceJasper(lista);
			}
			
			String pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "DUAJ_Inicial.jasper";
			
			//Parï¿½metros do relatÃ³rio
			Map parametros = new HashMap();
			
			parametros.put("caminhoLogo", stCaminho + "imagens" + File.separator + "logoEstadoGoias02.jpg" );
			if( guiaEmissaoDt.getGuiaTipo() != null && guiaEmissaoDt.getGuiaTipo().length() > 0 ) {
				parametros.put("titulo", guiaEmissaoDt.getGuiaTipo() );
			}
			else {
				parametros.put("titulo", guiaTipo );
			}
			parametros.put("total", totalGuia );
			parametros.put("Numero", Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) );
			parametros.put("Emissao", Funcoes.dateToStringSoData(new Date()) );
			parametros.put("Vencimento", Funcoes.TelaData(guiaEmissaoDt.getDataVencimento()) );
			if( guiaEmissaoDt.getApelante() != null && guiaEmissaoDt.getApelante().length() > 0 && guiaEmissaoDt.getApelado() != null && guiaEmissaoDt.getApelado().length() > 0 ) {
				parametros.put("Requerente", guiaEmissaoDt.getApelante() + " (100%)" );
				parametros.put("Requerido", guiaEmissaoDt.getApelado() );
			}
			else {
				if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 && guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
					
					this.setNomeApelanteNomeApelado(guiaEmissaoDt);
					
					parametros.put("Requerente", guiaEmissaoDt.getApelante() + " (100%)");
					parametros.put("Requerido", guiaEmissaoDt.getApelado() );
				}
				else {
					parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
					parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
				}
			}
			if( guiaEmissaoDt.getRateioCodigo() != null && guiaEmissaoDt.getRateioCodigo().length() > 0 ) {
				if( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalNe.RATEIO_100_REQUERENTE ) {
					if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 &&
						guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
						
						this.setNomeApelanteNomeApelado(guiaEmissaoDt);
						
						parametros.put("Requerente", guiaEmissaoDt.getApelante() + " (100%)");
						parametros.put("Requerido", guiaEmissaoDt.getApelado() );
					}
					else {
						if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelado() == null ) {
							parametros.put("Requerente", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_Apelante() ) + " (100%)");
							parametros.put("Requerido", "" );
						}
						else {
							parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() + " (100%)");
							parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
						}
					}
				}
				else {
					if( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalNe.RATEIO_100_REQUERIDO ) {
						if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 &&
							guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
							
							this.setNomeApelanteNomeApelado(guiaEmissaoDt);
							
							parametros.put("Requerente", guiaEmissaoDt.getApelante() );
							parametros.put("Requerido", guiaEmissaoDt.getApelado() + " (100%)");
						}
						else {
							if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelado() == null ) {
								parametros.put("Requerente", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_Apelante() ) );
								parametros.put("Requerido", "");
							}
							else {
								parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
								parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() + " (100%)");
							}
						}
					}
					else {
						parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() + " (50%)");
						parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() + " (50%)");
					}
				}
				if( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalNe.RATEIO_VARIAVEL ) {
					if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
						if( isParteRequerente(guiaEmissaoDt.getId_ProcessoParteResponsavelGuia(), guiaEmissaoDt.getListaRequerentes()) ) {
							parametros.put("Requerente", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) + " (100%)" );
							parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
						}
						if( isParteRequerido(guiaEmissaoDt.getId_ProcessoParteResponsavelGuia(), guiaEmissaoDt.getListaRequeridos()) ) {
							parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
							parametros.put("Requerido", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) + " (100%)" );
						}
					}
					else {
						parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
						parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
					}
				}
			}
			parametros.put("ComarcaCodigo", guiaEmissaoDt.getComarcaCodigo());
			parametros.put("Comarca", guiaEmissaoDt.getComarca() );
			parametros.put("AreaDistribuicao", areaDistribuicao );
			parametros.put("ProcessoTipoCodigo","");
			parametros.put("ProcessoTipo", guiaEmissaoDt.getProcessoTipo());
			if( guiaEmissaoDt.getGuiaModeloDt() != null && guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) && guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
				parametros.put("ProcessoNumero", "Vinculado: " + guiaEmissaoDt.getNumeroProcessoDependente());
			}
			else {
				parametros.put("ProcessoNumero", guiaEmissaoDt.getNumeroProcesso());
			}
			parametros.put("ProcessoValor", guiaEmissaoDt.getValorAcao() );
			parametros.put("CodigoBarraDigito", Funcoes.FormatarCodigoBarraGuiaDigito(codigoBarra, this.gerarDigitoCodigoBarra(codigoBarra.substring(0,11)), this.gerarDigitoCodigoBarra(codigoBarra.substring(11,22)), this.gerarDigitoCodigoBarra(codigoBarra.substring(22,33)), this.gerarDigitoCodigoBarra(codigoBarra.substring(33))));
			parametros.put("CodigoBarra", codigoBarra );
			
			
			List listaGuiaItemDtAux = guiaEmissaoDt.getListaGuiaItemDt();
			if( listaGuiaItemDtAux != null && listaGuiaItemDtAux.size() > 0 ) {
				for(int i = 1; i <= listaGuiaItemDtAux.size(); i++ ) {
					GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(i-1);
					
					String descricaoItemGuia = guiaItemDt.getCustaDt().getArrecadacaoCusta();
					
					parametros.put("CodigoItemGuia" + i			, guiaItemDt.getCustaDt().getCodigoArrecadacao() );
					parametros.put("DescricaoItemGuia" + i		, descricaoItemGuia );
					parametros.put("QuantidadeItemGuia" + i		, guiaItemDt.getQuantidade() );
					parametros.put("ValorItemGuia" + i			, Funcoes.FormatarDecimal( guiaItemDt.getValorCalculado() ) );
				}
			}
			
			JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);

			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();

			temp = baos.toByteArray();
			baos.close();
		}
		catch(Exception e) {
			try{if (baos!=null) baos.close();  }catch(Exception e2) {		}
		}
		
		return temp;
	}
	
	public ServentiaDt consultarServentiaProcesso(GuiaEmissaoConsultaDt guiaEmissaoConsultaDt) throws Exception{
		if (guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().equals(EnumSistemaProcessoTJGO.projudi)) {			 		 
			 return super.consultarServentiaProcesso(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getServentiaCodigo());
		} else {
			 return null;
		}
	}
	
	public ComarcaDt consultarComarca(GuiaEmissaoConsultaDt guiaEmissaoConsultaDt, String id_comarca) throws Exception{
		if (guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().equals(EnumSistemaProcessoTJGO.projudi)) {			 		 
			 return super.consultarComarca(id_comarca);
		} else {
			 return null;
		}
	}
	
}
