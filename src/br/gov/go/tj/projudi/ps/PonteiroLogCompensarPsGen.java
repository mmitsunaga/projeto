package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PonteiroLogCompensarPsGen extends Persistencia {

	public PonteiroLogCompensarPsGen() {

	}

	public void inserir(PonteiroLogCompensarDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PONTEIRO_LOG_COMPENSAR ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_AreaDistribuicao_O().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST_O " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicao_O());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia_O().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_O " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia_O());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo_O().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO_O " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo_O());  

			stVirgula=",";
		}
		if ((dados.getId_AreaDistribuicao_D().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST_D " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicao_D());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia_D().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_D " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia_D());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo_D().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO_D " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo_D());  

			stVirgula=",";
		}
		if ((dados.getQtd().length()>0)) {
			 stSqlCampos+=   stVirgula + "QTD " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getQtd());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia_I().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_I " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia_I());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia_F().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_F " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia_F());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataInicio());  

			stVirgula=",";
		}
		if ((dados.getDataFinal().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FINAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataFinal());  

			stVirgula=",";
		}
		if ((dados.getJustificativa().length()>0)) {
			 stSqlCampos+=   stVirgula + "JUSTIFICATIVA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getJustificativa());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		try {
			dados.setId(executarInsert(stSql,"ID_PONTEIRO_LOG_COMPENSAR",ps));
		}catch(Exception e){
			throw new MensagemException("PonteiroLogCompensarPsGen.inserir() - Não foi possível salvar os dados." + e.getMessage());
		} 
	} 

	public void alterar(PonteiroLogCompensarDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PONTEIRO_LOG_COMPENSAR SET  ";
		stSql+= "ID_AREA_DIST_O = ?";		 ps.adicionarLong(dados.getId_AreaDistribuicao_O());  

		stSql+= ",ID_SERV_O = ?";		 ps.adicionarLong(dados.getId_Serventia_O());  

		stSql+= ",ID_SERV_CARGO_O = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo_O());  

		stSql+= ",ID_AREA_DIST_D = ?";		 ps.adicionarLong(dados.getId_AreaDistribuicao_D());  

		stSql+= ",ID_SERV_D = ?";		 ps.adicionarLong(dados.getId_Serventia_D());  

		stSql+= ",ID_SERV_CARGO_D = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo_D());  

		stSql+= ",QTD = ?";		 ps.adicionarLong(dados.getQtd());  

		stSql+= ",ID_USU_SERV_I = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia_I());  

		stSql+= ",ID_USU_SERV_F = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia_F());  

		stSql+= ",DATA_INICIO = ?";		 ps.adicionarDateTime(dados.getDataInicio()); 

		if(dados.getDataFinal() != null && !dados.getDataFinal().equalsIgnoreCase("")) {
			stSql+= ",DATA_FINAL = ?";		 ps.adicionarDateTime(dados.getDataFinal());  
		}

		stSql+= ",JUSTIFICATIVA = ?";		 ps.adicionarString(dados.getJustificativa());  

		stSql += " WHERE ID_PONTEIRO_LOG_COMPENSAR  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

	public void excluir( String chave) throws Exception { 

		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSql= "DELETE FROM PROJUDI.PONTEIRO_LOG_COMPENSAR";
		stSql += " WHERE ID_PONTEIRO_LOG_COMPENSAR = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

	public PonteiroLogCompensarDt consultarId(String id_ponteirologcompensar )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PonteiroLogCompensarDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PONTEIRO_LOG_COMPENSAR WHERE ID_PONTEIRO_LOG_COMPENSAR = ?";		ps.adicionarLong(id_ponteirologcompensar); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PonteiroLogCompensarDt();
				associarDt(Dados, rs1);
			}
		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( PonteiroLogCompensarDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PONTEIRO_LOG_COMPENSAR"));
			Dados.setJustificativa(rs.getString("JUSTIFICATIVA"));
			Dados.setId_AreaDistribuicao_O( rs.getString("ID_AREA_DIST_O"));
			Dados.setAreaDistribuicao_O( rs.getString("AREA_DIST_O"));
			Dados.setId_Serventia_O( rs.getString("ID_SERV_O"));
			Dados.setServentia_O( rs.getString("SERV_O"));
			Dados.setId_ServentiaCargo_O( rs.getString("ID_SERV_CARGO_O"));
			Dados.setServentiaCargo_O( rs.getString("SERV_CARGO_O"));
			Dados.setId_AreaDistribuicao_D( rs.getString("ID_AREA_DIST_D"));
			Dados.setAreaDistribuicao_D( rs.getString("AREA_DIST_D"));
			Dados.setId_Serventia_D( rs.getString("ID_SERV_D"));
			Dados.setServentia_D( rs.getString("SERV_D"));
			Dados.setId_ServentiaCargo_D( rs.getString("ID_SERV_CARGO_D"));
			Dados.setServentiaCargo_D( rs.getString("SERV_CARGO_D"));
			Dados.setQtd( rs.getString("QTD"));
			Dados.setId_UsuarioServentia_I( rs.getString("ID_USU_SERV_I"));
			Dados.setUsuario_I( rs.getString("USU_I"));
			Dados.setId_UsuarioServentia_F( rs.getString("ID_USU_SERV_F"));
			Dados.setUsuario_F( rs.getString("USU_F"));
			Dados.setDataInicio( Funcoes.FormatarData(rs.getDate("DATA_INICIO")));
			Dados.setDataFinal( Funcoes.FormatarData(rs.getDate("DATA_FINAL")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_PONTEIRO_LOG_COMPENSAR, JUSTIFICATIVA ";
		stSqlFrom= " FROM PROJUDI.VIEW_PONTEIRO_LOG_COMPENSAR WHERE JUSTIFICATIVA LIKE ?";
		stSqlOrder = " ORDER BY JUSTIFICATIVA ";
		ps.adicionarString(descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			while (rs1.next()) {
				PonteiroLogCompensarDt obTemp = new PonteiroLogCompensarDt();
				obTemp.setId(rs1.getString("ID_PONTEIRO_LOG_COMPENSAR"));
				obTemp.setJustificativa(rs1.getString("JUSTIFICATIVA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return liTemp; 
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 3;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_PONTEIRO_LOG_COMPENSAR as id, JUSTIFICATIVA as descricao1, to_char(DATA_INICIO, 'dd/mm/yyyy') as descricao2, to_char(DATA_FINAL, 'dd/mm/yyyy') as descricao3 "; 
		stSqlFrom= " FROM VIEW_PONTEIRO_LOG_COMPENSAR WHERE JUSTIFICATIVA LIKE ?";
		ps.adicionarString("%"+descricao+"%"); 
		stSqlOrder= " ORDER BY JUSTIFICATIVA ";
		
		try{
			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}

} 
