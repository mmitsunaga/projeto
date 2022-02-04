package br.gov.go.tj.projudi.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Vector;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cms.CMSSignedData;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.Base64Utils;
import br.gov.go.tj.utils.Certificado.FileUploadUtils;
import br.gov.go.tj.utils.Certificado.VerificaP7s;

/** 
 * Classe que retorna um arquivo P7s baseado no conteúdo devolvido pelo applet
 */

public class GerenciaArquivo {

	private static GerenciaArquivo objeto = null;

	private GerenciaArquivo() {
	}

	public static GerenciaArquivo getInstancia() throws Exception {
		if (objeto == null) objeto = new GerenciaArquivo();

		return objeto;
	}

	/**
	 * Gera arquivo p7s, valida a assinatura e verifica a exitência de vírus
	 * @throws GeneralSecurityException 
	 * @throws CertificateException 
	 * @throws MensagemException 
	 */
	public void getArquivoP7s(ArquivoDt arquivo) throws Exception{

		//if (!arquivo.temContentType()) {
		// Setando tipo de conteúdo do arquivo
		//String extensao = getExtensao(arquivo.getNomeArquivo());
		String contentType = getTipoConteudo(arquivo.getNomeArquivo());
		if (contentType == null) {
			throw new MensagemException(String.format("A extensão do arquivo %s é inválida, favor inserir apenas pdf, mp3 e html.", arquivo.getNomeArquivo()));
		}
		arquivo.setContentType(contentType);
		//}

		// Setando nome do arquivo
		if (!arquivo.getNomeArquivo().contains(".p7s")) {
			arquivo.setNomeArquivo(arquivo.getNomeArquivo() + ".p7s");
		}

		// Capturando conteúdo assinado pelo applet
		CMSSignedData conteudoAssinado = getSignedData(arquivo.conteudoBytes());

		// Verifica existência de vírus
		//VerificaVirus(conteudoAssinado);

		// Verifica integridade da assinatura e seta usuário assinador
		arquivo.setUsuarioAssinador(validarPegarAssinaturas(conteudoAssinado));
		arquivo.setAssinado(true);

		// Seta conteúdo assinado no arquivo
		arquivo.setArquivo(conteudoAssinado.getEncoded());
	}

	/**
	 * Gera P7s do arquivo digitado no editor de texto
	 */
	/*
	private void montaArquivoOnLineP7s(ArquivoDt arquivo){

		// Sendo do editor de texto, o nome será padrão
		arquivo.setNomeArquivo("online.html.p7s");

		// Sendo do editor de texto, o conteúdo do arquivo será padrão
		arquivo.setContentType("text/html");

		// Capturando conteúdo assinado pelo applet 
		CMSSignedData conteudoAssinado = getSignedData(arquivo.getArquivo());

		//Verifica existência de vírus
		VerificaVirus(conteudoAssinado);

		// Verifica integridade da assinatura e seta usuário assinador
		arquivo.setUsuarioAssinador(VerificaAssinatura(conteudoAssinado));

		// Seta conteúdo assinado no arquivo
		arquivo.setArquivo(conteudoAssinado.getEncoded());
	}
	*/
	
