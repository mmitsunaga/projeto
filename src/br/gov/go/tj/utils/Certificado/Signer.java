package br.gov.go.tj.utils.Certificado;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.ne.CertificadoNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class Signer {

    /**
     * 
     * Método que assina um array de bytes
     * 
     * @return (byte[]) Retorna um array de bytes com o conteúdo e assinatura
     *         digital.
     * 
     * @throws SignerException,
     *             Exception
     */
    public static byte[] signBuffer(byte[] buffer, X509Certificate origCert, PrivateKey pK, Certificate[] certChain) throws Exception{

        try{
            ArrayList certList = new ArrayList();
            CMSProcessable msg = new CMSProcessableByteArray(buffer);

            if (certChain != null && certChain.length != 0) {
                for (int i = 0; i < certChain.length; i++) {
                    certList.add(certChain[i]);
                }
            }

            CertStore certs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

            gen.addSigner(pK, origCert, CMSSignedDataGenerator.DIGEST_SHA1);
            gen.addCertificatesAndCRLs(certs);

            CMSSignedData s = gen.generate(msg, true, "BC");
            return s.getEncoded();

        } catch(Exception e) {
            throw new MensagemException("Erro ao assinar dados! | Signer.signBuffer(byte[] buffer, X509Certificate origCert, PrivateKey pK, Certificate[] certChain) |");
        }
    }

    /**
     * 
     * Método que verifica a assinatura do recibo do projudi
     * 
     * @return (Boolean) Retorna true se a assinatura for válida ou false se não
     *         for.
     * @throws CMSException 
     * @throws NoSuchProviderException 
     * @throws NoSuchAlgorithmException 
     * @throws CertificateNotYetValidException 
     * @throws CertificateExpiredException 
     * 
     * @throws Exception
     */
    public static Boolean verifySig(byte[] signedRaw)throws Exception{

        boolean result = false;
        // Security.addProvider(new BouncyCastleProvider());

        ByteArrayInputStream bIn = new ByteArrayInputStream(signedRaw);
        ASN1InputStream aIn = new ASN1InputStream(bIn);
        CMSSignedData s = new CMSSignedData(ContentInfo.getInstance(aIn.readObject()));
        aIn.close();
        CertStore certs = s.getCertificatesAndCRLs("Collection", "BC");
        SignerInformationStore signers1 = s.getSignerInfos();

        Collection c = signers1.getSigners();
        Iterator it = c.iterator();

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = certs.getCertificates(signer.getSID());
            Iterator certIt = certCollection.iterator();
            X509Certificate cert = (X509Certificate) certIt.next();
            result = signer.verify(cert, "BC");

            // TESTA SE CERTIFICADO ESTÁ REVOGADO
            CertificadoNe certificadoNe = new CertificadoNe();
            CertificadoDt certProjudi = certificadoNe.consultarId(String.valueOf(cert.getSerialNumber()));
            if (certProjudi != null) {
                if (!certProjudi.ehValidoHoje()) {
                    throw new GeneralSecurityException("<{Arquivo assinado com certificado revogado.}> Local Exception: Signer.verifySig(byte[] signedRaw)");
                }
            }
        }

        return result;
    }

    public static byte[] concat(byte[] arr1, byte[] arr2) {
        byte[] resByteArr = new byte[arr1.length + arr2.length];

        for (int i = 0; i < arr1.length; ++i)
            resByteArr[i] = arr1[i];

        int j = arr1.length;
        for (int i = 0; i < arr2.length; ++i) {
            resByteArr[j] = arr2[i];
            ++j;
        }
        return resByteArr;

    }

