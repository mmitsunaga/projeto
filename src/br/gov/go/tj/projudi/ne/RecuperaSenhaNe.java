package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Funcoes;

public class RecuperaSenhaNe extends Negocio {

	private static final long serialVersionUID = 5731805837027599482L;
	
	public static final String LINK_PREFIXO 		 = "https://projudi.tjgo.jus.br/RecuperaSenha?token=";
	public static final String SAL = "3e27a687cb1ee0bdf7bb6ed35035eed3";
	
	public RecuperaSenhaNe() {		
		obLog = new LogNe();
	}
	
	/**
	 * Recebe um link de recuperação de senha que foi enviado para o usuário e processa ele.
	 * 
	 * @param String url
	 * @throws Exception
	 */
	public boolean isLinkRecuperaSenhaValido(String url) throws Exception {
		
		String id = null;
		String v = null;
		boolean isValido = false;
		
		String parametros[] = url.split("&");
		
		for(String param: parametros) {
			if( param.startsWith("id=") ) {
				id = param.substring(3);
			}
			else if( param.startsWith("v=") ) {
				v = param.substring(2);
			}
		}
		
		if( this.validaHashLinkConfirmacao(id, v) ) {
				isValido  = true;
		}

		return isValido;
	}
	
	/**
	 * Utilizando o hash, verifica se as informações recebidas pelo link de confirmação são válidas ou se
	 * foram adulteradas. Recebe o idPonteiroCejusc e busca as informações deste registro no banco para
	 * gerar o hash e comparar com o recebido.
	 * 
	 * @param String idUsu
	 * @param String hashParam
	 * @throws Exception
	 */
	private boolean validaHashLinkConfirmacao(String idUsu, String hashParam) throws Exception {
		String hashCorreto = null;
		UsuarioDt usuDt;
		UsuarioNe usuNe = new UsuarioNe(); 
		
		if(idUsu != null) {
			usuDt = usuNe.consultarId(idUsu);
			if( usuDt != null) {
				hashCorreto = Funcoes.GeraHashMd5( usuDt.getId() + usuDt.getUsuario() + usuDt.getRg() + SAL );
			}
		}
		
		if( hashCorreto != null && hashCorreto.equals(hashParam) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String gerarEmailRecuperacaoSenha(String token, String nome) {

		String mensagemEmail = "<br />";
		mensagemEmail += "Olá " + nome + "! Para cadastrar a sua nova senha do Processo Digital, clique no link abaixo";
		mensagemEmail += "<br />";
		mensagemEmail += "<br />";
		mensagemEmail += "<br/>";
		mensagemEmail += "<a href='" + LINK_PREFIXO + token + "'>CRIAR NOVA SENHA</a>";
		mensagemEmail += "<br />";
		mensagemEmail += "<br />";
		mensagemEmail += "<br />";
		mensagemEmail += "<p style='font-size:small'>";
		mensagemEmail += "Caso esteja com dificuldade em clicar no link acima, copie e cole o endereço abaixo em uma nova janela ou aba de seu navegador:";
		mensagemEmail += "<br />";
		mensagemEmail += LINK_PREFIXO + token;
		mensagemEmail += "<br />";
		mensagemEmail += "</p>";
		
		return mensagemEmail;
	}
	
}
