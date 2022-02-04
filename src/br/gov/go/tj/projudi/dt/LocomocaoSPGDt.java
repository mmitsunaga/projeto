package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class LocomocaoSPGDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5036727728506697502L;

	private long isnGuia;
	private boolean JaVerificada;
	private String CodigoBairro;
	private String CodigoMunicipio;
	private String Quantidade;
	private String IdProjudi;
	private int PosicaoVetor;
	private long NumeroMandado;
	private long NumeroGuiaComplementar;
	
	public long getIsnGuia() {
		return isnGuia;
	}

	public void setIsnGuia(long isnGuia) {
		this.isnGuia = isnGuia;
	}

	public boolean isJaVerificada() {
		return JaVerificada;
	}

	public void setJaVerificada(boolean jaVerificada) {
		JaVerificada = jaVerificada;
	}

	public String getCodigoBairro() {
		return CodigoBairro;
	}

	public void setCodigoBairro(String codigoBairro) {
		CodigoBairro = codigoBairro;
	}

	public String getCodigoMunicipio() {
		return CodigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		CodigoMunicipio = codigoMunicipio;
	}

	public String getQuantidade() {
		return Quantidade;
	}

	public void setQuantidade(String quantidade) {
		Quantidade = quantidade;
	}

	public String getIdProjudi() {
		return IdProjudi;
	}

	public void setIdProjudi(String idProjudi) {
		IdProjudi = idProjudi;
	}

	public int getPosicaoVetor() {
		return PosicaoVetor;
	}

	public void setPosicaoVetor(int posicaoVetor) {
		PosicaoVetor = posicaoVetor;
	}

	public long getNumeroMandado() {
		return NumeroMandado;
	}

	public void setNumeroMandado(long numeroMandado) {
		NumeroMandado = numeroMandado;
	}

	public long getNumeroGuiaComplementar() {
		return NumeroGuiaComplementar;
	}

	public void setNumeroGuiaComplementar(long numeroGuiaComplementar) {
		NumeroGuiaComplementar = numeroGuiaComplementar;
	}
}
