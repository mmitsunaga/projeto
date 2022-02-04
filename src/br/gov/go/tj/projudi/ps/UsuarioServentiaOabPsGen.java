package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class UsuarioServentiaOabPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6716270797449674034L;

	//---------------------------------------------------------
	public UsuarioServentiaOabPsGen() {

	}



//---------------------------------------------------------
	public void inserir(UsuarioServentiaOabDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioServentiaOabinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.USU_SERV_OAB ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getOabNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "OAB_NUMERO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getOabNumero());  

			stVirgula=",";
		}
		if ((dados.getOabComplemento().length()>0)) {
			 stSqlCampos+=   stVirgula + "OAB_COMPLEMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getOabComplemento());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_USU_SERV_OAB",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(UsuarioServentiaOabDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psUsuarioServentiaOabalterar()");

		stSql= "UPDATE PROJUDI.USU_SERV_OAB SET  ";
		stSql+= "OAB_NUMERO = ? ";		 ps.adicionarLong(dados.getOabNumero()); 
	
		stSql+= ",OAB_COMPLEMENTO = ? ";		 ps.adicionarString(dados.getOabComplemento()); 

		stSql+= ",ID_USU_SERV = ? ";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql += " WHERE ID_USU_SERV_OAB  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioServentiaOabexcluir()");

		stSql= "DELETE FROM PROJUDI.USU_SERV_OAB";
		stSql += " WHERE ID_USU_SERV_OAB = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public UsuarioServentiaOabDt consultarId(String id_usuarioserventiaoab )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioServentiaOabDt Dados=null;
		////System.out.println("....ps-ConsultaId_UsuarioServentiaOab)");

		stSql= "SELECT * FROM PROJUDI.VIEW_USU_SERV_OAB WHERE ID_USU_SERV_OAB = ?";		ps.adicionarLong(id_usuarioserventiaoab); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_UsuarioServentiaOab  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioServentiaOabDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( UsuarioServentiaOabDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_USU_SERV_OAB"));
		Dados.setOabNumero(rs.getString("OAB_NUMERO"));
		Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
		Dados.setOabComplemento( rs.getString("OAB_COMPLEMENTO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoUsuarioServentiaOab()");

		stSql= "SELECT ID_USU_SERV_OAB, OAB_NUMERO FROM PROJUDI.VIEW_USU_SERV_OAB WHERE OAB_NUMERO LIKE ?";
		stSql+= " ORDER BY OAB_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoUsuarioServentiaOab  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				UsuarioServentiaOabDt obTemp = new UsuarioServentiaOabDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_OAB"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_USU_SERV_OAB WHERE OAB_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..UsuarioServentiaOabPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
