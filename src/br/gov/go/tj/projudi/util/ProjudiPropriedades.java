/*
 * Created on 6 févr. 2005
 *
 * Este arquivo é parte do Projudi.
 * Não pode ser distribuído.
 */
package br.gov.go.tj.projudi.util;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PropriedadeDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.PropriedadeNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * @author Jesus Rodrigo Corrêa
 * @Colaboração André Luis C. Moreira
 * @Colaboração Leandro Lima Lira
 * Esta classe lista todas as propriedades do sistema
 * Singleton Design Pattern está sendo utilizado aqui
 *
 */
public class ProjudiPropriedades {
    private static Map<Integer, String> mapPropriedades = new HashMap<>();    

	public static final int Estado = 1;
	public static final int Sistema = 2;
	public static final int hostEmail = 3;
	public static final int dominioEMail = 4;
	public static final int nomeRemetenteEmail = 5;
	public static final int linkSistemaNaWEB = 6;
	public static final int eMailRemetente = 7;
	public static final int uf = 8;
	public static final int sgbdUtilizado = 9;
	public static final int tempoExpiraSessao = 10;
	public static final int hostID = 11;
	public static final int tamMaxArquivo = 12;
	public static final int tempDir = 13;
	public static final int certKeySize = 14;
	public static final int caminhoCertificados = 15;
	public static final int caminhoCAConfiavel = 16;
	public static final int senhaCertificadoRaiz = 17;
	public static final int pontoDeDistribuicao = 18;
	public static final int caminhoValidacaoDireta = 19;
	public static final int caminhoIdentidades = 20;
	public static final int senhaCertificadoEmissor = 21;
	public static final int caminhoLRC = 22;
	public static final int senhaIdentidadeDigitalSistema = 23;
	public static final int dirProcessos = 24;
	public static final int dirDocumentos = 25;
	public static final int enderecoProxy = 26;
	public static final int portaProxy = 27;
	public static final int usuarioProxy = 28;
	public static final int senhaProxy = 29;
	public static final int validadeID = 30;
	public static final int enderecoValidacaoPublicacao = 31;
	public static final int servidorSPG = 32;
	public static final int servidorSPGTipo = 33;
	public static final int enderecoWebSiteAudienciaPublicada = 34;
	public static final int chaveBaseHashWebSiteAudienciaPublicada = 35;	
	public static final int envioDeEmailHabilitado = 36;
	public static final int quantidadeMinimaDiasMarcarSessao2Grau = 37;
	public static final int urlServidorSPG_SSG = 38;
	public static final int enderecoValidacaoCertidao = 39;
//	public static final int enderecoWebSiteAudienciaPublicadaBaixarVideo = 40;
//	public static final int enderecoWebSiteAudienciaPublicadaBaixarVideoLinkLocal = 41;
	public static final int enderecoWebSiteConsultaProcessoSPG_SSG = 43;
	
	public static final int SPG_DADOS_GERAIS = 44;
	public static final int SPG_INTERLOCUTORIAS = 45;
	public static final int SPG_MANDADOS = 46;
	public static final int SPG_HISTORICO =47;	
	public static final int SPG_SENTENCA =48;
	public static final int SPG_INTIMACAO =49;
	public static final int SPG_REDISTRIBUICAO =50;
	public static final int SPG_VERIFICAR_ACESSO =51;
	public static final int SPG_LIGACOES = 52;
	
	public static final int OBJECT_STORAGE_HOST = 55;
	
	public static final int OBJECT_STORAGE_ACCESSKEY_DIGITALIZACAO = 53;
	public static final int OBJECT_STORAGE_SECRETKEY_DIGITALIZACAO = 54;
	public static final int OBJECT_STORAGE_BUCKET_DIGITALIZACAO = 56;
		
	public static final int OBJECT_STORAGE_UPLOAD_ACCESSKEY = 83;
	public static final int OBJECT_STORAGE_UPLOAD_SECRETKEY = 84;
	public static final int OBJECT_STORAGE_UPLOAD_CURRENT_BUCKET = 86;
	
