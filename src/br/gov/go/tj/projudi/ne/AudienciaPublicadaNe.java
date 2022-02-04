package br.gov.go.tj.projudi.ne;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.projudi.dt.AudienciaPublicadaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.AudienciaDRS2Ps;
import br.gov.go.tj.projudi.ps.AudienciaDRS3Ps;
import br.gov.go.tj.projudi.ps.AudienciaPublicadaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AudienciaPublicadaNe extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6997590285954231403L;
		
	private LogNe obLog;

	public AudienciaPublicadaNe() {				
		obLog = new LogNe();
	}
	
	public synchronized void executeProcessamento() throws Exception {
		MovimentacaoNe movimentacaoNe;
		try{
			movimentacaoNe = new MovimentacaoNe();
			List listaAudienciasPublicadas = consultarTodasAudienciasPublicadasNaoProcessadas();			
			if (listaAudienciasPublicadas != null && listaAudienciasPublicadas.size() > 0){
				
				UsuarioDt usuarioDt = new UsuarioDt();
	            usuarioDt.setId(UsuarioDt.SistemaProjudi);
	            usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
	            usuarioDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
	            usuarioDt.setIpComputadorLog("127.0.0.1");
	            usuarioDt.setNome("Sistema PROJUDI");
	            
				for (Iterator iterator = listaAudienciasPublicadas.iterator(); iterator.hasNext();) {
					AudienciaPublicadaDt audiencia = (AudienciaPublicadaDt) iterator.next();         
		            executeProcessamentoAudienciaDt(audiencia, usuarioDt, movimentacaoNe);
				}
			}						
		}catch(Exception e){
			graveErroExecucaoAutomatica(e);
		}
		finally{
			movimentacaoNe = null;
		}
	}
	
	private List consultarTodasAudienciasPublicadasNaoProcessadas() throws Exception{
		List tempList = null;

        FabricaConexao obFabricaConexao = null;
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            AudienciaPublicadaPs obPersistencia = new AudienciaPublicadaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarTodasAudienciasPublicadasNaoProcessadas();

        
        } finally{
            obFabricaConexao.fecharConexao();
        }

        return tempList;
	}
	
	private void executeProcessamentoAudienciaDt(AudienciaPublicadaDt audienciaPublicadaDt, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe) throws Exception{		 
		FabricaConexao obFabricaConexao = null;		 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			AudienciaPublicadaPs obPersistencia = new AudienciaPublicadaPs(obFabricaConexao.getConexao());		
			
			String id_Processo = obPersistencia.consultarIdProcesso(audienciaPublicadaDt.getProcessoNumero());
			
			if (id_Processo != null && id_Processo.trim().length() > 0){				
				LogDt obLogDt = new LogDt("AudienciaPublicada", id_Processo, usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaPublicadaDt.getPropriedades());
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String dataHoraAudienciaPublicada = df.format(Funcoes.DataHora( audienciaPublicadaDt.getDataAudiencia()));
				
				String complemento = "<a target=\"_blank\" href=\"" + AudienciaPublicadaDt.IDENTIFICADOR_WEBSITE  + "?" + AudienciaPublicadaDt.PROPRIEDADE_PROCESSO + "=" + audienciaPublicadaDt.getProcessoNumero() + "&" + AudienciaPublicadaDt.PROPRIEDADE_DATA + "=" + dataHoraAudienciaPublicada + "&" + AudienciaPublicadaDt.PROPRIEDADE_HASH + "=" + AudienciaPublicadaDt.IDENTIFICADOR_HASH + "\">" + AudienciaPublicadaDt.IDENTIFICADOR_MENSAGEM + "</a>";
				
				movimentacaoNe.gerarMovimentacaoAudienciaPublicada(id_Processo, usuarioDt.getId_UsuarioServentia(), complemento, obLogDt, obFabricaConexao);
				
				obPersistencia.excluirAudienciaPublicada(audienciaPublicadaDt);
				
				obLog.salvar(obLogDt, obFabricaConexao);
			}else{				
				obPersistencia.atualizeAudienciaPublicadaProcessoNaoEncontrado(audienciaPublicadaDt);
			}		
			
			obFabricaConexao.finalizarTransacao();
		}catch(Exception e){			
			try{obFabricaConexao.cancelarTransacao();} catch(Exception e1) {e1.printStackTrace();}
			graveErroExecucaoAutomatica(e);
		 } finally{
			 obFabricaConexao.fecharConexao();
	     }
	}
	
	private void graveErroExecucaoAutomatica(Exception exExecucaoAutomatica){
		try{
			obLog.salvar(new LogDt("AudienciaPublicada", "", UsuarioServentiaDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.Erro), "",  obtenhaConteudoExcecao(exExecucaoAutomatica)));
	    }catch(Exception e){
			exExecucaoAutomatica.printStackTrace();
			e.printStackTrace();
		}
	}	
	
	private String obtenhaConteudoExcecao(Exception ex){
    	try{
    		return Funcoes.obtenhaConteudoPrimeiraExcecao(ex);
    	}catch(Exception e){
    		return ex.getMessage();
    	}    	
    }
	
	public void executeProcessamentoAudienciaPublicadaImportacao(String id_Processo, String processoNumeroCompleto, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception{		 
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		
		List<AudienciaDRSDt> listaDeAudiencias = new ArrayList<AudienciaDRSDt>();
		
		NumeroProcessoDt numeroCompletoDt = new NumeroProcessoDt(processoNumeroCompleto);
		
		carregueListaDeAudienciasDRSOracleDRS3(numeroCompletoDt, listaDeAudiencias);

		carregueListaDeAudienciasDRSOracleDRS2(numeroCompletoDt, listaDeAudiencias);
		
		for(AudienciaDRSDt audienciaDtCorrente : listaDeAudiencias) {
			executeProcessamentoAudienciaDt(id_Processo, usuarioDt, audienciaDtCorrente, movimentacaoNe, logDt, obFabricaConexao);
		}
	}
	
	public void carregueListaDeAudienciasDRSOracleDRS3(String processoNumeroCompleto, List<AudienciaDRSDt> listaDeAudiencias) throws Exception
	{
		NumeroProcessoDt numeroCompletoDt = new NumeroProcessoDt(processoNumeroCompleto);
		this.carregueListaDeAudienciasDRSOracleDRS3(numeroCompletoDt, listaDeAudiencias);
	}
	
	public void carregueListaDeAudienciasDRSOracleDRS3(NumeroProcessoDt processoNumeroCompleto, List<AudienciaDRSDt> listaDeAudiencias) throws Exception
	{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.DRS3);	
		try
		{
			listaDeAudiencias.addAll(new AudienciaDRS3Ps(obFabricaConexao).consulteListaDeAudiencias(processoNumeroCompleto));			 
		}
		catch (Exception ex) {
			throw new MensagemException("Não foi possível consultar as audiências no DRS3.", ex);
		}
		finally
		{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void carregueListaDeAudienciasDRSOracleDRS2(String processoNumeroCompleto, List<AudienciaDRSDt> listaDeAudiencias) throws Exception
	{
		NumeroProcessoDt numeroCompletoDt = new NumeroProcessoDt(processoNumeroCompleto);
		this.carregueListaDeAudienciasDRSOracleDRS2(numeroCompletoDt, listaDeAudiencias);
	}
	
	public void carregueListaDeAudienciasDRSOracleDRS2(NumeroProcessoDt processoNumeroCompleto, List<AudienciaDRSDt> listaDeAudiencias) throws Exception
	{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.DRS3);	
		try
		{
			listaDeAudiencias.addAll(new AudienciaDRS2Ps(obFabricaConexao).consulteListaDeAudiencias(processoNumeroCompleto));			 
		} 
		catch (Exception ex) {
			throw new MensagemException("Não foi possível consultar as audiências no DRS2.", ex);
		}
		finally
		{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void executeProcessamentoAudienciaDt(String id_Processo, UsuarioDt usuarioDt, AudienciaDRSDt audienciaDRSDt, MovimentacaoNe movimentacaoNe, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception{		 
		LogDt obLogDt = new LogDt("AudienciaPublicada", id_Processo, logDt.getId_UsuarioLog(), logDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaDRSDt.getPropriedades());
		String dataHoraAudienciaPublicada = audienciaDRSDt.getDataHoraDaAudiencia().getDataHoraFormatadayyyyMMddHHmmss();
		
		String complemento = "<a target=\"_blank\" href=\"" + AudienciaPublicadaDt.IDENTIFICADOR_WEBSITE  + "?" + AudienciaPublicadaDt.PROPRIEDADE_PROCESSO + "=" + audienciaDRSDt.getProcessoNumero() + "&" + AudienciaPublicadaDt.PROPRIEDADE_DATA + "=" + dataHoraAudienciaPublicada + "&" + AudienciaPublicadaDt.PROPRIEDADE_HASH + "=" + AudienciaPublicadaDt.IDENTIFICADOR_HASH + "\">" + AudienciaPublicadaDt.IDENTIFICADOR_MENSAGEM + "</a>";
		
		movimentacaoNe.gerarMovimentacaoAudienciaPublicada(id_Processo, usuarioDt.getId_UsuarioServentia(), complemento, obLogDt, obFabricaConexao);
	}
	
	public void sincronizeAudiencias(ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);	
		try
		{
			obFabricaConexao.iniciarTransacao();
			
			// Sincronizando as audiências do processo...
			sincronizeAudiencias(processoDt, usuarioDt, movimentacaoNe, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();			
		} catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			throw new Exception(ex);
		}
		finally
		{
			obFabricaConexao.fecharConexao();
		}
	}
	
	private void sincronizeAudiencias(ProcessoDt processoDt, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, FabricaConexao obFabricaConexao) throws Exception {
		List<AudienciaDRSDt> listaDeAudienciasDRS = new ArrayList<AudienciaDRSDt>();
		
		carregueListaDeAudienciasDRSOracleDRS3(processoDt.getNumeroProcessoDt(), listaDeAudienciasDRS);

		carregueListaDeAudienciasDRSOracleDRS2(processoDt.getNumeroProcessoDt(), listaDeAudienciasDRS);
		
		AudienciaPublicadaPs obPersistencia = new AudienciaPublicadaPs(obFabricaConexao.getConexao());
		LogDt logDt = new LogDt("AudienciaPublicada", processoDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", "");
		
		List<AudienciaDRSDt> listaDeAudienciasPJD = obPersistencia.consultarTodasAudienciasPublicadasProcessadas(processoDt.getId());
		
		for(AudienciaDRSDt audienciaDtCorrente : listaDeAudienciasDRS) {
			if (!listaDeAudienciasPJD.contains(audienciaDtCorrente)) {
				executeProcessamentoAudienciaDt(processoDt.getId(), usuarioDt, audienciaDtCorrente, movimentacaoNe, logDt, obFabricaConexao);
			}
		}
	}
}