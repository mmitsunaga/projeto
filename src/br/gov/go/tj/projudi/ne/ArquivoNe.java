package br.gov.go.tj.projudi.ne;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.ps.ArquivoPs;
import br.gov.go.tj.projudi.util.GerarCabecalhoProcessoPDF;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverteImagemPDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

//---------------------------------------------------------
public class ArquivoNe extends ArquivoNeGen {

	//
	private static final long serialVersionUID = -3202248671433179296L;
	
	//---------------------------------------------------------
	public String Verificar(ArquivoDt dados) {

		String stRetorno = "";

		if (dados.getNomeArquivo().length() == 0) stRetorno += "O Campo NomeArquivo é obrigatório.";
		if (dados.getArquivoTipo().length() == 0) stRetorno += "O Campo ArquivoTipo é obrigatório.";
		if (dados.getContentType().length() == 0) stRetorno += "O Campo ContentType é obrigatório.";
		if (dados.getArquivo().length() == 0) stRetorno += "O Campo Arquivo é obrigatório.";
		if (dados.getCaminho().length() == 0) stRetorno += "O Campo Caminho é obrigatório.";
		if (dados.getDataInsercao().length() == 0) stRetorno += "O Campo DataInsercao é obrigatório.";
		if (dados.getUsuarioAssinador().length() == 0) stRetorno += "O Campo UsuarioAssinador é obrigatório.";
		if (dados.getArquivoTipoCodigo().length() == 0) stRetorno += "O Campo ArquivoTipoCodigo é obrigatório.";
		////System.out.println("..neArquivoVerificar()");
		return stRetorno;

	}

	/**
	 * Consultar arquivo em uma transacao
	 * @author Ronneesley Moura Teles
	 * @since 05/02/2009 09:50
	 * @param id_arquivo id do arquivo
	 * @param conexao conexao para continuar a transacao
	 * @return vo de arquivo
	 * @throws Exception
	 */
	public ArquivoDt consultarId(String id_arquivo, Connection conexao) throws Exception{
		ArquivoDt dtRetorno = null;
		
		ArquivoPs obPersistencia = new ArquivoPs(conexao);
		dtRetorno = obPersistencia.consultarId(id_arquivo);
		//atualizeArquivoObjectStorageProjudiCopiar(dtRetorno);
		
		return dtRetorno;
	}

	/**
	 * 	Inserir um novo arquivo com uma nova transacao
	 * @author Ronneesley Moura Teles
	 * @since 08/09/2008 15:09 
	 * @param ArquivoDt arquivoDt, pojo do arquivo
	 * @throws Exception
	 */
	public void inserir(ArquivoDt arquivoDt, LogDt logDt) throws Exception{
		this.inserir(arquivoDt, logDt, null);
	}

