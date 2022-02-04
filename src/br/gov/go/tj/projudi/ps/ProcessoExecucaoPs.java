package br.gov.go.tj.projudi.ps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoSituacaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoExecucaoPs extends ProcessoExecucaoPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -7206074723258557142L;

    public ProcessoExecucaoPs(Connection conexao){
    	Conexao = conexao;
	}

    
	/**
	 * Sobrescrevendo o método do gerador, excluindo campos que estão somenta na visão
	 * @author wcsilva
	 */
	public void inserir(ProcessoExecucaoDt dados ) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//System.out.println("....psProcessoExecucaoinserir()");
		SqlCampos= "INSERT INTO PROJUDI.PROC_EXE ("; 
		SqlValores = " Values (";
		if ((dados.getId_ProcessoExecucaoPenal().length()>0)){
			SqlCampos+= "ID_PROC_EXE_PENAL " ;
			SqlValores+= "?";
			ps.adicionarLong(dados.getId_ProcessoExecucaoPenal());
		}
		if ((dados.getId_ProcessoAcaoPenal().length()>0)){
			SqlCampos+= ",ID_PROC_ACAO_PENAL " ;
			SqlValores+= ",?";
			ps.adicionarLong(dados.getId_ProcessoAcaoPenal());
		}
		if ((dados.getId_CidadeOrigem().length()>0)){
			SqlCampos+= ",ID_CIDADE_ORIGEM " ;
			SqlValores+= ",?";
			ps.adicionarLong(dados.getId_CidadeOrigem());
		}
		if ((dados.getDataAcordao().length()>0)){
			SqlCampos+= ",DATA_ACORDAO " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataAcordao());
		}
		if ((dados.getDataDistribuicao().length()>0)){
			SqlCampos+= ",DATA_DIST " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataDistribuicao());
		}
		if ((dados.getDataPronuncia().length()>0)){
			SqlCampos+= ",DATA_PRONUNCIA " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataPronuncia());
		}
		if ((dados.getDataSentenca().length()>0)){
			SqlCampos+= ",DATA_SENTENCA " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataSentenca());
		}
		if ((dados.getDataTransitoJulgado().length()>0)){
			SqlCampos+= ",DATA_TRANS_JULGADO " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataTransitoJulgado());
		}
		if ((dados.getDataTransitoJulgadoMP().length()>0)){
			SqlCampos+= ",DATA_TRANS_JULGADO_MP " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataTransitoJulgadoMP());
		}
		if ((dados.getDataDenuncia().length()>0)){
			SqlCampos+= ",DATA_DENUNCIA " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataDenuncia());
		}
		if ((dados.getDataAdmonitoria().length()>0)){
			SqlCampos+= ",DATA_ADMONITORIA " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataAdmonitoria());
		}
		if ((dados.getDataInicioCumprimentoPena().length()>0)){
			SqlCampos+= ",DATA_INICIO_CUMP_PENA " ;
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataInicioCumprimentoPena());
		}
		if ((dados.getNumeroAcaoPenal().length()>0)){
			SqlCampos+= ",NUMERO_ACAO_PENAL " ;
			SqlValores+= ",?";
			ps.adicionarLong(dados.getNumeroAcaoPenal());
		}
		if ((dados.getVaraOrigem().length()>0)){
			SqlCampos+= ",VARA_ORIGEM " ;
			SqlValores+= ",?";
			ps.adicionarString(dados.getVaraOrigem());
		}
		
