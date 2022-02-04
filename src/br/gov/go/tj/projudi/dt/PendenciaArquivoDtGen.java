package br.gov.go.tj.projudi.dt;

public class PendenciaArquivoDtGen extends Dados{

	private String Id_PendenciaArquivo;
    private static final long serialVersionUID = 2823039121795173089L;
	private String NomeArquivo;

	private String Id_Arquivo;

	private String Recibo;
	private String Id_Pendencia;
	private String PendenciaTipo;
	private String Resposta;
	private String CodigoTemp;

//---------------------------------------------------------
	public PendenciaArquivoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PendenciaArquivo;}
	public void setId(String valor ) {if(valor!=null) Id_PendenciaArquivo = valor;}
	public String getNomeArquivo()  {return NomeArquivo;}
	public void setNomeArquivo(String valor ) {if (valor!=null) NomeArquivo = valor;}
	public String getId_Arquivo()  {return Id_Arquivo;}
	public void setId_Arquivo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Arquivo = ""; NomeArquivo = "";}else if (!valor.equalsIgnoreCase("")) Id_Arquivo = valor;}
	public String getRecibo()  {return Recibo;}
	public void setRecibo(String valor ) {if (valor!=null) Recibo = valor;}
	public String getId_Pendencia()  {return Id_Pendencia;}
	public void setId_Pendencia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Pendencia = ""; PendenciaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_Pendencia = valor;}
	public String getPendenciaTipo()  {return PendenciaTipo;}
	public void setPendenciaTipo(String valor ) {if (valor!=null) PendenciaTipo = valor;}
	public String getResposta()  {return Resposta;}
	public void setResposta(String valor ) {if (valor!=null) Resposta = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PendenciaArquivoDt objeto){
		 if (objeto==null) return;
		Id_PendenciaArquivo = objeto.getId();
		Id_Arquivo = objeto.getId_Arquivo();
		NomeArquivo = objeto.getNomeArquivo();
		Recibo = objeto.getRecibo();
		Id_Pendencia = objeto.getId_Pendencia();
		PendenciaTipo = objeto.getPendenciaTipo();
		Resposta = objeto.getResposta();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PendenciaArquivo="";
		Id_Arquivo="";
		NomeArquivo="";
		Recibo="";
		Id_Pendencia="";
		PendenciaTipo="";
		Resposta="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PendenciaArquivo:" + Id_PendenciaArquivo + ";Id_Arquivo:" + Id_Arquivo + ";NomeArquivo:" + NomeArquivo + ";Recibo:" + Recibo + ";Id_Pendencia:" + Id_Pendencia + ";PendenciaTipo:" + PendenciaTipo + ";Resposta:" + Resposta + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
