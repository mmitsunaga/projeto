package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoParteAlcunhaPs extends ProcessoParteAlcunhaPsGen{

	public ProcessoParteAlcunhaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * 
     */
    private static final long serialVersionUID = 7815034638325542579L;

//

    public List listarAlcunha(String idProcessoParte)  throws Exception {
    	List lista = new ArrayList();
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_ALCUNHA WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(idProcessoParte); 
		try{
		
			rs1 = consultar(stSql,ps);
			while (rs1.next()) {
				ProcessoParteAlcunhaDt dados= new ProcessoParteAlcunhaDt();
				associarDt(dados, rs1);
				lista.add(dados);
			}
			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
    
    public void inserir(ProcessoParteAlcunhaDt dados ) throws Exception {
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_ALCUNHA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_Alcunha().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ALCUNHA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Alcunha());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_PROC_PARTE_ALCUNHA",ps));

		 
	} 

	public void alterar(ProcessoParteAlcunhaDt dados) throws Exception{
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PROC_PARTE_ALCUNHA SET  ";
		stSql+= "ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  
		stSql+= ",ID_ALCUNHA = ?";		 ps.adicionarLong(dados.getId_Alcunha());  
		stSql += " WHERE ID_PROC_PARTE_ALCUNHA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 
	
}
