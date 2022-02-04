package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaItemDisponivelDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaItemDisponivelPs extends Persistencia {

	private static final long serialVersionUID = 5618903419743446496L;
	
	public GuiaItemDisponivelPs(Connection conexao){		
		Conexao = conexao;
	}
	
	/**
	 * Inclusão de guia-item-disponivel.
	 * @param dados
	 * @throws Exception
	 */
	public void inserir(GuiaItemDisponivelDt dados) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO projudi.GUIA_ITEM_DISPONIVEL (";

		stSqlValores += " Values (";
		
		if ((dados.getGuiaItemDt() != null && dados.getGuiaItemDt().getId() != null && dados.getGuiaItemDt().getId().length()>0)) {
			stSqlCampos += stVirgula + "ID_GUIA_ITEM ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getGuiaItemDt().getId());

			stVirgula=",";
		}
		
		if ((dados.getPendenciaDt() != null && dados.getPendenciaDt().getId() != null && dados.getPendenciaDt().getId().length()>0)) {
			stSqlCampos += stVirgula + "ID_PEND ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getPendenciaDt().getId());

			stVirgula=",";
		}
		
		if ((dados.getMovimentacaoDt() != null && dados.getMovimentacaoDt().getId() != null && dados.getMovimentacaoDt().getId().length()>0)) {
			stSqlCampos += stVirgula + "ID_MOVI ";
			stSqlValores += stVirgula + "? " ;
			ps.adicionarLong(dados.getMovimentacaoDt().getId());

			stVirgula=",";
		}
		
		if ((dados.getDataPendencia() != null && dados.getDataPendencia().length()>0)) {
			stSqlCampos += stVirgula + "DATA_PENDENCIA " ;
			stSqlValores += stVirgula + "? " ;
			ps.adicionarDate(dados.getDataPendencia());

			stVirgula=",";
		}
		
		if ((dados.getDataMovimentacao() != null && dados.getDataMovimentacao().length()>0)) {
			 stSqlCampos += stVirgula + "DATA_MOVIMENTACAO " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataMovimentacao());

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores;

		dados.setId(executarInsert(stSql,"ID_GUIA_ITEM_DISPONIVEL",ps));
	}
	
	/**
	 * Alteração da guia-item-disponivel.
	 * @param dados
	 * @throws Exception
	 */
	public void alterar(GuiaItemDisponivelDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		String stVirgula="";

		stSql= "UPDATE PROJUDI.GUIA_ITEM_DISPONIVEL SET  ";
		
		if ((dados.getGuiaItemDt() != null && dados.getGuiaItemDt().getId() != null && dados.getGuiaItemDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_GUIA_ITEM = ?";
			
			ps.adicionarLong(dados.getGuiaItemDt().getId());
			
			stVirgula=",";
		}
		
		if ((dados.getPendenciaDt() != null && dados.getPendenciaDt().getId() != null && dados.getPendenciaDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_PEND = ?";
			
			ps.adicionarLong(dados.getPendenciaDt().getId());

			stVirgula=",";
		
		}
		
		if ((dados.getMovimentacaoDt() != null && dados.getMovimentacaoDt().getId() != null && dados.getMovimentacaoDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_MOVI = ?";
			
			ps.adicionarLong(dados.getMovimentacaoDt().getId());

			stVirgula=",";
		}
		
		if ((dados.getDataPendencia() != null && dados.getDataPendencia().length()>0)) {
			stSql+= stVirgula + " DATA_PENDENCIA = ?";
			
			ps.adicionarDate(dados.getDataPendencia());

			stVirgula=",";
		}
		
		if ((dados.getDataMovimentacao() != null && dados.getDataMovimentacao().length()>0)) {
			stSql+= stVirgula + " DATA_MOVIMENTACAO = ?";
			
			ps.adicionarDate(dados.getDataMovimentacao());

			stVirgula=",";
		}
		
		stSql += " WHERE ID_GUIA_ITEM_DISPONIVEL = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(stSql,ps);
	}
	
	/**
	 * Método para liberar guia-item-disponivel da pendencia descartada.
	 * @param String idPend
	 * @param String idProc
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean liberarGuiaItemPendencia(String idPend, String idProc) throws Exception {
		
		String sql = "UPDATE PROJUDI.GUIA_ITEM_DISPONIVEL SET ID_PEND = NULL, DATA_PENDENCIA = NULL ";
		sql += "WHERE ID_GUIA_ITEM IN (SELECT ID_GUIA_ITEM FROM PROJUDI.GUIA_ITEM WHERE ID_GUIA_EMIS IN (SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ?)) ";
		sql += "AND ID_PEND = ? ";
		sql += "AND ID_PEND IS NOT NULL ";
		sql += "AND DATA_PENDENCIA IS NOT NULL ";
		sql += "AND ID_MOVI IS NULL ";
		sql += "AND DATA_MOVIMENTACAO IS NULL ";
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ps.adicionarLong(idProc);
		ps.adicionarLong(idPend);
		
		return executarUpdateDelete(sql, ps) > 0;
	}

	/**
	 * Método para vincular guia-item-disponivel de despesa postal com pendencia.
	 * @param String idPend
	 * @param String idProc
	 * @param String quantidade
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean vincularGuiaItemDepesaPostalPendencia(String idPend, String idProc, String quantidade) throws Exception {
		String sql = "UPDATE PROJUDI.GUIA_ITEM_DISPONIVEL SET ID_PEND = ?, DATA_PENDENCIA = SYSDATE ";
		sql += "WHERE ID_GUIA_ITEM IN (SELECT ID_GUIA_ITEM FROM PROJUDI.GUIA_ITEM WHERE ID_CUSTA IN (?,?,?) AND ID_GUIA_EMIS IN (SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? AND ID_GUIA_STATUS IN (?,?,?,?,?,?))) ";
		sql += "AND ID_PEND IS NULL ";
		sql += "AND DATA_PENDENCIA IS NULL ";
		sql += "AND ID_MOVI IS NULL ";
		sql += "AND DATA_MOVIMENTACAO IS NULL ";
		sql += "AND ROWNUM <= ?";
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ps.adicionarLong(idPend);
		ps.adicionarLong(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.DESPESA_POSTAL_ANTIGO);
		ps.adicionarLong(idProc);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(quantidade);
		
		return executarUpdateDelete(sql, ps) == Funcoes.StringToInt(quantidade);
	}
	
	/**
	 * Método para vincular guia-item-disponivel de despesa postal com MOVIMENTACAO que JA TENHA ID_PEND(pendencia) vinculada.
	 * @param String idMovi
	 * @param String idPend
	 * @param String idProc
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean vincularGuiaItemDespesaPostalMovimentacao(String idMovi, String idPend, String idProc) throws Exception {
		String sql = "UPDATE PROJUDI.GUIA_ITEM_DISPONIVEL SET ID_MOVI = ?, DATA_MOVIMENTACAO = SYSDATE ";
		sql += "WHERE ID_GUIA_ITEM IN (SELECT ID_GUIA_ITEM FROM PROJUDI.GUIA_ITEM WHERE ID_CUSTA IN (?,?,?) AND ID_GUIA_EMIS IN (SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? AND ID_GUIA_STATUS IN (?,?,?,?,?,?,?))) ";
		sql += "AND ID_PEND = ? ";
		sql += "AND DATA_PENDENCIA IS NOT NULL  ";
		sql += "AND ID_MOVI IS NULL ";
		sql += "AND DATA_MOVIMENTACAO IS NULL ";
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ps.adicionarLong(idMovi);
		ps.adicionarLong(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.DESPESA_POSTAL_ANTIGO);
		ps.adicionarLong(idProc);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		ps.adicionarLong(idPend);
		
		return executarUpdateDelete(sql, ps) > 0;
	}
	
	/**
	 * Método que consulta se processo possui item de custa de despesa postal emitido PAGO e SEM VINCULO com Pendencia.
	 * 
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean processoPossuiItemCustaDespesaPostalEmitidoPagoSemVinculoPendencia(String idProcesso) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT GUIA.ID_PROC FROM PROJUDI.GUIA_EMIS GUIA ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM ITEM ON (GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS) ";
		sql += "INNER JOIN PROJUDI.CUSTA CUSTA ON (ITEM.ID_CUSTA = CUSTA.ID_CUSTA) ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM_DISPONIVEL IDIS ON (ITEM.ID_GUIA_ITEM = IDIS.ID_GUIA_ITEM) ";
		sql += "WHERE GUIA.ID_PROC = ? ";
		sql += "AND CUSTA.ID_CUSTA IN (?, ?, ?) ";
		sql += "AND GUIA.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?) ";
		sql += "AND IDIS.ID_PEND IS NULL";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.DESPESA_POSTAL_ANTIGO);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( Funcoes.StringToInt(rs1.getString("ID_PROC")) == Funcoes.StringToInt(idProcesso) ) {
						retorno = true;
					}
				}				
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que consulta se processo possui item de custa de despesa postal emitido NAO PAGO e SEM VINCULO com Pendencia.
	 * 
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean processoPossuiItemCustaDespesaPostalEmitidoNaoPago(String idProcesso) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT GUIA.ID_PROC FROM PROJUDI.GUIA_EMIS GUIA ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM ITEM ON (GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS) ";
		sql += "INNER JOIN PROJUDI.CUSTA CUSTA ON (ITEM.ID_CUSTA = CUSTA.ID_CUSTA) ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM_DISPONIVEL IDIS ON (ITEM.ID_GUIA_ITEM = IDIS.ID_GUIA_ITEM) ";
		sql += "WHERE GUIA.ID_PROC = ? ";
		sql += "AND CUSTA.ID_CUSTA IN (?, ?, ?) ";
		sql += "AND GUIA.ID_GUIA_STATUS IN (?, ?) ";
		sql += "AND IDIS.ID_PEND IS NULL";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.DESPESA_POSTAL_ANTIGO);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( Funcoes.StringToInt(rs1.getString("ID_PROC")) == Funcoes.StringToInt(idProcesso) ) {
						retorno = true;
					}
				}				
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que consulta a guantidade de itens de despesa postal NAO pago.
	 * 
	 * @param String idProcesso
	 * @return int
	 * @throws Exception
	 * @author fasoares
	 */
	public int consultaQuantidadeItemDepesaPostalNaoPago(String idProcesso) throws Exception {
		int retorno = 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(IDIS.ID_GUIA_ITEM_DISPONIVEL) AS QUANTIDADE FROM PROJUDI.GUIA_EMIS GUIA ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM ITEM ON (GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS) ";
		sql += "INNER JOIN PROJUDI.CUSTA CUSTA ON (ITEM.ID_CUSTA = CUSTA.ID_CUSTA) ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM_DISPONIVEL IDIS ON (ITEM.ID_GUIA_ITEM = IDIS.ID_GUIA_ITEM) ";
		sql += "WHERE GUIA.ID_PROC = ? ";
		sql += "AND CUSTA.ID_CUSTA IN (?, ?, ?) ";
		sql += "AND GUIA.ID_GUIA_STATUS IN (?, ?) ";
		sql += "AND IDIS.ID_PEND IS NULL";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.DESPESA_POSTAL_ANTIGO);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					retorno = rs1.getInt("QUANTIDADE");
				}				
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que consulta a guantidade de itens despesa postal processo PAGO e SEM vinculo com Pendencia.
	 * @param String idProcesso
	 * @return int
	 * @throws Exception
	 * @author fasoares
	 */
	public int consultaQuantidadeItemDepesaPostalPagoSemVinculoPendencia(String idProcesso) throws Exception {
		int retorno = 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(IDIS.ID_GUIA_ITEM_DISPONIVEL) AS QUANTIDADE FROM PROJUDI.GUIA_EMIS GUIA ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM ITEM ON (GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS) ";
		sql += "INNER JOIN PROJUDI.CUSTA CUSTA ON (ITEM.ID_CUSTA = CUSTA.ID_CUSTA) ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM_DISPONIVEL IDIS ON (ITEM.ID_GUIA_ITEM = IDIS.ID_GUIA_ITEM) ";
		sql += "WHERE GUIA.ID_PROC = ? ";
		sql += "AND CUSTA.ID_CUSTA IN (?, ?, ?) ";
		sql += "AND GUIA.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";
		sql += "AND IDIS.ID_PEND IS NULL";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM);
		ps.adicionarLong(CustaDt.DESPESA_POSTAL_ANTIGO);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					retorno = rs1.getInt("QUANTIDADE");
				}				
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que consulta a guantidade de itens despesa postal vinculado com Pendencia.
	 * @param String idProcesso
	 * @return int
	 * @throws Exception
	 * @author fasoares
	 */
	public int consultaQuantidadeItemVinculadoPendencia(String idProcesso) throws Exception {
		int retorno = 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(IDIS.ID_GUIA_ITEM_DISPONIVEL) AS QUANTIDADE FROM PROJUDI.GUIA_EMIS GUIA ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM ITEM ON (GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS) ";
		sql += "INNER JOIN PROJUDI.CUSTA CUSTA ON (ITEM.ID_CUSTA = CUSTA.ID_CUSTA) ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM_DISPONIVEL IDIS ON (ITEM.ID_GUIA_ITEM = IDIS.ID_GUIA_ITEM) ";
		sql += "WHERE GUIA.ID_PROC = ? ";
		sql += "AND IDIS.ID_PEND IS NOT NULL";
		
		ps.adicionarLong(idProcesso);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					retorno = rs1.getInt("QUANTIDADE");
				}
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	
	/**
	 * Verifica se a guia está vinculada a pendencia OU movimentacao.
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaVinculadaPendenciaOUMovimentacao(String idGuiaEmissao) throws Exception {

		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT GUIA.ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS GUIA ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM ITEM ON (GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS) ";
		sql += "INNER JOIN PROJUDI.CUSTA CUSTA ON (ITEM.ID_CUSTA = CUSTA.ID_CUSTA) ";
		sql += "INNER JOIN PROJUDI.GUIA_ITEM_DISPONIVEL IDIS ON (ITEM.ID_GUIA_ITEM = IDIS.ID_GUIA_ITEM) ";
		sql += "WHERE GUIA.ID_GUIA_EMIS = ? ";
		sql += "AND ( IDIS.ID_PEND IS NOT NULL OR IDIS.ID_MOVI IS NOT NULL ) ";
		
		ps.adicionarLong(idGuiaEmissao);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( Funcoes.StringToInt(rs1.getString("ID_GUIA_EMIS")) == Funcoes.StringToInt(idGuiaEmissao) ) {
						retorno = true;
					}
				}				
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	
	}
	
}
