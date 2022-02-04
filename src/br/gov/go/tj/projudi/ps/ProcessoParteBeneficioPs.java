package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoParteBeneficioPs extends ProcessoParteBeneficioPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 5523273944958060013L;

    public ProcessoParteBeneficioPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta os benefícios cadastrados baseado nos parâmetros passados, onde a data final do benefício
	 * seja maior ou igual a data atual
	 * 
	 * @param nomeParte, filtro para nome da parte
	 * @param cpfParte, filtro para cpf da parte
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarBeneficiosProcessoParte(String nomeParte, String cpfParte, String posicao) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT *";
		sqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO b";
		sqlFrom += " WHERE b.DATA_FINAL >= ?";
		ps.adicionarDateTime(new Date());		
		if (nomeParte != null && nomeParte.length() > 0){
			sqlFrom += " AND b.NOME LIKE ?";					ps.adicionarString( nomeParte +"%");			
		}
		if (cpfParte != null && cpfParte.length() > 0){
			sqlFrom += " AND b.CPF = ?";
			ps.adicionarString(cpfParte);
		}
		sqlOrder = " ORDER BY DATA_FINAL";		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			while (rs1.next()) {
				ProcessoParteBeneficioDt obTemp = new ProcessoParteBeneficioDt();
				this.associarDt(obTemp, rs1);
				obTemp.setId_Processo(rs1.getString("ID_PROC"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Método para fazer consulta de benefícios usando JSON
	 * @param nomeParte - nome da parte a ser consultada
	 * @param cpfParte - cpf da parte
	 * @param posicaoPaginaAtual
	 * @return lista de benefícios
	 * @throws Exception
	 */
	public String consultarBeneficiosProcessoParteJSON(String nomeParte, String cpfParte, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 5;
		
		stSql = "SELECT b.ID_PROC_PARTE_BENEFICIO AS ID, b.PROC_NUMERO AS DESCRICAO1, b.PROC_BENEFICIO AS DESCRICAO2, b.NOME AS DESCRICAO3, "
				+ "to_char(b.DATA_INICIAL, 'dd/mm/yyyy') AS DESCRICAO4, to_char(b.DATA_FINAL,'dd/mm/yyyy') AS DESCRICAO5 ";
		stSqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO b ";
		stSqlFrom += " WHERE b.DATA_FINAL >= ? ";
		ps.adicionarDateTime(new Date());
		
		if (nomeParte != null && nomeParte.length() > 0){
			stSqlFrom += " AND b.NOME LIKE ? ";					ps.adicionarString( nomeParte +"%");			
		}
		if (cpfParte != null && cpfParte.length() > 0){
			stSqlFrom += " AND b.CPF = ? ";
			ps.adicionarString(cpfParte);
		}
		
		stSqlOrder = " ORDER BY DATA_FINAL ";

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE ";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
			try{if (rs2 != null)rs2.close();} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
	/**
	 * Consulta os benefícios cadastrados baseado nos parâmetros passado
	 * 
	 * @param idProcesso, identificador do processo
	 * 
	 * @author lsbernardes
	 */
	public List consultarPartesComBeneficio(String idProcesso) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT *";
		sqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO b";
		sqlFrom += " WHERE b.ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
//		sqlOrder = " ORDER BY b.NOME";
		sqlOrder = " ORDER BY b.DATA_INICIAL";
		
		try{
			rs1 = consultar(sql + sqlFrom + sqlOrder, ps);
			while (rs1.next()) {
				ProcessoParteBeneficioDt obTemp = new ProcessoParteBeneficioDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_BENEFICIO"));
				obTemp.setProcessoBeneficio(rs1.getString("PROC_BENEFICIO"));
				obTemp.setId_Processo(rs1.getString("ID_PROC"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setCpfParte(rs1.getString("CPF"));
				obTemp.setDataInicial(Funcoes.FormatarData(rs1.getDateTime("DATA_INICIAL")));
				liTemp.add(obTemp);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta os benefícios cadastrados baseado nos parâmetros passado usando JSON
	 * 
	 * @param idProcesso, identificador do processo
	 * 
	 * @author kbsriccioppo
	 */
	public String consultarPartesComBeneficioJSON(String idProcesso, String posicao) throws Exception {
		
		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 5;
		stSql = "SELECT b.ID_PROC_PARTE_BENEFICIO AS ID, b.PROC_NUMERO AS DESCRICAO1, b.PROC_BENEFICIO AS DESCRICAO2, b.NOME AS DESCRICAO3, "
				+ "to_char(b.DATA_INICIAL, 'dd/mm/yyyy') AS DESCRICAO4, to_char(b.DATA_FINAL,'dd/mm/yyyy') AS DESCRICAO5, b.CPF AS DESCRICAO6 ";
		stSqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO b";
		stSqlFrom += " WHERE b.ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
		stSqlOrder = " ORDER BY b.DATA_INICIAL";
		
		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE ";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
			try{if (rs2 != null)rs2.close();} catch(Exception e) {
			}
		}
		return stTemp;
	}

	/**
	 * Sobrescrevendo consulta para retornar dados do processo
	 */
	public ProcessoParteBeneficioDt consultarId(String id_ProcessoParteBeneficio) throws Exception {
		String sql;
		ProcessoParteBeneficioDt processoParteBeneficioDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO WHERE ID_PROC_PARTE_BENEFICIO = ?";
		ps.adicionarLong(id_ProcessoParteBeneficio);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				processoParteBeneficioDt = new ProcessoParteBeneficioDt();
				associarDt(processoParteBeneficioDt, rs1);
				processoParteBeneficioDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoParteBeneficioDt.setId_Serventia(rs1.getString("ID_SERV"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return processoParteBeneficioDt;
	}
	
	/**
	 * Consulta os benefícios cadastrados e ativos de uma parte.
	 * 
	 * @param cpfParte
	 * @return
	 * @throws Exception
	 */
	public List consultarBeneficiosProcessoParte(String cpfParte) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO b";
		sql += " WHERE b.DATA_FINAL >= ?";
		ps.adicionarDateTime(new Date());		
		if (cpfParte != null && cpfParte.length() > 0){
			sql += " AND b.CPF = ?";
			ps.adicionarString(cpfParte);
		}
		sql += " ORDER BY DATA_FINAL";		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ProcessoParteBeneficioDt obTemp = new ProcessoParteBeneficioDt();
				this.associarDt(obTemp, rs1);
				obTemp.setId_Processo(rs1.getString("ID_PROC"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp;
	}
	
	public List consultarPartesComBeneficios(String id_Processo) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO WHERE ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		stSql+= " ORDER BY DATA_INICIAL ";		
		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				ProcessoParteBeneficioDt processoParteBeneficioDt = new ProcessoParteBeneficioDt();
				associarDtCompleto(processoParteBeneficioDt, rs1);
				liTemp.add(processoParteBeneficioDt);
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	private void associarDtCompleto(ProcessoParteBeneficioDt processoParteBeneficioDt, ResultSetTJGO rs1)  throws Exception {
		
		if (rs1.getString("CODIGO_TEMP") != null && rs1.getString("CODIGO_TEMP").trim().length() > 0) {
			processoParteBeneficioDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));	
		}
		if (rs1.getString("CPF") != null && rs1.getString("CPF").trim().length() > 0) {
			processoParteBeneficioDt.setCpfParte(rs1.getString("CPF"));	
		}
		if (rs1.getString("DATA_FINAL") != null && rs1.getString("DATA_FINAL").trim().length() > 0) {
			processoParteBeneficioDt.setDataFinal(Funcoes.FormatarData(rs1.getString("DATA_FINAL")));	
		}
		if (rs1.getString("DATA_INICIAL") != null && rs1.getString("DATA_INICIAL").trim().length() > 0) {
			processoParteBeneficioDt.setDataInicial(Funcoes.FormatarData(rs1.getString("DATA_INICIAL")));	
		}
		if (rs1.getString("ID_PROC") != null && rs1.getString("ID_PROC").trim().length() > 0) {
			processoParteBeneficioDt.setId_Processo(rs1.getString("ID_PROC"));	
		}
		if (rs1.getString("ID_PROC_BENEFICIO") != null && rs1.getString("ID_PROC_BENEFICIO").trim().length() > 0) {
			processoParteBeneficioDt.setId_ProcessoBeneficio(rs1.getString("ID_PROC_BENEFICIO"));	
		}
		if (rs1.getString("ID_PROC_PARTE") != null && rs1.getString("ID_PROC_PARTE").trim().length() > 0) {
			processoParteBeneficioDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));	
		}
		if (rs1.getString("ID_PROC_PARTE_BENEFICIO") != null && rs1.getString("ID_PROC_PARTE_BENEFICIO").trim().length() > 0) {
			processoParteBeneficioDt.setId(rs1.getString("ID_PROC_PARTE_BENEFICIO"));	
		}
		if (rs1.getString("ID_SERV") != null && rs1.getString("ID_SERV").trim().length() > 0) {
			processoParteBeneficioDt.setId_Serventia(rs1.getString("ID_SERV"));	
		}		
		if (rs1.getString("NOME") != null && rs1.getString("NOME").trim().length() > 0) {
			processoParteBeneficioDt.setNome(rs1.getString("NOME"));	
		}
		if (rs1.getString("PROC_BENEFICIO") != null && rs1.getString("PROC_BENEFICIO").trim().length() > 0) {
			processoParteBeneficioDt.setProcessoBeneficio(rs1.getString("PROC_BENEFICIO"));	
		}
		if (rs1.getString("PROC_NUMERO") != null && rs1.getString("PROC_NUMERO").trim().length() > 0) {
			processoParteBeneficioDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));	
		}
	}

	public String consultarBeneficiosProcessoParteJSON(String id_processo_parte) throws Exception {
		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT b2.* ";
				 
		stSqlFrom= "from (select pp.id_PROC_PARTE ,pp.cpf, pp.nome_simplificado,pp.DATA_NASCIMENTO, pp.NOME_MAE, pp.RG,pp.ID_RG_ORGAO_EXP, pp.cnpj "; 
		stSqlFrom+= "    from  proc_parte pp";
		stSqlFrom+= "		            where pp.id_PROC_PARTE=? "; 																ps.adicionarLong(id_processo_parte);
		stSqlFrom+= "		    ) tab  inner join  proc_parte pp1 "; 
		stSqlFrom+= "		                    on ( (tab.id_proc_parte =  pp1.id_proc_parte) or ";
		stSqlFrom+= "		                        (tab.cpf=pp1.cpf) or ";
		stSqlFrom+= "		                         (tab.nome_simplificado = pp1.nome_simplificado and tab.DATA_NASCIMENTO= pp1.DATA_NASCIMENTO and tab.NOME_MAE=pp1.NOME_MAE) or "; 
		stSqlFrom+= "		                         (tab.RG = pp1.RG and tab.ID_RG_ORGAO_EXP=pp1.ID_RG_ORGAO_EXP) or ";
		stSqlFrom+= "		                         (tab.cnpj = pp1.cnpj) ";
		stSqlFrom+= "		                        ) ";
		stSqlFrom+= "		                inner join VIEW_PROC_PARTE_BENEFICIO b2 on pp1.id_proc_parte = b2.id_proc_parte ";																
		  						
		stSqlOrder= " ORDER BY b2.ID_PROC_PARTE_BENEFICIO ";
		try{


			rs1 = consultar(stSql+stSqlFrom+stSqlOrder, ps );
			stTemp="[";
			if (rs1.next()){	
				stTemp+=atribuirProcessoParteBeneficio( rs1);											
			}
			//if e while para não precisar testar toda hora se posso ou nao colocar a virgula
			while (rs1.next()){											
				stTemp+="," + atribuirProcessoParteBeneficio( rs1);					
			}
			stTemp+="]";							

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return stTemp; 
	}

	private String atribuirProcessoParteBeneficio(ResultSetTJGO rs1) throws Exception {
		ProcessoParteBeneficioDt dt = new ProcessoParteBeneficioDt();
		dt.setId(rs1.getString("ID_PROC_PARTE_BENEFICIO"));
		dt.setId_ProcessoBeneficio(rs1.getString("ID_PROC_BENEFICIO"));
		dt.setProcessoBeneficio(rs1.getString(Funcoes.limparStringToJSON("PROC_BENEFICIO")));
		dt.setNome(rs1.getString(Funcoes.limparStringToJSON("NOME")));
		dt.setDataInicial(Funcoes.FormatarData(rs1.getString("DATA_INICIAL")));
		dt.setDataFinal(Funcoes.FormatarData(rs1.getString("DATA_FINAL")));
		dt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
		dt.setId_Serventia(rs1.getString("ID_SERV"));
		dt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
		dt.setId_Processo(rs1.getString("ID_PROC"));
		dt.setCpfParte(rs1.getString("CPF"));						
		
		return dt.toJson();
	}

}
