package br.gov.go.tj.projudi.ne;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AcaoPenalDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.BeneficioCertidaoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.CertidaoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.CertidaoDt;
import br.gov.go.tj.projudi.dt.CertidaoExecucaoCPCDt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt;
import br.gov.go.tj.projudi.dt.CertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.CertidaoSegundoGrauNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoSegundoGrauPositivaNegativaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.RelatorioAudienciaProcesso;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ps.ModeloPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.projudi.ps.VotoPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;


/**
 * @author jpcpresa
 *
 */
@SuppressWarnings("all")
public class ModeloNe extends ModeloNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = 2259050168044536244L;
    /**
	 * Variáveis de modelo Processo Parte Promovente
	 */
	public static final String PROCESSO_POLOATIVO_NOME = "processo.poloativo.nome";
	public static final String PROCESSO_POLOATIVO_RG = "processo.poloativo.rg";
	public static final String PROCESSO_POLOATIVO_CPF_OU_CNPJ = "processo.poloativo.cpfOuCnpj";
	public static final String PROCESSO_POLOATIVO_ENDERECO_LOGRADOURO = "processo.poloativo.endereco.logradouro";
	public static final String PROCESSO_POLOATIVO_ENDERECO_NUMERO = "processo.poloativo.endereco.numero";
	public static final String PROCESSO_POLOATIVO_ENDERECO_COMPLEMENTO = "processo.poloativo.endereco.complemento";
	public static final String PROCESSO_POLOATIVO_ENDERECO_BAIRRO = "processo.poloativo.endereco.bairro";
	public static final String PROCESSO_POLOATIVO_ENDERECO_TELEFONE = "processo.poloativo.endereco.telefone";
	public static final String ROCESSO_POLOATIVO_ENDERECO_CIDADE = "processo.poloativo.endereco.cidade";
	public static final String PROCESSO_POLOATIVO_ENDERECO_ESTADO = "processo.poloativo.endereco.estado";
	public static final String PROCESSO_POLOATIVO_ENDERECO_CEP = "processo.poloativo.endereco.cep";

	/**
	 * Variáveis de modelo Processo Parte Promovida
	 */
	public static final String PROCESSO_POLOPASSIVO_NOME = "processo.polopassivo.nome";
	public static final String PROCESSO_POLOPASSIVO_RG = "processo.polopassivo.rg";
	public static final String PROCESSO_POLOPASSIVO_CPF_OU_CNPJ = "processo.polopassivo.cpfOuCnpj";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_LOGRADOURO = "processo.polopassivo.endereco.logradouro";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_NUMERO = "processo.polopassivo.endereco.numero";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_COMPLEMENTO = "processo.polopassivo.endereco.complemento";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_BAIRRO = "processo.polopassivo.endereco.bairro";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_TELEFONE = "processo.polopassivo.endereco.telefone";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_CIDADE = "rocesso.polopassivo.endereco.cidade";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_ESTADO = "processo.polopassivo.endereco.estado";
	public static final String PROCESSO_POLOPASSIVO_ENDERECO_CEP = "processo.polopassivo.endereco.cep";

	/**
	 * Variáveis de modelo Processo Parte Vitima
	 */
	public static final String PROCESSO_VITIMA_NOME = "processo.vitma.nome";
	public static final String PROCESSO_VITIMA_RG = "processo.vitima.rg";
	public static final String PROCESSO_VITIMA_CPF_OU_CNPJ = "processo.vitima.cpfOuCnpj";
	public static final String PROCESSO_VITIMA_ENDERECO_LOGRADOURO = "processo.vitima.endereco.logradouro";
	public static final String PROCESSO_VITIMA_ENDERECO_NUMERO = "processo.vitima.endereco.numero";
	public static final String PROCESSO_VITIMA_ENDERECO_COMPLEMENTO = "processo.vitima.endereco.complemento";
	public static final String PROCESSO_VITIMA_ENDERECO_BAIRRO = "processo.vitima.endereco.bairro";
	public static final String PROCESSO_VITIMA_ENDERECO_TELEFONE = "processo.vitima.endereco.telefone";
	public static final String PROCESSO_VITIMA_ENDERECO_CIDADE = "rocesso.vitima.endereco.cidade";
	public static final String PROCESSO_VITIMA_ENDERECO_ESTADO = "processo.vitima.endereco.estado";
	public static final String PROCESSO_VITIMA_ENDERECO_CEP = "processo.vitima.endereco.cep";
	
	/**
	 * Variáveis de modelo Processo Parte Testemunha
	 */
	public static final String PROCESSO_TESTEMUNHA_NOME = "processo.testemunha.nome";
	public static final String PROCESSO_TESTEMUNHA_RG = "processo.testemunha.rg";
	public static final String PROCESSO_TESTEMUNHA_CPF_OU_CNPJ = "processo.testemunha.cpfOuCnpj";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_LOGRADOURO = "processo.testemunha.endereco.logradouro";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_NUMERO = "processo.testemunha.endereco.numero";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_COMPLEMENTO = "processo.testemunha.endereco.complemento";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_BAIRRO = "processo.testemunha.endereco.bairro";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_TELEFONE = "processo.testemunha.endereco.telefone";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_CIDADE = "processo.testemunha.endereco.cidade";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_ESTADO = "processo.testemunha.endereco.estado";
	public static final String PROCESSO_TESTEMUNHA_ENDERECO_CEP = "processo.testemunha.endereco.cep";
	/**
	 * Variáveis de modelo cumprimento Parte 
	 */
	public static final String CUMPRIMENTO_PARTE_NOME = "cumprimento.parte.nome";
	public static final String CUMPRIMENTO_PARTE_RG = "cumprimento.parte.rg";
	public static final String CUMPRIMENTO_PARTE_CPF_OU_CNPJ = "cumprimento.parte.cpfOuCnpj";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_LOGRADOURO = "cumprimento.parte.endereco.logradouro";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_NUMERO = "cumprimento.parte.endereco.numero";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_COMPLEMENTO = "cumprimento.parte.endereco.complemento";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_BAIRRO = "cumprimento.parte.endereco.bairro";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_TELEFONE = "cumprimento.parte.endereco.telefone";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_CIDADE = "cumprimento.parte.endereco.cidade";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_ESTADO = "cumprimento.parte.endereco.estado";
	public static final String CUMPRIMENTO_PARTE_ENDERECO_CEP = "cumprimento.parte.endereco.cep";

	/**
	 * Variáveis de modelo cumprimento Testemunha
	 */
	public static final String CUMPRIMENTO_TESTEMUNHA_NOME = "cumprimento.parte.nome";
	public static final String CUMPRIMENTO_TESTEMUNHA_RG = "cumprimento.parte.rg";
	public static final String CUMPRIMENTO_TESTEMUNHA_CPF_OU_CNPJ = "cumprimento.parte.cpfOuCnpj";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_LOGRADOURO = "cumprimento.parte.endereco.logradouro";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_NUMERO = "cumprimento.parte.endereco.numero";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_COMPLEMENTO = "cumprimento.parte.endereco.complemento";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_BAIRRO = "cumprimento.parte.endereco.bairro";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_TELEFONE = "cumprimento.parte.endereco.telefone";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_CIDADE = "cumprimento.parte.endereco.cidade";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_ESTADO = "cumprimento.parte.endereco.estado";
	public static final String CUMPRIMENTO_TESTEMUNHA_ENDERECO_CEP = "cumprimento.parte.endereco.cep";

	/**
	 * Variáveis de modelo 
	 */
	public static final String PROCESSO_JUIZ = "processo.juiz";
	public static final String PROCESSO_RELATOR = "processo.relator";
	public static final String PROCESSO_REVISOR = "processo.revisor";
	public static final String PROCESSO_VOGAL = "processo.vogal";
	public static final String PROCESSO_PRESIDENTE_CAMARA = "processo.presidente.camara";
	public static final String PROCESSO_PRESIDENTE_TURMA = "processo.presidente.turma";
	
	public static final String PROCESSO_ACAO_VALOR = "processo.acao.valor";
	public static final String PROCESSO_ACAO_TIPO = "processo.acao.tipo";
	public static final String PROCESSO_DATA_DISTRIBUICAO  = "processo.data.distribuicao";
	public static final String PROCESSO_DATA_TRANSITO_JULGADO  = "processo.data.transito.julgado";
	
	public static final String PROCESSO_CLASSE_CNJ = "processo.classe.cnj";
	public static final String PROCESSO_ASSUNTOS = "processo.assuntos";
	public static final String PROCESSO_CRIMES = "processo.crimes";
	public static final String PROCESSO_LOCAL_CUMPRIMENTO_PENA = "processo.localcumprimentopena";
	public static final String PROCESSO_CODIGO_ACESSO = "processo.codigoAcesso";
	public static final String PROCESSO_DEPENDENTE_NUMERO = "processo.dependente.numero";
	public static final String PROCESSO_NUMERO = "processo.numero";
	public static final String PROCESSO_POLOSPASSIVOS_INICIO = "processo.polospassivos.inicio";
	public static final String PROCESSO_POLOSATIVOS_INICIO = "processo.polosativos.inicio";
	public static final String PROCESSO_POLOSPASSIVOS_FIM = "processo.polospassivos.fim";
	public static final String PROCESSO_POLOSATIVOS_FIM = "processo.polosativos.fim";
	public static final String SERVENTIA_ENDERECO_CEP = "serventia.endereco.cep";
	public static final String SERVENTIA_ENDERECO_ESTADO_COMPLETO = "serventia.endereco.estado.completo";
	public static final String SERVENTIA_ENDERECO_ESTADO_SIGLA = "serventia.endereco.estado.sigla";
	public static final String SERVENTIA_ENDERECO_CIDADE = "serventia.endereco.cidade";
	public static final String SERVENTIA_ENDERECO_TELEFONE = "serventia.endereco.telefone";
	public static final String SERVENTIA_ENDERECO_BAIRRO = "serventia.endereco.bairro";
	public static final String SERVENTIA_ENDERECO_COMPLEMENTO = "serventia.endereco.complemento";
	public static final String SERVENTIA_ENDERECO_NUMERO = "serventia.endereco.numero";
	public static final String SERVENTIA_ENDERECO_LOGRADOURO = "serventia.endereco.logradouro";
	public static final String SERVENTIA_COMARCA = "serventia.comarca";
	public static final String SERVENTIA_NOME = "serventia.nome";
	public static final String CUMPRIMENTO_CODIGO = "cumprimento.codigo";
	public static final String CUMPRIMENTO_AUDIENCIA_HORA = "cumprimento.audiencia.hora";
	public static final String CUMPRIMENTO_AUDIENCIA_DATA = "cumprimento.audiencia.data";
	public static final String CUMPRIMENTO_AUDIENCIA_INSTRUCAO_DATA = "cumprimento.audiencia.instrucao.data";
	public static final String CUMPRIMENTO_AUDIENCIA_INSTRUCAO_HORA = "cumprimento.audiencia.instrucao.hora";
	public static final String CUMPRIMENTO_AUDIENCIA_CONCILIACAO_HORA = "cumprimento.audiencia.conciliacao.hora";
	public static final String CUMPRIMENTO_PRAZO_DIAS = "cumprimento.prazo.dias";
	public static final String CUMPRIMENTO_AUDIENCIA_CONCILIACAO_DATA = "cumprimento.audiencia.conciliacao.data";
	public static final String CUMPRIMENTO_CODIGO_BARRA = "cumprimento.codigobarra";
	public static final String USUARIO_MATRICULA = "usuario.matricula";
	public static final String USUARIO_CARGO = "usuario.cargo";
	public static final String USUARIO_NOME = "usuario.nome";
	public static final String USUARIO_NOME_CHEFE = "usuario.chefe";
	public static final String BRASAO = "brasao";
	public static final String BRASAO_GOIAS = "brasao.goias";
	public static final String HORA = "hora";
	public static final String DATA = "data";
	
	public static final String ADVOGADOS_INICIO = "advogado.inicio";
	public static final String ADVOGADOS_FIM = "advogado.fim";
	
	public static final String PROCESSO_VITIMAS_INICIO = "processo.vitimas.inicio";
	public static final String PROCESSO_VITIMAS_FIM = "processo.vitimas.fim";
	
	
	/**
	 * Variáveis de Certidão
	 */
	public static final String CERTIDAO_IDENTIFICACAO_REQUERENTE = "certidao.identificacao.requerente";
	public static final String CERTIDAO_IDENTIFICACAO_PROFISSAO = "certidao.identificacao.profissao"; 
	public static final String CERTIDAO_IDENTIFICACAO_ESTADO_CIVIL = "certidao.identificacao.estado.civil";
	public static final String CERTIDAO_IDENTIFICACAO_SEXO = "certidao.identificacao.sexo";
	public static final String CERTIDAO_IDENTIFICACAO_CPFCNPJ = "certidao.identificacao.cpfCnpj";
	public static final String CERTIDAO_IDENTIFICACAO_RG = "certidao.identificacao.rg";
	public static final String CERTIDAO_IDENTIFICACAO_DOMICILIO = "certidao.identificacao.domicilio";
	public static final String CERTIDAO_IDENTIFICACAO_NACIONALIDADE = "certidao.identificacao.nacionalidade";
	public static final String CERTIDAO_IDENTIFICACAO_NOME_MAE = "certidao.identificacao.nome.mae";
	public static final String CERTIDAO_IDENTIFICACAO_NOME_PAI = "certidao.identificacao.nome.pai";
	public static final String CERTIDAO_IDENTIFICACAO_DATA_NASCIMENTO = "certidao.identificacao.data.nascimento";
	private static final String CERTIDAO_IDENTIFICACAO_NATURALIDADE = "certidao.identificacao.naturalidade";
	private static final String CERTIDAO_IDENTIFICACAO_OAB = "certidao.identificacao.oab";
	public static final String CERTIDAO_PROCESSOS_INICIO = "certidao.processos.inicio";
	public static final String CERTIDAO_PROCESSOS_FIM = "certidao.processos.fim";
	public static final String CERTIDAO_VALOR_CERTIDAO = "certidao.valor.certidao";
	public static final String CERTIDAO_VALOR_TAXA_JUDICIARIA = "certidao.valor.taxa.judiciaria";
	public static final String CERTIDAO_DATA_RECEITA = "certidao.data.receita";
	public static final String CERTIDAO_VALOR_TOTAL = "certidao.valor.total";
	public static final String CERTIDAO_NUMERO_GUIA = "certidao.numero.guia";
	public static final String PROCESSO_LACO_NUMERO = "processo.laco.numero";
	public static final String CERTIDAO_CUSTA_CERTIDAO = "certidao.custa.certidao";
	public static final String CERTIDAO_CUSTA_TAXA_JUDICIARIA = "certidao.custa.taxa.judiciaria";
	public static final String CERTIDAO_CUSTA_TOTAL = "certidao.custa.total";
	/**
	 * Variáveis da certidão negativa positiva  e antecedentes
	 */
	public static final String CERTIDAO_TEM_FIM_JUDICIAL = "certidao.tem.fim.judicial";
	public static final String CERTIDAO_BENEFICIO_INICIO = "certidao.beneficio.inicio";
	public static final String CERTIDAO_BENEFICIO_FINAL = "certidao.beneficio.final";
	public static final String CERTIDAO_PROCESSO_NUMERO = "certidao.processo.numero";
	public static final String CERTIDAO_PROCESSO_SERVENTIA ="certidao.processo.serventia";
	public static final String CERTIDAO_PROCESSO_TIPO = "certidao.processo.tipo";
	public static final String CERTIDAO_PROCESSO_PROMOVENTE = "certidao.processo.promovente";
	public static final String CERTIDAO_PROCESSO_PROMOVIDO = "certidao.processo.promovido";
	public static final String CERTIDAO_PROCESSO_DATA_DISTRIBUICAO = "certidao.processo.data.distribuicao";
	public static final String CERTIDAO_PROCESSO_VALOR_DA_ACAO = "certidao.processo.valor.da.acao";
	public static final String CERTIDAO_PROCESSO_ADVOGADO_PROMOVENTE = "certidao.processo.advogado.promovente";
	public static final String CERTIDAO_PROCESSO_ADVOGADO_PROMOVIDO = "certidao.processo.advogado.promovido";
	public static final String CERTIDAO_PROCESSO_ASSUNTO = "certidao.processo.assunto";
	public static final String CERTIDAO_PROCESSO_COMARCA = "certidao.processo.comarca";
	public static final String CERTIDAO_PROCESSO_JUIZ = "certidao.processo.juiz";
	public static final String CERTIDAO_PROCESSO_DATA_TRANSITO_JULGADO = "certidao.processo.data.transito.julgado";
	public static final String CERTIDAO_PROCESSO_FASE = "certidao.processo.fase";
	public static final String CERTIDAO_PROCESSO_FILIACAO = "certidao.processo.filiacao";
	public static final String CERTIDAO_PROCESSO_SENTENCA = "certidao.processo.sentenca";
	public static final String CERTIDAO_PROCESSO_DATABAIXA = "certidao.processo.dataBaixa";
	public static final String CERTIDAO_PROCESSO_MOTIVOBAIXA = "certidao.processo.motivoBaixa";
	public static final String CERTIDAO_PROCESSO_DATARECEBIMENTODENUNCIA = "certidao.processo.dataRecebimentoDenuncia";

	private static final String CERTIDAO_PRATICA_FORENSE_QUANTIDADE = "certidao.pratica.forense.quantidade";
	private static final String CERTIDAO_PRATICA_FORENSE_DATA_INICIAL = "certidao.pratica.forense.data.inicial";
	private static final String CERTIDAO_PRATICA_FORENSE_DATA_FINAL = "certidao.pratica.forense.data.final";
	private static final String CERTIDAO_PRATICA_FORENSE_ESCRIVAO = "certidao.pratica.forense.escrivao";
	
	/**
	 * Variáveis da certidão circunstanciada
	 */
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_NUMERO_ACAO_PENAL = "certidao.circunstanciada.processo.numero.acao";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_CIDADE_ORIGEM = "certidao.circunstanciada.processo.cidade.origem";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_VARA_ORIGEM = "certidao.circunstanciada.processo.vara.origem";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_DISTRIBUICAO = "certidao.circunstanciada.processo.data.distribuicao";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_DENUNCIA = "certidao.circunstanciada.processo.data.denuncia";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_SENTENCA = "certidao.circunstanciada.processo.data.sentenca";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_TRANSITO = "certidao.circunstanciada.processo.data.transito";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_PRONUNCIA = "certidao.circunstanciada.processo.data.pronuncia";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_ACORDAO = "certidao.circunstanciada.processo.data.acordao";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_ADMONITORIA = "certidao.circunstanciada.processo.data.admonitoria";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_ESTABELECIMENTO_PENAL = "certidao.circunstanciada.processo.estabelecimento.penal";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_REGIME = "certidao.circunstanciada.processo.regime";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_TIPO_PENA = "certidao.circunstanciada.processo.tipo.pena";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_CONDENACOES = "certidao.circunstanciada.processo.condenacoes";
	private static final String CERTIDAO_PRATICA_FORENSE_INICIO_ATUACAO = "certidao.pratica.forense.inicio.atuacao";

	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_MODALIDADES = "certidao.circunstanciada.processo.modalidades";
	public static final String CERTIDAO_CIRCUNSTANCIADA_PROCESSO_SURSIS = "certidao.circunstanciada.processo.sursis";
	private static final String CERTIDAO_AVERBACAO_CUSTA = "certidao.averbacao.custa";
	public static final  String CERTIDAO_PROCESSOS_INFORMATIVO_INICIO = "certidao.processos.informativo.inicio";
	public static final String  CERTIDAO_PROCESSOS_INFORMATIVO_FIM = "certidao.processos.informativo.fim" ;
	private static final String CERTIDAO_PROCESSO_RELATOR = "certidao.processo.relator";
	private static final String CERTIDAO_BENEFICIO = "certidao.beneficio";
	
	/**
	 * Variáveis do mandado de prisão
	 */
	public static final String MANDADO_PRISAO_NUMERO = "mandado.numerMandado";
	public static final String MANDADO_PRISAO_NUMERO_PROCESSO = "mandado.numeroProcessoCompleto";
	public static final String MANDADO_PRISAO_CLASSE_CNJ = "mandado.classeCNJ";
	public static final String MANDADO_PRISAO_ASSUNTO_CNJ = "mandado.assuntoCNJ";
	
	public static final String MANDADO_PRISAO_PROMOVIDO_NOME = "mandado.promovido.nome";
	public static final String MANDADO_PRISAO_PROMOVIDO_SEXO = "mandado.promovido.sexo";
	public static final String MANDADO_PRISAO_PROMOVIDO_CPF = "mandado.promovido.cpf";
	public static final String MANDADO_PRISAO_PROMOVIDO_ENDERECO = "mandado.promovido.endereco";
	public static final String MANDADO_PRISAO_PROMOVIDO_NOME_MAE = "mandado.promovido.nomeMae";
	public static final String MANDADO_PRISAO_PROMOVIDO_NOME_PAI = "mandado.promovido.nomePai";
	public static final String MANDADO_PRISAO_PROMOVIDO_DATA_NASCIMENTO = "mandado.promovido.dataNascimento";
	public static final String MANDADO_PRISAO_PROMOVIDO_NATURALIDADE = "mandado.promovido.naturalidade";
	
