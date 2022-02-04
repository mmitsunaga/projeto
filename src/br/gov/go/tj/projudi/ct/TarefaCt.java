package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.projudi.dt.ProjetoParticipanteDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.projudi.dt.TarefaPrioridadeDt;
import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import br.gov.go.tj.projudi.dt.TarefaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ProjetoParticipanteNe;
import br.gov.go.tj.projudi.ne.TarefaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
@SuppressWarnings("unchecked")
public class TarefaCt extends TarefaCtGen{

    private static final long serialVersionUID = -7085041995734963662L;

    //parametro de navegação entre as páginas de consultas utilizados
    //para lógico dentro do Coringa6.
    private static final String PRM_NAVEGACAO = "PRMNavegacao";
    
    private static final String PRM_MENSAGEM = "PRM_MENSAGEM";
    
    //parametro utilizado para identificar as páginas de retorno na navegação
    private static final String PRM_PAGINA_RETORNO = "PRMPaginaRetorno";
    
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TarefaDt Tarefadt;
		TarefaNe Tarefane;
		ProjetoParticipanteNe ProjetoParticipantene;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Tarefa.jsp";

    	String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		
		ProjetoParticipantene =(ProjetoParticipanteNe)request.getSession().getAttribute("ProjetoParticipantene");
		if (ProjetoParticipantene == null )  ProjetoParticipantene = new ProjetoParticipanteNe();  
		
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
		
		//---- auto-relacionamento com tarefa-pai
		Tarefadt.setId_TarefaPai( request.getParameter("Id_TarefaPai")); 
		Tarefadt.setTarefaPai( request.getParameter("TarefaPai"));
		
		Tarefadt.setPontosApf( request.getParameter("PontosApf")); 
		Tarefadt.setPontosApg( request.getParameter("PontosApg"));
		
		Tarefadt.setId_TarefaPrioridade( request.getParameter("Id_TarefaPrioridade")); 
		Tarefadt.setTarefaPrioridade( request.getParameter("TarefaPrioridade"));
		if(stNomeBusca3.equalsIgnoreCase("null")){
			Tarefadt.setId_TarefaPrioridade("");
			Tarefadt.setTarefaPrioridade("");
		}

		Tarefadt.setId_TarefaStatus( request.getParameter("Id_TarefaStatus")); 
		Tarefadt.setTarefaStatus( request.getParameter("TarefaStatus"));
		if(stNomeBusca2.equalsIgnoreCase("null")){
			Tarefadt.setId_TarefaStatus("");
			Tarefadt.setTarefaStatus("");
		}
		
		Tarefadt.setId_TarefaTipo( request.getParameter("Id_TarefaTipo")); 
		Tarefadt.setTarefaTipo( request.getParameter("TarefaTipo"));
		
		Tarefadt.setId_Projeto( request.getParameter("Id_Projeto")); 
		Tarefadt.setProjeto( request.getParameter("Projeto"));
		if(stNomeBusca4.equalsIgnoreCase("null")){
			Tarefadt.setId_Projeto("");
			Tarefadt.setProjeto("");
		}
		
		Tarefadt.setId_ProjetoParticipanteResponsavel( request.getParameter("Id_ProjetoParticipante")); 
		Tarefadt.setProjetoParticipanteResponsavel( request.getParameter("ProjetoParticipante")); 
		Tarefadt.setId_UsuarioCriador( request.getParameter("Id_UsuarioCriador")); 
		Tarefadt.setUsuarioCriador( request.getParameter("UsuarioCriador")); 
		Tarefadt.setId_UsuarioFinalizador( request.getParameter("Id_UsuarioFinalizador")); 
		Tarefadt.setUsuarioFinalizador( request.getParameter("UsuarioFinalizador")); 

		Tarefadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Tarefadt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		//parametros utilizados na renderização da tarefa
		popularParametrosRequest(request);
    	String idProjeto = "";
		
