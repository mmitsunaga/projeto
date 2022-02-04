package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class MandadoTipoDt extends MandadoTipoDtGen{

private static final long serialVersionUID = 6688595871820414509L;
    
    public static final int CodigoPermissao = 176;
    
    public static final int LIMINAR = 1;
    
    public static final int EXECUCAO = 2;
    
    public static final int COMUM = 3; 
    
    public static final int ESPECIAL = 4;
    
    public static final int ORDEM_SERVICO = 5;
    
    public static final int TRIBUNAL_JURI = 6;
    
    public static final int ASSISTENCIA = 7;
    
    public static final int EXECUCAO_FISCAL = 8;
    
    public static final int PLANTAO = 9;
    
    private int quantidadeLocomocao = 0;
    
//    public static final int PRAZO_DIAS_MANDADO_ORDINARIO = 15;
////    public static final int CODIGO_MANDADO_ORDINARIO = 1;
//    
//    public static final int PRAZO_DIAS_MANDADO_URGENTE = 1;
////    public static final int CODIGO_MANDADO_URGENTE = 2;
//    
//    public static final int PRAZO_DIAS_MANDADO_PRIORITARIO = 7;
////    public static final int CODIGO_MANDADO_PRIORITARIO = 3;
    
    public static final Map<String, String> PRAZO_COMUM = new HashMap<String, String>(){
		private static final long serialVersionUID = -4843861640388500488L;
		{
    	 put("descricao", "Comum");
    	 put("codigo", "1");
//    	 put("prazoDias", "15");
    	 //Em atendimento ao BO 2020/10122, alterando o prazo para cumprimento de mandados.
    	 //Para embasamento jurídico, verificar descrição e assentamento do BO.
    	 put("prazoDias", "40");
    	}
    };
    
    public static final Map<String, String> PRAZO_URGENTE = new HashMap<String, String>(){
		private static final long serialVersionUID = 6059213718373121070L;
		{
    	 put("descricao", "Urgente");
    	 put("codigo", "2");
//    	 put("prazoDias", "1");
    	//Em atendimento ao BO 2020/10122, alterando o prazo para cumprimento de mandados.
    	//Para embasamento jurídico, verificar descrição e assentamento do BO.
    	 put("prazoDias", "2");
    	}
    };
    
    public static final Map<String, String> PRAZO_PRIORITARIO = new HashMap<String, String>(){
		private static final long serialVersionUID = 7405247268798894069L;
		{
    	 put("descricao", "Prioritário");
    	 put("codigo", "3");
//    	 put("prazoDias", "7");
    	//Em atendimento ao BO 2020/10122, alterando o prazo para cumprimento de mandados.
    	//Para embasamento jurídico, verificar descrição e assentamento do BO.
    	 put("prazoDias", "14");
    	}
    };
    
    public static final Map<String, String> PRAZO_ESPECIAL = new HashMap<String, String>(){
		private static final long serialVersionUID = 8293253281405839840L;
		{
    	 put("descricao", "Especial");
    	 put("codigo", "4");
    	 put("prazoDias", null);
    	}
    };
    
    public static final List< Map<String, String> > listaPrazo() {
    	List< Map<String, String> > lista = new ArrayList<>();
    	lista.add(PRAZO_COMUM);
    	lista.add(PRAZO_URGENTE);
    	lista.add(PRAZO_PRIORITARIO);
    	lista.add(PRAZO_ESPECIAL);
    	return lista;
    }
    
    public static final String getDiasPrazo(String codigo) {
    	if(codigo != null) {
    		if(codigo.equals(PRAZO_COMUM.get("codigo"))){
    			return PRAZO_COMUM.get("prazoDias");
    		}
    		if(codigo.equals(PRAZO_URGENTE.get("codigo"))){
    			return PRAZO_URGENTE.get("prazoDias");
    		}
    		if(codigo.equals(PRAZO_PRIORITARIO.get("codigo"))){
    			return PRAZO_PRIORITARIO.get("prazoDias");
    		}
    	}
    	return "0";
    }
    
//    private static final Map<Integer, Integer> mandadoPrazoMap = new HashMap<Integer, Integer>();
//    static {
//    	mandadoPrazoMap.put(1, PRAZO_DIAS_MANDADO_ORDINARIO);
//    	mandadoPrazoMap.put(2, PRAZO_DIAS_MANDADO_URGENTE);
//    	mandadoPrazoMap.put(3, PRAZO_DIAS_MANDADO_PRIORITARIO);
//    	mandadoPrazoMap.put(4, null);
//    }
    
//    public getCodigo
    
    public int getQuantidadeLocomocao() {
		return quantidadeLocomocao;
	}

	public void setQuantidadeLocomocao(int quantidadeLocomocao) {
		this.quantidadeLocomocao = quantidadeLocomocao;
	}
	
	public static final boolean isTipoEspecial(String codigoTipo) {
		 if(codigoTipo != null && codigoTipo.equals(PRAZO_ESPECIAL.get("codigo"))) {
			return true;
		 }
		return false;
	}
	
	public static final boolean isTipoExecucaoFiscal(String codigo) {
		 if(Funcoes.StringToInt(codigo) == EXECUCAO_FISCAL) {
			return true;
		 }
		return false;
	}
}