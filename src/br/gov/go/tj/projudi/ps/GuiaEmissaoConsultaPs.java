package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

public class GuiaEmissaoConsultaPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5344125588792022303L;
	
	public GuiaEmissaoConsultaPs(Connection conexao){
		Conexao = conexao;
	}

}
