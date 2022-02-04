package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.GrupoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla a listagem de usuários do sistema e gerencia ações como:
 * Limpar Senha, Gerar Relatório em PDF, Alterar Dados.
 * @author msapaula
 *
 */
public class DadosUsuarioCt extends Controle {

    private static final long serialVersionUID = -692725431904618357L;

	public int Permissao() {
		return 326;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		List tempList = null;
		byte[] byTemp = null;
		UsuarioDt usuarioDt = null;
		GrupoNe grupoNe = null;
		UsuarioNe usuarioNe = new UsuarioNe();
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/DadosUsuario.jsp";

		request.setAttribute("tempPrograma", "DadosUsuario");
		request.setAttribute("tempRetorno", "DadosUsuario");
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		usuarioDt = (UsuarioDt) request.getSession().getAttribute("usuarioDt");
		if (usuarioDt == null) usuarioDt = new UsuarioDt();
		
		grupoNe = (GrupoNe) request.getSession().getAttribute("grupoNe");
		if(grupoNe == null) grupoNe = new GrupoNe();

		usuarioDt.setId(request.getParameter("Id_Usuario"));
		usuarioDt.setNome(request.getParameter("Nome"));
		usuarioDt.setId_Grupo(request.getParameter("Id_Grupo"));
		usuarioDt.setGrupo(request.getParameter("Grupo"));
		usuarioDt.setId_Serventia(request.getParameter("Id_Serventia"));
		usuarioDt.setServentia(request.getParameter("Serventia"));
		usuarioDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		usuarioDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		String curinga = request.getParameter("Curinga");

		request.setAttribute("PaginaAnterior", paginaatual);
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:
				usuarioDt.limpar();
				usuarioDt.setGrupo("Todas as Funçoes");
				stAcao = "/WEB-INF/jsptjgo/ListaUsuarios.jsp";
				request.getSession().setAttribute("ListaUsuario", new ArrayList());
				break;

			case Configuracao.Salvar:
				// Curinga "L" refere-se a Confirmação de Limpar senha
				// Curinga "A" refere-se a Confirmação de Ativação de Usuário
				if (curinga != null) {
					if (curinga.equals("L")) {
						request.setAttribute("Mensagem", "Clique para Confirmar a geração de nova senha de acesso para " + usuarioDt.getNome());
						request.setAttribute("Curinga", "L");
					} else if (curinga.equals("A")) {
						request.setAttribute("Mensagem", "Clique para Ativar o usuário " + usuarioDt.getNome());
						request.setAttribute("Curinga", "A");
					}
				}
				break;

			case Configuracao.SalvarResultado:
				// Curinga "L" refere-se a Limpar senha
				// Curinga "A" refere-se a Ativação de Usuário
				if (usuarioDt.getId().length() > 0) {
					if (curinga != null) {
						if (curinga.equals("L")) {
							usuarioNe.limparSenha(usuarioDt);
							request.setAttribute("MensagemOk", "Senha Alterada com sucesso. (senha 12345)");
						} else if (curinga.equals("A")) {
							usuarioNe.ativarUsuario(usuarioDt);
							usuarioDt.setAtivo("true"); //Somente para aparecer corretamente na tela
							request.setAttribute("MensagemOk", "Usuário Ativado com sucesso.");
						}
					}
				} else request.setAttribute("MensaemErro", "Nenhum Usuário foi selecionado.");
				break;

			case Configuracao.Localizar:
				tempList = usuarioNe.consultarUsuarios(usuarioDt.getId_Grupo(), usuarioDt.getId_Serventia(), usuarioDt.getNome());
				request.getSession().setAttribute("ListaUsuario", tempList);
				stAcao = "/WEB-INF/jsptjgo/ListaUsuarios.jsp";
				break;

			//Confirmação para Desativar Usuário
			case Configuracao.Excluir:
				request.setAttribute("Mensagem", "Clique para Desativar o usuário " + usuarioDt.getNome());
				break;

			case Configuracao.ExcluirResultado:
				if (usuarioDt.getId().length() > 0) {
					usuarioNe.desativarUsuario(usuarioDt);
					usuarioDt.setAtivo("false"); //Somente para aparecer corretamente na tela
					request.setAttribute("MensagemOk", "Usuário Desativado com sucesso.");
				} else request.setAttribute("MensagemErro", "Nenhum Usuário foi selecionado.");
				break;

			//Alterar Senha do Usuário Logado
			case Configuracao.Curinga6:
				stAcao = "/WEB-INF/jsptjgo/Padroes/Senha.jsp";
				alterarSenhaUsuarioLogado(request, UsuarioSessao);
				if (request.getParameter("PassoSenha") != null && request.getParameter("PassoSenha").equals("1") && UsuarioSessao.getUsuarioDt().getCodigoTemp().equalsIgnoreCase("trocarSenha")) {
					UsuarioSessao.getUsuarioDt().setCodigoTemp("");
					redireciona(response, "Usuario?PaginaAtual=-10&Mensagem='Senha Alterada com Sucesso'");
					return;
				}
				break;

			//Gera relatório de usuários em PDF
			case Configuracao.Imprimir:
				// PATH PARA OS ARQUIVOS JASPER DO RELATORIO USUARIO
				List listaUsuarios = (List) request.getSession().getAttribute("ListaUsuario");
				byTemp = usuarioNe.relListaUsuarios(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , listaUsuarios, usuarioDt.getGrupo());				
				
				enviarPDF(response, byTemp,"Relatorio");
				
				request.getSession().removeAttribute("ListaUsuario");
				byTemp = null;
				return;

			case (GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Digite o Grupo"};
					String[] lisDescricao = {"Grupo", "Código", "Tipo de Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Grupo");
					request.setAttribute("tempBuscaDescricao","Grupo");
					request.setAttribute("tempBuscaPrograma","Grupo");			
					request.setAttribute("tempRetorno","DadosUsuario");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = usuarioNe.consultarGruposListaUsuariosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
					enviarJSON(response, stTemp);
					return;								
				}
				break;
				
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia", "Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "DadosUsuario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = usuarioNe.consultarServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);					
					enviarJSON(response, stTemp);									
					return;
				}
				break;
				
