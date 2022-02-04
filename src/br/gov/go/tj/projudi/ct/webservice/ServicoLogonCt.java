package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;

public class ServicoLogonCt extends Controle  {

	private static final long serialVersionUID = 6065041215829725952L;

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {
		Logger.getLogger("aplicacao.logon");		
		UsuarioDt Usuariodt = null;
		List tempList = null;
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		String stOperacao = "";
		String stVersao = "1";
		String stAjuda = "1";

		response.setContentType("text/xml; charset=UTF-8");

		UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
		if (request.getParameter("a") != null) stOperacao = request.getParameter("a");
		
		switch (stOperacao) {
		case "1": //LOGON
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 1 � LOGON (VERS�O 1)\nEfetua a entrada do usu�rio no sistema\nURL:\nhttps://projudi.tjgo.jus.br/servico01\nPAR�METROS:\na=1\nb=usu�rio\nc=senha\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nLogon realizado com sucesso");
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
					String stUsuario, stSenha;					
					if (request.getParameter("b") != null && !request.getParameter("b").equalsIgnoreCase("")) stUsuario = request.getParameter("b");
					else throw new WebServiceException("Usu�rio n�o encontrado");
					if (request.getParameter("c") != null && !request.getParameter("c").equalsIgnoreCase("")) stSenha = request.getParameter("c");
					else throw new WebServiceException("Senha n�o encontrada");
					
					UsuarioSessao = new UsuarioNe();
					if (UsuarioSessao.logarUsuarioSenha(stUsuario, stSenha)) {
						if (request.getSession(false) != null) request.getSession(false).invalidate();
						request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());
						UsuarioSessao.getUsuarioDt().setDataEntrada(Funcoes.DataHora(request.getSession().getCreationTime()));
						UsuarioSessao.getUsuarioDt().setIpComputadorLog(Controle.getIpCliente(request));
						
						// j� fica na sess�o a lista das serventias grupos disponiveis
						UsuarioSessao.consultarServentiasGrupos();
						
						//GerenciaUsuarios.getInstancia().addUsuario(UsuarioSessao.getId_Usuario(), request.getSession());
						UsuarioSessao.setPermissao(UsuarioDt.CodigoPermissao);					
						UsuarioSessao.setPermissao(2166);
						UsuarioSessao.setPermissao(2167);
						UsuarioSessao.setPermissao(2169);
						UsuarioSessao.setPermissao(2160);
						request.getSession().setAttribute("UsuarioSessao", UsuarioSessao);
						request.setAttribute("Mensagem", "Logon Realizado com Sucesso");
						request.setAttribute("RespostaTipo", "OK");
					} else {
						request.setAttribute("Mensagem", "Usu�rio, senha ou opera��o inv�lida");
						request.setAttribute("RespostaTipo", "ERRO");
					}
					request.setAttribute("Operacao", "1");
				}
			}
			break;
			
		case "2": //LOGOFF
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 2 � LOGOFF (VERS�O 1)\nEfetua a sa�da do usu�rio no sistema\nURL:\nhttps://projudi.tjgo.jus.br/servico01\nPAR�METROS:\na=2\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nLogoff realizado com sucesso");
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
					sairSistema(request, response);
					request.setAttribute("Mensagem", "Logoff realizado com sucesso");
					request.setAttribute("RespostaTipo", "OK");
					request.setAttribute("Operacao", "2");
				}
			}
			break;
			
		case "3": //LISTAR SERVENTIA/GRUPO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 3 � LISTAR SERVENTIA/GRUPO (VERS�O 1)\nLista as serventias/grupos no qual o usu�rio logado est� vinculado.\nURL:\nhttps://projudi.tjgo.jus.br/servico01\nPAR�METROS:\na=3\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\nRETORNO:\nIdServentia\nDescricaoServentia\nServentiaCodigo\nGrupoCodigo\nDescricaoGrupo\nIdServentiaCargo");
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
				if (UsuarioSessao == null) throw new WebServiceException("Sem usu�rio na sess�o");
				
				String[] stDescricaoLista = {"IdServentia", "DescricaoServentia", "ServentiaCodigo", "GrupoCodigo", "DescricaoGrupo", "IdServentiaCargo" };
				String stTag = "ServentiaGrupo";
				
				tempList = UsuarioSessao.consultarServentiasGrupos();
				List lisValores = new ArrayList();
				for (int i = 0; i < tempList.size(); i++) {
					UsuarioDt tempDt = (UsuarioDt) tempList.get(i);
					String[] stTemp = {tempDt.getId_Serventia(), tempDt.getServentia(), tempDt.getServentiaCodigo(), tempDt.getGrupoCodigo(), tempDt.getGrupo(), tempDt.getId_ServentiaCargo() };
					lisValores.add(stTemp);
				}
				
				request.setAttribute("AtributosTag", stDescricaoLista);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", "3");
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				}
			}
			break;
			
		case "4": //DEFINIR SERVENTIA/GRUPO
			if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
				stAjuda = request.getParameter("h");
				switch (stAjuda) {
				case "1":
				default:
					request.setAttribute("Ajuda", "SERVI�O 4 � DEFINIR SERVENTIA/GRUPO (VERS�O 1)\nDefine a serventia na qual o usu�rio quer entrar e com qual perfil ele deseja trabalhar.\nURL:\nhttps://projudi.tjgo.jus.br/servico01\nPAR�METROS:\nh=n�mero vers�o(ajuda)\nv=n�mero vers�o(vers�o)\na=4\nb=IdServentia\nc=GrupoCodigo\nd=IdServentiaCargo\nRETORNO:\nServentia e grupo definidos com sucesso");
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
					String id_serventia, grupoCodigo, id_serventiaCargo = "";
	
					if (request.getParameter("b") != null && !request.getParameter("b").equalsIgnoreCase("")) id_serventia = request.getParameter("b");
					else throw new WebServiceException("IdServentia n�o encontrado");
					if (request.getParameter("c") != null && !request.getParameter("c").equalsIgnoreCase("")) grupoCodigo = request.getParameter("c");
					else throw new WebServiceException("GrupoCodigo n�o encontrado");
					if (request.getParameter("d") != null && !request.getParameter("d").equalsIgnoreCase("")) id_serventiaCargo = request.getParameter("d");
	
					tempList = (List) UsuarioSessao.getListaServentiasGruposUsuario();
	
					// Percorrendo lista de serventias/grupos para encontrar objeto correspondente a serventia/grupo escolhido
					for (int i = 0; i < tempList.size(); i++) {
						Usuariodt = ((UsuarioDt) tempList.get(i));
						if ((Usuariodt.getGrupoCodigo().equalsIgnoreCase(grupoCodigo)) && (Usuariodt.getId_Serventia().equalsIgnoreCase(id_serventia)) && (Usuariodt.getId_ServentiaCargo().equalsIgnoreCase(id_serventiaCargo))) {
							break;
						}
						Usuariodt = null;
					}
	
					if (Usuariodt == null) {
						request.setAttribute("Mensagem", "Serventia/Grupo n�o escolhidos.");
						request.setAttribute("RespostaTipo", "ERRO");
						request.setAttribute("Operacao", "4");
						break;
					}
	
					Usuariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
					UsuarioSessao.setUsuarioDt(Usuariodt);
	
					String Menu = UsuarioSessao.getMenu();
					request.setAttribute("Menu", Menu);
					request.getSession().setAttribute("UsuarioSessao", UsuarioSessao);
					request.getSession().setAttribute("UsuarioSessaoDt", UsuarioSessao.getUsuarioDt());
					request.setAttribute("Mensagem", "Seventia/Grupo definidos com sucesso");
					request.setAttribute("RespostaTipo", "OK");
					request.setAttribute("Operacao", "4");
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

	public int Permissao(){
		return UsuarioDt.CodigoPermissao;
	}

	//este m�todo deve ser sobrescrito pelos ct_publicos retornando o id do grupo publico
    protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
	
		
	
}