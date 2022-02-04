package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoBeneficioDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoParteBeneficioNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla o cadastro de Benefícios para Partes
 * @author msapaula
 * 06/10/2009 10:41:01
 */
public class ProcessoParteBeneficioCt extends ProcessoParteBeneficioCtGen {

    private static final long serialVersionUID = -7879572461141687687L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteBeneficioDt ProcessoParteBeneficiodt;
		ProcessoParteBeneficioNe ProcessoParteBeneficione;

		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stAcao = "/WEB-INF/jsptjgo/ProcessoParteBeneficio.jsp";

		request.setAttribute("tempPrograma", "ProcessoParteBeneficio");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		ProcessoParteBeneficione = (ProcessoParteBeneficioNe) request.getSession().getAttribute("ProcessoParteBeneficione");
		if (ProcessoParteBeneficione == null) ProcessoParteBeneficione = new ProcessoParteBeneficioNe();

		ProcessoParteBeneficiodt = (ProcessoParteBeneficioDt) request.getSession().getAttribute("ProcessoParteBeneficiodt");
		if (ProcessoParteBeneficiodt == null) ProcessoParteBeneficiodt = new ProcessoParteBeneficioDt();

		ProcessoParteBeneficiodt.setId_ProcessoBeneficio(request.getParameter("Id_ProcessoBeneficio"));
		ProcessoParteBeneficiodt.setProcessoBeneficio(request.getParameter("ProcessoBeneficio"));
		ProcessoParteBeneficiodt.setNome(request.getParameter("NomeParte"));
		ProcessoParteBeneficiodt.setCpfParte(request.getParameter("CpfParte"));
		ProcessoParteBeneficiodt.setDataInicial(request.getParameter("DataInicial"));
		ProcessoParteBeneficiodt.setPartesBeneficio(request.getParameterValues("partesBeneficio"));
		ProcessoParteBeneficiodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteBeneficiodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

//---------------------------------------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {
		
			case Configuracao.Novo:
				ProcessoParteBeneficiodt.limpar();
				break;

			case Configuracao.Salvar:
				if (request.getParameter("AJAX")!=null) {
					ProcessoParteBeneficioDt dt = new ProcessoParteBeneficioDt();
					super.atribuiRequest(request, dt);
					Mensagem=ProcessoParteBeneficione.Verificar(dt); 
					if (Mensagem.length()==0){
						dt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						dt.setIpComputadorLog(request.getRemoteAddr());
						ProcessoParteBeneficione.salvar(dt);
						enviarJSON(response, dt.toJson());
					}else {
						throw new MensagemException( Mensagem );
					}				
					return;
				}else {
					
				}
				request.setAttribute("Mensagem", "Clique para confirmar o cadastro do Benefício.");
				break;
				