	public static final int OBJECT_STORAGE_ACCESSKEY_PROJUDI = 104;
	public static final int OBJECT_STORAGE_SECRETKEY_PROJUDI = 105;
	public static final int OBJECT_STORAGE_BUCKET_PROJUDI = 106;
	public static final int OBJECT_STORAGE_HABILITADO_PROJUDI = 107;
	
	public static final int OBJECT_STORAGE_UPLOAD_TIPO_ARQUIVO = 87;
	public static final int OBJECT_STORAGE_UPLOAD_ENVIO_JAVASCRIPT = 94;
	public static final int OBJECT_STORAGE_UPLOAD_TAMANHO_MAXIMO = 95;
	public static final int OBJECT_STORAGE_UPLOAD_QUANTIDADE_MAXIMA = 96;
	public static final int OBJECT_STORAGE_UPLOAD_PROTOCOLO_HTTP = 97;
	public static final int OBJECT_STORAGE_UPLOAD_VERBO_PUT = 98;
	public static final int OBJECT_STORAGE_UPLOAD_FORCE_IFRAME_TRANSPORT = 99;
	public static final int OBJECT_STORAGE_UPLOAD_TAMANHO_PARTE = 100;
	public static final int OBJECT_STORAGE_UPLOAD_PASTA_TEMPORARIA = 101;
		
	public static final int quantidadeDiasMarcarAudienciaAutomaticaCejusc = 57;
	public static final int quantidadeDiasMarcarMediacaoAutomaticaCejusc = 58;
	public static final int quantidadeDiasMarcarAudienciaDPVATAutomaticaCejusc = 59;
	
	public static final int SPG_NOME_SERVICO = 60;
	
	public static final int USER_BNPR_TRIBUNAIS = 61;
	public static final int PASS_BNPR_TRIBUNAIS = 62;
	public static final int URL_BNPR_TRIBUNAIS = 63	;
	
	public static final int CAIXA_URL_WEBSERVICE = 65;
	public static final int CAIXA_USUARIO_WEBSERVICE = 66;
	public static final int CAIXA_CODIGO_BENEFICIARIO = 67;
	
	public static final int NUGEP_EMAIL = 68;
	public static final int NUGEP_DESCRICAO = 69;
	
	public static final int TAXA_JUDICIARIA_VALOR_MEIO_PORCENTO = 80;
	public static final int TAXA_JUDICIARIA_VALOR_UM_SETENTA_CINCO_PORCENTO = 81;
	
	public static final int HOST_ELASTICSEARCH = 300;
	public static final int PORT_ELASTICSEARCH = 301;
	public static final int PATH_ELASTICSEARCH = 302;
	public static final int USER_ELASTICSEARCH = 303;
	public static final int PASS_ELASTICSEARCH = 304;
	public static final int TAMINO_LAST_INDEXED = 305;
	public static final int SPG_HOST = 88;
	
	public static final int FLAG_IMPORTACAO_GUIAS_SPG = 89; //"ATIVO" ou qualquer outra string, como, "DESATIVADO"
	public static final int ID_PROCESSO_IMPORTACAO_GUIAS_SPG = 90;
	
	public static final int CORREIOS_URL_WEBSERVICE = 91;
	public static final int CORREIOS_USUARIO_WEBSERVICE = 92;
	public static final int CORREIOS_SENHA_WEBSERVICE = 93;
		
	public static String stCaminhoAplicacao = "";

	public String getCaminhoAplicacao() {
	    return stCaminhoAplicacao;
	}
	
	public void setCaminhoAplicacao(String caminho){
	    stCaminhoAplicacao = caminho;
	}
	
