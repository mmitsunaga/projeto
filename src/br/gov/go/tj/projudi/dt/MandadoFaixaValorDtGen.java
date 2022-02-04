package br.gov.go.tj.projudi.dt;

public class MandadoFaixaValorDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2024576817617819451L;
	protected String Id_MandadoFaixaValor;
	protected String FaixaInicio;


	protected String FaixaFim;
	protected String DataVigenciaInicio;
	protected String DataVigenciaFim;
	protected String Valor;
	protected String TipoValor;
	protected String MandadoFaixaValor;
	protected String CodigoTemp;

//---------------------------------------------------------
	public MandadoFaixaValorDtGen() {

		limpar();

	}

	public String getId()  {return Id_MandadoFaixaValor;}
	public void setId(String valor ) { if(valor!=null) Id_MandadoFaixaValor = valor;}
	public String getFaixaInicio()  {return FaixaInicio;}
	public void setFaixaInicio(String valor ) { if (valor!=null) FaixaInicio = valor;}
	public String getFaixaFim()  {return FaixaFim;}
	public void setFaixaFim(String valor ) { if (valor!=null) FaixaFim = valor;}
	public String getDataVigenciaInicio()  {return DataVigenciaInicio;}
	public void setDataVigenciaInicio(String valor ) { if (valor!=null) DataVigenciaInicio = valor;}
	public String getDataVigenciaFim()  {return DataVigenciaFim;}
	public void setDataVigenciaFim(String valor ) { if (valor!=null) DataVigenciaFim = valor;}
	
	
	public String getValor()  {return Valor;}
	public void setValor(String valor ) { if (valor!=null) Valor = valor;}
	
	public String getTipoValor()  {return TipoValor;}
	public void setTipoValor(String tipoValor ) { if (tipoValor!=null) TipoValor = tipoValor;}
	
	public String getMandadoFaixaValor()  {return MandadoFaixaValor;}
	public void setMandadoFaixaValor(String mandadoFaixaValor ) { if (mandadoFaixaValor!=null) MandadoFaixaValor = mandadoFaixaValor;}	
	
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(MandadoFaixaValorDt objeto){
		if (objeto==null) return;
		Id_MandadoFaixaValor = objeto.getId();
		FaixaInicio = objeto.getFaixaInicio();
		FaixaFim = objeto.getFaixaFim();
		DataVigenciaInicio = objeto.getDataVigenciaInicio();
		DataVigenciaFim = objeto.getDataVigenciaFim();
		Valor = objeto.getValor();
		TipoValor = objeto.getTipoValor();
		MandadoFaixaValor = objeto.getMandadoFaixaValor();
	
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MandadoFaixaValor="";
		FaixaInicio="";
		FaixaFim="";
		DataVigenciaInicio="";
		DataVigenciaFim="";
		Valor="";
		TipoValor = "";
		MandadoFaixaValor = "";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MandadoFaixaValor:" + Id_MandadoFaixaValor + "; FaixaInicio:" + FaixaInicio + "; FaixaFim:" + FaixaFim + "; DataVigenciaInicio:" + DataVigenciaInicio + "; DataVigenciaFim:" + DataVigenciaFim + "; Valor:" + Valor + "; Tipo Valor: " + TipoValor + "; Mandado Faixa Valor: " + MandadoFaixaValor + "; CodigoTemp:" + CodigoTemp + "]";
	}


} 
