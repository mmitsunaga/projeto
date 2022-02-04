package br.gov.go.tj.projudi.dt;

public class ProcessoObjetoArquivoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6374190838255605557L;
	protected String Id_ProcObjetoArq;
	protected String ProcObjetoArq;


	protected String Id_ObjetoSubtipo;
	protected String ObjetoSubtipo;
	protected String Id_Delegacia;
	protected String Delegacia;
	protected String Id_Proc;
	protected String ProcNumero;
	protected String CodigoLote;
	protected String Placa;
	protected String Chassi;
	protected String Id_ServArquivo;
	protected String Serventiaarquivo;
	protected String Modulo;
	protected String Perfil;
	protected String Nivel;
	protected String Unidade;
	protected String Leilao;
	protected String StatusLeilao;
	protected String NumeroRegistro;
	protected String DataEntrada;
	protected String StatusBaixa;
	protected String DataBaixa;
	protected String NomeRecebedor;
	protected String CpfRecebedor;
	protected String RgRecebedor;
	protected String Id_RgOrgaoExpRecebedor;
	protected String RgOrgaoExp;
	protected String Id_EnderecoRecebedor;
	protected String Logradouro;
	protected String CodigoTemp;
	protected String Numero;
	protected String Complemento;
	protected String Cep;
	protected String Id_Bairro;
	protected String Bairro;
	protected String Cidade;
	protected String Uf;
	protected String NomeDepositante;
	protected String Inquerito;

