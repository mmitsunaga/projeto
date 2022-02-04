package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class UsuarioServentiaGrupoDt extends UsuarioServentiaGrupoDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 2237637561457527208L;

    public static final int CodigoPermissao = 158;

	// Variáveis para controlar possíveis status de UsuarioServentiaGrupo
	public static final int INATIVO = 0;
	public static final int ATIVO = 1;

	// Variáveis auxiliares na habilitação de usuários
	private String Id_Usuario;
	private String Usuario;
	private String ServentiaTipoCodigo;

	private List listaServentiasGrupos;

	private String hash; //Atributo para validação

	public void limpar() {
		Id_Usuario = "";
		Usuario = "";
		ServentiaTipoCodigo = "";
		listaServentiasGrupos = new ArrayList();
		super.limpar();
	}

	public String getId_Usuario() {
		return Id_Usuario;
	}

	public void setId_Usuario(String valor) {
		if (valor != null) if (valor.equalsIgnoreCase("null")) {
			Id_Usuario = "";
			Usuario = "";
		} else if (!valor.equalsIgnoreCase("")) Id_Usuario = valor;
	}

	public String getUsuario() {
		return Usuario;
	}

	public void setUsuario(String valor) {
		if (valor != null) Usuario = valor;
	}

	public String getServentiaTipoCodigo() {
		return ServentiaTipoCodigo;
	}

	public void setServentiaTipoCodigo(String valor) {
		if (valor != null) ServentiaTipoCodigo = valor;
	}

	public List getListaServentiasGrupos() {
		return listaServentiasGrupos;
	}

	public void setListaServentiasGrupos(List listaServentiasGrupos) {
		this.listaServentiasGrupos = listaServentiasGrupos;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
