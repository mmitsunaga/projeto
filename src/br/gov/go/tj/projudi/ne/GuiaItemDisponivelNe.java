package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.GuiaItemDisponivelDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GuiaItemDisponivelPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class GuiaItemDisponivelNe extends Negocio {

	private static final long serialVersionUID = 8433177209336876592L;
	
	protected  GuiaItemDisponivelDt obDados;
	
	public GuiaItemDisponivelNe() {
		obLog = new LogNe();
		obDados = new GuiaItemDisponivelDt();
	}
	
	public void salvar(GuiaItemDisponivelDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		
		GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
		if (dados.getId() == null || dados.getId().length()==0) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("GuiaItemDisponivel",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.toString());
		}
		else {
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("GuiaItemDisponivel",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.toString(),dados.toString());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);		
	}
	
	/**
	 * Método para liberar guia-item-disponivel da pendencia descartada.
	 * @param String idPend
	 * @param String idProc
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean liberarGuiaItemPendencia(String idPend, String idProc, FabricaConexao obFabricaConexao) throws Exception {
		if( idPend == null || (idPend != null && idPend.isEmpty()) ) {
			throw new MensagemException("Erro ao liberar a Pendência ao item de Despesa Postal");
		}
		if( idProc == null || (idProc != null && idProc.isEmpty()) ) {
			throw new MensagemException("Erro ao liberar o Processo ao item de Despesa Postal");
		}
		GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
		return obPersistencia.liberarGuiaItemPendencia(idPend, idProc);
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
	public boolean vincularGuiaItemDepesaPostalPendencia(String idPend, String idProc, String quantidade, FabricaConexao obFabricaConexao) throws Exception {
		if( idPend == null || (idPend != null && idPend.isEmpty()) ) {
			throw new MensagemException("Erro ao vincular Pendência ao item de Despesa Postal");
		}
		if( idProc == null || (idProc != null && idProc.isEmpty()) ) {
			throw new MensagemException("Erro ao vincular Processo ao item de Despesa Postal");
		}
		GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
		return obPersistencia.vincularGuiaItemDepesaPostalPendencia(idPend, idProc, quantidade);
	}
	
	/**
	 * Método para vincular guia-item-disponivel de despesa postal com MOVIMENTACAO que JA TENHA ID_PEND(pendencia) vinculada.
	 * @param String idMovi
	 * @param String idPend
	 * @param String idProc
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean vincularGuiaItemDespesaPostalMovimentacao(String idMovi, String idPend, String idProc, FabricaConexao obFabricaConexao) throws Exception {
		if( idPend == null || (idPend != null && idPend.isEmpty()) ) {
			throw new MensagemException("Erro ao vincular Pendência ao item de Despesa Postal");
		}
		if( idProc == null || (idProc != null && idProc.isEmpty()) ) {
			throw new MensagemException("Erro ao vincular Processo ao item de Despesa Postal");
		}
		if( idMovi == null || (idMovi != null && idMovi.isEmpty()) ) {
			throw new MensagemException("Erro ao vincular Movimentação ao item de Despesa Postal");
		}
		GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
		return obPersistencia.vincularGuiaItemDespesaPostalMovimentacao(idMovi, idPend, idProc);
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
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
			return obPersistencia.processoPossuiItemCustaDespesaPostalEmitidoPagoSemVinculoPendencia(idProcesso);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
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
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
			return obPersistencia.processoPossuiItemCustaDespesaPostalEmitidoNaoPago(idProcesso);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que consulta a guantidade de itens de despesa postal NAO pago.
	 * 
	 * @param String idProcesso
	 * @return int
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public int consultaQuantidadeItemDepesaPostalNaoPago(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
			return obPersistencia.consultaQuantidadeItemDepesaPostalNaoPago(idProcesso);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que consulta a guantidade de itens despesa postal processo PAGO e SEM vinculo com Pendencia.
	 * @param String idProcesso
	 * @return Integer
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public int consultaQuantidadeItemDepesaPostalPagoSemVinculoPendencia(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
			return obPersistencia.consultaQuantidadeItemDepesaPostalPagoSemVinculoPendencia(idProcesso);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que consulta a guantidade de itens despesa postal vinculado com Pendencia.
	 * @param String idProcesso
	 * @return Integer
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public int consultaQuantidadeItemVinculadoPendencia(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
			return obPersistencia.consultaQuantidadeItemVinculadoPendencia(idProcesso);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Verifica se a guia está vinculada a pendencia OU movimentacao.
	 * 
	 * @param String idGuiaEmissao
	 * @param FabricaConexao obFabricaConexao
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean isGuiaVinculadaPendenciaOUMovimentacao(String idGuiaEmissao, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		
		if( idGuiaEmissao != null && !idGuiaEmissao.isEmpty() && obFabricaConexao != null ) {
			GuiaItemDisponivelPs obPersistencia = new GuiaItemDisponivelPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isGuiaVinculadaPendenciaOUMovimentacao(idGuiaEmissao);
		}
		
		return retorno;
	}
}
