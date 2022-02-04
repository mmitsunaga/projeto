package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.ImportaDadosSPGDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.UfrValorDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.projudi.ps.GuiaItemPs;
import br.gov.go.tj.projudi.ps.ImportaDadosSPGPs;
import br.gov.go.tj.projudi.ps.LocomocaoPs;
import br.gov.go.tj.projudi.ps.LogPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class ImportaDadosSPGNe {

	Funcoes funcao = new Funcoes();

	public ImportaDadosSPGNe() {

	}

	public List listaMandadosGratuitosSpgNOVO(List<RelatoriosMandadoDt> listaOficiaisProj, String anoMes,
			String nomeComarca, String cpfUsuario) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);

		List<RelatoriosMandadoDt> listaOficiais = new ArrayList<>();

		try {
			List<RelatoriosMandadoDt> listaOficiaisSpg = new ArrayList<>();
			
			for (int k = 0; k < listaOficiaisProj.size(); k++) {
				listaOficiaisProj.get(k)
						.setNomeComarca(Funcoes.RemoveAcentos(listaOficiaisProj.get(k).getNomeComarca()));
			}

			ImportaDadosSPGPs objPs = new ImportaDadosSPGPs(obFabricaConexao.getConexao());

			nomeComarca = Funcoes.RemoveAcentos(nomeComarca);

			listaOficiaisSpg = objPs.relatorioMensalMandadoGratuitoSpg(anoMes, nomeComarca, cpfUsuario);

			if (!listaOficiaisProj.isEmpty() || !listaOficiaisSpg.isEmpty()) {
				listaOficiais = (comparaElementoLista(listaOficiaisProj, listaOficiaisSpg));
			}

		} finally {

			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}

		return listaOficiais;
	}

	public List<RelatoriosMandadoDt> comparaElementoListaNOVO(List<RelatoriosMandadoDt> listaOficiaisProj,
			List<RelatoriosMandadoDt> listaOficiaisSpg) throws Exception {

		RelatoriosMandadoDt objDt = new RelatoriosMandadoDt();

		boolean acheiNaListaProjudi = false;

		for (int j = 0; j < listaOficiaisSpg.size(); j++) {

			for (int k = 0; k < listaOficiaisProj.size(); k++) {

				acheiNaListaProjudi = false;

				listaOficiaisSpg.get(j)
						.setCpfUsuario(Funcoes.completaCPFZeros(listaOficiaisSpg.get(j).getCpfUsuario()));

				if (listaOficiaisSpg.get(j).getCpfUsuario()
						.equalsIgnoreCase(listaOficiaisProj.get(k).getCpfUsuario())) {
					//
					// se o elemento da lista spg existir na lista
					// projudi
					// mantem a lista projudi
					// com o somatoria de mandados por mes.
					//

					listaOficiaisProj.get(k).setQuantFaixaReceber(listaOficiaisSpg.get(j).getQuantFaixaReceber()
							+ listaOficiaisProj.get(k).getQuantFaixaReceber());

					listaOficiaisProj.get(k).setQuantResolutivoReceber(listaOficiaisSpg.get(j).getQuantResolutivoReceber()
							+ listaOficiaisProj.get(k).getQuantResolutivoReceber());
					
					acheiNaListaProjudi = true;
					break;
				}

			}
			if (!acheiNaListaProjudi) {
				//
				// adiciona elemento spg inexistente na lista projudi
				//
				objDt = new RelatoriosMandadoDt();
				objDt = listaOficiaisSpg.get(j);
				objDt.setNomeUsuario(listaOficiaisSpg.get(j).getNomeUsuario().trim());
				objDt.setEscalaTipo(listaOficiaisSpg.get(j).getEscalaTipo().trim());
				objDt.setNomeComarca(listaOficiaisSpg.get(j).getNomeComarca().trim());
				if (objDt.getContaDv() == null)
					objDt.setContaDv("0");
				listaOficiaisProj.add(objDt);
			}
		}

		return listaOficiaisProj;
	}

	//
	// atualiza locomocao spg
	//
	public String atualizaLomocaoSpg(String dataInicial, String dataFinal, String idServentia, String idUsuarioSessao)
			throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		FabricaConexao obFabricaConexaoAdabas = new FabricaConexao(FabricaConexao.ADABAS);

		ImportaDadosSPGPs objPs = new ImportaDadosSPGPs(obFabricaConexao.getConexao());

		ImportaDadosSPGPs objPsAdabas = new ImportaDadosSPGPs(obFabricaConexaoAdabas.getConexao());

		List<ImportaDadosSPGDt> listaMandados = new ArrayList<>();

		int contador = 0;

		// trazer lista para atualizacao

		listaMandados = objPs.listaMandadosParaLocomocaoSpg(dataInicial, dataFinal, idServentia);

		if (!listaMandados.isEmpty()) {
			//
			// verifica se mandado ja foi incluido no locomocaoSpg
			//
			boolean existe = false;

			for (int i = 0; i < listaMandados.size(); i++) { // se abortar faz de novo. ver com o heleno como avisa para
				// o usuario fazer de novo.

				// testar capital e interior sem olhar comarca. mais seguro.

				if (listaMandados.get(i).getCodgComarca() == ImportaDadosSPGDt.CODIGO_COMARCA_GOIANIA)
					existe = objPsAdabas.consultaLocomocaoSpgPorMandadoCapital(listaMandados.get(i).getNumrMandado(),
							listaMandados.get(i).getNumrCpfOficial());
				else
					existe = objPsAdabas.consultaLocomocaoSpgPorMandadoInterior(listaMandados.get(i).getNumrMandado(),
							listaMandados.get(i).getNumrCpfOficial());

				if (!existe) {

					if (listaMandados.get(i).getValorLocomocao() != 0) {
						ImportaDadosSPGDt objDt = new ImportaDadosSPGDt();
						objDt.setStatPagamento(ImportaDadosSPGDt.STATUS_LOCOMOCAO_SPG_IMPORTADA_DO_PROJUDI);
						objDt.setNumrMandado(listaMandados.get(i).getNumrMandado());
						objDt.setValorLocomocao(listaMandados.get(i).getValorLocomocao());
						objDt.setCodgComarca(listaMandados.get(i).getCodgComarca());
						objDt.setDataValidacao(Funcoes.FormatarData(new Date()));
						objDt.setMatricValidacao(listaMandados.get(i).getMatricValidacao());
						objDt.setNumrCpfOficial(listaMandados.get(i).getNumrCpfOficial());

						if (listaMandados.get(i).getCodgComarca() == ImportaDadosSPGDt.CODIGO_COMARCA_GOIANIA)
							objPsAdabas.incluiLocomocaoSpgCapital(objDt);
						else
							objPsAdabas.incluiLocomocaoSpgInterior(objDt);
						//
						// atualiza status pagamento do mandado
						//
						objPs.alteraPagamentoStatus(objDt.getNumrMandado(), idUsuarioSessao);

						contador++;
					}
				}
			}

			LogPs obLogPs = new LogPs(obFabricaConexao.getConexao());

			LogDt obLogDt = new LogDt();
			obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.EnviarDadosDiretoriaFinanceira));
			obLogDt.setId_Usuario(idUsuarioSessao);
			obLogDt.setIpComputador(""); // colocar ip da maquina
			obLogDt.setData(Funcoes.getDataHoraAtual());
			obLogDt.setHora(null);
			obLogDt.setTabela("");
			obLogDt.setValorNovo("Dt.Ini..: " + dataInicial + " Dt.Fim.: " + dataFinal + "  Id Serv.: " + idServentia
					+ " Env.:" + contador);
			obLogDt.setCodigoTemp("");
			obLogDt.setId_Tabela("");
			obLogDt.setHash("");
			obLogDt.setQtdErrosDia(0);
			obLogPs.inserir(obLogDt);
		}
		if (obFabricaConexaoAdabas !=  null) obFabricaConexaoAdabas.fecharConexao();
		if (obFabricaConexao != null) obFabricaConexao.fecharConexao();

		return "Processamento completado. \n\n Total de registros enviados: " + contador;
	}

	/**
	 * 
	 * Controla da guia de saldo de locomoção do processo.
	 *
	 * @param idProc
	 * @param qtdeLocomocaoMandado
	 * @param idBairro
	 * @param fabricaConexao
	 * @param idMandJud
	 * @return quantidade locomoção usada
	 * @throws Exception
	 * @author fvmeireles
	 * 
	 */
	public int consultaGuiaSaldoLocomocao(int idProc, int qtdeLocomocaoMandado, int idBairro, int idMandJud,
			FabricaConexao fabricaConexao, FabricaConexao objFcAdabas) throws Exception {

		ImportaDadosSPGDt objDt = null;

		ImportaDadosSPGDt objDtAdabas = null;

		ImportaDadosSPGPs objPs = new ImportaDadosSPGPs(fabricaConexao.getConexao());

		ImportaDadosSPGPs objPsAdabas = new ImportaDadosSPGPs(objFcAdabas.getConexao());

		int qtdeLocomocaoUsada = 0;

		// try {

		// objFcAdabas.iniciarTransacao();

		boolean liberada = false;

		double locomocaoValorUnitario = this.buscaValorAtualLocomocao(idBairro);
		if (locomocaoValorUnitario != 0) {
			//
			// Procura guia de saldo no SPG pelo numero processo.
			//
			// Se não existir, usa as locomoções reservadas para o processo no projudi.
			//
			// Se existir, grava no projudi essa guia de saldo
			//
			// Procura no spg as guias desta guia de saldo e vincula
			// na guia de saldo projudi
			//
			// grava um log antes da checagem do saldo
			//
			// Sempre verificar se a guia de saldo esta liberada para uso. (status 88)
			//
			// Se uma locomocao do projudi e usada, a guia dessa locomocao,
			// devera ser gravada no controle do SPG.
			//
			// O spg nao mais usa essa guia , mas as locomocoes nao reservadas desse
			// processo poderam ser usadas no projudi.
			//
			// Se o saldo for utilizado, atualiza no projudi e no spg, gravando
			// um log no spg e um novo log no projudi
			//
			
			objDtAdabas = this.consultaGuiaSaldoSPG(Integer.toString(idProc));		 

			if (objDtAdabas != null) {
				
				objDt = objPs.consultaGuiaSaldoProjudi(idProc);

				if (objDt == null) {
					objDt = objPs.gravaGuiaSaldoProjudi(objDtAdabas, idProc);
				}
				
				List<ImportaDadosSPGDt> listaGuiaVinculadaSaldoSpg = new ArrayList<>();

				listaGuiaVinculadaSaldoSpg = this.consultaGuiaVinculadaSaldoSPG(idProc); 							
				
				if (!listaGuiaVinculadaSaldoSpg.isEmpty()) {
					for (int k = 0; k < listaGuiaVinculadaSaldoSpg.size(); k++) {		    			
						objPs.vinculaGuiaNaGuiaSaldoProjudi(listaGuiaVinculadaSaldoSpg.get(k).getNumeroGuia(),
								objDt.getNumeroGuiaSaldo());
					}
				}

				objDt.setGuiaSaldoStatusAnterior(objDt.getGuiaSaldoStatus());
				objDt.setGuiaSaldoValorAnterior(objDt.getGuiaSaldoValorAtualizado());
				objPs.atualizaGuiaSaldoProjudi(objDt.getIdGuiaSaldo(), objDtAdabas.getGuiaSaldoStatus(),
						objDtAdabas.getGuiaSaldoValorAtualizado());
				objDt.setGuiaSaldoValorAtualizado(objDtAdabas.getGuiaSaldoValorAtualizado());
				objDt.setGuiaSaldoStatus(objDtAdabas.getGuiaSaldoStatus());
				if (objDtAdabas.getGuiaSaldoStatus().equalsIgnoreCase(ImportaDadosSPGDt.GUIA_SALDO_STATUS_LIBERADA))
					liberada = true;
				LogPs obLogPs = new LogPs(fabricaConexao.getConexao());
				LogDt obLogDt = new LogDt();
				obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.AtualizaGuiaSaldo));
				obLogDt.setId_Usuario(UsuarioDt.SistemaProjudi);
				obLogDt.setIpComputador("");
				obLogDt.setTabela("guia_emis");
				obLogDt.setValorAtual(
						"Guia.." + objDt.getNumeroGuiaSaldo() + " Saldo.." + objDt.getGuiaSaldoValorAnterior()
								+ " Status Anterior.." + objDt.getGuiaSaldoStatusAnterior() + "  Id.Proc.." + idProc);
				obLogDt.setValorNovo(
						"Saldo.." + objDt.getGuiaSaldoValorAtualizado() + "  Status.." + objDt.getGuiaSaldoStatus());
				obLogDt.setCodigoTemp("");
				obLogDt.setId_Tabela(Integer.toString(objDt.getIdGuiaSaldo()));
				obLogDt.setHash("");
				obLogDt.setQtdErrosDia(0);
				obLogPs.inserir(obLogDt);
			}

			if (liberada) { // calcula locomocoes a serem usadas pelo saldo

				double guiaSaldoValorAnterior = objDt.getGuiaSaldoValorAtualizado();
				double guiaSaldoValorAtualizado = objDt.getGuiaSaldoValorAtualizado();

				if (guiaSaldoValorAtualizado >= locomocaoValorUnitario) {

					int locomocoesDisponiveisSaldo = (int) (Math
							.floor(guiaSaldoValorAtualizado / locomocaoValorUnitario));

					if (locomocoesDisponiveisSaldo >= qtdeLocomocaoMandado) // uso todas que preciso.
						qtdeLocomocaoUsada = qtdeLocomocaoMandado;
					else
						qtdeLocomocaoUsada = locomocoesDisponiveisSaldo; // uso todas disponiveis.

					guiaSaldoValorAtualizado = (guiaSaldoValorAtualizado
							- (qtdeLocomocaoUsada * locomocaoValorUnitario));
				}

				if (qtdeLocomocaoUsada > 0) {
					objDt.setQtdeLocomocaoUsada(qtdeLocomocaoUsada);
					objDt.setGuiaSaldoValorAtualizado(guiaSaldoValorAtualizado);
					objDt.setLocomocaoValorUnitario(locomocaoValorUnitario);
					objDt.setQtdeLocomocaoNecessaria(qtdeLocomocaoMandado);
					objDt.setGuiaSaldoValorAnterior(guiaSaldoValorAnterior);
					objDt.setIdMandJud(idMandJud);
					objDt.setIdBairro(idBairro);
					objDt.setIdProc(Integer.toString(idProc));
					this.atualizaGuiaSaldoLocomocao(objDt, fabricaConexao, objFcAdabas);
					objPsAdabas.gravaLogGuiaSaldoSpg(objDt);
				}
			}

		} else
			throw new MensagemException("Valor de Locomoção para o Bairro não Encontrado");

		// objFcAdabas.finalizarTransacao();

		// } catch (Exception e) {
		// if (objFcAdabas != null) {
		// objFcAdabas.cancelarTransacao();
		// }
		// throw e;

		// }

		// finally {
		// if (objFcAdabas != null) {
		// objFcAdabas.fecharConexao();
		// }
		// }

		return qtdeLocomocaoUsada;

	}

	public void atualizaGuiaSaldoLocomocao(ImportaDadosSPGDt objDt, FabricaConexao fabricaConexao,
			FabricaConexao objFcAdabas) throws Exception {

		ImportaDadosSPGPs objPs = new ImportaDadosSPGPs(fabricaConexao.getConexao());
		ImportaDadosSPGPs objPsAdabas = new ImportaDadosSPGPs(objFcAdabas.getConexao());
		objPs.atualizaGuiaSaldoProjudi(objDt.getIdGuiaSaldo(), objDt.getGuiaSaldoStatus(),
				objDt.getGuiaSaldoValorAtualizado());

		MandadoJudicialDt mandJudDt = new MandadoJudicialDt();
		mandJudDt.setId(Integer.toString(objDt.getIdMandJud()));

		ZonaBairroRegiaoDt zonaBairroRegiaoDt;
		ZonaBairroRegiaoNe zonaBairroRegiaoNe = new ZonaBairroRegiaoNe();
		zonaBairroRegiaoDt = zonaBairroRegiaoNe.consultarIdBairro(Integer.toString(objDt.getIdBairro()));

		BairroDt bairroDt = new BairroDt();
		bairroDt.setId(zonaBairroRegiaoDt.getId_Bairro());

		ZonaDt zonaDt = new ZonaDt();
		zonaDt.setId(zonaBairroRegiaoDt.getId_Zona());

		RegiaoDt regiaoDt = new RegiaoDt();
		regiaoDt.setId(zonaBairroRegiaoDt.getId_Regiao());

		GuiaItemDt guiaItemDt = null;
		LocomocaoDt locomocaoDt = null;
		GuiaItemPs guiaItemPs = new GuiaItemPs(fabricaConexao.getConexao());
		LocomocaoPs locomocaoPs = new LocomocaoPs(fabricaConexao.getConexao());

		for (int i = 0; i < objDt.getQtdeLocomocaoUsada(); i++) {

			guiaItemDt = new GuiaItemDt();
			guiaItemDt.setId_GuiaEmissao(Integer.toString(objDt.getIdGuiaSaldo()));
			guiaItemDt.setQuantidade("1");
			guiaItemDt.setValorCalculado(Funcoes.FormatarDecimal(objDt.getLocomocaoValorUnitario()));
			guiaItemDt.setValorReferencia(Funcoes.FormatarDecimal(objDt.getLocomocaoValorUnitario()));
			guiaItemDt.setGuiaItem("Item Guia Saldo");
			guiaItemDt.setId_Custa(Integer.toString(CustaDt.CUSTAS_LOCOMOCAO));
			guiaItemPs.inserir(guiaItemDt);

			locomocaoDt = new LocomocaoDt();
			locomocaoDt.setGuiaItemDt(guiaItemDt);
			locomocaoDt.setBairroDt(bairroDt);
			locomocaoDt.setRegiaoDt(regiaoDt);
			locomocaoDt.setZonaDt(zonaDt);
			locomocaoDt.setMandadoJudicialDt(mandJudDt);
			locomocaoPs.inserir(locomocaoDt);

		}

		LogPs obLogPs = new LogPs(fabricaConexao.getConexao());
		LogDt obLogDt = new LogDt();
		obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.AtualizaGuiaSaldo));
		obLogDt.setId_Usuario(UsuarioDt.SistemaProjudi);
		obLogDt.setIpComputador("");
		obLogDt.setTabela("guia_emis");
		obLogDt.setValorAtual("Guia.." + objDt.getNumeroGuiaSaldo() + " Saldo.." + objDt.getGuiaSaldoValorAnterior()
				+ " Loc.Necessária.." + objDt.getQtdeLocomocaoNecessaria() + " Valr.Unit.Loc.."
				+ objDt.getLocomocaoValorUnitario() + " Mand.." + objDt.getIdMandJud() + "  Id.Proc.."
				+ objDt.getIdProc());
		obLogDt.setValorNovo(
				"Saldo.." + objDt.getGuiaSaldoValorAtualizado() + " Locomoção Usada.." + objDt.getQtdeLocomocaoUsada());
		obLogDt.setCodigoTemp("");
		obLogDt.setId_Tabela(Integer.toString(objDt.getIdGuiaSaldo()));
		obLogDt.setHash("");
		obLogDt.setQtdErrosDia(0);
		obLogPs.inserir(obLogDt);
		//
		// atualiza saldo no SPG.
		//
		objPsAdabas.atualizaGuiaSaldoSPG(objDt.getNumeroGuiaSaldo(), objDt.getGuiaSaldoValorAtualizado());

	}

	public double buscaValorAtualLocomocao(int idBairro) throws Exception {
		double valorLocomocaoAtual = 0;
		ZonaBairroRegiaoDt zonaBairroRegiaoDt;
		ZonaBairroRegiaoNe zonaBairroRegiaoNe = new ZonaBairroRegiaoNe();
		zonaBairroRegiaoDt = zonaBairroRegiaoNe.consultarIdBairro(Integer.toString(idBairro));
		if (zonaBairroRegiaoDt != null) {
			ZonaDt zonaDt;
			ZonaNe zonaNe = new ZonaNe();
			zonaDt = zonaNe.consultarId(zonaBairroRegiaoDt.getId_Zona());
			if (zonaDt != null) {
				UfrValorDt ufrValorDt = null;
				UfrValorNe ufrValorNe = new UfrValorNe();
				ufrValorDt = ufrValorNe.consultarDataAtual();
				valorLocomocaoAtual = ufrValorDt.obtenhaValorURFEmReais(zonaDt.getValorCivel());
			}
		}
		return valorLocomocaoAtual;
	}

	public void gravaControleSpg(String idMandJud, String idProc, FabricaConexao fabricaConexao,
			FabricaConexao objFcAdabas) throws Exception {

		// consulta controle spg para ver se a guia ja esta gravada.
		// se nao, grava essa guia no controle de saldo spg.

		ImportaDadosSPGPs objPsAdabas = new ImportaDadosSPGPs(objFcAdabas.getConexao());

		ImportaDadosSPGDt objDt = null;

		ImportaDadosSPGPs objPs = new ImportaDadosSPGPs(fabricaConexao.getConexao());

		List<ImportaDadosSPGDt> listaGuiasLocomocoesUsadas = new ArrayList();

		boolean retorno = false;

		listaGuiasLocomocoesUsadas = objPs.listaGuiasLocomocoesUsadasProjudi(idMandJud);

		if (!listaGuiasLocomocoesUsadas.isEmpty()) {

			for (int i = 0; i < listaGuiasLocomocoesUsadas.size(); i++) {

				retorno = objPsAdabas.consultaControleSpg(listaGuiasLocomocoesUsadas.get(i).getNumeroGuia());

				if (!retorno) {
					String procNumero[] = objPs.consultaProcNumero(Integer.parseInt(idProc));
					objDt = new ImportaDadosSPGDt();
					objDt.setProcNumero(procNumero[0] + procNumero[1] + procNumero[2]);
					objDt.setNumeroGuia(listaGuiasLocomocoesUsadas.get(i).getNumeroGuia());
					objPsAdabas.gravaControleSpg(objDt);
				}
			}

		}
	}

	/**
	 * Método para consulta o id da guia de saldo e o saldo restante do processo projudi.
	 * @param String idProc
	 * @return ImportaDadosSPGDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public ImportaDadosSPGDt consultaGuiaSaldoProjudi(String idProc) throws Exception {
		FabricaConexao obFabricaConexao = null;
		ImportaDadosSPGDt importaDadosSPGDt = null;

		try {
			if( idProc != null ) {
	    		obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
	    		ImportaDadosSPGPs obPersistencia = new ImportaDadosSPGPs(obFabricaConexao.getConexao());
	    		importaDadosSPGDt = obPersistencia.consultaGuiaSaldoProjudi(Funcoes.StringToInt(idProc));
			}
    	}
    	finally {
    		if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
    	}
    	
    	return importaDadosSPGDt;
	}
	
	/**
	 * Método para consulta do saldo de uma guia de saldo de um processo projudi. Consulta feita no spg.
	 * @param String idProc
	 * @return ImportaDadosSPGDt
	 * @throws Exception
	 * 
	 * @author fernando meireles
	 */	
 
	public ImportaDadosSPGDt consultaGuiaSaldoSPG(String idProc) throws Exception {
		FabricaConexao obFabricaConexao = null;
		FabricaConexao obFabricaAdabas = null;
		ImportaDadosSPGDt importaDadosSPGDt = null;
		try {
			if( idProc != null ) {
	    		
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				obFabricaAdabas = new FabricaConexao(FabricaConexao.ADABAS);
	    		
	    		ImportaDadosSPGPs obPersistencia = new ImportaDadosSPGPs(obFabricaConexao.getConexao());
	    		
	    		String procNumero[] = obPersistencia.consultaProcNumero(Integer.parseInt(idProc));
	    		
	    		ImportaDadosSPGPs obAdabas = new ImportaDadosSPGPs(obFabricaAdabas.getConexao());
	    		
	    		importaDadosSPGDt = obAdabas.consultaGuiaSaldoSpg(procNumero[0] + procNumero[1] + procNumero[2]);    	
	    		
			}
    	}
    	finally {
    		if (obFabricaAdabas !=  null) obFabricaAdabas.fecharConexao();
    		if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
    	}
    	
    	return importaDadosSPGDt;
	}

	/**
	 * 
	 * Liberar locomoção de uma guia de saldo.
	 *
	 * @param idMandJud
	 * @param quantidade
	 *            a liberar
	 * @param conexão
	 *            oracle
	 * @param conexão
	 *            adabas
	 * @param idMandJud
	 * @return quantidade locomoção liberada
	 * @throws Exception
	 * @author fvmeireles
	 * 
	 */
	public int LiberarLocomocaoGuiaSaldo(String idMandJud, int qtdLiberar, FabricaConexao fabricaConexao,
			FabricaConexao objFcAdabas) throws Exception {

		ImportaDadosSPGDt objDtProj = null;
		ImportaDadosSPGPs objPsProj = new ImportaDadosSPGPs(fabricaConexao.getConexao());
		
		ImportaDadosSPGDt objDtAdabas = null;
		ImportaDadosSPGPs objPsAdabas = new ImportaDadosSPGPs(objFcAdabas.getConexao());
        
		List <ImportaDadosSPGDt>listaLocomocaoVinculada = new ArrayList<>();
		
		int quantidade = 0;
		int qtdLiberada = 0;
		double valorSaldoAtual = 0;
	
		LocomocaoDt locomocaoDt = null;
			
		listaLocomocaoVinculada = objPsProj.consultaLocomocaoVinculadaGuiaSaldo(idMandJud);  		                   
		
		if (!listaLocomocaoVinculada.isEmpty()) {
		
		   if (qtdLiberar <= listaLocomocaoVinculada.size()) {				
			
		       for (int i = 0; i < qtdLiberar; i++) {				    
    	           quantidade = objPsProj.liberaLocomocaoGuiaSaldo(listaLocomocaoVinculada.get(i).getIdLocomocao(),
    	      		              listaLocomocaoVinculada.get(i).getIdGuiaItem());
			       if (quantidade == 0) {
			   	       throw new MensagemException("Erro ao liberar locomoção do mandado de guia de saldo");
			       }			   
			       qtdLiberada ++;	
		       }      
		       
		       objDtAdabas = this.consultaGuiaSaldoSPG(listaLocomocaoVinculada.get(0).getIdProc());
		   
		       objDtProj = listaLocomocaoVinculada.get(0);
		   
		       valorSaldoAtual = objDtAdabas.getGuiaSaldoValorAtualizado() + (objDtProj.getLocomocaoValorUnitario() * qtdLiberada);   
		   
		       objPsProj.atualizaGuiaSaldoProjudi(objDtProj.getIdGuiaSaldo(), objDtAdabas.getGuiaSaldoStatus(), valorSaldoAtual);
		   
		       objPsAdabas.atualizaGuiaSaldoSPG(objDtProj.getNumeroGuiaSaldo(), valorSaldoAtual);
		   
		       objDtProj.setGuiaSaldoValorAtualizado(valorSaldoAtual);
		       objDtProj.setGuiaSaldoValorAnterior(objDtAdabas.getGuiaSaldoValorAtualizado());		       
		   
		       objPsAdabas.gravaLogGuiaSaldoSpg(objDtProj);			
		  
		       LogPs obLogPs = new LogPs(fabricaConexao.getConexao());
		       LogDt obLogDt = new LogDt();
		       obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.AtualizaGuiaSaldo));
		       obLogDt.setId_Usuario(UsuarioDt.SistemaProjudi);
		       obLogDt.setIpComputador("");
		       obLogDt.setTabela("guia_emis");
		       obLogDt.setValorAtual("Guia.." + objDtProj.getNumeroGuiaSaldo() + " Saldo.." + objDtAdabas.getGuiaSaldoValorAtualizado()
		        	+ " Loc. a Liberar.." + quantidade + " Valr.Unit.Loc.." + objDtProj.getLocomocaoValorUnitario()
				    + " Mand.." + idMandJud + "  Id.Proc.." + objDtProj.getIdProc());
		       obLogDt.setValorNovo("Saldo.." + valorSaldoAtual + " Locomoção Liberada.." + qtdLiberada);
		       obLogDt.setCodigoTemp("");
		       obLogDt.setId_Tabela(Integer.toString(objDtProj.getIdGuiaSaldo()));
		       obLogDt.setHash("");
		       obLogDt.setQtdErrosDia(0);
		       obLogPs.inserir(obLogDt);
		    }
		}
		return qtdLiberada;
	}
	
	public List consultaGuiaVinculadaSaldoSPG(int idProc) throws Exception {
		FabricaConexao obFabricaConexao = null;
		FabricaConexao obFabricaAdabas = null;
		List <ImportaDadosSPGDt> listaGuiaVinculadaSaldoSpg  = new ArrayList<>();
		try {
			if(idProc != 0 ) {
	    		
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				obFabricaAdabas = new FabricaConexao(FabricaConexao.ADABAS);
	    		
	    		ImportaDadosSPGPs obPersistencia = new ImportaDadosSPGPs(obFabricaConexao.getConexao());
	    		
	    		String procNumero[] = obPersistencia.consultaProcNumero(idProc);
	    		
	    		ImportaDadosSPGPs obAdabas = new ImportaDadosSPGPs(obFabricaAdabas.getConexao());
	    		
	            listaGuiaVinculadaSaldoSpg = obAdabas.consultaGuiaVinculadaSaldoSpg(procNumero[0] + procNumero[1] + procNumero[2]);    	    		
			}
    	}
    	finally {
    		if (obFabricaAdabas !=  null) obFabricaAdabas.fecharConexao();
    		if (obFabricaConexao != null) obFabricaConexao.fecharConexao();    		 
    	}
    	
    	return listaGuiaVinculadaSaldoSpg;
	}
	
	///////////////////////////////  tirar
	public List mapaFinanceiroMandadoGratuitoSpg(List<RelatoriosMandadoDt> listaOficiaisProj, String anoMes,
			String nomeComarca, String cpfUsuario) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);

		List<RelatoriosMandadoDt> listaOficiais = new ArrayList<>();

		try {
			List<RelatoriosMandadoDt> listaOficiaisSpg = new ArrayList<>();
			List<RelatoriosMandadoDt> listaOficiaisOrdenada = new ArrayList<>();

			for (int k = 0; k < listaOficiaisProj.size(); k++) {
				listaOficiaisProj.get(k)
						.setNomeComarca(Funcoes.RemoveAcentos(listaOficiaisProj.get(k).getNomeComarca()));
			}

			ImportaDadosSPGPs objPs = new ImportaDadosSPGPs(obFabricaConexao.getConexao());

			nomeComarca = Funcoes.RemoveAcentos(nomeComarca);

			listaOficiaisSpg = objPs.mapaFinanceiroMandadoGratuitoSpg(anoMes, nomeComarca, cpfUsuario);

			if (!listaOficiaisProj.isEmpty() || !listaOficiaisSpg.isEmpty()) {
				listaOficiais = (comparaElementoLista(listaOficiaisProj, listaOficiaisSpg));
			}

		} finally {

			obFabricaConexao.fecharConexao();
		}

		return listaOficiais;
	}

	public List<RelatoriosMandadoDt> comparaElementoLista(List<RelatoriosMandadoDt> listaOficiaisProj,
			List<RelatoriosMandadoDt> listaOficiaisSpg) throws Exception {

		RelatoriosMandadoDt objDt = new RelatoriosMandadoDt();

		boolean acheiNaListaProjudi = false;

		for (int j = 0; j < listaOficiaisSpg.size(); j++) {

			for (int k = 0; k < listaOficiaisProj.size(); k++) {

				acheiNaListaProjudi = false;

				listaOficiaisSpg.get(j)
						.setCpfUsuario(Funcoes.completaCPFZeros(listaOficiaisSpg.get(j).getCpfUsuario()));

				if (listaOficiaisSpg.get(j).getCpfUsuario()
						.equalsIgnoreCase(listaOficiaisProj.get(k).getCpfUsuario())) {
					//
					// se o elemento da lista spg existir na lista
					// projudi
					// mantem a lista projudi
					// com o somatoria de mandados por mes.
					//

					listaOficiaisProj.get(k).setQuantMandadoMesAnt1(listaOficiaisSpg.get(j).getQuantMandadoMesAnt1()
							+ listaOficiaisProj.get(k).getQuantMandadoMesAnt1());

					listaOficiaisProj.get(k).setQuantMandadoMesAnt2(listaOficiaisSpg.get(j).getQuantMandadoMesAnt2()
							+ listaOficiaisProj.get(k).getQuantMandadoMesAnt2());

					listaOficiaisProj.get(k).setQuantMandadoMesAnt3(listaOficiaisSpg.get(j).getQuantMandadoMesAnt3()
							+ listaOficiaisProj.get(k).getQuantMandadoMesAnt3());

					listaOficiaisProj.get(k).setQuantMandadoMesAnt4(listaOficiaisSpg.get(j).getQuantMandadoMesAnt4()
							+ listaOficiaisProj.get(k).getQuantMandadoMesAnt4());

					listaOficiaisProj.get(k).setQuantMandadoMesAnt5(listaOficiaisSpg.get(j).getQuantMandadoMesAnt5()
							+ listaOficiaisProj.get(k).getQuantMandadoMesAnt5());

					listaOficiaisProj.get(k).setQuantMandadoMesAnt6(listaOficiaisSpg.get(j).getQuantMandadoMesAnt6()
							+ listaOficiaisProj.get(k).getQuantMandadoMesAnt6());

					listaOficiaisProj.get(k).setQuantMandadoMesAnt7(listaOficiaisSpg.get(j).getQuantMandadoMesAnt7()
							+ listaOficiaisProj.get(k).getQuantMandadoMesAnt7());

					acheiNaListaProjudi = true;
					break;
				}

			}
			if (!acheiNaListaProjudi) {
				//
				// adiciona elemento spg inexistente na lista projudi
				//
				objDt = new RelatoriosMandadoDt();
				objDt = listaOficiaisSpg.get(j);
				objDt.setNomeUsuario(listaOficiaisSpg.get(j).getNomeUsuario().trim());
				objDt.setEscalaTipo(listaOficiaisSpg.get(j).getEscalaTipo().trim());
				objDt.setNomeComarca(listaOficiaisSpg.get(j).getNomeComarca().trim());
				if (objDt.getContaDv() == null)
					objDt.setContaDv("0");
				listaOficiaisProj.add(objDt);
			}
		}

		return listaOficiaisProj;
	}

	
	
	/// fim tirar
	
	
	
	
}
