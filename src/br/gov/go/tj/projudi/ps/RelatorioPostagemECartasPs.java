package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioPostagemECartasDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * 
 * @author mmitsunaga
 *
 */
public class RelatorioPostagemECartasPs extends Persistencia {

	private static final long serialVersionUID = 2727448493093372317L;
	
	public RelatorioPostagemECartasPs(Connection conexao) {
		Conexao = conexao;
	}
	
		
	/**
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @param idServentia
	 * @return
	 */
	public List<RelatorioPostagemECartasDt> consultarRelatorioPostagemECarta(String dataInicial, String dataFinal, String idComarca, String idServentia) throws Exception {
		        
		/*select distinct vw.*
		from (
		select vpc.id_serv
		  , vpc.serv
		  , pe.cod_rastreamento
		  , to_char(pe.data_expedicao, 'DD/MM/YYYY') as data_postagem 
		  , vpc.proc_numero
		  , vpc.digito_verificador
		  , vpc.ano
		  , vpc.forum_codigo
		  , e.cep
		  , pe.mao_propria
		from (
		  select p.id_proc, p.id_proc_parte, pc.data_expedicao, pc.cod_rastreamento, pc.mao_propria
		  from projudi.pend p inner join projudi.pend_correios pc on pc.id_pend = p.id_pend
		  where pc.data_expedicao is not null and pc.cod_rastreamento is not null
		  and pc.data_expedicao BETWEEN To_Date('20200801','YYYYMMDD') And To_Date('20200831','YYYYMMDD')
		  union
		  select pf.id_proc, pf.id_proc_parte, pfc.data_expedicao, pfc.cod_rastreamento, pfc.mao_propria
		  from projudi.pend_final pf inner join projudi.pend_final_correios pfc on pfc.id_pend = pf.id_pend
		  where pfc.data_expedicao is not null and pfc.cod_rastreamento is not null
		  and pfc.data_expedicao BETWEEN To_Date('20200801','YYYYMMDD') And To_Date('20200831','YYYYMMDD')
		) pe
		INNER JOIN projudi.view_proc_completo vpc on VPC.ID_PROC = pe.ID_PROC
		INNER JOIN projudi.proc_parte pp ON pe.id_proc_parte = pp.id_proc_parte
		LEFT JOIN projudi.view_endereco e ON pp.id_endereco=e.id_endereco
		) vw 
		order by vw.id_serv, vw.data_postagem desc;*/
		
		List<RelatorioPostagemECartasDt> lista = new ArrayList<>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();	
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT *");
		sql.append(" FROM (");
		sql.append(" 	SELECT VPC.ID_SERV");		
		sql.append("	 , VPC.SERV");
		sql.append("	 , PC.COD_RASTREAMENTO");
		sql.append("	 , TO_CHAR(PC.DATA_EXPEDICAO, 'DD/MM/YYYY') AS DATA_POSTAGEM");      
		sql.append("	 , VPC.PROC_NUMERO");
		sql.append("	 , VPC.DIGITO_VERIFICADOR");
		sql.append("	 , VPC.ANO");
		sql.append("	 , VPC.FORUM_CODIGO");
		sql.append("	 , E1.CEP");
		sql.append("	 , PC.MAO_PROPRIA");	
		sql.append(" 	FROM projudi.PEND P");
		sql.append(" 	INNER JOIN projudi.PEND_CORREIOS PC ON PC.ID_PEND=P.ID_PEND");
		sql.append(" 	INNER JOIN projudi.VIEW_PROC_COMPLETO VPC ON VPC.ID_PROC=P.ID_PROC");
		sql.append(" 	INNER JOIN projudi.SERV S ON VPC.ID_SERV=S.ID_SERV");
		sql.append(" 	INNER JOIN projudi.PROC_PARTE PP ON P.ID_PROC_PARTE = PP.ID_PROC_PARTE");
		sql.append(" 	LEFT JOIN projudi.VIEW_ENDERECO E1 ON PP.ID_ENDERECO=E1.ID_ENDERECO");
		sql.append(" 	WHERE PC.DATA_EXPEDICAO IS NOT NULL AND PC.COD_RASTREAMENTO IS NOT NULL");		
		sql.append(" 	AND PC.DATA_EXPEDICAO BETWEEN ? AND ?");
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		
		if (ValidacaoUtil.isNaoVazio(idComarca)){
			sql.append(" AND S.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
		}
		
		if (ValidacaoUtil.isNaoVazio(idServentia)){
			sql.append(" AND VPC.ID_SERV = ?");
			ps.adicionarLong(idServentia);
		}
		
		sql.append("  UNION");
		
		sql.append("	SELECT VPC.ID_SERV");    		
		sql.append("	 , VPC.SERV");
		sql.append("	 , PFC.COD_RASTREAMENTO");
		sql.append("	 , TO_CHAR(PFC.DATA_EXPEDICAO, 'DD/MM/YYYY') AS DATA_POSTAGEM");
		sql.append("	 , VPC.PROC_NUMERO");
		sql.append("	 , VPC.DIGITO_VERIFICADOR");
		sql.append("	 , VPC.ANO");
		sql.append("	 , VPC.FORUM_CODIGO");
		sql.append("	 , E1.CEP");
		sql.append("	 , PFC.MAO_PROPRIA");		  
		sql.append("	FROM projudi.PEND_FINAL PF");
		sql.append("	INNER JOIN projudi.PEND_FINAL_CORREIOS PFC ON PFC.ID_PEND=PF.ID_PEND");
		sql.append("	INNER JOIN projudi.VIEW_PROC_COMPLETO VPC ON VPC.ID_PROC=PF.ID_PROC");
		sql.append(" 	INNER JOIN projudi.SERV S ON VPC.ID_SERV=S.ID_SERV");		
		sql.append("	INNER JOIN projudi.PROC_PARTE PP ON PF.ID_PROC_PARTE = PP.ID_PROC_PARTE");
		sql.append("	LEFT JOIN projudi.VIEW_ENDERECO E1 ON PP.ID_ENDERECO=E1.ID_ENDERECO");
		sql.append("	WHERE PFC.DATA_EXPEDICAO IS NOT NULL AND PFC.COD_RASTREAMENTO IS NOT NULL");		
		sql.append("	AND PFC.DATA_EXPEDICAO BETWEEN ? AND ?");
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		
		if (ValidacaoUtil.isNaoVazio(idComarca)){
			sql.append(" AND S.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
		}
		
		if (ValidacaoUtil.isNaoVazio(idServentia)){
			sql.append(" AND VPC.ID_SERV = ?");
			ps.adicionarLong(idServentia);
		}
		
		sql.append(") VW"); 
		sql.append(" ORDER BY VW.ID_SERV, VW.DATA_POSTAGEM DESC");																																																																																						
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){				
				ProcessoDt processo = new ProcessoDt();
				processo.setProcessoNumero(rs.getString("PROC_NUMERO"));
				processo.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processo.setAno(rs.getString("ANO"));
				processo.setForumCodigo(rs.getString("FORUM_CODIGO"));				
				RelatorioPostagemECartasDt data = new RelatorioPostagemECartasDt();
				data.setIdServentia(rs.getString("ID_SERV"));
				data.setServentia(rs.getString("SERV"));
				data.setProcessoDt(processo);
				data.setCodigoRastreamento(rs.getString("COD_RASTREAMENTO"));
				data.setDataPostagem(rs.getString("DATA_POSTAGEM"));
				data.setCep(Funcoes.formatarCep(rs.getString("CEP")));
				data.setMaoPropria(rs.getString("MAO_PROPRIA"));
				//data.setQtdeFolhas(rs.getString("QTDE_FOLHAS")); fixado em 1 no método limpar() do DT
				lista.add(data);				
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}		
		return lista;
	}
	
	/**
	 * 
	 * @param codigoRastreamento
	 * @throws Exception
	 */
	public RelatorioPostagemECartasDt consultarDadosProcessoPorCodigoRastreamento(String codigoRastreamento) throws Exception {
		RelatorioPostagemECartasDt data = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct vw.*");
		sql.append(" from (");
		sql.append(" select vpc.id_serv");
		sql.append(" , vpc.serv");
		sql.append(" , pe.cod_rastreamento");
		sql.append(" , to_char(pe.data_expedicao, 'DD/MM/YYYY') as data_postagem"); 
		sql.append(" , vpc.proc_numero");
		sql.append(" , vpc.digito_verificador");
		sql.append(" , vpc.ano");
		sql.append(" , vpc.forum_codigo");
		sql.append(" , e.cep");
		sql.append(" , pe.mao_propria");
		sql.append(" , pp.nome");
		sql.append(" from (");
		sql.append(" 	select p.id_proc, p.id_proc_parte, pc.data_expedicao, pc.cod_rastreamento, pc.mao_propria");
		sql.append(" 	from projudi.pend p inner join projudi.pend_correios pc on pc.id_pend = p.id_pend");
		sql.append("	where pc.data_expedicao is not null and pc.cod_rastreamento is not null");
		sql.append("	and pc.cod_rastreamento = ?");
		ps.adicionarString(codigoRastreamento);
		sql.append("	union");
		sql.append("	select pf.id_proc, pf.id_proc_parte, pfc.data_expedicao, pfc.cod_rastreamento, pfc.mao_propria");
		sql.append("	from projudi.pend_final pf inner join projudi.pend_final_correios pfc on pfc.id_pend = pf.id_pend");
		sql.append("	where pfc.data_expedicao is not null and pfc.cod_rastreamento is not null");
		sql.append("	and pfc.cod_rastreamento = ?");
		ps.adicionarString(codigoRastreamento);
		sql.append(" ) pe");
		sql.append(" INNER JOIN projudi.view_proc_completo vpc on VPC.ID_PROC = pe.ID_PROC");
		sql.append(" INNER JOIN projudi.proc_parte pp ON pe.id_proc_parte = pp.id_proc_parte");
		sql.append(" LEFT JOIN projudi.view_endereco e ON pp.id_endereco=e.id_endereco");
		sql.append(" ) vw ");
		
		try {
			rs = consultar(sql.toString(), ps);			
			if (rs.next()){
				ProcessoDt processo = new ProcessoDt();
				processo.setProcessoNumero(rs.getString("PROC_NUMERO"));
				processo.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processo.setAno(rs.getString("ANO"));
				processo.setForumCodigo(rs.getString("FORUM_CODIGO"));				
				data = new RelatorioPostagemECartasDt();
				data.setIdServentia(rs.getString("ID_SERV"));
				data.setServentia(rs.getString("SERV"));
				data.setProcessoDt(processo);
				data.setCodigoRastreamento(rs.getString("COD_RASTREAMENTO"));
				data.setDataPostagem(rs.getString("DATA_POSTAGEM"));
				data.setCep(Funcoes.formatarCep(rs.getString("CEP")));
				data.setMaoPropria(rs.getString("MAO_PROPRIA"));
				data.setNomeParte(rs.getString("NOME"));				
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return data;
	}
	
}
