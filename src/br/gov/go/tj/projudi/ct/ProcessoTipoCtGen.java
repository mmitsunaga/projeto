package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -8624843696236816119L;

    public  ProcessoTipoCtGen() {

	} 
		public int Permissao(){
			return ProcessoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoTipoDt ProcessoTipodt;
		ProcessoTipoNe ProcessoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoTipo");




		ProcessoTipone =(ProcessoTipoNe)request.getSession().getAttribute("ProcessoTipone");
		if (ProcessoTipone == null )  ProcessoTipone = new ProcessoTipoNe();  


		ProcessoTipodt =(ProcessoTipoDt)request.getSession().getAttribute("ProcessoTipodt");
		if (ProcessoTipodt == null )  ProcessoTipodt = new ProcessoTipoDt();  

		ProcessoTipodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 
		ProcessoTipodt.setProcessoTipoCodigo( request.getParameter("ProcessoTipoCodigo")); 
		ProcessoTipodt.setOrdem2Grau( request.getParameter("Ordem2Grau")); 

		ProcessoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoTipone.excluir(ProcessoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				ProcessoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoTipone.Verificar(ProcessoTipodt); 
					if (Mensagem.length()==0){
						ProcessoTipone.salvar(ProcessoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoTipodt.getId()))){
						ProcessoTipodt.limpar();
						ProcessoTipodt = ProcessoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoTipodt",ProcessoTipodt );
		request.getSession().setAttribute("ProcessoTipone",ProcessoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
