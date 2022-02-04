package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoParteAdvogadoDt extends ProcessoParteAdvogadoDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 4446963159004914118L;

    public static final int CodigoPermissao = 228;
    
    public static final long QUANTIDADE_PROCESSOS_FORA_ESTADO = 5;

	private String Id_OabUf;
	private String EstadoOabUf;
	//variavel para esconder o nome da parte, quando for consulta publica
	private boolean boSegredoJustica;
	private boolean recebeIntimacao;
	private String grupoCodigo;
	private String grupo;
	private String Id_ServentiaHabilitacao;
	private String serventiaHabilitacao;
	private String dativo;
	private String email;
	
	public boolean getSegredoJustica(){
	    return boSegredoJustica;
	}
	public void setSegredoJusticao(boolean segredojustica){
	    boSegredoJustica = segredojustica;
	}
	//Variável para auxiliar na habilitação de advogados
	private String AdvogadoTipo;
	
	private String hash;

	public String getId_OabUf() {
		return Id_OabUf;
	}

	public void setId_OabUf(String id_OabUf) {
		if (id_OabUf != null) Id_OabUf = id_OabUf;
	}

	public String getEstadoOabUf() {
		return EstadoOabUf;
	}

	public void setEstadoOabUf(String estadoOabUf) {
		if (estadoOabUf != null) EstadoOabUf = estadoOabUf;
	}

	public void limpar() {
		super.limpar();
		Id_OabUf = "";
		EstadoOabUf = "";
		AdvogadoTipo = "";
		recebeIntimacao = true;
		serventiaHabilitacao = "";
		dativo = "";
		email= "";
	}

	public String getAdvogadoTipo() {
		return AdvogadoTipo;
	}

	public void setAdvogadoTipo(String advogadoTipo) {
		if (advogadoTipo != null) AdvogadoTipo = advogadoTipo;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public boolean getRecebeIntimacao() {
		return recebeIntimacao;
	}
	public void setRecebeIntimacao(boolean recebeIntimacao) {
		this.recebeIntimacao = recebeIntimacao;
	}
	
	public String getDativo() {
		return dativo;
	}
	public void setDativo(String dativo) {
		if (dativo != null)
			this.dativo = dativo;
	}
	/**
	 * Sobrescrevendo método getPropriedades da classe Gen
	 * @author hmgodinho
	 */
	public String getPropriedades(){
		return "[Id_ProcessoParteAdvogado:" + this.getId() + ";NomeAdvogado:" + this.getNomeAdvogado() + ";Id_UsuarioServentiaAdvogado:" + this.getId_UsuarioServentiaAdvogado() + ";UsuarioAdvogado:" + this.getUsuarioAdvogado() + ";Id_ProcessoParte:" + this.getId_ProcessoParte() + ";NomeParte:" + this.getNomeParte() + ";DataEntrada:" + this.getDataEntrada() + ";DataSaida:" + this.getDataSaida() + ";Principal:" + this.getPrincipal() + ";CodigoTemp:" + this.getCodigoTemp() + ";OabNumero:" + this.getOabNumero() + ";OabComplemento:" + this.getOabComplemento() + ";Id_Processo:" + this.getId_Processo() + ";ProcessoNumero:" + this.getProcessoNumero() + ";Id_ProcessoParteTipo:" + this.getId_ProcessoParteTipo() + ";ProcessoParteTipo:" + this.getProcessoParteTipo() + ";RecebeIntimacao:" + this.getRecebeIntimacao() + ";Dativo:" + this.getDativo() +";GrupoCodigo:"+ this.getGrupoCodigo()+";Grupo:"+this.getGrupo()+";Email:"+this.getEmail()+ "]";
	}
	
	public void setId_ServentiaHabilitacao(String id_ServentiaHabilitacao) {
		this.Id_ServentiaHabilitacao = id_ServentiaHabilitacao;
	}
	
	public String getId_ServentiaHabilitacao() {
		return this.Id_ServentiaHabilitacao;
	}
	
	public void setServentiaHabilitacao(String serventiaHabilitacao) {
		this.serventiaHabilitacao = serventiaHabilitacao;
	}
	
	public String getServentiaHabilitacao() {
		return this.serventiaHabilitacao;
	}
	
	public String getGrupoCodigo() {
		return grupoCodigo;
	}

	public void setGrupoCodigo(String valor) {
		if (valor != null) grupoCodigo = valor;
	}
	
	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String valor) {
		if (valor != null) grupo = valor;
	}

	public String mostrarDativo(){
		 if(Funcoes.StringToInt(getDativo(),-1)== 1){
			return "Sim";
		 }
		 return "Não";		 				
	}
	public String mostrarRecebeIntimacao(){
		 if(getRecebeIntimacao()){
			return "Sim";
		 }
		 return "Não";		 				
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
	public boolean hasEmail() {
		if (this.email != null && !this.email.isEmpty()){
			return true;
		} 
		return false;
	}	
	 
}
