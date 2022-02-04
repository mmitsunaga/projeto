package br.gov.go.tj.projudi.util.enumeradoresSeguros;

/**
 * 
 * Classe:     EnumConfigPingdom.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: TypeSafeEnum representando as propriedades das configurações de acesso ao pingdom   
 *             
 */

public class EnumConfigPingdomProjudi implements IEnumConfigPingdom {

	public static final EnumConfigPingdomProjudi pingdomURL = new EnumConfigPingdomProjudi("pingdomURL","URL do servico Pingdom");	
	public static final EnumConfigPingdomProjudi pingdomUserName = new EnumConfigPingdomProjudi("pingdomUserName","Usuario do servico Pingdom");
	public static final EnumConfigPingdomProjudi pingdomPassword = new EnumConfigPingdomProjudi("pingdomPassword","Usuario do servico Pingdom");
	public static final EnumConfigPingdomProjudi pingdomAPIKey = new EnumConfigPingdomProjudi("pingdomAPIKey","API Key de acesso ao Pingdom");
	public static final EnumConfigPingdomProjudi pingdomApplicationNameProjudi = new EnumConfigPingdomProjudi("pingdomApplicationNameProjudi","Application Name Projudi");
	
	public static EnumConfigPingdomProjudi[] Todos = {pingdomURL, 
		                                       pingdomUserName, 
		                                       pingdomPassword, 
		                                       pingdomAPIKey,
		                                       pingdomApplicationNameProjudi};
	
	
	private String Id;
	private String Valor;
	
	private EnumConfigPingdomProjudi(String Id, 
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
	
	public static EnumConfigPingdomProjudi getEnum(String Id)
	{
		if (Id == null) return null;
		
		for (EnumConfigPingdomProjudi config : Todos) {
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
