package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import br.gov.go.tj.projudi.ps.ServentiaSubtipoAssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ServentiaSubtipoAssuntoNe extends ServentiaSubtipoAssuntoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3758637732982282961L;

	public String Verificar(ServentiaSubtipoAssuntoDt dados) {

		String stRetorno = "";

		if (dados.getId_ServentiaSubtipo().length() == 0) stRetorno += "O Campo Id_ServentiaSubtipo é obrigatório.";
		////System.out.println("..neServentiaSubtipoAssuntoVerificar()");
		return stRetorno;

	}

	/**
	 * Consulta os assuntos disponíveis a partir da área de distribuição, chegando assim ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarAssuntosAreaDistribuicao(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAssuntosAreaDistribuicao(id_AreaDistribuicao, descricao, posicao);

			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta os assuntos disponíveis a partir da área de distribuição, chegando assim ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarAssuntosAreasDistribuicoes(List AreaDistribuicao, String descricao, String posicao) throws Exception {

		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAssuntosAreasDistribuicoes(AreaDistribuicao, descricao, posicao);

			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta os assuntos disponíveis a partir da área de distribuição, chegando assim ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarAssuntosAreasDistribuicoes(String id_Serventia, String descricao, String posicao) throws Exception {
		List tempList = null;
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		
		List areasDistribuicoes = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(id_Serventia);
		tempList = neObjeto.consultarAssuntosAreasDistribuicoes(areasDistribuicoes, descricao, posicao);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	public String consultarDescricaoServentiaSubtipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
		stTemp = ServentiaSubtipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarAssuntosAreasDistribuicoesJSON(List AreaDistribuicao, String descricao, String codigoCNJ, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAssuntosAreasDistribuicoesJSON(AreaDistribuicao, descricao, codigoCNJ, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarAssuntosAreaDistribuicaoJSON(String id_AreaDistribuicao, String descricao, String codigoCNJ, String posicao) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAssuntosAreaDistribuicaoJSON(id_AreaDistribuicao, descricao, codigoCNJ, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAssuntosServentiaJSON(String descricao, String id_Serventia, String posicao) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAssuntosServentiaJSON(descricao, id_Serventia, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public void incluirMultiplo(ServentiaSubtipoAssuntoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				ServentiaSubtipoAssuntoDt obDt = (ServentiaSubtipoAssuntoDt)lisGeral.get(j);
				if (obDt.getId_Assunto().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ServentiaSubtipoAssuntoDt obDt = (ServentiaSubtipoAssuntoDt)lisIncluir.get(i);				
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ServentiaSubtipoAssunto", obDt.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	//---------------------------------------------------------
	public void excluir(ServentiaSubtipoAssuntoDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ServentiaSubtipoAssuntoDt obDt = (ServentiaSubtipoAssuntoDt)lisGeral.get(i);
			if (!obDt.getId().equalsIgnoreCase("") && obDt.getId_Assunto().equalsIgnoreCase(id)){
				lisExcluir.add(obDt);
				break;
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisExcluir.size(); i++) {
				ServentiaSubtipoAssuntoDt obDtTemp = (ServentiaSubtipoAssuntoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("ServentiaSubtipoAssunto", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarAssuntoServentiaSubtipo(String id_serventiasubtipo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao =null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarAssuntoServentiaSubtipoGeral( id_serventiasubtipo);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return lisGeral;   
	}
	
	public String consultarAssuntosAreaDistribuicaoPjdJSON(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAssuntosAreaDistribuicaoPjdJSON(id_AreaDistribuicao, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAssuntosAreasDistribuicoesPjdJSON(List AreaDistribuicao, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAssuntosAreasDistribuicoesPjdJSON(AreaDistribuicao, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
}
