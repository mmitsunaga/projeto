package br.gov.go.tj.projudi.dt;

import org.json.JSONException;
import org.json.JSONObject;

public class ModeloDt extends ModeloDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 6630707346189437884L;
    public static final int CodigoPermissao = 166;

    public static final int MANDADO_PRISAO = 148;
    public static final int NARRATIVA_MODELO_CODIGO = 162;
	public static final int INTERDICAO_MODELO_CODIGO = 163;
	public static final int LICITACAO_MODELO_CODIGO = 164;
	public static final int AUTORIA_MODELO_CODIGO = 165;
	public static final int PRATICA_FORENSE_QUANTITATIVA_MODELO_CODIGO = 141;
	public static final int PRATICA_FORENSE_DESCRITIVA_MODELO_CODIGO = 140;
	public static final int PRATICA_FORENSE_NEGATIVA_MODELO_CODIGO = 142;
	public static final int NARRATIVA_PUBLICA_MODELO_CODIGO = 166;
	public static final int QTD_1_LOCOMOCAO_CODIGO = 1;
	public static final int QTD_2_LOCOMOCAO_CODIGO = 2;
	public static final int QTD_3_LOCOMOCAO_CODIGO = 3;
	public static final int QTD_4_LOCOMOCAO_CODIGO = 4;
	
	public String qtdLocomocao;
    

	public String getQtdLocomocao() {
		return qtdLocomocao;
	}

	public void setQtdLocomocao(String qtdLocomocao) {
		this.qtdLocomocao = qtdLocomocao;
	}

	public String getCriador() {
		if (getServentia() != null && !getServentia().equals("")) return getServentia();
		else if (getUsuarioServentia() != null && !getUsuarioServentia().equals("")) return getUsuarioServentia();
		else return "Genérico";
	}
	
	public JSONObject getJSON() throws JSONException {
		JSONObject json = new JSONObject();		
		json.put("id", this.getId());
		json.put("modelo", this.getModelo());	
		json.put("texto", this.getTexto());			
		return json;		
	}

	public void copiar(ModeloDt objeto){
		 if (objeto==null) return;
		super.copiar(objeto);
		qtdLocomocao = objeto.getQtdLocomocao();
	}

	public void limpar(){
		super.limpar();
		qtdLocomocao = "";
	}
	
}
