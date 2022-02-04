package br.gov.go.tj.projudi.dt;

/**
 * Classe criada para facilitar o cadastro de usuários para parte
 * @author msapaula
 *
 */
public class UsuarioParteDt extends UsuarioDt {

	/**
     * 
     */
    private static final long serialVersionUID = 6288717067456331203L;

    public static final int CodigoPermissao = 262;

	//Atributos a serem utilizados no cadastro de parte como usuário
	private ProcessoParteDt parteDt;
	//private String Id_ProcessoParte;
	//private String NomeParte;
	
	//Atributos para guardar o UsuarioServentia vinculado a parte temporariamente
	//private String Id_UsuarioServentiaParte;
	//private String UsuarioParte;
	

	public void limpar() {
		super.limpar();
		//Id_ProcessoParte = "";
		//NomeParte = "";
		//Id_UsuarioServentiaParte = "";
		//UsuarioParte = "";
	}

	/*public String getId_ProcessoParte() {
		return Id_ProcessoParte;
	}

	public void setId_ProcessoParte(String valor) {
		if (valor != null) Id_ProcessoParte = valor;
	}

	public String getNomeParte() {
		return NomeParte;
	}

	public void setNomeParte(String valor) {
		if (valor != null) NomeParte = valor;
	}

	public String getId_UsuarioServentiaParte() {
		return Id_UsuarioServentiaParte;
	}

	public void setId_UsuarioServentiaParte(String id_UsuarioServentiaParte) {
		Id_UsuarioServentiaParte = id_UsuarioServentiaParte;
	}

	public String getUsuarioParte() {
		return UsuarioParte;
	}

	public void setUsuarioParte(String usuarioParte) {
		UsuarioParte = usuarioParte;
	}*/

	public ProcessoParteDt getParteDt() {
		return parteDt;
	}

	public void setParteDt(ProcessoParteDt processoParteDt) {
		this.parteDt = processoParteDt;
	}
}
