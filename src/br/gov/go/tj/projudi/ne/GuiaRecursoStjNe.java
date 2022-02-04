package br.gov.go.tj.projudi.ne;

public class GuiaRecursoStjNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -1285124931032048644L;

	/**
	 * Método para validar se pode ter acesso ao para emitir esta guia.
	 * @param String id_serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validaAcessoEmissaoGuiaRecursoSTJ(String id_serventia) throws Exception{
		boolean retorno = false;
		
		if( id_serventia != null ) {
			retorno = isProcessoSegundoGrau(id_serventia);
		}
		
		return retorno;
	}	
}
