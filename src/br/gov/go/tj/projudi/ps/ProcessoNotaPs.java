package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.ProcessoNotaDt;
import br.gov.go.tj.projudi.util.Mensagem;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class ProcessoNotaPs extends ProcessoNotaPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5562507398929931018L;

	private ProcessoNotaPs( ) {}
	public ProcessoNotaPs(Connection conexao) {
		Conexao = conexao;
	}

	
	public void inserir(ProcessoNotaDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.proc_nota ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getProcessoNota().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_NOTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoNota());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getCodigoTemp().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_TEMP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCodigoTemp());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PROC_NOTA",ps));


		}catch(Exception e){
			
			throw new Exception(" <{Não foi possivel criar a Nota}> ProcessoNotaPsGen.inserir() " + e.getMessage() );
			} 
	} 

	public void alterar(ProcessoNotaDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.proc_nota SET  ";	
		stSql+= "PROC_NOTA = ?";		 		ps.adicionarString(dados.getProcessoNota());  
		stSql+= ",DATA_ALTERACAO = ?";		 		ps.adicionarDateTime(new Date());  
		stSql += " WHERE ID_PROC_NOTA  = ? "; 		ps.adicionarLong(dados.getId());
		stSql += " AND   ID_USU_SERV   = ? "; 		ps.adicionarLong(dados.getId_UsuarioServentia()); 

		executarUpdateDelete(stSql,ps);

	} 
	
	public String consultarNotasJSON(String id_proc, String id_usuarioServentia, String id_Serventia ) throws Exception {

		String stSql, stSqlFrom;
		
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		JSONArray aJson = new JSONArray();

		stSql= "SELECT ID_PROC_NOTA , proc_nota, id_serv ";
		stSqlFrom= " FROM proc_nota WHERE id_proc = ? and ";		ps.adicionarLong( id_proc);
		stSqlFrom += " ( id_usu_serv  = ?";				ps.adicionarLong( id_usuarioServentia);
		
		if (id_Serventia!=null){
			stSqlFrom += " or id_serv  = ? )";				ps.adicionarLong( id_Serventia);
		}else{
			stSqlFrom += " and id_serv  is null )";
		}
		
		try{

			rs1 = consultar(stSql+stSqlFrom, ps);
			
			while (rs1.next()) {								
				JSONObject oJson = new JSONObject();
				oJson.put("id_proc_nota", rs1.getLong("ID_PROC_NOTA"));
				oJson.put("proc_nota", rs1.getString("proc_nota"));
				oJson.put("id_serv", rs1.getLong("id_serv"));
				aJson.put(oJson);
			}											

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			
		}
		
		return aJson.toString(); 
	}
	
	public void excluirNota(String id_nota, String id_UsuarioServentia, String isPrivada, String id_Serventia) throws Exception {
		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.proc_nota";
		stSql += " WHERE ID_PROC_NOTA = ?";		ps.adicionarLong(id_nota); 
		
		if(isPrivada.equalsIgnoreCase("0")){
			stSql += " AND ID_SERV = ?";		ps.adicionarLong(id_Serventia);
		}
		
		int retorno = executarUpdateDelete(stSql,ps);
		if (retorno ==0) {
			throw new MensagemException("Essa nota não pode ser excluída, pois deve ser de outro usuário");
		}
		
	}
}
