package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class PoloSPGDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8457238139281967969L;
	
	private String isnParte;
	private String nome;
	private String filiacao;
	private String cpf;
	private TJDataHora dataDeNascimento;	
	
	public int getIdade() throws MensagemException
	{	
		return Funcoes.calculeIdade(dataDeNascimento.getDataFormatadaddMMyyyy());
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		if (nome == null) return "";
		return nome;
	}

	public void setFiliacao(String filiacao) {
		this.filiacao = filiacao;
	}

	public String getFiliacao() {
		if (filiacao == null) return "";
		return filiacao;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCpf() {
		if (cpf == null) return "";
		return cpf;
	}
	
	public String getCpfFormatado() {
		if (cpf == null || cpf.trim().length() == 0) return "";
		
		if (cpf.trim().length() <= 11) return Funcoes.formataCPF(this.cpf.trim());
		
		return Funcoes.formataCNPJ(this.cpf.trim());
	}

	public void setDataDeNascimento(TJDataHora dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	public TJDataHora getDataDeNascimento() {		
		return dataDeNascimento;
	}
	
	public boolean PossuiDataDeNascimento()
	{		
		return this.dataDeNascimento != null;
	}

	@Override
	public void setId(String id) {
		if (id != null) isnParte = id;		
	}

	@Override
	public String getId() {
		return isnParte;
	}
}
