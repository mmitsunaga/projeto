package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ComarcaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3174613077764803654L;

	//---------------------------------------------------------
	public ComarcaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ComarcaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psComarcainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.COMARCA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getComarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getComarca());  

			stVirgula=",";
		}
		if ((dados.getComarcaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "COMARCA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getComarcaCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_COMARCA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ComarcaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psComarcaalterar()");

		stSql= "UPDATE PROJUDI.COMARCA SET  ";
		stSql+= "COMARCA = ?";		 ps.adicionarString(dados.getComarca());  

		stSql+= ",COMARCA_CODIGO = ?";		 ps.adicionarLong(dados.getComarcaCodigo());  

		stSql += " WHERE ID_COMARCA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psComarcaexcluir()");

		stSql= "DELETE FROM PROJUDI.COMARCA";
		stSql += " WHERE ID_COMARCA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ComarcaDt consultarId(String id_comarca )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ComarcaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Comarca)");

		stSql= "SELECT * FROM PROJUDI.VIEW_COMARCA WHERE ID_COMARCA = ?";		ps.adicionarLong(id_comarca); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Comarca  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ComarcaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ComarcaDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_COMARCA"));
		Dados.setComarca(rs.getString("COMARCA"));
		Dados.setComarcaCodigo( rs.getString("COMARCA_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoComarca()");

		stSql= "SELECT ID_COMARCA, COMARCA FROM PROJUDI.VIEW_COMARCA WHERE COMARCA LIKE ?";
		stSql+= " ORDER BY COMARCA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoComarca  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ComarcaDt obTemp = new ComarcaDt();
				obTemp.setId(rs1.getString("ID_COMARCA"));
				obTemp.setComarca(rs1.getString("COMARCA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_COMARCA WHERE COMARCA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ComarcaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
