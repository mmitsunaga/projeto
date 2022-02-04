package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.ne.ArquivoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ArquivoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 5988412358182215138L;

    public  ArquivoTipoCtGen() {

	} 
		public int Permissao(){
			return ArquivoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ArquivoTipoDt ArquivoTipodt;
		ArquivoTipoNe ArquivoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ArquivoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ArquivoTipo");




		ArquivoTipone =(ArquivoTipoNe)request.getSession().getAttribute("ArquivoTipone");
		if (ArquivoTipone == null )  ArquivoTipone = new ArquivoTipoNe();  


		ArquivoTipodt =(ArquivoTipoDt)request.getSession().getAttribute("ArquivoTipodt");
		if (ArquivoTipodt == null )  ArquivoTipodt = new ArquivoTipoDt();  

		ArquivoTipodt.setArquivoTipo( request.getParameter("ArquivoTipo")); 
		ArquivoTipodt.setArquivoTipoCodigo( request.getParameter("ArquivoTipoCodigo")); 
		if (request.getParameter("Publico") != null)
			ArquivoTipodt.setPublico( request.getParameter("Publico")); 
		else ArquivoTipodt.setPublico("false");

		ArquivoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ArquivoTipodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ArquivoTipone.excluir(ArquivoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				ArquivoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ArquivoTipone.Verificar(ArquivoTipodt); 
					if (Mensagem.length()==0){
						ArquivoTipone.salvar(ArquivoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ArquivoTipo");
				if (stId != null && !stId.isEmpty())
					if (!stId.equalsIgnoreCase("")){
						ArquivoTipodt.limpar();
						ArquivoTipodt = ArquivoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ArquivoTipodt",ArquivoTipodt );
		request.getSession().setAttribute("ArquivoTipone",ArquivoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
