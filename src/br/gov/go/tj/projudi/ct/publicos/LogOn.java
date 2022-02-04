package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.GerenciaUsuarios;
import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class LogOn extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2575296426247409932L;
	
	public int Permissao(){
		return UsuarioDt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual)	throws Exception, ServletException, IOException {		
		
		String Usuario = "";
		String Senha = "";		
		RequestDispatcher dis = null; 
		
		boolean isExecpenweb = false;
		
		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");
					
		if (request.getParameter("Usuario") == null) Usuario = "Usuario";
		else Usuario = request.getParameter("Usuario");
		if (request.getParameter("Senha") == null) Senha = "Senha";
		else Senha = request.getParameter("Senha");
		
		if (request.getParameter("Acesso") != null && request.getParameter("Acesso").equals("Execpenweb")){
			isExecpenweb = true;
			request.getSession().setAttribute("Acesso", "Execpenweb");
		}
		if( request.getSession().getAttribute("Acesso") != null && request.getSession().getAttribute("Acesso").equals("Execpenweb") ) {
			isExecpenweb = true;
			request.getSession().setAttribute("Acesso", "Execpenweb");
		}
		//Comentado para evitar erro de utilização indevida de sessão.
//		if (!UsuarioSessao.isPublico()) {			
//			//redireciona para obeter a serventia grupo
//			redireciona(response, "Usuario?PaginaAtual=9");
//			return;
//		}
		
		/**
		 * Logon Projudi Novo
		 */
		if (paginaatual== Configuracao.Curinga6){
			redireciona(response, "certificado");
			return;
		} else if (paginaatual== Configuracao.Curinga8){
			request.getSession().setAttribute("CadastroComCertificado", "true");
			redireciona(response, "certificado");
			return;
		} else if (paginaatual== Configuracao.Curinga7){		
			if (UsuarioSessao.logarUsuarioSenha(Usuario, Senha)) {
				if(Senha != null && Senha.equalsIgnoreCase(UsuarioNe.SENHA_INVALIDA)){
					request.getSession(false).invalidate();
					request.setAttribute("MensagemErro", "Usuário ou senha inválidos.");						
					if (isExecpenweb) {
						dis = request.getRequestDispatcher("/index2.jsp");
					}
					else {
						dis = request.getRequestDispatcher("/index.jsp");
					}
					dis.forward(request, response);
					return;
				}
				if(Senha != null && (Senha.equalsIgnoreCase("12345") || Funcoes.isSenhaFraca(Senha))) {
					UsuarioSessao.getUsuarioDt().setCodigoTemp("trocarSenha");
				}
				
				GerenciaUsuarios.getInstancia().addUsuario(UsuarioSessao.getId_Usuario(), request.getSession(false));
				
				request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());
				UsuarioSessao.setPermissao(UsuarioDt.CodigoPermissao);					
				UsuarioSessao.setPermissao(2166);
				UsuarioSessao.setPermissao(2167);
				UsuarioSessao.setPermissao(2169);
				UsuarioSessao.setPermissao(2160);
				request.getSession().setAttribute("UsuarioSessao", UsuarioSessao);
				
				//redireciona para obeter a serventia grupo
				redireciona(response, "Usuario?PaginaAtual=9");
				
				return;

			} else {
				request.getSession(false).invalidate();
				request.setAttribute("MensagemErro", "Usuário ou senha inválidos.");					
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
				
	}
	
	private void paginaInicial(){
		
	}


	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
    protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
}
