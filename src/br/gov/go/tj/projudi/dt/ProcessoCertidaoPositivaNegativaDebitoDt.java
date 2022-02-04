package br.gov.go.tj.projudi.dt;

public class ProcessoCertidaoPositivaNegativaDebitoDt extends
		ProcessoCertidaoPositivaNegativaDt {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1074732017786148482L;
	private String parteAverbacaoNome;
	private String parteAverbacaoSexo;
	private String parteAverbacaoCPF;
	private String parteAverbacaoCNPJ;
	private String parteAverbcaoNomeMae;
	private String parteAverbacaoDataNascimento;

	public ProcessoCertidaoPositivaNegativaDebitoDt() {
		super();
		this.parteAverbacaoNome = "";
		this.parteAverbacaoSexo = "";
		this.parteAverbacaoCPF = "";
		this.parteAverbacaoCNPJ= "";
		this.parteAverbcaoNomeMae= "";
		this.parteAverbacaoDataNascimento= "";
	}

	public String getParteAverbacaoNome() {
		return parteAverbacaoNome;
	}

	public void setParteAverbacaoNome(String parteAverbacaoNome) {
		this.parteAverbacaoNome = parteAverbacaoNome;
	}

	public String getParteAverbacaoSexo() {
		return parteAverbacaoSexo;
	}

	public void setParteAverbacaoSexo(String parteAverbacaoSexo) {
		this.parteAverbacaoSexo = parteAverbacaoSexo;
	}

	public String getParteAverbacaoCPF() {
		return parteAverbacaoCPF;
	}

	public void setParteAverbacaoCPF(String parteAverbacaoCPF) {
		this.parteAverbacaoCPF = parteAverbacaoCPF;
	}

	public String getParteAverbacaoCNPJ() {
		return parteAverbacaoCNPJ;
	}

	public void setParteAverbacaoCNPJ(String parteAverbacaoCNPJ) {
		this.parteAverbacaoCNPJ = parteAverbacaoCNPJ;
	}

	public String getParteAverbcaoNomeMae() {
		return parteAverbcaoNomeMae;
	}

	public void setParteAverbcaoNomeMae(String parteAverbcaoNomeMae) {
		this.parteAverbcaoNomeMae = parteAverbcaoNomeMae;
	}

	public String getParteAverbacaoDataNascimento() {
		return parteAverbacaoDataNascimento;
	}

	public void setParteAverbacaoDataNascimento(String parteAverbacaoDataNascimento) {
		this.parteAverbacaoDataNascimento = parteAverbacaoDataNascimento;
	}
	


}
