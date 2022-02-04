package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoParalisadoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ServentiaRelacionadaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParalisadoCt extends Controle {

    private static final long serialVersionUID = 2838483345791588863L;

    public int Permissao() {
        return ProcessoParalisadoDt.CodigoPermissao;
    }

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

        ProcessoParalisadoDt processoParalisadoDt;
        ProcessoNe processoNe;
        String stNomeBusca1 = "";
        
        if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

        List tempList = null;
        String stAcao = "WEB-INF/jsptjgo/ProcessoParalisadoPesquisa.jsp";
        String stFluxo ="1";

        request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
        request.setAttribute("tempBuscaServentia", "Serventia");
        request.setAttribute("tempRetorno", "ProcessoParalisado");
        
        processoNe = (ProcessoNe) request.getSession().getAttribute("processoNe");
        if (processoNe == null) processoNe = new ProcessoNe();

        if (request.getParameter("fluxoParalisado") != null && !request.getParameter("fluxoParalisado").equals("")) {                       
            stFluxo = request.getParameter("fluxoParalisado");
            request.getSession().setAttribute("fluxoParalisado", stFluxo);
        }else if (request.getSession().getAttribute("fluxoParalisado")!=null){
        	stFluxo = (String) request.getSession().getAttribute("fluxoParalisado");  
        }
        
        processoParalisadoDt = (ProcessoParalisadoDt) request.getSession().getAttribute("processoParalisadoDt");
        if (processoParalisadoDt == null){
        	processoParalisadoDt = new ProcessoParalisadoDt();
        }
               
        processoParalisadoDt.setIdServentia(request.getParameter("Id_Serventia"));        
        processoParalisadoDt.setServentia(request.getParameter("Serventia"));                       
        processoParalisadoDt.setPeriodo(request.getParameter("Periodo"));
        if (request.getParameter("Id_ServentiaCargo")!=null){
        	processoParalisadoDt.setIdServentiaCargo(request.getParameter("Id_ServentiaCargo"));
        	processoParalisadoDt.setServentiaCargo(request.getParameter("ServentiaCargo") + " - " + request.getParameter("ServentiaCargoUsuario"));
        }		       
        
        request.setAttribute("PaginaAnterior", paginaatual);
        request.setAttribute("Curinga", "vazio");
        request.setAttribute("MensagemOk", "");
        request.setAttribute("MensagemErro", "");
        request.setAttribute("tempPrograma", "Processos Paralisados");
        request.setAttribute("TituloPagina", "Processos Paralisados");

        switch (paginaatual) {
        case Configuracao.Imprimir:
            break;
        case Configuracao.LocalizarDWR:
            break;
        case Configuracao.Localizar:
            // stFluxo 1 = Pesquisa de Processo Paralisado para usuário comum
            // stFluxo 2 = Pesquisa de Processo Paralisado para usuário da corregedoria
            // stFluxo 3 = Pesquisa de Processo Paralisado de cargo específico
            // stFluxo 4 = Pesquisa de Processo Paralisado por conclusão
        	
        	//Se logado como desembargador ou distribuidor, pré-seleciona magistrado responsavel
        	if(UsuarioSessao.getUsuarioDt().isDesembargador()) {
				processoParalisadoDt.setIdServentiaCargo(UsuarioSessao.getId_ServentiaCargo());				
				processoParalisadoDt.setServentiaCargo(UsuarioSessao.getUsuarioDt().getNome()+" - "+UsuarioSessao.getUsuarioDt().getServentiaCargo());
			}
        	 
//        	else if(UsuarioSessao.getUsuarioDt().isDistribuidorGabinete()) {
//				processoParalisadoDt.setIdServentiaCargo(UsuarioSessao.getId_ServentiaCargo());
//				UsuarioDt usuarioChefe =  new UsuarioNe().consultarUsuarioCompleto(UsuarioSessao.getId_UsuarioServentiaChefe());				
//				processoParalisadoDt.setServentiaCargo(UsuarioSessao.getServentia());
//			}
            if (stFluxo.equalsIgnoreCase("1")) {
            	//se o período estiver vazio é a primeira execução da tela e
            	//a página deve ser aberta vazia.
            	if(processoParalisadoDt.getPeriodo() != null && !processoParalisadoDt.getPeriodo().equals("")) {
            		tempList = processoNe.consultarProcessosParalisadosServentia(UsuarioSessao.getUsuarioDt().getId_Serventia(), processoParalisadoDt.getPeriodo(), processoParalisadoDt.getIdServentiaCargo(), posicaoPaginaAtual);
            	}
                processoParalisadoDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
                processoParalisadoDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
                request.setAttribute("Id_Serventia", UsuarioSessao.getUsuarioDt().getId_Serventia()); 
                request.setAttribute("TituloPagina", "Processos Paralisados - Pendências da Serventia");
            } else if (stFluxo.equalsIgnoreCase("3")) {
            	//se o período estiver vazio é a primeira execução da tela e
            	//a página deve ser aberta vazia.
            	if(processoParalisadoDt.getPeriodo() != null && !processoParalisadoDt.getPeriodo().equals("")) {
            		tempList = processoNe.consultarProcessosParalisadosServentia(UsuarioSessao.getUsuarioDt().getId_Serventia(), processoParalisadoDt.getPeriodo(), UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), posicaoPaginaAtual);
            	}
                request.setAttribute("TituloPagina", "Processos Paralisados - Meu Cargo");
                request.setAttribute("Id_Serventia", UsuarioSessao.getUsuarioDt().getId_Serventia());
                request.setAttribute("Cargo", UsuarioSessao.getUsuarioDt().getId_ServentiaCargo());
                stAcao = "WEB-INF/jsptjgo/ProcessoParalisadoCargoPesquisa.jsp";
            }else if (stFluxo.equalsIgnoreCase("4")) {
            	//se o período estiver vazio é a primeira execução da tela e
            	//a página deve ser aberta vazia.
            	if(processoParalisadoDt.getPeriodo() != null && !processoParalisadoDt.getPeriodo().equals("")) {
            		if(UsuarioSessao.isDesembargador() || UsuarioSessao.isDistribuidorGabinete() || UsuarioSessao.isEstagiarioGabinete() || UsuarioSessao.isAssessorDesembargador() || UsuarioSessao.isAssistenteGabinete()
            			|| UsuarioSessao.isJuizUPJ() || UsuarioSessao.isAssistenteGabineteComFluxo()){
            			//desembargadores e distribuidores de gabinete visualizarão conclusões pendentes ao desembargador selecionado
            			if(processoParalisadoDt.getIdServentiaCargo() == null || processoParalisadoDt.getIdServentiaCargo().equalsIgnoreCase("")){
            				request.setAttribute("MensagemErro", "Para consultas em gabinete é preciso informar o magistrado responsável.");
            				break;
            			}
            			
            			if(Integer.parseInt(UsuarioSessao.getServentiaSubTipoCodigo()) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
                   			List lista = new ServentiaRelacionadaNe().consultarServentiasPrincipaisRelacionadas(UsuarioSessao.getId_Serventia());
                   			ServentiaDt serventiaPrincipalDt = new ServentiaDt();
                   			if(!lista.isEmpty()) {
                   				serventiaPrincipalDt = (ServentiaDt)lista.get(0);
                   			}
            				tempList = processoNe.consultarProcessosParalisadosConclusao(serventiaPrincipalDt.getId(), processoParalisadoDt.getIdServentiaCargo(), processoParalisadoDt.getPeriodo(),  posicaoPaginaAtual);
            			} else {
	            			tempList = processoNe.consultarProcessosParalisadosConclusaoSegundoGrau(null, processoParalisadoDt.getIdServentiaCargo(), processoParalisadoDt.getPeriodo(),  posicaoPaginaAtual);
            			}
            			
            		} else {
            			tempList = processoNe.consultarProcessosParalisadosConclusao(UsuarioSessao.getUsuarioDt().getId_Serventia(), processoParalisadoDt.getIdServentiaCargo(), processoParalisadoDt.getPeriodo(),  posicaoPaginaAtual);
            		}
            	}
            	request.setAttribute("TituloPagina", "Processos Paralisados - Conclusão");
            }
            request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
            if (tempList != null && tempList.size() > 0) {
                request.setAttribute("ListaProcessos", tempList);
                request.setAttribute("PaginaAtual", paginaatual);                
                request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
                request.setAttribute("Curinga", "ServentiaVara");
                if (stFluxo.equalsIgnoreCase("1") || stFluxo.equalsIgnoreCase("3") || stFluxo.equalsIgnoreCase("4")) {
                    montarOpcoesLote(request, UsuarioSessao);
                }
            } else {
            	//se o período estiver vazio é a primeira execução da tela e
            	//não precisa mostrar mensagem de erro.
            	if(processoParalisadoDt.getPeriodo() != null && !processoParalisadoDt.getPeriodo().equals("")) {
	                request.setAttribute("MensagemErro", "Dados Não Localizados");
	                request.setAttribute("PaginaAtual", Configuracao.Editar);
            	}
            }
            break;
        case Configuracao.Novo:
            processoParalisadoDt.limpar();
            request.setAttribute("PaginaAtual", paginaatual);
            break;
        case Configuracao.Curinga6:
            redireciona(response, "BuscaProcesso?Id_Processo=" + request.getParameter("Id_Processo").toString());
            break;
        case Configuracao.Curinga7:
            stAcao = "WEB-INF/jsptjgo/ProcessoParalisadoPesquisaCorregedoria.jsp";            
            request.setAttribute("PaginaAtual", Configuracao.Curinga7);
            if (processoParalisadoDt.getIdServentia().length()==0) {
            	request.setAttribute("MensagemErro", "Selecione a Serventia");
            	break;
            }
            if (stFluxo.equalsIgnoreCase("2")) {               
                tempList = processoNe.consultarProcessosParalisadosServentia(processoParalisadoDt.getIdServentia(), processoParalisadoDt.getPeriodo(), processoParalisadoDt.getIdServentiaCargo(), posicaoPaginaAtual);
            }else if (stFluxo.equalsIgnoreCase("4")) {
                tempList = processoNe.consultarProcessosParalisadosConclusao(processoParalisadoDt.getIdServentia(), processoParalisadoDt.getIdServentiaCargo(), processoParalisadoDt.getPeriodo(), posicaoPaginaAtual);
            }            
            if (tempList!=null)
                if (tempList.size() > 0) {
                    request.setAttribute("ListaProcessos", tempList);
                    request.setAttribute("PaginaAtual", paginaatual);
                    request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
                    request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());           
                    request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());                               
                } else {
                    request.setAttribute("MensagemErro", "Dados Não Localizados");
                    request.setAttribute("PaginaAtual", Configuracao.Editar);
                }
            break;
            
        case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia","Estado"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "ProcessoParalisado");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
                request.setAttribute("Curinga", "ServentiaVara");
				stTemp = processoNe.consultarServentiaJSON(stNomeBusca1, posicaoPaginaAtual, (UsuarioDt) request.getSession().getAttribute("UsuarioSessaoDt"));				
				enviarJSON(response, stTemp);									
				return;
			}
            break;
        
         // Consultar Juiz Responsável - Serventia Cargo
		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			String Id_Serventia = UsuarioSessao.getUsuarioDt().getId_Serventia();
			String ServentiaTipoCodigo = UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo();
			String ServentiaSubtipoCodigo  = UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo();
			
			if ((request.getParameter("tempFluxo2") != null && request.getParameter("tempFluxo2").equalsIgnoreCase("S"))){
				if (processoParalisadoDt.getIdServentia().length()==0) {
					request.setAttribute("MensagemErro", "Selecione a Serventia");
					break;
			    }
				ServentiaDt serventiaSeleciona = processoNe.consultarIdServentia(processoParalisadoDt.getIdServentia());
				if (serventiaSeleciona != null){
					 Id_Serventia = processoParalisadoDt.getIdServentia();
					 ServentiaTipoCodigo = serventiaSeleciona.getServentiaTipoCodigo();
					 ServentiaSubtipoCodigo  = serventiaSeleciona.getServentiaSubtipoCodigo();
				}
				request.setAttribute("CuringaServentiaCargo", "S");
			 }
			
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"ServentiaCargo"};
				String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
				String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
				request.setAttribute("camposHidden",camposHidden);
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
				request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
				request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
				request.setAttribute("tempRetorno", "ProcessoParalisado");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
				request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				
				if (request.getParameter("CuringaServentiaCargo") != null && request.getParameter("CuringaServentiaCargo").equalsIgnoreCase("S")){	
					request.setAttribute("tempFluxo2", "S");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga7);
				}
			}else{
				String stTemp = "";
				if(UsuarioSessao.isDesembargador() || UsuarioSessao.isDistribuidorGabinete() || UsuarioSessao.isEstagiarioGabinete() || UsuarioSessao.isAssessorDesembargador() || UsuarioSessao.isAssistenteGabinete()
						|| UsuarioSessao.isJuizUPJ() || UsuarioSessao.isAssistenteGabineteComFluxo()){
					//desembargadores e distribuidores de gabinete poderão visualizar apenas usuários do tipo desembargador do mesmo gabinete em que trabalha
					stTemp = processoNe.consultarMagistradoGabineteJSON(stNomeBusca1, posicaoPaginaAtual, Id_Serventia, ServentiaSubtipoCodigo);
				} else {
					stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, posicaoPaginaAtual, Id_Serventia, ServentiaTipoCodigo, ServentiaSubtipoCodigo);
				}					
				enviarJSON(response, stTemp);
 				return;
			}
			break;	
			
        default:
            if (stFluxo.equalsIgnoreCase("2") || stFluxo.equalsIgnoreCase("4")){
            	stAcao = "WEB-INF/jsptjgo/ProcessoParalisadoPesquisaCorregedoria.jsp";
            }
            
            request.setAttribute("PaginaAtual", Configuracao.Editar);
            break;
        }
        
        request.setAttribute("fluxoParalisado",stFluxo);        
        request.getSession().setAttribute("processoParalisadoDt", processoParalisadoDt);
        request.getSession().setAttribute("processoNe", processoNe);

        RequestDispatcher dis = request.getRequestDispatcher(stAcao);
        dis.include(request, response);
    }

    /**
     * Verifica quais opções de movimentação em lote o usuário poderá visualizar
     * 
     * @param request
     * @param usuarioSessao
     */
    protected void montarOpcoesLote(HttpServletRequest request, UsuarioNe usuarioSessao) {
        request.setAttribute("podeMovimentar", usuarioSessao.getVerificaPermissao(MovimentacaoDt.CodigoPermissao));        
    }
}