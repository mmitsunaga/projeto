package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.PonteiroCejuscNe;
import br.gov.go.tj.projudi.ne.RecuperaSenhaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class RecuperaSenhaCt extends Controle {

	private static final long serialVersionUID = -1306467754080844788L;

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual)throws Exception, ServletException, IOException {
		
		final String SAL = "3e27a687cb1ee0bdf7bb6ed35035eed3";
		
		RequestDispatcher dis = null; 
		String email;
		String usuarioParam;
		String emailParam;
		String token = "";
		String tokenParam;
		String[] arrayParam;
		String parametros = "";
		String senha = "senha";
		String confirmarSenha = "confirmarSenha";
		String decrypted;
		UsuarioDt usu;
		
		RecuperaSenhaNe recuperaSenhaNe = new RecuperaSenhaNe();
		
		usuarioParam = request.getParameter("usuario");
		emailParam = request.getParameter("email");
		tokenParam = request.getParameter("token");
		
		if(tokenParam != null) {
			decrypted = Funcoes.aesDecrypt(tokenParam);
			if(decrypted != null) {
				paginaatual = Configuracao.Curinga7;
				arrayParam = decrypted.split("\\?");
			
				if(arrayParam.length == 1) {
					parametros = arrayParam[0];
				}
			}
			else {
				request.setAttribute("fluxo", "linkInvalido");
			}
		}
		
		if(request.getAttribute("fluxo") == null) {
			request.setAttribute("fluxo", "");
		}
		
		request.setAttribute("msg", "");
		
		switch (paginaatual) {
		
			// ENVIAR E-MAIL COM O LINK
		 	case Configuracao.Curinga6:
		 		
		 		// -- Consulta usuario
		 		usu = UsuarioSessao.consultarUsuarioCpf(usuarioParam);
		 		
		 		if(usu != null) {
		 			
			 		// -- Gera Link
			 		token = Funcoes.aesEncrypt( "id=" + usu.getId() + "&v=" + Funcoes.GeraHashMd5( usu.getId() + usu.getUsuario() + usu.getRg() + SAL) );
			 		
			 		// -- Envia e-mail
			 		email = UsuarioSessao.consultarEmailUsuario(usu.getId());
			 		if(email != null && !email.equals("")) {			 			
			 			new GerenciadorEmail(usu.getNome(), email, "Recuperação de Senha", recuperaSenhaNe.gerarEmailRecuperacaoSenha(token, usu.getNome()), GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			 			request.setAttribute("fluxo", "emailEnviado");
			 		}
			 		else {
			 			request.setAttribute("fluxo", "semEmail");
			 		}
		 		
		 		} 
		 		else {
		 		
		 			request.setAttribute("msg", "Este usuário não existe");
		 			request.setAttribute("paginaatual", Configuracao.Curinga6);
		 			request.setAttribute("fluxo", "inicio");
		 			
		 		}
		 		break;
		 		
		 		
		 	case Configuracao.Curinga7:
		 		if( recuperaSenhaNe.isLinkRecuperaSenhaValido(parametros) ){
		 			//RECUPERAR SENHA
		 			request.setAttribute("fluxo", "mostrarFormulario");
		 			request.setAttribute("paginaatual", Configuracao.Salvar);
		 			request.setAttribute("tokenParam", tokenParam);
		 		} else {
		 			//NÃO PERMITIDO
		 			request.setAttribute("fluxo", "linkInvalido");
		 		}
		 		break;
		 		
		 		
		 	case Configuracao.Salvar:
		 			
		 			tokenParam = Funcoes.aesDecrypt( request.getParameter("tokenParam") );
		 			senha = request.getParameter("novaSenha1");
		 			confirmarSenha = request.getParameter("novaSenha2");
		 			if( tokenParam != null && !tokenParam.equals("") && recuperaSenhaNe.isLinkRecuperaSenhaValido(tokenParam)) {
		 				if( senha.length() > 5 && senha.equals(confirmarSenha) ) {

		 					
		 					
		 					String param[] = tokenParam.split("&");
		 					String id = null;
		 					String v = null;
		 					
		 					for(String p: param) {
		 						if( p.startsWith("id=") ) {
		 							id = p.substring(3);
		 						}
		 						else if( p.startsWith("v=") ) {
		 							v = p.substring(2);
		 						}
		 					}
		 					
		 					//usu = UsuarioSessao.consultarUsuarioCompleto(id);
		 					usu = new UsuarioDt();
		 					usu.setId(id);
		 					usu.setId_UsuarioLog(UsuarioDt.USUARIO_PUBLICO);
		 					
		 					UsuarioSessao.alterarSenhaSemVerificarAtual(usu, senha);
		 					
		 					request.setAttribute("fluxo", "senhaAlterada");
		 					
		 				} else {
		 					request.setAttribute("fluxo", "mostrarFormulario");
				 			request.setAttribute("paginaatual", Configuracao.Salvar);
				 			request.setAttribute("tokenParam", request.getParameter("tokenParam"));
				 			request.setAttribute("msg", "As duas senhas são diferentes ou você digitou uma senha inválida");
		 				}
		 			}
		 			
					
		 		break;
		 		
		 	default:
		 		//Carregar página inicial
		 		request.setAttribute("paginaatual", Configuracao.Curinga6);
		 		if(request.getAttribute("fluxo") == null || request.getAttribute("fluxo").equals("")) {
		 			request.setAttribute("fluxo", "inicio");
		 		}
		 		break;
        }
		
		dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/RecuperaSenha.jsp");
 		dis.include(request, response);
	
	}
	
	@Override
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public int Permissao() {
		return 890;
	}
	
	
	
}
