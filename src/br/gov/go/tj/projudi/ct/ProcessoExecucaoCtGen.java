package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.ne.ProcessoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoExecucaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -3933714920285298277L;
    

    public  ProcessoExecucaoCtGen() {

	} 
		public int Permissao(){
			return ProcessoExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoExecucaoDt ProcessoExecucaodt;
		ProcessoExecucaoNe ProcessoExecucaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoExecucao");




		ProcessoExecucaone =(ProcessoExecucaoNe)request.getSession().getAttribute("ProcessoExecucaone");
		if (ProcessoExecucaone == null )  ProcessoExecucaone = new ProcessoExecucaoNe();  


		ProcessoExecucaodt =(ProcessoExecucaoDt)request.getSession().getAttribute("ProcessoExecucaodt");
		if (ProcessoExecucaodt == null )  ProcessoExecucaodt = new ProcessoExecucaoDt();  

		ProcessoExecucaodt.setId_ProcessoExecucaoPenal( request.getParameter("Id_ProcessoExecucaoPenal")); 
		ProcessoExecucaodt.setProcessoExecucaoPenalNumero( request.getParameter("ProcessoExecucaoPenalNumero")); 
		ProcessoExecucaodt.setId_ProcessoAcaoPenal( request.getParameter("Id_ProcessoAcaoPenal")); 
		ProcessoExecucaodt.setProcessoAcaoPenalNumero( request.getParameter("ProcessoAcaoPenalNumero")); 
		ProcessoExecucaodt.setId_CidadeOrigem( request.getParameter("Id_CidadeOrigem")); 
		ProcessoExecucaodt.setCidadeOrigem( request.getParameter("CidadeOrigem")); 
		ProcessoExecucaodt.setEstadoOrigem( request.getParameter("EstadoOrigem")); 
		ProcessoExecucaodt.setUfOrigem( request.getParameter("UfOrigem")); 
		ProcessoExecucaodt.setDataAcordao( request.getParameter("DataAcordao")); 
		ProcessoExecucaodt.setDataDistribuicao( request.getParameter("DataDistribuicao")); 
		ProcessoExecucaodt.setDataPronuncia( request.getParameter("DataPronuncia")); 
		ProcessoExecucaodt.setDataSentenca( request.getParameter("DataSentenca")); 
		ProcessoExecucaodt.setDataTransitoJulgado( request.getParameter("DataTransitoJulgado")); 
		ProcessoExecucaodt.setDataDenuncia( request.getParameter("DataDenuncia")); 
		ProcessoExecucaodt.setDataAdmonitoria( request.getParameter("DataAdmonitoria")); 
		ProcessoExecucaodt.setDataInicioCumprimentoPena( request.getParameter("DataInicioCumprimentoPena")); 
		ProcessoExecucaodt.setNumeroAcaoPenal( request.getParameter("NumeroAcaoPenal")); 
		ProcessoExecucaodt.setVaraOrigem( request.getParameter("VaraOrigem")); 

		ProcessoExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoExecucaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoExecucaone.excluir(ProcessoExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoExecucaoLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoExecucao","Id_ProcessoExecucao");
				request.setAttribute("tempBuscaProcessoExecucao","ProcessoExecucao");
				request.setAttribute("tempRetorno","ProcessoExecucao");
				tempList =ProcessoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoExecucao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoExecucaone.getQuantidadePaginas());
					ProcessoExecucaodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoExecucaone.Verificar(ProcessoExecucaodt); 
					if (Mensagem.length()==0){
						ProcessoExecucaone.salvar(ProcessoExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;

				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoAcaoPenal","Id_ProcessoAcaoPenal");
					request.setAttribute("tempBuscaProcesso","Processo");
					request.setAttribute("tempRetorno","ProcessoExecucao");
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
					tempList =ProcessoExecucaone.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcesso", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;			
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoExecucaodt.getId()))){
						ProcessoExecucaodt.limpar();
						ProcessoExecucaodt = ProcessoExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoExecucaodt",ProcessoExecucaodt );
		request.getSession().setAttribute("ProcessoExecucaone",ProcessoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
