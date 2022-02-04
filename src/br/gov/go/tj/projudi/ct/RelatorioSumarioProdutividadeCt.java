package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioSumarioProdutividadeCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioSumarioDt.CodigoPermissaoSumarioProdutividade;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioSumarioDt relatorioSumarioDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		List tempList = null;
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioSumarioProdutividade");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório Sumário de Produtividade");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioSumarioDt = (RelatorioSumarioDt) request.getSession().getAttribute("RelatorioSumariodt");
		if (relatorioSumarioDt == null)
			relatorioSumarioDt = new RelatorioSumarioDt();

		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			relatorioSumarioDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			relatorioSumarioDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			relatorioSumarioDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			relatorioSumarioDt.setAnoFinal(request.getParameter("AnoFinal"));
		}
		
		if (request.getParameter("DataInicial") != null && !request.getParameter("DataInicial").equals("")) {
			relatorioSumarioDt.setDataInicial(request.getParameter("DataInicial"));
		}
		if (request.getParameter("DataFinal") != null && !request.getParameter("DataFinal").equals("")) {
			relatorioSumarioDt.setDataFinal(request.getParameter("DataFinal"));
		}

		if (request.getParameter("AgrupamentoRelatorio") != null) {
			if (request.getParameter("AgrupamentoRelatorio").equals("null")) {
				relatorioSumarioDt.setAgrupamentoRelatorio("");
			} else {
				relatorioSumarioDt.setAgrupamentoRelatorio(request.getParameter("AgrupamentoRelatorio"));
			}
		}
		
		if (request.getParameter("OpcaoRelatorio") != null) {
			if (request.getParameter("OpcaoRelatorio").equals("null")) {
				relatorioSumarioDt.setOpcaoRelatorio("");
			} else {
				relatorioSumarioDt.setOpcaoRelatorio(request.getParameter("OpcaoRelatorio"));
			}
		}

		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				relatorioSumarioDt.setId_Serventia("");
			} else {
				relatorioSumarioDt.setId_Serventia(request.getParameter("Id_Serventia"));
			}
		}

		if (request.getParameter("Sistema") != null) {
			if (request.getParameter("Sistema").equals("null")) {
				relatorioSumarioDt.setSistema("");
			} else {
				relatorioSumarioDt.setSistema(request.getParameter("Sistema"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioSumarioDt.setServentia("");
			} else {
				relatorioSumarioDt.setServentia(request.getParameter("Serventia"));
			}
		}

		if (request.getParameter("Id_Grupo") != null) {
			if (request.getParameter("Id_Grupo").equals("null")) {
				relatorioSumarioDt.setId_CargoTipo("");
			} else {
				relatorioSumarioDt.setId_CargoTipo(request.getParameter("Id_Grupo"));
			}
		}

		if (request.getParameter("Grupo") != null) {
			if (request.getParameter("Grupo").equals("null")) {
				relatorioSumarioDt.setCargoTipo("");
			} else {
				relatorioSumarioDt.setCargoTipo(request.getParameter("Grupo"));
			}
		}

		if (request.getParameter("Id_Usuario") != null) {
			if (request.getParameter("Id_Usuario").equals("null")) {
				relatorioSumarioDt.getUsuario().setId("");
			} else {
				relatorioSumarioDt.getUsuario().setId(request.getParameter("Id_Usuario"));
			}
		}

		if (request.getParameter("Usuario") != null) {
			if (request.getParameter("Usuario").equals("null")) {
				relatorioSumarioDt.getUsuario().setNome("");
			} else {
				relatorioSumarioDt.getUsuario().setNome(request.getParameter("Usuario"));
			}
		}

		if (request.getParameter("Comarca") != null) {
			if (request.getParameter("Comarca").equals("null")) {
				relatorioSumarioDt.setComarca("");
			} else {
				relatorioSumarioDt.setComarca(request.getParameter("Comarca"));
			}
		}

		if (request.getParameter("Id_Comarca") != null) {
			if (request.getParameter("Id_Comarca").equals("null")) {
				relatorioSumarioDt.setId_Comarca("");
			} else {
				relatorioSumarioDt.setId_Comarca(request.getParameter("Id_Comarca"));
			}
		}

		if (request.getParameter("ItemEstatistica") != null) {
			if (request.getParameter("ItemEstatistica").equals("null")) {
				relatorioSumarioDt.setItemEstatistica("");
			} else {
				relatorioSumarioDt.setItemEstatistica(request.getParameter("ItemEstatistica"));
			}
		}

		if (request.getParameter("Id_ItemEstatistica") != null) {
			if (request.getParameter("Id_ItemEstatistica").equals("null")) {
				relatorioSumarioDt.setIdItemEstatistica("");
			} else {
				relatorioSumarioDt.setIdItemEstatistica(request.getParameter("Id_ItemEstatistica"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Imprimir:

			mensagemRetorno = "";

			//variável criada para controlar o acesso ao método de produtividade de assessores
			boolean assessores = false;
			if (request.getSession().getAttribute("assessores") != null) {
				assessores = new Boolean(request.getSession().getAttribute("assessores").toString());
			}

			Calendar dataInicial = Calendar.getInstance();
			Calendar dataFinal = Calendar.getInstance();
			if(!relatorioSumarioDt.getDataInicial().isEmpty() && !relatorioSumarioDt.getDataFinal().isEmpty()) {
				dataInicial.setTime(Funcoes.StringToDate(relatorioSumarioDt.getDataInicial()));
				dataFinal.setTime(Funcoes.StringToDate(relatorioSumarioDt.getDataFinal()));
			} else {
				dataInicial.set(Calendar.MONTH, Funcoes.StringToInt(relatorioSumarioDt.getMesInicial()));
				dataInicial.set(Calendar.YEAR, Funcoes.StringToInt(relatorioSumarioDt.getAnoInicial()));
				dataFinal.set(Calendar.MONTH, Funcoes.StringToInt(relatorioSumarioDt.getMesFinal()));
				dataFinal.set(Calendar.YEAR, Funcoes.StringToInt(relatorioSumarioDt.getAnoFinal()));
			}

			long[] diferencaDias = Funcoes.diferencaDatas(dataFinal.getTime(), dataInicial.getTime());
			if (diferencaDias[0] < 0) {
				mensagemRetorno = "Data/Mês/Ano Inicial não pode ser menor que Data/Mês/Ano Final. ";
			} else if (diferencaDias[0] >= 90) { //Assessor só utiliza a data inicial..
				mensagemRetorno = "Diferença entre Data/Mês/Ano Inicial e Data/Mês/Ano Final não pode superar 90(noventa) dias. ";
			}

			boolean acessoEspecial = false;
			if (request.getSession().getAttribute("acessoEspecial") != null) {
				acessoEspecial = new Boolean(request.getSession().getAttribute("acessoEspecial").toString());
			}
			
			// Validações para o usuário com acesso especial
			if (acessoEspecial) {
				// Quando o usuário com acesso especial informar o ID do servidor/usuário,
				// deverá ser informada também a Serventia.
				if (relatorioSumarioDt.getUsuario().getId() != null && !relatorioSumarioDt.getUsuario().getId().equalsIgnoreCase("")) {
					if (relatorioSumarioDt.getId_Serventia() == null || relatorioSumarioDt.getId_Serventia().equalsIgnoreCase("")) {
						mensagemRetorno = "Ao informar o Servidor Judiciário, Serventia deve ser informada.";
					}
				}
			}

			if (mensagemRetorno.equals("")) {
				String tipoArquivo = request.getParameter("tipo_Arquivo");

				// Se o usuário for tiver acesso especial, ele poderá consultar outras comarcas e serventias
				// se for usuário comum poderá consultar apenas sua própria comarca e serventia
				if (acessoEspecial) {
					byTemp = relatorioEstatisticaNe.relSumarioProdutividade( relatorioSumarioDt.getAnoInicial(), relatorioSumarioDt.getMesInicial(), relatorioSumarioDt.getAnoFinal(), relatorioSumarioDt.getMesFinal(), relatorioSumarioDt.getDataInicial(), relatorioSumarioDt.getDataFinal(), relatorioSumarioDt.getId_Comarca(), relatorioSumarioDt.getId_Serventia(), relatorioSumarioDt.getId_CargoTipo(), relatorioSumarioDt.getUsuario().getId(), relatorioSumarioDt.getIdItemEstatistica(), relatorioSumarioDt.getAgrupamentoRelatorio(), relatorioSumarioDt.getOpcaoRelatorio(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
				} else {
					if(!assessores){
						//relatório de produtividade de demais servidores
						byTemp = relatorioEstatisticaNe.relSumarioProdutividade( relatorioSumarioDt.getAnoInicial(), relatorioSumarioDt.getMesInicial(), relatorioSumarioDt.getAnoFinal(), relatorioSumarioDt.getMesFinal(), relatorioSumarioDt.getDataInicial(), relatorioSumarioDt.getDataFinal(), UsuarioSessao.getUsuarioDt().getId_Comarca(), UsuarioSessao.getUsuarioDt().getId_Serventia(), relatorioSumarioDt.getId_CargoTipo(), relatorioSumarioDt.getUsuario().getId(), relatorioSumarioDt.getIdItemEstatistica(), relatorioSumarioDt.getAgrupamentoRelatorio(), relatorioSumarioDt.getOpcaoRelatorio(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
					} else {
						//relatório de produtividade de assessores
						byTemp = relatorioEstatisticaNe.relSumarioProdutividadeAssessores(relatorioSumarioDt.getDataInicial(), relatorioSumarioDt.getDataFinal(), UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentia(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt(), UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt(), UsuarioSessao.getId_UsuarioServentia(), UsuarioSessao.getUsuarioDt().getNome(), Funcoes.StringToInt(relatorioSumarioDt.getOpcaoRelatorio()));
					}
				}

				//se byTemp for null deve retornar msg de erro de relatório vazio para o usuário
				if(byTemp != null) {
					// Se o parâmertro tipo_Arquivo for setado e igual a 2, significa que o relatório deve ser um
					// arquivo TXT. Algumas telas não tem esse parâmetro setado no request, logo é gerado um PDF.
					if (tipoArquivo != null && tipoArquivo.equals("2")) {						  
						enviarTXT(response, byTemp, "RelatorioSumarioProdutividade");
					} else {						
						enviarPDF(response, byTemp, "RelatorioSumarioProdutividade");
					}																			
					
				} else {
					//retornando msg de erro de relatório vazio.
					if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
						stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
					}
					request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					break;
				}
				return;

			} else {
				if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				request.setAttribute("MensagemErro", mensagemRetorno);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;
		case Configuracao.Novo:
			relatorioSumarioDt.limparCamposConsulta();
			relatorioSumarioDt = this.atribuirDataAtual(relatorioSumarioDt);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
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
				request.setAttribute("tempRetorno", "RelatorioSumarioProdutividade");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarServentiasComarcaJSON(stNomeBusca1, relatorioSumarioDt.getId_Comarca(),posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		case (EstatisticaProdutividadeItemDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Item de Produtividade"};
				String[] lisDescricao = {"Item de Produtividade"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_ItemEstatistica");
				request.setAttribute("tempBuscaDescricao", "ItemEstatistica");
				request.setAttribute("tempBuscaPrograma", "Estatística de Produtividade");
				request.setAttribute("tempRetorno", "RelatorioSumarioProdutividade");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (EstatisticaProdutividadeItemDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarDescricaoEstatisticaProdutividadeItemJSON(stNomeBusca1, posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Nome", "Usuário"};
				String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Usuario");
				request.setAttribute("tempBuscaDescricao", "Usuario");
				request.setAttribute("tempBuscaPrograma", "Usuario");
				request.setAttribute("tempRetorno", "RelatorioSumarioProdutividade");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				
				if(request.getSession().getAttribute("acessoEspecial") != null && new Boolean(request.getSession().getAttribute("acessoEspecial").toString())){					
						stTemp = relatorioEstatisticaNe.consultarDescricaoServidorJudiciarioServentiaJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual, relatorioSumarioDt.getId_Serventia());
				} else {
					relatorioSumarioDt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());					
					stTemp = relatorioEstatisticaNe.consultarDescricaoServidorJudiciarioServentiaJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
				}				
								
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
				request.setAttribute("tempRetorno","RelatorioSumarioProdutividade");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
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
		case Configuracao.Curinga6:
			// Curinga de acesso comum
			//sempre que acessar essa tela é preciso remover o atributo do relatório de assessores
			request.getSession().removeAttribute("assessores");
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioSumarioDt.limpar();

			if (relatorioSumarioDt.getMesInicial().equals("") && relatorioSumarioDt.getAnoInicial().equals("") && relatorioSumarioDt.getMesFinal().equals("") && relatorioSumarioDt.getAnoFinal().equals("")) {
				relatorioSumarioDt = this.atribuirDataAtual(relatorioSumarioDt);
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioSumarioProdutividade.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", false);
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			break;

		case Configuracao.Curinga7:
			// Curinga para acesso especial
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioSumarioDt.limpar();

			if (relatorioSumarioDt.getMesInicial().equals("") && relatorioSumarioDt.getAnoInicial().equals("") && relatorioSumarioDt.getMesFinal().equals("") && relatorioSumarioDt.getAnoFinal().equals("")) {
				relatorioSumarioDt = this.atribuirDataAtual(relatorioSumarioDt);
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioSumarioProdutividadeCorregedoria.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", true);
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			break;
			
		case Configuracao.Curinga8:
			// Curinga para acesso ao relatório de assessores
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempPrograma", "Relatório Sumário de Produtividade de Assessores/Assistentes");
			relatorioSumarioDt.limpar();
			
			if (relatorioSumarioDt.getMesInicial().equals("") && relatorioSumarioDt.getAnoInicial().equals("")) {
				relatorioSumarioDt = this.atribuirDataAtual(relatorioSumarioDt);
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioSumarioProdutividadeAssessores.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("assessores", true);
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			break;

		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			if (relatorioSumarioDt.getMesInicial().equals("") && relatorioSumarioDt.getAnoInicial().equals("") && relatorioSumarioDt.getMesFinal().equals("") && relatorioSumarioDt.getAnoFinal().equals("")) {
				relatorioSumarioDt = this.atribuirDataAtual(relatorioSumarioDt);
			}

			if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}

			break;
		}
				
		if(request.getSession().getAttribute("acessoEspecial")!= null && (Boolean)request.getSession().getAttribute("acessoEspecial")){
			if (relatorioSumarioDt.getAgrupamentoRelatorio().equals("")) {
				relatorioSumarioDt.setAgrupamentoRelatorio(String.valueOf(RelatorioSumarioDt.COMARCA));
			}
		} else {
			if(relatorioSumarioDt.getAgrupamentoRelatorio().equals("")) {
				relatorioSumarioDt.setAgrupamentoRelatorio(String.valueOf(RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO));
			}
		}

		request.getSession().setAttribute("RelatorioSumariodt", relatorioSumarioDt);
		request.getSession().setAttribute("RelatorioSumarione", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * 
	 * @param relatorioSumarioDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioSumarioDt atribuirDataAtual(RelatorioSumarioDt relatorioSumarioDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioSumarioDt.setMesInicial(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioSumarioDt.setAnoInicial(String.valueOf(dataAtual.get(Calendar.YEAR)));
		relatorioSumarioDt.setMesFinal(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioSumarioDt.setAnoFinal(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioSumarioDt;
	}
}