	private static ProjudiPropriedades projudi = null;
	//constantes que representa a nomenclatura do Mysql ("myqsl")
	public static final String NOME_MYSQL = "mysql";
	//constantes que representa a nomenclatura do PostgreSql ("postgresql")
	public static final String NOME_POSTGRESQL = "postgresql";
	//constantes que representa a nomenclatura do Oracle ("oracle")
	public static final String NOME_ORACLE = "oracle";
	//constante que representa o nome do banco a ser utilizado NAS INSERÇÕES transacionais
	//(não confundir com nome do SGBD)
	public static final String NOME_BANCO = "projuditjgo";
	

	private X509Certificate certificadoEmissor;	

	private ProjudiPropriedades() {		
	}

	/**
	 * Método que retorna a instância única
	 * @return A Instância única. Se esta nao existir, e criada uma nova, mas sempre recarrega as configurações do banco de dados.
	 * @throws Exception 
	 */
	public static ProjudiPropriedades getInstance()  {
		if (ProjudiPropriedades.projudi == null) {
			ProjudiPropriedades.projudi = new ProjudiPropriedades();
		}
			
		getPropriedades();
		
		return ProjudiPropriedades.projudi;
	}

	/**
	 * Retorna o tempo de expiração da sessão, em segundos
	 * @return O tmepo em segudos para expirar uma sessão
	 */
	public int getTempoExpiraSessao() {
		return Funcoes.StringToInt(mapPropriedades.get(tempoExpiraSessao).toString());
	}

	/**
	 * Método que retorna o tamanho da chave do certificado
	 * @return O tamanho da chave do certificado
	 */
	public int getCertKeySize() {
		return Funcoes.StringToInt(mapPropriedades.get(certKeySize).toString());
	}

	/**
	 * Método que retorna a senha do certificado raiz
	 * @return Senha do certificado raiz
	 */
	public String getSenhaCertificadoRaiz() {
		return mapPropriedades.get(senhaCertificadoRaiz).toString();
	}

	/**
	 * Método que retorna o caminho para a pasta onde ficam os certificados
	 * @return O caminho para a pasta onde ficam os certificados
	 */
	public String getCaminhoCertificados() {
		return mapPropriedades.get(caminhoCertificados).toString();
	}

	/**
	 * Método que retorna o ponto de distribuição
	 * @return Caminho do ponto de distribuição
	 */
	public String getPontoDeDistribuicao() {
		return mapPropriedades.get(pontoDeDistribuicao).toString();
	}

	/**
/	 * Método que retorna o ponto de distribuição
	 * @return Caminho do ponto de distribuição
	 */
	public String getEnderecoValidacaoPublicacao() {
		return mapPropriedades.get(enderecoValidacaoPublicacao).toString();
	}

	/**
	 * Método que retorna o caminho para validação direta
	 * @return Caminho para validação direta
	 */
	public String getCaminhoValidacaoDireta() {
		return mapPropriedades.get(caminhoValidacaoDireta).toString();
	}

	/**
	 * Método que retorna a senha do certificado emissor
	 * @return A senha do certificado emissor
	 */
	public String getSenhaCertificadoEmissor() {
		return mapPropriedades.get(senhaCertificadoEmissor).toString();
	}

	/**
	 * Método que retorna a senha da IdentidadeDigitalSistema
	 * @return A senha da IdentidadeDigitalSistema
	 */
	public String getSenhaIdentidadeDigitalSistema() {
		return mapPropriedades.get(senhaIdentidadeDigitalSistema).toString();
	}

	/**
	 * Método que retorna o caminho onde ficam as identidades dos usuários
	 * @return O caminho onde ficam as identidados dos usuários
	 */
	public String getCaminhoIdentidades() {
		return mapPropriedades.get(caminhoIdentidades).toString();
	}

	/**
	 * Método que retorna a validade em anexo para os certificados dos usuários
	 * @return A validade em anexo para os certificados dos usuários
	 */
	public int getValidadeID() {
		return Funcoes.StringToInt(mapPropriedades.get(validadeID).toString());
	}

	/**
	 * Método que retorna o caminho do diretório utilizado para armazenamento temporário de arquivos
	 * @return O caminho do diretório utilizado para armazenamento temporário de arquivos
	 */
	public String getTempDir() {
		return mapPropriedades.get(tempDir).toString();
	}

