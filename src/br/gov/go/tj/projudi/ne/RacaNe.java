package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.RacaDt;
import br.gov.go.tj.projudi.ps.RacaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class RacaNe extends RacaNeGen{

//

//---------------------------------------------------------
	public  String Verificar(RacaDt dados ) {

		String stRetorno="";


		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            RacaPs obPersistencia = new RacaPs( obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
        }
        finally {
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
	}

}
