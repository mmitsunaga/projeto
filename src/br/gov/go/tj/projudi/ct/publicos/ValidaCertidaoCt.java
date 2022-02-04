package br.gov.go.tj.projudi.ct.publicos;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import br.gov.go.tj.projudi.ne.OficialCertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet implementation class ValidaMandadoCt
 */
public class ValidaCertidaoCt extends Controle {
	
       
	private static final long serialVersionUID = -2998882052785505742L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ValidaCertidaoCt() {
        super();
        
    }
	
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
		String stIdArquivo = "";		
		String PosicaoPaginaAtual = "";
		String posicaoPagina= "";
		int passoBusca = 0;		
		OficialCertidaoNe oficialCertidaoNe;
		byte[] byTemp = null; //Usado para imprimir relatório
		//Configuracoes gerais
					
		response.setContentType("text/html");
        response.setCharacterEncoding("iso-8859-1");
        
		String stAcao = "/WEB-INF/jsptjgo/Branco.html";
		
		oficialCertidaoNe = (OficialCertidaoNe) request.getSession().getAttribute("OficialCertidaoNe");
		if (oficialCertidaoNe == null) oficialCertidaoNe = new OficialCertidaoNe();	
		
		if (request.getParameter("PosicaoPaginaAtual") == null) PosicaoPaginaAtual = "0";
		else PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
		
		// Pega valor digitado na caixa de paginação
		if (request.getParameter("PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		
		request.setAttribute("tempPrograma", "CertidaoPublica");		
		request.setAttribute("tempRetorno", "CertidaoPublica");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		/**
		 * Variável PassoBusca utilizada para auxiliar na busca		  
		 * PassoBusca 3 : Valida Captcha e caso usuario tenha digitado corretamente mostra a certidão  
		 */
		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		
		switch (paginaatual) {	
			case Configuracao.LocalizarDWR: 
				String codValidacao = request.getParameter("codCertidao");
				String valor = Cifrar.decodificarId_Certidao(codValidacao);
				request.getSession().setAttribute("codigo", codValidacao);

				if(Funcoes.StringToInt(valor) == -1){
					stAcao = "/WEB-INF/jsptjgo/Erro.jsp";
					request.setAttribute("Mensagem",  "Código Inválido, não foi possivel validar o mandado.");
					
				}else {
					if (valor != null && valor.length() > 0) {
						stAcao = "/WEB-INF/jsptjgo/ValidaCertidao.jsp";
					}
				}
				break;			
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stIdArquivo = request.getParameter("Id_Arquivo");	
				stAcao = "/WEB-INF/jsptjgo/ValidaCertidao.jsp";
				switch (passoBusca) {					
					case 0:				
						request.getSession().setAttribute("CodigoCaptcha", "1");				
						request.setAttribute("PaginaAtual", paginaatual);
						break;
					case 2: //Redireciona para tela de catpcha
					case 3:
//						if (validaCaptcha(request)) {
						if(super.checkRecaptcha(request)){	
							String idCertidao = Cifrar.decodificarId_Certidao((String) request.getSession().getAttribute("codigo"));
							
							OficialCertidaoDt oficialCertidaoDt = oficialCertidaoNe.consultarId(idCertidao);
							 if (oficialCertidaoDt != null) {
									//System.out.println("Número do mandado: "+oficialCertidaoDt.getNumeroMandado()); 
									if (oficialCertidaoDt.getSegredoJustica().equals("S")){
										request.setAttribute("MensagemErro", "Certidão "+oficialCertidaoDt.getId()+" do mandado "+oficialCertidaoDt.getNumeroMandado()+" Validada, em Segredo de Justiça!");
									}else{
										byTemp = oficialCertidaoNe.gerarCertidao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), oficialCertidaoDt);						            											
										enviarPDF(response, byTemp, "RelatorioListaCertidao");
										return;
									}
								}else{
									request.setAttribute("MensagemErro", "Certidão não encontrada!");
								}
									
							//System.out.println("Certidão número: "+idCertidao);
						}
//						}else{
//							exibirCaptcha(request, "ValidaCertidao","Id_Arquivo", stIdArquivo);
//							stAcao = "/WEB-INF/jsptjgo/Padroes/Captcha.jsp";
//						}
						break;
				}
		}
		
		request.setAttribute("PosicaoPagina", Funcoes.StringToInt(posicaoPagina+1));		
		request.getSession().setAttribute("OficialCertidaoNe", oficialCertidaoNe);		
		request.setAttribute("PassoBusca", passoBusca);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

	public int Permissao() {
		return 873;
	}
	
	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
    protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
	
}
