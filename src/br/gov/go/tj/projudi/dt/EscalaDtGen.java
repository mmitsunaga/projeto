package br.gov.go.tj.projudi.dt;

import java.util.List;

public class EscalaDtGen extends Dados{

    private static final long serialVersionUID = 8905773061241571508L;
    private String Id_Escala;
	private String Escala;

	private String Id_Serventia;
	private String Serventia;
	private String Id_EscalaTipo;
	private String EscalaTipo;
	private String Id_MandadoTipo;
	private String MandadoTipo;
	private String Id_Zona;
	private String Zona;
	private String Id_Regiao;
	private String Regiao;
	private String Id_Bairro;
	private String Bairro;
	private String QuantidadeMandado;
	private String ativo;
	private String comarca;
	
	private String ServentiaCodigo;
	private String EscalaTipoCodigo;
	private String MandadoTipoCodigo;
	private String ZonaCodigo;
	private String RegiaoCodigo;
	private String BairroCodigo;
	private String TipoEspecial;
	
	private List listaServentiaCargo;

	public EscalaDtGen() {
		limpar();
	}

	public String getId()  {return Id_Escala;}
	public void setId(String valor ) {if(valor!=null) Id_Escala = valor;}
	public String getEscala()  {return Escala;}
	public void setEscala(String valor ) {if (valor!=null) Escala = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_EscalaTipo()  {return Id_EscalaTipo;}
	public void setId_EscalaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EscalaTipo = ""; EscalaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_EscalaTipo = valor;}
	public String getEscalaTipo()  {return EscalaTipo;}
	public void setEscalaTipo(String valor ) {if (valor!=null) EscalaTipo = valor;}
	public String getId_MandadoTipo()  {return Id_MandadoTipo;}
	public void setId_MandadoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MandadoTipo = ""; MandadoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_MandadoTipo = valor;}
	public String getMandadoTipo()  {return MandadoTipo;}
	public void setMandadoTipo(String valor ) {if (valor!=null) MandadoTipo = valor;}
	public String getId_Zona()  {return Id_Zona;}
	public void setId_Zona(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Zona = ""; Zona = "";}else if (!valor.equalsIgnoreCase("")) Id_Zona = valor;}
	public String getZona()  {return Zona;}
	public void setZona(String valor ) {if (valor!=null) Zona = valor;}
	public String getId_Regiao()  {return Id_Regiao;}
	public void setId_Regiao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Regiao = ""; Regiao = "";}else if (!valor.equalsIgnoreCase("")) Id_Regiao = valor;}
	public String getRegiao()  {return Regiao;}
	public void setRegiao(String valor ) {if (valor!=null) Regiao = valor;}
	public String getId_Bairro()  {return Id_Bairro;}
	public void setId_Bairro(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Bairro = ""; Bairro = "";}else if (!valor.equalsIgnoreCase("")) Id_Bairro = valor;}
	public String getBairro()  {return Bairro;}
	public void setBairro(String valor ) {if (valor!=null) Bairro = valor;}
	public String getQuantidadeMandado()  {return QuantidadeMandado;}
	public void setQuantidadeMandado(String valor ) {if (valor!=null) QuantidadeMandado = valor;}
	public String getAtivo() {return ativo;}
	public void setAtivo(String ativo) {if (ativo!=null) this.ativo = ativo;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	public String getEscalaTipoCodigo()  {return EscalaTipoCodigo;}
	public void setEscalaTipoCodigo(String valor ) {if (valor!=null) EscalaTipoCodigo = valor;}
	public String getMandadoTipoCodigo()  {return MandadoTipoCodigo;}
	public void setMandadoTipoCodigo(String valor ) {if (valor!=null) MandadoTipoCodigo = valor;}
	public String getZonaCodigo()  {return ZonaCodigo;}
	public void setZonaCodigo(String valor ) {if (valor!=null) ZonaCodigo = valor;}
	public String getRegiaoCodigo()  {return RegiaoCodigo;}
	public void setRegiaoCodigo(String valor ) {if (valor!=null) RegiaoCodigo = valor;}
	public String getBairroCodigo()  {return BairroCodigo;}
	public void setBairroCodigo(String valor ) {if (valor!=null) BairroCodigo = valor;}
	public String getTipoEspecial()  {return TipoEspecial;}
	public void setTipoEspecial(String valor ) {if (valor!=null) TipoEspecial = valor;}
	
	
	
	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		if (comarca!=null)
			this.comarca = comarca;
	}

	public void copiar(EscalaDt objeto){
		Id_Escala = objeto.getId();
		Escala = objeto.getEscala();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_EscalaTipo = objeto.getId_EscalaTipo();
		EscalaTipo = objeto.getEscalaTipo();
		Id_MandadoTipo = objeto.getId_MandadoTipo();
		MandadoTipo = objeto.getMandadoTipo();
		Id_Zona = objeto.getId_Zona();
		Zona = objeto.getZona();
		Id_Regiao = objeto.getId_Regiao();
		Regiao = objeto.getRegiao();
		Id_Bairro = objeto.getId_Bairro();
		Bairro = objeto.getBairro();
		QuantidadeMandado = objeto.getQuantidadeMandado();
		CodigoTemp = objeto.getCodigoTemp();
		ServentiaCodigo = objeto.getServentiaCodigo();
		EscalaTipoCodigo = objeto.getEscalaTipoCodigo();
		MandadoTipoCodigo = objeto.getMandadoTipoCodigo();
		ZonaCodigo = objeto.getZonaCodigo();
		RegiaoCodigo = objeto.getRegiaoCodigo();
		BairroCodigo = objeto.getBairroCodigo();
		ativo = objeto.getAtivo();
		comarca = objeto.getComarca();
		TipoEspecial = objeto.getTipoEspecial();
	}

	public void limpar(){
		Id_Escala="";
		Escala="";
		Id_Serventia="";
		Serventia="";
		Id_EscalaTipo="";
		EscalaTipo="";
		Id_MandadoTipo="";
		MandadoTipo="";
		Id_Zona="";
		Zona="";
		Id_Regiao="";
		Regiao="";
		Id_Bairro="";
		Bairro="";
		QuantidadeMandado="";
		CodigoTemp="";
		ServentiaCodigo="";
		EscalaTipoCodigo="";
		MandadoTipoCodigo="";
		ZonaCodigo="";
		RegiaoCodigo="";
		BairroCodigo="";
		ativo="";
		listaServentiaCargo = null;
		comarca = "";
		TipoEspecial = "";
	}
	
	public List getListaServentiaCargo() {
		return listaServentiaCargo;
	}

	public void setListaServentiaCargo(List lista) {
		if (lista != null) this.listaServentiaCargo = lista;
	}

	public String getPropriedades(){
		return "[Id_Escala:" + Id_Escala + ";Escala:" + Escala + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_EscalaTipo:" + Id_EscalaTipo + ";EscalaTipo:" + EscalaTipo + ";Id_MandadoTipo:" + Id_MandadoTipo + ";MandadoTipo:" + MandadoTipo + ";Id_Zona:" + Id_Zona + ";Zona:" + Zona + ";Id_Regiao:" + Id_Regiao + ";Regiao:" + Regiao + ";Id_Bairro:" + Id_Bairro + ";Bairro:" + Bairro + ";QuantidadeMandado:" + QuantidadeMandado + ";CodigoTemp:" + CodigoTemp + ";ServentiaCodigo:" + ServentiaCodigo + ";EscalaTipoCodigo:" + EscalaTipoCodigo + ";MandadoTipoCodigo:" + MandadoTipoCodigo + ";ZonaCodigo:" + ZonaCodigo + ";RegiaoCodigo:" + RegiaoCodigo + ";BairroCodigo:" + BairroCodigo + ";TipoEspecial:" + TipoEspecial + ";Ativo:"+ ativo +"]";
	}
} 
