package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;

public class GuiaInicialComplementarNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = 201677218332523L;
	
	/**
	 * Método para auxiliar na verificação se o processo tipo código é de um dos tipos de mandado de segurança.
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
	 * Método para auxiliar na verificação se o processo tipo código é de um dos tipos de mandado de divórcio.
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
	 * Método para auxiliar na verificação se o processo tipo código é do tipo Divórcio Litigiosa.
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
	 * Método para salvar a guia inicial complementar.
	 * Este método somente 
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
