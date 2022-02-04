package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class AudienciaPublicadaDt implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 720159311959527873L;
	
	public static final String IDENTIFICADOR_HASH  = "<<VALOR_HASH>>";
	public static final String IDENTIFICADOR_WEBSITE  = "<<WEBSITE_REPOSITORIO>>";
	public static final String IDENTIFICADOR_MENSAGEM = "<<MENSAGEM>>";
	
	public static final String PROPRIEDADE_PROCESSO  = "ProcessoNumero";
	public static final String PROPRIEDADE_DATA  = "DataAudiencia";
	public static final String PROPRIEDADE_HASH  = "Hash";
	public static final String STATUS_PROCESSO_NAO_ENCONTRADO = "1";	
	public static final String MENSAGEM_VISUALIZACAO = "(Visualizar vídeo)";
	public static final String MENSAGEM_DOWNLOAD = "(Download vídeo)";
		
	public AudienciaPublicadaDt() {	
	}

	private String processoNumero;
	private String dataAudiencia; 

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		if (processoNumero != null) this.processoNumero = processoNumero;
	}

	public String getDataAudiencia() {
		return dataAudiencia;
	}

	public void setDataAudiencia(String dataAudiencia) {
		if (dataAudiencia != null) this.dataAudiencia = dataAudiencia;
	}
	
	public String getPropriedades(){
		return "[ProcessoNumero:" + processoNumero + ";DataAudiencia:" + dataAudiencia + "]";
	}

} 
