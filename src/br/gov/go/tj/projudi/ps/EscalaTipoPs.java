package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
//---------------------------------------------------------
public class EscalaTipoPs extends EscalaTipoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8269587827498071113L;
	private EscalaTipoPs( ) {}
	public EscalaTipoPs(Connection conexao) {
		Conexao = conexao;
	}
//

}
