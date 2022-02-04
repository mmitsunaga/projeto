package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaCt extends ServentiaCtGen {

	/*Fluxos
	 * 1 - serventia
	 * 2 - area de distribuição
	 * 3 - serventia relacionada
	 * o fluxo padrão é o 1
	 */
	
    private static final long serialVersionUID = 3821344534759482350L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaDt Serventiadt;
		ServentiaAreaDistribuicaoDt servAreaDistDt;
		ServentiaNe Serventiane;
		List tempList = null;
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		//o fluxo padrão será o serventia
		String stFluxo = request.getParameter("tempFluxo1");
		if (stFluxo==null) {
			stFluxo = ServentiaDt.FLUXO_SERVENTIA;
		}
		List areasDistribuicoes = null;
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/Serventia.jsp";
		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "Serventia");
		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempRetorno", "Serventia");
		request.setAttribute("tempBuscaId_ServentiaTipo", "Id_ServentiaTipo");
		request.setAttribute("tempBuscaServentiaTipo", "ServentiaTipo");
		request.setAttribute("tempBuscaId_ServentiaSubtipo", "Id_ServentiaSubtipo");
		request.setAttribute("tempBuscaServentiaSubtipo", "ServentiaSubtipo");
		request.setAttribute("tempBuscaId_Area", "Id_Area");
		request.setAttribute("tempBuscaArea", "Area");
		request.setAttribute("tempBuscaId_Comarca", "Id_Comarca");
		request.setAttribute("tempBuscaComarca", "Comarca");
		request.setAttribute("tempBuscaId_AudienciaTipo", "Id_AudienciaTipo");
		request.setAttribute("tempBuscaAudienciaTipo", "AudienciaTipo");
		request.setAttribute("tempBuscaId_Endereco", "Id_Endereco");
		request.setAttribute("tempBuscaEndereco", "Endereco");
		request.setAttribute("tempBuscaId_Estado", "Id_Estado");
		request.setAttribute("tempBuscaEstado", "Estado");
		request.setAttribute("tempBuscaId_Bairro", "Id_Bairro");
		request.setAttribute("tempBuscaBairro", "Bairro");
		request.setAttribute("tempBuscaId_ServentiaRelacionada", "Id_ServentiaRelacionada");
		request.setAttribute("tempBuscaServentiaRelacionada", "ServentiaRelacionada");
		request.setAttribute("tempFluxo1",  ServentiaDt.FLUXO_SERVENTIA);
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		Serventiane = (ServentiaNe) request.getSession().getAttribute("Serventiane");
		if (Serventiane == null) Serventiane = new ServentiaNe();

		Serventiadt = (ServentiaDt) request.getSession().getAttribute("Serventiadt");
		if (Serventiadt == null) Serventiadt = new ServentiaDt();
		
		servAreaDistDt = (ServentiaAreaDistribuicaoDt) request.getSession().getAttribute("ServAreaDistDt");
		if (servAreaDistDt == null) servAreaDistDt = new ServentiaAreaDistribuicaoDt();

		Serventiadt.setServentia(request.getParameter("Serventia"));
		Serventiadt.setServentiaCodigo(request.getParameter("ServentiaCodigo"));
		Serventiadt.setServentiaCodigoExterno(request.getParameter("ServentiaCodigoExterno"));
		Serventiadt.setId_CNJServentia(request.getParameter("Id_CNJServentia"));
		Serventiadt.setId_ServentiaTipo(request.getParameter("Id_ServentiaTipo"));
		Serventiadt.setServentiaTipo(request.getParameter("ServentiaTipo"));
		Serventiadt.setId_ServentiaSubtipo(request.getParameter("Id_ServentiaSubtipo"));
		Serventiadt.setServentiaSubtipo(request.getParameter("ServentiaSubtipo"));
		Serventiadt.setId_Area(request.getParameter("Id_Area"));
		Serventiadt.setArea(request.getParameter("Area"));
		Serventiadt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicaoPrimaria"));
		Serventiadt.setAreaDistribuicao(request.getParameter("AreaDistribuicaoPrimaria"));		
		Serventiadt.setId_AreaDistribuicaoSecundaria(request.getParameter("Id_AreaDistribuicaoSecundaria"));
		Serventiadt.setAreaDistribuicaoSecundaria(request.getParameter("AreaDistribuicaoSecundaria"));		
		Serventiadt.setId_Comarca(request.getParameter("Id_Comarca"));
		Serventiadt.setComarca(request.getParameter("Comarca"));
		Serventiadt.setId_AudienciaTipo(request.getParameter("Id_AudienciaTipo"));
		Serventiadt.setTelefone(request.getParameter("Telefone"));
		Serventiadt.setAudienciaTipo(request.getParameter("AudienciaTipo"));
		Serventiadt.setId_EstadoRepresentacao(request.getParameter("Id_Estado"));
		Serventiadt.setEstadoRepresentacao(request.getParameter("Estado"));
		Serventiadt.setServentiaTipoCodigo(request.getParameter("ServentiaTipoCodigo"));
		Serventiadt.setServentiaSubtipoCodigo(request.getParameter("ServentiaSubtipoCodigo"));
		Serventiadt.setAreaCodigo(request.getParameter("AreaCodigo"));
		Serventiadt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		Serventiadt.setDataImplantacao(request.getParameter("DataImplantacao"));
		Serventiadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Serventiadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		Serventiadt.setConclusoDireto(request.getParameter("ConclusoDireto"));		
		Serventiadt.setEmail(request.getParameter("Email"));		
		/*
		 * A checkbox 'Recebe Processo', na verdade, está relacionada ao campo 'QUANTIDADE_DIST' da tabela 'SERV'.
		 * Por isso o método setQuantidadeDistribuicao está recebendo o valor do campo 'RecebeProcessoQtdDist' do formulário Serventia.jsp.
		*/
		Serventiadt.setQuantidadeDistribuicao(request.getParameter("RecebeProcessoQtdDist"));
		//*************************************************************************************************
		if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA) && paginaatual!=Configuracao.ExcluirResultado && paginaatual!=Configuracao.SalvarResultado) {
    		setDadosServentiaRelacionadaEdicao(request.getParameter("Id_ServentiaRelacionada"),
    											request.getParameter("Id_ServentiaRelacao"), 
    											request.getParameter("ServentiaRelacao"),
    											request.getParameter("RecebeProcesso"), 
    											request.getParameter("Substituicao"), 
    											request.getParameter("Id_ServentiaSubstituicao"), 
    											request.getParameter("dataInicioSubstituicao"),
    											request.getParameter("dataFimSubstituicao"),
    											request.getParameter("Probabilidade"), 
    											request.getParameter("UfRepresentatividade"), 
    											Serventiadt);
		}
		
		servAreaDistDt.setId_AreaDist(request.getParameter("Id_AreaDistribuicaoServentia"));
		servAreaDistDt.setAreaDist(request.getParameter("AreaDistribuicaoServentia"));		
		servAreaDistDt.setProbabilidade(request.getParameter("QuantidadeDistribuicao"));
		//**************************************************************************************************

		// ENDEREÇO DA SERVENTIA
		Serventiadt.setId_Endereco(request.getParameter("Id_Endereco"));		
		Serventiadt.setCep(request.getParameter("Cep"));
		Serventiadt.setLogradouro(request.getParameter("Logradouro"));
		Serventiadt.setNumero(request.getParameter("Numero"));
		Serventiadt.setComplemento(request.getParameter("Complemento"));
		Serventiadt.setId_Bairro(request.getParameter("Id_Bairro"));
		Serventiadt.setBairro(request.getParameter("Bairro"));
		Serventiadt.setId_Cidade(request.getParameter("BairroId_Cidade"));
		Serventiadt.setUf(request.getParameter("BairroUf"));
		Serventiadt.setCep(request.getParameter("Cep"));
		Serventiadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Serventiadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		

		request.setAttribute("Id_Comarca", Serventiadt.getId_Comarca());
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		// --------------------------------------------------------------------------------//
		switch (paginaatual) {

		case Configuracao.Excluir:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA)) {
				request.setAttribute("tempFluxo1", ServentiaDt.FLUXO_SERVENTIA_RELACIONADA);
				//Serventiadt.limparServentiaRelacionadaEdicao();
				//Serventiadt.setDadosServentiaRelacionadaEdicao(request.getParameter("Id_ServentiaRelacionada"));
			} else if(stFluxo.equals(ServentiaDt.FLUXO_AREA_DISTRIBUICAO)) {
				request.setAttribute("tempFluxo1", ServentiaDt.FLUXO_AREA_DISTRIBUICAO);
				servAreaDistDt.setId(request.getParameter("Id_ServAreaDist"));
			}
			break;

		// Excluir 
		case Configuracao.ExcluirResultado:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA)) {
				Serventiane.excluiServentiaRelacionada(Serventiadt);
				request.setAttribute("tempFluxo1", "OK");
				request.setAttribute("MensagemOk", "Serventia Relacionada Excluída com Sucesso");
				Serventiadt.setListaServentiasRelacoes( Serventiane.consultarServentiasRelacionadas(Serventiadt.getId()));
			} else if(stFluxo.equals(ServentiaDt.FLUXO_AREA_DISTRIBUICAO)) {
				servAreaDistDt.setId_UsuarioLog(Serventiadt.getId_UsuarioLog());
				servAreaDistDt.setIpComputadorLog(Serventiadt.getIpComputadorLog());
				Serventiane.excluiAreaDistribuicaoServentia(servAreaDistDt, UsuarioSessao.getUsuarioDt());
				
				request.setAttribute("tempFluxo1", "OK");
				request.setAttribute("MensagemOk", "Área Distribuição Excluída com Sucesso");
				stId = Serventiadt.getId();
				Serventiadt.limpar();
				servAreaDistDt.limpar();
				Serventiadt = Serventiane.consultarId(stId);
				tempList = Serventiane.consultarServentiasRelacionadas(Serventiadt.getId());
				areasDistribuicoes = Serventiane.consultarAreasDistribuicoesServentia(Serventiadt.getId());
				if (tempList.size() > 0) {
					Serventiadt.setListaServentiasRelacoes(tempList);
				}
				if (areasDistribuicoes.size() > 0){
					Serventiadt.setListaAreasDistribuicoes(areasDistribuicoes);
				}
				
			} else {
				Serventiane.excluir(Serventiadt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
			}

			break;

		case Configuracao.Novo:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA)) {					
				Serventiadt.setServentiaDtRelacaoEdicao( new ServentiaRelacionadaDt());
			} else if (stFluxo.equals(ServentiaDt.FLUXO_AREA_DISTRIBUICAO)) {
				servAreaDistDt.limpar();
			} else {
				Serventiadt.limpar();
				servAreaDistDt.limpar();
			}
			break;

		// Localizar
		case Configuracao.Localizar:
			if (request.getParameter("Passo")==null){
				if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA)) {
					request.setAttribute("tempFluxo1", ServentiaDt.FLUXO_SERVENTIA_RELACIONADA);
					String[] lisNomeBusca = {"ServentiaRelacao"};
					String[] lisDescricao = {"Serventia","Representatividade"};
					String[] camposHidden = {"UfRepresentatividade"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServentiaRelacao");
					request.setAttribute("tempBuscaDescricao","ServentiaRelacao");
					request.setAttribute("tempBuscaPrograma","ServentiaRelacionada");			
					request.setAttribute("tempRetorno","Serventia");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					Serventiadt.setServentiaDtRelacaoEdicao( new ServentiaRelacionadaDt());
					break;
				} else {
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Serventia");			
					request.setAttribute("tempRetorno","Serventia");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					Serventiadt.limpar();
					break;
				}
			} else {
				String stTemp="";
				stTemp = Serventiane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;								
			}
		case Configuracao.Salvar:
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			if (stFluxo.equals(ServentiaDt.FLUXO_AREA_DISTRIBUICAO)){
				request.setAttribute("tempFluxo1", ServentiaDt.FLUXO_AREA_DISTRIBUICAO);
			} else if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA)) {
				String presidencia = request.getParameter("rdPresidente");
				if (presidencia!=null && !presidencia.isEmpty()) {
					String nomeServentia = null;
					String mensagemErroPresidencia = null;
					tempList = Serventiadt.getListaServentiasRelacoes();
					ServentiaRelacionadaDt objTemp;					
					for(int i = 0 ; i< tempList.size();i++) {
						objTemp = (ServentiaRelacionadaDt)tempList.get(i);	
						objTemp.setPresidencia("False");
						if (objTemp.isMesmoId(presidencia)){
							nomeServentia = objTemp.getServentiaRelacao();
							//se vier o campo rdPresidente o campo presidencia sera true
							if (presidencia!=null) {
								objTemp.atualizacaoPresidencia();
								mensagemErroPresidencia = Serventiane.VerificarPresidenciaDaCamara(objTemp);
								if (mensagemErroPresidencia.length() == 0) {
									objTemp.setPresidencia("True");
									request.setAttribute("Id_ServentiaRelacionada", presidencia);
									request.setAttribute("mensagemServentiaPresidencia", "Clique para Salvar Presidência '" + nomeServentia + "'");
								}else {
									request.setAttribute("MensagemErro", mensagemErroPresidencia);							
									request.setAttribute("PaginaAnterior", Configuracao.Editar);
								}
							}
							//guardo a serventia relacionada para edição
							Serventiadt.setServentiaDtRelacaoEdicao(objTemp);
							break;
						} 
					}
					if (nomeServentia == null ){
						request.setAttribute("MensagemErro", "Selecione ou Escolha uma serventia para a edição");
					}
				}
				request.setAttribute("tempFluxo1", ServentiaDt.FLUXO_SERVENTIA_RELACIONADA);
			} else if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_BLOQUEIO)){
				request.setAttribute("tempFluxo1", ServentiaDt.FLUXO_SERVENTIA_BLOQUEIO);
				request.setAttribute("Mensagem", "Clique para Bloquear a serventia " + Serventiadt.getServentia());
			} else if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_DESBLOQUEIO)){
				request.setAttribute("tempFluxo1", ServentiaDt.FLUXO_SERVENTIA_DESBLOQUEIO);
				request.setAttribute("Mensagem", "Clique para Desbloquear a serventia " + Serventiadt.getServentia());
			}
			break;
		// Salva dados Serventia
		case Configuracao.SalvarResultado:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			Mensagem = Serventiane.Verificar(Serventiadt);
			if (Mensagem.length() == 0) {
				if (stFluxo.equals(ServentiaDt.FLUXO_AREA_DISTRIBUICAO)) {
					//SALVA AREA DISTITRIBUICAO ***********************************************************
					if (!servAreaDistDt.getId_AreaDist().equalsIgnoreCase("")) {
						servAreaDistDt.setId_Serv(Serventiadt.getId());
						servAreaDistDt.setId_UsuarioLog(Serventiadt.getId_UsuarioLog());
						servAreaDistDt.setIpComputadorLog(Serventiadt.getIpComputadorLog());
						Serventiane.SalvarAreaDistribuicaoServentia(servAreaDistDt, UsuarioSessao.getUsuarioDt());
						request.setAttribute("tempFluxo1", "OK");
						request.setAttribute("MensagemOk", "Área Distribuição salva com sucesso");
					} else {
						request.setAttribute("MensagemErro", "Selecione a Área distribuição");
					}
				} else if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_RELACIONADA)) {
					stId = request.getParameter("Id_ServentiaRelacionada");
					ServentiaRelacionadaDt obServentiaRelacionada = Serventiadt.getServentiaDtRelacaoEdicao();
					//atualizar presidencia
					if (obServentiaRelacionada.isAtualizacaoPresidencia()) {
						Serventiane.atualizeServentiaRelacionadaPresidencia(Serventiadt.getId(), obServentiaRelacionada.getId());
						request.setAttribute("MensagemOk", "Presidência definida com sucesso");
					}else {
						//SALVA SERVENTIA RELACIONADA***********************************************************
						Mensagem = Serventiane.VerificarServentiaRelacionada(Serventiadt);
						if (Mensagem.length() == 0) {						
							Serventiane.SalvarServentiaRelacionada(Serventiadt);
							request.setAttribute("tempFluxo1", "OK");
							request.setAttribute("MensagemOk", "Serventia Relacionada salva com sucesso");
						} else {
							request.setAttribute("MensagemErro", Mensagem);
							request.setAttribute("tempFluxo1", stFluxo);
						}
					}
					Serventiadt.setListaServentiasRelacoes( Serventiane.consultarServentiasRelacionadas(Serventiadt.getId()));
				} else if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_DESBLOQUEIO)){
					Serventiane.ativarServentia(Serventiadt);
					request.setAttribute("MensagemOk", "Serventia Desbloqueada com sucesso.");
				} else if (stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA_BLOQUEIO)){
					Serventiane.desativarServentia(Serventiadt);
					request.setAttribute("MensagemOk", "Serventia Bloqueada com sucesso.");
				} else {
					//SALVA SERVENTIA************************************************************************
					Serventiane.salvar(Serventiadt, servAreaDistDt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				}

				if (Mensagem == null || Mensagem.length() == 0) {
					stId = Serventiadt.getId();
					Serventiadt.limpar();
					servAreaDistDt.limpar();
					Serventiadt = Serventiane.consultarId(stId);
					tempList = Serventiane.consultarServentiasRelacionadas(Serventiadt.getId());
					areasDistribuicoes = Serventiane.consultarAreasDistribuicoesServentia(Serventiadt.getId());
					if (tempList.size() > 0) {
						Serventiadt.setListaServentiasRelacoes(tempList);
					}
					if (areasDistribuicoes.size() > 0){
						Serventiadt.setListaAreasDistribuicoes(areasDistribuicoes);
					}
				}				

			} else request.setAttribute("MensagemErro", Mensagem);
			break;
			
		//Prepara cópia de Serventia
		case Configuracao.Curinga6:
			Serventiadt.setId("");
			Serventiadt.setServentiaCodigo("");
			Serventiadt.limparId_Endereco();
			request.setAttribute("MensagemOk", "Serventia copiada.");
			break;

		// Consulta de Bairro - Usado no endereço da serventia
		case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Bairro","Cidade","Uf"};
				String[] lisDescricao = {"Bairro","Cidade","Uf"};
				String[] camposHidden = {"BairroCidade","BairroUf"};
				request.setAttribute("camposHidden",camposHidden);
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Bairro");
				request.setAttribute("tempBuscaDescricao", "Bairro");
				request.setAttribute("tempBuscaPrograma", "Bairro");
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else{
				String stTemp = "";
				stTemp = Serventiane.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		// Consulta de Tipos de Serventia
		case (ServentiaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Tipo de Serventia"};
				String[] lisDescricao = {"Tipo de Serventia","Código"};
				request.setAttribute("tempBuscaId", "Id_ServentiaTipo");
				request.setAttribute("tempBuscaDescricao", "ServentiaTipo");
				request.setAttribute("tempBuscaPrograma", "Tipo de Serventia");
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Serventiane.consultarDescricaoServentiaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		// Consulta de SubTipos de Serventia
		case (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"ServentiaSubtipo"};
				String[] lisDescricao = {"ServentiaSubtipo"};
				request.setAttribute("tempBuscaId", "Id_ServentiaSubtipo");
				request.setAttribute("tempBuscaDescricao", "ServentiaSubtipo");
				request.setAttribute("tempBuscaPrograma", "ServentiaSubtipo");
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Serventiane.consultarDescricaoServentiaSubtipoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		// Consulta de Áreas
		case (AreaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Area"};
				String[] lisDescricao = {"Area"};
				request.setAttribute("tempBuscaId", "Id_Area");
				request.setAttribute("tempBuscaDescricao", "Area");
				request.setAttribute("tempBuscaPrograma", "Area");
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (AreaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Serventiane.consultarDescricaoAreaJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;		
			
		// Consulta de comarcas
		case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Comarca"};
				String[] lisDescricao = {"Comarca"};
				request.setAttribute("tempBuscaId", "Id_Comarca");
				request.setAttribute("tempBuscaDescricao", "Comarca");
				request.setAttribute("tempBuscaPrograma", "Comarca");
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Serventiane.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;

		// Consulta de Áreas de Distribuição
		case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String idRetorno = request.getParameter("idRetorno");
				String descricaoRetorno = request.getParameter("descricaoRetorno");
				String[] lisNomeBusca = {descricaoRetorno};
				String[] lisDescricao = {descricaoRetorno};				
				String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
				request.setAttribute("camposHidden",camposHidden);
				request.setAttribute("tempBuscaId", idRetorno);
				request.setAttribute("tempBuscaDescricao", descricaoRetorno);
				request.setAttribute("tempBuscaPrograma", descricaoRetorno);
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("tempFluxo1", stFluxo);
				request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}
			else {
				String stTemp = "";
				if (stFluxo != null && stFluxo.equals("LAS")) { //área de distribuição serventia (campo inferior) (LAS)
					stTemp = Serventiane.consultarAreasDistribuicaoComarcaServentiaSubTipoJSON(stNomeBusca1, Serventiadt.getId_Comarca(), PosicaoPaginaAtual);
				}
				else { //área de distribuição (Primária e Secundária)
					stTemp = Serventiane.consultarAreasDistribuicaoRecursalJSON(stNomeBusca1,PosicaoPaginaAtual);
				}
				enviarJSON(response, stTemp);
				return;
			}			
			break;
			
		// Consulta de Tipos de Audiência
		case (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"AudienciaTipo"};
				String[] lisDescricao = {"AudienciaTipo"};
				request.setAttribute("tempBuscaId", "Id_AudienciaTipo");
				request.setAttribute("tempBuscaDescricao", "AudienciaTipo");
				request.setAttribute("tempBuscaPrograma", "AudienciaTipo");
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Serventiane.consultarDescricaoAudienciaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		// Consulta de Estados (Representatividade)
		case (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Estado"};
				String[] lisDescricao = {"Uf","Estado"};
				request.setAttribute("tempBuscaId", "Id_Estado");
				request.setAttribute("tempBuscaDescricao", "Estado");
				request.setAttribute("tempBuscaPrograma", "Estado");
				request.setAttribute("tempRetorno", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Serventiane.consultarDescricaoEstadoUfJSON(stNomeBusca1, PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;		
		
		// Consulta o log da serventia (Botao 'Visualizar Log')
		case Configuracao.Curinga7:
		
			List listaLogDt = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String ano = sdf.format(new Date());
						
			LogNe Logne = new LogNe();
			
			stAcao = "/WEB-INF/jsptjgo/ServentiaLog.jsp";
			
			if (request.getSession().getAttribute("Logne") != null) {
				Logne = (LogNe) request.getSession().getAttribute("Logne");
			}
			
			if (request.getParameter("AnoBusca") != null) {
				ano = request.getParameter("AnoBusca");
			}			
			
			listaLogDt = (List<LogDt>) Logne.obterListaLog(Serventiadt.getId(), ano, "Serventia");
			
			request.setAttribute("listaLogDt", listaLogDt);	
			request.setAttribute("AnoBuscaSelecionado", ano);
			
			break;
			
		// Consulta dados serventia
		default:
				
			if(paginaatual == Configuracao.Editar && stFluxo != null && stFluxo.equals(ServentiaDt.FLUXO_SERVENTIA) && request.getParameter("Id_Serventia") != null && request.getParameter("Id_Serventia").trim().length() > 0){
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Serventia");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Serventiadt.getId())) {
					Serventiadt.limpar();
					Serventiadt = Serventiane.consultarId(stId);
					tempList = Serventiane.consultarServentiasRelacionadas(Serventiadt.getId());
					areasDistribuicoes = Serventiane.consultarAreasDistribuicoesServentia(Serventiadt.getId());
					if (tempList.size() > 0) {
						Serventiadt.setListaServentiasRelacoes(tempList);
					}
					if (areasDistribuicoes.size() > 0){
						Serventiadt.setListaAreasDistribuicoes(areasDistribuicoes);
					}
				}				
			}
			
			break;
		}

		request.getSession().setAttribute("Enderecodt", Serventiadt.getEnderecoDt());
		request.getSession().setAttribute("ServAreaDistDt", servAreaDistDt);
		request.getSession().setAttribute("Serventiadt", Serventiadt);
		request.getSession().setAttribute("Serventiane", Serventiane);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
    
    /**
     * Armazena os dados da serventia relacionada no dt principal
     * 
     * @param id_ServentiaRelacionada
     * @param recebeProcesso
     * @param substitutoSegundoGrau
     * @param id_ServentiaSubstituicao
     * @param dataInicialSubstituicao
     * @param dataFinalSubstituicao
     * @param Serventiadt
     * @param Serventiane
     * @throws Exception
     */
	protected void setDadosServentiaRelacionadaEdicao(String id_ServentiaRelacionada,String id_ServentiaRelacao,String ServentiaRelacao, String recebeProcesso, String substitutoSegundoGrau, String id_ServentiaSubstituicao, String dataInicialSubstituicao, String dataFinalSubstituicao, String probabilidade, String UfRepresentatividade, ServentiaDt Serventiadt) throws Exception {
    	ServentiaRelacionadaDt serventiaDtRelacaoEdicao = Serventiadt.getServentiaDtRelacaoEdicao();
    	
		if ((id_ServentiaRelacionada!=null && !id_ServentiaRelacionada.isEmpty() && serventiaDtRelacaoEdicao==null)  || (id_ServentiaRelacionada!=null && serventiaDtRelacaoEdicao!=null && !serventiaDtRelacaoEdicao.isMesmoId(id_ServentiaRelacionada) && Serventiadt.getListaServentiasRelacoes()!=null)){
			Serventiadt.limparServentiaRelacionadaEdicao();
			for(ServentiaRelacionadaDt serventiaRelacionada : Serventiadt.getListaServentiasRelacoes()){				
				if (serventiaRelacionada != null && serventiaRelacionada.isMesmoId(id_ServentiaRelacionada)){
					serventiaDtRelacaoEdicao = new ServentiaRelacionadaDt();
					serventiaDtRelacaoEdicao.copiar(serventiaRelacionada);
					//Serventiadt.setServentiaDtRelacaoEdicao(serventiaRelacionada);				
					Serventiadt.setServentiaDtRelacaoEdicao(serventiaDtRelacaoEdicao);
					break;
				} 
			}
		}else {
    		if (serventiaDtRelacaoEdicao != null) {
    			if (recebeProcesso != null ) {
    				serventiaDtRelacaoEdicao.setRecebeProcesso(recebeProcesso);
    			}else {
    				serventiaDtRelacaoEdicao.setRecebeProcesso("false");
    			}
    			if (substitutoSegundoGrau == null) {
    				serventiaDtRelacaoEdicao.setDataInicialSubstituicao("");				
					serventiaDtRelacaoEdicao.setDataFinalSubstituicao("");					
    				serventiaDtRelacaoEdicao.setId_ServentiaSubstituicao("null");		
    			}else 	if ( substitutoSegundoGrau.equalsIgnoreCase("true") && id_ServentiaSubstituicao != null && !id_ServentiaSubstituicao.equalsIgnoreCase("") ) {
    				serventiaDtRelacaoEdicao.setSubstitutoSegundoGrau(substitutoSegundoGrau);				
    				serventiaDtRelacaoEdicao.setRecebeProcesso("false");
//    				ServentiaDt serventiaSubstituidaPrincipal = new ServentiaNe().consultarId(id_ServentiaSubstituicao);    				
    				serventiaDtRelacaoEdicao.setId_ServentiaSubstituicao(id_ServentiaSubstituicao);
    				for(ServentiaRelacionadaDt serventiaRelacionada : Serventiadt.getListaServentiasRelacoes()){				
    					if (serventiaRelacionada != null && serventiaRelacionada.getId_ServentiaRelacao().equals(id_ServentiaSubstituicao)){    						
    						serventiaDtRelacaoEdicao.setServentiaSubstituicao(serventiaRelacionada.getServentiaRelacao());	
    						break;
    					} 
    				}
    				if (dataInicialSubstituicao != null && !dataInicialSubstituicao.isEmpty() ) {
    					serventiaDtRelacaoEdicao.setDataInicialSubstituicao(dataInicialSubstituicao);
    				}
    				
    				if (dataFinalSubstituicao != null && !dataFinalSubstituicao.isEmpty() ) {
    					serventiaDtRelacaoEdicao.setDataFinalSubstituicao(dataFinalSubstituicao);		
    				}
    			} 
    			if(probabilidade!=null && !probabilidade.isEmpty()) {
    				serventiaDtRelacaoEdicao.setProbabilidade(probabilidade);
    			}
    			if(id_ServentiaRelacao!=null && !id_ServentiaRelacao.isEmpty()) {
    				serventiaDtRelacaoEdicao.setId_ServentiaRelacao(id_ServentiaRelacao);
    			}
    			if(ServentiaRelacao!=null && !ServentiaRelacao.isEmpty()) {
    				serventiaDtRelacaoEdicao.setServentiaRelacao(ServentiaRelacao);
    			}
    			
    			if(UfRepresentatividade!=null && !UfRepresentatividade.isEmpty()) {
    				serventiaDtRelacaoEdicao.setEstadoRepresentacao(UfRepresentatividade);
    			}
    			serventiaDtRelacaoEdicao.setId_ServentiaPrincipal(Serventiadt.getId());
    		}
		}
	}
}
