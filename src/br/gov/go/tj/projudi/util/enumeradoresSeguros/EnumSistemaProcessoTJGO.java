package br.gov.go.tj.projudi.util.enumeradoresSeguros;

import java.io.Serializable;

/**
 * 
 * Classe:     EnumSistemaProcessoTJGO.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       27/03/2015
 * Finalidade: TypeSafeEnum representando os sistemas de processos   
 *             
 */

public class EnumSistemaProcessoTJGO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8526070415146061692L;
	
	public static final EnumSistemaProcessoTJGO projudi = new EnumSistemaProcessoTJGO(1,"PROJUDI", "Processo Judicial Digital - PROJUDI");	
	public static final EnumSistemaProcessoTJGO pje = new EnumSistemaProcessoTJGO(2,"PJE", "Processo Judicial Eletrônico - PJe");
	public static final EnumSistemaProcessoTJGO spg = new EnumSistemaProcessoTJGO(3,"SPG/SSG", "Sistema de Primeiro Grau / Sistema de Segundo Grau");
	
	public static EnumSistemaProcessoTJGO[] Todos = {projudi, 
		                                        pje,
		                                        spg};
	
	
	private int Id;
	private String Valor;
	private String ValorCompleto;	
	
	private EnumSistemaProcessoTJGO(int Id, 
			                   String Valor,
			                   String ValorCompleto)
	{
		this.Id = Id;
		this.Valor = Valor;
		this.ValorCompleto = ValorCompleto;		
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
	
	public static EnumSistemaProcessoTJGO getEnum(int Id)
	{
		for (EnumSistemaProcessoTJGO config : Todos) {
			if (config.Id == Id) return config;
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return this.Valor;
	}
	
	@Override
	public boolean equals(Object novoObjeto) {
		if (novoObjeto == null) return false;
		
		EnumSistemaProcessoTJGO sistema = null;

		sistema = (EnumSistemaProcessoTJGO) novoObjeto;

		
		return this.Id == sistema.Id;
	}	  
}
