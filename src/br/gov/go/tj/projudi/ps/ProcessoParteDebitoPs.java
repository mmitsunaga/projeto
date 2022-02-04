package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoParteDebitoPs extends ProcessoParteDebitoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -2623120753595960442L;

    public ProcessoParteDebitoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta os débitos cadastrados baseado nos parâmetros passados
	 * @param nomeParte, filtro para nome da parte
	 * @param cpfParte, filtro para cpf da parte
	 * @param cnpjParte, filtro para cnpj da parte
	 * @param processoNumero, filtro para número do processo
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author msapaula, hmgodinho
	 */
	public List consultarDebitosProcessoParte(String nomeParte, String cpfParte, String cnpjParte, String processoNumero, String posicao) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT * ";
		sqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_DEBITO b ";
		sqlFrom += " WHERE b.NOME LIKE ? ";
		ps.adicionarString( nomeParte +"%");
		
		if (cpfParte != null && cpfParte.length() > 0) {
			sqlFrom += " AND b.CPF = ?";
			ps.adicionarLong(cpfParte);
		}
		if (cnpjParte != null && cnpjParte.length() > 0) {
			sqlFrom += " AND b.CNPJ = ? ";
			ps.adicionarLong(cnpjParte);
		}
		if(processoNumero != null && processoNumero.length() > 0){
			sqlFrom += " AND b.PROC_NUMERO LIKE ? ";
			ps.adicionarString(processoNumero);
		}
		
		sqlOrder = " ORDER BY PROC_NUMERO ";
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			while (rs1.next()) {
				ProcessoParteDebitoDt obTemp = new ProcessoParteDebitoDt();
				this.associarDt(obTemp, rs1);
				obTemp.setId_Processo(rs1.getString("ID_PROC"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			if (rs2.next())	liTemp.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta os débitos cadastrados baseado nos parâmetros passados usando JSON
	 * @param nomeSimplifcadoParte, filtro para nome simplificado da parte
	 * @param cpfParte, filtro para cpf da parte
	 * @param cnpjParte, filtro para cnpj da parte
	 * @param processoNumero, filtro para número do processo
	 * @param digitoVerificador, filtro para o digito verificador do processo
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author hmgodinho
	 * @author fasoares (Adicionei os parametros 6 e 7 tipoDebito e status)
	 */
	public String consultarDebitosProcessoParteJSON(String nomeSimplificadoParte, String cpfParte, String cnpjParte, String processoNumero, String digitoVerificador, String numeroGuia, String serventia, String tipoDebito, String status, String posicao) throws Exception {

		String sql = ""; 
		String sqlFrom = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 6;

		sql = "SELECT b.ID_PROC_PARTE_DEBITO AS ID, b.PROC_NUMERO_COMPLETO AS DESCRICAO1, b.PROC_DEBITO AS DESCRICAO2, b.PROC_DEBITO_STATUS AS DESCRICAO3, b.NOME AS DESCRICAO4, b.SERV AS DESCRICAO5, b.NUMERO_GUIA_COMPLETO AS DESCRICAO6 ";
		sqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_DEBITO b ";
		sqlFrom += " WHERE"; 
		
		if(nomeSimplificadoParte != null && nomeSimplificadoParte.length() > 0) {
			sqlFrom += " AND b.NOME_SIMPLIFICADO = '" +nomeSimplificadoParte +"'";
		}
		if (cpfParte != null && cpfParte.length() > 0) {			
			sqlFrom += " AND b.CPF = ?";
			ps.adicionarLong(cpfParte);
		}
		if (cnpjParte != null && cnpjParte.length() > 0) {			
			sqlFrom += " AND b.CNPJ = ? ";
			ps.adicionarLong(cnpjParte);
		}
		if(processoNumero != null && processoNumero.length() > 0){			
			sqlFrom += " AND b.PROCESSO_NUMERO = ?";
			ps.adicionarString(processoNumero);
		}
		if(digitoVerificador != null && digitoVerificador.length() > 0){			
			sqlFrom += " AND b.DIGITO_VERIFICADOR = ?";
			ps.adicionarString(digitoVerificador);
		}	
		if (numeroGuia != null && numeroGuia.length() > 0) {			
			sqlFrom += " AND b.NUMERO_GUIA_COMPLETO = ? ";
			ps.adicionarLong(numeroGuia);
		}
		if (serventia != null && serventia.length() > 0) {
			sqlFrom += " AND b.SERV LIKE ?";
			ps.adicionarString("%" + serventia + "%");
		}
		if (tipoDebito != null && tipoDebito.length() > 0) {
			sqlFrom += " AND b.PROC_DEBITO LIKE ?";
			ps.adicionarString("%" + tipoDebito + "%");
		}
		if (status != null && status.length() > 0) {
			sqlFrom += " AND b.PROC_DEBITO_STATUS LIKE ?";
			ps.adicionarString("%" + status + "%");
		}
		
		sqlFrom += " ORDER BY b.PROC_NUMERO ";
		
		sqlFrom = sqlFrom.replace("WHERE AND", "WHERE");
		sqlFrom = sqlFrom.replace("WHERE ORDER", "ORDER");
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	private void associarDtCompleto(ProcessoParteDebitoDt processoParteDebitoDt, ResultSetTJGO rs1)  throws Exception {
		associarDt(processoParteDebitoDt, rs1);
		if (rs1.getString("CPF") != null && rs1.getString("CPF").trim().length() > 0) {
			processoParteDebitoDt.setCpfParte(rs1.getString("CPF"));	
		}
		if (rs1.getString("CNPJ") != null && rs1.getString("CNPJ").trim().length() > 0) {
			processoParteDebitoDt.setCpfParte(rs1.getString("CNPJ"));	
		}
		processoParteDebitoDt.setId_Processo(rs1.getString("ID_PROC"));
		processoParteDebitoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
		processoParteDebitoDt.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO_COMPLETO"));
		processoParteDebitoDt.setId_Serventia(rs1.getString("ID_SERV"));
		processoParteDebitoDt.setServentia(rs1.getString("SERV"));
		if (rs1.getString("ID_GUIA_EMIS") != null) {
			processoParteDebitoDt.setId_GuiaEmissao(rs1.getString("ID_GUIA_EMIS"));				
			processoParteDebitoDt.setNumeroGuiaEmissao(rs1.getString("NUMERO_GUIA_COMPLETO"));
			processoParteDebitoDt.setId_GuiaTipo(rs1.getString("ID_GUIA_TIPO"));
			processoParteDebitoDt.setGuiaTipo(rs1.getString("GUIA_TIPO"));
			processoParteDebitoDt.setId_GuiaStatus(rs1.getString("ID_GUIA_STATUS"));
			processoParteDebitoDt.setGuiaStatus(rs1.getString("GUIA_STATUS"));
			processoParteDebitoDt.setValorTotalGuia(rs1.getString("VALOR_TOTAL_GUIA"));
			processoParteDebitoDt.setDataVencimentoGuiaEmissao(rs1.getString("DATA_VENCIMENTO_GUIA"));
		}
		processoParteDebitoDt.setEnderecoPartePROAD(rs1.getString("ENDERECO_PARTE_PROAD"));
		processoParteDebitoDt.setDataDebitoPROAD(rs1.getString("DATA_VENCIMENTO_PROAD"));
		processoParteDebitoDt.setValorCreditoPROAD(rs1.getString("VALOR_CREDITO_PROAD"));		
	}

	/**
	 * Sobrescrevendo consulta para retornar dados do processo
	 */
	public ProcessoParteDebitoDt consultarId(String id_ProcessoParteDebito) throws Exception {
		String sql;
		ProcessoParteDebitoDt processoParteDebitoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE ID_PROC_PARTE_DEBITO = ?";
		 ps.adicionarLong(id_ProcessoParteDebito);

		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				processoParteDebitoDt = new ProcessoParteDebitoDt();
				associarDtCompleto(processoParteDebitoDt, rs1);
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return processoParteDebitoDt;
	}
	
	public List<ProcessoParteDebitoDt> consultarPartesComDebitos(String id_Processo ) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		List<ProcessoParteDebitoDt> liTemp = new ArrayList<ProcessoParteDebitoDt>();
		ResultSetTJGO rs1 = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		stSql+= " ORDER BY NOME ";		
		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				ProcessoParteDebitoDt processoParteDebitoDt = new ProcessoParteDebitoDt();
				associarDtCompleto(processoParteDebitoDt, rs1);
				liTemp.add(processoParteDebitoDt);
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	public ProcessoParteDebitoDt consultarProcessoNumeroProad(String numeroProcessoProad) throws Exception {
		String sql;
		ProcessoParteDebitoDt processoParteDebitoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE NUMERO_PROAD = ?";
		 ps.adicionarLong(numeroProcessoProad);

		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				processoParteDebitoDt = new ProcessoParteDebitoDt();
				associarDtCompleto(processoParteDebitoDt, rs1);
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return processoParteDebitoDt;
	}
	
	public List<ProcessoParteDebitoDt> consultarListaNumeroProcessoCompleto(String numeroProcessoCompleto) throws Exception {
		String sql;
		List<ProcessoParteDebitoDt> listaProcessoParteDebitoDt = new ArrayList<ProcessoParteDebitoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE PROC_NUMERO_COMPLETO = ?";
		 ps.adicionarString(numeroProcessoCompleto);

		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				ProcessoParteDebitoDt processoParteDebitoDt = new ProcessoParteDebitoDt();
				associarDtCompleto(processoParteDebitoDt, rs1);
				listaProcessoParteDebitoDt.add(processoParteDebitoDt);
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaProcessoParteDebitoDt;
	}
	
	public List<ProcessoParteDebitoDt> consultarListaIdProcesso(String idProcesso) throws Exception {
		String sql;
		List<ProcessoParteDebitoDt> listaProcessoParteDebitoDt = new ArrayList<ProcessoParteDebitoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE ID_PROC = ?";
		 ps.adicionarLong(idProcesso);

		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				ProcessoParteDebitoDt processoParteDebitoDt = new ProcessoParteDebitoDt();
				associarDtCompleto(processoParteDebitoDt, rs1);
				listaProcessoParteDebitoDt.add(processoParteDebitoDt);
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaProcessoParteDebitoDt;
	}
	
	public List<ProcessoParteDebitoDt> consultarListaLiberadoCadinAindaNaoEnvido() throws Exception {
		String sql;
		List<ProcessoParteDebitoDt> listaProcessoParteDebitoDt = new ArrayList<ProcessoParteDebitoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO PPD ";
		sql += " WHERE ID_PROC_DEBITO_STATUS = ? "; ps.adicionarLong(ProcessoDebitoStatusDt.LIBERADO_PARA_ENVIO_CADIN);
		sql += " AND NOT EXISTS (SELECT 1 ";
		sql += "                   FROM PROJUDI.PROC_PARTE_DEBITO_CADIN PPDC ";
		sql += "                  WHERE PPD.ID_PROC_PARTE_DEBITO = PPDC.ID_PROC_PARTE_DEBITO)";
		
		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				ProcessoParteDebitoDt processoParteDebitoDt = new ProcessoParteDebitoDt();
				associarDtCompleto(processoParteDebitoDt, rs1);
				listaProcessoParteDebitoDt.add(processoParteDebitoDt);
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaProcessoParteDebitoDt;
	}
	
	/**
	 * Método para consulta processo-parte-debito pelo id-guia-emis.
	 * 
	 * @param String idGuiaEmissao
	 * @return ProcessoParteDebitoDt
	 * @throws Exception
	 * @author fasoares
	 */
	public ProcessoParteDebitoDt consultarProcessoParteDebitoIdGuiaEmissao(String idGuiaEmissao) throws Exception {
		String sql;
		ProcessoParteDebitoDt processoParteDebitoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO ";
		sql += " WHERE ID_GUIA_EMIS = ? "; ps.adicionarLong(idGuiaEmissao);
		
		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				processoParteDebitoDt = new ProcessoParteDebitoDt();
				associarDtCompleto(processoParteDebitoDt, rs1);
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return processoParteDebitoDt;
	}
}
