package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class UsuarioServentiaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7454535801932063029L;

	//---------------------------------------------------------
	public UsuarioServentiaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(UsuarioServentiaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.USU_SERV ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getUsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getUsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getId_Usuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Usuario());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_EnderecoServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ENDERECO_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EnderecoServentia());  

			stVirgula=",";
		}
		if ((dados.getId_BairroServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_BAIRRO_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_BairroServentia());  

			stVirgula=",";
		}
		if ((dados.getId_CidadeServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CIDADE_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CidadeServentia());  

			stVirgula=",";
		}
		if ((dados.getAtivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAtivo());  

			stVirgula=",";
		}
		if ((dados.getServentiaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaCodigo());  

			stVirgula=",";
		}
		if ((dados.getServentiaCodigoExterno().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CODIGO_EXTERNO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaCodigoExterno());  

			stVirgula=",";
		}
		if ((dados.getComarcaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "COMARCA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getComarcaCodigo());  

			stVirgula=",";
		}
		if ((dados.getBairroCodigoServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "BAIRRO_CODIGO_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getBairroCodigoServentia());  

			stVirgula=",";
		}
		if ((dados.getCidadeCodigoServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "CIDADE_CODIGO_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCidadeCodigoServentia());  

			stVirgula=",";
		}
		if ((dados.getNome().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNome());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentiaChefe().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_CHEFE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentiaChefe());  

			stVirgula=",";
		}
		
		if ((dados.getPodeGuardarAssinarUsuarioServentiaChefe().length()>0)) {
			 stSqlCampos+=   stVirgula + "PODE_GUARDAR_ASSINAR_USUCHEFE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getPodeGuardarAssinarUsuarioServentiaChefe());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql,"ID_USU_SERV",ps));		 
	} 

//---------------------------------------------------------
	public void alterar(UsuarioServentiaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psUsuarioServentiaalterar()");

		stSql= "UPDATE PROJUDI.USU_SERV SET  ";
		stSql+= "USU_SERV = ?";		 ps.adicionarString(dados.getUsuarioServentia());  

		stSql+= ",ID_USU = ?";		 ps.adicionarLong(dados.getId_Usuario());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  

		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  

		stSql+= ",ENDERECO_SERV = ?";		 ps.adicionarLong(dados.getId_EnderecoServentia());  

		stSql+= ",ID_BAIRRO_SERV = ?";		 ps.adicionarLong(dados.getId_BairroServentia());  

		stSql+= ",ID_CIDADE_SERV = ?";		 ps.adicionarLong(dados.getId_CidadeServentia());  

		stSql+= ",ATIVO = ?";		 ps.adicionarBoolean(dados.getAtivo());  

		stSql+= ",SERV_CODIGO = ?";		 ps.adicionarLong(dados.getServentiaCodigo());  

		stSql+= ",SERV_CODIGO_EXTERNO = ?";		 ps.adicionarLong(dados.getServentiaCodigoExterno());  

		stSql+= ",COMARCA_CODIGO = ?";		 ps.adicionarLong(dados.getComarcaCodigo());  

		stSql+= ",BAIRRO_CODIGO_SERV = ?";		 ps.adicionarLong(dados.getBairroCodigoServentia());  

		stSql+= ",CIDADE_CODIGO_SERV = ?";		 ps.adicionarLong(dados.getCidadeCodigoServentia());  

		stSql+= ",NOME = ?";		 ps.adicionarString(dados.getNome());  

		stSql+= ",ID_USU_SERV_CHEFE = ?";		 ps.adicionarLong(dados.getId_UsuarioServentiaChefe());  
		
		stSql+= ",PODE_GUARDAR_ASSINAR_USUCHEFE = ?";		 ps.adicionarBoolean(dados.getAtivo());

		stSql += " WHERE ID_USU_SERV  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioServentiaexcluir()");

		stSql= "DELETE FROM PROJUDI.USU_SERV";
		stSql += " WHERE ID_USU_SERV = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public UsuarioServentiaDt consultarId(String id_usuarioserventia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioServentiaDt Dados=null;
		////System.out.println("....ps-ConsultaId_UsuarioServentia)");

		stSql= "SELECT * FROM PROJUDI.VIEW_USU_SERV WHERE ID_USU_SERV = ?";		ps.adicionarLong(id_usuarioserventia); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_UsuarioServentia  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioServentiaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( UsuarioServentiaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_USU_SERV"));
		Dados.setUsuarioServentia(rs.getString("USU_SERV"));
		Dados.setId_Usuario( rs.getString("ID_USU"));
		Dados.setUsuario( rs.getString("USU"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setId_Comarca( rs.getString("ID_COMARCA"));
		Dados.setComarca( rs.getString("COMARCA"));
		Dados.setId_EnderecoServentia( rs.getString("ENDERECO_SERV"));
		Dados.setEnderecoServentia( rs.getString("ENDERECO_SERV"));
		Dados.setId_BairroServentia( rs.getString("ID_BAIRRO_SERV"));
		Dados.setBairroServentia( rs.getString("BAIRRO_SERV"));
		Dados.setId_CidadeServentia( rs.getString("ID_CIDADE_SERV"));
		Dados.setCidadeServentia( rs.getString("CIDADE_SERV"));
		Dados.setAtivo( Funcoes.FormatarLogico(rs.getString("ATIVO")));
		Dados.setServentiaCodigo( rs.getString("SERV_CODIGO"));
		Dados.setServentiaCodigoExterno( rs.getString("SERV_CODIGO_EXTERNO"));
		Dados.setComarcaCodigo( rs.getString("COMARCA_CODIGO"));
		Dados.setBairroCodigoServentia( rs.getString("BAIRRO_CODIGO_SERV"));
		Dados.setCidadeCodigoServentia( rs.getString("CIDADE_CODIGO_SERV"));
		Dados.setNome( rs.getString("NOME"));
		Dados.setId_UsuarioServentiaChefe( rs.getString("ID_USU_SERV_CHEFE"));
		Dados.setUsuarioServentiaChefe( rs.getString("USU_SERV_CHEFE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoUsuarioServentia()");

		stSql= "SELECT ID_USU_SERV, USU_SERV FROM PROJUDI.VIEW_USU_SERV WHERE USU LIKE ?";
		stSql+= " ORDER BY USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoUsuarioServentia  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				UsuarioServentiaDt obTemp = new UsuarioServentiaDt();
				obTemp.setId(rs1.getString("ID_USU_SERV"));
				obTemp.setUsuarioServentia(rs1.getString("USU"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_USU_SERV WHERE USU LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..UsuarioServentiaPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
