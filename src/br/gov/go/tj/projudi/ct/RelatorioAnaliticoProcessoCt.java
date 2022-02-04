package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAnaliticoDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioAnaliticoProcessoCt extends Controle {

	private static final long serialVersionUID = 58752094293520253L;

	public int Permissao() {
		return RelatorioAnaliticoDt.CodigoPermissaoAnaliticoProcesso;
	}

	public void executar(HttpServletRequest request,
			HttpServletResponse response, UsuarioNe UsuarioSessao,
			int paginaatual, String tempNomeBusca, String posicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		RelatorioAnaliticoDt relatorioAnaliticoDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");

		List tempList = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioAnaliticoProcesso");
		request.setAttribute("tempBuscaSistema", "Sistema");
		
		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioAnaliticoDt = (RelatorioAnaliticoDt) request.getSession().getAttribute("RelatorioAnaliticodt");
		if (relatorioAnaliticoDt == null)
			relatorioAnaliticoDt = new RelatorioAnaliticoDt();
		
		if (request.getParameter("Mes") != null && !request.getParameter("Mes").equals("")) {
			relatorioAnaliticoDt.setMes(request.getParameter("Mes"));
		}
		if (request.getParameter("Ano") != null && !request.getParameter("Ano").equals("")) {
			relatorioAnaliticoDt.setAno(request.getParameter("Ano"));
		}

		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				relatorioAnaliticoDt.setId_Serventia("");
			} else {
				relatorioAnaliticoDt.setId_Serventia(request.getParameter("Id_Serventia"));
			}
		}
		
		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioAnaliticoDt.setServentia("");
			} else {
				relatorioAnaliticoDt.setServentia(request.getParameter("Serventia"));
			}
		}
		
		if (request.getParameter("Id_Grupo") != null) {
			if (request.getParameter("Id_Grupo").equals("null")) {
				relatorioAnaliticoDt.setId_CargoTipo("");
			} else {
				relatorioAnaliticoDt.setId_CargoTipo(request.getParameter("Id_Grupo"));
			}
		}
		
		if (request.getParameter("Grupo") != null) {
			if (request.getParameter("Grupo").equals("null")) {
				relatorioAnaliticoDt.setCargoTipo("");
			} else {
				relatorioAnaliticoDt.setCargoTipo(request.getParameter("Grupo"));
			}
		}
			
		if (request.getParameter("Id_Usuario") != null) {
			if (request.getParameter("Id_Usuario").equals("null")) {
				relatorioAnaliticoDt.getUsuario().setId("");
			} else {
				relatorioAnaliticoDt.getUsuario().setId(request.getParameter("Id_Usuario"));
			}
		}
		
		if (request.getParameter("Usuario") != null) {
			if (request.getParameter("Usuario").equals("null")) {
				relatorioAnaliticoDt.getUsuario().setNome("");
			} else {
				relatorioAnaliticoDt.getUsuario().setNome(request.getParameter("Usuario"));
			}
		} 
		
		if (request.getParameter("Comarca") != null) {
			if (request.getParameter("Comarca").equals("null")) {
				relatorioAnaliticoDt.setComarca("");
			} else {
				relatorioAnaliticoDt.setComarca(request.getParameter("Comarca"));
			}
		}

		if (request.getParameter("Id_Comarca") != null) {
			if (request.getParameter("Id_Comarca").equals("null")) {
				relatorioAnaliticoDt.setId_Comarca("");
			} else {
				relatorioAnaliticoDt.setId_Comarca(request.getParameter("Id_Comarca"));
			}
		}

		if (request.getParameter("ProcessoTipo") != null) {
			if (request.getParameter("ProcessoTipo").equals("null")) {
				relatorioAnaliticoDt.setProcessoTipo("");
			} else {
				relatorioAnaliticoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
			}
		}

		if (request.getParameter("Id_ProcessoTipo") != null) {
			if (request.getParameter("Id_ProcessoTipo").equals("null")) {
				relatorioAnaliticoDt.setId_ProcessoTipo("");
			} else {
				relatorioAnaliticoDt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Relatório Analítico Processo");

		switch (paginaatual) {
		case Configuracao.Localizar:

			mensagemRetorno = "";
			boolean acessoEspecial = false;
			if(request.getSession().getAttribute("acessoEspecial") != null) {
				acessoEspecial = new Boolean(request.getSession().getAttribute("acessoEspecial").toString());
			}

			//Validações para o usuário com acesso especial
			if(acessoEspecial){
				if(relatorioAnaliticoDt.getId_Comarca() == null || relatorioAnaliticoDt.getId_Comarca().equalsIgnoreCase("") 
						|| relatorioAnaliticoDt.getId_Serventia() == null || relatorioAnaliticoDt.getId_Serventia().equalsIgnoreCase("")){
					mensagemRetorno = "Comarca e Serventia devem ser informados.";
				}
			}
			
			if (mensagemRetorno.equals("")) {
				request.getParameter("tipo_Arquivo");

				//Se o usuário tiver acesso especial, ele poderá consultar outras comarcas e serventias
				//se for usuário comum poderá consultar apenas sua própria comarca e serventia
				if(acessoEspecial){
					tempList = relatorioEstatisticaNe.relAnaliticoProcesso(relatorioAnaliticoDt.getAno(), relatorioAnaliticoDt.getMes(), relatorioAnaliticoDt.getId_Comarca(), relatorioAnaliticoDt.getId_Serventia(), relatorioAnaliticoDt.getId_ProcessoTipo(), posicaoPaginaAtual);
				} else {
					tempList = relatorioEstatisticaNe.relAnaliticoProcesso(relatorioAnaliticoDt.getAno(), relatorioAnaliticoDt.getMes(), UsuarioSessao.getUsuarioDt().getId_Comarca(), UsuarioSessao.getUsuarioDt().getId_Serventia(), relatorioAnaliticoDt.getId_ProcessoTipo(), posicaoPaginaAtual);
				}
				
				if (tempList.size() > 0) {
					request.setAttribute("listaProcessos", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", relatorioEstatisticaNe.getQuantidadePaginas());
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				
				
			} else {
				request.setAttribute("MensagemErro", mensagemRetorno);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			
			if(request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")){
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;
		case Configuracao.Novo:
			relatorioAnaliticoDt.limparCamposConsulta();
			relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if(request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")){
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia", "Estado"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "RelatorioAnaliticoProcesso");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarServentiasComarcaJSON(stNomeBusca1, relatorioAnaliticoDt.getId_Comarca(),posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Comarca"};
				String[] lisDescricao = {"Comarca"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Comarca");
				request.setAttribute("tempBuscaDescricao","Comarca");
				request.setAttribute("tempBuscaPrograma","Forum");			
				request.setAttribute("tempRetorno","RelatorioAnaliticoProcesso");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = relatorioEstatisticaNe.consultarDescricaoComarcaJSON(stNomeBusca1, posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;								
			}
			break;
		case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"ProcessoTipo"};
				String[] lisDescricao = {"ProcessoTipo"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_ProcessoTipo");
				request.setAttribute("tempBuscaDescricao","ProcessoTipo");
				request.setAttribute("tempBuscaPrograma","Tipo de Processo");			
				request.setAttribute("tempRetorno","RelatorioAnaliticoProcesso");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = relatorioEstatisticaNe.consultarDescricaoProcessoTipoJSON(stNomeBusca1, posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;								
			}
			break;
			
		case Configuracao.Curinga6:
			//Curinga de acesso comum
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			if (relatorioAnaliticoDt.getMes().equals("") && relatorioAnaliticoDt.getAno().equals("")) {
				relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
			}
			
			stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoProcesso.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", false);
			
			break;
		
		case Configuracao.Curinga7:
			//Curinga para acesso especial
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			if (relatorioAnaliticoDt.getMes().equals("") && relatorioAnaliticoDt.getAno().equals("")) {
				relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
			}
			
			stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoProcessoCorregedoria.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", true);
			
			break;
		case Configuracao.Curinga8:
			redireciona(response, "BuscaProcesso?Id_Processo=" + request.getParameter("Id_Processo").toString());
			break;
			
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);

		if (relatorioAnaliticoDt.getMes().equals("") && relatorioAnaliticoDt.getAno().equals("")) {
				relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
			}
			
			if(request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")){
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}

			break;
		}
		
		request.setAttribute("tempPrograma", "Relatório Analítico de Processo");

		request.getSession().setAttribute("RelatorioAnaliticodt", relatorioAnaliticoDt);
		request.getSession().setAttribute("RelatorioAnaliticone", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * 
	 * @param relatorioAnaliticoDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioAnaliticoDt atribuirDataAtual(RelatorioAnaliticoDt relatorioAnaliticoDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioAnaliticoDt.setMes(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioAnaliticoDt.setAno(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioAnaliticoDt;
	}

}
