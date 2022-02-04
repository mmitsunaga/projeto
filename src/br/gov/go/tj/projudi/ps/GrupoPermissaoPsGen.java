package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoPermissaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GrupoPermissaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1385354146073232411L;

	//---------------------------------------------------------
	public GrupoPermissaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GrupoPermissaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoPermissaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.GRUPO_PERM ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Grupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Grupo());  

			stVirgula=",";
		}
		if ((dados.getGrupoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GRUPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGrupoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_Permissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PERM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Permissao());  

			stVirgula=",";
		}
		if ((dados.getPermissaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PERM_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPermissaoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GRUPO_PERM",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GrupoPermissaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGrupoPermissaoalterar()");

		stSql= "UPDATE PROJUDI.GRUPO_PERM SET  ";
		stSql+= "ID_GRUPO = ?";		 ps.adicionarLong(dados.getId_Grupo());  

		stSql+= ",GRUPO_CODIGO = ?";		 ps.adicionarLong(dados.getGrupoCodigo());  

		stSql+= ",ID_PERM = ?";		 ps.adicionarLong(dados.getId_Permissao());  

		stSql+= ",PERM_CODIGO = ?";		 ps.adicionarLong(dados.getPermissaoCodigo());  

		stSql += " WHERE ID_GRUPO_PERM  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoPermissaoexcluir()");

		stSql= "DELETE FROM PROJUDI.GRUPO_PERM";
		stSql += " WHERE ID_GRUPO_PERM = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public GrupoPermissaoDt consultarId(String id_grupopermissao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GrupoPermissaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_GrupoPermissao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_GRUPO_PERM WHERE ID_GRUPO_PERM = ?";		ps.adicionarLong(id_grupopermissao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GrupoPermissao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GrupoPermissaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GrupoPermissaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_GRUPO_PERM"));
		Dados.setGrupo(rs.getString("GRUPO"));
		Dados.setId_Grupo( rs.getString("ID_GRUPO"));
		Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));
		Dados.setId_Permissao( rs.getString("ID_PERM"));
		Dados.setPermissao( rs.getString("PERM"));
		Dados.setPermissaoCodigo( rs.getString("PERM_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGrupoPermissao()");

		stSql= "SELECT ID_GRUPO_PERM, GRUPO FROM PROJUDI.VIEW_GRUPO_PERM WHERE GRUPO LIKE ?";
		stSql+= " ORDER BY GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGrupoPermissao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GrupoPermissaoDt obTemp = new GrupoPermissaoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_PERM"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_GRUPO_PERM WHERE GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GrupoPermissaoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
