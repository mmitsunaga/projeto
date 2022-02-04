package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PropriedadeDt;
import br.gov.go.tj.projudi.ps.PropriedadePs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class PropriedadeNe extends PropriedadeNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 7421361143364795003L;

    //---------------------------------------------------------
	public  String Verificar(PropriedadeDt dados ) {

		String stRetorno="";

		if (dados.getPropriedadeCodigo().length()==0)
			stRetorno += "O Campo PropriedadeCodigo é obrigatório.";
		////System.out.println("..nePropriedadeVerificar()");
		return stRetorno;

	}
	
	//---------------------------------------------------------
    public void salvar(PropriedadeDt dados ) throws Exception {

        LogDt obLogDt;
        FabricaConexao obFabricaConexao =null;

        ////System.out.println("..nePropriedadesalvar()");
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
            PropriedadePs obPersistencia = new PropriedadePs(obFabricaConexao.getConexao());
            /* use o iu do objeto para saber se os dados ja estão ou não salvos */
            if (dados.getId().equalsIgnoreCase("" ) ) {            
                obPersistencia.inserir(dados);
                obLogDt = new LogDt("Propriedade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
            }else {            
                obPersistencia.alterar(dados);
                obLogDt = new LogDt("Propriedade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
            }

            obDados.copiar(dados);
            obLog.salvar(obLogDt, obFabricaConexao);
            obFabricaConexao.finalizarTransacao();
            
            ProjudiPropriedades.getPropriedades();

        }catch(Exception e){
            obFabricaConexao.cancelarTransacao();
            throw e;
        }finally{
            obFabricaConexao.fecharConexao();
        }
    }
	
    public List<PropriedadeDt> getPropriedades() throws Exception {
        List<PropriedadeDt> tempList=null;
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PropriedadePs obPersistencia = new PropriedadePs(obFabricaConexao.getConexao());                
            tempList=obPersistencia.getPropriedades();
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return tempList;   
    }
    
	public PropriedadeDt consultarCodigo(String codigo) throws Exception {

		PropriedadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PropriedadePs obPersistencia = new PropriedadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarCodigo(codigo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	

}
