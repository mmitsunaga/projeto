package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.ps.ProcessoNumeroPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoNumeroNe extends ProcessoNumeroNeGen{

/**
     * 
     */
    private static final long serialVersionUID = 84166123385568036L;

	  public String gerarNumero() throws Exception{	        
	        
	        FabricaConexao obFabricaConexao = null;
	        String stNumero ="";
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	           
	            ProcessoNumeroPs obPersistencia = new ProcessoNumeroPs(obFabricaConexao.getConexao());
	            /* use o iu do objeto para saber se os dados ja estão ou não salvos */
	            //dados = new ProcessoNumeroDt( Funcoes.DataHora(new Date()));
	            //obPersistencia.inserir(dados);
	            stNumero =obPersistencia.pegarProximoNumero();
	        
	        }finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stNumero;
	    }	   
	   
       public void reiniciarSequencia() throws Exception {
                      
           FabricaConexao obFabricaConexao = null;         
           try{
               obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);              
               ProcessoNumeroPs obPersistencia = new ProcessoNumeroPs(obFabricaConexao.getConexao());
                              
               obPersistencia.reiniciarSequencia();
           } finally {
               obFabricaConexao.fecharConexao();
           }
       }
       

 }
