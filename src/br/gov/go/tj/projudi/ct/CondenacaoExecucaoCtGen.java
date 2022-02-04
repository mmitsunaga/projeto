package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.ne.CondenacaoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CondenacaoExecucaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -7110201558818049292L;

    public  CondenacaoExecucaoCtGen() {

	} 
		public int Permissao(){
			return CondenacaoExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CondenacaoExecucaoDt CondenacaoExecucaodt;
		CondenacaoExecucaoNe CondenacaoExecucaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/CondenacaoExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","CondenacaoExecucao");




		CondenacaoExecucaone =(CondenacaoExecucaoNe)request.getSession().getAttribute("CondenacaoExecucaone");
		if (CondenacaoExecucaone == null )  CondenacaoExecucaone = new CondenacaoExecucaoNe();  


		CondenacaoExecucaodt =(CondenacaoExecucaoDt)request.getSession().getAttribute("CondenacaoExecucaodt");
		if (CondenacaoExecucaodt == null )  CondenacaoExecucaodt = new CondenacaoExecucaoDt();  

		CondenacaoExecucaodt.setTempoPena( request.getParameter("TempoPena")); 
		if (request.getParameter("Reincidente") != null)
			CondenacaoExecucaodt.setReincidente( request.getParameter("Reincidente")); 
		else CondenacaoExecucaodt.setReincidente("false");
		CondenacaoExecucaodt.setDataFato( request.getParameter("DataFato")); 
		CondenacaoExecucaodt.setId_ProcessoExecucao( request.getParameter("Id_ProcessoExecucao")); 
		CondenacaoExecucaodt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		CondenacaoExecucaodt.setId_CrimeExecucao( request.getParameter("Id_CrimeExecucao")); 
		CondenacaoExecucaodt.setCrimeExecucao( request.getParameter("CrimeExecucao")); 

		CondenacaoExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CondenacaoExecucaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					CondenacaoExecucaone.excluir(CondenacaoExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/CondenacaoExecucaoLocalizar.jsp";
				request.setAttribute("tempBuscaId_CondenacaoExecucao","Id_CondenacaoExecucao");
				request.setAttribute("tempBuscaCondenacaoExecucao","CondenacaoExecucao");
				request.setAttribute("tempRetorno","CondenacaoExecucao");
				tempList =CondenacaoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaCondenacaoExecucao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", CondenacaoExecucaone.getQuantidadePaginas());
					CondenacaoExecucaodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				CondenacaoExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=CondenacaoExecucaone.Verificar(CondenacaoExecucaodt); 
					if (Mensagem.length()==0){
						CondenacaoExecucaone.salvar(CondenacaoExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ProcessoExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoExecucao","Id_ProcessoExecucao");
					request.setAttribute("tempBuscaProcessoExecucao","ProcessoExecucao");
					request.setAttribute("tempRetorno","CondenacaoExecucao");
					stAcao="/WEB-INF/jsptjgo/ProcessoExecucaoLocalizar.jsp";
					tempList =CondenacaoExecucaone.consultarDescricaoProcessoExecucao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoExecucao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", CondenacaoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (CrimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_CrimeExecucao","Id_CrimeExecucao");
					request.setAttribute("tempBuscaCrimeExecucao","CrimeExecucao");
					request.setAttribute("tempRetorno","CondenacaoExecucao");
					stAcao="/WEB-INF/jsptjgo/CrimeExecucaoLocalizar.jsp";
					tempList =CondenacaoExecucaone.consultarDescricaoCrimeExecucao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaCrimeExecucao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", CondenacaoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_CondenacaoExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( CondenacaoExecucaodt.getId()))){
						CondenacaoExecucaodt.limpar();
						CondenacaoExecucaodt = CondenacaoExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("CondenacaoExecucaodt",CondenacaoExecucaodt );
		request.getSession().setAttribute("CondenacaoExecucaone",CondenacaoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
