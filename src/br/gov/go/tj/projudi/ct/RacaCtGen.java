package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.RacaNe;
import br.gov.go.tj.projudi.dt.RacaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class RacaCtGen extends Controle {


	public  RacaCtGen() {

	} 
		public int Permissao() {
			return RacaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RacaDt Racadt;
		RacaNe Racane;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Raca.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Raca");




		Racane =(RacaNe)request.getSession().getAttribute("Racane");
		if (Racane == null )  Racane = new RacaNe();  


		Racadt =(RacaDt)request.getSession().getAttribute("Racadt");
		if (Racadt == null )  Racadt = new RacaDt();  

		Racadt.setRaca( request.getParameter("Raca")); 

		Racadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Racadt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Racane.excluir(Racadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Raca"};
					String[] lisDescricao = {"Raca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Raca");
					request.setAttribute("tempBuscaDescricao","Raca");
					request.setAttribute("tempBuscaPrograma","Raca");
					request.setAttribute("tempRetorno","Raca");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Racane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				Racadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Racane.Verificar(Racadt); 
					if (Mensagem.length()==0){
						Racane.salvar(Racadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Raca");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Racadt.getId()))){
						Racadt.limpar();
						Racadt = Racane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Racadt",Racadt );
		request.getSession().setAttribute("Racane",Racane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
