package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoPermissaoDt;
import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import br.gov.go.tj.projudi.ne.GrupoPermissaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GrupoPermissaoCt extends GrupoPermissaoCtGen {

	private static final long serialVersionUID = -8665117590300116947L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoPermissaoDt GrupoPermissaodt;
		GrupoPermissaoNe GrupoPermissaone;			
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
				
		List tempListPermissoesGrupo = null;
		String Mensagem = "";
		String stGrupoId = "";
		
		String stListaPermissao = "";
		String stAcao = "/WEB-INF/jsptjgo/GrupoPermissao.jsp";

		request.setAttribute("tempPrograma", "GrupoPermissao");
		request.setAttribute("tempBuscaId_GrupoPermissao", "Id_GrupoPermissao");
		request.setAttribute("tempRetorno", "GrupoPermissao");
		request.setAttribute("tempBuscaId_Grupo", "Id_Grupo");
		request.setAttribute("tempBuscaGrupo", "Grupo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("ListaPermissao", stListaPermissao); // lista em branco
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		GrupoPermissaone = (GrupoPermissaoNe) request.getSession().getAttribute("GrupoPermissaone");
		if (GrupoPermissaone == null) GrupoPermissaone = new GrupoPermissaoNe();
		GrupoPermissaodt = (GrupoPermissaoDt) request.getSession().getAttribute("GrupoPermissaodt");
		if (GrupoPermissaodt == null) GrupoPermissaodt = new GrupoPermissaoDt();

		GrupoPermissaodt.setId_Grupo(request.getParameter("Id_Grupo"));
		GrupoPermissaodt.setGrupo(request.getParameter("Grupo"));
		GrupoPermissaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoPermissaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.SalvarResultado:
				if (GrupoPermissaodt.getId_Grupo() != null && !GrupoPermissaodt.getId_Grupo().equalsIgnoreCase("")) {
					
					// LISTA DE PERMISSOES DO GRUPO SELECIONADO
					tempListPermissoesGrupo = GrupoPermissaone.consultarPermissoesIdGrupo(GrupoPermissaodt.getId_Grupo());
					// LISTA DE PERMISSOES EDITADA
					List listEditada = new ArrayList();
					// MONTA LISTA DE PERMISSOES EDITADA*************************
					String[] idsPermissoes = request.getParameterValues("chkEditar");
					if (idsPermissoes != null) {
						for (int i = 0; i < idsPermissoes.length; i++) {
							PermissaoDt obTemp = new PermissaoDt();
							obTemp.setId(idsPermissoes[i]);
							listEditada.add(obTemp);
						}
					}
					// COMPARAÇÃO DAS LISTAS
					// ******************************************
					// ADICIONA NOVA PERMISSAO
					List listIncluir = new ArrayList();
					List listExcluir = new ArrayList();
					for (int i = 0; i < listEditada.size(); i++) {
						Boolean novaPermissao = Boolean.TRUE;
						PermissaoDt obTempEditado = (PermissaoDt) listEditada.get(i);
						for (int j = 0; j < tempListPermissoesGrupo.size(); j++) {
							GrupoPermissaoDt obTempOriginal = (GrupoPermissaoDt) tempListPermissoesGrupo.get(j);
							if (obTempEditado.getId().equalsIgnoreCase(obTempOriginal.getId_Permissao())) {
								novaPermissao = Boolean.FALSE;
								break;
							}
						}
						if (novaPermissao == Boolean.TRUE) {
							GrupoPermissaoDt grupoPermissao = new GrupoPermissaoDt();
							grupoPermissao.setId_Permissao(obTempEditado.getId());
							grupoPermissao.setId_Grupo(GrupoPermissaodt.getId_Grupo());
							grupoPermissao.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
							grupoPermissao.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
							listIncluir.add(grupoPermissao);
						}
					}
					// ***** EXCLUI PERMISSAO
					for (int j = 0; j < tempListPermissoesGrupo.size(); j++) {
						Boolean excluiPermissao = Boolean.TRUE;
						GrupoPermissaoDt obTempOriginal = (GrupoPermissaoDt) tempListPermissoesGrupo.get(j);
						for (int i = 0; i < listEditada.size(); i++) {
							PermissaoDt obTempEditado = (PermissaoDt) listEditada.get(i);
							if (obTempOriginal.getId_Permissao().equalsIgnoreCase(obTempEditado.getId())) {
								excluiPermissao = Boolean.FALSE;
								break;
							}
						}
						if (excluiPermissao == Boolean.TRUE) {
							obTempOriginal.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
							obTempOriginal.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
							listExcluir.add(obTempOriginal);
						}
					}
					// FIM DA COMPARAÇÃO DAS LISTAS********************************
					// SALVA LISTA DE PERMISSOES NO BD ********************
					Mensagem = GrupoPermissaone.Verificar(GrupoPermissaodt);
					if (Mensagem.length() == 0) {
						if (listIncluir.size() == 0 && listExcluir.size() == 0)
							request.setAttribute("MensagemErro", "A Lista de Permissões não foi Editada!");
						else if (GrupoPermissaone.atualizaListaPermissoesGrupo(listIncluir, listExcluir))
							request.setAttribute("MensagemOk", "Lista de Permissões Atualizada com sucesso");
						else
							request.setAttribute("MensagemErro", "Erro na tentativa de Atualização dos dados");
					} else
						request.setAttribute("MensagemErro", Mensagem);
					// CARREGA NOVAMENTE A LISTA DE PERMISSOES ATUALIZADA
					/*
					 * stListaPermissao =
					 * GrupoPermissaone.getListaPemissoes(GrupoPermissaodt.getId_Grupo());
					 * request.setAttribute("ListaPermissao", stListaPermissao);
					 * request.setAttribute("PaginaAtual", Configuracao.Editar);
					 * request.setAttribute("PosicaoPaginaAtual",
					 * Funcoes.StringToLong(PosicaoPaginaAtual));
					 */
					// ********************************************************************
					GrupoPermissaodt.limpar();
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				} else {
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					request.setAttribute("MensagemErro", "Um Grupo deve ser selecionado!");
				}
				break;
			case Configuracao.Editar:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
	
				if (request.getParameter("Id_Grupo") != null)
					stGrupoId = request.getParameter("Id_Grupo");
				else
					stGrupoId = GrupoPermissaodt.getId_Grupo();
	
				if (stGrupoId != null)
					if (!stGrupoId.equalsIgnoreCase("")) {						
						stListaPermissao = GrupoPermissaone.getListaPermissoes(stGrupoId);
						request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
						request.setAttribute("ListaPermissao", stListaPermissao);
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					}
				break;
			case (GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo","Serventia Tipo Código","Serventia Tipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "GrupoPermissao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = GrupoPermissaone.consultarDescricaoGrupoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			case Configuracao.Curinga6:
				String stPasso = request.getParameter("Passo");
				if("incluir".equals(stPasso)) {
					String stId_Grupo = request.getParameter("Id_Principal");
					String stId_Permissao = request.getParameter("Id_Secundario");
					GrupoPermissaoDt tempDt = new GrupoPermissaoDt();
					tempDt.setId_Permissao(stId_Permissao);
					tempDt.setId_Grupo(stId_Grupo);
					tempDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					tempDt.setIpComputadorLog(getIpCliente(request));					
					GrupoPermissaone.salvar(tempDt);
					return;
				}else if("excluir".equals(stPasso)) {
					String stId_GrupoPermissao = request.getParameter("Id_Excluir");
					GrupoPermissaoDt tempDt = new GrupoPermissaoDt();
					tempDt.setId(stId_GrupoPermissao);					
					tempDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					tempDt.setIpComputadorLog(getIpCliente(request));	
					
					GrupoPermissaone.excluir(tempDt);
					return;
				}
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("GrupoPermissaodt", GrupoPermissaodt);
		request.getSession().setAttribute("GrupoPermissaone", GrupoPermissaone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	}

}
