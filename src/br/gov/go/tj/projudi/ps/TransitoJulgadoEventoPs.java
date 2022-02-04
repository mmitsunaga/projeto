package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoSituacaoDt;
import br.gov.go.tj.projudi.dt.TransitoJulgadoEventoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class TransitoJulgadoEventoPs extends Persistencia {


/**
     * 
     */
    private static final long serialVersionUID = 2543733335148729856L;

 
	public TransitoJulgadoEventoPs(Connection conexao){
		Conexao = conexao;
	}



	//---------------------------------------------------------
	public void inserir(TransitoJulgadoEventoDt dados) throws Exception {

		String sql;
		PreparedStatementTJGO  ps = new PreparedStatementTJGO();
		
		sql= "INSERT INTO PROJUDI.TRANS_JULGADO_EVENTO ("; 
		if ((dados.getId_TransitoJulgadoEvento().length()>0)) sql+= "ID_TRANS_JULGADO_EVENTO " ;
		if ((dados.getId_Evento().length()>0)) sql+= ",ID_EVENTO " ;
		if ((dados.getId_TransitoJulgado().length()>0)) sql+= ",ID_TRANS_JULGADO " ;
		if ((dados.getFracao().length()>0)) sql+= ",FRACAO " ;
		sql+=") "; 
		sql +=  " Values (";
 		if ((dados.getId_TransitoJulgadoEvento().length()>0)) {
 			sql+= "?";
 			ps.adicionarLong(dados.getId_TransitoJulgadoEvento());  
 		}
		if ((dados.getId_Evento().length()>0)) {
			sql+= ", ?";
			ps.adicionarLong(dados.getId_Evento());  
		}
		if ((dados.getId_TransitoJulgado().length()>0)) {
			sql+= ", ?";
			ps.adicionarLong(dados.getId_TransitoJulgado());  
		}
		if ((dados.getFracao().length()>0)) {
			sql+= ", ?";
			ps.adicionarString(dados.getFracao());  
		}
		sql+=")";

		sql = sql.replace("(,","(");

					
			dados.setId(executarInsert(sql, "ID_TRANS_JULGADO_EVENTO", ps));

		 
	} 

//---------------------------------------------------------
	public void alterar(TransitoJulgadoEventoDt dados) throws Exception{
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql= "UPDATE PROJUDI.TRANS_JULGADO_EVENTO SET  ";
		sql+= ",ID_EVENTO  = ?";
		ps.adicionarLong(dados.getId_Evento()); 
		sql+= ",ID_TRANS_JULGADO = ?";
		ps.adicionarLong(dados.getId_TransitoJulgado()); 
		sql+= ",FRACAO  = ?";
		ps.adicionarString(dados.getFracao()); 
		sql=sql.replace("SET  ,","SET  ");
		sql = sql + " WHERE ID_TRANS_JULGADO_EVENTO  = ?";
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(sql,ps);
	} 

//---------------------------------------------------------
	public void excluir(String id_TransitoJulgadoEvento) throws Exception {
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql= "DELETE FROM PROJUDI.TRANS_JULGADO_EVENTO WHERE ID_TRANS_JULGADO_EVENTO = ?";
		ps.adicionarLong(id_TransitoJulgadoEvento); 
			executarUpdateDelete(sql, ps);
		
	} 
	
	public void excluirId_Evento(String id_Evento) throws Exception {
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql= "DELETE FROM PROJUDI.TRANS_JULGADO_EVENTO WHERE ID_EVENTO = ?";
		ps.adicionarLong(id_Evento); 
			executarUpdateDelete(sql, ps);
		
	} 

//---------------------------------------------------------
	public TransitoJulgadoEventoDt consultarId(String id)  throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		TransitoJulgadoEventoDt dados=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT * FROM PROJUDI.VIEW_TRANS_JULGADO_EVENTO WHERE ID_TRANS_JULGADO_EVENTO = ?";
		ps.adicionarLong(id);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				dados = new TransitoJulgadoEventoDt();
				associarDt(dados, rs1);
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return dados; 
	}

	protected void associarDt(TransitoJulgadoEventoDt dados, ResultSetTJGO rs1)  throws Exception {
		
		dados.setId(rs1.getString("ID_TRANS_JULGADO_EVENTO"));
		dados.setId_TransitoJulgadoEvento(rs1.getString("ID_TRANS_JULGADO_EVENTO"));
		dados.setId_Evento(rs1.getString("ID_EVENTO"));
		dados.setId_TransitoJulgado(rs1.getString("ID_TRANS_JULGADO"));
		if (rs1.getString("FRACAO") != null) dados.setFracao(rs1.getString("FRACAO"));
		dados.setId_ProcessoExecucao( rs1.getString("ID_PROC_EXE"));
		
	}