			//Salva Benefício de partes
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoParteBeneficione.Verificar(ProcessoParteBeneficiodt);
				if (Mensagem.length() == 0) {
					
					String id_ProcessoParteBeneficio = ProcessoParteBeneficiodt.getId();
					String id_processo = ProcessoParteBeneficiodt.getId_Processo();
					ProcessoParteBeneficione.salvarBeneficioPartes(ProcessoParteBeneficiodt);
					ProcessoParteBeneficiodt.limpar();					
					consultarBeneficiosIdProcesso(request, UsuarioSessao, ProcessoParteBeneficiodt, ProcessoParteBeneficione, id_processo);
				
					if (id_ProcessoParteBeneficio == null || id_ProcessoParteBeneficio.trim().length() == 0) {
						request.setAttribute("MensagemOk", "Benefício cadastrado com sucesso.");
					} else {
						request.setAttribute("MensagemOk", "Benefício atualizado com sucesso.");	
					}					
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			//Consulta os benefícios já cadastrados para partes de processo
			case Configuracao.Localizar:
				String stPasso = request.getParameter("Passo"); 						
				if (stPasso==null){
					
					String[] lisNomeBusca = {"Nome Parte", "CPF Parte"};
					String[] lisDescricao = {"Número Processo", "Tipo Benefício", "Nome Parte", "Data Inicial", "Data Final"};	
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";					
					String stPemissao = String.valueOf(Configuracao.Localizar);
					atribuirJSON(request, "Id_ProcessoParteBeneficio", "Processo Parte Beneficio", "ProcessoParteBeneficio", "ProcessoParteBeneficio", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
				}else if (stPasso.contains("sub_ben") ){
					String id_processo_parte = request.getParameter("Id_ProcessoParte");
					String stTemp = ProcessoParteBeneficione.consultarBeneficiosProcessoParteJSON(id_processo_parte);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}else{
					
					String stTemp = "";
					stTemp = ProcessoParteBeneficione.consultarBeneficiosProcessoParteJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);					
					enviarJSON(response, stTemp);
					return;
				}
				break;

			case Configuracao.Excluir:
				
				stId = request.getParameter("Id_ProcessoParteBeneficio");
				if (stId != null && stId.length() > 0) {
					
					ProcessoParteBeneficiodt = ProcessoParteBeneficione.consultarId(stId);
					request.setAttribute("Mensagem", "Clique para confirmar a exclusão do Benefício");
					stAcao = "/WEB-INF/jsptjgo/ProcessoParteBeneficioExclusao.jsp";
				}
				break;

			//Excluir Benefício cadastrado para parte
			case Configuracao.ExcluirResultado:
				
				if (ProcessoParteBeneficiodt.getId().length() > 0) {
					if (ProcessoParteBeneficiodt.getId_Serventia().equals(UsuarioSessao.getUsuarioDt().getId_Serventia())) {
						
						ProcessoDt processoCompleto = (ProcessoDt) request.getSession().getAttribute("processoDt");
						ProcessoParteBeneficione.excluir(ProcessoParteBeneficiodt);
						ProcessoParteBeneficiodt.limpar();
						consultarBeneficiosIdProcesso(request, UsuarioSessao, ProcessoParteBeneficiodt, ProcessoParteBeneficione, processoCompleto.getId());
						
						request.setAttribute("MensagemOk", "Benefício Excluído com Sucesso.");
					} else request.setAttribute("MensagemErro", "Não é possível excluir um Benefício de Processo de outra Serventia.");
				} else request.setAttribute("MensagemErro", "Nenhum Benefício foi selecionado para exclusão.");
				break;

			case (ProcessoBeneficioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo")==null){
					
					String[] lisNomeBusca = {"Processo Benefício"};
					String[] lisDescricao = {"Processo Benefício"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";					
					String stPemissao = String.valueOf(ProcessoBeneficioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_ProcessoBeneficio", "ProcessoBeneficio", "ProcessoBeneficio", "ProcessoParteBeneficio", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
				}else{
					
					String stTemp = "";
					stTemp = ProcessoParteBeneficione.consultarDescricaoProcessoBeneficioJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);					
					return;
				}				
				break;
				
			case Configuracao.Curinga6:
				
				stId = request.getParameter("Id_ProcessoParteBeneficio");
				if (stId != null && stId.length() > 0) {					
					ProcessoParteBeneficioDt processoParteBeneficiodtConsulta = ProcessoParteBeneficione.consultarId(stId);
					
					if (processoParteBeneficiodtConsulta != null) {						
						ProcessoParteBeneficiodt.copiar(processoParteBeneficiodtConsulta);
					}										
				}
				break;

			default:
				
				stId = request.getParameter("Id_ProcessoParteBeneficio");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoParteBeneficiodt.getId())) {
					ProcessoParteBeneficiodt.limpar();
					ProcessoParteBeneficiodt = ProcessoParteBeneficione.consultarId(stId);
				}
				
