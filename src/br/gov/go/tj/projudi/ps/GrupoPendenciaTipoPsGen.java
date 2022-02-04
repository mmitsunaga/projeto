package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoPendenciaTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GrupoPendenciaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 5008596735915552964L;

	//---------------------------------------------------------
	public GrupoPendenciaTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GrupoPendenciaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoPendenciaTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.GRUPO_PEND_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Grupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Grupo());  

			stVirgula=",";
		}
		if ((dados.getId_PendenciaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PendenciaTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GRUPO_PEND_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GrupoPendenciaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGrupoPendenciaTipoalterar()");

		stSql= "UPDATE PROJUDI.GRUPO_PEND_TIPO SET  ";
		stSql+= "ID_GRUPO = ?";		 ps.adicionarLong(dados.getId_Grupo());  

		stSql+= ",ID_PEND_TIPO = ?";		 ps.adicionarLong(dados.getId_PendenciaTipo());  

		stSql += " WHERE ID_GRUPO_PEND_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoPendenciaTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.GRUPO_PEND_TIPO";
		stSql += " WHERE ID_GRUPO_PEND_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GrupoPendenciaTipoDt consultarId(String id_grupopendenciatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GrupoPendenciaTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_GrupoPendenciaTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_GRUPO_PEND_TIPO WHERE ID_GRUPO_PEND_TIPO = ?";		ps.adicionarLong(id_grupopendenciatipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GrupoPendenciaTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GrupoPendenciaTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GrupoPendenciaTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_GRUPO_PEND_TIPO"));
		Dados.setGrupo(rs.getString("GRUPO"));
		Dados.setId_Grupo( rs.getString("ID_GRUPO"));
		Dados.setId_PendenciaTipo( rs.getString("ID_PEND_TIPO"));
		Dados.setPendenciaTipo( rs.getString("PEND_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));
		Dados.setPendenciaTipoCodigo( rs.getString("PEND_TIPO_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGrupoPendenciaTipo()");

		stSql= "SELECT ID_GRUPO_PEND_TIPO, GRUPO FROM PROJUDI.VIEW_GRUPO_PEND_TIPO WHERE GRUPO LIKE ?";
		stSql+= " ORDER BY GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGrupoPendenciaTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GrupoPendenciaTipoDt obTemp = new GrupoPendenciaTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_PEND_TIPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_GRUPO_PEND_TIPO WHERE GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GrupoPendenciaTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
} 
