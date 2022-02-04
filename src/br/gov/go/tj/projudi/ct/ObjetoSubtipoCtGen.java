package br.gov.go.tj.projudi.ct;

 import br.gov.go.tj.projudi.dt.ObjetoTipoDt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.ObjetoSubtipoNe;
import br.gov.go.tj.projudi.dt.ObjetoSubtipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ObjetoSubtipoCtGen extends Controle { 


	private static final long serialVersionUID = -1155189203080625513L;

	public  ObjetoSubtipoCtGen() { 

	} 

	public int Permissao() {
		return ObjetoSubtipoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ObjetoSubtipoDt ObjetoSubtipodt;
		ObjetoSubtipoNe ObjetoSubtipone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ObjetoSubtipo.jsp";

		request.setAttribute("tempPrograma","ObjetoSubtipo");

		ObjetoSubtipone =(ObjetoSubtipoNe)request.getSession().getAttribute("ObjetoSubtipone");
		if (ObjetoSubtipone == null )  ObjetoSubtipone = new ObjetoSubtipoNe();  


		ObjetoSubtipodt =(ObjetoSubtipoDt)request.getSession().getAttribute("ObjetoSubtipodt");
		if (ObjetoSubtipodt == null )  ObjetoSubtipodt = new ObjetoSubtipoDt();  

		ObjetoSubtipodt.setObjetoSubtipo( request.getParameter("ObjetoSubtipo")); 
		ObjetoSubtipodt.setId_ObjetoTipo( request.getParameter("Id_ObjetoTipo")); 
		ObjetoSubtipodt.setObjetoTipo( request.getParameter("ObjetoTipo")); 

		ObjetoSubtipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ObjetoSubtipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				 if (ObjetoSubtipodt.getId() != null && ObjetoSubtipodt.getId().length()>1){
					 ObjetoSubtipone.excluir(ObjetoSubtipodt); 
						request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				    } else {
						request.setAttribute("MensagemErro", "Nenhum Objeto SubTipo selecionado para exclusão!");					
					}
					 
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ObjetoSubtipo"};
					String[] lisDescricao = {"ObjetoSubtipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ObjetoSubtipo");
					request.setAttribute("tempBuscaDescricao","ObjetoSubtipo");
					request.setAttribute("tempBuscaPrograma","ObjetoSubtipo");
					request.setAttribute("tempRetorno","ObjetoSubtipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ObjetoSubtipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				ObjetoSubtipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ObjetoSubtipone.Verificar(ObjetoSubtipodt); 
					if (Mensagem.length()==0){
						ObjetoSubtipone.salvar(ObjetoSubtipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					} else	{
						request.setAttribute("MensagemErro", Mensagem );
					}
				break;
				case (ObjetoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ObjetoTipo"};
					String[] lisDescricao = {"ObjetoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ObjetoTipo");
					request.setAttribute("tempBuscaDescricao","ObjetoTipo");
					request.setAttribute("tempBuscaPrograma","ObjetoTipo");
					request.setAttribute("tempRetorno","ObjetoSubtipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ObjetoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ObjetoSubtipone.consultarDescricaoObjetoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
			default:
				stId = request.getParameter("Id_ObjetoSubtipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ObjetoSubtipodt.getId()))){
						ObjetoSubtipodt.limpar();
						ObjetoSubtipodt = ObjetoSubtipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ObjetoSubtipodt",ObjetoSubtipodt );
		request.getSession().setAttribute("ObjetoSubtipone",ObjetoSubtipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
