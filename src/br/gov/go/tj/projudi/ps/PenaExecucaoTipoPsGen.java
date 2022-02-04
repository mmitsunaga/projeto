package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PenaExecucaoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 9205289991522633250L;

	//---------------------------------------------------------
	public PenaExecucaoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PenaExecucaoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPenaExecucaoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PENA_EXE_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPenaExecucaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PENA_EXE_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPenaExecucaoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PENA_EXE_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PenaExecucaoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPenaExecucaoTipoalterar()");

		stSql= "UPDATE PROJUDI.PENA_EXE_TIPO SET  ";
		stSql+= "PENA_EXE_TIPO = ?";		 ps.adicionarString(dados.getPenaExecucaoTipo());  

		stSql += " WHERE ID_PENA_EXE_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPenaExecucaoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.PENA_EXE_TIPO";
		stSql += " WHERE ID_PENA_EXE_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PenaExecucaoTipoDt consultarId(String id_penaexecucaotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PenaExecucaoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_PenaExecucaoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO WHERE ID_PENA_EXE_TIPO = ?";		ps.adicionarLong(id_penaexecucaotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PenaExecucaoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PenaExecucaoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PenaExecucaoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PENA_EXE_TIPO"));
		Dados.setPenaExecucaoTipo(rs.getString("PENA_EXE_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPenaExecucaoTipo()");

		stSql= "SELECT ID_PENA_EXE_TIPO, PENA_EXE_TIPO FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO WHERE PENA_EXE_TIPO LIKE ?";
		stSql+= " ORDER BY PENA_EXE_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPenaExecucaoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PenaExecucaoTipoDt obTemp = new PenaExecucaoTipoDt();
				obTemp.setId(rs1.getString("ID_PENA_EXE_TIPO"));
				obTemp.setPenaExecucaoTipo(rs1.getString("PENA_EXE_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO WHERE PENA_EXE_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PenaExecucaoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PENA_EXE_TIPO as id, PENA_EXE_TIPO as descricao1 FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO WHERE PENA_EXE_TIPO LIKE ?";
		stSql+= " ORDER BY PENA_EXE_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO WHERE PENA_EXE_TIPO LIKE ?";
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
