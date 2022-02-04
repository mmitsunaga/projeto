package br.gov.go.tj.projudi.ne;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ComunicacaoDJEDt;
import br.gov.go.tj.projudi.dt.DiarioJusticaEletronicoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoIntimacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.DiarioJusticaEletronicoPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ParserUtil;
import br.gov.go.tj.utils.ValidacaoUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * Classe responsável em gerar os arquivos do Diário Eletrônico
 * Será feito pelo Execução automática do projudi.
 * @author mmitsunaga
 *
 */
public class DiarioJusticaEletronicoNe implements Serializable {
	
	private static final long serialVersionUID = 6294911650297290459L;
	
	private final int CODIGO_OPCAO_PUBLICACAO_2_GRAU = 1;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL = 2;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR = 3;
	
	private final String LOG_TABELA = "GerarArquivoDJE";
	
	private final String PASTA_SAIDA = "D://"; // diretório compartilhado na rede
	
	private static final String JTR = "809";
	
	/**
	 * Faz a geração do arquivo PDF das intimações do 2o Grau na data de hoje
	 * @param data - Data da publicação específica, no formato: dd/mm/yyyy
	 * @throws Exception
	 */
	public void gerarArquivoHojePara2oGrau(String data) throws Exception {		
		String dataPublicacao = ValidacaoUtil.isVazio(data) ? Funcoes.FormatarData(new Date(), "dd/MM/yyyy") : data;
		String pastaWebContent = getCaminhoPastaWebContent();
		this.gerarArquivo(dataPublicacao, CODIGO_OPCAO_PUBLICACAO_2_GRAU, PASTA_SAIDA, pastaWebContent);
	}
	
	/**
	 * Faz a geração do arquivo PDF das intimações do 1o Grau - Capital na data de hoje
	 * @throws Exception
	 */
	public void gerarArquivoHojePara1oGrauCapital(String data) throws Exception {
		String dataPublicacao = ValidacaoUtil.isVazio(data) ? Funcoes.FormatarData(new Date(), "dd/MM/yyyy") : data;
		String pastaWebContent = getCaminhoPastaWebContent();
		this.gerarArquivo(dataPublicacao, CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL, PASTA_SAIDA, pastaWebContent);
	}

	/**
	 * Faz a geração do arquivo PDF das intimações do 1o Grau Interior - na data de hoje
	 * @throws Exception
	 */
	public void gerarArquivoHojePara1oGrauInterior(String data) throws Exception {
		String dataPublicacao = ValidacaoUtil.isVazio(data) ? Funcoes.FormatarData(new Date(), "dd/MM/yyyy") : data;
		String pastaWebContent = getCaminhoPastaWebContent();
		this.gerarArquivo(dataPublicacao, CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR, PASTA_SAIDA, pastaWebContent);
	}
	
