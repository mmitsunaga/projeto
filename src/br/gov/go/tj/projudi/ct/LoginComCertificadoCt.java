package br.gov.go.tj.projudi.ct;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateRevokedException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import br.gov.go.tj.pe.oab.AdvogadoData;
import br.gov.go.tj.pe.oab.CadastroOABProxy;
import br.gov.go.tj.projudi.dt.AdvogadoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaOabNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.CpfHelper;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.ValidaCertificadoICPBR;

public class LoginComCertificadoCt extends Controle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8146132571148711385L;
	
	Logger logger = Logger.getLogger(LoginComCertificadoCt.class);
		
	public int Permissao(){
		return UsuarioDt.CodigoPermissao;
	}
	
	@Override
	protected String getId_GrupoPublico() {	
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
		
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca,	String posicaopaginaatual) throws Exception, ServletException, IOException {
				
		String cpf = null, dataNascimento="";		
		
		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");
		boolean isExecpenweb = request.getSession().getAttribute("Acesso") != null && request.getSession().getAttribute("Acesso").equals("Execpenweb");	
		boolean isCadastro = request.getSession().getAttribute("CadastroComCertificado") != null && request.getSession().getAttribute("CadastroComCertificado").equals("true");						
			
		if (!"SUCCESS".equals(request.getHeader("X-Client-Verify"))) {
			request.setAttribute("MensagemErro", "Nenhum certificado localizado.");
		}else {
			try {
				List<String> certificadoComCadeia = new ArrayList<String>();
				certificadoComCadeia.add(request.getHeader("X-SSL-Cert"));
				certificadoComCadeia.add(request.getHeader("X-SSL-Cert-Chain-0"));
				certificadoComCadeia.add(request.getHeader("X-SSL-Cert-Chain-1"));
				certificadoComCadeia.add(request.getHeader("X-SSL-Cert-Chain-2"));
				List<X509Certificate> cadeia = montaCadeia(certificadoComCadeia);
				new ValidaCertificadoICPBR().validaRevogacao(cadeia.get(0));
				Collection<List<?>> alternativeNames = cadeia.get(0).getSubjectAlternativeNames();					
				if(!alternativeNames.isEmpty()){
					for(List<?> alternativeName:alternativeNames){
						try{
							if(alternativeName.get(0) != null && alternativeName.get(0).equals(0)){
								byte[] valor = (byte[]) alternativeName.get(1);
								ASN1InputStream stream = new ASN1InputStream(new ByteArrayInputStream(valor));
								DERObject oct = stream.readObject();
								stream.close();
						        ASN1Sequence seq = ASN1Sequence.getInstance(oct);
			                    DERObjectIdentifier id = DERObjectIdentifier.getInstance(seq.getObjectAt(0));
			                    if (id.getId().equals("2.16.76.1.3.1")) {
			                        ASN1TaggedObject obj = (ASN1TaggedObject) seq.getObjectAt(1);
			                        byte[] octetos;
			                        try{
				                        octetos = ASN1OctetString.getInstance(obj.getObject()).getOctets();
			                        } catch(IllegalArgumentException e){
			                        	DERTaggedObject str = (DERTaggedObject) obj.getObject();
			                        	DERPrintableString str2 = (DERPrintableString) str.getObject();
				                        octetos = str2.getOctets();
			                        }
			                        String nome = new String(Hex.encode(octetos));
			                        StringBuilder sb = new StringBuilder();
			                        sb.append(nome.charAt(1)).append(nome.charAt(3)).append("/");
			                        sb.append(nome.charAt(5)).append(nome.charAt(7)).append("/");
			                        sb.append(nome.charAt(9)).append(nome.charAt(11)).append(nome.charAt(13)).append(nome.charAt(15));
			                        dataNascimento = sb.toString();
			                        sb = new StringBuilder();
			                        sb.append(nome.charAt(17)).append(nome.charAt(19)).append(nome.charAt(21));
			                        sb.append(nome.charAt(23)).append(nome.charAt(25)).append(nome.charAt(27));
			                        sb.append(nome.charAt(29)).append(nome.charAt(31)).append(nome.charAt(33));
			                        sb.append(nome.charAt(35)).append(nome.charAt(37));
									cpf = sb.toString();
									if (!CpfHelper.isCpfValido(cpf)) {
										cpf = null;
									}
			                        break;
			                    }
							}
						} catch (Exception e){
							logger.error("Nome alternativo incorreto.", e);
							for (Object o:alternativeName){
								logger.debug(o.getClass().getName()+" : "+o.toString());
							}
						}
					}
				} 
				if(cpf == null){
					throw new MensagemException( "Nenhum certificado localizado ou problema na obtenção do CPF.");						
				}
			} catch (CertificateRevokedException e) {
				throw new MensagemException( "Certificado Revogado.");
			}
		}
		
		if (cpf == null) {
			RequestDispatcher dis = null;
			if (isExecpenweb) {
				dis = request.getRequestDispatcher("/index2.jsp");
			}
			else {
				dis = request.getRequestDispatcher("/index.jsp");
			}
			dis.forward(request, response);
			return;
		}
		
		if(isCadastro){
			UsuarioNe ne = new UsuarioNe();
			if (ne.consultaUsuarioCertificado(cpf)) {								
				request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());
				UsuarioSessao.setPermissao(UsuarioDt.CodigoPermissao);					
				UsuarioSessao.setPermissao(2166);
				UsuarioSessao.setPermissao(2167);
				UsuarioSessao.setPermissao(2169);
				UsuarioSessao.setPermissao(2160);

				UsuarioDt advogado = new UsuarioNe().consultarUsuarioCompleto(ne.getId_Usuario());
				advogado.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
				advogado.getEnderecoUsuario().setId_UsuarioLog(advogado.getId_UsuarioLog());
				advogado.setGrupoCodigo(String.valueOf(GrupoDt.ADVOGADO_PARTICULAR));
				List<UsuarioServentiaOabDt> listaUsuarioServentias = new ArrayList<UsuarioServentiaOabDt>();
				
				for(UsuarioServentiaOabDt oab :new UsuarioServentiaOabNe().consultarUsuarioServentiaOab(advogado.getId())){
					listaUsuarioServentias.add(oab);
				}
				List<AdvogadoData> oabs = CadastroOABProxy.get().consultaAdvogado(cpf);
				if (oabs.isEmpty()) {
					request.setAttribute("MensagemErro", "Usuário não localizado no serviço da OAB.");
					RequestDispatcher dis = null;
					if (isExecpenweb) {
						dis = request.getRequestDispatcher("/index2.jsp");
					}
					else {
						dis = request.getRequestDispatcher("/index.jsp");
					}
					dis.forward(request, response);
					return;
				}
				boolean temCadastroPrincipal = false;
				for (AdvogadoData adv : oabs) {
					if ("ADVOGADO".equals(adv.getTipoInscricao())) {
						temCadastroPrincipal = true;
						advogado.setNome(adv.getNome());
						break;
					}
				}
				if (!temCadastroPrincipal) {
					request.setAttribute("MensagemErro", "Usuário com cadastro inconsistente no serviço da OAB. Possui apenas OABs suplementares.");
					RequestDispatcher dis = null;
					if (isExecpenweb) {
						dis = request.getRequestDispatcher("/index2.jsp");
					}
					else {
						dis = request.getRequestDispatcher("/index.jsp");
					}
					dis.forward(request, response);
					return;
				}
				for (AdvogadoData adv : oabs) {
					ServentiaNe serventiaNe = new ServentiaNe();
					List seccionais = serventiaNe.consultarServentiasHabilitacaoAdvogado("Ordem dos Advogados do Brasil " + adv.getUf(), "0");
					ServentiaDt serventia = null;
					if (!seccionais.isEmpty())
						serventia = (ServentiaDt) seccionais.get(0);
					boolean oabJaCadastrada = false;
					for(UsuarioServentiaOabDt oab:listaUsuarioServentias){
						if(oab.getOabNumero().trim().equals(Funcoes.removeZerosEsquerda(Funcoes.obtenhaSomenteNumeros(adv.getInscricao().trim())))){
							oabJaCadastrada = true;
							oab.setId_UsuarioLog(advogado.getId_UsuarioLog());
							if (oab.getGrupoCodigo().equals(advogado.getGrupoCodigo())){ //ADVOGADO_PARTICULAR
						        oab.setInativo(!"REGULAR".equals(adv.getSituacao()));
							}
							break;
						}
					}
					if(!oabJaCadastrada){
						UsuarioServentiaOabDt usuarioServentia = new UsuarioServentiaOabDt();
						usuarioServentia.setId_UsuarioLog(advogado.getId_UsuarioLog());
						usuarioServentia.setOabNumero(Funcoes.obtenhaSomenteNumeros(adv.getInscricao()));
						usuarioServentia.setServentia(serventia.getServentia());
						usuarioServentia.setInativo(!"REGULAR".equals(adv.getSituacao()));
						usuarioServentia.setIdServentia(serventia.getId());
						if ("ADVOGADO".equals(adv.getTipoInscricao())) {
							usuarioServentia.setOabComplemento("A");
						}
						else {
							usuarioServentia.setOabComplemento("S");
						}
						listaUsuarioServentias.add(usuarioServentia);
					}
				}
				advogado.setListaUsuarioServentias(listaUsuarioServentias);	
				request.getSession().setAttribute("NovoAdvogado", advogado);
				request.getSession().setAttribute("OabsSuplementaresNovoAdvogado", listaUsuarioServentias);
				redireciona(response, "AdvogadoCertificado?PaginaAtual=" + Configuracao.Curinga6);
				return;
			} else {
				List<AdvogadoData> oabs = CadastroOABProxy.get().consultaAdvogado(cpf);
				if (oabs.isEmpty()) {
					request.setAttribute("MensagemErro", "Usuário não localizado no serviço da OAB.");
					RequestDispatcher dis = null;
					if (isExecpenweb) {
						dis = request.getRequestDispatcher("/index2.jsp");
					}
					else {
						dis = request.getRequestDispatcher("/index.jsp");
					}
					dis.forward(request, response);
					return;
				}else {
					boolean temCadastroPrincipal = false;
					for (AdvogadoData adv : oabs) {
						if ("ADVOGADO".equals(adv.getTipoInscricao())) {
							temCadastroPrincipal = true;
							break;
						}
					}
					if (!temCadastroPrincipal) {
						request.setAttribute("MensagemErro", "Usuário com cadastro inconsistente no serviço da OAB. Possui apenas OABs suplementares.");
						RequestDispatcher dis = null;
						if (isExecpenweb) {
							dis = request.getRequestDispatcher("/index2.jsp");
						}
						else {
							dis = request.getRequestDispatcher("/index.jsp");
						}
						dis.forward(request, response);
						return;
					}
					UsuarioDt advogado = new AdvogadoDt();
					advogado.setSenha(UsuarioNe.SENHA_INVALIDA);
					advogado.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
					advogado.getEnderecoUsuario().setId_UsuarioLog(advogado.getId_UsuarioLog());
					advogado.setGrupoCodigo(String.valueOf(GrupoDt.ADVOGADO_PARTICULAR));
					advogado.setCpf(cpf);
					advogado.setDataNascimento(dataNascimento);
					List<UsuarioServentiaOabDt> listaUsuarioServentias = new ArrayList<UsuarioServentiaOabDt>();
					for (AdvogadoData adv : oabs) {
						UsuarioServentiaOabDt usuarioServentia = new UsuarioServentiaOabDt();
						usuarioServentia.setId_UsuarioLog(advogado.getId_UsuarioLog());
						usuarioServentia.setOabNumero(Funcoes.obtenhaSomenteNumeros(adv.getInscricao()));
						List seccionais = new ServentiaNe().consultarServentiasHabilitacaoAdvogado("Ordem dos Advogados do Brasil " + adv.getUf(), "0");
						ServentiaDt serventia = null;
						if (!seccionais.isEmpty())
							serventia = (ServentiaDt) seccionais.get(0);
						usuarioServentia.setServentia(serventia.getServentia());
						usuarioServentia.setInativo(!"REGULAR".equals(adv.getSituacao()));
						if ("ADVOGADO".equals(adv.getTipoInscricao())) {
							usuarioServentia.setOabComplemento("A");
							advogado.setUsuarioServentiaOab(usuarioServentia);
							advogado.setId_Serventia(serventia.getId());
							advogado.setNome((adv.getNome()));
							if (adv.getEmail() != null)
								advogado.setEMail(adv.getEmail());
							if (adv.getCodigoDeArea() != null && adv.getTelefone() != null)
								advogado.setTelefone(adv.getCodigoDeArea() + adv.getTelefone());
							listaUsuarioServentias.add(0, usuarioServentia);
						}
						else {
							usuarioServentia.setOabComplemento("S");
							usuarioServentia.setIdServentia(serventia.getId());
							listaUsuarioServentias.add(usuarioServentia);
						}
					}
					advogado.setListaUsuarioServentias(listaUsuarioServentias);
					
					request.getSession().setAttribute("NovoAdvogado", advogado);
					request.getSession().setAttribute("OabsSuplementaresNovoAdvogado", listaUsuarioServentias);
					redireciona(response, "AdvogadoCertificado?PaginaAtual=" + Configuracao.Curinga6);
					return;
				}
			}
		} else {
			if (UsuarioSessao.consultaUsuarioCertificado(cpf)) {
				if (UsuarioSessao.isAtivo()) {							
					request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());
					UsuarioSessao.setPermissao(UsuarioDt.CodigoPermissao);					
					UsuarioSessao.setPermissao(2166);
					UsuarioSessao.setPermissao(2167);
					UsuarioSessao.setPermissao(2169);
					UsuarioSessao.setPermissao(2160);
					UsuarioSessao.setLoginToken();
					//redireciona para obeter a serventia grupo
					redireciona(response, "Usuario?PaginaAtual=9");
					return;
				}	
				request.setAttribute("MensagemErro", "Usuário inativo.");
			}else {
				request.setAttribute("MensagemErro", "Usuário não cadastrado.");
			}
			LimparSessao(request.getSession());
			request.getSession().removeAttribute("UsuarioSessao");
			RequestDispatcher dis = null;
			if (isExecpenweb) {
				dis = request.getRequestDispatcher("/index2.jsp");
			}
			else {
				dis = request.getRequestDispatcher("/index.jsp");
			}
			dis.forward(request, response);
			return;
		}
	}

	
	public List<X509Certificate> montaCadeia(List<String> certificadoComCadeia) throws Exception {
		CertificateFactory factory = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());
		List<X509Certificate> cadeia = new ArrayList<X509Certificate>();
		for (String cert : certificadoComCadeia) {
			if (cert != null && !cert.isEmpty()) {
				if (cert.startsWith("-----BEGIN CERTIFICATE-----")){
					cert = cert.substring("-----BEGIN CERTIFICATE----- ".length());
					cert = cert.substring(0, cert.length() - " -----END CERTIFICATE----- ".length());
				}
				InputStream is = new ByteArrayInputStream(Base64.decode(cert));
				X509Certificate certificado = (X509Certificate) factory.generateCertificate(is);
				if (certificado != null) {
					//logger.debug(certificado.getSubjectDN().getName());
					cadeia.add(certificado);
				}
//				else {
					//logger.debug("##### CERTIFICADO NÃO RECONHECIDO ##### \n" + cert);
//				}
				is.close();
			}
//			else {
				//logger.debug("##### Certificado Inválido ##### \n" + cert);
//			}
		}
//		if (cadeia.size() != 4)
//			logger.warn("##### Não foi possível verificar a CRL de toda a cadeia. ##### \n");
		return cadeia;
	}
	
}
