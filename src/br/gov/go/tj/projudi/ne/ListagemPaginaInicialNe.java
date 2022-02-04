package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.relatorios.ResultadoRelatorioDt;

public class ListagemPaginaInicialNe extends Negocio {	

	private static final long serialVersionUID = -5731527387232834401L;

	public ResultadoRelatorioDt consultarRelatorioDeIntimacoesPendentes(UsuarioNe usuarioSessao) throws Exception {
		RelatoriosDiversosNe relatoriosDiversosNe = new RelatoriosDiversosNe();
		return relatoriosDiversosNe.consultarRelatorioDeIntimacoesPendentes(usuarioSessao);
	}
}
