package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.MandadoPrisaoArquivoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MandadoPrisaoArquivoPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7602033378784259168L;

	public MandadoPrisaoArquivoPs() {

	}
	    
    public MandadoPrisaoArquivoPs(Connection conexao){
    	Conexao = conexao;
	}

	public void inserir(MandadoPrisaoArquivoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.MANDADO_PRISAO_ARQUIVO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_MandadoPrisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MANDADO_PRISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MandadoPrisao());  

			stVirgula=",";
		}
		if ((dados.getId_Arquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQUIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getId_Arquivo());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		
			dados.setId(executarInsert(stSql,"ID_MANDADO_PRISAO_ARQUIVO",ps));

		 
	} 


	public void alterar(MandadoPrisaoArquivoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.MANDADO_PRISAO_ARQUIVO SET  ";
		stSql+= "ID_MANDADO_PRISAO = ?";		 ps.adicionarLong(dados.getId_MandadoPrisao());  
		stSql+= ",ID_ARQUIVO = ?";		 ps.adicionarLong(dados.getId_Arquivo());  
		stSql += " WHERE ID_MANDADO_PRISAO_ARQUIVO = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 


	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.MANDADO_PRISAO_ARQUIVO";
		stSql += " WHERE ID_MANDADO_PRISAO_ARQUIVO = ?";		ps.adicionarLong(chave); 
			executarUpdateDelete(stSql,ps);

		

	} 


	public MandadoPrisaoArquivoDt consultarId(String idMandadoPrisaoArquivo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoPrisaoArquivoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.MANDADO_PRISAO_ARQUIVO WHERE ID_MANDADO_PRISAO_ARQUIVO = ?";		
		ps.adicionarLong(idMandadoPrisaoArquivo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados = new MandadoPrisaoArquivoDt();
				associarDt(Dados, rs1);
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	public String consultarIdArquivo(String idMandadoPrisao)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stRetorno = "";

		stSql= "SELECT ID_ARQUIVO FROM PROJUDI.MANDADO_PRISAO_ARQUIVO WHERE ID_MANDADO_PRISAO = ?";		
		ps.adicionarLong(idMandadoPrisao); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				stRetorno  = rs1.getString("ID_ARQUIVO");
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return stRetorno ; 
	}

	
	protected void associarDt( MandadoPrisaoArquivoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_MANDADO_PRISAO_ARQUIVO"));
		Dados.setId_Arquivo( rs.getString("ID_ARQUIVO"));
		Dados.setId_MandadoPrisao( rs.getString("ID_MANDADO_PRISAO"));
		
	}


} 
