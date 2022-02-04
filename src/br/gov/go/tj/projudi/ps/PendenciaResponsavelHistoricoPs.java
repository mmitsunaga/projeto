package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class PendenciaResponsavelHistoricoPs extends PendenciaResponsavelHistoricoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5560105834061453517L;

	public PendenciaResponsavelHistoricoPs(Connection conexao){
		Conexao = conexao;
	}
	
	public void fecharHistorico(String id_Pendencia, String data_Fim ) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PEND_RESP_HIST SET  ";
		stSql+= " DATA_FIM = ?";		 ps.adicionarDateTime(data_Fim);  
		stSql += " WHERE ID_PEND  = ? "; 		ps.adicionarLong(id_Pendencia); 
		stSql += " AND DATA_FIM IS NULL";

		executarUpdateDelete(stSql,ps); 
	
	} 

	public void atualizaHistoricoPendenciaFilha(String idPendenciaPai, String idPendenciaFilha)throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PEND_RESP_HIST SET  ";
		stSql+= " ID_PEND = ?";	ps.adicionarLong(idPendenciaFilha); 
		stSql += " WHERE ID_PEND  = ? "; ps.adicionarLong(idPendenciaPai); 

		executarUpdateDelete(stSql,ps);
	}
	
	public List consultarHistoricosPendenciaFinal(String id_Pendencia ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP_HIST_FINAL WHERE ID_PEND = ?";
		stSql+= " ORDER BY ID_PEND_RESP_HIST_FINAL ";
		ps.adicionarLong(id_Pendencia);

		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				PendenciaResponsavelHistoricoDt obTemp = new PendenciaResponsavelHistoricoDt();
				obTemp.setId(rs1.getString("ID_PEND_RESP_HIST_FINAL"));
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
	
	/**
	 * Método que realiza consulta do último histórico de conclusão aberto.
	 * @param id_Pendencia - ID da pendência
	 * @return dt com a informação do histórico
	 * @throws Exception
	 * @author hmgodinho
	 */
	public PendenciaResponsavelHistoricoDt consultarHistoricoAbertoPendencia(String id_Pendencia) throws Exception {
		String stSql="";
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaResponsavelHistoricoDt obTemp = null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE ID_PEND = ? AND DATA_FIM IS NULL";
		ps.adicionarLong(id_Pendencia);
		stSql+= " ORDER BY ID_PEND_RESP_HIST DESC";

		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				obTemp = new PendenciaResponsavelHistoricoDt();
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
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return obTemp; 
	}
	
	/**
	 * Método que realiza consulta do resposavel anterior
	 * @param id_Pendencia - ID da pendência
	 * @return dt com a informação do histórico
	 * @throws Exception
	 * @author Jesus Rodrigo
	 */
	public PendenciaResponsavelHistoricoDt consultarResponsavelAnteriorPendencia(String id_Pendencia) throws Exception {
		String stSql="";
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaResponsavelHistoricoDt obTemp = null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP_HIST WHERE ID_PEND = ? AND DATA_FIM IS NOT NULL  ";		ps.adicionarLong(id_Pendencia);
		stSql+= " ORDER BY ID_PEND_RESP_HIST DESC FETCH NEXT 1 ROWS ONLY";

		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				obTemp = new PendenciaResponsavelHistoricoDt();
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
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return obTemp; 
	}

}
