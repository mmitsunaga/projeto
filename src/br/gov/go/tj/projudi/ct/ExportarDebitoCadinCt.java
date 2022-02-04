package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt;
import br.gov.go.tj.projudi.ne.ExportarDebitoCadinNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

/**
 * Servlet que controla a exportação de um arquivo de débitos para o Cadin
 * @author mmgomes - 18/12/2018 
 */
public class ExportarDebitoCadinCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3125865845077341643L;

	public int Permissao() {
		return ExportarDebitoCadinDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ExportarDebitoCadinDt exportarDebitoCadinDt;
		ExportarDebitoCadinNe exportarDebitoCadinNe;
		String mensagem = "";		
		String stAcao = "/WEB-INF/jsptjgo/ExportarDebitoCadin.jsp";

		request.setAttribute("tempPrograma", "ExportacaoDebitoCadin");
		request.setAttribute("tempRetorno", "ExportacaoDebitoCadin");

		exportarDebitoCadinNe = (ExportarDebitoCadinNe) request.getSession().getAttribute("ExportarDebitoCadinne");
		if (exportarDebitoCadinNe == null) exportarDebitoCadinNe = new ExportarDebitoCadinNe();

		exportarDebitoCadinDt = (ExportarDebitoCadinDt) request.getSession().getAttribute("ExportarDebitoCadindt");
		if (exportarDebitoCadinDt == null)	exportarDebitoCadinDt = new ExportarDebitoCadinDt();

		exportarDebitoCadinDt.setTipoExportacao(request.getParameter("tipoExportacao"));		
		exportarDebitoCadinDt.setNumeroDoLote(request.getParameter("numeroDoLote"));
		exportarDebitoCadinDt.setDataExportacao(request.getParameter("dataExportacao"));
		exportarDebitoCadinDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		exportarDebitoCadinDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Novo:
				exportarDebitoCadinDt.limpar();				
				break;
				
			case Configuracao.Salvar:
				mensagem = exportarDebitoCadinNe.Verificar(exportarDebitoCadinDt);
				if (mensagem.length() == 0) {
					request.setAttribute("MensagemConfirmacao", "Confirmar exportação");				
					request.setAttribute("Mensagem", "Clique para confirmar a exportação do arquivo.");	
				} else {
					request.setAttribute("PaginaAnterior", Configuracao.Novo);
					request.setAttribute("MensagemErro", mensagem);
				}
				break;
				
			case Configuracao.SalvarResultado:
				mensagem = exportarDebitoCadinNe.Verificar(exportarDebitoCadinDt);
				if (mensagem.length() == 0) {
					exportarDebitoCadinDt = exportarDebitoCadinNe.Exportar(exportarDebitoCadinDt);			
					if (exportarDebitoCadinDt.getConteudoArquivo() != null && 
						exportarDebitoCadinDt.getConteudoArquivo().trim().length() > 0) {
						enviarTXT(response, exportarDebitoCadinDt.getConteudoArquivo().getBytes(), exportarDebitoCadinDt.getNomeArquivo());
						return;
					} else if (exportarDebitoCadinDt.getConteudoArquivoInconsistencias() != null && 
							   exportarDebitoCadinDt.getConteudoArquivoInconsistencias().trim().length() > 0) {
						enviarCSVDownload(response, exportarDebitoCadinDt.getConteudoArquivoInconsistencias(), exportarDebitoCadinDt.getNomeArquivoInconsistencias());
						return;
					} else {
						request.setAttribute("MensagemOk", "Não foram localizados registros para exportação.");
					}
				} else {
					request.setAttribute("MensagemErro", mensagem);
				}
				break;
		}		

		request.getSession().setAttribute("ExportarDebitoCadindt", exportarDebitoCadinDt);
		request.getSession().setAttribute("ExportarDebitoCadinne", exportarDebitoCadinNe);		
	
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
