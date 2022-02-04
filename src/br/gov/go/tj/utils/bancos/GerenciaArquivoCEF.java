package br.gov.go.tj.utils.bancos;

import java.io.Serializable;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.ne.GerenciaArquivoBancoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.GuiaSSGNe;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

/**
 * Classe de leitura e criação de arquivo aplicando o leiaute do Banco Caixa Econômica Federal
 * para as movimentações financeiras.
 * @author fasoares
 */
public class GerenciaArquivoCEF extends GerenciaArquivo implements Serializable {

	private static final long serialVersionUID = -6926750856667346593L;
	
	private String NumeroGuia;
	private Double ValorPago;
	private TJDataHora DataHoraRecebimento;
	private String NumeroBancoPagamento;
	private String NumeroAgenciaPagamento;
	private TJDataHora DataHoraGeracao;
	
	public GerenciaArquivoCEF(FabricaConexao obFabricaConexao) {
		super(obFabricaConexao);
	}
	
	public GerenciaArquivoCEF() {
		super(null);
	}
	
	/**
	 * Método para ler o arquivo aplicando o layout da CEF.
	 * @param String conteudo
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int lerDados(String conteudoLinha) throws Exception {
		int retorno = GerenciaArquivoBancoNe.SEM_SUCESSO;
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		
		if( conteudoLinha != null && conteudoLinha.length() > 0 ) {
			
			int tipoDeRegistro = Funcoes.StringToInt(conteudoLinha.substring(LeiauteEscritaArquivoCEF.INICIO_TIPO_REGISTRO, LeiauteEscritaArquivoCEF.FIM_TIPO_REGISTRO).trim(), -1);
			
			if (tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_HEADER_ARQUIVO) {
				// Data de Geração do Arquivo | 144 | 151 | 9(008) | Formato DDMMAAAA (Dia, Mês e Ano) | G016
				String textoDataGeracao = conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_DATA_GERACAO, LeiauteLeituraArquivoCEF.FIM_DATA_GERACAO).trim();
				
				// Hora de Geração do Arquivo | 152 | 157 | 9(006) | Formato HHMMSS (Hora, Minuto e Segundos) | G017
				String textoHoraGeracao = conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_HORA_GERACAO, LeiauteLeituraArquivoCEF.FIM_HORA_GERACAO).trim();
				
				this.DataHoraGeracao = new TJDataHora();
				this.DataHoraGeracao.setDataddMMaaaaHHmmssSemSeparador(textoDataGeracao + textoHoraGeracao);				
				
			} else if (tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_DETALHE) {
				
				String tipoDeRegistroDetalhe = conteudoLinha.substring(LeiauteEscritaArquivoCEF.INICIO_TIPO_REGISTRO_DETALHE, LeiauteEscritaArquivoCEF.FIM_TIPO_REGISTRO_DETALHE).trim();
				
				if (tipoDeRegistroDetalhe.trim().equalsIgnoreCase("T")) {
					//15.3T | Número Documento (Seu Nº) | Número do Documento de Cobrança | 59 | 69 | X(011) | Seu Número do documento, conforme informado pelo Beneficiário | C011
					NumeroGuia = conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_NUMERO_DOCUMENTO, LeiauteLeituraArquivoCEF.FIM__NUMERO_DOCUMENTO).trim();	
				
					//17.3T | Valor do Título | Valor Nominal do Título | 82 | 96 | 9(013) | Valor nominal do título, com duas casas decimais | G070
					String textoValorPago 	= conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_VALOR_RECEBIDO, LeiauteLeituraArquivoCEF.FIM_VALOR_RECEBIDO).trim();
					ValorPago 		= Funcoes.StringToInt(textoValorPago) / 100.0;
					
					//18.3T | Banco Cobrador/Recebedor | Código do Banco | 97 | 99 | 9(003) | Código de identificação do Banco recebedor do pagamento do título; informado somente quando a liquidação for realizada em outro banco; se liquidado na CAIXA, virá zerado | C045
					NumeroBancoPagamento = conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_CODIGO_BANCO, LeiauteLeituraArquivoCEF.FIM_CODIGO_BANCO).trim();
					if (NumeroBancoPagamento.equals("000")) NumeroBancoPagamento = "104";
					
					//19.3T |  Agência Cobradora/Recebedora | Código da Agência Cobr/Receb | 100 | 104 | 9(005) | Código da agência CAIXA recebedora do título | C086
					NumeroAgenciaPagamento = conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_CODIGO_AGENCIA, LeiauteLeituraArquivoCEF.FIM_CODIGO_AGENCIA).trim();
					
				} else if (tipoDeRegistroDetalhe.trim().equalsIgnoreCase("U")) {
					
					//16.3U | Data da Ocorrência | Data da Ocorrência | 138 | 145 | 9(008) | Data do evento que afetou o estado do título de cobrança, no formato DDMMAAAA (Dia, Mês e Ano) | C056
					String textoDataRecebimento = conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_DATA_RECEBIMENTO, LeiauteLeituraArquivoCEF.FIM_DATA_RECEBIMENTO).trim();
					this.DataHoraRecebimento = new TJDataHora();
					this.DataHoraRecebimento.setDataddMMaaaaSemSeparador(textoDataRecebimento);
					
					GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(NumeroGuia, obFabricaConexao);
					
					if (guiaEmissaoDt != null) {
						//Guia base projudi						
						guiaEmissaoNe.atualizaGuiaConsolidado(obFabricaConexao, guiaEmissaoDt, "", DataHoraRecebimento);						
						InserirRajadaSAJ(guiaSPGNe, guiaEmissaoDt);
					} else {
						//Guia base SPG
						guiaEmissaoDt = consultarGuiaEmissaoSPG(guiaSPGNe);						
						if (guiaEmissaoDt != null) {
							InserirRajadaSAJ(guiaSPGNe, guiaEmissaoDt);
						} else {
							//Guia base SSG
							guiaEmissaoDt = consultarGuiaEmissaoSSG(guiaSSGNe);							
							if (guiaEmissaoDt != null) {
								InserirRajadaSAJ(guiaSPGNe, guiaEmissaoDt);
							}
						}
					}
				}					
				
				retorno = GerenciaArquivoBancoNe.SUCESSO;					
			} 
		}
		
		return retorno;
	}

	private GuiaEmissaoDt consultarGuiaEmissaoSSG(GuiaSSGNe guiaSSGNe) throws Exception {
		return guiaSSGNe.consultarGuiaEmissaoSSG(NumeroGuia, obFabricaConexaoAdabas);	
	}

	private GuiaEmissaoDt consultarGuiaEmissaoSPG(GuiaSPGNe guiaSPGNe) throws Exception {
		return guiaSPGNe.consultarGuiaEmissaoSPG(NumeroGuia, obFabricaConexaoAdabas);	
	}

	private void InserirRajadaSAJ(GuiaSPGNe guiaSPGNe, GuiaEmissaoDt guiaEmissaoDt) throws MensagemException, Exception {
		try {
			guiaSPGNe.inserirPagamentoRajadaGuiaSPGSAJ(guiaEmissaoDt, DataHoraGeracao, ValorPago, super.obFabricaConexaoAdabas);
		} catch(Exception e) {
			guiaSPGNe.inserirPagamentoRajadaGuiaSPGSAJ(guiaEmissaoDt, DataHoraGeracao, ValorPago, null);	
		}		
	}
}
