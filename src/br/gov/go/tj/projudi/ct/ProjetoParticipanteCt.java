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

public class ProjetoParticipanteCt extends ProjetoParticipanteCtGen{

    private static final long serialVersionUID = -7632961756642862620L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProjetoParticipanteDt ProjetoParticipantedt;
		ProjetoParticipanteNe ProjetoParticipantene;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/ProjetoParticipante.jsp";
    	String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

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
				Mensagem = ProjetoParticipantene.salvarMultiplos(ProjetoParticipantedt, idsDados); 
				if (Mensagem.length() == 0) {
					localizar(request, ProjetoParticipantedt.getId_Projeto(), ProjetoParticipantene, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else {
					request.setAttribute("MensagemErro", Mensagem );
					ProjetoParticipantedt = new ProjetoParticipanteDt();
				}
				break;
			case (ProjetoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Projeto"};
					String[] lisDescricao = {"Projeto"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Projeto");
					request.setAttribute("tempBuscaDescricao", "Projeto");
					request.setAttribute("tempBuscaPrograma", "Projeto");			
					request.setAttribute("tempRetorno","ProjetoParticipante");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", ProjetoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
				String stTemp="";
				stTemp = ProjetoParticipantene.consultarDescricaoProjetoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
					
				
				return;			
				}
				break;
			default:
				break;
		}

		request.getSession().setAttribute("ProjetoParticipantedt",ProjetoParticipantedt );
		request.getSession().setAttribute("ProjetoParticipantene",ProjetoParticipantene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, ProjetoParticipanteNe objNe, UsuarioNe UsuarioSessao) throws Exception{
			String tempDados = objNe.consultarServentiaCargoProjetoUlLiCheckBox(id, UsuarioSessao.getUsuarioDt().getId_Serventia());
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
