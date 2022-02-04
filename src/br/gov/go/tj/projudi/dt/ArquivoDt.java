package br.gov.go.tj.projudi.dt;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.Certificado.Signer;

public class ArquivoDt extends ArquivoDtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4304917045129820208L;

	public static final int CodigoPermissao = -1000;
	
	private static final int KBYTE = 1024;
	
	public static final int ARQUIVO_BLOQUEADO = 1;

	/**
	 * Foi parado de utilizar no projudi novo Agora estou utilizando-o como
	 * variavel auxiliar para insercao de arquivos nao assinados
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 25/09/2008 09:47
	 */
	private boolean assinado;
	private boolean bloqueado;
	private String hash;
	private boolean scan;
	private boolean boRecibo;
	private int volume; // campo transiente	

	private byte[] bytes;
	
	private boolean GerarAssinatura;
	private boolean SalvarSenha;
	private String SenhaCertificado;
	

	// Variável para armazenar o conteudo de um arquivo sem recibo
	//private byte[] conteudoSemRecibo;

	private String NomeObjetoStorage;

	//private boolean boNaoSalvarConteudoEsperarRecibo=false;
	
	// NOTE(jvosantos): Método criado pois o copiar do ArquivoDtGen não copia todas as informações. 
	//					Este método deveria ser um override do copiar do ArquivoDtGen. 
	public void copiarPJD(ArquivoDt arquivo) {
		copiar(arquivo);
		
		assinado = arquivo.isAssinado();
		bloqueado = arquivo.isBloqueado();
		hash = arquivo.getHash();
		scan = arquivo.isScan();
		boRecibo = arquivo.isRecibo();
		volume = arquivo.getVolume();
		bytes = arquivo.conteudoBytes();
		GerarAssinatura = arquivo.isGerarAssinatura();
		SalvarSenha = arquivo.isSalvarSenha();
		SenhaCertificado = arquivo.getSenhaCertificado();
	}

	public void setGerarAssinatura(boolean gerarAssinatura) {
		GerarAssinatura = gerarAssinatura;
	}


	public boolean isRecibo() {
		return boRecibo;
	}

	public void setRecibo(String valor) {
		super.setRecibo(valor);
		if (valor.equalsIgnoreCase("true")) boRecibo = true;
		else boRecibo = false;
	}

	public void limpar() {
		super.limpar();
		this.setAssinado(false);
		this.setScan(false);
		this.setHash("");
		this.volume = 1;
	}

	public boolean isAssinado() {
		return assinado;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setAssinado(String assinado) {
		this.assinado =Funcoes.StringToBoolean(assinado);		 
	}
	
	public void setAssinado(boolean assinado) {
		this.assinado = assinado;
	}

	public boolean isScan() {
		return scan;
	}

	public void setScan(boolean scan) {
		this.scan = scan;
	}

	/**
	 * Retorna se o arquivo e online
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 22/09/2008 16:09
	 * @return boolean
	 */
	public boolean isOnline() {
		return this.getContentType().equals("text/html");
	}

	/**
	 * Sobre carrega o metodo de modificacao do valor do arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 24/09/2008 09:07
	 */
	public void setArquivo(String valor) {
		super.setArquivo(valor);

		if (valor != null) this.bytes = valor.getBytes();
	}

	/**
	 * Encapsulamento para o conteudo em bytes
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 19/09/2008 09:50
	 * @param byte[]
	 *            bytes, arquivo em bytes
	 */
	public void setArquivo(byte[] bytes) {
        if (bytes != null)
            super.setArquivo(new String(bytes)); // Para modificar o conteudo
		// do arquivo quando este
		// nao e assinado
		this.bytes = bytes;
	}

	/**
	 * @author msapaula
	 */
	public String getArquivo() {
		if (this.bytes != null) return new String(this.bytes);
		else return null;
	}

	/**
	 * Se e arquivo assinado
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 15/09/2008 09:53
	 * @return boolean
	 */
	public boolean isArquivoAssinado() {
		return this.getUsuarioAssinador() != null && !this.getUsuarioAssinador().trim().equals("") || this.isAssinado();
	}

	/**
	 * Retorna o nome do arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/09/2008 14:04
	 * @return String
	 * @throws Exception 
	 */
	public String getNomeArquivoFormatado() throws Exception {
//        // Verifica se e um arquivo fisico
//        // if (this.isArquivoFisico())
//        int pos = this.getNomeArquivo().lastIndexOf(".");
//
//        // Se nao encontrou o ultimo ponto ou se o arquivo nao e assinado
//        if (pos == -1 || !this.isArquivoAssinado())
//            return this.getNomeArquivo();
//
//        // Retira segunda extensao .p7s
//        return this.getNomeArquivo().substring(0, this.getNomeArquivo().lastIndexOf("."));
		return Funcoes.nomeArquivoFormatado(this.getNomeArquivo(), this.isArquivoAssinado());
	}

	/**
	 * Verifica se o arquivo em questao e um arquivo fisico ou um arquivo
	 * gravado em banco de dados
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/09/2008 13:55
	 * @return boolean
	 */
	public boolean isArquivoFisico() {
		// Verifica se o caminho do arquivo esta preenchido
		return (this.getCaminho() != null && this.getCaminho().length() > 0);
	}

	/**
	 * Retorna o conteudo em bytes
	 * 
	 * @author Ronneesley Moura Teles
	 * @sicne 12/09/2008 15:32
	 * @return byte[]
	 */
	public byte[] conteudoBytes() {
//		if (boNaoSalvarConteudoEsperarRecibo) {
//			return null;
//		} 
		
		return this.bytes; // this.getArquivo().getBytes();
		// //this.getConteudo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// Pode retornar erro
		if( this.getId() != null && !this.getId().equals(""))
			result = prime * result + Funcoes.StringToInt(this.getId());
		else
			result = prime;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ArquivoDt other = (ArquivoDt) obj;
		if (!this.getId().equals(other.getId())) return false;
		return true;
	}

	public String getUsuarioAssinadorFormatado() {

		StringTokenizer st = new StringTokenizer(getUsuarioAssinador(), ",");
		StringBuffer assinador = new StringBuffer("");
		boolean coAssinado = false;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.startsWith("CN=")) {
				if (coAssinado) assinador.append("; ");
				assinador.append(token.substring(3));
				coAssinado = true;
			}
		}
		return assinador.toString();
		// return Funcoes.RetirarAssinantes(getUsuarioAssinador());
	}

	public String getPropriedades() {
		return "[Id_Arquivo:" + this.getId() + ";NomeArquivo:" + this.getNomeArquivo() + ";Id_ArquivoTipo:" + this.getId_ArquivoTipo() + ";ArquivoTipo:" + this.getArquivoTipo() + ";ContentType:" + this.getContentType() + ";Caminho:" + this.getCaminho() + ";DataInsercao:" + this.getDataInsercao() + ";UsuarioAssinador:" + this.getUsuarioAssinador() + ";CodigoTemp:" + this.getCodigoTemp() + ";ArquivoTipoCodigo:" + this.getArquivoTipoCodigo() + "]";
	}

