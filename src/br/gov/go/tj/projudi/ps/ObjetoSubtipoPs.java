package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
//---------------------------------------------------------
public class ObjetoSubtipoPs extends ObjetoSubtipoPsGen{

	private ObjetoSubtipoPs( ) {}
	public ObjetoSubtipoPs(Connection conexao) {
		Conexao = conexao;
	}
//

}
