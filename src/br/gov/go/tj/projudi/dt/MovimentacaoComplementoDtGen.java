package br.gov.go.tj.projudi.dt;

public class MovimentacaoComplementoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 4625224247448441445L;
    private String Id_MovimentacaoComplemento;
	private String Id_Complemento;
	private String Id_ComplementoTabelado;
	private String Id_Movimentacao;
	private String ValorIdentificador;
	private String ValorTexto;
	private String Origem;
	

//---------------------------------------------------------
	public MovimentacaoComplementoDtGen() {

		limpar();

	}

	public String getId()  {return Id_MovimentacaoComplemento;}
	public void setId(String valor ) {if(valor!=null) Id_MovimentacaoComplemento = valor;}
	public String getId_Complemento()  {return Id_Complemento;}
	public void setId_Complemento(String valor ) {if (valor!=null) Id_Complemento = valor;}
	public String getId_Movimentacao()  {return Id_Movimentacao;}
	public void setId_Movimentacao(String valor ) {if (valor!=null) Id_Movimentacao = valor;}
	public String getId_ComplementoTabelado()  {return Id_ComplementoTabelado;}
	public void setId_ComplementoTabelado(String valor ) {if (valor!=null) Id_ComplementoTabelado = valor;}
	public String getValorIdentificador()  {return ValorIdentificador;}
	public void setValorIdentificador(String valor ) {if (valor!=null) ValorIdentificador = valor;}
	public String getValorTexto()  {return ValorTexto;}
	public void setValorTexto(String valor ) {if (valor!=null) ValorTexto = valor;}
	public String getOrigem()  {return Origem;}
	public void setOrigem(String valor ) {if (valor!=null) Origem = valor;}
	

	public void copiar(MovimentacaoComplementoDt objeto){
		Id_MovimentacaoComplemento = objeto.getId();
		Id_Complemento = objeto.getId_Complemento();
		Id_ComplementoTabelado = objeto.getId_ComplementoTabelado();
		Id_Movimentacao = objeto.getId_Movimentacao();
		ValorIdentificador = objeto.getValorIdentificador();
		ValorTexto = objeto.getValorTexto();
		Origem = objeto.getOrigem();
	}

	public void limpar(){
		Id_MovimentacaoComplemento = "";
		Id_Complemento = "";
		Id_ComplementoTabelado = "";
		Id_Movimentacao = "";
		ValorIdentificador = "";
		ValorTexto = "";
		Origem = "";
	}


	public String getPropriedades(){
		return "[Id_MovimentacaoComplemento:" + Id_MovimentacaoComplemento + ";Id_Movimentacao:" + Id_Movimentacao + ";Id_Complemento:" + Id_Complemento + ";Id_ComplementoTabelado:" + Id_ComplementoTabelado + ";ValorIdentificador:" + ValorIdentificador + ";ValorTexto:" + ValorTexto + ";Origem:" + Origem + "]";
	}


} 
