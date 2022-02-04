package br.gov.go.tj.projudi.dt;

public class CejuscDisponibilidadeDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6672721502714600041L;
	protected String Id_CejuscDisponibilidade;
	
	protected String Id_UsuCejusc;
	protected String Nome;
	
	protected String Id_AudiTipo;
	protected String AudiTipo;
	
	protected String Serv;
	protected String Id_Serv;
	
	protected String Domingo;
	protected String Segunda;
	protected String Terca;
	protected String Quarta;
	protected String Quinta;
	protected String Sexta;
	protected String Sabado;	

//---------------------------------------------------------
	public CejuscDisponibilidadeDtGen() {

		limpar();

	}

	public String getId()  {return Id_CejuscDisponibilidade;}
	public void setId(String valor ) { if(valor!=null) Id_CejuscDisponibilidade = valor;}
	
	public String getId_UsuCejusc()  {return Id_UsuCejusc;}
	public void setId_UsuCejusc(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuCejusc = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuCejusc = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) { if (valor!=null) Nome = valor;}
	
	public String getId_AudiTipo()  {return Id_AudiTipo;}
	public void setId_AudiTipo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_AudiTipo = ""; AudiTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_AudiTipo = valor;}
	public String getAudiTipo()  {return AudiTipo;}
	public void setAudiTipo(String valor ) { if (valor!=null) AudiTipo = valor;}
	
	public String getServ()  {return Serv;}
	public void setServ(String valor ) { if (valor!=null) Serv = valor;}
	public String getId_Serv()  {return Id_Serv;}
	public void setId_Serv(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Serv = ""; Serv = "";}else if (!valor.equalsIgnoreCase("")) Id_Serv = valor;}
	
	public String getDomingo()  {return Domingo;}
	public void setDomingo(String valor ) { if (valor!=null) Domingo = valor;}
	public String getSegunda()  {return Segunda;}
	public void setSegunda(String valor ) { if (valor!=null) Segunda = valor;}
	public String getTerca()  {return Terca;}
	public void setTerca(String valor ) { if (valor!=null) Terca = valor;}
	public String getQuarta()  {return Quarta;}
	public void setQuarta(String valor ) { if (valor!=null) Quarta = valor;}
	public String getQuinta()  {return Quinta;}
	public void setQuinta(String valor ) { if (valor!=null) Quinta = valor;}
	public String getSexta()  {return Sexta;}
	public void setSexta(String valor ) { if (valor!=null) Sexta = valor;}
	public String getSabado()  {return Sabado;}
	public void setSabado(String valor ) { if (valor!=null) Sabado = valor;}	


	public void copiar(CejuscDisponibilidadeDt objeto){
		 if (objeto==null) return;
		Id_CejuscDisponibilidade = objeto.getId();
		Id_Serv = objeto.getId_Serv();
		Serv = objeto.getServ();
		Id_UsuCejusc = objeto.getId_UsuCejusc();
		Nome = objeto.getNome();
		Domingo = objeto.getDomingo();
		Segunda = objeto.getSegunda();
		Terca = objeto.getTerca();
		Quarta = objeto.getQuarta();
		Quinta = objeto.getQuinta();
		Sexta = objeto.getSexta();
		Sabado = objeto.getSabado();
		Id_AudiTipo = objeto.getId_AudiTipo();
		AudiTipo = objeto.getAudiTipo();
	}

	public void limpar(){
		Id_CejuscDisponibilidade="";
		Id_Serv="";
		Serv="";
		Id_UsuCejusc="";
		Nome="";
		Domingo="";
		Segunda="";
		Terca="";
		Quarta="";
		Quinta="";
		Sexta="";
		Sabado="";
		Id_AudiTipo="";
		AudiTipo="";
	}


	public String getPropriedades(){
		return "[Id_CejuscDisponibilidade:" + Id_CejuscDisponibilidade + ";Id_Serv:" + Id_Serv + ";Serv:" + Serv + ";Id_UsuCejusc:" + Id_UsuCejusc + ";Nome:" + Nome + ";Domingo:" + Domingo + ";Segunda:" + Segunda + ";Terca:" + Terca + ";Quarta:" + Quarta + ";Quinta:" + Quinta + ";Sexta:" + Sexta + ";Sabado:" + Sabado + ";Id_AudiTipo:" + Id_AudiTipo + ";AudiTipo:" + AudiTipo + "]";
	}


} 
