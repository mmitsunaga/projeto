package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.GraficoMovimentacaoAdvogadoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.GraficoMovimentacaoAdvogadoDt.EnumDataRelatorio;
import br.gov.go.tj.projudi.dt.relatorios.GraficoMovimentacaoAdvogadoItemDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class GraficoMovimentacaoAdvogadoPs extends Persistencia{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2065867704077677675L;	
	    
	public GraficoMovimentacaoAdvogadoPs(Connection conexao){
		Conexao = conexao;
	}	

	/**
	 * Retorna os dados das movimentações dos advogados.
	 * 
	 * @param graficoMovimentacaoAdvogadoDt
	 * @return
	 * @throws Exception
	 */
	public List graficoMovimentacoesAdvogado(GraficoMovimentacaoAdvogadoDt graficoMovimentacaoAdvogadoDt) throws Exception {

		List resultado = new ArrayList();		
		String stSql = new String();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Map<String, GraficoMovimentacaoAdvogadoItemDt> resultadoDataAnalise = null;
		Map<String, GraficoMovimentacaoAdvogadoItemDt> resultadoDataComparacao = null;
		Map<String, GraficoMovimentacaoAdvogadoItemDt> resultadoDataAdicional001 = null;
		Map<String, GraficoMovimentacaoAdvogadoItemDt> resultadoDataAdicional002 = null;
		Map<String, GraficoMovimentacaoAdvogadoItemDt> resultadoDataAdicional003 = null;
		
		stSql = " SELECT DATA_REALIZACAO, COUNT(1) QUANTIDADE ";
		stSql += " FROM ( ";
		stSql += " SELECT ID_MOVI, TO_CHAR(M.DATA_REALIZACAO, 'DD/MM/YYYY HH24:MI') || ':00' AS DATA_REALIZACAO ";
		stSql += " FROM PROJUDI.MOVI M  ";
		stSql += "  INNER JOIN PROJUDI.MOVI_TIPO MT ON M.ID_MOVI_TIPO = MT.ID_MOVI_TIPO ";
		stSql += "  INNER JOIN PROJUDI.USU_SERV US ON US.ID_USU_SERV = M.ID_USU_REALIZADOR ";
		stSql += "  INNER JOIN PROJUDI.SERV S ON S.ID_SERV = US.ID_SERV ";
		stSql += "  INNER JOIN PROJUDI.SERV_TIPO ST ON ST.ID_SERV_TIPO = S.ID_SERV_TIPO ";
		stSql += " WHERE (DATA_REALIZACAO BETWEEN ? AND ? "; 
		graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().atualizePrimeiraHoraDia();
		ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao());		
		graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().atualizeUltimaHoraDia();
		ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao());
		stSql += " OR DATA_REALIZACAO BETWEEN ? AND ? ";
		graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().atualizePrimeiraHoraDia();
		ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise());		
		graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().atualizeUltimaHoraDia();
		ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise());
		
		if (graficoMovimentacaoAdvogadoDt.getDataAdicional001() != null)
		{
			stSql += " OR DATA_REALIZACAO BETWEEN ? AND ? ";
			graficoMovimentacaoAdvogadoDt.getDataAdicional001().atualizePrimeiraHoraDia();
			ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataAdicional001());		
			graficoMovimentacaoAdvogadoDt.getDataAdicional001().atualizeUltimaHoraDia();
			ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataAdicional001());
		}
		
		if (graficoMovimentacaoAdvogadoDt.getDataAdicional002() != null)
		{
			stSql += " OR DATA_REALIZACAO BETWEEN ? AND ? ";
			graficoMovimentacaoAdvogadoDt.getDataAdicional002().atualizePrimeiraHoraDia();
			ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataAdicional002());		
			graficoMovimentacaoAdvogadoDt.getDataAdicional002().atualizeUltimaHoraDia();
			ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataAdicional002());
		}
		
		if (graficoMovimentacaoAdvogadoDt.getDataAdicional003() != null)
		{
			stSql += " OR DATA_REALIZACAO BETWEEN ? AND ? ";
			graficoMovimentacaoAdvogadoDt.getDataAdicional003().atualizePrimeiraHoraDia();
			ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataAdicional003());		
			graficoMovimentacaoAdvogadoDt.getDataAdicional003().atualizeUltimaHoraDia();
			ps.adicionarDateTime(graficoMovimentacaoAdvogadoDt.getDataAdicional003());
		}
		
		stSql += ") AND ST.SERV_TIPO_CODIGO = ? ";
		ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
		stSql += " ) ";
		stSql += " GROUP BY DATA_REALIZACAO ";
		stSql += " ORDER BY DATA_REALIZACAO ";
		
		try{
			resultadoDataAnalise = ObtenhaMapDeMovimentacoes(graficoMovimentacaoAdvogadoDt, EnumDataRelatorio.MOVIMENTACAO_ANALISE);
			resultadoDataComparacao = ObtenhaMapDeMovimentacoes(graficoMovimentacaoAdvogadoDt, EnumDataRelatorio.MOVIMENTACAO_COMPARACAO);
			if (graficoMovimentacaoAdvogadoDt.getDataAdicional001() != null) resultadoDataAdicional001 = ObtenhaMapDeMovimentacoes(graficoMovimentacaoAdvogadoDt, EnumDataRelatorio.ADICIONAL_001);
			if (graficoMovimentacaoAdvogadoDt.getDataAdicional002() != null) resultadoDataAdicional002 = ObtenhaMapDeMovimentacoes(graficoMovimentacaoAdvogadoDt, EnumDataRelatorio.ADICIONAL_002);
			if (graficoMovimentacaoAdvogadoDt.getDataAdicional003() != null) resultadoDataAdicional003 = ObtenhaMapDeMovimentacoes(graficoMovimentacaoAdvogadoDt, EnumDataRelatorio.ADICIONAL_003);
			rs = consultar(stSql.toString(), ps);
			while (rs.next()) {
				TJDataHora dataHoraMovimentacao = new TJDataHora();
				dataHoraMovimentacao.setDataddMMaaaaHHmmss(rs.getString("DATA_REALIZACAO"));
				GraficoMovimentacaoAdvogadoItemDt item = null;
				String chaveHoraFormatada = "";
				if (dataHoraMovimentacao.getHora() < 10)
					chaveHoraFormatada = String.format("0%s:", dataHoraMovimentacao.getHora());
				else 
					chaveHoraFormatada = String.format("%s:", dataHoraMovimentacao.getHora());
				
				if (dataHoraMovimentacao.getMinuto() < 30)
					chaveHoraFormatada += "00";
				else
					chaveHoraFormatada += "30";				
				
				if (dataHoraMovimentacao.getAno() == graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getAno() && dataHoraMovimentacao.getMes() == graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getMes() && dataHoraMovimentacao.getDia() == graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDia())
					item = resultadoDataAnalise.get(chaveHoraFormatada);
				else if (dataHoraMovimentacao.getAno() == graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getAno() && dataHoraMovimentacao.getMes() == graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getMes() && dataHoraMovimentacao.getDia() == graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDia())
					item = resultadoDataComparacao.get(chaveHoraFormatada);
				else if (graficoMovimentacaoAdvogadoDt.getDataAdicional001() != null && dataHoraMovimentacao.getAno() == graficoMovimentacaoAdvogadoDt.getDataAdicional001().getAno() && dataHoraMovimentacao.getMes() == graficoMovimentacaoAdvogadoDt.getDataAdicional001().getMes() && dataHoraMovimentacao.getDia() == graficoMovimentacaoAdvogadoDt.getDataAdicional001().getDia())
					item = resultadoDataAdicional001.get(chaveHoraFormatada);
				else if (graficoMovimentacaoAdvogadoDt.getDataAdicional002() != null && dataHoraMovimentacao.getAno() == graficoMovimentacaoAdvogadoDt.getDataAdicional002().getAno() && dataHoraMovimentacao.getMes() == graficoMovimentacaoAdvogadoDt.getDataAdicional002().getMes() && dataHoraMovimentacao.getDia() == graficoMovimentacaoAdvogadoDt.getDataAdicional002().getDia())
					item = resultadoDataAdicional002.get(chaveHoraFormatada);
				else if (graficoMovimentacaoAdvogadoDt.getDataAdicional003() != null && dataHoraMovimentacao.getAno() == graficoMovimentacaoAdvogadoDt.getDataAdicional003().getAno() && dataHoraMovimentacao.getMes() == graficoMovimentacaoAdvogadoDt.getDataAdicional003().getMes() && dataHoraMovimentacao.getDia() == graficoMovimentacaoAdvogadoDt.getDataAdicional003().getDia())
					item = resultadoDataAdicional003.get(chaveHoraFormatada);
				
				if (item != null)
					item.setQuantidade(rs.getInt("QUANTIDADE"));
			}
			
			for(GraficoMovimentacaoAdvogadoItemDt item : resultadoDataAnalise.values())
				resultado.add(item);
			
			for(GraficoMovimentacaoAdvogadoItemDt item : resultadoDataComparacao.values())
				resultado.add(item);
			
			if (graficoMovimentacaoAdvogadoDt.getDataAdicional001() != null)
			{
				for(GraficoMovimentacaoAdvogadoItemDt item : resultadoDataAdicional001.values())
					resultado.add(item);
			}
			
			if (graficoMovimentacaoAdvogadoDt.getDataAdicional002() != null)
			{
				for(GraficoMovimentacaoAdvogadoItemDt item : resultadoDataAdicional002.values())
					resultado.add(item);
			}
			
			if (graficoMovimentacaoAdvogadoDt.getDataAdicional003() != null)
			{
				for(GraficoMovimentacaoAdvogadoItemDt item : resultadoDataAdicional003.values())
					resultado.add(item);
			}
			
			// Ordenação por data e hora...
			Collections.sort(resultado);			

		
		} finally{
		   try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		
		return resultado;
	}
	
	private Map<String, GraficoMovimentacaoAdvogadoItemDt> ObtenhaMapDeMovimentacoes(GraficoMovimentacaoAdvogadoDt graficoMovimentacaoAdvogadoDt, EnumDataRelatorio enumDataRelatorio)
	{
		Map<String, GraficoMovimentacaoAdvogadoItemDt> resultadoData = new HashMap<String, GraficoMovimentacaoAdvogadoItemDt>();
		
		String horaFormatada = "";
		for (Integer i=0; i<=23; i++){
			GraficoMovimentacaoAdvogadoItemDt item = new GraficoMovimentacaoAdvogadoItemDt();
			GraficoMovimentacaoAdvogadoItemDt item2 = new GraficoMovimentacaoAdvogadoItemDt();
						
			item.setDataAnalise(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDataFormatadaddMMyyyy());
			item2.setDataAnalise(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDataFormatadaddMMyyyy());
			item.setDataComparacao(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDataFormatadaddMMyyyy());
			item2.setDataComparacao(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDataFormatadaddMMyyyy());			
			
			if (i < 10)
				horaFormatada = String.format("0%s:", i);			
			else
				horaFormatada = String.format("%s:", i);
			
			item.setHora(horaFormatada+"00");
			item2.setHora(horaFormatada+"30");			
			
			if (enumDataRelatorio == EnumDataRelatorio.MOVIMENTACAO_ANALISE)	
			{
				item.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDataFormatadaddMMyyyy());
				item2.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDataFormatadaddMMyyyy());				
			}
			else if (enumDataRelatorio == EnumDataRelatorio.MOVIMENTACAO_COMPARACAO)
			{
				item.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDataFormatadaddMMyyyy());
				item2.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDataFormatadaddMMyyyy());				
			}
			else if (enumDataRelatorio == EnumDataRelatorio.ADICIONAL_001) 
			{
				item.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataAdicional001().getDataFormatadaddMMyyyy());
				item2.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataAdicional001().getDataFormatadaddMMyyyy());				
			}
			else if (enumDataRelatorio == EnumDataRelatorio.ADICIONAL_002)
			{
				item.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataAdicional002().getDataFormatadaddMMyyyy());
				item2.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataAdicional002().getDataFormatadaddMMyyyy());				
			}
			else if (enumDataRelatorio == EnumDataRelatorio.ADICIONAL_003)
			{
				item.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataAdicional003().getDataFormatadaddMMyyyy());
				item2.setEstatisticaMovimentacaoItem(graficoMovimentacaoAdvogadoDt.getDataAdicional003().getDataFormatadaddMMyyyy());				
			}
			
			resultadoData.put(item.getHora(), item);
			resultadoData.put(item2.getHora(), item2);
			
			if (i == 23)
			{
				GraficoMovimentacaoAdvogadoItemDt item3 = new GraficoMovimentacaoAdvogadoItemDt();
				item3.setDataAnalise(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDataFormatadaddMMyyyy());
				item3.setDataComparacao(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDataFormatadaddMMyyyy());
				item3.setHora("23:59");
				item3.setEstatisticaMovimentacaoItem(item2.getEstatisticaMovimentacaoItem());
				
				resultadoData.put(item3.getHora(), item3);
			}
		}	
		
		return resultadoData;
	}
}