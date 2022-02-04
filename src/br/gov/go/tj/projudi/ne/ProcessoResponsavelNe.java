package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.utils.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ProcessoResponsavelPs;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class ProcessoResponsavelNe extends ProcessoResponsavelNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8966900236890261454L;

	public String Verificar(ProcessoResponsavelDt dados) {

		String stRetorno = "";

		if (dados.getServentiaCargo().length() == 0) stRetorno += "O Campo ServentiaCargo � obrigat�rio.";
		if (dados.getProcessoNumero().length() == 0) stRetorno += "O Campo ProcessoNumero � obrigat�rio.";
		return stRetorno;

	}

	/**
	 * Verifica dados obrigat�rios na troca de respons�vel do processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavel(ProcessoResponsavelDt dados, String serventiaTipoCodigo, String serventiaSubtipoCodigo, String grupoCodigo, boolean ehSemAssistente) throws Exception {
		String stRetorno = "";
		if (!ehSemAssistente && (dados.getId_ServentiaCargo().length() == 0 && dados.getId_ServentiaCargo().length() == 0)) stRetorno = "Selecione o Novo Respons�vel pelo Processo. \n";
		if (dados.getListaProcessos() == null || dados.getListaProcessos().size() == 0) stRetorno += "Nenhum Processo foi selecionado. \n";
		else if (!ehSemAssistente) {
			List processos = dados.getListaProcessos();
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				// Novo respons�vel n�o pode ser o mesmo que o atual
				if (processoDt.getServentiaCargoResponsavelDt() != null && processoDt.getServentiaCargoResponsavelDt().getId().equals(dados.getId_ServentiaCargo())) stRetorno += dados.getServentiaCargo() + " j� � Respons�vel pelo Processo " + processoDt.getProcessoNumero() + ". \n";
			}
		}
		
		// Validando altera��o de respons�veis pelo processo de segundo grau.
		if (!ehSemAssistente && stRetorno.length() == 0 && serventiaTipoCodigo != null && serventiaSubtipoCodigo != null &&
			serventiaTipoCodigo.equalsIgnoreCase(String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU)) &&	ServentiaSubtipoDt.isSegundoGrau(serventiaSubtipoCodigo)){
			
			ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
			
			List processos = dados.getListaProcessos();
			for (int i = 0; i < processos.size(); i++) {
				
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);				
				List listaResponsaveis = neObjeto.consultarResponsaveisProcesso(processoDt.getId(), grupoCodigo);
				
				if (listaResponsaveis != null && listaResponsaveis.size() > 0){
          			for (int j=0; j < listaResponsaveis.size(); j++){
          				ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) listaResponsaveis.get(j);
          				// Novo respons�vel n�o pode ser o mesmo que o atual
          				if (serventiaCargoDt != null && serventiaCargoDt.getId().equals(dados.getId_ServentiaCargo())){
          					stRetorno += dados.getServentiaCargo() + " j� � Respons�vel pelo Processo " + processoDt.getProcessoNumero() + " - Cargo: " + serventiaCargoDt.getCargoTipo() + ". \n";
          					break;
          				}     				
          				
          			}
	          	}				
			}		
		}		
		return stRetorno;
	}
	
	/**
	 * M�todo que valida se o novo relator que est� sendo inclu�do j� n�o se encontra registrado no processo.
	 * @param dados - registro que est� sendo inclu�do
	 * @throws Exception - mensagem de erro em caso de se confirmar e erro de cadastro
	 * @author hmgodinho
	 */
	public void verificarInclusaoRelatorDesabilitado(ProcessoResponsavelDt dados) throws Exception {
		List listaResponsaveis = this.consultarResponsaveisDesabilitadosProcesso(dados.getId_Processo());
		
		if (listaResponsaveis != null && listaResponsaveis.size() > 0){
			for (int j=0; j < listaResponsaveis.size(); j++){
				ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) listaResponsaveis.get(j);
				// Novo respons�vel n�o pode ser o mesmo que o atual
				if (serventiaCargoDt != null && serventiaCargoDt.getId().equals(dados.getId_ServentiaCargo())){
					throw new MensagemException("Relator informado j� � um dos respons�veis desabilitados pelo processo. \n");
				}     				
			}
	  	}	
	}
	
	/**
	 * Verifica dados obrigat�rios na troca de respons�vel do processo parte
	 * 
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavelProcessoParte(ProcessoResponsavelDt dados, String id_UsuarioServentiaAtual){
		String stRetorno = "";
		if (dados.getId_UsuarioServentia() == null || dados.getId_UsuarioServentia().length() == 0){ 
			stRetorno += "Selecione o Novo Respons�vel pelo Processo. \n";
		}
		
		//Se for troca de defensor p�blico n�o precisa fazer essa duas valida��es seguintes.
		if (id_UsuarioServentiaAtual == null || id_UsuarioServentiaAtual.length() == 0) {
				stRetorno += "Selecione o Respons�vel que ser� Substitu�do. \n";
		}
		
		if (dados.getId_UsuarioServentia() != null && dados.getId_UsuarioServentia().length() > 0 && id_UsuarioServentiaAtual != null && id_UsuarioServentiaAtual.length() > 0
					&& dados.getId_UsuarioServentia().equals(id_UsuarioServentiaAtual)) {
				stRetorno += "Selecione um Respons�vel diferente do atual no Processo. \n";
		}
		
		return stRetorno;
	}

//	/**
//	 * Verifica se o processo possui respons�vel para ser alterado
//	 * @author lsbernardes
//	 * @throws Exception 
//	 */
//	public String verificarResponsavel(ProcessoResponsavelDt dados){
//		String stRetorno = "";
//		
//		List processos = dados.getListaProcessos();
//		for (int i = 0; i < processos.size(); i++) {
//			ProcessoDt processoDt = (ProcessoDt) processos.get(i);
//			// Novo respons�vel n�o pode ser o mesmo que o atual
//			if (processoDt.getServentiaCargoResponsavelDt() == null) 
//				stRetorno += "A Conclus�o ainda n�o foi distribu�da! N�o � poss�vel trocar o assistente pois o mesmo ainda n�o existe. \n";
//		}
//		return stRetorno;
//	}
	
	/**
	 * M�todo respons�vel em salvar um ProcessoRespons�vel, e recebe a conex�o ativa
	 */
	public void salvar(ProcessoResponsavelDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoResponsavel", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoResponsavel", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			dados.setId("");
			throw e;
		}
	}

	/**
	 * M�todo respons�vel em vincular um Juiz a um Processo
	 * @param id_Processo, identifica��o do processo
	 * @param id_ServentiaCargo, id_serventiaCargo do juiz que ser� respons�vel pelo processo
	 * @param conexao, conex�o ativa
	 * 
	 * @author msapaula
	 */
	public void salvarProcessoResponsavel(String id_Processo, String id_ServentiaCargo, boolean redator, int cargoTipoCodigo, LogDt logDt, FabricaConexao conexao) throws Exception {
					
		ProcessoResponsavelDt processoResponsavelDt = new ProcessoResponsavelDt();
		processoResponsavelDt.setId_Processo(id_Processo);
		processoResponsavelDt.setId_ServentiaCargo(id_ServentiaCargo);
		processoResponsavelDt.setCargoTipoCodigo(String.valueOf(cargoTipoCodigo));
		processoResponsavelDt.setId_UsuarioLog(logDt.getId_Usuario());
		processoResponsavelDt.setIpComputadorLog(logDt.getIpComputador());
		processoResponsavelDt.setRedator(redator);

		salvar(processoResponsavelDt, conexao);		
	}

	/**
	 * Verifica se determinado usu�rio � respons�vel em um processo, validando o serventiaCargo do usu�rio
	 * @param id_ServentiaCargo, serventiaCargo do usu�rio
	 * @param id_Processo, identifica��o do processo
	 * @author msapaula
	 */
	public boolean isResponsavelProcesso(String id_ServentiaCargo, String id_Processo) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			if (id_ServentiaCargo != null && id_ServentiaCargo.length() > 0 && id_Processo != null && id_Processo.length() > 0) boRetorno = obPersistencia.isResponsavelProcesso(id_ServentiaCargo, id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}
	
	/**
	 * Verifica se determinado usu�rio � respons�vel em um processo, validando o serventiaCargo do usu�rio
	 * @param id_ServentiaCargo, serventiaCargo do usu�rio
	 * @param id_Processo, identifica��o do processo
	 * @author lsbernardes
	 */
	public boolean isResponsavelProcesso(String id_ServentiaCargo, String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		boolean boRetorno = false;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		if (id_ServentiaCargo != null && id_ServentiaCargo.length() > 0 && id_Processo != null && id_Processo.length() > 0) boRetorno = obPersistencia.isResponsavelProcesso(id_ServentiaCargo, id_Processo);
		
		return boRetorno;
	}

	/**
	 * M�todo que realiza chamada a outros m�todos para consultar o serventiaCargo que ser� respons�vel por uma
	 * conclus�o de acordo com o tipo de pend�ncia passado.
	 * Ex.: se for passado tipo de pend�ncia "Concluso - Decis�o Relator", a conclus�o deve ser gerada para o 
	 * serventiaCargo do relator
	 * 
	 * @param processoDt dt de processo
	 * @param pendenciaTipoCodigo c�digo do tipo de pend�ncia
	 * @conexao conex�o ativa
	 * 
	 * @author msapaula
	 */
	public ServentiaCargoDt getServentiaCargoConclusao(ProcessoDt processoDt, String pendenciaTipoCodigo, FabricaConexao conexao) throws Exception {

		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		switch (Funcoes.StringToInt(pendenciaTipoCodigo)) {
			//Se autos conclusos, o respons�vel sera o juiz respons�vel pelo processo (ServentiaCargo)
			case PendenciaTipoDt.CONCLUSO_DECISAO:
			case PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA:
			case PendenciaTipoDt.CONCLUSO_SENTENCA:
			case PendenciaTipoDt.CONCLUSO_DESPACHO:
			case PendenciaTipoDt.CONCLUSO_GENERICO:
				return this.getJuizResponsavelProcesso(processoDt.getId(), processoDt.getId_Serventia(), conexao);

				//Se concluso para presidente, ser� retornado o presidente da turma recursal
			case PendenciaTipoDt.CONCLUSO_PRESIDENTE:
			case PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE:				
				return serventiaCargoNe.getPresidenteTurmaRecursal(processoDt.getId_Serventia(), conexao);
				//Se concluso para relator, ser� retornado o relator respons�vel pelo processo

			case PendenciaTipoDt.CONCLUSO_RELATOR:
			case PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR:
				return this.getRelatorResponsavelProcesso(processoDt.getId(), processoDt.getId_Serventia(), conexao);
			case PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO:				
				return serventiaCargoNe.getPresidenteTribunalDeJustica(conexao);
			case PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO:				
				return serventiaCargoNe.getVicePresidenteTribunalDeJustica(conexao);
		}
		return null;
	}
	
	public ServentiaCargoDt getServentiaCargoConclusaoUPJ(ProcessoDt processoDt, String pendenciaTipoCodigo, FabricaConexao conexao) throws Exception {

		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		switch (Funcoes.StringToInt(pendenciaTipoCodigo)) {
			//Se autos conclusos, o respons�vel sera o juiz respons�vel pelo processo (ServentiaCargo)
			case PendenciaTipoDt.CONCLUSO_DECISAO:
			case PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA:
			case PendenciaTipoDt.CONCLUSO_SENTENCA:
			case PendenciaTipoDt.CONCLUSO_DESPACHO:
			case PendenciaTipoDt.CONCLUSO_GENERICO:
				return this.consultarJuizUPJ(processoDt.getId(), conexao);
			case PendenciaTipoDt.CONCLUSO_RELATOR:
			case PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR:			
				return this.consultarJuizUPJRelator(processoDt.getId(), conexao);
			case PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO:				
				return serventiaCargoNe.getPresidenteTribunalDeJustica(conexao);
			case PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO:				
				return serventiaCargoNe.getVicePresidenteTribunalDeJustica(conexao);
		}
		return null;
	}

	/**
	 * Retorna o ServentiaCargo do tipo Juiz respons�vel pelo processo em uma determinada serventia.
	 * @throws Exception 
	 */
	public ServentiaCargoDt getJuizResponsavelProcesso(String id_Processo, String id_Serventia) throws Exception{
		//return this.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, CargoTipoDt.JUIZ_1_GRAU, null);
		return this.consultarServentiaCargoResponsavelProcessoAtivo(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), null);
	}

	/**
	 * Retorna o ServentiaCargo do tipo Juiz respons�vel pelo processo em uma determinada serventia.
	 * Foi adicionado o parametro da serventia, pois com as redistribui��es um processo pode ter
	 * um juiz respons�vel por cada serventia em que passou.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param id_serventia serventia para a qual quer encontrar o juiz respons�vel pelo processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public ServentiaCargoDt getJuizResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		//return this.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, CargoTipoDt.JUIZ_1_GRAU, conexao);
		return this.consultarServentiaCargoResponsavelProcessoAtivo(id_Processo, id_Serventia,  String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), conexao);
	}

	/**
	 * Retorna o ServentiaCargo do tipo Delegado respons�vel pelo processo.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public ServentiaCargoDt getAutoridadePolicialResponsavelProcesso(String id_Processo, FabricaConexao conexao) throws Exception {
		//return this.consultarServentiaCargoResponsavelProcesso(id_Processo, null, CargoTipoDt.AUTORIDADE_POLICIAL, conexao);
		return this.consultarServentiaCargoResponsavelProcessoAtivo(id_Processo, null, String.valueOf(GrupoTipoDt.AUTORIDADE_POLICIAL), conexao);
	}
	
	/**
	 * Retorna o ServentiaCargo do tipo Relator respons�vel pelo processo.
	 * Foi adicionado o parametro da serventia, pois com as redistribui��es um processo pode ter
	 * um relator respons�vel por cada serventia em que passou.
	 * 
	 * @param id_Processo
	 * @param id_Serventia
	 * @return
	 * @throws Exception
	 */
	public ServentiaCargoDt getRelatorResponsavelProcesso(String id_Processo, String id_Serventia) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			return this.consultarServentiaCargoResponsavelProcessoAtivo(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.JUIZ_TURMA), obFabricaConexao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Retorna o ServentiaCargo do tipo Relator respons�vel pelo processo.
	 * Foi adicionado o parametro da serventia, pois com as redistribui��es um processo pode ter
	 * um relator respons�vel por cada serventia em que passou.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public ServentiaCargoDt getRelatorResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		//return this.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, CargoTipoDt.RELATOR_TURMA, conexao);
		return this.consultarServentiaCargoResponsavelProcessoAtivo(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.JUIZ_TURMA), conexao);
	}

	/**
	 * Retorna o ServentiaCargo do tipo Promotor respons�vel pelo processo.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public ServentiaCargoDt getPromotorResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		//return this.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, CargoTipoDt.PROMOTOR_JUIZADO, conexao);
		return this.consultarServentiaCargoResponsavelProcessoAtivo(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.MP), conexao);
	}
	
	/**
	 * Retorna o ServentiaCargo do tipo Assistente de Gabinete respons�vel pelo processo.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public ServentiaCargoDt getAssistenteGabineteResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		//return this.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, CargoTipoDt.ASSISTENTE_GABINETE, conexao);
		return this.consultarServentiaCargoResponsavelProcessoAtivo(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE), conexao);
	}
	
	/**
	 * Retorna o ServentiaCargo do tipo Assistente de Gabinete respons�vel pelo processo e que o cargo esteja ativo.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param conexao conex�o ativa
	 * @author lsbernardes
	 */
	public ServentiaCargoDt getAssistenteAtivoGabineteResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		return this.consultarServentiaCargoAtivoResponsavelProcesso(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE), conexao);
	}

	/**
	 * Retorna o respons�vel "Ativo" pelo processo de acordo com o Tipo de Cargo desejado, 
	 * e em uma determinada serventia
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_Serventia, identifica��o da serventia
	 * @param cargoTipoCodigo, determina qual respons�vel deve ser retornado (juiz, relator..)
	 * @param conexao conex�o ativa
	 * 
	 * @author msapaula
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoAtivo(String id_Processo, String id_Serventia, String grupoTipoCodigo, FabricaConexao  conexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			serventiaCargoDt = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, grupoTipoCodigo, true);			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return serventiaCargoDt;
	}

	/**
	 * M�todo para consultar relator ativo do processo. M�todo foi criado para atender necessidade de se consultar
	 * os dados do relator enquanto se realiza envio para inst�ncia superior, por isso se passa a conex�o como par�metro.
	 * @param idProcesso 
	 * @param conexao
	 * @return id do relator
	 * @throws Exception
	 */
	public String consultarRelatorProcesso(String idProcesso, FabricaConexao conexao) throws Exception {
		String idRelator = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			idRelator = obPersistencia.consultarRelatorProcesso(idProcesso);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return idRelator;
	}
	
	/**
	 * Retorna o respons�vel "Ativo" pelo processo de acordo com o Tipo de Cargo desejado, 
	 * e em uma determinada serventia
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_Serventia, identifica��o da serventia
	 * @param cargoTipoCodigo, determina qual respons�vel deve ser retornado (juiz, relator..)
	 * @param conexao conex�o ativa
	 * 
	 * @author lsbernardes
	 */
	public ServentiaCargoDt consultarServentiaCargoAtivoResponsavelProcesso(String id_Processo, String id_Serventia, String grupoTipoCodigo, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			serventiaCargoDt = obPersistencia.consultarServentiaCargoAtivoResponsavelProcesso(id_Processo, id_Serventia, grupoTipoCodigo, true);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o respons�vel "Inativo" pelo processo de acordo com o Tipo de Cargo desejado, 
	 * e em uma determinada serventia
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_Serventia, identifica��o da serventia
	 * @param cargoTipoCodigo, determina qual respons�vel deve ser retornado (juiz, relator..)
	 * @param conexao conex�o ativa
	 * 
	 * @author msapaula
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoInativo(String id_Processo, String id_Serventia, String grupoTipoCodigo, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			serventiaCargoDt = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, grupoTipoCodigo, false);				
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o respons�vel "Inativo" pelo processo do segundo Grau de acordo com o Tipo de Cargo desejado, 
	 * e em uma determinada serventia
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_Serventia, identifica��o da serventia
	 * @param conexao conex�o ativa
	 * 
	 * @author lsbernardes
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoSegundoGrauInativo(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(id_Serventia);
			if (serventiaDt.isTurma()) {
				serventiaCargoDt = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.JUIZ_TURMA), false);
			} else {
				serventiaCargoDt = obPersistencia.consultarServentiaCargoResponsavelProcessoSegundoGrau(id_Processo, id_Serventia, false);
			}
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return serventiaCargoDt;
	}
	
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoSegundoGrauAtivo(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(id_Serventia);
			if (serventiaDt.isTurma()) {
				serventiaCargoDt = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.JUIZ_TURMA), true);
			} else {
				serventiaCargoDt = obPersistencia.consultarServentiaCargoResponsavelProcessoSegundoGrau(id_Processo, id_Serventia, true);
			}
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o respons�vel ATIVO pelo processo do segundo Grau de acordo com o processo e em uma determinada serventia
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_Serventia, identifica��o da serventia
	 * @param conexao conex�o ativa
	 * 
	 * @author hmgodinho
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoSegundoGrauAtivo(String id_Processo, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			serventiaCargoDt = obPersistencia.consultarServentiaCargoResponsavelProcessoSegundoGrauAtivo(id_Processo);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return serventiaCargoDt;
	}

	
	/**
	 * Retorna o ServentiaCargo redator para ser o novo relator
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Desembargador pode atuar como  Relator, Vogal e Revisor 	 * 
	 * @param processoDt identifica��o do processo	
	 * @author jrcorrea
	 */
	

	public ServentiaCargoDt consultarProcessoResponsavelRedator2Grau(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		serventiaCargoDt = obPersistencia.consultarProcessoResponsavelRedator2Grau(id_Processo);
		
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo redator para ser o novo relator
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Desembargador pode atuar como  Relator, Vogal e Revisor 	 * 
	 * @param processoDt identifica��o do processo	
	 * @author jrcorrea
	 */
	

	public ServentiaCargoDt consultarRelator2GrauElaboracaoVoto(String id_Processo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			serventiaCargoDt = obPersistencia.consultarRelator2Grau(id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaCargoDt;
	}

	/**
	 * Retorna o ServentiaCargo respons�vel por um processo, de um determinado tipo e em uma determinada serventia.
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Juiz pode atuar como Relator, Vogal e Revisor e seu ServentiaCargo � somente do tipo DESEMBARGADOR
	 * 
	 * @param processoDt identifica��o do processo
	 * @param id_serventia identifica��o da serventia
	 * @param cargoTipoCodigo tipo do respons�vel a ser retornado
	 * @author msapaula
	 */
//	public ServentiaCargoDt consultarProcessoResponsavelCargoTipo(String id_Processo, String id_Serventia, String cargoTipoCodigo, FabricaConexao conexao){
//		ServentiaCargoDt serventiaCargoDt = null;
//		FabricaConexao obFabricaConexao = null;
//		try{
//			if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			else obFabricaConexao = conexao;
//			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
//
//			serventiaCargoDt = obPersistencia.consultarProcessoResponsavelCargoTipo(id_Processo, id_Serventia, cargoTipoCodigo);
//		} finally {
//			if (conexao == null) obFabricaConexao.fecharConexao();
//		}
//		return serventiaCargoDt;
//	}

	/**
	 * Retorna o ServentiaCargo respons�vel por um processo, de um determinado tipo.
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Desembargador pode atuar como Relator, Vogal e Revisor 
	 * @param id_Processo
	 * @param cargoTipoCodigo
	 * @param boAtivo
	 * @return
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarProcessoResponsavel2Grau(String id_Processo, int cargoTipoCodigo, boolean boAtivo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			return consultarProcessoResponsavel2Grau(id_Processo, cargoTipoCodigo, boAtivo, obFabricaConexao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	/**
	 * Retorna o ServentiaCargo respons�vel por um processo, de um determinado tipo.
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Desembargador pode atuar como Relator, Vogal e Revisor 	 
	 * @param processoDt identifica��o do processo	 * 
	 * @param cargoTipoCodigo tipo do respons�vel a ser retornado
	 * @author jrcorrea
	 */
	public ServentiaCargoDt consultarProcessoResponsavel2Grau(String id_Processo, int cargoTipoCodigo, boolean boAtivo, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		serventiaCargoDt = obPersistencia.consultarProcessoResponsavel2Grau(id_Processo, cargoTipoCodigo, boAtivo);
		return serventiaCargoDt;
	}
	

	/**
	 * Retorna o ServentiaCargo respons�vel por um processo, de um determinado tipo.
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para UPJ 	 
	 * @param processoDt identifica��o do processo	 * 
	 * @param cargoTipoCodigo tipo do respons�vel a ser retornado
	 * @author jrcorrea
	 */
	public ServentiaCargoDt consultarJuizUPJ_Relator(String id_Processo, int cargoTipoCodigo, boolean boAtivo, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			serventiaCargoDt = obPersistencia.consultarJuizUPJ_Relator(id_Processo, cargoTipoCodigo, boAtivo);
		} finally{			
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo do Relator de um processo de 2� grau.
	 * @param id_Processo, identifica��o do processo
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarRelator2Grau(String id_Processo, FabricaConexao conexao) throws Exception {
		return this.consultarProcessoResponsavel2Grau(id_Processo, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, true, conexao);
	}
	
	public ServentiaCargoDt consultarRelator2GrauInativo(String id_Processo, FabricaConexao conexao) throws Exception {
		return this.consultarProcessoResponsavel2Grau(id_Processo, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, false, conexao);
	}
	
	public ServentiaCargoDt consultarJuizUPJ(String id_Processo, FabricaConexao conexao) throws Exception {
		return this.consultarJuizUPJ_Relator(id_Processo, CargoTipoDt.JUIZ_UPJ, true, conexao);
	}
	
	public ServentiaCargoDt consultarJuizUPJRelator(String id_Processo, FabricaConexao conexao) throws Exception {
		return this.consultarJuizUPJ_Relator(id_Processo, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, true, conexao);
	}
	/**
	 * Retorna o ServentiaCargo do Revisor de um processo de 2� grau.
	 * @param id_Processo, identifica��o do processo
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarRevisor2Grau(String id_Processo, FabricaConexao conexao) throws Exception {
		return this.consultarProcessoResponsavel2Grau(id_Processo,  CargoTipoDt.REVISOR_SEGUNDO_GRAU, true, conexao);
	}

	/**
	 * Retorna o ServentiaCargo do Vogal de um processo de 2� grau.
	 * @param id_Processo, identifica��o do processo
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarVogal2Grau(String id_Processo, FabricaConexao conexao) throws Exception {
		return this.consultarProcessoResponsavel2Grau(id_Processo, CargoTipoDt.VOGAL_SEGUNDO_GRAU, true, conexao);
	}

	/**
	 * Retorna o respons�vel "Ativo" pelo processo de acordo com o grupo do usu�rio que est� consultando.
	 * Se usu�rio � analista da vara ver� o juiz respons�vel, ou ent�o, se usu�rio � analista da turma
	 * dever� ver o relator respons�vel.
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param grupoTipoCodigo, determina o grupo do usu�rio logado
	 * 
	 * @author msapaula
	 */
	public ServentiaCargoDt consultarResponsavelProcesso(String id_Processo, String grupoTipoCodigo, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		ServentiaCargoDt serventiaCargo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(grupoTipoCodigo)) {
				case GrupoTipoDt.DISTRIBUIDOR:
				case GrupoTipoDt.ANALISTA_VARA:
				case GrupoTipoDt.TECNICO_VARA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.ANALISTA_EXECUCAO:
					serventiaCargo = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, null, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), true);
					break;

				case GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU:
				case GrupoTipoDt.TECNICO_TURMA_SEGUNDO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
					if(serventiaTipoCodigo != null &&   
						serventiaTipoCodigo.trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU)) &&   
						(ServentiaSubtipoDt.isSegundoGrau(serventiaSubTipoCodigo) || ServentiaSubtipoDt.isUPJTurmaRecursal(serventiaSubTipoCodigo))
					){
						serventiaCargo = obPersistencia.consultarRelatorResponsavelProcessoSegundoGrau(id_Processo);	
					}	else{
						serventiaCargo = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, null, String.valueOf(GrupoTipoDt.JUIZ_TURMA), true);	
					}					
					break;
				case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
					serventiaCargo = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, null, String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE), true);
					break;
				case GrupoTipoDt.MP:
					serventiaCargo = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, null, String.valueOf(GrupoTipoDt.MP), true);
					break;
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaCargo;
	}
	
	/**
	 * Retorna o ServentiaCargo respons�vel por um processo, de um determinado tipo e em uma determinada serventia.
	 * 
	 * @param processoDt - identifica��o do processo
	 * @param id_serventia - identifica��o da serventia
	 * @param cargoTipoCodigo - tipo do respons�vel a ser retornado
	 * @param boAtivo - se o respons�vel est� ativo ou inativo no processo
	 * @author hmgodinho
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcesso(String idProcesso, String idServentia, String grupoTipoCodigo, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			serventiaCargo = obPersistencia.consultarServentiaCargoResponsavelProcesso(idProcesso, idServentia, grupoTipoCodigo, boAtivo);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaCargo;
		
	}
	
	/**
	 * Retorna os promotores respons�veis.
	 * 
	 * @param id_Processo, identifica��o do processo
	 * 
	 * @author mmgomes
	 */
	public List consultarResponsavelProcessoPromotores(String id_Processo) throws Exception {
		return consultarResponsavelProcessoPromotores(id_Processo, false);
	}
	
	/**
	 * Retorna o respons�vel "Ativo" pelo processo de acordo com o grupo do usu�rio que est� consultando.
	 * Se usu�rio � analista da vara ver� o juiz respons�vel, ou ent�o, se usu�rio � analista da turma
	 * dever� ver o relator respons�vel.
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param boSomenteAtivo, determina se � para consultar somente os promotores ativos
	 * 
	 * @author mmgomes
	 */
	public List consultarResponsavelProcessoPromotores(String id_Processo, boolean boSomenteAtivo) throws Exception {
		List serventiaCargo = null;
		FabricaConexao obFabricaConexao = null;
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			serventiaCargo = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, boSomenteAtivo);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaCargo;
	}
	
	public ServentiaCargoDt consultarAssistenteResponsavelProcesso(String id_Processo, String id_Serventia, String grupoTipoCodigo, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		ServentiaCargoDt serventiaCargo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(grupoTipoCodigo)) {
				case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				case GrupoTipoDt.GERENCIAMENTO_SEGUNDO_GRAU:
					serventiaCargo = obPersistencia.consultarServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE), true);
				
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaCargo;
	}

	/**
	 * Consulta todos os respons�veis por um processo.
	 * No caso de usu�rios externos n�o poder�o visualizar respons�veis do tipo Assistente de Gabinete.
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param grupoCodigo, c�digo do grupo que est� efetuando a consulta
	 * @author msapaula
	 */
	public List consultarResponsaveisProcesso(String id_Processo, String grupoCodigo) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			//Se for usu�rio externo, consulta os respons�veis que ele pode visualizar
			if (new UsuarioNe().isUsuarioExterno(grupoCodigo)){
				lista = obPersistencia.consultarResponsaveisProcessoAcessoExterno(id_Processo);
			} else lista = obPersistencia.consultarResponsaveisProcesso(id_Processo);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Consulta todos os respons�veis desabilitados por um processo.
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @author hmgodinho
	 */
	public List consultarResponsaveisDesabilitadosProcesso(String id_Processo) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarResponsaveisDesabilitadosProcesso(id_Processo);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * M�todo que consulta todos os respons�veis pelo processo, exceto usu�rios do MP.
	 * @param id_Processo - ID do processo
	 * @return lista de respons�veis
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarTodosResponsaveisProcesso(String id_Processo) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarTodosResponsaveisProcesso(id_Processo);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Consulta todos os responsaveis por um processo (ProcessoParteAdvogadoDt).
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param id_Serventia, identifica��o da serventia do usu�rio logado
	 * @param id_usu_serv, indentifica��o do usuario serventia vinculado a parte
	 * @author lsbernardes
	 */
	public List consultarProcessosPartesAdvogados(String id_Processo, String id_Serventia, String idUsuarioServentia) throws Exception {
		List lista = null;
		ProcessoParteAdvogadoNe processoParteAdvogadoNe = new ProcessoParteAdvogadoNe();
		lista = processoParteAdvogadoNe.consultarProcessosPartesAdvogados(id_Processo, id_Serventia, idUsuarioServentia);
		return lista;
	}
	
	public List consultarJuizesSegundoGrauResponsaveisProcesso(String id_Processo) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			lista = obPersistencia.consultarJuizesSegundoGrauResponsaveisProcesso(id_Processo);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	public List consultarJuizesSegundoGrauGabinete(String id_Serventia) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			lista = obPersistencia.consultarJuizSegundoGrauGabinete(id_Serventia);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	public List consultarJuizesTurma(String id_Serventia) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			lista = obPersistencia.consultarJuizesTurma(id_Serventia);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}

	/**
	 * Retorna o Id_UsuarioServentia do Juiz respons�vel pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param id_serventia serventia para a qual quer encontrar o juiz respons�vel pelo processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public String getUsuarioJuizResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
		String stRetorno = null;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarJuizResponsavelProcesso(id_Processo, id_Serventia);
		
		return stRetorno;
	}

	/**
	 * Retorna o Id_UsuarioServentia do Relator respons�vel pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param id_serventia serventia para a qual quer encontrar o relator respons�vel pelo processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public String getUsuarioRelatorResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
		String stRetorno = null;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarUsuarioResponsavelProcesso(id_Processo, id_Serventia, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		
		return stRetorno;
	}
	
	/**
	 * Retorna o Id_UsuarioServentia do Relator respons�vel pelo processo.
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Juiz pode atuar como Relator, Vogal e Revisor e seu ServentiaCargo � somente do tipo Desembargador
	 * @param id_Processo
	 * @param conexao
	 * @return
	 */
	public String getUsuarioRelatorResponsavelCargoTipo(String id_Processo, FabricaConexao obFabricaConexao) throws Exception{
		String stRetorno = null;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarUsuarioResponsavelCargoTipo(id_Processo, null, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		
		return stRetorno;
	}
	
	/**
	 * Retorna o Id_UsuarioServentia do Revisor respons�vel pelo processo.
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Juiz pode atuar como Relator, Vogal e Revisor e seu ServentiaCargo � somente do tipo Desembargador
	 * @param id_Processo
	 * @param conexao
	 * @return
	 */
	public String getUsuarioRevisorResponsavelCargoTipo(String id_Processo, FabricaConexao obFabricaConexao) throws Exception{
		String stRetorno = null;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarUsuarioResponsavelCargoTipo(id_Processo, null, CargoTipoDt.REVISOR_SEGUNDO_GRAU);
		
		return stRetorno;
	}
	
	/**
	 * Retorna o Id_UsuarioServentia do Vogal respons�vel pelo processo.
	 * Esse m�todo ir� buscar o CargoTipo da pr�pria tabela ProcessoResponsavel, para os casos do 2� grau
	 * onde o Juiz pode atuar como Relator, Vogal e Revisor e seu ServentiaCargo � somente do tipo Desembargador
	 * @param id_Processo
	 * @param conexao
	 * @return
	 */
	public String getUsuarioVogalResponsavelCargoTipo(String id_Processo, FabricaConexao obFabricaConexao) throws Exception{
		String stRetorno = null;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarUsuarioResponsavelCargoTipo(id_Processo, null, CargoTipoDt.VOGAL_SEGUNDO_GRAU);
		
		return stRetorno;
	}

	/**
	 * Atualizar Troca de Respons�vel de um ou mais processos.
	 * 
	 * @param id_ServentiaCargo, identifica��o do novo serventia cargo
	 * @param listaProcessos, lista de processos que ter� o respons�vel alterado
	 * @param logDt, objeto log
	 * @author msapaula
	 */
	public void AtualizarTrocaResponsavel(String id_ServentiaCargo, List listaProcessos, String cargoTipoCodigo, LogDt logDt, boolean ehSemAssistente) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) listaProcessos.get(i);

				if (processoDt.getServentiaCargoResponsavelDt() == null) {
					//Se n�o tinha um respons�vel deve inserir
					if (!ehSemAssistente) this.salvarProcessoResponsavel(processoDt.getId(), id_ServentiaCargo,true, Funcoes.StringToInt(cargoTipoCodigo), logDt, obFabricaConexao);

				} else if (ehSemAssistente) {				
					//Remove o respons�vel das pend�ncias vinculadas ao cargo
					PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
					responsavelNe.removerResponsaveisPendenciasProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), logDt, obFabricaConexao);

					//Remove o respons�vel por conclus�es vinculadas ao cargo
					responsavelNe.removerResponsavelConclusao(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), logDt, obFabricaConexao);
					
					//Remove tamb�m o respons�vel do processo					
					this.removerResponsavelProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), logDt, obFabricaConexao, null);
				} else {
					//Altera respons�vel do processo
					this.alterarResponsavelProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao, null);

					//Altera tamb�m o respons�vel das pend�ncias vinculadas ao cargo
					PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
					responsavelNe.atualizarResponsaveisPendenciasProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);

					//Altera o respons�vel por conclus�es vinculadas ao cargo
					responsavelNe.atualizarResponsavelConclusao(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);

					//Se possuir uma audi�ncia ou sess�o de 2� marcada deve trocar o relator respons�vel por essa tamb�m
					AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
					audienciaProcessoNe.atualizarResponsavelAudiencia(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);
				}
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Respons�vel de um ou mais processos.
	 * 
	 * @param id_ServentiaCargo, identifica��o do novo serventia cargo
	 * @param listaProcessos, lista de processos que ter� o respons�vel alterado
	 * @param logDt, objeto log
	 * @param usuarioDt, usuario logado no sistema
	 * @param movimentacaoProcessoDt, movimenta��o necess�ria para efetuar troca
	 * @author msapaula
	 */
	public void salvarTrocaResponsavel(String id_ServentiaCargo, List listaProcessos, String cargoTipoCodigo, LogDt logDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt) throws MensagemException, Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			
			
			String complemento = movimentacaoProcessoDt.getComplemento(); 

			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) listaProcessos.get(i);

				if (processoDt.getServentiaCargoResponsavelDt() == null) {
					//Se n�o tinha um respons�vel deve inserir
					this.salvarProcessoResponsavel(processoDt.getId(), id_ServentiaCargo,true, Funcoes.StringToInt(cargoTipoCodigo), logDt, obFabricaConexao);

					/* ---------- PONTEIRO ----------------*/
					///salvo o ponteiro
					Date dtAgora = new Date();
					new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.GANHO_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, id_ServentiaCargo  ),obFabricaConexao );
					/* ---------- PONTEIRO ----------------*/

				} else {
					//Altera respons�vel do processo
					this.alterarResponsavelProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao, null);
					
					/* ---------- PONTEIRO ----------------*/
					///salvo o ponteiro
					Date dtAgora = new Date();
					new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.GANHO_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, id_ServentiaCargo  ),obFabricaConexao );
					
					new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.PERDA_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, processoDt.getServentiaCargoResponsavelDt().getId()  ),obFabricaConexao );
					/* ---------- PONTEIRO ----------------*/
					
					//Altera tamb�m o respons�vel das pend�ncias vinculadas ao cargo
					PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
					responsavelNe.atualizarResponsaveisPendenciasProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);

					//Altera o respons�vel por conclus�es vinculadas ao cargo
					responsavelNe.atualizarResponsavelConclusao(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);

					//Se possuir uma audi�ncia ou sess�o de 2� marcada deve trocar o relator respons�vel por essa tamb�m
					AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
					audienciaProcessoNe.atualizarResponsavelAudiencia(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);
					
					//Atualizando descri��o da movimenta��o para incluir o nome do novo respons�vel pelo processo
					ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
					ServentiaCargoDt novoResponsavelDt = serventiaCargoNe.consultarId(id_ServentiaCargo);
					movimentacaoProcessoDt.setComplemento(complemento + "Novo respons�vel: " + novoResponsavelDt.getNomeUsuario());
				}
			}
			
			if (movimentacaoProcessoDt.getRedirecionaOutraServentia() != null && !movimentacaoProcessoDt.getRedirecionaOutraServentia().equals("")){
				MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
				movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
			
		
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Respons�vel de um ou mais processos.
	 * 
	 * @param id_ServentiaCargo, identifica��o do novo serventia cargo
	 * @param listaProcessos, lista de processos que ter� o respons�vel alterado
	 * @param logDt, objeto log
	 * @author lsbernardes
	 */
	public void salvarTrocaResponsavelPromotor(String id_ServentiaCargo, String id_ServentiaCargoAnterior, String id_UsuarioServentia, String grupoCodigo, List listaProcessos, String cargoTipoCodigo, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) listaProcessos.get(i);
				
				//Essa valida��o � uma tentativa para evitar as duplicidades de promotores nos processos.
				//Nos testes realizados, esse foi o �nico ponto em que se poderia gerar essa duplicidade.
				//Por algum motivo, o respons�vel chega null aqui e um novo insert na tabela de proc_resp � feito.
				//Caso chegue aqui o valor null (podendo ser o erro da duplicidade ou uma inser��o comum), 
				// o sistema consultar� novamente e pegar� o promotor que est� respons�vel pelo processo. 
				//Isso impedir� a duplicidade. Caso n�o haja promotor cadastrado, o sistema vai fazer a inclus�o do 
				//promotor normalmente.
				//@author hmgodinho
				if (processoDt.getServentiaCargoResponsavelDt() == null) {
					ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
					processoDt.setServentiaCargoResponsavelDt(processoResponsavelNe.getPromotorResponsavelProcesso(processoDt.getId(), null, obFabricaConexao));
				}
				
				//Para evitar erro no update de respons�vel, caso o id_UsuarioServentia chegar null at�
				//esse ponto, vai consultar e preencher o campo desde que haja id_ServentiaCargo.
				//@author hmgodinho
				if(id_UsuarioServentia == null || id_UsuarioServentia.equalsIgnoreCase("")){
					ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
					ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(id_ServentiaCargo);
					id_UsuarioServentia = serventiaCargoDt.getId_UsuarioServentia();
				}

				if (processoDt.getServentiaCargoResponsavelDt() == null) {
					//Se n�o tinha um respons�vel deve inserir
					this.salvarProcessoResponsavel(processoDt.getId(), id_ServentiaCargo,true, Funcoes.StringToInt(cargoTipoCodigo), logDt, obFabricaConexao);

				} else {
					//Altera respons�vel do processo
					this.alterarResponsavelProcesso(processoDt.getId(), id_ServentiaCargoAnterior, id_ServentiaCargo, logDt, obFabricaConexao, null);

					//Altera tamb�m o respons�vel das pend�ncias vinculadas ao cargo
					PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
					responsavelNe.atualizarResponsaveisPendenciasProcessoPromotoria(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, grupoCodigo, logDt, obFabricaConexao);

					//Altera tamb�m o respons�vel das pend�ncias vinculadas ao usuarioServentia (Substituto processual)
					responsavelNe.atualizarResponsaveisPendenciasProcessoPromotorSubstituto(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId_UsuarioServentia(), id_UsuarioServentia, grupoCodigo, logDt, obFabricaConexao);
					
					//Altera tamb�m o promotor Substituto processual
					ProcessoParteAdvogadoNe processoParteAdvogadoNe = new ProcessoParteAdvogadoNe();
					List processoParteAdvogado = processoParteAdvogadoNe.consultarProcessoPartePromotorSubstitutoProcessual(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId_UsuarioServentia(), obFabricaConexao);
					
					if (processoParteAdvogado != null && processoParteAdvogado.size() > 0){
						for (Iterator iterator = processoParteAdvogado.iterator(); iterator.hasNext();) {
							ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) iterator.next();
							processoParteAdvogadoNe.atualizarPromotorSubstitutoProcessual(processoParteAdvogadoDt.getId_ProcessoParte(), processoDt.getServentiaCargoResponsavelDt().getId_UsuarioServentia(), id_UsuarioServentia, logDt, obFabricaConexao);
						}
					}
					
					//Altera o respons�vel por conclus�es vinculadas ao cargo
					responsavelNe.atualizarResponsavelConclusao(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);

					//Se possuir uma audi�ncia ou sess�o de 2� marcada deve trocar o relator respons�vel por essa tamb�m
					AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
					audienciaProcessoNe.atualizarResponsavelAudiencia(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_ServentiaCargo, logDt, obFabricaConexao);
				}
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Troca procurador/defensor/advogado Respons�vel de um ou mais processo, altera��o em processo parte advogado.
	 * 
	 * @param String, identifica��o do novo usuario serventia
	 * @param String, identifica��o do atual usuario serventia
	 * @param String, codigo do grupo
	 * @param String, id_Usu_Serv_Realizador identifica��o do usu�rio serventia que est� realizando a opera��o
	 * @param listaProcessos, lista de processos que ter� o respons�vel alterado
	 * @param logDt, objeto log
	 * @throws Exeception
	 * 
	 * @author lsbernardes
	 */
	public void salvarTrocaResponsavelProcessoParteAdvogado(String id_UsuarioServentiaNovo, String id_UsuarioServentiaAtual, String id_Usu_Serv_Realizador, String grupoCodigo, List listaProcessos, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    ProcessoParteAdvogadoNe processoParteAdvogadoNe = new ProcessoParteAdvogadoNe();
	    MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		try{
			
			if (listaProcessos == null || listaProcessos.size()==0){
				throw new MensagemException("Nenhum processo encontrado para realizar opera��o!");
			}
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();

			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) listaProcessos.get(i);
				//Altera processo parte advogado de um processo vinculado ao id_UsuarioServentiaAtual
				processoParteAdvogadoNe.atualizarProcessoParteAdvogado(processoDt.getId_Processo(), id_UsuarioServentiaNovo, id_UsuarioServentiaAtual, logDt, obFabricaConexao);

				//Altera tamb�m o respons�vel das pend�ncias vinculadas ao id_UsuarioServentiaAtual
				PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
				responsavelNe.atualizarResponsavelPendenciaIntimacaoCitacao(processoDt.getId(), id_UsuarioServentiaAtual, id_UsuarioServentiaNovo, grupoCodigo, logDt, obFabricaConexao);
				
				int inGrupoCodigo = Funcoes.StringToInt(grupoCodigo);
				
				if ( inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL  || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL
						 || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL || inGrupoCodigo == GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA || inGrupoCodigo == GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO){
					
					String complemento = "";
					
					switch (inGrupoCodigo) {
					case GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL:
					case GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL:
					case GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL:
						complemento = "Procurador";
						break;
					case GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA:
						complemento = "Defensor";
						break;
					case GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO:
						complemento = "Advogado";
						break;
					default:
						break;
					}
					
					MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
					movimentacaoDt.setId_Processo(processoDt.getId());
					movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumero());
					movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.TROCAR_RESPONSAVEL_PROCESSO));
					movimentacaoDt.setMovimentacaoTipo("Troca de Respons�vel");
					movimentacaoDt.setComplemento(complemento+" Respons�vel Anterior: "+  new UsuarioNe().consultarNomeUsuario(id_UsuarioServentiaAtual, obFabricaConexao)+ " <br> "
														+complemento+" Respons�vel Atual: "+ new UsuarioNe().consultarNomeUsuario(id_UsuarioServentiaNovo, obFabricaConexao));
					movimentacaoDt.setId_UsuarioRealizador(id_Usu_Serv_Realizador);
					movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
					movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());
					movimentacaoNe.salvar(movimentacaoDt, obFabricaConexao);
				}
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * 
	 * @param id_UsuarioServentiaNovoProcurador
	 * @param id_UsuarioServentiaAtualProcurador
	 * @param grupoCodigo
	 * @param id_Processo
	 * @param logDt
	 * @throws Exception
	 * @author asrocha
	 */
	 
	public void salvarTrocaResponsavelProcuradorProcesso(String id_UsuarioServentiaNovo, String id_UsuarioServentiaAtual, String grupoCodigo, String id_Processo, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    ProcessoParteAdvogadoNe processoParteAdvogadoNe = new ProcessoParteAdvogadoNe();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			//Altera procruador respons�vel no processo
			processoParteAdvogadoNe.atualizarProcessoParteAdvogado(id_Processo, id_UsuarioServentiaNovo, id_UsuarioServentiaAtual, logDt, obFabricaConexao);

			//Altera tamb�m o respons�vel das pend�ncias vinculadas ao procurador
			PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
			responsavelNe.atualizarResponsavelPendenciaIntimacaoCitacao(id_Processo, id_UsuarioServentiaAtual, id_UsuarioServentiaNovo, grupoCodigo, logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva Troca de Respons�vel de um processo
	 * 
	 * @param id_ServentiaCargo, novo respons�vel pelo processo
	 * @param id_Processo, processo que ter� respons�vel modificado
	 * @param grupoTipoCodigo, define qual respons�vel deve ser modificado
	 * @param logDt, objeto com dados do log
	 * @author msapaula
	 * @throws Exception 
	 */
	public void salvarTrocaResponsavel(String id_ServentiaCargo, String id_Processo, String grupoTipoCodigo, LogDt logDt, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception{
		List listaProcessos = new ArrayList();
		if (id_Processo != null && id_Processo.length() > 0) {
			//Consulta dados b�sicos do processo e adiciona � lista
			ProcessoDt obj = this.consultarProcessoId(id_Processo);
			obj.setServentiaCargoResponsavelDt(this.consultarResponsavelProcesso(obj.getId(), grupoTipoCodigo, serventiaTipoCodigo, serventiaSubTipoCodigo));
			listaProcessos.add(obj);
		}

		ServentiaCargoDt serventiaCargo = new ServentiaCargoNe().consultarId(id_ServentiaCargo);

		this.AtualizarTrocaResponsavel(id_ServentiaCargo, listaProcessos, serventiaCargo.getCargoTipoCodigo(), logDt, false);
	}

	/**
	 * M�todo respons�vel em desativar os respons�veis de um processo, em virtude de uma redistribui��o ou retorno � origem (Rec.Inominado).
	 * 
	 * @param processoDt - processo que est� sendo redistribu�do
	 * @param conexao conex�o ativa
	 * 
	 * @author hmgodinho
	 */
	public void desativarResponsaveisProcesso(ProcessoDt processoDt, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com serventia atual do processo
		List relacionadasAnteriores = serventiaRelacionadaNe.consultarServentiasRel(processoDt.getId_Serventia());

		//Compara quais serventias relacionadas s�o diferentes para desativar nessas os respons�veis
		List serventiasDesativar = new ArrayList();
		
		//J� inicia com a serventia atual do processo
		serventiasDesativar.add(processoDt.getId_Serventia()); 
		
		for (int i = 0; i < relacionadasAnteriores.size(); i++) {
			ServentiaDt dt1 = (ServentiaDt) relacionadasAnteriores.get(i);
			serventiasDesativar.add(dt1.getId());
		}
		obPersistencia.desativarResponsaveisProcesso(processoDt.getId(), serventiasDesativar);
		
	}
	
	/**
	 * M�todo respons�vel por desativar os servidores do gabinete respons�veis por um processo.
	 * 
	 * @param idProcesso, identifica��o de processo
	 * @param idServentia, identifica��o do gabinete
	 * @param obFabricaConexao conex�o ativa
	 * 
	 * @author hmgodinho
	 */
	public void desativarGabineteResponsavelProcesso(String idProcesso, String idServentia, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		obPersistencia.desativarGabineteResponsavelProcesso(idProcesso, idServentia);
	}

	/**
	 * M�todo respons�vel por reativar os servidores do gabinete respons�veis por um processo.
	 * 
	 * @param idProcesso, identifica��o de processo
	 * @param idServentia, identifica��o do gabinete
	 * @param obFabricaConexao conex�o ativa
	 * 
	 * @author hmgodinho
	 */
	public void ativarGabineteResponsavelProcesso(String idProcesso, String idServentia, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		obPersistencia.ativarGabineteResponsavelProcesso(idProcesso, idServentia);
	}
	
	/**
	 * M�todo respons�vel em desativar os respons�veis de um processo, em virtude de uma redistribui��o ou retorno � origem (Rec.Inominado).
	 * Deve verificar antes de desativar se a nova serventia tem outras serventias relacionadas iguais
	 * a serventia atual, n�o necessitando nesse caso desativar os respons�veis.
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_NovaServentia, identifica��o da serventia para a qual o processo ser� redistribu�do
	 * @param conexao conex�o ativa
	 * 
	 * @author lsbernardes
	 */
	public void desativarResponsaveisProcessoRedistribuicaoLote(ProcessoDt processoDt, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com serventia atual do processo
		List relacionadas = serventiaRelacionadaNe.consultarServentiasRel(processoDt.getId_Serventia());
		List serventiasDesativar = new ArrayList();
		serventiasDesativar.add(processoDt.getId_Serventia()); //J� inicia com a serventia atual do processo
		for (int i = 0; i < relacionadas.size(); i++) {
			ServentiaDt dt1 = (ServentiaDt) relacionadas.get(i);
			serventiasDesativar.add(dt1.getId());
		}
		
		obPersistencia.desativarResponsaveisProcesso(processoDt.getId(), serventiasDesativar);
		
	}

	/**
	 * M�todo respons�vel em ativar os respons�veis de um processo, em virtude da subida de um Recurso Inominado que j� esteve no Segundo
	 * Grau anteriormente, portanto, os antigos respons�veis est�o desativados, devendo ser ativados.
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_NovaServentia, identifica��o da serventia para a qual o processo ser� distribu�do
	 * @param conexao conex�o ativa
	 * 
	 * @author msapaula
	 */
	public int  ativarResponsaveisProcesso(String id_Processo, String id_NovaServentia, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com a nova serventia do processo
		List relacionadasNovas = serventiaRelacionadaNe.consultarServentiasRel(id_NovaServentia);

		//Monta String com as serventias onde os respons�veis deve ser ativado
		List serventiasAtivar = new ArrayList();
		serventiasAtivar.add(id_NovaServentia);  //J� inicia com a serventia atual do processo
		for (int j = 0; j < relacionadasNovas.size(); j++) {
			ServentiaDt dt2 = (ServentiaDt) relacionadasNovas.get(j);
			serventiasAtivar.add(dt2.getId());
		}

		return obPersistencia.ativarResponsaveisProcesso(id_Processo, serventiasAtivar);			
		
	}
	
	/**
	 * M�todo respons�vel por reativar os respons�veis de um determinado processo de todas as serventias relacionadas � serventia destino do processo, 
	 * EXCETO desembargadores que ser�o ativados separadamente.
	 * @param id_Processo - ID do processo
	 * @param id_NovaServentia - ID da serventia de destino
	 * @param obFabricaConexao - conex�o
	 * @return qtde de registros alterados
	 * @throws Exception
	 */
	public int reativarResponsaveisProcesso(String id_Processo, String id_NovaServentia, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com a nova serventia do processo
		List relacionadasNovas = serventiaRelacionadaNe.consultarServentiasRel(id_NovaServentia);

		//Monta String com as serventias onde os respons�veis deve ser ativado
		List serventiasAtivar = new ArrayList();
		serventiasAtivar.add(id_NovaServentia);  //J� inicia com a serventia atual do processo
		for (int j = 0; j < relacionadasNovas.size(); j++) {
			ServentiaDt dt2 = (ServentiaDt) relacionadasNovas.get(j);
			serventiasAtivar.add(dt2.getId());
		}

		return obPersistencia.reativarResponsaveisProcesso(id_Processo, serventiasAtivar);			
		
	}
	
	/**
	 * M�todo respons�vel por reativar os respons�veis de um determinado processo de todas as serventias relacionadas � serventia destino do processo, 
	 * EXCETO desembargadores que ser�o ativados separadamente.
	 * @param id_Processo - ID do processo
	 * @param id_NovaServentia - ID da serventia de destino
	 * @param obFabricaConexao - conex�o
	 * @return qtde de registros alterados
	 * @throws Exception
	 */
	public int reativarResponsaveisProcessoExcetoServentia(String id_Processo, String id_NovaServentia, String idServentiaExcecao, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com a nova serventia do processo
		List relacionadasNovas = serventiaRelacionadaNe.consultarServentiasRel(id_NovaServentia);

		//Monta String com as serventias onde os respons�veis deve ser ativado
		List serventiasAtivar = new ArrayList();
		serventiasAtivar.add(id_NovaServentia);  //J� inicia com a serventia atual do processo
		for (int j = 0; j < relacionadasNovas.size(); j++) {
			ServentiaDt dt2 = (ServentiaDt) relacionadasNovas.get(j);
			
			// Se n�o se tratar da serventia de exce��o, adiciona na lista
			if(idServentiaExcecao == null) {
				serventiasAtivar.add(dt2.getId());
			} else {
				if(!idServentiaExcecao.equals(dt2.getId())) {
					serventiasAtivar.add(dt2.getId());
				}
			}

		}

		return obPersistencia.reativarResponsaveisProcesso(id_Processo, serventiasAtivar);			
		
	}
	
	/**
	 * M�todo respons�vel por reativar o desembargador respons�vel por um determinado processo.
	 * @param id_Processo - ID do processo
	 * @param id_NovaServentia - ID da serventia de destino
	 * @param obFabricaConexao - conex�o
	 * @return qtde de registros alterados
	 * @throws Exception
	 */
	public int reativarMagistradoResposavelProcesso(String id_Processo, String id_serventiaCargo_novo, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		return obPersistencia.reativarMagistradoResposavelProcesso(id_Processo, id_serventiaCargo_novo);			
		
	}
	
	/**
	 * M�todo respons�vel em ativar os respons�veis de um processo. Parte de uma serventia (id_NovaServentia) e
	 * percorre tamb�m as serventias relacionadas � ela. � poss�vel especificar uma serventia de exce��o para que
	 * os respons�veis que sejam dela n�o sejam ativados.
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_NovaServentia, identifica��o da serventia para a qual o processo ser� distribu�do
	 * @param idServentiaExcecao, identifica��o da serventia que n�o ter� os respons�veis ativados
	 * @param conexao conex�o ativa
	 * 
	 * @author hrrosa
	 */
	public void ativarResponsaveisProcessoExcetoServentia(String idProcesso, String idNovaServentia, String idServentiaExcecao, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com a nova serventia do processo
		List relacionadasNovas = serventiaRelacionadaNe.consultarServentiasRel(idNovaServentia);

		//Monta String com as serventias onde os respons�veis deve ser ativado
		List<String> serventiasAtivar = new ArrayList<String>();
		serventiasAtivar.add(idNovaServentia);  //J� inicia com a serventia atual do processo
		for (int j = 0; j < relacionadasNovas.size(); j++) {
			ServentiaDt dt2 = (ServentiaDt) relacionadasNovas.get(j);
			
			// Se n�o se tratar da serventia de exce��o, adiciona na lista
			// Se n�o se tratar da serventia de exce��o, adiciona na lista
			if(idServentiaExcecao == null) {
				serventiasAtivar.add(dt2.getId());
			} else {
				if(!idServentiaExcecao.equals(dt2.getId())) {
					serventiasAtivar.add(dt2.getId());
				}
			}
			
		}

		obPersistencia.ativarResponsaveisProcesso(idProcesso, serventiasAtivar);
	}
	
	/**
	 * M�todo respons�vel em ativar os redator de um processo, em virtude da subida de um Recurso Inominado que j� esteve no Segundo
	 * Grau anteriormente, portanto, os antigos respons�veis est�o desativados, devendo ser ativados. . � poss�vel especificar uma
	 * serventia de exce��o para que os respons�veis que sejam dela n�o sejam ativados
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_NovaServentia, identifica��o da serventia para a qual o processo ser� distribu�do
	 * @param String idServentiaExcecao, identifica��o da serventia que n�o ter� os redatores ativados 
	 * @param conexao conex�o ativa
	 * 
	 * @author hrrosa
	 */
	public void ativarRedatorProcessoExcetoServentia(String id_Processo, String id_NovaServentia, String idServentiaExcecao, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com a nova serventia do processo
		List relacionadasNovas = serventiaRelacionadaNe.consultarServentiasRel(id_NovaServentia);

		//Monta String com as serventias onde os respons�veis deve ser ativado
		List serventiasAtivar = new ArrayList();
		serventiasAtivar.add(id_NovaServentia);  //J� inicia com a serventia atual do processo
		for (int j = 0; j < relacionadasNovas.size(); j++) {
			ServentiaDt dt2 = (ServentiaDt) relacionadasNovas.get(j);
			
			// Se n�o se tratar da serventia de exce��o, adiciona na lista
			// Se n�o se tratar da serventia de exce��o, adiciona na lista
			if(idServentiaExcecao == null) {
				serventiasAtivar.add(dt2.getId());
			} else {
				if(!idServentiaExcecao.equals(dt2.getId())) {
					serventiasAtivar.add(dt2.getId());
				}
			}
		}

		obPersistencia.ativarRedatorProcesso(id_Processo, serventiasAtivar);
		
	}
	
	/**
	 * M�todo respons�vel em ativar os redator de um processo, em virtude da subida de um Recurso Inominado que j� esteve no Segundo
	 * Grau anteriormente, portanto, os antigos respons�veis est�o desativados, devendo ser ativados.
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_NovaServentia, identifica��o da serventia para a qual o processo ser� distribu�do
	 * @param conexao conex�o ativa
	 * 
	 * @author lsbernardes
	 */
	public void ativarRedatorProcesso(String id_Processo, String id_NovaServentia, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

		//Consulta serventias relacionadas com a nova serventia do processo
		List relacionadasNovas = serventiaRelacionadaNe.consultarServentiasRel(id_NovaServentia);

		//Monta String com as serventias onde os respons�veis deve ser ativado
		List serventiasAtivar = new ArrayList();
		serventiasAtivar.add(id_NovaServentia);  //J� inicia com a serventia atual do processo
		for (int j = 0; j < relacionadasNovas.size(); j++) {
			ServentiaDt dt2 = (ServentiaDt) relacionadasNovas.get(j);
			serventiasAtivar.add(dt2.getId());
		}

		obPersistencia.ativarRedatorProcesso(id_Processo, serventiasAtivar);
		
	}
	
	/**
	 * M�todo respons�vel por retirar um redator do processo
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param conexao conex�o ativa
	 * 
	 * @author lsbernardes
	 */
	public void desativarRedatorProcesso(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		obPersistencia.desativarRedatorProcesso(id_Processo);

	}

	/**
	 * Atualiza os dados de um ProcessoResponsavel em virtude de uma troca de respons�vel pelo processo
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param id_ResponsavelAnterior, respons�vel anterior
	 * @param id_NovoResponsavel, novo respons�vel
	 * @param logDt
	 * @param conexao
	 * @author msapaula
	 */
	public void alterarResponsavelProcesso(String id_Processo, String id_ResponsavelAnterior, String id_NovoResponsavel, LogDt logDt, FabricaConexao obFabricaConexao, String id_ProcessoResponsavel) throws Exception {
		LogDt obLogDt;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_Processo:" + id_Processo + ";Id_ServentiaCargo:" + id_ResponsavelAnterior + "]";
		String valorNovo = "[Id_Processo:" + id_Processo + ";Id_ServentiaCargo:" + id_NovoResponsavel + "]";

		obLogDt = new LogDt("ProcessoResponsavel", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelProcesso), valorAtual, valorNovo);
		if(id_ProcessoResponsavel != null && id_ProcessoResponsavel.trim().length() > 0){
			obPersistencia.alterarResponsavelProcesso(id_ProcessoResponsavel, id_NovoResponsavel);
		} else {
			obPersistencia.alterarResponsavelProcesso(id_Processo, id_ResponsavelAnterior, id_NovoResponsavel);
		}

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	/**
	 * Chama m�todo que realizar� a consulta
	 */
	public List consultarServentiaCargos(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		tempList = ServentiaCargone.consultarServentiaCargos(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
		ServentiaCargone = null;
		
		return tempList;
	}
	
	public String consultarServentiaCargosJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		String stTemp = "";
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		stTemp = ServentiaCargone.consultarServentiaCargosJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		ServentiaCargone = null;
		return stTemp;
	}
	
	/**
	 * Chama m�todo que realizar� a consulta
	 */
	public List consultarUsuariosServentia(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List tempList = null;
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		tempList = usuarioServentiaNe.consultarUsuariosServentia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = usuarioServentiaNe.getQuantidadePaginas();
		usuarioServentiaNe = null;

		return tempList;
	}

	/**
	 * Chama m�todo que realizar� a consulta
	 * @throws Exception 
	 */
	public ProcessoDt consultarProcessoId(String id_Processo) throws Exception {
		ProcessoDt obTemp = null;
		
		ProcessoNe processoNe = new ProcessoNe();
		obTemp = processoNe.consultarId(id_Processo);
		processoNe = null;
		
		return obTemp;
	}
	
	/**
	 * A condi��o adicionada no m�todo consultarId() - condi��o que trata sobre recursos - impedia que processos
	 * com recursos n�o autuados aparecessem na consulta, ocasionando erro na troca de MP respons�vel pelo
	 * processo. Como o coordenador de MP deve poder trocar o MP respons�vel independente de recurso
	 * autuado ou n�o, foi criado o m�todo abaixo que n�o usa o crit�rio novo.  
	 * 
	 * @param id_processo
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoIdTrocaResponsavel(String id_Processo) throws Exception {
		ProcessoDt obTemp = null;
		
		ProcessoNe processoNe = new ProcessoNe();
		obTemp = processoNe.consultarProcessoIdTrocaResponsavel(id_Processo);
		processoNe = null;
		
		return obTemp;
	}
	
	public String redirecionarTelaAssistenteUPJ(ProcessoDt processoDt, UsuarioNe usuarioSessao) throws Exception{
		String link = "";
		List listaPendencias = (List) new PendenciaNe().consultarConclusoesPendentesProcessoHash(processoDt.getId_Processo(), usuarioSessao);
		if(listaPendencias == null) {
			throw new MensagemException("O processo " + processoDt.getProcessoNumeroCompleto() + " n�o est� concluso.");
		} else {
			String idPendencia = null, hashPendencia = null;
			for (int i = 0; i < listaPendencias.size(); i++) {
				String[] pendenciaDt = (String[]) listaPendencias.get(i);
				String valores = pendenciaDt[0];
				String[] idHash = valores.split("@#!@");
				idPendencia = idHash[0];
				hashPendencia = idHash[1];
			}
			link = "PendenciaResponsavel?PaginaAtual=" + Configuracao.Novo + "&pendencia=" + idPendencia + "&CodigoPendencia=" + hashPendencia;
		}
		return link;
	}
	
	/**
	 * M�todo que verifica se a troca de respons�vel pode ser realizada para o processo passado
	 * 
	 * @param processoDt, processo que ter� respons�vel trocado
	 * @param usuarioDt, usu�rio que est� trocando o respons�vel pelo processo
	 * @param processoResponsavelNe, processoResponsavelNe para verificar se o usu�rio � respons�vel
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String podeTrocarResponsavel(ProcessoDt processoDt, UsuarioDt usuarioDt, ProcessoResponsavelNe processoResponsavelNe) throws Exception{
		String stMensagem = "";
		// Se usu�rio for de serventia diferente do processo e se n�o for responsavel pelo processo, n�o poder� trocar respons�vel
		if ((!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) 
				&& (!processoResponsavelNe.isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId_Processo()))) {
			stMensagem += "Sem permiss�o para trocar respons�vel em processo de outra serventia.";
		}

		return stMensagem;
	}
	
	public String podeTrocarPromotorResponsavel(ProcessoDt processoDt, UsuarioDt usuarioDt, ProcessoResponsavelNe processoResponsavelNe) throws Exception{
		String stMensagem = "";
		// Se usu�rio for de serventia diferente do processo e se n�o for responsavel pelo processo, n�o poder� trocar respons�vel
		if ((!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) 
				&& (!processoResponsavelNe.isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId_Processo()))
				&& (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.COORDENADOR_PROMOTORIA)) {
			stMensagem += "Sem permiss�o para trocar respons�vel em processo de outra serventia.";
		}

		return stMensagem;
	}
	
	public String podeTrocarResponsavelProcessoParte(ProcessoDt processoDt, UsuarioDt usuarioDt, ProcessoResponsavelNe processoResponsavelNe) throws Exception {
		String stMensagem = "";
		ProcessoParteAdvogadoNe processoParteAdvogadoNe = new ProcessoParteAdvogadoNe();
		// Se usu�rio for de serventia diferente do processo e se n�o for responsavel pelo processo parte, n�o poder� trocar respons�vel
		if ((!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) 
				&& (!processoParteAdvogadoNe.isAdvogadoProcesso(usuarioDt.getId_UsuarioServentia(), processoDt.getId_Processo()))
				&&  !usuarioDt.isCoordenadorJuridico()) {
			stMensagem += "Sem permiss�o para trocar respons�vel em processo de outra serventia.";
		}

		return stMensagem;
	}
	
	/**
	 * Retorna as serventias com base na descri��o, p�gina atual, Tipo da Serventia e SubTipo da Serventia
	 * @param descricao
	 * @param posicao
	 * @param serventiaTipoCodigo
	 * @param serventiaSubTipoCodigo
	 * 
	 *  @author M�rcio Gomes
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		List tempList=null;
		ServentiaNe Serventiane = new ServentiaNe(); 
		tempList = Serventiane.consultarServentiasAtivas(descricao,  posicao, serventiaTipoCodigo, null, serventiaSubTipoCodigo);
		QuantidadePaginas = Serventiane.getQuantidadePaginas();
		Serventiane = null;
		return tempList;
	}	
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		String stTemp = "";
		
		ServentiaNe ServentiaNe = new ServentiaNe(); 
		stTemp = ServentiaNe.consultarServentiasAtivasJSON(descricao,  posicao, serventiaTipoCodigo, null, serventiaSubTipoCodigo);
		
		return stTemp;
	}
	
	/**
	 * Consulta dados b�sicos de uma serventia pelo Id passado
	 * @param id_serventia, identifica��o da serventia
	 * 
	 * @author M�rcio Gomes
	 */
	public ServentiaDt consultarServentiaIdSimples(String id_serventia) throws Exception {
		ServentiaDt dtRetorno = null;
		ServentiaNe Serventiane = new ServentiaNe(); 
		dtRetorno = Serventiane.consultarIdSimples(id_serventia);			
		Serventiane = null;
		return dtRetorno;		
	}
	
	/**
	 * Retorna uma lista de ServentiaCargo do tipo Promotor respons�veis pelo processo.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param id_Serventia identifica��o da serventia
	 * @param conexao conex�o ativa 
	 * @author M�rcio Gomes
	 */
	public List getListaPromotoresResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao conexao) throws Exception {
		return this.consultarListaServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, CargoTipoDt.MINISTERIO_PUBLICO , true, conexao);
	}
	
	/**
	 * Retorna uma lista de ServentiaCargo respons�vel por um processo, de um determinado tipo e em uma determinada serventia.
	 * OBS.: Deve ser retornado uma lista de ServentiaCargo respons�vel que est�o ativos
	 * 
	 * @param processoDt identifica��o do processo
	 * @param id_serventia identifica��o da serventia
	 * @param cargoTipoCodigo tipo do respons�vel a ser retornado
	 * @author M�rcio Gomes
	 */
	public List consultarListaServentiaCargoResponsavelProcesso(String id_Processo, String id_Serventia, int cargoTipoCodigo, boolean boAtivo, FabricaConexao conexao) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			return obPersistencia.consultarListaServentiaCargoResponsavelProcesso(id_Processo, id_Serventia, cargoTipoCodigo, boAtivo);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * Retorna uma lista de ServentiaCargo respons�vel por um processo, de um determinado tipo e em uma determinada serventia.
	 * OBS.: Deve ser retornado uma lista de ServentiaCargo respons�vel que est�o ativos
	 * 
	 * @param processoDt identifica��o do processo
	 * @param id_serventia identifica��o da serventia
	 * @param cargoTipoCodigo tipo do respons�vel a ser retornado
	 */
	public List consultarListaPromotoresProcesso(String id_Processo, String id_Serventia, String serventiaSubTipoCodigo, int cargoTipoCodigo, boolean boAtivo, FabricaConexao conexao) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			return obPersistencia.consultarListaPromotoresProcesso(id_Processo, id_Serventia, serventiaSubTipoCodigo, cargoTipoCodigo, boAtivo);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}		
	}
	
	 /**
     * Retorna a serventia relacionada do tipo Promotoria
     * 
     * @param id_Serventia
     * 
     * @author mmgomes
     */
	public ServentiaDt consultarPromotoriaRelacionada(String id_Serventia) throws Exception {       
                	
        ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        return serventiaRelacionadaNe.consultarPromotoriaRelacionada(id_Serventia);        	
		
	}
	
	/**
	 * Consulta o revisor respons�vel pelo processo de segundo grau
	 * 
	 * @param id_Processo = identifica��o do processo
	 * @author mmgomes
	 */
	public ServentiaCargoDt consultarRevisorResponsavelProcessoSegundoGrau(String id_Processo) throws Exception {
		ServentiaCargoDt serventiaCargo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			serventiaCargo = obPersistencia.consultarRevisorResponsavelProcessoSegundoGrau(id_Processo);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaCargo;		
	}
	
	/**
	 * Consulta o vogal respons�vel pelo processo de segundo grau
	 * 
	 * @param id_Processo = identifica��o do processo
	 * @author mmgomes
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarVogalResponsavelProcessoSegundoGrau(String id_Processo) throws Exception{		
		ServentiaCargoDt serventiaCargo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			serventiaCargo = obPersistencia.consultarVogalResponsavelProcessoSegundoGrau(id_Processo);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaCargo;		
	}
	
	/***
	 * Verifica dados obrigat�rios na troca de desembargadores respons�veis do processo de segundo grau.
	 * @param dados
	 * @param processoResponsaveldtRevisor
	 * @param processoResponsaveldtVogal
	 * @return
	 * @author mmgomes
	 * @throws Exception
	 */
	public String verificarTrocaDesembargadoresResponsaveis(ProcessoResponsavelDt dados, ProcessoResponsavelDt processoResponsaveldtRevisor, ProcessoResponsavelDt processoResponsaveldtVogal){
			
		if (dados.getListaProcessos() == null || dados.getListaProcessos().size() == 0) return "Nenhum Processo foi selecionado. \n";
		
		String stRetorno = "";
		
		if (dados.getId_ServentiaCargo().length() == 0 && dados.getId_ServentiaCargo().length() == 0) stRetorno = "Selecione o Novo Relator Respons�vel pelo Processo. \n";			
		if (processoResponsaveldtRevisor.getId_ServentiaCargo().length() == 0 && processoResponsaveldtRevisor.getId_ServentiaCargo().length() == 0 && processoResponsaveldtVogal.getId_ServentiaCargo().length() > 0 && processoResponsaveldtVogal.getId_ServentiaCargo().length() > 0) stRetorno += "Selecione o Novo Revisor Respons�vel pelo Processo. \n";
				
		if (stRetorno.length() > 0) return stRetorno;
		
		if (dados.getId_ServentiaCargo().length() > 0 && processoResponsaveldtRevisor.getId_ServentiaCargo().length() > 0 && dados.getId_ServentiaCargo().trim().equalsIgnoreCase(processoResponsaveldtRevisor.getId_ServentiaCargo().trim())) stRetorno += "O Novo Relator n�o pode ser igual ao Novo Revisor. \n";
		if (dados.getId_ServentiaCargo().length() > 0 && processoResponsaveldtVogal.getId_ServentiaCargo().length() > 0 && dados.getId_ServentiaCargo().trim().equalsIgnoreCase(processoResponsaveldtVogal.getId_ServentiaCargo().trim())) stRetorno += "O Novo Relator n�o pode ser igual ao Novo Vogal. \n";
		if (processoResponsaveldtRevisor.getId_ServentiaCargo().length() > 0 && processoResponsaveldtVogal.getId_ServentiaCargo().length() > 0 && processoResponsaveldtRevisor.getId_ServentiaCargo().trim().equalsIgnoreCase(processoResponsaveldtVogal.getId_ServentiaCargo().trim())) stRetorno += "O Novo Revisor n�o pode ser igual ao Novo Vogal. \n";
		
		if (stRetorno.length() > 0) return stRetorno;
		
		List processos = dados.getListaProcessos();
		boolean novoRelatorEhIgualAnterior;
		boolean novoRevisorEhIgualAnterior;
		boolean novoVogalEhIgualAnterior;
		for (int i = 0; i < processos.size(); i++) {
			ProcessoDt processoDt = (ProcessoDt) processos.get(i);
			// Novo respons�vel n�o pode ser o mesmo que o atual
			novoRelatorEhIgualAnterior = (processoDt.getServentiaCargoResponsavelDt() != null && processoDt.getServentiaCargoResponsavelDt().getId().equals(dados.getId_ServentiaCargo()));
			novoRevisorEhIgualAnterior = ((processoDt.getServentiaCargoRevisorResponsavelDt() != null && processoDt.getServentiaCargoRevisorResponsavelDt().getId().equals(processoResponsaveldtRevisor.getId_ServentiaCargo())) || (processoDt.getServentiaCargoRevisorResponsavelDt() == null && processoResponsaveldtRevisor.getId_ServentiaCargo().length() == 0));
			novoVogalEhIgualAnterior = ((processoDt.getServentiaCargoVogalResponsavelDt() != null && processoDt.getServentiaCargoVogalResponsavelDt().getId().equals(processoResponsaveldtVogal.getId_ServentiaCargo())) || (processoDt.getServentiaCargoVogalResponsavelDt() == null && processoResponsaveldtVogal.getId_ServentiaCargo().length() == 0));
			
			if (novoRelatorEhIgualAnterior && novoRevisorEhIgualAnterior && novoVogalEhIgualAnterior) stRetorno += "N�o houve altera��o dos respons�veis pelo Processo " + processoDt.getProcessoNumero() + ", favor verificar. \n";				  
		}	
				
		return stRetorno;
	}
	
	/**
	 * M�todo que faz a valida��o da altera��o de Redator de um processo.
	 * @param processoResponsavelDt - DT vindo do CT com os respons�veis pelo processo
	 * @param idNovoRedator - ID do novo relator selecionado
	 * @return string com mensagem de erro ou string vazia se n�o houver erro
	 * @throws Exception
	 */
	public String verificarTrocaRedatorProcesso(ProcessoResponsavelDt processoResponsavelDt, String idNovoRedator){
		if (processoResponsavelDt.getListaProcessos() == null || processoResponsavelDt.getListaProcessos().size() == 0) return "Nenhum Processo foi selecionado. \n";
		if(processoResponsavelDt != null) {
			ProcessoDt processoDt = (ProcessoDt) processoResponsavelDt.getListaProcessos().get(0);
			//Valida se o Redator foi alterado.
			if(processoDt.getServentiaCargoResponsavelDt().getRedator().equals("1") && processoDt.getServentiaCargoResponsavelDt().getId().equals(idNovoRedator)){
				return "O Redator n�o foi alterado. Para formalizar a altera��o, o mesmo deve ser alterado. \n";
			} if(processoDt.getServentiaCargoRevisorResponsavelDt().getRedator().equals("1") && processoDt.getServentiaCargoRevisorResponsavelDt().getId().equals(idNovoRedator)){
				return "O Redator n�o foi alterado. Para formalizar a altera��o, o mesmo deve ser alterado. \n";
			} if(processoDt.getServentiaCargoVogalResponsavelDt().getRedator().equals("1") && processoDt.getServentiaCargoVogalResponsavelDt().getId().equals(idNovoRedator)){
				return "O Redator n�o foi alterado. Para formalizar a altera��o, o mesmo deve ser alterado. \n";
			}
		}
		return "";
	}
	
	/***
	 * Salva a troca de desembargadores respons�veis pelo processo de segundo grau.
	 * @param id_ServentiaCargoRelator
	 * @param id_ServentiaCargoRevisor
	 * @param id_ServentiaCargoVogal
	 * @param listaProcessos
	 * @param logDt
	 * @throws Exception
	 * 
	 * @author mmgomes
	 */
	public void salvarTrocaDesembargadoresResponsaveis(String id_ServentiaCargoRelator, String id_ServentiaCargoRevisor, String id_ServentiaCargoVogal, List listaProcessos, LogDt logDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt) throws MensagemException, Exception {
	    FabricaConexao obFabricaConexao = null;
	    
	    PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
	    AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
	    PendenciaNe pendenciaNe = new PendenciaNe();
	    MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
	    
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			String complementoMovimentacaoAlteracaoResponsavel = "";

			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) listaProcessos.get(i);
				
				List listaIdProcessoResponsavelRelator = null;
			    List listaPendenciasAbertasProcessoRelator = null;
			    List listaPendenciasProcessoAnaliseRelator = null;
			    PendenciaDt conclusaoRelator = null;
			    List listaPendenciasVotoRelator = null;
			    List listaPendenciasEmentaRelator = null;			    
			    AudienciaProcessoDt audienciaPendenteRelator = null;
			    
			    List listaIdProcessoResponsavelRevisor = null;
			    List listaPendenciasAbertasProcessoRevisor = null;
			    List listaPendenciasProcessoAnaliseRevisor = null;
			    PendenciaDt conclusaoRevisor = null;
			    List listaPendenciasVotoRevisor = null;
			    List listaPendenciasEmentaRevisor = null;
			    AudienciaProcessoDt audienciaPendenteRevisor = null;
			    
			    List listaIdProcessoResponsavelVogal = null;
			    List listaPendenciasAbertasProcessoVogal = null;
			    List listaPendenciasProcessoAnaliseVogal = null;
			    PendenciaDt conclusaoVogal = null;			    
			    List listaPendenciasVotoVogal = null;
			    List listaPendenciasEmentaVogal = null;
			    AudienciaProcessoDt audienciaPendenteVogal = null;
				
				// Obtendo os registros vinculados ao Relator Atual
				if (processoDt.getServentiaCargoResponsavelDt() != null && processoDt.getServentiaCargoResponsavelDt().getId() != null && processoDt.getServentiaCargoResponsavelDt().getId().trim().length() > 0 && !processoDt.getServentiaCargoResponsavelDt().getId().trim().equalsIgnoreCase(id_ServentiaCargoRelator.trim())) {
					listaIdProcessoResponsavelRelator = obPersistencia.consultarIdProcessoResponsavel(processoDt.getServentiaCargoResponsavelDt().getId(), processoDt.getId());
					listaPendenciasAbertasProcessoRelator = pendenciaNe.consultarPendenciasAbertasProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId());
					listaPendenciasProcessoAnaliseRelator = this.consultarPendenciasAnalise(pendenciaNe, processoDt.getServentiaCargoResponsavelDt().getId(), processoDt.getId(), obFabricaConexao);
					conclusaoRelator = pendenciaNe.consultarConclusaoAbertaProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), obFabricaConexao);
					listaPendenciasVotoRelator = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), null, obFabricaConexao);
					listaPendenciasEmentaRelator = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), null, obFabricaConexao);
					audienciaPendenteRelator = audienciaProcessoNe.consultarAudienciaPendenteProcesso(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), processoDt.getId_ProcessoTipo(), obFabricaConexao);
					
					//Na primeira execu��o do for, preencher os dados do relator, revisor e vogal no complemento da movimenta��o
					if(i == 0) {
						ServentiaCargoNe servCargoNe = new ServentiaCargoNe();
						ServentiaCargoDt novoRelatorDt = servCargoNe.consultarId(id_ServentiaCargoRelator);
						complementoMovimentacaoAlteracaoResponsavel += "Novo relator: " + novoRelatorDt.getNomeUsuario();
					}
				}	
				
				// Obtendo os registros vinculados ao Revisor Atual
				if (processoDt.getServentiaCargoRevisorResponsavelDt() != null && processoDt.getServentiaCargoRevisorResponsavelDt().getId() != null && processoDt.getServentiaCargoRevisorResponsavelDt().getId().trim().length() > 0 
						&& id_ServentiaCargoRevisor != null && id_ServentiaCargoRevisor.length()> 0 && !processoDt.getServentiaCargoRevisorResponsavelDt().getId().trim().equalsIgnoreCase(id_ServentiaCargoRevisor.trim())) {
					listaIdProcessoResponsavelRevisor = obPersistencia.consultarIdProcessoResponsavel(processoDt.getServentiaCargoRevisorResponsavelDt().getId(), processoDt.getId());
					listaPendenciasAbertasProcessoRevisor = pendenciaNe.consultarPendenciasAbertasProcesso(processoDt.getId(), processoDt.getServentiaCargoRevisorResponsavelDt().getId());
					listaPendenciasProcessoAnaliseRevisor = this.consultarPendenciasAnalise(pendenciaNe, processoDt.getServentiaCargoRevisorResponsavelDt().getId(), processoDt.getId(), obFabricaConexao);
					conclusaoRevisor = pendenciaNe.consultarConclusaoAbertaProcesso(processoDt.getId(), processoDt.getServentiaCargoRevisorResponsavelDt().getId(), obFabricaConexao);
					listaPendenciasVotoRevisor = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), processoDt.getServentiaCargoRevisorResponsavelDt().getId(), String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), null, obFabricaConexao);
					listaPendenciasEmentaRevisor = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), processoDt.getServentiaCargoRevisorResponsavelDt().getId(), String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), null, obFabricaConexao);
					audienciaPendenteRevisor = audienciaProcessoNe.consultarAudienciaPendenteProcesso(processoDt.getId(), processoDt.getServentiaCargoRevisorResponsavelDt().getId(), processoDt.getId_ProcessoTipo(), obFabricaConexao);
					
					//Na primeira execu��o do for, preencher os dados do relator, revisor e vogal no complemento da movimenta��o
					if(i == 0) {
						if(!complementoMovimentacaoAlteracaoResponsavel.isEmpty()) complementoMovimentacaoAlteracaoResponsavel += " - ";
						ServentiaCargoNe servCargoNe = new ServentiaCargoNe();
						ServentiaCargoDt novoRevisorDt = servCargoNe.consultarId(id_ServentiaCargoRevisor);
						complementoMovimentacaoAlteracaoResponsavel += "Novo revisor: " + novoRevisorDt.getNomeUsuario();
					}
				}
				
				// Obtendo os registros vinculados ao Vogal Atual				
				if (processoDt.getServentiaCargoVogalResponsavelDt() != null && processoDt.getServentiaCargoVogalResponsavelDt().getId() != null && processoDt.getServentiaCargoVogalResponsavelDt().getId().trim().length() > 0 
						&& id_ServentiaCargoVogal != null && id_ServentiaCargoVogal.length()> 0 && !processoDt.getServentiaCargoVogalResponsavelDt().getId().trim().equalsIgnoreCase(id_ServentiaCargoVogal.trim())) {
					listaIdProcessoResponsavelVogal = obPersistencia.consultarIdProcessoResponsavel(processoDt.getServentiaCargoVogalResponsavelDt().getId(), processoDt.getId());
					listaPendenciasAbertasProcessoVogal = pendenciaNe.consultarPendenciasAbertasProcesso(processoDt.getId(), processoDt.getServentiaCargoVogalResponsavelDt().getId());
					listaPendenciasProcessoAnaliseVogal = this.consultarPendenciasAnalise(pendenciaNe, processoDt.getServentiaCargoVogalResponsavelDt().getId(), processoDt.getId(), obFabricaConexao);
					conclusaoVogal = pendenciaNe.consultarConclusaoAbertaProcesso(processoDt.getId(), processoDt.getServentiaCargoVogalResponsavelDt().getId(), obFabricaConexao);
					listaPendenciasVotoVogal = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), processoDt.getServentiaCargoVogalResponsavelDt().getId(), String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), null, obFabricaConexao);
					listaPendenciasEmentaVogal = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), processoDt.getServentiaCargoVogalResponsavelDt().getId(), String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), null, obFabricaConexao);
					audienciaPendenteVogal = audienciaProcessoNe.consultarAudienciaPendenteProcesso(processoDt.getId(), processoDt.getServentiaCargoVogalResponsavelDt().getId(), processoDt.getId_ProcessoTipo(), obFabricaConexao);
				
					//Na primeira execu��o do for, preencher os dados do relator, revisor e vogal no complemento da movimenta��o
					if(i == 0) {
						if(!complementoMovimentacaoAlteracaoResponsavel.isEmpty()) complementoMovimentacaoAlteracaoResponsavel += " - ";
						ServentiaCargoNe servCargoNe = new ServentiaCargoNe();
						ServentiaCargoDt novoVogalDt = servCargoNe.consultarId(id_ServentiaCargoVogal);
						complementoMovimentacaoAlteracaoResponsavel +=  "Novo vogal: " + novoVogalDt.getNomeUsuario();
					}
				}				
				
				// Por quest�es de restri��es no banco de dados que impede a perman�ncia de uma mesma serventia cargo em mais de um cargo respons�vel pelo processo, temos que limpar todos os respons�veis antes de realizar a altera��o. 
				// Limpar o relator atual
				this.limparResponsavelProcesso(listaIdProcessoResponsavelRelator, obFabricaConexao);				
				// Limpar o revisor atual
				this.limparResponsavelProcesso(listaIdProcessoResponsavelRevisor, obFabricaConexao);
				// Limpar o vogal atual
				this.limparResponsavelProcesso(listaIdProcessoResponsavelVogal, obFabricaConexao);
				
				// Alterando o relator atual			
				this.salvarTrocaResponsavel(processoDt, id_ServentiaCargoRelator, processoDt.getServentiaCargoResponsavelDt(), CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, responsavelNe, audienciaProcessoNe, listaIdProcessoResponsavelRelator, listaPendenciasAbertasProcessoRelator, listaPendenciasProcessoAnaliseRelator, conclusaoRelator, listaPendenciasVotoRelator, listaPendenciasEmentaRelator, audienciaPendenteRelator, logDt, usuarioDt, obFabricaConexao);				
				// Alterando o revisor atual
				if (id_ServentiaCargoRevisor != null && id_ServentiaCargoRevisor.trim().length() > 0)
					this.salvarTrocaResponsavel(processoDt, id_ServentiaCargoRevisor, processoDt.getServentiaCargoRevisorResponsavelDt(), CargoTipoDt.REVISOR_SEGUNDO_GRAU, responsavelNe, audienciaProcessoNe, listaIdProcessoResponsavelRevisor, listaPendenciasAbertasProcessoRevisor, listaPendenciasProcessoAnaliseRevisor, conclusaoRevisor, listaPendenciasVotoRevisor, listaPendenciasEmentaRevisor, audienciaPendenteRevisor, logDt, usuarioDt, obFabricaConexao);
				else this.inativarResponsavelProcesso(listaIdProcessoResponsavelRevisor, obFabricaConexao);
				// Alterando o vogal atual
				if (id_ServentiaCargoVogal != null && id_ServentiaCargoVogal.trim().length() > 0)
					this.salvarTrocaResponsavel(processoDt, id_ServentiaCargoVogal, processoDt.getServentiaCargoVogalResponsavelDt(), CargoTipoDt.VOGAL_SEGUNDO_GRAU, responsavelNe, audienciaProcessoNe, listaIdProcessoResponsavelVogal, listaPendenciasAbertasProcessoVogal, listaPendenciasProcessoAnaliseVogal, conclusaoVogal, listaPendenciasVotoVogal, listaPendenciasEmentaVogal, audienciaPendenteVogal, logDt, usuarioDt, obFabricaConexao);
				else this.inativarResponsavelProcesso(listaIdProcessoResponsavelVogal, obFabricaConexao);
				
			}
			
			if (movimentacaoProcessoDt.getRedirecionaOutraServentia() != null && !movimentacaoProcessoDt.getRedirecionaOutraServentia().equals("")){
				movimentacaoProcessoDt.setComplemento(movimentacaoProcessoDt.getComplemento() + " " + complementoMovimentacaoAlteracaoResponsavel);
				movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		
		
		
			
		} finally{
			responsavelNe = null;
		    audienciaProcessoNe = null;
		    pendenciaNe = null;
			obFabricaConexao.fecharConexao();
		}
	}	
	
	/**
	 * M�todo que faz a troca do redator do processo, desabilitando o atual e habilita o novo redator.
	 * @param idProcesso -  ID do processo
	 * @param idRedatorAtualProcesso - ID do redator atual
	 * @param idNovoRedatorProcesso - ID do novo redator
	 * @param usuarioDt - usu�rio que est� realizando a altera��o
	 * @param movimentacaoProcessoDt - movimenta��o que est� sendo realizada
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void salvarTrocaRedatorProcesso(String idProcesso, String idRedatorAtualProcesso, String idNovoRedatorProcesso, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt) throws MensagemException, Exception {
	    FabricaConexao obFabricaConexao = null;
	    MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
	    
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao()); 
			
			//desabilitando o redator atual do processo
			obPersistencia.alterarStatusRedatorProcesso(idProcesso, idRedatorAtualProcesso, false);
			//habilitando o novo redator do processo
			obPersistencia.alterarStatusRedatorProcesso(idProcesso, idNovoRedatorProcesso, true);

			if (movimentacaoProcessoDt.getRedirecionaOutraServentia() != null && !movimentacaoProcessoDt.getRedirecionaOutraServentia().equals("")) {
				movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		
		
		
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}	
	
	/***
	 * Consulta as pend�ncias que podem ser analisadas (semelhante a conclus�es)
	 * @param pendenciaNe
	 * @param id_ServentiaCargo
	 * @param id_Processo
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * 
	 * @author mmgomes
	 */
	private List consultarPendenciasAnalise(PendenciaNe pendenciaNe, String id_ServentiaCargo, String id_Processo, FabricaConexao obFabricaConexao) throws Exception{				
		List listaPendenciasProcessoAnalise = pendenciaNe.consultarIdPendenciasProcessoNaoAnalisadas(id_ServentiaCargo, id_Processo, obFabricaConexao);
		if (listaPendenciasProcessoAnalise == null) listaPendenciasProcessoAnalise = new ArrayList();
		
		List listaPendenciasProcessoPreAnalisadas = pendenciaNe.consultarIdPendenciasProcessoPreAnalisadas(id_ServentiaCargo, id_Processo, false, obFabricaConexao);
		if (listaPendenciasProcessoPreAnalisadas != null){
			for (int i = 0; i < listaPendenciasProcessoPreAnalisadas.size(); i++) {
				listaPendenciasProcessoAnalise.add(listaPendenciasProcessoPreAnalisadas.get(i));
			}
		}
		
		List listaPendenciasProcessoPreAnalisadasPendentesAssinatura = pendenciaNe.consultarIdPendenciasProcessoPreAnalisadasPendentesAssinatura(id_ServentiaCargo, id_Processo, false, obFabricaConexao);
		if (listaPendenciasProcessoPreAnalisadasPendentesAssinatura != null){
			for (int i = 0; i < listaPendenciasProcessoPreAnalisadasPendentesAssinatura.size(); i++) {
				listaPendenciasProcessoAnalise.add(listaPendenciasProcessoPreAnalisadasPendentesAssinatura.get(i));
			}
		}		
		
		return listaPendenciasProcessoAnalise;	
	}

	/***
	 * Salva a troca de respons�veis pelo processo
	 * @param id_Processo
	 * @param id_ServentiaCargoNovo
	 * @param ServentiaCargoAtual
	 * @param cargoTipoCodigo
	 * @param responsavelNe
	 * @param audienciaProcessoNe
	 * @param listaIdProcessoResponsavel
	 * @param listaPendenciasAbertasProcesso
	 * @param listaPendenciasProcessoAnalise
	 * @param conclusao
	 * @param listaPendenciasVoto
	 * @param listaPendenciasEmenta	 
	 * @param audienciaPendente
	 * @param logDt
	 * @param obFabricaConexao
	 * @throws Exception
	 * 
	 * @author mmgomes 
	 */
	private void salvarTrocaResponsavel(ProcessoDt processoDt, String id_ServentiaCargoNovo, ServentiaCargoDt ServentiaCargoAtual, int cargoTipoCodigo, PendenciaResponsavelNe responsavelNe, AudienciaProcessoNe audienciaProcessoNe, List listaIdProcessoResponsavel, List listaPendenciasAbertasProcesso, List listaPendenciasProcessoAnalise, PendenciaDt conclusao, List listaPendenciasVoto, List listaPendenciasEmenta, AudienciaProcessoDt audienciaPendente, LogDt logDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
		
	    if (ServentiaCargoAtual == null || ServentiaCargoAtual.getId() == null || ServentiaCargoAtual.getId().trim().length() == 0) {
	    	ProcessoResponsavelPs obPersistencia = new  ProcessoResponsavelPs(obFabricaConexao.getConexao());
	    	//Verifica se j� existia um cargo do mesmo tipo como inativo
	    	String id_Proc_Resp = obPersistencia.consultarIdProcessoResponsavelProcessoCargoTipoCodigo(processoDt.getId(), String.valueOf(cargoTipoCodigo));
			
	    	if (id_Proc_Resp != null && id_Proc_Resp.trim().length() > 0){
	    		ProcessoResponsavelDt processoResponsavelDt = new ProcessoResponsavelDt();
	    		
	    		processoResponsavelDt.setId(id_Proc_Resp);
				processoResponsavelDt.setId_Processo(processoDt.getId());
				processoResponsavelDt.setId_ServentiaCargo(id_ServentiaCargoNovo);
				processoResponsavelDt.setCargoTipoCodigo(String.valueOf(cargoTipoCodigo));
				processoResponsavelDt.setId_UsuarioLog(logDt.getId_Usuario());
				processoResponsavelDt.setIpComputadorLog(logDt.getIpComputador());
				processoResponsavelDt.setRedator(false);
				processoResponsavelDt.setCodigoTemp(String.valueOf(ProcessoResponsavelDt.ATIVO));				
				
				salvar(processoResponsavelDt, obFabricaConexao);
				
	    	} else {
	    		//Se n�o tinha um respons�vel deve inserir
	    		this.salvarProcessoResponsavel(processoDt.getId(), id_ServentiaCargoNovo, false,  cargoTipoCodigo, logDt, obFabricaConexao);
	    		
	    		if (cargoTipoCodigo == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU) {
		    		/* ---------- PONTEIRO ----------------*/
					///salvo o ponteiro
					Date dtAgora = new Date();
					new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.GANHO_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, id_ServentiaCargoNovo  ),obFabricaConexao );
	    		}
	    		
	    	}	    		
			
	    } else if(!ServentiaCargoAtual.getId().trim().equalsIgnoreCase(id_ServentiaCargoNovo.trim())) {// A troca ser� realizada somente se o respons�vel anterior for diferente do respons�vel atual.
	    	
	    	if (listaIdProcessoResponsavel != null){
	    		//Altera respons�vel do processo
		    	for (int i = 0; i < listaIdProcessoResponsavel.size(); i++) {
		    		this.alterarResponsavelProcesso(processoDt.getId(), ServentiaCargoAtual.getId(), id_ServentiaCargoNovo, logDt, obFabricaConexao, String.valueOf(listaIdProcessoResponsavel.get(i)));
		    		//reativando todos os respons�veis (assessores), exceto desembargador
		    		ServentiaCargoDt novoRelator = new ServentiaCargoNe().consultarId(id_ServentiaCargoNovo);
					new ProcessoResponsavelNe().reativarResponsaveisProcesso(processoDt.getId(), novoRelator.getId_Serventia(), obFabricaConexao);
		    		
		    		if (cargoTipoCodigo == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU) {
			    		/* ---------- PONTEIRO ----------------*/
						///salvo o ponteiro
						Date dtAgora = new Date();
						new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.GANHO_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, id_ServentiaCargoNovo  ),obFabricaConexao );
						
						new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.PERDA_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, ServentiaCargoAtual.getId()  ),obFabricaConexao );
						/* ---------- PONTEIRO ----------------*/
		    		}
		    	}
	    	}
	    		
	    	
	    	if (listaPendenciasAbertasProcesso != null){
	    		//Altera tamb�m o respons�vel das pend�ncias vinculadas ao cargo
		    	for (int i = 0; i < listaPendenciasAbertasProcesso.size(); i++) {
					PendenciaDt pendenciaDt = (PendenciaDt) listaPendenciasAbertasProcesso.get(i);
					responsavelNe.alterarResponsavelPendencia(pendenciaDt.getId(), ServentiaCargoAtual.getId(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
				}	
	    	}	
	    	
	    	if (listaPendenciasProcessoAnalise != null){
	    		//Altera tamb�m o respons�vel das pend�ncias que podem ser analisadas vinculadas ao cargo
		    	for (int i = 0; i < listaPendenciasProcessoAnalise.size(); i++) {
					String id_Pendencia = (String) listaPendenciasProcessoAnalise.get(i);
					responsavelNe.alterarResponsavelPendencia(id_Pendencia, ServentiaCargoAtual.getId(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
				}	
	    	}
	    	
	    	if (listaPendenciasVoto != null){
	    		//Altera tamb�m o respons�vel das pend�ncias que podem ser analisadas vinculadas ao cargo
		    	for (int i = 0; i < listaPendenciasVoto.size(); i++) {
		    		PendenciaDt PendenciaDt = (PendenciaDt) listaPendenciasVoto.get(i);
					responsavelNe.alterarResponsavelPendencia(PendenciaDt.getId(), ServentiaCargoAtual.getId(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
				}	
	    	}
	    	
	    	if (listaPendenciasEmenta != null){
	    		//Altera tamb�m o respons�vel das pend�ncias que podem ser analisadas vinculadas ao cargo
		    	for (int i = 0; i < listaPendenciasEmenta.size(); i++) {
		    		PendenciaDt PendenciaDt = (PendenciaDt) listaPendenciasEmenta.get(i);
					responsavelNe.alterarResponsavelPendencia(PendenciaDt.getId(), ServentiaCargoAtual.getId(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
				}	
	    	}

			//Altera o respons�vel por conclus�es vinculadas ao cargo
	    	if (conclusao != null) {
	    		responsavelNe.alterarResponsavelPendencia(conclusao.getId(), ServentiaCargoAtual.getId(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
			}
			
			//Se possuir uma audi�ncia ou sess�o de 2� marcada deve trocar o respons�vel por essa tamb�m
	    	if (audienciaPendente != null) {
	    		audienciaProcessoNe.alterarResponsavelAudiencia(audienciaPendente, id_ServentiaCargoNovo, logDt, obFabricaConexao);
			}
	    	
		}    
						
	}
	
	/**
	 * Limpar os Respons�veis de um processo em virtude de uma troca de respons�vel pelo processo
	 * 
	 * @param listaIdProcessoResponsavel, Lista de identifica��o do processo respons�vel 
	 * 
	 * @author mmgomes
	 */
	private void limparResponsavelProcesso(List listaIdProcessoResponsavel, FabricaConexao obFabricaConexao) throws Exception {
		if (listaIdProcessoResponsavel != null){
    		//Altera respons�vel do processo								
			ProcessoResponsavelPs obPersistencia = new  ProcessoResponsavelPs(obFabricaConexao.getConexao());
			for (int i = 0; i < listaIdProcessoResponsavel.size(); i++) {	    		
				obPersistencia.limparResponsavelProcesso(String.valueOf(listaIdProcessoResponsavel.get(i)));
			}				
			
    	}	
	}
	
	/**
	 * Consultar Respons�veis pelo processo no segundo Grau
	 * 
	 * @param idProcesso, Lidentifica��o do processo respons�vel 
	 * 
	 * @author lsbernardes
	 */
	public List consultarResponsaveisProcessoSegundoGrau(String id_Processo, FabricaConexao conexao) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());

			return obPersistencia.consultarResponsaveisProcessoSegundoGrau(id_Processo);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * Limpar os Respons�veis de um processo n�o conhecido
	 * 
	 * @param listaIdProcessoResponsavel, Lista de identifica��o do processo respons�vel 
	 * 
	 * @author lsbernardes
	 */
	public void limparResponsaveisProcessoSegundoGrau(List listaIdProcessoResponsavel, FabricaConexao conexao) throws Exception {
		FabricaConexao obFabricaConexao = null;
	    try{
			if (conexao == null) 
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else 
				obFabricaConexao = conexao;
			
			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			if (listaIdProcessoResponsavel != null){
	    		//Altera respons�vel do processo
		    	for (int i = 0; i < listaIdProcessoResponsavel.size(); i++) {	    		
		    		obPersistencia.limparResponsaveisProcessoSegundoGrau(String.valueOf(listaIdProcessoResponsavel.get(i)));
		    	}
	    	}	

		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * Remove um respons�vel pelo processo
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param id_Responsavel, respons�vel anterior 
	 * @param logDt
	 * @param conexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void removerResponsavelProcesso(String id_Processo, String id_Responsavel, LogDt logDt, FabricaConexao obFabricaConexao, String id_ProcessoResponsavel) throws Exception {
		LogDt obLogDt;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_Processo:" + id_Processo + ";Id_ServentiaCargo:" + id_Responsavel + "]";
		String valorNovo = "[Id_Processo:" + id_Processo + ";Id_ServentiaCargo: Vazio]";

		obLogDt = new LogDt("ProcessoResponsavel", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelProcesso), valorAtual, valorNovo);
		obPersistencia.removeResponsavelProcesso(id_Processo, id_Responsavel);			

		obLog.salvar(obLogDt, obFabricaConexao);
	
	}
	
	/**
	 * Consulta o relator de um processo de segundo grau
	 * 
	 * @param id_Processo
	 * @param obFabricaConexao
	 * @return
	 * @author mmgomes
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarRelatorResponsavelProcessoSegundoGrau(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
					
		return obPersistencia.consultarRelatorResponsavelProcessoSegundoGrau(id_Processo);	
		
	}
	
	/**
	 * Inativa os Respons�veis de um processo 
	 * 
	 * @param listaIdProcessoResponsavel, Lista de identifica��o do processo respons�vel	 
	 * @param obFabricaConexao
	 * 
	 * @author mmgomes
	 */
	private void inativarResponsavelProcesso(List listaIdProcessoResponsavel, FabricaConexao obFabricaConexao) throws Exception {
		if (listaIdProcessoResponsavel != null){
    		//Altera respons�vel do processo			
						
			ProcessoResponsavelPs obPersistencia = new  ProcessoResponsavelPs(obFabricaConexao.getConexao());
			for (int i = 0; i < listaIdProcessoResponsavel.size(); i++) {
				obPersistencia.inativarResponsavelProcesso(String.valueOf(listaIdProcessoResponsavel.get(i)));
			}				

    	}	
	}
	
	/**
	 * M�todo que inativa um respons�vel de um processo.
	 * @param processoResponsavelDt - processo respons�vel que ser� inativado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void inativarResponsavelProcesso(ProcessoResponsavelDt processoResponsavelDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			obPersistencia.inativarResponsavelProcesso(processoResponsavelDt.getId());
		} finally{
			obFabricaConexao.fecharConexao();
		}
			
	}
	
	/**
	 * Consulta o relator de um processo de segundo grau, caso haja substituto ser� retornado o substituto.
	 * 
	 * @param id_Processo
	 * @param id_ServentiaProcesso
	 * @param serventiaSubTipoCodigo
	 * @return
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarRelator2GrauConsideraSubstituicao(String id_Processo, String id_ServentiaProcesso, String serventiaSubTipoCodigo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			return consultarRelator2GrauConsideraSubstituicao(id_Processo, id_ServentiaProcesso, serventiaSubTipoCodigo, obFabricaConexao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta o relator de um processo de segundo grau, caso haja substituto ser� retornado o substituto.
	 * 
	 * @param id_Processo
	 * @param obFabricaConexao
	 * @return
	 * @author mmgomes
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarRelator2GrauConsideraSubstituicao(String id_Processo, String id_ServentiaProcesso, String serventiaSubTipoCodigo,  FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		ServentiaCargoDt serventiaCargo = null;
		ServentiaCargoDt serventiaCargoSubstituto = null;
			
    	// Nesse caso consulta o tipo de ProcessoResponsavel com CargoTipo Relator
		serventiaCargo = this.consultarRelator2Grau(id_Processo, obFabricaConexao); 
		
		if (serventiaCargo == null)
			throw new MensagemException("N�o foi poss�vel localizar o relator do processo. id_Processo = " + id_Processo + ".");
		
		if (id_ServentiaProcesso == null || id_ServentiaProcesso.trim().length() == 0)
			id_ServentiaProcesso = (new ProcessoNe().consultarIdServentiaProcesso(id_Processo, obFabricaConexao));
		
		// Consulta gabinete substituto (se existir) 			
		ServentiaDt serventiaSubstitutaDt = serventiaRelacionadaNe.consultarGabineteSubstitutoAtualSegundoGrau(id_ServentiaProcesso, serventiaCargo.getId_Serventia(), obFabricaConexao); 			
		// testa para verificar se possui gabinete substituto, caso exista consulta o cargo de desembargador ativo (com quantidade de distribui��o) deste gabinete 
		if (serventiaSubstitutaDt != null) {
			ServentiaCargoDt serventiaCargoTitularServentiaSubstituta = serventiaCargoNe.getDesembargadorTitular(serventiaSubstitutaDt.getId(), obFabricaConexao);
			if (serventiaCargoTitularServentiaSubstituta != null) serventiaCargo = serventiaCargoTitularServentiaSubstituta;
		}
		
		if (serventiaSubTipoCodigo == null || serventiaSubTipoCodigo.trim().length() == 0) 			
			serventiaSubTipoCodigo = new ServentiaNe().consultarServentiaSubTipoCodigo(id_ServentiaProcesso, obFabricaConexao);
		
		// Consulta Substituto 2 Grau *********************************************************************************************
		serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(serventiaCargo.getId_Serventia(), serventiaSubTipoCodigo, obFabricaConexao);
		//*************************************************************************************************************************
		
		//testa para verificar se possui substituto********************************************************************************
		if (serventiaCargoSubstituto!= null && serventiaCargoSubstituto.getId() != null && serventiaCargoSubstituto.getId().length()>0){
			return serventiaCargoSubstituto;
		} else {
			return serventiaCargo;
		}
		//******************************************************************************************************	
		
		
	}
	
	/**
	 * Chama m�todo que realizar� a consulta
	 */
	public String consultarUsuariosServentiaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String stTemp = "";
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		stTemp = usuarioServentiaNe.consultarUsuariosServentiaAdvogadosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);			
		usuarioServentiaNe = null;
				
		return stTemp;
	}
	
	/**
	 * Verifica se existe algum magistrado inativo em uma determinada serventia para um determinado processo
	 * 
	 * @param idProc
	 * @param idServ
	 * @param obFabricaConexao
	 * @return boolean
	 * @author hrrosa
	 * @throws Exception
	 */
	public boolean temMagistradoInativoProcessoServentia(String idProc, String idServ) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try 
		{
			ProcessoResponsavelPs procRespPs = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
			
			if( idProc != null && !idProc.isEmpty() && idServ != null && !idServ.isEmpty() ) {
				return procRespPs.temMagistradoInativoProcessoServentia(idProc, idServ);
			} else {
				throw new MensagemException("Um processo e uma serventia devem ser informados.");
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Retorna o respons�vel "Ativo" pelo processo de acordo com o Tipo de Cargo desejado, 
	 * e em uma determinada serventia
	 * 
	 * @param id_Processo, identifica��o de processo
	 * @param id_Serventia, identifica��o da serventia
	 * @param cargoTipoCodigo, determina qual respons�vel deve ser retornado (juiz, relator..)
	 * @param conexao conex�o ativa
	 * 
	 * @author lsbernardes
	 */
	public ServentiaCargoDt consultarServentiaCargo(String id_ServentiaCargo) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		return serventiaCargoNe.consultarId(id_ServentiaCargo);
	}
	
	
	/**
	 * Altera codigoTemp, respons�vel por indicar habilita��o de um respons�vel.
	 * @author lsrodrigues
	 * @throws Exception 
	 */
	public void alterarCodigoTemp(String id_serv_cargo, String id_proc, String codigoTemp) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoResponsavelPs procRespPs = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		
		try {
			procRespPs.alterarCodigoTemp(id_serv_cargo, id_proc, codigoTemp);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * M�todo que habilita e desabilita respons�vel por processo.
	 * @param processoResponsavelDt - dados do respons�vel
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void habilitarDesabilitarResponsavelProcesso(ProcessoResponsavelDt processoResponsavelDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		try{
			obPersistencia.habilitarDesabilitarResponsavelProcesso(processoResponsavelDt.getId(), processoResponsavelDt.getCodigoTemp());
			obLogDt = new LogDt("ProcessoResponsavel", processoResponsavelDt.getId(), processoResponsavelDt.getId_UsuarioLog(), processoResponsavelDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", processoResponsavelDt.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Valida dados obrigat�rios na troca de respons�vel do processo.
	 * @author lsrodrigues
	 * @throws Exception 
	 */
	public String validaTrocaResponsavel(ProcessoResponsavelDt ProcessoResponsaveldt, String idServentiaCargoTemp, List<ServentiaCargoDt> listaPromotoresResponsaveis){
		String stRetorno = "";
		
		if(idServentiaCargoTemp.equals("") || idServentiaCargoTemp.equals("null")){					
			stRetorno = "Escolha um promotor para ser trocado por outro respons�vel. \n";
		}
		
		for(int i = 0; i < listaPromotoresResponsaveis.size(); i++){
			if( listaPromotoresResponsaveis.get(i).getId().equals(ProcessoResponsaveldt.getId_ServentiaCargo()) ){
				stRetorno = "Promotor j� � respons�vel no processo. \n";
			}
		}

		return stRetorno;
	}
	
	

/*	public boolean isPodeBloquear(String id_Processo, String id_ServentiaProcesso) throws Exception{
		int grupoTipo = getUsuarioDt().getGrupoTipoCodigoToInt();
		
		if ( (grupoTipo == GrupoTipoDt.DESEMBARGADOR	|| grupoTipo == GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU)
			&& ((new ProcessoNe()).isResponsavelProcesso(getUsuarioDt().getId_ServentiaCargo(), id_Processo))){*/
	
	/**
	 * Verifica se determinado usu�rio � substituto em um processo, validando o serventiaCargo do usu�rio
	 * @param id_Processo, identifica��o do processo
	 * @param id_serventiaProcesso, identifica��o da serventia do processo
	 * @param grupoCodigo, codigo do grupo do usu�rio
	 * @param id_serventiaCargo, identifica��o do cargo para verificar se o mesmo � substitudo de algum respons�vel
	 * @return boolean
	 * @author lsbernardes
	 */
	public boolean isSubstitutoProcessoSegundoGrau(String id_Processo, String id_serventiaProcesso, String grupoCodigo, String id_serventiaCargo) throws Exception {
		boolean boRetorno = false;
		List tempList = null;
		List listRetorno = new ArrayList();
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		tempList = neObjeto.consultarResponsaveisProcesso(id_Processo, grupoCodigo);
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		ServentiaNe serventiaNe = new ServentiaNe();
		
		if (tempList != null && tempList.size()>0){
			ServentiaDt servProc = serventiaNe.consultarId(id_serventiaProcesso);
			for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
				ServentiaCargoDt responsavelProcesso = (ServentiaCargoDt) iterator.next();
				//altera��o leandro******************************************************************************************************************************************************
				ServentiaCargoDt serventiaCargoSubstituto = null;
				// Consulta gabinete substituto (se existir)
	    		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
	    		ServentiaDt serventiaSubstitutaDt = serventiaRelacionadaNe.consultarGabineteSubstitutoAtualSegundoGrau(id_serventiaProcesso, responsavelProcesso.getId_Serventia()); 			
	    		// testa para verificar se possui gabinete substituto, caso exista consulta o cargo de desembargador ativo (com quantidade de distribui��o) deste gabinete 
	    		ServentiaCargoDt serventiaCargoTitularServentiaSubstituta = null;

	    		if (serventiaSubstitutaDt != null) {
	    			serventiaCargoTitularServentiaSubstituta = serventiaCargoNe.getDesembargadorTitular(serventiaSubstitutaDt.getId(), null);
	    		}
    			
	    		if (serventiaCargoTitularServentiaSubstituta != null)
	    			serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(serventiaCargoTitularServentiaSubstituta.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
	    		else
	    			serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(responsavelProcesso.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
				
	    		if (serventiaCargoSubstituto != null && serventiaCargoSubstituto.getId().equalsIgnoreCase(id_serventiaCargo) ){
	    			return  true;
	    		}
	    		//**************************************************************************************************************************************************************************
			}
		}

		serventiaCargoNe = null;
		serventiaNe = null;
		neObjeto = null;
		return boRetorno;
	}

	public ServentiaCargoDt consultarRelatorSessaoVirtual(String idAudiProc, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoResponsavelPs procRespPs = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		
		if( !StringUtils.isEmpty(idAudiProc) ) {
			return procRespPs.consultarRelatorSessaoVirtual(idAudiProc, obFabricaConexao);
		} else {
			throw new MensagemException("Uma Audiencia Processo deve ser informada.");
		}
	}
	
	/**
	 * Consulta o juiz respons�vel por um gabinete upj
	 * 
	 * @param id_Processo
	 * @param obFabricaConexao
	 * @return
	 * @author lsbernardes
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarJuizUPJ(String id_Processo, String id_ServentiaProcesso, String serventiaSubTipoCodigo,  FabricaConexao obFabricaConexao) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		ServentiaCargoDt serventiaCargo = null;
			
    	// Nesse caso consulta o tipo de ProcessoResponsavel
		serventiaCargo = this.consultarJuizUPJ(id_Processo, obFabricaConexao); 
		
		if (serventiaCargo == null)
			throw new MensagemException("N�o foi poss�vel localizar o respons�vel do processo. id_Processo = " + id_Processo + ".");
		
		return serventiaCargo;
		//******************************************************************************************************	
	}
		
	/**
	 * Retorna o Id_ServentiaCargo do magistrado Respons�vel pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param id_serventia serventia para a qual quer encontrar o juiz respons�vel pelo processo
	 * @param conexao conex�o ativa
	 */
	public String consultarMagistradoResponsavelProcesso(String id_Processo, String id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
		String stRetorno = null;
		
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarMagistradoResponsavelProcesso(id_Processo, id_Serventia);
		
		return stRetorno;
	}
	
	/**
	 * Retorna true caso o processo possua algum promotor como respons�vel na inst�ncia especificada pela serventia Subtipo informada.
	 * 
	 * @param conexao conex�o ativa
	 */
	public boolean temPromotorNaInstanciaProcesso(String id_Processo, String serventiaSubtipoCodigo, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoResponsavelPs obPersistencia = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		return obPersistencia.temPromotorNaInstanciaProcesso(id_Processo, serventiaSubtipoCodigo);
	}
	
	/**
	 * reativa especificamente um respons�vel pelo processo, identificado pelo id do processo e pelo idServCargo do respons�vel.
	 * @param idProc
	 * @param idServCargo
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 */
	public boolean reativaResponsavelProcessoServentia(String idProc, String idServCargo, FabricaConexao obFabricaConexao) throws Exception {
		
		if(Funcoes.StringToInt(idProc) == 0 || Funcoes.StringToInt(idServCargo) == 0 || obFabricaConexao == null) {
			throw new MensagemException("Par�metros insuficientes ao tentar ativar respons�vel");
		}
		
		ProcessoResponsavelPs processoResponsavelPs = new ProcessoResponsavelPs(obFabricaConexao.getConexao());
		return processoResponsavelPs.reativarResponsavelProcesso(idProc, idServCargo);
	}
}
