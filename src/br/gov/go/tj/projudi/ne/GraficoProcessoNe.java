package br.gov.go.tj.projudi.ne;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.relatorios.GraficoProcessoDt;
import br.gov.go.tj.projudi.ps.GraficoProcessoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Relatorios;

public class GraficoProcessoNe extends Negocio {

	/**
     * 
     */
    private static final long serialVersionUID = 7712921934755085258L;
        
	protected LogNe obLog;
	protected GraficoProcessoDt obDados;
	protected String stUltimaConsulta = "%";
	protected long QuantidadePaginas = 0;

	/**
	 * Construtor. Inicializa os atributos
	 */
	public GraficoProcessoNe() {
		
		obLog = new LogNe();
		obDados = new GraficoProcessoDt();
	}

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}

	/**
	 * M�todo que consulta comarca pela descri��o
	 * 
	 * @param tempNomeBusca
	 *            descri��o da comarca
	 * @param posicaoPaginaAtual
	 *            posi��o da p�gina do jsp
	 * @return lista de comarcas
	 * @throws Exception
	 * @author asrocha
	 */
	public List consultarDescricaoComarca(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		ComarcaNe Comarcane = new ComarcaNe();
		tempList = Comarcane.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = Comarcane.getQuantidadePaginas();
		Comarcane = null;
		
		return tempList;
	}

	/**
	 * M�todo que realiza consulta de serventias pela descri��o
	 * 
	 * @param tempNomeBusca
	 *            nome determinado na busca
	 * @param posicaoPaginaAtual
	 *            posi��o atual da busca
	 * @return lista de serventias
	 * @throws Exception
	 * @author asrocha
	 */
	public List consultarDescricaoServentia(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		ServentiaNe serventiaNe = new ServentiaNe();
		tempList = serventiaNe.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = serventiaNe.getQuantidadePaginas();
		serventiaNe = null;
		
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * 
	 * @param tempNomeBusca
	 *            nome que se quer consultar
	 * @param posicaoPaginaAtual
	 *            n�mero da p�gina
	 * @return lista de itens de produtividade
	 * @throws Exception
	 * @author asrocha
	 */
	public List consultarItemProdutividade(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		EstatisticaProdutividadeItemNe neObjeto = new EstatisticaProdutividadeItemNe();
		
		tempList = neObjeto.consultarEstatisticaProdutividadeItem(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * 
	 * @param diretorioProjeto
	 * @param anoInicial
	 * @param mesInicial
	 * @param anoFinal
	 * @param mesFinal
	 * @param idComarca
	 * @param usuarioSistema
	 * @return
	 * @throws Exception
	 */
	public byte[] graficoProcessoComarca(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idComarca, String usuarioSistema, String comarca) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GraficoProcessoPs obPersistencia = new GraficoProcessoPs(obFabricaConexao.getConexao());

			List listaProcessos = obPersistencia.graficoProcessoComarca(anoInicial, mesInicial, anoFinal, mesFinal, idComarca);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "graficoProcessoComarca";
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
			parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
			parametros.put("nomeSolicitante", usuarioSistema);
			parametros.put("comarca", comarca);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}

	/**
	 * M�todo que gera o gr�fico por serventia
	 * 
	 * @param diretorioProjeto
	 *            caminho para o projeto
	 * @param anoInicial
	 *            ano de in�cio do gr�fico
	 * @param mesInicial
	 *            m�s de in�cio do gr�fico
	 * @param anoFinal
	 *            ano de fim do gr�fico
	 * @param mesFinal
	 *            m�s de fim do gr�fico
	 * @param idServentia
	 *            n�mero de identifica��o da serventia
	 * @param usuarioSistema
	 *            usu�rio que est� solicitando o gr�fico
	 * @param serventia
	 *            nome da serventia
	 * @return um array de bytes com o documento pdf
	 * @throws Exception
	 * @author asrocha
	 */
	public byte[] graficoProcessoServentia(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idServentia, String usuarioSistema, String serventia) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GraficoProcessoPs obPersistencia = new GraficoProcessoPs(obFabricaConexao.getConexao());

			List listaProcessos = obPersistencia.graficoProcessoServentia(anoInicial, mesInicial, anoFinal, mesFinal, idServentia);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "graficoProcessoServentia";
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
			parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
			parametros.put("nomeSolicitante", usuarioSistema);
			parametros.put("serventia", serventia);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * M�todo que gera o gr�fico por item de produtividade, por comarca ou serventia
	 * @param diretorioProjeto
	 * @param anoInicial
	 * @param mesInicial
	 * @param anoFinal
	 * @param mesFinal
	 * @param idComarca
	 * @param idServentia
	 * @param comarca
	 * @param serventia
	 * @param usuarioSistema
	 * @param graficoProcessoDt
	 * @return um array de bytes com o gr�fico em pdf
	 * @throws Exception
	 */
	public byte[] graficoProcessoItemProdutividade(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idComarca, String idServentia, String comarca, String serventia, String usuarioSistema, GraficoProcessoDt graficoProcessoDt) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GraficoProcessoPs obPersistencia = new GraficoProcessoPs(obFabricaConexao.getConexao());

			List listaProcessos = obPersistencia.graficoProcessoItemProdutividade(anoInicial, mesInicial, anoFinal, mesFinal, idComarca, idServentia, graficoProcessoDt);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "graficoProcessoItemProdutividade";
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
			parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
			parametros.put("nomeSolicitante", usuarioSistema);
			parametros.put("comarca", comarca);
			parametros.put("serventia", serventia);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ServentiaNe serventiaNe = new ServentiaNe();
		stTemp = serventiaNe.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		serventiaNe = null;
		
		return stTemp;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe();
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		Comarcane = null;
		
		return stTemp;
	}
}
