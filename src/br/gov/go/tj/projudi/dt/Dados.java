package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

/**
 * Classe abstrata de Dados
 */
public abstract class Dados implements Serializable {
	
	private static final long serialVersionUID = -6035657026196985690L;
	
	protected String Id_UsuarioLog;
	protected String IpComputadorLog;
	protected String CodigoTemp;
			
	public String getIpComputadorLog() {return IpComputadorLog;}
	public void setIpComputadorLog(String valor) {	if (valor != null)	IpComputadorLog = valor;	}
	
	public String getId_UsuarioLog() {return Id_UsuarioLog;   }   
    public void setId_UsuarioLog(String id_UsuarioLog) {if(id_UsuarioLog!=null) Id_UsuarioLog = id_UsuarioLog; }
    
	public String getCodigoTemp()  {return CodigoTemp;}
    public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	abstract public void setId(String id);
	abstract public String getId();
	//abstract public String getDescricao();
	
    public void limpar() {
        Id_UsuarioLog="";
        IpComputadorLog="";
        CodigoTemp="";        
    }

}
