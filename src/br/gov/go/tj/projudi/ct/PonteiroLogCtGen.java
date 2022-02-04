package br.gov.go.tj.projudi.ct;

 import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
 import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
 import br.gov.go.tj.projudi.dt.ServentiaDt;
 import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.PonteiroLogNe;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PonteiroLogCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -8416534493495051710L;

	public  PonteiroLogCtGen() { 

	} 
		public int Permissao(){
			return PonteiroLogDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PonteiroLogDt PonteiroLogdt;
		PonteiroLogNe PonteiroLogne;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PonteiroLog.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PonteiroLog");




		PonteiroLogne =(PonteiroLogNe)request.getSession().getAttribute("PonteiroLogne");
		if (PonteiroLogne == null )  PonteiroLogne = new PonteiroLogNe();  


		PonteiroLogdt =(PonteiroLogDt)request.getSession().getAttribute("PonteiroLogdt");
		if (PonteiroLogdt == null )  PonteiroLogdt = new PonteiroLogDt();  

		PonteiroLogdt.setId_AreaDistribuicao( request.getParameter("Id_AreaDist")); 
		PonteiroLogdt.setAreaDistribuicao( request.getParameter("AreaDist")); 
		PonteiroLogdt.setId_PonteiroLogTipo( request.getParameter("Id_PonteiroLogTipo")); 
		PonteiroLogdt.setPonteiroLogTipo( request.getParameter("PonteiroLogTipo")); 
		PonteiroLogdt.setId_Proc( request.getParameter("Id_Proc")); 
		PonteiroLogdt.setProcNumero( request.getParameter("ProcNumero")); 
		PonteiroLogdt.setId_Serventia( request.getParameter("Id_Serv")); 
		PonteiroLogdt.setServentia( request.getParameter("Serv")); 
		PonteiroLogdt.setId_UsuarioServentia( request.getParameter("Id_UserServ")); 
		PonteiroLogdt.setNome( request.getParameter("Nome")); 
		PonteiroLogdt.setData( request.getParameter("Data")); 
		PonteiroLogdt.setJustificativa( request.getParameter("Justificativa")); 
		PonteiroLogdt.setQtd( request.getParameter("Qtd")); 

		PonteiroLogdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PonteiroLogdt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
		//não poderá excluir
//			case Configuracao.ExcluirResultado: //Excluir
//					PonteiroLogne.excluir(PonteiroLogdt); 
//					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
//				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PonteiroLog"};
					String[] lisDescricao = {"PonteiroLog"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PonteiroLog");
					request.setAttribute("tempBuscaDescricao","PonteiroLog");
					request.setAttribute("tempBuscaPrograma","PonteiroLog");
					request.setAttribute("tempRetorno","PonteiroLog");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				PonteiroLogdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PonteiroLogne.Verificar(PonteiroLogdt); 
					if (Mensagem.length()==0){
						PonteiroLogne.salvar(PonteiroLogdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDist");
					request.setAttribute("tempBuscaDescricao","AreaDist");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");
					request.setAttribute("tempRetorno","PonteiroLog");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogne.consultarDescricaoAreaDistribuicaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
				case (PonteiroLogTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PonteiroLogTipo"};
					String[] lisDescricao = {"PonteiroLogTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PonteiroLogTipo");
					request.setAttribute("tempBuscaDescricao","PonteiroLogTipo");
					request.setAttribute("tempBuscaPrograma","PonteiroLogTipo");
					request.setAttribute("tempRetorno","PonteiroLog");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (PonteiroLogTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogne.consultarDescricaoPonteiroLogTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
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
					request.setAttribute("tempRetorno","PonteiroLog");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogne.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
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
					request.setAttribute("tempBuscaId","Id_UserServ");
					request.setAttribute("tempBuscaDescricao","Nome");
					request.setAttribute("tempBuscaPrograma","UsuarioServentia");
					request.setAttribute("tempRetorno","PonteiroLog");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroLogne.consultarDescricaoUsuarioServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PonteiroLog");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PonteiroLogdt.getId()))){
						PonteiroLogdt.limpar();
						PonteiroLogdt = PonteiroLogne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PonteiroLogdt",PonteiroLogdt );
		request.getSession().setAttribute("PonteiroLogne",PonteiroLogne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
