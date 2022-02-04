package br.gov.go.tj.projudi.dt;

public class ImportarRetornoCadinDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6905584341596309146L;
	
	public static final int CodigoPermissao = 924;
	
	private String idImportarRetornoCadin;
	private String nomeArquivo;
	private String conteudoArquivo;
	private String conteudoArquivoRetorno;
	
	@Override
	public void setId(String id) {
		this.idImportarRetornoCadin = id;		
	}

	@Override
	public String getId() {
		return this.idImportarRetornoCadin;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		if (nomeArquivo != null) {
			this.nomeArquivo = nomeArquivo;	
		}		
	}
	
	public String getConteudoArquivo() {
		return conteudoArquivo;
	}

	public void setConteudoArquivo(String conteudoArquivo) {
		if (conteudoArquivo != null) {
			this.conteudoArquivo = conteudoArquivo;	
		}		
	}
	
	public String getConteudoArquivoRetorno() {
		return conteudoArquivoRetorno;
	}

	public void setConteudoArquivoRetorno(String conteudoArquivoRetorno) {
		if (conteudoArquivoRetorno != null) {
			this.conteudoArquivoRetorno = conteudoArquivoRetorno;	
		}		
	}
	
	public ImportarRetornoCadinDt() {
		limpar();
	}

	public void copiar(ImportarRetornoCadinDt objeto){
		idImportarRetornoCadin = objeto.getId();
		nomeArquivo = objeto.getNomeArquivo();
		conteudoArquivo = objeto.getConteudoArquivo();		
	}

	public void limpar(){
		idImportarRetornoCadin="";
		nomeArquivo="";
		conteudoArquivo="";
	}

	public String getPropriedades(){
		return "[IdImportarRetornoCadin:" + idImportarRetornoCadin + ";NomeArquivonomeArquivo:" + nomeArquivo + ";ConteudoArquivo:" + conteudoArquivo + "]";		
	}	
} 