			default:
				stId = request.getParameter("Id_Usuario");
				if (stId != null && stId.length() > 0) {
					usuarioDt = usuarioNe.consultarUsuarioCompleto(stId);
				} else { 
					if (usuarioDt.getId_Grupo().length() > 0) {
						tempList = usuarioNe.consultarUsuarios(usuarioDt.getId_Grupo(), usuarioDt.getId_Serventia(), usuarioDt.getNome());
						request.getSession().setAttribute("ListaUsuario", tempList);
					}
					stAcao = "/WEB-INF/jsptjgo/ListaUsuarios.jsp";
				}
				break;
		}

		request.getSession().setAttribute("usuarioDt", usuarioDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que realiza a verificação e alteração de senha de acesso para o usuário logado.
	 * @param request
	 * @param usuarioSessao
	 * @throws Exception
	 */
	protected void alterarSenhaUsuarioLogado(HttpServletRequest request, UsuarioNe usuarioSessao) throws Exception{
		String senhaAtual = "", senhaNova = "", senhaComparacao = "";
		UsuarioNe usuarioNe = new UsuarioNe();

		//Captura usuário da Sessão
		UsuarioDt usuarioLogado = usuarioSessao.getUsuarioDt();
		usuarioLogado.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		usuarioLogado.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAtual", Configuracao.Curinga6);
		
		if (request.getParameter("SenhaAtual") != null) senhaAtual = request.getParameter("SenhaAtual");		
		if (request.getParameter("SenhaNova") != null) senhaNova = (String) request.getParameter("SenhaNova");
		if (request.getParameter("SenhaNovaComparacao") != null) senhaComparacao = request.getParameter("SenhaNovaComparacao");

		//Se refere ao passo de salvar a senha, verifica dados digitados
		if (request.getParameter("PassoSenha") != null && request.getParameter("PassoSenha").equals("1")) {
			
			if(usuarioSessao.isLoginToken()) {
				String stMensagem = usuarioNe.verificarAlteracaoSenha(usuarioSessao.getUsuarioDt(), null, senhaNova, senhaComparacao, false);
				if (stMensagem.length() == 0) {
					usuarioNe.alterarSenha(usuarioSessao.getUsuarioDt(), null, senhaNova);
				} else {
					throw new MensagemException(stMensagem);
				}
			}else {
				String stMensagem = usuarioNe.verificarAlteracaoSenha(usuarioSessao.getUsuarioDt(), senhaAtual, senhaNova, senhaComparacao, true);
				if (stMensagem.length() == 0) {
					usuarioNe.alterarSenha(usuarioSessao.getUsuarioDt(), senhaAtual, senhaNova);
				} else {
					throw new MensagemException(stMensagem);
				}
			}
			request.setAttribute("MensagemOk", "Senha Alterada com Sucesso.");
			senhaAtual = "";
			senhaNova = "";
			senhaComparacao = "";
		} else if(usuarioSessao.getUsuarioDt().getCodigoTemp().equalsIgnoreCase("trocarSenha"))
			request.setAttribute("MensagemOk", "Por motivo de segurança é necessário efetuar a troca da senha.");
			
		request.setAttribute("SenhaAtual", senhaAtual);
		request.setAttribute("SenhaNova", senhaNova);
		request.setAttribute("SenhaNovaComparacao", senhaComparacao);
	}
}