//	public static final String MANDADO_PRISAO_ASSUNTO_DELITO_PRINCIPAL = "mandado.assuntoDelitoPrincipal";
	public static final String MANDADO_PRISAO_PENA_IMPOSTA = "mandado.penaImposta";
	public static final String MANDADO_PRISAO_REGIME = "mandado.regime";
	public static final String MANDADO_PRISAO_LOCAL_RECOLHIMENTO = "mandado.localRecolhimento";
	public static final String MANDADO_PRISAO_DATA_PRESCRICAO = "mandado.dataPrescricao";
	public static final String MANDADO_PRISAO_DECISAO = "mandado.decisao";
	public static final String MANDADO_PRISAO_TITULO = "mandado.titulo";
	
	/**
     * 
     */

	
	/**
	 * Variáveis do Certidões emitidas pelo os Oficiais - OficialCertidao
	 */
	public static final String OFICIAL_CERTIDAO_NUMERO = "oficialCertidao.numero";
	public static final String OFICIAL_CERTIDAO_PROCESSO_NUMERO = "oficialCertidao.processoNumero";
	public static final String OFICIAL_CERTIDAO_NUMERO_MANDADO = "oficialCertidao.numeroMandado";
	public static final String OFICIAL_CERTIDAO_SERVENTIA_NOME = "oficialCertidao.serventia";
	public static final String OFICIAL_CERTIDAO_PROCESSO_PROMOVENTE_NOME = "oficialCertidao.processoPromoventeNome";
	public static final String OFICIAL_CERTIDAO_PROCESSO_PROMOVIDO_NOME = "oficialCertidao.processoPromovidoNome";
	public static final String OFICIAL_CERTIDAO_DATA_DILIGENCIA = "oficialCertidao.dataDiligencia";
	public static final String OFICIAL_CERTIDAO_HORA_DILIGENCIA = "oficialCertidao.horaDiligencia";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_NOME = "oficialCertidao.promoventeNome";
	public static final String OFICIAL_CERTIDAO_DILIGENCIA_NOME_INTIMADO = "oficialCertidao.diligenciaNomeIntimado";
	public static final String OFICIAL_CERTIDAO_DILIGENCIA_RG_INTIMADO = "oficialCertidao.DiligenciaRGIntimado";
	public static final String OFICIAL_CERTIDAO_DILIGENCIA_ENDERECO = "oficialCertidao.diligenciaEndereco";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_LOGRADOURO = "oficialCertidao.promoventeEnderecoLogradouro";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_NUMERO = "oficialCertidao.promoventeEnderecoNumero";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_COMPLEMENTO = "oficialCertidao.promoventeEnderecoComplemento";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_QUADRA = "oficialCertidao.promoventeEnderecoQuadra";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_LOTE = "oficialCertidao.promoventeEnderecoLote";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_BAIRRO = "oficialCertidao.promoventeEnderecoBairro";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_CEP = "oficialCertidao.promoventeEnderecoCEP";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_CIDADE = "oficialCertidao.promoventeEnderecoCidade";
	public static final String OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_UF = "oficialCertidao.promoventeEnderecoUf";
	public static final String OFICIAL_CERTIDAO_DATA_EMISSAO = "oficialCertidao.dataEmissao";
	public static final String OFICIAL_CERTIDAO_USUARIO = "oficialCertidao.usuario";
	public static final String OFICIAL_CERTIDAO_CARGO = "oficialCertidao.cargo";
	
	
	public static final String AUDIENCIA_SESSAO_CLASSE_CNJ = "audienciaSessao.classe.cnj";
	public static final String AUDIENCIA_SESSAO_PROCESSO_NUMERO = "audienciaSessao.processo.numero";
	public static final String AUDIENCIA_SESSAO_PROCESSO_COMARCA = "audienciaSessao.processo.comarca";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_NOME = "audienciaSessao.processo.poloativo.nome";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_LISTA_NOME = "audienciaSessao.processo.poloativo.lista.nome";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_ADVOGADO = "audienciaSessao.processo.poloativo.advogado";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_FORMADETRATAMENTO = "audienciaSessao.processo.poloativo.formaDeTratamento";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_NOME = "audienciaSessao.processo.polopassivo.nome";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_LISTA_NOME = "audienciaSessao.processo.polopassivo.lista.nome";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_ADVOGADO = "audienciaSessao.processo.polopassivo.advogado";
	public static final String AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_FORMADETRATAMENTO = "audienciaSessao.processo.polopassivo.formaDeTratamento";
	public static final String AUDIENCIA_SESSAO_PROCESSO_RELATOR = "audienciaSessao.processo.relator";
	public static final String AUDIENCIA_SESSAO_PROCESSO_REPRESENTANTE_MP = "audienciaSessao.processo.representanteMP";
	public static final String AUDIENCIA_SESSAO_REDATOR = "audienciaSessao.redator"; // jvosantos - 13/08/2019 12:56 - Adicionar váriavel para substituir pelo nome do redator (1º divergente)
	
	public static final String AUDIENCIA_SESSAO_PROCESSO_LISTAPOLOS_AGRUPADOS = "audienciaSessao.processo.listaPolo.agrupado";
	
	// Variáveis da Certidão Narrativa
	public static final String CERTIDAO_IDENTIFICACAO_PROTOCOLO = "certidao.identificacao.protocolo";
	public static final String CERTIDAO_IDENTIFICACAO_JUIZO = "certidao.identificacao.juizo";
	public static final String CERTIDAO_IDENTIFICACAO_NATUREZA = "certidao.identificacao.natureza";
	public static final String CERTIDAO_IDENTIFICACAO_VALOR_ACAO = "certidao.identificacao.valor.acao";
	public static final String CERTIDAO_IDENTIFICACAO_REQUERENTE_PROCESSO = "certidao.identificacao.requerente.processo";
	public static final String CERTIDAO_IDENTIFICACAO_ADVOGADO_REQUERENTE = "certidao.identificacao.advogado.requerente";
	public static final String CERTIDAO_IDENTIFICACAO_REQUERIDO = "certidao.identificacao.requerido";
	public static final String CERTIDAO_IDENTIFICACAO_TEXTO_CERTIDAO = "certidao.identificacao.texto.certidao";
	public static final String CERTIDAO_NARRATIVA_TEXTO = "certidao.texto.certidao.narrativa";
	public static final String CERTIDAO_MOVIMENTACOES_PROCESSO = "certidao.movimentacoes.processo";
	
	public static final String SESSAO_HORA_FINAL = "sessao.horaFinal";
	
	/**
	 * Variáveis de mandados expedidos para a Central
	 */
	public static final String MANDADO_CENTRAL_NUMERO_EXPEDIDO = "mandadoCentral.numeroMandadoExpedido";
	
	/**
     * 
     */
	public String Verificar(ModeloDt dados) {
		String stRetorno = "";

		if (dados.getModelo().equalsIgnoreCase("")) {
			stRetorno += "Descrição do modelo é é obrigatório.";
		}
		if (dados.getTexto().equalsIgnoreCase("")) {
			stRetorno += "Insira um conteúdo para o modelo.";
		}

		if (dados.getId_ArquivoTipo().equalsIgnoreCase("")) {
			stRetorno += "Tipo de Arquivo é é obrigatório.";
		}

		return stRetorno;
	}

	/**
	 * Salva os dados do modelo. 
	 * De acordo com o grupo do usuário determinará se o modelo é do usuário ou da serventia,
	 * ou seja, se é usuário externo o modelo fica vinculado ao usuário, caso contrário fica vinculado
	 * a serventia.
	 * 
	 * @param modeloDt dt de modelo
	 * @param usuarioDt dt de usuário logado
	 * 
	 * @author msapaula
	 */
	public void salvarModelo(ModeloDt modeloDt, UsuarioDt usuarioDt) throws Exception {
		
		//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		switch (grupoTipo) {
			//Usuários externos criam modelos particulares, vinculados ao seu usuário
			case GrupoTipoDt.ADVOGADO:
			case GrupoTipoDt.AUTORIDADE_POLICIAL:
			case GrupoTipoDt.CONTADOR:
			case GrupoTipoDt.PARTE:
			case GrupoTipoDt.MP:
				modeloDt.setId_Serventia(usuarioDt.getId_Serventia());
				modeloDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentia());
				break;

			//Juiz cria modelos particulares
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				modeloDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentia());
				modeloDt.setId_Serventia(usuarioDt.getId_Serventia());
				break;

			//Assistentes criam modelos vinculados ao usuário chefe
			case GrupoTipoDt.ASSESSOR:
			case GrupoTipoDt.ASSESSOR_ADVOGADO:
			case GrupoTipoDt.ASSESSOR_MP:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				modeloDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentiaChefe());
				modeloDt.setId_Serventia(usuarioDt.getId_Serventia());
				break;

			//Administrador cria modelos genéricos
			case GrupoTipoDt.ADMINISTRADOR:
				break;
			
			//  modelo criado/alterado pelo gerenciamento.  se for alteração de um modelo de outra serventia 
			//                                              ele vai salva-lo com genérico ver com heleno.	
			case GrupoTipoDt.GERAL:
				 if (usuarioDt.getId_Serventia().equalsIgnoreCase(ServentiaDt.GERENCIAMENTO_SISTEMA_PRODUDI) &&
					 usuarioDt.getGrupoCodigo().equalsIgnoreCase(Integer.toString(GrupoDt.GERENCIAMENTO_TABELAS))) { 					 
					 break;
				 }		
				
			//Por padrão os modelos serão vinculados a serventia
			default:
				modeloDt.setId_Serventia(usuarioDt.getId_Serventia());
				break;
		}
		this.salvar(modeloDt);

	}

	/**
	 * Consulta geral de modelos, levando em consideração o grupo do usuário para tratar os modelos
	 * que o usuário pode visualizar.
	 * 
	 * @param descricao parâmetro para consulta
	 * @param posicao parametro para paginação
	 * @param id_ArquivoTipo tipo de arquivo para que sejam filtrados os modelos
	 * @param usuarioDt usuario que está realizando a consulta
	 * 
	 * @author msapaula
	 */
	public List consultarModelos(String descricao, String posicao, String id_ArquivoTipo, UsuarioDt usuarioDt) throws Exception {
		List tempList = null;		
//			
//			ob obPersistencia = new ob( obFabricaConexao);
		//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		switch (grupoTipo) {
			//Administrador visualiza modelos genéricos
			//case GrupoDt.ADMINISTRADORES:
			case GrupoTipoDt.ADMINISTRADOR:
				tempList = this.consultarModelosGenericos(descricao, posicao, id_ArquivoTipo);
				break;

			//Usuários externos visualizam apenas seus próprios modelos
//				case GrupoDt.ADVOGADOS:
//				case GrupoDt.AUTORIDADES_POLICIAIS:
//				case GrupoDt.CONTADORES_VARA:
//				case GrupoDt.PARTES:
//				case GrupoDt.MINISTERIO_PUBLICO:
			case GrupoTipoDt.ADVOGADO:
			case GrupoTipoDt.AUTORIDADE_POLICIAL:
			case GrupoTipoDt.CONTADOR:
			case GrupoTipoDt.PARTE:
			case GrupoTipoDt.MP:
				tempList = this.consultarModelosUsuario(descricao, posicao, id_ArquivoTipo, usuarioDt.getId_UsuarioServentia());
				break;

			//Juízes visualizam todos os modelos (seus, genéricos e da serventia)
//				case GrupoDt.JUIZES_TURMA_RECURSAL:
//				case GrupoDt.JUIZES_VARA:
//				case GrupoDt.DESEMBARGADOR:
			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				tempList = this.consultarModelosGrupo(descricao, posicao, id_ArquivoTipo, usuarioDt.getId_Serventia(), usuarioDt.getId_UsuarioServentia());
				break;

			//Assistentes visualizam modelos (do chefe, genéricos e da serventia)
//				case GrupoDt.ASSISTENTES:
//				case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
//				case GrupoDt.ASSISTENTES_JUIZES_VARA:
//				case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSESSOR:
			case GrupoTipoDt.ASSESSOR_ADVOGADO:
			case GrupoTipoDt.ASSESSOR_MP:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				tempList = this.consultarModelosGrupo(descricao, posicao, id_ArquivoTipo, usuarioDt.getId_Serventia(), usuarioDt.getId_UsuarioServentiaChefe());
				break;

			//Por padrão usuários visualizam modelos genéricos e da serventia
			default:
				tempList = this.consultarModelosServentia(descricao, posicao, id_ArquivoTipo, usuarioDt.getId_Serventia());
				break;
		}
		QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
		tempList.remove(tempList.size() - 1);
		
		return tempList;
	}

	/**
	* Realiza chamada ao objeto que efetuará a consulta modelo para código de acesso
	*/
	public ModeloDt consultarModeloCodigoAcesso(String id_ArquivoTipo) throws Exception {
		ModeloDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ModeloPs obPersistencia = new ModeloPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarModeloCodigoAcesso(id_ArquivoTipo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	* Realiza chamada ao objeto que efetuará a consulta modelo para código de acesso
	* através da coluna ModeloCodigo
	*/
	public ModeloDt consultarModeloCodigo(String ModeloCodigo) throws Exception {
		ModeloDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ModeloPs obPersistencia = new ModeloPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarModeloCodigo(ModeloCodigo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta modelos de serventia, retornando os modelos genéricos e os específicos da serventia
	 * passada como parâmetro
	 * 
	 * @param descricao parâmetro para consulta
	 * @param posicao parametro para paginação
	 * @param id_ArquivoTipo tipo de arquivo para que sejam filtrados os modelos
	 * @param id_Serventia identificação da serventia
	 * 
	 * @author msapaula
	 */
	private List consultarModelosServentia(String descricao, String posicao, String id_ArquivoTipo, String id_Serventia) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		List lista = null;
		try{
			lista = obPersistencia.consultarModelos(descricao, posicao, id_ArquivoTipo, id_Serventia, true);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	private String consultarModelosServentiaJSON(String descricao,  String posicao, String id_ArquivoTipo, String id_Serventia) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		String stTemp = "";
		try{
			
			stTemp = obPersistencia.consultarModelosJSON(descricao,  posicao, id_ArquivoTipo, id_Serventia, true);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Consulta modelos de um usuário
	 * 
	 * @param descricao parâmetro para consulta
	 * @param posicao parametro para paginação
	 * @param id_ArquivoTipo tipo de arquivo para que sejam filtrados os modelos
	 * @param id_UsuarioServentia identificação do usuario serventia
	 * 
	 * @author msapaula
	 */
	private List consultarModelosUsuario(String descricao, String posicao, String id_ArquivoTipo, String id_UsuarioServentia) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		List lista = null;
		try{
			lista = obPersistencia.consultarModelosUsuario(descricao, posicao, id_ArquivoTipo, id_UsuarioServentia);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}

	private String consultarModelosUsuarioJSON(String descricao, String posicao, String id_ArquivoTipo, String id_UsuarioServentia, String ordenacao, String quantidadeRegistros) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		String stTemp = null;
		try{
			stTemp = obPersistencia.consultarModelosUsuarioJSON(descricao,  posicao, id_ArquivoTipo, id_UsuarioServentia, ordenacao, quantidadeRegistros);		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
			
	/**
	 * Consulta modelos de um grupo específicio, usado até agora para o caso de juizes, onde
	 * esses podem ver os seus modelos e os genéricos
	 * 
	 * @param descricao parâmetro para consulta
	 * @param posicao parametro para paginação
	 * @param id_ArquivoTipo tipo de arquivo para que sejam filtrados os modelos
	 * @param id_UsuarioServentia identificação do usuario serventia
	 * 
	 * @author msapaula
	 */
	private List consultarModelosGrupo(String descricao, String posicao, String id_ArquivoTipo, String id_Serventia, String id_UsuarioServentia) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		List lista = null;
		try{
			lista = obPersistencia.consultarModelosGrupo(descricao, posicao, id_ArquivoTipo, id_UsuarioServentia, id_Serventia);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}

	private String consultarModelosGrupoJSON(String descricao,  String posicao, String id_ArquivoTipo, String id_Serventia, String id_UsuarioServentia) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		String stTemp = null;
		try{
			
			stTemp = obPersistencia.consultarModelosGrupoJSON(descricao,  posicao, id_ArquivoTipo, id_UsuarioServentia, id_Serventia);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consulta modelos de um grupo específicio, usado até agora para o caso de juizes, onde
	 * esses podem ver os seus modelos e os genéricos
	 * 
	 * @param descricao parâmetro para consulta
	 * @param posicao parametro para paginação
	 * @param id_ArquivoTipo tipo de arquivo para que sejam filtrados os modelos
	 * @param id_UsuarioServentia identificação do usuario serventia
	 * 
	 * @author msapaula
	 */
	private List consultarModelosGenericos(String descricao, String posicao, String id_ArquivoTipo) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		List lista = null;
		try{
			lista = obPersistencia.consultarModelos(descricao, posicao, id_ArquivoTipo, null, true);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	private String consultarModelosGenericosJSON(String descricao,  String posicao, String id_ArquivoTipo) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ModeloPs obPersistencia = new  ModeloPs(obFabricaConexao.getConexao());
		String stTemp = "";
		try{
			stTemp = obPersistencia.consultarModelosJSON(descricao,  posicao, id_ArquivoTipo, null, true);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Monta o modelo, normalmente utilizado para processos, pois nao possui o atributo prazo
	 * @author Ronneesley Moura Teles
	 * @since 15/05/2008 15:32
	 * @param String id_modelo, id do modelo
	 * @param String id_processo, id do processo
	 * @param UsuarioDt usuario, usuario que esta montando o modelo
	 * @return String
	 * @throws Exception 
	 * @throws Execption
	 */
	public String montaConteudo(String id_modelo, String id_processo, UsuarioDt usuario) throws Exception{
		return this.montaConteudo(id_modelo, id_processo, null, usuario);
	}

	/**
	 * Monta modelo 
	 * @author Ronneesley Moura Teles
	 * @since 15/05/2008 13:31
	 * 	Aprimoramento na rotina
	 *  @author Ronneesley Moura Teles
	 *  @since 27/05/2008 13:38
	 * @param String id_modelo, id do modelo
	 * @param String id_processo, id do processo
	 * @param String prazo, prazo do cumprimento
	 * @param UsuarioDt usuario, usuario que solicita o modelo
	 * @return String
	 * @throws Exception 
	 */
	public String montaConteudo(String id_modelo, String id_processo, String prazo, UsuarioDt usuario) throws Exception{
		String texto = super.consultarId(id_modelo).getTexto();

		return this.montaConteudo(id_processo, null, prazo, usuario, null, texto);
	}

	/**
	 * Monta conteudo de uma movimentacao
	 * Alteracoes
	 * 		@author Ronneesley Moura Teles
	 * 		@since 07/05/2008 10:20
	 * 		Reescrita do metodo utilizando o metodo base
	 * @author Ronneesley Moura Teles
	 * @param MovimentacaoDt movimentacao, Movimentacao a ser analisada
	 * @param UsuarioDt servidor, Usuario
	 * @param String texto, texto a ser modificado
	 * @since 28/03/2008 
	 * @return String
	 * @throws Exception 
	 */
	public String montaConteudo(MovimentacaoDt movimentacao, UsuarioDt servidor, String texto) throws Exception{
		return this.montaConteudo(movimentacao.getId_Processo(), null, null, servidor, null, texto);
	}

	/**
	 * Tratamento de caracteres
	 * @author Ronneesley Moura Teles
	 * @since 17/04/2008 - 16:56
	 * @param String texto, texto a ser tratado
	 * @return
	 */
	private String tratamento(String texto) {
		return texto.replace("\'", "\\'");
	}

	/**
	 * Monta conteudo apartir de um modelo
	 * @since 08/04/2008
	 * @author Ronneesley Moura Teles
	 *  Aprimoramento na rotina
	 *  @author Ronneesley Moura Teles
	 *  @since 27/05/2008 13:38 
	 * @param PendenciaDt pendencia, pendencia a ser analisa 
	 * @param servidor
	 * @param String id_modelo
	 * @return String
	 * @throws Exception
	 */
	public String montaConteudoPorModelo(PendenciaDt pendencia, UsuarioDt servidor, String id_modelo) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}

		return this.montaConteudo(pendencia, servidor, texto);
	}
	
	
	
	/**
	 * Monta o conteúdo de uma certidão Negativa/Positiva
	 * @param certidao Dt contendo os dados da certidão
	 * @param servidor Dt contendo os dados do servidor
	 * @param id_modelo id do modelo a ser utilizado na certidão
	 * @return texto da certidão em html.
	 * @throws Exception
	 */
	public String montaConteudoPorModelo(CertidaoNegativaPositivaDt certidao, UsuarioDt servidor, String id_modelo) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
		texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacao(certidao, texto);
		texto = montaCertidaoProcessosLaco(certidao,texto);
		return texto;
	}
	
	/**
	 * Monta o conteúdo de uma certidão Negativa/Positiva
	 * @param certidao Dt contendo os dados da certidão
	 * @param servidor Dt contendo os dados do servidor
	 * @param id_modelo id do modelo a ser utilizado na certidão
	 * @return texto da certidão em html.
	 * @throws Exception
	 */
	public String montaConteudoPorModelo(CertidaoSegundoGrauNegativaPositivaDt certidao, UsuarioDt servidor, String id_modelo) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
		texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacao(certidao, texto);
		texto = montaCertidaoProcessosLaco(certidao,texto);
		return texto;
		}
	
	
	/**
	 * Monta o conteúdo de uma certidão de Antecedentes Criminais
	 * @param certidao Dt contendo os dados da certidão
	 * @param servidor Dt contendo os dados do servidor
	 * @param id_modelo id do modelo a ser utilizado na certidão
	 * @return texto da certidão em html.
	 * @throws Exception
	 */
	public String montaConteudoPorModelo(CertidaoAntecedenteCriminalDt certidao, UsuarioDt servidor, String id_modelo) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
		texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacao(certidao, texto);
		texto = montaProcessosAntecedenteCriminal(certidao, texto);

		return texto;
		}
	
	/**
	 * Monta o conteúdo de uma certidão de Prática Forense
	 * @param certidao Dt contendo os dados da certidão
	 * @param servidor Dt contendo os dados do servidor
	 * @param id_modelo id do modelo a ser utilizado na certidão
	 * @return texto da certidão em html.
	 * @throws Exception
	 */
	public String montaConteudoPorModelo(CertidaoPraticaForenseDt certidao, UsuarioDt servidor, String id_modelo) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
	    texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacao(certidao, texto);
		texto = montaQuantidadeProcesso(certidao,texto);
		texto = montaCertidaoProcessosLaco(certidao,texto);
		
		return texto;
	}

	/**
	 * Adiciona no modelo a quantidade de processos figurados pelo advogado/promotor requerente.
	 *  
	 * @param certidao
	 * @param texto
	 * @return texto com a variável certidao.pratica.forense.quantidade substituída pela quantidade de processos do Dt.
	 */
	private String montaQuantidadeProcesso(CertidaoPraticaForenseDt certidao, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PRATICA_FORENSE_QUANTIDADE +"[\\s]*[}]", format(certidao.getQuantidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PRATICA_FORENSE_DATA_FINAL +"[\\s]*[}]", format(certidao.getMesTexto(certidao.getMesFinal())+ " de " + certidao.getAnoFinal()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PRATICA_FORENSE_DATA_INICIAL +"[\\s]*[}]", format(certidao.getMesTexto(certidao.getMesInicial())+ " de " + certidao.getAnoInicial()));
		
		return texto;
	}

	public String montaConteudoPorModeloMemoria(CertidaoNegativaPositivaDt certidao, UsuarioDt servidor, String id_modelo) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacao(certidao, texto);
		texto = montaCertidaoProcessosLaco(certidao,texto);
		return texto;
		}
	/**
	 * Monta o conteúdo de uma certidão de Antecedentes Criminais com os dados que se encontram na memória
	 * incluindo o Dt do modelo a ser utilizado.
	 * @param certidao Dt contendo os dados da certidão
	 * @param servidor Dt contendo os dados do servidor
	 * @param modelo dt do modelo a ser utilizado na certidão
	 * @return texto da certidão em html.
	 * @throws Exception
	 */
	public String montaConteudoPorModeloMemoria(CertidaoAntecedenteCriminalDt certidao, UsuarioDt servidor, ModeloDt modelo) throws Exception{
		String texto = modelo.getTexto();
		texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros( texto);
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacao(certidao, texto);
		texto = montaProcessosAntecedenteCriminal(certidao, texto);
		
		return texto;
		}
	
	
	
//	public String montaConteudoPorModeloExecucao(CertidaoDt certidao, UsuarioDt servidor, String id_modelo){
//		String texto = "";
//
//		try{
//			texto = super.consultarId(id_modelo).getTexto();
//		} catch(Exception ex) {
//			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
//		}
//		
//		texto = montaServentia(servidor.getId_Serventia(), texto);
//		texto = montaOutros(servidor, texto);
//		texto = montaUsuario(servidor, texto);
//		texto = montaCertidaoIdentificacao(certidao, texto);
//		texto = montaCertidaoCircunstanciadaProcessoLaco(certidao,texto);
//		texto = texto.replaceAll("[$][{][\\s]*"+PROCESSO_NUMERO+"[\\s]*[}]", format(certidao.getProcessoNumeroCompleto()));
//		return texto;
//	}
	public String montaModeloCertidaoCircunstanciada(ProcessoDt processoDt, String texto) throws Exception{
        CertidaoDt certidaoDt = new CertidaoDt();
        
        List listaProcessoAcaoPenal = new ProcessoExecucaoNe().listarAcoesPenaisComCondenacoes(processoDt.getId());
        
		List listaAcaoPenalDt = new ArrayList();
		
		for (int i=0; i<listaProcessoAcaoPenal.size(); i++){
			ProcessoExecucaoDt processoExecucao = (ProcessoExecucaoDt)listaProcessoAcaoPenal.get(i);
			
			//consulta modalidades
			processoExecucao.setListaModalidade(new ProcessoEventoExecucaoNe().listarModalidadesDaAcaoPenal(processoExecucao.getId()));
			//consulta sursis
			processoExecucao.setEventoSursisDt(new ProcessoEventoExecucaoNe().consultarProcessoEventoExecucao(processoExecucao.getId(), String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS)));
			if (processoExecucao.getEventoSursisDt() != null)
				processoExecucao.setTempoTotalSursisAnos(processoExecucao.getEventoSursisDt().getQuantidade());
			
			AcaoPenalDt acaoPenal = new AcaoPenalDt();
			acaoPenal.setNumeroAcaoPenal(processoExecucao.getNumeroAcaoPenal());
			acaoPenal.setVaraOrigem(processoExecucao.getVaraOrigem());
			acaoPenal.setCidadeOrigem(processoExecucao.getCidadeOrigem() + " / " + processoExecucao.getEstadoOrigem());
			acaoPenal.setDataAcordao(processoExecucao.getDataAcordao());
			acaoPenal.setDataDenuncia(processoExecucao.getDataDenuncia());
			acaoPenal.setDataDistribuicao(processoExecucao.getDataDistribuicao());
			acaoPenal.setDataPronuncia(processoExecucao.getDataPronuncia());
			acaoPenal.setDataSentenca(processoExecucao.getDataSentenca());
			acaoPenal.setDataTransitoJulgado(processoExecucao.getDataTransitoJulgado());
			acaoPenal.setDataAdmonitoria(processoExecucao.getDataAdmonitoria());
			acaoPenal.setLocalCumprimentoPena(processoExecucao.getLocalCumprimentoPena());
			acaoPenal.setPenaExecucaoTipo(processoExecucao.getPenaExecucaoTipo());
			acaoPenal.setRegimeExecucao(processoExecucao.getRegimeExecucao());
			acaoPenal.setListaCondenacoes(processoExecucao.getListaCondenacoes());
			acaoPenal.setListaModalidades(processoExecucao.getListaModalidade());
			acaoPenal.setTempoTotalSursisAnos(processoExecucao.getTempoTotalSursisAnos());
			listaAcaoPenalDt.add(acaoPenal);
		}
		certidaoDt.setListaProcesso(listaAcaoPenalDt);
		
        texto = montaCertidaoCircunstanciadaProcessoLaco(certidaoDt,texto);
        return texto;
    }


	private String montaCertidaoProcessosLaco(CertidaoNegativaPositivaDt certidao, String texto) {
		texto = texto.replaceAll("[\\n\\r]+", "");
		
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_TEM_FIM_JUDICIAL +"[\\s]*[}]", certidao.temFimJudical() ? "": "NÃO TEM VALOR PARA FINS JUDICIAIS");
		String patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List processos = certidao.getListaProcessos();
			
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			this.montaProcessoCertidaoNegativaPositiva(builder, matcher, 1, processos,certidao.getNome(),certidao.getNumeroGuia());

			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}

		return texto;
	}
	
	private String montaCertidaoProcessosLaco(CertidaoSegundoGrauNegativaPositivaDt certidao, String texto) {
		texto = texto.replaceAll("[\\n\\r]+", "");
		
		String patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List processos = certidao.getListaProcesso();
			
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			this.montaProcessoCertidaoSegundoGrauNegativaPositiva(builder, matcher, 1, processos);

			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}

		return texto;
	}
	
	
	


	private String montaProcessosAntecedenteCriminal(CertidaoAntecedenteCriminalDt certidao, String texto) {
		//TODO terminar
		texto = texto.replaceAll("[\\n\\r]+", "");
		int qtdadeProcessos = certidao.getListaProcessosImprimirCertidao().size();
		
		int numProc = 1;
		int uPagina = (qtdadeProcessos - 1) % 4;
		int paginas = ((qtdadeProcessos - 1) - uPagina) / 4;
		paginas++;
		if(uPagina > 2) {
			paginas+=2;
		} else {
			paginas++;
		}
		int pagina = 2;
		texto = texto.replaceAll("[\\n\\r]+", "");

		String patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List processos = certidao.getListaProcessoComarca();
			
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			if(!processos.isEmpty()) {
				Iterator itProcessos = processos.iterator();
				
				while (itProcessos.hasNext()) {
					ProcessoAntecedenteCriminalDt processo = (ProcessoAntecedenteCriminalDt) itProcessos.next();
					builder.append(this.montaConteudoProcesso(processo, matcher.group(1),numProc, pagina,paginas));
					numProc++;
					if (numProc > 5 && (numProc - 1) % 4 == 0 || numProc ==5) {
						pagina++;
					
					}
				}
				
			} else {
				builder.append("Nenhum Processo");
			}
			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}
		
		
		 patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INFORMATIVO_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_INFORMATIVO_FIM + "[\\s]*[}]";
		 pattern = Pattern.compile(patternStr);
		 matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List processos = certidao.getListProcessoInformativo();
			
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			if(!processos.isEmpty()) {
				Iterator itProcessos = processos.iterator();
				
				while (itProcessos.hasNext()) {
					ProcessoAntecedenteCriminalDt processo = (ProcessoAntecedenteCriminalDt) itProcessos.next();
					builder.append(this.montaConteudoProcesso(processo, matcher.group(1),numProc, pagina,paginas));
					numProc++;
					if (numProc > 5 && (numProc - 1) % 4 == 0 || numProc ==5) {
						pagina++;
					
					}
				}
			} else {
				builder.append("Nenhum Processo");
			}
			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}

		return texto;
		
	}
	
	/**
	 * Monta a relação de processos da certidão de Prática Forense
	 * @param certidaoDt
	 * @param texto
	 * @return texto do modelo com as variáveis substituidas
	 */
	private String montaCertidaoProcessosLaco(CertidaoPraticaForenseDt certidao, String texto) {
		texto = texto.replaceAll("[\\n\\r]+", "");

		String patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List processos = certidao.getListaProcesso();
			
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			this.montaProcessoPraticaForense(builder, matcher, 1, processos);

			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}

		return texto;
		
		
	}

	/**Metodo responsável por montar os processos da Certidão de Pratica Forense
	 * @param construtor
	 * @param matcher
	 * @param grupo
	 * @param processos
	 */
	private void montaProcessoPraticaForense(StringBuilder construtor, Matcher matcher, int grupo, List processos) {
		Iterator itProcessos = processos.iterator();

		while (itProcessos.hasNext()) {
			ProcessoCertidaoPraticaForenseDt processo = (ProcessoCertidaoPraticaForenseDt) itProcessos.next();

			construtor.append(this.montaConteudoProcesso(processo, matcher.group(grupo)));
		}
		
	}
	
	

	private void montaProcessoCertidaoNegativaPositiva(StringBuilder construtor, Matcher matcher, int grupo,
			List processos, String nome, String guia) {
		
		Iterator itProcessos = processos.iterator();
		int i = 1;
		int uPagina = (processos.size() - 1) % 4;
		int paginas = ((processos.size() - 1) - uPagina) / 4;
		paginas++;
		boolean separar = false;
		if(uPagina > 2) {
			paginas+=2;
		} else {
			paginas++;
		}
		int pagina = 2;
		
		while (itProcessos.hasNext()) {
			ProcessoCertidaoPositivaNegativaDt processo = (ProcessoCertidaoPositivaNegativaDt) itProcessos.next();
			if(paginas - 1 == pagina && uPagina > 2 && !separar && i == processos.size()) {
				separar = true;
				pagina++;
			}
			construtor.append(this.montaConteudoProcesso(processo, matcher.group(grupo),i,nome,guia,pagina,paginas,separar));
			i++;
			if (i > 5 && (i - 1) % 4 == 0 || i ==5) {
				pagina++;
			
			}

		}
		
	}
	
	private void montaProcessoCertidaoSegundoGrauNegativaPositiva(StringBuilder construtor, Matcher matcher, int grupo,
			List processos) {
		
		Iterator itProcessos = processos.iterator();

		while (itProcessos.hasNext()) {
			ProcessoCertidaoSegundoGrauPositivaNegativaDt processo = (ProcessoCertidaoSegundoGrauPositivaNegativaDt) itProcessos.next();

			construtor.append(this.montaConteudoProcesso(processo, matcher.group(grupo)));
		}
		
	}
	
	

	private String montaConteudoProcesso(ProcessoCertidaoPositivaNegativaDt processo, String texto,int i, String nome,String guia, int pagina, int qtdPaginas,boolean separar) {
		
		texto = texto.replaceAll("[$][{][\\s]*"+PROCESSO_LACO_NUMERO+"[\\s]*[}]", format(i));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_NUMERO+"[\\s]*[}]", format(processo.getProcessoNumeroCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_AVERBACAO_CUSTA+"[\\s]*[}]", format(processo instanceof ProcessoCertidaoPositivaNegativaDebitoDt || (processo.getAverbacao() != null && !processo.getAverbacao().isEmpty() && !processo.getAverbacao().equals(" ")) ? "PROCESSO COM AVERBAÇÃO DE CUSTAS": ""));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_SERVENTIA+"[\\s]*[}]", format(processo.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_TIPO +"[\\s]*[}]", format(processo.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVENTE +"[\\s]*[}]", format( processo.getCertidaoPromoventeNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVIDO +"[\\s]*[}]", format(processo.getPromovidoNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_ADVOGADO_PROMOVENTE+"[\\s]*[}]", format(processo.getNomeAdvogadoPromovente()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_ADVOGADO_PROMOVIDO +"[\\s]*[}]", format(processo.getNomeAdvogadoPromovido()));
		String data = processo.getDataRecebimento();
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_DATA_DISTRIBUICAO+"[\\s]*[}]", format(data));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_VALOR_DA_ACAO +"[\\s]*[}]", format(processo.getValor()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_ASSUNTO +"[\\s]*[}]", format(processo.assuntoToString()));
		
		if (i == 1 && !separar) {
			texto = texto.concat("<style>.break {page-break-before: always; }</style>");
			texto = texto.concat("<h1 class=\"break\"><span style=\"font-size:10px;\">Continuação da certidão de " + nome + " página " + pagina + " de "+qtdPaginas+" páginas emitida com a guia número: "+guia+ "</span></h1>");
		}
		if (i > 5 && (i - 1) % 4 == 0 || i ==5 && !separar) {
			texto = texto.concat("<h1 class=\"break\"><span style=\"font-size:10px;\">Continuação da certidão de " + nome + " página " + pagina + " de "+qtdPaginas+" páginas emitida com a guia número: "+guia+ "</span></h1>");
		}
		if(separar) {
			texto = texto.concat("<h1 class=\"break\"><span style=\"font-size:10px;\">Continuação da certidão de " + nome + " página " + pagina + " de "+qtdPaginas+" páginas emitida com a guia número: "+guia+ "</span></h1>");
		}
		
		
		return texto;
	}
	
	

	private String montaConteudoProcesso(ProcessoCertidaoSegundoGrauPositivaNegativaDt processo, String texto) {
		
		
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_NUMERO+"[\\s]*[}]", format(processo.getProcessoNumeroCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_SERVENTIA+"[\\s]*[}]", format(processo.getCamara()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_TIPO +"[\\s]*[}]", format(processo.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVENTE +"[\\s]*[}]", format( processo.getPromoventeNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVIDO +"[\\s]*[}]", format(processo.getPromovidoNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_TIPO +"[\\s]*[}]", format(processo.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_FASE +"[\\s]*[}]", format(processo.getFaseAtual()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_RELATOR +"[\\s]*[}]", format(processo.getRelator()));
	 

		
		return texto;
	}
	
	/** Monta o conteudo de um processo de pratica forense passado o texto com as variáveis.
	 * @param processo
	 * @param texto
	 * @return texto com as variáveis alteradas.
	 */
	private String montaConteudoProcesso(ProcessoCertidaoPraticaForenseDt processo, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_NUMERO+"[\\s]*[}]", format(processo.getProcessoNumeroCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_SERVENTIA+"[\\s]*[}]", format(processo.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_TIPO +"[\\s]*[}]", format(processo.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVENTE +"[\\s]*[}]", format(processo.getPromoventeProcessoParte()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVIDO +"[\\s]*[}]", format(processo.getPromovidoProcessoParte()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_ADVOGADO_PROMOVENTE+"[\\s]*[}]", format(processo.getPromoventeAdvogadoProcessoParte()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_ADVOGADO_PROMOVIDO +"[\\s]*[}]", format(processo.getPromovidoAdvogadoProcessoParte()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_DATA_DISTRIBUICAO+"[\\s]*[}]", format(processo.getDataRecebimentoFormatada()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PRATICA_FORENSE_INICIO_ATUACAO +"[\\s]*[}]", format(processo.getInicioAtuacaoFormatada()));
		
		return texto;
	}
	
	private String montaConteudoBeneficio(BeneficioCertidaoAntecedenteCriminalDt beneficio, String texto) {
		String benef = beneficio.getBeneficio();
		String dataInicio = beneficio.getData_beneficio_inicio();
		String dataFim = beneficio.getData_beneficio_fim();

		texto = texto.replaceFirst("[$][{][\\s]*"+CERTIDAO_BENEFICIO_INICIO +"[\\s]*[}]", format(dataInicio));
		texto = texto.replaceFirst("[$][{][\\s]*"+CERTIDAO_BENEFICIO_FINAL +"[\\s]*[}]", format(dataFim));
		texto = texto.replaceFirst("[$][{][\\s]*"+CERTIDAO_BENEFICIO +"[\\s]*[}]", benef);
		
		return texto;
		
	}
	private void montaBeneficioCertidaoAntecedenteCriminal(StringBuilder construtor, Matcher matcher, int grupo,
			List beneficios) {
		
		Iterator itBeneficios = beneficios.iterator();

		while (itBeneficios.hasNext()) {
			BeneficioCertidaoAntecedenteCriminalDt beneficio = (BeneficioCertidaoAntecedenteCriminalDt) itBeneficios.next();

			construtor.append(this.montaConteudoBeneficio(beneficio, matcher.group(grupo)));
		}
		
	}
	private String montaBeneficioLaco(ProcessoAntecedenteCriminalDt processo, String texto) {
		texto = texto.replaceAll("[\\n\\r]+", "");

		String patternStr = "<!--inicio_beneficio-->.*?<!--fim_beneficio-->";
		//String patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List beneficios = processo.getBeneficioList();
			
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			if(!beneficios.isEmpty()) {
			this.montaBeneficioCertidaoAntecedenteCriminal(builder, matcher,0, beneficios);
			} else {
				builder.append("");
			}
			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}

		return texto;
	}
	
	private String montaConteudoProcesso(ProcessoAntecedenteCriminalDt processo, String texto, int i,int pagina, int qtdPaginas) {
		
		texto = montaBeneficioLaco(processo, texto);	
		texto = texto.replaceAll("[$][{][\\s]*"+PROCESSO_LACO_NUMERO+"[\\s]*[}]", format(i));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_NUMERO+"[\\s]*[}]", format(processo.getProcessoNumeroCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_SERVENTIA+"[\\s]*[}]", format(processo.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_TIPO +"[\\s]*[}]", format(processo.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVENTE +"[\\s]*[}]", format(processo.getBuscadaProcessoParteTipo() == ProcessoParteTipoDt.POLO_ATIVO_CODIGO ? processo.getPromovidoNome(): processo.promoventeToString()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVIDO +"[\\s]*[}]", format(processo.getBuscadaProcessoParteTipo() == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO ? processo.getPromovidoNome(): processo.promoventeToString()));
		if(processo.getSistema().equals("SPG")) {
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_DATA_DISTRIBUICAO+"[\\s]*[}]", processo.getDataRecebimento());
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_ASSUNTO +"[\\s]*[}]", format(processo.getInfracaoCertidao()));
			texto = texto.replaceAll("Assunto", "Lei");
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_FASE +"[\\s]*[}]", format(processo.getFase()));
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_DATABAIXA +"[\\s]*[}]", format(processo.getDataBaixa()));
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_MOTIVOBAIXA +"[\\s]*[}]",format(processo.getMotivoBaixa()));
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_DATARECEBIMENTODENUNCIA+"[\\s]*[}]", format(processo.getDataRecebimentoDenuncia()));

		} else {
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_DATA_DISTRIBUICAO+"[\\s]*[}]", format(Funcoes.FormatarDataHoraMinuto(processo.getDataRecebimento())));
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_ASSUNTO +"[\\s]*[}]", format(processo.assuntoToString().equals("null") ? "" : processo.assuntoToString()));
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_FASE +"[\\s]*[}]", format(processo.getStatus()));
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_DATABAIXA +"[\\s]*[}]", format(processo.getDataBaixa()));
			texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_MOTIVOBAIXA +"[\\s]*[}]",format(processo.getMotivoBaixa()));
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_DATARECEBIMENTODENUNCIA+"[\\s]*[}]", format(Funcoes.FormatarDataHoraMinuto(processo.getDataRecebimentoDenuncia())));

			texto = texto.replaceAll("Fase", "Status");
			texto = texto.replaceAll("<tr id=\"databaixa\">.*?</tr>", "");			
		}
				
		texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_COMARCA +"[\\s]*[}]", format(processo.getComarca()));
		texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_DATA_DISTRIBUICAO +"[\\s]*[}]", format(processo.getDataRecebimento()));
		texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_DATA_TRANSITO_JULGADO +"[\\s]*[}]", format(processo.getDataTransitoJulgado()));
		texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_SENTENCA +"[\\s]*[}]", format(processo.getSentenca()));
		texto = texto.replaceAll("[$][{][\\s]*"+ CERTIDAO_PROCESSO_FILIACAO +"[\\s]*[}]", format(processo.getPromovidoNomeMae()));
		
		if (i == 1) {
			texto = texto.concat("<style>.break {page-break-before: always; }</style>");
			texto = texto.concat("<h1 class=\"break\"><span style=\"font-size:10px;\"> página " + pagina + " de "+qtdPaginas+" </span></h1>");
		}
		if (i > 5 && (i - 1) % 4 == 0 || i ==5) {
			texto = texto.concat("<h1 class=\"break\"><span style=\"font-size:10px;\"> página " + pagina + " de "+qtdPaginas+" </span></h1>");
		}	
		
		return texto;
	}

	public String montaCertidaoCircunstanciadaProcessoLaco(CertidaoDt certidao, String texto) {
		texto = texto.replaceAll("[\\n\\r]+", "");

		String patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			List processos = certidao.getListaProcesso();
			StringBuilder builder = new StringBuilder();

			//------------Monta dados dos processos------------
//			this.montaProcesso(builder, matcher, 1, processos);
			Iterator itProcessos = processos.iterator();

			while (itProcessos.hasNext()) {
				AcaoPenalDt dadosProcesso = (AcaoPenalDt) itProcessos.next();
				builder.append(this.montaConteudoCertidaoCircunstanciadaProcesso(dadosProcesso, matcher.group(1)));
			}
			//------------------------------------------------
			
			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}
		return texto;
	}
		
	private String montaConteudoCertidaoCircunstanciadaProcesso(AcaoPenalDt dadosProcesso, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_NUMERO_ACAO_PENAL +"[\\s]*[}]", format(dadosProcesso.getNumeroAcaoPenal()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_CIDADE_ORIGEM +"[\\s]*[}]", format(dadosProcesso.getCidadeOrigem()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_VARA_ORIGEM +"[\\s]*[}]", format(dadosProcesso.getVaraOrigem()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_DISTRIBUICAO +"[\\s]*[}]", format(dadosProcesso.getDataDistribuicao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_DENUNCIA +"[\\s]*[}]", format(dadosProcesso.getDataDenuncia()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_SENTENCA +"[\\s]*[}]", format(dadosProcesso.getDataSentenca()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_TRANSITO +"[\\s]*[}]", format(dadosProcesso.getDataTransitoJulgado()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_PRONUNCIA +"[\\s]*[}]", format(dadosProcesso.getDataPronuncia()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_ACORDAO +"[\\s]*[}]", format(dadosProcesso.getDataAcordao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_DATA_ADMONITORIA +"[\\s]*[}]", format(dadosProcesso.getDataAdmonitoria()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_ESTABELECIMENTO_PENAL +"[\\s]*[}]", format(dadosProcesso.getLocalCumprimentoPena()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_REGIME +"[\\s]*[}]", format(dadosProcesso.getRegimeExecucao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_TIPO_PENA +"[\\s]*[}]", format(dadosProcesso.getPenaExecucaoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CIRCUNSTANCIADA_PROCESSO_CONDENACOES +"[\\s]*[}]", format(dadosProcesso.getCondenacoesToString()));
		
		return texto;
	}
	
	
	
	
	/** 
	 * Monta a identificação, ou seja, os dados do requerente de uma Certidão Negativa Positiva.
	 * @param certidaoDt Dt contendo os dados do requerente
	 * @param texto texto do modelo da certidão
	 * @return texto do modelo da certidão com os dados carregados
	 */
	private String montaCertidaoIdentificacao(CertidaoNegativaPositivaDt certidao, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE+"[\\s]*[}]", format(certidao.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_PROFISSAO+"[\\s]*[}]", format(certidao.getProfissao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_ESTADO_CIVIL+"[\\s]*[}]", format(certidao.getEstadoCivil()));
		String sexo = certidao.getSexo();
		sexo = Funcoes.removeEspacosExcesso(sexo);
		if (sexo.matches("[fF][eE][mM][iI][nN][iIíÍ][nN][oO]")) {
			sexo = "Feminino"; 
		} else if(sexo.matches("[Mm][aA][sS][cC][uU][lL][iI][nN][oO]")) {
			sexo = "Masculino";
		} else {
			sexo = "";
		}
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_SEXO+"[\\s]*[}]", format(sexo.matches("[M]|Masculino")?"Masculino":"Feminino"));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ+"[\\s]*[}]", format(certidao.getCpfCnpj()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_RG+"[\\s]*[}]", format(certidao.getIdentidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DOMICILIO+"[\\s]*[}]", format(certidao.getDomicilio()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NACIONALIDADE+"[\\s]*[}]", format(certidao.getNacionalidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NOME_MAE+"[\\s]*[}]", format(certidao.getNomeMae()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NOME_PAI+"[\\s]*[}]", format(certidao.getNomePai()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DATA_NASCIMENTO+"[\\s]*[}]", format(certidao.getDataNascimento()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_VALOR_TAXA_JUDICIARIA+"[\\s]*[}]", format(certidao.getValorTaxaFormatado()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_VALOR_CERTIDAO+"[\\s]*[}]", format(certidao.getValorCertidao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_VALOR_TOTAL+"[\\s]*[}]", format(certidao.getValortotalFormatado()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_NUMERO_GUIA+"[\\s]*[}]", format(certidao.getNumeroGuia()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_DATA_RECEITA+"[\\s]*[}]", format(certidao.getDataPagamento()));
		
		return texto;
	}
	
	
	/** 
	 * Monta a identificação, ou seja, os dados do requerente de uma Certidão Negativa Positiva.
	 * @param certidaoDt Dt contendo os dados do requerente
	 * @param texto texto do modelo da certidão
	 * @return texto do modelo da certidão com os dados carregados
	 */
	private String montaCertidaoIdentificacao(CertidaoSegundoGrauNegativaPositivaDt certidao, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE+"[\\s]*[}]", format(certidao.getNome().toUpperCase()));
//		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_PROFISSAO+"[\\s]*[}]", format(certidao.getProfissao()));
//		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_ESTADO_CIVIL+"[\\s]*[}]", format(certidao.getEstadoCivil()));
//		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_SEXO+"[\\s]*[}]", format(certidao.getSexo().matches("[M]|Masculino")?"Masculino":"Feminino"));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ+"[\\s]*[}]", format(certidao.getCpfCnpj()));
//		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_RG+"[\\s]*[}]", format(certidao.getIdentidade()));
//		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DOMICILIO+"[\\s]*[}]", format(certidao.getDomicilio()));
//		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NACIONALIDADE+"[\\s]*[}]", format(certidao.getNacionalidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NOME_MAE+"[\\s]*[}]", format(certidao.getNomeMae()));
//		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NOME_PAI+"[\\s]*[}]", format(certidao.getNomePai()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DATA_NASCIMENTO+"[\\s]*[}]", format(certidao.getDataNascimento()));
		
		return texto;
	}
	
	
	
	/** 
	 * 	Monta a identificação, ou seja, os dados do requerente de uma Certidão de Antecedentes Criminais.
	 * @param certidao  Dt contendo os dados do requerente
	 * @param texto texto do modelo da certidão
	 * @return texto do modelo da certidão com os dados carregados
	 */
	private String montaCertidaoIdentificacao(CertidaoAntecedenteCriminalDt certidao, String texto) {
		
		
		
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE+"[\\s]*[}]", format(certidao.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_PROFISSAO+"[\\s]*[}]", format(certidao.getProfissao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_ESTADO_CIVIL+"[\\s]*[}]", format(certidao.getEstadoCivil()));
		String sexo = certidao.getSexo();
		sexo = Funcoes.removeEspacosExcesso(sexo);
		if (sexo.matches("F")) {
			sexo = "Feminino"; 
		} else if(sexo.matches("M")) {
			sexo = "Masculino";
		} else {
			sexo = "";
		}	
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_SEXO+"[\\s]*[}]", format(sexo));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ+"[\\s]*[}]", format(certidao.getCpfCnpj()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_RG+"[\\s]*[}]", format(certidao.getIdentidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DOMICILIO+"[\\s]*[}]", format(certidao.getDomicilio()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NACIONALIDADE+"[\\s]*[}]", format(certidao.getNacionalidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NOME_MAE+"[\\s]*[}]", format(certidao.getNomeMae()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NOME_PAI+"[\\s]*[}]", format(certidao.getNomePai()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DATA_NASCIMENTO+"[\\s]*[}]", format(certidao.getDataNascimento()));
		
		return texto;
	}
	
	/** 
	 * 	Monta a identificação, ou seja, os dados do requerente de uma Pratica Forense.
	 * @param certidao  Dt contendo os dados do requerente
	 * @param texto texto do modelo da certidão
	 * @return texto do modelo da certidão com os dados carregados
	 */
	private String montaCertidaoIdentificacao(CertidaoPraticaForenseDt certidao, String texto) {
		
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE+"[\\s]*[}]", format(certidao.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_ESTADO_CIVIL+"[\\s]*[}]", format(certidao.getEstadoCivil()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_SEXO+"[\\s]*[}]", format(certidao.getSexo().equals("M")?"Masculino":certidao.getSexo().equals("F") ? "Feminino": ""));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ+"[\\s]*[}]", format(Funcoes.formataCPF(certidao.getCpf())));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_RG+"[\\s]*[}]", format(certidao.getIdentidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DOMICILIO+"[\\s]*[}]", format(certidao.getDomicilio()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NATURALIDADE +"[\\s]*[}]", format(certidao.getNaturalidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_OAB+"[\\s]*[}]", format(certidao.getOab() + " - " + certidao.getOabComplemento() + " - " + certidao.getOabUf()));
		
		return texto;
	}
	
	
	/**
	 * Monta conteudo de uma pendencia
	 * @author Ronneesley Moura Teles
	 * @since 08/04/2008
	 * @param PendenciaDt pendencia, pendencia a ser analisada 
	 * @param servidor
	 * @param texto
	 * @return String
	 * @throws Exception
	 */
	public String montaConteudo(PendenciaDt pendencia, UsuarioDt servidor, String texto) throws Exception{
		
//		if (pendencia != null && Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO)
//			return this.montaConteudo(pendencia.getId_Processo(),"9"+Funcoes.completarZeros(pendencia.getId(),8), pendencia.getPrazo(), servidor, pendencia.getId_ProcessoParte(), texto);
//		else
			return this.montaConteudo(pendencia.getId_Processo(), pendencia.getId(), pendencia.getPrazo(), servidor, pendencia.getId_ProcessoParte(), texto);
	}

	/**
	 * Encapsulamento da montagem de conteudo, apartir do processo
	 * @author Ronneesley Moura Teles
	 * @since 07/05/2008 10:18
	 * @param processo
	 * @param servidor
	 * @param texto
	 * @return String
	 * @throws Exception
	 */
	public String montaConteudo(ProcessoDt processo, UsuarioDt servidor, String texto) throws Exception{
		return this.montaConteudo(processo.getId_Processo(), null, null, servidor, null, texto);
	}

	/**
	 * Monta o conteudo a partir de um processo preenchido
	 * @author Ronneesley Moura Teles
	 * @since 27/05/2008 13:25
	 * @param String id_modelo, id do modelo
	 * @param ProcessoDt processoDt, bean do processo
	 * @param UsuarioDt usuario, usuario
	 * @return String
	 * @throws Exception
	 */
	public String montaConteudo(String id_modelo, ProcessoDt processo, UsuarioDt usuario, String arquivoTipoCodigo) throws Exception{
		ProcessoParteDt poloAtivo = null;
		ProcessoParteDt poloPassivo = null;
		ProcessoParteDt vitima = null;

		if (processo != null) {
			
			if (processo.getRecursoDt() != null && processo.getRecursoDt().getId() != null &&  processo.getRecursoDt().getId().length()>0){
				if ( processo.getRecursoDt().getListaRecorrentesAtivos() != null &&  processo.getRecursoDt().getListaRecorrentesAtivos().size() > 0) {
					RecursoParteDt recursoParteDt = (RecursoParteDt) processo.getRecursoDt().getListaRecorrentesAtivos().get(0);
					poloAtivo = (ProcessoParteDt) recursoParteDt.getProcessoParteDt();
				}
	
				if ( processo.getRecursoDt().getListaRecorridosAtivos() != null &&  processo.getRecursoDt().getListaRecorridosAtivos().size() > 0) {
					RecursoParteDt recursoParteDt = (RecursoParteDt)  processo.getRecursoDt().getListaRecorridosAtivos().get(0);
					poloPassivo = (ProcessoParteDt) recursoParteDt.getProcessoParteDt();
				}
			} else {
				
				poloAtivo = processo.getPrimeiroPoloAtivo();								
				poloPassivo = processo.getPrimeiroPoloPassivo();
				
				vitima= processo.getPrimeiraVitima();
			}
		}
		
		//Consulta o texto do modelo especificado
		String texto = this.consultarId(id_modelo).getTexto();

		if (arquivoTipoCodigo.equals(String.valueOf(ArquivoTipoDt.CERTIDAO_CIRCUNSTANCIADA_CODIGO))){
			texto = montaModeloCertidaoCircunstanciada(processo, texto);
		}
		
		return this.montaConteudoMemoria(processo, poloAtivo, poloPassivo, vitima, null, usuario, texto, null);
	}
	
	/**
	 * Monta o conteudo a partir de um processo preenchido
	 * @param String id_modelo, id do modelo
	 * @param ProcessoDt processoDt, bean do processo
	 * @param String id_parte, id_parte
	 * @return String
	 * @throws Exception
	 */
	public String montaConteudoAcessoCodigo(String id_modelo, UsuarioNe UsuarioSessao, ProcessoDt processo, String id_parte) throws Exception {

		//Consulta o texto do modelo especificado
		ProcessoNe processoNe = new ProcessoNe();
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		String texto = this.consultarId(id_modelo).getTexto();
		texto = texto.replaceAll("[$][{][\\s]*"+PROCESSO_NUMERO+"[\\s]*[}]", format(processo.getProcessoNumeroCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_CODIGO_ACESSO + "[\\s]*[}]", processoNe.gerarCodigoAcessoProcesso(processo.getId(), id_parte));
	    
		ProcessoParteDt parte = processoParteNe.consultarId(id_parte);
		texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_PARTE_NOME + "[\\s]*[}]", format(parte.getNome()));
		
		texto = this.montaOutros(texto);
		texto = this.montaUsuario(UsuarioSessao.getUsuarioDt(), texto);

		return texto;
	}

	/**
	 * Monta conteudo utilizando os dados da memoria
	 * 	Retirado a funcao montaAudiencia, pois nao ha Audiencias
	 * @author Ronneesley Moura Teles
	 * @since 27/05/2008 15:26
	 * @param ProcessoDt processoDt, bean processo
	 * @param ProcessoParteDt promovente, bean do promovente
	 * @param ProcessoParteDt promovido, bean do promovido 
	 * @param String prazo, bean do prazo
	 * @param UsuarioDt servidor, bean do servidor
	 * @param String texto, String texto a ser baseado
	 * @return String
	 * @throws Exception
	 */
	protected String montaConteudoMemoria(ProcessoDt processoDt, ProcessoParteDt poloAtivo, ProcessoParteDt poloPassivo, ProcessoParteDt vitima, String prazo, UsuarioDt servidor, String texto, String id_ParteCumprimento) throws Exception{
		//Monta as partes do processo
		if (processoDt != null) {
			texto = this.montaPartesProcessoMemoria(processoDt, texto);

			//Consulta a serventia da movimentacao
			if (processoDt.getId_Serventia() != null && !processoDt.getId_Serventia().equals("")) texto = this.montaServentia(processoDt.getId_Serventia(), texto);

			//Monta dados do processo
			texto = this.montaProcesso(processoDt, texto, id_ParteCumprimento);
			
			//Monta responsaveis segundo grau
			texto = this.montaResponsaveis(texto, processoDt.getId(), processoDt.getId_Serventia());

			if (processoDt.getId().length() > 0) {
				//monta dados do processo de execução penal
				texto = this.montaDadosExecucaoPenal(processoDt.getId(), texto);
				
				//Monta audiencia
				texto = this.montaAudiencia(processoDt.getId(), texto);
				
				List<String[]> t = new AudienciaProcessoNe().consultarAudienciasPendentesProcesso(processoDt.getId(), false);
				if(processoDt.isSegundoGrau() && t != null && !t.isEmpty() && t.get(0) != null && !StringUtils.isEmpty(t.get(0)[0])) {
					AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarId(t.get(0)[0]);
					audienciaProcessoDt.setProcessoDt(processoDt);
					
					texto = this.montaAudienciaSessao(audienciaProcessoDt, texto, true);					
				}
			}
			
			//Consulta um promovente do processo
			if (poloAtivo != null) texto = this.montaConteudoParte(poloAtivo, texto, "processo");

			//Consultar o promovido
			if (poloPassivo != null) texto = this.montaConteudoParte(poloPassivo, texto, "processo");

			//Consultar o vitima
			if (vitima != null) texto = this.montaConteudoParte(vitima, texto, "processo");
			
			//Monta conteudo da parte
			if (id_ParteCumprimento != null && id_ParteCumprimento.length() > 0) {
				ProcessoParteDt parteCumprimento = new ProcessoParteNe().consultarId(id_ParteCumprimento);
				//Monta conteudo da parte de cumprimento
				texto = this.montaConteudoParte(parteCumprimento, texto, "cumprimento", "parte");
			}
		}

		//Monta o prazo da pendencia
		if (prazo != null && prazo.equals("") == false) texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_PRAZO_DIAS + "[\\s]*[}]", format(prazo));

		//Monta dados do usuario
		if (servidor != null) texto = this.montaUsuario(servidor, texto);

		//Monta outros
		if (servidor != null) texto = this.montaOutros(texto);

		//Retorna o tratamento do texto
		return this.tratamento(texto);
	}

	/**
	 * 
	 * @param texto
	 * @param idProcesso
	 * @param id_Serventia
	 * @return
	 * @throws Exception
	 */
	private String montaResponsaveis(String texto, String idProcesso, String id_Serventia) throws Exception{
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
		String juiz = "";
		String relator = "";
		String revisor = ""; 
		String vogal = "";
		String presidenteCamara = "";
		String presidenteTurma = "";
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			serventiaCargoDt = processoResponsavelNe.consultarRelator2Grau(idProcesso, obFabricaConexao);
			if (serventiaCargoDt != null)
				relator = serventiaCargoDt.getNomeUsuario();
			
			serventiaCargoDt = processoResponsavelNe.consultarRevisor2Grau(idProcesso, obFabricaConexao);
			if (serventiaCargoDt != null)
				revisor = serventiaCargoDt.getNomeUsuario();
			
			serventiaCargoDt = processoResponsavelNe.consultarVogal2Grau(idProcesso, obFabricaConexao);
			if (serventiaCargoDt != null)
				vogal = serventiaCargoDt.getNomeUsuario();
			
			serventiaCargoDt = serventiaCargoNe.getPresidenteSegundoGrau(id_Serventia, obFabricaConexao);
			if (serventiaCargoDt != null)
				presidenteCamara = serventiaCargoDt.getNomeUsuario();
			
			serventiaCargoDt = serventiaCargoNe.getPresidenteTurmaRecursal(id_Serventia, obFabricaConexao);
			if (serventiaCargoDt != null)
				presidenteTurma = serventiaCargoDt.getNomeUsuario();
			
			serventiaCargoDt = processoResponsavelNe.getJuizResponsavelProcesso(idProcesso, id_Serventia);
			if (serventiaCargoDt != null)
				juiz = serventiaCargoDt.getNomeUsuario();
			
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_JUIZ + "[\\s]*[}]", format(formataDado(juiz)));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_RELATOR + "[\\s]*[}]", format(formataDado(relator)));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_REVISOR + "[\\s]*[}]", format(formataDado(revisor)));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_VOGAL + "[\\s]*[}]", format(formataDado(vogal)));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_PRESIDENTE_CAMARA + "[\\s]*[}]", format(formataDado(presidenteCamara)));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_PRESIDENTE_TURMA + "[\\s]*[}]", format(formataDado(presidenteTurma)));
		
		processoResponsavelNe = null;
		serventiaCargoNe = null;
		serventiaCargoDt = null;
		
		return texto;
	}

	/**
	 * Monta o conteudo utilizando beans ja preenchidos
	 * @author Ronneesley Moura Teles
	 * @since 27/05/2008 10:41
	 * @param ProcessoDt processoDt, bean do processo
	 * @param ServentiaDt serventiaDt, bean da serventia
	 * @param ProcessoParteDt promovente, promovente
	 * @param ProcessoParteDt promovido, promovido
	 * @param id_Pendencia, id da pendência, será utilizado como código do mandado
	 * @param String prazo, prazo
	 * @param UsuarioDt servidor, 
	 * @param String texto, texto a ser montado
	 * @return String
	 * @throws Exception
	 */
	private String montaConteudo(ProcessoDt processoDt, ProcessoParteDt promovente, ProcessoParteDt promovido, String id_Pendencia, String prazo, UsuarioDt servidor, String id_ParteCumprimento, String texto) throws Exception{
		ProjudiPropriedades projudiPropriedade = ProjudiPropriedades.getInstance();
		//Monta as partes do processo
		if (processoDt != null) {
			texto = this.montaPartesProcesso(processoDt.getId(), texto);
			
			
			//Consulta a serventia da movimentacao
			if (processoDt.getId_Serventia() != null) texto = this.montaServentia(processoDt.getId_Serventia(), texto);

			//Monta dados do processo
			texto = this.montaProcesso(processoDt, texto, id_ParteCumprimento);

			//Consulta um promovente do processo
			if (promovente != null) texto = this.montaConteudoParte(promovente, texto, "processo");

			//Consultar o promovido
			if (promovido != null) texto = this.montaConteudoParte(promovido, texto, "processo");
			
			//monta dados dos responsáveis
			texto = this.montaResponsaveis(texto, processoDt.getId_Processo(), processoDt.getId_Serventia());

			//Consulta dados da parte específica da pendência
			if (id_ParteCumprimento != null && id_ParteCumprimento.length() > 0) {
				ProcessoParteDt parteCumprimento = new ProcessoParteNe().consultarId(id_ParteCumprimento);
				//Monta conteudo da parte de cumprimento
				texto = this.montaConteudoParte(parteCumprimento, texto, "cumprimento", "parte");
			}
			//Consulta a ultima audiencia de conciliacao do processo
			texto = this.montaAudiencia(processoDt.getId(), texto);
		}

		//Monta o id da pendencia
		if (id_Pendencia != null && id_Pendencia.length()>0)  texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_CODIGO + "[\\s]*[}]", format(id_Pendencia));
		
		//Monta o código de barra - Jelves 22/07/13
		if (id_Pendencia != null && id_Pendencia.length()>0)  texto =  texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_CODIGO_BARRA + "[\\s]*[}]", "<img src=" + projudiPropriedade.getLinkSistemaNaWEB() +  "/Modelo?PaginaAtual=7&amp;numero=" + id_Pendencia+ ">");

		//Monta o prazo da pendencia
		if (prazo != null && prazo.length()>0)  texto =  texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_PRAZO_DIAS + "[\\s]*[}]", format(prazo));

		//Monta dados do usuario
		if (servidor != null) texto = this.montaUsuario(servidor, texto);
		
		//Monta outros
		if (servidor != null) texto = this.montaOutros(texto);

		//Retorna o tratamento do texto
		return this.tratamento(texto);
	}

	/**
	 * Monta o conteudo, utilizando os valores detalhados
	 * @author Ronneesley Moura Teles
	 * @since 07/05/2008 10:15
	 * @param String id_processo, id do processo
	 * @param String id_Pendencia, id da pendência que será utilizado como código do mandado
	 * @param String prazo, prazo do processo
	 * @param UsuarioDt servidor, usuario
	 * @param String texto, texto do modelo
	 * @return String
	 * @throws Exception
	 */
	private String montaConteudo(String id_processo, String id_Pendencia, String prazo, UsuarioDt servidor, String id_ParteCumprimento, String texto) throws Exception{
		ProcessoDt processo = null;
		ProcessoParteDt promovente = null;
		ProcessoParteDt promovido = null;

		if (id_processo != null & !id_processo.trim().equals("")) {
			ProcessoNe processoNe = new ProcessoNe();
			ProcessoParteNe processoParteNe = new ProcessoParteNe();
			processo = processoNe.consultarId(id_processo);

			//Consulta um promovente do processo
			promovente = processoParteNe.consultarUmPromovente(id_processo);

			//Consultar o promovido		
			promovido = processoParteNe.consultarUmPromovido(id_processo);
			
			// Captura lista de assuntos do processo
			ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
			processo.setListaAssuntos(processoAssuntoNe.consultarAssuntosProcesso(id_processo));
			
		}

		return this.montaConteudo(processo, promovente, promovido, id_Pendencia, prazo, servidor, id_ParteCumprimento, texto);
	}

	/**
	 * Monta dados da audiencia
	 * 	Necessita de revisao para os horarios
	 *  Necessita de revisão para as variáveis antigas:
	 *  CUMPRIMENTO_AUDIENCIA_CONCILIACAO_DATA
	 *  CUMPRIMENTO_AUDIENCIA_CONCILIACAO_HORA
	 *  CUMPRIMENTO_AUDIENCIA_INSTRUCAO_DATA
	 *  CUMPRIMENTO_AUDIENCIA_INSTRUCAO_HORA
	 * @author Ronneesley Moura Teles
	 * @since 18/04/2008 - 10:58
	 * @param String id_processo, id do processo
	 * @param String texto, texto a ser substituido
	 * @return String
	 * @throws Exception
	 */
	protected String montaAudiencia(String id_processo, String texto) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();

		Date dtData = audienciaNe.consultarDataUltimaAudiencia(id_processo);
		String stData = format(Funcoes.FormatarData(dtData));
		String stHora = format(Funcoes.FormatarHora(dtData));
		
		if (stData != null && !stData.trim().equals("")) {
			//Formato de variáveis antigo
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_CONCILIACAO_DATA + "[\\s]*[}]", stData);
			//Pegar apenas hora
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_CONCILIACAO_HORA + "[\\s]*[}]", stHora);

			//Formato de variáveis novo
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_HORA + "[\\s]*[}]", stHora);
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_DATA + "[\\s]*[}]", stData);
			
			// jvosantos - 04/11/2019 17:30 - Corrigir data de fim da votação
			VotoNe votoNe = new VotoNe();
			final int TAMANHO_HORAS = 9; // " 00:00:00".length() == 9
			
			String dataFinal = votoNe.addDiasUteis(Funcoes.FormatarData(dtData), id_processo, VotoNe.PRAZO_SESSAO_VIRTUAL_DIAS);
			dataFinal = dataFinal.substring(0, dataFinal.length() - TAMANHO_HORAS) + " 18:00";
			
			texto = texto.replaceAll("[$][{][\\s]*" + SESSAO_HORA_FINAL + "[\\s]*[}]", format(dataFinal));
		}

		if (stData != null && !stData.trim().equals("")) {
			//Formato de variáveis antigos
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_INSTRUCAO_DATA + "[\\s]*[}]", stData);
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_INSTRUCAO_HORA + "[\\s]*[}]", stHora);

			//Formato de variáveis novo
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_HORA + "[\\s]*[}]", stHora);
			texto = texto.replaceAll("[$][{][\\s]*" + CUMPRIMENTO_AUDIENCIA_DATA + "[\\s]*[}]", stData);
		}

		return texto;
	}

	/**
	 * Monta dados do processo de execução penal
	 * @param id_processo: identificação do processo
	 * @param texto: texto a ser substituido
	 * @return String
	 * @throws Exception
	 */
	public String montaDadosExecucaoPenal(String id_processo, String texto) throws Exception{
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_CRIMES + "[\\s]*[}]", format(formataDado(new CondenacaoExecucaoNe().consultarCrimesAtivosProcesso(id_processo))));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_LOCAL_CUMPRIMENTO_PENA + "[\\s]*[}]", format(formataDado(new LocalCumprimentoPenaNe().consultarDescricaoComEndereco(id_processo))));
		return texto;
	}

	/**
	 * Monta dados do processo
	 * 	- Modificacao no retorno do processoNe
	 *  @author Ronneesley Moura Teles
	 *  @since 19/05/2008 16:16
	 * @author Ronneesley Moura Teles
	 * @since 17/04/2008 - 11:23 
	 * @param ProcessoDt processo, processo a ser montado
	 * @param String texto, texto a ser montado
	 * @return String
	 * @throws Exception 
	 */
	protected String montaProcesso(ProcessoDt processo, String texto, String id_parte) throws Exception{
		ProcessoNe processoNe = new ProcessoNe();
		String valor = processo.getValor();

		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_NUMERO + "[\\s]*[}]", format(Funcoes.formataNumeroProcesso(processo.getProcessoNumeroCompleto())));

		//Verifica se tem id o processo
		if (processo.getId() != null && !processo.getId().trim().equals("")) {
			List dependentes = processoNe.consultarProcessosDependentes(processo.getId());

			boolean temDependentes = false;
			if (dependentes != null) {
				if (dependentes.size() > 0) {
					String sDependentes = "";

					Iterator iterator = dependentes.iterator();

					int qtdDependentes = 0;
					while (iterator.hasNext()) {
						if (qtdDependentes++ != 0) sDependentes += ", ";

						ProcessoDt processoDt = (ProcessoDt) iterator.next();

						sDependentes += processoDt.getProcessoNumero();
					}

					texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_DEPENDENTE_NUMERO + "[\\s]*[}]", format(sDependentes));

					temDependentes = true;
				}
			}

			if (temDependentes == false) texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_DEPENDENTE_NUMERO + "[\\s]*[}]", format(""));
		}

		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_ACAO_TIPO + "[\\s]*[}]", format(processo.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_ACAO_VALOR + "[\\s]*[}]", format(valor));
//      Retirada da Geração do Código de Acesso		
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_CODIGO_ACESSO + "[\\s]*[}]", processoNe.gerarCodigoAcessoProcesso( processo.getId(), id_parte));

		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_CLASSE_CNJ + "[\\s]*[}]", format(processo.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_ASSUNTOS + "[\\s]*[}]", format(new ProcessoAssuntoDt().getListaAssuntosToString(processo.getListaAssuntos())));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_DATA_DISTRIBUICAO + "[\\s]*[}]", format(processo.getDataRecebimento()));
		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_DATA_TRANSITO_JULGADO + "[\\s]*[}]", format(processo.getDataTransitoJulgado()));
				
		return texto;
	}

	/**
	 * Monta conteudo da serventia
	 * @author Ronneesley Moura Teles
	 * @since 28/03/2008
	 * @param ServentiaDt serventia, Bean da serventia
	 * @param String texto, Texto a ser substituido
	 * @return
	 * @throws Exception 
	 */
	private String montaServentia(String id_Serventia, String texto) throws Exception{
		ServentiaDt serventiaDt = new ServentiaNe().consultarId(id_Serventia);
		ProjudiPropriedades projudiPropriedade = ProjudiPropriedades.getInstance();
		return montaServentia(serventiaDt, projudiPropriedade, texto);
	}
	
	private String montaServentia(ServentiaDt serventiaDt, ProjudiPropriedades projudiPropriedade, String texto) throws Exception{
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_NOME + "[\\s]*[}]", format(serventiaDt.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_COMARCA + "[\\s]*[}]", format(serventiaDt.getComarca()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_LOGRADOURO + "[\\s]*[}]", format(serventiaDt.getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_NUMERO + "[\\s]*[}]", format(serventiaDt.getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_COMPLEMENTO + "[\\s]*[}]", format(serventiaDt.getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_BAIRRO + "[\\s]*[}]", format(serventiaDt.getBairro()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_TELEFONE + "[\\s]*[}]", format(serventiaDt.getTelefone()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_CIDADE + "[\\s]*[}]", format(serventiaDt.getCidade()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_ESTADO_SIGLA + "[\\s]*[}]", format(serventiaDt.getEstado()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_ESTADO_COMPLETO + "[\\s]*[}]", format(projudiPropriedade.getNomeEstado()));
		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO_CEP + "[\\s]*[}]", format(serventiaDt.getCep()));
		
		return texto;
	}

	/**
	 * Monta as partes do processo
	 * @author Ronneesley Moura Teles
	 * @since 28/03/2008
	 * @param ProcessoDt processo, bean do processo
	 * @param String texto, Texto a ser modificado
	 * @return String
	 * @throws Exception
	 */
	private String montaPartesProcesso(String id_processo, String texto) throws Exception{
		//Seleciona todos os tipos de partes do processo
		/*
		ProcessoParteTipoNe processoParteTipoNe = new ProcessoParteTipoNe();
		List partesTipos = processoParteTipoNe.consultarDescricao("%", "0");
		Iterator it = partesTipos.iterator();

		//Para cada tipo procura e substitui as marcacoes para a parte
		while ( it.hasNext() ){
			ProcessoParteTipoDt processoParteTipoDt = (ProcessoParteTipoDt) it.next();
			String tipo = processoParteTipoDt.getProcessoParteTipo().toLowerCase(); //Tipo da parte do processo			
		}
		*/

		//Monta partes do tipo promoventes e promovidos
		texto = this.montaPartesLaco(id_processo, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, PROCESSO_POLOSATIVOS_INICIO, texto);
		texto = this.montaPartesLaco(id_processo, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, PROCESSO_POLOSPASSIVOS_INICIO, texto);
		texto = this.montaPartesLaco(id_processo, ProcessoParteTipoDt.VITIMA, PROCESSO_VITIMAS_INICIO, texto);

		return texto;
	}

	/**
	 * Monta as partes do processo utilizando dados da memoria
	 * @author Ronneesley Moura Teles
	 * @since 27/05/2008
	 * @param ProcessoDt processo
	 * @param String texto
	 * @return String
	 * @throws Exception
	 */
	private String montaPartesProcessoMemoria(ProcessoDt processo, String texto) throws Exception{
		//Monta partes do tipo promoventes e promovidos
		
		if (processo.getRecursoDt() != null && processo.getRecursoDt().getId() != null &&  processo.getRecursoDt().getId().length()>0){
			texto = this.montaPartesSegundoGrauLacoMemoria(PROCESSO_POLOSATIVOS_INICIO, processo.getRecursoDt().getListaRecorrentesAtivos(), texto);
			texto = this.montaPartesSegundoGrauLacoMemoria(PROCESSO_POLOSPASSIVOS_INICIO, processo.getRecursoDt().getListaRecorridosAtivos(), texto);
		} else {
			texto = this.montaPartesLacoMemoria(PROCESSO_POLOSATIVOS_INICIO, processo.getListaPolosAtivos(), texto);
			texto = this.montaPartesLacoMemoria(PROCESSO_POLOSPASSIVOS_INICIO, processo.getListaPolosPassivos(), texto);
			texto = this.montaPartesLacoMemoria(PROCESSO_VITIMAS_INICIO, processo.getListaVitimas(), texto);
		}
		
		return texto;
	}

	/**
	 * Monta partes a partir de uma lista de partes
	 * @author Ronneesley Moura Teles
	 * @since 27/05/2008 14:41
	 * @param StringBuilder construtor, construtor
	 * @param Matcher matcher
	 * @param int grupo, posicao no matcher
	 * @param List partes, lista de partes
	 * @throws Exception
	 */
	private void montaPartes(StringBuilder construtor, Matcher matcher, int grupo, List partes) throws Exception{
		if (partes != null) {
			Iterator itPartes = partes.iterator();

			while (itPartes.hasNext()) {
				ProcessoParteDt parte = (ProcessoParteDt) itPartes.next();

				construtor.append(this.montaConteudoParte(parte, matcher.group(grupo), "processo"));
			}
		}
	}
	
	/**
	 * Monta partes a partir de uma lista de partes
	 * @param StringBuilder construtor, construtor
	 * @param Matcher matcher
	 * @param int grupo, posicao no matcher
	 * @param List partes, lista de partes
	 * @throws Exception
	 */
	private void montaPartesSegundoGrau(StringBuilder construtor, Matcher matcher, int grupo, List partes) throws Exception{
		if (partes != null) {
			Iterator itPartes = partes.iterator();

			while (itPartes.hasNext()) {
				RecursoParteDt recursoParte = (RecursoParteDt) itPartes.next();

				construtor.append(this.montaConteudoParte(recursoParte.getProcessoParteDt(), matcher.group(grupo), "processo"));
			}
		}
	}

	/**
	 * Monta partes do processo utilizando dados da memoria
	 * @author Ronneesley Moura Teles
	 * @since 27/05/2008 15:32
	 * @param String tipo, tipo da parte
	 * @param List partes, partes
	 * @param String texto, texto
	 * @return String
	 * @throws Exception
	 */
	private String montaPartesLacoMemoria(String tipo, List partes, String texto) throws Exception{
		texto = texto.replaceAll("[\\n\\r]+", "");
		{
			String constanteFim = "";
			if (tipo.equals(PROCESSO_POLOSATIVOS_INICIO)) {
				constanteFim = PROCESSO_POLOSATIVOS_FIM;
			} else if (tipo.equals(PROCESSO_POLOSPASSIVOS_INICIO)) {
				constanteFim = PROCESSO_POLOSPASSIVOS_FIM;
			} else if (tipo.equals(PROCESSO_VITIMAS_INICIO)) {
				constanteFim = PROCESSO_VITIMAS_FIM;
			}
			String patternStr = "[$][{][\\s]*" + tipo + "[\\s]*[}](.*?)[$][{][\\s]*" + constanteFim + "[\\s]*[}]";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);

			while (matcher.find()) {
				StringBuilder builder = new StringBuilder();

				//Monta dados das partes
				this.montaPartes(builder, matcher, 1, partes);

				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}

		return texto;
	}
	
	/**
	 * Monta partes do processo utilizando dados da memoria
	 * @param String tipo, tipo da parte
	 * @param List partes, partes
	 * @param String texto, texto
	 * @return String
	 * @throws Exception
	 */
	private String montaPartesSegundoGrauLacoMemoria(String tipo, List partes, String texto) throws Exception{
		texto = texto.replaceAll("[\\n\\r]+", "");
		{
			String constanteFim = "";
			if (tipo.equals(PROCESSO_POLOSATIVOS_INICIO)) {
				constanteFim = PROCESSO_POLOSATIVOS_FIM;
			} else if (tipo.equals(PROCESSO_POLOSPASSIVOS_INICIO)) {
				constanteFim = PROCESSO_POLOSPASSIVOS_FIM;
			}
			String patternStr = "[$][{][\\s]*" + tipo + "[\\s]*[}](.*?)[$][{][\\s]*" + constanteFim + "[\\s]*[}]";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);

			while (matcher.find()) {
				StringBuilder builder = new StringBuilder();

				//Monta dados das partes
				this.montaPartesSegundoGrau(builder, matcher, 1, partes);

				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}

		return texto;
	}

	/**
	 * Monta partes dentro do laco especificado
	 * @author Ronneesley Moura Teles
	 * @since 14/04/2008
	 * @param String id_processo, id do processo pesquisado
	 * @param String tipo, tipo da parte (normalmente utilizado no plural) ex: promoventes
	 * @param String texto, texto a ser modificado
	 * @return String
	 * @throws Exception 
	 */
	private String montaPartesLaco(String id_processo, int processoParteTipoCodigo, String tipo, String texto) throws Exception{
		ProcessoParteNe processoParteNe = new ProcessoParteNe();

		texto = texto.replaceAll("[\\n\\r]+", "");

		String constanteFim = "";
		if (tipo.equals(PROCESSO_POLOSATIVOS_INICIO)) {
			constanteFim = PROCESSO_POLOSATIVOS_FIM;
		} else if (tipo.equals(PROCESSO_POLOSPASSIVOS_INICIO)) {
			constanteFim = PROCESSO_POLOSPASSIVOS_FIM;
		} else if (tipo.equals(PROCESSO_VITIMAS_INICIO)) {
			constanteFim = PROCESSO_VITIMAS_FIM;
		}
		String patternStr = "[$][{][\\s]*" + tipo + "[\\s]*[}](.*?)[$][{][\\s]*" + constanteFim + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List partes = processoParteNe.consultarPartes(id_processo, String.valueOf(processoParteTipoCodigo));
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			this.montaPartes(builder, matcher, 1, partes);

			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}

		return texto;
	}

	/**
	 * Monta Usuario
	 * @author Ronneesley Moura Teles
	 * @since 28/03/2008
	 * @return String
	 */
	protected String montaUsuario(UsuarioDt servidor, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*" + USUARIO_NOME + "[\\s]*[}]", format(servidor.getNome()));
		String nomeChefe = servidor.getId_ServentiaCargoUsuarioChefe().isEmpty() ? servidor.getNome() : servidor.getUsuarioServentiaChefe();
		texto = texto.replaceAll("[$][{][\\s]*" + USUARIO_NOME_CHEFE + "[\\s]*[}]", format(nomeChefe));
		texto = texto.replaceAll("[$][{][\\s]*" + USUARIO_CARGO + "[\\s]*[}]", format(GrupoDt.getAtividadeUsuario(servidor.getGrupoCodigo())));
		texto = texto.replaceAll("[$][{][\\s]*" + USUARIO_MATRICULA + "[\\s]*[}]", format(servidor.getMatriculaTjGo()));

		return texto;
	}

	
	/**
	 * Monta outras variaveis
	 * @author Alex Rocha
	 * @param texto
	 * @return String
	 */
	protected String montaOutros(String texto) {
		ProjudiPropriedades projudiPropriedade = ProjudiPropriedades.getInstance();
		
		return montaOutros(texto, projudiPropriedade);
	}
	
	/**
	 * 
	 * @param texto
	 * @param projudiPropriedade
	 * @return
	 */
	protected String montaOutros(String texto, ProjudiPropriedades projudiPropriedade) {
		texto = texto.replaceAll("[$][{][\\s]*" + DATA + "[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(new Date())).toLowerCase());
		texto = texto.replaceAll("[$][{][\\s]*" + HORA + "[\\s]*[}]", Funcoes.Hora(new Date()));
		texto = texto.replaceAll("[$][{][\\s]*" + BRASAO + "[\\s]*[}]", "<img src=\""+ConverterHtmlPdf.BRASAO_BASE64_REDUZIDO+"\"></img>");
		texto = texto.replaceAll("[$][{][\\s]*" + BRASAO_GOIAS + "[\\s]*[}]", "<img src=\""+ConverterHtmlPdf.BRASAO_BASE64_REDUZIDO+"\"></img>");
							
		return texto;
	}

	/**
	 * Método responsável por montar o conteúdo de uma citação
	 * @param citacao objeto citação com os dados a serem usados no modelo
	 * @param servidor objeto servidor com os dados do servidor que
	 * está redigindo o documento
	 * @param texto, conteúdo original do modelo,ao qual será aplicado as devidas
	 * modificações (replace) para preenchê-lo de acordo com os dados desejados
	 * @throws Exception 
	 */
	/*public String montaConteudo(Citacao citacao, UsuarioDt servidor, String texto) throws Exception{
		//Vara vara = citacao.getVara();
		ServentiaNe serventiaNe = new ServentiaNe();
		ServentiaDt serventiaDt = serventiaNe.consultarServentiaCodigo(String.valueOf(citacao.getCodVaraSeRealizou()));
		Processo processo = citacao.getPartesProcesso().getProcesso();
		Date dataConciliacao = (processo.getAudienciaConciliacaos().size() > 0) ? ((AudienciaConciliacao) processo.getAudienciaConciliacaos().get(processo.getAudienciaConciliacaos().size()-1)).getAgendaConciliacao().getHorarioAudiencia() : null;
		Date dataInstrucao = null;
		String valor = NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(citacao.getPartesProcesso().getProcesso().getValoracao());
		Parte promovente = ((PartesProcesso) processo.getPartesProcessoPromoventes().get(0)).getParte();
		Parte promovido = ((PartesProcesso) citacao.getPartesProcesso()).getParte();
		Parte parteCitacao = citacao.getPartesProcesso().getParte();

		texto = texto.replaceAll("[\\n\\r]+", "");
		{
			String patternStr = "[$][{][\\s]*processo.promoventes.inicio[\\s]*[}](.*?)[$][{][\\s]*processo.promoventes.fim[\\s]*[}]";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promoventes = processo.getPartesProcessoPromoventes();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promoventes) {
					builder.append(montaConteudoPromovente(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}
		{
			String patternStr = "[$][{][\\s]*processo.promovidos.inicio[\\s]*[}](.*?)[$][{][\\s]*processo.promovidos.fim[\\s]*[}]";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promovidos = processo.getPartesProcessoPromovidas();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promovidos) {
					builder.append(montaConteudoPromovido(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}

		texto = texto.replaceAll("[$][{][\\s]*serventia.nome[\\s]*[}]", format(serventiaDt.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.comarca[\\s]*[}]", format(serventiaDt.getComarca()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.logradouro[\\s]*[}]", format(serventiaDt.getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.numero[\\s]*[}]", format(serventiaDt.getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.complemento[\\s]*[}]", format(serventiaDt.getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.bairro[\\s]*[}]", format(serventiaDt.getBairro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.telefone[\\s]*[}]", format(serventiaDt.getTelefone()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cidade[\\s]*[}]", format(serventiaDt.getCidade()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.sigla[\\s]*[}]", format(serventiaDt.getEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.completo[\\s]*[}]", format(ProjudiConfiguration.getInstance().getNomeEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cep[\\s]*[}]", format(serventiaDt.getCep()));

		texto = texto.replaceAll("[$][{][\\s]*processo.numero[\\s]*[}]", format(processo.getNumeroProcessoFormatado()));
		if (processo.getProcessosDependentes().size() > 0) {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(processo.getNumeroProcessoDependenteFormatado()));
		} else {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(""));
		}
		texto = montaConteudoPromovente(promovente, texto);
		texto = montaConteudoPromovido(promovido, texto);
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.tipo[\\s]*[}]", format(processo.getTipoAcao().getDescricao()));
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.valor[\\s]*[}]", format(valor));

		if (dataConciliacao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataConciliacao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataConciliacao)));
		}
		if (dataInstrucao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataInstrucao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataInstrucao)));
		}
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.nome[\\s]*[}]", format(parteCitacao.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.rg[\\s]*[}]", format(parteCitacao.getRGCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.cpfOuCnpj[\\s]*[}]", format(parteCitacao.getCpfcnpj()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.logradouro[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.numero[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.complemento[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.bairro[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getBairro()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.telefone[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getTelefone()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.cidade[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getCidade().getDescricao()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.estado[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getEstado()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.cep[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getCep()));
		if (citacao.getPrazo() != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.prazo.dias[\\s]*[}]", format(citacao.getPrazo().getNumeroDias()));
		}

		texto = texto.replaceAll("[$][{][\\s]*data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(new Date())));
		texto = texto.replaceAll("[$][{][\\s]*usuario.nome[\\s]*[}]", format(servidor.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*usuario.cargo[\\s]*[}]", format(servidor.getGrupo()));
		texto = texto.replaceAll("[$][{][\\s]*brasao[\\s]*[}]", format("<img src=\"http://www.tj.go.gov.br/projudi/imagens/brasaoPetroBranco.jpg\" />"));

		return texto.replace("\'" , "\\'");
	}*/

	/**
	 * Monta o conteudo de uma parte
	 * @author Ronenesley Moura Teles
	 * @since 22/04/2008 - 13:34
	 * @param parte
	 * @param texto
	 * @param tipo
	 * @return String
	 * @throws Exception
	 */
	private String montaConteudoParte(ProcessoParteDt parte, String texto, String tipo) throws Exception{
		return this.montaConteudoParte(parte, texto, tipo, null);
	}
	
	
	private String verificaPoloAtivoPoloPassivo(String processoParteTipo){
		String retorno = "";
		int inParte = Funcoes.StringToInt(processoParteTipo,-1);
		if ( inParte == ProcessoParteTipoDt.POLO_ATIVO_CODIGO ){
			retorno = "poloativo";
		}else if ( inParte == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO ){ 				
			retorno = "polopassivo";					
		}else if ( inParte == ProcessoParteTipoDt.VITIMA ){ 				
			retorno = "vitima";					
		}
		
		return retorno;
	}

	/**
	 * Monta conteudo da parte
	 * @author Ronenesley Moura Teles
	 * @since antes 22/04/2008 - 13:34
	 * @param ProcessoParteDt parte, parte do processo
	 * @param String texto, texto a substituir
	 * @param String tipo, tipo da parte
	 * @return
	 * @throws Exception
	 */
	private String montaConteudoParte(ProcessoParteDt parte, String texto, String tipo, String tParte) throws Exception{
		ProcessoParteNe processoParteNe = new ProcessoParteNe();

		if (!parte.getId().equals("")) parte = processoParteNe.consultarId(parte.getId());
		EnderecoDt endereco = parte.getEnderecoParte();

		String tipoParte;
		if (tParte == null) 
			tipoParte = verificaPoloAtivoPoloPassivo(parte.getProcessoParteTipoCodigo());//parte.getProcessoParteTipo().toLowerCase();
		else 
			tipoParte = tParte;

		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".nome[\\s]*[}]", format(parte.getNome()));

		if (parte.getId() != null && !parte.getId().trim().equals("")) texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".rg[\\s]*[}]", format(processoParteNe.consultarRgCompleto(parte.getId())));

		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".cpfOuCnpj[\\s]*[}]", format(formataDado(parte.getCpfCnpjFormatado())));

		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.logradouro[\\s]*[}]", format(endereco.getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.numero[\\s]*[}]", format(endereco.getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.complemento[\\s]*[}]", format(endereco.getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.bairro[\\s]*[}]", format(endereco.getBairro()));
		//Para manter a compatibilidade dos documentos ja cadastrados
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.telefone[\\s]*[}]", format(formataDado(parte.getTelefone())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.cidade[\\s]*[}]", format(formataDado(endereco.getCidade())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.cep[\\s]*[}]", format(formataDado(endereco.getCep())));

		//texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte +  ".endereco.estado[\\s]*[}]", format( estado.getEstado() ));		
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".endereco.estado[\\s]*[}]", format(formataDado(endereco.getUf())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".profissao[\\s]*[}]", format(formataDado(parte.getProfissao())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".estadoCivil[\\s]*[}]", format(formataDado(parte.getEstadoCivil())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".dataNascimento[\\s]*[}]", format(formataDado(parte.getDataNascimento())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".nomePai[\\s]*[}]", format(formataDado(parte.getNomePai())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".nomeMae[\\s]*[}]", format(formataDado(parte.getNomeMae())));
		
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".sexo[\\s]*[}]", format(parte.getSexo().equalsIgnoreCase("M")?"Masculino":"Feminino"));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".naturalidade[\\s]*[}]", format(formataDado(parte.getCidadeNaturalidade())));
		
		texto = montaAdvogados(texto, parte.getId(), tipo, tipoParte);
		
		return texto;
	}


	private String montaAdvogados(String texto, String idProcessoParte, String tipo, String tipoParte) throws Exception{
		texto = texto.replaceAll("[\\n\\r]+", "");

		String patternStr = "[$][{][\\s]*" + ADVOGADOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + ADVOGADOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			List listaAdvogados = new ProcessoParteAdvogadoNe().consultarAdvogadosParte(idProcessoParte);
			StringBuilder builder = new StringBuilder();

			//------------Monta dados dos advogados------------
			Iterator itAdvogados = listaAdvogados.iterator();

			while (itAdvogados.hasNext()) {
				ProcessoParteAdvogadoDt advogado = (ProcessoParteAdvogadoDt) itAdvogados.next();
				builder.append(this.montaConteudoAdvogado(advogado, matcher.group(1), tipo, tipoParte));
			}
			//------------------------------------------------
			
			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}
		return texto;
	}
	
	private String montaConteudoAdvogado(ProcessoParteAdvogadoDt advogado, String texto, String tipo, String tipoParte){
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".advogado.nome[\\s]*[}]", format(formataDado(advogado.getNomeAdvogado())));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".advogado.oab.numero[\\s]*[}]", format(advogado.getOabNumero()));
		texto = texto.replaceAll("[$][{][\\s]*" + tipo + "." + tipoParte + ".advogado.oab.complemento[\\s]*[}]", format(advogado.getOabComplemento()));
		return texto;
	}

	
	private String formataDado(String dado){
		if (dado.length() > 0) return dado;
		else return "--";
	}
	/**
	 * Método responsável por montar o conteúdo de uma intimação
	 * @param intimacao objeto Intimacao com os dados a serem usados no modelo
	 * @param texto, conteúdo original do modelo, ao qual serão aplicadas as devidas
	 * modificações (replace) para preenchê-lo de acordo com os dados desejados
	 * @param servidor objetoServidorJudiciario com os dados do usuário que está redigindo o documento
	 * @throws Exception 
	 */
	/*public String montaConteudo(Intimacao intimacao, String texto, UsuarioDt servidor){
		//Vara vara = intimacao.getVara();
		ServentiaNe serventiaNe = new ServentiaNe();
		ServentiaDt serventiaDt = serventiaNe.consultarServentiaCodigo(String.valueOf(intimacao.getCodVaraSeRealizou()));
		Processo processo = intimacao.getPartesProcesso().getProcesso();
		Date dataConciliacao = (processo.getAudienciaConciliacaos().size() > 0) ? ((AudienciaConciliacao) processo.getAudienciaConciliacaos().get(processo.getAudienciaConciliacaos().size() - 1)).getAgendaConciliacao().getHorarioAudiencia() : null;
		Date dataInstrucao = null;
		String valor = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(intimacao.getPartesProcesso().getProcesso().getValoracao());
		Parte promovente = ((PartesProcesso) processo.getPartesProcessoPromoventes().get(0)).getParte();
		Parte promovido = ((PartesProcesso) processo.getPartesProcessoPromovidas().get(0)).getParte();
		Parte parteCitacao = intimacao.getPartesProcesso().getParte();

		texto = texto.replaceAll("[\\n\\r]+", "");
		{
			String patternStr = "\\$\\{[\\s]*processo.promoventes.inicio[\\s]*\\}(.*?)\\$\\{[\\s]*processo.promoventes.fim[\\s]*\\}";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promoventes = processo.getPartesProcessoPromoventes();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promoventes) {
					builder.append(montaConteudoPromovente(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}
		{
			String patternStr = "\\$\\{[\\s]*processo.promovidos.inicio[\\s]*\\}(.*?)\\$\\{[\\s]*processo.promovidos.fim[\\s]*\\}";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promovidos = processo.getPartesProcessoPromovidas();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promovidos) {
					builder.append(montaConteudoPromovido(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}

		texto = texto.replaceAll("[$][{][\\s]*serventia.nome[\\s]*[}]", format(serventiaDt.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.comarca[\\s]*[}]", format(serventiaDt.getComarca()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.logradouro[\\s]*[}]", format(serventiaDt.getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.numero[\\s]*[}]", format(serventiaDt.getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.complemento[\\s]*[}]", format(serventiaDt.getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.bairro[\\s]*[}]", format(serventiaDt.getBairro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.telefone[\\s]*[}]", format(serventiaDt.getTelefone()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cidade[\\s]*[}]", format(serventiaDt.getCidade()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.sigla[\\s]*[}]", format(serventiaDt.getEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.completo[\\s]*[}]", format(ProjudiConfiguration.getInstance().getNomeEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cep[\\s]*[}]", format(serventiaDt.getCep()));

		texto = texto.replaceAll("[$][{][\\s]*processo.numero[\\s]*[}]", format(processo.getNumeroProcessoFormatado()));
		if (processo.getProcessosDependentes().size() > 0) {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(processo.getNumeroProcessoDependenteFormatado()));
		} else {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(""));
		}
		texto = montaConteudoPromovente(promovente, texto);
		texto = montaConteudoPromovido(promovido, texto);
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.tipo[\\s]*[}]", format(processo.getTipoAcao().getDescricao()));
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.valor[\\s]*[}]", format(valor));

		if (dataConciliacao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataConciliacao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataConciliacao)));
		}
		if (dataInstrucao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataInstrucao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataInstrucao)));
		}
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.nome[\\s]*[}]", format(parteCitacao.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.rg[\\s]*[}]", format(parteCitacao.getRGCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.cpfOuCnpj[\\s]*[}]", format(parteCitacao.getCpfcnpj()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.logradouro[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.numero[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.complemento[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.bairro[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getBairro()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.telefone[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getTelefone()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.cidade[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getCidade().getDescricao()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.estado[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getEstado()));
		texto = texto.replaceAll("[$][{][\\s]*cumprimento.parte.endereco.cep[\\s]*[}]", format(parteCitacao.getEnderecoantigo().getCep()));

		if (intimacao.getPrazo() != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.prazo.dias[\\s]*[}]", format(intimacao.getPrazo().getNumeroDias()));
		}

		texto = texto.replaceAll("[$][{][\\s]*data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(new Date())));
		texto = texto.replaceAll("[$][{][\\s]*usuario.nome[\\s]*[}]", format(servidor.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*usuario.cargo[\\s]*[}]", format(servidor.getGrupo()));
		texto = texto.replaceAll("[$][{][\\s]*brasao[\\s]*[}]", format("<img src=\"http://www.tj.go.gov.br/projudi/imagens/brasaoPetroBranco.jpg\" />"));

		return texto.replace("'", "\\'");
	}*/

	/**
	 * Método responsável por montar o conteúdo de um cumprimento
	 * @param texto, conteúdo original do modelo, ao qual serão aplicadas as devidas
	 * modificações (replace) para preenchê-lo de acordo com os dados desejados
	 * @param cumprimento objeto CumprimentoCartorio com os dados a serem usados no modelo
	 * @param servidor objetoServidorJudiciario com os dados do usuário que está redigindo o documento
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	/*public String montaConteudo(String texto, CumprimentoCartorio cumprimento, UsuarioDt servidor){
		//Vara vara = cumprimento.getMovimentacao().getVara();
		ServentiaNe serventiaNe = new ServentiaNe();
		ServentiaDt serventiaDt = serventiaNe.consultarServentiaCodigo(String.valueOf(cumprimento.getCodVaraSeRealizou()));
		Processo processo = cumprimento.getMovimentacao().getProcesso();
		String valor = NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(processo.getValoracao());
		Parte promovente = ((PartesProcesso) processo.getPartesProcessoPromoventes().get(0)).getParte();
		Parte promovido  = ((PartesProcesso) processo.getPartesProcessoPromovidas().get(0)).getParte();

		Date dataConciliacao = (processo.getAudienciaConciliacaos().size() > 0) ? ((AudienciaConciliacao) processo.getAudienciaConciliacaos().get(processo.getAudienciaConciliacaos().size()-1)).getAgendaConciliacao().getHorarioAudiencia() : null;
		Date dataInstrucao = (processo.getAudienciaInstrucaos().size() > 0) ? ((AudienciaInstrucao) processo.getAudienciaInstrucaos().get(processo.getAudienciaInstrucaos().size()-1)).getAgendaInstrucao().getHorarioAudiencia() : null;

		texto = texto.replaceAll("[\\n\\r]+", format(""));
		{
			String patternStr = "\\$\\{[\\s]*processo.promoventes.inicio[\\s]*\\}(.*?)\\$\\{[\\s]*processo.promoventes.fim[\\s]*\\}";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promoventes = processo.getPartesProcessoPromoventes();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promoventes) {
					builder.append(montaConteudoPromovente(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}
		{
			String patternStr = "\\$\\{[\\s]*processo.promovidos.inicio[\\s]*\\}(.*?)\\$\\{[\\s]*processo.promovidos.fim[\\s]*\\}";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promovidos = processo.getPartesProcessoPromovidas();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promovidos) {
					builder.append(montaConteudoPromovido(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}

		texto = texto.replaceAll("[$][{][\\s]*serventia.nome[\\s]*[}]", format(serventiaDt.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.comarca[\\s]*[}]", format(serventiaDt.getComarca()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.logradouro[\\s]*[}]", format(serventiaDt.getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.numero[\\s]*[}]", format(serventiaDt.getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.complemento[\\s]*[}]", format(serventiaDt.getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.bairro[\\s]*[}]", format(serventiaDt.getBairro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.telefone[\\s]*[}]", format(serventiaDt.getTelefone()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cidade[\\s]*[}]", format(serventiaDt.getCidade()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.sigla[\\s]*[}]", format(serventiaDt.getEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.completo[\\s]*[}]", format(ProjudiConfiguration.getInstance().getNomeEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cep[\\s]*[}]", format(serventiaDt.getCep()));
		texto = texto.replaceAll("[$][{][\\s]*processo.numero[\\s]*[}]", format(processo.getNumeroProcessoFormatado()));
		if (processo.getProcessosDependentes().size() > 0) {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(processo.getNumeroProcessoDependenteFormatado()));
		} else {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(""));
		}
		texto = montaConteudoPromovente(promovente, texto);
		texto = montaConteudoPromovido(promovido, texto);
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.tipo[\\s]*[}]", format(processo.getTipoAcao().getDescricao()));
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.valor[\\s]*[}]", format(valor));

		if (dataConciliacao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataConciliacao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataConciliacao)));
		}
		if (dataInstrucao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataInstrucao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataInstrucao)));
		}

		if (cumprimento.getPrazo() != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.prazo.dias[\\s]*[}]", format(cumprimento.getPrazo().getNumeroDias()));
		}

		texto = texto.replaceAll("[$][{][\\s]*data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(new Date())));
		texto = texto.replaceAll("[$][{][\\s]*usuario.nome[\\s]*[}]", format(servidor.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*usuario.cargo[\\s]*[}]", format(servidor.getGrupo()));
		texto = texto.replaceAll("[$][{][\\s]*brasao[\\s]*[}]", format("<img src=\"http://www.tj.go.gov.br/projudi/imagens/brasaoPetroBranco.jpg\" />"));

		return texto.replace("'", "\\'");
	}*/
	/**
	 * Método responsável por montar o conteúdo de uma conclusão
	 * @param conclusao objeto conclusão com os dados a serem usados no modelo
	 * @param nomeUsuario nome do usuário que está redigindo o documento
	 * @param atividadeUsuario atividade do usuário que está redigindo o documento
	 * @param texto, conteúdo original do modelo,ao qual será aplicado as devidas
	 * modificações (replace) para preenchê-lo de acordo com os dados desejados
	 * @throws Exception 
	 */
	/*public String montaConteudo(Conclusao conclusao, String nomeUsuario, String atividadeUsuario, String texto) throws Exception{
		MovimentacaoPeer movimentacaoPeer = new MovimentacaoPeer();
		//Vara vara = conclusao.getVara();
		ServentiaNe serventiaNe = new ServentiaNe();
		ServentiaDt serventiaDt = serventiaNe.consultarServentiaCodigo(String.valueOf(conclusao.getCodVaraSeRealizou()));
		Movimentacao movimentacao = movimentacaoPeer.getMovimentacao(conclusao.getCoddocumento());
		Processo processo = movimentacao.getProcesso();
		Date dataConciliacao = (processo.getAudienciaConciliacaos().size() > 0) ? ((AudienciaConciliacao) processo.getAudienciaConciliacaos().get(processo.getAudienciaConciliacaos().size()-1)).getAgendaConciliacao().getHorarioAudiencia() : null;
		Date dataInstrucao = null;
		String valor = NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(processo.getValoracao());
		Parte promovente = ((PartesProcesso) processo.getPartesProcessoPromoventes().get(0)).getParte();
		Parte promovido = new Parte();
		if (processo.getPartesProcessoPromovidas().size() > 0){
			promovido = ((PartesProcesso) processo.getPartesProcessoPromovidas().get(0)).getParte();
		}
		texto = texto.replaceAll("[\\n\\r]+", "");
		{
			String patternStr = "[$][{][\\s]*processo.promoventes.inicio[\\s]*[}](.*?)[$][{][\\s]*processo.promoventes.fim[\\s]*[}]";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promoventes = processo.getPartesProcessoPromoventes();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promoventes) {
					builder.append(montaConteudoPromovente(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}
		{
			String patternStr = "[$][{][\\s]*processo.promovidos.inicio[\\s]*[}](.*?)[$][{][\\s]*processo.promovidos.fim[\\s]*[}]";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(texto);
			while (matcher.find()) {
				List promovidos = processo.getPartesProcessoPromovidas();
				StringBuilder builder = new StringBuilder();
				for (PartesProcesso parte : promovidos) {
					builder.append(montaConteudoPromovido(parte.getParte(), matcher.group(1)));
				}
				texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
			}
		}

		texto = texto.replaceAll("[$][{][\\s]*serventia.nome[\\s]*[}]", format(serventiaDt.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.comarca[\\s]*[}]", format(serventiaDt.getComarca()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.logradouro[\\s]*[}]", format(serventiaDt.getLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.numero[\\s]*[}]", format(serventiaDt.getNumero()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.complemento[\\s]*[}]", format(serventiaDt.getComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.bairro[\\s]*[}]", format(serventiaDt.getBairro()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.telefone[\\s]*[}]", format(serventiaDt.getTelefone()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cidade[\\s]*[}]", format(serventiaDt.getCidade()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.sigla[\\s]*[}]", format(serventiaDt.getEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.estado.completo[\\s]*[}]", format(ProjudiConfiguration.getInstance().getNomeEstado()));
		texto = texto.replaceAll("[$][{][\\s]*serventia.endereco.cep[\\s]*[}]", format(serventiaDt.getCep()));

		texto = texto.replaceAll("[$][{][\\s]*processo.numero[\\s]*[}]", format(processo.getNumeroProcessoFormatado()));
		if (processo.getProcessosDependentes().size() > 0) {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(processo.getNumeroProcessoDependenteFormatado()));
		} else {
			texto = texto.replaceAll("[$][{][\\s]*processo.dependente.numero[\\s]*[}]", format(""));
		}
		texto = montaConteudoPromovente(promovente, texto);
		texto = montaConteudoPromovido(promovido, texto);
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.tipo[\\s]*[}]", format(processo.getTipoAcao().getDescricao()));
		texto = texto.replaceAll("[$][{][\\s]*processo.acao.valor[\\s]*[}]", format(valor));

		if (dataConciliacao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataConciliacao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.conciliacao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataConciliacao)));
		}
		if (dataInstrucao != null) {
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(dataInstrucao)));
			texto = texto.replaceAll("[$][{][\\s]*cumprimento.audiencia.instrucao.hora[\\s]*[}]", format(DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(dataInstrucao)));
		}

		texto = texto.replaceAll("[$][{][\\s]*data[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(new Date())));
		texto = texto.replaceAll("[$][{][\\s]*usuario.nome[\\s]*[}]", format(nomeUsuario));
		texto = texto.replaceAll("[$][{][\\s]*usuario.cargo[\\s]*[}]", format(atividadeUsuario));
		texto = texto.replaceAll("[$][{][\\s]*brasao[\\s]*[}]", format("<img src=\"http://www.tj.go.gov.br/projudi/imagens/brasaoPetroBranco.jpg\" />"));

		return texto.replace("\'" , "\\'");
	}*/
	private String format(Integer i) {
		if (i == null) return "";
		return Matcher.quoteReplacement(i.toString());
	}

	private String format(String s) {
		if (s == null) return "";
		//O escapeHtml foi usado para evitar que caracteres especiais (ex: $,&,etc) interfiram nas tags do HTML
		return Matcher.quoteReplacement(org.apache.commons.lang.StringEscapeUtils.escapeHtml(s.trim()));
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarGrupoArquivoTipo(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		tempList = neObjeto.consultarGrupoArquivoTipo(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	/** Metodo que preenche o modelo de Execucação Art. 615-a C.P.C.
	 * @param certidao
	 * @param servidor
	 * @param id
	 * @return texto com as variáveis substituidas
	 * @throws Exception 
	 */
	public String montaConteudoPorModelo(CertidaoExecucaoCPCDt certidao, UsuarioDt servidor, String id) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
	    texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaProcessoExecucao(certidao, texto);
		
		return texto;
	}

	/** Substitui as variáveis do processo de execução
	 * @param certidao
	 * @return texto com as variáveis substituidas
	 */
	private String montaProcessoExecucao(CertidaoExecucaoCPCDt certidao, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_NUMERO+"[\\s]*[}]", format(certidao.getProcessoNumeroCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_SERVENTIA+"[\\s]*[}]", format(certidao.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_TIPO +"[\\s]*[}]", format(certidao.getNatureza()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVENTE +"[\\s]*[}]", format(certidao.getPromovente()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_PROMOVIDO +"[\\s]*[}]", format(certidao.getPromovido()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_DATA_DISTRIBUICAO+"[\\s]*[}]", format(Funcoes.FormatarDataHoraMinuto(certidao.getDataDistribuicao())));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_ADVOGADO_PROMOVENTE +"[\\s]*[}]", format(certidao.getAdvogadoPromovente()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PROCESSO_VALOR_DA_ACAO+"[\\s]*[}]", format(certidao.getValor()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ +"[\\s]*[}]", format(certidao.getCpfCnpj()));

		return texto;
	}

	private String montaConteudoMandadoPrisao(MandadoPrisaoDt dados, String texto, String titulo) {
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_NUMERO +"[\\s]*[}]", format(dados.getMandadoPrisaoNumero()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_NUMERO_PROCESSO +"[\\s]*[}]", format(dados.getProcessoNumeroCompleto()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_CLASSE_CNJ +"[\\s]*[}]", format(dados.getProcessoTipo()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_ASSUNTO_CNJ +"[\\s]*[}]", format(dados.getAssunto()));
		
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_NOME +"[\\s]*[}]", format(dados.getProcessoParte()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_CPF +"[\\s]*[}]", format(dados.getCpf().length()==0?"-":dados.getCpf()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_ENDERECO +"[\\s]*[}]", format(dados.getEnderecoCompleto().length()==0?"-":dados.getEnderecoCompleto()));
		String sexo = "-";
		if (dados.getSexo().length() > 0){
			if (dados.getSexo().equalsIgnoreCase("M")) sexo = "Masculino";
			else sexo = "Feminino";	
		}
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_SEXO +"[\\s]*[}]", format(sexo));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_NOME_MAE +"[\\s]*[}]", format(dados.getNomeMae().length()==0?"-":dados.getNomeMae()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_NOME_PAI +"[\\s]*[}]", format(dados.getNomePai().length()==0?"-":dados.getNomePai()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_DATA_NASCIMENTO +"[\\s]*[}]", format(dados.getDataNascimento().length()==0?"-":dados.getDataNascimento()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PROMOVIDO_NATURALIDADE +"[\\s]*[}]", format(dados.getNaturalidade() + "-" + dados.getUfNaturalidade()));
		
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_PENA_IMPOSTA +"[\\s]*[}]", format(dados.getTempoPenaTotalAnos()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_REGIME +"[\\s]*[}]", format(dados.getRegimeExecucao()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_LOCAL_RECOLHIMENTO +"[\\s]*[}]", format(dados.getLocalRecolhimento().length()==0?"-":dados.getLocalRecolhimento()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_DATA_PRESCRICAO +"[\\s]*[}]", format(dados.getDataValidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_DECISAO +"[\\s]*[}]", format(dados.getSinteseDecisao()));
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_PRISAO_TITULO +"[\\s]*[}]", format(titulo));
		
		return texto;
	}
	
	public String montaModeloMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, UsuarioDt usuarioDt, String modeloCodigo, String titulo) throws Exception{
		String texto = "";
		
		texto = consultarModeloCodigo(modeloCodigo).getTexto();
		texto = montaServentia(usuarioDt.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(usuarioDt, texto);
		texto = montaConteudoMandadoPrisao(mandadoPrisaoDt, texto, titulo);
		
		return texto;
	}
	public String consultarModelosJSON(String descricao, String posicao, String id_ArquivoTipo, UsuarioDt usuarioDt) throws Exception {
		return consultarModelosJSON(descricao, posicao, id_ArquivoTipo, usuarioDt, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public String consultarModelosJSON(String descricao, String posicao, String id_ArquivoTipo, UsuarioDt usuarioDt, String ordenacao, String quantidadeRegistros) throws Exception {
		String stRetorno = null;
				
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		
		// HELLENO - Central de Mandados
		// TODO: O if abaixo está incluindo mandados offline. Desenvolver uma forma de entrar apenas se o mandado for
		// ser expedido para a Central de Mandados pois, nestes casos, deve obrigar o uso de um modelo de mandado
		// Genérico que contenha a quantidade de locomoções embutida.
		//
//		if(Funcoes.StringToInt(id_ArquivoTipo) == ArquivoTipoDt.MANDADO) {
//			// ATENÇÃO: quando tratar-se de Mandado, independente do grupo, só consulta modelos genéricos
//			stRetorno = this.consultarModelosGenericosJSON(descricao,  posicao, id_ArquivoTipo);
//		}
//		else {
			switch (grupoTipo) {
			//Administrador visualiza modelos genéricos
			case GrupoTipoDt.ADMINISTRADOR:
				stRetorno = this.consultarModelosGenericosJSON(descricao,  posicao, id_ArquivoTipo);
				break;

			//Usuários externos visualizam apenas seus próprios modelos
			case GrupoTipoDt.ADVOGADO:
			case GrupoTipoDt.AUTORIDADE_POLICIAL:
			case GrupoTipoDt.CONTADOR:
			case GrupoTipoDt.PARTE:
			case GrupoTipoDt.MP:
				stRetorno = this.consultarModelosUsuarioJSON(descricao, posicao, id_ArquivoTipo, usuarioDt.getId_UsuarioServentia(), ordenacao, quantidadeRegistros);
				break;

			//Juízes visualizam todos os modelos (seus, genéricos e da serventia)
			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				stRetorno = this.consultarModelosGrupoJSON(descricao,  posicao, id_ArquivoTipo, usuarioDt.getId_Serventia(), usuarioDt.getId_UsuarioServentia());
				break;

			//Assistentes visualizam modelos (do chefe, genéricos e da serventia)
			case GrupoTipoDt.ASSESSOR:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				stRetorno = this.consultarModelosGrupoJSON(descricao,  posicao, id_ArquivoTipo, usuarioDt.getId_Serventia(), usuarioDt.getId_UsuarioServentiaChefe());
				break;
				
			//Usuários externos visualizam apenas seus próprios modelos
			case GrupoTipoDt.ASSESSOR_ADVOGADO:
			case GrupoTipoDt.ASSESSOR_MP:
				stRetorno = this.consultarModelosUsuarioJSON(descricao, posicao, id_ArquivoTipo, usuarioDt.getId_UsuarioServentiaChefe(), ordenacao, quantidadeRegistros);
				break;

			//Por padrão usuários visualizam modelos genéricos e da serventia
			default:
				stRetorno = this.consultarModelosServentiaJSON(descricao,  posicao, id_ArquivoTipo, usuarioDt.getId_Serventia());
				break;
			}
//		}
		
		return stRetorno;
	}

	//Criado pelo Jelves
	public String montaModeloOficialCertidao(OficialCertidaoDt oficialCertidaoDt, UsuarioDt usuarioDt, String id_Modelo) throws Exception {
		String texto = "";
		texto = consultarModeloCodigo(id_Modelo).getTexto();
		texto = montaServentia(usuarioDt.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(usuarioDt, texto);
		texto = montaConteudoOficialCertidao(oficialCertidaoDt, oficialCertidaoDt.getTexto());
		return texto;
	}

	private String montaConteudoOficialCertidao(OficialCertidaoDt oficialCertidaoDt, String texto) {
		
		//texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_NUMERO +"[\\s]*[}]", format(oficialCertidaoDt.getId()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROCESSO_NUMERO +"[\\s]*[}]", format(oficialCertidaoDt.getProcessoNumero()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_NUMERO_MANDADO +"[\\s]*[}]", format(oficialCertidaoDt.getNumeroMandado()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_SERVENTIA_NOME +"[\\s]*[}]", format(oficialCertidaoDt.getServentia()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROCESSO_PROMOVENTE_NOME +"[\\s]*[}]", format(oficialCertidaoDt.getProcessoPromoventeNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROCESSO_PROMOVIDO_NOME +"[\\s]*[}]", format(oficialCertidaoDt.getProcessoPromovidoNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_NOME +"[\\s]*[}]", format(oficialCertidaoDt.getProcessoPromoventeNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_DILIGENCIA_NOME_INTIMADO +"[\\s]*[}]", format(oficialCertidaoDt.getDiligenciaNomeIntimado()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_DILIGENCIA_RG_INTIMADO +"[\\s]*[}]", format(oficialCertidaoDt.getDiligenciaRGIntimado()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_DILIGENCIA_ENDERECO+"[\\s]*[}]", format(oficialCertidaoDt.getDiligenciaEndereco()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_LOGRADOURO +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoLogradouro()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_NUMERO +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoNumero()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_COMPLEMENTO +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoComplemento()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_QUADRA +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoQuadra()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_LOTE +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoLote()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_BAIRRO +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoBairro()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_CEP +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoCEP()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_CIDADE +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoCidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_PROMOVENTE_ENDERECO_UF +"[\\s]*[}]", format(oficialCertidaoDt.getPromoventeEnderecoUf()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_DATA_EMISSAO +"[\\s]*[}]", format(oficialCertidaoDt.getDataEmissao()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_USUARIO +"[\\s]*[}]", format(oficialCertidaoDt.getUsuario()));
		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_CARGO +"[\\s]*[}]", format(oficialCertidaoDt.getCargo()));
		return texto;
	}
	
	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		stTemp = neObjeto.consultarGrupoArquivoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);

		neObjeto = null;
		return stTemp;
	}
	
	/**
	 * Monta o conteúdo de uma certidão Negativa/Positiva
	 * @param certidao Dt contendo os dados da certidão
	 * @param servidor Dt contendo os dados do servidor
	 * @param id_modelo id do modelo a ser utilizado na certidão
	 * @return texto da certidão em html.
	 * @throws Exception
	 */
	public String montaConteudoPorModelo(CertidaoNegativaPositivaPublicaDt certidao, UsuarioDt servidor, String id_modelo) throws Exception {
		String texto = "";

		try{
			texto = super.consultarId(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
		texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacao(certidao, texto);
		
		return texto;
	}
	
	/** 
	 * Monta a identificação, ou seja, os dados do requerente de uma Certidão Negativa Positiva.
	 * @param certidaoDt Dt contendo os dados do requerente
	 * @param texto texto do modelo da certidão
	 * @return texto do modelo da certidão com os dados carregados
	 */
	private String montaCertidaoIdentificacao(CertidaoNegativaPositivaPublicaDt certidao, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE+"[\\s]*[}]", format(certidao.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NOME_MAE+"[\\s]*[}]", format(certidao.getNomeMae()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ+"[\\s]*[}]", format(certidao.getCpfCnpj()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DATA_NASCIMENTO+"[\\s]*[}]", format(certidao.getDataNascimento()));
		return texto;
	}
	
	public String consultarDescricaoArquivoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		ArquivoTipoNe ArquivoTipone = new ArquivoTipoNe(); 
		stTemp = ArquivoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String montaConteudoPorModelo(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt servidor, String id_modelo) throws Exception {
		ModeloDt modeloDt = super.consultarId(id_modelo);
		return montaConteudoPorModelo(audienciaMovimentacaoDt, servidor, modeloDt);
	}
	
	public String montaConteudoPorModelo(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt servidor, ModeloDt modelo) throws Exception {
		String texto = "";

		try{
			texto = modelo.getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
		texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);
		texto = montaAudienciaSessao(audienciaMovimentacaoDt, texto);
		
		return texto;
	}
	private String montaAudienciaSessao(AudienciaMovimentacaoDt audienciaMovimentacaoDt, String texto) throws Exception {
		return this.montaAudienciaSessao(audienciaMovimentacaoDt, texto, false);
	}
	
	private String montaAudienciaSessao(AudienciaMovimentacaoDt audienciaMovimentacaoDt, String texto, boolean apenasNomeRelator) throws Exception {
		return this.montaAudienciaSessao(audienciaMovimentacaoDt.getAudienciaDt(), texto, apenasNomeRelator);
	}

	private String montaAudienciaSessao(AudienciaProcessoDt audienciaProcessoDt, String texto, boolean apenasNomeRelator) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();
		AudienciaDt audienciaDt = audienciaNe.consultarId(audienciaProcessoDt.getId_Audiencia());
		
		List<AudienciaProcessoDt> audi = new ArrayList<AudienciaProcessoDt>();
		audi.add(audienciaProcessoDt);
		
		audienciaDt.setListaAudienciaProcessoDt(audi);
		return this.montaAudienciaSessao(audienciaDt, texto, apenasNomeRelator);
	}
	
	private String montaAudienciaSessao(AudienciaDt audienciaDt, String texto, boolean apenasNomeRelator) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();
	    AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
	    
	    RelatorioAudienciaProcesso relatorioAudienciaProcesso = audienciaNe.ObtenhaRelatorioAudienciaProcesso(audienciaDt, audienciaProcessoDt, true);
	    
	    if(apenasNomeRelator) {
	    	relatorioAudienciaProcesso.setRelator(relatorioAudienciaProcesso.getRelator().substring(0, relatorioAudienciaProcesso.getRelator().lastIndexOf('(')).trim());
	    }
		
	    texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_CLASSE_CNJ+"[\\s]*[}]", format(relatorioAudienciaProcesso.getProcessoTipo()));		
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_NUMERO+"[\\s]*[}]", format(relatorioAudienciaProcesso.getProcessoNumero()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_COMARCA+"[\\s]*[}]", format(relatorioAudienciaProcesso.getComarca()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_FORMADETRATAMENTO+"[\\s]*[}]", format(relatorioAudienciaProcesso.getDescricaoPoloAtivo()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_FORMADETRATAMENTO+"[\\s]*[}]", format(relatorioAudienciaProcesso.getDescricaoPoloPassivo()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_RELATOR+"[\\s]*[}]", format(relatorioAudienciaProcesso.getRelator()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_REPRESENTANTE_MP+"[\\s]*[}]", format(relatorioAudienciaProcesso.getProcuradorJustica()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_NOME+"[\\s]*[}]", format(relatorioAudienciaProcesso.getNomePoloAtivo()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_LISTA_NOME+"[\\s]*[}]", format(relatorioAudienciaProcesso.getListaNomePoloAtivo()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOATIVO_ADVOGADO+"[\\s]*[}]", format(relatorioAudienciaProcesso.getAdvogadoPoloAtivo().replaceAll("\n", "<BR/>")));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_NOME+"[\\s]*[}]", format(relatorioAudienciaProcesso.getNomePoloPassivo()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_LISTA_NOME+"[\\s]*[}]", format(relatorioAudienciaProcesso.getListaNomePoloPassivo()));
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_POLOPASSIVO_ADVOGADO+"[\\s]*[}]", format(relatorioAudienciaProcesso.getAdvogadoPoloPassivo().replaceAll("\n", "<BR/>")));
		
		texto = texto.replaceAll("[$][{][\\s]*"+AUDIENCIA_SESSAO_PROCESSO_LISTAPOLOS_AGRUPADOS+"[\\s]*[}]", format(relatorioAudienciaProcesso.getPolosAgrupados()).replaceAll("\n", "<BR/><BR/>"));
		
		return texto;
	}
	
	/**
	 * Monta o conteúdo de uma Certidão Narrativa
	 * @param certidao Dt contendo os dados da certidão
	 * @param servidor Dt contendo os dados do servidor
	 * @param id_modelo id do modelo a ser utilizado na certidão
	 * @return texto da certidão em html.
	 * @throws Exception
	 */
	//PRIMEIRO
	public String montaConteudoPorModelo(CertidaoGuiaDt certidao, UsuarioDt servidor, int modeloCodigo) throws Exception {

		String texto = "";		
		try{
			texto = this.consultarModeloCodigo(String.valueOf(modeloCodigo)).getTexto();			
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}		
	    texto = montaServentia(servidor.getId_Serventia(), texto);
		texto = montaOutros(texto);
		texto = montaUsuario(servidor, texto);		
		texto = montaCertidaoIdentificacao(certidao, texto);		
		texto = montaTextoCertidaoNarrativa(certidao, texto);		
		return texto;
	}
	
	/** 
	 * Monta a identificação, ou seja, os dados do requerente de uma Certidão Narrativa.
	 * @param certidaoDt Dt contendo os dados do requerente
	 * @param texto texto do modelo da certidão
	 * @return texto do modelo da certidão com os dados carregados
	 */
	private String montaCertidaoIdentificacao(CertidaoGuiaDt certidao, String texto) {
		
		// Informações do Requerente
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE+"[\\s]*[}]", format(certidao.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NATURALIDADE+"[\\s]*[}]", format(certidao.getNaturalidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_PROFISSAO+"[\\s]*[}]", format(certidao.getProfissao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_ESTADO_CIVIL+"[\\s]*[}]", format(certidao.getEstadoCivil()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DATA_NASCIMENTO+"[\\s]*[}]", format(certidao.getDataNascimento()));
		
		if(certidao.getSexo() != null && !certidao.getSexo().equalsIgnoreCase("")){
			String sexo = certidao.getSexo();
			sexo = Funcoes.removeEspacosExcesso(sexo);
			if (sexo.matches("[fF][eE][mM][iI][nN][iIíÍ][nN][oO]")) {
				sexo = "Feminino"; 
			} else if(sexo.matches("[Mm][aA][sS][cC][uU][lL][iI][nN][oO]")) {
				sexo = "Masculino";
			} else {
				sexo = "";
			}
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_SEXO+"[\\s]*[}]", format(sexo.matches("[M]|Masculino")?"Masculino":"Feminino"));
		} else {
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_SEXO+"[\\s]*[}]", "");
		}
		
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_RG+"[\\s]*[}]", format(certidao.getRg()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ+"[\\s]*[}]", format(certidao.getCpf()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DOMICILIO+"[\\s]*[}]", format(certidao.getDomicilio()));
		
		// Informações do Processo
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_PROTOCOLO+"[\\s]*[}]", format(certidao.getProtocolo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_JUIZO+"[\\s]*[}]", format(certidao.getJuizo()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NATUREZA+"[\\s]*[}]", format(certidao.getNatureza()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_VALOR_ACAO+"[\\s]*[}]", format(certidao.getValorAcao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE_PROCESSO+"[\\s]*[}]", format(certidao.getRequerenteProcesso()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_ADVOGADO_REQUERENTE+"[\\s]*[}]", format(certidao.getAdvogadoRequerente()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERIDO+"[\\s]*[}]", format(certidao.getRequerido()));		

		if(!certidao.getMovimentacoesProcesso().isEmpty()){
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_MOVIMENTACOES_PROCESSO+"[\\s]*[}]", format(certidao.getMovimentacoesProcesso()));			
		} else {
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_MOVIMENTACOES_PROCESSO+"[\\s]*[}]", "Não há movimentações.");
		}		

		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_TEXTO_CERTIDAO+"[\\s]*[}]", format(certidao.getTextoCertidao()));			
		
		if(!certidao.getNumeroGuia().equalsIgnoreCase("")){
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_NUMERO_GUIA +"[\\s]*[}]", format(certidao.getNumeroGuia()));
		} else {
			texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_NUMERO_GUIA +"[\\s]*[}]", "Certidão sem custas.");						
		}

		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CUSTA_CERTIDAO +"[\\s]*[}]", format(certidao.getCustaCertidao()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CUSTA_TAXA_JUDICIARIA +"[\\s]*[}]", format(certidao.getCustaTaxaJudiciaria()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CUSTA_TOTAL +"[\\s]*[}]", format(certidao.getCustaTotal()));
		
		return texto;
	}
	
	private String montaTextoCertidaoNarrativa(CertidaoGuiaDt certidao, String texto) {
		
		String textoCertidao = "";
		if(certidao.getTextoCertidao() != null){
			textoCertidao = certidao.getTextoCertidao();
		}		
		if(textoCertidao.equalsIgnoreCase("Certifica mais que,\r\n") || textoCertidao.equalsIgnoreCase("Certifica mais que, ") || textoCertidao.equalsIgnoreCase("Certifica mais que,") || textoCertidao.equalsIgnoreCase("")){
			textoCertidao = "";
		} else {			
			if(texto.contains("R$")){
				texto = texto.replace("R$", "R\\$");
			}
		}		
		textoCertidao = Funcoes.retirarTagsHTMLTexto(textoCertidao);
		//convertendo os caracteres HTML vindos do ckeditor em caracteres especiais
		textoCertidao = Funcoes.converterCaracteresHTMLEmCaracteresEspeciais(textoCertidao);
		
		texto = texto.replaceAll("[$][{][\\s]*" + CERTIDAO_NARRATIVA_TEXTO + "[\\s]*[}]", format(textoCertidao));
		
		return texto;
	}
	
	public String montaConteudoPorModeloPF(CertidaoGuiaDt certidaoGuiaDt, UsuarioDt servidor, String id_modelo, ServentiaDt serventiaDt) throws Exception {
		String texto = "";

		try{
//			texto = super.consultarId(id_modelo).getTexto();
			texto = this.consultarModeloCodigo(id_modelo).getTexto();
		} catch(Exception ex) {
			//PODE-SE COLOCAR UMA MENSAGEM para o usuario neste momento 
		}
		
		ProjudiPropriedades projudiPropriedade = ProjudiPropriedades.getInstance();
		
		texto = montaServentia(serventiaDt, projudiPropriedade, texto);
		texto = montaOutros(texto, projudiPropriedade);
		if (certidaoGuiaDt.getNomeUsuarioEscrivaoResponsavel() != null &&
			certidaoGuiaDt.getNomeUsuarioEscrivaoResponsavel().trim().length() > 0) {
			texto = texto.replaceAll("[$][{][\\s]*" + CERTIDAO_PRATICA_FORENSE_ESCRIVAO + "[\\s]*[}]", format(Funcoes.capitalizeNome(certidaoGuiaDt.getNomeUsuarioEscrivaoResponsavel())));
		} else {
			texto = texto.replaceAll("[$][{][\\s]*" + CERTIDAO_PRATICA_FORENSE_ESCRIVAO + "[\\s]*[}]", format(servidor.getNome()));
		}		
		texto = montaUsuario(servidor, texto);
		texto = montaCertidaoIdentificacaoPF(certidaoGuiaDt, texto);
		texto = montaQuantidadeProcessoPF(certidaoGuiaDt,texto);
		texto = montaCertidaoProcessosLacoPF(certidaoGuiaDt,texto);
		
		return texto;
	}
	
	/** 
	 * Monta a identificação, ou seja, os dados do requerente de uma Certidão de Prática Forense.
	 * @param certidaoGuiaDt Dt contendo os dados do requerente
	 * @param texto texto do modelo da certidão
	 * @return texto do modelo da certidão com os dados carregados
	 */
	private String montaCertidaoIdentificacaoPF(CertidaoGuiaDt certidaoGuiaDt, String texto) {
		String RGDescription = "";		
		if (certidaoGuiaDt.getRg() != null && certidaoGuiaDt.getRg().trim().length() > 0) {
			RGDescription = certidaoGuiaDt.getRg();
			if (certidaoGuiaDt.getRgOrgaoExpedidorSigla() != null && certidaoGuiaDt.getRgOrgaoExpedidorSigla().trim().length() > 0) {
				RGDescription += " - " + certidaoGuiaDt.getRgOrgaoExpedidorSigla().trim();				
			} else if (certidaoGuiaDt.getId_RgOrgaoExpedidor() != null && certidaoGuiaDt.getId_RgOrgaoExpedidor().trim().length() > 0) {
				try
				{
					RgOrgaoExpedidorDt rgOrgaoExpedidorDt = new RgOrgaoExpedidorNe().consultarId(certidaoGuiaDt.getId_RgOrgaoExpedidor());
					if (rgOrgaoExpedidorDt != null) {
						if (rgOrgaoExpedidorDt.getSigla() != null && rgOrgaoExpedidorDt.getSigla().trim().length() > 0) {
							RGDescription += " - " + rgOrgaoExpedidorDt.getSigla().trim();
						}
						if (rgOrgaoExpedidorDt.getUf() != null && rgOrgaoExpedidorDt.getUf().trim().length() > 0) {
							RGDescription += " - " + rgOrgaoExpedidorDt.getUf().trim();
						}
					}
					
				} catch (Exception e) { }
			}
		}
		
		String OABDescription = "";	
		if (certidaoGuiaDt.getOab() != null && certidaoGuiaDt.getOab().trim().length() > 0) {
			OABDescription = certidaoGuiaDt.getOab().trim();			
			if (certidaoGuiaDt.getOabComplemento() != null && certidaoGuiaDt.getOabComplemento().trim().length() > 0) {
				OABDescription += " - " + certidaoGuiaDt.getOabComplemento().trim();
			}
			if (certidaoGuiaDt.getOabUf() != null && certidaoGuiaDt.getOabUf().trim().length() > 0) {
				OABDescription += " - " + certidaoGuiaDt.getOabUf().trim();
			}
		}
		
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_REQUERENTE+"[\\s]*[}]", format(certidaoGuiaDt.getNome()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_ESTADO_CIVIL+"[\\s]*[}]", format(certidaoGuiaDt.getEstadoCivil()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_SEXO+"[\\s]*[}]", format(certidaoGuiaDt.getSexo().equals("M")?"Masculino":certidaoGuiaDt.getSexo().equals("F") ? "Feminino": ""));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_CPFCNPJ+"[\\s]*[}]", format(Funcoes.formataCPF(certidaoGuiaDt.getCpf())));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_RG+"[\\s]*[}]", format(RGDescription));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_DOMICILIO+"[\\s]*[}]", format(certidaoGuiaDt.getDomicilio()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_NATURALIDADE +"[\\s]*[}]", format(certidaoGuiaDt.getNaturalidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_IDENTIFICACAO_OAB+"[\\s]*[}]", format(OABDescription));
		
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_NUMERO_GUIA +"[\\s]*[}]", format(certidaoGuiaDt.getNumeroGuia()));			
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CUSTA_CERTIDAO +"[\\s]*[}]", format(Funcoes.FormatarDecimal(certidaoGuiaDt.getCustaCertidao())));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CUSTA_TAXA_JUDICIARIA +"[\\s]*[}]", format(Funcoes.FormatarDecimal(certidaoGuiaDt.getCustaTaxaJudiciaria())));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_CUSTA_TOTAL +"[\\s]*[}]", format(Funcoes.FormatarDecimal(certidaoGuiaDt.getCustaTotal())));
		
		return texto;
	}
	
	private String montaQuantidadeProcessoPF(CertidaoGuiaDt certidaoGuiaDt, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PRATICA_FORENSE_QUANTIDADE +"[\\s]*[}]", format(certidaoGuiaDt.getQuantidade()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PRATICA_FORENSE_DATA_FINAL +"[\\s]*[}]", format(certidaoGuiaDt.getMesTexto(certidaoGuiaDt.getMesFinal())+ " de " + certidaoGuiaDt.getAnoFinal()));
		texto = texto.replaceAll("[$][{][\\s]*"+CERTIDAO_PRATICA_FORENSE_DATA_INICIAL +"[\\s]*[}]", format(certidaoGuiaDt.getMesTexto(certidaoGuiaDt.getMesInicial())+ " de " + certidaoGuiaDt.getAnoInicial()));
		
		return texto;
	}
	
	/**
	 * Monta a relação de processos da certidão de Prática Forense
	 * @param certidaoDt
	 * @param texto
	 * @return texto do modelo com as variáveis substituidas
	 */
	private String montaCertidaoProcessosLacoPF(CertidaoGuiaDt certidaoGuiaDt, String texto) {
		
		texto = texto.replaceAll("[\\n\\r]+", "");

		String patternStr = "[$][{][\\s]*" + CERTIDAO_PROCESSOS_INICIO + "[\\s]*[}](.*?)[$][{][\\s]*" + CERTIDAO_PROCESSOS_FIM + "[\\s]*[}]";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			//Seleciona as partes do processo do tipo especifico
			//Deve ser uma string o tipo da parte
			List processos = certidaoGuiaDt.getListaProcesso();
			
			StringBuilder builder = new StringBuilder();

			//Monta dados das partes
			this.montaProcessoPraticaForense(builder, matcher, 1, processos);

			texto = texto.replaceFirst(patternStr, Matcher.quoteReplacement(builder.toString()));
		}

		return texto;		
	}
	
	
	protected String montaRedator(ProcessoDt processoDt, String texto)  throws Exception {
		FabricaConexao obFabricaConexao = null;
		VotoPs votoPs = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			votoPs = new VotoPs(obFabricaConexao.getConexao());
			String idAudienciaProcesso = new AudienciaProcessoNe().consultarAudienciaProcessoDoProcesso(processoDt.getId());
			Optional<VotoDt> votoDivergente = votoPs.consultarVotosSessao(idAudienciaProcesso)
					.stream()
					.filter(voto -> StringUtils.equals(voto.getVotoTipoCodigo(), String.valueOf(VotoTipoDt.DIVERGE)))
					.findFirst();
			if(votoDivergente.isPresent())
				texto.replaceAll("[$][{][\\s]*"+ModeloNe.AUDIENCIA_SESSAO_REDATOR+"[\\s]*[}]", votoDivergente.get().getNomeVotante());
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return texto;
	}
	
	/**
	 * Método que exclui modelos levando em consideração o perfil do usuário que está realizando a ação.
	 * Motivo: somente o usuário ADMINISTRADOR pode excluir modelos que não tem ID_SERV (próprios).
	 * @param dados - informações do modelo 
	 * @param isUsuarioAdministrador - se o grupo do usuário logado é Administrador
	 * @throws Exception
	 */
	public void excluir(ModeloDt dados, boolean isUsuarioAdministrador) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ModeloPs obPersistencia = new ModeloPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Modelo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId(), isUsuarioAdministrador); 
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarNomeProprietarioModelo(String idUsuarioServentia) throws Exception {
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		
		UsuarioServentiaDt usuarioDt = usuarioServentiaNe.consultarId(idUsuarioServentia);
		
		if(usuarioDt != null && usuarioDt.getNome() != null) {
			return usuarioDt.getNome();
		} else {
			return "Nome não localizado.";
		}
	}
	
	/**
	 * Consulta o html do modelo e retorna um json
	 * @param codigoModelo
	 * @return json com id, modelo e texto
	 * @throws Exception
	 */
	public String consultarTextoModeloParaECartaJSON(String codigoModelo) throws Exception {
		CorreiosDt.ModeloCarta modeloCarta = new CorreiosDt().getModelo(codigoModelo);							
		ModeloDt modeloDt = this.consultarId(modeloCarta.getId_Modelo());		
		String texto = modeloDt.getJSON().toString();
		texto = texto.replaceAll("[$][{][\\s]*" + ModeloNe.BRASAO + "[\\s]*[}]", "<img src=\'"+ConverterHtmlPdf.BRASAO_BASE64_REDUZIDO+"\'></img>");
		texto = texto.replaceAll("[$][{][\\s]*" + ModeloNe.BRASAO_GOIAS + "[\\s]*[}]", "<img src=\'"+ConverterHtmlPdf.BRASAO_BASE64_REDUZIDO+"\'></img>");				
		return texto;
	}
	
	
	/**
	 * Retonar a quantidade de locomoção vinculada ao modelo
	 * @param idModelo
	 * @return String 
	 * @throws Exception
	 */
	public String consultarQtdLocomocao(String idModelo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String qtdLocomocao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ModeloPs modeloPs = new ModeloPs(obFabricaConexao.getConexao());
			qtdLocomocao = modeloPs.consultarQtdLocomocao(idModelo);
		} 
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return qtdLocomocao;
	}

	/**
	 * Substitui variáveis relacionadas à expedição de mandados para a Central de Mandados.
	 * @param pendenciaDt
	 * @param texto
	 * @return
	 */
	public String montaConteudoMandadoExpedicao(String numeroReservadoMandadoExpedir, String texto) {
		texto = texto.replaceAll("[$][{][\\s]*"+MANDADO_CENTRAL_NUMERO_EXPEDIDO +"[\\s]*[}]", format(numeroReservadoMandadoExpedir));
		return texto;
	}
}
