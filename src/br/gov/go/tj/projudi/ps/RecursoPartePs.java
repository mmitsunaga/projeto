package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.RecursoParteDt;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RecursoPartePs extends RecursoPartePsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 31388463547734408L;

    public RecursoPartePs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método inserir
	 */
	public void inserir(RecursoParteDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "INSERT INTO PROJUDI.RECURSO_PARTE (";
		if (!(dados.getId_Recurso().length() == 0)) Sql += "ID_RECURSO ";
		if (!(dados.getId_ProcessoParte().length() == 0)) Sql += ",ID_PROC_PARTE ";
		if ((dados.getId_ProcessoParteTipo().length() > 0) || (dados.getProcessoParteTipoCodigo().length() > 0)) Sql += ",ID_PROC_PARTE_TIPO ";
		if (!(dados.getDataBaixa().length() == 0)) Sql += ",DATA_BAIXA ";
		if (!(dados.getId_ProcessoTipo() == null || dados.getId_ProcessoTipo().length() == 0)) Sql += ",ID_PROC_TIPO ";
		if (!(dados.getOrdemParte() == null || dados.getOrdemParte().length() == 0)) Sql += ",ORDEM_PARTE ";
		Sql += ") ";
		Sql += " Values (";
		if (!(dados.getId_Recurso().length() == 0)){
			Sql += " ? ";
			ps.adicionarLong(dados.getId_Recurso());
		}
		if (!(dados.getId_ProcessoParte().length() == 0)){
			Sql += ", ?";
			ps.adicionarLong(dados.getId_ProcessoParte());
		}
		if (dados.getId_ProcessoParteTipo().length() > 0){
			Sql += ", ?";
			ps.adicionarLong(dados.getId_ProcessoParteTipo());
		}
		else if (dados.getProcessoParteTipoCodigo().length() > 0){
			Sql += ", (SELECT ID_PROC_PARTE_TIPO FROM PROC_PARTE_TIPO WHERE PROC_PARTE_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getProcessoParteTipoCodigo());
		}
		if (!(dados.getDataBaixa().length() == 0)) {
			Sql += ", ?";
			ps.adicionarDateTime(dados.getDataBaixa());
		}
		if (dados.getId_ProcessoTipo() != null && dados.getId_ProcessoTipo().length() > 0){
			Sql += ", ?";
			ps.adicionarLong(dados.getId_ProcessoTipo());
		}
		if (dados.getOrdemParte() != null && dados.getOrdemParte().length() > 0){
			Sql += ", ?";
			ps.adicionarLong(dados.getOrdemParte());
		}
		Sql += ")";

		Sql = Sql.replace("(,", "(");
				
		dados.setId(executarInsert(Sql, "ID_RECURSO_PARTE", ps));
		
	}

	/**
	 * Sobrescrevendo método alterar
	 */
	public void alterar(RecursoParteDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.RECURSO_PARTE SET  ";
		Sql += "ID_RECURSO  = ? "; ps.adicionarLong(dados.getId_Recurso());
		Sql += ",ID_PROC_PARTE  = ? "; ps.adicionarLong(dados.getId_ProcessoParte());
		if (dados.getId_ProcessoParteTipo().length() > 0) {
			Sql += ",ID_PROC_PARTE_TIPO  = ? ";
			ps.adicionarLong(dados.getId_ProcessoParteTipo());
		}
		else if (dados.getProcessoParteTipoCodigo().length() > 0){
			Sql += ",ID_PROC_PARTE_TIPO  = (SELECT ID_PROC_PARTE_TIPO FROM PROC_PARTE_TIPO WHERE PROC_PARTE_TIPO_CODIGO = ? )";
			ps.adicionarLong(dados.getProcessoParteTipoCodigo());
		}
		Sql += ",DATA_BAIXA  = ? ";
		ps.adicionarDateTime(dados.getDataBaixa());
		if (dados.getId_ProcessoTipo() != null && dados.getId_ProcessoTipo().length() > 0){
			Sql += ",ID_PROC_TIPO  = ? ";
			ps.adicionarLong(dados.getId_ProcessoTipo());
		}
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_RECURSO_PARTE  =" + dados.getId();

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Exclui partes de recurso quer será convertido em processo
	 * @param id_Recurso: identificação do recurso
	 * 
	 * @author lsbernardades
	 */
	public void excluirRecursoPartes(String id_recurso) throws Exception {
		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql= "DELETE FROM PROJUDI.RECURSO_PARTE";
		stSql += " WHERE ID_RECURSO = ?";	ps.adicionarLong(id_recurso);
		
		executarUpdateDelete(stSql,ps);
	}
	
	 /**
	 * Verifica se já existe um recurso ativo com a classe informada. 
	 * @param id_recurso
	 * @param id_ProcessoTipo
	 * @return
	 * @throws Exception
	 */
	public boolean existeRecursoParteAtivoNaClasse(String id_recurso, String id_ProcessoTipo) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql = "SELECT * FROM PROJUDI.VIEW_RECURSO_PARTE WHERE ID_RECURSO = ? "; ps.adicionarLong(id_recurso);
		stSql += " AND ID_PROC_TIPO = ? "; ps.adicionarLong(id_ProcessoTipo);
		stSql += " AND DATA_BAIXA IS NULL ";

		try{
			rs1 = consultar(stSql,ps);
			return rs1.next();				
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
	}
	
	 /**
	 * Consulta uma lista de recursos ativos com a classe informada. 
	 * @param id_recurso
	 * @param id_ProcessoTipo
	 * @return
	 * @throws Exception
	 */
	public List<RecursoParteDt> consultar(String id_recurso, String id_ProcessoTipo) throws Exception {
		String stSql="";
		List<RecursoParteDt> liTemp = new ArrayList<RecursoParteDt>();
		ResultSetTJGO rs1=null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT * FROM PROJUDI.VIEW_RECURSO_PARTE WHERE ID_RECURSO = ? "; ps.adicionarLong(id_recurso);
		stSql += " AND ID_PROC_TIPO = ? "; ps.adicionarLong(id_ProcessoTipo);
		stSql += " AND DATA_BAIXA IS NULL ";

		try{
			rs1 = consultar(stSql,ps);
			
			while (rs1.next()) {
				RecursoParteDt obTemp = new RecursoParteDt();
				associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}			
		}
		return liTemp; 
	}
	
	/**
	 * Consulta uma lista de recursos ativos. 
	 * @param id_recurso
	 * @return
	 * @throws Exception
	 */
	public List<RecursoParteDt> consultar(String id_recurso) throws Exception {
		String stSql="";
		List<RecursoParteDt> liTemp = new ArrayList<RecursoParteDt>();
		ResultSetTJGO rs1=null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT * FROM PROJUDI.VIEW_RECURSO_PARTE WHERE ID_RECURSO = ? "; ps.adicionarLong(id_recurso);
		stSql += " AND DATA_BAIXA IS NULL ";

		try{
			rs1 = consultar(stSql,ps);
			
			while (rs1.next()) {
				RecursoParteDt obTemp = new RecursoParteDt();
				associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}			
		}
		return liTemp; 
	}
}
