package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoProcessoTipoDt;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaSubtipoProcessoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 4542289521752273035L;

    public  ServentiaSubtipoProcessoTipoCtGen() {

	} 
		public int Permissao(){
			return ServentiaSubtipoProcessoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaSubtipoProcessoTipoDt ServentiaSubtipoProcessoTipodt;
		ServentiaSubtipoProcessoTipoNe ServentiaSubtipoProcessoTipone;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/ServentiaSubtipoProcessoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ServentiaSubtipoProcessoTipo");
		request.setAttribute("ListaUlLiServentiaSubtipoProcessoTipo","");




		ServentiaSubtipoProcessoTipone =(ServentiaSubtipoProcessoTipoNe)request.getSession().getAttribute("ServentiaSubtipoProcessoTipone");
		if (ServentiaSubtipoProcessoTipone == null )  ServentiaSubtipoProcessoTipone = new ServentiaSubtipoProcessoTipoNe();  


		ServentiaSubtipoProcessoTipodt =(ServentiaSubtipoProcessoTipoDt)request.getSession().getAttribute("ServentiaSubtipoProcessoTipodt");
		if (ServentiaSubtipoProcessoTipodt == null )  ServentiaSubtipoProcessoTipodt = new ServentiaSubtipoProcessoTipoDt();  

		ServentiaSubtipoProcessoTipodt.setId_ServentiaSubtipo( request.getParameter("Id_ServentiaSubtipo")); 
		ServentiaSubtipoProcessoTipodt.setServentiaSubtipo( request.getParameter("ServentiaSubtipo")); 
		ServentiaSubtipoProcessoTipodt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		ServentiaSubtipoProcessoTipodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 
		ServentiaSubtipoProcessoTipodt.setServentiaSubtipoCodigo( request.getParameter("ServentiaSubtipoCodigo")); 
		ServentiaSubtipoProcessoTipodt.setProcessoTipoCodigo( request.getParameter("ProcessoTipoCodigo")); 

		ServentiaSubtipoProcessoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaSubtipoProcessoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,ServentiaSubtipoProcessoTipodt.getId_ServentiaSubtipo() ,ServentiaSubtipoProcessoTipone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				ServentiaSubtipoProcessoTipodt.limpar();
				request.setAttribute("ListaUlLiServentiaSubtipoProcessoTipo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=ServentiaSubtipoProcessoTipone.Verificar(ServentiaSubtipoProcessoTipodt); 
				if (Mensagem.length()==0){
					ServentiaSubtipoProcessoTipone.salvarMultiplo(ServentiaSubtipoProcessoTipodt, idsDados); 
					localizar(request,ServentiaSubtipoProcessoTipodt.getId_ServentiaSubtipo() ,ServentiaSubtipoProcessoTipone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (ServentiaSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				request.setAttribute("tempBuscaId_ServentiaSubtipo","Id_ServentiaSubtipo");
				request.setAttribute("tempBuscaServentiaSubtipo","ServentiaSubtipo");
				request.setAttribute("tempRetorno","ServentiaSubtipoProcessoTipo");
				stAcao="/WEB-INF/jsptjgo/ServentiaSubtipoLocalizar.jsp";
				tempList =ServentiaSubtipoProcessoTipone.consultarDescricaoServentiaSubtipo(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaServentiaSubtipo", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ServentiaSubtipoProcessoTipone.getQuantidadePaginas());
					request.setAttribute("tempRetorno","ServentiaSubtipoProcessoTipo?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("ServentiaSubtipoProcessoTipodt",ServentiaSubtipoProcessoTipodt );
		request.getSession().setAttribute("ServentiaSubtipoProcessoTipone",ServentiaSubtipoProcessoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, ServentiaSubtipoProcessoTipoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarProcessoTipoServentiaSubtipoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiServentiaSubtipoProcessoTipo", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
