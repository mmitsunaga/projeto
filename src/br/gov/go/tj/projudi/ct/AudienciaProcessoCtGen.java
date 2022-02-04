package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaProcessoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1961992280286377556L;

    public  AudienciaProcessoCtGen() {

	} 
		public int Permissao(){
			return AudienciaProcessoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaProcessoDt audienciaProcessoDt;
		AudienciaProcessoNe AudienciaProcessone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/AudienciaProcesso.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","AudienciaProcesso");




		AudienciaProcessone =(AudienciaProcessoNe)request.getSession().getAttribute("AudienciaProcessone");
		if (AudienciaProcessone == null )  AudienciaProcessone = new AudienciaProcessoNe();  


		audienciaProcessoDt =(AudienciaProcessoDt)request.getSession().getAttribute("AudienciaProcessoDt");
		if (audienciaProcessoDt == null )  audienciaProcessoDt = new AudienciaProcessoDt();  

		audienciaProcessoDt.setId_Audiencia( request.getParameter("Id_Audiencia")); 
		audienciaProcessoDt.setAudienciaTipo( request.getParameter("AudienciaTipo")); 
		audienciaProcessoDt.setId_AudienciaProcessoStatus( request.getParameter("Id_AudienciaProcessoStatus")); 
		audienciaProcessoDt.setAudienciaProcessoStatus( request.getParameter("AudienciaProcessoStatus")); 
		audienciaProcessoDt.setId_ServentiaCargo( request.getParameter("Id_ServentiaCargo")); 
		audienciaProcessoDt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		audienciaProcessoDt.setId_Processo( request.getParameter("Id_Processo")); 
		audienciaProcessoDt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		audienciaProcessoDt.setDataMovimentacao( request.getParameter("DataMovimentacao")); 
		audienciaProcessoDt.setAudienciaTipoCodigo( request.getParameter("AudienciaTipoCodigo")); 
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo( request.getParameter("AudienciaProcessoStatusCodigo")); 

		audienciaProcessoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		audienciaProcessoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					AudienciaProcessone.excluir(audienciaProcessoDt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AudienciaProcessoLocalizar.jsp";
				request.setAttribute("tempBuscaId_AudienciaProcesso","Id_AudienciaProcesso");
				request.setAttribute("tempBuscaAudienciaProcesso","AudienciaProcesso");
				request.setAttribute("tempRetorno","AudienciaProcesso");
				tempList =AudienciaProcessone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAudienciaProcesso", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", AudienciaProcessone.getQuantidadePaginas());
					audienciaProcessoDt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				audienciaProcessoDt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=AudienciaProcessone.Verificar(audienciaProcessoDt); 
					if (Mensagem.length()==0){
						AudienciaProcessone.salvar(audienciaProcessoDt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (AudienciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Audiencia","Id_Audiencia");
					request.setAttribute("tempBuscaAudiencia","Audiencia");
					request.setAttribute("tempRetorno","AudienciaProcesso");
					stAcao="/WEB-INF/jsptjgo/AudienciaLocalizar.jsp";
					tempList =AudienciaProcessone.consultarDescricaoAudiencia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaAudiencia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", AudienciaProcessone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AudienciaProcessoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_AudienciaProcessoStatus","Id_AudienciaProcessoStatus");
					request.setAttribute("tempBuscaAudienciaProcessoStatus","AudienciaProcessoStatus");
					request.setAttribute("tempRetorno","AudienciaProcesso");
					stAcao="/WEB-INF/jsptjgo/AudienciaProcessoStatusLocalizar.jsp";
					tempList =AudienciaProcessone.consultarDescricaoAudienciaProcessoStatus(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaAudienciaProcessoStatus", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", AudienciaProcessone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ServentiaCargo","Id_ServentiaCargo");
					request.setAttribute("tempBuscaServentiaCargo","ServentiaCargo");
					request.setAttribute("tempRetorno","AudienciaProcesso");
					stAcao="/WEB-INF/jsptjgo/ServentiaCargoLocalizar.jsp";
					tempList =AudienciaProcessone.consultarDescricaoServentiaCargo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentiaCargo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", AudienciaProcessone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Processo","Id_Processo");
					request.setAttribute("tempBuscaProcesso","Processo");
					request.setAttribute("tempRetorno","AudienciaProcesso");
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
					tempList =AudienciaProcessone.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcesso", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", AudienciaProcessone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_AudienciaProcesso");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( audienciaProcessoDt.getId()))){
						audienciaProcessoDt.limpar();
						audienciaProcessoDt = AudienciaProcessone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("AudienciaProcessoDt",audienciaProcessoDt );
		request.getSession().setAttribute("AudienciaProcessone",AudienciaProcessone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
