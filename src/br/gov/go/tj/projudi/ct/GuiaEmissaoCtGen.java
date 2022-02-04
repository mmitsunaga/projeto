package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GuiaEmissaoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 975764380320815522L;

	public  GuiaEmissaoCtGen() {

	} 
		public int Permissao(){
			return GuiaEmissaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GuiaEmissaoDt GuiaEmissaodt;
		GuiaEmissaoNe GuiaEmissaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/GuiaEmissao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GuiaEmissao");




		GuiaEmissaone =(GuiaEmissaoNe)request.getSession().getAttribute("GuiaEmissaone");
		if (GuiaEmissaone == null )  GuiaEmissaone = new GuiaEmissaoNe();  


		GuiaEmissaodt =(GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaodt");
		if (GuiaEmissaodt == null )  GuiaEmissaodt = new GuiaEmissaoDt();  

		GuiaEmissaodt.setGuiaEmissao( request.getParameter("GuiaEmissao")); 
		GuiaEmissaodt.setId_GuiaModelo( request.getParameter("Id_GuiaModelo")); 
		GuiaEmissaodt.setGuiaModelo( request.getParameter("GuiaModelo")); 
		GuiaEmissaodt.setId_Processo( request.getParameter("Id_Processo")); 
		GuiaEmissaodt.setId_Serventia( request.getParameter("Id_Serventia")); 
		GuiaEmissaodt.setServentia( request.getParameter("Serventia")); 
		GuiaEmissaodt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		GuiaEmissaodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 

		GuiaEmissaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GuiaEmissaodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					GuiaEmissaone.excluir(GuiaEmissaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/GuiaEmissaoLocalizar.jsp";
				request.setAttribute("tempBuscaId_GuiaEmissao","Id_GuiaEmissao");
				request.setAttribute("tempBuscaGuiaEmissao","GuiaEmissao");
				request.setAttribute("tempRetorno","GuiaEmissao");
				tempList =GuiaEmissaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGuiaEmissao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", GuiaEmissaone.getQuantidadePaginas());
					GuiaEmissaodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				GuiaEmissaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=GuiaEmissaone.Verificar(GuiaEmissaodt); 
					if (Mensagem.length()==0){
						GuiaEmissaone.salvar(GuiaEmissaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (GuiaModeloDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_GuiaModelo","Id_GuiaModelo");
					request.setAttribute("tempBuscaGuiaModelo","GuiaModelo");
					request.setAttribute("tempRetorno","GuiaEmissao");
					stAcao="/WEB-INF/jsptjgo/GuiaModeloLocalizar.jsp";
					tempList =GuiaEmissaone.consultarDescricaoGuiaModelo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaGuiaModeloa", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", GuiaEmissaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Processo","Id_Processo");
					request.setAttribute("tempBuscaProcesso","Processo");
					request.setAttribute("tempRetorno","GuiaEmissao");
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
					tempList =GuiaEmissaone.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcesso", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", GuiaEmissaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
					request.setAttribute("tempBuscaServentia","Serventia");
					request.setAttribute("tempRetorno","GuiaEmissao");
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
					tempList =GuiaEmissaone.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", GuiaEmissaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_GuiaEmissao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( GuiaEmissaodt.getId()))){
						GuiaEmissaodt.limpar();
						GuiaEmissaodt = GuiaEmissaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("GuiaEmissaodt",GuiaEmissaodt );
		request.getSession().setAttribute("GuiaEmissaone",GuiaEmissaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
