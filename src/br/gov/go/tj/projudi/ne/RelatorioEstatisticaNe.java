package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
//import br.gov.go.tj.projudi.dt.EstatisticaRelatorioEstProItensDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
//import br.gov.go.tj.projudi.dt.RelatorioEstProDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoDistribuidoPorServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAcompanhamentoPonteiroDistribuicaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesPrimeiroGrauDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesSegundoGrauDt;
//import br.gov.go.tj.projudi.dt.relatorios.RelatorioEstProImpressaoDt;
//import br.gov.go.tj.projudi.dt.relatorios.RelatorioEstProOperacaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioEstatisticaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioMaioresPromoventesPromovidosDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioPonteiroDistribuicaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioRecursoRepetitivoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSituacaoGabineteDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioAudienciasComarcaDiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioAudienciasComarcaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAudienciasDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosComarcaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosServentiaDt;
import br.gov.go.tj.projudi.ps.RelatorioEstatisticaPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.pdf.InterfaceSubReportJasper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

public class RelatorioEstatisticaNe extends Negocio {

	private static final long serialVersionUID = 1382191779971832861L;
	
	protected LogNe obLog;
	protected RelatorioEstatisticaDt obDados;
	protected String stUltimaConsulta = "%";
	protected long QuantidadePaginas = 0;

	public RelatorioEstatisticaNe() {
		
		obLog = new LogNe();
		obDados = new RelatorioEstatisticaDt();
	}

	// ---------------------------------------------------------
	public String verificar(RelatorioEstatisticaDt dados) throws ParseException {
		String stRetorno = "";
		if (!dados.getDataInicial().equalsIgnoreCase("") && !dados.getDataFinal().equalsIgnoreCase("")) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dataInicial = format.parse(dados.getDataInicial());
			Date dataFinal = format.parse(dados.getDataFinal());
			if (dataFinal.before(dataInicial)) {
				stRetorno += "Per�odo inv�lido Data Inicial deve ser menor que a Data final.";
			}
		}

		return stRetorno;
	}

	// ---------------------------------------------------------

//	public long getQuantidadePaginas() {
//		return QuantidadePaginas;
//	}

	// ---------------------------------------------------------

	public RelatorioEstatisticaDt consultarDadosRelatorioEstatisticaServentia(RelatorioEstatisticaDt tipoRelatorio) throws Exception {
		RelatorioEstatisticaDt relatorioEstatistica = null;
		FabricaConexao obFabricaConexao = null;
		// //System.out.println("..ne-RelatorioEstatisticaNe");
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			relatorioEstatistica = obPersistencia.consultarDadosRelatorioEstatisticaServentia(tipoRelatorio);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return relatorioEstatistica;
	}

	public RelatorioEstatisticaDt consultarDadosEstatisticaMovimentacaoUsuario(RelatorioEstatisticaDt tipoRelatorio) throws Exception {
		RelatorioEstatisticaDt relatorioEstatistica = null;
		FabricaConexao obFabricaConexao = null;
		// //System.out.println("..ne-EstatisticaMovimentacaoNe");
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			relatorioEstatistica = obPersistencia.consultarDadosRelatorioEstatisticaUsuario(tipoRelatorio);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return relatorioEstatistica;
	}

	public RelatorioEstatisticaDt consultarDadosRelatorioEstatisticaUsuarioServentia(RelatorioEstatisticaDt tipoRelatorio) throws Exception {
		RelatorioEstatisticaDt relatorioEstatistica = null;
		FabricaConexao obFabricaConexao = null;
		// //System.out.println("..ne-EstatisticaMovimentacaoNe");
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			relatorioEstatistica = obPersistencia.consultarDadosRelatorioEstatisticaUsuarioServentia(tipoRelatorio);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return relatorioEstatistica;
	}

	public List consultarDescricaoServentiaTipoCodigo(String tempNomeBusca, String PosicaoPaginaAtual, String serventiaTipo) throws Exception {
		List tempList = null;
		ServentiaNe serventiaNe = new ServentiaNe();
		tempList = serventiaNe.consultarServentiasAtivas(tempNomeBusca, PosicaoPaginaAtual, serventiaTipo);
		QuantidadePaginas = serventiaNe.getQuantidadePaginas();
		serventiaNe = null;
		return tempList;
	}

	/**
	 * M�todo que realiza consulta de Serventias pela descri��o.
	 * 
	 * @param tempNomeBusca
	 *            - nome determinado na busca
	 * @param PosicaoPaginaAtual
	 *            - posi��o atual da busca
	 * @return lista de serventias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarServentiaDescricao(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		ServentiaNe serventiaNe = new ServentiaNe();
		tempList = serventiaNe.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = serventiaNe.getQuantidadePaginas();
		serventiaNe = null;
		
		return tempList;
	}

	public List consultarDescricaoServidorJudiciario(String tempNomeBusca, String usuarioBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;

		UsuarioNe usuarioNe = new UsuarioNe();
		tempList = usuarioNe.consultarDescricaoServidorJudiciario(tempNomeBusca, usuarioBusca, PosicaoPaginaAtual);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;
		
		return tempList;
	}
	
	public List consultarServentiasGruposUsuario(String id_ServidorJudiciario) throws Exception {
		List tempList = null;
		
		UsuarioNe usuarioNe = new UsuarioNe();
		tempList = usuarioNe.consultarServentiasGruposUsuario(id_ServidorJudiciario);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;

		return tempList;
	}

	public List consultarDescricaoServidorJudiciario(String id_Serventia) throws Exception {
		List tempList = null;
		
		UsuarioNe usuarioNe = new UsuarioNe();
		tempList = usuarioNe.consultarDescricaoServidorJudiciario(id_Serventia);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;

		return tempList;
	}

	/**
	 * M�todo que gera o relat�rio de estat�stica de movimenta��es.
	 * @param diretorioProjetos - diret�rio raiz do projeto
	 * @param bean - bean com os par�metros para gerar o relat�rio
	 * @return relat�rio de estat�stica de movimenta��o
	 * @throws IOException 
	 */
	public byte[] relEstatisticaMovimentacao(String diretorioProjetos, RelatorioEstatisticaDt bean) throws IOException{
		byte[] temp = null;
		InterfaceSubReportJasper ei = new InterfaceSubReportJasper(bean.getListaDetalhesMovimentacao());

		// PATH PARA OS ARQUIVOS JASPER DO RELATORIO
		// PENDENCIA*******************************************************
		String pathJasper = diretorioProjetos + "WEB-INF" + File.separator + "relatorios" + File.separator + "EstatisticaMovimentacao" + File.separator;
		String descricao = "";

		// PAR�METROS DO RELAT�RIO
		Map parametros = new HashMap();
		parametros.put("pathRelatorio", pathJasper + "detalhesMovimentacao.jasper");

		if (bean.getServentia() != null && !bean.getServentia().equalsIgnoreCase("") && bean.getUsuario() != null && !bean.getUsuario().getNome().equalsIgnoreCase("")) descricao = bean.getServentia() + " - " + "Usu�rio: " + bean.getUsuario().getNome();
		else if (bean.getServentia() != null && !bean.getServentia().equalsIgnoreCase("")) descricao = bean.getServentia();
		else if (bean.getUsuario() != null && !bean.getUsuario().getNome().equalsIgnoreCase("")) descricao = "Usu�rio: " + bean.getUsuario().getNome();

		parametros.put("descricao", descricao);
		ByteArrayOutputStream baos = null;
		try{
			JasperPrint jp = JasperFillManager.fillReport(pathJasper + "EstatisticaMovimentacao.jasper", parametros, ei);
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

	/**
	 * M�todo para gera��o do Relat�rio Sum�rio de Processos.
	 * 
	 * @param diretorioProjeto - caminho do diret�rio raiz do projeto 
	 * @param anoInicial - ano inicial selecionado
	 * @param mesInicial - mes inicial selecionado
	 * @param anoFinal - ano final selecionado
	 * @param mesFinal - mes final selecionado
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param tipoProcesso - tipo de processo selecionado
	 * @param agrupamento - tipo de agrupamento selecionado
	 * @param tipoArquivo - tipo de arquivo solicitado pelo usu�rio
	 * @param usuarioResponsavelRelatorio - usu�rio da sess�o que ser� colocado como solicitante do relat�rio
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relSumarioProcessos(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idComarca, String idServentia, String tipoProcesso, String agrupamento, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "";
			switch (Funcoes.StringToInt(agrupamento)) {
			case RelatorioSumarioDt.COMARCA:
				nomeRelatorio = "sumarioProcessosComarca";
				break;
			case RelatorioSumarioDt.COMARCA_SERVENTIA:
				nomeRelatorio = "sumarioProcessosComarcaServentia";
				break;
			case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
				nomeRelatorio = "sumarioProdutividade";
				break;
			}

			List listaProcessos = obPersistencia.relSumarioProcessos(anoInicial, mesInicial, anoFinal, mesFinal, idComarca, idServentia, tipoProcesso, agrupamento, tipoArquivo);

			//se a listaProcessos estiver vazia, retornar null
			//para gerar mensagem de erro para o usu�rio.
			if(listaProcessos.isEmpty()) {
				return null; 
			}
			
			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals("1")) {

				// PAR�METROS DO RELAT�RIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("titulo", "Relat�rio Sum�rio de Processos");
				parametros.put("dataAtual", new Date());
				parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
				parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				
				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

			} else {
				String conteudoArquivo = "";
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < listaProcessos.size(); i++) {
					RelatorioSumarioDt obTemp = (RelatorioSumarioDt) listaProcessos.get(i);
					
					switch (Funcoes.StringToInt(agrupamento)) {
					
					case RelatorioSumarioDt.COMARCA:
						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarca() + separadorTxt + obTemp.getProcessoRelatorio() + separadorTxt + obTemp.getQuantidade() + "\n";
						break;
					case RelatorioSumarioDt.COMARCA_SERVENTIA:
						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarca() + separadorTxt + obTemp.getServentiaRelatorio() + separadorTxt + obTemp.getProcessoRelatorio() + separadorTxt + obTemp.getQuantidade() + "\n";
						break;
					}
					
				}
				temp = conteudoArquivo.getBytes();
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}

	/**
	 * M�todo para gera��o do Relat�rio Sum�rio de Produtividade.
	 * 
	 * @param diretorioProjeto - caminho do diret�rio raiz do projeto 
	 * @param anoInicial - ano inicial selecionado
	 * @param mesInicial - mes inicial selecionado
	 * @param anoFinal - ano final selecionado
	 * @param mesFinal - mes final selecionado
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param idGrupo - ID do grupo de usu�rios selecionado
	 * @param idUsuario - ID do usu�rio selecionado
	 * @param tipoPendencia - tipo de pend�ncia selecionada
	 * @param agrupamento - agrupamento selecionado
	 * @param opcaoRelatorio - op��o do relat�rio por m�s fechado ou por data inicial e final
	 * @param tipoArquivo - tipo de arquivo solicitado pelo usu�rio
	 * @param usuarioResponsavelRelatorio - usu�rio da sess�o que ser� colocado como solicitante do relat�rio
	 * @return lista de produtividade
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relSumarioProdutividade( String anoInicial, String mesInicial, String anoFinal, String mesFinal, String dataInicial, String dataFinal, String idComarca, String idServentia, String idGrupo, String idUsuario, String tipoPendencia, String agrupamento, String opcaoRelatorio, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			List listaProdutividade = new ArrayList();
			if(opcaoRelatorio.equals(String.valueOf(RelatorioSumarioDt.MES_FECHADO))) {
				listaProdutividade = obPersistencia.relSumarioProdutividadeMesFechado(anoInicial, mesInicial, anoFinal, mesFinal, idComarca, idServentia, idGrupo, idUsuario, tipoPendencia, agrupamento, tipoArquivo);
			} else {
				listaProdutividade = obPersistencia.relSumarioProdutividadeDataInicialFinal(dataInicial, dataFinal, idComarca, idServentia, idGrupo, idUsuario, tipoPendencia, agrupamento, tipoArquivo);
			}
			
			
			//se a listaProcessos estiver vazia, retornar null
			//para gerar mensagem de erro para o usu�rio.
			if(listaProdutividade.isEmpty()) {
				return null; 
			}
			
			String diretorioProjeto = ProjudiPropriedades.getInstance().getCaminhoAplicacao();
			String pathRelatorio = Relatorios.getPathRelatorio( diretorioProjeto  );
			String nomeRelatorio = "";
			switch (Funcoes.StringToInt(agrupamento)) {
			case RelatorioSumarioDt.COMARCA:
				if(opcaoRelatorio.equals(String.valueOf(RelatorioSumarioDt.MES_FECHADO))){
					nomeRelatorio = "sumarioProdutividadeComarca";
				} else {
					nomeRelatorio = "sumarioProdutividadeComarcaDataInicialFinal";
				}
				break;
			case RelatorioSumarioDt.COMARCA_SERVENTIA:
				if(opcaoRelatorio.equals(String.valueOf(RelatorioSumarioDt.MES_FECHADO))){
					nomeRelatorio = "sumarioProdutividadeComarcaServentia";
				} else {
					nomeRelatorio = "sumarioProdutividadeComarcaServentiaDataInicialFinal";
				}
				break;
			case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
				if(opcaoRelatorio.equals(String.valueOf(RelatorioSumarioDt.MES_FECHADO))){
					nomeRelatorio = "sumarioProdutividade";
				} else {
					nomeRelatorio = "sumarioProdutividadeDataInicialFinal";
				}
				break;
			}

			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals("1")) {

				// PAR�METROS DO RELAT�RIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Relat�rio Sum�rio de Produtividade");
				if(opcaoRelatorio.equals(String.valueOf(RelatorioSumarioDt.MES_FECHADO))) {
					parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
					parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
				} else {
					parametros.put("dataInicial", dataInicial);
					parametros.put("dataFinal", dataFinal);
				}
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				
				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProdutividade);
				

			} else {
				String conteudoArquivo = "";
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < listaProdutividade.size(); i++) {
					RelatorioSumarioDt obTemp = (RelatorioSumarioDt) listaProdutividade.get(i);
					
					switch (Funcoes.StringToInt(agrupamento)) {
					case RelatorioSumarioDt.COMARCA:
						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarca() + separadorTxt + obTemp.getItemEstatisticaRelatorio() + separadorTxt + obTemp.getQuantidade() + "\n";
						break;
					case RelatorioSumarioDt.COMARCA_SERVENTIA:
						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarca() + separadorTxt + obTemp.getServentiaRelatorio() + separadorTxt + obTemp.getItemEstatisticaRelatorio() + separadorTxt + obTemp.getQuantidade() + "\n";
						break;
					case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarca() + separadorTxt + obTemp.getServentiaRelatorio() + separadorTxt + obTemp.getUsuarioRelatorio() + separadorTxt + obTemp.getItemEstatisticaRelatorio() + separadorTxt + obTemp.getQuantidade() + "\n";
						break;
					}
					
				}
				temp = conteudoArquivo.getBytes();
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * M�todo que controla a gera��o do relat�rio de produtividade dos assessores dos magistrados
	 * @param dataInicial - data inicial da consulta
	 * @param dataFinal - data final da consulta
	 * @param idServentia - id da sereventia do magistrado
	 * @param serventia - nome da serventia do magistrado
	 * @param grupoTipoCodigo - c�digo do grupo tipo ao qual pertence o magistrado
	 * @param grupoCodigo - c�digo do grupo ao qual o magistrado pertence
	 * @param idUsuarioServentiaChefe - id do usu�rio serventia chefe dos assessores
	 * @param usuarioResponsavelRelatorio - nome do usu�rio que est� gerando o relat�rio
	 * @return relat�rio em pdf contendo a produtividade
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relSumarioProdutividadeAssessores(String dataInicial, String dataFinal, String idServentia, String serventia, int grupoTipoCodigo, int grupoCodigo, String idUsuarioServentiaChefe, String usuarioResponsavelRelatorio, int opcaoRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			int grupoCodigoConsulta = 0;
			
			switch (grupoTipoCodigo) {
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				if(opcaoRelatorio == 1) {
					grupoCodigoConsulta = GrupoDt.ASSESSOR_DESEMBARGADOR;
				} else {
					grupoCodigoConsulta = GrupoDt.ASSISTENTE_GABINETE;
				}
				break;
			case GrupoTipoDt.JUIZ_AUXILIAR:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				if(grupoCodigo == GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU) {
					grupoCodigoConsulta = 999; //valor aleat�rio para ser usado apenas na valida��o logo abaixo
				} else {
					grupoCodigoConsulta = GrupoDt.ASSESSOR_JUIZES_VARA;
				}
				break;
			case GrupoTipoDt.JUIZ_TURMA:
				grupoCodigoConsulta = GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU;
				break;
			}
			
			List listaProdutividade = null;
			switch (grupoCodigoConsulta) {
				case 999:
					listaProdutividade = obPersistencia.relSumarioProdutividadeAssessoresFluxo(dataInicial, dataFinal, idServentia);
					break;
				case GrupoDt.ASSISTENTE_GABINETE:
					listaProdutividade = obPersistencia.relSumarioProdutividadeAssistenteGabinete(dataInicial, dataFinal, idServentia, grupoCodigoConsulta);
					break;
				default:
					listaProdutividade = obPersistencia.relSumarioProdutividadeAssessores(dataInicial, dataFinal, idServentia, grupoCodigoConsulta, idUsuarioServentiaChefe);
					break;
			
			}
			
			//se a listaProcessos estiver vazia, retornar null para gerar mensagem de erro para o usu�rio.
			if(listaProdutividade.isEmpty()) {
				return null; 
			}
			
			String diretorioProjeto = ProjudiPropriedades.getInstance().getCaminhoAplicacao();
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "sumarioProdutividadeAssessores";

			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			if(grupoCodigoConsulta == GrupoDt.ASSISTENTE_GABINETE) {
				parametros.put("titulo", "Relat�rio Sum�rio de Produtividade de Assistentes de Gabinete");
			} else {
				parametros.put("titulo", "Relat�rio Sum�rio de Produtividade de Assessores de Magistrado");
			}
			if(dataInicial.equals(dataFinal)) {
				parametros.put("dataReferencia", dataInicial);
			} else {
				parametros.put("dataReferencia", dataInicial + " a " + dataFinal);
			}
			parametros.put("serventia", serventia);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProdutividade);
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}

	/**
	 * M�todo para gera��o do Relat�rio Sum�rio de Pend�ncias
	 * 
	 * @param diretorioProjeto
	 *            - caminho do diret�rio raiz do projeto
	 * @param anoInicial
	 *            - ano inicial selecionado
	 * @param mesInicial
	 *            - mes inicial selecionado
	 * @param anoFinal
	 *            - ano final selecionado
	 * @param mesFinal
	 *            - mes final selecionado
	 * @param idServentia
	 *            - ID da Serventia selecionada
	 * @param idUsuario
	 *            - ID do usu�rio selecionado
	 * @param pendenciaTipo
	 *            - tipo de pend�ncia selecionada
	 * @return lista de produtividade
	 * @throws Exception
	 * @author asrocha
	 */

	public byte[] relSumarioPendencia(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idServentia, String idUsuario, String idComarca, int agrupamentoRelatorio, String usuarioSistema, String Id_Sistema, String tipoArquivo, String idPendenciaTipo) throws Exception {
			byte[] temp = null;
			FabricaConexao obFabricaConexao = null;
			try{
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

				List listaProcessos = obPersistencia.relSumarioPendencia(anoInicial, mesInicial, anoFinal, mesFinal, idServentia, idUsuario, idComarca, agrupamentoRelatorio, Id_Sistema, tipoArquivo, idPendenciaTipo);
				
				//se a listaProcessos estiver vazia, retornar null
				//para gerar mensagem de erro para o usu�rio.
				if(listaProcessos.isEmpty()) {
					return null; 
				}
				
				String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
				String nomeRelatorio = "";
				
				/* Escolha do arquivo jasper para preenchimento do relat�rio */
				if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA) nomeRelatorio = "sumarioPendencias_COMARCA";
				if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA) nomeRelatorio = "sumarioPendencias_COMARCA-SERVENTIA";
				if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) nomeRelatorio = "sumarioPendencias_COMARCA-SERVENTIA-USUARIO";

				// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
				if (tipoArquivo != null && tipoArquivo.equals("1")) {

					// PAR�METROS DO RELAT�RIO
					Map parametros = new HashMap();
					parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
					parametros.put("dataAtual", new Date());
					parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
					parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
					parametros.put("nomeSolicitante", usuarioSistema);

					temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

				} else {
					String conteudoArquivo = "";
					for (int i = 0; i < listaProcessos.size(); i++) {
						RelatorioSumarioDt obTemp = (RelatorioSumarioDt) listaProcessos.get(i);
						conteudoArquivo += obTemp.getAno() + "#" + obTemp.getMes() + "#" + obTemp.getComarca() + "#" + obTemp.getServentiaRelatorio() + "#" + obTemp.getUsuarioRelatorio() + "#" + obTemp.getPendenciaRelatorio() + "#" + obTemp.getQuantidade() + "\n";// \r";
					}
					temp = conteudoArquivo.getBytes();
				}
					
			
			} finally{
				obFabricaConexao.fecharConexao();
			}

			return temp;
		}
	
	/**
	 * M�todo que consulta Comarca pela descri��o
	 * @param tempNomeBusca - descri��o da comarca
	 * @param PosicaoPaginaAtual - posi��o da p�gina do jsp
	 * @return lista de comarcas
	 * @throws Exception
	 */
	public List consultarDescricaoComarca(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		tempList = Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Comarcane.getQuantidadePaginas();
		Comarcane = null;
		
		return tempList;
	}

	
	/**
	 * M�todo que lista todos os registros da tabela ProcessoTipo.
	 * 
	 * @return lista de dados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarTiposProcesso() throws Exception {
		List tempList = null;
		
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		tempList = (List)processoTipoNe.getProcessoTipos();
		
		return tempList;
	}
	
	/**
	 * M�todo que realiza a consulta dos RelatorioEstPro cadastrados.
	 * 
	 * @param diretorioProjeto
	 * @param anoInicial
	 *            - ano inicial selecionado
	 * @param mesInicial
	 *            - mes inicial selecionado
	 * @param anoFinal
	 *            - ano final selecionado
	 * @param mesFinal
	 *            - mes final selecionado
	 * @param idComarca
	 *            - ID da Comarca selecionada
	 * @param idServentia
	 *            - ID da Serventia selecionada
	 * @param idGrupo
	 *            - ID do grupo de usu�rios selecionado
	 * @param idUsuario
	 *            - ID do usu�rio selecionado
	 * @param idRelatorioEstPro
	 *            - ID do RelatorioEstPro selecionado
	 * @param agrupamento
	 *            - agrupamento selecionado
	 * @param tipoArquivo
	 *            - tipo de arquivo solicitado pelo usu�rio
	 * @param usuarioResponsavelRelatorio
	 *            - usu�rio solicitante do relat�rio
	 * @return relat�rio contendo a lista de produtividade resultante
	 * @throws Exception
	 * @author hmgodinho
	 */