				// Verifica se foi chamado a partir das opções do processo
				if (request.getParameter("PassoEditar") != null && request.getParameter("PassoEditar").trim().equalsIgnoreCase("1")) {
					ProcessoDt processoCompleto = (ProcessoDt) request.getSession().getAttribute("processoDt");
					if (processoCompleto != null) {
						if (processoCompleto.getId_Serventia().equals(UsuarioSessao.getUsuarioDt().getId_Serventia())) {
							
							ProcessoParteBeneficiodt.setId_Processo(processoCompleto.getId_Processo());
							ProcessoParteBeneficiodt.setProcessoNumero(processoCompleto.getProcessoNumeroCompleto());
							ProcessoParteBeneficiodt.setListaPartesPromoventes(processoCompleto.getListaPolosAtivos());
							ProcessoParteBeneficiodt.setListaPartesPromovidas(processoCompleto.getListaPolosPassivos());
							//alteração leandro
							ProcessoParteBeneficiodt.setListaPartesComBeneficio(ProcessoParteBeneficione.consultarPartesComBeneficio(processoCompleto.getId_Processo()));
							//**************************
							processoCompleto = null;
							
						} else request.setAttribute("MensagemErro", "Processo é de outra Serventia.");
					} else request.setAttribute("MensagemErro", "Processo não localizado.");
				} else {
					
					String processoNumero = request.getParameter("ProcessoNumero");
					//Se não tiver Id_Processo setado deve consultar dados do processos e suas partes
					if (processoNumero != null && processoNumero.length() > 0 && ProcessoParteBeneficiodt.getId_Processo().length() == 0) {
						ProcessoParteBeneficiodt.setProcessoNumero(processoNumero);
						//Verifica se Dígito Verificador foi digitado
						if (processoNumero.indexOf(".") > 0) {
							ProcessoDt processoConsultado = ProcessoParteBeneficione.consultarProcessoNumero(processoNumero);			
							if (processoConsultado.getId_Processo() != null && !processoConsultado.getId_Processo().equals("")) {								
								boolean ehOutraServentia = false;
								if (processoConsultado.getId_Serventia().equals(UsuarioSessao.getUsuarioDt().getId_Serventia())) {
									
									ProcessoParteBeneficiodt.setId_Processo(processoConsultado.getId_Processo());
									ProcessoParteBeneficiodt.setProcessoNumero(processoConsultado.getProcessoNumeroCompleto());
									ProcessoParteBeneficiodt.setListaPartesPromoventes(processoConsultado.getListaPolosAtivos());
									ProcessoParteBeneficiodt.setListaPartesPromovidas(processoConsultado.getListaPolosPassivos());
									//alteração leandro
									ProcessoParteBeneficiodt.setListaPartesComBeneficio(ProcessoParteBeneficione.consultarPartesComBeneficio(processoConsultado.getId_Processo()));
									//**************************
									processoConsultado = null;										
									ehOutraServentia = false;
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
					}
				}				
				break;
		}

		request.getSession().setAttribute("ProcessoParteBeneficiodt", ProcessoParteBeneficiodt);
		request.getSession().setAttribute("ProcessoParteBeneficione", ProcessoParteBeneficione);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
    
    protected void consultarBeneficiosIdProcesso(HttpServletRequest request, UsuarioNe UsuarioSessao, ProcessoParteBeneficioDt ProcessoParteBeneficiodt, ProcessoParteBeneficioNe ProcessoParteBeneficione, String id_Processo) throws Exception {
    	
		ProcessoDt processoCompleto = ProcessoParteBeneficione.consultarIdCompleto(id_Processo);
		
		if (processoCompleto != null) {
			if (processoCompleto.getId_Serventia().equals(UsuarioSessao.getUsuarioDt().getId_Serventia()) || Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA) {
				
				ProcessoParteBeneficiodt.setId_Processo(processoCompleto.getId_Processo());
				ProcessoParteBeneficiodt.setProcessoNumero(processoCompleto.getProcessoNumeroCompleto());
				ProcessoParteBeneficiodt.setListaPartesPromoventes(processoCompleto.getListaPolosAtivos());
				ProcessoParteBeneficiodt.setListaPartesPromovidas(processoCompleto.getListaPolosPassivos());
				ProcessoParteBeneficiodt.setListaPartesComBeneficio(ProcessoParteBeneficione.consultarPartesComBeneficios(processoCompleto.getId_Processo()));
				
				processoCompleto = null;
				
				request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
			} else request.setAttribute("MensagemErro", "Processo é de outra Serventia.");
		} else request.setAttribute("MensagemErro", "Processo não localizado.");
	}
    
}
