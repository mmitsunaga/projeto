package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaCidadeDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ComarcaCidadePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8066442521534571891L;

	//---------------------------------------------------------
	public ComarcaCidadePsGen() {

	}



//---------------------------------------------------------
	public void inserir(ComarcaCidadeDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psComarcaCidadeinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.COMARCA_CIDADE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_Cidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Cidade());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_COMARCA_CIDADE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ComarcaCidadeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psComarcaCidadealterar()");

		stSql= "UPDATE PROJUDI.COMARCA_CIDADE SET  ";
		stSql+= "ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  

		stSql+= ",ID_CIDADE = ?";		 ps.adicionarLong(dados.getId_Cidade());  

		stSql += " WHERE ID_COMARCA_CIDADE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psComarcaCidadeexcluir()");

		stSql= "DELETE FROM PROJUDI.COMARCA_CIDADE";
		stSql += " WHERE ID_COMARCA_CIDADE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ComarcaCidadeDt consultarId(String id_comarcacidade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ComarcaCidadeDt Dados=null;
		////System.out.println("....ps-ConsultaId_ComarcaCidade)");

		stSql= "SELECT * FROM PROJUDI.VIEW_COMARCA_CIDADE WHERE ID_COMARCA_CIDADE = ?";		ps.adicionarLong(id_comarcacidade); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ComarcaCidade  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ComarcaCidadeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ComarcaCidadeDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_COMARCA_CIDADE"));
		Dados.setComarca(rs.getString("COMARCA"));
		Dados.setId_Comarca( rs.getString("ID_COMARCA"));
		Dados.setId_Cidade( rs.getString("ID_CIDADE"));
		Dados.setCidade( rs.getString("CIDADE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoComarcaCidade()");

		stSql= "SELECT ID_COMARCA_CIDADE, COMARCA FROM PROJUDI.VIEW_COMARCA_CIDADE WHERE COMARCA LIKE ?";
		stSql+= " ORDER BY COMARCA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoComarcaCidade  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ComarcaCidadeDt obTemp = new ComarcaCidadeDt();
				obTemp.setId(rs1.getString("ID_COMARCA_CIDADE"));
				obTemp.setComarca(rs1.getString("COMARCA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_COMARCA_CIDADE WHERE COMARCA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ComarcaCidadePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
