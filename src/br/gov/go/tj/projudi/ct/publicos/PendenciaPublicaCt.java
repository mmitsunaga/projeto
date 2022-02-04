package br.gov.go.tj.projudi.ct.publicos;

import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;

public class PendenciaPublicaCt extends Controle {

	private static final long serialVersionUID		  = 64713257283622314L;

	private static final int  VALIDAR_DOCUMENTO		  = 1;
	private static final int  INTIMACOES_DIA_ANTERIOR = 2;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		byte[] byTemp = null;

		String stAcao = "/WEB-INF/jsptjgo/ValidarDocumentoPublico.jsp";

		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");

		request.setAttribute("tempPrograma", "PendenciaPublica");
		request.setAttribute("tempRetorno", "PendenciaPublica");

		switch (paginaatual) {
			case VALIDAR_DOCUMENTO:
				String codValidacao = request.getParameter("codPublicacao");
				String nomeArquivo = "Doc_" + codValidacao;
				String Id_Arquivo = Cifrar.decodificar(codValidacao, PendenciaArquivoDt.CodigoPermissao);

				if (Funcoes.StringToInt(Id_Arquivo) == -1) {
					stAcao = "/WEB-INF/jsptjgo/Erro.jsp";
					request.setAttribute("Mensagem", "C�digo inv�lido, n�o foi poss�vel validar a publica��o. Se o documento foi gerado antes de 08/02/2018, favor gerar um novo documento no formato PDF e utilizar o novo c�digo que ser� informado.");
				}

				if (checkRecaptcha(request)) {
					if (ValidacaoUtil.isNaoVazio(Id_Arquivo)) {
						PendenciaNe pendenciaNe = new PendenciaNe();
						byTemp = pendenciaNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), Id_Arquivo);
						if (byTemp == null) {
							request.setAttribute("Mensagem", "Erro, arquivo n�o dispon�vel ou bloqueado.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
							return;
						}
						enviarPDF(response, byTemp, nomeArquivo);
						return;
					}
				}
				break;

			case INTIMACOES_DIA_ANTERIOR: // Download do arquivo com a lista de intima��es geradas no dia anterior
				String stGerando = (String) request.getServletContext().getAttribute("Relatorio_Intimacoes_Gerando");

				if (stGerando != null && stGerando.equals("1")) {
					throw new MensagemException("Relat�rio em processamento, aguarde algums minutos e tente novamente.");
				}

				long dataRelatorio = Funcoes.StringToLong((String) request.getServletContext().getAttribute("Data_Relatorio_Intimacoes"), 0);

				if (System.currentTimeMillis() > dataRelatorio) {
					request.getServletContext().setAttribute("Relatorio_Intimacoes_Gerando", "1");
					byTemp = new PendenciaNe().listarIntimacoesDiaAnterior();

					Calendar cla = Calendar.getInstance();
					cla.add(Calendar.DAY_OF_YEAR, 1);
					cla.set(Calendar.HOUR_OF_DAY, 0);
					cla.set(Calendar.MINUTE, 0);
					cla.set(Calendar.SECOND, 0);

					request.getServletContext().setAttribute("Relatorio_Intimacoes", byTemp);
					request.getServletContext().setAttribute("Data_Relatorio_Intimacoes", String.valueOf(cla.getTimeInMillis()));
					request.getServletContext().setAttribute("Relatorio_Intimacoes_Gerando", "0");
				} else {
					byTemp = (byte[]) request.getServletContext().getAttribute("Relatorio_Intimacoes");
				}

				// se byTemp for null deve retornar msg de erro de relat�rio vazio para o usu�rio
				if (byTemp != null) {
					enviarTXT(response, byTemp, "ListaIntimacoes");
				} else {
					throw new MensagemException("Houve uma falha na gera��o do arquivo. Favor entrar em contato com o suporte.");
				}
				return;

		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	}

	@Override
	public int Permissao() {
		return 871;
	}

	// este m�todo deve ser sobrescrito pelos ct_publicos retornando o id do grupo publico
	protected String getId_GrupoPublico() {
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
}
