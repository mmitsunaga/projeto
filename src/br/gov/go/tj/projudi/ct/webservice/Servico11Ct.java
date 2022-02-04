package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;

public class Servico11Ct extends Controle {

	private static final long serialVersionUID = -191425437457515572L;

	public int Permissao() {
		return 912;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {
		String stOperacao = "";
		String a, b;
		String Mensagem = "";
		String stTag = "";
		List lisValores = null;
		List tempList = new ArrayList();
		String idUsuario, hash;
		String dados;
		String stVersao = "1";
		String stAjuda = "1";
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		
		response.setContentType("text/xml; charset=UTF-8");
		
		switch (paginaatual) {
		case 1: //LISTAR ADVOGADOS DA SERVENTIA
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 71 – LISTAR ADVOGADOS DA SERVENTIA (VERSÃO 1)\nResponsável em listar todos os Advogados da serventia do usuário logado.\nURL:\nhttps://projudi.tjgo.jus.br/servico11\nPARÂMETROS:\nPaginaAtual=1(operação)\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nIdAdvogado\nHashAdvogado\nNome\nCpf\nOab\nComplemento\nStatus");
					break;
				}
				stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
			} else {
				if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
					stVersao = request.getParameter("v");
				switch (stVersao) {
					case "1":
					default:
					String[] stAdvogadosServentia = {"IdAdvogado", "HashAdvogado", "Nome", "Cpf", "Oab", "Complemento", "Status"};
					stTag = "Advogado";
					
					tempList = UsuarioSessao.consultarTodosAdvogados("", "", UsuarioSessao.getUsuarioDt().getId_Serventia(), posicaopaginaatual);
					
					if (tempList.size() > 0) {
						lisValores = new ArrayList();
						if (tempList != null) {
							for (int i = 0; i < tempList.size(); i++) {
								UsuarioDt obj = (UsuarioDt) tempList.get(i);
								String[] stTemp = {obj.getId_UsuarioServentia(), UsuarioSessao.getCodigoHash(obj.getId_UsuarioServentia()), Funcoes.substituirCaracteresEspeciaisXML(obj.getNome(), true), obj.getUsuario(), obj.getOabNumero(), obj.getOabComplemento(), obj.getUsuarioServentiaAtivo().equalsIgnoreCase("true")?"ATIVO":"INATIVO"};
								lisValores.add(stTemp);
							}
						}				
						request.setAttribute("Paginacao", qtdPaginasSaida(UsuarioSessao.getQuantidadePaginas()));
						request.setAttribute("AtributosTag", stAdvogadosServentia);
						request.setAttribute("Tag", stTag);
						request.setAttribute("Operacao", "71");
						request.setAttribute("Valores", lisValores);
						stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
					} else {
						request.setAttribute("MensagemErro", "Nenhum Advogado Disponível.");
					}
				}
			}
			break;
			
		case 2: //HABILITAR ADVOGADO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 72 – HABILITAR ADVOGADO (VERSÃO 1)\nResponsável em ativar o Advogado na serventia.\nURL:\nhttps://projudi.tjgo.jus.br/servico11\nPARÂMETROS:\nPaginaAtual=2(operação)\na=IdAdvogado\nb=HashAdvogado\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nAdvogado habilitado com sucesso");
					break;
				}
				stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
			} else {
				if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
					stVersao = request.getParameter("v");
				switch (stVersao) {
				case "1":
				default:
					if (request.getParameter("a") != null && !request.getParameter("a").equals("")) idUsuario = request.getParameter("a");
					else throw new WebServiceException("IdUsuario não encontrado");
					if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hash = request.getParameter("b");
					else throw new WebServiceException("Hash não encontrado");
	
					if (UsuarioSessao.VerificarCodigoHashWebService(idUsuario, hash)) {
						UsuarioDt usuarioDt = new UsuarioDt();
						usuarioDt.setId_UsuarioServentia(idUsuario);
						usuarioDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						usuarioDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
	
						UsuarioSessao.ativarAdvogado(usuarioDt);
						request.setAttribute("Mensagem", "Advogado habilitado com sucesso");
						request.setAttribute("RespostaTipo", "OK");
					} else throw new WebServiceException("Operação Não Autorizada");
	
					request.setAttribute("Operacao", "72");
					stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				}
			}
			break;

		case 3: //DESABILITAR ADVOGADO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 73 – DESABILITAR ADVOGADO (VERSÃO 1)\nResponsável em desativar o Advogado na serventia.\nURL:\nhttps://projudi.tjgo.jus.br/servico11\nPARÂMETROS:\nPaginaAtual=2(operação)\na=IdAdvogado\nb=HashAdvogado\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nAdvogado desabilitado com sucesso");
					break;
				}
				stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
			} else {
				if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
					stVersao = request.getParameter("v");
				switch (stVersao) {
				case "1":
				default:
					if (request.getParameter("a") != null && !request.getParameter("a").equals("")) idUsuario = request.getParameter("a");
					else throw new WebServiceException("IdUsuario não encontrado");
					if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hash = request.getParameter("b");
					else throw new WebServiceException("Hash não encontrado");
	
					if (UsuarioSessao.VerificarCodigoHashWebService(idUsuario, hash)) {
						UsuarioDt usuarioDt = new UsuarioDt();
						usuarioDt.setId_UsuarioServentia(idUsuario);
						usuarioDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						usuarioDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
	
						UsuarioSessao.desativarUsuarioServentia(usuarioDt);
						request.setAttribute("Mensagem", "Advogado desabilitado com sucesso");
						request.setAttribute("RespostaTipo", "OK");
					} else throw new WebServiceException("Operação Não Autorizada");
	
					request.setAttribute("Operacao", "73");
					stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				}
			}
			break;
			
		case 4: //PROCESSOS DISTRIBUÍDOS
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVIÇO 81 – PROCESSOS DISTRIBUÍDOS (VERSÃO 1)\nConsulta os processos distribuídos de 00:00:00 às 23:59:59 da data informada.\nURL:\nhttps://projudi.tjgo.jus.br/servico11\nPARÂMETROS:\nPaginaAtual=4(operação)\nPosicaoPaginaAtual=paginação(começa em 0)\na=Data(dd/mm/aaaa)\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nDados dos Processos");
					break;
				}
				stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
			} else {
				if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
					stVersao = request.getParameter("v");
				switch (stVersao) {
				case "1":
				default:
					a = "";
					dados = "";
					if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
					else throw new WebServiceException("Data não encontrada");
					
					ServentiaNe serventiaNe = new ServentiaNe();
					ServentiaDt serventiaDt = serventiaNe.consultarServentiaCodigo(UsuarioSessao.getUsuarioDt().getServentiaCodigo());
					String codigoComarca = serventiaDt.getComarcaCodigo();
					
					ProcessoNe processoNe = new ProcessoNe();
					dados = processoNe.consultarProcessosDistribuidos(a, codigoComarca, posicaopaginaatual);
					request.setAttribute("Paginacao", qtdPaginasSaida(processoNe.getQuantidadePaginas()));
					request.setAttribute("RespostaTipo", "OK");
					request.setAttribute("Operacao", "81");
					request.setAttribute("Mensagem", dados);
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
	
	private String qtdPaginasSaida(long qtdRegistros) {
		long loTotal = 0;
		if ((qtdRegistros % Configuracao.TamanhoRetornoConsulta) != 0) {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta + 1;
		} else {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta;
		}
		return String.valueOf(loTotal);
	}

}
