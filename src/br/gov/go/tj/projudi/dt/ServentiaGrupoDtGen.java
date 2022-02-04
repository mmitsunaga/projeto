package br.gov.go.tj.projudi.dt;

public class ServentiaGrupoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1926222928280487123L;
	private String Id_ServentiaGrupo;
	private String ServentiaGrupo;


	private String Atividade;
	private String Id_Serventia;
	private String Serventia;
	private String Id_ServentiaGrupoProximo;
	private String ServentiaGrupoProximo;
	private String AtividadeProxima;
	private boolean envia_Magistrado;
	private String CodigoTemp;

//---------------------------------------------------------
	public ServentiaGrupoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaGrupo;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaGrupo = valor;}
	public String getServentiaGrupo()  {return ServentiaGrupo;}
	public void setServentiaGrupo(String valor ) {if (valor!=null) ServentiaGrupo = valor;}
	public String getAtividade()  {return Atividade;}
	public void setAtividade(String valor ) {if (valor!=null) Atividade = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_ServentiaGrupoProximo()  {return Id_ServentiaGrupoProximo;}
	public void setId_ServentiaGrupoProximo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaGrupoProximo = ""; ServentiaGrupoProximo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaGrupoProximo = valor;}
	public String getServentiaGrupoProximo()  {return ServentiaGrupoProximo;}
	public void setServentiaGrupoProximo(String valor ) {if (valor!=null) ServentiaGrupoProximo = valor;}
	public String getAtividadeProxima()  {return AtividadeProxima;}
	public void setAtividadeProxima(String valor ) {if (valor!=null) AtividadeProxima = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	

	public boolean isEnviaMagistrado() {
		return envia_Magistrado;
	}

	public void setEnviaMagistrado(boolean enviaDesembargador) {
		envia_Magistrado = enviaDesembargador;
	}

	public void copiar(ServentiaGrupoDt objeto){
		 if (objeto==null) return;
		Id_ServentiaGrupo = objeto.getId();
		ServentiaGrupo = objeto.getServentiaGrupo();
		Atividade = objeto.getAtividade();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_ServentiaGrupoProximo = objeto.getId_ServentiaGrupoProximo();
		ServentiaGrupoProximo = objeto.getServentiaGrupoProximo();
		AtividadeProxima = objeto.getAtividadeProxima();
		envia_Magistrado = objeto.isEnviaMagistrado();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ServentiaGrupo="";
		ServentiaGrupo="";
		Atividade="";
		Id_Serventia="";
		Serventia="";
		Id_ServentiaGrupoProximo="";
		ServentiaGrupoProximo="";
		AtividadeProxima="";
		envia_Magistrado = false;
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaGrupo:" + Id_ServentiaGrupo + ";ServentiaGrupo:" + ServentiaGrupo + ";Atividade:" + Atividade + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_ServentiaGrupoProximo:" + Id_ServentiaGrupoProximo + ";ServentiaGrupoProximo:" + ServentiaGrupoProximo + ";AtividadeProxima:" + AtividadeProxima + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
