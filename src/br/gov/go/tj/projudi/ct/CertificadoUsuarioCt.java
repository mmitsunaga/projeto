package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.dt.CertificadoUsuarioDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.CertificadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.Identidade;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class CertificadoUsuarioCt extends CertificadoCtGen{

/**
     * 
     */
    private static final long serialVersionUID = 3139744112798316527L;

    //
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertificadoDt Certificadodt;
		CertificadoNe Certificadone;

		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/CertificadoUsuarioBaixar.jsp";
		
		request.setAttribute("tempPrograma","CertificadoUsuario");
		request.setAttribute("tempBuscaId_Certificado","Id_Certificado");
		request.setAttribute("tempBuscaUsuarioCertificado","UsuarioCertificado");
		request.setAttribute("tempRetorno","CertificadoUsuario");

		Certificadone =(CertificadoNe)request.getSession().getAttribute("Certificadone");
		if (Certificadone == null )  Certificadone = new CertificadoNe();  

		Certificadodt =(CertificadoDt)request.getSession().getAttribute("Certificadodt");
		if (Certificadodt == null )  Certificadodt = new CertificadoDt();  

		//PARAMETROS PARA CRIAR IDENTIDADE DIGITAL*****************
		Identidade id = new Identidade();
		String senha = request.getParameter("senha");
		String cSenha = request.getParameter("cSenha");
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
				//CONFIRMAR REVOCAO DE CERTIFICADO DO USUARIO LOGADO
				stAcao="/WEB-INF/jsptjgo/CertificadoMensagemConfirmacao.jsp";
				request.setAttribute("certificadoDt", Certificadone.consultarId(request.getParameter("Id_Certificado")));
				request.setAttribute("PaginaAtual", Configuracao.Editar);
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
				
				//LOCALIZAR TODOS OS CERTICADOS DO USUARIO LOGADO
				tempList =Certificadone.consultaCertificadosUsuario(UsuarioSessao.getId_Usuario());
				if (tempList.size()>0){
					request.setAttribute("baixar",new Boolean(true));
					request.setAttribute("ListaCertificado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
					Certificadodt.limpar();
				}
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				
				break;
			case Configuracao.Imprimir: 
				break;
			case Configuracao.Curinga6:
				//BAIXAR CERTIFICADO
				Certificadodt = Certificadone.consultarId(request.getParameter("Id_Certificado"));
				Certificadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				Certificadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				 
				if (Certificadodt.getLiberado().equalsIgnoreCase("FALSE")) {
					 Mensagem = "O certificado não foi liberado para download. ";
				}
				if (Mensagem.length()==0){
					response.setContentType("application/x-pkcs12");
					response.setHeader("Content-disposition", "attachment; filename=certificado_" +Certificadodt.getUsuarioCertificado()+".p12");
					
					ServletOutputStream servletOutputStream = response.getOutputStream();
					Certificadone.baixarCertificado(Certificadodt, servletOutputStream);
					
					return;
				} else {
					request.setAttribute("MensagemErro", Mensagem );
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					//LOCALIZAR TODOS OS CERTICADOS DO USUARIO LOGADO
					tempList =Certificadone.consultaCertificadosUsuario(UsuarioSessao.getId_Usuario());
					if (tempList.size()>0){
						request.setAttribute("baixar",new Boolean(true));
						request.setAttribute("ListaCertificado", tempList); 
						request.setAttribute("PaginaAtual", Configuracao.Localizar);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
						Certificadodt.limpar();
					}
				}
				
				break;
			case Configuracao.Localizar:
				//LOCALIZAR TODOS OS CERTICADOS DO USUARIO LOGADO
				tempList =Certificadone.consultaCertificadosUsuario(UsuarioSessao.getId_Usuario());
				if (tempList.size()>0){
					request.setAttribute("baixar",new Boolean(true));
					request.setAttribute("ListaCertificado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
					Certificadodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Certificados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.LocalizarDWR: 
				break;
			case Configuracao.Novo: 
				
				if (UsuarioSessao.getUsuarioDt().isAdvogadoParticular()){
					
					request.setAttribute("MensagemErro", "Prezado(a) Advogado(a) <br>"
							+ " Informamos que a Resolução n° 59/2016, que regulamenta o "
							+ "processo digital no âmbito do Poder Judiciário do Estado de Goiás, reza em seu artigo 9° "
							+ "que os advogados serão habilitados obrigatoriamente, por meio de certificado digital "
							+ "(padrão A3 - ICP Brasil). Assim, não será mais permitido o uso do certificado padrão A1, "
							+ "emitido pelo TJGO."); 
					
					tempList =Certificadone.consultaCertificadosUsuario(UsuarioSessao.getId_Usuario());
					if (tempList.size()>0){
						request.setAttribute("baixar",new Boolean(true));
						request.setAttribute("ListaCertificado", tempList); 
						request.setAttribute("PaginaAtual", Configuracao.Localizar);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
						Certificadodt.limpar();
					}else{ 
						request.setAttribute("MensagemErro", "Certificados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
								
					break;
				}else{				
					Certificadodt.limpar();
					stAcao = "/WEB-INF/jsptjgo/CertificadoUsuario.jsp";
					request.setAttribute("senha", "");
					request.setAttribute("cSenha", "");
					request.setAttribute("PaginaAtual",Configuracao.Editar);
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido()); 
					break;
				}
			case Configuracao.Salvar:
				break;
			case Configuracao.SalvarResultado:
				//CRIAR IDENTIDADE DIGITAL ****************************************************
				criaID(id,UsuarioSessao);
				id.setSenha(senha);
				id.setCsenha(cSenha);		
				Mensagem = id.validaCampos();
				
				if (Certificadone.consultaNumeroCertificadosValido(UsuarioSessao.getId_Usuario()) > 0) {
					Mensagem = "Não é permitido ao usuário ter mais de uma identidade digital ativa. Se você deseja criar uma nova por motivo de segurança, deve primeiro revogar a atual.";
				}

				if (Mensagem.length()==0){
					if(Certificadone.criarIdentidadeDigital(UsuarioSessao.getUsuarioDt(),id,Certificadodt.getId_UsuarioLog(), Certificadodt.getIpComputadorLog())){
						RequestDispatcher dis =	request.getRequestDispatcher("/WEB-INF/jsptjgo/Identidade.htm");
						dis.include(request, response);
						return;
					} else
						request.setAttribute("MensagemErro", "Erro ao Criar Identidade Digital");
				}else {
					request.setAttribute("MensagemErro", Mensagem );
					request.setAttribute("senha", "");
					request.setAttribute("cSenha", "");
				}
				
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stAcao = "/WEB-INF/jsptjgo/CertificadoUsuario.jsp";
				break;
//--------------------------------------------------------------------------------//
			default:
				//LOCALIZAR TODOS OS CERTICADOS DO USUARIO LOGADO
				tempList =Certificadone.consultaCertificadosUsuario(UsuarioSessao.getId_Usuario());
				if (tempList.size()>0){
					request.setAttribute("baixar",new Boolean(true));
					request.setAttribute("ListaCertificado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
					Certificadodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Certificados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
			}

		request.getSession().setAttribute("Certificadodt",Certificadodt );
		request.getSession().setAttribute("Certificadone",Certificadone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected void criaID(Identidade id,UsuarioNe usuario) {
		id.setSenha("");
		id.setCsenha("");
		id.setCidade(usuario.getUsuarioDt().getUsuarioCidade());
		id.setEstado(usuario.getUsuarioDt().getUsuarioEstado());
		id.setEmail(usuario.getUsuarioDt().getEMail());
		id.setNome(usuario.getUsuarioDt().getNome());
		id.setCPF(usuario.getUsuarioDt().getCpf());
		id.setRG(usuario.getUsuarioDt().getRg());
		id.setRGExpedidor(usuario.getUsuarioDt().getRgOrgaoExpedidor());				
		id.setRGExpedidorUF(usuario.getUsuarioDt().getRgOrgaoExpedidorUf());
		id.setDataNascimento(usuario.getUsuarioDt().getDataNascimento());		
		id.setPais("BR");
		id.setNIS(null);		
 		id.setINSS(null);	
 		id.setEhPessoaFisica();
	}
	
	public int Permissao(){
		return CertificadoUsuarioDt.CodigoPermissao;
	}
}
