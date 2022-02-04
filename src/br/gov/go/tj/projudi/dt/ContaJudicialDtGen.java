package br.gov.go.tj.projudi.dt;

public class ContaJudicialDtGen extends Dados{

	protected String Id_ContaJudicial;
	protected String ContaJudicialNumero;

	protected String Id_ProcParte;
	protected String Nome;
	protected String ProcNumero;
	protected String Id_Banco;
	protected String Banco;

	protected String Id_Comarca;
	protected String Comarca;
	protected String Id_Serv;
	protected String Serv;
	protected String PessoaTipoDepositante;
	protected String CpfCnpjDepositante;
	protected String NomeDepositante;
	protected String PessoaTipoReclamado;
	protected String CpfCnpjReclamado;
	protected String NomeReclamado;
	protected String PessoalTipoReclamante;
	protected String CpfCnpjReclamante;
	protected String NomeReclamante;
	protected String PessoaTipoAdvReclamado;
	protected String CpfCnpjAdvReclamado;
	protected String NomeAdvReclamado;
	protected String PessoaTipoAdvReclamante;
	protected String CpfCnpjAdvReclamante;
	protected String NomeAdvReclamante;
	protected String CodigoTemp;

//---------------------------------------------------------
	public ContaJudicialDtGen() {

		limpar();

	}

