package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.ProcessoArquivamentoTipoNe;
import br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoArquivamentoTipoCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -2687309250282985904L;

	public  ProcessoArquivamentoTipoCtGen() { 

	} 
		public int Permissao(){
			return ProcessoArquivamentoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoArquivamentoTipoDt ProcessoArquivamentoTipodt;
		ProcessoArquivamentoTipoNe ProcessoArquivamentoTipone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoArquivamentoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoArquivamentoTipo");




		ProcessoArquivamentoTipone =(ProcessoArquivamentoTipoNe)request.getSession().getAttribute("ProcessoArquivamentoTipone");
		if (ProcessoArquivamentoTipone == null )  ProcessoArquivamentoTipone = new ProcessoArquivamentoTipoNe();  


		ProcessoArquivamentoTipodt =(ProcessoArquivamentoTipoDt)request.getSession().getAttribute("ProcessoArquivamentoTipodt");
		if (ProcessoArquivamentoTipodt == null )  ProcessoArquivamentoTipodt = new ProcessoArquivamentoTipoDt();  

		ProcessoArquivamentoTipodt.setProcessoArquivamentoTipo( request.getParameter("ProcessoArquivamentoTipo")); 

		ProcessoArquivamentoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoArquivamentoTipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoArquivamentoTipone.excluir(ProcessoArquivamentoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoArquivamentoTipo"};
					String[] lisDescricao = {"ProcessoArquivamentoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PrococessoArquivamentoTipo");
					request.setAttribute("tempBuscaDescricao","ProcessoArquivamentoTipo");
					request.setAttribute("tempBuscaPrograma","ProcessoArquivamentoTipo");
					request.setAttribute("tempRetorno","ProcessoArquivamentoTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoArquivamentoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				ProcessoArquivamentoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoArquivamentoTipone.Verificar(ProcessoArquivamentoTipodt); 
					if (Mensagem.length()==0){
						ProcessoArquivamentoTipone.salvar(ProcessoArquivamentoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PrococessoArquivamentoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoArquivamentoTipodt.getId()))){
						ProcessoArquivamentoTipodt.limpar();
						ProcessoArquivamentoTipodt = ProcessoArquivamentoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoArquivamentoTipodt",ProcessoArquivamentoTipodt );
		request.getSession().setAttribute("ProcessoArquivamentoTipone",ProcessoArquivamentoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
