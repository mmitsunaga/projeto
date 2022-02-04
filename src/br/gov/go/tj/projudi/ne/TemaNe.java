package br.gov.go.tj.projudi.ne;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.dt.TemaAssuntoDt;
import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.dt.TemaOrigemDt;
import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.projudi.dt.TemaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.TemaPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.jus.cnj.bnpr.BnprTribunaisWS;
import br.jus.cnj.bnpr.ObjectFactory;
import br.jus.cnj.bnpr.RetornoTipoRG;
import br.jus.cnj.bnpr.RetornoTipoRR;
import br.jus.cnj.bnpr.TipoIAC;
import br.jus.cnj.bnpr.TipoIRDR;
import br.jus.cnj.bnpr.TipoPReNUT;
import br.jus.cnj.bnpr.TipoProcessoParadigma;
import br.jus.cnj.bnpr.TipoProcessoSobrestado;
import br.jus.cnj.bnpr.TipoRG;
import br.jus.cnj.bnpr.TipoRR;
import br.jus.cnj.bnpr.TipoResposta;
import br.jus.cnj.bnpr.TipoRespostaGuardarPrecedentesRepetitivos;
import br.jus.cnj.bnpr.TipoRespostaPesquisaParadigma;
import br.jus.cnj.bnpr.TipoRespostaRG;
import br.jus.cnj.bnpr.TipoRespostaRR;
import br.jus.cnj.bnpr.TipoSiglaOrgao;
import br.jus.cnj.bnpr.TipoSituacaoIAC;
import br.jus.cnj.bnpr.TipoSituacaoIRDR;
import br.jus.cnj.bnpr.TipoSituacaoRG;
import br.jus.cnj.bnpr.TipoSituacaoRR;
import br.jus.cnj.bnpr.TipoTipoPR;
import br.jus.cnj.bnpr.factory.BnprTribunaisWsFactory;
import br.jus.cnj.bnpr.model.Chave;
import br.jus.cnj.bnpr.model.Tupla;
import br.jus.cnj.bnpr.util.EnumUtil;

public class TemaNe extends TemaNeGen{

	private static final long serialVersionUID = 3632407704573417997L;

