package br.gov.go.tj.projudi.ne;

import java.util.List;
import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

public class GeraTextoBancoNe {

	int sequenciaLote = 0;
	int sequenciaRegistroLote = 0;
	int totalGeralLinhas = 0;
	double somatorioValorLote = 0;
	
	String convenio = "";
	String banco = "";
	String agencia = "";
	String dvAgencia = "";
	String conta = "";
	String dvConta = "";
	String tipoTextoBanco = "";
	String identificador = "";
	
	StringBuffer sbTextoBanco = new StringBuffer();

	TJDataHora tjDataHora = new TJDataHora();
	Funcoes funcao = new Funcoes();

	public GeraTextoBancoNe() {
	}

	public StringBuffer textoMandadoGratuito(List<RelatoriosMandadoDt> listaOficiais, int seqTexto) throws Exception {

		RelatoriosMandadoDt objDt;
		
		convenio = BancoDt.CODIGO_CONVENIO_CAIXA_SEM_CUSTAS;
		banco = BancoDt.CODIGO_BANCO_SEM_CUSTAS;
		agencia = BancoDt.CODIGO_AGENCIA_CAIXA_SEM_CUSTAS;
		dvAgencia = BancoDt.DIGITO_AGENCIA_CAIXA_SEM_CUSTAS;
		conta = BancoDt.NUMERO_CONTA_CAIXA_SEM_CUSTAS;
		dvConta = BancoDt.DIGITO_CONTA_CAIXA_SEM_CUSTAS;	
		tipoTextoBanco = BancoDt.TIPO_TEXTO_BANCO_PRODUCAO;
		identificador = BancoDt.IDENTIFICADOR_TEXTO_SEM_CUSTAS;


		listaOficiais = this.ordenaListaPorBanco(listaOficiais);

		String nomeBancoAnt = listaOficiais.get(0).getNomeBanco();
		this.headerArquivoMandado(seqTexto);
		this.headerLoteMandado(listaOficiais.get(0).getNomeBanco());
		for (int linha = 0; linha < listaOficiais.size(); linha++) {
			objDt = (listaOficiais.get(linha));
			//
			// quebra por nome banco
			//
			if (nomeBancoAnt.equalsIgnoreCase(objDt.getNomeBanco())) {
				this.detalheAMandado(objDt);
				this.detalheBMandado(objDt);
			} else {
				this.trailerLoteMandado();
				this.sequenciaRegistroLote = 0;
				this.headerLoteMandado(objDt.getNomeBanco());
				this.somatorioValorLote = 0;
				this.detalheAMandado(objDt);
				this.detalheBMandado(objDt);
				nomeBancoAnt = objDt.getNomeBanco();
			}
		}
		this.trailerLoteMandado();
		this.trailerBancoMandado();
		return this.sbTextoBanco;

	}

	public StringBuffer textoMandadoComCustas(List<RelatoriosMandadoDt> listaOficiais, int seqTexto) throws Exception {
		RelatoriosMandadoDt objDt;
		
		convenio = BancoDt.CODIGO_CONVENIO_CAIXA_COM_CUSTAS;
		banco = BancoDt.CODIGO_BANCO_COM_CUSTAS;
		agencia = BancoDt.CODIGO_AGENCIA_CAIXA_COM_CUSTAS;
		dvAgencia = BancoDt.DIGITO_AGENCIA_CAIXA_COM_CUSTAS;
		conta = BancoDt.NUMERO_CONTA_CAIXA_COM_CUSTAS;
		dvConta = BancoDt.DIGITO_CONTA_CAIXA_COM_CUSTAS;	
		tipoTextoBanco = BancoDt.TIPO_TEXTO_BANCO_TESTE;	
		identificador = BancoDt.IDENTIFICADOR_TEXTO_COM_CUSTAS;

		listaOficiais = this.ordenaListaPorBanco(listaOficiais);
				
		String nomeBancoAnt = listaOficiais.get(0).getNomeBanco();
		headerArquivoMandado(seqTexto);
		headerLoteMandado(listaOficiais.get(0).getNomeBanco());
		for (int linha = 0; linha < listaOficiais.size(); linha++) {
			objDt = (listaOficiais.get(linha));
			//
			// quebra por nome banco
			//
			if (nomeBancoAnt.equalsIgnoreCase(objDt.getNomeBanco())) {
				detalheAMandado(objDt);
				detalheBMandado(objDt);
			} else {
				this.trailerLoteMandado();
				this.sequenciaRegistroLote = 0;
				this.headerLoteMandado(objDt.getNomeBanco());
				this.somatorioValorLote = 0;
				this.detalheAMandado(objDt);
				this.detalheBMandado(objDt);
				nomeBancoAnt = objDt.getNomeBanco();
			}
		}
		this.trailerLoteMandado();
		this.trailerBancoMandado();
		return this.sbTextoBanco;
	}

