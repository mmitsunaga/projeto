package br.gov.go.tj.projudi.ct;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import br.gov.go.tj.projudi.GerenciaUsuarios;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.ResultadoRelatorioDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioFoneNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.AjudanteArquivosAreaTransferencia;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ConflitoDeAbasException;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PersistenciaException;
import br.gov.go.tj.utils.WebServiceException;

/**
 * Classe abstrata de Controle
 */
abstract public class Controle extends HttpServlet implements Serializable {

	private static final long serialVersionUID = -1456075939629044736L;

	protected static final String NAO_E_PUBLICO = "-133";

	protected static final int SAIR_SISTEMA = -200;
	
	private final String IdMensagemErro = "MensagemErro";
	private final String IdMensagemOk = "MensagemOk";
	private final String IdMensagem = "Mensagem";	
	public static final int AJAX_ERROR = 555;
	
	/**
     * Recebe os parâmetros request e response e passa para o método doPost
     * 
     * @param request
     * @param response
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);

    } // fim do método doGet

    /**
     * Lista todos os atributos da sessão, retirando da mesma todos os objetos
     * que não fazem parte do logon
     * 
     * @param sessao
     */
    public void LimparSessao(HttpSession sessao) {

        Enumeration a = sessao.getAttributeNames();
        do {
            String stObj = a.nextElement().toString();
            if (!stObj.equalsIgnoreCase("UsuarioSessao") && !stObj.equalsIgnoreCase("UsuarioSessaoDt") 
            		&& !stObj.contains("__ControleExecucao__")
            		&& !stObj.equalsIgnoreCase(AjudanteArquivosAreaTransferencia.CHAVE_SESSAO_LISTA_ARQUIVOS)){
            	sessao.removeAttribute(stObj);
            }
        } while (a.hasMoreElements());

    } // fim do método LimparSessao

