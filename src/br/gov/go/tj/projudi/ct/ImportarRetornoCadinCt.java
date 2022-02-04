package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ImportarRetornoCadinDt;
import br.gov.go.tj.projudi.ne.ImportarRetornoCadinNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

/**
 * Servlet que controla a importação de um arquivo de retorno do Cadin
 * @author mmgomes - 07/12/2018 
 */
public class ImportarRetornoCadinCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8610683379237845548L;

	public int Permissao() {
		return ImportarRetornoCadinDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ImportarRetornoCadinDt importarRetornoCadinDt;
		ImportarRetornoCadinNe importarRetornoCadinNe;
		String mensagem = "";		
		String stAcao = "/WEB-INF/jsptjgo/ImportarRetornoCadin.jsp";

		request.setAttribute("tempPrograma", "ImportarRetornoCadin");
		request.setAttribute("tempRetorno", "ImportarRetornoCadin");

		importarRetornoCadinNe = (ImportarRetornoCadinNe) request.getSession().getAttribute("ImportarRetornoCadinne");
		if (importarRetornoCadinNe == null) importarRetornoCadinNe = new ImportarRetornoCadinNe();

		importarRetornoCadinDt = (ImportarRetornoCadinDt) request.getSession().getAttribute("ImportarRetornoCadindt");
		if (importarRetornoCadinDt == null)	importarRetornoCadinDt = new ImportarRetornoCadinDt();

		importarRetornoCadinDt.setNomeArquivo(super.getNomeArquivo(request));
		importarRetornoCadinDt.setConteudoArquivo(super.getConteudoArquivo(request));
		importarRetornoCadinDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		importarRetornoCadinDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Novo:
				importarRetornoCadinDt.limpar();				
				break;
				
			case Configuracao.Salvar:
				mensagem = importarRetornoCadinNe.Verificar(importarRetornoCadinDt);
				if (mensagem.length() == 0) {
					request.setAttribute("MensagemConfirmacao", "Confirmar importação");				
					request.setAttribute("Mensagem", "Clique para confirmar a importação do arquivo.");	
				} else {
					request.setAttribute("PaginaAnterior", Configuracao.Novo);
					request.setAttribute("MensagemErro", mensagem);
				}
				break;
				
			case Configuracao.SalvarResultado:
				mensagem = importarRetornoCadinNe.Verificar(importarRetornoCadinDt);
				if (mensagem.length() == 0) {
					importarRetornoCadinDt = importarRetornoCadinNe.Importar(importarRetornoCadinDt);			
					request.setAttribute("MensagemOk", "Importação realizada com sucesso.");
					if (importarRetornoCadinDt.getConteudoArquivoRetorno() != null && 
						importarRetornoCadinDt.getConteudoArquivoRetorno().trim().length() > 0) {
						enviarCSVDownload(response, importarRetornoCadinDt.getConteudoArquivoRetorno(), importarRetornoCadinDt.getNomeArquivo().replace(".csv", "") + "_Retorno");
						return;
					}										
				} else {
					request.setAttribute("MensagemErro", mensagem);
				}
				break;
		}		

		request.getSession().setAttribute("ImportarRetornoCadindt", importarRetornoCadinDt);
		request.getSession().setAttribute("ImportarRetornoCadinne", importarRetornoCadinNe);		
	
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
