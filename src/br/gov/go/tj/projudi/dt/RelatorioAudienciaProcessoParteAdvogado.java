package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelatorioAudienciaProcessoParteAdvogado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8804210789634544654L;
	
	private String nomeParte;
	private List nomesAdvogados;
	
	public RelatorioAudienciaProcessoParteAdvogado()
	{
		nomesAdvogados = new ArrayList();
	}
	
	public void setNomeParte(String nomeParte){
		this.nomeParte = nomeParte;
	}
	
	public String getNomeParte(){
		return "*" + this.nomeParte;
	}
	
	public void addNomeAdvogado(String nomeAdvogado)
	{
		nomesAdvogados.add(nomeAdvogado);
	}
	
	public String getNomesAdvogados()
	{
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < nomesAdvogados.size(); i++)
		{
			if (i!=0) str.append("\n");				
			str.append((String)nomesAdvogados.get(i));
		}
		return str.toString();
	}
	
	public String getParteAdvogado()
	{
		StringBuffer str = new StringBuffer();		
		str.append("*" + nomeParte);
		if (nomesAdvogados.size() > 0)
		{			
			str.append("\n ADV(S)  : ");
			for (int i = 0; i < nomesAdvogados.size(); i++)
			{
				if (i!=0) str.append("\n                 ");				
				str.append((String)nomesAdvogados.get(i));
			}
		}		
		return str.toString();
	}	

}
