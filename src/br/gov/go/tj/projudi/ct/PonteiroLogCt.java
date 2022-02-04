package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.PonteiroLogNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.TJDataHora;

public class PonteiroLogCt extends PonteiroLogCtGen {

	private static final long serialVersionUID = 4817433859212400686L;

	public int Permissao() {
		return PonteiroLogDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PonteiroLogDt PonteiroLogdt;
		PonteiroLogNe PonteiroLogne;

		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/PonteiroLog.jsp";

		request.setAttribute("tempPrograma", "PonteiroLog");

		PonteiroLogne = (PonteiroLogNe) request.getSession().getAttribute("PonteiroLogne");
		
		if (PonteiroLogne == null){
			PonteiroLogne = new PonteiroLogNe();
		}

		PonteiroLogdt = (PonteiroLogDt) request.getSession().getAttribute("PonteiroLogdt");
		
		if (PonteiroLogdt == null){
			PonteiroLogdt = new PonteiroLogDt();
		}
		
		PonteiroLogdt.setData(new TJDataHora().getDataFormatadaddMMyyyyHHmmssSemSeparador());
		
		if(request.getParameter("Id_PonteiroLog") == null || request.getParameter("Id_PonteiroLog").equalsIgnoreCase("")){
			request.setAttribute("classe", "formEdicaoInput");
			request.setAttribute("readonly", "");			
		} else {
			request.setAttribute("classe", "formEdicaoInputSomenteLeitura");
			request.setAttribute("readonly", "readonly");
		}

		PonteiroLogdt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
		PonteiroLogdt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		PonteiroLogdt.setId_PonteiroLogTipo(request.getParameter("Id_PonteiroLogTipo"));
		PonteiroLogdt.setPonteiroLogTipo(request.getParameter("PonteiroLogTipo"));
		PonteiroLogdt.setId_Proc(request.getParameter("Id_Processo"));
		PonteiroLogdt.setProcNumero(request.getParameter("Processo"));
		PonteiroLogdt.setId_Serventia(request.getParameter("Id_Serventia"));
		PonteiroLogdt.setServentia(request.getParameter("Serventia"));
		PonteiroLogdt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		PonteiroLogdt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		PonteiroLogdt.setServentiaCargoUsuario(request.getParameter("ServentiaCargoUsuario"));
		PonteiroLogdt.setData(request.getParameter("Data"));
		PonteiroLogdt.setJustificativa(request.getParameter("Justificativa"));
		PonteiroLogdt.setQtd(request.getParameter("Qtd"));
		PonteiroLogdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PonteiroLogdt.setIpComputadorLog(request.getRemoteAddr());

		if(request.getParameter("Id_UsuarioServentia") != null && !request.getParameter("Id_UsuarioServentia").equalsIgnoreCase("")){
			PonteiroLogdt.setId_UsuarioServentia(request.getParameter("Id_UsuarioServentia"));
			PonteiroLogdt.setNome(request.getParameter("UsuarioServentia"));
		} else {
			PonteiroLogdt.setId_UsuarioServentia(UsuarioSessao.getId_UsuarioServentia());
			PonteiroLogdt.setNome(UsuarioSessao.getUsuarioDt().getNome());
		}

		if (request.getParameter("nomeBusca1") != null){
			stNomeBusca1 = request.getParameter("nomeBusca1");
		}
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar); // é a página padrão

