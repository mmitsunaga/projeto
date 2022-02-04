package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.PonteiroCejuscStatuNe;
import br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PonteiroCejuscStatuCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -7154263195966642958L;

	public  PonteiroCejuscStatuCtGen() { 

	} 
		public int Permissao(){
			return PonteiroCejuscStatuDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PonteiroCejuscStatuDt PonteiroCejuscStatudt;
		PonteiroCejuscStatuNe PonteiroCejuscStatune;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PonteiroCejuscStatu.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PonteiroCejuscStatu");




		PonteiroCejuscStatune =(PonteiroCejuscStatuNe)request.getSession().getAttribute("PonteiroCejuscStatune");
		if (PonteiroCejuscStatune == null )  PonteiroCejuscStatune = new PonteiroCejuscStatuNe();  


		PonteiroCejuscStatudt =(PonteiroCejuscStatuDt)request.getSession().getAttribute("PonteiroCejuscStatudt");
		if (PonteiroCejuscStatudt == null )  PonteiroCejuscStatudt = new PonteiroCejuscStatuDt();  

		PonteiroCejuscStatudt.setPonteiroCejuscStatus( request.getParameter("PonteiroCejuscStatus")); 

		PonteiroCejuscStatudt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PonteiroCejuscStatudt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PonteiroCejuscStatune.excluir(PonteiroCejuscStatudt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PonteiroCejuscStatu"};
					String[] lisDescricao = {"PonteiroCejuscStatu"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PonteiroCejuscStatus");
					request.setAttribute("tempBuscaDescricao","PonteiroCejuscStatu");
					request.setAttribute("tempBuscaPrograma","PonteiroCejuscStatu");
					request.setAttribute("tempRetorno","PonteiroCejuscStatu");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PonteiroCejuscStatune.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				PonteiroCejuscStatudt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PonteiroCejuscStatune.Verificar(PonteiroCejuscStatudt); 
					if (Mensagem.length()==0){
						PonteiroCejuscStatune.salvar(PonteiroCejuscStatudt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PonteiroCejuscStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PonteiroCejuscStatudt.getId()))){
						PonteiroCejuscStatudt.limpar();
						PonteiroCejuscStatudt = PonteiroCejuscStatune.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PonteiroCejuscStatudt",PonteiroCejuscStatudt );
		request.getSession().setAttribute("PonteiroCejuscStatune",PonteiroCejuscStatune );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
