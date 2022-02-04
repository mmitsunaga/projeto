package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.PonteiroCejuscDt;


public class PonteiroCejuscPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6813458644961756578L;

	//---------------------------------------------------------
	public PonteiroCejuscPsGen() {


	}



//---------------------------------------------------------
	public void inserir(PonteiroCejuscDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PONTEIRO_CEJUSC ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_UsuCejusc().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_CEJUSC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuCejusc());  

			stVirgula=",";
		}
		if ((dados.getId_Serv().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serv());  

			stVirgula=",";
		}
		if ((dados.getId_UsuServConfirmou().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_CONFIRMOU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuServConfirmou());  

			stVirgula=",";
		}
		if ((dados.getId_UsuServCompareceu().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_COMPARECEU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuServCompareceu());  

			stVirgula=",";
		}
		if ((dados.getId_ServCargoBanca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO_BANCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServCargoBanca());  

			stVirgula=",";
		}
		if ((dados.getData().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getData());  

			stVirgula=",";
		}
		if ((dados.getId_PonteiroCejuscStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PONTEIRO_CEJUSC_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PonteiroCejuscStatus());  

			stVirgula=",";
		}
		if ((dados.getId_AudiTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AudiTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PONTEIRO_CEJUSC",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> PonteiroCejuscPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(PonteiroCejuscDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.PONTEIRO_CEJUSC SET  ";
		stSql+= "ID_USU_CEJUSC = ?";		 ps.adicionarLong(dados.getId_UsuCejusc());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serv());  

		stSql+= ",ID_USU_SERV_CONFIRMOU = ?";		 ps.adicionarLong(dados.getId_UsuServConfirmou());  

		stSql+= ",ID_USU_SERV_COMPARECEU = ?";		 ps.adicionarLong(dados.getId_UsuServCompareceu());  

		stSql+= ",ID_SERV_CARGO_BANCA = ?";		 ps.adicionarLong(dados.getId_ServCargoBanca());  

		stSql+= ",ID_PONTEIRO_CEJUSC_STATUS = ?";		 ps.adicionarLong(dados.getId_PonteiroCejuscStatus());
		
		stSql+= ",ID_AUDI_TIPO = ?";		 ps.adicionarLong(dados.getId_AudiTipo());  

		stSql += " WHERE ID_PONTEIRO_CEJUSC  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PONTEIRO_CEJUSC";
		stSql += " WHERE ID_PONTEIRO_CEJUSC = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public PonteiroCejuscDt consultarId(String id_ponteirocejusc )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PonteiroCejuscDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PONTEIRO_CEJUSC WHERE ID_PONTEIRO_CEJUSC = ?";		ps.adicionarLong(id_ponteirocejusc); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PonteiroCejuscDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( PonteiroCejuscDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PONTEIRO_CEJUSC"));
			Dados.setUsuCejusc(rs.getString("USU_CEJUSC"));
			Dados.setId_UsuCejusc( rs.getString("ID_USU_CEJUSC"));
			Dados.setId_Serv( rs.getString("ID_SERV"));
			Dados.setServ( rs.getString("SERV"));
			Dados.setId_UsuServConfirmou( rs.getString("ID_USU_SERV_CONFIRMOU"));
			Dados.setUsuServConfirmou( rs.getString("USU_SERV_CONFIRMOU"));
			Dados.setId_UsuServCompareceu( rs.getString("ID_USU_SERV_COMPARECEU"));
			Dados.setUsuServCompareceu( rs.getString("USU_SERV_COMPARECEU"));
			Dados.setId_ServCargoBanca( rs.getString("ID_SERV_CARGO_BANCA"));
			Dados.setData( Funcoes.FormatarDataHora(rs.getString("DATA")));
			Dados.setId_PonteiroCejuscStatus( rs.getString("ID_PONTEIRO_CEJUSC_STATUS"));
			Dados.setPonteiroCejuscStatus( rs.getString("PONTEIRO_CEJUSC_STATUS"));
			Dados.setId_AudiTipo( rs.getString("ID_AUDI_TIPO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PONTEIRO_CEJUSC, USU_CEJUSC FROM PROJUDI.VIEW_PONTEIRO_CEJUSC WHERE USU_CEJUSC LIKE ?";
		stSql+= " ORDER BY USU_CEJUSC ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				PonteiroCejuscDt obTemp = new PonteiroCejuscDt();
				obTemp.setId(rs1.getString("ID_PONTEIRO_CEJUSC"));
				obTemp.setUsuCejusc(rs1.getString("USU_CEJUSC"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PONTEIRO_CEJUSC WHERE USU_CEJUSC LIKE ?";
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

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PONTEIRO_CEJUSC as id, USU_CEJUSC as descricao1 FROM PROJUDI.VIEW_PONTEIRO_CEJUSC WHERE USU_CEJUSC LIKE ?";
		stSql+= " ORDER BY USU_CEJUSC ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PONTEIRO_CEJUSC WHERE USU_CEJUSC LIKE ?";
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