		switch (paginaatual) {
		
		// não poderá excluir
		// case Configuracao.ExcluirResultado: //Excluir
		// PonteiroLogne.excluir(PonteiroLogdt);
		// request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
		// break;

		case Configuracao.Imprimir:
			break;

		case Configuracao.Localizar: // localizar
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Justificativa"};
				String[] lisDescricao = {"Justificativa"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_PonteiroLog");
				request.setAttribute("tempBuscaDescricao", "Log de Ponteiro");
				request.setAttribute("tempBuscaPrograma", "Log de Ponteiro");
				request.setAttribute("tempRetorno", "PonteiroLog");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = PonteiroLogne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;

		case Configuracao.Novo:
			
			PonteiroLogdt.limpar();			
			request.setAttribute("classe", "formEdicaoInput");
			request.setAttribute("readonly", "");
			
			//sempre que limpar o DT, a data e usuário devem ser setados dessa forma
			PonteiroLogdt.setData(new TJDataHora().getDataFormatadaddMMyyyyHHmmssSemSeparador());
			PonteiroLogdt.setId_UsuarioServentia(UsuarioSessao.getId_UsuarioServentia());
			PonteiroLogdt.setNome(UsuarioSessao.getUsuarioDt().getNome());
			
			break;

		case Configuracao.SalvarResultado:
			
			if(request.getParameter("Id_PonteiroLog") == null || request.getParameter("Id_PonteiroLog").equalsIgnoreCase("")){ // condicional adicionada para impedir que haja edição de um cadastro. Só é permitido inclusão.
			
				Mensagem = PonteiroLogne.Verificar(PonteiroLogdt);
				if (Mensagem.length() == 0) {
					PonteiroLogne.salvar(PonteiroLogdt);
					request.setAttribute("MensagemOk", "Dados Salvos com Sucesso!");
				} else {
					request.setAttribute("MensagemErro", Mensagem);
				}
			} else {
				request.setAttribute("MensagemErro", "Não é permitido fazer alterações.\nCrie um novo registro.");
			}			
			break;
			
		case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"AreaDistribuicao"};
				String[] lisDescricao = {"AreaDistribuicao"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
				request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
				request.setAttribute("tempBuscaPrograma", "AreaDistribuicao");
				request.setAttribute("tempRetorno", "PonteiroLog");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",	(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = PonteiroLogne.consultarDescricaoAreaDistribuicaoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		case (PonteiroLogTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"PonteiroLogTipo"};
				String[] lisDescricao = {"PonteiroLogTipo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_PonteiroLogTipo");
				request.setAttribute("tempBuscaDescricao", "PonteiroLogTipo");
				request.setAttribute("tempBuscaPrograma", "PonteiroLogTipo");
				request.setAttribute("tempRetorno", "PonteiroLog");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",	(PonteiroLogTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = PonteiroLogne.consultarDescricaoPonteiroLogTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		case (ProcessoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Processo"};
				String[] lisDescricao = {"Processo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Processo");
				request.setAttribute("tempBuscaDescricao", "Processo");
				request.setAttribute("tempBuscaPrograma", "Processo");
				request.setAttribute("tempRetorno", "PonteiroLog");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ProcessoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = PonteiroLogne.consultarDescricaoProcessoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "PonteiroLog");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",	(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = PonteiroLogne.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Serventia Cargo" };
				String[] lisDescricao = { "Serventia Cargo", "Usuário", "Cargo Tipo" };
				String[] camposHidden = { "ServentiaCargoUsuario", "CargoTipo" };
				request.setAttribute("camposHidden", camposHidden);
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
				request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
				request.setAttribute("tempBuscaPrograma", "Serventia Cargo");
				request.setAttribute("tempRetorno", "PonteiroLog");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(PonteiroLogdt.getId_Serv());
				String stTemp = PonteiroLogne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, PonteiroLogdt.getId_Serv(), serventiaDt.getServentiaTipoCodigo(), serventiaDt.getServentiaSubtipoCodigo());
				enviarJSON(response, stTemp);

				return;
			}
		break;

		case (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"UsuarioServentia"};
				String[] lisDescricao = {"UsuarioServentia"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_UsuarioServentia");
				request.setAttribute("tempBuscaDescricao", "UsuarioServentia");
				request.setAttribute("tempBuscaPrograma", "UsuarioServentia");
				request.setAttribute("tempRetorno", "PonteiroLog");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",
						(UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = PonteiroLogne.consultarDescricaoUsuarioServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		default:
			stId = request.getParameter("Id_PonteiroLog");
			if (stId != null && !stId.isEmpty()){
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(PonteiroLogdt.getId())) {
					PonteiroLogdt.limpar();
					PonteiroLogdt = PonteiroLogne.consultarId(stId);
				}				
			}
			break;
		}

		request.getSession().setAttribute("PonteiroLogdt", PonteiroLogdt);
		request.getSession().setAttribute("PonteiroLogne", PonteiroLogne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}