	public List<RelatoriosMandadoDt> ordenaListaPorBanco(List<RelatoriosMandadoDt> listaObj) throws Exception {

		RelatoriosMandadoDt aux1 = null;
		RelatoriosMandadoDt aux2 = null;
		boolean ordenado = false;
		while (ordenado == false) {
			ordenado = true;
			for (int pos = 0; pos < listaObj.size(); pos++) {
				if (pos + 1 < listaObj.size()) {
					aux1 = listaObj.get(pos);
					aux2 = listaObj.get(pos + 1);
					int comp = aux1.getNomeBanco().compareTo(aux2.getNomeBanco());
					if (comp > 0) {
						ordenado = false;
						listaObj.set(pos, aux2);
						listaObj.set(pos + 1, aux1);
					}
				}
			}
		}
		return listaObj;
	}

	public void headerArquivoMandado(int seqTexto) throws Exception {

		String codigoBanco = this.banco;

		String codigoLote = "0000";

		String codigoRegistro = "0";

		String filler9 = funcao.preencheVazio(9);

		String tipoInscricao = "2";

		String cnpjTj = MandadoJudicialDt.CNPJ_TJGO;

		String codigoConvenio = this.convenio;

		String parametroTransmissao = "01";

		String ambienteCliente = tipoTextoBanco;

		String ambienteCaixa = funcao.preencheVazio(1);

		String origemAplicativo = funcao.preencheVazio(3);

		String versaoAplicativo = "0000";

		String filler3 = funcao.preencheVazio(3);

		String agencia = this.agencia;

		String dvAgencia = this.dvAgencia;

		String conta = this.conta;

		String dvConta = this.dvConta;

		String dvAgenciaConta = funcao.preencheVazio(1);

		String nomeEmpresa = funcao.completarEspacosDireita("TRIBUNAL DE JUSTICA DO ESTADO", 30);

		String nomeBanco = funcao.completarEspacosDireita("CAIXA", 30);

		String filler10 = funcao.preencheVazio(10);

		String codigoRemessa = "1";

		String dataHoraArquivo = tjDataHora.getDataFormatadaDDMMyyyyHHmmss();

		String NSA = funcao.completarZeros(Integer.toString(seqTexto), 6);

		String versaoLayoutArquivo = "080";

		String densidadeGravacao = "01600";

		String reservadoBanco = funcao.preencheVazio(20);

		String reservadoEmpresa = funcao.preencheVazio(20);

		String exclusivoFebraban = funcao.preencheVazio(11);

		String identificadorCobranca = funcao.preencheVazio(3);

		String exclusivoVans = "000";

		String tipoServico = funcao.preencheVazio(2);

		String cobrancaSemPapel = funcao.preencheVazio(10);

		this.sbTextoBanco.append(codigoBanco + codigoLote + codigoRegistro + filler9 + tipoInscricao + cnpjTj
				+ codigoConvenio + parametroTransmissao + ambienteCliente + ambienteCaixa + origemAplicativo
				+ versaoAplicativo + filler3 + agencia + dvAgencia + conta + dvConta + dvAgenciaConta + nomeEmpresa
				+ nomeBanco + filler10 + codigoRemessa + dataHoraArquivo + NSA + versaoLayoutArquivo + densidadeGravacao
				+ reservadoBanco + reservadoEmpresa + exclusivoFebraban + identificadorCobranca + exclusivoVans
				+ tipoServico + cobrancaSemPapel + "\n");
		this.totalGeralLinhas++;

	}

