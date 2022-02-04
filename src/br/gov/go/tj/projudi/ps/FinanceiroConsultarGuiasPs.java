package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

public class FinanceiroConsultarGuiasPs extends Persistencia {

	private static final long serialVersionUID = 4251302101951638956L;

	public FinanceiroConsultarGuiasPs(Connection conexao){
		Conexao = conexao;
	}
}
