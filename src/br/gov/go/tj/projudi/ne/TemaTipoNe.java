package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.TemaTipoDt;
import br.gov.go.tj.projudi.ps.TemaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class TemaTipoNe extends TemaTipoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -1951499526872806990L;

	//---------------------------------------------------------
	public  String Verificar(TemaTipoDt dados ) {

		String stRetorno="";

		if (dados.getTemaTipoCodigo().length()==0)
			stRetorno += "O Campo TemaTipoCodigo é obrigatório.";
		if (dados.getTemaTipo().length()==0)
			stRetorno += "O Campo TemaTipo é obrigatório.";

		return stRetorno;

	}

	
	/**
	 * Consulta o Tipo do Tema pelo seu código ou pelo campo tipo CNJ
	 * @param codigo
	 * @param descricao
	 * @return
	 * @throws Exception 
	 */
	public TemaTipoDt consultarPorCodigoOuTipoCNJ(Integer codigo, String tipoCNJ) throws Exception {
		TemaTipoDt temaTipoDt = null;		
        FabricaConexao obFabricaConexao = null;         
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            TemaTipoPs obPersistencia = new TemaTipoPs(obFabricaConexao.getConexao());
            temaTipoDt = obPersistencia.consultarPorCodigoOuTipoCNJ(codigo, tipoCNJ);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return temaTipoDt;
	}
	
}