	public  String Verificar(TemaDt dados ) {

		String stRetorno="";

		if (dados.getTitulo().length()==0)
			stRetorno += "O Campo Titulo é obrigatório.";
		if (dados.getTemaCodigo().length()==0)
			stRetorno += "O Campo TemaCodigo é obrigatório.";
		if (dados.getTemaSituacao().length()==0)
			stRetorno += "O Campo TemaSituacao é obrigatório.";
		if (dados.getTemaOrigem().length()==0)
			stRetorno += "O Campo TemaOrigem é obrigatório.";
		if (dados.getTemaTipo().length()==0)
			stRetorno += "O Campo TemaTipo é obrigatório.";

		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String origem, String situacao, String codigo, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, origem, situacao, codigo, posicao);
			} finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	public String consultarDescricaoAssuntoJSON(String stNomeBusca1, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AssuntoNe neObjeto = new AssuntoNe();		
		stTemp = neObjeto.consultarDescricaoAssuntoJSON(stNomeBusca1, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public void salvar(TemaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Tema",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Tema",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			TemaAssuntoNe temaAssunto = new TemaAssuntoNe();
			temaAssunto.salvarAssuntos(dados.getListaAssuntos(), dados.getId(), obFabricaConexao);
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
									
		} catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Altera o valor da situção e a data de trânsito do tema
	 * @param dados
	 * @throws Exception
	 */
	public void alterarSituacaoEDataTransito(TemaDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try {			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();			
			TemaPs obPersistencia = new TemaPs(obFabricaConexao.getConexao()); 
			obPersistencia.alterarSituacaoEDataTransito(dados);			
			obLogDt = new LogDt("Tema", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);			
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
		
	public TemaDt consultarId(String id_tema) throws Exception {
		TemaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TemaPs obPersistencia = new TemaPs(obFabricaConexao.getConexao()); 
			dtRetorno= obPersistencia.consultarId(id_tema ); 
			TemaAssuntoNe temaAssuntoNe = new TemaAssuntoNe();
			dtRetorno.setListaAssuntos(temaAssuntoNe.consultarAssuntosId(id_tema));
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	//Exclui Todos
	public void excluirTodosAssuntos(TemaDt temaAssunto) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			TemaPs obPersistencia = new TemaPs(obFabricaConexao.getConexao()); 
			obPersistencia.excluirTodosAssuntos(temaAssunto);
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluirAssunto(TemaAssuntoDt temaAssunto) throws Exception {
		TemaAssuntoNe temaAssuntone = new TemaAssuntoNe(); 
		temaAssuntone.excluir(temaAssunto);
	}
	
	public void excluir(TemaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao());
			TemaAssuntoNe temaAssuntoNe = new TemaAssuntoNe();
			
			temaAssuntoNe.excluirAssuntos(dados.getListaAssuntos(), obFabricaConexao);

			obLogDt = new LogDt("Tema",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			
			

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta um tema pelo código e pela origem
	 * @param codigo
	 * @param id_origem
	 * @return
	 * @throws Exception
	 */
	public List<TemaDt> consultarTemaPorCodigoEOrigem(String codigo, String origem) throws Exception {
		List<TemaDt> lista = null;
		FabricaConexao obFabricaConexao = null;
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			TemaPs obPersistencia = new TemaPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarTemaPorCodigoEOrigem(codigo, origem);			
		} catch(Exception e){			
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	
	/**
	 * Consulta o maior código do Tema (precedente) de uma origem (STF, STJ, TJGO).	
	 * Esse método é utilizado para descobrir novos temas no webservice do CNJ
	 * @param id_origem
	 * @return
	 * @throws Exception
	 */
	public Integer consultarMaiorTemaCodigoPorOrigem(String origem) throws Exception {
		Integer ultimaTemaCodigo = 0;
		FabricaConexao obFabricaConexao = null;
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			TemaPs obPersistencia = new TemaPs(obFabricaConexao.getConexao());
			ultimaTemaCodigo = obPersistencia.consultarMaiorTemaCodigoPorOrigem(origem);
		} catch(Exception e){
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return ultimaTemaCodigo;
	}
	
	
	/**
	 * BnprTribunais - Serviço 1 - RG
	 * Consulta novos recursos RG na base do CNJ e que ainda não foram cadastrados no projudi. 
	 * Caso não exista um precedente, faz 3 tentativas e finaliza a execução do método. 
	 * @throws Exception  
	 * 
	 */
	public void BnprNovosPrecedentesRG() throws Exception {
		
		BnprTribunaisWS port = BnprTribunaisWsFactory.getInstance(new URL(ProjudiPropriedades.getInstance().getUrlBnprTribunais()));
		
		int tentativas = 0;
		TipoRespostaRG resposta = null;
		
		// Obtém o maior código do tema de origem STF: incrementa o valor para consultar o novo
		Long codigo = Long.valueOf(this.consultarMaiorTemaCodigoPorOrigem("STF") + 1);
				
		// Obtém o id do Tipo Tema para "RG" - Repercussão Geral
		TemaTipoNe temaTipoNe = new TemaTipoNe();
		TemaTipoDt temaTipoDt = temaTipoNe.consultarPorCodigoOuTipoCNJ(null, "RG");
		
		// Obtém o id do TipoOrigem para "STF"
		TemaOrigemNe temaOrigemNe = new TemaOrigemNe();
		TemaOrigemDt temaOrigemDt = temaOrigemNe.consultarPorCodigoOuDescricao(null, TipoSiglaOrgao.STF.value());
		
		do {
				
			// Consulta o precedente do tipo RG no CNJ
			resposta = port.pesquisarRepercussaoGeral(codigo, TipoSiglaOrgao.STF, 1);
			
			// Se encontrou um registro, cadastra o novo tema
			if (resposta.getTotalProcessos() > 0){
				
				try {
				
					RetornoTipoRG retornoTipoRG = resposta.getRepercussoesGeral().get(0);
					TipoRG tipoRG = retornoTipoRG.getTipoRG();
									
					TemaDt temaDt = new TemaDt();
					temaDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
					temaDt.setTemaCodigo(String.valueOf(tipoRG.getNumero()));										
					temaDt.setTitulo("TEMA " + String.valueOf(tipoRG.getNumero()) + " - STF");					
					temaDt.setQuesDireito(Funcoes.removerAspasSimplesDuplasETagsHtml(tipoRG.getQuestao()));
					temaDt.setTeseFirmada(Funcoes.removerAspasSimplesDuplasETagsHtml(tipoRG.getTese()));
					temaDt.setInfoLegislativa(Funcoes.removerAspasSimplesDuplasETagsHtml(tipoRG.getReferenciasLegislativas().getValue()));
					temaDt.setId_TemaTipo(temaTipoDt.getId());
					temaDt.setId_TemaOrigem(temaOrigemDt.getId());
					TemaSituacaoNe temaSituacaoNe = new TemaSituacaoNe();
					TemaSituacaoDt temaSituacaoDt = temaSituacaoNe.consultarPorCodigoOuSituacaoCNJ(null, tipoRG.getSituacao().value());
					if (temaSituacaoDt == null) throw new Exception("Não existe registro em TEMA_SITUACAO com a coluna tema_situacao_cnj = " + tipoRG.getSituacao().value());
					temaDt.setId_TemaSituacao(temaSituacaoDt.getId());
					
					this.salvar(temaDt);
					
					// Salva informações de LOG para inserção do novo TEMA
					obLog.salvar(new LogDt("CadastrarNovosPrecedentesRG", temaDt.getId(), UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.CadastrarNovosPrecedentesRG), temaDt.getPropriedades()));
																			
				} catch (Exception ex){
					obLog.salvar(new LogDt("CadastrarNovosPrecedentesRG", "", UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.Erro), "" , ex.getMessage()));
				}
				
				tentativas = 0;
				codigo++;
												
			} else {
				tentativas ++;
			}
			
		} while (resposta.getTotalProcessos() > 0 || tentativas < 2);
		
	}
	
	/**
	 * BnprTribunais - Serviço 3 - RR
	 * Consulta novos recursos RR na base do CNJ e que ainda não foram cadastrados no projudi. 
	 * Caso não exista um precedente, faz 3 tentativas e finaliza a execução do método. 
	 * @throws Exception  
	 */
	public void BnprNovosPrecedentesRR() throws Exception {
		
		int tentativas = 0;
		TipoRespostaRR resposta = null;
				
		BnprTribunaisWS port = BnprTribunaisWsFactory.getInstance(new URL(ProjudiPropriedades.getInstance().getUrlBnprTribunais()));
												
		// Obtém o maior código do tema de origem STF: incrementa o valor para consultar o novo
		Long codigo = Long.valueOf(this.consultarMaiorTemaCodigoPorOrigem("STJ") + 1);
		
		// Obtém o id do TEMA_TIPO para "RR" - Recurso Repetitivo
		TemaTipoNe temaTipoNe = new TemaTipoNe();
		TemaTipoDt temaTipoDt = temaTipoNe.consultarPorCodigoOuTipoCNJ(null, "RR");
					
		// Obtém o id do TEMA_ORIGEM para STJ
		TemaOrigemNe temaOrigemNe = new TemaOrigemNe();
		TemaOrigemDt temaOrigemDt = temaOrigemNe.consultarPorCodigoOuDescricao(null, TipoSiglaOrgao.STJ.value());
		
		do {
								
			// Consulta o precedente do tipo RR no CNJ
			resposta = port.pesquisarRecursosRepetitivos(codigo, TipoSiglaOrgao.STJ, 1);
			
			// Se encontrou um registro, cadastra o novo tema
			if (resposta.getTotalProcessos() > 0){
				
				try {
					
					RetornoTipoRR retornoTipoRR = resposta.getRecursosRepetitivos().get(0);
					TipoRR tipoRR = retornoTipoRR.getTipoRR();
					
					TemaDt temaDt = new TemaDt();
					temaDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
					temaDt.setTemaCodigo(String.valueOf(tipoRR.getNumero()));					
					temaDt.setTitulo("TEMA " + String.valueOf(tipoRR.getNumero()) + " - STJ");
					temaDt.setQuesDireito(Funcoes.removerAspasSimplesDuplasETagsHtml(tipoRR.getQuestao()));
					temaDt.setTeseFirmada(Funcoes.removerAspasSimplesDuplasETagsHtml(tipoRR.getTese()));
					temaDt.setInfoLegislativa(Funcoes.removerAspasSimplesDuplasETagsHtml(tipoRR.getReferenciasLegislativas().getValue()));
					temaDt.setId_TemaTipo(temaTipoDt.getId());
					temaDt.setId_TemaOrigem(temaOrigemDt.getId());
					TemaSituacaoNe temaSituacaoNe = new TemaSituacaoNe();
					TemaSituacaoDt temaSituacaoDt = temaSituacaoNe.consultarPorCodigoOuSituacaoCNJ(null, tipoRR.getSituacao().value());
					if (temaSituacaoDt == null) throw new Exception("Não existe registro em TEMA_SITUACAO com a coluna tema_situacao_cnj = " + tipoRR.getSituacao().value());
					temaDt.setId_TemaSituacao(temaSituacaoDt.getId());
					
					this.salvar(temaDt);
					
					// Salva informações de LOG para inserção do novo TEMA
					obLog.salvar(new LogDt("CadastrarNovosPrecedentesRR", temaDt.getId(), UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.CadastrarNovosPrecedentesRR), temaDt.getPropriedades()));
														
				} catch (Exception ex){
					obLog.salvar(new LogDt("CadastrarNovosPrecedentesRR", "", UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.Erro), "" , ex.getMessage()));
				}
				
				tentativas = 0;
				codigo++;
				
			} else {	
				tentativas ++;
			}
			
		} while (resposta.getTotalProcessos() > 0 || tentativas < 2);
		
	}
	
	/**
	 * BnprTribunais - Serviço 5 - IRDR/IAC
	 * Envia novos temas IRDR, IAC para o CNJ e salva o número de NUT no projudi
	 * @param codigo - código (tema_codigo) do novo tema IRDR/IAC
	 * @throws Exception  
	 */
	public void BnprEnviarNovoPrecedenteTJGO (TemaDt temaDt) throws Exception {
		
		Optional<TipoPReNUT> nut = null;
		
		TipoRespostaGuardarPrecedentesRepetitivos resposta = null;
		
		if (ValidacaoUtil.isNaoVazio(temaDt)){
			
			// Verifica se existe pelo menos 1 processo paradigma
			if (ValidacaoUtil.isVazio(temaDt.getVinculantes())) throw new Exception("É necessário informar pelo menos 1 processo vinculante");
			
			// Verifica se existe pelo menos 1 processo paradigma com classe cnj 
			Map<String, Integer> classeParadigma = getClasseCodigoDoParadigma(temaDt.getVinculantes());
			if (ValidacaoUtil.isVazio(classeParadigma)) throw new Exception("É necessário informar pelo menos 1 vinculante com classe CNJ.");
			
			// Verifica se existe pelo menos 1 assunto cadastrado no tema e que tenha código CNJ
			List<Integer> codigosAssuntos = new TemaAssuntoNe().consultarAssuntosCodigoPorIdTema(temaDt.getId());
			if (ValidacaoUtil.isVazio(codigosAssuntos)) throw new Exception ("É necessário informar pelo menos 1 assunto com código CNJ.");
			
			// Seta os processo(s) paradigma(s) do tema
			List<TipoProcessoParadigma> listaParadigmas = new ArrayList<>();
			for (Entry<String, Integer> e : classeParadigma.entrySet()){
				TipoProcessoParadigma paradigma = new TipoProcessoParadigma();
				paradigma.setNumero(e.getKey());
				paradigma.setClasse(e.getValue());
				listaParadigmas.add(paradigma);					
			}
			
			// Conecta com o webservice bnprTribunais do CNJ
			BnprTribunaisWS port = BnprTribunaisWsFactory.getInstance(new URL(ProjudiPropriedades.getInstance().getUrlBnprTribunais()));

			if (temaDt.getTemaTipoCnj().equals("IAC")){
				
				// Verifica se a situação do tema tem um valores correspondente do CNJ (TEMA_SITUACAO, coluna tema_situacao_cnj)
				TipoSituacaoIAC tipoSituacaoIAC = EnumUtil.getTipoSituacaoIACFromString(temaDt.getTemaSituacaoCnj());
				if (ValidacaoUtil.isVazio(tipoSituacaoIAC)) throw new Exception ("Não foi encontrado uma situação CNJ correspondente para " + temaDt.getTemaSituacao());
				
				// Preenche o objeto TipoIAC com os dados do tema do projudi
				TipoIAC tipoIAC = new TipoIAC();
				tipoIAC.setNumero(Long.valueOf(temaDt.getTemaCodigo()));
				tipoIAC.setQuestao(temaDt.getQuesDireito());
				JAXBElement<String> refLegislativa = new ObjectFactory().createTipoPrecedenteRepetitivo1ReferenciasLegislativas(temaDt.getInfoLegislativa());
				tipoIAC.setReferenciasLegislativas(refLegislativa);
				tipoIAC.setTese(temaDt.getTeseFirmada());
				tipoIAC.setSituacao(tipoSituacaoIAC);
				tipoIAC.getAssuntos().addAll(codigosAssuntos);
				
				if (ValidacaoUtil.isNaoVazio(temaDt.getDataAdmissao())){
					Date dataAdmissao = Funcoes.StringToDate(temaDt.getDataAdmissao());
					//tipoIAC.setDataInstauracao(Funcoes.asXMLGregorianCalendar(dataAdmissao));
					tipoIAC.setDataAdmissao(Funcoes.asXMLGregorianCalendar(dataAdmissao));
				}
				
				// Adiciona o tema TipoIAC na lista de envio
				List<TipoIAC> lista = new ArrayList<TipoIAC>();
				lista.add(tipoIAC);
				
				// Faz o envio do IRDR para o cnj utilizando o webservice
				resposta = port.enviarIncidentesAssuncaoCompetencias(lista);
				if (ValidacaoUtil.isNaoVazio(listaParadigmas)){
					port.enviarProcessosParadigmas(Long.valueOf(temaDt.getTemaCodigo()), TipoTipoPR.IAC, listaParadigmas);
				}				
				
			}
			
			if (temaDt.getTemaTipoCnj().equals("IRDR")){
				
				// Verifica se a situação do tema tem um valores correspondente do CNJ (TEMA_SITUACAO, coluna tema_situacao_cnj)
				TipoSituacaoIRDR tipoSituacaoIRDR = EnumUtil.getTipoSituacaoIRDRFromString(temaDt.getTemaSituacaoCnj());
				if (ValidacaoUtil.isVazio(tipoSituacaoIRDR)) throw new Exception ("Não foi encontrado uma situação CNJ correspondente para " + temaDt.getTemaSituacao());
				
				// Preenche o objeto TipoIRDR com os dados do tema do projudi
				TipoIRDR tipoIRDR = new TipoIRDR();
				tipoIRDR.setNumero(Long.valueOf(temaDt.getTemaCodigo()));
				tipoIRDR.setQuestao(temaDt.getQuesDireito());
				JAXBElement<String> refLegislativa = new ObjectFactory().createTipoPrecedenteRepetitivo1ReferenciasLegislativas(temaDt.getInfoLegislativa());
				tipoIRDR.setReferenciasLegislativas(refLegislativa);
				tipoIRDR.setTese(temaDt.getTeseFirmada());
				tipoIRDR.setSituacao(tipoSituacaoIRDR);
				tipoIRDR.getAssuntos().addAll(codigosAssuntos);
				
				if (ValidacaoUtil.isNaoVazio(temaDt.getDataAdmissao())){
					Date dataAdmissao = Funcoes.StringToDate(temaDt.getDataAdmissao());
					tipoIRDR.setDataAdmissao(Funcoes.asXMLGregorianCalendar(dataAdmissao));
				}
				
				// Adiciona o tema TipoIRDR na lista de envio
				List<TipoIRDR> lista = new ArrayList<TipoIRDR>();
				lista.add(tipoIRDR);
				
				// Faz o envio do IAC para o cnj utilizando o webservice
				resposta = port.enviarIRDemandasRepetitivas(lista);
				if (ValidacaoUtil.isNaoVazio(listaParadigmas)){
					port.enviarProcessosParadigmas(Long.valueOf(temaDt.getTemaCodigo()), TipoTipoPR.IRDR, listaParadigmas);
				}
				
			}
			
			if (resposta.isSucesso()){				
				
				List<TipoPReNUT> preNuts = resposta.getPrecedentesSalvos();
				nut = preNuts.stream().findFirst();
				
				if (nut.isPresent()){
					
					// Atribui o valor do NUT
					temaDt.setNumeroIrdrCnj(nut.get().getNut());
					temaDt.setTemaCodigo(nut.get().getNumero().toString());
					
					// Atualiza o tema com o novo valor de NUT
					this.salvar(temaDt);
					obLog.salvar(new LogDt("Tema", temaDt.getId(), temaDt.getId_UsuarioLog(), temaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), temaDt.getPropriedades(), temaDt.getNumeroIrdrCnj()));
					
				} else {
					throw new Exception ("Não foi possível obter o número NUT. Tente novamente.");
				}
				
			} else {
				throw new Exception("[BnprTribunaisWS] - " + resposta.getMensagem().replaceAll("'", "\""));
			}
			
		} 
		
	}
	
	/**
	 * BnprTribunais - Serviço 6 - OK
	 * Enviar processos sobrestados para o CNJ
	 * @throws Exception  
	 */
	public void BnprEnviarProcessosSobrestados(List<Tupla> listaExterna) throws Exception {
		
		List<Tupla> processosSobrestados = new ArrayList<Tupla>();
		
		if (ValidacaoUtil.isNaoVazio(listaExterna)){
			processosSobrestados.addAll(listaExterna);
			
		} else {
			ProcessoTemaNe processoTemaNe = new ProcessoTemaNe();
			processosSobrestados.addAll(processoTemaNe.listarProcessosSobrestadosPorPeriodo(null, null));
			processosSobrestados.addAll(processoTemaNe.listarProcessosSobrestados_SSG());
			processosSobrestados.addAll(processoTemaNe.listarProcessosNaoSobrestadosDiaAnterior(Funcoes.getDiaAnterior()));
		}
		
		// Faz o agrupamento de processos por código do precedente (Tema)
		Map<Chave, List<Tupla>> processosPorCodigoTema = processosSobrestados.stream().collect(Collectors.groupingBy(Tupla::getChave));
		
		if (processosPorCodigoTema.size() > 0) {
			
			// Conecta com o webservice do CNJ para envio de processos sobrestados
			BnprTribunaisWS port = BnprTribunaisWsFactory.getInstance(new URL(ProjudiPropriedades.getInstance().getUrlBnprTribunais()));
			
			// Percorre os processos sobrestados agrupados e envia para o cnj
			for (Entry<Chave, List<Tupla>> item : processosPorCodigoTema.entrySet()){
											
				try {
					
					// Codigo do precedente (tema_codigo)
					Long codigo = item.getKey().getCodigo();
						
					// Tipo do precedente
					TipoTipoPR tipo = EnumUtil.getTipoPrecedenteFromString(item.getKey().getTipo());
					
					// Sigla do Órgão do precedente (tema origem)
					TipoSiglaOrgao siglaOrgao = EnumUtil.getTipoSiglaOrgaoFromString(item.getKey().getSigla());
								
					// Cria uma lista de TipoProcessoSobrestado para o código e o tipo do precedente
					List<TipoProcessoSobrestado> listaProcessosSobrestados = item.getValue().stream().map(t -> t.getProcesso()).collect(Collectors.toList());
					
					// Envia o processo pelo webservice
					TipoResposta resposta = port.enviarProcessosSobrestados(codigo, tipo, siglaOrgao, listaProcessosSobrestados);
					
					// Salva o registro no LOG
					if (resposta.isSucesso()){
						for (Tupla t : item.getValue()){
							
							// Grava no log a informação de sucesso
							obLog.salvar(new LogDt("EnviarProcessoSobrestadosCNJ", "", UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.EnviarProcessoSobrestadosCNJ), t.toJSON().toString()));
						}
					} else {
						throw new Exception(resposta.getMensagem() + " Info[" + item.getKey().toJSON().toString() + "]("+ item.getValue().size() + ")");
					}
													
				} catch (Exception ex){
					obLog.salvar(new LogDt("EnviarProcessoSobrestadosCNJ", "", UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.Erro), "" , ex.getMessage()));
					continue;
				}
			}
			
		}
				
		// Libera os recursos
		processosSobrestados.clear();		
		processosPorCodigoTema.clear();
		processosSobrestados = null;
		processosPorCodigoTema = null;
		
	}
	
	/**
	 * BnprTribunais - Serviço 7
	 * Verifica se o tema (precedente) associado ao processo foi julgado e atualiza a data de transito.
	 * Logo em seguida, verifica-se se todos os temas de um processo foi transitado e cria-se uma pendência de aviso.
	 */
	public void BnprBaixaPrecedenteEmProcessoSobrestado() throws Exception {
		
		// Consulta os Temas (precedentes) que estão associados a processos e estão em aberto (não TRANSITO JULGADO)
		ProcessoTemaNe processoTemaNe = new ProcessoTemaNe();
		List<ProcessoTemaDt> lista = processoTemaNe.consultarProcessoTemaComSituacaoTransitadoOuCancelado();
		
		// Se existe registros, conectar no webservice CNJ
		if (ValidacaoUtil.isNaoVazio(lista)){
			
			try {
				
				// Conecta com o webservice do CNJ e verifica a situação do precedente
				BnprTribunaisWS port = BnprTribunaisWsFactory.getInstance(new URL(ProjudiPropriedades.getInstance().getUrlBnprTribunais()));
				
				for (ProcessoTemaDt proc : lista){
					
					// Obtém o código do tema
					Long codigo = Long.valueOf(proc.getTema().getTemaCodigo());
										 	
					// Obtém o tipo do precedente
					TipoTipoPR tipoPR = EnumUtil.getTipoPrecedenteFromString(proc.getTema().getTemaTipoCnj());
					
					// Obtém a sigla de origem
					TipoSiglaOrgao siglaOrgao = EnumUtil.getTipoSiglaOrgaoFromString(proc.getTema().getTemaOrigem());
																									
					// Escolher o tipo do precedente e fazer a consulta ao webservice para obter a nova situação.
					if (ValidacaoUtil.isNaoVazio(tipoPR)){
						
						String situacaoCNJ = "";
						boolean transitadoJulgado = false;
												
						try {
							if (tipoPR.equals(TipoTipoPR.RG)){
								TipoRespostaRG respostaRG = port.pesquisarRepercussaoGeral(codigo, siglaOrgao, 1);
								if (respostaRG.getTotalProcessos() > 0 && respostaRG.getRepercussoesGeral().size() > 0){
									RetornoTipoRG retornoTipoRG = respostaRG.getRepercussoesGeral().get(0);
									TipoRG tipoRG = retornoTipoRG.getTipoRG();
									transitadoJulgado = tipoRG.getSituacao().equals(TipoSituacaoRG.TRANSITO_JULGADO) || tipoRG.getSituacao().equals(TipoSituacaoRG.CANCELADO);
									situacaoCNJ =  tipoRG.getSituacao().toString();
								}
							}							
							if (tipoPR.equals(TipoTipoPR.RR)){
								TipoRespostaRR respostaRR = port.pesquisarRecursosRepetitivos(codigo, siglaOrgao, 1);
								if (respostaRR.getTotalProcessos() > 0 && respostaRR.getRecursosRepetitivos().size() > 0){
									RetornoTipoRR retornoTipoRR = respostaRR.getRecursosRepetitivos().get(0);
									TipoRR tipoRR = retornoTipoRR.getTipoRR();
									transitadoJulgado = tipoRR.getSituacao().equals(TipoSituacaoRR.TRANSITADO_JULGADO) || tipoRR.getSituacao().equals(TipoSituacaoRR.CANCELADO);
									situacaoCNJ =  tipoRR.getSituacao().toString();
								}
							}							
						} catch (Exception ex){
							continue;
						}
																		
						// Se o tema está com a situacação TRANSITADO_JULGADO ou TRANSITO_JULGADO no CNJ
						if (transitadoJulgado){
							
							// Consulta a data de transito do tema em questão através do webservice "PesquisarProcessoParadigma"
							String dataTransito = consultarDataTransitoDoTema(port, codigo, tipoPR, siglaOrgao);
							
							// Obtém a situação pelo descrição do CNJ
							TemaSituacaoDt temaSituacaoDt = new TemaSituacaoNe().consultarPorCodigoOuSituacaoCNJ(null, situacaoCNJ);
							
							// Atualiza a situação e a data de transito
							if (ValidacaoUtil.isNaoNulo(dataTransito) && ValidacaoUtil.isNaoNulo(temaSituacaoDt)){
								TemaDt temaDt = new TemaDt();
								temaDt.setId(proc.getId_Tema());
								temaDt.setId_TemaSituacao(temaSituacaoDt.getId());
								temaDt.setDataTransito(dataTransito);
								temaDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
								new TemaNe().alterarSituacaoEDataTransito(temaDt);
							}
							
						}
						
					}	
					
				}
				
			} catch (Exception ex){
				obLog.salvar(new LogDt("AtualizarSituacaoTemaCNJ", "", UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.Erro), "" , ex.getMessage()));				
			}
						
			/*
			 * Consulta os processos que tiveram os temas com situação transitado julgado e cria as pendências
			 */
			List<ProcessoDt> listaProcessoDt = processoTemaNe.consultarProcessoComTodosTemasTransitadoJulgado();
			if (ValidacaoUtil.isNaoVazio(listaProcessoDt)){
				PendenciaNe pendenciaNe = new PendenciaNe();
				for (ProcessoDt p : listaProcessoDt){
					p.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
					p.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
					p.setIpComputadorLog("Scheduling");
					List<ProcessoTemaDt> listaProcessoTemaDt = processoTemaNe.consultarTemasProcessoPorIdProcesso(p.getId_Processo(), null);
					try {
						pendenciaNe.gerarPendenciaVerificarTemaTransitoJulgado(p, listaProcessoTemaDt, p.getId_Serventia());
					} catch (Exception ex){
						obLog.salvar(new LogDt("GerarPendenciaVerificarTema", "", UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.Erro), p.getPropriedades() , ex.getMessage()));
						continue;
					}
				}
			}
			
			// Libera os recursos			
			lista = null;
			
		}
		
	}
	
	/**
	 * Consulta a data de transito julgado de um precedente 
	 * @param id
	 * @return
	 */
	private String consultarDataTransitoDoTema (BnprTribunaisWS port, Long codigo, TipoTipoPR tipo, TipoSiglaOrgao sigla){
		try {
			// Consulta os processos paradigmas do tema
			TipoRespostaPesquisaParadigma resposta = port.pesquisarProcessosParadigmas(codigo, tipo, sigla, null, 1);
			if (resposta.isSucesso() && resposta.getTotalProcessos() > 0){
				// Obtém o 1a valor do atributo "data de transito"
				for (TipoProcessoParadigma tipoProcessoParadigma : resposta.getProcessos()){
					if (ValidacaoUtil.isNaoVazio(tipoProcessoParadigma.getDataTransitoJulgado())){
						Date dataTransito = Funcoes.asDate(tipoProcessoParadigma.getDataTransitoJulgado());
						return Funcoes.FormatarData(dataTransito);
					}
				}
			}			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Obtém o código da classe dos processos paradigmas do tema específico.
	 * @param vinculantesTema
	 * @throws Exception 
	 */
	private Map<String, Integer> getClasseCodigoDoParadigma(String vinculantesTema) throws Exception {
		Map<String, Integer> mapa = new HashMap<>();
		ProcessoNe processoNe = new ProcessoNe();
		String[] arrVinculantes = vinculantesTema.split(",|;|\\s");
		for (String numeroProcesso : arrVinculantes){
			ProcessoDt processoDt = processoNe.consultarIdCnjClassePorProcessoNumeroCompleto(numeroProcesso);
			if (ValidacaoUtil.isNaoNulo(processoDt)){
				if (ValidacaoUtil.isNaoVazio(processoDt.getProcessoTipoCodigo())){
					mapa.put(numeroProcesso, Integer.valueOf(processoDt.getCodigoTemp()));
				}
			}
		}
		return mapa;
	}
	
}