//    /**
//     * 
//     * Método que assina o recibo do projudi e disponibiliza o arquivo para
//     * download.
//     * @throws CMSException 
//     * @throws NoSuchProviderException 
//     * @throws NoSuchAlgorithmException 
//     * @throws CertificateNotYetValidException 
//     * @throws CertificateExpiredException 
//     * 
//     * @throws Exception
//     */
//    public static void gerarRecibo(String id_Movimentacao, String numeroProcesso, ArquivoDt arquivoDt) throws Exception{
//        CertificadoNe certificadoNe = new CertificadoNe();
//        CertificadoDt certificadoDt = null;
//        certificadoDt = certificadoNe.consultaCertificadoSistema();
//        // InputStream pkcs12 = new
//        // FileInputStream(ProjudiConfiguration.getInstance().getCaminhoIdentidades()+certificadoNe.consultaCaminhoCertificadoValidoSistema());
//        ByteArrayInputStream pkcs12 = new ByteArrayInputStream(certificadoDt.getConteudo());
//        PKCS12Parser p12parser = new PKCS12Parser(pkcs12, ProjudiPropriedades.getInstance().getSenhaIdentidadeDigitalSistema());
//
//        // CONTEÚDO DO
//        // RECIBO*************************************************************************************************************
//        SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        StringBuffer strRecibo = new StringBuffer("2;#!@;" + arquivoDt.getNomeArquivo().substring(0, arquivoDt.getNomeArquivo().length() - 4) + ";#!@;" + FormatoData.format(new Date()) + ";#!@;");
//        strRecibo.append("@#@!@" + Funcoes.formataNumeroProcesso(numeroProcesso) + "@#@!@" + id_Movimentacao);
//        strRecibo.append("@#@!@&#!@&Recibo comprovando o recebimento de arquivo no Sistema Projudi.&#!@&#@&!%#");
//        // ********************************************************************************************************************************
//
//        byte[] result = signBuffer(concat(strRecibo.toString().getBytes(), arquivoDt.conteudoBytes()), p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());
//
//        ////System.out.println("Arquivo Assinado: " + arquivoDt.conteudoBytes().length);
//        ////System.out.println("String Recibo: " + strRecibo.toString().getBytes().length);
//        ////System.out.println("ReciboArquivo Assinado: " + result.length);
//
//        if (verifySig(result)) {
//                arquivoDt.setArquivo(result);
//            
//        }
//    }

//    public static void gerarRecibo(List movimentacoes, ArquivoDt arquivoDt)throws Exception{
//        CertificadoNe certificadoNe = new CertificadoNe();
//        CertificadoDt certificadoDt = null;
//        // InputStream pkcs12 = new
//        // FileInputStream(ProjudiConfiguration.getInstance().getCaminhoIdentidades()+certificadoNe.consultaCaminhoCertificadoValidoSistema());
//        certificadoDt = certificadoNe.consultaCertificadoSistema();
//        if (certificadoDt==null) {
//        	throw new MensagemException("Certificado do Sistema não encontrado, favor verificar a validade do mesmo");	
//        }
//        try {
//            ByteArrayInputStream pkcs12 = new ByteArrayInputStream(certificadoDt.getConteudo());
//            PKCS12Parser p12parser = new PKCS12Parser(pkcs12, ProjudiPropriedades.getInstance().getSenhaIdentidadeDigitalSistema());
//    
//            SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            StringBuffer strRecibo = new StringBuffer("2;#!@;" + arquivoDt.getNomeArquivo().substring(0, arquivoDt.getNomeArquivo().length() - 4) + ";#!@;" + FormatoData.format(new Date()) + ";#!@;");
//    
//            for (int i = 0; i < movimentacoes.size(); i++) {
//                MovimentacaoDt movimentacaoDt = (MovimentacaoDt) movimentacoes.get(i);
//                // CONTEÚDO DO RECIBO*********************************************************************************************
//                strRecibo.append("@#@!@" + Funcoes.formataNumeroProcesso(movimentacaoDt.getProcessoNumero()) + "@#@!@" + movimentacaoDt.getId());
//                // ****************************************************************************************************************
//            }
//            strRecibo.append("@#@!@&#!@&Recibo comprovando o recebimento de arquivo no Sistema Projudi.&#!@&#@&!%#");
//            byte[] result = signBuffer(concat(strRecibo.toString().getBytes(), arquivoDt.conteudoBytes()), p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());
//    
//            ////System.out.println("Arquivo Assinado: " + arquivoDt.conteudoBytes().length);
//            ////System.out.println("String Recibo: " + strRecibo.toString().getBytes().length);
//            ////System.out.println("ReciboArquivo Assinado: " + result.length);
//    
//            if (verifySig(result)) {
//            	//o setRecido inserie o conteudo do arquivo e seta o boRecibo igual true, igual ao setArquivo mais boRecibo = true
//            	arquivoDt.setArquivo(result);            	
//            } 
//            
//        }catch (Exception e) {
//        	throw new MensagemException("Não foi possível gerar o Recibo do arquivo.");
//		}
//    }
    
