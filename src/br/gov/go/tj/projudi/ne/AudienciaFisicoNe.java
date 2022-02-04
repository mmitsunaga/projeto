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
				stRetorno += " O n�mero do processo n�o foi informado. \n";
			}
			
			if (audienciaDtAgendar.getProcessoNumero() != null && !Funcoes.validaProcessoNumero( audienciaDtAgendar.getProcessoNumero() )  ) {
				stRetorno += " O n�mero do processo inv�lido. Certifique-se de digitar o n�mero completo de 20 d�gitos. \n";
			}
			
			if (audienciaDtAgendar.getAudienciaTipoCodigo() == null || audienciaDtAgendar.getAudienciaTipoCodigo().equals("")) {
				stRetorno += " O tipo de Audi�ncia n�o foi informado. \n";
			}
			
			if (!(audienciaDtAgendar.isAgendamentoManual() || audienciaDtAgendar.isAgendamentoAutomatico())) {
				stRetorno += " O tipo de Agendamento n�o foi informado. \n";
			}

			// Verifica se j� existe audi�ncia marcada
			if (stRetorno.length() == 0) {				
				ProcessoFisicoDt processoFisicoDt = new ProcessoFisicoNe().getProcessoFisicoDadosGerais(Funcoes.obtenhaSomenteNumeros(audienciaDtAgendar.getProcessoNumero()));				
				
				if (processoFisicoDt == null) {
					stRetorno += " O n�mero do processo informado n�o existe na base do SPG. \n";
				} else {
					
					processoFisicoDt.setNumeroProcesso(audienciaDtAgendar.getProcessoNumero());
					
					audienciaDtAgendar.getAudienciaProcessoDt().setProcessoFisicoDt(processoFisicoDt);
					
					RetornoAudienciaMarcada retornoAudienciaMarcada = obPersistencia.obtenhaAudienciaTipoAgendamentoProcessoFisicoASerRealizada(audienciaDtAgendar.getProcessoNumero());
					if (retornoAudienciaMarcada != null) {
						stRetorno += "Agendamento de audi�ncia n�o pode ser realizado. O processo j� possui uma audi�ncia do tipo " + AudienciaTipoDt.Codigo.getDescricao(retornoAudienciaMarcada.getTipoAudiencia()) + " marcada para " + retornoAudienciaMarcada.getDataAudiencia() + " com status pendente na serventia " + retornoAudienciaMarcada.getServentia() + ".";
					}	
				}
			}
			
			// Verifica se o usu�rio pode marcar atudi�ncia para processos f�sicos dessa serventia
			if (stRetorno.length() == 0 && 
				audienciaDtAgendar.getAudienciaProcessoDt() != null &&
				audienciaDtAgendar.getAudienciaProcessoDt().getProcessoFisicoDt() != null &&
				Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL) {
				
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(usuarioDt.getId_Serventia(), obFabricaConexao);
				
				if (serventiaDt != null && serventiaDt.getServentiaCodigoExterno() != null &&				
				    Funcoes.StringToInt(serventiaDt.getServentiaCodigoExterno()) != Funcoes.StringToInt(audienciaDtAgendar.getAudienciaProcessoDt().getProcessoFisicoDt().getEscrivaniaCodigo())) {
					stRetorno += "Agendamento de audi�ncia n�o pode ser realizado. O processo est� vinculado a outra serventia (" + audienciaDtAgendar.getAudienciaProcessoDt().getProcessoFisicoDt().getEscrivania() + ").";	
				} else if (audienciaDtAgendar.isAgendamentoManual() && audienciaDtAgendar.isTipoConciliacaoMediacaoCEJUSC()) {
					stRetorno += " O tipo de Agendamento informado n�o pode ser manual para as audi�ncias do tipo Concilia��o / Media��o CEJUSC. \n";
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
			//Audi�ncias que precisam ficar vinculadas a um juiz
			case INSTRUCAO:
			case INSTRUCAO_JULGAMENTO:
			case JULGAMENTO: {
				// Ser� localizada a pr�xima audi�ncia livre para o cargo do juiz do processo
				audienciaDtAgendada = agendarAudienciaAutomaticamenteServentiaCargo(audienciaDt, usuarioDt);
				break;
			}
			//audi�ncias que n�o precisam ficar vinculadas a juiz
			default: {
				// Ser� localizada pr�xima audi�ncia livre independente do Cargo
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

			// AGENDAR AUDI�NCIA PARA O PROCESSO
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

			//JUIZ RESPONS�VEL - Ser�o localizadas as audi�ncias livres referentes a esse cargo serventia do juiz respons�vel pela serventia.
			List magistrados = new ServentiaCargoNe().consultarServentiaCargosJuizes(usuarioDt.getId_Serventia());
			
			if (magistrados == null || magistrados.size() == 0)
				throw new MensagemException("Imposs�vel marcar audi�ncia desse tipo, pois n�o existem magistrados cadastrados nessa serventia.");
			
			ServentiaCargoDt serventiaCargoAudienciaDt = null; 
			
			Iterator iteratorServentiaCargoDt = magistrados.iterator();
			while (iteratorServentiaCargoDt.hasNext()) {
				ServentiaCargoDt magistrado = (ServentiaCargoDt) iteratorServentiaCargoDt.next();
				if (Funcoes.StringToInt(magistrado.getQuantidadeDistribuicao()) > 0) {
					serventiaCargoAudienciaDt = magistrado;
				}
			}
			
			if (serventiaCargoAudienciaDt == null)
				throw new MensagemException("Imposs�vel marcar audi�ncia desse tipo, pois n�o existem magistrados cadastrados nessa serventia com quantidade de distribui��o maior que zero.");
			
			//AGENDAR AUDI�NCIA PARA O JUIZ DO PROCESSO
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
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
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

			// SALVA ATUALIZA��O DE "AUDIENCIAPROCESSODT"
			if (audienciaProcessoNe.agendarAudienciaProcessoManual(audienciaProcessoDt, obFabricaConexao)){				
				resultado = true;
				audienciaDtAgendar.setReservada("1");
				super.salvar(audienciaDtAgendar, obFabricaConexao);
				audienciaProcessoNe.incluirAudienciaProcessoAgendamentoProcessoFisico(audienciaProcessoDt, obFabricaConexao);
			}
			
			// COMMIT TRANSA��O
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
			
			// SALVA ATUALIZA��O DE "AUDIENCIAPROCESSODT"
			audienciaProcessoNe.retirarAudienciaProcesso(audienciaProcessoDt, obFabricaConexao);			
			audienciaDtAgendar.setReservada("0");
			super.salvar(audienciaDtAgendar, obFabricaConexao);
			audienciaProcessoNe.incluirAudienciaProcessoAgendamentoProcessoFisico(audienciaProcessoDt, obFabricaConexao);
						
			// COMMIT TRANSA��O
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	public String verificarMovimentacaoAudienciaProcesso(AudienciaFisicoMovimentacaoDt dados, UsuarioDt usuarioDt){
		String stRetorno = "";
		
		if (dados.getAudienciaStatusCodigo().length() > 0 && dados.getAudienciaStatusCodigo().equals("-1")) {
			stRetorno += "Selecione o Status da Audi�ncia. \n";		
		} else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.RETIRAR_PAUTA && dados.getAudienciaDt().devePossuirIndicadorDeAcordo()) {
			if (!(dados.getAudienciaProcessoFisicoDt().isHouveAcordo() || dados.getAudienciaProcessoFisicoDt().isNaoHouveAcordo())){
				stRetorno += "Selecione a op��o Houve Acordo? \n";	
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
			
			//--segundo grau gerar publica��o das decis�es
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
