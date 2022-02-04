package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.IndexadorNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class PendenciaPublicaCt extends Controle {

    private static final long serialVersionUID = -7536528235760791892L;
    public static final int CodigoPermissao = 397;
	
	public int Permissao() {
		return CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {
		
		String stId = "";
		String stIdArquivo = "";
		String textoPendencia = "";
		String dataInicial = "";
		String dataFinal = "";
		String posicaoPagina= "";
		int paginaAnterior = -1;
		PendenciaDt pendenciaDt;
		ServentiaDt serventiaDt;
		PendenciaNe pendenciaNe;
		String stNomeBusca1 = "";
		int consultaTipo = 0;
		String stAcao = "/WEB-INF/jsptjgo/Branco.html";
						
		response.setContentType("text/html");
        response.setCharacterEncoding("iso-8859-1");
		
        if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("ConsultaTipo") != null) consultaTipo = Funcoes.StringToInt(request.getParameter("ConsultaTipo"));

		pendenciaNe = (PendenciaNe) request.getSession().getAttribute("BuscaPublicacaone");
		if (pendenciaNe == null) pendenciaNe = new PendenciaNe();

		pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");
		if (pendenciaDt == null) pendenciaDt = new PendenciaDt();
		
		if (request.getSession().getAttribute("Serventiadt") != null)
			serventiaDt = (ServentiaDt)request.getSession().getAttribute("Serventiadt");
		else
			serventiaDt = new ServentiaDt();
		
		request.setAttribute("tempCabecalho", "nao");		
		
		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equalsIgnoreCase("null")){
				serventiaDt.setId("");
				serventiaDt.setServentia("");
			} else {
				serventiaDt.setId( request.getParameter("Id_Serventia") );
				serventiaDt.setServentia(request.getParameter("Serventia"));
			}
		}
		
		if (request.getParameter("textoPublicacao") != null ) {
			textoPendencia = (String) request.getParameter("textoPublicacao");
			request.getSession().setAttribute("textoPublicacao", textoPendencia);
			
		} else if(request.getSession().getAttribute("textoPublicacao") == null)
			request.getSession().setAttribute("textoPublicacao", "");
		
		if (request.getParameter("dataInicial") != null ) {
			dataInicial = (String) request.getParameter("dataInicial");
		} else if(request.getSession().getAttribute("dataInicial") != null){
			dataInicial = (String) request.getSession().getAttribute("dataInicial");
		} 
		
		if (request.getParameter("dataFinal") != null ) {
			dataFinal = (String) request.getParameter("dataFinal");
		} else if(request.getSession().getAttribute("dataFinal") != null){
			dataFinal = (String) request.getSession().getAttribute("dataFinal");
		} 
		
		if (request.getParameter("PaginaAnterior") != null)
			paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		
		if (request.getParameter("PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		
		request.getSession().setAttribute("dataInicial", dataInicial);
		request.getSession().setAttribute("dataFinal", dataFinal);
		request.setAttribute("Id_Serventia", serventiaDt.getId() );
		request.setAttribute("Serventia", serventiaDt.getServentia() );
		request.setAttribute("tempPrograma", "Pendencia Publicação");
		request.setAttribute("tempBuscaId_Pendencia", "Id_Pendencia");
		request.setAttribute("tempRetorno", "PendenciaPublicacao");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
 		switch (paginaatual) {
			case ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar: //Localizar Serventia
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "PendenciaPublicacao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("tempFluxo1", "1");
					if(paginaAnterior == 2)
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					else
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					
					break;
				}else{
					String stTemp = "";						
						stTemp = pendenciaNe.consultarDescricaoServentiaPublicacaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			
			case Configuracao.Localizar://Consulta Textual de Publicações
				if (request.getParameter("Passo") == null) {
					String[] lisDescricao = {"Serventia", "Nome", "Data de Inserção", "Abrir"};
					stAcao = "/WEB-INF/jsptjgo/ConsultaPublicacaoTextual.jsp";
					request.setAttribute("tempRetorno", "PendenciaPublicacao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else {
					
					if (!textoPendencia.equals("") ){
						String stTemp = "";
						if(consultaTipo == 1)								
							stTemp =  pendenciaNe.consultarTextoPublicacaoPublicaDuplaPalvrasJSON(dataInicial, dataFinal, serventiaDt.getId(), textoPendencia, posicaoPagina, UsuarioSessao);
						else
							
						stTemp =  pendenciaNe.consultarTextoPublicacaoQualquerPalavraJSON(dataInicial, dataFinal, serventiaDt.getId(), textoPendencia, posicaoPagina, UsuarioSessao);							
						
						enviarJSON(response, stTemp);
						
					} else if (request.getParameter("menu") == null){
						request.setAttribute("MensagemErro", "Digite o texto para efetuar a consulta.");	
					}
					
					break;
				}
							
			case Configuracao.Editar:
				String fluxo = "";
				if(request.getParameter("fluxo") != null)
					fluxo = request.getParameter("fluxo");
				if(request.getParameter("tempFluxo1") != null)
					fluxo = request.getParameter("tempFluxo1");
				if(fluxo.equalsIgnoreCase("1"))
					stAcao = "/WEB-INF/jsptjgo/PendenciaPublicacao.jsp";
				else
					stAcao = "/WEB-INF/jsptjgo/ValidarDocumento.jsp";
				request.setAttribute("PaginaAtual", paginaatual);
				break;
			
			case Configuracao.Curinga6: 
				
				 stId = request.getParameter("Id_Pendencia");
				 pendenciaDt = pendenciaNe.consultarFinalizadaId(stId);
				 pendenciaDt.setListaArquivos(pendenciaNe.consultarArquivosPublicacoes(stId));
				 stAcao = "/WEB-INF/jsptjgo/PendenciaPublicacaoDetalhada.jsp";				
				
				break;
				
			case Configuracao.Curinga7: //Validar publicação
				String codValidacao = request.getParameter("codPublicacao");
				String valor = Cifrar.decodificar(codValidacao, PendenciaArquivoDt.CodigoPermissao);
//				//para manter compatibilidade com a forma antiga de validar os documentos (31/01/2018)
//				if (Funcoes.StringToInt(valor) == -1) {
//					valor = Cifrar.decodificarId_Certidao(codValidacao);
//				}
				
				if(Funcoes.StringToInt(valor) == -1){
					stAcao = "/WEB-INF/jsptjgo/Erro.jsp";
					request.setAttribute("Mensagem",  "Código inválido, não foi possível validar a publicação. Se o documento foi gerado antes de 08/02/2018, favor gerar um novo documento no formato PDF e utilizar o novo código que será informado.");
					
				}else {
					if (valor != null && valor.length() > 0) {
												
						byte[] byTemp = pendenciaNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , valor);
						if (byTemp==null){
							request.setAttribute("Mensagem", "Erro, arquivo não disponível ou bloqueado.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
							return;
						}
						
						this.enviarPDF(response, byTemp,"pdfPublicacao");
													
						return;					
					}
				}
				break;
			
			case Configuracao.Curinga8: //Abrir arquivo da publicação
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stIdArquivo = request.getParameter("Id_Arquivo");
				if (stIdArquivo != null) {
					if (!stIdArquivo.equalsIgnoreCase("")) {
						
						response.setContentType("application/pdf");
						byte[] byTemp = pendenciaNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,stIdArquivo);
						if (byTemp==null){
							request.setAttribute("Mensagem", "Erro, arquivo não disponível ou bloqueado.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
							return;
						}
						
						this.enviarPDF(response, byTemp,"pdfPublicacao");
																				
						return;

					}
				} else 
					stAcao = "/WEB-INF/jsptjgo/PendenciaPublicacao.jsp";
				break;
				
			case Configuracao.Curinga9: // Fazer indexação do arquivo															
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stIdArquivo = request.getParameter("Id_Arquivo");
				if (stIdArquivo != null){
					if (!stIdArquivo.equalsIgnoreCase("")){						
						boolean ok = new IndexadorNe().indexar(stIdArquivo);
						if (ok) {
							request.setAttribute("MensagemOk", "Arquivo indexado com sucesso");
						} else {
							request.setAttribute("MensagemErro", "Arquivo não foi indexado.");
						}
					}
				}
				stId = request.getParameter("Id_Pendencia");
				pendenciaDt = pendenciaNe.consultarFinalizadaId(stId);
				pendenciaDt.setListaArquivos(pendenciaNe.consultarArquivosPublicacoes(stId));
				stAcao = "/WEB-INF/jsptjgo/PendenciaPublicacaoDetalhada.jsp";
				
				break;	
				
				
			case Configuracao.Cancelar: // Faz o bloqueio do arquivo															
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				
				stIdArquivo = request.getParameter("Id_Arquivo");
				stId 		= request.getParameter("Id_Pendencia");
				
				pendenciaDt = pendenciaNe.consultarFinalizadaId(stId);
				
				if (stIdArquivo != null){
					if (!stIdArquivo.equalsIgnoreCase("")){						
						boolean ok = new ArquivoNe().bloquearArquivoPublicacao(stIdArquivo, pendenciaDt.getId_ServentiaFinalizador(), UsuarioSessao.getId_Serventia());
						if (ok) {
							request.setAttribute("MensagemOk", "Arquivo bloqueado com sucesso");
						} else {
							request.setAttribute("MensagemErro", "Arquivo não foi bloqueado. Você não tem permissão para bloquear o arquivo ou o mesmo já está bloqueado.");
						}
					}
				}
				
				pendenciaDt.setListaArquivos(pendenciaNe.consultarArquivosPublicacoes(stId));
				stAcao = "/WEB-INF/jsptjgo/PendenciaPublicacaoDetalhada.jsp";
				
				break;	
												
			default:
					
		}
		
		request.getSession().setAttribute("Pendenciadt", pendenciaDt);
		request.getSession().setAttribute("Serventiadt", serventiaDt);
		request.getSession().setAttribute("BuscaPublicacaone", pendenciaNe);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

}
