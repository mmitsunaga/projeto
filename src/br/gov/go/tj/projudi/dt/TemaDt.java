package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class TemaDt extends TemaDtGen{

	private static final long serialVersionUID = -3834413818345556503L;
	
	public static final int CodigoPermissao=813;
	
	private List listaAssuntos;
	
	public String getTemaCodigoTitulo()  {
		return this.getTemaCodigo() + " - " + this.getTitulo();
	}
	
	public TemaDt() {
		limpar();
	}

	public List getListaAssuntos() {
		return listaAssuntos;
	}

	public void setListaAssuntos(List listaAssuntos) {
		this.listaAssuntos = listaAssuntos;
	}
	
	public void copiar(TemaDt objeto){
		super.copiar(objeto);
		listaAssuntos = objeto.getListaAssuntos();
	}

	public void limpar(){
		super.limpar();
		listaAssuntos = null;
	}
	
	public void adicionarAssunto(TemaAssuntoDt temaAssunto) {
		if(listaAssuntos != null)
			listaAssuntos.add(temaAssunto);
		else {
			listaAssuntos = new ArrayList();
			listaAssuntos.add(temaAssunto);
		}
	}
	
	public void removerAssunto(String posicaoLista) {
		if(listaAssuntos != null && posicaoLista.length() > 0)
			listaAssuntos.remove(Funcoes.StringToInt(posicaoLista));
	}
	
	public TemaAssuntoDt getItemListaAssuntos(String posicaoLista) {
		TemaAssuntoDt retorno = null;
		if(listaAssuntos != null && posicaoLista.length() > 0)
			retorno = (TemaAssuntoDt) listaAssuntos.get(Funcoes.StringToInt(posicaoLista));		
		return retorno;
	}

}
