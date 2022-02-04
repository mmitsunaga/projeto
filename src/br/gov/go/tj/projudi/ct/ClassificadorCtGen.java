package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ClassificadorNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ClassificadorCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -2662890647481144770L;

    public  ClassificadorCtGen() {

	} 
		public int Permissao(){
			return ClassificadorDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ClassificadorDt Classificadordt;
		ClassificadorNe Classificadorne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Classificador.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Classificador");
		request.setAttribute("tempBuscaId_Classificador","Id_Classificador");
		request.setAttribute("tempBuscaClassificador","Classificador");
		
		request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
		request.setAttribute("tempBuscaServentia","Serventia");

		request.setAttribute("tempRetorno","Classificador");



		Classificadorne =(ClassificadorNe)request.getSession().getAttribute("Classificadorne");
		if (Classificadorne == null )  Classificadorne = new ClassificadorNe();  


		Classificadordt =(ClassificadorDt)request.getSession().getAttribute("Classificadordt");
		if (Classificadordt == null )  Classificadordt = new ClassificadorDt();  

		Classificadordt.setClassificador( request.getParameter("Classificador")); 
		Classificadordt.setId_Serventia( request.getParameter("Id_Serventia")); 
		Classificadordt.setServentia( request.getParameter("Serventia")); 
		Classificadordt.setPrioridade( request.getParameter("Prioridade")); 
		Classificadordt.setServentiaCodigo( request.getParameter("ServentiaCodigo")); 

		Classificadordt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Classificadordt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Classificadorne.excluir(Classificadordt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				Classificadordt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Classificadorne.Verificar(Classificadordt); 
				if (Mensagem.length()==0){
					Classificadorne.salvar(Classificadordt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
						tempList =Classificadorne.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Classificadorne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Classificador");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Classificadordt.getId()))){
						Classificadordt.limpar();
						Classificadordt = Classificadorne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Classificadordt",Classificadordt );
		request.getSession().setAttribute("Classificadorne",Classificadorne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
