package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.RacaDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.ProcessoExecucaoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla as ações gerais nos processos de execução penal.
 * Utilizado no cadastro do Processo de Execução Penal; consulta das
 * condenações; Edição dos dados da ação penal durante a edição do evento;
 * Unificação e desmembramento de pena.
 * 
 * @author wcsilva
 * 
 */
public class ProcessoExecucaoCt extends ProcessoExecucaoCtGen {

    private static final long serialVersionUID = -2206787512465823883L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

        ProcessoExecucaoNe processoExecucaoNe;
        ProcessoExecucaoDt processoExecucaoDt;
        ProcessoParteDt processoParteDt = null;

        String Mensagem = "";
        String mensagemRetorno = "Os dados não foram encontrados.";
        int passoEditar = -1;
        int paginaAnterior = 0;
        String stNomeBusca1 = "";
        String stNomeBusca2 = "";
        String stNomeBusca3 = "";
        String posicaoLista = "";
        String cpf = "";
        List listaArquivosInseridos = null;
        List listaAcoesPenais = null;
        List tempList = null;
        byte[] byTemp = null;
        SituacaoAtualExecucaoDt situacaoAtualExecucaoDt = null;

        request.setAttribute("tempRetorno", "ProcessoExecucao");
        request.setAttribute("tempPrograma", "Processo de Execução Penal");
        request.setAttribute("MensagemOk", "");
        request.setAttribute("MensagemErro", "");
        request.setAttribute("tempRetornoProcesso", "BuscaProcesso");
        if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");
        if (request.getParameter("nomeBusca2") != null)	stNomeBusca2 = request.getParameter("nomeBusca2");
        if (request.getParameter("nomeBusca3") != null)	stNomeBusca3 = request.getParameter("nomeBusca3");
        
        // objetos da sessão
        processoExecucaoNe = (ProcessoExecucaoNe) request.getSession().getAttribute("ProcessoExecucaone");
        if (processoExecucaoNe == null) processoExecucaoNe = new ProcessoExecucaoNe();

        processoExecucaoDt = (ProcessoExecucaoDt) request.getSession().getAttribute("ProcessoExecucaodt_PE");
        if (processoExecucaoDt == null) processoExecucaoDt = new ProcessoExecucaoDt();

        // controle acessado a partir da busca do processo. Não é necessário validar dados para cadastro do processo de execução penal.
        if (paginaatual != Configuracao.Curinga6) {
            if (request.getSession().getAttribute("ProcessoPartedt") != null)
                processoParteDt = (ProcessoParteDt) request.getSession().getAttribute("ProcessoPartedt");
            else
                processoParteDt = new ProcessoParteDt();
        } else {
            processoExecucaoDt.setId_ProcessoExecucaoPenal(((ProcessoDt) request.getSession().getAttribute("processoDt")).getId_Processo());
            processoExecucaoDt.setProcessoDt(((ProcessoDt) request.getSession().getAttribute("processoDt")));
        }

		if (request.getParameter("nomeArquivo")!= null )
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");
        
		// verifica o tipo de cadastro: 1- cadastro da guia, 2- processo físico, 3- cálculo de liqudação de pena.
		if (request.getParameter("CadastroTipo") != null)	processoExecucaoDt.setCadastroTipo(Funcoes.StringToInt(request.getParameter("CadastroTipo")));

		String stAcao = getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
		
        // Variáveis auxiliares
        if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
        if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
        posicaoLista = request.getParameter("posicaoLista");
        if (request.getSession().getAttribute("displayNovaCondenacao") == null) request.getSession().setAttribute("displayNovaCondenacao", "none");

        if (request.getSession().getAttribute("SituacaoDt") != null)
			situacaoAtualExecucaoDt = (SituacaoAtualExecucaoDt) request.getSession().getAttribute("SituacaoDt");
        
        setDadosProcessoExecucao(request, UsuarioSessao, processoExecucaoDt);
        setDadosSituacaoAtualExecucao(request, UsuarioSessao, situacaoAtualExecucaoDt);
        // controle acessado a partir da busca do processo: Não é necessário validar dados de cadastro do processo de execução penal.
        if (paginaatual != Configuracao.Curinga6 && paginaAnterior != Configuracao.Curinga6) {
            setPasso(processoExecucaoDt, request.getParameter("Passo1"), request.getParameter("Passo2"), request.getParameter("Passo3"), request.getParameter("Passo4"));
            listaArquivosInseridos = getListaArquivos(request); // Captura arquivos inseridos
            setParametrosAuxiliares(processoParteDt, passoEditar, paginaatual, request, paginaAnterior, UsuarioSessao, processoExecucaoNe, processoExecucaoDt);
        }
        //controla qual a origem da tela de dados do processo de ação penal: pela edição do evento ("2") ou pelo menu ("1")
        String passoEditarSalvarAcao = "";
        if ((request.getSession().getAttribute("PassoEditarSalvarAcao") != null) && (!request.getSession().getAttribute("PassoEditarSalvarAcao").equals(""))) {
            passoEditarSalvarAcao = (String) request.getSession().getAttribute("PassoEditarSalvarAcao");
        }
        
    	stAcao = getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
        