	public void headerLoteMandado(String nomeBanco) throws Exception {

		String codigoBanco = this.banco;

		String loteServico = funcao.completarZeros(String.valueOf(++this.sequenciaLote), 4);

		String codigoRegistro = "1";

		String tipoOperacao = "C";

		String tipoServico = "98";

		String formaLancamento = "";
		if (nomeBanco.equalsIgnoreCase("CAIXA"))
			formaLancamento = "01";
		else
			formaLancamento = "41";

		String versaoLayoutLote = "041";

		String filler1 = funcao.preencheVazio(1);

		String tipoInscricao = "2";

		String numeroInscricao = MandadoJudicialDt.CNPJ_TJGO;

		String codigoConvenio = this.convenio;

		String tipoCompromisso = "01";

		String codigoCompromisso = "0001";

		String parametroTransmissao = "01";

		String filler6 = funcao.preencheVazio(6);

		String agencia = this.agencia;

		String dvAgencia = this.dvAgencia;

		String conta = this.conta;

		String dvConta = this.dvConta;

		String dvAgenciaConta = funcao.preencheVazio(1);

		String nomeEmpresa = funcao.completarEspacosDireita("TRIBUNAL DE JUSTICA DO ESTADO", 30);

		String mensagemAviso1 = funcao.preencheVazio(40);

		String logradouro = funcao.completarEspacosDireita("AV. ASSIS CHATEUBRIAND", 30);

		String numeroLocal = "00195";

		String complemento = funcao.completarEspacosDireita("PG LOCOMO", 15);

		String cidade = funcao.completarEspacosDireita("GOIANIA", 20);

		String cep = "74130";

		String complementoCep = "012";

		String siglaEstado = "GO";

		String usoFebraban = funcao.preencheVazio(8);

		String ocorrencias = funcao.preencheVazio(10);

		this.sbTextoBanco.append(codigoBanco + loteServico + codigoRegistro + tipoOperacao + tipoServico
				+ formaLancamento + versaoLayoutLote + filler1 + tipoInscricao + numeroInscricao + codigoConvenio
				+ tipoCompromisso + codigoCompromisso + parametroTransmissao + filler6 + agencia + dvAgencia + conta
				+ dvConta + dvAgenciaConta + nomeEmpresa + mensagemAviso1 + logradouro + numeroLocal + complemento
				+ cidade + cep + complementoCep + siglaEstado + usoFebraban + ocorrencias + "\n");

		this.totalGeralLinhas++;

		this.sequenciaRegistroLote++;

	}

