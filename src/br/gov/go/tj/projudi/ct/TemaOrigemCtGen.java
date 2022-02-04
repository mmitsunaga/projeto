package br.gov.go.tj.projudi.ct;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.TemaOrigemNe;
import br.gov.go.tj.projudi.dt.TemaOrigemDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class TemaOrigemCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3671817402738474385L;

	public  TemaOrigemCtGen() {

	} 
		public int Permissao() {
			return TemaOrigemDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TemaOrigemDt TemaOrigemdt;
		TemaOrigemNe TemaOrigemne;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/TemaOrigem.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descri��o das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma vari�vel para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","TemaOrigem");




		TemaOrigemne =(TemaOrigemNe)request.getSession().getAttribute("TemaOrigemne");
		if (TemaOrigemne == null )  TemaOrigemne = new TemaOrigemNe();  


		TemaOrigemdt =(TemaOrigemDt)request.getSession().getAttribute("TemaOrigemdt");
		if (TemaOrigemdt == null )  TemaOrigemdt = new TemaOrigemDt();  

		TemaOrigemdt.setTemaOrigemCodigo( request.getParameter("TemaOrigemCodigo")); 
		TemaOrigemdt.setTemaOrigem( request.getParameter("TemaOrigem")); 

		TemaOrigemdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		TemaOrigemdt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//� a p�gina padr�o
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					TemaOrigemne.excluir(TemaOrigemdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"TemaOrigem"};
					String[] lisDescricao = {"TemaOrigem"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaOrigem");
					request.setAttribute("tempBuscaDescricao","TemaOrigem");
					request.setAttribute("tempBuscaPrograma","TemaOrigem");
					request.setAttribute("tempRetorno","TemaOrigem");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = TemaOrigemne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				TemaOrigemdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=TemaOrigemne.Verificar(TemaOrigemdt); 
					if (Mensagem.length()==0){
						TemaOrigemne.salvar(TemaOrigemdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			default:
				stId = request.getParameter("Id_TemaOrigem");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( TemaOrigemdt.getId()))){
						TemaOrigemdt.limpar();
						TemaOrigemdt = TemaOrigemne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("TemaOrigemdt",TemaOrigemdt );
		request.getSession().setAttribute("TemaOrigemne",TemaOrigemne );
		
		request.getSession().getAttribute("MensagemErro");

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
