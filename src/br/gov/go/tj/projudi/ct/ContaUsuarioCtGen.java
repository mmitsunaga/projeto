package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

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
import br.gov.go.tj.utils.Funcoes;

public class ContaUsuarioCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 3199823718131934484L;

    public  ContaUsuarioCtGen() {

	} 
		public int Permissao(){
			return ContaUsuarioDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ContaUsuarioDt ContaUsuariodt;
		ContaUsuarioNe ContaUsuarione;


		List tempList=null; 
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
		if (request.getParameter("Ativa") != null)
			ContaUsuariodt.setAtiva( request.getParameter("Ativa")); 
		else ContaUsuariodt.setAtiva("false");

		ContaUsuariodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ContaUsuariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ContaUsuarione.excluir(ContaUsuariodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ContaUsuarioLocalizar.jsp";
				tempList =ContaUsuarione.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaContaUsuario", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ContaUsuarione.getQuantidadePaginas());
					ContaUsuariodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
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
						tempList =ContaUsuarione.consultarDescricaoUsuario(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaUsuario", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ContaUsuarione.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AgenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/AgenciaLocalizar.jsp";
						tempList =ContaUsuarione.consultarDescricaoAgencia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaAgencia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ContaUsuarione.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ContaUsuario");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ContaUsuariodt.getId()))){
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
