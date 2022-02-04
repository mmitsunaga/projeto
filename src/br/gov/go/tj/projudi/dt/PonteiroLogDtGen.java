package br.gov.go.tj.projudi.dt;

public class PonteiroLogDtGen extends Dados{

	private static final long serialVersionUID = -7237678648151089621L;
	protected String Id_PonteiroLog;
	protected String PonteiroLogTipo;

	protected String Id_AreaDist;
	protected String AreaDist;
	protected String Id_PonteiroLogTipo;

	protected String Id_Proc;
	protected String ProcNumero;
	protected String Id_Serv;
	protected String Serv;
	protected String Id_UsuarioServentia;
	protected String Nome;
	protected String Data;
	protected String Justificativa;
	protected String Qtd;
	protected String CodigoTemp;
	protected String Id_ServentiaCargo;
	protected String ServentiaCargo;
	protected String ServentiaCargoUsuario;

	public PonteiroLogDtGen() {
		limpar();
	}

	public String getId()  {return Id_PonteiroLog;}
	public void setId(String valor ) { if(valor!=null) Id_PonteiroLog = valor;}
	public String getPonteiroLogTipo()  {return PonteiroLogTipo;}
	public void setPonteiroLogTipo(String valor ) { if (valor!=null) PonteiroLogTipo = valor;}
	public String getId_AreaDistribuicao()  {return Id_AreaDist;}
	public void setId_AreaDistribuicao(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_AreaDist = ""; AreaDist = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDist = valor;}
	public String getAreaDistribuicao()  {return AreaDist;}
	public void setAreaDistribuicao(String valor ) { if (valor!=null) AreaDist = valor;}
	public String getId_PonteiroLogTipo()  {return Id_PonteiroLogTipo;}
	public void setId_PonteiroLogTipo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_PonteiroLogTipo = ""; PonteiroLogTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PonteiroLogTipo = valor;}
	public String getId_Proc()  {return Id_Proc;}
	public void setId_Proc(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Proc = ""; ProcNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Proc = valor;}
	public String getProcNumero()  {return ProcNumero;}
	public void setProcNumero(String valor ) { if (valor!=null) ProcNumero = valor;}
	public String getId_Serv()  {return Id_Serv;}
	public void setId_Serventia(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Serv = ""; Serv = "";}else if (!valor.equalsIgnoreCase("")) Id_Serv = valor;}
	public String getServ()  {return Serv;}
	public void setServentia(String valor ) { if (valor!=null) Serv = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuarioServentia = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) { if (valor!=null) Nome = valor;}
	public String getData()  {return Data;}
	public void setData(String valor ) { if (valor!=null) Data = valor;}
	public String getJustificativa()  {return Justificativa;}
	public void setJustificativa(String valor ) { if (valor!=null) Justificativa = valor;}
	public String getQtd()  {return Qtd;}
	public void setQtd(String valor ) { if (valor!=null) Qtd = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}
	
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) ServentiaCargo = valor;}
	
	public String getServentiaCargoUsuario()  {return ServentiaCargoUsuario;}
	public void setServentiaCargoUsuario(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { ServentiaCargoUsuario = "";}else if (!valor.equalsIgnoreCase("")) ServentiaCargoUsuario = valor;}
	
	public void copiar(PonteiroLogDt objeto){
		 if (objeto==null) return;
		Id_PonteiroLog = objeto.getId();
		Id_AreaDist = objeto.getId_AreaDistribuicao();
		AreaDist = objeto.getAreaDistribuicao();
		Id_PonteiroLogTipo = objeto.getId_PonteiroLogTipo();
		PonteiroLogTipo = objeto.getPonteiroLogTipo();
		Id_Proc = objeto.getId_Proc();
		ProcNumero = objeto.getProcNumero();
		Id_Serv = objeto.getId_Serv();
		Serv = objeto.getServ();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		Nome = objeto.getNome();
		Data = objeto.getData();
		Justificativa = objeto.getJustificativa();
		Qtd = objeto.getQtd();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PonteiroLog="";
		Id_AreaDist="";
		AreaDist="";
		Id_PonteiroLogTipo="";
		PonteiroLogTipo="";
		Id_Proc="";
		ProcNumero="";
		Id_Serv="";
		Serv="";
		Id_UsuarioServentia="";
		Nome="";
		Data="";
		Justificativa="";
		Qtd="";
		CodigoTemp="";
		ServentiaCargo = "";
		ServentiaCargoUsuario = "";
	}


	public String getPropriedades(){
		return "[Id_PonteiroLog:" + Id_PonteiroLog + ";Id_AreaDist:" + Id_AreaDist + ";AreaDist:" + AreaDist + ";Id_PonteiroLogTipo:" + Id_PonteiroLogTipo + ";PonteiroLogTipo:" + PonteiroLogTipo + ";Id_Proc:" + Id_Proc + ";ProcNumero:" + ProcNumero + ";Id_Serv:" + Id_Serv + ";Serv:" + Serv + ";Id_UserServ:" + Id_UsuarioServentia + ";Nome:" + Nome + ";Data:" + Data + ";Justificativa:" + Justificativa + ";Qtd:" + Qtd + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
