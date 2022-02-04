package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PendenciaPromotoriaPs extends PendenciaPsGen {

	private static final long serialVersionUID = 1071739448119938385L;

	public PendenciaPromotoriaPs(Connection conexao) {
		Conexao = conexao;
	}

	public List consultarIntimacoesPromotoriaCompleto(UsuarioNe usuarioNe, String procNum) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT  ");
		sql.append("  	vpip.ID_PEND,  ");
		sql.append("  	vpip.ID_PROC,  ");
		sql.append("  	vpip.PROC_NUMERO_COMPLETO,  ");
		sql.append("  	vpip.ID_MOVI,  ");
		sql.append("  	vpip.MOVI_TIPO,  ");
		sql.append("  	vpip.PEND_TIPO,  ");
		sql.append("  	vpip.DATA_INICIO,  ");
		sql.append("  	vpip.DATA_LIMITE,  ");
		sql.append("  	vpip.PEND_STATUS,  ");
		sql.append("  	vpip.NOME_PARTE,  ");
		sql.append("  	vpip.NOME,  ");
		sql.append("  	vpip.ID_SERV_CARGO,  ");
		sql.append("  	pt.PROC_TIPO,  ");
		sql.append("  	pt.PROC_TIPO_CODIGO  ");
		sql.append("  FROM  ");
		sql.append("  	projudi.VIEW_PEND_INTIMACOES_PROM vpip  ");
		sql.append("  INNER JOIN projudi.PROC p ON   ");
		sql.append("  	p.ID_PROC = vpip.ID_PROC   ");
		sql.append("  INNER JOIN PROC_TIPO pt ON   ");
		sql.append("  	pt.ID_PROC_TIPO = p.ID_PROC_TIPO   ");
		sql.append("  WHERE  ");
		sql.append("  	vpip.ID_SERV = ?  ");
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		sql.append("  	AND ( vpip.CODIGO_TEMP <> ?  ");
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		sql.append("  	OR vpip.CODIGO_TEMP is null ) AND vpip.DATA_FIM IS NULL   ");
		if (StringUtils.isNotBlank(procNum)) {
			sql.append("  	AND PROC_NUMERO_COMPLETO = ?    ");
			ps.adicionarString(procNum);
		}
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				if (pendencias == null)
					pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setNomeParte(rs1.getString("NOME_PARTE"));
				pendenciaDt.setPromotor(rs1.getString("NOME"));
				pendenciaDt.setId_Promotor(rs1.getString("ID_SERV_CARGO"));

				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				pendenciaDt.getProcessoDt().setId(rs1.getString("ID_PROC"));
				pendenciaDt.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.getProcessoDt().setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
				pendencias.add(pendenciaDt);
			}

		} finally {
			if (rs1 != null)
				rs1.close();
		}

		return pendencias;
	}
	
	public PendenciaDt acrescentaProcessoDt(PendenciaDt pendenciaDt)  throws Exception {

		String sql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT * FROM PROJUDI.VIEW_PEND WHERE ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());

		try {
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId(rs1.getString("ID_PROC"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
		return pendenciaDt;
	}

}
