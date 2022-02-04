package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt;


public class PonteiroCejuscStatuPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1350638383244796975L;

	//---------------------------------------------------------
	public PonteiroCejuscStatuPsGen() {


	}



//---------------------------------------------------------
	public void inserir(PonteiroCejuscStatuDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PONTEIRO_CEJUSC_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPonteiroCejuscStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "PONTEIRO_CEJUSC_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPonteiroCejuscStatus());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PONTEIRO_CEJUSC_STATUS",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> PonteiroCejuscStatuPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(PonteiroCejuscStatuDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.PONTEIRO_CEJUSC_STATUS SET  ";
		stSql+= "PONTEIRO_CEJUSC_STATUS = ?";		 ps.adicionarString(dados.getPonteiroCejuscStatus());  

		stSql += " WHERE ID_PONTEIRO_CEJUSC_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PONTEIRO_CEJUSC_STATUS";
		stSql += " WHERE ID_PONTEIRO_CEJUSC_STATUS = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public PonteiroCejuscStatuDt consultarId(String id_ponteirocejuscstatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PonteiroCejuscStatuDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PONTEIRO_CEJUSC_STATUS WHERE ID_PONTEIRO_CEJUSC_STATUS = ?";		ps.adicionarLong(id_ponteirocejuscstatus); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PonteiroCejuscStatuDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( PonteiroCejuscStatuDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PONTEIRO_CEJUSC_STATUS"));
			Dados.setPonteiroCejuscStatus(rs.getString("PONTEIRO_CEJUSC_STATUS"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PONTEIRO_CEJUSC_STATUS, PONTEIRO_CEJUSC_STATUS FROM PROJUDI.VIEW_PONTEIRO_CEJUSC_STATUS WHERE PONTEIRO_CEJUSC_STATUS LIKE ?";
		stSql+= " ORDER BY PONTEIRO_CEJUSC_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				PonteiroCejuscStatuDt obTemp = new PonteiroCejuscStatuDt();
				obTemp.setId(rs1.getString("ID_PONTEIRO_CEJUSC_STATUS"));
				obTemp.setPonteiroCejuscStatus(rs1.getString("PONTEIRO_CEJUSC_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PONTEIRO_CEJUSC_STATUS WHERE PONTEIRO_CEJUSC_STATUS LIKE ?";
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


		stSql= "SELECT ID_PONTEIRO_CEJUSC_STATUS as id, PONTEIRO_CEJUSC_STATUS as descricao1 FROM PROJUDI.VIEW_PONTEIRO_CEJUSC_STATUS WHERE PONTEIRO_CEJUSC_STATUS LIKE ?";
		stSql+= " ORDER BY PONTEIRO_CEJUSC_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PONTEIRO_CEJUSC_STATUS WHERE PONTEIRO_CEJUSC_STATUS LIKE ?";
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
