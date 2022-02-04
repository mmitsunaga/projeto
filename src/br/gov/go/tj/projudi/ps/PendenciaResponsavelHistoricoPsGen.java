package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PendenciaResponsavelHistoricoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 2756407056488280763L;



	//---------------------------------------------------------
	public PendenciaResponsavelHistoricoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(PendenciaResponsavelHistoricoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PEND_RESP_HIST ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPendenciaResponsavelHistorico().length()>0)) {
			 stSqlCampos+=   stVirgula + "PEND_RESP_HIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPendenciaResponsavelHistorico());  

			stVirgula=",";
		}
		if ((dados.getId_Pendencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Pendencia());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		
		if ((dados.getId_ServentiaGrupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaGrupo());  

			stVirgula=",";
		}
		
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataInicio());  

			stVirgula=",";
		}
		if ((dados.getDataFim().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataFim());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_PEND_RESP_HIST",ps));
	} 

//---------------------------------------------------------
	public void alterar(PendenciaResponsavelHistoricoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.PEND_RESP_HIST SET  ";
		stSql+= "PEND_RESP_HIST = ?";		 ps.adicionarString(dados.getPendenciaResponsavelHistorico());  

		stSql+= ",ID_PEND = ?";		 ps.adicionarLong(dados.getId_Pendencia());  

		stSql+= ",ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql+= ",DATA_INICIO = ?";		 ps.adicionarDateTime(dados.getDataInicio());  

		stSql+= ",DATA_FIM = ?";		 ps.adicionarDateTime(dados.getDataFim());  

		stSql += " WHERE ID_PEND_RESP_HIST  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PEND_RESP_HIST";
		stSql += " WHERE ID_PEND_RESP_HIST = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public PendenciaResponsavelHistoricoDt consultarId(String id_pendenciaresponsavelhistorico )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaResponsavelHistoricoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE ID_PEND_RESP_HIST = ?";		ps.adicionarLong(id_pendenciaresponsavelhistorico); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaResponsavelHistoricoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( PendenciaResponsavelHistoricoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PEND_RESP_HIST"));
		Dados.setPendenciaResponsavelHistorico(rs.getString("PEND_RESP_HIST"));
		Dados.setId_Pendencia( rs.getString("ID_PEND"));
		Dados.setPendendencia( rs.getString("PEND"));
		Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo( rs.getString("SERV_CARGO"));
		Dados.setDataInicio( Funcoes.FormatarDataHora(rs.getDateTime("DATA_INICIO")));
		Dados.setDataFim( Funcoes.FormatarDataHora(rs.getDateTime("DATA_FIM")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PEND_RESP_HIST, PEND_RESP_HIST FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE PEND_RESP_HIST LIKE ?";
		stSql+= " ORDER BY PEND_RESP_HIST ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				PendenciaResponsavelHistoricoDt obTemp = new PendenciaResponsavelHistoricoDt();
				obTemp.setId(rs1.getString("ID_PEND_RESP_HIST"));
				obTemp.setPendenciaResponsavelHistorico(rs1.getString("PEND_RESP_HIST"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE PEND_RESP_HIST LIKE ?";
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
	
	public List consultarHistoricosPendencia(String id_Pendencia ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE ID_PEND = ?";
		stSql+= " ORDER BY ID_PEND_RESP_HIST ";
		ps.adicionarLong(id_Pendencia);

		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				PendenciaResponsavelHistoricoDt obTemp = new PendenciaResponsavelHistoricoDt();
				obTemp.setId(rs1.getString("ID_PEND_RESP_HIST"));
				obTemp.setPendenciaResponsavelHistorico(rs1.getString("PEND_RESP_HIST"));
				obTemp.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				obTemp.setDataFim(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				obTemp.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setId_ServentiaGrupo(rs1.getString("ID_SERV_GRUPO"));
				obTemp.setServentiaGrupo(rs1.getString("SERV_GRUPO"));
				obTemp.setAtividade(rs1.getString("ATIVIDADE"));
				obTemp.setEnviaMagistrado(rs1.getBoolean("ENVIA_DESEMBARGADOR"));
				liTemp.add(obTemp);
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
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


		stSql= "SELECT ID_PEND_RESP_HIST as id, PEND_RESP_HIST as descricao1 FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE PEND_RESP_HIST LIKE ?";
		stSql+= " ORDER BY PEND_RESP_HIST ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE PEND_RESP_HIST LIKE ?";
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
