package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GuiaQuantidadeMaximaParcelasPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class GuiaQuantidadeMaximaParcelasNe extends Negocio {
	
	private static final long serialVersionUID = 496612263660599L;
	
	public GuiaQuantidadeMaximaParcelasNe() {
		obLog = new LogNe();
	}

	public boolean salvar(String idGuiaReferencia, String quantidadeMaximaParcelas, String motivo, String idUsuario, String ipComputadorLog, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		
		GuiaQuantidadeMaximaParcelasPs obPersistencia = new GuiaQuantidadeMaximaParcelasPs(obFabricaConexao.getConexao());
		
		String id = this.consultaIdQuantidadeMaximaParcelamento(idGuiaReferencia, obFabricaConexao);
		
		//Existe já no banco?
		if( id != null ) {
			retorno = this.alterar(id, idGuiaReferencia, quantidadeMaximaParcelas, motivo, idUsuario, ipComputadorLog, obFabricaConexao);
			
			if( retorno ) {
				LogDt obLogDt = new LogDt("GuiaQuantidadeMaximaParcelas", id, idUsuario, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ID_GUIA_QUANT_MAX_PARCELAS:"+id+";ID_GUIA_EMIS:"+idGuiaReferencia+";QUANT_MAX_PARCELAS:"+quantidadeMaximaParcelas+";MOTIVO:"+motivo+"]");
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			else {
				throw new MensagemException("Erro ao salvar.");
			}
		}
		else {
			id = obPersistencia.inserir(idGuiaReferencia, quantidadeMaximaParcelas, motivo, idUsuario);
			if( id != null ) {
				LogDt obLogDt = new LogDt("GuiaQuantidadeMaximaParcelas", id, idUsuario, ipComputadorLog, String.valueOf(LogTipoDt.Incluir), "", "[ID_GUIA_QUANT_MAX_PARCELAS:"+id+";ID_GUIA_EMIS:"+idGuiaReferencia+";QUANT_MAX_PARCELAS:"+quantidadeMaximaParcelas+";MOTIVO:"+motivo+"]");
				obLog.salvar(obLogDt, obFabricaConexao);
				
				retorno = true;
			}
			else {
				throw new MensagemException("Erro ao salvar.");
			}
		}
		
		return retorno;
	}
	
	public boolean alterar(String idGuiaQuantidadeMaximaParcela, String idGuiaReferencia, String quantidadeMaximaParcelas, String motivo, String idUsuario, String ipComputadorLog, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		
		GuiaQuantidadeMaximaParcelasPs obPersistencia = new GuiaQuantidadeMaximaParcelasPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.alterar(idGuiaQuantidadeMaximaParcela, idGuiaReferencia, quantidadeMaximaParcelas, motivo, idUsuario);
//		if( retorno ) {
//			LogDt obLogDt = new LogDt("GuiaQuantidadeMaximaParcelas", idGuiaQuantidadeMaximaParcela, idUsuario, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[ID_GUIA_QUANT_MAX_PARCELAS:"+idGuiaQuantidadeMaximaParcela+";ID_GUIA_EMIS:"+idGuiaReferencia+";QUANT_MAX_PARCELAS:"+quantidadeMaximaParcelas+"]");
//			obLog.salvar(obLogDt, obFabricaConexao);
//		}
//		else {
//			throw new MensagemException("Erro ao salvar.");
//		}
		
		return retorno;
	}
	
	public String consultaQuantidadeMaximaParcelamento(String idGuiaReferencia) throws Exception {
		String quantidade = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idGuiaReferencia != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaQuantidadeMaximaParcelasPs obPersistencia = new GuiaQuantidadeMaximaParcelasPs(obFabricaConexao.getConexao());
				
				quantidade = obPersistencia.consultaQuantidadeMaximaParcelamento(idGuiaReferencia);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return quantidade;
	}
	
	public String consultaQuantidadeMaximaParcelamento(String idGuiaReferencia, FabricaConexao obFabricaConexao) throws Exception {
		String quantidade = null;
		
		if( idGuiaReferencia != null ) {
			GuiaQuantidadeMaximaParcelasPs obPersistencia = new GuiaQuantidadeMaximaParcelasPs(obFabricaConexao.getConexao());
			
			quantidade = obPersistencia.consultaQuantidadeMaximaParcelamento(idGuiaReferencia);
		}
		
		return quantidade;
	}
	
	public String consultaIdQuantidadeMaximaParcelamento(String idGuiaReferencia) throws Exception {
		String idQuantidadeMaximaParcelamento = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idGuiaReferencia != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaQuantidadeMaximaParcelasPs obPersistencia = new GuiaQuantidadeMaximaParcelasPs(obFabricaConexao.getConexao());
				
				idQuantidadeMaximaParcelamento = obPersistencia.consultaIdQuantidadeMaximaParcelamento(idGuiaReferencia);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return idQuantidadeMaximaParcelamento;
	}
	
	public String consultaIdQuantidadeMaximaParcelamento(String idGuiaReferencia, FabricaConexao obFabricaConexao) throws Exception {
		String idQuantidadeMaximaParcelamento = null;
		
		if( idGuiaReferencia != null ) {
			
			GuiaQuantidadeMaximaParcelasPs obPersistencia = new GuiaQuantidadeMaximaParcelasPs(obFabricaConexao.getConexao());
			
			idQuantidadeMaximaParcelamento = obPersistencia.consultaIdQuantidadeMaximaParcelamento(idGuiaReferencia);
		}
		
		return idQuantidadeMaximaParcelamento;
	}
	
}
