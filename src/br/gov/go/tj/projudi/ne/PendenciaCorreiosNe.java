package br.gov.go.tj.projudi.ne;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Utilities;

import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaCorreiosDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.PendenciaCorreiosPs;
import br.gov.go.tj.projudi.util.EnvelopeCartaUtil;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class PendenciaCorreiosNe extends PendenciaCorreiosNeGen {

	private static final long serialVersionUID = 6612127515297613283L;

	public String Verificar(PendenciaCorreiosDt dados) {
		String stRetorno = "";
		return stRetorno;
	}
	
	public void salvar(PendenciaCorreiosDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());

		/* use o id do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().length() == 0) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("PendenciaCorreios", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("PendenciaCorreios", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public PendenciaCorreiosDt consultarIdPendencia(String id_Pendencia) throws Exception {

		PendenciaCorreiosDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdPendencia(id_Pendencia);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarIdPendenciaCorreios(String id_Pendencia) throws Exception {

		String idPendCorreios = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			idPendCorreios = obPersistencia.consultarIdPendenciaCorreios(id_Pendencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return idPendCorreios;
	}
	
	public PendenciaCorreiosDt consultarIdPendenciaStatus(String id_Pendencia, String idPendenciaStatus) throws Exception {

		PendenciaCorreiosDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdPendenciaStatus(id_Pendencia, idPendenciaStatus);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarIdPendenciaRastreamento(String codigoRastreamento, String idPendenciaStatus) throws Exception {

		String idPendencia;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			idPendencia = obPersistencia.consultarIdPendenciaRastreamento(codigoRastreamento, idPendenciaStatus);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return idPendencia;
	}
	
	public void reverterEnvioLote(String lote, LogDt obLogDt) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			
			obPersistencia.reverterEnvioLote(lote);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarPendenciasLote(String lote) throws Exception {

		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarPendenciasLote(lote);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Método que consuta a quantidade de OS aberta
	 * 
	 * @param String idProcesso
	 * @return String
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public String consultaQuantidadePendenciaCorreiosOrdemServicoAbertas(String idProcesso) throws Exception {
		String retorno = "0";
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultaQuantidadePendenciaCorreiosOrdemServicoAbertas(idProcesso);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
	 * Método que consuta a quantidade de OS fechadas
	 * 
	 * @param String idProcesso
	 * @return String
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public String consultaQuantidadePendenciaFinalCorreiosOrdemServicoFechada(String idProcesso) throws Exception {
		String retorno = "0";
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultaQuantidadePendenciaFinalCorreiosOrdemServicoFechada(idProcesso);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public PendenciaCorreiosDt consultarFinalizadaIdPendencia(String id_Pendencia) throws Exception {

		PendenciaCorreiosDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarFinalizadaIdPendencia(id_Pendencia);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public byte[] baixarArquivoECarta(String id_PendenciaCorreios, ProcessoDt processoDt, UsuarioDt usuarioDt, HttpServletResponse response, InputStream template, LogDt logDt) throws Exception{
		MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
		movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL));
		byte[] byModelo = null;
		
		if (new MovimentacaoArquivoNe().podeBaixarArquivo(usuarioDt, processoDt, movimentacaoArquivoDt)){
			PendenciaCorreiosDt pendenciaCorreiosDt = consultarIdInclusiveFinal(id_PendenciaCorreios);
			if(pendenciaCorreiosDt != null && pendenciaCorreiosDt.getMetaDados() != null && pendenciaCorreiosDt.getMetaDados().length() > 10) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				CorreiosDt correiosDt = objectMapper.readValue(pendenciaCorreiosDt.getMetaDados(), CorreiosDt.class);
				CorreiosDt.ModeloCarta modeloCarta = new CorreiosDt().getModelo(pendenciaCorreiosDt.getCodigoModelo());
				String html = new CorreiosNe().montaConteudoPorModelo(correiosDt, modeloCarta.getId_Modelo());
				byModelo = html.getBytes();
				byModelo = ConverterHtmlPdf.converteCartaPDF(byModelo, Utilities.millimetersToPoints(12), 0, Utilities.millimetersToPoints(99));
				EnvelopeCartaUtil e = new EnvelopeCartaUtil.Builder(template)
				.setCodigoRastreamento(pendenciaCorreiosDt.getCodigoRastreamento())
				.setDataExpedicao(pendenciaCorreiosDt.getDataExpedicao())
				.setNomeDestinatario(correiosDt.getNomeDestinatario())
				.setEnderecoDestinatario(correiosDt.getEnderecoDestinatario())
				.setNumeroDestinatario(correiosDt.getNumeroEnderecoDestinatario())
				.setComplementoDestinatario(correiosDt.getComplementoEnderecoDestinatario())
				.setBairroDestinatario(correiosDt.getBairroDestinatario())
				.setCidadeDestinatario(correiosDt.getCidadeDestinatario())
				.setUfDestinatario(correiosDt.getUfDestinatario())
				.setCepDestinatario(correiosDt.getCepDestinatario()) 			
				.setAnexo(byModelo)
				.build();
				byModelo = e.gerar();
				
				LogNe logNe = new LogNe();
				logNe.salvar(new LogDt("PendenciaCorreios", pendenciaCorreiosDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", pendenciaCorreiosDt.getPropriedades()));
			}
		} else throw new MensagemException("Acesso negado.");
		return byModelo;	
	}
	
	public PendenciaCorreiosDt consultarIdInclusiveFinal(String id_pendcorreios) throws Exception {

		PendenciaCorreiosDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdFinal(id_pendcorreios);
			
			if(dtRetorno == null) {
				dtRetorno = obPersistencia.consultarId(id_pendcorreios);
			}
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

}