package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PrazoSuspensoTipoDt;
import br.gov.go.tj.projudi.ne.PrazoSuspensoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PrazoSuspensoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1114889728229776940L;

    public  PrazoSuspensoTipoCtGen() {

	} 
		public int Permissao(){
			return PrazoSuspensoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PrazoSuspensoTipoDt PrazoSuspensoTipodt;
		PrazoSuspensoTipoNe PrazoSuspensoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PrazoSuspensoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PrazoSuspensoTipo");




		PrazoSuspensoTipone =(PrazoSuspensoTipoNe)request.getSession().getAttribute("PrazoSuspensoTipone");
		if (PrazoSuspensoTipone == null )  PrazoSuspensoTipone = new PrazoSuspensoTipoNe();  


		PrazoSuspensoTipodt =(PrazoSuspensoTipoDt)request.getSession().getAttribute("PrazoSuspensoTipodt");
		if (PrazoSuspensoTipodt == null )  PrazoSuspensoTipodt = new PrazoSuspensoTipoDt();  

		PrazoSuspensoTipodt.setPrazoSuspensoTipo( request.getParameter("PrazoSuspensoTipo")); 
		PrazoSuspensoTipodt.setPrazoSuspensoTipoCodigo( request.getParameter("PrazoSuspensoTipoCodigo")); 

		PrazoSuspensoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PrazoSuspensoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PrazoSuspensoTipone.excluir(PrazoSuspensoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/PrazoSuspensoTipoLocalizar.jsp";
				request.setAttribute("tempBuscaId_PrazoSuspensoTipo","Id_PrazoSuspensoTipo");
				request.setAttribute("tempBuscaPrazoSuspensoTipo","PrazoSuspensoTipo");
				request.setAttribute("tempRetorno","PrazoSuspensoTipo");
				tempList =PrazoSuspensoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaPrazoSuspensoTipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", PrazoSuspensoTipone.getQuantidadePaginas());
					PrazoSuspensoTipodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				PrazoSuspensoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PrazoSuspensoTipone.Verificar(PrazoSuspensoTipodt); 
					if (Mensagem.length()==0){
						PrazoSuspensoTipone.salvar(PrazoSuspensoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PrazoSuspensoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PrazoSuspensoTipodt.getId()))){
						PrazoSuspensoTipodt.limpar();
						PrazoSuspensoTipodt = PrazoSuspensoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PrazoSuspensoTipodt",PrazoSuspensoTipodt );
		request.getSession().setAttribute("PrazoSuspensoTipone",PrazoSuspensoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
