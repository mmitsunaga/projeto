package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class UsuarioServentiaOabCt extends UsuarioServentiaOabCtGen {

	private static final long serialVersionUID = 811337030507684712L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioDt usuarioDt;
		UsuarioNe usuarioNe;
		String stAcao = "";
		List tempList = null;
		String Mensagem = "";
		String stId = "";
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		String stNomeBusca2 = "";
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		if (UsuarioSessao.getUsuarioDt() != null &&  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_OAB)
			stAcao = "/WEB-INF/jsptjgo/HabilitacaoUsuarioServentiaOab.jsp";
		else
			stAcao = "/WEB-INF/jsptjgo/UsuarioServentiaOab.jsp";

		request.setAttribute("tempPrograma", "UsuarioServentiaOab");
		request.setAttribute("tempRetorno", "UsuarioServentiaOab");
		request.setAttribute("tempBuscaId_UsuarioServentiaOab", "Id_UsuarioServentiaOab");
		request.setAttribute("tempBuscaUsuarioServentiaOab", "UsuarioServentiaOab");

		usuarioNe = (UsuarioNe) request.getSession().getAttribute("usuarioNe");
		if (usuarioNe == null) usuarioNe = new UsuarioNe();

		usuarioDt = (UsuarioDt) request.getSession().getAttribute("usuarioDt");
		if (usuarioDt == null) usuarioDt = new UsuarioDt();
		
		usuarioDt.setUsuario(request.getParameter("Usuario"));
		usuarioDt.getUsuarioServentiaOab().setOabNumero(request.getParameter("OabNumero"));
		usuarioDt.getUsuarioServentiaOab().setOabComplemento(request.getParameter("OabComplemento"));
		usuarioDt.setId_Serventia(request.getParameter("Id_Serventia"));
		usuarioDt.setServentia(request.getParameter("Serventia"));
		usuarioDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		usuarioDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {

			case Configuracao.Novo:
				usuarioDt.limpar();
				break;

			// Confirmação Habilitação Advogado
			case Configuracao.Salvar:
				// Se já possui UsuarioServentia trata-se apenas de mudar o status para "Ativo"
				stId = request.getParameter("Id_UsuarioServentia");
				if (stId != null && stId.length() > 0) {
					usuarioDt.setId_UsuarioServentia(stId);
					request.setAttribute("Mensagem", "Clique para confirmar a Ativação do Usuário na Serventia: " + usuarioDt.getServentia() + ", OAb: " + usuarioDt.getUsuarioServentiaOab().getOabNumero());
				} else {// Caso em que será incluído um novo registro um Usuario Serventia
					request.setAttribute("Mensagem", "Clique para confirmar a Habilitação");
				}
				break;

			// Salvar Habilitação de Usuário em uma Serventia e OAB
			case Configuracao.SalvarResultado:
				if (usuarioDt.getId().length() > 0) {
					if (usuarioDt.getId_UsuarioServentia().length() == 0) {
						
						// Salva uma nova Habilitação em UsuarioServentiaOab
						Mensagem = usuarioNe.verificarUsuarioServentiaOab(usuarioDt);

						if (Mensagem.length() == 0) {
							usuarioNe.salvarUsuarioServentiaOab(usuarioDt);
							//Limpa dados para possibilitar próxima habilitação
							usuarioDt.setId_UsuarioServentia("");
							usuarioDt.setId_Serventia("");
							usuarioDt.getUsuarioServentiaOab().setOabNumero("");
							usuarioDt.getUsuarioServentiaOab().setOabComplemento("");
							usuarioDt.getUsuarioServentiaOab().setId_UsuarioServentia("");
							usuarioDt.getUsuarioServentiaOab().setId("");
						} else request.setAttribute("MensagemErro", Mensagem);
					} else {
						// Se já foi passado Id_UsuarioServentia trata-se apenas de voltar o status para "Ativo"
						usuarioNe.ativarAdvogado(usuarioDt);
						usuarioDt.setId_UsuarioServentia("");
						usuarioDt.setId_Serventia("null");
						usuarioDt.getUsuarioServentiaOab().setOabNumero("");
						usuarioDt.getUsuarioServentiaOab().setOabComplemento("");
					}

					if (UsuarioSessao.getUsuarioDt() != null &&  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo() != null	&& Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_OAB)
						tempList = usuarioNe.consultarServentiaOabAdvogadoHabilitacao(usuarioDt.getId(), UsuarioSessao.getUsuarioDt().getId_Serventia());
					else
						tempList = usuarioNe.consultarServentiaOabAdvogado(usuarioDt.getId());
					
					usuarioDt.setListaUsuarioServentias(tempList);
					request.setAttribute("MensagemOk", "Habilitação Realizada com Sucesso.");
				} else request.setAttribute("MensagemErro", "Nenhum Usuário foi selecionado.");
				break;

			// Confirmar ação de Desativar UsuarioServentia
			case Configuracao.Excluir:
				// Captura UsuarioServentia selecionado
				stId = request.getParameter("Id_UsuarioServentia");
				if (stId != null && stId.length() > 0) {
					usuarioDt.setId_UsuarioServentia(stId);
					request.setAttribute("Mensagem", "Clique para confirmar a Desativação do Usuário na Serventia: " + usuarioDt.getServentia() + ", OAB: " + usuarioDt.getUsuarioServentiaOab().getOabNumero());
				} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia/OAB para Desabilitação");
				break;

			// Desativar UsuarioServentia
			case Configuracao.ExcluirResultado:
				if (usuarioDt.getId_UsuarioServentia().length() > 0) {
					usuarioNe.desativarUsuarioServentia(usuarioDt);
					request.setAttribute("MensagemOk", "Usuário Desabilitado com Sucesso.");
					usuarioDt.setId_UsuarioServentia("");
					usuarioDt.setId_Serventia("null");
					usuarioDt.getUsuarioServentiaOab().setOabNumero("");
					usuarioDt.getUsuarioServentiaOab().setOabComplemento("");

					if (UsuarioSessao.getUsuarioDt() != null &&  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo() != null	&& Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_OAB)
						tempList = usuarioNe.consultarServentiaOabAdvogadoHabilitacao(usuarioDt.getId(), UsuarioSessao.getUsuarioDt().getId_Serventia());
					else
						tempList = usuarioNe.consultarServentiaOabAdvogado(usuarioDt.getId());
					
					usuarioDt.setListaUsuarioServentias(tempList);
				} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia/OAB para Desabilitação");
				break;

			case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				usuarioDt.limpar();
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome", "Usuário"};
					String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Usuario");
					request.setAttribute("tempBuscaDescricao","Usuario");
					request.setAttribute("tempBuscaPrograma","Servidor Judiciário");			
					request.setAttribute("tempRetorno","UsuarioServentiaOab");		
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
					if (UsuarioSessao.isCoordenadorOab() ){
						stTemp = usuarioNe.consultarTodosAdvogadosJSON(stNomeBusca1, stNomeBusca2, UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);
					}else{
						stTemp = usuarioNe.consultarTodosUsuariosJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					}
					enviarJSON(response, stTemp);
					return;								
				}
				break;

			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String stPermissao = String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_Serventia", "Serventia", "Serventia OAB", "UsuarioServentiaOab", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
				}else{
					String stTemp = "";
					stTemp = usuarioNe.consultarServentiasHabilitacaoAdvogadoJSON(stNomeBusca1, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);											
					return;	
				}
			
			break;			

			default:
				stId = request.getParameter("Id_UsuarioServentiaGrupo");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(usuarioDt.getId())) {
					usuarioDt.limpar();
					usuarioDt = usuarioNe.consultarId(stId);
				}
				stId = request.getParameter("Id_Usuario");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(usuarioDt.getId())) {
					usuarioDt.setId(stId);
					if (UsuarioSessao.getUsuarioDt() != null &&  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo() != null
							&& Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_OAB)
						tempList = usuarioNe.consultarServentiaOabAdvogadoHabilitacao(stId, UsuarioSessao.getUsuarioDt().getId_Serventia());
					else
						tempList = usuarioNe.consultarServentiaOabAdvogado(stId);
					
					if (tempList.size() > 0) usuarioDt.setListaUsuarioServentias(tempList);
				}
				break;
		}

		request.getSession().setAttribute("usuarioDt", usuarioDt);
		request.getSession().setAttribute("usuarioNe", usuarioNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
