package br.gov.go.tj.projudi.dt.relatorios;

import java.util.Date;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para armazenar serventias com projudi implantado
 * 
 * @author asrocha
 * 
 */
public class RelatorioImplantacaoDt extends Dados {

	private static final long serialVersionUID = 295719560779810332L;
	private String serventia;
	private String id_Serventia;
	private String comarca;
	private String id_Comarca;
	private String serventiaTipo;
	private String id_ServentiaTipo;
	private Date data;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioImplantacaoDt() {
		limpar();
	}

	/**
	 * Define os valores de todas as variáveis como nulo
	 */
	public void limpar() {
		id_Serventia = "";
		serventia = "";
		id_Comarca = "";
		comarca = "";
		id_ServentiaTipo = "";
		serventiaTipo = "";
		data = new Date();
	}

	/**
	 * Obtém o nome da serventia
	 * 
	 * @return um <code>String</code> com o nome
	 */
	public String getServentia() {
		return serventia;
	}

	/**
	 * Define o nome da serventia
	 * 
	 * @param serventia
	 *            nome da serventia
	 */
	public void setServentia(String serventia) {
		if (serventia != null) this.serventia = serventia;
	}

	/**
	 * Obtém o código da serventia
	 * 
	 * @return um <code>String</code> com o código
	 */
	public String getId_Serventia() {
		return id_Serventia;
	}

	/**
	 * Define o valor do código da serventia
	 * 
	 * @param id_Serventia
	 *            código da serventia
	 */
	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null) this.id_Serventia = id_Serventia;
	}

	/**
	 * Obtém o nome da comarca
	 * 
	 * @return um <code>String</code> com o nome
	 */
	public String getComarca() {
		return comarca;
	}

	/**
	 * Define o nome da serventia
	 * 
	 * @param serventia
	 *            nome da serventia
	 */
	public void setComarca(String comarca) {
		if (comarca != null) this.comarca = comarca;
	}

	/**
	 * Obtém o nome da comarca
	 * 
	 * @return um <code>String</code> com o nome
	 */
	public String getId_Comarca() {
		return id_Comarca;
	}

	/**
	 * Define o nome da serventia
	 * 
	 * @param serventia
	 *            nome da serventia
	 */
	public void setId_Comarca(String id_Comarca) {
		if (id_Comarca != null) this.id_Comarca = id_Comarca;
	}

	/**
	 * Obtém o tipo da serventia
	 * 
	 * @return
	 */
	public String getServentiaTipo() {
		return serventiaTipo;
	}

	/**
	 * Define o tipo da serventia
	 * 
	 * @param serventiaTipo
	 */
	public void setServentiaTipo(String serventiaTipo) {
		if (serventiaTipo != null) this.serventiaTipo = serventiaTipo;
	}

	/**
	 * Obtém o codigo do tipo da serventia
	 * 
	 * @return
	 */
	public String getId_ServentiaTipo() {
		return id_ServentiaTipo;
	}

	/**
	 * Define o código do tipo da serventia
	 * 
	 * @param idServentiaTipo
	 */
	public void setId_ServentiaTipo(String idServentiaTipo) {
		id_ServentiaTipo = idServentiaTipo;
	}

	/**
	 * Obtém o código do objeto
	 * 
	 * @return o codigo
	 */
	public String getId() {
		return null;
	}

	/**
	 * Define o código do objeto
	 * 
	 * @param id
	 *            código de identificação
	 */
	public void setId(String id) {
	}

	/**
	 * Obtém a data do objeto
	 * 
	 * @return a data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * Define a data do objeto
	 * 
	 * @param data
	 *            data de implantação do projudi
	 */
	public void setData(Date data) {
		this.data = data;
	}
}