	public void detalheAMandado(RelatoriosMandadoDt objDt) throws Exception {

		String codigoBanco = this.banco;

		String loteServico = funcao.completarZeros(String.valueOf(this.sequenciaLote), 4);

		String codigoRegistro = "3";

		String NSR = funcao.completarZeros(String.valueOf(++this.sequenciaRegistroLote), 5);

		String codigoSegmento = "A";

		String tipoDocumento = "0";

		String instrucaoDocumento = "00";

		String camaraCompensacao = "";
		if (objDt.getNomeBanco().equalsIgnoreCase("CAIXA")) 
			camaraCompensacao = "000";
		else
			camaraCompensacao = "018";

		String codigoBancoDestino = funcao.completarZeros(Integer.toString(objDt.getBanco()), 3);

		String codigoAgenciaDestino = funcao.completarZeros(Integer.toString(objDt.getAgencia()), 5);

		String dvAgenciaDestino = funcao.preencheVazio(1);

		String operacaoContaCorrenteDestino = funcao.completarZeros(Integer.toString(objDt.getContaOperacao()), 4);

		String contaCorrenteDestino = funcao.completarZeros(Integer.toString(objDt.getConta()), 8);

		String dvContaDestino = "";
		if (objDt.getContaDv() == null)
			dvContaDestino = funcao.preencheVazio(1);
		else if (objDt.getContaDv().equalsIgnoreCase(""))
			dvContaDestino = funcao.preencheVazio(1);
		else
			dvContaDestino = objDt.getContaDv();

		String dvAgenciaContaDestino = funcao.preencheVazio(1);

		if (objDt.getNomeUsuario().length() > 30)
			objDt.setNomeUsuario(objDt.getNomeUsuario().substring(0, 30));
		String nomeTerceiroFavorecido = funcao.completarEspacosDireita(objDt.getNomeUsuario(), 30);
		

		String numeroDocumentoEmpresa = "";

		numeroDocumentoEmpresa = funcao.completarZeros(String.valueOf(++this.totalGeralLinhas), 5);
		numeroDocumentoEmpresa = this.identificador + numeroDocumentoEmpresa;

		String filler13 = funcao.preencheVazio(13);

		String tipoContaFinalidadeTed = "";
		if (objDt.getNomeBanco().equalsIgnoreCase("CAIXA"))
			if (objDt.getContaOperacao() == 13)
				tipoContaFinalidadeTed = "2"; // poupanca
			else
				tipoContaFinalidadeTed = "1"; // conta corrente
		else
			tipoContaFinalidadeTed = "1";

		String dataVencimento = tjDataHora.getDataFormatadaddMMyyyySemSeparador();

		String tipoMoeda = "BRL"; // REAL

		String quantidadeMoeda = "";
		quantidadeMoeda = funcao.completarZeros(quantidadeMoeda, 15);

		String valorLancamento = funcao.completarZeros(String.valueOf(Math.round(objDt.getValorReceber() * 100)), 15);

		this.somatorioValorLote += objDt.getValorReceber();

		String numeroDocumento = "";
		numeroDocumento = funcao.completarZeros(numeroDocumento, 9);

		String filler3 = funcao.preencheVazio(3);

		String quantidadeParcelas = "01";

		String indicadorBloqueio = "S";

		String indicadorFormaPagamento = "1";

		String diaVencimento = "01";

		String numeroParcela = "00";

		String dataEfetivacao = "";
		dataEfetivacao = funcao.completarZeros(dataEfetivacao, 8);

		String valorAbatimento = "";
		valorAbatimento = funcao.completarZeros(valorAbatimento, 15);

		String valorRealEfetivado = "";
		valorRealEfetivado = funcao.completarZeros(valorRealEfetivado, 15);

		String informacao2 = funcao.preencheVazio(40);

		String finalidadeDoc = "00";

		String usoFebraban = funcao.preencheVazio(10);

		String avisoFavorecido = "0";

		String ocorrencias = funcao.preencheVazio(10);

		this.sbTextoBanco.append(codigoBanco + loteServico + codigoRegistro + NSR + codigoSegmento + tipoDocumento
				+ instrucaoDocumento + camaraCompensacao + codigoBancoDestino + codigoAgenciaDestino + dvAgenciaDestino
				+ operacaoContaCorrenteDestino + contaCorrenteDestino + dvContaDestino + dvAgenciaContaDestino
				+ nomeTerceiroFavorecido + numeroDocumentoEmpresa + filler13 + tipoContaFinalidadeTed + dataVencimento
				+ tipoMoeda + quantidadeMoeda + valorLancamento + numeroDocumento + filler3 + quantidadeParcelas
				+ indicadorBloqueio + indicadorFormaPagamento + diaVencimento + numeroParcela + dataEfetivacao
				+ valorRealEfetivado + informacao2 + finalidadeDoc + usoFebraban + avisoFavorecido + ocorrencias
				+ "\n");

	}

