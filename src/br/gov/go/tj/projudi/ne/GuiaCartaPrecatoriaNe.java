package br.gov.go.tj.projudi.ne;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class GuiaCartaPrecatoriaNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -7221348060448738799L;
	
	private GuiaEmissaoDt guiaEmissaoDt;
	private boolean emitirPendencia = true;
	
	/**
	 * Construtor
	 */
	public GuiaCartaPrecatoriaNe() {
		
	}
	
	
	/**
	 * Método para validar se pode emitir guia para a serventia atual.
	 * @param String codigoProcessoTipo
	 * @return boolean
	 * @throws Exception
	 * @author Fred
	 */
	public boolean validaAcessoEmissaoGuiaCartaPrecatoria(String codigoProcessoTipo){
		boolean retorno = false;
		
		switch(Funcoes.StringToInt(codigoProcessoTipo)) {
		
			case ProcessoTipoDt.CARTA_PRECATORIA :
			case ProcessoTipoDt.CARTA_PRECATORIA_CPC: 
			case ProcessoTipoDt.CARTA_PRECATORIA_CPP: {
				
				retorno = true; //Pode acessar
				break;
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Método para salvar Guia Emitida.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaItemDt
	 * @param boolean gerarPendencia
	 * 
	 * @throws Exception
	 */
	public void salvar(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, boolean gerarPendencia, String idUsuarioServentia) throws Exception{
		
		//***********
		//Verificação, pois a guia de carta precatória é somente para o primeiro grau.
		if( guiaEmissaoDt.getGuiaModeloDt() == null ) {
			GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
			guiaModeloDt.setFlagGrau("1");
			guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
		}
		else {
			if( guiaEmissaoDt.getGuiaModeloDt().getFlagGrau() != null ) {
				guiaEmissaoDt.getGuiaModeloDt().setFlagGrau("1");
			}
		}
		//************
		super.salvar(guiaEmissaoDt, listaGuiaItemDt, gerarPendencia, idUsuarioServentia);
		
	}
}
