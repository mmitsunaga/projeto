package br.gov.go.tj.projudi.ct;

 import br.gov.go.tj.projudi.dt.ServentiaDt;
 import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
 import br.gov.go.tj.projudi.dt.AudienciaTipoDt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.CejuscDisponibilidadeNe;
import br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CejuscDisponibilidadeCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -4416795700248179199L;

	public  CejuscDisponibilidadeCtGen() { 

	} 
		public int Permissao(){
			return CejuscDisponibilidadeDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CejuscDisponibilidadeDt CejuscDisponibilidadedt;
		CejuscDisponibilidadeNe CejuscDisponibilidadene;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/CejuscDisponibilidade.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","CejuscDisponibilidade");

		CejuscDisponibilidadene =(CejuscDisponibilidadeNe)request.getSession().getAttribute("CejuscDisponibilidadene");
		if (CejuscDisponibilidadene == null )  CejuscDisponibilidadene = new CejuscDisponibilidadeNe();  


		CejuscDisponibilidadedt =(CejuscDisponibilidadeDt)request.getSession().getAttribute("CejuscDisponibilidadedt");
		if (CejuscDisponibilidadedt == null )  CejuscDisponibilidadedt = new CejuscDisponibilidadeDt();  

		CejuscDisponibilidadedt.setId_Serv( request.getParameter("Id_Serv")); 
		CejuscDisponibilidadedt.setServ( request.getParameter("Serv")); 
		CejuscDisponibilidadedt.setId_UsuCejusc( request.getParameter("Id_UsuCejusc")); 
		CejuscDisponibilidadedt.setNome( request.getParameter("Nome")); 
		CejuscDisponibilidadedt.setDomingo( request.getParameter("Domingo")); 
		CejuscDisponibilidadedt.setSegunda( request.getParameter("Segunda")); 
		CejuscDisponibilidadedt.setTerca( request.getParameter("Terca")); 
		CejuscDisponibilidadedt.setQuarta( request.getParameter("Quarta")); 
		CejuscDisponibilidadedt.setQuinta( request.getParameter("Quinta")); 
		CejuscDisponibilidadedt.setSexta( request.getParameter("Sexta")); 
		CejuscDisponibilidadedt.setSabado( request.getParameter("Sabado")); 
		CejuscDisponibilidadedt.setId_AudiTipo( request.getParameter("Id_AudiTipo")); 
		CejuscDisponibilidadedt.setAudiTipo( request.getParameter("AudiTipo")); 

		CejuscDisponibilidadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CejuscDisponibilidadedt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					CejuscDisponibilidadene.excluir(CejuscDisponibilidadedt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"CejuscDisponibilidade"};
					String[] lisDescricao = {"CejuscDisponibilidade"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CejuscDisponibilidade");
					request.setAttribute("tempBuscaDescricao","CejuscDisponibilidade");
					request.setAttribute("tempBuscaPrograma","CejuscDisponibilidade");
					request.setAttribute("tempRetorno","CejuscDisponibilidade");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = CejuscDisponibilidadene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				CejuscDisponibilidadedt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=CejuscDisponibilidadene.Verificar(CejuscDisponibilidadedt); 
					if (Mensagem.length()==0){
						CejuscDisponibilidadene.salvar(CejuscDisponibilidadedt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serv");
					request.setAttribute("tempBuscaDescricao","Serv");
					request.setAttribute("tempBuscaPrograma","Serventia");
					request.setAttribute("tempRetorno","CejuscDisponibilidade");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = CejuscDisponibilidadene.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
				case (UsuarioCejuscDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"UsuarioCejusc"};
					String[] lisDescricao = {"UsuarioCejusc"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuCejusc");
					request.setAttribute("tempBuscaDescricao","Nome");
					request.setAttribute("tempBuscaPrograma","UsuarioCejusc");
					request.setAttribute("tempRetorno","CejuscDisponibilidade");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioCejuscDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = CejuscDisponibilidadene.consultarDescricaoUsuarioCejuscJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
				case (AudienciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"AudienciaTipo"};
					String[] lisDescricao = {"AudienciaTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AudiTipo");
					request.setAttribute("tempBuscaDescricao","AudiTipo");
					request.setAttribute("tempBuscaPrograma","AudienciaTipo");
					request.setAttribute("tempRetorno","CejuscDisponibilidade");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AudienciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = CejuscDisponibilidadene.consultarDescricaoAudienciaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_CejuscDisponibilidade");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( CejuscDisponibilidadedt.getId()))){
						CejuscDisponibilidadedt.limpar();
						CejuscDisponibilidadedt = CejuscDisponibilidadene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("CejuscDisponibilidadedt",CejuscDisponibilidadedt );
		request.getSession().setAttribute("CejuscDisponibilidadene",CejuscDisponibilidadene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
