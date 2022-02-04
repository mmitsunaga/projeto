package br.gov.go.tj.projudi.ct;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.MidiaPublicadaArquivoDt;
import br.gov.go.tj.projudi.dt.MidiaPublicadaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.MidiaPublicadaNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

/**
 * Servlet responsável em controlar o upload de mídias digitais
 * @author mmgomes
 */
public class MidiaPublicadaCt extends Controle {

	private static final long serialVersionUID = -1202448373828988587L;
	
	public int Permissao() {
		return MidiaPublicadaDt.CodigoPermissao;
	}
	
	private static String CONFIGURACOES_PROJUDI = "__ConfiguracoesProjudiCeph__";
	
	protected ProjudiPropriedades getProjudiPropriedades(HttpServletRequest request){
		ProjudiPropriedades propriedades = (ProjudiPropriedades) request.getAttribute(CONFIGURACOES_PROJUDI);
		if(propriedades == null){
			propriedades = ProjudiPropriedades.getInstance();
			request.setAttribute(CONFIGURACOES_PROJUDI, propriedades);
		}
		return propriedades;
	}
	
	private static String PASTA_TEMPORARIA = "__PastaTemporariaCeph__";
	
	protected String getPastaTemporaria(HttpServletRequest request){
		String pastaTemporaria = (String) request.getAttribute(PASTA_TEMPORARIA);
		if(pastaTemporaria == null  || pastaTemporaria.trim().length() == 0){
			ProjudiPropriedades propriedades = getProjudiPropriedades(request);
			pastaTemporaria = propriedades.getObjectStorageUploadPastaTemporaria();
			File filePastaTemporaria = new File(pastaTemporaria);
			if (!filePastaTemporaria.exists()) {
				pastaTemporaria = null;
			}		
			if (pastaTemporaria == null || pastaTemporaria.trim().length() == 0) {
				pastaTemporaria = propriedades.getCaminhoAplicacao();
			}
			request.setAttribute(PASTA_TEMPORARIA, pastaTemporaria);
		}
		return pastaTemporaria;
	}
	
	private static String MIDIA_PUBLICADA_ARQUIVO = "__CurrentMidiaPublicadaArquivoCeph__";
	
	protected MidiaPublicadaArquivoDt getCurrentMidiaPublicadaArquivo(HttpServletRequest request) {
		return (MidiaPublicadaArquivoDt) request.getAttribute(MIDIA_PUBLICADA_ARQUIVO);
	}
	
	protected void setCurrentMidiaPublicadaArquivo(HttpServletRequest request, MidiaPublicadaArquivoDt midiaPublicadaArquivoDt) {
		request.setAttribute(MIDIA_PUBLICADA_ARQUIVO, midiaPublicadaArquivoDt);
	}
	
	private static String MIDIA_PUBLICADA_ARQUIVO_CHUNK_INFO = "__CurrentMidiaPublicadaArquivoChunkInfoCeph__";
	
	protected String getCurrentMidiaPublicadaArquivoChunkInfo(HttpServletRequest request) {
		return (String) request.getAttribute(MIDIA_PUBLICADA_ARQUIVO_CHUNK_INFO);
	}
	
	protected void setCurrentMidiaPublicadaArquivoChunkInfo(HttpServletRequest request, String chunkInfo) {
		request.setAttribute(MIDIA_PUBLICADA_ARQUIVO_CHUNK_INFO, chunkInfo);
	}
	
