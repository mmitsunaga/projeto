package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.GrupoMovimentacaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GrupoMovimentacaoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -5018433024799785927L;

    public  GrupoMovimentacaoTipoCtGen() {

	} 
		public int Permissao(){
			return GrupoMovimentacaoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoMovimentacaoTipoDt GrupoMovimentacaoTipodt;
		GrupoMovimentacaoTipoNe GrupoMovimentacaoTipone;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/GrupoMovimentacaoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GrupoMovimentacaoTipo");
		request.setAttribute("tempBuscaId_GrupoMovimentacaoTipo","Id_GrupoMovimentacaoTipo");
		request.setAttribute("tempBuscaGrupoMovimentacaoTipo","GrupoMovimentacaoTipo");
		request.setAttribute("ListaUlLiGrupoMovimentacaoTipo","");
		request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
		request.setAttribute("tempBuscaGrupo","Grupo");
		request.setAttribute("tempBuscaId_MovimentacaoTipo","Id_MovimentacaoTipo");
		request.setAttribute("tempBuscaMovimentacaoTipo","MovimentacaoTipo");

		request.setAttribute("tempRetorno","GrupoMovimentacaoTipo");



		GrupoMovimentacaoTipone =(GrupoMovimentacaoTipoNe)request.getSession().getAttribute("GrupoMovimentacaoTipone");
		if (GrupoMovimentacaoTipone == null )  GrupoMovimentacaoTipone = new GrupoMovimentacaoTipoNe();  


		GrupoMovimentacaoTipodt =(GrupoMovimentacaoTipoDt)request.getSession().getAttribute("GrupoMovimentacaoTipodt");
		if (GrupoMovimentacaoTipodt == null )  GrupoMovimentacaoTipodt = new GrupoMovimentacaoTipoDt();  

		GrupoMovimentacaoTipodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		GrupoMovimentacaoTipodt.setGrupo( request.getParameter("Grupo")); 
		GrupoMovimentacaoTipodt.setId_MovimentacaoTipo( request.getParameter("Id_MovimentacaoTipo")); 
		GrupoMovimentacaoTipodt.setMovimentacaoTipo( request.getParameter("MovimentacaoTipo")); 
		GrupoMovimentacaoTipodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		GrupoMovimentacaoTipodt.setMovimentacaoTipoCodigo( request.getParameter("MovimentacaoTipoCodigo")); 

		GrupoMovimentacaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoMovimentacaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				localizar(request,GrupoMovimentacaoTipodt.getId_Grupo() ,GrupoMovimentacaoTipone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				GrupoMovimentacaoTipodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				request.setAttribute("ListaUlLiGrupoMovimentacaoTipo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=GrupoMovimentacaoTipone.Verificar(GrupoMovimentacaoTipodt); 
				if (Mensagem.length()==0){
					GrupoMovimentacaoTipone.salvarMultiplo(GrupoMovimentacaoTipodt, idsDados); 
					localizar(request,GrupoMovimentacaoTipodt.getId_Grupo() ,GrupoMovimentacaoTipone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				stAcao="/WEB-INF/jsptjgo/GrupoLocalizar.jsp";
				tempList =GrupoMovimentacaoTipone.consultarDescricaoGrupo(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGrupo", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", GrupoMovimentacaoTipone.getQuantidadePaginas());
					request.setAttribute("tempRetorno","GrupoMovimentacaoTipo?PaginaAtual="+ Configuracao.Localizar );
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

		request.getSession().setAttribute("GrupoMovimentacaoTipodt",GrupoMovimentacaoTipodt );
		request.getSession().setAttribute("GrupoMovimentacaoTipone",GrupoMovimentacaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, GrupoMovimentacaoTipoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarMovimentacaoTipoGrupoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiGrupoMovimentacaoTipo", tempDados);
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
                    request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
