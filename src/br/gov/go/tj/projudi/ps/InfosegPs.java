package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.relatorios.InfosegDt;
import br.gov.go.tj.utils.Configuracao;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class InfosegPs extends Persistencia{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 5066328894645852766L;

	public InfosegPs(Connection conexao){
		Conexao = conexao;
	}


	/**
	 * Lista os sentenciados com processos com status: Ativo e Cálculo, e liquidação de pena não extinta.
	 * @return lista com os sentenciados
	 * @throws Exception
	 */
	public List consultarDadosSentenciado() throws Exception {
		List lista = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		//lista somente os processo ativos ou de cálculo, que não estão com o status "extinto".
		//atualmente existem os eventoExecucaoStatus: Cumprindo Pena, foragido, Extinto e Não aplica(já foi excluído na view_proc_exec_ultimo_status)
		sql.append(	"SELECT  p.ID_PROC, p.PROC_NUMERO, p.ANO, p.DIGITO_VERIFICADOR, p.FORUM_CODIGO," +
				" pp.NOME, pp.NOME_MAE, pp.NOME_PAI," +
				" cn.CIDADE AS CIDADE_NASCIMENTO, en.UF AS UF_NASCIMENTO," +
				" pp.RG, rg.SIGLA as SIGLA_RG, erg.UF AS UF_RG, pp.CPF," +
				" v.ID_EVENTO_EXE_STATUS, v.EVENTO_EXE_STATUS" +
//				" FROM PROJUDI.VIEW_PROC_EXEC_ULTIMO_STATUS v" +
				" FROM PROJUDI.VIEW_SITUACAO_ATUAL_EXE v" +
				" INNER JOIN PROJUDI.PROC p ON v.ID_PROC = p.ID_PROC" +
				" INNER JOIN PROJUDI.PROC_STATUS ps ON p.ID_PROC_STATUS = ps.ID_PROC_STATUS" +
				" INNER JOIN PROJUDI.PROC_PARTE pp ON p.ID_PROC = pp.ID_PROC" +
				" LEFT JOIN PROJUDI.RG_ORGAO_EXP rg ON pp.ID_RG_ORGAO_EXP = rg.ID_RG_ORGAO_EXP" +
				" LEFT JOIN PROJUDI.ESTADO erg ON erg.ID_ESTADO = rg.ID_ESTADO" +
				" LEFT JOIN PROJUDI.CIDADE cn ON cn.ID_CIDADE = pp.ID_NATURALIDADE" +
				" LEFT JOIN PROJUDI.ESTADO en ON cn.ID_ESTADO = en.ID_ESTADO" +
				" WHERE ps.PROC_STATUS_CODIGO IN (?,?)" +
				" AND v.ID_EVENTO_EXE_STATUS <> ?" );
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoStatusDt.EXTINTO);
		sql.append(" ORDER BY v.ID_PROC");		
		try{
			rs = consultar(sql.toString(), ps);
			int contador = 0;
			while (rs.next()){
				InfosegDt sentenciado = new InfosegDt();
				String numeroCompleto = Funcoes.completarZeros(rs.getString("PROC_NUMERO"), 7) + "." + Funcoes.completarZeros(rs.getString("DIGITO_VERIFICADOR"), 2) + "." + rs.getString("ANO") + "." + Configuracao.JTR + "." + Funcoes.completarZeros(rs.getString("FORUM_CODIGO"), 4);
				sentenciado.setNumeroCompletoProcesso(numeroCompleto);
				sentenciado.setIdProcesso(rs.getString("ID_PROC"));
				if (rs.getString("NOME") != null) sentenciado.setNomeSentenciado(rs.getString("NOME"));
				if (rs.getString("NOME_MAE") != null) sentenciado.setNomeMae(rs.getString("NOME_MAE"));
				if (rs.getString("NOME_PAI") != null) sentenciado.setNomePai(rs.getString("NOME_PAI"));
				if (rs.getString("CIDADE_NASCIMENTO") != null) sentenciado.setCidadeNascimento(rs.getString("CIDADE_NASCIMENTO"));
				if (rs.getString("UF_NASCIMENTO") != null) sentenciado.setUfNascimento(rs.getString("UF_NASCIMENTO"));
				if (rs.getString("RG") != null) sentenciado.setNumeroRg(rs.getString("RG"));
				if (rs.getString("SIGLA_RG") != null && rs.getString("UF_RG") != null) sentenciado.setSiglaRg(rs.getString("SIGLA_RG") + "-" + rs.getString("UF_RG"));
				if (rs.getString("CPF") != null) sentenciado.setCpf(rs.getString("CPF"));
				if (rs.getString("ID_EVENTO_EXE_STATUS").equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA)))
					sentenciado.setForagido("Não");
				else sentenciado.setForagido("Sim");
				contador++;
				sentenciado.setTempoPenaAno(String.valueOf(contador));
				lista.add(sentenciado);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	
	/**
	 * Lista os processos com status: Ativo e Cálculo, e liquidação de pena não extinta, com o tempo total de condenação
	 * @return lista com os sentenciados
	 * @throws Exception
	 */
	public List consultarDadosProcesso() throws Exception {
		List lista = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		//lista somente os processo ativos ou de cálculo, que não estão com o status "extinto", com o respectivo total da condenação
		//atualmente existem os eventoExecucaoStatus: Cumprindo Pena, foragido, Extinto e Não aplica(já foi excluído na view_proc_exec_ultimo_status)
		sql.append("SELECT p.ID_PROC, p.PROC_NUMERO, p.ANO, p.DIGITO_VERIFICADOR, p.FORUM_CODIGO, sum(ce.TEMPO_PENA) AS TOTAL_CONDENACAO" +
//				" FROM PROJUDI.VIEW_PROC_EXEC_ULTIMO_STATUS v" +
				" FROM PROJUDI.VIEW_SITUACAO_ATUAL_EXE v" +
				" INNER JOIN PROJUDI.PROC p ON v.ID_PROC = p.ID_PROC" +
				" INNER JOIN PROJUDI.PROC_STATUS ps ON p.ID_PROC_STATUS = ps.ID_PROC_STATUS" +
				" INNER JOIN PROJUDI.PROC_EXE pe ON pe.ID_PROC_EXE_PENAL = p.ID_PROC" +
				" INNER JOIN PROJUDI.CONDENACAO_EXE ce ON pe.ID_PROC_EXE = ce.ID_PROC_EXE" +
				" WHERE ps.PROC_STATUS_CODIGO IN (?,?) " + //--processo ativo e cálculo
				" AND v.ID_EVENTO_EXE_STATUS <> ? " + //--não extinto
				" AND (pe.CODIGO_TEMP = ? OR pe.CODIGO_TEMP IS NULL) " + //--processo execução ativo
				" GROUP BY p.ID_PROC, p.PROC_NUMERO, p.ANO, p.DIGITO_VERIFICADOR, p.FORUM_CODIGO" +
				" ORDER BY p.ID_PROC");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoStatusDt.EXTINTO);
		ps.adicionarLong(1);
		
		try{
			rs = consultar(sql.toString(), ps);
			int contador = 0;
			while (rs.next()){
				InfosegDt sentenciado = new InfosegDt();
				
				String numeroCompleto = Funcoes.completarZeros(rs.getString("PROC_NUMERO"), 7) + "." + Funcoes.completarZeros(rs.getString("DIGITO_VERIFICADOR"), 2) + "." + rs.getString("ANO") + "." + Configuracao.JTR + "." + Funcoes.completarZeros(rs.getString("FORUM_CODIGO"), 4);
				sentenciado.setNumeroCompletoProcesso(numeroCompleto);
				sentenciado.setIdProcesso(rs.getString("ID_PROC"));
				
				String pena = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(rs.getString("TOTAL_CONDENACAO")));
				String[] temp = pena.split(" - ");
				sentenciado.setTempoPenaAno(temp[0]);
				sentenciado.setTempoPenaMes(temp[1]);
				sentenciado.setTempoPenaDia(temp[2]);
				contador++;
				sentenciado.setCpf(String.valueOf(contador));
				lista.add(sentenciado);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
}