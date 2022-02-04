package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
//---------------------------------------------------------
public class EventoTipoPs extends EventoTipoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6432046737647092366L;
	private EventoTipoPs( ) {}
	public EventoTipoPs(Connection conexao) {
		Conexao = conexao;
	}

}