    /**
     * 
     * @param request
     * @param response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int inPaginaAtual = Configuracao.Editar;
        String tempNomeBusca = "";
        String PosicaoPaginaAtual = "";
        UsuarioNe UsuarioSessao = null;
        
        response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");

        try{        	
        	
        	if (request.getSession().getAttribute("Invalidar_Sessão")!=null) {
        		try{
        			request.getSession(false).invalidate();
        		}catch(Exception e){}
        		throw new MensagemException("Essa sessão foi invalidada, pois esse usuário foi utilizado para fazer acesso o sistema em outro computador.");        		        		
        	}
        	
        	        	
        	if (ServletFileUpload.isMultipartContent(request)) {
             	tratarMultiParte(request);
            }
        	 
        	 //se for multipart tenho que usar getAttribute em vez de getParameter
            if (request.getParameter("PaginaAtual") != null){
            	inPaginaAtual = Funcoes.StringToInt(request.getParameter("PaginaAtual"));
            }else if (request.getAttribute("PaginaAtual") != null){
            	inPaginaAtual = Funcoes.StringToInt(request.getAttribute("PaginaAtual").toString());
            }
            if (request.getParameter("nomeBusca") != null){
            	tempNomeBusca = request.getParameter("nomeBusca");
            }else if(request.getAttribute("nomeBusca") != null){
            	tempNomeBusca =  request.getAttribute("nomeBusca").toString();
        	}else{
                tempNomeBusca = "";
        	}                            
            if (request.getParameter("PosicaoPaginaAtual") != null && !request.getParameter("PosicaoPaginaAtual").equalsIgnoreCase("null")){
            	PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
            } else if(request.getAttribute("PosicaoPaginaAtual") != null && !request.getAttribute("PosicaoPaginaAtual").toString().equalsIgnoreCase("null")){
            	PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
            }else{
            	PosicaoPaginaAtual = "0";
            }
            
            //vejo se é para sair do sistema
			if (inPaginaAtual== SAIR_SISTEMA){	
				sairSistema(request, response);				
				return;
			}
			
            UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
            
//            if (isURLServico(request.getServletPath()) && UsuarioSessao.isAcessoPredatorio()) {            
//            	throw new MensagemException("Os Serviços devem obedecer o limite de 1 requisição por cada 100 milisegundos.");            	
//        	}

            //controle do login em duas etapas
            if(UsuarioSessao != null && UsuarioSessao.isDuploLogin()) {
            	String codigo = request.getParameter("codigo");
            	if ((new UsuarioFoneNe()).validaCodigo(UsuarioSessao.getId_Usuario(),codigo)) {
            		UsuarioSessao.validadeDuploLogin(false);
            		redireciona(response, "Usuario?PaginaAtual=-10");
            		return;
            	}else {
            		RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/InformarCodigo.jsp");
               	 	dis.include(request, response);
               	 	return;
            	}
            }
			
            if (UsuarioSessao == null) {
            	String id_grupo = getId_GrupoPublico();
            	if (id_grupo.equals(NAO_E_PUBLICO)) {
            		throw new MensagemException("Usuário inválido ou sessão expirou. Se essa mensagem está acontecendo constantemente troque a sua senha.");
            	}else{
            		UsuarioSessao = carregarUsuario(id_grupo, request);
            		request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());				
            	}
            } else if (UsuarioSessao.getId_Usuario().equalsIgnoreCase("")) {
                throw new MensagemException("Usuário inválido, sem acesso ou sessão expirou.");
            }else if (UsuarioSessao.getId_Usuario().trim().equalsIgnoreCase(UsuarioDt.SistemaProjudi)) {
                if (request.getSession(false) != null) {
                	try {
                		request.getSession(false).invalidate();	
                	}catch(Exception e){}               	
                }
                throw new MensagemException("O Usuário do sistema não pode ser utilizado para entrar no sistema.");
            }
            UsuarioSessao.getUsuarioDt().setDataUlitmoAcesso(System.currentTimeMillis());

            //se apermissão for negativa, tem que existir a permissão pai
            if (inPaginaAtual<0) {
            	if (!UsuarioSessao.getVerificaPermissao(this.Permissao())) {
                    throw new MensagemException("Sem Permissão para o Usuario. Permissão n.º " + this.Permissao());
            	}
            }else if ((inPaginaAtual >= 0 && inPaginaAtual <= 9)) {
            	if (!UsuarioSessao.getVerificaPermissao(this.Permissao() * Configuracao.QtdPermissao + inPaginaAtual)) {
            		throw new MensagemException("Sem Permissão para o Usuario. Permissão nº " + (this.Permissao() * Configuracao.QtdPermissao + inPaginaAtual));
            	}
            }else if (!UsuarioSessao.getVerificaPermissao(inPaginaAtual)){
        			throw new MensagemException("Sem Permissão para o Usuario. Permissão nº " + inPaginaAtual);
            }
            
            // controle de multi-submet, sempre que a opção for salvar ou
            // excluir é criado um código
            // esse e devolvido para página, quando a opção confirmar salvar e
            // acionada esse código é
            // verificado e limpo. Somente se esse código existe a ação bd é
            // completada.
            switch (inPaginaAtual) {
            case Configuracao.Salvar:
            case Configuracao.Excluir:
                // é gerado o código do pedido, assim o submit so pode ser
                // executado uma unica vez		
            	if (request.getSession().getAttribute("__ControleExecucao__" + this.Permissao())==null) {
            		request.setAttribute("__Pedido__", UsuarioSessao.getPedido()); 		            		
            	}else{
            		//Se a variável __ControleExecucao__ estiver presente na sessão, o usuário estará refazendo uma operação
            		//que já foi tentada anteriormente. Se a solicitação tiver sido enviada há menos de 2 minutos, deve
            		//forçar o usuário a aguardar a primeira execução. Dessa forma se evita duplicidade na geração 
            		//de pendências, sobretudo conclusões.
            		long loTempo = (long) request.getSession().getAttribute("__ControleExecucao__" + this.Permissao());
            		if (( System.currentTimeMillis()-loTempo ) > 120000){
            			request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
            		}else{
            			throw new MensagemException("Aguarde, a operação solicitada está sendo executada.");
            		}
            	}
 				break;
            case Configuracao.SalvarResultado:
            case Configuracao.ExcluirResultado:            	
				if (!UsuarioSessao.verificarPedido(request.getParameter("__Pedido__"))) {
					throw new MensagemException("Pedido enviado mais de um vez.");
				}
				//Ao enviar uma requisição de salvar/excluir resultado, será adicionada à sessão essa variável contendo o momento exato
				//em que a solicitação foi enviada. Essa variável será usada para impedir que o usuário tente refazer a operação
				//caso o sistema apresente lentidão.
				//A variável será removida após a execução da tarefa, no finally deste método.
				request.getSession().setAttribute("__ControleExecucao__" + this.Permissao(), System.currentTimeMillis());
 			}
            this.executar(request, response, UsuarioSessao, inPaginaAtual, tempNomeBusca, PosicaoPaginaAtual);
            
        } catch(MensagemException ex) {        	
    		if (isURLServico(request.getServletPath())) {
    			tratarErroServico(request, response, ex, UsuarioSessao);
    			return;
    		} else if (request.getParameter("AJAX") != null){
       	 		retornarExceptionAJAX(response, UsuarioSessao, ex);
       	 		return;
            } else {	       	
	       	 	request.setAttribute("Mensagem", ex.getMessage());
	       	 	RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Mensagem.jsp");
	       	 	dis.include(request, response);
            }
        
        } catch(ConflitoDeAbasException ex) {        	       	
        	String mensagem = "Conflito de abas abertas, favor refazer a operação.";
			if (request.getParameter("AJAX") != null){
				retornarExceptionAJAX(response, UsuarioSessao, ex);				
				return;
			}			
			mensagem+= ex.getMessage();
			redireciona(response, "Usuario?PaginaAtual=" + Configuracao.Cancelar + "&MensagemErro=" + mensagem);
        } catch(PersistenciaException ex) {
        	
    		if (isURLServico(request.getServletPath())) {
				tratarErroServico(request, response, ex, UsuarioSessao);
				return;
    		} else if (request.getParameter("AJAX") != null){
       	 		retornarExceptionAJAX(response, UsuarioSessao, ex);
       	 		return;
    		} else {
    			executeTratamentoException(request, response, UsuarioSessao, ex);
    		}
        	
        } catch(WebServiceException ex) {        	
			tratarErroServico(request, response, ex, UsuarioSessao);			
    		return;
        } catch(Exception ex) {        	
        	if (isURLServico(request.getServletPath())) {    			
				tratarErroServico(request, response, ex, UsuarioSessao);				
    			return;
    		} else if (request.getParameter("AJAX") != null){
       	 		retornarExceptionAJAX(response, UsuarioSessao, ex);
       	 		return;
    		} else {
    			executeTratamentoException(request, response, UsuarioSessao, ex);
    		}

        }finally{
        	//Remoção da variável __ControleExecucao__ da sessão, pois ela deixa de ser útil quando a tarefa é finalizada.
        	if (inPaginaAtual== Configuracao.SalvarResultado || inPaginaAtual==Configuracao.ExcluirResultado){
        		request.getSession().removeAttribute("__ControleExecucao__" + this.Permissao());			
 			}
        }

    } // fim do método doPost

	private boolean isURLServico(String contextPath) {
		switch (contextPath) {
			case "/servico01":
			case "/servico02":
			case "/servico03":
			case "/servico04":
			case "/servico05":
			case "/servico06":
			case "/servico07":
			case "/servico08":
			case "/servico09":
			case "/servico10":
			case "/servico11":
			case "/CadastroProcesso":
			case "/ServicosPublicos":	
				return true;
		}
		return false;
	}

	private void tratarErroServico(HttpServletRequest request, HttpServletResponse response, Exception ex, UsuarioNe UsuarioSessao) throws ServletException, IOException  {
		
		String stMensagem = getMessageException(ex.getMessage());
		String id_log = "";
		
		id_log = salvarLog(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), ex, UsuarioSessao);
		
		request.setAttribute("Mensagem", stMensagem + " " + id_log);
		request.setAttribute("RespostaTipo", "ERRO");
		request.setAttribute("Operacao", "-1");
		
   	 	RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp");
   	 	dis.include(request, response);
	}

	private void executeTratamentoException(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, Exception ex) throws ServletException, IOException  {
		String stMensagem = getMessageException(ex.getMessage());
		String id_log = "";

		id_log = salvarLog(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), ex, UsuarioSessao);
		request.setAttribute("Mensagem", "Erro n. " + id_log + " - " + stMensagem);
			
		RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
		dis.include(request, response);
	}
	
    protected UsuarioNe carregarUsuario(String id_grupo, HttpServletRequest request) throws Exception {
    	
    	UsuarioNe UsuarioSessao = new UsuarioNe();
		UsuarioSessao.setUsuarioDt(new UsuarioDt());
		UsuarioSessao.getUsuarioDt().setId_Grupo(id_grupo);
		UsuarioSessao.getUsuarioDt().setGrupoCodigo(String.valueOf(GrupoDt.PUBLICO));
		UsuarioSessao.getUsuarioDt().setId(UsuarioDt.USUARIO_PUBLICO);
		UsuarioSessao.getUsuarioDt().setId_UsuarioLog(UsuarioDt.USUARIO_PUBLICO);
		UsuarioSessao.getUsuarioDt().setIpComputadorLog(getIpCliente(request));
							
		try {
			request.getSession(false).invalidate();	
		}catch(Exception e){}		
		String Menu =UsuarioSessao.getPermissoesGrupo();
		request.setAttribute("Menu", Menu);
		request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());
		request.getSession().setAttribute("UsuarioSessao", UsuarioSessao);
		request.getSession().setAttribute("CodigoCaptcha", "1");
		return UsuarioSessao;
	}

	//este método deve ser sobrescrito pelos ct_publicos
    protected String getId_GrupoPublico() {		
		return NAO_E_PUBLICO;
	}
    
    protected void tratarMultiParte(HttpServletRequest request) throws  MensagemException, UnsupportedEncodingException  {
    	tratarMultiParte(request, true, true);
	}
    
    protected void tratarMultiParte(HttpServletRequest request, boolean readString, boolean readContentType) throws  MensagemException, UnsupportedEncodingException  {
    	FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String enc = "iso-8859-1";
		//Interpreta dados no request em um formulário multipart
		try{
			List items = upload.parseRequest(request);
		
			// Processa os itens da página
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
	
				if (item.isFormField()) {
					//Se for um item de formulário, seta como attribute no request
					String name = item.getFieldName();
					String value = item.getString(enc);
	
					request.setAttribute(name, value);					
				} else {
					if (!"".equals(item.getName().trim()) && item.getSize() > 0){
						if (readString) {
							getListaConteudosArquivos(request).add(item.getString());
						}	
						if (readContentType) {
							getListaContentTypeArquivos(request).add(item.getContentType());
						}
						getListaArquivosBytes(request).add(item.get());
						getListaNomeArquivos(request).add(new String(item.getName().getBytes(), "utf-8") );
					}
				}
			}
        } catch(FileUploadException  ex ){
        	throw new MensagemException("O arquivo foi recebido com erro ou a transmissão foi cancelada. " + ex.getMessage());
        }
    }
    
    protected void limparMultiParte(HttpServletRequest request) {
    	getListaConteudosArquivos(request).clear();
    	getListaContentTypeArquivos(request).clear();
    	getListaArquivosBytes(request).clear();
    	getListaNomeArquivos(request).clear();
    }
	
	protected String getAtributeParameter(HttpServletRequest request, String nomeAtributoParametro) {
		String valor = request.getParameter(nomeAtributoParametro);
		if (valor != null) return valor;
		
		Object valorObj = request.getAttribute(nomeAtributoParametro);
		if (valorObj != null) valor = String.valueOf(valorObj);
		
		return valor;
	}

	protected List<String> getListaConteudosArquivos(HttpServletRequest request){
		List<String> lista = (List<String>) request.getAttribute("Arquivo");
		if(lista==null){
			lista = new ArrayList<String>();
			request.setAttribute("Arquivo",lista);
		}
		return lista;
	}
	
	protected String getConteudoArquivo(HttpServletRequest request){
		List<String> lista = getListaConteudosArquivos(request);
		if (lista != null && lista.size() > 0) return lista.get(0);
		return null;
	}
	
	protected List<byte[]> getListaArquivosBytes(HttpServletRequest request){
		List<byte[]> lista = null;
		Object o = request.getAttribute("ArquivoBytes");
		if(o==null || !(o instanceof List)){
			lista = new ArrayList<byte[]>();
			request.setAttribute("ArquivoBytes",lista);
		} else {
			lista = (List<byte[]>) o;
		}
		return lista;
	}
	
	protected byte[] getConteudoArquivoBytes(HttpServletRequest request){
		List<byte[] > lista = getListaArquivosBytes(request);
		if (lista != null && lista.size() > 0) return lista.get(0);
		return null;		
	}
	
	protected List<String> getListaNomeArquivos(HttpServletRequest request){
		List<String> lista = (List<String>) request.getAttribute("NomeArquivo");
		if(lista==null){
			lista = new ArrayList<String>();
			request.setAttribute("NomeArquivo",lista);
		}
		return lista;
	}
	
	protected List<String> getListaContentTypeArquivos(HttpServletRequest request){
		List<String> lista = (List<String>) request.getAttribute("ContentTypeArquivo");
		if(lista==null){
			lista = new ArrayList<String>();
			request.setAttribute("ContentTypeArquivo",lista);
		}
		return lista;
	}
	
	protected String getNomeArquivo(HttpServletRequest request){
		List<String> lista = getListaNomeArquivos(request);
		if (lista != null && lista.size() > 0) return lista.get(0);
		return null;
	}
	    
    protected String obtenhaConteudoLog(Exception ex, UsuarioNe usuarioNe){
    	String dadosUsuarioLog = "Usuário: "+usuarioNe.getUsuarioDt().getUsuario() +" -- Perfil: "+ usuarioNe.getUsuarioDt().getGrupo();
    	try{
    		return dadosUsuarioLog+Funcoes.obtenhaConteudoPrimeiraExcecao(ex);
    	}catch(Exception e){
    		return ex.getMessage();
    	}    	
    }    
    
    public static String getIpCliente(HttpServletRequest request) {
        final String HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";
        String stIps = "";
        String stTemp= "";        
        stIps =request.getHeader(HEADER_X_FORWARDED_FOR);
        if (stIps != null) {
            ////System.out.println("IPs...: " + stIps);            
            String[] vetIps = stIps.split(",");
            if (vetIps.length>0){           
                stTemp = vetIps[vetIps.length-1];
                ////System.out.println("IP Recuperado...: " + vetIps[vetIps.length-1]);  
            }
        }else{
            stTemp =request.getRemoteAddr();
        }
        return stTemp;
    }

    abstract public int Permissao();

    /**
     * 
     * @param request
     * @param response
     * @param UsuarioSessao
     * @param paginaatual
     * @param nomebusca
     * @param posicaopaginaatual
     * @throws Exception
     * @throws ServletException
     * @throws IOException
     */
    abstract public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException;

