package br.gov.go.tj.projudi.ne;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.MidiaPublicadaArquivoDt;
import br.gov.go.tj.projudi.dt.MidiaPublicadaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

//---------------------------------------------------------
public class MidiaPublicadaNe extends Negocio {
	
	private static final long serialVersionUID = 2855261964108336284L;
	
	public MidiaPublicadaNe() {
		obLog = new LogNe();
	}
	
	public String enviarMidiaCeph(MidiaPublicadaDt midiaPublicadaDt, MidiaPublicadaArquivoDt midiaPublicadaArquivoDt) throws Exception {
		validarMidiaPublicadaCeph(midiaPublicadaDt, midiaPublicadaArquivoDt.getCaminhoCompletoArquivoTemp(), midiaPublicadaArquivoDt.getContentType());
	
		ProjudiPropriedades projudiPropriedades = ProjudiPropriedades.getInstance();
		
		String bucketName = projudiPropriedades.getObjectStorageUploadCurrentBucket();
		
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferArquivo = null;
		
		try {
			File file = new File(midiaPublicadaArquivoDt.getCaminhoCompletoArquivoTemp());
			fileInputStream = new FileInputStream(file);
			bufferArquivo = new BufferedInputStream(fileInputStream, RequestClientOptions.DEFAULT_STREAM_BUFFER_SIZE);
			
			String fileObjKeyName =  geraNomeObjetoStorage(midiaPublicadaDt, midiaPublicadaArquivoDt);
		    
			AmazonS3 s3Client = obtenhaConexaoObjectStorage(projudiPropriedades);
			
			ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentType(midiaPublicadaArquivoDt.getContentType()); //"plain/text"
	        metadata.addUserMetadata("Projudi", "MidiaPublicada");
	        metadata.setContentLength(midiaPublicadaArquivoDt.getTamanhoArquivo());
	        
	        PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, bufferArquivo, metadata);
	        PutObjectResult result = s3Client.putObject(request);
	       	               
	        apagarArquivoTemporario(file);
	        
	        midiaPublicadaArquivoDt.setCaminhoCompletoArquivoCeph(String.format("[%s]%s", bucketName, fileObjKeyName));    
	        String urlCeph = "none";
	        try {
	        	GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileObjKeyName);
		        Calendar c = Calendar.getInstance();
		        c.add(Calendar.MINUTE, 2);
		        Date expirationDate = c.getTime();
		        urlRequest.setExpiration(expirationDate);
		                
				ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
			    responseHeaders.setContentDisposition("filename=" + midiaPublicadaArquivoDt.getNomeArquivo());
			    urlRequest.setResponseHeaders(responseHeaders);
		        
		        URL urlAssinada = s3Client.generatePresignedUrl(urlRequest);
		        
		        urlCeph = urlAssinada.toString();
	        } catch (Exception e) {}	
	        
