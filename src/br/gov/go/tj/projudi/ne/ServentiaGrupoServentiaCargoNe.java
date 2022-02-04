package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoServentiaCargoDt;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.projudi.ps.ServentiaGrupoPs;
import br.gov.go.tj.projudi.ps.ServentiaGrupoServentiaCargoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ServentiaGrupoServentiaCargoNe extends ServentiaGrupoServentiaCargoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -3544297673159885271L;

	//---------------------------------------------------------
	public  String Verificar(ServentiaGrupoServentiaCargoDt dados ) {

		String stRetorno="";

		if (dados.getId_ServentiaGrupo().length()==0)
			stRetorno += "O Campo Id_ServentiaGrupo é obrigatório.";

		return stRetorno;

	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		Serventiane = null;
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoJSON(String descricao, String posicao) throws Exception{
		return consultarDescricaoServentiaGrupoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public String consultarDescricaoServentiaGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String ordenacao, String quantidadeRegistros) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoOrdenacaoJSON(tempNomeBusca, PosicaoPaginaAtual, ordenacao, quantidadeRegistros);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoDoisParametrosBuscaJSON(String tempNomeBusca, String tempNomeBusca2, int grupoCodigo, String PosicaoPaginaAtual) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 
			stTemp = obPersistencia.consultarDescricaoServentiaGrupoDoisParametrosBuscaJSON(tempNomeBusca,tempNomeBusca2, grupoCodigo, PosicaoPaginaAtual);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiaCargoServentiaGrupoUlLiCheckBoxJson(String id_serv_grupo ) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		//String tempUlLi="<ul>";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		    ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 
		    
			ServentiaGrupoDt serventiaGrupoDt = new ServentiaGrupoNe().consultarId(id_serv_grupo);

			stTemp=obPersistencia.consultarServentiaCargoServentiaGrupoGeralJson(serventiaGrupoDt.getId(), serventiaGrupoDt.getId_Serventia());
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
