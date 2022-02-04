package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import br.gov.go.tj.projudi.dt.MandadoTipoRedistribuicaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MandadoTipoRedistribuicaoPs extends Persistencia {

	private static final long serialVersionUID = -1721922213034686213L;

	public MandadoTipoRedistribuicaoPs(Connection conexao) {
		Conexao = conexao;
	}

	public List consultaMandadoTipoRedistribuicao() throws Exception {

		List listaTipo = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try {

			sql.append("SELECT * FROM projudi.mand_tipo_redist");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				MandadoTipoRedistribuicaoDt obTemp = new MandadoTipoRedistribuicaoDt();
				obTemp.setMandTipoRedist(rs.getString("mand_tipo_redist"));
				obTemp.setIdMandTipoRedist(rs.getString("id_mand_tipo_redist"));
				listaTipo.add(obTemp);
			}
		}
		
		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaTipo;
	}
}
