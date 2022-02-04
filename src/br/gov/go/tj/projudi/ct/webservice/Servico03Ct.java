package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.ne.ArquivoTipoNe;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;

public class Servico03Ct extends Controle {

	private static final long serialVersionUID = 1396367972101425592L;

	public int Permissao() {
		return 423;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		String stOperacao = "";
		String stTag = "";
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		List lisValores = null;
		List tempList = new ArrayList();
		PendenciaNe pendenciaNe;
		PendenciaArquivoNe pendenciaArquivoNe;
		LogDt logDt;
		String stVersao = "1";
		String stAjuda = "1";
		
		response.setContentType("text/xml; charset=UTF-8");

		switch (paginaatual) {

			case 1: //LISTAR TIPOS DE ARQUIVO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 12 � LISTAR TIPOS DE ARQUIVO (VERS�O 1)\nLista os tipos de arquivo vinculados ao grupo do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=1\nPosicaoPaginaAtual=pagina��o\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdArquivoTipo\nDescricaoArquivoTipo");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String[] stDescricaoListaArquivoTipo = {"IdArquivoTipo", "DescricaoArquivoTipo"};
        				stTag = "ArquivoTipo";
        				ArquivoTipoNe ArquivoTipone = new ArquivoTipoNe();
        				tempList = ArquivoTipone.consultarGrupoArquivoTipo(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), "", posicaopaginaatual);
        				lisValores = new ArrayList();
        				if (tempList != null) {
        					for (int i = 0; i < tempList.size(); i++) {
        						ArquivoTipoDt temDados = (ArquivoTipoDt) tempList.get(i);
        						String[] stTemp = {temDados.getId(), Funcoes.substituirCaracteresEspeciaisXML(temDados.getArquivoTipo(), true) };
        						lisValores.add(stTemp);
        					}
        				}
        				request.setAttribute("Paginacao", qtdPaginas(ArquivoTipone.getQuantidadePaginas()));
        				request.setAttribute("AtributosTag", stDescricaoListaArquivoTipo);
        				request.setAttribute("Tag", stTag);
        				request.setAttribute("Operacao", "12");
        				request.setAttribute("Valores", lisValores);
        				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
					}
				}
				break;
				
			case 2: //LISTAR PEND�NCIAS ABERTAS DA SERVENTIA
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 16 � LISTAR PEND�NCIAS ABERTAS DA SERVENTIA (VERS�O 1)\nLista as pend�ncias abertas da serventia do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=2\nPosicaoPaginaAtual=pagina��o\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdPendencia\nHashPendencia\nNumeroProcesso\nMovimentacao\nTipoPendencia\nDataInicio\nStatus");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String[] stDescricaoListaPendenciasServentia = {"IdPendencia", "HashPendencia", "NumeroProcesso", "Movimentacao", "TipoPendencia", "DataInicio", "Status"};
        				stTag = "PendenciasServentia";
        				pendenciaNe = new PendenciaNe();
        				tempList = pendenciaNe.consultarAbertas(UsuarioSessao, null, null, false, null, null, "", null, null, posicaopaginaatual);
        
        				lisValores = new ArrayList();
        				if (tempList != null) {
        					for (int i = 0; i < tempList.size(); i++) {
        						PendenciaDt temDados = (PendenciaDt) tempList.get(i);
        						String[] stTemp = {temDados.getId(), temDados.getHash(), temDados.getProcessoNumero(), temDados.getMovimentacao(), temDados.getPendenciaTipo(), temDados.getDataInicio(), temDados.getPendenciaStatus() };
        						lisValores.add(stTemp);
        					}
        				}
        				request.setAttribute("Paginacao", qtdPaginas(pendenciaNe.getQuantidadePaginas()));
        				request.setAttribute("AtributosTag", stDescricaoListaPendenciasServentia);
        				request.setAttribute("Tag", stTag);
        				request.setAttribute("Operacao", "16");
        				request.setAttribute("Valores", lisValores);
        				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
					}
				}
				break;
				
			case 3: //LISTAR PEND�NCIAS ABERTAS DO CARGO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 17 � LISTAR PEND�NCIAS ABERTAS DO CARGO (VERS�O 1)\nLista as pend�ncias abertas para o cargo do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=3\nPosicaoPaginaAtual=pagina��o\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdPendencia\nHashPendencia\nNumeroProcesso\nMovimentacao\nTipoPendencia\nDataInicio\nStatus");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String[] stDescricaoListaPendenciasServentiaCargo = {"IdPendencia", "HashPendencia", "NumeroProcesso", "Movimentacao", "TipoPendencia", "DataInicio", "Status" };
        				stTag = "PendenciasServentiaCargo";
        				pendenciaNe = new PendenciaNe();
        				tempList = pendenciaNe.consultarAbertasServentiaCargo(UsuarioSessao, null, null, false, null, "", null, null, posicaopaginaatual);
        				lisValores = new ArrayList();
        				if (tempList != null) {
        					for (int i = 0; i < tempList.size(); i++) {
        						PendenciaDt temDados = (PendenciaDt) tempList.get(i);
        						String[] stTemp = {temDados.getId(), temDados.getHash(), temDados.getProcessoNumero(), temDados.getMovimentacao(), temDados.getPendenciaTipo(), temDados.getDataInicio(), temDados.getPendenciaStatus() };
        						lisValores.add(stTemp);
        					}
        				}
        				request.setAttribute("Paginacao", qtdPaginas(pendenciaNe.getQuantidadePaginas()));
        				request.setAttribute("AtributosTag", stDescricaoListaPendenciasServentiaCargo);
        				request.setAttribute("Tag", stTag);
        				request.setAttribute("Operacao", "17");
        				request.setAttribute("Valores", lisValores);
        				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
					}
				}
				break;
				
			case 4: //LISTAR INTIMA��ES
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 18 � LISTAR INTIMA��ES (VERS�O 1)\nLista intima��es geradas para o cargo ou para o usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=4\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdPendencia\nHashPendencia\nIdProcesso\nNumeroProcesso\nHashProcesso\nIdMovimentacao\nMovimentacao\nTipoPendencia\nDataInicio\nDataLimite\nStatus");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String[] stDescricaoListaPendenciasIntimacoes = {"IdPendencia", "HashPendencia", "IdProcesso", "NumeroProcesso", "HashProcesso", "IdMovimentacao", "Movimentacao", "TipoPendencia", "DataInicio", "DataLimite", "Status"};
        				stTag = "PendenciasIntimacoes";
        				pendenciaNe = new PendenciaNe();
        				tempList = pendenciaNe.consultarIntimacoesPromotorAdvogado(UsuarioSessao);
        
        				lisValores = new ArrayList();
        				if (tempList != null) {
        					for (int i = 0; i < tempList.size(); i++) {
        						PendenciaDt objPendencia = (PendenciaDt) tempList.get(i);
        						String[] stTemp = {objPendencia.getId(), objPendencia.getHash(), objPendencia.getId_Processo(), objPendencia.getProcessoNumero(), UsuarioSessao.getCodigoHash(objPendencia.getId_Processo()), objPendencia.getId_Movimentacao(), objPendencia.getMovimentacao(), objPendencia.getPendenciaTipo(), objPendencia.getDataInicio(), objPendencia.getDataLimite(), objPendencia.getPendenciaStatus() };
        						lisValores.add(stTemp);
        					}
        				}
        
        				request.setAttribute("AtributosTag", stDescricaoListaPendenciasIntimacoes);
        				request.setAttribute("Tag", stTag);
        				request.setAttribute("Operacao", "18");
        				request.setAttribute("Valores", lisValores);
        				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
					}
				}
				break;
				
			case 6: //LISTAR INTIMA��ES LIDAS
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 19 � LISTAR INTIMA��ES LIDAS (VERS�O 1)\nLista as intima��es lidas do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=6\nPosicaoPaginaAtual=pagina��o\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdPendencia\nHashPendencia\nNumeroProcesso\nMovimentacao\nTipoPendencia\nDataInicio\nDataLeitura\nDataLimite\nStatus");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String[] stDescricaoListaIntimacoesLidas = {"IdPendencia", "HashPendencia", "NumeroProcesso", "Movimentacao", "TipoPendencia", "DataInicio", "DataLeitura", "DataLimite", "Status" };
        				stTag = "PendenciasIntimacoesLidas";
        				pendenciaNe = new PendenciaNe();
        				tempList = pendenciaNe.consultarIntimacoesLidas(UsuarioSessao, "", null, null, null, null, posicaopaginaatual);
        				lisValores = new ArrayList();
        				if (tempList != null) {
        					for (int i = 0; i < tempList.size(); i++) {
        						PendenciaDt temDados = (PendenciaDt) tempList.get(i);
        						String[] stTemp = {temDados.getId(), temDados.getHash(), temDados.getProcessoNumero(), temDados.getMovimentacao(), temDados.getPendenciaTipo(), temDados.getDataInicio(), temDados.getDataFim(), temDados.getDataLimite(), temDados.getPendenciaStatus() };
        						lisValores.add(stTemp);
        					}
        				}
        				request.setAttribute("Paginacao", qtdPaginas(pendenciaNe.getQuantidadePaginas()));
        				request.setAttribute("AtributosTag", stDescricaoListaIntimacoesLidas);
        				request.setAttribute("Tag", stTag);
        				request.setAttribute("Operacao", "19");
        				request.setAttribute("Valores", lisValores);
        				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
					}
				}
				break;
				
			case 7: //LISTAR ARQUIVOS DE UMA PEND�NCIA
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 13 � LISTAR ARQUIVOS DE UMA PEND�NCIA (VERS�O 1)\nLista todos os arquivos vinculados a uma pend�ncia.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=7\na=IdPendencia\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdArquivo\nHashArquivo\nDescricao\nArquivoTipo\nAssinador\nRecibo\nResposta");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String[] stDescricaoListaArquivosPendencia = {"IdArquivo", "HashArquivo", "Descricao", "ArquivoTipo", "Assinador", "Recibo", "Resposta"};
        				stTag = "ArquivosPendencia";
        				String stIdPendencia;
        				if (request.getParameter("a") != null) stIdPendencia = request.getParameter("a");
        				else throw new WebServiceException("Id Pend�ncia n�o encontrado");
        				pendenciaNe = new PendenciaNe();
        				tempList = pendenciaNe.consultarArquivosAssinadosComHash(stIdPendencia, UsuarioSessao);
        				lisValores = new ArrayList();
        				if (tempList != null) {
        					for (int i = 0; i < tempList.size(); i++) {
        						PendenciaArquivoDt temDados = (PendenciaArquivoDt) tempList.get(i);
        						String[] stTemp = {temDados.getArquivoDt().getId(), temDados.getArquivoDt().getHash(), Funcoes.substituirCaracteresEspeciaisXML(temDados.getArquivoDt().getNomeArquivoFormatado(), true), temDados.getArquivoDt().getArquivoTipo(), Funcoes.substituirCaracteresEspeciaisXML(temDados.getArquivoDt().getUsuarioAssinador(), true), String.valueOf(temDados.getArquivoDt().isRecibo()), temDados.getResposta()};
        						lisValores.add(stTemp);
        					}
        				}
        				request.setAttribute("Paginacao", "1");
        				request.setAttribute("AtributosTag", stDescricaoListaArquivosPendencia);
        				request.setAttribute("Tag", stTag);
        				request.setAttribute("Operacao", "13");
        				request.setAttribute("Valores", lisValores);
        				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
					}
				}
				break;

			case 8: //BAIXAR ARQUIVO DE UMA PEND�NCIA
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 14 � BAIXAR ARQUIVO DE UMA PEND�NCIA (VERS�O 1)\nBaixa arquivo de uma pend�ncia.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=8\na=IdArquivo\nb=HashArquivo\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nO arquivo");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String stIdArquivo,
        				hashArquivo = null;
        				logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
        				if (request.getParameter("a") != null) stIdArquivo = request.getParameter("a");
        				else throw new WebServiceException("Id Arquivo n�o encontrado");
        				if (request.getParameter("b") != null) hashArquivo = request.getParameter("b");
        				else throw new WebServiceException("Hash do arquivo n�o encontrado");
        				if (UsuarioSessao.VerificarCodigoHashWebService(stIdArquivo, hashArquivo)) {
        					pendenciaArquivoNe = new PendenciaArquivoNe();
        					pendenciaArquivoNe.baixarArquivo(stIdArquivo, response, logDt);
        				} else throw new WebServiceException("Opera��o N�o Autorizada");
					}
				}
				return;

			case 9: //BAIXAR RECIBO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVI�O 15 � BAIXAR RECIBO (VERS�O 1)\nBaixa o recibo do arquivo, que cont�m, os dados da movimenta��o e o arquivo assinado.\nURL:\nhttps://projudi.tjgo.jus.br/servico03\nPAR�METROS:\nPaginaAtual=9\na=IdArquivo\nb=HashArquivo\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nO recibo");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String stIdArquivoRecibo,
        				hashRecibo = null;
        				logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
        
        				if (request.getParameter("a") != null) stIdArquivoRecibo = request.getParameter("a");
        				else throw new WebServiceException("Id Arquivo n�o encontrado");
        				if (request.getParameter("b") != null) hashRecibo = request.getParameter("b");
        				else throw new WebServiceException("Hash do arquivo n�o encontrado");
        				
        				if (UsuarioSessao.VerificarCodigoHashWebService(stIdArquivoRecibo, hashRecibo)) {
        					pendenciaArquivoNe = new PendenciaArquivoNe();
        					//pendenciaArquivoNe.baixarArquivo(stIdArquivoRecibo, response, logDt,true);
        				} else throw new WebServiceException("<{Opera��o N�o Autorizada");
					}
				}
				return;
				
			default:
				request.setAttribute("Mensagem", "Opera��o n�o definida");
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Operacao", "-1");
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		return;

	}

	private String qtdPaginas(long qtdRegistros) {
		long loTotal = 0;
		if ((qtdRegistros % Configuracao.TamanhoRetornoConsulta) != 0) {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta + 1;
		} else {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta;
		}
		return String.valueOf(loTotal);
	}
}