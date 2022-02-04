package br.gov.go.tj.projudi.util.enumeradoresSeguros;

import java.io.Serializable;

/**
 * 
 * Classe:     EnumConfigPingdom.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: TypeSafeEnum representando as propriedades das configurações de acesso ao pingdom   
 *             
 */

public class EnumSistemaPingdom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8526070415146061692L;
	
	public static final EnumSistemaPingdom projudi = new EnumSistemaPingdom(1,"PJD", "Processo Judicial", "https://projudi.tjgo.jus.br/");	
	public static final EnumSistemaPingdom pje = new EnumSistemaPingdom(2,"PJE", "Processo Judicial Eletrônico - PJe", "https://www.tjgo.jus.br/pje/login.seam");
	
	public static EnumSistemaPingdom[] Todos = {projudi, 
		                                        pje};
	
	
	private int Id;
	private String Valor;
	private String ValorCompleto;
	private String URL;
	
	private EnumSistemaPingdom(int Id, 
			                   String Valor,
			                   String ValorCompleto, 
			                   String URL)
	{
		this.Id = Id;
		this.Valor = Valor;
		this.ValorCompleto = ValorCompleto;
		this.URL = URL;
	}
	
	public String getId()
	{
		return String.valueOf(this.Id);
	}
	
	public String getValor()
	{
		return this.Valor;
	}
	
	public String getValorCompleto()
	{
		return this.ValorCompleto;
	}
	
	public String getURL()
	{
		return this.URL;
	}
	
	public static EnumSistemaPingdom getEnum(int Id)
	{
		for (EnumSistemaPingdom config : Todos) {
			if (config.Id == Id) return config;
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return this.Valor;
	}
	  
}
