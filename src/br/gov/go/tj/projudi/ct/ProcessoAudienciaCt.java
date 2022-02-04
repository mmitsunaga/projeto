package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoAudienciaDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoAudienciaCt extends Controle {

	private static final long serialVersionUID = 855864551617865648L;

	public int Permissao() {
		return ProcessoAudienciaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoAudienciaDt processoAudienciaDt;
		ProcessoNe processoNe;

		List tempList = null;
		String stAcao = "WEB-INF/jsptjgo/ProcessoAudienciaPesquisa.jsp";
		boolean acessoEspecial;

		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempRetorno", "ProcessoAudiencia");
		
		processoNe = (ProcessoNe) request.getSession().getAttribute("processoNe");
		if (processoNe == null)
			processoNe = new ProcessoNe();

		processoAudienciaDt = (ProcessoAudienciaDt) request.getSession().getAttribute("processoAudienciaDt");
		if (processoAudienciaDt == null)
			processoAudienciaDt = new ProcessoAudienciaDt();

		processoAudienciaDt.setIdServentia(request.getParameter("Id_Serventia"));
		processoAudienciaDt.setServentia(request.getParameter("Serventia"));
		processoAudienciaDt.setPeriodo(request.getParameter("Periodo"));
		processoAudienciaDt.setTipoAudiencia(request.getParameter("TipoAudiencia"));
		processoAudienciaDt.setTipoAudienciaAnterior(request.getParameter("TipoAudienciaAnterior"));
		processoAudienciaDt.setAPartirDa(request.getParameter("APartirDa"));
		processoAudienciaDt.setIdServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		processoAudienciaDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));

		acessoEspecial = new Boolean(request.getParameter("acessoEspecial"));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Processos Aguardando Audiência");
		request.setAttribute("TituloPagina", "Processos Aguardando Audiência");

		switch (paginaatual) {
		case Configuracao.Imprimir:
			break;
		case Configuracao.LocalizarDWR:
			break;
		case Configuracao.Localizar:
			String mensagemErro = "";
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));

			if (acessoEspecial) {
				if (processoAudienciaDt.getIdServentia() == null || processoAudienciaDt.getIdServentia().equalsIgnoreCase("") || processoAudienciaDt.getIdServentia().equalsIgnoreCase("null")) {
					mensagemErro = "O campo Serventia é obrigatório.";
				}
			}

			if (processoAudienciaDt.getAPartirDa() != null && processoAudienciaDt.getAPartirDa().equals("2")) {
				if (processoAudienciaDt.getTipoAudienciaAnterior() == null || processoAudienciaDt.getTipoAudienciaAnterior().equalsIgnoreCase("")) {
					mensagemErro = "É obrigatório selecionar o tipo de audiência anterior.";
				}
			}

			if (mensagemErro.equalsIgnoreCase("")) {
				if (acessoEspecial) {
					tempList = processoNe.consultarProcessosAguardandoAudiencia(processoAudienciaDt.getIdServentia(), processoAudienciaDt.getIdServentiaCargo(), processoAudienciaDt.getTipoAudiencia(), processoAudienciaDt.getPeriodo(), processoAudienciaDt.getAPartirDa(), processoAudienciaDt.getTipoAudienciaAnterior(), posicaoPaginaAtual);
				} else {
					tempList = processoNe.consultarProcessosAguardandoAudiencia(UsuarioSessao.getUsuarioDt().getId_Serventia(), processoAudienciaDt.getIdServentiaCargo(), processoAudienciaDt.getTipoAudiencia(), processoAudienciaDt.getPeriodo(), processoAudienciaDt.getAPartirDa(), processoAudienciaDt.getTipoAudienciaAnterior(), posicaoPaginaAtual);
				}

				if (tempList.size() > 0) {
					request.setAttribute("ListaProcessos", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
					request.setAttribute("Curinga", "ServentiaVara");
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
			} else {
				request.setAttribute("MensagemErro", mensagemErro);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}

			break;
		case Configuracao.Novo:
			processoAudienciaDt.limpar();
			break;
		case Configuracao.Curinga6:
			redireciona(response, "BuscaProcesso?Id_Processo=" + request.getParameter("Id_Processo").toString());
			break;
		case Configuracao.Curinga7:
			acessoEspecial = true;
			break;
		case Configuracao.Curinga8:
			acessoEspecial = false;
			break;
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			tempList = processoNe.consultarServentia(tempNomeBusca, posicaoPaginaAtual, (UsuarioDt) request.getSession().getAttribute("UsuarioSessaoDt"));
			stAcao = "/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
			request.setAttribute("tempPrograma", "Processo Aguardando Audiência");
			if (acessoEspecial) {
				request.setAttribute("tempRetorno", "ProcessoAudiencia?PaginaAtual=7");
			} else {
				request.setAttribute("tempRetorno", "ProcessoAudiencia?PaginaAtual=8");
			}
			if (tempList.size() > 0) {
				request.setAttribute("ListaServentia", tempList);
				request.setAttribute("PaginaAtual", paginaatual);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
				request.setAttribute("Curinga", "ServentiaVara");
			} else {
				request.setAttribute("MensagemErro", "Dados Não Localizados");
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;
		// Consultar Juiz Responsável - Serventia Cargo
		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
		if (request.getParameter("Passo")==null){
			String[] lisNomeBusca = {"ServentiaCargo"};
			String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
			String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
			request.setAttribute("camposHidden",camposHidden);
			stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
			request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
			request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
			request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
			request.setAttribute("tempRetorno", "ProcessoAudiencia");
			request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
			request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
			request.setAttribute("PosicaoPaginaAtual", "0");
			request.setAttribute("QuantidadePaginas", "0");
			request.setAttribute("lisNomeBusca", lisNomeBusca);
			request.setAttribute("lisDescricao", lisDescricao);
		}else{
			String stTemp = "";
			
			stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, posicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
				
			enviarJSON(response, stTemp);
				
			
			return;
		}
		
		
		
		
		
			break;	
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}

		request.setAttribute("acessoEspecial", acessoEspecial);
		request.getSession().setAttribute("processoAudienciaDt", processoAudienciaDt);
		request.getSession().setAttribute("processoNe", processoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}