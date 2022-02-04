package br.gov.go.tj.projudi.ct;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.ImportarExecpenNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;


public class ImportarExecpenCt extends Controle {

	private static final long serialVersionUID = -8442289100368907503L;
	
	public static int totalBarraProgresso = 0;
	public static int valorAtualBarraProgresso = 0;
	public static String execucaoBarraProgresso = ""; 

    public int Permissao() {
    	return 662; //permissão do "Importar Execpen"
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, 
			String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		ImportarExecpenNe importarExecpenNe;
		int passoEditar = 0;
		String stAcao = "/WEB-INF/jsptjgo/ImportarExecpen.jsp";
		String mensagem = "";
		String stNomeBusca1 = "";
		String comarca = "";
		String idComarca = "";
		String areaDistribuicao = "";
		String idAreaDistribuicao = "";
		String diretorioArquivoLog = "c:\\exp_int\\";
		String byTemp = "";
		String conteudoArquivoProcesso = "";
		String conteudoArquivoEvento = "";
		
		request.setAttribute("TituloPagina", "Importação dos Dados do Execpen");
		request.setAttribute("tempPrograma", "ImportarExecpen");
		request.setAttribute("tempRetorno", "ImportarExecpen");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");
		
		if (request.getParameter("nomeArquivo")!= null )
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");
		
		if (request.getParameter("DiretorioArquivo") != null && !request.getParameter("DiretorioArquivo").toString().equals("null")) 
			diretorioArquivoLog = request.getParameter("DiretorioArquivo");
		if (request.getAttribute("DiretorioArquivo") != null && !request.getAttribute("DiretorioArquivo").toString().equals("null")) 
			diretorioArquivoLog = (String)request.getAttribute("DiretorioArquivo");
		
		importarExecpenNe = (ImportarExecpenNe) request.getSession().getAttribute("importarExecpenNe");
		if (importarExecpenNe == null)	importarExecpenNe = new ImportarExecpenNe();

		if (request.getAttribute("PassoEditar") != null) passoEditar = Funcoes.StringToInt((String) request.getAttribute("PassoEditar"));
		else if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		
		if (request.getParameter("Comarca") != null && !request.getParameter("Comarca").toString().equals("null"))
			comarca = request.getParameter("Comarca");
		else if (request.getAttribute("Comarca") != null && !request.getAttribute("Comarca").toString().equals("null"))
			comarca = (String)request.getAttribute("Comarca");
		
		if (request.getParameter("Id_Comarca") != null && !request.getParameter("Id_Comarca").toString().equals("null"))
			idComarca = request.getParameter("Id_Comarca");
		if (request.getAttribute("Id_Comarca") != null && !request.getAttribute("Id_Comarca").toString().equals("null"))
			idComarca = (String)request.getAttribute("Id_Comarca");
		
		if (request.getParameter("AreaDistribuicao") != null && !request.getParameter("AreaDistribuicao").toString().equals("null"))
			areaDistribuicao = request.getParameter("AreaDistribuicao");
		if (request.getAttribute("AreaDistribuicao") != null && !request.getAttribute("AreaDistribuicao").toString().equals("null"))
			areaDistribuicao = (String)request.getAttribute("AreaDistribuicao");
		
		if (request.getParameter("Id_AreaDistribuicao") != null && !request.getParameter("Id_AreaDistribuicao").toString().equals("null"))
			idAreaDistribuicao = request.getParameter("Id_AreaDistribuicao");
		if (request.getAttribute("Id_AreaDistribuicao") != null && !request.getAttribute("Id_AreaDistribuicao").toString().equals("null"))
			idAreaDistribuicao = (String)request.getAttribute("Id_AreaDistribuicao");
		
		if (request.getParameter("conteudoArquivoProcesso") != null && !request.getParameter("conteudoArquivoProcesso").toString().equals("null"))
			conteudoArquivoProcesso = request.getParameter("conteudoArquivoProcesso");
		if (request.getAttribute("conteudoArquivoProcesso") != null && !request.getAttribute("conteudoArquivoProcesso").toString().equals("null"))
			conteudoArquivoProcesso = (String)request.getAttribute("conteudoArquivoProcesso");
		
		if (request.getParameter("conteudoArquivoEvento") != null && !request.getParameter("conteudoArquivoEvento").toString().equals("null"))
			conteudoArquivoEvento= request.getParameter("conteudoArquivoEvento");
		if (request.getAttribute("conteudoArquivoEvento") != null && !request.getAttribute("conteudoArquivoEvento").toString().equals("null"))
			conteudoArquivoEvento = (String)request.getAttribute("conteudoArquivoEvento");
		
		switch (paginaatual) {
			case Configuracao.Novo:		
				mensagem = "";
				comarca = "";
				idComarca = "";
				areaDistribuicao = "";
				idAreaDistribuicao = "";
				diretorioArquivoLog = "c:\\exp_int\\";
				byTemp = null;
				conteudoArquivoProcesso = "";
				conteudoArquivoEvento = "";
				break;
				
			case Configuracao.Localizar:
				
				ImportarExecpenCt.valorAtualBarraProgresso = 0;
				ImportarExecpenCt.totalBarraProgresso = 0;
				if (conteudoArquivoProcesso.length() > 0){
					mensagem = importarExecpenNe.importarProcessos(diretorioArquivoLog, conteudoArquivoProcesso, UsuarioSessao, idComarca, comarca, idAreaDistribuicao);
				}
				if (conteudoArquivoEvento.length() > 0){
					mensagem += importarExecpenNe.importarEventos(diretorioArquivoLog, conteudoArquivoEvento, UsuarioSessao, comarca);
				}
				if (mensagem.length() > 0) request.setAttribute("MensagemOk", mensagem);
				break;
				
			// Consulta as Comarcas disponíveis
	        case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):	        	
	        	if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","ImportarExecpen");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = importarExecpenNe.consultarDescricaoComarcaJSON(stNomeBusca1, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}                
	            break;
	            
	            // Consulta as áreas de distribuição disponíveis para a comarca escolhida
	        case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
	                	if (request.getParameter("Passo")==null){
	    					String[] lisNomeBusca = {"Digite a descrição:"};
	    					String[] lisDescricao = {"Área de Distribuição"};
	    					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	    					request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
	    					request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
	    					request.setAttribute("tempBuscaPrograma", "Áreas de Distribuição");

	                        request.setAttribute("Id_Comarca", idComarca);
	                        request.setAttribute("Comarca", comarca);
	                        
	    					request.setAttribute("tempRetorno", "ImportarExecpen");
	    					
	                        request.setAttribute("tempDescricaoId", "Id");
	                                                
	                        request.setAttribute("tempFluxo1", idComarca);
	                        request.setAttribute("tempFluxo2", comarca);
	                        
	    					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
	    					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
	    					request.setAttribute("PosicaoPaginaAtual", "0");
	    					request.setAttribute("QuantidadePaginas", "0");
	    					request.setAttribute("lisNomeBusca", lisNomeBusca);
	    					request.setAttribute("lisDescricao", lisDescricao);
	    				}else{
	    					String stTemp = "";
	    					
	    					stTemp = importarExecpenNe.consultarAreasDistribuicaoExecucaoJSON(stNomeBusca1, request.getParameter("tempFluxo1"), posicaopaginaatual);	    						    					
	    					enviarJSON(response, stTemp);	    						
	    					
	    					return;
	    				}
	            break;
	            
			default:
				switch(passoEditar){
					case 1:
						diretorioArquivoLog = ProjudiPropriedades.getInstance().getCaminhoAplicacao()  + "EXECPEN" + File.separator;
						request.setAttribute("DiretorioArquivo", diretorioArquivoLog);
						break;
						
					case 2: //consulta os processos de cálculo cadastrados
						byTemp = importarExecpenNe.relProcessoCalculoCadastrado();
						String nome ="PROCPROJ";  						  						
						
						enviarPDF(response, byTemp.getBytes(), nome);																								
						
						return;
					
					case 3 : 
						response.setContentType("text/html");
					    PrintWriter out = response.getWriter();
					    out.print(ImportarExecpenCt.execucaoBarraProgresso+":"+ImportarExecpenCt.valorAtualBarraProgresso + ":" + ImportarExecpenCt.totalBarraProgresso);
					    out.close();
						return;
						
					case 4:
						conteudoArquivoProcesso = getConteudoArquivo(request);
						break;
						
					case 5:
						conteudoArquivoEvento = getConteudoArquivo(request);
						break;
				}
		}
		
		
		request.getSession().setAttribute("importarExecpenNe", importarExecpenNe);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("Comarca", comarca);
		request.setAttribute("Id_Comarca", idComarca);
		
		if(request.getParameter("tempFluxo1") != null || request.getParameter("tempFluxo2") != null){
			if(!request.getParameter("tempFluxo1").equalsIgnoreCase("null") || !request.getParameter("tempFluxo2").equalsIgnoreCase("null")){
				request.setAttribute("Id_Comarca", request.getParameter("tempFluxo1"));
				request.setAttribute("Comarca", request.getParameter("tempFluxo2"));
			}
		}
		
		
		request.setAttribute("AreaDistribuicao", areaDistribuicao);
		request.setAttribute("Id_AreaDistribuicao", idAreaDistribuicao);
		request.setAttribute("DiretorioArquivo", diretorioArquivoLog);
		request.setAttribute("conteudoArquivoProcesso", conteudoArquivoProcesso);
		request.setAttribute("conteudoArquivoEvento", conteudoArquivoEvento);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}