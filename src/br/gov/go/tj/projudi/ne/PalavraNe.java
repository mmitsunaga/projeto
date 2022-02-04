package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.PalavraDt;
import br.gov.go.tj.projudi.ps.PalavraPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class PalavraNe extends PalavraNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 4196656065619008459L;

    //---------------------------------------------------------
	public  String Verificar(PalavraDt dados ) {

		String stRetorno="";

		if (dados.getPalavra().length()==0)
			stRetorno += "O Campo Palavra é obrigatório.";
		////System.out.println("..nePalavraVerificar()");
		return stRetorno;

	}

    public PalavraDt buscarSalvar(String palavra, FabricaConexao obFabricaConexao) throws Exception {
            PalavraDt tempPalavraDt = new PalavraDt();
            PalavraPs obPersistencia = null;
            ////System.out.println("..nePalavrabuscar()");
            try{
               
                obPersistencia = new PalavraPs(obFabricaConexao.getConexao());
                //mando buscar se não encontra, mando salvar
                tempPalavraDt = consultarDescricao(palavra, obFabricaConexao);                

            }catch(Exception e){
                //se houver esse primeiro, foi pq a palavra já existia
            	tempPalavraDt.setPalavra(palavra);
                obPersistencia.inserir(tempPalavraDt);
            }
            return tempPalavraDt;
     }

    private PalavraDt consultarDescricao(String palavra, FabricaConexao obFabricaConexao) throws Exception {
        
        PalavraDt dtRetorno=null;
        ////System.out.println("..ne-consultarDescricao" );

        PalavraPs obPersistencia = new PalavraPs(obFabricaConexao.getConexao());
        dtRetorno= obPersistencia.consultarDescricao(palavra ); 
        obDados.copiar(dtRetorno);
        
        return dtRetorno;
    }
}
