package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class UsuarioServentiaAfastamentoDt extends UsuarioServentiaAfastamentoDtGen {

	private static final long serialVersionUID = -3951184249223396067L;
	public static final int CodigoPermissao = 191;

	private String cpfUsuario;
	private String acao;
	private String nomeUsuarioCadastrador;
	private String nomeUsuarioFinalizador;

	public UsuarioServentiaAfastamentoDt() {
		super.limpar();
		this.limpar();
	}

	public void limpar() {
		cpfUsuario = "";
		acao = "";
		nomeUsuarioCadastrador = "";
		nomeUsuarioFinalizador = "";
	}

	public String getCpfUsuario() {
		return cpfUsuario;
	}

	public void setCpfUsuario(String cpfUsuario) {
		this.cpfUsuario = cpfUsuario;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getNomeUsuarioCadastrador() {
		return nomeUsuarioCadastrador;
	}

	public void setNomeUsuarioCadastrador(String nomeUsuarioCadastrador) {
		this.nomeUsuarioCadastrador = nomeUsuarioCadastrador;
	}

	public String getNomeUsuarioFinalizador() {
		return nomeUsuarioFinalizador;
	}

	public void setNomeUsuarioFinalizador(String nomeUsuarioFinalizador) {
		if (nomeUsuarioFinalizador == null)
			this.nomeUsuarioFinalizador = "";
		else
			this.nomeUsuarioFinalizador = nomeUsuarioFinalizador;
	}

}
