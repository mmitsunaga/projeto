package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.PonteiroLogNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet responsável em controlar o Apensamento/Desapensamento de Processos 
 * @author msapaula
 */
public class ProcessoApensoCt extends Controle {

	private static final long serialVersionUID = -1202448373828988587L;

	public int Permissao() {
		return 256;
	}
	
	@SuppressWarnings("deprecation")
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		ProcessoDt processoDt;
		ProcessoNe Processone;

		List tempList = null;
		String posicaoLista = "";
		String id_ProcessoDependente = "";
		String processoNumeroDependente = "";
		String apensoDependente = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoApenso.jsp";

		request.setAttribute("tempRetorno", "ProcessoApenso");
		request.setAttribute("tempPrograma", "Processo");

		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null) Processone = new ProcessoNe();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		//se não houver um processo na sessão ocorreu algum problema assim retorno para pagina inicial
		if (processoDt==null){
			redireciona(response, "Usuario?PaginaAtual=-10");
			return;			
		}

		processoDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		processoDt.setId_ProcessoPrincipal(request.getParameter("Id_ProcessoDependente"));
		processoDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
		processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		
		//Retira "-" do número do processo
		if (request.getParameter("ProcessoNumeroDependente") != null) processoDt.setProcessoNumeroPrincipal(request.getParameter("ProcessoNumeroDependente").replaceAll("[-]", ""));

		if (request.getParameter("apensoDependente") != null) apensoDependente = request.getParameter("apensoDependente");
		if (request.getParameter("Id_ProcessoDependente") != null) id_ProcessoDependente = request.getParameter("Id_ProcessoDependente");
		if (request.getParameter("ProcessoNumeroDependente") != null) processoNumeroDependente = request.getParameter("ProcessoNumeroDependente");
		posicaoLista = request.getParameter("posicaoLista");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:

				//Verifica se usuário pode apensar processo
				String Mensagem = Processone.podeApensarProcesso(processoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() > 0) {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
					return;
				} else {
					//se o processo tiver dependente e tiver apenso, então ele foi apensado
					if (processoDt.isDependente() && processoDt.getApenso().equals("true")) {
						request.setAttribute("MensagemOk", "Processo já apensado ao processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumeroPrincipal()));
						apensoDependente = "apenso";
						processoDt.setApenso("true");
					//se ele tiver dependente, mas não tiver apenso, então ele é dependente
					} else if (processoDt.isDependente() && processoDt.getApenso().equals("false")) {
						request.setAttribute("MensagemOk", "Processo já é dependente do processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumeroPrincipal()));
						apensoDependente = "dependente";
						processoDt.setApenso("false");
					} else {
						request.setAttribute("Id_ProcessoDependente", "");
						request.setAttribute("ProcessoNumeroDependente", "");
						apensoDependente = "";
					}
					request.setAttribute("Id_ProcessoDependente", processoDt.getId_ProcessoPrincipal());
					request.setAttribute("ProcessoNumeroDependente", processoDt.getProcessoNumeroPrincipal());
				}
				break;

			//Confirmação de desapensamento
			case Configuracao.Excluir:
				processoNumeroDependente = processoDt.getProcessoNumeroPrincipal();
				id_ProcessoDependente = processoDt.getId_ProcessoPrincipal();
				request.setAttribute("Id_ProcessoDependente", id_ProcessoDependente);
				request.setAttribute("ProcessoNumeroDependente", processoNumeroDependente);
				request.setAttribute("Mensagem", "Clique aqui para desapensar / cancelar dependência do Processo.");
				break;

			//Desapensar Processo
			case Configuracao.ExcluirResultado:
				
				processoDt.setApenso("false");
				
				Processone.desapensarCancelarDependenciaProcesso(processoDt, null, false);
				
				request.setAttribute("MensagemOk", "Dependência do processo / Apenso cancelado com sucesso");
				processoDt = Processone.consultarIdCompleto(processoDt.getId());
				request.setAttribute("ProcessoNumeroDependente", "");
				apensoDependente = "";
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				stAcao = "/WEB-INF/jsptjgo/ProcessoApenso.jsp";

				break;

			//Confirmação de apensamento
			case Configuracao.Salvar:
				if(request.getParameter("apensoDependente") == null || request.getParameter("apensoDependente").equalsIgnoreCase("selecionar")){
					request.setAttribute("MensagemErro", "Preencher o campo Tipo de Vínculo.");
					request.setAttribute("PaginaAtual", Configuracao.Novo);
					request.setAttribute("PaginaAnterior", Configuracao.Novo);
					request.setAttribute("ProcessoNumeroDependente", processoNumeroDependente);
					break;
				}
				
				String mensagem = "";
				if(request.getParameter("apensoDependente").equalsIgnoreCase("apenso")) {
					mensagem = "Clique para APENSAR o Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " ao Processo "+ Funcoes.formataNumeroProcesso(processoDt.getProcessoNumeroPrincipal()) +".";	
				} else if(request.getParameter("apensoDependente").equalsIgnoreCase("dependente")) {
					mensagem = "Clique para CRIAR DEPENDÊNCIA do Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " ao Processo "+ Funcoes.formataNumeroProcesso(processoDt.getProcessoNumeroPrincipal()) +".";
				}
				
				request.setAttribute("Mensagem", mensagem);
				request.setAttribute("ocultarSalvar", "true");
				request.setAttribute("ProcessoNumeroDependente", processoNumeroDependente);
				break;

			//criar dependência / apensamento
			case Configuracao.SalvarResultado:
				if (processoNumeroDependente.length() > 0) {
					//verifica se o processo foi digitado no formato correto (número do processo completo)
					if (processoNumeroDependente.indexOf(".") == 7 || processoNumeroDependente.indexOf("-") == 7) {
						String msg = "";
						ProcessoDt processoPrincipalDt = Processone.consultarProcessoPrincipal(processoNumeroDependente,null);

						if (processoPrincipalDt != null) {
							if (processoDt.getId_Processo().equals(processoPrincipalDt.getId_Processo())) {
								request.setAttribute("MensagemErro", "O processo apensado e o principal são o mesmo.");
								request.setAttribute("Id_ProcessoDependente", "");
								request.setAttribute("ProcessoNumeroDependente", processoNumeroDependente);
								apensoDependente = "";
								break;
							}
						
							//Se for realizar o apensamento, os dois processos devem estar na mesma serventia.
							//Para dependência essa regra não se aplica.
							if(apensoDependente.equalsIgnoreCase("apenso") && !processoDt.getId_Serventia().equalsIgnoreCase(processoPrincipalDt.getId_Serventia())) {
								request.setAttribute("MensagemErro", "Para realizar o apensamento, os processos Principal e Apenso devem estar na mesma serventia.");
								request.setAttribute("Id_ProcessoDependente", "");
								request.setAttribute("ProcessoNumeroDependente", processoNumeroDependente);
								apensoDependente = "";
								break;
							}
							
							if (Funcoes.StringToInt(processoDt.getProcessoStatusCodigo()) == ProcessoStatusDt.ARQUIVADO) {
        						request.setAttribute("MensagemErro", "Não é possível executar essa ação, processo está arquivado.");
		    					request.setAttribute("Id_ProcessoDependente", "");
			    				request.setAttribute("ProcessoNumeroDependente", processoNumeroDependente);
				    			apensoDependente = "";
					    		break;
						    }

							if (!Processone.validaDependencia(processoDt, processoPrincipalDt)) {
								request.setAttribute("MensagemErro", "Processo " + Funcoes.formataNumeroProcesso(processoPrincipalDt.getProcessoNumero()) + " já " + (processoPrincipalDt.getApenso().equals("true") ? "apensado" : "dependente") +
										" ao processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()));
		    					request.setAttribute("Id_ProcessoDependente", "");
			    				request.setAttribute("ProcessoNumeroDependente", processoNumeroDependente);
				    			apensoDependente = "";
					    		break;
							}

							processoDt.setId_ProcessoPrincipal(processoPrincipalDt.getId_Processo());
							processoDt.setProcessoNumeroPrincipal(processoNumeroDependente);							
							
							if(apensoDependente.equalsIgnoreCase("dependente")) {
								
								processoDt.setApenso("false");
								
								Processone.apensarCriarDependenciaProcesso(processoDt, processoDt.getId_ProcessoPrincipal(), false);
								msg = "Dependência do processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " ao processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumeroPrincipal()) + " criada com sucesso.";
								
							} else if(apensoDependente.equalsIgnoreCase("apenso")) {
								
								processoDt.setApenso("true");
								
								Processone.apensarCriarDependenciaProcesso(processoDt, processoDt.getId_ProcessoPrincipal(), true);
								msg = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " apensado ao Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumeroPrincipal()) + " com sucesso.";
								request.setAttribute("MensagemOk", msg);
								
							}
							request.setAttribute("MensagemOk", msg);
							processoDt = Processone.consultarIdCompleto(processoDt.getId());
							
						} else {
							request.setAttribute("MensagemErro", "Processo Principal inexistente.");
						}
					} else {
						request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
					}
				} else {
					request.setAttribute("MensagemErro", "É necessário informar o Número do Processo Principal.");
				}
				break;

			//Localiza os processos apensos E dependentes ao processo
			case Configuracao.Localizar:
				tempList = Processone.consultarProcessosApensosEDependentes(processoDt.getId_Processo());
				request.setAttribute("listaApensos", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				stAcao = "/WEB-INF/jsptjgo/ProcessosApensos.jsp";				
				break;
		}

		request.setAttribute("apensoDependente", apensoDependente);
		request.setAttribute("posicaoLista", posicaoLista);
		request.getSession().setAttribute("processoDt", processoDt);
		request.getSession().setAttribute("Processone", Processone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}


}