package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.EscalaMgDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoFaixaValorDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.projudi.ps.LogPs;
import br.gov.go.tj.projudi.ps.MandadoFaixaValorPs;
import br.gov.go.tj.projudi.ps.RelatoriosMandadoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Relatorios;

public class RelatoriosMandadoNe {

	private static final long serialVersionUID = -5736662801761842871L;

	List<RelatoriosMandadoDt> listaOficiaisReceber = new ArrayList();	
	 
	double valorFaixaDefinido = 0;
	int quantFaixaDefinido = 0;
	double valorResolutivoDefinido = 0;
	double valorAcimaFaixaDefinido = 0;
	double valorCenopesDefinido = 0;
	
	////////////////////////////////////  tirar
	//List<RelatoriosMandadoDt> listaOficiaisReceber = new ArrayList();
	List<MandadoFaixaValorDt> listaFaixaValor = new ArrayList();

	int mesesComMandado = 0;
	int somatorioMandados = 0;
	int mediaMandados = 0;
	//
	// variaveis texto banco
	//
	int sequenciaLote = 0;
	int sequenciaRegistroLote = 0;
	int totalGeralLinhas = 0;
	double somatorioValorLote = 0;

	
	/////  fim tirar
	

	public RelatoriosMandadoNe() {
	}

	public byte[] relatorioMensalMandadoGratuitoNOVO(int tipoArquivo, String diretorioProjeto, String dataReferencia,
			String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "relatorioMensalMandadoGratuito";

	
		MandadoFaixaValorPs mandadoFaixaValorPs;
		RelatoriosMandadoPs relatoriosMandadoPs;
		
		List<RelatoriosMandadoDt> listaOficiais = new ArrayList<>();	
		List<RelatoriosMandadoDt> listaMandados = new ArrayList<>();
		List<MandadoFaixaValorDt> listaFaixaValor = new ArrayList();

		FabricaConexao objFc = null;

		byte[] temp = null;

		String anoMes = "";

		int idUsuario;
		int idServentia;

		if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
			idUsuario = 0;
			// idServentia = 0;
			idServentia = 5821;
		} else {
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
				idUsuario = Integer.parseInt(idUsuarioSessao);
				idServentia = 0;
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
					idServentia = Integer.parseInt(idServentiaSessao);
					idUsuario = 0;
				} else {
					throw new MensagemException("Grupo de acesso inválido");
				}
			}
		}
		try {
			//
			// Monta anoMes. Parametro para buscar quantidade de mandados.
			//

			int ano = Integer.parseInt(dataReferencia.substring(6));
			int mes = Integer.parseInt(dataReferencia.substring(3, 5));

			if (mes < 10) {
				anoMes = ano + "0" + mes;
			} else {
				anoMes = ano + "" + mes;
			}

			objFc = new FabricaConexao(FabricaConexao.CONSULTA);

			relatoriosMandadoPs = new RelatoriosMandadoPs(objFc.getConexao());

			mandadoFaixaValorPs = new MandadoFaixaValorPs(objFc.getConexao());

			listaMandados = relatoriosMandadoPs.listaMandadosGratuitos(anoMes, tipoArquivo, idUsuario,
					idServentia);
			//
			// inicio spg - so enquanto existir dados no spg
			//
			// adiciona na lista, oficiais importados do spg.
			//
			//
			// busca comarca e cpf usuario , para limitar pesquisa, de acordo com a
			// serventia sessão e usuario sessão.
			//

			String codigoComarca = "";
			String cpfUsuario = "";
			String nomeComarca = "";

			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
				cpfUsuario = "";
				nomeComarca = "SENADOR CANEDO";
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
					nomeComarca = "";
					UsuarioNe usuarioNe = new UsuarioNe();
					UsuarioDt usuarioDt = null;
					usuarioDt = usuarioNe.consultarId(idUsuarioSessao);
					cpfUsuario = usuarioDt.getCpf();

				} else {
					if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
						cpfUsuario = "";
						ServentiaNe serventiaNe = new ServentiaNe();
						ServentiaDt serventiaDt = null;
						serventiaDt = serventiaNe.consultarId(idServentiaSessao);
						Funcoes funcao = new Funcoes();
						codigoComarca = funcao.completarZeros(serventiaDt.getServentiaCodigoExterno(), 6);
						codigoComarca = codigoComarca.substring(0, 3);
						ComarcaNe comarcaNe = new ComarcaNe();
						nomeComarca = comarcaNe.consultarCodigo(codigoComarca);

					} else {
						throw new MensagemException("Grupo de acesso inválido");
					}
				}
			}

			ImportaDadosSPGNe importaDadosSPGNe = new ImportaDadosSPGNe();
			listaMandados = importaDadosSPGNe.listaMandadosGratuitosSpgNOVO(listaMandados, anoMes, nomeComarca,
					cpfUsuario);

			////////////////////////////////////////////// fim spg
			
			listaFaixaValor = mandadoFaixaValorPs.consultaMandadoFaixaValorPorData(dataReferencia);

			if (listaMandados.isEmpty())
				throw new MensagemException("Não existe resultado para as informações solicitadas.");

			if (listaFaixaValor.isEmpty())
				throw new MensagemException("Mandado faixa valor não encontrado.");

			if (listaFaixaValor.size() != MandadoFaixaValorDt.QTDE_REG_MAND_FAIXA_VALOR)
				throw new MensagemException("Numero de registros inválidos para mandado faixa valor.");

			for (int i = 0; i < listaFaixaValor.size(); i++) {

				if (listaFaixaValor.get(i).getTipoValor().equalsIgnoreCase("VALOR FAIXA UNICA")) {
					this.valorFaixaDefinido = (Double.parseDouble(listaFaixaValor.get(i).getValor()));
					this.quantFaixaDefinido = (Integer.parseInt(listaFaixaValor.get(i).getFaixaFim()));
				}

				if (listaFaixaValor.get(i).getTipoValor().equalsIgnoreCase("VALOR ACIMA FAIXA"))
					this.valorAcimaFaixaDefinido = (Double.parseDouble(listaFaixaValor.get(i).getValor()));

				if (listaFaixaValor.get(i).getTipoValor().equalsIgnoreCase("VALOR RESOLUTIVO"))
					this.valorResolutivoDefinido = (Double.parseDouble(listaFaixaValor.get(i).getValor()));

				if (listaFaixaValor.get(i).getTipoValor().equalsIgnoreCase("CENOPES ACIMA FAIXA"))
					this.valorCenopesDefinido = (Double.parseDouble(listaFaixaValor.get(i).getValor()));
			}
			
			this.quantFaixaDefinido = 1;
			

			if (this.valorFaixaDefinido == 0 || this.valorResolutivoDefinido == 0 || this.valorAcimaFaixaDefinido == 0
					|| this.valorCenopesDefinido == 0 || this.quantFaixaDefinido == 0)
				throw new MensagemException("Valores não encontrados para mandado faixa valor.");

			listaMandados = ordenaListaPorDataHora(listaMandados);

			listaOficiais = geraListaOficiaisSintetica(listaMandados);

			for (int i = 0; i < listaOficiais.size(); i++) {

				RelatoriosMandadoDt objDt = new RelatoriosMandadoDt();

				objDt = (listaOficiais.get(i));

				switch (objDt.getEscalaTipo()) {
				case "Plantão":
					pagamentoPlantao(objDt);
					break;
				case "Cenops":
					pagamentoCenops(objDt);
					break;
				case "Normal":
					pagamentoNormal(objDt);
					break;
				default:
					break;
				}
			}

			if (tipoArquivo == 2) { // gera texto banco. quando implantar saja gera por ele acho
				int seqTexto = relatoriosMandadoPs.buscaSequenciaTextoBanco();

				GeraTextoBancoNe geraTextoBancoNe = new GeraTextoBancoNe();

				StringBuffer sbTextoBanco = geraTextoBancoNe.textoMandadoGratuito(listaOficiaisReceber, seqTexto);

				temp = sbTextoBanco.toString().getBytes();

				relatoriosMandadoPs = new RelatoriosMandadoPs(objFc.getConexao());

				LogPs obLogPs = new LogPs(objFc.getConexao());

				LogDt obLogDt = new LogDt();
				obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.TextoBancoMandadoGratuito));
				obLogDt.setId_Usuario(idUsuarioSessao);
				obLogDt.setIpComputador("");
				obLogDt.setData(null);
				obLogDt.setHora(null);
				obLogDt.setTabela("");
				obLogDt.setValorAtual(sbTextoBanco.toString());
				obLogDt.setValorNovo("Competencia..: " + anoMes + "  Sequencia Texto.. " + seqTexto
						+ "  Data Referencia.. " + dataReferencia);
				obLogDt.setCodigoTemp("");
				obLogDt.setId_Tabela("");
				obLogDt.setHash("");
				obLogDt.setQtdErrosDia(0);
				obLogPs.inserir(obLogDt);

			} else {

				ordenaListaPorComarcaUsuario();

				ano = Integer.parseInt(dataReferencia.substring(6));
				mes = Integer.parseInt(dataReferencia.substring(3, 5));

				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("titulo", "Relatório Mensal de Mandados Gratuitos");
				parametros.put("competencia", Funcoes.identificarNomeMes(mes) + "/" + ano);
				parametros.put("dataAtual", new Date());
				parametros.put("valorFaixaDefinido", this.valorFaixaDefinido);
				parametros.put("quantFaixaDefinido", this.quantFaixaDefinido);
				parametros.put("valorResolutivoDefinido", this.valorResolutivoDefinido);
				parametros.put("valorAcimaFaixaDefinido", this.valorAcimaFaixaDefinido);
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros,
						this.listaOficiaisReceber);
			}
		} finally

		{
			if (objFc != null)
				objFc.fecharConexao();
		}
		return temp;
	}

	public void pagamentoNormalNOVO(RelatoriosMandadoDt objDt) {

		if (objDt.getQuantFaixaReceber() != 0) {

			if (objDt.getQuantFaixaReceber() > this.quantFaixaDefinido) { // faixa unica
				objDt.setValorAcimaFaixaReceber(Double.valueOf(objDt.getQuantFaixaReceber() - this.quantFaixaDefinido)
						* this.valorAcimaFaixaDefinido);
				objDt.setQuantAcimaFaixaReceber(objDt.getQuantFaixaReceber() - this.quantFaixaDefinido);
			} else
				objDt.setValorAcimaFaixaReceber(0);

			objDt.setValorResolutivoReceber(objDt.getQuantResolutivoReceber() * this.valorResolutivoDefinido);

			objDt.setValorReceber(this.valorFaixaDefinido + objDt.getValorFaixaReceber()
					+ objDt.getValorResolutivoReceber() + objDt.getValorAcimaFaixaReceber());
			objDt.setCpfUsuario(Funcoes.formataCPF(objDt.getCpfUsuario()));

			this.listaOficiaisReceber.add(objDt);

		}
	}

	public void pagamentoPlantaoNOVO(RelatoriosMandadoDt objDt) {
		//
		// se nao tiver mandados no primeiro mes nao paga nada
		//
		// recebe sempre a partir do grupo 2
		//

		if (objDt.getQuantFaixaReceber() != 0) {
			objDt.setCpfUsuario(Funcoes.formataCPF(objDt.getCpfUsuario()));
			this.listaOficiaisReceber.add(objDt);
		}
	}

	public void pagamentoCenopsNOVO(RelatoriosMandadoDt objDt) {
		//
		// ver como fica se nao trabalhou nos ultimos 7 meses. - ne nao estiver na lista
		// tem que pagar sim.
		//
		// se nao tiver mandados no primeiro mes recebe pela faixa 1
		//
		// se tiver calcula pela media.
		//

		if (objDt.getQuantFaixaReceber() == 0) {

		} else {

		}

		objDt.setCpfUsuario(Funcoes.formataCPF(objDt.getCpfUsuario()));
		this.listaOficiaisReceber.add(objDt);
	}
	
	public void ordenaListaPorComarcaUsuario() {

		RelatoriosMandadoDt aux1 = null;
		RelatoriosMandadoDt aux2 = null;
		boolean ordenado = false;
		String nome1 = "";
		String nome2 = "";
		while (ordenado == false) {
			ordenado = true;
			for (int pos = 0; pos < this.listaOficiaisReceber.size(); pos++) {
				if (pos + 1 < this.listaOficiaisReceber.size()) {
					aux1 = (this.listaOficiaisReceber.get(pos));
					aux2 = (this.listaOficiaisReceber.get(pos + 1));
					nome1 = aux1.getNomeComarca() + aux1.getNomeUsuario();
					nome2 = aux2.getNomeComarca() + aux2.getNomeUsuario();
					int comp = nome1.compareTo(nome2);
					if (comp > 0) {
						ordenado = false;
						this.listaOficiaisReceber.set(pos, aux2);
						this.listaOficiaisReceber.set(pos + 1, aux1);
					}
				}
			}
		}
	}

	public List<RelatoriosMandadoDt> ordenaListaPorDataHora(List<RelatoriosMandadoDt> listaMandados) throws Exception {
		RelatoriosMandadoDt aux1 = null;
		RelatoriosMandadoDt aux2 = null;
		boolean ordenado = false;
		String nome1 = "";
		String nome2 = "";
		while (ordenado == false) {
			ordenado = true;
			for (int pos = 0; pos < listaMandados.size(); pos++) {
				if (pos + 1 < listaMandados.size()) {
					aux1 = (listaMandados.get(pos));
					aux2 = (listaMandados.get(pos + 1));
					nome1 = aux1.getNomeUsuario() + aux1.getDataString() + aux1.getHoraString();
					nome2 = aux2.getNomeUsuario() + aux2.getDataString() + aux2.getHoraString();
					int comp = nome1.compareTo(nome2);
					if (comp > 0) {
						ordenado = false;
						listaMandados.set(pos, aux2);
						listaMandados.set(pos + 1, aux1);
					}
				}
			}
		}
		return listaMandados;
	}

	public List<RelatoriosMandadoDt> geraListaOficiaisSintetica(List<RelatoriosMandadoDt> listaMandados) throws Exception {

		int contaRegistro = 0;
		int contaResolutivo = 0;

		List <RelatoriosMandadoDt> listaOficiais = new ArrayList<>();		

		RelatoriosMandadoDt objDt = null;		

		String nomeAnterior = listaMandados.get(0).getNomeUsuario();

		for (int i = 0; i < listaMandados.size(); i++) {	

			if (!nomeAnterior.equalsIgnoreCase(listaMandados.get(i).getNomeUsuario())) {
				
				objDt.setQuantFaixaReceber(contaRegistro);

				objDt.setQuantResolutivoReceber(contaResolutivo);

				if (contaRegistro > this.quantFaixaDefinido)
					objDt.setQuantAcimaFaixaReceber(contaRegistro - this.quantFaixaDefinido);

				listaOficiais.add(objDt);

				objDt = (listaMandados.get(i));

				nomeAnterior = objDt.getNomeUsuario();
				contaRegistro = 1;
				contaResolutivo = 0;

			} else {

				objDt = (listaMandados.get(i));				
				contaRegistro++;
				if (contaRegistro > this.quantFaixaDefinido)
					if (objDt.getResolutivo() == 1)
						contaResolutivo++;
			}		
		
		}
		
		objDt.setQuantFaixaReceber(contaRegistro);

		objDt.setQuantResolutivoReceber(contaResolutivo);

		if (contaRegistro > this.quantFaixaDefinido)
			objDt.setQuantAcimaFaixaReceber(contaRegistro - this.quantFaixaDefinido);

		listaOficiais.add(objDt);

		return listaOficiais;
	}

	public byte[] listaOrdemPagamentoAutorizada(String diretorioProjeto, String dataInicial, String dataFinal,
			String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "listaOrdemPagamentoAutorizada";

		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		MandadoJudicialDt mandadoJudicialDt = null;
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaOficiais = new ArrayList<>();

		FabricaConexao obFabricaConexao = null;
		byte[] temp = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

			int idUsuario = 0;

			int idServentia = 0;

			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA)))
				idUsuario = Integer.parseInt(idUsuarioSessao);
			else if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO)))
				idServentia = Integer.parseInt(idServentiaSessao);
			else
				return null;

			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());
			listaOficiais = relatoriosMandadoPs.listaOrdemPagamentoAutorizada(dataInicial, dataFinal, idUsuario,
					idServentia);

			if (listaOficiais.isEmpty())
				return null;

			for (int i = 0; i < listaOficiais.size(); i++) {
				mandadoJudicialDt = new MandadoJudicialDt();
				mandadoJudicialDt.setId(listaOficiais.get(i).getIdMandJud());
				mandadoJudicialDt = mandadoJudicialNe.calculaValorLocomocao(mandadoJudicialDt);
				if (!mandadoJudicialDt.getQuantidadeLocomocao().equalsIgnoreCase("")) {
					listaOficiais.get(i).setQuantidadeLocomocao(mandadoJudicialDt.getQuantidadeLocomocao());
					listaOficiais.get(i).setValorLocomocao(Double.parseDouble(mandadoJudicialDt.getValorLocomocao()));
				}
			}

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Ordens de Pagamento Autorizadas.");
			parametros.put("periodo", dataInicial + " a " + dataFinal);
			parametros.put("dataFinal", dataFinal);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaOficiais);

		} finally {
			if (obFabricaConexao != null)
				obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public byte[] RelatorioFinanceiroMandadoComCustas(int tipoArquivo, String diretorioProjeto, String dataInicial,
			String dataFinal, String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "relatorioFinanceiroMandadoComCustas"; // jasper
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaOficiais = new ArrayList<>();

		FabricaConexao obFabricaConexao = null;

		int idUsuario;

		int idServentia;

		if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
			idUsuario = 0;
			idServentia = 0;
		} else {
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
				idUsuario = Integer.parseInt(idUsuarioSessao);
				idServentia = 0;
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
					idServentia = Integer.parseInt(idServentiaSessao);
					idUsuario = 0;
				} else {
					return null;
				}
			}
		}

		byte[] temp = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());

			listaOficiais = relatoriosMandadoPs.RelatorioFinanceiroMandadoComCustas(dataInicial, dataFinal, tipoArquivo,
					idUsuario, idServentia);

			if (listaOficiais.isEmpty())
				return null;

			if (tipoArquivo == 2) {

				int seqTexto = relatoriosMandadoPs.buscaSequenciaTextoBanco();

				GeraTextoBancoNe geraTextoBancoNe = new GeraTextoBancoNe();

				StringBuffer sbTextoBanco = geraTextoBancoNe.textoMandadoComCustas(listaOficiais, seqTexto);
				//
				// atualiza status mandado
				//
				MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();

				mandadoJudicialNe.alteraPagamentoStatus(dataInicial, dataFinal, idUsuarioSessao);

				temp = sbTextoBanco.toString().getBytes();

				LogPs obLogPs = new LogPs(obFabricaConexao.getConexao());

				LogDt obLogDt = new LogDt();
				obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.TextoBancoMandadoComCustas));
				obLogDt.setId_Usuario(idUsuarioSessao);
				obLogDt.setIpComputador("");
				obLogDt.setData(Funcoes.getDataHoraAtual());
				obLogDt.setHora(null);
				obLogDt.setTabela("");
				obLogDt.setValorAtual(sbTextoBanco.toString());
				obLogDt.setValorNovo("Data Inicial..: " + dataInicial + "Data Final..: " + dataFinal
						+ "  Sequencia Texto.. " + seqTexto);
				obLogDt.setCodigoTemp("");
				obLogDt.setId_Tabela("");
				obLogDt.setHash("");
				obLogDt.setQtdErrosDia(0);
				obLogPs.inserir(obLogDt);

			} else {

				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("titulo", "Relatório Financeiro Mandado COM CUSTAS.");
				parametros.put("dataInicial", dataInicial);
				parametros.put("dataFinal", dataFinal);
				parametros.put("dataAtual", new Date());
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaOficiais);
			}

		} finally {
			if (obFabricaConexao != null)
				obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public byte[] listaMandadosDistribuidos(String assistencia, String diretorioProjeto, String dataInicial,
			String dataFinal, String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao, String idUsuarioTela, String tipoRelatorio) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);

		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		MandadoJudicialDt mandadoJudicialDt = null;
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaMandados = new ArrayList<>();
		String nomeRelatorio = "";
		FabricaConexao obFabricaConexao = null;
		byte[] temp = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

			int idUsuario = 0;

			int idServentia = 0;

			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA)))
				idUsuario = Integer.parseInt(idUsuarioSessao);
			else if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
				idServentia = Integer.parseInt(idServentiaSessao);
				idUsuario = Integer.parseInt(idUsuarioTela); // quando coordenador escolhe ver somente um oficial

			} else
				return null;

			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());

			if (tipoRelatorio.equalsIgnoreCase("analitico")) {
				listaMandados = relatoriosMandadoPs.listaMandadosDistribuidosAnalitico(dataInicial, dataFinal,
						idUsuario, assistencia, idServentia);

				if (listaMandados.isEmpty())
					return null;

				for (int i = 0; i < listaMandados.size(); i++) {
					mandadoJudicialDt = new MandadoJudicialDt();
					mandadoJudicialDt.setId(listaMandados.get(i).getIdMandJud());
					if (listaMandados.get(i).getAssistencia().equalsIgnoreCase("COM CUSTAS")) {
						mandadoJudicialDt = mandadoJudicialNe.calculaValorLocomocao(mandadoJudicialDt);
						if (!mandadoJudicialDt.getQuantidadeLocomocao().equalsIgnoreCase("")) {
							listaMandados.get(i).setQuantidadeLocomocao(mandadoJudicialDt.getQuantidadeLocomocao());
							listaMandados.get(i).setValor(mandadoJudicialDt.getValorLocomocao());
						}
					}
					if (assistencia.equalsIgnoreCase("sim")) {
						listaMandados.get(i).setStatusPagamento(null);
					} else {
						listaMandados.get(i).setEscalaTipo("");
					}
				}
				nomeRelatorio = "listaMandadosDistribuidosAnalitico";
			} else {
				listaMandados = relatoriosMandadoPs.listaMandadosDistribuidosSintetico(dataInicial, dataFinal,
						idUsuario, assistencia, idServentia);
				nomeRelatorio = "listaMandadosDistribuidosSintetico";
			}

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Mandados Distribuídos");
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaMandados);

		} finally

		{
			if (obFabricaConexao != null)
				obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public byte[] relatorioMandadosRedistribuidos(String diretorioProjeto, String dataInicial, String dataFinal,
			String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao, String idUsuarioTela) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);

		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		MandadoJudicialDt mandadoJudicialDt = null;
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaMandados = new ArrayList<>();
		String nomeRelatorio = "";
		FabricaConexao obFabricaConexao = null;
		byte[] temp = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

			int idUsuario = 0;

			int idServentia = 0;

			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA)))
				idUsuario = Integer.parseInt(idUsuarioSessao);
			else if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
				idServentia = Integer.parseInt(idServentiaSessao);
				idUsuario = Integer.parseInt(idUsuarioTela); // quando coordenador escolhe ver somente um oficial

			} else
				return null;

			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());

			listaMandados = relatoriosMandadoPs.relatorioMandadosRedistribuidos(dataInicial, dataFinal, idUsuario,
					idServentia);

			if (listaMandados.isEmpty())
				return null;

			for (int i = 0; i < listaMandados.size(); i++) {
				mandadoJudicialDt = new MandadoJudicialDt();
				mandadoJudicialDt.setId(listaMandados.get(i).getIdMandJud());
				mandadoJudicialDt = mandadoJudicialNe.calculaValorLocomocao(mandadoJudicialDt);
				if (!mandadoJudicialDt.getQuantidadeLocomocao().equalsIgnoreCase("")) {
					listaMandados.get(i).setQuantidadeLocomocao(mandadoJudicialDt.getQuantidadeLocomocao());
					listaMandados.get(i).setValor(mandadoJudicialDt.getValorLocomocao());
				}
				listaMandados.get(i).setStatusPagamento(null);
				listaMandados.get(i).setEscalaTipo("");
			}

			nomeRelatorio = "relatorioMandadosRedistribuidos";
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Mandados Redistribuídos");
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaMandados);

		} finally

		{
			if (obFabricaConexao != null)
				obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public byte[] listaMandadosPorOficial(String diretorioProjeto, String dataInicial, String dataFinal,
			String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao, String idUsuarioTela, int tipoOpcao) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "listaMandadosPorOficial"; // jasper

		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaMandados = new ArrayList<>();

		FabricaConexao obFabricaConexao = null;
		byte[] temp = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

			int idUsuario = 0;

			int idServentia = 0;

			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA)))
				idUsuario = Integer.parseInt(idUsuarioSessao);
			else if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
				idServentia = Integer.parseInt(idServentiaSessao);
				idUsuario = Integer.parseInt(idUsuarioTela); // quando coordenador escolhe ver somente um oficial
			} else
				return null;

			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());

			if (tipoOpcao == 5) // devolvido
				listaMandados = relatoriosMandadoPs.listaMandadosPorOficial(dataInicial, dataFinal, idUsuario,
						idServentia);
			else
				listaMandados = relatoriosMandadoPs.listaMandadosPorOficial(dataInicial, dataFinal, idUsuario,
						idServentia, tipoOpcao);

			if (listaMandados.isEmpty())
				return null;

			String dataRelatorio = "";

			if (tipoOpcao == 1) {
				dataRelatorio = "MANDADOS CONCLUÍDOS    De: " + dataInicial + " a " + dataFinal;
			} else if (tipoOpcao == 2) {
				dataRelatorio = "COM O OFICAL NA DATA ATUAL";
			} else if (tipoOpcao == 3) {
				dataRelatorio = "MANDADOS ATRASADOS NA DATA ATUAL";
				for (int i = 0; i < listaMandados.size(); i++) { // calculo dias atrasado
					listaMandados.get(i).setDiferencaDias(Integer.toString(Funcoes.calculaDiferencaEntreDatas(
							listaMandados.get(i).getDataLimite(), Funcoes.getDataHoraAtual())));
				}
			} else if (tipoOpcao == 4) {
				dataRelatorio = "MANDADOS AD HOC        De: " + dataInicial + " a " + dataFinal;
			} else if (tipoOpcao == 5) {
				dataRelatorio = "MANDADOS DEVOLVIDOS    De: " + dataInicial + " a " + dataFinal;
			}

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Mandados por Oficial");
			parametros.put("dataRelatorio", dataRelatorio);
			parametros.put("dataFinal", dataFinal);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaMandados);

		} finally

		{
			if (obFabricaConexao != null)
				obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public byte[] RelatorioFinanceiroMandadoAdHoc(String dataReferencia, String diretorioProjeto,
			String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "relatorioFinanceiroMandadoAdHoc"; // jasper
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaOficiais = new ArrayList<>();

		FabricaConexao obFabricaConexao = null;

		int idUsuario;

		int idServentia;

		if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
			idUsuario = 0;
			idServentia = 0;
		} else {
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
				idUsuario = Integer.parseInt(idUsuarioSessao);
				idServentia = 0;
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
					idServentia = Integer.parseInt(idServentiaSessao);
					idUsuario = 0;
				} else {
					return null;
				}
			}
		}

		byte[] temp = null;

		int ano = Integer.parseInt(dataReferencia.substring(6));
		int mes = Integer.parseInt(dataReferencia.substring(3, 5));

		String anoMes = "";
		if (mes < 10) {
			anoMes = ano + "0" + mes;
		} else {
			anoMes = ano + "" + mes;
		}

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());

			listaOficiais = relatoriosMandadoPs.RelatorioFinanceiroMandadoAdHoc(anoMes, idUsuario, idServentia);

			if (listaOficiais.isEmpty())
				return null;

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Relatório Financeiro Mandado AD HOC");
			parametros.put("competencia", Funcoes.identificarNomeMes(mes) + "/" + ano);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaOficiais);

		} finally

		{
			if (obFabricaConexao != null)
				obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public byte[] RelatorioFinanceiroGuiaVinculada(int tipoArquivo, String dataInicial, String dataFinal,
			String diretorioProjeto, String usuarioResponsavelRelatorio, String idUsuarioSessao,
			String codigoGrupoSessao, String idServentiaSessao) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "relatorioFinanceiroGuiaVinculada"; // jasper
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaGuias = new ArrayList<>();
		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		MandadoJudicialDt mandadoJudicialDt = null;

		FabricaConexao obFabricaConexao = null;

		int idUsuario;

		int idServentia;

		if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
			idUsuario = 0;
			idServentia = 0;
		} else {
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
				idUsuario = Integer.parseInt(idUsuarioSessao);
				idServentia = 0;
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
					idServentia = Integer.parseInt(idServentiaSessao);
					idUsuario = 0;
				} else {
					return null;
				}
			}
		}

		byte[] temp = null;

		int ano = Integer.parseInt(dataFinal.substring(6));
		int mes = Integer.parseInt(dataFinal.substring(3, 5));

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());

			listaGuias = relatoriosMandadoPs.RelatorioFinanceiroGuiaVinculada(dataInicial, dataFinal, idUsuario,
					idServentia);

			if (listaGuias.isEmpty())
				return null;

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", "Guias Complementares Vinculadas ao Oficial");
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("dataAtual", new Date());
			parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaGuias);

		} finally

		{
			if (obFabricaConexao != null)
				obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	
	////////////////////////  tirar
	////////////////////////
	
	public byte[] relatorioFinanceiroMandadoGratuito(int tipoArquivo, String diretorioProjeto, String dataReferencia,
			String usuarioResponsavelRelatorio, String idUsuarioSessao, String codigoGrupoSessao,
			String idServentiaSessao) throws Exception {

		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "relatorioFinanceiroMandadoGratuito";

		RelatoriosMandadoDt objDt;
		MandadoFaixaValorPs mandadoFaixaValorPs;
		RelatoriosMandadoPs relatoriosMandadoPs;
		List<RelatoriosMandadoDt> listaOficiaisProj = new ArrayList<>();
		List<RelatoriosMandadoDt> listaOficiais = new ArrayList<>();

		FabricaConexao obFabricaConexao = null;
		byte[] temp = null;

		int idUsuario;

		int idServentia;

		if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
			idUsuario = 0;
		//	idServentia = 0;
			idServentia = 5821; 				  				
		} else {
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
				idUsuario = Integer.parseInt(idUsuarioSessao);
				idServentia = 0;
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
					idServentia = Integer.parseInt(idServentiaSessao);
					idUsuario = 0;
				} else {
					return null;
				}
			}
		}
		try {
			int ano = Integer.parseInt(dataReferencia.substring(6));
			int mes = Integer.parseInt(dataReferencia.substring(3, 5));
			//
			// Monta Array anoMes. Parametro para buscar mandados por mes.
			//
			String[] arrayAnoMes = new String[7];

			for (int i = 0; i <= 6; i++) {

				if (mes < 10) {
					arrayAnoMes[i] = ano + "0" + mes;
				} else {
					arrayAnoMes[i] = ano + "" + mes;
				}
				if (mes == 1) {
					mes = 12;
					ano = ano - 1;
				} else
					mes = mes - 1;
			}

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());
			mandadoFaixaValorPs = new MandadoFaixaValorPs(obFabricaConexao.getConexao());
			listaOficiaisProj = relatoriosMandadoPs.relatorioFinanceiroMandadoGratuito(arrayAnoMes, tipoArquivo,
					idUsuario, idServentia);
			//
			// inicio spg - so enquanto existir dados no spg
			//
			// adiciona na lista, oficiais importados do spg.
			//
			//
			// busca comarca e cpf usuario , para limitar pesquisa, de acordo com a
			// serventia sessão e usuario sessão.
			//

			String codigoComarca = "";
			String cpfUsuario = "";
			String nomeComarca = "";

			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
				cpfUsuario = "";
				nomeComarca = "SENADOR CANEDO";  			 		
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
					nomeComarca = "";
					UsuarioNe usuarioNe = new UsuarioNe();
					UsuarioDt usuarioDt = null;
					usuarioDt = usuarioNe.consultarId(idUsuarioSessao);
					cpfUsuario = usuarioDt.getCpf();

				} else {
					if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
						cpfUsuario = "";
						ServentiaNe serventiaNe = new ServentiaNe();
						ServentiaDt serventiaDt = null;
						serventiaDt = serventiaNe.consultarId(idServentiaSessao);
						Funcoes funcao = new Funcoes();
						codigoComarca = funcao.completarZeros(serventiaDt.getServentiaCodigoExterno(), 6);
						codigoComarca = codigoComarca.substring(0, 3);
						ComarcaNe comarcaNe = new ComarcaNe();
						nomeComarca = comarcaNe.consultarCodigo(codigoComarca);

					} else {
						return null;
					}
				}
			}

			ImportaDadosSPGNe importaDadosSPGNe = new ImportaDadosSPGNe();
			listaOficiais = importaDadosSPGNe.mapaFinanceiroMandadoGratuitoSpg(listaOficiaisProj, arrayAnoMes[0],
					nomeComarca, cpfUsuario);

			////////////////////////////////////////////// fim spg

			//this.listaFaixaValor = mandadoFaixaValorPs.consultaMandadoFaixaValorPorData(dataReferencia);

			//if (listaOficiais.isEmpty() || (this.listaFaixaValor.isEmpty()
				//	|| this.listaFaixaValor.size() < MandadoFaixaValorDt.QTDE_MAND_FAIXA_VALOR))
				//return null;
			
			MandadoFaixaValorDt obDt = new MandadoFaixaValorDt();			
			obDt.setFaixaInicio("1");
			obDt.setFaixaFim("100");
			obDt.setValor("2860.45");
			this.listaFaixaValor.add(obDt);
			
			obDt = new MandadoFaixaValorDt();
			obDt.setFaixaInicio("101");
			obDt.setFaixaFim("250");
			obDt.setValor("4289.84");
			this.listaFaixaValor.add(obDt);
			
			obDt = new MandadoFaixaValorDt();
			obDt.setFaixaInicio("251");
			obDt.setFaixaFim("300");
			obDt.setValor("5727.76");
			this.listaFaixaValor.add(obDt);
			
			obDt = new MandadoFaixaValorDt();
			obDt.setFaixaInicio("301");
			obDt.setFaixaFim("999");
			obDt.setValor("7150.32");
			this.listaFaixaValor.add(obDt);				
			
		
			//////////////////////////////////////////
			
			//
			// calcula sempre a media a partir do mes de competencia.(mes da data informada
			// pelo usuario).
			//
			// para o calculo, usa a media de seis meses.
			//
			// se um mes estiver em branco procura o proximo mes anterior para o
			// calculo
			//
			// se dois meses subsequentes estiverem em branco, calcula so
			// pela media dos meses trabalhados anteriormente.
			//
			// regras para tipos de oficias(normal, plantao e cenops) são
			// tratadas nos proprios metodos.
			//
			objDt = new RelatoriosMandadoDt();
			for (int i = 0; i < listaOficiais.size(); i++) {
				objDt = (listaOficiais.get(i));
				switch (objDt.getEscalaTipo()) {
				case "Plantão":
					pagamentoPlantao(objDt);
					break;
				case "Cenops":
					pagamentoCenops(objDt);
					break;
				case "Normal":
				   pagamentoNormal(objDt);
					break;
				default:
					break;
				}
			}
			//
			// comparar listaOficiais a receber com a lista de oficiais da escalaMg cenops.
			// se nao aparecer na lista a receber, incluir na lista a receber com 1 mandado
			// para garantir a faixa 1 para ele. (cenops, mesmo sem ter nenhum mandado
			// distribuido, recebe pela faixa 1.
			//
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO)))
				this.comparaListaOficiaisCenops(tipoArquivo);

			if (tipoArquivo == 2) { // gera texto banco. quando implantar saja gera por ele acho
				int seqTexto = relatoriosMandadoPs.buscaSequenciaTextoBanco();

				GeraTextoBancoNe geraTextoBancoNe = new GeraTextoBancoNe();

				StringBuffer sbTextoBanco = geraTextoBancoNe.textoMandadoGratuito(this.listaOficiaisReceber, seqTexto);

				temp = sbTextoBanco.toString().getBytes();

				relatoriosMandadoPs = new RelatoriosMandadoPs(obFabricaConexao.getConexao());
				LogPs obLogPs = new LogPs(obFabricaConexao.getConexao());

				LogDt obLogDt = new LogDt();
				obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.TextoBancoMandadoGratuito));
				obLogDt.setId_Usuario(idUsuarioSessao);
				obLogDt.setIpComputador("");
				obLogDt.setData(null);
				obLogDt.setHora(null);
				obLogDt.setTabela("");
				obLogDt.setValorAtual(sbTextoBanco.toString());
				obLogDt.setValorNovo("Competencia..: " + arrayAnoMes[0] + "  Sequencia Texto.. " + seqTexto
						+ "  Data Referencia.. " + dataReferencia);
				obLogDt.setCodigoTemp("");
				obLogDt.setId_Tabela("");
				obLogDt.setHash("");
				obLogDt.setQtdErrosDia(0);
				obLogPs.inserir(obLogDt);

			} else {

				ordenaListaPorComarcaUsuario();

				ano = Integer.parseInt(dataReferencia.substring(6));
				mes = Integer.parseInt(dataReferencia.substring(3, 5));

				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("titulo", "Média de Mandados Recebidos");
				parametros.put("competencia", Funcoes.identificarNomeMes(mes) + "/" + ano);
				parametros.put("dataAtual", new Date());
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);
				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros,
						this.listaOficiaisReceber);
			}
		} finally

		{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}

	public void pagamentoNormal(RelatoriosMandadoDt objDt) {
		//
		// se nao tiver mandados no primeiro mes nao paga nada
		//
		if (objDt.getQuantMandadoMesAnt1() != 0) {

			calculaMediaMandado(objDt);

			objDt.setSomatorioMandados(this.somatorioMandados);
			objDt.setMesesComMandado(this.mesesComMandado);
			objDt.setMediaMandado(this.mediaMandados);

			//
			// calcula valor pela media/faixa valores
			//
			// PEGANDO FAIXA PELA ORDEM DE IDS. CRIEI A DESCRICAO FAIXA 1 NO REGISTRO. VER
			// SE MUDA

			if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(0).getFaixaFim()))) {
				objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(0).getValor()));
				objDt.setFaixa("Faixa 1");
			} else {
				if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(1).getFaixaFim()))) {
					objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(1).getValor()));
					objDt.setFaixa("Faixa 2");
				} else {
					if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(2).getFaixaFim()))) {
						objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(2).getValor()));
						objDt.setFaixa("Faixa 3");
					} else {
						objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(3).getValor()));
						objDt.setFaixa("Faixa 4");
					}
				}
			}
			objDt.setCpfUsuario(Funcoes.formataCPF(objDt.getCpfUsuario()));
			this.listaOficiaisReceber.add(objDt);
		}
	}

	public void pagamentoPlantao(RelatoriosMandadoDt objDt) {
		//
		// se nao tiver mandados no primeiro mes nao paga nada
		//
		// recebe sempre a partir do grupo 2
		//
		if (objDt.getQuantMandadoMesAnt1() != 0) {

			calculaMediaMandado(objDt);

			objDt.setSomatorioMandados(this.somatorioMandados);
			objDt.setMesesComMandado(this.mesesComMandado);
			objDt.setMediaMandado(this.mediaMandados);

			//
			// calcula valor pela media/faixa valores
			//

			if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(0).getFaixaFim()))) {
				objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(1).getValor()));
				objDt.setFaixa("Faixa 2");
			} else {
				if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(1).getFaixaFim()))) {
					objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(1).getValor()));
					objDt.setFaixa("Faixa 2");
				} else {
					if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(2).getFaixaFim()))) {
						objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(2).getValor()));
						objDt.setFaixa("Faixa 3");
					} else {
						objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(3).getValor()));
						objDt.setFaixa("Faixa 4");
					}
				}
			}
			objDt.setCpfUsuario(Funcoes.formataCPF(objDt.getCpfUsuario()));
			this.listaOficiaisReceber.add(objDt);
		}
	}

	public void pagamentoCenops(RelatoriosMandadoDt objDt) {
		//
		// ver como fica se nao trabalhou nos ultimos 7 meses. - ne nao estiver na lista
		// tem que pagar sim.
		//
		// se nao tiver mandados no primeiro mes recebe pela faixa 1
		//
		// se tiver calcula pela media.
		//

		if (objDt.getQuantMandadoMesAnt1() == 0) {
			objDt.setSomatorioMandados(0);
			objDt.setMesesComMandado(0);
			objDt.setMediaMandado(0);
			objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(0).getValor()));
			objDt.setFaixa("Faixa 1");

		} else {

			calculaMediaMandado(objDt);

			objDt.setSomatorioMandados(this.somatorioMandados);
			objDt.setMesesComMandado(this.mesesComMandado);
			objDt.setMediaMandado(this.mediaMandados);

			//
			// calcula valor pela media/faixa valores
			//

			if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(0).getFaixaFim()))) {
				objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(0).getValor()));
				objDt.setFaixa("Faixa 1");
			} else {
				if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(1).getFaixaFim()))) {
					objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(1).getValor()));
					objDt.setFaixa("Faixa 2");
				} else {
					if (this.mediaMandados <= (Integer.parseInt(this.listaFaixaValor.get(2).getFaixaFim()))) {
						objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(2).getValor()));
						objDt.setFaixa("Faixa 3");
					} else {
						objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(3).getValor()));
						objDt.setFaixa("Faixa 4");
					}
				}
			}
		}
		objDt.setCpfUsuario(Funcoes.formataCPF(objDt.getCpfUsuario()));
		this.listaOficiaisReceber.add(objDt);
	}

	public void calculaMediaMandado(RelatoriosMandadoDt objDt) {
		//
		// carrega um array com o total mandados dos meses.
		//
		int[] arrayMandados = { objDt.getQuantMandadoMesAnt1(), objDt.getQuantMandadoMesAnt2(),
				objDt.getQuantMandadoMesAnt3(), objDt.getQuantMandadoMesAnt4(), objDt.getQuantMandadoMesAnt5(),
				objDt.getQuantMandadoMesAnt6(), objDt.getQuantMandadoMesAnt7(), };

		int mesesSemMandado = 0;
		this.mesesComMandado = 0;
		this.somatorioMandados = 0;
		this.mediaMandados = 0;
		int k = 0;
		int arrayMesesComMandado[] = new int[6];

		for (int j = 0; j <= 6; j++) {
			if (arrayMandados[j] == 0) {
				mesesSemMandado++;
				//
				// se encontrar dois meses sem mandado, para processamento.
				//
				if (mesesSemMandado == 2)
					break;
			} else {
				if (this.mesesComMandado < 6) {
					arrayMesesComMandado[k] = arrayMandados[j];
					this.mesesComMandado++;
					this.somatorioMandados = this.somatorioMandados + arrayMandados[j];
					k++;
				}
			}
		}
		double soma = this.somatorioMandados;
		this.mediaMandados = (int) Math.round(soma / this.mesesComMandado);
	}
	public void comparaListaOficiaisCenops(int tipoArquivo) throws Exception {

		EscalaMgNe escalaMgNe = new EscalaMgNe();
		EscalaMgDt escalaMgDt = null;

		RelatoriosMandadoDt objDt = new RelatoriosMandadoDt();

		List<EscalaMgDt> listaOficiaisCenops = escalaMgNe.listaCenopsParaMandadoGratuito();

		boolean acheiNaListaAReceber = false;

		for (int j = 0; j < listaOficiaisCenops.size(); j++) {

			for (int k = 0; k < this.listaOficiaisReceber.size(); k++) {

				acheiNaListaAReceber = false;

				if (Funcoes.formataCPF(listaOficiaisCenops.get(j).getCpfUsuario())
						.equalsIgnoreCase(this.listaOficiaisReceber.get(k).getCpfUsuario())) {
					//
					// se o elemento da lista cenops existir na lista nao faz nada
					// porque ele ja garantiu a primeira faixa
					// se nao existir adiciona na lista
					//

					acheiNaListaAReceber = true;

					break;
				}
			}
			if (!acheiNaListaAReceber) {

				//
				// adiciona elemento cenops inexistente na lista a receber.
				// sem conta bancaria somente no relatorio.
				// no texto para o banco nao pode adicionar sem conta bancaria
				//

				if (objDt.getInfoConta() != null) {
					objDt = new RelatoriosMandadoDt();
					objDt.setIdUsuario(Integer.parseInt(listaOficiaisCenops.get(j).getIdUsuario()));
					objDt.setCpfUsuario(Funcoes.formataCPF(listaOficiaisCenops.get(j).getCpfUsuario()));
					objDt.setNomeUsuario(listaOficiaisCenops.get(j).getUsuario().trim());
					objDt.setNomeComarca(Funcoes.RemoveAcentos(listaOficiaisCenops.get(j).getComarca().trim())); /////////////////////////////////////////////////// so
																													/////////////////////////////////////////////////// enquanto
																													/////////////////////////////////////////////////// tem
																													/////////////////////////////////////////////////// spg
																													/////////////////////////////////////////////////// na
																													/////////////////////////////////////////////////// parada
																													/////////////////////////////////////////////////// esse
																													/////////////////////////////////////////////////// remove
																													/////////////////////////////////////////////////// acentos
					objDt.setBanco(listaOficiaisCenops.get(j).getBanco());
					objDt.setAgencia(listaOficiaisCenops.get(j).getAgencia());
					objDt.setBanco(listaOficiaisCenops.get(j).getBanco());
					objDt.setContaOperacao(listaOficiaisCenops.get(j).getContaOperacao());
					objDt.setConta(listaOficiaisCenops.get(j).getConta());
					objDt.setContaDv(listaOficiaisCenops.get(j).getContaDv());
					objDt.setNomeBanco(listaOficiaisCenops.get(j).getNomeBanco());
					objDt.setSomatorioMandados(0);
					objDt.setMesesComMandado(0);
					objDt.setMediaMandado(0);
					objDt.setFaixa("Faixa 1");
					objDt.setEscalaTipo("Cenops");
					objDt.setValorReceber(Double.valueOf(this.listaFaixaValor.get(0).getValor()));
					if (objDt.getContaDv() == null)
						objDt.setContaDv("");
					listaOficiaisReceber.add(objDt);
				} else {
					if (tipoArquivo == 1) // sair sem conta bancaria so no relatorio. no texto para o banco não pode
						listaOficiaisReceber.add(objDt);
				}
			}
		}

	}
	
	//  fim tirar
}
