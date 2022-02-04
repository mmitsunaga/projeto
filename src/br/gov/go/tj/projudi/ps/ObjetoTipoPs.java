package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
//---------------------------------------------------------
public class ObjetoTipoPs extends ObjetoTipoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4771936891944097336L;
	private ObjetoTipoPs( ) {}
	public ObjetoTipoPs(Connection conexao) {
		Conexao = conexao;
	}
//

}
