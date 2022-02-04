package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.projudi.ne.PermissaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PermissaoCt extends PermissaoCtGen {

	private static final long serialVersionUID = 1711533268595672010L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessaoNe, int paginaAtual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {
		PermissaoDt permissaoDt;
		PermissaoNe permissaoNe;		
		String id_Permissao = "";
		String mensagem = "";
		String stAcao = "";
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		request.setAttribute("tempPrograma", "Permissao");
		request.setAttribute("tempBuscaId_Permissao", "Id_Permissao");
		request.setAttribute("tempBuscaPermissao", "Permissao");
		request.setAttribute("tempBuscaId_PermissaoPai", "Id_PermissaoPai");
		request.setAttribute("tempBuscaPermissaoPai", "PermissaoPai");
		request.setAttribute("tempBuscaId_PermissaoEspecial", "Id_PermissaoEspecial");
		request.setAttribute("tempBuscaPermissaoEspecial", "PermissaoEspecial");
		request.setAttribute("tempRetorno", "Permissao");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAnterior", paginaAtual);

		permissaoNe = (PermissaoNe) request.getSession().getAttribute("Permissaone");
		if (permissaoNe == null) {permissaoNe = new PermissaoNe();}
		permissaoDt = (PermissaoDt) request.getSession().getAttribute("Permissaodt");
		if (permissaoDt == null) {permissaoDt = new PermissaoDt();}

		permissaoDt.setId(request.getParameter("Id_Permissao"));
		permissaoDt.setPermissao(request.getParameter("Permissao"));
		permissaoDt.setPermissaoCodigo(request.getParameter("PermissaoCodigo"));
		permissaoDt.setEMenu(request.getParameter("EMenu"));
		permissaoDt.setLink(request.getParameter("Link"));
		permissaoDt.setIrPara(request.getParameter("IrPara"));
		permissaoDt.setTitulo(request.getParameter("Titulo"));
		permissaoDt.setId_PermissaoPai(request.getParameter("Id_PermissaoPai"));
		permissaoDt.setPermissaoPai(request.getParameter("PermissaoPai"));
		permissaoDt.setId_PermissaoEspecial(request.getParameter("Id_PermissaoEspecial"));
		permissaoDt.setPermissaoEspecial(request.getParameter("PermissaoEspecial"));
		permissaoDt.setPermissaoCodigoPai(request.getParameter("PermissaoCodigoPai"));
		permissaoDt.setOrdenacao(request.getParameter("Ordenacao"));
		permissaoDt.setCodigoTemp(request.getParameter("CodigoTemp"));
		permissaoDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		permissaoDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());

		switch (paginaAtual) {
			case (Configuracao.LocalizarAutoPai): {
				if (request.getParameter("Passo")==null && request.getParameter("tempFluxo1") == null){
					String[] lisNomeBusca = {"Código","Permissão"};
					String[] lisDescricao = {"Permissão","Código","Cód. Pai","Pai","É Menu?","Ordenação"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PermissaoPai");
					request.setAttribute("tempBuscaDescricao","PermissaoPai");
					request.setAttribute("tempBuscaPrograma","Permissao");			
					request.setAttribute("tempRetorno","Permissao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.LocalizarAutoPai);
					request.setAttribute("PaginaAtual", (Configuracao.LocalizarAutoPai));
					request.setAttribute("tempFluxo1", "PreencherPai");
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else if(request.getParameter("Passo") == null && request.getParameter("tempFluxo1").equals("PreencherPai")){
					permissaoDt.setPermissaoCodigoPai(permissaoNe.consultarId(permissaoDt.getId_PermissaoPai()).getPermissaoCodigo());
					redireciona(response, "Permissao?PaginaAtual=" + Configuracao.Editar);
				} else {
					String stTemp="";
					stTemp = permissaoNe.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			case Configuracao.Localizar: {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Código","Permissão"};
					String[] lisDescricao = {"Código","Permissão","Cód. Pai","Pai","É Menu?","Ordenação"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Permissao");
					request.setAttribute("tempBuscaDescricao","Permissao");
					request.setAttribute("tempBuscaPrograma","Permissao");			
					request.setAttribute("tempRetorno","Permissao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = permissaoNe.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			case (PermissaoEspecialDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Permissão Especial"};
					String[] lisDescricao = {"Permissão Especial"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PermissaoEspecial");
					request.setAttribute("tempBuscaDescricao","PermissaoEspecial");
					request.setAttribute("tempBuscaPrograma","PermissaoEspecial");			
					request.setAttribute("tempRetorno","Permissao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (PermissaoEspecialDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = permissaoNe.consultarDescricaoPermissaoEspecialJSON(stNomeBusca1, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			case Configuracao.Novo: {
				stAcao = "/WEB-INF/jsptjgo/Permissao.jsp";
				permissaoDt.limpar();
				request.setAttribute("ArrayFuncoesPermissao", permissaoDt.getFuncoesPermissao());
				break;
			}
	
			case Configuracao.Salvar: {
				stAcao = "/WEB-INF/jsptjgo/Permissao.jsp";
				mensagem = permissaoNe.Verificar(permissaoDt);
				if (mensagem.length() != 0) {
					request.setAttribute("MensagemErro", mensagem);
				} else {
					request.setAttribute("MensagemErro", "");
				}
				break;
			}
	
			case Configuracao.SalvarResultado: {
				stAcao = "/WEB-INF/jsptjgo/Permissao.jsp";
				permissaoDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
				permissaoDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());
				permissaoNe.salvar(permissaoDt);
				PermissaoDt[] arrayFuncoesPermissao = permissaoNe.consultaFuncoesPermissao(permissaoDt.getPermissaoCodigo());
				permissaoDt.setFuncoesPermissao(arrayFuncoesPermissao);
				mensagem += "Dados salvos com sucesso";
				request.setAttribute("MensagemOk", mensagem);
				break;
			}
			
			case Configuracao.Editar: {
				stAcao = "/WEB-INF/jsptjgo/Permissao.jsp";
				id_Permissao = request.getParameter("Id_Permissao");
				boolean permissaoEncontrada = false;
				if ((id_Permissao != null) && !(id_Permissao.equalsIgnoreCase(""))) {
					permissaoDt.limpar();
					permissaoDt = permissaoNe.consultarId(id_Permissao);
					permissaoEncontrada = true;
				}
				if (permissaoEncontrada) {
					PermissaoDt[] arrayFuncoesPermissao = permissaoNe.consultaFuncoesPermissao(permissaoDt.getPermissaoCodigo());
					permissaoDt.setFuncoesPermissao(arrayFuncoesPermissao);
				}
				break;
			}
			
			case Configuracao.Excluir: {
				stAcao = "/WEB-INF/jsptjgo/Permissao.jsp";
				boolean permissaoEncontrada = false;
				if (permissaoDt.getId().equalsIgnoreCase("")) {
					mensagem = "Permissão não informada";
					request.setAttribute("MensagemErro", mensagem);
				} else {
					id_Permissao = permissaoDt.getId();
					permissaoDt.limpar();
					permissaoDt = permissaoNe.consultarId(id_Permissao);
					request.setAttribute("MensagemErro", "");
					permissaoEncontrada = true;
				}
				if (permissaoEncontrada) {
					PermissaoDt[] arrayFuncoesPermissao = permissaoNe.consultaFuncoesPermissao(permissaoDt.getPermissaoCodigo());
					permissaoDt.setFuncoesPermissao(arrayFuncoesPermissao);
				}
				break;
			}
			
			case Configuracao.ExcluirResultado: {
				stAcao = "/WEB-INF/jsptjgo/Permissao.jsp";
				permissaoNe.excluirFuncoes(permissaoDt);
				mensagem = permissaoNe.verificarDependenciasPermissao(permissaoDt);
				if (mensagem.length() == 0) {
					permissaoNe.excluir(permissaoDt);
					request.setAttribute("MensagemOk", "Dados excluídos com sucesso");
				} else {
					request.setAttribute("MensagemErro", mensagem);
				}
				permissaoDt.limpar();
				break;
			}
			
			default: {
				super.executar(request, response, usuarioSessaoNe, paginaAtual, tempNomeBusca, posicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("Permissaodt", permissaoDt);
		request.getSession().setAttribute("Permissaone", permissaoNe);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(stAcao);
		requestDispatcher.include(request, response);
	}

}