	/**
	 * Gera arquivo p7s, valida a assinatura. Arquivos que são assinados pelo servitium
	 * @throws GeneralSecurityException 
	 * @throws CertificateException 
	 * @throws MensagemException 
	 */
	public void getArquivoP7sFromA3(ArquivoDt arquivo) throws Exception{
		
		// Setando tipo de conteúdo do arquivo
		String extensao = getExtensao(arquivo.getNomeArquivo());
		String contentType = getTipoConteudo(extensao);
		if (contentType == null) throw new MensagemException(String.format("A extensão do arquivo %s é inválida, favor inserir apenas pdf, mp3 e html.", arquivo.getNomeArquivo()));
		arquivo.setContentType(contentType);

		// Setando nome do arquivo
		if (!arquivo.getNomeArquivo().contains(".p7s")) {
			arquivo.setNomeArquivo(arquivo.getNomeArquivo() + ".p7s");
		}		

		// Capturando conteúdo assinado pelo applet
		CMSSignedData conteudoAssinado = getSignedData(arquivo.getArquivo());
		
		// Verifica integridade da assinatura e seta usuário assinador
		arquivo.setUsuarioAssinador(validarPegarAssinaturas(conteudoAssinado));

		// Seta conteúdo assinado no arquivo
		arquivo.setArquivo(conteudoAssinado.getEncoded());
		
	}
	
	
	/**
	 * Verifica o tipo de arquivo inserido pela extensão
	 */
	public String getTipoConteudo(String arquivoNome) {
		String tipoConteudo = null;
		if (arquivoNome.toLowerCase().contains(".pdf.p7s") || arquivoNome.toLowerCase().contains("pdf")) tipoConteudo = "application/pdf";
		else if (arquivoNome.toLowerCase().contains(".mp3.p7s") || arquivoNome.toLowerCase().contains("mp3")) tipoConteudo = "audio/mpeg";
		else if (arquivoNome.toLowerCase().contains(".html.p7s") || arquivoNome.toLowerCase().contains("html")) tipoConteudo = "text/html";

		return tipoConteudo;
	}

	/**
	 * Retorna a extensão de uma arquivo inserido
	 */
	private String getExtensao(String nome) {	
		if (nome == null || nome.trim().length() == 0 || !nome.contains(".")) return "";
		String vetorNomeExtensao[] = nome.split("\\.");
		if (vetorNomeExtensao == null || vetorNomeExtensao.length == 1) return "";
		return vetorNomeExtensao[vetorNomeExtensao.length -1];
	}

	/**
	 * Trata arquivo já assinado anteriormente
	 */
	/*private ArquivoDt verificaArquivoAssinado(FileItem item){
		ArquivoDt arquivo = new ArquivoDt();

		// Seta nome do arquivo
		arquivo.setNome(getNome(item));

		//Seta tipo de conteúdo do arquivo
		String mime = "application/x-octet-stream";
		try{
			Magic parser = new Magic();
			parser.initialize();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			new CMSSignedData(item.get()).getSignedContent().write(out);
			MagicMatch match = parser.getMagicMatch(out.toByteArray());
			mime = match.getMimeType();
		} catch(Exception e) {

		}
		arquivo.setContentType(mime);

		// Verifica existência de vírus
		//arquivo.setScan(false);
		VerificaVirus(new CMSSignedData(item.getInputStream()));
		//arquivo.setScan(true);

		// Verifica integridade da assinatura e seta usuário assinador
		arquivo.setUsuarioAssinador(VerificaAssinatura(new CMSSignedData(item.getInputStream())));
		//arquivo.setAssinado(true);

		// Seta conteúdo assinado no arquivo
		arquivo.setConteudo(item.get());

		return arquivo;
	}*/

	/**
	 * Método que verifica assinatura e retorna os certificados do(s) usuário(s) assinante(s)
	 * @param CMSSignedData, signedRaw conteúdo assinado
	 * @return String, Com a cadeia de certificados do(s) usuário(s) assinante(s)
	 * @throws GeneralSecurityException 
	 * @throws CertificateException 
	 * @throws Exception 
	 */
	public String validarPegarAssinaturas(CMSSignedData conteudoAssinado) throws Exception{
		ProjudiPropriedades.getInstance();
		
		//verifica as assinatuas
		Vector certsAssinantes = new FileUploadUtils().verifica(conteudoAssinado);

		// Monta string com assinantes
		int cont = 1;
		StringBuffer assinantes = new StringBuffer("");
		while (cont <= certsAssinantes.size()) {
			if (cont > 1 && certsAssinantes.size() > 1) assinantes.append(",");
			assinantes.append(((X509Certificate) certsAssinantes.get(cont - 1)).getSubjectDN().getName());
			cont += 1;
		}
		return assinantes.toString();

	}

