package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProfissaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8459351741676546776L;

	//---------------------------------------------------------
	public ProfissaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProfissaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProfissaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROFISSAO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProfissaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROFISSAO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProfissaoCodigo());  

			stVirgula=",";
		}
		if ((dados.getProfissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROFISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProfissao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROFISSAO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProfissaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProfissaoalterar()");

		stSql= "UPDATE PROJUDI.PROFISSAO SET  ";
		stSql+= "PROFISSAO_CODIGO = ?";		 ps.adicionarLong(dados.getProfissaoCodigo());  

		stSql+= ",PROFISSAO = ?";		 ps.adicionarString(dados.getProfissao());  

		stSql += " WHERE ID_PROFISSAO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProfissaoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROFISSAO";
		stSql += " WHERE ID_PROFISSAO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProfissaoDt consultarId(String id_profissao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProfissaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Profissao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROFISSAO WHERE ID_PROFISSAO = ?";		ps.adicionarLong(id_profissao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Profissao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProfissaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProfissaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROFISSAO"));
		Dados.setProfissao(rs.getString("PROFISSAO"));
		Dados.setProfissaoCodigo( rs.getString("PROFISSAO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProfissao()");

		stSql= "SELECT ID_PROFISSAO, PROFISSAO FROM PROJUDI.VIEW_PROFISSAO WHERE PROFISSAO LIKE ?";
		stSql+= " ORDER BY PROFISSAO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProfissao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProfissaoDt obTemp = new ProfissaoDt();
				obTemp.setId(rs1.getString("ID_PROFISSAO"));
				obTemp.setProfissao(rs1.getString("PROFISSAO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROFISSAO WHERE PROFISSAO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProfissaoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_PROFISSAO AS ID, PROFISSAO AS DESCRICAO1 FROM PROJUDI.VIEW_PROFISSAO WHERE PROFISSAO LIKE ?";
		stSql+= " ORDER BY PROFISSAO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROFISSAO WHERE PROFISSAO LIKE ?";
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
