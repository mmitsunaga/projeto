package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.InsercaoArquivoCt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaGrupoNe;
import br.gov.go.tj.projudi.util.GerenciaArquivo;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;

public class Servico02Ct extends InsercaoArquivoCt {

	private static final long serialVersionUID = -4611155960760198215L;

	public int Permissao() {
		return 422;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		String stTag = "";
		String hash = null;
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		List lisValores = null;
		PendenciaNe pendenciaNe;
		List tempList = new ArrayList();
		String stVersao = "1";
		String stAjuda = "1";
		
		response.setContentType("text/xml; charset=UTF-8");

		switch (paginaatual) {

		case 1: //LISTAR CARGOS DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 5 – LISTAR CARGOS DA SERVENTIA (VERSÃO 1)\nLista todos os cargos da serventia do usuário logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=1\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nIdServentiaCargo\nHashServentiaCargo\nDescricaoServentiaCargo\nDescricaoCargoTipo\nServentia\nIdUsuarioServentiaGrupo\nUsuarioResponsavel");
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
					String[] stDescricaoListaServentiaCargo = {"IdServentiaCargo", "HashServentiaCargo", "DescricaoServentiaCargo", "DescricaoCargoTipo", "Serventia", "IdUsuarioServentiaGrupo", "UsuarioResponsavel"};
					stTag = "ServentiaCargo";
					tempList = new ServentiaCargoNe().consultarServentiaCargos(UsuarioSessao);
					lisValores = new ArrayList();
					if (tempList != null) {
						for (int i = 0; i < tempList.size(); i++) {
							ServentiaCargoDt temDados = (ServentiaCargoDt) tempList.get(i);
							String[] stTemp = {temDados.getId(), temDados.getHash(), temDados.getServentiaCargo(), temDados.getCargoTipo(), temDados.getServentia(), temDados.getId_UsuarioServentiaGrupo(), temDados.getNomeUsuario() };
							lisValores.add(stTemp);
						}
					}
					request.setAttribute("Paginacao", "1");
					request.setAttribute("AtributosTag", stDescricaoListaServentiaCargo);
					request.setAttribute("Tag", stTag);
					request.setAttribute("Operacao", "5");
					request.setAttribute("Valores", lisValores);
					stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;

		case 2: //LISTAR USUÁRIOS DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 6 – LISTAR USUÁRIOS DA SERVENTIA (VERSÃO 1)\nResponsável em listar os usuários cadastrados da serventia do usuário logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=2\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nIdUsuarioServentiaGrupo\nHashUsuario\nNome\nUsuario\nDescricaoGrupo");
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
					String[] stDescricaoListaUsuariosServentia = {"IdUsuarioServentiaGrupo", "HashUsuario", "Nome", "Usuario", "DescricaoGrupo"};
					stTag = "UsuariosServentia";
					tempList = new UsuarioServentiaGrupoNe().consultarTodosUsuarioServentiaGrupo(UsuarioSessao);
					lisValores = new ArrayList();
					if(tempList != null) {
						for (int i = 0; i < tempList.size(); i++) {
							UsuarioServentiaGrupoDt temDados = (UsuarioServentiaGrupoDt) tempList.get(i);
							String[] stTemp = {temDados.getId(), temDados.getHash(), Funcoes.substituirCaracteresEspeciaisXML(temDados.getNome(), true), temDados.getUsuario(), temDados.getGrupo()};
							lisValores.add(stTemp);
						}
					}
					request.setAttribute("Paginacao", "1");
					request.setAttribute("AtributosTag", stDescricaoListaUsuariosServentia);
					request.setAttribute("Tag", stTag);
					request.setAttribute("Operacao", "6");
					request.setAttribute("Valores", lisValores);
					stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;

		case 3: //RESPONDER PENDÊNCIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 20 – RESPONDER PENDÊNCIA (VERSÃO 1)\nResponde pendência da serventia ou do cargo inserindo um arquivo.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=3\na=IdPendencia\nb=Nome do Arquivo com Extensão\nc=IdArquivoTipo\nd=Arquivo base64(assinado ou não)\ne=HashPendencia\nf=senha do certificado(opcional)\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nPendência respondida com sucesso.");
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
					String stIdPendencia = "";
					String stNomeArquivo = "";
					String stIdArquivoTipo = "";
					String arquivoBase64 = "";
					String senhaCertificado = "";
					String contentType = "";
					if (request.getParameter("a") != null) stIdPendencia = request.getParameter("a");
					else throw new WebServiceException("Id Pendência não encontrado");
					if (request.getParameter("b") != null) stNomeArquivo = request.getParameter("b");
					else throw new WebServiceException("Nome do Arquivo não encontrado");
					if (request.getParameter("c") != null) stIdArquivoTipo = request.getParameter("c");
					else throw new WebServiceException("Arquivo Tipo não encontrado");
					if (request.getParameter("d") != null) arquivoBase64 = request.getParameter("d");
					else throw new WebServiceException("Arquivo não encontrado");
					if (request.getParameter("e") != null) hash = request.getParameter("e");
					else throw new WebServiceException("Hash da pendência não encontrado");
					if (request.getParameter("f") != null) senhaCertificado = request.getParameter("f");
					
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdPendencia, hash)) {
						pendenciaNe = new PendenciaNe();
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

						UsuarioSessao.getUsuarioDt().setIpComputadorLog(getIpCliente(request));
						UsuarioSessao.getUsuarioDt().setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
	    				List arquivos = Funcoes.converterMapParaList(mapArquivos);
						pendenciaNe.responderPendencia(stIdPendencia, UsuarioSessao.getUsuarioDt(), arquivos, null);
						request.setAttribute("Mensagem", "Pendência respondida com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "20");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
						request.getSession(false).removeAttribute("ListaArquivos");
					} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			break;

		case 4: //LER INTIMAÇÃO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 21 – LER INTIMAÇÃO (VERSÃO 1)\nResponde pendência do tipo intimação do cargo sem inserir arquivo.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=4\nh=número versão(ajuda)\nv=número versão(versão)\na=IdPendencia\nb=HashPendencia\nRETORNO:\nIntimação lida com sucesso.");
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
					String stIdPendenciaIntimacao;
					if (request.getParameter("a") != null) stIdPendenciaIntimacao = request.getParameter("a");
					else throw new WebServiceException("Intimação não encontrada");
					if (request.getParameter("b") != null) hash = request.getParameter("b");
					else throw new WebServiceException("Hash da pendência não encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdPendenciaIntimacao, hash)) {
						pendenciaNe = new PendenciaNe();
						UsuarioSessao.getUsuarioDt().setIpComputadorLog(getIpCliente(request));
						UsuarioSessao.getUsuarioDt().setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						pendenciaNe.marcarLido(stIdPendenciaIntimacao, UsuarioSessao.getUsuarioDt().getId_UsuarioLog(), UsuarioSessao.getUsuarioDt().getIpComputadorLog(), UsuarioSessao.getUsuarioDt(), false);
						request.setAttribute("Mensagem", "Intimação lida com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "21");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			break;

		case 6: //TROCAR RESPONSÁVEL POR PENDÊNCIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 11 – TROCAR RESPONSÁVEL POR PENDÊNCIA (VERSÃO 1)\nEfetua a troca do cargo que está responsável por uma pendência.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=6\nh=número versão(ajuda)\nv=número versão(versão)\na=IdPendencia\nb=IdServentiaCargo\nc=HashPendencia\nRETORNO:\nResponsável alterado com sucesso.");
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
					String stIdPendenciaTrocarResponsavel,
					stIdNovoResponsavel;
					if (request.getParameter("a") != null) stIdPendenciaTrocarResponsavel = request.getParameter("a");
					else throw new WebServiceException("Pendência não encontrada");
					if (request.getParameter("b") != null) stIdNovoResponsavel = request.getParameter("b");
					else throw new WebServiceException("Novo Responsável não encontrado");
					if (request.getParameter("c") != null) hash = request.getParameter("c");
					else throw new WebServiceException("Hash da pendência não encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdPendenciaTrocarResponsavel, hash)) {
						PendenciaResponsavelNe pendenciaResponsavelne = new PendenciaResponsavelNe();
						PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
						pendenciaResponsavelDt.setId_Pendencia(stIdPendenciaTrocarResponsavel);
						pendenciaResponsavelDt.setId_ServentiaCargo(stIdNovoResponsavel);
						pendenciaResponsavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						pendenciaResponsavelDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						pendenciaResponsavelne.salvarTrocaResponsavelPendencia(pendenciaResponsavelDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
						request.setAttribute("Mensagem", "Responsável alterado com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "11");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			break;

		case 7: //HABILITAR USUÁRIO EM CARGO DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 7 – HABILITAR USUÁRIO EM CARGO DA SERVENTIA (VERSÃO 1)\nHabilita um usuário em um determinado cargo da serventia..\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=7\nh=número versão(ajuda)\nv=número versão(versão)\na=IdServentiaCargo\nb=IdUsuarioServentiaGrupo\nc=HashServentiaCargo\nd=HashUsuario\nRETORNO:\nHabilitação de usuário em cargo efetuada com sucesso.");
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
					String stIdServentiaCargo,
					stIdNovoUsuario,
					hashServentiaCargo,
					hashUsuario;
					if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdServentiaCargo = request.getParameter("a");
					else throw new WebServiceException("Cargo da Serventia não encontrado");
					if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stIdNovoUsuario = request.getParameter("b");
					else throw new WebServiceException("Usuário Responsável não encontrado");
					if (request.getParameter("c") != null && !request.getParameter("c").equals("")) hashServentiaCargo = request.getParameter("c");
					else throw new WebServiceException("Código Hash do ServentiaCargo não encontrado");
					if (request.getParameter("d") != null && !request.getParameter("d").equals("")) hashUsuario = request.getParameter("d");
					else throw new WebServiceException("Código Hash do Usuário não encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdServentiaCargo, hashServentiaCargo) && UsuarioSessao.VerificarCodigoHashWebService(stIdNovoUsuario, hashUsuario)) {
						ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
						ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
						serventiaCargoDt.setId(stIdServentiaCargo);
						serventiaCargoDt.setId_UsuarioServentiaGrupo(stIdNovoUsuario);
						serventiaCargoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						serventiaCargoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						serventiaCargoNe.habilitaUsuarioServentiaCargo(serventiaCargoDt);
						request.setAttribute("Mensagem", "Habilitação de usuário em cargo efetuada com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "7");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			break;

		case 8: //LIMPAR CARGO DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 8 – LIMPAR CARGO DA SERVENTIA (VERSÃO 1)\nRetira o usuário habilitado em um determinado cargo, deixando este vazio.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=8\nh=número versão(ajuda)\nv=número versão(versão)\na=IdServentiaCargo\nb=IdUsuarioServentiaGrupo\nc=HashServentiaCargo\nd=HashUsuario\nRETORNO:\nDesabilitação de usuário em cargo efetuada com sucesso.");
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
					String stIdServentiaCargo = "";
					String hashServentiaCargo = "";
					if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdServentiaCargo = request.getParameter("a");
					else throw new WebServiceException("Cargo da Serventia não encontrado");
					if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hashServentiaCargo = request.getParameter("b");
					else throw new WebServiceException("Código Hash do ServentiaCargo não encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdServentiaCargo, hashServentiaCargo)) {
						ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
						ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
						serventiaCargoDt.setId(stIdServentiaCargo);
						serventiaCargoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						serventiaCargoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						serventiaCargoNe.desabilitaUsuarioServentiaCargo(serventiaCargoDt);
						request.setAttribute("Mensagem", "Desabilitação de usuário em cargo efetuada com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "8");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Operação Não Autorizada");
				}
			}
			break;

		case 9: //LISTAR PROCESSOS DE PROMOTORES/ADVOGADOS/PROCURADORES
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 9 – LISTAR PROCESSOS DE PROMOTORES/ADVOGADOS/PROCURADORES (VERSÃO 1)\nLista os processos onde o cargo do usuário logado é responsável.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPARÂMETROS:\nPaginaAtual=9\nPosicaoPaginaAtual=0(paginação)\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nId\nHashProcesso\nNumeroProcesso\nServentia\nNomePromovente\nNomePromovido");
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
					if (!UsuarioSessao.isAdvogado()) 
						throw new WebServiceException("Usuário deve ser do grupo de promotores, advogados ou procuradores");
					ProcessoNe processoNe = new ProcessoNe();
					tempList = processoNe.consultarProcessosPromotorAdvogado(UsuarioSessao, posicaopaginaatual);
					if (tempList != null) request.setAttribute("ListaProcessos", tempList);
					request.setAttribute("Paginacao", qtdPaginas(processoNe.getQuantidadePaginas()));
					request.setAttribute("Operacao", "9");
					stAcao = "/WEB-INF/jsptjgo/ListaProcessosXml.jsp";
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