package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaProcessoStatusCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7029588497769282415L;

    public  AudienciaProcessoStatusCtGen() {

	} 
		public int Permissao(){
			return AudienciaProcessoStatusDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaProcessoStatusDt AudienciaProcessoStatusdt;
		AudienciaProcessoStatusNe AudienciaProcessoStatusne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/AudienciaProcessoStatus.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","AudienciaProcessoStatus");




		AudienciaProcessoStatusne =(AudienciaProcessoStatusNe)request.getSession().getAttribute("AudienciaProcessoStatusne");
		if (AudienciaProcessoStatusne == null )  AudienciaProcessoStatusne = new AudienciaProcessoStatusNe();  


		AudienciaProcessoStatusdt =(AudienciaProcessoStatusDt)request.getSession().getAttribute("AudienciaProcessoStatusdt");
		if (AudienciaProcessoStatusdt == null )  AudienciaProcessoStatusdt = new AudienciaProcessoStatusDt();  

		AudienciaProcessoStatusdt.setAudienciaProcessoStatus( request.getParameter("AudienciaProcessoStatus")); 
		AudienciaProcessoStatusdt.setAudienciaProcessoStatusCodigo( request.getParameter("AudienciaProcessoStatusCodigo")); 
		AudienciaProcessoStatusdt.setId_ServentiaTipo( request.getParameter("Id_ServentiaTipo")); 
		AudienciaProcessoStatusdt.setServentiaTipo( request.getParameter("ServentiaTipo")); 

		AudienciaProcessoStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		AudienciaProcessoStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					AudienciaProcessoStatusne.excluir(AudienciaProcessoStatusdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AudienciaProcessoStatusLocalizar.jsp";
				request.setAttribute("tempBuscaId_AudienciaProcessoStatus","Id_AudienciaProcessoStatus");
				request.setAttribute("tempBuscaAudienciaProcessoStatus","AudienciaProcessoStatus");
				request.setAttribute("tempRetorno","AudienciaProcessoStatus");
				tempList =AudienciaProcessoStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAudienciaProcessoStatus", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", AudienciaProcessoStatusne.getQuantidadePaginas());
					AudienciaProcessoStatusdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				AudienciaProcessoStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=AudienciaProcessoStatusne.Verificar(AudienciaProcessoStatusdt); 
					if (Mensagem.length()==0){
						AudienciaProcessoStatusne.salvar(AudienciaProcessoStatusdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_AudienciaProcessoStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( AudienciaProcessoStatusdt.getId()))){
						AudienciaProcessoStatusdt.limpar();
						AudienciaProcessoStatusdt = AudienciaProcessoStatusne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("AudienciaProcessoStatusdt",AudienciaProcessoStatusdt );
		request.getSession().setAttribute("AudienciaProcessoStatusne",AudienciaProcessoStatusne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
