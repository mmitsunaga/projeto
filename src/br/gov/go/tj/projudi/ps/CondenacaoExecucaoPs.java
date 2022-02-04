package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoSituacaoDt;

import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class CondenacaoExecucaoPs extends CondenacaoExecucaoPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -6277952933987093129L;

    public CondenacaoExecucaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * sobrescreve o método do gerador, pois não altera o Id_ProcessoExecucao
	 * @author wcsilva
	 */
	public void alterar(CondenacaoExecucaoDt dados) throws Exception{

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.CONDENACAO_EXE SET  ";
			
		Sql+= "TEMPO_PENA  = ? ";
		ps.adicionarLong(dados.getTempoPena()); 
			
		if (dados.getReincidente().length() > 0){
			Sql+= ",REINCIDENTE  = ? ";
			ps.adicionarBoolean(dados.getReincidente()); 
		}
		if (dados.getDataFato().length() > 0){
			Sql+= ",DATA_FATO = ? ";
			ps.adicionarDate(dados.getDataFato()); 
		}
//		Sql+= ",ID_PROC_EXE  = " + Funcoes.BancoInteiro( dados.getId_ProcessoExecucao()); 
		if (dados.getId_CrimeExecucao().length() > 0){
			Sql+= ",ID_CRIME_EXE  = ? ";
			ps.adicionarLong(dados.getId_CrimeExecucao()); 
		}
		if (dados.getId_CondenacaoExecucaoSituacao().length() > 0){
			Sql+= ",ID_CONDENACAO_EXE_SIT  = ? ";
			ps.adicionarLong(dados.getId_CondenacaoExecucaoSituacao());
		}
		Sql+= ",TEMPO_CUMPRIDO_EXTINTO  = ? ";
		ps.adicionarLong(dados.getTempoCumpridoExtintoDias());
		
		Sql += ", OBSERVACAO = ? ";
		ps.adicionarString(dados.getObservacao());
		
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_CONDENACAO_EXE  = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(Sql,ps);	} 
	
	/**
	 * Lista as condenacoes de um Trânsito em Julgado
	 * @param id_ProcessoExecucao: identificação do ProcessoExecução
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 */
	public List listarCondenacaoDaAcaoPenal(String id_ProcessoExecucao) throws Exception{
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_CONDENACAO_EXE ce";
		sql += " INNER JOIN PROJUDI.VIEW_CRIME_EXE cre on ce.ID_CRIME_EXE = cre.ID_CRIME_EXE";
		sql += " WHERE ID_PROC_EXE = ? ";
		
		ps.adicionarLong(id_ProcessoExecucao);
		
		try{
			 rs1 = consultar(sql, ps);

			while (rs1.next()){
				CondenacaoExecucaoDt condenacao = new CondenacaoExecucaoDt();
				associarDt(condenacao, rs1);
				lista.add(condenacao);
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		
		return lista;
	}

	public String listarCondenacaoDaAcaoPenalJSON(String id_ProcessoExecucao, String posicao) throws Exception{
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 8;
		
		String sql = "SELECT ce.ID_CONDENACAO_EXE AS ID, ce.LEI AS DESCRICAO1, ce.ARTIGO AS DESCRICAO2,";
		sql += " ce.PARAGRAFO AS DESCRICAO3, ce.INCISO AS DESCRICAO4, ce.TEMPO_PENA AS DESCRICAO5,";
		sql += " to_char(ce.DATA_FATO, 'DD/MM/YYYY') AS DESCRICAO6, ce.REINCIDENTE AS DESCRICAO7,";
		sql += " ce.CONDENACAO_EXE_SIT AS DESCRICAO8 FROM PROJUDI.VIEW_CONDENACAO_EXE ce";
		sql += " INNER JOIN PROJUDI.VIEW_CRIME_EXE cre on ce.ID_CRIME_EXE = cre.ID_CRIME_EXE";
		sql += " WHERE ID_PROC_EXE = ? ";
		
		ps.adicionarLong(id_ProcessoExecucao);
		
		try{
			 rs1 = consultar(sql, ps);

			sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_CONDENACAO_EXE ce INNER JOIN";
            sql += " PROJUDI.VIEW_CRIME_EXE cre on ce.ID_CRIME_EXE = cre.ID_CRIME_EXE WHERE";
			sql += " ID_PROC_EXE = ? ";
			rs2 = consultar(sql, ps);
			rs2.next();			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		
		return stTemp;
	}

	/**
	 * Lista as condenacoes NÃO EXTINTAS de um Trânsito em Julgado
	 * @param id_ProcessoExecucao: identificação do ProcessoExecução
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 */
	public List listarCondenacaoNaoExtintaDaAcaoPenal(String id_ProcessoExecucao) throws Exception{
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		StringBuffer sql = new StringBuffer();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		sql.append("SELECT * FROM PROJUDI.VIEW_CONDENACAO_EXE ce");
		sql.append(" INNER JOIN PROJUDI.VIEW_CRIME_EXE cre ON ce.ID_CRIME_EXE = cre.ID_CRIME_EXE");
		sql.append(" WHERE ID_PROC_EXE = ? ");
		ps.adicionarLong(id_ProcessoExecucao);
		sql.append(" AND ce.ID_CONDENACAO_EXE_SIT = ? ");
		ps.adicionarLong(CondenacaoExecucaoSituacaoDt.NAO_APLICA); 
		sql.append(" ORDER BY ce.ID_PROC_EXE ");
		
		try{
			 rs1 = consultar(sql.toString(), ps);

			while (rs1.next()){
				CondenacaoExecucaoDt condenacao = new CondenacaoExecucaoDt();
				associarDt(condenacao, rs1);
				lista.add(condenacao);
			}
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return lista;
	}
	
	/**
	 * Lista as condenacoes não extintas de um Processo de Execução Penal
	 * @param id_Processo: identificação do Processo de Execução Penal
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 */
	public List listarCondenacaoNaoExtintaDoProcesso(String id_Processo) throws Exception{
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		StringBuffer sql = new StringBuffer();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT c.*, pce.* FROM PROJUDI.VIEW_PROC_EXE pe");
		sql.append(" INNER JOIN PROJUDI.VIEW_CONDENACAO_EXE c ON pe.ID_PROC_EXE = c.ID_PROC_EXE");
		sql.append(" LEFT JOIN PROJUDI.VIEW_PARAMETRO_CRIME_EXE pce on c.ID_CRIME_EXE = pce.ID_CRIME_EXE");
		sql.append(" WHERE (pe.ID_PROC_EXE_PENAL = ? OR pe.ID_PROC_DEPENDENTE = ?)");
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(id_Processo);
		sql.append(" AND c.ID_CONDENACAO_EXE_SIT = ? ");
		ps.adicionarLong(CondenacaoExecucaoSituacaoDt.NAO_APLICA);
		sql.append(" AND (pe.CODIGO_TEMP = ? OR pe.CODIGO_TEMP is NULL)");
		sql.append(" ORDER BY c.DATA_INICIO_CUMP_PENA ASC,c.DATA_TRANS_JULGADO ASC, pce.HEDIONDO_PROGRESSAO DESC, pce.HEDIONDO_LIVRAM_COND DESC, pce.EQUIPARA_HEDIONDO_LIVRAM_COND DESC, c.TEMPO_PENA DESC");
		ps.adicionarLong(1);
						
		try{
			rs = consultar(sql.toString(), ps);
			
			Map<String, CondenacaoExecucaoDt> condenacoes = new HashMap<String, CondenacaoExecucaoDt>();
			while (rs.next()){
				CondenacaoExecucaoDt condenacao = condenacoes.get(rs.getString("ID_CONDENACAO_EXE"));
				if(condenacao == null){ // verifica se repete o Id_CondenacaoExecucao
					condenacao = new CondenacaoExecucaoDt();
					associarDt(condenacao, rs);
					condenacoes.put(rs.getString("ID_CONDENACAO_EXE"), condenacao);
					lista.add(condenacao);
				}
				if (rs.getString("DATA") != null){
					if (!Funcoes.StringToDate(condenacao.getDataFato()).before(Funcoes.StringToDate(Funcoes.FormatarData(rs.getDateTime("DATA"))))){
						if (rs.getString("HEDIONDO_PROGRESSAO").equals("1")) condenacao.setHediondoProgressao(true);
						if (rs.getString("HEDIONDO_LIVRAM_COND").equals("1")) condenacao.setHediondoLivramento(true);
						if (rs.getString("EQUIPARA_HEDIONDO_LIVRAM_COND").equals("1")) condenacao.setEquiparaHediondoLivramento(true);
					}	
				}
			}
		} finally {
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        }  
		
		return lista;
	}
	
	/**
	 * Verifica se o crime da condenação é considerado hediondo para cálculo do livramento condicional e progressão de regime.
	 * Lista os parâmetros para o crime da condenação 
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 */
	public List setParametroCrime(List listaCondenacao) throws Exception{
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT * FROM PROJUDI.VIEW_PARAMETRO_CRIME_EXE p WHERE ");
			sql.append(" ? >= p.DATA");			
			sql.append(" AND p.ID_CRIME_EXE = ? ");			
			
			for (int i=0; i< listaCondenacao.size(); i++){
				ps.limpar();
				ps.adicionarDate(((CondenacaoExecucaoDt)listaCondenacao.get(i)).getDataFato());
				ps.adicionarLong(((CondenacaoExecucaoDt)listaCondenacao.get(i)).getId_CrimeExecucao());
				
				rs1 = consultar(sql.toString(), ps);
				
				while (rs1.next()){
					if (rs1.getString("HEDIONDO_PROGRESSAO").equals("1"))
						((CondenacaoExecucaoDt)listaCondenacao.get(i)).setHediondoProgressao(true);
					if (rs1.getString("HEDIONDO_LIVRAM_COND").equals("1"))
						((CondenacaoExecucaoDt)listaCondenacao.get(i)).setHediondoLivramento(true);
					if (rs1.getString("EQUIPARA_HEDIONDO_LIVRAM_COND").equals("1"))
						((CondenacaoExecucaoDt)listaCondenacao.get(i)).setEquiparaHediondoLivramento(true);
				}
			}
		} finally {
        	try{if(rs1 != null) rs1.close(); } catch(Exception e){}
        }  
		
		return listaCondenacao;
	}
	
	/**
	 * sobrescrevento o método do gerador, adicionado o parâmetro .setTempoPenaEmDias.
	 */
	protected void associarDt( CondenacaoExecucaoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		if (rs1.getString("ID_CONDENACAO_EXE") !=null) Dados.setId(rs1.getString("ID_CONDENACAO_EXE"));
		if (rs1.getString("TEMPO_PENA") !=null) Dados.setTempoPena( rs1.getString("TEMPO_PENA"));
		if (rs1.getString("TEMPO_PENA") !=null) Dados.setTempoPenaEmDias(rs1.getString("TEMPO_PENA"));
		if (rs1.getString("REINCIDENTE") !=null) Dados.setReincidente( Funcoes.FormatarLogico(rs1.getString("REINCIDENTE")));
		if (rs1.getString("DATA_FATO") !=null) Dados.setDataFato( Funcoes.FormatarData(rs1.getDateTime("DATA_FATO")));
		if (rs1.getString("DATA_TRANS_JULGADO") !=null) Dados.setDataTransitoJulgado(Funcoes.FormatarData(rs1.getDateTime("DATA_TRANS_JULGADO")));
		if (rs1.getString("DATA_INICIO_CUMP_PENA") !=null) Dados.setDataInicioCumprimentoPena( Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO_CUMP_PENA")));
		if (rs1.getString("ID_PROC_EXE") !=null) Dados.setId_ProcessoExecucao( rs1.getString("ID_PROC_EXE"));
		if (rs1.getString("PROC_NUMERO") !=null) Dados.setProcessoNumero( rs1.getString("PROC_NUMERO"));
		if (rs1.getString("ID_CRIME_EXE") !=null) Dados.setId_CrimeExecucao( rs1.getString("ID_CRIME_EXE"));
		if (rs1.getString("ID_CONDENACAO_EXE_SIT") !=null) Dados.setId_CondenacaoExecucaoSituacao( rs1.getString("ID_CONDENACAO_EXE_SIT"));
		if (rs1.getString("CONDENACAO_EXE_SIT") !=null) Dados.setCondenacaoExecucaoSituacao( rs1.getString("CONDENACAO_EXE_SIT"));
		if (rs1.getString("OBSERVACAO") !=null) Dados.setObservacao( rs1.getString("OBSERVACAO"));
		if (rs1.getString("TEMPO_CUMPRIDO_EXTINTO") !=null) Dados.setTempoCumpridoExtintoDias( rs1.getString("TEMPO_CUMPRIDO_EXTINTO"));
		//o crime contém: lei, artigo, parágrafo, inciso e crime
		String descricaoCrime = "";
		if ((rs1.getString("LEI")!=null) && (!rs1.getString("LEI").equals(""))) descricaoCrime += " Lei: " + rs1.getString("LEI") + " - ";
		if ((rs1.getString("ARTIGO") !=null) && (!rs1.getString("ARTIGO").equals(""))) descricaoCrime += " Art: " + rs1.getString("ARTIGO") + " - ";
		if ((rs1.getString("PARAGRAFO") !=null) && (!rs1.getString("PARAGRAFO").equals(""))) descricaoCrime += " Parág: " + rs1.getString("PARAGRAFO") + " - ";
		if ((rs1.getString("INCISO") !=null) && (!rs1.getString("INCISO").equals(""))) descricaoCrime += " Inc: " + rs1.getString("INCISO") + " - ";
		descricaoCrime += rs1.getString("CRIME_EXE");
		Dados.setCrimeExecucao(descricaoCrime);
//			if (rs1.getString("CODIGO_TEMP") !=null) Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		
	}
	
	/**
	 * Consulta o tempo total da condenação em dias, de um Trânsito em Julgado ou Guia de Recolhimento Provisória (apenas as condenações não extintas)
	 * @param id_ProcessoExecucao: filtro da consulta
	 * @return String: soma do tempo de condenação em dias.
	 * @throws Exception
	 */
	public String consultarTempoTotalCondenacaoDias(String id_ProcessoExecucao) throws Exception {
		String retorno = "";
		StringBuffer sql = new StringBuffer();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT SUM(TEMPO_PENA) AS SOMA FROM PROJUDI.VIEW_CONDENACAO_EXE c");
		sql.append(" WHERE c.ID_PROC_EXE = ? ");
		ps.adicionarLong(id_ProcessoExecucao);
		sql.append(" AND c.ID_CONDENACAO_EXE_SIT = ? ");
		ps.adicionarLong(CondenacaoExecucaoSituacaoDt.NAO_APLICA);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql.toString(), ps);
			rs1.next();
			retorno = rs1.getString("SOMA");
			if (retorno == null) retorno = "0";

		}finally{
			try{if(rs1 != null) rs1.close();}catch(Exception e){}
		}
		
		return retorno;
	}
	
	/**
	 * Lista os crimes das condenacoes de um Trânsito em Julgado (ação penal), INDEPENDENTE SE O idProcessoExecução É DE UMA AÇÃO PENAL ATIVA OU NÃO.
	 * @param idProcessoExecucao: identificação do Processo de Execução Penal
	 * @return listaCondenacao: lista com as condenações
	 * @author kbsriccioppo
	 */
	public List listarCrimeCondenacao(String idProcessoExecucao) throws Exception{
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		int i = 0;
		
		String sql = "SELECT ce.LEI, c.DATA_FATO, ce.ARTIGO, ce.PARAGRAFO, c.TEMPO_PENA, c.OBSERVACAO" +
				" FROM PROJUDI.VIEW_PROC_EXE pe INNER JOIN PROJUDI.VIEW_CONDENACAO_EXE c ON pe.ID_PROC_EXE = c.ID_PROC_EXE" +
				" INNER JOIN PROJUDI.VIEW_CRIME_EXE ce on ce.ID_CRIME_EXE = c.ID_CRIME_EXE" + 
				" WHERE pe.ID_PROC_EXE = ? ";
		ps.adicionarLong(idProcessoExecucao);
						
		try{
			rs1 = consultar(sql, ps);
			
			while (rs1.next()){
				String informacaoCondenacao = "";
				if ((rs1.getString("TEMPO_PENA")!=null) && (!rs1.getString("TEMPO_PENA").equals(""))) informacaoCondenacao += "Pena: " + Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(rs1.getString("TEMPO_PENA")));
				if ((rs1.getString("DATA_FATO")!=null) && (!rs1.getString("DATA_FATO").equals(""))) informacaoCondenacao += " Data Fato: " + Funcoes.FormatarData(rs1.getDateTime("DATA_FATO"));
				if ((rs1.getString("LEI")!=null) && (!rs1.getString("LEI").equals(""))) informacaoCondenacao += " Lei: " + rs1.getString("LEI");
				if ((rs1.getString("ARTIGO") !=null) && (!rs1.getString("ARTIGO").equals(""))) informacaoCondenacao += " Art: " + rs1.getString("ARTIGO");
				if ((rs1.getString("PARAGRAFO") !=null) && (!rs1.getString("PARAGRAFO").equals(""))) informacaoCondenacao += " Parag: " + rs1.getString("PARAGRAFO");
				if ((rs1.getString("OBSERVACAO") !=null) && (!rs1.getString("OBSERVACAO").equals(""))) informacaoCondenacao += " Obs.: " + rs1.getString("OBSERVACAO");
				lista.add(i, informacaoCondenacao);
				i++;
			}
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		
		return lista;
	}
	
	/**
	 * consulta os crimes ativos do processo de execução penal
	 * @param idProcesso: identificação do processo de execução penal
	 * @return String
	 * @throws Exception
	 */
	public String consultarCrimesAtivosProcesso(String idProcesso) throws Exception{
		String retorno = "";
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT c.LEI, c.ARTIGO, c.PARAGRAFO, c.INCISO FROM PROJUDI.VIEW_CONDENACAO_EXE c");
		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EXE pe ON c.ID_PROC_EXE = pe.ID_PROC_EXE");
		sql.append(" WHERE pe.ID_PROC_EXE_PENAL = ? OR pe.ID_PROC_DEPENDENTE = ? ");
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(idProcesso);
		sql.append(" AND (pe.CODIGO_TEMP = ? OR pe.CODIGO_TEMP is NULL)");
		ps.adicionarLong(1);

		try{
			rs = consultar(sql.toString(), ps);
			while(rs.next()){
				if (rs.getString("LEI") != null && rs.getString("LEI").length() > 0) retorno += " Lei: " + rs.getString("LEI");	
				if (rs.getString("ARTIGO") != null && rs.getString("ARTIGO").length() > 0) retorno += " Art: " + rs.getString("ARTIGO");
				if (rs.getString("PARAGRAFO") != null && rs.getString("PARAGRAFO").length() > 0) retorno += " Parag: " + rs.getString("PARAGRAFO");
				if (rs.getString("INCISO") != null && rs.getString("INCISO").length() > 0) retorno += " Inc: " + rs.getString("INCISO");
				retorno += " - ";
			}
			
		
		} finally{
			try{if(rs != null) rs.close();}catch(Exception e){}
		}
		
		return retorno;
	}
	
	public String consultarCondenacaoSituacao(String id_situacao )  throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		String descricaoSituacao = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_CONDENACAO_EXE_SIT WHERE ID_CONDENACAO_EXE_SIT = ? ";
		ps.adicionarLong(id_situacao);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				descricaoSituacao = rs1.getString("CONDENACAO_EXE_SIT");
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return descricaoSituacao; 
	}

}
