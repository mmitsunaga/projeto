package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1177867597033483996L;

	//---------------------------------------------------------
	public ServentiaTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ServentiaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.SERV_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentiaTipo());  

			stVirgula=",";
		}
		if ((dados.getServentiaTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getExterna().length()>0)) {
			 stSqlCampos+=   stVirgula + "EXTERNA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getExterna());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_SERV_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psServentiaTipoalterar()");

		stSql= "UPDATE PROJUDI.SERV_TIPO SET  ";
		stSql+= "SERV_TIPO = ?";		 ps.adicionarString(dados.getServentiaTipo());  

		stSql+= ",SERV_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getServentiaTipoCodigo());  

		stSql+= ",EXTERNA = ?";		 ps.adicionarBoolean(dados.getExterna());  

		stSql += " WHERE ID_SERV_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.SERV_TIPO";
		stSql += " WHERE ID_SERV_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaTipoDt consultarId(String id_serventiatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ServentiaTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_TIPO WHERE ID_SERV_TIPO = ?";		ps.adicionarLong(id_serventiatipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ServentiaTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo(rs.getString("SERV_TIPO"));
		Dados.setServentiaTipoCodigo( rs.getString("SERV_TIPO_CODIGO"));
		Dados.setExterna( Funcoes.FormatarLogico(rs.getString("EXTERNA")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServentiaTipo()");

		stSql= "SELECT ID_SERV_TIPO, SERV_TIPO FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
		stSql+= " ORDER BY SERV_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoServentiaTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaTipoDt obTemp = new ServentiaTipoDt();
				obTemp.setId(rs1.getString("ID_SERV_TIPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ServentiaTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
