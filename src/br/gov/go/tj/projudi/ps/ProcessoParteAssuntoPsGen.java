package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.ProcessoParteAssuntoDt;


public class ProcessoParteAssuntoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7142086522121069749L;

	//---------------------------------------------------------
	public ProcessoParteAssuntoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ProcessoParteAssuntoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROC_PARTE_ASSUNTO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_Assunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Assunto());  

			stVirgula=",";
		}
		if ((dados.getDataInclusao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INCLUSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataInclusao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE_ASSUNTO",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> ProcessoParteAssuntoPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteAssuntoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROC_PARTE_ASSUNTO SET  ";
		stSql+= "ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_ASSUNTO = ?";		 ps.adicionarLong(dados.getId_Assunto());  

		stSql+= ",DATA_INCLUSAO = ?";		 ps.adicionarDateTime(dados.getDataInclusao());  

		stSql += " WHERE ID_PROC_PARTE_ASSUNTO  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROC_PARTE_ASSUNTO";
		stSql += " WHERE ID_PROC_PARTE_ASSUNTO = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ProcessoParteAssuntoDt consultarId(String id_procparteassunto )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteAssuntoDt Dados=null;


		stSql= "SELECT * FROM VIEW_PROC_PARTE_ASSUNTO WHERE ID_PROC_PARTE_ASSUNTO = ?";		ps.adicionarLong(id_procparteassunto); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoParteAssuntoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ProcessoParteAssuntoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PROC_PARTE_ASSUNTO"));
			Dados.setProcessoParteNome(rs.getString("NOME"));
			Dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));
			Dados.setId_Assunto( rs.getString("ID_ASSUNTO"));
			Dados.setAssunto( rs.getString("ASSUNTO"));
			Dados.setDataInclusao( Funcoes.FormatarDataHora(rs.getString("DATA_INCLUSAO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_PARTE_ASSUNTO, NOME ";
		stSqlFrom= " FROM VIEW_PROC_PARTE_ASSUNTO WHERE NOME LIKE ?";
		stSqlOrder = " ORDER BY NOME ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ProcessoParteAssuntoDt obTemp = new ProcessoParteAssuntoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_ASSUNTO"));
				obTemp.setProcessoParteNome(rs1.getString("NOME"));
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


		stSql= "SELECT ID_PROC_PARTE_ASSUNTO as id, NOME as descricao1 ";
		stSqlFrom= " FROM VIEW_PROC_PARTE_ASSUNTO WHERE NOME LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY NOME ";
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
