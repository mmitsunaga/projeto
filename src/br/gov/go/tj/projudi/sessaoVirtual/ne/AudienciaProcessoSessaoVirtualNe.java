package br.gov.go.tj.projudi.sessaoVirtual.ne;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ps.AudienciaProcessoPs;
import br.gov.go.tj.projudi.sessaoVirtual.ps.AudienciaProcessoSessaoVirtualPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AudienciaProcessoSessaoVirtualNe extends AudienciaProcessoNe{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4878049413991497536L;

	// lrcampos - 14/05/2020 10:07 - Busca Objeto AudienciaProcessoDt passando idAudienciaProcesso.
	
	public AudienciaProcessoDt consultarIdPJD(String id_AudienciaProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			if(id_AudienciaProcesso == null) return null;
			AudienciaProcessoDt audienciaProcessoDt = null;
			
			AudienciaProcessoSessaoVirtualPs obPersistencia = new  AudienciaProcessoSessaoVirtualPs(obFabricaConexao.getConexao());
			audienciaProcessoDt = obPersistencia.consultarIdPJD(id_AudienciaProcesso);
			
			return audienciaProcessoDt;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	
	// jvosantos - 04/06/2019 10:01 - Criação de um método salvar que permite a passagem de conexão
	public void salvar(AudienciaProcessoDt dados, FabricaConexao fabrica) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = (fabrica == null) ? new FabricaConexao(FabricaConexao.PERSISTENCIA) : fabrica;
		try{
			if(fabrica == null) obFabricaConexao.iniciarTransacao();
			AudienciaProcessoSessaoVirtualPs obPersistencia = new AudienciaProcessoSessaoVirtualPs(obFabricaConexao.getConexao());
			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AudienciaProcesso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AudienciaProcesso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			if(fabrica == null) obFabricaConexao.finalizarTransacao();

		}finally{
			if(fabrica == null) obFabricaConexao.fecharConexao();
		}
	}
}
