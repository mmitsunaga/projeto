package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;


public class PonteiroLogPsGen extends Persistencia {

	private static final long serialVersionUID = -7042614271613424828L;

	public PonteiroLogPsGen() { }

	public void inserir(PonteiroLogDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PONTEIRO_LOG ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_AreaDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getId_PonteiroLogTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PONTEIRO_LOG_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PonteiroLogTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Proc().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Proc());  

			stVirgula=",";
		}
		if ((dados.getId_Serv().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serv());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USER_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getData().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getData());  

			stVirgula=",";
		}
		if ((dados.getJustificativa().length()>0)) {
			 stSqlCampos+=   stVirgula + "JUSTIFICATIVA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getJustificativa());  

			stVirgula=",";
		}
		if ((dados.getQtd().length()>0)) {
			 stSqlCampos+=   stVirgula + "QTD " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getQtd());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo() != null && dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PONTEIRO_LOG",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> PonteiroLogPsGen.inserir() " + e.getMessage() );
			} 
	} 


	public PonteiroLogDt consultarId(String id_ponteirolog )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PonteiroLogDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PONTEIRO_LOG WHERE ID_PONTEIRO_LOG = ?";		ps.adicionarLong(id_ponteirolog); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PonteiroLogDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt(PonteiroLogDt Dados, ResultSetTJGO rs) throws Exception {

		Dados.setId(rs.getString("ID_PONTEIRO_LOG"));
		Dados.setPonteiroLogTipo(rs.getString("PONTEIRO_LOG_TIPO"));
		Dados.setId_AreaDistribuicao(rs.getString("ID_AREA_DIST"));
		Dados.setAreaDistribuicao(rs.getString("AREA_DIST"));
		Dados.setId_PonteiroLogTipo(rs.getString("ID_PONTEIRO_LOG_TIPO"));
		Dados.setId_Proc(rs.getString("ID_PROC"));
		Dados.setProcNumero(rs.getString("PROC_NUMERO"));
		Dados.setId_Serventia(rs.getString("ID_SERV"));
		Dados.setServentia(rs.getString("SERV"));
		Dados.setId_UsuarioServentia(rs.getString("ID_USER_SERV"));
		Dados.setNome(rs.getString("NOME"));
		Dados.setId_ServentiaCargo(rs.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo(rs.getString("SERV_CARGO"));
		Dados.setServentiaCargoUsuario(rs.getString("MAGISTRADO"));
		Dados.setData(Funcoes.FormatarDataHora(rs.getDate("DATA")));
		Dados.setJustificativa(rs.getString("JUSTIFICATIVA"));
		Dados.setQtd(rs.getString("QTD"));
		Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_PONTEIRO_LOG, PONTEIRO_LOG_TIPO FROM PROJUDI.VIEW_PONTEIRO_LOG WHERE PONTEIRO_LOG_TIPO LIKE ?";
		stSql+= " ORDER BY PONTEIRO_LOG_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			while (rs1.next()) {
				PonteiroLogDt obTemp = new PonteiroLogDt();
				obTemp.setId(rs1.getString("ID_PONTEIRO_LOG"));
				obTemp.setPonteiroLogTipo(rs1.getString("PONTEIRO_LOG_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PONTEIRO_LOG WHERE PONTEIRO_LOG_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);

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
		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_PONTEIRO_LOG as id, JUSTIFICATIVA as descricao1 FROM PROJUDI.VIEW_PONTEIRO_LOG WHERE JUSTIFICATIVA LIKE ?";
		stSql+= " ORDER BY PONTEIRO_LOG_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PONTEIRO_LOG WHERE JUSTIFICATIVA LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}

} 