 		switch (paginaatual) {
 		    case Configuracao.LocalizarDWR:
 		        int inFluxo = 0;
 		        try{
 		            if(request.getParameter("fluxo") != null){
 						inFluxo = Funcoes.StringToInt( request.getParameter("fluxo").toString()); 
 					 } 

 		            switch (inFluxo) {
                        case 1:
                        	String nomebusca = "";
                        	try {
                        		if (request.getParameter("Id_Projeto") != null){
                        			idProjeto = request.getParameter("Id_Projeto").toString();
                        		}

                        		if (request.getParameter("nomeBusca") != null){
                        			nomebusca = request.getParameter("nomeBusca").toString();
                        		}
                        	}catch(Exception e){
                        		throw new Exception(e.getMessage());
                        	}
                        	pesquisarTarefasNaoFinalizadas(request, response, nomebusca, idProjeto, PosicaoPaginaAtual);
                            return;
                        default:
                            break;                    	
                    }
 		        }catch(Exception e){
 		            throw new Exception(e.getMessage());
 		        }
                break;
 		    case Configuracao.ExcluirResultado: //Excluir
					Tarefane.excluir(Tarefadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome da Tarefa"};
					String[] lisDescricao = {"Tarefa" , "Situação" , "Prioridade" , "Projeto"};
					String[][] lislocalizarBusca = new String [][] {
						{ "Status da Tarefa", String.valueOf(TarefaStatusDt.CodigoPermissao), "Status de Tarefa", Tarefadt.getTarefaStatus(), Tarefadt.getId_TarefaStatus()}							
						,{ "Prioridade da Tarefa", String.valueOf(TarefaPrioridadeDt.CodigoPermissao), "TarefaPrioridade", Tarefadt.getTarefaPrioridade(), Tarefadt.getId_TarefaPrioridade()}
						,{ "Projeto", String.valueOf(ProjetoDt.CodigoPermissao), "Projeto", Tarefadt.getProjeto(), Tarefadt.getId_Projeto()}
			            };						
					request.setAttribute("lislocalizarBusca", lislocalizarBusca);
					stAcao="/WEB-INF/jsptjgo/Padroes/LocalizarMultiplo.jsp";
					request.setAttribute("tempBuscaId", "Id_Tarefa");
					request.setAttribute("tempBuscaDescricao", "Tarefa");
					request.setAttribute("tempBuscaPrograma", "Tarefa");	
					request.setAttribute("tempRetorno","Tarefa");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					//limpa os parametros do filtro na primeira requisição
					request.setAttribute("TarefaStatus","");
					request.setAttribute("TarefaPrioridade","");
					request.setAttribute("Projeto","");
					request.setAttribute("tempFluxo1","1");
        		} else {
		          String stTemp="";
		          
		          stTemp = Tarefane.pesquisarTarefaJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, PosicaoPaginaAtual);			         
			      enviarJSON(response, stTemp);
			          		         
		         return;			
				}
				break;
			case Configuracao.Curinga6:
				//----------------------------------------------------
				//-- REQUISIÇÕES DA PÁGINA DE PESQUISA TRAZEM PRM_NAVEGACAO
				//----------------------------------------------------
				Integer navegacao=0;
				if(request.getParameter(PRM_NAVEGACAO) != null || request.getParameter("tempFluxo1") != null){
					navegacao = Funcoes.StringToInt(request.getParameter(PRM_NAVEGACAO));
					if(request.getParameter("tempFluxo1") != null){
						navegacao =  Funcoes.StringToInt(request.getParameter("tempFluxo1"));
					}
				}
				if (navegacao != 0) {
					switch (navegacao) {
						case 1: //CONSULTAR TAREFA USUÁRIO
							stAcao="/WEB-INF/jsptjgo/TarefaAtualizar.jsp";
							request.setAttribute("PaginaAtual",Configuracao.Curinga6);
							request.setAttribute(PRM_NAVEGACAO, request.getParameter(PRM_NAVEGACAO));
							stId = request.getParameter("Id_Tarefa");
							if (stId != null && !stId.isEmpty()){
								if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
									Tarefadt.limpar();
									Tarefadt = Tarefane.consultarId(stId);
								}
							}
							break;
						case 2: //SOLUCIONAR TAREFA
							if (request.getParameter("Passo")==null){
								request.setAttribute("tempFluxo1", request.getParameter(PRM_NAVEGACAO));
								Tarefane.solucionarTarefa(Tarefadt);
								String[] lisNomeBusca = {"Nome da Tarefa"};
								String[] lisDescricao = {"Tarefa" , "Situação" , "Prioridade" , "Projeto"};
								stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
								request.setAttribute("tempBuscaId", "Id_Tarefa");
								request.setAttribute("tempBuscaDescricao", "Tarefa");
								request.setAttribute("tempBuscaPrograma", "Tarefas do Usuário");	
								request.setAttribute("tempRetorno","Tarefa");
								request.setAttribute("tempDescricaoId","Id");
								request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
								request.setAttribute("PaginaAtual", Configuracao.Curinga6);
								request.setAttribute("PosicaoPaginaAtual", "0");
								request.setAttribute("QuantidadePaginas", "0");
								request.setAttribute("lisNomeBusca", lisNomeBusca);
								request.setAttribute("lisDescricao", lisDescricao);
								request.setAttribute("nomeUsuario",UsuarioSessao.getUsuarioDt().getServentia());
								request.setAttribute("Id_ServentiaCargo", UsuarioSessao.getUsuarioDt().getId_ServentiaCargo());
								//limpa os parametros do filtro na primeira requisição
								request.setAttribute("TarefaStatus","");
								request.setAttribute("TarefaPrioridade","");
								request.setAttribute("Projeto","");
							} else {
							String stTemp="";
							
							stTemp = Tarefane.pesquisarTarefaByParticipanteJSON(stNomeBusca1, null, null, null, UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), PosicaoPaginaAtual);							
							enviarJSON(response, stTemp);
															
							return;			
							}
							break;
						case 3: //PEGAR TAREFA
							stAcao="/WEB-INF/jsptjgo/PegarTarefa.jsp";
							request.setAttribute("PaginaAtual",Configuracao.Curinga6);
							request.setAttribute(PRM_NAVEGACAO, request.getParameter(PRM_NAVEGACAO));
							
