package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ne.UsuarioNe;

public class CertidaoExecucaoDistribuidorCt extends Controle {

	/**
     * 
     */
    private static final long serialVersionUID = 6279649551989126928L;

    @Override
	public int Permissao() {
		
		return 0;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		
	}

}
