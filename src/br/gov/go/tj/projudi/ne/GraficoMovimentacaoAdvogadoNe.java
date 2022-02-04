package br.gov.go.tj.projudi.ne;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.GraficoMovimentacaoAdvogadoDt;
import br.gov.go.tj.projudi.ps.GraficoMovimentacaoAdvogadoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Relatorios;

public class GraficoMovimentacaoAdvogadoNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7486963952426645720L;
	
	protected LogNe obLog;
	protected GraficoMovimentacaoAdvogadoDt obDados;	
	protected long QuantidadePaginas = 0;

	/**
	 * Construtor. Inicializa os atributos
	 */
	public GraficoMovimentacaoAdvogadoNe() {
		
		obLog = new LogNe();
		obDados = new GraficoMovimentacaoAdvogadoDt();
	}

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
	
	public byte[] GeraRelatorioGrafico(String diretorioProjeto, GraficoMovimentacaoAdvogadoDt graficoMovimentacaoAdvogadoDt, String usuarioSistema) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GraficoMovimentacaoAdvogadoPs obPersistencia = new  GraficoMovimentacaoAdvogadoPs(obFabricaConexao.getConexao());			

			List listaProcessos = obPersistencia.graficoMovimentacoesAdvogado(graficoMovimentacaoAdvogadoDt);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "graficoMovimentacaoAdvogado";
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("dataAtual", new Date());
			parametros.put("dataAnalise", graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise().getDataFormatadaddMMyyyy());
			parametros.put("dataComparacao", graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao().getDataFormatadaddMMyyyy());
			parametros.put("nomeSolicitante", usuarioSistema);			

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}	
}