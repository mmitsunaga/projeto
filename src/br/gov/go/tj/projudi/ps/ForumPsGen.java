package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ForumPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7775803667076236213L;

	//---------------------------------------------------------
	public ForumPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ForumDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psForuminserir()");
		stSqlCampos= "INSERT INTO PROJUDI.FORUM ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getForumCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "FORUM_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getForumCodigo());  

			stVirgula=",";
		}
		if ((dados.getForum().length()>0)) {
			 stSqlCampos+=   stVirgula + "FORUM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getForum());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_Endereco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Endereco());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_FORUM",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ForumDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psForumalterar()");

		stSql= "UPDATE PROJUDI.FORUM SET  ";
		stSql+= "FORUM_CODIGO = ?";		 ps.adicionarLong(dados.getForumCodigo());  

		stSql+= ",FORUM = ?";		 ps.adicionarString(dados.getForum());  

		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  
		
		stSql+= ",ID_ENDERECO = ?";		 ps.adicionarLong(dados.getId_Endereco());

		stSql += " WHERE ID_FORUM  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psForumexcluir()");

		stSql= "DELETE FROM PROJUDI.FORUM";
		stSql += " WHERE ID_FORUM = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ForumDt consultarId(String id_forum )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ForumDt Dados=null;
		////System.out.println("....ps-ConsultaId_Forum)");

		stSql= "SELECT * FROM PROJUDI.VIEW_FORUM WHERE ID_FORUM = ?";		ps.adicionarLong(id_forum); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Forum  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ForumDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ForumDt Dados, ResultSetTJGO rs )  throws Exception {
		
			Dados.setId(rs.getString("ID_FORUM"));
			Dados.setForum(rs.getString("FORUM"));
			Dados.setForumCodigo( rs.getString("FORUM_CODIGO"));
			Dados.setId_Comarca( rs.getString("ID_COMARCA"));
			Dados.setId_Endereco( rs.getString("ID_ENDERECO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			Dados.setComarca( rs.getString("COMARCA"));			
			Dados.setComarcaCodigo( rs.getString("COMARCA_CODIGO"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoForum()");

		stSql= "SELECT ID_FORUM, FORUM FROM PROJUDI.VIEW_FORUM WHERE FORUM LIKE ?";
		stSql+= " ORDER BY FORUM ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoForum  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ForumDt obTemp = new ForumDt();
				obTemp.setId(rs1.getString("ID_FORUM"));
				obTemp.setForum(rs1.getString("FORUM"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_FORUM WHERE FORUM LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ForumPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
