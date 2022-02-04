package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;  
import br.gov.go.tj.projudi.dt.EscalaTipoMgDt;
 import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class EscalaTipoMgPs extends Persistencia {

	private static final long serialVersionUID = -7693294546899960439L;

	public EscalaTipoMgPs(Connection conexao) {
		Conexao = conexao;
	}

	public List consultaEscalaTipoMg() throws Exception {

		List listaEscalaTipoMg = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try {
			sql.append("SELECT * FROM  projudi.escala_tipo_mg"
					+ " ORDER BY escala_tipo_mg");
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				EscalaTipoMgDt obTemp = new EscalaTipoMgDt();
				obTemp.setId(rs.getString("id_escala_tipo_mg"));
				obTemp.setCodigoEscalaTipoMg(rs.getString("codigo_escala_tipo_mg"));
				obTemp.setEscalaTipoMg(rs.getString("escala_tipo_mg"));
				listaEscalaTipoMg.add(obTemp);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaEscalaTipoMg;
	}
}
