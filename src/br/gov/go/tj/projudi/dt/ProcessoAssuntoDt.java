package br.gov.go.tj.projudi.dt;

import java.util.List;

public class ProcessoAssuntoDt extends ProcessoAssuntoDtGen{

    private static final long serialVersionUID = 3604348878467251026L;
    public static final int CodigoPermissao=2897;
    
    private String Assunto_Codigo;
    private String IdCNJAssunto;
    protected String IsAtivo; 
    
	public String getAssuntoCodigo()  {return Assunto_Codigo;}
	public void setAssuntoCodigo(String valor ) {if (valor!=null) Assunto_Codigo = valor;}
	
	public String getIsAtivo()  {return IsAtivo;}
	public void setIsAtivo(String valor ) {if (valor!=null) IsAtivo = valor;}
	
	public String getIdCNJAssunto()  {return IdCNJAssunto;}
	public void setIdCNJAssunto(String valor ) {if (valor!=null) IdCNJAssunto = valor;}
    
    /**
     * Método que transforma a lista de ProcessoAssuntoDt em uma String contendo os Assuntos.
     * @param listaProcessoAssunto - lista de ProcessoAssuntoDt
     * @return string com os Assuntos
     * @author hmgodinho
     */
    public String getListaAssuntosToString (List listaProcessoAssunto) {
    	String listaAssuntos = "";
    	if (listaProcessoAssunto!=null){
	    	for (int i = 0; i < listaProcessoAssunto.size(); i++) {
	    		if(listaAssuntos.length() > 0) {
	    			listaAssuntos += "; ";
	    		}
	    		ProcessoAssuntoDt processoAssunto = (ProcessoAssuntoDt) listaProcessoAssunto.get(i);
	    		listaAssuntos += processoAssunto.getAssuntoCNJ();
			}
    	}
    	return listaAssuntos;
    }
    
    public boolean isProcessoAssunto(){
		if (this.getId_Assunto() != null && this.getId_Assunto().length() > 0){
			return true;
		}
		
		return false;
	}
    
    public String getAssuntoCNJ() {
    	String assuntoToString = "";
    	if (IdCNJAssunto != null && IdCNJAssunto.trim().length() > 0) assuntoToString += this.getIdCNJAssunto().trim() + " - ";
    	assuntoToString += this.getAssunto();
    	return assuntoToString;
    }
}
