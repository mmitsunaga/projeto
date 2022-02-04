package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.ne.PenaExecucaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PenaExecucaoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1082354771800446060L;

    public  PenaExecucaoTipoCtGen() {

	} 
		public int Permissao(){
			return PenaExecucaoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PenaExecucaoTipoDt PenaExecucaoTipodt;
		PenaExecucaoTipoNe PenaExecucaoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PenaExecucaoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PenaExecucaoTipo");




		PenaExecucaoTipone =(PenaExecucaoTipoNe)request.getSession().getAttribute("PenaExecucaoTipone");
		if (PenaExecucaoTipone == null )  PenaExecucaoTipone = new PenaExecucaoTipoNe();  


		PenaExecucaoTipodt =(PenaExecucaoTipoDt)request.getSession().getAttribute("PenaExecucaoTipodt");
		if (PenaExecucaoTipodt == null )  PenaExecucaoTipodt = new PenaExecucaoTipoDt();  

		PenaExecucaoTipodt.setPenaExecucaoTipo( request.getParameter("PenaExecucaoTipo")); 

		PenaExecucaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PenaExecucaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PenaExecucaoTipone.excluir(PenaExecucaoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/PenaExecucaoTipoLocalizar.jsp";
				request.setAttribute("tempBuscaId_PenaExecucaoTipo","Id_PenaExecucaoTipo");
				request.setAttribute("tempBuscaPenaExecucaoTipo","PenaExecucaoTipo");
				request.setAttribute("tempRetorno","PenaExecucaoTipo");
				tempList =PenaExecucaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaPenaExecucaoTipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", PenaExecucaoTipone.getQuantidadePaginas());
					PenaExecucaoTipodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					stAcao="/WEB-INF/jsptjgo/PenaExecucaoTipo.jsp";
				}
				break;
			case Configuracao.Novo: 
				PenaExecucaoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PenaExecucaoTipone.Verificar(PenaExecucaoTipodt); 
					if (Mensagem.length()==0){
						PenaExecucaoTipone.salvar(PenaExecucaoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PenaExecucaoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PenaExecucaoTipodt.getId()))){
						PenaExecucaoTipodt.limpar();
						PenaExecucaoTipodt = PenaExecucaoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PenaExecucaoTipodt",PenaExecucaoTipodt );
		request.getSession().setAttribute("PenaExecucaoTipone",PenaExecucaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
