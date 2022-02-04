package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.ne.CidadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CidadeCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6260594630114145486L;

    public  CidadeCtGen() {

	} 
		public int Permissao(){
			return CidadeDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CidadeDt Cidadedt;
		CidadeNe Cidadene;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Cidade.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Cidade");




		Cidadene =(CidadeNe)request.getSession().getAttribute("Cidadene");
		if (Cidadene == null )  Cidadene = new CidadeNe();  


		Cidadedt =(CidadeDt)request.getSession().getAttribute("Cidadedt");
		if (Cidadedt == null )  Cidadedt = new CidadeDt();  

		Cidadedt.setCidade( request.getParameter("Cidade")); 
		Cidadedt.setCidadeCodigo( request.getParameter("CidadeCodigo")); 
		Cidadedt.setId_Estado( request.getParameter("Id_Estado")); 
		Cidadedt.setEstado( request.getParameter("Estado")); 
		Cidadedt.setEstadoCodigo( request.getParameter("EstadoCodigo")); 
		Cidadedt.setUf( request.getParameter("Uf")); 
		Cidadedt.setCodigoSPG( request.getParameter("CodigoSPG"));

		Cidadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Cidadedt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Cidadene.excluir(Cidadedt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				Cidadedt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Cidadene.Verificar(Cidadedt); 
					if (Mensagem.length()==0){
						Cidadene.salvar(Cidadedt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Estado","Id_Estado");
					request.setAttribute("tempBuscaEstado","Estado");
					request.setAttribute("tempRetorno","Cidade");
					tempList =Cidadene.consultarDescricaoEstado(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEstado", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Cidadene.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Cidade");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Cidadedt.getId()))){
						Cidadedt.limpar();
						Cidadedt = Cidadene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Cidadedt",Cidadedt );
		request.getSession().setAttribute("Cidadene",Cidadene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