	/**
	 * Método que retorna o tamanho máximo (em bits) de um arquivo para upload no sistema
	 * @return O tamanho máximo (em bits) de um arquivo para upload no sistema
	 */
	public int getTamMaxArquivo() {
		return Funcoes.StringToInt(mapPropriedades.get(tamMaxArquivo).toString());
	}

	/**
	 * Método que retorna caminho do diretório onde ficam os arquivos dos processos
	 * @return O caminho do diretório onde ficam os arquivos dos processos
	 */
	public String getDirProcessos() {
		return mapPropriedades.get(dirProcessos).toString();
	}

	/**
	* Método que retorna o caminho para distribuição da lista e certificados revogados dos usuários
	* @return O caminho para distribuição da lista e certificados revogados dos usuários
	*/
	public String getCaminhoLRC() {
		return mapPropriedades.get(caminhoLRC).toString();
	}

	/**
	 * Método que retorna o nome do Estado (Ente federativo) onde está atuando o sistema
	 * @return O nome do Estado (Ente federativo) onde está atuando o sistema
	 */
	public String getNomeEstado() {
		return mapPropriedades.get(Estado).toString();
	}

	/**
	 * Método que catura o domínio para manipulação/envio de e-mails
	 * @return Domínio para manipulação/envio de emails
	 */
	public String getDominioEMail() {
		return mapPropriedades.get(dominioEMail).toString();
	}

	/**
	 * Método que retorna o host para envio de e-mails
	 * @return Host para envio de e-mails
	 */
	public String getHostEmail() {
		return mapPropriedades.get(hostEmail).toString();
	}

	/**
	 * Método que retorna o nome do remetente a ser utilizado nos e-mails enviados
	 * pelo sistema
	 * @return O nome do remetente a ser utilizado nos e-mails enviados pelo sistema
	 */
	public String getNomeRemetenteEmail() {
		return mapPropriedades.get(nomeRemetenteEmail).toString();
	}

	/**
	 * Método que retorna o lik para o sistema na web
	 * @return O link para o sistema na web
	 */
	public String getLinkSistemaNaWEB() {
		return mapPropriedades.get(linkSistemaNaWEB).toString();
	}

	/**
	 * Método que retorna o nome do sistema
	 * @return O nome do sistema
	 */
	public String getNomeSistema() {
		return mapPropriedades.get(Sistema).toString();
	}

	/**
	 * Método que retorna o e-mail do remetente utilizado pelo sistema no envio das mensagens
	 * @return O e-mail do remetente utilizado pelo sistema no envio das mensagens
	 */
	public String getEMailRemetente() {
		return mapPropriedades.get(eMailRemetente).toString();
	}

	/**
	 * Método que retorna o nome do sgbd utilizado. Útil para trechos do código
	 * que são privativos de algum sgbd, como o cálculo de "Tempo médio nas estatísticas"
	 * @return O nome do sgbd utilizado
	 */
	public String getSgbdUtilizado() {
		return mapPropriedades.get(sgbdUtilizado).toString();
	}

	/**
	 * Método que retorna o UF do Estado onde está o sistema
	 * @return O UF do Estado onde está o sistema
	 */
	public String getUf() {
		return mapPropriedades.get(uf).toString();
	}

	public String getCaminhoCacheLCR() {
		return mapPropriedades.get(caminhoLRC).toString();
	}

	public boolean getEnvioDeEmailHabilitado() {
		return Funcoes.StringToBoolean( Funcoes.FormatarLogico( mapPropriedades.get(envioDeEmailHabilitado).toString() ) );
	}	
	
	public String getCaminhoCAConfiavel() {
		//return "D:\\acs-confiaveis";
		return mapPropriedades.get(caminhoCAConfiavel).toString();
	}

	public String getEnderecoProxy() {
		return mapPropriedades.get(enderecoProxy).toString();
	}

