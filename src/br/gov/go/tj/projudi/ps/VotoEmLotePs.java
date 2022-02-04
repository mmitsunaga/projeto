package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class VotoEmLotePs extends Persistencia{

	private static final long serialVersionUID = -6020367591508399965L;
	
	public VotoEmLotePs(Connection conexao) {
		Conexao = conexao;
	}

	
	public List<VotoSessaoLocalizarDt> consultaVotosPreAnaliseEmLoteOtimizado(String idServentiaCargo,
			int statusPendencia,
			int tipoPendencia,
			ArquivoPs arquivoPs,
			UsuarioNe usuarioSessao
			) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" 	* ");
		sql.append(" FROM ");
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		DISTINCT AP.ID_PROC, ");
		sql.append(" 		A.DATA_AGENDADA, ");
		sql.append(" 		VP.PROC_NUMERO_COMPLETO, ");
		sql.append(" 		P.ID_PEND, ");
		sql.append(" 		AP.ID_AUDI_PROC, ");
		sql.append(" 		U.NOME, ");
		sql.append(" 		C.ID_CLASSIFICADOR ID_CLASSIFICADOR_PEND, ");
		sql.append(" 		C.CLASSIFICADOR CLASSIFICADOR_PEND, ");
		sql.append(" 		C1.ID_CLASSIFICADOR ID_CLASSIFICADOR_PROC, ");
		sql.append(" 		C1.CLASSIFICADOR CLASSIFICADOR_PROC, ");
		sql.append(" 		PT.PROC_TIPO, ");
		sql.append(" 		PA.ID_ARQ, ");
		sql.append(" 		( ");
		sql.append(" 		SELECT ");
		sql.append(" 			DISTINCT PT1.PROC_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			AUDI_PROC AP1 ");
		sql.append(" 		INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON ");
		sql.append(" 			RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC ");
		sql.append(" 		INNER JOIN PROC_TIPO PT1 ON ");
		sql.append(" 			PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
		sql.append(" 		WHERE ");
		sql.append(" 			AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 			AND ROWNUM = 1) PROC_TIPO_REC_SEC , ");
		sql.append(" 		( ");
		sql.append(" 		SELECT ");
		sql.append(" 			1 ");
		sql.append(" 		FROM ");
		sql.append(" 			RECURSO R ");
		sql.append(" 		INNER JOIN AUDI_PROC AP2 ON ");
		sql.append(" 			AP2.ID_PROC = R.ID_PROC ");
		sql.append(" 		WHERE ");
		sql.append(" 			AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 			AND ROWNUM = 1) POSSUI_RECURSO ");
		sql.append(" 	FROM ");
		sql.append(" 		PROJUDI.PEND P ");
		sql.append(" 	INNER JOIN PROJUDI.VIEW_PEND VP ON ");
		sql.append(" 		VP.ID_PEND = P.ID_PEND ");
		sql.append(" 	INNER JOIN PROJUDI.PEND_VOTO_VIRTUAL PV ON ");
		sql.append(" 		PV.ID_PEND = P.ID_PEND ");
		sql.append(" 	INNER JOIN PROJUDI.PEND_ARQ PA ON ");
		sql.append(" 		PA.ID_PEND = PV.ID_PEND ");
		sql.append(" 	INNER JOIN PROC PROC ON ");
		sql.append(" 		PROC.ID_PROC = P.ID_PROC ");
		sql.append(" 	LEFT JOIN CLASSIFICADOR C ON ");
		sql.append(" 		C.ID_CLASSIFICADOR = P.ID_CLASSIFICADOR ");
		sql.append(" 	LEFT JOIN CLASSIFICADOR C1 ON ");
		sql.append(" 		C1.ID_CLASSIFICADOR = PROC.ID_CLASSIFICADOR ");
		sql.append(" 	JOIN PROJUDI.PEND_RESP PR ON ");
		sql.append(" 		P.ID_PEND = PR.ID_PEND ");
		sql.append(" 	INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON ");
		sql.append(" 		APP.ID_PEND = P.ID_PEND ");
		sql.append(" 	INNER JOIN AUDI_PROC AP ON ");
		sql.append(" 		AP.ID_AUDI_PROC = PV.ID_AUDI_PROC ");
		sql.append(" 	JOIN PROJUDI.PROC_TIPO PT ON ");
		sql.append(" 		PT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" 	LEFT JOIN AUDI A ON ");
		sql.append(" 		A.ID_AUDI = AP.ID_AUDI ");
		sql.append(" 	LEFT JOIN SERV_CARGO SC	ON ");
		sql.append(" 		AP.ID_SERV_CARGO = SC.ID_SERV_CARGO ");
		sql.append("   	LEFT JOIN USU_SERV_GRUPO USG ON ");
		sql.append("   		SC.ID_USU_SERV_GRUPO = USG.ID_USU_SERV_GRUPO ");
		sql.append("   	LEFT JOIN USU_SERV US ON ");
		sql.append("   		USG.ID_USU_SERV = US.ID_USU_SERV ");
		sql.append("   	LEFT JOIN USU U ON ");
		sql.append("   		US.ID_USU = U.ID_USU	 ");
		sql.append(" 	JOIN PEND_STATUS STATUS ON ");
		sql.append(" 		STATUS.ID_PEND_STATUS = P.ID_PEND_STATUS ");
		sql.append(" 	JOIN PEND_TIPO TIP ON ");
		sql.append(" 		P.ID_PEND_TIPO = TIP.ID_PEND_TIPO ");
		sql.append(" 	WHERE ");
		sql.append(" 		PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(idServentiaCargo);
		sql.append(" 		AND STATUS.PEND_STATUS_CODIGO = ? ");
		ps.adicionarLong(statusPendencia);
		sql.append(" 		AND TIP.PEND_TIPO_CODIGO = ? ) MYVIEW ");
		ps.adicionarLong(tipoPendencia);
		sql.append(" WHERE ");
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		COUNT(PA1.ID_ARQ) ");
		sql.append(" 	FROM ");
		sql.append(" 		PROJUDI.PEND_ARQ PA1 ");  
		sql.append(" 	WHERE ");  
		sql.append(" 		PA1.ID_ARQ = MYVIEW.ID_ARQ ");  
		sql.append(" 	GROUP BY ");  
		sql.append(" 		PA1.ID_ARQ) > 1 ");  
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setArquivoVoto(arquivoPs.consultarId(rs1.getString("ID_ARQ")));
				voto.setHashUsuario(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				voto.setPendenciaDt(new PendenciaDt());
				voto.setProcessoDt(new ProcessoDt());
				voto.getPendenciaDt().setId(rs1.getString("ID_PEND"));
				voto.getPendenciaDt().setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PEND"));
				voto.getPendenciaDt().setClassificador(rs1.getString("CLASSIFICADOR_PEND"));
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME"));
				voto.getProcessoDt().setId(rs1.getString("ID_PROC"));
				voto.getProcessoDt().setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PROC"));
				voto.getProcessoDt().setClassificador(rs1.getString("CLASSIFICADOR_PROC"));
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	public int consultaQtdeVotosPreAnaliseEmLote(String idServCargo, int pendStatusCodigo, int pendTipoCodigo)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT                                       ");
		sql.append(" 	COUNT(*)                                 ");
		sql.append(" FROM                                         ");
		sql.append(" 	(                                        ");
		sql.append(" 	SELECT                                   ");
		sql.append(" 		DISTINCT ");
		sql.append(" 		AP.ID_PROC , ");
		sql.append(" 		A.DATA_AGENDADA , ");
		sql.append(" 		P.PROC_NUMERO_COMPLETO , ");
		sql.append(" 		P.DATA_LIMITE ,                      ");
		sql.append(" 		PA.ID_ARQ                            ");
		sql.append(" 	FROM                                     ");
		sql.append(" 		PROJUDI.PEND_VOTO_VIRTUAL PV         ");
		sql.append(" 	JOIN PROJUDI.VIEW_PEND P ON              ");
		sql.append(" 		PV.ID_PEND = P.ID_PEND               ");
		sql.append(" 	JOIN PROJUDI.PEND_RESP PR ON             ");
		sql.append(" 		PV.ID_PEND = PR.ID_PEND              ");
		sql.append(" 	JOIN PROJUDI.PEND_ARQ PA ON              ");
		sql.append(" 		PA.ID_PEND = PV.ID_PEND              ");
		sql.append(" 	JOIN PROJUDI.AUDI_PROC AP ON ");
		sql.append(" 		AP.ID_AUDI_PROC = PV.ID_AUDI_PROC  ");
		sql.append(" 	JOIN PROJUDI.AUDI A ON ");
		sql.append(" 		A.ID_AUDI = AP.ID_AUDI  ");
		sql.append(" 	WHERE                                    ");
		sql.append(" 		PR.ID_SERV_CARGO = ?	             ");
		ps.adicionarLong(idServCargo);
		sql.append(" 		AND P.PEND_STATUS_CODIGO = ?         ");
		ps.adicionarLong(pendStatusCodigo);
		sql.append(" 		AND P.PEND_TIPO_CODIGO = ? ) MYVIEW	 ");
		ps.adicionarLong(pendTipoCodigo);
		sql.append(" WHERE                                        ");
		sql.append(" 	(                                        ");
		sql.append(" 	SELECT                                   ");
		sql.append(" 		COUNT(PA1.ID_ARQ)                    ");
		sql.append(" 	FROM                                     ");
		sql.append(" 		PROJUDI.PEND_ARQ PA1                 ");
		sql.append(" 	WHERE                                    ");
		sql.append(" 		PA1.ID_ARQ = MYVIEW.ID_ARQ           ");
		sql.append(" 	GROUP BY                                 ");
		sql.append(" 		PA1.ID_ARQ) > 1                      ");
		ResultSetTJGO rs1 = null;
		int result = 0;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) {
				result = rs1.getInt("COUNT(*)");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return result;
	}
	
	public List<Integer> consultaTiposVoto() throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT vvt.VOTO_VIRTUAL_TIPO_CODIGO FROM VOTO_VIRTUAL_TIPO vvt";
		ResultSetTJGO rs1 = null;
		List<Integer> result = new ArrayList<Integer>();
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				result.add(rs1.getInt("VOTO_VIRTUAL_TIPO_CODIGO"));
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return result;
	}
	
}
