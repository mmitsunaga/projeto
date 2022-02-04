package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;

public class GuiaInicialComplementarNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = 201677218332523L;
	
	/**
	 * M�todo para auxiliar na verifica��o se o processo tipo c�digo � de um dos tipos de mandado de seguran�a.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoMandado(int processoTipoCodigo) throws Exception {
		return new ProcessoTipoNe().isProcessoTipoMandado(processoTipoCodigo);
	}
	
	/**
	 * M�todo para auxiliar na verifica��o se o processo tipo c�digo � de um dos tipos de mandado de div�rcio.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoConversaoSeparacaoEmDivorcio(int processoTipoCodigo) throws Exception {
		return new ProcessoTipoNe().isProcessoTipoConversaoSeparacaoEmDivorcio(processoTipoCodigo);
	}
	
	/**
	 * M�todo para auxiliar na verifica��o se o processo tipo c�digo � do tipo Div�rcio Litigiosa.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoDivorcioLitigiosaLE(int processoTipoCodigo) throws Exception {
		return new ProcessoTipoNe().isProcessoTipoDivorcioLitigiosaLE(processoTipoCodigo);
	}
	
	/**
	 * M�todo para salvar a guia inicial complementar.
	 * Este m�todo somente 
	 */
	public void salvar(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, boolean gerarPendencia, String idUsuarioServentia) throws Exception {
		if( this.isGuiaZeradaOuNegativa() ) {
			guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.PAGO);
		}
		if( guiaEmissaoDt.getId_Processo() != null && !guiaEmissaoDt.getId_Processo().isEmpty() ) {
			//guiaEmissaoDtPrincipal.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
			ProcessoDt processoDt = new ProcessoNe().consultarId(guiaEmissaoDt.getId_Processo());
			if( processoDt != null ) {
				guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
			}
		}
		super.salvar(guiaEmissaoDt, listaGuiaItemDt, gerarPendencia, idUsuarioServentia);
	}
	
}
