package br.gov.go.tj.projudi.sessaoVirtual.utils;

import java.io.ByteArrayInputStream;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.PKCS12Parser;
import br.gov.go.tj.utils.Certificado.Signer;

public class AssinaturaArquivoUtils{
	   
    public static ArquivoDt assinarString(String senhaCertificado,  boolean salvarSenha, UsuarioNe usuarioSessao, String arquivoTexto) throws Exception{
    	ArquivoDt arquivoAssinatura = new ArquivoDt();
    	arquivoAssinatura.setArquivo(arquivoTexto);
    	assinar(senhaCertificado,  salvarSenha, usuarioSessao,  arquivoAssinatura);
    	return arquivoAssinatura;
    }
           
    public static ArquivoDt assinarComIdArquivo(String senhaCertificado,  boolean salvarSenha, UsuarioNe usuarioSessao, String idArquivo) throws Exception{
    	ArquivoDt arquivoAssinatura = new ArquivoNe().consultarId(idArquivo);     	
    	assinar(senhaCertificado, salvarSenha, usuarioSessao,  arquivoAssinatura);
    	return arquivoAssinatura;
    }
    
    public static ArquivoDt assinarArquivo(String senhaCertificado,  boolean salvarSenha, UsuarioNe usuarioSessao, ArquivoDt arquivoDt) throws Exception{
    	assinar(senhaCertificado, salvarSenha, usuarioSessao,  arquivoDt);
    	return arquivoDt;
    }

    private static void assinar(String senhaCertificado,  boolean salvarSenha, UsuarioNe usuarioSessao, ArquivoDt arquivoDt ) throws Exception{
		assinarArquivoPKCS12(salvarSenha, usuarioSessao, arquivoDt, carregarCertificadoPKCS12(senhaCertificado, usuarioSessao));		
	}
    
	public static void assinarListaDeArquivos(String senhaCertificado, boolean salvarSenha, UsuarioNe usuarioSessao, List<ArquivoDt> listaArquivos) throws Exception {	
		for (ArquivoDt arquivoDt : listaArquivos) {
			assinarArquivoPKCS12(salvarSenha, usuarioSessao, arquivoDt, carregarCertificadoPKCS12(senhaCertificado, usuarioSessao));
		}
	}

	private static void assinarArquivoPKCS12(boolean salvarSenha, UsuarioNe usuarioSessao, ArquivoDt arquivoDt,
		PKCS12Parser p12parser) throws Exception {
		validarArquivo(arquivoDt);
		
		Signer.signBuffer(arquivoDt, p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());
		arquivoDt.setAssinado(true);
		
		if(usuarioSessao.isSenhaCertificado() && !salvarSenha)
			usuarioSessao.setSenhaCertificado("");
	}
	
	private static PKCS12Parser carregarCertificadoPKCS12(String senhaCertificado, UsuarioNe usuarioSessao) throws Exception {	
		iniciarCertificadoUsuario(usuarioSessao);
		carregarSenha(senhaCertificado, usuarioSessao);
		ByteArrayInputStream pkcs12 = new ByteArrayInputStream(usuarioSessao.getUsuarioDt().getCertificadoConteudo());
		PKCS12Parser p12parser = null;
		try {
			p12parser = new PKCS12Parser(pkcs12, usuarioSessao.getSenhaCertificado());			 
		} catch (Exception e) {
			usuarioSessao.setSenhaCertificado("");
			throw e;
		}
	   return p12parser;
	}

	private static void iniciarCertificadoUsuario(UsuarioNe usuarioSessao) throws Exception {
		if(!usuarioSessao.isCertificadoCarregado())
			usuarioSessao.carregarCertificado();		 
	}
	
	private static void carregarSenha(String senhaCertificado, UsuarioNe usuarioSessao) throws MensagemException{
		validarCampo(senhaCertificado, usuarioSessao);
		 usuarioSessao.setSenhaCertificado(usuarioSessao.isSenhaCertificado() ? usuarioSessao.getSenhaCertificado() : senhaCertificado);
	}
	
	private static void validarArquivo(ArquivoDt arquivoDt){
    	if(arquivoDt == null)
    		throw new NullPointerException("Objeto ArquivoDt não identificado");
    	else if(arquivoDt.getArquivo() == null || arquivoDt.getArquivo().trim().equals(""))
    		throw new NullPointerException("Texto do Arquivo não identificado");
    	else if(arquivoDt.getNomeArquivo().trim().equals(""))
    		arquivoDt.setNomeArquivo("online.html");
	}
	
    private static void validarCampo(String senhaCertificado, UsuarioNe usuarioSessao) throws MensagemException{
    	if(usuarioSessao == null)
    		throw new MensagemException("usuarioSessao");
    	else if(!usuarioSessao.isSenhaCertificado() && (senhaCertificado == null || senhaCertificado.trim().equals("")))
    		throw new MensagemException("O campo senha é obrigatório");    	    		
    }
}