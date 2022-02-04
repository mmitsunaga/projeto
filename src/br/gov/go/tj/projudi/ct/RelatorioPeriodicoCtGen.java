package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt;
import br.gov.go.tj.projudi.ne.RelatorioPeriodicoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioPeriodicoCtGen extends Controle implements Serializable{

	private static final long serialVersionUID = 589169729500617706L;

	public  RelatorioPeriodicoCtGen() {

	} 
		public int Permissao(){
			return RelatorioPeriodicoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioPeriodicoDt RelatorioPeriodicodt;
		RelatorioPeriodicoNe RelatorioPeriodicone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/RelatorioPeriodico.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","RelatorioPeriodico");




		RelatorioPeriodicone =(RelatorioPeriodicoNe)request.getSession().getAttribute("RelatorioPeriodicone");
		if (RelatorioPeriodicone == null )  RelatorioPeriodicone = new RelatorioPeriodicoNe();  


		RelatorioPeriodicodt =(RelatorioPeriodicoDt)request.getSession().getAttribute("RelatorioPeriodicodt");
		if (RelatorioPeriodicodt == null )  RelatorioPeriodicodt = new RelatorioPeriodicoDt();  

		RelatorioPeriodicodt.setRelatorioPeriodico( request.getParameter("RelatorioPeriodico")); 
		RelatorioPeriodicodt.setCodigoSql( request.getParameter("CodigoSql")); 
		RelatorioPeriodicodt.setCamposSql( request.getParameter("CamposSql")); 

		RelatorioPeriodicodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RelatorioPeriodicodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					RelatorioPeriodicone.excluir(RelatorioPeriodicodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/RelatorioPeriodicoLocalizar.jsp";
				request.setAttribute("tempBuscaId_RelatorioPeriodico","Id_RelatorioPeriodico");
				request.setAttribute("tempBuscaRelatorioPeriodico","RelatorioPeriodico");
				request.setAttribute("tempRetorno","RelatorioPeriodico");
				tempList =RelatorioPeriodicone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaRelatorioPeriodico", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", RelatorioPeriodicone.getQuantidadePaginas());
					RelatorioPeriodicodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				RelatorioPeriodicodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=RelatorioPeriodicone.Verificar(RelatorioPeriodicodt); 
					if (Mensagem.length()==0){
						RelatorioPeriodicone.salvar(RelatorioPeriodicodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_RelatorioPeriodico");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( RelatorioPeriodicodt.getId()))){
						RelatorioPeriodicodt.limpar();
						RelatorioPeriodicodt = RelatorioPeriodicone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("RelatorioPeriodicodt",RelatorioPeriodicodt );
		request.getSession().setAttribute("RelatorioPeriodicone",RelatorioPeriodicone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