    /**
     * Monta uma listagem
     * 
     * @author Ronneesley Moura Teles
     * @since 24/06/2008 09:25
     * @param HttpServletRequest
     *            request
     * @param String
     *            nome, nome para o atributo lista
     * @param List
     *            lista, lista a ser atribuida
     * @param long
     *            qtd, quantidade de registros
     * @param String
     *            posicao, posicao na lista
     * @param String
     *            mensagem, mensagem a ser exibida caso nao possua itens
     * @param int
     *            paginaAtual, pagina atual
     * @param int
     *            proximaPagina, pagina atual caso nao tenha itens na lista
     * @return
     */
    public boolean lista(HttpServletRequest request, String nome, List lista, long qtd, String posicao, String mensagem, int paginaAtual, int proximaPagina) {

        if (lista.size() > 0) {
            request.setAttribute("Lista" + nome, lista);
            request.setAttribute("PaginaAtual", paginaAtual);
            request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicao));
            request.setAttribute("QuantidadePaginas", qtd);

            return true;
        } else {
            request.setAttribute("MensagemErro", mensagem);
            request.setAttribute("PaginaAtual", proximaPagina);
        }
        return false;

    } // fim do método lista
    
    /**
     * Redireciona as requisições através do objeto Dispacher...
     * 
     * @param String acao
     * @param HttpServletRequest request
     * @param HttpServletResponse response
     */
    protected void redirecione(String acao,
    		                   HttpServletRequest request, 
    		                   HttpServletResponse response) throws ServletException, IOException{
    	
    	RequestDispatcher dis = request.getRequestDispatcher(acao);
        dis.include(request, response);
    }
    
    /**
     * Prepara o caminho de uma página jsp para ser utilizado pelo método redirecione
     * 
     * @param String paginaSemExtensao
     * 
     */
    protected String obtenhaAcaoJSP(String paginaSemExtensao){
    	return "/WEB-INF/jsptjgo/" + paginaSemExtensao  + ".jsp";
    }
    
    /**
     * Armazena na requisição a mensagem de Erro/Inconsistencia para ser exibida no browser.
     * 
     * @param HttpServletRequest request
     * @param String mensagem
     * 
     */
    protected void exibaMensagemInconsistenciaErro(HttpServletRequest request, String mensagem){
    	request.setAttribute(IdMensagemErro,  mensagem);
    }
    
    /**
     * Armazena na requisição a mensagem de Sucesso para ser exibida no browser.
     * 
     * @param HttpServletRequest request
     * @param String mensagem
     * 
     */
    protected void exibaMensagemSucesso(HttpServletRequest request, String mensagem){
    	request.setAttribute(IdMensagemOk, mensagem);
    }
    
    /**
     * Armazena na requisição a mensagem de Confirmação para ser exibida no browser.
     * 
     * @param HttpServletRequest request
     * @param String mensagem
     * 
     */
    protected void exibaMensagemConfirmacao(HttpServletRequest request, String mensagem){
    	request.setAttribute(IdMensagem, mensagem);
    }
    
    /**
     * Limpa as mensagens da requisição.
     * 
     * @param HttpServletRequest request 
     * 
     */
    protected void limpeMensagens(HttpServletRequest request){
    	request.setAttribute(IdMensagem, "");
    	request.setAttribute(IdMensagemOk, "");
		request.setAttribute(IdMensagemErro, "");
    }
    
    /**
     * Atribui os dados necessários no request para gerar o json
     * @author jrcorrea
     * 
     * @param request -
     * @param tempBuscaID - id utilizado no campo de busca (id que será encontrado)
     * @param tempBuscaDescricao - Descrição do campo de busca que aparecerá na tela
     * @param tempBuscaPrograma - Titulo da Tela de localizar 
     * @param tempRetorno - Ct para onde a busca retonará os dados
     * @param tempDescricaoId - Para qual id será retornado o id localizado
     * @param tempPaginaAtualJSON - Case de retorno após a escolha na página localizar
     * @param PaginaAtual - Qual é o case atual - permisão utilizada
     * @param PosicaoPaginaAtual - Qual página de dados e a a tual (inicia-se com 0)
     * @param QuantidadePaginas - Quantas pagina existe com dados (inicia-se com 0)
     * @param lisNomeBusca - Lista com os campos que serão utilizados para a apresentação na tela (para gerar a tabela)
     * @param lisDescricao - Lista com as descrições das colunas das tabelas
     */
    

    public void atribuirJSON(HttpServletRequest request, String tempBuscaId, String tempBuscaDescricao, String tempBuscaPrograma, String  tempRetorno, int tempPaginaAtualJSON, String PaginaAtual, String[] lisNomeBusca, String[] lisDescricao ){
    	
    	request.setAttribute("tempBuscaId", tempBuscaId);
		request.setAttribute("tempBuscaDescricao",tempBuscaDescricao );
		request.setAttribute("tempBuscaPrograma",tempBuscaPrograma);
		request.setAttribute("tempRetorno", tempRetorno);
		request.setAttribute("tempPaginaAtualJSON", tempPaginaAtualJSON);
		request.setAttribute("PaginaAtual", PaginaAtual);
		request.setAttribute("PosicaoPaginaAtual", "0");
		request.setAttribute("QuantidadePaginas", "0");
		request.setAttribute("lisNomeBusca", lisNomeBusca);
		request.setAttribute("lisDescricao", lisDescricao);	
    	
    }
    
    /**
     * Gera e retorna um novo código hash
     * 
     * @param HttpServletRequest request
     * @param String codigo
     * @throws Exception 
     */
    protected String getCodigoHash(HttpServletRequest request, String codigo) throws Exception{
		String stTemp;
		stTemp = Funcoes.SenhaMd5(getStVerificador(request) + codigo);
		return stTemp;
	}

    /**
     * valida um código hash gerado anteriormente
     * 
     * @param HttpServletRequest request
     * @param String codigo
     * @param String hash
     * @throws Exception 
     */
    protected boolean verificarCodigoHash(HttpServletRequest request, String codigo, String hash) throws Exception{
		boolean boRetorno = false;

		String stTemp = Funcoes.SenhaMd5(getStVerificador(request) + codigo);

		if (stTemp.equals(hash)) boRetorno = true;

		return boRetorno;
	}
    
    private final String IdStVerificador = "stVerificador";
    
    /**
     * Obtem da sessão um código verificador para geração de hash
     * 
     * @param HttpServletRequest request
     * 
     */
	private String getStVerificador(HttpServletRequest request){
		return (String) request.getSession().getAttribute(IdStVerificador);
	}
	
