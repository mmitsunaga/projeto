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
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class EstatisticaProdutividadeItemCt extends EstatisticaProdutividadeItemCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = -8723334908646199345L;

    public  EstatisticaProdutividadeItemCt() {

	} 
		public int Permissao() {
			return EstatisticaProdutividadeItemDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		EstatisticaProdutividadeItemDt estatisticaProdutividadeItemDt;
		EstatisticaProdutividadeItemNe estatisticaProdutividadeItemNe;

		//List lisNomeBusca = null;
		//List lisDescricao = null;
		String stNomeBusca1 = "";
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
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		estatisticaProdutividadeItemNe =(EstatisticaProdutividadeItemNe)request.getSession().getAttribute("EstatisticaProdutividadeItemne");
		if (estatisticaProdutividadeItemNe == null )  estatisticaProdutividadeItemNe = new EstatisticaProdutividadeItemNe();  


		estatisticaProdutividadeItemDt =(EstatisticaProdutividadeItemDt)request.getSession().getAttribute("EstatisticaProdutividadeItemdt");
		if (estatisticaProdutividadeItemDt == null )  estatisticaProdutividadeItemDt = new EstatisticaProdutividadeItemDt();  

		estatisticaProdutividadeItemDt.setEstatisticaProdutividadeItem( request.getParameter("EstatisticaProdutividadeItem")); 
		estatisticaProdutividadeItemDt.setDadoCodigo( request.getParameter("DadoCodigo")); 

		estatisticaProdutividadeItemDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		estatisticaProdutividadeItemDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					estatisticaProdutividadeItemNe.excluir(estatisticaProdutividadeItemDt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				Integer tipoArquivo = new Integer(request.getParameter("tipoArquivo"));
				byte[] byTemp = estatisticaProdutividadeItemNe.relEstatisticaProdutividade(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , estatisticaProdutividadeItemDt.getEstatisticaProdutividadeItem(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
				// Se o parâmertro tipo_Arquivo for setado e igual a 2, significa que o relatório deve ser um
				// arquivo TXT. Algumas telas não tem esse parâmetro setado no request, logo é gerado um PDF.
				if (tipoArquivo != null && tipoArquivo.equals(2)) {					
					enviarTXT(response,byTemp, "Relatorio");
				} else {					
					enviarPDF(response,byTemp, "Relatorio");
				}
				return;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Item de Produtividade"};
					String[] lisDescricao = {"Item de Produtividade"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EstatisticaProdutividadeItem");
					request.setAttribute("tempBuscaDescricao", "EstatisticaProdutividadeItem");
					request.setAttribute("tempBuscaPrograma", "Estatística de Produtividade"); 
					request.setAttribute("tempRetorno", "RelatorioSumarioProdutividade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = estatisticaProdutividadeItemNe.consultarDescricaoJSON(stNomeBusca1, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.Novo: 
				estatisticaProdutividadeItemDt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=estatisticaProdutividadeItemNe.Verificar(estatisticaProdutividadeItemDt); 
					if (Mensagem.length()==0){
						estatisticaProdutividadeItemNe.salvar(estatisticaProdutividadeItemDt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case Configuracao.Curinga6:
				//Curinga de acesso à tela de listagem de EstatisticaProdutividadeItem
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				request.setAttribute("tempPrograma","Listagem de Estatística Produtividade Item");
				stAcao = "WEB-INF/jsptjgo/ListagemEstatisticaProdutividadeItem.jsp";
				request.getSession().setAttribute("stAcaoRetorno", stAcao);
				estatisticaProdutividadeItemDt.limpar();
			break;
			case Configuracao.Curinga7:
				//Curinga destinado à listagem de EstatisticaProdutividadeItem
				if (request.getParameter("EstatisticaProdutividadeItem") != null) {
					if (request.getParameter("EstatisticaProdutividadeItem").equals("null")) {
						estatisticaProdutividadeItemDt.setEstatisticaProdutividadeItem("");
					} else {
						estatisticaProdutividadeItemDt.setEstatisticaProdutividadeItem(request.getParameter("EstatisticaProdutividadeItem"));
					}
				}
				stAcao="/WEB-INF/jsptjgo/ListagemEstatisticaProdutividadeItem.jsp";
				tempList = estatisticaProdutividadeItemNe.consultarDescricao(estatisticaProdutividadeItemDt.getEstatisticaProdutividadeItem(), posicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("listaEstatisticaProdutividadeItem", tempList); 
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", estatisticaProdutividadeItemNe.getQuantidadePaginas());
					request.setAttribute("PaginaAtual", Configuracao.Curinga7);
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				request.setAttribute("tempPrograma","Listagem de Estatística Produtividade Item");
			break;
			default:
				stId = request.getParameter("Id_EstatisticaProdutividadeItem");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( estatisticaProdutividadeItemDt.getId()))){
						estatisticaProdutividadeItemDt.limpar();
						estatisticaProdutividadeItemDt = estatisticaProdutividadeItemNe.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EstatisticaProdutividadeItemdt",estatisticaProdutividadeItemDt );
		request.getSession().setAttribute("EstatisticaProdutividadeItemne",estatisticaProdutividadeItemNe );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
