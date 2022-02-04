package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoResponsavelCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 23208248201944915L;

    public  ProcessoResponsavelCtGen() {

	} 
		public int Permissao(){
			return ProcessoResponsavelDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoResponsavelDt ProcessoResponsaveldt;
		ProcessoResponsavelNe ProcessoResponsavelne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoResponsavel.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoResponsavel");




		ProcessoResponsavelne =(ProcessoResponsavelNe)request.getSession().getAttribute("ProcessoResponsavelne");
		if (ProcessoResponsavelne == null )  ProcessoResponsavelne = new ProcessoResponsavelNe();  


		ProcessoResponsaveldt =(ProcessoResponsavelDt)request.getSession().getAttribute("ProcessoResponsaveldt");
		if (ProcessoResponsaveldt == null )  ProcessoResponsaveldt = new ProcessoResponsavelDt();  

		ProcessoResponsaveldt.setId_ServentiaCargo( request.getParameter("Id_ServentiaCargo")); 
		ProcessoResponsaveldt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		ProcessoResponsaveldt.setId_Processo( request.getParameter("Id_Processo")); 
		ProcessoResponsaveldt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		ProcessoResponsaveldt.setId_CargoTipo( request.getParameter("Id_CargoTipo")); 
		ProcessoResponsaveldt.setCargoTipo( request.getParameter("CargoTipo")); 
		ProcessoResponsaveldt.setId_Grupo( request.getParameter("Id_Grupo")); 
		ProcessoResponsaveldt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		ProcessoResponsaveldt.setCargoTipoCodigo( request.getParameter("CargoTipoCodigo")); 

		ProcessoResponsaveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoResponsaveldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoResponsavelne.excluir(ProcessoResponsaveldt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ProcessoResponsavelLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoResponsavel","Id_ProcessoResponsavel");
				request.setAttribute("tempBuscaProcessoResponsavel","ProcessoResponsavel");
				request.setAttribute("tempRetorno","ProcessoResponsavel");
				tempList =ProcessoResponsavelne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProcessoResponsavel", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoResponsavelne.getQuantidadePaginas());
					ProcessoResponsaveldt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ProcessoResponsaveldt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoResponsavelne.Verificar(ProcessoResponsaveldt); 
					if (Mensagem.length()==0){
						ProcessoResponsavelne.salvar(ProcessoResponsaveldt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ServentiaCargo","Id_ServentiaCargo");
					request.setAttribute("tempBuscaServentiaCargo","ServentiaCargo");
					request.setAttribute("tempRetorno","ProcessoResponsavel");
					stAcao="/WEB-INF/jsptjgo/ServentiaCargoLocalizar.jsp";
					tempList =ProcessoResponsavelne.consultarDescricaoServentiaCargo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentiaCargo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoResponsavelne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Processo","Id_Processo");
					request.setAttribute("tempBuscaProcesso","Processo");
					request.setAttribute("tempRetorno","ProcessoResponsavel");
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
					tempList =ProcessoResponsavelne.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcesso", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoResponsavelne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
					request.setAttribute("tempBuscaGrupo","Grupo");
					request.setAttribute("tempRetorno","ProcessoResponsavel");
					stAcao="/WEB-INF/jsptjgo/GrupoLocalizar.jsp";
					tempList =ProcessoResponsavelne.consultarDescricaoGrupo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaGrupo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", ProcessoResponsavelne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoResponsavel");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoResponsaveldt.getId()))){
						ProcessoResponsaveldt.limpar();
						ProcessoResponsaveldt = ProcessoResponsavelne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoResponsaveldt",ProcessoResponsaveldt );
		request.getSession().setAttribute("ProcessoResponsavelne",ProcessoResponsavelne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
