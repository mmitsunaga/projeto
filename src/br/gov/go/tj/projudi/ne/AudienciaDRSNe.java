package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.AudienciaAnexoPs;
import br.gov.go.tj.projudi.ps.AudienciaDRS2Ps;
import br.gov.go.tj.projudi.ps.AudienciaDRS3Ps;
import br.gov.go.tj.projudi.ps.ProcessoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

//---------------------------------------------------------
public class AudienciaDRSNe extends Negocio {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3271712919537658324L;

	protected AudienciaDRSDt obDados;

	public AudienciaDRSNe() {
		obLog = new LogNe();
		obDados = new AudienciaDRSDt();
	}

	public String Verificar(AudienciaDRSDt audienciaDRSDt, 
			                ProcessoDt processoDt) throws Exception {
		AudienciaPublicadaNe audienciaPublicadaNe = new AudienciaPublicadaNe();		
		String mensagem = "";
		
		if (audienciaDRSDt.getProcessoNumero() == null || audienciaDRSDt.getProcessoNumero().trim().length() == 0) {
			mensagem += "Número do processo da audiência deve ser informado.";
		} else if (!Funcoes.validaProcessoNumero(audienciaDRSDt.getProcessoNumero())) {
			mensagem += "Número do processo da audiência  inválido.";
		} else if (Funcoes.desformataNumeroProcesso(audienciaDRSDt.getProcessoNumero()).trim().equalsIgnoreCase(Funcoes.desformataNumeroProcesso(processoDt.getProcessoNumeroCompleto()))) {
			mensagem += "Número do processo da audiência da audiência deve ser diferente do processo selecionado. \n Para vincular as audiências do processo selecionado, escolha a opção Sincronizar Audiências DRS.";
		}
		
		if (mensagem.trim().length() == 0) {
			List<AudienciaDRSDt> listaDeAudiencias = new ArrayList<AudienciaDRSDt>();
			
			audienciaPublicadaNe.carregueListaDeAudienciasDRSOracleDRS3(audienciaDRSDt.getProcessoNumero(), listaDeAudiencias);

			audienciaPublicadaNe.carregueListaDeAudienciasDRSOracleDRS2(audienciaDRSDt.getProcessoNumero(), listaDeAudiencias);
			
			boolean encontrou = false;
			for(AudienciaDRSDt audienciaDtCorrente : listaDeAudiencias) {
				if (audienciaDtCorrente.getDataHoraDaAudiencia().equals(audienciaDRSDt.getDataHoraDaAudiencia())) {
					encontrou = true;
					break;
				}			
			}
			
			if (!encontrou) {
				mensagem = "Audiência não localizada no DRS para o processo " + audienciaDRSDt.getProcessoNumero() + " na data e hora " + audienciaDRSDt.getDataHoraDaAudiencia().getDataFormatadaddMMyyyyHHmm() + ".";		
			}
		}		

		return mensagem;
	}

	public void salvar(AudienciaDRSDt audienciaDRSDt, ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {

		AudienciaPublicadaNe audienciaPublicadaNe = new AudienciaPublicadaNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			LogDt logDt = new LogDt("VinculacaoAudienciaDRS", processoDt.getId(), audienciaDRSDt.getId_UsuarioLog(), audienciaDRSDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaDRSDt.getPropriedades());

			// Gera movimentações de AUDIÊNCIAS PUBLICADAS
			audienciaPublicadaNe.executeProcessamentoAudienciaDt(processoDt.getId(), usuarioDt, audienciaDRSDt, movimentacaoNe, logDt, obFabricaConexao);
			
			obDados.copiar(audienciaDRSDt);
			obLog.salvar(logDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List<AudienciaDRSDt> consulteListaDeAudiencias(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception {
		List<AudienciaDRSDt> listaDeAudiencias = new ArrayList<AudienciaDRSDt>();
		
		carregueListaDeAudienciasDRS3x(numeroProcessoCompletoDt, listaDeAudiencias);

		carregueListaDeAudienciasDRS2x(numeroProcessoCompletoDt, listaDeAudiencias);
		
		return listaDeAudiencias;
	}
	
	public AudienciaDRSDt consulteAudiencia(NumeroProcessoDt numeroProcessoCompletoDt, TJDataHora dataHoraAudiencia) throws Exception {
		AudienciaDRSDt audienciaDt = null;
		
		List<AudienciaDRSDt> listaDeAudiencias = this.consulteListaDeAudiencias(numeroProcessoCompletoDt);
		
		for(AudienciaDRSDt audienciaDtCorrente : listaDeAudiencias) {
			if (audienciaDtCorrente.getDataHoraDaAudiencia().getDataHoraFormatadayyyyMMddHHmm().equals(dataHoraAudiencia.getDataHoraFormatadayyyyMMddHHmm())) audienciaDt = audienciaDtCorrente;
		}
		
		return audienciaDt;
	}
	
	private void carregueListaDeAudienciasDRS2x(NumeroProcessoDt numeroProcessoCompletoDt, List<AudienciaDRSDt> listaDeAudiencias) throws Exception
	{
		FabricaConexao obFabricaConexao = null;	
		try
		{
			obFabricaConexao = FabricaConexao.criarConexaoDRS2();
			listaDeAudiencias.addAll(new AudienciaDRS2Ps(obFabricaConexao).consulteListaDeAudiencias(numeroProcessoCompletoDt));			 
		}
		catch (Exception e) {
			if (listaDeAudiencias.size() == 0) throw e;
		}
		finally
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
	}
	
	private void carregueListaDeAudienciasDRS3x(NumeroProcessoDt numeroProcessoCompletoDt, List<AudienciaDRSDt> listaDeAudiencias) throws Exception
	{
		FabricaConexao obFabricaConexao = FabricaConexao.criarConexaoDRS3();	
		try
		{
			listaDeAudiencias.addAll(new AudienciaDRS3Ps(obFabricaConexao).consulteListaDeAudiencias(numeroProcessoCompletoDt));
			
			AudienciaAnexoPs audienciaAnexoPs = new AudienciaAnexoPs(obFabricaConexao);
			for(AudienciaDRSDt audienciaDtCorrente : listaDeAudiencias) {
				audienciaAnexoPs.consulteListaAnexosDeAudiencias(audienciaDtCorrente);
			}
		}
		finally
		{
			obFabricaConexao.fecharConexao();
		}
	}
}
