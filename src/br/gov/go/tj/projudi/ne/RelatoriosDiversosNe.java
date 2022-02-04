package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.ResultadoRelatorioDt;
import br.gov.go.tj.projudi.ps.RelatorioDiversosPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class RelatoriosDiversosNe extends Negocio {

	private static final long serialVersionUID = 676925224023146322L;

	public ResultadoRelatorioDt consultarRelatorioDeIntimacoesPendentes(UsuarioNe usuarioSessao) throws Exception {
		FabricaConexao obFabricaConexao = FabricaConexao.criarConexaoPersistencia();
		try{
			return consultarRelatorioDeIntimacoesPendentes(usuarioSessao, obFabricaConexao);			
		} finally {
			FabricaConexao.fecharConexao(obFabricaConexao);
		}
	}
	
	public ResultadoRelatorioDt consultarRelatorioDeIntimacoesPendentes(UsuarioNe usuarioSessao, FabricaConexao obFabricaConexao) throws Exception {
		ResultadoRelatorioDt relatorio = null;
		RelatorioDiversosPs relatorioDiversosPs = new RelatorioDiversosPs(obFabricaConexao.getConexao());
		switch (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoTipoCodigo())) {
			case GrupoTipoDt.COORDENADOR_PROMOTORIA:
				relatorio = relatorioDiversosPs.consultarRelatorioDeIntimacoesPendentesCoordenadorPromotoria(usuarioSessao);
			break;
		default:
			throw new MensagemException("Relatório não criado para esse grupo.");
		}
		return relatorio;
	}
}
