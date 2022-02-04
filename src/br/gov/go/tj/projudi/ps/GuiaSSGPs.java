package br.gov.go.tj.projudi.ps;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.UfrValorDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UfrValorNe;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ne.boletos.PagadorBoleto;
import br.gov.go.tj.projudi.ne.boletos.SituacaoBoleto;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class GuiaSSGPs extends Persistencia {
	
	private static final long serialVersionUID = -2001009650375188537L;
	
	public static final String DESENV_GATEWAY 		= "desenv.gyn.tjgo"; //10.0.11.40
	public static final String DESENV_DATADSN 		= "SSGU_GUIAS";
	public static final String DESENV_PORT 			= "7500";
	public static final String DESENV_USER 			= "cxadasql";
	public static final String DESENV_PASSWD 		= "123456"; //"cxadasql"; //123456
	
	public static final String PRODUCAO_GATEWAY 	= "prod.gyn.tjgo"; //10.0.10.40
	public static final String PRODUCAO_DATADSN 	= "SSGU_GUIAS";
	public static final String PRODUCAO_PORT 		= "7500";
	public static final String PRODUCAO_USER 		= "cxadasql";
	public static final String PRODUCAO_PASSWD 		= "123456";
	
	public GuiaSSGPs(Connection conexao) {
		Conexao = conexao;
	}
	
	/**
	 * Método para inserir a itens de guia no SSG.
	 * Somente deve ser chamado de dentro de um método de inserir guia.
	 * @param String isn ou id da guia
	 * @param List<GuiaItemDt>
	 * @param GuiaEmissaoDt
	 * @throws Exception
	 */
	private void inserirGuiaItem(String isnGuia, List<GuiaItemDt> listaGuiaItemDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if( listaGuiaItemDt.size() > 0 ) {
			for(int i = 0; i < listaGuiaItemDt.size(); i++) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
				
				String sql = "INSERT INTO V_SSGUGUIAS_ITENS (ISN_SSGU_GUIAS, CODG_ITEM, VALR_ITEM ) VALUES (";
				
				sql += isnGuia + ",";
				sql += guiaItemDt.getCustaDt().getCodigoArrecadacao() + ",";
				
				Double valor = Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
				
				UfrValorNe ufrValorNe = new UfrValorNe();
				UfrValorDt ufrValorDt = ufrValorNe.consultarDataAtual();
				
				if (ufrValorDt == null) throw new MensagemException("UFR Valor não configurado para a data atual.");
				
				if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.TAXA_JUDICIARIA_PROCESSO)) ) {
					valor = valor / ufrValorDt.obtenhaValorUFRTaxaJudiciaria();
				}
				else {
					valor = valor / ufrValorDt.obtenhaValorUFR();
				}
				
				
				BigDecimal bd = new BigDecimal(valor);
				
				if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTA_GENERICA)) ) {
					if( guiaItemDt.getId_ArrecadacaoCustaGenerica() != null && guiaItemDt.getId_ArrecadacaoCustaGenerica().equals(String.valueOf(ArrecadacaoCustaDt.CODIGO_TAXA_JUDICIARIA)) ) {
						bd.setScale(6, BigDecimal.ROUND_UP);
						bd = bd.add(new BigDecimal("0.001")); // para não deixar dar diferença de 1 centavo no SAJ
					}
					else {
						if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.TAXA_JUDICIARIA_PROCESSO)) ) {
							bd = bd.setScale(4, BigDecimal.ROUND_UP);
						}
					}
				}
				else {
					if( !guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.TAXA_JUDICIARIA_PROCESSO)) ) {
						bd.setScale(6, BigDecimal.ROUND_UP);
						bd = bd.add(new BigDecimal("0.001")); // para não deixar dar diferença de 1 centavo no SAJ
					}
					else {
						if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.TAXA_JUDICIARIA_PROCESSO)) ) {
							bd = bd.setScale(4, BigDecimal.ROUND_UP);
						}
					}
				}
				sql += bd.toString().replace(",", "");
				
				
				sql += ")";
				
				executarComando(sql);
				
				String codg_oficial = null;
				Double valr_loc = bd.doubleValue();
				
				//*** Código do oficial
				if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA)) ) {
					codg_oficial = guiaEmissaoDt.getCodigoOficialSPGAvaliador();
				}
				else {
					if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.PREGAO_PRACAO_LEILAO)) ) {
						codg_oficial = guiaEmissaoDt.getCodigoOficialSPGLeilao();
					}
					else {
						if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) ) {
							codg_oficial = guiaEmissaoDt.getCodigoOficialSPGLocomocao();
						}
						else {
							if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL)) ) {
								codg_oficial = guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada();
							}
							if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTA_PENHORA)) ) {
								codg_oficial = guiaEmissaoDt.getCodigoOficialSPGPenhora();
							}
						}
					}
				}
				//*** Fim código oficial
				
				if( codg_oficial != null && codg_oficial.length() > 0 && valr_loc != null ) {
					sql = "INSERT INTO SSGU_GUIAS_INFO_LOC_FINAL (ISN_SSGU_GUIAS, CODG_OFICIAL, VALR_LOC) VALUES (" + isnGuia + "," + codg_oficial + "," + bd.toString().replace(",", "") + ")";

					executarComando(sql);
				}				
			}
		}		
	}
	
	/**
	 * Método para inserir a guia INICIAL no SSG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @throws Exception
	 */
	public void inserirGuiaInicial2Grau(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, FabricaConexao obFabricaConexaoPJD) throws Exception {
		
		TJDataHora tjDataHora = new TJDataHora();
		
		ResultSetTJGO rs1 = null;
		
		String isnGuia = null;
		
		String sql = "INSERT INTO V_SSGUGUIAS ( NUMR_GUIA, " +
											" TIPO_GUIA, " +
											" CODG_NATUREZA, " +
											" VALR_CAUSA, " +
											" DATA_EMISSAO,";
//										if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
//											sql += "NUMR_GUIA_REFERENCIA,";
//										}
//										if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
//											sql += "QTDE_PARCELAS_GUIA,";
//										}
//										if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
//											sql += "NUMR_PARCELA_GUIA,";
//										}
										if( 
											(guiaEmissaoDt.getNumeroProcesso() != null && !guiaEmissaoDt.getNumeroProcesso().isEmpty())
											|| 
											(guiaEmissaoDt.getId_Processo() != null && !guiaEmissaoDt.getId_Processo().isEmpty())
										) {
											sql += "NUMR_PROJUDI,";
										}
										sql += ") VALUES ( ";
		
		sql = sql.toString().replace(",)", ")");
		
		//numr_guia
		sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
		
		//tipo_guia
		sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		
		//codg_natureza
		//sql += Funcoes.completarZeros(new ProcessoTipoNe().consultarId(guiaEmissaoDt.getId_ProcessoTipo()).getProcessoTipoCodigo(),3) + ",";
		sql += "0,";
		
		//valr_acao
		if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
			Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo(), obFabricaConexaoPJD).getValor()));
			sql += valor.toString().replace(",", "") + ",";
		}
		else {
			Double valor = Funcoes.StringToDouble(guiaEmissaoDt.getValorAcao().replace(".", "").replace(",", "."));
			sql += valor.toString().replace(",", "") + ",";
		}
		
		//data_emissao
		if( guiaEmissaoDt.getDataEmissao() != null && !guiaEmissaoDt.getDataEmissao().isEmpty() ) {
			tjDataHora.setData(Funcoes.Stringyyyy_MM_ddToDateTime(guiaEmissaoDt.getDataEmissao()));
		}
		sql += "'" + tjDataHora.getDataHoraFormatadayyyyMMdd() + "',";
		