	public boolean isUsarProxy() {
		boolean boTemp = false;
		if (mapPropriedades.get(usuarioProxy).toString().length() > 0) boTemp = true;
		return boTemp;
	}

	public int getPortaProxy() {
		return Funcoes.StringToInt(mapPropriedades.get(portaProxy).toString());
	}

	public String getUsuarioProxy() {
		return mapPropriedades.get(usuarioProxy).toString();
	}

	public String getSenhaProxy() {
		return mapPropriedades.get(senhaProxy).toString();
	}

	public String getNomeBanco() {
		return NOME_BANCO;
	}

	public int getHostId() {
		return Funcoes.StringToInt(mapPropriedades.get(hostID).toString());
	}

	public X509Certificate getCertificadoEmissor() {
		return certificadoEmissor;
	}

	public void setCertificadoEmissor(X509Certificate certificadoEmissor) {
		this.certificadoEmissor = certificadoEmissor;
	}

	public String getServidorSPG() {
		return mapPropriedades.get(servidorSPG).toString();
	}

	public String getServidorSPGTipo() {
		return mapPropriedades.get(servidorSPGTipo).toString();		
	}
	
	public String getEnderecoWebSiteAudienciaPublicada() {
		//"https://www.tjgo.jus.br/audiencias/VisualizacaoAudiencia";
		return mapPropriedades.get(enderecoWebSiteAudienciaPublicada).toString();		
	}
	
	public String getChaveBaseHashWebSiteAudienciaPublicada() {
		return mapPropriedades.get(chaveBaseHashWebSiteAudienciaPublicada).toString();		
	}
	
	public int getQuantidadeMinimaDiasMarcarSessao2Grau() {
		return Funcoes.StringToInt(mapPropriedades.get(quantidadeMinimaDiasMarcarSessao2Grau).toString());		
	}
	
	public String getUrlServidorSPG_SSG() {
		return mapPropriedades.get(urlServidorSPG_SSG).toString();		
	}	
	
	public String getEnderecoValidacaoCertidao() {
		return mapPropriedades.get(enderecoValidacaoCertidao).toString();
	}
	
	public String getEnderecoWebSiteConsultaProcessoSPG_SSG() {		
		return mapPropriedades.get(enderecoWebSiteConsultaProcessoSPG_SSG).toString();
	}
	
	public String getObjectStorageHost() {
		return mapPropriedades.get(OBJECT_STORAGE_HOST).toString();
	}
	
	public String getObjectStorageAccessKeyDigitalizacao() {		
		return mapPropriedades.get(OBJECT_STORAGE_ACCESSKEY_DIGITALIZACAO).toString();
	}
	
	public String getObjectStorageSecretKeyDigitalizacao() {
		return mapPropriedades.get(OBJECT_STORAGE_SECRETKEY_DIGITALIZACAO).toString();
	}
	
	public String getObjectStorageBucketDigitalizacao() {
		return mapPropriedades.get(OBJECT_STORAGE_BUCKET_DIGITALIZACAO).toString();
	}
	
	public String getObjectStorageAccessKeyProjudi() {		
		return mapPropriedades.get(OBJECT_STORAGE_ACCESSKEY_PROJUDI).toString();
	}
	
	public String getObjectStorageSecretKeyProjudi() {
		return mapPropriedades.get(OBJECT_STORAGE_SECRETKEY_PROJUDI).toString();
	}
	
	public String getObjectStorageBucketProjudi() {
		return mapPropriedades.get(OBJECT_STORAGE_BUCKET_PROJUDI).toString();
	}
	
	public boolean isObjectStorageProjudiHabilitado() {
		return Funcoes.StringToBoolean( Funcoes.FormatarLogico(mapPropriedades.get(OBJECT_STORAGE_HABILITADO_PROJUDI).toString()) );
	}
	
	public String getObjectStorageUploadAccessKey() {
		return mapPropriedades.get(OBJECT_STORAGE_UPLOAD_ACCESSKEY).toString();
	}
	
