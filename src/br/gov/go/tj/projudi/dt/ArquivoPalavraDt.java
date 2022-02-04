package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ArquivoPalavraDt extends ArquivoPalavraDtGen implements Comparable {
    /**
     * 
     */
    private static final long serialVersionUID = 6040449282661464303L;
    private int inOrdenador=1;
    private String ArquivoTipo;
    private String Serventia;
    private String UsuarioAssinador;
    private String DataInsercao;
    
	public static final int CodigoPermissao=6943;
//

    public void incrementaOrdenador() {
        inOrdenador++;        
    }

 
    public String getArquivoTipo()  {return ArquivoTipo;}
    public void setArquivoTipo(String valor ) {if(valor!=null) ArquivoTipo = valor;}
    
    public String getServentia()  {return Serventia;}
    public void setServentia(String valor ) {if(valor!=null) Serventia = valor;}
    
    public String getUsuarioAssinador()  {return UsuarioAssinador;}
    public void setUsuarioAssinador(String valor ) {if(valor!=null) UsuarioAssinador = valor;}

    public String getDataInsercao() {return DataInsercao;}

	public void setDataInsercao(String valor) {if (valor != null)	DataInsercao = valor;}

	public int getOrdenador(){
        return inOrdenador;
    }
    
	public String getNomeArquivoFormatado() throws Exception{
	    return Funcoes.nomeArquivoFormatado(this.getNomeArquivo(), true);
	}
	
    public int compareTo(Object o) {
        
        return (((ArquivoPalavraDt)o).getOrdenador() - this.inOrdenador);
        
    }

}
