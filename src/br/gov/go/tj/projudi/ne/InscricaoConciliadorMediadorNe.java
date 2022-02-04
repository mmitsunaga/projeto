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
	 * Método para enviar email para candidato.
	 * @param String email
	 * @throws Exception 
	 */
	public boolean enviarEmailParaCandidato(String emailInscricao, String cpfLink) throws Exception {
		boolean retorno = false;
		
		if( emailInscricao != null ) {
			String mensagemEmail = geraMensagemEmail(emailInscricao, cpfLink);						
			
			new GerenciadorEmail("Olá!" , emailInscricao, "TJGO: Confirme sua inscrição!", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para enviar email para candidato confirmando seu novo status.
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
			mensagemEmail += "A nova situação de sua inscrição foi atualizada para <b>"+ status +"</b>. Caso deseje verificar a observação deixada pelo avaliador, acesse o link de acompanhamento a seguir:";
			mensagemEmail += "<br />";
			mensagemEmail += "<a href='"+linkAcompanhamento+"'>"+linkAcompanhamento+"</a>";
			
			if(status.equals("APROVADO")){
				mensagemEmail += "<br /><br />";
				mensagemEmail += "Agora você deve acessar o Sistema de Processo Digital (https://projudi.tjgo.jus.br/) com o seu login e senha para escolher os locais e horários nos quais você estará disponível para conciliações/mediações.";
			}
			mensagemEmail += "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "Caso esteja com dificuldade em clicar no link ou endereço eletrônico acima, por favor, copie e cole o endereço em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a página.";
									
			new GerenciadorEmail("Olá!" , emailInscricao, "TJGO: Status de sua inscrição(Conciliador/Mediador)", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para enviar email de comprovação de inscrição.
	 * @param String emailInscricao
	 * @throws Exception
	 */
	public boolean enviarEmailComprovandoInscricao(String emailInscricao, String nomePessoa) throws Exception {
		boolean retorno = false;
		
		if( emailInscricao != null ) {
			String mensagemEmail = geraMensagemEmailComprovandoInscricao(emailInscricao);
						
			new GerenciadorEmail(nomePessoa , emailInscricao, "TJGO: Inscrição Realizada!", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para enviar mensagem sobre senha para o email do candidato.
	 * @param String email
	 * @throws Exception 
	 */
	public boolean enviarEmailSobreSenha(String emailInscricao) throws Exception {
		boolean retorno = false;
		
		if( emailInscricao != null ) {
			String mensagemEmail = geraMensagemEnvioSenha(emailInscricao);						
			
			new GerenciadorEmail("Olá!" , emailInscricao, "TJGO: Seu pedido com a sua senha!", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método que gera a mensage de email para o candidato.
	 * @param String emailDestino
	 * @param String cpfLink
	 * @return String mensagem
	 * @throws Exception 
	 */
	private String geraMensagemEmail(String emailDestino, String cpfLink) throws Exception {
		String retorno = "<b>Centro de Conciliação do Tribunal de Justiça do Estado de Goiás</b>";
		
		if( emailDestino != null ) {
			
			String codigoHash = Funcoes.GeraHashMd5(emailDestino + cpfLink + ConfirmacaoEmailInscricaoNe.CODIGO_PARA_HASH);
			
			String linkEmail = LINK_SISTEMA + LINK_CONFIRMAR_EMAIL + "?email=" + emailDestino + "&cpfLink=" + cpfLink +"&codigoValidacao=" + codigoHash + "&PaginaAtual=" + Configuracao.Curinga6;
			
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Confirme sua inscrição clicando neste link:";
			retorno += "<br />";
			retorno += "<a href='"+linkEmail+"'>"+linkEmail+"</a>";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Preencha o termo de compromisso que se encontra na página de inscrição e anexe ele juntamente com os documentos solicitados.";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Caso esteja com dificuldade em clicar no link ou endereço eletrônico, por favor, copie e cole o endereço em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a página.";
		}
		
		return retorno;
	}
	
	/**
	 * Método que gera a mensage de email para o candidato comprovando a inscrição.
	 * @param String emailDestino
	 * @return String mensagem
	 * @throws Exception 
	 */
	private String geraMensagemEmailComprovandoInscricao(String emailDestino) throws Exception {
		String retorno = "<b>Centro de Conciliação do Tribunal de Justiça do Estado de Goiás</b>";
		
		if( emailDestino != null ) {
			
			String linkEmail = LINK_SISTEMA + LINK_ACOMPANHAMENTO;
			
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Sua inscrição foi realizada. Acompanhe o andamento no link abaixo.";
			retorno += "<br />";
			retorno += "Sua senha inicial é 12345. Recomendamos que acesse e realize a mudança de sua senha. Guarde com cuidado a sua senha.";
			retorno += "<br />";
			retorno += "<a href='"+linkEmail+"'>"+linkEmail+"</a>";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Caso esteja com dificuldade em clicar no link ou endereço eletrônico, por favor, copie e cole o endereço em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a página.";
		}
		
		return retorno;
	}
	
	/**
	 * Método para enviar mensagem sobre senha para o email do candidato.
	 * @param String emailDestino
	 * @throws Exception 
	 */
	private String geraMensagemEnvioSenha(String emailDestino) throws Exception {
		String retorno = "<b>Centro de Conciliação do Tribunal de Justiça do Estado de Goiás</b>";
		
		if( emailDestino != null ) {
			
			String linkEmail = LINK_SISTEMA + LINK_ACOMPANHAMENTO;
			
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Sua senha de acesso inicial é 12345 e o login ou usuário é o seu CPF. Caso já tenha mudado sua senha e esquecido, por favor, se encaminhe até um cadastrador do TJGO Projudi mais próximo de você para realizar a alteração.";
			retorno += "<br />";
			retorno += "<a href='"+linkEmail+"'>"+linkEmail+"</a>";
			retorno += "<br />";
			retorno += "<br />";
			retorno += "Caso esteja com dificuldade em clicar no link ou endereço eletrônico, por favor, copie e cole o endereço em uma nova janela ou aba de seu navegador preferido e pressione a tecla ENTER para acessar a página.";
		}
		
		return retorno;
	}
}