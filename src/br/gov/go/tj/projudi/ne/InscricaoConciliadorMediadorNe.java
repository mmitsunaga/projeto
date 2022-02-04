package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class InscricaoConciliadorMediadorNe extends Negocio {

	private static final long serialVersionUID = -1208542604199003068L;
	
	public static final String NOME_CONTROLE 		= "InscricaoConciliadorMediador";
	public static final String CIENTE_INSCRICAO_SIM = "1";
	public static final String LINK_SISTEMA 		= "http://www.tjgo.jus.br/conciliadores";
//	public static final String LINK_SISTEMA 		= "http://localhost:8080/conciliadores";
	public static final String LINK_CONFIRMAR_EMAIL = "/ConfirmacaoEmailInscricao";
	public static final String LINK_ACOMPANHAMENTO  = "/AcompanhamentoInscricao";
	
	/**
	 * M�todo para enviar email para candidato.
	 * @param String email
	 * @throws Exception 
	 */
	public boolean enviarEmailParaCandidato(String emailInscricao, String cpfLink) throws Exception {
		boolean retorno = false;
		
		if( emailInscricao != null ) {
			String mensagemEmail = geraMensagemEmail(emailInscricao, cpfLink);						
			
			new GerenciadorEmail("Ol�!" , emailInscricao, "TJGO: Confirme sua inscri��o!", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * M�todo para enviar email para candidato confirmando seu novo status.
	 * 
	 * @param String emailInscricao
	 * @param String nomeUsuario
	 * @param String status
	 * @return boolean
	 * @throws Exception
	 */
	public boolean enviarEmailNovoStatus(String emailInscricao, String nomeUsuario, String status) throws Exception {
		boolean retorno = false;
		
		if( emailInscricao != null ) {
			
			String linkAcompanhamento = LINK_SISTEMA + LINK_ACOMPANHAMENTO;
			
			String mensagemEmail = "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "A nova situa��o de sua inscri��o foi atualizada para <b>"+ status +"</b>. Caso deseje verificar a observa��o deixada pelo avaliador, acesse o link de acompanhamento a seguir:";
			mensagemEmail += "<br />";
			mensagemEmail += "<a href='"+linkAcompanhamento+"'>"+linkAcompanhamento+"</a>";
			
			if(status.equals("APROVADO")){
				mensagemEmail += "<br /><br />";
				mensagemEmail += "Agora voc� deve acessar o Sistema de Processo Digital (https://projudi.tjgo.jus.br/) com o seu login e senha para escolher os locais e hor�rios nos quais voc� estar� dispon�vel para concilia��es/media��es.";
			}
			mensagemEmail += "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "Caso esteja com dificuldade em clicar no link ou endere�o eletr�nico acima, por favor, copie e cole o endere�o em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a p�gina.";
									
			new GerenciadorEmail("Ol�!" , emailInscricao, "TJGO: Status de sua inscri��o(Conciliador/Mediador)", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * M�todo para enviar email de comprova��o de inscri��o.
	 * @param String emailInscricao
	 * @throws Exception
	 */
	public boolean enviarEmailComprovandoInscricao(String emailInscricao, String nomePessoa) throws Exception {
		boolean retorno = false;
		
		if( emailInscricao != null ) {
			String mensagemEmail = geraMensagemEmailComprovandoInscricao(emailInscricao);
						
			new GerenciadorEmail(nomePessoa , emailInscricao, "TJGO: Inscri��o Realizada!", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * M�todo para enviar mensagem sobre senha para o email do candidato.
	 * @param String email
	 * @throws Exception 
	 */
	public boolean enviarEmailSobreSenha(String emailInscricao) throws Exception {
		boolean retorno = false;
		
		if( emailInscricao != null ) {
			String mensagemEmail = geraMensagemEnvioSenha(emailInscricao);						
			
			new GerenciadorEmail("Ol�!" , emailInscricao, "TJGO: Seu pedido com a sua senha!", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * M�todo que gera a mensage de email para o candidato.
	 * @param String emailDestino
	 * @param String cpfLink
	 * @return String mensagem
	 * @throws Exception 
	 */
	private String geraMensagemEmail(String emailDestino, String cpfLink) throws Exception {
		String retorno = "<b>Centro de Concilia��o do Tribunal de Justi�a do Estado de Goi�s</b>";
		
		if( emailDestino != null ) {
			
			String codigoHash = Funcoes.GeraHashMd5(emailDestino + cpfLink + ConfirmacaoEmailInscricaoNe.CODIGO_PARA_HASH);
			
			String linkEmail = LINK_SISTEMA + LINK_CONFIRMAR_EMAIL + "?email=" + emailDestino + "&cpfLink=" + cpfLink +"&codigoValidacao=" + codigoHash + "&PaginaAtual=" + Configuracao.Curinga6;
			
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Confirme sua inscri��o clicando neste link:";
			retorno += "<br />";
			retorno += "<a href='"+linkEmail+"'>"+linkEmail+"</a>";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Preencha o termo de compromisso que se encontra na p�gina de inscri��o e anexe ele juntamente com os documentos solicitados.";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Caso esteja com dificuldade em clicar no link ou endere�o eletr�nico, por favor, copie e cole o endere�o em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a p�gina.";
		}
		
		return retorno;
	}
	
	/**
	 * M�todo que gera a mensage de email para o candidato comprovando a inscri��o.
	 * @param String emailDestino
	 * @return String mensagem
	 * @throws Exception 
	 */
	private String geraMensagemEmailComprovandoInscricao(String emailDestino) throws Exception {
		String retorno = "<b>Centro de Concilia��o do Tribunal de Justi�a do Estado de Goi�s</b>";
		
		if( emailDestino != null ) {
			
			String linkEmail = LINK_SISTEMA + LINK_ACOMPANHAMENTO;
			
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Sua inscri��o foi realizada. Acompanhe o andamento no link abaixo.";
			retorno += "<br />";
			retorno += "Sua senha inicial � 12345. Recomendamos que acesse e realize a mudan�a de sua senha. Guarde com cuidado a sua senha.";
			retorno += "<br />";
			retorno += "<a href='"+linkEmail+"'>"+linkEmail+"</a>";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Caso esteja com dificuldade em clicar no link ou endere�o eletr�nico, por favor, copie e cole o endere�o em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a p�gina.";
		}
		
		return retorno;
	}
	
	/**
	 * M�todo para enviar mensagem sobre senha para o email do candidato.
	 * @param String emailDestino
	 * @throws Exception 
	 */
	private String geraMensagemEnvioSenha(String emailDestino) throws Exception {
		String retorno = "<b>Centro de Concilia��o do Tribunal de Justi�a do Estado de Goi�s</b>";
		
		if( emailDestino != null ) {
			
			String linkEmail = LINK_SISTEMA + LINK_ACOMPANHAMENTO;
			
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Sua senha de acesso inicial � 12345 e o login ou usu�rio � o seu CPF. Caso j� tenha mudado sua senha e esquecido, por favor, se encaminhe at� um cadastrador do TJGO Projudi mais pr�ximo de voc� para realizar a altera��o.";
			retorno += "<br />";
			retorno += "<a href='"+linkEmail+"'>"+linkEmail+"</a>";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Caso esteja com dificuldade em clicar no link ou endere�o eletr�nico, por favor, copie e cole o endere�o em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a p�gina.";
		}
		
		return retorno;
	}
}