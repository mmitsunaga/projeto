package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.CertificadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.Identidade;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class CertificadoCt extends CertificadoCtGen{

/**
     * 
     */
    private static final long serialVersionUID = -4816185627992562826L;

    //
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertificadoDt Certificadodt;
		CertificadoNe Certificadone;

		List tempList=null; 
		String Mensagem="";
		String cadastrador = "";
		
		
		String stAcao="";
		
		if ((request.getParameter("Cadastrador") != null && request.getParameter("Cadastrador").toString().equals("1"))
				|| (request.getSession().getAttribute("Cadastrador") != null && request.getSession().getAttribute("Cadastrador").toString().equals("1"))){
			stAcao = "/WEB-INF/jsptjgo/CertificadoLiberarCadastrador.jsp";
			cadastrador = "1";
		}else 
			stAcao = "/WEB-INF/jsptjgo/CertificadoLiberar.jsp";
		
		request.setAttribute("tempPrograma","Certificado");
		request.setAttribute("tempBuscaId_Certificado","Id_Certificado");
		request.setAttribute("tempBuscaUsuarioCertificado","UsuarioCertificado");
		request.setAttribute("Curinga","vazio");
		request.setAttribute("tempRetorno","Certificado");

		Certificadone =(CertificadoNe)request.getSession().getAttribute("Certificadone");
		if (Certificadone == null )  Certificadone = new CertificadoNe();  

		Certificadodt =(CertificadoDt)request.getSession().getAttribute("Certificadodt");
		if (Certificadodt == null )  Certificadodt = new CertificadoDt();  

		//PARAMETROS PARA CRIAR CERTIFICADO RAIZ DO PROJUDI*****
		String c = request.getParameter("c");
		String st = request.getParameter("st");
		String l = request.getParameter("l");
		String o = request.getParameter("o");
		String ou = request.getParameter("ou");
		String cn = request.getParameter("cn");
		String e = request.getParameter("e");
		String validade = request.getParameter("validade");
		//********************************************************
		//PARAMETROS PARA CRIAR CERTIFICADO EMISSOR DO PROJUDI*****
		String cE = request.getParameter("cE");
		String oE = request.getParameter("oE");
		String cnE = request.getParameter("cnE");
		String validadeE = request.getParameter("validadeE");
		//**********************************************************
		
		Certificadodt.setCodigoTemp( request.getParameter("CodigoTemp")); 
		Certificadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Certificadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Excluir:
				//CONFIRMAR REVOCAO DE CERTIFICADO
				request.setAttribute("certificadoDt", Certificadone.consultarId(request.getParameter("Id_Certificado")));
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stAcao="/WEB-INF/jsptjgo/CertificadoMensagemConfirmacao.jsp";
				break;
			case Configuracao.ExcluirResultado: 
				//REVOGAR CERTIFICADO DO USUARIO LOGADO
				Certificadodt = Certificadone.consultarId(request.getParameter("Id_Certificado"));
				Certificadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				Certificadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				//NÃO É PERMITIDO REVOGAR UM CERTIFICADO QUE NÃO FOI LIBERADO.
				if(Certificadodt.getLiberado().equalsIgnoreCase("FALSE")) {
					 Mensagem = "Não é permitido revogar um certificado que não foi liberado. ";
				}
				
				if (Mensagem.length()==0){
					if (Certificadone.revogar(UsuarioSessao.getUsuarioDt(), Certificadodt)) 
						request.setAttribute("MensagemOk", "Certificado Revogado com Sucesso");
					else 
						request.setAttribute("MensagemErro", "Erro na tentativa de Revogação do Certificado");
				} else {
					request.setAttribute("MensagemErro", Mensagem );
				}
				//LOCALIZAR CERTICADOS PARA REVOGAR
				tempList =Certificadone.consultaCertificadosNaoRevogados(tempNomeBusca, PosicaoPaginaAtual);
				stAcao="/WEB-INF/jsptjgo/CertificadoRevogar.jsp";
				if (tempList.size()>0){
					request.setAttribute("ListaCertificado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("Curinga","A");
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
					Certificadodt.limpar();
				}
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
			case Configuracao.Imprimir: 
				break;
			case Configuracao.LiberarCertificado:
				//CONFIRMAR LIBERACAO DE CERTIFICADO
				if (!cadastrador.equals("") && cadastrador.equals("1"))
					stAcao="/WEB-INF/jsptjgo/CertificadoCadastradorMensagemConfirmacao.jsp";
				else
					stAcao="/WEB-INF/jsptjgo/CertificadoMensagemConfirmacao.jsp";
				request.setAttribute("certificadoDt", Certificadone.consultarId(request.getParameter("Id_Certificado")));
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
			case Configuracao.Curinga6:
				//LIBERAR CERTIFICADOS
				Certificadodt = Certificadone.consultarId(request.getParameter("Id_Certificado"));
				Certificadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				Certificadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				if (Certificadone.liberar(UsuarioSessao.getUsuarioDt(), Certificadodt)) 
					request.setAttribute("MensagemOk", "Certificado Liberado com Sucesso");
				else 
					request.setAttribute("MensagemErro", "Erro na tentativa de Liberação do Certificado");
				
				request.setAttribute("idCertificado", request.getParameter("Id_Certificado"));
				request.setAttribute("usuarioCertificado", request.getParameter("UsuarioCertificado"));
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				//request.setAttribute("Curinga","C");
				//LOCALIZAR CERTICADOS NÃO LIBERADOS
				tempList =Certificadone.consultaCertificadosNaoLiberados(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("liberar",new Boolean(true));
					request.setAttribute("ListaCertificado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("Curinga","B");
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
					Certificadodt.limpar();
				}
				break;
			case Configuracao.Localizar:
				if (request.getParameter("Curinga").equalsIgnoreCase("A")){
					//LOCALIZAR CERTICADOS PARA REVOGAR
					tempList =Certificadone.consultaCertificadosNaoRevogados(tempNomeBusca, PosicaoPaginaAtual);
					 stAcao="/WEB-INF/jsptjgo/CertificadoRevogar.jsp";
					if (tempList.size()>0){
						request.setAttribute("ListaCertificado", tempList); 
						request.setAttribute("PaginaAtual", Configuracao.Localizar);
						request.setAttribute("Curinga","A");
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
						Certificadodt.limpar();
					}else{ 
						request.setAttribute("MensagemErro", "Certificados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						request.setAttribute("Curinga","C");
					}
				} else if (request.getParameter("Curinga").equalsIgnoreCase("B")){
					//LOCALIZAR CERTICADOS NÃO LIBERADOS
					tempList =Certificadone.consultaCertificadosNaoLiberados(tempNomeBusca, PosicaoPaginaAtual);
					 stAcao="/WEB-INF/jsptjgo/CertificadoLiberar.jsp";
					if (tempList.size()>0){
						request.setAttribute("liberar",new Boolean(true));
						request.setAttribute("ListaCertificado", tempList); 
						request.setAttribute("PaginaAtual", Configuracao.Localizar);
						request.setAttribute("Curinga","B");
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
						Certificadodt.limpar();
					}else{ 
						request.setAttribute("MensagemErro", "Certificados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						request.setAttribute("Curinga","C");
					}
				}
				break;
			case Configuracao.LocalizarDWR: 
	            
            	 int inFluxo = Funcoes.StringToInt( request.getParameter("fluxo").toString());
                 switch (inFluxo) {
                    case 1:
                    	getCertificadosNaoLiberados(request, response, tempNomeBusca, PosicaoPaginaAtual);
                        break;
                    case 2:
                    	getCertificadosNaoRevogados(request, response, tempNomeBusca, PosicaoPaginaAtual);
                        break;                  
                 }		                	               	            
				return;
			case Configuracao.Novo: 
				Certificadodt.limpar();
				stAcao = "/WEB-INF/jsptjgo/Certificado.jsp";
				newCertificadoRaiz(request);
				newCertificadoEmissor(request);
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				request.setAttribute("Curinga","F");				
				break;
			case Configuracao.Salvar:
				stAcao = "/WEB-INF/jsptjgo/Certificado.jsp";
				if (request.getParameter("Curinga").equalsIgnoreCase("D")){
					//CONFIRMAR CRIACAO DE CERTIFICADO RAIZ
					setAtributosCertificadoRaiz(request,c,st,l,o,ou,cn,e,validade);
					setAtributosCertificadoEmissor(request,cE,oE,cnE,validadeE);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					request.setAttribute("Curinga","D");
				} else if (request.getParameter("Curinga").equalsIgnoreCase("E")){
					//CONFIRMAR CRIACAO DE CERTIFICADO EMISSOR
					setAtributosCertificadoRaiz(request,c,st,l,o,ou,cn,e,validade);
					setAtributosCertificadoEmissor(request,cE,oE,cnE,validadeE);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					request.setAttribute("Curinga","E");
				} else if (request.getParameter("Curinga").equalsIgnoreCase("G")){
					//CONFIRMAR CRIACAO DE CERTIFICADO EMISSOR
					setAtributosCertificadoRaiz(request,c,st,l,o,ou,cn,e,validade);
					setAtributosCertificadoEmissor(request,cE,oE,cnE,validadeE);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					request.setAttribute("Curinga","G");
				}
				break;
			case Configuracao.SalvarResultado:
				stAcao = "/WEB-INF/jsptjgo/Certificado.jsp";
				if (request.getParameter("Curinga").equalsIgnoreCase("D")){
					//CRIAR CERTIFICADO RAIZ ****************************************************
					if(c == null || c.equals("")) Mensagem += "\"C\" é campo requerido. ";
					if(st == null || st.equals(""))Mensagem += "\"ST\" é campo requerido. ";
					if(l == null || l.equals("")) Mensagem += "\"L\" é campo requerido. ";
					if(o == null || o.equals("")) Mensagem += "\"O\" é campo requerido. ";
					if(ou == null || ou.equals("")) Mensagem += "\"OU\" é campo requerido. ";
					if(cn == null || cn.equals("")) Mensagem += "\"CN\" é campo requerido. ";
					if(e == null || e.equals("")) Mensagem += "\"E\" é campo requerido. ";
					if(validade == null || validade.equals("")) Mensagem += "\"Validade\" é campo requerido.";
					long v = 0;
					if(validade != null && !validade.equals("")){						
						v = Funcoes.StringToInt(validade,-1);
						if (v==-1){
							Mensagem += "\"Validade\" é campo numérico.";
						}
					}
					//String path = this.getServletContext().getRealPath("/root.cer");

					if (Mensagem.length()==0){
						String dn = "C="+c+", ST="+st+", L="+l+", O="+o+", OU="+ou+", CN="+cn+", E="+e;
						if(Certificadone.criarCertificadoRaiz(dn, cn, v,  Certificadodt.getId_UsuarioLog(), Certificadodt.getIpComputadorLog()))
							request.setAttribute("MensagemOk", "Certificado Raiz Criaddo com sucesso"); 
						else
							request.setAttribute("MensagemErro", "Erro ao Criar Certificado Raiz");
					}else
						request.setAttribute("MensagemErro", Mensagem );
				
					request.setAttribute("Curinga","D");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					setAtributosCertificadoRaiz(request,c,st,l,o,ou,cn,e,validade);
					newCertificadoEmissor(request);
				} else if (request.getParameter("Curinga").equalsIgnoreCase("E")){
					//CRIAR CERTIFICADO EMISSOR ****************************************************
					if(cE == null || cE.equals("")) Mensagem += "\"C\" é campo requerido. ";
					if(oE == null || oE.equals("")) Mensagem += "\"O\" é campo requerido. ";
					if(cnE == null || cnE.equals("")) Mensagem += "\"CN\" é campo requerido. ";
					if(validadeE == null || validadeE.equals("")) Mensagem += "\"Validade\" é campo requerido.";
					long vE = 0;
					if(validadeE != null && !validadeE.equals("")){					
						vE = Funcoes.StringToInt(validadeE,-1);
						if (vE==-1){
							Mensagem += "\"Validade\" é campo numérico.";
						}
						
					}
					
					if (Mensagem.length()==0){
						String dnE = "C="+cE+", O="+oE+", CN="+cnE;
						if(Certificadone.criarCertificadoEmissor(dnE, cnE, vE,Certificadodt.getId_UsuarioLog(), Certificadodt.getIpComputadorLog()))
							request.setAttribute("MensagemOk", "Certificado Emissor Criado com sucesso"); 
						else
							request.setAttribute("MensagemErro", "Erro ao Criar CertificadoEmissor");
					}else 
						request.setAttribute("MensagemErro", Mensagem );
					
					request.setAttribute("Curinga","E");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					setAtributosCertificadoEmissor(request,cE,oE,cnE,validadeE);
				} else if (request.getParameter("Curinga").equalsIgnoreCase("G")){
					
					UsuarioDt usuSys = new UsuarioNe().consultarUsuarioCompleto(UsuarioDt.SistemaProjudi);
					
					Identidade id = new Identidade();
					
					id.setCidade(usuSys.getCidade());
					id.setEstado(usuSys.getEstado());
					id.setEmail(usuSys.getEMail());
					id.setNome(usuSys.getNome());
					id.setCPF(usuSys.getCpf());
					id.setRG(usuSys.getRg());
					id.setRGExpedidor(usuSys.getRgOrgaoExpedidor());				
					id.setRGExpedidorUF(usuSys.getRgOrgaoExpedidorUf());
					id.setDataNascimento(usuSys.getDataNascimento());		
					id.setPais("BR");
					id.setNIS(null);		
			 		id.setINSS(null);	
			 		id.setEhPessoaFisica();
			 		ProjudiPropriedades prop =  ProjudiPropriedades.getInstance();
			 		id.setSenha(prop.getSenhaIdentidadeDigitalSistema());
					id.setCsenha(prop.getSenhaIdentidadeDigitalSistema());
					
					Mensagem = id.validaCampos();
					
					if (Certificadone.consultaNumeroCertificadosValido(usuSys.getId()) > 0) {
						Mensagem = "Não é permitido ao usuário ter mais de uma identidade digital ativa. Se você deseja criar uma nova por motivo de segurança, deve primeiro revogar a atual.";
					}

					if (Mensagem.length()==0){
						if(Certificadone.criarIdentidadeDigital(usuSys,id,Certificadodt.getId_UsuarioLog(), Certificadodt.getIpComputadorLog(),Funcoes.StringToLong(validade,3))){
							RequestDispatcher dis =	request.getRequestDispatcher("/WEB-INF/jsptjgo/Identidade.htm");
							dis.include(request, response);
							return;
						} else
							request.setAttribute("MensagemErro", "Erro ao Criar Identidade Digital");
					}else {
						request.setAttribute("MensagemErro", Mensagem );						
					}
					
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					stAcao = "/WEB-INF/jsptjgo/Certificado.jsp";
					break;
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				//LOCALIZAR CERTICADOS NÃO LIBERADOS
				tempList =Certificadone.consultaCertificadosNaoLiberados(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("liberar",new Boolean(true));
					request.setAttribute("ListaCertificado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("Curinga","B");
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
					Certificadodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Certificados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					request.setAttribute("Curinga","C");
				}
			}

		request.getSession().setAttribute("Cadastrador", cadastrador);
		request.getSession().setAttribute("Certificadodt",Certificadodt );
		request.getSession().setAttribute("Certificadone",Certificadone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	public void setAtributosCertificadoRaiz(HttpServletRequest request,String c,String st,String l,String o,String ou,String cn,String e, String validade) {
		if (c != null) request.setAttribute("c", c);
		 	else request.setAttribute("c", "BR");
		if (st != null) request.setAttribute("st", st);
			else request.setAttribute("st", "Goias");
		if (l != null) request.setAttribute("l", l);
			else request.setAttribute("l", "Goiania");
		if (o != null) request.setAttribute("o", o);
			else request.setAttribute("o", "Tribunal de Justica de Goias");
		if (ou != null) request.setAttribute("ou", ou);
			else request.setAttribute("ou", "Divisao de Sistemas de Informacao");
		if (cn != null) request.setAttribute("cn", cn);
			else request.setAttribute("cn", "Projudi - Certificado Raiz");
		if (e != null) request.setAttribute("e", e);
			else request.setAttribute("e", "webmaster@tj.go.gov.br");
		if (validade != null) request.setAttribute("validade", validade);
			else request.setAttribute("validade", "15");
	}
	
	public void setAtributosCertificadoEmissor(HttpServletRequest request,String c,String o,String cn, String validade) {
		if (c != null) request.setAttribute("cE", c);
		 	else request.setAttribute("cE", "BR");
		if (o != null) request.setAttribute("oE", o);
			else request.setAttribute("oE", "Divisao de Sistemas de Informacao");
		if (cn != null) request.setAttribute("cnE", cn);
			else request.setAttribute("cnE", "Projudi - Autoridade Registradora");
		if (validade != null) request.setAttribute("validadeE", validade);
			else request.setAttribute("validadeE", "15");
	}
	
	public void newCertificadoRaiz(HttpServletRequest request) {
		request.setAttribute("c", "BR");
		request.setAttribute("st", "Goias");
		request.setAttribute("l", "Goiania");
		request.setAttribute("o", "Tribunal de Justica de Goias");
		request.setAttribute("ou", "Divisao de Sistemas de Informacao");
		request.setAttribute("cn", "Projudi - Certificado Raiz");
		request.setAttribute("e", "webmaster@tj.go.gov.br");
		request.setAttribute("validade", "15");
	}
	
	public void newCertificadoEmissor(HttpServletRequest request) {
		request.setAttribute("cE", "BR");
		request.setAttribute("oE", "Divisao de Sistemas de Informacao");
		request.setAttribute("cnE", "Projudi - Autoridade Registradora");
		request.setAttribute("validadeE", "15");
	}
	
	public void getCertificadosNaoRevogados(HttpServletRequest request, HttpServletResponse response, String nome, String posicao) throws Exception {
		String stTemp = ""; 
		CertificadoNe obNegocio;
//		WebContext wc = WebContextFactory.get();
		//testo as permissões retorno erro
//		verificarPermissao(wc);
		obNegocio =  (CertificadoNe)request.getSession().getAttribute("Certificadone");
		if (obNegocio == null )  obNegocio = new CertificadoNe();
		stTemp = obNegocio.consultaCertificadosNaoRevogadosJSON(nome, posicao);
		enviarJSON(response, stTemp);

	}
	
	public void getCertificadosNaoLiberados(HttpServletRequest request, HttpServletResponse response, String nome, String posicao) throws Exception {
		String stTemp = ""; 
		CertificadoNe obNegocio;
//		WebContext wc = WebContextFactory.get();
		//testo as permissões retorno erro
//		verificarPermissao(wc);
		obNegocio =  (CertificadoNe)request.getSession().getAttribute("Certificadone");
		if (obNegocio == null )  obNegocio = new CertificadoNe();
		stTemp = obNegocio.consultaCertificadosNaoLiberadosJSON(nome, posicao);
		enviarJSON(response, stTemp);
	}
}
