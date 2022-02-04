package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoSPGDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoParteDebitoFisicoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteDebitoFisicoCt extends Controle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3698338636182889640L;

	@Override
	public int Permissao() {
		return ProcessoParteDebitoFisicoDt.CodigoPermissao;
	}

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteDebitoFisicoDt ProcessoParteDebitoFisicodt;
		ProcessoParteDebitoFisicoNe ProcessoParteDebitoFisicone;
		ProcessoDebitoStatusDt processoDebitoStatusDt;

		String Mensagem = "";
		String stId = "";
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String stNomeBusca5 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		if(request.getParameter("nomeBusca5") != null) stNomeBusca5 = request.getParameter("nomeBusca5");

		String stAcao = "/WEB-INF/jsptjgo/ProcessoParteDebitoFisico.jsp";
		request.setAttribute("tempPrograma", "ProcessoParteDebitoFisico");

		ProcessoParteDebitoFisicone = (ProcessoParteDebitoFisicoNe) request.getSession().getAttribute("ProcessoParteDebitoFisicone");
		if (ProcessoParteDebitoFisicone == null) ProcessoParteDebitoFisicone = new ProcessoParteDebitoFisicoNe();

		ProcessoParteDebitoFisicodt = (ProcessoParteDebitoFisicoDt) request.getSession().getAttribute("ProcessoParteDebitoFisicodt");
		if (ProcessoParteDebitoFisicodt == null) ProcessoParteDebitoFisicodt = new ProcessoParteDebitoFisicoDt();

		ProcessoParteDebitoFisicodt.setId(request.getParameter("Id_ProcessoParteDebitoFisico"));
		ProcessoParteDebitoFisicodt.setId_ProcessoDebito(request.getParameter("Id_ProcessoDebito"));
		ProcessoParteDebitoFisicodt.setProcessoDebito(request.getParameter("ProcessoDebito"));
		
		ProcessoParteDebitoFisicodt.setDividaSolidaria(request.getParameter("dividaSolidaria"));
		
		if (request.getParameter("tipoDeParte") != null && request.getParameter("tipoDeParte").trim().length() > 0) {
			ProcessoParteDebitoFisicodt.setTipoParte(request.getParameter("tipoDeParte"));			
			if (ProcessoParteDebitoFisicodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO))) {
				ProcessoParteDebitoFisicodt.setId_ProcessoParte(request.getParameter("idProcessoPartePoloAtivo"));
				ProcessoParteDebitoFisicodt.setNome(request.getParameter("nomeProcessoPartePoloAtivo"));	
			} else {
				ProcessoParteDebitoFisicodt.setId_ProcessoParte(request.getParameter("idProcessoPartePoloPassivo"));
				ProcessoParteDebitoFisicodt.setNome(request.getParameter("nomeProcessoPartePoloPassivo"));
			}							
		}	
		
		ProcessoParteDebitoFisicodt.setNumeroGuia(request.getParameter("NumeroGuia"));
		ProcessoParteDebitoFisicodt.setProcessoNumeroPROAD(request.getParameter("ProcessoNumeroProad"));
		
		ProcessoParteDebitoFisicodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteDebitoFisicodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:
				ProcessoParteDebitoFisicodt.limpar();
				ProcessoParteDebitoFisicodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				processoDebitoStatusDt = ProcessoParteDebitoFisicone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				if (ProcessoParteDebitoFisicodt != null) {
					ProcessoParteDebitoFisicodt.setProcessoDebitoStatus(ProcessoParteDebitoFisicodt.getProcessoDebitoStatus());
					ProcessoParteDebitoFisicodt.setId_ProcessoDebitoStatus(ProcessoParteDebitoFisicodt.getId());
				}
				break;				

			case Configuracao.Salvar:
				if (ProcessoParteDebitoFisicodt.getId() == null || ProcessoParteDebitoFisicodt.getId().trim().length() == 0) {
					request.setAttribute("Mensagem", "Clique para confirmar o cadastro do Débito.");	
				} else {
					request.setAttribute("Mensagem", "Clique para confirmar a atualização do Débito.");
				}
				break;

			//Salva Débito para Parte
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoParteDebitoFisicone.VerificarCadastroServentia(ProcessoParteDebitoFisicodt, UsuarioSessao);
				if (Mensagem.length() == 0) {
					String id_ProcessoParteDebito = ProcessoParteDebitoFisicodt.getId();
					String numeroProcesso = ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto();
					
					if (Funcoes.StringToInt(ProcessoParteDebitoFisicodt.getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.NENHUM)
						ProcessoParteDebitoFisicodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					
					ProcessoParteDebitoFisicone.salvarDebitoPartes(ProcessoParteDebitoFisicodt);
					
					ProcessoParteDebitoFisicodt.limpar();
					
					ProcessoParteDebitoFisicodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					processoDebitoStatusDt = ProcessoParteDebitoFisicone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
					if (processoDebitoStatusDt != null) ProcessoParteDebitoFisicodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
					
					consultarDebitosNumeroProcesso(request, UsuarioSessao, ProcessoParteDebitoFisicodt, ProcessoParteDebitoFisicone, numeroProcesso);
					
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
					String[] lisNomeBusca = {"Nome da Parte", "CPF/CNPJ da Parte", "Número do Processo", "Serventia", "Guia"};
					String[] lisDescricao = {"Número do Processo", "Tipo de Débito", "Nome da Parte", "Serventia", "Guia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoParteDebitoConsulta");
					request.setAttribute("tempBuscaDescricao", "ProcessoParteDebitoFisico");
					request.setAttribute("tempBuscaPrograma", "Processo Parte Debito Fisico"); // Foram adicionados os espaços para não aparecer o botão excluir.
					request.setAttribute("tempRetorno", "ProcessoParteDebitoFisico");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ProcessoParteDebitoFisicone.consultarDebitosProcessoParteJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, stNomeBusca5, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);
					return;
				}
				break;

			case Configuracao.Excluir:
				stId = request.getParameter("Id_ProcessoParteDebitoFisico");
				if (stId != null && stId.length() > 0) {
					ProcessoParteDebitoFisicoDt processoParteDebitodtConsulta = ProcessoParteDebitoFisicone.consultarId(stId);
					if (processoParteDebitodtConsulta != null) {
						ProcessoParteDebitoFisicodt.copiar(processoParteDebitodtConsulta);
					}					
					request.setAttribute("Mensagem", "Clique para confirmar a exclusão do Débito");
					stAcao = "/WEB-INF/jsptjgo/ProcessoParteDebitoFisicoExclusao.jsp";
				}
				break;

			//Excluir Débito cadastrado para parte
			case Configuracao.ExcluirResultado:
				if (ProcessoParteDebitoFisicodt.getId().length() > 0) {
					Mensagem = ProcessoParteDebitoFisicone.VerificarExclusao(ProcessoParteDebitoFisicodt, UsuarioSessao);
					if (Mensagem.length() == 0) {
						String numeroProcessoCompleto = ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto();
						ProcessoParteDebitoFisicone.excluir(ProcessoParteDebitoFisicodt);
						
						ProcessoParteDebitoFisicodt.limpar();
						
						ProcessoParteDebitoFisicodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
						processoDebitoStatusDt = ProcessoParteDebitoFisicone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
						if (processoDebitoStatusDt != null) ProcessoParteDebitoFisicodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
						
						consultarDebitosNumeroProcesso(request, UsuarioSessao, ProcessoParteDebitoFisicodt, ProcessoParteDebitoFisicone, numeroProcessoCompleto);	
						request.setAttribute("MensagemOk", "Débito Excluído com Sucesso.");	
					} else {
						request.setAttribute("MensagemErro", Mensagem);
					}					
				} else {
					request.setAttribute("MensagemErro", "Nenhum Débito foi selecionado para exclusão.");
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
					request.setAttribute("tempRetorno", "ProcessoParteDebitoFisico");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoDebitoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ProcessoParteDebitoFisicone.consultarDescricaoProcessoDebitoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);											
					
					return;
				}
				break;
				
			case Configuracao.Curinga6:
				stId = request.getParameter("Id_ProcessoParteDebitoFisico");
				if (stId != null && stId.length() > 0) {
					ProcessoParteDebitoFisicoDt processoParteDebitodtConsulta = ProcessoParteDebitoFisicone.consultarId(stId);
					if (processoParteDebitodtConsulta != null) {
						ProcessoParteDebitoFisicodt.copiar(processoParteDebitodtConsulta);
					}										
				}
				break;
			case Configuracao.Curinga7:
				ProcessoParteDebitoFisicodt.limparParcial();
				ProcessoParteDebitoFisicodt.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				processoDebitoStatusDt = ProcessoParteDebitoFisicone.consultarProcessoDebitoStatusId(String.valueOf(ProcessoDebitoStatusDt.NOVO));
				if (processoDebitoStatusDt != null) ProcessoParteDebitoFisicodt.setProcessoDebitoStatus(processoDebitoStatusDt.getProcessoDebitoStatus());
				break;
			default:
				String processoNumero = request.getParameter("ProcessoNumero");
				stId = request.getParameter("Id_ProcessoParteDebitoConsulta");
				boolean consultarDados = false;
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase("null") && !stId.equalsIgnoreCase(ProcessoParteDebitoFisicodt.getId())) {
					ProcessoParteDebitoFisicodt.limpar();
					ProcessoParteDebitoFisicodt = ProcessoParteDebitoFisicone.consultarId(stId);
					if (ProcessoParteDebitoFisicodt != null) {
						processoNumero = ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto();	
						consultarDados = true;
					}					
				}
				//Se não tiver número do processo setado deve consultar dados do processos e suas partes
				if (processoNumero != null && processoNumero.length() > 0 && (consultarDados || (ProcessoParteDebitoFisicodt == null || ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto() == null || ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto().trim().length() == 0))) {
					consultarDebitosNumeroProcesso(request, UsuarioSessao, ProcessoParteDebitoFisicodt, ProcessoParteDebitoFisicone, processoNumero);
				}			
				break;
		}

		request.getSession().setAttribute("ProcessoParteDebitoFisicodt", ProcessoParteDebitoFisicodt);
		request.getSession().setAttribute("ProcessoParteDebitoFisicone", ProcessoParteDebitoFisicone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

    protected void consultarDebitosNumeroProcesso(HttpServletRequest request, UsuarioNe UsuarioSessao, ProcessoParteDebitoFisicoDt ProcessoParteDebitodt, ProcessoParteDebitoFisicoNe ProcessoParteDebitone, String processoNumero) throws Exception {
		processoNumero = Funcoes.obtenhaSomenteNumeros(processoNumero);
		processoNumero = Funcoes.formataNumeroCompletoProcesso(processoNumero);
		//Verifica se Dígito Verificador foi digitado
		if (processoNumero.indexOf(".") > 0) {
			ProcessoSPGDt processoConsultado = ProcessoParteDebitone.consultarProcessoNumero(processoNumero);							
			if (processoConsultado != null && processoConsultado.getNumeroProcessoCompletoDt() != null) {
				/*if (processoConsultado.getIdServentia() > 0) {
					ServentiaDt serventiaDt = ProcessoParteDebitone.consultarServentiaCodigoExterno(String.valueOf(processoConsultado.getIdServentia()));
					if (serventiaDt != null && 
						!(UsuarioSessao.getId_Serventia().trim().equalsIgnoreCase(serventiaDt.getId().trim()) ||
						  Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA)) {
						request.setAttribute("MensagemErro", "Processo localizado no SPG, porém não pertence a essa serventia. \n Para cadastrar o débito a serventia acessada deve ser a " + serventiaDt.getServentia() + ".");	
						return;
					}
				}*/			
				ProcessoParteDebitodt.setProcessoNumeroCompleto(processoNumero);
				ProcessoParteDebitodt.setCodigoExternoServentia(String.valueOf(processoConsultado.getIdServentia()));
				ProcessoParteDebitodt.setDescricaoServentia(processoConsultado.getServentia());
				ProcessoParteDebitodt.setListaPartesPromoventes(processoConsultado.getPolosAtivos());
				ProcessoParteDebitodt.setListaPartesPromovidas(processoConsultado.getPolosPassivos());
				ProcessoParteDebitodt.setListaPartesComDebito(ProcessoParteDebitone.consultarPartesComDebitos(processoNumero));
				ProcessoParteDebitodt.setListaGuiasProcesso(ProcessoParteDebitone.consultarGuias(processoConsultado.getNumeroProcessoSPG()));
				ProcessoParteDebitodt.setListaProcessoDebito(ProcessoParteDebitone.consultarProcessoDebito(""));
				processoConsultado = null;	
			} else {
				request.setAttribute("MensagemErro", "Processo não localizado no SPG.");						
			}
		} else {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
		}
	}	
}
