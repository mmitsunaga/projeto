package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ne.ProcessoParteDebitoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteDebitoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 3549729879088685806L;

    public  ProcessoParteDebitoCtGen() {

	} 
		public int Permissao(){
			return ProcessoParteDebitoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteDebitoDt ProcessoParteDebitodt;
		ProcessoParteDebitoNe ProcessoParteDebitone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoParteDebito.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoParteDebito");




		ProcessoParteDebitone =(ProcessoParteDebitoNe)request.getSession().getAttribute("ProcessoParteDebitone");
		if (ProcessoParteDebitone == null )  ProcessoParteDebitone = new ProcessoParteDebitoNe();  


		ProcessoParteDebitodt =(ProcessoParteDebitoDt)request.getSession().getAttribute("ProcessoParteDebitodt");
		if (ProcessoParteDebitodt == null )  ProcessoParteDebitodt = new ProcessoParteDebitoDt();  

		ProcessoParteDebitodt.setId_ProcessoDebito( request.getParameter("Id_ProcessoDebito")); 
		ProcessoParteDebitodt.setProcessoDebito( request.getParameter("ProcessoDebito")); 
		ProcessoParteDebitodt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte")); 
		ProcessoParteDebitodt.setNome( request.getParameter("Nome")); 

		ProcessoParteDebitodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteDebitodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoParteDebitone.excluir(ProcessoParteDebitodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoParteDebitoLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoParteDebito","Id_ProcessoParteDebito");
				request.setAttribute("tempBuscaProcessoParteDebito","ProcessoParteDebito");
				request.setAttribute("tempRetorno","ProcessoParteDebito");
				tempList =ProcessoParteDebitone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoParteDebito", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoParteDebitone.getQuantidadePaginas());
					ProcessoParteDebitodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoParteDebitodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoParteDebitone.Verificar(ProcessoParteDebitodt); 
					if (Mensagem.length()==0){
						ProcessoParteDebitone.salvar(ProcessoParteDebitodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ProcessoDebitoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoDebito","Id_ProcessoDebito");
					request.setAttribute("tempBuscaProcessoDebito","ProcessoDebito");
					request.setAttribute("tempRetorno","ProcessoParteDebito");
					stAcao="/WEB-INF/jsptjgo/ProcessoDebitoLocalizar.jsp";
					tempList =ProcessoParteDebitone.consultarDescricaoProcessoDebito(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoDebito", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoParteDebitone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoParte","Id_ProcessoParte");
					request.setAttribute("tempBuscaProcessoParte","ProcessoParte");
					request.setAttribute("tempRetorno","ProcessoParteDebito");
					stAcao="/WEB-INF/jsptjgo/ProcessoParteLocalizar.jsp";
					tempList =ProcessoParteDebitone.consultarDescricaoProcessoParte(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoParte", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoParteDebitone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoParteDebito");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoParteDebitodt.getId()))){
						ProcessoParteDebitodt.limpar();
						ProcessoParteDebitodt = ProcessoParteDebitone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoParteDebitodt",ProcessoParteDebitodt );
		request.getSession().setAttribute("ProcessoParteDebitone",ProcessoParteDebitone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
