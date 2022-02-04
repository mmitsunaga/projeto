package br.gov.go.tj.projudi.util.enumeradoresSeguros;

/**
 * 
 * Classe:     EnumConfigProxy.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: TypeSafeEnum representando as propriedades das configurações de acesso ao servidor proxy   
 *             
 */
public class EnumConfigProxy {

	public static final EnumConfigProxy proxyURL = new EnumConfigProxy("proxyURL","Endereco do Proxy");	
	public static final EnumConfigProxy proxyPort = new EnumConfigProxy("proxyPort","Endereco do Proxy");
	public static final EnumConfigProxy proxyUserName = new EnumConfigProxy("proxyUserName","Endereco do Proxy");
	public static final EnumConfigProxy proxyPassword = new EnumConfigProxy("proxyPassword","Endereco do Proxy");
	
	public static EnumConfigProxy[] Todos = {proxyURL, 
											 proxyPort, 
											 proxyUserName, 
											 proxyPassword};
	
	
	private String Id;
	private String Valor;
	
	private EnumConfigProxy(String Id, 
			                String Valor)
	{
		this.Id = Id;
		this.Valor = Valor;
	}
	
	public String getId()
	{
		return this.Id;
	}
	
	public String getValor()
	{
		return this.Valor;
	}
	
	public static EnumConfigProxy getEnum(String Id)
	{
		if (Id == null) return null;
		
		for (EnumConfigProxy config : Todos) {
			if (config.Id.equalsIgnoreCase(Id)) return config;
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return this.Valor;
	}
	
	  
}
