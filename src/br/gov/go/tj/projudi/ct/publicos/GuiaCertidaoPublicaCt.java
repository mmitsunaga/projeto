package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.GuiaInicialCt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaCertidaoPublicaCt extends GuiaInicialCt {

	private static final long serialVersionUID = -920120114172699685L;
	
	@Override
	protected String obtenhaNomeControleWebXml() {
		return "GuiaCertidaoPublica";
	}
	
	private boolean validouCaptcha;
	@Override
	protected String obtenhaAcaoPreviaDeCalculo(HttpServletRequest request) {
		if (validouCaptcha) {
			return super.obtenhaAcaoPreviaDeCalculo(request);
		}
		
		exibirCaptcha(request);
		return "/WEB-INF/jsptjgo/Padroes/Captcha.jsp";
	}
	
	@Override
	protected String obtenhaTituloPagina() {
		return "Guia de Certidão";
	}
	
	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual)throws Exception, ServletException, IOException {
		configure(request, response);
		
		int passoBusca = -1;
		if((request.getParameter("PassoBusca") != null) && !(request.getParameter("PassoBusca").equals("null")) )
			passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		
		switch (paginaatual) {
		 	case Configuracao.Novo: {
		 		configure(request, response);
		 		break;
		 	}
	        case Configuracao.Salvar:
	        case Configuracao.Excluir: {
	            // é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
	            request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
	            break;
	        }
	        case Configuracao.SalvarResultado:
	        case Configuracao.ExcluirResultado: {
	            if (!UsuarioSessao.verificarPedido(request.getParameter("__Pedido__")))
	            	throw new MensagemException("<{Pedido enviado mais de um vez.}> Local Exception: " + this.getClass().getName() + ".doPost()");
	            break;
	        }
	        //Apresenta Prévia de Cálculo
			case Configuracao.Editar : {
				if (passoBusca == Configuracao.LocalizarDWR) {
					String stAcao = "";
					if (!checkRecaptcha(request)) {
						stAcao = this.obtenhaAcaoPreviaDeCalculo(request);
						RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
						dis.include(request, response);
						return;
					} else {
						paginaatual = Configuracao.Curinga6;
						validouCaptcha = true;
					}						
				}
				break;
			}
        }
	
		super.executar(request, response, UsuarioSessao, paginaatual, nomebusca, posicaopaginaatual);	
	}
	
	private void configure(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("TipoConsulta", "Publica");
		validouCaptcha = false;
	}
	
	private void exibirCaptcha(HttpServletRequest request) {
		request.setAttribute("action", obtenhaNomeControleWebXml());
		request.setAttribute("PaginaAtual", Configuracao.Curinga6);
		request.setAttribute("nome", "PassoEditar");
		request.setAttribute("valor", Configuracao.Curinga8);
	}
	
	@Override
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public int Permissao() {
		return 999999;
	}
}