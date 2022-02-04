package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public enum QuantidadeDias {
	UM(1,"VIEW_DISTRIBUIDOS_1DIA", "Ontem"), SETE(7, "VIEW_DISTRIBUIDOS_7DIAS", "Sete dias"), TRINTA(30, "VIEW_DISTRIBUIDOS_30DIAS", "Um Mês"), CENTO_OITENTA_DOIS(182, "VIEW_DISTRIBUIDOS_182DIAS","Seis Meses"), TREZENTOS_SESSENTA_CINCO(365, "VIEW_DISTRIBUIDOS_365DIAS","Um Ano") ;
	int Dias;
	String View;
	String Descricao;
	
	QuantidadeDias(int dias, String view, String descricao){
		Dias=dias;
		View=view;
		Descricao = descricao;
	}
	public int getDias() {
		return Dias;
	}
	public String getView() {
		return View;
	}
	public String getDescricao() {
		return Descricao;
	}
	
	static public QuantidadeDias getQuantidadeDias(String dias) {
		int inDias = Funcoes.StringToInt(dias);
		for(QuantidadeDias temp :QuantidadeDias.values()) {
			if(inDias==temp.getDias()) {
				return temp;
			}
		}
		return null;
	}
}
