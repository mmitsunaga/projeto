package br.gov.go.tj.projudi.dt;

import java.util.Date;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class CertificadoDt extends CertificadoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 3910544559787935620L;
    public static final int CodigoPermissao = 217;
    public static final int CodigoPermissaoCertificadoConfiavel = 454;
    private String nomeUsuario;
    private String descricao;
	private String situacao;
	private byte[] conteudo;	
	
	public CertificadoDt() {
		limpar();
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		if (descricao != null)
			this.descricao = descricao;
	}
	
	/**
	 * Método que retorna se o certificado é válido hoje
	 * @return true se o certificado é válido hoje, false caso contrário
	 */
	public boolean ehValidoHoje() {
		Date dataExpiracao = Funcoes.DataHora(this.getDataExpiracao());
		if(dataExpiracao.after(new Date())&& (this.getDataRevogacao().equalsIgnoreCase(""))) 
			return true;			

		return false;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		if (conteudo != null)
			this.conteudo = conteudo;
	}

	
	public void limpar(){
		descricao = "";
		nomeUsuario = "";
		conteudo = null;
		super.limpar();
	}

	
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		if (nomeUsuario != null)
			this.nomeUsuario = nomeUsuario;
	}
	
}