	public String getObjectStorageUploadSecretKey() {
		return mapPropriedades.get(OBJECT_STORAGE_UPLOAD_SECRETKEY).toString();
	}
	
	public String getObjectStorageUploadCurrentBucket() {
		return mapPropriedades.get(OBJECT_STORAGE_UPLOAD_CURRENT_BUCKET).toString();
	}
	
	public String getObjectStorageUploadTipoArquivo() {
		return mapPropriedades.get(OBJECT_STORAGE_UPLOAD_TIPO_ARQUIVO).toString();
	}
	
	public boolean isObjectStorageUploadEnvioJavascript() {
		return Funcoes.StringToBoolean( Funcoes.FormatarLogico(mapPropriedades.get(OBJECT_STORAGE_UPLOAD_ENVIO_JAVASCRIPT).toString()) );
	}
	
	public int getObjectStorageUploadTamanhoMaximo() {
		return Funcoes.StringToInt(mapPropriedades.get(OBJECT_STORAGE_UPLOAD_TAMANHO_MAXIMO).toString());		
	}
	
	public int getObjectStorageUploadQuantidadeMaxima() {
		return Funcoes.StringToInt(mapPropriedades.get(OBJECT_STORAGE_UPLOAD_QUANTIDADE_MAXIMA).toString());		
	}
	
	public boolean isObjectStorageUploadProtocoloHTTP() {
		return Funcoes.StringToBoolean( Funcoes.FormatarLogico(mapPropriedades.get(OBJECT_STORAGE_UPLOAD_PROTOCOLO_HTTP).toString()) );
	}
	
	public boolean isObjectStorageUploadVerboPUT() {
		return Funcoes.StringToBoolean( Funcoes.FormatarLogico(mapPropriedades.get(OBJECT_STORAGE_UPLOAD_VERBO_PUT).toString()) );
	}
	
	public boolean isObjectStorageUploadForceIframeTransport() {
		return Funcoes.StringToBoolean( Funcoes.FormatarLogico(mapPropriedades.get(OBJECT_STORAGE_UPLOAD_FORCE_IFRAME_TRANSPORT).toString()) );
	}
	
	/**
	 * maxChunkSize
	 * @return
	 */
	public int getObjectStorageUploadTamanhoParte() {
		return Funcoes.StringToInt(mapPropriedades.get(OBJECT_STORAGE_UPLOAD_TAMANHO_PARTE).toString());		
	}
	
	public String getObjectStorageUploadPastaTemporaria() {
		return mapPropriedades.get(OBJECT_STORAGE_UPLOAD_PASTA_TEMPORARIA).toString();		
	}
	
	public int getQuantidadeDiasMarcarAudienciaAutomaticaCejusc() {
		return Funcoes.StringToInt(mapPropriedades.get(quantidadeDiasMarcarAudienciaAutomaticaCejusc).toString());		
	}
	
	public int getQuantidadeDiasMarcarMediacaoAutomaticaCejusc() {
		return Funcoes.StringToInt(mapPropriedades.get(quantidadeDiasMarcarMediacaoAutomaticaCejusc).toString());		
	}
	
	public int getQuantidadeDiasMarcarAudienciaDPVATAutomaticaCejusc() {
		return Funcoes.StringToInt(mapPropriedades.get(quantidadeDiasMarcarAudienciaDPVATAutomaticaCejusc).toString());		
	}
	
	public String getSPGNomeServico() {
		return mapPropriedades.get(SPG_NOME_SERVICO).toString();		
	}
	
	public String getHostElasticSearch(){
		return mapPropriedades.get(HOST_ELASTICSEARCH).toString();
	}
	
	public String getPortElasticSearch(){
		return mapPropriedades.get(PORT_ELASTICSEARCH).toString();
	}

	public String getPathElasticSearch(){
		return mapPropriedades.get(PATH_ELASTICSEARCH).toString();
	}
	
