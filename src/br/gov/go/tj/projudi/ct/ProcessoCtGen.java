package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ObjetoPedidoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 2720195560491079127L;

    public  ProcessoCtGen() {

	} 
		public int Permissao(){
			return ProcessoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt;
		ProcessoNe Processone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Processo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Processo");




		Processone =(ProcessoNe)request.getSession().getAttribute("Processone");
		if (Processone == null )  Processone = new ProcessoNe();  


		processoDt =(ProcessoDt)request.getSession().getAttribute("processoDt");
		if (processoDt == null )  processoDt = new ProcessoDt();  

		processoDt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		processoDt.setId_ProcessoPrincipal( request.getParameter("Id_ProcessoDependente")); 
		processoDt.setProcessoNumeroPrincipal( request.getParameter("ProcessoNumeroDependente")); 
		processoDt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		processoDt.setProcessoTipo( request.getParameter("ProcessoTipo")); 
		processoDt.setId_ProcessoFase( request.getParameter("Id_ProcessoFase")); 
		processoDt.setProcessoFase( request.getParameter("ProcessoFase")); 
		processoDt.setId_ProcessoStatus( request.getParameter("Id_ProcessoStatus")); 
		processoDt.setProcessoStatus( request.getParameter("ProcessoStatus")); 
		processoDt.setId_ProcessoPrioridade( request.getParameter("Id_ProcessoPrioridade")); 
		processoDt.setProcessoPrioridade( request.getParameter("ProcessoPrioridade")); 
		processoDt.setId_Serventia( request.getParameter("Id_Serventia")); 
		processoDt.setServentia( request.getParameter("Serventia")); 
		processoDt.setId_ServentiaOrigem( request.getParameter("Id_ServentiaOrigem")); 
		processoDt.setServentiaOrigem( request.getParameter("ServentiaOrigem")); 
		processoDt.setId_Area( request.getParameter("Id_Area")); 
		processoDt.setArea( request.getParameter("Area")); 
		processoDt.setId_ObjetoPedido( request.getParameter("Id_ObjetoPedido")); 
		processoDt.setObjetoPedido( request.getParameter("ObjetoPedido")); 
		processoDt.setId_Classificador( request.getParameter("Id_Classificador")); 
		processoDt.setClassificador( request.getParameter("Classificador")); 
		if (request.getParameter("SegredoJustica") != null)
			processoDt.setSegredoJustica( request.getParameter("SegredoJustica")); 
		else processoDt.setSegredoJustica("false");
		processoDt.setProcessoDiretorio( request.getParameter("ProcessoDiretorio")); 
		processoDt.setTcoNumero( request.getParameter("TcoNumero")); 
		processoDt.setValor( request.getParameter("Valor")); 
		processoDt.setDataRecebimento( request.getParameter("DataRecebimento")); 
		processoDt.setDataArquivamento( request.getParameter("DataArquivamento")); 
		if (request.getParameter("Apenso") != null)
			processoDt.setApenso( request.getParameter("Apenso")); 
		else processoDt.setApenso("false");
		processoDt.setAno( request.getParameter("Ano")); 
		processoDt.setForumCodigo( request.getParameter("ForumCodigo")); 
		processoDt.setId_AreaDistribuicao( request.getParameter("Id_AreaDistribuicao")); 
		processoDt.setProcessoTipoCodigo( request.getParameter("ProcessoTipoCodigo")); 
		processoDt.setProcessoFaseCodigo( request.getParameter("ProcessoFaseCodigo")); 
		processoDt.setProcessoStatusCodigo( request.getParameter("ProcessoStatusCodigo")); 
		processoDt.setProcessoPrioridadeCodigo( request.getParameter("ProcessoPrioridadeCodigo")); 
		processoDt.setServentiaCodigo( request.getParameter("ServentiaCodigo")); 
		processoDt.setServentiaOrigemCodigo( request.getParameter("ServentiaOrigemCodigo")); 
		processoDt.setAreaCodigo( request.getParameter("AreaCodigo")); 
		processoDt.setObjetoPedidoCodigo( request.getParameter("ObjetoPedidoCodigo")); 

		processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Processone.excluir(processoDt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
				request.setAttribute("tempBuscaId_Processo","Id_Processo");
				request.setAttribute("tempBuscaProcesso","Processo");
				request.setAttribute("tempRetorno","Processo");
				tempList =Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcesso", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					processoDt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				processoDt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Processone.Verificar(processoDt); 
					if (Mensagem.length()==0){
						Processone.salvar(processoDt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoDependente","Id_ProcessoDependente");
					request.setAttribute("tempBuscaProcesso","Processo");
					request.setAttribute("tempRetorno","Processo");
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
					tempList =Processone.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcesso", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

				case (ProcessoFaseDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoFase","Id_ProcessoFase");
					request.setAttribute("tempBuscaProcessoFase","ProcessoFase");
					request.setAttribute("tempRetorno","Processo");
					stAcao="/WEB-INF/jsptjgo/ProcessoFaseLocalizar.jsp";
					tempList =Processone.consultarDescricaoProcessoFase(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoFase", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoStatus","Id_ProcessoStatus");
					request.setAttribute("tempBuscaProcessoStatus","ProcessoStatus");
					request.setAttribute("tempRetorno","Processo");
					tempList =Processone.consultarDescricaoProcessoStatus(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoStatus", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoPrioridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoPrioridade","Id_ProcessoPrioridade");
					request.setAttribute("tempBuscaProcessoPrioridade","ProcessoPrioridade");
					request.setAttribute("tempRetorno","Processo");
					stAcao="/WEB-INF/jsptjgo/ProcessoPrioridadeLocalizar.jsp";
					tempList =Processone.consultarDescricaoProcessoPrioridade(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoPrioridade", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ServentiaOrigem","Id_ServentiaOrigem");
					request.setAttribute("tempBuscaServentia","Serventia");
					request.setAttribute("tempRetorno","Processo");
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
					tempList =Processone.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AreaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Area","Id_Area");
					request.setAttribute("tempBuscaArea","Area");
					request.setAttribute("tempRetorno","Processo");
					stAcao="/WEB-INF/jsptjgo/AreaLocalizar.jsp";
					tempList =Processone.consultarDescricaoArea(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaArea", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ObjetoPedidoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ObjetoPedido","Id_ObjetoPedido");
					request.setAttribute("tempBuscaObjetoPedido","ObjetoPedido");
					request.setAttribute("tempRetorno","Processo");
					stAcao="/WEB-INF/jsptjgo/ObjetoPedidoLocalizar.jsp";
					tempList =Processone.consultarDescricaoObjetoPedido(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaObjetoPedido", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

				case (AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_AreaDistribuicao","Id_AreaDistribuicao");
					request.setAttribute("tempBuscaAreaDistribuicao","AreaDistribuicao");
					request.setAttribute("tempRetorno","Processo");
					stAcao="/WEB-INF/jsptjgo/AreaDistribuicaoLocalizar.jsp";
					tempList =Processone.consultarDescricaoAreaDistribuicao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaAreaDistribuicao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Processo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( processoDt.getId()))){
						processoDt.limpar();
						processoDt = Processone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("processoDt",processoDt );
		request.getSession().setAttribute("Processone",Processone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
