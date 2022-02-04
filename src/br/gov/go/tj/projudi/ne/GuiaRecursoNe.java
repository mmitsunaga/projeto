package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.ps.GuiaRecursoPs;
import br.gov.go.tj.utils.FabricaConexao;


public class GuiaRecursoNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = 6725378441801009745L;
	
	protected GuiaRecursoPs obPersistencia;
	
	
	/**
	 * Método para validar se advogado pode emitir esta guia.
	 * Caso o processo esteja em uma serventia de juizado especial, não pode emitir esta guia, somente pode de 
	 * recurto inominado.
	 * Retorna true se puder acessar e false caso não possa acessar.
	 * @param String idServentia
	 * @return boolean
	 * @throws Exception
	 * @author Fred
	 */
	public boolean validaAcessoEmissaoGuia(String idServentia) throws Exception {
		boolean retorno = false;
		
		FabricaConexao obFabricaConexao = null;
		
		try{
			if( idServentia != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaRecursoPs obPersistencia = new GuiaRecursoPs(obFabricaConexao.getConexao());
				
				retorno = obPersistencia.validaAcessoEmissaoGuia(idServentia);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
}
