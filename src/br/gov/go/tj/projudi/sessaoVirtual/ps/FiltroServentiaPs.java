package br.gov.go.tj.projudi.sessaoVirtual.ps;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class FiltroServentiaPs extends Persistencia {

	private static final long serialVersionUID = 8671457555159348029L;

	public FiltroServentiaPs(Connection conexao) {
		Conexao = conexao;
	}

//	public Optional<List<ServentiaDt>> consultarListaServentiaMinutaNaoAnalisada(String posicaoPaginaAtual,
//			String idServCargo, Boolean isIniciada) throws Exception {
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		List<ServentiaDt> listaTemp = new ArrayList<ServentiaDt>();
//		StringBuilder sql = new ServentiaPs(Conexao).obtemSQLConsultaServentiaMinutaNaoAnalisada(idServCargo,
//				isIniciada, ps);
//		try {
//			rs1 = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
//			while (rs1.next()) {
//				ServentiaDt serventia = new ServentiaDt();
//				serventia.setId(rs1.getString("ID"));
//				serventia.setServentia(rs1.getString("DESCRICAO1"));
//				listaTemp.add(serventia);
//			}
//		} finally {
//			try {
//				if (rs1 != null)
//					rs1.close();
//			} catch (SQLException e) {
//				throw new RuntimeException(e);
//			}
//		}
//		return Optional.of(listaTemp);
//	}

//	public Optional<List<ServentiaDt>> consultarServentiaVoto(String posicaoPaginaAtual, String idServCargo,
//			Integer pendStatusCodigo, Integer codigoTemp) throws Exception {
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		List<ServentiaDt> listaTemp = new ArrayList<ServentiaDt>();
//		String sql = obtemSqlConsultarServentiaVoto(idServCargo, pendStatusCodigo, codigoTemp, ps);
//		try {
//			rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);
//			while (rs1.next()) {
//				ServentiaDt serventia = new ServentiaDt();
//				serventia.setId(rs1.getString("ID"));
//				serventia.setServentia(rs1.getString("DESCRICAO1"));
//				listaTemp.add(serventia);
//			}
//		} finally {
//			try {
//				if (rs1 != null)
//					rs1.close();
//			} catch (SQLException e) {
//				throw new RuntimeException(e);
//			}
//		}
//		return Optional.of(listaTemp);
//	}
//
//	public String obtemSqlConsultarServentiaVoto(String id_ServentiaCargo, Integer pendStatusCodigo,
//			Integer codigoTemp, PreparedStatementTJGO ps) throws Exception {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" SELECT DISTINCT S.ID_SERV AS ID, S.SERV AS DESCRICAO1");
//		sql.append(" FROM PROJUDI.VIEW_PEND P ");
//		sql.append(" INNER JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
//		sql.append(" INNER JOIN PROJUDI.AUDI_PROC AP ON P.ID_PROC = AP.ID_PROC ");
//		sql.append(" INNER JOIN PROJUDI.AUDI A ON A.ID_AUDI = AP.ID_AUDI");
//		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON (APP.ID_PEND = P.ID_PEND AND AP.ID_AUDI_PROC = APP.ID_AUDI_PROC)");
//		sql.append(" INNER JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AP.ID_SERV_CARGO");
//		sql.append(" INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO");
//		sql.append(" INNER JOIN SERV S ON S.ID_SERV = A.ID_SERV");
//		sql.append(" WHERE ");
//		sql.append(" P.PEND_TIPO_CODIGO = ?");
//		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
//		sql.append(" AND PR.ID_SERV_CARGO = ?");
//		ps.adicionarLong(id_ServentiaCargo);
//		sql.append(" AND P.PEND_STATUS_CODIGO = ? ");
//		ps.adicionarLong(pendStatusCodigo);
//		if (codigoTemp != null) {
//			sql.append("AND P.CODIGO_TEMP = ? ");
//			ps.adicionarLong(codigoTemp);
//		} else {
//			sql.append(" AND AP.DATA_MOVI IS NULL");
//		}
//		return sql.toString();
//	}

}
