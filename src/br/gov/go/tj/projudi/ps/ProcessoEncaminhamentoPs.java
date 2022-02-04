package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class ProcessoEncaminhamentoPs extends ProcessoEncaminhamentoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1583708259586385176L;
	private ProcessoEncaminhamentoPs( ) {}
	public ProcessoEncaminhamentoPs(Connection conexao) {
		Conexao = conexao;
	}
//
	public long consultarQuantidadeProcessosEncaminhados(String id_Serventia) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			stSql = "SELECT COUNT(1) AS QUANTIDADE FROM PROJUDI.PROC_ENCAMINHAMENTO p ";
			stSql += " WHERE p.ID_SERV_ORIGEM = ?";			ps.adicionarLong(id_Serventia);			
			stSql += " AND p.DATA_RETORNO is null "; 

			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return loQuantidade;
	}

	/**
	 * Consulta a tabela proc_encaminhamento. Caso haja algum encamminhamento com os parâmetros informados e sem retorno para a origem registrado, traz o id
	 * do registro na tabela. Caso não encontre nenhum encaminhamento nesta situação, retorna null. Usado para saber se deve inserir um novo registro
	 * na tabela proc_encaminhamento ou apenas complementar o retorno de um registro existente.
	 * 
	 * @param idProc
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 * @since 13/08/2017
	 */
	public String consultarEncaminhamentoSemDevolucao(String idProc) throws Exception {
		String idProcEncaminhamento = null ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
						
			stSql = "SELECT ID_PROC_ENCAMINHAMENTO FROM PROJUDI.PROC_ENCAMINHAMENTO PE ";
			stSql += "WHERE PE.ID_PROC = ? "; ps.adicionarLong(idProc);
			stSql += "AND PE.DATA_RETORNO IS NULL ";
			stSql += "AND PE.ID_USU_SERV_RETORNO IS NULL ";

			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				idProcEncaminhamento =  rs1.getString("ID_PROC_ENCAMINHAMENTO") ;
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return idProcEncaminhamento;
	}
	
	/**
	 * Recebe o id do proc_encaminhamento e retorna o dt referente. Sobrescrevendo método da classe genérica
	 * pois o mesmo consulta em uma view que não está sendo preenchida. Voltar a utilizar o método da classe genérica
	 * quando o preenchimento da view for solucionado.
	 * 
	 * @author hrrosa
	 * @since 13/08/2017
	 */
	public ProcessoEncaminhamentoDt consultarId(String id_procencaminhamento )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoEncaminhamentoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.proc_encaminhamento WHERE ID_PROC_ENCAMINHAMENTO = ?";		ps.adicionarLong(id_procencaminhamento); 

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
	
	/**
	 * Sobrescrevendo o método associarDt provisoriamente para atender ao método acima. O método associarDt da classe genérica
	 * utiliza campos que não estão na tabela proc_encaminhamento. 
	 * 
	 * @author hrrosa
	 * @since 13/08/2017
	 */
	protected void associarDt( ProcessoEncaminhamentoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_ENCAMINHAMENTO"));
		Dados.setId_Proc( rs.getString("ID_PROC"));
		Dados.setId_ServOrigem( rs.getString("ID_SERV_ORIGEM"));
		Dados.setId_ServEncaminhamento( rs.getString("ID_SERV_ENCAMINHAMENTO"));
		Dados.setId_UsuServEncaminhamento( rs.getString("ID_USU_SERV_ENCAMINHAMENTO"));
		Dados.setId_UsuServRetorno( rs.getString("ID_USU_SERV_RETORNO"));
		Dados.setDataEncaminhamento( Funcoes.FormatarDataHora(rs.getString("DATA_ENCAMINHAMENTO")));
		Dados.setDataRetorno( Funcoes.FormatarDataHora(rs.getString("DATA_RETORNO")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}
	
	public boolean podeEncaminhar(String id_proc) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		boolean boRetorno = true;

		stSql= "SELECT * FROM PROJUDI.proc_encaminhamento WHERE ID_PROC = ?";		ps.adicionarLong(id_proc);
		stSql += " and DATA_RETORNO is null ";

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				boRetorno = false;				
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		
		return boRetorno;
	}
	
	public boolean podeEncaminharOrigemDestino(String id_proc, String id_serv_origem, String id_serv_encaminhamento) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		boolean boRetorno = false;

		stSql= "SELECT * FROM PROJUDI.proc_encaminhamento WHERE ID_PROC = ?";		ps.adicionarLong(id_proc);
		stSql += " and DATA_RETORNO is null ";
		stSql += " and ID_SERV_ORIGEM = ? "; 				ps.adicionarLong(id_serv_encaminhamento);
		stSql += " and ID_SERV_ENCAMINHAMENTO = ?  "; 		ps.adicionarLong(id_serv_origem);
		
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				boRetorno = true;				
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		
		return boRetorno;
	}
	
	/**
	 * Se houver algum encaminhamento em aberto para o idProc e idServEncaminhamento, retorna o id da serventia que encaminhou o processo.
	 * Se não houver nenhum encaminhamento em aberto para o idProc, retorna null. Utilizado para descobrir a serventia para a qual
	 * o processo deve ser devolvido.
	 * 
	 * @param idProc
	 * @param idServEncaminhamento
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 * @since 13/08/2017
	 */
	public String retornaServentiaDevolucaoEncaminhamento(String idProc, String idServEncaminhamento) throws Exception {
		String idServOrigem = null ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
						
			stSql = "SELECT ID_SERV_ORIGEM FROM PROJUDI.PROC_ENCAMINHAMENTO PE ";
			stSql += "WHERE PE.ID_PROC = ? "; ps.adicionarLong(idProc);
			stSql += "AND PE.DATA_RETORNO IS NULL ";
			stSql += "AND PE.ID_USU_SERV_RETORNO IS NULL ";
			if(idServEncaminhamento != null) {
				stSql += "AND PE.ID_SERV_ENCAMINHAMENTO = ? "; ps.adicionarLong(idServEncaminhamento);
			}

			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				idServOrigem =  rs1.getString("ID_SERV_ORIGEM") ;
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return idServOrigem;
	}
	
	/**
	 * Retorna o idServCargo do último magistrado responsável pelo processo na serventia especificada.
	 * Consulta a informação no ponteiro de redistribuição. 
	 * @param idProc
	 * @param idServ
	 * @return
	 * @throws Exception
	 */
	public String retornaUltimoResponsavelProcessoServentia(String idProc, String idServ) throws Exception {
		String idServCargo = null;
		String sql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try {
		
			sql += "select pl.id_serv_cargo from proc_encaminhamento pe ";
			sql += " inner join ponteiro_log pl on pl.id_proc=pe.id_proc "; 
		    sql += " where  pe.ID_PROC = ? "; 				ps.adicionarLong(idProc);
		    sql += " and pl.id_serv = ? "; 					ps.adicionarLong(idServ);
		    sql += " and pl.ID_PONTEIRO_LOG_TIPO = ? "; 	ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
		    sql += " order by id_ponteiro_log desc ";
		    sql += " OFFSET 0 ROWS FETCH NEXT  1 ROWS ONLY ";
		    rs1 = consultar(sql, ps);
			
			if (rs1.next()) {
				idServCargo =  rs1.getString("id_serv_cargo");
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		
		return idServCargo;
	}
	
}
