package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.CejuscDisponibilidadeNe;
import br.gov.go.tj.projudi.ne.UsuarioCejuscNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CejuscDisponibilidadeCt extends CejuscDisponibilidadeCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3982469469002220850L;
	
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CejuscDisponibilidadeDt CejuscDisponibilidadedt;
		CejuscDisponibilidadeNe CejuscDisponibilidadene;
		UsuarioDt usuarioDt = null;

		String stNomeBusca1="";
		String stNomeBusca2="";
		String Mensagem="";
		String stId="";
		int paginaAnterior=0;

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
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Novo: 
				CejuscDisponibilidadedt.limpar();
				request.getSession().removeAttribute("situacao");
				if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL &&
				    Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.NUPEMEC) {
					UsuarioCejuscDt usuarioCejuscDt = CejuscDisponibilidadene.consultarUsuarioCejuscDtIdUsuario(UsuarioSessao.getUsuarioDt().getId());
					if (usuarioCejuscDt != null) {
						if (usuarioCejuscDt.getCodigoStatusAtual().equalsIgnoreCase(UsuarioCejuscDt.CODIGO_STATUS_APROVADO)) {
							CejuscDisponibilidadedt.setId_UsuCejusc(usuarioCejuscDt.getId()); 
							CejuscDisponibilidadedt.setNome(UsuarioSessao.getUsuarioDt().getNome());
						}else {
							super.exibaMensagemInconsistenciaErro(request, "O seu usuário ainda não foi aprovado pelo CEJUSC.");
						}						
					} else {
						super.exibaMensagemInconsistenciaErro(request, "Não foi localizado um usuário cejusc vinculado ao seu usuário.");
					}
				}
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					
					String[] lisNomeBusca;
					String[] lisDescricao;
					
					if (UsuarioSessao.getUsuarioDt().isServentiaPreprocessual() || UsuarioSessao.getUsuarioDt().isServentiaNupemec() ) {
						lisNomeBusca = new String[]{"Serventia", "Nome"};
						lisDescricao = new String[] {"Usuário CEJUSC", "Tipo da Audiência", "Serventia", "Situação do Conciliador"};
					}
					else {
						lisNomeBusca = new String[]{"Serventia"};
						lisDescricao = new String[] {"Usuário CEJUSC", "Tipo da Audiência", "Serventia"};
					}
					
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CejuscDisponibilidade");
					request.setAttribute("tempBuscaDescricao","CejuscDisponibilidade");
					request.setAttribute("tempBuscaPrograma","Disponibilidade dos Conciliadores");
					request.setAttribute("tempRetorno","CejuscDisponibilidade");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = null;
															
					if (UsuarioSessao.getUsuarioDt().isServentiaPreprocessual() || UsuarioSessao.getUsuarioDt().isServentiaNupemec() ) {
							//Para o caso dos Cejuscs e Nupemec, passando null para o id do usuário para permitir consultar a disponibilidade
							//de todos.
							stTemp = CejuscDisponibilidadene.consultarDescricaoJSON(stNomeBusca2, stNomeBusca1, null, PosicaoPaginaAtual);
					} else {
							//Para conciliadores, passando o id do usuário restringe a consulta apenas às disponibilidades de quem está logado
							stTemp = CejuscDisponibilidadene.consultarDescricaoJSON(stNomeBusca2, stNomeBusca1, UsuarioSessao.getId_Usuario(), PosicaoPaginaAtual);
					}									
					
					enviarJSON(response, stTemp);
											
					return;
				}
				break;
			case (UsuarioCejuscDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Usuário Cejusc"};
					String[] lisDescricao = {"Usuário Cejusc", "CPF"};
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
					CejuscDisponibilidadedt.setId_AudiTipo("null");
					CejuscDisponibilidadedt.setAudiTipo("");
					CejuscDisponibilidadedt.setId_Serv("null");
					CejuscDisponibilidadedt.setServ("");
				} else {
					 															
					String stTemp = CejuscDisponibilidadene.consultarDescricaoUsuarioCejuscJSON(stNomeBusca1, PosicaoPaginaAtual);
											
					enviarJSON(response, stTemp);					
					
					return;
				}
				break;
			case (AudienciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (CejuscDisponibilidadedt.getId_UsuCejusc() != null && CejuscDisponibilidadedt.getId_UsuCejusc().trim().length() > 0) {
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Tipo de Audiência"};
						String[] lisDescricao = {"Tipo de Audiência"};
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
						CejuscDisponibilidadedt.setId_Serv("null");
						CejuscDisponibilidadedt.setServ("");
					} else {
						 
						String stTemp = CejuscDisponibilidadene.consultarDescricaoAudienciaTipoJSON(stNomeBusca1, CejuscDisponibilidadedt.getId_UsuCejusc(), PosicaoPaginaAtual);												
						enviarJSON(response, stTemp);						
						
						return;
					}
				} else {
					super.exibaMensagemInconsistenciaErro(request, "Selecione primeiramente o Usuário CEJUSC.");
				}
				
				break;
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (CejuscDisponibilidadedt.getId_AudiTipo() != null && CejuscDisponibilidadedt.getId_AudiTipo().trim().length() > 0) {
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Serventia"};
						String[] lisDescricao = {"Serventia", "UF"};
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
											
						enviarJSON(response, stTemp);						
						
						return;
					}
				} else {
					super.exibaMensagemInconsistenciaErro(request, "Selecione primeiramente o Tipo da Audiência.");
				}
				
				break;
			case Configuracao.SalvarResultado: 
				Mensagem=CejuscDisponibilidadene.Verificar(CejuscDisponibilidadedt); 
				if (Mensagem.length()==0){
					CejuscDisponibilidadene.salvar(CejuscDisponibilidadedt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case Configuracao.ExcluirResultado: //Excluir
				CejuscDisponibilidadene.excluir(CejuscDisponibilidadedt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;			
//--------------------------------------------------------------------------------//
			default:
				if (paginaAnterior == (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
					CejuscDisponibilidadeDt cejuscDisponibilidade = CejuscDisponibilidadene.consultarUsuarioCejuscDt(CejuscDisponibilidadedt.getId_UsuCejusc(), CejuscDisponibilidadedt.getId_AudiTipo(), CejuscDisponibilidadedt.getId_Serv());
					if (cejuscDisponibilidade != null) CejuscDisponibilidadedt = cejuscDisponibilidade;
				} else {
					stId = request.getParameter("Id_CejuscDisponibilidade");
					if (stId != null && !stId.isEmpty()) {
						if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( CejuscDisponibilidadedt.getId()))){
							CejuscDisponibilidadedt.limpar();
							CejuscDisponibilidadedt = CejuscDisponibilidadene.consultarId(stId);
						}
					}
				}
				break;
		}

		usuarioDt = CejuscDisponibilidadene.consultarUsuarioDt(CejuscDisponibilidadedt.getId_UsuCejusc());
		UsuarioCejuscDt usuarioCejuscDt = null;
		if(usuarioDt != null && !usuarioDt.getId().isEmpty()) {
			usuarioCejuscDt =  CejuscDisponibilidadene.consultarUsuarioCejuscDtIdUsuario(usuarioDt.getId());
		}
		
		request.getSession().setAttribute("email", usuarioDt.getEMail());
		request.getSession().setAttribute("telefone", usuarioDt.getTelefone() );
		request.getSession().setAttribute("celular", usuarioDt.getCelular() );
		if(usuarioCejuscDt != null) {
			request.getSession().setAttribute("situacao", usuarioCejuscDt.getCodigoStatusAtual().equals(UsuarioCejuscDt.CODIGO_STATUS_APROVADO) ? "APROVADO" : "NÃO APROVADO");
		}
		request.getSession().setAttribute("CejuscDisponibilidadedt",CejuscDisponibilidadedt );
		request.getSession().setAttribute("CejuscDisponibilidadene",CejuscDisponibilidadene );
		
		request.setAttribute("exibeConsultaUsuarioCejusc", new Boolean(Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.PREPROCESSUAL) || (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.NUPEMEC));
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