	public String getUserElasticSearch(){
		return mapPropriedades.get(USER_ELASTICSEARCH).toString();
	}
	
	public String getPasswordElasticSearch(){
		return mapPropriedades.get(PASS_ELASTICSEARCH).toString();
	}
	
	public String getUserBnprTribunais(){
		return mapPropriedades.get(USER_BNPR_TRIBUNAIS).toString();
	}
	
	public String getPasswordBnprTribunais(){
		return mapPropriedades.get(PASS_BNPR_TRIBUNAIS).toString();
	}
	
	public String getUrlBnprTribunais(){
		return mapPropriedades.get(URL_BNPR_TRIBUNAIS).toString();
	}
	
	public String getPropriedade(int propriedade) throws Exception {
		return mapPropriedades.get(propriedade).toString();
	}
	
	private static TJDataHora dataHoraCache = new TJDataHora();
	private static final int TEMPO_CACHE_MILISEGUNDOS = 60000;
	
	private static boolean isCacheValido() {
		if (mapPropriedades.size() == 0) return false;
		
		TJDataHora dataHoraComparacao = TJDataHora.CloneObjeto(dataHoraCache);
		dataHoraComparacao.adicioneMilisegundos(TEMPO_CACHE_MILISEGUNDOS);
		
		TJDataHora dataHoraAtual = new TJDataHora();
				
		return dataHoraComparacao.ehApos(dataHoraAtual);
	}
	
	public static void getPropriedades() {
		if (isCacheValido()) return;
		
		PropriedadeNe obPropriedades = new PropriedadeNe();
		List<PropriedadeDt> liPropriedade;
		try{
			liPropriedade = obPropriedades.getPropriedades();

			for (int i = 0; i < liPropriedade.size(); i++) {
				PropriedadeDt dados = new PropriedadeDt();
				dados = (PropriedadeDt) liPropriedade.get(i);
				mapPropriedades.put(Funcoes.StringToInt(dados.getPropriedadeCodigo()), dados.getValor());				
			}
			
			dataHoraCache = new TJDataHora();
		} catch(Exception e) {
			LogNe logNe = new LogNe();   	 	         
	        try{
				logNe.salvarErro(new LogDt("ProjudiPropriedades.getPropriedade()", "", UsuarioDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.Erro), "",  e.getMessage()));
			} catch(Exception e1) {
				
				e1.printStackTrace();
			}
		}
	}

	public String getUrlCaixa() {
		return mapPropriedades.get(CAIXA_URL_WEBSERVICE).toString();
	}

	public String getUsuarioCaixa() {
		return mapPropriedades.get(CAIXA_USUARIO_WEBSERVICE).toString();
	}

	public String getBeneficiarioCaixa() {
		return mapPropriedades.get(CAIXA_CODIGO_BENEFICIARIO).toString();
	}
	
	public String getNugepEmail(){
		return mapPropriedades.get(NUGEP_EMAIL).toString();
	}
	
	public String getNugepDescricao(){
		return mapPropriedades.get(NUGEP_DESCRICAO).toString();
	}
	
	public String getTaxaJudiciariaValorMeioPorcento() {
		return mapPropriedades.get(TAXA_JUDICIARIA_VALOR_MEIO_PORCENTO).toString();
	}
	
	public String getTaxaJudiciariaValorUmSetentaCincoPorcento() {
		return mapPropriedades.get(TAXA_JUDICIARIA_VALOR_UM_SETENTA_CINCO_PORCENTO).toString();
	}
	
	public String getSPGHost() {
		return mapPropriedades.get(SPG_HOST).toString();		
	}
	
	public String getUrlCorreios(){
		return mapPropriedades.get(CORREIOS_URL_WEBSERVICE).toString();
	}
	
	public String getUsuarioCorreios(){
		return mapPropriedades.get(CORREIOS_USUARIO_WEBSERVICE).toString();
	}
	
	public String getSenhaCorreios(){
		return mapPropriedades.get(CORREIOS_SENHA_WEBSERVICE).toString();
	}
	
}