package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoParteTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7788470184924845126L;

    public  ProcessoParteTipoCtGen() {

	} 
		public int Permissao(){
			return ProcessoParteTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteTipoDt ProcessoParteTipodt;
		ProcessoParteTipoNe ProcessoParteTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoParteTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoParteTipo");




		ProcessoParteTipone =(ProcessoParteTipoNe)request.getSession().getAttribute("ProcessoParteTipone");
		if (ProcessoParteTipone == null )  ProcessoParteTipone = new ProcessoParteTipoNe();  


		ProcessoParteTipodt =(ProcessoParteTipoDt)request.getSession().getAttribute("ProcessoParteTipodt");
		if (ProcessoParteTipodt == null )  ProcessoParteTipodt = new ProcessoParteTipoDt();  

		ProcessoParteTipodt.setProcessoParteTipo( request.getParameter("ProcessoParteTipo")); 
		ProcessoParteTipodt.setProcessoParteTipoCodigo( request.getParameter("ProcessoParteTipoCodigo")); 

		ProcessoParteTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoParteTipone.excluir(ProcessoParteTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoParteTipoLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoParteTipo","Id_ProcessoParteTipo");
				request.setAttribute("tempBuscaProcessoParteTipo","ProcessoParteTipo");
				request.setAttribute("tempRetorno","ProcessoParteTipo");
				tempList =ProcessoParteTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoParteTipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoParteTipone.getQuantidadePaginas());
					ProcessoParteTipodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoParteTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoParteTipone.Verificar(ProcessoParteTipodt); 
					if (Mensagem.length()==0){
						ProcessoParteTipone.salvar(ProcessoParteTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoParteTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoParteTipodt.getId()))){
						ProcessoParteTipodt.limpar();
						ProcessoParteTipodt = ProcessoParteTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoParteTipodt",ProcessoParteTipodt );
		request.getSession().setAttribute("ProcessoParteTipone",ProcessoParteTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
