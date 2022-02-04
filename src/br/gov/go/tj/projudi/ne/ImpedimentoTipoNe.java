package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ImpedimentoTipoDt;
import br.gov.go.tj.projudi.ps.ImpedimentoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ImpedimentoTipoNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3429465494484075974L;
	protected  ImpedimentoTipoDt obDados;
	
	public ImpedimentoTipoNe() {		

		obLog = new LogNe(); 

		obDados = new ImpedimentoTipoDt(); 

	}
	
	public ImpedimentoTipoDt consultarImpedimentoTipoCodigo(String votanteTipoCodigo) throws Exception {
		ImpedimentoTipoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ImpedimentoTipoPs obPersistencia = new ImpedimentoTipoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarProcessoParteTipoCodigo(votanteTipoCodigo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;		
	}
	
	public void alterarImpedimentoTipo(String idServentiaCargo, String idAudienciaProcesso, int novoTipo) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			alterarImpedimentoTipo(idServentiaCargo, idAudienciaProcesso, novoTipo, fabrica);
		} finally {
			fabrica.fecharConexao();
		}
	}

	public void alterarImpedimentoTipo(String idServentiaCargo, String idAudienciaProcesso, int novoTipo,
			FabricaConexao fabrica) throws Exception {
		ImpedimentoTipoPs votoPs = new ImpedimentoTipoPs(fabrica.getConexao());
		votoPs.alterarImpedimentoTipo(idServentiaCargo, idAudienciaProcesso, novoTipo);
	}


}
