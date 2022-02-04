package br.gov.go.tj.projudi.ps;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.InfoRepasseSPGDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LocomocaoSPGDt;
import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.SajaDocumentoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UfrValorDt;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoSPGNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
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

public class GuiaSPGPs extends Persistencia {
	
	private static final long serialVersionUID = 7171045475803901010L;
	
	public static final String NUMERO_GUIA_COMPLEMENTAR_VAZIO = "99999999999";
		
	public GuiaSPGPs(Connection conexao){
		Conexao = conexao;
	}
	
	private String obtenhaSufixoNomeTabela(String comarcaCodigo) {
		if (comarcaCodigo == null || 
			comarcaCodigo.trim().length() == 0 || 
			comarcaCodigo.equalsIgnoreCase(ComarcaDt.GOIANIA.trim())) 
			return "";
		
		return "_REM";
	}
	
	private String obtenhaSufixoNomeTabela(GuiaEmissaoDt guiaEmissaoDt) {
		if (guiaEmissaoDt != null) return obtenhaSufixoNomeTabela(guiaEmissaoDt.getComarcaCodigo());
		
		return "";		
	}
	
	/**
	 * Método para inserir a itens de guia no SPG.
	 * Somente deve ser chamado de dentro de um método de inserir guia.
	 * 
	 * @param String isn ou id da guia
	 * @param List<GuiaItemDt>
	 * @param GuiaEmissaoDt
	 * 
	 * @throws Exception
	 */
	private void inserirGuiaItem(String isnGuia, List<GuiaItemDt> listaGuiaItemDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if( listaGuiaItemDt.size() > 0 ) {
			for(int i = 0; i < listaGuiaItemDt.size(); i++) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);				
				Double valor = inserirGuiaItem(isnGuia, guiaItemDt, guiaEmissaoDt);
				inserirGuiaItemLocomocao(isnGuia, guiaItemDt, guiaEmissaoDt, valor);
			}
		}
	}
	
	private Double inserirGuiaItem(String isnGuia, GuiaItemDt guiaItemDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "INSERT INTO V_SPGAGUIAS_ITENS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", CODG_ITEM, VALR_ITEM) VALUES (";
		
		sql += isnGuia + ",";
		sql += guiaItemDt.getCustaDt().getCodigoArrecadacao() + ",";
		
		Double valor = Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
		
		UfrValorNe ufrValorNe = new UfrValorNe();
		UfrValorDt ufrValorDt = ufrValorNe.consultarDataAtual();
		
		if (ufrValorDt == null) 
			throw new MensagemException("UFR Valor não configurado para a data atual.");
		
		Double valorUFR = 0.0D;
		if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTA_GENERICA)) ) {
			if( guiaItemDt.getId_ArrecadacaoCustaGenerica() != null && guiaItemDt.getId_ArrecadacaoCustaGenerica().equals(String.valueOf(ArrecadacaoCustaDt.CODIGO_TAXA_JUDICIARIA)) ) {
				valor = valor / ufrValorDt.obtenhaValorUFRTaxaJudiciaria();
				valorUFR = ufrValorDt.obtenhaValorUFRTaxaJudiciaria();
			}
			else {
				valor = valor / ufrValorDt.obtenhaValorUFR();
				valorUFR = ufrValorDt.obtenhaValorUFR();
			}
		}
		else {
			if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.TAXA_JUDICIARIA_PROCESSO)) ) {
				valor = valor / ufrValorDt.obtenhaValorUFRTaxaJudiciaria();
				valorUFR = ufrValorDt.obtenhaValorUFRTaxaJudiciaria();
			}
			else {
				valor = valor / ufrValorDt.obtenhaValorUFR();
				valorUFR = ufrValorDt.obtenhaValorUFR();;
			}
		}
		
		String valorString = "0.00";
		
		if(valor != 0) {
			//#############################################################################
			String valorTempCalculado = Funcoes.FormatarDecimal(guiaItemDt.getValorCalculado()).replace(".","");
			valorTempCalculado = valorTempCalculado.replace(",",".");
			BigDecimal valorReaisProjudi = new BigDecimal(valorTempCalculado);
			
			String valorStringProjudi = valor.toString().replace(",","");
			String[] v = valorStringProjudi.toString().split("\\.");
			if( v[1].length() >= 4 ) {
				valorStringProjudi = v[0] + "." + v[1].substring(0, 4);
			}
			else {
				valorStringProjudi = v[0] + "." + v[1];
			}
			
			BigDecimal valorStringSPG = new BigDecimal(valorStringProjudi);
			BigDecimal valorReaisSPG = new BigDecimal(String.valueOf(valorStringSPG.doubleValue() * valorUFR));
			if( valorReaisSPG.toString().split("\\.")[1].length() >= 2 ) {
				valorReaisSPG = new BigDecimal( valorReaisSPG.toString().split("\\.")[0] + "." + valorReaisSPG.toString().split("\\.")[1].substring(0, 2) );
			}
			else {
				valorReaisSPG = new BigDecimal( valorReaisSPG.toString().split("\\.")[0] + "." + valorReaisSPG.toString().split("\\.")[1] );
			}
			
			if( valorReaisProjudi.compareTo(valorReaisSPG) == 0 ) {
				valorString = valorStringSPG.toString();
			}
			else {
				if( valorReaisSPG.compareTo(valorReaisProjudi) == -1 ) {
					while( valorReaisSPG.compareTo(valorReaisProjudi) != 0 ) {
						valorStringSPG = valorStringSPG.add(new BigDecimal("0.0001"));
						
						valorReaisSPG = new BigDecimal( String.valueOf(valorStringSPG.doubleValue() * valorUFR));

						valorReaisSPG = new BigDecimal( valorReaisSPG.toString().split("\\.")[0] + "." + valorReaisSPG.toString().split("\\.")[1].substring(0, (valorReaisSPG.toString().split("\\.")[1]).length()>1?2:1) );
					}
					valorString = valorStringSPG.toString();
				}
				else {
					if( valorReaisSPG.compareTo(valorReaisProjudi) == 1 ) {
						while( valorReaisSPG.compareTo(valorReaisProjudi) != 0 ) {
							valorStringSPG = valorStringSPG.subtract(new BigDecimal("0.0001"));
							
							valorReaisSPG = new BigDecimal( String.valueOf( valorStringSPG.doubleValue() * valorUFR ));
							
							valorReaisSPG = new BigDecimal( valorReaisSPG.toString().split("\\.")[0] + "." + valorReaisSPG.toString().split("\\.")[1].substring(0, (valorReaisSPG.toString().split("\\.")[1]).length()>1?2:1) );
						}
						valorString = valorStringSPG.toString();
					}
				}
			}
		}
		else {
			valorString = valor.toString().replace(",","");
		}					
		
		sql += valorString;
		
		sql += ")";
		
		executarComando(sql);
		
		return valor;		
	}
	
	private void inserirGuiaItemLocomocao(String isnGuia, GuiaItemDt guiaItemDt, GuiaEmissaoDt guiaEmissaoDt, Double valor) throws Exception {
		String sql = "";
		if (guiaItemDt.getLocomocaoDt() != null) {
			int quantidade = 1;
			if (guiaItemDt.getLocomocaoDt().getGuiaItemSegundoDt() != null)
				quantidade += 1;
			if (guiaItemDt.getLocomocaoDt().getGuiaItemTerceiroDt() != null)
				quantidade += 1;
			
			sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", CODG_BAIRRO, CODG_MUNICIPIO, QTDE_LOCOMOCOES, ID_PROJUDI) VALUES (";
			sql += isnGuia + ",";
			sql += guiaItemDt.getLocomocaoDt().getBairroDt().ObtenhaSomenteCodigoDoBairroSPGSemCodigoMunicipio() + ",";
			sql += guiaItemDt.getLocomocaoDt().getBairroDt().getCodigoCidadeSPG() + ",";
			sql += quantidade + ",";
			sql += guiaItemDt.getLocomocaoDt().getId() + ")";
			
			executarComando(sql);
			
			sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES_MANDADOS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", NUMR_MANDADO, DATA_CONCLUSAO, NUMR_GUIA_COMPLEMENTAR) VALUES (";
			sql += isnGuia + ",";
			if( guiaItemDt.getLocomocaoDt().getMandadoJudicialDt() != null && guiaItemDt.getLocomocaoDt().getMandadoJudicialDt().getId() != null && !guiaItemDt.getLocomocaoDt().getMandadoJudicialDt().getId().isEmpty() ) {
				sql += guiaItemDt.getLocomocaoDt().getMandadoJudicialDt().getId() + ",";
			}
			else {
				sql += "0,";
			}
			sql += "'',";
			sql += NUMERO_GUIA_COMPLEMENTAR_VAZIO + ")";
						
			executarComando(sql);
			
			sql = "INSERT INTO V_SPGAGUIAS_CREDITO" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", CODG_ZONA, QTDE_LOC) VALUES (";
			sql += isnGuia + ",";
			sql += guiaItemDt.getLocomocaoDt().getZonaDt().getZonaCodigo() + ",";			
			sql += quantidade + ")";
			
			executarComando(sql);			
					
			BigDecimal bd = new BigDecimal(valor);
			Double valr_loc = bd.doubleValue();			
			if (guiaItemDt.getLocomocaoDt().getCodigoOficialSPG() != null && guiaItemDt.getLocomocaoDt().getCodigoOficialSPG().trim().length() > 0 && !guiaItemDt.getLocomocaoDt().getCodigoOficialSPG().trim().equals(OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA) && valr_loc > 0) {
				sql = "INSERT INTO V_SPGAGUIAS_LOCFINAL" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", CODG_OFICIAL, VALR_LOC) VALUES (" + isnGuia + "," + guiaItemDt.getLocomocaoDt().getCodigoOficialSPG().trim() + "," + bd.toString().replace(",", "") + ")";
				executarComando(sql);
			}			
		} else if (guiaEmissaoDt.possuiOficialAvaliadorPenhoraPartidorOuLeilaoInformado()) {
			inserirGuiaItemOficialAvaliadorPenhoraPartidorOuLeilao(isnGuia, guiaItemDt, guiaEmissaoDt, valor);
		}
	}
	
	/**
	 * Na medida que os oficiais passarem a estar vinculados às custas dos tipos avaliador, custa partidor, pregão/leilão e penhora, esse método deixará de existir, 
	 * e passaremos a utilizar somente o método inserirGuiaItemLocomocao.
	 * @param isnGuia
	 * @param guiaItemDt
	 * @param guiaEmissaoDt
	 * @param valor
	 * @throws Exception
	 */
	private void inserirGuiaItemOficialAvaliadorPenhoraPartidorOuLeilao(String isnGuia, GuiaItemDt guiaItemDt, GuiaEmissaoDt guiaEmissaoDt, Double valor) throws Exception {
		String sql = "";
		String codg_oficial = null;
		BigDecimal bd = new BigDecimal(valor);
		Double valr_loc = bd.doubleValue();				

		if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA)) ) { //GUIAS: EXECUÇÃO QUEIXA CRIME / EXECUÇÃO SENTENÇA / FAZENDA MUNICIPAL / FINAL / FINAL JUIZADO / FINAL ZERO / RECURSO INOMINADO / RECURSO INOMINADO QUEIXA CRIME   
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGAvaliador();
		} else if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTA_PARTIDOR)) ) { //GUIA FINAL
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGPartidor();
		} else if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.PREGAO_PRACAO_LEILAO)) ) { //GUIA FINAL
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGLeilao();
		} else if ( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTA_PENHORA)) ) { //GUIAS: RECURSO INOMINADO / RECURSO INOMINADO QUEIXA CRIME
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGPenhora();
		}

		if(codg_oficial != null && codg_oficial.length() > 0 && !codg_oficial.equals(OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA) && valr_loc > 0) {
			if (guiaItemDt.getLocomocaoDt() != null) {			
				if (guiaItemDt.getLocomocaoDt().getBairroDt() != null) {
					sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", CODG_BAIRRO, CODG_MUNICIPIO, QTDE_LOCOMOCOES, ID_PROJUDI) VALUES (";
					sql += isnGuia + ",";
					sql += guiaItemDt.getLocomocaoDt().getBairroDt().ObtenhaSomenteCodigoDoBairroSPGSemCodigoMunicipio() + ",";
					sql += guiaItemDt.getLocomocaoDt().getBairroDt().getCodigoCidadeSPG() + ",";
					sql += "1,";
					sql += guiaItemDt.getLocomocaoDt().getId() + ")";
					
					executarComando(sql);
				} else {
					sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", QTDE_LOCOMOCOES, ID_PROJUDI) VALUES (" + isnGuia + "," + guiaItemDt.getQuantidade() + "," + guiaItemDt.getLocomocaoDt().getId() + ")";
					executarComando(sql);
				}
				
				sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES_MANDADOS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", NUMR_MANDADO, DATA_CONCLUSAO, NUMR_GUIA_COMPLEMENTAR) VALUES (";
				sql += isnGuia + ",";
				sql += "0,";
				sql += "'',";
				sql += NUMERO_GUIA_COMPLEMENTAR_VAZIO + ")";	
							
				executarComando(sql);
				
				if (guiaItemDt.getLocomocaoDt().getZonaDt() != null && guiaItemDt.getLocomocaoDt().getZonaDt().getZonaCodigo() != null && guiaItemDt.getLocomocaoDt().getZonaDt().getZonaCodigo().trim().length() > 0) {
					sql = "INSERT INTO V_SPGAGUIAS_CREDITO" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", CODG_ZONA, QTDE_LOC) VALUES (";
					sql += isnGuia + ",";
					sql += guiaItemDt.getLocomocaoDt().getZonaDt().getZonaCodigo() + ",";			
					sql += "1)";
					
					executarComando(sql);	
				}
			} else {
				sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", QTDE_LOCOMOCOES, ID_PROJUDI) VALUES (" + isnGuia + "," + guiaItemDt.getQuantidade() + "," + guiaItemDt.getLocomocaoDt().getId() + ")";
				executarComando(sql);
				
				sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES_MANDADOS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", NUMR_MANDADO, DATA_CONCLUSAO, NUMR_GUIA_COMPLEMENTAR) VALUES (";
				sql += isnGuia + ",";
				sql += "0,";
				sql += "'',";
				sql += NUMERO_GUIA_COMPLEMENTAR_VAZIO + ")";	
							
				executarComando(sql);
			}
			
			sql = "INSERT INTO V_SPGAGUIAS_LOCFINAL" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", CODG_OFICIAL, VALR_LOC) VALUES (" + isnGuia + "," + codg_oficial + "," + bd.toString().replace(",", "") + ")";
			executarComando(sql);
		}		
	}
	
	/**
	 * Método para inserir a guia INICIAL no SPG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	public void inserirGuiaInicial(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, FabricaConexao obFabricaConexaoPJD) throws Exception {
		
		ResultSetTJGO rs1 = null;
		
		String isnGuia = null;
		
		String sql = "INSERT INTO V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + 
						" (NUMR_GUIA, " +
						"TIPO_GUIA, " +
						"CODG_NATUREZA, " +
						"INFO_LOCAL_CERTIDAO, " +
						"VALR_ACAO, " +
						"DATA_EMISSAO, ";
						if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
							sql += "NUMR_GUIA_REFERENCIA, ";
						}
						if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
							sql += "QTDE_PARCELAS, ";
						}
						if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
							sql += "NUMR_PARCELA_ATUAL, ";
						}
						if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
							sql += "PERCENTUAL_HONOR, ";
						}
						if( guiaEmissaoDt.getNumeroGuiaPrincipalCompleto() != null && !guiaEmissaoDt.getNumeroGuiaPrincipalCompleto().isEmpty() ) {
							sql += "NUMR_GUIA_PRINCIPAL, ";
						}
						if( 
							(guiaEmissaoDt.getNumeroProcesso() != null && !guiaEmissaoDt.getNumeroProcesso().isEmpty())
							|| 
							(guiaEmissaoDt.getId_Processo() != null && !guiaEmissaoDt.getId_Processo().isEmpty())
						) {
							sql += "NUMR_PROJUDI, ";
						}
						sql += "DATA_VENC_GUIA, " +
						"INFO_INTERNET ) VALUES ( ";
		
		//numr_guia
		sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
		
		//tipo_guia
		if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		else {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		
		//codg_natureza
		if( guiaEmissaoDt.getId_NaturezaSPG() != null && guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().length() > 0 ) {
			sql += guiaEmissaoDt.getNaturezaSPGCodigo() + ",";
		}
		else {
			sql += "0,";
		}
		
		//info_local_certidao
		// Somente a comarca, exemplo Goiânia: 39000
		String codigoComarcaoServentia = ComarcaDt.GOIANIA;
		if( guiaEmissaoDt.getId_Serventia() != null && !guiaEmissaoDt.getId_Serventia().isEmpty() ) {
			ServentiaDt servDt = new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia());
			codigoComarcaoServentia = Funcoes.completarZeros(servDt.getServentiaCodigoExterno(),3);
		}
		else {
			ComarcaDt comarcaDt = new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca());
			if (comarcaDt != null && comarcaDt.getComarcaCodigo().trim().length() > 0) {
				codigoComarcaoServentia = Funcoes.completarZeros(comarcaDt.getComarcaCodigo(),3);
			}
			codigoComarcaoServentia = codigoComarcaoServentia + "000";
		}
		sql += codigoComarcaoServentia + ",";
		
		//valr_acao
		if( guiaEmissaoDt.getNovoValorAcaoAtualizado() != null && guiaEmissaoDt.getNovoValorAcaoAtualizado().length() > 0 ) {
			Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getNovoValorAcaoAtualizado()));
			sql += valor.toString().replace(",", "") + ",";
		}
		else {
			sql += "0.00,";
		}
		
		//data_emissao
		sql += Funcoes.BancoData(new Date()) + ",";
		
		//numero guia completo da guia de referencia para desconto/parcelamento
		if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
			sql += guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() + ",";
		}
		
		//quantidade total de parcelas
		if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
			sql += guiaEmissaoDt.getQuantidadeParcelas() + ",";
		}
		
		//parcela atual do parcelamento
		if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
			sql += guiaEmissaoDt.getParcelaAtual() + ",";
		}
		
		//porcentagem de desconto da guia
		if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
			Double valor = Funcoes.StringToDouble(guiaEmissaoDt.getPorcentagemDesconto().replace(".", "").replace(",", "."));
			sql += valor.toString().replace(",", "") + ",";
		}
		
		//Número guia principal
		if( guiaEmissaoDt.getNumeroGuiaPrincipalCompleto() != null && !guiaEmissaoDt.getNumeroGuiaPrincipalCompleto().isEmpty() ) {
			sql += guiaEmissaoDt.getNumeroGuiaPrincipalCompleto() + ",";
		}
		
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
		
		//data_venc_guia
		sql += Funcoes.BancoData(guiaEmissaoDt.getDataVencimento()) + ",";
		
		//info_internet
		sql += "2 )"; //valor 2 será fixo para 'via web'
		
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			executarComando(sql);
			
			sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					isnGuia = rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt));
				}
				if( isnGuia == null ) {
					throw new MensagemException("Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção)." );
				}
			}
			else {
				throw new MensagemException("Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção)." );
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
	 * Método para inserir a guia FINAL no SPG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @throws Exception
	 */
	public void inserirGuiaFinal(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		
		ResultSetTJGO rs1 = null;
		
		String isnGuia = null;
		
		String sql = "INSERT INTO V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (NUMR_GUIA, " +
											"TIPO_GUIA, " +
											"CODG_NATUREZA, " +
											"INFO_LOCAL_CERTIDAO, " +
											"VALR_ACAO, " +
											"DATA_EMISSAO, ";
											if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
												sql += "NUMR_GUIA_REFERENCIA, ";
											}
											if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
												sql += "QTDE_PARCELAS, ";
											}
											if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
												sql += "NUMR_PARCELA_ATUAL, ";
											}
											if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
												sql += "PERCENTUAL_HONOR, ";
											}
											sql += "DATA_VENC_GUIA, " +
											"INFO_INTERNET, " +
											"NUMR_PROJUDI, " +
											"NUMR_GUIA_PRINCIPAL) VALUES ( ";
		
		//numr_guia
		sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
		
		//tipo_guia
		if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		else {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		
		//codg_natureza
		if( guiaEmissaoDt.getId_NaturezaSPG() != null && guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().length() > 0 ) {
			sql += guiaEmissaoDt.getNaturezaSPGCodigo() + ",";
		}
		else {
			sql += "0,";
		}
		
		//info_local_certidao
		String codigoServentia = Funcoes.completarZeros(new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia()).getServentiaCodigoExterno(),3);
		sql += codigoServentia + ",";
		
		//valr_acao
		if( guiaEmissaoDt.getNovoValorAcaoAtualizado() != null && guiaEmissaoDt.getNovoValorAcaoAtualizado().length() > 0 ) {
			Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getNovoValorAcaoAtualizado()));
			sql += valor.toString().replace(",", "") + ",";
		}
		else {
			if( guiaEmissaoDt.getValorAcao() != null && guiaEmissaoDt.getValorAcao().length() > 0 ) {
				Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getValorAcao()));
				sql += valor.toString().replace(",", "") + ",";
			}
			else {
				sql += "0.00,";
			}
		}
		
		//data_emissao
		sql += Funcoes.BancoData(new Date()) + ",";
		
		//numero guia completo da guia de referencia para desconto/parcelamento
		if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
			sql += guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() + ",";
		}
		
		//quantidade total de parcelas
		if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
			sql += guiaEmissaoDt.getQuantidadeParcelas() + ",";
		}
		
		//parcela atual do parcelamento
		if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
			sql += guiaEmissaoDt.getParcelaAtual() + ",";
		}
		
		//porcentagem de desconto da guia
		if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
			Double valor = Funcoes.StringToDouble(guiaEmissaoDt.getPorcentagemDesconto().replace(".", "").replace(",", "."));
			sql += valor.toString().replace(",", "") + ",";
		}
		
		//data_venc_guia
		sql += Funcoes.BancoData(guiaEmissaoDt.getDataVencimento()) + ",";
		
		//info_internet
		sql += "2 ,"; //valor 2 ï¿½ fixo para 'via web'
		
		//numr_projudi
		//Alteração para o erro(314929965) ao cadastrar processo de execução fiscal
		if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
			String numeroProcesso[] = new GuiaEmissaoNe().consultarNumeroCompletoDoProcesso(guiaEmissaoDt.getId_Processo()).split("\\.");
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
			sql += Funcoes.completarZeros(numeroProcesso_0 + numeroProcesso_1 + numeroProcesso_2,13) + ",";
		}
		else {
			sql += "0000,";
		}
		
		//numr_guia_principal
		sql += Funcoes.completarZeros("", 11);
		
		sql += ")";
		
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			executarComando(sql);
			
			sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					isnGuia = rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt));
				}
				if( isnGuia == null ) {
					throw new MensagemException("Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção");
				}
			}
			else {
				throw new MensagemException("Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção");
			}
			
			//Inserir itens de guia
			this.inserirGuiaItem(isnGuia, listaGuiaItemDt, guiaEmissaoDt);
			
		} finally {
			//Fecha conexï¿½o e resultset
			if( rs1 != null ) rs1.close(); 
		}
	}
	
	/**
	 * Método para inserir a guia INTERMEDIï¿½RIO no SPG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @throws Exception
	 */
	public void inserirGuiaLocomocao(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		
		ResultSetTJGO rs1 = null;
		
		String isnGuia = null;
		
		//int quantidadeLocomocao = 0;
		Double valorLocomocao = 0.0D;
		
		for(int i = 0; i < listaGuiaItemDt.size(); i++ ) {
			GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
			
			if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) || guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL)) ) {
				//quantidadeLocomocao += Funcoes.StringToInt(guiaItemDt.getQuantidade());
				valorLocomocao += Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
			}
		}
		
		UfrValorNe ufrValorNe = new UfrValorNe();
		UfrValorDt ufrValorDt = ufrValorNe.consultarDataAtual();
		
		if (ufrValorDt == null) 
			throw new MensagemException("UFR Valor não configurado para a data atual.");
		
		valorLocomocao = Funcoes.retirarCasasDecimais(valorLocomocao / ufrValorDt.obtenhaValorUFR());
		
		String sql = "INSERT INTO V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (NUMR_GUIA, " +
											"TIPO_GUIA, " +
											"CODG_NATUREZA, " +
											"CODG_FINALIDADE, " +
											"INFO_LOCAL_CERTIDAO, " +
											"VALR_ACAO, " +
											"DATA_EMISSAO, ";
											if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
												sql += "NUMR_GUIA_REFERENCIA, ";
											}
											if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
												sql += "QTDE_PARCELAS, ";
											}
											if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
												sql += "NUMR_PARCELA_ATUAL, ";
											}
											if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
												sql += "PERCENTUAL_HONOR, ";
											}
											sql += "DATA_VENC_GUIA, " +
											"INFO_INTERNET, " +
											"NUMR_PROJUDI, " +
											"NUMR_GUIA_PRINCIPAL) VALUES ( ";
		
		//numr_guia
		sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
		
		//tipo_guia
		if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		else {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		
		//codg_natureza
		if( guiaEmissaoDt.getId_NaturezaSPG() != null && guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().length() > 0 ) {
			sql += guiaEmissaoDt.getNaturezaSPGCodigo() + ",";
		}
		else {
			sql += "0,";
		}
		
		//codg_finalidade
		sql += Funcoes.completarZeros(String.valueOf(Funcoes.StringToInt(guiaEmissaoDt.getFinalidade())),3) + ",";
		
		//info_local_certidao
		//String codigoComarca = Funcoes.completarZeros(new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca()).getComarcaCodigo(),3);
		String codigoServentia = Funcoes.completarZeros(new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia()).getServentiaCodigoExterno(),3);
		sql += codigoServentia + ",";
		
		//valr_acao
		if( guiaEmissaoDt.getNovoValorAcaoAtualizado() != null && guiaEmissaoDt.getNovoValorAcaoAtualizado().length() > 0 ) {
			Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getNovoValorAcaoAtualizado()));
			sql += valor.toString().replace(",", "") + ",";
		}
		else {
			if( guiaEmissaoDt.getValorAcao() != null && guiaEmissaoDt.getValorAcao().length() > 0 ) {
				Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getValorAcao()));
				sql += valor.toString().replace(",", "") + ",";
			}
			else {
				sql += "0.00,";
			}
		}
		
		//data_emissao
		sql += Funcoes.BancoData(new Date()) + ",";
		
		//numero guia completo da guia de referencia para desconto/parcelamento
		if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
			sql += guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() + ",";
		}
		
		//quantidade total de parcelas
		if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
			sql += guiaEmissaoDt.getQuantidadeParcelas() + ",";
		}
		
		//parcela atual do parcelamento
		if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
			sql += guiaEmissaoDt.getParcelaAtual() + ",";
		}
		
		//porcentagem de desconto da guia
		if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
			Double valor = Funcoes.StringToDouble(guiaEmissaoDt.getPorcentagemDesconto().replace(".", "").replace(",", "."));
			sql += valor.toString().replace(",", "") + ",";
		}
		
		//data_venc_guia
		sql += Funcoes.BancoData(guiaEmissaoDt.getDataVencimento()) + ",";
		
		//info_internet
		sql += "2 ,"; //valor 2 ï¿½ fixo para 'via web'
		
		//numr_projudi
		//Alteração para o erro(314929965) ao cadastrar processo de execução fiscal
		if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
			String numeroProcesso[] = new GuiaEmissaoNe().consultarNumeroCompletoDoProcesso(guiaEmissaoDt.getId_Processo()).split("\\.");
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
			sql += Funcoes.completarZeros(numeroProcesso_0 + numeroProcesso_1 + numeroProcesso_2,13) + ",";
		}
		else {
			sql += "0000,";
		}
		
		//numr_guia_principal
		sql += Funcoes.completarZeros("", 11);
		
		sql += ")";
		
		try {
//			conn.createStatement().execute(sql);
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			executarComando(sql);
			
			sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
//			rs = conn.createStatement().executeQuery(sql);
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					isnGuia = rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt));
				}
				if( isnGuia == null ) {
					throw new MensagemException("<{Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção).");
				}
			}
			else {
				throw new MensagemException("<{Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção).");
			}
			
			//Inserir itens de guia
			this.inserirGuiaItem(isnGuia, listaGuiaItemDt, guiaEmissaoDt);
			
			this.atualizarGuiaComplementarLocomocoesNaoUtilizadas(guiaEmissaoDt);
		} finally {
			//Fecha conexï¿½o e resultset
			if( rs1 != null ) rs1.close();
		}
	}
	
	private void atualizarGuiaComplementarLocomocoesNaoUtilizadas(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		// Atualiza as locomoções não utilizadas com a nova guia gerada como guia complementar...
		if (guiaEmissaoDt.isLocomocaoComplementar() && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt() != null && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt().size() > 0) {
			for(LocomocaoDt locomocaoDt : guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt()) {
				if (locomocaoDt != null && locomocaoDt.getLocomocaoSPGDt() != null && locomocaoDt.getGuiaItemDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt() != null) {
					
					String isnGuiaLocomocaoNaoUtilizada = this.obtenhaISNGuiaSPG(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getNumeroGuiaCompleto(), guiaEmissaoDt.getComarcaCodigo());
					
					String sql = "UPDATE V_SPGAGUIAS_LOCOMOCOES_MANDADOS" + obtenhaSufixoNomeTabela(guiaEmissaoDt);
					sql += " SET NUMR_GUIA_COMPLEMENTAR = " + guiaEmissaoDt.getNumeroGuiaCompleto();
					sql += " WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = " + isnGuiaLocomocaoNaoUtilizada;
					sql += " AND CNXARRAYCOLUMN = " + locomocaoDt.getLocomocaoSPGDt().getPosicaoVetor();
					
					if (executarComando(sql) == 0) {
						sql = "INSERT INTO V_SPGAGUIAS_LOCOMOCOES_MANDADOS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", NUMR_MANDADO, DATA_CONCLUSAO, NUMR_GUIA_COMPLEMENTAR, CNXARRAYCOLUMN) VALUES (";
						sql += isnGuiaLocomocaoNaoUtilizada + ",";
						sql += "0,";
						sql += "'',";
						sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
						sql += locomocaoDt.getLocomocaoSPGDt().getPosicaoVetor() + ")";
									
						executarComando(sql);
					}
				}					
			}
		}
	}

	/**
	 * Método para inserir a guia COMPLEMENTAR no SPG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @param String tipoInterlocutoria
	 * @throws Exception
	 */
	public void inserirGuiaComplementar(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, String tipoInterlocutoria) throws Exception {
		
		ResultSetTJGO rs1 = null;
		
		String isnGuia = null;
		
		String sql = "INSERT INTO V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (NUMR_GUIA," +
											"TIPO_GUIA," +
											"CODG_NATUREZA," +
											"INFO_LOCAL_CERTIDAO," +
											"VALR_ACAO," +
											"DATA_EMISSAO,";
											if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
												sql += "NUMR_GUIA_REFERENCIA,";
											}
											if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
												sql += "QTDE_PARCELAS,";
											}
											if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
												sql += "NUMR_PARCELA_ATUAL,";
											}
											if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
												sql += "PERCENTUAL_HONOR,";
											}
											sql += "DATA_VENC_GUIA," +
											"INFO_INTERNET," +
											"NUMR_PROJUDI," +
											"NUMR_GUIA_PRINCIPAL,";
		if( tipoInterlocutoria != null && tipoInterlocutoria.length() > 0 ) {
			sql += "TIPO_INTERLOCUTORIA,";
		}
		if( guiaEmissaoDt.getNumeroDUAM() != null && guiaEmissaoDt.getNumeroDUAM().length() > 0 ) {
			sql += "INFO_DUAM,";
		}
		if( guiaEmissaoDt.getQuantidadeParcelasDUAM() != null && guiaEmissaoDt.getQuantidadeParcelasDUAM().length() > 0 ) {
			sql += "INFO_DUAM_PARC,";
		}
		
		if( guiaEmissaoDt.getCodigoOabSPG() != null && guiaEmissaoDt.getCodigoOabSPG().length() > 0 ) {
			sql += "CODG_OAB,";
		}
		if( guiaEmissaoDt.getDataIniCertidaoSPG() != null && guiaEmissaoDt.getDataIniCertidaoSPG().length() > 0 ) {
			sql += "DATA_INI_CERT,";
		}
		if( guiaEmissaoDt.getDataFimCertidaoSPG() != null && guiaEmissaoDt.getDataFimCertidaoSPG().length() > 0 ) {
			sql += "DATA_FIM_CERT,";
		}
		
		sql += ") VALUES ( ";
		
		sql = sql.replace(",)", ")");
		
		//numr_guia
		sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
		
		//tipo_guia
		if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		else {
			sql += Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + ",";
		}
		
		//codg_natureza
		if( guiaEmissaoDt.getId_NaturezaSPG() != null && guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().length() > 0 ) {
			sql += guiaEmissaoDt.getNaturezaSPGCodigo() + ",";
		}
		else {
			sql += "0,";
		}
		
		//info_local_certidao
		//String codigoComarca = Funcoes.completarZeros(new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca()).getComarcaCodigo(),3);
		String codigoServentia = Funcoes.completarZeros(new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia()).getServentiaCodigoExterno(),3);
		sql += codigoServentia + ",";
		
		//valr_acao
		if( guiaEmissaoDt.getNovoValorAcaoAtualizado() != null && guiaEmissaoDt.getNovoValorAcaoAtualizado().length() > 0 ) {
			Double valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getNovoValorAcaoAtualizado()));
			sql += valor.toString().replace(",", "") + ",";
		}
		else {
			sql += "0.00,";
		}
		
		//data_emissao
		sql += Funcoes.BancoData(new Date()) + ",";
		
		//numero guia completo da guia de referencia para desconto/parcelamento
		if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
			sql += guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() + ",";
		}
		
		//quantidade total de parcelas
		if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
			sql += guiaEmissaoDt.getQuantidadeParcelas() + ",";
		}
		
		//parcela atual do parcelamento
		if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
			sql += guiaEmissaoDt.getParcelaAtual() + ",";
		}
		
		//porcentagem de desconto da guia
		if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
			Double valor = Funcoes.StringToDouble(guiaEmissaoDt.getPorcentagemDesconto().replace(".", "").replace(",", "."));
			sql += valor.toString().replace(",", "") + ",";
		}
		
		//data_venc_guia
		sql += Funcoes.BancoData(guiaEmissaoDt.getDataVencimento()) + ",";
		
		//info_internet
		sql += "2 ,"; //valor 2 ï¿½ fixo para 'via web'
		
		//numr_projudi
		//Alteração para o erro(314929965) ao cadastrar processo de execução fiscal
		if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
			String numeroProcesso[] = new GuiaEmissaoNe().consultarNumeroCompletoDoProcesso(guiaEmissaoDt.getId_Processo()).split("\\.");
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
			sql += Funcoes.completarZeros(numeroProcesso_0 + numeroProcesso_1 + numeroProcesso_2,13) + ",";
		}
		else {
			sql += "0000,";
		}
		
		//numr_guia_principal
		if( guiaEmissaoDt.getNumeroGuiaPrincipalCompleto() != null && guiaEmissaoDt.getNumeroGuiaPrincipalCompleto().length() > 0 ) {
			sql += Funcoes.completarZeros(guiaEmissaoDt.getNumeroGuiaPrincipalCompleto(), 11) + ",";
		}
		else {
			sql += Funcoes.completarZeros("", 11) + ",";
		}
		
		//tipo_interlocutoria
		if( tipoInterlocutoria != null && tipoInterlocutoria.length() > 0 ) {
			sql += Funcoes.BancoInteiro(tipoInterlocutoria) + ",";
		}
		
		//nï¿½mero da DUAM
		if( guiaEmissaoDt.getNumeroDUAM() != null && guiaEmissaoDt.getNumeroDUAM().length() > 0 ) {
			sql += Funcoes.BancoInteiro(guiaEmissaoDt.getNumeroDUAM()) + ",";
		}
		
		//nï¿½mero de parcelas da DUAM
		if( guiaEmissaoDt.getQuantidadeParcelasDUAM() != null && guiaEmissaoDt.getQuantidadeParcelasDUAM().length() > 0 ) {
			sql += Funcoes.BancoInteiro(guiaEmissaoDt.getQuantidadeParcelasDUAM()) + ",";
		}
		
		if( guiaEmissaoDt.getCodigoOabSPG() != null && guiaEmissaoDt.getCodigoOabSPG().length() > 0 ) {
			sql += "'" + guiaEmissaoDt.getCodigoOabSPG() + "',";
		}
		if( guiaEmissaoDt.getDataIniCertidaoSPG() != null && guiaEmissaoDt.getDataIniCertidaoSPG().length() > 0 ) {
			sql += Funcoes.BancoData(guiaEmissaoDt.getDataIniCertidaoSPG()) + ",";
		}
		if( guiaEmissaoDt.getDataFimCertidaoSPG() != null && guiaEmissaoDt.getDataFimCertidaoSPG().length() > 0 ) {
			sql += Funcoes.BancoData(guiaEmissaoDt.getDataFimCertidaoSPG()) + ",";
		}
		
		sql += ")";
		
		sql = sql.replace(",)", ")");
		
		try {
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			executarComando(sql);
			
			sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
			
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					isnGuia = rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt));
				}
				if( isnGuia == null ) {
					throw new MensagemException("<{Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção).");
				}
			}
			else {
				throw new MensagemException("<{Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção).");
			}
			
			//Inserir itens de guia
			this.inserirGuiaItem(isnGuia, listaGuiaItemDt, guiaEmissaoDt);
		} finally {
			//Fecha conexï¿½o e resultset
			if( rs1 != null ) rs1.close();			
		}
	}
	
	/**
	 * Método para consultar o valor da causa na guia informada como parï¿½metro.
	 * @param String numeroGuia
	 * @return String
	 * @throws Exception
	 */
	public String consultarValorCausaGuia(String numeroGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		ResultSetTJGO rs1 = null;
		
		String retorno = null;
		
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			if( numeroGuia != null && numeroGuia.length() > 0 && !numeroGuia.equals("0") && !numeroGuia.equals("null") && Funcoes.StringToLong(numeroGuia) > 0L ) {
				String sql = "SELECT VALR_ACAO FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = " + numeroGuia;
				
				rs1 = consultar(sql, ps);
				
				if( rs1 != null ) {
					while( rs1.next() ) {
						retorno = rs1.getString("VALR_ACAO");
					}
				}
			}
		} finally {
			if( rs1 != null ) rs1.close();			
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar se a guia existe no SPG.
	 * Retorna true se sim e false caso nï¿½o exista.
	 * 
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPresenteSPG(String numeroGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		ResultSetTJGO rs1 = null;
		
		boolean retorno = false;
		
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			if( numeroGuia != null && !numeroGuia.isEmpty() && !numeroGuia.equals("0") && !numeroGuia.equals("null") && Funcoes.StringToLong(numeroGuia) > 0L ) {
				String sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", NUMR_GUIA FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = " + numeroGuia;		
		
				rs1 = consultar(sql, ps);
				
				if( rs1 != null ) {
					while( rs1.next() ) {
						int numeroGuiaInteiro = Funcoes.StringToInt(numeroGuia);
						
						if( rs1.getString("NUMR_GUIA") != null && rs1.getString("NUMR_GUIA").equals(String.valueOf(numeroGuiaInteiro)) ) {
							retorno = true;
						}
					}
				}
			}
		} finally {
			if( rs1 != null ) rs1.close();			
		}
		
		return retorno;
	}
	
	/**
	 * Método para atualizar o tipo da guia no spg.
	 * @param String numeroGuia
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void atualizarTipoGuiaSPG(String numeroGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		String sql = "update V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " set TIPO_GUIA = " + Funcoes.completarZeros(new GuiaTipoNe().consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()).getGuiaTipoCodigoExterno(),2) + " where NUMR_GUIA = " + numeroGuia;
				
		executarComando(sql);
	}
	
	/**
	 * Método Alterar o valor da taxa judiciária da guia no spg.
	 * @param String numeroGuia
	 * @param String codigoArrecadacao
	 * @param String valorCorreto
	 * @throws Exception
	 */
	public void atualizarTaxaJudiciariaGuiaSPG(String numeroGuia, String codigoArrecadacao, String valorCorreto, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		ResultSetTJGO rs1 = null;
		
		if( numeroGuia != null && !numeroGuia.isEmpty() && !numeroGuia.equals("0") && !numeroGuia.equals("null") && Funcoes.StringToLong(numeroGuia) > 0L ) {
			String sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", NUMR_GUIA FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = " + numeroGuia;
			
			try {
				PreparedStatementTJGO ps = new PreparedStatementTJGO();
				
				rs1 = consultar(sql, ps);
				String idGuiaSPG = null;
				if( rs1 != null ) {
					while( rs1.next() ) {
						if( rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt)) != null && rs1.getString("NUMR_GUIA").equals(numeroGuia) ) {
							idGuiaSPG = rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt));
						}
					}
					
					if( idGuiaSPG != null && idGuiaSPG.length() > 0 ) {
						sql = "update V_SPGAGUIAS_ITENS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " set VALR_ITEM = " + valorCorreto + " where ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = " + idGuiaSPG + " and CODG_ITEM = " + codigoArrecadacao;
						
						executarComando(sql);
					}
				}
			} finally {
				if( rs1 != null ) rs1.close();			
			}
		}
	}
	
	/**
	 * Método para inserir a guia PREFEITURA DE GOIÂNIA no SPG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @throws Exception
	 */
	public void inserirGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		
		ResultSetTJGO rs1 = null;		
		String isnGuia = null;
		
		String sql = "INSERT INTO V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (NUMR_GUIA, " +
					"TIPO_GUIA, " +
					"CODG_NATUREZA, " +
					"VALR_ACAO, " +
					"DATA_EMISSAO, ";
					if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
						sql += "NUMR_GUIA_REFERENCIA, ";
					}
					if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
						sql += "QTDE_PARCELAS, ";
					}
					if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
						sql += "NUMR_PARCELA_ATUAL, ";
					}
					if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
						sql += "PERCENTUAL_HONOR, ";
					}
					sql += "DATA_VENC_GUIA, " +
					"INFO_INTERNET, " +
					"INFO_LOCAL_CERTIDAO, " +
					"NOME_PRIMEIRO_AUTOR, " +
					"NOME_PRIMEIRO_REU, " +
					"INFO_CONVENIO, " +
					"NUMR_PROJUDI, " +
					"INFO_CONVENIO_PREFEITURA) VALUES ( ";
		
		//numr_guia
		sql += guiaEmissaoDt.getNumeroGuiaCompleto() + ",";
		
		//tipo_guia
		sql += "1 ,"; //valor 1 fixo para 'inicial'		
		
		//codg_natureza
		if( guiaEmissaoDt.getId_NaturezaSPG() != null && guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().length() > 0 ) {
			sql += guiaEmissaoDt.getNaturezaSPGCodigo() + ",";
		}
		else {
			sql += "0,";
		}
		
		//valr_acao
		Double valor = 0d;
		if(guiaEmissaoDt.getProcessoDt() != null) {
			valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getProcessoDt().getValor()));			
		}
		else {
			valor = Funcoes.StringToDouble(guiaEmissaoDt.getValorAcao().replace(".", "").replace(",", "."));			
		}
		sql += valor.toString().replace(",", "") + ",";
		
		//data_emissao
		sql += Funcoes.BancoData(new Date()) + ",";
		
		//numero guia completo da guia de referencia para desconto/parcelamento
		if( guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento().isEmpty() ) {
			sql += guiaEmissaoDt.getNumeroGuiaReferenciaDescontoParcelamento() + ",";
		}
		
		//quantidade total de parcelas
		if( guiaEmissaoDt.getQuantidadeParcelas() != null && !guiaEmissaoDt.getQuantidadeParcelas().isEmpty() ) {
			sql += guiaEmissaoDt.getQuantidadeParcelas() + ",";
		}
		
		//parcela atual do parcelamento
		if( guiaEmissaoDt.getParcelaAtual() != null && !guiaEmissaoDt.getParcelaAtual().isEmpty() ) {
			sql += guiaEmissaoDt.getParcelaAtual() + ",";
		}
		
		//porcentagem de desconto da guia
		if( guiaEmissaoDt.getPorcentagemDesconto() != null && !guiaEmissaoDt.getPorcentagemDesconto().isEmpty() ) {
			Double valorPorcentagemDesconto = Funcoes.StringToDouble(guiaEmissaoDt.getPorcentagemDesconto().replace(".", "").replace(",", "."));
			sql += valorPorcentagemDesconto.toString().replace(",", "") + ",";
		}
		
		//data_venc_guia
		sql += Funcoes.BancoData(guiaEmissaoDt.getDataVencimento()) + ",";
		
		//info_internet
		sql += "2 ,"; //valor 2 fixo para 'via web'
		
		//info_local_certidao
		String localCertidao = "";
		if(guiaEmissaoDt.getProcessoDt() != null && guiaEmissaoDt.getId_Serventia() != null && guiaEmissaoDt.getId_Serventia().length() > 0) {
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia());
			if (serventiaDt != null) {
				localCertidao += Funcoes.completarZeros(serventiaDt.getServentiaCodigoExterno(),6);
			}
		}	
		sql += localCertidao + ",";
		
		//nome_primeiro_autor
		String nome_primeiro_autor = "";
		if (guiaEmissaoDt.getProcessoDt() != null) {
			nome_primeiro_autor = guiaEmissaoDt.getProcessoDt().getPrimeiroPoloAtivoNome().replaceAll("'", "");
		}
		sql += "'" + nome_primeiro_autor + "',";
		
		//nome_primeiro_reu
		String nome_primeiro_reu = "";
		if (guiaEmissaoDt.getProcessoDt() != null) {
			nome_primeiro_reu = guiaEmissaoDt.getProcessoDt().getPrimeiroPoloPassivoNome().replaceAll("'", "");
		}
		sql += "'"+ nome_primeiro_reu + "',";
		
		//info_convenio
		sql += "'E' ,"; //valor 'E' fixo para 'EMITIDA'
		
		//numr_projudi
		String numr_projudi = "";
		if (guiaEmissaoDt.getProcessoDt() != null) {
			numr_projudi = Funcoes.completarZeros(guiaEmissaoDt.getProcessoDt().getProcessoNumero().replace(".", ""), 9) + guiaEmissaoDt.getProcessoDt().getAno();
		}
		sql += numr_projudi;
		
		//info_convenio_prefeitura
		sql += ", 'S'"; //valor 'S' indicando que essa guia faz parte do convênio com a prefeitura
		
		sql += ")";
		
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			executarComando(sql);
			
			sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					isnGuia = rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt));
				}
				if( isnGuia == null ) {
					throw new MensagemException("<{Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção).");
				}
			}
			else {
				throw new MensagemException("<{Erro ao inserir a Guia Final no SPG(Erro no SQL de inserção).");
			}
			
			//Inserir itens de guia
			this.inserirGuiaItem(isnGuia, listaGuiaItemDt, guiaEmissaoDt);
			
		} finally {
			//Fecha conexï¿½o e resultset
			if( rs1 != null ) rs1.close();			
		}
	}
	
	/**
	 * Método para inserir pagamento de guia PREFEITURA DE GOIÂNIA no SPG.
	 * @param GuiaEmissaoDt
	 * @param List<GuiaItemDt>
	 * @param Date - Data de Pagamento
	 * @throws Exception
	 */
	public void inserirPagamentoGuiaPrefeituraDeGoianiaSAJ(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, TJDataHora dataPagamento, String valorTotalGuia, String numeroBancoPagamento, String numeroAgenciaPagamento) throws Exception {
		
		String isnGuia = null;
		String numeroGuiaSemSerie = null;
		boolean atualizaDataPagamentoSPG = true;
		
		if (numeroBancoPagamento != null) numeroBancoPagamento = numeroBancoPagamento.trim();
		if (numeroAgenciaPagamento != null) numeroAgenciaPagamento = numeroAgenciaPagamento.trim();
		
		if (guiaEmissaoDt.getNumeroGuiaCompleto() != null && guiaEmissaoDt.getNumeroGuiaCompleto().length() > 0 ) {
			numeroGuiaSemSerie = guiaEmissaoDt.getNumeroGuiaCompleto().substring(0, guiaEmissaoDt.getNumeroGuiaCompleto().length() - 2); 
		}	
		
		String cdgServentiaComarca = "";
		String cdgServentia = "";
		String cdgComarca = "";
		if(guiaEmissaoDt.getProcessoDt() != null && guiaEmissaoDt.getId_Serventia() != null && guiaEmissaoDt.getId_Serventia().length() > 0) {
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia());
			if (serventiaDt != null) {
				//Exemplo: 039029 (Goiânia - 1ª Vara da Fazenda Pública Municipal)
				cdgServentiaComarca = Funcoes.completarZeros(serventiaDt.getServentiaCodigoExterno(),6);
				//Exemplo: 039 (GOIÂNIA)
				cdgComarca = cdgServentiaComarca.substring(0, 3);
				//Exemplo: 029 (Goiânia - 1ª Vara da Fazenda Pública Municipal)
				cdgServentia = cdgServentiaComarca.substring(3, 6);
			}
		}
		
		TJDataHora dataRepasse = new TJDataHora(); //Data atual do sistema.
		//dataRepasse.setData(2015, 8, 27);
		
		// Só irá existir no SAJ no caso da prefeitura ter enviado o indicador de pagamento e depois o estorno, e agora o de pagamento novamente, nesse caso não poderemos excluir/inserir a guia no SAJ pelo fato de já existir o repasse. 
		isnGuia = obtenhaISNGuiaSAJ(numeroGuiaSemSerie, "050");
		if(isnGuia != null) { 
			executarComando("DELETE FROM V_SAJA_INFODOCUMENTOS WHERE ISN_SAJA_DOCUMENTOS = "+ isnGuia); //Exclui o registro do pagamento da guia, caso exista...
			atualizaDataPagamentoSPG = false;
			//executarComando("DELETE FROM V_SAJADOCUMENTOS WHERE ISN_SAJA_DOCUMENTOS = "+ isnGuia);
			//throw new MensagemException("Erro ao processar o pagamento da Guia " + numeroGuiaSemSerie + " da Prefeitura no SAJA, pagamento já havia sido processado.");				
		} else {
			//Inserir pagamento da guia
			this.inserirPagamentoGuiaPrefeituraDeGoianiaSAJ(numeroGuiaSemSerie, guiaEmissaoDt, dataPagamento, numeroBancoPagamento, numeroAgenciaPagamento, cdgServentia, cdgComarca, dataRepasse);
			isnGuia = obtenhaISNGuiaSAJ(numeroGuiaSemSerie, "050");
			if( isnGuia == null ) {
				throw new MensagemException("Erro ao inserir o pagamento da Guia " + numeroGuiaSemSerie + " da Prefeitura no SAJA(Erro no SQL de insercao)." );
			}
		}
		
		//Inserir pagamento itens de guia
		this.inserirPagamentoGuiaItemPrefeituraDeGoianiaSAJ(isnGuia, listaGuiaItemDt, valorTotalGuia, guiaEmissaoDt);	
		
		isnGuia = obtenhaISNGuiaSPG(guiaEmissaoDt);
		if( isnGuia == null ) {
			this.inserirGuiaPrefeituraDeGoiania(guiaEmissaoDt, listaGuiaItemDt);
			isnGuia = obtenhaISNGuiaSPG(guiaEmissaoDt);
			if( isnGuia == null ) 
				throw new MensagemException("Erro ao obter isn da Guia " + numeroGuiaSemSerie + " da Prefeitura no SPG(Erro no SQL de insercao).");
		}
		
		if (atualizaDataPagamentoSPG) {
			this.atualizarDataApresentacaoESituacaoDoPagamentoGuiaSGP(isnGuia, dataRepasse, guiaEmissaoDt);
			this.inserirGuiaInfoRepasse(isnGuia, cdgServentiaComarca, dataRepasse, guiaEmissaoDt.getComarcaCodigo());
		} else {
			// Só entrará nessa situação no caso da prefeitura ter enviado o indicador de pagamento em um dia e depois o de estorno em outro dia, e agora o de pagamento novamente, nesse caso não poderemos atualizar a data de apresentação no SPG pelo fato de já existir o repasse.
			this.atualizarSituacaoDoPagamentoGuiaSGP(isnGuia, guiaEmissaoDt);				
		}
	}
	
	private void inserirPagamentoGuiaPrefeituraDeGoianiaSAJ(String numeroGuiaSemSerie, GuiaEmissaoDt guiaEmissaoDt, TJDataHora dataPagamento, String numeroBancoPagamento, String numeroAgenciaPagamento, String cdgServentia, String cdgComarca, TJDataHora dataRepasse) throws Exception {
		String sql = "INSERT INTO V_SAJADOCUMENTOS (NUMR_DOCUMENTO, " +
				   "NUMR_SERIE_DOCUMENTO, " +
				   "TIPO_DOCUMENTO, " +
				   "CODG_SERVENTIA, " +
				   "CODG_COMARCA, " +
				   "CODG_NATUREZA, " +
				   "VALR_CAUSA, " +
				   "TIPO_CUSTAS, " +
				   "CODG_DOCUMENTO) VALUES ( ";

		//NUMR_DOCUMENTO
		sql += Funcoes.completarZeros(numeroGuiaSemSerie, 9) + ",";
		
		//NUMR_SERIE_DOCUMENTO
		sql += "'050' ,"; //valor 050 fixo para 'inicial'
		
		//TIPO_DOCUMENTO
		sql += "1 ,'"; //valor 1 (GRJ - guia de recolhimento judicial)	
		
		//CODG_SERVENTIA
		sql += cdgServentia + "',";
		
		//CODG_COMARCA
		sql += cdgComarca + ",";
		
		//CODG_NATUREZA
		ProcessoTipoDt processoTipoDt = null;
		if(guiaEmissaoDt.getProcessoDt() != null) {
			processoTipoDt = new ProcessoTipoNe().consultarId(guiaEmissaoDt.getProcessoDt().getId_ProcessoTipo());			
		} else {
			processoTipoDt = new ProcessoTipoNe().consultarId(guiaEmissaoDt.getId_ProcessoTipo());				
		}	
		
		if (processoTipoDt != null) {
			sql += Funcoes.completarZeros(processoTipoDt.getProcessoTipoCodigo(),3) + ",";
		} else {
			sql += "000,";
		}
		
		//VALR_CAUSA
		Double valor = 0d;
		if(guiaEmissaoDt.getProcessoDt() != null) {
			valor = Funcoes.StringToDouble(Funcoes.BancoDecimal(guiaEmissaoDt.getProcessoDt().getValor()));			
		}
		else {
			valor = Funcoes.StringToDouble(guiaEmissaoDt.getValorAcao().replace(".", "").replace(",", "."));			
		}
		sql += valor.toString().replace(",", "") + ",";
		
		//TIPO_CUSTAS
		sql += "3 ,"; 
		
		//CODG_DOCUMENTO
		sql += dataRepasse.getDataHoraFormatadayyyyMMdd() + //DATA-RECOLHIMENTO 
			   Funcoes.completarZeros(numeroBancoPagamento,3) + //NUMR-BANCO
			   Funcoes.completarZeros(numeroAgenciaPagamento,4) + //NUMR-AGENCIA
			   "0900"+ //NUMR-SUBLOTE
			   "01"; //NUMR-SEQ-DOCUMENTO
		
		sql += ")";
		
		executarComando(sql);
	}
	
	public String obtenhaISNGuiaSAJ(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String numeroGuiaSemSerie = "";
		String serie = ""; 
		if (guiaEmissaoDt.getNumeroGuiaCompleto() != null && guiaEmissaoDt.getNumeroGuiaCompleto().length() > 0) {
			numeroGuiaSemSerie = guiaEmissaoDt.getNumeroGuiaCompleto().substring(0, guiaEmissaoDt.getNumeroGuiaCompleto().length() - 2);
			serie = guiaEmissaoDt.getNumeroGuiaCompleto().substring(guiaEmissaoDt.getNumeroGuiaCompleto().length() - 2);
		}		
		return obtenhaISNGuiaSAJ(numeroGuiaSemSerie, serie);
	}
	
	private String obtenhaISNGuiaSAJ(String numeroGuiaSemSerie, String serie) throws Exception {
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try 
		{
			ps.adicionarString(Funcoes.completarZeros(numeroGuiaSemSerie, 9) + Funcoes.completarZeros(serie, 3));
			//CODG_DOCUMENTOS_SUPER é um campo descritor no adabas (indice), tornando as consultar mais rápidas...;
			sql = "SELECT ISN_SAJA_DOCUMENTOS FROM V_SAJADOCUMENTOS WHERE CODG_DOCUMENTOS_SUPER = ?";
			rs1 = consultar(sql, ps);
			if(rs1 != null ) {
				if(rs1.next()) {
					return rs1.getString("ISN_SAJA_DOCUMENTOS");
				}				
			}
			
			//NUMR_DOCUMENTO não é um campo descritor no adabas (indice), portanto as consultas são mais lentas por esse campo...;
			//Coisas do CONXX, para consulta um campo descritor no PreparedStatement é uma string, mas no ResultSet é um object.
//			sql = "SELECT ISN_SAJA_DOCUMENTOS, CODG_DOCUMENTOS_SUPER FROM V_SAJADOCUMENTOS WHERE NUMR_DOCUMENTO = "+ numeroGuiaSemSerie;
//			rs1 = consultar(sql, ps);
//			if(rs1 != null ) {
//				if(rs1.next()) {
//					Object codDocumentoDescritor = rs1.getObject("CODG_DOCUMENTOS_SUPER");
//					return rs1.getString("ISN_SAJA_DOCUMENTOS");
//				}				
//			}
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();			
		}
		
		return null;
	}
	
	private void inserirPagamentoGuiaItemPrefeituraDeGoianiaSAJ(String isnGuia, List<GuiaItemDt> listaGuiaItemDt, String valorTotalGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "";		
		if(listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for(int i = 0; i < listaGuiaItemDt.size(); i++) {
				// valor de cada item da guia
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);	
				String codigoGrupo = guiaItemDt.getCodigoArrecadacao();
				if ((codigoGrupo == null || codigoGrupo.length() == 0) && guiaItemDt.getCustaDt() != null)
					codigoGrupo = guiaItemDt.getCustaDt().getCodigoArrecadacao();
				sql = obtenhaComandoSQLInserirPagamentoGuiaItemPrefeituraDeGoianiaSAJ(isnGuia, codigoGrupo, guiaItemDt.getValorCalculado(), guiaEmissaoDt);				
				executarComando(sql);
			}
		}
		
		// Valor total da guia
		sql = obtenhaComandoSQLInserirPagamentoGuiaItemPrefeituraDeGoianiaSAJ(isnGuia, "3999", valorTotalGuia, guiaEmissaoDt);
		executarComando(sql);
	}
	
	private String obtenhaComandoSQLInserirPagamentoGuiaItemPrefeituraDeGoianiaSAJ(String isnGuia, String codigoGrupo, String valor, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		String sql = "INSERT INTO V_SAJA_INFODOCUMENTOS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " (ISN_SAJA_DOCUMENTOS, " +
												        "MATR_ITEM, " +
												        "CODG_GRUPO, " +
												        "VALR_ITEM) VALUES ( ";
		
		//ISN_SAJA_DOCUMENTOS
		sql += isnGuia + ",";
		
		//MATR_ITEM
		sql += "'9999999' ,"; //valor 9999999 fixo, indica que foi inserido pelo Projudi
		
		//CODG_GRUPO
		sql += "'" + codigoGrupo + "',"; //código do grupo (receita da custa ou 3999 para o total pago da guia)
		
		//VALR_ITEM
		Double valorItem = 0d;
		valorItem = Funcoes.StringToDouble(valor);
		sql += valorItem.toString().replace(",", "");
		
		sql += ")";
		
		return sql;
	}
	
	/**
	 * Método para cancelar a guia PREFEITURA DE GOIÂNIA no SPG.
	 * @param GuiaEmissaoDt
	 * @throws Exception
	 */
	public void cancelarGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET INFO_CONVENIO = 'D' WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
		executarComando(sql);
	}
	
	public boolean atualizaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String processoNumeroAntigoTemp, String codigoServentia, String comarcaCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " SET NUMR_PROJUDI = ? ,INFO_LOCAL_CERTIDAO = ? ,DATA_APRESENTACAO = "+ Funcoes.BancoData(new Date()) +" WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? ";
		
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
		ps.adicionarLong(codigoServentia);
		ps.adicionarLong(isnGuiaSPG);
		
		return executarUpdateDelete(sql, ps) > 0;
	}
	
	public boolean limpaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String comarcaCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " SET NUMR_PROJUDI = NULL ,INFO_LOCAL_CERTIDAO = NULL WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? AND NUMR_PROCESSO IS NULL AND NUMR_PROJUDI = ?";
		ps.adicionarLong(isnGuiaSPG);
		ps.adicionarLong(numeroProcesso);
		
		return executarUpdateDelete(sql, ps) > 0;
	}
	
	public void atualizeIndicadorGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET INFO_CONVENIO_PREFEITURA = 'S' WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
		executarComando(sql);
	}
	
	public void atualizeInfoLocalCertidao(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String codigoComarcaoServentia = null;
		
		ServentiaNe serventiaNe = new ServentiaNe();
		
		if (guiaEmissaoDt.getId_Serventia() != null && guiaEmissaoDt.getId_Serventia().trim().length() > 0) {
			ServentiaDt serventiaDt = serventiaNe.consultarId(guiaEmissaoDt.getId_Serventia());
			if (serventiaDt != null && serventiaDt.getServentiaCodigoExterno() != null && serventiaDt.getServentiaCodigoExterno().length() > 0) 
				codigoComarcaoServentia = Funcoes.completarZeros(serventiaDt.getServentiaCodigoExterno(),3);			
		} 
		
		if (codigoComarcaoServentia == null && guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().trim().length() > 0) {
			ProcessoDt processoDt = new ProcessoNe().consultarId(guiaEmissaoDt.getId_Processo());
			if (processoDt != null) {
				ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia());
				if (serventiaDt != null && serventiaDt.getServentiaCodigoExterno() != null && serventiaDt.getServentiaCodigoExterno().length() > 0) 
					codigoComarcaoServentia = Funcoes.completarZeros(serventiaDt.getServentiaCodigoExterno(),3);
			}
		}
		
		if (codigoComarcaoServentia == null && guiaEmissaoDt.getId_Comarca() != null && guiaEmissaoDt.getId_Comarca().trim().length() > 0) {
			ComarcaDt comarcaDt = new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca());
			if (comarcaDt != null && comarcaDt.getComarcaCodigo().trim().length() > 0) codigoComarcaoServentia = Funcoes.completarZeros(comarcaDt.getComarcaCodigo(),3);
			codigoComarcaoServentia += "000";	
		}
		
		if (codigoComarcaoServentia != null && codigoComarcaoServentia.trim().length() > 0) {
			String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET INFO_LOCAL_CERTIDAO = '" + codigoComarcaoServentia + "' WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
			executarComando(sql);	
		}
	}
	
	/**
	 * Método que faz o rollback no momento do cadastro do processo apagando dados do processo que não existe da transação do projudi.
	 * @param String isnGuiaSPG
	 * @param String numeroProcesso
	 * @param String codigoServentia
	 * @param String comarcaCodigo
	 * @throws Exception
	 */
	public boolean rollbackAtualizaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String codigoServentia, String comarcaCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= " UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " SET ";
		
		sql += " NUMR_PROJUDI = NULL ";
				
		if( codigoServentia != null && !codigoServentia.isEmpty() ) {
			sql += ",INFO_LOCAL_CERTIDAO = NULL ";
		}
		
		sql += " WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? ";
		ps.adicionarLong(isnGuiaSPG);
		
		sql += " AND NUMR_PROCESSO IS NULL ";
		sql += " AND NUMR_PROJUDI = ? ";
		ps.adicionarLong(numeroProcesso);

		if( codigoServentia != null && !codigoServentia.isEmpty() ) {
			sql += " AND INFO_LOCAL_CERTIDAO = ?";
			ps.adicionarLong(codigoServentia);
		}
		
		return executarUpdateDelete(sql, ps) == 1;
	}
	
	/**
	 * Método para atualizar como transmitida a guia PREFEITURA DE GOIÂNIA no SPG.
	 * @param GuiaEmissaoDt	 
	 * @throws Exception
	 */
	public void atualizarParaTransmitidaGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET INFO_CONVENIO = 'T' WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
		executarComando(sql);
	}
	
	/**
	 * Método para atualizar como recalculada a guia PREFEITURA DE GOIÂNIA no SPG.
	 * @param GuiaEmissaoDt	 
	 * @throws Exception
	 */
	public void atualizarParaRecalculadaGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET INFO_CONVENIO = 'R' WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
		executarComando(sql);
	}
	
	/**
	 * Método para atualizar como estornada por devolução de cheque a guia PREFEITURA DE GOIÂNIA no SPG.
	 * @param GuiaEmissaoDt	 
	 * @throws Exception
	 */
	public void atualizarParaEstornoPagamentoDevolucaoChequeGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET STAT_PAGAMENTO = '7' WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
		executarComando(sql);
	}
	
	/**
	 * Método para atualizar indicador para o SGP desconsiderar as guias da prefeitura nos relatórios de recebimento do banco.
	 * @param GuiaEmissaoDt	 
	 * @throws Exception
	 */
	public void atualizarInfoGuiaPrefeituraDeGoiania(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET INFO_CONVENIO_PREFEITURA = 'S' WHERE NUMR_GUIA = "+ guiaEmissaoDt.getNumeroGuiaCompleto();
		executarComando(sql);
	}

	public List<LocomocaoSPGDt> consultarLocomocaoNaoUtilizada(List<String> numerosDeGuia, String comarcaCodigo) throws Exception {
		List<LocomocaoSPGDt> locomocoesNaoUtilizadas = new ArrayList<LocomocaoSPGDt>();
		
		for (String numeroGuia : numerosDeGuia) {
			String isnGuia = this.obtenhaISNGuiaSPG(numeroGuia, comarcaCodigo);			
			if (isnGuia != null && isnGuia.trim().length() > 0) obtenhaLocomocaoNaoUtilizada(locomocoesNaoUtilizadas, isnGuia, comarcaCodigo);
		}
		
		return locomocoesNaoUtilizadas;
	}
	
	private String obtenhaISNGuiaSPG(String numeroGuiaCompleto, String comarcaCodigo) throws Exception {
		ResultSetTJGO rs1 = null;
		String sql = null;
		String isnGuia = null;
		try 
		{
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() && !numeroGuiaCompleto.equals("0") && !numeroGuiaCompleto.equals("null") && Funcoes.StringToLong(numeroGuiaCompleto) > 0L ) {
				sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " WHERE NUMR_GUIA = "+ numeroGuiaCompleto;
				rs1 = consultar(sql, new PreparedStatementTJGO());
				if(rs1 != null ) {
					if(rs1.next()) {
						isnGuia = rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo));
					}				
				}
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();			
		}
		
		return isnGuia;
	}
	
	private void obtenhaLocomocaoNaoUtilizada(List<LocomocaoSPGDt> locomocoesNaoUtilizadas, String isnGuia, String comarcaCodigo) throws Exception {
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT CODG_BAIRRO, CODG_MUNICIPIO, QTDE_LOCOMOCOES, ID_PROJUDI, CNXARRAYCOLUMN";
			sql += " FROM V_SPGAGUIAS_LOCOMOCOES" + obtenhaSufixoNomeTabela(comarcaCodigo);
			sql += " WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = " + isnGuia;
			
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null ) {
				while(rs1.next()) {
					LocomocaoSPGDt locomocaoSPGDt = new LocomocaoSPGDt();
					
					locomocaoSPGDt.setCodigoBairro(rs1.getString("CODG_BAIRRO"));
					locomocaoSPGDt.setCodigoMunicipio(rs1.getString("CODG_MUNICIPIO"));
					locomocaoSPGDt.setQuantidade(rs1.getString("QTDE_LOCOMOCOES"));
					locomocaoSPGDt.setIdProjudi(rs1.getString("ID_PROJUDI"));
					locomocaoSPGDt.setPosicaoVetor(rs1.getInt("CNXARRAYCOLUMN"));	
					
					//A OPERAÇÃO EXISTS NÃO É SUPORTADA PELO CONNX
					sql = "SELECT NUMR_MANDADO, DATA_CONCLUSAO, NUMR_GUIA_COMPLEMENTAR";
					sql += " FROM V_SPGAGUIAS_LOCOMOCOES_MANDADOS" + obtenhaSufixoNomeTabela(comarcaCodigo);
					sql += " WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = " + isnGuia;
					sql += " AND CNXARRAYCOLUMN = " + locomocaoSPGDt.getPosicaoVetor();
					sql += " AND NUMR_MANDADO <> 99999999999";
					
					rs2 = consultar(sql, new PreparedStatementTJGO());
					if(rs2 != null && rs2.next()) {
						locomocaoSPGDt.setNumeroMandado(rs2.getLong("NUMR_MANDADO"));
						if (Funcoes.StringToLong(NUMERO_GUIA_COMPLEMENTAR_VAZIO) != rs2.getLong("NUMR_GUIA_COMPLEMENTAR")) 
							locomocaoSPGDt.setNumeroGuiaComplementar(rs2.getLong("NUMR_GUIA_COMPLEMENTAR"));
					}
					
					locomocoesNaoUtilizadas.add(locomocaoSPGDt);
				}				
			}
			
		} finally {
			//Fecha conexï¿½o e resultset
			if( rs1 != null ) rs1.close();
			if( rs2 != null ) rs2.close();
		}		
	}
	
	public void cancelarGuiaEmitidaLocomocaoComplementar(GuiaEmissaoDt guiaEmissaoDt, List<LocomocaoDt> locomocoesUtilizadasComplementarProjudi) throws Exception {
		// Atualiza as locomoções não utilizadas com a nova guia gerada como guia complementar...
		if (guiaEmissaoDt.isLocomocaoComplementar() && locomocoesUtilizadasComplementarProjudi != null && locomocoesUtilizadasComplementarProjudi.size() > 0) {
			for(LocomocaoDt locomocaoDt : locomocoesUtilizadasComplementarProjudi) {
				if (locomocaoDt != null && locomocaoDt.getLocomocaoSPGDt() != null && locomocaoDt.getGuiaItemDt() != null && 
					locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getNumeroGuiaCompleto() != null) {
					
					String isnGuiaLocomocao = this.obtenhaISNGuiaSPG(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getNumeroGuiaCompleto(), guiaEmissaoDt.getComarcaCodigo());
					
					String sql = "UPDATE V_SPGAGUIAS_LOCOMOCOES_MANDADOS" + obtenhaSufixoNomeTabela(guiaEmissaoDt);
					sql += " SET NUMR_GUIA_COMPLEMENTAR = " + NUMERO_GUIA_COMPLEMENTAR_VAZIO;
					sql += " WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = " + isnGuiaLocomocao;
					sql += " AND CNXARRAYCOLUMN = " + locomocaoDt.getLocomocaoSPGDt().getPosicaoVetor();
					
					executarComando(sql);
				}					
			}
		}
	}
	
	public String obtenhaISNGuiaSPG(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try 
		{
			if( guiaEmissaoDt != null && guiaEmissaoDt.getNumeroGuiaCompleto() != null && !guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() && !guiaEmissaoDt.getNumeroGuiaCompleto().equals("0") && !guiaEmissaoDt.getNumeroGuiaCompleto().equals("null") && Funcoes.StringToLong(guiaEmissaoDt.getNumeroGuiaCompleto()) > 0L) {
				ps.adicionarString(guiaEmissaoDt.getNumeroGuiaCompleto());
				sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = ?";
				rs1 = consultar(sql, ps);
				if(rs1 != null ) {
					if(rs1.next()) {
						return rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt));
					}				
				}
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();			
		}
		
		return null;
	}
	
	private void atualizarDataApresentacaoESituacaoDoPagamentoGuiaSGP(String isnGuia, TJDataHora dataRepasse, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET DATA_APRESENTACAO = " + Funcoes.BancoData(dataRepasse.getDataFormatadaddMMyyyy()) + ", STAT_PAGAMENTO = NULL WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = "+ isnGuia;
		executarComando(sql);
	}
	
	private void atualizarSituacaoDoPagamentoGuiaSGP(String isnGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET STAT_PAGAMENTO = NULL WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = "+ isnGuia;
		executarComando(sql);
	}
	
	/**
	 * Método para inserir info repasse.
	 * 
	 * @param String isnGuia
	 * @param String cdgServentiaComarca
	 * @param TJDataHora dataRepasse
	 * @param String comarcaCodigo
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public void inserirGuiaInfoRepasse(String isnGuia, String cdgServentiaComarca, TJDataHora dataRepasse, String comarcaCodigo) throws Exception {
		String sql = "INSERT INTO V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(comarcaCodigo) + " ( ";
	
		if( isnGuia != null && isnGuia.trim().length() > 0 ) {
			sql += "ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + ", ";
		}
				
		if( cdgServentiaComarca != null && cdgServentiaComarca.trim().length() > 0 ) {
			sql += "CODG_ESCRIVANIA, ";
		}
		
		if( dataRepasse != null ) {
			sql += "DATA_REPASSE, ";
		}
		
		sql += "PERC_REPASSE) VALUES ( ";

		//ISN_SPGA_GUIAS
		if( isnGuia != null && isnGuia.trim().length() > 0 ) {
			sql += isnGuia + ",";
		}
		
		//CODG_ESCRIVANIA
		if( cdgServentiaComarca != null && cdgServentiaComarca.trim().length() > 0 ) {
			sql += cdgServentiaComarca + ",";
		}
		
		//DATA_REPASSE
		if( dataRepasse != null ) {
			sql += Funcoes.BancoData(dataRepasse.getDataFormatadaddMMyyyy()) + ",";
		}
		
		//PERC_REPASSE
		sql += "100)"; //valor 100 fixo
		
		executarComando(sql);
	}
	
	/**
	 * Método para inserir info repasse.
	 * 
	 * @param String isnGuia
	 * @param String cdgServentiaComarca
	 * @param TJDataHora dataRepasse
	 * @param String percentualRepasse
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public void inserirGuiaInfoRepasse(String isnGuia, String cdgServentiaComarca, TJDataHora dataRepasse, String percentualRepasse, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String sql = "INSERT INTO V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " ( ";
	
		if( isnGuia != null && isnGuia.trim().length() > 0 ) {
			sql += "ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ", ";
		}
				
		if( cdgServentiaComarca != null && cdgServentiaComarca.trim().length() > 0 ) {
			sql += "CODG_ESCRIVANIA, ";
		}
		
		if( dataRepasse != null ) {
			sql += "DATA_REPASSE, ";
		}
		
		sql += "PERC_REPASSE) VALUES ( ";

		//ISN_SPGA_GUIAS
		if( isnGuia != null && isnGuia.trim().length() > 0 ) {
			sql += isnGuia + ",";
		}
		
		//CODG_ESCRIVANIA
		if( cdgServentiaComarca != null && cdgServentiaComarca.trim().length() > 0 ) {
			sql += cdgServentiaComarca + ",";
		}
		
		//DATA_REPASSE
		if( dataRepasse != null ) {
			sql += Funcoes.BancoData(dataRepasse.getDataFormatadaddMMyyyy()) + ",";
		}
		
		//PERC_REPASSE
		sql += percentualRepasse + ")";
		
		executarComando(sql);
	}
	
	/**
	 * Método para atualizar info repasse.
	 * 
	 * @param String isnGuia
	 * @param String cdgServentiaComarca
	 * @param String comarcaCodigo
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	/*public boolean atualizaGuiaInfoRepasse(String isnGuia, String cdgServentiaComarca, String comarcaCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(comarcaCodigo) + " SET CODG_ESCRIVANIA = ? , DATA_REPASSE = "+Funcoes.BancoData(new TJDataHora().getDataFormatadaddMMyyyy())+" WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? AND CODG_ESCRIVANIA IS NULL";
		ps.adicionarLong(cdgServentiaComarca);
		ps.adicionarLong(isnGuia);
		
		return executarUpdateDelete(sql, ps) > 0;
	}*/
	
	/**
	 * Método para atualizar info repasse.
	 * 
	 * @param String isnGuia
	 * @param String cdgServentiaComarca
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	/*public boolean atualizaGuiaInfoRepasseSemValidacao(String isnGuia, String cdgServentiaComarca, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET CODG_ESCRIVANIA = ? , DATA_REPASSE = "+Funcoes.BancoData(new TJDataHora().getDataFormatadaddMMyyyy())+" WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = ?";
		ps.adicionarLong(cdgServentiaComarca);
		ps.adicionarLong(isnGuia);
		
		return executarUpdateDelete(sql, ps) > 0;
	}*/
	
	/**
	 * Método que faz o delete no momento do cadastro do processo apagando dados do processo que não existe da transação do projudi.
	 * @param String isnGuia
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public boolean rollbackInserirGuiaInfoRepasse(String isnGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "DELETE FROM V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = ? ";
		ps.adicionarLong(isnGuia);
		
		return executarUpdateDelete(sql, ps) == 1;
	}
	
	/**
	 * Consulta as guias emitidas pelo SPG (Série 09)
	 * 
	 * @param String numeroProcessoDigitoAnoProjudi
	 * @param String numeroProcessoSPG
	 * @param String comarcaCodigo
	 * @param List<GuiaEmissaoDt> listaGuiaProjudi
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaEmissaoSPG(String numeroProcessoDigitoAnoProjudi, String numeroProcessoSPG, String comarcaCodigo, List<GuiaEmissaoDt> listaGuiaProjudi) throws Exception {
		List<GuiaEmissaoDt> guiasSPG = new ArrayList<GuiaEmissaoDt>();
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + ", NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA";
			sql += " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo);
			
			boolean temProcesso = false;
			
			if (numeroProcessoDigitoAnoProjudi != null && numeroProcessoDigitoAnoProjudi.trim().length() > 0 && Funcoes.StringToLong(numeroProcessoDigitoAnoProjudi) > 0) {
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
				if( listaGuiaProjudi != null && !listaGuiaProjudi.isEmpty() ) {
					sql += " AND NUMR_GUIA NOT IN (";
					for( GuiaEmissaoDt guiaEmissaoDt: listaGuiaProjudi ) {
						if( guiaEmissaoDt.getNumeroGuiaCompleto() != null && !guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() ) {
							sql += guiaEmissaoDt.getNumeroGuiaCompleto() + " ,";
						}
					}
					sql += ")";
				}
			}
			
			sql = sql.replace(",)", ")");
			
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null ) {
				while(rs1.next()) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaEmissaoDt, rs1, comarcaCodigo);					
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
	 * Consulta as guias emitidas pelo SPG (Série 09)
	 * 
	 * @param isnGuia
	 * @param comarcaCodigo
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(String isnGuia, String comarcaCodigo) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + ", NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL,INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo);
			sql += " WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = " + isnGuia;
						
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null ) {
				while(rs1.next()) {
					guiaEmissaoDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaEmissaoDt, rs1, comarcaCodigo);
					this.consultarItensGuiaEmissaoSPG(isnGuia, comarcaCodigo, guiaEmissaoDt);
				}				
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Consulta as guias emitidas pelo SPG (Série 09)
	 * @param numeroCompletoGuia
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialSPG(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaSPGDt = null;
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS";
			sql += " WHERE NUMR_GUIA = " + numeroCompletoGuia;
			//sql += " AND NUMR_PROCESSO IS NULL";
			//sql += " AND NUMR_PROJUDI IS NULL"; Verificar no projudi se realmente essa guia foi utilizada.
			sql += " AND NUMR_GUIA_PRINCIPAL IS NULL";
			sql += " AND TIPO_GUIA = 1"; //Inicial
						
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null && rs1.next()) {
				guiaSPGDt = new GuiaEmissaoDt();					
				this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.GOIANIA);	
				this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.GOIANIA, guiaSPGDt);
			}
			
			if (guiaSPGDt == null) {
				sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
				sql += "NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
				sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
				sql += " FROM V_SPGAGUIAS_REM";
				sql += " WHERE NUMR_GUIA = " + numeroCompletoGuia;
				//sql += " AND NUMR_PROCESSO IS NULL";
				//sql += " AND NUMR_PROJUDI IS NULL";  Verificar no projudi se realmente essa guia foi utilizada.
				sql += " AND NUMR_GUIA_PRINCIPAL IS NULL";
				sql += " AND TIPO_GUIA = 1"; //Inicial
				
				rs1 = consultar(sql, new PreparedStatementTJGO());
				if(rs1 != null && rs1.next()) {
					guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.APARECIDA_DE_GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.APARECIDA_DE_GOIANIA, guiaSPGDt);
				}	
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return guiaSPGDt;
	}
	
	/**
	 * Consulta as guias emitidas pelo SPG (Série 09)
	 * @param numeroCompletoGuia
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialSPGNumeroProcessoProjudi(String numeroProcessoProjudi) throws Exception {
		GuiaEmissaoDt guiaSPGDt = null;
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS";
			sql += " WHERE NUMR_PROJUDI = " + numeroProcessoProjudi;
			sql += " AND NUMR_GUIA_PRINCIPAL IS NULL";
			sql += " AND TIPO_GUIA = 1"; //Inicial
						
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null && rs1.next()) {
				guiaSPGDt = new GuiaEmissaoDt();					
				this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.GOIANIA);	
				this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.GOIANIA, guiaSPGDt);
			}
			
			if (guiaSPGDt == null) {
				sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
				sql += "NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
				sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
				sql += " FROM V_SPGAGUIAS_REM";
				sql += " WHERE NUMR_PROJUDI = " + numeroProcessoProjudi;
				sql += " AND NUMR_GUIA_PRINCIPAL IS NULL";
				sql += " AND TIPO_GUIA = 1"; //Inicial
				
				rs1 = consultar(sql, new PreparedStatementTJGO());
				if(rs1 != null && rs1.next()) {
					guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.APARECIDA_DE_GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.APARECIDA_DE_GOIANIA, guiaSPGDt);
				}	
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return guiaSPGDt;
	}
	
	/**
	 * Consulta as guias emitidas pelo SPG (Série 09)
	 * @param numeroCompletoGuia
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoComplementarSPG(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaSPGDt = null;
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS";
			sql += " WHERE NUMR_GUIA = " + numeroCompletoGuia;
			sql += " AND NUMR_GUIA_PRINCIPAL IS NOT NULL";
			sql += " AND TIPO_GUIA IN (1, 2)"; //Inicial ou Interlocutória
						
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null && rs1.next()) {
				guiaSPGDt = new GuiaEmissaoDt();					
				this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.GOIANIA);	
				this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.GOIANIA, guiaSPGDt);
			}
			
			if (guiaSPGDt == null) {
				sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
				sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
				sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
				sql += " FROM V_SPGAGUIAS_REM";
				sql += " WHERE NUMR_GUIA = " + numeroCompletoGuia;				
				sql += " AND NUMR_GUIA_PRINCIPAL IS NOT NULL";
				sql += " AND TIPO_GUIA IN (1, 2)"; //Inicial ou Interlocutória
				
				rs1 = consultar(sql, new PreparedStatementTJGO());
				if(rs1 != null && rs1.next()) {
					guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.APARECIDA_DE_GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.APARECIDA_DE_GOIANIA, guiaSPGDt);
				}	
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return guiaSPGDt;
	}
	
	/**
	 * Consulta as guias emitidas pelo SPG (Série 09)
	 * @param numeroCompletoGuia
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(String numeroCompletoGuia) throws Exception {
		GuiaEmissaoDt guiaSPGDt = null;
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS";
			sql += " WHERE NUMR_GUIA = " + numeroCompletoGuia;
									
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if(rs1 != null && rs1.next()) {
				guiaSPGDt = new GuiaEmissaoDt();					
				this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.GOIANIA);	
				this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.GOIANIA, guiaSPGDt);
			}
			
			if (guiaSPGDt == null) {
				sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
				sql += "NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
				sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
				sql += " FROM V_SPGAGUIAS_REM";
				sql += " WHERE NUMR_GUIA = " + numeroCompletoGuia;
								
				rs1 = consultar(sql, new PreparedStatementTJGO());
				if(rs1 != null && rs1.next()) {
					guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.APARECIDA_DE_GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.APARECIDA_DE_GOIANIA, guiaSPGDt);
				}	
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return guiaSPGDt;
	}
	
	private void consultarItensGuiaEmissaoSPG(String isnGuia, String comarcaCodigo, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String sql1 = null;
		String sql2 = null;
		PreparedStatementTJGO ps1 = new PreparedStatementTJGO();
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
		guiaEmissaoDt.setListaGuiaItemDt(new ArrayList<GuiaItemDt>());
		
		UfrValorNe ufrValorNe = new UfrValorNe();
		UfrValorDt ufrValorDt = ufrValorNe.consultarData(Funcoes.Stringyyyy_MM_ddToDateTime(guiaEmissaoDt.getDataEmissao()));		
		if (ufrValorDt == null) ufrValorDt = ufrValorNe.consultarDataAtual();		
		if (ufrValorDt == null) throw new MensagemException("UFR Valor não configurado para a data atual.");
		
		sql1 = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + ", CODG_ITEM, VALR_ITEM";
		sql1 += " FROM V_SPGAGUIAS_ITENS" + obtenhaSufixoNomeTabela(comarcaCodigo);
		sql1 += " WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? AND VALR_ITEM IS NOT NULL";
		ps1.adicionarString(isnGuia);
				
		sql2 = "SELECT NOME_GRUPO FROM V_SAJAGRUPOS WHERE CODG_GRUPO = ?";
		try 
		{
			rs1 = consultar(sql1, ps1);
			if(rs1 != null ) {
				while(rs1.next()) {
					GuiaItemDt guiaItemDt = new GuiaItemDt();					
					
					guiaItemDt.setId(rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo)));
					guiaItemDt.setCodigoArrecadacao(rs1.getString("CODG_ITEM"));
					if (Funcoes.StringToInt(guiaItemDt.getCodigoArrecadacao()) == Funcoes.StringToInt(CustaDt.REGIMENTO_TAXA_JUDICIARIA)) {
						guiaItemDt.setValorCalculado(ufrValorDt.obtenhaValorTaxaJudiciariaSPGEmReais(rs1.getString("VALR_ITEM")).toString());
					} else {
						guiaItemDt.setValorCalculado(ufrValorDt.obtenhaValorURFSPGEmReais(rs1.getString("VALR_ITEM")).toString());
					}	
					guiaItemDt.setValorReferencia(guiaItemDt.getValorCalculado());
					guiaItemDt.setQuantidade("1");
					guiaItemDt.setArrecadacaoCusta("TAXA SPG"); //BUSCAR DA VIEW SAJA-GRUPOS
					
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
	
	private void associarGuiaEmissaoDtSPG(GuiaEmissaoDt guiaEmissaoDt, ResultSetTJGO rs1, String comarcaCodigo)  throws Exception {
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = null;
		String sql = null;
		
		guiaEmissaoDt.setGuiaEmitidaSPG(true);
		guiaEmissaoDt.setGuiaEmitidaSSG(false);
		
		guiaEmissaoDt.setId(rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo)));
		guiaEmissaoDt.setNumeroGuiaCompleto(rs1.getString("NUMR_GUIA"));
		guiaEmissaoDt.atualizeNumeroESeriePeloNumeroCompleto();
		guiaEmissaoDt.setDataEmissao(new TJDataHora(rs1.getDate("DATA_EMISSAO")).getDataHoraFormatadaaaaa_MM_ddHHmmss());
		
		int tipoGuia = Funcoes.StringToInt(rs1.getString("TIPO_GUIA"));
		int numeroGuiaPrincipal = Funcoes.StringToInt(rs1.getString("NUMR_GUIA_PRINCIPAL"));
				
		guiaEmissaoDt.setId_GuiaTipo(String.valueOf(tipoGuia));
		guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG);
		if (guiaEmissaoDt.isSerie02()) {
			if (tipoGuia == 1) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_PUBLICACAO);
			else if (tipoGuia == 2) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_GRS);
			else if (tipoGuia == 3) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_JUDICIAL);
			else if (tipoGuia == 4) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_CONCURSO);
			else if (tipoGuia == 5) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_DEVOLUCAO);
			else if (tipoGuia == 8) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_EVENTO);
			else if (tipoGuia == 9) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_PRECATORIO);
		} else {
			if (tipoGuia == 1) {
				if(numeroGuiaPrincipal != 0) {
					guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_COMPLEMENTAR);
				} else {
					guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_INICIAL);
				}
			}			
			else if (tipoGuia == 2) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_GRS);
			else if (tipoGuia == 3) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_CERTIDAO);
			else if (tipoGuia == 4) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_DESARQUIVAMENTO);
			else if (tipoGuia == 5) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_FINAL_FAZENDA_PUBLICA);
			else if (tipoGuia == 6) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_INTERMEDIARIA);
			else if (tipoGuia == 7) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_FINAL);
			else if (tipoGuia == 8) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_FINAL_ZERO);
			else if (tipoGuia == 9) guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.NOME_GUIA_SPG_DEPOSITO_JUDICIAL);
		}		
		
		GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
		guiaModeloDt.setId("");
		guiaModeloDt.setGuiaModelo("");
		guiaModeloDt.setId_GuiaTipo(rs1.getString("TIPO_GUIA"));
		guiaModeloDt.setGuiaTipo(guiaEmissaoDt.getGuiaTipo());
		guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
		
		if( rs1.getDate("DATA_VENC_GUIA") != null ) {
			guiaEmissaoDt.setDataVencimento(new TJDataHora(rs1.getDate("DATA_VENC_GUIA")).getDataHoraFormatadaaaaa_MM_ddHHmmss());
		}
		
		if( rs1.getDate("DATA_APRESENTACAO") != null ) {
			guiaEmissaoDt.setDataApresentacaoSPG(new TJDataHora(rs1.getDate("DATA_APRESENTACAO")).getDataHoraFormatadaaaaa_MM_ddHHmmss());
		}
		
		int statPagamento = Funcoes.StringToInt(rs1.getString("STAT_PAGAMENTO"));
		
		if (statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_4 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_5 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_6 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_7 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_8 || 
			statPagamento == GuiaSPGNe.PEDIDO_RESSARCIMENTO_TIPO_9) {
			
			guiaEmissaoDt.setId_GuiaStatus("10");
			guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.NOME_GUIA_SPG_PEDIDO_RESSARCIMENTO_SOLICITADO);
			
		} else {
			
			sql = "SELECT CODG_DOCUMENTO FROM V_SAJADOCUMENTOS WHERE CODG_DOCUMENTOS_SUPER = ?";
			ps = new PreparedStatementTJGO();
			ps.adicionarString(Funcoes.completarZeros(guiaEmissaoDt.getNumeroGuia(), 9) + Funcoes.completarZeros(guiaEmissaoDt.getNumeroGuiaSerie(), 3));
			
			try 
			{
				rs2 = consultar(sql, ps);
				if(rs2 != null && rs2.next() ) {
					String codgDocumento = rs2.getString("CODG_DOCUMENTO");
					if (codgDocumento != null && codgDocumento.length() >= 8 && Funcoes.validaDataYYYYMMDD(codgDocumento.substring(0, 7))) {
						String dataAnoMesDia = codgDocumento.substring(0, 8);
						
						TJDataHora dataPagamento = new TJDataHora();
						dataPagamento.setDataaaaaMMdd(dataAnoMesDia);
						
						guiaEmissaoDt.setDataRecebimento(dataPagamento.getDataHoraFormatadaaaaa_MM_ddHHmmss());
						
						guiaEmissaoDt.setId_GuiaStatus("3");
						guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.PAGO_SPG);
					} else {
						guiaEmissaoDt.setId_GuiaStatus("1");
						guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO_SPG);
					}
				} else {
					guiaEmissaoDt.setId_GuiaStatus("1");
					guiaEmissaoDt.setGuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO_SPG);
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
		guiaEmissaoDt.setValorAcao(rs1.getString("VALR_ACAO"));
		guiaEmissaoDt.setNumeroProcesso(rs1.getString("NUMR_PROJUDI"));
		guiaEmissaoDt.setNumeroProcessoSPG(rs1.getString("NUMR_PROCESSO"));
		guiaEmissaoDt.setId_GuiaEmissaoPrincipal(rs1.getString("NUMR_GUIA_PRINCIPAL"));
		guiaEmissaoDt.setSituacaoPagamentoSPG(rs1.getString("STAT_PAGAMENTO"));
		guiaEmissaoDt.setGuiaEmitidaSPGCapital(comarcaCodigo == null || comarcaCodigo.trim().length() == 0 || comarcaCodigo.equalsIgnoreCase(ComarcaDt.GOIANIA.trim()));
		guiaEmissaoDt.setNumeroProcessoDependente(rs1.getString("NUMR_PROCESSO_PRINCIPAL"));
		guiaEmissaoDt.setInfoLocalCertidaoSPG(rs1.getString("INFO_LOCAL_CERTIDAO"));
		guiaEmissaoDt.setTipoPessoaSPG(rs1.getString("TIPO_PESSOA"));
		guiaEmissaoDt.setComarcaCodigo(comarcaCodigo);
		guiaEmissaoDt.setNumeroGuiaReferenciaDescontoParcelamento(rs1.getString("NUMR_GUIA_REFERENCIA"));
		guiaEmissaoDt.setQuantidadeParcelas(rs1.getString("QTDE_PARCELAS"));
		guiaEmissaoDt.setParcelaAtual(rs1.getString("NUMR_PARCELA_ATUAL"));
		if( rs1.getString("NUMR_PARCELA_ATUAL") != null && !rs1.getString("NUMR_PARCELA_ATUAL").equals("null") && Funcoes.StringToInt(rs1.getString("NUMR_PARCELA_ATUAL")) > 0 ) {
			guiaEmissaoDt.setTipoGuiaReferenciaDescontoParcelamento(GuiaEmissaoDt.TIPO_GUIA_PARCELADA);
		}
		guiaEmissaoDt.setInfoPatenteSPG(rs1.getString("INFO_PATENTE"));
		guiaEmissaoDt.setId_GuiaTipo(rs1.getString("TIPO_GUIA"));
		if( rs1.getString("TIPO_CERTIDAO") != null ) {
			guiaEmissaoDt.setTipoGuiaCertidaoSPG(rs1.getString("TIPO_CERTIDAO"));
		}
		
		if( rs1.getString("MODO_EMISSAO") != null ) {
			guiaEmissaoDt.setModoEmissaoCertidaoSPG(rs1.getString("MODO_EMISSAO"));
		}
		
		if( rs1.getDate("DATA_INI_CERT") != null ) {
			guiaEmissaoDt.setDataIniCertidaoSPG(new TJDataHora(rs1.getDate("DATA_INI_CERT")).getDataHoraFormatadaaaaa_MM_ddHHmmss());
		}
		
		if( rs1.getDate("DATA_FIM_CERT") != null ) {
			guiaEmissaoDt.setDataFimCertidaoSPG(new TJDataHora(rs1.getDate("DATA_FIM_CERT")).getDataHoraFormatadaaaaa_MM_ddHHmmss());
		}
		
		if( rs1.getDate("INFO_AREA") != null ) {
			guiaEmissaoDt.setInfoAreaSPG(rs1.getString("INFO_AREA"));
		}
		
		guiaEmissaoDt.setGuiaEmitidaSPG(true);
		
		guiaEmissaoDt.atualizeNumeroESeriePeloNumeroCompleto();
	}
	
	/**
	 * Método para consultar Guia no SPG pelo número da Guia Completo.
	 * @param String numeroGuiaCompleto
	 * @param String comarcaCodigo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaSPGByNumeroGuiaCompleto(String numeroGuiaCompleto, String comarcaCodigo) throws Exception {
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() && !numeroGuiaCompleto.equals("0") && !numeroGuiaCompleto.equals("null") && Funcoes.StringToLong(numeroGuiaCompleto) > 0L ) {
				
				String sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) +", NUMR_GUIA, NUMR_PROJUDI, INFO_LOCAL_CERTIDAO, VALR_ACAO FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " WHERE NUMR_GUIA = ? ";
				ps.adicionarString(numeroGuiaCompleto);
				
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						
						guiaEmissaoSPGDt = new GuiaEmissaoDt();
						
						guiaEmissaoSPGDt.setGuiaEmitidaSPG(true);
						guiaEmissaoSPGDt.setId(						rs1.getString("ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo)));
						guiaEmissaoSPGDt.setNumeroGuiaCompleto(		rs1.getString("NUMR_GUIA"));
						guiaEmissaoSPGDt.setNumeroProcesso(			rs1.getString("NUMR_PROJUDI"));
						guiaEmissaoSPGDt.setInfoLocalCertidaoSPG( 	rs1.getString("INFO_LOCAL_CERTIDAO"));
						guiaEmissaoSPGDt.setValorAcao(				rs1.getString("VALR_ACAO"));
						
						guiaEmissaoSPGDt.setGuiaEmitidaSPG(true);
						
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoSPGDt;
	}
	
	/**
	 * Método para consultar as guias do SPG pelo número completo da guia (capital)
	 * @param String numeroGuiaCompleto
	 * @throws Exception
	 * @author fogomes
	 */
	public GuiaEmissaoDt consultarGuiaSPGPeloNumeroGuiaCompletoCapital(String numeroGuiaCompleto) throws Exception {
		
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		GuiaEmissaoDt guiaEmissaoSPGDt2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		ResultSetTJGO rs1 = null;
		String numProcCNJ = "";
		
		try {
			if( numeroGuiaCompleto != null ) {
				
				String sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, NUMR_PROJUDI, INFO_LOCAL_CERTIDAO, VALR_ACAO, NOME_PRIMEIRO_AUTOR, NUMR_CPF_CGC, TIPO_GUIA, ";
				sql += "NUMR_PROCESSO, CODG_NATUREZA ";
				sql += "FROM V_SPGAGUIAS ";
				sql += "WHERE NUMR_GUIA = ? ";

				ps.adicionarString(numeroGuiaCompleto);
				
				rs1 = this.consultar(sql, ps);
								
				if(rs1 != null) { 
					
					while(rs1.next()) {
						
						guiaEmissaoSPGDt = new GuiaEmissaoDt();
						
						guiaEmissaoSPGDt.setGuiaEmitidaSPG(true);
						
						guiaEmissaoSPGDt.setId(							rs1.getString("ISN_SPGA_GUIAS"));
						guiaEmissaoSPGDt.setNumeroGuiaCompleto(			rs1.getString("NUMR_GUIA"));						
						guiaEmissaoSPGDt.setInfoLocalCertidaoSPG( 		rs1.getString("INFO_LOCAL_CERTIDAO"));
						guiaEmissaoSPGDt.setValorAcao(					rs1.getString("VALR_ACAO"));
						
						if( rs1.getString("NOME_PRIMEIRO_AUTOR") != null ) {
							guiaEmissaoSPGDt.setNomePrimeiroAutor(			rs1.getString("NOME_PRIMEIRO_AUTOR").replaceAll("  ", ""));
						}
						guiaEmissaoSPGDt.setNumrCpfCgc(					rs1.getString("NUMR_CPF_CGC"));
						guiaEmissaoSPGDt.setTipoGuia(					rs1.getString("TIPO_GUIA"));
						guiaEmissaoSPGDt.setProcessoTipoCodNatureza(	rs1.getString("CODG_NATUREZA"));
						
						if(rs1.getString("NUMR_PROJUDI") != null && !rs1.getString("NUMR_PROJUDI").isEmpty()){
							guiaEmissaoSPGDt.setNumeroProcesso(			rs1.getString("NUMR_PROJUDI"));							
						}
						else {
							
							guiaEmissaoSPGDt2 = new ProcessoSPGNe().consultarNumeroProcessoCNJ(rs1.getString("ISN_SPGA_GUIAS"));
							
							// TERMINAR ESTE TRECHO
							
							
							
						}
					}
				}
			}		
		} // Fim do try
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}		
		return guiaEmissaoSPGDt;
	}
	
	/**
	 * Método para consultar as guias do SPG pelo número completo da guia (interior)
	 * @param String numeroGuiaCompleto
	 * @throws Exception
	 * @author fogomes
	 */
	public GuiaEmissaoDt consultarGuiaSPGPeloNumeroGuiaCompletoInterior(String numeroGuiaCompleto) throws Exception {
		
		GuiaEmissaoDt guiaEmissaoSPGDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		
		try {
			if( numeroGuiaCompleto != null ) {
				
				String sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, NUMR_PROJUDI, INFO_LOCAL_CERTIDAO, VALR_ACAO, NOME_PRIMEIRO_AUTOR, NUMR_CPF_CGC, TIPO_GUIA, ";
				sql += "NUMR_PROCESSO, CODG_NATUREZA ";
				sql += "FROM V_SPGAGUIAS_REM ";
				sql += "WHERE NUMR_GUIA = ? ";
								
				ps.adicionarString(numeroGuiaCompleto);
				
				rs1 = this.consultar(sql, ps);
								
				if(rs1 != null) {
					
					while(rs1.next()) {
						
						guiaEmissaoSPGDt = new GuiaEmissaoDt();
						
						guiaEmissaoSPGDt.setGuiaEmitidaSPG(true);
						guiaEmissaoSPGDt.setId(							rs1.getString("ISN_SPGA_GUIAS_REM"));
						guiaEmissaoSPGDt.setNumeroGuiaCompleto(			rs1.getString("NUMR_GUIA"));
						guiaEmissaoSPGDt.setNumeroProcesso(				rs1.getString("NUMR_PROJUDI"));
						guiaEmissaoSPGDt.setInfoLocalCertidaoSPG( 		rs1.getString("INFO_LOCAL_CERTIDAO"));
						guiaEmissaoSPGDt.setValorAcao(					rs1.getString("VALR_ACAO"));
						
						guiaEmissaoSPGDt.setNomePrimeiroAutor(			rs1.getString("NOME_PRIMEIRO_AUTOR"));
						guiaEmissaoSPGDt.setNumrCpfCgc(					rs1.getString("NUMR_CPF_CGC"));
						guiaEmissaoSPGDt.setTipoGuia(					rs1.getString("TIPO_GUIA"));
						guiaEmissaoSPGDt.setProcessoTipoCodNatureza(	rs1.getString("CODG_NATUREZA"));
					}
				} 
			}		
		} // Fim do try
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}		
		return guiaEmissaoSPGDt;
	}	
	
	/**
	 * Método que consulta se o isn existe nesta view.
	 * 
	 * @param String isnGuia
	 * @param String comarcaCodigo
	 * @return String isn
	 * @throws Exception
	 */
	public String consultarISNInfoRepasseByISNGuia(String isnGuia, String comarcaCodigo) throws Exception {
		String isn = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( isnGuia != null ) {
				
				String sql = "SELECT ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " FROM V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(comarcaCodigo) + " WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = ?";
				ps.adicionarLong(isnGuia);
				
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						
						String isnConsultada = rs1.getString("ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo));
						
						if( isnConsultada != null && isnConsultada.trim().equals(isnGuia) ) {
							isn = isnConsultada;
						}
						
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return isn;
	}
	
	/**
	 * Método para consultar a info_repasse pelo isn da guia.
	 * @param String isnGuia
	 * @return InfoRepasseSPGDt infoRepasseSPGDt
	 * @throws Exception
	 */
	public InfoRepasseSPGDt consultarInfoRepasseByISNGuia(String isnGuia, String comarcaCodigo) throws Exception {
		InfoRepasseSPGDt infoRepasseSPGDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( isnGuia != null ) {
				
				String sql = "SELECT * FROM V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(comarcaCodigo) + " WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo) + " = ?";
				ps.adicionarLong(isnGuia);
				
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					if(rs1.next()) {
						
						if( infoRepasseSPGDt == null ) {
							infoRepasseSPGDt = new InfoRepasseSPGDt();
						}
						
						infoRepasseSPGDt.setIsn(rs1.getString("ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo)));
						infoRepasseSPGDt.setCodgEscrivania(rs1.getString("CODG_ESCRIVANIA"));
						infoRepasseSPGDt.setPercRepasse(rs1.getString("PERC_REPASSE"));
						if( rs1.getString("DATA_REPASSE") != null ) {
							infoRepasseSPGDt.setDataRepasse(rs1.getString("DATA_REPASSE"));
							infoRepasseSPGDt.setDataRepasseTJDataHora(new TJDataHora(rs1.getDate("DATA_REPASSE")));
						}
						
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return infoRepasseSPGDt;
	}
	
	/**
	 * Método para consultar lista de info_repasse pelo isn da guia.
	 * @param String isnGuia
	 * @param String comarcaCodigo
	 * @return List<InfoRepasseSPGDt> listaInfoRepasseSPGDt
	 * @throws Exception
	 */
	public List<InfoRepasseSPGDt> consultarListaInfoRepasseByISNGuia(String isnGuia, String comarcaCodigo) throws Exception {
		List<InfoRepasseSPGDt> listaInfoRepasseSPGDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( isnGuia != null ) {
				
				String sql = "SELECT * FROM V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(comarcaCodigo) + " WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo) + " = ?";
				ps.adicionarLong(isnGuia);
				
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					if(rs1.next()) {
						
						if( listaInfoRepasseSPGDt == null ) {
							listaInfoRepasseSPGDt = new ArrayList<InfoRepasseSPGDt>();
						}
						
						InfoRepasseSPGDt infoRepasseSPGDt = new InfoRepasseSPGDt();
						
						infoRepasseSPGDt.setIsn(rs1.getString("ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo)));
						infoRepasseSPGDt.setCodgEscrivania(rs1.getString("CODG_ESCRIVANIA"));
						infoRepasseSPGDt.setPercRepasse(rs1.getString("PERC_REPASSE"));
						if( rs1.getString("DATA_REPASSE") != null ) {
							infoRepasseSPGDt.setDataRepasse(rs1.getString("DATA_REPASSE"));
							infoRepasseSPGDt.setDataRepasseTJDataHora(new TJDataHora(rs1.getDate("DATA_REPASSE")));
						}
						
						listaInfoRepasseSPGDt.add(infoRepasseSPGDt);
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaInfoRepasseSPGDt;
	}

	public List<String> consultarGuiasProjudiPagasNoDia(TJDataHora dataPagamento) throws Exception {
		List<String> listaGuiasPagasSerie50 = new ArrayList<String>();
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		ResultSetTJGO rs1 = null;
		try
		{
			
			String parametroInicial = dataPagamento.getDataHoraFormatadayyyyMMdd() + //DATA-RECOLHIMENTO 
					   "000" + //NUMR-BANCO
					   "0000" + //NUMR-AGENCIA
					   "0000"+ //NUMR-SUBLOTE
					   "00"; //NUMR-SEQ-DOCUMENTO
			
			String parametroFinal = dataPagamento.getDataHoraFormatadayyyyMMdd() + //DATA-RECOLHIMENTO 
					   "999" + //NUMR-BANCO
					   "9999" + //NUMR-AGENCIA
					   "9999"+ //NUMR-SUBLOTE
					   "99"; //NUMR-SEQ-DOCUMENTO
			
			String sql = "SELECT NUMR_DOCUMENTO, NUMR_SERIE_DOCUMENTO FROM V_SAJADOCUMENTOS WHERE CODG_DOCUMENTO >= ? AND CODG_DOCUMENTO <= ?";
			ps.adicionarString(parametroInicial);			
			ps.adicionarString(parametroFinal);
			
			rs1 = this.consultar(sql, ps);
			if(rs1 != null) {
				while(rs1.next()) {
					if(rs1.getString("NUMR_DOCUMENTO") != null && rs1.getString("NUMR_SERIE_DOCUMENTO") != null) {
						String numeroGuiaCompleto = rs1.getString("NUMR_DOCUMENTO").trim() + Funcoes.completarZeros(String.valueOf(Funcoes.StringToInt(rs1.getString("NUMR_SERIE_DOCUMENTO"))), 2).trim(); 
						if (Funcoes.isNumeroGuiaProjudiValido(numeroGuiaCompleto)) {
							listaGuiasPagasSerie50.add(numeroGuiaCompleto);	
						}	
					}					
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return  listaGuiasPagasSerie50;
	}
	
	public TJDataHora consultarDataDePagamentoGuia(String numeroGuiaCompleto) throws Exception {
		TJDataHora dataDePagamento = null;		
		String numeroGuiaSemSerie = null;
		String serieGuia = null;
		
		if (numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0) {
			numeroGuiaSemSerie = numeroGuiaCompleto.substring(0, numeroGuiaCompleto.length() - 2);
			serieGuia = numeroGuiaCompleto.substring(numeroGuiaCompleto.length() - 2);
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();		
			ResultSetTJGO rs1 = null;
			try
			{
				
				String sql = "SELECT CODG_DOCUMENTO FROM V_SAJADOCUMENTOS WHERE CODG_DOCUMENTOS_SUPER = ?";
				ps.adicionarString(Funcoes.completarZeros(numeroGuiaSemSerie, 9) + Funcoes.completarZeros(serieGuia, 3));
								
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					if(rs1.next()) {
						if(rs1.getString("CODG_DOCUMENTO") != null && rs1.getString("CODG_DOCUMENTO").trim().length() > 8) {
							String dataPagamentoAAAAMMDD = rs1.getString("CODG_DOCUMENTO").trim().substring(0, 8); 
							
							dataDePagamento = new TJDataHora();
							dataDePagamento.setDataaaaaMMdd(dataPagamentoAAAAMMDD);
						}					
					}
				}
			
			}
			finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
			}
		}		
		
		return dataDePagamento;
	}
	
	public boolean atualizaDataApresentacao(String isn, String comarcaCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " SET DATA_APRESENTACAO = "+Funcoes.BancoData(new TJDataHora().getDataFormatadaddMMyyyy())+" WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? AND DATA_APRESENTACAO IS NULL";
		ps.adicionarLong(isn);
		
		return executarUpdateDelete(sql, ps) > 0;
	}
	
	/**
	 * Método para consultar guia de processo digitalizado que ainda não tem vinculação com o PJD.
	 * @param String numeroProcessoSPG
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiasSPGProcesso(String numeroProcessoSPG) throws Exception {
		List<GuiaEmissaoDt> listaGuiasSPGDt = new ArrayList<GuiaEmissaoDt>();
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS";
			sql += " WHERE NUMR_PROJUDI IS NULL";
			sql += " AND NUMR_PROCESSO = " + numeroProcessoSPG;
												
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				while(rs1.next()) {
					GuiaEmissaoDt guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.GOIANIA, guiaSPGDt);
					listaGuiasSPGDt.add(guiaSPGDt);					
				}
			}
			
			sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += "NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS_REM";
			sql += " WHERE NUMR_PROJUDI IS NULL";
			sql += " AND NUMR_PROCESSO = " + numeroProcessoSPG;
							
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				while(rs1.next()) {
					GuiaEmissaoDt guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.APARECIDA_DE_GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.APARECIDA_DE_GOIANIA, guiaSPGDt);
					listaGuiasSPGDt.add(guiaSPGDt);
				}				
			}	
			
		}
		finally {
			//Fecha conexão e resultset
			if( rs1 != null ) {
				rs1.close();
			}
		}		
		
		return listaGuiasSPGDt;
	}
	
	/**
	 * Método para consultar guias parceladas de guia de referência.
	 * @param String numeroGuiaReferencia
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiasSPGParceladas(String numeroGuiaReferencia) throws Exception {
		List<GuiaEmissaoDt> listaGuiasSPGDt = new ArrayList<GuiaEmissaoDt>();
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try 
		{
			sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO,";
			sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS";
			sql += " WHERE NUMR_GUIA_REFERENCIA = " + numeroGuiaReferencia;
												
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				while(rs1.next()) {
					GuiaEmissaoDt guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.GOIANIA, guiaSPGDt);
					listaGuiasSPGDt.add(guiaSPGDt);					
				}
			}
			
			sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
			sql += "NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA";
			sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
			sql += " FROM V_SPGAGUIAS_REM";
			sql += " WHERE NUMR_GUIA_REFERENCIA = " + numeroGuiaReferencia;
							
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				while(rs1.next()) {
					GuiaEmissaoDt guiaSPGDt = new GuiaEmissaoDt();					
					this.associarGuiaEmissaoDtSPG(guiaSPGDt, rs1, ComarcaDt.APARECIDA_DE_GOIANIA);
					this.consultarItensGuiaEmissaoSPG(guiaSPGDt.getId(), ComarcaDt.APARECIDA_DE_GOIANIA, guiaSPGDt);
					listaGuiasSPGDt.add(guiaSPGDt);
				}				
			}	
			
		}
		finally {
			//Fecha conexão e resultset
			if( rs1 != null ) {
				rs1.close();
			}
		}		
		
		return listaGuiasSPGDt;
	}
	
	public boolean vinculeLocomocaoSPGAoProjudi(String isnGuia, boolean isCapital, int posicaoVetor, String idProjudi) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sufixoTabela = (isCapital ? "" : "_REM");
		
		String sql= "UPDATE V_SPGAGUIAS_LOCOMOCOES" + sufixoTabela + " SET ID_PROJUDI = ? WHERE ISN_SPGA_GUIAS"+ sufixoTabela + " = ? AND CNXARRAYCOLUMN = ?";
		ps.adicionarLong(idProjudi);
		ps.adicionarLong(isnGuia);
		ps.adicionarLong(posicaoVetor);
		
		return executarUpdateDelete(sql, ps) > 0;			
	}
	
	/**
	 * Método para consultar a porcentagem do último repasse feito da guia.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return String (número da porcentagem)
	 * @throws Exception
	 */
	public String consultarUltimaPorcentagemRepassadaInfoRepasse(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String retorno = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		try {
			if( guiaEmissaoDt != null && guiaEmissaoDt.getNumeroGuiaCompleto() != null && !guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() && !guiaEmissaoDt.getNumeroGuiaCompleto().equals("0") && !guiaEmissaoDt.getNumeroGuiaCompleto().equals("null") && Funcoes.StringToLong(guiaEmissaoDt.getNumeroGuiaCompleto()) > 0L ) {
			
				String sql = "SELECT PERC_REPASSE FROM V_SPGAGUIAS_INFO_REPASSE" + obtenhaSufixoNomeTabela(guiaEmissaoDt) +" WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = (SELECT MAX(ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + ") AS ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " FROM V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " WHERE NUMR_GUIA = ?) AND DATA_REPASSE IS NOT NULL ORDER BY DATA_REPASSE";
				ps.adicionarString(guiaEmissaoDt.getNumeroGuiaCompleto());
				
				rs1 = consultar(sql, ps);
				
				if(rs1 != null) {
					while(rs1.next()) {
						retorno = rs1.getString("PERC_REPASSE");
					}
				}
				
			}
		}
		finally {
			if( rs1 != null ) {
				rs1.close();
			}
		}
		
		return retorno;
	}
	
	public void retireDataApresentacao(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(guiaEmissaoDt) + " SET DATA_APRESENTACAO = NULL WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(guiaEmissaoDt) + " = ?";
		ps.adicionarLong(guiaEmissaoDt.getId());
		
		executarUpdateDelete(sql, ps);
	}

	public BoletoDt buscaBoletoPorNumero(String numeroGuiaCompleto) throws Exception {
		
		BoletoDt boleto = null;
		
		ResultSetTJGO rs1 = null;
		String sql = null;
		
		try 
		{
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() && !numeroGuiaCompleto.equals("0") && !numeroGuiaCompleto.equals("null") ) {
				sql = "SELECT ISN_SPGA_GUIAS, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
				sql += " NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO,";
				sql += " NUMR_PROCESSO, NUMR_PROJUDI, NUMR_GUIA_PRINCIPAL, TIPO_GUIA, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA,";
				sql += " INFO_SITUACAO_BOLETO, NUMR_BOLETO, DATA_EMIS_BOLETO, DATA_VENCIMENTO_BOLETO, VALR_BOLETO";
				sql += " ,URL_PDF_BOLETO, INFO_ID_PAGADOR_BOLETO, NOME_PAGADOR_BOLETO";
				sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
				sql += " FROM V_SPGAGUIAS";
				sql += " WHERE NUMR_GUIA = " + numeroGuiaCompleto;
										
				rs1 = consultar(sql, new PreparedStatementTJGO());
				if(rs1 != null && rs1.next()) {
					boleto = new BoletoDt();								
					this.associarGuiaEmissaoDtSPG(boleto, rs1, ComarcaDt.GOIANIA);	
					this.consultarItensGuiaEmissaoSPG(boleto.getId(), ComarcaDt.GOIANIA, boleto);
				}
				
				if (boleto == null) {
					sql = "SELECT ISN_SPGA_GUIAS_REM, NUMR_GUIA, DATA_EMISSAO, TIPO_GUIA, NUMR_GUIA_PRINCIPAL, DATA_VENC_GUIA, STAT_PAGAMENTO,";
					sql += "NOME_PRIMEIRO_AUTOR, NOME_PRIMEIRO_REU, CODG_OAB, CODG_NATUREZA, QTDE_CITACAO_CORREIO, VALR_ACAO, DATA_APRESENTACAO, NUMR_PROJUDI, NUMR_PROCESSO, NUMR_PROCESSO_PRINCIPAL, INFO_LOCAL_CERTIDAO, TIPO_PESSOA,";
					sql += " INFO_SITUACAO_BOLETO, NUMR_BOLETO, DATA_EMIS_BOLETO, DATA_VENCIMENTO_BOLETO, VALR_BOLETO";
					sql += " ,URL_PDF_BOLETO, INFO_ID_PAGADOR_BOLETO, NOME_PAGADOR_BOLETO";
					sql += " ,NUMR_GUIA_REFERENCIA, QTDE_PARCELAS, NUMR_PARCELA_ATUAL, INFO_PATENTE, TIPO_CERTIDAO, MODO_EMISSAO, DATA_INI_CERT, DATA_FIM_CERT, INFO_AREA ";
					sql += " FROM V_SPGAGUIAS_REM";
					sql += " WHERE NUMR_GUIA = " + numeroGuiaCompleto;
									
					rs1 = consultar(sql, new PreparedStatementTJGO());
					if(rs1 != null && rs1.next()) {
						boleto = new BoletoDt();	
						this.associarGuiaEmissaoDtSPG(boleto, rs1, ComarcaDt.APARECIDA_DE_GOIANIA);
						this.consultarItensGuiaEmissaoSPG(boleto.getId(), ComarcaDt.APARECIDA_DE_GOIANIA, boleto);
					}	
				}
				if (boleto != null && !associarDtBoleto(boleto, rs1))
					return null;
			}
			
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();
		}		
		
		return boleto;
	}

	private boolean associarDtBoleto(BoletoDt boleto, ResultSetTJGO rs1) throws Exception {
		String codSituacao = rs1.getString("INFO_SITUACAO_BOLETO");
		if (codSituacao == null || codSituacao.isEmpty())
			return false;
		boleto.setSituacaoBoleto(SituacaoBoleto.comCodigo(codSituacao));
		boleto.setNossoNumero(rs1.getString("NUMR_BOLETO"));
		boleto.setNumeroDocumento(rs1.getString("NUMR_GUIA"));
		boleto.setDataEmissaoBoleto(rs1.getString("DATA_EMIS_BOLETO"));
		boleto.setDataVencimentoBoleto(rs1.getString("DATA_VENCIMENTO_BOLETO"));
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
//		boleto.setObservacao1(rs1.getString("OBSERVACAO1_BOLETO"));
//		boleto.setObservacao2(rs1.getString("OBSERVACAO2_BOLETO"));
//		boleto.setObservacao3(rs1.getString("OBSERVACAO3_BOLETO"));
//		boleto.setObservacao4(rs1.getString("OBSERVACAO4_BOLETO"));
		return true;
	}

	public void salvar(BoletoDt boleto) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql= "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(boleto);

		Sql += " SET INFO_SITUACAO_BOLETO = ?";
		ps.adicionarLong(boleto.getSituacaoBoleto().getCodigo());
		
		Sql += " ,NUMR_BOLETO = ?";
		ps.adicionarString(boleto.getNossoNumero());
		
		Sql += " ,DATA_EMIS_BOLETO = ?";
		ps.adicionarDate(boleto.getDataEmissaoBoleto());
		
		Sql += " ,DATA_VENCIMENTO_BOLETO = ?";
		ps.adicionarDate(boleto.getDataVencimentoBoleto());
		
		Sql += " ,VALR_BOLETO = ?";
		ps.adicionarString(boleto.getValorBoleto());
		
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
		
		Sql += " WHERE ISN_SPGA_GUIAS"+ obtenhaSufixoNomeTabela(boleto) + " = ? ";
		ps.adicionarLong(boleto.getId());
		
		executarUpdateDelete(Sql, ps);
		
	}

	public void atualizarSituacaoBoleto(BoletoDt boleto) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String Sql = " UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(boleto) + " SET INFO_SITUACAO_BOLETO = ?";
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
	 * Método para ser utilizado no momento de cancelar uma guia no PJD. Em conversa com o Júnio Feitosa e 
	 * Marques (18/04/2018) ficou definido limpar os números de processos das guias no SPG para evitar 
	 * que a parte realize o pagamento de uma guia cancelada e depois ser realizada o rateio no SPG.
	 * 
	 * Alterado depois de conversa com o Leandro Prezotto.
	 * Agora será alterado INFO_INTERNET para 5 para identificar que a guia está cancelada no SPG.
	 * O valor original é 2 (guias do projudi série 50).
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param boolean alteraInfoInternet
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean limparNumerosProcessosGuiaSPG(GuiaEmissaoDt guiaEmissaoDt, boolean alteraInfoInternet) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS"+ obtenhaSufixoNomeTabela(guiaEmissaoDt) +" SET NUMR_PROJUDI = null, NUMR_PROCESSO = null, INFO_LOCAL_CERTIDAO = null ";
		
		if( alteraInfoInternet ) {
			sql += ", INFO_INTERNET = 5 ";
		}
		
		sql += "WHERE NUMR_GUIA = ? ";
		
		if( alteraInfoInternet ) {
			sql += " AND INFO_INTERNET = 2";
		}
		
		ps.adicionarLong(guiaEmissaoDt.getNumeroGuiaCompleto());
		
		return executarUpdateDelete(sql, ps) == 1;
	}
	
	public boolean atualizaGuiaVinculadaProcesso(String isnGuiaSPG, String numeroProcesso, String comarcaCodigo, String numeroGuiaCompleto) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " SET NUMR_PROJUDI = ? WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? ";
		
		sql += " AND NUMR_GUIA = ?";
		sql += " AND (NUMR_PROJUDI IS NULL OR NUMR_PROJUDI = 0)";
		
		ps.adicionarLong(numeroProcesso);
		ps.adicionarLong(isnGuiaSPG);
		ps.adicionarLong(numeroGuiaCompleto);
		
		return executarUpdateDelete(sql, ps) > 0;
	}
	
	public boolean atualizaGuiaVinculadaProcessoHibrido(String isnGuiaSPG, String numeroProcessoPJD, String numeroProcessoAntigoSPG, String comarcaCodigo, String numeroGuiaCompleto) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " SET NUMR_PROJUDI = ? WHERE ISN_SPGA_GUIAS" + obtenhaSufixoNomeTabela(comarcaCodigo) + " = ? ";
		
		sql += " AND NUMR_GUIA = ? ";
		sql += " AND (NUMR_PROJUDI IS NULL OR NUMR_PROJUDI = 0) ";
		sql += " AND NUMR_PROCESSO = ? ";
		
		ps.adicionarLong(numeroProcessoPJD);
		ps.adicionarLong(isnGuiaSPG);
		ps.adicionarLong(numeroGuiaCompleto);
		ps.adicionarLong(numeroProcessoAntigoSPG);
		
		return executarUpdateDelete(sql, ps) > 0;
	}
	
	public void inserirPagamentoRajadaGuiaSAJ(GuiaEmissaoDt guiaEmissaoDt, 
									          TJDataHora dataHoraGeracao, 
									          Double ValorPago) throws Exception {
		String numeroGuiaSemSerie = "";
		String serie = ""; 
		String cdgServentia = "";
		String cdgComarca = "0";
		if (guiaEmissaoDt.getNumeroGuiaCompleto() != null && guiaEmissaoDt.getNumeroGuiaCompleto().length() > 0) {
			numeroGuiaSemSerie = guiaEmissaoDt.getNumeroGuiaCompleto().substring(0, guiaEmissaoDt.getNumeroGuiaCompleto().length() - 2);
			serie = guiaEmissaoDt.getNumeroGuiaCompleto().substring(guiaEmissaoDt.getNumeroGuiaCompleto().length() - 2);
		}
		
		String sql = "INSERT INTO V_SAJADOCUMENTOS (NUMR_DOCUMENTO, " +
				   "NUMR_SERIE_DOCUMENTO, " +
				   "TIPO_DOCUMENTO, " +
				   "CODG_SERVENTIA, " +
				   "CODG_COMARCA, " +
				   "CODG_NATUREZA, " +
				   "VALR_CAUSA, " +
				   "TIPO_CUSTAS, " +
				   "CODG_DOCUMENTO) VALUES ( ";

		//NUMR_DOCUMENTO
		sql += Funcoes.completarZeros(numeroGuiaSemSerie, 9) + ","; //informar o número da guia
		
		//NUMR_SERIE_DOCUMENTO
		sql += "'" + Funcoes.completarZeros(serie, 3) + "',"; //informar o número de serie da guia	
		
		//TIPO_DOCUMENTO
		sql += "1,'"; //valor 1 (GRJ - guia de recolhimento judicial)	
		
		//CODG_SERVENTIA
		sql += cdgServentia + "',";
		
		//CODG_COMARCA
		sql += cdgComarca + ",";
		
		//CODG_NATUREZA
		ProcessoTipoDt processoTipoDt = null;
		if(guiaEmissaoDt.getProcessoDt() != null && guiaEmissaoDt.getProcessoDt().getId_ProcessoTipo() != null && guiaEmissaoDt.getProcessoDt().getId_ProcessoTipo().trim().length() > 0) {
			processoTipoDt = new ProcessoTipoNe().consultarId(guiaEmissaoDt.getProcessoDt().getId_ProcessoTipo());			
		} else if (guiaEmissaoDt.getId_ProcessoTipo() != null && guiaEmissaoDt.getId_ProcessoTipo().trim().length() > 0) {
			processoTipoDt = new ProcessoTipoNe().consultarId(guiaEmissaoDt.getId_ProcessoTipo());				
		}	
		
		if (processoTipoDt != null && processoTipoDt.getProcessoTipoCodigo() != null) {
			sql += Funcoes.completarZeros(processoTipoDt.getProcessoTipoCodigo(),3) + ",";
		} else if (guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().trim().length() == 3) {
			sql += guiaEmissaoDt.getNaturezaSPGCodigo() + ",";
		} else {
			sql += "000,";
		}
		
		//VALR_CAUSA
		sql += ValorPago.toString().replace(",", "") + ","; //informar o valor recolhido no banco
		
		//TIPO_CUSTAS
		sql += "3,"; 
		
		//CODG_DOCUMENTO
		sql += "19000101" + //informar 19000101
				dataHoraGeracao.getDataHoraFormatadayyyyMMdd() + //informar a data em que foi gerado o arquivo de rajada no formato(AAAAMMDD) - AAAA=ano  MM=mês  DD-=dia
				dataHoraGeracao.getDataFormatadaHHmm() + //informar a hora em que foi gerado o arquivo de rajada no formato(HHMM) -  HH=hora  MM=minuto
			   "0"; //a posição restante deve ser preenchida com 0(zero)
		
		sql += ")";
		
		executarComando(sql);
	}
	
	public String obtenhaCodigoDocumento(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String numeroGuiaSemSerie = "";
		String serie = ""; 
		if (guiaEmissaoDt.getNumeroGuiaCompleto() != null && guiaEmissaoDt.getNumeroGuiaCompleto().length() > 0) {
			numeroGuiaSemSerie = guiaEmissaoDt.getNumeroGuiaCompleto().substring(0, guiaEmissaoDt.getNumeroGuiaCompleto().length() - 2);
			serie = guiaEmissaoDt.getNumeroGuiaCompleto().substring(guiaEmissaoDt.getNumeroGuiaCompleto().length() - 2);
		}		
		return obtenhaCodigoDocumento(numeroGuiaSemSerie, serie);
	}
	
	private String obtenhaCodigoDocumento(String numeroGuiaSemSerie, String serie) throws Exception {
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try 
		{
			ps.adicionarString(Funcoes.completarZeros(numeroGuiaSemSerie, 9) + Funcoes.completarZeros(serie, 3));
			//CODG_DOCUMENTOS_SUPER é um campo descritor no adabas (indice), tornando as consultar mais rápidas...;
			sql = "SELECT CODG_DOCUMENTO FROM V_SAJADOCUMENTOS WHERE CODG_DOCUMENTOS_SUPER = ?";
			rs1 = consultar(sql, ps);
			if(rs1 != null ) {
				if(rs1.next()) {
					return rs1.getString("CODG_DOCUMENTO");
				}				
			}
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();			
		}
		
		return null;
	}
	
	public void excluaSajaDocumento(String isnGuiaSAJ) throws Exception {
		executarComando("DELETE FROM V_SAJA_INFODOCUMENTOS WHERE ISN_SAJA_DOCUMENTOS = "+ isnGuiaSAJ); //Exclui o registro do pagamento da guia, caso exista...
	}
	
	public SajaDocumentoDt obtenhaSajaDocumento(String numeroGuiaSemSerie, String serie) throws Exception {
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		SajaDocumentoDt retorno = null;
		try 
		{
			ps.adicionarString(Funcoes.completarZeros(numeroGuiaSemSerie, 9) + Funcoes.completarZeros(serie, 3));
			//CODG_DOCUMENTOS_SUPER é um campo descritor no adabas (indice), tornando as consultar mais rápidas...;
			sql = "SELECT ISN_SAJA_DOCUMENTOS, NUMR_DOCUMENTO, NUMR_SERIE_DOCUMENTO, TIPO_DOCUMENTO, CODG_SERVENTIA, CODG_NATUREZA, VALR_CAUSA, TIPO_CUSTAS, CODG_DOCUMENTO FROM V_SAJADOCUMENTOS WHERE CODG_DOCUMENTOS_SUPER = ?";
			rs1 = consultar(sql, ps);
			if(rs1 != null && rs1.next()) {
				retorno = new SajaDocumentoDt();
				retorno.setId(rs1.getString("ISN_SAJA_DOCUMENTOS"));
				retorno.setNumeroDocumento(rs1.getString("NUMR_DOCUMENTO"));
				retorno.setNumeroSerieDocumento(rs1.getString("NUMR_SERIE_DOCUMENTO"));
				retorno.setTipoDeDocumento(rs1.getString("TIPO_DOCUMENTO"));
				retorno.setCodigoServentia(rs1.getString("CODG_SERVENTIA"));
				retorno.setCodigoNatureza(rs1.getString("CODG_NATUREZA"));
				retorno.setValorCausa(rs1.getString("VALR_CAUSA"));
				retorno.setTipoCustas(rs1.getString("TIPO_CUSTAS"));								
				retorno.setCodigoDocumento(rs1.getString("CODG_DOCUMENTO"));
			}
		} finally {
			//Fecha conexão e resultset
			if( rs1 != null ) rs1.close();			
		}	
		
		return retorno;
	}
	
	/**
	 * Método para desfazer cancelamento de guia.
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
	public boolean desfazerCancelamentoGuiaSPG(GuiaEmissaoDt guiaEmissaoDt, String numeroProcessoDigitoAno) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql= "UPDATE V_SPGAGUIAS"+ obtenhaSufixoNomeTabela(guiaEmissaoDt) +" SET NUMR_PROJUDI = '"+numeroProcessoDigitoAno+"', INFO_INTERNET = 2 WHERE NUMR_GUIA = ? AND NUMR_PROJUDI IS NULL";
		
		ps.adicionarLong(guiaEmissaoDt.getNumeroGuiaCompleto());
		
		return executarUpdateDelete(sql, ps) == 1;
	}
	
//	/**
//	 * Atualia os dados da certidão de prática forense no SPG para ser possível 
//	 * recuperar as informações dos processos do SPG
//	 * 
//	 * @param certidaoGuiaDt
//	 * @throws Exception
//	 */
//	public void atualizarDadosCertidaoPraticaForense(CertidaoGuiaDt certidaoGuiaDt) throws Exception {
//		if (certidaoGuiaDt == null || 
//			certidaoGuiaDt.getGuiaEmissaoDt() == null || 
//			!certidaoGuiaDt.getGuiaEmissaoDt().isSerie50()) return;
//		
//		if (certidaoGuiaDt.isDadosCertidaoGravadosSPG()) return;
//		
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		
//		String sql= "UPDATE V_SPGAGUIAS"+ obtenhaSufixoNomeTabela(certidaoGuiaDt.getGuiaEmissaoDt());
//		sql +=      " SET NOME_PRIMEIRO_AUTOR = ? "; ps.adicionarString(certidaoGuiaDt.getNome());
//		if (certidaoGuiaDt.getNaturalidade() != null && certidaoGuiaDt.getNaturalidade().trim().length() > 0) {
//			sql +=		" , NOME_NATURALIDADE = ? "; ps.adicionarString(certidaoGuiaDt.getNaturalidade());				
//		}
//		sql +=      " , TIPO_GUIA = ? "; ps.adicionarLong(GuiaTipoDt.ID_INFO_CERTIDAO_SPG_PRATICA_FORENSE);
//		sql +=      " , TIPO_CERTIDAO = ? "; ps.adicionarString(GuiaTipoDt.ID_INFO_CERTIDAO_SPG_PRATICA_FORENSE);
//		sql +=      " , DATA_INI_CERT = ? "; ps.adicionarDate(certidaoGuiaDt.getDataTimeInicial());
//		sql +=	    " , DATA_FIM_CERT = ? "; ps.adicionarDate(certidaoGuiaDt.getDataTimeFinal());
//		if (certidaoGuiaDt.getId_EstadoCivil() != null && certidaoGuiaDt.getId_EstadoCivil().trim().length() > 0) {
//			sql +=	    " , CODG_ESTADO_CIVIL = ? ";
//			if (certidaoGuiaDt.getId_EstadoCivil().equalsIgnoreCase(EstadoCivilDt.ID_CASADO)) {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_CASADO);
//			} else if (certidaoGuiaDt.getId_EstadoCivil().equalsIgnoreCase(EstadoCivilDt.ID_DIVORCIADO)) {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_DIVORCIADO);
//			} else if (certidaoGuiaDt.getId_EstadoCivil().equalsIgnoreCase(EstadoCivilDt.ID_SEPARADO_JUDICIALMENTE)) {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_SEPARADO_JUDICIALMENTE);
//			} else if (certidaoGuiaDt.getId_EstadoCivil().equalsIgnoreCase(EstadoCivilDt.ID_SOLTEIRO)) {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_SOLTEIRO);
//			} else if (certidaoGuiaDt.getId_EstadoCivil().equalsIgnoreCase(EstadoCivilDt.ID_UNIAO_ESTAVEL)) {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_UNIAO_ESTAVEL);
//			} else if (certidaoGuiaDt.getId_EstadoCivil().equalsIgnoreCase(EstadoCivilDt.ID_VIUVO)) {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_VIUVO);
//			} else if (certidaoGuiaDt.getId_EstadoCivil().equalsIgnoreCase(EstadoCivilDt.ID_DESQUITADO)) {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_DIVORCIADO);
//			} else {
//				ps.adicionarString(EstadoCivilDt.ID_SPG_SOLTEIRO);
//			}
//		}
//		if (certidaoGuiaDt.getSexo() != null && certidaoGuiaDt.getSexo().trim().length() > 0) {
//			sql +=	    " , INFO_SEXO = ? "; ps.adicionarString(certidaoGuiaDt.getSexo());
//		}
//		if (certidaoGuiaDt.getCpf() != null && certidaoGuiaDt.getCpf().trim().length() > 0) {
//			sql +=	    " , NUMR_CPF_CGC = ? "; ps.adicionarString(Funcoes.obtenhaSomenteNumeros(certidaoGuiaDt.getCpf()));			
//		}
//		sql +=	    " , CODG_OAB = ? "; ps.adicionarString(Funcoes.completarEspacosDireita(certidaoGuiaDt.getOabNumero(), 7) + certidaoGuiaDt.getOabUf());
//		sql +=	    " , INFO_ORGAO_EXPEDIDOR = 2 ";
//		if (certidaoGuiaDt.getRg() != null && certidaoGuiaDt.getRg().trim().length() > 0) {
//			sql +=	    " , NUMR_IDENTIDADE = ? "; ps.adicionarString(Funcoes.obtenhaSomenteNumeros(certidaoGuiaDt.getRg()));
//		}		
//		sql +=	    " , INFO_AREA = ? "; ps.adicionarString(certidaoGuiaDt.getInfoArea());
//		sql +=      " , MODO_EMISSAO = " + (certidaoGuiaDt.getTipo().equalsIgnoreCase("Quantitativa") ? "'Q'" : "'N'"); //Quantitativo ou Normal
//		sql +=	    " WHERE NUMR_GUIA = ? "; ps.adicionarLong(certidaoGuiaDt.getGuiaEmissaoDt().getNumeroGuiaCompleto());
//		
//		executarUpdateDelete(sql, ps);
//	}
}