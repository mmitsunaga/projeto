package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioSumarioPendenciaCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioSumarioDt.CodigoPermissaoSumarioPendencia;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioSumarioDt relatorioSumarioDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		//List lisNomeBusca = null;
		//List lisDescricao = null;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		List tempList = null;
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");

		request.setAttribute("tempRetorno", "RelatorioSumarioPendencia");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório Sumário de Pendência");

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

		if (request.getParameter("AgrupamentoRelatorio") != null) {
			if (request.getParameter("AgrupamentoRelatorio").equals("null")) {
				relatorioSumarioDt.setAgrupamentoRelatorio("");
			} else {
				relatorioSumarioDt.setAgrupamentoRelatorio(request.getParameter("AgrupamentoRelatorio"));
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

		if (request.getParameter("PendenciaTipo") != null) {
			if (request.getParameter("PendenciaTipo").equals("null")) {
				relatorioSumarioDt.setPendenciaTipo("");
			} else {
				relatorioSumarioDt.setPendenciaTipo(request.getParameter("PendenciaTipo"));
			}
		}

		if (request.getParameter("Id_PendenciaTipo") != null) {
			if (request.getParameter("Id_PendenciaTipo").equals("null")) {
				relatorioSumarioDt.setId_PendenciaTipo("");
			} else {
				relatorioSumarioDt.setId_PendenciaTipo(request.getParameter("Id_PendenciaTipo"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Imprimir:

			mensagemRetorno = "";

			Calendar dataInicial = Calendar.getInstance();
			Calendar dataFinal = Calendar.getInstance();
			dataInicial.set(Calendar.MONTH, Funcoes.StringToInt(relatorioSumarioDt.getMesInicial()));
			dataInicial.set(Calendar.YEAR, Funcoes.StringToInt(relatorioSumarioDt.getAnoInicial()));
			dataFinal.set(Calendar.MONTH, Funcoes.StringToInt(relatorioSumarioDt.getMesFinal()));
			dataFinal.set(Calendar.YEAR, Funcoes.StringToInt(relatorioSumarioDt.getAnoFinal()));
			long[] diferencaDias = Funcoes.diferencaDatas(dataFinal.getTime(), dataInicial.getTime());
			if (diferencaDias[0] < 0) {
				mensagemRetorno = "Mês/Ano Inicial não pode ser maior que Mês/Ano Final. ";
			} else if (diferencaDias[0] >= 89) {
				mensagemRetorno = "Período entre Mês/Ano Inicial e Mês/Ano Final não pode superar 03(três) meses. ";
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
					byTemp = relatorioEstatisticaNe.relSumarioPendencia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioSumarioDt.getAnoInicial(), relatorioSumarioDt.getMesInicial(), relatorioSumarioDt.getAnoFinal(), relatorioSumarioDt.getMesFinal(), relatorioSumarioDt.getId_Serventia(), relatorioSumarioDt.getUsuario().getId(), relatorioSumarioDt.getId_Comarca(), Funcoes.StringToInt(relatorioSumarioDt.getAgrupamentoRelatorio()), UsuarioSessao.getUsuarioDt().getNome(), "1", tipoArquivo, relatorioSumarioDt.getId_PendenciaTipo());
				} else {
					byTemp = relatorioEstatisticaNe.relSumarioPendencia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioSumarioDt.getAnoInicial(), relatorioSumarioDt.getMesInicial(), relatorioSumarioDt.getAnoFinal(), relatorioSumarioDt.getMesFinal(), UsuarioSessao.getUsuarioDt().getId_Serventia(), relatorioSumarioDt.getUsuario().getId(), UsuarioSessao.getUsuarioDt().getId_Comarca(), Funcoes.StringToInt(relatorioSumarioDt.getAgrupamentoRelatorio()), UsuarioSessao.getUsuarioDt().getNome(), "1", tipoArquivo, relatorioSumarioDt.getId_PendenciaTipo());
				}

				//se byTemp for null deve retornar msg de erro de relatório vazio para o usuário
				if(byTemp != null){
					// Se o parâmertro tipo_Arquivo for setado e igual a 2, significa que o relatório deve ser um
					// arquivo TXT. Algumas telas não tem esse parâmetro setado no request, logo é gerado um PDF.
					if (tipoArquivo != null && tipoArquivo.equals("2")) {						
						enviarTXT(response, byTemp,"RelatorioSumarioPendencia");
					} else {						
						enviarPDF(response, byTemp,"RelatorioSumarioPendencia");
					}																									
						//System.out.println("WARNING : Occoreu um erro de rede no envio do arquivo.");
					
				}else {
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
				request.setAttribute("tempRetorno", "RelatorioSumarioPendencia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
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
		case (PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"PendenciaTipo"};
				String[] lisDescricao = {"PendenciaTipo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_PendenciaTipo");
				request.setAttribute("tempBuscaDescricao", "PendenciaTipo");
				request.setAttribute("tempBuscaPrograma", "PendenciaTipo");
				request.setAttribute("tempRetorno", "RelatorioSumarioPendencia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(PendenciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarDescricaoPendenciaTipoJSON(stNomeBusca1, posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Nome", "Usuario" };
				String[] lisDescricao = {"Nome", "Usuario" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Usuario");
				request.setAttribute("tempBuscaDescricao", "Usuario");
				request.setAttribute("tempBuscaPrograma", "Usuario");
				request.setAttribute("tempRetorno", "RelatorioSumarioPendencia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarDescricaoServidorJudiciarioJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual);
									
				enviarJSON(response, stTemp);								
				
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
				request.setAttribute("tempRetorno","RelatorioSumarioPendencia");		
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
		case Configuracao.Curinga6:
			// Curinga de acesso comum
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioSumarioDt.limpar();

			if (relatorioSumarioDt.getMesInicial().equals("") && relatorioSumarioDt.getAnoInicial().equals("") && relatorioSumarioDt.getMesFinal().equals("") && relatorioSumarioDt.getAnoFinal().equals("")) {
				relatorioSumarioDt = this.atribuirDataAtual(relatorioSumarioDt);
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioSumarioPendencias.jsp";
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

			stAcao = "WEB-INF/jsptjgo/RelatorioSumarioPendenciasCorregedoria.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", true);
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
		
		if((Boolean)request.getSession().getAttribute("acessoEspecial")){
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