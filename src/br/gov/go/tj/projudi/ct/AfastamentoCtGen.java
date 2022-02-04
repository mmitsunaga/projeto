package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.projudi.ne.AfastamentoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AfastamentoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 1747238798412131461L;

    public  AfastamentoCtGen() {

	} 
		public int Permissao(){
			return AfastamentoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AfastamentoDt Afastamentodt;
		AfastamentoNe Afastamentone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Afastamento.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Afastamento");
		request.setAttribute("tempBuscaId_Afastamento","Id_Afastamento");
		request.setAttribute("tempBuscaAfastamento","Afastamento");

		request.setAttribute("tempRetorno","Afastamento");



		Afastamentone =(AfastamentoNe)request.getSession().getAttribute("Afastamentone");
		if (Afastamentone == null )  Afastamentone = new AfastamentoNe();  


		Afastamentodt =(AfastamentoDt)request.getSession().getAttribute("Afastamentodt");
		if (Afastamentodt == null )  Afastamentodt = new AfastamentoDt();  

		Afastamentodt.setAfastamento( request.getParameter("Afastamento")); 


		Afastamentodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Afastamentodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Afastamentone.excluir(Afastamentodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AfastamentoLocalizar.jsp";
				tempList =Afastamentone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAfastamento", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Afastamentone.getQuantidadePaginas());
					Afastamentodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.LocalizarDWR: 
				break;
			case Configuracao.Novo: 
				Afastamentodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Afastamentone.Verificar(Afastamentodt); 
				if (Mensagem.length()==0){
					Afastamentone.salvar(Afastamentodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Afastamento");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Afastamentodt.getId()))){
						Afastamentodt.limpar();
						Afastamentodt = Afastamentone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Afastamentodt",Afastamentodt );
		request.getSession().setAttribute("Afastamentone",Afastamentone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
