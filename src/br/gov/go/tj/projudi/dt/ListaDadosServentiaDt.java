package br.gov.go.tj.projudi.dt;

/**
 * Lista de Dados da Serventia que serão exibidos na página inicial.
 * 
 * Inicialmente contempla uma descrição, quantidade e link para que a página inicial seja montada.
 * 
 * @author Ronneesley Moura Teles
 * @author msapaula
 */
public class ListaDadosServentiaDt extends Dados {

	private static final long serialVersionUID = -8005419285763153564L;

	private String descricao;
	private long quantidade;
	private String link;
	private long codigo;
	
	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public ListaDadosServentiaDt() {
		limpar();
	}
	
	public void limpar() {
		descricao = "";
		quantidade = 0;
		link = "";
		codigo = -1;
	}
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
	public String getLink() {return link;}
	public void setLink(String link) {this.link = link;}
	public long getQuantidade() {return quantidade;}
	public void setQuantidade(long quantidade) {this.quantidade = quantidade;}
	public String getId() {return null;}
	public void setId(String id) {}
	public boolean isSigiloso(){
		if (codigo==ProcessoStatusDt.SIGILOSO){
			return true;
		}
		return false;
	}
}