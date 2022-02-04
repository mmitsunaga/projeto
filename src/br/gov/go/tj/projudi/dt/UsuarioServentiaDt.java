package br.gov.go.tj.projudi.dt;

public class UsuarioServentiaDt extends UsuarioServentiaDtGen {

    private static final long serialVersionUID = -3150644042995011020L;

    public static final int CodigoPermissao = 170;

	public static final String SistemaProjudi = "1";

	// Variáveis para controlar possíveis status de UsuarioServentia
	public static final int INATIVO = 0;
	public static final int ATIVO = 1;

	private String Id_Cidade;
	private String PodeGuardarAssinarUsuarioServentiaChefe;

	private String Email = null;

	public String getId_Cidade() {
		return Id_Cidade;
	}

	public void setId_Cidade(String valor) {
		if (valor != null) Id_Cidade = valor;
	}	
	
	public void limpar() {
		Id_Cidade = "";
		this.PodeGuardarAssinarUsuarioServentiaChefe = "";
		super.limpar();
	}

	public String getPodeGuardarAssinarUsuarioServentiaChefe() {
		return PodeGuardarAssinarUsuarioServentiaChefe;
	}

	public void setPodeGuardarAssinarUsuarioServentiaChefe(String valor) {
		if (valor != null) PodeGuardarAssinarUsuarioServentiaChefe = valor;		
	}
	
	public String getPropriedades(){
		return "[Id_UsuarioServentia:" + getId() + ";UsuarioServentia:" + getUsuarioServentia() + ";Id_Usuario:" + getId_Usuario() + ";Usuario:" + getUsuario() + ";Id_Serventia:" + getId_Serventia() + ";Serventia:" + getServentia() + ";Id_Comarca:" + getId_Comarca() + ";Comarca:" + getComarca() + ";Id_EnderecoServentia:" + getId_EnderecoServentia() + ";EnderecoServentia:" + getEnderecoServentia() + ";Id_BairroServentia:" + getId_BairroServentia() + ";BairroServentia:" + getBairroServentia() + ";Id_CidadeServentia:" + getId_CidadeServentia() + ";CidadeServentia:" + getCidadeServentia() + ";Ativo:" + getAtivo() + ";ServentiaCodigo:" + getServentiaCodigo() + ";ServentiaCodigoExterno:" + getServentiaCodigoExterno() + ";ComarcaCodigo:" + getComarcaCodigo() + ";BairroCodigoServentia:" + getBairroCodigoServentia() + ";CidadeCodigoServentia:" + getCidadeCodigoServentia() + ";Nome:" + getNome() + ";Id_UsuarioServentiaChefe:" + getId_UsuarioServentiaChefe() + ";UsuarioServentiaChefe:" + getUsuarioServentiaChefe() + ";CodigoTemp:" + CodigoTemp + ";Id_Cidade:" + Id_Cidade + ";PodeGuardarAssinarUsuarioServentiaChefe:" + PodeGuardarAssinarUsuarioServentiaChefe + "]";
	}

	public boolean isAtivo() {
		String stAtivo = getAtivo();
		if(stAtivo!=null && (stAtivo.equalsIgnoreCase("true") || stAtivo.equalsIgnoreCase("1") || stAtivo.equalsIgnoreCase("verdadeiro") ))
			return true;
		return false;
	}

	public void setEmail(String email) {
		Email  = email;		
	}
	
	public String getEmail(){
		return Email;
	}

	public boolean isAssessor() {
		if (getId_UsuarioServentiaChefe()!=null && !getId_UsuarioServentiaChefe().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isMesmoChefe(String id_UsuarioServentiaChefe) {
		if (isAssessor() && id_UsuarioServentiaChefe.equals(getId_UsuarioServentiaChefe())) {
			return true;
		}
		return false;
	}
}
