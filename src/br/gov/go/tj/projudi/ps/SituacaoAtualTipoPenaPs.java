package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class SituacaoAtualTipoPenaPs extends Persistencia {

	private static final long serialVersionUID = -6709488765524218000L;

	public SituacaoAtualTipoPenaPs() {
	}

	public SituacaoAtualTipoPenaPs(Connection conexao){
    	Conexao = conexao;
    }
	
	public void inserir(SituacaoAtualTipoPenaDt dados) throws Exception {
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.SITUACAO_ATUAL_TIPO_PENA("; 
		stSqlValores +=  " Values (";
 
		if ((dados.getIdPenaExecucaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PENA_EXE_TIPO" ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdPenaExecucaoTipo());  
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

		dados.setId(executarInsert(stSql,"ID_SITUACAO_ATUAL_TIPO_PENA",ps)); 
	} 

	public void alterar(SituacaoAtualTipoPenaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.SITUACAO_ATUAL_TIPO_PENA SET  ";
		stSql+= "ID_PENA_EXE_TIPO = ?";		 
		ps.adicionarLong(dados.getIdPenaExecucaoTipo());  
		stSql+= ",ID_SITUACAO_ATUAL_EXE = ?";
		ps.adicionarLong(dados.getIdSituacaoAtualExecucao());  
		stSql += " WHERE ID_SITUACAO_ATUAL_TIPO_PENA  = ? "; 
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	} 

	public void excluir(String chave) throws Exception {
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.SITUACAO_ATUAL_TIPO_PENA";
		stSql += " WHERE ID_SITUACAO_ATUAL_TIPO_PENA = ?";
		ps.adicionarLong(chave); 
		executarUpdateDelete(stSql,ps);
	} 

//	public void excluirIdSituacaoAtualExecucao(String chave){
//		String stSql="";
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//		stSql= "DELETE FROM PROJUDI.SITUACAO_ATUAL_TIPO_PENA";
//		stSql += " WHERE ID_SITUACAO_ATUAL_EXE = ?";
//		ps.adicionarLong(chave); 
//
//	    executarUpdateDelete(stSql,ps);
//	} 
	
	public SituacaoAtualTipoPenaDt consultarId(String idSituacaoAtualTipoPena )  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		SituacaoAtualTipoPenaDt dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_SITUACAO_ATUAL_TIPO_PENA " +
				" WHERE ID_SITUACAO_ATUAL_TIPO_PENA = ?";	
		ps.adicionarLong(idSituacaoAtualTipoPena); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				dados= new SituacaoAtualTipoPenaDt();
				associarDt(dados, rs1);
			}
		
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dados; 
	}

	protected void associarDt(SituacaoAtualTipoPenaDt dados, ResultSetTJGO rs )  throws Exception {
		
		dados.setId(rs.getString("ID_SITUACAO_ATUAL_TIPO_PENA"));
		dados.setIdPenaExecucaoTipo(rs.getString("ID_PENA_EXE_TIPO"));
		dados.setIdSituacaoAtualExecucao(rs.getString("ID_SITUACAO_ATUAL_EXE"));
		dados.setPenaExecucaoTipo(rs.getString("PENA_EXE_TIPO"));
		
	}
} 
