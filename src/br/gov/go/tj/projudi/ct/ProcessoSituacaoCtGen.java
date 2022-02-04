package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoSituacaoDt;
import br.gov.go.tj.projudi.ne.ProcessoSituacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoSituacaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -2145865481068062115L;

    public  ProcessoSituacaoCtGen() {

	} 
		public int Permissao(){
			return ProcessoSituacaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoSituacaoDt ProcessoSituacaodt;
		ProcessoSituacaoNe ProcessoSituacaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoSituacao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoSituacao");
		request.setAttribute("tempBuscaId_ProcessoSituacao","Id_ProcessoSituacao");
		request.setAttribute("tempBuscaProcessoSituacao","ProcessoSituacao");
		

		request.setAttribute("tempRetorno","ProcessoSituacao");



		ProcessoSituacaone =(ProcessoSituacaoNe)request.getSession().getAttribute("ProcessoSituacaone");
		if (ProcessoSituacaone == null )  ProcessoSituacaone = new ProcessoSituacaoNe();  


		ProcessoSituacaodt =(ProcessoSituacaoDt)request.getSession().getAttribute("ProcessoSituacaodt");
		if (ProcessoSituacaodt == null )  ProcessoSituacaodt = new ProcessoSituacaoDt();  

		ProcessoSituacaodt.setProcessoSituacao( request.getParameter("ProcessoSituacao")); 
		ProcessoSituacaodt.setProcessoSituacaoCodigo( request.getParameter("ProcessoSituacaoCodigo")); 

		ProcessoSituacaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoSituacaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ProcessoSituacaone.excluir(ProcessoSituacaodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoSituacaoLocalizar.jsp";
				tempList =ProcessoSituacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoSituacao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoSituacaone.getQuantidadePaginas());
					ProcessoSituacaodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				ProcessoSituacaodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=ProcessoSituacaone.Verificar(ProcessoSituacaodt); 
				if (Mensagem.length()==0){
					ProcessoSituacaone.salvar(ProcessoSituacaodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ProcessoSituacao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoSituacaodt.getId()))){
						ProcessoSituacaodt.limpar();
						ProcessoSituacaodt = ProcessoSituacaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoSituacaodt",ProcessoSituacaodt );
		request.getSession().setAttribute("ProcessoSituacaone",ProcessoSituacaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
