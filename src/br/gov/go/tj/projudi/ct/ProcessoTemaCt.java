package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.dt.TemaOrigemDt;
import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.projudi.dt.TemaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoTemaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

public class ProcessoTemaCt extends ProcessoTemaCtGen{

	private static final long serialVersionUID = -5970159035139586825L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoTemaDt ProcessoTemadt;
		ProcessoTemaNe ProcessoTemane;
		TemaDt temaDt;
		ServentiaDt serventiaDt;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String Mensagem = "";
		int paginaAnterior = 0;
		String posicaoLista = "";
		int passoBusca = 0;
		List tempList = null;
		
		String stAcao="/WEB-INF/jsptjgo/ProcessoTema.jsp";

		request.setAttribute("tempPrograma","ProcessoTema");
		request.setAttribute("tempRetorno", "ProcessoTema");
		
		ProcessoTemane =(ProcessoTemaNe)request.getSession().getAttribute("ProcessoTemane");
		if (ProcessoTemane == null )  ProcessoTemane = new ProcessoTemaNe();  

//		ProcessoTemadt =(ProcessoTemaDt)request.getSession().getAttribute("ProcessoTemadt");
		ProcessoTemadt = new ProcessoTemaDt();
		
		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if(processoDt == null) processoDt = new ProcessoDt();
		
		temaDt =(TemaDt)request.getSession().getAttribute("TemaDt");
		if (temaDt == null )  temaDt = new TemaDt();
		
		serventiaDt =(ServentiaDt)request.getSession().getAttribute("ServentiaDt");
		if (serventiaDt == null )  serventiaDt = new ServentiaDt();
		
		List listaTemasEditada = (List) request.getSession().getAttribute("ListaTemas"); //Lista dos temas após alteração
		if (listaTemasEditada == null) listaTemasEditada = new ArrayList();
		
		List listaTemasHistorico = (List) request.getSession().getAttribute("ListaTemasHistorico"); 
		if (listaTemasHistorico == null) listaTemasHistorico = new ArrayList();

		ProcessoTemadt.setId_Tema( request.getParameter("Id_Tema")); 
		ProcessoTemadt.setId_Proc( processoDt.getId_Processo()); 
		ProcessoTemadt.setProcNumero( processoDt.getProcessoNumero()); 
		ProcessoTemadt.setTemaCodigo( request.getParameter("Tema")); 
		ProcessoTemadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoTemadt.setIpComputadorLog(request.getRemoteAddr());
		
		temaDt.setId(request.getParameter("Id_Tema"));
		temaDt.setTitulo(request.getParameter("Tema"));
		temaDt.setId_TemaOrigem(request.getParameter("Id_TemaOrigem"));
		temaDt.setTemaOrigem(request.getParameter("TemaOrigem"));
		temaDt.setId_TemaSituacao(request.getParameter("Id_TemaSituacao"));
		temaDt.setTemaSituacao(request.getParameter("TemaSituacao"));
		temaDt.setId_TemaTipo(request.getParameter("Id_TemaTipo"));
		temaDt.setTemaTipo(request.getParameter("TemaTipo"));
		temaDt.setTemaCodigo(request.getParameter("TemaCodigo"));
		
