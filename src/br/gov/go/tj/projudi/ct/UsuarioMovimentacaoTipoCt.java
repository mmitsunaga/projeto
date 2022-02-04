package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioMovimentacaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class UsuarioMovimentacaoTipoCt extends Controle {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889670395675651947L;

	public  UsuarioMovimentacaoTipoCt() {

	} 
	public int Permissao(){
		return UsuarioMovimentacaoTipoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioMovimentacaoTipoNe usuarioMovimentacaoTipoNe;

		String stAcao="/WEB-INF/jsptjgo/UsuarioMovimentacaoTipo.jsp";	
		
		usuarioMovimentacaoTipoNe =(UsuarioMovimentacaoTipoNe)request.getSession().getAttribute("usuarioMovimentacaoTipoNe");
		if (usuarioMovimentacaoTipoNe == null )  usuarioMovimentacaoTipoNe = new UsuarioMovimentacaoTipoNe();
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("ListaUlLiUsuarioMovimentacaoTipo", "");

		if (paginaatual == Configuracao.SalvarResultado)
		{
			String[] idsDados = request.getParameterValues("chkEditar");
			String Mensagem = usuarioMovimentacaoTipoNe.Verificar(idsDados); 
			if (Mensagem.length()==0){
				usuarioMovimentacaoTipoNe.salvarMultiplo(UsuarioSessao.getUsuarioDt(), idsDados);					
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
			}else {
				request.setAttribute("MensagemErro", Mensagem );
			}				
		}
		
		executeAcaoEditar(request, usuarioMovimentacaoTipoNe, UsuarioSessao);

		request.getSession().setAttribute("usuarioMovimentacaoTipoNe", usuarioMovimentacaoTipoNe);
		
		//é gerado o código do pedido, assim o submit só pode ser executado uma unica vez
        request.setAttribute("__Pedido__", UsuarioSessao.getPedido());

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private void executeAcaoEditar(HttpServletRequest request, UsuarioMovimentacaoTipoNe usuarioMovimentacaoTipoNe, UsuarioNe UsuarioSessao) throws Exception{
		usuarioMovimentacaoTipoNe.limpar();
		request.setAttribute("PaginaAtual",Configuracao.Editar);				
		localizar(request, usuarioMovimentacaoTipoNe, UsuarioSessao);
	}

	private void localizar(HttpServletRequest request, UsuarioMovimentacaoTipoNe usuarioMovimentacaoTipoNe, UsuarioNe UsuarioSessao) throws Exception{
		String tempDados = usuarioMovimentacaoTipoNe.consultarMovimentacaoTipoGrupoUlLiCheckBox(UsuarioSessao.getUsuarioDt());
		if (tempDados.length()>0){
			request.setAttribute("ListaUlLiUsuarioMovimentacaoTipo", tempDados);			
		}else{ 
			request.setAttribute("MensagemErro", "Dados Não Localizados"); 
			request.setAttribute("PaginaAtual", Configuracao.Editar);
		}		
	}
}
