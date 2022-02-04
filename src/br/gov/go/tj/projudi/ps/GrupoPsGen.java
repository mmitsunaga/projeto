package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GrupoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8393070997697612567L;

	//---------------------------------------------------------
	public GrupoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GrupoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.GRUPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getGrupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGrupo());  

			stVirgula=",";
		}
		if ((dados.getGrupoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GRUPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGrupoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_GrupoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GRUPO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GrupoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GRUPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GrupoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGrupoalterar()");

		stSql= "UPDATE PROJUDI.GRUPO SET  ";
		stSql+= "GRUPO = ?";		 ps.adicionarString(dados.getGrupo());  

		stSql+= ",GRUPO_CODIGO = ?";		 ps.adicionarLong(dados.getGrupoCodigo());  

		stSql+= ",ID_SERV_TIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaTipo());  

		stSql+= ",ID_GRUPO_TIPO = ?";		 ps.adicionarLong(dados.getId_GrupoTipo());  

		stSql += " WHERE ID_GRUPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoexcluir()");

		stSql= "DELETE FROM PROJUDI.GRUPO";
		stSql += " WHERE ID_GRUPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GrupoDt consultarId(String id_grupo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GrupoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Grupo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_GRUPO WHERE ID_GRUPO = ?";		ps.adicionarLong(id_grupo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Grupo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GrupoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GrupoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GRUPO"));
		Dados.setGrupo(rs.getString("GRUPO"));
		Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));
		Dados.setId_ServentiaTipo( rs.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo( rs.getString("SERV_TIPO"));
		Dados.setId_GrupoTipo( rs.getString("ID_GRUPO_TIPO"));
		Dados.setGrupoTipo( rs.getString("GRUPO_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setServentiaTipoCodigo( rs.getString("SERV_TIPO_CODIGO"));
		Dados.setGrupoTipoCodigo( rs.getString("GRUPO_TIPO_CODIGO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGrupo()");

		stSql= "SELECT ID_GRUPO, GRUPO FROM PROJUDI.VIEW_GRUPO WHERE GRUPO LIKE ?";
		stSql+= " ORDER BY GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGrupo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GrupoDt obTemp = new GrupoDt();
				obTemp.setId(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_GRUPO WHERE GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GrupoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