	        return urlCeph;
		} finally {
			if (fileInputStream != null) fileInputStream.close();
			if (bufferArquivo != null) bufferArquivo.close();
		}
	}
	
	private String geraNomeObjetoStorage(MidiaPublicadaDt midiaPublicadaDt, MidiaPublicadaArquivoDt midiaPublicadaArquivoDt) {
		String nomeObjetoStorage = String.format("%s/%s/%s/%s/%s", 
				                          Funcoes.completarZeros(String.valueOf(midiaPublicadaDt.getProcesso().getNumeroProcessoDt().getDigito()), 2),
				                          Funcoes.obtenhaSomenteNumeros(midiaPublicadaDt.getProcessoNumero()),
				                          midiaPublicadaDt.getDataHora().getDataHoraFormatadayyyyMMddHHmm(),
				                          (new TJDataHora()).getDataHoraFormatadayyyyMMddHHmm(),
				                          midiaPublicadaArquivoDt.getNomeArquivo());
		
		return nomeObjetoStorage;
	}
	
	private void apagarArquivoTemporario(File file) {
		try {
			file.delete();
		} catch (Exception e) {}
	}
	
	public String gerarURLCeph(MidiaPublicadaDt midiaPublicadaDt, String nomeArquivo, String contentType) throws Exception {
		validarMidiaPublicadaCeph(midiaPublicadaDt, nomeArquivo, contentType);
		
		ProjudiPropriedades projudiPropriedades = ProjudiPropriedades.getInstance();
		
		String bucketName = projudiPropriedades.getObjectStorageUploadCurrentBucket(); //"audiencias-judiciais-2020";
		
		AmazonS3 s3Client = obtenhaConexaoObjectStorage(projudiPropriedades);
		
		try {
			ObjectMetadata object = s3Client.getObjectMetadata(bucketName, nomeArquivo);
			if (object.getContentLength() > 0) {
				s3Client.deleteObject(bucketName, nomeArquivo);
			}	
		} catch (Exception e) {
			System.out.print(e);
		}
		// Set the pre-signed URL to expire after one hour.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        
        // Generate the pre-signed URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, nomeArquivo)
                .withMethod((projudiPropriedades.isObjectStorageUploadVerboPUT() ? HttpMethod.PUT : HttpMethod.POST))
                .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        
        return url.toString();
	}
	
	private AmazonS3 obtenhaConexaoObjectStorage(ProjudiPropriedades projudiPropriedades) {
		String accessKey = projudiPropriedades.getObjectStorageUploadAccessKey();
		String secretKey = projudiPropriedades.getObjectStorageUploadSecretKey();
		String host = projudiPropriedades.getObjectStorageHost();
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
    	ClientConfiguration clientConfig = new ClientConfiguration();
    	if (projudiPropriedades.isObjectStorageUploadProtocoloHTTP()) {
    		clientConfig.setProtocol(Protocol.HTTP);    			
    	} else {
    		clientConfig.setProtocol(Protocol.HTTPS);    		
    	}
		//clientConfig.setUseGzip(true);			
		
		AmazonS3 s3Client = new AmazonS3Client(awsCreds, clientConfig);
		
		S3ClientOptions s3ClientOptions = new S3ClientOptions();
		s3Client.setS3ClientOptions(s3ClientOptions);
		s3Client.setEndpoint(host);
		
		return s3Client;
	}
	
	private void validarMidiaPublicadaCeph(MidiaPublicadaDt midiaPublicadaDt, String nomeArquivo, String contentType) throws MensagemException {
		if (midiaPublicadaDt.getProcesso() == null) {
			throw new MensagemException("Processo deve ser informado.");
		}		
		if (Funcoes.isStringVazia(nomeArquivo)) {
			throw new MensagemException("Nome do arquivo deve ser informado.");
		}
		if (Funcoes.isStringVazia(contentType)) {
			throw new MensagemException("ContentType do arquivo deve ser informado.");
		}
	}

	public void gerarMovimentacaoMidiaPublicada(MidiaPublicadaDt midiaPublicadaDt, UsuarioDt usuarioDt) throws Exception {
		validarMidiaPublicadaCeph(midiaPublicadaDt);
		
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();		
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();		
		FabricaConexao obFabricaConexao = FabricaConexao.criarConexaoPersistencia();
		LogDt logDt = new LogDt(midiaPublicadaDt.getId_UsuarioLog(), midiaPublicadaDt.getIpComputadorLog());
		
		try {		
			obFabricaConexao.iniciarTransacao();
			
			MovimentacaoDt movimentacaoDt = movimentacaoNe.gerarMovimentacaoMidiaPublicada(midiaPublicadaDt.getProcesso().getId(), 
													                                       usuarioDt.getId_UsuarioServentia(), 
													                                       midiaPublicadaDt.getComplementoMovimentacao(), 
													                                       logDt, 
													                                       obFabricaConexao);
			
			movimentacaoDt.setId_UsuarioLog(midiaPublicadaDt.getId_UsuarioLog());
			movimentacaoDt.setIpComputadorLog(midiaPublicadaDt.getIpComputadorLog());
			
			List<ArquivoDt> listaArquivosObjectStorage = obtenhaListaDeArquivoDoObjectStorage(midiaPublicadaDt);
			movimentacaoArquivoNe.inserirArquivosObjectStorage(movimentacaoDt.getId(), listaArquivosObjectStorage, logDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	private List<ArquivoDt> obtenhaListaDeArquivoDoObjectStorage(MidiaPublicadaDt midiaPublicadaDt) {
		List<ArquivoDt> listaDeArquivos = new ArrayList<>();
		for (MidiaPublicadaArquivoDt midiaPublicadaArquivoDt : midiaPublicadaDt.getListaMidiasPublicadas()) {
			if (midiaPublicadaArquivoDt.isArquivoEnviadoCeph() && 
				!midiaPublicadaArquivoDt.isErro()) {
				
				ArquivoDt arquivoDt = new ArquivoDt();
				
				arquivoDt.setNomeArquivo(midiaPublicadaArquivoDt.getNomeArquivo());
				arquivoDt.setCaminho(midiaPublicadaArquivoDt.getCaminhoCompletoArquivoCeph());

				arquivoDt.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.MIDIA_DIGITAL));

				arquivoDt.setContentType(midiaPublicadaArquivoDt.getContentType());
				byte[] bytes = ByteBuffer.allocate(Long.BYTES).putLong(midiaPublicadaArquivoDt.getTamanhoArquivo()).array();
				arquivoDt.setArquivo(bytes);
				
				arquivoDt.setCodigoTemp(String.valueOf(midiaPublicadaArquivoDt.getTamanhoArquivo()));

				listaDeArquivos.add(arquivoDt);
			}			
		}
		return listaDeArquivos;
	}			
	
	private void validarMidiaPublicadaCeph(MidiaPublicadaDt midiaPublicadaDt) throws MensagemException {
		if (midiaPublicadaDt.getProcesso() == null) {
			throw new MensagemException("Processo deve ser informado.");
		}	
		if (midiaPublicadaDt.getDataHora() == null) {
			throw new MensagemException("Data e hora devem ser informados.");
		}
		if (midiaPublicadaDt.getListaMidiasPublicadas().size() == 0) {
			throw new MensagemException("Pelo menos um arquivo deve ser informado.");
		}
		for (MidiaPublicadaArquivoDt midiaGravadaArquivo : midiaPublicadaDt.getListaMidiasPublicadas()) {
			if (midiaGravadaArquivo.isArquivoEnviadoCeph()) {
				return;
			}			
		}
		throw new MensagemException("Pelo menos um arquivo deve ter sido feito upload.");
	}
}
