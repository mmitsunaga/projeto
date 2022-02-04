package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.GraficoProcessoDt;
import br.gov.go.tj.projudi.ne.EstatisticaProdutividadeItemNe;
import br.gov.go.tj.projudi.ne.GraficoProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GraficoProcessoCt extends Controle {

    private static final long serialVersionUID = 6522534986325767018L;

    public int Permissao() {
			return 533;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		GraficoProcessoNe graficoProcessoNe;
		GraficoProcessoDt graficoProcessoDt;

		String stNomeBusca1 = "";
		List tempList = null;
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";
		GregorianCalendar dataInicial = new GregorianCalendar();
		GregorianCalendar dataFinal = new GregorianCalendar();
		String posicaoLista = "";
		int paginaAnterior = 0;
		boolean removerItemProdutividade=false;

		request.setAttribute("tempRetorno", "GraficoProcesso");
		request.setAttribute("tempBuscaSistema", "Sistema");
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		if (request.getParameter("tipoGraficoProcesso") != null && !request.getParameter("tipoGraficoProcesso").equals("")) {
			request.getSession().setAttribute("tipoGraficoProcesso", request.getParameter("tipoGraficoProcesso"));
			request.removeAttribute("tipoGraficoProcesso");
		}

		graficoProcessoNe = (GraficoProcessoNe) request.getSession().getAttribute("GraficoProcessone");
		if (graficoProcessoNe == null) graficoProcessoNe = new GraficoProcessoNe();

		graficoProcessoDt = (GraficoProcessoDt) request.getSession().getAttribute("GraficoProcessodt");
		if (graficoProcessoDt == null) graficoProcessoDt = new GraficoProcessoDt();

		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			graficoProcessoDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			graficoProcessoDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			graficoProcessoDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			graficoProcessoDt.setAnoFinal(request.getParameter("AnoFinal"));
		}

		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				graficoProcessoDt.setId_Serventia("");
			} else {
				graficoProcessoDt.setId_Serventia(request.getParameter("Id_Serventia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				graficoProcessoDt.setServentia("");
			} else {
				graficoProcessoDt.setServentia(request.getParameter("Serventia"));
			}
		}

		if (request.getParameter("Id_Comarca") != null) {
			if (request.getParameter("Id_Comarca").equals("null")) {
				graficoProcessoDt.setId_Comarca("");
			} else {
				graficoProcessoDt.setId_Comarca(request.getParameter("Id_Comarca"));
			}
		}

		if (request.getParameter("Comarca") != null) {
			if (request.getParameter("Comarca").equals("null")) {
				graficoProcessoDt.setComarca("");
			} else {
				graficoProcessoDt.setComarca(request.getParameter("Comarca"));
			}
		}
		
		if (request.getParameter("Id_EstatisticaProdutividadeItem") != null) {
			if (request.getParameter("Id_EstatisticaProdutividadeItem").equals("null")) {
				graficoProcessoDt.setId_EstatisticaProdutividadeItem("");
			} else {
				graficoProcessoDt.setId_EstatisticaProdutividadeItem(request.getParameter("Id_EstatisticaProdutividadeItem"));
			}
		}

		if (request.getParameter("EstatisticaProdutividadeItem") != null) {
			if (request.getParameter("EstatisticaProdutividadeItem").equals("null")) {
				graficoProcessoDt.setEstatisticaProdutividadeItem("");
			} else {
				graficoProcessoDt.setEstatisticaProdutividadeItem(request.getParameter("EstatisticaProdutividadeItem"));
			}
		}
		
		if (request.getParameter("removerItemProdutividade") != null) {
			if (request.getParameter("removerItemProdutividade").equals("null"))
				removerItemProdutividade=false;
			else
				removerItemProdutividade=true;
		}
		
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Gráfico Relatório");
		posicaoLista = request.getParameter("posicaoLista");
		
		//Quando um item produtividade é selecionado já insere na lista
		if (!graficoProcessoDt.getId_EstatisticaProdutividadeItem().equals("") && paginaAnterior == (EstatisticaProdutividadeItemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar))
			this.adicionarItemProdutividade(graficoProcessoDt, request);
			
		if(removerItemProdutividade)
			removerItemProdutividade(graficoProcessoDt, posicaoLista);
		
		switch (paginaatual) {
			case Configuracao.Novo: //Processos por Item Produtividade - Novo gráfico
				graficoProcessoDt.limpar();
				graficoProcessoDt = this.atribuirDataAtual(graficoProcessoDt);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
			case Configuracao.Imprimir: //Processos por Item Produtividade - Imprimir gráfico
				mensagemRetorno = "";
				dataInicial.set(GregorianCalendar.MONTH, Funcoes.StringToInt(graficoProcessoDt.getMesInicial()));
				dataInicial.set(GregorianCalendar.YEAR, Funcoes.StringToInt(graficoProcessoDt.getAnoInicial()));
				dataFinal.set(GregorianCalendar.MONTH, Funcoes.StringToInt(graficoProcessoDt.getMesFinal()));
				dataFinal.set(GregorianCalendar.YEAR, Funcoes.StringToInt(graficoProcessoDt.getAnoFinal()));
				if(graficoProcessoDt.getComarca().equalsIgnoreCase("") && graficoProcessoDt.getServentia().equalsIgnoreCase("") && UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA)))
					mensagemRetorno = "Forneça a Comarca ou a Serventia para ser emitido o gráfico";
				if(graficoProcessoDt.getListaEstatisticaProdutividadeItem() == null || graficoProcessoDt.getListaEstatisticaProdutividadeItem().isEmpty())
					mensagemRetorno = "Forneça ao menos um item de produtividade";
				if (mensagemRetorno.equals("")) {
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))){
						byTemp = graficoProcessoNe.graficoProcessoItemProdutividade(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , graficoProcessoDt.getAnoInicial(), graficoProcessoDt.getMesInicial(), graficoProcessoDt.getAnoFinal(), graficoProcessoDt.getMesFinal(), graficoProcessoDt.getId_Comarca(), graficoProcessoDt.getId_Serventia(), graficoProcessoDt.getComarca(), graficoProcessoDt.getServentia(), UsuarioSessao.getUsuarioDt().getNome(), graficoProcessoDt);
					}else{
						byTemp = graficoProcessoNe.graficoProcessoItemProdutividade(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , graficoProcessoDt.getAnoInicial(), graficoProcessoDt.getMesInicial(), graficoProcessoDt.getAnoFinal(), graficoProcessoDt.getMesFinal(), UsuarioSessao.getUsuarioDt().getId_Comarca(), UsuarioSessao.getUsuarioDt().getId_Serventia(), "", UsuarioSessao.getUsuarioDt().getServentia(), UsuarioSessao.getUsuarioDt().getNome(), graficoProcessoDt);
					}
															
					enviarPDF(response,byTemp,"Relatorio");
										
					return;
				} else {
					request.setAttribute("MensagemErro", mensagemRetorno);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			
			case Configuracao.Curinga6: //Processos por Comarca - Novo gráfico
				graficoProcessoDt.limpar();
				graficoProcessoDt = this.atribuirDataAtual(graficoProcessoDt);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
			case Configuracao.Curinga7: //Processos por Comarca - Imprimir gráfico
				mensagemRetorno = "";
				dataInicial.set(GregorianCalendar.MONTH, Funcoes.StringToInt(graficoProcessoDt.getMesInicial()));
				dataInicial.set(GregorianCalendar.YEAR, Funcoes.StringToInt(graficoProcessoDt.getAnoInicial()));
				dataFinal.set(GregorianCalendar.MONTH, Funcoes.StringToInt(graficoProcessoDt.getMesFinal()));
				dataFinal.set(GregorianCalendar.YEAR, Funcoes.StringToInt(graficoProcessoDt.getAnoFinal()));
				if(graficoProcessoDt.getComarca().equalsIgnoreCase(""))
					mensagemRetorno = "Forneça a Comarca para ser emitido o gráfico";
				if (mensagemRetorno.equals("")) {
					byTemp = graficoProcessoNe.graficoProcessoComarca(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , graficoProcessoDt.getAnoInicial(), graficoProcessoDt.getMesInicial(), graficoProcessoDt.getAnoFinal(), graficoProcessoDt.getMesFinal(), graficoProcessoDt.getId_Comarca(), UsuarioSessao.getUsuarioDt().getNome(), request.getParameter("Comarca"));					
					
					enviarPDF(response,byTemp,"Relatorio");	
									
					return;
				} else {
					request.setAttribute("MensagemErro", mensagemRetorno);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Curinga8: //Processos por Serventia - Novo gráfico
				graficoProcessoDt.limpar();
				graficoProcessoDt = this.atribuirDataAtual(graficoProcessoDt);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
			case Configuracao.Curinga9: //Processos por Serventia - Imprimir gráfico
				mensagemRetorno = "";
				dataInicial.set(GregorianCalendar.MONTH, Funcoes.StringToInt(graficoProcessoDt.getMesInicial()));
				dataInicial.set(GregorianCalendar.YEAR, Funcoes.StringToInt(graficoProcessoDt.getAnoInicial()));
				dataFinal.set(GregorianCalendar.MONTH, Funcoes.StringToInt(graficoProcessoDt.getMesFinal()));
				dataFinal.set(GregorianCalendar.YEAR, Funcoes.StringToInt(graficoProcessoDt.getAnoFinal()));
				if(graficoProcessoDt.getServentia().equalsIgnoreCase("") && UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA)))
					mensagemRetorno = "Forneça a Serventia para ser emitido o gráfico";
				if (mensagemRetorno.equals("")) {
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))){
						byTemp = graficoProcessoNe.graficoProcessoServentia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , graficoProcessoDt.getAnoInicial(), graficoProcessoDt.getMesInicial(), graficoProcessoDt.getAnoFinal(), graficoProcessoDt.getMesFinal(), graficoProcessoDt.getId_Serventia(), UsuarioSessao.getUsuarioDt().getNome(), request.getParameter("Serventia"));
					} else {
						byTemp = graficoProcessoNe.graficoProcessoServentia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , graficoProcessoDt.getAnoInicial(), graficoProcessoDt.getMesInicial(), graficoProcessoDt.getAnoFinal(), graficoProcessoDt.getMesFinal(), UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getNome(), UsuarioSessao.getUsuarioDt().getServentia());
					}
										
					enviarPDF(response,byTemp,"Relatorio");										
					
					return;
				} else {
					request.setAttribute("MensagemErro", mensagemRetorno);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): //Localizar Serventia
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "GraficoProcesso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = graficoProcessoNe.consultarDescricaoServentiaJSON(stNomeBusca1, posicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
											
					return;
				}
				break;
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): //Localizar Comarca
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","GraficoProcesso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = graficoProcessoNe.consultarDescricaoComarcaJSON(stNomeBusca1, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
			break;
			case (EstatisticaProdutividadeItemDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Item de Produtividade"};
					String[] lisDescricao = {"Item de Produtividade"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EstatisticaProdutividadeItem");
					request.setAttribute("tempBuscaDescricao", "EstatisticaProdutividadeItem");
					request.setAttribute("tempBuscaPrograma", "Estatística de Produtividade"); //descrição da tela
					request.setAttribute("tempRetorno", "GraficoProcesso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstatisticaProdutividadeItemDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					EstatisticaProdutividadeItemNe estatisticaProdutividadeItemNe = new EstatisticaProdutividadeItemNe();
					stTemp = estatisticaProdutividadeItemNe.consultarDescricaoJSON(stNomeBusca1, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				if (graficoProcessoDt.getMesInicial().equals("") && graficoProcessoDt.getAnoInicial().equals("") && graficoProcessoDt.getMesFinal().equals("") && graficoProcessoDt.getAnoFinal().equals("")) {
					graficoProcessoDt = this.atribuirDataAtual(graficoProcessoDt);
				}
				break;
		}
		if (stAcao.equals("")) {
			switch (Funcoes.StringToInt(request.getSession().getAttribute("tipoGraficoProcesso").toString())) {
				case 1: // Gráfico de Processos por Comarca
					stAcao = "WEB-INF/jsptjgo/GraficoProcessoComarca.jsp";
					request.setAttribute("tempPrograma", "Gráfico de Processos por Comarca");
					break;
				case 2: // Gráfico de Processos por Serventia
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))){
						stAcao = "WEB-INF/jsptjgo/GraficoProcessoServentiaAvancado.jsp";
					} else {
						stAcao = "WEB-INF/jsptjgo/GraficoProcessoServentia.jsp";
					}
					request.setAttribute("tempPrograma", "Gráfico de Processos por Serventia");
					break;
				case 3: // Gráfico de Processos por Item Produtividade
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))){
						stAcao = "WEB-INF/jsptjgo/GraficoProcessoGeralAvancado.jsp";
					} else {
						stAcao = "WEB-INF/jsptjgo/GraficoProcessoGeral.jsp";
						
						
					}
					request.setAttribute("tempPrograma", "Gráfico de Processos por Item Produtividade");
					break;
	
				default:
					break;
			}
		}

		request.getSession().setAttribute("GraficoProcessodt", graficoProcessoDt);
		request.getSession().setAttribute("GraficoProcessone", graficoProcessoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório.
	 * 
	 * @param relatorioSumarioDt
	 * @return DT com data atualizada
	 * @author asrocha
	 */
	protected GraficoProcessoDt atribuirDataAtual(GraficoProcessoDt graficoProcessoDt) {
		GregorianCalendar dataAtual = new GregorianCalendar();
		graficoProcessoDt.setMesInicial(String.valueOf(dataAtual.get(GregorianCalendar.MONTH) + 1));
		graficoProcessoDt.setAnoInicial(String.valueOf(dataAtual.get(GregorianCalendar.YEAR)));
		graficoProcessoDt.setMesFinal(String.valueOf(dataAtual.get(GregorianCalendar.MONTH) + 1));
		graficoProcessoDt.setAnoFinal(String.valueOf(dataAtual.get(GregorianCalendar.YEAR)));

		return graficoProcessoDt;
	}
	
	/**
	 * Método responsável em adicionar itens de produtividade
	 */
	protected void adicionarItemProdutividade(GraficoProcessoDt graficoProcessoDt, HttpServletRequest request) {
		if (graficoProcessoDt.getId_EstatisticaProdutividadeItem().length() > 0) {
			EstatisticaProdutividadeItemDt estatisticaProdutividadeItemDt = new EstatisticaProdutividadeItemDt();
			estatisticaProdutividadeItemDt.setId(graficoProcessoDt.getId_EstatisticaProdutividadeItem());
			estatisticaProdutividadeItemDt.setEstatisticaProdutividadeItem(graficoProcessoDt.getEstatisticaProdutividadeItem());
			graficoProcessoDt.addListaEstatisticaProdutividadeItem(estatisticaProdutividadeItemDt);
			graficoProcessoDt.setId_EstatisticaProdutividadeItem("null");
			graficoProcessoDt.setEstatisticaProdutividadeItem("null");
		} else request.setAttribute("MensagemErro", "Selecione um Item de produtividade para ser adicionado.");
	}
	
	/**
	 * Método responsável em remover um item de produtividade
	 */
	private void removerItemProdutividade(GraficoProcessoDt graficoProcessoDt, String posicaoLista) {
		if (posicaoLista != null && posicaoLista.length() > 0) {
			graficoProcessoDt.getListaEstatisticaProdutividadeItem().remove(Funcoes.StringToInt(posicaoLista));
		}
	}
}