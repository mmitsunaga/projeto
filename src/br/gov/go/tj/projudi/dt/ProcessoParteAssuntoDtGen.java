package br.gov.go.tj.projudi.dt;

public class ProcessoParteAssuntoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3033511744788403228L;
	protected String Id_ProcessoParteAssunto;
	protected String ProcessoParteNome;

	protected String Id_ProcessoParte;

	protected String Id_Assunto;
	protected String Assunto;
	protected String DataInclusao;
	protected String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteAssuntoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteAssunto;}
	public void setId(String valor ) { if(valor!=null) Id_ProcessoParteAssunto = valor;}
	public String getProcessoParteNome()  {return ProcessoParteNome;}
	public void setProcessoParteNome(String valor ) { if (valor!=null) ProcessoParteNome = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ProcessoParte = ""; ProcessoParteNome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getId_Assunto()  {return Id_Assunto;}
	public void setId_Assunto(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Assunto = ""; Assunto = "";}else if (!valor.equalsIgnoreCase("")) Id_Assunto = valor;}
	public String getAssunto()  {return Assunto;}
	public void setAssunto(String valor ) { if (valor!=null) Assunto = valor;}
	public String getDataInclusao()  {return DataInclusao;}
	public void setDataInclusao(String valor ) { if (valor!=null) DataInclusao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteAssuntoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoParteAssunto = objeto.getId();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		ProcessoParteNome = objeto.getProcessoParteNome();
		Id_Assunto = objeto.getId_Assunto();
		Assunto = objeto.getAssunto();
		DataInclusao = objeto.getDataInclusao();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoParteAssunto="";
		Id_ProcessoParte="";
		ProcessoParteNome="";
		Id_Assunto="";
		Assunto="";
		DataInclusao="";
		CodigoTemp="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() +  "\",\"Id_ProcParte\":\"" + getId_ProcessoParte() + "\",\"Nome\":\"" + getProcessoParteNome() + "\",\"Id_Assunto\":\"" + getId_Assunto() + "\",\"Assunto\":\"" + getAssunto() + "\",\"DataInclusao\":\"" + getDataInclusao() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_ProcParteAssunto:" + Id_ProcessoParteAssunto + ";Id_ProcParte:" + Id_ProcessoParte + ";Nome:" + ProcessoParteNome + ";Id_Assunto:" + Id_Assunto + ";Assunto:" + Assunto + ";DataInclusao:" + DataInclusao + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