		serventiaDt.setId(request.getParameter("Id_Serventia"));
		serventiaDt.setServentia(request.getParameter("Serventia"));
		
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		
		if (!ProcessoTemadt.getId_Tema().equals("") && paginaAnterior == (TemaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			listaTemasEditada.add(ProcessoTemadt);
		}
		
		 if (ValidacaoUtil.isNaoVazio(request.getParameter("posicaoLista"))){
			posicaoLista = request.getParameter("posicaoLista");
		} else if (ValidacaoUtil.isNaoVazio(request.getSession().getAttribute("posicaoLista"))){
			posicaoLista = (String) request.getSession().getAttribute("posicaoLista");
		}
		
		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		
		request.getSession().setAttribute("posicaoLista", posicaoLista);
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
				
		boolean isSuspenso = ProcessoTemane.isProcessoSuspenso(processoDt.getId_Processo());
		request.setAttribute("podeSuspender", !isSuspenso);
		request.setAttribute("mostrarMotivo", (paginaatual == Configuracao.Excluir && isSuspenso));
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				if (isSuspenso){
					
					// Obtém o motivo informado pelo usuário
					String motivoDigitado = request.getParameter("motivo");
					
					// Verificar se o motivo foi preenchido
					if (ValidacaoUtil.isNaoVazio(motivoDigitado)){
						
						// Se houver apenas um tema no processo, não permitir excluir
						if (listaTemasEditada.size() > 1){
							
							// Obtém o objeto que foi selecionado 
							ProcessoTemaDt processoTemaDt = (ProcessoTemaDt) listaTemasEditada.get(Funcoes.StringToInt(posicaoLista));
							processoTemaDt.setMotivo(motivoDigitado);
							processoTemaDt.setDataFinalSobrestado(Funcoes.DataHora(new Date()));
							processoTemaDt.setId_Usuario(UsuarioSessao.getId_Usuario());
							
							// Faz a alteração dos itens da página
							ProcessoTemane.salvar(processoTemaDt);
							
							// Atualiza a lista em session para fazer o refresh da tela
							listaTemasEditada = new ArrayList();
							listaTemasEditada.addAll(ProcessoTemane.consultarTemasProcessoPorIdProcesso(processoDt.getId_Processo(), UsuarioSessao));						
							request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
							
						} else {
							request.setAttribute("MensagemErro", "Não é possível excluir se há apenas 1 Tema vinculado ao processo.");
						}						
						
					} else {						
						// Mostra mensagem de alerta
						request.setAttribute("MensagemErro", "Informe o motivo da exclusão do Tema");
					}
				} else {					
					ProcessoTemane.excluir((ProcessoTemaDt) listaTemasEditada.get(Funcoes.StringToInt(posicaoLista)));
					request.getSession().removeAttribute("posicaoLista");
					listaTemasEditada.remove(Funcoes.StringToInt(posicaoLista));
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				}
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoTema"};
					String[] lisDescricao = {"ProcessoTema"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcTema");
					request.setAttribute("tempBuscaDescricao","ProcessoTema");
					request.setAttribute("tempBuscaPrograma","ProcessoTema");
					request.setAttribute("tempRetorno","ProcessoTema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoTemane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
			case Configuracao.Novo: 
				request.getSession().removeAttribute("ListaTemas");
				listaTemasEditada = new ArrayList();
				listaTemasEditada.addAll(ProcessoTemane.consultarTemasProcessoPorIdProcesso(processoDt.getId_Processo(), UsuarioSessao));				
				request.getSession().removeAttribute("ListaTemasHistorico");
				listaTemasHistorico = new ArrayList();
				listaTemasHistorico.addAll(ProcessoTemane.consultarTemasProcessoComDataSobrestadoFinalNaoNulo(processoDt.getId_Processo()));
				
				break;
			case Configuracao.Excluir:
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				ProcessoTemaDt processoTemaDt = (ProcessoTemaDt) listaTemasEditada.get(Funcoes.StringToInt(posicaoLista));			
				request.setAttribute("temaSelecionado", processoTemaDt.getTemaCodigo() + " - " + processoTemaDt.getTitulo());
				break;
			case Configuracao.SalvarResultado:
				Mensagem=ProcessoTemane.Verificar(listaTemasEditada);
				if (Mensagem.length()==0){
					if (!isSuspenso){
						ProcessoNe processoNe = new ProcessoNe();
						Mensagem = processoNe.podeSuspender(processoDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
						if (Mensagem.length() == 0){
							ProcessoTemane.salvarESuspender(processoDt.getId_Processo(), listaTemasEditada, UsuarioSessao);
							request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
						} else {
							request.setAttribute("MensagemErro", Mensagem);
							ProcessoTemane.salvar(listaTemasEditada);
						}
					} else {
						ProcessoTemane.salvar(listaTemasEditada);
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
					}					
				} else {
					request.setAttribute("MensagemErro", Mensagem);
				}
				listaTemasEditada = new ArrayList();
				listaTemasEditada.addAll(ProcessoTemane.consultarTemasProcessoPorIdProcesso(processoDt.getId_Processo(), UsuarioSessao));
			break;
			case (TemaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Pesquisa", "Origem", "Situação", "Número do Tema"};
					String[] lisDescricao = {"Tema", "Origem", "Situação"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Tema");
					request.setAttribute("tempBuscaDescricao","Tema");
					request.setAttribute("tempBuscaPrograma","Tema");
					request.setAttribute("tempRetorno","ProcessoTema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (TemaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if(paginaAnterior == Configuracao.Curinga6)
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					else
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				} else {
					String stTemp = ProcessoTemane.consultarTemasValidosPorDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
				
			case TemaOrigemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar: 
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema Origem"};
					String[] lisDescricao = {"Tema Origem"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaOrigem");
					request.setAttribute("tempBuscaDescricao","TemaOrigem");
					request.setAttribute("tempBuscaPrograma","TemaOrigem");
					request.setAttribute("tempRetorno","ProcessoTema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					request.setAttribute("PaginaAtual", (TemaOrigemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoTemane.consultarDescricaoTemaOrigemJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
			case TemaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema Tipo"};
					String[] lisDescricao = {"Tema Tipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaTipo");
					request.setAttribute("tempBuscaDescricao","TemaTipo");
					request.setAttribute("tempBuscaPrograma","TemaTipo");
					request.setAttribute("tempRetorno","ProcessoTema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					request.setAttribute("PaginaAtual", (TemaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoTemane.consultarDescricaoTemaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
				
			case TemaSituacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema Situação"};
					String[] lisDescricao = {"Tema Situação"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaSituacao");
					request.setAttribute("tempBuscaDescricao","TemaSituacao");
					request.setAttribute("tempBuscaPrograma","TemaSituacao");
					request.setAttribute("tempRetorno","ProcessoTema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					request.setAttribute("PaginaAtual", (TemaSituacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoTemane.consultarDescricaoTemaSituacaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
			
			// Consulta serventias disponíveis
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Serventia");			
					request.setAttribute("tempRetorno","ProcessoTema");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
				} else {					
					String stTemp = ProcessoTemane.consultarServentiaJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt());
					enviarJSON(response, stTemp);
					return;								
				}
				break;	
				
			//Consulta de Processos por Tema
			case Configuracao.Curinga6:
				
				if (passoBusca == 1) {
					if(validarCampos(temaDt))
						request.setAttribute("MensagemErro", "Informe pelo menos um filtro da pesquisa.");
					else {
						tempList = ProcessoTemane.consultarProcessosTema(temaDt.getId()
								, temaDt.getId_TemaOrigem()
								, temaDt.getId_TemaSituacao(), temaDt.getId_TemaTipo()
								, temaDt.getTemaCodigo()
								, tratarValorSeNulo(serventiaDt.getId()));
						if (tempList.size() > 0) {							
							request.setAttribute("ListaProcessos", tempList);
						} else request.setAttribute("MensagemErro", "Nenhum Processo foi localizado.");
					}					
				}
				
				// Somente se a serventia do filtro é igual do usuário logado e se tem registros de resposta da consulta
				boolean podeMovimentar = serventiaDt.getId().equalsIgnoreCase(UsuarioSessao.getId_Serventia()) && ValidacaoUtil.isNaoVazio(tempList);
				request.setAttribute("podeMovimentar", podeMovimentar && UsuarioSessao.getVerificaPermissao(MovimentacaoDt.CodigoPermissao));
								
				request.setAttribute("PaginaAtual",Configuracao.Curinga6);
				stAcao = "/WEB-INF/jsptjgo/ProcessoTemaLocalizar.jsp";
				break;
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
		}
		
		request.getSession().setAttribute("ListaTemas", listaTemasEditada);
		request.getSession().setAttribute("ListaTemasHistorico", listaTemasHistorico);
		request.getSession().setAttribute("TemaDt",temaDt);
		request.getSession().setAttribute("ServentiaDt", serventiaDt);
		request.getSession().setAttribute("ProcessoTemane",ProcessoTemane);
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected boolean validarCampos(TemaDt temaDt){
		return temaDt.getId().length()==0 && temaDt.getId_TemaOrigem().length()==0 && temaDt.getId_TemaSituacao().length()==0 && temaDt.getId_TemaTipo().length()==0 && temaDt.getTemaCodigo().length()==0;
	}
		
	protected String tratarValorSeNulo(String valor){
		return (valor == null || (valor != null && valor.equalsIgnoreCase("null"))) ? "" : valor;
	}
	
}
