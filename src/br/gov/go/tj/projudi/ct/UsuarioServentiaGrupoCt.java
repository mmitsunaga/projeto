package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaGrupoNe;
import br.gov.go.tj.utils.Configuracao;

public class UsuarioServentiaGrupoCt extends UsuarioServentiaGrupoCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1009761077974838354L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioServentiaGrupoDt UsuarioServentiaGrupodt;
		UsuarioServentiaGrupoNe UsuarioServentiaGrupone;

		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String stAcao = "";
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		String stNomeBusca2 = "";
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		if (UsuarioSessao.isCoordenadorHabilitacaoMp())
			stAcao = "/WEB-INF/jsptjgo/HabilitacaoMP.jsp";
		else		
			stAcao = "/WEB-INF/jsptjgo/UsuarioServentiaGrupo.jsp";
		
		ServentiaTipoDt serventiaTipoDt;

		request.setAttribute("tempPrograma", "UsuarioServentiaGrupo");
		request.setAttribute("tempRetorno", "UsuarioServentiaGrupo");

		UsuarioServentiaGrupone = (UsuarioServentiaGrupoNe) request.getSession().getAttribute("UsuarioServentiaGrupone");
		if (UsuarioServentiaGrupone == null) UsuarioServentiaGrupone = new UsuarioServentiaGrupoNe();

		UsuarioServentiaGrupodt = (UsuarioServentiaGrupoDt) request.getSession().getAttribute("UsuarioServentiaGrupodt");
		if (UsuarioServentiaGrupodt == null) UsuarioServentiaGrupodt = new UsuarioServentiaGrupoDt();

		UsuarioServentiaGrupodt.setUsuarioServentiaGrupo(request.getParameter("UsuarioServentiaGrupo"));
		UsuarioServentiaGrupodt.setId_Serventia(request.getParameter("Id_Serventia"));
		UsuarioServentiaGrupodt.setServentia(request.getParameter("Serventia"));
		UsuarioServentiaGrupodt.setId_Grupo(request.getParameter("Id_Grupo"));
		UsuarioServentiaGrupodt.setGrupo(request.getParameter("Grupo"));
		UsuarioServentiaGrupodt.setUsuario(request.getParameter("Usuario"));
		UsuarioServentiaGrupodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioServentiaGrupodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		// -----------------------------------------------------------------------------------------------------------------------
		switch (paginaatual) {

			case Configuracao.Localizar: // localizar
				
				if (request.getParameter("Passo") == null) {
					
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Usuário"};
					String[] lisDescricao = {"Usuário","Nome","Grupo","Serventia"};
					
					String[] camposHidden = {"NomeUsuario"};
					request.setAttribute("tempBuscaId", "Id_UsuarioServentiaGrupo");
					request.setAttribute("tempBuscaDescricao", "UsuarioServentiaGrupo");
					request.setAttribute("tempBuscaPrograma", "UsuarioServentiaGrupo");
					request.setAttribute("tempRetorno", "ServentiaCargo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioServentiaGrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("camposHidden", camposHidden);
					
					
					atribuirJSON(request, "Id_UsuarioServentiaGrupo", "UsuarioServentiaGrupo", "Usuário Serventia Grupo", "UsuarioServentiaGrupo", Configuracao.Editar,String.valueOf(Configuracao.Localizar), lisNomeBusca, lisDescricao);
					
				} else {
					String stTemp = "";
					stTemp = UsuarioServentiaGrupone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;				

			case Configuracao.Novo:
				UsuarioServentiaGrupodt.limpar();
				break;

			// Confirmação Habilitação Usuario
			case Configuracao.Salvar:
				// Se já possui UsuarioServentiaGrupo trata-se apenas de mudar o status para "Ativo"
				stId = request.getParameter("Id_UsuarioServentiaGrupo");
				if (stId != null && stId.length() > 0) {
					UsuarioServentiaGrupodt.setId(stId);
					request.setAttribute("Mensagem", "Clique para confirmar a Ativação do Usuário na Serventia: " + UsuarioServentiaGrupodt.getServentia() + ", Grupo: " + UsuarioServentiaGrupodt.getGrupo());
				} else {// Caso em que será incluído um novo registro um Usuario Serventia
					request.setAttribute("Mensagem", "Clique para confirmar a Habilitação");
				}
				break;

			// Salvar Habilitação de Usuário em uma Serventia e Grupo
			case Configuracao.SalvarResultado:
				if (UsuarioServentiaGrupodt.getId_Usuario().length() > 0) {
					if (UsuarioServentiaGrupodt.getId().length() == 0) {
						// Salva uma nova Habilitação em Serventia/Grupo
						Mensagem = UsuarioServentiaGrupone.Verificar(UsuarioServentiaGrupodt);

						if (Mensagem.length() == 0) {
							if (!UsuarioServentiaGrupone.verificarHabilitacaoUsuario(UsuarioServentiaGrupodt)) {
								UsuarioServentiaGrupone.salvarUsuarioServentiaGrupo(UsuarioServentiaGrupodt);
								UsuarioServentiaGrupodt.setId_UsuarioServentia("null");
								UsuarioServentiaGrupodt.setId_Serventia("null");
								UsuarioServentiaGrupodt.setId_Grupo("null");
							} else {
								request.setAttribute("MensagemErro", "Usuário já está habilitado na Serventia e Grupo selecionados.");
								break;
							}
						} else request.setAttribute("MensagemErro", Mensagem);
					} else {
						// Se já foi passado Id_UsuarioServentiaGrupo trata-se apenas de voltar o status para "Ativo"
						UsuarioServentiaGrupone.ativarUsuarioServentiaGrupo(UsuarioServentiaGrupodt);
						UsuarioServentiaGrupodt.setId_Serventia("null");
						UsuarioServentiaGrupodt.setId_Grupo("null");
						UsuarioServentiaGrupodt.setId("");
					}

					// Atualiza lista de serventias e grupos
					// Atualiza lista de serventias e grupos
					if (UsuarioSessao.isCoordenadorHabilitacaoMp()) {
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposPromotores(UsuarioServentiaGrupodt.getId_Usuario());
					}else if (UsuarioSessao.isCoordenadorHabilitacaoSsp()) {
						//completar o codigo
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposPoliciais(UsuarioServentiaGrupodt.getId_Usuario());
					}else {
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposServidorJudiciario(UsuarioServentiaGrupodt.getId_Usuario());
					}
					UsuarioServentiaGrupodt.setListaServentiasGrupos(tempList);
					request.setAttribute("MensagemOk", "Habilitação Realizada com Sucesso.");
				} else request.setAttribute("MensagemErro", "Nenhum Usuário foi selecionado.");
				break;

			// Confirmar ação de Desativar UsuarioServentiaGrupo
			case Configuracao.Excluir:
				// Captura UsuarioServentiaGrupo selecionado
				stId = request.getParameter("Id_UsuarioServentiaGrupo");
				if (stId != null && stId.length() > 0) {
					UsuarioServentiaGrupodt.setId(stId);
					request.setAttribute("Mensagem", "Clique para confirmar a Desativação do Usuário na Serventia: " + UsuarioServentiaGrupodt.getServentia() + ", Grupo: " + UsuarioServentiaGrupodt.getGrupo());
				} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia/Grupo para Desabilitação");
				break;

			// Desativar UsuarioServentiaGrupo
			case Configuracao.ExcluirResultado:
				if (UsuarioServentiaGrupodt.getId().length() > 0) {
					UsuarioServentiaGrupone.desativarUsuarioServentiaGrupo(UsuarioServentiaGrupodt);
					request.setAttribute("MensagemOk", "Usuário Desabilitado com Sucesso.");
					UsuarioServentiaGrupodt.setId_Serventia("null");
					UsuarioServentiaGrupodt.setId_Grupo("null");
					UsuarioServentiaGrupodt.setId("");

					// Atualiza lista de serventias e grupos
					if (UsuarioSessao.isCoordenadorHabilitacaoMp()) {
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposPromotores(UsuarioServentiaGrupodt.getId_Usuario());
					}else if ( UsuarioSessao.isCoordenadorHabilitacaoSsp()) {
						//completar aqui
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposPoliciais(UsuarioServentiaGrupodt.getId_Usuario());
					}else {
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposServidorJudiciario(UsuarioServentiaGrupodt.getId_Usuario());
					}
					UsuarioServentiaGrupodt.setListaServentiasGrupos(tempList);
				} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia/Grupo para Desabilitação");
				break;

			case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				UsuarioServentiaGrupodt.limpar();
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome", "Usuário"};
					String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Usuario");
					request.setAttribute("tempBuscaDescricao","Usuario");
					request.setAttribute("tempBuscaPrograma","Servidor Judiciário");			
					request.setAttribute("tempRetorno","UsuarioServentiaGrupo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					request.setAttribute("Curinga", "vazio");

					String stTemp="";

					stTemp = UsuarioServentiaGrupone.consultarTodosUsuariosJSON(stNomeBusca1, stNomeBusca2, UsuarioSessao, PosicaoPaginaAtual);

					enviarJSON(response, stTemp);

					return;								
				}
				break;

			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia","Estado"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "UsuarioServentiaGrupo");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				request.setAttribute("ServentiaTipoCodigo", UsuarioServentiaGrupodt.getServentiaTipoCodigo());

				String Id_ServentiaTipoCodigo;
				if (UsuarioServentiaGrupodt.getId_Usuario() != null && !UsuarioServentiaGrupodt.getId_Usuario().isEmpty()) {
					Id_ServentiaTipoCodigo = UsuarioServentiaGrupodt.getServentiaTipoCodigo();
				} else {
					Id_ServentiaTipoCodigo = UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo();
				}
				stTemp = UsuarioServentiaGrupone.consultarServentiasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual, Id_ServentiaTipoCodigo);

				enviarJSON(response, stTemp);

				return;
			}
				break;

			case (GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo","Tipo Serventia","Id Serventia"};	
					String[] camposHidden = {"TipoServentia","IdServentia"};
					request.setAttribute("camposHidden",camposHidden);			
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "UsuarioServentiaGrupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Grupo");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual",  String.valueOf(GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
					stTemp = UsuarioServentiaGrupone.consultarGruposHabilitacaoServidoresJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				
			UsuarioServentiaGrupodt.setId_Serventia("null");
			UsuarioServentiaGrupodt.setServentia("");
				break;

			default:
				stId = request.getParameter("Id_UsuarioServentiaGrupo");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(UsuarioServentiaGrupodt.getId())) {
					UsuarioServentiaGrupodt.limpar();
					UsuarioServentiaGrupodt = UsuarioServentiaGrupone.consultarId(stId);
				}

				stId = request.getParameter("Id_Usuario");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(UsuarioServentiaGrupodt.getId_Usuario())) {
					UsuarioServentiaGrupodt.setId_Usuario(request.getParameter("Id_Usuario"));
					
					if (UsuarioSessao.isCoordenadorHabilitacaoMp()) {
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposPromotores(stId);
					}else if(UsuarioSessao.isCoordenadorHabilitacaoSsp()) {
						//completar aqui
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposPoliciais(stId);
					}else {
						tempList = UsuarioServentiaGrupone.consultarServentiasGruposServidorJudiciario(stId);
					}
					
					if (tempList.size() > 0) UsuarioServentiaGrupodt.setListaServentiasGrupos(tempList);
				}
				break;
		}

		request.getSession().setAttribute("UsuarioServentiaGrupodt", UsuarioServentiaGrupodt);
		request.getSession().setAttribute("UsuarioServentiaGrupone", UsuarioServentiaGrupone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
