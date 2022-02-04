package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.Date;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;

public class ServentiaCargoEscalaStatusHistoricoPs extends Persistencia {
	
	private static final long serialVersionUID = 4973535509113990092L;

	public ServentiaCargoEscalaStatusHistoricoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método de inserção de ServentiaCargoEscalaStatusHistoricoDt
	 * @param String Id_UsuarioServentiaEscala
	 * @param String Id_UsuarioServentiaEscalaStatus
	 * @throws Exception
	 */
	public void inserir(ServentiaCargoEscalaDt serventiaCargoEscalaDt) throws Exception {

		String sql = "INSERT INTO PROJUDI.SERV_CARGO_ESC_HISTORICO (ID_SERV_CARGO, ID_ESC, ID_SERV_CARGO_ESC_STATUS, DATA_VINCULACAO, ID_SERV_CARGO_ESC, DATA_ALTERACAO, ATIVO) VALUES ";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
    	
		sql += " ( ?, ?, ?, ?, ?, ?, ?)";
		ps.adicionarLong(serventiaCargoEscalaDt.getId_ServentiaCargo());
		ps.adicionarLong(serventiaCargoEscalaDt.getId_Escala());
		ps.adicionarLong(serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getId());
		ps.adicionarDateTime(serventiaCargoEscalaDt.getDataVinculacao());
    	ps.adicionarLong(serventiaCargoEscalaDt.getId());
    	ps.adicionarDate(new Date());
    	ps.adicionarLong(serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getAtivo());
    	
		executarInsert(sql, "ID_SERV_CARGO_ESC_HISTORICO", ps);
		
	}
}
