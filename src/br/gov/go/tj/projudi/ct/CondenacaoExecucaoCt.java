package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ne.ProcessoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class CondenacaoExecucaoCt extends CondenacaoExecucaoCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = -5646981369936900788L;

//
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		switch (paginaatual) {
		  case Configuracao.LocalizarDWR: 
            
       	    int inFluxo = Funcoes.StringToInt( request.getParameter("fluxo").toString());
            switch (inFluxo) {
               case 1:
               	getListaCondenacoesProcessoExecucao(request, response, tempNomeBusca, PosicaoPaginaAtual);
                   break;
            }		                	               	            
		}
	}
			
	public void getListaCondenacoesProcessoExecucao(HttpServletRequest request, HttpServletResponse response, String idProcessoExecucao, String posicao) throws Exception {
		String stTemp = ""; 
		ProcessoExecucaoNe obNegocio;
		obNegocio =  (ProcessoExecucaoNe)request.getSession().getAttribute("ProcessoExecucaone");
		if (obNegocio == null )  obNegocio = new ProcessoExecucaoNe();
		stTemp = obNegocio.listarCondenacoesDaAcaoPenalJSON(idProcessoExecucao, posicao);
		enviarJSON(response, stTemp);
	}
}
