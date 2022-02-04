package br.gov.go.tj.projudi.ct;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerenciaArquivo;
import br.gov.go.tj.utils.AjudanteArquivosAreaTransferencia;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.Base64Utils;

/**
 * Classe responsável em tratar a inserção de arquivos usando DWR
 */
public class InsercaoArquivoCt extends Controle {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7015568780704184831L;
	
	private static final int LIMPAR_AREA_TRANSFERENCIA = 1;
	private static final int INCLUIR_ARQUIVO_AREA_TRANSFERENCIA = 2;
//	private static final int LIMPAR_ARQUIVOS_ASSINADOS = 3;
	private static final int SET_ARQUIVO = 4;
//  private static final int SET_ABA = 5;
//	private static final int SUBMENTER_ARQUIVOS_ASSINADOS = 6;
//	private static final int CANCELAR_ARQUIVO_ASSINADO = 7;
	private static final int EXCLUIR_ARQUIVO = 8;
	private static final int GET_TODOS_ARQUIVOS = 9;
	//private static final int SET_ARQUIVO_ASSINADO = 10;
	private static final int ASSINAR_HTML_SESSION = 11;


//	/**
//	 * Retorna lista de arquivos adicionados (DWR)
//	 * @return
//	 * @throws Exception
//	 */
//	private Set getLista( HttpServletRequest request) throws Exception{
//		Set listaArquivo = null;	
//
//		listaArquivo = (Set) request.getSession().getAttribute("ListaArquivosDwr");
//		if (listaArquivo == null) {
//			listaArquivo = new LinkedHashSet();
//			request.getSession().setAttribute("ListaArquivosDwr", listaArquivo);
//		}
//		return listaArquivo;
//	}
	
	/**
	 * Retorna uma lista de arquivos identificada. Método usado para retornar
	 * o map dos arquivos adicionados
	 * 
	 * @param nomeLista
	 * @throws Exception 
	 */
	private Map<String, ArquivoDt> getMapArquivosSessao(String nomeLista, HttpServletRequest request) throws Exception{
		Map<String, ArquivoDt> mapArquivosSessao = (Map<String, ArquivoDt>) request.getSession().getAttribute(nomeLista);
		if (mapArquivosSessao == null) {
			mapArquivosSessao = new LinkedHashMap<String, ArquivoDt>();
			request.getSession().setAttribute(nomeLista, mapArquivosSessao);
		}
		return mapArquivosSessao;
	}

