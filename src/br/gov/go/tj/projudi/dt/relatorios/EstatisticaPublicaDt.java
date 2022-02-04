package br.gov.go.tj.projudi.dt.relatorios;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class EstatisticaPublicaDt extends Dados {

	private static final long serialVersionUID = 5406600816297766275L;

	public List listaProcessosGeralComarca;
	public List listaProcessosServentia;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public EstatisticaPublicaDt() {
		limpar();
	}

	public void limpar() {
		listaProcessosGeralComarca = new ArrayList();
		listaProcessosServentia = new ArrayList();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public List getListaProcessosGeralComarca() {
		return listaProcessosGeralComarca;
	}

	public void setListaProcessosGeralComarca(List listaProcessosGeralComarca) {
		this.listaProcessosGeralComarca = listaProcessosGeralComarca;
	}

	public List getListaProcessosServentia() {
		return listaProcessosServentia;
	}

	public void setListaProcessosServentia(List listaProcessosServentia) {
		this.listaProcessosServentia = listaProcessosServentia;
	}

}