package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.SpgMandadoParteDt;
import br.gov.go.tj.projudi.ps.SpgMandadoPartePs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class SpgMandadoParteNe extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5861022699623477618L;
	
	protected  SpgMandadoParteDt obDados;


	public SpgMandadoParteNe() {
		
		obLog = new LogNe(); 

		obDados = new SpgMandadoParteDt(); 

	}
	
	//---------------------------------------------------------
	public void salvar(SpgMandadoParteDt dados, int tipo, FabricaConexao obFabricaConexao , FabricaConexao obFabricaConexaoADABAS ) throws MensagemException, Exception {
		LogDt obLogDt;
		SpgMandadoPartePs obPersistencia = new SpgMandadoPartePs(obFabricaConexaoADABAS.getConexao(), tipo);
		obPersistencia.inserir(dados);
		obLogDt = new LogDt("SpgMandadoParte", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}

}
