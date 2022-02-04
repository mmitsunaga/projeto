package br.gov.go.tj.projudi.dt;

public class PartesIsentaDtGen extends Dados{

	protected String Id_PartesIsentas;
	protected String Nome;


	protected String Cpf;
	protected String Cnpj;
	protected String Id_UsuarioCadastrador;
	protected String NomeUsuarioCadastrador;
	protected String Id_ServentiaUsuarioCadastrador;
	protected String ServentiaUsuarioCadastrador;
	protected String DataCadastro;
	protected String Id_UsuarioBaixa;
	protected String NomeUsuarioBaixa;
	protected String Id_ServentiaUsuarioBaixa;
	protected String ServentiaUsuarioBaixa;
	protected String DataBaixa;
	protected String CodigoTemp;

//---------------------------------------------------------
	public PartesIsentaDtGen() {

		limpar();

	}

	public String getId()  {return Id_PartesIsentas;}
	public void setId(String valor ) { if(valor!=null) Id_PartesIsentas = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) { if (valor!=null) Nome = valor;}
	public String getCpf()  {return Cpf;}
	public void setCpf(String valor ) { if (valor!=null) Cpf = valor;}
	public String getCnpj()  {return Cnpj;}
	public void setCnpj(String valor ) { if (valor!=null) Cnpj = valor;}
	public String getId_UsuarioCadastrador()  {return Id_UsuarioCadastrador;}
	public void setId_UsuarioCadastrador(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuarioCadastrador = ""; NomeUsuarioCadastrador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioCadastrador = valor;}
	public String getNomeUsuarioCadastrador()  {return NomeUsuarioCadastrador;}
	public void setNomeUsuarioCadastrador(String valor ) { if (valor!=null) NomeUsuarioCadastrador = valor;}
	public String getId_ServentiaUsuarioCadastrador()  {return Id_ServentiaUsuarioCadastrador;}
	public void setId_ServentiaUsuarioCadastrador(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServentiaUsuarioCadastrador = ""; ServentiaUsuarioCadastrador = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaUsuarioCadastrador = valor;}
	public String getServentiaUsuarioCadastrador()  {return ServentiaUsuarioCadastrador;}
	public void setServentiaUsuarioCadastrador(String valor ) { if (valor!=null) ServentiaUsuarioCadastrador = valor;}
	public String getDataCadastro()  {return DataCadastro;}
	public void setDataCadastro(String valor ) { if (valor!=null) DataCadastro = valor;}
	public String getId_UsuarioBaixa()  {return Id_UsuarioBaixa;}
	public void setId_UsuarioBaixa(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuarioBaixa = ""; NomeUsuarioBaixa = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioBaixa = valor;}
	public String getNomeUsuarioBaixa()  {return NomeUsuarioBaixa;}
	public void setNomeUsuarioBaixa(String valor ) { if (valor!=null) NomeUsuarioBaixa = valor;}
	public String getId_ServentiaUsuarioBaixa()  {return Id_ServentiaUsuarioBaixa;}
	public void setId_ServentiaUsuarioBaixa(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServentiaUsuarioBaixa = ""; ServentiaUsuarioBaixa = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaUsuarioBaixa = valor;}
	public String getServentiaUsuarioBaixa()  {return ServentiaUsuarioBaixa;}
	public void setServentiaUsuarioBaixa(String valor ) { if (valor!=null) ServentiaUsuarioBaixa = valor;}
	public String getDataBaixa()  {return DataBaixa;}
	public void setDataBaixa(String valor ) { if (valor!=null) DataBaixa = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(PartesIsentaDt objeto){
		 if (objeto==null) return;
		Id_PartesIsentas = objeto.getId();
		Nome = objeto.getNome();
		Cpf = objeto.getCpf();
		Cnpj = objeto.getCnpj();
		Id_UsuarioCadastrador = objeto.getId_UsuarioCadastrador();
		NomeUsuarioCadastrador = objeto.getNomeUsuarioCadastrador();
		Id_ServentiaUsuarioCadastrador = objeto.getId_ServentiaUsuarioCadastrador();
		ServentiaUsuarioCadastrador = objeto.getServentiaUsuarioCadastrador();
		DataCadastro = objeto.getDataCadastro();
		Id_UsuarioBaixa = objeto.getId_UsuarioBaixa();
		NomeUsuarioBaixa = objeto.getNomeUsuarioBaixa();
		Id_ServentiaUsuarioBaixa = objeto.getId_ServentiaUsuarioBaixa();
		ServentiaUsuarioBaixa = objeto.getServentiaUsuarioBaixa();
		DataBaixa = objeto.getDataBaixa();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PartesIsentas="";
		Nome="";
		Cpf="";
		Cnpj="";
		Id_UsuarioCadastrador="";
		NomeUsuarioCadastrador="";
		Id_ServentiaUsuarioCadastrador="";
		ServentiaUsuarioCadastrador="";
		DataCadastro="";
		Id_UsuarioBaixa="";
		NomeUsuarioBaixa="";
		Id_ServentiaUsuarioBaixa="";
		ServentiaUsuarioBaixa="";
		DataBaixa="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PartesIsentas:" + Id_PartesIsentas + ";Nome:" + Nome + ";Cpf:" + Cpf + ";Cnpj:" + Cnpj + ";Id_UsuarioCadastrador:" + Id_UsuarioCadastrador + ";NomeUsuarioCadastrador:" + NomeUsuarioCadastrador + ";Id_ServentiaUsuarioCadastrador:" + Id_ServentiaUsuarioCadastrador + ";ServentiaUsuarioCadastrador:" + ServentiaUsuarioCadastrador + ";DataCadastro:" + DataCadastro + ";Id_UsuarioBaixa:" + Id_UsuarioBaixa + ";NomeUsuarioBaixa:" + NomeUsuarioBaixa + ";Id_ServentiaUsuarioBaixa:" + Id_ServentiaUsuarioBaixa + ";ServentiaUsuarioBaixa:" + ServentiaUsuarioBaixa + ";DataBaixa:" + DataBaixa + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
