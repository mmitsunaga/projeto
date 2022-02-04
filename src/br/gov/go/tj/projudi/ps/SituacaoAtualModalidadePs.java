package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class SituacaoAtualModalidadePs extends Persistencia {

	private static final long serialVersionUID = 1164930033773378454L;

	public SituacaoAtualModalidadePs() {
	}
	
	public SituacaoAtualModalidadePs(Connection conexao){
    	Conexao = conexao;
    }
	
	public void inserir(SituacaoAtualModalidadeDt dados) throws Exception {
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.SITUACAO_ATUAL_MODALIDADE("; 
		stSqlValores +=  " Values (";
 
		if ((dados.getIdModalidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MODALIDADE" ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdModalidade());  
			stVirgula=",";
		}
		if ((dados.getIdSituacaoAtualExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SITUACAO_ATUAL_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdSituacaoAtualExecucao());  
			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_SITUACAO_ATUAL_MODALIDADE",ps)); 
	} 

	public void alterar(SituacaoAtualModalidadeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.SITUACAO_ATUAL_MODALIDADE SET  ";
		stSql+= "ID_MODALIDADE = ?";		 
		ps.adicionarLong(dados.getIdModalidade());  
		stSql+= ",ID_SITUACAO_ATUAL_EXE = ?";
		ps.adicionarLong(dados.getIdSituacaoAtualExecucao());  
		stSql += " WHERE ID_SITUACAO_ATUAL_MODALIDADE  = ? "; 
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	} 

	public void excluir(String chave) throws Exception {
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.SITUACAO_ATUAL_MODALIDADE";
		stSql += " WHERE ID_SITUACAO_ATUAL_MODALIDADE = ?";
		ps.adicionarLong(chave); 
		executarUpdateDelete(stSql,ps);

		
	} 

//	public void excluirIdSituacaoAtualExecucao(String chave){
//		String stSql="";
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//		stSql= "DELETE FROM PROJUDI.SITUACAO_ATUAL_MODALIDADE";
//		stSql += " WHERE ID_SITUACAO_ATUAL_EXE = ?";
//		ps.adicionarLong(chave); 
//
//		executarUpdateDelete(stSql,ps);
//	} 
	
	public SituacaoAtualModalidadeDt consultarId(String idSituacaoAtualModalidade )  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		SituacaoAtualModalidadeDt dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_SITUACAO_ATUAL_MODALIDADE " +
				" WHERE ID_SITUACAO_ATUAL_MODALIDADE = ?";	
		ps.adicionarLong(idSituacaoAtualModalidade); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				dados= new SituacaoAtualModalidadeDt();
				associarDt(dados, rs1);
			}
		
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dados; 
	}

	protected void associarDt(SituacaoAtualModalidadeDt dados, ResultSetTJGO rs )  throws Exception {
		
		dados.setId(rs.getString("ID_SITUACAO_ATUAL_MODALIDADE"));
		dados.setIdModalidade(rs.getString("ID_MODALIDADE"));
		dados.setIdSituacaoAtualExecucao(rs.getString("ID_SITUACAO_ATUAL_EXE"));
		dados.setModalidade(rs.getString("MODALIDADE"));
		
	}
} 
