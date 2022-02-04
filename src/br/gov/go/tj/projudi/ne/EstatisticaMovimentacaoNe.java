package br.gov.go.tj.projudi.ne;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaMovimentacaoDt;
import br.gov.go.tj.projudi.ps.EstatisticaMovimentacaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.pdf.InterfaceSubReportJasper;

//---------------------------------------------------------
public class EstatisticaMovimentacaoNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2115839126827313814L;
	
	protected LogNe obLog;
	protected EstatisticaMovimentacaoDt obDados;
	protected String stUltimaConsulta ="%";
	protected long QuantidadePaginas = 0; 
	
	public EstatisticaMovimentacaoNe() {
		
		obLog = new LogNe(); 
		obDados = new EstatisticaMovimentacaoDt(); 
	}
	
//---------------------------------------------------------
	public String Verificar(EstatisticaMovimentacaoDt dados) throws ParseException {
		String stRetorno = "";
		if (!dados.getDataInicial().equalsIgnoreCase("")&& !dados.getDataFinal().equalsIgnoreCase("")) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dataInicial = format.parse(dados.getDataInicial());
			Date dataFinal = format.parse(dados.getDataFinal());
			if (dataFinal.before(dataInicial)) {
				stRetorno += "Período inválido Data Inicial deve ser menor que a Data final.";
			}
		}

		return stRetorno;
	}
// ---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
//---------------------------------------------------------
	
	public EstatisticaMovimentacaoDt consultarDadosEstatisticaMovimentacaoServentia(EstatisticaMovimentacaoDt movimentacao) throws Exception {
		EstatisticaMovimentacaoDt estatisticaMovimentacao = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-EstatisticaMovimentacaoNe");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaMovimentacaoPs obPersistencia = new EstatisticaMovimentacaoPs(obFabricaConexao.getConexao());
			estatisticaMovimentacao = obPersistencia.consultarDadosEstatisticaMovimentacaoServentia(movimentacao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return estatisticaMovimentacao;
	}
	
	public EstatisticaMovimentacaoDt consultarDadosEstatisticaMovimentacaoUsuario(EstatisticaMovimentacaoDt movimentacao) throws Exception {
		EstatisticaMovimentacaoDt estatisticaMovimentacao = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-EstatisticaMovimentacaoNe");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaMovimentacaoPs obPersistencia = new EstatisticaMovimentacaoPs(obFabricaConexao.getConexao());
			estatisticaMovimentacao = obPersistencia.consultarDadosEstatisticaMovimentacaoUsuario(movimentacao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return estatisticaMovimentacao;
	}
	
	public EstatisticaMovimentacaoDt consultarDadosEstatisticaMovimentacaoUsuarioServentia(EstatisticaMovimentacaoDt movimentacao) throws Exception {
		EstatisticaMovimentacaoDt estatisticaMovimentacao = null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-EstatisticaMovimentacaoNe");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaMovimentacaoPs obPersistencia = new EstatisticaMovimentacaoPs(obFabricaConexao.getConexao());
			estatisticaMovimentacao = obPersistencia.consultarDadosEstatisticaMovimentacaoUsuarioServentia(movimentacao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return estatisticaMovimentacao;
	}
	
	public List consultarDescricaoServentiaTipoCodigo(String tempNomeBusca, String PosicaoPaginaAtual, String serventiaTipo) throws Exception {
		List tempList=null;
		ServentiaNe serventiaNe = new ServentiaNe(); 
		tempList = serventiaNe.consultarServentiasAtivas(tempNomeBusca, PosicaoPaginaAtual, serventiaTipo);
		QuantidadePaginas = serventiaNe.getQuantidadePaginas();
		serventiaNe = null;
		return tempList;
	}

	public String consultarDescricaoServentiaTipoCodigoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String serventiaTipo) throws Exception {
		String stTemp = "";
		
		ServentiaNe serventiaNe = new ServentiaNe(); 
		stTemp = serventiaNe.consultarServentiasAtivasJSON(tempNomeBusca, PosicaoPaginaAtual, serventiaTipo);
		
		return stTemp;
	}
	
	public List consultarDescricaoServidorJudiciario(String tempNomeBusca, String usuarioBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList=null;
		
		UsuarioNe usuarioNe = new UsuarioNe(); 
		tempList = usuarioNe.consultarDescricaoServidorJudiciario(tempNomeBusca, usuarioBusca, PosicaoPaginaAtual);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;
		
		return tempList;
	}
	
	public String consultarDescricaoServidorJudiciarioJSON(String nome, String usuario, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		UsuarioNe Usuarione = new UsuarioNe(); 
		stTemp = Usuarione.consultarDescricaoServidorJudiciarioJSON(nome, usuario, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public List consultarServentiasGruposUsuario(String id_ServidorJudiciario) throws Exception {
		List tempList=null;
		
		UsuarioNe usuarioNe = new UsuarioNe(); 
		tempList = usuarioNe.consultarServentiasGruposUsuario(id_ServidorJudiciario);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;
		
		return tempList;
	}
	
	public List consultarDescricaoServidorJudiciario(String id_Serventia) throws Exception {
		List tempList=null;
		UsuarioNe usuarioNe = new UsuarioNe(); 
		tempList = usuarioNe.consultarDescricaoServidorJudiciario(id_Serventia);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;
		return tempList;
	}
	
	public byte[] relEstatisticaMovimentacao(String diretorioProjetos, EstatisticaMovimentacaoDt bean) throws IOException{
		byte[] temp = null;
		InterfaceSubReportJasper ei = new InterfaceSubReportJasper(bean.getListaDetalhesMovimentacao());
		
		// PATH PARA OS ARQUIVOS JASPER DO RELATORIO PENDENCIA*******************************************************
		String pathJasper = diretorioProjetos+"WEB-INF"+File.separator+"relatorios"+File.separator+"EstatisticaMovimentacao"+File.separator;
		String descricao = "";
		
		// PARÂMETROS DO RELATÓRIO
		Map parametros = new HashMap();
		parametros.put("pathRelatorio", pathJasper+"detalhesMovimentacao.jasper");
		
		if (bean.getServentia()!= null && !bean.getServentia().equalsIgnoreCase("") && bean.getUsuario() != null && !bean.getUsuario().getNome().equalsIgnoreCase(""))
			descricao = bean.getServentia()+" - "+"Usuário: "+bean.getUsuario().getNome();
		else if (bean.getServentia()!= null && !bean.getServentia().equalsIgnoreCase(""))
			descricao =  bean.getServentia();
		else if (bean.getUsuario() != null && !bean.getUsuario().getNome().equalsIgnoreCase(""))
			descricao = "Usuário: "+bean.getUsuario().getNome();
		
		parametros.put("descricao", descricao);
		ByteArrayOutputStream baos = null;
		try{
			JasperPrint jp = JasperFillManager.fillReport(pathJasper+"EstatisticaMovimentacao.jasper",parametros, ei);
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
		return temp; 
	}

}
