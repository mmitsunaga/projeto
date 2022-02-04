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
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaMovimentacaoDt;
import br.gov.go.tj.projudi.ne.EstatisticaMovimentacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaMovimentacaoCt extends Controle {

	/**
     * 
     */
    private static final long serialVersionUID = 8558733203603577119L;

    public EstatisticaMovimentacaoCt() {

	}

	public int Permissao() {
		return EstatisticaMovimentacaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca,
			String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		//****************
		EstatisticaMovimentacaoDt EstatisticaMovimentacaodt;
		EstatisticaMovimentacaoNe EstatisticaMovimentacaone;
		//******************************

		List tempList = null;
		byte[] byTemp = null;
		String Mensagem = "";
		String stAcao = "/WEB-INF/jsptjgo/EstatisticaMovimentacao.jsp";

		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		String stNomeBusca2 = "";
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		request.setAttribute("tempPrograma", "EstatisticaMovimentacao");
		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempBuscaId_Usuario", "Id_Usuario");
		request.setAttribute("tempBuscaUsuario", "Usuario");
		request.setAttribute("tempBuscaId_UsuarioServentia", "Id_UsuarioServentia");
		request.setAttribute("tempRetorno", "EstatisticaMovimentacao");

		EstatisticaMovimentacaone = (EstatisticaMovimentacaoNe) request.getSession().getAttribute("EstatisticaMovimentacaone");
		if (EstatisticaMovimentacaone == null) EstatisticaMovimentacaone = new EstatisticaMovimentacaoNe();

		EstatisticaMovimentacaodt = (EstatisticaMovimentacaoDt) request.getSession().getAttribute("EstatisticaMovimentacaodt");
		if (EstatisticaMovimentacaodt == null) EstatisticaMovimentacaodt = new EstatisticaMovimentacaoDt();

		EstatisticaMovimentacaodt.setId_Serventia(request.getParameter("Id_Serventia"));
		EstatisticaMovimentacaodt.setServentia(request.getParameter("Serventia"));
		EstatisticaMovimentacaodt.getUsuario().setId(request.getParameter("Id_Usuario"));
		EstatisticaMovimentacaodt.getUsuario().setNome(request.getParameter("Usuario"));
		EstatisticaMovimentacaodt.getUsuario().setId_UsuarioServentia(request.getParameter("Id_UsuarioServentia"));
		EstatisticaMovimentacaodt.setDataInicial(request.getParameter("DataInicial"));
		EstatisticaMovimentacaodt.setDataFinal(request.getParameter("DataFinal"));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//--------------------------------------------------------------------------------//
		switch (paginaatual) {
		case Configuracao.Imprimir:
			if (EstatisticaMovimentacaodt.getId_Serventia().length() > 0 || EstatisticaMovimentacaodt.getUsuario().getId().length() > 0) {

				Mensagem = EstatisticaMovimentacaone.Verificar(EstatisticaMovimentacaodt);

				if (Mensagem.length() > 0) {
					request.setAttribute("MensagemErro", Mensagem);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				} else {
					if (EstatisticaMovimentacaodt.getId_Serventia().length() > 0
							&& EstatisticaMovimentacaodt.getUsuario().getId_UsuarioServentia().length() == 0) {
						EstatisticaMovimentacaodt = EstatisticaMovimentacaone
								.consultarDadosEstatisticaMovimentacaoServentia(EstatisticaMovimentacaodt);
						EstatisticaMovimentacaodt.getUsuario().setNome("");
					} else if (EstatisticaMovimentacaodt.getId_Serventia().length() > 0
							&& EstatisticaMovimentacaodt.getUsuario().getId_UsuarioServentia().length() > 0) {
						EstatisticaMovimentacaodt = EstatisticaMovimentacaone
								.consultarDadosEstatisticaMovimentacaoUsuarioServentia(EstatisticaMovimentacaodt);
					} else if (EstatisticaMovimentacaodt.getUsuario().getId().length() > 0
							&& EstatisticaMovimentacaodt.getUsuario().getId_UsuarioServentia().length() == 0) {
						EstatisticaMovimentacaodt.setServentia("");
						EstatisticaMovimentacaodt = EstatisticaMovimentacaone.consultarDadosEstatisticaMovimentacaoUsuario(EstatisticaMovimentacaodt);
					} else if (EstatisticaMovimentacaodt.getUsuario().getId().length() > 0
							&& EstatisticaMovimentacaodt.getUsuario().getId_UsuarioServentia().length() > 0) {
						EstatisticaMovimentacaodt = EstatisticaMovimentacaone
								.consultarDadosEstatisticaMovimentacaoUsuarioServentia(EstatisticaMovimentacaodt);
					}

					byTemp = EstatisticaMovimentacaone.relEstatisticaMovimentacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , EstatisticaMovimentacaodt);					
					
					enviarPDF(response,byTemp,"Relatorio");
											
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
			EstatisticaMovimentacaodt.limpar();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			EstatisticaMovimentacaodt.limparParametrosConsulta();
			
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia","Estado"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Serventia");
				request.setAttribute("tempBuscaDescricao","Serventia");
				request.setAttribute("tempBuscaPrograma","Consulta de Serventias");			
				request.setAttribute("tempRetorno","EstatisticaMovimentacao");		
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
				stTemp = EstatisticaMovimentacaone.consultarDescricaoServentiaTipoCodigoJSON(stNomeBusca1, PosicaoPaginaAtual, String.valueOf(ServentiaTipoDt.VARA));					
				
				enviarJSON(response, stTemp);					
				
				return;
			}
			break;
			
		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			EstatisticaMovimentacaodt.limparParametrosConsulta();
			
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Nome", "Usuário"};
				String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Usuario");
				request.setAttribute("tempBuscaDescricao","Usuario");
				request.setAttribute("tempBuscaPrograma","Servidor Judiciário");			
				request.setAttribute("tempRetorno","EstatisticaMovimentacao");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = EstatisticaMovimentacaone.consultarDescricaoServidorJudiciarioJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;								
			}
			break;
			
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (EstatisticaMovimentacaodt.getUsuario().getId() != null) if (!EstatisticaMovimentacaodt.getUsuario().getId().equalsIgnoreCase("")) {
				tempList = EstatisticaMovimentacaone.consultarServentiasGruposUsuario(EstatisticaMovimentacaodt.getUsuario().getId());
				if (tempList.size() > 0) {
					EstatisticaMovimentacaodt.getUsuario().setListaUsuarioServentias(tempList);
				}
			}
			if (EstatisticaMovimentacaodt.getId_Serventia() != null) if (!EstatisticaMovimentacaodt.getId_Serventia().equalsIgnoreCase("")) {
				tempList = EstatisticaMovimentacaone.consultarDescricaoServidorJudiciario(EstatisticaMovimentacaodt.getId_Serventia());
				if (tempList.size() > 0) {
					EstatisticaMovimentacaodt.getUsuario().setListaUsuarioServentias(tempList);
				}
			}
			break;
		}

		request.getSession().setAttribute("EstatisticaMovimentacaodt", EstatisticaMovimentacaodt);
		request.getSession().setAttribute("EstatisticaMovimentacaone", EstatisticaMovimentacaone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
