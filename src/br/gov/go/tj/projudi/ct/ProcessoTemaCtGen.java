package br.gov.go.tj.projudi.ct;

 import br.gov.go.tj.projudi.dt.TemaDt;
 import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.ProcessoTemaNe;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoTemaCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7973420236985468243L;

	public  ProcessoTemaCtGen() {

	} 
		public int Permissao() {
			return ProcessoTemaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoTemaDt ProcessoTemadt;
		ProcessoTemaNe ProcessoTemane;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoTema.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoTema");




		ProcessoTemane =(ProcessoTemaNe)request.getSession().getAttribute("ProcessoTemane");
		if (ProcessoTemane == null )  ProcessoTemane = new ProcessoTemaNe();  


		ProcessoTemadt =(ProcessoTemaDt)request.getSession().getAttribute("ProcessoTemadt");
		if (ProcessoTemadt == null )  ProcessoTemadt = new ProcessoTemaDt();  

		ProcessoTemadt.setId_Tema( request.getParameter("Id_Tema")); 
		ProcessoTemadt.setId_Proc( request.getParameter("Id_Proc")); 
		ProcessoTemadt.setProcNumero( request.getParameter("ProcNumero")); 
		ProcessoTemadt.setTemaCodigo( request.getParameter("TemaCodigo")); 

		ProcessoTemadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoTemadt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoTemane.excluir(ProcessoTemadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoTema"};
					String[] lisDescricao = {"ProcessoTema"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcTema");
					request.setAttribute("tempBuscaDescricao","ProcessoTema");
					request.setAttribute("tempBuscaPrograma","ProcessoTema");
					request.setAttribute("tempRetorno","ProcessoTema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoTemane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				ProcessoTemadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoTemane.Verificar(ProcessoTemadt); 
					if (Mensagem.length()==0){
						ProcessoTemane.salvar(ProcessoTemadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (TemaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema"};
					String[] lisDescricao = {"Tema"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Tema");
					request.setAttribute("tempBuscaDescricao","Id_Proc");
					request.setAttribute("tempBuscaPrograma","Tema");
					request.setAttribute("tempRetorno","ProcessoTema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (TemaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoTemane.consultarDescricaoTemaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcTema");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoTemadt.getId()))){
						ProcessoTemadt.limpar();
						ProcessoTemadt = ProcessoTemane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoTemadt",ProcessoTemadt );
		request.getSession().setAttribute("ProcessoTemane",ProcessoTemane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