	/**
	 * Método responsável em adicionar um arquivo que está sendo assinado pelo usuário,
	 * resgata o P7s devolvido pelo applet, realiza verificações da assinatura e caso não tenha restrições
	 * adiciona o arquivo na lista.
	 * 
	 * Esse método manipula duas listas: um Set e um Map, ambas ficam na sessão.
	 * O Set é uma lista apenas para exibir os dados dos arquivos inseridos na tela, não tem os dados completos
	 * pois não é necessário trafegar esses dados.
	 * O Map tem os dados completos do arquivo, inclusive o conteúdo assinado.
	 * 
	 * @param arquivo, arquivo recebido
	 * 
	 * @author jrcorrea, msapaula
	 * @param request 
	 * @param request 
	 * @throws Exception 
	 */
	public void setArquivo(ArquivoDt arquivo, HttpServletRequest request ) throws Exception{

//		//Lista com arquivos para ser utilizada no DWR, só possui dados que serão mostrados na tela
//		Set listaDwr = getLista(request);

		//Lista de arquivos que ficará na sessão, arquivos tem todos dados (inclusive conteúdo assinado)
		Map<String, ArquivoDt> mapArquivosSessao = getMapArquivosSessao("ListaArquivos",request);

		ArquivoDt dtArquivo = new ArquivoDt();
		ArquivoNe arquivoNe = new ArquivoNe();
		
		if (arquivo.getId_ArquivoTipo() != null && !arquivo.getId_ArquivoTipo().equals("")) {
			dtArquivo.setId_ArquivoTipo(arquivo.getId_ArquivoTipo());
			dtArquivo.setArquivoTipo(arquivo.getArquivoTipo());
			dtArquivo.setNomeArquivo(arquivo.getNomeArquivo());

			//Verifica se o arquivo tem conteudo
			if (arquivo.getArquivo() != null && arquivo.getArquivo().length() > 0 ) {
				//Atribui o conteudo do arquivo
			    
				dtArquivo.setArquivo(arquivo.getArquivo());
				arquivo.setArquivo("");
				
				String contentType = arquivo.getContentType();
				if (contentType==null || contentType.isEmpty()) {
					throw new MensagemException("Não foi possível determinar o ContentType do arquivo.");
				}else if(contentType.equals("text/html")){
					arquivo.setContentType("text/html");
					String nomeArquivo = arquivo.getNomeArquivo();
					if (nomeArquivo==null || nomeArquivo.isEmpty()) {
						arquivo.setNomeArquivo("online.html");
					}else {		
						arquivo.setNomeArquivo(nomeArquivo.replace(".html", "") + ".html");
					}
				}else {
					//se o arquivo não é do editor ele chega em base 64, tenho que converter para bytes
					String[] conteudoBase64 = dtArquivo.getArquivo().split(",");
					if(conteudoBase64.length==2) {
						dtArquivo.setArquivo(Base64Utils.base64Decode(conteudoBase64[1]));
					}else {
						dtArquivo.setArquivo(Base64Utils.base64Decode(conteudoBase64[0]));
					}
				}
				//Apenas adiciona o tipo do arquivo
				dtArquivo.setContentType(arquivo.getContentType());
				
				//Verifica se o arquivo e assinado
				if (arquivo.isAssinado()){	
					//Resgata P7s. Esse será adicionado a lista de arquivos da sessão "ListaArquivos"					
					GerenciaArquivo.getInstancia().getArquivoP7s(dtArquivo);
					//Se for arquivo on-line devolve o nome setado por padrão
					if (arquivo.getNomeArquivo().equals("")) {
						arquivo.setNomeArquivo(dtArquivo.getNomeArquivo());
					}
				} else {
					
					String assinante = null;
					try {
						assinante = GerenciaArquivo.getInstancia().pegarAssinaturas(dtArquivo.conteudoBytes());
					}catch (Exception e) {
						assinante = null;
					}
					
					if (assinante!=null) {
						throw new MensagemException("Arquivo já está assinado");
					}
					dtArquivo.setNomeArquivo(arquivo.getNomeArquivo());
					
					if (arquivo.isGerarAssinatura()){
						UsuarioNe UsuarioSessao;						
						if (request.getSession().getAttribute("UsuarioSessao") != null) {
							UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
							//verifico se o certificado ja está carregado
							if(!UsuarioSessao.isCertificadoCarregado()){
								//se não carrego o certificado
								UsuarioSessao.carregarCertificado();
							} 
							//nesse ponto o certificado já foi carregado pefeitamente, ou uma exception foi lançada
							if (arquivo.temSenhaCertificado() ) {
								UsuarioSessao.setSenhaCertificado(arquivo.getSenhaCertificado());
							}
							//assino o arquivo e incluo o assinante e preparo o .p7s
							UsuarioSessao.assinarByte(dtArquivo);
							//se não é para salvar o cetificado eu limpo
							if (!arquivo.isSalvarSenha()) {
								UsuarioSessao.setSenhaCertificado("");
							}
							
						} else {
							throw new MensagemException("Sem Login");
						}
					}					
					
				}
			} else {//Se o arquivo nao for assinado
				throw new MensagemException("Arquivo sem conteudo, por favor digite o conteudo deste arquivo ou verifique se o arquivo está vazio.");
			}

			//Validação para saber se o arquivo tem acesso liberado, está formatado corretamente e se pode ser convertido em PDF
			arquivoNe.validarArquivoInseridoProcesso(dtArquivo);
						
			//coloco o arquivo no map na sessão para ser utilizados nas pendencia/movimentações				
			mapArquivosSessao.put(getNextId(request), dtArquivo);
		} else {
			throw new MensagemException("É necessário selecionar um Tipo de Arquivo");
		}
				
	}
		
//	public void setaAbaJQuery(HttpServletRequest request){				
//		request.getSession().setAttribute("ultimaAba", 1);
////		limpaArquivos(request);
//	}
	
//	public void submeteArquivoAssinado(HttpServletRequest request) throws Exception{		
//		
//		List<ArquivoDt> arquivos = (List<ArquivoDt>) request.getSession().getAttribute("ArquivoAssinadoCompleto");
//		for(ArquivoDt arquivo:arquivos)
//			setArquivo(arquivo, request);
//		limpaArquivos(request);
//	}
	
		
//	private void limpaArquivos(HttpServletRequest req){
//		req.getSession().removeAttribute("ArquivoAssinadoCompleto");
//		req.setAttribute("nomeArquivo",null);
//		req.setAttribute("Arquivo",null);
//		req.setAttribute("ArquivoBytes",null);
//		req.setAttribute("arquivoAssinado",null);
//	}
	
//	public void setArquivoAssinado(ArquivoDt arquivo, HttpServletRequest request) throws Exception{		
//				
//		List<ArquivoDt> arquivos = (List<ArquivoDt>) request.getSession().getAttribute("ArquivoAssinadoCompleto");
//		if (arquivos == null){
//			arquivos = new ArrayList<ArquivoDt>();
//			request.getSession().setAttribute("ArquivoAssinadoCompleto", arquivos);
//		}
//		arquivos.add(arquivo);
//	}