//---------------------------------------------------------
	public ProcessoObjetoArquivoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcObjetoArq;}
	public void setId(String valor ) { if(valor!=null) Id_ProcObjetoArq = valor;}
	public String getProcObjetoArq()  {return ProcObjetoArq;}
	public void setProcObjetoArq(String valor ) { if (valor!=null) ProcObjetoArq = valor;}
	public String getId_ObjetoSubtipo()  {return Id_ObjetoSubtipo;}
	public void setId_ObjetoSubtipo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ObjetoSubtipo = ""; ObjetoSubtipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ObjetoSubtipo = valor;}
	public String getObjetoSubtipo()  {return ObjetoSubtipo;}
	public void setObjetoSubtipo(String valor ) { if (valor!=null) ObjetoSubtipo = valor;}
	public String getId_Delegacia()  {return Id_Delegacia;}
	public void setId_Delegacia(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Delegacia = ""; Delegacia = "";}else if (!valor.equalsIgnoreCase("")) Id_Delegacia = valor;}
	public String getDelegacia()  {return Delegacia;}
	public void setDelegacia(String valor ) { if (valor!=null) Delegacia = valor;}
	public String getId_Processo()  {return Id_Proc;}
	public void setId_Processo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Proc = ""; ProcNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Proc = valor;}
	public String getProcNumero()  {return ProcNumero;}
	public void setProcNumero(String valor ) { if (valor!=null) ProcNumero = valor;}
	public String getCodigoLote()  {return CodigoLote;}
	public void setCodigoLote(String valor ) { if (valor!=null) CodigoLote = valor;}
	public String getPlaca()  {return Placa;}
	public void setPlaca(String valor ) { if (valor!=null) Placa = valor;}
	public String getChassi()  {return Chassi;}
	public void setChassi(String valor ) { if (valor!=null) Chassi = valor;}
	public String getId_ServArquivo()  {return Id_ServArquivo;}
	public void setId_ServArquivo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServArquivo = ""; Serventiaarquivo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServArquivo = valor;}
	public String getServentiaarquivo()  {return Serventiaarquivo;}
	public void setServentiaarquivo(String valor ) { if (valor!=null) Serventiaarquivo = valor;}
	public String getModulo()  {return Modulo;}
	public void setModulo(String valor ) { if (valor!=null) Modulo = valor;}
	public String getPerfil()  {return Perfil;}
	public void setPerfil(String valor ) { if (valor!=null) Perfil = valor;}
	public String getNivel()  {return Nivel;}
	public void setNivel(String valor ) { if (valor!=null) Nivel = valor;}
	public String getUnidade()  {return Unidade;}
	public void setUnidade(String valor ) { if (valor!=null) Unidade = valor;}
	public String getLeilao()  {return Leilao;}
	public void setLeilao(String valor ) { if (valor!=null) Leilao = valor;}
	public String getStatusLeilao()  {return StatusLeilao;}
	public void setStatusLeilao(String valor ) { if (valor!=null) StatusLeilao = valor;}
	public String getNumeroRegistro()  {return NumeroRegistro;}
	public void setNumeroRegistro(String valor ) { if (valor!=null) NumeroRegistro = valor;}
	public String getDataEntrada()  {return DataEntrada;}
	public void setDataEntrada(String valor ) { if (valor!=null) DataEntrada = valor;}
	public String getStatusBaixa()  {return StatusBaixa;}
	public void setStatusBaixa(String valor ) { if (valor!=null) StatusBaixa = valor;}
	public String getDataBaixa()  {return DataBaixa;}
	public void setDataBaixa(String valor ) { if (valor!=null) DataBaixa = valor;}
	public String getNomeRecebedor()  {return NomeRecebedor;}
	public void setNomeRecebedor(String valor ) { if (valor!=null) NomeRecebedor = valor;}
	public String getCpfRecebedor()  {return CpfRecebedor;}
	public void setCpfRecebedor(String valor ) { if (valor!=null) CpfRecebedor = valor;}
	public String getRgRecebedor()  {return RgRecebedor;}
	public void setRgRecebedor(String valor ) { if (valor!=null) RgRecebedor = valor;}
	public String getId_RgOrgaoExpRecebedor()  {return Id_RgOrgaoExpRecebedor;}
	public void setId_RgOrgaoExpRecebedor(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_RgOrgaoExpRecebedor = ""; RgOrgaoExp = "";}else if (!valor.equalsIgnoreCase("")) Id_RgOrgaoExpRecebedor = valor;}
	public String getRgOrgaoExp()  {return RgOrgaoExp;}
	public void setRgOrgaoExp(String valor ) { if (valor!=null) RgOrgaoExp = valor;}
	public String getId_EnderecoRecebedor()  {return Id_EnderecoRecebedor;}
	public void setId_EnderecoRecebedor(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_EnderecoRecebedor = ""; Logradouro = "";}else if (!valor.equalsIgnoreCase("")) Id_EnderecoRecebedor = valor;}
	public String getLogradouro()  {return Logradouro;}
	public void setLogradouro(String valor ) { if (valor!=null) Logradouro = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}
	public String getNumero()  {return Numero;}
	public void setNumero(String valor ) { if (valor!=null) Numero = valor;}
	public String getComplemento()  {return Complemento;}
	public void setComplemento(String valor ) { if (valor!=null) Complemento = valor;}
	public String getCep()  {return Cep;}
	public void setCep(String valor ) { if (valor!=null) Cep = valor;}
	public String getBairro()  {return Bairro;}
	public void setBairro(String valor ) { if (valor!=null) Bairro = valor;}
	public String getId_Bairro()  {return Id_Bairro;}
	public void setId_Bairro(String valor ) { if (valor!=null) Id_Bairro = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) { if (valor!=null) Cidade = valor;}
	public String getUf()  {return Uf;}
	public void setUf(String valor ) { if (valor!=null) Uf = valor;}
	public String getInquerito()  {return Inquerito;}
	public void setInquerito(String valor ) { if (valor!=null) Inquerito = valor;}
	public String getNomeDepositante()  {return NomeDepositante;}
	public void setNomeDepositante(String valor ) { if (valor!=null) NomeDepositante = valor;}


	public void copiar(ProcessoObjetoArquivoDt objeto){
		 if (objeto==null) return;
		Id_ProcObjetoArq = objeto.getId();
		ProcObjetoArq = objeto.getProcObjetoArq();
		Id_ObjetoSubtipo = objeto.getId_ObjetoSubtipo();
		ObjetoSubtipo = objeto.getObjetoSubtipo();
		Id_Delegacia = objeto.getId_Delegacia();
		Delegacia = objeto.getDelegacia();
		NomeDepositante = objeto.getNomeDepositante();
		Id_Proc = objeto.getId_Processo();
		ProcNumero = objeto.getProcNumero();
		Inquerito = objeto.getInquerito();
		CodigoLote = objeto.getCodigoLote();
		Placa = objeto.getPlaca();
		Chassi = objeto.getChassi();
		Id_ServArquivo = objeto.getId_ServArquivo();
		Serventiaarquivo = objeto.getServentiaarquivo();
		Modulo = objeto.getModulo();
		Perfil = objeto.getPerfil();
		Nivel = objeto.getNivel();
		Unidade = objeto.getUnidade();
		Leilao = objeto.getLeilao();
		StatusLeilao = objeto.getStatusLeilao();
		NumeroRegistro = objeto.getNumeroRegistro();
		DataEntrada = objeto.getDataEntrada();
		StatusBaixa = objeto.getStatusBaixa();
		DataBaixa = objeto.getDataBaixa();
		NomeRecebedor = objeto.getNomeRecebedor();
		CpfRecebedor = objeto.getCpfRecebedor();
		RgRecebedor = objeto.getRgRecebedor();
		Id_RgOrgaoExpRecebedor = objeto.getId_RgOrgaoExpRecebedor();
		RgOrgaoExp = objeto.getRgOrgaoExp();
		Id_EnderecoRecebedor = objeto.getId_EnderecoRecebedor();
		Logradouro = objeto.getLogradouro();
		CodigoTemp = objeto.getCodigoTemp();
		Numero = objeto.getNumero();
		Complemento = objeto.getComplemento();
		Cep = objeto.getCep();
		Id_Bairro = objeto.getId_Bairro();
		Bairro = objeto.getBairro();
		Cidade = objeto.getCidade();
		Uf = objeto.getUf();
	}

	public void limpar(){
		Id_ProcObjetoArq="";
		ProcObjetoArq="";
		Id_ObjetoSubtipo="";
		ObjetoSubtipo="";
		Id_Delegacia="";
		Delegacia="";
		Id_Proc="";
		ProcNumero="";
		CodigoLote="";
		Placa="";
		Chassi="";
		Id_ServArquivo="";
		Serventiaarquivo="";
		Modulo="";
		Perfil="";
		Nivel="";
		Unidade="";
		Leilao="";
		StatusLeilao="";
		NumeroRegistro="";
		DataEntrada="";
		DataBaixa="";
		StatusBaixa="";
		NomeRecebedor="";
		CpfRecebedor="";
		RgRecebedor="";
		Id_RgOrgaoExpRecebedor="";
		RgOrgaoExp="";
		Id_EnderecoRecebedor="";
		Logradouro="";
		CodigoTemp="";
		Numero="";
		Complemento="";
		Cep="";
		Id_Bairro="";
		Bairro="";
		Cidade="";
		Uf="";
		Inquerito="";
		NomeDepositante="";
	}
	
	public void limparObjetoLote(){
		Id_ProcObjetoArq="";
		ProcObjetoArq="";
		Id_ObjetoSubtipo="";
		ObjetoSubtipo="";
		Id_Delegacia="";
		Delegacia="";
		Id_Proc="";
		ProcNumero="";
		Placa="";
		Chassi="";
		Id_ServArquivo="";
		Serventiaarquivo="";
		Modulo="";
		Perfil="";
		Nivel="";
		Unidade="";
		Leilao="";
		StatusLeilao="";
		NumeroRegistro="";
		DataEntrada="";
		DataBaixa="";
		StatusBaixa="";
		NomeRecebedor="";
		CpfRecebedor="";
		RgRecebedor="";
		Id_RgOrgaoExpRecebedor="";
		RgOrgaoExp="";
		Id_EnderecoRecebedor="";
		Logradouro="";
		CodigoTemp="";
		Numero="";
		Complemento="";
		Cep="";
		Id_Bairro="";
		Bairro="";
		Cidade="";
		Uf="";
		Inquerito="";
		NomeDepositante="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() + "\",\"ProcObjetoArq\":\"" + getProcObjetoArq() + "\",\"Id_ObjetoSubtipo\":\"" + getId_ObjetoSubtipo() + "\",\"ObjetoSubtipo\":\"" + getObjetoSubtipo() +  "\",\"NomeDepositante\":\"" + getNomeDepositante() + "\",\"Id_Delegacia\":\"" + getId_Delegacia() + "\",\"Delegacia\":\"" + getDelegacia() + "\",\"Id_Proc\":\"" + getId_Processo() + "\",\"Inquerito\":\"" + getInquerito() + "\",\"ProcNumero\":\"" + getProcNumero() + "\",\"CodigoLote\":\"" + getCodigoLote() + "\",\"Placa\":\"" + getPlaca() + "\",\"Chassi\":\"" + getChassi() + "\",\"Id_ServArquivo\":\"" + getId_ServArquivo() + "\",\"Serventiaarquivo\":\"" + getServentiaarquivo() + "\",\"Modulo\":\"" + getModulo() + "\",\"Perfil\":\"" + getPerfil() + "\",\"Nivel\":\"" + getNivel() + "\",\"Unidade\":\"" + getUnidade() + "\",\"Leilao\":\"" + getLeilao() + "\",\"StatusLeilao\":\"" + getStatusLeilao() + "\",\"NumeroRegistro\":\"" + getNumeroRegistro() + "\",\"DataEntrada\":\"" + getDataEntrada() + "\",\"DataBaixa\":\"" + getDataBaixa() + "\",\"StatusBaixa\":\"" + getStatusBaixa() + "\",\"NomeRecebedor\":\"" + getNomeRecebedor() + "\",\"CpfRecebedor\":\"" + getCpfRecebedor() + "\",\"RgRecebedor\":\"" + getRgRecebedor() + "\",\"Id_RgOrgaoExpRecebedor\":\"" + getId_RgOrgaoExpRecebedor() + "\",\"RgOrgaoExp\":\"" + getRgOrgaoExp() + "\",\"Id_EnderecoRecebedor\":\"" + getId_EnderecoRecebedor() + "\",\"Logradouro\":\"" + getLogradouro() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\",\"Numero\":\"" + getNumero() + "\",\"Complemento\":\"" + getComplemento() + "\",\"Cep\":\"" + getCep() + "\",\"Id_Bairro\":\"" + getId_Bairro() + "\",\"Bairro\":\"" + getBairro() + "\",\"Cidade\":\"" + getCidade() + "\",\"Uf\":\"" + getUf() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_ProcObjetoArq:" + Id_ProcObjetoArq + ";ProcObjetoArq:" + ProcObjetoArq + ";Id_ObjetoSubtipo:" + Id_ObjetoSubtipo + ";ObjetoSubtipo:" + ObjetoSubtipo + ";NomeDepositante:" + NomeDepositante  + ";Id_Delegacia:" + Id_Delegacia + ";Delegacia:" + Delegacia + ";Id_Proc:" + Id_Proc + ";ProcNumero:" + ProcNumero + ";CodigoLote:" + CodigoLote + ";Placa:" + Placa + ";Chassi:" + Chassi + ";Id_ServArquivo:" + Id_ServArquivo + ";Serventiaarquivo:" + Serventiaarquivo + ";Modulo:" + Modulo + ";Perfil:" + Perfil + ";Nivel:" + Nivel + ";Unidade:" + Unidade + ";Leilao:" + Leilao + ";StatusLeilao:" + StatusLeilao + ";NumeroRegistro:" + NumeroRegistro + ";DataEntrada:" + DataEntrada + ";DataBaixa:" + DataBaixa + ";StatusBaixa:" + StatusBaixa + ";NomeRecebedor:" + NomeRecebedor + ";CpfRecebedor:" + CpfRecebedor + ";RgRecebedor:" + RgRecebedor + ";Id_RgOrgaoExpRecebedor:" + Id_RgOrgaoExpRecebedor + ";RgOrgaoExp:" + RgOrgaoExp + ";Id_EnderecoRecebedor:" + Id_EnderecoRecebedor + ";Logradouro:" + Logradouro + ";CodigoTemp:" + CodigoTemp + ";Numero:" + Numero + ";Complemento:" + Complemento + ";Cep:" + Cep + ";Id_Bairro:" + Id_Bairro + ";Bairro:" + Bairro + ";Cidade:" + Cidade + ";Uf:" + Uf + "]";
	}


} 
