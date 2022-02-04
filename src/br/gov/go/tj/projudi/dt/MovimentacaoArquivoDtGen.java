package br.gov.go.tj.projudi.dt;

public class MovimentacaoArquivoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 4625224247448441445L;
    private String Id_MovimentacaoArquivo;
	private String NomeArquivo;
	
	

	private String Id_Arquivo;

	private String Id_Movimentacao;
	private String MovimentacaoTipo;
	

//---------------------------------------------------------
	public MovimentacaoArquivoDtGen() {

		limpar();

	}

	public String getId()  {return Id_MovimentacaoArquivo;}
	public void setId(String valor ) {if(valor!=null) Id_MovimentacaoArquivo = valor;}
	public String getNomeArquivo()  {return NomeArquivo;}
	public void setNomeArquivo(String valor ) {if (valor!=null) NomeArquivo = valor;}
	public String getId_Arquivo()  {return Id_Arquivo;}
	public void setId_Arquivo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Arquivo = ""; NomeArquivo = "";}else if (!valor.equalsIgnoreCase("")) Id_Arquivo = valor;}
	public String getId_Movimentacao()  {return Id_Movimentacao;}
	public void setId_Movimentacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Movimentacao = ""; MovimentacaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_Movimentacao = valor;}
	public String getMovimentacaoTipo()  {return MovimentacaoTipo;}
	public void setMovimentacaoTipo(String valor ) {if (valor!=null) MovimentacaoTipo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(MovimentacaoArquivoDt objeto){
		Id_MovimentacaoArquivo = objeto.getId();
		Id_Arquivo = objeto.getId_Arquivo();
		NomeArquivo = objeto.getNomeArquivo();
		Id_Movimentacao = objeto.getId_Movimentacao();
		MovimentacaoTipo = objeto.getMovimentacaoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MovimentacaoArquivo="";
		Id_Arquivo="";
		NomeArquivo="";
		Id_Movimentacao="";
		MovimentacaoTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MovimentacaoArquivo:" + Id_MovimentacaoArquivo + ";Id_Arquivo:" + Id_Arquivo + ";NomeArquivo:" + NomeArquivo + ";Id_Movimentacao:" + Id_Movimentacao + ";MovimentacaoTipo:" + MovimentacaoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