	public String getId()  {return Id_ContaJudicial;}
	public void setId(String valor ) {if(valor!=null) Id_ContaJudicial = valor;}
	public String getContaJudicialNumero()  {return ContaJudicialNumero;}
	public void setContaJudicialNumero(String valor ) {if (valor!=null) ContaJudicialNumero = valor;}
	public String getId_ProcParte()  {return Id_ProcParte;}
	public void setId_ProcParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getProcNumero()  {return ProcNumero;}
	public void setProcNumero(String valor ) {if (valor!=null) ProcNumero = valor;}
	public String getId_Banco()  {return Id_Banco;}
	public void setId_Banco(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Banco = ""; Banco = "";}else if (!valor.equalsIgnoreCase("")) Id_Banco = valor;}
	public String getBanco()  {return Banco;}
	public void setBanco(String valor ) {if (valor!=null) Banco = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getId_Serv()  {return Id_Serv;}
	public void setId_Serv(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serv = ""; Serv = "";}else if (!valor.equalsIgnoreCase("")) Id_Serv = valor;}
	public String getServ()  {return Serv;}
	public void setServ(String valor ) {if (valor!=null) Serv = valor;}
	public String getPessoaTipoDepositante()  {return PessoaTipoDepositante;}
	public void setPessoaTipoDepositante(String valor ) {if (valor!=null) PessoaTipoDepositante = valor;}
	public String getCpfCnpjDepositante()  {return CpfCnpjDepositante;}
	public void setCpfCnpjDepositante(String valor ) {if (valor!=null) CpfCnpjDepositante = valor;}
	public String getNomeDepositante()  {return NomeDepositante;}
	public void setNomeDepositante(String valor ) {if (valor!=null) NomeDepositante = valor;}
	public String getPessoaTipoReclamado()  {return PessoaTipoReclamado;}
	public void setPessoaTipoReclamado(String valor ) {if (valor!=null) PessoaTipoReclamado = valor;}
	public String getCpfCnpjReclamado()  {return CpfCnpjReclamado;}
	public void setCpfCnpjReclamado(String valor ) {if (valor!=null) CpfCnpjReclamado = valor;}
	public String getNomeReclamado()  {return NomeReclamado;}
	public void setNomeReclamado(String valor ) {if (valor!=null) NomeReclamado = valor;}
	public String getPessoalTipoReclamante()  {return PessoalTipoReclamante;}
	public void setPessoalTipoReclamante(String valor ) {if (valor!=null) PessoalTipoReclamante = valor;}
	public String getCpfCnpjReclamante()  {return CpfCnpjReclamante;}
	public void setCpfCnpjReclamante(String valor ) {if (valor!=null) CpfCnpjReclamante = valor;}
	public String getNomeReclamante()  {return NomeReclamante;}
	public void setNomeReclamante(String valor ) {if (valor!=null) NomeReclamante = valor;}
	public String getPessoaTipoAdvReclamado()  {return PessoaTipoAdvReclamado;}
	public void setPessoaTipoAdvReclamado(String valor ) {if (valor!=null) PessoaTipoAdvReclamado = valor;}
	public String getCpfCnpjAdvReclamado()  {return CpfCnpjAdvReclamado;}
	public void setCpfCnpjAdvReclamado(String valor ) {if (valor!=null) CpfCnpjAdvReclamado = valor;}
	public String getNomeAdvReclamado()  {return NomeAdvReclamado;}
	public void setNomeAdvReclamado(String valor ) {if (valor!=null) NomeAdvReclamado = valor;}
	public String getPessoaTipoAdvReclamante()  {return PessoaTipoAdvReclamante;}
	public void setPessoaTipoAdvReclamante(String valor ) {if (valor!=null) PessoaTipoAdvReclamante = valor;}
	public String getCpfCnpjAdvReclamante()  {return CpfCnpjAdvReclamante;}
	public void setCpfCnpjAdvReclamante(String valor ) {if (valor!=null) CpfCnpjAdvReclamante = valor;}
	public String getNomeAdvReclamante()  {return NomeAdvReclamante;}
	public void setNomeAdvReclamante(String valor ) {if (valor!=null) NomeAdvReclamante = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ContaJudicialDt objeto){
		 if (objeto==null) return;
		Id_ContaJudicial = objeto.getId();
		Id_ProcParte = objeto.getId_ProcParte();
		Nome = objeto.getNome();
		ProcNumero = objeto.getProcNumero();
		Id_Banco = objeto.getId_Banco();
		Banco = objeto.getBanco();
		ContaJudicialNumero = objeto.getContaJudicialNumero();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		Id_Serv = objeto.getId_Serv();
		Serv = objeto.getServ();
		PessoaTipoDepositante = objeto.getPessoaTipoDepositante();
		CpfCnpjDepositante = objeto.getCpfCnpjDepositante();
		NomeDepositante = objeto.getNomeDepositante();
		PessoaTipoReclamado = objeto.getPessoaTipoReclamado();
		CpfCnpjReclamado = objeto.getCpfCnpjReclamado();
		NomeReclamado = objeto.getNomeReclamado();
		PessoalTipoReclamante = objeto.getPessoalTipoReclamante();
		CpfCnpjReclamante = objeto.getCpfCnpjReclamante();
		NomeReclamante = objeto.getNomeReclamante();
		PessoaTipoAdvReclamado = objeto.getPessoaTipoAdvReclamado();
		CpfCnpjAdvReclamado = objeto.getCpfCnpjAdvReclamado();
		NomeAdvReclamado = objeto.getNomeAdvReclamado();
		PessoaTipoAdvReclamante = objeto.getPessoaTipoAdvReclamante();
		CpfCnpjAdvReclamante = objeto.getCpfCnpjAdvReclamante();
		NomeAdvReclamante = objeto.getNomeAdvReclamante();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ContaJudicial="";
		Id_ProcParte="";
		Nome="";
		ProcNumero="";
		Id_Banco="";
		Banco="";
		ContaJudicialNumero="";
		Id_Comarca="";
		Comarca="";
		Id_Serv="";
		Serv="";
		PessoaTipoDepositante="";
		CpfCnpjDepositante="";
		NomeDepositante="";
		PessoaTipoReclamado="";
		CpfCnpjReclamado="";
		NomeReclamado="";
		PessoalTipoReclamante="";
		CpfCnpjReclamante="";
		NomeReclamante="";
		PessoaTipoAdvReclamado="";
		CpfCnpjAdvReclamado="";
		NomeAdvReclamado="";
		PessoaTipoAdvReclamante="";
		CpfCnpjAdvReclamante="";
		NomeAdvReclamante="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ContaJudicial:" + Id_ContaJudicial + ";Id_ProcParte:" + Id_ProcParte + ";Nome:" + Nome + ";ProcNumero:" + ProcNumero + ";Id_Banco:" + Id_Banco + ";Banco:" + Banco + ";ContaJudicialNumero:" + ContaJudicialNumero + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";Id_Serv:" + Id_Serv + ";Serv:" + Serv + ";PessoaTipoDepositante:" + PessoaTipoDepositante + ";CpfCnpjDepositante:" + CpfCnpjDepositante + ";NomeDepositante:" + NomeDepositante + ";PessoaTipoReclamado:" + PessoaTipoReclamado + ";CpfCnpjReclamado:" + CpfCnpjReclamado + ";NomeReclamado:" + NomeReclamado + ";PessoalTipoReclamante:" + PessoalTipoReclamante + ";CpfCnpjReclamante:" + CpfCnpjReclamante + ";NomeReclamante:" + NomeReclamante + ";PessoaTipoAdvReclamado:" + PessoaTipoAdvReclamado + ";CpfCnpjAdvReclamado:" + CpfCnpjAdvReclamado + ";NomeAdvReclamado:" + NomeAdvReclamado + ";PessoaTipoAdvReclamante:" + PessoaTipoAdvReclamante + ";CpfCnpjAdvReclamante:" + CpfCnpjAdvReclamante + ";NomeAdvReclamante:" + NomeAdvReclamante + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
