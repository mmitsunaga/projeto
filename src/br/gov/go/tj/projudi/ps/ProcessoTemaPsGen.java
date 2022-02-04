package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;


public class ProcessoTemaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 4738593355074265393L;

	//---------------------------------------------------------
	public ProcessoTemaPsGen() {


	}

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PROC_TEMA";
		stSql += " WHERE ID_PROC_TEMA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoTemaDt consultarId(String id_proctema )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoTemaDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_TEMA WHERE ID_PROC_TEMA = ?";		ps.adicionarLong(id_proctema); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoTemaDt();
				associarDt(Dados, rs1);
			}

		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoTemaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROC_TEMA"));
		Dados.setProcNumero(rs.getString("PROC_NUMERO"));
		Dados.setId_Tema( rs.getString("ID_TEMA"));
		Dados.setId_Proc( rs.getString("ID_PROC"));
		Dados.setTemaCodigo( rs.getString("TEMA_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_TEMA, PROC_NUMERO FROM PROJUDI.VIEW_PROC_TEMA WHERE PROC_NUMERO LIKE ?";
		stSql+= " ORDER BY PROC_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				ProcessoTemaDt obTemp = new ProcessoTemaDt();
				obTemp.setId(rs1.getString("ID_PROC_TEMA"));
				obTemp.setProcNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PROC_TEMA WHERE PROC_NUMERO LIKE ?";
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


		stSql= "SELECT ID_PROC_TEMA as id, PROC_NUMERO as descricao1 FROM PROJUDI.VIEW_PROC_TEMA WHERE PROC_NUMERO LIKE ?";
		stSql+= " ORDER BY PROC_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PROC_TEMA WHERE PROC_NUMERO LIKE ?";
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
