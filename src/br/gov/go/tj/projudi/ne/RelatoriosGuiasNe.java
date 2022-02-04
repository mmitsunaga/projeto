package br.gov.go.tj.projudi.ne;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.utils.MensagemException;

public class RelatoriosGuiasNe extends Negocio {

	private static final long serialVersionUID = -7767556903347370484L;
	
	/**
	 * ATENÇÃO: Método para uso local até achar uma forma de colocar no sistema.
	 * Gerar Relatorio ArquivoLocal Processo Com GuiaInicial Paga Sem Guia Complementar e Gerar Estimativa Valor Guia Complementar Hoje
	 * 
	 * Criado com base na ocorrencia 2021/1696.
	 * 
	 * @throws Exception
	 */
	public void gerarRelatorioEstimativaValorGuiaComplementarHoje() throws Exception {
		/**
		 * 
		 * SQL BASE 
		 * 
		 select proc.proc_numero || '-' || TRIM(TO_CHAR(proc.digito_verificador, '00')) || '.' || proc.ano || '#' ||
ps.proc_status || '#' ||
proc.valor || '#' ||
serv.serv || '#' ||
guia.numero_guia_completo || '#' || 
modelo.guia_tipo || '#' || 
(select sum(valor_calculado) from projudi.guia_item where id_guia_emis = guia.id_guia_emis)  || '#' ||
guia.valor_acao || '#' ||
'xxxx' || '#' ||
proc.id_proc

from projudi.guia_emis guia
inner join projudi.proc proc on proc.id_proc = guia.id_proc
inner join projudi.serv serv on serv.id_serv = proc.id_serv
inner join projudi.proc_status ps on proc.id_proc_status = ps.id_proc_status
inner join projudi.view_guia_modelo modelo on modelo.id_guia_modelo = guia.id_guia_modelo

where proc.valor > guia.valor_acao --com o valor da causa superior ao informado na guia de custas iniciais

and proc.data_recebimento >= '01/01/2017' --processos protocolados a partir do ano de 2017
and proc.data_recebimento < '01/05/2021'

and modelo.id_guia_tipo in (1, 22) --guia de custas iniciais paga
and guia.id_guia_status in (3,4,5,11,21,26) --guia de custas iniciais paga

and not exists ( --Deve ser excluído do relatório, os processos que possuir guia complementar expedida.
    select 1 
    from projudi.guia_emis guia2
    inner join projudi.guia_modelo modelo2 on modelo2.id_guia_modelo = guia2.id_guia_modelo
    where modelo2.id_guia_tipo in (2, 40)
    and guia2.id_proc = proc.id_proc
)

and proc.id_proc_tipo not in (42,157,123,1260,1261) --Marcelo mandou msg zap pedindo para retirar processos carta precatorias
and ( TO_CHAR(guia.numero_guia_completo) like '%50' or TO_CHAR(guia.numero_guia_completo) like '%09' ) --Marcelo mandou msg informando que achou guias com 06 e pediu para deixar somente as 09 e 50

and guia.tipo_guia_desconto_parcelament is null

order by serv.serv, proc.valor
		 */
		FileWriter arquivoSaida = null;
		BufferedReader arquivoEntrada = null;
		
		int cont = 0;
		String str = "";
		
		try {
			GuiaComplementarNe guiaComplementarNe = new GuiaComplementarNe();
			
			String caminhoDir = "C:"+File.separator+"TJGO"+File.separator+"relatorios"+File.separator+"ocorrencia-2021-1696"+File.separator;
			
			arquivoSaida = new FileWriter(new File(caminhoDir + "ocorrencia-2021-1696.csv"), false);
			arquivoEntrada = new BufferedReader(new FileReader(caminhoDir + "ocorrencia-2021-1696.txt"));
			
			while (arquivoEntrada.ready()) {
				try {
					str = arquivoEntrada.readLine();
					
					if( str == null || str.length() == 0 ) {
						return;
					}
					
					String dados[] = str.split("#");
					
					boolean isProcessoSegundoGrau = false; //Default primeiro grau
					String tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU; //Default complementar de primeiro grau
					String GRAU_ESCOLHIDO = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU;
					
					List listaItensGuia 	= null;
					
					//consulta processo
					ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(dados[9]);
					//consulta guia
					GuiaEmissaoDt guiaEmissaoDt = guiaComplementarNe.consultarGuiaEmissaoNumeroGuia(dados[4]);
					
					if( processoDt != null && guiaEmissaoDt != null ) {
						
						guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
						
						Map valoresReferenciaCalculo = new HashMap();
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				processoDt.getValor());
						valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				processoDt.getValor());
						if( processoDt.getValor().length() == 0 ) {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0");
						}
						else {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, processoDt.getValor());
						}
						if( guiaComplementarNe.isProcessoTipoMandado(Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
							valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
							if( guiaEmissaoDt.getNumeroImpetrantes() != null && guiaEmissaoDt.getNumeroImpetrantes().length() > 0 ) {
								valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
							}
							if( guiaEmissaoDt.getProcessoTipoCodigo() != null && Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO ) {
								valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO);
							}
						}
						
						ServentiaDt serventiaDt = null;
						ComarcaDt comarcaDt = null;
						if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
							serventiaDt = guiaComplementarNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
							if( serventiaDt != null ) {
								comarcaDt = guiaComplementarNe.consultarComarca(serventiaDt.getId_Comarca());
								processoDt.setComarca(serventiaDt.getComarca());
								
								//Verifica se processo estï¿½ no segundo grau
								isProcessoSegundoGrau = guiaComplementarNe.isProcessoSegundoGrau(serventiaDt.getId());
								if( isProcessoSegundoGrau ) {
									tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU;
									GRAU_ESCOLHIDO = GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU;
								}
								else {
									tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU;
									GRAU_ESCOLHIDO = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU;
								}
							}
						}
						
						//Consulta modelo
						if( isProcessoSegundoGrau ) {
							guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, guiaEmissaoDt.getId_ProcessoTipo()));
						}
						else {
							guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo()));
						}
						
						if( guiaEmissaoDt.getGuiaModeloDt() == null ) {
							//throw new MensagemException("Modelo/Template da guia não encontrado. Pode ser que esta classe não tenha cálculo homologado(Guia Inicial).");
							continue;
						}
						
						//Consulta itens
						if( isProcessoSegundoGrau ) {
							listaItensGuia = guiaComplementarNe.consultarItensGuiaNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
						}
						else {
							listaItensGuia = guiaComplementarNe.consultarItensGuiaNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
						}
						
						if( listaItensGuia == null ) {
							listaItensGuia = new ArrayList();
						}
						
						guiaComplementarNe.adicionarItemCautelarContenciosoParaGuiaComplementar(GRAU_ESCOLHIDO, listaItensGuia, guiaEmissaoDt);
						
						
						List<String> listaIdGuiaTipoConsultar = new ArrayList<String>();
						
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU);
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU);
						
						
						//Tem guia inicial ou complementar inicial?
						List<GuiaEmissaoDt> listaGuiaEmissaoDtPagas = guiaComplementarNe.consultarGuiaEmissaoPagaParaGuiaComplementar(null, processoDt.getId(), listaIdGuiaTipoConsultar);
						
						if( listaGuiaEmissaoDtPagas != null ) {
							GuiaItemNe guiaItemNe = new GuiaItemNe();
							
							for(int i = 0; i < listaGuiaEmissaoDtPagas.size(); i++) {
								GuiaEmissaoDt guiaEmissaoDtAux = (GuiaEmissaoDt) listaGuiaEmissaoDtPagas.get(i);
								
								List listaAux = guiaItemNe.consultaItensGuiaGuiaComplementar(null, guiaEmissaoDtAux);
								if( guiaEmissaoDt.getListaGuiaItemDt() != null ) {
									if( listaAux != null ) {
										guiaEmissaoDt.getListaGuiaItemDt().addAll( listaAux );
									}
								}
								else {
									if( listaAux != null ) {
										guiaEmissaoDt.setListaGuiaItemDt( listaAux );
									}
								}
							}
						}
						
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = guiaComplementarNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null, null);
						
						
						List listaGuiaItemDtLocomocao = new ArrayList();
						
						//Calcular a guia Complementar
						guiaComplementarNe.recalcularGuiaComplementar(guiaEmissaoDt, listaGuiaItemDt, listaGuiaItemDtLocomocao);
						guiaComplementarNe.recalcularTotalGuia(listaGuiaItemDt);
						
						//System.out.println(guiaComplementarNe.getGuiaCalculoNe().getTotalGuia());
						System.out.println(++cont);
						arquivoSaida.write(str.replace("xxxx", String.valueOf(guiaComplementarNe.getGuiaCalculoNe().getTotalGuia())) + "\n");
					
					}
				}
				catch(Exception ee) {
					System.out.println(ee);
					arquivoSaida.write(str.replace("xxxx", ee.getMessage())+ "\n");
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			if( arquivoSaida != null ) {
				arquivoSaida.close();
			}
			if( arquivoEntrada != null ) {
				arquivoEntrada.close();
			}
		}
		
	}

}
