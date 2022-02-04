package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.ne.ProcessoEventoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoEventoExecucaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6043298075257511963L;

    public  ProcessoEventoExecucaoCtGen() {

	} 
		public int Permissao(){
			return ProcessoEventoExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoEventoExecucaoDt ProcessoEventoExecucaodt;
		ProcessoEventoExecucaoNe ProcessoEventoExecucaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoEventoExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoEventoExecucao");




		ProcessoEventoExecucaone =(ProcessoEventoExecucaoNe)request.getSession().getAttribute("ProcessoEventoExecucaone");
		if (ProcessoEventoExecucaone == null )  ProcessoEventoExecucaone = new ProcessoEventoExecucaoNe();  


		ProcessoEventoExecucaodt =(ProcessoEventoExecucaoDt)request.getSession().getAttribute("ProcessoEventoExecucaodt");
		if (ProcessoEventoExecucaodt == null )  ProcessoEventoExecucaodt = new ProcessoEventoExecucaoDt();  

		ProcessoEventoExecucaodt.setDataInicio( request.getParameter("DataInicio")); 
		ProcessoEventoExecucaodt.setDataFim( request.getParameter("DataFim")); 
		ProcessoEventoExecucaodt.setQuantidade( request.getParameter("Quantidade")); 
		ProcessoEventoExecucaodt.setObservacao( request.getParameter("Observacao")); 
		ProcessoEventoExecucaodt.setId_LivramentoCondicional( request.getParameter("Id_LivramentoCondicional")); 
		if (request.getParameter("ConsiderarTempoLivramentoCondicional") != null)
			ProcessoEventoExecucaodt.setConsiderarTempoLivramentoCondicional( request.getParameter("ConsiderarTempoLivramentoCondicional")); 
		else ProcessoEventoExecucaodt.setConsiderarTempoLivramentoCondicional("false");
		ProcessoEventoExecucaodt.setId_Movimentacao( request.getParameter("Id_Movimentacao")); 
		ProcessoEventoExecucaodt.setId_ProcessoExecucao( request.getParameter("Id_ProcessoExecucao")); 
		ProcessoEventoExecucaodt.setMovimentacaoTipo( request.getParameter("MovimentacaoTipo")); 
		ProcessoEventoExecucaodt.setId_EventoExecucao( request.getParameter("Id_EventoExecucao")); 
		ProcessoEventoExecucaodt.setEventoExecucao( request.getParameter("EventoExecucao")); 

		ProcessoEventoExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoEventoExecucaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoEventoExecucaone.excluir(ProcessoEventoExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoEventoExecucaoLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoEventoExecucao","Id_ProcessoEventoExecucao");
				request.setAttribute("tempBuscaProcessoEventoExecucao","ProcessoEventoExecucao");
				request.setAttribute("tempRetorno","ProcessoEventoExecucao");
				tempList =ProcessoEventoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoEventoExecucao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoEventoExecucaone.getQuantidadePaginas());
					ProcessoEventoExecucaodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoEventoExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoEventoExecucaone.Verificar(ProcessoEventoExecucaodt); 
					if (Mensagem.length()==0){
						ProcessoEventoExecucaone.salvar(ProcessoEventoExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ProcessoEventoExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_LivramentoCondicional","Id_LivramentoCondicional");
					request.setAttribute("tempBuscaProcessoEventoExecucao","ProcessoEventoExecucao");
					request.setAttribute("tempRetorno","ProcessoEventoExecucao");
					stAcao="/WEB-INF/jsptjgo/ProcessoEventoExecucaoLocalizar.jsp";
					tempList =ProcessoEventoExecucaone.consultarDescricaoProcessoEventoExecucao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoEventoExecucao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoEventoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (MovimentacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Movimentacao","Id_Movimentacao");
					request.setAttribute("tempBuscaMovimentacao","Movimentacao");
					request.setAttribute("tempRetorno","ProcessoEventoExecucao");
					stAcao="/WEB-INF/jsptjgo/MovimentacaoLocalizar.jsp";
					tempList =ProcessoEventoExecucaone.consultarDescricaoMovimentacao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaMovimentacao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoEventoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoExecucao","Id_ProcessoExecucao");
					request.setAttribute("tempBuscaProcessoExecucao","ProcessoExecucao");
					request.setAttribute("tempRetorno","ProcessoEventoExecucao");
					stAcao="/WEB-INF/jsptjgo/ProcessoExecucaoLocalizar.jsp";
					tempList =ProcessoEventoExecucaone.consultarDescricaoProcessoExecucao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoExecucao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoEventoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EventoExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_EventoExecucao","Id_EventoExecucao");
					request.setAttribute("tempBuscaEventoExecucao","EventoExecucao");
					request.setAttribute("tempRetorno","ProcessoEventoExecucao");
					stAcao="/WEB-INF/jsptjgo/EventoExecucaoLocalizar.jsp";
					tempList =ProcessoEventoExecucaone.consultarDescricaoEventoExecucao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEventoExecucao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoEventoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoEventoExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoEventoExecucaodt.getId()))){
						ProcessoEventoExecucaodt.limpar();
						ProcessoEventoExecucaodt = ProcessoEventoExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoEventoExecucaodt",ProcessoEventoExecucaodt );
		request.getSession().setAttribute("ProcessoEventoExecucaone",ProcessoEventoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
