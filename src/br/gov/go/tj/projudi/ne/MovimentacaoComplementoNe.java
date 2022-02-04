package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoComplementoDt;
import br.gov.go.tj.projudi.ps.MovimentacaoComplementoPs;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MovimentacaoComplementoNe extends MovimentacaoComplementoNeGen {

	private static final long serialVersionUID = 4322091844287086651L;
	
	private String origem = "";
	private AudienciaDt audienciaDt = null;

	private static ArrayList<String> DISTRIBUICAO_26 = new ArrayList<String>(Arrays.asList("168", "405")); // 254
																											// não
																											// será
																											// mais
																											// utilizado
	private static ArrayList<String> REDISTRIBUICAO_36 = new ArrayList<String>(Arrays.asList("14", "255"));
	private static ArrayList<String> CONCLUSAO_51 = new ArrayList<String>(Arrays.asList("21", "44", "45"));

	private static ArrayList<String> EXPEDICAO_60 = new ArrayList<String>(Arrays.asList("5", "10", "27", "55", "90",
			"113", "130", "197", "208", "222", "257", "392", "395", "427", "657", "786", "677"));
	private static ArrayList<String> JUNTADA_PETICAO_85 = new ArrayList<String>(
			Arrays.asList("20", "24", "25", "28", "31", "49", "52", "65", "68", "70", "76", "81", "86", "93", "98",
					"139", "140", "141", "142", "143", "144", "145", "167", "190", "235", "260"));
	private static ArrayList<String> REMETIDOS_123 = new ArrayList<String>(Arrays.asList("15", "233")); // 265
																										// não
																										// é
																										// utilizado
	private static ArrayList<String> JUNTADA_581 = new ArrayList<String>(
			Arrays.asList("12", "16", "17", "18", "60", "91", "120", "150", "152", "176", "177", "178", "212", "213",
					"216", "262", "339", "390", "391", "393", "394", "396", "397", "399", "400", "401", "402", "406",
					"414", "420", "660", "725", "667", "669", "789", "790"));
	private static ArrayList<String> SESSAO_873 = new ArrayList<String>(Arrays.asList("404")); // 353
																								// -
																								// não
																								// é
																								// possível
																								// recuperar
	private static ArrayList<String> AUDIENCIA_970 = new ArrayList<String>(Arrays.asList("35", "36", "37", "38", "39",
			"40", "196", "209", "215", "227", "245", "247", "248", "249", "250", "367", "398", "407", "408", "409",
			"410", "411", "412", "413",  "441", "442", "674", "679", "680", "681", "683", "684", "685", "724", "740", "741", "862",
			"939", "940", "941", "942", "943", "944", "945", "946", "947", "948", "949", "950", "951", "952", "956", "957", "960",
			"961", "985" ));

	public String Verificar(MovimentacaoComplementoDt dados) {

		String stRetorno = "";
		return stRetorno;

	}

	public void salvar(MovimentacaoComplementoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try {
			MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(),
						dados.getPropriedades());
			}
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch (Exception e) {
			dados.setId("");
			throw e;
		}
	}

	/**
	 * Consultar Complementos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao,
	 *            identificação da movimentação
	 * 
	 * @author acbloureiro
	 */
	public List consultarComplementosMovimentacao(String id_Movimentacao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarComplementosMovimentacao(id_Movimentacao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List consultarComplementosMovimentacao(String id_Movimentacao, String id_Complemento) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		try {

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarComplementosMovimentacao(id_Movimentacao, id_Complemento);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public void excluir(MovimentacaoComplementoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), dados.getId_UsuarioLog(),
				dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
		obPersistencia.excluir(dados.getId());
		obLog.salvar(obLogDt, obFabricaConexao);
	}

	public void geraMovimentoComplementoIndividual(MovimentacaoDt movDt, AudienciaDt audienciaDt, FabricaConexao obFabricaConexao) throws Exception {
		this.origem = "1";
		this.audienciaDt = audienciaDt;
		this.geraMovimentoComplemento(movDt, obFabricaConexao);
	}
	
	public void geraMovimentoComplementoIndividual(MovimentacaoDt movDt, FabricaConexao obFabricaConexao) throws Exception {
		this.origem = "1";		
		this.geraMovimentoComplemento(movDt, obFabricaConexao);
	}
	
	// Método recebe cada movimentacao, verificar todas as regras de complemento
	// e gerar o complemento.

	public void geraMovimentoComplemento(MovimentacaoDt movDt, FabricaConexao obFabricaConexao) throws Exception {

		if (DISTRIBUICAO_26.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoDistruibuicao(movDt, obFabricaConexao);

		else if (REDISTRIBUICAO_36.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoRedistruibuicao(movDt, obFabricaConexao);

		else if (CONCLUSAO_51.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoConclusao(movDt, obFabricaConexao);

		else if (EXPEDICAO_60.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoExpedicao(movDt, obFabricaConexao);

		else if (JUNTADA_PETICAO_85.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoJuntadaPeticao(movDt, obFabricaConexao);

		else if (REMETIDOS_123.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoRemetidos(movDt, obFabricaConexao);

		else if (JUNTADA_581.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoJuntada(movDt, obFabricaConexao);

		else if (SESSAO_873.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoSessao(movDt, obFabricaConexao);

		else if (AUDIENCIA_970.contains(movDt.getMovimentacaoTipoCodigo()))
			trataComplementoMovimentacaoAudiencia(movDt, obFabricaConexao);
	}

	public void trataComplementoMovimentacaoDistruibuicao(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("2");

		if (movDt.getMovimentacaoTipoCodigo().equals("168") || movDt.getMovimentacaoTipoCodigo().equals("405")) {

			// verificar se não existe o complemento cadastrado
			if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

				if (movDt.getComplemento().indexOf("Normal") >= 0)
					dados.setId_ComplementoTabelado("2"); // sorteio

				else if (movDt.getComplemento().indexOf("Dependen") >= 0)
					dados.setId_ComplementoTabelado("4"); // dependencia

				else if (movDt.getComplemento().indexOf("retorno") >= 0)
					dados.setId_ComplementoTabelado("3"); // prevenção

				else
					dados.setId_ComplementoTabelado("2"); // sorteio

				try {
					dados.setId_Movimentacao(movDt.getId());
					dados.setOrigem(this.origem);
					obPersistencia.inserir(dados);

					obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
							String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
					obLog.salvar(obLogDt, obFabricaConexao);

				} catch (Exception e) {
					dados.setId("");
					throw e;
				}
			}
		}
	}

	public void trataComplementoMovimentacaoRedistruibuicao(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("2");

		if (movDt.getMovimentacaoTipoCodigo().equals("14") || movDt.getMovimentacaoTipoCodigo().equals("255")) {

			// verificar se não existe o complemento cadastrado
			if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

				if (movDt.getComplemento().indexOf("Normal") >= 0)
					dados.setId_ComplementoTabelado("2"); // sorteio

				else if (movDt.getComplemento().indexOf("Direcionada Magistrado") >= 0)
					dados.setId_ComplementoTabelado("3"); // prevenção

				else if (movDt.getComplemento().indexOf("Direcionada Serventia") >= 0)
					dados.setId_ComplementoTabelado("3"); // prevenção

				else
					dados.setId_ComplementoTabelado("2"); // sorteio

				try {
					dados.setId_Movimentacao(movDt.getId());
					dados.setOrigem(this.origem);
					obPersistencia.inserir(dados);

					obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
							String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
					obLog.salvar(obLogDt, obFabricaConexao);

				} catch (Exception e) {
					dados.setId("");
					throw e;
				}
			}
		}
	}

	public void trataComplementoMovimentacaoConclusao(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("3");

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if (movDt.getMovimentacaoTipoCodigo().equals("44"))
				dados.setId_ComplementoTabelado("6"); // decisão

			else if (movDt.getMovimentacaoTipoCodigo().equals("45"))
				dados.setId_ComplementoTabelado("36"); // julgamento

			else if (movDt.getMovimentacaoTipoCodigo().equals("21")) {

				if ((movDt.getComplemento().indexOf("sentenca") >= 0)
						|| (movDt.getComplemento().indexOf("relator") >= 0)
						|| (movDt.getComplemento().indexOf("presidente") >= 0))

					dados.setId_ComplementoTabelado("36"); // julgamento

				else if ((movDt.getComplemento().indexOf("decisao") >= 0)
						|| (movDt.getComplemento().indexOf("beneficio de assistência") >= 0))

					dados.setId_ComplementoTabelado("6"); // decisão

				else
					dados.setId_ComplementoTabelado("5"); // despacho

			}

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
	}

	public void trataComplementoMovimentacaoExpedicao(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("4");

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if (movDt.getMovimentacaoTipoCodigo().equals("10"))
				dados.setId_ComplementoTabelado("116"); // informações
														// solicitadas

			else if (movDt.getMovimentacaoTipoCodigo().equals("27") || movDt.getMovimentacaoTipoCodigo().equals("786"))
				dados.setId_ComplementoTabelado("73"); // alvará

			else if (movDt.getMovimentacaoTipoCodigo().equals("55") || movDt.getMovimentacaoTipoCodigo().equals("392"))
				dados.setId_ComplementoTabelado("76"); // Carta precatória

			else if (movDt.getMovimentacaoTipoCodigo().equals("90") || movDt.getMovimentacaoTipoCodigo().equals("197")
					|| movDt.getMovimentacaoTipoCodigo().equals("657"))
				dados.setId_ComplementoTabelado("78"); // Mandado

			else if (movDt.getMovimentacaoTipoCodigo().equals("113"))
				dados.setId_ComplementoTabelado("79"); // Ofício

			else if (movDt.getMovimentacaoTipoCodigo().equals("130"))
				dados.setId_ComplementoTabelado("170"); // Carta

			else if (movDt.getMovimentacaoTipoCodigo().equals("222") || movDt.getMovimentacaoTipoCodigo().equals("427"))
				dados.setId_ComplementoTabelado("107"); // Certidão
			else
				dados.setId_ComplementoTabelado("80"); // outros documentos

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
	}

	public void trataComplementoMovimentacaoJuntadaPeticao(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("19");

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if (movDt.getMovimentacaoTipoCodigo().equals("235"))
				dados.setId_ComplementoTabelado("52"); // cumprimento de
														// sentença

			else if (movDt.getMovimentacaoTipoCodigo().equals("167"))
				dados.setId_ComplementoTabelado("58"); // petição inicial

			else if (movDt.getMovimentacaoTipoCodigo().equals("24") || movDt.getMovimentacaoTipoCodigo().equals("25")
					|| movDt.getMovimentacaoTipoCodigo().equals("145"))
				dados.setId_ComplementoTabelado("111"); // renuncia de
														// mandado

			else if (movDt.getMovimentacaoTipoCodigo().equals("65"))
				dados.setId_ComplementoTabelado("45"); // contestacao

			else if (movDt.getMovimentacaoTipoCodigo().equals("98"))
				dados.setId_ComplementoTabelado("69"); // recurso ordinário

			else if (movDt.getMovimentacaoTipoCodigo().equals("139"))
				dados.setId_ComplementoTabelado("180"); // embargos a
														// execução
			else
				dados.setId_ComplementoTabelado("57"); // petição outras

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
	}

	public void trataComplementoMovimentacaoJuntada(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("4");

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if (movDt.getMovimentacaoTipoCodigo().equals("17") || movDt.getMovimentacaoTipoCodigo().equals("18")
					|| movDt.getMovimentacaoTipoCodigo().equals("91") || movDt.getMovimentacaoTipoCodigo().equals("150")
					|| movDt.getMovimentacaoTipoCodigo().equals("660"))

				dados.setId_ComplementoTabelado("78"); // mandado
			else if (movDt.getMovimentacaoTipoCodigo().equals("152") || movDt.getMovimentacaoTipoCodigo().equals("216")
					|| movDt.getMovimentacaoTipoCodigo().equals("262") || movDt.getMovimentacaoTipoCodigo().equals("399")
					|| movDt.getMovimentacaoTipoCodigo().equals("401") || movDt.getMovimentacaoTipoCodigo().equals("402"))

				dados.setId_ComplementoTabelado("79"); // oficio

			else if (movDt.getMovimentacaoTipoCodigo().equals("120"))
				dados.setId_ComplementoTabelado("74"); // AR

			else if (movDt.getMovimentacaoTipoCodigo().equals("390") || movDt.getMovimentacaoTipoCodigo().equals("391")
					|| movDt.getMovimentacaoTipoCodigo().equals("789") || movDt.getMovimentacaoTipoCodigo().equals("790"))
				dados.setId_ComplementoTabelado("73"); // alvará

			else
				dados.setId_ComplementoTabelado("80"); // outros documentos

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
	}

	public void trataComplementoMovimentacaoRemetidos(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("18");

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if (movDt.getMovimentacaoTipoCodigo().equals("233"))
				dados.setId_ComplementoTabelado("40"); // outros motivos

			else if (movDt.getMovimentacaoTipoCodigo().equals("15"))
				dados.setId_ComplementoTabelado("37"); // em diligência

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}

		dados = new MovimentacaoComplementoDt();
		dados.setId_Complemento("7");

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if (movDt.getMovimentacaoTipoCodigo().equals("233"))
				dados.setValorIdentificador("Contadoria");

			else if (movDt.getMovimentacaoTipoCodigo().equals("15"))
				dados.setValorIdentificador("Delegacia");

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
	}

	public void trataComplementoMovimentacaoSessao(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("23");

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if (movDt.getMovimentacaoTipoCodigo().equals("404"))
				dados.setId_ComplementoTabelado("99"); // pedido de vista

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
	}

	public void trataComplementoMovimentacaoAudiencia(MovimentacaoDt movDt, FabricaConexao obFabricaConexao)
			throws Exception {

		LogDt obLogDt;
		MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
		MovimentacaoComplementoDt dados = new MovimentacaoComplementoDt();

		dados.setId_Complemento("15"); // situacao da audiencia

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if ((movDt.getMovimentacaoTipo().indexOf("Marcada") >= 0) || (movDt.getComplemento().indexOf("Marcada") >= 0) || (movDt.getComplemento().indexOf("Agendada") >= 0)) 
				dados.setId_ComplementoTabelado("9"); // designada

			else if ((movDt.getMovimentacaoTipo().indexOf("Não Realizada") >= 0) || (movDt.getComplemento().indexOf("Não Realizada") >= 0))
				dados.setId_ComplementoTabelado("14"); // não realizada
		
			else if ((movDt.getMovimentacaoTipo().indexOf("Realizada") >= 0) || (movDt.getComplemento().indexOf("Realizada") >= 0))
				dados.setId_ComplementoTabelado("13"); // realizada

			else if ((movDt.getMovimentacaoTipo().indexOf("Cancelada") >= 0) || (movDt.getComplemento().indexOf("Cancelada") >= 0) || (movDt.getMovimentacaoTipo().indexOf("Desmarcada") >= 0) || (movDt.getComplemento().indexOf("Desmarcada") >= 0))
				dados.setId_ComplementoTabelado("11"); // cancelada
				
			else if ((movDt.getMovimentacaoTipo().indexOf("Negativa") >= 0) || (movDt.getComplemento().indexOf("Negativa") >= 0))
				dados.setId_ComplementoTabelado("14"); // não realizada

			else if ((movDt.getMovimentacaoTipo().indexOf("Redesignada") >= 0) || (movDt.getComplemento().indexOf("Redesignada") >= 0) || (movDt.getMovimentacaoTipo().indexOf("Remarcada") >= 0) || (movDt.getComplemento().indexOf("Remarcada") >= 0))
				dados.setId_ComplementoTabelado("10"); // redesignada
			
			else
				dados.setId_ComplementoTabelado("0");
			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}

		dados = new MovimentacaoComplementoDt();
		dados.setId_Complemento("16"); // tipo de audiencia

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {

			if ((movDt.getMovimentacaoTipo().indexOf("Instrução e Julgamento") >= 0) ||	(movDt.getComplemento().indexOf("Instrução e Julgamento") >= 0))
				dados.setId_ComplementoTabelado("23"); // inst e julgamento

			else if ((movDt.getMovimentacaoTipo().indexOf("Instrução") >= 0) || (movDt.getComplemento().indexOf("Instrução") >= 0))
				dados.setId_ComplementoTabelado("22"); // instrução

			else if ((movDt.getMovimentacaoTipo().indexOf("Conciliação") >= 0) ||(movDt.getComplemento().indexOf("Conciliação") >= 0))
				dados.setId_ComplementoTabelado("17"); // conciliação
			
			else if ((movDt.getMovimentacaoTipo().indexOf("Preliminar") >= 0) || (movDt.getComplemento().indexOf("Preliminar") >= 0))
				dados.setId_ComplementoTabelado("25"); // preliminar

			else if ((movDt.getMovimentacaoTipo().indexOf("Admonitória") >= 0) || (movDt.getComplemento().indexOf("Admonitória") >= 0))
				dados.setId_ComplementoTabelado("16"); // admonitoria

			else if ((movDt.getMovimentacaoTipo().indexOf("Justificação") >= 0) || (movDt.getComplemento().indexOf("Justificação") >= 0))
				dados.setId_ComplementoTabelado("19");

			else if ((movDt.getMovimentacaoTipo().indexOf("Julgamento") >= 0) || (movDt.getComplemento().indexOf("Julgamento") >= 0))
				dados.setId_ComplementoTabelado("24");

			else if ((movDt.getMovimentacaoTipo().indexOf("Mediação") >= 0) || (movDt.getComplemento().indexOf("Mediação") >= 0))
				dados.setId_ComplementoTabelado("92");

			else if ((movDt.getMovimentacaoTipo().indexOf("Inicial") >= 0) || (movDt.getComplemento().indexOf("Inicial") >= 0))
				dados.setId_ComplementoTabelado("21");

			else if ((movDt.getMovimentacaoTipo().indexOf("Execução") >= 0) || (movDt.getComplemento().indexOf("Execução") >= 0))
				dados.setId_ComplementoTabelado("20");

			else if ((movDt.getMovimentacaoTipo().indexOf("Interrogatório") >= 0) || (movDt.getComplemento().indexOf("Interrogatório") >= 0))
				dados.setId_ComplementoTabelado("18");
			
			else if ((movDt.getMovimentacaoTipo().indexOf("Inquirição") >= 0) || (movDt.getComplemento().indexOf("Inquirição") >= 0))
				dados.setId_ComplementoTabelado("18");
			
			else if ((movDt.getMovimentacaoTipo().indexOf("Custódia") >= 0) || (movDt.getComplemento().indexOf("Custódia") >= 0))
				dados.setId_ComplementoTabelado("193");

			else
				dados.setId_ComplementoTabelado("0");
			
			if (!dados.getId_ComplementoTabelado().equals("0")) {
				try {
					dados.setId_Movimentacao(movDt.getId());
					dados.setOrigem(this.origem);
					obPersistencia.inserir(dados);

					obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
							String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
					obLog.salvar(obLogDt, obFabricaConexao);

				} catch (Exception e) {
					dados.setId("");
					throw e;
				}
			}
		}

		dados = new MovimentacaoComplementoDt();
		dados.setId_Complemento("36"); // dirigida por

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {
							
			if ((movDt.getMovimentacaoTipo().indexOf("Conciliação") >= 0) || (movDt.getComplemento().indexOf("Conciliação") >= 0))
				dados.setId_ComplementoTabelado("188"); // conciliação

			else if ((movDt.getMovimentacaoTipo().indexOf("Mediação") >= 0) || (movDt.getComplemento().indexOf("Mediação") >= 0))
				dados.setId_ComplementoTabelado("187");

			else
				dados.setId_ComplementoTabelado("185");
			
			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
		
		dados = new MovimentacaoComplementoDt();
		dados.setId_Complemento("12"); // data_hora

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {
		
			// se não encontrar dados da data da audiência, grava a data da movimentação
			dados.setValorIdentificador(movDt.getDataRealizacao());
			
			if (movDt.getComplemento() != null) {
				int primeira_barra = movDt.getComplemento().indexOf("/");
				int ultima_barra = movDt.getComplemento().lastIndexOf("/");
				//se tem o string de data no complemento
				if (ultima_barra - primeira_barra == 3) {
				
					int inicio_data = primeira_barra - 2;
					int fim_data = inicio_data + 16; // total de caracteres para a data e hora
					try {
						dados.setValorIdentificador(movDt.getComplemento().substring(inicio_data, fim_data));
					}
					catch (Exception e) {
						dados.setValorIdentificador(movDt.getDataRealizacao());
					}
				}
			}

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
		
		dados = new MovimentacaoComplementoDt();
		dados.setId_Complemento("21"); // local

		// verificar se não existe o complemento cadastrado
		if (consultarComplementosMovimentacao(movDt.getId(), dados.getId_Complemento()) == null) {
			
			String local = "";
			if (this.audienciaDt != null) {
				local = this.audienciaDt.getServentia();
			}
			else {
				UsuarioServentiaNe usuServNe = new UsuarioServentiaNe();
				UsuarioServentiaDt usuServDt = usuServNe.buscaUsuarioServentiaIdUsuServ(movDt.getId_UsuarioRealizador());
				local = usuServDt.getServentia();
			}
			
			dados.setValorIdentificador(local);
			dados.setId_ComplementoTabelado("");
			dados.setValorTexto("");

			try {
				dados.setId_Movimentacao(movDt.getId());
				dados.setOrigem(this.origem);
				obPersistencia.inserir(dados);

				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), "1", movDt.getIpComputadorLog(),
						String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);

			} catch (Exception e) {
				dados.setId("");
				throw e;
			}
		}
	}
}