	@Override
	protected void tratarMultiParte(HttpServletRequest request) throws  MensagemException, UnsupportedEncodingException  {
    	//process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
               
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        String range = request.getHeader("Content-Range");
                        long fileFullLength = -1;
                        long chunkFrom = -1;
                        long chunkTo = -1;
                        if (range != null) {
                            if (!range.startsWith("bytes "))
                                throw new ServletException("Unexpected range format: " + range);
                            String[] fromToAndLength = range.substring(6).split(Pattern.quote("/"));
                            fileFullLength = Long.parseLong(fromToAndLength[1]);
                            String[] fromAndTo = fromToAndLength[0].split(Pattern.quote("-"));
                            chunkFrom = Long.parseLong(fromAndTo[0]);
                            chunkTo = Long.parseLong(fromAndTo[1]);
                        }
                        
                        MidiaPublicadaDt midiaPublicadaDt = getMidiaPublicadaFromSession(request);
                        String fileName = new File(item.getName()).getName();
                                                
                        MidiaPublicadaArquivoDt midiaPublicadaArquivoDt = midiaPublicadaDt.getMidiaPublicadaArquivo(fileName); 
                        if (midiaPublicadaArquivoDt == null) {
                        	midiaPublicadaArquivoDt = midiaPublicadaDt.adicioneMidiaPublicada(getPastaTemporaria(request), fileName, item.getContentType(), fileFullLength);
                        }
                        if (midiaPublicadaArquivoDt != null) {
                        	this.setCurrentMidiaPublicadaArquivo(request, midiaPublicadaArquivoDt);
                        	File file = new File(midiaPublicadaArquivoDt.getCaminhoCompletoArquivoTemp());
                            
                        	if (chunkFrom == -1) {
                        		if (file.exists()) file.delete();
                           	 	midiaPublicadaArquivoDt.setUploadCompleto(true);
                           	 	midiaPublicadaArquivoDt.setTamanhoArquivo(item.get().length);
                        	} else if (chunkFrom == 0) {
                            	 //System.out.println("Primeira parte do arquivo " + name);
                            	 if (file.exists()) file.delete();
                            	 midiaPublicadaArquivoDt.setUploadCompleto(false);
                            } else if ((chunkTo + 1) == fileFullLength) {
                            	midiaPublicadaArquivoDt.setUploadCompleto(true);
                            	//System.out.println("Última parte do arquivo " + name);
                            }                        
                            
                            FileOutputStream fos = new FileOutputStream(file, true);
                            
                            fos.write(item.get());
                            
                            fos.close();   
                            
                            String chunkInfoCurrentFile = String.format("Arquivo %s enviado com sucesso. Tamanho total: %s bytes. Parte de : %s bytes. Parte até : %s bytes.", fileName, fileFullLength, chunkFrom, chunkTo);
                            setCurrentMidiaPublicadaArquivoChunkInfo(request, chunkInfoCurrentFile);
                            //System.out.println(chunkInfoCurrentFile);	
                        }
                    }
                }
                          
               request.setAttribute("message", "File Uploaded Successfully");               
            } catch (Exception ex) {
               request.setAttribute("message", "File Upload Failed due to " + ex);
               System.out.println("File Upload Failed");
               System.out.println(ex);
            }
        }
	}
	
	private MidiaPublicadaDt getMidiaPublicadaFromSession(HttpServletRequest request)  {
		MidiaPublicadaDt midiaPublicadaDt = (MidiaPublicadaDt) request.getSession().getAttribute("midiaPublicadaDt");
		if (midiaPublicadaDt == null) {
			midiaPublicadaDt = new MidiaPublicadaDt();
			request.getSession().setAttribute("midiaPublicadaDt", midiaPublicadaDt);
		}
		return midiaPublicadaDt;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		ProcessoDt processoDt;
		MidiaPublicadaNe midiaPublicadaNe;
		MidiaPublicadaDt midiaPublicadaDt = getMidiaPublicadaFromSession(request);
		String stAcao = "/WEB-INF/jsptjgo/MidiaPublicada.jsp";
		String mensagem = "";
				
		request.setAttribute("tempRetorno", "MidiaPublicada");
		request.setAttribute("tempPrograma", "MidiaPublicada");

		midiaPublicadaNe = (MidiaPublicadaNe) request.getSession().getAttribute("midiaPublicadaNe");
		if (midiaPublicadaNe == null) midiaPublicadaNe = new MidiaPublicadaNe();
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		//se não houver um processo na sessão ocorreu algum problema assim retorno para pagina inicial
		if (processoDt==null){
			redireciona(response, "Usuario?PaginaAtual=-10");
			return;			
		}

		midiaPublicadaDt.setProcesso(processoDt);
		midiaPublicadaDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
		midiaPublicadaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		midiaPublicadaDt.setComplemento(request.getParameter("MovimentacaoComplemento"));
				
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {		
			case Configuracao.Novo:
				midiaPublicadaDt.LimparMidias();
				midiaPublicadaDt = new MidiaPublicadaDt();				
				break;

			case Configuracao.Salvar:
				mensagem = "";
				if (request.getParameter("DataRealizacao") == null || request.getParameter("DataRealizacao").trim().length() == 0) {
					mensagem += "Data deve ser informada.";
				} else if (!Funcoes.validaData(request.getParameter("DataRealizacao"))) {
					mensagem += "Data inválida.";
				}
				
				if (request.getParameter("HoraRealizacao") == null || request.getParameter("HoraRealizacao").trim().length() == 0) {
					mensagem += "Hora deve ser informada.";
				} else if (!Funcoes.validaHora(request.getParameter("HoraRealizacao"))) {
					mensagem += "Hora inválida.";
				}				
				if (mensagem.length() == 0) {
					TJDataHora dataHoraInformada = new TJDataHora(request.getParameter("DataRealizacao"), request.getParameter("HoraRealizacao") + ":00");
					midiaPublicadaDt.setDataHora(dataHoraInformada);
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("__Pedido__", request.getAttribute("__Pedido__").toString());
					jsonObject.put("sucesso", "sucesso");
					super.montaRetornoJSON(response, jsonObject.toString());					
				} else {
					super.montaRetornoJSON(response, "erro", mensagem);
				}	
				return;

			case Configuracao.Curinga7:	
				String urlAssinada = midiaPublicadaNe.gerarURLCeph(midiaPublicadaDt, request.getParameter("nomeArquivo"), request.getParameter("contentType"));
				super.montaRetornoJSON(response, "urlCephAssinada", urlAssinada);
				return;
				
			case Configuracao.Curinga9:	
				MidiaPublicadaArquivoDt midiaPublicadaArquivoDt = this.getCurrentMidiaPublicadaArquivo(request);
				
				if (midiaPublicadaArquivoDt != null &&
					midiaPublicadaArquivoDt.isUploadCompleto() && 
					!midiaPublicadaArquivoDt.isArquivoEnviadoCeph() &&
					!midiaPublicadaArquivoDt.isErro()) {
						
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", midiaPublicadaArquivoDt.getNomeArquivoOriginal());
					jsonObject.put("size", midiaPublicadaArquivoDt.getTamanhoArquivo());
                    try {
						String urlDownload = midiaPublicadaNe.enviarMidiaCeph(midiaPublicadaDt, midiaPublicadaArquivoDt);
						jsonObject.put("url", urlDownload.toString());
						jsonObject.put("sucesso", "true");
					} catch (Exception e) {
						midiaPublicadaArquivoDt.setMensagemErro(Funcoes.obtenhaConteudoExcecao(e));
						jsonObject.put("error", e.getMessage());
						jsonObject.put("sucesso", "false");
					}						
					
					JSONArray jsonArray = new JSONArray();
					jsonArray.put(jsonObject);
					
					JSONObject jsonObjectFiles = new JSONObject();
					jsonObjectFiles.put("files", jsonArray);
											
					super.enviarJSON(response, jsonObjectFiles.toString());
					return;
				}		
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("sucesso", "sucesso");
				jsonObject.put("info", getCurrentMidiaPublicadaArquivoChunkInfo(request));
				super.enviarJSON(response, jsonObject.toString());
				return;
				
			case Configuracao.SalvarResultado:
				try {
					midiaPublicadaNe.gerarMovimentacaoMidiaPublicada(midiaPublicadaDt, UsuarioSessao.getUsuarioDt());
					super.montaRetornoSucessoJSON(response);
				} catch (MensagemException e) {
					super.montaRetornoMensagemJSON(response, e.getMessage());
				}				
				return;			
		}

		request.getSession().setAttribute("midiaPublicadaDt", midiaPublicadaDt);
		request.getSession().setAttribute("midiaPublicadaNe", midiaPublicadaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}