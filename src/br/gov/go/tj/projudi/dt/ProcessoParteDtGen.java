package br.gov.go.tj.projudi.dt;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1197383573619892979L;
    private String Id_ProcessoParte;
	private String Nome;


	private String Sexo;
	private String NomeMae;
	private String NomePai;
	private String Id_ProcessoParteTipo;
	private String ProcessoParteTipo;
	private String Id_Processo;
	private String ProcessoNumero;
	private String Id_ProcessoParteAusencia;
	private String ProcessoParteAusencia;
	private String Id_Naturalidade;
	private String CidadeNaturalidade;
	private String DataNascimento;
	private String Id_EstadoCivil;
	private String EstadoCivil;
	private String Id_Profissao;
	private String Profissao;
	private String Id_Endereco;
	private String Endereco;
	private String Rg;
	private String Id_RgOrgaoExpedidor;
	private String RgOrgaoExpedidor;
	private String RgDataExpedicao;
	private String Id_Escolaridade;
	private String Escolaridade;
	private String Cpf;
	private String Cnpj;
	private String TituloEleitor;
	private String TituloEleitorZona;
	private String TituloEleitorSecao;
	private String Ctps;
	private String CtpsSerie;
	private String Id_CtpsUf;
	private String EstadoCtpsUf;
	private String Pis;
	private String DataCadastro;
	private String EMail;
	private String Telefone;
	private String Celular;
	private String CitacaoOnline;
	private String IntimacaoOnline;
	private String RecebeEMail;
	private String DataBaixa;
	private String Citada;
	private String Id_UsuarioServentia;
	private String Usuario;
	private String Id_GovernoTipo;
	private String GovernoTipo;
	private String Id_EmpresaTipo;
	private String EmpresaTipo;
	private String CodigoTemp;
	private String ProcessoParteTipoCodigo;
	private String ProcessoParteAusenciaCodigo;
	private String Raca;
	private String Id_Raca;

