package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

//---------------------------------------------------------
public class PrisaoTipoPs extends PrisaoTipoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6258616911497998524L;
	
	@SuppressWarnings("unused")
	private PrisaoTipoPs() {		
	}
	
	public PrisaoTipoPs(Connection conexao){
		Conexao = conexao;
	}

//

}
