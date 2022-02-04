package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ClassificadorPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.projudi.ps.ServentiaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.InterfaceJasper;

// ---------------------------------------------------------
public class ServentiaNe extends ServentiaNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4640168200619485839L;

	/**
	 * VERIFICAR CAMPOS OBRIGATÓRIOS
	 */
	public String Verificar(ServentiaDt dados) {

		String stRetorno = "";

		if (dados.getServentia().equalsIgnoreCase("")) stRetorno += "O Campo Descrição é obrigatório. \n";
		if (dados.getId_ServentiaTipo().equalsIgnoreCase("")) stRetorno += "O Campo Tipo de Serventia é obrigatório. \n";
		if (dados.getId_EstadoRepresentacao().equalsIgnoreCase("")) stRetorno += "O Campo Representatividade é obrigatório. \n";
		//if (dados.getQuantidadeDistribuicao().equalsIgnoreCase("")) stRetorno += "O Campo Quantidade de Distribuição é obrigatório. \n";

		if (dados.getServentiaTipoCodigo().length() > 0 && Funcoes.StringToInt(dados.getServentiaTipoCodigo()) == ServentiaTipoDt.VARA) {
			//Se for Vara, deve informar o código externo para Recebimento de Guias
			if (dados.getServentiaCodigoExterno().length() == 0) stRetorno += "É necessário informar o Código Externo (Código da Serventia no SPG). \n";
			if (dados.getId_ServentiaSubtipo().length() == 0) stRetorno += "É necessário informar o Sub-Tipo da Serventia. \n";
		}
		
		if (dados.getId_ServentiaSubtipo() != null && dados.getId_ServentiaSubtipo().trim().length() > 0){
			try{
				ServentiaSubtipoDt subTipodt = (new ServentiaSubtipoNe()).consultarId(dados.getId_ServentiaSubtipo());
				if (subTipodt != null){					
					dados.setServentiaSubtipo(subTipodt.getServentiaSubtipo());
					dados.setServentiaSubtipoCodigo(subTipodt.getServentiaSubtipoCodigo());
				}
			} catch(Exception e) {e.printStackTrace();}
		}
		
		if (dados.getServentiaSubtipoCodigo() != null && dados.getServentiaSubtipoCodigo().trim().length() > 0){
			if (dados.getServentiaSubtipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO))){
				if (dados.getServentiaTipoCodigo() != null && dados.getServentiaTipoCodigo().trim().length() >= 0 && !dados.isGabineteSegundoGrau()){
					stRetorno += "Para o Sub Tipo de Serventia Gabinete Presidência do Tribunal de Justiça o campo Tipo de Serventia deve ser Gabinete de Desembargador. \n";
				} else {
					ServentiaDt presidenciaTJGO = null;
					try{
						presidenciaTJGO = this.consultarGabinetePresidenciaTJGO();
					} catch(Exception e) {e.printStackTrace();}
					if (presidenciaTJGO != null && !presidenciaTJGO.getId().equalsIgnoreCase(dados.getId())){
						stRetorno += "Ja existe uma serventia com o Sub Tipo Gabinete Presidência do Tribunal de Justiça: " + presidenciaTJGO.getServentia() + ". \n";
					}
				}				
			} else if (dados.isGabineteVicePresidenciaTjgo()){
				if (dados.isGabineteSegundoGrau() ){
					stRetorno += "Para o Sub Tipo de Serventia Gabinete Vice Presidência do Tribunal de Justiça o campo Tipo de Serventia deve ser Gabinete de Desembargador. \n";
				} else {
					ServentiaDt vicePresidenciaTJGO = null;
					try{
						vicePresidenciaTJGO = this.consultarGabineteVicePresidenciaTJGO();
					} catch(Exception e) {e.printStackTrace();}
					if (vicePresidenciaTJGO != null && !vicePresidenciaTJGO.getId().equalsIgnoreCase(dados.getId())){
						stRetorno += "Ja existe uma serventia com o Sub Tipo Gabinete Vice Presidência do Tribunal de Justiça: " + vicePresidenciaTJGO.getServentia() + ". \n";
					}
				}				
			}
		}
		
		if (dados.getDataImplantacao().equalsIgnoreCase("")) stRetorno += "O Campo Data de Implantação é obrigatório. \n";

		// Valida dados do endereço
		stRetorno += new EnderecoNe().Verificar(dados.getEnderecoDt());

		return stRetorno;

	}

	public byte[] relProcessosServentia(String stCaminho) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		ByteArrayOutputStream baos = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());

			List liProcessosServentia = obPersistencia.relProcessoServentia();

			InterfaceJasper ei = new InterfaceJasper(liProcessosServentia);

			// PATH PARA OS ARQUIVOS JASPER DO RELATORIO
			// MOVIMENTACAO*******************************************************
			String pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "processosVaras.jasper";

			// parâmetros do relatório
			Map parametros = new HashMap();
			parametros.put("DataReferencia", "");

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
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}

	/**
	 * Consulta dados básicos de uma serventia pelo Id passado
	 * @param id_serventia, identificação da serventia
	 * 
	 * @author msapaula
	 */
	public ServentiaDt consultarIdSimples(String id_serventia) throws Exception {
		ServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdSimples(id_serventia);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta Serventia SubTipo Codigo de uma serventia pelo Id passado
	 * 
	 * @param id_serventia
	 *            , identificação da serventia
	 * 
	 * @author lsbernardes
	 */
	public String consultarServentiaSubTipoCodigo(String id_serventia, FabricaConexao obFabricaConexao) throws Exception {
		String serventiaSubTipoCodigo = null;
		
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		serventiaSubTipoCodigo = obPersistencia.consultarServentiaSubTipoCodigo(id_serventia);
		
		return serventiaSubTipoCodigo;
	}

	/**
	 * Sobrescrevendo método para permitir salvar o endereço da serventia
	 * 
	 * @author leandro
	 */
	public void salvar(ServentiaDt dados, ServentiaAreaDistribuicaoDt servAreaDistDt) throws Exception {
		LogDt obLogDt;
		EnderecoNe enderecoNe = new EnderecoNe();
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		ServentiaAreaDistribuicaoNe servAreaDistNe = new ServentiaAreaDistribuicaoNe();
		FabricaConexao obFabricaConexao = null;
		UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
		String id_ServentiaTipoInicial;
		String id_ServentiaTipoNovo;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());

			//verifico se area de distribuicao foi alterada ou nao
			//if (!dados.getId_AreaDistribuicao().equalsIgnoreCase(obDados.getId_AreaDistribuicao()) || !dados.getQuantidadeDistribuicao().equalsIgnoreCase(obDados.getQuantidadeDistribuicao())) {
			//}

			// Salva endereço da serventia
			enderecoNe.salvar(dados.getEnderecoDt(), obFabricaConexao);
			dados.setId_Endereco(dados.getEnderecoDt().getId());

			// Salva/Altera dados da serventia
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Serventia", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());

				// SALVA SERVENTIA RELACIONADA***********************************************************
				if (dados.getServentiaDtRelacaoEdicao() != null && dados.getServentiaDtRelacaoEdicao().getId() != null && !dados.getServentiaDtRelacaoEdicao().getId().equalsIgnoreCase("")) {
					ServentiaRelacionadaDt serventiaRelacionada = new ServentiaRelacionadaDt();
					serventiaRelacionada.setId_ServentiaPrincipal(dados.getId());
					serventiaRelacionada.setId_ServentiaRelacao(dados.getServentiaDtRelacaoEdicao().getId());
					serventiaRelacionada.setId_UsuarioLog(dados.getId_UsuarioLog());
					serventiaRelacionada.setIpComputadorLog(dados.getIpComputadorLog());
					serventiaRelacionadaNe.salvar(serventiaRelacionada, obFabricaConexao);
				}
				// *****************************************************************************************
				
				// SALVA AREA DISTRIBUICAO ***********************************************************
				if (servAreaDistDt.getId_AreaDist() != null && !servAreaDistDt.getId_AreaDist().equalsIgnoreCase("")) {
					servAreaDistDt.setId_Serv(dados.getId());
					servAreaDistDt.setId_UsuarioLog(dados.getId_UsuarioLog());
					servAreaDistDt.setIpComputadorLog(dados.getIpComputadorLog());
					servAreaDistNe.salvar(servAreaDistDt, obFabricaConexao);
				}
				// *****************************************************************************************

			} else {
				obPersistencia.alterar(dados);
 				obDados = this.consultarId(dados.getId());			
				id_ServentiaTipoInicial = obDados.getId_ServentiaTipo();
				id_ServentiaTipoNovo = dados.getId_ServentiaTipo();
				if (obDados.isServentiaAdvogadoProcurador() && dados.isServentiaAdvogadoProcurador()) {
    				if (!id_ServentiaTipoInicial.equals(id_ServentiaTipoNovo)) {
    					usuarioServentiaGrupoNe.alteraGrupoAdvogados(dados.getId(), id_ServentiaTipoNovo, obFabricaConexao);
    				}
				}
				obLogDt = new LogDt("Serventia", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

			//-----------CONTROLE DE DISTRIBUIÇÃO--------------------------------------------------------------------                                    
			//Verifico se está sendo uma serventia que podem particiar das distribuições
//			if ((Funcoes.StringToInt(dados.getServentiaTipoCodigo()) == ServentiaTipoDt.VARA) || (Funcoes.StringToInt(dados.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU)) {
//				//verifico se a serventia tem valor na quantidade de distribuição
//				if (boAlteracaoArea) DistribuicaoProcesso.getInstance().lerDados(dados.getId_AreaDistribuicao());
//			}

			//-------------------------------------------------------------------------------
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			dados.getEnderecoDt().setId("");
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva serventia relacionda a serventia principal
	 * 
	 * @author leandro
	 */
	public void SalvarServentiaRelacionada(ServentiaDt serventiaDt) throws Exception { 
		
		if (serventiaDt.getServentiaDtRelacaoEdicao() == null) {
			throw new MensagemException("Erro ao salvar a serventia relacionada, objeto não possui referência.");
		}
		
			
		ServentiaRelacionadaDt serventiaRelacionada = serventiaDt.getServentiaDtRelacaoEdicao();
		serventiaRelacionada.setId_UsuarioLog(serventiaDt.getId_UsuarioLog());
		serventiaRelacionada.setIpComputadorLog(serventiaDt.getIpComputadorLog());
		
//		ServentiaRelacionadaDt serventiaRelacionada = new ServentiaRelacionadaDt();
//		serventiaRelacionada.setId(serventiaDt.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao());
//		
//		serventiaRelacionada.setId_ServentiaPrincipal(serventiaDt.getId());
//		
//		serventiaRelacionada.setId_ServentiaRelacao(serventiaDt.getServentiaDtRelacaoEdicao().getId());
//		serventiaRelacionada.setRecebeProcesso(serventiaDt.getServentiaDtRelacaoEdicao().getRecebeProcesso());
//		if (serventiaDt.getServentiaDtRelacaoEdicao().isSubstitutoSegundoGrau()){
//			serventiaRelacionada.setId_ServentiaSubstituicao(serventiaDt.getServentiaDtRelacaoEdicao().getId_ServentiaSubstituicao());
//			serventiaRelacionada.setDataInicialSubstituicao(serventiaDt.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao());
//			serventiaRelacionada.setDataFinalSubstituicao(serventiaDt.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao());
//		} 
//		serventiaRelacionada.setId_UsuarioLog(serventiaDt.getId_UsuarioLog());
//		serventiaRelacionada.setIpComputadorLog(serventiaDt.getIpComputadorLog());
//		
//		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();		
//		serventiaRelacionadaNe.salvar(serventiaRelacionada);
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		serventiaRelacionadaNe.salvar(serventiaRelacionada);
		serventiaRelacionadaNe = null;
	}
	
	/**
	 * Salva area distribuicao da serventia
	 * 
	 * @author leandro
	 */
	public void SalvarAreaDistribuicaoServentia(ServentiaAreaDistribuicaoDt servAreaDistDt, UsuarioDt usuarioDt) throws Exception {
		ServentiaAreaDistribuicaoNe servAreaDistNe = new ServentiaAreaDistribuicaoNe();
		servAreaDistNe.salvar(servAreaDistDt, usuarioDt);
	}

	/**
	 * Exclui serventia relacionda da serventia principal
	 * 
	 * @author leandro
	 */
	public void excluiServentiaRelacionada(ServentiaDt serventiaDt) throws Exception {
		if (serventiaDt.getServentiaDtRelacaoEdicao() == null) throw new MensagemException("Erro ao salvar a serventia relacionada, objeto não possui referência.");
		
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		ServentiaRelacionadaDt serventiaRelacionadaDt = serventiaDt.getServentiaDtRelacaoEdicao();
		serventiaRelacionadaDt.setId_UsuarioLog(serventiaDt.getId_UsuarioLog());
		serventiaRelacionadaDt.setIpComputadorLog(serventiaDt.getIpComputadorLog());
		serventiaRelacionadaNe.excluir(serventiaRelacionadaDt);
		serventiaDt.removerServentiaRelacionadaLista(serventiaRelacionadaDt);
	}
	
	/**
	 * Exclui área distribuição da serventia
	 * 
	 * @author leandro
	 */
	public void excluiAreaDistribuicaoServentia(ServentiaAreaDistribuicaoDt servAreaDistDt, UsuarioDt usuarioDt) throws Exception {
		ServentiaAreaDistribuicaoNe servAreaDistNe = new ServentiaAreaDistribuicaoNe();
		ServentiaAreaDistribuicaoDt areaDistribuicao = servAreaDistNe.consultarId(servAreaDistDt.getId());
		areaDistribuicao.setId_UsuarioLog(servAreaDistDt.getId_UsuarioLog());
		areaDistribuicao.setIpComputadorLog(servAreaDistDt.getIpComputadorLog());
		servAreaDistNe.excluir(areaDistribuicao, usuarioDt);
	}

	/**
	 * Consulta serventias do tipo Vara e Turma Recursal. Esse método é
	 * utilizada para retornar todas as serventias em que haverá distribuição de
	 * processos para que sejam armazenados em memória os ponteiros para
	 * distribuição.
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasVaraTurma() throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasVaraTurma();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Retorna todas as serventias que serão utilizadas na distribuição de processos para serventias cargos. 
	 * Esse método é chamado ao iniciar o sistema, e deve retornar todas as serventias do tipo: 
	 * 		- Vara: os cargos desse tipo de serventia serão os juízes que podem ser responsáveis por processos, 
	 * 		- Turma: os cargos desse tipo de serventia serão os relatores que podem ser responsáveis, e 
	 * 		- Promotoria: os cargos desse tipo serão os promotores que também podem ser responsáveis por processos.
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasDistribuicaoProcesso() throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasDistribuicaoProcesso();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Consultar a descrição de uma serventia de acordo com Id passado
	 * 
	 * @param id_Serventia,
	 *            identificação da serventia
	 * 
	 * @author msapaula
	 */
	public String consultarServentiaDescricao(String id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
		String descricaoServentia = null;
		
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		descricaoServentia = obPersistencia.consultarServentiaDescricao(id_Serventia);
				
		return descricaoServentia;
	}

	/**
	 * Consultar determinada serventia de acordo com Código (ServentiaCodigo)
	 * 
	 * @param serventiaCodigo, codigo a ser consultado
	 * @author msapaula
	 */
	public ServentiaDt consultarServentiaCodigo(String serventiaCodigo) throws Exception {
		ServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaCodigo(serventiaCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consultar determinada serventia de acordo com Código (ServentiaCodigo)
	 * 
	 * @param serventiaCodigo, codigo a ser consultado
	 * @author msapaula
	 */
	public ServentiaDt consultarServentiaCodigo(String serventiaCodigo, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarServentiaCodigo(serventiaCodigo);		
	}

	/**
	 * Consultar determinada serventia de acordo com Código (ServentiaCodigoExterno)
	 * 
	 * @param serventiaCodigoExterno, codigo a ser consultado
	 * @author msapaula
	 */
	public ServentiaDt consultarServentiaCodigoExterno(String serventiaCodigoExterno) throws Exception {
		ServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaCodigoExterno(serventiaCodigoExterno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consultar determinada serventia de acordo com Código (ServentiaCodigoExterno)
	 * 
	 * @param serventiaCodigoExterno, codigo a ser consultado
	 * @param fabricaConexao, conexão com o banco de dados
	 * @author msapaula
	 */
	public ServentiaDt consultarServentiaCodigoExterno(String serventiaCodigoExterno, FabricaConexao fabricaConexao) throws Exception {
		ServentiaPs obPersistencia = new ServentiaPs(fabricaConexao.getConexao());
		return obPersistencia.consultarServentiaCodigoExterno(serventiaCodigoExterno);
	}

	/**
	 * Consulta o código externo de uma serventia, de acordo com o código passado (ServentiaCodigo)
	 * 
	 * @param serventiaCodigo, código a ser consultado
	 * @author msapaula
	 */
	public String consultarCodigoExterno(String serventiaCodigo) throws Exception {
		String stRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stRetorno = obPersistencia.consultarCodigoExterno(serventiaCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Consulta todas as serventias ativas de acordo com tipo desejado
	 * (ServentiaTipoCodigo)
	 * 
	 * @param serventiaTipoCodigo
	 *            tipo da serventia a ser filtrada
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private List consultarServentiasAtivas(String serventiaTipoCodigo) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasAtivas(serventiaTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consultar serventias ativas, podendo filtrar pelo Tipo (ServentiaTipoCodigo) ou pela Comarca, e faz uso de paginação
	 * 
	 * @param descricao,  filtro da pesquisa
	 * @param posicao, parametro para paginação
	 * @param serventiaTipoCodigo1Grau, tipo da serventia a ser filtrada
	 * @param serventiaTipoCodigo2Grau, tipo da serventia a ser filtrada
	 * @param id_Comarca, identificação da comarca para filtrar as serventias
	 * 
	 * @author lsbernardes
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo1Grau , String serventiaTipoCodigo2Grau, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasAtivas(descricao, posicao, serventiaTipoCodigo1Grau, serventiaTipoCodigo2Grau, id_Comarca, serventiaSubTipoCodigo);
			stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consultar serventias ativas, podendo filtrar pelo Tipo (ServentiaTipoCodigo) ou pela Comarca, e faz uso de paginação
	 * 
	 * @param descricao,  filtro da pesquisa
	 * @param posicao, parametro para paginação
	 * @param serventiaTipoCodigo, tipo da serventia a ser filtrada
	 * @param id_Comarca, identificação da comarca para filtrar as serventias
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasAtivas(descricao, posicao, serventiaTipoCodigo, id_Comarca, serventiaSubTipoCodigo);
			stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consultar serventias ativas, podendo filtrar pelo Tipo (ServentiaTipoCodigo) e faz uso de paginação
	 * 
	 * @param descricao,  filtro da pesquisa
	 * @param posicao, parametro para paginação
	 * @param serventiaTipoCodigo, tipo da serventia a ser filtrada
	 * 
	 * @author asrocha 20/11/2014
	 */
	public String consultarServentiasJuridicasJSON(String descricao, String posicao, int[] serventiaTipoCodigo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasJuridicasJSON(descricao, posicao, serventiaTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consultar serventias ativas, podendo filtrar pelo Tipo (ServentiaTipoCodigo) ou pela Comarca, e faz uso de paginação
	 * 
	 * @param descricao,  filtro da pesquisa
	 * @param posicao, parametro para paginação
	 * @param serventiaTipoCodigo, tipo da serventia a ser filtrada
	 * @param id_Comarca, identificação da comarca para filtrar as serventias
	 * 
	 * @author lsbernardes
	 */
	public List consultarServentiasAtivasRedistribuicaoLote(String descricao, String posicao, String serventiaTipoCodigo, String id_Comarca) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasAtivasRedistribuicaoLote(descricao, posicao, serventiaTipoCodigo, id_Comarca);
			stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
    public String consultarServentiasAtivasRedistribuicaoLoteJSON(String descricao, String posicao, UsuarioNe usuarioSessao) throws Exception {
    	String stTemp = null;
        FabricaConexao obFabricaConexao = null;
        
        List listaServentiaSubTipoCodigo = new ArrayList();
        if(usuarioSessao.isDistribuidor2Grau()) {
        	listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CAMARA_CIVEL);
        	listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CAMARA_CRIMINAL);
        	listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.SECAO_CIVEL);
        	listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.SECAO_CRIMINAL);
        	listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
        	listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CORTE_ESPECIAL);
        	listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);        	
        }else if (usuarioSessao.isTurma()){
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CAMARA_CIVEL);		
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.SECAO_CIVEL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.SECAO_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CORTE_ESPECIAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
		} else {
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAMILIA_CAPITAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAMILIA_INTERIOR);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_FAMILIA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.PRECATORIA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.VARAS_CIVEL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.PREPROCESSUAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.PROTOCOLO_JUDICIAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.VARA_CRIMINAL);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_CUSTODIA);
			listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_SUCESSOES);
			if (usuarioSessao.isDistribuidor1Grau()){
				listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
				listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
				listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
				listaServentiaSubTipoCodigo.add(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
			}
		}
		
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarServentiasAtivasRedistribuicaoLoteJSON(descricao, posicao, listaServentiaSubTipoCodigo, usuarioSessao.getUsuarioDt().getId_Comarca());
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }

	/**
	 * Consultar serventias para o cadastro de usuário. Se usuário logado é um
	 * Administrador poderá visualizar todas as serventias ativas, caso
	 * contrário retorna as serventias de acordo com o ServentiaTipo passado e
	 * retirando as que sejam do tipo Administração, OAB e Sistema Projudi.
	 * 
	 * @param descricao
	 * @param posicao
	 * @param serventiaTipo,
	 *            tipo de serventia a ser filtrado
	 * @param grupoCodigo,
	 *            grupo do usuário logado
	 * @author msapaula
	 */
	public List consultarServentiasHabilitacaoUsuarios(String descricao, String posicao, String serventiaTipo, String grupoCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			int grupo = Funcoes.StringToInt(grupoCodigo);

			if (grupo == GrupoDt.ADMINISTRADORES) tempList = obPersistencia.consultarServentiasAtivas(descricao, posicao, serventiaTipo, null, null);
			else tempList = obPersistencia.consultarServentiasHabilitacaoUsuarios(descricao, posicao, serventiaTipo);

			if (tempList.size() > 0) {
				QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
				tempList.remove(tempList.size() - 1);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Encapsulamento para consulta de serventias ativas de um determinado tipo
	 * não devendo levar em consideração o filtro de comarca
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo) throws Exception{
		return this.consultarServentiasAtivas(descricao, posicao, serventiaTipoCodigo, null, null);
	}
	
	/**
	 * Encapsulamento para consulta de serventias ativas de um determinado tipo
	 * não devendo levar em consideração o filtro de comarca
	 * 
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo1Grau, String serventiaTipoCodigo2Grau) throws Exception{
		return this.consultarServentiasAtivas(descricao, posicao, serventiaTipoCodigo1Grau, serventiaTipoCodigo2Grau, null, null);
	}

	/**
	 * Retorna todas as serventias ativas disponíveis para redistribuição de processos. 
	 * Dependendo do tipo da serventia do usuário ele poderá visualizar serventias relacionadas a sua.
	 * 
	 * @param descricao,  filtro para pesquisa
	 * @param posicao,  parametro para paginação
	 * @param serventiaTipoCodigo, tipo da serventia do usuário que está realizando a consulta
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarServentiasRedistribuicao(String descricao, String posicao, String serventiaTipoCodigo) throws Exception{
		List lista = null;

		switch (Funcoes.StringToInt(serventiaTipoCodigo)) {
			case ServentiaTipoDt.VARA:
				lista = this.consultarVarasAtivas(descricao, posicao);
				break;

			case ServentiaTipoDt.SEGUNDO_GRAU:
				lista = this.consultarServentiasVarasSegundoGrauAtivas(descricao, posicao);
				break;
		}
		return lista;
	}
	
	/**
	 * Retorna todas as serventias ativas disponíveis para redistribuição de processos. 
	 * Dependendo do tipo da serventia do usuário ele poderá visualizar serventias relacionadas a sua.
	 * 
	 * @param descricao,  filtro para pesquisa
	 * @param posicao,  parametro para paginação
	 * @param serventiaTipoCodigo, tipo da serventia do usuário que está realizando a consulta
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarServentiasRedistribuicaoLote(String descricao, String posicao, String serventiaTipoCodigo) throws Exception{
		List lista = null;

		switch (Funcoes.StringToInt(serventiaTipoCodigo)) {
			case ServentiaTipoDt.VARA:
				lista = this.consultarVarasAtivas(descricao, posicao);
				break;

			case ServentiaTipoDt.SEGUNDO_GRAU:
				lista = this.consultarServentiasVarasSegundoGrauAtivas(descricao, posicao);
				break;
		}
		return lista;
	}

	/**
	 * Retorna todas as serventias ativas que sejam do tipo Vara, sem paginação
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarVarasAtivas() throws Exception{
		return this.consultarServentiasAtivas(String.valueOf(ServentiaTipoDt.VARA));
	}

	/**
	 * Retorna as serventias ativas que sejam do tipo Vara, com uso de paginação
	 * 
	 * @author msapaula
	 */
	public List consultarVarasAtivas(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivas(descricao, posicao, String.valueOf(ServentiaTipoDt.VARA));
	}

	/**
	 * Retorna as serventias ativas que sejam do tipo Turma, com paginação
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasSegundoGrauAtivas(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivas(descricao, posicao, String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU));
	}

	/**
	 * Retorna as serventias ativas que sejam do tipo Turma, com paginação
	 * 
	 * @author jrcorrea
	 * 02/09/2014
	 */
	public String consultarServentiasSegundoGrauAtivasJSON(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivasJSON(descricao, posicao, String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU));
	}
	
	/**
	 * Retorna as serventias ativas que sejam do tipo Turma, com paginação
	 * 
	 * @author lsbernardes
	 */
	public List consultarServentiasVarasSegundoGrauAtivas(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivas(descricao, posicao, String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU));
	}

	/**
	 * Realiza chamada ao método para retornar somentes as serventias do tipo
	 * OAB
	 */
	public List consultarDescricaoOab(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivas(descricao, posicao, Integer.toString(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL));
	}

	/**
	 * Realizada chamada ao método para retornar as serventias que um
	 * determinado usuário pode visualizar
	 * @throws Exception 
	 */
	public List consultarServentia(String descricao, String posicao, UsuarioDt usuarioDt) throws Exception{
		List lista = null;
		//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		switch (grupoTipo) {
//			case GrupoDt.CONTADORES_VARA:
//			case GrupoDt.DISTRIBUIDOR:
			case GrupoTipoDt.CONTADOR:
			case GrupoTipoDt.DISTRIBUIDOR:
				// O contador irá visualizar somente as serventias de sua comarca
				lista = this.consultarServentiasAtivas(descricao, posicao, Integer.toString(ServentiaTipoDt.VARA), usuarioDt.getId_Comarca(), null);
				break;

			default:
				// Os usuários internos, advogado e consultor podem acessar os processos em todas serventias
				// lista = this.consultarDescricaoServentiaTipoCodigo(descricao, posicao, Integer.toString(ServentiaTipoDt.VARA), null);
				lista = this.consultarServentiasVaraTurma(descricao, posicao);
				break;
		}
		return lista;
	}
	
	public String consultarServentiaJSON(String descricao, String posicao, UsuarioDt usuarioDt) throws Exception{
		String stTemp ="";
		//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		switch (grupoTipo) {
//			case GrupoDt.CONTADORES_VARA:
//			case GrupoDt.DISTRIBUIDOR:
			case GrupoTipoDt.CONTADOR:
			case GrupoTipoDt.DISTRIBUIDOR:
				// O contador irá visualizar somente as serventias de sua comarca
				stTemp = this.consultarServentiasAtivasJSON(descricao, posicao, Integer.toString(ServentiaTipoDt.VARA), usuarioDt.getId_Comarca(), null);
				break;

			default:
				// Os usuários internos, advogado e consultor podem acessar os processos em todas serventias
				// lista = this.consultarDescricaoServentiaTipoCodigo(descricao, posicao, Integer.toString(ServentiaTipoDt.VARA), null);
				stTemp = this.consultarServentiasVaraTurmaJSON(descricao, posicao);
				break;
		}
		return stTemp;
	}
	

	/**
	 * Consultar serventias do tipo Vara e Turma Recursal com uso de paginação
	 * 
	 * @param descricao,
	 *            filtro da pesquisa
	 * @param posicao,
	 *            parametro para paginação
	 * @author msapaula
	 */
	public List consultarServentiasVaraTurma(String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasVaraTurma(descricao, posicao);
			stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarServentiasVaraTurmaJSON(String descricao, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaPs obPersistencia = new  ServentiaPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarServentiasVaraTurmaJSON(descricao, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}

	/**
	 * Consulta as serventias disponíveis em uma área de distribuição
	 * 
	 * @param id_AreaDistribuicao,
	 *            identificação da área de distribuição
	 * @author msapaula
	 */
	
//	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String id_ProcessoTipo){
//	    return consultarServentiasAreaDistribuicao(id_AreaDistribuicao,id_ProcessoTipo, "");
//	}
	
	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String id_ProcessoTipo, String Id_Serventia) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasAreaDistribuicao(id_AreaDistribuicao, id_ProcessoTipo, Id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String Id_Serventia) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasAreaDistribuicao(id_AreaDistribuicao,  Id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String id_ProcessoTipo, String Id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
		String stTemp = "";
		
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		stTemp = obPersistencia.consultarServentiasAreaDistribuicao(id_AreaDistribuicao, id_ProcessoTipo, Id_Serventia);
		
		return stTemp;
	}
	
//	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String Id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
//		String stTemp = "";
//		
//		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
//		stTemp = obPersistencia.consultarServentiasAreaDistribuicao(id_AreaDistribuicao, Id_Serventia);
//				
//		return stTemp;
//	}

	/**
	 * Retorna as serventias relacionadas a uma serventia passada
	 * 
	 * @param idServentiaPrincipal,
	 *            serventia principal
	 */
	public List<ServentiaRelacionadaDt> consultarServentiasRelacionadas(String id_ServentiaPrincipal) throws Exception {
		List liTemp = null;
			
		ServentiaRelacionadaNe obPersistencia = new ServentiaRelacionadaNe();
		liTemp = obPersistencia.consultarServentiasRelacionadas(id_ServentiaPrincipal);
		
		return liTemp;
	}
	
	/**
	 * Retorna as areas distribuições relacionadas a uma serventia passada
	 * 
	 * @param idServentia,
	 *            serventia
	 */
	public List consultarAreasDistribuicoesServentia(String id_Serventia) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarAreasDistribuicoesServentia(id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	public List consultarDescricaoBairro(String tempNomeBusca, String cidade, String uf, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		BairroNe neObjeto = new BairroNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, cidade, uf, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;

	}

	public List consultarDescricaoEstado(String descricao, String posicao) throws Exception {
		List tempList = null;
		EstadoNe neObjeto = new EstadoNe();
		
		tempList = neObjeto.consultarDescricao(descricao, posicao);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;

	}

	public List consultarAreasDistribuicaoComarcaServentiaSubTipo(String id_Comarca, String id_ServentiaSubTipo, String posicao) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		
		tempList = neObjeto.consultarAreasDistribuicaoComarcaServentiaSubTipo("", id_Comarca, id_ServentiaSubTipo, posicao);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	public List consultarAreasDistribuicaoComarcaServentiaSubTipo(String id_Comarca, String posicao) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		
		tempList = neObjeto.consultarAreasDistribuicaoComarcaServentiaSubTipo("", id_Comarca, "", posicao);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	public List consultarAreasDistribuicaoRecursal() throws Exception{
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		
		tempList = neObjeto.consultarAreasDistribuicaoSegundoGrauAtivas("", "0");
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Consultar serventias que sejam do tipo OAB ou PROCURADORIA_MUNICIPAL, PROCURADORIA_ESTADUAL,
	 * PROCURADORIA_ASSISTENCIA_JUDICIARIA ou PROCURADORIA_UNIAO para serem utilizadas no cadastro de advogado.
	 * 
	 * @param descricao
	 * @param posicao
	 */

	public List consultarServentiasHabilitacaoAdvogado(String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasHabilitacaoAdvogado(descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consultar serventias filtrando pela Serventia Tipo
	 */
	public List consultarDescricaoServentia(String descricao, String serventiaTipo, UsuarioDt usuarioDt, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoServentia(descricao, serventiaTipo, usuarioDt, posicao);
			stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarDescricaoServentiaJSON(String descricao, String serventiaTipo, UsuarioDt usuarioDt, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaPs obPersistencia = new  ServentiaPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoServentiaJSON(descricao, serventiaTipo, usuarioDt, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
	
	/**
	 * Consultar serventias 
	 */
	public List consultarDescricaoServentiaAlterarResponsavelConclusao(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoServentiaAlterarResponsavelConclusao(descricao, usuarioDt, posicao);
			stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}	
	
	public String consultarDescricaoServentiaAlterarResponsavelConclusaoJSON(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaAlterarResponsavelConclusaoJSON(descricao, usuarioDt, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consultar serventias filtrando pela Serventia Tipo Codigo - webservice
	 */
	public List consultarServentiaTipoCodigo(String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiaTipoCodigo(posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Retorna o status da serventia
	 * 
	 * @return
	 */
	public int getStatus() {
		if (obDados.getCodigoTemp().equalsIgnoreCase("")) {return 1; }
		return 0;
	}

	/**
	 * Método para consultar Serventia por Id, baseado em uma conexão já existente
	 * @param id_serventia
	 * @param conexao
	 */
	public ServentiaDt consultarId(String id_serventia, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaDt dtRetorno = null;
		
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarId(id_serventia);
		if (dtRetorno != null) obDados.copiar(dtRetorno);
				
		return dtRetorno;
	}
	
	/**
	 * Método para consultar Serventia anterior do processo na conversão do processo em recurso
	 * @param id_serventia
	 * @param conexao
	 */
	public String consultarServentiaAteriorProcesso(String id_Processo, String id_ServentiaAtual, FabricaConexao obFabricaConexao) throws Exception {
		String id_ServentiaAnterior = "";
		
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		id_ServentiaAnterior = obPersistencia.consultarServentiaAnteriorProcesso(id_Processo, id_ServentiaAtual);
		
		return id_ServentiaAnterior;
	}
	
	/**
	 * Método que consulta a lista de Serventias de uma Comarca.
	 * @param idComarca - ID da Comarca
	 * @return lista de Serventias
	 * @throws Exception
	 * @author hmgodinho
	 * @author jrcorrea
	 * @data 15/09/2015
	 */
	public List consultarServentiasComarca(String idComarca, String serventiaTipoCodigo) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasComarca(idComarca, serventiaTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}
	

	public String consultarServentiaAreaDistribuicaoPassada(String id_AreaDistribuicao, String id_Processo, String id_serventia) throws Exception {
		
		String stId_Serventia = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stId_Serventia = obPersistencia.consultarServentiaAreaDistribuicaoPassada(id_AreaDistribuicao, id_Processo, id_serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stId_Serventia;
	}
	
	public String consultarServentiasHabilitacaoAdvogadoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasHabilitacaoAdvogadoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiasCentralMandadosJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs( obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasCentralMandadosJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Método para verificar se a serventia é de segundo grau.
	 * @param String id_serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isServentiaSegundoGrau(String id_serventia) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isServentiaSegundoGrau(id_serventia);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public String consultarServentiasDistribuidorAtivoJSON(String descricao, String posicao, String idComarca) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasDistribuidorAtivoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null, idComarca);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarServentiasHabilitacaoUsuariosJSON(String descricao, String posicao, String serventiaTipo, String grupoCodigo, String idComarca) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			int grupo = Funcoes.StringToInt(grupoCodigo);

			if (grupo == GrupoDt.ADMINISTRADORES) { 
				stTemp = obPersistencia.consultarServentiasAtivasJSON(descricao, posicao, serventiaTipo);
			} else if (grupo == GrupoDt.CADASTRADOR_MASTER) { 
				stTemp = obPersistencia.consultarServentiasHabilitacaoMasterJSON(descricao, posicao, serventiaTipo);
			} else {
				stTemp = obPersistencia.consultarServentiasHabilitacaoUsuariosJSON(descricao, posicao, serventiaTipo, idComarca);
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiasHabilitacaoUsuariosJSON(String descricao, String posicao, String serventiaTipo, String grupoCodigo) throws Exception {
		return consultarServentiasHabilitacaoUsuariosJSON(descricao, posicao, serventiaTipo, grupoCodigo, null);
	}
	
	/**
	 * Aplica as regras para a serventia do tipo Presidência
	 * 
	 * @param dados
	 */
	public String VerificarPresidenciaDaCamara(ServentiaRelacionadaDt dados) {

		String stRetorno = "";
		
		if (!dados.isGabineteDesembargadorServentiaRelacionada()) {			
			stRetorno = "Favor selecionar uma serventia com tipo igual a Gabinete de Desembargador.";
		}
		
		return stRetorno;
	}
	
	/**
     * Atualiza uma serventia relacionada como presidência
     * 
     * @param id_SeventiaPrincipal
     * @param id_ServentiaRelacionadaPresidencia
     * @throws Exception
     * @author mmgomes
     */
    public void atualizeServentiaRelacionadaPresidencia(String id_SeventiaPrincipal, String id_ServentiaRelacionadaPresidencia) throws Exception{
		ServentiaRelacionadaNe serventiaRelacionadaNe = null;
		
		serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		serventiaRelacionadaNe.atualizeServentiaRelacionadaPresidencia(id_SeventiaPrincipal, id_ServentiaRelacionadaPresidencia);
		serventiaRelacionadaNe = null;
		
	}

    /***
     * Desativa uma serventia
     * 
     * @param idServentia
     * @throws Exception
     * @author mmgomes
     */
	public void ativarServentia(ServentiaDt serventiaDt) throws Exception{
		
		this.alterarStatusServentia(serventiaDt, String.valueOf(ServentiaDt.INATIVO), String.valueOf(ServentiaDt.ATIVO));
							
	}

	/**
	 * Ativa uma serventia
	 * 
	 * @param idServentia
	 * @throws Exception
	 * @author mmgomes
	 */
	public void desativarServentia(ServentiaDt serventiaDt) throws Exception{		
		
		this.alterarStatusServentia(serventiaDt, String.valueOf(ServentiaDt.ATIVO), String.valueOf(ServentiaDt.INATIVO));		
			
	}
	
	/**
	 * Altera status da serventia
	 * 
	 * @param idServentia
	 * @param usuarioDt
	 * @param statusAtual
	 * @param statusNovo
	 * @throws Exception
	 * @author mmgomes
	 */
	private void alterarStatusServentia(ServentiaDt serventiaDt, String statusAtual, String novoStatus) throws Exception{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			obFabricaConexao.iniciarTransacao();
			
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			
			String valorAtual = "[Id_Serv:" + serventiaDt.getId() + ";Ativo:" + statusAtual + "]";
			String valorNovo = "[Id_Serv:" + serventiaDt.getId() + ";Ativo:" + novoStatus + "]";
			
			LogDt obLogDt = new LogDt("Serventia", serventiaDt.getId(), serventiaDt.getId_UsuarioLog(), serventiaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			
			obPersistencia.alterarStatusServentia(serventiaDt.getId(), novoStatus);
			
			obLog.salvar(obLogDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	public BairroDt consultarDescricaoBairroId(String idBairro) throws Exception {
		BairroDt bairroDt = new BairroDt();
		
		BairroNe Bairrone = new BairroNe();
		bairroDt = Bairrone.consultarId(idBairro);		
		
		return bairroDt;
	}
	
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";
		
		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);
		
		return stTemp;
	}
	
	public String consultarDescricaoServentiaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaTipoNe ServentiaTipone = new ServentiaTipoNe(); 
		stTemp = ServentiaTipone.consultarDescricaoJSON(tempNomeBusca);
		
		return stTemp;
	}
	
	public String consultarDescricaoServentiaSubtipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
		stTemp = ServentiaSubtipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);

		return stTemp;
	}
	
	public String consultarDescricaoAreaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		AreaNe Areane = new AreaNe(); 
		stTemp = Areane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoAudienciaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		AudienciaTipoNe AudienciaTipone = new AudienciaTipoNe(); 
		stTemp = AudienciaTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);

		return stTemp;
	}
	
	public String consultarDescricaoEstadoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		
		EstadoNe neObjeto = new EstadoNe();
		stTemp = neObjeto.consultarDescricaoJSON(descricao, posicao);
		
		return stTemp;

	}
	
	public String consultarDescricaoEstadoUfJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		
		EstadoNe neObjeto = new EstadoNe();
		stTemp = neObjeto.consultarDescricaoUfJSON(descricao, posicao);
		
		return stTemp;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";

		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);

		return stTemp;
	}
	
	public String consultarAreasDistribuicaoComarcaServentiaSubTipoJSON(String descricao, String id_Comarca, String posicao) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		
		stTemp = neObjeto.consultarAreasDistribuicaoComarcaServentiaSubTipoJSON(descricao, id_Comarca, "", posicao);
		
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoRecursalJSON(String descricao, String posicao) throws Exception {
		String stTemp;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		
		stTemp = neObjeto.consultarAreasDistribuicaoSegundoGrauAtivasJSON(descricao, posicao);
		
		return stTemp;
	}
	
	/**
	 * Consultar determinada serventia de acordo com sub tipo  (ServentiaSubTipoCodigo)
	 * 
	 * @param serventiaSubTipoCodigo, sub tipo a ser consultado
	 * @author mmgomes
	 */
	public ServentiaDt consultarServentiaPeloSubTipoCodigo(String serventiaSubTipoCodigo, FabricaConexao conexao) throws Exception {
		ServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaPs obPersistencia = new  ServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaPeloSubTipoCodigo(serventiaSubTipoCodigo);			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consultar uma serventia com sub tipo, Gabinete da Presidência do TJGO
	 *	 
	 * @author mmgomes
	 * @throws Exception 
	 */
	public ServentiaDt consultarGabinetePresidenciaTJGO() throws Exception{
		return consultarGabinetePresidenciaTJGO(null);
	}
	
	/**
	 * Consultar uma serventia com sub tipo, Gabinete da Vice Presidência do TJGO
	 *	 
	 * @author mmgomes
	 * @throws Exception 
	 */
	public ServentiaDt consultarGabineteVicePresidenciaTJGO() throws Exception{
		return consultarGabineteVicePresidenciaTJGO(null);
	}
	
	/**
	 * Consultar uma serventia com sub tipo, Gabinete da Presidência do TJGO
	 * @param obFabricaConexao
	 * @author mmgomes
	 */
	public ServentiaDt consultarGabinetePresidenciaTJGO(FabricaConexao conexao) throws Exception {
		return consultarServentiaPeloSubTipoCodigo(String.valueOf(ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO), conexao);
	}
	
	/**
	 * Consultar uma serventia com sub tipo, Gabinete da Vice Presidência do TJGO
	 * @param obFabricaConexao
	 * @author mmgomes
	 */
	public ServentiaDt consultarGabineteVicePresidenciaTJGO(FabricaConexao conexao) throws Exception {
		return consultarServentiaPeloSubTipoCodigo(String.valueOf(ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO), conexao);
	}
	
	/**
	 * Aplica as regras para a serventia relacionada
	 * 
	 * @param dados
	 * @throws Exception 
	 */
	public String VerificarServentiaRelacionada(ServentiaDt dados) throws Exception {

		String mensagemRetorno = "";
		ServentiaRelacionadaNe serventiaRelacionadane = new ServentiaRelacionadaNe();
				
		if (dados.getServentiaDtRelacaoEdicao() == null || (dados.getServentiaDtRelacaoEdicao().getId().isEmpty() && dados.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao().isEmpty())){
			mensagemRetorno += "Selecione a serventia a ser relacionada. \n";
		} else {
			if (dados.getServentiaDtRelacaoEdicao().isSubstitutoSegundoGrau()){
				
				if (dados.getServentiaDtRelacaoEdicao().getId_ServentiaSubstituicao() == null || dados.getServentiaDtRelacaoEdicao().getId_ServentiaSubstituicao().length() == 0) 
					mensagemRetorno += "O Gabinete a ser Substituído é obrigatório. \n";
				
				if (dados.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao() == null || dados.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao().length() == 0) 
					mensagemRetorno += "A data Inicial do período é obrigatória. \n";
				
				if (dados.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao() == null || dados.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao().length() == 0)
					mensagemRetorno += "A data Final do período é obrigatória. \n";
				
				if (dados.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao() != null && dados.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao().length() > 0
				    && dados.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao() != null && dados.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao().length() > 0){
					
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
				    Date dataInicial = format.parse(dados.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao());
				    Date dataFinal = format.parse(dados.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao()); 
				      
				    if (dataInicial.after(dataFinal)) mensagemRetorno += "A Data Inicial do Período deve ser Menor ou Igual a Data Final. \n";				    
				}
			}
			
			if (mensagemRetorno.length() == 0 && dados.getListaServentiasRelacoes() != null){				
				if (dados.getServentiaDtRelacaoEdicao().isSubstitutoSegundoGrau()) {
					//se é edição não se deve verificar se ja existe relacionamento no periodo
					if (!dados.getServentiaDtRelacaoEdicao().isTemId()) {
						// Verifica se já existe substituto para o período informado...
						List listaGabinetesSubstitutos = serventiaRelacionadane.consultarGabinetesSubstitutosSegundoGrau(dados.getId(), dados.getServentiaDtRelacaoEdicao().getId_ServentiaSubstituicao(), dados.getServentiaDtRelacaoEdicao().getDataInicialSubstituicao(), dados.getServentiaDtRelacaoEdicao().getDataFinalSubstituicao());
						if (listaGabinetesSubstitutos != null && listaGabinetesSubstitutos.size() > 0){
							if (listaGabinetesSubstitutos.size() > 1) {
								mensagemRetorno += "\nExistem outras serventias substituindo esta serventia neste período. Favor verificar. ";
							} else {
								ServentiaDt serventiaSubstituta = (ServentiaDt) listaGabinetesSubstitutos.get(0);
								// Verifica se é inclusão, pois se for alteração não devemos impedir
								if (dados.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao() == null || dados.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao().trim().length() == 0 || !(dados.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao().equalsIgnoreCase(serventiaSubstituta.getId_ServentiaRelacaoEdicao()))) {
									mensagemRetorno += "\n" + serventiaSubstituta.getServentia() + " já está substituindo neste período (" + serventiaSubstituta.getDataInicialSubstituicao() + " a " + serventiaSubstituta.getDataFinalSubstituicao() + "). Favor verificar. ";
								}
							}
						}
					}
				}  else {
					ServentiaRelacionadaDt objTemp;					
					for(int i = 0; i < dados.getListaServentiasRelacoes().size(); i++) {
						objTemp = (ServentiaRelacionadaDt)dados.getListaServentiasRelacoes().get(i);						
						// Se não for serventia substituta
						if (!objTemp.isSubstitutoSegundoGrau()) {
							// Verifica se trata da mesma serventia principal
							if (objTemp != null && objTemp.getId() != null && dados.getServentiaDtRelacaoEdicao() != null && dados.getServentiaDtRelacaoEdicao().getId() != null && dados.getServentiaDtRelacaoEdicao().getId().equalsIgnoreCase(objTemp.getId())) {
								// Verifica se é inclusão, pois se for alteração não devemos impedir
								if (dados.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao() == null || dados.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao().trim().length() == 0 || !(dados.getServentiaDtRelacaoEdicao().getId_ServentiaRelacao().equalsIgnoreCase(objTemp.getId_ServentiaRelacao()))) {
									mensagemRetorno += "\nA serventia selecionada já está relacionada como principal. Favor verificar. ";
								}
							}	
						}											 											
					}
				}
			}
		}	
		
		serventiaRelacionadane = null;
	
		return mensagemRetorno;
	}
	
	/**
	 * Método que consulta os IDs das Serventias cadastradas na Área de Distribuição.
	 * @param idAreaDistribuicao - ID da Área de Distribuição
	 * @return lista de IDs de Serventias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarServentiasAreaDistribuicao(String idAreaDistribuicao) throws Exception {
		List stTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.listarServentiasAreaDistribuicao(idAreaDistribuicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiasRedistribuicaoJSON(String descricao, String posicao, String serventiaTipoCodigo) throws Exception{
		String stTemp = "";

		switch (Funcoes.StringToInt(serventiaTipoCodigo)) {
			case ServentiaTipoDt.VARA:
				stTemp = this.consultarVarasAtivasJSON(descricao, posicao);
				break;

			case ServentiaTipoDt.SEGUNDO_GRAU:
				stTemp = this.consultarServentiasVarasSegundoGrauAtivasJSON(descricao, posicao);
				break;
		}
		return stTemp;
	}
	
	public String consultarServentiasAtivasAreaDistribuicaoJSON(String descricao, String idAreaDistribuicao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasAtivasAreaDistribuicaoJSON(descricao, idAreaDistribuicao, posicao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarVarasAtivasJSON(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivasJSON(descricao, posicao, String.valueOf(ServentiaTipoDt.VARA));
	}
	
	public String consultarDelegaciasAtivasJSON(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivasJSON(descricao, posicao, String.valueOf(ServentiaTipoDt.DELEGACIA));
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo) throws Exception {
		return this.consultarServentiasAtivasJSON(descricao, posicao, serventiaTipoCodigo, null, null);
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasAtivasJSON(descricao, posicao, serventiaTipoCodigo, id_Comarca, serventiaSubTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiasVarasSegundoGrauAtivasJSON(String descricao, String posicao) throws Exception {
		return this.consultarServentiasAtivasJSON(descricao, posicao, String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU));
	}

	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo1Grau, String serventiaTipoCodigo2Grau) throws Exception {
		return this.consultarServentiasAtivasJSON(descricao, posicao, serventiaTipoCodigo1Grau, serventiaTipoCodigo2Grau, null, null);
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo1Grau , String serventiaTipoCodigo2Grau, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasAtivasJSON(descricao, posicao, serventiaTipoCodigo1Grau, serventiaTipoCodigo2Grau, id_Comarca, serventiaSubTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaPublicacaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaPublicacaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiasComarcaJSON(String descricao, String idComarca, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new  ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasComarcaJSON(descricao, idComarca, posicao);
		
		} finally{obFabricaConexao.fecharConexao();}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaPreProcessualComarcaJSON(String descricao, String idComarca, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ServentiaPs obPersistencia = new  ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaPreProcessualComarcaJSON(descricao, idComarca, posicao);
		} catch (Exception e) {
			throw new Exception(" <{ Erro ao consultar Serventias por Comarca. }>" + this.getClass().getName() + ".consultarServentiasComarca(): " + e.getMessage(), e);
		} finally {obFabricaConexao.fecharConexao();}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaPreProcessualJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ServentiaPs obPersistencia = new  ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaPreProcessualJSON(descricao, posicao);
		} catch (Exception e) {
			throw new Exception(" <{ Erro ao consultar Serventias por Comarca. }>" + this.getClass().getName() + ".consultarDescricaoServentiaPreProcessualJSON(): " + e.getMessage(), e);
		} finally {obFabricaConexao.fecharConexao();}
		return stTemp;
	}
	
	/**
	 * Retorna a serventia pelo código externo, tipo e subtipo.
	 * @param serventiaCodigoExterno
	 * @param idServentiaTipo
	 * @param idServentiaSubTipo
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public ServentiaDt consultarServentiaCodigoExterno(String serventiaCodigoExterno, String idServentiaTipo, String idServentiaSubTipo, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarServentiaCodigoExterno(serventiaCodigoExterno, idServentiaTipo, idServentiaSubTipo);
	}
	
	public void inserir(ServentiaDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		EnderecoNe enderecoNe = new EnderecoNe();		
		try{
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());

			// Salva endereço da serventia
			enderecoNe.salvar(dados.getEnderecoDt(), obFabricaConexao);
			dados.setId_Endereco(dados.getEnderecoDt().getId());
			
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Serventia", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			dados.getEnderecoDt().setId("");
			throw e;
		} 
	}
	
	public List consultarListaServentias(String idAreaDistribuicao) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarListaServentias(idAreaDistribuicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}
	
	public String obtenhaProximoId() throws Exception {
		String novoId = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			novoId = obtenhaProximoId(obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return novoId;
	}
	
	public String obtenhaProximoId(FabricaConexao obFabricaConexao) throws Exception {
		ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
		return obPersistencia.obtenhaProximoId();
	}
	
	public String obtenhaProximoCodigo() throws Exception {
		String novoCodigo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			novoCodigo = obtenhaProximoCodigo(obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return novoCodigo;
	}
	
	public String obtenhaProximoCodigo(FabricaConexao obFabricaConexao) throws Exception {
		String novoId = null;
		ServentiaDt serventiaDt = null;
		
		novoId = obtenhaProximoId(obFabricaConexao);
		serventiaDt = this.consultarServentiaCodigo(novoId, obFabricaConexao);
		while (serventiaDt != null) {
			novoId = obtenhaProximoId();
			serventiaDt = consultarServentiaCodigo(novoId, obFabricaConexao);
		}
		
		return novoId;
	}
	
	public String consultarIdComarca(String idServentia) throws Exception {
		String idComarca = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			idComarca = obPersistencia.consultarIdComarca(idServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return idComarca;
	}
	
	public boolean isCentralMandados(String idServentia, FabricaConexao fabricaConexao) throws Exception {
		
		FabricaConexao obFabricaConexao;
		String serventiaTipoCodigo = null;
		
		if(fabricaConexao == null) {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
		} else {
			obFabricaConexao = fabricaConexao;
		}
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			serventiaTipoCodigo = obPersistencia.consultarCodigoServentiaTipo(idServentia);
			if(serventiaTipoCodigo != null && Funcoes.StringToInt(serventiaTipoCodigo) == ServentiaTipoDt.CENTRAL_MANDADOS){
				return true;
			} 
			else {
				return false;
			}
		} finally{
			if(fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * Método para verificar se a serventia é um escritório jurídico ou procuradoria ou defensoria.
	 * @param String id_serventia
	 * @param FabricaConexao, fabrica ojeto de conexão
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isServentiaEscritorioJuridicoProcuradoriaDefensoria(String id_serventia, FabricaConexao fabrica) throws Exception {
		boolean retorno = false;
		ServentiaPs obPersistencia = new ServentiaPs(fabrica.getConexao());
		retorno = obPersistencia.isServentiaEscritorioJuridicoProcuradoriaDefensoria(id_serventia);
		return retorno;
	}

	public ServentiaRelacionadaDt consultarId_ServentiaRelacionada(String id_Serventia_principal, String id_serventia_relacionada) throws Exception {
		ServentiaRelacionadaDt dtTemp;
		ServentiaRelacionadaNe neObjeto = new ServentiaRelacionadaNe();
		
		dtTemp = neObjeto.consultarId_ServentiaRelacionada(id_Serventia_principal,id_serventia_relacionada);
		
		return dtTemp;
	}
	
	public String consultarDescricaoServentiaUnidaTrabalhoJSON(String descricao, int grupoCodigo, String posicao ) throws Exception{
		return consultarDescricaoServentiaUnidaTrabalhoJSON(descricao, grupoCodigo, posicao, Persistencia.ORDENACAO_PADRAO, null);   
	}
	
	public String consultarDescricaoServentiaUnidaTrabalhoJSON(String descricao, int grupoCodigo, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaUnidaTrabalhoJSON(descricao, grupoCodigo, posicao, ordenacao, quantidadeRegistros);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

	/**
	 * Método para Listar todas as serventias existentes na minuta não analisada de uma sessão virtual para o desembargador.
	 * @param String stNomeBusca1
	 * @param String posicaoPaginaAtual
	 * @param UsuarioDt usuarioDt, 
	 * @return String
	 * @throws Exception
	 * @author lrcampos
	 * @since 19/03/2020
	 */
	public String consultarDescricaoServentiaMinutaNaoAnalisadaJSON(String stNomeBusca1, String posicaoPaginaAtual,
			UsuarioDt usuarioDt, Boolean isIniciada ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		String idServentiaCargo = usuarioDt.getId_ServentiaCargo();
		try {
			if(Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				idServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaMinutaNaoAnalisadaJSON(stNomeBusca1, posicaoPaginaAtual,
					idServentiaCargo, isIniciada);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Método para Listar todas as serventias existentes na minuta pré analisada de uma sessão virtual para o desembargador.
	 * @param String stNomeBusca1
	 * @param String posicaoPaginaAtual
	 * @param UsuarioDt usuarioDt, 
	 * @return String
	 * @throws Exception
	 * @author lrcampos
	 * @since 19/03/2020
	 */
	public String consultarDescricaoPreAnalisadaServentiaMinutaJSON(String stNomeBusca1, String posicaoPaginaAtual,
			UsuarioDt usuarioDt, Boolean isIniciada) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		String idServentiaCargo = usuarioDt.getId_ServentiaCargo();
		try {
			if(Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				idServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoPreAnalisadaServentiaMinutaJSON(stNomeBusca1, posicaoPaginaAtual,
					idServentiaCargo, isIniciada);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Método para Listar todas as serventias existentes no voto de uma sessão virtual para o desembargador.
	 * @param String stNomeBusca1
	 * @param String posicaoPaginaAtual
	 * @param UsuarioDt usuarioDt, 
	 * @return String
	 * @throws Exception
	 * @author lrcampos
	 * @param codigoTemp 
	 * @since 19/03/2020
	 */
	
	public String consultarDescricaoServentiaVotoJSON(String stNomeBusca1, String posicaoPaginaAtual,
			UsuarioDt usuarioDt, Integer pendStatusCodigo, Integer codigoTemp) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		String idServentiaCargo = usuarioDt.getId_ServentiaCargo();
		try {
			if(Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				idServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaVotoJSON(stNomeBusca1, posicaoPaginaAtual,
					idServentiaCargo, pendStatusCodigo, codigoTemp);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Retorna true para comarcas que já tenham a Central de Mandados do Projudi implantada.
	 * @param idServ
	 * @return
	 * @throws Exception
	 */
	public boolean temCentralProjudiImplantada(String idServ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		ServentiaPs serventiaPs = null;
		ServentiaDt serventiaDt = null;
		boolean retorno = false;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			serventiaPs = new ServentiaPs(obFabricaConexao.getConexao());
			if(idServ != null && !idServ.isEmpty()){
				serventiaDt = serventiaPs.consultarId(idServ);
				if(new ComarcaDt().temCentralProjudiImplantada(serventiaDt.getComarcaCodigo())){
					retorno = true;
				}
			}
		}
		finally {
			if(obFabricaConexao != null) obFabricaConexao.fecharConexao();
		} 
		
		return retorno;
	}
	
}
