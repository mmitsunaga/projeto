package br.gov.go.tj.projudi.util.enumeradoresSeguros;

/**
 * 
 * Classe:     EnumConfigPingdom.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: TypeSafeEnum representando as propriedades das configurações de acesso ao pingdom   
 *             
 */

public class EnumConfigPingdom {

	public static final EnumConfigPingdom pingdomURL = new EnumConfigPingdom("pingdomURL","URL do servico Pingdom");	
	public static final EnumConfigPingdom pingdomUserName = new EnumConfigPingdom("pingdomUserName","Usuario do servico Pingdom");
	public static final EnumConfigPingdom pingdomPassword = new EnumConfigPingdom("pingdomPassword","Usuario do servico Pingdom");
	public static final EnumConfigPingdom pingdomAPIKey = new EnumConfigPingdom("pingdomAPIKey","API Key de acesso ao Pingdom");
	public static final EnumConfigPingdom pingdomApplicationNameProjudi = new EnumConfigPingdom("pingdomApplicationNameProjudi","Application Name Projudi");
	
	public static EnumConfigPingdom[] Todos = {pingdomURL, 
		                                       pingdomUserName, 
		                                       pingdomPassword, 
		                                       pingdomAPIKey,
		                                       pingdomApplicationNameProjudi};
	
	
	private String Id;
	private String Valor;
	
	private EnumConfigPingdom(String Id, 
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
	
	public static EnumConfigPingdom getEnum(String Id)
	{
		if (Id == null) return null;
		
		for (EnumConfigPingdom config : Todos) {
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
