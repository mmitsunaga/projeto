package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.dt.relatorios.SituacaoAtualExecucaoPenalDt;
import br.gov.go.tj.projudi.ps.RelatorioExecpenwebPs;
import br.gov.go.tj.projudi.ps.RelatorioProcessoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.pdf.InterfaceJasper;


/**
 * 
 * @author kbsriccioppo
 * 
 */
public class RelatorioProcessoExecucaoNe {
	
	protected LogNe obLog;
	protected long QuantidadePaginas = 0;

	/**
	 * Construtor
	 */
	public RelatorioProcessoExecucaoNe() {
		

		obLog = new LogNe(); 

	}

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
	
	/**
	 * Verifica os dados obrigatórios para consulta de Data Provavel
	 * @param dataProvavelDt
	 * @throws Exception
	 * @author kbsriccioppo
	 */
	public String VerificarConsultaDataProvavel(DataProvavelDt dados) {
		String stRetorno = "";

		if (dados.getDataInicialPeriodo().equals("")) stRetorno += "O Campo Data Inicial do Período é obrigatório.";
		if (dados.getTipoConsulta().equals("")) stRetorno += "Informe o parâmetro da consulta.";
		
		return stRetorno;
	}
	
	/**
	 * Verifica os dados obrigatórios para consulta de Processo Com Cálculo
	 * @param dataProvavelDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public String verificarConsultaProcessoComCalculo(DataProvavelDt dados) {
		String stRetorno = "";

		if (dados.getDataInicialPeriodo().equals("")) stRetorno += "O Campo Data Inicial do Período é obrigatório.";
		
		return stRetorno;
	}
	
	/**
	 * Consulta "Data provável" para progressão, livramento condicional ou validade do mandado de prisão
	 * @param dataInicialPeriodo: data início da consulta
	 * @param dataFinalPeriodo: data fim da consulta
	 * @param tipoConsulta: tipo da consulta (PROGRESSAO: Progressão de Regime, LIVRAMENTO: livramento Condicional ou MANDADOPRISAO: Valdiade do Mandado de Prisão)
	 * @param posicaoPaginaAtual: utilizado na paginação
	 * @param idServentia: escopo da consulta 
	 * @return
	 * @throws Exception
	 */
	public List consultarPeriodoDataProvavel(DataProvavelDt dataProvavelDt, String posicaoPaginaAtual, String idServentia) throws Exception{
		List listaSentenciado = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioProcessoExecucaoPs obPersistencia = new RelatorioProcessoExecucaoPs(obFabricaConexao.getConexao());
			listaSentenciado = obPersistencia.consultarPeriodoDataProvavel(dataProvavelDt, posicaoPaginaAtual, idServentia);
			if (listaSentenciado != null && listaSentenciado.size() > 0 && posicaoPaginaAtual.length() > 0) {
				QuantidadePaginas = (Long)listaSentenciado.get(listaSentenciado.size()-1);
				listaSentenciado.remove(listaSentenciado.size()-1);
			}
		} finally {
            obFabricaConexao.fecharConexao();
		}
		return listaSentenciado;
	}
	
	/**
	 * Consulta "Data provável" para progressão, livramento condicional ou validade do mandado de prisão
	 * @param dataInicialPeriodo: data início da consulta
	 * @param dataFinalPeriodo: data fim da consulta
	 * @param tipoConsulta: tipo da consulta (PROGRESSAO: Progressão de Regime, LIVRAMENTO: livramento Condicional ou MANDADOPRISAO: Valdiade do Mandado de Prisão)
	 * @param posicaoPaginaAtual: utilizado na paginação
	 * @param idServentia: escopo da consulta 
	 * @return
	 * @throws Exception
	 */
	public List consultarPeriodoDataProvavel(String dataInicialPeriodo, String dataFinalPeriodo, String tipoConsulta, String posicaoPaginaAtual, String idServentia) throws Exception{
		List listaSentenciado = new ArrayList(); 
		DataProvavelDt dataProvavelDt = new DataProvavelDt();
		
		dataProvavelDt.setDataInicialPeriodo(dataInicialPeriodo);
		dataProvavelDt.setDataFinalPeriodo(dataFinalPeriodo);
		dataProvavelDt.setTipoConsulta(tipoConsulta);
		
		listaSentenciado = consultarPeriodoDataProvavel(dataProvavelDt, posicaoPaginaAtual, idServentia);
		
		return listaSentenciado;
	}
	
	
	/**
	 * lista somente os processo ativos que  tem calculo de liquidação de pena e da serventia do usuario logado, no período informado
	 * @param dataInicialPeriodo: data início do período
	 * @param dataFinalPeriodo: data fim do período
	 * @param posicaoPaginaAtual
	 * @param idServentia: escopo da consulta
	 * @return lista de sentenciado
	 * @throws Exception
	 */
	public List consultarPeriodoCalculo(String dataInicialPeriodo, String dataFinalPeriodo, String posicaoPaginaAtual, String idServentia) throws Exception{
		List listaSentenciado = new ArrayList(); 
		DataProvavelDt dataProvavelDt = new DataProvavelDt();
		
		dataProvavelDt.setDataInicialPeriodo(dataInicialPeriodo);
		dataProvavelDt.setDataFinalPeriodo(dataFinalPeriodo);
		
		listaSentenciado = consultarPeriodoCalculo(dataProvavelDt, posicaoPaginaAtual, idServentia);
		
		return listaSentenciado;
	}
	
	/**
	 * lista somente os processo ativos que tem calculo de liquidação de pena e da serventia do usuario logado, no período informado
	 * @param dataProvavelDt: objeto preenchido
	 * @return lista de sentenciados
	 * @throws Exception
	 */
	public List consultarPeriodoCalculo(DataProvavelDt dataProvavelDt, String posicaoPaginaAtual, String idServentia) throws Exception{
		List listaSentenciado = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioProcessoExecucaoPs obPersistencia = new RelatorioProcessoExecucaoPs(obFabricaConexao.getConexao());
			listaSentenciado = obPersistencia.consultarPeriodoCalculo(dataProvavelDt, posicaoPaginaAtual, idServentia);
			if (listaSentenciado != null && listaSentenciado.size() > 0  && posicaoPaginaAtual.length() > 0) {
				QuantidadePaginas = (Long)listaSentenciado.get(listaSentenciado.size()-1);
				listaSentenciado.remove(listaSentenciado.size()-1);
			}
		} finally {
            obFabricaConexao.fecharConexao();
		}
		return listaSentenciado;
	}
	
	/**
	 * Consulta a situação atual do sentenciado
	 * @param situacaoAtualDt: objeto preenchido com os parâmetros da consulta
	 * @param posicaoPaginaAtual
	 * @param idServentia
	 * @return
	 * @throws Exception
	 */
	public List consultarSituacaoAtualProcessoExecucao1(SituacaoAtualExecucaoDt situacaoAtualDt, String posicaoPaginaAtual, String idServentia, boolean excluirPRD) throws Exception{
		List listaSentenciado = new ArrayList();
			listaSentenciado = this.consultarSituacaoAtualProcessoExecucao(situacaoAtualDt.getIdFormaCumprimento(), situacaoAtualDt.getIdLocalCumprimentoPena(), situacaoAtualDt.getIdRegime(), situacaoAtualDt.getIdEventoExecucaoStatus(), situacaoAtualDt.getIdModalidade(), posicaoPaginaAtual, idServentia, excluirPRD);
		
		return listaSentenciado;
	}
	
	
	/**
	 * Consulta a situação atual do sentenciado
	 * @param idLocalCumprimentoPena: filtro para a pesquisa
	 * @param idRegime: filtro para a pesquisa
	 * @param idSituacao: filtro para a pesquisa
	 * @param idModalidade: filtro para a pesquisa
	 * @param posicaoPaginaAtual
	 * @param idServentia: filtro para a pesquisa
	 * @return lista de sentenciado
	 * @throws Exception
	 */
	public List consultarSituacaoAtualProcessoExecucao(String idFormaCumprimento, String idLocalCumprimentoPena, String idRegime, String idSituacao, String idModalidade, String posicaoPaginaAtual, String idServentia, boolean excluirPRD) throws Exception{
		List listaSentenciado = new ArrayList(); 
		SituacaoAtualExecucaoPenalDt situacaoAtualDt = new SituacaoAtualExecucaoPenalDt();
		
		List listaIdLocal = new ArrayList();
		if (idLocalCumprimentoPena.length() > 0)
			listaIdLocal.add(idLocalCumprimentoPena);
		
		List listaIdRegime = new ArrayList();
		if (idRegime.length() > 0)
			listaIdRegime.add(idRegime);
		
		List listaIdModalidade = new ArrayList();
		if (idModalidade.length() > 0)
			listaIdModalidade.add(idModalidade);
		
		List listaIdStatus = new ArrayList();
		if (idSituacao.length() > 0)
			listaIdStatus.add(idSituacao);
		
		List listaIdFormaCumprimento = new ArrayList();
		if (idFormaCumprimento.length() > 0)
			listaIdFormaCumprimento.add(idFormaCumprimento);
		
		listaSentenciado = consultarSituacaoAtualProcessoExecucao(listaIdFormaCumprimento, listaIdLocal, listaIdRegime, listaIdStatus, listaIdModalidade, posicaoPaginaAtual, idServentia, excluirPRD);

		return listaSentenciado;
	}
	
	public List consultarSituacaoAtualProcessoExecucao(List listaIdFormaCumprimento, List listaIdLocalCumprimentoPena, List listaIdRegime, List listaIdStatus, List listaIdModalidade, String posicaoPaginaAtual, String idServentia, boolean excluirPRD) throws Exception{
		List listaSentenciado = new ArrayList(); 
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioProcessoExecucaoPs obPersistencia = new RelatorioProcessoExecucaoPs(obFabricaConexao.getConexao());
			listaSentenciado = obPersistencia.consultarSituacaoAtualProcessoExecucao(listaIdFormaCumprimento, listaIdLocalCumprimentoPena, listaIdRegime, listaIdModalidade, listaIdStatus, posicaoPaginaAtual, idServentia);
			if (excluirPRD){
				listaSentenciado = this.excluirPenaRestritiva(listaSentenciado);
			}
			if (listaSentenciado != null && listaSentenciado.size() > 0  && posicaoPaginaAtual.length() > 0) {
				QuantidadePaginas = (Long)listaSentenciado.get(listaSentenciado.size()-1);
				listaSentenciado.remove(listaSentenciado.size()-1);
			}
		} finally {
            obFabricaConexao.fecharConexao();
		}
		
		return listaSentenciado;
	}
	
