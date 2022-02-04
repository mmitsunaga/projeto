package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PermissaoEspecialPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 5330546190160812459L;

	//---------------------------------------------------------
	public PermissaoEspecialPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PermissaoEspecialDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPermissaoEspecialinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PERM_ESPECIAL ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPermissaoEspecial().length()>0)) {
			 stSqlCampos+=   stVirgula + "PERM_ESPECIAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPermissaoEspecial());  

			stVirgula=",";
		}
		if ((dados.getPermissaoEspecialCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PERM_ESPECIAL_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPermissaoEspecialCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PERM_ESPECIAL",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PermissaoEspecialDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPermissaoEspecialalterar()");

		stSql= "UPDATE PROJUDI.PERM_ESPECIAL SET  ";
		stSql+= "PERM_ESPECIAL = ?";		 ps.adicionarString(dados.getPermissaoEspecial());  

		stSql+= ",PERM_ESPECIAL_CODIGO = ?";		 ps.adicionarLong(dados.getPermissaoEspecialCodigo());  

		stSql += " WHERE ID_PERM_ESPECIAL  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPermissaoEspecialexcluir()");

		stSql= "DELETE FROM PROJUDI.PERM_ESPECIAL";
		stSql += " WHERE ID_PERM_ESPECIAL = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PermissaoEspecialDt consultarId(String id_permissaoespecial )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PermissaoEspecialDt Dados=null;
		////System.out.println("....ps-ConsultaId_PermissaoEspecial)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PERM_ESPECIAL WHERE ID_PERM_ESPECIAL = ?";		ps.adicionarLong(id_permissaoespecial); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PermissaoEspecial  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PermissaoEspecialDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PermissaoEspecialDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PERM_ESPECIAL"));
		Dados.setPermissaoEspecial(rs.getString("PERM_ESPECIAL"));
		Dados.setPermissaoEspecialCodigo( rs.getString("PERM_ESPECIAL_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPermissaoEspecial()");

		stSql= "SELECT ID_PERM_ESPECIAL, PERM_ESPECIAL FROM PROJUDI.VIEW_PERM_ESPECIAL WHERE PERM_ESPECIAL LIKE ?";
		stSql+= " ORDER BY PERM_ESPECIAL ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPermissaoEspecial  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PermissaoEspecialDt obTemp = new PermissaoEspecialDt();
				obTemp.setId(rs1.getString("ID_PERM_ESPECIAL"));
				obTemp.setPermissaoEspecial(rs1.getString("PERM_ESPECIAL"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PERM_ESPECIAL WHERE PERM_ESPECIAL LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PermissaoEspecialPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