//    public static void alterarRecibo(String id_Movimentacao, String numeroProcesso, ArquivoDt arquivoDt)throws Exception{
//    	List movimentacoes = new ArrayList();
//    	
//    	MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
//    	movimentacaoDt.setId(id_Movimentacao);
//    	movimentacaoDt.setProcessoNumero(numeroProcesso);
//    	movimentacoes.add(movimentacaoDt);
//    	
//    	alterarRecibo(movimentacoes, arquivoDt);
//    }

//    public static void alterarRecibo(List movimentacoes, ArquivoDt arquivoDt)throws Exception{
//        CertificadoNe certificadoNe = new CertificadoNe();
//        CertificadoDt certificadoDt = null;
//        // InputStream pkcs12 = new
//        // FileInputStream(ProjudiConfiguration.getInstance().getCaminhoIdentidades()+certificadoNe.consultaCaminhoCertificadoValidoSistema());
//        certificadoDt = certificadoNe.consultaCertificadoSistema();
//        ByteArrayInputStream pkcs12 = new ByteArrayInputStream(certificadoDt.getConteudo());
//        PKCS12Parser p12parser = new PKCS12Parser(pkcs12, ProjudiPropriedades.getInstance().getSenhaIdentidadeDigitalSistema());
//
//        SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        StringBuffer strRecibo = new StringBuffer("2;#!@;" + arquivoDt.getNomeArquivo().substring(0, arquivoDt.getNomeArquivo().length() - 4) + ";#!@;" + FormatoData.format(new Date()) + ";#!@;");
//
//        String[] stParteProcessoMovimentacao = Signer.conteudoRecibo(arquivoDt);
//        if (stParteProcessoMovimentacao != null) {
//            for (int i = 1; i < stParteProcessoMovimentacao.length - 1; i++) {
//                // CONTEÚDO DE RECIBO JÁ
//                // EXISTENTE********************************************************************************
//                strRecibo.append("@#@!@" + stParteProcessoMovimentacao[i]);
//                // ****************************************************************************************************************
//            }
//        }
//
//        for (int i = 0; i < movimentacoes.size(); i++) {
//            MovimentacaoDt movimentacaoDt = (MovimentacaoDt) movimentacoes.get(i);
//            // CONTEÚDO DO
//            // RECIBO*********************************************************************************************
//            strRecibo.append("@#@!@" + Funcoes.formataNumeroProcesso(movimentacaoDt.getProcessoNumero()) + "@#@!@" + movimentacaoDt.getId());
//            // ****************************************************************************************************************
//        }
//        strRecibo.append("@#@!@&#!@&Recibo comprovando o recebimento de arquivo no Sistema Projudi.&#!@&#@&!%#");
//        byte[] result = signBuffer(concat(strRecibo.toString().getBytes(), Signer.extrairConteudoRecibo(new ByteArrayInputStream(arquivoDt.conteudoBytes())).getEncoded()), p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());
//
//        ////System.out.println("Arquivo Assinado: " + arquivoDt.conteudoBytes().length);
//        ////System.out.println("String Recibo: " + strRecibo.toString().getBytes().length);
//        ////System.out.println("ReciboArquivo Assinado: " + result.length);
//
//        if (verifySig(result)) {
//        	arquivoDt.setArquivo(result);
//        } else {
//
//        }
//    }

    public static String[] conteudoRecibo(ArquivoDt arquivoDt)throws Exception{
        String[] stParteProcessoMovimentacao = null;
        ByteArrayOutputStream out = null;
        ByteArrayOutputStream out2 = null;
        try{
            // LÊ CONTEÚDO DO ARQUIVO E CRIA UM CmsSignedData
            // CONTEUDO RECIBO E ARQUIVO
            // **************************************************************************************
            out = new ByteArrayOutputStream();
            CMSSignedData dadosConteudo = new CMSSignedData(arquivoDt.conteudoBytes());
            CMSProcessable conteudoReciboArquvio = dadosConteudo.getSignedContent();
            conteudoReciboArquvio.write(out);
            byte[] byteTotalReciboArquivo = out.toByteArray();

            // CONTEUDO RECIBO
            // **********************************************************************************************
            byte[] byteRecibo = new byte[byteTotalReciboArquivo.length];
            System.arraycopy(byteTotalReciboArquivo, 0, byteRecibo, 0, byteTotalReciboArquivo.length);
            out2 = new ByteArrayOutputStream();
            out2.write(byteRecibo);
            
            String[] tipoRecibo =  out2.toString().split(";#");
            
    		if (tipoRecibo[0].equals("1")) {
    			stParteProcessoMovimentacao = out2.toString().split("@#@");
    		} else if (tipoRecibo[0].equals("2")){
    			stParteProcessoMovimentacao = out2.toString().split("@#@!@");
    		}

//            for (int i = 1; i < stParteProcessoMovimentacao.length - 1; i++) {
//                if (i % 2 != 0)
//                    ////System.out.println(" Nº Processo.............: " + stParteProcessoMovimentacao[i] + "\n");
//                else
                    ////System.out.println(" Nº Movimentação.....: " + stParteProcessoMovimentacao[i] + "\n");
//            }
    		out.close();
    		out2.close();
        } catch(Exception e) {
        	try{if (out!=null) out.close(); } catch(Exception ex ) {};
        	try{if (out2!=null) out2.close(); } catch(Exception ex ) {};
        	
            throw e;            
        }
        return stParteProcessoMovimentacao;
    }

    public static byte[] extrairConteudoP7s(byte[] dados)throws Exception{
        CMSSignedData objAssinatura = new CMSSignedData(dados);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CMSProcessable conteudo = objAssinatura.getSignedContent();
        conteudo.write(out);
        conteudo = null;
        objAssinatura = null;
        return out.toByteArray();
    }

    public static byte[] extrairConteudoP7sRecibo(byte[] dados)throws Exception{
        CMSSignedData objAssinatura = Signer.extrairConteudoRecibo(dados);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CMSProcessable conteudo = objAssinatura.getSignedContent();
        conteudo.write(out);
        conteudo = null;
        objAssinatura = null;
        return out.toByteArray();
    }
    
    public static byte[] extrairP7sRecibo(byte[] dados)throws Exception{
        CMSSignedData objAssinatura = Signer.extrairConteudoRecibo(dados);
        return objAssinatura.getEncoded();
    }

    public static CMSSignedData extrairConteudoRecibo(byte[] dados)throws Exception{

        return extrairConteudoRecibo(new ByteArrayInputStream(dados));
    }

    public static CMSSignedData extrairConteudoRecibo(InputStream inputStream)throws Exception{
        CMSSignedData dados = null;
        // CONTEUDO RECIBO E ARQUIVO
        // **************************************************************************************
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CMSSignedData dadosConteudo = new CMSSignedData(inputStream);
        CMSProcessable conteudoReciboArquvio = dadosConteudo.getSignedContent();
        conteudoReciboArquvio.write(out);
        byte[] byteTotalReciboArquivo = out.toByteArray();
        String[] tipoRecibo =  out.toString().split(";#");
		
        int tamanhoRecibo = 0;
        
		if (tipoRecibo[0].equals("1")) {
	        String[] stringDivisao = out.toString().split("#@&!%", 0);
	        tamanhoRecibo = stringDivisao[0].getBytes().length + 5;
		} else if (tipoRecibo[0].equals("2")){
			String[] stringDivisao = out.toString().split("#@&!%#", 0);
		    tamanhoRecibo = stringDivisao[0].getBytes().length + 6;
		}

        // CONTEUDO ARQUIVO
        // **********************************************************************************************
        byte[] byteArquivo = new byte[byteTotalReciboArquivo.length - tamanhoRecibo];
        System.arraycopy(byteTotalReciboArquivo, tamanhoRecibo, byteArquivo, 0, byteArquivo.length);
        dados = new CMSSignedData(byteArquivo);
        return dados;
    }
    
    public static  byte[] extrairConteudoReciboArquivoNaoAssinado(InputStream inputStream)throws Exception{
        // CONTEUDO RECIBO E ARQUIVO
        // **************************************************************************************
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CMSSignedData dadosConteudo = new CMSSignedData(inputStream);
        CMSProcessable conteudoReciboArquvio = dadosConteudo.getSignedContent();
        conteudoReciboArquvio.write(out);
        byte[] byteTotalReciboArquivo = out.toByteArray();
        String[] tipoRecibo =  out.toString().split(";#");
		
        int tamanhoRecibo = 0;
        
		if (tipoRecibo[0].equals("1")) {
	        String[] stringDivisao = out.toString().split("#@&!%", 0);
	        tamanhoRecibo = stringDivisao[0].getBytes().length + 5;
		} else if (tipoRecibo[0].equals("2")){
			String[] stringDivisao = out.toString().split("#@&!%#", 0);
		    tamanhoRecibo = stringDivisao[0].getBytes().length + 6;
		}

        // CONTEUDO ARQUIVO
        // **********************************************************************************************
        byte[] byteArquivo = new byte[byteTotalReciboArquivo.length - tamanhoRecibo];
        System.arraycopy(byteTotalReciboArquivo, tamanhoRecibo, byteArquivo, 0, byteArquivo.length);
        return byteArquivo;
    }

    /**
     * 
     * Método que assina o conteúdo de um arquivo com o certificado do Sistema
     * Projudi. Será usado nos casos em que o arquivo é gerado pelo Projudi e
     * deve ser assinado, por exemplo, o arquivo de verificar prevenção em
     * processo.
     * 
     * @throws Exception
     */
    public static void assinaArquivoCertificadoSistema(ArquivoDt arquivoDt)throws Exception{

        CertificadoNe certificadoNe = new CertificadoNe();
        CertificadoDt certificadoDt = certificadoNe.consultaCertificadoSistema();

        ByteArrayInputStream pkcs12 = new ByteArrayInputStream(certificadoDt.getConteudo());
        PKCS12Parser p12parser = new PKCS12Parser(pkcs12, ProjudiPropriedades.getInstance().getSenhaIdentidadeDigitalSistema());

        byte[] result = signBuffer(arquivoDt.conteudoBytes(), p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());

        if (verifySig(result)) {
        	arquivoDt.setArquivo(result);
            arquivoDt.setUsuarioAssinador(p12parser.getCertificate().getSubjectDN().getName());
        }
    }

    public static void acceptSSL() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

        // Install the all-trusting trust manager
        try{
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch(Exception e) {
        }
    }

	public static void signBuffer(ArquivoDt dtArquivo, X509Certificate origCert, PrivateKey pK, Certificate[] certChain) throws Exception{
        try{
            ArrayList certList = new ArrayList();
            byte[] buffer = dtArquivo.conteudoBytes();
            CMSProcessable msg = new CMSProcessableByteArray(buffer);

            if (certChain != null && certChain.length != 0) {            	
                for (int i = 0; i < certChain.length; i++) {
                    certList.add(certChain[i]);
                    
                }
            }

            dtArquivo.addUsuarioAssinador(origCert.getSubjectDN().getName());
    		
            CertStore certs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

            gen.addSigner(pK, origCert, CMSSignedDataGenerator.DIGEST_SHA1);
            gen.addCertificatesAndCRLs(certs);

            CMSSignedData s = gen.generate(msg, true, "BC");
            dtArquivo.setArquivo( s.getEncoded());
            // Setando nome do arquivo
            dtArquivo.setNomeArquivo(dtArquivo.getNomeArquivo() + ".p7s");
    		
        } catch(Exception e) {
            throw new MensagemException("Erro ao assinar dados! | Signer.signBuffer(byte[] buffer, X509Certificate origCert, PrivateKey pK, Certificate[] certChain) |");
        }
	}
	
    public static void gerarReciboECarta(String id_Movimentacao, String numeroProcesso, ArquivoDt arquivoDt, String dataHoraMovimentacao) throws Exception{
        CertificadoNe certificadoNe = new CertificadoNe();
        CertificadoDt certificadoDt = null;
        certificadoDt = certificadoNe.consultaCertificadoSistema();
        // InputStream pkcs12 = new
        // FileInputStream(ProjudiConfiguration.getInstance().getCaminhoIdentidades()+certificadoNe.consultaCaminhoCertificadoValidoSistema());
        ByteArrayInputStream pkcs12 = new ByteArrayInputStream(certificadoDt.getConteudo());
        PKCS12Parser p12parser = new PKCS12Parser(pkcs12, ProjudiPropriedades.getInstance().getSenhaIdentidadeDigitalSistema());

        // CONTEÚDO DO
        // RECIBO*************************************************************************************************************
        SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuffer strRecibo = new StringBuffer("2;#!@;" + arquivoDt.getNomeArquivo().substring(0, arquivoDt.getNomeArquivo().length() - 4) + ";#!@;" + dataHoraMovimentacao + ";#!@;");
        strRecibo.append("@#@!@" + Funcoes.formataNumeroProcesso(numeroProcesso) + "@#@!@" + id_Movimentacao);
        strRecibo.append("@#@!@&#!@&Recibo comprovando o recebimento de arquivo no Sistema Projudi.&#!@&#@&!%#");
        // ********************************************************************************************************************************

        byte[] result = signBuffer(concat(strRecibo.toString().getBytes(), arquivoDt.conteudoBytes()), p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());

        ////System.out.println("Arquivo Assinado: " + arquivoDt.conteudoBytes().length);
        ////System.out.println("String Recibo: " + strRecibo.toString().getBytes().length);
        ////System.out.println("ReciboArquivo Assinado: " + result.length);

        if (verifySig(result)) {
                arquivoDt.setArquivo(result);
            
        }
    }

    public static void gerarRecibo(MovimentacaoDt movimentacaoDt, ArquivoDt arquivoDt)throws Exception{
        CertificadoNe certificadoNe = new CertificadoNe();
        CertificadoDt certificadoDt = null;
        // InputStream pkcs12 = new
        // FileInputStream(ProjudiConfiguration.getInstance().getCaminhoIdentidades()+certificadoNe.consultaCaminhoCertificadoValidoSistema());
        certificadoDt = certificadoNe.consultaCertificadoSistema();
        if (certificadoDt==null) {
        	throw new MensagemException("Certificado do Sistema não encontrado, favor verificar a validade do mesmo");	
        }
        try {
            ByteArrayInputStream pkcs12 = new ByteArrayInputStream(certificadoDt.getConteudo());
            PKCS12Parser p12parser = new PKCS12Parser(pkcs12, ProjudiPropriedades.getInstance().getSenhaIdentidadeDigitalSistema());
    
            SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            StringBuffer strRecibo = new StringBuffer("2;#!@;" + arquivoDt.getNomeArquivo().substring(0, arquivoDt.getNomeArquivo().length() - 4) + ";#!@;" + FormatoData.format(new Date()) + ";#!@;");
                                
            // CONTEÚDO DO RECIBO*********************************************************************************************
            strRecibo.append("@#@!@" + Funcoes.formataNumeroProcesso(movimentacaoDt.getProcessoNumero()) + "@#@!@" + movimentacaoDt.getId());
            // ****************************************************************************************************************
            
            strRecibo.append("@#@!@&#!@&Recibo comprovando o recebimento de arquivo no Sistema Projudi.&#!@&#@&!%#");
            byte[] result = signBuffer(concat(strRecibo.toString().getBytes(), arquivoDt.conteudoBytes()), p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());
    
            ////System.out.println("Arquivo Assinado: " + arquivoDt.conteudoBytes().length);
            ////System.out.println("String Recibo: " + strRecibo.toString().getBytes().length);
            ////System.out.println("ReciboArquivo Assinado: " + result.length);
    
            //if (verifySig(result)) {
           	//o setRecido inserie o conteudo do arquivo e seta o boRecibo igual true, igual ao setArquivo mais boRecibo = true
            arquivoDt.setArquivo(result);            	
           // } 
            
        }catch (Exception e) {
        	throw new MensagemException("Não foi possível gerar o Recibo do arquivo.");
		}
    }
}