	/**
	 * Retorna próximo id
	 * @return
	 * @throws Exception
	 */
	private String getNextId(HttpServletRequest request ) throws Exception{	
		int inId = 0;
		if( request.getSession().getAttribute("Id_ListaArquivosDwr") != null){
			inId = (int) request.getSession().getAttribute("Id_ListaArquivosDwr");
		}			
		inId++;
		request.getSession().setAttribute("Id_ListaArquivosDwr", inId);

		return String.valueOf(inId);
	}

	/**
	 * Exclui um arquivo selecionado pelo usuário.
	 * Remove da lista do DWR (Set) e também do Map
	 * @param indice_lista
	 * @throws Exception
	 */
	public void excluir(String indice_lista, HttpServletRequest request) throws Exception{
		
		//Map listaSessao = getLista("ListaArquivos",request);
		Map mapArquivosSessao = getMapArquivosSessao("ListaArquivos",request);
		
		mapArquivosSessao.remove(indice_lista);
		//listaSessao.remove(indice_lista);
	}	

	public int Permissao() {
		return MovimentacaoArquivoDt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual)	throws Exception {
		int inFluxo = 0;
		ArquivoDt arq = null;
		if(request.getParameter("fluxo") != null) inFluxo = Funcoes.StringToInt( request.getParameter("fluxo").toString());
		
		switch (paginaatual) {
			case Configuracao.LocalizarDWR:{
				
				 switch (inFluxo) {
					case LIMPAR_AREA_TRANSFERENCIA:
						AjudanteArquivosAreaTransferencia.limparAreaTransferencia(request);
						break;
					case INCLUIR_ARQUIVO_AREA_TRANSFERENCIA:						
						// Obtem o arquivo da area de transferência
						Map listaSessaoArquivosAreaTransferencia = (Map) AjudanteArquivosAreaTransferencia.getListaArquivosAreaTransferencia(request);
								
						if (listaSessaoArquivosAreaTransferencia == null || listaSessaoArquivosAreaTransferencia.size() == 0) 
							throw new MensagemException("Erro ao inserir arquivos: Não existem arquivos na area de transferência");	
						
						for(Object arquivoObject : listaSessaoArquivosAreaTransferencia.values()){
							ArquivoDt arquivoSessao = (ArquivoDt) arquivoObject;			
							ArquivoDt arquivoParaTransferencia = AjudanteArquivosAreaTransferencia.obtenhaArquivoParaTransferencia(arquivoSessao.getId());
							setArquivo(arquivoParaTransferencia, request);
						}	
						return ;	
//					case LIMPAR_ARQUIVOS_ASSINADOS:
//						limpaArquivos(request);																								
//						break;
					case SET_ARQUIVO:										
						arq = lerArquivoResquest(request);
						setArquivo(arq,request);
						ArquivoDt arquivoEmenta = lerArquivoResquestEmenta(request);
						if (arquivoEmenta != null) {
							setArquivo(arquivoEmenta,request);
						}
						break;
//					case SET_ABA:
//						setaAbaJQuery(request);
//						break;
//					case SUBMENTER_ARQUIVOS_ASSINADOS:
//						submeteArquivoAssinado(request);
//						break;
//					case CANCELAR_ARQUIVO_ASSINADO:
//						limpaArquivos(request);
//						break;
					case EXCLUIR_ARQUIVO:						
						String stIndiceArquivoSessao =  request.getParameter("id_list").toString();
						excluir(stIndiceArquivoSessao, request);
						break;
					case GET_TODOS_ARQUIVOS:
						getTodosArquivos(request,response);
						break;
//					case SET_ARQUIVO_ASSINADO:
//						setArquivoAssinado(arq, request);	
//						break;
					case ASSINAR_HTML_SESSION:
						arq = lerArquivoResquest(request);
						arq.setArquivo((String) request.getSession().getAttribute(arq.getArquivo()));
						setArquivo(arq,request);		
						break;
					default:
						break;
				 }
				
			}		
			default:
				return;
		}
		
	}
	
