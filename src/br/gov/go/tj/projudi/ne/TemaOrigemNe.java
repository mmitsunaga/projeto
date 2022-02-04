package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.TemaOrigemDt;
import br.gov.go.tj.projudi.ps.TemaOrigemPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class TemaOrigemNe extends TemaOrigemNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -8512574194536364032L;

	//---------------------------------------------------------
	public  String Verificar(TemaOrigemDt dados ) {

		String stRetorno="";
		if(dados.getTemaOrigem() == null || dados.getTemaOrigem().equals("")) {
			stRetorno = "O campo Origem é obrigatório. \n";
		}
		if(dados.getTemaOrigemCodigo() == null || dados.getTemaOrigemCodigo().equals("")) {
			stRetorno += "O campo Código é obrigatório.";
		}

		return stRetorno;

	}
	
	/**
	 * Consulta um Tema Origem pelo seu código ou pela descrição
	 * @param codigo
	 * @param descricao
	 * @return
	 * @throws Exception
	 */
	public TemaOrigemDt consultarPorCodigoOuDescricao(Integer codigo, String descricao) throws Exception{
		TemaOrigemDt temaOrigemDt = null;		
        FabricaConexao obFabricaConexao = null;         
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            TemaOrigemPs obPersistencia = new TemaOrigemPs(obFabricaConexao.getConexao());
            temaOrigemDt = obPersistencia.consultarPorCodigoOuDescricao(codigo, descricao);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return temaOrigemDt;
	}

}
