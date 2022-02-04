package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ResultadoConsultaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.AdministrarNe;
import br.gov.go.tj.projudi.ne.LocomocaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AdministradorCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6700539185620880584L;

	public AdministradorCt() {

	}

	public int Permissao() {
		return 226;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AdministrarNe neAdministracao = (AdministrarNe)request.getSession().getAttribute("neAdministracao");
		if(neAdministracao == null){
			neAdministracao = new AdministrarNe();
		}
		
		String stAcao = "/WEB-INF/jsptjgo/Administracao.jsp";
		
		String fluxo = request.getParameter("Fluxo");

		List ListaUsuarios = null;

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		String Pagina = request.getParameter("Pagina");
		String Id_Sessao = request.getParameter("Id_Sessao");

		request.setAttribute("Pagina", Pagina);
		request.setAttribute("Id_Sessao", Id_Sessao);

		// a p�gina padr�o e a propria listagem de usu�rios
		request.setAttribute("PaginaAtual", Configuracao.Editar);

//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Novo:
				//atualizo as pemissoes de um usu�rio
				neAdministracao.atualizarPermissoesUsuario(Id_Sessao);
				//pego a lista de usu�rios conectados e deixo no request
				ListaUsuarios = neAdministracao.getUsuarios();
				request.setAttribute("ListaUsuarios", ListaUsuarios);
				request.setAttribute("MensagemOk", "Atualiza��o de permiss�es realizada com sucesso");
				break;

			case Configuracao.ExcluirResultado: //Excluir

				break;

			//Localizar ponteiros de Distribui��o
			case Configuracao.Localizar:
				//request.setAttribute("mapDistribuicao", DistribuicaoProcesso.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoServentiaCargo", DistribuicaoProcessoServentiaCargo.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoConciliador", DistribuicaoProcessoConciliador.getInstance().getMapDistribuicao());
				stAcao = "/WEB-INF/jsptjgo/AdministracaoDistribuicao.jsp";
				break;

			//Atualizar ponteiros de distribui��o de Processo em �rea de Distribui��o
			case Configuracao.LocalizarDWR:
				//DistribuicaoProcesso.getInstance().lerDados();
				//request.setAttribute("mapDistribuicao", DistribuicaoProcesso.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoServentiaCargo", DistribuicaoProcessoServentiaCargo.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoConciliador", DistribuicaoProcessoConciliador.getInstance().getMapDistribuicao());
				stAcao = "/WEB-INF/jsptjgo/AdministracaoDistribuicao.jsp";
				break;

			//Atualizar ponteiros de distribui��o de Processo para ServentiaCargo (Juiz, Relator e Promotor)
			case Configuracao.Imprimir:
				//DistribuicaoProcessoServentiaCargo.getInstance().lerDados();
				//request.setAttribute("mapDistribuicao", DistribuicaoProcesso.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoServentiaCargo", DistribuicaoProcessoServentiaCargo.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoConciliador", DistribuicaoProcessoConciliador.getInstance().getMapDistribuicao());
				stAcao = "/WEB-INF/jsptjgo/AdministracaoDistribuicao.jsp";
				break;
				
				//Atualizar ponteiros de distribui��o de Processo para Conciliador  
				// alterar mandados com erro de cadastro
			case Configuracao.Curinga6:				
				////////////////////////////////////////////////// inicio corre��o mandados 
				String mensagem = "";
				if (fluxo.equalsIgnoreCase("correcaoMandadoCentral")) {						
					String idMandado = request.getParameter("idMandado");
					if(idMandado == null || idMandado.equalsIgnoreCase(""))
						mensagem =  "Informe o numero do mandado \n\n";
					String quantidade = request.getParameter("numeroLocomocao");
					if(quantidade == null || quantidade.equalsIgnoreCase("")) {
						mensagem += "Informe o numero de locomo��es";
					} else {
						if(quantidade.equalsIgnoreCase("0") || Integer.parseInt(quantidade) > 5)				 
							mensagem += "N�mero locomo��es n�o pode ser zero e nem maior que 5";
					}
					if (mensagem.equalsIgnoreCase("")) {
					    LocomocaoNe locomocaoNe = new LocomocaoNe();						
					    mensagem = locomocaoNe.correcaoMandadoCentral(idMandado,Integer.parseInt(quantidade), UsuarioSessao.getUsuarioDt().getId());
					    request.setAttribute("MensagemOk", mensagem);
					} else 					
					  request.setAttribute("MensagemErro", mensagem);						 
					stAcao = "/WEB-INF/jsptjgo/CorrecaoMandadoCentral.jsp";
				}/////////////////// fim corre��o
				
				else {			
				   //DistribuicaoProcessoConciliador.getInstance().lerDados();
				   //request.setAttribute("mapDistribuicao", DistribuicaoProcesso.getInstance().getMapDistribuicao());
				   //request.setAttribute("mapDistribuicaoServentiaCargo", DistribuicaoProcessoServentiaCargo.getInstance().getMapDistribuicao());
				   //request.setAttribute("mapDistribuicaoConciliador", DistribuicaoProcessoConciliador.getInstance().getMapDistribuicao());
				   stAcao = "/WEB-INF/jsptjgo/AdministracaoDistribuicao.jsp";
				}
				break;
			case Configuracao.Curinga7:
				neAdministracao.atualizarPropriedades();
				//DistribuicaoProcessoConciliador.getInstance().lerDados();
				//request.setAttribute("mapDistribuicao", DistribuicaoProcesso.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoServentiaCargo", DistribuicaoProcessoServentiaCargo.getInstance().getMapDistribuicao());
				//request.setAttribute("mapDistribuicaoConciliador", DistribuicaoProcessoConciliador.getInstance().getMapDistribuicao());				
				break;
				
			case Configuracao.Curinga8:
				//Envio de e-mail teste para confirmar que o Projudi est� conseguindo enviar e-mails/intima��es para advogados.
				//Essa funcionalidade foi colocada aqui porque todos os outros curingas est�o ocupados. Se o if for confirmado
				//enviar� o email e encerrar� a funcionalidade, voltando para a tela inicial com a devida mensagem.
				//O if n�o ser� satisfeito se estiver executando um comando SQL.
				if(request.getParameter("Email") !=  null && !request.getParameter("Email").equalsIgnoreCase("")){
										
					new GerenciadorEmail("Sr. Administrador", request.getParameter("Email").toString(), "Sistema Projudi - E-mail para teste de recebimento", "Este � um e-mail para testar o recebimento de mensagens/intima��es enviadas pelo Sistema Projudi. Se este e-mail chegou, o envio de e-mails est� normal. Favor n�o responder.", GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
					request.setAttribute("MensagemOk", "E-mail enviado para " + request.getParameter("Email") +" com sucesso.");
					request.setAttribute("Email", "");
					request.setAttribute("comando", "");
					request.setAttribute("resposta", "");
					request.setAttribute("motivo", "");						
					stAcao = "/WEB-INF/jsptjgo/ExecutarComando.jsp";
					break;
				}
				
				int passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar").toString());
				String comando = "", comandoAntigo = "", motivo = "";
				if(request.getParameter("comando") != null && !request.getParameter("comando").equals("")) {
					comando = request.getParameter("comando").toString();
				}
				if(request.getParameter("comandoAntigo") != null && !request.getParameter("comandoAntigo").equals("")) {
					comandoAntigo = request.getParameter("comandoAntigo").toString();
				}
				if(request.getParameter("resposta") != null && !request.getParameter("resposta").equals("")) {
					request.getParameter("resposta").toString();
				}
				if(request.getParameter("motivo") != null && !request.getParameter("motivo").equals("")) {
					motivo = request.getParameter("motivo").toString();
				}
				switch (passoEditar) {
				case 1: //Abrir a p�gina
					request.setAttribute("comando", "");
					request.setAttribute("resposta", "");
					request.setAttribute("motivo", "");
					stAcao = "/WEB-INF/jsptjgo/ExecutarComando.jsp";
					break;
				case 2: //Executar SQL
					int[] respostaComando = neAdministracao.executarComando(comando);
					if(respostaComando.length > 0) {
						int somaTotal = 0;
						String respostaExecucao = "Comando SQL executado com sucesso. ";
						for (int i = 0; i < respostaComando.length; i++) {
							int posicaoSQL = i + 1;
							respostaExecucao += "O " + posicaoSQL +"� SQL afetar�(�o) " + respostaComando[i] + " registro(s). ";
							somaTotal = somaTotal + respostaComando[i];									
						}
						respostaExecucao = "Ser�(�o) afetado(s) " + somaTotal + " registro(s)." +
								"\nRollback realizado com sucesso. Execute o comando novamente com a fun��o Commit." + respostaExecucao;
						request.setAttribute("resposta", respostaExecucao);
						//Armazenando o comando atual para garantir que ele n�o ser� alterado na pr�xima execu��o.
						comandoAntigo = comando;
						request.setAttribute("comandoAntigo", comandoAntigo);
					} else {
						request.setAttribute("resposta", "Problema na execu��o do SQL.");
					}
					request.setAttribute("comando", comando);
					request.setAttribute("motivo", motivo);
					stAcao = "/WEB-INF/jsptjgo/ExecutarComando.jsp";
					break;
				case 4: //realizar commit de SQL
					if(motivo == null || motivo.equals("")) {
						request.setAttribute("resposta", "ERRO: O campo Motivo � obrigat�rio para realizar commit.");
						request.setAttribute("comando", comando);
						request.setAttribute("motivo", motivo);
						request.setAttribute("comandoAntigo", comandoAntigo);
					} else {
						if(comando.equals(comandoAntigo)){
							neAdministracao.comitarTransacao(UsuarioSessao, comando, motivo);
							request.setAttribute("resposta", "Commit realizado com sucesso.");
							request.setAttribute("comando", comando);
							request.setAttribute("motivo", motivo);
						} else {
							request.setAttribute("resposta", "O SQL foi alterado e n�o pode ser commitado. Execute o comando antes de tentar commitar.");
						} 
					}
					stAcao = "/WEB-INF/jsptjgo/ExecutarComando.jsp";
					break;
				}
				break;

			case Configuracao.SalvarResultado:

				if (Pagina != null && Pagina.equalsIgnoreCase("0")) {
					// o SalvarResultado ser� usado como confirma��o e finaliza��o de sess�o
					request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
					request.setAttribute("Pagina", "1");
					UsuarioDt dtUsuario = neAdministracao.getUsuario(Id_Sessao);
					request.setAttribute("Usuario", dtUsuario);
					stAcao = "/WEB-INF/jsptjgo/AdministracaoConfirmar.jsp";
				}
				if (Pagina != null && Pagina.equalsIgnoreCase("1")) {
					neAdministracao.InvalidaSessao(Id_Sessao);
					//pego a lista de usu�rios conectados e deixo no request                    
					ListaUsuarios = neAdministracao.getUsuarios();
					request.setAttribute("ListaUsuarios", ListaUsuarios);
				}
				break;
			case Configuracao.Curinga9:
				int passoEditarConsulta = Funcoes.StringToInt(request.getParameter("PassoEditar").toString());
				String comandoConsulta = "";				
				if(request.getParameter("comando") != null && !request.getParameter("comando").equals(""))
					comandoConsulta = request.getParameter("comando").toString();
				
				switch (passoEditarConsulta) {
					case 1: //Abrir a p�gina
						request.setAttribute("comando", "");
						//request.setAttribute("resposta", "");						
						stAcao = "/WEB-INF/jsptjgo/ExecutarConsulta.jsp";
						break;
					case 2: //Executar Consulta SQL
							try{
								ResultadoConsultaDt resultado = neAdministracao.executarConsulta(comandoConsulta);
								request.getSession().setAttribute("ResultaConsultadt", resultado);
								if (!resultado.PossuiResultados())
									super.exibaMensagemInconsistenciaErro(request, "SQL n�o retornou resultados!");								
							} catch(Exception e) {
								super.exibaMensagemInconsistenciaErro(request, "Erro ao Executar Consulta SQL: \n " + e.getLocalizedMessage());								
							}
						request.setAttribute("comando", comandoConsulta);						
						stAcao = "/WEB-INF/jsptjgo/ExecutarConsulta.jsp";
						break;					
				}
				break;
			//--------------------------------------------------------------------------------//
			default:
				
				if ("correcaomandadocentral".equalsIgnoreCase(fluxo)) {
					stAcao = "/WEB-INF/jsptjgo/CorrecaoMandadoCentral.jsp";					
				} else {
				
				  // a p�gina padr�o e a propria listagem de usu�rios
				  //pego a lista de usu�rios conectados e deixo no request
				  ListaUsuarios = neAdministracao.getUsuarios();
				  request.setAttribute("ListaUsuarios", ListaUsuarios);
				}

				break;
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
