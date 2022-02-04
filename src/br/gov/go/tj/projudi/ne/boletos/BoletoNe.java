package br.gov.go.tj.projudi.ne.boletos;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.PrazoSuspensoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.boletos.caixa.BoletoNeCaixaJaxWS;
import br.gov.go.tj.projudi.ps.GuiaEmissaoPs;
import br.gov.go.tj.projudi.ps.GuiaSPGPs;
import br.gov.go.tj.projudi.ps.GuiaSSGPs;
import br.gov.go.tj.projudi.ps.PrazoSuspensoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public abstract class BoletoNe {
	
	private final Logger LOGGER = Logger.getLogger(BoletoNe.class);
	
	private static BoletoNe SINGLETON;
	
	public static BoletoNe get() throws Exception {
		if (SINGLETON == null) {
			SINGLETON = new BoletoNeCaixaJaxWS();
		}
		return SINGLETON;
	}
	
	public BoletoDt transformar(GuiaEmissaoDt guia, PagadorBoleto pagador) throws Exception {
		if(!guia.podeGerarBoleto())
			throw new MensagemException("Esta guia não pode gerar novos boletos, verifique o status ou a data de vencimento.");
		BoletoDt boleto = new BoletoDt();
		boleto.copiar(guia);
		boleto.setSituacaoBoleto(SituacaoBoleto.NAO_REGISTRADO);
		boleto.setNumeroDocumento(Funcoes.completarZeros(guia.getNumeroGuiaCompleto(), 11));
		boleto.setNossoNumero("14" + boleto.getNumeroDocumento() + "0000");
		boleto.setDataEmissaoBoleto(Funcoes.dateToStringSoData(Funcoes.Stringyyyy_MM_ddToDateTime(guia.getDataEmissao())));
		if ((guia.getId_Comarca() == null || guia.getId_Comarca().trim().length() == 0) && (guia.isGuiaEmitidaSPG() || guia.isGuiaEmitidaSSG())) {
			String comarcaCodigo = ComarcaDt.GOIANIA;
			if (guia.isGuiaCertidaoSPG() && guia.getInfoLocalCertidaoSPG() != null && guia.getInfoLocalCertidaoSPG().trim().length() > 3) {
				comarcaCodigo = guia.getInfoLocalCertidaoSPG().trim().substring(0, 3);
			}
			ComarcaDt comarcaDt = new ComarcaNe().consultarComarcaCodigo(comarcaCodigo);
			if (comarcaDt != null) {
				guia.setId_Comarca(comarcaDt.getId());
				guia.setComarca(comarcaDt.getComarca());
				guia.setComarcaCodigo(comarcaDt.getComarcaCodigo());
			}
		}
		
		Date dataVencimentoGuia = Funcoes.Stringyyyy_MM_ddToDateTime(guia.getDataVencimento());		
		Date dataVencimentoBoleto = getProximaDataBancariaValida(new Date(), 15, guia.getId_Comarca());
		
		if (Funcoes.StringToInt(guia.getParcelaAtual()) > 1 &&
			!dataVencimentoGuia.before(new Date())) {
			dataVencimentoBoleto = dataVencimentoGuia;
		} else {
			if (boleto.isSerie10()) {
				dataVencimentoBoleto = dataVencimentoGuia;
			}
		}
		
		//Guia parcelada tem vencimento do boleto igual a guia
		// OU
		//Ocorrencia 2020/6290: Guia de Devolução de Vencimento / Auxílios tb tem vencimento do boleto igual a guia
		if( guia.isGuiaParcelada() || (guia.isSerie02() && guia.isGuiaTipoDevolucaoSPG()) || guia.isGuiaGRSPrecatorioSPG() ) {
			dataVencimentoBoleto = dataVencimentoGuia;
		}
		
		if (dataVencimentoBoleto.before(dataVencimentoGuia) || guia.isSerie10())
			boleto.setDataVencimentoBoleto(Funcoes.dateToStringSoData(dataVencimentoBoleto));
		else
			boleto.setDataVencimentoBoleto(Funcoes.dateToStringSoData(dataVencimentoGuia));
		
		//**********
		//Guia parcelada tem vencimento do boleto igual a guia
		if( guia.isGuiaParcelada() || (guia.isSerie02() && guia.isGuiaTipoDevolucaoSPG()) || guia.isGuiaGRSPrecatorioSPG() ) {
			boleto.setDataVencimentoBoleto(Funcoes.dateToStringSoData(dataVencimentoGuia));
		}
		
		boleto.setValorBoleto(new DecimalFormat("#0.00").format(guia.getValorTotalGuiaDouble()).replace(",", "."));
		if (pagador == null)
			throw new MensagemException("Por favor, informe o pagador!");
		boleto.getPagador().setTipoPessoa(pagador.getTipoPessoa());
		boleto.getPagador().setCpf(pagador.getCpf());
		boleto.getPagador().setNome(pagador.getNome());
		boleto.getPagador().setCnpj(pagador.getCnpj());
		boleto.getPagador().setRazaoSocial(pagador.getRazaoSocial());
		boleto.getPagador().setLogradouro(pagador.getLogradouro());
		boleto.getPagador().setBairro(pagador.getBairro());
		boleto.getPagador().setCidade(pagador.getCidade());
		boleto.getPagador().setUf(pagador.getUf());
		boleto.getPagador().setCep(pagador.getCep());
		
		if (guia instanceof BoletoDt) {
			BoletoDt boletoTemp = (BoletoDt) guia;
			boleto.setObservacao1(boletoTemp.getObservacao1());
			boleto.setObservacao2(boletoTemp.getObservacao2());
			boleto.setObservacao3(boletoTemp.getObservacao3());
			boleto.setObservacao4(boletoTemp.getObservacao4());
		}
		
		return boleto;
	}
	
	private Date getProximaDataBancariaValida(Date dataInicial, int diasUteis, String id_Comarca) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataInicial);
			PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
			int contagemDiasUteis = 0;
			do {
				calendar.add(Calendar.DATE, 1);
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					calendar.add(Calendar.DATE, 2);
				}
				else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					calendar.add(Calendar.DATE, 1);
				}
				if (id_Comarca == null || id_Comarca.trim().length() == 0 || obPersistencia.isDataValidaBancaria(calendar.getTime(), id_Comarca)) {
					contagemDiasUteis++;
				}
			} while (contagemDiasUteis < diasUteis);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			return calendar.getTime();
		} finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public void atualizaSituacao(BoletoDt boleto) throws Exception {
		atualizaSituacao(boleto, null);
	}
	
	public void atualizaSituacao(BoletoDt boleto, UsuarioNe usuarioNe) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			SituacaoBoleto situacao = consultaSituacao(boleto);
			if (situacao != boleto.getSituacaoBoleto()) {
				boleto.setSituacaoBoleto(situacao);
			if (boleto.isGuiaEmitidaSPG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				new GuiaSPGPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
			}
			else if (boleto.isGuiaEmitidaSSG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				new GuiaSSGPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
			}
			else {
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					obFabricaConexao.iniciarTransacao();
					new GuiaEmissaoPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
					LogDt obLogDt = new LogDt("GuiaEmissao", null, null, boleto.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", boleto.getPropriedades());
					if (usuarioNe != null) {
						obLogDt.setId_UsuarioLog(usuarioNe.getId_Usuario());
						obLogDt.setIpComputadorLog(usuarioNe.getIpComputadorLog());
					}
					else {
						obLogDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
						obLogDt.setIpComputadorLog("127.0.0.1");
					}
					new LogNe().salvar(obLogDt, obFabricaConexao);
					obFabricaConexao.finalizarTransacao();
					
				} catch (Exception e) {
					obFabricaConexao.cancelarTransacao();
					throw e;
				}
			}
			LOGGER.debug("NOVA SITUAÇÃO: " + situacao.getTexto());
			}
			else {
				LOGGER.debug("MESMA SITUAÇÃO: " + situacao.getTexto());
			}
		} finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public BoletoDt emiteBoleto(GuiaEmissaoDt guia, PagadorBoleto pagador) throws Exception {
		return emiteBoleto(guia, pagador, null);
	}
	
	public BoletoDt emiteBoleto(GuiaEmissaoDt guia, PagadorBoleto pagador, UsuarioNe usuarioNe) throws Exception {
		switch (Integer.parseInt(guia.getId_GuiaStatus())) {
			case GuiaStatusDt.AGUARDANDO_PAGAMENTO:
			case GuiaStatusDt.ESTORNO_BANCARIO:
				break;
			default:
				throw new MensagemException("Somente guias aguardando pagamento podem gerar boletos.");
		}
		if(!guia.podeGerarBoleto())
			throw new MensagemException("Esta guia não pode gerar novos boletos, verifique o status ou a data de vencimento.");		
		BoletoDt boleto = null;
		FabricaConexao obFabricaConexao = null;
		try {
			if (guia.isGuiaEmitidaSPG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				boleto = emiteBoletoSPG(guia, pagador, obFabricaConexao);
			}
			else if (guia.isGuiaEmitidaSSG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				boleto = emiteBoletoSSG(guia, pagador, obFabricaConexao);
			}
			else {
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					obFabricaConexao.iniciarTransacao();
					boleto = emiteBoletoProjudi(guia, pagador, usuarioNe, obFabricaConexao);
					obFabricaConexao.finalizarTransacao();
				} catch (Exception e) {
					obFabricaConexao.cancelarTransacao();
					throw e;
				}
			}
		} finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return boleto;
	}
	
	private BoletoDt emiteBoletoProjudi(GuiaEmissaoDt guia, PagadorBoleto pagador, UsuarioNe usuarioNe, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoPs guiaEmissaoPs = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		BoletoDt boleto = guiaEmissaoPs.buscaBoletoPorId(guia.getId());
		if (boleto != null)
			throw new MensagemException("Esta guia já possui um boleto emitido.");
		boleto = transformar(guia, pagador);
		guiaEmissaoPs.salvar(boleto);
		trasmiteBoleto(boleto);
		boleto.setSituacaoBoleto(SituacaoBoleto.EM_ABERTO);
		guiaEmissaoPs.atualizarSituacaoBoleto(boleto);
		LogDt obLogDt = new LogDt("GuiaEmissao", boleto.getId(), null, null, String.valueOf(LogTipoDt.BoletoEmitido), "", boleto.getPropriedades());
		if (usuarioNe != null) {
			obLogDt.setId_UsuarioLog(usuarioNe.getId_Usuario());
			obLogDt.setIpComputadorLog(usuarioNe.getIpComputadorLog());
		}
		else {
			obLogDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
			obLogDt.setIpComputadorLog("127.0.0.1");
		}
		new LogNe().salvar(obLogDt, obFabricaConexao);
		LOGGER.debug("BOLETO EMITIDO.\n\t" + boleto.getUrlPdf());
		return boleto;
	}
	
	private BoletoDt emiteBoletoSPG(GuiaEmissaoDt guia, PagadorBoleto pagador, FabricaConexao obFabricaConexao) throws Exception {
		switch (Integer.parseInt(guia.getId_GuiaStatus())) {
			case GuiaStatusDt.AGUARDANDO_PAGAMENTO:
			case GuiaStatusDt.ESTORNO_BANCARIO:
				break;
			default:
				throw new MensagemException("Somente guias aguardando pagamento podem gerar boletos.");
		}
		
		if( guia.getNumeroGuiaCompleto().contains(".") ) {
			throw new MensagemException("Número da Guia informado não pode conter pontos(.) [Erro14]");
		}
		
		GuiaSPGPs guiaSpgPs = new GuiaSPGPs(obFabricaConexao.getConexao());
		BoletoDt boleto = guiaSpgPs.buscaBoletoPorNumero(guia.getNumeroGuiaCompleto());
		if (boleto != null)
			throw new MensagemException("Esta guia já possui um boleto emitido.");
		boleto = transformar(guia, pagador);
		guiaSpgPs.salvar(boleto);
		trasmiteBoleto(boleto);
		boleto.setSituacaoBoleto(SituacaoBoleto.EM_ABERTO);
		guiaSpgPs.atualizarSituacaoBoleto(boleto);
		LOGGER.debug("BOLETO EMITIDO.\n\t" + boleto.getUrlPdf());
		return boleto;
	}
	
	private BoletoDt emiteBoletoSSG(GuiaEmissaoDt guia, PagadorBoleto pagador, FabricaConexao obFabricaConexao) throws Exception {
		switch (Integer.parseInt(guia.getId_GuiaStatus())) {
			case GuiaStatusDt.AGUARDANDO_PAGAMENTO:
			case GuiaStatusDt.ESTORNO_BANCARIO:
				break;
			default:
				throw new MensagemException("Somente guias aguardando pagamento podem gerar boletos.");
		}
		GuiaSSGPs guiaSsgPs = new GuiaSSGPs(obFabricaConexao.getConexao());
		BoletoDt boleto = guiaSsgPs.buscaBoletoPorNumero(guia.getNumeroGuiaCompleto());
		if (boleto != null)
			throw new MensagemException("Esta guia já possui um boleto emitido.");
		boleto = transformar(guia, pagador);
		guiaSsgPs.salvar(boleto);
		trasmiteBoleto(boleto);
		boleto.setSituacaoBoleto(SituacaoBoleto.EM_ABERTO);
		guiaSsgPs.atualizarSituacaoBoleto(boleto);
		LOGGER.debug("BOLETO EMITIDO.\n\t" + boleto.getUrlPdf());
		return boleto;
	}
	
	public void reemiteBoleto(BoletoDt boleto) throws Exception {
		reemiteBoleto(boleto, null);
	}
	
	public void reemiteBoleto(BoletoDt boleto, UsuarioNe usuarioNe) throws Exception {
		switch (Integer.parseInt(boleto.getId_GuiaStatus())) {
			case GuiaStatusDt.AGUARDANDO_PAGAMENTO:
			case GuiaStatusDt.ESTORNO_BANCARIO:
				break;
			default:
				throw new MensagemException("Somente guias aguardando pagamento podem gerar boletos.");
		}
		if (boleto.getId() == null || boleto.getId().isEmpty())
			throw new MensagemException("Esta guia não possui um boleto emitido.");
		switch (boleto.getSituacaoBoleto()) {
			case BAIXA_POR_DEVOLUCAO:
				break;
			case EM_ABERTO:
				if (boleto.isVencido() || boleto.isMudouPagador() || boleto.isValorGuiaDiferenteValorBoleto() || boleto.isObservacaoFoiAterada()) {
//					if(boleto.isGuiaVencida() && !boleto.isSerie10())
//						throw new MensagemException("Esta guia está vencida, não pode gerar novos boletos.");
					if(boleto.isGuiaVencida()){
				        if (boleto.isSerie10())
				                return;
				        throw new MensagemException("Esta guia está vencida, não pode gerar novos boletos.");
					}
					break;
				}
			default:
				throw new MensagemException("Somente boletos vencidos podem ser reemitidos.");
		}
		String contador = Long.toString((Long.parseLong(boleto.getNossoNumero()) % 10000) + 1);
		contador = Funcoes.completarZeros(contador, 4);
		boleto.setNumeroDocumento(Funcoes.completarZeros(boleto.getNumeroGuiaCompleto(), 11));
		boleto.setNossoNumero("14" + boleto.getNumeroDocumento() + contador);
		
		if ((boleto.getId_Comarca() == null || boleto.getId_Comarca().trim().length() == 0) && (boleto.isGuiaEmitidaSPG() || boleto.isGuiaEmitidaSSG())) {
			String comarcaCodigo = ComarcaDt.GOIANIA;
			if (boleto.isGuiaCertidaoSPG() && boleto.getInfoLocalCertidaoSPG() != null && boleto.getInfoLocalCertidaoSPG().trim().length() > 3) {
				comarcaCodigo = boleto.getInfoLocalCertidaoSPG().trim().substring(0, 3);
			}
			ComarcaDt comarcaDt = new ComarcaNe().consultarComarcaCodigo(comarcaCodigo);
			if (comarcaDt != null) {
				boleto.setId_Comarca(comarcaDt.getId());
				boleto.setComarca(comarcaDt.getComarca());
				boleto.setComarcaCodigo(comarcaDt.getComarcaCodigo());
			}
		}
		
		atualizaDataDeVencimentoReemissao(boleto);
		
		if (boleto.getDataEmissaoBoleto() == null || boleto.getDataEmissaoBoleto().trim().length() == 0) {
			boleto.setDataEmissaoBoleto(Funcoes.dateToStringSoData(Funcoes.Stringyyyy_MM_ddToDateTime(boleto.getDataEmissao())));
		} else if (boleto.getDataEmissaoBoleto().contains("/")) {
			boleto.setDataEmissaoBoleto(Funcoes.dateToStringSoData(Funcoes.StringToDateTime(boleto.getDataEmissaoBoleto())));
		} else {
			boleto.setDataEmissaoBoleto(Funcoes.dateToStringSoData(Funcoes.Stringyyyy_MM_ddToDateTime(boleto.getDataEmissaoBoleto())));	
		}
		
		if (boleto.isValorGuiaDiferenteValorBoleto()) {
			boleto.setValorBoleto(new DecimalFormat("#0.00").format(boleto.getValorTotalGuiaDouble()).replace(",", "."));
		}
		
		trasmiteBoleto(boleto);
		boleto.setSituacaoBoleto(SituacaoBoleto.EM_ABERTO);
		FabricaConexao obFabricaConexao = null;
		try {
			if (boleto.isGuiaEmitidaSPG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				new GuiaSPGPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
			}
			else if (boleto.isGuiaEmitidaSSG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				new GuiaSSGPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
			}
			else {
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					obFabricaConexao.iniciarTransacao();
					new GuiaEmissaoPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
					LogDt obLogDt = new LogDt("GuiaEmissao", boleto.getId(), null, null, String.valueOf(LogTipoDt.BoletoReemitido), "", boleto.getPropriedades());
					if (usuarioNe != null) {
						obLogDt.setId_UsuarioLog(usuarioNe.getId_Usuario());
						obLogDt.setIpComputadorLog(usuarioNe.getIpComputadorLog());
					}
					else {
						obLogDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
						obLogDt.setIpComputadorLog("127.0.0.1");
					}
					new LogNe().salvar(obLogDt, obFabricaConexao);
					obFabricaConexao.finalizarTransacao();
				} catch (Exception e) {
					obFabricaConexao.cancelarTransacao();
					throw e;
				}
			}
		} finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
		}
		LOGGER.debug("BOLETO REEMITIDO.\n\t" + boleto.getUrlPdf());
	}
	
	private void atualizaDataDeVencimentoReemissao(BoletoDt boleto) throws Exception {
		Date dataVencimentoGuia = Funcoes.Stringyyyy_MM_ddToDateTime(boleto.getDataVencimento());
		
		if (boleto.isSerie10() && !(new TJDataHora()).ehApos(new TJDataHora(dataVencimentoGuia))) {
			boleto.setDataVencimentoBoleto(Funcoes.dateToStringSoData(dataVencimentoGuia));
		} else {
			Date dataVencimentoBoleto = getProximaDataBancariaValida(new Date(), 15, boleto.getId_Comarca());
			
			if (Funcoes.StringToInt(boleto.getParcelaAtual()) > 1 &&
				!dataVencimentoGuia.before(new Date())) {
				dataVencimentoBoleto = dataVencimentoGuia;
			}
			
			if (dataVencimentoBoleto.before(dataVencimentoGuia) || dataVencimentoGuia.before(new Date()))
				boleto.setDataVencimentoBoleto(Funcoes.dateToStringSoData(dataVencimentoBoleto));
			else
				boleto.setDataVencimentoBoleto(Funcoes.dateToStringSoData(dataVencimentoGuia));
		}
	}
	
	public void cancela(BoletoDt boleto) throws Exception {
		cancela(boleto, null);
	}
	
	public void cancela(BoletoDt boleto, UsuarioNe usuarioNe) throws Exception {
		if (boleto.getSituacaoBoleto() != SituacaoBoleto.EM_ABERTO)
			throw new MensagemException("Somente boletos EM ABERTO podem ser cancelados.");
		transmiteCancelamento(boleto);
		boleto.setSituacaoBoleto(SituacaoBoleto.BAIXA_POR_DEVOLUCAO);
		FabricaConexao obFabricaConexao = null;
		try {
			if (boleto.isGuiaEmitidaSPG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				new GuiaSPGPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
			}
			else if (boleto.isGuiaEmitidaSSG()) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
				new GuiaSSGPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
			}
			else {
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					obFabricaConexao.iniciarTransacao();
					new GuiaEmissaoPs(obFabricaConexao.getConexao()).atualizarSituacaoBoleto(boleto);
					LogDt obLogDt = new LogDt("GuiaEmissao", null, null, boleto.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", boleto.getPropriedades());
					if (usuarioNe != null) {
						obLogDt.setId_UsuarioLog(usuarioNe.getId_Usuario());
						obLogDt.setIpComputadorLog(usuarioNe.getIpComputadorLog());
					}
					else {
						obLogDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
						obLogDt.setIpComputadorLog("127.0.0.1");
					}
					new LogNe().salvar(obLogDt, obFabricaConexao);
					obFabricaConexao.finalizarTransacao();
				} catch (Exception e) {
					obFabricaConexao.cancelarTransacao();
					throw e;
				}
			}
		} finally {
			if (obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
		}
		LOGGER.debug("BOLETO CANCELADO.");
	}
	
	protected abstract SituacaoBoleto consultaSituacao(BoletoDt boleto) throws Exception;
	
	protected abstract void trasmiteBoleto(BoletoDt boleto) throws Exception;
	
	protected abstract void transmiteCancelamento(BoletoDt boleto) throws Exception;
	
	protected abstract void transmiteAlteracao(BoletoDt boleto) throws Exception;
	
	public abstract byte[] geraPDF(BoletoDt boleto) throws Exception;
	
	public abstract String consultaUrlPdf(BoletoDt boleto) throws Exception;

	public abstract void atualizaInformacoesBoletoTransmitido(BoletoDt boleto) throws Exception;
}
