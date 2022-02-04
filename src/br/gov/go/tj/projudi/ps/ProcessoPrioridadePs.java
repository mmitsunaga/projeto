package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoPrioridadePs extends ProcessoPrioridadePsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 641085390460567171L;

    public ProcessoPrioridadePs(Connection conexao){
    	Conexao = conexao;
	}

    
  //---------------------------------------------------------
  	public void inserir(ProcessoPrioridadeDt dados ) throws Exception {

  		String stSqlCampos="";
  		String stSqlValores="";
  		String stSql="";
  		String stVirgula="";
  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		////System.out.println("....psProcessoPrioridadeinserir()");
  		stSqlCampos= "INSERT INTO PROJUDI.PROC_PRIOR ("; 

  		stSqlValores +=  " Values (";
   
  		if ((dados.getProcessoPrioridade().length()>0)) {
  			 stSqlCampos+=   stVirgula + "PROC_PRIOR " ;
  			 stSqlValores+=   stVirgula + "? " ;
  			 ps.adicionarString(dados.getProcessoPrioridade());  

  			stVirgula=",";
  		}
  		if ((dados.getProcessoPrioridadeCodigo().length()>0)) {
  			 stSqlCampos+=   stVirgula + "PROC_PRIOR_CODIGO " ;
  			 stSqlValores+=   stVirgula + "? " ;
  			 ps.adicionarLong(dados.getProcessoPrioridadeCodigo());  

  			stVirgula=",";
  		}
  		if ((dados.getProcessoPrioridadeOrdem().length()>0)) {
 			 stSqlCampos+=   stVirgula + "PROC_PRIOR_ORDEM " ;
 			 stSqlValores+=   stVirgula + "? " ;
 			 ps.adicionarLong(dados.getProcessoPrioridadeOrdem());  

 			stVirgula=",";
 		}
  		stSqlCampos+= ")";
  		stSqlValores+= ")";
  		stSql+= stSqlCampos + stSqlValores; 
  		////System.out.println("....Sql " + stSql);

  		
  			dados.setId(executarInsert(stSql,"ID_PROC_PRIOR",ps));
  			////System.out.println("....Execução OK"  );

  		 
  	} 

  //---------------------------------------------------------
  	public void alterar(ProcessoPrioridadeDt dados) throws Exception{

  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		String stSql="";

  		////System.out.println("....psProcessoPrioridadealterar()");

  		stSql= "UPDATE PROJUDI.PROC_PRIOR SET  ";
  		stSql+= "PROC_PRIOR = ?";		 ps.adicionarString(dados.getProcessoPrioridade());  

  		stSql+= ",PROC_PRIOR_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoPrioridadeCodigo());  
  		stSql+= ",PROC_PRIOR_ORDEM = ?";		 ps.adicionarLong(dados.getProcessoPrioridadeOrdem());

  		stSql += " WHERE ID_PROC_PRIOR  = ? "; 		ps.adicionarLong(dados.getId()); 

  		executarUpdateDelete(stSql,ps);
  	
  	} 
  	
	/**
	 * Obter registro de ProcessoPrioridade baseado no código (ProcessoPrioridadeCodigo)
	 */
	public ProcessoPrioridadeDt consultarProcessoPrioridadeCodigo(String processoPrioridadeCodigo) throws Exception {
		String Sql;
		ProcessoPrioridadeDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM VIEW_PROC_PRIOR pp WHERE pp.PROC_PRIOR_CODIGO= ?";
		ps.adicionarLong(processoPrioridadeCodigo);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados = new ProcessoPrioridadeDt();
				Dados.setId(rs1.getString("ID_PROC_PRIOR"));
				Dados.setProcessoPrioridade(rs1.getString("PROC_PRIOR"));
				Dados.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Sobrescrevendo método para retornar o Código
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String Sql2;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_PROC_PRIOR,PROC_PRIOR,PROC_PRIOR_CODIGO FROM PROJUDI.VIEW_PROC_PRIOR WHERE PROC_PRIOR LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql += " ORDER BY PROC_PRIOR";
		
		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				ProcessoPrioridadeDt obTemp = new ProcessoPrioridadeDt();
				obTemp.setId(rs1.getString("ID_PROC_PRIOR"));
				obTemp.setProcessoPrioridade(rs1.getString("PROC_PRIOR"));
				obTemp.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql2 = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_PROC_PRIOR WHERE PROC_PRIOR LIKE ?";
			rs2 = consultar(Sql2,ps);
			
			if (rs2.next())	liTemp.add(rs2.getLong("QUANTIDADE"));

			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e) {}
        } 
		return liTemp;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String Sql2;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		Sql = "SELECT ID_PROC_PRIOR AS ID, PROC_PRIOR AS DESCRICAO1, PROC_PRIOR_CODIGO AS DESCRICAO2, PROC_PRIOR_ORDEM AS DESCRICAO3 FROM PROJUDI.VIEW_PROC_PRIOR";
		Sql += " WHERE PROC_PRIOR LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql += " ORDER BY PROC_PRIOR_ORDEM DESC, PROC_PRIOR DESC";
		
		try{
			rs1 = consultar(Sql,ps);
			Sql2 = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_PROC_PRIOR WHERE PROC_PRIOR LIKE ?";
			rs2 = consultar(Sql2,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
        	try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e) {}
        } 
		return stTemp;
	}

	public ProcessoPrioridadeDt consultarId(String id_processoprioridade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoPrioridadeDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoPrioridade)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PRIOR WHERE ID_PROC_PRIOR = ?";		ps.adicionarLong(id_processoprioridade); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoPrioridade  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoPrioridadeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoPrioridadeDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_PROC_PRIOR"));
		Dados.setProcessoPrioridade(rs.getString("PROC_PRIOR"));
		Dados.setProcessoPrioridadeCodigo( rs.getString("PROC_PRIOR_CODIGO"));
		Dados.setProcessoPrioridadeOrdem( rs.getString("PROC_PRIOR_ORDEM"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
}