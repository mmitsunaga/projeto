package br.gov.go.tj.projudi.dt;

import java.util.List;

//---------------------------------------------------------
public class UsuarioArquivoDt extends UsuarioArquivoDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = -4452177838959297024L;

    public static final int CodigoPermissao = 115;

	private UsuarioDt usuarioDt;
	private ArquivoDt arquivoDt;
	
	private String hash;
	
	private List arquivosInseridos;

	//Atributos auxiliares na inserção de documentos
	private String Id_ArquivoTipo;
	private String ArquivoTipo;

	public void limpar() {
		Id_ArquivoTipo = "";
		ArquivoTipo = "";
		super.limpar();
	}

	public String getId_ArquivoTipo() {
		return Id_ArquivoTipo;
	}

	public void setId_ArquivoTipo(String id_ArquivoTipo) {
		if (id_ArquivoTipo != null) if (id_ArquivoTipo.equalsIgnoreCase("null")) {
			Id_ArquivoTipo = "";
			ArquivoTipo = "";
		} else if (!id_ArquivoTipo.equalsIgnoreCase("")) Id_ArquivoTipo = id_ArquivoTipo;
	}

	public String getArquivoTipo() {
		return ArquivoTipo;
	}

	public void setArquivoTipo(String arquivoTipo) {
		if (arquivoTipo != null) if (arquivoTipo.equalsIgnoreCase("null")) ArquivoTipo = "";
		else if (!arquivoTipo.equalsIgnoreCase("")) ArquivoTipo = arquivoTipo;
	}

	public UsuarioDt getUsuarioDt() {
		return usuarioDt;
	}

	public void setUsuarioDt(UsuarioDt usuarioDt) {
		this.usuarioDt = usuarioDt;
	}

	public List getArquivosInseridos() {
		return arquivosInseridos;
	}

	public void setArquivosInseridos(List arquivosInseridos) {
		this.arquivosInseridos = arquivosInseridos;
	}

	public ArquivoDt getArquivoDt() {
		return arquivoDt;
	}

	public void setArquivoDt(ArquivoDt arquivoDt) {
		this.arquivoDt = arquivoDt;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
