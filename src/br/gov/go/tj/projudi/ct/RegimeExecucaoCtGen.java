package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.ne.RegimeExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RegimeExecucaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 493755031649908256L;

    public  RegimeExecucaoCtGen() {

	} 
		public int Permissao(){
			return RegimeExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RegimeExecucaoDt RegimeExecucaodt;
		RegimeExecucaoNe RegimeExecucaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/RegimeExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","RegimeExecucao");




		RegimeExecucaone =(RegimeExecucaoNe)request.getSession().getAttribute("RegimeExecucaone");
		if (RegimeExecucaone == null )  RegimeExecucaone = new RegimeExecucaoNe();  


		RegimeExecucaodt =(RegimeExecucaoDt)request.getSession().getAttribute("RegimeExecucaodt");
		if (RegimeExecucaodt == null )  RegimeExecucaodt = new RegimeExecucaoDt();  

		RegimeExecucaodt.setRegimeExecucao( request.getParameter("RegimeExecucao")); 
		RegimeExecucaodt.setId_PenaExecucaoTipo( request.getParameter("Id_PenaExecucaoTipo")); 
		RegimeExecucaodt.setPenaExecucaoTipo( request.getParameter("PenaExecucaoTipo")); 

		RegimeExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RegimeExecucaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					RegimeExecucaone.excluir(RegimeExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/RegimeExecucaoLocalizar.jsp";
				request.setAttribute("tempBuscaId_RegimeExecucao","Id_RegimeExecucao");
				request.setAttribute("tempBuscaRegimeExecucao","RegimeExecucao");
				request.setAttribute("tempRetorno","RegimeExecucao");
				tempList =RegimeExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaRegimeExecucao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", RegimeExecucaone.getQuantidadePaginas());
					RegimeExecucaodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					stAcao="/WEB-INF/jsptjgo/RegimeExecucao.jsp";
				}
				break;
			case Configuracao.Novo: 
				RegimeExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=RegimeExecucaone.Verificar(RegimeExecucaodt); 
					if (Mensagem.length()==0){
						RegimeExecucaone.salvar(RegimeExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (PenaExecucaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_PenaExecucaoTipo","Id_PenaExecucaoTipo");
					request.setAttribute("tempBuscaPenaExecucaoTipo","PenaExecucaoTipo");
					request.setAttribute("tempRetorno","RegimeExecucao");
					stAcao="/WEB-INF/jsptjgo/PenaExecucaoTipoLocalizar.jsp";
					tempList =RegimeExecucaone.consultarDescricaoPenaExecucaoTipo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaPenaExecucaoTipo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", RegimeExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_RegimeExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( RegimeExecucaodt.getId()))){
						RegimeExecucaodt.limpar();
						RegimeExecucaodt = RegimeExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("RegimeExecucaodt",RegimeExecucaodt );
		request.getSession().setAttribute("RegimeExecucaone",RegimeExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
