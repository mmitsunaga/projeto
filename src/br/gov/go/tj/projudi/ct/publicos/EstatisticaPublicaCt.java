package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaPublicaDt;
import br.gov.go.tj.projudi.ne.EstatisticaPublicaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaPublicaCt extends Controle {

	private static final long serialVersionUID = 7830879038531172815L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {

		EstatisticaPublicaDt estatisticaPublicaDt;
		EstatisticaPublicaNe estatisticaPublicaNe;

		String stAcao = "";
		int passoBusca = 2;

		estatisticaPublicaNe = (EstatisticaPublicaNe) request.getSession().getAttribute("EstatisticaPublicane");
		if (estatisticaPublicaNe == null)
			estatisticaPublicaNe = new EstatisticaPublicaNe();

		estatisticaPublicaDt = (EstatisticaPublicaDt) request.getSession().getAttribute("EstatisticaPublicadt");
		if (estatisticaPublicaDt == null)
			estatisticaPublicaDt = new EstatisticaPublicaDt();

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", "EstatisticaPublica");
		request.setAttribute("tempPrograma", "Estatísticas Públicas do Projudi");

		request.getSession().setAttribute("EstatisticaPublicadt", estatisticaPublicaDt);
		request.getSession().setAttribute("EstatisticaPublicane", estatisticaPublicaNe);
		request.getSession().setAttribute("CodigoCaptcha", "1");

		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");	

		if (request.getParameter("PassoBusca") != null){
			passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		} else {
			passoBusca = 0;
		}
		
		if (request.getParameter("EstatisticaPublicaTipoConsulta") != null){
			request.getSession().setAttribute("EstatisticaPublicaTipoConsulta", request.getParameter("EstatisticaPublicaTipoConsulta"));
		}
		
		if(request.getParameter("Id_Comarca") != null){
			request.getSession().setAttribute("Id_Comarca", request.getParameter("Id_Comarca"));
		}

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (passoBusca) {
		case 0: 
			stAcao = "/index.jsp";
			break;
			
		case 2: // Redireciona para tela de catpcha
		case 3:
			if (checkRecaptcha(request)) {
				estatisticaPublicaDt.limpar();
				int tipoConsulta = Funcoes.StringToInt(request.getSession().getAttribute("EstatisticaPublicaTipoConsulta").toString());
				String idComarca = null;
				if(request.getSession().getAttribute("Id_Comarca") != null && !request.getSession().getAttribute("Id_Comarca").equals("")) {
					idComarca = request.getSession().getAttribute("Id_Comarca").toString();
					request.getSession().removeAttribute("Id_Comarca");
				}
				switch (tipoConsulta) {
				case 1: //Estatística de processos Ativos, Arquivados e Recebidos no Estado
					//Se idComarca tiver sido informado, irá consultar por uma Serventia
					//Senão, irá consultar por Comarcas
					if(idComarca == null || idComarca.equalsIgnoreCase("")){
						estatisticaPublicaDt = estatisticaPublicaNe.consultarEstatisticasPublicasProjudi();
						stAcao = "WEB-INF/jsptjgo/EstatisticaPublicaProcessoComarca.jsp";
					} else {
						estatisticaPublicaDt = estatisticaPublicaNe.consultarEstatisticaProcessosServentiasComarca(idComarca);
						stAcao = "WEB-INF/jsptjgo/EstatisticaPublicaProcessoServentia.jsp";
					}
					break;
					
				case 2: //Estatística de Tipos de Processos no Estado
					
					break;
				
				case 3: //Quantitativo de Tipo de Serventias no Estado
					request.setAttribute("listaServentiasEstado", estatisticaPublicaNe.consultarEstatisticaServentiasEstado());
					stAcao = "WEB-INF/jsptjgo/EstatisticaPublicaServentiasEstado.jsp";
					break;
				}
				
				
				request.getSession().setAttribute("EstatisticaPublicadt", estatisticaPublicaDt);
			} else {				
				stAcao = "/index.jsp";
				passoBusca = 0;
			}
			break;
		}

		request.setAttribute("PassoBusca", passoBusca);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	}

	@Override
	public int Permissao() {
		return 868;
	}

	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}

}