//		//numero guia completo da guia de referencia para desconto/parcelamento
//		if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
//			sql += guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() + ",";
//		}
		
//		//quantidade total de parcelas
//		if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
//			sql += guiaEmissaoDt.getQuantidadeParcelas() + ",";
//		}
		
//		//parcela atual do parcelamento
//		if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
//			sql += guiaEmissaoDt.getParcelaAtual() + ",";
//		}
		
		//Número do processo projudi
		if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
			String numeroProcesso[] = new GuiaEmissaoNe().consultarNumeroCompletoDoProcesso(guiaEmissaoDt.getId_Processo(), obFabricaConexaoPJD).split("\\.");
			String numeroProcesso_0 = "";
			String numeroProcesso_1 = "";
			String numeroProcesso_2 = "";
			if( numeroProcesso != null && numeroProcesso.length > 0 ) {
				for( int i = 0; i < numeroProcesso.length; i++ ) {
					if( i == 0 ) {
						numeroProcesso_0 = numeroProcesso[0];
					}
					if( i == 1 ) {
						numeroProcesso_1 = numeroProcesso[1];
					}
					if( i == 2 ) {
						numeroProcesso_2 = numeroProcesso[2];
					}
				}
			}
			sql += Funcoes.completarZeros(numeroProcesso_0 + numeroProcesso_1 + numeroProcesso_2, 13) + ",";
		}
		else {
			if( guiaEmissaoDt.getNumeroProcesso() != null && !guiaEmissaoDt.getNumeroProcesso().isEmpty() ) {
				String numrProcesso = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
				sql += numrProcesso + ",";
			}
		}
		
		sql += ")";
		
		sql = sql.toString().replace(",)", ")");
		
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			executarComando(sql);
			
			sql = "SELECT ISN_SSGU_GUIAS FROM V_SSGUGUIAS WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					isnGuia = rs1.getString("ISN_SSGU_GUIAS");
				}
				if( isnGuia == null ) {
					throw new MensagemException("Erro ao inserir a Guia Final no SSG(Erro no SQL de inserção).");
				}
			}
			else {
				throw new MensagemException("Erro ao inserir a Guia Final no SSG(Erro no SQL de inserção).");
			}
			
			//Inserir itens de guia
			this.inserirGuiaItem(isnGuia, listaGuiaItemDt, guiaEmissaoDt);
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) {
				rs1.close();
			}
		}
	}
	
	/**
	 * Método para inserir a guia INICIAL no SSG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @throws Exception
	 */
	public void inserirGuia2Grau(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, FabricaConexao obFabricaConexaoPJD) throws Exception {
		
		TJDataHora tjDataHora = new TJDataHora();
		
		ResultSetTJGO rs1 = null;
		
		String isnGuia = null;
		
		String sql = "INSERT INTO V_SSGUGUIAS ( NUMR_GUIA, " +
											" TIPO_GUIA, " +
											" CODG_NATUREZA, " +
											" VALR_CAUSA, " +
											" DATA_EMISSAO,";
//											if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
//												sql += "NUMR_GUIA_REFERENCIA,";
//											}
//											if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
//												sql += "QTDE_PARCELAS_GUIA,";
//											}
//											if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
//												sql += "NUMR_PARCELA_ATUAL,";
//											}
											if( 
												(guiaEmissaoDt.getNumeroProcesso() != null && !guiaEmissaoDt.getNumeroProcesso().isEmpty())
												|| 
												(guiaEmissaoDt.getId_Processo() != null && !guiaEmissaoDt.getId_Processo().isEmpty())
											) {
												sql += "NUMR_PROJUDI,";
											}
											sql += ") VALUES ( ";
											
		sql = sql.toString().replace(",)", ")");
		
		//numr_guia
		sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
		
		//tipo_guia
		sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		
		//codg_natureza
		//sql += Funcoes.completarZeros(new ProcessoTipoNe().consultarId(guiaEmissaoDt.getId_ProcessoTipo()).getProcessoTipoCodigo(),3) + ",";
		sql += "0,";
		
		//valr_acao
		if( guiaEmissaoDt.getId_Processo().length() > 0 ) {
			Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(new ProcessoNe().consultarId(guiaEmissaoDt.getId_Processo()).getValor()));
			sql += valor.toString().replace(",", "") + ",";
		}
		else {
			Double valor = Funcoes.StringToDouble(guiaEmissaoDt.getValorAcao().replace(".", "").replace(",", "."));
			sql += valor.toString().replace(",", "") + ",";
		}
		
		//data_emissao
		if( guiaEmissaoDt.getDataEmissao() != null && !guiaEmissaoDt.getDataEmissao().isEmpty() ) {
			tjDataHora.setData(Funcoes.Stringyyyy_MM_ddToDateTime(guiaEmissaoDt.getDataEmissao()));
		}
		sql += "'" + tjDataHora.getDataHoraFormatadayyyyMMdd() + "',";
		
