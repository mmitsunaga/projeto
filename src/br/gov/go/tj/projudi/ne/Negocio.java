package br.gov.go.tj.projudi.ne;

import java.io.Serializable;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;


/**
 * Classe abstrata de Negócio
 */
public abstract class Negocio implements Serializable {

    private static final long serialVersionUID = 1248112563870802054L;
    protected LogNe obLog=null;
    protected long QuantidadePaginas = 0;
    
    /**
     * 
     * @return
     */
    public long getQuantidadePaginas(){
        
    	return QuantidadePaginas;
        
    } //fim do método getQuantidadePaginas
    
    protected void SalvarLogNoConsole(UsuarioDt usuarioDt, String mensagem) {    	
    	String nomeDoMetodo = Thread.currentThread().getStackTrace()[2].getMethodName(); //Método que chamou
    	String nomeDaClasse = Thread.currentThread().getStackTrace()[2].getClassName(); //Classe que chamou
    	String dataHoraAtual = new TJDataHora().getDataHoraFormatadaaaaa_MM_ddHHmmss(); //Data e hora
    	String prefixo = String.format("%s.%s - %s - %s :", nomeDaClasse, nomeDoMetodo, usuarioDt.getIpComputadorLog(), dataHoraAtual);
    	
    	System.out.println(prefixo + Funcoes.retirarAcentos(mensagem));
    }    

    protected void setQuantidadePaginas(List lista) {
		QuantidadePaginas=0;
		if (lista!=null && !lista.isEmpty()) {
			QuantidadePaginas = (Long) lista.get(lista.size() - 1);
			lista.remove(lista.size() - 1);
		}
	}
    
} //fim da classe Negocio
