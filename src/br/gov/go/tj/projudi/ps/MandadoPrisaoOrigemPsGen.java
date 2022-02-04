package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MandadoPrisaoOrigemPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 2913620136507092618L;

	//---------------------------------------------------------
	public MandadoPrisaoOrigemPsGen() {


	}



//---------------------------------------------------------
	public void inserir(MandadoPrisaoOrigemDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.MANDADO_PRISAO_ORIGEM ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMandadoPrisaoOrigemCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_ORIGEM_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMandadoPrisaoOrigemCodigo());  

			stVirgula=",";
		}
		if ((dados.getMandadoPrisaoOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMandadoPrisaoOrigem());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_MANDADO_PRISAO_ORIGEM",ps));
	} 

//---------------------------------------------------------
	public void alterar(MandadoPrisaoOrigemDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.MANDADO_PRISAO_ORIGEM SET  ";
		stSql+= "MANDADO_PRISAO_ORIGEM_CODIGO = ?";		 ps.adicionarLong(dados.getMandadoPrisaoOrigemCodigo());  

		stSql+= ",MANDADO_PRISAO_ORIGEM = ?";		 ps.adicionarString(dados.getMandadoPrisaoOrigem());  

		stSql += " WHERE ID_MANDADO_PRISAO_ORIGEM  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.MANDADO_PRISAO_ORIGEM";
		stSql += " WHERE ID_MANDADO_PRISAO_ORIGEM = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public MandadoPrisaoOrigemDt consultarId(String id_mandadoprisaoorigem )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoPrisaoOrigemDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_MANDADO_PRISAO_ORIGEM WHERE ID_MANDADO_PRISAO_ORIGEM = ?";		ps.adicionarLong(id_mandadoprisaoorigem); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoPrisaoOrigemDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( MandadoPrisaoOrigemDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_MANDADO_PRISAO_ORIGEM"));
		Dados.setMandadoPrisaoOrigem(rs.getString("MANDADO_PRISAO_ORIGEM"));
		Dados.setMandadoPrisaoOrigemCodigo( rs.getString("MANDADO_PRISAO_ORIGEM_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_MANDADO_PRISAO_ORIGEM, MANDADO_PRISAO_ORIGEM FROM PROJUDI.VIEW_MANDADO_PRISAO_ORIGEM WHERE MANDADO_PRISAO_ORIGEM LIKE ?";
		stSql+= " ORDER BY MANDADO_PRISAO_ORIGEM ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				MandadoPrisaoOrigemDt obTemp = new MandadoPrisaoOrigemDt();
				obTemp.setId(rs1.getString("ID_MANDADO_PRISAO_ORIGEM"));
				obTemp.setMandadoPrisaoOrigem(rs1.getString("MANDADO_PRISAO_ORIGEM"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MANDADO_PRISAO_ORIGEM WHERE MANDADO_PRISAO_ORIGEM LIKE ?";
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


		stSql= "SELECT ID_MANDADO_PRISAO_ORIGEM as id, MANDADO_PRISAO_ORIGEM as descricao1 FROM PROJUDI.VIEW_MANDADO_PRISAO_ORIGEM WHERE MANDADO_PRISAO_ORIGEM LIKE ?";
		stSql+= " ORDER BY MANDADO_PRISAO_ORIGEM ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MANDADO_PRISAO_ORIGEM WHERE MANDADO_PRISAO_ORIGEM LIKE ?";
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
