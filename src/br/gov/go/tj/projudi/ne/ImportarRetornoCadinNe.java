package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.ImportarRetornoCadinDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoCadinDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoCadinPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class ImportarRetornoCadinNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8674050097179322654L;
	
	private enum EnumDataImportacao
	{
		CODIGO,
		DEVEDOR,
		CPF_CNPJ,
		VALOR_DEVIDO,
		DATA_VENCIMENTO, 
		NUMERO_PROCESSO,
		NATUREZA_PENDENCIA,
		CATEGORIA_PENDENCIA,
		OBSERVACAO,
		DATA_INCLUSAO,
		SITUACAO,
		DATA_SITUACAO,
		MOTIVO,
		OBSERVACAO_ATUALIZACAO,
		LENGHT
	}
	
	protected  ProcessoParteDebitoCadinDt obDados;

	public ImportarRetornoCadinNe() {
		obLog = new LogNe(); 

		obDados = new ProcessoParteDebitoCadinDt(); 
	}
	
	public String Verificar(ImportarRetornoCadinDt dados) {
		String stRetorno = "";

		if (dados.getNomeArquivo() == null || dados.getNomeArquivo().length() == 0 ||
			dados.getConteudoArquivo() == null || dados.getConteudoArquivo().length() == 0) {
			stRetorno += "Selecione um arquivo para a importação. \n";
		} else if (!dados.getNomeArquivo().trim().toLowerCase().endsWith(".csv")) {
			stRetorno += "A extensão do arquivo deve ser CSV. \n";
		} else if (!dados.getConteudoArquivo().contains("\t")) {
			stRetorno += "O separador do arquivo deve ser {TAB}. \n";
		} else {
			String[] linhas = dados.getConteudoArquivo().split("\n");
			if (linhas.length == 0) {
				stRetorno += "O arquivo não possui linhas. \n";
			} else {
				String[] vetor = linhas[0].split("\t");
				if (vetor == null || vetor.length != EnumDataImportacao.LENGHT.ordinal()) {
					stRetorno += "Layout inválido. Deve ser: Código | Devedor | CPF/CNPJ | Valor Devido | Data Vencimento | Número Processo | Natureza Pendência | Categoria Pendência | Observação | Data Inclusão | Situação | Data Situação | Motivo | Observação Atualização";
				}
			}
		}
		
		return stRetorno;
	}
	
	public ImportarRetornoCadinDt Importar(ImportarRetornoCadinDt arquivo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);		
		try{
			
			obFabricaConexao.iniciarTransacao();

			arquivo = this.Importar(arquivo, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
			
			return arquivo;
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	private ImportarRetornoCadinDt Importar(ImportarRetornoCadinDt arquivo, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoNe processoParteDebitoNe = new ProcessoParteDebitoNe();
		ProcessoParteDebitoFisicoNe processoParteDebitoFisicoNe = new ProcessoParteDebitoFisicoNe();
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		ProcessoNe processoNe = new ProcessoNe();
		LogNe logNe = new LogNe();
		StringBuilder retorno = new StringBuilder();
		String[] linhas = null; // Vetor que armazena as linhas
		String[] vetor = null; // Vetor que armazena os dados de cada linha
		String linha = null;
		String novaLinha = null;
		LogDt logDt = new LogDt(arquivo.getId_UsuarioLog(), arquivo.getIpComputadorLog());		
		
		linhas = arquivo.getConteudoArquivo().split("\n");
		
		for (int i = 0; i < linhas.length; i++) {
			linha = linhas[i];
			novaLinha = Funcoes.removerQuebrasDeLinha(linha) + "\t";
						
			vetor = linha.split("\t");
			if (vetor != null && vetor.length == EnumDataImportacao.LENGHT.ordinal()) {
				if (Funcoes.StringToLong(vetor[EnumDataImportacao.CODIGO.ordinal()]) > 0) {
					novaLinha += this.Importar(vetor[EnumDataImportacao.SITUACAO.ordinal()],
							                   vetor[EnumDataImportacao.DATA_SITUACAO.ordinal()], 
							                   vetor[EnumDataImportacao.NUMERO_PROCESSO.ordinal()], 
							                   vetor[EnumDataImportacao.DEVEDOR.ordinal()],
							                   vetor[EnumDataImportacao.CPF_CNPJ.ordinal()],
							                   vetor[EnumDataImportacao.VALOR_DEVIDO.ordinal()],
							                   vetor[EnumDataImportacao.OBSERVACAO_ATUALIZACAO.ordinal()],
							                   logDt,							               
							                   processoParteDebitoNe,
							                   processoParteDebitoFisicoNe,
							                   guiaEmissaoNe,
							                   processoNe,
							                   logNe,
							                   obFabricaConexao);
				} else {
					novaLinha += "Resultado PJD";
				}
			} else {
				throw new MensagemException("Layout inválido. Deve ser: Código | Devedor | CPF/CNPJ | Valor Devido | Data Vencimento | Número Processo | Natureza Pendência | Categoria Pendência | Observação | Data Inclusão | Situação | Data Situação | Motivo | Observação Atualização");				
			}						
			retorno.append(novaLinha + "\n");
		}		
		arquivo.setConteudoArquivoRetorno(retorno.toString());		
		
		return arquivo;
	}
	
	public String Importar(String situacao, 
			               String dataPagamento, 
			               String numeroProcesso, 
			               String nomeParte, 
			               String cpfCnpjParte,
			               String valorDebito, 
			               String observacao,
			               LogDt logDt,
			               ProcessoParteDebitoNe processoParteDebitoNe,
			               ProcessoParteDebitoFisicoNe processoParteDebitoFisicoNe,
			               GuiaEmissaoNe guiaEmissaoNe,
			               ProcessoNe processoNe,
						   LogNe logNe,
			               FabricaConexao obFabricaConexao) throws Exception {
		
		if (Funcoes.isStringDiferente(situacao, "BAIXADO")) {
			return "A situação do débito não está baixada. Situação informada na planilha " + situacao + ".";
		}
		
		boolean baixouDebito = ImportarProcessoParteDebito(dataPagamento, numeroProcesso, nomeParte, cpfCnpjParte, valorDebito , observacao, logDt, processoParteDebitoNe, guiaEmissaoNe, processoNe, logNe, obFabricaConexao);
		
		if (!baixouDebito) {
			baixouDebito = ImportarProcessoParteDebitoFisico(dataPagamento, numeroProcesso, nomeParte, cpfCnpjParte, valorDebito, observacao, logDt, processoParteDebitoNe, guiaEmissaoNe, processoNe, logNe, obFabricaConexao);
		}
		
		if (baixouDebito)
			return "Débito baixado com sucesso.";
				
		return "Débito não encontrado com os dados informados.";
	}

	private boolean ImportarProcessoParteDebito(String dataPagamento, 
			                                    String numeroProcesso, 
			                                    String nomeParte,
			                                    String cpfCnpjParte,
			                                    String valorDebito,
			                                    String observacao,
			                                    LogDt logDt,
											    ProcessoParteDebitoNe processoParteDebitoNe, 
											    GuiaEmissaoNe guiaEmissaoNe,
											    ProcessoNe processoNe,
											    LogNe logNe,
											    FabricaConexao obFabricaConexao) throws Exception {
		
		boolean baixouDebito = false;
		
		String id_processo = processoNe.getIdDoProcesso(numeroProcesso);
		List<ProcessoParteDebitoDt> listaProcessoParteDebito = processoParteDebitoNe.consultarListaIdProcesso(id_processo, obFabricaConexao);
		if (listaProcessoParteDebito != null) {
			for (ProcessoParteDebitoDt processoParteDebito : listaProcessoParteDebito) {
				if (processoParteDebito.isEnviadoCadin()) {	
					
					// tenta buscar o débito utilizando o cpf/cnpj da parte...
					boolean podeBaixar = (processoParteDebito.getCpfParte() != null && cpfCnpjParte != null &&
						                  Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(processoParteDebito.getCpfParte()), 14).equalsIgnoreCase(
						                  Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(cpfCnpjParte), 14)));					
					
					if (!podeBaixar) {
						// Se não encontrou pelo cpf/cnpj, tenta localizar pelo nome
						podeBaixar = (Funcoes.converteNomeSimplificado(Funcoes.removeTodoEspacos(processoParteDebito.getNome())).equalsIgnoreCase(Funcoes.converteNomeSimplificado(Funcoes.removeTodoEspacos(nomeParte))));
						
					}
					
					if (!podeBaixar) {
						// Se não encontrou pelo nome e cpf/cnpj, tenta localizar pelo valor
						podeBaixar = (processoParteDebito.getValorTotalGuia() != null && valorDebito != null &&
								      Funcoes.StringToDouble(processoParteDebito.getValorTotalGuia().trim()) == Funcoes.StringToDouble(valorDebito.trim()));
					}
					
					if (podeBaixar) {
						ProcessoParteDebitoDt dadosAnteriores = new ProcessoParteDebitoDt();
						dadosAnteriores.copiar(processoParteDebito);
						processoParteDebitoNe.atualizeDadosAnteriores(dadosAnteriores);
						
						processoParteDebito.setDataBaixa(dataPagamento);
						processoParteDebito.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.BAIXADO));
						processoParteDebito.setObservacaoProcessoDebitoStatus(observacao);
						processoParteDebito.setIpComputadorLog(logDt.getIpComputadorLog());
						processoParteDebito.setId_UsuarioLog(logDt.getId_UsuarioLog());
						
						processoParteDebitoNe.salvar(processoParteDebito, obFabricaConexao);
						baixouDebito = true;
						if (processoParteDebito.getId_GuiaEmissao() != null && 
							processoParteDebito.getId_GuiaEmissao().trim().length() > 0) {
							GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarId(processoParteDebito.getId_GuiaEmissao(), obFabricaConexao);
							if (guiaEmissaoDt != null && guiaEmissaoDt.isGuiaAguardandoPagamento()) {							
								TJDataHora dataPagamentoTJ = new TJDataHora();
								dataPagamentoTJ.setDataaaaa_MM_ddHHmmss(dataPagamento);
								
								guiaEmissaoNe.atualizarPagamento(guiaEmissaoDt.getNumeroGuiaCompleto(), GuiaStatusDt.PAGO_DARE_CADIN, dataPagamentoTJ.getDate(), obFabricaConexao);
								LogDt obLogDt = new LogDt("GuiaEmissao", processoParteDebito.getId_GuiaEmissao(), logDt.getId_UsuarioLog(), logDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),"[Id_GuiaEmissao:" + processoParteDebito.getId_GuiaEmissao() + ";DataRecebimento:" + dataPagamentoTJ.getDataFormatadaddMMyyyy() + ";numeroGuiaCompleto:" + guiaEmissaoDt.getNumeroGuiaCompleto() + ";GuiaStatusCodigo:" + GuiaStatusDt.AGUARDANDO_PAGAMENTO + "]", "[Id_GuiaEmissao:" + processoParteDebito.getId_GuiaEmissao() + ";DataRecebimento:" + dataPagamentoTJ.getDataFormatadaddMMyyyy() + ";numeroGuiaCompleto:" + guiaEmissaoDt.getNumeroGuiaCompleto() + ";GuiaStatusCodigo:" + GuiaStatusDt.PAGO_DARE_CADIN + "]");
								logNe.salvar(obLogDt, obFabricaConexao);
							}
						}
					}
				}	
			}
		}
		
		return baixouDebito;
	}
	
	private boolean ImportarProcessoParteDebitoFisico(String dataPagamento, 
											          String numeroProcesso, 
											          String nomeParte,
											          String cpfCnpjParte,
											          String valorDebito,
											          String observacao,
											          LogDt logDt,
													  ProcessoParteDebitoNe processoParteDebitoNe, 
													  GuiaEmissaoNe guiaEmissaoNe,
													  ProcessoNe processoNe,
													  LogNe logNe,
													  FabricaConexao obFabricaConexao) throws Exception {

		boolean baixouDebito = false;
		
		return baixouDebito;
	}
}
