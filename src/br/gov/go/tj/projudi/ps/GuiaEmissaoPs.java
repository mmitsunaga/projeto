package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.FinanceiroConsultarGuiasDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ne.boletos.PagadorBoleto;
import br.gov.go.tj.projudi.ne.boletos.SituacaoBoleto;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class GuiaEmissaoPs extends GuiaEmissaoPsGen {
	
	private static final long serialVersionUID = -8893409497120998903L;

	public GuiaEmissaoPs(Connection conexao){
		Conexao = conexao;
	}

	protected void associarDt( GuiaEmissaoDt Dados, ResultSetTJGO rs)  throws Exception {
		associarDtCompleto(Dados, rs);
	}
	
	/**
	 * Método para salvar Guia Emitida.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void inserir(GuiaEmissaoDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos= "INSERT INTO PROJUDI.GUIA_EMIS ("; 
		SqlValores = " Values (";
		
		SqlCampos += "ID_GUIA_MODELO " ;
		SqlValores += "?";
		ps.adicionarLong(dados.getGuiaModeloDt().getId());
		
		if ( dados.getGuiaFinalidadeModeloDt() != null && dados.getGuiaFinalidadeModeloDt().getId() != null && dados.getGuiaFinalidadeModeloDt().getId().length()>0 ){
			SqlCampos += ",ID_GUIA_FINALIDADE_MODELO " ;
			SqlValores += "?";
			ps.adicionarLong(dados.getGuiaFinalidadeModeloDt().getId());			
		}
			
		if ( dados.getId_AreaDistribuicao() != null && dados.getId_AreaDistribuicao().length()>0 ){
			SqlCampos+= ",ID_AREA_DIST " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_AreaDistribuicao());
		}
		if ( dados.getId_ProcessoPrioridade() != null && dados.getId_ProcessoPrioridade().length() > 0 ){
			SqlCampos+= ",ID_PROC_PRIOR " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoPrioridade());
		}
		if( dados.getId_GuiaEmissaoPrincipal() != null && dados.getId_GuiaEmissaoPrincipal().length() > 0 ) {
			SqlCampos+= ",ID_GUIA_EMIS_PRINC";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_GuiaEmissaoPrincipal());
		}
		if( dados.getId_GuiaTipo() != null && dados.getId_GuiaTipo().length() > 0 ) {
			SqlCampos+= ",ID_GUIA_TIPO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_GuiaTipo());
		}
		if ( dados.getDataVencimento() != null && dados.getDataVencimento().length()>0 ){
			SqlCampos += ",DATA_VENCIMENTO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataVencimento());
		}
		if ( dados.getDataRecebimento() != null && dados.getDataRecebimento().length()>0 ){
			SqlCampos += ",DATA_RECEBIMENTO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataRecebimento());
		}
		if ( dados.getDataCancelamento() != null && dados.getDataCancelamento().length()>0 ){
			SqlCampos += ",DATA_CANCELAMENTO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataCancelamento());
		}
		if ( dados.getDataBaseAtualizacao() != null && dados.getDataBaseAtualizacao().length()>0 ){
			SqlCampos += ",DATA_BASE_ATUALIZACAO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataBaseAtualizacao());
		}
		if ( dados.getDataBaseFinalAtualizacao() != null && dados.getDataBaseFinalAtualizacao().length()>0 ){
			SqlCampos += ",DATA_BASE_FINAL_ATUALIZACAO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataBaseFinalAtualizacao());
		}
		
		if( dados.getNovoValorAcaoAtualizado() != null && dados.getNovoValorAcaoAtualizado().length() > 0 ) {
			SqlCampos+= ",VALOR_ACAO " ;
			SqlValores += ",?";
			ps.adicionarDecimal(dados.getNovoValorAcaoAtualizado());
		}
		else {
			if( dados.getValorAcao() != null && dados.getValorAcao().length()>0 ){
				SqlCampos+= ",VALOR_ACAO " ;
				SqlValores += ",?";
				ps.adicionarDecimal(dados.getValorAcao());
			}
			else {
				SqlCampos+= ",VALOR_ACAO " ;
				SqlValores += ",?";
				ps.adicionarBigDecimal("0.0");
			}
		}
		if ( dados.getNumeroGuiaCompleto() != null && dados.getNumeroGuiaCompleto().length()>0 ){
			SqlCampos+= ",NUMERO_GUIA_COMPLETO " ;
			SqlValores += ",?";
			ps.adicionarString(dados.getNumeroGuiaCompleto());
		}
		if ( dados.getNumeroProcessoDependente() != null && dados.getNumeroProcessoDependente().length() > 0 ){
			SqlCampos+= ",NUMERO_PROC_DEPENDENTE";
			SqlValores += ",?";
			ps.adicionarString(dados.getNumeroProcessoDependente());
		}
		if ( dados.getId_Comarca() != null && dados.getId_Comarca().length()>0){
			SqlCampos+= ",ID_COMARCA " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Comarca());
		}
		if ( dados.getId_Usuario() != null && dados.getId_Usuario().length()>0 ){
			SqlCampos+= ",ID_USU ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Usuario());
		}
		if ( dados.getId_Processo() != null && dados.getId_Processo().length()>0 ){
			SqlCampos+= ",ID_PROC ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Processo());
		}
		if ( dados.getId_Serventia() != null && dados.getId_Serventia().length()>0){
			SqlCampos+= ",ID_SERV " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Serventia());
		}
		if ( dados.getId_ProcessoTipo() != null && dados.getId_ProcessoTipo().length() > 0 ){
			SqlCampos+= ",ID_PROC_TIPO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoTipo());
		}
		if ( dados.getId_NaturezaSPG() != null && dados.getId_NaturezaSPG().length() > 0 ){
			SqlCampos+= ",ID_NATUREZA_SPG ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_NaturezaSPG());
		}
		if ( dados.getId_GuiaStatus() != null && dados.getId_GuiaStatus().length() > 0 ){
			SqlCampos+= ",ID_GUIA_STATUS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_GuiaStatus());
		}else{
			SqlCampos+= ",ID_GUIA_STATUS";
			SqlValores += ",?";
			ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		}
		
		SqlCampos += ",DATA_EMIS ";
		SqlValores += ",?";
		
		if (dados.isGuiaEmitidaSPG() && dados.getDataEmissao() != null && dados.getDataEmissao().trim().length() > 0) {
			ps.adicionarDate(dados.getDataEmissao());
		}
		else {
			if (dados.isGuiaEmitidaSSG() && dados.getDataEmissao() != null && !dados.getDataEmissao().isEmpty()) {
				//******************
				//ANALISAR ESTE CASO
				//******************
				//ps.adicionarDateTime(Funcoes.Stringyyyy_MM_ddToDateTime(dados.getDataEmissao()));
				ps.adicionarDateTime(Funcoes.StringToDate(dados.getDataEmissao()));
			}
			else {
				ps.adicionarDateTime(new Date());	
			}
		}
		
	    if( dados.getRequerente() != null && dados.getRequerente().length()>0 ){
			SqlCampos+= ",REQUERENTE";
			SqlValores += ",?";
			ps.adicionarString(dados.getRequerente());
		}
		if( dados.getRequerido() != null && dados.getRequerido().length()>0 ){
			SqlCampos += ",REQUERIDO";
			SqlValores += ",?";
			ps.adicionarString(dados.getRequerido());
		}
		if( dados.getNumeroDUAM() != null && dados.getNumeroDUAM().length() > 0 ){
			SqlCampos += ",NUMERO_DUAM";
			SqlValores += ",?";
			ps.adicionarLong(dados.getNumeroDUAM());
		}
		if( dados.getQuantidadeParcelasDUAM() != null && dados.getQuantidadeParcelasDUAM().length() > 0 ){
			SqlCampos += ",QUANTIDADE_PARCELAS_DUAM";
			SqlValores += ",?";
			ps.adicionarLong(dados.getQuantidadeParcelasDUAM());
		}
		if( dados.getDataVencimentoDUAM() != null && dados.getDataVencimentoDUAM().length() > 0 ){
			SqlCampos += ",DATA_VENCIMENTO_DUAM";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataVencimentoDUAM());
		}
		if( dados.getValorImpostoMunicipalDUAM() != null && dados.getValorImpostoMunicipalDUAM().length() > 0 ){
			SqlCampos += ",VALOR_IMPOSTO_MUNICIPAL_DUAM";
			SqlValores += ",?";
			ps.adicionarDecimal(dados.getValorImpostoMunicipalDUAM());
		}
		if ( dados.getId_Apelante() != null && dados.getId_Apelante().length() > 0 ){
			SqlCampos+= ",ID_PROC_PARTE_RECORRENTE ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Apelante());
		}
		if ( dados.getId_Apelado() != null && dados.getId_Apelado().length() > 0 ){
			SqlCampos+= ",ID_PROC_PARTE_RECORRIDO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Apelado());
		}
		if( dados.getProcessoParteTipoCodigo() != null && dados.getProcessoParteTipoCodigo().length() > 0 ) {
			SqlCampos+= ",PROC_PARTE_TIPO_CODIGO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getProcessoParteTipoCodigo());
		}
		if( dados.getId_ProcessoParteResponsavelGuia() != null && dados.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
			SqlCampos+= ",ID_PROC_PARTE_RESP_GUIA ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoParteResponsavelGuia());
		}
		if( dados.getRateioCodigo() != null && dados.getRateioCodigo().length() > 0 ) {
			SqlCampos+= ",RATEIO_CODIGO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getRateioCodigo());
		}
		if( dados.getCodigoNatureza() != null && dados.getCodigoNatureza().length() > 0 ) {
			SqlCampos += ",CODIGO_NATUREZA";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCodigoNatureza());
		}
		
		if( dados.getIdGuiaReferenciaDescontoParcelamento() != null && dados.getIdGuiaReferenciaDescontoParcelamento().length() > 0 ) {
			SqlCampos += ",ID_GUIA_EMIS_REFERENCIA";
			SqlValores += ",?";
			ps.adicionarLong(dados.getIdGuiaReferenciaDescontoParcelamento());
		}
		if( dados.getTipoGuiaReferenciaDescontoParcelamento() != null && dados.getTipoGuiaReferenciaDescontoParcelamento().length() > 0 ) {
			SqlCampos += ",TIPO_GUIA_DESCONTO_PARCELAMENT";
			SqlValores += ",?";
			ps.adicionarLong(dados.getTipoGuiaReferenciaDescontoParcelamento());
		}
		if( dados.getQuantidadeParcelas() != null && dados.getQuantidadeParcelas().length() > 0 ) {
			SqlCampos += ",QUANTIDADE_PARCELAS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getQuantidadeParcelas());
		}
		if( dados.getParcelaAtual() != null && dados.getParcelaAtual().length() > 0 ) {
			SqlCampos += ",PARCELA_ATUAL";
			SqlValores += ",?";
			ps.adicionarLong(dados.getParcelaAtual());
		}
		if( dados.getPorcentagemDesconto() != null && dados.getPorcentagemDesconto().length() > 0 ) {
			SqlCampos += ",PORCENTAGEM_DESCONTO";
			SqlValores += ",?";
			ps.adicionarDecimal(dados.getPorcentagemDesconto());
		}
		if( dados.getMetadadosGuia() != null && dados.getMetadadosGuia().length() > 0 ) {
			SqlCampos += ",METADADOS";
			SqlValores += ",?";
			ps.adicionarClob(dados.getMetadadosGuia());
		}
		
		SqlCampos += ",GUIA_COMPLEMENTAR ";
		SqlValores += ",?";
		ps.adicionarBoolean(dados.isLocomocaoComplementar());
				
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
			
		dados.setId(executarInsert(Sql, "ID_GUIA_EMIS", ps));
		
	}
	
	/**
	 * Método para alterar dados de Guia Emitida.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return 
	 * @throws Exception
	 */
	public boolean alterar(GuiaEmissaoDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.GUIA_EMIS SET ";
		
		if ( dados.getGuiaModeloDt() != null && dados.getGuiaModeloDt().getId() != null && dados.getGuiaModeloDt().getId().length()>0 ){
			Sql+= ",ID_GUIA_MODELO = ?";
			ps.adicionarLong(dados.getGuiaModeloDt().getId());
		}
		
		if ( dados.getGuiaFinalidadeModeloDt() != null && dados.getGuiaFinalidadeModeloDt().getId() != null && dados.getGuiaFinalidadeModeloDt().getId().length()>0 ){
			Sql+= ",ID_GUIA_FINALIDADE_MODELO = ?";
			ps.adicionarLong(dados.getGuiaFinalidadeModeloDt().getId());
		}
		
		if ( dados.getId_AreaDistribuicao() != null && dados.getId_AreaDistribuicao().length()>0 ){
			Sql+= ",ID_AREA_DIST = ?";
			ps.adicionarLong(dados.getId_AreaDistribuicao());
		}
		if ( dados.getId_ProcessoPrioridade() != null && dados.getId_ProcessoPrioridade().length() > 0 ){
			Sql+= ",ID_PROC_PRIOR = ?";
			ps.adicionarLong(dados.getId_ProcessoPrioridade());
		}
		if ( dados.getDataVencimento() != null && dados.getDataVencimento().length()>0 ){
			Sql += ",DATA_VENCIMENTO = ?";
			ps.adicionarDate(dados.getDataVencimento());
		}
		if( dados.getNovoValorAcaoAtualizado() != null && dados.getNovoValorAcaoAtualizado().length() > 0 ) {
			Sql+= ",VALOR_ACAO = ?";
			ps.adicionarDecimal(dados.getNovoValorAcaoAtualizado());
		}
		else {
			if( dados.getNovoValorAcaoAtualizado() != null && dados.getNovoValorAcaoAtualizado().length()>0 ){
				Sql+= ",VALOR_ACAO = ?";
				ps.adicionarDecimal(dados.getNovoValorAcaoAtualizado());
			}
		}
		if ( dados.getNumeroGuiaCompleto() != null && dados.getNumeroGuiaCompleto().length()>0 ){
			Sql+= ",NUMERO_GUIA_COMPLETO = ?";
			ps.adicionarLong(dados.getNumeroGuiaCompleto());
		}
		if ( dados.getId_Comarca() != null && dados.getId_Comarca().length()>0){
			Sql+= ",ID_COMARCA = ?";
			ps.adicionarLong(dados.getId_Comarca());
		}
		if ( dados.getId_Usuario() != null && dados.getId_Usuario().length() > 0 ){
			Sql+= ",ID_USU = ?";
			ps.adicionarLong(dados.getId_Usuario());
		}
		if ( dados.getNumeroProcessoDependente() != null && dados.getNumeroProcessoDependente().length() > 0 ){
			Sql+= ",NUMERO_PROC_DEPENDENTE = ?";
			ps.adicionarLong(dados.getNumeroProcessoDependente());
		}else{
			Sql+= ",NUMERO_PROC_DEPENDENTE = ?";
			ps.adicionarLongNull();
		}
		if ( dados.getId_Processo() != null && dados.getId_Processo().length() > 0 ){
			Sql+= ",ID_PROC = ?";
			ps.adicionarLong(dados.getId_Processo());
		}
		if ( dados.getId_Apelante() != null && dados.getId_Apelante().length() > 0 ){
			Sql+= ",ID_PROC_PARTE_RECORRENTE = ?";
			ps.adicionarLong(dados.getId_Apelante());
		}
		if ( dados.getId_Apelado() != null && dados.getId_Apelado().length() > 0 ){
			Sql+= ",ID_PROC_PARTE_RECORRIDO = ?";
			ps.adicionarLong(dados.getId_Processo());
		}
		if( dados.getProcessoParteTipoCodigo() != null && dados.getProcessoParteTipoCodigo().length() > 0 ) {
			Sql+= ",PROC_PARTE_TIPO_CODIGO = ?";
			ps.adicionarLong(dados.getProcessoParteTipoCodigo());
		}
		if( dados.getRateioCodigo() != null && dados.getRateioCodigo().length() > 0 ) {
			Sql+= ",RATEIO_CODIGO = ?";
			ps.adicionarLong(dados.getRateioCodigo());
		}
		
		Sql+= ",GUIA_COMPLEMENTAR = ?";
		ps.adicionarBoolean(dados.isLocomocaoComplementar());
		
		Sql=Sql.replace("SET ,","SET ");
		
		Sql = Sql + " WHERE ID_GUIA_EMIS = ?";
		ps.adicionarLong(dados.getId());

		return executarUpdateDelete(Sql, ps) > 0;
		
	}
	
	/**
	 * Método para cancelar a guia caso ela não esteja paga.
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public void cancelarGuiaEmitida(String idGuiaEmissao, String idGuiaStatus) throws Exception {

		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ?, DATA_CANCELAMENTO = ?, ID_GUIA_EMIS_PRINC = null WHERE ID_GUIA_EMIS = ? ";

		ps.adicionarLong(idGuiaStatus);

		ps.adicionarDateTime(new Date());
		ps.adicionarLong(idGuiaEmissao);
		
		executarUpdateDelete(Sql, ps);
		
	}
	
	/**
	 * Método que verifica se guia está paga pelo número da Guia e Série.
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPaga(String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT G.NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS G, projudi.GUIA_MODELO GM" +
		" WHERE G.NUMERO_GUIA_COMPLETO = ?"  +  
		" AND ( G.ID_GUIA_STATUS = ? OR " +
				"G.ID_GUIA_STATUS = ? OR " + 
				"G.ID_GUIA_STATUS = ? OR " +
				"G.ID_GUIA_STATUS = ? OR " +
				"G.ID_GUIA_STATUS = ? OR " +
				"G.ID_GUIA_STATUS = ? OR " + 
				"G.ID_GUIA_STATUS = ? )" +
		" AND G.ID_GUIA_MODELO = GM.ID_GUIA_MODELO";
		
		ps.adicionarLong(numeroGuiaCompleto);
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
					if( Funcoes.StringToInt(rs1.getString("NUMERO_GUIA_COMPLETO")) == Funcoes.StringToInt(numeroGuiaCompleto) ) {
						retorno = true;
					}
				}				
			}
		} finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que verifica se guia está com pedido de ressarcimento ou ressarcida.
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaRessarcidoPedidoRessarcido(String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT G.NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS G, projudi.GUIA_MODELO GM" +
		" WHERE G.NUMERO_GUIA_COMPLETO = ?"  +  
		" AND ( G.ID_GUIA_STATUS = ? OR " +
				"G.ID_GUIA_STATUS = ? )" +
		" AND G.ID_GUIA_MODELO = GM.ID_GUIA_MODELO";
		
		ps.adicionarLong(numeroGuiaCompleto);
		ps.adicionarLong(GuiaStatusDt.PEDIDO_RESSARCIMENTO_SOLICITADO);
		ps.adicionarLong(GuiaStatusDt.RESSARCIDO );
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( Funcoes.StringToInt(rs1.getString("NUMERO_GUIA_COMPLETO")) == Funcoes.StringToInt(numeroGuiaCompleto) ) {
						retorno = true;
					}
				}
			}
		} finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que verifica se guia está paga pelo ID da Guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPaga(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(guiaEmissaoDt.getId());
		
		sql += " AND ID_GUIA_STATUS IN ( ?, ?, ?, ?, ?, ?) ";
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getString("ID_GUIA_EMIS").equals(guiaEmissaoDt.getId()) )
						retorno = true;
				}
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para veriicar se a guia está cancelada ou não.
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaCancelada(String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS WHERE ID_GUIA_STATUS = ? AND NUMERO_GUIA_COMPLETO = ? ";
		ps.adicionarLong(GuiaStatusDt.CANCELADA);
		ps.adicionarLong(numeroGuiaCompleto);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getString("NUMERO_GUIA_COMPLETO").equals(numeroGuiaCompleto) ) {
						retorno = true;
					}
				}
			}
		} finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para veriicar se a guia está cancelada e paga ou não.
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaCanceladaPaga(String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS WHERE ID_GUIA_STATUS = ? AND NUMERO_GUIA_COMPLETO = ? ";
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(numeroGuiaCompleto);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getString("NUMERO_GUIA_COMPLETO").equals(numeroGuiaCompleto) ) {
						retorno = true;
					}
				}
			}
		} finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se a guia está cancelada ou não, independente de pagamento.
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaCanceladaIndependentePagamento(String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ?  AND ( ID_GUIA_STATUS = ? OR ID_GUIA_STATUS = ? OR ID_GUIA_STATUS = ? ) ";
		ps.adicionarLong(numeroGuiaCompleto);
		ps.adicionarLong(GuiaStatusDt.CANCELADA);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getString("NUMERO_GUIA_COMPLETO").equals(numeroGuiaCompleto) ) {
						retorno = true;
					}
				}
			}
		} finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se a guia está com o status de AGUARDANDO PAGAMENTO.
	 * Retorno true se sim e false de não.
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaStatusAguardandoPagamento(String numeroGuia) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS WHERE ID_GUIA_STATUS IN (?, ?) AND NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		ps.adicionarLong(numeroGuia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if( rs1 != null ) {
				Long numeroGuiaNumerico = Funcoes.StringToLong(numeroGuia);
				while(rs1.next()) {
					if( rs1.getString("NUMERO_GUIA_COMPLETO").equals(String.valueOf(numeroGuiaNumerico)) ) {
						retorno = true;
					}
				}
			}
		
		}
		finally{
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar guias emitidas pelo usuário.
	 * @param String Id_Usuario
	 * @param List<GuiaTipoDt>
	 * @return List<GuiaEmissaoDt>
	 * @throws Excetption
	 */
	public List consultarGuiasIdUsuario(String Id_Usuario, List listaGuiaTipoDt) throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_USU = ?";
		ps.adicionarLong(Id_Usuario);
		
		if( listaGuiaTipoDt != null && listaGuiaTipoDt.size() > 0 ) {
			sql += " AND ID_GUIA_TIPO IN (";
			
			for( int k = 0; k < listaGuiaTipoDt.size(); k++ ) {
				String idGuiaTipo = (String) listaGuiaTipoDt.get(k);
				
				sql += "?,";
				ps.adicionarLong(idGuiaTipo);
			}
			
			sql += ")";
			
			sql = sql.replace(",)", ")");
		}
		
		sql += " ORDER BY ID_GUIA_EMIS DESC";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaGuiaEmissaoDt == null )
						listaGuiaEmissaoDt = new ArrayList();
					
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);					
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo número da Guia.
	 * @param String numeroGuiaCompleto
	 * @param String idGuiaTipo(pode ser nulo)
	 * @param String idGuiaStatus(pode ser nulo)
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarNumeroCompleto(String numeroGuiaCompleto, String idGuiaTipo, String idGuiaStatus) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ? ";
		ps.adicionarLong(numeroGuiaCompleto);
		
		if( idGuiaTipo != null && idGuiaTipo.length() > 0 ) {
			sql = sql + " AND ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
		}
		if( idGuiaStatus != null && idGuiaStatus.length() > 0 ) {
			sql = sql + " AND ID_GUIA_STATUS = ?";
			ps.adicionarLong(idGuiaStatus);
		}
		
		
		ResultSetTJGO rs1 = null;
		try{
			if( numeroGuiaCompleto != null ) {
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo número da Guia.
	 * @param String numeroGuiaCompleto
	 * @param String idGuiaTipo(pode ser nulo)
	 * @param String idGuiaStatus(pode ser nulo)
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarNumeroCompletoGuiaInicial(String numeroGuiaCompleto, String idGuiaTipo, String idGuiaStatus) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ? AND ID_PROC IS NULL ";
		ps.adicionarLong(numeroGuiaCompleto);
		
		if( idGuiaTipo != null && idGuiaTipo.length() > 0 ) {
			sql = sql + " AND ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
		}
		if( idGuiaStatus != null && idGuiaStatus.length() > 0 ) {
			sql = sql + " AND ID_GUIA_STATUS = ?";
			ps.adicionarLong(idGuiaStatus);
		}
		
		
		ResultSetTJGO rs1 = null;
		try{
			if( numeroGuiaCompleto != null ) {
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar guia para a diretoria financeira.
	 * 
	 * @param FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaEmissao(FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt) throws Exception {
		
		List<GuiaEmissaoDt> listaGuiasEmitidas = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ";
		//ps.adicionarLong(idGuiaEmissao);
		
		if( financeiroConsultarGuiasDt.getNumeroGuiaCompleto() != null && financeiroConsultarGuiasDt.getNumeroGuiaCompleto().length() > 0 ) {
			sql += " AND NUMERO_GUIA_COMPLETO = ? ";
			ps.adicionarLong(financeiroConsultarGuiasDt.getNumeroGuiaCompleto());
		}
		if( financeiroConsultarGuiasDt.getNumeroProcesso() != null && financeiroConsultarGuiasDt.getNumeroProcesso().length() > 0 ) {
			String numeroProcesso[] = financeiroConsultarGuiasDt.getNumeroProcesso().split("\\.");
			if( numeroProcesso.length == 2 ) {
				sql += " AND ID_PROC = ( SELECT ID_PROC FROM projudi.PROC WHERE ";
				if( numeroProcesso[0].length() > 0 ) {
					sql += " PROC_NUMERO = ? ";
					ps.adicionarLong(numeroProcesso[0]);
				}
				if( numeroProcesso[1].length() > 0 ) {
					sql += " AND DIGITO_VERIFICADOR = ? ";
					ps.adicionarLong(numeroProcesso[1]);
				}
				sql += " ) ";
			}
		}
		if( financeiroConsultarGuiasDt.getId_GuiaTipo() != null && financeiroConsultarGuiasDt.getId_GuiaTipo().length() > 0 ) {
			sql += " AND ID_GUIA_TIPO = ? ";
			ps.adicionarLong(financeiroConsultarGuiasDt.getId_GuiaTipo());
		}
		if( financeiroConsultarGuiasDt.getId_GuiaStatus() != null && financeiroConsultarGuiasDt.getId_GuiaStatus().length() > 0 ) {
			sql += " AND ID_GUIA_STATUS = ? ";
			ps.adicionarLong(financeiroConsultarGuiasDt.getId_GuiaStatus());
		}
		if( financeiroConsultarGuiasDt.getDataInicioEmissao() != null && financeiroConsultarGuiasDt.getDataInicioEmissao().length() > 0 ) {
			sql += " AND DATA_EMIS >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(financeiroConsultarGuiasDt.getDataInicioEmissao());
		}
		if( financeiroConsultarGuiasDt.getDataFimEmissao() != null && financeiroConsultarGuiasDt.getDataFimEmissao().length() > 0 ) {
			sql += " AND DATA_EMIS <= ? ";
			ps.adicionarDateTimeUltimaHoraDia(financeiroConsultarGuiasDt.getDataFimEmissao());
		}
		if( financeiroConsultarGuiasDt.getDataInicioRecebimento() != null && financeiroConsultarGuiasDt.getDataInicioRecebimento().length() > 0 ) {
			sql += " AND DATA_RECEBIMENTO >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(financeiroConsultarGuiasDt.getDataInicioRecebimento());
		}
		if( financeiroConsultarGuiasDt.getDataFimRecebimento() != null && financeiroConsultarGuiasDt.getDataFimRecebimento().length() > 0 ) {
			sql += " AND DATA_RECEBIMENTO <= ? ";
			ps.adicionarDateTimeUltimaHoraDia(financeiroConsultarGuiasDt.getDataFimRecebimento());
		}
		if( financeiroConsultarGuiasDt.getDataInicioCancelamento() != null && financeiroConsultarGuiasDt.getDataInicioCancelamento().length() > 0 ) {
			sql += " AND DATA_CANCELAMENTO >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(financeiroConsultarGuiasDt.getDataInicioCancelamento());
		}
		if( financeiroConsultarGuiasDt.getDataFimCancelamento() != null && financeiroConsultarGuiasDt.getDataFimCancelamento().length() > 0 ) {
			sql += " AND DATA_CANCELAMENTO <= ?  ";
			ps.adicionarDateTimeUltimaHoraDia(financeiroConsultarGuiasDt.getDataFimCancelamento());
		}
		
		
		sql += " ORDER BY DATA_EMIS ";
		if( financeiroConsultarGuiasDt.getOrdenacao().equals("0") ) {
			sql += " DESC ";
		}
		
		sql = sql.replace("WHERE  AND", "WHERE");
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if( rs1 != null ) {
				
				GuiaEmissaoDt guiaEmissaoDt = null;
				
				while( rs1.next() ) {
					if( listaGuiasEmitidas == null ) {
						listaGuiasEmitidas = new ArrayList<GuiaEmissaoDt>();
					}
					guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);
					
					listaGuiasEmitidas.add(guiaEmissaoDt);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiasEmitidas;
	}
	
	/**
	 * Método para consultar guia para a diretoria financeira.
	 * 
	 * @param FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt
	 * @return String
	 * @throws Exception
	 */
	public String consultarGuiaEmissaoJSON(FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt, String PosicaoPaginaAtual) throws Exception {
		
		String stTemp = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//ID_GUIA_EMIS AS ID, 
		String sqlHeader = "SELECT GE.NUMERO_GUIA_COMPLETO AS Id, GE.GUIA_TIPO AS descricao1, (SELECT TRIM(PROC_NUMERO || '.' || DIGITO_VERIFICADOR || '.' || ANO) FROM PROC P WHERE P.ID_PROC = GE.ID_PROC) AS descricao2,  TO_CHAR(GE.DATA_EMIS, 'DD/MM/YYYY HH24:MI') AS descricao3, TO_CHAR(GE.DATA_VENCIMENTO, 'DD/MM/YYYY') AS descricao4, TO_CHAR(GE.DATA_RECEBIMENTO, 'DD/MM/YYYY') AS descricao5, TO_CHAR(GE.DATA_CANCELAMENTO, 'DD/MM/YYYY HH24:MI') AS descricao6, GE.GUIA_STATUS AS descricao7, NVL((SELECT 'SIM' FROM PROC_PARTE_DEBITO PPD WHERE PPD.ID_GUIA_EMIS = GE.ID_GUIA_EMIS AND ROWNUM = 1), 'NÃO') AS descricao8, NVL((SELECT 'SIM' FROM PROC_PARTE_DEBITO_CADIN PPDC INNER JOIN PROC_PARTE_DEBITO PPD ON PPD.ID_PROC_PARTE_DEBITO = PPDC.ID_PROC_PARTE_DEBITO WHERE PPD.ID_GUIA_EMIS = GE.ID_GUIA_EMIS AND ROWNUM = 1), 'NÃO') AS descricao9  FROM PROJUDI.VIEW_GUIA_EMIS GE ";
		String sqlHeaderCertidao = "";
		
		if( ( financeiroConsultarGuiasDt.getDataInicioCertidao() != null && !financeiroConsultarGuiasDt.getDataInicioCertidao().isEmpty() ) 
				|| 
			( financeiroConsultarGuiasDt.getDataFimCertidao() != null && !financeiroConsultarGuiasDt.getDataFimCertidao().isEmpty() ) ) {
			sqlHeaderCertidao = " INNER JOIN PROJUDI.CERT C ON C.NUMERO_GUIA_COMPLETO = GE.NUMERO_GUIA_COMPLETO ";
		}
		
		sqlHeader += sqlHeaderCertidao + "WHERE";
		
		String sql = "";
				
		if( financeiroConsultarGuiasDt.getNumeroGuiaCompleto() != null && financeiroConsultarGuiasDt.getNumeroGuiaCompleto().length() > 0 ) {
			sql += " AND GE.NUMERO_GUIA_COMPLETO = ? ";
			ps.adicionarLong(financeiroConsultarGuiasDt.getNumeroGuiaCompleto());			
		}
		if( financeiroConsultarGuiasDt.getNumeroProcesso() != null && financeiroConsultarGuiasDt.getNumeroProcesso().length() > 0 ) {
			String numeroProcesso[] = financeiroConsultarGuiasDt.getNumeroProcesso().split("\\.");
			if( numeroProcesso.length >= 2 ) {
				sql += " AND GE.ID_PROC IN ( SELECT ID_PROC FROM projudi.PROC WHERE ";
				if( numeroProcesso[0].length() > 0 ) {
					sql += " PROC_NUMERO = ? ";
					ps.adicionarLong(numeroProcesso[0]);					
				}
				if( numeroProcesso[1].length() > 0 ) {
					sql += " AND DIGITO_VERIFICADOR = ? ";
					ps.adicionarLong(numeroProcesso[1]);					
				}
				if( numeroProcesso.length == 3 && numeroProcesso[2].length() > 0 ) {
					sql += " AND ANO = ? ";
					ps.adicionarLong(numeroProcesso[2]);					
				}
				sql += " ) ";
			}
		}
		if( financeiroConsultarGuiasDt.getId_GuiaTipo() != null && financeiroConsultarGuiasDt.getId_GuiaTipo().length() > 0 ) {
			sql += " AND GE.ID_GUIA_TIPO = ? ";
			ps.adicionarLong(financeiroConsultarGuiasDt.getId_GuiaTipo());
		}
		if( financeiroConsultarGuiasDt.getId_GuiaStatus() != null && financeiroConsultarGuiasDt.getId_GuiaStatus().length() > 0 ) {
			sql += " AND GE.ID_GUIA_STATUS = ? ";
			ps.adicionarLong(financeiroConsultarGuiasDt.getId_GuiaStatus());
		}
		if( financeiroConsultarGuiasDt.getDataInicioEmissao() != null && financeiroConsultarGuiasDt.getDataInicioEmissao().length() > 0 ) {
			sql += " AND GE.DATA_EMIS >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(financeiroConsultarGuiasDt.getDataInicioEmissao());
		}
		if( financeiroConsultarGuiasDt.getDataFimEmissao() != null && financeiroConsultarGuiasDt.getDataFimEmissao().length() > 0 ) {
			sql += " AND GE.DATA_EMIS <= ? ";
			ps.adicionarDateTimeUltimaHoraDia(financeiroConsultarGuiasDt.getDataFimEmissao());
		}
		if( financeiroConsultarGuiasDt.getDataInicioRecebimento() != null && financeiroConsultarGuiasDt.getDataInicioRecebimento().length() > 0 ) {
			sql += " AND GE.DATA_RECEBIMENTO >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(financeiroConsultarGuiasDt.getDataInicioRecebimento());
		}
		if( financeiroConsultarGuiasDt.getDataFimRecebimento() != null && financeiroConsultarGuiasDt.getDataFimRecebimento().length() > 0 ) {
			sql += " AND GE.DATA_RECEBIMENTO <= ? ";
			ps.adicionarDateTimeUltimaHoraDia(financeiroConsultarGuiasDt.getDataFimRecebimento());
		}
		if( financeiroConsultarGuiasDt.getDataInicioCancelamento() != null && financeiroConsultarGuiasDt.getDataInicioCancelamento().length() > 0 ) {
			sql += " AND GE.DATA_CANCELAMENTO >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(financeiroConsultarGuiasDt.getDataInicioCancelamento());
		}
		if( financeiroConsultarGuiasDt.getDataFimCancelamento() != null && financeiroConsultarGuiasDt.getDataFimCancelamento().length() > 0 ) {
			sql += " AND GE.DATA_CANCELAMENTO <= ?  ";
			ps.adicionarDateTimeUltimaHoraDia(financeiroConsultarGuiasDt.getDataFimCancelamento());
		}
		
		
		if( ( financeiroConsultarGuiasDt.getDataInicioCertidao() != null && !financeiroConsultarGuiasDt.getDataInicioCertidao().isEmpty() ) 
				|| 
			( financeiroConsultarGuiasDt.getDataFimCertidao() != null && !financeiroConsultarGuiasDt.getDataFimCertidao().isEmpty() ) ) {
			
			if( financeiroConsultarGuiasDt.getDataInicioCertidao() != null && financeiroConsultarGuiasDt.getDataInicioCertidao().length() > 0 ) {
				sql += " AND C.DATA_EMISSAO >= ? ";
				ps.adicionarDateTimePrimeiraHoraDia(financeiroConsultarGuiasDt.getDataInicioCertidao());
			}
			
			if( financeiroConsultarGuiasDt.getDataFimCertidao() != null && financeiroConsultarGuiasDt.getDataFimCertidao().length() > 0 ) {
				sql += " AND C.DATA_EMISSAO <= ?  ";
				ps.adicionarDateTimeUltimaHoraDia(financeiroConsultarGuiasDt.getDataFimCertidao());
			}
			
		}
		
		
		sql += " ORDER BY GE.DATA_EMIS ";
		if( financeiroConsultarGuiasDt.getOrdenacao().equals("0") ) {
			sql += " DESC ";
		}
		
		String sqlFinal = sqlHeader + sql;
		sqlFinal = sqlFinal.replace("WHERE AND", "WHERE ");
		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		try{
			rs1 = this.consultarPaginacao(sqlFinal, ps, PosicaoPaginaAtual);
						
			//Paginação
			String sqlQuantidade = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_GUIA_EMIS GE " + sqlHeaderCertidao + " WHERE " + sql;
			sqlQuantidade = sqlQuantidade.replace("WHERE  AND", "WHERE");
			
			rs2 = this.consultar(sqlQuantidade, ps);
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), PosicaoPaginaAtual, rs1, 9);
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
		}
		
		return stTemp;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo id.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissao(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_EMIS = ?";
		ps.adicionarLong(idGuiaEmissao);
		
		ResultSetTJGO rs1 = null;
		try{
			if( idGuiaEmissao != null ) {
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo id que está aguardando pagamento.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoAguardandoPagamento(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_EMIS = ? AND ID_GUIA_STATUS IN (?, ?)";
		ps.adicionarLong(idGuiaEmissao);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		
		ResultSetTJGO rs1 = null;
		try{
			if( idGuiaEmissao != null ) {
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo id que está aguardando pagamento e não vencida.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoAguardandoPagamentoNaoVencida(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_EMIS = ? AND ID_GUIA_STATUS IN (?, ?) AND SYSDATE <= DATA_VENCIMENTO";
		ps.adicionarLong(idGuiaEmissao);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		
		ResultSetTJGO rs1 = null;
		try{
			if( idGuiaEmissao != null ) {
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo número da Guia.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoNumeroGuia(String numeroGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( numeroGuia != null ) {
				
				String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ?";
				ps.adicionarLong(numeroGuia);
				
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias inciais e complemetares pagas.
	 * 
	 * @param String idProcesso
	 * @return List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoInicial_Complementar(String idProcesso) throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS " 
		+ " WHERE ID_PROC = ? AND ID_GUIA_STATUS IN (" 
		+ GuiaStatusDt.PAGO_ON_LINE + ","
		+ GuiaStatusDt.PAGO + ","
		+ GuiaStatusDt.PAGO_APOS_VENCIMENTO + ","
		+ GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA + ","
		+ GuiaStatusDt.PAGO_COM_VALOR_INFERIOR + ","
		+ GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR + ","
		+ GuiaStatusDt.CANCELADA_PAGA + ","
		+ GuiaStatusDt.PAGA_CANCELADA 
		+") "
		+ " AND ID_GUIA_MODELO IN ("
		+ " SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN ("
		+ GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU + ","
		+ GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU + ","
		+ GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU + ","
		+ GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU
		+ " )"
		+ " )";
		ps.adicionarLong(idProcesso);
		
		try{
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				
				listaGuiaEmissaoDt = new ArrayList();
				
				while(rs1.next()) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					this.associarDt(guiaEmissaoDt, rs1);
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias inciais e complemetares com qualquer status.
	 * 
	 * @param String idProcesso
	 * @return List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoInicial_ComplementarQualquerStatus(String idProcesso) throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS " 
		+ " WHERE ID_PROC = ? AND ID_GUIA_MODELO IN ("
		+ " SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN ("
		+ GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU + ","
		+ GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU + ","
		+ GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU + ","
		+ GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU
		+ " )"
		+ " )";
		
		ps.adicionarLong(idProcesso);
		
		try{
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				
				listaGuiaEmissaoDt = new ArrayList();
				
				while(rs1.next()) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					this.associarDt(guiaEmissaoDt, rs1);
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar a guia incial paga do processo.
	 * 
	 * @param String idProcesso
	 * @return GuiaEmissaoDt guia inicial paga do processo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicial(String idProcesso) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS " 
		+ " WHERE ID_PROC = ? AND ID_GUIA_STATUS IN (" 
		+ GuiaStatusDt.PAGO_ON_LINE + ","
		+ GuiaStatusDt.PAGO + ","
		+ GuiaStatusDt.PAGO_APOS_VENCIMENTO + ","
		+ GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA + ","
		+ GuiaStatusDt.PAGO_COM_VALOR_INFERIOR + ","
		+ GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR 
		+") "
		+ " AND ID_GUIA_TIPO IN (" 
		+ GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU + ","
		+ GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU
		+ " )";
		ps.adicionarLong(idProcesso);
		
		try{
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				
				if(rs1.next()) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					this.associarDt(guiaEmissaoDt, rs1);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia.
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissao() throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//String sql = "SELECT * FROM projudi.VIEW_GUIA_EMIS where id_guia_emis >= 9900 and id_guia_modelo is not null ORDER BY NUMERO_GUIA_COMPLETO";
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS where id_guia_emis >= 10916 ORDER BY NUMERO_GUIA_COMPLETO";
		//String sql = "SELECT * FROM projudi.VIEW_GUIA_EMIS where NUMERO_GUIA_COMPLETO in (11215150,10875850) and id_guia_emis not in (10179,10181,10183,10185) ORDER BY NUMERO_GUIA_COMPLETO";
		//String sql = "SELECT * FROM projudi.VIEW_GUIA_EMIS where id_guia_emis >= 2493 and id_guia_emis <= 3509 and id_guia_emis not in (10179,10181,10183,10185) ORDER BY NUMERO_GUIA_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					if( listaGuiaEmissaoDt == null )
						listaGuiaEmissaoDt = new ArrayList();
					
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia.
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissao(String idProcesso, List listaId_GuiaTipo) throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ";
		sql += " ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
		if( listaId_GuiaTipo != null && listaId_GuiaTipo.size() > 0 ) {
			sql += " AND ID_GUIA_TIPO IN (";
			
			for(int i = 0; i < listaId_GuiaTipo.size(); i++) {
				sql += "?,";
				ps.adicionarLong(listaId_GuiaTipo.get(i).toString());
			}
			sql += ")";
			
			sql = sql.replace(",)", ")");
		}
		
		sql += " ORDER BY NUMERO_GUIA_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					if( listaGuiaEmissaoDt == null )
						listaGuiaEmissaoDt = new ArrayList();
					
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia e status.
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @param List listaIdGuiaStatus
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoStatus(String idProcesso, List listaId_GuiaTipo, List listaIdGuiaStatus) throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ";
		sql += " ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
		if( listaId_GuiaTipo != null && listaId_GuiaTipo.size() > 0 ) {
			sql += " AND ID_GUIA_TIPO IN (";
			
			for(int i = 0; i < listaId_GuiaTipo.size(); i++) {
				sql += "?,";
				ps.adicionarLong(listaId_GuiaTipo.get(i).toString());
			}
			sql += ")";
			
			sql = sql.replace(",)", ")");
		}
		
		if( listaIdGuiaStatus != null && listaIdGuiaStatus.size() > 0 ) {
			sql += " AND ID_GUIA_STATUS IN (";
			
			for(int i = 0; i < listaIdGuiaStatus.size(); i++) {
				sql += "?,";
				ps.adicionarLong(listaIdGuiaStatus.get(i).toString());
			}
			sql += ")";
			
			sql = sql.replace(",)", ")");
		}
		
		sql += " ORDER BY DATA_EMIS, NUMERO_GUIA_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			while( rs1.next() ) {
				if( listaGuiaEmissaoDt == null )
					listaGuiaEmissaoDt = new ArrayList();
				
				GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
				
				associarDtCompleto(guiaEmissaoDt, rs1);
				
				listaGuiaEmissaoDt.add(guiaEmissaoDt);
			}
			
			rs1.close();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public List consultarGuiaEmissaoPagaGenericaParaGuiaComplementar(FabricaConexao obFabricaConexao, List listaGuiaEmissaoDtIgnorar, String idProcesso) throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ";
		sql += " ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
		if( listaGuiaEmissaoDtIgnorar != null && !listaGuiaEmissaoDtIgnorar.isEmpty() ) {
			sql += " AND NUMERO_GUIA_COMPLETO NOT IN (";
			for( int index = 0; index < listaGuiaEmissaoDtIgnorar.size(); index++ ) {
				GuiaEmissaoDt guiaIgnorar = (GuiaEmissaoDt) listaGuiaEmissaoDtIgnorar.get(index);
				
				if( guiaIgnorar.getNumeroGuiaCompleto() != null ) {
					sql += " ?,";
					ps.adicionarLong(guiaIgnorar.getNumeroGuiaCompleto());
				}
			}
			sql += ")";
			sql = sql.replace(",)", ")");
		}
		
		sql += " AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?)";
		ps.adicionarLong(GuiaTipoDt.ID_GUIA_GENERICA);
		
		sql += " AND ID_GUIA_TIPO_TITULO IN (?, ?, ?, ?)";
		ps.adicionarLong(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
		ps.adicionarLong(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
		ps.adicionarLong(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU);
		ps.adicionarLong(GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU);
		
		sql += " AND ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?, ?)";
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
		
		sql += " ORDER BY DATA_EMIS, NUMERO_GUIA_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			while( rs1.next() ) {
				if( listaGuiaEmissaoDt == null )
					listaGuiaEmissaoDt = new ArrayList();
				
				GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
				
				associarDtCompleto(guiaEmissaoDt, rs1);
				
				listaGuiaEmissaoDt.add(guiaEmissaoDt);
			}
			
			rs1.close();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia que NÃO ESTÃO CANCELADAS.
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoValidas(String idProcesso, List listaId_GuiaTipo) throws Exception {
		List listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ";
		sql += " ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
		if( listaId_GuiaTipo != null && listaId_GuiaTipo.size() > 0 ) {
			sql += " AND ID_GUIA_TIPO IN (";
			
			for(int i = 0; i < listaId_GuiaTipo.size(); i++) {
				sql += "?,";
				ps.adicionarLong(listaId_GuiaTipo.get(i).toString());
			}
			sql += ")";
			
			sql = sql.replace(",)", ")");
		}
		
		sql += " AND ID_GUIA_STATUS <> ? ";
		ps.adicionarLong(GuiaStatusDt.CANCELADA);
		
		sql += " ORDER BY DATA_EMIS, NUMERO_GUIA_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			while( rs1.next() ) {
				if( listaGuiaEmissaoDt == null )
					listaGuiaEmissaoDt = new ArrayList();
				
				GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
				
				associarDtCompleto(guiaEmissaoDt, rs1);
				
				listaGuiaEmissaoDt.add(guiaEmissaoDt);
			}
			rs1.close();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método de Consulta da GuiaEmissao pelo seu Id.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarId(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_EMIS = ?";
		ps.adicionarLong(idGuiaEmissao);	
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			if(rs1 != null && rs1.next()) {
				if( guiaEmissaoDt == null ) guiaEmissaoDt = new GuiaEmissaoDt();					
				this.associarDt(guiaEmissaoDt, rs1);
			}	
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}		
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método que verifica se tem Guia emitida para este Processo do tipo GuiaTipo.
	 * @param String idProcesso
	 * @param String idGuiaTipo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean possuiGuiaEmitida(String idProcesso, String idGuiaTipo) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		StringBuilder sql = new StringBuilder("SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS ge, PROJUDI.GUIA_MODELO tg, PROJUDI.GUIA_TIPO gt WHERE ge.ID_PROC = ? ");
		ps.adicionarLong(idProcesso);
		if( idGuiaTipo != null && idGuiaTipo.length() > 0 ) {
			sql.append(" AND ge.ID_GUIA_MODELO = tg.ID_GUIA_MODELO ");
			sql.append(" AND tg.ID_GUIA_TIPO = gt.ID_GUIA_TIPO ");
			sql.append(" AND gt.ID_GUIA_TIPO = ? ");
			ps.adicionarLong(idGuiaTipo);
		}
		
		sql.append(" AND ge.ID_GUIA_STATUS <> ? ");
		ps.adicionarLong(GuiaStatusDt.CANCELADA);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql.toString(), ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getString("ID_GUIA_EMIS") != null )
						retorno = true;
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que retorna o último número de guia emitida.
	 * @return String ultimoNumeroGuia
	 * @throws Exception
	 */
	public String consultarUltimoNumeroGuia() throws Exception {
		String ultimoNumeroGuia = null;
				
		ResultSetTJGO rs1 = null;
		try{
			String sql = "SELECT MAX(NUMERO_GUIA_COMPLETO) as NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS";
			
			rs1 = consultarSemParametros(sql);
			if( rs1 != null ) {
				while( rs1.next() ) {
					ultimoNumeroGuia = rs1.getString("NUMERO_GUIA_COMPLETO");
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return ultimoNumeroGuia;
	}
	
	/**
	 * Método para atualizar pagamentos das guias vinda do SAJ.
	 * @param String numerosGuiaCompleto
	 * @param String dataPagamento
	 * @throws Exception
	 */
	public void atualizarPagamentos(String numeroGuiaCompleto, String dataPagamento) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.GUIA_EMIS SET ";
		Sql+= "ID_GUIA_STATUS = ?";
		ps.adicionarLong(GuiaStatusDt.PAGO);
		Sql+= ",DATA_RECEBIMENTO = ?";
		ps.adicionarDate(dataPagamento);
		
		Sql+= " WHERE NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(numeroGuiaCompleto);
				
		executarUpdateDelete(Sql, ps);
		
	}
	
	/**
	 * Método para atualizar o pagamento do número da guia.
	 * @param String numeroGuiaCompleto
	 * @param Integer idGuiaStatus
	 * @param String dataPagamento
	 * @return boolean
	 * @throws Exception
	 */
	public boolean atualizarPagamento(String numeroGuiaCompleto, Integer idGuiaStatus, Date dataPagamento) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = "UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ? , ";
		ps.adicionarLong(idGuiaStatus);
		if( dataPagamento != null ){
			Sql += " DATA_RECEBIMENTO = ?";
			ps.adicionarDate(dataPagamento);
		}else{
			Sql += " DATA_RECEBIMENTO = ?";
			ps.adicionarDate(new Date());
		}
		
		Sql+= " WHERE NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(numeroGuiaCompleto);		
		
		retorno = executarUpdateDelete(Sql, ps) > 0;
				
		return retorno;
	}
	
	/**
	 * Método para atualizar o pagamento do número da guia.
	 * @param String numeroGuiaCompleto
	 * @param Integer idGuiaStatus
	 * @param Date dataPagamento
	 * @param Date dataMovimento (Data de Baixa / Repasse do Banco)
	 * @param double valorPagamento
	 * @return boolean
	 * @throws Exception
	 */
	public boolean atualizarPagamento(String numeroGuiaCompleto, Integer idGuiaStatus, Date dataPagamento,  Date dataMovimento, String valorPagamento) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//fred
		String Sql = "UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ? , ";
		ps.adicionarLong(idGuiaStatus);
		Sql += " DATA_RECEBIMENTO = ? , ";
		if( dataPagamento != null ){
			ps.adicionarDate(dataPagamento);
		}else{
			ps.adicionarDate(new Date());
		}
		Sql += " DATA_MOVIMENTO = ? , ";
		if( dataMovimento != null ){
			ps.adicionarDate(dataMovimento);
		}else{
			ps.adicionarDate(new Date());
		}
		Sql += " VALOR_RECEBIMENTO = ? ";
		ps.adicionarDouble(valorPagamento);
		Sql+= " WHERE NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(numeroGuiaCompleto);		
		
		executarUpdateDelete(Sql, ps);
		retorno = true;
		
		return retorno;
	}
	
	/**
	 * Método para consultar a guia complementar emitida da guia de locomoção.
	 * @param String idGuiaLocomocao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaComplementarGuiaLocomocao(String idGuiaLocomocao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_EMIS_PRINC = ?";
		ps.adicionarLong(idGuiaLocomocao);
		
		ResultSetTJGO rs1 = null;
		
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt; 
	}
	
	/**
	 * Método para consultar se existe no processo alguma guia emitida para este tipo específico.
	 * @param String idProcesso
	 * @param int idGuiaTipo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existeGuiaEmitidaMesmoTipo(String idProcesso, String idGuiaTipo) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		
		try{
			
			if( idProcesso != null && idGuiaTipo != null ) {
				String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_MODELO IN ( SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ? ) AND ID_PROC = ?";
				ps.adicionarLong(idGuiaTipo);
				ps.adicionarLong(idProcesso);
				
				rs1 = consultar(sql, ps);
				if( rs1 != null ) {
					while( rs1.next() ) {
						rs1.getString("ID_GUIA_EMIS");
						retorno = true;
						break;
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar se existe no processo alguma guia emitida para este tipo específico.
	 * @param String idProcesso
	 * @param int idGuiaTipo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existeGuiaEmitidaMesmoTipoNaoCancelada(String idProcesso, String idGuiaTipo) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		
		try {
			
			if( idProcesso != null && idGuiaTipo != null ) {
				String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_MODELO IN ( SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ? ) AND ID_PROC = ? AND ID_GUIA_STATUS <> (SELECT ID_GUIA_STATUS FROM PROJUDI.GUIA_STATUS WHERE GUIA_STATUS_CODIGO = ?) ";
				ps.adicionarLong(idGuiaTipo);
				ps.adicionarLong(idProcesso);
				ps.adicionarLong(GuiaStatusDt.CANCELADA);
				
				rs1 = consultar(sql, ps);
				if( rs1 != null ) {
					while( rs1.next() ) {
						rs1.getString("ID_GUIA_EMIS");
						retorno = true;
						break;
					}
				}
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar data de vencimento da guia.
	 * @param String numeroGuiaCompleto
	 * @return Date
	 * @throws Exception
	 */
	public Date consultarDataVencimento(String numeroGuiaCompleto) throws Exception {
		Date retorno = null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		
		try{
			String sql = "SELECT DATA_VENCIMENTO FROM PROJUDI.GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ?";
			ps.adicionarLong(numeroGuiaCompleto);
			
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					retorno = rs1.getDateTime("DATA_VENCIMENTO");
					break;
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método que consulta a lista de guias pelo status.
	 * @return List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public List consultarGuiasPeloStatus(String idGuiaStatus) throws Exception{
		
		return consultarGuiasPeloStatus(idGuiaStatus, 0);
	}
	
	public List consultarGuiasPeloStatus(String idGuiaStatus, int quantidadeMaxima) throws Exception {
		
		List listaGuiaEmissaoDt = new ArrayList();
		
		if (idGuiaStatus == null || idGuiaStatus.trim().length() == 0) return listaGuiaEmissaoDt;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_STATUS = ?";
		ps.adicionarLong(idGuiaStatus);
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);	
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);
					
					if (quantidadeMaxima > 0 && listaGuiaEmissaoDt.size() >= quantidadeMaxima) {
						return listaGuiaEmissaoDt;
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}	

	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao
	 * @param String idGuiaStatus
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean atualizarStatusGuiaEmitida(String idGuiaEmissao, String idGuiaStatus) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ? WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(idGuiaStatus);
		ps.adicionarLong(idGuiaEmissao);
		
		return executarUpdateDelete(Sql, ps) == 1;

	}
	
	/**
	 * Método para atualizar id_comarca e id_area_dist da guia emitida sem vinculo com processo e serventia.
	 * @param String idGuiaEmissao
	 * @param String idAreaDistribuicao
	 * @param String idComarca
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean atualizarComarcaAreaDistribuicao(String idGuiaEmissao, String idAreaDistribuicao, String idComarca) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET ID_COMARCA = ?, ID_AREA_DIST = ? WHERE ID_GUIA_EMIS = ? AND ID_PROC IS NULL AND ID_SERV IS NULL";
		ps.adicionarLong(idComarca);
		ps.adicionarLong(idAreaDistribuicao);
		ps.adicionarLong(idGuiaEmissao);
		
		return executarUpdateDelete(Sql, ps) == 1;

	}
	
	/**
	 * Método para verificar se guia gerada é 
	 * @param idGuiaEmissao
	 * @return
	 * @throws Exception
	 */
	public boolean isGuiaGeradaEnviarPrefeitura(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		
		try{
			String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_MODELO IN ( SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ? ) AND ID_GUIA_EMIS = ?";
			ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);
			ps.adicionarLong(idGuiaEmissao);
			
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					retorno = true;
					break;
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo id_Processo e tipo de guia.
	 * @param String idProcesso
	 * @param String id_GuiaTipo
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarUltimaGuiaEmissao(String idProcesso, String id_GuiaTipo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		String sql = "SELECT * ";
		sql += " FROM PROJUDI.VIEW_GUIA_EMIS ";
		sql += " WHERE ID_PROC = ? "; ps.adicionarLong(idProcesso);
		
		if(id_GuiaTipo != null && id_GuiaTipo.trim().length() > 0 ) {
			sql += " AND ID_GUIA_TIPO = ? "; ps.adicionarLong(id_GuiaTipo);			
		}
		
		sql += " AND ID_GUIA_EMIS = (SELECT MAX(ID_GUIA_EMIS) ";
		sql += "                       FROM PROJUDI.VIEW_GUIA_EMIS ";
		sql += "                      WHERE ID_PROC = ? "; ps.adicionarLong(idProcesso);
		
		if(id_GuiaTipo != null && id_GuiaTipo.trim().length() > 0 ) {
			sql += "                   AND ID_GUIA_TIPO = ? "; ps.adicionarLong(id_GuiaTipo);			
		}
		sql += "                    ) ";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				if ( rs1.next() ) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);			
					
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarUltimaGuiaEmissaoFazenda(String idProcesso) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		String sql = "SELECT *  ";
		sql += " FROM PROJUDI.VIEW_GUIA_EMIS ";
		sql += " WHERE ID_GUIA_EMIS = (SELECT MAX(ID_GUIA_EMIS) ";
		sql += "                       FROM PROJUDI.VIEW_GUIA_EMIS ";
		sql += "                       WHERE ID_PROC = ? "; ps.adicionarLong(idProcesso);
		sql += "                       AND ID_GUIA_TIPO IN (?,?,?)";			
		ps.adicionarLong(GuiaTipoDt.ID_FAZENDA_MUNICIPAL);
		ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);
		ps.adicionarLong(GuiaTipoDt.ID_FAZENDA_PUBLICA_AUTOMATICA);
		sql += "                       AND ID_GUIA_STATUS IN (?, ?) "; ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO); ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		sql += "                      ) ";
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				if ( rs1.next() ) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					associarDtCompleto(guiaEmissaoDt, rs1);			
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para estornar o pagamento do número da guia.
	 * @param String numeroGuiaCompleto
	 * @throws Exception
	 */
	public void estornarPagamento(String numeroGuiaCompleto) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String Sql = "UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ? , ";
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		Sql += " DATA_RECEBIMENTO = ?";
		ps.adicionarDateNull();		
		Sql+= " WHERE NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(numeroGuiaCompleto);		

		executarUpdateDelete(Sql, ps);			

	}
	
	/**
	 * Método para estornar o pagamento do número da guia da prefeitura.
	 * @param String numeroGuiaCompleto
	 * @throws Exception
	 */
	public void estornarPagamentoPrefeitura(String numeroGuiaCompleto) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String Sql = "UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ? , ";
		ps.adicionarLong(GuiaStatusDt.GUIA_ESTORNADA_PREFEITURA);
		Sql+= " WHERE NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(numeroGuiaCompleto);		
		
		executarUpdateDelete(Sql, ps);			
		
	}
	
	/**
	 * Método para estornar o pagamento do número da guia.
	 * @param String numeroGuiaCompleto
	 * @throws Exception
	 */
	public void estornarPagamentoPeloBanco(String numeroGuiaCompleto) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String Sql = "UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ? , ";
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		Sql += " DATA_RECEBIMENTO = ?";
		ps.adicionarDateNull();		
		Sql+= " WHERE NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(numeroGuiaCompleto);		

		executarUpdateDelete(Sql, ps);			

	}
	
	public List consultarGuiasPrefeiturasQueDevemSerCanceladasPeloPagamentoDaGuiaFazendaMunicipal() throws Exception {
		
		List listaGuiaEmissaoDt = new ArrayList();
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS GE";
		sql += " WHERE GE.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);		
		sql += " AND GE.ID_GUIA_STATUS NOT IN(?, ?, ?, ?, ?, ?, ?, ?) ";
		ps.adicionarLong(GuiaStatusDt.CANCELADA);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		sql += " AND EXISTS (SELECT 1 ";
		sql += "              FROM PROJUDI.GUIA_EMIS GE2 ";
		sql += "             WHERE GE2.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";	
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		sql += "              AND GE2.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_FAZENDA_MUNICIPAL);		
		sql += "              AND GE.ID_PROC = GE2.ID_PROC) ";	
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);	
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaFazendaMunicipal(String idProcesso) throws Exception {
		
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		if (idProcesso == null || idProcesso.trim().length() == 0) return guiaEmissaoDt;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS GE ";
		sql += " WHERE GE.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_FAZENDA_MUNICIPAL);
		sql += " AND GE.ID_GUIA_STATUS IN (?, ?) "; ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO); ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		sql += " AND GE.ID_PROC = ? "; ps.adicionarLong(idProcesso);
		sql += " AND EXISTS (SELECT 1 "; 
		sql += "               FROM PROJUDI.GUIA_EMIS GE2 "; 
		sql += "             WHERE GE2.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);
		sql += "               AND GE.ID_PROC = GE2.ID_PROC "; 
		sql += "               AND GE2.ID_GUIA_STATUS <> ? ";  ps.adicionarLong(GuiaStatusDt.CANCELADA);
		sql += "               AND GE2.ID_GUIA_STATUS <> ? ";  ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		sql += "               AND GE2.ID_GUIA_STATUS <> ?) ";  ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				if( rs1.next() ) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					associarDtCompleto(guiaEmissaoDt, rs1);	
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	public List consultarGuiasPrefeiturasCanceladasUsuarioEnvioPendentes() throws Exception {
		
		List listaGuiaEmissaoDt = new ArrayList();
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS GE";
		sql += " WHERE GE.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);		
		sql += " AND GE.ID_GUIA_STATUS = ? "; ps.adicionarLong(GuiaStatusDt.CANCELADA);
		sql += " AND GE.CODIGO_TEMP IS NULL ";
		sql += " AND NOT EXISTS (SELECT 1 ";
		sql += "                  FROM PROJUDI.GUIA_EMIS GE2 ";
		sql += "                 WHERE GE2.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";	
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);		
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		sql += "                 AND GE2.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_FAZENDA_MUNICIPAL);		
		sql += "                 AND GE.ID_PROC = GE2.ID_PROC) ";	
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);
					
					listaGuiaEmissaoDt.add(guiaEmissaoDt);	
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public void atualizarGuiasPrefeiturasCanceladasUsuarioPendentesDeEnvio(String idGuiaEmissao) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET CODIGO_TEMP = ? WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(1);
		ps.adicionarLong(idGuiaEmissao);
		
		executarUpdateDelete(Sql, ps);
		
	}
	
	public GuiaEmissaoDt associarDtCompleto(ResultSetTJGO rs)  throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
		associarDtCompleto(guiaEmissaoDt, rs);
		return guiaEmissaoDt;		
	}
	
	public void associarDtCompleto(GuiaEmissaoDt guiaEmissaoDt, ResultSetTJGO rs)  throws Exception {
		guiaEmissaoDt.setId(						rs.getString("ID_GUIA_EMIS"));
		guiaEmissaoDt.setGuiaEmissao(				rs.getString("GUIA_EMIS"));
		guiaEmissaoDt.setId_Processo(				rs.getString("ID_PROC"));
		guiaEmissaoDt.setNumeroProcesso(   		    rs.getString("PROC_NUMERO"));
		guiaEmissaoDt.setNumeroGuiaCompleto(		rs.getString("NUMERO_GUIA_COMPLETO"));
		guiaEmissaoDt.setNumeroProcessoDependente( 	rs.getString("NUMERO_PROC_DEPENDENTE"));
		guiaEmissaoDt.setId_ProcessoTipo(			rs.getString("ID_PROC_TIPO"));
		guiaEmissaoDt.setProcessoTipo(				rs.getString("PROC_TIPO"));
		guiaEmissaoDt.setId_NaturezaSPG(			rs.getString("ID_NATUREZA_SPG"));
		guiaEmissaoDt.setNaturezaSPG(   			rs.getString("NATUREZA_SPG"));
		guiaEmissaoDt.setId_GuiaEmissaoPrincipal(	rs.getString("ID_GUIA_EMIS_PRINC"));
		guiaEmissaoDt.setId_Serventia(				rs.getString("ID_SERV"));
		guiaEmissaoDt.setServentia(					rs.getString("SERV"));
		guiaEmissaoDt.setId_Comarca(				rs.getString("ID_COMARCA"));
		guiaEmissaoDt.setComarca(					rs.getString("COMARCA"));
		guiaEmissaoDt.setId_GuiaStatus(				rs.getString("ID_GUIA_STATUS"));
		guiaEmissaoDt.setGuiaStatus(				rs.getString("GUIA_STATUS"));
		guiaEmissaoDt.setId_Usuario(				rs.getString("ID_USU"));
		guiaEmissaoDt.setDataEmissao(				rs.getString("DATA_EMIS"));
		guiaEmissaoDt.setDataRecebimento(			rs.getString("DATA_RECEBIMENTO"));
		guiaEmissaoDt.setDataVencimento(			rs.getString("DATA_VENCIMENTO"));
		guiaEmissaoDt.setDataCancelamento( 			rs.getString("DATA_CANCELAMENTO"));
		guiaEmissaoDt.setDataEmissaoBoleto( 		rs.getString("DATA_EMISSAO_BOLETO"));
		guiaEmissaoDt.setDataBaseAtualizacao( 		rs.getString("DATA_BASE_ATUALIZACAO"));
		guiaEmissaoDt.setDataBaseFinalAtualizacao( 	rs.getString("DATA_BASE_FINAL_ATUALIZACAO"));
		guiaEmissaoDt.setRequerente(				rs.getString("REQUERENTE"));
		guiaEmissaoDt.setRequerido(					rs.getString("REQUERIDO"));
		guiaEmissaoDt.setValorAcao(					rs.getString("VALOR_ACAO"));
		guiaEmissaoDt.setNovoValorAcao(				rs.getString("VALOR_ACAO"));
		guiaEmissaoDt.setNovoValorAcaoAtualizado(	rs.getString("VALOR_ACAO"));
		guiaEmissaoDt.setCodigoTemp(				rs.getString("CODIGO_TEMP"));
		guiaEmissaoDt.setId_ProcessoPrioridade( 	rs.getString("ID_PROC_PRIOR"));
		guiaEmissaoDt.setId_AreaDistribuicao( 		rs.getString("ID_AREA_DIST"));
		guiaEmissaoDt.setAreaDistribuicao(			rs.getString("AREA_DIST"));
		guiaEmissaoDt.setNumeroDUAM( 				rs.getString("NUMERO_DUAM"));
		guiaEmissaoDt.setQuantidadeParcelasDUAM( 	rs.getString("QUANTIDADE_PARCELAS_DUAM"));
		guiaEmissaoDt.setDataVencimentoDUAM( 		rs.getString("DATA_VENCIMENTO_DUAM"));
		guiaEmissaoDt.setValorImpostoMunicipalDUAM( rs.getString("VALOR_IMPOSTO_MUNICIPAL_DUAM"));
		guiaEmissaoDt.setId_Apelante(				rs.getString("ID_PROC_PARTE_RECORRENTE"));
		guiaEmissaoDt.setId_Apelado( 				rs.getString("ID_PROC_PARTE_RECORRIDO"));
		guiaEmissaoDt.setProcessoParteTipoCodigo( 	rs.getString("PROC_PARTE_TIPO_CODIGO"));
		guiaEmissaoDt.setId_ProcessoParteResponsavelGuia(rs.getString("ID_PROC_PARTE_RESP_GUIA"));
		guiaEmissaoDt.setId_GuiaTipo( 				rs.getString("ID_GUIA_TIPO_TITULO") );
		guiaEmissaoDt.setRateioCodigo( 				rs.getString("RATEIO_CODIGO"));
		guiaEmissaoDt.setCodigoNatureza( 			rs.getString("CODIGO_NATUREZA"));
		guiaEmissaoDt.setValorRecebimento( 			rs.getString("VALOR_RECEBIMENTO"));
		guiaEmissaoDt.setDataMovimento( 			rs.getString("DATA_MOVIMENTO"));
		guiaEmissaoDt.setDataRepasse( 			    rs.getString("DATA_REPASSE"));
		guiaEmissaoDt.setLocomocaoComplementar(     rs.getBoolean("GUIA_COMPLEMENTAR"));
		
		if( rs.getString("ID_GUIA_EMIS_REFERENCIA") != null ) {
			guiaEmissaoDt.setIdGuiaReferenciaDescontoParcelamento(		rs.getString("ID_GUIA_EMIS_REFERENCIA"));
		}
		if( rs.getString("TIPO_GUIA_DESCONTO_PARCELAMENT") != null ) {
			guiaEmissaoDt.setTipoGuiaReferenciaDescontoParcelamento(	rs.getString("TIPO_GUIA_DESCONTO_PARCELAMENT"));
		}
		if( rs.getString("QUANTIDADE_PARCELAS") != null ) {
			guiaEmissaoDt.setQuantidadeParcelas(						rs.getString("QUANTIDADE_PARCELAS"));
		}
		if( rs.getString("PARCELA_ATUAL") != null ) {
			guiaEmissaoDt.setParcelaAtual(								rs.getString("PARCELA_ATUAL"));
		}
		if( rs.getString("PORCENTAGEM_DESCONTO") != null ) {
			guiaEmissaoDt.setPorcentagemDesconto(						rs.getString("PORCENTAGEM_DESCONTO"));
		}
		
		GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
		guiaModeloDt.setId(					rs.getString("ID_GUIA_MODELO"));
		guiaModeloDt.setGuiaModelo(			rs.getString("GUIA_MODELO"));
		guiaModeloDt.setId_GuiaTipo(		rs.getString("ID_GUIA_TIPO"));
		if (guiaEmissaoDt.isLocomocaoComplementar()) guiaModeloDt.setGuiaTipo(rs.getString("GUIA_TIPO") + " COMPLEMENTAR");
		else guiaModeloDt.setGuiaTipo(rs.getString("GUIA_TIPO"));
		guiaModeloDt.setId_ProcessoTipo(rs.getString("ID_PROC_TIPO"));
		guiaModeloDt.setProcessoTipo(rs.getString("PROC_TIPO"));
		guiaModeloDt.setId_NaturezaSPG(rs.getString("ID_NATUREZA_SPG"));
		guiaModeloDt.setNaturezaSPG(rs.getString("NATUREZA_SPG"));
		guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
		
		GuiaFinalidadeModeloDt guiaFinalidadeModeloDt = new GuiaFinalidadeModeloDt();
		guiaFinalidadeModeloDt.setId(		rs.getString("ID_GUIA_FINALIDADE_MODELO"));
		guiaFinalidadeModeloDt.setId_GuiaTipo(rs.getString("ID_GUIA_TIPO"));
		if (guiaEmissaoDt.isLocomocaoComplementar()) guiaFinalidadeModeloDt.setGuiaTipo(rs.getString("GUIA_TIPO") + " COMPLEMENTAR");
		else guiaFinalidadeModeloDt.setGuiaTipo(rs.getString("GUIA_TIPO"));
		guiaEmissaoDt.setGuiaFinalidadeModeloDt(guiaFinalidadeModeloDt);
		
		guiaEmissaoDt.setGuiaEmitidaSPG(false);
		
		guiaEmissaoDt.atualizeNumeroESeriePeloNumeroCompleto();
	}
	
	/**
	 * Método para atualizar o repasse do número da guia.
	 * @param String numeroGuiaCompleto 
	 * @throws Exception
	 */
	public void atualizarRepasse(String numeroGuiaCompleto, String valorRepasse, TJDataHora dataMovimento) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//fred
		String Sql = "UPDATE PROJUDI.GUIA_EMIS SET DATA_REPASSE  = ? ";
		ps.adicionarDate(new Date());
		Sql+= " , VALOR_RECEBIMENTO = ?";
		ps.adicionarDouble(valorRepasse);
		Sql+= " , DATA_MOVIMENTO = ?";
		ps.adicionarDate(dataMovimento);
		Sql+= " WHERE NUMERO_GUIA_COMPLETO = ?";
		ps.adicionarLong(numeroGuiaCompleto);		
		
		executarUpdateDelete(Sql, ps);		
	}
	
	/**
	 * Método para verificar se processo possui guias pendentes de pagamento
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 */
	public boolean verificarGuiasPendentesProcesso(String idProcesso) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.GUIA_EMIS GE LEFT JOIN PROC P ON (GE.ID_PROC = P.ID_PROC) LEFT JOIN GUIA_STATUS GS ON (GE.ID_GUIA_STATUS = GS.ID_GUIA_STATUS) WHERE ";
		sql += " GE.ID_PROC = ? AND GS.GUIA_STATUS_CODIGO IN (?, ?) AND GE.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?)";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		ps.adicionarLong(GuiaTipoDt.ID_FINAL);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				if ( rs1.next() )
					retorno = true;
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para vincular a guia inicial ao processo no momento do cadastro.
	 * @param String idGuiaEmissaoDt
	 * @param String idProcesso
	 * @param String idServentia
	 * @param String idComarca
	 * @return boolean
	 * @throws Exception
	 */
	public boolean vinculaGuiaProcesso(String idGuiaEmissaoDt, String idProcesso, String idServentia, String idComarca) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.GUIA_EMIS SET ID_PROC = ?, ID_SERV = ?";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(idServentia);
		if (idComarca != null && idComarca.trim().length() > 0) {
			Sql += ", ID_COMARCA = ?";
			ps.adicionarLong(idComarca);	
		}
		Sql += " WHERE ID_GUIA_EMIS = ? AND ID_PROC IS NULL AND ID_SERV IS NULL AND ID_GUIA_STATUS <> ?";
		ps.adicionarLong(idGuiaEmissaoDt);
		ps.adicionarLong(GuiaStatusDt.PEDIDO_RESSARCIMENTO_SOLICITADO);
		
		return executarUpdateDelete(Sql, ps) > 0;
	}
	
	/**
	 * Método para verificar se guia é do tipo complementar.
	 * 
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaComplementar(String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT NUMERO_GUIA_COMPLETO FROM PROJUDI.GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ? AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO in (?,?))";
		ps.adicionarLong(numeroGuiaCompleto);
		ps.adicionarLong(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU);
		ps.adicionarLong(GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU);
		
		ResultSetTJGO rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			if( rs1.next() ) {
				if( Funcoes.StringToInt(rs1.getString("NUMERO_GUIA_COMPLETO")) == Funcoes.StringToInt(numeroGuiaCompleto) ) {
					retorno = true;
				}
			}
			rs1.close();
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se guia é do tipo inicial.
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaInicial(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS WHERE ID_GUIA_EMIS = ? AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?,?))";
		ps.adicionarLong(idGuiaEmissao);
		ps.adicionarLong(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
		ps.adicionarLong(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
		
		ResultSetTJGO rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			if( rs1.next() ) {
				if( rs1.getString("ID_GUIA_EMIS").equals(idGuiaEmissao) ) {
					retorno = true;
				}
			}
			rs1.close();
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se guia é do tipo final ou final zero.
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaFinal_FinalZero(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS WHERE ID_GUIA_EMIS = ? AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?,?))";
		ps.adicionarLong(idGuiaEmissao);
		ps.adicionarLong(GuiaTipoDt.ID_FINAL);
		ps.adicionarLong(GuiaTipoDt.ID_FINAL_ZERO);
		
		ResultSetTJGO rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			if( rs1.next() ) {
				if( rs1.getString("ID_GUIA_EMIS").equals(idGuiaEmissao) ) {
					retorno = true;
				}
			}
			rs1.close();
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar se o processo possui guia final ou final zero AGUARDANDO PAGAMENTO.
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoPossuiGuiaFinal_FinalZeroAguardandoPagamento(String idProcesso) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT GUIA.ID_PROC FROM PROJUDI.GUIA_EMIS GUIA";
		sql += " INNER JOIN PROJUDI.PROC P ON (GUIA.ID_PROC = P.ID_PROC)";
		sql += " INNER JOIN PROJUDI.GUIA_MODELO MODELO ON (GUIA.ID_GUIA_MODELO = MODELO.ID_GUIA_MODELO)";
		sql += " WHERE MODELO.ID_GUIA_TIPO IN (?, ?)";
		sql += " AND GUIA.ID_PROC = ?";
		sql += " AND GUIA.ID_GUIA_STATUS = ?";
		
		ps.adicionarLong(GuiaTipoDt.ID_FINAL);
		ps.adicionarLong(GuiaTipoDt.ID_FINAL_ZERO);
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		
		ResultSetTJGO rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			if( rs1.next() ) {
				if( rs1.getString("ID_PROC").equals(idProcesso) ) {
					retorno = true;
				}
			}
			rs1.close();
		}
		
		return retorno;
	}
	
	public List<GuiaEmissaoDt> consultarGuiaEmissaoPorDataEmissao(String dataEmissao) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE DATA_EMIS >= ? AND DATA_EMIS <= ?";
		ps.adicionarDateTime(dataEmissao + " 00:00:00");
		ps.adicionarDateTime(dataEmissao + " 23:59:59");
		
		ResultSetTJGO rs1 = null;
		rs1 = consultar(sql, ps);
		
		if( rs1 != null ) {
			while( rs1.next() ) {
				if( listaGuiaEmissaoDt == null )
					listaGuiaEmissaoDt = new ArrayList<GuiaEmissaoDt>();
				
				GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
				
				associarDtCompleto(guiaEmissaoDt, rs1);
				
				listaGuiaEmissaoDt.add(guiaEmissaoDt);
			}
			rs1.close();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaPrefeituraGoianiaAguardandoPagamento(String idProcesso) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS GE";
		sql += " WHERE GE.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);		
		sql += " AND GE.ID_GUIA_STATUS IN (?, ?) "; ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO); ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		sql += " AND GE.ID_PROC = ? "; ps.adicionarLong(idProcesso);	
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null && rs1.next()) {
				guiaEmissaoDt = new GuiaEmissaoDt();				
				associarDtCompleto(guiaEmissaoDt, rs1);
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaPrefeituraGoianiaPaga(String idProcesso) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS GE";
		sql += " WHERE GE.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);		
		sql += " AND GE.ID_PROC = ? "; ps.adicionarLong(idProcesso);	
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			while(rs1 != null && rs1.next()) {
				guiaEmissaoDt = new GuiaEmissaoDt();				
				associarDtCompleto(guiaEmissaoDt, rs1);
				if (guiaEmissaoDt.isGuiaPaga()) break;
				guiaEmissaoDt = null;
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	public List<GuiaEmissaoDt> consultarGuiasPrefeituraDeGoiania(TJDataHora dataEmissao) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = new ArrayList<GuiaEmissaoDt>();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS GE";
		sql += " WHERE GE.ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ?) "; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);
		sql += " AND GE.DATA_EMIS >= ? "; dataEmissao.atualizePrimeiraHoraDia(); ps.adicionarDateTime(dataEmissao);
		sql += " AND GE.DATA_EMIS <= ? "; dataEmissao.atualizeUltimaHoraDia(); ps.adicionarDateTime(dataEmissao);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null) {
				while(rs1.next()) {
					GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();				
					associarDtCompleto(guiaEmissaoDt, rs1);
					listaGuiaEmissaoDt.add(guiaEmissaoDt);
				}				
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	
	/**
	 * Consulta guia inicial.
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaInicial(String numeroGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( numeroGuia != null ) {
				
				String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ? ";
				sql += " AND ID_GUIA_MODELO IN ( SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?,?) ) ";
				
				ps.adicionarLong(numeroGuia);
				
				ps.adicionarLong(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
				ps.adicionarLong(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
				
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Consulta guia inicial que não foi complementada, ou seja, não possui o seu id no campo id_guia_emis_princ de outra guia.
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaInicialNaoComplementada(String numeroGuia) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( numeroGuia != null ) {
				
				String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS VG WHERE VG.NUMERO_GUIA_COMPLETO = ? ";
				sql += " AND VG.ID_GUIA_MODELO IN ( SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?,?) ) ";
				sql += " AND VG.ID_GUIA_EMIS NOT IN ( ";
				sql += " SELECT G.ID_GUIA_EMIS_PRINC FROM PROJUDI.GUIA_EMIS G ";
				sql += " WHERE G.ID_GUIA_MODELO IN ( SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?,?) ) ";
				sql += " AND G.ID_GUIA_EMIS_PRINC = VG.ID_GUIA_EMIS ";
				sql += " ) ";
				
				ps.adicionarLong(numeroGuia);
				
				ps.adicionarLong(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
				ps.adicionarLong(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
				ps.adicionarLong(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
				ps.adicionarLong(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
				
				rs1 = this.consultar(sql, ps);
				if(rs1 != null) {
					while(rs1.next()) {
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar os metadados da guia.
	 * @param String numeroGuiaCompleto
	 * @return String
	 * @throws Exception
	 */
	public String consultaMetadadosGuia(String numeroGuiaCompleto) throws Exception {
		
		String Sql = "";		
		String retorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT METADADOS FROM projudi.GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ?";
		
		ps.adicionarLong(numeroGuiaCompleto);
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				retorno = rs1.getString("METADADOS");
			}
        } finally {
        	try {
            	 if (rs1 != null){
            		 rs1.close();            		 
            	 }
            }
            catch(Exception e) {
            }
        }
		return retorno;	
	}
	
	/**
	 * Método para consultar os dados de guias registradas do SPG.
	 * @param String numeroGuiaCompleto
	 * @return String
	 * @throws Exception
	 */
	public String consultaMetadadosGuiaSPG(String numeroGuiaCompleto) throws Exception {
		
		String Sql = "";		
		String retorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT METADADOS FROM projudi.GUIA_EMIS WHERE NUMERO_GUIA_COMPLETO = ?";
		
		ps.adicionarLong(numeroGuiaCompleto);
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				retorno = rs1.getString("METADADOS");
			}
        } finally {
        	try {
            	 if (rs1 != null){
            		 rs1.close();            		 
            	 }
            }
            catch(Exception e) {
            }
        }
		return retorno;	
	}
	
	/**
	 * Método para consultar guias do processo que são parcelas e que está com o status de aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaParceladaProcessoAguardandoPagamento(String idProcesso) throws Exception {
		
		List<GuiaEmissaoDt> listaGuiasEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS ";
		sql += " WHERE ID_PROC = ?";
		sql += " AND ID_GUIA_STATUS IN (?, ?)";
		sql += " AND ID_GUIA_EMIS_REFERENCIA IS NOT NULL";
		sql += " AND TIPO_GUIA_DESCONTO_PARCELAMENT = ?";
		sql += " ORDER BY DATA_EMIS ";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		ps.adicionarLong(GuiaEmissaoDt.TIPO_GUIA_PARCELADA);
		
		ResultSetTJGO rs1 = null;
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				rs1 = this.consultar(sql, ps);
				if( rs1 != null ) {
					
					GuiaEmissaoDt guiaEmissaoDt = null;
					
					while( rs1.next() ) {
						if( listaGuiasEmissaoDt == null ) {
							listaGuiasEmissaoDt = new ArrayList<GuiaEmissaoDt>();
						}
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
						
						listaGuiasEmissaoDt.add(guiaEmissaoDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiasEmissaoDt;
	}
	
	/**
	 * Método para consultar guias do processo que são GRS Genérica que está com o status de aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaGRSGenericaProcessoAguardandoPagamento(String idProcesso) throws Exception {
		
		List<GuiaEmissaoDt> listaGuiasEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS ";
		sql += " WHERE ID_PROC = ?";
		sql += " AND ID_GUIA_STATUS IN (?, ?)";
		sql += " AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?) )";
		sql += " ORDER BY DATA_EMIS ";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(GuiaStatusDt.ESTORNO_BANCARIO);
		ps.adicionarLong(GuiaTipoDt.ID_GUIA_GENERICA);
		
		ResultSetTJGO rs1 = null;
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				rs1 = this.consultar(sql, ps);
				if( rs1 != null ) {
					
					GuiaEmissaoDt guiaEmissaoDt = null;
					
					while( rs1.next() ) {
						if( listaGuiasEmissaoDt == null ) {
							listaGuiasEmissaoDt = new ArrayList<GuiaEmissaoDt>();
						}
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
						
						listaGuiasEmissaoDt.add(guiaEmissaoDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiasEmissaoDt;
	}
	
	/**
	 * Método para consultar parcelas das guias através da guia referencia.
	 * 
	 * @param String idGuiaEmissao
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarParcelasPelaGuiaReferencia(String idGuiaEmissaoReferencia) throws Exception {
		
		List<GuiaEmissaoDt> listaParcelasGuiasEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS ";
		sql += " WHERE ID_GUIA_EMIS_REFERENCIA = ? ";
		sql += " AND TIPO_GUIA_DESCONTO_PARCELAMENT = ? ";
		sql += " ORDER BY DATA_EMIS ";
		
		ps.adicionarLong(idGuiaEmissaoReferencia);
		ps.adicionarLong(GuiaEmissaoDt.TIPO_GUIA_PARCELADA);
		
		ResultSetTJGO rs1 = null;
		try {
			if( idGuiaEmissaoReferencia != null && !idGuiaEmissaoReferencia.isEmpty() ) {
				rs1 = this.consultar(sql, ps);
				if( rs1 != null ) {
					
					GuiaEmissaoDt guiaEmissaoDt = null;
					
					while( rs1.next() ) {
						if( listaParcelasGuiasEmissaoDt == null ) {
							listaParcelasGuiasEmissaoDt = new ArrayList<GuiaEmissaoDt>();
						}
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
						
						listaParcelasGuiasEmissaoDt.add(guiaEmissaoDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaParcelasGuiasEmissaoDt;
	}
	
	
	/**
	 * Método para consultar as guias inciais aguardando deferimento.
	 * 
	 * @param String idProcesso
	 * @return GuiaEmissaoDt guia inicial do processo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialAguardandoDeferimento(String idProcesso) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS " 
		+ " WHERE ( ID_PROC = ? AND ID_GUIA_STATUS = ? ) " 
		+ " AND ID_GUIA_TIPO IN (" 
		+ GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU + ","
		+ GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU
		+ " )";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_DEFERIMENTO);
		
		try{
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				
				if(rs1.next()) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					this.associarDt(guiaEmissaoDt, rs1);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias inciais aguardando deferimento.
	 * 
	 * @param String idProcesso
	 * @return GuiaEmissaoDt guia inicial do processo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialBaixadaIsencao(String idProcesso) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS " 
		+ " WHERE ( ID_PROC = ? AND ID_GUIA_STATUS = ? ) " 
		+ " AND ID_GUIA_TIPO IN (" 
		+ GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU + ","
		+ GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU
		+ " )";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaStatusDt.BAIXADA_COM_ISENCAO);
		
		try{
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				
				if(rs1.next()) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					this.associarDt(guiaEmissaoDt, rs1);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Atualizar o status, Aguardando Deferimento, da guia emitida 
	 * @param String idGuiaEmissao
	 * @param String idGuiaStatus
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusGuiaInicialAguardandoDeferimento(String idGuiaEmissao, String idGuiaStatus) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ? WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(idGuiaStatus);
		ps.adicionarLong(idGuiaEmissao);
		
		executarUpdateDelete(Sql, ps);

	}

	public void atualizarSituacaoBoleto(BoletoDt boleto) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET COD_SITUACAO_BOLETO = ?";
		ps.adicionarLong(boleto.getSituacaoBoleto().getCodigo());

		Sql += " ,NOSSO_NUMERO_BOLETO = ?";
		ps.adicionarString(boleto.getNossoNumero());
		
		Sql += " ,DATA_VENCIMENTO_BOLETO = ?";
		ps.adicionarDate(boleto.getDataVencimentoBoleto());
		
		
		if (boleto.getCodigoDeBarras() != null && !boleto.getCodigoDeBarras().isEmpty()) {
			Sql += " ,CODIGO_BARRAS_BOLETO = ?";
			ps.adicionarString(boleto.getCodigoDeBarras());
		}
		
		if (boleto.getLinhaDigitavel() != null && !boleto.getLinhaDigitavel().isEmpty()) {
			Sql += " ,LINHA_DIGITAVEL_BOLETO = ?";
			ps.adicionarString(boleto.getLinhaDigitavel());
		}
		
		if (boleto.getUrlPdf() != null && !boleto.getUrlPdf().isEmpty()) {
			Sql += " ,URL_PDF_BOLETO = ?";
			ps.adicionarString(boleto.getUrlPdf());
		}
		
		if (boleto.getObservacao1() != null && !boleto.getObservacao1().isEmpty()) {
			Sql += " ,OBSERVACAO1_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao1());
		}
		
		if (boleto.getObservacao2() != null && !boleto.getObservacao2().isEmpty()) {
			Sql += " ,OBSERVACAO2_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao2());
		}
		
		if (boleto.getObservacao3() != null && !boleto.getObservacao3().isEmpty()) {
			Sql += " ,OBSERVACAO3_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao3());
		}
		
		if (boleto.getObservacao4() != null && !boleto.getObservacao4().isEmpty()) {
			Sql += " ,OBSERVACAO4_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao4());
		}
		
		Sql += " WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(boleto.getId());
		
		executarUpdateDelete(Sql, ps);
	}

	public void salvar(BoletoDt boleto) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET COD_SITUACAO_BOLETO = ?";
		ps.adicionarLong(boleto.getSituacaoBoleto().getCodigo());
		
		Sql += " ,NOSSO_NUMERO_BOLETO = ?";
		ps.adicionarString(boleto.getNossoNumero());
		
		Sql += " ,NUMERO_DOCUMENTO_BOLETO = ?";
		ps.adicionarString(boleto.getNumeroDocumento());
		
		Sql += " ,DATA_EMISSAO_BOLETO = ?";
		ps.adicionarDate(boleto.getDataEmissaoBoleto());
		
		Sql += " ,DATA_VENCIMENTO_BOLETO = ?";
		ps.adicionarDate(boleto.getDataVencimentoBoleto());
		
		Sql += " ,VALOR_BOLETO = ?";
		ps.adicionarDecimal(boleto.getValorBoleto());
		
		if (boleto.getCodigoDeBarras() != null && !boleto.getCodigoDeBarras().isEmpty()) {
			Sql += " ,CODIGO_BARRAS_BOLETO = ?";
			ps.adicionarString(boleto.getCodigoDeBarras());
		}
		
		if (boleto.getLinhaDigitavel() != null && !boleto.getLinhaDigitavel().isEmpty()) {
			Sql += " ,LINHA_DIGITAVEL_BOLETO = ?";
			ps.adicionarString(boleto.getLinhaDigitavel());
		}
		
		if (boleto.getUrlPdf() != null && !boleto.getUrlPdf().isEmpty()) {
			Sql += " ,URL_PDF_BOLETO = ?";
			ps.adicionarString(boleto.getUrlPdf());
		}
		
		if (boleto.getPagador().getNome() != null && !boleto.getPagador().getNome().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_NOME = ?";
			ps.adicionarString(boleto.getPagador().getNome());
		}
		
		if (boleto.getPagador().getCpf() != null && !boleto.getPagador().getCpf().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_CPF = ?";
			ps.adicionarString(boleto.getPagador().getCpf());
		}
		
		if (boleto.getPagador().getRazaoSocial() != null && !boleto.getPagador().getRazaoSocial().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_RAZAO_SOCIAL = ?";
			ps.adicionarString(boleto.getPagador().getRazaoSocial());
		}
		
		if (boleto.getPagador().getCnpj() != null && !boleto.getPagador().getCnpj().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_CNPJ = ?";
			ps.adicionarString(boleto.getPagador().getCnpj());
		}
		
		if (boleto.getPagador().getLogradouro() != null && !boleto.getPagador().getLogradouro().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_LOGRADOURO = ?";
			ps.adicionarString(boleto.getPagador().getLogradouro());
		}
		
		if (boleto.getPagador().getBairro() != null && !boleto.getPagador().getBairro().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_BAIRRO = ?";
			ps.adicionarString(boleto.getPagador().getBairro());
		}
		
		if (boleto.getPagador().getCidade() != null && !boleto.getPagador().getCidade().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_CIDADE = ?";
			ps.adicionarString(boleto.getPagador().getCidade());
		}
		
		if (boleto.getPagador().getUf() != null && !boleto.getPagador().getUf().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_UF = ?";
			ps.adicionarString(boleto.getPagador().getUf());
		}
		
		if (boleto.getPagador().getCep() != null && !boleto.getPagador().getCep().isEmpty()) {
			Sql += " ,PAGADOR_BOLETO_CEP = ?";
			ps.adicionarString(boleto.getPagador().getCep());
		}
		
		if (boleto.getObservacao1() != null && !boleto.getObservacao1().isEmpty()) {
			Sql += " ,OBSERVACAO1_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao1());
		}
		
		if (boleto.getObservacao2() != null && !boleto.getObservacao2().isEmpty()) {
			Sql += " ,OBSERVACAO2_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao2());
		}
		
		if (boleto.getObservacao3() != null && !boleto.getObservacao3().isEmpty()) {
			Sql += " ,OBSERVACAO3_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao3());
		}
		
		if (boleto.getObservacao4() != null && !boleto.getObservacao4().isEmpty()) {
			Sql += " ,OBSERVACAO4_BOLETO = ?";
			ps.adicionarString(boleto.getObservacao4());
		}
		
		Sql += " WHERE ID_GUIA_EMIS = ? ";
		ps.adicionarLong(boleto.getId());
		
		executarUpdateDelete(Sql, ps);
	}

	public BoletoDt buscaBoletoPorId(String id_boleto) throws Exception {
		BoletoDt boleto = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS WHERE ID_GUIA_EMIS = ?";
		ps.adicionarLong(id_boleto);
		
		ResultSetTJGO rs1 = null;
		try {
			if (id_boleto != null) {
				rs1 = this.consultar(sql, ps);
				if (rs1 != null) {
					while (rs1.next()) {
						boleto = new BoletoDt();
						if (!associarDtBoleto(boleto, rs1))
							return null;
						associarDtCompleto(boleto, rs1);
					}
				}
			}
			
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
			}
		}
		return boleto;
	}
	
	private boolean associarDtBoleto(BoletoDt boleto, ResultSetTJGO rs1) throws Exception {
		String codSituacao = rs1.getString("COD_SITUACAO_BOLETO");
		if (codSituacao == null || codSituacao.isEmpty())
			return false;
		boleto.setSituacaoBoleto(SituacaoBoleto.comCodigo(codSituacao));
		boleto.setNossoNumero(rs1.getString("NOSSO_NUMERO_BOLETO"));
		boleto.setNumeroDocumento(rs1.getString("NUMERO_DOCUMENTO_BOLETO"));
		boleto.setDataEmissaoBoleto(rs1.getString("DATA_EMISSAO_BOLETO"));
		boleto.setDataVencimentoBoleto(rs1.getString("DATA_VENCIMENTO_BOLETO"));
		boleto.setValorBoleto(Funcoes.BancoDecimal(Funcoes.FormatarDecimal(rs1.getDouble("VALOR_BOLETO") / 100)));
		boleto.setCodigoDeBarras(rs1.getString("CODIGO_BARRAS_BOLETO"));
		boleto.setLinhaDigitavel(rs1.getString("LINHA_DIGITAVEL_BOLETO"));
		boleto.setUrlPdf(rs1.getString("URL_PDF_BOLETO"));
		String cpf = rs1.getString("PAGADOR_BOLETO_CPF");
		if(cpf != null && !cpf.isEmpty()){
			boleto.getPagador().setTipoPessoa(PagadorBoleto.TIPO_PESSOA_FISICA);
			boleto.getPagador().setNome(rs1.getString("PAGADOR_BOLETO_NOME"));
			boleto.getPagador().setCpf(cpf);
		} else{
			boleto.getPagador().setTipoPessoa(PagadorBoleto.TIPO_PESSOA_JURIDICA);
			boleto.getPagador().setRazaoSocial(rs1.getString("PAGADOR_BOLETO_RAZAO_SOCIAL"));
			boleto.getPagador().setCnpj(rs1.getString("PAGADOR_BOLETO_CNPJ"));			
		}
		boleto.getPagador().setLogradouro(rs1.getString("PAGADOR_BOLETO_LOGRADOURO"));
		boleto.getPagador().setBairro(rs1.getString("PAGADOR_BOLETO_BAIRRO"));
		boleto.getPagador().setCidade(rs1.getString("PAGADOR_BOLETO_CIDADE"));
		boleto.getPagador().setUf(rs1.getString("PAGADOR_BOLETO_UF"));
		boleto.getPagador().setCep(rs1.getString("PAGADOR_BOLETO_CEP"));
		boleto.setObservacao1(rs1.getString("OBSERVACAO1_BOLETO"));
		boleto.setObservacao2(rs1.getString("OBSERVACAO2_BOLETO"));
		boleto.setObservacao3(rs1.getString("OBSERVACAO3_BOLETO"));
		boleto.setObservacao4(rs1.getString("OBSERVACAO4_BOLETO"));
		return true;
	}
	
	/**
	 * Método para desvincular guia de processo no PJD.
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean desvincularGuiaProcesso(String idGuiaEmissao) throws Exception {

		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = "UPDATE PROJUDI.GUIA_EMIS SET ID_PROC = NULL, ID_SERV = NULL WHERE ID_GUIA_EMIS = ?";

		ps.adicionarLong(idGuiaEmissao);
		
		return executarUpdateDelete(Sql, ps) == 1;
		
	}
	
	/**
	 * Método para consultar as guias inciais aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @param String idUsuario
	 * @return List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialAguardandoPagamento(String idProcesso, String idUsuario) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS " 
		+ " WHERE ID_PROC = ? AND ID_USU = ? " 
		        + " AND ID_GUIA_STATUS IN (" + GuiaStatusDt.AGUARDANDO_PAGAMENTO + "," + GuiaStatusDt.ESTORNO_BANCARIO  +") "
				+ " AND ID_GUIA_TIPO IN ("+ GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU + ","+ GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU + " )";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(idUsuario);
		
		try{
			rs1 = consultar(sql, ps);
			if(rs1 != null) {
				if(rs1.next()) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					this.associarDt(guiaEmissaoDt, rs1);
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar guias de serviço aguardando pagamento do processo informado.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiasServicosAguardandoPagamentoProcesso(String idProcesso) throws Exception {
		
		List<GuiaEmissaoDt> listaGuiasEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS G";
		sql += " WHERE G.ID_PROC = ?";
		sql += " AND ID_GUIA_STATUS IN (?, ?)";
		sql += " AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO = ? )";
		sql += " AND TIPO_GUIA_DESCONTO_PARCELAMENT IS NULL ";
		sql += " AND NOT EXISTS ( SELECT 1 FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? and ID_GUIA_EMIS_REFERENCIA = G.ID_GUIA_EMIS )";
		sql += " ORDER BY DATA_EMIS ";
		
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
					
					GuiaEmissaoDt guiaEmissaoDt = null;
					
					while( rs1.next() ) {
						if( listaGuiasEmissaoDt == null ) {
							listaGuiasEmissaoDt = new ArrayList<GuiaEmissaoDt>();
						}
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
						
						listaGuiasEmissaoDt.add(guiaEmissaoDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiasEmissaoDt;
	}
	
	/**
	 * Método para consultar guias complementares aguardando pagamento do processo informado.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiasComplementaresAguardandoPagamentoProcesso(String idProcesso) throws Exception {
		
		List<GuiaEmissaoDt> listaGuiasEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_GUIA_EMIS G";
		sql += " WHERE G.ID_PROC = ?";
		sql += " AND ID_GUIA_STATUS IN (?, ?)";
		sql += " AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?, ?) )";
		sql += " AND TIPO_GUIA_DESCONTO_PARCELAMENT IS NULL ";
		sql += " AND NOT EXISTS ( SELECT 1 FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? and ID_GUIA_EMIS_REFERENCIA = G.ID_GUIA_EMIS )";
		sql += " ORDER BY DATA_EMIS ";
		
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
					
					GuiaEmissaoDt guiaEmissaoDt = null;
					
					while( rs1.next() ) {
						if( listaGuiasEmissaoDt == null ) {
							listaGuiasEmissaoDt = new ArrayList<GuiaEmissaoDt>();
						}
						guiaEmissaoDt = new GuiaEmissaoDt();
						
						associarDtCompleto(guiaEmissaoDt, rs1);
						
						listaGuiasEmissaoDt.add(guiaEmissaoDt);
					}
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiasEmissaoDt;
	}
	
	/**
	 * Método que consulta a quantidade de boletos emitidos no dia de hoje.
	 * 
	 * @return long
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public long consultarQuantidadeBoletosEmitidosHoje() throws Exception {
		
		long quantidadeBoletosEmitidosHoje = 0;
		
		String sql = "SELECT COUNT(ID_GUIA_EMIS) AS QUANTIDADE_BOLETOS FROM PROJUDI.GUIA_EMIS WHERE TO_NUMBER(TO_CHAR(DATA_EMISSAO_BOLETO, 'YYYYMMDD')) >= TO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMMDD'))";
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql, new PreparedStatementTJGO());
			if( rs1 != null ) {
				while(rs1.next()) {
					quantidadeBoletosEmitidosHoje = rs1.getLong("QUANTIDADE_BOLETOS");
				}
			}
		}
		finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return quantidadeBoletosEmitidosHoje;
	}
	
	/**
	 * Método para desfazer cancelamento da guia.
	 * 
	 * @param String idGuiaEmissao
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean desfazerCancelamentoGuia(String idGuiaEmissao) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " UPDATE PROJUDI.GUIA_EMIS SET ID_GUIA_STATUS = ?, DATA_CANCELAMENTO = null WHERE ID_GUIA_EMIS = ? ";

		ps.adicionarLong(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		ps.adicionarLong(idGuiaEmissao);
		
		return executarUpdateDelete(Sql, ps) == 1;
	}
	
	/**
	 * Método para verificar se o processo possui guia diferente da série 50.
	 * 
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean processoPossuiGuiaDiferenteSerie50(String idProcesso) throws Exception {
		
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(ID_GUIA_EMIS) AS QUANTIDADE FROM PROJUDI.GUIA_EMIS WHERE TO_CHAT(NUMERO_GUIA_COMPLETO) NOT LIKE '%50' AND ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getInt("QUANTIDADE") > 0 ) {
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
	 * Método que verifica se o id pertence a guia que foi parcelada.
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isGuiaFonteParcelamento(String idGuiaEmissao) throws Exception {
		
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(ID_GUIA_EMIS) AS QUANTIDADE FROM PROJUDI.GUIA_EMIS WHERE ID_GUIA_EMIS_REFERENCIA = ? AND ID_GUIA_STATUS <> ?";
		ps.adicionarLong(idGuiaEmissao);
		ps.adicionarLong(GuiaStatusDt.CANCELADA);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getInt("QUANTIDADE") > 0 ) {
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
	 * Método que verifica se guia é do mesmo processo da guia principal.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isGuiaMesmoProcessoGuiaPrincipal(String numeroGuiaCompleto, String idProcesso) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(GUIA.ID_GUIA_EMIS) AS QUANTIDADE FROM PROJUDI.GUIA_EMIS GUIA "
		+"WHERE GUIA.NUMERO_GUIA_COMPLETO = ? "
		+"AND GUIA.ID_GUIA_EMIS_PRINC IS NOT NULL "
		+"AND EXISTS (SELECT 1 FROM PROJUDI.GUIA_EMIS WHERE ID_PROC = ? AND ID_GUIA_EMIS = GUIA.ID_GUIA_EMIS_PRINC)";
		
		ps.adicionarLong(numeroGuiaCompleto);
		ps.adicionarLong(idProcesso);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getInt("QUANTIDADE") > 0 ) {
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
	 * Método que verifica se a guia tem oficial vinculado.
	 * Oficial no campo CODIGO_OFICIAL_SPG do SPG (Maneira antiga)
	 * Oficial vinculado no mandado (Central de mandado nova)
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isGuiaOficialVinculadoSPG_Mandando(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(GUIA.ID_GUIA_EMIS) AS QUANTIDADE FROM PROJUDI.GUIA_EMIS GUIA "+
					"INNER JOIN PROJUDI.GUIA_ITEM ITEM ON GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS "+
					"INNER JOIN PROJUDI.LOCOMOCAO LOC ON LOC.ID_GUIA_ITEM = ITEM.ID_GUIA_ITEM "+
					"WHERE GUIA.ID_GUIA_EMIS = ? "+
					"AND ( "+
					"    LOC.CODIGO_OFICIAL_SPG IS NOT NULL "+
					"    OR "+
					"    EXISTS ( "+
					"        SELECT 1 FROM PROJUDI.MAND_JUD "+ 
					"        WHERE ID_MAND_JUD = LOC.ID_MAND_JUD "+
					"        AND ID_USU_SERV_1 IS NOT NULL "+
					"    ) "+
					") ";
		
		ps.adicionarLong(idGuiaEmissao);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( rs1.getInt("QUANTIDADE") > 0 ) {
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
	 * Método que consulta as guias de locomoções com oficiais de justiça.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
 /*
	public List<GuiaEmissaoDt> consultarGuiaLocomocaoComOficialVinculado(String idProcesso) throws Exception {
		
		List<GuiaEmissaoDt> listaGuiasEmissaoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT u.nome as nomeUsuario, G.* FROM PROJUDI.VIEW_GUIA_EMIS G" 
		+ " INNER JOIN PROJUDI.GUIA_ITEM ITEM ON G.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS"
		+ " INNER JOIN PROJUDI.LOCOMOCAO LOC ON ITEM.ID_GUIA_ITEM = LOC.ID_GUIA_ITEM"
		//
		+ " INNER JOIN PROJUDI.MAND_JUD MJ ON MJ.ID_MAND_JUD = LOC.ID_MAND_JUD"
		+ " INNER JOIN PROJUDI.USU_SERV US ON US.ID_USU_SERV = MJ.ID_USU_SERV_1"
		+ " INNER JOIN PROJUDI.USU U ON U.ID_USU = US.ID_USU "
		//
		+ " WHERE LOC.ID_MAND_JUD IS NOT NULL"
		+ " AND LOC.CODIGO_OFICIAL_SPG IS NOT NULL"
		+ " AND ID_PROC = ? AND G.ID_GUIA_TIPO = ?";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(GuiaTipoDt.ID_LOCOMOCAO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			if( rs1 != null ) {
				
				GuiaEmissaoDt guiaEmissaoDt = null;
				
				while( rs1.next() ) {
					if( listaGuiasEmissaoDt == null ) {
						listaGuiasEmissaoDt = new ArrayList<GuiaEmissaoDt>();
					}
					guiaEmissaoDt = new GuiaEmissaoDt();
					
					associarDtCompleto(guiaEmissaoDt, rs1);
					
					guiaEmissaoDt.setUsuario(rs1.getString("nomeUsuario"));
					
					listaGuiasEmissaoDt.add(guiaEmissaoDt);
				}
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiasEmissaoDt;
	}
 /*
	
	/**
	 * Método que consulta as guias de locomocao vinculadas ao oficial de justica
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 * 
	 * @author fernando meireles
	 */
 
	public List<GuiaEmissaoDt> consultarGuiaLocomocaoComOficialVinculado(String idProcesso) throws Exception {
		
		List<GuiaEmissaoDt> listaGuiasEmissaoDt = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		GuiaEmissaoDt guiaEmissaoDt = null;
		GuiaModeloDt guiaModeloDt = null;
		
		String sql = "SELECT ge.id_guia_emis AS idGuiaEmis, ge.numero_guia_completo AS numeroGuia, ge.id_guia_modelo AS idGuiaModelo,"
				+ " ge.id_guia_status AS idGuiaStatus, ge.guia_tipo AS guiaTipo, ge.id_guia_tipo AS idGuiaTipo,"
				+ " u.nome AS nome, ge.data_emis AS dataEmissao, ge.data_vencimento AS dataVencimento,"
				+ " ge.data_recebimento AS dataRecebimento, ge.data_cancelamento AS dataCancelamento," 
				+ " ge.guia_status AS situacao, count(1)"
				+ " FROM projudi.view_guia_emis ge"
				+ " INNER JOIN projudi.guia_item gi ON gi.id_guia_emis = ge.id_guia_emis"			 
				+ " INNER JOIN projudi.locomocao l ON l.id_guia_item = gi.id_guia_item"
				+ " INNER JOIN projudi.mand_jud mj ON mj.id_mand_jud = l.id_mand_jud"
				+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1"
				+ " INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
				+ " WHERE l.id_mand_jud IS NOT NULL AND l.codigo_oficial_spg IS NOT NULL AND ge.id_proc = ?"
				+ " GROUP BY ge.id_guia_emis, ge.numero_guia_completo, ge.id_guia_modelo, ge.id_guia_status,"
				+ " ge.guia_tipo, ge.id_guia_tipo, u.nome, ge.data_emis,"
				+ " ge.data_vencimento, ge.data_recebimento, ge.data_cancelamento, ge.guia_status";   				
			
		ps.adicionarLong(idProcesso);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			while( rs1.next() ) {				
			      
				  guiaModeloDt = new GuiaModeloDt();
			 	  guiaEmissaoDt = new GuiaEmissaoDt();
				   
				  guiaModeloDt.setId(rs1.getString("idGuiaModelo"));
				  guiaModeloDt.setId_GuiaTipo(rs1.getString("idGuiaTipo"));
			      guiaModeloDt.setGuiaTipo(rs1.getString("guiaTipo"));
			 	  guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
			     
			 	  guiaEmissaoDt.setId(rs1.getString("idGuiaEmis"));				  
				  guiaEmissaoDt.setNumeroGuiaCompleto(rs1.getString("numeroGuia")); 				
			      guiaEmissaoDt.setId_GuiaStatus(rs1.getString("idGuiaStatus"));
			      guiaEmissaoDt.setUsuario(rs1.getString("nome"));
			      guiaEmissaoDt.setDataEmissao(rs1.getString("dataEmissao"));
			      guiaEmissaoDt.setDataVencimento(rs1.getString("dataVencimento"));
			      guiaEmissaoDt.setDataRecebimento(rs1.getString("dataRecebimento"));
			      guiaEmissaoDt.setDataCancelamento(rs1.getString("dataCancelamento"));				  
			      guiaEmissaoDt.setGuiaStatus(rs1.getString("situacao"));	
			      guiaEmissaoDt.setGuiaEmitidaSPG(false);
			  
			      listaGuiasEmissaoDt.add(guiaEmissaoDt);	  
		   }
		}
		catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaGuiasEmissaoDt;
	}	 
	
	/**
	 * Método para verificar se guia tem boleto emitido.
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isBoletoEmitido(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS GUIA";
		sql += " WHERE ID_GUIA_EMIS = ?";
		sql += " AND CODIGO_BARRAS_BOLETO IS NOT NULL";
		sql += " AND DATA_EMISSAO_BOLETO IS NOT NULL";
		
		ps.adicionarLong(idGuiaEmissao);
		
		ResultSetTJGO rs1 = consultar(sql, ps);
		if( rs1 != null ) {
			if( rs1.next() ) {
				if( rs1.getString("ID_GUIA_EMIS").equals(idGuiaEmissao) ) {
					retorno = true;
				}
			}
			rs1.close();
		}
		
		return retorno;
	}
}