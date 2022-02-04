package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PeticionamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Classe responsável em controlar o peticionamento em um processo.
 * Usuários externos quando querem inserir um documento no processo usam o peticionamento.
 * Suporta peticionamento múltiplo.
 * 
 * @author msapaula
 */
public class PeticionamentoCt extends Controle {

    private static final long serialVersionUID = 7195409629847710171L;

    public int Permissao() {
		return PeticionamentoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt = null;
		MovimentacaoNe movimentacaoNe;
		PeticionamentoDt peticionamentoDt;
		String Mensagem = "";
		int paginaAnterior = 0;
		String processos[] = null;
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		String stAcao = "/WEB-INF/jsptjgo/Peticionamento.jsp";

		request.setAttribute("tempPrograma", "Peticionamento");
		request.setAttribute("tempRetorno", "Peticionamento");
		request.setAttribute("TituloPagina", "Peticionamento");

		movimentacaoNe = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (movimentacaoNe == null) movimentacaoNe = new MovimentacaoNe();

		peticionamentoDt = (PeticionamentoDt) request.getSession().getAttribute("Peticionamentodt");
		if (peticionamentoDt == null) peticionamentoDt = new PeticionamentoDt();
					
		if (request.getParameter("nomeArquivo")!= null )
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");

		peticionamentoDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		peticionamentoDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		peticionamentoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		peticionamentoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		peticionamentoDt.setId_Modelo(request.getParameter("Id_Modelo"));
		peticionamentoDt.setModelo(request.getParameter("Modelo"));
		peticionamentoDt.setTextoEditor(request.getParameter("TextoEditor"));
		peticionamentoDt.setComplemento(request.getParameter("MovimentacaoComplemento"));
		//Setando prioridade
		if (request.getParameter("pedidoUrgencia") != null && request.getParameter("pedidoUrgencia").equals("true")) peticionamentoDt.setPedidoUrgencia(true);
		
		//Setando segredo justica
		if (request.getParameter("pedidoSegredoJustica") != null && request.getParameter("pedidoSegredoJustica").equals("true")) peticionamentoDt.setSegredoJustica(true);
		
		peticionamentoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		peticionamentoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));

		//Quando um modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!peticionamentoDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(peticionamentoDt.getId_Modelo(), peticionamentoDt.getPrimeiroProcessoLista(), UsuarioSessao.getUsuarioDt());
			peticionamentoDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			peticionamentoDt.setArquivoTipo(modeloDt.getArquivoTipo());
			peticionamentoDt.setTextoEditor(modeloDt.getTexto());
		}
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:
				peticionamentoDt = new PeticionamentoDt();

				// Captura os processos que serão peticionados, se for o caso de Peticionamento em Lote
				if (request.getParameter("Id_Processo_Peticionar") != null && !request.getParameter("Id_Processo_Peticionar").equals("")) { 
					processos = request.getParameterValues("Id_Processo_Peticionar");
				} else if (request.getParameter("processos") != null) {
					processos = request.getParameterValues("processos");
				} else {
					//Recupera o processo da sessão e adiciona ao vetor
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					if (processoDt != null && processoDt.getId().length() > 0) {
						//Processos com status ERRO DE MIGRAÇÃO não podem receber novas petições
						if(processoDt.isProcessoErroMigracao()){
							throw new MensagemException("Não é permitido peticionamento no processo "+ processoDt.getProcessoNumero() +", pois ele possui status ERRO DE MIGRAÇÃO. Estes processos não existem juridicamente.");
						}
						
						//Processos das serventias de execução da penal podem ser movimentados apenas para arquivamento. BO: 2019/13635 - 2019/14873
						//Se o processo for de uma classe de execução penal e for de assunto diferente de Acordo de não persecução penal
						if(processoDt.isExecucaoPenal() && !processoDt.isAcordoNaoPersecucaoPenal()){
							ServentiaDt serventiaDt = movimentacaoNe.consultarServentiaProcesso(processoDt.getId_Serventia());
							//e for de uma serventia do subtipo execução penal
							if(serventiaDt != null && serventiaDt.isSubTipoExecucaoPenal()) {
								throw new MensagemException("Processos de execução penal não podem ser movimentados. Decreto judiciário n.º 2029/2019.");
							}
						}
						
						processos = new String[] {processoDt.getId() };
					}
				}

				//bloqueia o peticionamento nos processos de cálculo de liquidação de pena (processo físico)
				if (processoDt != null && processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
					redireciona(response, "BuscaProcessoUsuarioExterno?PassoBusca=5&PaginaAtual=" + Configuracao.Editar + "&PassoEditar="+ Configuracao.Editar + "&Id_Processo=" + processoDt.getId() + "&MensagemOk=a&MensagemErro=" + "Não é possível executar essa ação. Motivo: Processo físico!");
					return;
				} 
				
				//bloqueia o peticionamento nas CARTA PRECATÓRIA (criada a partir de pendências e não os processos em si) arquivadas
				if (processoDt != null && (processoDt.getDataArquivamento() != null && !processoDt.getDataArquivamento().equalsIgnoreCase("")) 
						&& (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA))
						|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPC))
						|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPP))
						|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE))
						|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFRACIONAL)))){
					throw new MensagemException("Não é possível peticionar em Carta Precatória arquivada.");
				}
				
				// Restringe o peticionamento dos oficiais de justição apenas aos processos nos quais estiverem trabalhando (possuírem mandado em aberto)
				if( UsuarioSessao.isOficial() ) {
				//não pode ver processo sigiloso
					if (processoDt.isSigiloso()){
						throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
					}
					if( !movimentacaoNe.temPendenciaAberta(UsuarioSessao.getId_Serventia(), processoDt.getId(), UsuarioSessao.getId_ServentiaCargo()) ) {
						throw new MensagemException("Não é possível peticionar no processo ("+ processoDt.getProcessoNumero() +"). É permitido aos Oficiais peticionarem apenas em processos nos quais possuem mandado em aberto.");
					}
					
				}
				
				//validação se pode peticionar em processo apenso
				if(processoDt != null && processoDt.isApenso()){
					ProcessoNe processoNe = new ProcessoNe();
					if (!processoNe.podePeticionarProcessoApenso(processoDt.getId_Processo(), processoDt.getId_ProcessoPrincipal())){
						throw new MensagemException("Não é possível peticionar no processo ("+ processoDt.getProcessoNumero() +"). Faça o peticionamento no processo principal/origináio.");
					}
				}
				
				//peticionamento em lote
				if (processos != null && processos.length > 0) {
					//Processos com status ERRO DE MIGRAÇÃO não podem receber novas petições
					for (int i = 0; i < processos.length; i++) {
						ProcessoDt processoConsultadoDt = (ProcessoDt)new ProcessoNe().consultarId(processos[i]);
						if(processoConsultadoDt.isProcessoErroMigracao()){
							throw new MensagemException("Não é permitido peticionamento no processo "+ processoConsultadoDt.getProcessoNumero() +", pois ele possui status ERRO DE MIGRAÇÃO. Estes processos não existem juridicamente.");
						}
					}
					
					montaTelaPeticionamento(processos, processoDt, peticionamentoDt, movimentacaoNe, UsuarioSessao, request, response);
					
					//peticionamento em lote: bloqueia o peticionamento nos processos de cálculo de liquidação de pena (processo físico)
					for (ProcessoDt processo : (List<ProcessoDt>)peticionamentoDt.getListaProcessos()) {
						if (processo.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
							redireciona(response, "BuscaProcessoUsuarioExterno?PassoBusca=1&PaginaAtual=" + request.getParameter("TipoConsulta") + "&MensagemErro=" + "Não é possível executar essa ação. Motivo: O Processo nº " + processo.getProcessoNumero() + " é processo físico!");
							return;
						}
						//validação se pode peticionar em processo apenso
						if(processo != null && processo.isApenso()){
							ProcessoNe processoNe = new ProcessoNe();
							if (!processoNe.podePeticionarProcessoApenso(processo.getId_Processo(), processo.getId_ProcessoPrincipal())){
								throw new MensagemException("Não é possível peticionar no processo ("+ processo.getProcessoNumero() +"). Faça o peticionamento no processo principal/origináio.");
							}
						}						
					}
					
					//lista de arquivos enviados da pendenciact
					request.getSession().setAttribute("ListaArquivosDwr", request.getSession().getAttribute("arquivosTransferencia"));
					request.getSession().setAttribute("ListaArquivos", request.getSession().getAttribute("arquivosTransferenciaMap"));
				} else {
					//Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcessoUsuarioExterno?PassoBusca=1&PaginaAtual=" + request.getParameter("TipoConsulta") + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				// Seta os tipos de movimentação configuradas para o usuário e grupo do usuário logado, parametrizados por ele
				peticionamentoDt.setListaTiposMovimentacaoConfigurado(movimentacaoNe.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				break;

			//Confirma peticionamento
			case Configuracao.Salvar:
				//Recupera arquivos inseridos
				peticionamentoDt.setListaArquivos(getListaArquivos(request));
				stAcao = "/WEB-INF/jsptjgo/PeticionamentoConfirmacao.jsp";
				break;

			//Salvando peticionamento
			case Configuracao.SalvarResultado:
				Mensagem = movimentacaoNe.verificarPeticionamento(peticionamentoDt);

				if (Mensagem.length() == 0) {
					movimentacaoNe.salvarPeticionamento(peticionamentoDt, UsuarioSessao.getUsuarioDt());

					// Limpa lista DWR e zera contador Arquivos
					request.getSession().removeAttribute("ListaArquivosDwr");
					request.getSession().removeAttribute("ListaArquivos");
					request.getSession().removeAttribute("Id_ListaArquivosDwr");

					if (peticionamentoDt.isMultiplo()) {
						request.setAttribute("dt", peticionamentoDt);
						request.setAttribute("MensagemOk", "Peticionamento Múltiplo efetuado com sucesso");
						stAcao = "/WEB-INF/jsptjgo/PeticionamentoLoteConfirmacao.jsp";
						peticionamentoDt = new PeticionamentoDt();
					} else {
						redireciona(response, "BuscaProcessoUsuarioExterno?Id_Processo=" + peticionamentoDt.getIdPrimeiroProcessoLista() + "&PassoBusca=5&MensagemOk=Petição enviada com sucesso. \n É prudente verificar se os arquivos foram inseridos corretamente e fazer o download do recibo da petição.");
						peticionamentoDt.limpar();
						return;
					}

				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			//Retira processos de um peticionamento múltiplo
			case Configuracao.Excluir:
				if (peticionamentoDt.getListaProcessos() != null) {
					if (peticionamentoDt.getListaProcessos().size() > 1) {
						String posicao = request.getParameter("posicao");
						if (posicao != null && !posicao.equals("")) {
							ProcessoDt objTemp = (ProcessoDt) peticionamentoDt.getListaProcessos().get(Funcoes.StringToInt(posicao));
							peticionamentoDt.getListaProcessos().remove(Funcoes.StringToInt(posicao));
							request.setAttribute("MensagemOk", "Processo " + Funcoes.formataNumeroProcesso(objTemp.getProcessoNumero()) + " retirado do peticionamento múltiplo.");
						}
					} else if (peticionamentoDt.getListaProcessos().size() == 1) request.setAttribute("MensagemErro", "Não é possível retirar esse processo do peticionamento múltiplo.");
				}
				break;

			// Consultar Tipos de Movimentação
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MovimentacaoTipo"};
					String[] lisDescricao = {"MovimentacaoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_MovimentacaoTipo");
					request.setAttribute("tempBuscaDescricao", "MovimentacaoTipo");
					request.setAttribute("tempBuscaPrograma", "MovimentacaoTipo");
					request.setAttribute("tempRetorno", "Peticionamento");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = movimentacaoNe.consultarGrupoMovimentacaoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;

			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","Peticionamento");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = movimentacaoNe.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;
				
			// Consultar Modelos do Usuário
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo",};
					String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo");
					request.setAttribute("tempBuscaDescricao", "Modelo");
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "Peticionamento");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Modelo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = movimentacaoNe.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), peticionamentoDt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
			break;
			default:
		}

		//Seta variáveis auxiliares
		request.setAttribute("Id_ArquivoTipo", peticionamentoDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", peticionamentoDt.getArquivoTipo());
		request.setAttribute("Modelo", peticionamentoDt.getModelo());
		request.setAttribute("TextoEditor", peticionamentoDt.getTextoEditor());

		request.getSession().setAttribute("Peticionamentodt", peticionamentoDt);
		request.getSession().setAttribute("Movimentacaone", movimentacaoNe);
		
		//Caso o tipo de Movimentação escolhido não esteja configurado teremos que adicioná-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(peticionamentoDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	protected void montaTelaPeticionamento(String[] processos, ProcessoDt processoDt, PeticionamentoDt peticionamentoDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao, HttpServletRequest request, HttpServletResponse response) throws Exception{
		ProcessoDt objProcesso = null;

		//Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		if (processos.length == 1) {
			if (processoDt == null) processoDt = movimentacaone.consultarProcessoIdCompleto(processos[0]);
			peticionamentoDt.addListaProcessos(processoDt);
		} else {
			//Consulta dados de cada processo e adiciona à lista
			for (int i = 0; i < processos.length; i++) {
				objProcesso = movimentacaone.consultarProcessoId(processos[i]);
				peticionamentoDt.addListaProcessos(objProcesso);
			}
		}
		if (processos.length > 1) {
			peticionamentoDt.setMultiplo(true);
			request.setAttribute("TituloPagina", "Peticionamento Múltiplo");
		}
	}

	protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		List lista = Funcoes.converterMapParaList(mapArquivos);

		return lista;
	}

	/**
	 * Verifica se o tipo de movimentação selecionado é um tipo de movimentação já configurado para o usuário logado
	 * @param peticionamentoDt
	 */
	protected void verifiqueMovimentacaoTipo(PeticionamentoDt peticionamentoDt) {
		if ((peticionamentoDt.getId_MovimentacaoTipo() == null) || (peticionamentoDt.getId_MovimentacaoTipo().trim().equalsIgnoreCase("")) 
		    || (peticionamentoDt.getMovimentacaoTipo() == null) || (peticionamentoDt.getMovimentacaoTipo().trim().equalsIgnoreCase(""))) return;
		boolean encontrouNaLista = false;	
		List listaMovimentacaoTipo = peticionamentoDt.getListaTiposMovimentacaoConfigurado();
		for (int i=0;i<listaMovimentacaoTipo.size();i++){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);
			if (peticionamentoDt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo())) encontrouNaLista = true;
		}
		if (!encontrouNaLista){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = new UsuarioMovimentacaoTipoDt();
			usuarioMovimentacaoTipoDt.setId_MovimentacaoTipo(peticionamentoDt.getId_MovimentacaoTipo());
			usuarioMovimentacaoTipoDt.setMovimentacaoTipo(peticionamentoDt.getMovimentacaoTipo());
			peticionamentoDt.addListaTiposMovimentacaoConfigurado(usuarioMovimentacaoTipoDt);
		}
	}

}
