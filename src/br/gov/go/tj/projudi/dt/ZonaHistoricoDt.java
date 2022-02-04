package br.gov.go.tj.projudi.dt;

public class ZonaHistoricoDt extends Dados{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8607210860135573210L;
	
	public static final int CodigoPermissao=172;
	
	private String Id_ZonaHistorico;
    private ZonaDt ZonaDt;
    private String DataFim; 
    private String NomeUsuario;
    
    public String getId()  {return Id_ZonaHistorico;}
	public void setId(String valor ) { if(valor!=null) Id_ZonaHistorico = valor;}
	
	public ZonaDt getZonaDt() {
		return ZonaDt;
	}
	public void setZonaDt(ZonaDt zonaDt) {
		ZonaDt = zonaDt;
	}
    
    public void limpar(){
		super.limpar();
		Id_ZonaHistorico = "";
		setZonaDt(null);
		DataFim = "";		
	}
    
    public String getDataFim() {
		return DataFim;
	}
	public void setDataFim(String dataFim) {
		DataFim = dataFim;
	}

	public String getPropriedades() {
		String propriedades = "[Id_ZonaHistorico:" + Id_ZonaHistorico + ";DataFim:" + DataFim + "]";
		if (ZonaDt != null) return propriedades + ZonaDt.getPropriedades();
		return propriedades; 
	}
	
	public String getNomeUsuario() {
		return NomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		NomeUsuario = nomeUsuario;
	}
}