	private ArquivoDt lerArquivoResquestEmenta(HttpServletRequest request) {
		if (request.getParameter("id_ArquivoTipoEmenta") == null || request.getParameter("id_ArquivoTipoEmenta").trim().length() == 0) return null;
		return lerArquivoResquest(request, "id_ArquivoTipoEmenta", "arquivoTipoEmenta", "nomeArquivoEmenta", "arquivoEmenta");
	}
	
	private ArquivoDt lerArquivoResquest(HttpServletRequest request) {
		return lerArquivoResquest(request, "id_ArquivoTipo", "arquivoTipo", "nomeArquivo", "arquivo");
	}
	
	private ArquivoDt lerArquivoResquest(HttpServletRequest request, 
			                             String param_id_ArquivoTipo,
			                             String param_arquivoTipo,
			                             String param_nomeArquivo,
			                             String param_arquivo) {
		ArquivoDt arq = new ArquivoDt();
		arq.setId(request.getParameter("id"));
		arq.setId_ArquivoTipo(request.getParameter(param_id_ArquivoTipo));
		arq.setArquivoTipo(request.getParameter(param_arquivoTipo));
		arq.setNomeArquivo(request.getParameter(param_nomeArquivo));
		arq.setArquivo(request.getParameter(param_arquivo));
		arq.setAssinado((String)request.getParameter("assinado"));
		arq.setGerarAssinatura(Funcoes.StringToBoolean((String)request.getParameter("gerarAssinatura")));
		arq.setSenhaCertificado((String)request.getParameter("senhaCertificado"));
		arq.setContentType((String)request.getParameter("contentType"));
		arq.setSalvarSenha(Funcoes.StringToBoolean((String)request.getParameter("salvarSenha")));
		return arq;
	}

	private void getTodosArquivos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, ArquivoDt> mapArquivosSessao = getMapArquivosSessao("ListaArquivos",request);
				
		StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");		
		boolean vigula=false;
		
		for(String indice : mapArquivosSessao.keySet()) {
			ArquivoDt arq = mapArquivosSessao.get(indice);
			if (!vigula){
				stTemp.append(arq.getJSON_ASSINATURA(indice));
				vigula=true;
			}else{
				stTemp.append(',').append(arq.getJSON_ASSINATURA(indice));
			}
		}
		
		stTemp.append("]");
		
		enviarJSON(response, stTemp.toString());		

		return;
			
	}
		
	
}