//	public String consultarSituacaoAtualProcessoExecucaoJSON(List listaIdFormaCumprimento, List listaIdLocalCumprimentoPena, List listaIdRegime, List listaIdStatus, List listaIdModalidade, String posicaoPaginaAtual, String idServentia, boolean excluirPRD) throws Exception{
//		String stTemp ="";
//        FabricaConexao obFabricaConexao = null; 
//        
//        try{
//            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
//            RelatorioProcessoExecucaoPs obPersistencia = new RelatorioProcessoExecucaoPs(obFabricaConexao.getConexao());
//			stTemp = obPersistencia.consultarSituacaoAtualProcessoExecucaoJSON(listaIdFormaCumprimento, listaIdLocalCumprimentoPena, listaIdRegime, listaIdModalidade, listaIdStatus, posicaoPaginaAtual, idServentia);
//                        
//        } finally {
//            obFabricaConexao.fecharConexao();
//		}
//        return stTemp;
//	}
	
	public List excluirPenaRestritiva(List lista) throws Exception{
		
		for (int i = 0; i < lista.size(); i++) {
			SituacaoAtualExecucaoPenalDt dados = (SituacaoAtualExecucaoPenalDt) lista.get(i);
			if (dados.getDescricaoModalidade().length() > 0){
				List listaEvento = listarEventos(dados.getIdProcesso());
				if (new ProcessoEventoExecucaoNe().isProcessoPenaRestritivaDireito(listaEvento)){
					lista.remove(dados);
					i--;
				} else {
					((SituacaoAtualExecucaoPenalDt) lista.get(i)).setDescricaoModalidade("");
				}
			}
		}

		return lista;
	}
	
	public List listarEventos(String idProcesso) throws Exception{
		List lista = new ArrayList(); 
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioExecpenwebPs obPersistencia = new  RelatorioExecpenwebPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarEventos(idProcesso);
		} finally {
            obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Consulta os processos sem cálculo de liquidação de pena
	 * @param posicaoPaginaAtual
	 * @param idServentia
	 * @return
	 * @throws Exception
	 */
	public List consultarProcessoSemCalculo(String posicaoPaginaAtual, String idServentia) throws Exception{
		List listaSentenciado = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioProcessoExecucaoPs obPersistencia = new RelatorioProcessoExecucaoPs(obFabricaConexao.getConexao());
			listaSentenciado = obPersistencia.consultarProcessoSemCalculo(posicaoPaginaAtual, idServentia);
			if (listaSentenciado != null && listaSentenciado.size() > 0 && posicaoPaginaAtual.length() > 0) {
				QuantidadePaginas = (Long)listaSentenciado.get(listaSentenciado.size()-1);
				listaSentenciado.remove(listaSentenciado.size()-1);
			}
		
		} finally{
            obFabricaConexao.fecharConexao();
        }
		return listaSentenciado;
	}
	
	/**
	 * Verifica os dados obrigatórios para consulta de Situação Atual estão preenchidos
	 * @param situacaoAtualDt
	 * @throws Exception
	 * @author kbsriccioppo
	 */
	public String VerificarConsultaSituacaoAtual(SituacaoAtualExecucaoDt dados) {

		String stRetorno = "";

		if ((dados.getIdRegime() == null) && (dados.getIdLocalCumprimentoPena() == null) && (dados.getIdEventoExecucaoStatus() == null)) {
			stRetorno += "Informe o(s) parâmetro(s) da consulta.";
		}
		
		return stRetorno;

	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List listarDescricaoRegimeExecucao(String tempNomeBusca, String id_PenaExecucaoTipo, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		RegimeExecucaoNe neObjeto = new RegimeExecucaoNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, id_PenaExecucaoTipo, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();

		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public RegimeExecucaoDt consultarRegimeExecucao(String id_regimeexecucao) throws Exception{
		RegimeExecucaoDt regimeExecucaoDt = new RegimeExecucaoDt();
		RegimeExecucaoNe neObjeto = new RegimeExecucaoNe();
		
		regimeExecucaoDt = neObjeto.consultarId(id_regimeexecucao);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return regimeExecucaoDt;
	}

	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List listarDescricaoLocalCumprimentoPena(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		LocalCumprimentoPenaNe neObjeto = new LocalCumprimentoPenaNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public LocalCumprimentoPenaDt consultarLocalCumprimentoPena(String id_localCumprimentoPena) throws Exception{
		LocalCumprimentoPenaDt localCumprimentoPenaDt = new LocalCumprimentoPenaDt();
		LocalCumprimentoPenaNe neObjeto = new LocalCumprimentoPenaNe();
		
		localCumprimentoPenaDt = neObjeto.consultarId(id_localCumprimentoPena);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return localCumprimentoPenaDt;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List listarDescricaoEventoExecucaoStatus(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		EventoExecucaoStatusNe neObjeto = new EventoExecucaoStatusNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public EventoExecucaoStatusDt consultarEventoExecucaoStatus(String id_eventoExecucaoStatus) throws Exception{
		EventoExecucaoStatusDt eventoExecucaoStatusDt = new EventoExecucaoStatusDt();
		EventoExecucaoStatusNe  neObjeto = new EventoExecucaoStatusNe();
		
		eventoExecucaoStatusDt = neObjeto.consultarId(id_eventoExecucaoStatus);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		return eventoExecucaoStatusDt;
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
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoSituacaoProcessoExecucao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		EventoExecucaoStatusNe neObjeto = new EventoExecucaoStatusNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	public List consultarDescricaoFormaCumprimentoExecucao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		tempList = new FormaCumprimentoExecucaoNe().consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		
		return tempList;
	}
	
	public byte[] imprimirRelatorio(String diretorioProjetos, List lista, String serventia, String tituloRelatorio, String nomeJasper, String observacao, String usuario, String tituloDataBeneficio, String tipoConsulta, String parametroConsulta) throws Exception{
		byte[] temp = null;
		ByteArrayOutputStream baos = null;

		try{
			String pathJasper = diretorioProjetos + "WEB-INF" + File.separator + "relatorios" + File.separator + "processoExecucao" + File.separator + nomeJasper;
			
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", diretorioProjetos + "imagens" + File.separator + "logoTJ.jpg");
			parametros.put("serventia", serventia);
			parametros.put("titulo", tituloRelatorio);
			if (usuario.length() > 0) parametros.put("usuario", usuario);
			if (observacao.length() > 0) parametros.put("observacao", observacao);
			if (tituloDataBeneficio.length() > 0) parametros.put("tituloDataBeneficio", tituloDataBeneficio);
			if (parametroConsulta.length() > 0) parametros.put("parametros", parametroConsulta);
			
			if (tipoConsulta.equals("MANDADOPRISAO"))
				parametros.put("isMandadoPrisao", "true");
			else parametros.put("isMandadoPrisao", "false");
				
			InterfaceJasper ei = new InterfaceJasper(lista);

			JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();

			temp = baos.toByteArray();
			baos.close();
			
		} catch(Exception e) {
			try{if (baos!=null) baos.close();  }catch(Exception e2) {		}
		} finally{
			baos = null;
		}

		return temp;
	}
	
	public List consultarDadosRelatorio(int passoEditar, String posicaoPaginaAtual, String idServentia, String serventia) throws Exception{
		return consultarDadosRelatorio(passoEditar, posicaoPaginaAtual, idServentia, serventia, false);
	}
	
	public List consultarDadosRelatorio(int passoEditar, String posicaoPaginaAtual, String idServentia, String serventia, boolean somenteMaioresDe70Anos) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);;
		try{
			RelatorioProcessoExecucaoPs obPersistencia = new  RelatorioProcessoExecucaoPs(obFabricaConexao.getConexao());
			switch (passoEditar){
				case 7:
					lista = obPersistencia.listarProcessoAtivoServentia(posicaoPaginaAtual, idServentia);
					break;
				case 8:
					lista = obPersistencia.listarProcessoCalculoAtraso(posicaoPaginaAtual, idServentia);
					break;
				case 9:
					lista = obPersistencia.listarProcessoTerminoPenaAtraso(posicaoPaginaAtual, idServentia, serventia);
					break;
				case 10:
					lista = obPersistencia.consultarProcessoProgressaoAtraso(posicaoPaginaAtual, idServentia, serventia);
					break;
				case 11:
					lista = obPersistencia.consultarProcessoLivramentoAtraso(posicaoPaginaAtual, idServentia, serventia);
					break;
				case 12:
					lista = obPersistencia.consultarProcessoMandadoAtraso(posicaoPaginaAtual, idServentia, serventia);
					break;
				case 13:
					lista = obPersistencia.listarProcessoAtivoServentia(posicaoPaginaAtual, idServentia, somenteMaioresDe70Anos);
					break;
			}
			
			
		
		} finally{
            obFabricaConexao.fecharConexao();
        }
		if (posicaoPaginaAtual.length() > 0){
			if (lista != null && lista.size() > 0) {
				QuantidadePaginas = (Long)lista.get(lista.size()-1);
				lista.remove(lista.size()-1);
			}
		}
		
		return lista;
	}
	
	public void setListaRegimeRequest(HttpServletRequest request) throws Exception{
		new ProcessoEventoExecucaoNe().setListaRegimeRequest(request);
	}
}
