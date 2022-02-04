package br.gov.go.tj.projudi.ct.publicos;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioImplantacaoDt;
import br.gov.go.tj.projudi.ne.RelatorioImplantacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet responsável pela Consulta Pública de Juizados com PROJUDI implantado.
 * Essa servlet estende diretamente de HttpServlet e não de Controle, pois a
 * população terá acesso a essa consulta
 * 
 * @author asrocha
 */
public class ListaJuizadosCt extends Controle {

	private static final long serialVersionUID = 6253194158085370468L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
		RelatorioImplantacaoDt relatorioImplantacaoDt;
		RelatorioImplantacaoNe relatorioImplantacaoNe;

		byte[] byTemp = null;
		String stAcao = "";
		int passoBusca = 2;

		relatorioImplantacaoNe = (RelatorioImplantacaoNe) request.getSession().getAttribute("RelatorioImplantacaone");
		if (relatorioImplantacaoNe == null) relatorioImplantacaoNe = new RelatorioImplantacaoNe();

		relatorioImplantacaoDt = (RelatorioImplantacaoDt) request.getSession().getAttribute("RelatorioImplantacaodt");
		if (relatorioImplantacaoDt == null) relatorioImplantacaoDt = new RelatorioImplantacaoDt();

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", "RelatorioImplantacao");
		request.setAttribute("tempPrograma", "Relatório de Serventias Implantadas");

		request.getSession().setAttribute("RelatorioImplantacaodt", relatorioImplantacaoDt);
		request.getSession().setAttribute("Relatorioimplantacaone", relatorioImplantacaoNe);
		request.getSession().setAttribute("CodigoCaptcha", "1");

		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");	

		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");		

		switch (passoBusca) {
			case 2: // Redireciona para tela de catpcha				
			case 3:
				byTemp = relatorioImplantacaoNe.relImplantacaoServentiasPublico(ProjudiPropriedades.getInstance().getCaminhoAplicacao());															
				if (byTemp != null) {
					enviarPDF( response, byTemp, "ServentiasImplantadas");										
					return;	
				} 
			break;
		}

		request.setAttribute("PassoBusca", passoBusca);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);	
	}

	@Override
	public int Permissao() {
		return 869;
	}
	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
    protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
	
}