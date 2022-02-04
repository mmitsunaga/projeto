package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.AudienciaPublicadaNe;
import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class MovimentacaoArquivoCt extends MovimentacaoArquivoCtGen {

    private static final long serialVersionUID = 231596767619299144L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca,	String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoArquivoDt MovimentacaoArquivodt;
		MovimentacaoArquivoNe MovimentacaoArquivone;

		String Mensagem = "";
		String stId = "";
		int passoEditar = -1;
		int proximaPaginaAtual = -1;

		request.setAttribute("tempPrograma", "MovimentacaoArquivo");

		MovimentacaoArquivone = (MovimentacaoArquivoNe) request.getSession().getAttribute("MovimentacaoArquivone");
		if (MovimentacaoArquivone == null) MovimentacaoArquivone = new MovimentacaoArquivoNe();

		MovimentacaoArquivodt = (MovimentacaoArquivoDt) request.getSession().getAttribute("MovimentacaoArquivodt");
		if (MovimentacaoArquivodt == null) MovimentacaoArquivodt = new MovimentacaoArquivoDt();

		MovimentacaoArquivodt.setNomeArquivo(request.getParameter("NomeArquivo"));
		MovimentacaoArquivodt.setId_Movimentacao(request.getParameter("Id_Movimentacao"));
		MovimentacaoArquivodt.setCodigoTemp(request.getParameter("CodigoTemp"));
		MovimentacaoArquivodt.setArquivoTipoCodigo(request.getParameter("ArquivoTipoCodigo"));
		MovimentacaoArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		ProcessoDt processoDt = null;
		MovimentacaoProcessoDt movimentacaoProcessoDt = null;
		
		// Variáveis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("ProximaPaginaAtual") != null) proximaPaginaAtual = Funcoes.StringToInt(request.getParameter("ProximaPaginaAtual"));

		switch (paginaatual) {

		// Validar Arquivos de Movimentações
		case Configuracao.Curinga6:
			stId = request.getParameter("Id_MovimentacaoArquivo");			
			
			if (stId != null && stId.length() > 0) {
				MovimentacaoArquivodt = MovimentacaoArquivone.consultarId(stId);
				MovimentacaoArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				MovimentacaoArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				if (MovimentacaoArquivone.VerificarAlteracaoVisibilidadeAquivo(MovimentacaoArquivodt, UsuarioSessao.getUsuarioDt())){
					MovimentacaoArquivone.validarArquivoMovimentacao(MovimentacaoArquivodt);
					Mensagem = "Arquivo " + MovimentacaoArquivodt.getNomeArquivo() + " foi Validado com Sucesso.";
					// Recupera o processo da sessão e redireciona para tela com mensagem de sucesso
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + Mensagem);
					return;
				} else {
					Mensagem = "Não foi possível alterar a visibilidade do Arquivo, esse arquivo possui nível de visibilidade Magistrado, somente o magistrado pode alterar a visibilidade do mesmo.";
				}
				
			} else 
				Mensagem = "Não foi possível alterar a visibilidade do Arquivo. Selecione novamente.";
			
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
			redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
			return;

			// Invalidar Arquivos de Movimentações
		case Configuracao.Curinga7:
			stId = request.getParameter("Id_MovimentacaoArquivo");
			String tipoBloqueio = request.getParameter("TipoBloqueio");
			
			if (tipoBloqueio != null && tipoBloqueio.length()>0){
			    int constanteAcesso=0;
			    constanteAcesso = MovimentacaoArquivodt.getTipoAcesso(tipoBloqueio);
			    
			    processoDt = null;
				if (stId != null && stId.length() > 0 && constanteAcesso != 0) {
					MovimentacaoArquivodt = MovimentacaoArquivone.consultarId(stId);
					
					if (MovimentacaoArquivone.VerificarAlteracaoVisibilidadeAquivo(MovimentacaoArquivodt, UsuarioSessao.getUsuarioDt())){
						MovimentacaoArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						MovimentacaoArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						MovimentacaoArquivone.invalidarArquivoMovimentacao(MovimentacaoArquivodt, constanteAcesso, null);
	
						Mensagem = "A visibilidade do Arquivo " + MovimentacaoArquivodt.getNomeArquivo() + " foi Alterada com Sucesso.";
						// Recupera o processo da sessão e redireciona para tela com mensagem de sucesso
						processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + Mensagem);
						return;
					} else {
						Mensagem = "Não foi possível alterar a visibilidade do Arquivo, esse arquivo possui nível de visibilidade Magistrado, somente o magistrado pode alterar a visibilidade do mesmo.";
						processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + Mensagem);
						return;
					}
					
				} else 
					Mensagem = "Não foi possível alterar a visibilidade do Arquivo. Selecione novamente.";
		    
			} else
				Mensagem = "Não foi possível alterar a visibilidade do Arquivo.";
			
			
			redireciona(response, "BuscaProcesso?MensagemErro=" + Mensagem);
			return;
		case Configuracao.Curinga8:
			 
			String stTemp = MovimentacaoArquivone.consultarArquivosMovimentacaoHashJSON(MovimentacaoArquivodt.getId_Movimentacao(),  UsuarioSessao);				 		
			enviarJSON(response, stTemp);
				 			
			return;
		case Configuracao.Curinga9:
			movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
			if (processoDt != null && processoDt.getId() != null &&  !processoDt.getId().equals("")) {
				processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				MovimentacaoArquivone.atualizeListaDeArquivoDoObjectStorageDigitalizacao(processoDt, 1, "", UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt);
				String mensagem = "Storage sincronizado com sucesso!";
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
				return; 
			}			
			break;
			
		case Configuracao.LocalizarDWR:
			movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
			List listaDeArquivos = getListaArquivos(request);
			if (processoDt != null && 
				processoDt.getId() != null && 
				!processoDt.getId().equals("")) {
				
				processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				String mensagem = "";	
				if (listaDeArquivos != null &&
				    listaDeArquivos.size() > 0) {
					if (passoEditar == 2) {
						// Retornar processo digital para híbrido, com movimentação no processo
						MovimentacaoArquivone.retornarProcessoHibridoParaDigital(processoDt, UsuarioSessao.getUsuarioDt(), listaDeArquivos, movimentacaoProcessoDt);
						mensagem = "Processo retornado para híbrido com sucesso!";
					} else if (passoEditar == 3) {
						// Adicionar novas peças em processo digital que já foi híbrido
						MovimentacaoArquivone.atualizeListaDeArquivoDoProcessoHibrido(processoDt, UsuarioSessao.getUsuarioDt(), listaDeArquivos, false);
						mensagem = "Peças adicionadas do processo que já foi híbrido com sucesso!";
					} else {
						// Converter processo de híbrido para digital
						MovimentacaoArquivone.atualizeListaDeArquivoDoProcessoHibrido(processoDt, UsuarioSessao.getUsuarioDt(), listaDeArquivos, true);
						mensagem = "Processo convertido com sucesso!";
					}
				} 
				
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
				return; 
			}			
			break;
		case Configuracao.Salvar:
			if (proximaPaginaAtual == Configuracao.LocalizarDWR &&
				passoEditar == 2) {
				
				String mensagem = "Clique para confirmar o Retorno do processo Digital para Híbrido.";
				
				request.setAttribute("Mensagem", mensagem);
				request.setAttribute("MensagemOk", mensagem);
				
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				
				request.setAttribute("PaginaRetorno", "MovimentacaoArquivo");
				request.setAttribute("PassoEditar", passoEditar);
				request.setAttribute("ProximaPaginaAtual", proximaPaginaAtual);
				
				RequestDispatcher dis = request.getRequestDispatcher(super.obtenhaAcaoJSP("ConfirmarOperacaoProcesso"));
				dis.include(request, response);
				return;
			}	
			super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
		case Configuracao.SalvarResultado:
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
			if (proximaPaginaAtual == Configuracao.LocalizarDWR &&
				passoEditar == 2 &&
				processoDt != null) {
				// Retornar processo digital para híbrido, sem movimentação no processo
				MovimentacaoArquivone.retornarProcessoHibridoParaDigital(processoDt, UsuarioSessao.getUsuarioDt(), null, null);
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Processo retornado para híbrido com sucesso!");				
			}
		break;
		default:
			super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
		}

	}
    
    protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		List lista = Funcoes.converterMapParaList(mapArquivos);

		return lista;
	}

	/**
	 * Resgata lista de pendências a serem inseridas Converte de Set para List
	 */
	protected List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);
		return lista;
	}

	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		// Limpa lista DWR e zera contador Pendências
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}
}
