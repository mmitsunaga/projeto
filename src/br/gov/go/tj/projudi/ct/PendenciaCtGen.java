package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PendenciaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1815359270126039086L;

    public  PendenciaCtGen() {

	} 
		public int Permissao(){
			return PendenciaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaDt Pendenciadt;
		PendenciaNe Pendenciane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Pendencia.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Pendencia");
		request.setAttribute("tempBuscaId_Pendencia","Id_Pendencia");
		request.setAttribute("tempBuscaPendencia","Pendencia");
		request.setAttribute("tempBuscaId_PendenciaTipo","Id_PendenciaTipo");
		request.setAttribute("tempBuscaPendenciaTipo","PendenciaTipo");
		request.setAttribute("tempBuscaId_Movimentacao","Id_Movimentacao");
		request.setAttribute("tempBuscaMovimentacao","Movimentacao");
		request.setAttribute("tempBuscaId_Processo","Id_Processo");
		request.setAttribute("tempBuscaProcesso","Processo");
		request.setAttribute("tempBuscaId_ProcessoPrioridade","Id_ProcessoPrioridade");
		request.setAttribute("tempBuscaProcessoPrioridade","ProcessoPrioridade");
		request.setAttribute("tempBuscaId_ProcessoParte","Id_ProcessoParte");
		request.setAttribute("tempBuscaProcessoParte","ProcessoParte");
		request.setAttribute("tempBuscaId_PendenciaStatus","Id_PendenciaStatus");
		request.setAttribute("tempBuscaPendenciaStatus","PendenciaStatus");
		request.setAttribute("tempBuscaId_UsuarioCadastrador","Id_UsuarioCadastrador");
		request.setAttribute("tempBuscaUsuarioCadastrador","UsuarioCadastrador");
		request.setAttribute("tempBuscaId_UsuarioFinalizador","Id_UsuarioFinalizador");
		request.setAttribute("tempBuscaUsuarioFinalizador","UsuarioFinalizador");
		request.setAttribute("tempBuscaId_PendenciaPai","Id_PendenciaPai");
		request.setAttribute("tempBuscaPendenciaPai","PendenciaPai");

		request.setAttribute("tempRetorno","Pendencia");



		Pendenciane =(PendenciaNe)request.getSession().getAttribute("Pendenciane");
		if (Pendenciane == null )  Pendenciane = new PendenciaNe();  


		Pendenciadt =(PendenciaDt)request.getSession().getAttribute("Pendenciadt");
		if (Pendenciadt == null )  Pendenciadt = new PendenciaDt();  

		Pendenciadt.setPendencia( request.getParameter("Pendencia")); 
		Pendenciadt.setDataVisto( request.getParameter("DataVisto")); 
		Pendenciadt.setId_PendenciaTipo( request.getParameter("Id_PendenciaTipo")); 
		Pendenciadt.setPendenciaTipo( request.getParameter("PendenciaTipo")); 
		Pendenciadt.setId_Movimentacao( request.getParameter("Id_Movimentacao")); 
		Pendenciadt.setMovimentacao( request.getParameter("Movimentacao")); 
		Pendenciadt.setId_Processo( request.getParameter("Id_Processo")); 
		Pendenciadt.setProcessoNumero( request.getParameter("ProcessoNumero")); 
		Pendenciadt.setId_ProcessoPrioridade( request.getParameter("Id_ProcessoPrioridade")); 
		Pendenciadt.setProcessoPrioridade( request.getParameter("ProcessoPrioridade")); 
		Pendenciadt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte")); 
		Pendenciadt.setNomeParte( request.getParameter("NomeParte")); 
		Pendenciadt.setId_PendenciaStatus( request.getParameter("Id_PendenciaStatus")); 
		Pendenciadt.setPendenciaStatus( request.getParameter("PendenciaStatus")); 
		Pendenciadt.setId_UsuarioCadastrador( request.getParameter("Id_UsuarioCadastrador")); 
		Pendenciadt.setUsuarioCadastrador( request.getParameter("UsuarioCadastrador")); 
		Pendenciadt.setId_UsuarioFinalizador( request.getParameter("Id_UsuarioFinalizador")); 
		Pendenciadt.setUsuarioFinalizador( request.getParameter("UsuarioFinalizador")); 
		Pendenciadt.setDataInicio( request.getParameter("DataInicio")); 
		Pendenciadt.setDataFim( request.getParameter("DataFim")); 
		Pendenciadt.setDataLimite( request.getParameter("DataLimite")); 
		Pendenciadt.setDataDistribuicao( request.getParameter("DataDistribuicao")); 
		Pendenciadt.setPrazo( request.getParameter("Prazo")); 
		Pendenciadt.setDataTemp( request.getParameter("DataTemp")); 
		Pendenciadt.setId_PendenciaPai( request.getParameter("Id_PendenciaPai")); 
		Pendenciadt.setPendenciaTipoCodigo( request.getParameter("PendenciaTipoCodigo")); 

		Pendenciadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Pendenciadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Pendenciane.excluir(Pendenciadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/PendenciaLocalizar.jsp";
				tempList =Pendenciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaPendencia", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Pendenciane.getQuantidadePaginas());
					Pendenciadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Pendenciadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Pendenciane.Verificar(Pendenciadt); 
					if (Mensagem.length()==0){
						Pendenciane.salvar(Pendenciadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (PendenciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					stAcao="/WEB-INF/jsptjgo/PendenciaTipoLocalizar.jsp";
//						tempList =Pendenciane.consultarDescricaoPendenciaTipo(tempNomeBusca,  PosicaoPaginaAtual);
//						if (tempList.size()>0){
//							request.setAttribute("ListaPendenciaTipo", tempList); 
//							request.setAttribute("PaginaAtual", paginaatual);
//							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//							request.setAttribute("QuantidadePaginas", Pendenciane.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
					break;
				case (MovimentacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/MovimentacaoLocalizar.jsp";
						tempList =Pendenciane.consultarDescricaoMovimentacao(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaMovimentacao", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Pendenciane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
						tempList =Pendenciane.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcesso", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Pendenciane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoPrioridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ProcessoPrioridadeLocalizar.jsp";
						tempList =Pendenciane.consultarDescricaoProcessoPrioridade(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcessoPrioridade", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Pendenciane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ProcessoParteLocalizar.jsp";
						tempList =Pendenciane.consultarDescricaoProcessoParte(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcessoParte", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Pendenciane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;

//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Pendencia");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Pendenciadt.getId()))){
						Pendenciadt.limpar();
						Pendenciadt = Pendenciane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Pendenciadt",Pendenciadt );
		request.getSession().setAttribute("Pendenciane",Pendenciane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
