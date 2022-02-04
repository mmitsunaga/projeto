package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.PaisDt;
import br.gov.go.tj.projudi.ne.EstadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EstadoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 8640483870270814886L;

    public  EstadoCtGen() {

	} 
		public int Permissao(){
			return EstadoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EstadoDt Estadodt;
		EstadoNe Estadone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Estado.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Estado");				

		Estadone =(EstadoNe)request.getSession().getAttribute("Estadone");
		if (Estadone == null )  Estadone = new EstadoNe();  

		Estadodt =(EstadoDt)request.getSession().getAttribute("Estadodt");
		if (Estadodt == null )  Estadodt = new EstadoDt();  

		Estadodt.setEstado( request.getParameter("Estado")); 
		Estadodt.setEstadoCodigo( request.getParameter("EstadoCodigo")); 
		Estadodt.setId_Pais( request.getParameter("Id_Pais")); 
		Estadodt.setPais( request.getParameter("Pais")); 
		Estadodt.setUf( request.getParameter("Uf")); 
		Estadodt.setPaisCodigo( request.getParameter("PaisCodigo")); 

		Estadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Estadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Estadone.excluir(Estadodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
			    request.setAttribute("tempBuscaId_Estado","Id_Estado");
			    request.setAttribute("tempBuscaEstado","Estado");
			    request.setAttribute("tempRetorno","Estado");
				tempList =Estadone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaEstado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Estadone.getQuantidadePaginas());
					Estadodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Estadodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Estadone.Verificar(Estadodt); 
					if (Mensagem.length()==0){
						Estadone.salvar(Estadodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (PaisDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Estado");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Estadodt.getId()))){
						Estadodt.limpar();
						Estadodt = Estadone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Estadodt",Estadodt );
		request.getSession().setAttribute("Estadone",Estadone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
