package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoFaixaValorDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class MandadoFaixaValorPs extends MandadoFaixaValorPsGen {

	private static final long serialVersionUID = -8403883258287434276L;

	public MandadoFaixaValorPs(Connection conexao) {
		Conexao = conexao;
	}

	public List consultaMandadoFaixaValorPorData(String dataReferencia)

			throws Exception {

		List listaValores = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("select faixa_inicio, faixa_fim, valor, tipo_valor from projudi.mandado_faixa_valor"
				+ " WHERE data_vigencia_inicio < = ? AND data_vigencia_fim is null AND"
				+ " tipo_valor in('VALOR FAIXA UNICA', 'VALOR ACIMA FAIXA', 'VALOR RESOLUTIVO', 'CENOPES ACIMA FAIXA')"
				+ " ORDER BY id_mandado_faixa_valor");
		ps.adicionarDateTimePrimeiraHoraDia(dataReferencia);
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				MandadoFaixaValorDt obTemp = new MandadoFaixaValorDt();

				obTemp.setFaixaInicio(rs.getString("faixa_inicio"));
				obTemp.setFaixaFim(rs.getString("faixa_fim"));
				obTemp.setValor(rs.getString("valor"));
				obTemp.setTipoValor(rs.getString("tipo_valor"));
				listaValores.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaValores;
	}

}
