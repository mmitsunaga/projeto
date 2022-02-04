package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.CertidaoTipoDt;
import br.gov.go.tj.projudi.ps.CertidaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class CertidaoTipoNe extends CertidaoTipoNeGen{


    private static final long serialVersionUID = 5500818119080440164L;

	public  String Verificar(CertidaoTipoDt dados ) {

		String stRetorno="";

		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null; 
		String stTemp = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoTipoPs obPersistencia = new CertidaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
