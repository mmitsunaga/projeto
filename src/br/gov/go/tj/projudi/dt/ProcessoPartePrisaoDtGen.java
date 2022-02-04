package br.gov.go.tj.projudi.dt;

public class ProcessoPartePrisaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7704519794234933051L;
	protected String Id_ProcessoPartePrisao;
	protected String PrisaoTipo;

	protected String Id_ProcessoParte;
	protected String Nome;
	protected String DataPrisao;
	protected String Id_PrisaoTipo;

	protected String Id_LocalCumpPena;
	protected String LocalCumpPena;	
	protected String DataEvento;
	protected String Id_EventoTipo;
	protected String EventoTipo;
	protected String PrazoPrisao;
	protected String Id_MoviEvento;
	protected String MoviEvento;
	protected String Id_MoviPrisao;
	protected String MoviPrisao;
	protected String Observacao;

//---------------------------------------------------------
	public ProcessoPartePrisaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoPartePrisao;}
	public void setId(String valor ) { if(valor!=null) Id_ProcessoPartePrisao = valor;}
	public String getPrisaoTipo()  {return PrisaoTipo;}
	public void setPrisaoTipo(String valor ) { if (valor!=null) PrisaoTipo = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ProcessoParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) { if (valor!=null) Nome = valor;}
	public String getDataPrisao()  {return DataPrisao;}
	public void setDataPrisao(String valor ) { if (valor!=null) DataPrisao = valor;}
	public String getId_PrisaoTipo()  {return Id_PrisaoTipo;}
	public void setId_PrisaoTipo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_PrisaoTipo = ""; PrisaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PrisaoTipo = valor;}
	public String getId_LocalCumpPena()  {return Id_LocalCumpPena;}
	public void setId_LocalCumpPena(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_LocalCumpPena = ""; LocalCumpPena = "";}else if (!valor.equalsIgnoreCase("")) Id_LocalCumpPena = valor;}
	public String getLocalCumpPena()  {return LocalCumpPena;}
	public void setLocalCumpPena(String valor ) { if (valor!=null) LocalCumpPena = valor;}	
	public String getDataEvento()  {return DataEvento;}
	public void setDataEvento(String valor ) { if (valor!=null) DataEvento = valor;}
	public String getId_EventoTipo()  {return Id_EventoTipo;}
	public void setId_EventoTipo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_EventoTipo = ""; EventoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_EventoTipo = valor;}
	public String getEventoTipo()  {return EventoTipo;}
	public void setEventoTipo(String valor ) { if (valor!=null) EventoTipo = valor;}
	public String getPrazoPrisao()  {return PrazoPrisao;}
	public void setPrazoPrisao(String valor ) { if (valor!=null) PrazoPrisao = valor;}
	public String getId_MoviEvento()  {return Id_MoviEvento;}
	public void setId_MoviEvento(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_MoviEvento = ""; MoviEvento = "";}else if (!valor.equalsIgnoreCase("")) Id_MoviEvento = valor;}
	public String getMoviEvento()  {return MoviEvento;}
	public void setMoviEvento(String valor ) { if (valor!=null) MoviEvento = valor;}
	public String getId_MoviPrisao()  {return Id_MoviPrisao;}
	public void setId_MoviPrisao(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_MoviPrisao = ""; MoviPrisao = "";}else if (!valor.equalsIgnoreCase("")) Id_MoviPrisao = valor;}
	public String getMoviPrisao()  {return MoviPrisao;}
	public void setMoviPrisao(String valor ) { if (valor!=null) MoviPrisao = valor;}
	public String getObservacao()  {return Observacao;}
	public void setObservacao(String valor ) { if (valor!=null) Observacao = valor;}


	public void copiar(ProcessoPartePrisaoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoPartePrisao = objeto.getId();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		Nome = objeto.getNome();
		DataPrisao = objeto.getDataPrisao();
		Id_PrisaoTipo = objeto.getId_PrisaoTipo();
		PrisaoTipo = objeto.getPrisaoTipo();
		Id_LocalCumpPena = objeto.getId_LocalCumpPena();
		LocalCumpPena = objeto.getLocalCumpPena();			
		DataEvento = objeto.getDataEvento();
		Id_EventoTipo = objeto.getId_EventoTipo();
		EventoTipo = objeto.getEventoTipo();
		PrazoPrisao = objeto.getPrazoPrisao();
		Id_MoviEvento = objeto.getId_MoviEvento();
		MoviEvento = objeto.getMoviEvento();
		Id_MoviPrisao = objeto.getId_MoviPrisao();
		MoviPrisao = objeto.getMoviPrisao();
		Observacao = objeto.getObservacao();
	}

	public void limpar(){
		Id_ProcessoPartePrisao="";
		Id_ProcessoParte="";
		Nome="";
		DataPrisao="";
		Id_PrisaoTipo="";
		PrisaoTipo="";
		Id_LocalCumpPena="";
		LocalCumpPena="";		
		DataEvento="";
		Id_EventoTipo="";
		EventoTipo="";
		PrazoPrisao="";
		Id_MoviEvento="";
		MoviEvento="";
		Id_MoviPrisao="";
		MoviPrisao="";
		Observacao="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() + "\",\"Id_ProcPartePrisao\":\"" + getId() + "\",\"Id_ProcParte\":\"" + getId_ProcessoParte() + "\",\"Nome\":\"" + getNome() + "\",\"DataPrisao\":\"" + getDataPrisao() + "\",\"Id_PrisaoTipo\":\"" + getId_PrisaoTipo() + "\",\"PrisaoTipo\":\"" + getPrisaoTipo() + "\",\"Id_LocalCumpPena\":\"" + getId_LocalCumpPena() + "\",\"LocalCumpPena\":\"" + getLocalCumpPena() + "\",\"InformouFamiliares\":\"" + "\",\"DataEvento\":\"" + getDataEvento() + "\",\"Id_EventoTipo\":\"" + getId_EventoTipo() + "\",\"EventoTipo\":\"" + getEventoTipo() + "\",\"PrazoPrisao\":\"" + getPrazoPrisao() + "\",\"Id_MoviEvento\":\"" + getId_MoviEvento() + "\",\"MoviEvento\":\"" + getMoviEvento() + "\",\"Id_MoviPrisao\":\"" + getId_MoviPrisao() + "\",\"MoviPrisao\":\"" + getMoviPrisao() + "\",\"Observacao\":\"" + getObservacao() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_ProcPartePrisao:" + Id_ProcessoPartePrisao + ";Id_ProcParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";DataPrisao:" + DataPrisao + ";Id_PrisaoTipo:" + Id_PrisaoTipo + ";PrisaoTipo:" + PrisaoTipo + ";Id_LocalCumpPena:" + Id_LocalCumpPena + ";LocalCumpPena:" + LocalCumpPena +  ";DataEvento:" + DataEvento + ";Id_EventoTipo:" + Id_EventoTipo + ";EventoTipo:" + EventoTipo + ";PrazoPrisao:" + PrazoPrisao + ";Id_MoviEvento:" + Id_MoviEvento + ";MoviEvento:" + MoviEvento + ";Id_MoviPrisao:" + Id_MoviPrisao + ";MoviPrisao:" + MoviPrisao + ";Observacao:" + Observacao + "]";
	}


} 