//		if ((dados.getCodigoTemp().length()>0)) Sql+= ",CODIGO_TEMP " ;
//		if ((dados.getCodigoTemp().length()>0)) Sql+= "," + Funcoes.BancoLogico(dados.getCodigoTemp());	
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_PROC_EXE", ps));		 
	} 
	
	/**
	 * sobrescreve o método do gerador, pois altera somente os campos que realmente foram alterados.
	 */
	public void alterar(ProcessoExecucaoDt dados) throws Exception{

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.PROC_EXE SET  ";
		//dados obrigatórios
		if ((dados.getId_ProcessoExecucaoPenal().length()>0)){
			Sql+= "ID_PROC_EXE_PENAL  = ? ";
			ps.adicionarLong(dados.getId_ProcessoExecucaoPenal());
		}
		if ((dados.getId_ProcessoAcaoPenal().length()>0)){
			Sql+= ",ID_PROC_ACAO_PENAL  = ? ";
			ps.adicionarLong(dados.getId_ProcessoAcaoPenal());
		}
		if ((dados.getId_CidadeOrigem().length()>0)){
			Sql+= ",ID_CIDADE_ORIGEM = ? ";
			ps.adicionarLong(dados.getId_CidadeOrigem());
		}
		if ((dados.getDataDistribuicao().length()>0)){
			Sql+= ",DATA_DIST = ? ";
			ps.adicionarDate(dados.getDataDistribuicao()); 
		}
		if ((dados.getDataSentenca().length()>0)){
			Sql+= ",DATA_SENTENCA = ? ";
			ps.adicionarDate(dados.getDataSentenca()); 
		}
		if ((dados.getDataTransitoJulgado().length()>0)){
			Sql+= ",DATA_TRANS_JULGADO = ? ";
			ps.adicionarDate(dados.getDataTransitoJulgado()); 
		}
		if ((dados.getDataDenuncia().length()>0)){
			Sql+= ",DATA_DENUNCIA = ? ";
			ps.adicionarDate(dados.getDataDenuncia()); 
		}
		if ((dados.getDataInicioCumprimentoPena().length()>0)){
			Sql+= ",DATA_INICIO_CUMP_PENA = ? ";
			ps.adicionarDate(dados.getDataInicioCumprimentoPena());
		}
		if ((dados.getNumeroAcaoPenal().length()>0)){
			Sql+= ",NUMERO_ACAO_PENAL  = ? ";
			ps.adicionarLong(dados.getNumeroAcaoPenal()); 
		}
		if ((dados.getVaraOrigem().length()>0)){
			Sql+= ",VARA_ORIGEM  = ? ";
			ps.adicionarString(dados.getVaraOrigem());
		}
		if ((dados.getCodigoTemp().length()>0)){
			Sql+= ",CODIGO_TEMP  = ? ";
			ps.adicionarBoolean(dados.getCodigoTemp());
		}
		//dados opcionais
		Sql+= ",DATA_ACORDAO = ? ";
		ps.adicionarDate(dados.getDataAcordao());
		Sql+= ",DATA_PRONUNCIA = ? ";
		ps.adicionarDate(dados.getDataPronuncia());
		Sql+= ",DATA_ADMONITORIA = ? ";
		ps.adicionarDate(dados.getDataAdmonitoria());
		Sql+= ",DATA_TRANS_JULGADO_MP = ? ";
		ps.adicionarDate(dados.getDataTransitoJulgadoMP()); 
		
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_PROC_EXE  = ? ";
		ps.adicionarLong(dados.getId()); 
		
		executarUpdateDelete(Sql,ps); 
	}  
	
	/**
	 * Lista os dados das ações penais (com o tempo total de condenação) do processo de execução informado pelo id_ProcessoExecucaoPenal
	 * @param id_ProcessoExecucaoPenal: id do processo de execução penal.
	 * @return List<ProcessoExecucaoDt> lista com os dados das ações penais.
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarAcoesPenais(String id_ProcessoExecucaoPenal) throws Exception{
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
//		sql.append("SELECT ID_PROC_EXE, PROC_EXE_PENAL_NUMERO, ID_PROC_EXE_PENAL, ID_PROC_ACAOPENAL, PROC_ACAO_PENAL_NUMERO, ID_CIDADE_ORIGEM, CIDADE_ORIGEM, ESTADO_ORIGEM, UF_ORIGEM, DATA_ACORDAO, DATA_DIST, DATA_PRONUNCIA, DATA_SENTENCA, DATA_TRANS_JULGADO, DATA_DENUNCIA, DATA_ADMONITORIA, DATA_INICIO_CUMP_PENA, NUMERO_ACAO_PENAL, VARA_ORIGEM, CODIGO_TEMP, ID_LOCAL_CUMP_PENA, LOCAL_CUMP_PENA, ID_EVENTO_LOCAL, ID_REGIME_EXE, REGIME_EXE, ID_PENA_EXE_TIPO, PENA_EXE_TIPO,");
//		sql.append(" sum(c.TEMPO_PENA) TEMPO_TOTAL_CONDENACAO FROM VIEW_PROC_EXE pe");
//		sql.append(" LEFT JOIN VIEW_CONDENACAO_EXE c on c.ID_PROC_EXE = pe.ID_PROC_EXE");
//		sql.append(" INNER JOIN VIEW_PROC_EVENTO_EXE pee on pe.ID_PROC_EXE = pee.ID_PROC_EXE");
//		sql.append(" INNER JOIN VIEW_EVENTO_LOCAL el on el.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
//		sql.append(" INNER JOIN VIEW_EVENTO_REGIME er on er.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
//		sql.append(" WHERE pe.ID_PROC_EXE_PENAL = ? ");
//		ps.adicionarLong(id_ProcessoExecucaoPenal);
//		sql.append(" AND pee.ID_EVENTO_EXE IN (?,?)");
//		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
//		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);
//		sql.append(" GROUP BY ID_PROC_EXE, PROC_EXE_PENAL_NUMERO, ID_PROC_EXE_PENAL, ID_PROC_ACAOPENAL, PROC_ACAO_PENAL_NUMERO, ID_CIDADE_ORIGEM, CIDADE_ORIGEM, ESTADO_ORIGEM, UF_ORIGEM, DATA_ACORDAO, DATA_DIST, DATA_PRONUNCIA, DATA_SENTENCA, DATA_TRANS_JULGADO, DATA_DENUNCIA, DATA_ADMONITORIA, DATA_INICIO_CUMP_PENA, NUMERO_ACAO_PENAL, VARA_ORIGEM, CODIGO_TEMP, ID_LOCAL_CUMP_PENA, LOCAL_CUMP_PENA, ID_EVENTO_LOCAL, ID_REGIME_EXE, REGIME_EXE, ID_PENA_EXE_TIPO, PENA_EXE_TIPO ");
//		sql.append(" ORDER BY pe.DATA_TRANS_JULGADO, c.DATA_FATO, c.ID_CRIME_EXE");
		
		sql.append("SELECT DISTINCT(pe.ID_PROC_EXE), pe.PROC_EXE_PENAL_NUMERO, pe.ID_PROC_EXE_PENAL, pe.ID_PROC_ACAO_PENAL, pe.PROC_ACAO_PENAL_NUMERO, pe.ID_CIDADE_ORIGEM, pe.CIDADE_ORIGEM,"); 
		sql.append(" pe.ESTADO_ORIGEM, pe.UF_ORIGEM, pe.DATA_ACORDAO, pe.DATA_DIST, pe.DATA_PRONUNCIA, pe.DATA_SENTENCA, pe.DATA_TRANS_JULGADO, pe.DATA_TRANS_JULGADO_MP, pe.DATA_DENUNCIA, pe.DATA_ADMONITORIA,"); 
		sql.append(" pe.DATA_INICIO_CUMP_PENA, pe.NUMERO_ACAO_PENAL, pe.VARA_ORIGEM, pe.CODIGO_TEMP, el.ID_LOCAL_CUMP_PENA, el.LOCAL_CUMP_PENA, el.ID_EVENTO_LOCAL, ");
		sql.append(" er.ID_EVENTO_REGIME, er.ID_REGIME_EXE, er.REGIME_EXE, er.ID_PENA_EXE_TIPO, er.PENA_EXE_TIPO, sum(c.TEMPO_PENA) as TEMPO_TOTAL_CONDENACAO ");
		sql.append(" FROM PROJUDI.VIEW_PROC_EXE pe ");
		sql.append(" LEFT JOIN PROJUDI.VIEW_CONDENACAO_EXE c on c.ID_PROC_EXE = pe.ID_PROC_EXE"); 
		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EVENTO_EXE pee on pe.ID_PROC_EXE = pee.ID_PROC_EXE"); 
		sql.append(" LEFT JOIN PROJUDI.VIEW_EVENTO_LOCAL el on el.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE"); 
		sql.append(" LEFT JOIN PROJUDI.VIEW_EVENTO_REGIME er on er.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
		sql.append(" WHERE (pe.ID_PROC_EXE_PENAL = ? or pe.ID_PROC_DEPENDENTE = ?)");
		ps.adicionarLong(id_ProcessoExecucaoPenal);
		ps.adicionarLong(id_ProcessoExecucaoPenal);
		sql.append(" AND pee.ID_EVENTO_EXE IN (?,?)");
		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);
		sql.append(" GROUP BY pe.ID_PROC_EXE, pe.PROC_EXE_PENAL_NUMERO, pe.ID_PROC_EXE_PENAL, pe.ID_PROC_ACAO_PENAL, pe.PROC_ACAO_PENAL_NUMERO, pe.ID_CIDADE_ORIGEM, pe.CIDADE_ORIGEM,"); 
		sql.append(" pe.ESTADO_ORIGEM, pe.UF_ORIGEM, pe.DATA_ACORDAO, pe.DATA_DIST, pe.DATA_PRONUNCIA, pe.DATA_SENTENCA, pe.DATA_TRANS_JULGADO, pe.DATA_TRANS_JULGADO_MP, pe.DATA_DENUNCIA, pe.DATA_ADMONITORIA, ");
		sql.append(" pe.DATA_INICIO_CUMP_PENA, pe.NUMERO_ACAO_PENAL, pe.VARA_ORIGEM, pe.CODIGO_TEMP, el.ID_LOCAL_CUMP_PENA, el.LOCAL_CUMP_PENA, el.ID_EVENTO_LOCAL, er.ID_REGIME_EXE, ");
		sql.append(" er.ID_EVENTO_REGIME, er.REGIME_EXE, er.ID_PENA_EXE_TIPO, er.PENA_EXE_TIPO ");
		sql.append(" ORDER BY pe.DATA_TRANS_JULGADO");
//		sql.append(" ORDER BY pe.DATA_TRANS_JULGADO, c.DATA_FATO, c.ID_CRIME_EXE");
		
		try{
			rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				ProcessoExecucaoDt processoExecucaoDt = new ProcessoExecucaoDt();
				
				associarDt(processoExecucaoDt, rs);
				setDadosEventoLocal(processoExecucaoDt, rs);
				setDadosEventoRegime(processoExecucaoDt, rs);
				processoExecucaoDt.setTempoTotalCondenacaoDias(rs.getString("TEMPO_TOTAL_CONDENACAO"));
				lista.add(processoExecucaoDt);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 

		return lista;
	}
	
	/**
	 * Lista os dados das ações penais (com as condenações respectivas) do processo de execução informado pelo id_ProcessoExecucaoPenal
	 * @param id_ProcessoExecucaoPenal: id do processo de execução penal.
	 * @return List<ProcessoExecucaoDt> lista com os dados das ações penais e condenações.
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarAcoesPenaisComCondenacoes(String id_ProcessoExecucaoPenal) throws Exception{
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql.append("SELECT * FROM PROJUDI.VIEW_PROC_EXE pe");
		sql.append(" INNER JOIN PROJUDI.PROC_EVENTO_EXE pee on pe.ID_PROC_EXE = pee.ID_PROC_EXE");
		sql.append(" LEFT JOIN PROJUDI.VIEW_CONDENACAO_EXE c on c.ID_PROC_EXE = pe.ID_PROC_EXE"); //LEFT JOIN pois medida de segurança não tem condenação
		//alterei as linhas abaixo para left join pois, na importação, pode não ter vindo o local e o regime
		sql.append(" LEFT JOIN PROJUDI.VIEW_EVENTO_LOCAL el ON el.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE "); 
		sql.append(" left JOIN PROJUDI.VIEW_EVENTO_REGIME er on er.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
		sql.append(" WHERE pe.ID_PROC_EXE_PENAL = ? ");
		ps.adicionarLong(id_ProcessoExecucaoPenal);
		sql.append(" AND pee.ID_EVENTO_EXE IN (?,?)");
		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);
		sql.append(" ORDER BY pe.ID_PROC_EXE");
		
		try{
			rs = consultar(sql.toString(), ps);
			String idProcessoExecucao = "";
			
			while (rs.next()){
				if (!idProcessoExecucao.equals(rs.getString("ID_PROC_EXE"))){
					ProcessoExecucaoDt processoExecucaoDt = new ProcessoExecucaoDt();
					associarDt(processoExecucaoDt, rs);
					setDadosEventoLocal(processoExecucaoDt, rs);
					setDadosEventoRegime(processoExecucaoDt, rs);
					
					if (!processoExecucaoDt.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))){
						CondenacaoExecucaoDt condenacao = new CondenacaoExecucaoDt();
						associarDtCondenacao(condenacao, rs);
						processoExecucaoDt.addListaCondenacoes(condenacao);
						
					}
					lista.add(processoExecucaoDt);
				} else{
					CondenacaoExecucaoDt condenacao = new CondenacaoExecucaoDt();
					associarDtCondenacao(condenacao, rs);
					
					((ProcessoExecucaoDt)lista.get(lista.size()-1)).addListaCondenacoes(condenacao);
				}
				
				idProcessoExecucao = rs.getString("ID_PROC_EXE");
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 

		return lista;
	}

	public void associarDtCondenacao( CondenacaoExecucaoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		if (rs1.getString("ID_CONDENACAO_EXE") !=null) Dados.setId(rs1.getString("ID_CONDENACAO_EXE"));
		if (rs1.getString("TEMPO_PENA") !=null) Dados.setTempoPena( rs1.getString("TEMPO_PENA"));
		if (rs1.getString("TEMPO_PENA") !=null) Dados.setTempoPenaEmDias(rs1.getString("TEMPO_PENA"));
		if (rs1.getString("REINCIDENTE") !=null) Dados.setReincidente( Funcoes.FormatarLogico(rs1.getString("REINCIDENTE")));
		if (rs1.getString("DATA_FATO") !=null) Dados.setDataFato( Funcoes.FormatarData(rs1.getDateTime("DATA_FATO")));
//			if (rs1.getString("DATA_TRANS_JULGADO") !=null) Dados.setDataTransitoJulgado(Funcoes.FormatarData(rs1.getDate("DATA_TRANS_JULGADO")));
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
	 * lista todos os trânsitos em julgado (e guia de recolhimento provisória), com condenações  do processo principal e apensos, e vínculo com a comutação (se houver)
	 * (HashMap com as chaves: Id_ProcessoExecucao, DataTransitoJulgado, TempoNaoExtintoDias, TempoExtintoDias, TempoTotalDias, Checked, Fracao, Id_ComutacaoTransitoEvento)
	 * @param id_ProcessoExecucaoPenal: identificador do processo de execução penal
	 * @param id_ProcessoEventoExecucao: identificador do evento de comutação que está sendo alterado.
	 * @return lista com o HashMap com as informações dos TJs
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarTransitoComTotalCondenacao_HashMap(String id_ProcessoExecucaoPenal, String id_ProcessoEventoExecucao, String id_EventoExecucao) throws Exception{
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql.append("SELECT pe.DATA_TRANS_JULGADO, pe.DATA_INICIO_CUMP_PENA, c.ID_CONDENACAO_EXE_SIT, sum(c.TEMPO_PENA) as TEMPO_CONDENACAO, pe.ID_PROC_EXE");
		if (id_ProcessoEventoExecucao.length() > 0)	sql.append(" , tje.ID_TRANS_JULGADO_EVENTO, tje.FRACAO, tje.ID_EVENTO, tje.ID_TRANS_JULGADO, tje.ID_EVENTO_EXE");
		sql.append(" FROM PROJUDI.VIEW_PROC_EXE pe");
		sql.append(" INNER JOIN PROJUDI.VIEW_CONDENACAO_EXE c ON pe.ID_PROC_EXE = c.ID_PROC_EXE ");
		if (id_ProcessoEventoExecucao.length() > 0) sql.append(" LEFT JOIN PROJUDI.VIEW_TRANS_JULGADO_EVENTO tje on pe.ID_PROC_EXE = tje.ID_PROC_EXE");		
		sql.append(" WHERE (pe.ID_PROC_EXE_PENAL = ?");
		ps.adicionarLong(id_ProcessoExecucaoPenal);
		sql.append(" OR pe.ID_PROC_DEPENDENTE = ?)");
		ps.adicionarLong(id_ProcessoExecucaoPenal);
		sql.append(" AND (pe.CODIGO_TEMP =  1 OR pe.CODIGO_TEMP is Null)"); //processo execução ativo
//		if (id_ProcessoEventoExecucao.length() > 0)	{
//			sql.append(" AND ctj.ID_EVENTO_EXE = ?");
//			ps.adicionarLong(id_EventoExecucao);
//		}
		sql.append(" GROUP BY pe.ID_PROC_EXE, c.ID_CONDENACAO_EXE_SIT, pe.DATA_TRANS_JULGADO, pe.DATA_INICIO_CUMP_PENA");
		if (id_ProcessoEventoExecucao.length() > 0)	sql.append(" , tje.ID_TRANS_JULGADO_EVENTO, tje.FRACAO, tje.ID_EVENTO, tje.ID_TRANS_JULGADO, tje.ID_EVENTO_EXE");
		sql.append(" ORDER BY ID_PROC_EXE");
			
		try{
			rs = consultar(sql.toString(), ps);
			String id_ProcessoExecucao = "";
			String id_CondenacaoExecucaoSituacao = "";
			HashMap map = null;

			while (rs.next()){
				if (!id_ProcessoExecucao.equals(rs.getString("ID_PROC_EXE"))){//identifica os TJ diferentes, para adicionar somente uma vez na lista
					map = new HashMap();
					map.put("Id_ProcessoExecucao", rs.getString("ID_PROC_EXE"));
					map.put("DataTransitoJulgado", Funcoes.FormatarData(rs.getDateTime("DATA_TRANS_JULGADO")));
					if (rs.getString("DATA_INICIO_CUMP_PENA") != null && !rs.getString("DATA_INICIO_CUMP_PENA").equals(""))
						map.put("DataInicioCumprimentoPena", Funcoes.FormatarData(rs.getDateTime("DATA_INICIO_CUMP_PENA")));
					else map.put("DataInicioCumprimentoPena", "");
					
					//verifica o tempo de condenação que não está extinto
					if (rs.getString("ID_CONDENACAO_EXE_SIT").equals(String.valueOf(CondenacaoExecucaoSituacaoDt.NAO_APLICA))){
						map.put("TempoNaoExtintoDias", rs.getString("TEMPO_CONDENACAO"));
						map.put("TempoExtintoDias", "0");
					}
					else {//verifica o tempo de condenação que está extinto (por prescrição ou cumprimento)
						map.put("TempoExtintoDias", rs.getString("TEMPO_CONDENACAO"));
						map.put("TempoNaoExtintoDias", "0");
					}
					
					map.put("Checked", "0");
					map.put("Fracao", ""); 
					map.put("Id_TransitoJulgadoEvento", "");
					
					//verifica se é alteração ou inclusão do evento de comutação de pena:
						// em caso de alteração, verifica qual TJ está relacionado à comutação selecionada pelo usuário
					if (id_ProcessoEventoExecucao.length() > 0){
						if (rs.getString("ID_TRANS_JULGADO_EVENTO") != null && rs.getString("ID_EVENTO").equals(id_ProcessoEventoExecucao)
								&& id_EventoExecucao.equals(rs.getString("ID_EVENTO_EXE"))){
							map.put("Checked", "1"); 
							map.put("Fracao", rs.getString("FRACAO"));
							map.put("Id_TransitoJulgadoEvento", rs.getString("ID_TRANS_JULGADO_EVENTO"));
						}
					}
					
					id_ProcessoExecucao = rs.getString("ID_PROC_EXE");
					id_CondenacaoExecucaoSituacao = rs.getString("ID_CONDENACAO_EXE_SIT");
					lista.add(map);
					
				} else{
					int tempoNaoExtinto = 0;
					int tempoExtinto = 0;
					
					//ou caso seja o mesmo TJ do registro anterior, em comutação diferente, porém, com situação de condenação diferente, tem q somar no tempo de TJ
					if (!id_CondenacaoExecucaoSituacao.equals(rs.getString("ID_CONDENACAO_EXE_SIT"))){
						tempoNaoExtinto = Funcoes.StringToInt(((HashMap)lista.get(lista.size()-1)).get("TempoNaoExtintoDias").toString());
						tempoExtinto = Funcoes.StringToInt(((HashMap)lista.get(lista.size()-1)).get("TempoExtintoDias").toString());	
					
						//verifica, pois o registro no ResultSetTJGO pode repetir (caso tenha outro id_CondenacaoExecucaoSituacao para exitnção da condenação. Ex: extinção por prescrição - id=2, extinção por cumprimento - id=1)
						if (rs.getString("ID_CONDENACAO_EXE_SIT").equals(String.valueOf(CondenacaoExecucaoSituacaoDt.NAO_APLICA)))
							tempoNaoExtinto += Funcoes.StringToInt(rs.getString("TEMPO_CONDENACAO"));
						else tempoExtinto += Funcoes.StringToInt(rs.getString("TEMPO_CONDENACAO"));
						
						((HashMap)lista.get(lista.size()-1)).put("TempoNaoExtintoDias", tempoNaoExtinto);
						((HashMap)lista.get(lista.size()-1)).put("TempoExtintoDias", tempoExtinto);
					
					}
					//quando é o mesmo TJ, verifica se a comutação é a mesma selecionada.
					else{
						if (id_ProcessoEventoExecucao.length() > 0){
							if (rs.getString("ID_TRANS_JULGADO_EVENTO") != null && rs.getString("ID_EVENTO").equals(id_ProcessoEventoExecucao)
									&& id_EventoExecucao.equals(rs.getString("ID_EVENTO_EXE"))){
								((HashMap)lista.get(lista.size()-1)).put("Checked", "1"); 
								((HashMap)lista.get(lista.size()-1)).put("Fracao", rs.getString("FRACAO"));
								((HashMap)lista.get(lista.size()-1)).put("Id_TransitoJulgadoEvento", rs.getString("ID_TRANS_JULGADO_EVENTO"));
							} 
						}
					}
				}
			}
			for (int i=0; i<lista.size(); i++){
	        	HashMap map1 = (HashMap)lista.get(i);
	        	int tempoTotalDias = Funcoes.StringToInt(map1.get("TempoNaoExtintoDias").toString()) + Funcoes.StringToInt(map1.get("TempoExtintoDias").toString());
	        	map1.put("TempoTotalDias", tempoTotalDias);
	        	map1.put("TempoTotalAnos", Funcoes.converterParaAnoMesDia(tempoTotalDias));
				map1.put("TempoExtintoAnos", Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(map1.get("TempoExtintoDias").toString())));
				map1.put("TempoNaoExtintoAnos", Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(map1.get("TempoNaoExtintoDias").toString())));
	        }
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	/**
	 * Consulta os dados da ação penal (com as condenações respectivas) do processoExecução informado pelo id_ProcessoExecucao
	 * @param id_ProcessoExecucao: id do processoExecucao.
	 * @return ProcessoExecucaoDt: dados da ação penal e condenações.
	 * @throws Exception
	 * @author wcsilva
	 */
	public ProcessoExecucaoDt consultarAcaoPenalComCondenacao(String id_ProcessoExecucao) throws Exception{
		StringBuffer sql = new StringBuffer();
		ProcessoExecucaoDt processoExecucaoDt = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql.append("SELECT * FROM PROJUDI.VIEW_PROC_EXE pe");
		sql.append(" LEFT JOIN PROJUDI.VIEW_CONDENACAO_EXE c on c.ID_PROC_EXE = pe.ID_PROC_EXE");
		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EVENTO_EXE pee on pe.ID_PROC_EXE = pee.ID_PROC_EXE");
		//alterei as linhas abaixo para left join pois, na importação, pode não ter vindo o local e o regime
		sql.append(" left JOIN PROJUDI.VIEW_EVENTO_LOCAL el on el.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
		sql.append(" left JOIN PROJUDI.VIEW_EVENTO_REGIME er on er.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
		sql.append(" WHERE pe.ID_PROC_EXE = ?");
		ps.adicionarLong(id_ProcessoExecucao);
		sql.append(" AND pee.ID_EVENTO_EXE IN (?,?)");
		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);
		try{
			rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				if (processoExecucaoDt == null){
					processoExecucaoDt = new ProcessoExecucaoDt();
					associarDt(processoExecucaoDt, rs);
					setDadosEventoLocal(processoExecucaoDt, rs);
					setDadosEventoRegime(processoExecucaoDt, rs);
				}
				
				if (rs.getString("ID_CONDENACAO_EXE") != null) {
					CondenacaoExecucaoDt condenacao = new CondenacaoExecucaoDt();
					associarDtCondenacao(condenacao, rs);
					processoExecucaoDt.addListaCondenacoes(condenacao);
				}
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 

		return processoExecucaoDt;
	}
	
	private void setDadosEventoLocal(ProcessoExecucaoDt processoExecucaoDt, ResultSetTJGO rs) throws Exception{		
		processoExecucaoDt.setId_LocalCumprimentoPena(rs.getString("ID_LOCAL_CUMP_PENA"));
		processoExecucaoDt.setLocalCumprimentoPena(rs.getString("LOCAL_CUMP_PENA"));
		processoExecucaoDt.setIdEventoLocal(rs.getString("ID_EVENTO_LOCAL"));			
	}

	private void setDadosEventoRegime(ProcessoExecucaoDt processoExecucaoDt, ResultSetTJGO rs) throws Exception{

		processoExecucaoDt.setId_RegimeExecucao(rs.getString("ID_REGIME_EXE"));
		processoExecucaoDt.setRegimeExecucao(rs.getString("REGIME_EXE"));
		processoExecucaoDt.setId_PenaExecucaoTipo(rs.getString("ID_PENA_EXE_TIPO"));
		processoExecucaoDt.setPenaExecucaoTipo(rs.getString("PENA_EXE_TIPO"));
		processoExecucaoDt.setIdEventoRegime(rs.getString("ID_EVENTO_REGIME"));

	}
	
	/**
	 * Recupera os dados da Guia de Recolhimento no SPG através do número da guia numeroGuiaRecolhimento
	 * @param numeroGuiaRecolhimento: número da guia de recolhimento
	 * @return mensagem, quando retorna diferente de vazio é porque ocorreu erro na consulta.
	 * @throws Exception
	 * @author kbsriccioppo
	 */
	public String consultarGuiaRecolhimento(String numeroGuiaRecolhimento, ProcessoExecucaoDt processoExecucaoDt, ProcessoParteDt processoParteDt) throws Exception{
		URL url = new URL("http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-guia/forpspgi/URA0003T?numeroguia=" + numeroGuiaRecolhimento);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String mensagem = "";
		//File xml = new File (br);
		//StringBuffer buffer = new StringBuffer();
		//Criamos uma classe SAXBuilder que vai processar o XML4
		SAXBuilder sb = new SAXBuilder();  
		//Este documento agora possui toda a estrutura do arquivo.  
		Document d = sb.build(br);  
		//Recuperamos o elemento root  
		Element guiaRecolhimento = d.getRootElement();
		//se o numero da guia informado para pesquisa for igual ao retorno xml
		if (numeroGuiaRecolhimento.equals(guiaRecolhimento.getAttributeValue("numeroGuiaRecolhimento").trim())){
			//processoExecucaoDt.setNumeroGuiaRecolhimento(guiaRecolhimento.getAttributeValue("numeroGuiaRecolhimento").trim());
			List dados = guiaRecolhimento.getChildren();  
			Iterator i = dados.iterator();  
			//Iteramos com os elementos filhos, e filhos do dos filhos  
			while (i.hasNext()) { 
				Element itensDados = (Element) i.next();
				String nomeNo = itensDados.getName();
				String valorNo = itensDados.getValue().trim();
				if (valorNo.length()>0){
					if (nomeNo.equals("msg")){
						mensagem = valorNo;
						return mensagem;
					}
					else if (nomeNo.equals("tipoGR")) 				processoExecucaoDt.setGuiaRecolhimento(valorNo);
					else if (nomeNo.equals("numeroProcessoAcaoPenal")) processoExecucaoDt.setNumeroAcaoPenal(valorNo);
					else if (nomeNo.equals("dataSentenca"))			processoExecucaoDt.setDataSentenca(valorNo);
					else if (nomeNo.equals("dataTransitoJulgado"))	processoExecucaoDt.setDataTransitoJulgado(valorNo);
					else if (nomeNo.equals("dataAcordao"))			processoExecucaoDt.setDataAcordao(valorNo);
					else if (nomeNo.equals("dataPronuncia")) 		processoExecucaoDt.setDataPronuncia(valorNo);
					else if (nomeNo.equals("dataDenuncia"))			processoExecucaoDt.setDataDenuncia(valorNo);
					else if (nomeNo.equals("dataDistribuicao"))		processoExecucaoDt.setDataDistribuicao(valorNo);
					else if (nomeNo.equals("dataAdmonitoria"))		{
						//08/07/2010 - de acordo com o jr. existe mais de uma data admonitoria - tem que decidir o que fazer
						processoExecucaoDt.setDataAdmonitoria(valorNo);
					}
					else if (nomeNo.equals("acaoPenalVaraOrigem"))	processoExecucaoDt.setVaraOrigem(valorNo);
					else if (nomeNo.equals("codigoTipoPena")) 		{
						if (!valorNo.equals("0")) processoExecucaoDt.setId_PenaExecucaoTipo(valorNo);
					}
					else if (nomeNo.equals("codigoRegime")) 		{
						if (!valorNo.equals("0")){
							try{
								int valor = Integer.parseInt(valorNo);
								processoExecucaoDt.setId_RegimeExecucao(valorNo);	
							}catch(Exception e){
								processoExecucaoDt.setId_RegimeExecucao("");
							}
						}
					}
					else if (nomeNo.equals("sentenciado")){
						List itemSentenciado = itensDados.getChildren(); 
						Iterator s = itemSentenciado.iterator();  
						String nomeNoFilhoSentenciado = "";
						String valorNoFilhoSentenciado = "";
						while (s.hasNext()) { 
							Element itensSentenciado = (Element) s.next(); 
							nomeNoFilhoSentenciado = itensSentenciado.getName();
							valorNoFilhoSentenciado = itensSentenciado.getValue().trim();
							if (nomeNoFilhoSentenciado.equals("numeroCpf"))			processoParteDt.setCpf(valorNoFilhoSentenciado);
							else if (nomeNoFilhoSentenciado.equals("nome"))			processoParteDt.setNome(valorNoFilhoSentenciado);
							else if (nomeNoFilhoSentenciado.equals("nomeMae"))		processoParteDt.setNomeMae(valorNoFilhoSentenciado);
							else if (nomeNoFilhoSentenciado.equals("nomePai"))		processoParteDt.setNomePai(valorNoFilhoSentenciado);
							else if (nomeNoFilhoSentenciado.equals("dataNascimento"))processoParteDt.setDataNascimento(valorNoFilhoSentenciado);
							else if (nomeNoFilhoSentenciado.equals("sexo"))			processoParteDt.setSexo(valorNoFilhoSentenciado);
							else if (nomeNoFilhoSentenciado.equals("idProfissao"))	{
								if (!valorNo.equals("0")){
									try{
										int valor = Integer.parseInt(valorNo);
										processoParteDt.setId_Profissao(valorNoFilhoSentenciado);
									}catch(Exception e){
										processoParteDt.setId_Profissao("");
									}
								}
								
//								if (!valorNo.equals("0")) processoParteDt.setId_Profissao(valorNoFilhoSentenciado);
							}
							else if (nomeNoFilhoSentenciado.equals("endereco")){
								List itemEndereco = itensSentenciado.getChildren();
								Iterator e = itemEndereco.iterator();  
								String nomeNoFilhoEndereco = "";
								String valorNoFilhoEndereco = "";
								while (e.hasNext()) { 
									Element itensEndereco = (Element) e.next(); 
									nomeNoFilhoEndereco = itensEndereco.getName();
									valorNoFilhoEndereco = itensEndereco.getValue().trim();
									if (nomeNoFilhoEndereco.equals("logradouro"))		
										processoParteDt.getEnderecoParte().setLogradouro(valorNoFilhoEndereco);
									else if (nomeNoFilhoEndereco.equals("numero"))		
											processoParteDt.getEnderecoParte().setNumero(valorNoFilhoEndereco);
									else if (nomeNoFilhoEndereco.equals("complemento"))
											processoParteDt.getEnderecoParte().setComplemento(valorNoFilhoEndereco);
									else if (nomeNoFilhoEndereco.equals("cep"))		
											processoParteDt.getEnderecoParte().setCep(valorNoFilhoEndereco);
								}
							}
							else if (nomeNoFilhoSentenciado.equals("numeroRg"))				processoParteDt.setRg(valorNoFilhoSentenciado);
							else if (nomeNoFilhoSentenciado.equals("telefoneResidencial"))		processoParteDt.setTelefone(valorNoFilhoSentenciado);
						}
						
					}
//					else if (nomeNo.equals("listaCondenacao")){
//						List listaCondenacao = itensDados.getChildren();
//						Iterator lc = listaCondenacao.iterator();  
//						String nomeNoFilhoCondenacao = "";
//						String valorNoFilhoCondenacao = "";
//						processoExecucaoDt.setListaCondenacoes(null);
//						while (lc.hasNext()) { 
//							CondenacaoExecucaoDt condenacaoExecucaoDt = new CondenacaoExecucaoDt();
//							Element condenacao = (Element) lc.next(); 
//							List itensCondenacao = condenacao.getChildren();
//							Iterator ic = itensCondenacao.iterator();  
//							int qtdeAnos = 0, qtdeMeses = 0, qtdeDias = 0;
//							while (ic.hasNext()) { 
//								Element itemCondenacao = (Element) ic.next(); 
//								nomeNoFilhoCondenacao = itemCondenacao.getName();
//								valorNoFilhoCondenacao = itemCondenacao.getValue();
//								if (nomeNoFilhoCondenacao.equals("idInfracao")) condenacaoExecucaoDt.setId_CrimeExecucao(valorNoFilhoCondenacao);
//								if ((nomeNoFilhoCondenacao.equals("tempoAnoPena") && (!valorNoFilhoCondenacao.equals("")))) {
//									qtdeAnos = Funcoes.StringToInt(valorNoFilhoCondenacao);
//								}
//								if ((nomeNoFilhoCondenacao.equals("tempoMesPena") && (!valorNoFilhoCondenacao.equals("")))) {
//									qtdeMeses = Funcoes.StringToInt(valorNoFilhoCondenacao);
//								}
//								if ((nomeNoFilhoCondenacao.equals("tempoDiaPena") && (!valorNoFilhoCondenacao.equals("")))) {
//									qtdeDias = Funcoes.StringToInt(valorNoFilhoCondenacao);
//								}
//								else if (nomeNoFilhoCondenacao.equals("DATA_FATO"))	condenacaoExecucaoDt.setDataFato(valorNoFilhoCondenacao);
//							}
//							//confirmar wilana
//							String totalDias = Funcoes.converterParaDias(qtdeAnos, qtdeMeses, qtdeDias);
//							condenacaoExecucaoDt.setTempoPenaEmDias(totalDias);
//							processoExecucaoDt.addListaCondenacoes(condenacaoExecucaoDt);
//						}
//					}
					//retirado informação da prisão e liberdade provisória a pedido da Nair, pois a informação está sendo cadastrada errado no SPG. Em 21/08/2014
					else if (nomeNo.equals("listaPrisao")){//confirmar wilana
						List listaPrisao = itensDados.getChildren();
						Iterator lp = listaPrisao.iterator();  
						String nomeNoFilhoPrisao = "";
						String valorNoFilhoPrisao = "";
						processoExecucaoDt.setListaPrisoesProvisorias(null);
						processoExecucaoDt.setListaLiberdadesProvisorias(null);
						while (lp.hasNext()) { 
							Element prisao = (Element) lp.next(); 
							List itensPrisao = prisao.getChildren();
							Iterator ip = itensPrisao.iterator();  
							while (ip.hasNext()) { 
								Element itemPrisao = (Element) ip.next(); 
								nomeNoFilhoPrisao = itemPrisao.getName();
								valorNoFilhoPrisao = itemPrisao.getValue().trim();
								if (valorNoFilhoPrisao!=null && !valorNoFilhoPrisao.equals("")){
									if (nomeNoFilhoPrisao.equals("dataPrisaoProvisoria")){
//										processoExecucaoDt.addListaPrisoesProvisorias(valorNoFilhoPrisao);
									} else if (nomeNoFilhoPrisao.equals("dataLiberdadeProvisoria")){
										//adicionar um dia do que foi informado na guia
										if (!valorNoFilhoPrisao.equals("")){
											valorNoFilhoPrisao = Funcoes.somaData(valorNoFilhoPrisao, 1);
//											processoExecucaoDt.addListaLiberdadesProvisorias(valorNoFilhoPrisao);
										}
									}
									else if (nomeNoFilhoPrisao.equals("dataInicioCumprimentoPena"))
											processoExecucaoDt.setDataPrimeiroRegime(valorNoFilhoPrisao);
								}
							}
						}
					}
				}
			} 
		}
		else {
			mensagem = "O Número da Guia de Recolhimento informado não foi encontrado no SPG.";
		}
		return mensagem;
	}

	
	//consulta JSON para os dados da ação penal
	public String consultarIdJSON(String idProcessoExecucao) throws Exception {
		
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoExecucaoDt dados=null;
		StringBuilder stTemp = new StringBuilder();
		
		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_EXE WHERE ID_PROC_EXE = ?";
		ps.adicionarLong(idProcessoExecucao); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				dados= new ProcessoExecucaoDt();
				associarDt(dados, rs1);
			}
			
			stTemp.append("[{");
			stTemp.append("\"numeroAcaoPenal\":\"" + dados.getNumeroAcaoPenal() + "\",");
			stTemp.append("\"varaOrigem\":\"" + dados.getCidadeOrigem()+ "-" + dados.getEstadoOrigem() + " - " + dados.getVaraOrigem() + "\",");
			stTemp.append("\"dataDistribuicao\":\"" + dados.getDataDistribuicao() + "\",");
			stTemp.append("\"dataPronuncia\":\"" + dados.getDataPronuncia() + "\",");
			stTemp.append("\"dataDenuncia\":\"" + dados.getDataDenuncia() + "\",");
			stTemp.append("\"dataAcordao\":\"" + dados.getDataAcordao() + "\",");
			stTemp.append("\"dataSentenca\":\"" + dados.getDataSentenca() + "\",");
			stTemp.append("\"dataAdmonitoria\":\"" + dados.getDataAdmonitoria() + "\",");
			stTemp.append("\"dataTransitoJulgadoMP\":\"" + dados.getDataTransitoJulgadoMP() + "\",");
			stTemp.append("\"dataTransitoJulgado\":\"" + dados.getDataTransitoJulgado() + "\",");
			stTemp.append("\"dataInicioCumprimentoPena\":\"" + dados.getDataInicioCumprimentoPena() + "\"");
			stTemp.append("}]");
			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return stTemp.toString();	
	}

	public String consultarDataSentenca(String idProcessoExecucao) throws Exception{
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String data = "";

		sql.append("SELECT DATA_SENTENCA FROM PROJUDI.VIEW_PROC_EXE pe");
		sql.append(" WHERE pe.ID_PROC_EXE = ?");
		ps.adicionarLong(idProcessoExecucao);
		try{
			rs = consultar(sql.toString(), ps);
			
			if (rs.next()){
				data = Funcoes.FormatarData(rs.getDateTime("DATA_SENTENCA"));
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 

		return data;
	}
}
