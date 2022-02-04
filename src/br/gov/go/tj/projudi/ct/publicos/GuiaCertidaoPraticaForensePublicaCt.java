package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.GuiaCertidaoPraticaForenseCt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class GuiaCertidaoPraticaForensePublicaCt extends GuiaCertidaoPraticaForenseCt {
	
	private static final long serialVersionUID = 7748801312848043597L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		configure(request, response);
		request.getSession().setAttribute("Cabecalho", true);
		
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
			case Configuracao.Curinga9:
				super.redireciona(response, obtenhaServletPaginaGuia() + "&ServletRedirect="+obtenhaServletDeRetornoPesquisa() + "&TituloDaPagina=" + obtenhaTituloPagina());
				return;					
	    }
	
		super.executar(request, response, UsuarioSessao, paginaatual, nomebusca, posicaopaginaatual);
	}
	
	@Override
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaCertidaoPraticaForense";
	}
	
	@Override
	protected String obtenhaTituloPagina() {
		return "Guia de Certidão de Prática Forense [Acesso Público]";
	}
	
	@Override
	protected String obtenhaServletPaginaGuia() {
		return "GuiaCertidaoPraticaForense?PaginaAtual=" + Configuracao.Novo;
	}
	
	@Override
	protected String obtenhaGuiaTipo(){
		return GuiaTipoDt.ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE;
	}
	
	@Override
	protected String obtenhaServletProximaPagina() {
		return "CertidaoGuia?PaginaAtual=" + Configuracao.Novo;
	}
	
	@Override
	protected String obtenhaAcaoMensagemOk(String mensagem) {
		return obtenhaServletProximaPagina() + "&MensagemOk=" + mensagem;
	}
	
	@Override
	protected String obtenhaAcaoMensagemErro(String mensagem) {
		return obtenhaServletProximaPagina() + "&MensagemErro=" + mensagem;
	}
		
	protected void configure(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("TipoConsulta", "Publica");		
	}	
	
	@Override
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public int Permissao() {
		return 509;
	}	
}
