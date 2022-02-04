package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.ct.GuiaMediacaoCt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaMediacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class GuiaMediacaoPublicaCt extends GuiaMediacaoCt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5269957128426803088L;

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
		return "GuiaMediacaoPublica";
	} 
	
	@Override
	protected String obtenhaServletBuscaProcesso() {
		return "BuscaProcessoPublica";
	}
	
	@Override
	protected String obtenhaTituloPagina() {
		return "Guia de Mediação [Acesso Público]";
	}
	
	@Override
	protected String obtenhaAcaoMensagemOk(ProcessoDt processoDt, String mensagem) {
		return "GuiaMediacaoPublica?PaginaAtual="+Configuracao.Novo+"&MensagemOk="+mensagem;
	}
	
	@Override
	protected String obtenhaAcaoMensagemErro(ProcessoDt processoDt, String mensagem) {
		return "GuiaMediacaoPublica?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+mensagem;
	}
	
	private void configure(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("TipoConsulta", "Publica");		
	}	
	
	@Override
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public int Permissao() {
		return GuiaMediacaoDt.CodigoPermissao;
	}	
}
