package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.PonteiroCejuscNe;
import br.gov.go.tj.projudi.ne.RecuperaSenhaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;


/**
 * 
 * @author hrrosa
 * 
 * Este Ct � utilizado para que sejam recebidos e processados links que foram enviados por e-mail para os usu�rios.
 * Fez-se necess�rio pois, nestes casos espec�ficos, n�o ser� exigida autentica��o. O link enviado para o usu�rio
 * possui seus par�metros obrigat�riamete criptografados. Cabe a cada Ct gerar e criptografar o seu pr�prio link
 * utilizando a fun��o Funcoes.GeraHashMd5() e passando os par�metros obrigat�rios citados abaixo. Este Ct ir�
 * receber o link, descriptograf�-lo, verificar se ele encontra-se expirado e, se for o caso, passar os par�metros
 * para o Ct respons�vel processar. A resposta do processamento do link ser� uma String simples, sucinta, que deve
 * ser retornada e posta na vari�vel 'mensagem'. Se houver alguma d�vida, o case 'pc' switch(controleOrigem) deve
 * ser tomado como exemplo.
 * 
 * Exemplo de link enviado: 
 * https://projudi.tjgo.jus.br/ConfirmaLink?url=oKnUWBWWu81jLVKokVr4hNTQ65Cz2OsqdQi2m-bZjYsEZ0xCobu1O7X8zQ41-BImlZaO0gLfpx6UWBsrarxyHSNeDurUrJHen56d4nvzSl8
 *
 * Exemplo de url descriptografada:
 * pc?comp=s&id=1222&v=163302238747170277708510711782870946248&dt=1524167378249
 * 
 * Como pode ser observado na url descriptografada, dois par�metros s�o obrigat�rios:
 *  1) Os caracteres antes da '?': ser�o utilizados abaixo, em um switch, para direcionar
 *     a requisi��o para o Ct que gerou o link. O par�metro 'dt'
 *  2) dt: ser� a data/hora em milisegundos que o link foi criado. Se o link foi criado h�
 *     mais de 24 horas ele � aqui considerado como expirado.
 *     
 * 
 */
public class ConfirmaLinkCt extends Controle {

	private static final long serialVersionUID = 304803368085963870L;
	
	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual)throws Exception, ServletException, IOException {
		RequestDispatcher dis = null; 
		String decrypted;
		String controleOrigem = null;
		String parametros = null;
		String arrayParam[] = null;
		String mensagem = "";
		Date dataLink = null;
		String[] parametrosDivididos = null;
		
		String url = request.getParameter("url");
		
		//O �nico fluxo permitido neste ct � o paginaatual=6 que envia a url
		//para o ne correspondente
		paginaatual = 6;
		
		if(url != null) {
			try {
				decrypted = Funcoes.aesDecrypt(url);
			}
			catch(BadPaddingException e) {
				decrypted = null;
			}

			if(decrypted != null) {
				arrayParam = decrypted.split("\\?");
			
				if(arrayParam.length == 2) {
					controleOrigem = arrayParam[0];
					parametros = arrayParam[1];
				}
			}
		}
		
		// Verificando se o link n�o expirou. Todo link deve ter um par�metro de nome dt cujo valor
		// � a data e hora em milisegundos de que o link foi criado. Nas linhas abaixo este valor
		// � verificado para saber se o link n�o � mais velho do que 24hs. Ap�s este tempo, o link expira.
		Boolean linkInvalido = true;
		if(parametros != null){
			parametrosDivididos = parametros.split("&");
			if(parametrosDivididos != null) {
				for(String p: parametrosDivididos){
					if( p.lastIndexOf("dt=") != -1 ){
						p = p.replace("dt=", "");
						dataLink = new Date( Funcoes.StringToLong(p) );
						if(dataLink != null && Funcoes.calculaDiferencaEntreDatas(dataLink, new Date() ) == 0) {
							linkInvalido = false;
						}
					}
				}
			}
		}
		// For�a a op��o default no switch para mostrar a mensagem de link inv�lido
		if(linkInvalido) {
			controleOrigem = "link_ivalido";
		}
		
		
		switch (paginaatual) {
		
		 	case Configuracao.Curinga6:
		 		if(controleOrigem != null) {

		 			switch(controleOrigem) {
			 			//Enviar para o PonteiroCejuscNe
				 		case "pc":
				 				PonteiroCejuscNe ponteiroCejuscNe = new PonteiroCejuscNe();
				 				mensagem = ponteiroCejuscNe.processaLinkConfirmacaoComparecimento(parametros);
				 				break;
				 		
				 		//TODO: Verificar se ser� dada continuidade � esta funcionalidade. 
				 		//RecuperaSenha
//				 		case "rs":
////				 				RecuperaSenhaNe recuperaSenhaNe = new RecuperaSenhaNe();
////				 				mensagem = recuperaSenhaNe.processaLinkRecuperaSenha(parametros);
////				 				System.out.println(mensagem);
//				 				break;
				 				
				 		default:
				 				mensagem = "ERRO, O LINK EXPIROU OU � INV�LIDO.";
				 				break;
			 		}
		 		
		 		}
		 		else {
		 			mensagem =  "ERRO, O LINK � INV�LIDO.";
		 		}
		 		request.setAttribute("mensagem", mensagem);
		 		dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/ConfirmaLink.jsp");
		 		dis.include(request, response);
		 		break;
        }
	
	}
	
	@Override
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public int Permissao() {
		return 887;
	}
	
	
	
}
