package br.gov.go.tj.projudi.dt;

import java.io.IOException;
import java.io.Serializable;

import br.gov.go.tj.utils.ValidacaoUtil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PesquisaAvancadaDt implements Serializable {

	private static final long serialVersionUID = -6169049389562966833L;

	@JsonProperty(value = "ProcessoNumero")
	private String numeroProcesso;
	
	@JsonProperty(value = "Id_Serventia")
	private String id_serventia;
	
	@JsonProperty(value = "Serventia")
	private String serventia;
	
	@JsonProperty(value = "Id_Usuario")
	private String id_realizador;
	
	@JsonProperty(value = "Usuario")
	private String realizador;
	
	@JsonProperty(value = "ArquivoTipo")
	private String tipoArquivo;
	
	@JsonProperty(value = "Id_ArquivoTipo")
	private String id_tipoArquivo;
	
	@JsonProperty(value = "DataInicial")
	private String dataPublicacaoIni;
	
	@JsonProperty(value = "DataFinal")
	private String dataPublicacaoFim;
	
	@JsonProperty(value = "texto")
	private String texto;
	
	public String getId_serventia() {
		return id_serventia;
	}

	public void setId_serventia(String id_serventia) {
		if (ValidacaoUtil.isNaoVazio(id_serventia)){
			this.id_serventia = id_serventia;
		}
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (ValidacaoUtil.isNaoVazio(serventia)){
			this.serventia = serventia;
		}		
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		if (ValidacaoUtil.isNaoVazio(numeroProcesso)){
			this.numeroProcesso = numeroProcesso;
		}		
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		if (ValidacaoUtil.isNaoVazio(tipoArquivo)){
			this.tipoArquivo = tipoArquivo;
		}
	}

	public String getId_tipoArquivo() {
		return id_tipoArquivo;
	}

	public void setId_tipoArquivo(String id_tipoArquivo) {
		if (ValidacaoUtil.isNaoVazio(id_tipoArquivo)){
			this.id_tipoArquivo = id_tipoArquivo;
		}
	}
	
	public String getId_realizador() {
		return id_realizador;
	}

	public void setId_realizador(String id_realizador) {
		if (ValidacaoUtil.isNaoVazio(id_realizador)){
			this.id_realizador = id_realizador;
		}
	}

	public String getRealizador() {
		return realizador;
	}

	public void setRealizador(String realizador) {
		if (ValidacaoUtil.isNaoVazio(realizador)){
			this.realizador = realizador;
		}		
	}

	public String getDataPublicacaoIni() {
		return dataPublicacaoIni;
	}

	public void setDataPublicacaoIni(String dataPublicacaoIni) {
		if (ValidacaoUtil.isNaoVazio(dataPublicacaoIni)){
			this.dataPublicacaoIni = dataPublicacaoIni;
		}		
	}

	public String getDataPublicacaoFim() {
		return dataPublicacaoFim;
	}

	public void setDataPublicacaoFim(String dataPublicacaoFim) {
		if (ValidacaoUtil.isNaoVazio(dataPublicacaoFim)){
			this.dataPublicacaoFim = dataPublicacaoFim;
		}		
	}
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		if (ValidacaoUtil.isNaoVazio(texto)){
			this.texto = texto;
		}		
	}

	public void limpar(){		
		this.numeroProcesso = null;
		this.dataPublicacaoIni = null;
		this.dataPublicacaoFim = null;
		this.id_serventia = null;
		this.serventia = null;
		this.id_realizador = null;
		this.realizador = null;
		this.id_tipoArquivo = null;
		this.tipoArquivo = null;
		this.texto = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toJSON(){
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException ex){
			return "";
		}			
	}
	
	/**
	 * Carrega um objeto do tipo PesquisaAvancadaDt com dados de um json
	 * @param json
	 * @return
	 */
	public static PesquisaAvancadaDt parse(String json){
		if (ValidacaoUtil.isNaoNulo(json)){
			try {
				return new ObjectMapper().readValue(json, PesquisaAvancadaDt.class);
			} catch (IOException e) {			
				return null;
			}
		} else {
			return null;
		}
		
	}
	
	  
}
