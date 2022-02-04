package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class FormaCumprimentoExecucaoPs extends Persistencia {


	private static final long serialVersionUID = 8667424828181060497L;

	public FormaCumprimentoExecucaoPs() {

	}

	public FormaCumprimentoExecucaoPs(Connection conexao){
    	Conexao = conexao;
    }


	public void inserir(FormaCumprimentoExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSqlCampos= "INSERT INTO PROJUDI.FORMA_CUMPRIMENTO_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getFormaCumprimentoExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "FORMA_CUMPRIMENTO_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getFormaCumprimentoExecucao());  

			stVirgula=",";
		}
		if ((dados.getFormaCumprimentoExecucaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "FORMA_CUMPRIMENTO_EXE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getFormaCumprimentoExecucaoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		
			dados.setId(executarInsert(stSql,"ID_FORMA_CUMPRIMENTO_EXE",ps));

		 
	} 

	public void alterar(FormaCumprimentoExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";


		stSql= "UPDATE PROJUDI.FORMA_CUMPRIMENTO_EXE SET  ";
		stSql+= "FORMA_CUMPRIMENTO_EXE = ?";		 
		ps.adicionarString(dados.getFormaCumprimentoExecucao());  
		stSql+= ",FORMA_CUMPRIMENTO_EXE_CODIGO = ?";		 
		ps.adicionarLong(dados.getFormaCumprimentoExecucaoCodigo());  
		stSql += " WHERE ID_FORMA_CUMPRIMENTO_EXE  = ? "; 		
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.FORMA_CUMPRIMENTO_EXE";
		stSql += " WHERE ID_FORMA_CUMPRIMENTO_EXE = ?";		
		ps.adicionarLong(chave); 
			executarUpdateDelete(stSql,ps);

		

	} 

	public FormaCumprimentoExecucaoDt consultarId(String id_eventoexecucaostatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		FormaCumprimentoExecucaoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_FORMA_CUMPRIMENTO_EXE WHERE ID_FORMA_CUMPRIMENTO_EXE = ?";	
		ps.adicionarLong(id_eventoexecucaostatus); 


		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new FormaCumprimentoExecucaoDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( FormaCumprimentoExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_FORMA_CUMPRIMENTO_EXE"));
		Dados.setFormaCumprimentoExecucao(rs.getString("FORMA_CUMPRIMENTO_EXE"));
		Dados.setFormaCumprimentoExecucaoCodigo( rs.getString("FORMA_CUMPRIMENTO_EXE_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_FORMA_CUMPRIMENTO_EXE, FORMA_CUMPRIMENTO_EXE";
		stSqlFrom = " FROM PROJUDI.VIEW_FORMA_CUMPRIMENTO_EXE";
		stSqlFrom += " WHERE FORMA_CUMPRIMENTO_EXE LIKE ?";
		stSqlOrder = " ORDER BY FORMA_CUMPRIMENTO_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			if (posicao.length() > 0)
				rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			else 
				rs1 = consultar(stSql + stSqlFrom + stSqlOrder, ps);

			while (rs1.next()) {
				FormaCumprimentoExecucaoDt obTemp = new FormaCumprimentoExecucaoDt();
				obTemp.setId(rs1.getString("ID_FORMA_CUMPRIMENTO_EXE"));
				obTemp.setFormaCumprimentoExecucao(rs1.getString("FORMA_CUMPRIMENTO_EXE"));
				liTemp.add(obTemp);
			}
			if (posicao.length() > 0){
				stSql= "SELECT COUNT(*) as QUANTIDADE";
				rs2 = consultar(stSql + stSqlFrom, ps);
				while (rs2.next()) {
					liTemp.add(rs2.getLong("QUANTIDADE"));
				}	
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		
		String Sql="";
		String SqlFrom ="";
		String SqlOrder ="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
				
		Sql = "SELECT ID_FORMA_CUMPRIMENTO_EXE as id, FORMA_CUMPRIMENTO_EXE as descricao1, FORMA_CUMPRIMENTO_EXE_CODIGO as descricao2";
		SqlFrom = " FROM projudi.VIEW_FORMA_CUMPRIMENTO_EXE" ;
		SqlFrom += " WHERE FORMA_CUMPRIMENTO_EXE LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY FORMA_CUMPRIMENTO_EXE";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			
			ps = new PreparedStatementTJGO();

			Sql = "SELECT COUNT(*) AS QUANTIDADE";			
			ps.adicionarString("%"+ descricao +"%");
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
} 
