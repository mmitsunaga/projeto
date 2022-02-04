package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.ne.ProcessoSubtipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoSubtipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -2053377217740949353L;

    public  ProcessoSubtipoCtGen() {

	} 
		public int Permissao(){
			return ProcessoSubtipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoSubtipoDt ProcessoSubtipodt;
		ProcessoSubtipoNe ProcessoSubtipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoSubtipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoSubtipo");




		ProcessoSubtipone =(ProcessoSubtipoNe)request.getSession().getAttribute("ProcessoSubtipone");
		if (ProcessoSubtipone == null )  ProcessoSubtipone = new ProcessoSubtipoNe();  


		ProcessoSubtipodt =(ProcessoSubtipoDt)request.getSession().getAttribute("ProcessoSubtipodt");
		if (ProcessoSubtipodt == null )  ProcessoSubtipodt = new ProcessoSubtipoDt();  

		ProcessoSubtipodt.setProcessoSubtipo( request.getParameter("ProcessoSubtipo")); 
		ProcessoSubtipodt.setProcessoSubtipoCodigo( request.getParameter("ProcessoSubtipoCodigo")); 

		ProcessoSubtipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoSubtipodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoSubtipone.excluir(ProcessoSubtipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoSubtipoLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoSubtipo","Id_ProcessoSubtipo");
				request.setAttribute("tempBuscaProcessoSubtipo","ProcessoSubtipo");
				request.setAttribute("tempRetorno","ProcessoSubtipo");
				tempList =ProcessoSubtipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoSubtipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoSubtipone.getQuantidadePaginas());
					ProcessoSubtipodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoSubtipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoSubtipone.Verificar(ProcessoSubtipodt); 
					if (Mensagem.length()==0){
						ProcessoSubtipone.salvar(ProcessoSubtipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoSubtipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoSubtipodt.getId()))){
						ProcessoSubtipodt.limpar();
						ProcessoSubtipodt = ProcessoSubtipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoSubtipodt",ProcessoSubtipodt );
		request.getSession().setAttribute("ProcessoSubtipone",ProcessoSubtipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
