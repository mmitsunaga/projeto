package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.bouncycastle.asn1.x509.CRLReason;
import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.CertificadoPs;
import br.gov.go.tj.projudi.util.Identidade;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Certificado.CertificadoUtils;

public class CertificadoNe extends CertificadoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4187973578729604768L;

	private static List listCertificadosConfiaveis = null;
	
	// ---------------------------------------------------------
	public String Verificar(CertificadoDt dados) {

		String stRetorno = "";

		// if (dados.getUsuarioCertificado()ario().equalsIgnoreCase(""))
		// stRetorno += "O Campo Usuario � obrigat�rio.";
		// if (dados.getLiberado().equalsIgnoreCase(""))
		// stRetorno += "O Campo Liberado � obrigat�rio.";
		// if (dados.getCaminho().equalsIgnoreCase(""))
		// stRetorno += "O Campo Caminho � obrigat�rio.";
		// if (dados.getDataEmissao().equalsIgnoreCase(""))
		// stRetorno += "O Campo DataEmissao � obrigat�rio.";
		// if (dados.getDataExpiracao().equalsIgnoreCase(""))
		// stRetorno += "O Campo DataExpiracao � obrigat�rio.";
		// ////System.out.println("..neCertificadoVerificar()");
		return stRetorno;

	}
	
	public String VerificarCAConfiavel(CertificadoDt dados, String certRaiz) throws Exception{

		String stRetorno = "";

		CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
		InputStream certStream = new ByteArrayInputStream(dados.getConteudo());
		X509Certificate certificate = (X509Certificate) cf.generateCertificate(certStream);
		certStream.close();
		String[] teste = certificate.getSubjectDN().getName().split("CN=");
		dados.setDescricao(teste[1]);
		try{
			certificate.checkValidity(new Date());
		} catch(CertificateExpiredException e) {
			
			
			stRetorno += " Certificado Expirou. ";
		} catch(CertificateNotYetValidException e) {
			
			
			stRetorno += " Certificado N�o � v�lido. ";
		}
		
		if (certRaiz == null || !certRaiz.equals("1")){
			List certificadosConfiaveis = this.consultaCertificadosConfiaveis();
			
			if (certificadosConfiaveis != null && certificadosConfiaveis.size()>0){
				HashSet trustAnchors = new HashSet();
				for (int index = 0; index < certificadosConfiaveis.size(); index++) {
					CertificadoDt certificadoConfiavel = (CertificadoDt)certificadosConfiaveis.get(index);
					InputStream certConfiavelStream = new ByteArrayInputStream(certificadoConfiavel.getConteudo());
					X509Certificate trustedCertificate = (X509Certificate) cf.generateCertificate(certConfiavelStream);
					certConfiavelStream.close();
					TrustAnchor trustAnchor = new TrustAnchor(trustedCertificate, null);
					trustAnchors.add(trustAnchor);
				}
				PKIXParameters certPathValidatorParams = new PKIXParameters(trustAnchors);
				certPathValidatorParams.setRevocationEnabled(false);
				CertPathValidator chainValidator = CertPathValidator.getInstance("PKIX", "BC");
				
				//*************
				List chain = new ArrayList(5);
				chain.add(certificate);
				if (!verifyCertificationChain(cf.generateCertPath(chain), chainValidator, certPathValidatorParams)) {
					stRetorno += " Certificado  n�o confi�vel. ";
				}
			}
		}
		
		return stRetorno;
	}
	
	/**
	 * M�todo que verifica a v�lidade de uma cadeia de certificados
	 * @param CertPath, cadeia a ser v�lidada
	 * @return boolean, verdadeiro se a cadeia for v�lida ou falso se n�o for v�lida
	 * @throws GeneralSecurityException 
	 */
	public boolean verifyCertificationChain(CertPath aCertChain, CertPathValidator chainValidator, PKIXParameters certPathValidatorParams) throws GeneralSecurityException {
		int chainLength = aCertChain.getCertificates().size();
		if (chainLength < 1) {
			return false;
		}
		try{
			chainValidator.validate(aCertChain, certPathValidatorParams);
		} catch(CertPathValidatorException cpve) {
			return false;
		}
		return true;
	}

	/**
	 * M�todo que retorna a quantidade de certificados v�lidos de um usuario
	 * 
	 * @param usuario
	 *            � o usuario cuja quantidade de certificados v�lidos se deseja
	 *            verificar
	 * @return A quantidade de certificados v�lidos de um usuario
	 * @throws Exception
	 */
	public int consultaNumeroCertificadosValido(String idUsuario) throws Exception {
		int qtd;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			qtd = obPersistencia.consultarCertificadoValidoUsuario(idUsuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return qtd;
	}

	/**
	 * 
	 * M�todo que retorna a Identidade Digital do Sistema Projudi
	 * 
	 * @return CertificadoDt, Retorna a Identidade Digital do Sistema  Projudi
	 * 
	 * @throws Exception
	 */
	public CertificadoDt consultaCertificadoSistema() throws Exception {
		CertificadoDt Dados = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			Dados = obPersistencia.consultarCertificadoSistema();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return Dados;
	}
	
	/**
	 * 
	 * M�todo que retorna o certificado raiz do Sistema Projudi
	 * 
	 * @return CertificadoDt, Retorna o certificado raiz do Sistema Projudi
	 * 
	 * 
	 * @throws Exception
	 */
	public CertificadoDt consultaCertificadoRaizSistema() throws Exception {
		CertificadoDt Dados = null;
		FabricaConexao obFabricaConexao = null;
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
						
			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			Dados = obPersistencia.consultarCertificadoRaizSistema();
		
		} finally{
			obFabricaConexao.fecharConexao();			
		}
		return Dados;
	}
	
	/**
	 * 
	 * M�todo que retorna o certificado raiz do Sistema Projudi
	 * 
	 * @return CertificadoDt, Retorna certificado raiz do Sistema  Projudi
	 * 
	 * @throws Exception
	 */
	public CertificadoDt consultaCertificadoRaizSistema(FabricaConexao obFabricaConexao) throws Exception {
		CertificadoDt Dados = null;

		CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
		Dados = obPersistencia.consultarCertificadoRaizSistema();
				
		return Dados;
	}


	/**
	 * 
	 * M�todo que retorna o certificado emissor do Sistema Projudi
	 * 
	 * @return CertificadoDt, Retorna o certificado emissor do Sistema Projudi
	 * 
	 * @throws Exception
	 */
	public CertificadoDt consultaCertificadoEmissorSistema(FabricaConexao obFabricaConexao) throws Exception {
		CertificadoDt Dados = null;

		CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
		Dados = obPersistencia.consultarCertificadoEmissorSistema();
		
		return Dados;
	}
	
	/**
	 * 
	 * M�todo que retorna o certificado emissor do Sistema Projudi
	 * 
	 * @return CertificadoDt, Retorna o certificado emissor do Sistema Projudi
	 * 
	 * @throws Exception
	 */
	public CertificadoDt consultaCertificadoEmissorSistema() throws Exception {
		CertificadoDt Dados = null;
		FabricaConexao obFabricaConexao = null;
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			Dados = obPersistencia.consultarCertificadoEmissorSistema();
		
		} finally{			
			obFabricaConexao.fecharConexao();			
		}
		return Dados;
	}
	/**
	 * Consulta um certificado v�lido para um usu�rio, mesmo que n�o esteja
	 * liberado.
	 * 
	 * @param usuarioDt,
	 *            identifica��o do usu�rio
	 * @author msapaula
	 */
	public CertificadoDt consultarCertificadoUsuario(String id_Usuario, FabricaConexao obFabricaConexao) throws Exception {
		CertificadoDt Dados = null;
		
		CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
		Dados = obPersistencia.consultarCertificadoUsuario(id_Usuario);
		
		return Dados;
	}

	/**
	 * M�todo que retorna uma lista de certificados confi�veis do sistema
	 * 
	 * @return uma lista de certificados confi�veis
	 * @throws Exception
	 */
	public List consultaCertificadosConfiaveis() throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		if (listCertificadosConfiaveis == null){
			////System.out.println("..ne-consultaCertificadosConfiaveis");
	
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
				listCertificadosConfiaveis = obPersistencia.consultaCertificadosConfiaveis();
			
			} finally{
				obFabricaConexao.fecharConexao();
			}
		}
		return listCertificadosConfiaveis;
	}
	
	public CertificadoDt consultaCertificadoConfiavel(String id_certificado_confiavel) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultaCertificadoConfiavel(id_certificado_confiavel);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * M�todo que retorna uma lista de certificados confi�veis do sistema
	 * 
	 * @return uma lista de certificados confi�veis
	 * @throws Exception
	 */
	public List consultaDescricaoCertificadosConfiaveis(String descricao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-consultaDescricaoCertificadosConfiaveis");
	
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoCertificadoConfiavel(descricao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return tempList;
	}
	
	/**
	 * M�todo que retorna uma lista de certificados a liberar
	 * 
	 * @return uma lista de certificados n�o liberados
	 * @throws Exception
	 */
	public List consultaCertificadosNaoLiberados(String usuario, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-consultaCertificadosNaoLiberados");

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultaCertificadosNaoLiberados(usuario, posicao);
			stUltimaConsulta = usuario;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public String consultaCertificadosNaoLiberadosJSON(String usuario, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultaCertificadosNaoLiberadosJSON(usuario, posicao);
			stUltimaConsulta = usuario;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * M�todo que retorna uma lista de certificados liberados de um ou mais
	 * usuario
	 * 
	 * @param usuario
	 *            � o usuario cuja a lista de certificados desejar buscar
	 * @return uma lista de certificados de um usuario
	 * @throws Exception
	 */
	public List consultaCertificadosNaoRevogados(String usuario, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-consultaCertificadosUsuarios");

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultaCertificadosNaoRevogados(usuario, posicao);
			stUltimaConsulta = usuario;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public String consultaCertificadosNaoRevogadosJSON(String usuario, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultaCertificadosNaoRevogadosJSON(usuario, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * M�todo que retorna uma lista de certificados de um usuario
	 * 
	 * @param idUsuario
	 *            � o idUsuario cuja a lista de certificados desejar buscar
	 * @return uma lista de certificados de um usuario
	 * @throws Exception
	 */
	public List consultaCertificadosUsuario(String idUsuario) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-consultaCertificadosUsuario");

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultaCertificadosUsuario(idUsuario);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * M�todo que retorna uma lista de certificados de revogados
	 * @return uma lista de certificados de um usuario
	 * @throws Exception
	 */
	public List consultaCertificadosRevogados() throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-consultaCertificadosRevogados");

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultaCertificadosRevogados();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	// ---------------------------------------------------------
	/**
	 * Atualiza o conte�do de um arquivo p12
	 * 
	 * @param certificadoDt,
	 *            objeto certificado
	 * @param fabConexao,
	 *            fabrica de conexao
	 * 
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public void atualizaConteudoArquivoP12(CertificadoDt certificadoDt, FabricaConexao obFabricaConexao) throws Exception{		
		
		CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
		obPersistencia.atualizaConteudoArquivoP12(certificadoDt);
		
	}
	
	/**
	 * Atualiza o conte�do de um certificado confi�vel
	 * 
	 * @param certificadoDt,
	 *            objeto certificado
	 * @param fabConexao,
	 *            fabrica de conexao
	 * 
	 * @author lsbernardes
	 */
	/*public void atualizaConteudoCertificadoConfiavel(CertificadoDt certificadoDt, FabricaConexao fabConexao) throws Exception {
		try{
			if (fabConexao == null) {
				FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabConexao;
			}
			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			obPersistencia.atualizaConteudoCertificadoConfiavel(certificadoDt);
		
		} finally{
			if (fabConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}*/

	/**
	 * M�todo respons�vel por salvar um certificado
	 * 
	 * @param CertificadoDt, certificadoDt certificado a ser baixado
	 * @param FabricaConexao, obFabricaConexao fabrica de conex�o
	 * @throws Exception
	 */
	public void salvar(CertificadoDt certificadoDt, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja est�o ou n�o salvos */

		if (certificadoDt.getId().equalsIgnoreCase("")) {
			obLogDt = new LogDt("Certificado", certificadoDt.getId(), certificadoDt.getId_UsuarioLog(), certificadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", certificadoDt.getPropriedades());
			obPersistencia.inserir(certificadoDt);
		} else {
			obLogDt = new LogDt("Certificado", certificadoDt.getId(), certificadoDt.getId_UsuarioLog(), certificadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), certificadoDt.getPropriedades());
			obPersistencia.alterar(certificadoDt);
		}
		
		obDados.copiar(certificadoDt);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 * M�todo respons�vel por salvar um certificado confiavel
	 * 
	 * @param CertificadoDt, certificadoDt certificado a ser salvo
	 * @throws Exception
	 */
	
	/**
	 * M�todo respons�vel por salvar um certificado confiavel
	 * 
	 * @param CertificadoDt, certificadoDt certificado a ser baixado
	 * @param FabricaConexao, obFabricaConexao fabrica de conex�o
	 * @throws NoSuchProviderException 
	 * @throws CertificateException 
	 * @throws Exception
	 */
	public void salvarCaConfiavel(CertificadoDt certificadoDt) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
		InputStream certStream = new ByteArrayInputStream(certificadoDt.getConteudo());
		X509Certificate certificate = (X509Certificate) cf.generateCertificate(certStream);
		certStream.close();
		
		String[] teste = certificate.getSubjectDN().getName().split("CN=");
		certificadoDt.setDescricao(teste[1]+ " - "+certificate.getSerialNumber());
		certificadoDt.setId_UsuarioCertificado(certificadoDt.getId_UsuarioLog());
		certificadoDt.setDataEmissao(FormatoData.format(certificate.getNotBefore()));
		certificadoDt.setDataExpiracao(FormatoData.format(certificate.getNotAfter()));

		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
			if (certificadoDt.getId().equalsIgnoreCase("")) {
				obLogDt = new LogDt("Certificado Confiavel",certificadoDt.getId(), certificadoDt.getId_UsuarioLog(), certificadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", certificadoDt.getPropriedades());
				obPersistencia.inserirCaConfiavel(certificadoDt);
				obDados.copiar(certificadoDt);
				obLog.salvar(obLogDt, obFabricaConexao);
			} 
		
		} finally{			
			obFabricaConexao.fecharConexao();
			listCertificadosConfiaveis = null;			
		}
	}
	
	public void salvarCaConfiavel(CertificadoDt certificadoDt, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;		
		////System.out.println("..ne salvarCaConfiavel()");
	
		CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
		if (certificadoDt.getId().equalsIgnoreCase("")) {
			obLogDt = new LogDt("Certificado Confiavel",certificadoDt.getId(), certificadoDt.getId_UsuarioLog(), certificadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", certificadoDt.getPropriedades());
			obPersistencia.inserirCaConfiavel(certificadoDt);
			obDados.copiar(certificadoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
		} 
		
		listCertificadosConfiaveis = null;	
	}

	/**
	 * M�todo que permite baixar o certificado do usu�rio
	 * 
	 * @param CertificadoDt, certificadoDt certificado a ser baixado
	 * @param OutputStream, outputStream que recebera o certificado carregado
	 * @throws Exception
	 */
	public void baixarCertificado(CertificadoDt certificadoDt, OutputStream outputStream) throws Exception {
		LogDt logDt;
		DataOutputStream dos = null;
		try{
			dos = new DataOutputStream(outputStream);
			dos.write(certificadoDt.getConteudo());
			dos.close();		
		}
		catch(Exception e) {			
		}

		// Grava o log da requisicao
		LogNe logNe = new LogNe();
		logDt = new LogDt("Certificado", certificadoDt.getId(), certificadoDt.getId_UsuarioLog(), certificadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), certificadoDt.getPropriedades());
		logNe.salvar(new LogDt("Arquivo", certificadoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", certificadoDt.getPropriedades()));
	}

	/**
	 * M�todo que libera o certificado de um usu�rio
	 * 
	 * @param UsuarioDt, usuarioDt respons�vel por liberar o certificado
	 * @param CertificadoDt, certificadoDt certificado a ser liberado
	 * @return boolean, verdadeiro se libera��o efutada ou falso se libera��o n�o for efeutada
	 * @throws Exception
	 */
	public boolean liberar(UsuarioDt usuarioDt, CertificadoDt certificadoDt) throws Exception {
		Boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());

			certificadoDt.setLiberado(Boolean.TRUE.toString());
			certificadoDt.setId_UsuarioLiberador(usuarioDt.getId());

			// Atualiza dados do certificado e gera log de liberac�o
			LogDt obLogDt = new LogDt("Certificado",certificadoDt.getId(), certificadoDt.getId_UsuarioLog(), certificadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.LiberacaoCertificado), obDados.getPropriedades(), certificadoDt.getPropriedades());
			obPersistencia.alterar(certificadoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			boRetorno = true;
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return boRetorno;
	}

	/**
	 * M�todo que revoga o certificado de um usu�rio
	 * 
	 * @param UsuarioDt, usuarioDt respons�vel por revogar o certificado
	 * @param CertificadoDt, certificadoDt certificado a ser revogado
	 * @return boolean, verdadeiro se revoga��o efutada ou falso se revoga��o n�o for efeutada
	 * @throws Exception
	 */
	public boolean revogar(UsuarioDt usuarioDt, CertificadoDt certificadoDt) throws Exception {
		Boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());

			SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			certificadoDt.setDataRevogacao(FormatoData.format(new Date()));
			certificadoDt.setMotivoRevogacao(String.valueOf(CRLReason.keyCompromise));
			certificadoDt.setId_UsuarioRevogador(usuarioDt.getId());

			// Atualiza dados do certificado e gera log da revoga��o
			LogDt obLogDt = new LogDt("Certificado", certificadoDt.getId(), certificadoDt.getId_UsuarioLog(), certificadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.RevogacaoCertificado), obDados.getPropriedades(), certificadoDt.getPropriedades());
			obPersistencia.alterar(certificadoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			boRetorno = true;
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Revoga o certificado v�lido de um usu�rio. Primeiro deve consultar se
	 * usu�rio possui um certificado v�lido e depois o revoga.
	 * 
	 * @param id_Usuario,
	 *            identifica��o do usu�rio que ter� seu certificado revogado
	 * @param id_UsuarioRevogador,
	 *            identifica��o do usu�rio que est� revogando o usu�rio
	 * 
	 * @author msapaula
	 */
	public boolean revogarCertificadoValido(String id_Usuario, String id_UsuarioRevogador, String ipComputador, FabricaConexao obFabricaConexao) throws Exception {
		Boolean boRetorno = false;
		
		CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		CertificadoDt certificadoAtual = this.consultarCertificadoUsuario(id_Usuario, obFabricaConexao);

		if (certificadoAtual != null) {
			obDados.copiar(certificadoAtual);
			certificadoAtual.setDataRevogacao(FormatoData.format(new Date()));
			certificadoAtual.setMotivoRevogacao(String.valueOf(CRLReason.keyCompromise));
			certificadoAtual.setId_UsuarioRevogador(id_UsuarioRevogador);

			// Atualiza dados do certificado e gera log da revoga��o
			LogDt obLogDt = new LogDt("Certificado", certificadoAtual.getId(), id_UsuarioRevogador, ipComputador, String.valueOf(LogTipoDt.RevogacaoCertificado), obDados.getPropriedades(), certificadoAtual.getPropriedades());
			obPersistencia.alterar(certificadoAtual);
			obLog.salvar(obLogDt, obFabricaConexao);

			boRetorno = true;
		}
				
		return boRetorno;
	}

	/**
	 * M�todo que cria o certificado raiz do sistema
	 * 
	 * @param String, dn informa��es do emissor do certificado
	 * @param String, cn dono do certificado
	 * @param long, validade do certificado raiz
	 * @param String, idUsuarioLog  id do usu�rio logado
	 * @param String, ipComputadorLog  ip do computador do usu�rio logado
	 * @return boolean, verdadeiro se opera��o efutada ou falso se opera��o n�o for efeutada
	 * @throws Exception
	 */
	public boolean criarCertificadoRaiz(String dn, String cn, long validade, String idUsuarioLog, String ipComputadorLog) throws Exception {

		Boolean boRetorno = false;
		FabricaConexao obFabricaConexao = null;
		ByteArrayOutputStream outputStreamP12 = null;
		try{
			CertificadoUtils certUtils = new CertificadoUtils();
			SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date from = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
			Date to = new Date(System.currentTimeMillis() + (validade * 365 * 24 * 60 * 60 * 1000));
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();

			CertificadoDt certificadoDt = new CertificadoDt();
			certificadoDt.setDataEmissao(FormatoData.format(from));
			certificadoDt.setDataExpiracao(FormatoData.format(to));
			certificadoDt.setRaiz(Boolean.TRUE.toString());
			certificadoDt.setEmissor(Boolean.FALSE.toString());
			certificadoDt.setLiberado(Boolean.FALSE.toString());
			certificadoDt.setMotivoRevogacao(Boolean.FALSE.toString());
			certificadoDt.setId_UsuarioLog(idUsuarioLog);
			certificadoDt.setIpComputadorLog(ipComputadorLog);

			this.salvar(certificadoDt, obFabricaConexao);
			KeyStore ks = certUtils.criarCertificadoRaiz(ProjudiPropriedades.getInstance().getCertKeySize(), new BigInteger("" + certificadoDt.getId()), dn, ProjudiPropriedades.getInstance().getPontoDeDistribuicao(), cn, from, to);
			outputStreamP12 = new ByteArrayOutputStream();
			ks.store(outputStreamP12,  ProjudiPropriedades.getInstance().getSenhaCertificadoRaiz().toCharArray());
			outputStreamP12.close();
			
			certificadoDt.setConteudo(outputStreamP12.toByteArray());
			this.atualizaConteudoArquivoP12(certificadoDt, obFabricaConexao);
			
			//Monta Ca-Confiavel e salva o mesmo *************************************************
			CertificadoDt certificadoConfiavel = new CertificadoDt();
			certificadoConfiavel.setDescricao("raiz-"+certificadoDt.getId()+".cer");
			certificadoConfiavel.setId_UsuarioCertificado(idUsuarioLog);
			certificadoConfiavel.setDataEmissao(certificadoDt.getDataEmissao());
			certificadoConfiavel.setDataExpiracao(certificadoDt.getDataExpiracao());
			certificadoConfiavel.setId_UsuarioLog(idUsuarioLog);
			certificadoConfiavel.setIpComputadorLog(ipComputadorLog);
			certificadoConfiavel.setConteudo(ks.getCertificate(cn).getEncoded());
			this.salvarCaConfiavel(certificadoConfiavel, obFabricaConexao);
			
//			certificadoConfiavel.setConteudo(ks.getCertificate(cn).getEncoded());
//			this.atualizaConteudoCertificadoConfiavel(certificadoConfiavel, obFabricaConexao);
			//*************************************************************************************
			outputStreamP12.close();
			obFabricaConexao.finalizarTransacao();
			boRetorno = true; // Tudo ok, cadastro realizado com sucesso.
		} catch(Exception ex) {
			try{if (outputStreamP12!=null) outputStreamP12.close(); } catch(Exception e ) {};
			obFabricaConexao.cancelarTransacao();

			throw ex;

		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * M�todo que cria o certificado Emissor do sistema
	 * 
	 * @param String, dn informa��es do emissor do certificado
	 * @param String, cn dono do certificado
	 * @param long, validade do certificado raiz
	 * @param String, idUsuarioLog  id do usu�rio logado
	 * @param String, ipComputadorLog  ip do computador do usu�rio logado
	 * @return boolean, verdadeiro se opera��o efutada ou falso se opera��o n�o for efeutada
	 * @throws Exception
	 */
	public boolean criarCertificadoEmissor(String dn, String cn, long validade, String idUsuarioLog, String ipComputadorLog) throws Exception {
		Boolean boRetorno = false;
		FabricaConexao obFabricaConexao = null;
		ByteArrayOutputStream outputStreamP12 = null;
		try{
			CertificadoUtils certUtils = new CertificadoUtils();
			SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date from = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
			Date to = new Date(System.currentTimeMillis() + (validade * 365 * 24 * 60 * 60 * 1000));
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();

			CertificadoDt certificadoDt = new CertificadoDt();
			certificadoDt.setDataEmissao(FormatoData.format(from));
			certificadoDt.setDataExpiracao(FormatoData.format(to));
			certificadoDt.setEmissor(Boolean.TRUE.toString());
			certificadoDt.setRaiz(Boolean.FALSE.toString());
			certificadoDt.setLiberado(Boolean.FALSE.toString());
			certificadoDt.setMotivoRevogacao(Boolean.FALSE.toString());
			certificadoDt.setId_UsuarioLog(idUsuarioLog);
			certificadoDt.setIpComputadorLog(ipComputadorLog);

			this.salvar(certificadoDt, obFabricaConexao);

			CertificadoDt certificadoRaiz = this.consultaCertificadoRaizSistema(obFabricaConexao);
			ByteArrayInputStream bisPKCS12 = new ByteArrayInputStream(certificadoRaiz.getConteudo());
			
			KeyStore ksRoot = KeyStore.getInstance("PKCS12","BC");
			ksRoot.load(bisPKCS12, ProjudiPropriedades.getInstance().getSenhaCertificadoRaiz().toCharArray());
			bisPKCS12.close();
			
			KeyStore ks = certUtils.criarCertificadoEmissor(ksRoot, ProjudiPropriedades.getInstance().getSenhaCertificadoRaiz(), ProjudiPropriedades.getInstance().getCertKeySize(), new BigInteger("" + certificadoDt.getId()), dn, ProjudiPropriedades.getInstance().getPontoDeDistribuicao(), cn, from, to);
			outputStreamP12 = new ByteArrayOutputStream();
			ks.store(outputStreamP12, ProjudiPropriedades.getInstance().getSenhaCertificadoEmissor().toCharArray());
			outputStreamP12.close();
			
			certificadoDt.setConteudo(outputStreamP12.toByteArray());
			this.atualizaConteudoArquivoP12(certificadoDt, obFabricaConexao);
			outputStreamP12.close();
			obFabricaConexao.finalizarTransacao();
			boRetorno = true; // Tudo ok, cadastro realizado com sucesso.
		
		} catch(Exception ex) {
			try{if (outputStreamP12!=null) outputStreamP12.close(); } catch(Exception e ) {};
			obFabricaConexao.cancelarTransacao();
			////System.out.println("..ne-criarCertificadoEmissor" + ex.getMessage());
			throw ex;
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	public boolean criarIdentidadeDigital(UsuarioDt usuario, Identidade id, String idUsuarioLog, String ipComputadorLog) throws Exception {
		long v = ProjudiPropriedades.getInstance().getValidadeID();
		return criarIdentidadeDigital( usuario,  id,  idUsuarioLog,  ipComputadorLog, v);
	}
	/**
	 * M�todo que cria a identidade digital de um usu�rio
	 * 
	 * @param UsuarioDt, usuario para qual o certificado ser� criado
	 * @param Identidade, id informa��es para formar o nome alternativo do certicado
	 * @param String, idUsuarioLog  id do usu�rio logado
	 * @param String, ipComputadorLog  ip do computador do usu�rio logado
	 * @return boolean, verdadeiro se opera��o efutada ou falso se opera��o n�o for efeutada
	 * @throws Exception
	 */
	public boolean criarIdentidadeDigital(UsuarioDt usuario, Identidade id, String idUsuarioLog, String ipComputadorLog, long validade) throws Exception {
		Boolean boRetorno = false;
		FabricaConexao obFabricaConexao = null;

		try{

			CertificadoUtils certUtils = new CertificadoUtils();
			SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			Date from = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
			Date to = new Date(System.currentTimeMillis() + (validade * 365 * 24 * 60 * 60 * 1000));
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();

			CertificadoDt certificadoDt = new CertificadoDt();
			certificadoDt.setUsuarioCertificado(usuario.getUsuario());
			certificadoDt.setId_UsuarioCertificado(usuario.getId());
			certificadoDt.setDataEmissao(FormatoData.format(from));
			certificadoDt.setDataExpiracao(FormatoData.format(to));
			certificadoDt.setLiberado(Boolean.FALSE.toString());
			certificadoDt.setEmissor(Boolean.FALSE.toString());
			certificadoDt.setRaiz(Boolean.FALSE.toString());
			certificadoDt.setMotivoRevogacao(Boolean.FALSE.toString());
			certificadoDt.setId_UsuarioLog(idUsuarioLog);
			certificadoDt.setIpComputadorLog(ipComputadorLog);

			this.salvar(certificadoDt, obFabricaConexao);
			
			CertificadoDt certificadoEmissor = this.consultaCertificadoEmissorSistema(obFabricaConexao);
			ByteArrayInputStream bisPKCS12 = new ByteArrayInputStream(certificadoEmissor.getConteudo());
			
			KeyStore ksRoot = KeyStore.getInstance("PKCS12","BC");
			ksRoot.load(bisPKCS12, ProjudiPropriedades.getInstance().getSenhaCertificadoEmissor().toCharArray());
			bisPKCS12.close();
			
			KeyStore ks = certUtils.criarIdentidadeDigital(ksRoot, ProjudiPropriedades.getInstance().getSenhaCertificadoEmissor(), ProjudiPropriedades.getInstance().getCertKeySize(), new BigInteger("" + certificadoDt.getId()), id, ProjudiPropriedades.getInstance().getPontoDeDistribuicao(), from, to);
			ByteArrayOutputStream outputStreamP12 = new ByteArrayOutputStream();
			ks.store(outputStreamP12, id.getSenha().toCharArray());
			
			certificadoDt.setConteudo(outputStreamP12.toByteArray());
			this.atualizaConteudoArquivoP12(certificadoDt, obFabricaConexao);
			outputStreamP12.close();
			obFabricaConexao.finalizarTransacao();
			boRetorno = true;
		} catch(Exception ex) {
			obFabricaConexao.cancelarTransacao();
			////System.out.println("..ne-criarIdentidadeDigigal" + ex.getMessage());
			throw ex;
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	 /**
		 * M�todo que retorna uma lista de certificados de um usuario
		 * 
		 * @param idUsuario
		 *            � o idUsuario cuja a lista de certificados desejar buscar
		 * @return uma lista de certificados de um usuario
		 * @throws Exception
		 */
		public String consultaCertificadosUsuarioJSON(String idUsuario, String posicao) throws Exception {
			String stTemp = null;
			FabricaConexao obFabricaConexao = null;
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				CertificadoPs obPersistencia = new  CertificadoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultaCertificadosUsuarioJSON(idUsuario, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
			return stTemp;
		}
	
}
