package br.gov.go.tj.projudi.ne;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioImplantacaoDt;
import br.gov.go.tj.projudi.ps.RelatorioImplantacaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Relatorios;

/**
 * 
 * @author asrocha
 * 
 */
public class RelatorioImplantacaoNe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8542413852795446874L;
	protected LogNe obLog;
	protected RelatorioImplantacaoDt obDados;
	protected String stUltimaConsulta = "%";
	protected long QuantidadePaginas = 0;

	/**
	 * Construtor
	 */
	public RelatorioImplantacaoNe() {
		
		obLog = new LogNe();
		obDados = new RelatorioImplantacaoDt();
	}

	/**
	 * Método que consulta Comarca pela descrição
	 * 
	 * @param tempNomeBusca
	 *            - descrição da comarca
	 * @param PosicaoPaginaAtual
	 *            - posição da página do jsp
	 * @return lista de comarcas
	 * @throws Exception
	 */
	public List consultarDescricaoComarca(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		ComarcaNe Comarcane = new ComarcaNe();
		tempList = Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Comarcane.getQuantidadePaginas();
		Comarcane = null;
		
		return tempList;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	/**
	 * 
	 */
	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
	
	public List consultarServentiaTipoId(String id_ServentiaTipo ) throws Exception {
		List tempList=null;
		
		ServentiaTipoNe ServentiaTipone = new ServentiaTipoNe();
		tempList = ServentiaTipone.consultarServentiaTipoId(id_ServentiaTipo);
		
		return tempList;   
	}

	/**
	 * 
	 * @return
	 */
	public byte[] relImplantacaoServentias(String diretorioProjeto, String idComarca, String idServentiaTipo, String usuarioSistema) throws Exception {
		FabricaConexao obFabricaConexao = null;
		byte[] temp = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioImplantacaoPs obPersistencia = new RelatorioImplantacaoPs(obFabricaConexao.getConexao());

			List listaProcessos = obPersistencia.relImplantacaoServentias(idComarca, idServentiaTipo);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "serventiasImplantadas";

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("dataAtual", new Date());
			parametros.put("nomeSolicitante", usuarioSistema);
			parametros.put("titulo", "Serventias Implantadas");

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * 
	 * @param diretorioProjeto
	 * @return
	 * @throws Exception
	 * @author asrocha
	 */
	public byte[] relImplantacaoServentiasPublico(String diretorioProjeto) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioImplantacaoPs obPersistencia = new RelatorioImplantacaoPs(obFabricaConexao.getConexao());

			List lista = obPersistencia.relImplantacaoServentiasPublico();
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "serventiasImplantadasPublico";

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("dataAtual", new Date());
			parametros.put("titulo", "Serventias Implantadas");

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, lista);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public String consultarDescricaoServentiaTipoJSON(String tempNomeBusca)  throws Exception {
		String stTemp = "";
		ServentiaTipoNe neObjeto = new ServentiaTipoNe();

			stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca);
		neObjeto = null;
		return stTemp;
	}
}
