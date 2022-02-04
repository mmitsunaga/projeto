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
					request.setAttribute("Ajuda", "SERVI�O 5 � LISTAR CARGOS DA SERVENTIA (VERS�O 1)\nLista todos os cargos da serventia do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=1\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdServentiaCargo\nHashServentiaCargo\nDescricaoServentiaCargo\nDescricaoCargoTipo\nServentia\nIdUsuarioServentiaGrupo\nUsuarioResponsavel");
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

		case 2: //LISTAR USU�RIOS DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 6 � LISTAR USU�RIOS DA SERVENTIA (VERS�O 1)\nRespons�vel em listar os usu�rios cadastrados da serventia do usu�rio logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=2\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdUsuarioServentiaGrupo\nHashUsuario\nNome\nUsuario\nDescricaoGrupo");
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

		case 3: //RESPONDER PEND�NCIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 20 � RESPONDER PEND�NCIA (VERS�O 1)\nResponde pend�ncia da serventia ou do cargo inserindo um arquivo.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=3\na=IdPendencia\nb=Nome do Arquivo com Extens�o\nc=IdArquivoTipo\nd=Arquivo base64(assinado ou n�o)\ne=HashPendencia\nf=senha do certificado(opcional)\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nPend�ncia respondida com sucesso.");
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
					else throw new WebServiceException("Id Pend�ncia n�o encontrado");
					if (request.getParameter("b") != null) stNomeArquivo = request.getParameter("b");
					else throw new WebServiceException("Nome do Arquivo n�o encontrado");
					if (request.getParameter("c") != null) stIdArquivoTipo = request.getParameter("c");
					else throw new WebServiceException("Arquivo Tipo n�o encontrado");
					if (request.getParameter("d") != null) arquivoBase64 = request.getParameter("d");
					else throw new WebServiceException("Arquivo n�o encontrado");
					if (request.getParameter("e") != null) hash = request.getParameter("e");
					else throw new WebServiceException("Hash da pend�ncia n�o encontrado");
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
						request.setAttribute("Mensagem", "Pend�ncia respondida com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "20");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
						request.getSession(false).removeAttribute("ListaArquivos");
					} else throw new WebServiceException("Opera��o N�o Autorizada");
				}
			}
			break;

		case 4: //LER INTIMA��O
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 21 � LER INTIMA��O (VERS�O 1)\nResponde pend�ncia do tipo intima��o do cargo sem inserir arquivo.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=4\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\na=IdPendencia\nb=HashPendencia\nRETORNO:\nIntima��o lida com sucesso.");
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
					else throw new WebServiceException("Intima��o n�o encontrada");
					if (request.getParameter("b") != null) hash = request.getParameter("b");
					else throw new WebServiceException("Hash da pend�ncia n�o encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdPendenciaIntimacao, hash)) {
						pendenciaNe = new PendenciaNe();
						UsuarioSessao.getUsuarioDt().setIpComputadorLog(getIpCliente(request));
						UsuarioSessao.getUsuarioDt().setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						pendenciaNe.marcarLido(stIdPendenciaIntimacao, UsuarioSessao.getUsuarioDt().getId_UsuarioLog(), UsuarioSessao.getUsuarioDt().getIpComputadorLog(), UsuarioSessao.getUsuarioDt(), false);
						request.setAttribute("Mensagem", "Intima��o lida com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "21");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Opera��o N�o Autorizada");
				}
			}
			break;

		case 6: //TROCAR RESPONS�VEL POR PEND�NCIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 11 � TROCAR RESPONS�VEL POR PEND�NCIA (VERS�O 1)\nEfetua a troca do cargo que est� respons�vel por uma pend�ncia.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=6\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\na=IdPendencia\nb=IdServentiaCargo\nc=HashPendencia\nRETORNO:\nRespons�vel alterado com sucesso.");
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
					else throw new WebServiceException("Pend�ncia n�o encontrada");
					if (request.getParameter("b") != null) stIdNovoResponsavel = request.getParameter("b");
					else throw new WebServiceException("Novo Respons�vel n�o encontrado");
					if (request.getParameter("c") != null) hash = request.getParameter("c");
					else throw new WebServiceException("Hash da pend�ncia n�o encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdPendenciaTrocarResponsavel, hash)) {
						PendenciaResponsavelNe pendenciaResponsavelne = new PendenciaResponsavelNe();
						PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
						pendenciaResponsavelDt.setId_Pendencia(stIdPendenciaTrocarResponsavel);
						pendenciaResponsavelDt.setId_ServentiaCargo(stIdNovoResponsavel);
						pendenciaResponsavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						pendenciaResponsavelDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						pendenciaResponsavelne.salvarTrocaResponsavelPendencia(pendenciaResponsavelDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
						request.setAttribute("Mensagem", "Respons�vel alterado com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "11");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Opera��o N�o Autorizada");
				}
			}
			break;

		case 7: //HABILITAR USU�RIO EM CARGO DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 7 � HABILITAR USU�RIO EM CARGO DA SERVENTIA (VERS�O 1)\nHabilita um usu�rio em um determinado cargo da serventia..\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=7\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\na=IdServentiaCargo\nb=IdUsuarioServentiaGrupo\nc=HashServentiaCargo\nd=HashUsuario\nRETORNO:\nHabilita��o de usu�rio em cargo efetuada com sucesso.");
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
					else throw new WebServiceException("Cargo da Serventia n�o encontrado");
					if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stIdNovoUsuario = request.getParameter("b");
					else throw new WebServiceException("Usu�rio Respons�vel n�o encontrado");
					if (request.getParameter("c") != null && !request.getParameter("c").equals("")) hashServentiaCargo = request.getParameter("c");
					else throw new WebServiceException("C�digo Hash do ServentiaCargo n�o encontrado");
					if (request.getParameter("d") != null && !request.getParameter("d").equals("")) hashUsuario = request.getParameter("d");
					else throw new WebServiceException("C�digo Hash do Usu�rio n�o encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdServentiaCargo, hashServentiaCargo) && UsuarioSessao.VerificarCodigoHashWebService(stIdNovoUsuario, hashUsuario)) {
						ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
						ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
						serventiaCargoDt.setId(stIdServentiaCargo);
						serventiaCargoDt.setId_UsuarioServentiaGrupo(stIdNovoUsuario);
						serventiaCargoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						serventiaCargoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						serventiaCargoNe.habilitaUsuarioServentiaCargo(serventiaCargoDt);
						request.setAttribute("Mensagem", "Habilita��o de usu�rio em cargo efetuada com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "7");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Opera��o N�o Autorizada");
				}
			}
			break;

		case 8: //LIMPAR CARGO DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 8 � LIMPAR CARGO DA SERVENTIA (VERS�O 1)\nRetira o usu�rio habilitado em um determinado cargo, deixando este vazio.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=8\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\na=IdServentiaCargo\nb=IdUsuarioServentiaGrupo\nc=HashServentiaCargo\nd=HashUsuario\nRETORNO:\nDesabilita��o de usu�rio em cargo efetuada com sucesso.");
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
					else throw new WebServiceException("Cargo da Serventia n�o encontrado");
					if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hashServentiaCargo = request.getParameter("b");
					else throw new WebServiceException("C�digo Hash do ServentiaCargo n�o encontrado");
					if (UsuarioSessao.VerificarCodigoHashWebService(stIdServentiaCargo, hashServentiaCargo)) {
						ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
						ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
						serventiaCargoDt.setId(stIdServentiaCargo);
						serventiaCargoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						serventiaCargoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						serventiaCargoNe.desabilitaUsuarioServentiaCargo(serventiaCargoDt);
						request.setAttribute("Mensagem", "Desabilita��o de usu�rio em cargo efetuada com sucesso");
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Operacao", "8");
						stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					} else throw new WebServiceException("Opera��o N�o Autorizada");
				}
			}
			break;

		case 9: //LISTAR PROCESSOS DE PROMOTORES/ADVOGADOS/PROCURADORES
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 9 � LISTAR PROCESSOS DE PROMOTORES/ADVOGADOS/PROCURADORES (VERS�O 1)\nLista os processos onde o cargo do usu�rio logado � respons�vel.\nURL:\nhttps://projudi.tjgo.jus.br/servico02\nPAR�METROS:\nPaginaAtual=9\nPosicaoPaginaAtual=0(pagina��o)\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nId\nHashProcesso\nNumeroProcesso\nServentia\nNomePromovente\nNomePromovido");
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
						throw new WebServiceException("Usu�rio deve ser do grupo de promotores, advogados ou procuradores");
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