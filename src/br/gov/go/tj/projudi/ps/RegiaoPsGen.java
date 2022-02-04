package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RegiaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7303158888690618223L;

	//---------------------------------------------------------
	public RegiaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(RegiaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRegiaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.Regiao ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getRegiao().length()>0)) {
			 stSqlCampos+=   stVirgula + "REGIAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRegiao());  

			stVirgula=",";
		}
		if ((dados.getRegiaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "REGIAO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getRegiaoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_REGIAO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(RegiaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psRegiaoalterar()");

		stSql= "UPDATE PROJUDI.Regiao SET  ";
		stSql+= "REGIAO = ?";		 ps.adicionarString(dados.getRegiao());  

		stSql+= ",REGIAO_CODIGO = ?";		 ps.adicionarLong(dados.getRegiaoCodigo());  

		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  

		stSql += " WHERE ID_REGIAO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRegiaoexcluir()");

		stSql= "DELETE FROM PROJUDI.Regiao";
		stSql += " WHERE ID_REGIAO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public RegiaoDt consultarId(String id_regiao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RegiaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Regiao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_REGIAO WHERE ID_REGIAO = ?";		ps.adicionarLong(id_regiao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Regiao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RegiaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( RegiaoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_REGIAO"));
		Dados.setRegiao(rs.getString("REGIAO"));
		Dados.setRegiaoCodigo( rs.getString("REGIAO_CODIGO"));
		Dados.setId_Comarca( rs.getString("ID_COMARCA"));
		Dados.setComarca( rs.getString("COMARCA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setComarcaCodigo( rs.getString("COMARCA_CODIGO"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoRegiao()");

		stSql= "SELECT ID_REGIAO, REGIAO FROM PROJUDI.VIEW_REGIAO WHERE REGIAO LIKE ?";
		stSql+= " ORDER BY REGIAO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoRegiao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				RegiaoDt obTemp = new RegiaoDt();
				obTemp.setId(rs1.getString("ID_REGIAO"));
				obTemp.setRegiao(rs1.getString("REGIAO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_REGIAO WHERE REGIAO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..RegiaoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
