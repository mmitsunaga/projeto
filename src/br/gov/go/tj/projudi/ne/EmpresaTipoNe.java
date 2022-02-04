package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.EmpresaTipoDt;
import br.gov.go.tj.projudi.ps.EmpresaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class EmpresaTipoNe extends EmpresaTipoNeGen{

    private static final long serialVersionUID = -3304054472275254047L;

    //---------------------------------------------------------
	public  String Verificar(EmpresaTipoDt dados ) {

		String stRetorno="";

		//System.out.println("..neEmpresaTipoVerificar()");
		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EmpresaTipoPs obPersistencia = new EmpresaTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
