package br.gov.go.tj.projudi.ct;

 import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.PonteiroLogCompensarNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PonteiroLogCompensarCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -4765264630736105793L;

	public  PonteiroLogCompensarCtGen() { 

	} 
		public int Permissao(){
			return PonteiroLogCompensarDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PonteiroLogCompensarDt PonteiroLogCompensardt;
		PonteiroLogCompensarNe PonteiroLogCompensarne;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PonteiroLogCompensar.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PonteiroLogCompensar");


		PonteiroLogCompensarne =(PonteiroLogCompensarNe)request.getSession().getAttribute("PonteiroLogCompensarne");
		if (PonteiroLogCompensarne == null )  PonteiroLogCompensarne = new PonteiroLogCompensarNe();  


		PonteiroLogCompensardt =(PonteiroLogCompensarDt)request.getSession().getAttribute("PonteiroLogCompensardt");
		if (PonteiroLogCompensardt == null )  PonteiroLogCompensardt = new PonteiroLogCompensarDt();  

		PonteiroLogCompensardt.setId_AreaDistribuicao_O( request.getParameter("Id_AreaDistribuicao_O")); 
		PonteiroLogCompensardt.setAreaDistribuicao_O( request.getParameter("AreaDistribuicao_O")); 
		PonteiroLogCompensardt.setId_Serventia_O( request.getParameter("Id_Serventia_O")); 
		PonteiroLogCompensardt.setServentia_O( request.getParameter("Serventia_O")); 
		PonteiroLogCompensardt.setId_ServentiaCargo_O( request.getParameter("Id_ServentiaCargo_O")); 
		PonteiroLogCompensardt.setServentiaCargo_O( request.getParameter("ServentiaCargo_O")); 
		PonteiroLogCompensardt.setId_AreaDistribuicao_D( request.getParameter("Id_AreaDistribuicao_D")); 
		PonteiroLogCompensardt.setAreaDistribuicao_D( request.getParameter("AreaDistribuicao_D")); 
		PonteiroLogCompensardt.setId_Serventia_D( request.getParameter("Id_Serventia_D")); 
		PonteiroLogCompensardt.setServentia_D( request.getParameter("Serventia_D")); 
		PonteiroLogCompensardt.setId_ServentiaCargo_D( request.getParameter("Id_ServentiaCargo_D")); 
		PonteiroLogCompensardt.setServentiaCargo_D( request.getParameter("ServentiaCargo_D")); 
		PonteiroLogCompensardt.setQtd( request.getParameter("Qtd")); 
		PonteiroLogCompensardt.setId_UsuarioServentia_I( request.getParameter("Id_UsuarioServentia_I")); 
		PonteiroLogCompensardt.setUsuario_I( request.getParameter("Usuario_I")); 
		PonteiroLogCompensardt.setId_UsuarioServentia_F( request.getParameter("Id_UsuarioServentia_F")); 
		PonteiroLogCompensardt.setUsuario_F( request.getParameter("Usuario_F")); 
		PonteiroLogCompensardt.setDataInicio( request.getParameter("DataInicio")); 
		PonteiroLogCompensardt.setDataFinal( request.getParameter("DataFinal")); 
		PonteiroLogCompensardt.setJustificativa( request.getParameter("Justificativa")); 

		PonteiroLogCompensardt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		PonteiroLogCompensardt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PonteiroLogCompensarne.excluir(PonteiroLogCompensardt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PonteiroLogCompensar"};
					String[] lisDescricao = {"PonteiroLogCompensar"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PonteiroLogCompensar");
					request.setAttribute("tempBuscaDescricao","PonteiroLogCompensar");
					request.setAttribute("tempBuscaPrograma","PonteiroLogCompensar");
					request.setAttribute("tempRetorno","PonteiroLogCompensar");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogCompensarne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				PonteiroLogCompensardt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PonteiroLogCompensarne.Verificar(PonteiroLogCompensardt); 
					if (Mensagem.length()==0){
						PonteiroLogCompensarne.salvar(PonteiroLogCompensardt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;

				case (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServentiaCargo_O");
					request.setAttribute("tempBuscaDescricao","ServentiaCargo_O");
					request.setAttribute("tempBuscaPrograma","ServentiaCargo");
					request.setAttribute("tempRetorno","PonteiroLogCompensar");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					// TODO: Corrigir linha abaixo.
					//String stTemp = PonteiroLogCompensarne.consultarDescricaoServentiaCargoJSON(stNomeBusca1, PosicaoPaginaAtual);
					String stTemp = null;
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
				case (AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDistribuicao_D");
					request.setAttribute("tempBuscaDescricao","AreaDistribuicao_D");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");
					request.setAttribute("tempRetorno","PonteiroLogCompensar");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogCompensarne.consultarDescricaoAreaDistribuicaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"UsuarioServentia"};
					String[] lisDescricao = {"UsuarioServentia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuarioServentia_F");
					request.setAttribute("tempBuscaDescricao","Usuario_F");
					request.setAttribute("tempBuscaPrograma","UsuarioServentia");
					request.setAttribute("tempRetorno","PonteiroLogCompensar");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogCompensarne.consultarDescricaoUsuarioServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PonteiroLogCompensar");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PonteiroLogCompensardt.getId()))){
						PonteiroLogCompensardt.limpar();
						PonteiroLogCompensardt = PonteiroLogCompensarne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PonteiroLogCompensardt",PonteiroLogCompensardt );
		request.getSession().setAttribute("PonteiroLogCompensarne",PonteiroLogCompensarne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
