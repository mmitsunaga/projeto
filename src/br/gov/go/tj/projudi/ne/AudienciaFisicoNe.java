package br.gov.go.tj.projudi.ne;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaFisicoMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.RetornoAudienciaMarcada;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.AudienciaFisicoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AudienciaFisicoNe extends AudienciaNe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1787730558528154555L;

	public String validarAudienciaAgendamento(AudienciaFisicoDt audienciaDtAgendar, UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaFisicoPs obPersistencia= new  AudienciaFisicoPs(obFabricaConexao.getConexao());
			
			if (audienciaDtAgendar.getProcessoNumero() == null || audienciaDtAgendar.getProcessoNumero().trim().length() == 0) {
				stRetorno += " O número do processo não foi informado. \n";
			}
			
			if (audienciaDtAgendar.getProcessoNumero() != null && !Funcoes.validaProcessoNumero( audienciaDtAgendar.getProcessoNumero() )  ) {
				stRetorno += " O número do processo inválido. Certifique-se de digitar o número completo de 20 dígitos. \n";
			}
			
			if (audienciaDtAgendar.getAudienciaTipoCodigo() == null || audienciaDtAgendar.getAudienciaTipoCodigo().equals("")) {
				stRetorno += " O tipo de Audiência não foi informado. \n";
			}
			
			if (!(audienciaDtAgendar.isAgendamentoManual() || audienciaDtAgendar.isAgendamentoAutomatico())) {
				stRetorno += " O tipo de Agendamento não foi informado. \n";
			}

			// Verifica se já existe audiência marcada
			if (stRetorno.length() == 0) {				
				ProcessoFisicoDt processoFisicoDt = new ProcessoFisicoNe().getProcessoFisicoDadosGerais(Funcoes.obtenhaSomenteNumeros(audienciaDtAgendar.getProcessoNumero()));				
				
				if (processoFisicoDt == null) {
					stRetorno += " O número do processo informado não existe na base do SPG. \n";
				} else {
					
					processoFisicoDt.setNumeroProcesso(audienciaDtAgendar.getProcessoNumero());
					
					audienciaDtAgendar.getAudienciaProcessoDt().setProcessoFisicoDt(processoFisicoDt);
					
					RetornoAudienciaMarcada retornoAudienciaMarcada = obPersistencia.obtenhaAudienciaTipoAgendamentoProcessoFisicoASerRealizada(audienciaDtAgendar.getProcessoNumero());
					if (retornoAudienciaMarcada != null) {
						stRetorno += "Agendamento de audiência não pode ser realizado. O processo já possui uma audiência do tipo " + AudienciaTipoDt.Codigo.getDescricao(retornoAudienciaMarcada.getTipoAudiencia()) + " marcada para " + retornoAudienciaMarcada.getDataAudiencia() + " com status pendente na serventia " + retornoAudienciaMarcada.getServentia() + ".";
					}	
				}
			}
			
			// Verifica se o usuário pode marcar atudiência para processos físicos dessa serventia
			if (stRetorno.length() == 0 && 
				audienciaDtAgendar.getAudienciaProcessoDt() != null &&
				audienciaDtAgendar.getAudienciaProcessoDt().getProcessoFisicoDt() != null &&
				Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL) {
				
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(usuarioDt.getId_Serventia(), obFabricaConexao);
				
				if (serventiaDt != null && serventiaDt.getServentiaCodigoExterno() != null &&				
				    Funcoes.StringToInt(serventiaDt.getServentiaCodigoExterno()) != Funcoes.StringToInt(audienciaDtAgendar.getAudienciaProcessoDt().getProcessoFisicoDt().getEscrivaniaCodigo())) {
					stRetorno += "Agendamento de audiência não pode ser realizado. O processo está vinculado a outra serventia (" + audienciaDtAgendar.getAudienciaProcessoDt().getProcessoFisicoDt().getEscrivania() + ").";	
				} else if (audienciaDtAgendar.isAgendamentoManual() && audienciaDtAgendar.isTipoConciliacaoMediacaoCEJUSC()) {
					stRetorno += " O tipo de Agendamento informado não pode ser manual para as audiências do tipo Conciliação / Mediação CEJUSC. \n";
				}
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
	
		return stRetorno;		
	}
	
	public AudienciaFisicoDt agendarAudienciaAutomaticamenteProcesso(AudienciaFisicoDt audienciaDt, UsuarioDt usuarioDt) throws Exception{
		AudienciaFisicoDt audienciaDtAgendada = null;
		AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
		tipoCodigo = tipoCodigo.getCodigo(Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()));

		switch (tipoCodigo) {
			//Audiências que precisam ficar vinculadas a um juiz
			case INSTRUCAO:
			case INSTRUCAO_JULGAMENTO:
			case JULGAMENTO: {
				// Será localizada a próxima audiência livre para o cargo do juiz do processo
				audienciaDtAgendada = agendarAudienciaAutomaticamenteServentiaCargo(audienciaDt, usuarioDt);
				break;
			}
			//audiências que não precisam ficar vinculadas a juiz
			default: {
				// Será localizada próxima audiência livre independente do Cargo
				audienciaDtAgendada = agendarAudienciaAutomaticamenteServentia(audienciaDt, usuarioDt);
				break;
			}
		}

		return audienciaDtAgendada;
	}
	
	private AudienciaFisicoDt agendarAudienciaAutomaticamenteServentia(AudienciaFisicoDt audienciaDt, UsuarioDt usuarioDt) throws Exception{
		AudienciaFisicoDt audienciaDtAgendada = null;
		AudienciaProcessoFisicoNe audienciaProcessoNe = new AudienciaProcessoFisicoNe();
		FabricaConexao obFabricaConexao = null;
		try{
			LogDt logDt = new LogDt(audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog());			

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			AudienciaFisicoPs obPersistencia= new  AudienciaFisicoPs(obFabricaConexao.getConexao());

			AudienciaProcessoFisicoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
			ProcessoFisicoDt processoFisicoDt = audienciaProcessoDt.getProcessoFisicoDt();

			// AGENDAR AUDIÊNCIA PARA O PROCESSO
			if (audienciaProcessoNe.agendarAudienciaProcessoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoFisicoDt, audienciaDt.getId_Serventia(), obFabricaConexao)){
				audienciaDtAgendada = obPersistencia.getUltimaAudienciaMarcadaAgendamentoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoFisicoDt);
				audienciaDt.setReservada("1");
				super.salvar(audienciaDt, obFabricaConexao);
				obPersistencia.inserirVinculoProcessoFisico(audienciaDtAgendada, processoFisicoDt);
				gravarLog(audienciaDtAgendada, logDt, obFabricaConexao);				
			}
			
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtAgendada;
	}
	
	private AudienciaFisicoDt agendarAudienciaAutomaticamenteServentiaCargo(AudienciaFisicoDt audienciaDt, UsuarioDt usuarioDt) throws Exception{
		AudienciaFisicoDt audienciaDtAgendada = null;
		AudienciaProcessoFisicoNe audienciaProcessoNe = new AudienciaProcessoFisicoNe();	
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaFisicoPs obPersistencia= new  AudienciaFisicoPs(obFabricaConexao.getConexao());

			LogDt logDt = new LogDt(audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog());

			AudienciaProcessoFisicoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
			ProcessoFisicoDt processoFisicoDt = audienciaProcessoDt.getProcessoFisicoDt();

			//JUIZ RESPONSÁVEL - Serão localizadas as audiências livres referentes a esse cargo serventia do juiz responsável pela serventia.
			List magistrados = new ServentiaCargoNe().consultarServentiaCargosJuizes(usuarioDt.getId_Serventia());
			
			if (magistrados == null || magistrados.size() == 0)
				throw new MensagemException("Impossível marcar audiência desse tipo, pois não existem magistrados cadastrados nessa serventia.");
			
			ServentiaCargoDt serventiaCargoAudienciaDt = null; 
			
			Iterator iteratorServentiaCargoDt = magistrados.iterator();
			while (iteratorServentiaCargoDt.hasNext()) {
				ServentiaCargoDt magistrado = (ServentiaCargoDt) iteratorServentiaCargoDt.next();
				if (Funcoes.StringToInt(magistrado.getQuantidadeDistribuicao()) > 0) {
					serventiaCargoAudienciaDt = magistrado;
				}
			}
			
			if (serventiaCargoAudienciaDt == null)
				throw new MensagemException("Impossível marcar audiência desse tipo, pois não existem magistrados cadastrados nessa serventia com quantidade de distribuição maior que zero.");
			
			//AGENDAR AUDIÊNCIA PARA O JUIZ DO PROCESSO
			if (audienciaProcessoNe.agendarAudienciaProcessoAutomaticoServentiaCargo(serventiaCargoAudienciaDt.getId(), audienciaDt.getAudienciaTipoCodigo(), processoFisicoDt, obFabricaConexao)) {
				audienciaDtAgendada = obPersistencia.getUltimaAudienciaMarcadaAgendamentoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoFisicoDt);
				obPersistencia.inserirVinculoProcessoFisico(audienciaDtAgendada, processoFisicoDt);
				gravarLog(audienciaDtAgendada, logDt, obFabricaConexao);
			}							
			
			obFabricaConexao.finalizarTransacao();	
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtAgendada;
	}
	
	private void gravarLog(AudienciaFisicoDt audienciaDtAgendada, LogDt logDt, FabricaConexao conexao) throws Exception {
		logDt.setTabela("AudienciaProcessoFisico");
		logDt.setId_Tabela(audienciaDtAgendada.getId_AudienciaProcessoFisico());
		logDt.setId_LogTipo(String.valueOf(LogTipoDt.Incluir));
		logDt.setValorAtual(audienciaDtAgendada.getPropriedades());
		new LogNe().salvar(logDt, conexao);        
    }
	
	public AudienciaFisicoDt consultarAudienciaLivreCompleta(String id_Audiencia) throws Exception {
		AudienciaFisicoDt audienciaDtCompleta = null;
		FabricaConexao obFabricaConexao = null;

		try{
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaFisicoPs obPersistencia = new  AudienciaFisicoPs(obFabricaConexao.getConexao());
			audienciaDtCompleta = obPersistencia.consultarAudienciaLivreCompleta(id_Audiencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtCompleta;
	}
	
	public boolean agendarAudienciaManualmente(AudienciaFisicoDt audienciaDtAgendar, UsuarioDt usuarioDt) throws Exception {
		AudienciaProcessoFisicoNe audienciaProcessoNe = new AudienciaProcessoFisicoNe();
		boolean resultado = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			AudienciaProcessoFisicoDt audienciaProcessoDt = audienciaDtAgendar.getAudienciaProcessoDt();			
			ProcessoFisicoDt processoDt = audienciaProcessoDt.getProcessoFisicoDt();

			// Seta Id_Processo e Status em AudienciaProcesso
			audienciaProcessoDt.setCodigoTemp(Funcoes.obtenhaSomenteNumeros(processoDt.getNumeroProcesso()));
			audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA));
			//audienciaDtAgendar.setReservada();

			// SALVA ATUALIZAÇÃO DE "AUDIENCIAPROCESSODT"
			if (audienciaProcessoNe.agendarAudienciaProcessoManual(audienciaProcessoDt, obFabricaConexao)){				
				resultado = true;
				audienciaDtAgendar.setReservada("1");
				super.salvar(audienciaDtAgendar, obFabricaConexao);
				audienciaProcessoNe.incluirAudienciaProcessoAgendamentoProcessoFisico(audienciaProcessoDt, obFabricaConexao);
			}
			
			// COMMIT TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
						
			return resultado;			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	public void retirarAudienciaProcesso(AudienciaFisicoDt audienciaDtAgendar, UsuarioDt usuarioDt) throws Exception {
		AudienciaProcessoFisicoNe audienciaProcessoNe = new AudienciaProcessoFisicoNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			AudienciaProcessoFisicoDt audienciaProcessoDt = audienciaDtAgendar.getAudienciaProcessoDt();			
			
			// SALVA ATUALIZAÇÃO DE "AUDIENCIAPROCESSODT"
			audienciaProcessoNe.retirarAudienciaProcesso(audienciaProcessoDt, obFabricaConexao);			
			audienciaDtAgendar.setReservada("0");
			super.salvar(audienciaDtAgendar, obFabricaConexao);
			audienciaProcessoNe.incluirAudienciaProcessoAgendamentoProcessoFisico(audienciaProcessoDt, obFabricaConexao);
						
			// COMMIT TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	public String verificarMovimentacaoAudienciaProcesso(AudienciaFisicoMovimentacaoDt dados, UsuarioDt usuarioDt){
		String stRetorno = "";
		
		if (dados.getAudienciaStatusCodigo().length() > 0 && dados.getAudienciaStatusCodigo().equals("-1")) {
			stRetorno += "Selecione o Status da Audiência. \n";		
		} else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.RETIRAR_PAUTA && dados.getAudienciaDt().devePossuirIndicadorDeAcordo()) {
			if (!(dados.getAudienciaProcessoFisicoDt().isHouveAcordo() || dados.getAudienciaProcessoFisicoDt().isNaoHouveAcordo())){
				stRetorno += "Selecione a opção Houve Acordo? \n";	
			} else if (dados.getAudienciaProcessoFisicoDt().isHouveAcordo() && dados.getAudienciaProcessoFisicoDt().getValorAcordo().length() == 0) {
				stRetorno += "Valor do acordo deve ser maior que zero. \n";				
			}
		}	
		
		return stRetorno;
	}
	
	public void salvarMovimentacaoAudienciaProcesso(AudienciaFisicoMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
		FabricaConexao obFabricaConexao = null;
		AudienciaProcessoFisicoNe audienciaProcessoFisicoNe = new AudienciaProcessoFisicoNe();
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();			

			LogDt logDt = new LogDt(usuarioDt.getId(), audienciaMovimentacaoDt.getIpComputadorLog());
			
			//--segundo grau gerar publicação das decisões
		    int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
	        codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());
	        
	        // Atualiza dados em "Audiencia"
			String dataMovimentacao = Funcoes.DataHora(new Date());
			
			audienciaProcessoFisicoNe.alterarAudienciaProcessoMovimentacao(audienciaMovimentacaoDt.getAudienciaProcessoFisicoDt(), dataMovimentacao, String.valueOf(codigoStatus), logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch(MensagemException m){
			obFabricaConexao.cancelarTransacao();
			throw m;
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();			
			throw e;
		} finally{
			 obFabricaConexao.fecharConexao();
		}
	}
}
