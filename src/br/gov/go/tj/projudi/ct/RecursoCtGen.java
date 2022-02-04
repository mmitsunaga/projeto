package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.RecursoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RecursoCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7229235610914337542L;

	public  RecursoCtGen() {

	} 
		public int Permissao(){
			return RecursoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RecursoDt Recursodt;
		RecursoNe Recursone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Recurso.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Recurso");




		Recursone =(RecursoNe)request.getSession().getAttribute("Recursone");
		if (Recursone == null )  Recursone = new RecursoNe();  


		Recursodt =(RecursoDt)request.getSession().getAttribute("Recursodt");
		if (Recursodt == null )  Recursodt = new RecursoDt();  

		Recursodt.setId_Processo( request.getParameter("Id_Processo")); 
		Recursodt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		Recursodt.setId_ServentiaOrigem( request.getParameter("Id_ServentiaOrigem")); 
		Recursodt.setServentiaOrigem( request.getParameter("ServentiaOrigem")); 
		Recursodt.setId_ServentiaRecurso( request.getParameter("Id_ServentiaRecurso")); 
		Recursodt.setServentiaRecurso( request.getParameter("ServentiaRecurso")); 
		Recursodt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		Recursodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 
		Recursodt.setDataEnvio( request.getParameter("DataEnvio")); 
		Recursodt.setDataRecebimento( request.getParameter("DataRecebimento")); 
		Recursodt.setDataRetorno( request.getParameter("DataRetorno")); 

		Recursodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Recursodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Recursone.excluir(Recursodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/RecursoLocalizar.jsp";
				request.setAttribute("tempBuscaId_Recurso","Id_Recurso");
				request.setAttribute("tempBuscaRecurso","Recurso");
				request.setAttribute("tempRetorno","Recurso");
				tempList =Recursone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaRecurso", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Recursone.getQuantidadePaginas());
					Recursodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Recursodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Recursone.Verificar(Recursodt); 
					if (Mensagem.length()==0){
						Recursone.salvar(Recursodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Processo","Id_Processo");
					request.setAttribute("tempBuscaProcesso","Processo");
					request.setAttribute("tempRetorno","Recurso");
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
					tempList =Recursone.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcesso", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Recursone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ServentiaRecurso","Id_ServentiaRecurso");
					request.setAttribute("tempBuscaServentia","Serventia");
					request.setAttribute("tempRetorno","Recurso");
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
					tempList =Recursone.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Recursone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Recurso");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Recursodt.getId()))){
						Recursodt.limpar();
						Recursodt = Recursone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Recursodt",Recursodt );
		request.getSession().setAttribute("Recursone",Recursone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
