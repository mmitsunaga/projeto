package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.AudienciaProcessoPendenciaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaProcessoPendenciaPs extends Persistencia {

	public AudienciaProcessoPendenciaPs(Connection conexao) {
		this.Conexao = conexao;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8046453660779340521L;

	public String inserir(AudienciaProcessoPendenciaDt dados) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "INSERT INTO AUDI_PROC_PEND(ID_PEND, ID_AUDI_PROC) VALUES (?, ?)";

		ps.adicionarLong(dados.getIdPend());
		ps.adicionarLong(dados.getIdAudienciaProcesso());

		return executarInsert(sql, "ID_AUDI_PROC_PEND", ps);
	}
	
	// jvosantos - 12/09/2019 17:49 - Remover método de inserir lista, a arquitetura não suporta.
	public String consultarPorIdPend(String idPend) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = " SELECT ID_AUDI_PROC FROM AUDI_PROC_PEND WHERE ID_PEND = ? ";
		ps.adicionarLong(idPend);
		
		rs1 = this.consultar(sql, ps);
		
		if(rs1.next()) {
			return rs1.getString("ID_AUDI_PROC");
		}
		
		return null;
	}

	// jvosantos - 06/11/2019 12:20 - Método para buscar a audiencia_processo que tem a pendencia e está em andamento
	public String consultarPorIdPendEmAndamento(String idPend) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = " SELECT APP.ID_AUDI_PROC FROM AUDI_PROC_PEND APP INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = APP.ID_AUDI_PROC WHERE ID_PEND = ? AND AP.ID_AUDI_PROC_STATUS = 1 ";
		ps.adicionarLong(idPend);
		
		rs1 = this.consultar(sql, ps);
		
		if(rs1.next()) {
			return rs1.getString("ID_AUDI_PROC");
		}
		
		return null;
	}
}
