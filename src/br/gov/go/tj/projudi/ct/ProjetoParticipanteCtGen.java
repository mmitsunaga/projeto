package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.projudi.dt.ProjetoParticipanteDt;
import br.gov.go.tj.projudi.ne.ProjetoParticipanteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProjetoParticipanteCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -2629475553291181093L;

    public  ProjetoParticipanteCtGen() {

	} 
		public int Permissao(){
			return ProjetoParticipanteDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProjetoParticipanteDt ProjetoParticipantedt;
		ProjetoParticipanteNe ProjetoParticipantene;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/ProjetoParticipante.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProjetoParticipante");
		request.setAttribute("ListaUlLiProjetoParticipante","");




		ProjetoParticipantene =(ProjetoParticipanteNe)request.getSession().getAttribute("ProjetoParticipantene");
		if (ProjetoParticipantene == null )  ProjetoParticipantene = new ProjetoParticipanteNe();  


		ProjetoParticipantedt =(ProjetoParticipanteDt)request.getSession().getAttribute("ProjetoParticipantedt");
		if (ProjetoParticipantedt == null )  ProjetoParticipantedt = new ProjetoParticipanteDt();  

		ProjetoParticipantedt.setId_Projeto( request.getParameter("Id_Projeto")); 
		ProjetoParticipantedt.setProjeto( request.getParameter("Projeto")); 
		ProjetoParticipantedt.setId_ServentiaCargo( request.getParameter("Id_ServentiaCargo")); 
		ProjetoParticipantedt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		ProjetoParticipantedt.setNome( request.getParameter("Nome")); 

		ProjetoParticipantedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProjetoParticipantedt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,ProjetoParticipantedt.getId_Projeto() ,ProjetoParticipantene, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				ProjetoParticipantedt.limpar();
				request.setAttribute("ListaUlLiProjetoParticipante", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=ProjetoParticipantene.Verificar(ProjetoParticipantedt); 
				if (Mensagem.length()==0){
					ProjetoParticipantene.salvarMultiplo(ProjetoParticipantedt, idsDados); 
					localizar(request,ProjetoParticipantedt.getId_Projeto() ,ProjetoParticipantene, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (ProjetoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				request.setAttribute("tempBuscaId_Projeto","Id_Projeto");
				request.setAttribute("tempBuscaProjeto","Projeto");
				request.setAttribute("tempRetorno","ProjetoParticipante");
				tempList =ProjetoParticipantene.consultarDescricaoProjeto(tempNomeBusca, PosicaoPaginaAtual);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", ProjetoParticipantene.getQuantidadePaginas());
				if (tempList.size()>0){
					request.setAttribute("ListaProjeto", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("tempRetorno","ProjetoParticipante?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("ProjetoParticipantedt",ProjetoParticipantedt );
		request.getSession().setAttribute("ProjetoParticipantene",ProjetoParticipantene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, ProjetoParticipanteNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarServentiaCargoProjetoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiProjetoParticipante", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
