package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ne.ProcessoParteBeneficioNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteBeneficioCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 142680154972351499L;

    public  ProcessoParteBeneficioCtGen() {

	} 
		public int Permissao(){
			return ProcessoParteBeneficioDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteBeneficioDt ProcessoParteBeneficiodt;
		ProcessoParteBeneficioNe ProcessoParteBeneficione;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoParteBeneficio.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoParteBeneficio");




		ProcessoParteBeneficione =(ProcessoParteBeneficioNe)request.getSession().getAttribute("ProcessoParteBeneficione");
		if (ProcessoParteBeneficione == null )  ProcessoParteBeneficione = new ProcessoParteBeneficioNe();  


		ProcessoParteBeneficiodt =(ProcessoParteBeneficioDt)request.getSession().getAttribute("ProcessoParteBeneficiodt");
		if (ProcessoParteBeneficiodt == null )  ProcessoParteBeneficiodt = new ProcessoParteBeneficioDt();  

		ProcessoParteBeneficiodt.setId_ProcessoBeneficio( request.getParameter("Id_ProcessoBeneficio")); 
		ProcessoParteBeneficiodt.setProcessoBeneficio( request.getParameter("ProcessoBeneficio")); 
		ProcessoParteBeneficiodt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte")); 
		ProcessoParteBeneficiodt.setNome( request.getParameter("Nome")); 
		ProcessoParteBeneficiodt.setDataInicial( request.getParameter("DataInicial")); 
		ProcessoParteBeneficiodt.setDataFinal( request.getParameter("DataFinal")); 

		ProcessoParteBeneficiodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteBeneficiodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoParteBeneficione.excluir(ProcessoParteBeneficiodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoParteBeneficioLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoParteBeneficio","Id_ProcessoParteBeneficio");
				request.setAttribute("tempBuscaProcessoParteBeneficio","ProcessoParteBeneficio");
				request.setAttribute("tempRetorno","ProcessoParteBeneficio");
				tempList =ProcessoParteBeneficione.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoParteBeneficio", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoParteBeneficione.getQuantidadePaginas());
					ProcessoParteBeneficiodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoParteBeneficiodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoParteBeneficione.Verificar(ProcessoParteBeneficiodt); 
					if (Mensagem.length()==0){
						ProcessoParteBeneficione.salvar(ProcessoParteBeneficiodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProcessoParte","Id_ProcessoParte");
					request.setAttribute("tempBuscaProcessoParte","ProcessoParte");
					request.setAttribute("tempRetorno","ProcessoParteBeneficio");
					stAcao="/WEB-INF/jsptjgo/ProcessoParteLocalizar.jsp";
					tempList =ProcessoParteBeneficione.consultarDescricaoProcessoParte(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoParte", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoParteBeneficione.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoParteBeneficio");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoParteBeneficiodt.getId()))){
						ProcessoParteBeneficiodt.limpar();
						ProcessoParteBeneficiodt = ProcessoParteBeneficione.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoParteBeneficiodt",ProcessoParteBeneficiodt );
		request.getSession().setAttribute("ProcessoParteBeneficione",ProcessoParteBeneficione );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
