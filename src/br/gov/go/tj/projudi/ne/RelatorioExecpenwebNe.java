package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.ps.RelatorioExecpenwebPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.pdf.InterfaceJasper;

/**
 * @author wcsilva
 */
public class RelatorioExecpenwebNe {
	
	protected LogNe obLog;
	protected long QuantidadePaginas = 0;

	/**
	 * Construtor
	 */
	public RelatorioExecpenwebNe() {
		obLog = new LogNe(); 
	}

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
	
	public byte[] imprimirRelatorio(String diretorioProjetos, String usuario, List lista, String titulo, String nomeJasper, String tituloDataBeneficio, String observacao) throws Exception{
		byte[] temp = null;
		ByteArrayOutputStream baos = null;

		try{
			if (lista != null && lista.size() > 0){
				String pathJasper = diretorioProjetos + "WEB-INF" + File.separator + "relatorios" + File.separator + "processoExecucao" + File.separator + nomeJasper;
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", diretorioProjetos + "imagens" + File.separator + "logoEstadoGoias02.jpg");
				parametros.put("titulo", titulo);
				parametros.put("usuario", usuario);
				parametros.put("observacao", observacao);
				if (tituloDataBeneficio.length() > 0)
					parametros.put("tituloDataBeneficio", tituloDataBeneficio);
				
				InterfaceJasper ei = new InterfaceJasper(lista);
				JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);
				JRPdfExporter jr = new JRPdfExporter();
				jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
				baos = new ByteArrayOutputStream();
				jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
				jr.exportReport();

				temp = baos.toByteArray();
				
			}
		
		} finally{
			if (baos != null) baos.close();
		}
		return temp;
	}

	public List consultarDadosRelatorio(int passoEditar, String idServentia) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);;
		try{
			RelatorioExecpenwebPs obPersistencia = new  RelatorioExecpenwebPs(obFabricaConexao.getConexao());
			switch (passoEditar){
				case 2:
					lista = obPersistencia.consultarTotalProcessoCalculoAtrasoSemCalculoServentia(idServentia);
					break;
				case 3:
//					lista = obPersistencia.consultarProcessoModalidade(idServentia);
					break;
				case 4:
					lista = obPersistencia.consultarProcessoRegime(idServentia);
					break;
				case 5:
					lista = obPersistencia.consultarTotalProcessoServentia(idServentia);
					break;
				case 6:
					lista = obPersistencia.consultarProcessoCalculoAtrasoSemCalculo(idServentia);
					break;
				case 7:
					lista = obPersistencia.consultarProcessoTerminoPenaAtraso(idServentia);
					break;
				case 8:
					lista = obPersistencia.consultarProcessoProgressaoAtraso(idServentia);
					break;
				case 9:
					lista = obPersistencia.consultarProcessoLivramentoAtraso(idServentia);
					break;
				case 10:
					lista = obPersistencia.consultarProcessoMandadoAtraso(idServentia);
					break;
				case 11:
					lista = obPersistencia.listarProcessoAtivoServentia(idServentia);
					break;
					
				case 12:
					lista = obPersistencia.listarProcessoEletronicoAtivoServentia(idServentia);
					break;
			}
			
			
		
		} finally{
            obFabricaConexao.fecharConexao();
        }
		return lista;
	}
	

	public List consultarTotalProcessoComCalculoPeriodo(String dataInicio, String dataFim, String idServentia) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);;
		try{
			RelatorioExecpenwebPs obPersistencia = new  RelatorioExecpenwebPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarTotalProcessoComCalculoPeriodo(dataInicio, dataFim, idServentia);
		
		} finally{
            obFabricaConexao.fecharConexao();
        }
		return lista;
	}
	
	public String consultarDescricaoServentiaJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioExecpenwebPs obPersistencia = new  RelatorioExecpenwebPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
//	public List consultarSituacaoAtual(String idLocalCumprimentoPena, String idRegime, String idSituacao, String idModalidade, String idServentia) throws Exception{
	public List consultarSituacaoAtual(List listaIdFormaCumprimento, List listaIdLocalCumprimentoPena, List listaIdRegime, List listaIdModalidade, List listaIdSituacao, String idServentia, boolean excluirPRD) throws Exception{
		List lista = null;
			lista = new RelatorioProcessoExecucaoNe().consultarSituacaoAtualProcessoExecucao(listaIdFormaCumprimento, listaIdLocalCumprimentoPena, listaIdRegime, listaIdSituacao, listaIdModalidade, "", idServentia, excluirPRD);
		
		return lista;
	}
	
	public List consultarDescricaoSituacaoProcessoExecucao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		EventoExecucaoStatusNe neObjeto = new EventoExecucaoStatusNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual, false);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	public List consultarDescricaoFormaCumprimentoExecucao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;

			tempList = new FormaCumprimentoExecucaoNe().consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoRegimeExecucao(String tempNomeBusca, String id_PenaExecucaoTipo, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		RegimeExecucaoNe neObjeto = new RegimeExecucaoNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, id_PenaExecucaoTipo, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoLocalCumprimentoPena(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		LocalCumprimentoPenaNe neObjeto = new LocalCumprimentoPenaNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	
	
	
}
