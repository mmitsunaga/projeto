package br.gov.go.tj.projudi.dt;

public class ExpedirMandadoDtGen extends Dados {

	/**
     * 
     */
    private static final long serialVersionUID = 2997386074874204638L;
    private String CodDocumento;
	private String NumeroProcesso;

	private String Data;
	private String CodCumprimento;
	private String ServentiaCodigo;
	private String Serventia;
	private String Id_Parte;
	private String Parte;
	private String CpfCnpj;
	private String Rg;
	private String Mae;
	private String Ctps;
	private String Inss;
	private String DataNascimento;
	private String Telefone;
	private String Celular;
	private String Id_Endereco;
	private String Logradouro;
	private String Numero;
	private String Complemento;
	private String Id_Bairro;
	private String Bairro;
	private String CodCidade;
	private String Cidade;
	private String Estado;
	private String Cep;
	private String Id_Area;
	private String Area;
	private String Id_MandadoTipo;
	private String MandadoTipo;
	private String Assistencia;
	private String Id_Escala;
	private String Escala;
	private String Regiao;

	
	protected String Id_UsuarioLog;
	protected String IpComputadorLog;
			
	
	
	
//---------------------------------------------------------
	public ExpedirMandadoDtGen() {

		limpar();

	}

	public String getId()  {return CodDocumento;}
	public void setId(String valor ) {if(valor!=null) CodDocumento = valor;}
	public String getCodDocumento()  {return CodDocumento;}
	public void setCodDocumento(String valor ) {if(valor!=null) CodDocumento = valor;}
	public String getNumeroProcesso()  {return NumeroProcesso;}
	public void setNumeroProcesso(String valor ) {if (valor!=null) NumeroProcesso = valor;}
	public String getData()  {return Data;}
	public void setData(String valor ) {if (valor!=null) Data = valor;}
	public String getCodCumprimento()  {return CodCumprimento;}
	public void setCodCumprimento(String valor ) {if (valor!=null) CodCumprimento = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_Parte()  {return Id_Parte;}
	public void setId_Parte(String valor ) {if (valor!=null) Id_Parte = valor;}
	public String getParte()  {return Parte;}
	public void setParte(String valor ) {if (valor!=null) Parte = valor;}
	public String getCpfCnpj()  {return CpfCnpj;}
	public void setCpfCnpj(String valor ) {if (valor!=null) CpfCnpj = valor;}
	public String getRg()  {return Rg;}
	public void setRg(String valor ) {if (valor!=null) Rg = valor;}
	public String getMae()  {return Mae;}
	public void setMae(String valor ) {if (valor!=null) Mae = valor;}
	public String getCtps()  {return Ctps;}
	public void setCtps(String valor ) {if (valor!=null) Ctps = valor;}
	public String getInss()  {return Inss;}
	public void setInss(String valor ) {if (valor!=null) Inss = valor;}
	public String getDataNascimento()  {return DataNascimento;}
	public void setDataNascimento(String valor ) {if (valor!=null) DataNascimento = valor;}
	public String getTelefone()  {return Telefone;}
	public void setTelefone(String valor ) {if (valor!=null) Telefone = valor;}
	public String getCelular()  {return Celular;}
	public void setCelular(String valor ) {if (valor!=null) Celular = valor;}
	public String getId_Endereco()  {return Id_Endereco;}
	public void setId_Endereco(String valor ) {if (valor!=null) Id_Endereco = valor;}
	public String getLogradouro()  {return Logradouro;}
	public void setLogradouro(String valor ) {if (valor!=null) Logradouro = valor;}
	public String getNumero()  {return Numero;}
	public void setNumero(String valor ) {if (valor!=null) Numero = valor;}
	public String getComplemento()  {return Complemento;}
	public void setComplemento(String valor ) {if (valor!=null) Complemento = valor;}
	public String getId_Bairro()  {return Id_Bairro;}
	public void setId_Bairro(String valor ) {if (valor!=null) Id_Bairro = valor;}
	public String getBairro()  {return Bairro;}
	public void setBairro(String valor ) {if (valor!=null) Bairro = valor;}
	public String getCodCidade()  {return CodCidade;}
	public void setCodCidade(String valor ) {if (valor!=null) CodCidade = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) {if (valor!=null) Cidade = valor;}
	public String getEstado()  {return Estado;}
	public void setEstado(String valor ) {if (valor!=null) Estado = valor;}
	public String getCep()  {return Cep;}
	public void setCep(String valor ) {if (valor!=null) Cep = valor;}
	public String getId_Area()  {return Id_Area;}
	public void setId_Area(String valor ) {if (valor!=null) Id_Area = valor;}
	public String getArea()  {return Area;}
	public void setArea(String valor ) {if (valor!=null) Area = valor;}
	public String getId_MandadoTipo()  {return Id_MandadoTipo;}
	public void setId_MandadoTipo(String valor ) {if (valor!=null) Id_MandadoTipo = valor;}
	public String getMandadoTipo()  {return MandadoTipo;}
	public void setMandadoTipo(String valor ) {if (valor!=null) MandadoTipo = valor;}
	public String getAssistencia()  {return Assistencia;}
	public void setAssistencia(String valor ) {if (valor!=null) Assistencia = valor;}
	public String getId_Escala()  {return Id_Escala;}
	public void setId_Escala(String valor ) {if (valor!=null) Id_Escala = valor;}
	public String getEscala()  {return Escala;}
	public void setEscala(String valor ) {if (valor!=null) Escala = valor;}
	public String getRegiao()  {return Regiao;}
	public void setRegiao(String valor ) {if (valor!=null) Regiao = valor;}
	
	
	
	

	public void copiar(ExpedirMandadoDt objeto){
		CodDocumento = objeto.getCodDocumento();
		NumeroProcesso = objeto.getNumeroProcesso();
		Data = objeto.getData();
		CodCumprimento = objeto.getCodCumprimento();
		ServentiaCodigo = objeto.getServentiaCodigo();
		Serventia = objeto.getServentia();
		Id_Parte = objeto.getId_Parte();
		Parte = objeto.getParte();
		CpfCnpj = objeto.getCpfCnpj();
		Rg = objeto.getRg();
		Mae = objeto.getMae();
		Ctps = objeto.getCtps();
		Inss = objeto.getInss();
		DataNascimento = objeto.getDataNascimento();
		Telefone = objeto.getTelefone();
		Celular = objeto.getCelular();
		Id_Endereco = objeto.getId_Endereco();
		Logradouro = objeto.getLogradouro();
		Numero = objeto.getNumero();
		Complemento = objeto.getComplemento();
		Id_Bairro = objeto.getId_Bairro();
		Bairro = objeto.getBairro();
		CodCidade = objeto.getCodCidade();
		Cidade = objeto.getCidade();
		Estado = objeto.getEstado();
		Cep = objeto.getCep();
		Id_Area = objeto.getId_Area();
		Area = objeto.getArea();
		Id_MandadoTipo = objeto.getId_MandadoTipo();
		MandadoTipo = objeto.getMandadoTipo();
		Assistencia = objeto.getAssistencia();
		Id_Escala = objeto.getId_Escala();
		Escala = objeto.getEscala();
		Regiao = objeto.getRegiao();
	}

	public void limpar(){
		CodDocumento="";
		NumeroProcesso="";
		Data="";
		CodCumprimento="";
		ServentiaCodigo="";
		Serventia="";
		Id_Parte="";
		Parte="";
		CpfCnpj="";
		Rg="";
		Mae="";
		Ctps="";
		Inss="";
		DataNascimento="";
		Telefone="";
		Celular="";
		Id_Endereco="";
		Logradouro="";
		Numero="";
		Complemento="";
		Id_Bairro="";
		Bairro="";
		CodCidade="";
		Cidade="";
		Estado="";
		Cep="";
		Id_Area="";
		Area="";
		Id_MandadoTipo="";
		MandadoTipo="";
		Assistencia="";
		Id_Escala="";
		Escala="";
		Regiao="";
	}


	public String getPropriedades(){
		return "[CodDocumento:" + CodDocumento + ";NumeroProcesso:" + NumeroProcesso + ";Data:" + Data + ";CodCumprimento:" + CodCumprimento + ";ServentiaCodigo:" + ServentiaCodigo + ";Serventia:" + Serventia + ";Id_Parte:" + Id_Parte + ";Parte:" + Parte + ";CpfCnpj:" + CpfCnpj + ";Rg:" + Rg + ";Mae:" + Mae + ";Ctps:" + Ctps + ";Inss:" + Inss + ";DataNascimento:" + DataNascimento + ";Telefone:" + Telefone + ";Celular:" + Celular + ";Id_Endereco:" + Id_Endereco + ";Logradouro:" + Logradouro + ";Numero:" + Numero + ";Complemento:" + Complemento + ";Id_Bairro:" + Id_Bairro + ";Bairro:" + Bairro + ";CodCidade:" + CodCidade + ";Cidade:" + Cidade + ";Estado:" + Estado + ";Cep:" + Cep + ";Id_Area:" + Id_Area + ";Area:" + Area + ";Id_MandadoTipo:" + Id_MandadoTipo + ";MandadoTipo:" + MandadoTipo + ";Assistencia:" + Assistencia + ";Id_Escala:" + Id_Escala + ";Escala:" + Escala + ";Regiao:" + Regiao + "]";
	}


} 
