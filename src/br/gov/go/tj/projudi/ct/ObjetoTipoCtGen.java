package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.ObjetoTipoNe;
import br.gov.go.tj.projudi.dt.ObjetoTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ObjetoTipoCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -1020137409717310067L;

	public  ObjetoTipoCtGen() { 

	} 
		public int Permissao(){
			return ObjetoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ObjetoTipoDt ObjetoTipodt;
		ObjetoTipoNe ObjetoTipone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ObjetoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ObjetoTipo");




		ObjetoTipone =(ObjetoTipoNe)request.getSession().getAttribute("ObjetoTipone");
		if (ObjetoTipone == null )  ObjetoTipone = new ObjetoTipoNe();  


		ObjetoTipodt =(ObjetoTipoDt)request.getSession().getAttribute("ObjetoTipodt");
		if (ObjetoTipodt == null )  ObjetoTipodt = new ObjetoTipoDt();  

		ObjetoTipodt.setObjetoTipo( request.getParameter("ObjetoTipo")); 
		ObjetoTipodt.setCodigo( request.getParameter("Codigo")); 

		ObjetoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ObjetoTipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				    if (ObjetoTipodt.getId() != null && ObjetoTipodt.getId().length()>1){
						ObjetoTipone.excluir(ObjetoTipodt); 
						request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				    } else {
						request.setAttribute("MensagemErro", "Nenhum Objeto Tipo selecionado para exclusão!");					
					}
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ObjetoTipo"};
					String[] lisDescricao = {"ObjetoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ObjetoTipo");
					request.setAttribute("tempBuscaDescricao","ObjetoTipo");
					request.setAttribute("tempBuscaPrograma","ObjetoTipo");
					request.setAttribute("tempRetorno","ObjetoTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ObjetoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				ObjetoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ObjetoTipone.Verificar(ObjetoTipodt); 
					if (Mensagem.length()==0){
						ObjetoTipone.salvar(ObjetoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					} else {
						request.setAttribute("MensagemErro", Mensagem );					
					}
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ObjetoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ObjetoTipodt.getId()))){
						ObjetoTipodt.limpar();
						ObjetoTipodt = ObjetoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ObjetoTipodt",ObjetoTipodt );
		request.getSession().setAttribute("ObjetoTipone",ObjetoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
