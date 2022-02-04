package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoResponsavelPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AudienciaProcessoResponsavelNe extends AudienciaProcessoResponsavelNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2583551399769260426L;

	public String Verificar(AudienciaProcessoResponsavelDt dados) {

		String stRetorno = "";

		if (dados.getProcessoNumero().length() == 0) stRetorno += "O Campo ProcessoNumero � obrigat�rio.";
		if (dados.getUsuario().length() == 0) stRetorno += "O Campo Usuario � obrigat�rio.";
		if (dados.getCargoTipo().length() == 0) stRetorno += "O Campo CargoTipo � obrigat�rio.";
		if (dados.getDataAgendada().length() == 0) stRetorno += "O Campo DataAgendada � obrigat�rio.";
		if (dados.getNome().length() == 0) stRetorno += "O Campo Nome � obrigat�rio.";
		return stRetorno;

	}

	/**
	 * M�todo para salvar um AudienciaProcessoResponsavel, recebendo uma conex�o ativa
	 * 
	 * @param dados
	 * @param conexao
	 * @throws Exception
	 */
	public void salvar(AudienciaProcessoResponsavelDt dados, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		
		AudienciaProcessoResponsavelPs obPersistencia = new AudienciaProcessoResponsavelPs(conexao.getConexao());
		if (dados.getId().equalsIgnoreCase("")) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("AudienciaProcessoResponsavel", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("AudienciaProcessoResponsavel", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, conexao);

	}

	/**
	 * M�todo que insere os usu�rios respons�veis por uma audi�ncia.
	 * 
	 * @param id_AudienciaProcesso, identifica��o da audi�ncia que est� sendo movimentada
	 * @param audienciaTipoCodigo, tipo da audi�ncia que est� sendo movimentada
	 * @param id_Processo, identifica��o do processo que tem a audi�ncia em quest�o
	 * @param usuarioDt, usu�rio que est� movimentando audi�ncia
	 * @param logDt, objeto com dados do log
	 * @param conexao, conex�o ativa
	 * 
	 * @author msapaula
	 */
	public void salvarAudienciaProcessoResponsavel(String id_AudienciaProcesso, String audienciaTipoCodigo, ProcessoDt processoDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
					
		if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal()) {
			//Captura a serventia do processo
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());

			if (ServentiaSubtipoDt.isSegundoGrau(serventiaDt.getServentiaSubtipoCodigo()) || ServentiaSubtipoDt.isUPJTurmaRecursal(serventiaDt.getServentiaSubtipoCodigo())) {
				// Quando se tratar de sess�o de 2� em C�maras, ser�o inseridos o Relator, Revisor e Vogal como respons�veis
				List<AudienciaProcessoResponsavelDt> listaDeResponsaveis = this.consultarResponsaveisAudienciaProcesso(id_AudienciaProcesso);
				String id_UsuarioServentiaRelatorAtual = null;
				String id_UsuarioServentiaRevisorAtual = null;
				String id_UsuarioServentiaVogalAtual = null;
				
				if (listaDeResponsaveis != null) {
					for (AudienciaProcessoResponsavelDt responsavel : listaDeResponsaveis) {
						if (Funcoes.StringToInt(responsavel.getId_CargoTipo()) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU) {
							id_UsuarioServentiaRelatorAtual = responsavel.getId_UsuarioServentia();	
						} else if (Funcoes.StringToInt(responsavel.getId_CargoTipo()) == CargoTipoDt.REVISOR_SEGUNDO_GRAU) {
							id_UsuarioServentiaRevisorAtual = responsavel.getId_UsuarioServentia();	
						} else if (Funcoes.StringToInt(responsavel.getId_CargoTipo()) == CargoTipoDt.VOGAL_SEGUNDO_GRAU) {
							id_UsuarioServentiaVogalAtual = responsavel.getId_UsuarioServentia();	
						}
					}					 
				}
				
				String id_UsuarioServentiaRelator = new ProcessoResponsavelNe().getUsuarioRelatorResponsavelCargoTipo(processoDt.getId(), conexao);
				if (id_UsuarioServentiaRelatorAtual == null || !id_UsuarioServentiaRelatorAtual.trim().equalsIgnoreCase(id_UsuarioServentiaRelator)) {
					if (id_UsuarioServentiaRelator == null) throw new MensagemException("N�o foi identificado relator ativo no processo.");
					this.inserirAudienciaProcessoResponsavel(id_AudienciaProcesso, String.valueOf(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU), id_UsuarioServentiaRelator, logDt, conexao);	
				}				
				
				String id_UsuarioServentiaRevisor = new ProcessoResponsavelNe().getUsuarioRevisorResponsavelCargoTipo(processoDt.getId(), conexao);
				if (id_UsuarioServentiaRevisorAtual == null || !id_UsuarioServentiaRevisorAtual.trim().equalsIgnoreCase(id_UsuarioServentiaRevisor)) {
					if (id_UsuarioServentiaRevisor != null) this.inserirAudienciaProcessoResponsavel(id_AudienciaProcesso, String.valueOf(CargoTipoDt.REVISOR_SEGUNDO_GRAU), id_UsuarioServentiaRevisor, logDt, conexao);	
				}				
				
				String id_UsuarioServentiaVogal = new ProcessoResponsavelNe().getUsuarioVogalResponsavelCargoTipo(processoDt.getId(), conexao);
				if (id_UsuarioServentiaVogalAtual == null || !id_UsuarioServentiaVogalAtual.trim().equalsIgnoreCase(id_UsuarioServentiaVogal)) {
					if (id_UsuarioServentiaVogal != null) this.inserirAudienciaProcessoResponsavel(id_AudienciaProcesso, String.valueOf(CargoTipoDt.VOGAL_SEGUNDO_GRAU), id_UsuarioServentiaVogal, logDt, conexao);	
				}				
				
			} else if (ServentiaSubtipoDt.isTurma(serventiaDt.getServentiaSubtipoCodigo())) {
				//No caso das turmas, ser�o inseridos o presidente da turma e o relator como respons�veis
				String id_UsuarioServentiaPresidente = new ServentiaCargoNe().getUsuarioServentiaPresidenteTurmaRecursal(usuarioDt.getId_Serventia());
				if (id_UsuarioServentiaPresidente == null || id_UsuarioServentiaPresidente.trim().equalsIgnoreCase("")) throw new MensagemException("O presidente da Turma n�o foi encontrado. Favor verificar.");					
				this.inserirAudienciaProcessoResponsavel(id_AudienciaProcesso, String.valueOf(CargoTipoDt.PRESIDENTE_TURMA), id_UsuarioServentiaPresidente, logDt, conexao);

				// Consulta o relator respons�vel pelo processo, pois ele ser� respons�vel pela movimenta��o da audi�ncia
				String id_UsuarioServentiaRelator = new ProcessoResponsavelNe().getUsuarioRelatorResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), conexao);
				if (id_UsuarioServentiaRelator == null || id_UsuarioServentiaRelator.trim().equalsIgnoreCase("")) throw new MensagemException("O Relator do Processo n�o foi encontrado. Favor verificar.");
				this.inserirAudienciaProcessoResponsavel(id_AudienciaProcesso, String.valueOf(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU), id_UsuarioServentiaRelator, logDt, conexao);
			} else {
				throw new MensagemException("Serventia n�o mapeada para o segundo grau ou turma recursal. C�digo : " + serventiaDt.getServentiaSubtipoCodigo() + ".");
			}
		} else {
			// Para audi�ncia de instru��o ser�o inseridos como respons�veis o juiz e quem est� movimentando a audi�ncia
			if ((Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.INSTRUCAO.ordinal()) || (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.INSTRUCAO_JULGAMENTO.ordinal())) {

				 String id_UsuarioServentiaJuiz = new ProcessoResponsavelNe().getUsuarioJuizResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), conexao);
				
				if (id_UsuarioServentiaJuiz == null)
					id_UsuarioServentiaJuiz = this.getUsuarioJuizResponsavelAudiencia(id_AudienciaProcesso, conexao);
				
				this.inserirAudienciaProcessoResponsavel(id_AudienciaProcesso, String.valueOf(CargoTipoDt.JUIZ_1_GRAU), id_UsuarioServentiaJuiz, logDt, conexao);
			}

			// Para os outros casos ser� inserido como respons�vel o usu�rio que est� movimentando a audi�ncia
			this.inserirAudienciaProcessoResponsavel(id_AudienciaProcesso, usuarioDt.getCargoTipoCodigo(), usuarioDt.getId_UsuarioServentia(), logDt, conexao);
		}


	}
	
	/**
	 * Retorna o Id_UsuarioServentia do Juiz respons�vel pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo identifica��o de processo
	 * @param id_serventia serventia para a qual quer encontrar o juiz respons�vel pelo processo
	 * @param conexao conex�o ativa
	 * @author msapaula
	 */
	public String getUsuarioJuizResponsavelAudiencia(String id_AudienciaProcesso, FabricaConexao obFabricaConexao) throws Exception {
		String stRetorno = null;
		
		AudienciaProcessoResponsavelPs obPersistencia = new AudienciaProcessoResponsavelPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarJuizResponsavelAudiencia(id_AudienciaProcesso);
		
		return stRetorno;
	}

	/**
	 * Monta objeto AudienciaProcessoResponsavel e chama m�todo salvar()
	 * 
	 * @param id_AudienciaProcesso
	 * @param cargoTipo
	 * @param id_UsuarioServentia
	 * @throws Exception 
	 */
	// jvosantos - 25/07/2019 17:42 - Alterar o m�todo para public, para utilizar em outra classe (Corre��o para trazer o relator da sess�o corretamente na hora de gerar a movimenta��o da decis�o)
	public void inserirAudienciaProcessoResponsavel(String id_AudienciaProcesso, String cargoTipoCodigo, String id_UsuarioServentia, LogDt logDt, FabricaConexao conexao) throws Exception{
		AudienciaProcessoResponsavelDt audienciaProcessoResponsavelDt = new AudienciaProcessoResponsavelDt();
		audienciaProcessoResponsavelDt.setId_AudienciaProcesso(id_AudienciaProcesso);
		if (cargoTipoCodigo != null && cargoTipoCodigo.length() > 0) audienciaProcessoResponsavelDt.setCargoTipoCodigo(cargoTipoCodigo);
		else throw new MensagemException("Usu�rio n�o est� habilitado em um Cargo da Serventia.");
		
		audienciaProcessoResponsavelDt.setId_UsuarioServentia(id_UsuarioServentia);
		audienciaProcessoResponsavelDt.setId_UsuarioLog(logDt.getId_Usuario());
		audienciaProcessoResponsavelDt.setIpComputadorLog(logDt.getIpComputador());
		
		this.salvar(audienciaProcessoResponsavelDt, conexao);
	}

	/**
	 * Consulta os respons�veis por uma "AudienciaProcesso". Retorna todas os usu�rios que realizaram uma audi�ncia de processo.
	 * 
	 * @author msapaula
	 */
	public List consultarResponsaveisAudienciaProcesso(String id_AudienciaProcesso) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoResponsavelPs obPersistencia = new AudienciaProcessoResponsavelPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarResponsaveisAudienciaProcesso(id_AudienciaProcesso);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarDescricaoCargoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		CargoTipoNe CargoTipone = new CargoTipoNe(); 
		stTemp = CargoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

}
