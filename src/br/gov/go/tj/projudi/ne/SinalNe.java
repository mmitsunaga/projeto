package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.SinalDt;
import br.gov.go.tj.projudi.ps.SinalPs;
import br.gov.go.tj.utils.FabricaConexao;

public class SinalNe extends SinalNeGen {

	private static final long serialVersionUID = -3290211270692492656L;

	public String Verificar(SinalDt dados) {

		String stRetorno = "";

		if (dados.getSinal().length() == 0)
			stRetorno += "O Campo Sinal é obrigatório.";
		if (dados.getSinal().length() > 60)
			stRetorno += "O Campo Sinal deve possuir no máximo 60 caracteres.";
		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		String tempList=null;
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				SinalPs obPersistencia = new SinalPs(obFabricaConexao.getConexao());
				tempList = obPersistencia.consultarDescricaoJSON( descricao, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

}
