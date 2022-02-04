package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoMovimentacaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoObjetoArquivoMovimentacaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7846661032544944725L;

	//---------------------------------------------------------
	public ProcessoObjetoArquivoMovimentacaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ProcessoObjetoArquivoMovimentacaoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO projudi.PROC_OBJETO_ARQ_movi ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoObjetoArquivoMovimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_OBJETO_ARQ_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoObjetoArquivoMovimentacao());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoObjetoArquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_OBJETO_ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoObjetoArquivo());  

			stVirgula=",";
		}
		if ((dados.getDataMovimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataMovimentacao());  

			stVirgula=",";
		}
		if ((dados.getDataRetorno().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_RETORNO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataRetorno());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


			dados.setId(executarInsert(stSql,"ID_PROC_OBJETO_ARQ_MOVI",ps));


	} 

//---------------------------------------------------------
	public void alterar(ProcessoObjetoArquivoMovimentacaoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE projudi.PROC_OBJETO_ARQ_movi SET  ";
		stSql+= "PROC_OBJETO_ARQ_MOVI = ?";		 ps.adicionarString(dados.getProcessoObjetoArquivoMovimentacao());  

		stSql+= ",ID_PROC_OBJETO_ARQ = ?";		 ps.adicionarLong(dados.getId_ProcessoObjetoArquivo());  

		stSql+= ",DATA_MOVI = ?";		 ps.adicionarDateTime(dados.getDataMovimentacao());  

		stSql+= ",DATA_RETORNO = ?";		 ps.adicionarDateTime(dados.getDataRetorno());  

		stSql += " WHERE ID_PROC_OBJETO_ARQ_MOVI  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM projudi.PROC_OBJETO_ARQ_movi";
		stSql += " WHERE ID_PROC_OBJETO_ARQ_MOVI = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ProcessoObjetoArquivoMovimentacaoDt consultarId(String id_processoobjetoarquivomovimentacao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoObjetoArquivoMovimentacaoDt Dados=null;


		stSql= "SELECT * FROM projudi.VIEW_PROC_OBJETO_ARQ_movi WHERE ID_PROC_OBJETO_ARQ_MOVI = ?";		ps.adicionarLong(id_processoobjetoarquivomovimentacao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoObjetoArquivoMovimentacaoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ProcessoObjetoArquivoMovimentacaoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PROC_OBJETO_ARQ_MOVI"));
			Dados.setProcessoObjetoArquivoMovimentacao(rs.getString("PROC_OBJETO_ARQ_MOVI"));
			Dados.setId_ProcessoObjetoArquivo( rs.getString("ID_PROC_OBJETO_ARQ"));
			Dados.setProcessoObjetoArquivo( rs.getString("PROC_OBJETO_ARQ"));
			Dados.setDataMovimentacao( Funcoes.FormatarData(rs.getDate("DATA_MOVI")));
			Dados.setDataRetorno( Funcoes.FormatarData(rs.getDate("DATA_RETORNO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_OBJETO_ARQ_MOVI, PROC_OBJETO_ARQ_MOVI ";
		stSqlFrom= " FROM projudi.VIEW_PROC_OBJETO_ARQ_movi WHERE PROC_OBJETO_ARQ_MOVI LIKE ?";
		stSqlOrder = " ORDER BY PROC_OBJETO_ARQ_MOVI ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ProcessoObjetoArquivoMovimentacaoDt obTemp = new ProcessoObjetoArquivoMovimentacaoDt();
				obTemp.setId(rs1.getString("ID_PROC_OBJETO_ARQ_MOVI"));
				obTemp.setProcessoObjetoArquivoMovimentacao(rs1.getString("PROC_OBJETO_ARQ_MOVI"));
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


		stSql= "SELECT ID_PROC_OBJETO_ARQ_MOVI as id, PROC_OBJETO_ARQ_MOVI as descricao1 ";
		stSqlFrom= " FROM projudi.VIEW_PROC_OBJETO_ARQ_movi WHERE PROC_OBJETO_ARQ_MOVI LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY PROC_OBJETO_ARQ_MOVI ";
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