//		//numero guia completo da guia de referencia para desconto/parcelamento
//		if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
//			sql += guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() + ",";
//		}
		
//		//quantidade total de parcelas
//		if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
//			sql += guiaEmissaoDt.getQuantidadeParcelas() + ",";
//		}
		
		//parcela atual do parcelamento
//		if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
//			sql += guiaEmissaoDt.getParcelaAtual() + ",";
//		}
		
		//Número do processo projudi
		if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
			String numeroProcesso[] = new GuiaEmissaoNe().consultarNumeroCompletoDoProcesso(guiaEmissaoDt.getId_Processo(), obFabricaConexaoPJD).split("\\.");
			String numeroProcesso_0 = "";
			String numeroProcesso_1 = "";
			String numeroProcesso_2 = "";
			if( numeroProcesso != null && numeroProcesso.length > 0 ) {
				for( int i = 0; i < numeroProcesso.length; i++ ) {
					if( i == 0 ) {
						numeroProcesso_0 = numeroProcesso[0];
					}
					if( i == 1 ) {
						numeroProcesso_1 = numeroProcesso[1];
					}
					if( i == 2 ) {
						numeroProcesso_2 = numeroProcesso[2];
					}
				}
			}
			sql += Funcoes.completarZeros(numeroProcesso_0 + numeroProcesso_1 + numeroProcesso_2, 13) + ",";
		}
		else {
			if( guiaEmissaoDt.getNumeroProcesso() != null && !guiaEmissaoDt.getNumeroProcesso().isEmpty() ) {
				String numrProcesso = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
				sql += numrProcesso + ",";
			}
		}
		
		sql += ")";
		
		sql = sql.toString().replace(",)", ")");
		
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			executarComando(sql);
			
			sql = "SELECT ISN_SSGU_GUIAS FROM V_SSGUGUIAS WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					isnGuia = rs1.getString("ISN_SSGU_GUIAS");
				}
				if( isnGuia == null ) {
					throw new MensagemException("Erro ao inserir a Guia Final no SSG(Erro no SQL de inserção)." );
				}
			}
			else {
				throw new MensagemException("Erro ao inserir a Guia Final no SSG(Erro no SQL de inserção)." );
			}
			
			//Inserir itens de guia
			this.inserirGuiaItem(isnGuia, listaGuiaItemDt, guiaEmissaoDt);
			
		}		
		finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();			
		}
	}
	
	/**
	 * Consulta as guias emitidas pelo SPG
	 * @param numeroCompletoGuia
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoSSG(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaSSGDt = null;
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SSGU_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_CAUSA, VALR_CAUSA_ATUAL, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROCESSO_PRINCIPAL";
			sql += " FROM V_SSGUGUIAS";
			sql += " WHERE NUMR_GUIA = " + numeroCompletoGuia;
									
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null && rs1.next()) {
				guiaSSGDt = new GuiaEmissaoDt();					
				this.associarGuiaEmissaoDtSSG(guiaSSGDt, rs1);
				this.consultarItensGuiaEmissaoSSG(guiaSSGDt.getId(), guiaSSGDt);
			}
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return guiaSSGDt;
	}
	
	private void associarGuiaEmissaoDtSSG(GuiaEmissaoDt guiaEmissaoDt, ResultSetTJGO rs1)  throws Exception {
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = null;
		String sql = null;
		
		guiaEmissaoDt.setGuiaEmitidaSPG(false);
		guiaEmissaoDt.setGuiaEmitidaSSG(true);
		
		guiaEmissaoDt.setId(rs1.getString("ISN_SSGU_GUIAS"));
		guiaEmissaoDt.setNumeroGuiaCompleto(rs1.getString("NUMR_GUIA"));
		guiaEmissaoDt.atualizeNumeroESeriePeloNumeroCompleto();
		
		TJDataHora dataEmissao = new TJDataHora(Funcoes.StringAAAAMMDDToDate(rs1.getString("DATA_EMISSAO")));
		guiaEmissaoDt.setDataEmissao(dataEmissao.getDataHoraFormatadaaaaa_MM_ddHHmmss());
		
		TJDataHora dataVencimento = new TJDataHora();
		dataVencimento.setData(dataEmissao.getAno() + 1, 1, 31, false);
		guiaEmissaoDt.setDataVencimento(dataVencimento.getDataHoraFormatadaaaaa_MM_ddHHmmss());
		
		int tipoGuia = Funcoes.StringToInt(rs1.getString("TIPO_GUIA"));
		guiaEmissaoDt.setId_GuiaTipo(String.valueOf(tipoGuia));
		guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SSG);
		if (tipoGuia == 1) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SSG_INICIAL);
		else if (tipoGuia == 3) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SSG_COMPLEMENTAR);
		
		GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
		guiaModeloDt.setId("");
		guiaModeloDt.setGuiaModelo("");
		guiaModeloDt.setId_GuiaTipo(rs1.getString("TIPO_GUIA"));
		guiaModeloDt.setGuiaTipo(guiaEmissaoDt.getGuiaTipo());
		guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
		
		if( rs1.getString("DATA_APRESENTACAO") != null && rs1.getString("DATA_APRESENTACAO").trim().length() > 0) {
			TJDataHora dataApresentacao = new TJDataHora(Funcoes.StringAAAAMMDDToDate(rs1.getString("DATA_APRESENTACAO")));
			guiaEmissaoDt.setDataApresentacaoSPG(dataApresentacao.getDataHoraFormatadaaaaa_MM_ddHHmmss());
		}
		
		int statPagamento = Funcoes.StringToInt(rs1.getString("STAT_PAGAMENTO"));
		
		if (statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_4 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_5 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_6 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_7 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_8 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_9) {
			
			guiaEmissaoDt.setId_GuiaStatus("10");
			guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.NOME_GUIA_SSG_PEDIDO_RESSARCIMENTO_SOLICITADO);
			
		} else {
			sql = "SELECT CODG_DOCUMENTO FROM V_SAJADOCUMENTOS WHERE CODG_DOCUMENTOS_SUPER = ?";
			ps = new PreparedStatementTJGO();
			ps.adicionarString(Funcoes.completarZeros(guiaEmissaoDt.getNumeroGuia(), 9) + Funcoes.completarZeros(guiaEmissaoDt.getNumeroGuiaSerie(), 3));
			
			try	{
				
				rs2 = consultar(sql, ps);
				if(rs2 != null && rs2.next()) {
					String codgDocumento = rs2.getString("CODG_DOCUMENTO");
					if (codgDocumento != null && codgDocumento.length() >= 8 && Funcoes.validaDataYYYYMMDD(codgDocumento.substring(0, 7))) {
						String dataAnoMesDia = codgDocumento.substring(0, 8);
						
						TJDataHora dataPagamento = new TJDataHora();
						dataPagamento.setDataaaaaMMdd(dataAnoMesDia);
						
						guiaEmissaoDt.setDataRecebimento(dataPagamento.getDataHoraFormatadaaaaa_MM_ddHHmmss());
					
						guiaEmissaoDt.setId_GuiaStatus("3");
						guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.PAGO_SSG);
					} else {
						guiaEmissaoDt.setId_GuiaStatus("1");
						guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO_SSG);
					}					
				} else {
					guiaEmissaoDt.setId_GuiaStatus("1");
					guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO_SSG);
				}
			} finally {
				//Fecha conexão e resultset
				if( rs2 != null ) rs2.close();
			}
		}	
		
		guiaEmissaoDt.setRequerente(rs1.getString("NOME_PRIMEIRO_AUTOR"));
		guiaEmissaoDt.setRequerido(rs1.getString("NOME_PRIMEIRO_REU"));
		guiaEmissaoDt.setNaturezaSPGCodigo(rs1.getString("CODG_NATUREZA"));
		guiaEmissaoDt.setCorreioQuantidade(rs1.getString("QTDE_CITACAO_CORREIO"));
		guiaEmissaoDt.setValorAcao(rs1.getString("VALR_CAUSA"));
		guiaEmissaoDt.setNumeroProcesso(rs1.getString("NUMR_PROJUDI"));
		guiaEmissaoDt.setNumeroProcessoSPG(rs1.getString("NUMR_PROCESSO"));
		guiaEmissaoDt.setId_GuiaEmissaoPrincipal(rs1.getString("NUMR_GUIA_PRINCIPAL"));
		guiaEmissaoDt.setSituacaoPagamentoSPG(rs1.getString("STAT_PAGAMENTO"));
		guiaEmissaoDt.setNumeroProcessoDependente(rs1.getString("NUMR_PROCESSO_PRINCIPAL"));		
	}
	
	private void consultarItensGuiaEmissaoSSG(String isnGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String sql1 = null;
		String sql2 = null;
		PreparedStatementTJGO ps1 = new PreparedStatementTJGO();
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
		guiaEmissaoDt.setListaGuiaItemDt(new ArrayList<GuiaItemDt>());
		
		UfrValorNe ufrValorNe = new UfrValorNe();
		UfrValorDt ufrValorDt = ufrValorNe.consultarData(Funcoes.Stringyyyy_MM_ddToDateTime(guiaEmissaoDt.getDataEmissao()));
//		UfrValorDt ufrValorDt = ufrValorNe.consultarData(Funcoes.Stringyyyy_MM_ddToDateTime(Funcoes.StringAAAAMMDDToDate(guiaEmissaoDt.getDataEmissao())));
		
		if (ufrValorDt == null) ufrValorDt = ufrValorNe.consultarDataAtual();		
		if (ufrValorDt == null) throw new MensagemException("UFR Valor não configurado para a data atual.");
		
		sql1 = "SELECT ISN_SSGU_GUIAS, CODG_ITEM, VALR_ITEM";
		sql1 += " FROM V_SSGUGUIAS_ITENS";
		sql1 += " WHERE ISN_SSGU_GUIAS = ? AND VALR_ITEM IS NOT NULL";
		ps1.adicionarString(isnGuia);
				
		sql2 = "SELECT NOME_GRUPO FROM V_SAJAGRUPOS WHERE CODG_GRUPO = ?";
		try 
		{
			rs1 = consultar(sql1, ps1);
			if(rs1 != null ) {
				while(rs1.next()) {
					GuiaItemDt guiaItemDt = new GuiaItemDt();					
					
					guiaItemDt.setId(rs1.getString("ISN_SSGU_GUIAS"));
					guiaItemDt.setCodigoArrecadacao(rs1.getString("CODG_ITEM"));
					if (Funcoes.StringToInt(guiaItemDt.getCodigoArrecadacao()) == Funcoes.StringToInt(CustaDt.REGIMENTO_TAXA_JUDICIARIA)) {
						guiaItemDt.setValorCalculado(ufrValorDt.obtenhaValorTaxaJudiciariaSPGEmReais(rs1.getString("VALR_ITEM")).toString());
					} else {
						guiaItemDt.setValorCalculado(ufrValorDt.obtenhaValorURFSPGEmReais(rs1.getString("VALR_ITEM")).toString());
					}	
					guiaItemDt.setValorReferencia(guiaItemDt.getValorCalculado());
					guiaItemDt.setQuantidade("1");
					guiaItemDt.setArrecadacaoCusta("TAXA SSG"); //BUSCAR DA VIEW SAJA-GRUPOS
					
					guiaEmissaoDt.getListaGuiaItemDt().add(guiaItemDt);		

					ps2.limpar();
					if (guiaItemDt.getCodigoArrecadacao() != null && guiaItemDt.getCodigoArrecadacao().trim().length() > 0) {
						ps2.adicionarString(guiaItemDt.getCodigoArrecadacao());
						rs2 = consultar(sql2, ps2);
						if(rs2 != null && rs2.next() && rs2.getString("NOME_GRUPO") != null && rs2.getString("NOME_GRUPO").trim().length() > 0) {
							guiaItemDt.setArrecadacaoCusta(rs2.getString("NOME_GRUPO"));
							rs2.close();
							rs2 = null;
						}	
					}					
				}
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
			if( rs2 != null) rs2.close();
		}
	}
	
	public BoletoDt buscaBoletoPorNumero(String numeroGuiaCompleto) throws Exception {
		BoletoDt boletoGuiaSSGDt = null;
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		//DATA_VENC_GUIA - VALR_ACAO -> VALR_CAUSA_ATUAL - INFO_LOCAL_CERTIDAO - TIPO_PESSOA
		try 
		{
			sql = "SELECT ISN_SSGU_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_CAUSA_ATUAL, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROCESSO_PRINCIPAL,";
			sql += " INFO_SITUACAO_BOLETO, NUMR_BOLETO, DATA_EMIS_BOLETO, VALR_BOLETO, DATA_VENCIMENTO_BOLETO,";
			sql += " VALR_CAUSA, URL_PDF_BOLETO, INFO_ID_PAGADOR_BOLETO, NOME_PAGADOR_BOLETO";
			sql += " FROM V_SSGUGUIAS";
			sql += " WHERE NUMR_GUIA = " + numeroGuiaCompleto;
									
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null && rs1.next()) {
				boletoGuiaSSGDt = new BoletoDt();		
				if (!associarDtBoleto(boletoGuiaSSGDt, rs1))
					return null;
				this.associarGuiaEmissaoDtSSG(boletoGuiaSSGDt, rs1);	
				this.consultarItensGuiaEmissaoSSG(boletoGuiaSSGDt.getId(), boletoGuiaSSGDt);
			}
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return boletoGuiaSSGDt;
	}
	
	private boolean associarDtBoleto(BoletoDt boleto, ResultSetTJGO rs1) throws Exception {
		String codSituacao = rs1.getString("INFO_SITUACAO_BOLETO");
		if (codSituacao == null || codSituacao.isEmpty())
			return false;
		
		boleto.setSituacaoBoleto(SituacaoBoleto.comCodigo(codSituacao));		
		boleto.setNossoNumero(rs1.getString("NUMR_BOLETO"));
		
		boleto.setNumeroDocumento(rs1.getString("NUMR_GUIA"));
		
		TJDataHora dataEmissao = new TJDataHora(Funcoes.StringAAAAMMDDToDate(rs1.getString("DATA_EMIS_BOLETO")));
		boleto.setDataEmissaoBoleto(dataEmissao.getDataHoraFormatadaaaaa_MM_ddHHmmss());
				
		TJDataHora dataVencimento = new TJDataHora(Funcoes.StringAAAAMMDDToDate(rs1.getString("DATA_VENCIMENTO_BOLETO")));
		boleto.setDataVencimentoBoleto(dataVencimento.getDataHoraFormatadaaaaa_MM_ddHHmmss());
		boleto.setValorBoleto(rs1.getString("VALR_BOLETO"));
		boleto.setUrlPdf(rs1.getString("URL_PDF_BOLETO"));
		String cpfCnpjPagador = rs1.getString("INFO_ID_PAGADOR_BOLETO");
		if (cpfCnpjPagador != null && cpfCnpjPagador.trim().length() > 0){
			if(cpfCnpjPagador.length() > 11){
				boleto.getPagador().setTipoPessoa(PagadorBoleto.TIPO_PESSOA_JURIDICA);
				boleto.getPagador().setCnpj(cpfCnpjPagador);		
				boleto.getPagador().setRazaoSocial(rs1.getString("NOME_PAGADOR_BOLETO"));		
			} else {
				boleto.getPagador().setTipoPessoa(PagadorBoleto.TIPO_PESSOA_FISICA);
				boleto.getPagador().setCpf(cpfCnpjPagador);		
				boleto.getPagador().setNome(rs1.getString("NOME_PAGADOR_BOLETO"));				
			}
		}
				
		return true;
	}

	public void salvar(BoletoDt boleto) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String Sql = " UPDATE V_SSGUGUIAS SET INFO_SITUACAO_BOLETO = ?";
		ps.adicionarLong(boleto.getSituacaoBoleto().getCodigo());
		
		if (boleto.getNossoNumero() != null && !boleto.getNossoNumero().isEmpty()) {
			Sql += " ,NUMR_BOLETO = ?";
			ps.adicionarString(boleto.getNossoNumero());
		}
		
		if (boleto.getDataEmissaoBoleto() != null && !boleto.getDataEmissaoBoleto().isEmpty()) {
			Sql += " ,DATA_EMIS_BOLETO = ?";
			TJDataHora dataEmissao = new TJDataHora(boleto.getDataEmissaoBoleto());
			ps.adicionarLong(dataEmissao.getDataHoraFormatadayyyyMMdd());			
		}
		
		if (boleto.getDataVencimentoBoleto() != null && !boleto.getDataVencimentoBoleto().isEmpty()) {
			Sql += " ,DATA_VENCIMENTO_BOLETO = ?";
			TJDataHora dataVencimento = new TJDataHora(boleto.getDataVencimentoBoleto());
			ps.adicionarLong(dataVencimento.getDataHoraFormatadayyyyMMdd());
		}
		
		if (boleto.getValorBoleto() != null && !boleto.getValorBoleto().isEmpty()) {
			Sql += " ,VALR_BOLETO = ?";
			ps.adicionarString(boleto.getValorBoleto());
		}
		
		Sql += " ,URL_PDF_BOLETO = ?";
		ps.adicionarString(boleto.getUrlPdf());
		
		if (boleto.getPagador().getCpf() != null && !boleto.getPagador().getCpf().isEmpty()) {
			Sql += " ,INFO_ID_PAGADOR_BOLETO = ?";
			ps.adicionarString(boleto.getPagador().getCpf());
			Sql += " ,NOME_PAGADOR_BOLETO = ?";
			ps.adicionarString(boleto.getPagador().getNome());
		} else if (boleto.getPagador().getCnpj() != null && !boleto.getPagador().getCnpj().isEmpty()) {
			Sql += " ,INFO_ID_PAGADOR_BOLETO = ?";
			ps.adicionarString(boleto.getPagador().getCnpj());
			Sql += " ,NOME_PAGADOR_BOLETO = ?";
			ps.adicionarString(boleto.getPagador().getRazaoSocial());
		}
		
		Sql += " WHERE NUMR_GUIA = ?";
		ps.adicionarLong(boleto.getNumeroGuiaCompleto());
		
		executarUpdateDelete(Sql, ps);			
	}

	public void atualizarSituacaoBoleto(BoletoDt boleto) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String Sql = " UPDATE V_SSGUGUIAS SET INFO_SITUACAO_BOLETO = ?";
		ps.adicionarLong(boleto.getSituacaoBoleto().getCodigo());
		
		Sql += " ,NUMR_BOLETO = ?";
		ps.adicionarString(boleto.getNossoNumero());
		
		Sql += " ,DATA_VENCIMENTO_BOLETO = ?";
		ps.adicionarDate(boleto.getDataVencimentoBoleto());
		
		/*if (boleto.getCodigoDeBarras() != null && !boleto.getCodigoDeBarras().isEmpty()) {
			Sql += " ,CODIGO_BARRAS_BOLETO = ?";
			ps.adicionarString(boleto.getCodigoDeBarras());
		}
		
		if (boleto.getLinhaDigitavel() != null && !boleto.getLinhaDigitavel().isEmpty()) {
			Sql += " ,LINHA_DIGITAVEL_BOLETO = ?";
			ps.adicionarString(boleto.getLinhaDigitavel());
		}*/
		
		if (boleto.getUrlPdf() != null && !boleto.getUrlPdf().isEmpty()) {
			Sql += " ,URL_PDF_BOLETO = ?";
			ps.adicionarString(boleto.getUrlPdf());
		}
		
		Sql += " WHERE NUMR_GUIA = ?";
		ps.adicionarLong(boleto.getNumeroGuiaCompleto());
		
		executarUpdateDelete(Sql, ps);		
	}
	
	/**
	 * Método para consultar guias no SSG (2º Grau) que possuem número do processo cadastrado nas guias.
	 * 
	 * @param String numeroProcessoDigitoAnoProjudi
	 * @param String numeroProcessoSPG
	 * @param List<GuiaEmissaoDt> listaGuiaEmissaoDtProjudi
	 * 
	 * @return List<GuiaEmissaoDt>
	 * 
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaEmissaoSSG(String numeroProcessoDigitoAnoProjudi, String numeroProcessoSPG, List<GuiaEmissaoDt> listaGuiaEmissaoDtProjudi) throws Exception {
		List<GuiaEmissaoDt> guiasSPG = new ArrayList<GuiaEmissaoDt>();
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SSGU_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, DATA_APRESENTACAO, STAT_PAGAMENTO, ";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_CAUSA, ";
			sql += " NUMR_PROJUDI, NUMR_PROCESSO, NUMR_GUIA_PRINCIPAL, NUMR_PROCESSO_PRINCIPAL";
			sql += " FROM V_SSGUGUIAS ";
			
			boolean temProcesso = false;
			
			if (numeroProcessoDigitoAnoProjudi != null && numeroProcessoDigitoAnoProjudi.trim().length() > 0 && Funcoes.StringToLong(numeroProcessoDigitoAnoProjudi) > 0 ) {
				sql += " WHERE NUMR_PROJUDI = " + numeroProcessoDigitoAnoProjudi;
				temProcesso = true;
			}
			else {
				if(numeroProcessoSPG != null && numeroProcessoSPG.trim().length() > 0 && Funcoes.StringToLong(numeroProcessoSPG) > 0 ) {
					sql += " WHERE NUMR_PROCESSO = " + numeroProcessoSPG;
					temProcesso = true;
				}
			}
			
			if( !temProcesso ) {
				return guiasSPG;
			}
			else {
				if( listaGuiaEmissaoDtProjudi != null && !listaGuiaEmissaoDtProjudi.isEmpty() ) {
					sql += " AND NUMR_GUIA NOT IN (";
					for( GuiaEmissaoDt guiaEmissaoDt: listaGuiaEmissaoDtProjudi ) {
						if( guiaEmissaoDt.getNumeroGuiaCompleto() != null && !guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() ) {
							sql += guiaEmissaoDt.getNumeroGuiaCompleto() + " ,";
						}
					}
					sql += ")";
				}
				
				sql = sql.replace(",)", ")");
			}
			
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null ) {
				while(rs1.next()) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSSG(guiaEmissaoDt, rs1);
					guiasSPG.add(guiaEmissaoDt);
				}				
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return guiasSPG;
	}
	
	/**
	 * Método para ser utilizado no momento de cancelar uma guia no PJD. Em conversa com o Júnio Feitosa e 
	 * Marques (18/04/2018) ficou definido limpar os números de processos das guias no SSG para evitar 
	 * que a parte realize o pagamento de uma guia cancelada e depois ser realizada o rateio no SSG.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean limparNumerosProcessosGuiaSSG(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SSGUGUIAS SET NUMR_PROJUDI = null, NUMR_PROCESSO = null WHERE NUMR_GUIA = ?";
		ps.adicionarLong(guiaEmissaoDt.getNumeroGuiaCompleto());
		
		return executarUpdateDelete(sql, ps) == 1;
	}
	
	public boolean atualizaGuiaVinculadaProcesso(String isnGuiaSSG, String numeroProcesso, String processoNumeroAntigoTemp, String codigoServentia, String comarcaCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		TJDataHora tjDataHora = new TJDataHora();
		
		//String sql= "UPDATE V_SSGUGUIAS SET NUMR_PROJUDI = ? ,CODG_SERVENTIA = ? ,CODG_COMARCA = ? ,DATA_APRESENTACAO = "+ Funcoes.BancoData(new Date()) +" WHERE ISN_SSGU_GUIAS = ? ";
		String sql= "UPDATE V_SSGUGUIAS SET NUMR_PROJUDI = ? ,DATA_APRESENTACAO = '"+ tjDataHora.getDataHoraFormatadayyyyMMdd() +"' WHERE ISN_SSGU_GUIAS = ? ";
		
		if( processoNumeroAntigoTemp == null || ( processoNumeroAntigoTemp != null && processoNumeroAntigoTemp.trim().length() == 0 ) ) {
			sql += " AND (NUMR_PROCESSO IS NULL OR NUMR_PROCESSO = 0) ";
		}
		else {
			if( processoNumeroAntigoTemp != null && processoNumeroAntigoTemp.trim().length() > 0 ) {
				sql += " AND (NUMR_PROCESSO IS NULL OR NUMR_PROCESSO = " + processoNumeroAntigoTemp.trim() + "0000) ";
			}
		}
		
		sql += " AND (NUMR_PROJUDI IS NULL OR NUMR_PROJUDI = 0)";
		
		ps.adicionarLong(numeroProcesso);
//		ps.adicionarLong(codigoServentia);
//		ps.adicionarLong(comarcaCodigo);
		ps.adicionarLong(isnGuiaSSG);
		
		return executarUpdateDelete(sql, ps) == 1;
	}
	
	/**
	 * Método para desfazer cancelamento da guia.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String numeroProcessoDigitoAno
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean desfazerCancelamentoGuiaSSG(GuiaEmissaoDt guiaEmissaoDt, String numeroProcessoDigitoAno) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SSGUGUIAS SET NUMR_PROJUDI = '"+numeroProcessoDigitoAno+"' WHERE NUMR_GUIA = ? AND NUMR_PROJUDI IS NULL";
		ps.adicionarLong(guiaEmissaoDt.getNumeroGuiaCompleto());
		
		return executarUpdateDelete(sql, ps) == 1;
	}
}
