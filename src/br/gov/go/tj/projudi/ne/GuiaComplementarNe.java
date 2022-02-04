package br.gov.go.tj.projudi.ne;

import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.utils.Funcoes;

public class GuiaComplementarNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -525753789225003302L;
	
	private GuiaEmissaoDt guiaEmissaoDt;
	private boolean emitirPendencia = true; //flag utilizado na alteração de dados de processo. (true = gera pendência, false = não gerar pendência)
	private int quantidadeRequeridos = 0;
	private int processoTipoCodigo = 0;
	
	public GuiaComplementarNe() {
	}
	
	public int getQuantidadeRequeridos() {
		return quantidadeRequeridos;
	}

	public void setQuantidadeRequeridos(int quantidadeRequeridos) {
		this.quantidadeRequeridos = quantidadeRequeridos;
	}

	public int getProcessoTipoCodigo() {
		return processoTipoCodigo;
	}

	public void setProcessoTipoCodigo(int processoTipoCodigo) {
		this.processoTipoCodigo = processoTipoCodigo;
	}

	/**
	 * Método para retornar se deve ou não emitir pendência.
	 * @return boolean
	 */
	public boolean isEmitirPendencia() {
		return this.emitirPendencia;
	}
	
	/**
	 * Método para verificar se a guia foi emitida e gerou id.
	 * @return boolean
	 */
	public boolean isGuiaEmitidaComSucesso() {
		boolean retorno = false;
		
		if( guiaEmissaoDt != null 
			&& 
			guiaEmissaoDt.getListaGuiaItemDt() != null 
			&& 
			guiaEmissaoDt.getListaGuiaItemDt().size() > 0
			&&
			guiaEmissaoDt.getId() != null
			&&
			guiaEmissaoDt.getId().trim().length() > 0 ) {
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para calcular os itens da guia.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @param Map valoresReferenciaCalculo
	 * @param String reducao50Porcento
	 * @param String citacaoHoraCerta
	 * @param String foraHorarioNormal
	 * 
	 * @return List listaGuiaItemDt
	 * 
	 * @throws Exception
	 */
	public List calcularItensGuia(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaCustaModeloDt, Map valoresReferenciaCalculo, String reducao50Porcento, String citacaoHoraCerta, String foraHorarioNormal) throws Exception {
		List listaGuiaItemDt = null;
		

		
		if( listaGuiaCustaModeloDt != null && listaGuiaCustaModeloDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
				CustaDt custaDt = ((GuiaCustaModeloDt) listaGuiaCustaModeloDt.get(i)).getCustaDt();
				
				switch( Funcoes.StringToInt(custaDt.getId()) ) {
					
					case CustaDt.ATUALIZACAO_MOEDA_NACIONAL: {
						guiaEmissaoDt.setAtualizacaoValorNominalQuantidade("1");
						guiaEmissaoDt.setContadorQuantidade("1");
						guiaEmissaoDt.setCustasQuantidade("1");
						guiaEmissaoDt.setRetificacaoCalculosQuantidade("1");
						guiaEmissaoDt.setTransformacaoMoedaQuantidade("1");
						guiaEmissaoDt.setRetificacaoCustasQuantidade("1");
						guiaEmissaoDt.setDepositarioPublico("1");
						guiaEmissaoDt.setDepositarioPublicoBemImovel("1");
						guiaEmissaoDt.setAfixacaoEdital("1");
						guiaEmissaoDt.setLeilaoQuantidade("1");
						guiaEmissaoDt.setDistribuidorQuantidade("1");
						guiaEmissaoDt.setDistribuidorQuantidade("1");
						break;
					}
					
					case CustaDt.TAXA_PROTOCOLO : {
						guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
						break;
					}
					
					case CustaDt.PROCESSOS_EXEC_SENTENCA_OU_TIT_EXTRA : {
						guiaEmissaoDt.setEscrivaniaQuantidade("1");
						break;
					}
					
					case CustaDt.DESPESA_POSTAL : {
						guiaEmissaoDt.setCorreioQuantidade("1");
						break;
					}
					
					case CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA : {
						guiaEmissaoDt.setAvaliadorQuantidade("1");
						break;
					}
					
					case CustaDt.CUSTA_PARTIDOR : {
						guiaEmissaoDt.setPartidorQuantidade("1");
						break;
					}
					
					case CustaDt.CUSTA_PENHORA : {
						guiaEmissaoDt.setPenhoraQuantidade("1");
						break;
					}
					
				}
			}
		}
		
		listaGuiaItemDt = super.calcularItensGuia(guiaEmissaoDt, listaGuiaCustaModeloDt, valoresReferenciaCalculo, reducao50Porcento, citacaoHoraCerta);
	
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para auxiliar na verificação se o processo tipo código é de um dos tipos de mandado de segurança.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isProcessoTipoMandado(int processoTipoCodigo) throws Exception {
		return new ProcessoTipoNe().isProcessoTipoMandado(processoTipoCodigo);
	}
}
