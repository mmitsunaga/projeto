package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.SpgMandadoDt;
import br.gov.go.tj.projudi.ps.SpgMandadoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class SpgMandadoNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4496682574546727465L;
	
	protected  SpgMandadoDt obDados;


	public SpgMandadoNe() {
		
		obLog = new LogNe(); 

		obDados = new SpgMandadoDt(); 

	}


//---------------------------------------------------------
	public void salvar(SpgMandadoDt dados, int tipo, FabricaConexao obFabricaConexao, FabricaConexao obFabricaConexaoADABAS  ) throws MensagemException, Exception {
		LogDt obLogDt;
		
		SpgMandadoPs obPersistencia = new SpgMandadoPs(obFabricaConexaoADABAS.getConexao(), tipo);
		obPersistencia.inserir(dados);
		obLogDt = new LogDt("SpgMandado", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	}

}
