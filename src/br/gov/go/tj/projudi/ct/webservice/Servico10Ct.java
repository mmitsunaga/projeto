package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.InsercaoArquivoCt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PeticionamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerenciaArquivo;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;
import br.gov.go.tj.utils.Certificado.Base64Utils;

public class Servico10Ct extends InsercaoArquivoCt {

	private static final long serialVersionUID = 1006641734390865104L;

	public int Permissao() {
		return 848;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		String a, b;
		String stTag = "";
		List lisValores = null;
		List lisArquivos = null;
		String stIdProcesso = "";
		String stHashProcesso = "";
		String stIdServentia = "";
		String stPedidoUrgencia = "";
		String stSigiloso = "";
		String stIdMovimentacaoTipo = "";
		String stNomeArquivo = "";
		String stIdArquivoTipo = "";
		String arquivoBase64 = "";
		String Mensagem = "";
		String idParte = "";
		String hashParte = "";
		String senhaCertificado = "";
		String contentType = "";
		String stVersao = "1";
		String stAjuda = "1";
		
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		List tempList = new ArrayList();
		
		response.setContentType("text/xml; charset=UTF-8");
		
		switch (paginaatual) {
		case 1: //LIBERAR ACESSO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 64 � LIBERAR ACESSO (VERS�O 1)\nLibera acesso aos arquivos de um processo pelo per�odo de 24 horas.\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=1\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nIdProcesso\nHashProcesso\nRETORNO:\nLibera��o de acesso efetuada com sucesso.");
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
    				Mensagem = "";
    				if (request.getParameter("a") != null) a = request.getParameter("a");
    				else throw new WebServiceException("Id do processo n�o encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) b = request.getParameter("b");
    				else throw new WebServiceException("Hash n�o encontrado");
    				if (UsuarioSessao.VerificarCodigoHashWebService(a, b)) {
    					ProcessoNe processoNe = new ProcessoNe();
    					ProcessoDt processoDt = new ProcessoDt();
    					processoDt = processoNe.consultarIdSimples(a);
    					if (processoDt.getSegredoJustica().equalsIgnoreCase("false")) {
    						processoNe.gerarPendeciaLiberarAcessoProcesso(UsuarioSessao.getUsuarioDt(), a, processoDt.isSegredoJustica());
    					} else {
    						Mensagem = "N�o � poss�vel realizar esta a��o. Motivo: Processo Segredo de Justi�a.";
    					}
    				} else
    					throw new WebServiceException("Opera��o N�o Autorizada");
    				if (Mensagem.length() == 0) {
    					request.setAttribute("Mensagem", "Libera��o de acesso efetuada com sucesso");
    					request.setAttribute("RespostaTipo", "OK");
    				} else {
    					request.setAttribute("RespostaTipo", "ERRO");
    					request.setAttribute("Mensagem", Mensagem);
    				}
    				request.setAttribute("Operacao", "64");
    				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				}
			}
			break;

		case 2: //ALTERAR/CADASTRAR DADOS DA PARTE
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 65 � ALTERAR/CADASTRAR DADOS DA PARTE (VERS�O 1)\nAltera dados da parte caso seja fornecido o Id da Parte. Caso contr�rio, cadastra uma nova parte.\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=3\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nArquivoAssinado64\nIdParte\nHashParte\nRETORNO:\nParte alterada com sucesso.");
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
					arquivoBase64	= (String) request.getParameter("a");
    				idParte 		= (String) request.getParameter("b");
    				hashParte 		= (String) request.getParameter("c");
    
    				if (UsuarioSessao.VerificarCodigoHashWebService(idParte, hashParte)) {
    					String conteudoArquivo = new String(Base64Utils.base64Decode(arquivoBase64));
    					if (conteudoArquivo != null && conteudoArquivo.length() > 0) {
    						LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
    						ProcessoParteNe processoParteNe = new ProcessoParteNe();
    						processoParteNe.salvarWebservice(conteudoArquivo, logDt);
    					} else throw new WebServiceException("Selecione um Arquivo para prosseguir");
    				} else throw new WebServiceException("Opera��o N�o Autorizada");
    
    				request.setAttribute("Mensagem", "Parte alterada com sucesso");
    				request.setAttribute("RespostaTipo", "OK");
    				request.setAttribute("Operacao", "65");
    				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				}
			}
			break;
			
		case 3: //PETICIONAR EM PROCESSO COM M�LTIPLOS ARQUIVOS
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 66 � PETICIONAR EM PROCESSO COM M�LTIPLOS ARQUIVOS (VERS�O 1)\nInsere arquivos em processo j� cadastrado.\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=3\na=IdProcesso\nb=NomeArquivo\nc=IdArquivoTipo\nd=ArquivoBase64\ne=SenhaCertificado(opcional)\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nArquivo inserido com sucesso.");
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
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
    				else throw new WebServiceException("Id Processo n�o encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stNomeArquivo = request.getParameter("b");
    				else throw new WebServiceException("Nome do Arquivo n�o encontrado");
    				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) stIdArquivoTipo = request.getParameter("c");
    				else throw new WebServiceException("Id Tipo de Arquivo n�o encontrado");
    				if (request.getParameter("d") != null && !request.getParameter("d").equals("")) arquivoBase64 = request.getParameter("d");
    				else throw new WebServiceException("Arquivo n�o encontrado");
    				if (request.getParameter("e") != null && !request.getParameter("e").equals("")) senhaCertificado = request.getParameter("e");
    				
    				ArquivoDt arquivoDt = new ArquivoDt();
    				arquivoDt.setId_ArquivoTipo(stIdArquivoTipo);
    				arquivoDt.setArquivo(arquivoBase64);
    				arquivoDt.setNomeArquivo(stNomeArquivo);
    				contentType = GerenciaArquivo.getInstancia().getTipoConteudo(arquivoDt.getNomeArquivo());
    				arquivoDt.setContentType(contentType);
    				
    				if(senhaCertificado.length() > 0) {
    					arquivoDt.setAssinado(false);
    					arquivoDt.setSenhaCertificado(senhaCertificado);
    					arquivoDt.setSalvarSenha(false);
    					arquivoDt.setGerarAssinatura(true);
    				} else {
    					arquivoDt.setAssinado(true);
    					arquivoDt.setGerarAssinatura(false);
    				}
    				
    				setArquivo(arquivoDt, request);
    				
    				request.setAttribute("Mensagem", "Arquivo inserido com sucesso");
    				request.setAttribute("RespostaTipo", "OK");
    				request.setAttribute("Operacao", "66");
    				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				}
			}
			break;
			
		case 4: //CONCLUIR PETICIONAMENTO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 67 � CONCLUIR PETICIONAMENTO (VERS�O 1)\nConclui a inser��o dos arquivos do servi�o anterior(66).\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=4\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nPeticionamento efetuado com sucesso.");
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
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
    				else throw new WebServiceException("Processo n�o encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stIdMovimentacaoTipo = request.getParameter("b");
    				else throw new WebServiceException("Tipo de Movimenta��o n�o encontrado");
    				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) stPedidoUrgencia = request.getParameter("c");
    				else throw new WebServiceException("Pedido de Urg�ncia n�o encontrado");
    				if (request.getParameter("d") != null && !request.getParameter("d").equals("")) stSigiloso = request.getParameter("d");
    
    				// Monta dados para peticionamento
    				Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
    				List lista = Funcoes.converterMapParaList(mapArquivos);
    				MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
    				PeticionamentoDt peticionamentoDt = new PeticionamentoDt();
    				ProcessoDt processoDt = movimentacaoNe.consultarProcessoId(stIdProcesso);
    				peticionamentoDt.setId_MovimentacaoTipo(stIdMovimentacaoTipo);
    				peticionamentoDt.setListaArquivos(lista);
    				peticionamentoDt.addListaProcessos(processoDt);
    				if (stPedidoUrgencia != null && stPedidoUrgencia.equalsIgnoreCase("S")) peticionamentoDt.setPedidoUrgencia(true);
    				if((stSigiloso != null && stSigiloso.equalsIgnoreCase("S")) && (UsuarioSessao.isAutoridadePolicial() || UsuarioSessao.isMp())) peticionamentoDt.setSegredoJustica(true);
    				peticionamentoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
    				//if(UsuarioSessao.isAutoridadePolicial()) peticionamentoDt.setComplemento(UsuarioSessao.getUsuarioDt().getServentia());
    
    				String id_Movimentacao = movimentacaoNe.salvarPeticionamento(peticionamentoDt, UsuarioSessao.getUsuarioDt());
    				
    				// Muda classe do processo(de 1280-Auto de Pris�o em Flagrante(CF) para 1279-Inqu�rito Policial(CPP) ou de 2461-Auto de Apreens�o em Flagrante para 2460-Procedimentos Investigat�rios(ECA))
    				if(UsuarioSessao.isAutoridadePolicial() && lista != null && lista.size() > 0) {
    					ArquivoDt arquivo = (ArquivoDt) lista.get(0);
    					if(Funcoes.StringToInt(arquivo.getId_ArquivoTipo()) == ArquivoTipoDt.ID_INQUERITO_POLICIAL && processoDt.getId_ProcessoTipo().equals(String.valueOf(ProcessoTipoDt.ID_APF))) {
    						new ProcessoNe().alterarProcessoTipo(stIdProcesso, String.valueOf(ProcessoTipoDt.ID_IP), "Inqu�rito Policial (CPP)", processoDt.getProcessoTipo(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
    					} else if(Funcoes.StringToInt(arquivo.getId_ArquivoTipo()) == ArquivoTipoDt.ID_AUTO_INVESTIGACAO && processoDt.getId_ProcessoTipo().equals(String.valueOf(ProcessoTipoDt.ID_AAF))) {
    						new ProcessoNe().alterarProcessoTipo(stIdProcesso, String.valueOf(ProcessoTipoDt.ID_AI), "Procedimentos Investigat�rios (ECA)", processoDt.getProcessoTipo(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
    					}
    				}
    				String[] stDescricaoListaArquivos = {"IdMovimentacao"};
    				stTag = "Movimentacao";
    				String[] stTemp = {id_Movimentacao};
    				lisValores = new ArrayList();
					lisValores.add(stTemp);
    				
    				request.setAttribute("RespostaTipo", "OK");
    				request.setAttribute("Operacao", "67");
    				request.setAttribute("Tag", stTag);
    				request.setAttribute("AtributosTag", stDescricaoListaArquivos);
    				request.setAttribute("Valores", lisValores);
    				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
    				peticionamentoDt = new PeticionamentoDt();
    				request.getSession(false).removeAttribute("ListaArquivos");
    				request.getSession(false).removeAttribute("ArquivoTipo");
				}
			}
			break;
			
		case 6: //LISTAR PROCESSOS EM SIGILO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 68 � LISTAR PROCESSOS EM SIGILO (VERS�O 1)\nConsulta processos em sigilo, do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=6\nPosicaoPaginaAtual=0(pagina��o)\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nId\nHashProcesso\nNumeroProcesso\nServentia\nIdProcessoTipo\nValor\nNomePromovente\nNomePromovido");
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
    				ProcessoNe processoNe = new ProcessoNe();
    				tempList = processoNe.consultarProcessosSigiloso(UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
    				if (tempList != null) {
    					UsuarioSessao.setHashIdProcesso(tempList);
    					request.setAttribute("ListaProcessos", tempList);
    				}
    				request.setAttribute("Paginacao", qtdPaginas(processoNe.getQuantidadePaginas()));
    				request.setAttribute("Operacao", "68");
    				stAcao = "/WEB-INF/jsptjgo/ListaProcessosXml.jsp";
				}
			}
			break;
			
		case 7: //LISTAR RESPONS�VEIS DE PROCESSO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 69 � LISTAR RESPONS�VEIS DE PROCESSO (VERS�O 1)\nLista os respons�veis de um processo.\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=7\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nIdProcesso\nHashProcesso\nIdServentia\nRETORNO:\nIdServentiaCargo\nCargo\nNome\nIdServentiaCargo\nServentia");
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
    				String[] stDescricaoListaArquivos = {"IdServentiaCargo", "Cargo", "Nome", "IdServentia", "Serventia"};
    				stTag = "Responsaveis";
    				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
    				else throw new WebServiceException("Id Processo n�o encontrado");
    				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stHashProcesso = request.getParameter("b");
    				else throw new WebServiceException("Hash n�o encontrado");
    				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) stIdServentia = request.getParameter("c");
    				else throw new WebServiceException("Id Serventia n�o encontrado");
    				ProcessoNe processone = new ProcessoNe();
    				tempList = processone.consultarResponsaveisProcesso(stIdProcesso, stIdServentia, UsuarioSessao.getGrupoCodigo());
    				
    				lisValores = new ArrayList();
    				if (tempList != null) {
    					for (int i = 0; i < tempList.size(); i++) {
    						ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) tempList.get(i);
    						String[] stTemp = {serventiaCargoDt.getId(), serventiaCargoDt.getCargoTipo(), serventiaCargoDt.getNomeUsuario(), serventiaCargoDt.getId_Serventia(), serventiaCargoDt.getServentia()};
    						lisValores.add(stTemp);
    					}
    				}
    				request.setAttribute("Paginacao", "1");
    				request.setAttribute("AtributosTag", stDescricaoListaArquivos);
    				request.setAttribute("Tag", stTag);
    				request.setAttribute("Operacao", "69");
    				request.setAttribute("Valores", lisValores);
    				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;
			
		case 8: //LISTAR INTIMA��ES DA PROMOTORIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 70 � LISTAR INTIMA��ES DA PROMOTORIA (VERS�O 1)\nLista as intima��es n�o lidas da promotoria do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=8\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdPendencia\nHashPendencia\nIdProcesso\nNumeroProcesso\nHashProcesso\nIdMovimentacao\nMovimentacao\nTipoPendencia\nDataInicio\nStatus\nIdResponsavel\nNomeResponsavel");
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
    				String[] stDescricaoListaPendenciasIntimacoes = {"IdPendencia", "HashPendencia", "IdProcesso", "NumeroProcesso", "HashProcesso", "ProcessoTipo", "IdMovimentacao", "Movimentacao", "TipoPendencia", "DataInicio", "Status", "IdResponsavel", "NomeResponsavel"};
    				stTag = "PendenciasIntimacoes";
    				PendenciaNe pendenciaNe = new PendenciaNe();
    				tempList = pendenciaNe.consultarIntimacoesPromotoria(UsuarioSessao);
    				lisValores = new ArrayList();
    				if (tempList != null) {
    					for (int i = 0; i < tempList.size(); i++) {
    						PendenciaDt objPendencia = (PendenciaDt) tempList.get(i);
    						String[] stTemp = {objPendencia.getId(), objPendencia.getHash(), objPendencia.getId_Processo(), Funcoes.formataNumeroCompletoProcessoNovo(objPendencia.getProcessoNumeroCompleto()), objPendencia.getProcessoDt().getHash(), objPendencia.getProcessoTipo(), objPendencia.getId_Movimentacao(), objPendencia.getMovimentacao(), objPendencia.getPendenciaTipo(), objPendencia.getDataInicio(), objPendencia.getPendenciaStatus(), objPendencia.getId_Promotor(), objPendencia.getPromotor()};
    						lisValores.add(stTemp);
    					}
    				}
    				request.setAttribute("AtributosTag", stDescricaoListaPendenciasIntimacoes);
    				request.setAttribute("Tag", stTag);
    				request.setAttribute("Operacao", "70");
    				request.setAttribute("Valores", lisValores);
    				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;
			
		case 9: //CONSULTAR PROCESSO POR N�MERO DE TCO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 78 � CONSULTAR PROCESSOS POR N�MERO DE TCO (VERS�O 1)\nConsulta processos utilizando o n�mero do TCO.\nURL:\nhttps://projudi.tjgo.jus.br/servico10\nPAR�METROS:\nPaginaAtual=9\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\na=n�mero TCO\nRETORNO:\nId\nNumeroProcesso\nServentia\nIdProcessoTipo\nValor\nNomePromovente\nNomePromovido");
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
    				if (request.getParameter("a") != null) a = request.getParameter("a");
    				else throw new WebServiceException("N�mero do TCO n�o encontrado");
    				tempList =  new ProcessoNe().consultarProcessosTCO(a);
    				if (tempList != null) request.setAttribute("ListaProcessos", tempList);
    				request.setAttribute("Operacao", "78");
    				stAcao = "/WEB-INF/jsptjgo/ListaProcessosXml.jsp";
				}
			}
			break;

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