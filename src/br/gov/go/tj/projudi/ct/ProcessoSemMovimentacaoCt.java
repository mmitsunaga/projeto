package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoSemMovimentacaoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoSemMovimentacaoCt extends Controle{
	
	private static final long serialVersionUID = 2838483345791588863L;
	
	public int Permissao() {
		return ProcessoSemMovimentacaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {
	
		ProcessoSemMovimentacaoDt processoSemMovimentacaoDt;
		ProcessoNe processoNe;

		List tempList = null;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stAcao = "";

		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempRetorno", "ProcessoSemMovimentacao");
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		processoNe = (ProcessoNe) request.getSession().getAttribute("processoNe");
		if (processoNe == null)
			processoNe = new ProcessoNe();

		processoSemMovimentacaoDt = (ProcessoSemMovimentacaoDt) request.getSession().getAttribute("processoSemMovimentacaoDt");
		if (processoSemMovimentacaoDt == null)
			processoSemMovimentacaoDt = new ProcessoSemMovimentacaoDt();

		if (request.getParameter("fluxoSemMovimentacao") != null && !request.getParameter("fluxoSemMovimentacao").equals("")) {
			request.getSession().setAttribute("fluxoSemMovimentacao", request.getParameter("fluxoSemMovimentacao"));
			request.removeAttribute("fluxoSemMovimentacao");
		}
		if (request.getParameter("Id_Serventia") != null) {
			processoSemMovimentacaoDt.setIdServentia(request.getParameter("Id_Serventia"));
			processoSemMovimentacaoDt.setServentia(request.getParameter("Serventia"));
		}
		if (request.getParameter("Periodo") != null) {
			if (request.getParameter("Periodo").equals("null")) {
				processoSemMovimentacaoDt.setPeriodo("");
			} else {
				processoSemMovimentacaoDt.setPeriodo(request.getParameter("Periodo"));
			}
		}
		processoSemMovimentacaoDt.setIdServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		processoSemMovimentacaoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Processos Sem Movimentação");
		request.setAttribute("tituloPagina", "Processos Sem Movimentação");
		
		switch (paginaatual) {
		case Configuracao.Imprimir:
			break;
		case Configuracao.LocalizarDWR:
			break;
		case Configuracao.Localizar: 
			String stTemp = "";
			
			if(request.getSession().getAttribute("fluxoSemMovimentacao") != null && request.getSession().getAttribute("fluxoSemMovimentacao").toString().equals("1")) {
				stTemp = processoNe.consultarProcessosSemMovimentacaoServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia(), stNomeBusca1, stNomeBusca2, posicaoPaginaAtual);
				processoSemMovimentacaoDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
				processoSemMovimentacaoDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
			} else {
				stTemp = processoNe.consultarProcessosSemMovimentacaoServentiaJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaoPaginaAtual);
			}
										
			if(request.getSession().getAttribute("fluxoSemMovimentacao") != null && request.getSession().getAttribute("fluxoSemMovimentacao").toString().equals("1")) {
				montarOpcoesLote(request, UsuarioSessao);
			}
														
			enviarJSON(response, stTemp);
							
			return;

		case Configuracao.Novo:
			processoSemMovimentacaoDt.limpar();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		case Configuracao.Curinga6:
			redireciona(response, "BuscaProcesso?Id_Processo=" + request.getParameter("Id_Processo").toString());
			break;
			
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia","Estado"};	
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "ProcessoSemMovimentacao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",  String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);							
			}else{
				stTemp = "";
				stTemp = processoNe.consultarDescricaoServentiaJSON(stNomeBusca1, posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;					
			}
			break;
			
		// Consultar Juiz Responsável - Serventia Cargo
		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			int fluxo = Funcoes.StringToInt(request.getSession().getAttribute("fluxoSemMovimentacao").toString());


		if(fluxo == 1){
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"ServentiaCargo"};
				String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
				String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
				request.setAttribute("camposHidden",camposHidden);
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
				request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
				request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
				request.setAttribute("tempRetorno", "ProcessoSemMovimentacao");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				stTemp = "";
				stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, posicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
					
					enviarJSON(response, stTemp);
					
				
				return;
			}
			
		}else if(fluxo == 2){
			String ServentiaTipoCodigo = UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo();
			String ServentiaSubtipoCodigo  = UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo();
			
			if (processoSemMovimentacaoDt.getIdServentia().equalsIgnoreCase("") || processoSemMovimentacaoDt.getIdServentia().equalsIgnoreCase("null") || processoSemMovimentacaoDt.getIdServentia().length()==0) {
				request.setAttribute("MensagemErro", "Selecione a Serventia");
				break;
		    }

		    	ServentiaDt serventiaSeleciona = processoNe.consultarIdServentia(processoSemMovimentacaoDt.getIdServentia());
				if (serventiaSeleciona != null){
					 ServentiaTipoCodigo = serventiaSeleciona.getServentiaTipoCodigo();
					 ServentiaSubtipoCodigo  = serventiaSeleciona.getServentiaSubtipoCodigo();
				}
				request.setAttribute("CuringaServentiaCargo", "S");
			
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "ProcessoSemMovimentacao");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					stTemp = "";
					stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, posicaoPaginaAtual, processoSemMovimentacaoDt.getIdServentia(), ServentiaTipoCodigo, ServentiaSubtipoCodigo);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			}
			break;
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}
		if (stAcao.equals("")) {
			switch (Funcoes.StringToInt((String)request.getSession().getAttribute("fluxoSemMovimentacao"))) {
			case 1:
				int fluxo = Funcoes.StringToInt(request.getSession().getAttribute("fluxoSemMovimentacao").toString());
				if (request.getParameter("Passo")==null){
					String[][] lislocalizarBusca = {
							{ "Juiz Responsável", String.valueOf(ServentiaCargoDt.CodigoPermissao), "ServentiaCargo", processoSemMovimentacaoDt.getServentiaCargo()+Funcoes.retorneVazio(request.getParameter("ServentiaCargoUsuario")), processoSemMovimentacaoDt.getIdServentiaCargo()}							
							};
				request.setAttribute("lislocalizarBusca", lislocalizarBusca);
				String[][] lislocalizarDropdown = new String [][] {
					{ "Período Sem Movimentação", "Até 20 dias", "19", "Mais de 20 dias", "20", "Mais de 30 dias", "30", "Mais de 40 dias", "40", "Mais de 50 dias", "50", "Mais de 60 dias", "60", "Mais de 70 dias", "70", "Mais de 80 dias", "80", "Mais de 90 dias", "90", "Mais de 100 dias", "100", "Mais de 110 dias", "110", "Mais de 120 dias", "120", "Mais de 130 dias", "130", "Mais de 140 dias", "140", "Mais de 150 dias", "150", "Mais de 180 dias", "180", "Mais de 240 dias", "240", "Mais de 360 dias", "360"}
					};
		        request.setAttribute("lislocalizarDropdown", lislocalizarDropdown);
				String[] lisDescricao = {"Nº Processo", "Última Movimentação", "Descrição da Movimentação"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/LocalizarMultiplo.jsp";
				request.setAttribute("tempBuscaId", "Id_Processo");
				request.setAttribute("tempBuscaDescricao", "Processo");
				request.setAttribute("tempBuscaPrograma", "Processos Sem Movimentação");
				request.setAttribute("tempRetorno", "ProcessoSemMovimentacao");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisDescricao", lisDescricao);
			}
				break;
				
			case 2:
				if (request.getParameter("Passo")==null){
				String[][] lislocalizarBusca = new String [0][0];
				lislocalizarBusca = new String [][] {
					{ "Serventia", String.valueOf(ServentiaDt.CodigoPermissao), "Serventia", processoSemMovimentacaoDt.getServentia(), processoSemMovimentacaoDt.getIdServentia()}							
					,{ "Juiz Responsável", String.valueOf(ServentiaCargoDt.CodigoPermissao), "ServentiaCargo", processoSemMovimentacaoDt.getServentiaCargo()+Funcoes.retorneVazio(request.getParameter("ServentiaCargoUsuario")), processoSemMovimentacaoDt.getIdServentiaCargo()}
		            };						
				request.setAttribute("lislocalizarBusca", lislocalizarBusca);
				String[][] lislocalizarDropdown = new String [][] {
					{ "Período Sem Movimentação", "Até 20 dias", "1", "Mais de 20 dias", "20", "Mais de 30 dias", "30", "Mais de 40 dias", "40", "Mais de 50 dias", "50", "Mais de 60 dias", "60", "Mais de 70 dias", "70", "Mais de 80 dias", "80", "Mais de 90 dias", "90", "Mais de 100 dias", "100", "Mais de 110 dias", "110", "Mais de 120 dias", "120", "Mais de 130 dias", "130", "Mais de 140 dias", "140", "Mais de 150 dias", "150", "Mais de 180 dias", "180", "Mais de 240 dias", "240", "Mais de 360 dias", "360"}
					};
		        request.setAttribute("lislocalizarDropdown", lislocalizarDropdown);
				String[] lisDescricao = {"Nº Processo", "Última Movimentação", "Descrição da Movimentação"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/LocalizarMultiplo.jsp";
				request.setAttribute("tempBuscaId", "Id_Processo");
				request.setAttribute("tempBuscaDescricao", "Processo");
				request.setAttribute("tempBuscaPrograma", "Processos Sem Movimentação");
				request.setAttribute("tempRetorno", "ProcessoSemMovimentacao");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisDescricao", lisDescricao);
			}
				break;
				
			default:
				break;
			}
		}

		request.getSession().setAttribute("processoSemMovimentacaoDt", processoSemMovimentacaoDt);
		request.getSession().setAttribute("processoNe", processoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Verifica quais opções de movimentação em lote o usuário poderá visualizar
	 * @param request
	 * @param usuarioSessao
	 */
	protected void montarOpcoesLote(HttpServletRequest request, UsuarioNe usuarioSessao) {
		request.setAttribute("podeMovimentar", usuarioSessao.getVerificaPermissao(MovimentacaoDt.CodigoPermissao));		
	}
}