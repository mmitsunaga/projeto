package br.gov.go.tj.projudi.dt;

public class UsuarioDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1657695236870458736L;
    private String Id_Usuario;
	private String Usuario;
	
	


	private String Id_CtpsUf;
	private String CtpsEstado;
	private String Id_Naturalidade;
	private String Naturalidade;
	private String Id_Endereco;
	private String Endereco;
	private String Id_RgOrgaoExpedidor;
	private String RgOrgaoExpedidor;
	private String RgOrgaoExpedidorSigla;
	private String Senha;
	private String Nome;
	private String Sexo;
	private String DataNascimento;
	private String Rg;
	private String RgDataExpedicao;
	private String Cpf;
	private String TituloEleitor;
	private String TituloEleitorZona;
	private String TituloEleitorSecao;
	private String Ctps;
	private String CtpsSerie;
	private String Pis;
	private String MatriculaTjGo;
	private String NumeroConciliador;
	private String DataCadastro;
	private String Ativo;
	private String DataAtivo;
	private String DataExpiracao;
	private String EMail;
	private String Telefone;
	private String Celular;
	

//---------------------------------------------------------
	public UsuarioDtGen() {

		limpar();

	}

	public String getId()  {return Id_Usuario;}
	public void setId(String valor ) {if(valor!=null) Id_Usuario = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}
	public String getId_CtpsUf()  {return Id_CtpsUf;}
	public void setId_CtpsUf(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CtpsUf = ""; CtpsEstado = "";}else if (!valor.equalsIgnoreCase("")) Id_CtpsUf = valor;}
	public String getCtpsEstado()  {return CtpsEstado;}
	public void setCtpsEstado(String valor ) {if (valor!=null) CtpsEstado = valor;}
	public String getId_Naturalidade()  {return Id_Naturalidade;}
	public void setId_Naturalidade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Naturalidade = ""; Naturalidade = "";}else if (!valor.equalsIgnoreCase("")) Id_Naturalidade = valor;}
	public String getNaturalidade()  {return Naturalidade;}
	public void setNaturalidade(String valor ) {if (valor!=null) Naturalidade = valor;}
	public String getId_Endereco()  {return Id_Endereco;}
	public void setId_Endereco(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Endereco = ""; Endereco = "";}else if (!valor.equalsIgnoreCase("")) Id_Endereco = valor;}
	public String getEndereco()  {return Endereco;}
	public void setEndereco(String valor ) {if (valor!=null) Endereco = valor;}
	public String getId_RgOrgaoExpedidor()  {return Id_RgOrgaoExpedidor;}
	public void setId_RgOrgaoExpedidor(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_RgOrgaoExpedidor = ""; RgOrgaoExpedidor = "";}else if (!valor.equalsIgnoreCase("")) Id_RgOrgaoExpedidor = valor;}
	public String getRgOrgaoExpedidor()  {return RgOrgaoExpedidor;}
	public void setRgOrgaoExpedidor(String valor ) {if (valor!=null) RgOrgaoExpedidor = valor;}
	public String getRgOrgaoExpedidorSigla()  {return RgOrgaoExpedidorSigla;}
	public void setRgOrgaoExpedidorSigla(String valor ) {if (valor!=null) RgOrgaoExpedidorSigla = valor;}
	public String getSenha()  {return Senha;}
	public void setSenha(String valor ) {if (valor!=null) Senha = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getSexo()  {return Sexo;}
	public void setSexo(String valor ) {if (valor!=null) Sexo = valor;}
	public String getDataNascimento()  {return DataNascimento;}
	public void setDataNascimento(String valor ) {if (valor!=null) DataNascimento = valor;}
	public String getRg()  {return Rg;}
	public void setRg(String valor ) {if (valor!=null) Rg = valor;}
	public String getRgDataExpedicao()  {return RgDataExpedicao;}
	public void setRgDataExpedicao(String valor ) {if (valor!=null) RgDataExpedicao = valor;}
	public String getCpf()  {return Cpf;}
	public void setCpf(String valor ) {if (valor!=null) Cpf = valor;}
	public String getTituloEleitor()  {return TituloEleitor;}
	public void setTituloEleitor(String valor ) {if (valor!=null) TituloEleitor = valor;}
	public String getTituloEleitorZona()  {return TituloEleitorZona;}
	public void setTituloEleitorZona(String valor ) {if (valor!=null) TituloEleitorZona = valor;}
	public String getTituloEleitorSecao()  {return TituloEleitorSecao;}
	public void setTituloEleitorSecao(String valor ) {if (valor!=null) TituloEleitorSecao = valor;}
	public String getCtps()  {return Ctps;}
	public void setCtps(String valor ) {if (valor!=null) Ctps = valor;}
	public String getCtpsSerie()  {return CtpsSerie;}
	public void setCtpsSerie(String valor ) {if (valor!=null) CtpsSerie = valor;}
	public String getPis()  {return Pis;}
	public void setPis(String valor ) {if (valor!=null) Pis = valor;}
	public String getMatriculaTjGo()  {return MatriculaTjGo;}
	public void setMatriculaTjGo(String valor ) {if (valor!=null) MatriculaTjGo = valor;}
	public String getNumeroConciliador()  {return NumeroConciliador;}
	public void setNumeroConciliador(String valor ) {if (valor!=null) NumeroConciliador = valor;}
	public String getDataCadastro()  {return DataCadastro;}
	public void setDataCadastro(String valor ) {if (valor!=null) DataCadastro = valor;}
	public String getAtivo()  {return Ativo;}
	public void setAtivo(String valor ) {if (valor!=null) Ativo = valor;}
	public String getDataAtivo()  {return DataAtivo;}
	public void setDataAtivo(String valor ) {if (valor!=null) DataAtivo = valor;}
	public String getDataExpiracao()  {return DataExpiracao;}
	public void setDataExpiracao(String valor ) {if (valor!=null) DataExpiracao = valor;}
	public String getEMail()  {return EMail;}
	public void setEMail(String valor ) {if (valor!=null) EMail = valor;}
	public String getTelefone()  {return Telefone;}
	public void setTelefone(String valor ) {if (valor!=null) Telefone = valor;}
	public String getCelular()  {return Celular;}
	public void setCelular(String valor ) {if (valor!=null) Celular = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(UsuarioDt objeto){
		Id_Usuario = objeto.getId();
		Usuario = objeto.getUsuario();
		Id_CtpsUf = objeto.getId_CtpsUf();
		CtpsEstado = objeto.getCtpsEstado();
		Id_Naturalidade = objeto.getId_Naturalidade();
		Naturalidade = objeto.getNaturalidade();
		Id_Endereco = objeto.getId_Endereco();
		Endereco = objeto.getEndereco();
		Id_RgOrgaoExpedidor = objeto.getId_RgOrgaoExpedidor();
		RgOrgaoExpedidor = objeto.getRgOrgaoExpedidor();
		RgOrgaoExpedidorSigla = objeto.getRgOrgaoExpedidorSigla();
		Senha = objeto.getSenha();
		Nome = objeto.getNome();
		Sexo = objeto.getSexo();
		DataNascimento = objeto.getDataNascimento();
		Rg = objeto.getRg();
		RgDataExpedicao = objeto.getRgDataExpedicao();
		Cpf = objeto.getCpf();
		TituloEleitor = objeto.getTituloEleitor();
		TituloEleitorZona = objeto.getTituloEleitorZona();
		TituloEleitorSecao = objeto.getTituloEleitorSecao();
		Ctps = objeto.getCtps();
		CtpsSerie = objeto.getCtpsSerie();
		Pis = objeto.getPis();
		MatriculaTjGo = objeto.getMatriculaTjGo();
		NumeroConciliador = objeto.getNumeroConciliador();
		DataCadastro = objeto.getDataCadastro();
		Ativo = objeto.getAtivo();
		DataAtivo = objeto.getDataAtivo();
		DataExpiracao = objeto.getDataExpiracao();
		EMail = objeto.getEMail();
		Telefone = objeto.getTelefone();
		Celular = objeto.getCelular();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Usuario="";
		Usuario="";
		Id_CtpsUf="";
		CtpsEstado="";
		Id_Naturalidade="";
		Naturalidade="";
		Id_Endereco="";
		Endereco="";
		Id_RgOrgaoExpedidor="";
		RgOrgaoExpedidor="";
		RgOrgaoExpedidorSigla="";
		Senha="";
		Nome="";
		Sexo="";
		DataNascimento="";
		Rg="";
		RgDataExpedicao="";
		Cpf="";
		TituloEleitor="";
		TituloEleitorZona="";
		TituloEleitorSecao="";
		Ctps="";
		CtpsSerie="";
		Pis="";
		MatriculaTjGo="";
		NumeroConciliador="";
		DataCadastro="";
		Ativo="";
		DataAtivo="";
		DataExpiracao="";
		EMail="";
		Telefone="";
		Celular="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Usuario:" + Id_Usuario + ";Usuario:" + Usuario + ";Id_CtpsUf:" + Id_CtpsUf + ";CtpsEstado:" + CtpsEstado + ";Id_Naturalidade:" + Id_Naturalidade + ";Naturalidade:" + Naturalidade + ";Id_Endereco:" + Id_Endereco + ";Endereco:" + Endereco + ";Id_RgOrgaoExpedidor:" + Id_RgOrgaoExpedidor + ";RgOrgaoExpedidor:" + RgOrgaoExpedidor + ";RgOrgaoExpedidorSigla:" + RgOrgaoExpedidorSigla + ";Senha:" + Senha + ";Nome:" + Nome + ";Sexo:" + Sexo + ";DataNascimento:" + DataNascimento + ";Rg:" + Rg + ";RgDataExpedicao:" + RgDataExpedicao + ";Cpf:" + Cpf + ";TituloEleitor:" + TituloEleitor + ";TituloEleitorZona:" + TituloEleitorZona + ";TituloEleitorSecao:" + TituloEleitorSecao + ";Ctps:" + Ctps + ";CtpsSerie:" + CtpsSerie + ";Pis:" + Pis + ";MatriculaTjGo:" + MatriculaTjGo + ";NumeroConciliador:" + NumeroConciliador + ";DataCadastro:" + DataCadastro + ";Ativo:" + Ativo + ";DataAtivo:" + DataAtivo + ";DataExpiracao:" + DataExpiracao + ";EMail:" + EMail + ";Telefone:" + Telefone + ";Celular:" + Celular + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
