package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PermissaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -415836309729873769L;

	//---------------------------------------------------------
	public PermissaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PermissaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPermissaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PERM ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPermissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PERM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPermissao());  

			stVirgula=",";
		}
		if ((dados.getPermissaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PERM_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPermissaoCodigo());  

			stVirgula=",";
		}
		if ((dados.getEMenu().length()>0)) {
			 stSqlCampos+=   stVirgula + "E_MENU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getEMenu());  

			stVirgula=",";
		}
		if ((dados.getLink().length()>0)) {
			 stSqlCampos+=   stVirgula + "LINK " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLink());  

			stVirgula=",";
		}
		if ((dados.getIrPara().length()>0)) {
			 stSqlCampos+=   stVirgula + "IR_PARA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getIrPara());  

			stVirgula=",";
		}
		if ((dados.getTitulo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TITULO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTitulo());  

			stVirgula=",";
		}
		if ((dados.getId_PermissaoPai().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PERM_PAI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PermissaoPai());  

			stVirgula=",";
		}
		if ((dados.getId_PermissaoEspecial().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PERM_ESPECIAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PermissaoEspecial());  

			stVirgula=",";
		}
		if ((dados.getOrdenacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ORDENACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getOrdenacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PERM",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PermissaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPermissaoalterar()");

		stSql= "UPDATE PROJUDI.PERM SET  ";
		stSql+= "PERM = ?";		 ps.adicionarString(dados.getPermissao());  

		stSql+= ",PERM_CODIGO = ?";		 ps.adicionarLong(dados.getPermissaoCodigo());  

		stSql+= ",E_MENU = ?";		 ps.adicionarBoolean(dados.getEMenu());  

		stSql+= ",LINK = ?";		 ps.adicionarString(dados.getLink());  

		stSql+= ",IR_PARA = ?";		 ps.adicionarString(dados.getIrPara());  

		stSql+= ",TITULO = ?";		 ps.adicionarString(dados.getTitulo());  

		stSql+= ",ID_PERM_PAI = ?";		 ps.adicionarLong(dados.getId_PermissaoPai());  

		stSql+= ",ID_PERM_ESPECIAL = ?";		 ps.adicionarLong(dados.getId_PermissaoEspecial());  

		stSql+= ",ORDENACAO = ?";		 ps.adicionarLong(dados.getOrdenacao());  

		stSql += " WHERE ID_PERM  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPermissaoexcluir()");

		stSql= "DELETE FROM PROJUDI.PERM";
		stSql += " WHERE ID_PERM = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PermissaoDt consultarId(String id_permissao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PermissaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Permissao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PERM WHERE ID_PERM = ?";		ps.adicionarLong(id_permissao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Permissao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PermissaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PermissaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PERM"));
		Dados.setPermissao(rs.getString("PERM"));
		Dados.setPermissaoCodigo( rs.getString("PERM_CODIGO"));
		Dados.setEMenu( Funcoes.FormatarLogico(rs.getString("E_MENU")));
		Dados.setLink( rs.getString("LINK"));
		Dados.setIrPara( rs.getString("IR_PARA"));
		Dados.setTitulo( rs.getString("TITULO"));
		Dados.setId_PermissaoPai( rs.getString("ID_PERM_PAI"));
		Dados.setPermissaoPai( rs.getString("PERM_PAI"));
		Dados.setId_PermissaoEspecial( rs.getString("ID_PERM_ESPECIAL"));
		Dados.setPermissaoEspecial( rs.getString("PERM_ESPECIAL"));
		Dados.setOrdenacao( rs.getString("ORDENACAO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setPermissaoCodigoPai( rs.getString("PERM_CODIGO_PAI"));
		Dados.setPermissaoEspecialCodigo( rs.getString("PERM_ESPECIAL_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPermissao()");

		stSql= "SELECT ID_PERM, PERM FROM PROJUDI.VIEW_PERM WHERE PERM LIKE ?";
		stSql+= " ORDER BY PERM ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPermissao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PermissaoDt obTemp = new PermissaoDt();
				obTemp.setId(rs1.getString("ID_PERM"));
				obTemp.setPermissao(rs1.getString("PERM"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PERM WHERE PERM LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PermissaoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
