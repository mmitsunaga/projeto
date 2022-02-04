package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.EscalaTipoNe;
import br.gov.go.tj.projudi.dt.EscalaTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EscalaTipoCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = 1330876259959132673L;

	public  EscalaTipoCtGen() { 

	} 
		public int Permissao(){
			return EscalaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EscalaTipoDt EscalaTipodt;
		EscalaTipoNe EscalaTipone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/EscalaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EscalaTipo");




		EscalaTipone =(EscalaTipoNe)request.getSession().getAttribute("EscalaTipone");
		if (EscalaTipone == null )  EscalaTipone = new EscalaTipoNe();  


		EscalaTipodt =(EscalaTipoDt)request.getSession().getAttribute("EscalaTipodt");
		if (EscalaTipodt == null )  EscalaTipodt = new EscalaTipoDt();  

		EscalaTipodt.setEscalaTipoCodigo( request.getParameter("EscalaTipoCodigo")); 
		EscalaTipodt.setEscalaTipo( request.getParameter("EscalaTipo")); 

		EscalaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EscalaTipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					EscalaTipone.excluir(EscalaTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"EscalaTipo"};
					String[] lisDescricao = {"EscalaTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_EscalaTipo");
					request.setAttribute("tempBuscaDescricao","EscalaTipo");
					request.setAttribute("tempBuscaPrograma","EscalaTipo");
					request.setAttribute("tempRetorno","EscalaTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = EscalaTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				EscalaTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=EscalaTipone.Verificar(EscalaTipodt); 
					if (Mensagem.length()==0){
						EscalaTipone.salvar(EscalaTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_EscalaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EscalaTipodt.getId()))){
						EscalaTipodt.limpar();
						EscalaTipodt = EscalaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EscalaTipodt",EscalaTipodt );
		request.getSession().setAttribute("EscalaTipone",EscalaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
