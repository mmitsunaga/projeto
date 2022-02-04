package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoEncaminhamentoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2854710703549875188L;

//---------------------------------------------------------
	public ProcessoEncaminhamentoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ProcessoEncaminhamentoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.proc_encaminhamento ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Proc().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Proc());  

			stVirgula=",";
		}
		if ((dados.getId_ServOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServOrigem());  

			stVirgula=",";
		}
		if ((dados.getId_ServEncaminhamento().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_ENCAMINHAMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServEncaminhamento());  

			stVirgula=",";
		}
		if ((dados.getId_UsuServEncaminhamento().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_ENCAMINHAMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuServEncaminhamento());  

			stVirgula=",";
		}
		if ((dados.getId_UsuServRetorno().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_RETORNO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuServRetorno());  

			stVirgula=",";
		}
		if ((dados.getDataEncaminhamento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_ENCAMINHAMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEncaminhamento());  

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


		try {
			dados.setId(executarInsert(stSql,"ID_PROC_ENCAMINHAMENTO",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> ProcessoEncaminhamentoPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoEncaminhamentoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.proc_encaminhamento SET  ";
		stSql+= "ID_PROC = ?";		 ps.adicionarLong(dados.getId_Proc());  

		stSql+= ",ID_SERV_ORIGEM = ?";		 ps.adicionarLong(dados.getId_ServOrigem());  

		stSql+= ",ID_SERV_ENCAMINHAMENTO = ?";		 ps.adicionarLong(dados.getId_ServEncaminhamento());  

		stSql+= ",ID_USU_SERV_ENCAMINHAMENTO = ?";		 ps.adicionarLong(dados.getId_UsuServEncaminhamento());  

		stSql+= ",ID_USU_SERV_RETORNO = ?";		 ps.adicionarLong(dados.getId_UsuServRetorno());  

		stSql+= ",DATA_ENCAMINHAMENTO = ?";		 ps.adicionarDateTime(dados.getDataEncaminhamento());  

		stSql+= ",DATA_RETORNO = ?";		 ps.adicionarDateTime(dados.getDataRetorno());  

		stSql += " WHERE ID_PROC_ENCAMINHAMENTO  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.proc_encaminhamento";
		stSql += " WHERE ID_PROC_ENCAMINHAMENTO = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ProcessoEncaminhamentoDt consultarId(String id_procencaminhamento )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoEncaminhamentoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.view_proc_encaminhamento WHERE ID_PROC_ENCAMINHAMENTO = ?";		ps.adicionarLong(id_procencaminhamento); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoEncaminhamentoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ProcessoEncaminhamentoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PROC_ENCAMINHAMENTO"));
			Dados.setProcNumero(rs.getString("PROC_NUMERO"));
			Dados.setId_Proc( rs.getString("ID_PROC"));
			Dados.setId_ServOrigem( rs.getString("ID_SERV_ORIGEM"));
			Dados.setServOrigem( rs.getString("SERV_ORIGEM"));
			Dados.setId_ServEncaminhamento( rs.getString("ID_SERV_ENCAMINHAMENTO"));
			Dados.setServEncaminhamento( rs.getString("SERV_ENCAMINHAMENTO"));
			Dados.setId_UsuServEncaminhamento( rs.getString("ID_USU_SERV_ENCAMINHAMENTO"));
			Dados.setUsuEncaminhamento( rs.getString("USU_ENCAMINHAMENTO"));
			Dados.setId_UsuServRetorno( rs.getString("ID_USU_SERV_RETORNO"));
			Dados.setUsuRetorno( rs.getString("USU_RETORNO"));
			Dados.setDataEncaminhamento( Funcoes.FormatarDataHora(rs.getString("DATA_ENCAMINHAMENTO")));
			Dados.setDataRetorno( Funcoes.FormatarDataHora(rs.getString("DATA_RETORNO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_ENCAMINHAMENTO, PROC_NUMERO ";
		stSqlFrom= " FROM PROJUDI.view_proc_encaminhamento WHERE PROC_NUMERO LIKE ?";
		stSqlOrder = " ORDER BY PROC_NUMERO ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ProcessoEncaminhamentoDt obTemp = new ProcessoEncaminhamentoDt();
				obTemp.setId(rs1.getString("ID_PROC_ENCAMINHAMENTO"));
				obTemp.setProcNumero(rs1.getString("PROC_NUMERO"));
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


		stSql= "SELECT ID_PROC_ENCAMINHAMENTO as id, PROC_NUMERO as descricao1 ";
		stSqlFrom= " FROM PROJUDI.view_proc_encaminhamento WHERE PROC_NUMERO LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY PROC_NUMERO ";
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
