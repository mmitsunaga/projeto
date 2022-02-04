package br.gov.go.tj.projudi.util;

import br.gov.go.tj.projudi.ne.PonteiroLogNe;
import br.gov.go.tj.utils.FabricaConexao;

public class DistribuicaoProcesso {

	private static DistribuicaoProcesso objeto = null;

	private DistribuicaoProcesso(){
		
	}

	static public DistribuicaoProcesso getInstance(){
		if (objeto == null) objeto = new DistribuicaoProcesso();

		return objeto;
	}

	

	public  String getDistribuicao(String id_areadistribuicao, String id_serventia) throws Exception{
		String stRetorno = "";
		//stRetorno = new ServentiaNe().consultarServentiasAreaDistribuicao(id_areadistribuicao,"");		    
		stRetorno = new PonteiroLogNe().consultarServentiasAreaDistribuicao(id_areadistribuicao,id_serventia);

		return stRetorno;
	}
	
	public  String getDistribuicao(String id_areadistribuicao) throws Exception{
		String stRetorno = "";
		//stRetorno = new ServentiaNe().consultarServentiasAreaDistribuicao(id_areadistribuicao,"");		    
		stRetorno = new PonteiroLogNe().consultarServentiasAreaDistribuicao(id_areadistribuicao);

		return stRetorno;
	}
	
	public  String getDistribuicaoServentiaCargo(String id_serventia, String id_area_distribuicao) throws Exception{				

		return new PonteiroLogNe().consultarServentiaCargoServentias(id_serventia, "", id_area_distribuicao);
	}
	
	public  String getDistribuicaoServentiaCargo(String id_serventia, String id_serv_cargo, String id_area_distribuicao) throws Exception{				

		return new PonteiroLogNe().consultarServentiaCargoServentias(id_serventia, id_serv_cargo, id_area_distribuicao);
	}
	
	public String getDistribuicao(String id_areadistribuicao, String id_serventia, FabricaConexao obFabricaConexao) throws Exception {
		String stRetorno = "";

		stRetorno = new PonteiroLogNe().consultarServentiasAreaDistribuicao(id_areadistribuicao,id_serventia, obFabricaConexao);

		return stRetorno;
	}	
	
}
