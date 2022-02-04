package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.PonteiroLogCompensarNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PonteiroLogCompensarCt extends Controle {

	private static final long serialVersionUID = 6780575386154028232L;

	public int Permissao() {
		return PonteiroLogCompensarDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PonteiroLogCompensarDt ponteiroLogCompensarDt;
		PonteiroLogCompensarNe ponteiroLogCompensarNe;

		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/PonteiroLogCompensar.jsp";
		request.setAttribute("tempPrograma", "PonteiroLogCompensar");

		ponteiroLogCompensarNe = (PonteiroLogCompensarNe) request.getSession().getAttribute("ponteiroLogCompensarNe");
		if (ponteiroLogCompensarNe == null)
			ponteiroLogCompensarNe = new PonteiroLogCompensarNe();

		ponteiroLogCompensarDt = (PonteiroLogCompensarDt) request.getSession().getAttribute("ponteiroLogCompensarDt");
		if (ponteiroLogCompensarDt == null)
			ponteiroLogCompensarDt = new PonteiroLogCompensarDt();

		ponteiroLogCompensarDt.setJustificativa(request.getParameter("Justificativa"));
		ponteiroLogCompensarDt.setId_AreaDistribuicao_O(request.getParameter("Id_AreaDistribuicao_O"));
		ponteiroLogCompensarDt.setAreaDistribuicao_O(request.getParameter("AreaDistribuicao_O"));
		ponteiroLogCompensarDt.setId_Serventia_O(request.getParameter("Id_Serventia_O"));
		ponteiroLogCompensarDt.setServentia_O(request.getParameter("Serventia_O"));
		ponteiroLogCompensarDt.setId_ServentiaCargo_O(request.getParameter("Id_ServentiaCargo_O"));
		ponteiroLogCompensarDt.setServentiaCargo_O(request.getParameter("ServentiaCargo_O"));
		ponteiroLogCompensarDt.setId_AreaDistribuicao_D(request.getParameter("Id_AreaDistribuicao_D"));
		ponteiroLogCompensarDt.setAreaDistribuicao_D(request.getParameter("AreaDistribuicao_D"));
		ponteiroLogCompensarDt.setId_Serventia_D(request.getParameter("Id_Serventia_D"));
		ponteiroLogCompensarDt.setServentia_D(request.getParameter("Serventia_D"));
		ponteiroLogCompensarDt.setId_ServentiaCargo_D(request.getParameter("Id_ServentiaCargo_D"));
		ponteiroLogCompensarDt.setServentiaCargo_D(request.getParameter("ServentiaCargo_D"));
		ponteiroLogCompensarDt.setQtd(request.getParameter("Qtd"));
		ponteiroLogCompensarDt.setDataInicio(request.getParameter("DataInicio"));
		ponteiroLogCompensarDt.setId_UsuarioServentia_I(request.getParameter("Id_UsuarioServentia_I"));
		ponteiroLogCompensarDt.setUsuario_I(request.getParameter("Usuario_I"));
		ponteiroLogCompensarDt.setDataFinal(request.getParameter("DataFinal"));
		ponteiroLogCompensarDt.setId_UsuarioServentia_F(request.getParameter("Id_UsuarioServentia_F"));
		ponteiroLogCompensarDt.setUsuario_F(request.getParameter("Usuario_F"));
		ponteiroLogCompensarDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		ponteiroLogCompensarDt.setIpComputadorLog(request.getRemoteAddr());
		
		//Essa validação é feita para controlar quando a busca é realizada para o campo Área de Origem ou de Destino
		if(request.getParameter("idRetorno") != null && request.getParameter("idRetorno").equalsIgnoreCase("Id_AreaDistribuicao_O")) {
			//limpando campos que devem ser preenchidos após essa consulta
			ponteiroLogCompensarDt.setId_Serventia_O("");
			ponteiroLogCompensarDt.setServentia_O("");
			ponteiroLogCompensarDt.setId_ServentiaCargo_O("");
			ponteiroLogCompensarDt.setServentiaCargo_O("");
			
		} 
		if(request.getParameter("idRetorno") != null && request.getParameter("idRetorno").equalsIgnoreCase("Id_Serventia_O")) {
			//limpando campos que devem ser preenchidos após essa consulta
			ponteiroLogCompensarDt.setId_ServentiaCargo_O("");
			ponteiroLogCompensarDt.setServentiaCargo_O("");
		}

		if(request.getParameter("Id_AreaDistribuicao_D") != null && !request.getParameter("Id_AreaDistribuicao_D").equalsIgnoreCase("")) {		
			//limpando campos que devem ser preenchidos após essa consulta
			ponteiroLogCompensarDt.setId_Serventia_D("");
			ponteiroLogCompensarDt.setServentia_D("");
			ponteiroLogCompensarDt.setId_ServentiaCargo_D("");
			ponteiroLogCompensarDt.setServentiaCargo_D("");
		}
		if(request.getParameter("Id_Serventia_D") != null && !request.getParameter("Id_Serventia_D").equalsIgnoreCase("")) {
			//limpando campos que devem ser preenchidos após essa consulta
			ponteiroLogCompensarDt.setId_ServentiaCargo_D("");
			ponteiroLogCompensarDt.setServentiaCargo_D("");
		}
		
		if(request.getParameter("Id_PonteiroLogCompensar") != null && !request.getParameter("Id_PonteiroLogCompensar").equalsIgnoreCase("")){
			request.setAttribute("telaEdicao", 1);
		} else {
			request.removeAttribute("telaEdicao");
		}
		
		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
		case Configuracao.Excluir: // Excluir
			request.setAttribute("Mensagem", "Confirma a finalização do ponteiro de compensação?");
			break; 
			
		case Configuracao.ExcluirResultado: // Excluir
			String idPonteiro = ponteiroLogCompensarDt.getId();
			ponteiroLogCompensarNe.finalizarPonteiro(ponteiroLogCompensarDt, usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
			request.setAttribute("MensagemOk", "Ponteiro finalizado com sucesso.");
			ponteiroLogCompensarDt = ponteiroLogCompensarNe.consultarId(idPonteiro);
			break;

		case Configuracao.Localizar: // localizar
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Descrição" };
				String[] lisDescricao = { "Descrição", "Data Início", "Data Fim" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_PonteiroLogCompensar");
				request.setAttribute("tempBuscaDescricao", "Ponteiro de Compensação");
				request.setAttribute("tempBuscaPrograma", "Ponteiro de Compensação");
				request.setAttribute("tempRetorno", "PonteiroLogCompensar");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = ponteiroLogCompensarNe.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
				try {
					response.setContentType("text/x-json");
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				} catch (Exception e) {
				}
				return;
			}
			break;
			
		case Configuracao.Novo:
			ponteiroLogCompensarDt.limpar();
			request.removeAttribute("telaEdicao");
			break;
			
		case Configuracao.SalvarResultado:
			Mensagem = ponteiroLogCompensarNe.Verificar(ponteiroLogCompensarDt);
			if (Mensagem.length() == 0) {
				if(ponteiroLogCompensarDt.getDataInicio() != null && !ponteiroLogCompensarDt.getDataInicio().equalsIgnoreCase("")){
					ponteiroLogCompensarDt.setId_UsuarioServentia_I(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				}
				if(ponteiroLogCompensarDt.getDataFinal() != null && !ponteiroLogCompensarDt.getDataFinal().equalsIgnoreCase("")){
					ponteiroLogCompensarDt.setId_UsuarioServentia_F(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				}
				
				ponteiroLogCompensarNe.salvar(ponteiroLogCompensarDt);
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				ponteiroLogCompensarDt = ponteiroLogCompensarNe.consultarId(ponteiroLogCompensarDt.getId());
				request.setAttribute("telaEdicao", 1);
			} else
				request.setAttribute("MensagemErro", Mensagem);
			break;

		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "ServentiaCargo" };
				String[] lisDescricao = { "ServentiaCargo", "Grupo", "Usuário", "Serventia" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", request.getParameter("idRetorno").toString());
				request.setAttribute("tempBuscaDescricao", request.getParameter("descricaoRetorno").toString());
				//usando o tempFluxo1 para ser usado no passo seguinte da busca json
				request.setAttribute("tempFluxo1", request.getParameter("idRetorno").toString());
				request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
				request.setAttribute("tempRetorno", "PonteiroLogCompensar");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = null; 
				if(request.getParameter("tempFluxo1").toString().equalsIgnoreCase("Id_ServentiaCargo_O")) {
					stTemp = ponteiroLogCompensarNe.consultarDescricaoServentiaCargoJSON(stNomeBusca1, ponteiroLogCompensarDt.getId_Serventia_O(), PosicaoPaginaAtual);
				} else {
					stTemp = ponteiroLogCompensarNe.consultarDescricaoServentiaCargoJSON(stNomeBusca1, ponteiroLogCompensarDt.getId_Serventia_D(), PosicaoPaginaAtual);
				}
				try {
					response.setContentType("text/x-json");
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				} catch (Exception e) {
				}
				return;
			}
			break;
			
		case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "AreaDistribuicao" };
				String[] lisDescricao = { "AreaDistribuicao" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", request.getParameter("idRetorno").toString());
				request.setAttribute("tempBuscaDescricao", request.getParameter("descricaoRetorno").toString());
				request.setAttribute("tempBuscaPrograma", "AreaDistribuicao");
				request.setAttribute("tempRetorno", "PonteiroLogCompensar");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = ponteiroLogCompensarNe.consultarDescricaoAreaDistribuicaoJSON(stNomeBusca1, PosicaoPaginaAtual);  
				try {
					response.setContentType("text/x-json");
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				} catch (Exception e) {
				}
				return;
			}
			break;
			
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Serventia" };
				String[] lisDescricao = { "Serventia" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", request.getParameter("idRetorno").toString());
				request.setAttribute("tempBuscaDescricao", request.getParameter("descricaoRetorno").toString());
				//usando o tempFluxo1 para ser usado no passo seguinte da busca json
				request.setAttribute("tempFluxo1", request.getParameter("idRetorno").toString());
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "PonteiroLogCompensar");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = null;
				if(request.getParameter("tempFluxo1").toString().equalsIgnoreCase("Id_Serventia_O")) {
					stTemp = ponteiroLogCompensarNe.consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, ponteiroLogCompensarDt.getId_AreaDistribuicao_O(), PosicaoPaginaAtual);
				} else {
					stTemp = ponteiroLogCompensarNe.consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, ponteiroLogCompensarDt.getId_AreaDistribuicao_D(), PosicaoPaginaAtual);
				}
				try {
					response.setContentType("text/x-json");
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				} catch (Exception e) {
				}
				return;
			}
			break;

		default:
			stId = request.getParameter("Id_PonteiroLogCompensar");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ponteiroLogCompensarDt.getId())) {
					ponteiroLogCompensarDt.limpar();
					ponteiroLogCompensarDt = ponteiroLogCompensarNe.consultarId(stId);
				}
			break;
		}

		request.getSession().setAttribute("ponteiroLogCompensarDt", ponteiroLogCompensarDt);
		request.getSession().setAttribute("ponteiroLogCompensarNe", ponteiroLogCompensarNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}