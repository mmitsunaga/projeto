package br.gov.go.tj.projudi.dt;

public class PonteiroCejuscDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5755298384688221918L;
	protected String Id_PonteiroCejusc;
	protected String UsuCejusc;

	protected String Id_UsuCejusc;

	protected String Id_Serv;
	protected String Serv;
	protected String Id_UsuServConfirmou;
	protected String UsuServConfirmou;
	protected String Id_UsuServCompareceu;
	protected String UsuServCompareceu;
	protected String Id_ServCargoBanca;
	protected String Data;
	protected String Id_PonteiroCejuscStatus;
	protected String PonteiroCejuscStatus;
	protected String Id_AudiTipo;

//---------------------------------------------------------
	public PonteiroCejuscDtGen() {

		limpar();

	}

	public String getId()  {return Id_PonteiroCejusc;}
	public void setId(String valor ) { if(valor!=null) Id_PonteiroCejusc = valor;}
	public String getUsuCejusc()  {return UsuCejusc;}
	public void setUsuCejusc(String valor ) { if (valor!=null) UsuCejusc = valor;}
	public String getId_UsuCejusc()  {return Id_UsuCejusc;}
	public void setId_UsuCejusc(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuCejusc = ""; UsuCejusc = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuCejusc = valor;}
	public String getId_Serv()  {return Id_Serv;}
	public void setId_Serv(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Serv = ""; Serv = "";}else if (!valor.equalsIgnoreCase("")) Id_Serv = valor;}
	public String getServ()  {return Serv;}
	public void setServ(String valor ) { if (valor!=null) Serv = valor;}
	public String getId_UsuServConfirmou()  {return Id_UsuServConfirmou;}
	public void setId_UsuServConfirmou(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuServConfirmou = ""; UsuServConfirmou = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuServConfirmou = valor;}
	public String getUsuServConfirmou()  {return UsuServConfirmou;}
	public void setUsuServConfirmou(String valor ) { if (valor!=null) UsuServConfirmou = valor;}
	public String getId_UsuServCompareceu()  {return Id_UsuServCompareceu;}
	public void setId_UsuServCompareceu(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuServCompareceu = ""; UsuServCompareceu = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuServCompareceu = valor;}
	public String getUsuServCompareceu()  {return UsuServCompareceu;}
	public void setUsuServCompareceu(String valor ) { if (valor!=null) UsuServCompareceu = valor;}
	public String getId_ServCargoBanca()  {return Id_ServCargoBanca;}
	public void setId_ServCargoBanca(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServCargoBanca = ""; Data = "";}else if (!valor.equalsIgnoreCase("")) Id_ServCargoBanca = valor;}
	public String getData()  {return Data;}
	public void setData(String valor ) { if (valor!=null) Data = valor;}
	public String getId_PonteiroCejuscStatus()  {return Id_PonteiroCejuscStatus;}
	public void setId_PonteiroCejuscStatus(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_PonteiroCejuscStatus = ""; PonteiroCejuscStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_PonteiroCejuscStatus = valor;}
	public String getPonteiroCejuscStatus()  {return PonteiroCejuscStatus;}
	public void setPonteiroCejuscStatus(String valor ) { if (valor!=null) PonteiroCejuscStatus = valor;}
	public String getId_AudiTipo()  {return Id_AudiTipo;}
	public void setId_AudiTipo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_AudiTipo = ""; Data = "";}else if (!valor.equalsIgnoreCase("")) Id_AudiTipo = valor;}



	public void copiar(PonteiroCejuscDt objeto){
		 if (objeto==null) return;
		Id_PonteiroCejusc = objeto.getId();
		Id_UsuCejusc = objeto.getId_UsuCejusc();
		UsuCejusc = objeto.getUsuCejusc();
		Id_Serv = objeto.getId_Serv();
		Serv = objeto.getServ();
		Id_UsuServConfirmou = objeto.getId_UsuServConfirmou();
		UsuServConfirmou = objeto.getUsuServConfirmou();
		Id_UsuServCompareceu = objeto.getId_UsuServCompareceu();
		UsuServCompareceu = objeto.getUsuServCompareceu();
		Id_ServCargoBanca = objeto.getId_ServCargoBanca();
		Data = objeto.getData();
		Id_PonteiroCejuscStatus = objeto.getId_PonteiroCejuscStatus();
		PonteiroCejuscStatus = objeto.getPonteiroCejuscStatus();
		Id_AudiTipo = objeto.getId_AudiTipo();
	}

	public void limpar(){
		Id_PonteiroCejusc="";
		Id_UsuCejusc="";
		UsuCejusc="";
		Id_Serv="";
		Serv="";
		Id_UsuServConfirmou="";
		UsuServConfirmou="";
		Id_UsuServCompareceu="";
		UsuServCompareceu="";
		Id_ServCargoBanca="";
		Data="";
		Id_PonteiroCejuscStatus="";
		PonteiroCejuscStatus="";
		Id_AudiTipo="";
	}


	public String getPropriedades(){
		return "[Id_PonteiroCejusc:" + Id_PonteiroCejusc + ";Id_UsuCejusc:" + Id_UsuCejusc + ";UsuCejusc:" + UsuCejusc + ";Id_Serv:" + Id_Serv + ";Serv:" + Serv + ";Id_UsuServConfirmou:" + Id_UsuServConfirmou + ";UsuServConfirmou:" + UsuServConfirmou + ";Id_UsuServCompareceu:" + Id_UsuServCompareceu + ";UsuServCompareceu:" + UsuServCompareceu + ";Id_ServCargoBanca:" + Id_ServCargoBanca + ";Data:" + Data + ";Id_PonteiroCejuscStatus:" + Id_PonteiroCejuscStatus + ";PonteiroCejuscStatus:" + PonteiroCejuscStatus + ";Id_AudiTipo:" + Id_AudiTipo + "]";
	}


} 
