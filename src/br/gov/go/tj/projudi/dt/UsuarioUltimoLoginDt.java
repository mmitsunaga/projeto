package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class UsuarioUltimoLoginDt  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8475996017976306293L;
	
	private String Id_Usuario;	
	private String Id_Serventia;	
	private String GrupoCodigo;
	private String Id_ServentiaCargo;
	private String Id_ServentiaCargoUsuarioChefe;
	private String Id_UsuarioServentiaChefe;
	
	private String LayoutCapaProcesso;
	private String Modo_Contraste;
	
	public String getLayoutCapaProcesso() {
		return LayoutCapaProcesso;
	}
	public void setLayoutCapaProcesso(String layoutCapaProcesso) {
		LayoutCapaProcesso = layoutCapaProcesso;
	}
	public String getModo_Contraste() {
		return Modo_Contraste;
	}
	public void setModo_Contraste(String modo_Contraste) {
		Modo_Contraste = modo_Contraste;
	}
	public String getId_Usuario() {
		return Id_Usuario;
	}
	public void setId_Usuario(String id_Usuario) {
		Id_Usuario = id_Usuario;
	}
	public String getId_Serventia() {
		return Id_Serventia;
	}
	public void setId_Serventia(String id_Serventia) {
		Id_Serventia = id_Serventia;
	}
	public String getGrupoCodigo() {
		return GrupoCodigo;
	}
	public void setGrupoCodigo(String grupoCodigo) {
		GrupoCodigo = grupoCodigo;
	}
	public String getId_ServentiaCargo() {
		return Id_ServentiaCargo;
	}
	public void setId_ServentiaCargo(String id_ServentiaCargo) {
		Id_ServentiaCargo = id_ServentiaCargo;
	}
	public String getId_ServentiaCargoUsuarioChefe() {
		return Id_ServentiaCargoUsuarioChefe;
	}
	public void setId_ServentiaCargoUsuarioChefe(
			String id_ServentiaCargoUsuarioChefe) {
		Id_ServentiaCargoUsuarioChefe = id_ServentiaCargoUsuarioChefe;
	}
	public String getId_UsuarioServentiaChefe() {
		return Id_UsuarioServentiaChefe;
	}
	public void setId_UsuarioServentiaChefe(String id_UsuarioServentiaChefe) {
		Id_UsuarioServentiaChefe = id_UsuarioServentiaChefe;
	}
}
