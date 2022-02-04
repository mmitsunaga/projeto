package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoProcessoSubtipoDt;
import br.gov.go.tj.projudi.ne.ProcessoTipoProcessoSubtipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoTipoProcessoSubtipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -397222376317344212L;

    public  ProcessoTipoProcessoSubtipoCtGen() {

	} 
		public int Permissao(){
			return ProcessoTipoProcessoSubtipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoTipoProcessoSubtipoDt ProcessoTipoProcessoSubtipodt;
		ProcessoTipoProcessoSubtipoNe ProcessoTipoProcessoSubtipone;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/ProcessoTipoProcessoSubtipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoTipoProcessoSubtipo");
		request.setAttribute("ListaUlLiProcessoTipoProcessoSubtipo","");




		ProcessoTipoProcessoSubtipone =(ProcessoTipoProcessoSubtipoNe)request.getSession().getAttribute("ProcessoTipoProcessoSubtipone");
		if (ProcessoTipoProcessoSubtipone == null )  ProcessoTipoProcessoSubtipone = new ProcessoTipoProcessoSubtipoNe();  


		ProcessoTipoProcessoSubtipodt =(ProcessoTipoProcessoSubtipoDt)request.getSession().getAttribute("ProcessoTipoProcessoSubtipodt");
		if (ProcessoTipoProcessoSubtipodt == null )  ProcessoTipoProcessoSubtipodt = new ProcessoTipoProcessoSubtipoDt();  

		ProcessoTipoProcessoSubtipodt.setId_ProcessoSubtipo( request.getParameter("Id_ProcessoSubtipo")); 
		ProcessoTipoProcessoSubtipodt.setProcessoSubtipo( request.getParameter("ProcessoSubtipo")); 
		ProcessoTipoProcessoSubtipodt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		ProcessoTipoProcessoSubtipodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 

		ProcessoTipoProcessoSubtipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoTipoProcessoSubtipodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,ProcessoTipoProcessoSubtipodt.getId_ProcessoSubtipo() ,ProcessoTipoProcessoSubtipone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				ProcessoTipoProcessoSubtipodt.limpar();
				request.setAttribute("ListaUlLiProcessoTipoProcessoSubtipo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=ProcessoTipoProcessoSubtipone.Verificar(ProcessoTipoProcessoSubtipodt); 
				if (Mensagem.length()==0){
					ProcessoTipoProcessoSubtipone.salvarMultiplo(ProcessoTipoProcessoSubtipodt, idsDados); 
					localizar(request,ProcessoTipoProcessoSubtipodt.getId_ProcessoSubtipo() ,ProcessoTipoProcessoSubtipone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (ProcessoSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				request.setAttribute("tempBuscaId_ProcessoSubtipo","Id_ProcessoSubtipo");
				request.setAttribute("tempBuscaProcessoSubtipo","ProcessoSubtipo");
				request.setAttribute("tempRetorno","ProcessoTipoProcessoSubtipo");
				stAcao="/WEB-INF/jsptjgo/ProcessoSubtipoLocalizar.jsp";
				tempList =ProcessoTipoProcessoSubtipone.consultarDescricaoProcessoSubtipo(tempNomeBusca, PosicaoPaginaAtual);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", ProcessoTipoProcessoSubtipone.getQuantidadePaginas());
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoSubtipo", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("tempRetorno","ProcessoTipoProcessoSubtipo?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("ProcessoTipoProcessoSubtipodt",ProcessoTipoProcessoSubtipodt );
		request.getSession().setAttribute("ProcessoTipoProcessoSubtipone",ProcessoTipoProcessoSubtipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	protected void localizar(HttpServletRequest request, String id, ProcessoTipoProcessoSubtipoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarProcessoTipoProcessoSubtipoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiProcessoTipoProcessoSubtipo", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
