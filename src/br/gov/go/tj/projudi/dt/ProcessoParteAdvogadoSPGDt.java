package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

//---------------------------------------------------------
public class ProcessoParteAdvogadoSPGDt implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8528604069324826779L;
	
	private String Id_Processo;
	private String Id_Processo_Parte;
	private String OAB_Numero;
	private String OAB_UF;
	private String NomeCompleto;
	private String NomeSimplificado;
	private String DataImportacao;
	private String OAB_Complemento;
	private String CPF;
	private String Email;
	public String getId_Processo() {
		return Id_Processo;
	}
	public void setId_Processo(String id_Processo) {
		Id_Processo = id_Processo;
	}
	public String getId_Processo_Parte() {
		return Id_Processo_Parte;
	}
	public void setId_Processo_Parte(String id_Processo_Parte) {
		Id_Processo_Parte = id_Processo_Parte;
	}
	public String getOAB_Numero() {
		return OAB_Numero;
	}
	public void setOAB_Numero(String oAB_Numero) {
		OAB_Numero = oAB_Numero;
	}
	public String getOAB_UF() {
		return OAB_UF;
	}
	public void setOAB_UF(String oAB_UF) {
		OAB_UF = oAB_UF;
	}
	public String getNomeCompleto() {
		return NomeCompleto;
	}
	public void setNomeCompleto(String nomeCompleto) {
		NomeCompleto = nomeCompleto;
	}
	public String getNomeSimplificado() {
		return NomeSimplificado;
	}
	public void setNomeSimplificado(String nomeSimplificado) {
		NomeSimplificado = nomeSimplificado;
	}
	public String getDataImportacao() {
		return DataImportacao;
	}
	public void setDataImportacao(String dataImportacao) {
		DataImportacao = dataImportacao;
	}
	public String getOAB_Complemento() {
		return OAB_Complemento;
	}
	public void setOAB_Complemento(String oAB_Complemento) {
		OAB_Complemento = oAB_Complemento;
	}
	public String getCPF() {
		return CPF;
	}
	public void setCPF(String cPF) {
		CPF = cPF;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	
}
