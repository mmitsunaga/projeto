package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.dt.GerenciaArquivoBancoDt;
import br.gov.go.tj.projudi.ne.BancoNe;
import br.gov.go.tj.projudi.ne.GerenciaArquivoBancoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GerenciaArquivoBancoCt extends Controle {

	private static final long serialVersionUID = -2502683186847132160L;
	private GerenciaArquivoBancoNe gerenciaArquivoBancoNe;
	private String tipoMensagem = "";
	private String mensagem = "";

	public int Permissao() {
		return GerenciaArquivoBancoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		String stId = "";
		Integer idBanco = null;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "/WEB-INF/jsptjgo/GerenciaArquivoBanco.jsp";

		request.setAttribute("tempPrograma", "GerenciaArquivoBanco");
		request.setAttribute("tempRetorno", "GerenciaArquivoBanco");

		if (request.getAttribute("idBanco") != null && !request.getAttribute("idBanco").equals(""))	idBanco = Funcoes.StringToInt(request.getAttribute("idBanco").toString());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Curinga6: {
				if (idBanco != null) gerenciaArquivoBancoNe = new GerenciaArquivoBancoNe(idBanco);
				String conteudo = getConteudoArquivo(request);
				int retorno = gerenciaArquivoBancoNe.lerArquivo(conteudo);
	
				switch (retorno) {
					case GerenciaArquivoBancoNe.SUCESSO: {
						mensagem = "Processamento do Upload do Arquivo do Banco " + request.getAttribute("Banco") + " realizado com Sucesso!";
						tipoMensagem = "MensagemOk";
						break;
					}
					case GerenciaArquivoBancoNe.SEM_SUCESSO: {
						break;
					}
				}
				request.setAttribute(tipoMensagem, mensagem);
				break;
			}
	
			case Configuracao.Limpar: {
				request.setAttribute("idBanco", "");
				request.setAttribute("Banco", "");
	
				break;
			}
	
			case BancoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar: {
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Banco"};
					String[] lisDescricao = {"Banco"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Banco");
					request.setAttribute("tempBuscaDescricao", "Banco");
					request.setAttribute("tempBuscaPrograma", "Banco");
					request.setAttribute("tempRetorno", "GerenciaArquivoBanco");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BancoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					BancoNe bancoNe = new BancoNe();
					stTemp = bancoNe.consultarDescricaoJSON(stNomeBusca1, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}
	
			default: {
				stId = request.getParameter("tempBuscaId_Banco");
				if (stId != null && !stId.isEmpty()) {
					request.setAttribute("idBanco", stId);
					request.setAttribute("Banco", request.getParameter("tempBuscaBanco"));
					stId = null;
				} else {
					request.setAttribute("idBanco", "");
					request.setAttribute("Banco", "");
				}
				break;
			}
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
