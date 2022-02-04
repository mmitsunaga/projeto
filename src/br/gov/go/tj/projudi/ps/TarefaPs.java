package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

@SuppressWarnings("unchecked")
public class TarefaPs extends TarefaPsGen{

    private static final long serialVersionUID = 5455834270430081680L;

    public TarefaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * 
	 * @param nome
	 * @param idStatusTarefa
	 * @param idProjeto
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List pesquisarTarefaAguardandoVisto(String nome, String idStatusTarefa, String idUsuarioCriador, String idProjeto, String posicao ) throws Exception {
		StringBuffer sql = new StringBuffer();
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		String clausulaWhere = "";
		String comandoClausulaWhere = " WHERE ";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("SELECT ID_TAREFA, TAREFA, TAREFA_STATUS, TAREFA_STATUS_CODIGO, PROJETO, TAREFA_PRIOR FROM PROJUDI.VIEW_TAREFA ");
		
		//-----------------------
		//-- FILTRA PELO NOME
		//-----------------------
		if(nome != null && !nome.isEmpty()){
			clausulaWhere += comandoClausulaWhere +  " TAREFA LIKE ? ";
			ps.adicionarString( nome +"%");
			comandoClausulaWhere = " AND ";
		}

		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idStatusTarefa != null && !idStatusTarefa.isEmpty()){
			clausulaWhere += comandoClausulaWhere +  " ID_TAREFA_STATUS = ? ";
			ps.adicionarLong(idStatusTarefa);
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR USUÁRIO CRIADOR DA TAREFA
		//-----------------------
		if(idUsuarioCriador != null && !idUsuarioCriador.isEmpty()){
			clausulaWhere += comandoClausulaWhere + " ID_USU_CRIADOR = ? ";
			ps.adicionarLong(idUsuarioCriador);
			comandoClausulaWhere = " AND ";
		}

		//-----------------------
		//-- FILTRA POR PROJETO
		//-----------------------
		if(idProjeto != null && !idProjeto.isEmpty() && !idProjeto.equals("null")){
			clausulaWhere += comandoClausulaWhere + " ID_PROJETO = ? ";
			ps.adicionarLong(idProjeto);
			comandoClausulaWhere = " AND ";
		}
		
		sql.append(clausulaWhere);
		sql.append(" ORDER BY TAREFA ");		
		try{
			rs = consultarPaginacao(sql.toString(), ps, posicao);
			while (rs.next()) {
				TarefaDt obTemp = new TarefaDt();
				obTemp.setId(rs.getString("ID_TAREFA"));
				obTemp.setTarefa(rs.getString("TAREFA"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setTarefaStatus(rs.getString("TAREFA_STATUS"));
				obTemp.setTarefaStatusCodigo(rs.getString("TAREFA_STATUS_CODIGO"));
				obTemp.setTarefaPrioridade(rs.getString("TAREFA_PRIOR"));
				liTemp.add(obTemp);
			}
			
			sql = new StringBuffer();
			sql.append("SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA " + clausulaWhere);
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
			}catch(Exception e) {
				
			}
		}
		return liTemp; 
	}
	
	/**
	 * Consulta as tarefas e retorna em formato JSON.
	 * 
	 * @param descricao
	 * @param idStatusTarefa
	 * @param idProjeto
	 * @param idUsuarioCriador
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public String consultarDescricaoJSON(String descricaoProjeto, String descricaoTarefa, String idStatusTarefa, String idUsuarioCriador, String posicao) throws Exception {
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;		
		
		Sql = " FROM PROJUDI.VIEW_TAREFA WHERE TAREFA LIKE ? ";
		ps.adicionarString( descricaoTarefa +"%");
		
		if(idStatusTarefa != null && !idStatusTarefa.isEmpty()){
			Sql += " AND ID_TAREFA_STATUS = ? ";
			ps.adicionarLong(idStatusTarefa);			
		}
		
		if(descricaoProjeto != null && !descricaoProjeto.isEmpty() && !descricaoProjeto.equals("null")){
			Sql += " AND PROJETO LIKE ? ";
			ps.adicionarString( descricaoProjeto +"%");			
		}
		
		if(idUsuarioCriador != null && !idUsuarioCriador.isEmpty()){
			Sql += " AND ID_USU_CRIADOR = ? ";
			ps.adicionarLong(idUsuarioCriador);			
		}			
		
		Sql+= " ORDER BY TAREFA ";		
	
		try{
			rs1 = consultarPaginacao("SELECT ID_TAREFA AS ID, TAREFA AS DESCRICAO1, TAREFA_STATUS AS DESCRICAO2, TAREFA_PRIOR AS DESCRICAO3, PROJETO AS DESCRICAO4 " + Sql, ps, posicao);			
					
			rs2 = consultar("SELECT COUNT(1) as QUANTIDADE " + Sql, ps);
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		
		return stTemp; 
	}
	
	/**
	 * 
	 * @param nome
	 * @param idProjeto
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List pesquisarTarefaNaoFinalizada(String nome, String idProjeto, String posicao ) throws Exception {
		StringBuffer sql = new StringBuffer();
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		String clausulaWhere = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("SELECT ID_TAREFA, TAREFA, TAREFA_STATUS, TAREFA_STATUS_CODIGO, PROJETO, TAREFA_PRIOR FROM PROJUDI.VIEW_TAREFA WHERE  " +
				"ID_TAREFA_STATUS <> ? ");
		ps.adicionarLong(TarefaStatusDt.FINALIZADAS);
		
		//-----------------------
		//-- FILTRA PELO NOME
		//-----------------------
		if(nome != null && !nome.isEmpty()){
			clausulaWhere += " AND TAREFA LIKE ?";
			ps.adicionarString( nome +"%");
		}

		//-----------------------
		//-- FILTRA POR PROJETO
		//-----------------------
		if(idProjeto != null && !idProjeto.isEmpty() && !idProjeto.equals("null")){
			clausulaWhere += " AND ID_PROJETO = ? ";
			ps.adicionarLong(idProjeto);
		}
		
		sql.append(clausulaWhere);
		sql.append(" ORDER BY TAREFA ");		
		try{
			rs = consultarPaginacao(sql.toString(), ps, posicao);
			while (rs.next()) {
				TarefaDt obTemp = new TarefaDt();
				obTemp.setId(rs.getString("ID_TAREFA"));
				obTemp.setTarefa(rs.getString("TAREFA"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setTarefaStatus(rs.getString("TAREFA_STATUS"));
				obTemp.setTarefaStatusCodigo(rs.getString("TAREFA_STATUS_CODIGO"));
				obTemp.setTarefaPrioridade(rs.getString("TAREFA_PRIOR"));
				liTemp.add(obTemp);
			}
			
			sql = new StringBuffer();
			sql.append("SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA WHERE ID_TAREFA_STATUS <> ? " + clausulaWhere);
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
			}catch(Exception e) {
				
			}
		}
		return liTemp; 
	}	
	
	public String pesquisarTarefaNaoFinalizadaJSON(String nome, String idProjeto, String posicao ) throws Exception {
		StringBuffer sql = new StringBuffer();
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		String clausulaWhere = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		sql.append("SELECT ID_TAREFA AS ID, TAREFA AS DESCRICAO1, TAREFA_STATUS AS DESCRICAO2, TAREFA_PRIOR AS DESCRICAO3 FROM PROJUDI.VIEW_TAREFA WHERE  " +
				"ID_TAREFA_STATUS <> ? ");
		ps.adicionarLong(TarefaStatusDt.FINALIZADAS);
		
		//-----------------------
		//-- FILTRA PELO NOME
		//-----------------------
		if(nome != null && !nome.isEmpty()){
			clausulaWhere += " AND TAREFA LIKE ?";
			ps.adicionarString( nome +"%");
		}

		//-----------------------
		//-- FILTRA POR PROJETO
		//-----------------------
		if(idProjeto != null && !idProjeto.isEmpty() && !idProjeto.equals("null")){
			clausulaWhere += " AND ID_PROJETO = ? ";
			ps.adicionarLong(idProjeto);
		}
		
		sql.append(clausulaWhere);
		sql.append(" ORDER BY TAREFA ");		
		try{
			rs1 = consultarPaginacao(sql.toString(), ps, posicao);
			
			sql = new StringBuffer();
			sql.append("SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA WHERE ID_TAREFA_STATUS <> ? " + clausulaWhere);
			rs2 = consultar(sql.toString(), ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
			try{
				if (rs1 != null){
					rs1.close();
				}
			}catch(Exception e) {
				
			}
			try{
				if (rs2 != null){
					rs2.close();
				}
			}catch(Exception e) {
				
			}
		}
		return stTemp;
	}	
	
	/**
	 * Pesquisa uma tarefa filtrando por Nome, e Situação (aberto, emAndamento, aguardaVisto e Finalizados)
	 * 
	 * @param nome
	 * @param aberta
	 * @param emAndamento
	 * @param aguardaVisto
	 * @param finalizados
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List pesquisarTarefa(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String posicao ) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer clausulaWhere = new StringBuffer();
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String comandoClausulaWhere = " WHERE ";

		sql.append("SELECT ID_TAREFA, TAREFA, TAREFA_STATUS, TAREFA_STATUS_CODIGO, PROJETO, TAREFA_PRIOR FROM PROJUDI.VIEW_TAREFA ");
		
		//-----------------------
		//-- FILTRA PELO NOME
		//-----------------------
		if(nome != null && !nome.isEmpty() && !nome.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " TAREFA LIKE ?");
			ps.adicionarString( nome +"%");
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idPrioridadeTarefa != null && !idPrioridadeTarefa.isEmpty() && !idPrioridadeTarefa.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_PRIOR = ? ");
			ps.adicionarLong(idPrioridadeTarefa);
			comandoClausulaWhere = " AND ";
		}

		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idStatusTarefa != null && !idStatusTarefa.isEmpty() && !idStatusTarefa.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_STATUS = ? ");
			ps.adicionarLong(idStatusTarefa);
			comandoClausulaWhere = " AND ";
		}

		//-----------------------
		//-- FILTRA POR PROJETO
		//-----------------------
		if(idProjeto != null && !idProjeto.isEmpty() && !idProjeto.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " ID_PROJETO = ? ");
			ps.adicionarLong(idProjeto);
			comandoClausulaWhere = " AND ";
		}
		
		sql.append(clausulaWhere.toString());
		sql.append(" ORDER BY TAREFA ");		
		try{
			rs = consultarPaginacao(sql.toString(), ps, posicao);
			while (rs.next()) {
				TarefaDt obTemp = new TarefaDt();
				obTemp.setId(rs.getString("ID_TAREFA"));
				obTemp.setTarefa(rs.getString("TAREFA"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setTarefaStatus(rs.getString("TAREFA_STATUS"));
				obTemp.setTarefaStatusCodigo(rs.getString("TAREFA_STATUS_CODIGO"));
				obTemp.setTarefaPrioridade(rs.getString("TAREFA_PRIOR"));
				liTemp.add(obTemp);
			}
			
			sql = new StringBuffer();
			sql.append("SELECT Count(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA ");
			sql.append(clausulaWhere.toString());
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
			}catch(Exception e) {
			}
		}
		return liTemp; 
	}

	public String pesquisarTarefaJSON(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String posicao ) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer clausulaWhere = new StringBuffer();
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String comandoClausulaWhere = " WHERE ";
		int qtdeColunas = 4;

		sql.append("SELECT ID_TAREFA AS ID, TAREFA AS DESCRICAO1, TAREFA_STATUS AS DESCRICAO2, TAREFA_PRIOR AS DESCRICAO3, PROJETO AS DESCRICAO4 FROM PROJUDI.VIEW_TAREFA ");
		
		//-----------------------
		//-- FILTRA PELO NOME
		//-----------------------
		if(nome != null && !nome.isEmpty() && !nome.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " TAREFA LIKE ?");
			ps.adicionarString( nome +"%");
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idPrioridadeTarefa != null && !idPrioridadeTarefa.isEmpty() && !idPrioridadeTarefa.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_PRIOR = ? ");
			ps.adicionarLong(idPrioridadeTarefa);
			comandoClausulaWhere = " AND ";
		}

		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idStatusTarefa != null && !idStatusTarefa.isEmpty() && !idStatusTarefa.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_STATUS = ? ");
			ps.adicionarLong(idStatusTarefa);
			comandoClausulaWhere = " AND ";
		}

		//-----------------------
		//-- FILTRA POR PROJETO
		//-----------------------
		if(idProjeto != null && !idProjeto.isEmpty() && !idProjeto.equals("null")){
			clausulaWhere.append(comandoClausulaWhere + " ID_PROJETO = ? ");
			ps.adicionarLong(idProjeto);
			comandoClausulaWhere = " AND ";
		}
		
		sql.append(clausulaWhere.toString());
		sql.append(" ORDER BY TAREFA ");		
		try{
			rs1 = consultarPaginacao(sql.toString(), ps, posicao);
			sql = new StringBuffer();
			sql.append("SELECT Count(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA ");
			sql.append(clausulaWhere.toString());
			rs2 = consultar(sql.toString(), ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
			try{
				if (rs1 != null){
					rs1.close();
				}
			}catch(Exception e) {
			}
			try{
				if (rs2 != null){
					rs2.close();
				}
			}catch(Exception e) {
			}
		}
		return stTemp; 
	}

	/**
	 * Recupera a lista de tarefas vinculadas 
	 * ao participante do projeto logado no sistema.
	 * 
	 * @param nome
	 * @param aberta
	 * @param emAndamento
	 * @param aguardaVisto
	 * @param finalizados
	 * @param idProjetoParticipante
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List pesquisarTarefaByParticipante(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String idpartcipante, String posicao ) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer clausulaWhere = new StringBuffer();
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String comandoClausulaWhere = " WHERE ";
		
		sql.append("SELECT ID_TAREFA, TAREFA, TAREFA_STATUS, TAREFA_STATUS_CODIGO, PROJETO, TAREFA_PRIOR FROM PROJUDI.VIEW_TAREFA ");
		
		//-----------------------
		//-- FILTRA PELO NOME
		//-----------------------
		if(nome != null && !nome.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " TAREFA LIKE ?");
			ps.adicionarString( nome +"%");
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idPrioridadeTarefa != null && !idPrioridadeTarefa.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_PRIOR = ? ");
			ps.adicionarLong(idPrioridadeTarefa);
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idStatusTarefa != null && !idStatusTarefa.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_STATUS = ? ");
			ps.adicionarLong(idStatusTarefa);
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR PROJETO
		//-----------------------
		if(idProjeto != null && !idProjeto.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_PROJETO = ? ");
			ps.adicionarLong(idProjeto);
			comandoClausulaWhere = " AND ";
		}

		//-----------------------------
		//-- FILTRA POR PARTICIPANTE
		//-----------------------------
		if(idpartcipante != null && !idpartcipante.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_PROJ_PARTIC_RESPONSAVEL = ? ");
			ps.adicionarLong(idpartcipante);
			comandoClausulaWhere = " AND ";
		}
		
		sql.append(clausulaWhere.toString());
		sql.append(" ORDER BY TAREFA ");		
		try{
			rs = consultarPaginacao(sql.toString(), ps, posicao);
			while (rs.next()) {
				TarefaDt obTemp = new TarefaDt();
				obTemp.setId(rs.getString("ID_TAREFA"));
				obTemp.setTarefa(rs.getString("TAREFA"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setTarefaStatus(rs.getString("TAREFA_STATUS"));
				obTemp.setTarefaStatusCodigo(rs.getString("TAREFA_STATUS_CODIGO"));
				obTemp.setTarefaPrioridade(rs.getString("TAREFA_PRIOR"));
				liTemp.add(obTemp);
			}
			
			sql = new StringBuffer();
			sql.append("SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA ");
			sql.append(clausulaWhere.toString());
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
			}catch(Exception e) {
			}
		}
		return liTemp; 
	}
	
	public String pesquisarTarefaByParticipanteJSON(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String idpartcipante, String posicao ) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer clausulaWhere = new StringBuffer();
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String comandoClausulaWhere = " WHERE ";
		int qtdeColunas = 4;
		
		sql.append("SELECT ID_TAREFA AS ID, TAREFA AS DESCRICAO1, TAREFA_STATUS AS DESCRICAO2, TAREFA_PRIOR AS DESCRICAO3, PROJETO AS DESCRICAO4 FROM PROJUDI.VIEW_TAREFA ");
		
		//-----------------------
		//-- FILTRA PELO NOME
		//-----------------------
		if(nome != null && !nome.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " TAREFA LIKE ?");
			ps.adicionarString( nome +"%");
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idPrioridadeTarefa != null && !idPrioridadeTarefa.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_PRIOR = ? ");
			ps.adicionarLong(idPrioridadeTarefa);
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR STATUS
		//-----------------------
		if(idStatusTarefa != null && !idStatusTarefa.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_TAREFA_STATUS = ? ");
			ps.adicionarLong(idStatusTarefa);
			comandoClausulaWhere = " AND ";
		}
		
		//-----------------------
		//-- FILTRA POR PROJETO
		//-----------------------
		if(idProjeto != null && !idProjeto.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_PROJETO = ? ");
			ps.adicionarLong(idProjeto);
			comandoClausulaWhere = " AND ";
		}

		//-----------------------------
		//-- FILTRA POR PARTICIPANTE
		//-----------------------------
		if(idpartcipante != null && !idpartcipante.isEmpty()){
			clausulaWhere.append(comandoClausulaWhere + " ID_PROJ_PARTIC_RESPONSAVEL = ? ");
			ps.adicionarLong(idpartcipante);
			comandoClausulaWhere = " AND ";
		}
		
		sql.append(clausulaWhere.toString());
		sql.append(" ORDER BY TAREFA ");		
		try{
			rs1 = consultarPaginacao(sql.toString(), ps, posicao);
			
			sql = new StringBuffer();
			sql.append("SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA ");
			sql.append(clausulaWhere.toString());
			rs2 = consultar(sql.toString(), ps);
			rs2.next();
		    stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try{
				if (rs1 != null){
					rs1.close();
				}
			}catch(Exception e) {
			}
			try{
				if (rs2 != null){
					rs2.close();
				}
			}catch(Exception e) {
			}
		}
		return stTemp; 
	}
	
	/**
	 * Adiciona um novo registro de tarefas
	 * @param dados - TarefaDt
	 * @throws Exception 
	 */
	public void inserir(TarefaDt dados ) throws Exception{
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "INSERT INTO PROJUDI.Tarefa ("; 
		if ((dados.getTarefa().length()>0)) Sql+= "TAREFA " ;
		if ((dados.getDescricao().length()>0)) Sql+= ",DESCRICAO " ;
		if ((dados.getResposta().length()>0)) Sql+= ",RESPOSTA " ;
		if ((dados.getDataInicio().length()>0)) Sql+= ",DATA_INICIO " ;
		if ((dados.getDataCriacao().length()>0)) Sql+= ",DATA_CRIACAO " ;
		if ((dados.getPrevisao().length()>0)) Sql+= ",PREVISAO " ;
		if ((dados.getDataFim().length()>0)) Sql+= ",DATA_FIM " ;
		if ((dados.getId_TarefaPai().length()>0)) Sql+= ",ID_TAREFA_PAI " ;
		if ((dados.getPontosApf().length()>0)) Sql+= ",PONTOS_APF " ;
		if ((dados.getPontosApg().length()>0)) Sql+= ",PONTOS_APG " ;
		if ((dados.getId_TarefaPrioridade().length()>0)) Sql+= ",ID_TAREFA_PRIOR " ;
		if ((dados.getId_TarefaStatus().length()>0)) Sql+= ",ID_TAREFA_STATUS " ;
		if ((dados.getId_TarefaTipo().length()>0)) Sql+= ",ID_TAREFA_TIPO " ;
		if ((dados.getId_Projeto().length()>0)) Sql+= ",ID_PROJETO " ;
		if ((dados.getId_ProjetoParticipanteResponsavel().length()>0)) Sql+= ",ID_PROJ_PARTIC_RESPONSAVEL " ;
		if ((dados.getId_UsuarioCriador().length()>0)) Sql+= ",ID_USU_CRIADOR " ;
		if ((dados.getId_UsuarioFinalizador().length()>0)) Sql+= ",ID_USU_FINALIZADOR " ;
		Sql+=") "; 
		Sql +=  " Values (";
 		if ((dados.getTarefa().length()>0)){
 			Sql+= "?";
 			ps.adicionarString(dados.getTarefa());
 		}
		if ((dados.getDescricao().length()>0)){
			Sql+= ",?";
			ps.adicionarString(dados.getDescricao());
		}
		if ((dados.getResposta().length()>0)){
			Sql+= ",?";
			ps.adicionarString(dados.getResposta());
		}
		if ((dados.getDataInicio().length()>0)){
			Sql+= ",?";
			ps.adicionarDate(dados.getDataInicio());
		}
		if ((dados.getDataCriacao().length()>0)){
			Sql+= ",?";
			ps.adicionarDate(dados.getDataCriacao());
		}
		if ((dados.getPrevisao().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getPrevisao());
		}
		if ((dados.getDataFim().length()>0)){
			Sql+= ",?";
			ps.adicionarDate(dados.getDataFim());
		}
		if ((dados.getId_TarefaPai().length()>0)) {
			Sql+= ",?";
			ps.adicionarLong(dados.getId_TarefaPai());
		}
		if ((dados.getPontosApf().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getPontosApf());
		}
		if ((dados.getPontosApg().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getPontosApg());
		}
		if ((dados.getId_TarefaPrioridade().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getId_TarefaPrioridade());
		}
		if ((dados.getId_TarefaStatus().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getId_TarefaStatus());
		}
		if ((dados.getId_TarefaTipo().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getId_TarefaTipo());
		}
		if ((dados.getId_Projeto().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getId_Projeto());			
		}
		if ((dados.getId_ProjetoParticipanteResponsavel().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getId_ProjetoParticipanteResponsavel());
		}
		if ((dados.getId_UsuarioCriador().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getId_UsuarioCriador());
		}
		if ((dados.getId_UsuarioFinalizador().length()>0)){
			Sql+= ",?";
			ps.adicionarLong(dados.getId_UsuarioFinalizador());
		}
		Sql+=")";

		Sql=Sql.replace("(,","(");
		
		dados.setId(executarInsert(Sql, "ID_TAREFA", ps));
		 
	} 
	
	/**
	 * Atualiza os dados do registro passado como parâmetro.
	 * @param dados
	 */
	public void alterar(TarefaDt dados) throws Exception{

		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Sql= "UPDATE PROJUDI.Tarefa SET  ";
		Sql+= "TAREFA  = ? ";ps.adicionarString(dados.getTarefa()); 
		Sql+= ",DESCRICAO  = ? "; ps.adicionarString(dados.getDescricao()); 
		Sql+= ",RESPOSTA  = ? "; ps.adicionarString(dados.getResposta()); 
		Sql+= ",DATA_INICIO = ? "; ps.adicionarDate(dados.getDataInicio()); 
		Sql+= ",PREVISAO  = ? "; ps.adicionarLong(dados.getPrevisao()); 
		Sql+= ",DATA_FIM = ? "; ps.adicionarDate(dados.getDataFim()); 
		Sql+= ",ID_TAREFA_PAI  = ? "; ps.adicionarLong(dados.getId_TarefaPai()); 
		Sql+= ",PONTOS_APF  = ? "; ps.adicionarLong(dados.getPontosApf()); 
		Sql+= ",PONTOS_APG  = ? "; ps.adicionarLong(dados.getPontosApg()); 
		Sql+= ",ID_TAREFA_PRIOR  = ? "; ps.adicionarLong(dados.getId_TarefaPrioridade()); 
		Sql+= ",ID_TAREFA_STATUS  = ? "; ps.adicionarLong(dados.getId_TarefaStatus()); 
		Sql+= ",ID_TAREFA_TIPO  = ? "; ps.adicionarLong(dados.getId_TarefaTipo()); 
		Sql+= ",ID_PROJETO  = ? "; ps.adicionarLong(dados.getId_Projeto()); 
		Sql+= ",ID_PROJ_PARTIC_RESPONSAVEL  = ? "; ps.adicionarLong(dados.getId_ProjetoParticipanteResponsavel()); 
		Sql+= ",ID_USU_CRIADOR  = ? "; ps.adicionarLong(dados.getId_UsuarioCriador()); 
		Sql+= ",ID_USU_FINALIZADOR  = ? "; ps.adicionarLong(dados.getId_UsuarioFinalizador()); 
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_TAREFA  = ? "; ps.adicionarLong(dados.getId()); 
		executarUpdateDelete(Sql, ps); 
	}

	/**
	 * Atribui a TAREFA cujo ID foi passado como parâmetro ao registro de projeto
	 * participante passado como segundo parametro para o método.
	 * 
	 * @param idTarefa - ID da tarefa que será atribuida ao usuário.
	 * @param idProjetoParticipante - ID do usuário logado.
	 * @param idStatusTarefa - Status EM ANDAMENTO.
	 * @throws Exception
	 */
	public void atribuirTarefaResponsavel(String idTarefa, String idProjetoParticipante, String idStatusTarefa) throws Exception{
		
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "UPDATE PROJUDI.Tarefa SET  ";
		Sql+= ",ID_PROJ_PARTIC_RESPONSAVEL  = ? "; ps.adicionarLong(idProjetoParticipante); 
		Sql+= ",ID_TAREFA_STATUS  = ? "; ps.adicionarLong(idStatusTarefa); 
		Sql+= ",DATA_INICIO  = ? "; ps.adicionarDate(new Date()); 
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_TAREFA  = ? "; ps.adicionarLong(idTarefa); 
		executarUpdateDelete(Sql, ps); 
	}

	/**
	 * Recupera o ID da tarefa mais antiga pendente vinculada ao projeto
	 * com ID equivalente ao passado como parâmetro.
	 * 
	 * @param idProjeto
	 * @return String
	 * @throws Exception
	 */
	public String buscarIdPrimeiraTarefaPendenteByProjeto(String idProjeto) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlData = new StringBuffer();
		ResultSetTJGO rs=null;
		ResultSetTJGO rsData=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			//recupera a data da tarefa mais antiga vinculada ao projeto
			sqlData.append(" SELECT MIN(DATA_CRIACAO) as DATA_CRIACAO FROM PROJUDI.VIEW_TAREFA  WHERE ID_PROJETO = ? ");
			ps.adicionarLong(idProjeto);
			sqlData.append(" AND ID_PROJ_PARTIC_RESPONSAVEL IS NULL ");
			//sqlData.append(" group by DATA_CRIACAO "); sem necessidade
			
			rsData = consultar(sqlData.toString(), ps);
			rsData.next();
			
			Date dataCriacao;
			try{
				dataCriacao = rsData.getDateTime("DATA_CRIACAO");
			}catch(NullPointerException e) {
				return null;
			}
			
			//recupera a taerefa
			ps.limpar();
			sql.append(" SELECT ID_TAREFA FROM PROJUDI.VIEW_TAREFA ");
			sql.append(" 	WHERE ID_PROJETO = ? AND DATA_CRIACAO = ? ");
			ps.adicionarLong(idProjeto);
			ps.adicionarDate(dataCriacao);
			sql.append(" 	AND ID_PROJ_PARTIC_RESPONSAVEL IS NULL  ");
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				return rs.getString("ID_TAREFA");
			}
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				if (rsData != null){
					rsData.close();
				}
			}catch(Exception e) {
				
			}
		}
		return null;
	}
	
	/**
	 * Recupera os dados do ResultSet e constroi a entidade
	 * de retorno DT.
	 * @param Dados
	 * @param rs
	 */
	protected void associarDt( TarefaDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_TAREFA"));
		Dados.setTarefa(rs.getString("TAREFA"));
		Dados.setDescricao( rs.getString("DESCRICAO"));
		Dados.setResposta( rs.getString("RESPOSTA"));
		Dados.setDataInicio( Funcoes.FormatarData(rs.getDateTime("DATA_INICIO")));
		Dados.setPrevisao( rs.getString("PREVISAO"));
		Dados.setDataFim( Funcoes.FormatarData(rs.getDateTime("DATA_FIM")));
		Dados.setId_TarefaPai( rs.getString("ID_TAREFA_PAI"));
		Dados.setTarefaPai( rs.getString("TAREFA_PAI"));
		Dados.setPontosApf( rs.getString("PONTOS_APF"));
		Dados.setPontosApg( rs.getString("PONTOS_APG"));
		Dados.setId_TarefaPrioridade( rs.getString("ID_TAREFA_PRIOR"));
		Dados.setTarefaPrioridade( rs.getString("TAREFA_PRIOR"));

		Dados.setId_TarefaStatus( rs.getString("ID_TAREFA_STATUS"));
		Dados.setTarefaStatus( rs.getString("TAREFA_STATUS"));
		Dados.setTarefaStatusCodigo( rs.getString("TAREFA_STATUS_CODIGO"));
		
		Dados.setId_TarefaTipo( rs.getString("ID_TAREFA_TIPO"));
		Dados.setTarefaTipo( rs.getString("TAREFA_TIPO"));
		Dados.setId_Projeto( rs.getString("ID_PROJETO"));
		Dados.setProjeto( rs.getString("PROJETO"));
		Dados.setId_ProjetoParticipanteResponsavel( rs.getString("ID_PROJ_PARTIC_RESPONSAVEL"));
		Dados.setProjetoParticipanteResponsavel( rs.getString("PROJETO_PARTICIPANTE_RESP"));
		Dados.setId_UsuarioCriador( rs.getString("ID_USU_CRIADOR"));
		Dados.setUsuarioCriador( rs.getString("USU_CRIADOR"));
		Dados.setId_UsuarioFinalizador( rs.getString("ID_USU_FINALIZADOR"));
		Dados.setUsuarioFinalizador( rs.getString("USU_FINALIZADOR"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

	/**
	 * 
	 * 
	 * @param periodoInicialUtilizado
	 * @param periodoFinalUtilizado
	 * @param idProjeto
	 * @param idServentiaCargo
	 * @return
	 * @throws Exception 
	 */
	public List obtenhaRelatorioListaTarefas(TJDataHora periodoInicialUtilizado, TJDataHora periodoFinalUtilizado, String idProjeto, String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaRetorno = new ArrayList();
		ResultSetTJGO rs = null;
		
		String Sql = "";
		Sql += "SELECT ID_TAREFA, TAREFA, DATA_CRIACAO, DATA_INICIO, DATA_FIM, PREVISAO, PONTOS_APF, PONTOS_APG, ";
		Sql += "ID_TAREFA_PRIOR, TAREFA_PRIOR, ID_TAREFA_STATUS, TAREFA_STATUS, TAREFA_STATUS_CODIGO, ";
		Sql += "ID_TAREFA_TIPO, TAREFA_TIPO, ID_PROJ_PARTIC_RESPONSAVEL, PROJETO_PARTICIPANTE_RESP, ";
		Sql += "DESCRICAO, RESPOSTA, ID_TAREFA_PAI, TAREFA_PAI, ID_PROJETO, PROJETO, ID_USU_CRIADOR, USU_CRIADOR, ID_USU_FINALIZADOR, USU_FINALIZADOR, CODIGO_TEMP ";
		Sql += "FROM VIEW_TAREFA ";
		Sql += "WHERE TAREFA_STATUS_CODIGO IN (?, ?) ";
		ps.adicionarLong(TarefaStatusDt.AG_VISTO);
		ps.adicionarLong(TarefaStatusDt.FINALIZADAS);
		Sql += "AND ID_PROJETO = ? ";
		ps.adicionarLong(idProjeto);
		Sql += "AND ( ";
		Sql += "  (NOT DATA_CRIACAO IS NULL AND DATA_CRIACAO BETWEEN ? AND ?) ";
		ps.adicionarDate(periodoInicialUtilizado.getDate());
		ps.adicionarDate(periodoFinalUtilizado.getDate());
		Sql += "  OR ";
		Sql += "  (NOT DATA_INICIO IS NULL AND DATA_INICIO BETWEEN ? AND ?) ";
		ps.adicionarDate(periodoInicialUtilizado.getDate());
		ps.adicionarDate(periodoFinalUtilizado.getDate());
		Sql += "  OR  ";
		Sql += "  (NOT DATA_FIM IS NULL AND DATA_FIM BETWEEN ? AND ?) ";
		ps.adicionarDate(periodoInicialUtilizado.getDate());
		ps.adicionarDate(periodoFinalUtilizado.getDate());
		Sql += ") ";
		if (idServentiaCargo != null && idServentiaCargo.trim().length() > 0)
		{
			Sql += "AND ID_PROJ_PARTIC_RESPONSAVEL = (SELECT ID_PROJETO_PARTICIPANTE ";
			Sql += "                                   FROM VIEW_PROJETO_PARTICIPANTE ";
			Sql += "                                  WHERE ID_PROJETO = ? ";
			ps.adicionarLong(idProjeto);
			Sql += "                                   AND ID_SERV_CARGO = ? ";
			ps.adicionarLong(idServentiaCargo);
			Sql += "                                   AND ROWNUM = 1) ";
		}
		
		rs = consultar(Sql, ps);
		try{
			while (rs.next()) {
				TarefaDt tarefa = new TarefaDt();
				associarDt(tarefa, rs);
				listaRetorno.add(tarefa);
			}
		} catch(Exception e) {
			if (rs != null) rs.close();			
		}
		
		return listaRetorno;
	}	

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_TAREFA AS ID, TAREFA AS DESCRICAO1 FROM PROJUDI.VIEW_TAREFA WHERE TAREFA LIKE ?";
		stSql+= " ORDER BY TAREFA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql, ps, posicao);

			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA WHERE TAREFA LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}

}
