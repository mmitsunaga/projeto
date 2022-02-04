package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;

import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaItemPs extends GuiaItemPsGen {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4692889099823870596L;

	public GuiaItemPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Consulta os GuiaItem de uma Guia Emitida.
	 * @param GuiaEmissaoDt
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultaItensGuia(GuiaEmissaoDt guiaEmissaoDtParam) throws Exception {
		List<GuiaItemDt> listaItensGuia = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM projudi.VIEW_GUIA_ITEM WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(guiaEmissaoDtParam.getId());
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaItensGuia == null )
						listaItensGuia = new ArrayList<GuiaItemDt>();
					
					GuiaItemDt guiaItemDt = new GuiaItemDt();					
					this.associarDt(guiaItemDt, rs1);					
					listaItensGuia.add(guiaItemDt);
				}
			}
		
		}
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return listaItensGuia;
	}
	
	/**
	 * Consulta os GuiaItem de uma Guia Emitida em uma Dia específico.
	 * @param String dataEmissaoGuia
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List consultaItensGuia(String dataEmissaoGuia) throws Exception {
		List listaItensGuia = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_ITEM WHERE DATA_EMIS = ? ";
		ps.adicionarDate(dataEmissaoGuia);		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaItensGuia == null )
						listaItensGuia = new ArrayList();
					
					GuiaItemDt guiaItemDt = new GuiaItemDt();
					this.associarDt(guiaItemDt, rs1);
					listaItensGuia.add(guiaItemDt);
				}
			}
		
		}
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return listaItensGuia;
	}
	
	
	/**
	 * Consulta os GuiaItem de uma Guia Emitida.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return List listaExcluirIdArrecadacaoCusta
	 * @throws Exception
	 */
	public List consultaItensGuia(GuiaEmissaoDt guiaEmissaoDt, List listaExcluirIdArrecadacaoCusta) throws Exception {
		List listaItensGuia = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM projudi.VIEW_GUIA_ITEM WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(guiaEmissaoDt.getId());
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaItensGuia == null )
						listaItensGuia = new ArrayList();
					
					GuiaItemDt guiaItemDt = new GuiaItemDt();
					this.associarDt(guiaItemDt, rs1);
					listaItensGuia.add(guiaItemDt);
				}
			}
		
		}
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return listaItensGuia;
	}
	
	/**
	 * Consulta os GuiaItem de uma Guia Emitida.
	 * @param GuiaEmissaoDt
	 * @param String idArrecadacaoCusta
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List consultaItensGuia(GuiaEmissaoDt guiaEmissaoDt, String idArrecadacaoCusta) throws Exception {
		List listaItensGuia = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM projudi.VIEW_GUIA_ITEM WHERE ID_GUIA_EMIS = ? AND ID_ARRECADACAO_CUSTA = ? ";
		ps.adicionarLong(guiaEmissaoDt.getId());
		ps.adicionarLong(ArrecadacaoCustaDt.CUSTAS);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaItensGuia == null )
						listaItensGuia = new ArrayList();
					
					GuiaItemDt guiaItemDt = new GuiaItemDt();
					this.associarDt(guiaItemDt, rs1);
					listaItensGuia.add(guiaItemDt);
				}
			}
		
		}
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return listaItensGuia;
	}
	
	/**
	 * Método para salvar lista de itens de guia calculados.
	 * @param GuiaItemDt guiaItemDt
	 * @param String idGuiaEmissao
	 * @throws Exception
	 */
	public void salvar(GuiaItemDt guiaItemDt, String idGuiaEmissao, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String codg_oficial = null;
		if( guiaItemDt.getCodigoOficial() != null){
			codg_oficial = guiaItemDt.getCodigoOficial();
		}
		if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA)) ) {
			//sql += " , " + guiaEmissaoDt.getCodigoOficialSPGAvaliador();
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGAvaliador();
		}
		if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.PREGAO_PRACAO_LEILAO)) ) {
			//sql += " , " + guiaEmissaoDt.getCodigoOficialSPGLeilao();
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGLeilao();
		}
		if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) ) {
			//sql += " , " + guiaEmissaoDt.getCodigoOficialSPGLocomocao();
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGLocomocao();
		}
		if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL)) ) {
			//sql += " , " + guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada();
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada();
		}
		if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTA_PENHORA)) ) {
			//sql += " , " + guiaEmissaoDt.getCodigoOficialSPGPenhora();
			codg_oficial = guiaEmissaoDt.getCodigoOficialSPGPenhora();
		}
		
		String Sql;
		Sql= "INSERT INTO projudi.GUIA_ITEM ( ";
		Sql+= "GUIA_ITEM " ;
		Sql+= ",ID_GUIA_EMIS " ;
		Sql+= ",ID_CUSTA " ;
		Sql+= ",GUIA_ITEM_CODIGO " ;
		Sql+= ",QUANTIDADE " ;
		Sql+= ",VALOR_CALCULADO " ;
		Sql+= ",VALOR_REFERENCIA " ;
		Sql+= ",PARCELAS " ;
		Sql+= ",PARCELA_CORRENTE " ;
		Sql+= ",ID_ARRECADACAO_CUSTA_GENERICA " ;
		Sql+= ",ID_GUIA_ITEM_VINCULADO " ;			
		
		if( codg_oficial != null && codg_oficial.length() > 0 ) {
			Sql += ",CODIGO_OFICIAL";
		}
		
		if( guiaItemDt.getValorCalculadoOriginal() != null && guiaItemDt.getValorCalculadoOriginal().length() > 0 ) {
			Sql += ",VALOR_CALCULADO_ORIGINAL";
		}
		
		Sql+= ") ";
		
		Sql+= " Values ";
		
							
		Sql += "( ? ";
		
 		if (guiaItemDt.getGuiaItem() != null && guiaItemDt.getGuiaItem().length()>0)	 			
 			ps.adicionarString(guiaItemDt.getGuiaItem());
 		else
 			ps.adicionarStringNull();
 		
 		
		Sql+= ", ?";
		ps.adicionarLong(idGuiaEmissao);
		
		
		Sql+= ", ?";
		ps.adicionarLong(guiaItemDt.getCustaDt().getId());
		
		Sql+= ", ?";
		if (guiaItemDt.getGuiaItemCodigo() != null && guiaItemDt.getGuiaItemCodigo().length()>0)				
			ps.adicionarLong(guiaItemDt.getGuiaItemCodigo());
		else
			ps.adicionarLongNull();
		
		Sql+= ", ?";
		if (guiaItemDt.getQuantidade() != null && guiaItemDt.getQuantidade().length()>0)				
			ps.adicionarDecimal(guiaItemDt.getQuantidadeBanco());
		else
			ps.adicionarLong(1);
		
		
		Sql+= ", ?";
		ps.adicionarDecimal(Funcoes.FormatarDecimal(guiaItemDt.getValorCalculado()));
		
		
		Sql+= ", ?";
		ps.adicionarDecimal(Funcoes.FormatarDecimal(guiaItemDt.getValorReferencia()));
		
		Sql+= ", ?";
		if (guiaItemDt.getParcelas() != null && guiaItemDt.getParcelas().length()>0)				
			ps.adicionarLong(guiaItemDt.getParcelas());
		else
			ps.adicionarLongNull();	
		
		Sql+= ", ?";
		if (guiaItemDt.getParcelaCorrente().length()>0)				
			ps.adicionarLong(guiaItemDt.getParcelaCorrente());
		else
			ps.adicionarLongNull();
		
		Sql+= ", ?";
		if (guiaItemDt.getId_ArrecadacaoCustaGenerica() != null && guiaItemDt.getId_ArrecadacaoCustaGenerica().length()>0)				
			ps.adicionarLong(Funcoes.StringToInt(guiaItemDt.getId_ArrecadacaoCustaGenerica()));
		else
			ps.adicionarLongNull();
		
		Sql+= ", ?";
		if (guiaItemDt.getGuiaItemVinculadoDt() != null && guiaItemDt.getGuiaItemVinculadoDt().getId() != null &&  guiaItemDt.getGuiaItemVinculadoDt().getId().trim().length()>0)				
			ps.adicionarLong(Funcoes.StringToInt(guiaItemDt.getGuiaItemVinculadoDt().getId()));
		else
			ps.adicionarLongNull();
		
		if( codg_oficial != null && codg_oficial.length() > 0) {
			Sql+= ", ?";
			ps.adicionarLong(codg_oficial);				
		}
		
		if( guiaItemDt.getValorCalculadoOriginal() != null && guiaItemDt.getValorCalculadoOriginal().length() > 0 ) {
			Sql+= ", ?";
			ps.adicionarDecimal(Funcoes.FormatarDecimal(guiaItemDt.getValorCalculadoOriginal()));
		}
		
		Sql+=")";	
		
		guiaItemDt.setId(executarInsert(Sql, "ID_GUIA_ITEM", ps));
	}
	
	/**
	 * Método para excluir Itens de Guia de um único Id de Guia Emitida.
	 * @param String idGuiaEmissao
	 * @throws Exception
	 */
	public void excluirItensDaGuiaEmissao(String idGuiaEmissao) throws Exception { 

		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "DELETE FROM projudi.GUIA_ITEM WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(idGuiaEmissao);

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método para consultar o total das guias.
	 * 
	 * @param List listaIdGuias
	 * @return String
	 * @throws Exception
	 */
	public String consultarTotalGuias(List listaIdGuias) throws Exception {
		String retorno = "0.00";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		if (listaIdGuias != null && listaIdGuias.size() > 0) {
			ResultSetTJGO rs1 = null;
			
			String sql = "SELECT SUM(valor_calculado) AS TOTAL FROM PROJUDI.GUIA_ITEM ";
			for( int i = 0; i < listaIdGuias.size(); i++ ) {
				if (i == 0) sql += " WHERE ID_GUIA_EMIS = ?";
				else sql += " OR ID_GUIA_EMIS = ?";
				ps.adicionarLong(listaIdGuias.get(i).toString());
			}
			
			try{
				rs1 = this.consultar(sql, ps);
				if( rs1 != null && rs1.next())
					retorno = rs1.getString("TOTAL");
			
			}
			finally{
	             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
	        }
		}
		
		return retorno;
	}
	
	@Override
	protected void associarDt( GuiaItemDt Dados, ResultSetTJGO rs1)  throws Exception {
		Dados.setId(rs1.getString("ID_GUIA_ITEM"));
		Dados.setGuiaItem(rs1.getString("GUIA_ITEM"));
		Dados.setId_GuiaEmissao( rs1.getString("ID_GUIA_EMIS"));
		Dados.setId_Custa( rs1.getString("ID_CUSTA"));
		Dados.setCusta( rs1.getString("CUSTA"));
		Dados.setGuiaItemCodigo( rs1.getString("GUIA_ITEM_CODIGO"));
		Dados.setQuantidade( rs1.getString("QUANTIDADE"));
		Dados.setValorCalculado( rs1.getString("VALOR_CALCULADO"));
		Dados.setValorCalculadoOriginal( rs1.getString("VALOR_CALCULADO_ORIGINAL"));
		Dados.setValorReferencia( rs1.getString("VALOR_REFERENCIA"));
		Dados.setParcelas( rs1.getString("PARCELAS"));
		Dados.setParcelaCorrente( rs1.getString("PARCELA_CORRENTE"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setCodigoOficial(rs1.getString("CODIGO_OFICIAL"));
		Dados.setId_ArrecadacaoCustaGenerica(rs1.getString("ID_ARRECADACAO_CUSTA_GENERICA"));
		
		CustaDt custaDt = new CustaDt();
		custaDt.setId(rs1.getString("ID_CUSTA"));
		custaDt.setCusta(rs1.getString("CUSTA"));
		custaDt.setCodigoArrecadacao(rs1.getString("CODIGO_ARRECADACAO"));
		custaDt.setArrecadacaoCusta(rs1.getString("ARRECADACAO_CUSTA"));
		custaDt.setId_ArrecadacaoCusta(rs1.getString("ID_ARRECADACAO_CUSTA"));
		custaDt.setCodigoRegimento(rs1.getString("CODIGO_REGIMENTO"));
		custaDt.setReferenciaCalculo(rs1.getString("REFERENCIA_CALCULO"));
		Dados.setCustaDt(custaDt);
		
		GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
		guiaEmissaoDt.setId(rs1.getString("ID_GUIA_EMIS"));
		guiaEmissaoDt.setId_Processo(rs1.getString("ID_PROC"));
		guiaEmissaoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
		guiaEmissaoDt.setId_Serventia(rs1.getString("ID_SERV"));
		guiaEmissaoDt.setId_Comarca(rs1.getString("ID_COMARCA"));
		guiaEmissaoDt.setId_GuiaStatus(rs1.getString("ID_GUIA_STATUS"));
		guiaEmissaoDt.setDataRecebimento(rs1.getString("DATA_RECEBIMENTO"));
		guiaEmissaoDt.setDataEmissao(rs1.getString("DATA_EMIS"));
		guiaEmissaoDt.setDataVencimento(rs1.getString("DATA_VENCIMENTO"));
		guiaEmissaoDt.setNumeroGuiaCompleto(rs1.getString("NUMERO_GUIA_COMPLETO"));
		Dados.setGuiaEmissaoDt(guiaEmissaoDt);
		
		GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
		guiaModeloDt.setId(rs1.getString("ID_GUIA_MODELO"));
		guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
		
		if (rs1.getString("ID_GUIA_ITEM_VINCULADO") != null && rs1.getString("ID_GUIA_ITEM_VINCULADO").trim().length() > 0) {
			GuiaItemDt guiaItemVinculadoDt = new GuiaItemDt();
			
			guiaItemVinculadoDt.setId(rs1.getString("ID_GUIA_ITEMV"));
			guiaItemVinculadoDt.setGuiaItem(rs1.getString("GUIA_ITEMV"));
			guiaItemVinculadoDt.setId_GuiaEmissao( rs1.getString("ID_GUIA_EMISV"));
			guiaItemVinculadoDt.setId_Custa( rs1.getString("ID_CUSTAV"));
			guiaItemVinculadoDt.setCusta( rs1.getString("CUSTAV"));
			guiaItemVinculadoDt.setGuiaItemCodigo( rs1.getString("GUIA_ITEM_CODIGOV"));
			guiaItemVinculadoDt.setQuantidade( rs1.getString("QUANTIDADEV"));
			guiaItemVinculadoDt.setValorCalculado( rs1.getString("VALOR_CALCULADOV"));
			guiaItemVinculadoDt.setValorCalculadoOriginal( rs1.getString("VALOR_CALCULADO_ORIGINALV"));
			guiaItemVinculadoDt.setValorReferencia( rs1.getString("VALOR_REFERENCIAV"));
			guiaItemVinculadoDt.setParcelas( rs1.getString("PARCELASV"));
			guiaItemVinculadoDt.setParcelaCorrente( rs1.getString("PARCELA_CORRENTEV"));
			guiaItemVinculadoDt.setCodigoTemp( rs1.getString("CODIGO_TEMPV"));
			guiaItemVinculadoDt.setCodigoOficial(rs1.getString("CODIGO_OFICIALV"));
			guiaItemVinculadoDt.setId_ArrecadacaoCustaGenerica(rs1.getString("ID_ARRECADACAO_CUSTA_GENERICAV"));
			
			CustaDt custaVinculadoDt = new CustaDt();
			custaVinculadoDt.setId(rs1.getString("ID_CUSTAV"));
			custaVinculadoDt.setCusta(rs1.getString("CUSTAV"));
			custaVinculadoDt.setCodigoArrecadacao(rs1.getString("CODIGO_ARRECADACAOV"));
			custaVinculadoDt.setArrecadacaoCusta(rs1.getString("ARRECADACAO_CUSTAV"));
			custaVinculadoDt.setId_ArrecadacaoCusta(rs1.getString("ID_ARRECADACAO_CUSTAV"));
			custaVinculadoDt.setCodigoRegimento(rs1.getString("CODIGO_REGIMENTOV"));
			custaVinculadoDt.setReferenciaCalculo(rs1.getString("REFERENCIA_CALCULOV"));
			guiaItemVinculadoDt.setCustaDt(custaVinculadoDt);
			
			guiaItemVinculadoDt.setGuiaEmissaoDt(guiaEmissaoDt);
						
			Dados.setGuiaItemVinculadoDt(guiaItemVinculadoDt);
		}
	}
	
	/**
	 * Método para consultar itens de guias parceladas que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasParcelasAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_ITEM ";
		sql += " WHERE ID_GUIA_EMIS IN (";
		sql += " 	SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS ";
		sql += " 	WHERE ID_PROC = ?";
		sql += " 	AND ID_GUIA_EMIS_REFERENCIA IS NOT NULL";
		sql += " 	AND ID_GUIA_STATUS IN (?, ?)";
		sql += " )";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		
		ResultSetTJGO rs1 = null;
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				rs1 = this.consultar(sql, ps);
				if( rs1 != null ) {
					
					GuiaItemDt guiaItemDt = null;
					
					while( rs1.next() ) {
						if( listaGuiaItemDt == null ) {
							listaGuiaItemDt = new ArrayList<GuiaItemDt>();
						}
						guiaItemDt = new GuiaItemDt();
						
						associarDt(guiaItemDt, rs1);
						
						listaGuiaItemDt.add(guiaItemDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para consultar itens de guias de serviços que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasServicosAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_ITEM ITEM";
		sql += " WHERE ITEM.ID_GUIA_EMIS IN (";
		sql += " 	SELECT G1.ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS G1";
		sql += " 	WHERE G1.ID_PROC = ?";
		sql += " 	AND G1.ID_GUIA_STATUS IN (?, ?)";
		sql += " 	AND G1.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?)";
		sql += " 	AND G1.TIPO_GUIA_DESCONTO_PARCELAMENT IS NULL ";
		sql += "    AND NOT EXISTS ( SELECT 1 FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? and ID_GUIA_EMIS_REFERENCIA = G1.ID_GUIA_EMIS )";
		sql += " )";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		ps.adicionarLong(GuiaTipoDt.ID_SERVICOS);
		ps.adicionarLong(idProcesso);
		
		ResultSetTJGO rs1 = null;
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				rs1 = this.consultar(sql, ps);
				if( rs1 != null ) {
					
					GuiaItemDt guiaItemDt = null;
					
					while( rs1.next() ) {
						if( listaGuiaItemDt == null ) {
							listaGuiaItemDt = new ArrayList<GuiaItemDt>();
						}
						guiaItemDt = new GuiaItemDt();
						
						associarDt(guiaItemDt, rs1);
						
						listaGuiaItemDt.add(guiaItemDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para consultar itens de guias complementares que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasComplementaresAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_ITEM ITEM";
		sql += " WHERE ITEM.ID_GUIA_EMIS IN (";
		sql += " 	SELECT G1.ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS G1";
		sql += " 	WHERE G1.ID_PROC = ?";
		sql += " 	AND G1.ID_GUIA_STATUS IN (?, ?)";
		sql += " 	AND G1.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?, ?) )";
		sql += " 	AND G1.TIPO_GUIA_DESCONTO_PARCELAMENT IS NULL ";
		sql += "    AND NOT EXISTS ( SELECT 1 FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? and ID_GUIA_EMIS_REFERENCIA = G1.ID_GUIA_EMIS )";
		sql += " )";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		ps.adicionarLong(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU);
		ps.adicionarLong(GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU);
		ps.adicionarLong(idProcesso);
		
		ResultSetTJGO rs1 = null;
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				rs1 = this.consultar(sql, ps);
				if( rs1 != null ) {
					
					GuiaItemDt guiaItemDt = null;
					
					while( rs1.next() ) {
						if( listaGuiaItemDt == null ) {
							listaGuiaItemDt = new ArrayList<GuiaItemDt>();
						}
						guiaItemDt = new GuiaItemDt();
						
						associarDt(guiaItemDt, rs1);
						
						listaGuiaItemDt.add(guiaItemDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para consultar itens de guias complementares que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasGRSGenericaAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_ITEM ITEM";
		sql += " WHERE ITEM.ID_GUIA_EMIS IN (";
		sql += " 	SELECT G1.ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS G1";
		sql += " 	WHERE G1.ID_PROC = ?";
		sql += " 	AND G1.ID_GUIA_STATUS IN (?, ?)";
		sql += " 	AND G1.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?) )";
		sql += " 	AND G1.TIPO_GUIA_DESCONTO_PARCELAMENT IS NULL ";
		sql += "    AND NOT EXISTS ( SELECT 1 FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? and ID_GUIA_EMIS_REFERENCIA = G1.ID_GUIA_EMIS )";
		sql += " )";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		ps.adicionarLong(GuiaTipoDt.ID_GUIA_GENERICA);
		ps.adicionarLong(idProcesso);
		
		ResultSetTJGO rs1 = null;
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				rs1 = this.consultar(sql, ps);
				if( rs1 != null ) {
					
					GuiaItemDt guiaItemDt = null;
					
					while( rs1.next() ) {
						if( listaGuiaItemDt == null ) {
							listaGuiaItemDt = new ArrayList<GuiaItemDt>();
						}
						guiaItemDt = new GuiaItemDt();
						
						associarDt(guiaItemDt, rs1);
						
						listaGuiaItemDt.add(guiaItemDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método que consulta se guia tem item de despesa postal.
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean guiaPossuiItemDespesaPostal(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(ITEM.ID_GUIA_ITEM) AS QUANTIDADE FROM PROJUDI.GUIA_ITEM ITEM ";
		sql += "INNER JOIN PROJUDI.GUIA_EMIS GUIA ON (ITEM.ID_GUIA_EMIS = GUIA.ID_GUIA_EMIS) ";
		sql += "WHERE GUIA.ID_GUIA_EMIS = ? ";
		sql += "AND ITEM.ID_CUSTA IN (?, ?, ?) ";
		
		ps.adicionarLong(idGuiaEmissao);
		ps.adicionarLong(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.DESPESA_POSTAL_ANTIGO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					retorno = rs1.getInt("QUANTIDADE") > 0;
				}				
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
}
