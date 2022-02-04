package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoParteDebitoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Controla a manutenção de Débitos para Partes de Processo
 * @author mmgomes
 * 28/06/2018
 */
public class ProcessoParteDebitoFinanceiroCt extends Controle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -792491334739820599L;

	public int Permissao(){
		return ProcessoParteDebitoDt.CodigoPermissaoFinanceiro;
	}

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteDebitoDt ProcessoParteDebitodt;
		ProcessoParteDebitoNe ProcessoParteDebitone;
		ProcessoDebitoStatusDt processoDebitoStatusDt;
		ProcessoDt processoDt = null;

		String Mensagem = "";
		String stId = "";
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String stNomeBusca5 = "";
		String stNomeBusca6 = "";
		String stNomeBusca7 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		if(request.getParameter("nomeBusca5") != null) stNomeBusca5 = request.getParameter("nomeBusca5");
		if(request.getParameter("nomeBusca6") != null) stNomeBusca6 = request.getParameter("nomeBusca6");
		if(request.getParameter("nomeBusca7") != null) stNomeBusca7 = request.getParameter("nomeBusca7");

		String stAcao = "/WEB-INF/jsptjgo/ProcessoParteDebitoFinanceiro.jsp";
		request.setAttribute("tempPrograma", "ProcessoParteDebito");

		ProcessoParteDebitone = (ProcessoParteDebitoNe) request.getSession().getAttribute("ProcessoParteDebitone");
		if (ProcessoParteDebitone == null) ProcessoParteDebitone = new ProcessoParteDebitoNe();

		ProcessoParteDebitodt = (ProcessoParteDebitoDt) request.getSession().getAttribute("ProcessoParteDebitodt");
		if (ProcessoParteDebitodt == null) ProcessoParteDebitodt = new ProcessoParteDebitoDt();
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDebitoDt");
		
		ProcessoParteDebitodt.setId(request.getParameter("Id_ProcessoParteDebito"));
		ProcessoParteDebitodt.setId_ProcessoDebito(request.getParameter("Id_ProcessoDebito"));
		ProcessoParteDebitodt.setProcessoDebito(request.getParameter("ProcessoDebito"));
		ProcessoParteDebitodt.setDividaSolidaria(request.getParameter("dividaSolidaria"));
		if (request.getParameter("tipoDeParte") != null && request.getParameter("tipoDeParte").trim().length() > 0) {
			ProcessoParteDebitodt.setTipoParte(request.getParameter("tipoDeParte"));			
			if (ProcessoParteDebitodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO))) {
				ProcessoParteDebitodt.setId_ProcessoParte(request.getParameter("idProcessoPartePoloAtivo"));
				ProcessoParteDebitodt.setNome(request.getParameter("nomeProcessoPartePoloAtivo"));	
			} else {
				ProcessoParteDebitodt.setId_ProcessoParte(request.getParameter("idProcessoPartePoloPassivo"));
				ProcessoParteDebitodt.setNome(request.getParameter("nomeProcessoPartePoloPassivo"));
			}							
		}
		ProcessoParteDebitodt.setId_GuiaEmissao(request.getParameter("id_GuiaEmissao"));
		ProcessoParteDebitodt.setNumeroGuiaEmissao(request.getParameter("numeroGuiaEmissao"));
		ProcessoParteDebitodt.setProcessoNumeroPROAD(request.getParameter("ProcessoNumeroProad"));
		ProcessoParteDebitodt.setProcessoDebitoStatus(request.getParameter("ProcessoDebitoStatus"));
		ProcessoParteDebitodt.setId_ProcessoDebitoStatus(request.getParameter("Id_ProcessoDebitoStatus"));
		if (ProcessoParteDebitodt.isBaixado()) {
			ProcessoParteDebitodt.setDataBaixa(request.getParameter("DataBaixa"));	
		}
		ProcessoParteDebitodt.setObservacaoProcessoDebitoStatus(request.getParameter("editor"));
		
		ProcessoParteDebitodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteDebitodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:
				ProcessoParteDebitodt.limpar();
				processoDt = null;
				ProcessoParteDebitodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				processoDebitoStatusDt = ProcessoParteDebitone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				if (processoDebitoStatusDt != null) {
					ProcessoParteDebitodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
					ProcessoParteDebitodt.setId_ProcessoDebitoStatus(processoDebitoStatusDt.getId());					
				}
				break;

			case Configuracao.Salvar:
				if (ProcessoParteDebitodt.getId() == null || ProcessoParteDebitodt.getId().trim().length() == 0) {
					request.setAttribute("Mensagem", "Clique para confirmar o cadastro do Débito.");	
				} else {
					request.setAttribute("Mensagem", "Clique para confirmar a atualização do Débito.");
				}
				break;

			//Salva Débito para Parte
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoParteDebitone.VerificarFinanceiro(ProcessoParteDebitodt);
				if (Mensagem.length() == 0) {
					String id_ProcessoParteDebito = ProcessoParteDebitodt.getId();
					String id_processo = ProcessoParteDebitodt.getId_Processo();
					
					if (Funcoes.StringToInt(ProcessoParteDebitodt.getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.NENHUM)
						ProcessoParteDebitodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					ProcessoParteDebitone.salvarDebitoPartes(ProcessoParteDebitodt);
					
					ProcessoParteDebitodt.limpar();
					processoDt = null;
					ProcessoParteDebitodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					processoDebitoStatusDt = ProcessoParteDebitone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					if (processoDebitoStatusDt != null) ProcessoParteDebitodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
					
					processoDt = consultarDebitosIdProcesso(request, UsuarioSessao, ProcessoParteDebitodt, ProcessoParteDebitone, id_processo);
					
					if (id_ProcessoParteDebito == null || id_ProcessoParteDebito.trim().length() == 0) {
						request.setAttribute("MensagemOk", "Débito cadastrado com sucesso.");
					} else {
						request.setAttribute("MensagemOk", "Débito atualizado com sucesso.");	
					}					
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			//Consulta os débitos já cadastrados para partes de processo
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA) {
						String[] lisNomeBusca = {"Nome da Parte", "CPF/CNPJ da Parte", "Número do Processo", "Guia", "Serventia", "Tipo de Débito", "Status" };
						request.setAttribute("lisNomeBusca", lisNomeBusca);
					} else {
						String[] lisNomeBusca = {"Nome da Parte", "CPF/CNPJ da Parte", "Número do Processo", "Guia"};
						request.setAttribute("lisNomeBusca", lisNomeBusca);
					}					
					String[] lisDescricao = {"Número do Processo", "Tipo de Débito", "Status", "Nome da Parte", "Serventia", "Guia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoParteDebitoConsulta");
					request.setAttribute("tempBuscaDescricao", "ProcessoParteDebito");
					request.setAttribute("tempBuscaPrograma", "Processo Parte Debito"); // Foram adicionados os espaços para não aparecer o botão excluir.
					request.setAttribute("tempRetorno", "ProcessoParteDebitoFinanceiro");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) != ServentiaTipoDt.DIRETORIA_FINANCEIRA) {
						stNomeBusca5 = UsuarioSessao.getServentia();
					}
					stTemp = ProcessoParteDebitone.consultarDebitosProcessoParteJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, stNomeBusca5, stNomeBusca6, stNomeBusca7, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);
					return;
				}
				break;

			case Configuracao.Excluir:
				stId = request.getParameter("Id_ProcessoParteDebito");
				if (stId != null && stId.length() > 0) {
					ProcessoParteDebitoDt processoParteDebitodtConsulta = ProcessoParteDebitone.consultarId(stId);
					if (processoParteDebitodtConsulta != null) {
						ProcessoParteDebitodt.copiar(processoParteDebitodtConsulta);
					}					
					request.setAttribute("Mensagem", "Clique para confirmar a exclusão do Débito");
					stAcao = "/WEB-INF/jsptjgo/ProcessoParteDebitoExclusao.jsp";
				}
				break;

			//Excluir Débito cadastrado para parte
			case Configuracao.ExcluirResultado:
				Mensagem = ProcessoParteDebitone.VerificarExclusao(ProcessoParteDebitodt, UsuarioSessao);
				if (Mensagem.length() == 0) {
					String numeroProcessoCompleto = ProcessoParteDebitodt.getProcessoNumeroCompleto();
					ProcessoParteDebitone.excluir(ProcessoParteDebitodt);
					
					ProcessoParteDebitodt.limpar();
					processoDt = null;
					
					ProcessoParteDebitodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					processoDebitoStatusDt = ProcessoParteDebitone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					if (processoDebitoStatusDt != null) ProcessoParteDebitodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
					
					processoDt = consultarDebitosNumeroProcesso(request, UsuarioSessao, ProcessoParteDebitodt, ProcessoParteDebitone, numeroProcessoCompleto);	
					
					request.setAttribute("MensagemOk", "Débito Excluído com Sucesso.");	
				} else {
					request.setAttribute("MensagemErro", Mensagem);
				}	
				break;

			case (ProcessoDebitoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Processo Débito"};
					String[] lisDescricao = {"Processo Débito"};;
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoDebito");
					request.setAttribute("tempBuscaDescricao", "ProcessoDebito");
					request.setAttribute("tempBuscaPrograma", "ProcessoDebito");
					request.setAttribute("tempRetorno", "ProcessoParteDebitoFinanceiro");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoDebitoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ProcessoParteDebitone.consultarDescricaoProcessoDebitoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);											
					
					return;
				}
				break;
				
			case Configuracao.Curinga6:
				stId = request.getParameter("Id_ProcessoParteDebito");
				if (stId != null && stId.length() > 0) {
					ProcessoParteDebitoDt processoParteDebitodtConsulta = ProcessoParteDebitone.consultarId(stId);
					if (processoParteDebitodtConsulta != null) {
						ProcessoParteDebitodt.copiar(processoParteDebitodtConsulta);
					}										
				}
				break;
			case Configuracao.Curinga7:
				ProcessoParteDebitodt.limparParcial();
				ProcessoParteDebitodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				processoDebitoStatusDt = ProcessoParteDebitone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				if (processoDebitoStatusDt != null) {
					ProcessoParteDebitodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
					ProcessoParteDebitodt.setId_ProcessoDebitoStatus(processoDebitoStatusDt.getId());
				}
				break;
			default:
				String processoNumero = request.getParameter("ProcessoNumero");
				stId = request.getParameter("Id_ProcessoParteDebitoConsulta");
				boolean consultarDados = false;
				if (stId != null && paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase("null")) {
					if (!stId.equalsIgnoreCase(ProcessoParteDebitodt.getId())) {
						ProcessoParteDebitodt.limpar();
						processoDt = null;
						ProcessoParteDebitodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
						ProcessoParteDebitodt = ProcessoParteDebitone.consultarId(stId);
						if (ProcessoParteDebitodt != null) {
							processoNumero = ProcessoParteDebitodt.getProcessoNumeroCompleto();
							if (ProcessoParteDebitodt.getId_Processo() != null && ProcessoParteDebitodt.getId_Processo().trim().length() > 0) {
								processoDt = ProcessoParteDebitone.consultarIdCompleto(ProcessoParteDebitodt.getId_Processo());	
							}							
							consultarDados = true;
						}	
					}										
				} else {
					ProcessoParteDebitodt.limpar();
					processoDt = null;
					ProcessoParteDebitodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					processoDebitoStatusDt = ProcessoParteDebitone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					if (processoDebitoStatusDt != null) {
						ProcessoParteDebitodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
						ProcessoParteDebitodt.setId_ProcessoDebitoStatus(processoDebitoStatusDt.getId());
					}
				}
				// Verifica se foi chamado a partir das opções do processo
				if (request.getParameter("PassoEditar") != null && request.getParameter("PassoEditar").trim().equalsIgnoreCase("1")) {
					ProcessoDt processoCompleto = (ProcessoDt) request.getSession().getAttribute("processoDt");
					if (processoCompleto != null) {
						if (processoCompleto.getId_Serventia().equals(UsuarioSessao.getUsuarioDt().getId_Serventia()) ||
							Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA) {
							ProcessoParteDebitodt.setId_Processo(processoCompleto.getId_Processo());
							ProcessoParteDebitodt.setProcessoNumero(processoCompleto.getProcessoNumeroCompleto());
							ProcessoParteDebitodt.setListaPartesPromoventes(processoCompleto.getListaPolosAtivos());
							ProcessoParteDebitodt.setListaPartesPromovidas(processoCompleto.getListaPolosPassivos());
							//alteração leandro
							ProcessoParteDebitodt.setListaPartesComDebito(ProcessoParteDebitone.consultarPartesComDebitos(processoCompleto.getId_Processo()));
							ProcessoParteDebitodt.setListaGuiasProcesso(ProcessoParteDebitone.consultarGuiasFinais_GRSSimplificada_AguardandoPagamento(processoCompleto.getId_Processo()));
							//**************************
							ProcessoParteDebitodt.setListaProcessoDebito(ProcessoParteDebitone.consultarProcessoDebito(""));
							ProcessoParteDebitodt.setListaProcessoDebitoStatus(ProcessoParteDebitone.consultarProcessoDebitoStatus(""));
							processoDt = processoCompleto;
						} else request.setAttribute("MensagemErro", "Processo é de outra Serventia.");
					} else request.setAttribute("MensagemErro", "Processo não localizado.");
				} else if (request.getParameter("PassoEditar") != null && request.getParameter("PassoEditar").trim().equalsIgnoreCase("2")) {
					String id_Processo = request.getParameter("Id_Processo");
					processoDt = consultarDebitosIdProcesso(request, UsuarioSessao, ProcessoParteDebitodt, ProcessoParteDebitone, id_Processo);
				} else {
					//Se não tiver Id_Processo setado deve consultar dados do processos e suas partes
					if (processoNumero != null && processoNumero.length() > 0 && (consultarDados || (ProcessoParteDebitodt == null || ProcessoParteDebitodt.getId_Processo() == null || ProcessoParteDebitodt.getId_Processo().trim().length() == 0))) {
						ProcessoParteDebitodt.setProcessoNumero(processoNumero);
						processoDt = consultarDebitosNumeroProcesso(request, UsuarioSessao, ProcessoParteDebitodt, ProcessoParteDebitone, processoNumero);
					}
				}				
				break;
		}

		request.getSession().setAttribute("ProcessoParteDebitodt", ProcessoParteDebitodt);
		request.getSession().setAttribute("ProcessoParteDebitone", ProcessoParteDebitone);
		request.getSession().setAttribute("processoDebitoDt", processoDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

    protected ProcessoDt consultarDebitosIdProcesso(HttpServletRequest request, UsuarioNe UsuarioSessao, ProcessoParteDebitoDt ProcessoParteDebitodt, ProcessoParteDebitoNe ProcessoParteDebitone, String id_Processo) throws Exception {
		ProcessoDt processoCompleto = ProcessoParteDebitone.consultarIdCompleto(id_Processo);
		if (processoCompleto != null) {
			if (processoCompleto.getId_Serventia().equals(UsuarioSessao.getUsuarioDt().getId_Serventia()) ||
					Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA) {
				ProcessoParteDebitodt.setId_Processo(processoCompleto.getId_Processo());
				ProcessoParteDebitodt.setProcessoNumero(processoCompleto.getProcessoNumero());
				ProcessoParteDebitodt.setProcessoNumeroCompleto(processoCompleto.getProcessoNumeroCompleto());
				ProcessoParteDebitodt.setListaPartesPromoventes(processoCompleto.getListaPolosAtivos());
				ProcessoParteDebitodt.setListaPartesPromovidas(processoCompleto.getListaPolosPassivos());
				ProcessoParteDebitodt.setListaPartesComDebito(ProcessoParteDebitone.consultarPartesComDebitos(processoCompleto.getId_Processo()));
				ProcessoParteDebitodt.setListaGuiasProcesso(ProcessoParteDebitone.consultarGuiasFinais_GRSSimplificada_AguardandoPagamento(processoCompleto.getId_Processo()));
				ProcessoParteDebitodt.setListaProcessoDebito(ProcessoParteDebitone.consultarProcessoDebito(""));
				ProcessoParteDebitodt.setListaProcessoDebitoStatus(ProcessoParteDebitone.consultarProcessoDebitoStatus(""));
				request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));				
			} else request.setAttribute("MensagemErro", "Processo é de outra Serventia.");
		} else request.setAttribute("MensagemErro", "Processo não localizado.");
		return processoCompleto;
	}
	
    protected ProcessoDt consultarDebitosNumeroProcesso(HttpServletRequest request, UsuarioNe UsuarioSessao, ProcessoParteDebitoDt ProcessoParteDebitodt, ProcessoParteDebitoNe ProcessoParteDebitone, String processoNumero) throws Exception {
		//Verifica se Dígito Verificador foi digitado
		if (processoNumero.indexOf(".") > 0) {
			ProcessoDt processoConsultado = ProcessoParteDebitone.consultarProcessoNumero(processoNumero);							
			if (processoConsultado != null && processoConsultado.temId_Processo()) {
				boolean ehOutraServentia = false;
				if (processoConsultado.getId_Serventia().equals(UsuarioSessao.getUsuarioDt().getId_Serventia()) ||
						Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA) {
					ProcessoParteDebitodt.setId_Processo(processoConsultado.getId_Processo());
					ProcessoParteDebitodt.setProcessoNumero(processoConsultado.getProcessoNumero());
					ProcessoParteDebitodt.setProcessoNumeroCompleto(processoConsultado.getProcessoNumeroCompleto());
					ProcessoParteDebitodt.setListaPartesPromoventes(processoConsultado.getListaPolosAtivos());
					ProcessoParteDebitodt.setListaPartesPromovidas(processoConsultado.getListaPolosPassivos());
					ProcessoParteDebitodt.setListaPartesComDebito(ProcessoParteDebitone.consultarPartesComDebitos(processoConsultado.getId_Processo()));
					ProcessoParteDebitodt.setListaGuiasProcesso(ProcessoParteDebitone.consultarGuiasFinais_GRSSimplificada_AguardandoPagamento(processoConsultado.getId_Processo()));
					ProcessoParteDebitodt.setListaProcessoDebito(ProcessoParteDebitone.consultarProcessoDebito(""));
					ProcessoParteDebitodt.setListaProcessoDebitoStatus(ProcessoParteDebitone.consultarProcessoDebitoStatus(""));
					ehOutraServentia = false;
					return processoConsultado;
				} else {
					ehOutraServentia = true;
				}
				if (ehOutraServentia) {
					request.setAttribute("MensagemErro", "Processo é de outra Serventia.");							
				}
			} else {
				request.setAttribute("MensagemErro", "Processo não localizado.");						
			}
		} else {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
		}
		return null;
	}
}
