package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelHistoricoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PendenciaResponsavelHistoricoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6048893507579049400L;

	public  PendenciaResponsavelHistoricoCtGen() {

	} 
		public int Permissao() {
			return PendenciaResponsavelHistoricoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaResponsavelHistoricoDt PendenciaResponsavelHistoricodt;
		PendenciaResponsavelHistoricoNe PendenciaResponsavelHistoricone;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PendenciaResponsavelHistorico.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PendenciaResponsavelHistorico");




		PendenciaResponsavelHistoricone =(PendenciaResponsavelHistoricoNe)request.getSession().getAttribute("PendenciaResponsavelHistoricone");
		if (PendenciaResponsavelHistoricone == null )  PendenciaResponsavelHistoricone = new PendenciaResponsavelHistoricoNe();  


		PendenciaResponsavelHistoricodt =(PendenciaResponsavelHistoricoDt)request.getSession().getAttribute("PendenciaResponsavelHistoricodt");
		if (PendenciaResponsavelHistoricodt == null )  PendenciaResponsavelHistoricodt = new PendenciaResponsavelHistoricoDt();  

		PendenciaResponsavelHistoricodt.setPendenciaResponsavelHistorico( request.getParameter("PendenciaResponsavelHistorico")); 
		PendenciaResponsavelHistoricodt.setId_Pendencia( request.getParameter("Id_Pendencia")); 
		PendenciaResponsavelHistoricodt.setPendendencia( request.getParameter("Pendendencia")); 
		PendenciaResponsavelHistoricodt.setId_ServentiaCargo( request.getParameter("Id_ServentiaCargo")); 
		PendenciaResponsavelHistoricodt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		PendenciaResponsavelHistoricodt.setDataInicio( request.getParameter("DataInicio")); 
		PendenciaResponsavelHistoricodt.setDataFim( request.getParameter("DataFim")); 

		PendenciaResponsavelHistoricodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PendenciaResponsavelHistoricodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PendenciaResponsavelHistoricone.excluir(PendenciaResponsavelHistoricodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("PendenciaResponsavelHistorico");
					descricao.add("PendenciaResponsavelHistorico");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PendenciaResponsavelHistorico");
					request.setAttribute("tempBuscaDescricao","PendenciaResponsavelHistorico");
					request.setAttribute("tempBuscaPrograma","PendenciaResponsavelHistorico");
					request.setAttribute("tempRetorno","PendenciaResponsavelHistorico");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","PendenciaResponsavelHistorico");
					stTemp = PendenciaResponsavelHistoricone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
				break;
			case Configuracao.Novo: 
				PendenciaResponsavelHistoricodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PendenciaResponsavelHistoricone.Verificar(PendenciaResponsavelHistoricodt); 
					if (Mensagem.length()==0){
						PendenciaResponsavelHistoricone.salvar(PendenciaResponsavelHistoricodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (PendenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Pendencia");
					descricao.add("Pendencia");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Pendencia");
					request.setAttribute("tempBuscaDescricao","Pendendencia");
					request.setAttribute("tempBuscaPrograma","Pendencia");
					request.setAttribute("tempRetorno","PendenciaResponsavelHistorico");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(PendenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","PendenciaResponsavelHistorico");
					stTemp = PendenciaResponsavelHistoricone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
					break;
				case (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("ServentiaCargo");
					descricao.add("ServentiaCargo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao","ServentiaCargo");
					request.setAttribute("tempBuscaPrograma","ServentiaCargo");
					request.setAttribute("tempRetorno","PendenciaResponsavelHistorico");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","PendenciaResponsavelHistorico");
					stTemp = PendenciaResponsavelHistoricone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e)  {}
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PendenciaResponsavelHistorico");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PendenciaResponsavelHistoricodt.getId()))){
						PendenciaResponsavelHistoricodt.limpar();
						PendenciaResponsavelHistoricodt = PendenciaResponsavelHistoricone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PendenciaResponsavelHistoricodt",PendenciaResponsavelHistoricodt );
		request.getSession().setAttribute("PendenciaResponsavelHistoricone",PendenciaResponsavelHistoricone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
