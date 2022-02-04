package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoComutacaoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoPrescricaoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoProgressaoLivramentoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoSursisDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoSituacaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.EventoLocalDt;
import br.gov.go.tj.projudi.dt.EventoRegimeDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt;
import br.gov.go.tj.projudi.dt.TransitoJulgadoEventoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoEventoExecucaoRelatorioDt;
import br.gov.go.tj.projudi.dt.relatorios.SaidaTemporariaDt;
import br.gov.go.tj.projudi.ps.ProcessoEventoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.InterfaceJasper;
import br.gov.go.tj.utils.pdf.InterfaceMultiplaSubReportJasper;

//---------------------------------------------------------
public class ProcessoEventoExecucaoNe extends ProcessoEventoExecucaoNeGen {

	/**
     * 
     */
	private static final long serialVersionUID = -1024198932106680244L;

	public String Verificar(ProcessoEventoExecucaoDt dados) {
		String stRetorno = "";

		if (dados.getDataInicio().length() == 0) stRetorno += "O campo Data Início é obrigatório.";
		if (dados.getMovimentacaoTipo().length() == 0) stRetorno += "O campo Movimentação do Processo é obrigatório.";

		return stRetorno;
	}

	public String VerificarCadastroEventoExecucao(ProcessoEventoExecucaoDt dados, ProcessoExecucaoDt processoExecucaoDt, String idProcesso, int tempoHorasEstudo, String dataDecretoSelecionado) throws Exception {
		String stRetorno = "";

		// verifica se perdeu a referência do processoExecucao
		if (dados.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)) || dados.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))) {
			if (dados.getId_ProcessoExecucao().length() == 0) return "Selecione novamente o evento e realize as alterações. \nMotivo: Perda da referência com a Ação Penal.";
			else
				stRetorno = new ProcessoExecucaoNe().verificarDadosSentenca(processoExecucaoDt, false);
		}

		if (dados.getId_Movimentacao().length() == 0) stRetorno += "O campo Movimentação do Processo é obrigatório. \n";
		if (dados.getEventoExecucao().length() == 0) stRetorno += "O campo Evento é obrigatório. \n";
		if (dados.getDataInicio().length() == 0) stRetorno += "O campo Data Início é obrigatório. \n";
		else {
			if (isEventoManterAcaoPenal(dados.getId_EventoExecucao()).equalsIgnoreCase("false")) 
				if (!Funcoes.validaData(dados.getDataInicio())) stRetorno += "A data início informada não é válida. Verifique!";
		}

		String informarQuantidade = isInformarQuantidade(dados.getId_EventoExecucao());

		if ((dados.getEventoExecucaoDt().getAlteraRegime().equalsIgnoreCase("S") && !dados.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PRISAO)) && !dados.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PRISAO_FLAGRANTE))) && (dados.getEventoRegimeDt().getId_RegimeExecucao().length() == 0)) stRetorno += "O campo Regime é obrigatório. \n";
		if ((dados.getEventoExecucaoDt().getAlteraLocal().equalsIgnoreCase("S")) && (dados.getEventoLocalDt().getId_LocalCumprimentoPena().length() == 0)) stRetorno += "O campo Local de Cumprimento de pena é obrigatório. \n";

		if (dados.getDataInicio().length() > 0 && dados.getDataInicioLivramentoCondicional() != null && dados.getDataInicioLivramentoCondicional().length() > 0) {
			if (dados.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))) {
				int resultado = Funcoes.calculaDiferencaEntreDatas(dados.getDataInicio(), dados.getDataInicioLivramentoCondicional());
				if (resultado > 0) stRetorno += "Verifique! A Data Início da Revogação do Livramento deve ser posterior à Data Início da Concessão do Livramento. \n";
			}
			if (dados.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))) {
				// if (dados.getDataInicioLivramentoCondicional() != null) {
				int resultado = Funcoes.calculaDiferencaEntreDatas(dados.getDataInicio(), dados.getDataInicioLivramentoCondicional());
				if (resultado > 0) stRetorno += "Verifique! A Data Início da Concessão do Livramento deve ser posterior à Data Início da última Revogação. \n";
				// }
			}
		}
		
		if (informarQuantidade.length() > 0 && dados.getQuantidade().length() == 0) {
			stRetorno += "O campo Quantidade é obrigatório. \n";
		} else {
			if (dados.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_TRABALHADOS_REMICAO))) {
				// validações quando for inclusão perda de dias trabalhados
				List listaTipoEvento = new ArrayList(2);
				listaTipoEvento.add(0, String.valueOf(EventoExecucaoDt.DIAS_TRABALHADOS_REMICAO));
				listaTipoEvento.add(1, String.valueOf(EventoExecucaoDt.PERDA_DIAS_TRABALHADOS_REMICAO));
				int saldoDiasTrabalhados = this.VerificarSaldoDiasTrabalhadosHorasEstudo(idProcesso, dados.getDataInicio(), listaTipoEvento);

				if (saldoDiasTrabalhados < Funcoes.StringToInt(dados.getQuantidade())) {
					stRetorno += "Verifique! A Quantidade de Perda de Dias Trabalhados deve ser menor que o Saldo encontrado de " + saldoDiasTrabalhados + " Dias Trabalhados anterior à Data Início deste evento. \n";
				}
			}
			if (dados.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMICAO))) {
				// validações quando for inclusão perda de dias trabalhados
				List listaTipoEvento = new ArrayList(2);
				listaTipoEvento.add(0, String.valueOf(EventoExecucaoDt.HORAS_ESTUDO_REMICAO));
				listaTipoEvento.add(1, String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMICAO));
				int saldoHorasEstudo = this.VerificarSaldoDiasTrabalhadosHorasEstudo(idProcesso, dados.getDataInicio(), listaTipoEvento);
				if (saldoHorasEstudo < Funcoes.StringToInt(dados.getQuantidade())) {
					stRetorno += "Verifique! A Quantidade de Perda de Horas de Estudo deve ser menor que o Saldo encontrado de " + saldoHorasEstudo + " Horas de Estudo anterior à Data Início deste evento. \n";
				}
			}
			if (dados.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))) {
				if (dataDecretoSelecionado.length() == 0) stRetorno += "Selecione a data do decreto!";

				List listaEvento = listarEventos(idProcesso, "");
				montarListaEventos(listaEvento, null, null, idProcesso, tempoHorasEstudo);
				// int totalCondenacao =
				// Funcoes.StringToInt(calcularTempoTotalCondenacaoRemanescenteDias(listaEvento));

				List listaTotalCondenacaoRemanescente = new ArrayList();

				for (ProcessoEventoExecucaoDt pee : (List<ProcessoEventoExecucaoDt>) listaEvento) {
					if (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) || pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)) || pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))) {
						listaTotalCondenacaoRemanescente.add(pee.getCondenacaoAnos());
					}
				}
				int totalCondenacao = Funcoes.StringToInt(somarAnoMesDia(listaTotalCondenacaoRemanescente));

				if (totalCondenacao > 0) {
					if (Funcoes.StringToInt(dados.getQuantidade()) > totalCondenacao) stRetorno += "O tempo de comutação informado é maior que o total da condenação do sentenciado. \n CONDENAÇÃO REMANESCENTE: " + totalCondenacao + "dias. \n";
				}
			}
		}
		
		

		return stRetorno;
	}

	/**
	 * Método utilizado para verificar se é possível excluir o evento selecionado
	 * 
	 * @param dados
	 *            , evento a ser excluído
	 * @param lista
	 *            , lista de eventos
	 * @author wcsilva
	 */
	public String VerificarExclusao(ProcessoEventoExecucaoDt dados, List lista) {
		String stRetorno = "";

		if (dados.getEventoExecucaoDt().getId().equalsIgnoreCase(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))) 
			stRetorno += "Não é possível excluir o Trânsito em Julgado!";

		if (dados.getEventoExecucaoDt().getId().equalsIgnoreCase(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) 
			stRetorno += "Não é possível excluir o evento Guia de Recolhimento Provisória!";

		if (dados.getEventoExecucaoDt().getId().equalsIgnoreCase(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))) {
			for (int i = 0; i < lista.size(); i++) {
				if (dados.getId().equals(((ProcessoEventoExecucaoDt) lista.get(i)).getId_LivramentoCondicional())) 
					stRetorno += "Verifique! Não foi possível excluir a Concessão de Livramento Condicional pois existe uma Revogação referente a mesma. Primeiro exclua a Revogação do Livramento Condicional!";
			}
		}
		return stRetorno;
	}

	/**
	 * Verifica se os dados necessários para realização do cálculo foram informados.
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @return stRetorno: String com a mensagem de erro.
	 * @author wcsilva
	 * @throws MensagemException 
	 */
	public String verificarCalculo(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt) throws MensagemException{
		String stRetorno = "";

		// verifica se existe próximo regime para o regime atual.
		boolean isVerificaRegime = true;
		if (calculoLiquidacaoDt.getListaTipoCalculo() != null) {
			for (String tipoCalculo : (List<String>) calculoLiquidacaoDt.getListaTipoCalculo()) {
				if (tipoCalculo.equals("PRESCRICAO_PUNITIVA") 
						|| tipoCalculo.equals("OUTRAPENA")
						|| tipoCalculo.equals("SURSIS")
						|| tipoCalculo.equals("PRESCRICAO_EXECUTORIA_IND")
						|| tipoCalculo.equals("PRESCRICAO_EXECUTORIA_UNI")
						) {
					isVerificaRegime = false;
					break;
				}
			}
		}
		if (isVerificaRegime && calculoLiquidacaoDt.isIniciouCumprimentoPena()) {
			EventoRegimeDt eventoRegimeDt = getUltimoEventoRegimeDt(listaEvento);
			if (eventoRegimeDt == null) stRetorno += "Não existe regime atual para este sentenciado ou Sentenciado não iniciou o cumprimento da pena!";
		}

		if (getPrimeiraDataBase(listaEvento).length() == 0) 
			stRetorno += "\nNão existe evento de data base.";

		if (calculoLiquidacaoDt.getListaTipoCalculo() != null && stRetorno.length() == 0) {
			for (int i = 0; i < calculoLiquidacaoDt.getListaTipoCalculo().size(); i++) {
				if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("PROGRESSAO")) {
					if ((calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt() == null 
							|| calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataBaseProgressao().length() == 0)
							 && !calculoLiquidacaoDt.getCalcularSomente().equalsIgnoreCase("LC")) 
						stRetorno += "Selecione a Data Base para Cálculo da Progressão de Regime! \n";
					if (!possuiDataInicioCumprimentoPenaPreenchida(listaEvento)) 
						stRetorno += "Existe \"Trânsito em Julgado\" ou \"Guia de Recolhimento Provisória\" sem DICC (Data de Início de Cumprimento da Condenação). Verifique!\n";
//						if (calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getForcarCalculoLC().length() > 0 && calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getNovoRegimeProgressao().length() == 0) stRetorno += "Informe o novo regime para o cálculo da Progressão!\n";

				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("INDULTO")) {
					if (calculoLiquidacaoDt.getCalculoIndultoDt() == null 
							|| (calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoComum().length() == 0
									&& calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoHediondo().length() == 0)
							) stRetorno += "Selecionade uma fração para o cálculo do Indulto.";
				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("COMUTACAO") || ((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("COMUTACAO_UNIFICADA")) {
					if (calculoLiquidacaoDt.getCalculoComutacaoDt() == null || calculoLiquidacaoDt.getCalculoComutacaoDt().getIdParametroComutacao().length() == 0) stRetorno += "Selecionade o decreto presidencial para o cálculo da Comutação Prévia.";
				}
			}
		}

		return stRetorno;
	}

	/**
	 * verifica se todos os TJ possuem data de início de cumprimento da pena.
	 * 
	 * @param listaEvento
	 * @return
	 */
	private boolean possuiDataInicioCumprimentoPenaPreenchida(List listaEvento) {
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
					|| evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
				if (evento.getDataInicioCumpirmentoPena().length() == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Verifica se o ano informado para consulta da saida temporaria é valido.
	 * 
	 * @param o
	 *            primeiro evento da lista de eventos
	 * @author kbsriccioppo
	 * @return
	 */
	public String VerificarSaidaTemporaria(ProcessoEventoExecucaoDt dados, String anoInformado) {
		String stRetorno = "";
		if (anoInformado.length() == 0) {
			stRetorno = "Informe o ano!";
		} else {
			if (dados.getDataInicio().length() > 0) {
				if (Funcoes.StringToInt(anoInformado) < Funcoes.StringToInt(dados.getDataInicio().substring(6, 10))) {
					stRetorno += "Verifique! Não existe Processo de Execução no Ano informado.";
				}
			}
		}
		return stRetorno;
	}

	/**
	 * Consulta o último evento do processo informado e dos tipos de eventos informados
	 * 
	 * @param id_processo
	 *            , identificação do processo
	 * @param lista
	 *            com os tipos dos eventos
	 * @return o último evento
	 * @author kbsriccioppo
	 */
	public List VerificarListaLCeRLC(String id_processo, List listaTipoEvento) throws Exception {

		List dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarUltimoEvento(id_processo, listaTipoEvento);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * recupera o saldo de dias ou de horas de estudo
	 * 
	 * @param id_processo
	 *            , data início do evento
	 * @param lista
	 *            com os tipos dos eventos
	 * @return o saldo
	 * @author kbsriccioppo
	 */
	public int VerificarSaldoDiasTrabalhadosHorasEstudo(String id_processo, String dataInicio, List listaTipoEvento) throws Exception {
		int retorno = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.calcularSaldo(id_processo, dataInicio, listaTipoEvento);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}

	/**
	 * Verifica se na lista de eventos não possui o evento Progressão de Regime.
	 * 
	 * @param listaEvento
	 * @author wcsilva
	 * @return
	 */
	public boolean isPrimeiraProgressao(List listaEvento) {
		boolean boRetorno = true;

		// verifica se é o cálculo da primeira progressão
		if (listaEvento != null) {
			for (int i = 0; i < listaEvento.size(); i++) {
				if (((ProcessoEventoExecucaoDt) listaEvento.get(i)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PROGRESSAO_REGIME))) boRetorno = false;
			}
		}
		return boRetorno;
	}

	/**
	 * Verifica se o sentenciado iniciou o cumprimento da pena.
	 * 
	 * @param listaEvento
	 * @author wcsilva
	 * @return
	 */
	public boolean isIniciouCumprimentoPena(List listaEvento) {
		boolean boRetorno = false;

		if (listaEvento != null) {
			for (int i = 0; i < listaEvento.size(); i++) {
				if (((ProcessoEventoExecucaoDt) listaEvento.get(i)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME)) || ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO))) boRetorno = true;
			}
		}
		return boRetorno;
	}

	/**
	 * lista os Id_ProcessoEventoExecução dos TJ e GRP do processo e processo dependente
	 * 
	 * @param id_Processo
	 *            : identificação do processo
	 * @return lista<ProcessoEventoExecucaoDt> com id's do processoEventoExecução referente à TJ ou GRP
	 * @throws Exception
	 */
	public List listarIdProcessoEventoExecucao_TJ_GRP(String id_Processo) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarIdProcessoEventoExecucao_TJ_GRP(id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}

	/**
	 * Monta a lista de eventos: calcula o tempo total das condenações e o tempo cumprido. Preenche a lista de condenações extintas
	 * 
	 * @param lista
	 *            , lista dos eventos
	 * @param usuarioDt
	 *            , identificação do usuário
	 * @param listaCondenacaoExtinta
	 *            , lista que será alimentada com as condenacoes extintas.
	 * @author wcsilva
	 * @throws Exception
	 */
	public void montarListaEventos(List listaEvento, UsuarioDt usuarioDt, List listaCondenacaoExtinta, String id_Processo, int tempoHorasEstudo) throws Exception {
		try{
			int dias = 0;
			boolean listaMovimentacao = false;

			// significa que é a lista dos eventos da movimentação, portanto
			// deve listar todos os TJ do processo (listaTJ)
			if (listaCondenacaoExtinta == null) {
				listarIdProcessoEventoExecucao_TJ_GRP(id_Processo);
				listaMovimentacao = true;
			} else
				listaCondenacaoExtinta.removeAll(listaCondenacaoExtinta);

			// INÍCIO DA MONTAGEM DOS EVENTOS
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);

//				processoEventoExecucaoDt.setObservacaoVisualizada(processoEventoExecucaoDt.getObservacao());
				if (!listaMovimentacao) {
					// ajusta a visualização do regime
					if (
							(processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INFORMATIVO))
									&& !processoEventoExecucaoDt.getEventoExecucaoDt().getAlteraRegime().equalsIgnoreCase("s")
//							&& !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REGRESSAO_CAUTELAR))
//							&& !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_CAUTELAR_LC))
//							&& !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PRIVATIVA_LIBERDADE_CAUTELAR))
							)
							
							&& !isEventoPenaRestritivaDireito(processoEventoExecucaoDt.getEventoExecucaoDt().getId()) 
							&& !processoEventoExecucaoDt.getEventoRegimeDt().getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))
							) {
						processoEventoExecucaoDt.getEventoRegimeDt().setRegimeExecucao("");
					}
				}

				// calcula tempo total das condenações e ajusta a descrição do evento
				if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
						|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {

					List listaCondenacao = new CondenacaoExecucaoNe().listarCondenacaoDaAcaoPenal(processoEventoExecucaoDt.getId_ProcessoExecucao());
					String tempoCondenacaoNaoExtinta = "";
					String tempoExtintoCumprimento = "";
					List listaTempoCondenacaonaoExtinta = new ArrayList();
					List listaTempoExtintoCumprimento = new ArrayList();
//					double tempoExtintoIndulto = 0;

					for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacao) {
						condenacao.setDataTransitoJulgado(processoEventoExecucaoDt.getDataInicio());
						if (condenacao.getTempoCumpridoExtintoDias().length() > 0) {
							listaTempoExtintoCumprimento.add(Funcoes.converterParaAnoMesDia(Integer.parseInt(condenacao.getTempoCumpridoExtintoDias())));		
						}
						
						switch (Funcoes.StringToInt(condenacao.getId_CondenacaoExecucaoSituacao())) {
						case CondenacaoExecucaoSituacaoDt.NAO_APLICA:
							listaTempoCondenacaonaoExtinta.add(Funcoes.converterParaAnoMesDia(Integer.parseInt(condenacao.getTempoPenaEmDias())));
							break;
						//considera o tempo extinto da condenação como tempo cumprido
						case CondenacaoExecucaoSituacaoDt.EXTINTA_CUMPRIMENTO: 
							if (listaCondenacaoExtinta != null) listaCondenacaoExtinta.add(condenacao);
							listaTempoExtintoCumprimento.add(Funcoes.converterParaAnoMesDia(Integer.parseInt(condenacao.getTempoPenaEmDias())));
							break;
						case CondenacaoExecucaoSituacaoDt.EXTINTA_PRESCRICAO:
							if (listaCondenacaoExtinta != null) listaCondenacaoExtinta.add(condenacao);
							break;
						//considera a fração da condenação como tempo cumprido
						case CondenacaoExecucaoSituacaoDt.EXTINTA_INDULTO:
							if (listaCondenacaoExtinta != null) listaCondenacaoExtinta.add(condenacao);
							break;
						}
					}
					tempoCondenacaoNaoExtinta = somarAnoMesDia(listaTempoCondenacaonaoExtinta);
					processoEventoExecucaoDt.setCondenacaoDias(tempoCondenacaoNaoExtinta);
					tempoExtintoCumprimento = somarAnoMesDia(listaTempoExtintoCumprimento);
					
					if (Funcoes.StringToInt(tempoExtintoCumprimento) > 0) {// considera o tempo extinto como tempo cumprido
						processoEventoExecucaoDt.setTempoCumpridoDias(String.valueOf(tempoExtintoCumprimento));
						processoEventoExecucaoDt.setTempoCumpridoAnos_TempoEmAnos("(" + processoEventoExecucaoDt.getTempoCumpridoAnos() + ")");
					}

					String descricaoEvento = processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao();
					// verifica se a descrição abaixo já foi adicionada à descrição do evento
					if (!descricaoEvento.substring(descricaoEvento.length() - 1, descricaoEvento.length()).equals(")")) {
						if (processoEventoExecucaoDt.getDataInicioCumpirmentoPena() != null && processoEventoExecucaoDt.getDataInicioCumpirmentoPena().length() > 0) {
							descricaoEvento += "<br />(DICC* " + processoEventoExecucaoDt.getDataInicioCumpirmentoPena() + ")";
						}
						if (processoEventoExecucaoDt.getCondenacaoDias().equals("0")) {
							if (processoEventoExecucaoDt.getEventoRegimeDt().getRegimeExecucaoDt().getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))) descricaoEvento += " (Medida de Segurança)";
							else
								descricaoEvento += " (extinto)";
						}
						processoEventoExecucaoDt.getEventoExecucaoDt().setEventoExecucao(descricaoEvento);
						processoEventoExecucaoDt.setEventoExecucao(descricaoEvento);
						
						if (processoEventoExecucaoDt.getObservacaoAux().length() > 0 && processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
							processoEventoExecucaoDt.getEventoExecucaoDt().setEventoExecucao(processoEventoExecucaoDt.getEventoExecucao() + "<br /><b>(Trânsito em Julgado: " + processoEventoExecucaoDt.getObservacaoAux() + ")");
						}
					}

					
				} else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))) {
					processoEventoExecucaoDt.setCondenacaoDias(processoEventoExecucaoDt.getQuantidade());
					processoEventoExecucaoDt.setCondenacaoAnosComutacao(processoEventoExecucaoDt.getQuantidade());
//					processoEventoExecucaoDt.setObservacaoVisualizada(processoEventoExecucaoDt.getObservacao() + processoEventoExecucaoDt.getObservacaoAux());
				}

				// caso não seja para considerar o tempo do Livramento
				// Condicional, zerar o tempo cumprido do LC correspondente à
				// Revogação
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL)) 
						|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))) {
					// 0: Não (fato novo) - 1/1, zera o tempo cumprido do livramento condicional
					// 1: Sim (Crime anterior ao LC) - fração, considera o tempo cumprido do livramento condicional
					// 2: Sim (por descumprimento) - 1/1, considera o tempo cumprido do livramento condicional
					if (processoEventoExecucaoDt.getConsiderarTempoLivramentoCondicional().equals("0")) {	
						for (int w = listaEvento.size() - 1; w >= 0; w--) {
							// localiza o Id_LivramentoCondicional indicado no evento REVOGAÇÃO na lista de eventos, e zera o tempo cumprido do LC indicado
							if (processoEventoExecucaoDt.getId_LivramentoCondicional().equals(((ProcessoEventoExecucaoDt) listaEvento.get(w)).getId())) {
								((ProcessoEventoExecucaoDt) listaEvento.get(w)).setTempoCumpridoDias("0");

								// zera o tempo cumprido do LC até a prisão indicada no evento de revogação
								for (int s = w + 1; s < listaEvento.size(); s++) {
									if (((ProcessoEventoExecucaoDt) listaEvento.get(s)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))) {
										if (((ProcessoEventoExecucaoDt) listaEvento.get(s)).getDataInicio().equals(processoEventoExecucaoDt.getDataPrisaoRevogacaoLC())) {
											break;
										}
										((ProcessoEventoExecucaoDt) listaEvento.get(s)).setTempoCumpridoDias("0");
									}
								}
								break;
							}
						}
					}
				}

				// calcula tempo cumprido dos eventos do tipo "genérico"
				if (processoEventoExecucaoDt.getDataFim() != null && processoEventoExecucaoDt.getDataFim().length() > 0 && processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))) {
					dias = Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), processoEventoExecucaoDt.getDataFim()) + 1;
					if (dias > 0) processoEventoExecucaoDt.setTempoCumpridoDias(String.valueOf(dias));
					else
						processoEventoExecucaoDt.setTempoCumpridoDias("0");
				}

				// informa o tempo cumprido do evento "Tempo Cumprido PRD"
				if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TEMPO_CUMPRIDO_PRD))) {
					if (processoEventoExecucaoDt.getQuantidade().length() > 0) processoEventoExecucaoDt.setTempoCumpridoDias(processoEventoExecucaoDt.getQuantidade());
					else
						processoEventoExecucaoDt.setTempoCumpridoDias("0");
				}
				// informa o tempo cumprido do evento "Falta" e "Falta LFS"
				if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA)) || processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA_LFS))) {
					if (processoEventoExecucaoDt.getQuantidade().length() > 0) processoEventoExecucaoDt.setTempoCumpridoDias(processoEventoExecucaoDt.getQuantidade());
					else
						processoEventoExecucaoDt.setTempoCumpridoDias("0");
					processoEventoExecucaoDt.setTempoCumpridoAnos_TempoEmAnos("(" + processoEventoExecucaoDt.getTempoCumpridoAnos() + ")");
				}

				// informa o tempo cumprido dos eventos do tipo "remição"
				if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))) {
					String tempoCumprido = "";
					if (processoEventoExecucaoDt.getQuantidade().length() > 0) {
						// eventos de remição
						if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.DIAS_TRABALHADOS_REMICAO)) 
								|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_TRABALHADOS_REMICAO)))
							tempoCumprido = String.valueOf((int) Math.ceil((double) Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade()) * 1 / 3));
						else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.HORAS_ESTUDO_REMICAO)) 
								|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMICAO)))
							tempoCumprido = String.valueOf((int) Math.ceil((double) Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade()) * 1 / tempoHorasEstudo));
						else 			
							tempoCumprido = processoEventoExecucaoDt.getQuantidade();
					}
					if (tempoCumprido.length() > 0) processoEventoExecucaoDt.setTempoCumpridoDias(tempoCumprido);
					else
						processoEventoExecucaoDt.setTempoCumpridoDias("0");

					if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_TRABALHADOS_REMICAO)) 
							|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMICAO))
							|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_LEITURA_REMIDO))
							|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMIDO))) {
						processoEventoExecucaoDt.setTempoCumpridoAnos_TempoEmAnos("(" + processoEventoExecucaoDt.getTempoCumpridoAnos() + ")");
					}
				}

				// ajusta a descrição do evento: adiciona a quantidade na descrição do evento
				if ( processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO)) 
						|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS)) 
						|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TEMPO_CUMPRIDO_PRD))
						|| isModalidade(processoEventoExecucaoDt.getEventoExecucaoDt().getId())
					) {
					if (processoEventoExecucaoDt.getQuantidade().length() > 0) {
						String descricaoEvento = processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao();

						// verifica se a descrição abaixo já foi adicionada à descrição do evento
						if (!descricaoEvento.substring(descricaoEvento.length() - 1, descricaoEvento.length()).equals(")")
								//OS EVENTOS DE REMIÇÃO TEM NA DESCRIÇÃO "(remição)"
								|| descricaoEvento.substring(descricaoEvento.length() - 2, descricaoEvento.length()).equalsIgnoreCase("o)")) {
							if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.HORAS_ESTUDO_REMICAO)) 
									|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMICAO)) 
									|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE))) 
								descricaoEvento += "<br />(" + processoEventoExecucaoDt.getQuantidade() + " horas)";
							else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS))
									|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS))
									|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.LIMITACAO_FIM_SEMANA))){
								descricaoEvento += "<br />(" + processoEventoExecucaoDt.getQuantidadeAnos() + " a-m-d";
								if (processoEventoExecucaoDt.getDataInicioSursis().length() > 0){
									descricaoEvento += ". Início em: " + processoEventoExecucaoDt.getDataInicioSursis() + ")";
								} else descricaoEvento += ")";
							}
							else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PRESTACAO_PECUNIARIA))) 
								descricaoEvento += "<br />(" + Funcoes.FormatarMoeda(processoEventoExecucaoDt.getQuantidade()) + ")";
							else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.CESTA_BASICA))) 
								descricaoEvento += "<br />(" + processoEventoExecucaoDt.getQuantidade() + " cestas)";
							else
								descricaoEvento += "<br />(" + processoEventoExecucaoDt.getQuantidade() + " dias)";
							processoEventoExecucaoDt.getEventoExecucaoDt().setEventoExecucao(descricaoEvento);
						}
					}
				}

				if (usuarioDt != null) {
					processoEventoExecucaoDt.setId_UsuarioLog(usuarioDt.getId());
					processoEventoExecucaoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
					processoEventoExecucaoDt.getEventoLocalDt().setId_UsuarioLog(usuarioDt.getId());
					processoEventoExecucaoDt.getEventoLocalDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					processoEventoExecucaoDt.getEventoRegimeDt().setId_UsuarioLog(usuarioDt.getId());
					processoEventoExecucaoDt.getEventoRegimeDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					processoEventoExecucaoDt.getEventoExecucaoDt().setId_UsuarioLog(usuarioDt.getId());
					processoEventoExecucaoDt.getEventoExecucaoDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
				}

				// informa se a partir deste evento, acessa os dados da ação penal
				processoEventoExecucaoDt.setManterAcaoPenal(isEventoManterAcaoPenal(processoEventoExecucaoDt.getEventoExecucaoDt().getId()));

			} // FIM DA MONTAGEM DOS EVENTOS
		} catch(MensagemException m) {
			throw new MensagemException(m.getMessage());
		
		}
	}

	/**
	 * Monta a lista de eventos e a lista de evento de histórico de cumprimento das penas restritivas de direito.
	 * 
	 * @param listaEvento
	 *            : lista com todos os eventos do processo
	 * @return map: com as chaves listaEvento e listaHistorico
	 * @throws Exception 
	 */
	public HashMap separarLista_Evento_HistoricoPRD(List listaEvento, UsuarioDt usuarioDt) throws Exception{
		HashMap map = new HashMap();
	
		// lista com apenas os eventos de histórico de cumprimento da pena restritiva de direito
		List listaHistoricoPsc = null; // prestação de serviço à comunidade
		List listaHistoricoPec = null; // prestação pecuniária
		List listaHistoricoLfs = null; // limitação de fim de semana
		List listaHistoricoItd = null; // interdição temporária de direito
		List listaHistoricoPcb = null; // cestas básicas

		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.ALTERACAO_MODALIDADE))) {
			    listaHistoricoPsc = null; 
			    listaHistoricoPec = null;
			    listaHistoricoLfs = null;
			    listaHistoricoItd = null;
			    listaHistoricoPcb = null; 				    
			}else if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.HISTORICO_LFS))) {
				if (usuarioDt != null) {
					evento.setId_UsuarioLog(usuarioDt.getId());
					evento.setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoLocalDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoLocalDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoRegimeDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoRegimeDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoExecucaoDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoExecucaoDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
				}
				if (listaHistoricoLfs == null) listaHistoricoLfs = new ArrayList();
				listaHistoricoLfs.add(evento);
				listaEvento.remove(evento);
				i--;

			} else if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.HISTORICO_PSC))) {
				if (usuarioDt != null) {
					evento.setId_UsuarioLog(usuarioDt.getId());
					evento.setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoLocalDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoLocalDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoRegimeDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoRegimeDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoExecucaoDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoExecucaoDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
				}
				// ajusta a descrição do evento: adiciona a quantidade na descrição do evento
				if (evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.HORAS_CUMPRIDAS_PSC))) {
					if (evento.getQuantidade().length() > 0) {
						String descricaoEvento = evento.getEventoExecucaoDt().getEventoExecucao() + " (" + evento.getQuantidade() + " horas)";
						evento.getEventoExecucaoDt().setEventoExecucao(descricaoEvento);
					}
				}
				if (listaHistoricoPsc == null) listaHistoricoPsc = new ArrayList();
				listaHistoricoPsc.add(evento);
				listaEvento.remove(evento);
				i--;
				
			} else if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.HISTORICO_PEC))) {
				if (usuarioDt != null) {
					evento.setId_UsuarioLog(usuarioDt.getId());
					evento.setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoLocalDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoLocalDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoRegimeDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoRegimeDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoExecucaoDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoExecucaoDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
				}
				if (listaHistoricoPec == null) listaHistoricoPec = new ArrayList();
				listaHistoricoPec.add(evento);
				listaEvento.remove(evento);
				i--;
			}
			else if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.HISTORICO_ITD))) {
				if (usuarioDt != null) {
					evento.setId_UsuarioLog(usuarioDt.getId());
					evento.setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoLocalDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoLocalDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoRegimeDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoRegimeDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoExecucaoDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoExecucaoDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
				}
				if (listaHistoricoItd == null) listaHistoricoItd = new ArrayList();
				listaHistoricoItd.add(evento);
				listaEvento.remove(evento);
				i--;

			}
			else if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.HISTORICO_PCB))) {
				if (usuarioDt != null) {
					evento.setId_UsuarioLog(usuarioDt.getId());
					evento.setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoLocalDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoLocalDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoRegimeDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoRegimeDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
					evento.getEventoExecucaoDt().setId_UsuarioLog(usuarioDt.getId());
					evento.getEventoExecucaoDt().setIpComputadorLog(usuarioDt.getIpComputadorLog());
				}
				if (listaHistoricoPcb == null) listaHistoricoPcb = new ArrayList();
				listaHistoricoPcb.add(evento);
				listaEvento.remove(evento);
				i--;
			}
		}
		// calcula a data fim
		if (listaHistoricoLfs != null) listaHistoricoLfs = this.calcularDataFim(listaHistoricoLfs);
		if (listaHistoricoPsc != null) listaHistoricoPsc = this.calcularDataFim(listaHistoricoPsc);
		if (listaHistoricoItd != null) listaHistoricoItd = this.calcularDataFim(listaHistoricoItd);

		if (listaHistoricoLfs != null) {
			for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaHistoricoLfs) {
				// calcula tempo cumprido dos eventos do tipo "genérico" a lista lfs
				if (evento.getDataFim() != null && evento.getDataFim().length() > 0 && isEventoPRD_Generico(evento.getId_EventoExecucao())) {
					int dias = Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), evento.getDataFim()) + 1;
					if (dias > 0) evento.setTempoCumpridoDias(String.valueOf(dias));
					else
						evento.setTempoCumpridoDias("0");
				}
			}
		}
		if (listaHistoricoItd != null) {
			for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaHistoricoItd) {
				// calcula tempo cumprido dos eventos do tipo "genérico" a lista lfs
				if (evento.getDataFim() != null && evento.getDataFim().length() > 0 && isEventoPRD_Generico(evento.getId_EventoExecucao())) {
					int dias = Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), evento.getDataFim()) + 1;
					if (dias > 0) evento.setTempoCumpridoDias(String.valueOf(dias));
					else
						evento.setTempoCumpridoDias("0");
				}
			}
		}

		map.put("listaEventos", listaEvento);
		map.put("listaHistoricoPsc", listaHistoricoPsc);
		map.put("listaHistoricoPec", listaHistoricoPec);
		map.put("listaHistoricoLfs", listaHistoricoLfs);
		map.put("listaHistoricoItd", listaHistoricoItd);
		map.put("listaHistoricoPcb", listaHistoricoPcb);

		return map;
	}

	/**
	 * verifica se o evento de PRD (Pena Restritiva de Direito) é do tipo Genérico, ou seja, fecha data e conta como tempo cumprido
	 * 
	 * @param idEvento
	 *            : id do Evento
	 * @return
	 * @throws Exception
	 */
	public boolean isEventoPRD_Generico(String idEventoExecucao){
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.INICIO_LFS)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.RETORNO_CUMPRIMENTO_LFS)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.INICIO_PSC)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.RETORNO_CUMPRIMENTO_PSC))
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.INICIO_ITD))
				) {
			return true;
		} else
			return false;
	}

	/**
	 * verifica se o evento de PRD (Pena Restritiva de Direito) é do tipo Interrupção, ou seja, fecha data e NÃO conta como tempo cumprido
	 * 
	 * @param idEvento
	 *            : id do Evento
	 * @return
	 * @throws Exception
	 */
	public boolean isEventoPRD_Interrupcao(String idEventoExecucao){
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.INTERRUPCAO_CUMPRIMENTO_LFS)) || idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.INTERRUPCAO_CUMPRIMENTO_PSC))) {
			return true;
		} else
			return false;
	}

	/**
	 * verifica se o evento de PRD (Pena Restritiva de Direito) é do tipo Informativo, ou seja, não fecha data
	 * 
	 * @param idEvento
	 *            : id do Evento
	 * @return
	 * @throws Exception
	 */
	public boolean isEventoPRD_Informativo(String idEventoExecucao){
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.HORAS_CUMPRIDAS_PSC))) {
			return true;
		} else
			return false;
	}

	/**
	 * Método utilizado no cadastro de processo para salvar os eventos da lista (com os regimes e locais de cumprimento de pena, se necessário)
	 * 
	 * @param listaEventos
	 *            : lista de Eventos a serem salvos (ProcessoEventoExecucaoDt)
	 * @param obFabricaConexao
	 *            : conexão
	 * @param logDt
	 *            : objeto com os dados do usuário e computador
	 * @author wcsilva
	 */
	public void salvarListaEvento(List listaEventos, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		// Salvando eventos da lista
		if (listaEventos != null) {
			for (int i = 0; i < listaEventos.size(); i++) {
				ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEventos.get(i);
				salvarEventoCompleto(processoEventoExecucaoDt, logDt, obFabricaConexao);
			}
		}
	}

	/**
	 * Salva o evento, evento local e evento regime.
	 * 
	 * @param processoEventoExecucaoDt
	 *            : evento
	 * @param logDt
	 *            : objeto log
	 * @param obFabricaConexao
	 *            : objeto de conexão
	 * @throws Exception
	 */
	public void salvarEventoCompleto(ProcessoEventoExecucaoDt processoEventoExecucaoDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
			processoEventoExecucaoDt.setId_UsuarioLog(logDt.getId_Usuario());
			processoEventoExecucaoDt.setIpComputadorLog(logDt.getIpComputador());
			
			this.salvar(processoEventoExecucaoDt, obFabricaConexao);

			// criar obj e salvar local cumprimento pena (EventoLocalDt)
			if (processoEventoExecucaoDt.getEventoLocalDt().getId_LocalCumprimentoPena().length() > 0) {
				EventoLocalDt eventoLocalDt = processoEventoExecucaoDt.getEventoLocalDt();
				eventoLocalDt.setId_ProcessoEventoExecucao(processoEventoExecucaoDt.getId());
				new EventoLocalNe().salvar(processoEventoExecucaoDt.getEventoLocalDt(), obFabricaConexao, logDt);
			}

			// criar obj e salvar regime (EventoRegimeDt)
			if (processoEventoExecucaoDt.getEventoRegimeDt().getId_RegimeExecucao().length() > 0) {
				EventoRegimeDt eventoRegimeDt = processoEventoExecucaoDt.getEventoRegimeDt();
				eventoRegimeDt.setId_ProcessoEventoExecucao(processoEventoExecucaoDt.getId());
				new EventoRegimeNe().salvar(processoEventoExecucaoDt.getEventoRegimeDt(), obFabricaConexao, logDt);
			}
	}

	/**
	 * Trata, na última movimentação, os eventos: prisão provisória, liberdade provisória e primeiro regime, se eles forem após o primeiro trânsito em julgado.
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param obFabricaConexao
	 *            , conexão
	 * @throws Exception
	 * @author wcsilva
	 */
	public void corrigirPrisaoProvisoria(String id_Processo, String idUsuarioServentia, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		List lista = null;
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());

		lista = obPersistencia.listarEventos(id_Processo, "");

		String dataPrimeiroTJ = null;
		int id_ultimaMovimentacao = 0;

		for (int i = 0; i < lista.size(); i++) {
			// captura id da ultima movimentacao
			if (id_ultimaMovimentacao < Funcoes.StringToInt(((ProcessoEventoExecucaoDt) lista.get(i)).getId_Movimentacao())) id_ultimaMovimentacao = Funcoes.StringToInt(((ProcessoEventoExecucaoDt) lista.get(i)).getId_Movimentacao());

			// captura menor data de trânsito em julgado
			if (((ProcessoEventoExecucaoDt) lista.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) || ((ProcessoEventoExecucaoDt) lista.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
				if (dataPrimeiroTJ == null) dataPrimeiroTJ = ((ProcessoEventoExecucaoDt) lista.get(i)).getDataInicio();
				else if (Funcoes.StringToDate(dataPrimeiroTJ).after(Funcoes.StringToDate(((ProcessoEventoExecucaoDt) lista.get(i)).getDataInicio()))) dataPrimeiroTJ = ((ProcessoEventoExecucaoDt) lista.get(i)).getDataInicio();
			}
		}

		// verifica se a data do TJ da última movimentação é maior que a
		// data do primeiro TJ
		boolean corrigirEventos = false;
		for (int i = 0; i < lista.size(); i++) {
			if (id_ultimaMovimentacao == Funcoes.StringToInt(((ProcessoEventoExecucaoDt) lista.get(i)).getId_Movimentacao())) {
				if (((ProcessoEventoExecucaoDt) lista.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) || ((ProcessoEventoExecucaoDt) lista.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
					if (Funcoes.StringToDate(dataPrimeiroTJ).before(Funcoes.StringToDate(((ProcessoEventoExecucaoDt) lista.get(i)).getDataInicio()))) {
						corrigirEventos = true;
					}
				}
			}
		}

		if (corrigirEventos) {
			for (int i = 0; i < lista.size(); i++) {
				ProcessoEventoExecucaoDt peeDt = (ProcessoEventoExecucaoDt) lista.get(i);

				boolean possuiLiberdadeProvisoria = false;
				boolean possuiPrisaoProvisoria = false;

				if (id_ultimaMovimentacao == Funcoes.StringToInt(((ProcessoEventoExecucaoDt) lista.get(i)).getId_Movimentacao())) {
					peeDt.setId_UsuarioLog(logDt.getId_Usuario());
					peeDt.setIpComputadorLog(logDt.getIpComputador());
					peeDt.setId_UsuarioServentia(idUsuarioServentia);

					// altera prisão provisória para prisão
					if (peeDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRISAO_PROVISORIA))) {
						peeDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PRISAO));
						salvar(peeDt, obFabricaConexao);
						possuiPrisaoProvisoria = true;
					}
					// altera liberdade provisória para fuga
					if (peeDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INTERRUPCAO_PRISAO_PROVISORIA))) {
						peeDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.FUGA));
						salvar(peeDt, obFabricaConexao);
						possuiLiberdadeProvisoria = true;
					}
					// altera ou exclui primeiro regime
					if (peeDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME))) {
						// altera primeiro regime para prisão
						if (!possuiPrisaoProvisoria || possuiLiberdadeProvisoria) {
							// excluirEventoRegime(peeDt.getEventoRegimeDt(),
							// logDt, obFabricaConexao);
							peeDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PRISAO));
							salvar(peeDt, obFabricaConexao);
							// exclui o primeiro regime
						} else
							excluir(peeDt, obFabricaConexao);
					}
					i++;
				}
			}
		}
			
	}

	/**
	 * Exclui os dados da tabela EventoRegime.
	 * 
	 * @param eventoRegimeDt
	 *            , identificação do eventoRegime.
	 * @param logDt
	 *            , identificação do usuário e IP
	 * @param obFabricaConexao
	 *            , conexão
	 * @author wcsilva
	 */
	public void excluirEventoRegime(EventoRegimeDt eventoRegimeDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		eventoRegimeDt.setId_UsuarioLog(logDt.getId_Usuario());
		eventoRegimeDt.setIpComputadorLog(logDt.getIpComputador());
		new EventoRegimeNe().excluir(eventoRegimeDt, obFabricaConexao);
		
	}

	/**
	 * Exclui os dados da tabela EventoLocal.
	 * 
	 * @param eventoLocalDt
	 *            , identificação do eventoLocal.
	 * @param logDt
	 *            , identificação do usuário e IP
	 * @param obFabricaConexao
	 *            , conexão
	 * @author wcsilva
	 */
	public void excluirEventoLocal(EventoLocalDt eventoLocalDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		eventoLocalDt.setId_UsuarioLog(logDt.getId_Usuario());
		eventoLocalDt.setIpComputadorLog(logDt.getIpComputador());
		new EventoLocalNe().excluir(eventoLocalDt, obFabricaConexao);

	}

	/**
	 * sobrescreve o método do gerador, considerando a conexão
	 * 
	 * @param dados
	 * @param obFabricaConexao
	 * @throws Exception
	 * @author wcsilva
	 */
	public void salvar(ProcessoEventoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().equalsIgnoreCase("")) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoEventoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoEventoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * consulta o Id_ProcessoEventoExecucao, tendo como filtro o Id_ProcessoExecucao e Id_EventoExecucao
	 * 
	 * @param Id_ProcessoExecucao
	 *            : idProcessoExecucao relacionado ao id_ProcessoEventoExecucaoPsque será consultado
	 * @param Id_EventoExecucao
	 *            : idEventoExecucao relacionado ao id_ProcessoEventoExecucaoPsque será consultado
	 * @return Id_ProcessoEventoExecucao
	 * @throws Exception
	 * @author wcsilva
	 */
	public String consultarId_ProcessoEventoExecucao(String idProcessoExecucao, String idEventoExecucao) throws Exception {
		String idProcessoEventoExecucao = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			idProcessoEventoExecucao = consultarId_ProcessoEventoExecucao(idProcessoExecucao, idEventoExecucao, obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return idProcessoEventoExecucao;
	}

	/**
	 * consulta o Id_ProcessoEventoExecucao, tendo como filtro o Id_ProcessoExecucao e Id_EventoExecucao
	 * 
	 * @param Id_ProcessoExecucao
	 *            : idProcessoExecucao relacionado ao id_ProcessoEventoExecucaoPsque será consultado
	 * @param Id_EventoExecucao
	 *            : idEventoExecucao relacionado ao id_ProcessoEventoExecucaoPsque será consultado
	 * @param FabricaConexao
	 *            : objeto de conexão
	 * @return Id_ProcessoEventoExecucao
	 * @throws Exception
	 * @author wcsilva
	 */
	public String consultarId_ProcessoEventoExecucao(String idProcessoExecucao, String idEventoExecucao, FabricaConexao obFabricaConexao) throws Exception {
		String idProcessoEventoExecucao = "";
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
		idProcessoEventoExecucao = obPersistencia.consultarId_ProcessoEventoExecucao(idProcessoExecucao, idEventoExecucao);
		
		return idProcessoEventoExecucao;
	}

	/**
	 * consulta o ProcessoEventoExecucao, tendo como filtro o Id_ProcessoExecucao e Id_EventoExecucao
	 * 
	 * @param Id_ProcessoExecucao
	 *            : idProcessoExecucao relacionado ao id_ProcessoEventoExecucaoPsque será consultado
	 * @param Id_EventoExecucao
	 *            : idEventoExecucao relacionado ao id_ProcessoEventoExecucaoPsque será consultado
	 * @return ProcessoEventoExecucao
	 * @throws Exception
	 * @author wcsilva
	 */
	public ProcessoEventoExecucaoDt consultarProcessoEventoExecucao(String idProcessoExecucao, String idEventoExecucao) throws Exception {
		ProcessoEventoExecucaoDt processoEventoExecucaoDt = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			processoEventoExecucaoDt = obPersistencia.consultarProcessoEventoExecucao(idEventoExecucao, idProcessoExecucao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return processoEventoExecucaoDt;
	}

	public ProcessoEventoExecucaoDt consultarProcessoEventoExecucao(String idProcessoExecucao, String idEventoExecucao, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoEventoExecucaoDt processoEventoExecucaoDt = null;

		try{
			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			processoEventoExecucaoDt = obPersistencia.consultarProcessoEventoExecucao(idEventoExecucao, idProcessoExecucao);
		
		} finally{
		}
		return processoEventoExecucaoDt;
	}

	public ProcessoEventoExecucaoDt consultarProcessoEventoExecucaoCompleto(String idProcessoExecucao, String idEventoExecucao, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoEventoExecucaoDt processoEventoExecucaoDt = null;

		try{
			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			processoEventoExecucaoDt = obPersistencia.consultarProcessoEventoExecucaoCompleto(idEventoExecucao, idProcessoExecucao);
		
		} finally{
		}
		return processoEventoExecucaoDt;
	}

	/**
	 * Exclui a condenação ou uma lista de condenações
	 * 
	 * @param condenacaoExecucaodt
	 * @throws Exception
	 * @author wcsilva
	 */
	public void excluirCondenacao(CondenacaoExecucaoDt condenacaoExecucaodt, List listaCondenacao) throws Exception {
		
		CondenacaoExecucaoNe condenacaoExecucaoNe = new CondenacaoExecucaoNe();
		if (condenacaoExecucaodt != null) condenacaoExecucaoNe.excluir(condenacaoExecucaodt);
		if (listaCondenacao != null) condenacaoExecucaoNe.excluirListaCondenacao(listaCondenacao);

	}

	/**
	 * Lista os eventos a partir do id do processo, id da movimentação. Valida para a Prisão Provisória e Interrupção da Prisão Provisória para estar sempre antes do Trânsito em Julgado.
	 * 
	 * @param id_processo
	 *            , identificação do processo
	 * @param id_movimentacao
	 *            , identificação da movimentação
	 * @param obFabricaConexao
	 *            , conexão com o banco
	 * @return lista com os eventos
	 * @author wcsilva
	 */
	public List listarEventos(String id_processo, String id_movimentacao, FabricaConexao obFabricaConexao) throws Exception {
		List lista = null;
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
		lista = obPersistencia.listarEventos(id_processo, id_movimentacao);

		if (lista != null && lista.size() > 0) {
			int posicaoTJ = 0;
			String dataTJ = "";

			for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>)lista) {
				evento.setObservacaoVisualizada(evento.getObservacao() + evento.getObservacaoAux());
			}
			
			for (int i = 0; i < lista.size(); i++) {
				// verifica se o primeiro evento é prisão provisória, se
				// for, pode existir a interrupção da prisão provisória
				if (((ProcessoEventoExecucaoDt) lista.get(i)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PRISAO_PROVISORIA))) {

					for (int j = i; j < lista.size(); j++) {
						// percorre a lista até encontrar o evento "TJ" ou
						// "GRP" após a "Prisão Provisória"
						ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) lista.get(j);
						if (evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) || evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
							posicaoTJ = j;
							dataTJ = evento.getDataInicio();

							// percorre a lista até encontrar o evento
							// "Interrupção da Prisão Provisória" após o
							// "TJ"
							for (int w = j; w < lista.size(); w++) {
								ProcessoEventoExecucaoDt evento1 = (ProcessoEventoExecucaoDt) lista.get(w);
								if (evento1.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.INTERRUPCAO_PRISAO_PROVISORIA)) && evento1.getDataInicio().equals(dataTJ)) {
									lista.remove(evento1);
									lista.add(posicaoTJ, evento1);
									break;
								}
							}
							break;
						}
					}
				} else
					break;
			}
		}
		
		return lista;
	}

	/**
	 * Lista os eventos a partir do id do processo, id da movimentação
	 * 
	 * @param id_processo
	 *            , identificação do processo
	 * @return lista com os eventos
	 * @author wcsilva
	 */
	public List listarEventos(String id_processo, String id_movimentacao) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			lista = listarEventos(id_processo, id_movimentacao, obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}

	/**
	 * Calcula a data fim dos eventos da lista.
	 * 
	 * @return lista, lista de eventos com a data fim corrigida
	 * @author wcsilva
	 * @throws Exception 
	 */
	public List calcularDataFim(List lista) throws Exception{
		
		if (lista != null && lista.size() > 0) {
			int j = 0;
			for (int i = 0; i < lista.size(); i++) {
				while (i < lista.size() - 1 && (((ProcessoEventoExecucaoDt) lista.get(i)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INFORMATIVO)) || isEventoPRD_Informativo(((ProcessoEventoExecucaoDt) lista.get(i)).getId_EventoExecucao()) || ((ProcessoEventoExecucaoDt) lista.get(i)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO)))) {
					((ProcessoEventoExecucaoDt) lista.get(i)).setDataFim("");
					i++;
				}
				int w = i + 1;
				if (w < lista.size()) {
					while (w < lista.size() - 1 && (((ProcessoEventoExecucaoDt) lista.get(w)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INFORMATIVO)) || isEventoPRD_Informativo(((ProcessoEventoExecucaoDt) lista.get(w)).getId_EventoExecucao()) || ((ProcessoEventoExecucaoDt) lista.get(w)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))))
						w++;

					((ProcessoEventoExecucaoDt) lista.get(i)).setDataFim(Funcoes.somaData(((ProcessoEventoExecucaoDt) lista.get(w)).getDataInicio(), -1));
				}

				// verifica índice do último evento não informativo e não
				// remição para zerar a dataFim
				if (!((ProcessoEventoExecucaoDt) lista.get(i)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INFORMATIVO)) && (!isEventoPRD_Informativo(((ProcessoEventoExecucaoDt) lista.get(i)).getId_EventoExecucao()) && !((ProcessoEventoExecucaoDt) lista.get(i)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO)))) {
					j = i;
				}
			}

			// dataFim do último evento não informativo é sempre vazia
			if (lista != null) ((ProcessoEventoExecucaoDt) lista.get(j)).setDataFim("");
		}
	
		return lista;
	}

	/**
	 * Consulta evento execução pelo id
	 * @throws Exception 
	 */
	public EventoExecucaoDt consultarIdEventoExecucao(String id_EventoExecucao) throws Exception{
		EventoExecucaoDt obj = null;
		
		obj = new EventoExecucaoNe().consultarId(id_EventoExecucao);
		QuantidadePaginas = 1;
		
		return obj;
	}

	/**
	 * Consulta os dados do EventoExecução através do id_eventoExecucao.
	 * 
	 * @param id_eventoExecucao
	 *            : id do evento
	 * @return eventoExecucaoDt
	 * @author wcsilva
	 * @throws Exception 
	 */
	public EventoExecucaoDt consultarEventoExecucao(String id_eventoexecucao) throws Exception{
		EventoExecucaoDt eventoExecucaoDt = null;
		
		eventoExecucaoDt = new EventoExecucaoNe().consultarId(id_eventoexecucao);

		return eventoExecucaoDt;
	}

	/**
	 * Efetua alteração dos dados do evento de um processo de execução penal, incluindo regime, local de cumprimento de pena.
	 * 
	 * @param processoEventoExecuçãodt
	 *            : dt com os dados do Evento
	 * @author wcsilva
	 */
	public void alterar(ProcessoEventoExecucaoDt processoEventoExecucaodt, ProcessoExecucaoDt processoExecucaoDt) throws Exception {
		EventoLocalNe eventoLocalNe = new EventoLocalNe();
		EventoRegimeNe eventoRegimeNe = new EventoRegimeNe();
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			// verifica se o evento alterado não informa a quantidade
			if (isInformarQuantidade(processoEventoExecucaodt.getId_EventoExecucao()).length() == 0) {
				if (!isEventoPenaRestritivaDireito(processoEventoExecucaodt.getEventoExecucaoDt().getId())) {
					processoEventoExecucaodt.setQuantidade("");
				}
			}

			// somente os eventos REVOGAÇÃO e SUSPENSÃO DO LIVRAMENTO CONDICIONAL possuem os parâmetros abaixo.
			if (!processoEventoExecucaodt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL)) 
					&& !processoEventoExecucaodt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))) {
				processoEventoExecucaodt.setId_LivramentoCondicional("");
				processoEventoExecucaodt.setConsiderarTempoLivramentoCondicional("0");
			}

			// altera processoEventoExecucaoDt
			this.salvar(processoEventoExecucaodt, obFabricaConexao);
			
			if (processoEventoExecucaodt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PENA_RESTRITIVA_DIREITO))|| 
				processoEventoExecucaodt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.ALTERACAO_MODALIDADE))){
				// altera lista de modalidade
				if (processoExecucaoDt.getListaModalidade() != null && processoExecucaoDt.getListaModalidade().size() > 0) {
					for (ProcessoEventoExecucaoDt modalidade : (List<ProcessoEventoExecucaoDt>)processoExecucaoDt.getListaModalidade()) {
						modalidade.setId_UsuarioServentia(processoEventoExecucaodt.getId_UsuarioServentia());
						modalidade.setIdProcessoExecucaoPenal(processoEventoExecucaodt.getIdProcessoExecucaoPenal());
						modalidade.setIdEventoPai(processoEventoExecucaodt.getId());
						modalidade.setDataInicio(processoEventoExecucaodt.getDataInicio());
						//nova modalidade incluída no processo
						if (modalidade.getId().length() == 0){		
							modalidade.setId_Movimentacao(processoEventoExecucaodt.getId_Movimentacao()); 
							modalidade.setId_ProcessoExecucao(processoExecucaoDt.getId());
						}
					}
					//salva/altera as modalidades (evento e eventoRegime)
					LogDt logDt = new LogDt(processoEventoExecucaodt.getId_UsuarioLog(), processoEventoExecucaodt.getIpComputadorLog());
					this.salvarListaEvento(processoExecucaoDt.getListaModalidade(), logDt, obFabricaConexao);
				} 
				
			}

			// salva ou altera o vínculo da comutação com os TJ na tabela Transito_Julgado_Evento (antiga ComutacaoTransitoJulgado).
			if (isVinculoEvento_TJ(processoEventoExecucaodt.getEventoExecucaoDt().getId())) {
				TransitoJulgadoEventoNe comutacaoTJNe = new TransitoJulgadoEventoNe();
				// TJ relacionados à comutação
				List listaComutacaoTJ = comutacaoTJNe.listarTransitoJulgadoEvento(processoEventoExecucaodt.getId(), processoEventoExecucaodt.getEventoExecucaoDt().getId(), obFabricaConexao); 

				for (int i = 0; i < processoEventoExecucaodt.getListaTJ().size(); i++) {
					// todos os TJ (selecionados e não selecionados pelo usuário)
					HashMap mapTJ = (HashMap) processoEventoExecucaodt.getListaTJ().get(i); 

					boolean incluir = false;
					boolean excluir = false;

					// inclui ou altera os dados
					if (mapTJ.get("Checked").toString().equals("1")) incluir = true; 
					else {
						for (TransitoJulgadoEventoDt comutacaoTJ : (List<TransitoJulgadoEventoDt>) listaComutacaoTJ) {
							// verifica se já existe o vínculo do TJ selecionado pelo usuário na tabela ComutacaoTransitoJulgado
							if (mapTJ.get("Id_TransitoJulgadoEvento").toString().equals(comutacaoTJ.getId_TransitoJulgadoEvento())) {
								if (mapTJ.get("Checked").toString().equals("1")) {
									incluir = false;
									break;
								} else {
									excluir = true;
									break;
								}
							}
						}
					}
					if (incluir || excluir) {
						TransitoJulgadoEventoDt dados = new TransitoJulgadoEventoDt();
						dados.setId_Evento(processoEventoExecucaodt.getId());
						dados.setId_TransitoJulgadoEvento(mapTJ.get("Id_TransitoJulgadoEvento").toString());
						dados.setId_TransitoJulgado(consultarId_ProcessoEventoExecucao(mapTJ.get("Id_ProcessoExecucao").toString(), String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO) + "," + String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA), obFabricaConexao));
						if (mapTJ.get("Fracao") != null) dados.setFracao(mapTJ.get("Fracao").toString());
						dados.setId_UsuarioLog(processoEventoExecucaodt.getId_UsuarioLog());
						dados.setIpComputadorLog(processoEventoExecucaodt.getIpComputadorLog());

						if (incluir) comutacaoTJNe.salvar(dados, obFabricaConexao);
						if (excluir) comutacaoTJNe.excluir(dados, obFabricaConexao);
					}
				}
			}

			// no caso de Trânsito em Julgado, Guia de Recolhimento Provisória e
			// eventos de modalidade e sursis, só altera o local e regime na
			// edição dos dados da ação penal
			if (!Funcoes.StringToBoolean(isEventoManterAcaoPenal(processoEventoExecucaodt.getEventoExecucaoDt().getId()))) {
				// altera eventoLocalDt
				if (processoEventoExecucaodt.getEventoLocalDt().getId().length() > 0 
						|| processoEventoExecucaodt.getEventoLocalDt().getId_LocalCumprimentoPena().length() > 0) 
					if (processoEventoExecucaodt.getEventoExecucaoDt().getAlteraLocal().equalsIgnoreCase("N")) 
						eventoLocalNe.excluir(processoEventoExecucaodt.getEventoLocalDt(), obFabricaConexao);
				else {
					processoEventoExecucaodt.getEventoLocalDt().setId_ProcessoEventoExecucao(processoEventoExecucaodt.getId());
					eventoLocalNe.salvar(processoEventoExecucaodt.getEventoLocalDt(), obFabricaConexao);
				}

				// altera eventoRegimeDt
				if (processoEventoExecucaodt.getEventoRegimeDt().getId().length() > 0 
						|| processoEventoExecucaodt.getEventoRegimeDt().getId_RegimeExecucao().length() > 0)
					if (processoEventoExecucaodt.getEventoExecucaoDt().getAlteraRegime().equalsIgnoreCase("N") 
							// no caso dos eventos de prisão e prisão flagrante
							|| processoEventoExecucaodt.getEventoRegimeDt().getId_RegimeExecucao().length() == 0) 
						eventoRegimeNe.excluir(processoEventoExecucaodt.getEventoRegimeDt(), obFabricaConexao);
					else {
						processoEventoExecucaodt.getEventoRegimeDt().setId_ProcessoEventoExecucao(processoEventoExecucaodt.getId());
						eventoRegimeNe.salvar(processoEventoExecucaodt.getEventoRegimeDt(), obFabricaConexao);
					}
			}

			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva dados da ação penal
	 * 
	 * @param processoExecucaoDt
	 *            : objeto com os dados da ação penal
	 * @param idServentia
	 *            : identificação da serventia
	 * @throws Exception
	 */
	public void salvarProcessoExecucao(ProcessoExecucaoDt processoExecucaoDt, String idUsuarioServentia) throws Exception {
		new ProcessoExecucaoNe().salvarComCondenacao(processoExecucaoDt, idUsuarioServentia);
	}

	/**
	 * Exclui o evento de um processo de execução penal, incluindo regime, local de cumprimento de pena e vínculo com o TJ (se existir)
	 * Tem que ter iniciado transação
	 * @param processoEventoExecucaodt
	 *            : dt com os dados do evento
	 * @param obFabricaConexao
	 *            : objeto de conexão
	 * @throws Exception
	 */
	public void excluirCompleto(ProcessoEventoExecucaoDt processoEventoExecucaodt, FabricaConexao obFabricaConexao) throws Exception {
		
		LogDt logDt = new LogDt(processoEventoExecucaodt.getId_UsuarioLog(), processoEventoExecucaodt.getIpComputadorLog());

		// exclui eventoLocalDt
		if (processoEventoExecucaodt.getEventoLocalDt().getId().length() > 0) 
			new EventoLocalNe().excluirId_ProcessoEventoExecucao(processoEventoExecucaodt.getEventoLocalDt(), obFabricaConexao, logDt);

		// exclui eventoRegimeDt
		if (processoEventoExecucaodt.getEventoRegimeDt().getId().length() > 0) 
			new EventoRegimeNe().excluirId_ProcessoEventoExecucao(processoEventoExecucaodt.getEventoRegimeDt(), obFabricaConexao, logDt);

		// exclui todos os vínculos do evento com o TJ (TransitoJulgadoEvento)
		if (isVinculoEvento_TJ(processoEventoExecucaodt.getEventoExecucaoDt().getId())) {
			new TransitoJulgadoEventoNe().excluirId_Evento(processoEventoExecucaodt.getId(), obFabricaConexao, processoEventoExecucaodt.getId_UsuarioLog(), processoEventoExecucaodt.getIpComputadorLog());
		}

		// exclui os eventos filho
		this.excluirEventoFilho(processoEventoExecucaodt, obFabricaConexao);
		
		// exclui processoEventoExecucaoDt
		this.excluir(processoEventoExecucaodt, obFabricaConexao);
		
	}

	public void excluirCompleto(List listaEventos) throws Exception{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			for (ProcessoEventoExecucaoDt processoEventoExecucaodt : (List<ProcessoEventoExecucaoDt>)listaEventos) {
				this.excluirCompleto(processoEventoExecucaodt, obFabricaConexao);	
			}
			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Exclui o evento de um processo de execução penal, incluindo regime, local de cumprimento de pena
	 * 
	 * @param processoEventoExecuçãodt
	 *            : dt com os dados do Evento
	 * @author wcsilva
	 */
	public void excluirCompleto(ProcessoEventoExecucaoDt processoEventoExecucaodt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			this.excluirCompleto(processoEventoExecucaodt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluir(ProcessoEventoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("ProcessoEventoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
		obPersistencia.excluir(dados.getId());
		obLog.salvar(obLogDt, obFabricaConexao);

		dados.limpar();
	}

	public void excluirEventoFilho(ProcessoEventoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("ProcessoEventoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
		obPersistencia.excluirEventoFilho(dados.getId());
		obLog.salvar(obLogDt, obFabricaConexao);

	}
	
	/**
	 * Método responsável por efetuar os cálculos de liquidação de pena selecionados
	 * 
	 * @param listaProcessoEventoExecucaoDt
	 *            , lista com os eventos
	 * @param calculoLiquidacaoDt
	 *            , objeto contendo a lista com os tipos de cálculo a serem realizados
	 * @param idProcesso
	 *            , identificação do processo
	 * @return HashMap, map com as mensagens: msgGeral, msgProgressao, msgLivramento, msgIndulto, msgComutacao, msgPrescricao
	 * @author wcsilva
	 * @throws Exception
	 */
	public HashMap calcularLiquidacaoPena(List listaEvento, List listaEventoPSC, List listaEventoLFS, List listaEventoPec, List listaEventoITD, List listaEventoPcb, CalculoLiquidacaoDt calculoLiquidacaoDt, String dataNascimento, String idProcesso) throws Exception {
		HashMap map = new HashMap();
		

		List listaTotalCondenacaoRemanescente = new ArrayList();
		List listaTotalCondenacao = new ArrayList();
		List listaTotalComutacao = new ArrayList();

		for (ProcessoEventoExecucaoDt pee : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
					|| pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)) 
					|| pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))) {
				listaTotalCondenacaoRemanescente.add(pee.getCondenacaoAnos());

				if (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
						|| pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
					listaTotalCondenacao.add(pee.getCondenacaoAnos());
				} else if (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))) {
					listaTotalComutacao.add(pee.getCondenacaoAnos());
				}

			}
		}
		calculoLiquidacaoDt.setTempoTotalCondenacaoRemanescenteDias(somarAnoMesDia(listaTotalCondenacaoRemanescente));
		calculoLiquidacaoDt.setTempoTotalCondenacaoDias(calcularTempoTotal(listaEvento, false, true));
		calculoLiquidacaoDt.setTempoTotalComutacaoDias(calcularTempoTotal(listaEvento, true, false));
		// calculoLiquidacaoDt.setTempoTotalCondenacaoRemanescenteDias(calcularTempoTotalCondenacaoRemanescenteDias(listaEvento));

		calcularRestantePena_TempoCumprido_TerminoPena(listaEvento, calculoLiquidacaoDt);

		// cálculo da pena privativa de liberdade
		calcularRemicao(listaEvento, calculoLiquidacaoDt);

		// verifica se existe cálculo de pena privativa de liberdade a ser
		// efetuado
		if (calculoLiquidacaoDt.getListaTipoCalculo() != null) {

			// ---- INÍCIO DO CÁLCULO DO TEMPO DE PENA REMANESCENTE ----//
			// ---coloquei no método separado
			List listaCondenacoesNaoExtintas = calcularPenaRemanescente(idProcesso, listaEvento);
			// ---- FIM DO CÁLCULO DO TEMPO DE PENA REMANESCENTE ----//

			// verifica se a pena está interrompida
			if (getIdStatus(listaEvento).equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) {
				for (int i = 0; i < calculoLiquidacaoDt.getListaTipoCalculo().size(); i++) {
					if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("PROGRESSAO")) {
						calculoLiquidacaoDt.getListaTipoCalculo().remove(i);
						i--;
						map.put("msgGeral", "Não é possível efetuar os cálculos de Progressão, Livramento, Comutação e/ou Indulto: EXECUÇÃO DA PENA INTERROMPIDA.\n");
					}
				}
			}

			boolean jaCalculouPrescricao = false;
			boolean jaCalculouComutacao = false;
			for (int i = 0; i < calculoLiquidacaoDt.getListaTipoCalculo().size(); i++) {
				String tipoCalculo = (String) calculoLiquidacaoDt.getListaTipoCalculo().get(i);

				if (tipoCalculo.equalsIgnoreCase("PROGRESSAO")) {
					if (calculoLiquidacaoDt.getDataTerminoPena().length() > 0 && !"-".equals(calculoLiquidacaoDt.getDataTerminoPena()) && Funcoes.StringToDate(calculoLiquidacaoDt.getDataTerminoPena()).before(new Date())) {
						String msg = "A pena foi cumprida integralmente. Provável término da pena em " + calculoLiquidacaoDt.getDataTerminoPena() + ".";
						map.put("msgProgressao", msg);
						map.put("msgLivramento", msg);
						if (calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt() != null) {
							calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setMensagemLivramento(msg);
							calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setMensagemProgressao(msg);
						}
					} else {
						// captura os dados de opções de cálculo
						String dataBasePR = calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataBaseProgressao();
						String reincidenteHediondoPR = calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getReincidenteHediondoPR();
						String descRegime = calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getNovoRegimeProgressao();
						String dataBaseLC = calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataBaseLivramento();
						String reincidenteEspecifico = calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getReincidenteEspecificoLC();
						String opc = calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getForcarCalculoLC();
						calculoLiquidacaoDt.limparDadosEspecificos(false, true, false, false, false);
						calculoLiquidacaoDt.newCalculoProgressaoLivramentoDt();
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setDataBaseProgressao(dataBasePR);
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setReincidenteHediondoPR(reincidenteHediondoPR);
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setNovoRegimeProgressao(descRegime);
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setDataBaseLivramento(dataBaseLC);
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setReincidenteEspecificoLC(reincidenteEspecifico);
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setForcarCalculoLC(opc);
						
						boolean isCalcularPR = false;
						boolean isCalcularLC = false;
							
						if (calculoLiquidacaoDt.getCalcularSomente().equalsIgnoreCase("PR")){
							isCalcularPR = true;
						} else if (calculoLiquidacaoDt.getCalcularSomente().equalsIgnoreCase("LC")){
							isCalcularLC = true;
						} else {
							isCalcularLC = true;
							isCalcularPR = true;
						}
						if (isCalcularPR){
							map.put("msgProgressao", calcularProgressaoRegime(listaEvento, calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt(), listaCondenacoesNaoExtintas, calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias(), calculoLiquidacaoDt.getTipoRemicao()));
							calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setMensagemProgressao(map.get("msgProgressao").toString());
						} 
						if (isCalcularLC){
							map.put("msgLivramento", calcularLivramentoCondicional(listaEvento, calculoLiquidacaoDt, listaCondenacoesNaoExtintas));
							calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setMensagemLivramento(map.get("msgLivramento").toString());
						}
					}

				} else if (tipoCalculo.equalsIgnoreCase("COMUTACAO") || tipoCalculo.equalsIgnoreCase("COMUTACAO_UNIFICADA")) {
					String idParametroComutacao = "";
					if (calculoLiquidacaoDt.getCalculoComutacaoDt() != null) idParametroComutacao = calculoLiquidacaoDt.getCalculoComutacaoDt().getIdParametroComutacao();
					if (!jaCalculouComutacao) {
						calculoLiquidacaoDt.limparDadosEspecificos(false, false, true, false, false);
						calculoLiquidacaoDt.newCalculoComutacaoDt();
						map.put("msgComutacao", "");
						map.put("msgComutacaoUnificada", "");
					}
					calculoLiquidacaoDt.getCalculoComutacaoDt().setIdParametroComutacao(idParametroComutacao);
					if (tipoCalculo.equalsIgnoreCase("COMUTACAO")){
						calcularComutacaoPreviaUnificada(listaEvento, calculoLiquidacaoDt, listaCondenacoesNaoExtintas, idParametroComutacao);
						map.put("msgComutacao", calcularComutacaoPreviaIndividual(listaEvento, calculoLiquidacaoDt, listaCondenacoesNaoExtintas, idParametroComutacao));							
					}
					else
						map.put("msgComutacaoUnificada", calcularComutacaoPreviaUnificada(listaEvento, calculoLiquidacaoDt, listaCondenacoesNaoExtintas, idParametroComutacao));
					jaCalculouComutacao = true;

				} else if (tipoCalculo.equalsIgnoreCase("INDULTO")) {
//					String fracaoIndulto = calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndulto();
					String fracaoIndultoComum = calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoComum();
					String fracaoIndultoHediondo = calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoHediondo();
					calculoLiquidacaoDt.limparDadosEspecificos(false, false, false, true, false);
					calculoLiquidacaoDt.newCalculoIndultoDt();
//					calculoLiquidacaoDt.getCalculoIndultoDt().setFracaoIndulto(fracaoIndulto);
					calculoLiquidacaoDt.getCalculoIndultoDt().setFracaoIndultoComum(fracaoIndultoComum);
					calculoLiquidacaoDt.getCalculoIndultoDt().setFracaoIndultoHediondo(fracaoIndultoHediondo);
					map.put("msgIndulto", calcularIndulto(listaEvento, calculoLiquidacaoDt, listaCondenacoesNaoExtintas));

				} else if (tipoCalculo.equalsIgnoreCase("PRESCRICAO_PUNITIVA") 
						|| tipoCalculo.equalsIgnoreCase("PRESCRICAO_EXECUTORIA_IND") 
						|| tipoCalculo.equalsIgnoreCase("PRESCRICAO_EXECUTORIA_UNI")) {

					boolean isPrescricaoUnificada = false;
					if (tipoCalculo.equalsIgnoreCase("PRESCRICAO_EXECUTORIA_UNI")) {
						isPrescricaoUnificada = true;
					}
					if (!jaCalculouPrescricao) {
						calculoLiquidacaoDt.limparDadosEspecificos(false, false, false, false, true);
						calculoLiquidacaoDt.newCalculoPrescricaoDt();
					}

					if (tipoCalculo.equalsIgnoreCase("PRESCRICAO_PUNITIVA")) {
						map.put("msgPrescricaoPunitiva", calcularPrescricaoPunitiva(listaEvento, calculoLiquidacaoDt, listaCondenacoesNaoExtintas, dataNascimento));

					} else if (tipoCalculo.equalsIgnoreCase("PRESCRICAO_EXECUTORIA_IND") || isPrescricaoUnificada) {
						String msg = calcularPrescricaoExecutoria(listaEvento, calculoLiquidacaoDt, listaCondenacoesNaoExtintas, dataNascimento, isPrescricaoUnificada);
						if (isPrescricaoUnificada){
							map.put("msgPrescricaoExecutoriaUnificada",msg);
						} else 
							map.put("msgPrescricaoExecutoriaInd", msg);
					}

//					if (isPrescricaoUnificada) {
//						if (calculoLiquidacaoDt.getDataValidadeMandadoPrisaoUnificada().length() == 0 && calculoLiquidacaoDt.getDataUltimaFuga().length() == 0) {
//							map.put("msgPrescricaoExecutoriaUnificada", "Não é possível efetuar o cálculo: SENTENCIADO CUMPRINDO PENA!");
////						} else if (calculoLiquidacaoDt.getTempoPrescricaoUnificadaAnos().length() == 0){
////							map.put("msgPrescricaoExecutoriaUnificada", "Não é possível efetuar o cálculo: PENA CUMPRIDA!");
//						}
//					}
					jaCalculouPrescricao = true;

				} else if (tipoCalculo.equalsIgnoreCase("PENARESTRITIVA")) {
					calculoLiquidacaoDt.newCalculoPenaRestritivaDt();
					map.put("msgPenaRestritiva", calcularPenaRestritiva(listaEvento, listaEventoPSC, listaEventoLFS, listaEventoPec, listaEventoITD, listaEventoPcb, calculoLiquidacaoDt));
				} else if (tipoCalculo.equalsIgnoreCase("SURSIS")) {
					map.put("msgSursis", calcularSursis(listaEvento, calculoLiquidacaoDt));
				}
			}
		}	

		return map;
	}

	/**
	 * calcula o tempo da pena remanescente de cada condenação não extinta
	 * 
	 * @param listaCondenacoesNaoExtintas
	 * @param idProcesso
	 * @param listaEvento
	 * @return
	 * @throws Exception
	 */
	public List calcularPenaRemanescente(String idProcesso, List listaEvento) throws Exception{
		List listaCondenacoesNaoExtintas = null;
		
		// lista as condenações e verifica os parâmetros do crime (para Progressão e Livramento)
		listaCondenacoesNaoExtintas = listarCondenacoesNaoExtintas(idProcesso);

		List lista = calcularTempoComutacao(idProcesso, listaEvento, null);
		List listaComutacaoTJ = new ArrayList();
		
		// está ordenada pelo TJ e depois pelo Id da Condenação
		if (lista != null) listaComutacaoTJ = (List) lista.get(0); 

		for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacoesNaoExtintas) {
			condenacao.setTempoPenaRemanescenteEmDias(condenacao.getTempoPenaEmDias());

			for (int w = 0; w < listaComutacaoTJ.size(); w++) {
				TransitoJulgadoEventoDt ctj = (TransitoJulgadoEventoDt) listaComutacaoTJ.get(w);

				// verifica se houve comutação para a condenação através do Id_CondenacaoExecucao.
				if (condenacao.getId().equals(ctj.getId_CondenacaoExecucao())) {

					// localiza a última CONDENAÇÃO igual, da lista
					// comutacaoTJ (que está ordenada pelo TJ e Condenação),
					// para calcular a pena remanescente desta condenação
					// (tem que calcular a pena remanescente de cada
					// condenação, no caso de um TJ ter mais de uma
					// condenação)
					if (w == listaComutacaoTJ.size() - 1 || !ctj.getId_CondenacaoExecucao().equals(((TransitoJulgadoEventoDt) listaComutacaoTJ.get(w + 1)).getId_CondenacaoExecucao())) {
						int tempoRemanescente = Funcoes.StringToInt(ctj.getTempoPenaRemanescenteTJDias()) - Funcoes.StringToInt(ctj.getTempoComutacaoDias());
						condenacao.setTempoPenaRemanescenteEmDias(String.valueOf(tempoRemanescente));
					}
				}
			}
		}
		
		return listaCondenacoesNaoExtintas;
	}

	/**
	 * Lista com os tempo em ano-mês-dia a serem somados
	 * 
	 * @param tempoEmAnos
	 * @return
	 * @throws Exception
	 */
	private String somarAnoMesDia(List listTempoEmAnos){
		List listaTempoPositivo = new ArrayList();
		List listaTempoNegativo = new ArrayList();
		String tempoTotalDias = "";

		if (listTempoEmAnos != null && listTempoEmAnos.size() > 0) {
			for (String tempo : (List<String>) listTempoEmAnos) {
				if (tempo.length() > 0) {
					if (tempo.substring(0, 1).equals("(")) {// tempo
																// negativo
						tempo = tempo.replace("(", "");
						tempo = tempo.replace(")", "");
						listaTempoNegativo.add(tempo.split(" - "));
					} else {
						listaTempoPositivo.add(tempo.split(" - "));
					}
				}
			}

			float tempoAno = 0;
			float tempoMes = 0;
			float tempoDia = 0;
			for (String[] vetor : (List<String[]>) listaTempoPositivo) {
				tempoAno += Funcoes.StringToInt(vetor[0]);
				tempoMes += Funcoes.StringToInt(vetor[1]);
				tempoDia += Funcoes.StringToInt(vetor[2]);
			}
			for (String[] vetor : (List<String[]>) listaTempoNegativo) {
				tempoDia -= Funcoes.StringToInt(vetor[2]);
				if (tempoDia < 0) {
					tempoDia += 30;
					tempoMes -= 1;
				}
				tempoMes -= Funcoes.StringToInt(vetor[1]);
				if (tempoMes < 0) {
					tempoMes += 12;
					tempoAno -= 1;
				}
				tempoAno -= Funcoes.StringToInt(vetor[0]);
			}

			while (tempoDia >= 30) {
				tempoDia -= 30;
				tempoMes += 1;
			}
			while (tempoMes >= 12) {
				tempoMes -= 12;
				tempoAno += 1;
			}

			boolean isTempoNegativo = false;
			if (tempoAno < 0 || tempoMes < 0 || tempoDia < 0) {
				isTempoNegativo = true;
				tempoAno = Math.abs(tempoAno);
				tempoMes = Math.abs(tempoMes);
				tempoDia = Math.abs(tempoDia);
			}

			tempoTotalDias = Funcoes.converterParaDias(String.valueOf((int) tempoAno), String.valueOf((int) tempoMes), String.valueOf((int) tempoDia));
			if (isTempoNegativo) tempoTotalDias = "-" + tempoTotalDias;
		}

		if (tempoTotalDias.length() == 0) tempoTotalDias = "0";
				
		return tempoTotalDias;
	}

	private String calcularTempoTotal(List listaEvento, boolean totalComutacao, boolean totalCondenacao){
		String tempoTotalDias = "";
		int tempoAno = 0;
		int tempoMes = 0;
		int tempoDia = 0;
		
		for (ProcessoEventoExecucaoDt pee : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (totalCondenacao && (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) || pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)))) {
				if (pee.getCondenacaoDias().length() > 0 && pee.getCondenacaoAnos().length() > 0) {
					String[] vetor = pee.getCondenacaoAnos().split(" - ");
					tempoAno += Funcoes.StringToInt(vetor[0]);
					tempoMes += Funcoes.StringToInt(vetor[1]);
					tempoDia += Funcoes.StringToInt(vetor[2]);
				}
			} else if (totalComutacao && (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA)))) {
				if (pee.getCondenacaoDias().length() > 0 && pee.getCondenacaoAnos().length() > 0) {
					String tempo = pee.getCondenacaoAnos();
					tempo = tempo.replace("(", "");
					tempo = tempo.replace(")", "");

					String[] vetor = tempo.split(" - ");
					tempoAno += Funcoes.StringToInt(vetor[0]);
					tempoMes += Funcoes.StringToInt(vetor[1]);
					tempoDia += Funcoes.StringToInt(vetor[2]);
				}
			}
		}

		while (tempoDia >= 30) {
			tempoDia -= 30;
			tempoMes += 1;
		}
		while (tempoMes >= 12) {
			tempoMes -= 12;
			tempoAno += 1;
		}
		tempoTotalDias = Funcoes.converterParaDias(String.valueOf(tempoAno), String.valueOf(tempoMes), String.valueOf(tempoDia));

		return tempoTotalDias;
	}
	
	/**
	 * Método que recebe uma lista de eventos de condenação e faz o cálculo do tempo total de 
	 * condenação apenas dos crimes considerados não hediondos.
	 * @param listaEvento - lista de condenações
	 * @return tempo total de condenação em dias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public int calcularTempoCondenacaoCrimesNaoHediondos(List listaEvento){
		String tempoTotalDias = "";
		int tempoAno = 0;
		int tempoMes = 0;
		int tempoDia = 0;
		
		for (CondenacaoExecucaoDt condenacaoDt : (List<CondenacaoExecucaoDt>) listaEvento) {
			if (!condenacaoDt.isHediondoLivramento() && !condenacaoDt.isHediondoProgressao()) {
				if (condenacaoDt.getQtdeDias().length() > 0 && condenacaoDt.getQtdeAno().length() > 0) {
					tempoAno += Funcoes.StringToInt(condenacaoDt.getQtdeAno());
					tempoMes += Funcoes.StringToInt(condenacaoDt.getQtdeMes());
					tempoDia += Funcoes.StringToInt(condenacaoDt.getQtdeDias());
				}
			} 
		}

		while (tempoDia >= 30) {
			tempoDia -= 30;
			tempoMes += 1;
		}
		while (tempoMes >= 12) {
			tempoMes -= 12;
			tempoAno += 1;
		}
		tempoTotalDias = Funcoes.converterParaDias(String.valueOf(tempoAno), String.valueOf(tempoMes), String.valueOf(tempoDia));
		
		
		return Integer.parseInt(tempoTotalDias);
	}

	// private String calcularTempoTotalCondenacaoRemanescenteDias(List
	// listaEvento) throws Exception{
	// List listaTempoPositivo = new ArrayList();
	// List listaTempoNegativo = new ArrayList();
	// String tempoCondenacaoDias = "";
	// //reformulação do método: wilana - 10/10/2012
	// try{
	// for (ProcessoEventoExecucaoDt pee :
	// (List<ProcessoEventoExecucaoDt>)listaEvento) {
	// if
	// (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))
	// ||
	// pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))){
	// if (pee.getCondenacaoDias().length() > 0){
	// listaTempoPositivo.add(pee.getCondenacaoAnos().split(" - "));
	// }
	// }
	// else if
	// (pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){
	// if (pee.getCondenacaoDias().length() > 0){
	// String tempo = pee.getCondenacaoAnos();
	// tempo = tempo.replace("(", "");
	// tempo = tempo.replace(")", "");
	// listaTempoNegativo.add(tempo.split(" - "));
	// }
	// }
	// }
	//
	// int tempoAno = 0;
	// int tempoMes = 0;
	// int tempoDia = 0;
	// for (String[] vetor : (List<String[]>)listaTempoPositivo) {
	// tempoAno += Funcoes.StringToInt(vetor[0]);
	// tempoMes += Funcoes.StringToInt(vetor[1]);
	// tempoDia += Funcoes.StringToInt(vetor[2]);
	// }
	// for (String[] vetor : (List<String[]>)listaTempoNegativo) {
	// tempoDia -= Funcoes.StringToInt(vetor[2]);
	// if (tempoDia < 0){
	// tempoDia += 30;
	// tempoMes -= 1;
	// }
	// tempoMes -= Funcoes.StringToInt(vetor[1]);
	// if (tempoMes < 0){
	// tempoMes += 12;
	// tempoAno -= 1;
	// }
	// tempoAno -= Funcoes.StringToInt(vetor[0]);
	// }
	//
	// while (tempoDia >= 30){
	// tempoDia -= 30;
	// tempoMes += 1;
	// }
	// while (tempoMes >= 12){
	// tempoMes -= 12;
	// tempoAno += 1;
	// }
	// tempoCondenacaoDias = Funcoes.converterParaDias(String.valueOf(tempoAno),
	// String.valueOf(tempoMes), String.valueOf(tempoDia));
	// }catch(Exception e) {
	// throw new
	// Exception(" <{Erro ao Calcular pena remanescente.}> Local Exception: " +
	// this.getClass().getName() + ".calcularPenaRemanescente(): " +
	// e.getMessage(), e);
	// }
	// return tempoCondenacaoDias;
	// }

	/**
	 * Lista todas as condenações (NÃO EXTINTAS) de todos os trânsito em julgado. Verifica se o crime da condenação é considerado hediondo para cálculo do livramento condicional e progressão de regime.
	 * 
	 * @param listaProcessoEventoExecucaoDt
	 * @return listaCondenacao, lista com as condenações NÃO EXTINTAS de todos os transito em julgado
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarCondenacoesNaoExtintas(String idProcesso) throws Exception{
		List listaCondenacao = new ArrayList();
		
		listaCondenacao = new CondenacaoExecucaoNe().listarCondenacaoNaoExtintaDoProcesso(idProcesso);

		return listaCondenacao;
	}

	/**
	 * lista todos os trânsitos em julgado (e guia de recolhimento provisória), com condenações do processo principal e apensos (HashMap com as chaves: Id_ProcessoExecucao, DataTransitoJulgado, TempoNaoExtintoDias, TempoExtintoDias, TempoTotalDias, Checked, Fracao, Id_TransitoJulgadoEvento)
	 * 
	 * @param id_ProcessoExecucaoPenal
	 *            : identificador do processo de execução penal
	 * @param id_ProcessoEventoExecucao
	 *            : identificador do evento de comutação que está sendo alterado.
	 * @return lista com o HashMap com as informações dos TJs
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarTransitoComTotalCondenacao_HashMap(String id_ProcessoExecucaoPenal, String id_ProcessoEventoExecucao, String id_EventoExecucao) throws Exception{
		List listaTJ = null;
		
		listaTJ = new ProcessoExecucaoNe().listarTransitoComTotalCondenacao_HashMap(id_ProcessoExecucaoPenal, id_ProcessoEventoExecucao, id_EventoExecucao);

		return listaTJ;
	}

	/**
	 * verifica o último evento que possui regime e retorna o eventoRegimeDt correspondente
	 * 
	 * @param listaEvento
	 *            , lista com os eventos (ProcessoEventoExecucaoDt)
	 * @return eventoRegimeDt.
	 */
	public EventoRegimeDt getUltimoEventoRegimeDt(List listaEvento) {
		EventoRegimeDt eventoRegimeDt = null;

		// verifica o último evento que possui regime
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucao = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (processoEventoExecucao.getEventoRegimeDt().getRegimeExecucao().length() > 0 && !processoEventoExecucao.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) && !processoEventoExecucao.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) eventoRegimeDt = processoEventoExecucao.getEventoRegimeDt();
		}
		return eventoRegimeDt;
	}

	/**
	 * verifica o status do último evento
	 * 
	 * @param listaEvento
	 *            , lista com os eventos (ProcessoEventoExecucaoDt)
	 * @return status: Descrição do status.
	 */
	public String getDescricaoStatus(List listaEvento) {
		String status = "";

		// verifica o último evento que possui regime
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucao = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (!processoEventoExecucao.getId_EventoExecucaoStatus().equals(String.valueOf(EventoExecucaoStatusDt.NAO_APLICA))) 
				status = processoEventoExecucao.getEventoExecucaoDt().getEventoExecucaoStatus();
		}
		return status;
	}

	public String getIdStatus(List listaEvento) {
		String idStatus = "";

		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucao = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (!processoEventoExecucao.getId_EventoExecucaoStatus().equals(String.valueOf(EventoExecucaoStatusDt.NAO_APLICA)))
				idStatus = processoEventoExecucao.getEventoExecucaoDt().getId_EventoExecucaoStatus();
		}
		return idStatus;
	}

	/**
	 * Verifica se o crime da condenação é considerado hediondo para cálculo do livramento condicional e progressão de regime.
	 * 
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 * @throws Exception 
	 */
	public List verificarParametroCrime(List listaCondenacao) throws Exception{

		listaCondenacao = new CondenacaoExecucaoNe().verificarParametroCrime(listaCondenacao);

		return listaCondenacao;
	}

	/**
	 * verifica se existe crime hediondo a partir de 25/07/1990 (mesmo parâmetro dos crimes hediondo para livramento condicional)
	 * 
	 * @param listaCondenacao
	 *            , lista das condenações
	 * @return
	 */
	public boolean isContemCrimeHediondo(List listaCondenacao) {
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);
			if (condenacaoExecucaoDt.isHediondoLivramento()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * verifica se existe crime comum para LC.
	 * 
	 * @param listaCondenacao
	 *            , lista das condenações
	 * @return
	 */
	public boolean isContemCrimeComum(List listaCondenacao){
		
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);
			// verifica se o crime é comum para LC
			if (!condenacaoExecucaoDt.isHediondoLivramento()) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * lista apenas as condenações com data inicio de cumprimento da pena anteriores à data referência
	 * 
	 * @param listaCondenacao
	 *            , lista de todas as condenações
	 * @param inicio
	 *            , data referência
	 * @return
	 * @throws Exception 
	 */
	public List getListaCondenacaoAteData(List listaCondenacao, String inicio) throws Exception{
		return getListaCondenacaoAteData(listaCondenacao, inicio, null);
	}
	
	public List getListaCondenacaoAteData(List listaCondenacao, String inicio, String fim) throws Exception{
		List listaCondenacaoAteData = new ArrayList();
		
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);
			// verifica se a dataInicioCumprimentoPena é igual ou anterior à data informada.
			if (inicio.length() > 0 && condenacaoExecucaoDt.getDataInicioCumprimentoPena().length() > 0) {
				if (Funcoes.StringToDate(inicio).after(Funcoes.StringToDate(condenacaoExecucaoDt.getDataInicioCumprimentoPena()))
						|| Funcoes.StringToDate(inicio).equals(Funcoes.StringToDate(condenacaoExecucaoDt.getDataInicioCumprimentoPena()))) {
					listaCondenacaoAteData.add(condenacaoExecucaoDt);
				} else {
					if (fim.length() > 0 && Funcoes.StringToDate(fim).after(Funcoes.StringToDate(condenacaoExecucaoDt.getDataInicioCumprimentoPena()))) {
						listaCondenacaoAteData.add(condenacaoExecucaoDt);
					}
				}
			}
		}

		return listaCondenacaoAteData;
	}

	/**
	 * verifica se existe reincidência em algum crime
	 * 
	 * @param listaCondenacao
	 * @return
	 */
	public boolean isReincidente(List listaCondenacao) {
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);
			if (condenacaoExecucaoDt.isReincidente()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * lista os tipos de reincidência: genérica ou específica
	 * 
	 * @param listaEvento
	 * @param listaCondenacao
	 * @return listaTipoReincidencia: lista de string com os tipos de reincidência
	 */
	public List getListaTipoReincidencia(List listaEvento, List listaCondenacao) {
		List listaTipoReincidencia = new ArrayList();

		// int qtdeComum = 0;
		int qtdeComumReinc = 0;
		int qtdeHed = 0;
		int qtdeHedReinc = 0;

		// soma a qtde de cada tipo de crime para validação posterior
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);

			// soma a qtde de cada tipo de crime para validação posterior
			if (condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) qtdeHedReinc++;
			if (condenacaoExecucaoDt.isHediondoLivramento() && !condenacaoExecucaoDt.isReincidente()) qtdeHed++;
			if (!condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) qtdeComumReinc++;
		}

		if ((qtdeHedReinc == 1 && qtdeHed == 0) || qtdeComumReinc > 0) 
			listaTipoReincidencia.add("Reincidente Genérico");

		if ((qtdeHedReinc == 1 && qtdeHed > 0) || qtdeHedReinc > 0) 
			listaTipoReincidencia.add("Reincidente Específico");

		return listaTipoReincidencia;
	}

	/**
	 * Calcula o requisito temporal para Progressão de Regime
	 * 
	 * @param listaEvento
	 *            , lista com os eventos
	 * @param calculoLiquidacaoDt
	 *            , objeto com os dados do cálculo
	 * @param listaCondenacao
	 *            , lista com as condenações
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularProgressaoRegime(List listaEvento, CalculoLiquidacaoProgressaoLivramentoDt calculoProgressaoDt, List listaCondenacaoNaoExtinta, String tempoTotalCondeancaoDias, String tipoRemicao) throws Exception{

		String msgRetorno = "";
		EventoRegimeDt eventoRegimeDt = getUltimoEventoRegimeDt(listaEvento);
		String dataRequisito = "";

		int tempoTotalCondenacaoDiasInt = 0;
		if (tempoTotalCondeancaoDias.length() > 0) tempoTotalCondenacaoDiasInt = Integer.parseInt(tempoTotalCondeancaoDias);
		
		// verifica se existe próximo regime para o regime atual e se não é para
		// forçar o cálculo do LC
		if (calculoProgressaoDt.getForcarCalculoLC().length() == 0) {
			if (eventoRegimeDt.getId_ProximoRegimeExecucao().length() == 0) 
				msgRetorno += "Não existe Progressão de Regime para o regime atual: " + eventoRegimeDt.getRegimeExecucao() + ". \n";
		}

		if (msgRetorno.length() == 0) {
			// consulta o novo regime
			if (calculoProgressaoDt.getNovoRegimeProgressao().length() == 0 && eventoRegimeDt.getId_ProximoRegimeExecucao().length() > 0) {
				RegimeExecucaoDt regimeExecucaoDt = new RegimeExecucaoNe().consultarRegime(eventoRegimeDt.getId_ProximoRegimeExecucao());
				calculoProgressaoDt.setNovoRegimeProgressao(regimeExecucaoDt.getRegimeExecucao());
			}

			// verifica data base
			if (calculoProgressaoDt.getDataBaseProgressao().length() == 0 && isPrimeiraProgressao(listaEvento)) 
				calculoProgressaoDt.setDataBaseProgressao(getPrimeiraDataBase(listaEvento));

			// para o tipoRemicao == 2: inlcui, no tempo cumprido, todo o
			// tempo de remição (inclusive os que estão após a data base)
			calculoProgressaoDt.setTempoCumpridoDataBaseDias(calcularTempoCumpridoAteDataBase(listaEvento, calculoProgressaoDt.getDataBaseProgressao(), tempoTotalCondeancaoDias, tipoRemicao));
			calculoProgressaoDt.setTempoRestanteDataBaseDias(String.valueOf(tempoTotalCondenacaoDiasInt - Integer.parseInt(calculoProgressaoDt.getTempoCumpridoDataBaseDias())));
			calculoProgressaoDt.setTempoInterrupcaoAposDataBaseDias(calcularTempoInterrupcao_AposDataBase(listaEvento, calculoProgressaoDt.getDataBaseProgressao()));

			// -------------------------------------------------Nova fórmula------------------------------------------------------
			// conforme ata de reunião nº 15
			calcularTempoACumprir_AteDataBase_Progressao(listaEvento, calculoProgressaoDt, listaCondenacaoNaoExtinta, tipoRemicao);
			calcularTempoAcumprir_AposDataBase_Progressao(listaEvento, calculoProgressaoDt, listaCondenacaoNaoExtinta, tipoRemicao);

			// requisito temporal
			dataRequisito = calcularDataRequisitoProgressao_Livramento(Funcoes.StringToInt(calculoProgressaoDt.getTempoACumprirProgressaoDias()), listaEvento, calculoProgressaoDt.getDataBaseProgressao(), tipoRemicao, "");
			calculoProgressaoDt.setDataRequisitoTemporalProgressao(dataRequisito);
			// ------------------------------------------------------------------------------------------------------------------
		}

		return msgRetorno;
	}
	
	private String calcularDataRequisitoProgressao_Livramento(int tempoACumprir, List listaEvento, String dataBase, String tipoRemicao, String dataDecreto, boolean isLivramento) throws Exception{
		String dataRequisito = "";
		
		dataRequisito = calcularDataRequisitoProgressao_Livramento(tempoACumprir, listaEvento, dataBase, tipoRemicao, true, dataDecreto, isLivramento);			
		
		return dataRequisito;
	}
	
	private String calcularDataRequisitoProgressao_Livramento(int tempoACumprir, List listaEvento, String dataBase, String tipoRemicao, String dataDecreto) throws Exception{
		String dataRequisito = "";
		
		dataRequisito = calcularDataRequisitoProgressao_Livramento(tempoACumprir, listaEvento, dataBase, tipoRemicao, true, dataDecreto, false);			
		
		return dataRequisito;
	}

	/**
	 * Calcula a data do requisito temporal (fórmula: tempo a cumprir - dias cumpridos após a data base, até o cumprimento total do saldo devedor).
	 * 
	 * @param tempoACumprir
	 *            : tempo a cumprir para ter direito à progressão de regime (saldo devedor).
	 * @param listaEvento
	 * @param dataBase
	 * @return data do requisito temporal
	 * @throws Exception
	 */
	private String calcularDataRequisitoProgressao_Livramento(int tempoACumprir, List listaEvento, String dataBase, String tipoRemicao, boolean ehPrimeiraVez, String dataDecreto, boolean isLivramento) throws Exception{
		
		String dataEvento = ""; // data do evento que será somado o restante do tempo a cumprir (após subtraído os tempos cumpridos) 
		String id_ProcessoEventoExecucao_UltimoEventoSubtraido = ""; // id do último evento da lista que foi subtraído o tempo a cumprir
		String dataUltimoEventoSubtraido = ""; // data início do último evento da lista que foi subtraído o tempo a cumprir 
		String dataRequisito = "";
		
		if (ehPrimeiraVez){
			listaEvento = this.limparUsouTempoCumprido(listaEvento);
		}
		boolean flagAtingiuLivramento = false;
		//Soma faltas
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataInicio()) >= 0) {
				if (!evento.isUsouTempoCumprido() && evento.getTempoCumpridoDias().length() > 0) {
					int tempoCumprido = getTempoCumprido(evento);
					if (tempoCumprido < 0 ) {
						tempoACumprir -= tempoCumprido;
						evento.setUsouTempoCumprido(true);
					} 
				}
			}
		}
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			// verifica se a data início do evento está após a data base
			// (desconta o tempo cumprido do tempo a cumprir)
			if (Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataInicio()) >= 0) {

				if (!evento.isUsouTempoCumprido()){
					// tipo 1: deduzir o tempo de Remição após a data base, direto no Requisito Temporal
					// tipo 2: considerar todo o tempo de Remição (antes e depois da data base) como Tempo Cumprido até data base

					// subtrai o tempo cumprido do tempo a cumprir, até que o tempo a cumprir seja menor que o tempo cumprido
					// (neste momento, guarda a data e o id do evento)
					if ((evento.getTempoCumpridoDias().length() > 0)
							// para o tipo 2: para atender à ata de reunião nº 23
							&& !evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))) {

						int tempoCumprido = getTempoCumprido(evento);

						if (tempoACumprir > tempoCumprido) {
							tempoACumprir -= tempoCumprido;
							dataUltimoEventoSubtraido = evento.getDataInicio();
							id_ProcessoEventoExecucao_UltimoEventoSubtraido = evento.getId();
							evento.setUsouTempoCumprido(true);
						} else {
							flagAtingiuLivramento = true;
							dataEvento = evento.getDataInicio();
							break;
						}
					}
				}
			
				// se for o último evento da lista, é porque sobrou tempo a cumprir (após ter subtraído o tempo cumprido)
				if (i == listaEvento.size() - 1) {

					// verifica a data do último evento genérico (para somar o restante do tempo a cumprir a esta data)
					for (int w = listaEvento.size() - 1; w >= 0; w--) {
						ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(w);
						if ((processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))
								// qdo o TJ é a data base, pois o TJ não é um evento genérico
								|| ((processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
										|| processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) 
										&& processoEventoExecucaoDt.getDataInicio().equals(dataBase)) 
						)) {
							if (!processoEventoExecucaoDt.isUsouTempoCumprido()){
								dataEvento = processoEventoExecucaoDt.getDataInicio();
								if (dataUltimoEventoSubtraido.length() == 0 || Funcoes.StringToDate(dataEvento).after(Funcoes.StringToDate(dataUltimoEventoSubtraido))) {
									id_ProcessoEventoExecucao_UltimoEventoSubtraido = processoEventoExecucaoDt.getId();
								}	
							} else {
								dataRequisito = "-";
							}
							
							break;
						}		
					}
				}
				
			//verifica se a data fim está após a data base
			} else if (evento.getDataFim().length() > 0 
					&& Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataFim()) >= 0
					&& evento.getTempoCumpridoDias().length() > 0){
				int tempoCumprido = Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataFim());

				if (tempoACumprir > tempoCumprido) {
					tempoACumprir -= tempoCumprido;
					dataUltimoEventoSubtraido = evento.getDataInicio();
					// feito isso para não considerar o evento de falta (após o último evento genérico) duas vezes.
					id_ProcessoEventoExecucao_UltimoEventoSubtraido = evento.getId(); 
					evento.setUsouTempoCumprido(true);
				} else {
					dataEvento = dataBase;
					flagAtingiuLivramento = true;
					break;
				}
			}
		}

		// percorre a lista a partir do útlimo evento que foi subtraído o tempo cumprido do tempo a cumprir (identificado pelo id_ProcessoEventoExecucao)
		// para considerar o tempo de remição e falta após este evento.
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			if (evento.getId().equals(id_ProcessoEventoExecucao_UltimoEventoSubtraido)) {
				// verifica se tem evento de falta, se existir, soma o tempo de interrupção ao tempo a cumprir restante.
				for (int j = i + 1; j < listaEvento.size(); j++) {
					ProcessoEventoExecucaoDt e = (ProcessoEventoExecucaoDt) listaEvento.get(j);
					if (!e.isUsouTempoCumprido() && e.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA))) {
						tempoACumprir += getTempoInterrupcao(e);
						e.setUsouTempoCumprido(true);
					}
				}
			}

			// verifica se a data início do evento está após a data base (para considerar a remição após a data base)
			if (Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataInicio()) >= 0) {
				// para o tipo 2: o tempo de remição após a data base já foi
				// somado no tempo cumprido até a data base, para atender a ata de reunião nº 23

				// tipo 1: deduzir o tempo de Remição após a data base, direto no Requisito Temporal
				// tipo 2: considerar todo o tempo de Remição (antes e depois da data base) como Tempo Cumprido até data base
				// para o tipo1: verifica se tem evento de remição, subtrai(ou soma) o tempo de remição do tempo a cumprir restante.
				boolean isConsiderarRemicao = true;
				//verifica se a data do decreto está antes da data da remição
				if (dataDecreto.length() > 0 && Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), dataDecreto) < 0)
					isConsiderarRemicao = false;
					
				if (!evento.isUsouTempoCumprido() 
						&& tipoRemicao.equals("1") 
						&& evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))
						&& isConsiderarRemicao
						 && (!isLivramento || !flagAtingiuLivramento)) {
					// verifica se o tempoCumprido é negativo
					if (evento.getTempoCumpridoAnos().substring(0, 1).equals("(")){
						tempoACumprir += Funcoes.StringToInt(evento.getTempoCumpridoDias());
						evento.setUsouTempoCumprido(true);
					} else {
						tempoACumprir -= Funcoes.StringToInt(evento.getTempoCumpridoDias());
						evento.setUsouTempoCumprido(true);
					}
						
				}
			}
		}
		
		//verifico se o tempo a cumprir ficou maior que o tempo cumprido do último evento verificado (após considerar a remição e a falta)
		for (int i=0; i<listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			if (evento.getDataInicio().equals(dataEvento) && (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.TEMPO_CUMPRIDO_PRD))
					|| (evento.getDataFim().length() > 0 && !evento.isUsouTempoCumprido()))) {
				int tempoCumprido = getTempoCumprido(evento);
				if (tempoCumprido > 0 && tempoACumprir > tempoCumprido){
					dataRequisito = this.calcularDataRequisitoProgressao_Livramento(tempoACumprir, listaEvento, evento.getDataInicio(), tipoRemicao, false, dataDecreto, isLivramento);
				} else {
					dataRequisito = Funcoes.somaData(dataEvento, (tempoACumprir));
				}
				break;
			} 
		}
		
		try{
			// requisito temporal
			if (dataRequisito == ""){
				if (dataEvento.length() > 0)
					dataRequisito = Funcoes.somaData(dataEvento, tempoACumprir);
				else dataRequisito = Funcoes.somaData(dataBase, tempoACumprir);	
			}
				
			return dataRequisito; 
		} catch(MensagemException e) {
			throw new MensagemException("Não foi possível calcular a data do requisito temporal. Verifique a data de início dos eventos relacionados.");
		}
	}

	/**
	 * retorna a primeira data base da lista de evento
	 * 
	 * @param listaEvento
	 * @return dataBase: String;
	 * @author wcsilva
	 * @throws MensagemException 
	 */
	public String getPrimeiraDataBase(List listaEvento) throws MensagemException {

		// recebe os eventos da listaEvento que podem ser data base
		List eventosDataBase = getListaEventosDataBase_Da_ListaEventos(listaEvento); 

		// apenas os eventos Prisão Provisória e Primeiro Regime podem ser a primeira data base.
		for (int i = 0; i < eventosDataBase.size(); i++) {
			ProcessoEventoExecucaoDt e = (ProcessoEventoExecucaoDt) eventosDataBase.get(i);
			if (!e.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRISAO_PROVISORIA)) && !e.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME)) && !e.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO))) {
				eventosDataBase.remove(e);
				i--;
			}
		}

		// se houver evento(s) que pode(m) ser data base, retorna a data início do primeiro da lista de eventos que podem ser data base
		if (eventosDataBase.size() > 0) return ((ProcessoEventoExecucaoDt) eventosDataBase.get(0)).getDataInicio(); 
		else
			// se nenhum evento for evento de data base, retorna a data início do primeiro evento da lista de eventos
			if (listaEvento.size() > 0)
				return ((ProcessoEventoExecucaoDt) listaEvento.get(0)).getDataInicio(); 
			else
				throw new MensagemException("Não existem eventos para serem calculados");
	}

	/**
	 * Retorna o tempo cumprido do evento (positivo ou negativo)
	 * 
	 * @param processoEventoExecucaoDt
	 *            : evento
	 * @return int: tempoCumprido.
	 * @author wcsilva
	 */
	private int getTempoCumprido(ProcessoEventoExecucaoDt processoEventoExecucaoDt) {
		int tempoCumprido = 0;

		if (processoEventoExecucaoDt.getTempoCumpridoDias().length() > 0) {
			// verifica se o tempo cumprido na lista de eventos é negativo
			if (processoEventoExecucaoDt.getTempoCumpridoAnos().substring(0, 1).equals("(")) 
				tempoCumprido -= Funcoes.StringToInt(processoEventoExecucaoDt.getTempoCumpridoDias());
			else
				tempoCumprido += Funcoes.StringToInt(processoEventoExecucaoDt.getTempoCumpridoDias());
		}

		// // calcula tempo de interrupcao para o evento "FALTA"
		// if
		// ((processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA))
		// ||
		// processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA_LFS)))
		// && processoEventoExecucaoDt.getQuantidade().length() > 0) {
		// tempoCumprido -=
		// Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
		// }
		return tempoCumprido;
	}

	/**
	 * Calcula o restante da pena e tempo cumprido (referento ao último evento e à data atual).
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @throws Exception
	 * @author wcsilva
	 */
	private void calcularRestantePena_TempoCumprido_TerminoPena(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt) throws Exception{
		
		//Criando essa variável para descobrir quais registros estão gerando erros
		ProcessoEventoExecucaoDt eventoErro = new ProcessoEventoExecucaoDt();
		
		try{
			int restantePenaDataAtual = 0;
			int restantePenaUltimoEvento = 0;
			int tempoCumpridoDataAtual = 0;
			int tempoCumpridoUltimoEvento = 0;
			String dataInicioUltimoEventoGenerico = "";
			String dataTerminoPena = "";
			String dataTerminoPenaUnificada = "";

			// soma do tempo cumprido até o último evento.
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);

				tempoCumpridoUltimoEvento += getTempoCumprido(processoEventoExecucaoDt); 

				// utilizado para calcular o tempo cumprido até data atual.
				if (processoEventoExecucaoDt.getDataFim().length() == 0 
						&& (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO)) 
								|| isEventoPRD_Generico(processoEventoExecucaoDt.getId_EventoExecucao())))
					dataInicioUltimoEventoGenerico = processoEventoExecucaoDt.getDataInicio();
			}

			int diferencaEntreDatas = 0;
			if (dataInicioUltimoEventoGenerico.length() > 0) 
				diferencaEntreDatas = Funcoes.calculaDiferencaEntreDatas(dataInicioUltimoEventoGenerico, Funcoes.dateToStringSoData(new Date()));

			// Esta variável será utilizada nos cálculos de: provável término da pena, comutação e indulto.
			tempoCumpridoDataAtual = tempoCumpridoUltimoEvento + diferencaEntreDatas; 

			// calcula o restante da pena (referente ao último evento e à data atual).
			restantePenaDataAtual = Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias()) - tempoCumpridoDataAtual;
			restantePenaUltimoEvento = Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias()) - tempoCumpridoUltimoEvento;

			if (!getIdStatus(listaEvento).equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO)) && isIniciouCumprimentoPena(listaEvento)) {

				//informando o evento que pode gerar erro
				if(listaEvento.get(0) != null){
					eventoErro = (ProcessoEventoExecucaoDt) listaEvento.get(0);
				}
				
//				HashMap map = calcularDataRequisitoComutacao(Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias()), listaEvento, ((ProcessoEventoExecucaoDt) listaEvento.get(0)).getDataInicio(), true);
//				if (map.get("dataRequisito") != null) dataTerminoPena = map.get("dataRequisito").toString();
				
				dataTerminoPena = calcularDataRequisitoProgressao_Livramento(Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias()), listaEvento, ((ProcessoEventoExecucaoDt) listaEvento.get(0)).getDataInicio(), "1", "");

				// cálculo do provável término da pena unificada: neste caso,
				// considera-se o tempo máximo de condenação de 30 anos (10957 dias)
				if (calculoLiquidacaoDt.getVisualizaPenaUnificada() != null && (calculoLiquidacaoDt.getVisualizaPenaUnificada().equalsIgnoreCase("S") || calculoLiquidacaoDt.getVisualizaPenaUnificada().length() == 0)) {
					if (Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias()) > 10957) {
						dataTerminoPenaUnificada = calcularDataRequisitoProgressao_Livramento(10957, listaEvento, ((ProcessoEventoExecucaoDt) listaEvento.get(0)).getDataInicio(), "1", "");
					}
				}
			}
			calculoLiquidacaoDt.setDataTerminoPena(dataTerminoPena);
			calculoLiquidacaoDt.setDataTerminoPenaUnificada(dataTerminoPenaUnificada);

			if (restantePenaDataAtual <= 0) calculoLiquidacaoDt.setTempoRestanteDataAtualDias("0");
			else
				calculoLiquidacaoDt.setTempoRestanteDataAtualDias(String.valueOf(restantePenaDataAtual));
			if (restantePenaUltimoEvento <= 0) calculoLiquidacaoDt.setTempoRestanteUltimoEventoDias("0");
			else
				calculoLiquidacaoDt.setTempoRestanteUltimoEventoDias(String.valueOf(restantePenaUltimoEvento));

			calculoLiquidacaoDt.setTempoCumpridoDataAtualDias(String.valueOf(tempoCumpridoDataAtual));
			calculoLiquidacaoDt.setTempoCumpridoUltimoEventoDias(String.valueOf(tempoCumpridoUltimoEvento));

		} catch(MensagemException m) {
			throw new MensagemException(m.getMessage());
		
		}
	}

	/**
	 * Calcula o restante da pena e tempo cumprido até a data base.
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @throws Exception
	 * @author wcsilva
	 */
	private String calcularTempoCumpridoAteDataBase(List listaEvento, String dataBase, String tempoTotalCondenacaoDias, String tipoRemicao) throws Exception{
		
		int tempoCumpridoAteDataBase = 0;

		// soma do tempo cumprido.
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			// verifica se a data início do evento está antes da data base ou é igual à data base (no caso do TJ ser data base)
			if (Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), dataBase) >= 0) {

				//verifica se a data início está antes da data base
				if (Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), dataBase) > 0){
					
					if (processoEventoExecucaoDt.getDataFim().length() > 0){
						//verifica se a data fim está antes da data base
						if (Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataFim(), dataBase) >= 0){
							tempoCumpridoAteDataBase += getTempoCumprido(processoEventoExecucaoDt);
							
						} else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))){
							tempoCumpridoAteDataBase += Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), dataBase);
						}
						
					} else if (processoEventoExecucaoDt.getTempoCumpridoDias().length() > 0)
						tempoCumpridoAteDataBase += getTempoCumprido(processoEventoExecucaoDt);
					
					else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO)))
						tempoCumpridoAteDataBase += Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), dataBase);
					
				}

				// ob.: a remição antes da data base é considerada como tempo cumprido para o tipo 1 e tipo 2.
				// para o tipo 2: inclui no tempo cumprido até a data base, o tempo de remição após a data base. (Atendendo solicitação conforme Ata de Reunião nº 23)
				// tipo 1: deduzir o tempo de Remição após a data base, direto no Requisito Temporal
				// tipo 2: considerar todo o tempo de Remição (antes e depois da data base) como Tempo Cumprido até data base
			} else {
				if (tipoRemicao.equals("2") && processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))) {
					tempoCumpridoAteDataBase += getTempoCumprido(processoEventoExecucaoDt);
				}
			}
		}

		return String.valueOf(tempoCumpridoAteDataBase);

		
	}

	/**
	 * Calcula o tempo de remição, considerando as horas de estudo e dias trabalhados
	 * 
	 * @param listaEvento
	 *            , lista com os evenots
	 * @param calculoLiquidacaoDt
	 *            , objeto com os dados do cálculo
	 * @throws Exception
	 * @author wcsilva
	 */
	private void calcularRemicao(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt){
		
		int diasTrabalhados = 0;
		int horasEstudo = 0;
		int tempoRemicaoTrabalho = 0;
		int tempoRemicaoEstudo = 0;
		int tempoEstudo = 0;

		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))) {
				if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_TRABALHADOS_REMICAO))) 
					diasTrabalhados -= Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.DIAS_TRABALHADOS_REMICAO))) 
					diasTrabalhados += Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMICAO))) 
					horasEstudo -= Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.HORAS_ESTUDO_REMICAO))) 
					horasEstudo += Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.DIAS_LEITURA_REMIDO)))
					tempoEstudo += Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_LEITURA_REMIDO)))
					tempoEstudo -= Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.HORAS_ESTUDO_REMIDO)))
					tempoEstudo += Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
				else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMIDO)))
					tempoEstudo -= Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
			}
		}

		tempoRemicaoEstudo = (int) Math.ceil((double) horasEstudo * 1 / Funcoes.StringToInt(calculoLiquidacaoDt.getQtdeTempoHorasEstudo()));
		tempoRemicaoTrabalho = (int) Math.ceil((double) diasTrabalhados * 1 / 3);

		calculoLiquidacaoDt.setTotalDiasTrabalhados(String.valueOf(diasTrabalhados));
		calculoLiquidacaoDt.setTotalHorasEstudo(String.valueOf(horasEstudo));
		calculoLiquidacaoDt.setTempoTotalRemicaoEstudoDias(String.valueOf(tempoRemicaoEstudo));
		calculoLiquidacaoDt.setTempoTotalRemicaoTrabalhoDias(String.valueOf(tempoRemicaoTrabalho));
		calculoLiquidacaoDt.setTempoTotalRemicaoLeituraDias(String.valueOf(tempoEstudo));

	}

	/**
	 * Calcula o tempo que falta cumprir para obter a PRIMEIRA progressão de regime, aplicando a fração correspondente no total da condenação de cada crime. Identifica o tempo de condenação correspondente a cada tipo de crime (comum, hediondo e hediondo reincidente). Se já houver tempo a cumprir no objeto calculoLiquidacaoDt, soma com o tempo existente.
	 * 
	 * @param listaEvento
	 *            - lista com os eventos
	 * @param calculoLiquidacaoDt
	 *            - objeto com os dados do cálculo de liquidação de pena
	 * @param listaCondenacao
	 *            - lista com as condenações
	 * @author wcsilva
	 * @throws Exception 
	 */
	public void calcularTempoAcumprir_AposDataBase_Progressao(List listaEvento, CalculoLiquidacaoProgressaoLivramentoDt calculoProgressaoDt, List listaCondenacao, String tipoRemicao) throws Exception{
		
		int tempoACumprir = 0;
		int tempoCondenacaoComum = 0; // considera-se o tempo de crime comum as condenações de crime comum e hediondo até 28/03/2007
		int tempoCondenacaoHediondo = 0;
		int tempoCondenacaoHediondoReincidente = 0;

		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);

			// percorre a lista até encontrar o evento TJ ou GRP da condenação referenciada pelo id_ProcessoExecucao
			for (int w = 0; w < listaEvento.size(); w++) {
				if (((ProcessoEventoExecucaoDt) listaEvento.get(w)).getId_ProcessoExecucao().equals(condenacaoExecucaoDt.getId_ProcessoExecucao()) && (((ProcessoEventoExecucaoDt) listaEvento.get(w)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) || ((ProcessoEventoExecucaoDt) listaEvento.get(w)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)))) {
					// verifica se o TJ está após a data base
					if (Funcoes.calculaDiferencaEntreDatas(calculoProgressaoDt.getDataBaseProgressao(), ((ProcessoEventoExecucaoDt) listaEvento.get(w)).getDataInicioCumpirmentoPena()) > 0) {
						
						if (calculoProgressaoDt.getReincidenteHediondoPR().equalsIgnoreCase("true") && condenacaoExecucaoDt.isHediondoProgressao()){
							tempoCondenacaoHediondoReincidente += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
						} else {
							if (condenacaoExecucaoDt.isHediondoProgressao() && condenacaoExecucaoDt.isReincidente()) {
								tempoCondenacaoHediondoReincidente += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
							} else if (condenacaoExecucaoDt.isHediondoProgressao() && !condenacaoExecucaoDt.isReincidente()) {
								tempoCondenacaoHediondo += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
							} else if (!condenacaoExecucaoDt.isHediondoProgressao()) {
								tempoCondenacaoComum += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
							}
						}
						
					}
					break;
				}
			}
		}

		// verifica se existe tempo cumprido até a data base que não foi considerado:
		// se não encontrar nenhuma condenação anterior à data base
		// selecionada, o tempo cumprido não será considerado (indica que
		// não houve tempo a cumprir antes da data base) - método
		// .calcularTempoAAcumprir_AteDataBase_Progressao
		if (calculoProgressaoDt.getTempoCumpridoDataBaseDias().length() > 0 
				&& (calculoProgressaoDt.getTempoACumprirProgressaoDias().length() == 0 
						|| Funcoes.StringToInt(calculoProgressaoDt.getTempoACumprirProgressaoDias()) == 0)) {
			// se não tem tempo a cumprir, é pq não encontrou condenação para descontar o tempo cumprido considera primeiro a condenação dos crimes mais graves
			int tempoCumpridoADeduzir = Funcoes.StringToInt(calculoProgressaoDt.getTempoCumpridoDataBaseDias());
			if (tempoCondenacaoHediondoReincidente > 0) {
				if(tempoCumpridoADeduzir > tempoCondenacaoHediondoReincidente){
					tempoCumpridoADeduzir -= tempoCondenacaoHediondoReincidente;
					tempoCondenacaoHediondoReincidente = 0;
				} else{
					tempoCondenacaoHediondoReincidente -= tempoCumpridoADeduzir;
					tempoCumpridoADeduzir=0;
				}
			}
			if (tempoCondenacaoHediondo > 0) {
				if(tempoCumpridoADeduzir > tempoCondenacaoHediondo){
					tempoCumpridoADeduzir -= tempoCondenacaoHediondo;
					tempoCondenacaoHediondo = 0;
				} else{
					tempoCondenacaoHediondo -= tempoCumpridoADeduzir;
					tempoCumpridoADeduzir=0;
				}
			}
			if (tempoCondenacaoComum > 0) {
				if(tempoCumpridoADeduzir > tempoCondenacaoComum){
					tempoCumpridoADeduzir -= tempoCondenacaoComum;
					tempoCondenacaoComum = 0;
				} else{
					tempoCondenacaoComum -= tempoCumpridoADeduzir;
					tempoCumpridoADeduzir=0;
				}
			} 
		}

		tempoACumprir = (int) Math.ceil(((double) tempoCondenacaoComum * 1 / 6) + ((double) tempoCondenacaoHediondo * 2 / 5) + ((double) tempoCondenacaoHediondoReincidente * 3 / 5));

		setTempoACumprirProgressao(calculoProgressaoDt, tempoACumprir, tempoCondenacaoHediondoReincidente, tempoCondenacaoHediondo, tempoCondenacaoComum);

	}

	/**
	 * Calcula o tempo que falta cumprir para obter a progressão de regime, aplicando a fração correspondente. Identifica o tempo de condenação correspondente a cada tipo de crime (comum, hediondo e hediondo reincidente) Se já houver tempo a cumprir no objeto calculoLiquidacaoDt, soma com o tempo existente.
	 * 
	 * @param listaEvento
	 *            - lista com os eventos
	 * @param calculoLiquidacaoDt
	 *            , objeto com os dados do cálculo de liquidação de pena
	 * @param listaCondenacao
	 *            - lista com as condenações
	 * @author wcsilva
	 * @throws Exception
	 */
	public void calcularTempoACumprir_AteDataBase_Progressao(List listaEvento, CalculoLiquidacaoProgressaoLivramentoDt calculoProgressaoDt, List listaCondenacao, String tipoRemicao) throws Exception{
		
		// Prepara Lista para o cálculo, setando o atributo temporário com o valor cumprido de cada evento
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			int dias = Funcoes.StringToInt(evento.getTempoCumpridoDias());
			if(evento.getTempoCumpridoAnos().startsWith("(")) dias = -dias;
			evento.setTempoCumpridoCalculadoDias(dias);				
		}
		List listaCondenacaoOrdenada = ordenarCrimeMaisGrave_ateDataBase(listaCondenacao, listaEvento, calculoProgressaoDt.getDataBaseProgressao(), true);

		int tempoCumpridoTotal = Funcoes.StringToInt(calculoProgressaoDt.getTempoCumpridoDataBaseDias());
		int tempoACumprir = 0;
		int tempoHediondoReincidente = 0;
		int tempoHediondo = 0;
		int tempoComum = 0;

		for (int i = 0; i < listaCondenacaoOrdenada.size(); i++) {

			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(i);

			HashMap map = calcularRestantePenaCrime((CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(i), calculoProgressaoDt.getDataBaseProgressao(), listaEvento, tempoCumpridoTotal, i, tipoRemicao);
			int restantePenaCrime = Funcoes.StringToInt(map.get("restantePenaCrime").toString());
			tempoCumpridoTotal = Funcoes.StringToInt(map.get("tempoCumpridoTotal").toString());

			if (calculoProgressaoDt.getReincidenteHediondoPR().equalsIgnoreCase("true") && condenacaoExecucaoDt.isHediondoProgressao()){
				tempoHediondoReincidente += restantePenaCrime;
			} else {
				if (condenacaoExecucaoDt.isHediondoProgressao() && condenacaoExecucaoDt.isReincidente()) {
					tempoHediondoReincidente += restantePenaCrime;
				} else if (condenacaoExecucaoDt.isHediondoProgressao() && !condenacaoExecucaoDt.isReincidente()) {
					tempoHediondo += restantePenaCrime;
				} else if (!condenacaoExecucaoDt.isHediondoProgressao()) {
					tempoComum += restantePenaCrime;
				}
			}
		}

		tempoACumprir = (int) Math.ceil(((double) tempoComum * 1 / 6) + ((double) tempoHediondo * 2 / 5) + ((double) tempoHediondoReincidente * 3 / 5));

		setTempoACumprirProgressao(calculoProgressaoDt, tempoACumprir, tempoHediondoReincidente, tempoHediondo, tempoComum);

	}

	/**
	 * calcula o restante da pena para cada condenação método deve ser utilizado dentro do laço de repetição da lista de condenação ordenada.
	 * 
	 * @param condenacaoExecucaoDt
	 * @param dataBase
	 * @param listaEvento
	 * @param tempoCumpridoTotal
	 * @param posicaoListaCondenacao
	 *            : identifica se é a primeira condenação a calcular o restante da pena, para considerar o tempo cumprido na prisão provisória, se houver
	 * @return int: restante da pena do crime
	 * @throws Exception
	 */
	public HashMap calcularRestantePenaCrime(CondenacaoExecucaoDt condenacaoExecucaoDt, String dataBase, List listaEvento, int tempoCumpridoTotal, int posicaoListaCondenacao, String tipoRemicao) throws Exception{
		HashMap map = new HashMap();
		
		int tempoCumpridoCrime = 0; // soma dos tempos cumpridos do crime até a data base
		int restantePenaCrime = 0;

		if (tempoCumpridoTotal > 0) {
			tempoCumpridoCrime = calcularTempoCumpridoDoCrime(listaEvento, dataBase, condenacaoExecucaoDt, posicaoListaCondenacao, tipoRemicao);

			// caso o tempo cumprido do crime seja maior que o tempo de
			// condenação, considera-se somente o tempo da condenação
			if (tempoCumpridoCrime > Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias())) {
				tempoCumpridoCrime = Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
			}

			if (tempoCumpridoTotal <= tempoCumpridoCrime) tempoCumpridoCrime = tempoCumpridoTotal;

			tempoCumpridoTotal -= tempoCumpridoCrime;
			restantePenaCrime = Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias()) - tempoCumpridoCrime;
		} else
			restantePenaCrime = Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());

		map.put("restantePenaCrime", restantePenaCrime);
		map.put("tempoCumpridoTotal", tempoCumpridoTotal);
		

		return map;
	}

	/**
	 * Seta o valor das variáveis: tempoACumprir, tempoHediondoReincidente, tempoHediondo e tempoComum no objeto cálculoLiquidacaoDt para o cálculo de progressao de regime
	 * 
	 * @param calculoLiquidacaoDt
	 *            : objeto que contém os dados do cálculo de liquidação de penas
	 * @param tempoACumprir
	 *            : tempo a cumprir em dias
	 * @param tempoHediondoReincidente
	 *            : tempo em dias de condenação em crime hediondo reincidente
	 * @param tempoHediondo
	 *            : tempo em dias de condenação em crime hediondo sem reincidência
	 * @param tempoComum
	 *            : tempo em dias de condenação em crime comum
	 * @author wcsilva
	 */
	private void setTempoACumprirProgressao(CalculoLiquidacaoProgressaoLivramentoDt calculoProgressaoDt, int tempoACumprir, int tempoHediondoReincidente, int tempoHediondo, int tempoComum) {

		if (tempoComum > 0) {
			if (calculoProgressaoDt.getTempoCondenacaoComumProgressaoDias().length() > 0) tempoComum += Funcoes.StringToInt(calculoProgressaoDt.getTempoCondenacaoComumProgressaoDias());
			calculoProgressaoDt.setTempoCondenacaoComumProgressaoDias(String.valueOf(tempoComum));
		}
		if (tempoHediondo > 0) {
			if (calculoProgressaoDt.getTempoCondenacaoHediondoProgressaoDias().length() > 0) tempoHediondo += Funcoes.StringToInt(calculoProgressaoDt.getTempoCondenacaoHediondoProgressaoDias());
			calculoProgressaoDt.setTempoCondenacaoHediondoProgressaoDias(String.valueOf(tempoHediondo));
		}
		if (tempoHediondoReincidente > 0) {
			if (calculoProgressaoDt.getTempoCondenacaoHediondoReincidenteProgressaoDias().length() > 0) tempoHediondoReincidente += Funcoes.StringToInt(calculoProgressaoDt.getTempoCondenacaoHediondoReincidenteProgressaoDias());
			calculoProgressaoDt.setTempoCondenacaoHediondoReincidenteProgressaoDias(String.valueOf(tempoHediondoReincidente));
		}

		if (calculoProgressaoDt.getTempoACumprirProgressaoDias().length() > 0) tempoACumprir += Funcoes.StringToInt(calculoProgressaoDt.getTempoACumprirProgressaoDias());
		calculoProgressaoDt.setTempoACumprirProgressaoDias(String.valueOf(tempoACumprir));
	}

	/**
	 * Calcula o tempo cumprido da data início de cumprimento do Trânsito em Julgado ou Guia de Recolhimento Provisória do crime referenciado pelo Id_ProcessoExecução até a data base
	 * 
	 * @param listaEvento
	 *            : lista com os eventos
	 * @param dataBase
	 *            : data base do cálculo de progressão de regime
	 * @param id_ProcessoExecucao
	 *            : idProcessoExecução, que será a referência para o crime
	 * @param posicaoListaCondenacao
	 *            : identifica se é a primeira condenação a calcular o restante da pena, para considerar o tempo cumprido na prisão provisória, se houver
	 * @return tempoCumprido: qtde de dias de tempo cumprido.
	 * @throws Exception
	 * @author wcsilva
	 */
	private int calcularTempoCumpridoDoCrime(List listaEvento, String dataBase, CondenacaoExecucaoDt condenacaoExecucaoDt, int posicaoListaCondenacao, String tipoRemicao) throws Exception{
		int tempoCumprido = 0;
	
		String dataInicioCumprimentoTJ = ""; // data início de cumprimento do TJ ou GRP do crime referenciado pelo id_ProcessoExecucao
		//String dataInicioProximoEvento = ""; // data início do próximo evento que não seja de remissão

		// percorre a lista até encontrar o evento TJ ou GRP do crime
		// referenciado pelo id_ProcessoExecucao (para guardar a data início
		// de cumprimento da condenação)
		for (int i = 0; i < listaEvento.size(); i++) {
			if (((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_ProcessoExecucao().equals(condenacaoExecucaoDt.getId_ProcessoExecucao()) 
					&& (((ProcessoEventoExecucaoDt) listaEvento.get(i)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)))) {

				// captura data início de cumprimento da condenação do TJ ou GRP do crime
				dataInicioCumprimentoTJ = ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getDataInicioCumpirmentoPena();
				break;
			}
		}

		int restante = Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
		if (dataInicioCumprimentoTJ.length() > 0) {
			// percorre a lista até encontrar o evento com data início maior ou igual à dataInicioCumprimentoTJ
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);

				// verifica o evento está antes da data base...
				// e se a data início do evento é a mesma ou posterior à dataInicioCumprimentoTJ pois este lapso (se existir) não é considerado como 'tempo cumprido' na lista de evento.
				if (Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), dataBase) > 0 && 
						(Funcoes.calculaDiferencaEntreDatas(dataInicioCumprimentoTJ, evento.getDataInicio()) >= 0 || (evento.getDataFim()!=null && !"".equals(evento.getDataFim()) && Funcoes.calculaDiferencaEntreDatas(dataInicioCumprimentoTJ, evento.getDataFim()) >= 0))) {
					// captura data início do TJ ou GRP do crime
					//dataInicioProximoEvento = evento.getDataInicio();

					// percorre a lista para somar os tempos cumpridos e
					// calcular o tempo de falta até a data base
					for (int j = i; j < listaEvento.size(); j++) {
						ProcessoEventoExecucaoDt e = (ProcessoEventoExecucaoDt) listaEvento.get(j);
						if (Funcoes.calculaDiferencaEntreDatas(e.getDataInicio(), dataBase) > 0) {
							int t =  e.getTempoCumpridoCalculadoDias();
							if(t < restante){
								tempoCumprido += t;
								restante -= t;
								e.setTempoCumpridoCalculadoDias(0);
							} else {
								tempoCumprido += restante;
								e.setTempoCumpridoCalculadoDias(t-restante);
								restante = 0;
								return tempoCumprido;
							}
							
							

							// caso a data base seja posterior à data início
							// do últmo evento genérico da lista, considera
							// o tempo cumprido da data início do evento
							// genérico até a data base
							if (e.getDataFim().length() == 0 
									&& e.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))) 
								tempoCumprido += Funcoes.calculaDiferencaEntreDatas(e.getDataInicio(), dataBase);

							// tipo 2: para a primeira condenação, inclui no
							// tempo cumprido, o tempo de remição após a
							// data base. (Atendendo solicitação conforme
							// Ata de Reunião nº 23)
						} else {
							// tipo 1: deduzir o tempo de Remição após a
							// data base, direto no Requisito Temporal
							// tipo 2: considerar todo o tempo de Remição
							// (antes e depois da data base) como Tempo
							// Cumprido até data base
							if (tipoRemicao.equals("2") && posicaoListaCondenacao == 0 
									&& e.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))) {
								int t =  e.getTempoCumpridoCalculadoDias();
								if(t < restante){
									tempoCumprido += t;
									restante -= t;
									e.setTempoCumpridoCalculadoDias(0);
								} else {
									tempoCumprido += restante;
									restante = 0;
									e.setTempoCumpridoCalculadoDias(t-restante);
								}
							}
						}
					}
					break;
				}
			}
			
		}

		
		
		return tempoCumprido;
	}

	/**
	 * Calcula o tempo cumprido até a data referenciada (data início de algum evento da lista de eventos)
	 * 
	 * @param listaEvento
	 *            , lista com os eventos do processo de execução penal
	 * @param data
	 *            , data referência para o cálculo
	 * @return tempoCumprido, tempo cumprido até a data informada
	 * @throws Exception
	 */
	public int calcularTempoCumpridoAteDataInicioEvento(List listaEvento, String data) throws Exception{
		int tempoCumprido = 0;
		
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			// verifica se a data início é anterior à data referência para fazer o cálculo do tempo cumprido até a data referência
			if (Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), data) > 0) {

				if (processoEventoExecucaoDt.getTempoCumpridoDias().length() > 0) {
					// verifica se a data fim do evento é anterior à data referência
					if (processoEventoExecucaoDt.getDataFim().length() == 0 || Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataFim(), data) >= 0) {
						tempoCumprido += getTempoCumprido(processoEventoExecucaoDt);
					} else
						tempoCumprido += Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), data);
				// o evento genérico considera o tempo como tempo cumprido
				} else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))) 
					tempoCumprido += Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), data);

			} else
				break;
		}

		return tempoCumprido;
	}
	
	/**
	 * Calcula o tempo cumprido da pena por crimes comuns até a data referenciada (data início de algum evento da lista de eventos)
	 * 
	 * @param listaEvento
	 *            , lista com os eventos do processo de execução penal
	 * @param data
	 *            , data referência para o cálculo
	 * @return tempoCumprido, tempo cumprido até a data informada
	 * @throws Exception
	 */
	public int calcularTempoCumpridoCrimesNaoHediondos(List listaEvento, List listaCondenacao, String data) throws Exception{
	    int tempoCumpridoComum = 0;
	    int numeroDeCondenacoes = listaCondenacao.size();
	    int[] tempoCondenacao = new int[numeroDeCondenacoes];
	    String[] dicc = new String[numeroDeCondenacoes];
	    boolean[] isHediondo = new boolean[numeroDeCondenacoes];
	    for(int c=0;c<numeroDeCondenacoes;c++) {
		CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacao.get(c);
		tempoCondenacao[c] = Funcoes.StringToInt(condenacao.getTempoPenaEmDias());
		dicc[c] = condenacao.getDataInicioCumprimentoPena();
		isHediondo[c] = condenacao.isHediondoLivramento() || condenacao.isHediondoProgressao();
	    }
	    
		for (int i = 0; i < listaEvento.size(); i++) {
		    ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);	
		    if (Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), data) <= 0) 
				break;
			    int tempoAContabilizar = getTempoCumprido(evento);
			    for(int c = 0; c < numeroDeCondenacoes; c++) {
					if(Funcoes.calculaDiferencaEntreDatas(dicc[c], evento.getDataInicio()) >= 0){
					    if (tempoCondenacao[c] > 0){
			        		if (tempoAContabilizar > 0) {
			        		    // verifica se a data fim do evento é anterior à data referência
			        		    if (evento.getDataFim().length() == 0 || Funcoes.calculaDiferencaEntreDatas(evento.getDataFim(), data) >= 0) {
			        			 if(tempoAContabilizar > tempoCondenacao[c]){
			        			     tempoAContabilizar -= tempoCondenacao[c];
				        		     if(!isHediondo[c])
					        		tempoCumpridoComum += tempoCondenacao[c];
				        		     tempoCondenacao[c]=0;
				        		 } else {
				        		     if(!isHediondo[c])
				        			 tempoCumpridoComum += tempoAContabilizar;
				        		     tempoCondenacao[c] -= tempoAContabilizar;
				        		     tempoAContabilizar = 0;
				        		     break;
				        		}
			        		    } else{
			        			int tempoAtual = Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), data);
			        		        if(!isHediondo[c])
			        		      		tempoCumpridoComum += tempoAtual;
			        		      	tempoCondenacao[c] -= tempoAtual;
				        		break;
			        		    }
			        		} else {
			        		    // o evento genérico considera o tempo como tempo cumprido
			        		    if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))) {
			        			int tempoAtual = Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), data);
			        		        if(tempoAtual > tempoCondenacao[c]){
			        		            tempoAContabilizar = tempoAtual-tempoCondenacao[c];
			        		            if(!isHediondo[c])
				        		        tempoCumpridoComum += tempoCondenacao[c];
			        		            tempoCondenacao[c]=0;
			        		        } else {
			        		            tempoAContabilizar = 0;
			        		            if(!isHediondo[c])
				        		        tempoCumpridoComum += tempoAtual;
			        		            tempoCondenacao[c] -= tempoAtual;
			        		            break;
			        		        }
			        		    }
			        		}
					    }
					}
		    }
    	}
	    
	    
	    return tempoCumpridoComum;
	}

	/**
	 * calcula tempo de interrupção após a data base
	 * 
	 * @param listaEvento
	 *            : lista com os eventos (ProcessoEventoExecucaoDt)
	 * @param dataBase
	 *            : String no formato dd/mm/aaa
	 * @return tempoInterrupção: tempo em dias
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularTempoInterrupcao_AposDataBase(List listaEvento, String dataBase) throws Exception{
		int tempoInterrupcao = 0;
		
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			// verifica se a data início do evento está após a data base
			if (Funcoes.calculaDiferencaEntreDatas(dataBase, processoEventoExecucaoDt.getDataInicio()) >= 0) {
				tempoInterrupcao += getTempoInterrupcao(processoEventoExecucaoDt);
			}
		}
				
		return String.valueOf(tempoInterrupcao);
	}

	/**
	 * calcula tempo de interrupção total
	 * 
	 * @param listaEvento
	 *            : lista com os eventos (ProcessoEventoExecucaoDt)
	 * @return tempoInterrupção: tempo em dias
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularTempoInterrupcaoTotal(List listaEvento) throws Exception{
		int tempoInterrupcao = 0;
		
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			tempoInterrupcao += getTempoInterrupcao(processoEventoExecucaoDt);
		}	
		
		return String.valueOf(tempoInterrupcao);
	}

	/**
	 * calcula tempo de interrupção até data referência
	 * 
	 * @param listaEvento
	 *            : lista com os eventos (ProcessoEventoExecucaoDt)
	 * @param data
	 *            : data referência
	 * @return tempoInterrupção: tempo em dias
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularTempoInterrupcaoAteData(List listaEvento, String data) throws Exception{
		int tempoInterrupcao = 0;
		
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			// verifica se a data início é anterior à data referência para
			// fazer o cálculo do tempo cumprido até a data referência
			if (Funcoes.StringToDate(processoEventoExecucaoDt.getDataInicio()).before(Funcoes.StringToDate(data))) {
				tempoInterrupcao += getTempoInterrupcao(processoEventoExecucaoDt);

				// calcula tempo de interrupcao do evento de interrupção,
				// que não fechou data, até data referência
				if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INTERRUPCAO)) && (processoEventoExecucaoDt.getDataFim() == null || processoEventoExecucaoDt.getDataFim().length() == 0)) {
					tempoInterrupcao += Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), data) + 1;
				}
			}
		}
				
		return String.valueOf(tempoInterrupcao);
	}

	/**
	 * calcula o tempo de interrupção do evento
	 * 
	 * @param processoEventoExecucaoDt
	 *            : evento
	 * @return tempo de interrupção em dias
	 * @throws Exception
	 */
	public int getTempoInterrupcao(ProcessoEventoExecucaoDt processoEventoExecucaoDt) throws Exception{
		int tempoInterrupcao = 0;

		// calcula tempo de interrupcao após a data base para o evento
		// "FUGA"
		if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INTERRUPCAO)) || isEventoPRD_Interrupcao(processoEventoExecucaoDt.getId_EventoExecucao())) {
			// calcula tempo de interrupcao
			if (processoEventoExecucaoDt.getDataFim() != null && processoEventoExecucaoDt.getDataFim().length() > 0) tempoInterrupcao += Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), processoEventoExecucaoDt.getDataFim()) + 1;

			// calcula tempo de interrupcao para o evento "FALTA"
		} else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA))) {
			// calcula o tempo de interrupção
			if (processoEventoExecucaoDt.getQuantidade().length() > 0) tempoInterrupcao += Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade());
		}
		// calcula o tempo de interrupção para o evento de
		// "CONCESSÃO DO LIVRAMENTO CONDICIONAL" quando houve revogação e
		// não foi considerado o tempo cumprido deste evento
		else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))) {
			if (processoEventoExecucaoDt.getDataFim().length() > 0 && Funcoes.StringToInt(processoEventoExecucaoDt.getTempoCumpridoDias()) == 0) {
				tempoInterrupcao += Funcoes.calculaDiferencaEntreDatas(processoEventoExecucaoDt.getDataInicio(), processoEventoExecucaoDt.getDataFim()) + 1;
			}
		}
				
		return tempoInterrupcao;
	}

	/**
	 * Ordena a lista de condenações do crime mais grave para o crime menos grave ocorridos até a data base
	 * 
	 * @param listaCondenacao
	 * @param listaEvento
	 * @param dataBase
	 * @return listaCondenacaoOrdenada
	 * @author wcsilva
	 */
	public List ordenarCrimeMaisGrave_ateDataBase(List listaCondenacao, List listaEvento, String dataBase, boolean isIniciouCumprimentoPena) throws Exception {
		List listaCondenacaoOrdenada = new ArrayList();
		List listaCrimeComum = new ArrayList();
		List listaCrimeHediondo = new ArrayList();
		String dataInicioCumprimentoPena = "";
		if (listaCondenacao != null) {
			for (int i = 0; i < listaCondenacao.size(); i++) {
				for (int j = 0; j < listaEvento.size(); j++) {
					ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(j);

					if (processoEventoExecucaoDt.getId_ProcessoExecucao().equals(((CondenacaoExecucaoDt) listaCondenacao.get(i)).getId_ProcessoExecucao()) 
							&& (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
									|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)))) {

						if (isIniciouCumprimentoPena) 
							dataInicioCumprimentoPena = processoEventoExecucaoDt.getDataInicioCumpirmentoPena();
						else
							dataInicioCumprimentoPena = ((CondenacaoExecucaoDt) listaCondenacao.get(i)).getDataTransitoJulgado();

						// verifica os crimes dos TJ anteriores ou igual à data base
						if (isIniciouCumprimentoPena && (processoEventoExecucaoDt.getDataInicioCumpirmentoPena().length() == 0 || dataBase.length() == 0)) {
							throw new MensagemException("Existe condenação sem DICC (Data de Início de Cumprimento da Condenação). Verifique!");

						} else if (Funcoes.calculaDiferencaEntreDatas(dataInicioCumprimentoPena, dataBase) >= 0) {
							if (((CondenacaoExecucaoDt) listaCondenacao.get(i)).isHediondoProgressao()) 
								listaCrimeHediondo.add(listaCondenacao.get(i));
							else
								listaCrimeComum.add(listaCondenacao.get(i));
						}
					}
				}
			}
		}

		// ordena os TJ pelo crime (primeiro hediondo, depois comum), pela data do TJ em ordem crescente e pela maior pena em ordem crescente.
		if (listaCrimeHediondo.size() > 0) {
			listaCrimeHediondo = ordenarListaTJ_MaiorPena(listaCrimeHediondo);
		}
		if (listaCrimeComum.size() > 0) {
			listaCrimeComum = ordenarListaTJ_MaiorPena(listaCrimeComum);
		}
		listaCondenacaoOrdenada.addAll(listaCrimeHediondo);
		listaCondenacaoOrdenada.addAll(listaCrimeComum);
		return listaCondenacaoOrdenada;
	}

	// ordena os TJ pelo crime (primeiro hediondo, depois comum), pela data do TJ em ordem crescente e pela maior pena em ordem crescente.
	private List ordenarListaTJ_MaiorPena(List lista) throws Exception{
		
		if (lista.size() > 0) {
			for (int w = 0; w < lista.size(); w++) {
				CondenacaoExecucaoDt cond1 = (CondenacaoExecucaoDt) lista.get(w);
				CondenacaoExecucaoDt cond2 = null;
				// verifica se não é a última posicao
				if (w < lista.size() - 1) {
					cond2 = (CondenacaoExecucaoDt) lista.get(w + 1);
				}
				if (cond2 != null) {
					if (cond2.getDataTransitoJulgado().length() > 0 && cond1.getDataTransitoJulgado().length() > 0) {
						int difData = Funcoes.calculaDiferencaEntreDatas(cond1.getDataTransitoJulgado(), cond2.getDataTransitoJulgado());
						// verifica se a data do TJ da cond2 é menor que a data da cond1
						if (difData < 0) {
							lista.remove(cond1);
							lista.remove(cond2);
							lista.add(w, cond2);
							lista.add(w + 1, cond1);

						// verifica a maior pena, caso seja a mesma data
						} else if (difData == 0) {
							if (Funcoes.StringToInt(cond2.getTempoPenaEmDias()) > Funcoes.StringToInt(cond1.getTempoPenaEmDias())) {
								lista.remove(cond1);
								lista.remove(cond2);
								lista.add(w, cond2);
								lista.add(w + 1, cond1);
							}
						}
					}
				}
			}
		}
				
		return lista;
	}

	/**
	 * Ordena a lista de condenações do crime mais grave para o crime menos grave
	 * 
	 * @param listaCondenacao
	 *            : lista das condenações
	 * @param listaEvento
	 *            : lista com os eventos
	 * @return
	 * @throws Exception
	 */
	public List ordenarCrimeMaisGrave(List listaCondenacao, List listaEvento) throws Exception {
		List listaCondenacaoOrdenada = new ArrayList();

		// para ordenar todos os crimes, informa como data base, a data fim do último evento da lista de eventos
		String dataBase = "";
		if (((ProcessoEventoExecucaoDt) listaEvento.get(listaEvento.size() - 1)).getDataFim().length() > 0)
			dataBase = ((ProcessoEventoExecucaoDt) listaEvento.get(listaEvento.size() - 1)).getDataFim();
		else dataBase = ((ProcessoEventoExecucaoDt) listaEvento.get(listaEvento.size() - 1)).getDataInicio();
		listaCondenacaoOrdenada = ordenarCrimeMaisGrave_ateDataBase(listaCondenacao, listaEvento, dataBase, true);
		
		return listaCondenacaoOrdenada;
	}

	public List ordenarCrimeDICC(List listaCondenacao, boolean isIniciouCumprimentoPena) throws Exception {
		// ordena a lista de condenação pela data de início de cumprimento da condenação
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacao.get(i);
			// verifica se não é o último registro da lista
			if (i < listaCondenacao.size() - 1) {
				CondenacaoExecucaoDt condenacao1 = (CondenacaoExecucaoDt) listaCondenacao.get(i + 1);
				
				if (!isIniciouCumprimentoPena){
					condenacao.setDataInicioCumprimentoPena(condenacao.getDataTransitoJulgado());
					condenacao1.setDataInicioCumprimentoPena(condenacao1.getDataTransitoJulgado());
				}
				
				if (condenacao.getDataInicioCumprimentoPena().length() == 0 || condenacao1.getDataInicioCumprimentoPena().length() == 0){
					throw new MensagemException("Existe condenação sem DICC (Data de Início de Cumprimento da Condenação). Verifique!");
					
				} else if (Funcoes.StringToDate(condenacao.getDataInicioCumprimentoPena()).after(Funcoes.StringToDate(condenacao1.getDataInicioCumprimentoPena()))) {
					listaCondenacao.remove(condenacao);
					listaCondenacao.add(i + 1, condenacao);
					i--;
				}
			}
		}
		return listaCondenacao;
	}
	/**
	 * Este método contém os eventos padrão cuja data início pode ser a data base para o cálculo de liquidação de penas, e retorna os eventos da lista de eventos que podem ser data base.
	 * 
	 * @param listaEventos
	 *            - lista de eventos do processo de execução penal
	 * @return listaEventosDataBase - lista com os eventos da 'listaEventos' cuja data início pode ser data base
	 * @author wcsilva
	 */
	public List getListaEventosDataBase() {
		// eventos que podem ser data base
		List eventosDataBase = new ArrayList();
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.PROGRESSAO_REGIME));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.REGRESSAO_REGIME));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.PRISAO));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.PRISAO_FLAGRANTE));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.PRISAO_PROVISORIA));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.FALTA_GRAVE));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.REINCLUSAO));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.APRESENTACAO_ESPONTANEA));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.CONVERSAO_PRIVATIVA_LIBERDADE));
		eventosDataBase.add(String.valueOf(EventoExecucaoDt.ALTERAÇÃO_REGIME));

		return eventosDataBase;
	}

	/**
	 * verifica, na lista de eventos informada, quais eventos são data base
	 * 
	 * @param listaEventos
	 *            : lista de eventos do processo
	 * @return eventosDataBase: eventos da listaEventos que são dataBase
	 */
	public List getListaEventosDataBase_Da_ListaEventos(List listaEventos) {
		List eventosDataBase = getListaEventosDataBase();
		List listaRetorno = new ArrayList();

		for (int i = 0; i < listaEventos.size(); i++) {
			for (int j = 0; j < eventosDataBase.size(); j++) {
				if (((ProcessoEventoExecucaoDt) listaEventos.get(i)).getEventoExecucaoDt().getId().equals(eventosDataBase.get(j))) listaRetorno.add(listaEventos.get(i));
			}
		}
		return listaRetorno;
	}

	/**
	 * Relação dos eventos que possuem link para edição da ação penal.
	 * 
	 * @param idEventoExecucao
	 *            : id do Evento
	 * @return
	 */
	public String isEventoManterAcaoPenal(String idEventoExecucao) {
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)) 
				|| isEventoPenaRestritivaDireito(idEventoExecucao)) 
			return "true";
		else
			return "false";
	}

	/**
	 * Relação dos eventos de pena restritiva de direito e sursis que são inseridos, alterados e excluídos pela ação penal
	 * 
	 * @param idEventoExecucao
	 *            : id do evento para verificar se ele consta na lista de eventos do método
	 * @return boolean.
	 */
	public boolean isEventoPenaRestritivaDireito(String idEventoExecucao) {
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS)) 
				|| isModalidade(idEventoExecucao))
			return true;
		else
			return false;
	}

	/**
	 * Verifica se o tipo de pena atual do processo é Pena Restritiva de Direito.
	 * 
	 * @param listaEvento
	 *            : lista com os eventos
	 * @return boolean
	 */
	public boolean isProcessoPenaRestritivaDireito(List listaEvento) {
		if (listaEvento != null && listaEvento.size() > 0) {
			for (int i = listaEvento.size() - 1; i >= 0; i--) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
				if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO)) 
						|| evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PENA_RESTRITIVA_DIREITO))) {
					return true;
				} else if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PRIVATIVA_LIBERDADE)) 
						|| evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_MEDIDA_SEGURANCA))
						|| evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PRIVATIVA_LIBERDADE_CAUTELAR))) {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Relação dos Eventos que necessitam informar a quantidade (em horas ou dias)
	 * 
	 * @param idEventoExecucao
	 *            : id do Evento
	 * @return String: Informa se a quantidade é em dias, horas ou ""(sem quantidade)
	 */
	public String isInformarQuantidade(String idEventoExecucao) {
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA)) // quantidade = tempo total da comutação
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.DIAS_TRABALHADOS_REMICAO)) // quantidade = qtde de dias trabalhados
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.FALTA))// quantidade = qtde de dias de falta
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.FALTA_LFS))// quantidade = qtde de dias de falta
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_TRABALHADOS_REMICAO)) // quantidade = qtde de dias perdidos
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.TEMPO_CUMPRIDO_PRD)) // quantidade = qtde de dias cumpridos durante a pena restritiva de direito
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.DIAS_LEITURA_REMIDO))
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.PERDA_DIAS_LEITURA_REMIDO))
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.HORAS_ESTUDO_REMIDO))
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMIDO))
		) return "Dias";

		else if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.HORAS_ESTUDO_REMICAO)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.PERDA_HORAS_ESTUDO_REMICAO)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.HORAS_CUMPRIDAS_PSC))) 
			return "Horas";
		
		else if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.PAGAMENTO_PEC))) 
			return "R$";

		else if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.PAGAMENTO_CESTA_BASICA))) 
			return "Cestas";

		else
			return "";
	}

	public boolean isInformarObs(String idEventoExecucao) {
		if (isModalidade(idEventoExecucao)) return false;
		else return true;
	}
	
	public boolean isModalidade(String idEventoExecucao){
		if (idEventoExecucao.length() > 0){
			switch (Integer.parseInt(idEventoExecucao)){
				case EventoExecucaoDt.PERDA_BENS_VALORES:
				case EventoExecucaoDt.PRESTACAO_PECUNIARIA:
				case EventoExecucaoDt.CESTA_BASICA:
				case EventoExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE:
				case EventoExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS:
				case EventoExecucaoDt.LIMITACAO_FIM_SEMANA:
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Calcula o tempo que falta cumprir para obter o livramento condicional, aplicando a fração correspondente. Identifica o tempo de condenação correspondente a cada tipo de crime (comum, hediondo e reincidente)
	 * 
	 * @param listaEvento
	 *            - lista com os eventos
	 * @param calculoLivramentoDt
	 *            - objeto com os dados do cálculo de liquidação de pena
	 * @param listaCondenacao
	 *            - lista com as condenações
	 * @author wcsilva
	 * @throws Exception 
	 */
	public void calcularTempoAAcumprirLivramento(List listaEvento, CalculoLiquidacaoProgressaoLivramentoDt calculoLivramentoDt, List listaCondenacao) throws Exception{
		
		// int qtdeComum = 0;
		int qtdeComumReinc = 0;
		int qtdeHed = 0;
		int qtdeHedReinc = 0;

		int tempoComum = 0; // fração 1/3
		int tempoComumReinc = 0;// fração 1/2
		int tempoHed = 0; // fração 2/3
		int tempoHedReinc = 0; // fração 1/1

		List listaId_ProcExec = new ArrayList(); // id_ProcessoExecucao dos TJ ou GRP anteriores ao LC que foi revogado
		List listaComutacaoTJ = null; // lista com os vínculos da Revogação do LC com o TJ

		// verifica se existe revogação do livramento condicional
		for (int i = listaEvento.size() - 1; i >= 0; i--) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			// verifica se houve revogação do livramento e não é para
			// considerar o tempo do livramento (tipo 0 e 2)
			// 0: Não (fato novo) - 1/1, zera o tempo cumprido do livramento condicional
			// 1: Sim (Crime anterior ao LC) - fração, considera o tempo cumprido do livramento condicional
			// 2: Sim (por descumprimento) - 1/1, considera o tempo cumprido do livramento condicional
			if ((processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL)) 
					|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))) 
					&& (processoEventoExecucaoDt.getConsiderarTempoLivramentoCondicional().equals("0") 
					|| processoEventoExecucaoDt.getConsiderarTempoLivramentoCondicional().equals("2")) 
			) {
				listaComutacaoTJ = new TransitoJulgadoEventoNe().listarTransitoJulgadoEventoNaoExtinto(processoEventoExecucaoDt.getId(), processoEventoExecucaoDt.getEventoExecucaoDt().getId());
				break;
			}
		}

		String idProcExe = "";
		// nova forma de verificar os TJ que deve considerar 1/1 na
		// revogação do LC: 05/09/2011
		if (listaComutacaoTJ != null && listaComutacaoTJ.size() > 0) {
			for (TransitoJulgadoEventoDt ctj : (List<TransitoJulgadoEventoDt>) listaComutacaoTJ) {
				for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
					if ((evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
							|| evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) 
							&& evento.getId_ProcessoExecucao().equals(ctj.getId_ProcessoExecucao())) {
						// verifica se o Id_ProcessoExecução é diferente do último que foi adicionado à lista, para não
						// adicionar repetido, no caso de duas condenações na mesma ação penal.
						if (!idProcExe.equals(evento.getId_ProcessoExecucao())) {
							listaId_ProcExec.add(evento.getId_ProcessoExecucao());
							idProcExe = evento.getId_ProcessoExecucao();
						}
						break;
					}
				}
			}
		}

		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);

			// soma o tempo de condenação dos crimes anteriores ao LC que foi revogado
			boolean isRevogacaoLC = false;
			for (int j = 0; j < listaId_ProcExec.size(); j++) {
				if (((CondenacaoExecucaoDt) listaCondenacao.get(i)).getId_ProcessoExecucao().equals(listaId_ProcExec.get(j))) {
					tempoHedReinc += Funcoes.StringToInt(((CondenacaoExecucaoDt) listaCondenacao.get(i)).getTempoPenaRemanescenteEmDias());
					isRevogacaoLC = true;
				}
			}
			// se o crime está após a revogação, ou ela não existe, soma a qtde de cada tipo de crime para validação posterior
			if (!isRevogacaoLC) {
				if (condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) qtdeHedReinc++;
				else if (condenacaoExecucaoDt.isHediondoLivramento() && !condenacaoExecucaoDt.isReincidente()) qtdeHed++;
				else if (!condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) qtdeComumReinc++;
			}
		}

		// valida a qtde de cada crime para verificar a fração a ser aplicada.
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacao.get(i);

			// verifica se o tempo desta condenação já foi somada para não ser somado novamente.
			boolean isRevogacaoLC = false;
			for (int j = 0; j < listaId_ProcExec.size(); j++) {
				if (condenacaoExecucaoDt.getId_ProcessoExecucao().equals(listaId_ProcExec.get(j))) isRevogacaoLC = true;
			}

			if (!isRevogacaoLC) {
				// se o analista de cálculo informar que deseja considerar os crimes hediondos como "reincidente espefíco", o
				// sistema deve considerar a fração de 1/1 para todos os crimes hediondos
				if (calculoLivramentoDt.getReincidenteEspecificoLC().equalsIgnoreCase("true") && condenacaoExecucaoDt.isHediondoLivramento()) {
					tempoHedReinc += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
				// é um crime comum mas aplica a fração de 2/3
				} else if (condenacaoExecucaoDt.isEquiparaHediondoLivramento()) {
					tempoHed += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
				} else {
					// em atendimento à Nair (25/06/2012): para ser
					// reincidente específico, deve ter pelo menos dois
					// crimes hediondo, um deles reincidente (com data do
					// fato posterior à data do TJ de pelo menos outro crime
					// hediondo)
					if ((qtdeHedReinc > 1) || (qtdeHedReinc >= 1 && qtdeHed >= 1)) {
						if (condenacaoExecucaoDt.isHediondoLivramento()) {

							boolean isHedReinc = false;
							if (qtdeHedReinc > 1) {
								tempoHedReinc += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
								isHedReinc = true;
							} else {
								if (condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) {
									for (int w = i; w >= 0; w--) {
										CondenacaoExecucaoDt cond = (CondenacaoExecucaoDt) listaCondenacao.get(w);
										if (cond.isHediondoLivramento()) {
											if (Funcoes.StringToDate(cond.getDataTransitoJulgado()).before(Funcoes.StringToDate(condenacaoExecucaoDt.getDataFato()))) {
												tempoHedReinc += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
												isHedReinc = true;
												break;
											}
										}
									}
								}
							}
							if (!isHedReinc) tempoHed += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
						}
					}
					if (qtdeComumReinc == 0 && qtdeHedReinc == 0) {
						if (!condenacaoExecucaoDt.isHediondoLivramento()) tempoComum += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
					}
					if ((qtdeHedReinc == 0) || (qtdeHedReinc == 1 && qtdeHed == 0) || (qtdeHedReinc == 0 && qtdeHed == 1)) {
						if (condenacaoExecucaoDt.isHediondoLivramento()) tempoHed += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
					}
					if (qtdeComumReinc >= 1 || qtdeHedReinc >= 1) {
						if (!condenacaoExecucaoDt.isHediondoLivramento()) tempoComumReinc += Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias());
					}
				}
			}
		}

		int tempoACumprir = (int) Math.ceil(((double) tempoComum * 1 / 3) + ((double) tempoComumReinc * 1 / 2) + ((double) tempoHed * 2 / 3) + ((double) tempoHedReinc));

		if (tempoComum > 0) calculoLivramentoDt.setTempoComumLivramentoDias(String.valueOf(tempoComum));
		if (tempoComumReinc > 0) calculoLivramentoDt.setTempoComumReincidenteLivramentoDias(String.valueOf(tempoComumReinc));
		if (tempoHed > 0) calculoLivramentoDt.setTempoHediondoLivramentoDias(String.valueOf(tempoHed));
		if (tempoHedReinc > 0) calculoLivramentoDt.setTempoHediondoReincidenteLivramentoDias(String.valueOf(tempoHedReinc));

		calculoLivramentoDt.setTempoACumprirLivramentoDias(String.valueOf(tempoACumprir));

	}

	public void calcularTempoAAcumprirLivramento_novo(List listaEvento, CalculoLiquidacaoProgressaoLivramentoDt calculoLivramentoDt, List listaCondenacao, String tipoRemicao) throws Exception{
		
		List listaId_ProcExec = new ArrayList(); // id_ProcessoExecucao dos TJ ou GRP anteriores ao LC que foi revogado
		List listaComutacaoTJ = null; // lista com os vínculos da Revogação do LC com o TJ

		// verifica se existe revogação do livramento condicional
		for (int i = listaEvento.size() - 1; i >= 0; i--) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			// verifica se houve revogação do livramento e não é para
			// considerar o tempo do livramento (tipo 0 e 2)
			// 0: Não (fato novo) - 1/1, zera o tempo cumprido do livramento condicional
			// 1: Sim (Crime anterior ao LC) - fração, considera o tempo cumprido do livramento condicional
			// 2: Sim (por descumprimento) - 1/1, considera o tempo cumprido do livramento condicional
			if ((processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL)) 
					|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))) 
					&& (processoEventoExecucaoDt.getConsiderarTempoLivramentoCondicional().equals("0") 
					|| processoEventoExecucaoDt.getConsiderarTempoLivramentoCondicional().equals("2")) 
			) {
				listaComutacaoTJ = new TransitoJulgadoEventoNe().listarTransitoJulgadoEventoNaoExtinto(processoEventoExecucaoDt.getId(), processoEventoExecucaoDt.getEventoExecucaoDt().getId());
				break;
			}
		}

		String idProcExe = "";
		// nova forma de verificar os TJ que deve considerar 1/1 na revogação do LC: 05/09/2011
		if (listaComutacaoTJ != null && listaComutacaoTJ.size() > 0) {
			for (TransitoJulgadoEventoDt ctj : (List<TransitoJulgadoEventoDt>) listaComutacaoTJ) {
				for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
					if ((evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
							|| evento.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) 
							&& evento.getId_ProcessoExecucao().equals(ctj.getId_ProcessoExecucao())) {
						// verifica se o Id_ProcessoExecução é diferente do último que foi adicionado à lista, para não
						// adicionar repetido, no caso de duas condenações na mesma ação penal.
						if (!idProcExe.equals(evento.getId_ProcessoExecucao())) {
							listaId_ProcExec.add(evento.getId_ProcessoExecucao());
							idProcExe = evento.getId_ProcessoExecucao();
						}
						break;
					}
				}
			}
		}

		List listaCondenacaoOrdenada = ordenarCrimeMaisGrave(listaCondenacao, listaEvento);
		
		int qtdeComumReinc = 0;
		int qtdeHed = 0;
		int qtdeHedReinc = 0;

		for (int i = 0; i < listaCondenacaoOrdenada.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(i);

			// soma o tempo de condenação dos crimes anteriores ao LC que foi revogado
			boolean isRevogacaoLC = false;
			for (int j = 0; j < listaId_ProcExec.size(); j++) {
				if (((CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(i)).getId_ProcessoExecucao().equals(listaId_ProcExec.get(j))) {
					isRevogacaoLC = true;
				}
			}
			// se o crime está após a revogação, ou ela não existe, soma a qtde de cada tipo de crime para validação posterior
			if (!isRevogacaoLC) {
				if (condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) qtdeHedReinc++;
				else if (condenacaoExecucaoDt.isHediondoLivramento() && !condenacaoExecucaoDt.isReincidente()) qtdeHed++;
				else if (!condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) qtdeComumReinc++;
			}
		}
		
		
		//--------------CALCULA O TEMPO A CUMPRIR ATÉ A DATA BASE-------------------------------------------------------------------
		
		List listaCondenacaoAteDataBase = ordenarCrimeMaisGrave_ateDataBase(listaCondenacao, listaEvento, calculoLivramentoDt.getDataBaseLivramento(), true);
		int tempoCumpridoTotal = Funcoes.StringToInt(calculoLivramentoDt.getTempoCumpridoDataBaseLivramentoDias());
					
		for (int i = 0; i < listaCondenacaoAteDataBase.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacaoAteDataBase.get(i);
			
			HashMap map = calcularRestantePenaCrime(condenacaoExecucaoDt, calculoLivramentoDt.getDataBaseLivramento(), listaEvento, tempoCumpridoTotal, i, tipoRemicao);
			int restantePenaCrime = Funcoes.StringToInt(map.get("restantePenaCrime").toString());
			tempoCumpridoTotal = Funcoes.StringToInt(map.get("tempoCumpridoTotal").toString());
			
			this.somarTempoCrimeLivramento(calculoLivramentoDt, listaId_ProcExec, condenacaoExecucaoDt, restantePenaCrime, calculoLivramentoDt.getReincidenteEspecificoLC(), qtdeHedReinc, qtdeHedReinc, qtdeComumReinc, i, listaCondenacaoAteDataBase);
		}
		//---------------------------------------------------------------------------------------------------------------------------
		
		
		//--------------CALCULA O TEMPO A CUMPRIR APÓS A DATA BASE-------------------------------------------------------------------
		
		// verifica se existe tempo cumprido até a data base que não foi considerado:
		// se não encontrar nenhuma condenação anterior à data base selecionada, o tempo cumprido não será considerado (indica que
		// não houve tempo a cumprir antes da data base)
		boolean isConsiderarTempoCumpridoAteDataBase = false;
		if (calculoLivramentoDt.getTempoCumpridoDataBaseLivramentoDias().length() > 0 
				&& (Funcoes.StringToInt(calculoLivramentoDt.getTempoComumLivramentoDias()) == 0 && Funcoes.StringToInt(calculoLivramentoDt.getTempoComumReincidenteLivramentoDias()) == 0
						&& Funcoes.StringToInt(calculoLivramentoDt.getTempoHediondoLivramentoDias()) == 0 && Funcoes.StringToInt(calculoLivramentoDt.getTempoHediondoReincidenteLivramentoDias()) == 0)) {
			// se não tem tempo a cumprir, é pq não encontrou condenação para descontar o tempo cumprido considera primeiro a condenação dos crimes mais graves
			isConsiderarTempoCumpridoAteDataBase = true;
		}
		
		for (int i = 0; i < listaCondenacaoOrdenada.size(); i++) {
			CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(i);
			// percorre a lista até encontrar o evento TJ ou GRP da condenação referenciada pelo id_ProcessoExecucao
			for (int w = 0; w < listaEvento.size(); w++) {
				if (((ProcessoEventoExecucaoDt) listaEvento.get(w)).getId_ProcessoExecucao().equals(condenacaoExecucaoDt.getId_ProcessoExecucao()) && (((ProcessoEventoExecucaoDt) listaEvento.get(w)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) || ((ProcessoEventoExecucaoDt) listaEvento.get(w)).getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)))) {
					// verifica se o TJ está após a data base
					if (Funcoes.calculaDiferencaEntreDatas(calculoLivramentoDt.getDataBaseLivramento(), ((ProcessoEventoExecucaoDt) listaEvento.get(w)).getDataInicioCumpirmentoPena()) > 0) {
						this.somarTempoCrimeLivramento(calculoLivramentoDt, listaId_ProcExec, condenacaoExecucaoDt, Funcoes.StringToInt(condenacaoExecucaoDt.getTempoPenaRemanescenteEmDias()), calculoLivramentoDt.getReincidenteEspecificoLC(), qtdeHedReinc, qtdeHedReinc, qtdeComumReinc, i, listaCondenacaoOrdenada);
					}
					break;
				}
			}
		}
		
		if (isConsiderarTempoCumpridoAteDataBase){
			if (calculoLivramentoDt.getTempoHediondoLivramentoDias().length() > 0){
				int tempo = Funcoes.StringToInt(calculoLivramentoDt.getTempoHediondoLivramentoDias()) - Funcoes.StringToInt(calculoLivramentoDt.getTempoCumpridoDataBaseLivramentoDias());
				calculoLivramentoDt.setTempoHediondoLivramentoDias(String.valueOf(tempo));
			}
			else if (calculoLivramentoDt.getTempoComumReincidenteLivramentoDias().length() > 0){
				int tempo = Funcoes.StringToInt(calculoLivramentoDt.getTempoComumReincidenteLivramentoDias()) - Funcoes.StringToInt(calculoLivramentoDt.getTempoCumpridoDataBaseLivramentoDias());
				calculoLivramentoDt.setTempoComumReincidenteLivramentoDias(String.valueOf(tempo));
			}
			else if (calculoLivramentoDt.getTempoComumLivramentoDias().length() > 0){
				int tempo = Funcoes.StringToInt(calculoLivramentoDt.getTempoComumLivramentoDias()) - Funcoes.StringToInt(calculoLivramentoDt.getTempoCumpridoDataBaseLivramentoDias());
				calculoLivramentoDt.setTempoComumLivramentoDias(String.valueOf(tempo));
			}
		}
		
		//---------------------------------------------------------------------------------------------------------------------------
		
		int tempoACumprir = (int) Math.ceil((Funcoes.StringToDouble(calculoLivramentoDt.getTempoComumLivramentoDias()) * 1 / 3) + 
								(Funcoes.StringToDouble(calculoLivramentoDt.getTempoComumReincidenteLivramentoDias()) * 1 / 2) + 
								(Funcoes.StringToDouble(calculoLivramentoDt.getTempoHediondoLivramentoDias()) * 2 / 3) + 
								(Funcoes.StringToDouble(calculoLivramentoDt.getTempoHediondoReincidenteLivramentoDias())));
		calculoLivramentoDt.setTempoACumprirLivramentoDias(String.valueOf(tempoACumprir));
					
	}
	
	public void somarTempoCrimeLivramento(CalculoLiquidacaoProgressaoLivramentoDt calculoLivramentoDt, List listaId_ProcExec, CondenacaoExecucaoDt condenacaoExecucaoDt, int tempoASomar, String isReincidenteEspecificoLC, int qtdeHedReinc, int qtdeHed, int qtdeComumReinc, int i, List listaCondenacaoOrdenada) throws Exception{

		int tempoComum = 0; // fração 1/3
		int tempoComumReinc = 0;// fração 1/2
		int tempoHed = 0; // fração 2/3
		int tempoHedReinc = 0; // fração 1/1
				
		// verifica se o tempo desta condenação já foi somada para não ser somado novamente.
		boolean isRevogacaoLC = false;
		for (int j = 0; j < listaId_ProcExec.size(); j++) {
			if (condenacaoExecucaoDt.getId_ProcessoExecucao().equals(listaId_ProcExec.get(j))) {
				isRevogacaoLC = true;
				tempoHedReinc += tempoASomar;
			}
		}

		if (!isRevogacaoLC) {
			// se o analista de cálculo informar que deseja considerar os crimes hediondos como "reincidente espefíco", o
			// sistema deve considerar a fração de 1/1 para todos os crimes hediondos
			if (isReincidenteEspecificoLC.equalsIgnoreCase("true") && condenacaoExecucaoDt.isHediondoLivramento()) {
				tempoHedReinc += tempoASomar;
			// é um crime comum mas aplica a fração de 2/3
			} else if (condenacaoExecucaoDt.isEquiparaHediondoLivramento()) {
				tempoHed += tempoASomar;
			} else {
				// em atendimento à Nair (25/06/2012): para ser reincidente específico, deve ter pelo menos dois
				// crimes hediondo, um deles reincidente (com data do fato posterior à data do TJ de pelo menos outro crime hediondo)
				if ((qtdeHedReinc > 1) || (qtdeHedReinc >= 1 && qtdeHed >= 1)) {
					if (condenacaoExecucaoDt.isHediondoLivramento()) {

						boolean isHedReinc = false;
						if (qtdeHedReinc > 1) {
							tempoHedReinc += tempoASomar;
							isHedReinc = true;
						} else {
							if (condenacaoExecucaoDt.isHediondoLivramento() && condenacaoExecucaoDt.isReincidente()) {
								for (int w = i; w >= 0; w--) {
									CondenacaoExecucaoDt cond = (CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(w);
									if (cond.isHediondoLivramento()) {
										if (Funcoes.StringToDate(cond.getDataTransitoJulgado()).before(Funcoes.StringToDate(condenacaoExecucaoDt.getDataFato()))) {
											tempoHedReinc += tempoASomar;
											isHedReinc = true;
											break;
										}
									}
								}
							}
						}
						if (!isHedReinc) tempoHed += tempoASomar;
					}
				}
				if (qtdeComumReinc == 0 && qtdeHedReinc == 0) {
					if (!condenacaoExecucaoDt.isHediondoLivramento()) tempoComum += tempoASomar;
				}
				if ((qtdeHedReinc == 0) || (qtdeHedReinc == 1 && qtdeHed == 0) || (qtdeHedReinc == 0 && qtdeHed == 1)) {
					if (condenacaoExecucaoDt.isHediondoLivramento()) tempoHed += tempoASomar;
				}
				if (qtdeComumReinc >= 1 || qtdeHedReinc >= 1) {
					if (!condenacaoExecucaoDt.isHediondoLivramento()) tempoComumReinc += tempoASomar;
				}
			}
		}
		
		if (tempoComum > 0){
			if (calculoLivramentoDt.getTempoComumLivramentoDias().length() > 0) 
				tempoComum += Funcoes.StringToInt(calculoLivramentoDt.getTempoComumLivramentoDias());
			calculoLivramentoDt.setTempoComumLivramentoDias(String.valueOf(tempoComum));	
		}
		
		if (tempoComumReinc > 0){
			if (calculoLivramentoDt.getTempoComumReincidenteLivramentoDias().length() > 0)
				tempoComumReinc += Funcoes.StringToInt(calculoLivramentoDt.getTempoComumReincidenteLivramentoDias());
			calculoLivramentoDt.setTempoComumReincidenteLivramentoDias(String.valueOf(tempoComumReinc));
		}
		
		if (tempoHed > 0){
			if (calculoLivramentoDt.getTempoHediondoLivramentoDias().length() > 0)
				tempoHed += Funcoes.StringToInt(calculoLivramentoDt.getTempoHediondoLivramentoDias());
			calculoLivramentoDt.setTempoHediondoLivramentoDias(String.valueOf(tempoHed));	
		}
		
		if (tempoHedReinc > 0){
			if (calculoLivramentoDt.getTempoHediondoReincidenteLivramentoDias().length() > 0)
				tempoHedReinc += Funcoes.StringToInt(calculoLivramentoDt.getTempoHediondoReincidenteLivramentoDias());
			calculoLivramentoDt.setTempoHediondoReincidenteLivramentoDias(String.valueOf(tempoHedReinc));	
		}
		
	}
	
	/**
	 * calcula o requisito temporal para o Livramento Condicional
	 * 
	 * @param listaEvento
	 *            : lista com os eventos (processoEventoExecucaoDt)
	 * @param calculoLiquidacaoDt
	 *            : objeto com os dados do cálculo de liquidação
	 * @param listaCondenacao
	 *            : lista com as condenações (CondenacaoExecucaoDt)
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularLivramentoCondicional(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoNaoExtinta) throws Exception{
		String msgRetorno = "";
		
		if (getIdStatus(listaEvento).equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) 
			msgRetorno += "Não é possível efetuar o cálculo: EXECUÇÃO DA PENA INTERROMPIDA.\n";

		EventoRegimeDt eventoRegimeDt = getUltimoEventoRegimeDt(listaEvento);

		// verifica se existe próximo regime para o regime atual e se não é
		// para forçar o cálculo do LC
		if (calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getForcarCalculoLC().length() == 0) {
			if (eventoRegimeDt.getId_RegimeExecucao().equals(String.valueOf(RegimeExecucaoDt.LIVRAMENTO_CONDICIONAL))) 
				msgRetorno += "O sentenciado já está em Livramento Condicional. \n";

			if (Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalCondenacaoDias()) < 730) {
				msgRetorno += "Não é possível realizar o cálculo do Livramento Condicional! (Motivo: Pena total menor que 2 anos, nos termos do art.83, caput, do Código Penal.)\n";
			}
		}

		if (msgRetorno.length() == 0) {
			boolean isSelecionouDataBase = true;
			if (calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataBaseLivramento().length() == 0){
				calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setDataBaseLivramento(getPrimeiraDataBase(listaEvento));
				isSelecionouDataBase = false;
			}
			
			int tempoTotalCondenacaoDiasInt = 0;
			if (calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias().length() > 0) tempoTotalCondenacaoDiasInt = Integer.parseInt(calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias());
			
			calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setTempoInterrupcaoTotalDias(calcularTempoInterrupcaoTotal(listaEvento));

			//significa que o usuário selecionou a data base para o cálculo
			if (isSelecionouDataBase){
				calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setTempoCumpridoDataBaseLivramentoDias(calcularTempoCumpridoAteDataBase(listaEvento, calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataBaseLivramento(), String.valueOf(tempoTotalCondenacaoDiasInt), calculoLiquidacaoDt.getTipoRemicao()));
				calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setTempoRestanteDataBaseLivramentoDias(String.valueOf(tempoTotalCondenacaoDiasInt - Integer.parseInt(calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getTempoCumpridoDataBaseLivramentoDias())));
				calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setTempoInterrupcaoAposDataBaseLivramentoDias(calcularTempoInterrupcao_AposDataBase(listaEvento, calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataBaseLivramento()));	
			}
			
			calcularTempoAAcumprirLivramento_novo(listaEvento, calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt(), listaCondenacaoNaoExtinta, calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteDias());

			String dataRequisito = calcularDataRequisitoProgressao_Livramento(Funcoes.StringToInt(calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getTempoACumprirLivramentoDias()), listaEvento, calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataBaseLivramento(), calculoLiquidacaoDt.getTipoRemicao(), "", true);

			// calcula o tempo de remição total
			int remicaoTotal = 0;
			if (calculoLiquidacaoDt.getTempoTotalRemicaoEstudoDias().length() > 0) 
				remicaoTotal += Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalRemicaoEstudoDias());

			if (calculoLiquidacaoDt.getTempoTotalRemicaoTrabalhoDias().length() > 0) 
				remicaoTotal += Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalRemicaoTrabalhoDias());
			
			if (calculoLiquidacaoDt.getTempoTotalRemicaoLeituraDias().length() > 0)
				remicaoTotal += Funcoes.StringToInt(calculoLiquidacaoDt.getTempoTotalRemicaoLeituraDias());

			calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setTempoTotalRemicaoDias(String.valueOf(remicaoTotal));

			// tipo 2(remição): considera o tempo de remição, pois ele não é
			// considerado no cálculo da data do requisito temporal (pois
			// este cálculo é o mesmo para progressão e livramento)
			if (calculoLiquidacaoDt.getTipoRemicao().equals("2")) 
				dataRequisito = Funcoes.somaData(dataRequisito, -(remicaoTotal));

			calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setDataRequisitoTemporalLivramento(dataRequisito);
			// utilizado no relatório pdf
			calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setTempoTotalCondenacaoAnos(calculoLiquidacaoDt.getTempoTotalCondenacaoAnos());
		}
				
		return msgRetorno;
	}

	/**
	 * Calcula o requisito temporal para a comutação de pena unificada apenas se não possuir condenação com crime hediondo
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @param listaCondenacaoNaoExtinta
	 * @return String, mensagem identificando se há crime hediondo
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularComutacaoPreviaUnificada(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoNaoExtinta, String idParametroComutacao) throws Exception{
		
			ParametroComutacaoExecucaoDt parametroComutacao = new ParametroComutacaoExecucaoNe().consultarId(idParametroComutacao);

			// verifica se todas as condenações possuem
			// "data início de cumprimento da pena" preenchida
			if (listaCondenacaoNaoExtinta != null) {
				for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacaoNaoExtinta) {
					if (condenacao.getDataInicioCumprimentoPena().length() == 0) {
						return "É necessário informar a Data Início de Cumprimento da Pena de todos os TJ!";
					}
				}
			}

			if (parametroComutacao != null) {

				List listaCondenacaoAteDecreto = getListaCondenacaoAteData(listaCondenacaoNaoExtinta, parametroComutacao.getDataDecreto());

				// verifica se tem condenação até a data do decreto
				if (listaCondenacaoAteDecreto != null && listaCondenacaoAteDecreto.size() > 0) {

					String descricaoComutacao = validarCalculoComutacao(listaCondenacaoAteDecreto, listaEvento, parametroComutacao);

					// -----------INÍCIO CÁLCULO-----------------
					if (descricaoComutacao.length() == 0) {
						double fracaoComum = 0;
						double fracaoHediondo = 0;
						int tempoTotalCondenacaoComum = 0;
						int tempoTotalCondenacaoHediondo = 0;

						List listaTempoTotalCondenacao = new ArrayList();
						boolean isReincidente = false;
						String dataBase = "";

						// calcula o tempo total de condenação até a data do decreto
						for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacaoAteDecreto) {
							
							if (condenacao.isReincidente()) 
								isReincidente = true;
							
							// crime comum
							if (!condenacao.isHediondoLivramento()) {
								if (condenacao.getTempoPenaRemanescenteEmDias().length() > 0) {
									tempoTotalCondenacaoComum += Funcoes.StringToInt(condenacao.getTempoPenaRemanescenteEmDias());
									listaTempoTotalCondenacao.add(condenacao.getTempoPenaRemanescenteEmAnos());
								} else {
									tempoTotalCondenacaoComum += Funcoes.StringToInt(condenacao.getTempoPenaEmDias());
									listaTempoTotalCondenacao.add(condenacao.getTempoPenaRemanescenteEmDias());
								}

								if (condenacao.isReincidente()) 
									isReincidente = true;

								if (dataBase.length() == 0 || Funcoes.calculaDiferencaEntreDatas(condenacao.getDataInicioCumprimentoPena(), dataBase) > 0) 
									dataBase = condenacao.getDataInicioCumprimentoPena();

								// crime hediondo
							} else if (calculoLiquidacaoDt.isSaldoDevedorCrimeHediondo()) {
								if (condenacao.getTempoPenaRemanescenteEmDias().length() > 0) {
									tempoTotalCondenacaoHediondo += Funcoes.StringToInt(condenacao.getTempoPenaRemanescenteEmDias());
									listaTempoTotalCondenacao.add(condenacao.getTempoPenaRemanescenteEmAnos());
								} else {
									tempoTotalCondenacaoHediondo += Funcoes.StringToInt(condenacao.getTempoPenaEmDias());
									listaTempoTotalCondenacao.add(condenacao.getTempoPenaRemanescenteEmDias());
								}

								if (dataBase.length() == 0 || Funcoes.calculaDiferencaEntreDatas(condenacao.getDataInicioCumprimentoPena(), dataBase) > 0) 
									dataBase = condenacao.getDataInicioCumprimentoPena();
							}

						}

						CalculoLiquidacaoComutacaoDt calculoComutacaoDt = new CalculoLiquidacaoComutacaoDt();
						calculoComutacaoDt.setDataDecreto(parametroComutacao.getDataDecreto());
						calculoComutacaoDt.setDescricao(descricaoComutacao);
						
						String fracaoTempoCumpridoComum = "";
						
						boolean isFemininoComMenores = false;
						ProcessoParteDt sentenciado = new ProcessoParteNe().consultarUmPromovido(calculoLiquidacaoDt.getIdProcesso());
						if (sentenciado != null && "F".equalsIgnoreCase(sentenciado.getSexo()) && calculoLiquidacaoDt.isMulherComMenores()){
							isFemininoComMenores = true;
						}
						
								if (isReincidente) {
									if (isFemininoComMenores && !parametroComutacao.getFracaoComumReincFeminino().isEmpty())
										fracaoTempoCumpridoComum = parametroComutacao.getFracaoComumReincFeminino();
									else
										fracaoTempoCumpridoComum = parametroComutacao.getFracaoComumReinc();
								} else {
									if (isFemininoComMenores && !parametroComutacao.getFracaoComumFeminino().isEmpty())
										fracaoTempoCumpridoComum = parametroComutacao.getFracaoComumFeminino();
									else
										fracaoTempoCumpridoComum = parametroComutacao.getFracaoComum();
								}
								fracaoComum = (double) Funcoes.StringToInt(fracaoTempoCumpridoComum.substring(0, 1)) / Funcoes.StringToInt(fracaoTempoCumpridoComum.substring(2));
								calculoComutacaoDt.setFracaoTempoCumprido(fracaoTempoCumpridoComum);
														

							// se for para considerar o tempo cumprido do crime hediondo
							int tempoACumprirHediondo = 0;
							if (calculoLiquidacaoDt.isSaldoDevedorCrimeHediondo()) {

									fracaoHediondo = (double) Funcoes.StringToInt(parametroComutacao.getFracaoHediondo().substring(0, 1)) / Funcoes.StringToInt(parametroComutacao.getFracaoHediondo().substring(2));
								tempoACumprirHediondo = (int) Math.ceil(((double) Funcoes.StringToDouble(String.valueOf(tempoTotalCondenacaoHediondo)) * fracaoHediondo));

								if (tempoACumprirHediondo > 0){
									calculoComutacaoDt.setFracaoTempoCumprido(fracaoTempoCumpridoComum + " + " + parametroComutacao.getFracaoHediondo());	
								}
							}

							// calcula o tempo a cumprir (saldo devedor)
							int tempoACumprirComum = (int) Math.ceil(((double) Funcoes.StringToDouble(String.valueOf(tempoTotalCondenacaoComum)) * fracaoComum)); 
							int tempoACumprir = tempoACumprirComum + tempoACumprirHediondo;

							// calcula o tempo total de interrupção
							int tempoTotalInterrupcao = Funcoes.StringToInt(calcularTempoInterrupcaoAteData(listaEvento, calculoComutacaoDt.getDataDecreto()));

							// calcula data do requisito temporal da comutação
							// String dataRequisito = Funcoes.somaData(dataBase, tempoACumprir + tempoTotalInterrupcao);
//							HashMap map = calcularDataRequisitoComutacao(tempoACumprir, listaEvento, dataBase, true);
							if (((ProcessoEventoExecucaoDt)listaEvento.get(listaEvento.size()-1)).getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INTERRUPCAO))) {
								calculoComutacaoDt.setDataRequisitoTemporalComutacao("PENA INTERROMPIDA");
								calculoComutacaoDt.setHouveComutacao("Não");
							} else {
								calculoComutacaoDt.setDataRequisitoTemporalComutacao(calcularDataRequisitoProgressao_Livramento(tempoACumprir, listaEvento, dataBase, "1", parametroComutacao.getDataDecreto()));
								if (!Funcoes.StringToDate(calculoComutacaoDt.getDataRequisitoTemporalComutacao()).after(Funcoes.StringToDate(calculoComutacaoDt.getDataDecreto()))) 
									calculoComutacaoDt.setHouveComutacao("SIM");
								else
									calculoComutacaoDt.setHouveComutacao("Não");
							}
							
							// alimenta a variável para ser impressa no relatório
							calculoComutacaoDt.setTempoACumprirDias(String.valueOf(tempoACumprir));
							calculoComutacaoDt.setDataBaseComutacao(dataBase);
							calculoComutacaoDt.setTempoInterrupcaoAteDecretoAnos(Funcoes.converterParaAnoMesDia(tempoTotalInterrupcao));
							calculoComutacaoDt.setTempoTotalCondenacaoAnos((Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(somarAnoMesDia(listaTempoTotalCondenacao)))));

							String diccComum = "";
							String diccHediondo = "";
							
							//verifica a menor data de início de cumprimento da condenação (dicc)
							for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>)listaCondenacaoAteDecreto) {
								if (condenacao.isHediondoLivramento()){
									if (diccHediondo.length() == 0 || Funcoes.StringToDate(condenacao.getDataInicioCumprimentoPena()).before(Funcoes.StringToDate(diccHediondo))){
										diccHediondo = condenacao.getDataInicioCumprimentoPena();
									}
								} else {
									if (diccComum.length() == 0 || Funcoes.StringToDate(condenacao.getDataInicioCumprimentoPena()).before(Funcoes.StringToDate(diccComum))){
										diccComum = condenacao.getDataInicioCumprimentoPena();
									}
								}
							}
							
							// calcula quanto tempo da pena (comum e hediondo) foi cumprido até a data do decreto
							int tempoCumpridoComumAteDataDecreto = 0;
							int tempoCumpridoHediondoAteDataDecreto = 0;
							
							int tempoCumpridoAteDataDecreto = calcularTempoCumpridoAteDataInicioEvento(listaEvento, parametroComutacao.getDataDecreto());
							
							//calcula o tempo cumprido do crime comum até a data do decreto
							for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>)listaEvento) {
								
								//verifica se a data início do evento está após o dicc comum e antes da data do decreto
								if (Funcoes.calculaDiferencaEntreDatas(diccComum, evento.getDataInicio()) >= 0
										&& Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), parametroComutacao.getDataDecreto()) >= 0){
									if (evento.getTempoCumpridoDias().length() > 0) {
										//verifica se a data fim do evento está antes da data do decreto
										if (evento.getDataFim().length() == 0 || Funcoes.calculaDiferencaEntreDatas(evento.getDataFim(), parametroComutacao.getDataDecreto()) >= 0){
											tempoCumpridoComumAteDataDecreto += getTempoCumprido(evento);
										} else {
											//pega o tempo cumprido data data início até a data do decreto
											tempoCumpridoComumAteDataDecreto += Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), parametroComutacao.getDataDecreto());
										}	
									} else if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))){
										//pega o tempo cumprido data data início até a data do decreto
										tempoCumpridoComumAteDataDecreto += Funcoes.calculaDiferencaEntreDatas(evento.getDataInicio(), parametroComutacao.getDataDecreto());
									}
									
								}
							}
							
							if (tempoCumpridoComumAteDataDecreto > tempoACumprirComum){
								tempoCumpridoComumAteDataDecreto = tempoACumprirComum;
							}
							
							if (calculoLiquidacaoDt.isSaldoDevedorCrimeHediondo()){
								tempoCumpridoHediondoAteDataDecreto = tempoCumpridoAteDataDecreto - tempoCumpridoComumAteDataDecreto;
							}
							
							calculoComutacaoDt.setTempoCumpridoTotalAteDecretoAnos(Funcoes.converterParaAnoMesDia(tempoCumpridoAteDataDecreto));
							calculoComutacaoDt.setDescricaoFracaoComum(fracaoTempoCumpridoComum + "  de  " + Funcoes.converterParaAnoMesDia(tempoTotalCondenacaoComum) + "  =  " + Funcoes.converterParaAnoMesDia(tempoACumprirComum));
							calculoComutacaoDt.setTempoCumpridoComumAteDecretoAnos(String.valueOf(tempoCumpridoComumAteDataDecreto));
							
							List listaAux = new ArrayList();
							listaAux.add(calculoComutacaoDt.getTempoTotalCondenacaoAnos());
							listaAux.add("(" + calculoComutacaoDt.getTempoCumpridoTotalAteDecretoAnos() + ")");
							calculoComutacaoDt.setRestantePenaTotalAteDecretoAnos(Funcoes.converterParaAnoMesDia(Integer.parseInt(somarAnoMesDia(listaAux))));
							
							//verifica se tem crime hediondo
							if (diccHediondo.length() > 0 && calculoLiquidacaoDt.isSaldoDevedorCrimeHediondo()){
								calculoComutacaoDt.setDescricaoFracaoHediondo(parametroComutacao.getFracaoHediondo() + "  de  " + Funcoes.converterParaAnoMesDia(tempoTotalCondenacaoHediondo) + "  =  " + Funcoes.converterParaAnoMesDia(tempoACumprirHediondo));
								calculoComutacaoDt.setTempoCumpridoHediondoAteDecretoAnos(String.valueOf(tempoCumpridoHediondoAteDataDecreto));	
							}
							
							calculoLiquidacaoDt.addListaComutacaoUnificada(calculoComutacaoDt);
																		

					} // -----------FIM CÁLCULO-------------

					else {
						CalculoLiquidacaoComutacaoDt calculoComutacaoDt = new CalculoLiquidacaoComutacaoDt();
						calculoComutacaoDt.setDataDecreto(parametroComutacao.getDataDecreto());
						calculoComutacaoDt.setDescricao(descricaoComutacao);
						calculoLiquidacaoDt.addListaComutacaoUnificada(calculoComutacaoDt);
					}
				} else
					return "Não existe condenação com \"Data Início de Cumprimento da Condenação\" anterior à data do decreto!";
			} else
				return "Não existe Parâmetro de Comutação cadastrado!";

				
		return "";
	}

	/**
	 * Calcula o requisito temporal para a comutação de pena individual apenas se não possuir condenação com crime hediondo
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @param listaCondenacaoNaoExtinta
	 * @return String, mensagem identificando se há crime hediondo
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularComutacaoPreviaIndividual(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoNaoExtinta, String idParametroComutacao) throws Exception{
		
		ParametroComutacaoExecucaoDt parametroComutacao = new ParametroComutacaoExecucaoNe().consultarId(idParametroComutacao);

		listaCondenacaoNaoExtinta = ordenarCrimeDICC(listaCondenacaoNaoExtinta, true);
//			// verifica se todas as condenações possuem "data início de cumprimento da pena" preenchida
//			if (listaCondenacaoNaoExtinta != null) {
//				for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacaoNaoExtinta) {
//					if (condenacao.getDataInicioCumprimentoPena().length() == 0) {
//						return "É necessário informar a Data Início de Cumprimento da Pena de todos os TJ!";
//					}
//				}
//			}

		// faz o cálculo para cada ano do decreto.
		if (parametroComutacao != null) {

			List listaCondenacaoAteDecreto = getListaCondenacaoAteData(listaCondenacaoNaoExtinta, parametroComutacao.getDataDecreto());

			// verifica se tem condenação até a data do decreto
			if (listaCondenacaoAteDecreto != null && listaCondenacaoAteDecreto.size() > 0) {

				String descricaoComutacao = validarCalculoComutacao(listaCondenacaoAteDecreto, listaEvento, parametroComutacao);

				// -----------INÍCIO CÁLCULO-----------------
				if (descricaoComutacao.length() == 0) {
					
					int tempoCumpridoTotal = 0; // tempo cumprido até a data atual
					String dataRequisitoAnterior = "";

					if (calculoLiquidacaoDt.getTempoCumpridoDataAtualDias().length() > 0) 
						tempoCumpridoTotal = Funcoes.StringToInt(calculoLiquidacaoDt.getTempoCumpridoDataAtualDias());

//						// ordena a lista de condenação pela data de início de cumprimento da condenação
//						for (int i = 0; i < listaCondenacaoAteDecreto.size(); i++) {
//							CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacaoAteDecreto.get(i);
//							// verifica se não é o último registro da lista
//							if (i < listaCondenacaoAteDecreto.size() - 1) {
//								CondenacaoExecucaoDt condenacao1 = (CondenacaoExecucaoDt) listaCondenacaoAteDecreto.get(i + 1);
//								if (Funcoes.StringToDate(condenacao.getDataInicioCumprimentoPena()).after(Funcoes.StringToDate(condenacao1.getDataInicioCumprimentoPena()))) {
//									listaCondenacaoAteDecreto.remove(condenacao);
//									listaCondenacaoAteDecreto.add(i + 1, condenacao);
//									i--;
//								}
//							}
//						}

					listaEvento = this.limparUsouTempoCumprido(listaEvento);
//						for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>)listaEvento) {
//							evento.setUsouTempoCumprido(false);
//						}
					
					for (int i = 0; i < listaCondenacaoAteDecreto.size(); i++) {
						CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacaoAteDecreto.get(i);

						double fracao = 0;
						String fracaoTempoCumprido = "";

						boolean isFemininoComMenores = false;
						ProcessoParteDt sentenciado = new ProcessoParteNe().consultarUmPromovido(calculoLiquidacaoDt.getIdProcesso());
						if (sentenciado != null && "F".equalsIgnoreCase(sentenciado.getSexo()) && calculoLiquidacaoDt.isMulherComMenores()){
							isFemininoComMenores = true;
						}
						
						//crime comum
						if (!condenacao.isHediondoLivramento()){
							if (condenacao.isReincidente()) {
								if (isFemininoComMenores && !parametroComutacao.getFracaoComumReincFeminino().isEmpty())
									fracaoTempoCumprido = parametroComutacao.getFracaoComumReincFeminino();
								else
									fracaoTempoCumprido = parametroComutacao.getFracaoComumReinc();
							} else {
								if (isFemininoComMenores && !parametroComutacao.getFracaoComumFeminino().isEmpty())
									fracaoTempoCumprido = parametroComutacao.getFracaoComumFeminino();
								else
									fracaoTempoCumprido = parametroComutacao.getFracaoComum();
							}
//							//crime hediondo
//							} else if (calculoLiquidacaoDt.isSaldoDevedorCrimeHediondo() && condenacao.isHediondoLivramento()) {
//								fracaoTempoCumprido = parametroComutacao.getFracaoHediondo();
						}

						if (!condenacao.isHediondoLivramento()) {
//							if (fracaoTempoCumprido.length() > 0) {

							CalculoLiquidacaoComutacaoDt calculoComutacaoDt = new CalculoLiquidacaoComutacaoDt();
							calculoComutacaoDt.setDataDecreto(parametroComutacao.getDataDecreto());
							calculoComutacaoDt.setDescricao(descricaoComutacao);

							
//									if (condenacao.isReincidente()) {
//										fracao = (double) Funcoes.StringToInt(parametroComutacao.getFracaoComumReinc().substring(0, 1)) / Funcoes.StringToInt(parametroComutacao.getFracaoComumReinc().substring(2));
//										calculoComutacaoDt.setFracaoTempoCumprido(parametroComutacao.getFracaoComumReinc());
//									} else {
//										fracao = (double) Funcoes.StringToInt(parametroComutacao.getFracaoComum().substring(0, 1)) / Funcoes.StringToInt(parametroComutacao.getFracaoComum().substring(2));
//										calculoComutacaoDt.setFracaoTempoCumprido(parametroComutacao.getFracaoComum());
//									}
							fracao = (double) Funcoes.StringToInt(fracaoTempoCumprido.substring(0, 1)) / Funcoes.StringToInt(fracaoTempoCumprido.substring(2));
							calculoComutacaoDt.setFracaoTempoCumprido(fracaoTempoCumprido);
																

							// calcula o tempo a cumprir (saldo devedor)
							int tempoACumprir = (int) Math.ceil(((double) Funcoes.StringToDouble(condenacao.getTempoPenaRemanescenteEmDias()) * fracao));
							String dataBase = "";
							boolean isPenaInterrompida = false;
							boolean isPrimeiraVez = false;

							if (tempoCumpridoTotal > 0) {
								if (i == 0 || dataRequisitoAnterior.length() == 0){
									dataBase = condenacao.getDataInicioCumprimentoPena();
									isPrimeiraVez = true;
								} else if (Funcoes.StringToDate(dataRequisitoAnterior).after(Funcoes.StringToDate(condenacao.getDataInicioCumprimentoPena()))) {
									dataBase = dataRequisitoAnterior;
								} else
									dataBase = condenacao.getDataInicioCumprimentoPena();

								boolean isConsiderarRestanteRemicao = false;
								if (i == listaCondenacaoAteDecreto.size()-1)
									isConsiderarRestanteRemicao = true;
									
								HashMap map = calcularDataRequisitoComutacaoIndividual(tempoACumprir, listaEvento, dataBase, isConsiderarRestanteRemicao);

								tempoCumpridoTotal -= Funcoes.StringToInt(map.get("tempoCumpridoCondenacao").toString());
								if (map.get("dataRequisito") != null) dataRequisitoAnterior = map.get("dataRequisito").toString();
								if (map.get("penaInterrompida") != null && map.get("penaInterrompida").toString().equals("true")) {
									isPenaInterrompida = true;
									calculoComutacaoDt.setDataRequisitoTemporalComutacao("PENA INTERROMPIDA");
								} else
									calculoComutacaoDt.setDataRequisitoTemporalComutacao(map.get("dataRequisito").toString());
							}

							calculoComutacaoDt.setTempoACumprirDias(String.valueOf(tempoACumprir));
							calculoComutacaoDt.setDataBaseComutacao(dataBase);

							// alimenta a variável para ser impressa no relatório
							calculoComutacaoDt.setTempoTotalCondenacaoAnos(condenacao.getTempoPenaRemanescenteEmAnos());
							calculoComutacaoDt.setDataInicioCumprimentoCondenacao(condenacao.getDataInicioCumprimentoPena());
							// verifica se a data do requisito é menor ou igual à data do decreto, para ter direito à comutação
							if (!isPenaInterrompida && calculoComutacaoDt.getDataRequisitoTemporalComutacao().length() > 0 && !Funcoes.StringToDate(calculoComutacaoDt.getDataRequisitoTemporalComutacao()).after(Funcoes.StringToDate(calculoComutacaoDt.getDataDecreto()))) calculoComutacaoDt.setHouveComutacao("SIM");
							else
								calculoComutacaoDt.setHouveComutacao("Não");

							calculoLiquidacaoDt.addListaComutacao(calculoComutacaoDt);
						}
					}
				} // -----------FIM CÁLCULO-------------

				else {
					CalculoLiquidacaoComutacaoDt calculoComutacaoDt = new CalculoLiquidacaoComutacaoDt();
					calculoComutacaoDt.setDataDecreto(parametroComutacao.getDataDecreto());
					calculoComutacaoDt.setDescricao(descricaoComutacao);
					calculoLiquidacaoDt.addListaComutacao(calculoComutacaoDt);
				}
			} else
				return "Não existe condenação com \"Data Início de Cumprimento da Condenação\" anterior à data do decreto!";
		} else {
			return "Não existe Parâmetro de Comutação cadastrado!";
		}			

		return "";
	}

	private String validarCalculoComutacao(List listaCondenacaoAteDecreto, List listaEvento, ParametroComutacaoExecucaoDt parametroComutacao) throws Exception{
		String descricaoComutacao = "";
		String ID_EVENTO_COMUTACAO = "3";
		
		if(!"true".equals(parametroComutacao.getBeneficioAcumulado()) && listaEvento != null){
			for (Object o: listaEvento){
				ProcessoEventoExecucaoDt processoEvento = (ProcessoEventoExecucaoDt) o;
				if (ID_EVENTO_COMUTACAO.equals(processoEvento.getId_EventoExecucao()))
					return "O sentenciado não tem direito à Comutação! (Motivo: Disposto contido no decreto selecionado exclui sentenciados já beneficiados por decretos anteriores)";
			}
		}
		
		// verifica se existe crime comum até a data do decreto.
		if (!isContemCrimeComum(listaCondenacaoAteDecreto)) {
			descricaoComutacao += "O sentenciado não tem direito à Comutação! (Motivo: Possui apenas crimes hediondos até a data do decreto)";

		} else {
			// se existe crime hediondo até a data do decreto, verifica se
			// tem direito à comutação e cumpriu 2/3 ou 1/1 da condenação, conforme decreto
			if (isContemCrimeHediondo(listaCondenacaoAteDecreto)) {

				// se o campo "fração hediondo" não for preenchido, e houver
				// crime hediondo, o sentenciado não terá direito à comutação
				if (parametroComutacao.getFracaoHediondo().length() == 0) {
					descricaoComutacao += "O sentenciado não tem direito à Comutação! (Motivo: Possui crime hediondo até a data do decreto)";
				} else {
					int tempoTotalHediondo = 0;
					int tempoCumpridoAteData = calcularTempoCumpridoAteDataInicioEvento(listaEvento, parametroComutacao.getDataDecreto());

					// calcula o tempo total dos crimes hediondos (para livramento condicional)
					for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacaoAteDecreto) {
						if (condenacao.isHediondoLivramento()) tempoTotalHediondo += Funcoes.StringToInt(condenacao.getTempoPenaRemanescenteEmDias());
					}

					// verifica se cumpriu 1/1 ou 2/3 (fração exigida no decreto) da condenação até a data do decreto
					int tempoACumprirParaComutacao = (tempoTotalHediondo * Funcoes.StringToInt(parametroComutacao.getFracaoHediondo().substring(0, 1)) / Funcoes.StringToInt(parametroComutacao.getFracaoHediondo().substring(2)));
					if (tempoCumpridoAteData < tempoACumprirParaComutacao) {
						descricaoComutacao += "O sentenciado não tem direito à Comutação! (Motivo: Não cumpriu " + parametroComutacao.getFracaoHediondo() + " do crime hediondo até a data do decreto).<br/>" + " Tempo a Cumprir: " + Funcoes.converterParaAnoMesDia(tempoACumprirParaComutacao) + " (a-m-d).<br/>" + " Tempo Cumprido: " + Funcoes.converterParaAnoMesDia(tempoCumpridoAteData) + " (a-m-d).<br/>"  + " Tempo Total Hediondo: " + Funcoes.converterParaAnoMesDia(tempoTotalHediondo) + " (a-m-d).";
					}
				}
			}
		}
				
		return descricaoComutacao;
	}

	/**
	 * Calcula a data do requisito temporal (fórmula: tempo a cumprir - dias cumpridos após a data base, até o cumprimento total do saldo devedor).
	 * 
	 * @param tempoACumprir
	 *            : tempo a cumprir para ter direito à progressão de regime (saldo devedor).
	 * @param listaEvento
	 * @param dataBase
	 * @return data do requisito temporal
	 * @throws Exception
	 */
	private HashMap calcularDataRequisitoComutacaoIndividual(int tempoACumprir, List listaEvento, String dataBase, boolean isConsiderarRestanteRemicao) throws Exception{
		HashMap map = null;
		String dataEvento = ""; // data do evento que será somado o restante do tempo a cumprir (após subtraído os tempos cumpridos)
		String dataUltimoEventoSubtraido = ""; // data início do último evento da lista que foi subtraído o tempo a cumprir
		String id_ProcessoEventoExecucao_UltimoEventoSubtraido = ""; // data início do último evento da lista que foi subtraído o tempo a cumprir
		int tempoCumpridoCondenacao = 0;
		boolean isPenaInterrompida = false;

		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			// verifica se a data início do evento está após a data base
			// (desconta o tempo cumprido do tempo a cumprir)
			if (!evento.isUsouTempoCumprido() && Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataInicio()) >= 0) {

				// subtrai o tempo cumprido do tempo a cumprir, até que o tempo a cumprir seja menor que o tempo cumprido
				// (neste momento, guarda a data e o id do evento)
				if ((evento.getTempoCumpridoDias().length() > 0)) {

					int tempoCumprido = getTempoCumprido(evento);

					if (tempoACumprir > tempoCumprido) {
						tempoACumprir -= tempoCumprido;
						tempoCumpridoCondenacao += tempoCumprido;
						dataUltimoEventoSubtraido = evento.getDataInicio();
						id_ProcessoEventoExecucao_UltimoEventoSubtraido = evento.getId();
						evento.setUsouTempoCumprido(true);
					} else {
						// se o evento for de remição, pega a data início do próximo evento genérico.
						if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))) {
							for (int j = i; j < listaEvento.size(); j++) {
								if (evento.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))) {
									dataEvento = evento.getDataInicio();
									break;
								}
							}
						} else {
							dataEvento = evento.getDataInicio();
							break;
						}
					}
				}

				// se for o último evento da lista, é porque sobrou tempo a cumprir (após ter subtraído o tempo cumprido)
				if (i == listaEvento.size() - 1) {

					// verifica a data do último evento genérico (para somar o restante do tempo a cumprir a esta data)
					for (int w = listaEvento.size() - 1; w >= 0; w--) {
						ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEvento.get(w);

						// verifica se a data início do evento está após a data base (desconta o tempo cumprido do tempo a cumprir)
//						if (Funcoes.calculaDiferencaEntreDatas(dataBase, processoEventoExecucaoDt.getDataInicio()) >= 0) {

							// pega o último evento genérico
							if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.GENERICO))) {
								dataEvento = processoEventoExecucaoDt.getDataInicio();

								if (dataUltimoEventoSubtraido.length() == 0 || Funcoes.StringToDate(dataEvento).after(Funcoes.StringToDate(dataUltimoEventoSubtraido))) {
									dataUltimoEventoSubtraido = processoEventoExecucaoDt.getDataInicio();
									id_ProcessoEventoExecucao_UltimoEventoSubtraido = processoEventoExecucaoDt.getId();
								}
								break;

								// último evento de interrupção
							} else if (processoEventoExecucaoDt.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.INTERRUPCAO))) {
								isPenaInterrompida = true;
								break;
							}
//						}
					}

				}

			// verifica se a data fim é posterior à data base
			//se tem data fim, não será evento de remição
			} else if (evento.getDataFim().length() > 0 && Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataFim()) >= 0) {

				// subtrai o tempo cumprido do tempo a cumprir, até que o tempo a cumprir seja menor que o tempo cumprido
				// (neste momento, guarda a data e o id do evento)
				if ((evento.getTempoCumpridoDias().length() > 0)) {

					int tempoCumprido = Funcoes.calculaDiferencaEntreDatas(dataBase, evento.getDataFim());

					if (tempoACumprir > tempoCumprido) {
						tempoACumprir -= tempoCumprido;
						tempoCumpridoCondenacao += tempoCumprido;

						dataUltimoEventoSubtraido = evento.getDataInicio();
						id_ProcessoEventoExecucao_UltimoEventoSubtraido = evento.getId();
						evento.setUsouTempoCumprido(true);
					} else {
						dataEvento = dataBase;
						break;
					}
				}
			}
		}

		if (isConsiderarRestanteRemicao){
			// percorre a lista a partir do útlimo evento que foi subtraído o tempo cumprido do tempo a cumprir (identificado pelo id_ProcessoEventoExecucao)
			// para considerar o tempo de remição e falta após este evento.
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);

				if (!evento.isUsouTempoCumprido() && evento.getDataInicio().equals(dataEvento)) {
					// verifica se tem evento de falta, se existir, soma o tempo de interrupção ao tempo a cumprir restante.
					for (int j = i + 1; j < listaEvento.size(); j++) {
						ProcessoEventoExecucaoDt e = (ProcessoEventoExecucaoDt) listaEvento.get(j);
						
						if (e.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.FALTA))) {
							tempoACumprir += getTempoInterrupcao(e);
							tempoCumpridoCondenacao -= getTempoInterrupcao(e); 
							e.setUsouTempoCumprido(true);
						} else if (e.getEventoExecucaoDt().getId_EventoExecucaoTipo().equals(String.valueOf(EventoExecucaoTipoDt.REMICAO))) {
							// verifica se o tempoCumprido é negativo
							if (e.getTempoCumpridoAnos().substring(0, 1).equals("(")) {
								tempoACumprir += Funcoes.StringToInt(e.getTempoCumpridoDias());
								tempoCumpridoCondenacao -= Funcoes.StringToInt(e.getTempoCumpridoDias());
								e.setUsouTempoCumprido(true);
							} else {
								tempoACumprir -= Funcoes.StringToInt(e.getTempoCumpridoDias());
								tempoCumpridoCondenacao += Funcoes.StringToInt(e.getTempoCumpridoDias());
								e.setUsouTempoCumprido(true);
							}
						}
					}
					break;
				} 
			}
		}
		
		
		//verifico se o tempo a cumprir ficou maior que o tempo cumprido do último evento verificado
		for (int i=0; i<listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);

			if (evento.getDataInicio().equals(dataEvento) && evento.getDataFim().length() > 0) {
				if (tempoACumprir > getTempoCumprido(evento)){
					map = this.calcularDataRequisitoComutacaoIndividual(tempoACumprir, listaEvento, evento.getDataInicio(), isConsiderarRestanteRemicao);
				}
				break;
			} 
		}
		
		try{
			if (map == null){
				map = new HashMap();
				if (isPenaInterrompida) map.put("penaInterrompida", "true");
				else if (dataEvento.length() > 0) map.put("dataRequisito", Funcoes.somaData(dataEvento, tempoACumprir));
				else
					map.put("dataRequisito", Funcoes.somaData(dataBase, tempoACumprir));
				map.put("tempoCumpridoCondenacao", tempoCumpridoCondenacao);				
			}

			return map;

		} catch(MensagemException m) {
			throw new MensagemException(m.getMessage());
		
		} 
	}
	
	public List limparUsouTempoCumprido(List listaEvento){
		List lista = listaEvento;
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>)lista) {
			evento.setUsouTempoCumprido(false);
		}
		return lista;
	}
	/**
	 * calcula o requisito temporal para o indulto Não realiza o cálculo automaticamento, considerando os decretos presidenciais.
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @param listaCondenacaoNaoExtinta
	 * @throws Exception
	 * @author wcsilva
	 */
	public String calcularIndulto(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoNaoExtinta) throws Exception{
		String msg = "";
		
//			if (calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndulto().length() != 0) {
		if (calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoComum().length() != 0
				|| calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoHediondo().length() != 0) {
			calcularDataRequisitoIndulto(calculoLiquidacaoDt, listaCondenacaoNaoExtinta, listaEvento);
		}

		return msg;
	}

	/**
	 * calcula a data do requisito temporal do indulto para cada fração informada
	 * 
	 * @param fracao
	 *            - fração informada
	 * @param calculoLiquidacaoDt
	 *            - objeto com os dados do cálculo de liquidação
	 * @return dataRequisito - data do requisito temporal para a fração informada
	 * @throws Exception
	 * @author wcsilva
	 */
	private String calcularDataRequisitoIndulto(CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacao, List listaEvento) throws Exception{
		String dataRequisito = "";		
			
		String fracaoComum = calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoComum(); 
		String fracaoHediondo = calculoLiquidacaoDt.getCalculoIndultoDt().getFracaoIndultoHediondo();
		
		// faz a divisão da fração
		double valorFracaoComum = 0;
		double valorFracaoHediondo = 0;
		
		if (fracaoComum.length() > 0){
			String[] vetorFracaoComum = fracaoComum.split("/");
			valorFracaoComum = (double) Funcoes.StringToInt(vetorFracaoComum[0]) / Funcoes.StringToInt(vetorFracaoComum[1]);
		}
		if (fracaoHediondo.length() > 0){
			String[] vetorFracaoHediondo = fracaoHediondo.split("/");
			valorFracaoHediondo = (double) Funcoes.StringToInt(vetorFracaoHediondo[0]) / Funcoes.StringToInt(vetorFracaoHediondo[1]);
		}
		
		int tempoACumprirComum = 0;
		int tempoACumprirHediondo = 0;
		int tempoTotalCondenacaoComumDias = 0;
		int tempoTotalCondenacaoHediondoDias = 0;
		
		for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>)listaCondenacao) {
			if (condenacao.isHediondoLivramento()){
				tempoTotalCondenacaoHediondoDias += Integer.parseInt(condenacao.getTempoPenaRemanescenteEmDias());
			} else 
				tempoTotalCondenacaoComumDias += Integer.parseInt(condenacao.getTempoPenaRemanescenteEmDias());
		}
		
		if (valorFracaoComum > 0){
			tempoACumprirComum += (int) Math.ceil(((double) tempoTotalCondenacaoComumDias * valorFracaoComum));
		} else if (valorFracaoHediondo > 0){
			tempoACumprirHediondo += (int) Math.ceil(((double) tempoTotalCondenacaoComumDias * valorFracaoHediondo));
			tempoTotalCondenacaoHediondoDias += tempoTotalCondenacaoComumDias;
		}
		if (valorFracaoHediondo > 0){
			tempoACumprirHediondo += (int) Math.ceil(((double) tempoTotalCondenacaoHediondoDias * valorFracaoHediondo));	
		} else if (valorFracaoComum > 0){
			tempoACumprirComum += (int) Math.ceil(((double) tempoTotalCondenacaoHediondoDias * valorFracaoComum));
			tempoTotalCondenacaoComumDias += tempoTotalCondenacaoHediondoDias;
		}
		
		int tempoACumprir = tempoACumprirComum + tempoACumprirHediondo;

		// alterada forma de cálculo para a data do requisito não cair no intervalo do evento de fuga.
//			HashMap map = calcularDataRequisitoComutacao(tempoACumprir, listaEvento, ((ProcessoEventoExecucaoDt) listaEvento.get(0)).getDataInicio(), true);
//			if (map.get("dataRequisito") != null) dataRequisito = map.get("dataRequisito").toString();
		
		dataRequisito = calcularDataRequisitoProgressao_Livramento(tempoACumprir, listaEvento, ((ProcessoEventoExecucaoDt) listaEvento.get(0)).getDataInicio(), "1", "");

		calculoLiquidacaoDt.getCalculoIndultoDt().setDataRequisitoIndulto(dataRequisito);
		calculoLiquidacaoDt.getCalculoIndultoDt().setTempoCondenacaoComumDias(String.valueOf(tempoTotalCondenacaoComumDias));
		calculoLiquidacaoDt.getCalculoIndultoDt().setTempoCondenacaoHediondoDias(String.valueOf(tempoTotalCondenacaoHediondoDias));
		calculoLiquidacaoDt.getCalculoIndultoDt().setTempoACumprirComumDias(String.valueOf(tempoACumprirComum));
		calculoLiquidacaoDt.getCalculoIndultoDt().setTempoACumprirHediondoDias(String.valueOf(tempoACumprirHediondo));
		calculoLiquidacaoDt.getCalculoIndultoDt().setDescRelIndulto(getDescricaoResultadoIndulto(calculoLiquidacaoDt));

		
		return dataRequisito;
	}

	/**
	 * Calcula a prescrição de pena e validade do mandado de prisão
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @param listaCondenacaoNaoExtinta
	 * @throws Exception
	 */
	public String calcularPrescricaoPunitiva(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoNaoExtinta, String dataNascimento) throws Exception{
		String msg = "";
		
		HashMap mapArtigo = new HashMap();
		// incisos do artigo 119
		mapArtigo.put("I", false);
		mapArtigo.put("II", false);
		mapArtigo.put("III", false);
		mapArtigo.put("IV", false);
		mapArtigo.put("V", false);
		mapArtigo.put("VI", false);
		mapArtigo.put("110", false); // artigo 110
		mapArtigo.put("113", false); // artigo 113
		mapArtigo.put("115", false); // artigo 115

		// verifica se houve prescrição da pena antes do trânsito em julgado de cada crime.
		// verifica o lapso temporal entre a data do fato, recebimento da
		// denúncia, sentença e trânsito em julgado
		msg = getCalculPrescricaoPunitiva(listaCondenacaoNaoExtinta, calculoLiquidacaoDt, dataNascimento, mapArtigo);

		if (msg.length() == 0) {
			if (calculoLiquidacaoDt.getListaPrescricaoPunitiva() == null || calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() == 0) msg = "Não há Prescrição de Pena.";
		}

		String artigo = "De acordo com: Art.109";
		if (Funcoes.StringToBoolean(mapArtigo.get("I").toString())) artigo += " inc.I";
		if (Funcoes.StringToBoolean(mapArtigo.get("II").toString())) artigo += " inc.II";
		if (Funcoes.StringToBoolean(mapArtigo.get("III").toString())) artigo += " inc.III";
		if (Funcoes.StringToBoolean(mapArtigo.get("IV").toString())) artigo += " inc.IV";
		if (Funcoes.StringToBoolean(mapArtigo.get("V").toString())) artigo += " inc.V";
		if (Funcoes.StringToBoolean(mapArtigo.get("VI").toString())) artigo += " inc.VI";
		if (Funcoes.StringToBoolean(mapArtigo.get("110").toString())) artigo += ", Art.110";
		if (Funcoes.StringToBoolean(mapArtigo.get("113").toString())) artigo += ", Art.113";
		if (Funcoes.StringToBoolean(mapArtigo.get("115").toString())) artigo += ", Art.115";
		artigo += ", Art.119.";

		calculoLiquidacaoDt.setArtigoPrescricaoPunitiva(artigo);
		
		return msg;
	}

	/**
	 * Calcula a prescrição de pena e validade do mandado de prisão
	 * 
	 * @param listaEvento
	 * @param calculoLiquidacaoDt
	 * @param listaCondenacaoNaoExtinta
	 * @throws Exception
	 */
	public String calcularPrescricaoExecutoria(List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoNaoExtinta, String dataNascimento, boolean isPrescricaoUnificada) throws Exception {
		String msg = "";

		HashMap mapArtigo = new HashMap();

		mapArtigo.put("109", false); // incisos do artigo 109: prescrição unificada
		mapArtigo.put("I", false); // incisos do artigo 109: prescrição individual
		mapArtigo.put("II", false);
		mapArtigo.put("III", false);
		mapArtigo.put("IV", false);
		mapArtigo.put("V", false);
		mapArtigo.put("VI", false);
		mapArtigo.put("110", false); // artigo 110 - reincidente
		mapArtigo.put("113", false); // artigo 113 - restante da pena
		mapArtigo.put("115", false); // artigo 115 - maioridade/menoridade

		// verifica se houve prescrição da pena (após o trânsito em julgado)
		msg = getCalculoPrescricaoExecutoria(listaCondenacaoNaoExtinta, listaEvento, calculoLiquidacaoDt, dataNascimento, mapArtigo, isPrescricaoUnificada);

		if (calculoLiquidacaoDt.getListaPrescricaoExecutoria() != null && calculoLiquidacaoDt.getListaPrescricaoExecutoria().size() >= 0){
			if (isPrescricaoUnificada) {
				String artigo = "De acordo com: Art. 109 inc. " + mapArtigo.get("109");
				if (Funcoes.StringToBoolean(mapArtigo.get("110").toString())) artigo += ", Art.110";
				if (Funcoes.StringToBoolean(mapArtigo.get("113").toString())) artigo += ", Art.113";
				if (Funcoes.StringToBoolean(mapArtigo.get("115").toString())) artigo += ", Art.115";

				calculoLiquidacaoDt.setArtigoPrescricaoExecutoriaUnificada(artigo);
			} else {
				String artigo = "De acordo com: Art.109";
				if (Funcoes.StringToBoolean(mapArtigo.get("I").toString())) artigo += " inc.I";
				if (Funcoes.StringToBoolean(mapArtigo.get("II").toString())) artigo += " inc.II";
				if (Funcoes.StringToBoolean(mapArtigo.get("III").toString())) artigo += " inc.III";
				if (Funcoes.StringToBoolean(mapArtigo.get("IV").toString())) artigo += " inc.IV";
				if (Funcoes.StringToBoolean(mapArtigo.get("V").toString())) artigo += " inc.V";
				if (Funcoes.StringToBoolean(mapArtigo.get("VI").toString())) artigo += " inc.VI";
				if (Funcoes.StringToBoolean(mapArtigo.get("110").toString())) artigo += ", Art.110";
				if (Funcoes.StringToBoolean(mapArtigo.get("113").toString())) artigo += ", Art.113";
				if (Funcoes.StringToBoolean(mapArtigo.get("115").toString())) artigo += ", Art.115";
				artigo += ", Art.119."; // prescrição individual

				List listaDetracao = consultarEventoNaLista(listaEvento, String.valueOf(EventoExecucaoDt.PRISAO_PROVISORIA));
				if (!calculoLiquidacaoDt.isIniciouCumprimentoPena() && !calculoLiquidacaoDt.isConsiderarDetracao() && listaDetracao != null && listaDetracao.size() > 0){
					artigo += "    Não foi considerado o tempo de detração no cálculo, conforme RECURSO ESPECIAL Nº 1.095.225 - SP (2008/0209631-1) do STJ.";
				}
				calculoLiquidacaoDt.setArtigoPrescricaoExecutoria(artigo);
			}
		}


		return msg;
	}

	/**
	 * verifica se houve prescrição de pena antes do trânsito em julgado de cada crime, através do lapso temporal entre a data do fato, recebimento da denúncia, sentença e trânsito em julgado.
	 * 
	 * @param listaCondenacao
	 * @param calculoLiquidacaoDt
	 * @param dataNascimento
	 * @throws Exception
	 * @author wcsilva
	 */
	private String getCalculPrescricaoPunitiva(List listaCondenacao, CalculoLiquidacaoDt calculoLiquidacaoDt, String dataNascimento, HashMap mapArtigo) throws Exception{
		String msg = "";
		
		// faz o cálculo da prescrição punitiva para cada condenação
		for (int i = 0; i < listaCondenacao.size(); i++) {
			CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacao.get(i);
			ProcessoExecucaoDt processoExecucao = new ProcessoExecucaoNe().consultarId(condenacao.getId_ProcessoExecucao());

			// verifica o preenchimento dos dados
			String temp = "";
			if (condenacao.getDataFato().length() == 0) temp += "Informe a data do fato. ";
			if (processoExecucao.getDataSentenca().length() == 0) temp += "Informe a data da sentença. ";
			if (processoExecucao.getDataDenuncia().length() == 0) temp += "Informe a data da denúncia. ";
			if (processoExecucao.getDataTransitoJulgado().length() == 0) temp += "Informe a data do Trânsito em Julgado. ";
			if (temp.length() > 0) {
				msg += "\nAção penal nº: " + processoExecucao.getNumeroAcaoPenal() + ": " + temp;

			} else {
				// verifica o tempo para prescrição da pena
				int tempoPrescricao = calcularTempoPrescricao(condenacao.getTempoPenaEmDias(), false, condenacao.getDataFato(), condenacao.isReincidente(), mapArtigo, isMenoridadeMaioridadePrescricao(dataNascimento, condenacao.getDataFato(), processoExecucao.getDataSentenca()), false);

				String descricaoLapso = "";
				if (condenacao.getDataFato().length() > 0 && processoExecucao.getDataDenuncia().length() > 0 && Funcoes.StringToDate(condenacao.getDataFato()).before(Funcoes.StringToDate("06/05/2010"))) {
					if (isPenaPrescrita_calcularDataPrescricao(calculoLiquidacaoDt, condenacao.getTempoPenaEmDias(), condenacao.getTempoPenaRemanescenteEmDias(), tempoPrescricao, "", condenacao.getDataFato(), processoExecucao.getDataDenuncia(), true)) {
					}
					CalculoLiquidacaoPrescricaoDt presc = (CalculoLiquidacaoPrescricaoDt) calculoLiquidacaoDt.getListaPrescricaoPunitiva().get(calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() - 1);
					descricaoLapso = "Fato: " + presc.getDataInicioLapso() + " ao Rec. Den.: " + presc.getDataFimLapso() + " = " + presc.getTempoLapsoAnos() + " (a-m-d)";
					((CalculoLiquidacaoPrescricaoDt) calculoLiquidacaoDt.getListaPrescricaoPunitiva().get(calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() - 1)).setDescricaoLapso(descricaoLapso);
				}
				if (processoExecucao.getDataDenuncia().length() > 0 && processoExecucao.getDataSentenca().length() > 0) {
					if (isPenaPrescrita_calcularDataPrescricao(calculoLiquidacaoDt, condenacao.getTempoPenaEmDias(), condenacao.getTempoPenaRemanescenteEmDias(), tempoPrescricao, "", processoExecucao.getDataDenuncia(), processoExecucao.getDataSentenca(), true)) {
					}
					CalculoLiquidacaoPrescricaoDt presc = (CalculoLiquidacaoPrescricaoDt) calculoLiquidacaoDt.getListaPrescricaoPunitiva().get(calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() - 1);
					descricaoLapso = "Rec. Den.: " + presc.getDataInicioLapso() + " à Sent: " + presc.getDataFimLapso() + " = " + presc.getTempoLapsoAnos() + " (a-m-d)";
					((CalculoLiquidacaoPrescricaoDt) calculoLiquidacaoDt.getListaPrescricaoPunitiva().get(calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() - 1)).setDescricaoLapso(descricaoLapso);
				}
				if (processoExecucao.getDataSentenca().length() > 0 && processoExecucao.getDataTransitoJulgado().length() > 0) {
					if (isPenaPrescrita_calcularDataPrescricao(calculoLiquidacaoDt, condenacao.getTempoPenaEmDias(), condenacao.getTempoPenaRemanescenteEmDias(), tempoPrescricao, "", processoExecucao.getDataSentenca(), processoExecucao.getDataTransitoJulgado(), true)) {
					}
					CalculoLiquidacaoPrescricaoDt presc = (CalculoLiquidacaoPrescricaoDt) calculoLiquidacaoDt.getListaPrescricaoPunitiva().get(calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() - 1);
					descricaoLapso = "Sent: " + presc.getDataInicioLapso() + " ao TJ: " + presc.getDataFimLapso() + " = " + presc.getTempoLapsoAnos() + " (a-m-d)";
					((CalculoLiquidacaoPrescricaoDt) calculoLiquidacaoDt.getListaPrescricaoPunitiva().get(calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() - 1)).setDescricaoLapso(descricaoLapso);
				}
			}
		}

		if (msg.length() > 0) {
			String temp = msg;
			msg = "Não foi possível efetuar o cálculo da PRESCRIÇÃO PUNITIVA. Motivo: " + temp;
		}
				
		return msg;
	}

	// faz o cálculo da prescrição executória individual e unificada. A
	// prescrição unificada é o cálculo da validade do mandado de prisão.
	private String getCalculoPrescricaoExecutoria(List listaCondenacao, List listaEvento, CalculoLiquidacaoDt calculoLiquidacaoDt, String dataNascimento, HashMap mapArtigo, boolean isPrescricaoExecUnificada) throws Exception{
		String msg = "";
		// ***********************************INICIOU CUMPRIMENTO DA PENA***************************************
		if (calculoLiquidacaoDt.isIniciouCumprimentoPena()) {

			List listaFuga = new ArrayList();
			List listaPrisao = new ArrayList();

			int posicaoFuga = 1;
			int posicaoPrisao = 0;

			// lista os eventos de fuga e prisão existentes
			listaFuga.add(listaEvento.get(0));
			if (listaEvento != null) {
				for (int i = 0; i < listaEvento.size(); i++) {
					if (((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.FUGA)) 
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.FUGA_ALVARA)) 
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.LIBERACAO_ALVARA_PRD)) 
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INTERRUPCAO_CUMPRIMENTO_PENA))) {
						listaFuga.add(listaEvento.get(i));
						posicaoFuga++;
					} else if ((((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRISAO)) 
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.APRESENTACAO_ESPONTANEA)) 
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME)) 
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.REINCLUSAO))  
							|| ((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRISAO_FLAGRANTE))) 
							&& (posicaoFuga > posicaoPrisao)) {
						listaPrisao.add(listaEvento.get(i));
						posicaoPrisao++;
					}
					if (((ProcessoEventoExecucaoDt) listaEvento.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME))){
						((ProcessoEventoExecucaoDt)listaFuga.get(0)).setDataFim(Funcoes.somaData(((ProcessoEventoExecucaoDt) listaEvento.get(i)).getDataInicio(),-1));
					}
				}
			}


			Map<String, List> listaCondenacaoComInicioCumprimento = new HashMap<String, List>();

			List listaCondenacaoSemInicioCumprimento = new ArrayList();
			// verifica se tem alguma condenação com data do TJ posterior à última fuga.
			for(int i=0;i<listaFuga.size();i++){
		    	ProcessoEventoExecucaoDt eventoFuga = (ProcessoEventoExecucaoDt) listaFuga.get(i);	
		    	listaCondenacaoComInicioCumprimento.put(eventoFuga.getId(), new ArrayList());
		    	if(i == (listaFuga.size() - 1)){
		    		for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacao) {				    
						if (Funcoes.StringToDate(eventoFuga.getDataInicio()).before(Funcoes.StringToDate(condenacao.getDataTransitoJulgado()))) {
							listaCondenacaoSemInicioCumprimento.add(condenacao);
						} else {
							if (Funcoes.StringToDate(eventoFuga.getDataInicio()).before(Funcoes.StringToDate(condenacao.getDataTransitoJulgado()))
								&& Funcoes.StringToDate(eventoFuga.getDataFim()).after(Funcoes.StringToDate(condenacao.getDataTransitoJulgado()))) {
								condenacao.setEmFuga(eventoFuga, true);
							} else {
								condenacao.setEmFuga(eventoFuga, false);
							}
						    listaCondenacaoComInicioCumprimento.get(eventoFuga.getId()).add(condenacao);
						}
				    }
		    	} else {
					for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacao) {				    
						if (Funcoes.StringToDate(eventoFuga.getDataInicio()).before(Funcoes.StringToDate(condenacao.getDataTransitoJulgado()))
							&& (Funcoes.StringToDate(eventoFuga.getDataFim()).after(Funcoes.StringToDate(condenacao.getDataTransitoJulgado()))
									|| Funcoes.StringToDate(eventoFuga.getDataFim()).equals(Funcoes.StringToDate(condenacao.getDataTransitoJulgado())))) {
							condenacao.setEmFuga(eventoFuga, true);
						} else {
							condenacao.setEmFuga(eventoFuga, false);
						}
					    listaCondenacaoComInicioCumprimento.get(eventoFuga.getId()).add(condenacao);
				    }
		    	}
			}
			
			if (isPrescricaoExecUnificada)
				msg = calcularValidadeMandadoPrisaoUnificada(listaEvento, listaCondenacao, listaFuga, listaPrisao, calculoLiquidacaoDt, mapArtigo, dataNascimento);
			else {
				if ((listaFuga.size() - listaPrisao.size()) > 1)
					throw new MensagemException("Não é possível fazer o cálculo da prescrição executória. Verifique os lapsos de fuga/prisão.");
				
				getPrescricaoExecIndIniciouCumprimento(listaEvento, listaCondenacaoComInicioCumprimento, listaFuga, listaPrisao, calculoLiquidacaoDt, mapArtigo, dataNascimento);
			}
			


			if (listaCondenacaoSemInicioCumprimento.size() > 0) {
				getPrescricaoExecIndNaoIniciouCumprimento(listaEvento, listaCondenacaoSemInicioCumprimento, calculoLiquidacaoDt, mapArtigo, dataNascimento);
			}

			
		} else {
		    	// **************************************NÃO INICIOU CUMPRIMENTO DA PENA*********************************************
			getPrescricaoExecIndNaoIniciouCumprimento(listaEvento, listaCondenacao, calculoLiquidacaoDt, mapArtigo, dataNascimento);
		}
		if (!isPrescricaoExecUnificada && calculoLiquidacaoDt.getListaPrescricaoExecutoria().size() == 0){
			msg = "Não há prescrição de pena executória individual.";
		}
		return msg;
	}

	private void getPrescricaoExecIndIniciouCumprimento(List listaEvento, Map<String, List> listaCondenacao, List listaFuga, List listaPrisao, CalculoLiquidacaoDt calculoLiquidacaoDt, HashMap mapArtigo, String dataNascimento) throws Exception{
		// para cada lapso temporal (de fuga/prisão), verifica se alguma condenação foi prescrita.
		for (int i = 0; i < listaFuga.size(); i++) {
			ProcessoEventoExecucaoDt eventoFuga = (ProcessoEventoExecucaoDt) listaFuga.get(i);

			// variável utilizada no caso do restante da pena ser negativo.
			// neste caso, este tempo que sobrou será diminuído do restante
			// da pena da próxima condenação (do mesmo lapso de fuga)
			int sobraRestantePenaCrimeAnterior = 0;

			// lista as condenações até a data da fuga
//						List listaCondenacaoOrdenada1 = ordenarCrimeMaisGrave_ateDataBase(listaCondenacaoOrdenada, listaEvento, eventoFuga.getDataInicio(), true);
			List listaCondenacaoOrdenada = ordenarCrimeDICC(listaCondenacao.get(eventoFuga.getId()), true); 
			listaCondenacaoOrdenada = getListaCondenacaoAteData(listaCondenacao.get(eventoFuga.getId()), eventoFuga.getDataInicio(), eventoFuga.getDataFim());
			if (!eventoFuga.getDataFim().isEmpty()){
				List listaAuxiliar = new ArrayList(listaCondenacaoOrdenada);
				for(Object o: listaAuxiliar){
					CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) o;
					if(Funcoes.StringToDate(condenacao.getDataTransitoJulgado()).after(Funcoes.StringToDate(eventoFuga.getDataFim())))
						listaCondenacaoOrdenada.remove(condenacao);
				}
			}
			int tempoCumpridoAteFuga = calcularTempoCumpridoAteDataInicioEvento(listaEvento, eventoFuga.getDataInicio());


			// Prepara Lista para o cálculo, setando o atributo temporário com o valor cumprido de cada evento
			for (int j = 0; j < listaEvento.size(); j++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(j);
				evento.setTempoCumpridoCalculadoDias(Funcoes.StringToInt(evento.getTempoCumpridoDias()));				
			}
			
			
			for (int w = 0; w < listaCondenacaoOrdenada.size(); w++) {
				CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(w);
//							ProcessoExecucaoDt processoExecucao = new ProcessoExecucaoNe().consultarId(condenacao.getId_ProcessoExecucao());
				String dataSentenca = new ProcessoExecucaoNe().consultarDataSentenca(condenacao.getId_ProcessoExecucao());

				HashMap map = calcularRestantePenaCrime(condenacao, eventoFuga.getDataInicio(), listaEvento, tempoCumpridoAteFuga, w, calculoLiquidacaoDt.getTipoRemicao());
				int restantePenaCrimeAteFuga = Funcoes.StringToInt(map.get("restantePenaCrime").toString());
				tempoCumpridoAteFuga = Funcoes.StringToInt(map.get("tempoCumpridoTotal").toString());

				// utiliza a sobra do restante da pena do crime anterior (quando o restante da pena deste crime é negativo) neste crime
				if (restantePenaCrimeAteFuga > 0 && sobraRestantePenaCrimeAnterior > 0) {
					restantePenaCrimeAteFuga -= sobraRestantePenaCrimeAnterior;
					sobraRestantePenaCrimeAnterior = 0;
				}

				int tempoPrescricao = 0;

				// verifica se o restante da pena a cumprir desta condenação é negativo, neste caso:
				// - este tempo será diminuído do restante da pena da próxima condenação (do mesmo lapso de fuga)
				// - o tempo da prescrição será zerado e a data da prescrição será um dia após a fuga.
				// ----- alteração feita em 02/09/2011 ----------
				if (restantePenaCrimeAteFuga < 0) {
					sobraRestantePenaCrimeAnterior = Math.abs(restantePenaCrimeAteFuga);
				} else {
					tempoPrescricao = calcularTempoPrescricao(String.valueOf(restantePenaCrimeAteFuga), true, condenacao.getDataFato(), condenacao.isReincidente(), mapArtigo, isMenoridadeMaioridadePrescricao(dataNascimento, condenacao.getDataFato(), dataSentenca), false);
				}

				// última fuga da lista e não existe prisão correspondente à fuga
				// verifica se o sentenciado está foragido (existe mais fuga do que prisão)
				if (i == listaFuga.size() - 1 && (listaFuga.size() > listaPrisao.size())) {
					isPenaPrescrita_calcularDataPrescricao(calculoLiquidacaoDt, condenacao.getTempoPenaEmDias(), condenacao.getTempoPenaRemanescenteEmDias(), tempoPrescricao, String.valueOf(restantePenaCrimeAteFuga), condenacao.isEmFuga(eventoFuga) ? condenacao.getDataTransitoJulgado() :  eventoFuga.getDataInicio(), "", false);
					calculoLiquidacaoDt.setDataValidadeMandadoPrisaoIndividual(getDataValidadeMandadoPrisaoIndividual(calculoLiquidacaoDt.getListaPrescricaoExecutoria()));
				}

				// indica que não é a última fuga ou, sendo a última fuga, existe uma prisão correspondente
				else {
					ProcessoEventoExecucaoDt eventoPrisao = new ProcessoEventoExecucaoDt();
					if(listaPrisao.get(i) != null){
						eventoPrisao = (ProcessoEventoExecucaoDt) listaPrisao.get(i);
					}
					int lapsoTemporal = Funcoes.calculaDiferencaEntreDatas(condenacao.isEmFuga(eventoFuga) ? condenacao.getDataTransitoJulgado() :  eventoFuga.getDataInicio(), eventoPrisao.getDataInicio());
					if ((i > 0) || lapsoTemporal > 1)
						isPenaPrescrita_calcularDataPrescricao(calculoLiquidacaoDt, condenacao.getTempoPenaEmDias(), condenacao.getTempoPenaRemanescenteEmDias(), tempoPrescricao, String.valueOf(restantePenaCrimeAteFuga), condenacao.isEmFuga(eventoFuga) ? condenacao.getDataTransitoJulgado() :  eventoFuga.getDataInicio(), eventoPrisao.getDataInicio(), false);
				}
			}
		}
	}

	private void getPrescricaoExecIndNaoIniciouCumprimento(List listaEvento, List listaCondenacao, CalculoLiquidacaoDt calculoLiquidacaoDt, HashMap mapArtigo, String dataNascimento) throws Exception{
		// variável utilizada no caso do restante da pena ser negativo.
		// neste caso, este tempo que sobrou será diminuído do restante da pena
		// da próxima condenação (do mesmo lapso de fuga)
		int sobraRestantePenaCrimeAnterior = 0;

		// lista as condenações até a data da fuga
//		List listaCondenacaoOrdenada = ordenarCrimeMaisGrave_ateDataBase(listaCondenacao, listaEvento, ((ProcessoEventoExecucaoDt) listaEvento.get(listaEvento.size() - 1)).getDataInicio(), false);
		List listaCondenacaoOrdenada = ordenarCrimeDICC(listaCondenacao, false);
		int tempoCumpridoAteTJ = 0;
		
		if (calculoLiquidacaoDt.isConsiderarDetracao())
			tempoCumpridoAteTJ += Funcoes.StringToInt(calculoLiquidacaoDt.getTempoCumpridoUltimoEventoDias());

//		String dataTJ = "";
//		String dataSentenca = "";
//		String idProcessoExecucaoUltimoTJ = "";

		// utilizado no cálculo da validade do mandado de prisão da pena unificada
//		for (int i = listaEvento.size() - 1; i >= 0; i--) {
//			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
//			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
//					|| evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
//				idProcessoExecucaoUltimoTJ = evento.getId_ProcessoExecucao();
//				break;
//			}
//		}
		

		// Prepara Lista para o cálculo, setando o atributo temporário com o valor cumprido de cada evento
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			evento.setTempoCumpridoCalculadoDias(Funcoes.StringToInt(evento.getTempoCumpridoDias()));				
		}
		

		for (int w = 0; w < listaCondenacaoOrdenada.size(); w++) {
			CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(w);
			ProcessoExecucaoDt processoExecucao = new ProcessoExecucaoNe().consultarId(condenacao.getId_ProcessoExecucao());

			String dataTJ = processoExecucao.getDataTransitoJulgado();
			if (processoExecucao.getDataTransitoJulgadoMP().length() > 0)
				dataTJ = processoExecucao.getDataTransitoJulgadoMP();
			HashMap map = calcularRestantePenaCrime(condenacao, dataTJ, listaEvento, tempoCumpridoAteTJ, w, calculoLiquidacaoDt.getTipoRemicao());
			int restantePenaCrimeAteFuga = Funcoes.StringToInt(map.get("restantePenaCrime").toString());
			tempoCumpridoAteTJ = Funcoes.StringToInt(map.get("tempoCumpridoTotal").toString());

			// utiliza a sobra do restante da pena do crime anterior (quando o
			// restante da pena deste crime é negativo) neste crime
			if (restantePenaCrimeAteFuga > 0 && sobraRestantePenaCrimeAnterior > 0) {
				restantePenaCrimeAteFuga -= sobraRestantePenaCrimeAnterior;
				sobraRestantePenaCrimeAnterior = 0;
			}

			int tempoPrescricao = 0;

			// verifica se o restante da pena a cumprir desta condenação é
			// negativo, neste caso:
			// - este tempo será diminuído do restante da pena da próxima
			// condenação (do mesmo lapso de fuga)
			// - o tempo da prescrição será zerado e a data da prescrição será
			// um dia após a fuga.
			// ----- alteração feita em 02/09/2011 ----------
			if (restantePenaCrimeAteFuga < 0) {
				sobraRestantePenaCrimeAnterior = Math.abs(restantePenaCrimeAteFuga);
			} else {
				tempoPrescricao = calcularTempoPrescricao(String.valueOf(restantePenaCrimeAteFuga), true, condenacao.getDataFato(), condenacao.isReincidente(), mapArtigo, isMenoridadeMaioridadePrescricao(dataNascimento, condenacao.getDataFato(), processoExecucao.getDataSentenca()), false);
			}
			isPenaPrescrita_calcularDataPrescricao(calculoLiquidacaoDt, condenacao.getTempoPenaEmDias(), condenacao.getTempoPenaRemanescenteEmDias(), tempoPrescricao, String.valueOf(restantePenaCrimeAteFuga), dataTJ, "", false);

			// utilizado no cálculo da validade do mandado de prisão da pena
			// unificada
//			if (processoExecucao.getId().equals(idProcessoExecucaoUltimoTJ)) {
////				dataTJ = processoExecucao.getDataTransitoJulgado();
////				dataSentenca = processoExecucao.getDataSentenca();
//			}
		}

		calculoLiquidacaoDt.setDataValidadeMandadoPrisaoIndividual(getDataValidadeMandadoPrisaoIndividual(calculoLiquidacaoDt.getListaPrescricaoExecutoria()));
	}

	public String getDataValidadeMandadoPrisaoIndividual(List listaPrescricaoExecutoria) throws Exception{
		String data = "";
		
		for (CalculoLiquidacaoPrescricaoDt prescricao: (List<CalculoLiquidacaoPrescricaoDt>)listaPrescricaoExecutoria) {
			//para data prescrição diferente de "--"
			if (data.length() <= 2)
				data = prescricao.getDataPrescricao();
			else if (prescricao.getDataPrescricao().length() > 2 && Funcoes.StringToDate(data).before(Funcoes.StringToDate(prescricao.getDataPrescricao())))
				data = prescricao.getDataPrescricao();
		}
		
		return data;
	}
	
	/**
	 * Calcula a data de validade do mandado de prisão utilizando o restante da pena até a data atual para verificar o tempo para prescrição. Considera apenas a reincidência, não verifica se o sentenciado é menor ou maior de idade na data do fato, uma vez que a pena é unificada.
	 * 
	 * @param listaCondenacao
	 *            : lista com todas as condenações
	 * @param calculoLiquidacaoDt
	 *            : objeto com os dados do cálculo
	 * @param mapArtigo
	 * @param dataUltimaFuga
	 *            : data da última fuga
	 * @return: data de validade do mandado de prisão.
	 * @throws Exception
	 */
	private String calcularValidadeMandadoPrisaoUnificada(List listaEvento, List listaCondenacao, List listaFuga, List listaPrisao, CalculoLiquidacaoDt calculoLiquidacaoDt, HashMap mapArtigo, String dataNascimento) throws Exception{
		String dataPrescricao = "";
		String msg = "";
		
		boolean isReincidente = false;
		boolean isMenoridade = true;
//			String dataFato = "";

		//verifica se o sentenciado está foragido (existe mais fuga do que prisão)
		String maiorDataFato = "";
		if (listaFuga.size() > listaPrisao.size()){
			ProcessoEventoExecucaoDt eventoUltimaFuga = (ProcessoEventoExecucaoDt) listaFuga.get(listaFuga.size() -1);
			
			for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>) listaCondenacao) {
				String dataSentenca = new ProcessoExecucaoNe().consultarDataSentenca(condenacao.getId_ProcessoExecucao());
				
				if (condenacao.isReincidente() && !isReincidente) {
					// deve ser reincidente em pelo menos uma condenação
					isReincidente = true; 
				}
				
				if (!isMenoridadeMaioridadePrescricao(dataNascimento, condenacao.getDataFato(), dataSentenca) && isMenoridade) {
					// deve ser menor de idade em todas as condenações
					isMenoridade = false; 
				}
				
				//Alteração para atender lei de 2010 sob prescrição de pena de 2 ou 3 anos para
				//atender réus foragidos. Solicitado por Nair.
				if (maiorDataFato.isEmpty() || Funcoes.StringToDate(maiorDataFato).before(Funcoes.StringToDate(condenacao.getDataFato()))){
					maiorDataFato = condenacao.getDataFato();
				}
			}
			
//				// data do fato: colocar a data da última fuga - definido com Nair em 02/03/2012
//				if (listaCondenacao != null && listaCondenacao.size() > 1){
//					dataFato = eventoUltimaFuga.getDataInicio(); 
//				}
			
			int tempoPrescricao = calcularTempoPrescricao(calculoLiquidacaoDt.getTempoRestanteDataAtualDias(), true, maiorDataFato, isReincidente, mapArtigo, isMenoridade, true);

			dataPrescricao = Funcoes.somaData(eventoUltimaFuga.getDataInicio(), tempoPrescricao - 1);
			
			if (tempoPrescricao == 0) {
				calculoLiquidacaoDt.setTempoPrescricaoUnificadaAnos("");
				calculoLiquidacaoDt.setDataValidadeMandadoPrisaoUnificada("");
				calculoLiquidacaoDt.setDataUltimaFuga(eventoUltimaFuga.getDataInicio());
				msg =  "Não é possível efetuar o cálculo: PENA CUMPRIDA!";
			}
			else {
				calculoLiquidacaoDt.setTempoPrescricaoUnificadaAnos(Funcoes.converterParaAnoMesDia(tempoPrescricao));
				calculoLiquidacaoDt.setDataValidadeMandadoPrisaoUnificada(dataPrescricao);
				calculoLiquidacaoDt.setDataUltimaFuga(eventoUltimaFuga.getDataInicio());
			}
		} 
		if (calculoLiquidacaoDt.getDataValidadeMandadoPrisaoUnificada().length() == 0 && calculoLiquidacaoDt.getDataUltimaFuga().length() == 0) {
			msg =  "Não é possível efetuar o cálculo: SENTENCIADO CUMPRINDO PENA!";
		}			
		return msg;

	}

	/**
	 * calcula o tempo necessário para prescrição da pena, conforme artigos 109, 110 e 115 do código penal.
	 * 
	 * @param tempoPenaDias
	 *            : tempo da pena
	 * @param prescricaoExecutoria
	 *            : tipo do cálculo de prescrição
	 * @param dataNascimento
	 *            : data de nascimento do sentenciado
	 * @param dataFato
	 *            : data do fato referente à condenação
	 * @param dataSentenca
	 *            : data da sentença referente à condenação
	 * @return int tempoPrescricao: tempo para prescrição da pena.
	 * @throws Exception
	 * @author wcsilva
	 */
	public int calcularTempoPrescricao(String tempoPenaDias, boolean prescricaoExecutoria, String dataFato, boolean isReincidente, HashMap mapArtigo, boolean isMenoridadeMaioridade, boolean prescExecUnificada) throws Exception{

		String msgErro = "";
		if (dataFato.length() == 0) msgErro += "A data do fato não foi informada!";

		// verifica o tempo necessário para prescrição da pena.
		int tempoPrescricao = 0;
		int penaDias = Funcoes.StringToInt(tempoPenaDias);

		if (penaDias == 0) {
			tempoPrescricao = 0;
		} else if (penaDias > 4383) {// tempo de condenação > 12 anos (4383 dias)
			tempoPrescricao = 7305; // 20 anos
			if (prescExecUnificada) mapArtigo.put("109", "I");
			else
				mapArtigo.put("I", true);
		}

		else if (penaDias > 2922 && penaDias <= 4383) {// tempo de condenação > 8 anos (2922 dias) e <= 12 anos (4383 dias)
			tempoPrescricao = 5844; // 16 anos
			if (prescExecUnificada) mapArtigo.put("109", "II");
			else
				mapArtigo.put("II", true);
		}

		else if (penaDias > 1461 && penaDias <= 2922) {// tempo de condenação > 4 anos (1461 dias) e <= 8 anos (2922 dias)
			tempoPrescricao = 4383; // 12 anos
			if (prescExecUnificada) mapArtigo.put("109", "III");
			else
				mapArtigo.put("III", true);
		}

		else if (penaDias > 730 && penaDias <= 1461) {// tempo de condenação > 2anos (730 dias) e <= 4 anos (1461 dias)
			tempoPrescricao = 2922; // 8 anos
			if (prescExecUnificada) mapArtigo.put("109", "IV");
			else
				mapArtigo.put("IV", true);
		}

		else if (penaDias >= 365 && penaDias <= 730) {// tempo de condenação >= 1 ano (365 dias) e <= 2 anos (730 dias)
			tempoPrescricao = 1461; // 4 anos
			if (prescExecUnificada) mapArtigo.put("109", "V");
			else
				mapArtigo.put("V", true);
		}
				//tempo de condenação < 1 ano (365 dias) e data do fato >=  06/05/2010	
		else if (penaDias < 365 && Funcoes.StringToDate(dataFato).after(Funcoes.StringToDate("05/05/2010"))) {
			tempoPrescricao = 1095; // 3 anos (1095 dias)
			if (prescExecUnificada) mapArtigo.put("109", "VI");
			else
				mapArtigo.put("VI", true);
		}

		// tempo de condenação < 1 ano (365 dias) e data do fato < 06/05/2010
		else if (penaDias < 365 && Funcoes.StringToDate(dataFato).before(Funcoes.StringToDate("06/05/2010"))) {
			tempoPrescricao = 730; // 2 anos
			if (prescExecUnificada) mapArtigo.put("109", "VI");
			else
				mapArtigo.put("VI", true);
		}

		// Reduz o tempo da prescrição pela metade se:
		// - na data do fato o sentenciado tinha menos de 21 anos (7670 dias) 
		// ou na data da sentença o sentenciado tinha mais de 70 anos (25567 dias)
		if (isMenoridadeMaioridade) {
			tempoPrescricao = (int) Math.ceil(tempoPrescricao / 2);
			mapArtigo.put("115", true);
		}

		// Aumenta o tempo da prescrição em 1/3 se reincidente, para a prescrição executória
		if (prescricaoExecutoria && isReincidente) {
			tempoPrescricao = (int) Math.ceil(tempoPrescricao + ((double) tempoPrescricao * 1 / 3));
			mapArtigo.put("113", true);
			mapArtigo.put("110", true);
		}
		
		
		return tempoPrescricao;
	}

	/**
	 * Verifica a menoridade e maioridade para prescrição da penal
	 * 
	 * @param dataNascimento
	 * @param dataFato
	 * @return
	 * @throws Exception 
	 */
	public boolean isMenoridadeMaioridadePrescricao(String dataNascimento, String dataFato, String dataSentenca) throws Exception{
		
		if (dataNascimento.length() > 0 && dataFato.length() > 0 && dataSentenca.length() > 0) {
			// na data do fato o sentenciado tinha menos de 21 anos (7670
			// dias) ou na data da sentença o sentenciado tinha mais de 70
			// anos (25567 dias)
			if (Funcoes.calculaDiferencaEntreDatas(dataNascimento, dataFato) < 7670 || (Funcoes.calculaDiferencaEntreDatas(dataNascimento, dataSentenca) >= 25567)) return true;
			else
				return false;
		}
		return false;
		
	}

	/**
	 * Calcula a data da prescrição de pena, se houver.
	 * 
	 * @param calculoLiquidacaoDt
	 * @param tempoCondenacaoDias
	 * @param tempoPrescricaoDias
	 * @param restantePenaDias
	 * @param dataInicio
	 * @param dataFim
	 * @return boolean: indica se foi detectado prescrição de pena.
	 * @throws Exception
	 */
	private boolean isPenaPrescrita_calcularDataPrescricao(CalculoLiquidacaoDt calculoLiquidacaoDt, String tempoCondenacaoDias, String tempoCondenacaoRemanescenteDias, int tempoPrescricaoDias, String restantePenaDias, String dataInicio, String dataFim, boolean isPrescricaoPunitiva) throws Exception{

		boolean isPenaPrescrita = false;
		int lapsoTemporal = 0;

		String dataPrescricao = "";
		if (tempoPrescricaoDias > 0) dataPrescricao = Funcoes.somaData(dataInicio, tempoPrescricaoDias - 1);
		else
			// no caso do restante da pena ser negativo
			dataPrescricao = Funcoes.somaData(dataInicio, 1); 

		// verifica prescrição para os lapsos onde existe a prisão respectiva à fuga
		if (dataFim.length() > 0) {
			lapsoTemporal = Funcoes.calculaDiferencaEntreDatas(dataInicio, dataFim);

			if (lapsoTemporal >= tempoPrescricaoDias) isPenaPrescrita = true;

		// verifica a prescrição para a última fuga (não tem prisão=dataFim)
		} else {
			if (Funcoes.StringToDate(dataPrescricao).before(new Date())) isPenaPrescrita = true;
		}

		CalculoLiquidacaoPrescricaoDt prescricao = new CalculoLiquidacaoPrescricaoDt();
		prescricao.setTempoCondenacaoDias(tempoCondenacaoDias);
		prescricao.setTempoCondenacaoRemanescenteDias(tempoCondenacaoRemanescenteDias);
		prescricao.setTempoPrescricaoDias(String.valueOf(tempoPrescricaoDias));
		prescricao.setDataPrescricao(dataPrescricao);
		prescricao.setRestantePenaDias(restantePenaDias);
		prescricao.setDataInicioLapso(dataInicio);
		if (dataFim.length() == 0) prescricao.setDataFimLapso("---");
		else
			prescricao.setDataFimLapso(dataFim);
		prescricao.setTempoLapsoDias(String.valueOf(lapsoTemporal));
		
		String stPenaPrescrita = "";
		if (!isPrescricaoPunitiva && (restantePenaDias.equals("0") || restantePenaDias.length() == 0)) {
			stPenaPrescrita = "--";
			prescricao.setDataPrescricao("--");
		}
		else if (isPenaPrescrita) stPenaPrescrita = "SIM";
		else stPenaPrescrita = "Não";
		
		prescricao.setBoHouvePrescricao(isPenaPrescrita, stPenaPrescrita);

		if (isPrescricaoPunitiva) calculoLiquidacaoDt.addListaPrescricaoPunitiva(prescricao);
		else{
			calculoLiquidacaoDt.addListaPrescricaoExecutoria(prescricao);
		}

		return isPenaPrescrita;
	}

	/**
	 * utilizado para a montagem do relatório de cálculo de liquidação de penas
	 * 
	 * @param diretorioProjetos
	 * @param listaEventosBean
	 *            - lista com os eventos
	 * @param calculoLiquidacaoDt
	 *            - objeto CálculoLiquidacaoDt com os dados do cálculo
	 * @return
	 * @throws IOException 
	 */
	public byte[] relCalculoLiquidacao(String diretorioProjetos, List listaEventos, List listaEventoPSC, List listaEventoLFS, List listaEventoPEC, List listaEventoITD, List listaEventoPCB, CalculoLiquidacaoDt calculoLiquidacaoDt, ProcessoDt processoDt, String informacoes, boolean calculoNaoOficial, List listaCondenacaoExtinta, String tituloRelatorio, String iniciaisNome) throws Exception{

		String forum = consultarDescricaoForum(processoDt.getId_AreaDistribuicao());

		// cria a lista para incluir no subreport
		List arrayDadosGerais = new ArrayList();
		arrayDadosGerais.add(calculoLiquidacaoDt);

		// verifica se o cálculo de indulto e comutação foram feitos, caso estejam na lista
		verificarListaCalculo(calculoLiquidacaoDt);

		List beanListaEventos = montarBeanListaEvento(listaEventos);

		List beanListaEventosPSC = null;
		if (listaEventoPSC != null && listaEventoPSC.size() > 0) beanListaEventosPSC = montarBeanListaEvento(listaEventoPSC);
		
		List beanListaEventosPEC = null;
		if (listaEventoPEC != null && listaEventoPEC.size() > 0) beanListaEventosPEC = montarBeanListaEvento(listaEventoPEC);

		List beanListaEventosLFS = null;
		if (listaEventoLFS != null && listaEventoLFS.size() > 0) beanListaEventosLFS = montarBeanListaEvento(listaEventoLFS);
		
		List beanListaEventosITD = null;
		if (listaEventoITD != null && listaEventoITD.size() > 0) beanListaEventosITD = montarBeanListaEvento(listaEventoITD);
		
		List beanListaEventosPCB = null;
		if (listaEventoPCB != null && listaEventoPCB.size() > 0) beanListaEventosPCB = montarBeanListaEvento(listaEventoPCB);

		// cria map dos subreports
		Map subReports = new HashMap();
		subReports.put("detalhes_evento", beanListaEventos);

		boolean isPenaRestritiva = false;
		if (isProcessoPenaRestritivaDireito(listaEventos)) isPenaRestritiva = true;
		if (isPenaRestritiva) {
			if (beanListaEventosPSC != null) subReports.put("detalhes_evento_PSC", beanListaEventosPSC);
			if (beanListaEventosLFS != null) subReports.put("detalhes_evento_LFS", beanListaEventosLFS);
			if (beanListaEventosPEC != null) subReports.put("detalhes_evento_PEC", beanListaEventosPEC);
			if (beanListaEventosITD != null) subReports.put("detalhes_evento_ITD", beanListaEventosITD);
			if (beanListaEventosPCB != null) subReports.put("detalhes_evento_PCB", beanListaEventosPCB);
		}

		if (listaCondenacaoExtinta != null && listaCondenacaoExtinta.size() > 0) subReports.put("detalhes_CondenacaoExtinta", listaCondenacaoExtinta);

		// cria um subReport para cada tipo de cálculo e adiciona a lista(array) em cada subReport
		List array = new ArrayList();
		// parâmetros do relatório
		Map parametros = new HashMap();
		// interface utilizda para montar mais de um subrelatório
		InterfaceMultiplaSubReportJasper ei = new InterfaceMultiplaSubReportJasper(subReports);
		String pathJasper = diretorioProjetos + "WEB-INF" + File.separator + "relatorios" + File.separator + "calculoLiquidacao" + File.separator;
		
		if (isPenaRestritiva) {
			if (calculoLiquidacaoDt.getCalculoPenaRestritivaDt() == null) calculoLiquidacaoDt.newCalculoPenaRestritivaDt();
			calculoLiquidacaoDt.getCalculoPenaRestritivaDt().setTempoTotalCondenacaoAnos(calculoLiquidacaoDt.getTempoTotalCondenacaoAnos());
			calculoLiquidacaoDt.getCalculoPenaRestritivaDt().setTempoCumpridoUltimoEventoAnos(calculoLiquidacaoDt.getTempoCumpridoUltimoEventoAnos());
			calculoLiquidacaoDt.getCalculoPenaRestritivaDt().setTempoTotalComutacaoAnos(calculoLiquidacaoDt.getTempoTotalComutacaoAnos());
			calculoLiquidacaoDt.getCalculoPenaRestritivaDt().setTempoTotalComutacaoDias(calculoLiquidacaoDt.getTempoTotalComutacaoDias());
			calculoLiquidacaoDt.getCalculoPenaRestritivaDt().setTempoTotalCondenacaoRemanescenteAnos(calculoLiquidacaoDt.getTempoTotalCondenacaoRemanescenteAnos());
			calculoLiquidacaoDt.getCalculoPenaRestritivaDt().setVisualizaRestantePenaUltimoEvento(calculoLiquidacaoDt.getVisualizaRestantePenaUltimoEvento());
			calculoLiquidacaoDt.getCalculoPenaRestritivaDt().setTempoRestanteUltimoEventoAnos(calculoLiquidacaoDt.getTempoRestanteUltimoEventoAnos());
			array.add(calculoLiquidacaoDt.getCalculoPenaRestritivaDt());
			subReports.put("detalhes_geral_PRD", array);
		} else {
			subReports.put("detalhes_geral", arrayDadosGerais);
		}
		
		if (calculoLiquidacaoDt.getListaTipoCalculo() != null) {
			for (int i = 0; i < calculoLiquidacaoDt.getListaTipoCalculo().size(); i++) {
				array = new ArrayList();
				if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("PRESCRICAO_PUNITIVA")) {
					String textoPrescPunitiva = "PRESCRIÇÃO PUNITIVA (Retroativa e/ou Intercorrente) da Pena Individual";
					if (calculoLiquidacaoDt.getListaPrescricaoPunitiva() != null && calculoLiquidacaoDt.getListaPrescricaoPunitiva().size() > 0) {
						subReports.put("detalhes_PRESCRICAOPunitiva", calculoLiquidacaoDt.getListaPrescricaoPunitiva());
						parametros.put("pathRelatorioCalculo_PRESCRICAOPunitiva", pathJasper + "calculoLiquidacaoDetalhes_PRESCRICAOPunitiva.jasper");
					} else {
						textoPrescPunitiva += ": Não há Prescrição Punitiva!";
					}
					parametros.put("textoPrescPunitiva", textoPrescPunitiva);
					
				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("PRESCRICAO_EXECUTORIA_IND")) {
					String textoPrescExecutoria = "PRESCRIÇÃO EXECUTÓRIA da Pena Individual";
					if (calculoLiquidacaoDt.getListaPrescricaoExecutoria() != null && calculoLiquidacaoDt.getListaPrescricaoExecutoria().size() > 0) {
						subReports.put("detalhes_PRESCRICAOExecutoria", calculoLiquidacaoDt.getListaPrescricaoExecutoria());
						parametros.put("pathRelatorioCalculo_PRESCRICAOExecutoria", pathJasper + "calculoLiquidacaoDetalhes_PRESCRICAOExecutoria.jasper");
					} else {
						textoPrescExecutoria += ": Não há Prescrição Executória!";
					}
					parametros.put("textoPrescExecutoria", textoPrescExecutoria);
					
				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("PRESCRICAO_EXECUTORIA_UNI") && calculoLiquidacaoDt.getDataValidadeMandadoPrisaoUnificada().length() > 0) {
					array.add(calculoLiquidacaoDt);
					subReports.put("detalhes_PRESCRICAOUnificada", array);
					parametros.put("pathRelatorioCalculo_PRESCRICAOUnificada", pathJasper + "calculoLiquidacaoDetalhes_PRESCRICAOUnificada.jasper");
					
				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("COMUTACAO")) {
					if (calculoLiquidacaoDt.getListaComutacao() != null && calculoLiquidacaoDt.getListaComutacao().size() > 0) {
						subReports.put("detalhes_COMUTACAO", calculoLiquidacaoDt.getListaComutacao());
						parametros.put("pathRelatorioCalculo_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i), pathJasper + "calculoLiquidacaoDetalhes_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i) + ".jasper");
					}
					
				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("COMUTACAO_UNIFICADA")) {
					if (calculoLiquidacaoDt.getListaComutacaoUnificada() != null && calculoLiquidacaoDt.getListaComutacaoUnificada().size() > 0) {
						subReports.put("detalhes_COMUTACAOUnificada", calculoLiquidacaoDt.getListaComutacaoUnificada());
						parametros.put("pathRelatorioCalculo_COMUTACAOUnificada", pathJasper + "calculoLiquidacaoDetalhes_COMUTACAOUnificada.jasper");
					}
					
				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("PENARESTRITIVA")) {
					if (calculoLiquidacaoDt.getCalculoPenaRestritivaDt() != null) {
						array.add(calculoLiquidacaoDt.getCalculoPenaRestritivaDt());
						if (calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getTotalCondenacaoPSCHoras().length() > 0 && calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getTotalCondenacaoPSCHoras() != "0"){
							subReports.put("detalhes_PSC", array);
							parametros.put("pathRelatorioCalculo_PSC", pathJasper + "calculoLiquidacaoDetalhes_PSC.jasper");
						}
						if (calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getTotalCondenacaoLFSDias().length() > 0 && calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getTotalCondenacaoLFSDias() != "0"){
							subReports.put("detalhes_LFS", array);
							parametros.put("pathRelatorioCalculo_LFS", pathJasper + "calculoLiquidacaoDetalhes_LFS.jasper");
						}
						if (calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getTotalPagamentoPEC().length() > 0){
							subReports.put("detalhes_PEC", array);
							parametros.put("pathRelatorioCalculo_PEC", pathJasper + "calculoLiquidacaoDetalhes_PEC.jasper");
						}
						if (calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getTotalCondenacaoITDDias().length() > 0 && calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getTotalCondenacaoITDDias() != "0"){
							subReports.put("detalhes_ITD", array);
							parametros.put("pathRelatorioCalculo_ITD", pathJasper + "calculoLiquidacaoDetalhes_ITD.jasper");
						}
					}
				} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("SURSIS")){
					if (calculoLiquidacaoDt.getListaSursis() != null && calculoLiquidacaoDt.getListaSursis().size() > 0) {
						array = calculoLiquidacaoDt.getListaSursis();
						subReports.put("detalhes_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i), array);
						parametros.put("pathRelatorioCalculo_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i), pathJasper + "calculoLiquidacaoDetalhes_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i) + ".jasper");
					}
				} else {
					
					if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("PROGRESSAO")) {
						array.add(calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt());
					} else if (((String) calculoLiquidacaoDt.getListaTipoCalculo().get(i)).equalsIgnoreCase("INDULTO")) {
						array.add(calculoLiquidacaoDt.getCalculoIndultoDt());
					}
					subReports.put("detalhes_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i), array);
					parametros.put("pathRelatorioCalculo_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i), pathJasper + "calculoLiquidacaoDetalhes_" + calculoLiquidacaoDt.getListaTipoCalculo().get(i) + ".jasper");
				}
			}
		}

		parametros.put("pathRelatorioEvento", pathJasper + "calculoLiquidacaoDetalhes_Evento.jasper");
		if (isPenaRestritiva) {
			if (beanListaEventosPSC != null) parametros.put("pathRelatorioEvento_PSC", pathJasper + "calculoLiquidacaoDetalhes_Evento_PSC.jasper");
			if (beanListaEventosLFS != null) parametros.put("pathRelatorioEvento_LFS", pathJasper + "calculoLiquidacaoDetalhes_Evento_LFS.jasper");
			if (beanListaEventosPEC != null) parametros.put("pathRelatorioEvento_PEC", pathJasper + "calculoLiquidacaoDetalhes_Evento_PEC.jasper");
			if (beanListaEventosITD != null) parametros.put("pathRelatorioEvento_ITD", pathJasper + "calculoLiquidacaoDetalhes_Evento_ITD.jasper");
			if (beanListaEventosPCB != null) parametros.put("pathRelatorioEvento_PCB", pathJasper + "calculoLiquidacaoDetalhes_Evento_PCB.jasper");
		}
		if (isPenaRestritiva) parametros.put("pathRelatorioCalculo_Geral_PRD", pathJasper + "calculoLiquidacaoDetalhes_Geral_PRD.jasper");
		else
			parametros.put("pathRelatorioCalculo_Geral", pathJasper + "calculoLiquidacaoDetalhes_Geral.jasper");
		parametros.put("numeroProcesso", processoDt.getProcessoNumeroCompleto());
		parametros.put("serventia", processoDt.getServentia());
		parametros.put("provavelTerminoPena", calculoLiquidacaoDt.getDataTerminoPena());
		parametros.put("provavelTerminoPenaUnificada", calculoLiquidacaoDt.getDataTerminoPenaUnificada());
		parametros.put("informacoes", informacoes);
		parametros.put("artigoPrescricaoPunitiva", calculoLiquidacaoDt.getArtigoPrescricaoPunitiva());
		parametros.put("artigoPrescricaoExecutoria", calculoLiquidacaoDt.getArtigoPrescricaoExecutoria());
		parametros.put("sentenciado", ((ProcessoParteDt) processoDt.getListaPolosPassivos().get(0)).getNome());
		parametros.put("maeSentenciado", ((ProcessoParteDt) processoDt.getListaPolosPassivos().get(0)).getNomeMae());
		parametros.put("dataNascimento", ((ProcessoParteDt) processoDt.getListaPolosPassivos().get(0)).getDataNascimento());
		parametros.put("forum", forum);
		parametros.put("iniciaisNome", iniciaisNome);
		parametros.put("calculoNaoOficial", calculoNaoOficial);
		parametros.put("tituloRelatorio", tituloRelatorio);
		parametros.put("caminhoFiguraCalculoNaoOficial", diretorioProjetos + "imagens" + File.separator + "doc_faixa.jpg");
		parametros.put("caminhoLogo", diretorioProjetos + "imagens" + File.separator + "logoTJ.jpg");
		if (listaCondenacaoExtinta != null && listaCondenacaoExtinta.size() > 0) parametros.put("pathRelatorio_CondenacaoExtinta", pathJasper + "calculoLiquidacaoDetalhes_CondenacaoExtinta.jasper");
		if (calculoLiquidacaoDt.getDataValidadeMandadoPrisaoIndividual().length() > 0) {
			parametros.put("validadeMandadoPrisaoIndividual", calculoLiquidacaoDt.getDataValidadeMandadoPrisaoIndividual());
		}

		ByteArrayOutputStream baos = null;
		try{
			JasperPrint jp = JasperFillManager.fillReport(pathJasper + "calculoLiquidacao.jasper", parametros, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();

			byte[] temp = baos.toByteArray();

			baos.close();
			return temp;

		} finally {
			try{ if (baos != null) baos.close();		} catch(Exception ex) {			}
		}
	}

	public byte[] relSaidaTemporaria(String diretorioProjetos, SaidaTemporariaDt saidaTemporaria, UsuarioDt usuarioDt) throws Exception {
		byte[] temp = null;
		ByteArrayOutputStream baos = null;

		try{

			if (saidaTemporaria.getListaSaidasTemporarias() != null && saidaTemporaria.getListaSaidasTemporarias().size() > 0) {
				String texto = "";
				for (int i = 0; i < saidaTemporaria.getListaSaidasTemporarias().size(); i++) {
					texto += ((ProcessoEventoExecucaoDt) saidaTemporaria.getListaSaidasTemporarias().get(i)).getDataInicio();
					if (i != saidaTemporaria.getListaSaidasTemporarias().size() - 1) texto += ", ";
				}
				saidaTemporaria.setTextoSaidaTemporaria(texto);
			} else
				saidaTemporaria.setTextoSaidaTemporaria("Não há Saída Temporária neste período.");
			List listaBean = new ArrayList();
			listaBean.add(saidaTemporaria);

			String titulo = "Relatório de Saída Temporária";

			String pathJasper = diretorioProjetos + "WEB-INF" + File.separator + "relatorios" + File.separator + "processoExecucao" + File.separator + "saidaTemporaria.jasper";

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjetos + "imagens" + File.separator + "logoTJ.jpg");
			parametros.put("serventia", usuarioDt.getServentia());
			parametros.put("titulo", titulo);

			InterfaceJasper ei = new InterfaceJasper(listaBean);

			JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();

			temp = baos.toByteArray();
			baos.close();

		} finally{
			try { if (baos != null) baos.close(); } catch(Exception ex) {}			
			baos = null;
		}

		return temp;
	}

	/**
	 * monta o bean com os dados dos eventos, para visualização do relatório
	 * 
	 * @param listaEventos
	 * @return List, processoEventoExecucaoBean
	 * @throws Exception
	 */
	private List montarBeanListaEvento(List listaEventos) throws Exception{
		List listaBean = new ArrayList();
		String informacaoAdicional = "";

		for (int i = 0; i < listaEventos.size(); i++) {
			ProcessoEventoExecucaoRelatorioDt bean = new ProcessoEventoExecucaoRelatorioDt();
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEventos.get(i);

			// oculta do relatório os eventos abaixo:
			if (!processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SAIDA_TEMPORARIA))
					&& !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.BLOQUEIO_SAIDA_TEMPORARIA)) 
					&& !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.BLOQUEIO_TRABALHO_EXTERNO)) 
					&& !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.DESBLOQUEIO_SAIDA_TEMPORARIA)) 
					&& !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.DESBLOQUEIO_TRABALHO_EXTERNO))) {

				if ((Funcoes.StringToInt(processoEventoExecucaoDt.getEventoExecucaoDt().getId()) == EventoExecucaoDt.TRANSITO_JULGADO) 
						|| (Funcoes.StringToInt(processoEventoExecucaoDt.getEventoExecucaoDt().getId()) == EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)) {
					// recuperar informações: data do fato, lei das condenações
					ProcessoEventoExecucaoNe processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
					List listaCondenacao = processoEventoExecucaoNe.listarCrimeCondenacao(processoEventoExecucaoDt.getId_ProcessoExecucao());
					informacaoAdicional = "";
					if (listaCondenacao.size() > 0) {
						for (int j = 0; j < listaCondenacao.size(); j++) {
							informacaoAdicional += listaCondenacao.get(j) + "\n";
						}
					}
					bean.setObservacao(informacaoAdicional + processoEventoExecucaoDt.getObservacao());

					// exclui o nome do regime
					bean.setRegime("");

				} else {
//					bean.setObservacao(processoEventoExecucaoDt.getObservacao() + processoEventoExecucaoDt.getObservacaoAux());
					bean.setObservacao(processoEventoExecucaoDt.getObservacaoVisualizada());
					// exclui a descrição do regime
					if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO))) 
						bean.setRegime(""); 
					else
						bean.setRegime(processoEventoExecucaoDt.getEventoRegimeDt().getRegimeExecucao());
				}
				bean.setCondenacaoAnos(processoEventoExecucaoDt.getCondenacaoAnos());
				bean.setDataFim(processoEventoExecucaoDt.getDataFim());
				bean.setDataInicio(processoEventoExecucaoDt.getDataInicio());
				bean.setEvento(processoEventoExecucaoDt.getEventoExecucaoDt().getEventoExecucao());
				bean.setTempoCumpridoAnos(processoEventoExecucaoDt.getTempoCumpridoAnos());
				if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PAGAMENTO_PEC))){
					bean.setQuantidade(Funcoes.FormatarMoeda(processoEventoExecucaoDt.getQuantidade()));
				}
				listaBean.add(bean);
			}
		}
		return listaBean;
	}

	/**
	 * verifica se o cálculo de comutação e indulto foram realizados. Caso não tenha sido, exlcui a comutação e o indulto da lista. Monta a String com o resultado do cálculo do indulto
	 * 
	 * @param calculo
	 *            - objeto calculoLiquidacaoDt
	 */
	private void verificarListaCalculo(CalculoLiquidacaoDt calculo) {
		String descricao = "";
		boolean montarDadosIndulto = false;

		if (calculo.getListaTipoCalculo() != null) {
			for (int i = 0; i < calculo.getListaTipoCalculo().size(); i++) {
				if (((String) calculo.getListaTipoCalculo().get(i)).equalsIgnoreCase("COMUTACAO")) {
					if (calculo.getCalculoComutacaoDt() == null || calculo.getListaComutacao() == null) {
						calculo.getListaTipoCalculo().remove(i);
						i--;
					}
				} else if (((String) calculo.getListaTipoCalculo().get(i)).equalsIgnoreCase("INDULTO")) {
					if (calculo.getCalculoIndultoDt().getFracaoIndultoComum().length() > 0
							|| calculo.getCalculoIndultoDt().getFracaoIndultoHediondo().length() > 0) {
						montarDadosIndulto = true;
					} else {
						calculo.getListaTipoCalculo().remove(i);
						i--;
					}
				}
			}
		}
		if (montarDadosIndulto) {
			calculo.getCalculoIndultoDt().setDescRelIndulto(getDescricaoResultadoIndulto(calculo));
		}
	}

	public String getDescricaoResultadoIndulto(CalculoLiquidacaoDt calculo){
		String desc = "<b>Cálculo</b>: ";
		
		if (calculo.getCalculoIndultoDt().getFracaoIndultoComum().length() > 0){
			desc += calculo.getCalculoIndultoDt().getFracaoIndultoComum() + " de " + calculo.getCalculoIndultoDt().getTempoCondenacaoComumAnos() + " = " + calculo.getCalculoIndultoDt().getTempoACumprirComumAnos();
		}
		if (calculo.getCalculoIndultoDt().getFracaoIndultoHediondo().length() > 0 && calculo.getCalculoIndultoDt().getFracaoIndultoComum().length() > 0)
			desc += "<b> + </b>";
		
		if (calculo.getCalculoIndultoDt().getFracaoIndultoHediondo().length() > 0){
			desc += calculo.getCalculoIndultoDt().getFracaoIndultoHediondo() + " de " + calculo.getCalculoIndultoDt().getTempoCondenacaoHediondoAnos() + " = " + calculo.getCalculoIndultoDt().getTempoACumprirHediondoAnos();
		}
		
		desc += "<br /><b>Requisito temporal em:</b> " + calculo.getCalculoIndultoDt().getDataRequisitoIndulto() ;
		if (calculo.getCalculoIndultoDt().getDataRequisitoIndulto().equals("-")){
			desc += " - Sentenciado não cumpriu o tempo necessário! Tempo cumprido: " + calculo.getTempoCumpridoDataAtualAnos();
		}
		
		return desc;
	}
	
	/**
	 * Consulta descrição do Fórum a partir da área de distribuição
	 * @throws Exception 
	 **/
	public String consultarDescricaoForum(String idAreaDistribuicao) throws Exception{
		
		String forum = new AreaDistribuicaoNe().consultarDescricaoForum(idAreaDistribuicao);
		
		return forum;
	}

	/**
	 * verifica se existe na listaEvento o evento informado
	 * 
	 * @param listaEvento
	 *            : lista a ser percorrida
	 * @param idEvento
	 *            : id do evento que deseja localizar na lista
	 * @return List<ProcessoEventoExecucaoDt> com os eventos encontrados.
	 */
	public List consultarEventoNaLista(List listaEvento, String idEvento){
		List listaPesquisa = new ArrayList();
		
		for (int i = 0; i < listaEvento.size(); i++) {
			ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (evento.getId_EventoExecucao().equals(idEvento)) listaPesquisa.add(evento);
		}
		
		return listaPesquisa;
	}

	/**
	 * verifica se existe na listaEvento o evento informado, no período informado
	 * 
	 * @param listaEvento
	 *            : lista a ser percorrida
	 * @param idEvento
	 *            : id do evento que deseja localizar na lista
	 * @param dataInicio
	 *            : parametro utilizado para verificar o evento com dataInicio posterior à data início informada
	 * @param dataFim
	 *            : parametro utilizado para verificar o evento com dataInicio anterior à dataFim informada
	 * @return List<ProcessoEventoExecucaoDt> com os eventos encontrados.
	 * @throws Exception 
	 */
	public List consultarEventoNaLista(List listaEvento, String idEvento, String dataInicio, String dataFim) throws Exception{
		List listaPesquisa = new ArrayList();
		
		if (listaEvento != null) {
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
				if (evento.getId_EventoExecucao().equals(idEvento)) {
					if (dataInicio.length() > 0 && dataFim.length() > 0) {
						if (!Funcoes.StringToDate(evento.getDataInicio()).before(Funcoes.StringToDate(dataInicio)) && !Funcoes.StringToDate(evento.getDataInicio()).after(Funcoes.StringToDate(dataFim))) listaPesquisa.add(evento);
					}
				}
			}
		}
		
		return listaPesquisa;
	}

	/**
	 * calcula o tempo de condenação 1/4 e 1/6 para a saída temporária
	 * 
	 * @param bean
	 * @param tempoTotalCondenacaoDias
	 */
	public void calcularTempoCondenacao_SaidaTemporaria(SaidaTemporariaDt bean, int tempoTotalCondenacaoDias) {
		int tempo16 = 0; // 1/6 do total da condenação
		int tempo14 = 0; // 1/4 do total da condenação

		tempo14 = (int) Math.ceil((double) tempoTotalCondenacaoDias * 1 / 4);
		tempo16 = (int) Math.ceil((double) tempoTotalCondenacaoDias * 1 / 6);

		bean.setTempoCondenacao14dias(String.valueOf(tempo14));
		bean.setTempoCondenacao16dias(String.valueOf(tempo16));
	}

	/**
	 * informa se existe o evento de bloqueio e não há o evento de desbloqueio respectivo, posterior ao bloqueio informado
	 * 
	 * @param idEventoBloqueio
	 * @param idEventoDesbloqueio
	 */
	public boolean verificaBloqueio(List listaEvento, int idEventoBloqueio, int idEventoDesbloqueio) {
		boolean desbloqueio = false;
		for (int i = listaEvento.size() - 1; i >= 0; i--) {
			ProcessoEventoExecucaoDt lista = (ProcessoEventoExecucaoDt) listaEvento.get(i);
			if (lista.getEventoExecucaoDt().getId().equals(String.valueOf(idEventoDesbloqueio))) desbloqueio = true;
			else if (lista.getEventoExecucaoDt().getId().equals(String.valueOf(idEventoBloqueio))) {
				if (!desbloqueio) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * seta os valores do beanSaídaTemporária.
	 * 
	 * @param processoDt
	 *            , contém os dados do processo
	 * @param listaEventos
	 *            , lista dos eventos
	 * @param CalculoLiquidacaodt
	 *            , contém os dados
	 * @return SaidaTemporariaBean
	 * @throws Exception
	 */
	public SaidaTemporariaDt montarBeanSaidaTemporaria(ProcessoDt processoDt, List listaEventos, List listaEventosSaidaTemporaria, CalculoLiquidacaoDt CalculoLiquidacaodt, String ano) throws Exception{
		SaidaTemporariaDt bean = new SaidaTemporariaDt();
		
		List listaCondenacao = this.listarCondenacoesNaoExtintas(processoDt.getId());

		bean.setAno(ano);
		bean.setIdProcesso(processoDt.getId());
		bean.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
		bean.setNomeSentenciado(((ProcessoParteDt) processoDt.getListaPolosPassivos().get(0)).getNome());
		bean.setNomeMae(((ProcessoParteDt) processoDt.getListaPolosPassivos().get(0)).getNomeMae());
		bean.setDataNascimento(((ProcessoParteDt) processoDt.getListaPolosPassivos().get(0)).getDataNascimento());
		EventoRegimeDt eventoRegimeDt = this.getUltimoEventoRegimeDt(listaEventos);
		if (eventoRegimeDt != null) bean.setRegime(this.getUltimoEventoRegimeDt(listaEventos).getRegimeExecucao());
		bean.setStatus(this.getDescricaoStatus(listaEventos));
//			bean.setListaTipoReincidencia(this.getListaTipoReincidencia(listaEventos, listaCondenacao));
		bean.setTempoTotalCondenacao(CalculoLiquidacaodt.getTempoTotalCondenacaoAnos());
		bean.setListaSaidasTemporarias(this.consultarEventoNaLista(listaEventosSaidaTemporaria, String.valueOf(EventoExecucaoDt.SAIDA_TEMPORARIA), "01/01/" + ano, "31/12/" + ano));
		bean.setTotalSaidasTemporarias(String.valueOf(bean.getListaSaidasTemporarias().size()));

		List listaReincidencia = this.getListaTipoReincidencia(listaEventos, listaCondenacao);
		if (listaReincidencia != null && listaReincidencia.size() > 0){
			for (int i=0; i<listaReincidencia.size(); i++) {
				String reinc = (String)listaReincidencia.get(i);
				if (i == 0) bean.setDescricaoReincidencia(reinc);
				else bean.setDescricaoReincidencia(bean.getDescricaoReincidencia() + ", " + reinc);
			}
		} else bean.setDescricaoReincidencia("Não");
		
		if (verificaBloqueio(listaEventosSaidaTemporaria, EventoExecucaoDt.BLOQUEIO_SAIDA_TEMPORARIA, EventoExecucaoDt.DESBLOQUEIO_SAIDA_TEMPORARIA)) bean.setBloqueioSaidas("Sim");
		else bean.setBloqueioSaidas("Não");

		if (verificaBloqueio(listaEventosSaidaTemporaria, EventoExecucaoDt.BLOQUEIO_TRABALHO_EXTERNO, EventoExecucaoDt.DESBLOQUEIO_TRABALHO_EXTERNO)) bean.setBloqueioTrabalho("Sim");
		else  bean.setBloqueioTrabalho("Não");

		if (this.isContemCrimeHediondo(listaCondenacao)) bean.setHediondo("Sim");
		else bean.setHediondo("Não");

		// seta o tempo de condenação 1/6 e 1/4
		calcularTempoCondenacao_SaidaTemporaria(bean, Funcoes.StringToInt(CalculoLiquidacaodt.getTempoTotalCondenacaoRemanescenteDias()));

		bean.setTempoCumpridoDias(CalculoLiquidacaodt.getTempoCumpridoDataAtualDias());
		bean.setDataUltimoCalculo(this.consultarDataUltimoCalculo(processoDt.getId()));
		
		return bean;
	}

	/**
	 * seta os valores do calculoLiquidadcaoDt
	 * 
	 * @param dataProvavelDt
	 * @return dataProvavelDt
	 * @throws Exception
	 */
	public CalculoLiquidacaoDt consultarCalculoLiquidacao(CalculoLiquidacaoDt calculoLiquidacaoDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			calculoLiquidacaoDt = obPersistencia.consultarCalculoLiquidacao(calculoLiquidacaoDt);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return calculoLiquidacaoDt;
	}

	/**
	 * @param dataProvavelDt
	 * @return dataProvavelDt
	 * @throws Exception
	 */
	public DataProvavelDt consultarCalculoLiquidacao(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		DataProvavelDt objRetorno = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			objRetorno = obPersistencia.consultarCalculoLiquidacao(idProcesso);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return objRetorno;
	}

	public String consultarDataUltimoCalculo(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stRetorno = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			stRetorno = obPersistencia.consultarDataUltimoCalculo(idProcesso);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	
	/**
	 * Verifica os dados obrigatórios para consulta de Data Provavel
	 * 
	 * @param dataProvavelDt
	 * @throws Exception
	 * @author kbsriccioppo
	 */
	public String VerificarConsultaDataProvavel(DataProvavelDt dados) {
		String stRetorno = "";

		if (dados.getDataInicialPeriodo() == null) stRetorno += "O campo Data Inicial do Período é obrigatório.";
		if (dados.getTipoConsulta() == null) stRetorno += "Informe o(s) parâmetro(s) da consulta .";

		return stRetorno;
	}

	/**
	 * Verifica se todos os eventos que exigem vínculo com o TJ possuem este vínculo
	 * 
	 * @param listaEventos
	 * @param idProcesso
	 * @return boolean
	 * @throws Exception
	 */
	public String verificarTodosVinculoEventoTJ(List listaEventos, String idProcesso) throws Exception{
				
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEventos) {
			if (isVinculoEvento_TJ(evento.getId_EventoExecucao()) && !evento.getConsiderarTempoLivramentoCondicional().equals("1")) {
				boolean temVinculo = false;
				List listaTJ = listarTransitoComTotalCondenacao_HashMap(idProcesso, evento.getId(), evento.getId_EventoExecucao());
				for (HashMap mapTJ : (List<HashMap>) listaTJ) {
					if (mapTJ.get("Checked").equals("1")) {
						temVinculo = true;
						break;
					}
				}
				if (!temVinculo) {
					return "Existe evento de Comutação, Revogação do LC e/ou Suspensão do LC sem vínculo com o TJ. Verifique!";
				}
			}
		}
		return "";

	}

	public String getDataInicioLivramentoCondicional(String idProcessoEventoExecucao, List listaEventos, String idProcesso, String dataEventoReferencia) throws Exception{
		for (int i = 0; i < listaEventos.size(); i++) {
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEventos.get(i);
			if (idProcessoEventoExecucao.equals(processoEventoExecucaoDt.getId())) {
				return processoEventoExecucaoDt.getDataInicio();
			}
		}
		String dataInicio = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			dataInicio = obPersistencia.consultarDataInicioEvento(idProcessoEventoExecucao, idProcesso, dataEventoReferencia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dataInicio;
	}

	public ProcessoEventoExecucaoDt getLivramentoCondicional(String idProcesso, String dataEventoReferencia) throws Exception {
		ProcessoEventoExecucaoDt evento = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			evento = obPersistencia.consultarLivramentoCondicional(idProcesso, dataEventoReferencia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return evento;
	}

	/**
	 * Consulta os dados dos crimes das Condenações através do idProcessoExecucao, INDEPENDENTE SE O idProcessoExecução É DE UMA AÇÃO PENAL ATIVA OU NÃO.
	 * 
	 * @param idProcessoExecucao
	 *            : id do processo execucao
	 * @return string
	 * @author kbsriccioppo
	 * @throws Exception 
	 */
	public List listarCrimeCondenacao(String idProcessoExecucao) throws Exception{
		List listaCondenacao = new ArrayList();

			listaCondenacao = new CondenacaoExecucaoNe().listarCrimeCondenacao(idProcessoExecucao);
		return listaCondenacao;
	}

	/**
	 * Verifica se o usuário pode modificar dados do ProcessoEventoExecucao.
	 * 
	 * @param processoDt
	 * @param usuarioDt
	 * @param idProcesso
	 * @param idMovimentacao
	 * @return mensagem
	 * @author wcsilva
	 */
	public String podeModificarDados(ProcessoDt processoDt, UsuarioDt usuarioDt){
		String stMensagem = "";

			stMensagem = new ProcessoExecucaoNe().podeModificarDados(processoDt, usuarioDt);
		return stMensagem;
	}

	/**
	 * Verifica se o usuário pode alterar o evento.
	 * 
	 * @param ProcessoEventoExecucaoDt
	 *            : evento
	 * @return mensagem: String
	 * @author wcsilva
	 */
	public String podeAlterarEvento(ProcessoEventoExecucaoDt dado) {
		String stMensagem = "";

		if (dado.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO)) || dado.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))) return "";

		if (dado.getId().length() > 0 && (isVinculoEvento_TJ(dado.getEventoExecucaoDt().getId()) || isEventoPenaRestritivaDireito(dado.getEventoExecucaoDt().getId()))) stMensagem = "Não é possível alterar o evento " + dado.getEventoExecucaoDt().getEventoExecucao() + "\n";

		return stMensagem;
	}

	/**
	 * Calcula o tempo de cada evento de comutação, de acordo com a fração e os trânsitos em julgado. 
	 * Informa o tempo total da comutação em ProcessoEventoExecucao.quantidade()
	 * 
	 * @param id_Processo
	 *            : identificação do processo
	 * @param listaEvento
	 *            : lista com todos os eventos
	 * @param listaTJ
	 *            : lista com os Tj do processo (!= null quando a lista de eventos é montada na movimentação)
	 * @return listaRetorno: contém duas listas com o objeto TransitoJulgadoEventoDt. posição 0: listaCTJ, posição 1: listaComutacao
	 * @throws Exception
	 */
	public List calcularTempoComutacao(String id_Processo, List listaEvento, List listaTJ) throws Exception{
		List listaRetorno = null;
		
		// a consulta ordenada pela data do evento comutação depois pela condenação do TJ
		List listaCTJ = new TransitoJulgadoEventoNe().listarTodosTransitoJulgadoEventoNaoExtinto(id_Processo, String.valueOf(EventoExecucaoDt.COMUTACAO_PENA), true, false); 

		if (listaCTJ != null && listaCTJ.size() > 0) {// verifica se existe comutação para este processo
			List listaComutacao = new ArrayList();

			List listaAux = null;
			if (listaTJ != null) listaAux = listaTJ; // lista de eventos montada a partir da movimentação
			else
				listaAux = listaEvento; // lista de eventos montada a partir do menu: manter eventos de execução

			// calcula o tempo de comutação para cada registro da lista de
			// comutacaoTransitoJulgado, ordenada pela data da comutacao
			// depois pelo TJ
			// ------------------------------------------------------------------------------------------------------------------------------------
			// ----------------- ALGORITMO DO CÁLCULO MANUAL DO TEMPO A COMUTAR (USUÁRIO INFORMA A QUANTIDADE DE DIAS A COMUTAR)-------------------

			String idComutacao = "";
			float multiplicador = 0;
//				new ArrayList();

			for (int i = 0; i < listaCTJ.size(); i++) {
				TransitoJulgadoEventoDt ctj = (TransitoJulgadoEventoDt) listaCTJ.get(i);

				// verifica se mudou a comutação da lista
				if (!idComutacao.equals(ctj.getId_Evento())) {// ------INICIO NOVA COMUTAÇÃO
					idComutacao = ctj.getId_Evento();

					// calcula o tempo da pena remanescente de todos os TJs para a comutação atual.
					// percorre a lista para verificar os TJ desta comutação
					for (int z = i; z < listaCTJ.size(); z++) {
						TransitoJulgadoEventoDt ctjZ = (TransitoJulgadoEventoDt) listaCTJ.get(z);

						// garante que está olhando os TJ apenas da comutação atual
						if (ctjZ.getId_Evento().equals(idComutacao)) {

							// percorre a lista da posição anterior até o início para ver se teve outra comutação para este TJ
							//(não percorre esta laço na primeira vez de cada comutação)
							for (int w = i - 1; w > -1; w--) {
								TransitoJulgadoEventoDt ctjW = (TransitoJulgadoEventoDt) listaCTJ.get(w);
//									if (ctjW.getId_TransitoJulgado().equals(ctjZ.getId_TransitoJulgado())) {
								if (ctjW.getId_CondenacaoExecucao().equals(ctjZ.getId_CondenacaoExecucao())) {

									// calcula o tempo da pena resmanescente deste TJ, olhando a comutação anterior.
									int penaRemanescente = Funcoes.StringToInt(ctjW.getTempoPenaRemanescenteTJDias()) - Funcoes.StringToInt(ctjW.getTempoComutacaoDias());
									ctjZ.setTempoPenaRemanescenteTJDias(String.valueOf(penaRemanescente));
									break;
								}
							}

						} else
							break;
					}

					// calcula o tempo total das penas remanescentes dos TJ
					// relacionados com a comutação em questão.
					int tempoTotalTJ = 0;
					for (int j = i; j < listaCTJ.size(); j++) {
						TransitoJulgadoEventoDt ctjAux = (TransitoJulgadoEventoDt) listaCTJ.get(j);

						if (idComutacao.equals(ctjAux.getId_Evento())) {
							// para a primeira comutação, o tempo de pena
							// remanescente do TJ é igual ao tempo total da
							// condenação.
							if (ctjAux.getTempoPenaRemanescenteTJDias().length() == 0) 
								ctjAux.setTempoPenaRemanescenteTJDias(ctjAux.getTempoPenaTJDias());

							tempoTotalTJ += Funcoes.StringToInt(ctjAux.getTempoPenaRemanescenteTJDias());
						} else
							break;
					}

					// calcula o valor a ser multiplicado por cada tempo de
					// pena do TJ para descobrir o tempo a comutar.
					multiplicador = (float) (Float.parseFloat(ctj.getTempoComutacaoDias()) / tempoTotalTJ);

					// monta a lista de comutação
					for (ProcessoEventoExecucaoDt pee : (List<ProcessoEventoExecucaoDt>) listaAux) {
						if (pee.getId().equals(ctj.getId_Evento())) {
							TransitoJulgadoEventoDt comutacao = new TransitoJulgadoEventoDt();
							comutacao.setId_Evento(ctj.getId_Evento());
							comutacao.setTempoComutacaoDias(pee.getQuantidade());
							comutacao.setFracao(ctj.getFracao());
							comutacao.setDataInicioEvento(ctj.getDataInicioEvento());
							listaComutacao.add(comutacao);
							break;
						}
					}

				} // ------FIM NOVA COMUTAÇÃO

				int tempoAComutar = Math.round(multiplicador * Funcoes.StringToInt(ctj.getTempoPenaRemanescenteTJDias()));
				ctj.setTempoComutacaoDias(String.valueOf(tempoAComutar));
			}

			// refaz a listaCTJ ordenando pelo TJ
			for (int i = 0; i < listaCTJ.size(); i++) {
				TransitoJulgadoEventoDt ctj = (TransitoJulgadoEventoDt) listaCTJ.get(i);
				// procura outra comutação referente ao mesmo TJ
				for (int w = i + 1; w < listaCTJ.size(); w++) {
					TransitoJulgadoEventoDt ctjW = (TransitoJulgadoEventoDt) listaCTJ.get(w);
					if (ctj.getId_CondenacaoExecucao().equalsIgnoreCase(ctjW.getId_CondenacaoExecucao())) {
						listaCTJ.remove(ctjW);
						listaCTJ.add(i + 1, ctjW);
						i++;
					}
				}
			}

			// ------------------------------------------------------------------------------------------------------------------------------------

			// ------------------------------------------------------------------------------------------------------------------------------------
			// ----------------- ALGORITMO DO CÁLCULO DINÂMICO DO TEMPO A
			// COMUTAR------------------------------------------------------------------

			// percorre a lista de evento até encontrar um TJ ou GRP
			// for (ProcessoEventoExecucaoDt pee :
			// (List<ProcessoEventoExecucaoDt>)listaAux) {
			// if(pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))
			// ||
			// pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))){
			// int tempoPena = 0;
			// //percorre a lista de comutacaoTransitoJulgado para verificar
			// se existe comutação para o TJ encontrado
			// for (ComutacaoTransitoJulgadoDt ctj :
			// (List<ComutacaoTransitoJulgadoDt>)listaCTJ) {
			// if(pee.getId().equals(ctj.getId_TransitoJulgado())){
			//
			// //calcula o tempo a comutar do TJ em questão
			// if (tempoPena == 0) tempoPena =
			// Funcoes.StringToInt(ctj.getTempoPenaTJDias());
			// int tempoComutacao = tempoPena *
			// Funcoes.StringToInt(ctj.getFracao().substring(0,1)) /
			// Funcoes.StringToInt(ctj.getFracao().substring(2));
			//
			// ctj.setTempoComutacaoDias(String.valueOf(tempoComutacao));
			// ctj.setTempoPenaRemanescenteTJDias(String.valueOf(tempoPena));
			//
			// tempoPena -= tempoComutacao; //remanescente da pena
			//
			// boolean encontrou = false;
			// //percorre a lista auxiliar de comutações (que armazena o
			// tempo total de cada comutação).
			// for (ComutacaoTransitoJulgadoDt comutacao :
			// (List<ComutacaoTransitoJulgadoDt>)listaComutacao) {
			// //verifica se a comutação já está na lista
			// if
			// (ctj.getId_Comutacao().equals(comutacao.getId_Comutacao())){
			// encontrou = true;
			// tempoComutacao +=
			// Funcoes.StringToInt(comutacao.getTempoComutacaoDias());
			// comutacao.setTempoComutacaoDias(String.valueOf(tempoComutacao));
			// }
			// }
			// if (!encontrou){//se não tiver na lista, adiciona
			// ComutacaoTransitoJulgadoDt comutacao = new
			// ComutacaoTransitoJulgadoDt();
			// comutacao.setId_Comutacao(ctj.getId_Comutacao());
			// comutacao.setTempoComutacaoDias(String.valueOf(tempoComutacao));
			// comutacao.setFracao(ctj.getFracao());
			// comutacao.setDataInicioComutacao(ctj.getDataInicioComutacao());
			// listaComutacao.add(comutacao);
			// }
			// }
			// }
			// }
			// }
			//
			// //adiciona o tempo total da comutação na lista de evento.
			// for (ProcessoEventoExecucaoDt pee :
			// (List<ProcessoEventoExecucaoDt>)listaEvento) {
			// if(pee.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){
			// for (ComutacaoTransitoJulgadoDt comutacao :
			// (List<ComutacaoTransitoJulgadoDt>)listaComutacao) {
			// if (pee.getId().equals(comutacao.getId_Comutacao())){
			// pee.setQuantidade(comutacao.getTempoComutacaoDias());
			// break;
			// }
			// }
			// }
			// }
			// ------------------------------------------------------------------------------------------------------------------------------
			// utilizado em Comutacao.jsp
			listaRetorno = new ArrayList();
			listaRetorno.add(listaCTJ);
			listaRetorno.add(listaComutacao);
		}
			
		return listaRetorno;
	}

	/**
	 * Lista as modalidades do processo execução (ação penal)
	 * 
	 * @param idProcessoExecucao
	 * @return lista de modalidades
	 * @throws Exception
	 */
	public List listarModalidadesDaAcaoPenal(String idProcessoExecucao) throws Exception {
		List lista = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarModalidadesDaAcaoPenal(idProcessoExecucao);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}

	public List listarModalidadesDaAcaoPenal(String idProcessoExecucao, FabricaConexao obFabricaConexao) throws Exception {
		List lista = new ArrayList();
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
		lista = obPersistencia.listarModalidadesDaAcaoPenal(idProcessoExecucao);

		return lista;
	}

	public List listarModalidadesDoEvento(String idProcessoEventoExecucao) throws Exception {
		List lista = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarModalidadesDoEvento(idProcessoEventoExecucao);		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Consulta o Id_Movimentação do evento
	 * 
	 * @param idEventoExecucao
	 *            : id do evento que contém a movimentação
	 * @param idProcessoExecucao
	 *            : id do processoExecucao que do evento que contém a movimentação
	 * @return String idMovimentacao
	 * @throws Exception
	 */
	public String consultarIdMovimentacao(String idEventoExecucao, String idProcessoExecucao) throws Exception {
		String retorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarIdMovimentacao(idEventoExecucao, idProcessoExecucao);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}

	public String consultarIdMovimentacao(String idEventoExecucao, String idProcessoExecucao, FabricaConexao obFabricaConexao) throws Exception {
		String retorno = "";
		
		ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.consultarIdMovimentacao(idEventoExecucao, idProcessoExecucao);

		return retorno;
	}

	public String calcularPenaRestritiva(List listaEvento, List listaEventoPSC, List listaEventoLFS, List listaEventoPEC, List listaEventoITD, List listaEventoPCB, CalculoLiquidacaoDt calculoLiquidacao) throws Exception{
		String msg = "";
		List listaIdModalidadesCalculo = new ArrayList();
		
		//verifica as modalidades para cálculo
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
		    if(Integer.parseInt(evento.getId_EventoExecucao()) == EventoExecucaoDt.ALTERACAO_MODALIDADE)
			listaIdModalidadesCalculo.clear();
		    if (isModalidade(evento.getId_EventoExecucao())){
			boolean jaInseriu = false;
			for (String idModalidade : (List<String>)listaIdModalidadesCalculo) {
			    if (evento.getId_EventoExecucao().equals(idModalidade))
				jaInseriu = true;
			}
			if (!jaInseriu) listaIdModalidadesCalculo.add(evento.getId_EventoExecucao());
		    }
		}
		
		if (listaIdModalidadesCalculo.size() == 0){
			return "Não foi possível realizar o cálculo! (Motivo: Não foi localizada nenhuma modalidade.)";
			
		} else {
			for (String idModalidade : (List<String>)listaIdModalidadesCalculo) {
				switch (Integer.parseInt(idModalidade)){
					case EventoExecucaoDt.PERDA_BENS_VALORES:
						break;
					case EventoExecucaoDt.PRESTACAO_PECUNIARIA:
						calcularPEC(listaEvento, listaEventoPEC, calculoLiquidacao);
						break;
					case EventoExecucaoDt.CESTA_BASICA:
						calcularPCB(listaEvento, listaEventoPCB, calculoLiquidacao);
						break;
					case EventoExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE:
						calcularPSC(listaEvento, listaEventoPSC, calculoLiquidacao);
						break;
					case EventoExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS:
						calcularITD(listaEvento, listaEventoITD, calculoLiquidacao);
						break;
					case EventoExecucaoDt.LIMITACAO_FIM_SEMANA:
						calcularLFS(listaEvento, listaEventoLFS, calculoLiquidacao);
						break;
				}
			}
		}
		return msg;
		
	}

	
	public void calcularPSC(List listaEvento, List listaEventoPSC, CalculoLiquidacaoDt calculoLiquidacao){
		int horasPSC = 0; // horas cumpridas de "prestação de serviço à comunidade"
		int totalCondenacaoPSC = 0;
		
		// calcula o tempo total de condenação para cada modalidade
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE))) {
				totalCondenacaoPSC += Funcoes.StringToInt(evento.getQuantidade());
			}
		}
		// soma as horas cumpridas de PSC
		if (listaEventoPSC != null) {
			for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEventoPSC) {
				if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.HORAS_CUMPRIDAS_PSC))) {
					if (evento.getQuantidade().length() > 0) horasPSC += Funcoes.StringToInt(evento.getQuantidade());
				}
			}
		}

		// nas horas cumpridas PSC, deve considerar todo o tempo cumprido da lista de evento principal
		horasPSC += Funcoes.StringToInt(calculoLiquidacao.getTempoCumpridoUltimoEventoDias());
		calculoLiquidacao.getCalculoPenaRestritivaDt().setHorasCumpridasPSC(String.valueOf(horasPSC));
		calculoLiquidacao.getCalculoPenaRestritivaDt().setHorasRestantePSC(String.valueOf(totalCondenacaoPSC - horasPSC));
		calculoLiquidacao.getCalculoPenaRestritivaDt().setDataInicioPSC(getDataInicio(listaEvento, String.valueOf(EventoExecucaoDt.INICIO_PSC)));
		calculoLiquidacao.getCalculoPenaRestritivaDt().setTotalCondenacaoPSCHoras(String.valueOf(totalCondenacaoPSC));

	}
	
	public void calcularLFS(List listaEvento, List listaEventoLFS, CalculoLiquidacaoDt calculoLiquidacao) throws Exception{
		int totalCondenacaoLFS = 0;
		String msgNaoIniciouLFS = "Não é possível realizar o cálculo! (Motivo: Não foi inserido o evento \"Início da LFS\".)";;
		
		boolean isIniciouLFS = false;
		// calcula o tempo total de condenação para cada modalidade
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.LIMITACAO_FIM_SEMANA))) {
				totalCondenacaoLFS += Funcoes.StringToInt(evento.getQuantidade());
			}
		}
		
		calculoLiquidacao.getCalculoPenaRestritivaDt().setTotalCondenacaoLFSDias(String.valueOf(totalCondenacaoLFS));
		
		// CÁLCULO PARA LIMITAÇÃO DE FIM DE SEMANA
		if (listaEventoLFS != null && listaEventoLFS.size() > 0) {
			for (ProcessoEventoExecucaoDt eventoLFS : (List<ProcessoEventoExecucaoDt>) listaEventoLFS) {
				if (eventoLFS.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INICIO_LFS))) {
					isIniciouLFS = true;
				}
			}
			//verifica se iniciou o cumprimento da LFS
			if (!isIniciouLFS){
				calculoLiquidacao.getCalculoPenaRestritivaDt().setObservacaoCalculoLFS(msgNaoIniciouLFS);
				return;
			
			// verifica se a pena está interrompida
			} else if (((ProcessoEventoExecucaoDt) listaEventoLFS.get(listaEventoLFS.size() - 1)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INTERRUPCAO_CUMPRIMENTO_LFS)) 
					|| ((ProcessoEventoExecucaoDt) listaEventoLFS.get(listaEventoLFS.size() - 1)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.FIM_LFS))) {
				calculoLiquidacao.getCalculoPenaRestritivaDt().setObservacaoCalculoLFS("Não é possível realizar o cálculo! (Motivo: Cumprimento da LFS interrompido.)");
				return;

			} else {
				// captura o valor antes de ser substituído
				int tempoCumpridoUltimoEventoListaPrincipal = 0;
				if (calculoLiquidacao.getTempoCumpridoUltimoEventoDias().length() > 0) 
					tempoCumpridoUltimoEventoListaPrincipal = Funcoes.StringToInt(calculoLiquidacao.getTempoCumpridoUltimoEventoDias());

				// o cálculo do tempo cumprido até ultimo evento da lista de evento principal já foi feito anteriormente:
				// - para calcular o tempo cumrpido da LFS, tenho que somar o tempo cumprido da lista de evento principal (até o último evento) + o tempo cumprido da lista de LFS (até a data atual)
				calcularRestantePena_TempoCumprido_TerminoPena(listaEventoLFS, calculoLiquidacao);

				// soma o tempo cumpridoAteUltimoEvento da lista de Eventos + tempoCumpridoDataAtual da listaEventoLFS
				// (que foi a que eu passei pra fazer este cálculo)
				int tempoCumpridoDataAtual = Funcoes.StringToInt(calculoLiquidacao.getTempoCumpridoDataAtualDias()) + tempoCumpridoUltimoEventoListaPrincipal;
				int restantePenaDataAtual = Funcoes.StringToInt(calculoLiquidacao.getCalculoPenaRestritivaDt().getTotalCondenacaoLFSDias()) - tempoCumpridoDataAtual;
				String dataTerminoPena = Funcoes.somaData(Funcoes.dateToStringSoData(new Date()), restantePenaDataAtual);

				calculoLiquidacao.getCalculoPenaRestritivaDt().setTempoCumpridoLFSDias(String.valueOf(tempoCumpridoDataAtual));
				calculoLiquidacao.getCalculoPenaRestritivaDt().setTempoRestanteLFSDias(String.valueOf(restantePenaDataAtual));
				calculoLiquidacao.getCalculoPenaRestritivaDt().setDataTerminoLFS(dataTerminoPena);

				calculoLiquidacao.getCalculoPenaRestritivaDt().setTempoInterrupcaoLFSDias(calcularTempoInterrupcaoTotal(listaEventoLFS));
				calculoLiquidacao.setTempoCumpridoUltimoEventoDias(String.valueOf(tempoCumpridoUltimoEventoListaPrincipal));
			}
		} else {
			calculoLiquidacao.getCalculoPenaRestritivaDt().setObservacaoCalculoLFS(msgNaoIniciouLFS);
			return;
		}
				
	}
	
	public void calcularPCB(List listaEvento, List listaEventoPCB, CalculoLiquidacaoDt calculoLiquidacao){
		long valorPagoPcb = 0;
		long totalCondenacaoPcb = 0;
		
		// calcula o total da condenação para cada modalidade
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CESTA_BASICA))) {
				totalCondenacaoPcb += Funcoes.StringToInt(evento.getQuantidade());
			}
		}
		// soma as cestas pagas
		if (listaEventoPCB != null) {
			for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEventoPCB) {
				if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PAGAMENTO_CESTA_BASICA))) {
					if (evento.getQuantidade().length() > 0) 
						valorPagoPcb += Funcoes.StringToInt(evento.getQuantidade());
				}
			}
		}

		calculoLiquidacao.getCalculoPenaRestritivaDt().setTotalPagamentoPCB(String.valueOf(totalCondenacaoPcb));
		calculoLiquidacao.getCalculoPenaRestritivaDt().setQtdPagaPCB(String.valueOf(valorPagoPcb));
		calculoLiquidacao.getCalculoPenaRestritivaDt().setQtdDevidaPCB(String.valueOf(totalCondenacaoPcb - valorPagoPcb));

	}
	
	public void calcularPEC(List listaEvento, List listaEventoPEC, CalculoLiquidacaoDt calculoLiquidacao){
		double valorPagoPec = 0;
		double totalCondenacaoPec = 0;
		
		// calcula o tempo total de condenação para cada modalidade
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRESTACAO_PECUNIARIA))) {
				totalCondenacaoPec += Funcoes.StringToDouble(evento.getQuantidade().replace(",", "."));
			}
		}
		// soma as horas cumpridas de PSC
		if (listaEventoPEC != null) {
			for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEventoPEC) {
				if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PAGAMENTO_PEC))) {
					if (evento.getQuantidade().length() > 0) 
						valorPagoPec += Funcoes.StringToDouble(evento.getQuantidade().replace(",", "."));
				}
			}
		}

		calculoLiquidacao.getCalculoPenaRestritivaDt().setTotalPagamentoPEC(Funcoes.FormatarMoeda(String.valueOf(totalCondenacaoPec)));
		calculoLiquidacao.getCalculoPenaRestritivaDt().setValorPagoPEC(Funcoes.FormatarMoeda(String.valueOf(valorPagoPec)));
		calculoLiquidacao.getCalculoPenaRestritivaDt().setValorDevidoPEC(Funcoes.FormatarMoeda(String.valueOf(totalCondenacaoPec - valorPagoPec)));
		
	}
	
	public void calcularITD(List listaEvento, List listaEventoITD, CalculoLiquidacaoDt calculoLiquidacao) throws Exception{
		int totalCondenacaoITD = 0;
		String msgNaoIniciouITD = "Não é possível realizar o cálculo! (Motivo: Não foi inserido o evento \"Início da ITD\".)";;
		
		boolean isIniciouITD = false;
		// calcula o tempo total de condenação para cada modalidade
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS))) {
				totalCondenacaoITD += Funcoes.StringToInt(evento.getQuantidade());
			}
		}
		
		calculoLiquidacao.getCalculoPenaRestritivaDt().setTotalCondenacaoITDDias(String.valueOf(totalCondenacaoITD));
		
		// CÁLCULO
		if (listaEventoITD != null && listaEventoITD.size() > 0) {
			for (ProcessoEventoExecucaoDt eventoITD : (List<ProcessoEventoExecucaoDt>) listaEventoITD) {
				if (eventoITD.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.INICIO_ITD))) {
					isIniciouITD = true;
				}
			}
			//verifica se iniciou o cumprimento da ITD
			if (!isIniciouITD){
				calculoLiquidacao.getCalculoPenaRestritivaDt().setObservacaoCalculoITD(msgNaoIniciouITD);
				return;
			
			// verifica se a pena está interrompida
			} else if (((ProcessoEventoExecucaoDt) listaEventoITD.get(listaEventoITD.size() - 1)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.FIM_ITD))) {
				calculoLiquidacao.getCalculoPenaRestritivaDt().setObservacaoCalculoLFS("Não é possível realizar o cálculo! (Motivo: Cumprimento da ITD interrompido.)");
				return;

			} else {
				// captura o valor antes de ser substituído
				int tempoCumpridoUltimoEventoListaPrincipal = 0;
				if (calculoLiquidacao.getTempoCumpridoUltimoEventoDias().length() > 0) 
					tempoCumpridoUltimoEventoListaPrincipal = Funcoes.StringToInt(calculoLiquidacao.getTempoCumpridoUltimoEventoDias());

				// o cálculo do tempo cumprido até ultimo evento da lista de evento principal já foi feito anteriormente:
				// - para calcular o tempo cumrpido da ITD, tenho que somar o tempo cumprido da lista de evento principal (até o último evento) + o tempo cumprido da lista de ITD (até a data atual)
				calcularRestantePena_TempoCumprido_TerminoPena(listaEventoITD, calculoLiquidacao);

				// soma o tempo cumpridoAteUltimoEvento da lista de Eventos + tempoCumpridoDataAtual da listaEventoITD
				// (que foi a que eu passei pra fazer este cálculo)
				int tempoCumpridoDataAtual = Funcoes.StringToInt(calculoLiquidacao.getTempoCumpridoDataAtualDias()) + tempoCumpridoUltimoEventoListaPrincipal;
				int restantePenaDataAtual = Funcoes.StringToInt(calculoLiquidacao.getCalculoPenaRestritivaDt().getTotalCondenacaoITDDias()) - tempoCumpridoDataAtual;
				String dataTerminoPena = Funcoes.somaData(Funcoes.dateToStringSoData(new Date()), restantePenaDataAtual);

				calculoLiquidacao.getCalculoPenaRestritivaDt().setTempoCumpridoITDDias(String.valueOf(tempoCumpridoDataAtual));
				calculoLiquidacao.getCalculoPenaRestritivaDt().setTempoRestanteITDDias(String.valueOf(restantePenaDataAtual));
				calculoLiquidacao.getCalculoPenaRestritivaDt().setDataTerminoITD(dataTerminoPena);

//					calculoLiquidacao.getCalculoPenaRestritivaDt().setTempoInterrupcaoITDDias(calcularTempoInterrupcaoTotal(listaEventoITD));
				calculoLiquidacao.setTempoCumpridoUltimoEventoDias(String.valueOf(tempoCumpridoUltimoEventoListaPrincipal));
			}
		} else {
			calculoLiquidacao.getCalculoPenaRestritivaDt().setObservacaoCalculoITD(msgNaoIniciouITD);
			return;
		}
				
		
	}
	
	public String calcularSursis(List listaEvento, CalculoLiquidacaoDt calculoLiquidacao) throws Exception{
		String msg = "";
		boolean isTemSursis = false;		
			
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (evento.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS))) {
				isTemSursis = true;
				int qtde = 0;
				if (evento.getDataInicioSursis().length() == 0){
					msg = "Informe a data início de cumprimento do SURSIS (TJ em " + evento.getDataInicio() + ")";
				} else {
					if (evento.getQuantidade().length() > 0) qtde = Integer.parseInt(evento.getQuantidade());
					String dataTermino = Funcoes.somaData(evento.getDataInicioSursis(), qtde);
					
					CalculoLiquidacaoSursisDt sursisDt = new CalculoLiquidacaoSursisDt();
					sursisDt.setDataInicio(evento.getDataInicioSursis());
					sursisDt.setDataProvavelTermino(dataTermino);
					sursisDt.setTempoSursisDias(String.valueOf(qtde));
					
					calculoLiquidacao.addListaSursis(sursisDt);	
				}
			}
		}
		if (!isTemSursis){
			return "Não foi encontrado o evento \"Concessão SURSIS\"";
		}

		return msg;
	}

	private String getDataInicio(List listaEvento, String idEventoExecucao) {
		for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
			if (evento.getId_EventoExecucao().equals(idEventoExecucao)) return evento.getDataInicio();
		}
		return "";
	}

	public List getListaEventoSaidaTemporaria(List listaEvento) {
		List listaEventoSaidaTemporaria = new ArrayList();

		if (listaEvento != null) {
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
				if (isEventoSaidaTemporaria(evento.getEventoExecucaoDt().getId())) {
					listaEventoSaidaTemporaria.add(evento);
				}
			}
		}

		return listaEventoSaidaTemporaria;
	}

	/**
	 * Relação dos eventos que são listados apenas na consulta de saída temporária
	 * 
	 * @param idEventoExecucao
	 *            : id do evento para verificar se ele consta na lista de eventos do método
	 * @return boolean.
	 */
	public boolean isEventoSaidaTemporaria(String idEventoExecucao) {
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.SAIDA_TEMPORARIA)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.BLOQUEIO_SAIDA_TEMPORARIA)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.BLOQUEIO_TRABALHO_EXTERNO)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.DESBLOQUEIO_SAIDA_TEMPORARIA)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.DESBLOQUEIO_TRABALHO_EXTERNO))) 
			return true;
		else
			return false;
	}

	/**
	 * Lista os registros de "parametro das comutações". Objeto ParametroComutacaoExecucaoDt
	 * 
	 * @return list: lista com os parâmetros
	 * @throws Exception
	 */
	public List listarParametroComutacaoExecucao() throws Exception{
		List lista = null;

			lista = new ParametroComutacaoExecucaoNe().listarParametroComutacaoExecucao();
		return lista;
	}

	// consulta dados da ação penal via ajax
	public String consultarIdJSON(String idProcessoexecucao) throws Exception{
		String retorno = null;

			retorno = new ProcessoExecucaoNe().consultarIdJSON(idProcessoexecucao);
		return retorno;
	}

	/**
	 * Monta a lista de eventos com os dados dos eventos, dados das condenações e dados do ProcessoExecução
	 * 
	 * @param id_processo
	 *            : identificador do processo
	 * @param usuarioDt
	 *            : usuário da sessão
	 * @param calculoLiquidacaoDt
	 *            : objeto de cálculo de liquidação de pena
	 * @param listaCondenacaoExtinta
	 *            : lista de condenação extinta
	 * @return map com as listas de evento:
	 * @throws Exception
	 */
	public HashMap montarListaEventosCompleta(String id_processo, UsuarioDt usuarioDt, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoExtinta) throws Exception {
		HashMap maplista_Evento_Historico = null;

		try{
			if (listaCondenacaoExtinta != null && listaCondenacaoExtinta.size() > 0) listaCondenacaoExtinta.removeAll(listaCondenacaoExtinta);
			List listaEvento = this.listarEventos(id_processo, "");

			// separa a lista de eventos e a lista de histórico de cumprimento
			// de pena restritiva de direito (armazena no HashMap)
			maplista_Evento_Historico = this.separarLista_Evento_HistoricoPRD(listaEvento, usuarioDt);

			listaEvento = (List) maplista_Evento_Historico.get("listaEventos");
			List listaHistoricoPsc = (List) maplista_Evento_Historico.get("listaHistoricoPsc");
			List listaHistoricoLfs = (List) maplista_Evento_Historico.get("listaHistoricoLfs");
			List listaHistoricoPec = (List) maplista_Evento_Historico.get("listaHistoricoPec");
			List listaHistoricoItd = (List) maplista_Evento_Historico.get("listaHistoricoItd");
			List listaHistoricoPcb = (List) maplista_Evento_Historico.get("listaHistoricoPcb");
			
			List listaEventosSaidaTemporaria = new ArrayList();
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
				if (this.isEventoSaidaTemporaria(evento.getEventoExecucaoDt().getId())) {
					listaEventosSaidaTemporaria.add(evento);
					listaEvento.remove(evento);
					i--;
				}
			}
			maplista_Evento_Historico.put("listaEventosSaidaTemporaria", listaEventosSaidaTemporaria);

			this.calcularDataFim(listaEvento);
			// preenche a lista de condenações extintas
			this.montarListaEventos(listaEvento, usuarioDt, listaCondenacaoExtinta, id_processo, Funcoes.StringToInt(calculoLiquidacaoDt.getQtdeTempoHorasEstudo())); 

			// volto a lista de eventos montada para o map
			maplista_Evento_Historico.put("listaEventos", listaEvento);

			// zera o objeto com os dados do cálculo
			if (calculoLiquidacaoDt.getListaTipoCalculo() != null) calculoLiquidacaoDt.limpar();

			// calcula liquidação de pena (apenas com os dados gerais do
			// cálculo)
			this.calcularLiquidacaoPena(listaEvento, listaHistoricoPsc, listaHistoricoLfs, listaHistoricoPec, listaHistoricoItd, listaHistoricoPcb, calculoLiquidacaoDt, "", id_processo);
		} catch(MensagemException m) {
			throw new MensagemException(m.getMessage());
		
		}
		return maplista_Evento_Historico;
	}

	/**
	 * copia os todos os dados do objeto: EventoExecucao, EventoLocal, EventoRegime
	 * 
	 * @param processoEventoExecucaoDt
	 *            : objeto a ser copiado
	 * @return
	 */
	public ProcessoEventoExecucaoDt copiar(ProcessoEventoExecucaoDt processoEventoExecucaoDt) {
		ProcessoEventoExecucaoDt copia = new ProcessoEventoExecucaoDt();
		copia.copiar(processoEventoExecucaoDt);
		copia.setId_Movimentacao(processoEventoExecucaoDt.getId_Movimentacao());
		copia.setMovimentacaoDataRealizacaoTipo(processoEventoExecucaoDt.getMovimentacaoDataRealizacaoTipo());
		copia.getEventoExecucaoDt().copiar(processoEventoExecucaoDt.getEventoExecucaoDt());
		copia.getEventoLocalDt().copiar(processoEventoExecucaoDt.getEventoLocalDt());
		copia.getEventoRegimeDt().copiar(processoEventoExecucaoDt.getEventoRegimeDt());
		return copia;
	}

	/**
	 * verifica se o evento informado é algum das opções do método
	 * 
	 * @param idEventoExecucao
	 * @return
	 */
	public boolean isVinculoEvento_TJ(String idEventoExecucao) {
		if (idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL)) 
				|| idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO)))
			return true;
		else
			return false;
	}

	/**
	 * Salva o cálculo de liquidação
	 * 
	 * @param calculoLiquidacaodt
	 * @throws Exception
	 * @author wcsilva
	 */
	public void salvarCalculoLiquidacao(DataProvavelDt dados, CalculoLiquidacaoDt calculoLiquidacaoDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());

			String idProcesso = dados.getIdProcesso();

			// verifica os dados a serem salvos (para não sobrescrever as datas prováveis de PR, LC e Validade do mandado de prisão)
			boolean salvarDadosBeneficios = false;

			for (String tipoCalculo : (ArrayList<String>) calculoLiquidacaoDt.getListaTipoCalculo()) {
				if (tipoCalculo.equalsIgnoreCase("PROGRESSAO")) {
					dados.setDataProvavelLivramento(calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataRequisitoTemporalLivramento());
					dados.setDataProvavelProgressao(calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().getDataRequisitoTemporalProgressao());
					dados.setDataProvavelTerminoPena(calculoLiquidacaoDt.getDataTerminoPena());
					dados.setDataValidadeMandadoPrisao("");
					dados.setDataCalculo(Funcoes.dateToStringSoData(new Date()));
					salvarDadosBeneficios = true;

				} else if (tipoCalculo.equalsIgnoreCase("COMUTACAO") 
						|| tipoCalculo.equalsIgnoreCase("COMUTACAO_UNIFICADA") 
						|| tipoCalculo.equalsIgnoreCase("INDULTO") 
						|| tipoCalculo.equalsIgnoreCase("PRESCRICAO_PUNITIVA")
						|| tipoCalculo.equalsIgnoreCase("OUTRAPENA")) {
					salvarDadosBeneficios = false;

				} else if ((tipoCalculo.equalsIgnoreCase("PRESCRICAO_EXECUTORIA_IND") 
						|| tipoCalculo.equalsIgnoreCase("PRESCRICAO_EXECUTORIA_UNI")) 
//						&& calculoLiquidacaoDt.getDataValidadeMandadoPrisaoUnificada().length() > 0
						) {
					if (calculoLiquidacaoDt.getDataValidadeMandadoPrisaoIndividual().equalsIgnoreCase("--")){
						calculoLiquidacaoDt.setDataValidadeMandadoPrisaoIndividual("");
					}
					if (dados.getDataValidadeMandadoPrisao().equalsIgnoreCase("--")){
						dados.setDataValidadeMandadoPrisao("");
					}
						
					if (calculoLiquidacaoDt.getDataValidadeMandadoPrisaoIndividual().length() > 0)
						dados.setDataValidadeMandadoPrisao(calculoLiquidacaoDt.getDataValidadeMandadoPrisaoIndividual());
					else dados.setDataValidadeMandadoPrisao(calculoLiquidacaoDt.getDataValidadeMandadoPrisaoUnificada());
					dados.setDataCalculo(Funcoes.dateToStringSoData(new Date()));
					dados.setDataProvavelLivramento("");
					dados.setDataProvavelProgressao("");
					if (calculoLiquidacaoDt.getDataTerminoPena().length() > 2){
						dados.setDataProvavelTerminoPena(calculoLiquidacaoDt.getDataTerminoPena());
					} else dados.setDataProvavelTerminoPena("");
					salvarDadosBeneficios = true;

				} else if (tipoCalculo.equalsIgnoreCase("PENARESTRITIVA")) {
					dados.setDataCalculo(Funcoes.dateToStringSoData(new Date()));
					dados.setDataProvavelLivramento("");
					dados.setDataProvavelProgressao("");
					dados.setDataProvavelTerminoPena("");
					dados.setHoraRestantePSC(calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getHorasRestantePSC());
					dados.setDataTerminoLFS(calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getDataTerminoLFS());
					dados.setDataTerminoITD(calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getDataTerminoITD());
					dados.setValorDevidoPEC(calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getValorDevidoPEC());
					dados.setQtdDevidaPCB(calculoLiquidacaoDt.getCalculoPenaRestritivaDt().getQtdDevidaPCB());
					salvarDadosBeneficios = true;
					
				} else if (tipoCalculo.equalsIgnoreCase("SURSIS")) {
					dados.setDataCalculo(Funcoes.dateToStringSoData(new Date()));
					String terminoSursis = "";
					for (int i=0; i<calculoLiquidacaoDt.getListaSursis().size(); i++){
						String data = ((CalculoLiquidacaoSursisDt) calculoLiquidacaoDt.getListaSursis().get(i)).getDataProvavelTermino();
						terminoSursis += data;
						if (i != calculoLiquidacaoDt.getListaSursis().size() -1)
							terminoSursis += ", ";
					}
					dados.setTerminoSURSIS(terminoSursis);
					salvarDadosBeneficios = false;
				}
			}

			
			//--------se o cálculo foi feito no processo principal-----------------
			// salva os dados do cálculo no processo principal
			obPersistencia.excluirCalculo(idProcesso); //exclui os cálculos do processo
			obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios); //inclui o cálculo

			//salva os dados do cálculo no processo apenso (se houver)
			if (dados.isTemProcessoApenso()) {
				List listaProcessosApensos = new ProcessoNe().consultarProcessosApensosEDependentes(idProcesso);
				for (ProcessoDt proc : (List<ProcessoDt>) listaProcessosApensos) {
					if (!proc.getId_Processo().equals(idProcesso)) {
						// estou excluindo para garantir que não duplique o registro de cálculo para cada processo
						obPersistencia.excluirCalculo(proc.getId_Processo()); //exclui os cálculos do processo apenso
						dados.setIdProcesso(proc.getId_Processo()); 
						obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios); //inclui o cálculo no processo apenso
						dados.setIdProcesso(idProcesso);
					}
				}
			}
			//--------se o cálculo for feito no processo apenso....--------------------
			if (dados.getIdProcessoPrincipal().length() > 0 && !dados.getIdProcessoPrincipal().equals(idProcesso)) {
				dados.setIdProcesso(dados.getIdProcessoPrincipal());
				// estou excluindo para garantir que não duplique o registro de cálculo para cada processo
				obPersistencia.excluirCalculo(dados.getIdProcesso());  //exclui o cálculo do processo principal
				obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios); //inclui o cálculo no processo principal
				dados.setIdProcesso(idProcesso);
			}

			obLogDt = new LogDt("Cálculo de Liquidação de Penas", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", "");
			
			
			
//			// inclui o cálculo
//			if (dados.getId().length() == 0) {
//
//				// salva os dados do processo principal e apensos (se houver)
//				obPersistencia.excluirCalculo(idProcesso); 
//				obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios);
//
//				if (dados.isTemProcessoApenso()) {
//					List listaProcessosApensos = new ProcessoNe().consultarProcessosApensos(idProcesso);
//					for (ProcessoDt proc : (List<ProcessoDt>) listaProcessosApensos) {
//						if (!proc.getId_Processo().equals(idProcesso)) {
//							DataProvavelDt temp = consultarCalculoLiquidacao(proc.getId_Processo());
//							dados.setIdProcesso(proc.getId_Processo());
//							// ALTERA O CÁLCULO
//							if (temp.getIdCalculo().length() > 0) {
//								obPersistencia.alterarCalculoLiquidacao(dados, salvarDadosBeneficios);
//							// INCLUI O CÁLCULO
//							} else {
//								// estou excluindo para garantir que não duplique o registro de cálculo para cada processo
//								obPersistencia.excluirCalculo(proc.getId_Processo()); 
//								obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios);
//							}
//							dados.setIdProcesso(idProcesso);
//						}
//					}
//				}
//				if (dados.getIdProcessoPrincipal().length() > 0 && !dados.getIdProcessoPrincipal().equals(idProcesso)) {
//					dados.setIdProcesso(dados.getIdProcessoPrincipal());
//					DataProvavelDt temp = consultarCalculoLiquidacao(dados.getIdProcessoPrincipal());
//					if (temp.getIdCalculo().length() > 0) obPersistencia.alterarCalculoLiquidacao(dados, salvarDadosBeneficios);
//					else {
//						// estou excluindo para garantir que não duplique o registro de cálculo para cada processo
//						obPersistencia.excluirCalculo(dados.getIdProcesso()); 
//						obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios);
//					}
//					dados.setIdProcesso(idProcesso);
//				}
//
//				obLogDt = new LogDt("Cálculo de Liquidação de Penas", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", "");
//
//				// altera o cálculo
//			} else {
//				obPersistencia.alterarCalculoLiquidacao(dados, salvarDadosBeneficios);
//
//				if (dados.isTemProcessoApenso()) {
//					List listaProcessosApensos = new ProcessoNe().consultarProcessosApensos(idProcesso);
//					for (ProcessoDt proc : (List<ProcessoDt>) listaProcessosApensos) {
//						if (!proc.getId_Processo().equals(idProcesso)) {
//							DataProvavelDt temp = consultarCalculoLiquidacao(proc.getId_Processo());
//							dados.setIdProcesso(proc.getId_Processo());
//							// ALTERA O CÁLCULO
//							if (temp.getIdCalculo().length() > 0) {
//								obPersistencia.alterarCalculoLiquidacao(dados, salvarDadosBeneficios);
//							// INCLUI O CÁLCULO
//							} else {
//								// estou excluindo para garantir que não duplique o registro de cálculo para cada processo
//								obPersistencia.excluirCalculo(proc.getId_Processo()); 
//								obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios);
//							}
//							dados.setIdProcesso(idProcesso);
//						}
//					}
//				}
//				if (dados.getIdProcessoPrincipal().length() > 0 && !dados.getIdProcessoPrincipal().equals(idProcesso)) {
//					dados.setIdProcesso(dados.getIdProcessoPrincipal());
//					DataProvavelDt temp = consultarCalculoLiquidacao(dados.getIdProcessoPrincipal());
//					if (temp.getIdCalculo().length() > 0) obPersistencia.alterarCalculoLiquidacao(dados, salvarDadosBeneficios);
//					else {
//						// estou excluindo para garantir que não duplique o registro de cálculo para cada processo
//						obPersistencia.excluirCalculo(dados.getIdProcesso()); 
//						obPersistencia.inserirCalculoLiquidacao(dados, salvarDadosBeneficios);
//					}
//					dados.setIdProcesso(idProcesso);
//				}
//
//				obLogDt = new LogDt("Cálculo de Liquidação de Penas", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), "");
//			}
			dados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void salvarDataHomologacaoCalculoLiquidacao(DataProvavelDt dados, boolean salvarTodosDados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoEventoExecucaoPs obPersistencia = new  ProcessoEventoExecucaoPs(obFabricaConexao.getConexao());

			obPersistencia.salvarDataHomologacaoCalculoLiquidacao(dados);
			obLogDt = new LogDt("Cálculo de Liquidação de Penas", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", "");
			dados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public String verificarSituacaoAtualExecucao(SituacaoAtualExecucaoDt dados){
		String msg = "";
		
		if (dados.getIdEventoExecucaoStatus().length() == 0) msg += "Informe a situação.";
		if (dados.getIdLocalCumprimentoPena().length() == 0) msg += "\nInforme o local de cumprimento de pena.";
//			if (dados.getIdRegime().length() == 0) msg += "\nInforme o regime.";
		if (dados.getIdFormaCumprimento().length() == 0) msg += "\nInforme a forma de cumprimento da pena.";
//			if (dados.getListaSituacaoAtualTipoPenaDt() == null || dados.getListaSituacaoAtualTipoPenaDt().size() == 0) msg += "\nInforme o tipo de pena.";
		
		return msg;
	}
	
	public void salvarSituacaoAtualExecucao(SituacaoAtualExecucaoDt dados) throws Exception{

			new SituacaoAtualExecucaoNe().salvar(dados);
	}
	
	public ProcessoExecucaoDt consultarAcaoPenalComCondenacao(String idProcessoExecucao) throws Exception{
		ProcessoExecucaoDt processoExecucaoDt = null;

			processoExecucaoDt = new ProcessoExecucaoNe().consultarAcaoPenalComCondenacao(idProcessoExecucao);
		return processoExecucaoDt;
	}

	public List consultarIdsPenaExecucaoTipo(String id_opcoes) throws Exception{
		List tempList = null;

			tempList = new ProcessoExecucaoNe().consultarIdsPenaExecucaoTipo(id_opcoes);
		return tempList;
	}

	public List consultarDescricaoRegimeExecucao(String id_PenaExecucaoTipo) throws Exception{
		List tempList = null;

			tempList = new ProcessoExecucaoNe().consultarDescricaoRegimeExecucao(id_PenaExecucaoTipo);
		return tempList;
	}

	public List consultarDescricaoCidade(String tempNomeBusca, String uf, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		CidadeNe neObjeto = new CidadeNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, uf, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	public String consultarDescricaoCidadeJSON(String tempNomeBusca, String uf, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		CidadeNe Naturalidadene = new CidadeNe();
		stTemp = Naturalidadene.consultarDescricaoJSON(tempNomeBusca, uf, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarEventoExecucaoJSON(String tempNomeBusca, String idEventoExecucao, String PosicaoPaginaAtual ) throws Exception {
		String stTemp ="";
		List tempList = new ArrayList();
		EventoExecucaoNe eventoExecucaoNe = new EventoExecucaoNe();
		
		if (idEventoExecucao != null && idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))) {
			//edição do evento Trânsito em Julgado: altera somente para Guia de Recolhimento Provisória
			stTemp = eventoExecucaoNe.consultarIdJSON(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA));
		} else if (idEventoExecucao != null && idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))) {
			//edição do evento Guia de Recolhimento Provisória: altera somente para Trânsito em Julgado
			stTemp = eventoExecucaoNe.consultarIdJSON(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO));
		} else if (idEventoExecucao != null && idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))) {
			stTemp = eventoExecucaoNe.consultarIdJSON(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL));
		} else if (idEventoExecucao != null && idEventoExecucao.equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL)))
			stTemp = eventoExecucaoNe.consultarIdJSON(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO));
		else {
			//os demais eventos não alteram nem para GRP nem para TJ
			stTemp = eventoExecucaoNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		}
		
		return stTemp;
	}

	public String consultarDescricaoLocalCumprimentoPenaJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		LocalCumprimentoPenaNe local = new LocalCumprimentoPenaNe();
		stTemp = local.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	public List consultarDescricaoLocalCumprimentoPena() throws Exception{
		List tempList = null;
		tempList = new LocalCumprimentoPenaNe().consultarDescricao("", "");
		
		return tempList;
	}
	
	public List consultarDescricaoFormaCumprimentoExecucao() throws Exception{
		List tempList = null;

		tempList = new FormaCumprimentoExecucaoNe().consultarDescricao("", "");
		
		return tempList;
	}
	
	public List consultarDescricaoEventoExecucaoStatus(boolean isMostrarNaoAplica) throws Exception{
		List tempList = null;

			tempList = new EventoExecucaoStatusNe().consultarDescricao("", "", isMostrarNaoAplica);
		return tempList;
	}
	
	public String consultarDescricaoCrimeExecucaoJSON(String crime, String lei, String artigo, String posicao) throws Exception {
		String stTemp = "";

		CrimeExecucaoNe crimeNe = new CrimeExecucaoNe();
		stTemp = crimeNe.consultarDescricaoJSON(crime, lei, artigo, posicao);

		return stTemp;
	}

	 public void adicionarModalidadeSituacaoAtual(SituacaoAtualExecucaoDt situacaoAtualExecucaoDt) throws Exception{
    	
		SituacaoAtualModalidadeDt modalidadeDt = new SituacaoAtualModalidadeDt();
		modalidadeDt.setIdModalidade(situacaoAtualExecucaoDt.getIdModalidade());
		modalidadeDt.setIdSituacaoAtualExecucao(situacaoAtualExecucaoDt.getId());
		modalidadeDt.setModalidade(situacaoAtualExecucaoDt.getModalidade());
		modalidadeDt.setId_UsuarioLog(situacaoAtualExecucaoDt.getId_UsuarioLog());
		modalidadeDt.setIpComputadorLog(situacaoAtualExecucaoDt.getIpComputadorLog());
		situacaoAtualExecucaoDt.addListaSituacaoAtualModalidadeDt(modalidadeDt);

		if (modalidadeDt.getIdSituacaoAtualExecucao().length() > 0){
			new SituacaoAtualModalidadeNe().salvar(modalidadeDt);    			
		}
    		    	
    }
	 
	public void excluirModalidadeSituacaoAtual(SituacaoAtualModalidadeDt modalidadeDt, UsuarioDt usuarioDt) throws Exception{
		
		modalidadeDt.setId_UsuarioLog(usuarioDt.getId_UsuarioLog());
		modalidadeDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		
		new SituacaoAtualModalidadeNe().excluir(modalidadeDt);
		
	}
	 
	public void excluirTipoPenaSituacaoAtual(SituacaoAtualTipoPenaDt tipoPenaDt, UsuarioDt usuarioDt) throws Exception{
		
		tipoPenaDt.setId_UsuarioLog(usuarioDt.getId_UsuarioLog());
		tipoPenaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		
		new SituacaoAtualTipoPenaNe().excluir(tipoPenaDt);
		
	}
	
	public void adicionarTipoPenaSituacaoAtual(SituacaoAtualExecucaoDt situacaoAtualExecucaoDt) throws Exception{
    	 
		SituacaoAtualTipoPenaDt tipoPenaDt = new SituacaoAtualTipoPenaDt();
		tipoPenaDt.setIdPenaExecucaoTipo(situacaoAtualExecucaoDt.getIdPenaExecucaoTipo());
		tipoPenaDt.setIdSituacaoAtualExecucao(situacaoAtualExecucaoDt.getId());
		tipoPenaDt.setPenaExecucaoTipo(situacaoAtualExecucaoDt.getPenaExecucaoTipo());
		tipoPenaDt.setId_UsuarioLog(situacaoAtualExecucaoDt.getId_UsuarioLog());
		tipoPenaDt.setIpComputadorLog(situacaoAtualExecucaoDt.getIpComputadorLog());
		situacaoAtualExecucaoDt.addListaSituacaoAtualTipoPenaDt(tipoPenaDt);

		if (tipoPenaDt.getIdSituacaoAtualExecucao().length() > 0){
			new SituacaoAtualTipoPenaNe().salvar(tipoPenaDt);    			
		}
    	
    }
	 
	public SituacaoAtualExecucaoDt consultarSituacaoAtualExecucao(String idProcesso, String idUsuario, String ipComputador) throws Exception{
		SituacaoAtualExecucaoDt situacaoAtualExecucaoDt = null;
		 
		situacaoAtualExecucaoDt = new SituacaoAtualExecucaoNe().consultarIdProcesso(idProcesso, idUsuario, ipComputador);

		return situacaoAtualExecucaoDt;
	}
	
	public String adicionarModalidadeProcessoExecucao(ProcessoExecucaoDt processoExecucaoDt) throws Exception{
		String mensagemRetorno = new ProcessoExecucaoNe().adicionarModalidadeProcessoExecucao(processoExecucaoDt);
		return mensagemRetorno;
	}
	
	public String adicionarCondenacaoProcesso(HttpServletRequest request, UsuarioDt usuarioDt, int paginaAtual, ProcessoExecucaoDt processoExecucaoDt) throws Exception{
		String mensagemRetorno = new ProcessoExecucaoNe().adicionarCondenacaoProcesso(request, usuarioDt, paginaAtual, processoExecucaoDt);
		return mensagemRetorno;
	}
	

	public String verificarDadosCondenacao(ProcessoExecucaoDt dados) throws Exception{
		String stRetorno = "";

			stRetorno = new ProcessoExecucaoNe().verificarDadosCondenacao(dados);
		return stRetorno;
	}

	/**
	 * Salva a condenação
	 * 
	 * @param condenacaoExecucaodt
	 * @throws Exception
	 * @author wcsilva
	 */
	public void salvarCondenacao(CondenacaoExecucaoDt condenacaoExecucaodt) throws Exception{

			new CondenacaoExecucaoNe().salvar(condenacaoExecucaodt);
	}

	/**
	 * Exclui a condenação
	 * 
	 * @param condenacaoExecucaodt
	 * @throws Exception
	 * @author wcsilva
	 */
	public String excluirCondenacao(String posicaoLista, ProcessoExecucaoDt processoExecucaoDt) throws Exception{
		String msg = "";
		
//			 if (posicaoLista != null && posicaoLista.length() > 0) {
//				 if (processoExecucaoDt.getListaCondenacoes() != null && processoExecucaoDt.getListaCondenacoes().size() > 1
//						 && !processoExecucaoDt.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))){
//					 new CondenacaoExecucaoNe().excluir((CondenacaoExecucaoDt) processoExecucaoDt.getListaCondenacoes().get(Funcoes.StringToInt(posicaoLista)));
//					 processoExecucaoDt.getListaCondenacoes().remove(Funcoes.StringToInt(posicaoLista));
//				 } else {
//					 msg = "Não é possível excluir a condenação. (Motivo: O processo deve conter pelo menos uma condenação!)";
//				 }
//	        }
		msg = new ProcessoExecucaoNe().excluirCondenacao(posicaoLista, processoExecucaoDt, true);

		return msg;
	}

	 public void setListaRegimeRequest(HttpServletRequest request) throws Exception{
		 List listaRegimePPL = null;
	    	List listaRegimeMS = null;
	    	if (request.getSession().getAttribute("ListaRegime_PPL") == null){
	    		listaRegimePPL = consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE));
	    		request.getSession().setAttribute("ListaRegime_PPL", listaRegimePPL);
	    	} else listaRegimePPL = (List)request.getSession().getAttribute("ListaRegime_PPL");
	    	
	    	if (request.getSession().getAttribute("ListaRegime_MS") == null){
	    		listaRegimeMS = consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA));
	    		request.getSession().setAttribute("ListaRegime_MS", listaRegimeMS);	
	    	} else listaRegimeMS = (List)request.getSession().getAttribute("ListaRegime_MS");
	        
	    	if (request.getSession().getAttribute("SA_ListaRegime_PPL") == null){
	    		List SA_listaRegimePPL = new ArrayList();
	            SA_listaRegimePPL.addAll(listaRegimePPL);
	            SA_listaRegimePPL.addAll(listaRegimeMS);
	            request.getSession().setAttribute("SA_ListaRegime_PPL", SA_listaRegimePPL);	
	    	}
	        
	    	if (request.getSession().getAttribute("ListaPenaExecucaoTipo") == null) request.getSession().setAttribute("ListaPenaExecucaoTipo", consultarIdsPenaExecucaoTipo(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE + "," + PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA));
	    	if (request.getSession().getAttribute("SA_ListaPenaExecucaoTipo") == null) request.getSession().setAttribute("SA_ListaPenaExecucaoTipo", consultarIdsPenaExecucaoTipo(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE + "," + PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA + "," + PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO));
	    	if (request.getSession().getAttribute("ListaModalidade") == null) request.getSession().setAttribute("ListaModalidade", consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO)));
	        if (request.getSession().getAttribute("ListaLocal") == null) request.getSession().setAttribute("ListaLocal", consultarDescricaoLocalCumprimentoPena());
	        if (request.getSession().getAttribute("ListaFormaCumprimento") == null) request.getSession().setAttribute("ListaFormaCumprimento", consultarDescricaoFormaCumprimentoExecucao());
	        if (request.getSession().getAttribute("ListaStatus") == null) request.getSession().setAttribute("ListaStatus", consultarDescricaoEventoExecucaoStatus(true));
    }

	public String consultarGrupoMovimentacaoTipoJSON(String grupoCodigo, String tempNomeBusca,String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		MovimentacaoTipoNe neObjeto = new MovimentacaoTipoNe();
		stTemp = neObjeto.consultarGrupoMovimentacaoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
				
		neObjeto = null;
		return stTemp;
	}
}
