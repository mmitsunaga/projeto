package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaAreaDistribuicaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3919272234666259254L;

	//---------------------------------------------------------
	public ServentiaAreaDistribuicaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ServentiaAreaDistribuicaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServAreaDistinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.SERV_AREA_DIST ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServAreaDist().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_AREA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServAreaDist());  

			stVirgula=",";
		}
		if ((dados.getId_Serv().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serv());  

			stVirgula=",";
		}
		if ((dados.getId_AreaDist().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDist());  

			stVirgula=",";
		}
		
		if ((dados.getProbabilidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROBABILIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDecimal(dados.getProbabilidade());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_SERV_AREA_DIST",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaAreaDistribuicaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psServAreaDistalterar()");

		stSql= "UPDATE PROJUDI.SERV_AREA_DIST SET  ";
		stSql+= "SERV_AREA_DIST = ?";		 ps.adicionarString(dados.getServAreaDist());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serv());  

		stSql+= ",ID_AREA_DIST = ?";		 ps.adicionarLong(dados.getId_AreaDist());  
		
		stSql+= ",PROBABILIDADE = ?";		 ps.adicionarDecimal(dados.getProbabilidade());  

		stSql += " WHERE ID_SERV_AREA_DIST  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServAreaDistexcluir()");

		stSql= "DELETE FROM PROJUDI.SERV_AREA_DIST";
		stSql += " WHERE ID_SERV_AREA_DIST = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaAreaDistribuicaoDt consultarId(String id_servareadist )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaAreaDistribuicaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ServAreaDist)");

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_AREA_DIST WHERE ID_SERV_AREA_DIST = ?";		ps.adicionarLong(id_servareadist); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ServAreaDist  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaAreaDistribuicaoDt();
				associarDt(Dados, rs1);
				Dados.setProbabilidade(Funcoes.FormatarDecimal(rs1.getString("PROBABILIDADE")));
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaAreaDistribuicaoDt Dados, ResultSetTJGO rs )  throws Exception {
	
		Dados.setId(rs.getString("ID_SERV_AREA_DIST"));
		Dados.setServAreaDist(rs.getString("SERV_AREA_DIST"));
		Dados.setId_Serv( rs.getString("ID_SERV"));
		Dados.setServ( rs.getString("SERV"));
		Dados.setId_AreaDist( rs.getString("ID_AREA_DIST"));
		Dados.setAreaDist( rs.getString("AREA_DIST"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));	
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServAreaDist()");

		stSql= "SELECT ID_SERV_AREA_DIST, SERV_AREA_DIST FROM PROJUDI.VIEW_SERV_AREA_DIST WHERE SERV_AREA_DIST LIKE ?";
		stSql+= " ORDER BY SERV_AREA_DIST ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoServAreaDist  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaAreaDistribuicaoDt obTemp = new ServentiaAreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_SERV_AREA_DIST"));
				obTemp.setServAreaDist(rs1.getString("SERV_AREA_DIST"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_AREA_DIST WHERE SERV_AREA_DIST LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ServAreaDistPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