//---------------------------------------------------------
	/**
	 * Lista as TransitoJulgadoEvento referente a um evento
	 */
	public List listarTransitoJulgadoEvento(String idProcessoEventoExecucao, String idEventoExecucao) throws Exception{
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "Select * FROM PROJUDI.VIEW_TRANS_JULGADO_EVENTO WHERE ID_EVENTO = ? AND ID_EVENTO_EXE = ?";
		
		ps.adicionarLong(idProcessoEventoExecucao);
		ps.adicionarLong(idEventoExecucao);
		
		try{
			rs = consultar(sql,ps);
			while (rs.next()){
				TransitoJulgadoEventoDt dados = new TransitoJulgadoEventoDt();
				associarDt(dados, rs);
				lista.add(dados);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
        return lista;
	}
	
	/**
	 * Lista as TransitoJulgadoEvento referente a um evento de condenação não extinta
	 */
	public List listarTransitoJulgadoEventoNaoExtinto(String idProcessoEventoExecucao, String idEventoExecucao) throws Exception{
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "Select * FROM PROJUDI.VIEW_TRANS_JULGADO_EVENTO_COMP WHERE ID_EVENTO = ? " +
				" AND ID_EVENTO_EXE = ? " +
				" AND ID_CONDENACAO_EXE_SIT = ? " +
				" order by ID_PROC_EXE";
		
		ps.adicionarLong(idProcessoEventoExecucao);
		ps.adicionarLong(idEventoExecucao);
		ps.adicionarLong(CondenacaoExecucaoSituacaoDt.NAO_APLICA);
		
		try{
			rs = consultar(sql,ps);
			while (rs.next()){
				TransitoJulgadoEventoDt dados = new TransitoJulgadoEventoDt();
				associarDt(dados, rs);
				lista.add(dados);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
        return lista;
	}
	
	/**
	 * Lista as TransitoJulgadoEvento referente a um processo de execução penal referente ao TJ com condenação não extinta
	 * @param id_ProcessoExecucaoPenal: identificador do procsso
	 * @return List<TransitoJulgadoEventoDt>
	 * @throws Exception
	 */
	public List listarTodosTransitoJulgadoEventoNaoExtinto(String id_ProcessoExecucaoPenal, String id_eventoExecucao, boolean ordenarPeloEvento, boolean ordenarPeloTJ) throws Exception{
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append(" SELECT tje.*"); //eu preciso de todos os dados
		sql.append(" FROM PROJUDI.VIEW_TRANS_JULGADO_EVENTO_COMP tje"); 
		sql.append(" WHERE tje.ID_CONDENACAO_EXE_SIT = ?");
		ps.adicionarLong(CondenacaoExecucaoSituacaoDt.NAO_APLICA); //condenações não extintas
		sql.append(" AND (tje.ID_PROC_EXE_PENAL = ? OR tje.ID_PROC_DEPENDENTE = ?)");
		ps.adicionarLong(id_ProcessoExecucaoPenal);
		ps.adicionarLong(id_ProcessoExecucaoPenal);
		sql.append(" AND tje.ID_EVENTO_EXE = ?");
		ps.adicionarLong(id_eventoExecucao);
//		sql.append(" GROUP BY ctj.DATA_INICIO_TRANSITO, ctj.DATA_INICIO_COMUTACAO, ctj.QUANTIDADE_COMUTACAO");
		if (ordenarPeloTJ) {
			sql.append(" ORDER BY tje.ID_PROC_EXE, tje.ID_CONDENACAO_EXE, tje.DATA_INICIO_EVENTO, tje.ID_EVENTO"); //ordena pelo TJ e pela sequência do evento
		}
		//ordena pela data início da comutação
		if (ordenarPeloEvento) {
			sql.append(" ORDER BY tje.DATA_INICIO_EVENTO, tje.ID_EVENTO, tje.ID_PROC_EXE, tje.ID_CONDENACAO_EXE"); //ordena pela sequência do evento, deplos pelo TJ
		}
		
		try{
			rs = consultar(sql.toString(),ps);
			while (rs.next()){
				TransitoJulgadoEventoDt dados = new TransitoJulgadoEventoDt();
				associarDt(dados, rs);
				dados.setDataInicioTransito(Funcoes.FormatarData(rs.getDateTime("DATA_INICIO_TRANSITO")));
				dados.setDataInicioEvento(Funcoes.FormatarData(rs.getDateTime("DATA_INICIO_EVENTO")));
				dados.setTempoPenaTJDias(rs.getString("TEMPO_PENA"));
				if (rs.getString("QUANTIDADE_EVENTO") != null) dados.setTempoComutacaoDias(rs.getString("QUANTIDADE_EVENTO"));
				dados.setId_CondenacaoExecucao(rs.getString("ID_CONDENACAO_EXE"));
				lista.add(dados);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
        return lista;
	}
} 

 
