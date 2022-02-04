package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * Controlador responsável em receber o identificador de um arquivo e como resposta, o arquivo PDF para download
 * @author mmitsunaga
 *
 */
public class BuscaArquivoPublicoCt extends Controle {

	private static final long serialVersionUID = 1L;

	@Override
	public int Permissao() {
		return 871;
	}
	
	protected String getId_GrupoPublico() {
		return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		String stAcao = "";
		
		String id = request.getParameter("Id_MovimentacaoArquivo") != null ? request.getParameter("Id_MovimentacaoArquivo") : "";
	    
	    if (ValidacaoUtil.isNaoVazio(id)){
	    	
	    	if (isArquivoPublico(id)){
	    		
	    		ProcessoNe Processone = new ProcessoNe();
	    		String stIdArquivo = Processone.consultarIdArquivo(id); 
	    		
	    		PendenciaNe pendenciaNe = new PendenciaNe();
    			byte[] byTemp = pendenciaNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), stIdArquivo);
    			if (byTemp!=null){
    				try{
    					response.setStatus(HttpStatus.SC_OK);
    					response.setHeader("Content-Disposition", "inline; filename=" +System.currentTimeMillis()+".pdf");
    					response.setContentType("application/pdf");
    					response.getOutputStream().write(byTemp);
    					response.flushBuffer();											
    				} catch(Exception e) {}
    				return;
    			} else {
    				response.setStatus(HttpStatus.SC_BAD_REQUEST);
    				request.setAttribute("MensagemErro", "Erro, arquivo não disponível ou bloqueado.");
    				RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
    				dis.include(request, response);							
    			}
	    			    			    		
	    	} else {
	    		response.setStatus(HttpStatus.SC_BAD_REQUEST);
	    		stAcao = "/WEB-INF/jsptjgo/Erro.jsp";
	    		request.setAttribute("Mensagem", "O arquivo informado não é público nesse momento.");	
	    	}
	    	
	    } else {
	    	response.setStatus(HttpStatus.SC_BAD_REQUEST);
	    	stAcao = "/WEB-INF/jsptjgo/Erro.jsp";
    		request.setAttribute("Mensagem", "Não foi informado o identificador do arquivo.");	    	
	    }
	    	    
	    RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}
	
	public boolean isArquivoPublico(String id) throws Exception {
		ArquivoNe arquivoNe = new ArquivoNe();
		return arquivoNe.isArquivoPublicoEAcessoNormalPublico(id);
	}
	
}
