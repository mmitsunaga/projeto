package br.gov.go.tj.projudi.dt.relatorios;

import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.UsuarioDt;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioSumarioAudienciasComarcaDiaDt extends RelatorioSumarioDt {


	private static final long serialVersionUID = 9011064767181270170L;

	public static final int CodigoPermissaoSumarioAudiencia = 904;
	
	private String data;
	
	
	
	private Long qtdDesignadas;
	private Long qtdRealizadas;
	private Long qtdAcordos;
	private Long valorAcordos;
	private String servSubtipo;
	
	// variáveis das telas

	private String idComarca;
	private String comarca;
	private UsuarioDt usuario;


	// variáveis dos relatórios
	private String usuarioRelatorio;
	
	private HashMap<String, RelatorioSumarioAudienciasComarcaDiaServDt> listaServentiasComarca;
	
	
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioSumarioAudienciasComarcaDiaDt() {
		limpar();
	}


	public void limpar() {
		idComarca = "";
		comarca = "";
		usuario = new UsuarioDt();
		
	}

	/**
	 * Método que limpa os campos de consulta da tela, mas não limpa o Tipo de
	 * Relatório que está sendo gerado.
	 */
	public void limparCamposConsulta() {
		idComarca = "";
		comarca = "";
		usuario = new UsuarioDt();
	
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getIdComarca() {
		return idComarca;
	}


	public void setIdComarca(String idComarca) {
		this.idComarca = idComarca;
	}


	public String getComarca() {
		return comarca;
	}


	public void setComarca(String comarca) {
		this.comarca = comarca;
	}


	public UsuarioDt getUsuario() {
		return usuario;
	}

	public String getServSubtipo() {
		return servSubtipo;
	}


	public void setServSubtipo(String servSubtipo) {
		this.servSubtipo = servSubtipo;
	}


	public Long getQtdDesignadas() {
		return qtdDesignadas;
	}


	public void setQtdDesignadas(Long qtdDesignadas) {
		this.qtdDesignadas = qtdDesignadas;
	}


	public Long getQtdRealizadas() {
		return qtdRealizadas;
	}


	public void setQtdRealizadas(Long qtdRealizadas) {
		this.qtdRealizadas = qtdRealizadas;
	}


	public Long getQtdAcordos() {
		return qtdAcordos;
	}


	public void setQtdAcordos(Long qtdAcordos) {
		this.qtdAcordos = qtdAcordos;
	}


	public Long getValorAcordos() {
		return valorAcordos;
	}


	public void setValorAcordos(Long valorAcordos) {
		this.valorAcordos = valorAcordos;
	}


	public void setUsuario(UsuarioDt usuario) {
		this.usuario = usuario;
	}


	public String getUsuarioRelatorio() {
		return usuarioRelatorio;
	}


	public void setUsuarioRelatorio(String usuarioRelatorio) {
		this.usuarioRelatorio = usuarioRelatorio;
	}


	public HashMap<String, RelatorioSumarioAudienciasComarcaDiaServDt> getListaServentiasComarca() {
		return listaServentiasComarca;
	}


	public void setListaServentiasComarca(
			HashMap<String, RelatorioSumarioAudienciasComarcaDiaServDt> listaServentiasComarca) {
		this.listaServentiasComarca = listaServentiasComarca;
	}


	public RelatorioSumarioAudienciasComarcaDiaServDt getServentiaListaServentiasComarca( String serventia ) {
		
		if( this.listaServentiasComarca != null)
			return (RelatorioSumarioAudienciasComarcaDiaServDt) this.listaServentiasComarca.get(serventia);
		else
			return null;
		
	}
	
	public void putServentiaListaServentiasComarca(RelatorioSumarioAudienciasComarcaDiaServDt serventia){
		
		if(serventia != null) {
			
			if( this.listaServentiasComarca != null) {
				this.listaServentiasComarca.put(serventia.getServentia(), serventia);
			}
			else {
				this.listaServentiasComarca = new HashMap<String, RelatorioSumarioAudienciasComarcaDiaServDt>();
				this.listaServentiasComarca.put(serventia.getServentia(), serventia);
			}
				
			
		}
			
	}
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}