        switch (paginaatual) {
        // Novo Processo, limpar todos os dados
        case Configuracao.Novo:
//        	passoEditarBuscarParte = "1";//pela guia de recolhimento
            if (processoExecucaoDt != null) limparProcessoExecucao(processoExecucaoDt, UsuarioSessao, processoExecucaoNe);
            if (processoParteDt != null) processoParteDt.limpar();
            if (situacaoAtualExecucaoDt != null) situacaoAtualExecucaoDt.limpar();

            // Limpa lista DWR e zera contador Arquivos
            request.getSession().removeAttribute("ListaArquivosDwr");
            request.getSession().removeAttribute("ListaArquivos");
            request.getSession().removeAttribute("Id_ListaArquivosDwr");

            passoEditar = -1;

            if (request.getSession().getAttribute("PaginaPai") != null) {
                request.getSession().removeAttribute("PaginaPai");
            }
            break;

        // Remover partes inseridas
        case Configuracao.Excluir:
        	if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)){
            	processoExecucaoDt.getProcessoDt().setListaPolosPassivos(null);
        	}
            break;

        case Configuracao.SalvarResultado:
            // Salvar dados do Processo de Execução Penal
            processoExecucaoNe.cadastrarProcessoExecucao(processoExecucaoDt, UsuarioSessao.getUsuarioDt(), false, situacaoAtualExecucaoDt);

            // Redireciona para jsp de Processo Cadastrado com sucesso
            stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoCadastrado.jsp";

            // Joga processo no request e limpa o objeto da sessão
            request.setAttribute("ProcessoExecucaodt_PE", processoExecucaoDt);
            processoExecucaoDt = new ProcessoExecucaoDt();
            processoParteDt = new ProcessoParteDt();

            // Limpa lista DWR e zera contador Arquivos
            request.getSession().removeAttribute("ListaArquivosDwr");
            request.getSession().removeAttribute("ListaArquivos");
            request.getSession().removeAttribute("Id_ListaArquivosDwr");
            break;

          //inclui e exclui condenação da lista de condenações
        case Configuracao.LocalizarAutoPai:
            if (posicaoLista.length() == 0) {//adicionar condenação
            	  Mensagem = processoExecucaoNe.adicionarCondenacaoProcesso(request, UsuarioSessao.getUsuarioDt(), paginaAnterior, processoExecucaoDt);
                  if (Mensagem.length() > 0)
                      request.setAttribute("MensagemErro", Mensagem);
            } else {
            	boolean excluirDefinitivo = false;
	        	if (paginaAnterior == Configuracao.Curinga6) {
	        		excluirDefinitivo = true;
	        	}
	        	Mensagem = processoExecucaoNe.excluirCondenacao(posicaoLista, processoExecucaoDt, excluirDefinitivo);
            	if (Mensagem.length() > 0)
                    request.setAttribute("MensagemErro", Mensagem);
            }
            stAcao = getStAcao(request, paginaAnterior);
            paginaatual = getPaginaAtual(request, paginaAnterior);
            break;
        	
        // lista as ações penais do processo de execução penal
        case Configuracao.Curinga6:
        	setListaRegimeRequest(processoExecucaoNe, request);
        	if (passoEditar == 2 || passoEditar == 3 || passoEditar == 4){
            	Mensagem = processoExecucaoNe.podeModificarDados(processoExecucaoDt.getProcessoDt(), UsuarioSessao.getUsuarioDt());
    			if (Mensagem.length() > 0){
    				redireciona(response, "BuscaProcesso?Id_Processo=" + processoExecucaoDt.getProcessoDt().getId() + "&MensagemErro=" + Mensagem);
    				return;
    			} else{
    				listaAcoesPenais = (List)request.getSession().getAttribute("listaAcoesPenais");
	                int posicao = Funcoes.StringToInt(posicaoLista);
	                ProcessoDt processoDt = processoExecucaoDt.getProcessoDt();
	                if(listaAcoesPenais != null && listaAcoesPenais.get(posicao) != null) {
	                	processoExecucaoDt = (ProcessoExecucaoDt) listaAcoesPenais.get(posicao);
	                	processoExecucaoDt.setProcessoDt(processoDt);
	                }
	                passoEditarSalvarAcao = "1";// referencia do acesso de origem: Menu
    			}
        	}
    	
            // 1: Acesso pelo Menu "Manter Registro de Ação Penal": apresenta a lista de ações penais de um processo de execução.
            // 2: Acesso pelo Menu: Carrega os dados da ação penal selecionada e condenações.
            // 3: ativa/desativa ação penal.
            // 4: exclui ação penal.
        	switch (passoEditar) {
	            case 1:
	                // Acesso pelo Menu "Manter Registro de Ação Penal": apresenta a lista de ações penais de um processo de execução
	                listaAcoesPenais = processoExecucaoNe.listarAcoesPenais(processoExecucaoDt.getId_ProcessoExecucaoPenal());
	                stAcao = "/WEB-INF/jsptjgo/ListaCondenacaoProcessoExecucao.jsp";
	                break;
	            case 2: // Acesso pelo Menu: Carrega os dados da ação penal selecionada e condenações.
		                processoExecucaoDt.setListaCondenacoes(processoExecucaoNe.listarCondenacoesDaAcaoPenal(processoExecucaoDt.getId()));
		                stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosAcaoPenal.jsp";
	                break;
	                
	            case 3: //ativa/desativa ação penal
	            	boolean ativo = processoExecucaoDt.isAtivo();
	            	processoExecucaoDt.setAtivo(String.valueOf(!ativo));
	            	processoExecucaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
	            	processoExecucaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
	            	processoExecucaoNe.salvar(processoExecucaoDt);
	            	stAcao = "/WEB-INF/jsptjgo/ListaCondenacaoProcessoExecucao.jsp";
	                break;
	                
	            case 4: // exclui ação penal
	            	if (listaAcoesPenais.size() > 1){
	            		processoExecucaoNe.excluirGuiaRecolhimento(processoExecucaoDt, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
	            		listaAcoesPenais.remove(processoExecucaoDt);
	            	} else {
	                    request.setAttribute("MensagemErro", "Não é possível excluir a Ação Penal. (Motivo: única ação penal do processo!)");
	            	}
	            	stAcao = "/WEB-INF/jsptjgo/ListaCondenacaoProcessoExecucao.jsp";
	                break;
	            default:
	                stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosAcaoPenal.jsp";
	                break;
        	}
            break;
            
        // salva as alterações realizadas nos dados da ação penal e condenações
        case Configuracao.Curinga7:
        	Mensagem = processoExecucaoNe.podeModificarDados(processoExecucaoDt.getProcessoDt(), UsuarioSessao.getUsuarioDt());
			if (Mensagem.length() > 0){
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoExecucaoDt.getProcessoDt().getId() + "&MensagemErro=" + Mensagem);
				return;
			} else{
	        	Mensagem = processoExecucaoNe.verificarDadosSentenca(processoExecucaoDt, false);
	            if (Mensagem.length() > 0) {
	                request.setAttribute("MensagemErro", Mensagem);
	                stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosAcaoPenal.jsp";
	                request.setAttribute("tempRetorno", "ProcessoExecucao");
	                request.setAttribute("tempPrograma", "ProcessoExecucao");
	                request.setAttribute("TituloPagina", "Processo Execução");
	                paginaatual = Configuracao.Curinga6;
	                passoEditar = -1;
	            } else {
	                processoExecucaoNe.salvarComCondenacao(processoExecucaoDt, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
	                request.setAttribute("MensagemOk", "O Processo de Ação Penal Nº: " + processoExecucaoDt.getNumeroAcaoPenal() + " foi alterado com sucesso!");
	
	                switch (Funcoes.StringToInt(passoEditarSalvarAcao)) {
	                case 1: // Acesso pelo Menu
	                    listaAcoesPenais = processoExecucaoNe.listarAcoesPenais(processoExecucaoDt.getId_ProcessoExecucaoPenal());
	                    stAcao = "/WEB-INF/jsptjgo/ListaCondenacaoProcessoExecucao.jsp";
	                    break;
	                }
	            }
			}
            break;
            
        //consulta dos dados das ações penais
        case Configuracao.Curinga8:
        	String idProcesso = ((ProcessoDt) request.getSession().getAttribute("processoDt")).getId_Processo();
        	request.getSession().setAttribute("listaAcoesPenais", listaAcoesPenais);
        	listaAcoesPenais = null;
        	listaAcoesPenais = processoExecucaoNe.listarAcoesPenaisComCondenacoes(idProcesso);
        	if (listaAcoesPenais != null && listaAcoesPenais.size() > 0){
        		request.getSession().setAttribute("listaAcoesPenais", listaAcoesPenais);
        		stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoConsultaAcaoPenal.jsp";
        	} else {
        		redireciona(response, "BuscaProcesso?Id_Processo=" + idProcesso + "&MensagemErro=Nenhuma Ação Penal localizada para este Processo!");
        	}
        	
        	break;
                
        // Consulta as Comarcas disponíveis
        case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
        		if (request.getParameter("Passo")==null){
        			String[] lisNomeBusca = {"Comarca"};
        			String[] lisDescricao = {"Comarca"};
        			stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
        			request.setAttribute("tempBuscaId","Id_Comarca");
        			request.setAttribute("tempBuscaDescricao","Comarca");
        			request.setAttribute("tempBuscaPrograma","Comarca");			
        			request.setAttribute("tempRetorno","ProcessoExecucao");		
        			request.setAttribute("tempDescricaoId","Id");
        			request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
        			request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
        			request.setAttribute("PosicaoPaginaAtual", "0");
        			request.setAttribute("QuantidadePaginas", "0");
        			request.setAttribute("lisNomeBusca", lisNomeBusca);
        			request.setAttribute("lisDescricao", lisDescricao);
        			//Limpa área de distribuição
        			processoExecucaoDt.getProcessoDt().setId_AreaDistribuicao("null");
        			processoExecucaoDt.getProcessoDt().setAreaDistribuicao("null");
        		} else {
        			String stTemp="";
        			
        			stTemp = processoExecucaoNe.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);        				        		
        			enviarJSON(response, stTemp);
        				
        			
        			return;								
        		}
        	}
			break;
        	
        // Consulta os crimes
        case (CrimeExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Crime","Lei","Artigo"};
				String[] lisDescricao = {"Crime"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String stRetorno = redirecionarPaginaPaiJSON(request, paginaAnterior, "-1");
				if (posicaoLista.length() == 0) {
					request.setAttribute("tempBuscaId","Id_CrimeExecucao");
					request.setAttribute("tempBuscaDescricao","CrimeExecucao");
                    request.getSession().setAttribute("displayNovaCondenacao", "block");
                } else {
                    request.setAttribute("tempBuscaId", "Id_CrimeExecucao_" + posicaoLista);
                    request.setAttribute("tempBuscaDescricao", "CrimeExecucao_" + posicaoLista);
                }
				
				if("Passo 1".equalsIgnoreCase(processoExecucaoDt.getPasso1()) && "".equals(passoEditarSalvarAcao)){
					request.setAttribute("tempFluxo2", "/WEB-INF/jsptjgo/ProcessoExecucaoExecpenweb.jsp");
				}else if("Passo 1 OK".equalsIgnoreCase(processoExecucaoDt.getPasso1())){
					request.setAttribute("tempFluxo2", "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp");
				}else if("Passo 2 OK".equalsIgnoreCase(processoExecucaoDt.getPasso2())){
					request.setAttribute("tempFluxo2", "/WEB-INF/jsptjgo/ProcessoExecucaoVisualizar.jsp");
				}else request.setAttribute("tempFluxo2", "/WEB-INF/jsptjgo/ProcessoExecucaoDadosAcaoPenal.jsp");
				
				
				
				request.setAttribute("tempBuscaPrograma","CrimeExecucao");
				request.setAttribute("tempRetorno",stRetorno);
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (CrimeExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				request.setAttribute("tempFluxo1", "1"); //mesmo passo editar
			} else {
				if (paginaAnterior == 6){
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Curinga6));
				}
				String stTemp = "";
				stTemp = processoExecucaoNe.consultarDescricaoCrimeExecucaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
            break;
            
        // Consulta as áreas de distribuição disponíveis para a comarca escolhida
        case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
            if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
                if (processoExecucaoDt.getId_Comarca().length() > 0) {
                	if (request.getParameter("Passo")==null){
    					String[] lisNomeBusca = {"Digite a descrição:"};
    					String[] lisDescricao = {"Área de Distribuição"};
    					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
    					request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
    					request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
    					request.setAttribute("tempBuscaPrograma", "Áreas de Distribuição");

                        request.setAttribute("Id_Comarca", processoExecucaoDt.getId_Comarca());
                        request.setAttribute("Comarca", processoExecucaoDt.getComarca());
                        
    					request.setAttribute("tempRetorno", "ProcessoExecucao");
    					
                        request.setAttribute("tempDescricaoId", "Id");
                        
    					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
    					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
    					request.setAttribute("PosicaoPaginaAtual", "0");
    					request.setAttribute("QuantidadePaginas", "0");
    					request.setAttribute("lisNomeBusca", lisNomeBusca);
    					request.setAttribute("lisDescricao", lisDescricao);
    				}else{
    					String stTemp = "";
    					
    					stTemp = processoExecucaoNe.consultarAreasDistribuicaoExecucaoJSON(stNomeBusca1, processoExecucaoDt.getId_Comarca(), PosicaoPaginaAtual);    					    					
    					enviarJSON(response, stTemp);    						
    					
    					return;
    				}
                	} else
                    request.setAttribute("MensagemErro", "O campo Comarca deve ser informado primeiramente.");
            }
            break;
            
        // Consulta tipos de processo disponíveis na área de distribuição escolhida (Classe CNJ)
        case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
            if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
            	request.setAttribute("MensagemErro", "Não é possível alterar este dado!");
            }
            break;
            
        // Consulta tipos de prioridade disponíveis
        case (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
            if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
            	if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoPrioridade"};
					String[] lisDescricao = {"ProcessoPrioridade", "Codigo"};
//					String[] camposHidden = {"ProcessoPrioridadeCodigo"};
//					request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoPrioridade");
					request.setAttribute("tempBuscaDescricao","ProcessoPrioridade");
					request.setAttribute("tempBuscaPrograma","ProcessoPrioridade");			
					request.setAttribute("tempRetorno","ProcessoExecucao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
//					passoEditar = -1;
					break;
				} else {
					String stTemp="";
					stTemp = processoExecucaoNe.consultarDescricaoProcessoPrioridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
            }
            break;
            
        // Consulta de Cidades - Naturalidade da Parte
        case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
            if (passoEditar == -1) {// passoEditar == 0: cidadeOrigem do processo
                String uf;
                if (request.getParameter("ufBusca") == null)
                    uf = "";
                else
                    uf = request.getParameter("ufBusca");                                
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";			
					String stRetorno = redirecionarPaginaPaiJSON(request, paginaAnterior, "-1");
					String stPemissao = String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					
					atribuirJSON(request, "Id_CidadeOrigem", "CidadeOrigem", "CidadeOrigem", stRetorno, Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					
					request.setAttribute("tempFluxo2", null);
					
					request.setAttribute("setEstadoOrigem", "true");
					
				} else{
					if (paginaAnterior == 6){
						request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Curinga6));
					}
					String stTemp = "";
					stTemp = processoExecucaoNe.consultarDescricaoCidadeJSON(stNomeBusca1, uf, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				                                
            } else if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";					
					
					String stPemissao = String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					
					atribuirJSON(request, "Id_Naturalidade", "CidadeNaturalidade", "Cidade", "ProcessoExecucao?PassoEditar=4", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					
				} else {
					String stTemp = "";
					stTemp = processoExecucaoNe.consultarDescricaoCidadeJSON(stNomeBusca1,"", PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
            }	
            break;
            
        // Consulta de Estado Civil - Usado no cadastro de partes
        case (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
	        	if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Estado Civil"};
					String[] lisDescricao = {"Estado Civil"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPemissao = String.valueOf(EstadoCivilDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_EstadoCivil", "EstadoCivil", "EstadoCivil", "ProcessoExecucao", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = processoExecucaoNe.consultarDescricaoEstadoCivilJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			}
		break;
            
        // Consulta de Profissão - Usado no cadastro de partes
        case (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
            if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
            	if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descrição"};
					String[] lisDescricao = {"Descrição"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Profissao");
					request.setAttribute("tempBuscaDescricao","Profissao");
					request.setAttribute("tempBuscaPrograma","Profissão");			
					request.setAttribute("tempRetorno","ProcessoExecucao?PassoEditar=4");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = processoExecucaoNe.consultarDescricaoProfissaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}   
            }
            break;
            
        // Consulta de Escolaridade - Usado no cadastro de partes
        case (EscolaridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Escolaridade"};
				String[] lisDescricao = {"Escolaridade"};				
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Escolaridade");
				request.setAttribute("tempBuscaDescricao", "Escolaridade");
				request.setAttribute("tempBuscaPrograma", "Escolaridade");
				request.setAttribute("tempRetorno", "ProcessoExecucao?PassoEditar=4");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",  String.valueOf(EscolaridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);							
			}else{
				String stTemp = "";
				stTemp =processoExecucaoNe.consultarDescricaoEscolaridadeJSON(tempNomeBusca, PosicaoPaginaAtual);
								
				enviarJSON(response, stTemp);				
				
				return;
			}
			break;
            
        case (RacaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Raca"};
				String[] lisDescricao = {"Raca"};				
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Raca");
				request.setAttribute("tempBuscaDescricao", "Raca");
				request.setAttribute("tempBuscaPrograma", "Raca");
				request.setAttribute("tempRetorno", "ProcessoExecucao?PassoEditar=4");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",  String.valueOf(RacaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);							
			}else{
				String stTemp = "";
				stTemp =processoExecucaoNe.consultarDescricaoRacaJSON(tempNomeBusca, PosicaoPaginaAtual);
				
				enviarJSON(response, stTemp);
									
				return;
			}
			break;
			
        // Consulta de Orgao Expedidor - Usado no cadastro de partes
        case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Sigla", "Descrição"};
				String[] lisDescricao = {"Sigla","Nome","Estado"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_RgOrgaoExpedidor");
				request.setAttribute("tempBuscaDescricao","RgOrgaoExpedidor");
				request.setAttribute("tempBuscaPrograma","Órgão Expeditor RG");			
				request.setAttribute("tempRetorno","ProcessoExecucao?PassoEditar=4");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = processoExecucaoNe.consultarDescricaoOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2,PosicaoPaginaAtual);
					
				
					enviarJSON(response, stTemp);
					
				
            }
            break;
            
        // Consulta de Bairro - Usado no endereço da parte
        case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
            if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
                String cidade;
                String uf;
                if (request.getParameter("cidadeBusca") == null)
                    cidade = "";
                else
                    cidade = request.getParameter("cidadeBusca");
                if (request.getParameter("ufBusca") == null)
                    uf = "";
                else
                    uf = request.getParameter("ufBusca");
                if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descrição", "Cidade", "UF"};
					String[] lisDescricao = {"Bairro","Zona","Cidade", "UF"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Bairro");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");			
					request.setAttribute("tempRetorno","ProcessoExecucao?PassoEditar=4");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = processoExecucaoNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
            }
            break;
            
        // Consultar tipos de Arquivo
        case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
	        if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"ArquivoTipo"};
				String[] lisDescricao = {"Tipo de Arquivo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				
				String stPermissaoPaginaAtual = String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
				
				atribuirJSON(request, "Id_ArquivoTipo", "ArquivoTipo", "Tipo de Arquivo", "ProcessoExecucao?PassoEditar=-2", Configuracao.Editar, stPermissaoPaginaAtual, lisNomeBusca, lisDescricao);										
			
			}else{
				String stTemp = "";
				stTemp = processoExecucaoNe.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
            break;
            
        // Consultar Modelos do Usuário
        case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
            tempList = processoExecucaoNe.consultarModelo(UsuarioSessao.getUsuarioDt(), processoExecucaoDt.getId_ArquivoTipo(), tempNomeBusca, PosicaoPaginaAtual);
            if (tempList != null && tempList.size() > 0) {
                request.setAttribute("ListaModelo", tempList);
                request.setAttribute("PaginaAtual", paginaatual);
                request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
                request.setAttribute("QuantidadePaginas", processoExecucaoNe.getQuantidadePaginas());
                request.setAttribute("tempBuscaId_Modelo", "Id_Modelo");
                request.setAttribute("tempBuscaModelo", "Modelo");
                stAcao = "/WEB-INF/jsptjgo/ModeloArquivoTipoLocalizar.jsp";
            } else {
                request.setAttribute("MensagemErro", "Nenhum Modelo encontrado para " + request.getAttribute("ArquivoTipo"));
                stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoPeticionar.jsp";
            }
            request.setAttribute("tempRetorno", "ProcessoExecucao?PassoEditar=-2");
            break;

        // Consulta Assuntos
        case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
	        	if (processoExecucaoDt.getProcessoDt().getId_AreaDistribuicao().length() > 0) {
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Assunto", "Código CNJ"};
						String[] lisDescricao = {"Assunto","Pai","Disp. Legal", "Código CNJ"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Assunto");
						request.setAttribute("tempBuscaDescricao","Assunto");
						request.setAttribute("tempBuscaPrograma","Assunto");			
						request.setAttribute("tempRetorno","ProcessoExecucao");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp = "";
						stTemp = processoExecucaoNe.consultarDescricaoAssuntoJSON(stNomeBusca1, stNomeBusca2, processoExecucaoDt.getProcessoDt().getId_AreaDistribuicao(), PosicaoPaginaAtual);
													
							enviarJSON(response, stTemp);
							
						
						return;								
					}
				} else {
					request.setAttribute("MensagemErro", "O campo Área de Distribuição deve ser informado primeiramente.");
				}
        	}
			break;
        	
		// Consultar Juiz Responsável - Serventia Cargo
		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");			
					request.setAttribute("tempRetorno","ServentiaCargo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
				String stTemp="";
					stTemp = processoExecucaoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
					
					enviarJSON(response, stTemp);
					
				
				return;			
				}
			}
			break;	
			
        default:
            // passos utilizados no cadastro do processo de execução penal
            // Função Editar é dividida da seguinte forma:
            // Passo -5 : Redireciona para o cadastro dos dados da condenação
            // Passo -3 : Redireciona para "default"
            // Passo -2 : Redireciona para passo 5
            // Passo 0 : Redireciona para passo 5
            // Passo 1 : Redireciona para passo 6
            // Passo 2 : Efetua consulta da parte e exibe a lista de partes com os dados
            // Passo 3 : Valida dados da parte
            // Passo 4 : Redireciona para jsp de cadastro de parte
            // Passo 5 : Passo 2 e Passo 3 (Valida dados do processo)
            // Passo 6 : Resgata arquivos inseridos no passo 2 e redireciona para passo 3
            // Passo 7 : Redireciona para jsp de busca de partes
            // Passo 8 : Atualiza assuntos do processo (inserção e exclusão)
            // Passo 9 : Busca dados da parte selecionada (vincula processo e parte)
            // Passo 10: Imprime dados das ações penais
            // Passo 11: Atualiza prisões/liberdades provisórias do processo (inserção e exclusão)
            // Passo 12: Atualiza alcunha da parte
            // Passo 13: Atualiza sinal particular da parte
            // Passo 14: Limpa os dados da parte
            // Passo 15: Recuperar os dados da GR do SPG
            // Passo 16: Limpa o objeto da Parte e Redireciona para jsp de cadastro de parte
        	// Passo 17: Alteração do número do processo de cálculo
        	// Passo 18: 
            // Passo 19: 
        	// Passo 20: 
        	// Passo 21: exclui/inclui modalidades no processo (passo 2)
        	// Passo 22: consulta o número do processo físico
        	// Passo 23: busca dados do processo selecionado e inclui no processoExecucao (vincula processo e parte)
            switch (passoEditar) {

            case -3: // ao clicar em "Passo 1" no cadastro do processo.
                setPasso(processoExecucaoDt, "Passo 1", "", "", "");
                break;

            case -2: // ao clicar em "Passo 3" no cadastro do processo.
                request.setAttribute("tempRetorno", "ProcessoExecucao");
                setPasso(processoExecucaoDt, null, null, "Passo 3", "");
                stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoPeticionar.jsp";
                break;

            case 0: // ao clicar em "Passo 2" no cadastro do processo.
                request.setAttribute("tempRetorno", "ProcessoExecucao");
                setPasso(processoExecucaoDt, null, "Passo 2", "", "");
                // listas utilizadas no cadastro
            	setListaRegimeRequest(processoExecucaoNe, request);
	            stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp";
                break;

            case 1: // ao clicar em "Passo 4" no cadastro do processo.
	            stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoVisualizar.jsp";
	            request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
                break;

            case 2: // Busca a parte e exibe dados
                if (request.getParameter("NumeroProcesso").equals("") && request.getParameter("CpfCnpj").equals("") && request.getParameter("Sentenciado").equals("") && request.getParameter("Mae").equals("") && request.getParameter("Nascimento").equals("")) {
                    request.setAttribute("MensagemErro", "Informe o(s) parâmetro(s) para a consulta!");
                    stAcao = "/WEB-INF/jsptjgo/BuscaProcessoExecucaoParte.jsp";
                } else {
                    if (request.getParameter("CpfCnpj") != null) cpf = request.getParameter("CpfCnpj").replaceAll("[/.-]", "");
                    if (cpf != null && !cpf.equals("")) {
                        Mensagem = processoExecucaoNe.verificaCpfParte(cpf);
                        if (Mensagem.length() == 0) {
                            if (listaParte(request, PosicaoPaginaAtual, paginaatual, processoParteDt, processoExecucaoNe, mensagemRetorno))
                                stAcao = "/WEB-INF/jsptjgo/BuscaProcessoExecucaoParte.jsp";
                            else {
                                request.setAttribute("MensagemErro", "Verifique! Não foi encontrado sentenciado para a consulta.");
                                stAcao = "/WEB-INF/jsptjgo/BuscaProcessoExecucaoParte.jsp";
                            }
                        } else {
                            request.setAttribute("MensagemErro", "Verifique! O número do CPF informado é inválido.");
                            stAcao = "/WEB-INF/jsptjgo/BuscaProcessoExecucaoParte.jsp";
                        }
                    }
                    else {
                    	 if (listaParte(request, PosicaoPaginaAtual, paginaatual, processoParteDt, processoExecucaoNe, mensagemRetorno))
                             stAcao = "/WEB-INF/jsptjgo/BuscaProcessoExecucaoParte.jsp";
                         else {
                             request.setAttribute("MensagemErro", "Verifique! Não foi encontrado sentenciado para a consulta.");
                             stAcao = "/WEB-INF/jsptjgo/BuscaProcessoExecucaoParte.jsp";
                         }
                    }
                }
                request.setAttribute("Sentenciado", request.getParameter("Sentenciado"));
                break;

            case 3: // Valida dados da parte
            	if (!processoParteCadastrada(processoExecucaoDt)) {
            		Mensagem = processoExecucaoNe.VerificarParteProcessoExecucao(processoParteDt);
            		if (Mensagem.length() == 0) {
            			ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)); 
                    	processoParteDt.setProcessoParteTipoCodigo(processoParteTipoDt.getProcessoParteTipoCodigo());
                    	processoParteDt.setId_ProcessoParteTipo(processoParteTipoDt.getId());
                        // Adiciona lista promovido
                        processoExecucaoDt.getProcessoDt().addListaPolosPassivos(processoParteDt);
            		} else {
                        request.setAttribute("MensagemErro", Mensagem);
                        stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
                    }
            	} 
                break;

            case 4: // Redireciona para o cadastro da parte
                stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
                break;

            case -5: // Redireciona para o cadastro dos dados da condenação
                stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp";
                break;

            case 5: // Passo 2 e Passo 3 (Valida dados do processo)
            	//assunto fixo para os processos de cálculo
	        	 if (processoExecucaoDt.getProcessoDt().getId_AreaDistribuicao().length() > 0) {//processo de cálculo (Execpenweb)
	        		 if (processoExecucaoDt.getProcessoDt().getListaAssuntos() == null || processoExecucaoDt.getProcessoDt().getListaAssuntos().size() == 0){
	        			 tempList = processoExecucaoNe.consultarDescricaoAssunto("Execucao Penal", processoExecucaoDt.getProcessoDt().getId_AreaDistribuicao(), PosicaoPaginaAtual);
	 	             	if (tempList != null && tempList.size() > 0){
	 	             		ProcessoAssuntoDt processoAssuntoDt = new ProcessoAssuntoDt();
	 	                    processoAssuntoDt.setId_Assunto(((AssuntoDt)tempList.get(0)).getId());
	 	                    processoAssuntoDt.setAssunto(((AssuntoDt)tempList.get(0)).getAssunto());
	 	                    processoExecucaoDt.getProcessoDt().addListaAssuntos(processoAssuntoDt);
	 	             	}	 
	        		 }
	             }
	        	 
                // inserção dos dados da condenação
	        	request.getSession().setAttribute("displayNovaCondenacao", "block");
                Mensagem = processoExecucaoNe.VerificarProcessoExecucao(processoExecucaoDt);
                if (Mensagem.length() > 0) {
                    request.setAttribute("MensagemErro", Mensagem);
                    if (processoExecucaoDt.getPasso1().equals("Passo 1 OK"))
                        stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp";
                    else
                    	stAcao = getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());

                } else {
                	// listas utilizadas no cadastro
                	setListaRegimeRequest(processoExecucaoNe, request);
                    
                    if (processoExecucaoDt.getPasso1().equals("Passo 1")) {
                        setPasso(processoExecucaoDt, "Passo 1 OK", "Passo 2", null, null);
                        stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp";
                    }
                    // Valida dados do processo e passa para passo de inserção de arquivos
                    else if (processoExecucaoDt.getPasso1().equals("Passo 1 OK")) {
                        setPasso(processoExecucaoDt, "Passo 1 OK", "Passo 2 OK", "Passo 3", null);
                        if (processoExecucaoDt.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_CALCULO){//processo de cálculo não possui o passo 3 (inclusão de arquivos)
                        	stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoVisualizar.jsp";
                        	request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
                        } else stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoPeticionar.jsp";
                    }
                }
                break;

            case 6: // Resgata lista de arquivos inseridos e adiciona ao processo. Redireciona para confirmação cadastro processo
                if (listaArquivosInseridos != null && listaArquivosInseridos.size() > 0) {
                    processoExecucaoDt.setListaArquivos(listaArquivosInseridos);
                    setPasso(processoExecucaoDt, "Passo 1 OK", "Passo 2 OK", "Passo 3 OK", "Passo 4");

                    // Gera código do pedido, assim o submit so pode ser executado uma unica vez
                    request.setAttribute("__Pedido__", UsuarioSessao.getPedido());

                    stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoVisualizar.jsp";
                    processoExecucaoDt.setId_ArquivoTipo("");
                    processoExecucaoDt.setArquivoTipo("");
                    processoExecucaoDt.setId_Modelo("");
                    processoExecucaoDt.setModelo("");
                    processoExecucaoDt.setTextoEditor("");
                } else {
                    request.setAttribute("MensagemErro", "É necessário inserir um arquivo para prosseguir o cadastro.");
                    stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoPeticionar.jsp";
                }
                break;

            case 7: // Redireciona para busca de parte
            	if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)){
            		if (processoExecucaoDt.getProcessoDt().getListaPolosPassivos() != null && processoExecucaoDt.getProcessoDt().getListaPolosPassivos().size() == 1){
                    	stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
                    } else 
                    	stAcao = "/WEB-INF/jsptjgo/BuscaProcessoExecucaoParte.jsp";	
            	}
                break;

            case 8: // Atualiza assuntos do processo
                if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)) {
                    if (posicaoLista != null && posicaoLista.length() > 0) {
                        processoExecucaoDt.getProcessoDt().getListaAssuntos().remove(Funcoes.StringToInt(posicaoLista));
                    }
                }
                break;

            case 9: // busca dados da parte selecionada e inclui no processo (vincula processo e parte)
                if (!processoParteCadastrada(processoExecucaoDt)) {
                    request.setAttribute("tempBuscaId_ProcessoParte", "Id_ProcessoParte");
                    request.setAttribute("tempBuscaProcessoParte", "ProcessoParte");

                    // consulta os dados da parte
                    processoParteDt = processoExecucaoNe.consultarParte(request.getParameter("Id_ProcessoParte"));
                    
                    //não permite cadastrar guia em processo de outra serventia
                    ProcessoDt processoSelecionado = processoExecucaoNe.consultarIdSimples(processoParteDt.getId_Processo());
                    Mensagem = processoExecucaoNe.verificarVinculoProcesso(processoSelecionado, processoExecucaoDt, UsuarioSessao.getUsuarioDt().getId_Serventia());
                    if (Mensagem.length() > 0){
        				request.setAttribute("MensagemErro", Mensagem);
        				processoExecucaoDt.setPodeCadastrarProcessoFisico(false);
        			} else {
        				vincularProcessoEParte(processoExecucaoDt, null, processoParteDt, processoExecucaoNe);
           			}
            		getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
                    
                } else
                    request.setAttribute("MensagemErro", "Parte já cadastrada no Processo.");
                break;
                
            case 10: //imprime dados das ações penais
            	listaAcoesPenais = (List)request.getSession().getAttribute("listaAcoesPenais");
            	if (listaAcoesPenais!=null && listaAcoesPenais.size()>0){
	            	((ProcessoExecucaoDt)listaAcoesPenais.get(0)).setProcessoDt(((ProcessoDt) request.getSession().getAttribute("processoDt")));
					byTemp = processoExecucaoNe.relDadosAcaoPenal(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , listaAcoesPenais, UsuarioSessao.getUsuarioDt());
									    
				    enviarPDF(response,byTemp,"Relatorio");
				    																	
				    byTemp = null;
            	}
			    return;
                
            case 11: // Atualiza prisões/liberdades provisórias do processo (inclusão e exclusão)
                if (posicaoLista == null || posicaoLista.length() <= 0 || posicaoLista.equals("")) {
                    Mensagem = processoExecucaoNe.verificarDadosPrisoes(processoExecucaoDt);
                    if (Mensagem.length() > 0) {
                        request.setAttribute("MensagemErro", Mensagem);
                        stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp";
                    } else {
                        if (processoExecucaoDt.getDataPrisaoProvisoria().length() > 0)
                            processoExecucaoDt.addListaPrisoesProvisorias(processoExecucaoDt.getDataPrisaoProvisoria());
                        else
                            request.setAttribute("MensagemErro", "O campo Data da Prisão Provisória é obrigatório.");

                        if (processoExecucaoDt.getDataLiberdadeProvisoria().length() > 0)
                            processoExecucaoDt.addListaLiberdadesProvisorias(processoExecucaoDt.getDataLiberdadeProvisoria());
                        else
                            processoExecucaoDt.addListaLiberdadesProvisorias("");

                        processoExecucaoDt.setDataPrisaoProvisoria("null");
                        processoExecucaoDt.setDataLiberdadeProvisoria("null");
                    }
                }
                // removerPrisaoLiberdadeProvisoriaProcesso
                if (posicaoLista != null && posicaoLista.length() > 0) {
                    processoExecucaoDt.getListaPrisoesProvisorias().remove(Funcoes.StringToInt(posicaoLista));
                    processoExecucaoDt.getListaLiberdadesProvisorias().remove(Funcoes.StringToInt(posicaoLista));
                }
                stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp";
                break;

            case 12: // Atualiza alcunha da parte
                if (posicaoLista != null && posicaoLista.length() > 0) {
                	ProcessoParteAlcunhaDt alcunha = (ProcessoParteAlcunhaDt) processoParteDt.getListaAlcunhaParte().get(Funcoes.StringToInt(posicaoLista));
                	if (alcunha.getId().length() > 0){
                		alcunha.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
                		alcunha.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
            			processoExecucaoNe.excluirAlcunha(alcunha);
            		}
                    processoParteDt.getListaAlcunhaParte().remove(Funcoes.StringToInt(posicaoLista));
                } else {
                	if (request.getParameter("Alcunha").length() > 0){
                		ProcessoParteAlcunhaDt alcunha = new ProcessoParteAlcunhaDt();
                    	alcunha.setAlcunha(request.getParameter("Alcunha"));
                    	processoParteDt.addListaAlcunhaParte(alcunha);	
                    	processoParteDt.setAlcunha("null");
                	}
                }
                stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
                break;

            case 13: // Atualiza sinal particular da parte
                if (posicaoLista != null && posicaoLista.length() > 0) {
                	ProcessoParteSinalDt sinal = (ProcessoParteSinalDt) processoParteDt.getListaSinalParte().get(Funcoes.StringToInt(posicaoLista));
                	if (sinal.getId().length() > 0){
                		sinal.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
                		sinal.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
            			processoExecucaoNe.excluirSinal(sinal);
            		}
                    processoParteDt.getListaSinalParte().remove(Funcoes.StringToInt(posicaoLista));
                } else {
                	if (request.getParameter("Sinal").length() > 0){
                		ProcessoParteSinalDt sinal = new ProcessoParteSinalDt();
                        sinal.setSinal(request.getParameter("Sinal"));
                    	processoParteDt.addListaSinalParte(sinal);
                    	processoParteDt.setSinal("null");
                	}
                }
                stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
                break;
                
            case 14: //limpar os dados da parte;
            	stAcao = getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
                break;
                
            case 15: // Consultar Guia de Recolhimento do Processo de Ação Penal no SPG
                String mensagem = "";
                if (processoExecucaoDt.getNumeroGuiaRecolhimento().equals("")) {
                    request.setAttribute("MensagemErro", "O campo Número da Guia de Recolhimento é obrigatório.");
                    break;
                }
                else if (processoExecucaoDt.getAnoGuiaRecolhimento().equals("")){
                	request.setAttribute("MensagemErro", "O campo Ano da Guia de Recolhimento é obrigatório.");
                    break;
                }
                else if (processoExecucaoDt.getAnoGuiaRecolhimento().length() != 4){
                	request.setAttribute("MensagemErro", "O campo Ano da Guia de Recolhimento deve conter 4 dígitos.");
                    break;
                }
                //formato do número da guia: ano + numero = aaaaXXXXXXXX
                //o numero é de 8 digitos: quando o usuário digitar completar com zero
                String numeroGuiaFormatado = processoExecucaoDt.getAnoGuiaRecolhimento() + Funcoes.preencheZeros(Funcoes.StringToLong(processoExecucaoDt.getNumeroGuiaRecolhimento()),8);
                mensagem = processoExecucaoNe.consultarGuiaRecolhimento(numeroGuiaFormatado, processoExecucaoDt, processoParteDt);
                if (!mensagem.equals("")) {
                    request.setAttribute("MensagemErro", mensagem);
                    break;
                }
                // busca o sentenciado do SPG no projudi
                if ((!processoParteDt.getProcessoNumero().equals("")) || (!processoParteDt.getCpf().equals("")) || (!processoParteDt.getNome().equals("")) || (!processoParteDt.getNomeMae().equals("")) || (!processoParteDt.getDataNascimento().equals(""))) {
                    String[] nomeSentenciado = processoParteDt.getNome().split(" ");
                    String[] nomeMaeSentenciado = processoParteDt.getNomeMae().split(" ");
                    List listaPartes = new ArrayList();
                    if (!processoParteDt.getCpf().equals("") && !processoParteDt.getCpf().equals("0")) {
                        // busca pelo cpf
                        listaPartes = processoExecucaoNe.listarPartes(processoParteDt.getCpf(), "", "", "", "", PosicaoPaginaAtual);
                        if (listaPartes.size() == 0) {
                            // se nao encontrou busca pelo nome, nome mae e data nascimento - os nomes serão consultados somente pelo primeiro nome
                            listaPartes = processoExecucaoNe.listarPartes("", nomeSentenciado[0], nomeMaeSentenciado[0], processoParteDt.getDataNascimento(), "", PosicaoPaginaAtual);
                            if (listaPartes.size() == 0) {
                                // se nao encontrou busca pelo nome (será consultado com o primeiro e segundo nome)
                                listaPartes = processoExecucaoNe.listarPartes("", nomeSentenciado[0] + " " + nomeSentenciado[1], "", "", "", PosicaoPaginaAtual);
                                if (listaPartes.size() == 0) {
                                	stAcao = getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
                                }
                            }
                        }
                    } else {
                        // se nao tem cpf busca pelo nome, nome mae e data nascimento - os nomes serão consultados somente pelo primeiro nome
                    	if (!processoParteDt.getDataNascimento().matches("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d")){
                    		processoParteDt.setDataNascimento("");
                    	} 
                    	
                        listaPartes = processoExecucaoNe.listarPartes("", nomeSentenciado[0], nomeMaeSentenciado[0], processoParteDt.getDataNascimento(), "", PosicaoPaginaAtual);
                        if (listaPartes.size() == 0) {
                            // se nao encontrou busca pelo nome (será consultado com o primeiro e segundo nome)
                            listaPartes = processoExecucaoNe.listarPartes("", nomeSentenciado[0] + " " + nomeSentenciado[1], "", "", "", PosicaoPaginaAtual);
                            if (listaPartes.size() == 0 && processoExecucaoDt.getCadastroTipo() != ProcessoExecucaoDt.PROCESSO_CALCULO) {
                            	stAcao = getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
                            }
                        }
                    }
                    // se os dados da parte do SPG encontrou uma ou mais referencias no Processo Execucao lista os sentenciados encontrados
                    if (listaPartes.size() > 0) {
                        //processoParteDt.limpar(); //Retirado pra aproveitar os dados do spg que vieram na guia. 
                        request.setAttribute("ListaProcessoParte", listaPartes);
                        request.setAttribute("Enderecodt", processoParteDt.getEnderecoParte());
                        request.setAttribute("processoParteDt", "ParteTipo");
                        request.setAttribute("PaginaAtual", paginaatual);
                        request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
                        request.setAttribute("QuantidadePaginas", processoExecucaoNe.getQuantidadePaginas());
                        request.setAttribute("tempBuscaId_ProcessoParte", "Id_ProcessoParte");
                        request.setAttribute("tempBuscaProcessoParte", "ProcessoParte");
                        stAcao = "/WEB-INF/jsptjgo/ListaProcessoParteExecucao.jsp";
                    }
                }
                request.setAttribute("MensagemOk", "A Guia de Recolhimento informada foi encontrada no SPG:\n Número da GR: " + processoExecucaoDt.getNumeroGuiaRecolhimento() + "/" + processoExecucaoDt.getAnoGuiaRecolhimento() + "\n Número do Processo de Ação Penal: " + processoExecucaoDt.getNumeroAcaoPenal() + "\n\n Verifique! Alguns dados foram recuperados para agilizar o cadastro.");
                break;
                
            case 16: //Limpa o objeto Parte e Redireciona para o cadastro da parte
            	if (!processoExecucaoEmAndamento(request, stAcao, processoExecucaoDt)){
                    stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
                }
                break;

            case 17: //alteração do número do processo de cálculo
            	if (request.getParameter("ModificaDados") != null && request.getParameter("ModificaDados").equals("true")){
            		stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoModificaDados.jsp";
            	} else {
            		String numeroProcesso = request.getParameter("ProcessoFisicoNumero");
            		mensagemRetorno = processoExecucaoNe.validarProcessoFisicoNumero(numeroProcesso, true);
            		if (mensagemRetorno.length() > 0){
            			request.setAttribute("MensagemErro", mensagemRetorno);
            			stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoModificaDados.jsp";
            		} else {
            			processoExecucaoDt.setProcessoDt(((ProcessoDt) request.getSession().getAttribute("processoDt")));
            			processoExecucaoDt.setProcessoFisicoNumero(numeroProcesso);
            			processoExecucaoNe.atualizarNumeroProcessoFisico(processoExecucaoDt);
            			processoExecucaoDt.getProcessoDt().setId_UsuarioLog(UsuarioSessao.getUsuarioDt().getId_UsuarioLog());
            			processoExecucaoDt.getProcessoDt().setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
            			processoExecucaoNe.alterarNumeroProcesso(processoExecucaoDt.getProcessoDt());
            			redireciona(response, "BuscaProcesso?Id_Processo=" + processoExecucaoDt.getProcessoDt().getId() + "&MensagemOk=Número do processo alterado com sucesso!");
            		}
            	}
                break;
                 
            case 18:
                break;
                
            case 19:
                break;
                
            case 20:
            	break;
            	
            case 21: //exclui/inclui modalidades no processo (passo 2)
                if (posicaoLista != null && posicaoLista.length() > 0){//exclui modalidade
                	if (paginaAnterior == Configuracao.Curinga6) {
                		ProcessoEventoExecucaoDt modalidade = (ProcessoEventoExecucaoDt) processoExecucaoDt.getListaModalidade().get(Funcoes.StringToInt(posicaoLista));
                		if (modalidade.getId().length() > 0){
                			modalidade.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
                			modalidade.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
                			processoExecucaoNe.excluirModalidade(modalidade);
                		}
                    }
                	processoExecucaoDt.getListaModalidade().remove(Funcoes.StringToInt(posicaoLista));
                } else {//inclui modalidade
                	mensagem = processoExecucaoNe.adicionarModalidadeProcessoExecucao(processoExecucaoDt);
         	        if (mensagem.length() > 0) request.setAttribute("MensagemErro", mensagem);
                }
                stAcao = getStAcao(request, paginaAnterior);
                paginaatual = getPaginaAtual(request, paginaAnterior);
                break;

            case 22: //consulta o número do processo físico
            	String numeroProcesso = processoExecucaoDt.getProcessoFisicoNumero();
            	limparProcessoExecucao(processoExecucaoDt, UsuarioSessao, processoExecucaoNe);
            	
            	processoExecucaoDt.setProcessoFisicoNumero(numeroProcesso);
            	
            	//verifica se o número do processo físico foi informado corretamente e se já foi cadastrado no projudi
            	mensagemRetorno = processoExecucaoNe.verificarNumeroProcessoFisico(processoExecucaoDt.getCadastroTipo(), processoExecucaoDt.getProcessoFisicoNumero());
				
            	if (mensagemRetorno.length() == 0){
            		processoExecucaoDt.setPodeCadastrarProcessoFisico(true);
            		processoExecucaoNe.atualizarNumeroProcessoFisico(processoExecucaoDt);
					List listaProcesso = processoExecucaoNe.existeProcessoCadastrado(processoExecucaoDt.getProcessoDt().getProcessoNumeroCompleto());
					
					if (listaProcesso != null && listaProcesso.size() > 0){//existe o processo cadastrado
						request.getSession().setAttribute("ListaProcesso", listaProcesso);
						mensagemRetorno = "Este processo já está cadastrado! Confirme os dados do sentenciado e prossiga o cadastro!";
						
						stAcao = "/WEB-INF/jsptjgo/ListaProcessoExecucao.jsp";
					}
            	} else {
            		request.setAttribute("MensagemOk", mensagemRetorno);
            		processoExecucaoDt.setPodeCadastrarProcessoFisico(false);
            	}
            	break;
            	
            case 23: // busca dados do processo selecionado e inclui no processoExecucao (vincula processo e parte)
            	if (posicaoLista != null && posicaoLista.length() > 0) {
            		Mensagem = "";
            		int posicao = Funcoes.StringToInt(posicaoLista);
            		List listaProcesso = (List) request.getSession().getAttribute("ListaProcesso");
            		ProcessoDt processoDt = (ProcessoDt) listaProcesso.get(posicao);
            			
            		Mensagem = processoExecucaoNe.verificarVinculoProcesso(processoDt, processoExecucaoDt, UsuarioSessao.getUsuarioDt().getId_Serventia());
            		
        			if (Mensagem.length() > 0){
        				request.setAttribute("MensagemErro", Mensagem);
        				processoExecucaoDt.setPodeCadastrarProcessoFisico(false);
        			} else {
        				vincularProcessoEParte(processoExecucaoDt, processoDt, null, processoExecucaoNe);
        				processoExecucaoDt.setPodeCadastrarProcessoFisico(true);
           			}
            		getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
            	}
            	break;
            	
            case 24:  // inclui ação penal à partir da lista de ações penais: menu opções processo -> manter registro da ação penal
            	ProcessoDt processo = processoExecucaoDt.getProcessoDt();
            	limparProcessoExecucao(processoExecucaoDt, UsuarioSessao, processoExecucaoNe);
				vincularProcessoEParte(processoExecucaoDt, processo, null, processoExecucaoNe);
				processoExecucaoDt.setPodeCadastrarProcessoFisico(true);
				processoExecucaoDt.setProcessoFisicoNumero(Funcoes.completarZeros(processoExecucaoDt.getProcessoDt().getProcessoNumeroCompleto(),25));
				processoExecucaoDt.setCadastroTipo(ProcessoExecucaoDt.PROCESSO_CALCULO);
				stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoExecpenweb.jsp";
            	break;
            	
            default:
            	if (paginaAnterior == Configuracao.Curinga6){
            		 stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoDadosAcaoPenal.jsp";
            	} else {
            		setPasso(processoExecucaoDt, null, "", "", "");	
            	}
                break;
            }
        }

        //verifica se a pena é medida de segurança para controle da jsp
        if (processoExecucaoDt.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))
        		|| processoExecucaoDt.getId_PenaExecucaoTipo().length() == 0){//para iniciar a tela com a mesma configuração de medida de segurança
        	processoExecucaoDt.setMedidaSeguranca(true);
        	if (processoExecucaoDt.getListaCondenacoes() != null && processoExecucaoDt.getListaCondenacoes().size() > 0)
        		processoExecucaoDt.setMedidaSeguranca(false); //para permitir excluir as condenações e modalidades, caso tenha cadastrado errado
        } else processoExecucaoDt.setMedidaSeguranca(false);
        
        request.setAttribute("PassoEditar", passoEditar);
        request.getSession().setAttribute("PassoEditarSalvarAcao", passoEditarSalvarAcao);
        request.setAttribute("PaginaAnterior", paginaatual);
        request.getSession().setAttribute("ProcessoExecucaodt_PE", processoExecucaoDt);
        request.getSession().setAttribute("ProcessoExecucao", processoExecucaoDt);
        request.getSession().setAttribute("ProcessoExecucaone", processoExecucaoNe);

        if (paginaatual != Configuracao.Curinga6 && paginaAnterior != Configuracao.Curinga6) {
            request.getSession().setAttribute("ArquivosInseridos", processoExecucaoDt.getListaArquivos());
            request.getSession().setAttribute("ProcessoPartedt", processoParteDt);
            request.getSession().setAttribute("Enderecodt", processoParteDt.getEnderecoParte());
        }
        request.getSession().setAttribute("listaAcoesPenais", listaAcoesPenais);

        if((request.getParameter("tempFluxo2") != null) && !(request.getParameter("tempFluxo2").equalsIgnoreCase("null"))){
//        	if(stAcao.equalsIgnoreCase("") && (request.getParameter("tempFluxo2") != null) && !(request.getParameter("tempFluxo2").equalsIgnoreCase("null"))){
			stAcao = request.getParameter("tempFluxo2");
		}
        RequestDispatcher dis = request.getRequestDispatcher(stAcao);
        dis.include(request, response);
    }

    /**
     * Método que faz tratamentos necessários com parâmetros auxiliares no cadastro de processo
     * @throws Exception 
     */
    protected void setParametrosAuxiliares(ProcessoParteDt processoParteDt, int passoEditar, int paginaatual, HttpServletRequest request, int paginaAnterior, UsuarioNe UsuarioSessao, ProcessoExecucaoNe processoExecucaoNe, ProcessoExecucaoDt processoExecucaoDt) throws Exception{
        // Se passoEditar for 3 ou 4 refere-se a cadastro de parte, portanto resgata os dados desta somente nesse caso
//        if (passoEditar == 2 || passoEditar == 3 || passoEditar == 4 || passoEditar == 16) {
            setDadosProcessoParte(request, processoParteDt, UsuarioSessao);

            // Quando uma alcunha é selecionada já insere na lista de alcunhas
//            if (!processoParteDt.getId_Alcunha().equals("") && paginaAnterior == (AlcunhaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
//                boolean adiciona = true;
//                if (processoParteDt.getListaAlcunhaParte() != null) {
//                    for (int i = 0; i < processoParteDt.getListaAlcunhaParte().size(); i++) {
//                        if (processoParteDt.getId_Alcunha().equals(((ProcessoParteAlcunhaDt) processoParteDt.getListaAlcunhaParte().get(i)).getId_Alcunha())) {
//                            request.setAttribute("MensagemErro", "Alcunha já cadastrada.");
//                            processoParteDt.setId_Alcunha("null");
//                            processoParteDt.setAlcunha("null");
//                            adiciona = false;
//                        }
//                    }
//                }
//                if (adiciona) {
//                    if (processoParteDt.getId_Alcunha().length() > 0) {
//                        ProcessoParteAlcunhaDt ppaDt = new ProcessoParteAlcunhaDt();
//                        ppaDt.setId_Alcunha(processoParteDt.getId_Alcunha());
//                        ppaDt.setAlcunha(processoParteDt.getAlcunha());
//                        ppaDt.setId_ProcessoParte(processoParteDt.getId_ProcessoParte());
//                        processoParteDt.addListaAlcunhaParte(ppaDt);
//                        processoParteDt.setId_Alcunha("null");
//                        processoParteDt.setAlcunha("null");
//                    } else
//                        request.setAttribute("MensagemErro", "Selecione uma Alcunha para ser adicionada.");
//                }
//            }
            // Quando um sinal particular é selecionado já insere na lista de sinal particular
//            if (!processoParteDt.getId_Sinal().equals("") && paginaAnterior == (SinalDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
//                boolean adiciona = true;
//                if (processoParteDt.getListaSinalParte() != null) {
//                    for (int i = 0; i < processoParteDt.getListaSinalParte().size(); i++) {
//                        if (processoParteDt.getId_Sinal().equals(((ProcessoParteSinalDt) processoParteDt.getListaSinalParte().get(i)).getId_Sinal())) {
//                            request.setAttribute("MensagemErro", "Sinal particular já adicionado.");
//                            processoParteDt.setId_Sinal("null");
//                            processoParteDt.setSinal("null");
//                            adiciona = false;
//                        }
//                    }
//                }
//                if (adiciona) {
//                    if (processoParteDt.getId_Sinal().length() > 0) {
//                        ProcessoParteSinalDt ppaDt = new ProcessoParteSinalDt();
//                        ppaDt.setId_Sinal(processoParteDt.getId_Sinal());
//                        ppaDt.setSinal(processoParteDt.getSinal());
//                        ppaDt.setId_ProcessoParte(processoParteDt.getId_ProcessoParte());
//                        processoParteDt.addListaSinalParte(ppaDt);
//                        processoParteDt.setId_Sinal("null");
//                        processoParteDt.setSinal("null");
//                    } else
//                        request.setAttribute("MensagemErro", "Selecione um Sinal Particular para ser adicionado.");
//                }
//            }
//        }

        // Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
        if (!processoExecucaoDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
            ModeloDt modeloDt = processoExecucaoNe.consultarModeloId(processoExecucaoDt.getId_Modelo(), processoExecucaoDt.getProcessoDt(), UsuarioSessao.getUsuarioDt());
            processoExecucaoDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
            processoExecucaoDt.setArquivoTipo(modeloDt.getArquivoTipo());
            processoExecucaoDt.setTextoEditor(modeloDt.getTexto());
        }

        // Quando um assunto é selecionado já insere na lista de assuntos
        if (!processoExecucaoDt.getId_Assunto().equals("") && paginaAnterior == (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
            if (processoExecucaoDt.getId_Assunto().length() > 0) {
                ProcessoAssuntoDt processoAssuntoDt = new ProcessoAssuntoDt();
                processoAssuntoDt.setId_Assunto(processoExecucaoDt.getId_Assunto());
                processoAssuntoDt.setAssunto(processoExecucaoDt.getAssunto());
                processoExecucaoDt.getProcessoDt().addListaAssuntos(processoAssuntoDt);

                processoExecucaoDt.setId_Assunto("null");
                processoExecucaoDt.setAssunto("null");
            } else
                request.setAttribute("MensagemErro", "O campo Assunto é obrigatório.");
        }

        request.setAttribute("PaginaAnterior", paginaatual);
        request.setAttribute("Id_ArquivoTipo", processoExecucaoDt.getId_ArquivoTipo());
        request.setAttribute("ArquivoTipo", processoExecucaoDt.getArquivoTipo());
        request.setAttribute("Modelo", processoExecucaoDt.getModelo());
        request.setAttribute("TextoEditor", processoExecucaoDt.getTextoEditor());
        request.setAttribute("PaginaAtual", Configuracao.Editar);
    }

    /**
     * Resgata atributos da Parte e seu Endereço
     */
    protected void setDadosProcessoParte(HttpServletRequest request, ProcessoParteDt processoParteDt, UsuarioNe usuarioSessao) {

        if (request.getParameter("Id_ProcessoParte") == null || request.getParameter("Id_ProcessoParte").equalsIgnoreCase("null")){
        	processoParteDt.setId("");
        }  else processoParteDt.setId(request.getParameter("Id_ProcessoParte"));
        processoParteDt.setNome(request.getParameter("Nome"));
        processoParteDt.setCpf(request.getParameter("Cpf"));
        processoParteDt.setRg(request.getParameter("Rg"));
        processoParteDt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
        processoParteDt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
        processoParteDt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
        processoParteDt.setSexo(request.getParameter("Sexo"));
        processoParteDt.setNomeMae(request.getParameter("NomeMae"));
        processoParteDt.setNomePai(request.getParameter("NomePai"));
        processoParteDt.setDataNascimento(request.getParameter("DataNascimento"));
        processoParteDt.setId_Naturalidade(request.getParameter("Id_Naturalidade"));
        processoParteDt.setCidadeNaturalidade(request.getParameter("CidadeNaturalidade"));
        processoParteDt.setId_EstadoCivil(request.getParameter("Id_EstadoCivil"));
        processoParteDt.setEstadoCivil(request.getParameter("EstadoCivil"));
        processoParteDt.setId_Profissao(request.getParameter("Id_Profissao"));
        processoParteDt.setProfissao(request.getParameter("Profissao"));
        processoParteDt.setId_Escolaridade(request.getParameter("Id_Escolaridade"));
        processoParteDt.setEscolaridade(request.getParameter("Escolaridade"));
        processoParteDt.setId_Alcunha(request.getParameter("Id_Alcunha"));
        processoParteDt.setAlcunha(request.getParameter("Alcunha"));
        processoParteDt.setId_Sinal(request.getParameter("Id_Sinal"));
        processoParteDt.setSinal(request.getParameter("Sinal"));
        processoParteDt.setRaca(request.getParameter("Raca"));
        processoParteDt.setId_Raca(request.getParameter("Id_Raca"));
        processoParteDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        processoParteDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

        // Endereço da parte
        processoParteDt.getEnderecoParte().setLogradouro(request.getParameter("Logradouro"));
        processoParteDt.getEnderecoParte().setNumero(request.getParameter("Numero"));
        processoParteDt.getEnderecoParte().setComplemento(request.getParameter("Complemento"));
        processoParteDt.getEnderecoParte().setId_Bairro(request.getParameter("Id_Bairro"));
        processoParteDt.getEnderecoParte().setBairro(request.getParameter("Bairro"));
        processoParteDt.getEnderecoParte().setId_Cidade(request.getParameter("BairroId_Cidade"));
        processoParteDt.getEnderecoParte().setCidade(request.getParameter("BairroCidade"));
        processoParteDt.getEnderecoParte().setUf(request.getParameter("BairroUf"));
        processoParteDt.getEnderecoParte().setCep(request.getParameter("Cep"));
        processoParteDt.getEnderecoParte().setId_UsuarioLog(usuarioSessao.getId_Usuario());
        processoParteDt.getEnderecoParte().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

        request.getSession().setAttribute("Enderecodt", processoParteDt.getEnderecoParte());
        request.getSession().setAttribute("processoParteDt", processoParteDt);
    }

    /*
     * restaga dados do cadastro do Processo de Execução Penal
     */
    protected void setDadosProcessoExecucao(HttpServletRequest request, UsuarioNe usuarioSessao, ProcessoExecucaoDt processoExecucaoDt) throws Exception{
    	processoExecucaoDt.setId_PenaExecucaoTipo(request.getParameter("Id_PenaExecucaoTipo"));
    	processoExecucaoDt.setPenaExecucaoTipo(request.getParameter("PenaExecucaoTipo"));
    	if (processoExecucaoDt.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA)))
    		processoExecucaoDt.setId_RegimeExecucao(request.getParameter("Id_RegimeExecucao_MS"));
    	else processoExecucaoDt.setId_RegimeExecucao(request.getParameter("Id_RegimeExecucao_PPL"));
    	processoExecucaoDt.setRegimeExecucao(request.getParameter("RegimeExecucao"));
        processoExecucaoDt.setId_Modalidade(request.getParameter("Id_Modalidade"));
        processoExecucaoDt.setModalidade(request.getParameter("Modalidade"));
        processoExecucaoDt.setId_LocalCumprimentoPena(request.getParameter("Id_LocalCumprimentoPena"));
        processoExecucaoDt.setLocalCumprimentoPena(request.getParameter("LocalCumprimentoPena"));
        processoExecucaoDt.setDataAcordao(request.getParameter("DataAcordao"));
        processoExecucaoDt.setDataDistribuicao(request.getParameter("DataDistribuicao"));
        processoExecucaoDt.setDataPronuncia(request.getParameter("DataPronuncia"));
        processoExecucaoDt.setDataDenuncia(request.getParameter("DataDenuncia"));
        processoExecucaoDt.setDataSentenca(request.getParameter("DataSentenca"));
        processoExecucaoDt.setDataAdmonitoria(request.getParameter("DataAdmonitoria"));
        processoExecucaoDt.setDataTransitoJulgado(request.getParameter("DataTransito"));
        processoExecucaoDt.setDataTransitoJulgadoMP(request.getParameter("DataTJ_MP"));
        processoExecucaoDt.setAnoGuiaRecolhimento(request.getParameter("AnoGuiaRecolhimento"));
        processoExecucaoDt.setNumeroGuiaRecolhimento(request.getParameter("NumeroGuiaRecolhimento"));
        processoExecucaoDt.setNumeroAcaoPenal(request.getParameter("NumeroAcaoPenal"));
        processoExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        processoExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
        processoExecucaoDt.setDataInicioSursis(request.getParameter("DataInicioSursis"));
        processoExecucaoDt.setTempoSursisAno(request.getParameter("SursisAno"));
        processoExecucaoDt.setTempoSursisMes(request.getParameter("SursisMes"));
        processoExecucaoDt.setTempoSursisDia(request.getParameter("SursisDia"));
        //fiz isso para controle de tela (para não ficar no tempo do sursis: 00-00-00)
        String tempoTotalSursisDias = Funcoes.converterParaDias(processoExecucaoDt.getTempoSursisAno(), processoExecucaoDt.getTempoSursisMes(), processoExecucaoDt.getTempoSursisDia());
        if (Funcoes.StringToInt(tempoTotalSursisDias) > 0)
        	processoExecucaoDt.setTempoTotalSursisDias(tempoTotalSursisDias);
        else processoExecucaoDt.setTempoTotalSursisDias("");
        
        if (request.getParameter("radioGuiaRecolhimento") != null) {
            if (request.getParameter("radioGuiaRecolhimento").equalsIgnoreCase("P"))
                processoExecucaoDt.setGuiaRecolhimento("P");
            else if (request.getParameter("radioGuiaRecolhimento").equalsIgnoreCase("D"))
                processoExecucaoDt.setGuiaRecolhimento("D");
            else
                processoExecucaoDt.setGuiaRecolhimento("");
        }

        // Parâmetros para auxiliar no cadastro das condenações
        processoExecucaoDt.setId_CrimeExecucao(request.getParameter("Id_CrimeExecucao"));
        processoExecucaoDt.setCrimeExecucao(request.getParameter("CrimeExecucao"));
        processoExecucaoDt.setDataFato(request.getParameter("DataFato"));
        processoExecucaoDt.setIdCondenacaoExecucaoSituacao(request.getParameter("Id_CondenacaoExecucaoSituacao"));
        processoExecucaoDt.setTempoCondenacaoAno(request.getParameter("Ano"));
        processoExecucaoDt.setTempoCondenacaoMes(request.getParameter("Mes"));
        processoExecucaoDt.setTempoCondenacaoDia(request.getParameter("Dia"));
        processoExecucaoDt.setTempoPenaEmDias(Funcoes.converterParaDias(processoExecucaoDt.getTempoCondenacaoAno(), processoExecucaoDt.getTempoCondenacaoMes(), processoExecucaoDt.getTempoCondenacaoDia()));
        processoExecucaoDt.setObservacaoCondenacao(request.getParameter("ObservacaoCondenacao"));
        if (request.getParameter("radioReincidente") != null){
			if (request.getParameter("radioReincidente").equalsIgnoreCase("t"))
				processoExecucaoDt.setReincidente("true");
			else if (request.getParameter("radioReincidente").equalsIgnoreCase("f"))
				processoExecucaoDt.setReincidente("false");
			else processoExecucaoDt.setReincidente("");
		}
        
        if (request.getParameter("chkPrimeiroRegime[]") != null){
			if (request.getParameter("chkPrimeiroRegime[]").equalsIgnoreCase("S"))
				processoExecucaoDt.setIniciouCumprimentoPena(false);
		}
        
        // Parâmetros para auxiliar na alteração das condenações existentes
        if (processoExecucaoDt.getListaCondenacoes() != null) {
            for (int i=0; i<processoExecucaoDt.getListaCondenacoes().size(); i++) {
                CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) processoExecucaoDt.getListaCondenacoes().get(i);
                condenacaoExecucaoDt.setId_CrimeExecucao(request.getParameter("Id_CrimeExecucao_" + i));
                condenacaoExecucaoDt.setCrimeExecucao(request.getParameter("CrimeExecucao_" + i));
                condenacaoExecucaoDt.setId_CondenacaoExecucaoSituacao(request.getParameter("Id_CondenacaoExecucaoSituacao_" + i));
                condenacaoExecucaoDt.setTempoCumpridoExtintoDias(request.getParameter("TempoCumpridoExtintoDias_" + i));
                condenacaoExecucaoDt.setDataFato(request.getParameter("DataFato_" + i));
                condenacaoExecucaoDt.setReincidente(request.getParameter("radioReincidente_"+i));
                condenacaoExecucaoDt.setQtdeAno(request.getParameter("Ano_" + i));
                condenacaoExecucaoDt.setQtdeMes(request.getParameter("Mes_" + i));
                condenacaoExecucaoDt.setQtdeDias(request.getParameter("Dia_" + i));
                condenacaoExecucaoDt.setTempoPenaEmDias(Funcoes.converterParaDias(condenacaoExecucaoDt.getQtdeAno(), condenacaoExecucaoDt.getQtdeMes(), condenacaoExecucaoDt.getQtdeDias()));
                condenacaoExecucaoDt.setTempoPena(condenacaoExecucaoDt.getTempoPenaEmDias());
                condenacaoExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
                condenacaoExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
                condenacaoExecucaoDt.setObservacao(request.getParameter("ObservacaoCondenacao_"+i));
                processoExecucaoDt.getListaCondenacoes().set(i, condenacaoExecucaoDt);
            }
        }

        // Parâmetros para auxiliar na alteração das modalidades existentes
        if (processoExecucaoDt.getListaModalidade() != null) {
            for (int i=0; i<processoExecucaoDt.getListaModalidade().size(); i++) {
                ProcessoEventoExecucaoDt modalidadeDt = (ProcessoEventoExecucaoDt) processoExecucaoDt.getListaModalidade().get(i);
                if (modalidadeDt.getEventoRegimeDt().getId_RegimeExecucao().equals("11")){
                	modalidadeDt.setQuantidade(request.getParameter("QuantidadeModalidade_" + i));
                } else {
                	modalidadeDt.setTempoAno(request.getParameter("ModAno_" + i));
                    modalidadeDt.setTempoMes(request.getParameter("ModMes_" + i));
                    modalidadeDt.setTempoDia(request.getParameter("ModDia_" + i));	
                	modalidadeDt.setQuantidade(Funcoes.converterParaDias(modalidadeDt.getTempoAno(), modalidadeDt.getTempoMes(), modalidadeDt.getTempoDia()));                	
                }
                modalidadeDt.setObservacao(request.getParameter("ObsModalidade_" + i));
                modalidadeDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
                modalidadeDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
          
                processoExecucaoDt.getListaModalidade().set(i, modalidadeDt);
            }
        }
        
        // Parâmetros para auxiliar no cadastro das prisões/liberdades provisórias
        processoExecucaoDt.setDataLiberdadeProvisoria(request.getParameter("LiberdadeProvisoria"));
        processoExecucaoDt.setDataPrisaoProvisoria(request.getParameter("PrisaoProvisoria"));

        processoExecucaoDt.setDataPrimeiroRegime(request.getParameter("DataPrimeiroRegime"));
        processoExecucaoDt.setDataInicioCumprimentoPena(request.getParameter("DataInicioCumprimentoPena"));

        processoExecucaoDt.setId_Comarca(request.getParameter("Id_Comarca"));
        processoExecucaoDt.setComarca(request.getParameter("Comarca"));
        processoExecucaoDt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
        processoExecucaoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
        processoExecucaoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
        processoExecucaoDt.setId_Modelo(request.getParameter("Id_Modelo"));
        processoExecucaoDt.setModelo(request.getParameter("Modelo"));
        processoExecucaoDt.setId_Assunto(request.getParameter("Id_Assunto"));
        processoExecucaoDt.setAssunto(request.getParameter("Assunto"));
        processoExecucaoDt.setTextoEditor(request.getParameter("TextoEditor"));

        // dados da origem do processo
        processoExecucaoDt.setVaraOrigem(request.getParameter("VaraOrigem"));
        processoExecucaoDt.setId_CidadeOrigem(request.getParameter("Id_CidadeOrigem"));
        processoExecucaoDt.setCidadeOrigem(request.getParameter("CidadeOrigem"));
        processoExecucaoDt.setEstadoOrigem(request.getParameter("EstadoOrigem"));
        processoExecucaoDt.setUfOrigem(request.getParameter("EstadoOrigem"));

        if (request.getParameter("ProcessoFisicoNumero") != null)
        	processoExecucaoDt.setProcessoFisicoNumero(request.getParameter("ProcessoFisicoNumero"));
        
        // dados da distribuição do processo
    	if (processoExecucaoDt.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_FISICO){//processo físico
    		processoExecucaoDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
        	processoExecucaoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));	
    	}
		else{//processo de cadastro de guia ou cadastro de processo para cálculo
			processoExecucaoDt.getProcessoDt().setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
	        processoExecucaoDt.getProcessoDt().setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		}
        
        //foi setado ao limpar o processoExecução, porém pode ser alterado pelo usuário.
        processoExecucaoDt.getProcessoDt().setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
        processoExecucaoDt.getProcessoDt().setProcessoTipo(request.getParameter("ProcessoTipo"));
        
        processoExecucaoDt.getProcessoDt().setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
        processoExecucaoDt.getProcessoDt().setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
        processoExecucaoDt.getProcessoDt().setSegredoJustica("false");
        processoExecucaoDt.getProcessoDt().setForumCodigo(request.getParameter("ForumCodigo"));
        processoExecucaoDt.getProcessoDt().setId_UsuarioLog(usuarioSessao.getId_Usuario());
        processoExecucaoDt.getProcessoDt().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
    }
    
    /**
     * Busca uma parte de acordo com os parâmetros passados
     * @throws Exception 
     */
    protected boolean listaParte(HttpServletRequest request, String posicaoPaginaAtual, int paginaatual, ProcessoParteDt processoParteDt, ProcessoExecucaoNe processoExecucaoNe, String mensagemRetorno) throws Exception{
        String cpf = "", sentenciado = "", mae = "", dataNascimento = "", numeroProcesso = "";
        boolean boRetorno = false;

        if (request.getParameter("CpfCnpj") != null) cpf = request.getParameter("CpfCnpj").replaceAll("[/.-]", "");
        if (request.getParameter("Sentenciado") != null) sentenciado = request.getParameter("Sentenciado");
        if (request.getParameter("Mae") != null) mae = request.getParameter("Mae");
        if (request.getParameter("Nascimento") != null) dataNascimento = request.getParameter("Nascimento");
        if (request.getParameter("NumeroProcesso") != null) numeroProcesso = request.getParameter("NumeroProcesso");

        List lista = new ArrayList();
        processoParteDt = new ProcessoParteDt();

        if ((numeroProcesso.length() > 0) || (cpf.length() > 0) || (sentenciado.length() > 0) || (mae.length() > 0) || (dataNascimento.length() > 0)) {
            lista = processoExecucaoNe.listarPartes(cpf, sentenciado, mae, dataNascimento, numeroProcesso, posicaoPaginaAtual);

            if (lista.size() > 0) {
                request.setAttribute("ListaProcessoParte", lista);
                request.setAttribute("Enderecodt", processoParteDt.getEnderecoParte());
                request.setAttribute("PaginaAtual", paginaatual);
                request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
                request.setAttribute("QuantidadePaginas", processoExecucaoNe.getQuantidadePaginas());
                request.setAttribute("tempBuscaId_ProcessoParte", "Id_ProcessoParte");
                request.setAttribute("tempBuscaProcessoParte", "ProcessoParte");
                boRetorno = true;

            } else {
                processoParteDt.limpar();
                request.setAttribute("MensagemErro", mensagemRetorno);
            }
        }
        return boRetorno;
    }

    /**
     * Resgata lista de arquivos inseridos no passo 2 usando DWR Converte de Map para List
     */
    protected List getListaArquivos(HttpServletRequest request) {
        Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
        return Funcoes.converterMapParaList(mapArquivos);
    }

    /**
     * Método que verifica se a parte já foi inserida no processo
     */
    protected boolean processoParteCadastrada(ProcessoExecucaoDt processoExecucaoDt) {
        if (processoExecucaoDt.getProcessoDt().getListaPolosPassivos() != null && processoExecucaoDt.getProcessoDt().getListaPolosPassivos().size() >= 1)
        	return true; //o processo de execucao penal deve conter apenas uma parte

        return false;
    }

    /**
     * Método que seta os passos para cadastro do processo
     * 
     * @param passo1 - cadastro dos dados de origem do processo
     * @param passo2 - cadastro das condenações
     * @param passo3 - movimentação
     * @param passo4 - confirmação dos dados
     */
    protected void setPasso(ProcessoExecucaoDt processoExecucaoDt, String passo1, String passo2, String passo3, String passo4) {
        if (passo1 != null) processoExecucaoDt.setPasso1(passo1);
        if (passo2 != null) processoExecucaoDt.setPasso2(passo2);
        if (passo3 != null) processoExecucaoDt.setPasso3(passo3);
        if (passo4 != null) processoExecucaoDt.setPasso4(passo4);
    }

    protected boolean processoExecucaoEmAndamento(HttpServletRequest request, String stAcao, ProcessoExecucaoDt processoExecucaoDt) {
    	if (processoExecucaoDt.getId_ProcessoExecucaoPenal() != null && processoExecucaoDt.getId_ProcessoExecucaoPenal().length() > 0){
            request.setAttribute("MensagemErro", "Não é possível alterar este dado. Processo de Execução em andamento!");
            stAcao = getStAcaoProcessoExecucao(processoExecucaoDt.getCadastroTipo());
            return true;
        } else
            return false;
    }

    /**
     * Método para redirecionar para a jsp origem
     */
    protected void redirecionarPaginaPai(HttpServletRequest request, int paginaAnterior, String passoEditar) {
        if (paginaAnterior == Configuracao.Curinga6){
            request.setAttribute("tempRetorno", "ProcessoExecucao?PaginaAtual=6&PassoEditar=-1");
        } else {
            request.setAttribute("tempRetorno", "ProcessoExecucao?PassoEditar=" + passoEditar);
        }
    }
    
    protected String redirecionarPaginaPaiJSON(HttpServletRequest request, int paginaAnterior, String passoEditar) {
    	String tempRetorno = "";
        if (paginaAnterior == Configuracao.Curinga6){
            tempRetorno = "ProcessoExecucao?PaginaAnterior=6&PassoEditar=-1";
        } else {
            tempRetorno = "ProcessoExecucao?PassoEditar=" + passoEditar;
        }
        request.setAttribute("tempRetorno?PassoEditar=-1", tempRetorno);
        return tempRetorno;
    }

    protected int getPaginaAtual(HttpServletRequest request, int paginaAnterior){
    	if (paginaAnterior == Configuracao.Curinga6){
    		request.setAttribute("PaginaAtual", Configuracao.Curinga6);
    		request.setAttribute("PassoEditar", "-1");
    		return Configuracao.Curinga6;
    	} else {
    		request.setAttribute("PaginaAtual", Configuracao.Editar);
    		request.setAttribute("PassoEditar", "-5");
    		return Configuracao.Editar;
    	}
    }
    
    protected String getStAcao(HttpServletRequest request, int paginaAnterior) throws Exception{
    	if (paginaAnterior == Configuracao.Curinga6){
    		request.setAttribute("PaginaAtual", Configuracao.Curinga6);
    		request.setAttribute("PassoEditar", "-1");
    		return "/WEB-INF/jsptjgo/ProcessoExecucaoDadosAcaoPenal.jsp";    	
    	} else {
        	request.setAttribute("PaginaAtual", Configuracao.Editar);
    		request.setAttribute("PassoEditar", "-5");
        	return "/WEB-INF/jsptjgo/ProcessoExecucaoDadosCondenacao.jsp";
        }
    }
    
    protected String getStAcaoProcessoExecucao(int cadastroTipo){
    	String st = "";
    	switch(cadastroTipo){
    	case 1:
    		st = "/WEB-INF/jsptjgo/ProcessoExecucao.jsp";
    		break;
    	case 2: 
    		st = "/WEB-INF/jsptjgo/ProcessoExecucaoFisico.jsp";
    		break;
    	case 3: 
    		st = "/WEB-INF/jsptjgo/ProcessoExecucaoExecpenweb.jsp";
    		break;
    	}
    	return st;
    }
    
    protected void setListaRegimeRequest(ProcessoExecucaoNe negocio, HttpServletRequest request) throws Exception{
//    	List listaRegimePPL = null;
//    	List listaRegimeMS = null;
//    	if (request.getSession().getAttribute("ListaRegime_PPL") == null){
//    		listaRegimePPL = negocio.consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE));
//    		request.getSession().setAttribute("ListaRegime_PPL", listaRegimePPL);
//    	} else listaRegimePPL = (List)request.getSession().getAttribute("ListaRegime_PPL");
//    	
//    	if (request.getSession().getAttribute("ListaRegime_MS") == null){
//    		listaRegimeMS = negocio.consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA));
//    		request.getSession().setAttribute("ListaRegime_MS", listaRegimeMS);	
//    	} else listaRegimeMS = (List)request.getSession().getAttribute("ListaRegime_MS");
//        
//    	if (request.getSession().getAttribute("SA_ListaRegime_PPL") == null){
//    		List SA_listaRegimePPL = new ArrayList();
//            SA_listaRegimePPL.addAll(listaRegimePPL);
//            SA_listaRegimePPL.addAll(listaRegimeMS);
//            request.getSession().setAttribute("SA_ListaRegime_PPL", SA_listaRegimePPL);	
//    	}
//    	if (request.getSession().getAttribute("SA_ListaPenaExecucaoTipo") == null) request.getSession().setAttribute("SA_ListaPenaExecucaoTipo", negocio.consultarIdsPenaExecucaoTipo(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE + "," + PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA + "," + PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO));
//    	if (request.getSession().getAttribute("ListaPenaExecucaoTipo") == null) request.getSession().setAttribute("ListaPenaExecucaoTipo", negocio.consultarIdsPenaExecucaoTipo(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE + "," + PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA));
//    	if (request.getSession().getAttribute("ListaModalidade") == null) request.getSession().setAttribute("ListaModalidade", negocio.consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO)));
//        if (request.getSession().getAttribute("ListaLocal") == null) request.getSession().setAttribute("ListaLocal", negocio.consultarDescricaoLocalCumprimentoPena());
//        if (request.getSession().getAttribute("ListaFormaCumprimento") == null) request.getSession().setAttribute("ListaFormaCumprimento", negocio.consultarDescricaoFormaCumprimentoExecucao());
//        if (request.getSession().getAttribute("ListaStatus") == null) request.getSession().setAttribute("ListaStatus", negocio.consultarDescricaoEventoExecucaoStatus(true));
    	negocio.setListaRegimeRequest(request);
    }
    
    protected void limparProcessoExecucao(ProcessoExecucaoDt processoExecucaoDt, UsuarioNe UsuarioSessao, ProcessoExecucaoNe processoExecucaoNe) throws Exception{
    	// Guarda se processo é fisico, para depois de limpar setar de novo
    	int cadastroTipo = processoExecucaoDt.getCadastroTipo();
    	
    	//dados armazenados do processo físico - utilizado para consulta do processo tipo (classe CNJ)
    	//captura os dados antes de limpar o processo.
		String id_AreaDistribuicao = processoExecucaoDt.getProcessoDt().getId_AreaDistribuicao();
		String areaDistribuicao = processoExecucaoDt.getProcessoDt().getAreaDistribuicao(); 		
		String areaDistribuicaoCodigo = processoExecucaoDt.getAreaDistribuicaoCodigo(); 
		String forumCodigo = processoExecucaoDt.getProcessoDt().getForumCodigo();
		
		processoExecucaoDt.limpar();
		processoExecucaoDt.setCadastroTipo(cadastroTipo);
		
		//seta a comarca automaticamente (pelo usuarioSessao)
		if (cadastroTipo != ProcessoExecucaoDt.PROCESSO_FISICO){
			ComarcaDt comarcaDt = processoExecucaoNe.consultarIdComarca(UsuarioSessao.getUsuarioDt().getId_Comarca());
			processoExecucaoDt.setComarca(comarcaDt.getComarca());
			processoExecucaoDt.setId_Comarca(comarcaDt.getId());
			processoExecucaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
		}
		
		//seta a classe CNJ (processoTipo) automaticamente, pois, até o momento, está disponível apenas uma classe CNJ
		ProcessoTipoDt processoTipoDt = null;
		if (cadastroTipo == ProcessoExecucaoDt.PROCESSO_CALCULO) processoTipoDt = processoExecucaoNe.consultarProcessoTipo(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA));
		else processoTipoDt = processoExecucaoNe.consultarProcessoTipo(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA));
		processoExecucaoDt.getProcessoDt().setId_ProcessoTipo(processoTipoDt.getId());
		processoExecucaoDt.getProcessoDt().setProcessoTipo(processoTipoDt.getProcessoTipo());

		//seta Área de Distribuição, para o cadastro do processo físico
		//		if (fisico){
		if (cadastroTipo == ProcessoExecucaoDt.PROCESSO_FISICO){
//			processoExecucaoDt.setProcessoFisico(fisico);
	    	processoExecucaoDt.getProcessoDt().setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
	    	processoExecucaoDt.getProcessoDt().setServentia(UsuarioSessao.getUsuarioDt().getServentia());
	    	
			//Se as informações de Area de Distribuição e/ou Fórum ainda não estiverem sido carregadas, as mesmas são obtidas da base...
			if (id_AreaDistribuicao == null || id_AreaDistribuicao == ""
				|| areaDistribuicao == null || areaDistribuicao == "" 
				|| areaDistribuicaoCodigo == null || areaDistribuicaoCodigo == "" 
				|| forumCodigo == null || forumCodigo == ""){
			
				ServentiaDt serventiaDt = processoExecucaoNe.consultarServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
				if (serventiaDt != null) {
					AreaDistribuicaoDt areaDistribuicaoDt = processoExecucaoNe.consultarAreaDistribuicao(serventiaDt.getId_AreaDistribuicao());
					if (areaDistribuicaoDt !=  null) {
						processoExecucaoDt.getProcessoDt().setId_AreaDistribuicao(areaDistribuicaoDt.getId());
						processoExecucaoDt.getProcessoDt().setAreaDistribuicao(areaDistribuicaoDt.getAreaDistribuicao());
						processoExecucaoDt.setAreaDistribuicaoCodigo(areaDistribuicaoDt.getAreaDistribuicaoCodigo());
						processoExecucaoDt.getProcessoDt().setForumCodigo(areaDistribuicaoDt.getForumCodigo());							
					}
				}
			} else{
				//Caso já tenha sido carregadas as mesmas são recuperadas...
				processoExecucaoDt.getProcessoDt().setId_AreaDistribuicao(id_AreaDistribuicao);
				processoExecucaoDt.getProcessoDt().setAreaDistribuicao(areaDistribuicao);
				processoExecucaoDt.setAreaDistribuicaoCodigo(areaDistribuicaoCodigo);
				processoExecucaoDt.getProcessoDt().setForumCodigo(forumCodigo);	
			}
		}
    }
    
    protected String vincularProcessoEParte(ProcessoExecucaoDt processoExecucaoDt, ProcessoDt processoDt, ProcessoParteDt processoParteDt, ProcessoExecucaoNe processoExecucaoNe) throws Exception{
        // associa os dados do processo existente da parte selecionada ao processo atual
    	if (processoDt == null)  {
    		processoDt = processoExecucaoNe.consultarIdSimples(processoParteDt.getId_Processo());
    		processoDt.addListaPolosPassivos(processoParteDt);
    	}
    	processoExecucaoDt.setProcessoDt(processoDt);
        processoExecucaoDt.setProcessoNovo(false);

        if (processoExecucaoDt.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_FISICO){//migração do processo físico
        	ServentiaCargoDt serventiaCargoDt = processoExecucaoNe.consultarServentiCargoResponsavelProcesso(processoExecucaoDt.getProcessoDt().getId(), String.valueOf(GrupoDt.JUIZ_EXECUCAO_PENAL));
        	if (serventiaCargoDt == null){
        		processoExecucaoDt.setPodeCadastrarProcessoFisico(false);
        		return " \nEste processo não possui Juiz responsável! Não é possível continuar o cadastro!";
        	} else {
        		processoExecucaoDt.setId_ServentiaCargo(serventiaCargoDt.getId());
        		processoExecucaoDt.setServentiaCargo(serventiaCargoDt.getNomeUsuario());
        	}
        }
        ServentiaDt serventia = new ServentiaDt();
        serventia = processoExecucaoNe.consultarIdServentia(processoExecucaoDt.getProcessoDt().getId_Serventia());

      	processoExecucaoDt.setId_Comarca(serventia.getId_Comarca());
        processoExecucaoDt.setComarca(serventia.getComarca());
        
        List lista = new AreaDistribuicaoNe().consultarAreasDistribuicaoServentia(serventia.getId());
        
        if(lista == null || lista.size() != 1)
    		return " \nNão foi possível determinar a área de distribuição da serventia!";
        
        AreaDistribuicaoDt areaDistDt = (AreaDistribuicaoDt) lista.get(0);
        processoExecucaoDt.getProcessoDt().setId_AreaDistribuicao(areaDistDt.getId());
        processoExecucaoDt.getProcessoDt().setAreaDistribuicao(areaDistDt.getAreaDistribuicao());
        
        processoExecucaoDt.getProcessoDt().setListaAssuntos(processoExecucaoNe.getAssuntosProcesso(processoExecucaoDt.getProcessoDt().getId()));
         
        processoExecucaoDt.setId_ProcessoExecucaoPenal(processoExecucaoDt.getProcessoDt().getId());
        return "";
    }
    
    protected void setDadosSituacaoAtualExecucao(HttpServletRequest request, UsuarioNe usuarioSessao, SituacaoAtualExecucaoDt situacaoAtualExecucaoDt) throws Exception{
    	if (request.getParameter("SA_Id_EventoExecucaoStatus") != null){
    		situacaoAtualExecucaoDt.setDataFuga(request.getParameter("SA_DataFuga"));
    		situacaoAtualExecucaoDt.setIdEventoExecucaoStatus(request.getParameter("SA_Id_EventoExecucaoStatus"));
    		situacaoAtualExecucaoDt.setEventoExecucaoStatus(request.getParameter("SA_EventoExecucaoStatus"));
//    		situacaoAtualExecucaoDt.setIdLocalCumprimentoPena(request.getParameter("SA_Id_LocalCumprimentoPena"));
//        	situacaoAtualExecucaoDt.setLocalCumprimentoPena(request.getParameter("SA_LocalCumprimentoPena"));
//        	situacaoAtualExecucaoDt.setIdRegime(request.getParameter("SA_Id_RegimeExecucao"));
//        	situacaoAtualExecucaoDt.setRegime(request.getParameter("SA_RegimeExecucao"));
        	situacaoAtualExecucaoDt.setIdFormaCumprimento(request.getParameter("SA_Id_FormaCumprimento"));
        	situacaoAtualExecucaoDt.setFormaCumprimento(request.getParameter("SA_FormaCumprimento"));
//    		situacaoAtualExecucaoDt.setIdPenaExecucaoTipo(request.getParameter("SA_Id_PenaExecucaoTipo"));
//    		situacaoAtualExecucaoDt.setPenaExecucaoTipo(request.getParameter("SA_PenaExecucaoTipo"));
//        	situacaoAtualExecucaoDt.setIdModalidade(request.getParameter("SA_Id_Modalidade"));
//        	situacaoAtualExecucaoDt.setModalidade(request.getParameter("SA_Modalidade"));
        	situacaoAtualExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        	situacaoAtualExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
        	if (!situacaoAtualExecucaoDt.getIdEventoExecucaoStatus().equalsIgnoreCase(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))){
    			situacaoAtualExecucaoDt.setDataFuga("");	
    		}
    	}
	}
}
