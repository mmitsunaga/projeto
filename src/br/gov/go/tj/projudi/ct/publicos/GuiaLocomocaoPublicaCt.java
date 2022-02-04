package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.GuiaLocomocaoCt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class GuiaLocomocaoPublicaCt extends GuiaLocomocaoCt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6915432178893943128L;
	
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
			case Configuracao.Curinga6 : {
				if (checkRecaptcha(request)) {
					paginaatual = Configuracao.Curinga6;
				}
				else {
					String stAcao = this.PAGINA_GUIA_LOCOMOCAO;
					stAcao = this.obtenhaAcaoPreviaDeCalculo(request);
					RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
					dis.include(request, response);
					return;
				}
				break;
			}
	    }
	
		super.executarEspecialista(request, response, UsuarioDt.SistemaProjudi, super.getIpCliente(request), UsuarioServentiaDt.SistemaProjudi, UsuarioSessao, paginaatual, posicaopaginaatual);
	}
	
	@Override
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaLocomocaoPublica";
	} 
	
	@Override
	protected String obtenhaServletBuscaProcesso() {
		return "BuscaProcessoPublica";
	}
	
	@Override
	protected String obtenhaTituloPagina() {
		return "Guia Locomoção [Acesso Público]";
	}
	
	@Override
	protected String obtenhaAcaoMensagemOk(ProcessoDt processoDt, String mensagem) {
		return "GuiaLocomocaoPublica?PaginaAtual="+Configuracao.Novo+"&MensagemOk="+mensagem;
	}
	
	@Override
	protected String obtenhaAcaoMensagemErro(ProcessoDt processoDt, String mensagem) {
		return "GuiaLocomocaoPublica?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+mensagem;
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
		return 875;
	}	
}
