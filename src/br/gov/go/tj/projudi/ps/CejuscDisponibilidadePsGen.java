package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt;


public class CejuscDisponibilidadePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3847818634210116452L;

	public CejuscDisponibilidadePsGen() {


	}

	public void inserir(CejuscDisponibilidadeDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.CEJUSC_DISPONIBILIDADE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_UsuCejusc().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_CEJUSC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuCejusc());  

			stVirgula=",";
		}
		if ((dados.getId_AudiTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AudiTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Serv().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serv());  

			stVirgula=",";
		}		
		if ((dados.getDomingo().length()>0)) {
			 stSqlCampos+=   stVirgula + "DOMINGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getDomingo());  

			stVirgula=",";
		}
		if ((dados.getSegunda().length()>0)) {
			 stSqlCampos+=   stVirgula + "SEGUNDA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getSegunda());  

			stVirgula=",";
		}
		if ((dados.getTerca().length()>0)) {
			 stSqlCampos+=   stVirgula + "TERCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTerca());  

			stVirgula=",";
		}
		if ((dados.getQuarta().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUARTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getQuarta());  

			stVirgula=",";
		}
		if ((dados.getQuinta().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUINTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getQuinta());  

			stVirgula=",";
		}
		if ((dados.getSexta().length()>0)) {
			 stSqlCampos+=   stVirgula + "SEXTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getSexta());  

			stVirgula=",";
		}
		if ((dados.getSabado().length()>0)) {
			 stSqlCampos+=   stVirgula + "SABADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getSabado());  

			stVirgula=",";
		}		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_CEJUSC_DISPONIBILIDADE",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> CejuscDisponibilidadePsGen.inserir() " + e.getMessage() );
			} 
	} 

	public void alterar(CejuscDisponibilidadeDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.CEJUSC_DISPONIBILIDADE SET  ";
		
		stSql+= "ID_USU_CEJUSC = ?";		 ps.adicionarLong(dados.getId_UsuCejusc());
		
		stSql+= ",ID_AUDI_TIPO = ?";		 ps.adicionarLong(dados.getId_AudiTipo());  
		
		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serv());		  

		stSql+= ",DOMINGO = ?";		 ps.adicionarLong(dados.getDomingo());  

		stSql+= ",SEGUNDA = ?";		 ps.adicionarLong(dados.getSegunda());  

		stSql+= ",TERCA = ?";		 ps.adicionarLong(dados.getTerca());  

		stSql+= ",QUARTA = ?";		 ps.adicionarLong(dados.getQuarta());  

		stSql+= ",QUINTA = ?";		 ps.adicionarLong(dados.getQuinta());  

		stSql+= ",SEXTA = ?";		 ps.adicionarLong(dados.getSexta());  

		stSql+= ",SABADO = ?";		 ps.adicionarLong(dados.getSabado());  

		stSql += " WHERE ID_CEJUSC_DISPONIBILIDADE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.CEJUSC_DISPONIBILIDADE";
		stSql += " WHERE ID_CEJUSC_DISPONIBILIDADE = ?";		ps.adicionarLong(chave); 



		executarUpdateDelete(stSql,ps);
	} 

	public CejuscDisponibilidadeDt consultarId(String id_cejuscdisponibilidade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CejuscDisponibilidadeDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE WHERE ID_CEJUSC_DISPONIBILIDADE = ?";		ps.adicionarLong(id_cejuscdisponibilidade); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CejuscDisponibilidadeDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( CejuscDisponibilidadeDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_CEJUSC_DISPONIBILIDADE"));
			Dados.setId_UsuCejusc( rs.getString("ID_USU_CEJUSC"));
			Dados.setNome( rs.getString("NOME"));
			Dados.setServ(rs.getString("SERV"));
			Dados.setId_AudiTipo( rs.getString("ID_AUDI_TIPO"));
			Dados.setAudiTipo( rs.getString("AUDI_TIPO"));
			Dados.setId_Serv( rs.getString("ID_SERV"));			
			Dados.setDomingo( rs.getString("DOMINGO"));
			Dados.setSegunda( rs.getString("SEGUNDA"));
			Dados.setTerca( rs.getString("TERCA"));
			Dados.setQuarta( rs.getString("QUARTA"));
			Dados.setQuinta( rs.getString("QUINTA"));
			Dados.setSexta( rs.getString("SEXTA"));
			Dados.setSabado( rs.getString("SABADO"));			
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CEJUSC_DISPONIBILIDADE, SERV FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE WHERE SERV LIKE ?";
		stSql+= " ORDER BY SERV ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				CejuscDisponibilidadeDt obTemp = new CejuscDisponibilidadeDt();
				obTemp.setId(rs1.getString("ID_CEJUSC_DISPONIBILIDADE"));
				obTemp.setServ(rs1.getString("SERV"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE WHERE SERV LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return liTemp; 
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CEJUSC_DISPONIBILIDADE as id, SERV as descricao1 FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE WHERE SERV LIKE ?";
		stSql+= " ORDER BY SERV ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE WHERE SERV LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}

} 
