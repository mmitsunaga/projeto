package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.Date;

import br.gov.go.tj.projudi.dt.HistoricoRedistribuicaoMandadosDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;

public class HistoricoRedistribuicaoMandadoPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1127149527387047425L;

	public HistoricoRedistribuicaoMandadoPs(Connection conexao) {
		Conexao = conexao;
	}

	public void cadastraHistoricoRedistribuicao(HistoricoRedistribuicaoMandadosDt objDt) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = "INSERT INTO projudi.hist_redist_mand(data_redist, id_mand_jud, id_usu_serv_ant, id_usu_serv_atual,"
				+ " motivo, id_esc_ant, id_esc_atual, id_mand_tipo_redist) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		ps.adicionarDateTime(new Date());
		ps.adicionarString(objDt.getIdMandJud());
		ps.adicionarString(objDt.getIdUsuServAnt());
		ps.adicionarString(objDt.getIdUsuServAtual());
		ps.adicionarString(objDt.getMotivo());
		ps.adicionarString(objDt.getIdEscAnterior());
		ps.adicionarString(objDt.getIdEscAtual());
		ps.adicionarString(objDt.getIdMandTipoRedist());
		objDt.setId(executarInsert(sql, "ID_HIST_REDIST_MAND", ps));
	}
}
