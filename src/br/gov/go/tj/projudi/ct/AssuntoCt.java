package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.ne.AssuntoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

//---------------------------------------------------------
public class AssuntoCt extends AssuntoCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6924110363478946420L;
	/**
	 * 
	 */	
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AssuntoDt Assuntodt;
		AssuntoNe Assuntone;

		String stNomeBusca1="";
		String stNomeBusca2="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Assunto.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Assunto");

		Assuntone =(AssuntoNe)request.getSession().getAttribute("Assuntone");
		if (Assuntone == null )  Assuntone = new AssuntoNe();  


		Assuntodt =(AssuntoDt)request.getSession().getAttribute("Assuntodt");
		if (Assuntodt == null )  {
			Assuntodt = new AssuntoDt();
			Assuntodt.setIsAtivo("1");
		}

		Assuntodt.setAssunto( request.getParameter("Assunto")); 
		Assuntodt.setAssuntoCodigo( request.getParameter("AssuntoCodigo")); 
		Assuntodt.setId_AssuntoPai( request.getParameter("Id_AssuntoPai")); 
		Assuntodt.setAssuntoPai( request.getParameter("AssuntoPai")); 
		if (request.getParameter("IsAtivo")==null){
			Assuntodt.setIsAtivo("0");
		}else{
			Assuntodt.setIsAtivo("1");
		}
		Assuntodt.setDispositivoLegal( request.getParameter("DispositivoLegal")); 
		Assuntodt.setArtigo( request.getParameter("Artigo")); 
		Assuntodt.setId_CnjAssunto( request.getParameter("Id_CnjAssunto")); 
		Assuntodt.setSigla( request.getParameter("Sigla")); 

		Assuntodt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		Assuntodt.setIpComputadorLog(request.getRemoteAddr());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				Assuntone.excluir(Assuntodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Assunto", "Código CNJ"};
					String[] lisDescricao = {"Assunto", "Código CNJ"};
					
					String id_retorno_busca =  request.getParameter("id_retorno_busca");
					String descricao_retorno_busca = "AssuntoPai";
					if (id_retorno_busca==null || id_retorno_busca.length()==0 ){
						id_retorno_busca = "Id_Assunto";
						descricao_retorno_busca = "Assunto";
					}
					
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					request.setAttribute("tempBuscaId", id_retorno_busca);					
					request.setAttribute("tempBuscaDescricao",descricao_retorno_busca);
					
					request.setAttribute("tempBuscaPrograma","Assunto");
					request.setAttribute("tempRetorno","Assunto");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Assuntone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);	
					return;
				}
				break;
			case Configuracao.Novo: 
				Assuntodt.limpar();
				Assuntodt.setIsAtivo("1");
				break;
			case Configuracao.SalvarResultado: 
				Mensagem=Assuntone.Verificar(Assuntodt); 
				if (Mensagem.length()==0){
					Assuntone.salvar(Assuntodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				} else {
					request.setAttribute("MensagemErro", Mensagem );
				}
				break;			
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Assunto");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Assuntodt.getId()))){
						Assuntodt.limpar();
						Assuntodt = Assuntone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Assuntodt",Assuntodt );
		request.getSession().setAttribute("Assuntone",Assuntone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

//

}