//	public byte[] getConteudoSemRecibo() {
//		return conteudoSemRecibo;
//	}

//	public void setConteudoSemRecibo(byte[] conteudoSemRecibo) {
//		this.bytes = conteudoSemRecibo;
//		boRecibo=false;
//		boNaoSalvarConteudoEsperarRecibo=true;
//	}

	public byte[] getConteudo() throws Exception{
		byte[] byTemp = null;
		if (boRecibo) {
			byTemp = Signer.extrairConteudoP7sRecibo(conteudoBytes());
		} else {
			byTemp = Signer.extrairConteudoP7s(conteudoBytes());
		}
		return byTemp;
	}
	
	public byte[] getConteudoSemAssinar() throws Exception{
		return conteudoBytes();
	}
	
	public boolean isArquivoHtml()
	{
		if(this.getContentType() == null) return false;
		return (this.getContentType().equalsIgnoreCase("text/html"));
	}
	
	public boolean isArquivoPodeSerHtml(){
		if(this.getContentType() == null) return false;
		return (this.getContentType().toUpperCase().contains("HTML"));
	}
	
	public boolean isArquivoPDF()
	{
		if(this.getContentType() == null) return false;
		return (this.getContentType().equalsIgnoreCase("application/pdf"));
	}
	
	public boolean isArquivoPodeSerPDF(){
		if(this.getContentType() == null) return false;
		return (this.getContentType().toUpperCase().contains("PDF"));
	}
	
	public boolean isImagemJPEG()
	{
		if(this.getContentType() == null) return false;
		return (this.getContentType().equalsIgnoreCase("image/jpeg"));
	}

	public boolean isConteudo() {
		if (this.bytes!=null && this.bytes.length>0) return true;
		return false;
	}

	public String getJSON_ASSINATURA(String indice) {
		StringBuilder stTemp= new StringBuilder();
		stTemp.append("{\"eleid\":\"").append(indice).append("\",\"id\":\"").append(this.getId()).append("\",\"arquivo_tipo\":\"").append(getArquivoTipo()).append("\",\"arquivo_nome\":\"").append(this.getNomeArquivo()).append("\"}");
		return stTemp.toString();
	}
	
	public boolean isArquivoConfiguracao()
	{
		if(this.getArquivoTipoCodigo() != null && this.getArquivoTipoCodigo().equals(String.valueOf(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE))) return true;
		else return false;
	}
	
	public long getTamanhoEmKbytes(){		 
		return ValidacaoUtil.isNaoVazio(this.getArquivo()) ? Long.valueOf(this.getArquivo()) / KBYTE : 0;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}

	public boolean isGerarAssinatura() {
		// TODO Auto-generated method stub
		return GerarAssinatura;
	}


	public String getSenhaCertificado() {
		return SenhaCertificado;
	}


	public void setSenhaCertificado(String senhaCertificado) {
		SenhaCertificado = senhaCertificado;
	}


	public void addUsuarioAssinador(String assinante) {
		if (UsuarioAssinador==null || UsuarioAssinador.isEmpty()) {
			UsuarioAssinador=assinante;
		}else {
			UsuarioAssinador+=", " + assinante;
		}		
		
	}

	public boolean temSenhaCertificado() {
		// TODO Auto-generated method stub
		return SenhaCertificado!=null && !SenhaCertificado.isEmpty();
	}

	public boolean temContentType() {
		return getContentType()!=null && !getContentType().isEmpty();
	}
	
	public boolean isSalvarSenha() {
		return SalvarSenha;
	}


	public void setSalvarSenha(boolean salvarSenha) {
		SalvarSenha = salvarSenha;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}
	
	public void setBloqueado(String bloqueado) {
		this.bloqueado = "1".equals(bloqueado);	 
	}	
	
	public boolean isECarta(){
		return ValidacaoUtil.isVazio(getUsuarioAssinador());
	}
	
	public byte[] obterConteudoECarta() throws Exception {
		if (isRecibo()) {
			return Signer.extrairConteudoReciboArquivoNaoAssinado(new ByteArrayInputStream(conteudoBytes()));
		} else {
			return conteudoBytes();
		}		
	}
	
	public boolean isMidiaDigitalUpload() {
		return (ArquivoTipoDt.isMidiaDigitalUpload(this.getId_ArquivoTipo()) &&
                Funcoes.isNotStringVazia(this.getCaminho()) && 
                this.getCaminho().indexOf('[') >= 0 && 
                this.getCaminho().indexOf("]") >= 0);
	}
	
	
	public boolean isArquivoObjectStorageProjudi() {
		if (!ArquivoTipoDt.isMidiaDigitalUpload(this.getId_ArquivoTipo()) &&
             Funcoes.isNotStringVazia(this.getCaminho()) && 
             this.getCaminho().indexOf('[') >= 0 && 
             this.getCaminho().indexOf("]") >= 0) {
		
			ProjudiPropriedades projudiPropriedades=ProjudiPropriedades.getInstance(); 
			int indiceInicioBucket = this.getCaminho().indexOf("[");
			int indiceFinalBucket = this.getCaminho().indexOf("]");
			
			if (indiceInicioBucket < 0 || indiceFinalBucket < 0 || indiceFinalBucket <= indiceInicioBucket) 
				return false;
			
			String bucketFile = this.getCaminho().substring(indiceInicioBucket + 1, indiceFinalBucket);
			
			if (bucketFile == null || bucketFile.trim().length() == 0) return false;
			
			bucketFile = bucketFile.trim();
									
			String bucketBd = projudiPropriedades.getObjectStorageBucketProjudi();
			
			if (bucketBd == null || bucketBd.trim().length() == 0) return false;
			
			bucketBd = bucketBd.trim();
			
			return bucketFile.equalsIgnoreCase(bucketBd);
		}
		return false;
	}
	
	public void limparConteudoArquivo() {
		this.bytes = null;
		super.setArquivo("");
	}
	
	public String getNomeObjetosStorage() {				
		String temID="@@ID@@";
		if (getId()!=null && !getId().isEmpty()) {
			temID=getId();
		}
		if (NomeObjetoStorage==null || NomeObjetoStorage.isEmpty()) {
			SimpleDateFormat FormatoData = new SimpleDateFormat("yyyyMMdd/HHmm");
			NomeObjetoStorage = String.format("%s/%s",	FormatoData.format(new Date()),  "id_" + temID + "_" +  getNomeArquivo());
		}
		
		return NomeObjetoStorage;
	}

	public boolean isSalvarCeph() {
		ProjudiPropriedades projudiPropriedades=ProjudiPropriedades.getInstance(); 
		return isArquivoAssinado() && isConteudo() && projudiPropriedades.isObjectStorageProjudiHabilitado();	
		
	}

	public String getCaminhoCeph() {
		String bucketBd = ProjudiPropriedades.getInstance().getObjectStorageBucketProjudi();
		
		return String.format("[%s]%s", bucketBd, getNomeObjetosStorage());
	}

//	public void setRecibo(byte[] bytes) {
//        setArquivo(bytes);
//		boRecibo = true;
//		boNaoSalvarConteudoEsperarRecibo = false;
//	}

	public boolean isTemId() {
		return getId()!=null && !getId().isEmpty();
	}

//	public void setNaoSalvarConteudo() {
//		boNaoSalvarConteudoEsperarRecibo =true;		
//	}

//	public byte[] getConteudoBytesGerarRecibo() {
//		return this.bytes;
//	}
	
	public void setId(String valor ) {
		if(valor!=null) {
			Id_Arquivo = valor;
			if (isSalvarCeph() && !valor.isEmpty()) {
				Caminho = getCaminhoCeph();
				Caminho = Caminho.replace("@@ID@@",valor);
				NomeObjetoStorage = NomeObjetoStorage.replace("@@ID@@",valor);
			}
		}
	
	}
}
