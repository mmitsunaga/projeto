package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PendenciaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7186987541009433621L;

	//---------------------------------------------------------
	public PendenciaTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PendenciaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PEND_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPendenciaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PEND_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPendenciaTipo());  

			stVirgula=",";
		}
		if ((dados.getPendenciaTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PEND_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPendenciaTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_ArquivoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ArquivoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PEND_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PendenciaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPendenciaTipoalterar()");

		stSql= "UPDATE PROJUDI.PEND_TIPO SET  ";
		stSql+= "PEND_TIPO = ?";		 ps.adicionarString(dados.getPendenciaTipo());  

		stSql+= ",PEND_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getPendenciaTipoCodigo());  

		stSql+= ",ID_ARQ_TIPO = ?";		 ps.adicionarLong(dados.getId_ArquivoTipo());  

		stSql += " WHERE ID_PEND_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.PEND_TIPO";
		stSql += " WHERE ID_PEND_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PendenciaTipoDt consultarId(String id_pendenciatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_PendenciaTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_TIPO WHERE ID_PEND_TIPO = ?";		ps.adicionarLong(id_pendenciatipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PendenciaTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_PEND_TIPO"));
		Dados.setPendenciaTipo(rs.getString("PEND_TIPO"));
		Dados.setPendenciaTipoCodigo( rs.getString("PEND_TIPO_CODIGO"));
		Dados.setId_ArquivoTipo( rs.getString("ID_ARQ_TIPO"));
		Dados.setArquivoTipo( rs.getString("ARQ_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPendenciaTipo()");

		stSql= "SELECT ID_PEND_TIPO, PEND_TIPO FROM PROJUDI.VIEW_PEND_TIPO WHERE PEND_TIPO LIKE ?";
		stSql+= " ORDER BY PEND_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPendenciaTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PendenciaTipoDt obTemp = new PendenciaTipoDt();
				obTemp.setId(rs1.getString("ID_PEND_TIPO"));
				obTemp.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PEND_TIPO WHERE PEND_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PendenciaTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