							stId = request.getParameter("Id_Tarefa");
							if (stId != null && !stId.isEmpty()){
								if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
									Tarefadt.limpar();
									Tarefadt = Tarefane.consultarId(stId);
								}
							}
							break;
						case 4: //ATRIBUIR PEGAR TAREFA
							request.setAttribute(PRM_NAVEGACAO, request.getParameter(PRM_NAVEGACAO));
							//------------------------------
							//--- ATRIBUIR A ÚLTIMA TAREFA
							//------------------------------
							stId = Tarefane.atribuirUltimaTarefa(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), Tarefadt.getId_Projeto());
							if (stId != null && isNumeric(stId)){
								stAcao = "/WEB-INF/jsptjgo/TarefaAtualizar.jsp";
								request.setAttribute("PaginaAtual",Configuracao.Curinga6);
								if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
									Tarefadt.limpar();
									Tarefadt = Tarefane.consultarId(stId);
								}
							}else if(stId != null){
								stAcao="/WEB-INF/jsptjgo/PegarTarefa.jsp";
								request.setAttribute(PRM_MENSAGEM, stId);
							}
							break;
						default: //LISTAGEM DE TAREFAS POR USUÁRIO LOGADO
							if (request.getParameter("Passo")==null){
								request.setAttribute("tempFluxo1", request.getParameter(PRM_NAVEGACAO));
								String[] lisNomeBusca = {"Nome da Tarefa"};
								String[] lisDescricao = {"Tarefa" , "Situação" , "Prioridade" , "Projeto"};
								stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
								request.setAttribute("tempBuscaId", "Id_Tarefa");
								request.setAttribute("tempBuscaDescricao", "Tarefa");
								request.setAttribute("tempBuscaPrograma", "Tarefas do Usuário");	
								request.setAttribute("tempRetorno","Tarefa");
								request.setAttribute("tempDescricaoId","Id");
								request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
								request.setAttribute("PaginaAtual", Configuracao.Curinga6);
								request.setAttribute("PosicaoPaginaAtual", "0");
								request.setAttribute("QuantidadePaginas", "0");
								request.setAttribute("lisNomeBusca", lisNomeBusca);
								request.setAttribute("lisDescricao", lisDescricao);
								request.setAttribute("nomeUsuario",UsuarioSessao.getUsuarioDt().getServentia());
								request.setAttribute("Id_ServentiaCargo", UsuarioSessao.getUsuarioDt().getId_ServentiaCargo());
								//limpa os parametros do filtro na primeira requisição
								request.setAttribute("TarefaStatus","");
								request.setAttribute("TarefaPrioridade","");
								request.setAttribute("Projeto","");
							} else {
							String stTemp="";
							
							stTemp = Tarefane.pesquisarTarefaByParticipanteJSON(stNomeBusca1, null, null, null, UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), PosicaoPaginaAtual);							
							enviarJSON(response, stTemp);								
							
							return;			
							}
							break;
						}
					break;
				}else if(request.getParameter("PRMAcoes") != null && !request.getParameter("PRMAcoes").isEmpty()){
					Integer renderizar = Funcoes.StringToInt(request.getParameter("PRMAcoes"));
					//----------------------
					//-- GERAR SUB-TAREFA
					//----------------------
					if(renderizar.equals(1)){
						//a tarefa atual será a tarefa-pai da próxima tela de cadastro
						Tarefadt.limpar();
						Tarefadt.setId_TarefaPai(request.getParameter("Id_Tarefa"));
						Tarefadt.setTarefaPai(request.getParameter("Tarefa"));
						
					//----------------------
					//-- ATRIBUIR TAREFA
					//----------------------
					}else if(renderizar.equals(2)){
						//parametro que indica que pode adicionar usuário responsável
						request.setAttribute("PRMAlterarResponsavel", true); 
						stId = request.getParameter("Id_Tarefa");
						if (stId != null && !stId.isEmpty()){
							if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
								Tarefadt.limpar();
								Tarefadt = Tarefane.consultarId(stId);
							}
						}
					}
				}else{
					//permite localizar uma tarefa PAI (Auto-Relacionamento em tarefa)
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Digite a Tarefa"};
						String[] lisDescricao = {"Tarefa"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_TarefaPai");
						request.setAttribute("tempBuscaDescricao", "TarefaPai");
						request.setAttribute("tempBuscaPrograma", "Tarefa Pai");	
						request.setAttribute("tempRetorno","Tarefa");
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", Configuracao.Curinga6);
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
					String stTemp="";
					stTemp = Tarefane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;			
					}
				}
				break;
			case Configuracao.Curinga7: //atribui a última tarefa em aberto do projeto ao
										// usuário logado
				Mensagem = Tarefane.Verificar(Tarefadt); 
				if (Mensagem.length()==0){
					// vincula o Usuário Logado como criador da tarefa
					Tarefadt.setId_UsuarioCriador(UsuarioSessao.getId_Usuario());
					Tarefane.salvarAlterarStatusAberto(Tarefadt); 
					request.setAttribute("MensagemOk", "Tarefa atribuida com sucesso."); 

					stAcao="/WEB-INF/jsptjgo/TarefaAtualizar.jsp";
					request.setAttribute("PaginaAtual",Configuracao.Curinga6);
					request.setAttribute(PRM_NAVEGACAO, "1");
					stId = request.getParameter("Id_Tarefa");
					if (stId != null && !stId.isEmpty()){
						if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
							Tarefadt.limpar();
							Tarefadt = Tarefane.consultarId(stId);
						}
					}
					break;
				}else{
					request.setAttribute("MensagemErro", Mensagem );
				}
				break;
				// Consulta de status possíveis
			case Configuracao.Novo:
				Tarefadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Tarefane.Verificar(Tarefadt); 
					if (Mensagem.length()==0){
						//---------------------------
						//--- Se não existir usuário criado 
						//--- então é inclusão de nova tarefa
						//---------------------------
						if(Tarefadt.getId_UsuarioCriador() == null || Tarefadt.getId_UsuarioCriador().isEmpty()){
							// vincula o Usuário Logado como criador da tarefa
							Tarefadt.setId_UsuarioCriador(UsuarioSessao.getId_Usuario());
							Tarefadt.setDataCriacao(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
							Tarefane.salvarAlterarStatusAberto(Tarefadt); 
							request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
						}else{
							// se for alteração e a tarefa estiver vinculada ao STATUS Aberta,
							// altera para o Status em andamento.
							Integer statusAtualTarefa = Funcoes.StringToInt(Tarefadt.getTarefaStatusCodigo());
							if(statusAtualTarefa.equals(TarefaStatusDt.ABERTA) 
									&& Tarefadt.getId_ProjetoParticipanteResponsavel() != null
										&& !Tarefadt.getId_ProjetoParticipanteResponsavel().isEmpty()){
								
								Tarefane.salvarAlterarStatusEmAndamento(Tarefadt); 
							}else{
								Tarefane.salvar(Tarefadt); 
							}
							request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
							Tarefadt = new TarefaDt();
						}
					}else{
						request.setAttribute("MensagemErro", Mensagem );
					}
				break;
// Link não encontrado 20/06/2017
//			case (TarefaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//				request.setAttribute("tempBuscaId_TarefaPai","Id_TarefaPai");
//				request.setAttribute("tempBuscaTarefa","Tarefa");
//				request.setAttribute("tempRetorno","Tarefa");
//				stAcao="/WEB-INF/jsptjgo/TarefaLocalizar.jsp";
//				tempList =Tarefane.consultarDescricaoTarefa(tempNomeBusca, PosicaoPaginaAtual);
//				if (tempList.size()>0){
//					request.setAttribute("ListaTarefa", tempList); 
//					request.setAttribute("PaginaAtual", paginaatual);
//					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//					request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
//				}else{ 
//					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//					request.setAttribute("PaginaAtual", Configuracao.Editar);
//				}
//				break;
				case (TarefaPrioridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						String PRMPaginaRetorno;
						if (request.getParameter("PRMPaginaRetorno")==null){
							PRMPaginaRetorno = request.getParameter("tempFluxo1");
						} else {
							PRMPaginaRetorno = request.getParameter("PRMPaginaRetorno");
						}
						String[] lisNomeBusca = {"Digite o TarefaPrioridade"};
						String[] lisDescricao = {"TarefaPrioridade"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_TarefaPrioridade");
						request.setAttribute("tempBuscaDescricao", "TarefaPrioridade");
						request.setAttribute("tempBuscaPrograma", "TarefaPrioridade");	
						request.setAttribute("tempRetorno","Tarefa?PRMPaginaRetorno=".concat(PRMPaginaRetorno));
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (TarefaPrioridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
					String stTemp="";
					stTemp = Tarefane.consultarDescricaoTarefaPrioridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;			
					}
					break;
				case (TarefaStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						String PRMPaginaRetorno;
						if (request.getParameter("PRMPaginaRetorno")==null){
							PRMPaginaRetorno = request.getParameter("tempFluxo1");
						} else {
							PRMPaginaRetorno = request.getParameter("PRMPaginaRetorno");
						}
						String[] lisNomeBusca = {"Digite a Descrição"};
						String[] lisDescricao = {"TarefaStatus"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_TarefaStatus");
						request.setAttribute("tempBuscaDescricao", "TarefaStatus");
						request.setAttribute("tempBuscaPrograma", "Status de Tarefa");	
						request.setAttribute("tempRetorno","Tarefa?PRMPaginaRetorno=".concat(PRMPaginaRetorno));
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (TarefaStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
					String stTemp="";
					stTemp = Tarefane.consultarDescricaoTarefaStatusJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;			
					}
					break;
				case (TarefaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Digite o TarefaTipo"};
						String[] lisDescricao = {"TarefaTipo"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_TarefaTipo");
						request.setAttribute("tempBuscaDescricao", "TarefaTipo");
						request.setAttribute("tempBuscaPrograma", "TarefaTipo");			
						request.setAttribute("tempRetorno","Tarefa");			
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", TarefaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
					String stTemp="";
					stTemp = Tarefane.consultarDescricaoTarefaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;			
					}
					break;
				case (ProjetoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						String PRMPaginaRetorno;
						if (request.getParameter("PRMPaginaRetorno")==null){
							PRMPaginaRetorno = request.getParameter("tempFluxo1");
						} else {
							PRMPaginaRetorno = request.getParameter("PRMPaginaRetorno");
						}
						String[] lisNomeBusca = {"Projeto"};
						String[] lisDescricao = {"Projeto"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_Projeto");
						request.setAttribute("tempBuscaDescricao", "Projeto");
						request.setAttribute("tempBuscaPrograma", "Projeto");			
						request.setAttribute("tempRetorno","Tarefa?PRMPaginaRetorno=".concat(PRMPaginaRetorno));			
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", ProjetoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
					String stTemp="";
					stTemp = ProjetoParticipantene.consultarDescricaoProjetoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;			
					}
					break;
				case (ProjetoParticipanteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("PRMAlterarResponsavel", true); 
					request.setAttribute("tempBuscaId_ProjetoParticipante","Id_ProjetoParticipante");
					request.setAttribute("tempBuscaProjetoParticipante","ProjetoParticipante");
					request.setAttribute("tempRetorno","Tarefa");
					request.setAttribute("idProjeto", Tarefadt.getId_Projeto());
					tempList =Tarefane.consultarParticipanteDescricaoProjeto(tempNomeBusca, Tarefadt.getId_Projeto(), PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProjetoParticipante", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
						request.setAttribute("MensagemOk", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
			default:
				//-------------------------------------------------
				//-- INTERCEPTA O RETORNO DAS PÁGINAS LOCALIZAR
				//-- DE STATUS, PRIORIDADE, PROJETO DIRECIONANDO
				//-- PARA A PÁGINA DE LOCALIZAR TAREFA.
				//-------------------------------------------------
				if(request.getParameter(PRM_PAGINA_RETORNO) != null && !request.getParameter(PRM_PAGINA_RETORNO).isEmpty()){
					Integer PRMRetorno = Funcoes.StringToInt(request.getParameter(PRM_PAGINA_RETORNO));
					switch (PRMRetorno) {
						case 1:
								String[] lisNomeBusca = {"Nome da Tarefa"};
								String[] lisDescricao = {"Tarefa" , "Situação" , "Prioridade" , "Projeto"};
								String[][] lislocalizarBusca = new String [][] {
									{ "Status da Tarefa", String.valueOf(TarefaStatusDt.CodigoPermissao), "Status de Tarefa", Tarefadt.getTarefaStatus(), Tarefadt.getId_TarefaStatus()}						
									,{ "Prioridade da Tarefa", String.valueOf(TarefaPrioridadeDt.CodigoPermissao), "TarefaPrioridade", Tarefadt.getTarefaPrioridade(), Tarefadt.getId_TarefaPrioridade()}
									,{ "Projeto", String.valueOf(ProjetoDt.CodigoPermissao), "Projeto", Tarefadt.getProjeto(), Tarefadt.getId_Projeto()}
						            };						
								request.setAttribute("lislocalizarBusca", lislocalizarBusca);
								stAcao="/WEB-INF/jsptjgo/Padroes/LocalizarMultiplo.jsp";
								request.setAttribute("tempBuscaId", "Id_Tarefa");
								request.setAttribute("tempBuscaDescricao", "Tarefa");
								request.setAttribute("tempBuscaPrograma", "Tarefa");	
								request.setAttribute("tempRetorno","Tarefa");
								request.setAttribute("tempDescricaoId","Id");
								request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
								request.setAttribute("PaginaAtual", Configuracao.Localizar);
								request.setAttribute("PosicaoPaginaAtual", "0");
								request.setAttribute("QuantidadePaginas", "0");
								request.setAttribute("lisNomeBusca", lisNomeBusca);
								request.setAttribute("lisDescricao", lisDescricao);
								request.setAttribute("tempFluxo1","1");
			  				break;
						case 2:
							stAcao="/WEB-INF/jsptjgo/PegarTarefa.jsp";
							//Atributos de Projeto - FILTRO
							request.setAttribute("PRMPaginaRetorno",	request.getParameter("PRMPaginaRetorno"));
							request.setAttribute("Projeto",	Tarefadt.getProjeto());
							request.setAttribute("Id_Projeto",		Tarefadt.getId_Projeto());
							break;
						default:
							stId = request.getParameter("Id_Tarefa");
							if (stId != null && !stId.isEmpty()){
								if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
									Tarefadt.limpar();
									Tarefadt = Tarefane.consultarId(stId);
								}
							}
							break;
						}
				break;
				//---------------------------------------------
				//---- ALTERAR O RESPONSÁVEL PELA TAREFA
				//---------------------------------------------
				}else if(request.getParameter("PRMAlterarResponsavel") != null){
					request.setAttribute("PRMAlterarResponsavel", new Boolean(request.getParameter("PRMAlterarResponsavel")));
					stId = request.getParameter("Id_Tarefa");
					if (stId != null && !stId.isEmpty()){
						if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
							Tarefadt.limpar();
							Tarefadt = Tarefane.consultarId(stId);
						}
					}
 				}else{
					stId = request.getParameter("Id_Tarefa");
					if (stId != null && !stId.isEmpty()){
						if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Tarefadt.getId()))){
							Tarefadt.limpar();
							Tarefadt = Tarefane.consultarId(stId);
						}
					}
				}
				break;
		}

		request.getSession().setAttribute("Tarefadt",Tarefadt );
		request.getSession().setAttribute("Tarefane",Tarefane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
    
    /**
     * Método responsável por re-popular os parametros no request.
     * @param request
     */
    private void popularParametrosRequest(HttpServletRequest request){
    	if(request.getParameter("PRMAcoes") != null 
    			&& !request.getParameter("PRMAcoes").isEmpty()){
    		request.setAttribute("PRMAcoes", request.getParameter("PRMAcoes"));
    		
    	}

    	if(request.getParameter(PRM_NAVEGACAO) != null 
    			&& !request.getParameter(PRM_NAVEGACAO).isEmpty()){
    		request.setAttribute(PRM_NAVEGACAO, request.getParameter(PRM_NAVEGACAO));
    	}else{
    		request.setAttribute("PRMAlterarResponsavel", false);    		
    	}

    	if(request.getParameter(PRM_PAGINA_RETORNO) != null 
    			&& !request.getParameter(PRM_PAGINA_RETORNO).isEmpty()){
    		request.setAttribute(PRM_PAGINA_RETORNO, request.getParameter(PRM_PAGINA_RETORNO));
    		
    	}
    }
    
    private boolean isNumeric(String valor) {
        try{
        	Double.parseDouble(valor.trim());
            return true;
        } catch(Exception e) {
            return false;
        }
    }

	public void pesquisarTarefasNaoFinalizadas(HttpServletRequest request, HttpServletResponse response, String nome, String idProjeto, String posicao) throws Exception {
		String stTemp = ""; 
		TarefaNe obNegocio;
		int permissao = Permissao();
		obNegocio =  (TarefaNe)request.getSession().getAttribute("Tarefane");
		if (obNegocio == null )  obNegocio = new TarefaNe();
		if (Funcoes.StringToInt(posicao)<0) posicao="1";
		
		//realiza a busca
		stTemp =  obNegocio.pesquisarTarefaNaoFinalizadaJSON(nome, idProjeto, posicao);
		            
        enviarJSON(response,  stTemp);
            
	}

}
