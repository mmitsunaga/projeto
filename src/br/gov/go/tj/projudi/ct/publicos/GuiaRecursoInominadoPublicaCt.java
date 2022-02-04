package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.GuiaRecursoInominadoCt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class GuiaRecursoInominadoPublicaCt extends GuiaRecursoInominadoCt {

	private static final long serialVersionUID = -1311914059496066015L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		configure(request, response);
		
		switch (paginaatual) {
		 	case Configuracao.Salvar:
	        case Configuracao.Excluir:
	            // é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
	            if (UsuarioSessao != null) request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
	            break;
	        case Configuracao.SalvarResultado:
	        case Configuracao.ExcluirResultado:
	            if ((UsuarioSessao != null) && !UsuarioSessao.verificarPedido(request.getParameter("__Pedido__")))
	            	throw new MensagemException("<{Pedido enviado mais de um vez.}> Local Exception: " + this.getClass().getName() + ".doPost()");
	            break;	        
			case Configuracao.Curinga9 :
				super.redireciona(response, "BuscaProcessoPublica?PaginaAtual=" + Configuracao.Novo + "&ServletRedirect="+obtenhaServletDeRetornoPesquisa() + "&TituloDaPagina=" + obtenhaTituloPagina());
				return;					
	    }
	
		super.executar(request, response, UsuarioSessao, paginaatual, nomebusca, posicaopaginaatual);
	}
	
	@Override
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaRecursoInominadoPublica";
	}
	
	@Override
	protected String obtenhaServletBuscaProcesso() {
		return "BuscaProcessoPublica";
	}
	
	@Override
	protected String obtenhaTituloPagina() {
		return "Guia de Recurso Inominado [Acesso Público]";
	}
	
	@Override
	protected String obtenhaAcaoMensagemOk(ProcessoDt processoDt, String mensagem) {
		return obtenhaServletDeRetornoPesquisa()+"?PaginaAtual="+Configuracao.Novo+"&MensagemOk="+mensagem;
	}
	
	@Override
	protected String obtenhaAcaoMensagemErro(ProcessoDt processoDt, String mensagem) {
		return obtenhaServletDeRetornoPesquisa()+"?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+mensagem;
	}
	
	protected void configure(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("TipoConsulta", "Publica");
		
		request.setAttribute("GuiaRecursoInominadoPublica", "GuiaRecursoInominadoPublica");
	}	
	
	@Override
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public int Permissao() {
		//Usando a mesma permissão da guia de postagem pública
		return 878;
	}
}
