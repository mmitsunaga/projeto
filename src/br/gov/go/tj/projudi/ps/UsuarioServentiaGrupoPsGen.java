package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class UsuarioServentiaGrupoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 5477934139302476641L;

	//---------------------------------------------------------
	public UsuarioServentiaGrupoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(UsuarioServentiaGrupoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioServentiaGrupoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.USU_SERV_GRUPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getUsuarioServentiaGrupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "USU_SERV_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getUsuarioServentiaGrupo());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getId_Grupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Grupo());  

			stVirgula=",";
		}
		if ((dados.getAtivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAtivo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_USU_SERV_GRUPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(UsuarioServentiaGrupoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psUsuarioServentiaGrupoalterar()");

		stSql= "UPDATE PROJUDI.USU_SERV_GRUPO SET  ";

		stSql+= "ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql+= ",ID_GRUPO = ?";		 ps.adicionarLong(dados.getId_Grupo());  

		stSql+= ",ATIVO = ?";		 ps.adicionarBoolean(dados.getAtivo());  

		stSql += " WHERE ID_USU_SERV_GRUPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioServentiaGrupoexcluir()");

		stSql= "DELETE FROM PROJUDI.USU_SERV_GRUPO";
		stSql += " WHERE ID_USU_SERV_GRUPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public UsuarioServentiaGrupoDt consultarId(String id_usuarioserventiagrupo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioServentiaGrupoDt Dados=null;
		////System.out.println("....ps-ConsultaId_UsuarioServentiaGrupo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_USU_SERV_GRUPO WHERE ID_USU_SERV_GRUPO = ?";		ps.adicionarLong(id_usuarioserventiagrupo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_UsuarioServentiaGrupo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioServentiaGrupoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( UsuarioServentiaGrupoDt Dados, ResultSetTJGO rs )  throws Exception {
		
			Dados.setId(rs.getString("ID_USU_SERV_GRUPO"));
			Dados.setUsuarioServentiaGrupo(rs.getString("USU_SERV_GRUPO"));
			Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
			Dados.setUsuarioServentia( rs.getString("USU_SERV"));
			Dados.setId_Grupo( rs.getString("ID_GRUPO"));
			Dados.setGrupo( rs.getString("GRUPO"));
			Dados.setAtivo( Funcoes.FormatarLogico(rs.getString("ATIVO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));
			Dados.setNome( rs.getString("NOME"));
			Dados.setId_Serventia( rs.getString("ID_SERV"));
			Dados.setServentia( rs.getString("SERV"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoUsuarioServentiaGrupo()");

		stSql= "SELECT ID_USU_SERV_GRUPO, USU_SERV_GRUPO FROM PROJUDI.VIEW_USU_SERV_GRUPO WHERE USU_SERV_GRUPO LIKE ?";
		stSql+= " ORDER BY USU_SERV_GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoUsuarioServentiaGrupo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				UsuarioServentiaGrupoDt obTemp = new UsuarioServentiaGrupoDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setUsuarioServentiaGrupo(rs1.getString("USU_SERV_GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_USU_SERV_GRUPO WHERE USU_SERV_GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..UsuarioServentiaGrupoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
