package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.projudi.ps.AfastamentoPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

public class AfastamentoNe extends AfastamentoNeGen {

	private static final long serialVersionUID = 2530722985090529819L;

	public String Verificar(AfastamentoDt dados) {

		String stRetorno = "";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception{
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AfastamentoPs obPersistencia = new  AfastamentoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	public List consultarTodos() throws Exception {
		List listaAfastamento = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AfastamentoPs obPersistencia = new  AfastamentoPs(obFabricaConexao.getConexao());
			listaAfastamento = obPersistencia.consultarTodos();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAfastamento;
	}

}