//	protected String ProjudiPropriedades.getInstance().getCaminhoAplicacao() {
//		String caminhoDaAplicacao = this.getServletContext().getRealPath(File.separator);
//		
//		if (caminhoDaAplicacao != null && !caminhoDaAplicacao.trim().endsWith(File.separator)) caminhoDaAplicacao += File.separator;
//		
//		return caminhoDaAplicacao;
//	}
	
	 protected void redireciona(HttpServletResponse response, String url) throws IOException {
		 if (url == null) return;
		 String complemento = "&";
		 if (!url.contains("?")) complemento = "?";
		 complemento += "hashFluxo=" + new Date().getTime();
		 response.sendRedirect(getUrlEncoder(url + complemento));
	 }
	 
	 private String getUrlEncoder(String url) throws UnsupportedEncodingException {
		 if (url == null || !url.contains("=") || !url.contains("?")) return url;
		 
		 String partesPrincipais[] = url.split("\\?");
		 
		 String urlTratada = partesPrincipais[0];
		 
		 String parametros[] = partesPrincipais[1].split("&");
		 for(int i = 0; i < parametros.length; i++) {
			 if (i==0)urlTratada += "?";
			 else urlTratada += "&";
			 
			 if (parametros[i].contains("=")) {
				 String parametroValor[] = parametros[i].split("=");
				 if(parametroValor.length>1)
					 urlTratada += parametroValor[0] + "=" + URLEncoder.encode(parametroValor[1], "UTF-8");
				 else
					 urlTratada += parametroValor[0] + "=";
			 } else {
				 urlTratada += parametros[i];
			 }			 
		 }
		 
		 return urlTratada;
	 }
	 
	 protected byte[] getArquivoBytes(HttpServletRequest request){
		return getListaArquivosBytes(request).get(0);
	}
	 
	public void exibirCaptcha(HttpServletRequest request, String action, String nome, String valor) {
		request.setAttribute("action", action);
		request.setAttribute("nome", nome);
		request.setAttribute("valor", valor);
	}

