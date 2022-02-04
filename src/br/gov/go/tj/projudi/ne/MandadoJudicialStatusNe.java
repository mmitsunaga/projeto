package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.ps.MandadoJudicialStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class MandadoJudicialStatusNe extends MandadoJudicialStatusNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -6613737671960373861L;

    //---------------------------------------------------------
	public  String Verificar(MandadoJudicialStatusDt dados ) {

		String stRetorno="";

		if (dados.getMandadoJudicialStatus().length()==0)
			stRetorno += "O Campo MandadoJudicialStatus é obrigatório.";
		/*if (dados.getMandadoJudicialStatusCodigo().length()==0)
			stRetorno += "O Campo MandadoJudicialStatusCodigo é obrigatório.";*/
		////System.out.println("..neMandadoJudicialStatusVerificar()");
		return stRetorno;

	}
	
	/**
	 * Consultar todos os status possíveis de mandado judicial.
	 * @param List listaMandadoStatusExcluidos
	 * @return List
	 * @throws Exception
	 */
	public List consultarListaStatus(List listaMandadoStatusExcluir) throws Exception {
		List listaStatusMandadoJudicial = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialStatusPs obPersistencia = new MandadoJudicialStatusPs(obFabricaConexao.getConexao());
			
			listaStatusMandadoJudicial = obPersistencia.consultarListaStatus(listaMandadoStatusExcluir);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaStatusMandadoJudicial;
	}
	public List consultarListaStatus() throws Exception {
		List listaStatusMandadoJudicial = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			MandadoJudicialStatusPs obPersistencia = new MandadoJudicialStatusPs(obFabricaConexao.getConexao());
			
			listaStatusMandadoJudicial = obPersistencia.consultarListaStatus();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaStatusMandadoJudicial;
	}
	
	
	public String consultarCodigo(String codigo, FabricaConexao fabConexao) throws Exception {

		String retorno = "";
            
		MandadoJudicialStatusPs obPersistencia = new MandadoJudicialStatusPs(fabConexao.getConexao());
		retorno = obPersistencia.consultarCodigo(codigo); 
		
		return retorno;
	}

}