	/**
	 * Verifica a existência de vírus
	 */
	public void VerificaVirus(CMSSignedData data) throws Exception {
		Socket s = null;
		Socket v = null;
		BufferedReader r = null;
		OutputStream out = null;
		OutputStream os = null;
		String result = null;
		try{
			s = new Socket("localhost", 3310);
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = s.getOutputStream();
			out.write(new byte[] {'S', 'T', 'R', 'E', 'A', 'M', '\n' });
			String saida = r.readLine();
			int port = Funcoes.StringToInt(saida.substring(5));
			v = new Socket("localhost", port);
			os = v.getOutputStream();
			os.write(data.getEncoded());
			result = r.readLine();
			
			os.close();
			v.close();			
			r.close();
			s.close();	
			out.close();
			
		} catch(Exception e) {
			try{if (os!=null) os.close(); } catch(Exception ex ) {};
			try{if (out!=null) out.close(); } catch(Exception ex ) {};
			try{if (v!=null) v.close(); } catch(Exception ex ) {};
			try{if (r!=null) r.close(); } catch(Exception ex ) {};
			try{if (s!=null) s.close(); } catch(Exception ex ) {};
			throw e;
		}
		if (result.substring(8) != null) {throw new MensagemException("FileUpload - Arquivo com vírus: " + (result.substring(8).substring(0, result.substring(8).length() - 5))); }
	}

	/**
	 * Captura e processa o conteúdo assinado pelo applet
	 */
	public CMSSignedData getSignedData(String signature) throws Exception {
		CMSSignedData s = null;
		ByteArrayInputStream bIn = new ByteArrayInputStream(Base64Utils.base64Decode(signature));
		ASN1InputStream aIn = new ASN1InputStream(bIn);
		s = new CMSSignedData(ContentInfo.getInstance(aIn.readObject()));
		aIn.close();
		return s;
	}
	/**
	 * Captura e processa o conteúdo assinado pelo applet
	 */
	public CMSSignedData getSignedData(byte[] signature) throws Exception {
		CMSSignedData s = null;
		ByteArrayInputStream bIn = new ByteArrayInputStream(signature);
		ASN1InputStream aIn = new ASN1InputStream(bIn);
		s = new CMSSignedData(ContentInfo.getInstance(aIn.readObject()));
		aIn.close();
		return s;
	}
	
	/**
	 * Método para pegar os assinantes do arquivo
	 * @param CMSSignedData, signedRaw conteúdo assinado
	 * @return String, Com a cadeia de certificados do(s) usuário(s) assinante(s)
	 * @throws GeneralSecurityException 
	 * @throws CertificateException 
	 * @throws Exception 
	 */
	public String pegarAssinaturas(CMSSignedData conteudoAssinado) throws Exception{
				
		//verifica as assinatuas
		Vector certsAssinantes = new VerificaP7s().getSigners(conteudoAssinado);

		// Monta string com assinantes
		int cont = 1;
		StringBuffer assinantes = new StringBuffer("");
		while (cont <= certsAssinantes.size()) {
			if (cont > 1 && certsAssinantes.size() > 1) assinantes.append(",");
			assinantes.append(((X509Certificate) certsAssinantes.get(cont - 1)).getSubjectDN().getName());
			cont += 1;
		}
		return assinantes.toString();

	}

	/**
	 * Método para pegar os assinantes do arquivo
	 * @param CMSSignedData, signedRaw conteúdo assinado
	 * @return String, Com a cadeia de certificados do(s) usuário(s) assinante(s)
	 * @throws GeneralSecurityException 
	 * @throws CertificateException 
	 * @throws Exception 
	 */
	public String pegarAssinaturas(byte[] conteudoBytes) throws Exception{
				
		// Capturando conteúdo assinado pelo applet
		CMSSignedData conteudoAssinado = getSignedData(conteudoBytes);
				
		//verifica as assinatuas
		Vector certsAssinantes = new VerificaP7s().getSigners(conteudoAssinado);

		// Monta string com assinantes
		int cont = 1;
		StringBuffer assinantes = new StringBuffer("");
		while (cont <= certsAssinantes.size()) {
			if (cont > 1 && certsAssinantes.size() > 1) assinantes.append(",");
			assinantes.append(((X509Certificate) certsAssinantes.get(cont - 1)).getSubjectDN().getName());
			cont += 1;
		}
		return assinantes.toString();

	}
	

}