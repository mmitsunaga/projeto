package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioArquivoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class UsuarioArquivoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 4838420828034382134L;

	//---------------------------------------------------------
	public UsuarioArquivoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(UsuarioArquivoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioArquivoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.USU_ARQ ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Usuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Usuario());  

			stVirgula=",";
		}
		if ((dados.getId_Arquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Arquivo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_USU_ARQ",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(UsuarioArquivoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psUsuarioArquivoalterar()");

		stSql= "UPDATE PROJUDI.USU_ARQ SET  ";
		stSql+= "ID_USU = ?";		 ps.adicionarLong(dados.getId_Usuario());  

		stSql+= ",ID_ARQ = ?";		 ps.adicionarLong(dados.getId_Arquivo());  

		stSql += " WHERE ID_USU_ARQ  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioArquivoexcluir()");

		stSql= "DELETE FROM PROJUDI.USU_ARQ";
		stSql += " WHERE ID_USU_ARQ = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public UsuarioArquivoDt consultarId(String id_usuarioarquivo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioArquivoDt Dados=null;
		////System.out.println("....ps-ConsultaId_UsuarioArquivo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_USU_ARQ WHERE ID_USU_ARQ = ?";		ps.adicionarLong(id_usuarioarquivo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_UsuarioArquivo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioArquivoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( UsuarioArquivoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_USU_ARQ"));
		Dados.setUsuario(rs.getString("USU"));
		Dados.setId_Usuario( rs.getString("ID_USU"));
		Dados.setId_Arquivo( rs.getString("ID_ARQ"));
		Dados.setNomeArquivo( rs.getString("NOME_ARQ"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoUsuarioArquivo()");

		stSql= "SELECT ID_USU_ARQ, USU FROM PROJUDI.VIEW_USU_ARQ WHERE USU LIKE ?";
		stSql+= " ORDER BY USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			
			while (rs1.next()) {
				UsuarioArquivoDt obTemp = new UsuarioArquivoDt();
				obTemp.setId(rs1.getString("ID_USU_ARQ"));
				obTemp.setUsuario(rs1.getString("USU"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_USU_ARQ WHERE USU LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
