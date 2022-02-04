package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.ps.AssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AssuntoNe extends AssuntoNeGen {

	private static final long serialVersionUID = 6331675025801895954L;

	public String Verificar(AssuntoDt dados) {
		String stRetorno = "";
		
		if (dados.getAssunto().equalsIgnoreCase("")) {
			stRetorno += "O campo Descrição obrigatório.";
		}
		
		if (dados.getAssuntoCodigo().equalsIgnoreCase("")) {
			if (stRetorno.length() > 0) stRetorno += "\n";
			stRetorno += "O campo Código CNJ é obrigatório.";
		}		
		
		return stRetorno;
	}

	public String consultarDescricaoAssuntoServentiaJSON(String tempNomeBusca, String idServentia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AssuntoPs obPersistencia = new  AssuntoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoAssuntoServentiaJSON(tempNomeBusca, idServentia, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return stTemp;
	}
	
	public String consultarDescricaoAssuntoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AssuntoPs obPersistencia = new  AssuntoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoAssuntoJSON(tempNomeBusca, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return stTemp;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao, String id_AreaDistribuicao, String id_Serventia) throws Exception {
		FabricaConexao obFabricaConexao = null; 
		List<String> idsAreasDistribuicoes = new ArrayList<String>();
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
						
			if (id_AreaDistribuicao != null && id_AreaDistribuicao.trim().length() > 0){
				idsAreasDistribuicoes.add(id_AreaDistribuicao);
			} else if (id_Serventia != null && id_Serventia.trim().length() > 0) {
				List areasDistribuicoes = new AreaDistribuicaoNe().consultarAreasDistribuicaoServentia(id_Serventia);
				if (areasDistribuicoes != null && areasDistribuicoes.size() > 0) {
					for(Object areaDistribuicaoObj : areasDistribuicoes) {
						if (areaDistribuicaoObj != null && areaDistribuicaoObj instanceof AreaDistribuicaoDt) {
							idsAreasDistribuicoes.add(((AreaDistribuicaoDt)areaDistribuicaoObj).getId());
						}
					}					
				}
			}
			
			AssuntoPs obPersistencia = new  AssuntoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarDescricaoCombo(descricao, idsAreasDistribuicoes);
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public String consultarDescricaoJSON(String descricao, String codigoCNJ, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, codigoCNJ, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	public String consultarDescricaoArtigoJSON(String descricao, String codigoCNJ, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoArtigoJSON(descricao, codigoCNJ, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}	
	
	public AssuntoDt consultarId(String id_assunto, FabricaConexao obFabricaConexao) throws Exception {
		AssuntoDt dtRetorno=null;
		AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

		dtRetorno= obPersistencia.consultarId(id_assunto ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
}
