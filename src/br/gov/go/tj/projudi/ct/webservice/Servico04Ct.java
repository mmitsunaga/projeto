package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.AdvogadoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PeticionamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerenciaArquivo;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;
import br.gov.go.tj.utils.Certificado.Base64Utils;

public class Servico04Ct extends Controle {

	private static final long serialVersionUID = 6052285281346198574L;

	public int Permissao() {
		return 426;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		String stTag = "";
		List tempList = new ArrayList();
		List lisValores = null;
		String hash = null;
		ProcessoNe processoNe = null;
		ProcessoDt processoDt = null;
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		String stVersao = "1";
		String stAjuda = "1";

		switch (paginaatual) {

		case 1: //TROCAR PROMOTOR RESPONSÁVEL POR PROCESSO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 10 – TROCAR PROMOTOR RESPONSÁVEL POR PROCESSO (VERSÃO 1)\nTroca o cargo de promotor que está responsável por um processo.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=1\na=IdProcesso\nb=IdServentiaCargo\nc=HashProcesso\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nResponsável por processo alterado com sucesso.");
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
    				String stIdProcessoTrocarResponsavel,
    				stIdServentiaCargo;
    				
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcessoTrocarResponsavel = request.getParameter("a");
    				else throw new WebServiceException("Id Processo não encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stIdServentiaCargo = request.getParameter("b");
    				else throw new WebServiceException("Id Cargo da Serventia do novo responsável não encontrado");
    				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) hash = request.getParameter("c");
    				else throw new WebServiceException("Hash do processo não encontrado");
    
    				if (UsuarioSessao.VerificarCodigoHashWebService(stIdProcessoTrocarResponsavel, hash)) {
    					ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
    					processoResponsavelNe.salvarTrocaResponsavel(stIdServentiaCargo, stIdProcessoTrocarResponsavel, UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
    					request.setAttribute("Mensagem", "Responsável por processo alterado com sucesso");
    					request.setAttribute("RespostaTipo", "OK");
    					request.setAttribute("Operacao", "10");
    					stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
    				} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			break;

		case 2: //LISTAR TIPOS DE MOVIMENTAÇÃO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 22 – LISTAR TIPOS DE MOVIMENTAÇÃO (VERSÃO 1)\nLista os tipos de movimentações vinculados ao grupo do usuário logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=2\nPosicaoPaginaAtual=paginação\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nIdMovimentacaoTipo\nDescricaoMovimentacaoTipo");
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
    				String[] stDescricaoListaArquivoTipo = {"IdMovimentacaoTipo", "DescricaoMovimentacaoTipo"};
    				stTag = "MovimentacaoTipo";
    				
    				MovimentacaoTipoNe movimentacaoTipoNe = new MovimentacaoTipoNe();
    				tempList = movimentacaoTipoNe.consultarGrupoMovimentacaoTipo(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), "", posicaopaginaatual);
    				lisValores = new ArrayList();
    				
    				if (tempList != null) {
    					for (int i = 0; i < tempList.size(); i++) {
    						MovimentacaoTipoDt obj = (MovimentacaoTipoDt) tempList.get(i);
    						String[] stTemp = {obj.getId(), Funcoes.substituirCaracteresEspeciaisXML(obj.getMovimentacaoTipo(), true) };
    						lisValores.add(stTemp);
    					}
    				}
    				request.setAttribute("Paginacao", qtdPaginas(movimentacaoTipoNe.getQuantidadePaginas()));
    				request.setAttribute("AtributosTag", stDescricaoListaArquivoTipo);
    				request.setAttribute("Tag", stTag);
    				request.setAttribute("Operacao", "22");
    				request.setAttribute("Valores", lisValores);
    				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;

		case 3: //LISTAR MOVIMENTAÇÕES DE UM PROCESSO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 23 – LISTAR MOVIMENTAÇÕES DE UM PROCESSO (VERSÃO 1)\nLista as movimentações de um processo.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=3\na=IdProcesso\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nIdMovimentacao\nDescricaoMovimentacao\nComplemento\nDataRealizacao\nUsuarioRealizador\nNomeUsuarioRealizador");
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
    				String stIdProcesso, complemento;
    				String[] stDescricaoListaMovimentacoes = {"IdMovimentacao", "DescricaoMovimentacao", "Complemento", "DataRealizacao", "UsuarioRealizador", "NomeUsuarioRealizador"};
    				stTag = "Movimentacao";
    
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
    				else throw new WebServiceException("IdProcesso não encontrado");
    
    				MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
    				tempList = movimentacaoNe.consultarMovimentacoesProcesso(UsuarioSessao.getUsuarioDt(), stIdProcesso, UsuarioSessao.getNivelAcesso());
    
    				lisValores = new ArrayList();
    				if (tempList != null) {
    					for (int i = 0; i < tempList.size(); i++) {
    						MovimentacaoDt obj = (MovimentacaoDt) tempList.get(i);
    						complemento = "";
    						if(obj.getComplemento().indexOf("<a") != -1 && obj.getComplemento().indexOf("a>") != -1) {
    							complemento = obj.getComplemento().substring(0, obj.getComplemento().indexOf("<a")) + obj.getComplemento().substring(obj.getComplemento().indexOf("a>") + 2, obj.getComplemento().length());
    						} else {
    							complemento = obj.getComplemento();
    						}
    						String[] stTemp = {obj.getId(), Funcoes.substituirCaracteresEspeciaisXML(obj.getMovimentacaoTipo(), true) , Funcoes.substituirCaracteresEspeciaisXML(complemento, true), obj.getDataRealizacao(), obj.getUsuarioRealizador(), Funcoes.substituirCaracteresEspeciaisXML(obj.getNomeUsuarioRealizador(), true) };
    						lisValores.add(stTemp);
    					}
    				}
    				request.setAttribute("Paginacao", "1");
    				request.setAttribute("AtributosTag", stDescricaoListaMovimentacoes);
    				request.setAttribute("Tag", stTag);
    				request.setAttribute("Operacao", "23");
    				request.setAttribute("Valores", lisValores);
    				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;

		case 4: //LISTAR ARQUIVOS DE UMA MOVIMENTAÇÃO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 24 – LISTAR ARQUIVOS DE UMA MOVIMENTAÇÃO (VERSÃO 1)\nLista os arquivos vinculados à movimentação de um processo.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=4\na=IdMovimentacao\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nIdArquivo\nHashArquivo\nNome\nArquivoTipoCodigo\nArquivoTipo\nAssinador\nRecibo");
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
    				String stIdMovimentacao;
    				String[] stDescricaoListaArquivos = {"IdArquivo", "HashArquivo", "Nome", "ArquivoTipoCodigo", "ArquivoTipo", "Assinador", "Recibo"};
    				stTag = "ArquivosMovimentacao";
    				
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdMovimentacao = request.getParameter("a");
    				else throw new WebServiceException("IdMovimentacao não encontrado");
    				
    				MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
    				tempList = movimentacaoArquivoNe.consultarArquivosMovimentacaoWebServiceComHash(stIdMovimentacao, UsuarioSessao);
    				lisValores = new ArrayList();
    				if (tempList != null) {
    					for (int i = 0; i < tempList.size(); i++) {
    						MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) tempList.get(i);
    						String[] stTemp = {movimentacaoArquivoDt.getId(), movimentacaoArquivoDt.getHash(), Funcoes.substituirCaracteresEspeciaisXML(movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado(), true), movimentacaoArquivoDt.getArquivoDt().getArquivoTipoCodigo(), movimentacaoArquivoDt.getArquivoDt().getArquivoTipo(), Funcoes.substituirCaracteresEspeciaisXML(movimentacaoArquivoDt.getArquivoDt().getUsuarioAssinador(), true), String.valueOf(movimentacaoArquivoDt.getArquivoDt().isRecibo())};
    						lisValores.add(stTemp);
    					}
    				}
    				request.setAttribute("Paginacao", "1");
    				request.setAttribute("AtributosTag", stDescricaoListaArquivos);
    				request.setAttribute("Tag", stTag);
    				request.setAttribute("Operacao", "24");
    				request.setAttribute("Valores", lisValores);
    				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;

		case 6: //BAIXAR ARQUIVO DE UMA MOVIMENTAÇÃO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 25 – BAIXAR ARQUIVO DE UMA MOVIMENTAÇÃO (VERSÃO 1)\nBaixa arquivo de uma movimentação processual.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=6\na=IdArquivo\nb=HashArquivo\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nO próprio arquivo.");
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
    				String stIdMovimentacaoArquivo,
    				hashArquivo = null,
    				idProcesso = "";
    				boolean recibo = false;
    				
    				LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdMovimentacaoArquivo = request.getParameter("a");
    				else throw new WebServiceException("IdArquivo não encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hashArquivo = request.getParameter("b");
    				else throw new WebServiceException("Hash do Arquivo não encontrado");
    				
    				if (UsuarioSessao.VerificarCodigoHashWebService(stIdMovimentacaoArquivo, hashArquivo)) {
    					MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
    					idProcesso = movimentacaoArquivoNe.consultarIdProcesso(stIdMovimentacaoArquivo);
    					processoNe = new ProcessoNe();
    					processoDt = new ProcessoDt();
    					processoDt = processoNe.consultarId(idProcesso);
    					if(!movimentacaoArquivoNe.baixarArquivoMovimentacaoWebService(stIdMovimentacaoArquivo, UsuarioSessao.getUsuarioDt(), recibo, response, logDt, processoDt))
    						throw new WebServiceException("Usuário sem permissão para baixar o arquivo");
    				} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			return;

		case 7: //PETICIONAR EM PROCESSO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 26 – PETICIONAR EM PROCESSO (VERSÃO 1)\nInsere arquivo em processo já cadastrado.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=7\na=IdProcesso\nb=IdMovimentacaoTipo\nc=nome do arquivo\nd=IdArquivoTipo\ne=arquivo assinado base64\nf=pedido de urgência\ng=segredo de justiça\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nPeticionamento efetuado com sucesso.");
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
    				String stIdMovimentacaoTipo, stNomeArquivo,	stIdArquivoTipo, stPedidoUrgencia, arquivoAssinado64, stIdProcesso,	segredoJustica = "";
    				
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
    				else throw new WebServiceException("IdProcesso não encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stIdMovimentacaoTipo = request.getParameter("b");
    				else throw new WebServiceException("IdMovimentacaoTipo não encontrado");
    				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) stNomeArquivo = request.getParameter("c");
    				else throw new WebServiceException("Nome do Arquivo não encontrado");
    				if (request.getParameter("d") != null && !request.getParameter("d").equals("")) stIdArquivoTipo = request.getParameter("d");
    				else throw new WebServiceException("IdArquivoTipo não encontrado");
    				if (request.getParameter("e") != null && !request.getParameter("e").equals("")) arquivoAssinado64 = request.getParameter("e");
    				else throw new WebServiceException("Arquivo Assinado não encontrado");
    				if (request.getParameter("f") != null && !request.getParameter("f").equals("")) stPedidoUrgencia = request.getParameter("f");
    				else throw new WebServiceException("Pedido de Urgência não encontrado");
    				if (request.getParameter("g") != null && !request.getParameter("g").equals("")) segredoJustica = request.getParameter("g");
    				
    				// Monta arquivo
    				ArquivoDt arquivoDt = new ArquivoDt();
    				arquivoDt.setId_ArquivoTipo(stIdArquivoTipo);
    				arquivoDt.setArquivo(Base64Utils.base64Decode(arquivoAssinado64));
    				if(stNomeArquivo.contains(".p7s"))
    					arquivoDt.setNomeArquivo(stNomeArquivo);
    				else
    					arquivoDt.setNomeArquivo(stNomeArquivo + ".p7s");
    				GerenciaArquivo.getInstancia().getArquivoP7s(arquivoDt);
    				// Monta dados para peticionamento				
    				MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
    				processoDt = movimentacaoNe.consultarProcessoId(stIdProcesso);
    				PeticionamentoDt peticionamentoDt = new PeticionamentoDt();
    				peticionamentoDt.setId_MovimentacaoTipo(stIdMovimentacaoTipo);
    				peticionamentoDt.addListaArquivos(arquivoDt);
    				peticionamentoDt.addListaProcessos(processoDt);
    				if(stPedidoUrgencia != null && stPedidoUrgencia.equalsIgnoreCase("S")) peticionamentoDt.setPedidoUrgencia(true);
    				if((segredoJustica != null && segredoJustica.equalsIgnoreCase("S")) && (UsuarioSessao.isAutoridadePolicial() || UsuarioSessao.isMp())) peticionamentoDt.setSegredoJustica(true);
    				peticionamentoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
    //				if(UsuarioSessao.isAutoridadePolicial()) peticionamentoDt.setComplemento(UsuarioSessao.getUsuarioDt().getServentia());
    				movimentacaoNe.salvarPeticionamento(peticionamentoDt, UsuarioSessao.getUsuarioDt());
    				// Muda classe do processo(de 1280-Auto de Prisão em Flagrante(CF) para 1279-Inquérito Policial(CPP) ou de 2461-Auto de Apreensão em Flagrante para 2460-Procedimentos Investigatórios(ECA))
    				if(UsuarioSessao.isAutoridadePolicial()) {
    					if(Funcoes.StringToInt(stIdArquivoTipo) == ArquivoTipoDt.ID_INQUERITO_POLICIAL && processoDt.getId_ProcessoTipo().equals(String.valueOf(ProcessoTipoDt.ID_APF))) {
    						new ProcessoNe().alterarProcessoTipo(stIdProcesso, String.valueOf(ProcessoTipoDt.ID_IP), "Inquérito Policial (CPP)", processoDt.getProcessoTipo(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
    					} else if(Funcoes.StringToInt(stIdArquivoTipo) == ArquivoTipoDt.ID_AUTO_INVESTIGACAO && processoDt.getId_ProcessoTipo().equals(String.valueOf(ProcessoTipoDt.ID_AAF))) {
    						new ProcessoNe().alterarProcessoTipo(stIdProcesso, String.valueOf(ProcessoTipoDt.ID_AI), "Procedimentos Investigatórios (ECA)", processoDt.getProcessoTipo(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
    					}
    				}
    				request.setAttribute("Mensagem", "Peticionamento efetuado com sucesso.");
    				request.setAttribute("RespostaTipo", "OK");
    				request.setAttribute("Operacao", "26");
    				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				}
			}
			break;

		case 8: //LISTAR DADOS DE PROCESSO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 27 – LISTAR DADOS DE PROCESSO (VERSÃO 1)\nLista os dados de um processo.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=8\na=IdProcesso\nb=HashProcesso\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nOs dados do processo.");
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
    				String stIdProcesso = "";
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
    				else throw new WebServiceException("IdProcesso não encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hash = request.getParameter("b");
    				else throw new WebServiceException("HashProcesso não encontrado");
    
    				if (UsuarioSessao.VerificarCodigoHashWebService(stIdProcesso, hash)) {
    					processoNe = new ProcessoNe();
    					processoDt = processoNe.consultarIdCompletoWebService(stIdProcesso);
    					List<String[]> dataAudiencia = processoNe.consultarAudienciasPendentes(processoDt.getId(), false);
    					ServentiaDt serventiaDt = processoNe.consultarIdServentia(processoDt.getId_Serventia());
    					request.setAttribute("Operacao", "27");
    					request.setAttribute("ProcessoCompletoDt", processoDt);
    					request.setAttribute("ServentiaDt", serventiaDt);
    					if(dataAudiencia.size() > 0) request.setAttribute("DataAudiencia", dataAudiencia.get(0)[2]);
    					processoDt = new ProcessoDt();
    					request.getSession(false).removeAttribute("ProcessoCompletoDt");
    					stAcao = "/WEB-INF/jsptjgo/DadosProcessoXml.jsp";
    				} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			break;

		case 9: //DESATIVAR PROCURADOR
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 47 – DESATIVAR PROCURADOR (VERSÃO 1)\nDesativa procurador em procuradoria.\nURL:\nhttps://projudi.tjgo.jus.br/servico04\nPARÂMETROS:\nPaginaAtual=9\na=Cpf\nb=IdServentia\nc=Oab\nd=Complemento\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nProcurador desativado com sucesso.");
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
    				String cpfUsuario, idServentia, oabAdvogado, complementoAdvogado, stId;
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) cpfUsuario = request.getParameter("a");
    				else throw new WebServiceException("CPF não encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) idServentia = request.getParameter("b");
    				else throw new WebServiceException("IdServentia não encontrado");
    				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) oabAdvogado = request.getParameter("c");
    				else throw new WebServiceException("OAB não encontrado");
    				if (request.getParameter("d") != null && !request.getParameter("d").equals("")) complementoAdvogado = request.getParameter("d");
    				else throw new WebServiceException("Complemento não encontrado");
    				UsuarioDt Advogadodt = new AdvogadoDt();
    				UsuarioNe Advogadone = new UsuarioNe();
    				UsuarioServentiaOabDt UsuarioServentiaOabdt = new UsuarioServentiaOabDt();
    				Advogadodt = Advogadone.consultarUsuarioCpf(cpfUsuario);
    				stId = Advogadodt.getId();
    				if (stId != null && !stId.equalsIgnoreCase("")) {
    					Advogadodt.limpar();
    					Advogadodt = Advogadone.consultarAdvogadoId(stId);
    					tempList = Advogadone.consultarServentiaOabAdvogado(stId);
    					if (tempList.size() > 0) Advogadodt.setListaUsuarioServentias(tempList);
    				}
    				UsuarioServentiaOabdt.setOabNumero(oabAdvogado);
    				UsuarioServentiaOabdt.setOabComplemento(complementoAdvogado);
    				Advogadodt.setUsuarioServentiaOab(UsuarioServentiaOabdt);
    				Advogadodt.setId_Serventia(idServentia);
    				Advogadone.verificarServentiaOAB(Advogadodt);
    				if (!Advogadodt.getId_Serventia().equalsIgnoreCase("") && !Advogadodt.getId().equalsIgnoreCase("")) {
    					if (Advogadone.verificarHabilitacaoUsuario(Advogadodt)) {
    						for (int i = 0; i < Advogadodt.getListaUsuarioServentias().size(); i++) {
    							UsuarioDt dt = (UsuarioDt) Advogadodt.getListaUsuarioServentias().get(i);
    							if (dt.getUsuarioServentiaOab().getOabNumero().equalsIgnoreCase(oabAdvogado) && dt.getUsuarioServentiaOab().getOabComplemento().equalsIgnoreCase(complementoAdvogado) && dt.getId_Serventia().equalsIgnoreCase(idServentia)) {
    								dt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
    								dt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
    								Advogadone.desativarUsuarioServentia(dt);
    								request.setAttribute("Mensagem", "Procurador desativado com sucesso.");
    								request.setAttribute("RespostaTipo", "OK");
    							}
    						}
    					} else {
    					request.setAttribute("Mensagem", "Não foi possível obter Serventia/Grupo para Desativação");
    					request.setAttribute("RespostaTipo", "ERRO");
    					}
    				}
    				request.setAttribute("Operacao", "47");
    				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				}
			}
			break;

		default:
			request.setAttribute("Mensagem", "Operação não definida");
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