	/**
	 *  Inserir um novo arquivo utilizando-se de uma transacao corrente, caso nao
	 * tenha sido criada, sera criado no metodo a transacao
	 * @author Ronneesley Moura Teles
	 * @since 08/09/2008 15:08
	 * @param ArquivoDt arquivoDt, pojo a ser inserido
	 * @param FabricaConexao fabConexao, fabrica de conexoes
	 * @return void
	 * @throws Exception
	 */
	public void inserir(ArquivoDt arquivoDt, LogDt logDt, FabricaConexao fabConexao) throws Exception{
	    FabricaConexao obFabricaConexao = null;
	    
	    try{
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabConexao;
			}

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			
			// Se o arquivo já tem id não é necessário salvar novamente
			if (arquivoDt.getId().equalsIgnoreCase("")) {
				if(arquivoDt.getId_UsuarioLog() == null || arquivoDt.getId_UsuarioLog().equals("")){
					arquivoDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
				}
				if(arquivoDt.getIpComputadorLog() == null || arquivoDt.getIpComputadorLog().equals("")){
					arquivoDt.setIpComputadorLog(logDt.getIpComputadorLog());
				}
				
				obPersistencia.inserir(arquivoDt);
												
				LogNe logNe = new LogNe();
				logNe.salvar(new LogDt("Arquivo", arquivoDt.getId(), arquivoDt.getId_UsuarioLog(), arquivoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", arquivoDt.getPropriedades()),obFabricaConexao);
				
				//o conteudo do arquivo é para o ceph
				if (arquivoDt.isSalvarCeph()) {					
					salvarArquivoTransacaoCeph(arquivoDt);											
				}
				
			} else if (arquivoDt.conteudoBytes() == null) {
				arquivoDt.setArquivo(obPersistencia.consultarConteudoArquivo(arquivoDt));
			}			
		
		} finally{
			if (fabConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	 /**
	 * Método que verifica se um usuário pode baixar um arquivo, e caso possa efetua o download
	 * OBS: Este metodo restringue esta camada de negocio ao servlet
	 * @author Marielli
	 * @author Ronneesley Moura Teles
	 * @param String id_Arquivo, id do arquivo
	 * @param HttpServletResponse response, response do servlet
	 * @param LogDt logDt, pojo de log 
	 * @throws Exception
	 */
	public void baixarArquivo(String id_Arquivo, HttpServletResponse response, LogDt logDt, boolean ehConsultaPublica) throws Exception{
		//Consulta o arquivo
		ArquivoDt arquivoDt = this.consultarId(id_Arquivo, ehConsultaPublica);
		URL urlAssinada = null;

		//se o arquivo for fisico, ou melhor se não estiver no banco
		if (arquivoDt.isArquivoFisico()) {
			if (arquivoDt.isMidiaDigitalUpload()) {
				urlAssinada = getUrlAssinadaBaixarArquivoObjectStorageMidiaDigitalUpload(arquivoDt);
			} else if (!arquivoDt.isArquivoObjectStorageProjudi()) {
				urlAssinada = getUrlAssinadaBaixarArquivoObjectStorageDigitalizacao(arquivoDt);
			}
		} 
		
		if (urlAssinada != null) {
			response.sendRedirect(urlAssinada.toString());
		} else {
			baixarArquvio(arquivoDt, response);
		}
		
		//Grava o log da requisicao
		LogNe logNe = new LogNe();
		logNe.salvar(new LogDt("Arquivo", arquivoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", arquivoDt.getPropriedades()));
	}
	
	private void baixarArquvio(ArquivoDt arquivoDt, HttpServletResponse response) throws Exception {
	
		//Inicilaiza os objetos
		CMSSignedData dados = null;
		//Se o arquivo e assinado
		if (arquivoDt.isArquivoAssinado()) {
			
			if (arquivoDt.conteudoBytes() == null){
				throw new MensagemException("Arquivo corrompido! Por favor insira o arquivo novamente!");
			}
			
			InputStream inputStream = new ByteArrayInputStream(arquivoDt.conteudoBytes());

			//Verifica se e recibo
			if (arquivoDt.getRecibo().trim().equals("true")) {
				//Extrai arquivo do recibo 
				dados = Signer.extrairConteudoRecibo(inputStream);
			} else {
				dados = new CMSSignedData(arquivoDt.conteudoBytes());
			}

			//Fecha o input stream
			inputStream.close();
		}
		//}

		DataOutputStream dos = null;
		try{
			//Modifica o cabecalho
			response.setContentType(arquivoDt.getContentType());
			//response.setHeader("Content-disposition", "attachment; filename=\"" + arquivoDt.getNomeArquivoFormatado() + "\"");
			response.setHeader("Content-disposition", "filename=\"" + arquivoDt.getNomeArquivoFormatado() + "\"");
			if (arquivoDt.getContentType().equals("text/html")) {
				response.setCharacterEncoding("iso-8859-1");
			}
			
			//Se o arquivo e assinado
			if (arquivoDt.isArquivoAssinado()) {

				//Retira o conteudo do arquivo assinado
				CMSProcessable conteudo = dados.getSignedContent();

				//Escreve o conteudo na saida do servlet				
				conteudo.write(response.getOutputStream());
												
				//System.out.println("WARNING : Occoreu um erro de rede no envio do arquivo PDF.");				
			} else {
				// Extrair arquivo não assinado de um recibo e-carta
				if (arquivoDt.isRecibo()) {
					InputStream inputStream = new ByteArrayInputStream(arquivoDt.conteudoBytes());
					dos = new DataOutputStream(response.getOutputStream());
					dos.write(Signer.extrairConteudoReciboArquivoNaoAssinado(inputStream));
					dos.close();
				} else {
					dos = new DataOutputStream(response.getOutputStream());
					dos.write(arquivoDt.conteudoBytes());
					dos.close();
				}
				
			}
		} catch(Exception e) {
			try{if (dos!=null) dos.close(); } catch(Exception ex) {}
			//se ouver erro no envio do arquivo não há o que fazer, então só retorno
			return;
		}
	}
	
	/**
	 * Helleno
	 * @param id_Arquivo
	 * @param logDt
	 * @return
	 * @throws Exception
	 */
	public String retornarConteudoArquivo(String id_Arquivo, LogDt logDt) throws Exception{
		//Consulta o arquivo
		ArquivoDt arquivoDt = this.consultarId(id_Arquivo, false);
		
		//Inicializa os objetos
		CMSSignedData dados = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		if (arquivoDt.isArquivoAssinado()) {
			InputStream inputStream = new ByteArrayInputStream(arquivoDt.conteudoBytes());

			//Verifica se e recibo
			if (arquivoDt.getRecibo().trim().equals("true")) {
				//Extrai arquivo do recibo 
				dados = Signer.extrairConteudoRecibo(inputStream);
			} else {
				dados = new CMSSignedData(arquivoDt.conteudoBytes());
			}

			//Fecha o input stream
			inputStream.close();
		}

		DataOutputStream dos = null;
		try{
			//Se o arquivo e assinado
			if (arquivoDt.isArquivoAssinado()) {

				//Verifica a integridade da assinatura e existência de vírus
				// GerenciaArquivo.getInstancia().VerificaAssinatura(dados);
				// GerenciaArquivo.getInstancia().VerificaVirus(dados);

				//Retira o conteudo do arquivo assinado
				CMSProcessable conteudo = dados.getSignedContent();

				//Escreve o conteudo na saida do servlet
				
				conteudo.write(baos);
								
				
				//System.out.println("WARNING : Occoreu um erro de rede no envio do arquivo PDF.");				
			} else {
									
				dos = new DataOutputStream(baos);
				dos.write(arquivoDt.conteudoBytes());
				dos.close();					
				
			}
		}	catch(Exception e) {
			try{if (dos!=null) dos.close(); } catch(Exception ex) {}
			//se ouver erro no envio do arquivo não há o que fazer, então só retorno
			return null;
		}

		//Grava o log da requisicao
		LogNe logNe = new LogNe();
		logNe.salvar(new LogDt("Arquivo", arquivoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", arquivoDt.getPropriedades()));
		return baos.toString();
	}
	
	/**
	 * Método que verifica se um usuário pode baixar um arquivo, e caso possa efetua o download
	 * OBS: Este metodo restringue esta camada de negocio ao servlet
	 * @author Márcio Mendonça Gomes
	 * @param String id_Arquivo, id do arquivo
	 * @param HttpServletResponse response, response do servlet
	 * @param LogDt logDt, pojo de log 
	 * @throws Exception
	 */
	public void baixarArquivoFileSystem(String id_Arquivo, HttpServletResponse response, LogDt logDt, boolean ehConsultaPublica) throws Exception{
		//Consulta o arquivo
		ArquivoDt arquivoDt = this.consultarId(id_Arquivo, ehConsultaPublica);

		//Modifica o cabecalho
		response.setContentType(arquivoDt.getContentType());
		response.setHeader("Content-disposition", "filename=\"" + arquivoDt.getNomeArquivoFormatado() + "\"");

		BufferedInputStream bis = null;
		try {
			  
              byte[] barray = new byte[1024];
              int read = 0;
              
              bis = new BufferedInputStream(new FileInputStream(arquivoDt.getCaminho())); 
              
              response.addCookie(new Cookie("fileDownload", "true")); 
              
              while ((read = bis.read(barray)) != -1){
                  response.getOutputStream().write(barray,0, read);
              }
              
              bis.close();  
		} catch(Exception e) {
			try{if (bis!=null) bis.close(); } catch(Exception ex) {}
			//se ouver erro no envio do arquivo não há o que fazer, então só retorno
			return;
		}

		//Grava o log da requisicao
		LogNe logNe = new LogNe();
		logNe.salvar(new LogDt("Arquivo", arquivoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", arquivoDt.getPropriedades()));
	}
	
	private URL getUrlAssinadaBaixarArquivoObjectStorageDigitalizacao(ArquivoDt arquivoDt)  {
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		
		String bucket = projudiConfiguration.getObjectStorageBucketDigitalizacao(); 
		
		String pathArquivo = arquivoDt.getCaminho();
		
		if (pathArquivo != null && pathArquivo.indexOf("/") != 3) {
			pathArquivo = Funcoes.obtenhaSomenteNumeros(pathArquivo).substring(9, 13) + "/" + Funcoes.obtenhaSomenteNumeros(pathArquivo).substring(0, 3) + "/" + pathArquivo;
		}
		
		AmazonS3 conn = obtenhaConexaoObjectStorageDigitalizacao(projudiConfiguration, false);
		
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket, pathArquivo);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 2);
        Date expirationDate = c.getTime();
        urlRequest.setExpiration(expirationDate);
                
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
	    responseHeaders.setContentDisposition("filename=" + arquivoDt.getNomeArquivo());
	    urlRequest.setResponseHeaders(responseHeaders);
        
        return conn.generatePresignedUrl(urlRequest);
	}
	
	private URL getUrlAssinadaBaixarArquivoObjectStorageMidiaDigitalUpload(ArquivoDt arquivoDt) throws MensagemException  {
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		
		int indiceInicioBucket = arquivoDt.getCaminho().indexOf("[");
		int indiceFinalBucket = arquivoDt.getCaminho().indexOf("]");
		
		if (indiceInicioBucket < 0 || indiceFinalBucket < 0 || indiceFinalBucket <= indiceInicioBucket) 
			throw new MensagemException("Caminho da mídia digital inválido");
		
		String bucket = arquivoDt.getCaminho().substring(indiceInicioBucket + 1, indiceFinalBucket);
		String pathArquivo = arquivoDt.getCaminho().substring(indiceFinalBucket + 1);
		
		AmazonS3 conn = obtenhaConexaoObjectStorageMidiaDigitalUpload(projudiConfiguration, false);
		
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket, pathArquivo);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 5);
        Date expirationDate = c.getTime();
        urlRequest.setExpiration(expirationDate);
                
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
	    responseHeaders.setContentDisposition("filename=" + arquivoDt.getNomeArquivo());
	    urlRequest.setResponseHeaders(responseHeaders);
        
        return conn.generatePresignedUrl(urlRequest);        
	}
	
	private AmazonS3 obtenhaConexaoObjectStorageMidiaDigitalUpload(ProjudiPropriedades projudiPropriedades, boolean setSignerOverride) {
		String accessKey = projudiPropriedades.getObjectStorageUploadAccessKey();
		String secretKey = projudiPropriedades.getObjectStorageUploadSecretKey();
		String host = projudiPropriedades.getObjectStorageHost();
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
    	ClientConfiguration clientConfig = new ClientConfiguration();
    	clientConfig.setProtocol(Protocol.HTTP); 
    	if (setSignerOverride) {
			clientConfig.setSignerOverride("S3SignerType");	
		}		
		
    	AmazonS3 s3Client = new AmazonS3Client(awsCreds, clientConfig);
		
		S3ClientOptions s3ClientOptions = new S3ClientOptions();
		s3Client.setS3ClientOptions(s3ClientOptions);
		s3Client.setEndpoint(host);
		
		return s3Client;
	}
	
	
//	private URL getUrlAssinadaBaixarArquivoObjectStorageProjudi(ArquivoDt arquivoDt) throws MensagemException  {
//		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
//		
//		int indiceInicioBucket = arquivoDt.getCaminho().indexOf("[");
//		int indiceFinalBucket = arquivoDt.getCaminho().indexOf("]");
//		
//		if (indiceInicioBucket < 0 || indiceFinalBucket < 0 || indiceFinalBucket <= indiceInicioBucket) 
//			throw new MensagemException("Caminho da mídia digital inválido");
//		
//		String bucket = arquivoDt.getCaminho().substring(indiceInicioBucket + 1, indiceFinalBucket);
//		String pathArquivo = arquivoDt.getCaminho().substring(indiceFinalBucket + 1);
//		
//		AmazonS3 conn = obtenhaConexaoObjectStorageProjudi(projudiConfiguration, false);
//		
//        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket, pathArquivo);
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.MINUTE, 5);
//        Date expirationDate = c.getTime();
//        urlRequest.setExpiration(expirationDate);
//                
//		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
//	    responseHeaders.setContentDisposition("filename=" + arquivoDt.getNomeArquivo());
//	    urlRequest.setResponseHeaders(responseHeaders);
//        
//        return conn.generatePresignedUrl(urlRequest);        
//	}
			
	/**
	 * Método que verifica se um usuário pode baixar um arquivo, e caso possa efetua o download
	 * OBS: Este metodo restringue esta camada de negocio ao servlet
	 * @author Márcio Mendonça Gomes
	 * @param String numeroProcessoCompleto, número completo do processo
	 * @param HttpServletResponse response, response do servlet
	 * @throws Exception
	 */
	public void baixarArquivoCompletoObjectStorageDigitalizacao(String numeroProcessoCompleto, HttpServletResponse response) throws Exception{
		
		String pathArquivo = Funcoes.obtenhaSomenteNumeros(numeroProcessoCompleto) + "/00000-completo.pdf";
		
        String urlAssinada = obtenhaURLAssinaBaixarArquivoObjectStorageDigitalizacao(pathArquivo);
		
		response.sendRedirect(urlAssinada);
	}
	
	/**
	 * @author Márcio Mendonça Gomes
	 * @param String numeroProcessoCompleto, número do processo
	 * @param String nomeCompletoDoArquivo, nome do arquivo no Object Storage
	 * @param HttpServletResponse response, response do servlet
	 * @throws Exception
	 */
	public void baixarArquivoObjectStorageDigitalizacao(String numeroProcessoCompleto, String nomeCompletoDoArquivo, HttpServletResponse response) throws Exception{
		String pathArquivo = Funcoes.obtenhaSomenteNumeros(numeroProcessoCompleto) + "/" + nomeCompletoDoArquivo;
		
		String prefixoPath = "";		
		if (pathArquivo != null && pathArquivo.indexOf("/") != 3) {
			prefixoPath = Funcoes.obtenhaSomenteNumeros(pathArquivo).substring(9, 13) + "/" + Funcoes.obtenhaSomenteNumeros(pathArquivo).substring(0, 3) + "/";
		}
		
		String urlAssinada = null;
		if (!existeArquivoNoObjectStorageDigitalizacao(prefixoPath + pathArquivo)) {			
			String numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento = ObtenhaNumeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento(numeroProcessoCompleto);
			
			if (numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento != null && 
				Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento).length() > 0 && 
				!Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento).equalsIgnoreCase(Funcoes.obtenhaSomenteNumeros(numeroProcessoCompleto))){
				
				pathArquivo = Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento) + nomeCompletoDoArquivo;				
			}
			
			if (!existeArquivoNoObjectStorageDigitalizacao(prefixoPath + pathArquivo)) {
				pathArquivo = Funcoes.obtenhaSomenteNumeros(numeroProcessoCompleto) + "/SDM2/" + nomeCompletoDoArquivo;
				if (!existeArquivoNoObjectStorageDigitalizacao(prefixoPath + pathArquivo)) {
					pathArquivo = Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento) + "/SDM2/" + nomeCompletoDoArquivo;					
				}
			}
		}
		
		urlAssinada = obtenhaURLAssinaBaixarArquivoObjectStorageDigitalizacao(pathArquivo);
		
		response.sendRedirect(urlAssinada);
	}
	
	/**
	 * Método que verifica se um usuário pode baixar um arquivo, e caso possa efetua o download
	 * OBS: Este metodo restringue esta camada de negocio ao servlet
	 * @author Márcio Mendonça Gomes
	 * @param String id_Arquivo, id do arquivo
	 * @param HttpServletResponse response, response do servlet
	 * @param LogDt logDt, pojo de log 
	 * @oaram boolean ehConsultaPublica, indicador se é consulta pública
	 * @throws Exception
	 */
	public void baixarArquivoObjectStorageWebServiceDigitalizacao(String id_Arquivo, HttpServletResponse response, LogDt logDt, boolean ehConsultaPublica) throws Exception{
		//Consulta o arquivo
		ArquivoDt arquivoDt = this.consultarId(id_Arquivo, ehConsultaPublica);
		
		InputStream in = obtenhaStreamObjectStorageDigitalizacao(arquivoDt.getCaminho());	  
        
        //Modifica o cabecalho
  		response.setContentType(arquivoDt.getContentType());
  		response.setHeader("Content-disposition", "filename=\"" + arquivoDt.getNomeArquivoFormatado() + "\"");

  		BufferedInputStream bis = null;
  		try {  			  
            byte[] barray = new byte[1024];
            int read = 0;
            
            bis = new BufferedInputStream(in); 
            
            response.addCookie(new Cookie("fileDownload", "true")); 
            
            while ((read = bis.read(barray)) != -1){
                response.getOutputStream().write(barray,0, read);
            }
            
            bis.close();  
  		} catch(Exception e) {
  			try{if (bis!=null) bis.close(); } catch(Exception ex) {}
  			//se ouver erro no envio do arquivo não há o que fazer, então só retorno
  			return;
  		} finally {
  			if (in != null) in.close();
  		}
        
        //Grava o log da requisicao
		LogNe logNe = new LogNe();
		logNe.salvar(new LogDt("Arquivo", arquivoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", arquivoDt.getPropriedades()));
	}
	
	/**
	 * Método que verifica se um usuário pode baixar um arquivo, e caso possa efetua o download
	 * OBS: Este metodo restringue esta camada de negocio ao servlet
	 * @author Márcio Mendonça Gomes
	 * @param String nomeArquivo, nome do arquivo
	 * @throws Exception
	 */
	private String obtenhaURLAssinaBaixarArquivoObjectStorageDigitalizacao(String pathArquivo) throws Exception{
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		
		String bucket = projudiConfiguration.getObjectStorageBucketDigitalizacao();
		
		if (pathArquivo != null && pathArquivo.indexOf("/") != 3) {
			pathArquivo = Funcoes.obtenhaSomenteNumeros(pathArquivo).substring(9, 13) + "/" + Funcoes.obtenhaSomenteNumeros(pathArquivo).substring(0, 3) + "/" + pathArquivo;
		}
		
		AmazonS3 conn = obtenhaConexaoObjectStorageDigitalizacao(projudiConfiguration, false);
		
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket, pathArquivo);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 2);
        Date expirationDate = c.getTime();
        urlRequest.setExpiration(expirationDate);

        URL urlAssinada = conn.generatePresignedUrl(urlRequest);
		
		return urlAssinada.toString();
	}
	
	public List<ArquivoDt> consultarArquivosCompletosFisicos(String numeroCompletoDoProcesso) throws Exception {
		return consultarArquivosCompletosFisicos(numeroCompletoDoProcesso, false);
	}
	
	public void validaExistenciaArquivoCompletoFisico(String numeroCompletoDoProcesso) throws Exception {
		consultarArquivosCompletosFisicos(numeroCompletoDoProcesso, true);
	}
	
	private List<ArquivoDt> consultarArquivosCompletosFisicos(String numeroCompletoDoProcesso, boolean lancaException404) throws Exception {
		List<ArquivoDt> lista = consultarArquivosCompletosFisicos(numeroCompletoDoProcesso, "", lancaException404);
		if (lista.size() == 0) {
			lista = consultarArquivosCompletosFisicos(numeroCompletoDoProcesso, "/SDM2", lancaException404);
		}
		return lista;
	}
	
	private List<ArquivoDt> consultarArquivosCompletosFisicos(String numeroCompletoDoProcesso, String prefixo, boolean lancaException404) throws Exception {
		List<ArquivoDt> lista = new ArrayList<ArquivoDt>();
		
		String path = Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcesso) + prefixo + "/completos.json";
		
		if (path != null && path.indexOf("/") != 3) {
			path = Funcoes.obtenhaSomenteNumeros(path).substring(9, 13) + "/" + Funcoes.obtenhaSomenteNumeros(path).substring(0, 3) + "/" + path;
		}
		
		String conteudoMetadataJson = "";		
		try {
			conteudoMetadataJson = obtenhaArquivoObjectStorage(path, lancaException404);
		} catch(MensagemException ex) {
			if (!ex.getMessage().startsWith("Não foi possível localizar no Storage o arquivo")) throw ex;
		}		
		
		if (conteudoMetadataJson != null && conteudoMetadataJson.trim().length() > 0) {
			atualizeListaArquivosPdfCompleto(numeroCompletoDoProcesso, conteudoMetadataJson, lista, prefixo);			
		} else if (existeArquivoNoObjectStorageDigitalizacao(path.replace("completos.json", "00000-completo.pdf"))) {
			atualizeListaArquivoPdfCompleto(numeroCompletoDoProcesso, lista, prefixo);
		} else {			
			String numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento = ObtenhaNumeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento(numeroCompletoDoProcesso);
			if (numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento != null && 
				Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento).length() > 0 && 
				!Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento).equalsIgnoreCase(Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcesso))){
				
				path = Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento) + prefixo + "/completos.json";
				conteudoMetadataJson = obtenhaArquivoObjectStorage(path, lancaException404);
				
				if (conteudoMetadataJson != null && conteudoMetadataJson.trim().length() > 0) {
					atualizeListaArquivosPdfCompleto(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento, conteudoMetadataJson, lista, prefixo);			
				} else if (existeArquivoNoObjectStorageDigitalizacao(path.replace("completos.json", "00000-completo.pdf"))) {
					atualizeListaArquivoPdfCompleto(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento, lista, prefixo);
				}
			} else {
				path = Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento) + prefixo + "/metadata.json";
				conteudoMetadataJson = obtenhaArquivoObjectStorage(path, lancaException404);
				
				if (conteudoMetadataJson != null && conteudoMetadataJson.trim().length() > 0) {
					atualizeListaArquivosPdfMetadata(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento, conteudoMetadataJson, lista, prefixo);			
				} else if (existeArquivoNoObjectStorageDigitalizacao(path.replace("metadata.json", "00000-completo.pdf"))) {
					atualizeListaArquivoPdfMetadata(numeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento, lista, prefixo);
				}
			}
		}
		
		return lista;
	}
	
	private String ObtenhaNumeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento(String numeroCompletoDoProcesso) throws Exception {
		String dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.ObtenhaNumeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento(numeroCompletoDoProcesso);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	private void atualizeListaArquivoPdfCompleto(String numeroCompletoDoProcesso, List<ArquivoDt> lista, String prefixo) throws JSONException {
		ArquivoDt arquivoCompleto = new ArquivoDt();
		arquivoCompleto.setNomeArquivo("00000-completo.pdf");
		arquivoCompleto.setCaminho(numeroCompletoDoProcesso + prefixo + "/" + arquivoCompleto.getNomeArquivo());
		lista.add(arquivoCompleto);
	}
	
	private void atualizeListaArquivosPdfCompleto(String numeroCompletoDoProcesso, String conteudoMetadataJson, List<ArquivoDt> lista, String prefixo) throws JSONException {
		JSONObject obj = new JSONObject(conteudoMetadataJson);
		JSONArray arr = obj.getJSONArray("completo");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject peca = arr.getJSONObject(i);
			
			ArquivoDt arquivoDt = new ArquivoDt();
			arquivoDt.setNomeArquivo(peca.getString("nome"));
			arquivoDt.setCaminho(numeroCompletoDoProcesso + prefixo + "/" + arquivoDt.getNomeArquivo());
			lista.add(arquivoDt);
		}
	}
	
	private void atualizeListaArquivoPdfMetadata(String numeroCompletoDoProcesso, List<ArquivoDt> lista, String prefixo) throws JSONException {
		ArquivoDt arquivoCompleto = new ArquivoDt();
		arquivoCompleto.setNomeArquivo("00000-completo.pdf");
		arquivoCompleto.setCaminho(numeroCompletoDoProcesso + prefixo + "/" + arquivoCompleto.getNomeArquivo());
		lista.add(arquivoCompleto);
	}
	
	private void atualizeListaArquivosPdfMetadata(String numeroCompletoDoProcesso, String conteudoMetadataJson, List<ArquivoDt> lista, String prefixo) throws JSONException {
		JSONObject obj = new JSONObject(conteudoMetadataJson);
		JSONArray arr = obj.getJSONArray("pecas");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject peca = arr.getJSONObject(i);
			
			ArquivoDt arquivoDt = new ArquivoDt();
			arquivoDt.setNomeArquivo(peca.getString("nome"));
			arquivoDt.setCaminho(numeroCompletoDoProcesso + prefixo + "/" + arquivoDt.getNomeArquivo());
			lista.add(arquivoDt);
		}
	}
	
	private boolean existeArquivoNoObjectStorageDigitalizacao(String path) throws IOException, MensagemException {		
		InputStream in = null;
		try {
			in = obtenhaStreamObjectStorageDigitalizacao(path);	
			return true;
		} catch (AmazonS3Exception ex) {
			if (ex.getStatusCode() != 404) throw ex; //Arquivo inexistente
			return false;
		} finally {
			if (in != null) in.close();
		}
	}
	
	private String obtenhaArquivoObjectStorage(String path, boolean lancaException404) throws IOException, MensagemException {		
		InputStream in = null;
		try {
			in = obtenhaStreamObjectStorageDigitalizacao(path);		
			StringWriter writer = new StringWriter();
			IOUtils.copy(in, writer, "UTF-8");
			return writer.toString();
		} catch (AmazonS3Exception ex) {
			if (lancaException404) throw ex;
			if (ex.getStatusCode() != 404) throw ex; //Arquivo inexistente
		} finally {
			if (in != null) in.close();
		}	
		return "";
	}
	
	public InputStream obtenhaStreamObjectStorageDigitalizacao(String path) throws MensagemException {
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		
		String bucket = projudiConfiguration.getObjectStorageBucketDigitalizacao(); 
		
		AmazonS3 conn = obtenhaConexaoObjectStorageDigitalizacao(projudiConfiguration, true);
		
		try {
			return conn.getObject(new GetObjectRequest(bucket, path)).getObjectContent();	
		} catch (Exception ex) {
			throw new MensagemException("Não foi possível localizar no Storage o arquivo " + path + ". Favor entrar em contato com o responsável no departamento de infraestrutura.", ex);
		}
	}
	
	private AmazonS3 obtenhaConexaoObjectStorageDigitalizacao(ProjudiPropriedades projudiConfiguration, boolean setSignerOverride) {
		String accessKey = projudiConfiguration.getObjectStorageAccessKeyDigitalizacao(); 
		String secretKey = projudiConfiguration.getObjectStorageSecretKeyDigitalizacao(); 
		String host = projudiConfiguration.getObjectStorageHost(); 
				
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		clientConfig.setUseGzip(true);
		if (setSignerOverride) {
			clientConfig.setSignerOverride("S3SignerType");	
		}		
		
		AmazonS3 conn = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey), clientConfig);
		S3ClientOptions s3ClientOptions = new S3ClientOptions();
		
		conn.setS3ClientOptions(s3ClientOptions);
		conn.setEndpoint(host);
		
		return conn;
	}
	
	/**
	 * Método que verifica se um usuário pode baixar um recibo, e caso possa efetua o download
	 * OBS: Este metodo restringue esta camada de negocio ao servlet
	 * @author Leandro
	 * @param String id_Arquivo, id do arquivo
	 * @param HttpServletResponse response, response do servlet
	 * @param LogDt logDt, pojo de log 
	 * @param ehConsultaPublica, indica se a consulta é pública
	 * @throws Exception
	 */
	public void baixarRecibo(MovimentacaoDt movimentacaoArquivoDt, String id_arquivo, HttpServletResponse response, LogDt logDt, boolean ehConsultaPublica) throws Exception{
		//Inicilaiza os objetos
		//CMSSignedData dados = null;
	
		//Consulta o arquivo
		ArquivoDt arquivoDt = this.consultarId(id_arquivo, ehConsultaPublica);
		
		
		if (!arquivoDt.isRecibo()) {
			Signer.gerarRecibo(movimentacaoArquivoDt, arquivoDt);
		}

		//Modifica o cabecalho
		response.setContentType("Recibo/Projudi");
		String nome = arquivoDt.getNomeArquivoFormatado();
		response.setHeader("Content-disposition", "attachment; filename=\"" + nome.substring(0, nome.lastIndexOf(".")) + ".ReciboProjudi" + "\"");

		DataOutputStream dos=null;
		try{
			dos = new DataOutputStream(response.getOutputStream());
			dos.write(arquivoDt.conteudoBytes());
			dos.close();
		}
		catch(Exception e) {
			try{if (dos!=null) dos.close(); } catch(Exception ex) {}
			throw new MensagemException("Erro no dowanload do arquivo ou o mesmo foi cancelado pelo usuário");
		}

		//Grava o log da requisicao
		LogNe logNe = new LogNe();
		logNe.salvar(new LogDt("Arquivo", arquivoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", arquivoDt.getPropriedades()));
	}

	/**
	 * @param id_arquivo
	 * @param ehConsultaPublica
	 * @return
	 * @throws Exception
	 */
	public ArquivoDt consultarId(String id_arquivo, boolean ehConsultaPublica) throws Exception {
		ArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.CONSULTA : FabricaConexao.PERSISTENCIA));
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_arquivo);
			//atualizeArquivoObjectStorageProjudiCopiar(dtRetorno);
			obDados.copiar(dtRetorno);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta dados de um arquivo com a opção de descompactar o conteúdo ou não
	 * @param id_arquivo
	 * @param descompactar
	 * @return
	 * @throws Exception
	 */
	public ArquivoDt consultarIdAtivo(String id_arquivo, boolean descompactar, boolean ehConsultaPublica) throws Exception{
		ArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.CONSULTA : FabricaConexao.PERSISTENCIA));
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdAtivo(id_arquivo, descompactar);			
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;		
	}
	
	/**
	 * Consulta dados de um arquivo com a opção de descompactar o conteúdo ou não
	 * @param id_arquivo
	 * @param descompactar
	 * @return
	 * @throws Exception
	 * 09/09/2014
	 * @author jrcorrea
	 */
	public ArquivoDt consultarIdMandadoPrisaoAtivo(String id_arquivo, boolean descompactar, boolean ehConsultaPublica) throws Exception{
		ArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {			
			obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.CONSULTA : FabricaConexao.PERSISTENCIA));
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdMandadoPrisaoAtivo(id_arquivo, descompactar);			
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return dtRetorno;
	}
	
	/**
	 * Consulta o conteúdo de um arquivo com a opção de descompactar o conteúdo ou não
	 * @param id_arquivo
	 * @return ArquivoDt
	 * @throws Exception
	 */
	public byte[] consultarConteudoArquivo(ArquivoDt arquivo) throws Exception{
		return this.consultarConteudoArquivo(arquivo, null);
	}

	/**
	 * Consulta o conteúdo de um arquivo com a opção de descompactar o conteúdo ou não
	 * @param arquivo
	 * @return ArquivoDt
	 * @throws Exception
	 */
	public byte[] consultarConteudoArquivo(ArquivoDt arquivo, FabricaConexao conexao) throws Exception{
		byte[] dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarConteudoArquivo(arquivo);						
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta dados especificos de um arquivo de uma pendencia
	 * @param id_arquivo
	 * @return ArquivoDt
	 * @throws Exception
	 */
	public ArquivoDt consultarArquivoPendenciaId(String id_arquivo) throws Exception{
		ArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_arquivo);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Retorna o arquivo quando nao assinado
	 * @author Ronneesley Moura Teles
	 * @since 30/09/2008 10:06
	 * @param String idArquivo, id do arquivo, cuja o conteudo e desejado
	 * @return ArquivoDt
	 * @throws Exception (Caso o arquivo nao seja assinado ou ocorra algum erro)
	 */
	public ArquivoDt consultarArquivoNaoAssinado(String idArquivo) throws Exception{
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());

			ArquivoDt arquivoDt = obPersistencia.consultarId(idArquivo);

			//Somente se o arquivo nao e assinado retorna o pojo 
			if (!arquivoDt.isArquivoAssinado()) {return arquivoDt; }
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return null;
	}
	
	
	/**
	 * Consulta os arquivos das pendências de um período
	 * @return Lista de ArquivoDt
	 * @throws Exception
	 */
	public List<ArquivoDt> consultarArquivosPendenciaPorData(String dataInicio, String dataFinal, int opcaoPublicacao) throws Exception {
		List<ArquivoDt> lstRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());			
			lstRetorno = obPersistencia.consultarArquivosPendenciaData(dataInicio, dataFinal, opcaoPublicacao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lstRetorno;
	}
	

	/**
	 * Método para salvar Arquivo, que recebe uma conexão como parâmetro
	 * @throws Exception 
	 */
	public void salvar(ArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
						
		LogDt obLogDt = null;
		ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
		
		
		if (dados.getId().equalsIgnoreCase("")) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Arquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("Arquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);		
			
		//o conteudo do arquivo é para o ceph
		if ( dados.isSalvarCeph()) {
			salvarArquivoTransacaoCeph(dados);
		}

	}

	/**
	 * Método para excluir um Arquivo, que recebe uma conexão como parâmetro
	 * Será utilizado par apagar arquivos de usuário
	 * @throws Exception 
	 */
	public void excluir(ArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;

		ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Arquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
		obPersistencia.excluir(dados.getId());
		dados.limpar();
		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * Consultar arquivos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * 
	 * @author msapaula
	 * 
	 * @return List<ArquivoDt>
	 * @throws Exception 
	 */
	public List consultarArquivosMovimentacao(String id_Movimentacao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarArquivosMovimentacao(id_Movimentacao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

//	/**
//	 * Método responsável em salvar arquivo(s) físico(s) vinculados a um processo. 
//	 * @param arquivos: lista de arquivos a serem salvos
//	 * @param processoDt: Dt de processo para que seja montado diretório onde arquivos serão salvos
//	 * @param logDt: objeto de log
//	 * @param obFabricaConexao: conexão
//	 * @author msapaula
//	 * @deprecated
//	 */
//	public void inserirArquivo(List arquivos, ProcessoDt processoDt, LogDt logDt, boolean multiplo, FabricaConexao obFabricaConexao) throws Exception {
//		String caminho = null;
//
//	//Monta diretório onde arquivos serão salvos
//	if (multiplo) caminho = Diretorios.getDirMultiplo();
//	else caminho = processoDt.getProcessoDiretorio() + File.separator + processoDt.getProcessoNumero();
//
//	File diretorioFisico = new File(ProjudiPropriedades.getInstance().getDirProcessos() + caminho);
//	int cont = 1;
//	if (diretorioFisico.exists()) cont = diretorioFisico.listFiles().length + 1;
//	diretorioFisico.mkdirs();
//
//	for (int i = 0; i < arquivos.size(); i++) {
//		ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
//		arquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
//		arquivoDt.setIpComputadorLog(logDt.getIpComputador());
//		// Seta caminho dos arquivos (\2007\1\3920070000003\arquivo.p7s)
//		arquivoDt.setCaminho(caminho + File.separator + "arquivo" + (cont++) + ".ReciboProjudi");
//		//Salva arquivo
//		this.inserir(arquivoDt, obFabricaConexao);
//	}
//	}

	/**
	 * Método responsável em salvar uma lista de arquivos
	 *  
	 * @param arquivos: lista de arquivos a serem salvos
	 * @param logDt: objeto de log
	 * @param obFabricaConexao: conexão
	 * @author msapaula
	 * @throws Exception 
	 * @since 15/01/2009 10:32
	 */
	public void inserirArquivos(List arquivos, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception{
		
		for (int i = 0; i < arquivos.size(); i++) {
			ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
			arquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
			arquivoDt.setIpComputadorLog(logDt.getIpComputador());

			//Salva arquivo
			this.inserir(arquivoDt, logDt, obFabricaConexao);
		}
				
	}

	/**
	 * Grava arquivos físicos referentes a um processo somente
	 * @param arquivos: lista de arquivos
	 * @param processoDt: dt de processo
	 * 
	 * @author msapaula
	 * @throws Exception 
	 * @deprecated
	 */
	public void gravarArquivoFisico(List arquivos, ProcessoDt processoDt) throws Exception{
		
		for (int i = 0; i < arquivos.size(); i++) {
			ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
			// Cria arquivo a ser salvo em disco (monta caminho completo)
			File diretorioArquivo = new File(ProjudiPropriedades.getInstance().getDirProcessos() + arquivoDt.getCaminho());

			//GERAR RECIBO ASSINADO PROJUDI 
			//Signer.assinaReciboComPKCS12(processoDt, arquivoDt);

			//Grava arquivo compactado em disco
			Funcoes.gravaArquivo(arquivoDt.conteudoBytes(), diretorioArquivo);
		}
				
	}

//	/**
//	 * Grava arquivos físicos que podem se referir a vários processos
//	 * @param arquivos: lista de arquivos
//	 * @param processos
//	 * 
//	 * @author msapaula
//	 * @throws Exception 
//	 * @deprecated
//	 */
//	public void gravarArquivoFisico(List arquivos, List processos) throws Exception{
//		
//		if (arquivos != null) {
//			for (int i = 0; i < arquivos.size(); i++) {
//				ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
//				// Cria arquivo a ser salvo em disco (monta caminho completo)
//				File diretorioArquivo = new File(ProjudiPropriedades.getInstance().getDirProcessos() + arquivoDt.getCaminho());
//				//GERAR RECIBO ASSINADO PROJUDI 
//				Signer.gerarRecibo(processos, arquivoDt);
//				//Grava arquivo compactado em disco
//				Funcoes.gravaArquivo(arquivoDt.conteudoBytes(), diretorioArquivo);
//			}
//		}
//		
//	}

//	/**
//	 * Atualiza o conteúdo de um arquivo após a geração de recibo para esse
//	 * 
//	 * @param arquivoDt, objeto arquivo
//	 * @param fabConexao, fabrica de conexao
//	 * 
//	 * @author msapaula
//	 * @throws Exception 
//	 */
//	public void atualizaConteudoRecibo(ArquivoDt arquivoDt, FabricaConexao fabConexao) throws Exception{
//		ArquivoPs obPersistencia = new  ArquivoPs(fabConexao.getConexao());
//		
//		arquivoDt.setRecibo("true");					
//		
//		obPersistencia.atualizaConteudoArquivo(arquivoDt);
//		
//		//o conteudo do arquivo é para o ceph
//		if ( arquivoDt.isSalvarCeph()) {
//			salvarArquivoTransacaoCeph(arquivoDt);		 
//		}
//	}

	public void salvarImportacao(ArquivoDt dados) throws Exception{
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {			
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
									
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
			} else {
				obPersistencia.alterar(dados);
			}
			
			obDados.copiar(dados);

			//o conteudo do arquivo é para o ceph	
			if ( dados.isSalvarCeph()) {
				salvarArquivoTransacaoCeph(dados);
			}
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método que salva o arquivo de Verificação de Prevenção
	 * 
	 * @param processoDt, dt do processo
	 * @param conexao, conexão ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	public ArquivoDt salvarArquivoVerificarConexao(ProcessoCadastroDt processoDt, FabricaConexao conexao) throws Exception{

		// Chama método para gerar conteúdo do arquivo
		ArquivoDt arquivoPrevencao = montaArquivoVerificarPrevencao(processoDt, conexao);

		// Chama método para assinar o arquivo com certificado do Sistema Projudi
		Signer.assinaArquivoCertificadoSistema(arquivoPrevencao);
		this.salvar(arquivoPrevencao, conexao);

		return arquivoPrevencao;
	}

	/**
	 * Realiza tratamentos referente aos dados da parte autora. Se uma ou mais partes foram cadastradas sem CPF
	 * deve gerar uma Certidão para que o cartório verifique e tome as providências conforme Provimento da Corregedoria
	 * nº 06 de 23/03/2010).
	 * 
	 * @param processo, obj de processo
	 * @param conexao, conexãoa ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public ArquivoDt salvarArquivoVerificarCpfParte(ProcessoDt processoDt, FabricaConexao conexao) throws Exception{
		String partesSemCpf = "";
		ArquivoDt arquivoCertidao = null;

		//Verifica se alguma parte está sem CPF
		if (processoDt.getListaPolosAtivos() != null) {
			for (int i = 0; i < processoDt.getListaPolosAtivos().size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) processoDt.getListaPolosAtivos().get(i);
				if (parteDt.getCpf().length() == 0 && parteDt.getCnpj().length() == 0) {
					if (partesSemCpf.length() > 0) partesSemCpf += ", ";
					partesSemCpf += parteDt.getNome();
				}
			}
		}

		if (partesSemCpf.length() > 0) {
			// Chama método para gerar conteúdo do arquivo
			arquivoCertidao = montaArquivoVerificarCpfParte(processoDt, partesSemCpf);

			// Chama método para assinar o arquivo com certificado do Sistema Projudi
			Signer.assinaArquivoCertificadoSistema(arquivoCertidao);
			this.salvar(arquivoCertidao, conexao);
		}
		return arquivoCertidao;
	}

	/**
	 * Monta o arquivo referente a prevenção de processos. Esse arquivo será vinculado a pendência do tipo "Verificar Prevenção" 
	 * e terá detalhes sobre os possíveis preventos para o processo passado.
	 * 
	 * @param processoDt, dt do processo
	 * @author msapaula
	 */
	private ArquivoDt montaArquivoVerificarPrevencao(ProcessoCadastroDt processoDt, FabricaConexao conexao){
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		String conteudo = "";
		List preventos = processoDt.getListaPreventos();
		ArquivoDt arquivoDt = new ArquivoDt();

		// Gerando texto de configuração da pre-análise
		conteudo = "<html>";
		conteudo += "<head>";
		conteudo += "	<meta CONTENT=\"text/html; charset=UTF-8\" HTTP-EQUIV=\"CONTENT-TYPE\"/>";
		conteudo += "</head>";
		conteudo += "<body>";
		conteudo += "	<h3 align=\"center\">VERIFICAR PREVEN&Ccedil;&Atilde;O/CONEX&Atilde;O PROCESSO</h3>";
		conteudo += "	<div align=\"center\">";
		conteudo += "		<label><b>Processo Principal:</b> </label>";
		conteudo += "		<span><a href=\"" + projudiConfiguration.getLinkSistemaNaWEB() + "/BuscaProcesso?Id_Processo=" + processoDt.getId() + "\">";
		conteudo += Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " </a></span>";
		conteudo += "		<br />";
		conteudo += "		<label><b>Serventia:  </b> </label>";
		conteudo += "		<span> " + processoDt.getServentiaDt().getServentia() + "</span>";
		conteudo += "		<label><b>Valor:  </b> </label>";
		conteudo += "		<span>" + processoDt.getValor() + "</span>";
		conteudo += "	</div>";
		conteudo += "	<br />";
		conteudo += "	<table border=\"1\" width=\"95%\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\">";
		conteudo += "		<thead>";
		conteudo += "		<tr>";
		conteudo += "			<th colspan=\"5\">Poss&iacute;veis Preventos/Conexos</th>";
		conteudo += "		</tr>";
		conteudo += "		<tr>";
		conteudo += "			<th>Processo</th>";
		conteudo += "			<th>Data Distribui&ccedil;&atilde;o</th>";
		conteudo += "			<th>Serventia</th>";
		conteudo += "			<th>Classe</th>";
		conteudo += "			<th>Valor</th>";
		conteudo += "		</tr>";
		conteudo += "		</thead>";
		conteudo += "		<tbody>";
		for (int i = 0; i < preventos.size(); i++) {
			ProcessoDt objPrevento = (ProcessoDt) preventos.get(i);
			conteudo += "		<tr align=\"center\">";
			conteudo += "			<td><a href=\"" + projudiConfiguration.getLinkSistemaNaWEB() + "/BuscaProcesso?Id_Processo=" + objPrevento.getId() + "\">";
			conteudo += Funcoes.formataNumeroProcesso(objPrevento.getProcessoNumero()) + "</a></td>";
			conteudo += "			<td>" + objPrevento.getDataRecebimento() + "</td>";
			conteudo += "			<td>" + objPrevento.getServentia() + "</td>";
			conteudo += "			<td>" + objPrevento.getProcessoTipo() + "</td>";
			conteudo += "			<td>" + objPrevento.getValor() + "</td>";
			conteudo += "		</tr>";
		}
		conteudo += "		</tbody>";
		conteudo += "	</table>";
		conteudo += "</body>";
		conteudo += "</html>";

		// Monta arquivo
		arquivoDt.setArquivo(conteudo);
		arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.VERIFICAR_PREVENCAO));
		arquivoDt.setNomeArquivo("Prevenção");
		arquivoDt.setContentType("text/html");
		arquivoDt.setId_UsuarioLog(processoDt.getId_UsuarioLog());
		arquivoDt.setIpComputadorLog(processoDt.getIpComputadorLog());

		return arquivoDt;
	}

	/**
	 * Monta o arquivo referente à situação em que uma ou mais partes autoras foram cadastradas sem CPF.
	 * 
	 * @param processoDt, dt do processo
	 * @author msapaula
	 */
	private ArquivoDt montaArquivoVerificarCpfParte(ProcessoDt processoDt, String partesSemCpf){
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		String conteudo = "";
		ArquivoDt arquivoDt = new ArquivoDt();

		// Gerando texto de configuração da pre-análise
		conteudo = "<html>";
		conteudo += "<head>";
		conteudo += "	<meta CONTENT=\"text/html; charset=UTF-8\" HTTP-EQUIV=\"CONTENT-TYPE\"/>";
		conteudo += "</head>";
		conteudo += "<body>";
		conteudo += "	<h3 align=\"center\">VERIFICAR PARTE(S) SEM CPF</h3>";
		conteudo += " 	<div align=\"center\">";
		conteudo += "		<label><b>Processo:</b> </label>";
		conteudo += "		<span><a href=\"" + projudiConfiguration.getLinkSistemaNaWEB() + "/BuscaProcesso?Id_Processo=" + processoDt.getId() + "\">";
		conteudo += Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + "</a></span>";
		conteudo += "		<br />";
		conteudo += "		<br />";
		conteudo += "		<span> Atenção, verifique porque o CPF da(s) parte(s) <b> " + partesSemCpf + " </b> não foi informado. ";
		conteudo += " 		Tomar as medidas cabíveis conforme Provimento da Corregedoria";
		conteudo += "		nº 06, de 23/03/2010.</span>";
		conteudo += "	</div>";
		conteudo += "</body>";
		conteudo += "</html>";

		// Monta arquivo
		arquivoDt.setArquivo(conteudo);
		arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.CERTIDAO));
		arquivoDt.setNomeArquivo("Certidão");
		arquivoDt.setContentType("text/html");
		arquivoDt.setId_UsuarioLog(processoDt.getId_UsuarioLog());
		arquivoDt.setIpComputadorLog(processoDt.getIpComputadorLog());

		return arquivoDt;
	}

	/**
	 * Retorna o id do Último arquivo do tipo relatório inserido no processo
	 * @author Leandro Bernardes
	 * @param String idProcesso
	 * @param String idUsuarioServentia
	 * @return String id do Arquivo
	 * @throws Exception
	 */
	public String consultarRelatorioProcesso(String idProcesso, String id_UsuarioServentia) throws Exception{
		String idMovimentacaoAquivo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			idMovimentacaoAquivo = obPersistencia.consultarRelatorioProcesso(idProcesso, id_UsuarioServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return idMovimentacaoAquivo;
	}
	
	public ArquivoDt consultarArquivo(String idMandadoPrisao, String arquivoTipoCodigo) throws Exception{
		ArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarArquivo(idMandadoPrisao, arquivoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarDescricaoArquivoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ArquivoTipoNe ArquivoTipone = new ArquivoTipoNe(); 
		stTemp = ArquivoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * Método responsável por validar acesso, proteções e formatação de todos os arquivos que são anexados a um processo.
	 * 
	 * @param arquivoDt - arquivo sendo inserido
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void validarArquivoInseridoProcesso(ArquivoDt arquivoDt) throws Exception{
		
		String nomeArquivo = arquivoDt.getNomeArquivo().toLowerCase();
		String[] extensoes = nomeArquivo.split("\\.");
		String extensaoFinal = extensoes[extensoes.length - 2];
		byte[] conteudoArquivo = null;
		
		// SE FOR MP3 APENAS RETORNA POIS NÃO SERÁ VALIDADO
		if (extensaoFinal.equalsIgnoreCase("mp3")) {
			return;
		}
		
		if(extensoes[extensoes.length - 1].equals("p7s")){
			//se for um arquivo assinado, tem de extrair o conteúdo de outra forma
			extensaoFinal = extensoes[extensoes.length - 2];
			conteudoArquivo = arquivoDt.getConteudo();
		} else {
			//se o arquivo não for assinado, tem de extrair o conteudo em bytes
			extensaoFinal = extensoes[extensoes.length - 1];
			conteudoArquivo = arquivoDt.conteudoBytes();
		}

		//Variável gerada para receber o retorno dos métodos de conversão para PDF.
		//Criei essa variável para não precisar ter dois métodos fazendo a mesma coisa na classe de conversão, o que dificultaria uma manutenção futura.
		byte[] arquivoTmp= null;
				 		
		if (extensaoFinal.equalsIgnoreCase("html")) {
			// SE HTML CONVERTER PARA PDF				
			arquivoTmp = ConverterHtmlPdf.converteHtmlPDF(conteudoArquivo, false);
						
		} else if (extensaoFinal.equalsIgnoreCase("jpg")) {
			// SE JPG CONVERTER PARA PDF				
			arquivoTmp = ConverteImagemPDF.gerarPdfImagem(conteudoArquivo);				
			
		} else if (extensaoFinal.equalsIgnoreCase("pdf")) {
			
			// SE O ARQUIVO NAO FOR HTML OU JPG GERA UM PDF COM A MSG ABAIXO				
			GerarCabecalhoProcessoPDF.validarAcessoPDF(conteudoArquivo);
			
			// Verificar o tamanho e a quantidade de paginas do arquivo
			Funcoes.validarTamanhoPadraoArquivo(conteudoArquivo);
			
			// Verifica se é do tipo PDF-A
			//Funcoes.validarSeArquivoEhTipoPDFA(conteudoArquivo);
			
			// Abre o arquivo e procura pela 1a página com texto puro 
			// (Somente decisão, despacho, sentença, acórdão, relatorio e voto)			
			//Funcoes.validarSeConteudoEhTexto(arquivoDt.getId_ArquivoTipo(), conteudoArquivo);
			
		} else if (!(extensaoFinal.equalsIgnoreCase("pdf"))) {
			// SE O ARQUIVO NAO FOR HTML OU JPG GERA UM PDF COM A MSG ABAIXO
			throw new MensagemException("\n\n Este arquivo não pode ser aberto, pois não está no formato \".pdf\" ou \".html\". Utilize a \"navegação pelo processo\" para abrir o arquivo.");
		}

		arquivoTmp = null;
		
		
	}
	
	/**
	 * Consulta os arquivos da movimentação, cujo acesso é NORMAL e PUBLICO
	 * e o tipo do arquivo deve ser DJe = 1
	 * @param id_movimentacao
	 * @return
	 * @throws Exception
	 */
	public List<ArquivoDt> consultaArquivosComAcessoNormalPublicoParaPublicacao(String id_movimentacao) throws Exception {
		List<ArquivoDt> lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultaArquivosComAcessoNormalPublicoParaDJE(id_movimentacao);		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * 
	 * @param id_arquivo
	 * @return
	 * @throws Exception
	 */
	public boolean isArquivoPublicoEAcessoNormalPublico(String id_arquivo) throws Exception {
		boolean isPublico = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			isPublico = obPersistencia.isArquivoPublicoEAcessoNormalPublico(id_arquivo);		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return isPublico;
	}
	
	/**
	 * Altera a data de indexação do arquivo
	 * Utilizado pela execução automática - indexação de arquivos no elasticsearch
	 * @param obFabricaConexao
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int alterarDataIndexacao (String id) throws Exception {
		int rowsAfetados = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			rowsAfetados = obPersistencia.alterarDataIndexacao(id);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return rowsAfetados;
	}
	
	/**
	 * Método que salva o arquivo de Verificação de Tema transitado Julgado
	 * 
	 * @param processoDt, dt do processo
	 * @param conexao, conexão ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	public ArquivoDt salvarArquivoVerificarVerificarTemaTransitoJulgado (ProcessoDt processoDt, List<ProcessoTemaDt> temas, FabricaConexao conexao) throws Exception{
		
		// Chama método para gerar conteúdo do arquivo
		ArquivoDt arquivoHtml = montaArquivoVerificarVerificarTemaTransitoJulgado(processoDt, temas);
		
		// Chama método para assinar o arquivo com certificado do Sistema Projudi
		Signer.assinaArquivoCertificadoSistema(arquivoHtml);
		this.salvar(arquivoHtml, conexao);

		return arquivoHtml;
	}
	
	/**
	 *  
	 * @param processoDt
	 * @param conexao
	 * @return
	 */
	private ArquivoDt montaArquivoVerificarVerificarTemaTransitoJulgado (ProcessoDt processoDt, List<ProcessoTemaDt> lista){
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		StringBuilder conteudo = new StringBuilder();
		conteudo.append("<html>");
		conteudo.append("<head>");
		conteudo.append("<meta CONTENT=\"text/html; charset=UTF-8\" HTTP-EQUIV=\"CONTENT-TYPE\"/>");
		conteudo.append("</head>");
		conteudo.append("<body>");
		conteudo.append("<h3 align=\"center\">VERIFICAR PROCESSO COM TEMA(S) EM TRANSITO JULGADO</h3>");
		conteudo.append("<div align=\"center\">");
		conteudo.append("<label><b>Processo: </b></label>");
		conteudo.append("<span><a href=\"" + projudiConfiguration.getLinkSistemaNaWEB() + "/BuscaProcesso?Id_Processo=" + processoDt.getId() + "\">");
		conteudo.append(Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + "</a></span>");
		conteudo.append("<br />");
		conteudo.append("<label><b>Serventia:  </b> </label>");		
		conteudo.append("<span> " + processoDt.getServentia() + "</span>");
		conteudo.append("<br />");
		conteudo.append("<table border=\"1\" width=\"95%\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" padding=\"10px;\">");
		conteudo.append("<thead>");
		conteudo.append("<tr>");
		conteudo.append("<th>Código</th>");
		conteudo.append("<th>Tema</th>");
		conteudo.append("<th>Data Sobrestamento</th>");
		conteudo.append("<th>Situação</th>");
		conteudo.append("<th>Origem</th>");
		conteudo.append("<th>Data Trânsito</th>");
		conteudo.append("</tr>");
		conteudo.append("</thead>");
		conteudo.append("<tbody>");
		lista.forEach(processoTema -> {
			conteudo.append("<tr>");
			conteudo.append("<td align=\"center\">" + processoTema.getTemaCodigo() + "</td>");
			conteudo.append("<td>" + processoTema.getTitulo() + ".. </td>");
			conteudo.append("<td align=\"center\">" + processoTema.getDataSobrestado() + "</td>");
			conteudo.append("<td align=\"center\">" + processoTema.getTemaSituacao() + "</td>");
			conteudo.append("<td align=\"center\">" + processoTema.getTemaOrigem() + "</td>");
			conteudo.append("<td align=\"center\">" + processoTema.getTema().getDataTransito() + "</td>");
			conteudo.append("</tr>");
		});
		conteudo.append("</tbody>");
		conteudo.append("</table>");		
		conteudo.append("</div>");
		conteudo.append("</body>");
		conteudo.append("</html>");

		// Monta arquivo
		ArquivoDt arquivoDt = new ArquivoDt();
		arquivoDt.setArquivo(conteudo.toString());
		arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.OUTROS));
		arquivoDt.setNomeArquivo("Temas_Transitados");
		arquivoDt.setContentType("text/html");
		arquivoDt.setId_UsuarioLog(processoDt.getId_UsuarioLog());
		arquivoDt.setIpComputadorLog(processoDt.getIpComputadorLog());

		return arquivoDt;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ArquivoDt consultarArquivoEmentaPorIdRelatorioVoto(String id, FabricaConexao conexao) throws Exception {
		ArquivoDt arquivo = null;
		FabricaConexao obFabricaConexao = null;
		try {			
			obFabricaConexao = ValidacaoUtil.isNulo(conexao) ? new FabricaConexao(FabricaConexao.CONSULTA) : conexao;
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			arquivo = obPersistencia.consultarArquivoEmentaPorIdRelatorioVoto(id);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return arquivo;		
	}
	
	/**
	 *  Inserir um novo arquivo utilizando-se de uma transacao corrente, caso nao
	 * tenha sido criada, sera criado no metodo a transacao
	 * @author Márcio Mendonça Gomes
	 * @since 28/11/2018 10:44
	 * @param ArquivoDt arquivoDt, pojo a ser inserido
	 * @param FabricaConexao fabConexao, fabrica de conexoes
	 * @return void
	 * @throws Exception
	 */
	public void inserirArquivoStorage(ArquivoDt arquivoDt, LogDt logDt, FabricaConexao fabConexao) throws Exception{
	    FabricaConexao obFabricaConexao = null;
		try{
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabConexao;
			}

			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			
			// Se o arquivo já tem id não é necessário salvar novamente
			if (arquivoDt.getId().equalsIgnoreCase("")) {					
				obPersistencia.inserirArquivoStorage(arquivoDt);

				if(arquivoDt.getId_UsuarioLog() == null || arquivoDt.getId_UsuarioLog().equals("")){
					arquivoDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
				}
				if(arquivoDt.getIpComputadorLog() == null || arquivoDt.getIpComputadorLog().equals("")){
					arquivoDt.setIpComputadorLog(logDt.getIpComputadorLog());
				}
				LogNe logNe = new LogNe();
				logNe.salvar(new LogDt("Arquivo", arquivoDt.getId(), arquivoDt.getId_UsuarioLog(), arquivoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", arquivoDt.getPropriedades()), obFabricaConexao);
			} else if (arquivoDt.conteudoBytes() == null) {
				arquivoDt.setArquivo(obPersistencia.consultarConteudoArquivo(arquivoDt));
			}
			
		
		} finally{
			if (fabConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	
	/**
	 * Altera a data de indexação do arquivo
	 * Utilizado pela execução automática - indexação de arquivos no elasticsearch
	 * @param obFabricaConexao
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean bloquearArquivoPublicacao(String id, String idServentiaPend, String idServentiaUsuario) throws Exception {
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		try {
			// Consulta arquivo 
			ArquivoDt arquivoDt =  this.consultarId(id, obFabricaConexao.getConexao());
			
			//Verifica se está na mesma serventia usuário e arquivo
			if(!StringUtils.equals(idServentiaPend, idServentiaUsuario)) {
				return false;
			}
			
			// Se o arquivo já está bloqueado não é necessário salvar novamente
			if (!arquivoDt.isBloqueado()) {
				
				//Desindexar o arquivo antes do bloqueio.
				new ElasticSearchNe().excluirArquivoPublicado(id);
				
				ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
				 obPersistencia.bloquearArquivoPublicacao(id, idServentiaUsuario);
				 return true;
			} else {
				return false;
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public ArquivoDt montaArquivoVerificarECarta(PendenciaDt pendenciaDt){
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		String conteudo = "";
		ArquivoDt arquivoDt = new ArquivoDt();

		conteudo = "<html>";
		conteudo += "<head>";
		conteudo += "	<meta CONTENT=\"text/html; charset=UTF-8\" HTTP-EQUIV=\"CONTENT-TYPE\"/>";
		conteudo += "</head>";
		conteudo += "<body>";
		conteudo += "	<h3 align=\"center\">VERIFICAR DADOS DA PARTE</h3>";
		conteudo += " 	<div align=\"center\">";
		conteudo += "		<label><b>Processo:</b> </label>";
		conteudo += "		<span><a href=\"" + projudiConfiguration.getLinkSistemaNaWEB() + "/BuscaProcesso?Id_Processo=" + pendenciaDt.getId_Processo() + "\">";
		conteudo += Funcoes.formataNumeroProcesso(pendenciaDt.getProcessoNumero()) + "</a></span>";
		conteudo += "		<br />";
		conteudo += "		<br />";
		conteudo += "		<span> Atenção, verifique o endereço da parte <b>" + pendenciaDt.getNomeParte() + "</b></span><br />";
		conteudo += "		<span> Mensagem: " + pendenciaDt.getComplemento() + "</span>";
		conteudo += "	</div>";
		conteudo += "</body>";
		conteudo += "</html>";

		// Monta arquivo
		arquivoDt.setArquivo(conteudo);
		arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.CERTIDAO));
		arquivoDt.setNomeArquivo("Certidão");
		arquivoDt.setContentType("text/html");
		arquivoDt.setId_UsuarioLog(pendenciaDt.getId_UsuarioLog());
		arquivoDt.setIpComputadorLog(pendenciaDt.getIpComputadorLog());
		
		return arquivoDt;
	}
	
	public boolean arquivoExiste(String nomeArquivo, int id_ArquivoTipo) throws Exception {
		boolean existe = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			existe = obPersistencia.arquivoExiste(nomeArquivo, id_ArquivoTipo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return existe;
	}
	
	public List consultarArquivosECarta(String nomeArquivo, int id_ArquivoTipo, String dataInicial, String dataFinal) throws Exception {
		List arquivosZip = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			arquivosZip = obPersistencia.consultarArquivosECarta(nomeArquivo, id_ArquivoTipo, dataInicial, dataFinal);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return arquivosZip;
	}
	
	public void salvarECarta(ArquivoDt dados ) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			obFabricaConexao.iniciarTransacao();
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			obPersistencia.inserirECarta(dados);
			obLogDt = new LogDt("Arquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarECarta() throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarECarta(); 
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	public void alterarDataInsercao(String[] arquivo) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			obPersistencia.alterarDataInsercao(arquivo); 
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public String[] lerReciboServicoBD(int id_ArquivoTipo, String dataInicial, String dataFinal, String idPendencia, String rastreamento) throws Exception {
		String[] retorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.lerReciboServicoBD(id_ArquivoTipo, dataInicial, dataFinal, idPendencia, rastreamento);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public String[] lerNotificacaoInconsistenciaBD(int id_ArquivoTipo, String dataInicial, String dataFinal, String idPendencia) throws Exception {
		String[] retorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.lerNotificacaoInconsistenciaBD(id_ArquivoTipo, dataInicial, dataFinal, idPendencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public String[] lerRastreamentoDataEstimadaEntregaBD(int id_ArquivoTipo, String dataInicial, String dataFinal, Map rastreamento) throws Exception {
		String[] retorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.lerRastreamentoDataEstimadaEntregaBD(id_ArquivoTipo, dataInicial, dataFinal, rastreamento);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public String[] lerRastreamentoDataFinalEntregaBD(int id_ArquivoTipo, String dataInicial, String dataFinal, Map rastreamento) throws Exception {
		String[] retorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.lerRastreamentoDataFinalEntregaBD(id_ArquivoTipo, dataInicial, dataFinal, rastreamento);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public String[] lerDevolucaoARsBD(int id_ArquivoTipo, String dataInicial, String dataFinal, String idPendencia, Map codRastreamento) throws Exception {
		String[] retorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.lerDevolucaoARsBD(id_ArquivoTipo, dataInicial, dataFinal, idPendencia, codRastreamento);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	private void salvarArquivoTransacaoCeph(ArquivoDt arquivoDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
			obPersistencia.salvarArquivoTransacaoCeph(arquivoDt);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluirArquivosControleCeph(String idUsuarioLog, FabricaConexao obFabricaConexao) throws Exception {
		ArquivoPs obPersistencia = new  ArquivoPs(obFabricaConexao.getConexao());
		obPersistencia.excluirArquivosControleCeph(idUsuarioLog);
	}
	
}