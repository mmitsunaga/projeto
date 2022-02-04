package br.gov.go.tj.projudi.ne;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioPostagemECartasDt;
import br.gov.go.tj.projudi.ps.RelatorioPostagemECartasPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * 
 * @author mmitsunaga
 *
 */
public class RelatorioPostagemECartasNe extends Negocio {
	
	private static final long serialVersionUID = -3473427180025342464L;

	public RelatorioPostagemECartasNe() {
		obLog = new LogNe(); 
	}
	
	/**
	 * 
	 * @param diretorioProjeto
	 * @param dataInicial
	 * @param dataFinal
	 * @param idServentia
	 * @param usuarioResponsavelRelatorio
	 * @return
	 */
	public byte[] emitirRelatorioPostagemECartas(String diretorioProjeto, String dataInicial, String dataFinal, String idComarca, String idServentia, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioPostagemECartasPs obPersistencia = new RelatorioPostagemECartasPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "relatorioAnaliticoPostagemECarta";

			List<RelatorioPostagemECartasDt> dados = obPersistencia.consultarRelatorioPostagemECarta(dataInicial, dataFinal, idComarca, idServentia);
			
			if (dados.isEmpty()) {
				return null;
			}

			// PARÂMETROS DO RELATÓRIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relatório Analítico de Postagem [E-Cartas]");
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			parametros.put("mostrarSummary", ValidacaoUtil.isVazio(idServentia));
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, dados);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return temp;
		
	}
	
	/**
	 * Consulta as comarcas disponiveis e retorna o JSON
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 */
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ComarcaNe neObjeto = new ComarcaNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
		
	/**
	 * Consulta as serventias ativas do tipo VARA de uma comarca específica
	 * @param descricao
	 * @param posicao
	 * @param idComarca
	 * @param serventiaTipoCodigo
	 * @return
	 * @throws Exception
	 */
	public String consultarServentiasAtivasComarcaJSON(String descricao, String posicao, String idComarca, String serventiaTipoCodigo) throws Exception {
		String stTemp = "";		
		ServentiaNe serventiaNe = new ServentiaNe();
		stTemp = serventiaNe.consultarServentiasAtivasJSON(descricao, posicao, serventiaTipoCodigo, idComarca, null);
		return stTemp;
	}
	
	
	/**
	 * 
	 * @param codigoRastreamento
	 * @return
	 * @throws Exception
	 */
	public RelatorioPostagemECartasDt consultarDadosProcessoPorCodigoRastreamento(String codigoRastreamento) throws Exception {
		FabricaConexao obFabricaConexao = null;
		RelatorioPostagemECartasDt dado = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioPostagemECartasPs obPersistencia = new RelatorioPostagemECartasPs(obFabricaConexao.getConexao());
			dado = obPersistencia.consultarDadosProcessoPorCodigoRastreamento(codigoRastreamento);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dado;
	}
	
}
