package br.gov.go.tj.projudi.webservice;

import java.io.Serializable;

public class RequisicaoCredenciaisTJGO implements Serializable {

	private static final long serialVersionUID = 8057025575206926711L;

	private String LoginConsultante;
	private String SenhaConsultante;
	private String GrupoCodigo;
	private String Id_usuarioServentia;
	private String Id_serventiaCargo;
	private String Id_serventiaCargoUsuarioChefe;
	private String Id_UsuarioServentiaChefe;
	
	public String getLoginConsultante() {
		return LoginConsultante;
	}

	public void setLoginConsultante(String loginConsultante) {
		LoginConsultante = loginConsultante;
	}
	
	public String getSenhaConsultante() {
		return SenhaConsultante;
	}
	
	public void setSenhaConsultante(String senhaConsultante) {
		SenhaConsultante = senhaConsultante;
	}

	public String getGrupoCodigo() {
		if (GrupoCodigo == null) GrupoCodigo = "";
		return GrupoCodigo;
	}

	public void setGrupoCodigo(String grupoCodigo) {
		GrupoCodigo = grupoCodigo;
	}

	public String getId_usuarioServentia() {
		if (Id_usuarioServentia == null) Id_usuarioServentia = "";
		return Id_usuarioServentia;
	}

	public void setId_usuarioServentia(String id_usuarioServentia) {
		Id_usuarioServentia = id_usuarioServentia;
	}

	public String getId_serventiaCargo() {
		if (Id_serventiaCargo == null) Id_serventiaCargo = "";
		return Id_serventiaCargo;
	}

	public void setId_serventiaCargo(String id_serventiaCargo) {
		Id_serventiaCargo = id_serventiaCargo;
	}

	public String getId_serventiaCargoUsuarioChefe() {
		if (Id_serventiaCargoUsuarioChefe == null) Id_serventiaCargoUsuarioChefe = "";		
		return Id_serventiaCargoUsuarioChefe;
	}

	public void setId_serventiaCargoUsuarioChefe(String id_serventiaCargoUsuarioChefe) {
		Id_serventiaCargoUsuarioChefe = id_serventiaCargoUsuarioChefe;
	}

	public String getId_UsuarioServentiaChefe() {
		if (Id_UsuarioServentiaChefe == null) Id_UsuarioServentiaChefe = "";
		return Id_UsuarioServentiaChefe;
	}

	public void setId_UsuarioServentiaChefe(String id_UsuarioServentiaChefe) {
		Id_UsuarioServentiaChefe = id_UsuarioServentiaChefe;
	}
}
