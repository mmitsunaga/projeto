package br.gov.go.tj.projudi.ne;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.indexacao.ResponseHits;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ParserUtil;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * Faz a indexa��o de uma lista de arquivos para o Elasticsearch.
 * Tira o conte�do (somente texto) dos anexos e envia para o servidor do elasticsearch
 * @author mmitsunaga
 *
 */
public class IndexadorNe extends Negocio {
	
	private static final long serialVersionUID = -7106581511208112276L;
	
	private static final Pattern TAG_REGEX = Pattern.compile("<p class=\"ementa\".*>(.+?)</p>", Pattern.CASE_INSENSITIVE);
	
	private final Pattern TAG_REGEX2 = Pattern.compile("[E][M][E][N][T][A][:]*", Pattern.CASE_INSENSITIVE);
	
	private final Pattern TAG_REGEX3 = Pattern.compile("[Aa][Cc][��oO][Rr][Dd][��aA][Oo]\\s+[Vv][Ii][Ss][Tt][Oo][Ss]*", Pattern.CASE_INSENSITIVE);
	
	private final Pattern TAG_REGEX4 = Pattern.compile("[Aa]\\s+[Cc]\\s+[��oO]\\s+[Rr]\\s+[Dd]\\s+[��aA]\\s+[Oo]", Pattern.CASE_INSENSITIVE);

	public IndexadorNe() {
		obLog = new LogNe();
	}
	
