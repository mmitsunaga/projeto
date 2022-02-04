package br.gov.go.tj.projudi.dt;

public class AudienciaTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 1780999284312014973L;
    private String Id_AudienciaTipo;
	private String AudienciaTipo;
	
	


	private String AudienciaTipoCodigo;
	

//---------------------------------------------------------
	public AudienciaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_AudienciaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_AudienciaTipo = valor;}
	public String getAudienciaTipo()  {return AudienciaTipo;}
	public void setAudienciaTipo(String valor ) {if (valor!=null) AudienciaTipo = valor;}
	public String getAudienciaTipoCodigo()  {return AudienciaTipoCodigo;}
	public void setAudienciaTipoCodigo(String valor ) {if (valor!=null) AudienciaTipoCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(AudienciaTipoDt objeto){
		Id_AudienciaTipo = objeto.getId();
		AudienciaTipo = objeto.getAudienciaTipo();
		AudienciaTipoCodigo = objeto.getAudienciaTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_AudienciaTipo="";
		AudienciaTipo="";
		AudienciaTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_AudienciaTipo:" + Id_AudienciaTipo + ";AudienciaTipo:" + AudienciaTipo + ";AudienciaTipoCodigo:" + AudienciaTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
