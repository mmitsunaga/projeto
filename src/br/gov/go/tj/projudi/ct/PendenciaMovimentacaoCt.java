package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla a geração de pendências em uma movimentação já existente.
 * Essa ação poderá ser feita pelo cartório ou pelo juiz a qualquer momento no processo, desde que não seja uma movimentação
 * gerada pelo Sistema Projudi.
 * 
 * @author msapaula
 * 
 */
public class PendenciaMovimentacaoCt extends MovimentacaoCtGen {

	private static final long serialVersionUID = -5370072411115726032L;
	
	private static final String PAGINA_PADRAO_LOCALIZAR = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	private static final String NOME_CONTROLE_WEB_XML 	= "PendenciaMovimentacao";

	public int Permissao() {
		return MovimentacaoProcessoDt.CodigoPermissaoGerarPendencias;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		MovimentacaoProcessoDt movimentacaoProcessoDt;
		ProcessoEncaminhamentoDt processoEncaminhamentoDt;
		MovimentacaoNe Movimentacaone;
		ProcessoDt processoDt = null;
		String idServentia = null;
		String serventia = null;
		String idAreaDistribuicao = null;
		String areaDistribuicao = null;

		String Mensagem = "";
		String stId = "";
		int paginaAnterior = 0;
		String stAcao = "/WEB-INF/jsptjgo/PendenciaMovimentacao.jsp";
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "Pendências de Movimentação");
		request.setAttribute("tempRetorno", NOME_CONTROLE_WEB_XML);
		request.setAttribute("TituloPagina", "Gerar Pendências em Movimentação");

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
		if (movimentacaoProcessoDt == null) movimentacaoProcessoDt = new MovimentacaoProcessoDt();

		//se não existe o processo na seção houve algum erro(possivel uso de abas e click na pagina inicial)
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		
		if (processoDt==null){
			redireciona(response, "Usuario?PaginaAtual=-10");
			return;
		}

		// Variáveis auxiliares
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		
		
