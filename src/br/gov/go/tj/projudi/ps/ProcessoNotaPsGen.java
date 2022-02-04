package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoNotaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoNotaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1109767862739438408L;

//---------------------------------------------------------
	public ProcessoNotaPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ProcessoNotaDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.proc_nota ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getProcessoNota().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_NOTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoNota());  

			stVirgula=",";
		}
		if ((dados.getDataCriacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_CRIACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataCriacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PROC_NOTA",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> ProcessoNotaPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoNotaDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.proc_nota SET  ";
		stSql+= "ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

		stSql+= ",PROC_NOTA = ?";		 ps.adicionarString(dados.getProcessoNota());  

		stSql+= ",DATA_CRIACAO = ?";		 ps.adicionarDateTime(dados.getDataCriacao());  

		stSql += " WHERE ID_PROC_NOTA  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.proc_nota";
		stSql += " WHERE ID_PROC_NOTA = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ProcessoNotaDt consultarId(String id_processonota )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoNotaDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_proc_nota WHERE ID_PROC_NOTA = ?";		ps.adicionarLong(id_processonota); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoNotaDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ProcessoNotaDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PROC_NOTA"));
			Dados.setNome(rs.getString("NOME"));
			Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
			Dados.setId_Processo( rs.getString("ID_PROC"));
			Dados.setProcNumero( rs.getString("PROC_NUMERO"));
			Dados.setProcessoNota( rs.getString("PROC_NOTA"));
			Dados.setDataCriacao( Funcoes.FormatarDataHora(rs.getString("DATA_CRIACAO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_NOTA, NOME ";
		stSqlFrom= " FROM PROJUDI.VIEW_proc_nota WHERE NOME LIKE ?";
		stSqlOrder = " ORDER BY NOME ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ProcessoNotaDt obTemp = new ProcessoNotaDt();
				obTemp.setId(rs1.getString("ID_PROC_NOTA"));
				obTemp.setNome(rs1.getString("NOME"));
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


		stSql= "SELECT ID_PROC_NOTA as id, NOME as descricao1 ";
		stSqlFrom= " FROM PROJUDI.VIEW_proc_nota WHERE NOME LIKE ?";
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