	/**
	 * Faz a indexa��o de um arquivo individual. Extrai o conte�do do arquivo e faz um POST para o elasticsearch
	 * @param id - identificador da tabela ARQ
	 */
	public boolean indexar(String id){
		if (ValidacaoUtil.isNaoNulo(id)){
			try {
				// Consulta arquivo na movimenta��o (ARQ e MOVI_ARQ)
				MovimentacaoArquivoDt dadosDt =  new MovimentacaoArquivoNe().consultarEntidadePorIdArquivo(id, null);
				if (ValidacaoUtil.isNaoNulo(dadosDt)){
					// Verifica se o conte�do do arquivo foi obtido pelo banco. Caso contr�rio, pegar do CEPH
					ArquivoDt arquivo = dadosDt.getArquivoDt();
					if (!arquivo.isConteudo()) dadosDt.setArquivoDt(new ArquivoNe().consultarId(arquivo.getId()));
					return indexarDocumento(dadosDt, null, null);
				} else {
					// Consulta o arquivo na pend�ncia (ARQ e PEND_ARQ)
					dadosDt =  new MovimentacaoArquivoNe().consultarArquivoPublicadoForaProcesso(id, null);
					if (ValidacaoUtil.isNaoNulo(dadosDt)) {
						// Verifica se o conte�do do arquivo foi obtido pelo banco. Caso contr�rio, pegar do CEPH
						ArquivoDt arquivo = dadosDt.getArquivoDt();
						if (!arquivo.isConteudo()) dadosDt.setArquivoDt(new ArquivoNe().consultarId(arquivo.getId()));
						return indexarDocumento(dadosDt, null, null);
					}
				}
			} catch (Exception ex){
				// Se houver algum erro na comunica��o do elasticsearch, n�o fazer nada. 
				// Evitar que os m�todos de neg�cio sejam interrompidos por problema no elasticsearch. 
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Faz a indexa��o de uma lista de arquivos. Extrai o conte�do do arquivo e faz um POST para o elasticsearch
	 * @param arquivos
	 * @throws Exception
	 */
	public void indexar(List<Integer> arquivos) {
		for (Integer id : arquivos){
			try {
				indexar(String.valueOf(id));
			} catch (Exception ex){
				continue;
			}
		}		
	}
		
	/**
	 * Faz a indexa��o de um arquivo individual. Extrai o conte�do do arquivo e faz um POST para o elasticsearch
	 * Como existe uma transa��o ainda n�o finalizada, a lista de arquivos ir� conter tanto o relat�rio e a ementa.
	 * Dessa forma, n�o � necess�rio fazer a busca em banco, apenas percorrer a lista e obter os dados da ementa.
	 * M�todo usado dentro de PendenciaArquivoNe();
	 * @param id - identificador da tabela ARQ
	 * @param arquivos - Lista de arquivos (ArquivoDt) apensados � conclus�o
	 * @param fabricaConexao - conex�o em aberto do banco de dados
	 */
	public void indexar(String id, List<ArquivoDt> arquivos, FabricaConexao fabricaConexao){
		if (ValidacaoUtil.isNaoNulo(id)){
			try {
				// Consulta arquivo na movimenta��o (ARQ e MOVI_ARQ)
				MovimentacaoArquivoDt dadosDt =  new MovimentacaoArquivoNe().consultarEntidadePorIdArquivo(id, fabricaConexao);
				if (ValidacaoUtil.isNaoNulo(dadosDt)) {
					// Verifica se o conte�do foi obtido pelo banco. Caso contr�rio, pegar do CEPH
					ArquivoDt arquivo = dadosDt.getArquivoDt();
					if (!arquivo.isConteudo()) dadosDt.setArquivoDt(new ArquivoNe().consultarId(arquivo.getId(), fabricaConexao.getConexao()));					
					indexarDocumento(dadosDt, arquivos, fabricaConexao);
				} else {
					// Consulta o arquivo na pend�ncia (ARQ e PEND_ARQ)
					dadosDt =  new MovimentacaoArquivoNe().consultarArquivoPublicadoForaProcesso(id, fabricaConexao);
					if (ValidacaoUtil.isNaoNulo(dadosDt)) {
						// Verifica se o conte�do do arquivo foi obtido pelo banco. Caso contr�rio, pegar do CEPH
						ArquivoDt arquivo = dadosDt.getArquivoDt();
						if (!arquivo.isConteudo()) dadosDt.setArquivoDt(new ArquivoNe().consultarId(arquivo.getId(), fabricaConexao.getConexao()));
						indexarDocumento(dadosDt, arquivos, fabricaConexao);
					}
				}
			} catch (Exception ex){
				// Se houver algum erro na comunica��o do elasticsearch, n�o fazer nada. 
				// Evitar que os m�todos de neg�cio sejam interrompidos por problema no elasticsearch. 
				return;
			}
		}
	}
	
	/**
	 * Faz a indexa��o de um arquivo. Extrai o conte�do faz um POST para o Elasticsearch. Atualiza a data_indexacao em ARQ
	 * @param movimentacaoArquivoDt - informa��es sobre o arquivo
	 * @param fabricaConexao - conex�o em aberto do banco de dados
	 */
	private boolean indexarDocumento(MovimentacaoArquivoDt movimentacaoArquivoDt, List<ArquivoDt> arquivos, FabricaConexao fabricaConexao) throws Exception {
		
		ElasticSearchNe elasticsearchNe = new ElasticSearchNe();
		ArquivoIndexacaoNe arquivoIndexacaoNe = new ArquivoIndexacaoNe();
		
		ArquivoDt arq = movimentacaoArquivoDt.getArquivoDt();
		
		if (ValidacaoUtil.isNaoNulo(arq)){
			
			if (arq.isConteudo()){
				
				byte[] out = arq.getConteudo();
				
				String texto = null;
				
				if (arq.getContentType().equals("application/pdf")){
					texto = ParserUtil.parsePdfToPlainText(out);
					
				} else if (arq.getContentType().equals("text/html")){
					texto = ParserUtil.parseHtmlToPlainText(out);
					
				} else {
					texto = ParserUtil.parseToPlainText(out);
				}

				if (ValidacaoUtil.isNaoVazio(texto)){
						    				
					MovimentacaoDt mov = movimentacaoArquivoDt.getMovimentacaoDt();
					
					ProcessoDt proc = mov.getProcessoDt();
					
					if (!isExists(arq.getId(), proc.getProcessoNumeroCompleto())){
						
						JSONObject json = new JSONObject();
	        			json.put("id_arq", arq.getId());
	        			json.put("id_movi", mov.getId());
	        			json.put("id_serv", proc.getId_Serventia());
	        			json.put("serv", proc.getServentia());
	        			json.put("id_usu", mov.getId_UsuarioRealizador());
	        			json.put("realizador", mov.getUsuarioRealizador());
	        			json.put("id_tipo_arq", arq.getId_ArquivoTipo());
	        			json.put("tipo_arq", arq.getArquivoTipo());
	        			json.put("numero_processo", proc.getProcessoNumeroCompleto());
	        			json.put("data_publicacao", mov.getDataRealizacao());
	        			json.put("data_indexacao", Funcoes.FormatarDataHora(new Date()));
	        			json.put("texto", Funcoes.normalizarEspacoEmBrancoEntreTexto(texto.trim().replaceAll("\n|\r|\t|\\s{2,}", " ")));
	        			json.put("extra", getConteudoEmenta(arq, arquivos, fabricaConexao));
	        								
	        			if (elasticsearchNe.salvarArquivoIndexado(json) == HttpStatus.SC_CREATED){
	        				
	        				// Preenche a data de indexa��o na tabela ARQ
	        		    	arquivoIndexacaoNe.alterarDataIndexacao(arq.getId(), fabricaConexao);
	        		    	
	        		    	return true;
	        			}
	        			
					}
					
				}
				
			}
									
		}
		
		return false;
	}
	
	/**
	 * Verifica se j� existe o json com o id_arq e n�mero do processo no elasticsearch.
	 * @param id - identificador do arquivo ARQ
	 * @param numero - n�mero do processo
	 * @return true/false se encontrou o arquivo
	 */
	public boolean isExists(String id, String numero){
		boolean exists = false;
		try {
			ElasticSearchNe elasticSearchNe = new ElasticSearchNe();
			ResponseHits responseHits = elasticSearchNe.consultarPublicacoesPorIdArquivoENumeroProcesso(id, numero);
			exists = responseHits != null && responseHits.getHits().getTotal() > 0;
			responseHits = null;
			elasticSearchNe = null;
		} catch (ClientProtocolException e) {			
		} catch (IOException e) {}
	    return exists;
	}
	
	/**
	 * Obt�m apenas o texto do arquivo, retirando marca��es html e de estilo css
	 * Retira metadados de PDF.
	 * @return
	 */
	private String getConteudoArquivo(ArquivoDt arq) throws Exception {
		String texto = null;
		if (ValidacaoUtil.isNaoNulo(arq)){
			byte[] out = arq.getConteudo();
			if (arq.getContentType().equals("application/pdf")){
				texto = ParserUtil.parsePdfToPlainText(out);
			} else if (arq.getContentType().equals("text/html")){
				texto = ParserUtil.parseHtmlToPlainText(out);
			} else {
				texto = ParserUtil.parseToPlainText(out);
			}
		}
		return texto;
	}
	
	/**
	 * Obt�m o conte�do do arquivo de tipo Ementa.
	 * Se � uma publica��o normal, o arquivo n�o tem ementa.
	 * Se � uma ementa de Turma Recursal, o conte�do da ementa est� dentro do proprio inteiro teor
	 * Se � uma ementa de C�mara 2o Grau, o conte�do ficar� em outro arquivo. 
	 * @param arq
	 * @param arquivos
	 * @return
	 * @throws Exception
	 */
	public String getConteudoEmenta(ArquivoDt inteiroTeor, List<ArquivoDt> arquivos, FabricaConexao conexao) throws Exception {
		StringBuilder texto = new StringBuilder();
		int tipoArquivo = Integer.parseInt(inteiroTeor.getArquivoTipoCodigo());
	    if (tipoArquivo == ArquivoTipoDt.RELATORIO_VOTO){
	    	ArquivoDt ementa = null;
	    	if (ValidacaoUtil.isNaoVazio(arquivos)){
	    		ementa = arquivos.stream().filter(a -> isArquivoTipoEmenta(a)).findFirst().orElse(null);
	    		texto.append(getConteudoArquivo(ementa));
	    	} else {
	    		ArquivoNe arquivoNe = new ArquivoNe();
	    		ementa = arquivoNe.consultarArquivoEmentaPorIdRelatorioVoto(inteiroTeor.getId(), conexao);
	    		if (!ementa.isConteudo()) ementa = arquivoNe.consultarId(ementa.getId(), conexao.getConexao()); 
	    		texto.append(getConteudoArquivo(ementa));
	    	}
	    }
	    if (ValidacaoUtil.isVazio(texto)){
	    	// M�todo 2: procurar a tag <ementa> e obter seu conte�do
	    	byte[] out = inteiroTeor.getConteudo();
			String str = new String(out, StandardCharsets.ISO_8859_1);
		    final Matcher matcher = TAG_REGEX.matcher(str);
		    while (matcher.find()){
		    	str = matcher.group();
		    	str = StringEscapeUtils.unescapeHtml(str);
				str = str.replaceAll("(?m)^\\s+$","");
				str = str.replaceAll("", "");
				str = str.replaceAll("_", "");
				texto.append(str.replaceAll("\\<[^>]*>",""));
		    }
	    }
	    if (ValidacaoUtil.isVazio(texto)){
	    	// M�todo 3: Procurar o termo "Ementa:" e "Ac�rd�o Visto" e obter seu conte�do
	    	int indexIni = -1;
			int indexFim = -1;				
			String textoEmenta = getConteudoArquivo(inteiroTeor);
			if (ValidacaoUtil.isNaoVazio(textoEmenta)){
				textoEmenta = textoEmenta.replaceAll("_|'", "");
				textoEmenta = textoEmenta.replaceAll("\n|\r|\t|\\s{2,}", " ");
				textoEmenta = Funcoes.normalizarEspacoEmBrancoEntreTexto(textoEmenta);
				// Procura o in�cio da ementa pelo termo "EMENTA:"
				Matcher matcherIni = TAG_REGEX2.matcher(textoEmenta);
			    if (matcherIni.find()) indexIni = matcherIni.start();
				// Procura o fim do texto da ementa pelo termo em TAG_REGEX3 e TAG_REGEX4
				Matcher matcherFim = TAG_REGEX3.matcher(textoEmenta);
			    if (matcherFim.find()) indexFim = matcherFim.start();
			    matcherFim = TAG_REGEX4.matcher(textoEmenta);
			    if (matcherFim.find()) indexFim = matcherFim.start();
			    // Se encontrou o inicio e o fim e o fim � maior que o inicio, retornar a substring
			    if ((indexIni > -1 && indexFim > -1) && (indexIni < indexFim)){
			    	texto.append(textoEmenta.substring(indexIni, indexFim).trim());
			    }
			}			
	    }
		return ValidacaoUtil.isNaoVazio(texto) ? texto.toString().trim().replaceAll("\n|\r|\t|\\s{2,}", " ") : null;
	}
	
	/**
	 * Verifica se o c�digo passado � do tipo EMENTA
	 * @param a
	 * @return
	 */
	private boolean isArquivoTipoEmenta(ArquivoDt a){
		return Integer.parseInt(a.getId_ArquivoTipo()) == ArquivoTipoDt.EMENTA;
	}
	
}
