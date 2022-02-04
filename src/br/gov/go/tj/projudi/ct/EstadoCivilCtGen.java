package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.ne.EstadoCivilNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EstadoCivilCtGen extends Controle {

    private static final long serialVersionUID = 8576601542871022862L;

    public  EstadoCivilCtGen() {

	} 
		public int Permissao() {
			return EstadoCivilDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EstadoCivilDt EstadoCivildt;
		EstadoCivilNe EstadoCivilne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/EstadoCivil.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EstadoCivil");
		request.setAttribute("tempBuscaId_EstadoCivil","Id_EstadoCivil");
		request.setAttribute("tempBuscaEstadoCivil","EstadoCivil");
		

		request.setAttribute("tempRetorno","EstadoCivil");



		EstadoCivilne =(EstadoCivilNe)request.getSession().getAttribute("EstadoCivilne");
		if (EstadoCivilne == null )  EstadoCivilne = new EstadoCivilNe();  


		EstadoCivildt =(EstadoCivilDt)request.getSession().getAttribute("EstadoCivildt");
		if (EstadoCivildt == null )  EstadoCivildt = new EstadoCivilDt();  

		EstadoCivildt.setEstadoCivil( request.getParameter("EstadoCivil")); 

		EstadoCivildt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EstadoCivildt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				EstadoCivilne.excluir(EstadoCivildt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Estado Civil"};
					String[] lisDescricao = {"Estado Civil"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPemissao = String.valueOf(EstadoCivilDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_EstadoCivil", "EstadoCivil", "EstadoCivil", "ProcessoParte", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
				}else{
					String stTemp = "";
					stTemp = EstadoCivilne.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					} catch(Exception e) {}
					return;
				}
			break;
			case Configuracao.Novo: 
				EstadoCivildt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=EstadoCivilne.Verificar(EstadoCivildt); 
				if (Mensagem.length()==0){
					EstadoCivilne.salvar(EstadoCivildt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_EstadoCivil");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EstadoCivildt.getId()))){
						EstadoCivildt.limpar();
						EstadoCivildt = EstadoCivilne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EstadoCivildt",EstadoCivildt );
		request.getSession().setAttribute("EstadoCivilne",EstadoCivilne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
