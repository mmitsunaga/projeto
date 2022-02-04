package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoAssuntoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaSubtipoAssuntoCtGen extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 390778972153240600L;

	public ServentiaSubtipoAssuntoCtGen() {

	}

	public int Permissao() {
		return ServentiaSubtipoAssuntoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaSubtipoAssuntoDt ServentiaSubtipoAssuntodt;
		ServentiaSubtipoAssuntoNe ServentiaSubtipoAssuntone;

		List tempList = null;
		String Mensagem = "";
		String stAcao = "/WEB-INF/jsptjgo/ServentiaSubtipoAssunto.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "ServentiaSubtipoAssunto");
		request.setAttribute("ListaUlLiServentiaSubtipoAssunto", "");

		ServentiaSubtipoAssuntone = (ServentiaSubtipoAssuntoNe) request.getSession().getAttribute("ServentiaSubtipoAssuntone");
		if (ServentiaSubtipoAssuntone == null) ServentiaSubtipoAssuntone = new ServentiaSubtipoAssuntoNe();

		ServentiaSubtipoAssuntodt = (ServentiaSubtipoAssuntoDt) request.getSession().getAttribute("ServentiaSubtipoAssuntodt");
		if (ServentiaSubtipoAssuntodt == null) ServentiaSubtipoAssuntodt = new ServentiaSubtipoAssuntoDt();

		ServentiaSubtipoAssuntodt.setId_ServentiaSubtipo(request.getParameter("Id_ServentiaSubtipo"));
		ServentiaSubtipoAssuntodt.setServentiaSubtipo(request.getParameter("ServentiaSubtipo"));
		ServentiaSubtipoAssuntodt.setId_Assunto(request.getParameter("Id_Assunto"));
		ServentiaSubtipoAssuntodt.setAssunto(request.getParameter("Assunto"));
		ServentiaSubtipoAssuntodt.setServentiaSubtipoCodigo(request.getParameter("ServentiaSubtipoCodigo"));
		ServentiaSubtipoAssuntodt.setAssuntoCodigo(request.getParameter("AssuntoCodigo"));

		ServentiaSubtipoAssuntodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaSubtipoAssuntodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual", Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request, ServentiaSubtipoAssuntodt.getId_ServentiaSubtipo(), ServentiaSubtipoAssuntone, UsuarioSessao);
				break;
			case Configuracao.Novo:
				ServentiaSubtipoAssuntodt.limpar();
				request.setAttribute("ListaUlLiServentiaSubtipoAssunto", "");
				break;
			case Configuracao.SalvarResultado:
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem = ServentiaSubtipoAssuntone.Verificar(ServentiaSubtipoAssuntodt);
				if (Mensagem.length() == 0) {
					ServentiaSubtipoAssuntone.salvarMultiplo(ServentiaSubtipoAssuntodt, idsDados);
					localizar(request, ServentiaSubtipoAssuntodt.getId_ServentiaSubtipo(), ServentiaSubtipoAssuntone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else request.setAttribute("MensagemErro", Mensagem);
				break;
			case (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				request.setAttribute("tempBuscaId_ServentiaSubtipo", "Id_ServentiaSubtipo");
				request.setAttribute("tempBuscaServentiaSubtipo", "ServentiaSubtipo");
				request.setAttribute("tempRetorno", "ServentiaSubtipoAssunto");
				stAcao = "/WEB-INF/jsptjgo/ServentiaSubtipoLocalizar.jsp";
				tempList = ServentiaSubtipoAssuntone.consultarDescricaoServentiaSubtipo(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size() > 0) {
					request.setAttribute("ListaServentiaSubtipo", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ServentiaSubtipoAssuntone.getQuantidadePaginas());
					request.setAttribute("tempRetorno", "ServentiaSubtipoAssunto?PaginaAtual=" + Configuracao.Localizar);
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("ServentiaSubtipoAssuntodt", ServentiaSubtipoAssuntodt);
		request.getSession().setAttribute("ServentiaSubtipoAssuntone", ServentiaSubtipoAssuntone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, ServentiaSubtipoAssuntoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
		String tempDados = objNe.consultarAssuntoServentiaSubtipoUlLiCheckBox(id);
		if (tempDados.length() > 0) {
			request.setAttribute("ListaUlLiServentiaSubtipoAssunto", tempDados);
			//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
			request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
		} else {
			request.setAttribute("MensagemErro", "Dados Não Localizados");
			request.setAttribute("PaginaAtual", Configuracao.Editar);
		}
	}
}
