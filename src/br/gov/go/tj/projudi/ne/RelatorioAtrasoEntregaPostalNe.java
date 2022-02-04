package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioAtrasoEntregaPostalDt;
import br.gov.go.tj.projudi.ps.RelatorioAtrasoEntregaPostalPs;
import br.gov.go.tj.utils.FabricaConexao;

/**
 * 
 * @author mmitsunaga
 *
 */
public class RelatorioAtrasoEntregaPostalNe extends Negocio {

	private static final long serialVersionUID = -1978442479215597805L;

	public RelatorioAtrasoEntregaPostalNe() {
		obLog = new LogNe(); 
	}
	
	/**
	 * Consulta os processos com pendência de correios entregue e que ainda não possuem o recebimento de AR.
	 * Obtém a quantidade de dias em espera, da data de entrega aos dias atuais.
	 * @param idComarca
	 * @param idServentia
	 * @param diasEspera
	 * @return
	 * @throws Exception
	 */
	public List<RelatorioAtrasoEntregaPostalDt> consultarAtrasoEntregaPostal(String idComarca, String idServentia, String diasEspera) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<RelatorioAtrasoEntregaPostalDt> dados = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioAtrasoEntregaPostalPs obPersistencia = new RelatorioAtrasoEntregaPostalPs(obFabricaConexao.getConexao());
			dados = obPersistencia.consultarAtrasoEntregaPostal(idComarca, idServentia, diasEspera);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dados;
	}
	
	/**
	 * Cria um mapa ordenado (TreeMap) onde cada chave é uma serventia e o value, a lista de registros
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<RelatorioAtrasoEntregaPostalDt>> agruparProcessosComAtrasoEntregaPostal(List<RelatorioAtrasoEntregaPostalDt> lista) throws Exception {
		Map<String, List<RelatorioAtrasoEntregaPostalDt>> mapa = new TreeMap<>();
		for (RelatorioAtrasoEntregaPostalDt oItem : lista){
			if (!mapa.containsKey(oItem.getServentia())){
				mapa.put(oItem.getServentia(), new ArrayList<RelatorioAtrasoEntregaPostalDt>());
			}
			List<RelatorioAtrasoEntregaPostalDt> values = mapa.get(oItem.getServentia());
			values.add(oItem);
		}
		return mapa;
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

	
}
