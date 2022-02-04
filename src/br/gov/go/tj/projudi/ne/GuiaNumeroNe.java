package br.gov.go.tj.projudi.ne;

import java.util.Date;

import br.gov.go.tj.projudi.dt.GuiaNumeroDt;
import br.gov.go.tj.projudi.ps.GuiaNumeroPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class GuiaNumeroNe extends Negocio {
	
	private static final long serialVersionUID = -460834584602281037L;	
		

	public  String Verificar(GuiaNumeroDt dados ) {

		String stRetorno="";

		if (dados.getLocalizador().length()==0)
			stRetorno += "O Campo Localizador é obrigatório.";
		return stRetorno;

	}
	
	   public String gerarNumero() throws Exception{	        
	        GuiaNumeroDt dados = null;
	        FabricaConexao obFabricaConexao = null;	        
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	           
	            GuiaNumeroPs obPersistencia = new GuiaNumeroPs(obFabricaConexao.getConexao());
	            /* use o iu do objeto para saber se os dados ja estão ou não salvos */
	            dados = new GuiaNumeroDt( Funcoes.DataHora(new Date()));
	            obPersistencia.inserir(dados);	       	           	                  	        

	        } finally {
	            obFabricaConexao.fecharConexao();
	        }
	        return dados.getId();
	    }
	   
       public void limparTabela() throws Exception {          
           
           FabricaConexao obFabricaConexao = null;         
           try{
               obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);              
               GuiaNumeroPs obPersistencia = new GuiaNumeroPs(obFabricaConexao.getConexao());
                              
               obPersistencia.limparTabela();                                                  

           } finally {
               obFabricaConexao.fecharConexao();
           }
           
       }
	   
       public void reiniciarTabela() throws Exception {          
           
           FabricaConexao obFabricaConexao = null;         
           try{
               obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);              
               GuiaNumeroPs obPersistencia = new GuiaNumeroPs(obFabricaConexao.getConexao());
                              
               obPersistencia.reiniciarTabela();                                                  

           } finally {
               obFabricaConexao.fecharConexao();
           }
           
       }
}