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
 * Este Ct é utilizado para que sejam recebidos e processados links que foram enviados por e-mail para os usuários.
 * Fez-se necessário pois, nestes casos específicos, não será exigida autenticação. O link enviado para o usuário
 * possui seus parâmetros obrigatóriamete criptografados. Cabe a cada Ct gerar e criptografar o seu próprio link
 * utilizando a função Funcoes.GeraHashMd5() e passando os parâmetros obrigatórios citados abaixo. Este Ct irá
 * receber o link, descriptografá-lo, verificar se ele encontra-se expirado e, se for o caso, passar os parâmetros
 * para o Ct responsável processar. A resposta do processamento do link será uma String simples, sucinta, que deve
 * ser retornada e posta na variável 'mensagem'. Se houver alguma dúvida, o case 'pc' switch(controleOrigem) deve
 * ser tomado como exemplo.
 * 
 * Exemplo de link enviado: 
 * https://projudi.tjgo.jus.br/ConfirmaLink?url=oKnUWBWWu81jLVKokVr4hNTQ65Cz2OsqdQi2m-bZjYsEZ0xCobu1O7X8zQ41-BImlZaO0gLfpx6UWBsrarxyHSNeDurUrJHen56d4nvzSl8
 *
 * Exemplo de url descriptografada:
 * pc?comp=s&id=1222&v=163302238747170277708510711782870946248&dt=1524167378249
 * 
 * Como pode ser observado na url descriptografada, dois parâmetros são obrigatórios:
 *  1) Os caracteres antes da '?': serão utilizados abaixo, em um switch, para direcionar
 *     a requisição para o Ct que gerou o link. O parâmetro 'dt'
 *  2) dt: será a data/hora em milisegundos que o link foi criado. Se o link foi criado há
 *     mais de 24 horas ele é aqui considerado como expirado.
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
		
		//O único fluxo permitido neste ct é o paginaatual=6 que envia a url
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
		
		// Verificando se o link não expirou. Todo link deve ter um parâmetro de nome dt cujo valor
		// é a data e hora em milisegundos de que o link foi criado. Nas linhas abaixo este valor
		// é verificado para saber se o link não é mais velho do que 24hs. Após este tempo, o link expira.
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
		// Força a opção default no switch para mostrar a mensagem de link inválido
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
				 		
				 		//TODO: Verificar se será dada continuidade à esta funcionalidade. 
				 		//RecuperaSenha
//				 		case "rs":
////				 				RecuperaSenhaNe recuperaSenhaNe = new RecuperaSenhaNe();
////				 				mensagem = recuperaSenhaNe.processaLinkRecuperaSenha(parametros);
////				 				System.out.println(mensagem);
//				 				break;
				 				
				 		default:
				 				mensagem = "ERRO, O LINK EXPIROU OU É INVÁLIDO.";
				 				break;
			 		}
		 		
		 		}
		 		else {
		 			mensagem =  "ERRO, O LINK É INVÁLIDO.";
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
