package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AreaDistribuicaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 2982325027791914872L;

	//---------------------------------------------------------
	public AreaDistribuicaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AreaDistribuicaoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAreaDistribuicaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AREA_DIST ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getAreaDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "AREA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAreaDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getAreaDistribuicaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "AREA_DIST_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getAreaDistribuicaoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_Forum().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_FORUM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Forum());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

			stVirgula=",";
		}
		if ((dados.getId_AreaDistribuicaoRelacionada().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST_RELACIONADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicaoRelacionada());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		dados.setId(executarInsert(stSql,"ID_AREA_DIST",ps));
	} 

//---------------------------------------------------------
	public void alterar(AreaDistribuicaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAreaDistribuicaoalterar()");

		stSql= "UPDATE PROJUDI.AREA_DIST SET  ";
		stSql+= "AREA_DIST = ?";		 ps.adicionarString(dados.getAreaDistribuicao());  

		stSql+= ",AREA_DIST_CODIGO = ?";		 ps.adicionarLong(dados.getAreaDistribuicaoCodigo());  

		stSql+= ",ID_FORUM = ?";		 ps.adicionarLong(dados.getId_Forum());  

		stSql+= ",ID_SERV_SUBTIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

		stSql+= ",ID_AREA_DIST_RELACIONADA = ?";		 ps.adicionarLong(dados.getId_AreaDistribuicaoRelacionada());  

		stSql += " WHERE ID_AREA_DIST  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAreaDistribuicaoexcluir()");

		stSql= "DELETE FROM PROJUDI.AREA_DIST";
		stSql += " WHERE ID_AREA_DIST = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AreaDistribuicaoDt consultarId(String ID_AREA_DIST )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AreaDistribuicaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_AreaDistribuicao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AREA_DIST WHERE ID_AREA_DIST = ?";		ps.adicionarLong(ID_AREA_DIST); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_AreaDistribuicao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AreaDistribuicaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AreaDistribuicaoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_AREA_DIST"));
		Dados.setAreaDistribuicao(rs1.getString("AREA_DIST"));
		Dados.setAreaDistribuicaoCodigo( rs1.getString("AREA_DIST_CODIGO"));
		Dados.setId_Forum( rs1.getString("ID_FORUM"));
		Dados.setForum( rs1.getString("FORUM"));
		Dados.setId_ServentiaSubtipo( rs1.getString("ID_SERV_SUBTIPO"));
		Dados.setServentiaSubtipo( rs1.getString("SERV_SUBTIPO"));
		//Dados.setId_AreaDistribuicaoRelacionada( rs1.getString("ID_AREA_DIST_RELACIONADA"));
		//Dados.setAreaDistribuicaoRelacionada( rs1.getString("AREA_DIST_RELACIONADA"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setComarcaCodigo( rs1.getString("COMARCA_CODIGO"));
		Dados.setServentiaSubtipoCodigo( rs1.getString("SERV_SUBTIPO_CODIGO"));
		Dados.setId_Comarca( rs1.getString("ID_COMARCA"));
		Dados.setComarca( rs1.getString("COMARCA"));
		Dados.setForumCodigo( rs1.getString("FORUM_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAreaDistribuicao()");

		stSql= "SELECT ID_AREA_DIST, AREA_DIST FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
		stSql+= " ORDER BY AREA_DIST ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAreaDistribuicao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta QUANTIDADE OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AreaDistribuicaoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
