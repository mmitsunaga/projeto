package br.gov.go.tj.projudi.sessaoVirtual.ne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.PendenciaTipoNe;
import br.gov.go.tj.projudi.ps.PendenciaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;
public class PendenciaSessaoVirtualNe extends PendenciaNe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PendenciaDt gerarPendenciaSessaoVirtualPJD(String idServentiaCargo, String idServentia, UsuarioDt usuario, String idProcesso,
			String dataLimite, String idAudienciaProcesso, int codigoPendencia, FabricaConexao conexao, String statusPendencia) throws Exception, MensagemException {
		PendenciaDt pendenciaDt = new PendenciaDt();
		//lrcampos 04/03/2020 11:55 - Incluindo usuario Cadastrador como o usuario logado no sistema
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		DateTimeFormatter formatterSegundos = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		pendenciaDt.setDataInicio(LocalDateTime.now().format(formatterSegundos));
		Boolean podeInserirMaisUmaPendencia = false;
		if(statusPendencia != null) {
			pendenciaDt.setPendenciaStatusCodigo(statusPendencia);
			podeInserirMaisUmaPendencia = true;
		}
		pendenciaDt.setDataLimite(dataLimite);
		PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(codigoPendencia, conexao);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo não encontrado");
		}
		pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		List<PendenciaDt> emAndamento = pendenciaPs.consultarPendenciasAudienciaProcessoResponsavelPorTipo(idAudienciaProcesso, idServentiaCargo, codigoPendencia);
		if(!podeInserirMaisUmaPendencia && emAndamento != null && !emAndamento.isEmpty()) {
			return null;
		}
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		// Grava o log da operacao
		this.gravarLogPJD(pendenciaDt, conexao);
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		if(idServentiaCargo == null && idServentia == null) {
			pendenciaResponsavelDt.setId_UsuarioResponsavel(usuario.getId_UsuarioServentia());
		} else {
			if(idServentiaCargo != null) {
				pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
			} else {
				pendenciaResponsavelDt.setId_Serventia(idServentia);
			}
		}
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
		return pendenciaDt;
	}
	public List<PendenciaDt> consultarPendenciasAudienciaProcessoPorListaTipoPJD(String idAudienciaProcesso, FabricaConexao obFabricaConexao, Integer... pendenciaTipoCodigo)
			throws Exception {
		return consultarPendenciasAudienciaProcessoPorListaTipoPJD(idAudienciaProcesso, obFabricaConexao, null, pendenciaTipoCodigo);
	}
	public List<PendenciaDt> consultarPendenciasAudienciaProcessoPorListaTipoPJD(String idAudienciaProcesso, FabricaConexao obFabricaConexao, String idServentiaProcesso, Integer... pendenciaTipoCodigo)
			throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, idServentiaProcesso, null, pendenciaTipoCodigo);
	}
	public PendenciaDt gerarPendenciaVotoSessaoVirtualPJD(String idServentiaCargo, UsuarioDt usuario, String idProcesso, String dataLimite, String idAudienciaProcesso) throws Exception {
		FabricaConexao conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			PendenciaDt pendenciaDt = gerarPendenciaVotoSessaoVirtualPJD(idServentiaCargo, usuario, idProcesso, dataLimite, idAudienciaProcesso, conexao);
			return pendenciaDt;
		} catch (Exception e) {
			conexao.cancelarTransacao();
			throw e;
		} finally {
			conexao.fecharConexao();
		}
	}
	// jvosantos - 22/08/2019 15:58 - Adicionar ID da AUDI_PROC para gerar a pendência de voto da sessão virtual, para verificar se existe pendência aberta 
    public PendenciaDt gerarPendenciaVotoSessaoVirtualPJD(String idServentiaCargo, UsuarioDt usuario, String idProcesso, String dataLimite, String idAudienciaProcesso, FabricaConexao conexao) throws Exception {
    	return gerarPendenciaSessaoVirtualPJD(idServentiaCargo, null, usuario, idProcesso, dataLimite, idAudienciaProcesso, PendenciaTipoDt.VOTO_SESSAO, conexao, null);
	}
    /**
     * Grava o log da operacao na pendencia Depende que ja tenha uma conexao
     * aberta
     * 
     * @author Ronneesley Moura Teles
     * @since 06/06/2008 10:22
     * @param dados
     *            dados da pendencia
     * @param conexao
     *            fabrica de conexao utilizada
     * @throws Exception
     */
    private boolean gravarLogPJD(PendenciaDt dados, FabricaConexao conexao) throws Exception {
        LogDt obLogDt;
        if (dados.getId().trim().equalsIgnoreCase("") || dados.getId() == null) {
            obLogDt = new LogDt("Pendencia", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
        } else {
            obLogDt = new LogDt("Pendencia", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
        }
        this.obLog.salvar(obLogDt, conexao);
        return true;
    }
	//lrcampos 24/07/2020 - Gera pendencia voto minerva	
	public PendenciaDt gerarPendenciaVotoMinervaPJD(String idServentiaCargo, UsuarioDt usuario, String idProcesso,
			String dataLimite, String idAudienciaProcesso, int codigoPendencia, FabricaConexao conexao) throws Exception, MensagemException {
		PendenciaDt pendenciaDt = new PendenciaDt();
		//lrcampos 04/03/2020 11:55 - Incluindo usuario Cadastrador como o usuario logado no sistema
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		DateTimeFormatter formatterSegundos = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		pendenciaDt.setDataInicio(LocalDateTime.now().format(formatterSegundos));
		pendenciaDt.setDataLimite(dataLimite);
		PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(codigoPendencia, conexao);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo não encontrado");
		}
		pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		// Grava o log da operacao
		this.gravarLogPJD(pendenciaDt, conexao);
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
		return pendenciaDt;
	}
}