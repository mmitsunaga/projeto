package br.gov.go.tj.projudi.ct.publicos;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;

public class CalculadoraTemposDatasPublicaCt extends Controle {

	private static final long serialVersionUID = -2214329886867822214L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
		response.setContentType("text/html");
        response.setCharacterEncoding("iso-8859-1");
		
		String stAcao="/WEB-INF/jsptjgo/CalculadoraTemposDatas.jsp";
		
		request.setAttribute("mostraCabecalho", "S");

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	@Override
	public int Permissao() {
		return 872;
	}
	
	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
    protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
}