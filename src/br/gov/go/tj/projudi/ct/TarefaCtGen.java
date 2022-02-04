package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.projudi.dt.ProjetoParticipanteDt;
import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.projudi.dt.TarefaPrioridadeDt;
import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import br.gov.go.tj.projudi.dt.TarefaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.TarefaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class TarefaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 9006278854002292491L;

    public  TarefaCtGen() {

	} 
		public int Permissao(){
			return TarefaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TarefaDt Tarefadt;
		TarefaNe Tarefane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Tarefa.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Tarefa");




		Tarefane =(TarefaNe)request.getSession().getAttribute("Tarefane");
		if (Tarefane == null )  Tarefane = new TarefaNe();  


		Tarefadt =(TarefaDt)request.getSession().getAttribute("Tarefadt");
		if (Tarefadt == null )  Tarefadt = new TarefaDt();  

		Tarefadt.setTarefa( request.getParameter("Tarefa")); 
		Tarefadt.setDescricao( request.getParameter("Descricao")); 
		Tarefadt.setResposta( request.getParameter("Resposta")); 
		Tarefadt.setDataInicio( request.getParameter("DataInicio")); 
		Tarefadt.setPrevisao( request.getParameter("Previsao")); 
		Tarefadt.setDataFim( request.getParameter("DataFim")); 
		Tarefadt.setId_TarefaPai( request.getParameter("Id_TarefaPai")); 
		Tarefadt.setTarefaPai( request.getParameter("TarefaPai")); 
		Tarefadt.setPontosApf( request.getParameter("PontosApf")); 
		Tarefadt.setPontosApg( request.getParameter("PontosApg")); 
		Tarefadt.setId_TarefaPrioridade( request.getParameter("Id_TarefaPrioridade")); 
		Tarefadt.setTarefaPrioridade( request.getParameter("TarefaPrioridade")); 

		Tarefadt.setId_TarefaStatus( request.getParameter("Id_TarefaStatus")); 
		Tarefadt.setTarefaStatus( request.getParameter("TarefaStatus")); 
		Tarefadt.setId_TarefaTipo( request.getParameter("Id_TarefaTipo")); 
		Tarefadt.setTarefaTipo( request.getParameter("TarefaTipo")); 
		Tarefadt.setId_Projeto( request.getParameter("Id_Projeto")); 
		Tarefadt.setProjeto( request.getParameter("Projeto")); 
		Tarefadt.setId_ProjetoParticipanteResponsavel( request.getParameter("Id_ProjetoParticipanteResponsavel")); 
		Tarefadt.setProjetoParticipanteResponsavel( request.getParameter("ProjetoParticipanteResponsavel")); 
		Tarefadt.setId_UsuarioCriador( request.getParameter("Id_UsuarioCriador")); 
		Tarefadt.setUsuarioCriador( request.getParameter("UsuarioCriador")); 
		Tarefadt.setId_UsuarioFinalizador( request.getParameter("Id_UsuarioFinalizador")); 
		Tarefadt.setUsuarioFinalizador( request.getParameter("UsuarioFinalizador")); 

		Tarefadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Tarefadt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Tarefane.excluir(Tarefadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				request.setAttribute("tempBuscaId_Tarefa","Id_Tarefa");
				request.setAttribute("tempBuscaTarefa","Tarefa");
				request.setAttribute("tempRetorno","Tarefa");
				tempList =Tarefane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaTarefa", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					Tarefadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Tarefadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Tarefane.Verificar(Tarefadt); 
					if (Mensagem.length()==0){
						Tarefane.salvar(Tarefadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (TarefaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_TarefaPai","Id_TarefaPai");
					request.setAttribute("tempBuscaTarefa","Tarefa");
					request.setAttribute("tempRetorno","Tarefa");
					tempList =Tarefane.consultarDescricaoTarefa(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaTarefa", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (TarefaPrioridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_TarefaPrioridade","Id_TarefaPrioridade");
					request.setAttribute("tempBuscaTarefaPrioridade","TarefaPrioridade");
					request.setAttribute("tempRetorno","Tarefa");
					tempList =Tarefane.consultarDescricaoTarefaPrioridade(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaTarefaPrioridade", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (TarefaStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_TarefaStatus","Id_TarefaStatus");
					request.setAttribute("tempBuscaTarefaStatus","TarefaStatus");
					request.setAttribute("tempRetorno","Tarefa");
					tempList =Tarefane.consultarDescricaoTarefaStatus(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaTarefaStatus", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (TarefaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_TarefaTipo","Id_TarefaTipo");
					request.setAttribute("tempBuscaTarefaTipo","TarefaTipo");
					request.setAttribute("tempRetorno","Tarefa");
					tempList =Tarefane.consultarDescricaoTarefaTipo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaTarefaTipo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProjetoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Projeto","Id_Projeto");
					request.setAttribute("tempBuscaProjeto","Projeto");
					request.setAttribute("tempRetorno","Tarefa");
					tempList =Tarefane.consultarDescricaoProjeto(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProjeto", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProjetoParticipanteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ProjetoParticipanteResponsavel","Id_ProjetoParticipanteResponsavel");
					request.setAttribute("tempBuscaProjetoParticipante","ProjetoParticipante");
					request.setAttribute("tempRetorno","Tarefa");
					tempList =Tarefane.consultarDescricaoProjetoParticipante(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProjetoParticipante", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_UsuarioFinalizador","Id_UsuarioFinalizador");
					request.setAttribute("tempBuscaUsuario","Usuario");
					request.setAttribute("tempRetorno","Tarefa");
					tempList =Tarefane.consultarDescricaoUsuario(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaUsuario", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Tarefa");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
						Tarefadt.limpar();
						Tarefadt = Tarefane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Tarefadt",Tarefadt );
		request.getSession().setAttribute("Tarefane",Tarefane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
