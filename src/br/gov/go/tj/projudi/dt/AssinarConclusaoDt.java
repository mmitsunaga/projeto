package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.Collection;

public class AssinarConclusaoDt extends AssinarPendenciaDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5995934858925488799L;	
	
	private String classificador;
	private String id_Classificador;
	
	public AssinarConclusaoDt() {
		super();		
	}
	
	public void limpar() {
		super.limpar();
		classificador = "";
		id_Classificador = "";
	}

	public void copiar(AnaliseConclusaoDt objeto) {
		super.copiar(objeto);
		id_Classificador = objeto.getId_Classificador();
		classificador = objeto.getClassificador();		
	}
	
	public String getClassificador() {
		if (classificador == null) return "";
		return classificador;
	}

	public void setClassificador(String classificador) {
		if (classificador != null) if (classificador.equalsIgnoreCase("null")) this.classificador = "";
		else if (!classificador.equalsIgnoreCase("")) this.classificador = classificador;
	}
	
	public String getId_Classificador() {
		if (id_Classificador == null) return "";
		return this.id_Classificador;
	}

	public void setId_Classificador(String id_Classificador) {
		if (id_Classificador != null) if (id_Classificador.equalsIgnoreCase("null")) {
			this.id_Classificador = "";
			this.classificador = "";
		} else if (!id_Classificador.equalsIgnoreCase("")) this.id_Classificador = id_Classificador;
	}
	
}