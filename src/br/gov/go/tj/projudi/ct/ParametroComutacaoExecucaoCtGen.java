package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt;
import br.gov.go.tj.projudi.ne.ParametroComutacaoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ParametroComutacaoExecucaoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2053047571185414054L;

	public  ParametroComutacaoExecucaoCtGen() {

	} 
		public int Permissao(){
			return ParametroComutacaoExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ParametroComutacaoExecucaoDt ParametroComutacaoExecucaodt;
		ParametroComutacaoExecucaoNe ParametroComutacaoExecucaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ParametroComutacaoExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Parametro das Comutacoes");




		ParametroComutacaoExecucaone =(ParametroComutacaoExecucaoNe)request.getSession().getAttribute("ParametroComutacaoExecucaone");
		if (ParametroComutacaoExecucaone == null )  ParametroComutacaoExecucaone = new ParametroComutacaoExecucaoNe();  


		ParametroComutacaoExecucaodt =(ParametroComutacaoExecucaoDt)request.getSession().getAttribute("ParametroComutacaoExecucaodt");
		if (ParametroComutacaoExecucaodt == null )  ParametroComutacaoExecucaodt = new ParametroComutacaoExecucaoDt();  

		ParametroComutacaoExecucaodt.setDataDecreto( request.getParameter("DataDecreto")); 
		ParametroComutacaoExecucaodt.setFracaoHediondo( request.getParameter("FracaoHediondo")); 
		ParametroComutacaoExecucaodt.setFracaoComum( request.getParameter("FracaoComum")); 
		ParametroComutacaoExecucaodt.setFracaoComumReinc( request.getParameter("FracaoComumReinc")); 
//		if (request.getParameter("PenaUnificada") != null)
			ParametroComutacaoExecucaodt.setPenaUnificada( request.getParameter("PenaUnificada")); 
//		else ParametroComutacaoExecucaodt.setPenaUnificada("false");

		ParametroComutacaoExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ParametroComutacaoExecucaodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ParametroComutacaoExecucaone.excluir(ParametroComutacaoExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ParametroComutacaoExecucaoLocalizar.jsp";
				request.setAttribute("tempBuscaId_ParametroComutacaoExecucao","Id_ParametroComutacaoExecucao");
				request.setAttribute("tempBuscaParametroComutacaoExecucao","ParametroComutacaoExecucao");
				request.setAttribute("tempRetorno","ParametroComutacaoExecucao");
				tempList =ParametroComutacaoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaParametroComutacaoExecucao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ParametroComutacaoExecucaone.getQuantidadePaginas());
					ParametroComutacaoExecucaodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ParametroComutacaoExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ParametroComutacaoExecucaone.Verificar(ParametroComutacaoExecucaodt); 
					if (Mensagem.length()==0){
						ParametroComutacaoExecucaone.salvar(ParametroComutacaoExecucaodt); 
						request.setAttribute("MensagemOk", "Dados salvos com sucesso!"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ParametroComutacaoExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ParametroComutacaoExecucaodt.getId()))){
						ParametroComutacaoExecucaodt.limpar();
						ParametroComutacaoExecucaodt = ParametroComutacaoExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ParametroComutacaoExecucaodt",ParametroComutacaoExecucaodt );
		request.getSession().setAttribute("ParametroComutacaoExecucaone",ParametroComutacaoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
