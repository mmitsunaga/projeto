package br.gov.go.tj.projudi.dt.relatorios;

public class DetalhesUsuarioDt {

	private String id;

	private String nome;

	private String usuario;

	private String grupo;

	private String doc;

	private String email;

	private String telefone;

	private String situacao;

	public DetalhesUsuarioDt() {
		id = "";
		nome = "";
		usuario = "";
		grupo = "";
		doc = "";
		email = "";
		telefone = "";
		situacao = "";
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome != null) this.nome = nome;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		if (doc != null) this.doc = doc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null) this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		if (telefone != null) this.telefone = telefone;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		if (situacao != null) this.situacao = situacao;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		if (grupo != null) this.grupo = grupo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id != null) this.id = id;
	}

}
