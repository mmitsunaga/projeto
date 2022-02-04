package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import br.gov.go.tj.projudi.ne.EstatisticaProdutividadeItemNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaProdutividadeItemCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -8442993416836855207L;

    public  EstatisticaProdutividadeItemCtGen() {

	} 
		public int Permissao(){
			return EstatisticaProdutividadeItemDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EstatisticaProdutividadeItemDt EstatisticaProdutividadeItemdt;
		EstatisticaProdutividadeItemNe EstatisticaProdutividadeItemne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/EstatisticaProdutividadeItem.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EstatisticaProdutividadeItem");




		EstatisticaProdutividadeItemne =(EstatisticaProdutividadeItemNe)request.getSession().getAttribute("EstatisticaProdutividadeItemne");
		if (EstatisticaProdutividadeItemne == null )  EstatisticaProdutividadeItemne = new EstatisticaProdutividadeItemNe();  


		EstatisticaProdutividadeItemdt =(EstatisticaProdutividadeItemDt)request.getSession().getAttribute("EstatisticaProdutividadeItemdt");
		if (EstatisticaProdutividadeItemdt == null )  EstatisticaProdutividadeItemdt = new EstatisticaProdutividadeItemDt();  

		EstatisticaProdutividadeItemdt.setEstatisticaProdutividadeItem( request.getParameter("EstatisticaProdutividadeItem")); 
		EstatisticaProdutividadeItemdt.setDadoCodigo( request.getParameter("DadoCodigo")); 

		EstatisticaProdutividadeItemdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EstatisticaProdutividadeItemdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					EstatisticaProdutividadeItemne.excluir(EstatisticaProdutividadeItemdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/EstatisticaProdutividadeItemLocalizar.jsp";
				request.setAttribute("tempBuscaId_EstatisticaProdutividadeItem","Id_EstatisticaProdutividadeItem");
				request.setAttribute("tempBuscaEstatisticaProdutividadeItem","EstatisticaProdutividadeItem");
				request.setAttribute("tempRetorno","EstatisticaProdutividadeItem");
				tempList =EstatisticaProdutividadeItemne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaEstatisticaProdutividadeItem", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", EstatisticaProdutividadeItemne.getQuantidadePaginas());
					EstatisticaProdutividadeItemdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				EstatisticaProdutividadeItemdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=EstatisticaProdutividadeItemne.Verificar(EstatisticaProdutividadeItemdt); 
					if (Mensagem.length()==0){
						EstatisticaProdutividadeItemne.salvar(EstatisticaProdutividadeItemdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_EstatisticaProdutividadeItem");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EstatisticaProdutividadeItemdt.getId()))){
						EstatisticaProdutividadeItemdt.limpar();
						EstatisticaProdutividadeItemdt = EstatisticaProdutividadeItemne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EstatisticaProdutividadeItemdt",EstatisticaProdutividadeItemdt );
		request.getSession().setAttribute("EstatisticaProdutividadeItemne",EstatisticaProdutividadeItemne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
