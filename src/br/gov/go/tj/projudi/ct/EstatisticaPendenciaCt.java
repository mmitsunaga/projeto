package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaPendenciaDt;
import br.gov.go.tj.projudi.ne.EstatisticaPendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaPendenciaCt extends Controle {

	/**
     * 
     */
    private static final long serialVersionUID = -2730025134323193108L;

    public EstatisticaPendenciaCt() {

	}

	public int Permissao() {
		return EstatisticaPendenciaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca,
			String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		//****************
		EstatisticaPendenciaDt EstatisticaPendenciaServentiadt;
		EstatisticaPendenciaNe EstatisticaPendenciaServentiane;
		//******************************

		List tempList = null;
		byte[] byTemp = null;
		String Mensagem = "";
		String stAcao = "/WEB-INF/jsptjgo/EstatisticaPendencia.jsp";

		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		String stNomeBusca2 = "";
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		request.setAttribute("tempPrograma", "EstatisticaPendencia");
		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempBuscaId_Usuario", "Id_Usuario");
		request.setAttribute("tempBuscaUsuario", "Usuario");
		request.setAttribute("tempBuscaId_UsuarioServentia", "Id_UsuarioServentia");
		request.setAttribute("tempRetorno", "EstatisticaPendencia");

		EstatisticaPendenciaServentiane = (EstatisticaPendenciaNe) request.getSession().getAttribute("EstatisticaPendenciaServentiane");
		if (EstatisticaPendenciaServentiane == null) EstatisticaPendenciaServentiane = new EstatisticaPendenciaNe();

		EstatisticaPendenciaServentiadt = (EstatisticaPendenciaDt) request.getSession().getAttribute("EstatisticaPendenciaServentiadt");
		if (EstatisticaPendenciaServentiadt == null) EstatisticaPendenciaServentiadt = new EstatisticaPendenciaDt();

		EstatisticaPendenciaServentiadt.setId_Serventia(request.getParameter("Id_Serventia"));
		EstatisticaPendenciaServentiadt.setServentia(request.getParameter("Serventia"));
		EstatisticaPendenciaServentiadt.getUsuario().setId(request.getParameter("Id_Usuario"));
		EstatisticaPendenciaServentiadt.getUsuario().setNome(request.getParameter("Usuario"));
		EstatisticaPendenciaServentiadt.getUsuario().setId_UsuarioServentia(request.getParameter("Id_UsuarioServentia"));
		EstatisticaPendenciaServentiadt.setDataInicial(request.getParameter("DataInicial"));
		EstatisticaPendenciaServentiadt.setDataFinal(request.getParameter("DataFinal"));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//--------------------------------------------------------------------------------//
		switch (paginaatual) {
		case Configuracao.Imprimir:
			if (EstatisticaPendenciaServentiadt.getId_Serventia().length() > 0 || EstatisticaPendenciaServentiadt.getUsuario().getId().length() > 0) {

				Mensagem = EstatisticaPendenciaServentiane.Verificar(EstatisticaPendenciaServentiadt);

				if (Mensagem.length() > 0) {
					request.setAttribute("MensagemErro", Mensagem);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				} else {
					if (EstatisticaPendenciaServentiadt.getId_Serventia().length() > 0
							&& EstatisticaPendenciaServentiadt.getUsuario().getId_UsuarioServentia().length() == 0) {
						EstatisticaPendenciaServentiadt = EstatisticaPendenciaServentiane
								.consultarDadosEstatisticaPendenciaServentia(EstatisticaPendenciaServentiadt);
						EstatisticaPendenciaServentiadt.getUsuario().setNome("");
					} else if (EstatisticaPendenciaServentiadt.getId_Serventia().length() > 0
							&& EstatisticaPendenciaServentiadt.getUsuario().getId_UsuarioServentia().length() > 0) {
						EstatisticaPendenciaServentiadt = EstatisticaPendenciaServentiane
								.consultarDadosEstatisticaPendenciaUsuarioServentia(EstatisticaPendenciaServentiadt);
					} else if (EstatisticaPendenciaServentiadt.getUsuario().getId().length() > 0
							&& EstatisticaPendenciaServentiadt.getUsuario().getId_UsuarioServentia().length() == 0) {
						EstatisticaPendenciaServentiadt.setServentia("");
						EstatisticaPendenciaServentiadt = EstatisticaPendenciaServentiane
								.consultarDadosEstatisticaPendenciaUsuario(EstatisticaPendenciaServentiadt);
					} else if (EstatisticaPendenciaServentiadt.getUsuario().getId().length() > 0
							&& EstatisticaPendenciaServentiadt.getUsuario().getId_UsuarioServentia().length() > 0) {
						EstatisticaPendenciaServentiadt = EstatisticaPendenciaServentiane
								.consultarDadosEstatisticaPendenciaUsuarioServentia(EstatisticaPendenciaServentiadt);
					}

					byTemp = EstatisticaPendenciaServentiane.relEstatisticaPendencia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,			EstatisticaPendenciaServentiadt);
									
					enviarPDF(response,byTemp,"Relatório");
					
					return;
				}

			} else {
				request.setAttribute("MensagemErro", "Selecione uma Serventia ou um Servidor Judiciário.");
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;
		case Configuracao.LocalizarDWR:
			break;
		case Configuracao.Novo:
			EstatisticaPendenciaServentiadt.limpar();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			EstatisticaPendenciaServentiadt.limparParametrosConsulta();
			
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia","Estado"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Serventia");
				request.setAttribute("tempBuscaDescricao","Serventia");
				request.setAttribute("tempBuscaPrograma","Consulta de Serventias");			
				request.setAttribute("tempRetorno","EstatisticaPendencia");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				request.setAttribute("Curinga", "ServentiaVara");
			}else{
				String stTemp = "";
				stTemp = EstatisticaPendenciaServentiane.consultarDescricaoServentiaTipoCodigoJSON(stNomeBusca1, PosicaoPaginaAtual, String.valueOf(ServentiaTipoDt.VARA));
					
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
			
		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			EstatisticaPendenciaServentiadt.limparParametrosConsulta();
			
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Nome", "Usuário"};
				String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Usuario");
				request.setAttribute("tempBuscaDescricao","Usuario");
				request.setAttribute("tempBuscaPrograma","Servidor Judiciário");			
				request.setAttribute("tempRetorno","EstatisticaPendencia");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = EstatisticaPendenciaServentiane.consultarDescricaoServidorJudiciarioJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;								
			}
			break;

		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (EstatisticaPendenciaServentiadt.getUsuario().getId() != null)
				if (!EstatisticaPendenciaServentiadt.getUsuario().getId().equalsIgnoreCase("")) {
					tempList = EstatisticaPendenciaServentiane.consultarServentiasGruposUsuario(EstatisticaPendenciaServentiadt.getUsuario().getId());
					if (tempList.size() > 0) {
						EstatisticaPendenciaServentiadt.getUsuario().setListaUsuarioServentias(tempList);
					}
				}
			if (EstatisticaPendenciaServentiadt.getId_Serventia() != null)
				if (!EstatisticaPendenciaServentiadt.getId_Serventia().equalsIgnoreCase("")) {
					tempList = EstatisticaPendenciaServentiane
							.consultarDescricaoServidorJudiciario(EstatisticaPendenciaServentiadt.getId_Serventia());
					if (tempList.size() > 0) {
						EstatisticaPendenciaServentiadt.getUsuario().setListaUsuarioServentias(tempList);
					}
				}
			break;
		}

		request.getSession().setAttribute("EstatisticaPendenciaServentiadt", EstatisticaPendenciaServentiadt);
		request.getSession().setAttribute("EstatisticaPendenciaServentiane", EstatisticaPendenciaServentiane);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
