package br.gov.go.tj.projudi.dt;

public class PendenciaCorreiosDtGen extends Dados{

	private static final long serialVersionUID = -5336417647485302388L;
	protected String Id_PendCorreios;
	protected String CodigoRastreamento;
	protected String Matriz;
	protected String Lote;
	protected String CodigoInconsistencia;
	protected String CodigoBaixa;
	protected String DataEntrega;
	protected String DataExpedicao;
	protected String Id_Pend;
	protected String MaoPropria;
	protected String CodigoModelo;
	protected String Id_ProcessoCustaTipo;
	protected String OrdemServico;
	protected String MetaDados;

	public PendenciaCorreiosDtGen() {
		limpar();
	}

	public String getId()  {return Id_PendCorreios;}
	public void setId(String valor ) { if(valor!=null) Id_PendCorreios = valor;}
	public String getCodigoRastreamento()  {return CodigoRastreamento;}
	public void setCodigoRastreamento(String valor ) { if (valor!=null) CodigoRastreamento = valor;}
	public String getMatriz()  {return Matriz;}
	public void setMatriz(String valor ) { if (valor!=null) Matriz = valor;}
	public String getLote()  {return Lote;}
	public void setLote(String valor ) { if (valor!=null) Lote = valor;}
	public String getCodigoInconsistencia()  {return CodigoInconsistencia;}
	public void setCodigoInconsistencia(String valor ) { if (valor!=null) CodigoInconsistencia = valor;}
	public String getCodigoBaixa()  {return CodigoBaixa;}
	public void setCodigoBaixa(String valor ) { if (valor!=null) CodigoBaixa = valor;}
	public String getDataEntrega()  {return DataEntrega;}
	public void setDataEntrega(String valor ) { if (valor!=null) DataEntrega = valor;}
	public String getDataExpedicao()  {return DataExpedicao;}
	public void setDataExpedicao(String valor ) { if (valor!=null) DataExpedicao = valor;}
	public String getId_Pend()  {return Id_Pend;}
	public void setId_Pend(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Pend = "";}else if (!valor.equalsIgnoreCase("")) Id_Pend = valor;}
	public String getMaoPropria()  {return MaoPropria;}
	public void setMaoPropria(String valor ) { if (valor!=null) MaoPropria = valor;}
	public String getOrdemServico()  {return OrdemServico;}
	public void setOrdemServico(String valor ) { if (valor!=null) OrdemServico = valor;}
	public String getId_ProcessoCustaTipo()  {return Id_ProcessoCustaTipo;}
	public void setId_ProcessoCustaTipo(String valor ) { if (valor!=null) Id_ProcessoCustaTipo = valor;}
	public String getCodigoModelo()  {return CodigoModelo;}
	public void setCodigoModelo(String valor ) { if (valor!=null) CodigoModelo = valor;}
	public String getMetaDados()  {return MetaDados;}
	public void setMetaDados(String valor ) { if (valor!=null) MetaDados = valor;}

	public void copiar(PendenciaCorreiosDt objeto){
		 if (objeto==null) return;
		Id_PendCorreios = objeto.getId();
		CodigoRastreamento = objeto.getCodigoRastreamento();
		Matriz = objeto.getMatriz();
		Lote = objeto.getLote();
		CodigoInconsistencia = objeto.getCodigoInconsistencia();
		CodigoBaixa = objeto.getCodigoBaixa();
		DataEntrega = objeto.getDataEntrega();
		DataExpedicao = objeto.getDataExpedicao();
		Id_Pend = objeto.getId_Pend();
		CodigoModelo = objeto.getCodigoModelo();
		MaoPropria = objeto.getMaoPropria();
		OrdemServico = objeto.getOrdemServico();
		Id_ProcessoCustaTipo = objeto.getId_ProcessoCustaTipo();
		MetaDados = objeto.getMetaDados();
	}

	public void limpar(){
		Id_PendCorreios="";
		CodigoRastreamento="";
		Matriz="";
		Lote="";
		CodigoInconsistencia="";
		CodigoBaixa="";
		DataEntrega="";
		DataExpedicao="";
		Id_Pend="";
		MaoPropria = "";
		OrdemServico = "";
		CodigoModelo = "";
		Id_ProcessoCustaTipo = "";
		MetaDados = "";
	}

	public String toJson(){
		return "{\"id\":\"" + getId() + "\",\"CodigoRastreamento\":\"" + getCodigoRastreamento() + "\",\"Matriz\":\"" + getMatriz() + "\",\"Lote\":\"" + getLote() + "\",\"CodigoInconsistencia\":\"" + getCodigoInconsistencia() + "\",\"CodigoBaixa\":\"" + getCodigoBaixa() + "\",\"DataEntrega\":\"" + getDataEntrega() + "\",\"Id_Pend\":\"" + getId_Pend() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_PendCorreios:" + Id_PendCorreios + ";CodigoRastreamento:" + CodigoRastreamento + ";Matriz:" + Matriz + ";Lote:" + Lote + ";CodigoInconsistencia:" + CodigoInconsistencia + ";CodigoBaixa:" + CodigoBaixa + ";DataEntrega:" + DataEntrega + ";Id_Pend:" + Id_Pend + ";DataExpedicao:" + DataExpedicao + ";OrdemServico:" + OrdemServico +"]";
	}

}