		processoEncaminhamentoDt = (ProcessoEncaminhamentoDt) request.getSession().getAttribute("processoEncaminhamentoDt");
		if (processoEncaminhamentoDt == null) processoEncaminhamentoDt = new ProcessoEncaminhamentoDt();
		
		
		if(request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").equals(""))  
			processoEncaminhamentoDt.setIdServentia(request.getParameter("Id_Serventia"));
		
		if(request.getParameter("Serventia") != null && !request.getParameter("Serventia").equals("")) 
			processoEncaminhamentoDt.setServentia(request.getParameter("Serventia"));
		
		if(request.getParameter("Id_ServentiaCargo") != null && !request.getParameter("Id_ServentiaCargo").equals(""))  
			processoEncaminhamentoDt.setIdServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		
		if(request.getParameter("ServentiaCargo") != null && !request.getParameter("ServentiaCargo").equals("")) 
			processoEncaminhamentoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		
		if(request.getParameter("Id_AreaDistribuicao") != null && !request.getParameter("Id_AreaDistribuicao").equals("")){ 
			processoEncaminhamentoDt.setIdAreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			processoEncaminhamentoDt.setIdServentia("");
			processoEncaminhamentoDt.setServentia("");
		}
		
		if(request.getParameter("AreaDistribuicao") != null && !request.getParameter("AreaDistribuicao").equals("")) 
			processoEncaminhamentoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));

		movimentacaoProcessoDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		movimentacaoProcessoDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		movimentacaoProcessoDt.setId_Classificador(request.getParameter("Id_Classificador"));
		movimentacaoProcessoDt.setClassificador(request.getParameter("Classificador"));
		movimentacaoProcessoDt.setId_ClassificadorPendencia(request.getParameter("Id_ClassificadorPendencia"));
		movimentacaoProcessoDt.setClassificadorPendencia(request.getParameter("ClassificadorPendencia"));
		movimentacaoProcessoDt.setId_UsuarioRealizador(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
		movimentacaoProcessoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		movimentacaoProcessoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		setParametrosAuxiliares(movimentacaoProcessoDt, paginaAnterior, paginaatual, request, Movimentacaone, UsuarioSessao);

		// -----------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			// Inicializa geração de pendências em movimentação
			case Configuracao.Novo:
//				if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
//					Mensagem = "Não é possível realizar esta ação. Motivo: Processo físico!";
//					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
//				} else{
					movimentacaoProcessoDt = new MovimentacaoProcessoDt();
					// Captura a movimentação que será utilizada
					stId = request.getParameter("Id_Movimentacao");
					if (stId != null && !stId.isEmpty()) {
						
						//Processos das serventias de execução da penal podem ser movimentados apenas para arquivamento. BO: 2019/13635 - 2019/14873
						//Se o processo for de uma classe de execução penal e for de assunto diferente de Acordo de não persecução penal
						if(processoDt.isExecucaoPenal() && !processoDt.isAcordoNaoPersecucaoPenal()){
							ServentiaDt serventiaDt = Movimentacaone.consultarServentiaProcesso(processoDt.getId_Serventia());
							//e for de uma serventia do subtipo execução penal
							if(serventiaDt != null && serventiaDt.isSubTipoExecucaoPenal()) {
								request.setAttribute("MensagemOk", "Este processo é de execução da penal e só será permitido movimentar ele caso seja para arquivar. Qualquer outro tipo de movimentação será bloqueado ao final. Decreto judiciário n.º 2029/2019.");
							}
						}
						
						movimentacaoProcessoDt = Movimentacaone.consultarDadosMovimentacao(stId);
						movimentacaoProcessoDt.addListaProcessos(processoDt);
	
						// Seta tipos de pendências que poderão ser geradas
						movimentacaoProcessoDt.setListaPendenciaTipos(Movimentacaone.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
	
						// Limpa lista DWR e zera contador Pendências
						request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
						request.getSession().removeAttribute("ListaPendencias");
						
						//Trata acesso de outra serventia, proveniente de Carta precatória
						if (request.getSession().getAttribute("AcessoOutraServentia") != null && (Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.CARTA_PRECATORIA)) {
							String processoOutraServentia = null;
							if (request.getSession().getAttribute("ProcessoOutraServentia") != null) processoOutraServentia = (String) request.getSession().getAttribute("ProcessoOutraServentia");
							//Se processo que será movimentado é o mesmo com acesso externo liberado, deve liberar
							if (processoOutraServentia != null && processoOutraServentia.equalsIgnoreCase(processoDt.getId())) movimentacaoProcessoDt.setAcessoOutraServentia(request.getSession().getAttribute("AcessoOutraServentia").toString());
						}
	
						Mensagem = Movimentacaone.podeGerarPendenciasMovimentacao(movimentacaoProcessoDt, UsuarioSessao.getUsuarioDt());
						if (Mensagem.length() > 0) Mensagem = "Operação não permitida para processo(s). Motivo(s): " + Mensagem;
						
						request.getSession().removeAttribute("PendenciaTipoCodigo");
						processoEncaminhamentoDt.limpar();
						
					} else Mensagem = "Nenhuma Movimentação foi selecionada";
	
					if (Mensagem.length() > 0) {
						if (processoDt != null) redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						return;
					}
//				}
				break;

			case Configuracao.Salvar:
				// Captura lista de pendências
				movimentacaoProcessoDt.setListaPendenciasGerar(getListaPendencias(request));
				Mensagem = Movimentacaone.obtemAlertaMovimentarProcesso(movimentacaoProcessoDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
				if (Mensagem.length() != 0) {
					request.setAttribute("MensagemErro", Mensagem);	
				}
				request.setAttribute("Mensagem", "Clique para confirmar a Geração de Pendência(s)");
				
				//Valida pedido de arquivamento para apresentar mensagem de advertência
				String mensagemAdvertencia = Movimentacaone.validaPedidoArquivamento(movimentacaoProcessoDt, processoDt);
				if( mensagemAdvertencia != null && mensagemAdvertencia.length() > 0 ) {
					request.setAttribute("MensagemAdvertencia", mensagemAdvertencia);
				}
				
				break;

			// Salva geração de pendências em movimentação
			case Configuracao.SalvarResultado:
				Mensagem = Movimentacaone.verificarMovimentacaoPendencia(movimentacaoProcessoDt);
				if (Mensagem.length() == 0) {
					Mensagem = Movimentacaone.podeMovimentarProcesso(movimentacaoProcessoDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo());

					if (Mensagem.length() == 0) {
						Movimentacaone.salvarPendenciaMovimentacao(movimentacaoProcessoDt, UsuarioSessao.getUsuarioDt());

						redireciona(response, "BuscaProcesso?Id_Processo=" + movimentacaoProcessoDt.getIdPrimeiroProcessoLista() + "&MensagemOk=Geração de Pendências Realizada com Sucesso.");

						movimentacaoProcessoDt.limpar();
						limparListas(request);
						return;
					} else request.setAttribute("MensagemErro", Mensagem);
				} else request.setAttribute("MensagemErro", Mensagem);
				request.getSession().removeAttribute("PendenciaTipoCodigo");
				processoEncaminhamentoDt.limpar();
				break;

			// Consultar classificador
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				stAcao = PAGINA_PADRAO_LOCALIZAR;
				String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				if (request.getParameter("Passo")==null){
					if (request.getParameter("tempFluxo1").equalsIgnoreCase("processo")) {
						request.setAttribute("tempFluxo1", "processo");
						String[] lisNomeBusca = {"Classificador"};
						String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
						atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", NOME_CONTROLE_WEB_XML, Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
						break;
					} else {
						String[] lisNomeBusca = {"Classificador"};
						String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
						atribuirJSON(request, "Id_ClassificadorPendencia", "ClassificadorPendencia", "Classificador", NOME_CONTROLE_WEB_XML, Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
						break;
					}					
				} else{
					String stTemp="";
					stTemp = Movimentacaone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					enviarJSON(response, stTemp);
					return;								
				}				
			
			// Consultar tipos de processo / Classes
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoTipo"};
					String[] lisDescricao = {"ProcessoTipo","ProcessoTipoCodigo"};
					String[] camposHidden = {"ProcessoTipoCodigo","desc2"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					request.setAttribute("tempBuscaId", "Id_ProcessoTipo");
					request.setAttribute("tempBuscaDescricao", NOME_CONTROLE_WEB_XML);
					request.setAttribute("tempBuscaPrograma", NOME_CONTROLE_WEB_XML);
					request.setAttribute("tempRetorno", NOME_CONTROLE_WEB_XML);
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("Id_Serventia", processoDt.getId_Serventia());
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				}
				else {
					String stTemp = "";
					stTemp = new ProcessoNe().consultarProcessoTipoServentiaJSON(stNomeBusca1, processoDt.getId_Serventia(),  UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
			
			//Consultar Áreas de Distribuição
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					//quando for necessário retornar outros valos além do id, coloque outras colunas de descrição
					// na localizar.jsp as descrições geram novos input hidem para retornar ao ct
					// na funcoes.js as descricoes serão usadas para gerar os AlterarValue para retornar para o ct
					//String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
					//request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");			
					request.setAttribute("tempRetorno","PendenciaMovimentacao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = new ProcessoNe().consultarDescricaoAreasDistribuicaoAtivaJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;

			//Consultar Serventias
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if("consultaGabinete".equals(request.getParameter("fluxo"))){
					//Consulta utilizada na pendência "Ecaminhar Processo Gabiente"
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Serventia"};
						String[] lisDescricao = {"Serventia"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Serventia");
						request.setAttribute("tempBuscaDescricao","Serventia");
						request.setAttribute("tempBuscaPrograma","Serventia");			
						request.setAttribute("tempRetorno","PendenciaMovimentacao?fluxo=consultaGabinete");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp="";
						stTemp = new ProcessoNe().consultarListaServentiaRelacionadaJSON(stNomeBusca1, processoDt.getId_Serventia(), ServentiaTipoDt.GABINETE, PosicaoPaginaAtual, 1);
						enviarJSON(response, stTemp);
						return;								
					}
					
				}
				else {
					
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Serventia"};
						String[] lisDescricao = {"Serventia"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Serventia");
						request.setAttribute("tempBuscaDescricao","Serventia");
						request.setAttribute("tempBuscaPrograma","Serventia");			
						request.setAttribute("tempRetorno","PendenciaMovimentacao");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp="";
						stTemp = new ProcessoNe().consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, processoEncaminhamentoDt.getIdAreaDistribuicao(), PosicaoPaginaAtual);
						enviarJSON(response, stTemp);
						return;								
					}
				
				}
			break;
			
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):							

				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo [Serventia]", "Usuário", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "PendenciaMovimentacao");
					request.setAttribute("tempRetorno", "PendenciaMovimentacao");
					//Atributo novoRelatorDesabilitado será usado para o controle das transações no ct
					if(request.getSession().getAttribute("novoRelatorDesabilitado") == null) {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					} else {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					}
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
					request.setAttribute("tempFluxo1", "1");
				}else{
					String stTemp = "";
					stTemp = new ProcessoNe().consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());						
					enviarJSON(response, stTemp);
					return;
				}
				break;
			
			default:
				stId = request.getParameter("Id_Movimentacao");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(movimentacaoProcessoDt.getId())) {
						movimentacaoProcessoDt.limpar();
						movimentacaoProcessoDt = (MovimentacaoProcessoDt) Movimentacaone.consultarId(stId);
					}
				}
				
				stId = request.getParameter("Id_ProcessoTipo");
				if( stId != null && stId.length() > 0 ) {
					ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(stId);
					request.setAttribute("Id_ProcessoTipo", processoTipoDt.getId());
					request.setAttribute("ProcessoTipo", processoTipoDt.getProcessoTipo());
				}
				break;
		}

		request.getSession().setAttribute("Movimentacaodt", movimentacaoProcessoDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);
		
		request.getSession().setAttribute("processoEncaminhamentoDt", processoEncaminhamentoDt);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Tratamentos necessários ao realizar uma movimentação
	 */
	private void setParametrosAuxiliares(MovimentacaoProcessoDt movimentacaoDt, int paginaAnterior, int paginaatual, HttpServletRequest request, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioNe){
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

	}

	/**
	 * Resgata lista de pendências a serem inseridas Converte de Set para List
	 */
	private List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);
		return lista;
	}

	private void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Pendências
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}

}
