package br.gov.go.tj.projudi.ct;

 import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.dt.TemaOrigemDt;
import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.projudi.dt.TemaTipoDt;
import br.gov.go.tj.projudi.ne.TemaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ValidacaoUtil;

public class TemaCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2019480516195622421L;

	public  TemaCtGen() {

	} 
		public int Permissao() {
			return TemaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TemaDt Temadt;
		TemaNe Temane;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Tema.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Tema");




		Temane =(TemaNe)request.getSession().getAttribute("Temane");
		if (Temane == null )  Temane = new TemaNe();  


		Temadt =(TemaDt)request.getSession().getAttribute("Temadt");
		if (Temadt == null )  Temadt = new TemaDt();  

		Temadt.setTitulo( request.getParameter("Titulo")); 
		Temadt.setTemaCodigo( request.getParameter("TemaCodigo")); 
		Temadt.setQuesDireito( request.getParameter("QuesDireito")); 
		Temadt.setVinculantes( request.getParameter("Vinculantes")); 
		Temadt.setId_TemaSituacao( request.getParameter("Id_TemaSituacao")); 
		Temadt.setId_TemaOrigem( request.getParameter("Id_TemaOrigem")); 
		Temadt.setId_TemaTipo( request.getParameter("Id_TemaTipo")); 
		Temadt.setTemaSituacao( request.getParameter("TemaSituacao")); 
		Temadt.setTemaOrigem( request.getParameter("TemaOrigem")); 
		Temadt.setTemaTipo( request.getParameter("TemaTipo")); 
		Temadt.setDataTransito(request.getParameter("DataTransito"));
		Temadt.setOpcaoProcessual(request.getParameter("OpcaoProcessual"));	

		Temadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Temadt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Temane.excluir(Temadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema"};
					String[] lisDescricao = {"Tema"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Tema");
					request.setAttribute("tempBuscaDescricao","Tema");
					request.setAttribute("tempBuscaPrograma","Tema");
					request.setAttribute("tempRetorno","Tema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Temane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				Temadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Temane.Verificar(Temadt); 
					if (Mensagem.length()==0){
						Temane.salvar(Temadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (TemaSituacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"TemaSituacao"};
					String[] lisDescricao = {"TemaSituacao"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaSituacao");
					request.setAttribute("tempBuscaDescricao","TemaSituacao");
					request.setAttribute("tempBuscaPrograma","TemaSituacao");
					request.setAttribute("tempRetorno","Tema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (TemaSituacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Temane.consultarDescricaoTemaSituacaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
					break;
				case (TemaOrigemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"TemaOrigem"};
					String[] lisDescricao = {"TemaOrigem"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaOrigem");
					request.setAttribute("tempBuscaDescricao","TemaOrigem");
					request.setAttribute("tempBuscaPrograma","TemaOrigem");
					request.setAttribute("tempRetorno","Tema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (TemaOrigemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Temane.consultarDescricaoTemaOrigemJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
					break;
				case (TemaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"TemaTipo"};
					String[] lisDescricao = {"TemaTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaTipo");
					request.setAttribute("tempBuscaDescricao","TemaTipo");
					request.setAttribute("tempBuscaPrograma","TemaTipo");
					request.setAttribute("tempRetorno","Tema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (TemaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Temane.consultarDescricaoTemaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Tema");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Temadt.getId()))){
						Temadt.limpar();
						Temadt = Temane.consultarId(stId);
					}
				break;
		}
		
		request.setAttribute("isPodeEnviarTemaParaCNJ", ValidacaoUtil.isNaoNulo(Temadt) && ValidacaoUtil.isNaoVazio(Temadt.getId()) && (Temadt.getTemaTipoCnj().equals("IRDR") || Temadt.getTemaTipoCnj().equals("IAC")));
		
		request.getSession().setAttribute("Temadt",Temadt );
		request.getSession().setAttribute("Temane",Temane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