	/**
	 * Faz a geração do arquivo PDF das intimações de uma data
	 * @param data - data de publicação (data da pendencia de intimação / citação)
	 * @param opcao - Se é intimação do 2o grau, 1o grau capital ou interior
	 * @param pastaSaida - Local onde os arquivos serão gravados - usar pasta compartilhada da rede
	 * @param pastaWebContent - pasta onde contém a images/
	 */
	private void gerarArquivo (String dataPublicacao, int opcao, String pastaSaida, String pastaWebContent) throws Exception {		
		BufferedOutputStream bos = null;		
		try {
			System.out.println(getCaminhoPastaWebContent());
			long tempoInicial = System.currentTimeMillis();
			String dataInicial = dataPublicacao + " 00:00:00";
			String dataFinal = dataPublicacao + " 23:59:59";		
			System.out.println("Consultando dados para o filtro: [DataInicio=" + dataInicial + ", DataFim: " + dataFinal + ", Opcao=" + opcao + "]");
			gravarLogInicioProcessamento(dataInicial, dataFinal, opcao);
			PendenciaNe pendenciaNe = new PendenciaNe();	
			List<MovimentacaoIntimacaoDt> intimacoes = pendenciaNe.consultarIntimacoesParaPublicacao(dataInicial, dataFinal, opcao);
			if (intimacoes.size() != 0){
				System.out.println("Iniciando a geração do arquivo: " + intimacoes.size() + " registros encontrados...");
				FileOutputStream fos = new FileOutputStream(new File(pastaSaida + definirNomeArquivoPDF(dataPublicacao, opcao)));				
				HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) new CssAppliersImpl(new XMLWorkerFontProvider()));				
				bos = new BufferedOutputStream(fos);
				Document document = new Document(PageSize.A4, 50, 50, 50, 50);
				int index = 0;				
				String serventia = "";					
				ArrayList<HashMap<String, Object>> bookmarks = new ArrayList<HashMap<String, Object>>();					
				PdfSmartCopy copy = new PdfSmartCopy(document, bos);					
				document.open();					
				for (MovimentacaoIntimacaoDt m : intimacoes){
					pendenciaNe.gerarPdfIntimacaoPorData(m, hpc, pastaWebContent, copy, bookmarks, (!serventia.equalsIgnoreCase(m.getProcessoDt().getServentia())));
					serventia = m.getProcessoDt().getServentia();										
					index++;						
					System.out.println(index + "/" + intimacoes.size() + " - " + m.getProcessoDt().getProcessoNumeroCompleto() + " ... ok");						
				}
				copy.setOutlines(bookmarks);
				try { if (document!=null) document.close(); } catch(Exception ex ){};				
				fos.flush();
				fos.close();	
				bos.close();
			}
			System.out.println("Terminado.");
			gravarLogConcluido(dataInicial, dataFinal, opcao, intimacoes.size(), getTempoExecucao(tempoInicial));
		} catch (Exception ex){
			gravarLogErro(ex);
			try {if (bos!=null) bos.close(); } catch(Exception e) {};				
			throw ex;
		}
		
	}
	
	/**
	 * Monta o nome do arquivo de saída de acordo com a opção de geração
	 * @param dataPublicacao
	 * @param opcaoPublicacao
	 * @return
	 */
	private String definirNomeArquivoPDF(String dataPublicacao, int opcaoPublicacao){
		String tipo = "";
		if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_2_GRAU){
			tipo = "_SI_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL){
			tipo = "_SII_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR){
			tipo = "_SIII_";
		}
		return "Intimacao" + tipo + Funcoes.FormatarDataSemBarra(dataPublicacao) + ".pdf";
	}
	
	private String getTempoExecucao(long inicio){
    	long millis = System.currentTimeMillis() - inicio;
    	return String.format("%d min, %d seg", 
    			TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
	
	private void gravarLogInicioProcessamento(String dataIni, String dataFim, int opcao) throws Exception {
		String detalhes = "{\"dataInicio\":\"" + dataIni + "\", \"dataFim\":\"" + dataFim + "\", \"opcao\":\"" + opcao + "\"}";
		tenteGravarLog(LOG_TABELA, "Início do processamento.", detalhes);
	}
	
	private void gravarLogConcluido(String dataIni, String dataFim, int opcao, int total, String tempoDuracao) throws Exception {		
		String detalhes = "{\"dataInicio\":\"" + dataIni + "\", \"dataFim\":\"" + dataFim + 
				"\", \"opcao\":\"" + opcao + "\", \"total\":\"" + total + "\", \"tempoExecucao\":\"" + tempoDuracao + "\"}";		
		tenteGravarLog(LOG_TABELA, "Fim do processamento com sucesso.", detalhes);
	}
	
	private void gravarLogErro(Exception ex) throws Exception {		
		LogDt logDt = new LogDt(LOG_TABELA, "", UsuarioDt.SistemaProjudi, "Servidor", "", "Fim do processamento com erro.", Funcoes.obtenhaConteudoExcecao(ex));
		logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.Erro));
		new LogNe().salvar(logDt);
	}
	
	private void tenteGravarLog(String Tabela, String Mensagem, String descricao) throws Exception {
		LogDt logDt = new LogDt(Tabela, "", UsuarioDt.SistemaProjudi, "Servidor", "", Mensagem, descricao);
		logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomatica));
		new LogNe().salvar(logDt);
	}
	
	private String getCaminhoPastaWebContent() throws Exception {
		//ProjudiPropriedades.getInstance().getCaminhoAplicacao();
		return new File(".").getCanonicalPath() + File.separator + "WebContent" + File.separator;	
	}
	
	/**
	 * Consulta as intimações e cartas de citação de um determinado período
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */
	public List<DiarioJusticaEletronicoDt> listarIntimacoesOuCartaCitacaoPorData(String dataInicial, String dataFinal, int opcao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);          
            DiarioJusticaEletronicoPs obPersistencia = new DiarioJusticaEletronicoPs(obFabricaConexao.getConexao());
            return obPersistencia.listarIntimacoesOuCartaCitacaoPorData(dataInicial, dataFinal, opcao);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	/**
	 * Faz o agrupamento de registros DiarioJusticaEletronicoDt por parte em ComunicacaoDJE
	 * Calcula a data de disponibilização (próximos 2 dias úteis)
	 * Extrai o texto do arquivo da intimação (Melhor colocar somente quando for enviar realmente)
	 * 
	 * @param lista
	 * @return
	 * @throws Exception
	 */
	private List<ComunicacaoDJEDt> prepararIntimacoesOuCartaCitacaoPorParte(List<DiarioJusticaEletronicoDt> dados) throws Exception {
		String flag = "@";
		ComunicacaoDJEDt comunicacaoDJE = null;
		UsuarioNe usuarioNe = new UsuarioNe();
		List<ComunicacaoDJEDt> lista = new ArrayList<>();
		
		List<String> partes = new ArrayList<>();
		
		for (DiarioJusticaEletronicoDt o : dados){
			if (!flag.equals(o.getPendId())){
				// Para cada pendência, uma parte intimada/citada
				comunicacaoDJE = new ComunicacaoDJEDt();
				comunicacaoDJE.setCodigoClasse(definirIdClasseCNJ(o.getClasseCnjId(), o.getClasseCnjRecursoId()));								
				comunicacaoDJE.setMeio("D");
				comunicacaoDJE.setNumeroProcesso(definirNumeroProcessoCNJ(o.getProcessoNumero(), o.getDigitoVerificador(), o.getForumCodigo(), o.getAno()));
				comunicacaoDJE.setOrgao(o.getServentia());				
				comunicacaoDJE.setTexto("");
				comunicacaoDJE.setLink(definirLinkArquivo(usuarioNe, o));
				comunicacaoDJE.setArqId(o.getArqId());
				comunicacaoDJE.setMoviId(o.getMoviId());
				comunicacaoDJE.setTipoComunicao(definirTipoComunicacao(o.getPendCodigo()));
				comunicacaoDJE.setTipoDocumento(o.getTipoArq());
				comunicacaoDJE.setDataDisponibilizacao(o.getDataFim());
				comunicacaoDJE.addDestinatario(o.getNomeParte(), o.getCpfCnpjParte(), o.getPoloParte());
				comunicacaoDJE.addAdvogado(o.getNomeAdvogado(), o.getOabAdvogado(), o.getUfOabAdvogado());
				if (!partes.contains(o.getNomeParte())){
					lista.add(comunicacaoDJE);
					partes.add(o.getNomeParte());
				}
			} else {
				// Se é a mesma pendência, houve mudança de advogado
				comunicacaoDJE.addAdvogado(o.getNomeAdvogado(), o.getOabAdvogado(), o.getUfOabAdvogado());
			}
			flag = o.getPendId();
		}
		return lista;
	}
	
	
	/**
	 * Consulta as intimações / carta de citação, cria o json da comunicação e envia para o CNJ (http post)
	 * Esse método foi pensado para ser usado no execução automática
	 * @param dataPublicacao
	 * @throws Exception  
	 */
	public void enviarIntimacoesOuCartaCitacaoParaCNJ(String dataPublicacao) throws Exception {
		DiarioJusticaEletronicoNe diario = new DiarioJusticaEletronicoNe();
		List<DiarioJusticaEletronicoDt> lista = diario.listarIntimacoesOuCartaCitacaoPorData(dataPublicacao + " 00:00:00", dataPublicacao + " 23:59:59", 2);
		List<ComunicacaoDJEDt> comunicacoes = prepararIntimacoesOuCartaCitacaoPorParte(lista);
		System.out.println(comunicacoes.size());
		
		// Para cada comunicação, deve-se extrair o conteudo do arquivo.
		// Não foi feito no método prepararIntimacoesOuCartaCitacaoPorParte() para evitar o uso indiscriminado de memória
		ComunicacaoDJEDt c = comunicacoes.get(0);
		c.setTexto(obterConteudoArquivo(c.getArqId()));
		
				
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        System.out.println(mapper.writeValueAsString(c));
        
        // Criar código para fazer o http post para o cnj ...
	}
	
	/**
	 * 
	 * 
	 * Esse método foi pensado para ser usado no momento que a intimação é criada, no PendenciaNe.
	 * @param fabricaConexao
	 * @param processo
	 * @param pendencia
	 * @param advogados
	 * @param arquivos
	 * @throws Exception
	 */
	public void prepararIntimacoesOuCartaCitacaoParaCNJ(FabricaConexao fabricaConexao, ProcessoDt processo, PendenciaDt pendencia, List<ProcessoParteAdvogadoDt> advogados, List<ArquivoDt> arquivos) throws Exception {
		
		ComunicacaoDJEDt comunicacaoDJE = new ComunicacaoDJEDt();
		comunicacaoDJE.setMeio("D");
		comunicacaoDJE.setCodigoClasse(processo.getIdCNJClasse());										
		comunicacaoDJE.setNumeroProcesso(Funcoes.desformataNumeroProcesso(processo.getNumeroProcessoDt().getNumeroCompletoProcesso()));
		comunicacaoDJE.setOrgao(processo.getServentia());				
		comunicacaoDJE.setTipoComunicao(definirTipoComunicacao(pendencia.getPendenciaTipoCodigo()));
		comunicacaoDJE.setDataDisponibilizacao(Funcoes.FormatarData(Funcoes.StringToDate(pendencia.getDataFim()), "yyyy-MM-dd"));		
		
		//UsuarioNe usuarioNe = new UsuarioNe();
		//comunicacaoDJE.setTexto("");
		//comunicacaoDJE.setLink(definirLinkArquivo(usuarioNe, o));
		//comunicacaoDJE.setTipoDocumento(o.getTipoArq());
		
		// Destinatário (parte)		
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		ProcessoParteDt parte = processoParteNe.consultarId(pendencia.getId_ProcessoParte());
		comunicacaoDJE.addDestinatario(parte.getNome(), parte.getCpfCnpj(), definirTipoPoloParte(parte.getProcessoParteTipoCodigo()));
		
		// Advogados
		ServentiaNe serventiaNe = new ServentiaNe();
		for (ProcessoParteAdvogadoDt adv : advogados){
			ServentiaDt serventiaOab = serventiaNe.consultarId(adv.getId_Serventia());
			comunicacaoDJE.addAdvogado(adv.getNomeAdvogado(), adv.getOabNumero(), serventiaOab.getEstadoRepresentacao());
		}
				
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        System.out.println(mapper.writeValueAsString(comunicacaoDJE));
        
	}
	
	private String definirNumeroProcessoCNJ(String numero, String digito, String forum, String ano){
		StringBuilder numeroProcesso = new StringBuilder();
		numeroProcesso.append(Funcoes.completarZeros(numero, 7));
		numeroProcesso.append(Funcoes.completarZeros(digito, 2));
		numeroProcesso.append(Funcoes.completarZeros(ano, 4));
		numeroProcesso.append(DiarioJusticaEletronicoNe.JTR);
		numeroProcesso.append(Funcoes.completarZeros(forum, 4));
		return numeroProcesso.toString();
	}
	
	private String definirIdClasseCNJ(String classeCnjId, String classeCnjRecursoId){
		return ValidacaoUtil.isNaoVazio(classeCnjRecursoId) ?  classeCnjRecursoId: classeCnjId;		
	}
	
	private String definirTipoComunicacao(String pendCodigo){
		return pendCodigo.equals(PendenciaTipoDt.CARTA_CITACAO) ? "C" : "I";		
	}
	
	private String definirTipoPoloParte(String procParteTipoCodigo){
		return String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO).equals(procParteTipoCodigo) ?  "A" : "P"; 
	}
	
	private String definirLinkArquivo(UsuarioNe usuarioNe, DiarioJusticaEletronicoDt dados) throws Exception {
		String url = "";
		// Se o tipo do arquivo é publicável (dje = 1)
		if (ValidacaoUtil.isNaoVazio(dados.getArqTipoCodigo())){
			// Se não tem arquivo na intimação, não gerar o link
			if (ValidacaoUtil.isNaoVazio(dados.getArqId())){
				// Se tem arquivo de voto, é uma ementa / relatório e voto
				// O link será para o arquivo do voto e o campo texto será a ementa
				String moviArqId = ValidacaoUtil.isNaoVazio(dados.getVotoArqId()) ? dados.getVotoMoviArqId() : dados.getMoviArqId();
				String hash = usuarioNe.getCodigoHash(moviArqId + dados.getProcessoId()) + "." + moviArqId;				
				url = ProjudiPropriedades.getInstance().getLinkSistemaNaWEB() 
						+ "/BuscaArquivoPublico?PaginaAtual=6&Id_MovimentacaoArquivo=" 
						+ moviArqId + "&hash=" + hash + "&CodigoVerificacao=true";
			}
		}		
		return url;
	}
	
	/**
	 * Abre o arquivo e faz a extração do texto, eliminando os espaços em branco, tabulações.  
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private String obterConteudoArquivo(String id) throws Exception {
		String texto = "";
		if (ValidacaoUtil.isNaoVazio(id)){
			ArquivoDt arq = new ArquivoNe().consultarId(id);
			if (ValidacaoUtil.isNaoNulo(arq)){
				byte[] out = arq.getConteudo();
				if (arq.getContentType().equals("application/pdf")){
					texto = ParserUtil.parsePdfToPlainText(out);
				} else if (arq.getContentType().equals("text/html")){
					texto = ParserUtil.parseHtmlToPlainText(out);
				} else {
					texto = ParserUtil.parseToPlainText(out);
				}
				if (ValidacaoUtil.isNaoVazio(texto)){				
					texto = texto.replaceAll("_|'", "");
					texto = texto.replaceAll("\n|\r|\t|\\s{2,}", " ");
					texto = Funcoes.normalizarEspacoEmBrancoEntreTexto(texto.trim());
				}			
			}
		}		
		return texto;
	}
	
	/*
	--- Dados da parte intimada
	select pp.id_proc_parte, pp.nome, ppt.proc_parte_tipo_codigo, pp.id_proc,
	case when pp.cpf is not null then pp.cpf when pp.cnpj is not null then pp.cnpj else null end as CPF_CNPJ_PARTE_INTIMADA
	from projudi.view_proc_parte pp join projudi.proc_parte_tipo ppt on ppt.id_proc_parte_tipo = pp.id_proc_parte_tipo
	where pp.id_proc_parte = 14735503;

	-- Dados do advogado
	select ppa.id_usu_serv, oab.oab_numero, oab.oab_complemento, u.nome, est.uf
	from projudi.proc_parte_advogado ppa 
	join projudi.usu_serv us ON us.id_usu_serv = ppa.id_usu_serv
	join projudi.usu u ON u.id_usu = us.id_usu
	join projudi.usu_serv_oab oab ON oab.id_usu_serv = us.id_usu_serv
	join projudi.usu_serv_grupo usg ON us.id_usu_serv = usg.id_usu_serv
	join projudi.grupo g ON usg.id_grupo = g.id_grupo AND g.grupo_codigo IN (2,80)		
	left join projudi.serv serv ON serv.id_serv = us.id_serv
	left join projudi.estado est ON est.id_estado = serv.id_estado_representacao
	where ppa.recebe_intimacao = 1 and ppa.data_saida is null 
	and ppa.id_proc_parte = 827418;
	--and ppa.id_usu_serv = 33575;

	-- Arquivo
	SELECT a.id_arq, a.nome_arq, a.content_type, a.arq, a.usu_assinador, a.recibo, ta.arq_tipo_codigo, ta.arq_tipo, ma.id_movi_arq, ma.id_movi
	FROM projudi.movi_arq ma
	JOIN projudi.arq a ON a.id_arq = ma.id_arq
	JOIN projudi.arq_tipo ta on a.id_arq_tipo = ta.id_arq_tipo AND ta.publico = 1
	WHERE ma.id_movi_arq_acesso IN (1,2)
	and ma.id_movi = 153145092
	AND a.id_arq in (183399814,183399813,183423039);
	*/
}
