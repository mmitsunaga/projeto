package br.gov.go.tj.projudi.ne;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaProcessoServentiaDt;
import br.gov.go.tj.projudi.ps.EstatisticaProcessoServentiaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.pdf.InterfaceBeanJasper;

//---------------------------------------------------------
public class EstatisticaProcessoServentiaNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1753247136758278516L;
		
	protected LogNe obLog;
	protected EstatisticaProcessoServentiaDt obDados;
	protected String stUltimaConsulta ="%";
	protected long QuantidadePaginas = 0; 
	
	public EstatisticaProcessoServentiaNe() {		
		obLog = new LogNe(); 
		obDados = new EstatisticaProcessoServentiaDt(); 
	}
	
//---------------------------------------------------------
	public  String Verificar(EstatisticaProcessoServentiaDt dados ) {
		String stRetorno="";
		return stRetorno;
	}
	
	/**
	 * Método que retorna a quantidade de processos Ativos 
	 * @param idServenta e idProcessoTipo 
	 * @return A a quantidade de processos Ativos na Serventia com o tipo de processo passado ou todo os tipos se idProcessoTipo = ""
	 * @throws Exception
	 */
	public int consultaQtdProcessosAtivos(String idServentia, String idProcessoTipo) throws Exception {
		////System.out.println("..ne-consultaQtdProcessosAtivos");
		FabricaConexao obFabricaConexao = null;
		int qtd;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProcessoServentiaPs obPersistencia = new EstatisticaProcessoServentiaPs(obFabricaConexao.getConexao());
			qtd = obPersistencia.consultaQtdProcessosAtivos(idServentia, idProcessoTipo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return qtd;
	}
	
	/**
	 * MÉTODO QUE RETORNA A QUANTIDADE DE PROCESSOS DE 1º GRAU NUMA DETERMINADA SITUAÇÃO
	 * DE ACORDO COM OS PARAMETROS: PRIORIDADE, SEGREDO DE JUSTIÇA E ETC QUE ESTÃO NO OBJETO EstatisticaProcessoServentiaDt.
	 * @param estatisticaProcessoServentia 
	 * @return A QUANTIDADE DE PROCESSOS DE 1º GRAU NUMA DETERMINADA SITUAÇÃO
	 * @throws Exception
	 */
	public int consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		////System.out.println("..ne-consultaQtdProcessosEmSituacao");
		int qtd;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProcessoServentiaPs obPersistencia = new EstatisticaProcessoServentiaPs(obFabricaConexao.getConexao());
			qtd = obPersistencia.consultaQtdProcessosEmSituacao(estatisticaProcessoServentia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return qtd;
	}
	
	/**
	 * 	MÉTODO QUE CALCULA  A MÉDIA DE DIAS EM TRAMITAÇÃO DE PROCESSOS
	 * DE ACORDO COM OS PARAMETROS: PRIORIDADE, SEGREDO DE JUSTIÇA E ETC QUE ESTÃO NO OBJETO EstatisticaProcessoServentiaDt.
	 * @param estatisticaProcessoServentia 
	 * @return A QUANTIDADE DE PROCESSOS DE 1º GRAU NUMA DETERMINADA SITUAÇÃO
	 * @throws Exception
	 */
	public String mediaDiasEmTramitacao(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		////System.out.println("..ne-mediaDiasEmTramitacao");
		String qtd;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProcessoServentiaPs obPersistencia = new EstatisticaProcessoServentiaPs(obFabricaConexao.getConexao());
			qtd = obPersistencia.mediaDiasEmTramitacao(estatisticaProcessoServentia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return qtd;
	}
	
	/**
	 * Método que retorna a quantidade de processos Suspensos com Prazo ou sem prazo de acordo com o parametro SuspensoPrazo que esta em estatisticaProcessoServentia
	 * @param estatisticaProcessoServentia 
	 * @return A a quantidade de processos Suspensos com Prazo na Serventia com o tipo de processo passado ou todo os tipos se idProcessoTipo = ""
	 * @throws Exception
	 */
	public int consultaQtdProcessosSuspensos(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		////System.out.println("..ne-consultaQtdProcessosSuspensos");
		int qtd;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProcessoServentiaPs obPersistencia = new EstatisticaProcessoServentiaPs(obFabricaConexao.getConexao());
			qtd = obPersistencia.consultaQtdProcessosSuspensos(estatisticaProcessoServentia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return qtd;
	}
	
	/**
	 * Método que retorna a quantidade de processos Paralisados ou semi-paralisados
	 * @param estatisticaProcessoServentia 
	 * @return A a quantidade de processos processos Paralisados ou semi-paralisados na Serventia com o tipo de processo passado ou todo os tipos se idProcessoTipo = ""
	 * @throws Exception
	 */
	public int consultaQtdProcessosProcessosParalisados(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		////System.out.println("..ne-consultaQtdProcessosProcessosParalisados");
		int qtd;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProcessoServentiaPs obPersistencia = new EstatisticaProcessoServentiaPs(obFabricaConexao.getConexao());
			qtd = obPersistencia.consultaQtdProcessosProcessosParalisados(estatisticaProcessoServentia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return qtd;
	}
	
	/**
	 * Método que retorna o valor percentual, relaivo à parte em relação ao total.
	 * Internamente formata o valor para duas casas decimais
	 * @param parte Valor parte cujo percentual se deseja saber em relação ao total
	 * @param total Valor Total
	 * @return O percentual, num formato x.xx
	 */	
	public String getPercentual(long parte, long total){
		
		if (total== 0) return "0.00";
		return formataPara2CasasDecimais((parte *100) /total);
		
	}
	
	/**
	 * Método que formata o valor para duas casas decimais
	 * @param valor Valor que se deseja formatar
	 * @return Valor com duas casas decimais
	 */
	private synchronized String formataPara2CasasDecimais(double valor) {
		NumberFormat formatadorDeNumeros = NumberFormat.getInstance();
		formatadorDeNumeros.setMaximumFractionDigits(2);
		return formatadorDeNumeros.format(valor);
		
	}
	
	public byte[] relProcessosServentia(String diretorioProjetos,EstatisticaProcessoServentiaDt bean) throws IOException{
		byte[] temp = null;
		//EstatisticaProcessoServentiaDecoratorJRDataSource ei = new EstatisticaProcessoServentiaDecoratorJRDataSource(bean);
		InterfaceBeanJasper ei = new InterfaceBeanJasper(bean);
		// parâmetros do relatório
		Map parametros = new HashMap();
		
		// PATH PARA OS ARQUIVOS JASPER*******************************************************
		String pathJasper = diretorioProjetos+"WEB-INF"+ File.separator+"relatorios"+ File.separator+"EstatisticaProcessoServentia"+ File.separator+"EstatisticaProcessoPorVara.jasper";
		ByteArrayOutputStream baos = null;
		try{
			JasperPrint jp = JasperFillManager.fillReport(pathJasper,parametros, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();
			temp = baos.toByteArray();
			
			baos.close();			

		} catch(JRException e) {
			try{if (baos!=null) baos.close();  }catch(Exception e2) {		}
		}
		return  temp;
		
	}
	
	//Jelves 17/08/15
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		try{
			ComarcaNe Comarcane = new ComarcaNe(); 
			stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		}catch(Exception e){ 
			throw new Exception(" <{Erro: .... }> EstatisticaProcessoServentiaNe.consultarDescricaoComarcaJSON() " + e.getMessage(), e);
		}
		return stTemp;
	}
	
	//Jelves 17/08/15
	public String consultarServentiasComarcaJSON(String tempNomeBusca, String idComarca, String PosicaoPaginaAtual) throws Exception { 
		String stTemp = "";
		try{
			ServentiaNe Serventiane = new ServentiaNe(); 
			stTemp = Serventiane.consultarServentiasComarcaJSON(tempNomeBusca, idComarca, PosicaoPaginaAtual);
		}catch(Exception e){ 
			throw new Exception(" <{Erro: .... }> EstatisticaProcessoServentiaNe.consultarServentiasComarcaJSON() " + e.getMessage(), e);
		}
		return stTemp;
	}
	
	//Retornar uma lista de tipos de acões - Jelves 17/08/15
	public String consultarDescricaoProcessoTiposJSON(String id_ServentiaSubtipo, String descricao, String posicao) throws Exception { 
		String stTemp = "";
		try{
			ServentiaSubtipoProcessoTipoNe ServentiaSubtipoProcessoTipone = new ServentiaSubtipoProcessoTipoNe(); 
			stTemp = ServentiaSubtipoProcessoTipone.consultarProcessoTiposJSON(id_ServentiaSubtipo, descricao, posicao);
		}catch(Exception e){ 
			throw new Exception(" <{Erro: .... }> EstatisticaProcessoServentiaNe.consultarDescricaoProcessoTiposJSON() " + e.getMessage(), e);
		}
		return stTemp;
	}	
}
