package br.gov.go.tj.projudi.util.enumeradoresSeguros;

/**
 * 
 * Classe:     EnumConfigPingdom.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: TypeSafeEnum representando as propriedades das configurações de acesso ao pingdom   
 *             
 */

public class EnumConfigPingdomPJE implements IEnumConfigPingdom {

	public static final EnumConfigPingdomPJE pingdomURL = new EnumConfigPingdomPJE("pingdomURLPJE","URL do servico Pingdom");	
	public static final EnumConfigPingdomPJE pingdomUserName = new EnumConfigPingdomPJE("pingdomUserNamePJE","Usuario do servico Pingdom");
	public static final EnumConfigPingdomPJE pingdomPassword = new EnumConfigPingdomPJE("pingdomPasswordPJE","Usuario do servico Pingdom");
	public static final EnumConfigPingdomPJE pingdomAPIKey = new EnumConfigPingdomPJE("pingdomAPIKeyPJE","API Key de acesso ao Pingdom");
	public static final EnumConfigPingdomPJE pingdomApplicationNamePJE = new EnumConfigPingdomPJE("pingdomApplicationNamePJE","Application Name PJE");
	
	public static EnumConfigPingdomPJE[] Todos = {pingdomURL, 
		                                       pingdomUserName, 
		                                       pingdomPassword, 
		                                       pingdomAPIKey,
		                                       pingdomApplicationNamePJE};
	
	
	private String Id;
	private String Valor;
	
	private EnumConfigPingdomPJE(String Id, 
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
	
	public static EnumConfigPingdomPJE getEnum(String Id)
	{
		if (Id == null) return null;
		
		for (EnumConfigPingdomPJE config : Todos) {
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
