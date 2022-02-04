package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.ne.GuiaItemNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GuiaItemCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1051458589313858029L;

	public  GuiaItemCtGen() {

	} 
		public int Permissao(){
			return GuiaItemDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GuiaItemDt GuiaItemdt;
		GuiaItemNe GuiaItemne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/GuiaItem.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GuiaItem");




		GuiaItemne =(GuiaItemNe)request.getSession().getAttribute("GuiaItemne");
		if (GuiaItemne == null )  GuiaItemne = new GuiaItemNe();  


		GuiaItemdt =(GuiaItemDt)request.getSession().getAttribute("GuiaItemdt");
		if (GuiaItemdt == null )  GuiaItemdt = new GuiaItemDt();  

		GuiaItemdt.setGuiaItem( request.getParameter("GuiaItem")); 
		GuiaItemdt.setId_GuiaEmissao( request.getParameter("Id_GuiaEmissao")); 
		GuiaItemdt.setId_Custa( request.getParameter("Id_Custa")); 
		GuiaItemdt.setCusta( request.getParameter("Custa")); 
		GuiaItemdt.setGuiaItemCodigo( request.getParameter("GuiaItemCodigo")); 
		GuiaItemdt.setQuantidade( request.getParameter("Quantidade")); 
		GuiaItemdt.setValorCalculado( request.getParameter("ValorCalculado")); 
		GuiaItemdt.setValorReferencia( request.getParameter("ValorReferencia")); 
		GuiaItemdt.setParcelas( request.getParameter("Parcelas")); 
		GuiaItemdt.setParcelaCorrente( request.getParameter("ParcelaCorrente")); 

		GuiaItemdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GuiaItemdt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					GuiaItemne.excluir(GuiaItemdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/GuiaItemLocalizar.jsp";
				request.setAttribute("tempBuscaId_GuiaItem","Id_GuiaItem");
				request.setAttribute("tempBuscaGuiaItem","GuiaItem");
				request.setAttribute("tempRetorno","GuiaItem");
				tempList =GuiaItemne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGuiaItem", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", GuiaItemne.getQuantidadePaginas());
					GuiaItemdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				GuiaItemdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=GuiaItemne.Verificar(GuiaItemdt); 
					if (Mensagem.length()==0){
						GuiaItemne.salvar(GuiaItemdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (GuiaEmissaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_GuiaEmissao","Id_GuiaEmissao");
					request.setAttribute("tempBuscaGuiaEmissao","GuiaEmissao");
					request.setAttribute("tempRetorno","GuiaItem");
					stAcao="/WEB-INF/jsptjgo/GuiaEmissaoLocalizar.jsp";
					tempList =GuiaItemne.consultarDescricaoGuiaEmissao(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaGuiaEmissao", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", GuiaItemne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (CustaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Custa","Id_Custa");
					request.setAttribute("tempBuscaCusta","Custa");
					request.setAttribute("tempRetorno","GuiaItem");
					stAcao="/WEB-INF/jsptjgo/CustaLocalizar.jsp";
					tempList =GuiaItemne.consultarDescricaoCusta(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaCusta", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", GuiaItemne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_GuiaItem");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( GuiaItemdt.getId()))){
						GuiaItemdt.limpar();
						GuiaItemdt = GuiaItemne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("GuiaItemdt",GuiaItemdt );
		request.getSession().setAttribute("GuiaItemne",GuiaItemne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
