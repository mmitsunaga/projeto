package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.projudi.ps.ServentiaGrupoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ServentiaGrupoNe extends ServentiaGrupoNeGen {

	private static final long serialVersionUID = -303130041525665885L;

	public String Verificar(ServentiaGrupoDt dados) {

		String stRetorno = "";

		if (dados.getServentiaGrupo().length() == 0)
			stRetorno += "O Campo ServentiaGrupo é obrigatório.";
		if (dados.getServentia().length() == 0)
			stRetorno += "O Campo Serventia é obrigatório.";

		return stRetorno;

	}
	
	public String consultarDescricaoServentiaUnidaTrabalhoJSON(String tempNomeBusca, int grupoCodigo, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarDescricaoServentiaUnidaTrabalhoJSON(tempNomeBusca, grupoCodigo, PosicaoPaginaAtual);
		Serventiane = null;
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventiagrupo ) throws Exception {
		return consultarDescricaoServentiaGrupoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventiagrupo, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	
	public String consultarDescricaoServentiaGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventiagrupo,  String ordenacao, String quantidadeRegistros ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventiagrupo, ordenacao, quantidadeRegistros);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventia ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoServentiaGrupoServentiaJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventia);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventia, String id_serventiagrupo ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoServentiaGrupoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventia, id_serventiagrupo);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	//lrcampos * 18/07/2019 * Montar o select2 filtrando pela descrição serv_cargo
	public String consultarDescricaoServentiaServCargoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventia ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoServentiaServCargoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventia);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	
	public String consultarDescricaoServentiaGrupoProximaFuncaoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventiagrupo, String id_serventia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoServentiaGrupoProximaFuncaoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventiagrupo, id_serventia);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	

	public String consultarDescricaoJSON(String funcao, String serventia, int grupoCodigo, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(funcao, serventia, grupoCodigo, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public ServentiaGrupoDt consultarSeventiaGrupoDistribuidorId(String id_ServentiaCargo ) throws Exception {

		ServentiaGrupoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarSeventiaGrupoDistribuidorId(id_ServentiaCargo); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ServentiaGrupoDt consultarSeventiaGrupoMagistradoId(String id_ServentiaCargo ) throws Exception {

		ServentiaGrupoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaGrupoPs obPersistencia = new  ServentiaGrupoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarSeventiaGrupoMagistradoId(id_ServentiaCargo); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

}
