package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class UsuarioServentiaAfastamentoPsGen extends Persistencia {


	/**
	 * 
	 */
	private static final long serialVersionUID = 356187038479232346L;

	//---------------------------------------------------------
	public UsuarioServentiaAfastamentoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(UsuarioServentiaAfastamentoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioAfastamentoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.USU_SERV_AFASTAMENTO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getId_Afastamento().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AFASTAMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Afastamento());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataInicio());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOTIVO_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getMotivoInicio());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_CADASTRADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getId_UsuServCadastrador());  

			stVirgula=",";
		}
		if ((dados.getDataFim().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataFim());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOTIVO_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getMotivoFim());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_FINALIZADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getId_UsuServFinalizador());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_USU_SERV_AFASTAMENTO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(UsuarioServentiaAfastamentoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psUsuarioAfastamentoalterar()");

		stSql= "UPDATE PROJUDI.USU_SERV_AFASTAMENTO SET  ";
		stSql+= "ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql+= ",ID_AFASTAMENTO = ?";		 ps.adicionarLong(dados.getId_Afastamento());  

		stSql+= ",DATA_INICIO = ?";		 ps.adicionarDateTime(dados.getDataInicio());  
		
		stSql+= ",MOTIVO_INICIO = ?";		 ps.adicionarString(dados.getMotivoInicio());
		
		stSql+= ",ID_USU_SERV_CADASTRADOR = ?";		 ps.adicionarLong(dados.getId_UsuServCadastrador());

		stSql+= ",DATA_FIM = ?";		 ps.adicionarDateTime(dados.getDataFim());
		
		stSql+= ",MOTIVO_FIM = ?";		 ps.adicionarString(dados.getMotivoFim());
		
		stSql+= ",ID_USU_SERV_FINALIZADOR = ?";		 ps.adicionarLong(dados.getId_UsuServFinalizador());

		stSql += " WHERE ID_USU_SERV_AFASTAMENTO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioAfastamentoexcluir()");

		stSql= "DELETE FROM PROJUDI.USU_SERV_AFASTAMENTO";
		stSql += " WHERE ID_USU_SERV_AFASTAMENTO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public UsuarioServentiaAfastamentoDt consultarId(String id_usuarioServentiaAfastamento )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioServentiaAfastamentoDt Dados=null;
		////System.out.println("....ps-ConsultaId_UsuarioAfastamento)");

		stSql= "SELECT * FROM PROJUDI.VIEW_USU_SERV_AFASTAMENTO WHERE ID_USU_SERV_AFASTAMENTO = ?";		ps.adicionarLong(id_usuarioServentiaAfastamento); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_UsuarioAfastamento  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioServentiaAfastamentoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( UsuarioServentiaAfastamentoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_USU_SERV_AFASTAMENTO"));
		Dados.setUsuario(rs.getString("USU"));
		Dados.setNomeUsuario(rs.getString("USU_NOME"));
		Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
		Dados.setId_Afastamento( rs.getString("ID_AFASTAMENTO"));
		Dados.setAfastamento( rs.getString("AFASTAMENTO"));
		Dados.setDataInicio( Funcoes.FormatarDataHora(rs.getDateTime("DATA_INICIO")));
		Dados.setMotivoInicio(rs.getString("MOTIVO_INICIO"));
		Dados.setId_UsuServCadastrador(rs.getString("ID_USU_SERV_CADASTRADOR"));
		Dados.setDataFim( Funcoes.FormatarDataHora(rs.getDateTime("DATA_FIM")));
		Dados.setMotivoFim(rs.getString("MOTIVO_FIM"));
		Dados.setId_UsuServFinalizador(rs.getString("ID_USU_SERV_FINALIZADOR"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoUsuarioAfastamento()");

		stSql= "SELECT ID_USU_SERV_AFASTAMENTO, USU FROM PROJUDI.VIEW_USU_SERV_AFASTAMENTO WHERE USU LIKE ?";
		stSql+= " ORDER BY USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoUsuarioAfastamento  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				UsuarioServentiaAfastamentoDt obTemp = new UsuarioServentiaAfastamentoDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_AFASTAMENTO"));
				obTemp.setUsuario(rs1.getString("USU"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_USU_SERV_AFASTAMENTO WHERE USU LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..UsuarioAfastamentoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
