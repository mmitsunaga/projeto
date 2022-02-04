package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AgenciaDt;
import br.gov.go.tj.projudi.dt.ContaUsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ContaUsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ValidacaoUtil;

public class ContaUsuarioCt extends ContaUsuarioCtGen{

    private static final long serialVersionUID = -1825315900402843545L;

    public int Permissao() {
		return ContaUsuarioDt.CodigoPermissao;
	}
    
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ContaUsuarioDt ContaUsuariodt;
		ContaUsuarioNe ContaUsuarione;
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
				
		String Mensagem="";
		String stId="";
		String stAcao="/WEB-INF/jsptjgo/ContaUsuario.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ContaUsuario");
		request.setAttribute("tempBuscaId_ContaUsuario","Id_ContaUsuario");
		request.setAttribute("tempBuscaOperacaoContaUsuario","OperacaoContaUsuario");
		request.setAttribute("tempBuscaContaUsuario","ContaUsuario");
		request.setAttribute("tempBuscaDvContaUsuario","DvContaUsuario");
		request.setAttribute("tempBuscaId_Usuario","Id_Usuario");
		request.setAttribute("tempBuscaUsuario","Usuario");
		request.setAttribute("tempBuscaId_Agencia","Id_Agencia");
		request.setAttribute("tempBuscaAgencia","Agencia");
		request.setAttribute("tempRetorno","ContaUsuario");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		ContaUsuarione =(ContaUsuarioNe)request.getSession().getAttribute("ContaUsuarione");
		if (ContaUsuarione == null )  ContaUsuarione = new ContaUsuarioNe();  

		ContaUsuariodt =(ContaUsuarioDt)request.getSession().getAttribute("ContaUsuariodt");
		if (ContaUsuariodt == null )  ContaUsuariodt = new ContaUsuarioDt();  
		
		ContaUsuariodt.setOperacaoContaUsuario( request.getParameter("OperacaoContaUsuario")); 
		ContaUsuariodt.setContaUsuario( request.getParameter("ContaUsuario")); 
		ContaUsuariodt.setDvContaUsuario( request.getParameter("DvContaUsuario")); 
		ContaUsuariodt.setId_Usuario( request.getParameter("Id_Usuario")); 
		ContaUsuariodt.setUsuario( request.getParameter("Usuario")); 
		ContaUsuariodt.setId_Agencia( request.getParameter("Id_Agencia")); 
		ContaUsuariodt.setAgencia( request.getParameter("Agencia")); 
		ContaUsuariodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ContaUsuariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		if (request.getParameter("Ativa") != null) ContaUsuariodt.setAtiva( request.getParameter("Ativa")); 
		else ContaUsuariodt.setAtiva("false");

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ContaUsuarione.excluir(ContaUsuariodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome Usuario"};
					String[] lisDescricao = {"Usuario", "Banco", "Codigo Agencia", "Agencia", "Operacao", "Conta", "Dv", "Ativa"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ContaUsuario");
					request.setAttribute("tempBuscaDescricao", "ContaUsuario");
					request.setAttribute("tempBuscaPrograma", "ContaUsuario");
					request.setAttribute("tempRetorno", "ContaUsuario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "ContaUsuario");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ContaUsuarione.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.Novo: 
				ContaUsuariodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=ContaUsuarione.Verificar(ContaUsuariodt); 
				if (Mensagem.length()==0){
					ContaUsuarione.salvar(ContaUsuariodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Nome","Usuario"};
					String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Usuario");
					request.setAttribute("tempBuscaDescricao", "Usuario");
					request.setAttribute("tempBuscaPrograma", "Usuario");
					request.setAttribute("tempRetorno", "ContaUsuario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ContaUsuarione.consultarTodosUsuariosJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			case (AgenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Agencia"};
					String[] lisDescricao = {"Agencia", "Codigo", "Banco"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Agencia");
					request.setAttribute("tempBuscaDescricao", "Agencia");
					request.setAttribute("tempBuscaPrograma", "Agencia");
					request.setAttribute("tempRetorno", "ContaUsuario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Agencia");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AgenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ContaUsuarione.consultarDescricaoAgenciaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ContaUsuario");
				if (!ValidacaoUtil.isVazio(stId)) {
					ContaUsuariodt.limpar();
					ContaUsuariodt = ContaUsuarione.consultarId(stId);
		    	}
			break;
		}

		request.getSession().setAttribute("ContaUsuariodt",ContaUsuariodt );
		request.getSession().setAttribute("ContaUsuarione",ContaUsuarione );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
