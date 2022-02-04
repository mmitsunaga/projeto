package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
//---------------------------------------------------------
public class MandadoPrisaoOrigemPs extends MandadoPrisaoOrigemPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -79028506272775633L;
	@SuppressWarnings("unused")
	private MandadoPrisaoOrigemPs( ) {}
	public MandadoPrisaoOrigemPs(Connection conexao){
		Conexao = conexao;
	}
//

}
