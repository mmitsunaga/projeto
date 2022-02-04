package br.gov.go.tj.projudi.dt;

public class GuiaFinalidadeModeloCustaDt extends Dados {

	private static final long serialVersionUID = 1771226285299236240L;

	private String Id_GuiaFinalidadeModeloCusta;
	private String GuiaFinalidadeModeloCusta;
	
	private GuiaFinalidadeModeloDt guiaFinalidadeModeloDt;
	private CustaDt custaDt;

	private String Id_GuiaFinalidadeModelo;
	private String GuiaFinalidadeModelo;
	private String Id_Custa;
	private String Custa;
	private String CodigoTemp;
	
	@Override
	public String getId() {
		return Id_GuiaFinalidadeModeloCusta;
	}

	@Override
	public void setId(String id) {
		Id_GuiaFinalidadeModeloCusta = id;;
	}
	
	public String getId_GuiaFinalidadeModeloCusta() {
		return Id_GuiaFinalidadeModeloCusta;
	}
	public void setId_GuiaFinalidadeModeloCusta(String id_GuiaFinalidadeModeloCusta) {
		Id_GuiaFinalidadeModeloCusta = id_GuiaFinalidadeModeloCusta;
	}
	
	public String getGuiaFinalidadeModeloCusta() {
		return GuiaFinalidadeModeloCusta;
	}
	public void setGuiaFinalidadeModeloCusta(String guiaFinalidadeModeloCusta) {
		GuiaFinalidadeModeloCusta = guiaFinalidadeModeloCusta;
	}
	
	public GuiaFinalidadeModeloDt getGuiaFinalidadeModeloDt() {
		return guiaFinalidadeModeloDt;
	}
	public void setGuiaFinalidadeModeloDt(
			GuiaFinalidadeModeloDt guiaFinalidadeModeloDt) {
		this.guiaFinalidadeModeloDt = guiaFinalidadeModeloDt;
	}
	
	public CustaDt getCustaDt() {
		return custaDt;
	}
	public void setCustaDt(CustaDt custaDt) {
		this.custaDt = custaDt;
	}
	
	public String getId_GuiaFinalidadeModelo() {
		return Id_GuiaFinalidadeModelo;
	}
	public void setId_GuiaFinalidadeModelo(String id_GuiaFinalidadeModelo) {
		Id_GuiaFinalidadeModelo = id_GuiaFinalidadeModelo;
	}
	
	public String getGuiaFinalidadeModelo() {
		return GuiaFinalidadeModelo;
	}
	public void setGuiaFinalidadeModelo(String guiaFinalidadeModelo) {
		GuiaFinalidadeModelo = guiaFinalidadeModelo;
	}
	
	public String getId_Custa() {
		return Id_Custa;
	}
	public void setId_Custa(String id_Custa) {
		Id_Custa = id_Custa;
	}
	
	public String getCusta() {
		return Custa;
	}
	public void setCusta(String custa) {
		Custa = custa;
	}
	
	public String getCodigoTemp() {
		return CodigoTemp;
	}
	public void setCodigoTemp(String codigoTemp) {
		CodigoTemp = codigoTemp;
	}
	
	
	
}
