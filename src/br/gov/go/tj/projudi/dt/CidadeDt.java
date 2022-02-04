package br.gov.go.tj.projudi.dt;

/**
 * Classe que implementa o objeto Cidade
 */
public class CidadeDt extends CidadeDtGen {

	private static final long serialVersionUID = 1506654911387703876L;

	public static final int CodigoPermissao = 214;
	
	public static final int CODIGO_CIDADE_GOIANIA = 520094;

	private String cidadeCompleta;

	/**
	 * Obt�m todos os atributos de Cidade
	 * 
	 * @return um <code>String</code> com todos os atributos de Cidade
	 */
	public String getCidadeCompleta() {

		return cidadeCompleta;

	} // fim do m�todo getCidadeCompleta

	/**
	 * Define o valor de CidadeCompleta
	 * 
	 * @param cidadeCompleta
	 *            todos os atributos de Cidade
	 */
	public void setCidadeCompleta(String cidadeCompleta) {

		this.cidadeCompleta = cidadeCompleta;

	} // fim do m�todo setCidadeCompleta

} // fim da classe CidadeDt
