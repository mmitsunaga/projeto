package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.projudi.ps.EventoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.ps.ProjetoPs;

//---------------------------------------------------------
public class ProjetoNe extends ProjetoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -1591097797350322796L;

    //---------------------------------------------------------
	public  String Verificar(ProjetoDt dados ) {

		String stRetorno="";

		if (dados.getProjeto().length()==0)
			stRetorno += "O Campo Projeto é obrigatório.";
		//System.out.println("..neProjetoVerificar()");
		return stRetorno;

	}
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProjetoPs obPersistencia = new ProjetoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON( descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
}
