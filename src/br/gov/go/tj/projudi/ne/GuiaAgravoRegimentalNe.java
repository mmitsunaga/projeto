package br.gov.go.tj.projudi.ne;

public class GuiaAgravoRegimentalNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -637505854942692552L;

	/**
	 * Método para validar se pode ter acesso ao para emitir esta guia.
	 * @param String id_serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validaAcessoEmissaoGuiaAgravoRegimental(String id_serventia) throws Exception {
		boolean retorno = false;
		
		if( id_serventia != null ) {
			retorno = isProcessoSegundoGrau(id_serventia);
		}
		
		return retorno;
	}
}