	public void detalheBMandado(RelatoriosMandadoDt objDt) throws Exception {

		String codigoBanco = this.banco;

		String loteServico = funcao.completarZeros(String.valueOf(this.sequenciaLote), 4);

		String codigoRegistro = "3";

		String NSR = funcao.completarZeros(String.valueOf(++this.sequenciaRegistroLote), 5);

		String codigoSegmento = "B";

		String usuFebraban = funcao.preencheVazio(3);

		String tipoInscricao = "1";

		objDt.setCpfUsuario(objDt.getCpfUsuario().replace(".", ""));
		objDt.setCpfUsuario(objDt.getCpfUsuario().replace("-", ""));
		String numeroInscricao = funcao.completarZeros(objDt.getCpfUsuario(), 14);

		String logradouro = funcao.completarEspacosDireita("AV. ASSIS CHATEUBRIAND", 30);

		String numeroLocal = "00195";

		String complemento = funcao.completarEspacosDireita("PG LOCOMO", 15);

		String bairro = funcao.completarEspacosDireita("SETOR OESTE", 15);

		String cidade = funcao.completarEspacosDireita("GOIANIA", 20);

		String cep = "74130";

		String complementoCep = "012";

		String uf = "GO";

		String dataVencimento = tjDataHora.getDataFormatadaddMMyyyySemSeparador();

		String valorDocumento = "";
		valorDocumento = funcao.completarZeros(valorDocumento, 15);

		String valorAbatimento = "";
		valorAbatimento = funcao.completarZeros(valorAbatimento, 15);

		String valorDesconto = "";
		valorDesconto = funcao.completarZeros(valorDesconto, 15);

		String valorMora = "";
		valorMora = funcao.completarZeros(valorMora, 15);

		String valorMulta = "";
		valorMulta = funcao.completarZeros(valorMulta, 15);

		String codigoMovimentoFavorecido = funcao.preencheVazio(15);

		String usuFebraban1 = funcao.preencheVazio(15);

		this.sbTextoBanco.append(codigoBanco + loteServico + codigoRegistro + NSR + codigoSegmento + usuFebraban
				+ tipoInscricao + numeroInscricao + logradouro + numeroLocal + complemento + bairro + cidade + cep
				+ complementoCep + uf + dataVencimento + valorDocumento + valorAbatimento + valorDesconto + valorMora
				+ valorMulta + codigoMovimentoFavorecido + usuFebraban1 + "\n");
		this.totalGeralLinhas++;

	}

	public void trailerLoteMandado() throws Exception {

		String codigoBanco = this.banco;
		String loteServico = funcao.completarZeros(String.valueOf(this.sequenciaLote), 4);

		String codigoRegistro = "5";

		String febraban = funcao.preencheVazio(9);

		String quantidadeRegistrosLote = funcao.completarZeros(String.valueOf(++this.sequenciaRegistroLote), 6);

		String somatorioValores = funcao.FormatarBigDecimal(String.valueOf(this.somatorioValorLote));
		somatorioValores = somatorioValores.replace(",", "");
		somatorioValores = somatorioValores.replace(".", "");
		somatorioValores = funcao.completarZeros(somatorioValores, 18);

		String somatorioQuantidadeMoeda = "";
		somatorioQuantidadeMoeda = funcao.completarZeros(somatorioQuantidadeMoeda, 18);

		String numeroAvisoDebito = "";
		numeroAvisoDebito = funcao.completarZeros(numeroAvisoDebito, 6);

		String febraban1 = funcao.preencheVazio(165);

		String ocorrencias = funcao.preencheVazio(10);

		this.sbTextoBanco.append(codigoBanco + loteServico + codigoRegistro + febraban + quantidadeRegistrosLote
				+ somatorioValores + somatorioQuantidadeMoeda + numeroAvisoDebito + febraban1 + ocorrencias + "\n");
		this.totalGeralLinhas++;

	}

	public void trailerBancoMandado() throws Exception {

		final String codigoBanco = this.banco;

		String loteServico = "9999";

		String codigoRegistro = "9";

		String febraban = funcao.preencheVazio(9);

		String quantidadeLotes = funcao.completarZeros(String.valueOf(this.sequenciaLote), 6);

		String quantidadeRegistros = funcao.completarZeros(String.valueOf(++this.totalGeralLinhas), 6);

		String quantidadeContasConciliacao = "";
		quantidadeContasConciliacao = funcao.completarZeros(quantidadeContasConciliacao, 6);

		String febraban1 = funcao.preencheVazio(205);

		this.sbTextoBanco.append(codigoBanco + loteServico + codigoRegistro + febraban + quantidadeLotes
				+ quantidadeRegistros + quantidadeContasConciliacao + febraban1 + "\n");

	}

}
