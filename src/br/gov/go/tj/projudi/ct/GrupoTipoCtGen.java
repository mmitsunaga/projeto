package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.ne.GrupoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GrupoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 5501737265044466995L;

    public  GrupoTipoCtGen() {

	} 
		public int Permissao(){
			return GrupoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoTipoDt GrupoTipodt;
		GrupoTipoNe GrupoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/GrupoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GrupoTipo");




		GrupoTipone =(GrupoTipoNe)request.getSession().getAttribute("GrupoTipone");
		if (GrupoTipone == null )  GrupoTipone = new GrupoTipoNe();  


		GrupoTipodt =(GrupoTipoDt)request.getSession().getAttribute("GrupoTipodt");
		if (GrupoTipodt == null )  GrupoTipodt = new GrupoTipoDt();  

		GrupoTipodt.setGrupoTipo( request.getParameter("GrupoTipo")); 
		GrupoTipodt.setGrupoTipoCodigo( request.getParameter("GrupoTipoCodigo")); 

		GrupoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoTipodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					GrupoTipone.excluir(GrupoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				GrupoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=GrupoTipone.Verificar(GrupoTipodt); 
					if (Mensagem.length()==0){
						GrupoTipone.salvar(GrupoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_GrupoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( GrupoTipodt.getId()))){
						GrupoTipodt.limpar();
						GrupoTipodt = GrupoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("GrupoTipodt",GrupoTipodt );
		request.getSession().setAttribute("GrupoTipone",GrupoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