//	/**
//	 * Verifica se as letras digitadas pelo usuário conferem com a imagem gerada do captcha
//	 
//	 */
//	public boolean validaCaptcha(HttpServletRequest request) {
//		try {
//			String captchaId = request.getSession().getId();
//			String captcha = request.getParameter("textoDigitado");
//
//			// Verifica se caracteres digitados conferem com o código de conferência(captcha)
//			if ((captcha != null) && (GerenciadorCaptcha.getService().validateResponseForID(captchaId, captcha))) {
//				return true;
//			}// No caso de código de verificação digitado não conferir com a imagem
//			else {
//				if ((captcha != null) && (!captcha.equals(""))){
//					request.setAttribute("MensagemErro", "Código digitado não confere");
//				}else{
//					request.setAttribute("MensagemErro", "Digite o código de conferência para efetuar a consulta");
//				}
//			}
//			// Tratamento de exceção no caso do captcha ficar em cache e não ser atualizado.
//		} catch (CaptchaServiceException e) {
//			return false;
//		}
//		return false;
//	}

	protected String salveLog(Exception ex, UsuarioNe UsuarioSessao) throws Exception {
		LogNe logNe = new LogNe();
		if (UsuarioSessao != null) {
			logNe.salvarErro(new LogDt(this.getServletName(), "", UsuarioSessao.getId_Usuario(), "127.0.0.1", String.valueOf(LogTipoDt.Erro), "", obtenhaConteudoLog(ex, UsuarioSessao)));
		}
		return logNe.getId_Log();
	}
	
	protected void sairSistema(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dis = null;
		
		boolean isExecpenweb = false;
		if (request.getSession().getAttribute("Acesso") != null && request.getSession().getAttribute("Acesso").equals("Execpenweb")) {
			isExecpenweb = true;
		}		
		
		UsuarioNe usuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
		
		if (usuarioSessao != null) {
			GerenciaUsuarios.getInstancia().InvalidaSessao(usuarioSessao.getId_Usuario());	
		}
		
		//request.getSession().removeAttribute("UsuarioSessao");
		
		if (!isURLServico(request.getServletPath())) {
			if (isExecpenweb) {
				dis = request.getRequestDispatcher("/index2.jsp");
			}
			else {
				dis = request.getRequestDispatcher("/index.jsp");
			}
			dis.include(request, response);
		}
		try {
			request.getSession(false).invalidate();	
		} catch (Exception ex) {}
	}
	
	public static final String DescMensagemOkInclusaoCadastro	= "Os dados foram incluídos/alterados com sucesso!";
	public static final String DescMensagemOkExclusaoCadastro	= "Os dados foram excluídos com sucesso!";
	public static final String DescMensagemNaoEncontradoConsulta = "A informação não foi encontrada.";
	public static final String DescMensagemErroCadastro		  = "Ocorreu um erro ao salvar os dados.";
	
	protected void montaRetornoJSON(HttpServletResponse response, String conteudoJSON) throws IOException, JSONException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.append(conteudoJSON);
		writer.flush();
	}
	
	protected void montaRetornoMensagemComboJSON(HttpServletResponse response, String mensagem) throws IOException, JSONException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", 0);
		jsonObject.put("mensagem", mensagem);
		jsonArray.put(jsonObject);
		montaRetornoJSON(response, jsonArray.toString());
	}
	
	protected String obtenhaValorCascadeComboJSON(HttpServletRequest request, String nomeConceito, String nomeControle, String valorControle)
			throws MensagemException {
		if (nomeConceito != null && nomeConceito.trim().length() > 0 && request.getParameter(nomeControle) != null
				&& request.getParameter(nomeControle).trim().length() > 0 && request.getParameter(nomeControle).trim().equalsIgnoreCase(nomeConceito)) {
			if (request.getParameter(valorControle) != null && request.getParameter(valorControle).trim().length() > 0)
				return request.getParameter(valorControle).trim();
			throw new MensagemException("Selecione primeiramente o atributo " + nomeConceito + ".");
		}
		
		return "";
	}
	
	protected String obtenhaValorDoParametro(HttpServletRequest request, String nomeParametro) throws MensagemException {
		if (nomeParametro != null && nomeParametro.trim().length() > 0 && request.getParameter(nomeParametro) != null
				&& request.getParameter(nomeParametro).trim().length() > 0)
			return request.getParameter(nomeParametro).trim();
		return "";
	}
	
	protected void montaRetornoMensagemJSON(HttpServletResponse response, String mensagem) throws IOException, JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mensagem", mensagem);
		montaRetornoJSON(response, jsonObject.toString());
	}
	
	protected void montaRetornoMensagemJSON(HttpServletResponse response, String mensagem, String tipoMensagem) throws IOException, JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mensagem", mensagem);
		jsonObject.put("tipo", tipoMensagem); // {sucesso, erro, alerta, informacao)
		montaRetornoJSON(response, jsonObject.toString());
	}
	
	protected void montaRetornoSucessoJSON(HttpServletResponse response) throws IOException, JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sucesso", "sucesso");
		montaRetornoJSON(response, jsonObject.toString());
	}
	
	protected void montaRetornoJSON(HttpServletResponse response, String key, String value) throws IOException, JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(key, value);
		montaRetornoJSON(response, jsonObject.toString());
	}
	
	/**
	 * Método responsável em transformar o conteúdo de um json criado pela camada de persistência no modelo esperado pelo controle bootstrap-table, onde deverão existir as colunas 'rows' e 'total'.
	 * 
	 * @param response
	 * @param conteudoJSON
	 * @throws IOException
	 * @throws JSONException
	 */
	protected void montaRetornoJSONParaBootstrapTable(HttpServletResponse response, String conteudoJSON) throws IOException, JSONException {
		JSONArray jsonArray = new JSONArray(conteudoJSON);
		if (jsonArray.length() > 0) {
			// Obtém o 1o json do conteúdo que contém na coluna desc1, o valor total de registros.
			// Após isso, descartar o json do conteúdo original.
			JSONObject item0 = (JSONObject) jsonArray.remove(0);
			String qtde = String.valueOf(item0.get("desc1"));
			// Apenas descarta o 2o json do conteúdo original
			jsonArray.remove(0);
			// Define o json de resposta, com as colunas total e rows
			JSONObject json = new JSONObject();
			json.put("total", qtde);
			json.put("rows", jsonArray);
			montaRetornoJSON(response, json.toString());
		}
	}
	
	/**
	 * Método responsável em transformar o conteúdo de um json criado pela camada de persistência no modelo esperado pelo controle bootstrap-table, onde deverão existir as colunas 'rows' e 'total'.
	 * 
	 * @param response
	 * @param conteudoJSON
	 * @throws IOException
	 * @throws JSONException
	 */
	protected void montaRetornoJSONParaBootstrapTable(HttpServletResponse response, List lista) throws IOException, JSONException {
		JSONObject json = new JSONObject();
		json.put("total", lista != null ? lista.size() : 0);
		json.put("rows", lista != null ? new JSONArray(lista) : "");
		montaRetornoJSON(response, json.toString());
	}
	
	/**
	 * Método responsável em identificar qual o número da página em relação de um offset (0 até a quantidade de registros -1) e quantidade de registros de paginação.
	 * 
	 * @param deslocamento
	 * @param tamanhoPagina
	 * @return
	 */
	public String getNumeroPaginaDoDeslocamento(String deslocamento, String tamanhoPagina) {
		int deslocamentoInt = Integer.valueOf(deslocamento).intValue();
		int tamanhoPaginaInt = Integer.valueOf(tamanhoPagina).intValue();
		Double pagina = Double.valueOf(Math.ceil(deslocamentoInt / tamanhoPaginaInt));
		return deslocamentoInt > 0 ? String.valueOf(pagina.intValue()) : "0";
	}
	
	protected static <T extends Object> T setFromMap(Class<T> beanClazz, HashMap<String, String> propValues) throws Exception {
		T bean = (T) beanClazz.newInstance();
		// Object obj = new Object();
		PropertyDescriptor[] pdescriptors = null;
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClazz);
		pdescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < pdescriptors.length; i++) {
			String descriptorName = pdescriptors[i].getName();
			if (!(descriptorName.equals("class"))) {
				String propName = descriptorName;
				String value = (String) propValues.get(propName);
				if (value != null) {
					Object[] objArray = new Object[1];
					objArray[0] = value;
					Method writeMethod = pdescriptors[i].getWriteMethod();
					writeMethod.invoke(bean, objArray);
				}
			}
		}
		return bean;
	}
		
	protected String nomePastaAtual(HttpServletRequest request) {
		if (request.getLocalName().toString().equals("127.0.0.1")) {
			return "jsp";
		}
		else {
			return "jsptjgo";
		}
	}

	public void enviarJSON(HttpServletResponse response, String dados) {		
		try {
			response.setContentType("application/json");					
			response.getOutputStream().write(dados.getBytes());
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}

	public void enviarPDF(HttpServletResponse response, byte[] dados, String nome) {		
		try {
			//inline para abrir no proprio navegador, attachment para fazer download
			response.setHeader("Content-Disposition", "inline; filename=" + nome + System.currentTimeMillis()+".pdf");
			response.setContentType("application/pdf");					
			response.getOutputStream().write(dados);
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}
	
	public void enviarPDFDownload(HttpServletResponse response, byte[] dados, String nome) {		
		try {
			//inline para abrir no proprio navegador, attachment para fazer download
			response.setHeader("Content-Disposition", "attachment; filename=" + nome + System.currentTimeMillis()+".pdf");
			response.setContentType("application/pdf");					
			response.getOutputStream().write(dados);
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}

	public void enviarImagem(HttpServletResponse response, byte[] dados) {		
		try {
			//inline para abrir no proprio navegador, attachment para fazer download			
			response.setHeader("Content-Disposition", "inline; filename=imagem" + System.currentTimeMillis()+".jpg");			
			response.setContentType("image/jpeg");						
			response.getOutputStream().write(dados);
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}		
	
	public void enviarODT(HttpServletResponse response, byte[] dados, String nome) {
		try {			
			//inline para abrir no proprio navegador, attachment para fazer download			
			response.setHeader("Content-Disposition", "inline; filename="+ nome +System.currentTimeMillis()+".odt");
			response.setContentType("application/odt");			
			response.getOutputStream().write(dados);
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}

	public void enviarTXT(HttpServletResponse response, byte[] dados, String nome) {
		try {						
			response.setHeader("Content-Disposition", "attachment; filename=" + nome +System.currentTimeMillis()+".txt");
			response.setContentType("text/plain");	
			response.getOutputStream().write(dados);
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}
	
	public void enviarCSV(HttpServletResponse response, String dados, String nome) {
		try {			
			//inline para abrir no proprio navegador, attachment para fazer download			
			response.setHeader("Content-Disposition", "inline; filename=" + nome +System.currentTimeMillis()+".csv");
			response.setContentType("text/plain");	
			response.getOutputStream().write(dados.getBytes());
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}
	
	public void enviarCSVDownload(HttpServletResponse response, String dados, String nome) {
		try {			
			//inline para abrir no proprio navegador, attachment para fazer download			
			response.setHeader("Content-Disposition", "attachment; filename=" + nome +System.currentTimeMillis()+".csv");
			response.setContentType("text/plain");	
			response.getOutputStream().write(dados.getBytes());
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}
	
	public void enviarProjudi(HttpServletResponse response, byte[] dados, String nome) {
		try {		
			//inline para abrir no proprio navegador, attachment para fazer download			
			response.setHeader("Content-Disposition", "attachment; filename=" + nome + "_" + System.currentTimeMillis()+".projudi");
			response.setContentType("text/plain");	
			response.getOutputStream().write(dados);
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}
	
	public void enviarParaDownload(HttpServletResponse response, ResultadoRelatorioDt resultadoRelatorioDt) {
		try {			
			//inline para abrir no proprio navegador, attachment para fazer download			
			response.setHeader("Content-Disposition", "attachment; filename=" + resultadoRelatorioDt.getNomeArquivo());
			response.setContentType(resultadoRelatorioDt.getContentType());	
			response.getOutputStream().write(resultadoRelatorioDt.getConteudoArquivo());
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}
	
	public void retornarExceptionAJAX(HttpServletResponse response, UsuarioNe UsuarioSessao, Exception ex) {
		
		String stMensagem = getMessageException(ex.getMessage());
		
		if (!(ex instanceof MensagemException)) {
			String id_log = "";

			id_log = salvarLog(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), ex, UsuarioSessao);
			stMensagem = "Erro n. " + id_log + " - " + stMensagem;
		}
		
		try {															
			response.setContentType("text/plain");	
			response.setStatus(AJAX_ERROR);
			response.getOutputStream().write(stMensagem.getBytes());
			response.flushBuffer();
		} catch (Exception e) {
			// se der erro de escrita para o cliente não faz nada pois não há mais forma de rentornar nada				
		}		
	}
	
	public boolean checkRecaptcha(HttpServletRequest request){
		String recap = request.getParameter("g-recaptcha-response");
		
		UsuarioNe UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
		
		if(UsuarioSessao.isValidadoGooogle()) {
			return true;
		}
		
		if (recap==null) {
			return false;
		}
		/*
		 * 
		 * quem esta como adm são: o marcio, o jesus, bernardes, o massa, giulinao, jadher
		 * basta logar no google e acessar o endereço abaixo:
		 * https://www.google.com/recaptcha/admin/site/345693108
		 * 
		 */
		try{
			String urlGoogle = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
			
			String secret = "6Lc9XJQUAAAAAF_q4vg9CgZAo06iulQF3N36MlFv";
			// Send get request to Google reCaptcha server with secret key
			String urlFormatada = String.format(urlGoogle, secret, recap, (request.getRemoteAddr() != null ? request.getRemoteAddr() : "0.0.0.0"));
			URL url = new URL(urlFormatada);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String line, outputString = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				outputString += line;
			}
			// Convert response into Object
			CaptchaResponse capRes = new Gson().fromJson(outputString, CaptchaResponse.class);

			// Verify whether the input from Human or Robot
			if (capRes.isSuccess()) {
				// Input by Human
				UsuarioSessao.setGoogleOK();
				return true;
			} else {
				// Input by Robot
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
 	private class CaptchaResponse {
		private boolean success;
 		private String[] errorCodes;

 		public boolean isSuccess() {
 			return success;
 		}

 		public void setSuccess(boolean success) {
 			this.success = success;
 		}

 		public String[] getErrorCodes() {
 			return errorCodes;
 		}

	 	public void setErrorCodes(String[] errorCodes) {
 			this.errorCodes = errorCodes;
 		}

 	}	
 	
	public void atribuiRequest(HttpServletRequest request, Object objDt) {
		Enumeration<String> parametros = request.getParameterNames();

		while(parametros.hasMoreElements()) {
			 String param = parametros.nextElement();
			 String valor = request.getParameter(param);
			 try {
    			 if ( !valor.isEmpty()) {
    				 Method setNameMethod;
     				setNameMethod = objDt.getClass().getMethod("set" + param, valor.getClass());
     				setNameMethod.invoke(objDt, valor.equalsIgnoreCase("null")?"":valor);
     		        //System.out.println(param + " : " + valor);
    			}
			 } catch (Exception  e) {	 } 
		}				
	}
	
	private String salvarLog(String id_Usuario, String ipComputadorLog, Exception ex, UsuarioNe UsuarioSessao) {
		LogNe logNe = new LogNe(); 
		String tempIdLog = "";
		String hostname = "";	
		String stIp = "";
		try {
			InetAddress ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();	
			stIp = ip.getHostAddress();
			
			if (UsuarioSessao != null && UsuarioSessao.isTemIdUsuario()){
				logNe.salvarErro(new LogDt(this.getServletName(), "", id_Usuario, ipComputadorLog, String.valueOf(LogTipoDt.Erro), "",  obtenhaConteudoLog(ex, UsuarioSessao) + "Servidor: "+hostname+"("+stIp+")"));		         	  
			} else {
				logNe.salvarErro(new LogDt(this.getServletName(), "UsuarioVazio", UsuarioDt.SistemaProjudi, stIp, String.valueOf(LogTipoDt.Erro), "",  Funcoes.obtenhaConteudoPrimeiraExcecao(ex)));
			}
			tempIdLog = logNe.getId_Log();
		} catch (Exception e) {
			Logger logger = Logger.getLogger(Controle.class);
    		logger.warn("Data do erro no arquivo de log do servidor " + Funcoes.DataHora(System.currentTimeMillis()) + ". Erro no tratamento de uma Exception. Servidor: "+hostname+"("+stIp+")", e);
    		logger.warn("Erro capturado não processado no catch(Exception) devido à exceção anterior.", e);
		}
		return tempIdLog;
	}
	
	private String getMessageException(String mensagem) {
		String stMensagem = mensagem;
	    if (stMensagem != null && stMensagem.length() > 0) {
	        Pattern paTeste01 = Pattern.compile("<\\{(.*?)\\}>");
	        Matcher maTeste01 = paTeste01.matcher(stMensagem);
	        stMensagem = "";
	        while (maTeste01.find()) {
	            stMensagem += maTeste01.group().replaceAll("(<\\{)|(\\}>)", "");
	        }
	    } else {
	    	stMensagem = "";
	    }
	    if (stMensagem.trim().length() == 0) {
	    	stMensagem = mensagem;
	    }
	    return stMensagem;
	}	    
	
} // fim da classe Controle