//	
//	public byte[] imprimirRelatorioEstPro(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, String idComarca, String idServentia, String idGrupo, String idUsuario, String idRelatorioEstPro, String agrupamento, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
//		byte[] temp = null;
//		FabricaConexao obFabricaConexao = null;
//		try{
//			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
//			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
//
//			RelatorioEstProNe relatorioEstProNe = new RelatorioEstProNe();
//			RelatorioEstProDt relatorioEstPro = relatorioEstProNe.consultarId(idRelatorioEstPro);
//
//			EstatisticaRelatorioEstProItensNe estatisticaRelatorioEstProItensNe = new EstatisticaRelatorioEstProItensNe();
//			List listaEstatisticas = estatisticaRelatorioEstProItensNe.consultarIdRelatorioEstPro(relatorioEstPro.getId());
//			List listaEstatisticasConsulta = new ArrayList();
//			for (int i = 0; i < listaEstatisticas.size(); i++) {
//				EstatisticaRelatorioEstProItensDt estatistica = (EstatisticaRelatorioEstProItensDt) listaEstatisticas.get(i);
//				listaEstatisticasConsulta.add(estatistica.getId_EstatisticaProdutividadeItem());
//			}
//
//			List listaRetornoConsulta = obPersistencia.imprimirRelatorioEstPro(anoInicial, mesInicial, anoFinal, mesFinal, idComarca, idServentia, idGrupo, idUsuario, listaEstatisticasConsulta, agrupamento, tipoArquivo);
//
//			// Preparando as listas de Opera��es a serem realizadas.
//			List listaSomas = new ArrayList();
//			List listaMedias = new ArrayList();
//			String[] todasOperacoesTmp = relatorioEstPro.getOperacaoDescricao().split("#");
//			if (!todasOperacoesTmp[0].equals("")) {
//				for (int i = 0; i < todasOperacoesTmp.length; i++) {
//					String[] itemOperacaoTmp = todasOperacoesTmp[i].split(";");
//					RelatorioEstProOperacaoDt operacao = new RelatorioEstProOperacaoDt();
//					operacao.setTipoOperacao(itemOperacaoTmp[0]);
//					operacao.setNome(itemOperacaoTmp[1]);
//					operacao.setDescricao(itemOperacaoTmp[2]);
//					if (operacao.getTipoOperacao().equals("Soma")) {
//						listaSomas.add(operacao);
//					} else {
//						listaMedias.add(operacao);
//					}
//				}
//			}
//
//			// Preparando a lista de estat�sticas e opera��es do relat�rio
//			double valorQuantidade = 0;
//
//			TreeMap mapSomatorios = new TreeMap();
//			TreeMap mapMedias = new TreeMap();
//
//			List listaPosicoesRemoverListaTemporariaSomatorio = new ArrayList();
//			List listaPosicoesRemoverListaTemporariaMedia = new ArrayList();
//
//			List listaChavesMapSomatorio = new ArrayList();
//			List listaChavesMapMedia = new ArrayList();
//
//			List listaTemporariaSomatorio = new ArrayList();
//			List listaTemporariaMedia = new ArrayList();
//
//			listaTemporariaSomatorio.addAll(listaRetornoConsulta);
//			listaTemporariaMedia.addAll(listaRetornoConsulta);
//
//			//Efetuando o c�lculo das Somas cadastradas no RelatorioEstPro
//			for (int i = 0; i < listaSomas.size(); i++) {
//				RelatorioEstProOperacaoDt operacao = (RelatorioEstProOperacaoDt) listaSomas.get(i);
//				String[] descricaoOperacaoItens = operacao.getDescricao().split(",");
//				for (int j = 0; j < listaTemporariaSomatorio.size(); j++) {
//					RelatorioEstProImpressaoDt itemListaRelatorio = (RelatorioEstProImpressaoDt) listaTemporariaSomatorio.get(j);
//					for (int k = 0; k < descricaoOperacaoItens.length; k++) {
//						String idItem = descricaoOperacaoItens[k];
//						//� preciso aumentar o k pois o pr�ximo item do array � a informa��o boolean que indica se o registro
//						//deve ser mantido ou apagado quando montar o relat�rio.
//						k++;
//						boolean manterRegistro = Boolean.valueOf(descricaoOperacaoItens[k]);
//						if (idItem.equals(itemListaRelatorio.getIdItemEstatisticaRelatorio())) {
//							String key = itemListaRelatorio.getAno() + ";" + itemListaRelatorio.getMes() + ";" + itemListaRelatorio.getComarcaRelatorio() + ";" + itemListaRelatorio.getServentiaRelatorio() + ";" + itemListaRelatorio.getUsuarioRelatorio() + ";" + itemListaRelatorio.getGrupoUsuarioRelatorio() + ";" + operacao.getNome();
//							if (mapSomatorios.get(key) != null) {
//								Double novoValor = (Double) mapSomatorios.get(key);
//								valorQuantidade = novoValor + itemListaRelatorio.getQuantidade();
//							} else {
//								valorQuantidade = itemListaRelatorio.getQuantidade();
//							}
//							if(!manterRegistro){
//								listaPosicoesRemoverListaTemporariaSomatorio.add(j);
//							}
//							if (!listaChavesMapSomatorio.contains(key)) {
//								listaChavesMapSomatorio.add(key);
//							}
//							mapSomatorios.put(key, valorQuantidade);
//						}
//					}
//				}
//			}
//
//			//Convertendo as Somas geradas anteriormente em registros
//			//RelatorioEstProImpressaoDt e inserindo estes registros
//			//dentro da listaTemporariaSomatorio
//			listaTemporariaSomatorio = new ArrayList();
//			for (int i = 0; i < listaChavesMapSomatorio.size(); i++) {
//				String[] chave = listaChavesMapSomatorio.get(i).toString().split(";");
//				RelatorioEstProImpressaoDt itemMapRelatorio = new RelatorioEstProImpressaoDt();
//				itemMapRelatorio.setAno(chave[0]);
//				itemMapRelatorio.setMes(chave[1]);
//				itemMapRelatorio.setComarcaRelatorio(chave[2]);
//				itemMapRelatorio.setServentiaRelatorio(chave[3]);
//				itemMapRelatorio.setUsuarioRelatorio(chave[4]);
//				itemMapRelatorio.setGrupoUsuarioRelatorio(chave[5]);
//				itemMapRelatorio.setItemEstatisticaRelatorio(chave[6]);
//				itemMapRelatorio.setQuantidade((Double) mapSomatorios.get(listaChavesMapSomatorio.get(i)));
//				itemMapRelatorio.setOperador(true);
//				listaTemporariaSomatorio.add(itemMapRelatorio);
//			}
//
//			//Efetuando o c�lculo das M�dias cadastradas no RelatorioEstPro
//			int qtdeRegistrosContabilizados;
//			String valorMap = "";
//			for (int i = 0; i < listaMedias.size(); i++) {
//				RelatorioEstProOperacaoDt operacao = (RelatorioEstProOperacaoDt) listaMedias.get(i);
//				String[] descricaoOperacaoItens = operacao.getDescricao().split(",");
//				for (int j = 0; j < listaTemporariaMedia.size(); j++) {
//					RelatorioEstProImpressaoDt itemListaRelatorio = (RelatorioEstProImpressaoDt) listaTemporariaMedia.get(j);
//					for (int k = 0; k < descricaoOperacaoItens.length; k++) {
//						String idItem = descricaoOperacaoItens[k];
//						//� preciso aumentar o k pois o pr�ximo item do array � a informa��o boolean que indica se o registro
//						//deve ser mantido ou apagado quando montar o relat�rio.
//						k++;
//						boolean manterRegistro = Boolean.valueOf(descricaoOperacaoItens[k]);
//						if (idItem.equals(itemListaRelatorio.getIdItemEstatisticaRelatorio())) {
//							String key = itemListaRelatorio.getAno() + ";" + itemListaRelatorio.getMes() + ";" + itemListaRelatorio.getComarcaRelatorio() + ";" + itemListaRelatorio.getServentiaRelatorio() + ";" + itemListaRelatorio.getUsuarioRelatorio() + ";" + itemListaRelatorio.getGrupoUsuarioRelatorio() + ";" + operacao.getNome();
//							if (mapMedias.get(key) != null) {
//								String[] valorMapTemp = mapMedias.get(key).toString().split(",");
//								qtdeRegistrosContabilizados = Funcoes.StringToInt(valorMapTemp[0]) + 1;
//								Double novoValor = new Double(valorMapTemp[1]);
//								valorQuantidade = novoValor + itemListaRelatorio.getQuantidade();
//								valorMap = String.valueOf(qtdeRegistrosContabilizados) + "," + String.valueOf(valorQuantidade);
//							} else {
//								qtdeRegistrosContabilizados = 1;
//								valorQuantidade = itemListaRelatorio.getQuantidade();
//								valorMap = String.valueOf(qtdeRegistrosContabilizados) + "," + String.valueOf(valorQuantidade);
//							}
//							if(!manterRegistro){
//								listaPosicoesRemoverListaTemporariaMedia.add(j);
//							}
//							if (!listaChavesMapMedia.contains(key)) {
//								listaChavesMapMedia.add(key);
//							}
//							mapMedias.put(key, valorMap);
//						}
//					}
//				}
//			}
//
//			//Convertendo as M�dias geradas anteriormente em registros
//			//RelatorioEstProImpressaoDt e inserindo estes registros
//			//dentro da listaTemporariaMedia
//			listaTemporariaMedia = new ArrayList();
//			for (int i = 0; i < listaChavesMapMedia.size(); i++) {
//				String[] chave = listaChavesMapMedia.get(i).toString().split(";");
//				RelatorioEstProImpressaoDt itemMapRelatorio = new RelatorioEstProImpressaoDt();
//				itemMapRelatorio.setAno(chave[0]);
//				itemMapRelatorio.setMes(chave[1]);
//				itemMapRelatorio.setComarcaRelatorio(chave[2]);
//				itemMapRelatorio.setServentiaRelatorio(chave[3]);
//				itemMapRelatorio.setUsuarioRelatorio(chave[4]);
//				itemMapRelatorio.setGrupoUsuarioRelatorio(chave[5]);
//				itemMapRelatorio.setItemEstatisticaRelatorio(chave[6]);
//
//				String[] valorMapTemp = mapMedias.get(listaChavesMapMedia.get(i)).toString().split(",");
//				qtdeRegistrosContabilizados = Funcoes.StringToInt(valorMapTemp[0]);
//				Double valorTotal = new Double(valorMapTemp[1]);
//				Double valorMedia = valorTotal / qtdeRegistrosContabilizados;
//
//				itemMapRelatorio.setQuantidadeMedia(valorMedia);
//
//				itemMapRelatorio.setOperador(true);
//				listaTemporariaMedia.add(itemMapRelatorio);
//			}
//
//			//Unindo as listaPosicoesRemoverListaTemporariaSomatorio e listaPosicoesRemoverListaTemporariaMedia
//			//na listaFinalRemocoes que ser� a lista definitiva de remo��o dos itens de estat�stica que foram 
//			//utilizados nas opera��es.
//			List listaFinalRemocoes = new ArrayList();
//			listaFinalRemocoes.addAll(listaPosicoesRemoverListaTemporariaSomatorio);
//			for (int i = 0; i < listaPosicoesRemoverListaTemporariaMedia.size(); i++) {
//				int posicao = (Integer) listaPosicoesRemoverListaTemporariaMedia.get(i);
//				if (!listaFinalRemocoes.contains(posicao)) {
//					listaFinalRemocoes.add(posicao);
//				}
//			}
//			Collections.sort(listaFinalRemocoes);
//
//			//Usando a listaFinalRemocoes para remover os itens da listaRetornoConsulta.
//			for (int i = listaFinalRemocoes.size() - 1; i >= 0; i--) {
//				int posicao = (Integer) listaFinalRemocoes.get(i);
//				listaRetornoConsulta.remove(posicao);
//			}
//
//			//Ap�s excluir os devidos registros da listaRetornoConsulta � preciso adicionar as opera��es de Soma
//			//e M�dia que foram calculadas
//			listaRetornoConsulta.addAll(listaTemporariaSomatorio);
//			listaRetornoConsulta.addAll(listaTemporariaMedia);
//			// Ordenando a listaRetornoConsulta que ser� apresentada no relat�rio
//			Collections.sort(listaRetornoConsulta);
//
//			//Modificando o m�s de num�rico (ex: 01, 02, etc) para o respectivo nome (ex: Janeiro, Fevereiro, etc)
//			for (int i = 0; i < listaRetornoConsulta.size(); i++) {
//				RelatorioEstProImpressaoDt itemMapRelatorio = (RelatorioEstProImpressaoDt) listaRetornoConsulta.get(i);
//				itemMapRelatorio.setMes(Funcoes.identificarNomeMes(Funcoes.StringToInt(itemMapRelatorio.getMes())));
//			}
//			
//			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
//			String nomeRelatorio = "";
//			switch (Funcoes.StringToInt(agrupamento)) {
//			case RelatorioSumarioDt.COMARCA:
//				nomeRelatorio = "relatorioEstProComarca";
//				break;
//			case RelatorioSumarioDt.COMARCA_SERVENTIA:
//				nomeRelatorio = "relatorioEstProComarcaServentia";
//				break;
//			case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
//				nomeRelatorio = "relatorioEstPro";
//				break;
//			}
//
//			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
//			if (tipoArquivo != null && tipoArquivo.equals("1")) {
//
//				// PAR�METROS DO RELAT�RIO
//				Map parametros = new HashMap();
//				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
//				parametros.put("dataAtual", new Date());
//				parametros.put("titulo", relatorioEstPro.getRelatorioEstPro());
//				parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
//				parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
//				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
//				
//				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaRetornoConsulta);				
//
//			} else {
//				String conteudoArquivo = "";
//				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
//				double valorRelatorio = 0;
//				for (int i = 0; i < listaRetornoConsulta.size(); i++) {
//					RelatorioEstProImpressaoDt obTemp = (RelatorioEstProImpressaoDt) listaRetornoConsulta.get(i);
//
//					valorRelatorio = 0;
//					if (obTemp.getQuantidade() != 0) {
//						valorRelatorio = obTemp.getQuantidade();
//					} else if (obTemp.getQuantidadeMedia() != 0) {
//						valorRelatorio = obTemp.getQuantidadeMedia();
//					}
//					switch (Funcoes.StringToInt(agrupamento)) {
//					case RelatorioSumarioDt.COMARCA:
//						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarcaRelatorio() + separadorTxt + obTemp.getItemEstatisticaRelatorio() + separadorTxt + valorRelatorio + "\n";
//						break;
//					case RelatorioSumarioDt.COMARCA_SERVENTIA:
//						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarcaRelatorio() + separadorTxt + obTemp.getServentiaRelatorio() + separadorTxt + obTemp.getItemEstatisticaRelatorio() + separadorTxt + valorRelatorio + "\n";
//						break;
//					case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
//						conteudoArquivo += obTemp.getAno() + separadorTxt + obTemp.getMes() + separadorTxt + obTemp.getComarcaRelatorio() + separadorTxt + obTemp.getServentiaRelatorio() + separadorTxt + obTemp.getUsuarioRelatorio() + separadorTxt + obTemp.getItemEstatisticaRelatorio() + separadorTxt + valorRelatorio + "\n";
//						break;
//					}
//
//				}
//				temp = conteudoArquivo.getBytes();
//			}
//		
//		} finally{
//			obFabricaConexao.fecharConexao();
//		}
//
//		return temp;
//	}
//	
	/**
	 * M�todo que realiza a consulta do relat�rio anal�tico de produtividade.
	 * @param ano - ano informado na tela
	 * @param mes - m�s informado na tela
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param idUsuario  - ID do usu�rio selecionado
	 * @param idMovimentacaoTipo - tipo de movimenta��o informada
	 * @param posicaoPaginaAtual - p�gina indicada na pagina��o
	 * @return lista de produtividade
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relAnaliticoProdutividade(String ano, String mes, String idComarca, String idServentia, String idUsuario, String idMovimentacaoTipo, String posicaoPaginaAtual) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.relAnaliticoProdutividade(ano, mes, idComarca, idServentia, idUsuario, idMovimentacaoTipo, posicaoPaginaAtual);
			setQuantidadePaginas(listaProcessos);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return listaProcessos;
	}
	
	/**
	 * M�todo que realiza a consulta do relat�rio anal�tico de produtividade.
	 * @param ano - ano informado na tela
	 * @param mes - m�s informado na tela
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param idUsuario  - ID do usu�rio selecionado
	 * @param idMovimentacaoTipo - tipo de movimenta��o informada
	 * @param posicaoPaginaAtual - p�gina indicada na pagina��o
	 * @return lista de produtividade
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String relAnaliticoProdutividadeJSON(String ano, String mes, String idComarca, String idServentia, String idUsuario, String idMovimentacaoTipo, String posicaoPaginaAtual) throws Exception {
		String listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.relAnaliticoProdutividadeJSON(ano, mes, idComarca, idServentia, idUsuario, idMovimentacaoTipo, posicaoPaginaAtual);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	/**
	 * M�todo que realiza a consulta do relat�rio anal�tico de processo.
	 * @param ano - ano informado na tela
	 * @param mes - m�s informado na tela
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param idUsuario  - ID do usu�rio selecionado
	 * @param idProcessoTipo - tipo de processo informado
	 * @param posicaoPaginaAtual - p�gina indicada na pagina��o
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relAnaliticoProcesso(String ano, String mes, String idComarca, String idServentia, String idProcessoTipo, String posicaoPaginaAtual) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.relAnaliticoProcesso(ano, mes, idComarca, idServentia, idProcessoTipo, posicaoPaginaAtual);
			setQuantidadePaginas(listaProcessos);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return listaProcessos;
	}
	
	/**
	 * M�todo que realiza a consulta do relat�rio anal�tico de processo.
	 * @param ano - ano informado na tela
	 * @param mes - m�s informado na tela
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param idUsuario  - ID do usu�rio selecionado
	 * @param idProcessoTipo - tipo de processo informado
	 * @param posicaoPaginaAtual - p�gina indicada na pagina��o
	 * @return json da lista de processos
	 * @throws Exception
	 * @author gschiquini
	 */
	public String relAnaliticoProcessoJSON(String ano, String mes, String idComarca, String idServentia, String idProcessoTipo, String posicaoPaginaAtual) throws Exception {
		String stTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs( obFabricaConexao.getConexao());
			stTemp = obPersistencia.relAnaliticoProcessoJSON(ano, mes, idComarca, idServentia, idProcessoTipo, posicaoPaginaAtual);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return stTemp;
	}
	
	/**
	 * M�todo que realiza a consulta do relat�rio anal�tico de pend�ncia.
	 * @param ano - ano informado na tela
	 * @param mes - m�s informado na tela
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param idUsuario  - ID do usu�rio selecionado
	 * @param idPendenciaTipo - tipo de pend�ncia informada
	 * @param posicaoPaginaAtual - p�gina indicada na pagina��o
	 * @return lista de processos com pend�ncias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relAnaliticoPendencia(String ano, String mes, String idComarca, String idServentia, String idUsuario, String idPendenciaTipo, String posicaoPaginaAtual) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.relAnaliticoPendencia(ano, mes, idComarca, idServentia, idUsuario, idPendenciaTipo, posicaoPaginaAtual);
			setQuantidadePaginas(listaProcessos);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return listaProcessos;
	}
	
	/**
	 * M�todo que realiza a consulta do relat�rio anal�tico de pend�ncia.
	 * @param ano - ano informado na tela
	 * @param mes - m�s informado na tela
	 * @param idComarca - ID da Comarca selecionada
	 * @param idServentia - ID da Serventia selecionada
	 * @param idUsuario  - ID do usu�rio selecionado
	 * @param idPendenciaTipo - tipo de pend�ncia informada
	 * @param posicaoPaginaAtual - p�gina indicada na pagina��o
	 * @return json da lista de processos com pend�ncias
	 * @throws Exception
	 * @author gschiquini
	 */
	public String relAnaliticoPendenciaJSON(String ano, String mes, String idComarca, String idServentia, String idUsuario, String idPendenciaTipo, String posicaoPaginaAtual) throws Exception {
		String stTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs( obFabricaConexao.getConexao());
			stTemp = obPersistencia.relAnaliticoPendenciaJSON(ano, mes, idComarca, idServentia, idUsuario, idPendenciaTipo, posicaoPaginaAtual);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return stTemp;
	}
	
	/**
	 * M�todo respons�vel pela listagem e gera��o do relat�rio PDF de maiores promoventes e promovidos.
	 * @param diretorioProjeto - diret�rio raiz do projeto
	 * @param tipoParte - tipo de parte selecionada (promovente ou promovido)
	 * @param limiteConsulta - quantidade de registros m�xima da consulta
	 * @param tipoArquivo - indica que tipo de relat�rio (TXT ou PDF) ser� gerado
	 * @param usuarioResponsavelRelatorio - nome do usu�rio que solicitou o relat�rio
	 * @return relat�rio contendo a lista de maiores promoventes e promovidos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relMaioresPromoventesPromovidos(String diretorioProjeto, String tipoParte, String limiteConsulta, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "";
			nomeRelatorio = "maioresPromoventesPromovidos";

			List listaPromoventesPromovidos = obPersistencia.relMaioresPromoventesPromovidos(tipoParte, limiteConsulta);
			
			if (tipoArquivo != null && tipoArquivo.equals("1")) {

				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				if(tipoParte.equals(String.valueOf(RelatorioMaioresPromoventesPromovidosDt.TIPO_PROMOVENTE))) {
					parametros.put("titulo", "Relat�rio de Maiores Promoventes");
				} else {
					parametros.put("titulo", "Relat�rio de Maiores Promovidos");
				}
				parametros.put("limiteConsulta", limiteConsulta);
				parametros.put("dataAtual", new Date());
				parametros.put("nomeSolicitante", usuarioResponsavelRelatorio);

					temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaPromoventesPromovidos);

			} else {
				String conteudoArquivo = "";
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < listaPromoventesPromovidos.size(); i++) {
					RelatorioMaioresPromoventesPromovidosDt obTemp = (RelatorioMaioresPromoventesPromovidosDt) listaPromoventesPromovidos.get(i);
					conteudoArquivo += obTemp.getNomeParteRelatorio() + separadorTxt + obTemp.getCpfCnpjParteRelatorio() + separadorTxt + obTemp.getQtdeProcessosRelatorio() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}


		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * M�todo que gera o relat�rio sum�rio de serventias com mais processos.
	 * @param diretorioProjeto - diret�rio raiz do projeto
	 * @param tipoProcesso - tipo do processo informado
	 * @param limiteConsulta - limite de registros da consulta
	 * @param usuarioResponsavelRelatorio - nome do usu�rio da sess�o que est� solicitando o relat�rio
	 * @return lista de serventias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relSumarioMaisProcessosServentia(String diretorioProjeto, String tipoProcesso, String limiteConsulta, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "";
			nomeRelatorio = "sumarioMaisProcessosServentia";

			List listaServentias = obPersistencia.relSumarioMaisProcessosServentia(tipoProcesso, limiteConsulta);

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			switch (Funcoes.StringToInt(tipoProcesso)) {
			case RelatorioSumarioProcessosServentiaDt.ATIVO:
				parametros.put("titulo", "Relat�rio de Serventias com mais Processos Ativos");
				break;
			case RelatorioSumarioProcessosServentiaDt.ARQUIVADO:
				parametros.put("titulo", "Relat�rio de Serventias com mais Processos Arquivados");
				break;
			case RelatorioSumarioProcessosServentiaDt.DISTRIBUIDO:
				parametros.put("titulo", "Relat�rio de Serventias com mais Processos Distribu�dos");
				break;
			}
			parametros.put("limiteConsulta", limiteConsulta);
			parametros.put("dataAtual", new Date());
			parametros.put("nomeSolicitante", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaServentias);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * M�todo que gera o relat�rio sum�rio de comarcas com mais processos.
	 * @param diretorioProjeto - diret�rio raiz do projeto
	 * @param tipoProcesso - tipo do processo informado
	 * @param limiteConsulta - limite de registros da consulta
	 * @param usuarioResponsavelRelatorio - nome do usu�rio da sess�o que est� solicitando o relat�rio
	 * @return lista de comarcas
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relSumarioMaisProcessosComarca(String diretorioProjeto, String tipoProcesso, String limiteConsulta, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "";
			nomeRelatorio = "sumarioMaisProcessosComarca";

			List listaServentias = obPersistencia.relSumarioMaisProcessosComarca(tipoProcesso, limiteConsulta);

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			switch (Funcoes.StringToInt(tipoProcesso)) {
			case RelatorioSumarioProcessosComarcaDt.ATIVO:
				parametros.put("titulo", "Relat�rio de Comarcas com mais Processos Ativos");
				break;
			case RelatorioSumarioProcessosComarcaDt.ARQUIVADO:
				parametros.put("titulo", "Relat�rio de Comarcas com mais Processos Arquivados");
				break;
			case RelatorioSumarioProcessosComarcaDt.DISTRIBUIDO:
				parametros.put("titulo", "Relat�rio de Comarcas com mais Processos Distribu�dos");
				break;
			}
			parametros.put("limiteConsulta", limiteConsulta);
			parametros.put("dataAtual", new Date());
			parametros.put("nomeSolicitante", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaServentias);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * M�todo que gera o relat�rio quantitativo de processos distribu�dos no 2� grau, segundo os par�metros informados.
	 * @param diretorioProjeto - diret�rio raiz do projeto
	 * @param anoConsulta - ano da distribui��o
	 * @param idAreaDistribuicao - ID da �rea de distribui��o
	 * @param areaDistribuicao - nome da �rea de distribui��o
	 * @param usuarioResponsavelRelatorio - nome do usu�rio da sess�o que est� solicitando o relat�rio
	 * @return lista de processos distribu�dos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] imprimirRelatorioProcessoSegundoGrauArea(String diretorioProjeto, String anoConsulta, String idAreaDistribuicao, String areaDistribuicao, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "";
			nomeRelatorio = "processoSegundoGrauArea";

			List listaServentias = obPersistencia.imprimirRelatorioProcessoSegundoGrauArea(anoConsulta, idAreaDistribuicao);

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Processos Distribu�dos na �rea de Distribui��o");
			parametros.put("anoConsulta", anoConsulta);
			parametros.put("areaDistribuicao", areaDistribuicao);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaServentias);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * M�todo que gera o relat�rio quantitativo de processos distribu�dos no 2� grau por Desembargadores
	 * da Serventia, segundo os par�metros informados.
	 * @param diretorioProjeto - diret�rio raiz do projeto
	 * @param anoConsulta - ano da distribui��o
	 * @param idServentia - ID da Serventia selecionada
	 * @param serventia - nome da Serventia
	 * @param usuarioResponsavelRelatorio - nome do usu�rio da sess�o que est� solicitando o relat�rio
	 * @return lista de processos distribu�dos
	 * @author hmgodinho
	 */
	public byte[] imprimirRelatorioProcessoDistribuidosMagistradoServentia(String diretorioProjeto, String anoConsulta, String idServentia, String serventia, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "";
			nomeRelatorio = "processoSegundoGrauDesembargadores";

			List listaServentias = null;
			//A consulta de processos distribuidos s�o os mesmo para juiz, juiz turma e desembargador
//			ServentiaNe serventiaNe = new ServentiaNe();
//			String idServentiaSubTipoCodigo = serventiaNe.consultarServentiaSubTipoCodigo(idServentia, obFabricaConexao);
//			if(ServentiaSubtipoDt.isSegundoGrau(idServentiaSubTipoCodigo)){
//				//ser�o consideradas como serventias de 2� grau
//				listaServentias = obPersistencia.imprimirRelatorioProcessoSegundoGrauDesembargadoresServentia2Grau(anoConsulta, idServentia);
//			} else if(ServentiaSubtipoDt.isTurma(idServentiaSubTipoCodigo)){
//				//ser�o consideradas como serventias do tipo Turma			
				listaServentias = obPersistencia.imprimirRelatorioProcessoMagistradoResponsavel(anoConsulta, idServentia);
//			} else {
//				//se n�o for nenhum dos tipos citados acima, deve retornar o relat�rio vazio.
//				listaServentias = new ArrayList();
//			}
			
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Processos Distribu�dos na Serventia por Respons�veis");
			parametros.put("anoConsulta", anoConsulta);
			parametros.put("serventia", serventia);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaServentias);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * M�todo que gera o relat�rio quantitativo de processos distribu�dos no 1� grau por Ju�zes
	 * da Serventia, segundo os par�metros informados.
	 * @param diretorioProjeto - diret�rio raiz do projeto
	 * @param anoConsulta - ano da distribui��o
	 * @param idServentia - ID da Serventia selecionada
	 * @param serventia - nome da Serventia
	 * @param usuarioResponsavelRelatorio - nome do usu�rio da sess�o que est� solicitando o relat�rio
	 * @return lista de processos distribu�dos
	 * @author hmgodinho
	 */
	public byte[] imprimirRelatorioProcessoPrimeiroGrauJuizes(String diretorioProjeto, String anoConsulta, String idServentia, String serventia, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "";
			nomeRelatorio = "processoPrimeiroGrauJuizes";

			List listaServentias = obPersistencia.imprimirRelatorioProcessoPrimeiroGrauJuizes(anoConsulta, idServentia);

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Processos Distribu�dos no 1� Grau por Ju�zes da Serventia");
			parametros.put("anoConsulta", anoConsulta);
			parametros.put("serventia", serventia);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaServentias);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * M�todo respons�vel pela consulta dos dados dos relat�rios de conclus�es do segundo grau.
	 * @param diretorioProjeto - diret�rio do projeto
	 * @param anoInicial - ano inicial que ser� repassado como par�metro ao relat�rio
	 * @param mesInicial - mes inicial que ser� repassado como par�metro ao relat�rio
	 * @param anoFinal - ano final que ser� repassado como par�metro ao relat�rio
	 * @param mesFinal - mes final que ser� repassado como par�metro ao relat�rio
	 * @param dataInicial - data inicial tipo date que ser� repassado para a consulta do sql
	 * @param dataFinal - data final tipo date que ser� repassado para a consulta do sql
	 * @param idServentia - id da serventia da consulta
	 * @param serventia - nome da serventia que ser� repassado como par�metro ao relat�rio
	 * @param tipoRelatorio - tipo de relat�rio
	 * @param usuarioResponsavelRelatorio - nome do usu�rio que est� gerando o relat�rio
	 * @return relat�rio a ser impresso
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] imprimirRelatorioConclusoesSegundoGrau(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, Date dataInicial, Date dataFinal, String idServentia, String serventia, String tipoRelatorio, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			List listaConclusoes = null;
			String tituloRelatorio = "";
			String periodoConsulta = "";
			switch (Funcoes.StringToInt(tipoRelatorio)) {
			case RelatorioConclusoesSegundoGrauDt.PENDENTES:
				listaConclusoes = obPersistencia.imprimirRelatorioConclusoesPendentesSegundoGrau(idServentia);
				tituloRelatorio = "Relat�rio de Conclus�es/Sess�es Pendentes";
				periodoConsulta = Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial;
				break;
			case RelatorioConclusoesSegundoGrauDt.REALIZADAS:
				listaConclusoes = obPersistencia.imprimirRelatorioConclusoesRealizadasSegundoGrau(dataInicial, dataFinal, idServentia);
				tituloRelatorio = "Relat�rio de Conclus�es/Sess�es Realizadas no Per�odo";
				periodoConsulta = Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial + " a " + Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal;
				break;
			case RelatorioConclusoesSegundoGrauDt.RECEBIDAS:
				listaConclusoes = obPersistencia.imprimirRelatorioConclusoesRecebidasSegundoGrau(dataInicial, dataFinal, idServentia);
				tituloRelatorio = "Relat�rio de Conclus�es/Sess�es Recebidas no Per�odo";
				periodoConsulta = Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial + " a " + Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal;
				break;
			}

			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", tituloRelatorio);
			parametros.put("serventia", serventia);
			parametros.put("dataAtual", new Date());
			parametros.put("periodoConsulta", periodoConsulta);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				
			temp = Relatorios.gerarRelatorioPdf(Relatorios.getPathRelatorio(diretorioProjeto), "conclusoesSegundoGrau", parametros, listaConclusoes);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * M�todo respons�vel pela consulta dos dados dos relat�rios de conclus�es do primeiro grau.
	 * @param diretorioProjeto - diret�rio do projeto
	 * @param anoInicial - ano inicial que ser� repassado como par�metro ao relat�rio
	 * @param mesInicial - mes inicial que ser� repassado como par�metro ao relat�rio
	 * @param anoFinal - ano final que ser� repassado como par�metro ao relat�rio
	 * @param mesFinal - mes final que ser� repassado como par�metro ao relat�rio
	 * @param dataInicial - data inicial tipo date que ser� repassado para a consulta do sql
	 * @param dataFinal - data final tipo date que ser� repassado para a consulta do sql
	 * @param idServentia - id da serventia da consulta
	 * @param serventia - nome da serventia que ser� repassado como par�metro ao relat�rio
	 * @param tipoRelatorio - tipo de relat�rio
	 * @param usuarioResponsavelRelatorio - nome do usu�rio que est� gerando o relat�rio
	 * @return relat�rio a ser impresso
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] imprimirRelatorioConclusoesPrimeiroGrau(String diretorioProjeto, String anoInicial, String mesInicial, String anoFinal, String mesFinal, Date dataInicial, Date dataFinal, String idServentia, String serventia, String tipoRelatorio, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			List listaConclusoes = null;
			String tituloRelatorio = "";
			String periodoConsulta = "";
			switch (Funcoes.StringToInt(tipoRelatorio)) {
			case RelatorioConclusoesPrimeiroGrauDt.PENDENTES:
				listaConclusoes = obPersistencia.imprimirRelatorioConclusoesPendentesPrimeiroGrau(idServentia);
				tituloRelatorio = "Relat�rio de Conclus�es Pendentes";
				periodoConsulta = Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial;
				break;
			case RelatorioConclusoesPrimeiroGrauDt.REALIZADAS:
				listaConclusoes = obPersistencia.imprimirRelatorioConclusoesRealizadasPrimeiroGrau(dataInicial, dataFinal, idServentia);
				tituloRelatorio = "Relat�rio de Conclus�es Realizadas no Per�odo";
				periodoConsulta = Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial + " a " + Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal;
				break;
			case RelatorioConclusoesPrimeiroGrauDt.RECEBIDAS:
				listaConclusoes = obPersistencia.imprimirRelatorioConclusoesRecebidasPrimeiroGrau(dataInicial, dataFinal, idServentia);
				tituloRelatorio = "Relat�rio de Conclus�es Recebidas no Per�odo";
				periodoConsulta = Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial + " a " + Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal;
				break;
			}

			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", tituloRelatorio);
			parametros.put("serventia", serventia);
			parametros.put("dataAtual", new Date());
			parametros.put("periodoConsulta", periodoConsulta);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				
			temp = Relatorios.gerarRelatorioPdf(Relatorios.getPathRelatorio(diretorioProjeto), "conclusoesPrimeiroGrau", parametros, listaConclusoes);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * M�todo para gera��o do Relat�rio de Situa��o do Gabinete.
	 * @param anoInicial - ano inicial selecionado
	 * @param mesInicial - mes inicial selecionado
	 * @param anoFinal - ano final selecionado
	 * @param mesFinal - mes final selecionado
	 * @param idServentia - ID da Serventia selecionada
	 * @return lista com a situa��o do gabinete
	 * @throws Exception
	 * @author hmgodinho
	 */
	public RelatorioSituacaoGabineteDt imprimirRelatorioSituacaoGabinete(RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		RelatorioSituacaoGabineteDt retorno = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			MovimentacaoTipoClasseNe movimentacaoTipoClasseNe = new MovimentacaoTipoClasseNe();
			List listaTempMovimentacaoTipoClasse = (movimentacaoTipoClasseNe.listarMovimentacoesTipoClasse());
			MovimentacaoTipoClasseDt movimentacaoTipoClasseDt = null;
			for (int i = 0; i < listaTempMovimentacaoTipoClasse.size(); i++) {
				movimentacaoTipoClasseDt = (MovimentacaoTipoClasseDt) listaTempMovimentacaoTipoClasse.get(i);
				movimentacaoTipoClasseDt.setCodigoTemp(obPersistencia.imprimirCapaRelatorioSituacaoGabinteGenericos(relatorioSituacaoGabineteDt.getMesInicial(), relatorioSituacaoGabineteDt.getAnoInicial(), relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal(), movimentacaoTipoClasseDt.getMovimentacaoTipoClasseCodigo(), relatorioSituacaoGabineteDt.getIdUsuarioServentiaResponsavel()));
				
				relatorioSituacaoGabineteDt.getListaMovimentacaoTipoClasse().add(movimentacaoTipoClasseDt);
			}

			retorno = obPersistencia.imprimirCapaRelatorioSituacaoGabinte(relatorioSituacaoGabineteDt.getMesInicial(), relatorioSituacaoGabineteDt.getAnoInicial(), relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal(), relatorioSituacaoGabineteDt.getIdServentiaCargoResponsavel());
			
			relatorioSituacaoGabineteDt.setQtdeProcessosDistribuidos(retorno.getQtdeProcessosDistribuidos());
			relatorioSituacaoGabineteDt.setQtdeConclusosFinalizados(retorno.getQtdeConclusosFinalizados());
			relatorioSituacaoGabineteDt.setQtdeConclusosPendentes(retorno.getQtdeConclusosPendentes());
			relatorioSituacaoGabineteDt.setQtdeRevisao(retorno.getQtdeRevisao());			
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return relatorioSituacaoGabineteDt;
	}
	
	/**
	 * M�todo respons�vel por consultar os processos distribu�dos no gabinete
	 * @param relatorioSituacaoGabineteDt - Dt com as informa��es necess�rias
	 * @return Dt atualizado com a informa��o
	 * @throws Exception
	 * @author hmgodinho
	 */
	public RelatorioSituacaoGabineteDt consultarProcessosDistribuidosGabinete(RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			relatorioSituacaoGabineteDt.setListaItens(obPersistencia.consultarProcessosDistribuidosGabinete(relatorioSituacaoGabineteDt.getMesInicial(), relatorioSituacaoGabineteDt.getAnoInicial(), relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal(), relatorioSituacaoGabineteDt.getIdServentiaCargoResponsavel()));
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return relatorioSituacaoGabineteDt;
	}
	
	/**
	 * M�todo respons�vel por consultar os processos pendentes no gabinete
	 * @param relatorioSituacaoGabineteDt - Dt com as informa��es necess�rias
	 * @return Dt atualizado com a informa��o
	 * @throws Exception
	 * @author hmgodinho
	 */
	public RelatorioSituacaoGabineteDt consultarProcessosConclusosPendentesGabinete(RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			relatorioSituacaoGabineteDt.setListaItens(obPersistencia.consultarProcessosConclusosPendentesGabinete(relatorioSituacaoGabineteDt.getMesInicial(), relatorioSituacaoGabineteDt.getAnoInicial(), relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal(), relatorioSituacaoGabineteDt.getIdServentiaCargoResponsavel()));
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return relatorioSituacaoGabineteDt;
	}
	
	/**
	 * M�todo respons�vel por consultar os processos conclusos finalizados no gabinete
	 * @param relatorioSituacaoGabineteDt - Dt com as informa��es necess�rias
	 * @return Dt atualizado com a informa��o
	 * @throws Exception
	 * @author hmgodinho
	 */
	public RelatorioSituacaoGabineteDt consultarProcessosConclusosFinalizadosGabinete(RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			relatorioSituacaoGabineteDt.setListaItens(obPersistencia.consultarProcessosConclusosFinalizadosGabinete(relatorioSituacaoGabineteDt.getMesInicial(), relatorioSituacaoGabineteDt.getAnoInicial(), relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal(), relatorioSituacaoGabineteDt.getIdServentiaCargoResponsavel()));
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return relatorioSituacaoGabineteDt;
	}
	
	/**
	 * M�todo respons�vel por consultar os processos em revis�o no gabinete
	 * @param relatorioSituacaoGabineteDt - Dt com as informa��es necess�rias
	 * @return Dt atualizado com a informa��o
	 * @throws Exception
	 * @author hmgodinho
	 */
	public RelatorioSituacaoGabineteDt consultarProcessosRevisaoGabinete(RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			relatorioSituacaoGabineteDt.setListaItens(obPersistencia.consultarProcessosRevisaoGabinete(relatorioSituacaoGabineteDt.getMesInicial(), relatorioSituacaoGabineteDt.getAnoInicial(), relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal(), relatorioSituacaoGabineteDt.getIdServentiaCargoResponsavel()));
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return relatorioSituacaoGabineteDt;
	}
	
	/**
	 * M�todo respons�vel por consultar a estat�stica gen�rica (relativa aos MovimentacaoTipoMovimentacaoTipoClasse) no gabinete
	 * @param relatorioSituacaoGabineteDt - Dt com as informa��es necess�rias
	 * @return Dt atualizado com a informa��o
	 * @throws Exception
	 * @author hmgodinho
	 */
	public RelatorioSituacaoGabineteDt consultarEstatisticaGenericaGabinete(RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			relatorioSituacaoGabineteDt.setListaItens(obPersistencia.consultarEstatisticaGenericaGabinete(relatorioSituacaoGabineteDt.getMesInicial(), relatorioSituacaoGabineteDt.getAnoInicial(), relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal(), relatorioSituacaoGabineteDt.getIdUsuarioServentiaResponsavel(), relatorioSituacaoGabineteDt.getIdMovimentacaoTipoClasse()));
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return relatorioSituacaoGabineteDt;
	}
	
	/**
	 * M�todo respons�vel por consultar a situa��o do ponteiro de distribui��o a partir de uma determinada data.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param idAreaDistribuicao - ID da �rea de Distribui��o
	 * @param areaDistribuicao - nome da �rea de distribui��o
	 * @param dataVerificacao - data de in�cio da verifica��o
	 * @param tipoArquivo - tipo de arquivo solicitado na tela
	 * @param usuarioResponsavelRelatorio - nome do usu�rio que solicitou o relat�rio
	 * @return conte�do do relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] verificarSituacaoPonteiroDistribuicaoArea(String diretorioProjeto, String idAreaDistribuicao, String areaDistribuicao, String dataVerificacao, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			//Setando a �ltima hora do dia na data informada pelo usu�rio
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			Date dataVerificacaoRelatorio = new Date(format.parse(dataVerificacao).getTime()); 
			dataVerificacaoRelatorio = Funcoes.calculeUltimaHora(dataVerificacaoRelatorio);
			
			List situacaoPonteiro = obPersistencia.verificarSituacaoPonteiroDistribuicaoAreaDistribuicao(idAreaDistribuicao, dataVerificacaoRelatorio);
			String tipoServentiaSelecionada = null;
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);

			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals("1")) {
				// PAR�METROS DO RELAT�RIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Relat�rio de Situa��o do Ponteiro de Distribui��o por �rea de Distribui��o");
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
				if(dif == 0){ //� hoje
					Calendar dataAtual = new GregorianCalendar();
					parametros.put("dataVerificacao", Funcoes.FormatarData(new Date()) + " �s " + Funcoes.FormatarHora(new Date()));
				} else { //data anterior
					parametros.put("dataVerificacao", dataVerificacao + " �s 23:59:59");
				}
				parametros.put("tipoServentiaSelecionada", tipoServentiaSelecionada);
				parametros.put("areaDistribuicao", areaDistribuicao);
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "situacaoPonteiroDistribuicao", parametros, situacaoPonteiro);
			} else {
				String conteudoArquivo = "";
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < situacaoPonteiro.size(); i++) {
					RelatorioPonteiroDistribuicaoDt obTemp = (RelatorioPonteiroDistribuicaoDt) situacaoPonteiro.get(i);
					conteudoArquivo += obTemp.getTipoProcesso() + separadorTxt + obTemp.getNomeServentia() + separadorTxt + obTemp.getQuantidade() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * M�todo respons�vel por gerar uma lista de todos os processos que foram distribuidos na �rea de distribui��o.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param idAreaDistribuicao - ID da �rea de Distribui��o
	 * @param areaDistribuicao - nome da �rea de distribui��o
	 * @param dataVerificacao - data de in�cio da verifica��o
	 * @param tipoArquivo - tipo de arquivo solicitado na tela
	 * @param usuarioResponsavelRelatorio - nome do usu�rio que solicitou o relat�rio
	 * @return conte�do do relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] listarProcessosPonteiroDistribuicaoArea(String diretorioProjeto, String idAreaDistribuicao, String areaDistribuicao, String dataVerificacao, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			//Calculando as datas de referencia do ponteiro a partir da data informada pelo usu�rio
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			Date dataFinal = new Date(format.parse(dataVerificacao).getTime()); 
			Calendar dataInicial = new GregorianCalendar();
			dataInicial.setTime(dataFinal);
			dataFinal.setTime(dataInicial.getTimeInMillis());
			dataInicial.add(Calendar.DAY_OF_YEAR, -29);

			//o dataPonteiro ser� a data inicial e deve ser setado o primeiro segundo do dia
			dataInicial = Funcoes.calculePrimeraHora(dataInicial);
			dataFinal = Funcoes.calculeUltimaHora(dataFinal);

			List listaProcessos = obPersistencia.listarProcessosPonteiroDistribuicaoAreaDistribuicao(idAreaDistribuicao, dataInicial.getTime(), dataFinal);
			String tipoServentiaSelecionada = "";
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);

			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals("1")) {
				// PAR�METROS DO RELAT�RIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Lan�amentos do Ponteiro de Distribui��o por �rea");
				
				//Formatando a data de refer�ncia que ser� apresentada no relat�rio impresso.
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
				if(dif == 0){ //� hoje
					parametros.put("dataVerificacao", dataInicial.get(Calendar.DATE)+"/"+ (dataInicial.get(Calendar.MONTH)+1) +"/"+dataInicial.get(Calendar.YEAR) + " �s 00:00:00 a " + dataVerificacao + " �s " + Funcoes.FormatarHora(new Date()));
				} else { //data anterior
					parametros.put("dataVerificacao", dataInicial.get(Calendar.DATE)+"/"+ (dataInicial.get(Calendar.MONTH)+1) +"/"+dataInicial.get(Calendar.YEAR) + " �s 00:00:00 a " + dataVerificacao + " �s 23:59:59");
				}
				parametros.put("areaDistribuicao", areaDistribuicao);
				parametros.put("tipoServentiaSelecionada", tipoServentiaSelecionada);
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "listagemProcessosPonteiroDistribuicao", parametros, listaProcessos);				

			} else {
				String conteudoArquivo = "";
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < listaProcessos.size(); i++) {
					RelatorioPonteiroDistribuicaoDt obTemp = (RelatorioPonteiroDistribuicaoDt) listaProcessos.get(i);
					conteudoArquivo += obTemp.getDataRecebimento() + separadorTxt + obTemp.getNomeServentia() + separadorTxt + obTemp.getTipoProcesso() + separadorTxt + obTemp.getNumeroProcesso() + "." + obTemp.getDigitoVerificador() + separadorTxt + obTemp.getDistribuicaoTipo() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * Relat�rio que acompanha a distribui��o dos processos dentro da �rea de distribui��o.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param idAreaDistribuicao - ID da �rea de Distribui��o
	 * @param areaDistribuicao - nome da �rea de Distribui��o
	 * @param dataVerificacao - data da verifica��o preenchida na tela
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return  lista com as informa��es do relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] acompanharDistribuicaoArea(String diretorioProjeto, String idAreaDistribuicao, String areaDistribuicao, String dataVerificacao, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			Map parametros = new HashMap();
			String dataVerificacaoRelatorio = "";

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			List listaDiasConsulta = new ArrayList();
			String dataTemp = dataVerificacao;
			dataVerificacaoRelatorio = dataVerificacao;
			for (int i = 0; i < 30; i++) {
				parametros.put("Dia"+String.valueOf(i), format.parse(dataTemp));
				listaDiasConsulta.add((Funcoes.calculeUltimaHora(new Date(format.parse(dataTemp).getTime()))));
				//Esse if � para garantir a data correta que ser� apresentada no relat�rio
				//impresso. N�o tem impacto na consulta.
				if(i <= 28) {
					dataTemp = Funcoes.somaData(dataTemp, -1);
				}
			}
			
			Collection situacaoPonteiro = null;
			String tipoServentiaSelecionada = null;
			
			situacaoPonteiro = obPersistencia.acompanharProcessosDistribuidosAreaDistribuicao(idAreaDistribuicao, listaDiasConsulta);
			ArrayList listaSituacao = new ArrayList();
			listaSituacao.addAll(situacaoPonteiro);
			
			//Ordenando a lista por Tipo de Processo e Nome da Serventia
		    Collections.sort (listaSituacao, new Comparator() {   
                public int compare(Object o1, Object o2) {   
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c1 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o1;    
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c2 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o2;    
                	int diferenca = c1.getTipoProcesso().compareToIgnoreCase(c2.getTipoProcesso());
                	if(diferenca == 0) {
                		diferenca = c1.getIdTipoProcesso().compareToIgnoreCase(c2.getIdTipoProcesso());
                	}
                	if(diferenca == 0) {
                		diferenca = c1.getNomeServentia().compareToIgnoreCase(c2.getNomeServentia());
                	}
                    return diferenca;
                  }  
		    });
		    
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			parametros.put("titulo", "Relat�rio de Acompanhamento dos Lan�amentos do Ponteiro de Distribui��o por �rea de Distribui��o");
			//Formatando a data de refer�ncia que ser� apresentada no relat�rio impresso.
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
			if(dif == 0){ //� hoje
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s " + Funcoes.FormatarHora(new Date()));
			} else { //data anterior
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s 23:59:59");
			}
			parametros.put("tipoServentiaSelecionada", tipoServentiaSelecionada);
			parametros.put("local", "�rea de Distribui��o: " + areaDistribuicao);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "processoPonteiroDistribuicaoAcompanhamento", parametros, listaSituacao);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * Relat�rio que acompanha a distribui��o dos processos dentro de uma serventia espec�fica.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param idAreaDistribuicao - ID da �rea de Distribui��o
	 * @param areaDistribuicao - nome da �rea de Distribui��o
	 * @param idServentia - ID da Serventia
	 * @param serverntia - nome da Serventia
	 * @param dataVerificacao - data da verifica��o preenchida na tela
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return  lista com as informa��es do relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] acompanharDistribuicaoResponsavel(String diretorioProjeto, String idAreaDistribuicao, String areaDistribuicao, String idServentia, String serventia, String dataVerificacao, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			Map parametros = new HashMap();
			String dataVerificacaoRelatorio = "";

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			List listaDiasConsulta = new ArrayList();
			String dataTemp = dataVerificacao;
			dataVerificacaoRelatorio = dataVerificacao;
			for (int i = 0; i < 30; i++) {
				parametros.put("Dia"+String.valueOf(i), format.parse(dataTemp));
				listaDiasConsulta.add((Funcoes.calculeUltimaHora(new Date(format.parse(dataTemp).getTime()))));
				//Esse if � para garantir a data correta que ser� apresentada no relat�rio
				//impresso. N�o tem impacto na consulta.
				if(i <= 28) {
					dataTemp = Funcoes.somaData(dataTemp, -1);
				}
			}
			
			Collection situacaoPonteiro = obPersistencia.acompanharProcessosDistribuidosResponsavel(idAreaDistribuicao, idServentia, listaDiasConsulta);;
			
			ArrayList listaSituacao = new ArrayList();
			listaSituacao.addAll(situacaoPonteiro);
			
			//Ordenando a lista por Tipo de Processo e Nome da Serventia
		    Collections.sort (listaSituacao, new Comparator() {   
                public int compare(Object o1, Object o2) {   
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c1 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o1;    
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c2 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o2;    
                	int diferenca = c1.getTipoProcesso().compareToIgnoreCase(c2.getTipoProcesso());
                	if(diferenca == 0) {
                		diferenca = c1.getIdTipoProcesso().compareToIgnoreCase(c2.getIdTipoProcesso());
                	}
                	if(diferenca == 0) {
                		diferenca = c1.getNomeServentia().compareToIgnoreCase(c2.getNomeServentia());
                	}
                    return diferenca;
                  }  
		    });
		    
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			parametros.put("titulo", "Relat�rio de Acompanhamento da Quantidade de Processos Distribu�dos por Serventia");
			//Formatando a data de refer�ncia que ser� apresentada no relat�rio impresso.
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
			if(dif == 0){ //� hoje
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s " + Funcoes.FormatarHora(new Date()));
			} else { //data anterior
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s 23:59:59");
			}
			parametros.put("local", "Serventia: " + serventia);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "processoPonteiroDistribuicaoAcompanhamento", parametros, listaSituacao);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	public byte[] acompanharSituacaoPonteiroDistribuicaoResponsavel(String diretorioProjeto, String idAreaDistribuicao, String areaDistribuicao, String idServentia, String serventia, String dataVerificacao, String usuarioResponsavelRelatorio) throws Exception {
	
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			Map parametros = new HashMap();
			String dataVerificacaoRelatorio = "";

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			List listaDiasConsulta = new ArrayList();
			String dataTemp = dataVerificacao;
			dataVerificacaoRelatorio = dataVerificacao;
			for (int i = 0; i < 30; i++) {
				parametros.put("Dia"+String.valueOf(i), format.parse(dataTemp));
				listaDiasConsulta.add((Funcoes.calculeUltimaHora(new Date(format.parse(dataTemp).getTime()))));
				//Esse if � para garantir a data correta que ser� apresentada no relat�rio
				//impresso. N�o tem impacto na consulta.
				if(i <= 28) {
					dataTemp = Funcoes.somaData(dataTemp, -1);
				}
			}
			
			Collection situacaoPonteiro = obPersistencia.acompanharSituacaoPonteiroDistribuicaoResponsavel(idAreaDistribuicao, idServentia, listaDiasConsulta);
			ArrayList listaSituacao = new ArrayList();
			listaSituacao.addAll(situacaoPonteiro);
			
			//Ordenando a lista pelo Nome do Respons�vel
		    Collections.sort (listaSituacao, new Comparator() {   
                public int compare(Object o1, Object o2) {   
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c1 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o1;    
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c2 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o2;    
                	int diferenca = c1.getTipoProcesso().compareToIgnoreCase(c2.getTipoProcesso());
                	if(diferenca == 0) {
                		diferenca = c1.getIdTipoProcesso().compareToIgnoreCase(c2.getIdTipoProcesso());
                	}
                	if(diferenca == 0) {
                		diferenca = c1.getNomeServentia().compareToIgnoreCase(c2.getNomeServentia());
                	}
                    return diferenca;
                  }  
		    });
		    
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			parametros.put("titulo", "Relat�rio de Acompanhamento da Situa��o do Ponteiro de Distribui��o na Serventia");
			//Formatando a data de refer�ncia que ser� apresentada no relat�rio impresso.
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
			if(dif == 0){ //� hoje
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s " + Funcoes.FormatarHora(new Date()));
			} else { //data anterior
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s 23:59:59");
			}
			parametros.put("local", "Serventia: " + serventia);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "situacaoPonteiroDistribuicaoAcompanhamento", parametros, listaSituacao);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return temp;
	}
	
	/**
	 * Relat�rio que acompanha a situa��o do ponteiro de distribui��o na �rea de distribui��o.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param idAreaDistribuicao - ID da �rea de Distribui��o
	 * @param areaDistribuicao - nome da �rea de Distribui��o
	 * @param dataVerificacao - data da verifica��o preenchida na tela
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return  lista com as informa��es do relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] acompanharSituacaoPonteiroDistribuicaoArea(String diretorioProjeto, String idAreaDistribuicao, String areaDistribuicao, String dataVerificacao, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			Map parametros = new HashMap();
			String dataVerificacaoRelatorio = "";
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			List listaDiasFinaisConsulta = new ArrayList();
			String dataTemp = dataVerificacao;
			dataVerificacaoRelatorio = dataVerificacao;
			for (int i = 0; i < 30; i++) {
				parametros.put("Dia"+String.valueOf(i), format.parse(dataTemp));
				listaDiasFinaisConsulta.add(Funcoes.calculeUltimaHora(new Date(format.parse(dataTemp).getTime())));
				//Esse if � para garantir a data correta que ser� apresentada no relat�rio
				//impresso. N�o tem impacto na consulta.
				if(i <= 28) {
					dataTemp = Funcoes.somaData(dataTemp, -1);
				}
			}
			
			Collection situacaoPonteiro = null;
			String tipoServentiaSelecionada = null;
			String nomeArquivoRelatorio = null;
			ArrayList listaSituacao = new ArrayList();
			listaSituacao.addAll(obPersistencia.acompanharSituacaoPonteiroDistribuicaoAreaDistribuicao(idAreaDistribuicao, listaDiasFinaisConsulta));
			
			//Ordenando a lista por Tipo de Processo e Nome da Serventia
		    Collections.sort (listaSituacao, new Comparator() {   
                public int compare(Object o1, Object o2) {   
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c1 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o1;    
                	RelatorioAcompanhamentoPonteiroDistribuicaoDt c2 = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) o2;    
                	int diferenca = c1.getTipoProcesso().compareToIgnoreCase(c2.getTipoProcesso());
                	if(diferenca == 0) {
                		diferenca = c1.getIdTipoProcesso().compareToIgnoreCase(c2.getIdTipoProcesso());
                	}
                	if(diferenca == 0) {
                		diferenca = c1.getNomeServentia().compareToIgnoreCase(c2.getNomeServentia());
                	}
                    return diferenca;
                  }  
		    });
		    
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			parametros.put("titulo", "Relat�rio de Acompanhamento da Situa��o do Ponteiro de Distribui��o por �rea de Distribui��o");
			//Formatando a data de refer�ncia que ser� apresentada no relat�rio impresso.
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
			if(dif == 0){ //� hoje
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s " + Funcoes.FormatarHora(new Date()));
			} else { //data anterior
				parametros.put("dataVerificacao", dataTemp + " �s 00:00:00 a " + dataVerificacaoRelatorio + " �s 23:59:59");
			}
			parametros.put("local", "�rea de Distribui��o: " + areaDistribuicao);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "situacaoPonteiroDistribuicaoAcompanhamento", parametros, listaSituacao);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	public String consultarServentiasComarcaJSON(String tempNomeBusca, String idComarca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarServentiasComarcaJSON(tempNomeBusca, idComarca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * Relat�rio que informa a situa��o do ponteiro de distribui��o na serventia.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param idServentia - ID da Serventia
	 * @param serventia - nome da Serventia
	 * @param dataVerificacao - data da verifica��o preenchida na tela
	 * @param tipoArquivo - tipo de arquivo selecionado
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return - lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] verificarSituacaoPonteiroDistribuicaoResponsavel(String diretorioProjeto, String idServentia, String serventia, String idAreaDistribuicao, String areaDistribuicao, String dataVerificacao, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new  RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			//Setando a �ltima hora do dia na data informada pelo usu�rio
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			Date dataVerificacaoRelatorio = new Date(format.parse(dataVerificacao).getTime()); 
			dataVerificacaoRelatorio = Funcoes.calculeUltimaHora(dataVerificacaoRelatorio);
			
			List situacaoPonteiro = obPersistencia.verificarSituacaoPonteiroDistribuicaoResponsavel(idServentia, idAreaDistribuicao, dataVerificacaoRelatorio);
			
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);

			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals("1")) {

				// PAR�METROS DO RELAT�RIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Situa��o do Ponteiro de Distribui��o na Serventia");
				parametros.put("dataVerificacao", dataVerificacao);
				parametros.put("serventia", serventia);
				parametros.put("areaDistribuicao", areaDistribuicao);
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
				if(dif == 0){ //� hoje
					Calendar dataAtual = new GregorianCalendar();
					parametros.put("dataVerificacao", Funcoes.FormatarData(new Date()) + " �s " + Funcoes.FormatarHora(new Date()));
				} else { //data anterior
					parametros.put("dataVerificacao", dataVerificacao + " �s 23:59:59");
				}
				parametros.put("local", "Serventia: " + serventia);
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "processoPonteiroDistribuicaoResponsaveis", parametros, situacaoPonteiro);

			} else {
				String conteudoArquivo = "";
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < situacaoPonteiro.size(); i++) {
					RelatorioPonteiroDistribuicaoDt obTemp = (RelatorioPonteiroDistribuicaoDt) situacaoPonteiro.get(i);
					conteudoArquivo += obTemp.getTipoProcesso() +  separadorTxt + obTemp.getNomeResponsavel() + separadorTxt + obTemp.getServCargoResponsavel() + separadorTxt + obTemp.getQuantidade() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * Relat�rio que lista os processos que foram distribu�dos pelo ponteiro de distribui��o na serventia.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param idServentia - ID da Serventia
	 * @param serventia - nome da Serventia
	 * @param dataVerificacao - data da verifica��o preenchida na tela
	 * @param tipoArquivo - tipo de arquivo selecionado
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return - lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] listarProcessosPonteiroDistribuicaoResponsavel(String diretorioProjeto, String idServentia, String serventia, String idAreaDistribuicao, String areaDistribuicao, String dataVerificacao, String tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new  RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			
			//Calculando as datas de referencia do ponteiro a partir da data informada pelo usu�rio
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			Date dataFinal = new Date(format.parse(dataVerificacao).getTime()); 
			Calendar dataInicial = new GregorianCalendar();
			dataInicial.setTime(dataFinal);
			dataFinal.setTime(dataInicial.getTimeInMillis());
			dataInicial.add(Calendar.DAY_OF_YEAR, -29);

			//o dataPonteiro ser� a data inicial e deve ser setado o primeiro segundo do dia
			dataInicial = Funcoes.calculePrimeraHora(dataInicial);
			dataFinal = Funcoes.calculeUltimaHora(dataFinal);

			List situacaoPonteiro = obPersistencia.listarProcessosPonteiroDistribuicaoResponsavel(idAreaDistribuicao, idServentia, dataInicial.getTime(), dataFinal);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);

			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals("1")) {

				// PAR�METROS DO RELAT�RIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Processos Distribu�dos pelo Ponteiro de Distribui��o na Serventia");
				//Formatando a data de refer�ncia que ser� apresentada no relat�rio impresso.
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				int dif = Funcoes.calculaDiferencaEntreDatas(dataVerificacao, df.format(new Date()));
				if(dif == 0){ //� hoje
					parametros.put("dataVerificacao", dataInicial.get(Calendar.DATE)+"/"+ (dataInicial.get(Calendar.MONTH)+1) +"/"+dataInicial.get(Calendar.YEAR) + " �s 00:00:00 a " + dataVerificacao + " �s " + Funcoes.FormatarHora(new Date()));
				} else { //data anterior
					parametros.put("dataVerificacao", dataInicial.get(Calendar.DATE)+"/"+ (dataInicial.get(Calendar.MONTH)+1) +"/"+dataInicial.get(Calendar.YEAR) + " �s 00:00:00 a " + dataVerificacao + " �s 23:59:59");
				}
				parametros.put("serventia", serventia);
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "listagemProcessosPonteiroDistribuicaoResponsaveis", parametros, situacaoPonteiro);
			} else {
				String conteudoArquivo = "";
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < situacaoPonteiro.size(); i++) {
					RelatorioPonteiroDistribuicaoDt obTemp = (RelatorioPonteiroDistribuicaoDt) situacaoPonteiro.get(i);
					conteudoArquivo += obTemp.getTipoProcesso() + separadorTxt + obTemp.getDataRecebimento() + separadorTxt + obTemp.getNumeroProcesso() + separadorTxt + obTemp.getDigitoVerificador() + separadorTxt + obTemp.getNomeResponsavel() + separadorTxt + obTemp.getServCargoResponsavel() + separadorTxt + obTemp.getDistribuicaoTipo() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	public String consultarDescricaoAreaDistribuicaoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		AreaDistribuicaoNe AreaDistribuicaone = new AreaDistribuicaoNe(); 
		stTemp = AreaDistribuicaone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * M�todo para consultar o ServentiaSubTipoCodigo de uma serventia.
	 * @param idServentia - ID da serventia selecionada
	 * @return ServentiaSubTipoCodigo consultado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarServentiaSubTipoCodigo(String idServentia) throws Exception {
		String serventiaSubTipoCodigo = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ServentiaNe serventiaNe = new ServentiaNe();
			serventiaSubTipoCodigo = serventiaNe.consultarServentiaSubTipoCodigo(idServentia, obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaSubTipoCodigo;
	}
	
	public String consultarDescricaoEstatisticaProdutividadeItemJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		EstatisticaProdutividadeItemNe estatisticaProdutividadeItemNe = new EstatisticaProdutividadeItemNe();
		stTemp = estatisticaProdutividadeItemNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		GrupoNe Grupone = new GrupoNe();
		stTemp = Grupone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);

		return stTemp;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ComarcaNe comarcaNe = new ComarcaNe();
		stTemp = comarcaNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);

		return stTemp;
	}
	
	public String consultarDescricaoProcessoTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		stTemp = processoTipoNe.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoPendenciaTipoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		stTemp = pendenciaTipoNe.consultarDescricaoJSON(descricao, posicao);
		
		return stTemp;
	}
	
	public String consultarDescricaoServidorJudiciarioJSON(String nome, String usuario, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		UsuarioNe Usuarione = new UsuarioNe(); 
		stTemp = Usuarione.consultarDescricaoServidorJudiciarioJSON(nome, usuario, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoMovimentacaoTipoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		
		MovimentacaoTipoNe movimentacaoTipoNe = new MovimentacaoTipoNe();
		stTemp = movimentacaoTipoNe.consultarDescricaoJSON(descricao, posicao);

		return stTemp;
	}


	/**
	 * M�todo respons�vel pela consulta de advogados de outros estados que tem mais processos 
	 * que o permitido pela OAB-GO.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return lista de advogados com excesso de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] consultarAdvogadosProcessosOutrosEstados(String diretorioProjeto, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "advogadosProcessosOutrosEstados";

			List listaAdvogados = obPersistencia.consultarAdvogadosProcessosOutrosEstados();

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relat�rio de Advogados com mais processos que o permitido pela OAB-GO");
			parametros.put("limitePermitido", Integer.valueOf(Configuracao.QUANTIDADE_MAXIMA_PROCESSOS_ADVOGADOS_OUTRAS_OAB));
			parametros.put("dataAtual", new Date());
			parametros.put("nomeSolicitante", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaAdvogados);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	public String consultarDescricaoAreaDistribuicaoServentiaJSON(String descricao, String idServentia, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		AreaDistribuicaoNe AreaDistribuicaone = new AreaDistribuicaoNe(); 
		stTemp = AreaDistribuicaone.consultarDescricaoServentiaJSON(descricao, idServentia, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);		
		
		return stTemp;
	}
	
	public String consultarDescricaoServidorJudiciarioServentiaJSON(String tempNomeBusca, String usuarioBusca, String PosicaoPaginaAtual, String idServentia) throws Exception {
		String stTemp = "";
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		stTemp = usuarioServentiaNe.consultarDescricaoServidorJudiciarioServentiaJSON(tempNomeBusca, usuarioBusca, PosicaoPaginaAtual, idServentia);
		usuarioServentiaNe = null;
		
		return stTemp;
	}
	
	public String consultarDescricaoTemaJSON(String descricao, String origem, String situacao, String codigo, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		TemaNe temaNe = new TemaNe(); 
		stTemp = temaNe.consultarDescricaoJSON(descricao, origem, situacao, codigo, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * M�todo de gera��o do relat�rio sum�rio de recursos repetitivos.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param dataInicial - data inicial de sobrestamento
	 * @param dataFinal - data final de sobrestamento
	 * @param idTema - ID do tema
	 * @param tema - nome do tema
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relSumarioRecursoRepetitivo(String diretorioProjeto, String dataInicial, String dataFinal, String idTema, String tema, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "sumarioRecursoRepetitivo";

			List listaProcessos = obPersistencia.relSumarioRecursoRepetitivo(dataInicial, dataFinal, idTema);

			//se a listaProcessos estiver vazia, retornar null para gerar mensagem de erro para o usu�rio.
			if(listaProcessos.isEmpty()) {
				return null; 
			}
			
			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relat�rio Sint�tico de Processos Sobrestados");
			parametros.put("dataAtual", new Date());
			if(tema.equals("")) {
				tema = "Todos";
			}
			parametros.put("temaRecurso", tema);
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * M�todo de gera��o do relat�rio anal�tico de recursos repetitivos.
	 * @param diretorioProjeto - caminho do diret�rio onde se encontram os arquivos jasper para gera��o do relat�rio
	 * @param dataInicial - data inicial de sobrestamento
	 * @param dataFinal - data final de sobrestamento
	 * @param idTema - ID do tema
	 * @param tema - nome do tema
	 * @param usuarioResponsavelRelatorio - usu�rio que solicitou o relat�rio
	 * @return relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relAnaliticoRecursoRepetitivo(String diretorioProjeto, String dataInicial, String dataFinal, String idTema, String tema, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "analiticoRecursoRepetitivo";

			List listaProcessos = obPersistencia.relAnaliticoRecursoRepetitivo(dataInicial, dataFinal, idTema);

			//se a listaProcessos estiver vazia, retornar null
			//para gerar mensagem de erro para o usu�rio.
			if(listaProcessos.isEmpty()) {
				return null; 
			}
			
			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relat�rio Anal�tico de Processos Sobrestados");
			parametros.put("dataAtual", new Date());
			if(tema.equals("")) {
				tema = "Todos";
			}
			parametros.put("temaRecurso", tema);
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	public byte[] relSumarioAudienciasComarca(RelatorioAudienciasDt RelatorioAudienciasDt, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		
		if(
			RelatorioAudienciasDt == null || 
			RelatorioAudienciasDt.getId_Comarca() == null ||
			RelatorioAudienciasDt.getComarca() == null ||
			RelatorioAudienciasDt.getDataInicial() == null ||
			RelatorioAudienciasDt.getDataFinal() == null
		) {
			throw new MensagemException("Par�metros incompletos.");
		}
			
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String diretorioProjeto = ProjudiPropriedades.getInstance().getCaminhoAplicacao();;
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "sumarioAudienciasPorComarca";

			List<RelatorioSumarioAudienciasComarcaDt> listaRelSumarioAudienciasComarcaDt = obPersistencia.relSumarioAudienciasComarca(RelatorioAudienciasDt.getId_Comarca(), RelatorioAudienciasDt.getDataInicial(), RelatorioAudienciasDt.getDataFinal());

			//se a listaProcessos estiver vazia, retornar null
			//para gerar mensagem de erro para o usu�rio.
			if(listaRelSumarioAudienciasComarcaDt == null) {
				return null; 
			}
			
			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relat�rio Sum�rio de Audi�ncias");
			parametros.put("dataInicio", RelatorioAudienciasDt.getDataInicial());
			parametros.put("dataFim", RelatorioAudienciasDt.getDataFinal());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
//			parametros.put("qtdDesignadas", relSumarioAudienciasDt.getQtdDesignadas());
//			parametros.put("qtdRealizadas", relSumarioAudienciasDt.getQtdRealizadas());
//			parametros.put("qtdAcordos", relSumarioAudienciasDt.getQtdAcordos());
//			parametros.put("valorAcordos", relSumarioAudienciasDt.getValorAcordos());
			parametros.put("serventiaRelatorio", RelatorioAudienciasDt.getServentia());
			parametros.put("comarca", RelatorioAudienciasDt.getComarca());
			
//			List lista = new ArrayList();
//			lista.add(relSumarioAudienciasDt);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaRelSumarioAudienciasComarcaDt);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	public byte[] relSumarioAudienciasComarcaDia(RelatorioAudienciasDt RelatorioAudienciasDt, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		
		if(
			RelatorioAudienciasDt == null || 
			RelatorioAudienciasDt.getId_Comarca() == null ||
			RelatorioAudienciasDt.getComarca() == null ||
			RelatorioAudienciasDt.getDataInicial() == null
		) {
			throw new MensagemException("Par�metros incompletos.");
		}
			
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String diretorioProjeto = ProjudiPropriedades.getInstance().getCaminhoAplicacao();;
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "sumarioAudienciasPorComarcaDia";

			List<RelatorioSumarioAudienciasComarcaDiaDt> listaRelSumarioAudienciasComarcaDiaDt = obPersistencia.relSumarioAudienciasComarcaDia(RelatorioAudienciasDt.getId_Comarca(), RelatorioAudienciasDt.getDataInicial(), RelatorioAudienciasDt.getDataFinal());

			//se a listaProcessos estiver vazia, retornar null
			//para gerar mensagem de erro para o usu�rio.
			if(listaRelSumarioAudienciasComarcaDiaDt == null) {
				return null; 
			}
			
			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relat�rio Sum�rio de Audi�ncias");
			parametros.put("dataInicial", RelatorioAudienciasDt.getDataInicial());
			parametros.put("dataFinal", RelatorioAudienciasDt.getDataFinal());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			parametros.put("serventiaRelatorio", RelatorioAudienciasDt.getServentia());
			parametros.put("comarca", RelatorioAudienciasDt.getComarca());
			parametros.put("dataAtual", Funcoes.FormatarData(new Date()));
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaRelSumarioAudienciasComarcaDiaDt);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	public byte[] relAnaliticoAudiencias(RelatorioAudienciasDt relatorioAudienciasDt, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		
		if(
			relatorioAudienciasDt == null || 
			relatorioAudienciasDt.getId_Comarca() == null ||
			relatorioAudienciasDt.getComarca() == null ||
			relatorioAudienciasDt.getDataInicial() == null
		) {
			throw new MensagemException("Par�metros incompletos.");
		}
			
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String diretorioProjeto = ProjudiPropriedades.getInstance().getCaminhoAplicacao();;
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "analiticoAudiencias";

			List<RelatorioAudienciasDt> listaRelatorioAudienciasDt = obPersistencia.relAnaliticoAudiencias(relatorioAudienciasDt.getId_Comarca(), relatorioAudienciasDt.getId_Serventia(), relatorioAudienciasDt.getDataInicial(), relatorioAudienciasDt.getDataFinal());

			//se a listaProcessos estiver vazia, retornar null
			//para gerar mensagem de erro para o usu�rio.
			if(listaRelatorioAudienciasDt == null) {
				return null; 
			}
			
			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relat�rio Sum�rio de Audi�ncias");
			parametros.put("dataInicial", relatorioAudienciasDt.getDataInicial());
			parametros.put("dataFinal", relatorioAudienciasDt.getDataFinal());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			parametros.put("serventiaRelatorio", relatorioAudienciasDt.getServentia());
			parametros.put("comarca", relatorioAudienciasDt.getComarca());
			parametros.put("dataAtual", Funcoes.FormatarData(new Date()));
			
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaRelatorioAudienciasDt);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	public String consultarServentiasSegundoGrauAtivasJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp="";				
						
		ServentiaNe serventiaNe = new ServentiaNe();
		stTemp = serventiaNe.consultarServentiasSegundoGrauAtivasJSON(tempNomeBusca, posicaoPaginaAtual);
		
		return stTemp;
	}

	public List consultarServentiasPrincipaisRelacionadas(String id) throws Exception {
		List lisTemp = null;
		
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();		
		lisTemp = serventiaRelacionadaNe.consultarServentiasPrincipaisRelacionadas(id);
	
		return lisTemp;
	}

	public ServentiaDt consultarServentiaId(String idServentiaConsulta) throws Exception {
		ServentiaDt serventia=null;
		
		ServentiaNe serventiaNe = new ServentiaNe();		
		serventia = serventiaNe.consultarId(idServentiaConsulta);
		
		return serventia;
	}
	
	public byte[] relatorioEstatisticoNugep(String diretorioProjeto, String dataInicial, String dataFinal,
			String usuarioResponsavelRelatorio) throws Exception {

		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try {

			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "relatorioEstatisticoNugep";
			List<RelatorioRecursoRepetitivoDt> tempList = new ArrayList<>();
			List<RelatorioRecursoRepetitivoDt> tempList1 = new ArrayList<>();
			RelatorioRecursoRepetitivoDt obTemp = new RelatorioRecursoRepetitivoDt();
			tempList = obPersistencia.relatorioEstatisticoNugep(dataInicial, dataFinal);
			if (tempList == null) {
				return null;
			}
			//
			// Monta lista(tempList1) na ordem de impressao (definido pelo stj) com todos os itens com
			// quantidade zero.
			//
			// Da lista(tempList), que vem do sql, compara os tipos de movimento e altera
			// (tempList1) as quantidades dos intens encontrados
			//
			// Itens n�o encontrados sao impressos com zero.
			//
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial Autuado");
			obTemp.setTipoMovimentoCodigo(Integer.toString(ProcessoTipoDt.RECURSO_ESPECIAL));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial Admitido");
			obTemp.setTipoMovimentoCodigo(Integer.toString(MovimentacaoTipoDt.RECURSO_ESPECIAL_ADMITIDO));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial Admitido - IRDR");
			obTemp.setTipoMovimentoCodigo(Integer.toString(MovimentacaoTipoDt.RECURSO_ESPECIAL_ADMITIDO_IRDR));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial encaminhado como representativo da controv�rsia");
			obTemp.setTipoMovimentoCodigo(Integer.toString(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPRESENTATIVO_CONTROVERSIA));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial aguardando ju�zo de admissibilidade");
			obTemp.setTipoMovimentoCodigo("Recurso Especial aguardando ju�zo de admissibilidade");
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial Repetitivo N�o Admitido em Conson�ncia");
			obTemp.setTipoMovimentoCodigo(Integer.toString(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPETITIVO_NAO_ADMITIDO_CONSONANCIA));			
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial Repetitivo encaminhado para Ju�zo de Retrata��o");
			obTemp.setTipoMovimentoCodigo(Integer.toString(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPETITIVO_ENCAMINHADO_JUIZO_RETRATACAO));			
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Recurso Especial N�o Admitido");
			obTemp.setTipoMovimentoCodigo(Integer.toString(MovimentacaoTipoDt.RECURSO_ESPECIAL_NAO_ADMITIDO));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Agravo ao STJ");
			obTemp.setTipoMovimentoCodigo(Integer.toString(ProcessoTipoDt.AGRAVO_STJ));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Agravo Interno das Decis�es de Aplica��o do Tema do STJ");  
			obTemp.setTipoMovimentoCodigo(Integer.toString(ProcessoTipoDt.AGRAVO_INTERNO_DECISOES_APLICACAO_STJ));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Admiss�o de Incidente de Assun��o de Compet�ncia");
			obTemp.setTipoMovimentoCodigo(Integer.toString(MovimentacaoTipoDt.ADMITIDO_IAC));
			tempList1.add(obTemp);
			obTemp = new RelatorioRecursoRepetitivoDt();
			obTemp.setQuantidade(0);
			obTemp.setTipoMovimento("Total de Processos Sobrestados");
			obTemp.setTipoMovimentoCodigo("Total de Processos Sobrestados");
			tempList1.add(obTemp);
			//
			// compara lista para preencher quantidade.
			//
			for (int j = 0; j < tempList.size(); j++) {
				for (int k = 0; k < tempList1.size(); k++) {					 
					if (tempList.get(j).getTipoMovimentoCodigo().equals(tempList1.get(k).getTipoMovimentoCodigo())) {
						tempList1.get(k).setQuantidade(tempList.get(j).getQuantidade());						
						break;
					}				
				}
			}
			//
			// adicionar no final(tempList1) os  recursos sobrestados por item. Tipo movimento vem com branco no inicio (tempList).
			//
					
			for (int j = 0; j < tempList.size(); j++) {
				if (tempList.get(j).getTipoMovimentoCodigo().substring(0, 1).equalsIgnoreCase(" ")) {
					obTemp = new RelatorioRecursoRepetitivoDt();
					obTemp.setTipoMovimento(tempList.get(j).getTipoMovimentoCodigo());
					obTemp.setQuantidade(tempList.get(j).getQuantidade());
					tempList1.add(obTemp);
				}
			}
			//
			// PAR�METROS DO RELAT�RIO
			//
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Estat�stica de Movimenta��o de Recursos");
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, tempList1);

		} finally

		{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}

	public byte[] relAnaliticoRecursoRepetitivoPorServentia(String diretorioProjeto, String dataInicial, String dataFinal,
			String idTema, String tema, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "analiticoRecursoRepetitivoPorServentia";

			List listaProcessos = obPersistencia.relAnaliticoRecursoRepetitivoPorServentia(dataInicial, dataFinal, idTema);

			// se a listaProcessos estiver vazia, retornar null
			// para gerar mensagem de erro para o usu�rio.
			if (listaProcessos.isEmpty()) {
				return null;
			}

			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relat�rio Anal�tico de Processos Sobrestados Por Serventia");
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	public byte[] processoDistribuidoPorServentiaAnalitico(String diretorioProjeto, String idAreaDistribuicao,
			String areaDistribuicao, String idServentia, String Serventia, String idUsuario, String usuario,
			String dataInicial, String dataFinal, String usuarioResponsavelRelatorio, String idUsuarioSessao,
			String idServentiaSessao, String codigoGrupoSessao) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);		
			
            String tipoSaida = "";	
			
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ESTATISTICA)) ||
			    codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.DISTRIBUIDOR_SEGUNDO_GRAU))) {				
				if (idServentia != null && !idServentia.equals("")) {  // serventia selecionada na tela
				    tipoSaida = "serventiaSelecionada";					
				} else {
					if (!codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ESTATISTICA))) {
					    if (!idServentiaSessao.equalsIgnoreCase(Integer.toString(ServentiaDt.DIVISAO_DISTRIBUICAO_TRIBUNAL_ID))) { 
					    	tipoSaida = "serventiaSessao";
					    }	
					}
				  }
			} else {
				tipoSaida = "serventiaSessao";
			}	
			
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());

			List<ProcessoDistribuidoPorServentiaDt> listaProcesso =  obPersistencia
					.processoDistribuidoPorServentiaAnalitico(idAreaDistribuicao, idServentia, idUsuario, dataInicial,
							dataFinal, tipoSaida, idServentiaSessao);
			
			if (listaProcesso.isEmpty()) {
				return null;
			}

			String tipoServentiaSelecionada = null;
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("titulo", "Processos Distr�buidos Por Serventia");
			parametros.put("AreaDistribuicao", areaDistribuicao);
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "analiticoProcessoDistribuidoPorServentia", parametros,
					listaProcesso);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public byte[] processoDistribuidoPorServentiaSintetico(String diretorioProjeto, String idAreaDistribuicao,
			String areaDistribuicao, String idServentia, String Serventia, String idUsuario, String usuario,
			String dataInicial, String dataFinal, String nomeUsuarioSessao,
			String idUsuarioSessao, String idServentiaSessao, String codigoGrupoSessao) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;		 
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);				
			
			  String tipoSaida = "";	
				
			  if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ESTATISTICA)) ||
				  codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.DISTRIBUIDOR_SEGUNDO_GRAU))) {				
				  if (idServentia != null && !idServentia.equals("")) {  // serventia selecionada na tela
					  tipoSaida = "serventiaSelecionada";					
				} else {
					if (!codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ESTATISTICA))) {
				    	if (!idServentiaSessao.equalsIgnoreCase(Integer.toString(ServentiaDt.DIVISAO_DISTRIBUICAO_TRIBUNAL_ID))) { 
						    tipoSaida = "serventiaSessao";
					    }
					}
				  }
			  } else {
				  tipoSaida = "serventiaSessao";
			  }	
			
			RelatorioEstatisticaPs obPersistencia = new RelatorioEstatisticaPs(obFabricaConexao.getConexao());
			List<ProcessoDistribuidoPorServentiaDt> listaTotalProcesso = obPersistencia
					.processoDistribuidoPorServentiaSintetico(idAreaDistribuicao, idServentia, idUsuario, dataInicial,
							dataFinal, tipoSaida, idServentiaSessao);
			
			if (listaTotalProcesso.isEmpty()) {
				return null;
			}
			
			ProcessoDistribuidoPorServentiaDt objTemp = new ProcessoDistribuidoPorServentiaDt();

			List listaTotal = new ArrayList<>();

			for (int i = 0; i < listaTotalProcesso.size(); i++) {
				objTemp = new ProcessoDistribuidoPorServentiaDt();
				objTemp.setNomeServentia(listaTotalProcesso.get(i).getNomeServentia());
				objTemp.setNomeUsuario(listaTotalProcesso.get(i).getNomeUsuario());
				objTemp.setDistribuicao(listaTotalProcesso.get(i).getDistribuicao());
				objTemp.setRedistribuicao(listaTotalProcesso.get(i).getRedistribuicao() * -1);
				objTemp.setGanhoResponsabilidade(listaTotalProcesso.get(i).getGanhoResponsabilidade());
				objTemp.setPerdaResponsabilidade(listaTotalProcesso.get(i).getPerdaResponsabilidade() * -1);
				objTemp.setCompensacao(
						listaTotalProcesso.get(i).getGanhoCompensacao() - listaTotalProcesso.get(i).getPerdaCompensacao());
				objTemp.setCorrecao(
						listaTotalProcesso.get(i).getGanhoCorrecao() - listaTotalProcesso.get(i).getPerdaCorrecao());
				listaTotal.add(objTemp);
			}

			String tipoServentiaSelecionada = null;
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("titulo", "Processos Distr�buidos Por Serventia");
			parametros.put("AreaDistribuicao", areaDistribuicao);
			parametros.put("usuarioResponsavelRelatorio", nomeUsuarioSessao);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, "sinteticoProcessoDistribuidoPorServentia", parametros,
					listaTotal);

		} finally

		{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
}
