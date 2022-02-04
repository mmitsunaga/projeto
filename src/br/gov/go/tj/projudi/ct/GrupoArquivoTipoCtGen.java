package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.GrupoArquivoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GrupoArquivoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 753165105872311988L;

    public  GrupoArquivoTipoCtGen() {

	} 
		public int Permissao(){
			return GrupoArquivoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoArquivoTipoDt GrupoArquivoTipodt;
		GrupoArquivoTipoNe GrupoArquivoTipone;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/GrupoArquivoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GrupoArquivoTipo");
		request.setAttribute("tempBuscaId_GrupoArquivoTipo","Id_GrupoArquivoTipo");
		request.setAttribute("tempBuscaGrupoArquivoTipo","GrupoArquivoTipo");
		request.setAttribute("ListaUlLiGrupoArquivoTipo","");
		request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
		request.setAttribute("tempBuscaGrupo","Grupo");
		request.setAttribute("tempBuscaId_ArquivoTipo","Id_ArquivoTipo");
		request.setAttribute("tempBuscaArquivoTipo","ArquivoTipo");

		request.setAttribute("tempRetorno","GrupoArquivoTipo");



		GrupoArquivoTipone =(GrupoArquivoTipoNe)request.getSession().getAttribute("GrupoArquivoTipone");
		if (GrupoArquivoTipone == null )  GrupoArquivoTipone = new GrupoArquivoTipoNe();  


		GrupoArquivoTipodt =(GrupoArquivoTipoDt)request.getSession().getAttribute("GrupoArquivoTipodt");
		if (GrupoArquivoTipodt == null )  GrupoArquivoTipodt = new GrupoArquivoTipoDt();  

		GrupoArquivoTipodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		GrupoArquivoTipodt.setGrupo( request.getParameter("Grupo")); 
		GrupoArquivoTipodt.setId_ArquivoTipo( request.getParameter("Id_ArquivoTipo")); 
		GrupoArquivoTipodt.setArquivoTipo( request.getParameter("ArquivoTipo")); 
		GrupoArquivoTipodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		GrupoArquivoTipodt.setArquivoTipoCodigo( request.getParameter("ArquivoTipoCodigo")); 

		GrupoArquivoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoArquivoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				localizar(request,GrupoArquivoTipodt.getId_Grupo() ,GrupoArquivoTipone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				GrupoArquivoTipodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				request.setAttribute("ListaUlLiGrupoArquivoTipo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=GrupoArquivoTipone.Verificar(GrupoArquivoTipodt); 
				if (Mensagem.length()==0){
					GrupoArquivoTipone.salvarMultiplo(GrupoArquivoTipodt, idsDados); 
					localizar(request,GrupoArquivoTipodt.getId_Grupo() ,GrupoArquivoTipone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				stAcao="/WEB-INF/jsptjgo/GrupoLocalizar.jsp";
				tempList =GrupoArquivoTipone.consultarDescricaoGrupo(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGrupo", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", GrupoArquivoTipone.getQuantidadePaginas());
					request.setAttribute("tempRetorno","GrupoArquivoTipo?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
		}

		request.getSession().setAttribute("GrupoArquivoTipodt",GrupoArquivoTipodt );
		request.getSession().setAttribute("GrupoArquivoTipone",GrupoArquivoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, GrupoArquivoTipoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarArquivoTipoGrupoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiGrupoArquivoTipo", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
                    request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
