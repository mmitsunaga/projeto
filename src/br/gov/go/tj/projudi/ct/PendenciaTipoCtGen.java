package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.ne.PendenciaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PendenciaTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -524346890493802535L;

    public  PendenciaTipoCtGen() {

	} 
		public int Permissao(){
			return PendenciaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaTipoDt PendenciaTipodt;
		PendenciaTipoNe PendenciaTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PendenciaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PendenciaTipo");




		PendenciaTipone =(PendenciaTipoNe)request.getSession().getAttribute("PendenciaTipone");
		if (PendenciaTipone == null )  PendenciaTipone = new PendenciaTipoNe();  


		PendenciaTipodt =(PendenciaTipoDt)request.getSession().getAttribute("PendenciaTipodt");
		if (PendenciaTipodt == null )  PendenciaTipodt = new PendenciaTipoDt();  

		PendenciaTipodt.setPendenciaTipo( request.getParameter("PendenciaTipo")); 
		PendenciaTipodt.setPendenciaTipoCodigo( request.getParameter("PendenciaTipoCodigo")); 
		PendenciaTipodt.setId_ArquivoTipo( request.getParameter("Id_ArquivoTipo")); 
		PendenciaTipodt.setArquivoTipo( request.getParameter("ArquivoTipo")); 

		PendenciaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PendenciaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PendenciaTipone.excluir(PendenciaTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				PendenciaTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PendenciaTipone.Verificar(PendenciaTipodt); 
					if (Mensagem.length()==0){
						PendenciaTipone.salvar(PendenciaTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PendenciaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PendenciaTipodt.getId()))){
						PendenciaTipodt.limpar();
						PendenciaTipodt = PendenciaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PendenciaTipodt",PendenciaTipodt );
		request.getSession().setAttribute("PendenciaTipone",PendenciaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
