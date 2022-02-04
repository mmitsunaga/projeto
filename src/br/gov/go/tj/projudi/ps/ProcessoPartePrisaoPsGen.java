package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;


public class ProcessoPartePrisaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6849672906686315532L;

	//---------------------------------------------------------
	public ProcessoPartePrisaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ProcessoPartePrisaoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO proc_parte_prisao_novo ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getDataPrisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_PRISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataPrisao());  

			stVirgula=",";
		}
		if ((dados.getId_PrisaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PRISAO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PrisaoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_LocalCumpPena().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_LOCAL_CUMP_PENA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_LocalCumpPena());  

			stVirgula=",";
		}
		if ((dados.getDataEvento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EVENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEvento());  

			stVirgula=",";
		}
		if ((dados.getId_EventoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_EVENTO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EventoTipo());  

			stVirgula=",";
		}
		if ((dados.getPrazoPrisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRAZO_PRISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPrazoPrisao());  

			stVirgula=",";
		}
		if ((dados.getId_MoviEvento().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI_EVENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MoviEvento());  

			stVirgula=",";
		}
		if ((dados.getId_MoviPrisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI_PRISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MoviPrisao());  

			stVirgula=",";
		}
		if ((dados.getObservacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "OBSERVACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getObservacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE_PRISAO",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> ProcessoPartePrisaoPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoPartePrisaoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE proc_parte_prisao_novo SET  ";
		stSql+= "ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",DATA_PRISAO = ?";		 ps.adicionarDateTime(dados.getDataPrisao());  

		stSql+= ",ID_PRISAO_TIPO = ?";		 ps.adicionarLong(dados.getId_PrisaoTipo());  

		stSql+= ",ID_LOCAL_CUMP_PENA = ?";		 ps.adicionarLong(dados.getId_LocalCumpPena());  

		stSql+= ",DATA_EVENTO = ?";		 ps.adicionarDateTime(dados.getDataEvento());  

		stSql+= ",ID_EVENTO_TIPO = ?";		 ps.adicionarLong(dados.getId_EventoTipo());  

		stSql+= ",PRAZO_PRISAO = ?";		 ps.adicionarLong(dados.getPrazoPrisao());  

		stSql+= ",ID_MOVI_EVENTO = ?";		 ps.adicionarLong(dados.getId_MoviEvento());  

		stSql+= ",ID_MOVI_PRISAO = ?";		 ps.adicionarLong(dados.getId_MoviPrisao());  

		stSql+= ",OBSERVACAO = ?";		 ps.adicionarString(dados.getObservacao());  

		stSql += " WHERE ID_PROC_PARTE_PRISAO  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM proc_parte_prisao_novo";
		stSql += " WHERE ID_PROC_PARTE_PRISAO = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ProcessoPartePrisaoDt consultarId(String id_procparteprisao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoPartePrisaoDt Dados=null;


		stSql= "SELECT * FROM view_proc_parte_prisao WHERE ID_PROC_PARTE_PRISAO = ?";		ps.adicionarLong(id_procparteprisao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoPartePrisaoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ProcessoPartePrisaoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PROC_PARTE_PRISAO"));
			Dados.setPrisaoTipo(rs.getString("PRISAO_TIPO"));
			Dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));
			Dados.setNome( rs.getString("NOME"));
			Dados.setDataPrisao( Funcoes.FormatarDataHora(rs.getString("DATA_PRISAO")));
			Dados.setId_PrisaoTipo( rs.getString("ID_PRISAO_TIPO"));
			Dados.setId_LocalCumpPena( rs.getString("ID_LOCAL_CUMP_PENA"));
			Dados.setLocalCumpPena( rs.getString("LOCAL_CUMP_PENA"));			
			Dados.setDataEvento( Funcoes.FormatarDataHora(rs.getString("DATA_EVENTO")));
			Dados.setId_EventoTipo( rs.getString("ID_EVENTO_TIPO"));
			Dados.setEventoTipo( rs.getString("EVENTO_TIPO"));
			Dados.setPrazoPrisao( rs.getString("PRAZO_PRISAO"));
			Dados.setId_MoviEvento( rs.getString("ID_MOVI_EVENTO"));
			Dados.setMoviEvento( rs.getString("MOVI_EVENTO"));
			Dados.setId_MoviPrisao( rs.getString("ID_MOVI_PRISAO"));
			Dados.setMoviPrisao( rs.getString("MOVI_PRISAO"));
			Dados.setObservacao( rs.getString("OBSERVACAO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_PARTE_PRISAO, PRISAO_TIPO ";
		stSqlFrom= " FROM view_proc_parte_prisao WHERE PRISAO_TIPO LIKE ?";
		stSqlOrder = " ORDER BY PRISAO_TIPO ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ProcessoPartePrisaoDt obTemp = new ProcessoPartePrisaoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_PRISAO"));
				obTemp.setPrisaoTipo(rs1.getString("PRISAO_TIPO"));
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

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_PARTE_PRISAO as id, PRISAO_TIPO as descricao1 ";
		stSqlFrom= " FROM view_proc_parte_prisao WHERE PRISAO_TIPO LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY PRISAO_TIPO ";
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
