package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioFoneDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class UsuarioFonePs extends UsuarioFonePsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2797467670695238442L;
	
	private UsuarioFonePs( ) {}
	public UsuarioFonePs(Connection conexao) {
		Conexao = conexao;
	}
//
	public UsuarioFoneDt consultarUsuarioCpfImei(String cpf, String fone, String imei) throws Exception {
		// TODO Auto-generated method stub
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioFoneDt Dados=null;

		stSql= "SELECT * FROM projudi.VIEW_USU_FONE ";
		stSql += " WHERE CPF = ?";		ps.adicionarString(cpf); 
		stSql += " AND IMEI  = ?";		ps.adicionarString(imei);

		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioFoneDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	//---------------------------------------------------------
	public void inserir(UsuarioFoneDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO projudi.USU_FONE ("; 

		stSqlValores +=  " Values (";
 
		
		 stSqlCampos+=   stVirgula + "ID_USU " ;
		 stSqlValores+=   stVirgula + "? ";				 ps.adicionarLong(dados.getId_Usuario());  
		stVirgula=",";
		
		 stSqlCampos+=   stVirgula + "IMEI " ;
		 stSqlValores+=   stVirgula + "? " ;			 ps.adicionarString(dados.getImei());  
		stVirgula=",";

		 stSqlCampos+=   stVirgula + "FONE " ;
		 stSqlValores+=   stVirgula + "? " ;			 ps.adicionarString(dados.getFone());  
		stVirgula=",";

		 stSqlCampos+=   stVirgula + "DATA_PEDIDO " ;
		 stSqlValores+=   stVirgula + "? " ;			 ps.adicionarDateTime(new Date());  

		stVirgula=",";

		stSqlCampos+= ")";
		stSqlValores+= ")";
		
		stSql+= stSqlCampos + stSqlValores; 

		try {
			dados.setId(executarInsert(stSql,"ID_USU_FONE",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> UsuarioFonePsGen.inserir() " + e.getMessage() );
			} 
	} 
	
	public void salvarCodigo(String id_usu_fone, String codigo, long validade) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE projudi.USU_FONE SET  ";

		stSql+= "CODIGO = ?";		 ps.adicionarString(codigo);  
		stSql+= ",CODIGO_VALIDADE = ?";		 ps.adicionarLong(validade);  
		stSql += " WHERE ID_USU_FONE  = ? "; 		ps.adicionarLong(id_usu_fone); 
		executarUpdateDelete(stSql,ps);


	}
	public String consultarCodigoValidacao(String id_Usuario) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stRetorno =null;


		stSql= "SELECT CODIGO FROM projudi.VIEW_USU_FONE";
		stSql+= " WHERE ID_USU = ?";				ps.adicionarLong(id_Usuario);
		stSql+= " AND  CODIGO_VALIDADE <= ?";		ps.adicionarLong((System.currentTimeMillis()+60000));
		stSql+= " AND  DATA_LIBERACAO IS NOT NULL";
		
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				stRetorno = rs1.getString("CODIGO");				
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return stRetorno; 
	}
	public boolean temDuplaAutentificacao(String id_Usuario) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		boolean boRetorno =false;


		stSql= "SELECT CODIGO FROM projudi.VIEW_USU_FONE";
		stSql+= " WHERE ID_USU = ?";				ps.adicionarLong(id_Usuario);
		stSql+= " AND  DATA_LIBERACAO is not null";	
		
		
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				boRetorno = true;				
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return boRetorno; 
	}
	public List consultarFones(String id_Usuario) throws Exception {
		String stSql, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * ";
		stSqlFrom= " FROM projudi.VIEW_USU_FONE WHERE ID_USU = ?"; 			ps.adicionarLong(id_Usuario);

		try{
			rs1 = consultar(stSql + stSqlFrom , ps);
			while (rs1.next()) {
				UsuarioFoneDt obTemp = new UsuarioFoneDt();
				associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}


		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		
		}
			return liTemp; 
	}
	public void bloquear(String id_usuarioFone) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE projudi.USU_FONE SET  ";  
		stSql+= " data_liberacao = null";  
		stSql += " WHERE ID_USU_FONE  = ? "; 		ps.adicionarLong(id_usuarioFone); 
		executarUpdateDelete(stSql,ps);
		
	}
	public void liberar(String id_usuarioFone, String id_usu) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		
		stSql= "UPDATE projudi.USU_FONE SET  ";  
		stSql+= " data_liberacao = null ";  
		stSql += " WHERE ID_USU  = ? "; 			ps.adicionarLong(id_usu);
		executarUpdateDelete(stSql,ps);
		
		ps = new PreparedStatementTJGO();
		
		stSql= "UPDATE projudi.USU_FONE SET  ";  
		stSql+= " data_liberacao = sysdate ";  
		stSql+= " WHERE ID_USU_FONE  = ? "; 		ps.adicionarLong(id_usuarioFone); 
		
		executarUpdateDelete(stSql,ps);
		
	} 
}