//---------------------------------------------------------
	public ProcessoParteDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParte;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = Funcoes.removeEspacosExcesso(valor);}
	public String getSexo()  {return Sexo;}
	public void setSexo(String valor ) {if (valor!=null) Sexo = valor;}
	public String getNomeMae()  {return NomeMae;}
	public void setNomeMae(String valor ) {if (valor!=null) NomeMae = Funcoes.removeEspacosExcesso(valor);}
	public String getNomePai()  {return NomePai;}
	public void setNomePai(String valor ) {if (valor!=null) NomePai = Funcoes.removeEspacosExcesso(valor);}
	public String getId_ProcessoParteTipo()  {return Id_ProcessoParteTipo;}
	public void setId_ProcessoParteTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParteTipo = ""; ProcessoParteTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParteTipo = valor;}
	public String getProcessoParteTipo()  {return ProcessoParteTipo;}
	public void setProcessoParteTipo(String valor ) {if (valor!=null) ProcessoParteTipo = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_ProcessoParteAusencia()  {return Id_ProcessoParteAusencia;}
	public void setId_ProcessoParteAusencia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParteAusencia = ""; ProcessoParteAusencia = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParteAusencia = valor;}
	public String getProcessoParteAusencia()  {return ProcessoParteAusencia;}
	public void setProcessoParteAusencia(String valor ) {if (valor!=null) ProcessoParteAusencia = valor;}
	public String getId_Naturalidade()  {return Id_Naturalidade;}
	public void setId_Naturalidade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Naturalidade = ""; CidadeNaturalidade = "";}else if (!valor.equalsIgnoreCase("")) Id_Naturalidade = valor;}
	public String getCidadeNaturalidade()  {return CidadeNaturalidade;}
	public void setCidadeNaturalidade(String valor ) {if (valor!=null) CidadeNaturalidade = valor;}
	public String getDataNascimento()  {return DataNascimento;}
	public void setDataNascimento(String valor ) {if (valor!=null) DataNascimento = valor;}
	public String getId_EstadoCivil()  {return Id_EstadoCivil;}
	public void setId_EstadoCivil(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EstadoCivil = ""; EstadoCivil = "";}else if (!valor.equalsIgnoreCase("")) Id_EstadoCivil = valor;}
	public String getEstadoCivil()  {return EstadoCivil;}
	public void setEstadoCivil(String valor ) {if (valor!=null) EstadoCivil = valor;}
	public String getId_Profissao()  {return Id_Profissao;}
	public void setId_Profissao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Profissao = ""; Profissao = "";}else if (!valor.equalsIgnoreCase("")) Id_Profissao = valor;}
	public String getProfissao()  {return Profissao;}
	public void setProfissao(String valor ) {if (valor!=null) Profissao = valor;}
	public String getId_Endereco()  {return Id_Endereco;}
	public void setId_Endereco(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Endereco = ""; Endereco = "";}else if (!valor.equalsIgnoreCase("")) Id_Endereco = valor;}
	public String getEndereco()  {return Endereco;}
	public void setEndereco(String valor ) {if (valor!=null) Endereco = valor;}
	public String getRg()  {return Rg;}
	public void setRg(String valor ) {if (valor!=null) Rg = valor;}
	public String getId_RgOrgaoExpedidor()  {return Id_RgOrgaoExpedidor;}
	public void setId_RgOrgaoExpedidor(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_RgOrgaoExpedidor = ""; RgOrgaoExpedidor = "";}else if (!valor.equalsIgnoreCase("")) Id_RgOrgaoExpedidor = valor;}
	public String getRgOrgaoExpedidor()  {return RgOrgaoExpedidor;}
	public void setRgOrgaoExpedidor(String valor ) {if (valor!=null) RgOrgaoExpedidor = valor;}
	public String getRgDataExpedicao()  {return RgDataExpedicao;}
	public void setRgDataExpedicao(String valor ) {if (valor!=null) RgDataExpedicao = valor;}
	public String getId_Escolaridade()  {return Id_Escolaridade;}
	public void setId_Escolaridade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Escolaridade = ""; Escolaridade = "";}else if (!valor.equalsIgnoreCase("")) Id_Escolaridade = valor;}
	public String getEscolaridade()  {return Escolaridade;}
	public void setEscolaridade(String valor ) {if (valor!=null) Escolaridade = valor;}
	public String getCpf()  {return Cpf;}
	public void setCpf(String valor ) {if (valor!=null) Cpf = valor;}
	public String getCnpj()  {return Cnpj;}
	public void setCnpj(String valor ) {if (valor!=null) Cnpj = valor;}
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
	public String getId_CtpsUf()  {return Id_CtpsUf;}
	public void setId_CtpsUf(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CtpsUf = ""; EstadoCtpsUf = "";}else if (!valor.equalsIgnoreCase("")) Id_CtpsUf = valor;}
	public String getEstadoCtpsUf()  {return EstadoCtpsUf;}
	public void setEstadoCtpsUf(String valor ) {if (valor!=null) EstadoCtpsUf = valor;}
	public String getPis()  {return Pis;}
	public void setPis(String valor ) {if (valor!=null) Pis = valor;}
	public String getDataCadastro()  {return DataCadastro;}
	public void setDataCadastro(String valor ) {if (valor!=null) DataCadastro = valor;}
	public String getEMail()  {return EMail;}
	public void setEMail(String valor ) {if (valor!=null) EMail = valor;}
	public String getTelefone()  {return Telefone;}
	public void setTelefone(String valor ) {if (valor!=null) Telefone = valor;}
	public String getCelular()  {return Celular;}
	public void setCelular(String valor ) {if (valor!=null) Celular = valor;}
	public String getCitacaoOnline()  {return CitacaoOnline;}
	public void setCitacaoOnline(String valor ) {if (valor!=null) CitacaoOnline = valor;}
	public String getIntimacaoOnline()  {return IntimacaoOnline;}
	public void setIntimacaoOnline(String valor ) {if (valor!=null) IntimacaoOnline = valor;}
	public String getRecebeEMail()  {return RecebeEMail;}
	public void setRecebeEMail(String valor ) {if (valor!=null) RecebeEMail = valor;}
	public String getDataBaixa()  {return DataBaixa;}
	public void setDataBaixa(String valor ) {if (valor!=null) DataBaixa = valor;}
	public String getCitada()  {return Citada;}
	public void setCitada(String valor ) {if (valor!=null) Citada = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}
	public String getId_GovernoTipo()  {return Id_GovernoTipo;}
	public void setId_GovernoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GovernoTipo = ""; GovernoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_GovernoTipo = valor;}
	public String getGovernoTipo()  {return GovernoTipo;}
	public void setGovernoTipo(String valor ) {if (valor!=null) GovernoTipo = valor;}
	public String getId_EmpresaTipo()  {return Id_EmpresaTipo;}
	public void setId_EmpresaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EmpresaTipo = ""; EmpresaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_EmpresaTipo = valor;}
	public String getEmpresaTipo()  {return EmpresaTipo;}
	public void setEmpresaTipo(String valor ) {if (valor!=null) EmpresaTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getProcessoParteTipoCodigo()  {return ProcessoParteTipoCodigo;}
	public void setProcessoParteTipoCodigo(String valor ) {if (valor!=null) ProcessoParteTipoCodigo = valor;}
	public String getProcessoParteAusenciaCodigo()  {return ProcessoParteAusenciaCodigo;}
	public void setProcessoParteAusenciaCodigo(String valor ) {if (valor!=null) ProcessoParteAusenciaCodigo = valor;}
	public String getId_Raca()  {return Id_Raca;}
	public void setId_Raca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Raca = ""; Raca = "";}else if (!valor.equalsIgnoreCase("")) Id_Raca = valor;}
	public String getRaca()  {return Raca;}
	public void setRaca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Raca = "";}else if (!valor.equalsIgnoreCase("")) Raca = valor;}

	public void copiar(ProcessoParteDt objeto){
		 if (objeto==null) return;
		Id_ProcessoParte = objeto.getId();
		Nome = objeto.getNome();
		Sexo = objeto.getSexo();
		NomeMae = objeto.getNomeMae();
		NomePai = objeto.getNomePai();
		Id_ProcessoParteTipo = objeto.getId_ProcessoParteTipo();
		ProcessoParteTipo = objeto.getProcessoParteTipo();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_ProcessoParteAusencia = objeto.getId_ProcessoParteAusencia();
		ProcessoParteAusencia = objeto.getProcessoParteAusencia();
		Id_Naturalidade = objeto.getId_Naturalidade();
		CidadeNaturalidade = objeto.getCidadeNaturalidade();
		DataNascimento = objeto.getDataNascimento();
		Id_EstadoCivil = objeto.getId_EstadoCivil();
		EstadoCivil = objeto.getEstadoCivil();
		Id_Profissao = objeto.getId_Profissao();
		Profissao = objeto.getProfissao();
		Id_Endereco = objeto.getId_Endereco();
		Endereco = objeto.getEndereco();
		Rg = objeto.getRg();
		Id_RgOrgaoExpedidor = objeto.getId_RgOrgaoExpedidor();
		RgOrgaoExpedidor = objeto.getRgOrgaoExpedidor();
		RgDataExpedicao = objeto.getRgDataExpedicao();
		Id_Escolaridade = objeto.getId_Escolaridade();
		Escolaridade = objeto.getEscolaridade();
		Cpf = objeto.getCpf();
		Cnpj = objeto.getCnpj();
		TituloEleitor = objeto.getTituloEleitor();
		TituloEleitorZona = objeto.getTituloEleitorZona();
		TituloEleitorSecao = objeto.getTituloEleitorSecao();
		Ctps = objeto.getCtps();
		CtpsSerie = objeto.getCtpsSerie();
		Id_CtpsUf = objeto.getId_CtpsUf();
		EstadoCtpsUf = objeto.getEstadoCtpsUf();
		Pis = objeto.getPis();
		DataCadastro = objeto.getDataCadastro();
		EMail = objeto.getEMail();
		Telefone = objeto.getTelefone();
		Celular = objeto.getCelular();
		CitacaoOnline = objeto.getCitacaoOnline();
		IntimacaoOnline = objeto.getIntimacaoOnline();
		RecebeEMail = objeto.getRecebeEMail();
		DataBaixa = objeto.getDataBaixa();
		Citada = objeto.getCitada();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		Usuario = objeto.getUsuario();
		Id_GovernoTipo = objeto.getId_GovernoTipo();
		GovernoTipo = objeto.getGovernoTipo();
		Id_EmpresaTipo = objeto.getId_EmpresaTipo();
		EmpresaTipo = objeto.getEmpresaTipo();
		CodigoTemp = objeto.getCodigoTemp();
		ProcessoParteTipoCodigo = objeto.getProcessoParteTipoCodigo();
		ProcessoParteAusenciaCodigo = objeto.getProcessoParteAusenciaCodigo();
		Id_Raca = objeto.getId_Raca();
		Raca = objeto.getRaca();
	}

	public void limpar(){
		Id_ProcessoParte="";
		Nome="";
		Sexo="";
		NomeMae="";
		NomePai="";
		Id_ProcessoParteTipo="";
		ProcessoParteTipo="";
		Id_Processo="";
		ProcessoNumero="";
		Id_ProcessoParteAusencia="";
		ProcessoParteAusencia="";
		Id_Naturalidade="";
		CidadeNaturalidade="";
		DataNascimento="";
		Id_EstadoCivil="";
		EstadoCivil="";
		Id_Profissao="";
		Profissao="";
		Id_Endereco="";
		Endereco="";
		Rg="";
		Id_RgOrgaoExpedidor="";
		RgOrgaoExpedidor="";
		RgDataExpedicao="";
		Id_Escolaridade="";
		Escolaridade="";
		Cpf="";
		Cnpj="";
		TituloEleitor="";
		TituloEleitorZona="";
		TituloEleitorSecao="";
		Ctps="";
		CtpsSerie="";
		Id_CtpsUf="";
		EstadoCtpsUf="";
		Pis="";
		DataCadastro="";
		EMail="";
		Telefone="";
		Celular="";
		CitacaoOnline="";
		IntimacaoOnline="";
		RecebeEMail="";
		DataBaixa="";
		Citada="";
		Id_UsuarioServentia="";
		Usuario="";
		Id_GovernoTipo="";
		GovernoTipo="";
		Id_EmpresaTipo="";
		EmpresaTipo="";
		CodigoTemp="";
		ProcessoParteTipoCodigo="";
		ProcessoParteAusenciaCodigo="";
		Id_Raca = "";
		Raca = "";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";Sexo:" + Sexo + ";NomeMae:" + NomeMae + ";NomePai:" + NomePai + ";Id_ProcessoParteTipo:" + Id_ProcessoParteTipo + ";ProcessoParteTipo:" + ProcessoParteTipo + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";Id_ProcessoParteAusencia:" + Id_ProcessoParteAusencia + ";ProcessoParteAusencia:" + ProcessoParteAusencia + ";Id_Naturalidade:" + Id_Naturalidade + ";CidadeNaturalidade:" + CidadeNaturalidade + ";DataNascimento:" + DataNascimento + ";Id_EstadoCivil:" + Id_EstadoCivil + ";EstadoCivil:" + EstadoCivil + ";Id_Profissao:" + Id_Profissao + ";Profissao:" + Profissao + ";Id_Endereco:" + Id_Endereco + ";Endereco:" + Endereco + ";Rg:" + Rg + ";Id_RgOrgaoExpedidor:" + Id_RgOrgaoExpedidor + ";RgOrgaoExpedidor:" + RgOrgaoExpedidor + ";RgDataExpedicao:" + RgDataExpedicao + ";Id_Escolaridade:" + Id_Escolaridade + ";Escolaridade:" + Escolaridade + ";Cpf:" + Cpf + ";Cnpj:" + Cnpj + ";TituloEleitor:" + TituloEleitor + ";TituloEleitorZona:" + TituloEleitorZona + ";TituloEleitorSecao:" + TituloEleitorSecao + ";Ctps:" + Ctps + ";CtpsSerie:" + CtpsSerie + ";Id_CtpsUf:" + Id_CtpsUf + ";EstadoCtpsUf:" + EstadoCtpsUf + ";Pis:" + Pis + ";DataCadastro:" + DataCadastro + ";EMail:" + EMail + ";Telefone:" + Telefone + ";Celular:" + Celular + ";CitacaoOnline:" + CitacaoOnline + ";IntimacaoOnline:" + IntimacaoOnline + ";RecebeEMail:" + RecebeEMail + ";DataBaixa:" + DataBaixa + ";Citada:" + Citada + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";Usuario:" + Usuario + ";Id_GovernoTipo:" + Id_GovernoTipo + ";GovernoTipo:" + GovernoTipo + ";Id_EmpresaTipo:" + Id_EmpresaTipo + ";EmpresaTipo:" + EmpresaTipo + ";CodigoTemp:" + CodigoTemp + ";ProcessoParteTipoCodigo:" + ProcessoParteTipoCodigo + ";ProcessoParteAusenciaCodigo:" + ProcessoParteAusenciaCodigo + ";Id_Raca:" + Id_Raca + "]";
	}


} 
