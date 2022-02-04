package br.gov.go.tj.projudi.dt;

public class LogDtGen extends Dados {

	/**
     * 
     */
	private static final long serialVersionUID = 4054612261577020067L;
	private String Id_Log;
	private String Tabela;
	private String Id_LogTipo;
	private String LogTipo;
	private String Data;
	private String Hora;	
	private String Usuario;	
	private String ValorAtual;
	private String ValorNovo;
	private String CodigoTemp;
	private String LogTipoCodigo;
	private String Id_Tabela;

	// ---------------------------------------------------------
	public LogDtGen() {

		limpar();

	}

	public String getId() {
		return Id_Log;
	}

	public void setId(String valor) {
		if (valor != null)
			Id_Log = valor;
	}

	public String getTabela() {
		return Tabela;
	}

	public void setTabela(String valor) {
		if (valor != null)
			Tabela = valor;
	}

	public String getId_Tabela() {
		return Id_Tabela;
	}

	public void setId_Tabela(String valor) {
		if (valor != null)
			Id_Tabela = valor;
	}

	public String getId_LogTipo() {
		return Id_LogTipo;
	}

	public void setId_LogTipo(String valor) {
		if (valor != null)
			if (valor.equalsIgnoreCase("null")) {
				Id_LogTipo = "";
				LogTipo = "";
			} else if (!valor.equalsIgnoreCase(""))
				Id_LogTipo = valor;
	}

	public String getLogTipo() {
		return LogTipo;
	}

	public void setLogTipo(String valor) {
		if (valor != null)
			LogTipo = valor;
	}

	public String getData() {
		return Data;
	}

	public void setData(String valor) {
		if (valor != null)
			Data = valor;
	}

	public String getHora() {
		return Hora;
	}

	public void setHora(String valor) {
		if (valor != null)
			Hora = valor;
	}

	public String getUsuario() {
		return Usuario;
	}

	public void setUsuario(String valor) {
		if (valor != null)
			Usuario = valor;
	}


	public String getValorAtual() {
		return ValorAtual;
	}

	public void setValorAtual(String valor) {
		if (valor != null)
			ValorAtual = valor;
	}

	public String getValorNovo() {
		return ValorNovo;
	}

	public void setValorNovo(String valor) {
		if (valor != null)
			ValorNovo = valor;
	}

	public String getCodigoTemp() {
		return CodigoTemp;
	}

	public void setCodigoTemp(String valor) {
		if (valor != null)
			CodigoTemp = valor;
	}

	public String getLogTipoCodigo() {
		return LogTipoCodigo;
	}

	public void setLogTipoCodigo(String valor) {
		if (valor != null)
			LogTipoCodigo = valor;
	}

	public void copiar(LogDt objeto) {
		Id_Log = objeto.getId();
		Tabela = objeto.getTabela();
		Id_LogTipo = objeto.getId_LogTipo();
		LogTipo = objeto.getLogTipo();
		Data = objeto.getData();
		Hora = objeto.getHora();		
		Usuario = objeto.getUsuario();		
		ValorAtual = objeto.getValorAtual();
		ValorNovo = objeto.getValorNovo();
		CodigoTemp = objeto.getCodigoTemp();
		LogTipoCodigo = objeto.getLogTipoCodigo();
		Id_Tabela = objeto.getId_Tabela();
	}

	public void limpar() {
		Id_Log = "";
		Tabela = "";
		Id_LogTipo = "";
		LogTipo = "";
		Data = "";
		Hora = "";		
		Usuario = "";		
		ValorAtual = "";
		ValorNovo = "";
		CodigoTemp = "";
		LogTipoCodigo = "";
		Id_Tabela = "";
		super.limpar();
	}

	public String getPropriedades() {
		return "[Id_Log:" + Id_Log + ";Tabela:" + Tabela + ";Id_LogTipo:" + Id_LogTipo + ";LogTipo:" + LogTipo + ";Data:" + Data + ";Hora:" + Hora + ";Id_Usuario:" + super.getId_UsuarioLog()+ ";Usuario:" + Usuario + ";IpComputador:" + super.getIpComputadorLog() + ";ValorAtual:" + ValorAtual + ";ValorNovo:" + ValorNovo + ";CodigoTemp:" + CodigoTemp + ";LogTipoCodigo:" + LogTipoCodigo + ";Id_Tabela:" + Id_Tabela + "]";
	}

}
