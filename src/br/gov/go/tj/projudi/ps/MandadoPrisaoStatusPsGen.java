package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MandadoPrisaoStatusPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 885277604488804360L;

	//---------------------------------------------------------
	public MandadoPrisaoStatusPsGen() {


	}



//---------------------------------------------------------
	public void inserir(MandadoPrisaoStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.MANDADO_PRISAO_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMandadoPrisaoStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMandadoPrisaoStatusCodigo());  

			stVirgula=",";
		}
		if ((dados.getMandadoPrisaoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMandadoPrisaoStatus());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_MANDADO_PRISAO_STATUS",ps));
	} 

//---------------------------------------------------------
	public void alterar(MandadoPrisaoStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.MANDADO_PRISAO_STATUS SET  ";
		stSql+= "MANDADO_PRISAO_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getMandadoPrisaoStatusCodigo());  

		stSql+= ",MANDADO_PRISAO_STATUS = ?";		 ps.adicionarString(dados.getMandadoPrisaoStatus());  

		stSql += " WHERE ID_MANDADO_PRISAO_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.MANDADO_PRISAO_STATUS";
		stSql += " WHERE ID_MANDADO_PRISAO_STATUS = ?";		ps.adicionarLong(chave); 


		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public MandadoPrisaoStatusDt consultarId(String id_mandadoprisaostatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoPrisaoStatusDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_MANDADO_PRISAO_STATUS WHERE ID_MANDADO_PRISAO_STATUS = ?";		ps.adicionarLong(id_mandadoprisaostatus); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoPrisaoStatusDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( MandadoPrisaoStatusDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_MANDADO_PRISAO_STATUS"));
		Dados.setMandadoPrisaoStatus(rs.getString("MANDADO_PRISAO_STATUS"));
		Dados.setMandadoPrisaoStatusCodigo( rs.getString("MANDADO_PRISAO_STATUS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_MANDADO_PRISAO_STATUS, MANDADO_PRISAO_STATUS FROM PROJUDI.VIEW_MANDADO_PRISAO_STATUS WHERE MANDADO_PRISAO_STATUS LIKE ?";
		stSql+= " ORDER BY MANDADO_PRISAO_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				MandadoPrisaoStatusDt obTemp = new MandadoPrisaoStatusDt();
				obTemp.setId(rs1.getString("ID_MANDADO_PRISAO_STATUS"));
				obTemp.setMandadoPrisaoStatus(rs1.getString("MANDADO_PRISAO_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MANDADO_PRISAO_STATUS WHERE MANDADO_PRISAO_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
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


		stSql= "SELECT ID_MANDADO_PRISAO_STATUS as id, MANDADO_PRISAO_STATUS as descricao1 FROM PROJUDI.VIEW_MANDADO_PRISAO_STATUS WHERE MANDADO_PRISAO_STATUS LIKE ?";
		stSql+= " ORDER BY MANDADO_PRISAO_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MANDADO_PRISAO_STATUS WHERE MANDADO_PRISAO_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
