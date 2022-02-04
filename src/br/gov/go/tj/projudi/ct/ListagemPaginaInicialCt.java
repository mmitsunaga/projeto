package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ListagemPaginaInicialDt;
import br.gov.go.tj.projudi.dt.relatorios.ResultadoRelatorioDt;
import br.gov.go.tj.projudi.ne.ListagemPaginaInicialNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class ListagemPaginaInicialCt extends Controle {
	
	private static final long serialVersionUID = 8552710771245949358L;

	public int Permissao(){
		return ListagemPaginaInicialDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ListagemPaginaInicialNe listagemPaginaInicialNe;		
		
		listagemPaginaInicialNe =(ListagemPaginaInicialNe)request.getSession().getAttribute("listagemPaginaInicialNe");
		if (listagemPaginaInicialNe == null )  listagemPaginaInicialNe = new ListagemPaginaInicialNe();  	

		request.setAttribute("tempPrograma","ListagemPaginaInicial");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.Imprimir:
				ResultadoRelatorioDt relatorio = listagemPaginaInicialNe.consultarRelatorioDeIntimacoesPendentes(UsuarioSessao);
				super.enviarParaDownload(response, relatorio);				
				break;			
		default:
			throw new MensagemException("Listagem não configurada.");			
		}		
	}
}
