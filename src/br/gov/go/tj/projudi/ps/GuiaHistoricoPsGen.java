package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.projudi.dt.GuiaHistoricoDt;


public class GuiaHistoricoPsGen extends Persistencia {


//---------------------------------------------------------
	public GuiaHistoricoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(GuiaHistoricoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.GUIA_HISTORICO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Guia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Guia());  

			stVirgula=",";
		}
		if ((dados.getValor().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getValor());  

			stVirgula=",";
		}
		if ((dados.getData().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getData());  

			stVirgula=",";
		}
		if ((dados.getItens().length()>0)) {
			 stSqlCampos+=   stVirgula + "ITENS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getItens());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_GUIA_HISTORICO",ps));
	} 

//---------------------------------------------------------
	public void alterar(GuiaHistoricoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.GUIA_HISTORICO SET  ";
		stSql+= "ID_GUIA = ?";		 ps.adicionarLong(dados.getId_Guia());  

		stSql+= ",VALOR = ?";		 ps.adicionarLong(dados.getValor());  

		stSql+= ",DATA = ?";		 ps.adicionarDateTime(dados.getData());  

		stSql+= ",ITENS = ?";		 ps.adicionarString(dados.getItens());  

		stSql += " WHERE ID_GUIA_HISTORICO  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.GUIA_HISTORICO";
		stSql += " WHERE ID_GUIA_HISTORICO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public GuiaHistoricoDt consultarId(String id_guiahistorico )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GuiaHistoricoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_GUIA_HISTORICO WHERE ID_GUIA_HISTORICO = ?";		ps.adicionarLong(id_guiahistorico); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GuiaHistoricoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( GuiaHistoricoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_GUIA_HISTORICO"));
		Dados.setNumeroGuiaCompleto(rs.getString("NUMERO_GUIA_COMPLETO"));
		Dados.setId_Guia( rs.getString("ID_GUIA"));
		Dados.setValor( rs.getString("VALOR"));
		Dados.setData( Funcoes.FormatarDataHora(rs.getDateTime("DATA")));
		Dados.setItens( rs.getString("ITENS"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_GUIA_HISTORICO, NUMERO_GUIA_COMPLETO FROM PROJUDI.VIEW_GUIA_HISTORICO WHERE NUMERO_GUIA_COMPLETO LIKE ?";
		stSql+= " ORDER BY NUMERO_GUIA_COMPLETO ";
		ps.adicionarString("%"+descricao+"%"); 

		try {

			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				GuiaHistoricoDt obTemp = new GuiaHistoricoDt();
				obTemp.setId(rs1.getString("ID_GUIA_HISTORICO"));
				obTemp.setNumeroGuiaCompleto(rs1.getString("NUMERO_GUIA_COMPLETO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_GUIA_HISTORICO WHERE NUMERO_GUIA_COMPLETO LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_GUIA_HISTORICO as id, NUMERO_GUIA_COMPLETO as descricao1 FROM PROJUDI.VIEW_GUIA_HISTORICO WHERE NUMERO_GUIA_COMPLETO LIKE ?";
		stSql+= " ORDER BY NUMERO_GUIA_COMPLETO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_GUIA_HISTORICO WHERE NUMERO_GUIA_COMPLETO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
