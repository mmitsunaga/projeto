package br.gov.go.tj.projudi.ct;

import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
 import br.gov.go.tj.projudi.dt.ServentiaDt;
 import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
 import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
 import br.gov.go.tj.projudi.dt.PonteiroCejuscDt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.PonteiroCejuscNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PonteiroCejuscCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = 335444048155616797L;

	public  PonteiroCejuscCtGen() { 

	} 
		public int Permissao(){
			return PonteiroCejuscDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PonteiroCejuscDt PonteiroCejuscdt;
		PonteiroCejuscNe PonteiroCejuscne;


		String stNomeBusca1="";
		String stNomeBusca2="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PonteiroCejusc.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PonteiroCejusc");

		PonteiroCejuscne =(PonteiroCejuscNe)request.getSession().getAttribute("PonteiroCejuscne");
		if (PonteiroCejuscne == null )  PonteiroCejuscne = new PonteiroCejuscNe();  

		PonteiroCejuscdt =(PonteiroCejuscDt)request.getSession().getAttribute("PonteiroCejuscdt");
		if (PonteiroCejuscdt == null )  PonteiroCejuscdt = new PonteiroCejuscDt();  

		PonteiroCejuscdt.setId_UsuCejusc( request.getParameter("Id_UsuCejusc")); 
		PonteiroCejuscdt.setUsuCejusc( request.getParameter("UsuCejusc")); 
		PonteiroCejuscdt.setId_Serv( request.getParameter("Id_Serv")); 
		PonteiroCejuscdt.setServ( request.getParameter("Serv")); 
		PonteiroCejuscdt.setId_UsuServConfirmou( request.getParameter("Id_UsuServConfirmou")); 
		PonteiroCejuscdt.setUsuServConfirmou( request.getParameter("UsuServConfirmou")); 
//		PonteiroCejuscdt.setId_UsuServCompareceu( request.getParameter("Id_UsuServCompareceu")); 
//		PonteiroCejuscdt.setUsuServCompareceu( request.getParameter("UsuServCompareceu")); 
		PonteiroCejuscdt.setId_ServCargoBanca( request.getParameter("Id_ServCargoBanca")); 
		PonteiroCejuscdt.setData( request.getParameter("Data")); 
		PonteiroCejuscdt.setId_PonteiroCejuscStatus( request.getParameter("Id_PonteiroCejuscStatus")); 
		PonteiroCejuscdt.setPonteiroCejuscStatus( request.getParameter("PonteiroCejuscStatus")); 

		PonteiroCejuscdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PonteiroCejuscdt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PonteiroCejuscne.excluir(PonteiroCejuscdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PonteiroCejusc"};
					String[] lisDescricao = {"PonteiroCejusc"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PonteiroCejusc");
					request.setAttribute("tempBuscaDescricao","PonteiroCejusc");
					request.setAttribute("tempBuscaPrograma","PonteiroCejusc");
					request.setAttribute("tempRetorno","PonteiroCejusc");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroCejuscne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				PonteiroCejuscdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PonteiroCejuscne.Verificar(PonteiroCejuscdt); 
					if (Mensagem.length()==0){
						PonteiroCejuscne.salvar(PonteiroCejuscdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioCejuscDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"UsuarioCejusc"};
					String[] lisDescricao = {"UsuarioCejusc"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuCejusc");
					request.setAttribute("tempBuscaDescricao","UsuCejusc");
					request.setAttribute("tempBuscaPrograma","UsuarioCejusc");
					request.setAttribute("tempRetorno","PonteiroCejusc");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioCejuscDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroCejuscne.consultarDescricaoUsuarioCejuscJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serv");
					request.setAttribute("tempBuscaDescricao","Serv");
					request.setAttribute("tempBuscaPrograma","Serventia");
					request.setAttribute("tempRetorno","PonteiroCejusc");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroCejuscne.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"UsuarioServentia"};
					String[] lisDescricao = {"UsuarioServentia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuServCompareceu");
					request.setAttribute("tempBuscaDescricao","UsuServCompareceu");
					request.setAttribute("tempBuscaPrograma","UsuarioServentia");
					request.setAttribute("tempRetorno","PonteiroCejusc");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroCejuscne.consultarDescricaoUsuarioServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
				case (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo","Serventia"};
					String[] lisDescricao = {"ServentiaCargo","Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServCargoBanca");
					request.setAttribute("tempBuscaDescricao","Data");
					request.setAttribute("tempBuscaPrograma","ServentiaCargo");
					request.setAttribute("tempRetorno","PonteiroCejusc");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroCejuscne.consultarDescricaoServentiaCargoJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
				case (PonteiroCejuscDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PonteiroCejusc"};
					String[] lisDescricao = {"PonteiroCejusc"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PonteiroCejuscStatus");
					request.setAttribute("tempBuscaDescricao","PonteiroCejuscStatus");
					request.setAttribute("tempBuscaPrograma","PonteiroCejusc");
					request.setAttribute("tempRetorno","PonteiroCejusc");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (PonteiroCejuscDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroCejuscne.consultarDescricaoPonteiroCejuscJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
			default:
				stId = request.getParameter("Id_PonteiroCejusc");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PonteiroCejuscdt.getId()))){
						PonteiroCejuscdt.limpar();
						PonteiroCejuscdt = PonteiroCejuscne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PonteiroCejuscdt",PonteiroCejuscdt );
		request.getSession().setAttribute("PonteiroCejuscne",PonteiroCejuscne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
