package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.ps.VotanteTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class VotanteTipoNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 598186473294754734L;
	protected  VotanteTipoDt obDados;
	
	public VotanteTipoNe() {		

		obLog = new LogNe(); 

		obDados = new VotanteTipoDt(); 

	}
	
	public VotanteTipoDt consultarVotanteTipoCodigo(String votanteTipoCodigo) throws Exception {
		VotanteTipoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotanteTipoPs obPersistencia = new VotanteTipoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarProcessoParteTipoCodigo(votanteTipoCodigo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;		
	}
	//lrcampos 10/07/2019 * buscar id votante passando o codigo.
	public Integer consultarVotanteTipoId(String votanteTipoCodigo) throws Exception {
		Integer dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotanteTipoPs obPersistencia = new VotanteTipoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarVotanteTipoId(votanteTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;		
	}

}
