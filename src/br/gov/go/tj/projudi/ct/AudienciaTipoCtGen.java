package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 4653096500331732962L;

    public  AudienciaTipoCtGen() {

	} 
		public int Permissao(){
			return AudienciaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaTipoDt AudienciaTipodt;
		AudienciaTipoNe AudienciaTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/AudienciaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","AudienciaTipo");
		request.setAttribute("tempBuscaId_AudienciaTipo","Id_AudienciaTipo");
		request.setAttribute("tempBuscaAudienciaTipo","AudienciaTipo");
		

		request.setAttribute("tempRetorno","AudienciaTipo");



		AudienciaTipone =(AudienciaTipoNe)request.getSession().getAttribute("AudienciaTipone");
		if (AudienciaTipone == null )  AudienciaTipone = new AudienciaTipoNe();  


		AudienciaTipodt =(AudienciaTipoDt)request.getSession().getAttribute("AudienciaTipodt");
		if (AudienciaTipodt == null )  AudienciaTipodt = new AudienciaTipoDt();  

		AudienciaTipodt.setAudienciaTipo( request.getParameter("AudienciaTipo")); 
		AudienciaTipodt.setAudienciaTipoCodigo( request.getParameter("AudienciaTipoCodigo")); 

		AudienciaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		AudienciaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				AudienciaTipone.excluir(AudienciaTipodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AudienciaTipoLocalizar.jsp";
				tempList =AudienciaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAudienciaTipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", AudienciaTipone.getQuantidadePaginas());
					AudienciaTipodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				AudienciaTipodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=AudienciaTipone.Verificar(AudienciaTipodt); 
				if (Mensagem.length()==0){
					AudienciaTipone.salvar(AudienciaTipodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_AudienciaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( AudienciaTipodt.getId()))){
						AudienciaTipodt.limpar();
						AudienciaTipodt = AudienciaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("AudienciaTipodt",AudienciaTipodt );
		request.getSession().setAttribute("AudienciaTipone",AudienciaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
