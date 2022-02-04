package br.gov.go.tj.projudi.ne;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.cms.CMSException;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AssinarConclusaoDt;
import br.gov.go.tj.projudi.dt.AssinarPendenciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PeticionamentoDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.PreAnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.dwrMovimentarProcesso;
import br.gov.go.tj.projudi.ps.MovimentacaoComplementoPs;
import br.gov.go.tj.projudi.ps.MovimentacaoPs;
import br.gov.go.tj.projudi.ps.PendenciaPs;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.utils.ConflitoDeAbasException;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.projudi.dt.AudienciaDt;

public class MovimentacaoNe extends MovimentacaoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -253242170674598792L;

	public String Verificar(MovimentacaoDt dados) {
		String stRetorno = "";
		if (dados.getId_MovimentacaoTipo() == null || dados.getId_MovimentacaoTipo().equalsIgnoreCase("null") || dados.getId_MovimentacaoTipo().length() == 0) stRetorno += "Selecione o Tipo da Movimentação. \n";
		//if (dados.getListaPendenciasGerar().size() == 0) stRetorno += "É necessário inserir uma pendência. \n";

		return stRetorno;
	}

	/**
	 * Método que verifica se os dados obrigatórios em uma movimentação foram preenchidos
	 * 
	 * @param dados
	 *            objeto com dados da movimentação a ser verificada
	 * @author msapaula
	 */
	public String verificarMovimentacaoGenerica(MovimentacaoProcessoDt dados) throws Exception {
		String stRetorno = "";
		boolean procPrecatoria = false;
		ProcessoDt procCompleto = null;
		ProcessoDt proc = null;

		List listProc = dados.getListaProcessos();

		if (listProc != null && listProc.size() > 0) {
			for (int i = 0; i < listProc.size(); i++) {
//				if (listProc.get(i) instanceof ProcessoCompletoDt) {
					procCompleto = (ProcessoDt) listProc.get(i);
					if (procCompleto != null && procCompleto.getProcessoTipoCodigo() != null && Funcoes.StringToInt(procCompleto.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA) {
						procPrecatoria = true;
					}
					
					if (procCompleto != null && procCompleto.isProcessoPrecatoriaExpedidaOnline() ) {
						procPrecatoria = true;
					}
					
					if (procCompleto.isArquivado()) {
						MovimentacaoTipoNe movimentacaoTipoNe = new MovimentacaoTipoNe();
						MovimentacaoTipoDt moviTipoDt = movimentacaoTipoNe.consultarId(dados.getId_MovimentacaoTipo());
						if(!moviTipoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.PETICAO_ENVIADA))
								&& !moviTipoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.CERTIDAO_EXPEDIDA))
								&& !moviTipoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.JUNTADA_DE_PETICAO))){
							stRetorno = "Movimentação não permitida para processo ARQUIVADO.";
						}
					}else if(procCompleto.isErroMigracao()){
						stRetorno = "Movimentação não permitida para processo com ERRO DE MIGRAÇÃO.";
					}
					
//				} else if (listProc.get(i) instanceof ProcessoDt) {
//					proc = (ProcessoDt) listProc.get(i);
//				}
	
				if (proc != null && proc.getProcessoTipoCodigo() != null && Funcoes.StringToInt(proc.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA) {
					procPrecatoria = true;
				}
				
				if (proc != null && proc.isProcessoPrecatoriaExpedidaOnline() ) {
					procPrecatoria = true;
				}
						
			}
		}else if(listProc==null || listProc.size() == 0){
			stRetorno += "Deve existir ao menos um processo.\n";
		}

		if ((dados.getId_MovimentacaoTipo() == null || dados.getId_MovimentacaoTipo().equalsIgnoreCase("null") || dados.getId_MovimentacaoTipo().length() == 0) && (procPrecatoria == false)) stRetorno += "Selecione o Tipo da Movimentação. \n";
		//De acordo com Cássia(9º juizado) não é obrigatorio a inserção de um arquivo, resolver depois
		if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) stRetorno += "É necessário inserir um arquivo. \n";

		return stRetorno;
	}

	/**
	 * Método que verifica se os dados obrigatórios em uma geração de pendência em movimentação foram preenchidos
	 * 
	 * @param dados
	 *            objeto com dados da movimentação a ser verificada
	 * @author msapaula
	 * @throws Exception 
	 */
	public String verificarMovimentacaoPendencia(MovimentacaoProcessoDt dados) throws Exception{
		String stRetorno = "";
		if (dados.getListaProcessos() == null || dados.getListaProcessos().size() == 0) stRetorno += "Nenhum Processo foi selecionado. \n";
		if (dados.getListaPendenciasGerar() == null || dados.getListaPendenciasGerar().size() == 0) stRetorno += "É necessário inserir uma pendência. \n";
		
		ProcessoNe processoNe = new ProcessoNe();
		//retiro o erro de perda de dados da memoria
		if (dados !=null && dados.getId_Processo()!=null && dados.getId_Processo().length()>0){
			ProcessoDt processoDt = processoNe.consultarIdCompleto(dados.getId_Processo());
		
			if (processoDt.isArquivado()) {
				for (int i = 0; i < dados.getListaPendenciasGerar().size(); i++) {
					dwrMovimentarProcesso pendenciaDt = (dwrMovimentarProcesso)dados.getListaPendenciasGerar().get(i);
					if(!pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.DESARQUIVAMENTO))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DECISAO))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DESPACHO))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_GENERICO))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_PRESIDENTE))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_RELATOR))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_SENTENCA))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO))
							&& !pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO))){
								stRetorno += "Pendência "+ pendenciaDt.getPendenciaTipo() +" não permitida para processo ARQUIVADO.";
					}
				}
			}else if (processoDt.isErroMigracao()) {
				stRetorno += "Não é possível gerar pendência para processo com ERRO DE MIGRAÇÃO.";
			}
			
			for (int i = 0; i < dados.getListaPendenciasGerar().size(); i++) {
				dwrMovimentarProcesso pendenciaDt = (dwrMovimentarProcesso)dados.getListaPendenciasGerar().get(i);
				if(pendenciaDt.isPendenciaTipo( new int[]{ PendenciaTipoDt.ENVIAR_INSTANCIA_SUPERIOR,PendenciaTipoDt.RETORNAR_SERVENTIA_ORIGEM})){
					FabricaConexao obFabricaConexao = null;
					try{
						obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
						if (new PendenciaNe().verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) {
							stRetorno += "Processo " + processoDt.getProcessoNumero() + " há pendência(s) em aberto. \n";
						}
					} finally{
						obFabricaConexao.fecharConexao();
					}
				}
			}
			
			//Bloqueio para evitar que o processo seja enviado ao presidente em duplicidade
			for (int i = 0; i < dados.getListaPendenciasGerar().size(); i++) {
				dwrMovimentarProcesso pendenciaDt = (dwrMovimentarProcesso)dados.getListaPendenciasGerar().get(i);
				FabricaConexao obFabricaConexao = null;
				if(pendenciaDt.isPendenciaTipo( new int[]{PendenciaTipoDt.ENVIAR_PROCESSO_PRESIDENTE_UNIDADE})){
					try{
						obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
						String idRelator = new ProcessoResponsavelNe().consultarRelatorProcesso(processoDt.getId(), obFabricaConexao);
						if(idRelator != null) {
							ServentiaCargoDt presidenteDt = new ServentiaCargoNe().getPresidenteSegundoGrau(processoDt.getId_Serventia(), obFabricaConexao);
							if(presidenteDt==null) {
								stRetorno += "Presidente não localizado.\n";
							}else if(idRelator.equalsIgnoreCase(presidenteDt.getId())){
								stRetorno += "Processo já se encontra com o Presidente do Órgão. \n";
							}
						}
						if(new PendenciaNe().verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
							stRetorno += "Processo não pode ser enviado ao Presidente, pois possui conclusões em aberto. \n";
						}
					} finally{
						obFabricaConexao.fecharConexao();
					}
				}
			}
			
			//Bloqueio para evitar que o processo seja enviado ao relator em duplicidade
			for (int i = 0; i < dados.getListaPendenciasGerar().size(); i++) {
				dwrMovimentarProcesso pendenciaDt = (dwrMovimentarProcesso)dados.getListaPendenciasGerar().get(i);
				FabricaConexao obFabricaConexao = null;
				if(pendenciaDt.isPendenciaTipo( new int[]{PendenciaTipoDt.RETORNAR_AUTOS_RELATOR_PROCESSO})){
					try{
						obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
						String idRelator = new ProcessoResponsavelNe().consultarRelatorProcesso(processoDt.getId(), obFabricaConexao);
						if(idRelator != null) {
							ServentiaCargoDt presidenteDt = new ServentiaCargoNe().getPresidenteSegundoGrau(processoDt.getId_Serventia(), obFabricaConexao);
							if(!idRelator.equalsIgnoreCase(presidenteDt.getId())){
								stRetorno += "Funcionalidade disponível somente quando o processo se encontra sob responsabilidade do Presidente do Órgão. \n";
							}
						}
						if(new PendenciaNe().verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
							stRetorno += "Processo não pode ser enviado ao Relator, pois possui conclusões em aberto. \n";
						}
					} finally{
						obFabricaConexao.fecharConexao();
					}
				}
			}

			//se o processo tiver recurso aguardando autuação não poderá gerar novas pendências.
			RecursoNe recursoNe = new RecursoNe();
			if(recursoNe.existeRecursoNaoAutuadoProcesso(processoDt.getId_Processo(), processoDt.getId_Serventia())) {
				stRetorno += "Não é possível gerar pendências em processos que possuem recurso aguardando autuação.";
			}
			
			//BO 2020/7211 - se o processo tiver pendência do tipo VERIFRICAR DISTRIBUIÇÃO ativa, não poderá gerar autos conclusos
			for (int i = 0; i < dados.getListaPendenciasGerar().size(); i++) {
				dwrMovimentarProcesso pendenciaDt = (dwrMovimentarProcesso)dados.getListaPendenciasGerar().get(i);
				if(pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DECISAO))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DESPACHO))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_GENERICO))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_PRESIDENTE))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_RELATOR))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_SENTENCA))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO))
						|| pendenciaDt.getCodPendenciaTipo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO))){
					
					ProcessoDt processo = processoNe.consultarIdCompleto(dados.getId_Processo());
					List listaPendencias = new PendenciaNe().consultarPendenciasAbertasProcesso(dados.getId_Processo());
					for (int j = 0; j < listaPendencias.size(); j++) {
						PendenciaDt pendenciaTemp = (PendenciaDt) listaPendencias.get(j);
						if(pendenciaTemp.getPendenciaTipoCodigo().equals(String.valueOf(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO))) {
							stRetorno += "Processo com pendência ativa do tipo Verificar Distribuição. É preciso verificar a pendência antes de enviar os autos conclusos.";
						}
					}
				}
			}
		}else{
			stRetorno += "Houve uma falha, número do processo não disponível. \n";
		}
		
		return stRetorno;
	}

	/**
	 * Método que verifica se uma movimentação processual pode ser efetuada dependendo do tipo de ação marcada para ser gerada, ou se existem restrições. Ex: Envio para Turma - verifica se pode enviar, caso contrario, retorna erro
	 * 
	 * @param movimentacaoDt
	 *            , objeto com dados da movimentação
	 * @param grupoCodigo
	 *            , grupo do usuário que está movimentando
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String podeMovimentarProcesso(MovimentacaoProcessoDt movimentacaoDt, String grupoCodigo) throws Exception{
		String stRetorno = "";
		
		List processos = movimentacaoDt.getListaProcessos();
		List pendenciasGerar = movimentacaoDt.getListaPendenciasGerar();

		// Para cada pendência marcada para ser gerada verifica se há alguma restrição
		if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
			PendenciaNe pendenciaNe = new PendenciaNe();
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				//Se tratar de uma movimentação proveniente de Acesso de Outra Serventia bloqueia algumas ações
				if (movimentacaoDt.getAcessoOutraServentia().length() > 0) {
					stRetorno = pendenciaNe.verificaPendenciasProcessoOutraServentia(pendenciasGerar);
				}
				else {
					stRetorno += pendenciaNe.verificaPendenciasProcesso(processoDt, pendenciasGerar, grupoCodigo, false);
				}
			}
		}
		
		//Em atendimento ao BO 2019/13635, apenas movimentações para arquivamento são permitidas nos processos de execução penal. 
		if(movimentacaoDt.getListaProcessos() != null) {
			for(ProcessoDt processoDt: (List<ProcessoDt>) movimentacaoDt.getListaProcessos()){
				//Tratando-se de um processo de execução penal, a única movimentação que pode ser feita
				//é a que contém uma pendência de arquivamento do processo. BO: 2019/13635 - 2019/14873
				//Se o processo for de uma classe de execução penal e for de assunto diferente de Acordo de não persecução penal
				if( processoDt.isExecucaoPenal() && !processoDt.isAcordoNaoPersecucaoPenal() && (
					movimentacaoDt.getListaPendenciasGerar() == null
					|| movimentacaoDt.getListaPendenciasGerar().size() != 1
					|| !((dwrMovimentarProcesso) movimentacaoDt.getListaPendenciasGerar().get(0)).isArquivamento())) {
					
					ServentiaDt serventiaDt = this.consultarServentiaProcesso(processoDt.getId_Serventia());
					//e for de uma serventia do subtipo execução penal
					if(serventiaDt != null && serventiaDt.isSubTipoExecucaoPenal()) {
						stRetorno += "A movimentação não foi concluída. Processos de execução penal só podem ser movimentados para arquivamento. Decreto judiciário n.º 2029/2019.";
					}
				}
			}
		}
		
		return stRetorno;
	}
	
	/**
	 * Método que verifica se uma movimentação processual pode ser efetuada dependendo do tipo de ação marcada para ser gerada, ou se existem restrições. Ex: Envio para Turma - verifica se pode enviar, caso contrario, retorna erro
	 * 
	 * @param movimentacaoDt
	 *            , objeto com dados da movimentação
	 * @param grupoCodigo
	 *            , grupo do usuário que está movimentando
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String obtemAlertaMovimentarProcesso(MovimentacaoProcessoDt movimentacaoDt, String grupoCodigo) throws Exception{
		String stRetorno = "";
		
		List processos = movimentacaoDt.getListaProcessos();
		List pendenciasGerar = movimentacaoDt.getListaPendenciasGerar();

		// Para cada pendência marcada para ser gerada verifica se há alguma restrição
		if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
			PendenciaNe pendenciaNe = new PendenciaNe();
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				//Se tratar de uma movimentação proveniente de Acesso de Outra Serventia bloqueia algumas ações
				if (movimentacaoDt.getAcessoOutraServentia().length() > 0) {
					stRetorno += "";
				}
				else {
					stRetorno += pendenciaNe.obtemAlertaMovimentarProcesso(processoDt, pendenciasGerar, grupoCodigo);
				}
			}
		}
		
		
		return stRetorno;
	}

	/**
	 * Método que verifica se uma movimentação pode ter seu status modificado (válido ou inválido). Somente poderá se não for uma movimentação do sistema, e se o usuário for juiz ou analista judiciário
	 * 
	 * @param dados
	 *            objeto com dados da movimentação
	 * @param grupoCodigo
	 *            , grupo do usuário que está tentando invalidar
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String podeMudarStatusMovimentacao(MovimentacaoDt movimentacaoDt, UsuarioDt usuarioDt) throws Exception{
		String stRetorno = "";
		
		if (movimentacaoDt.getId_UsuarioRealizador().equalsIgnoreCase(UsuarioServentiaDt.SistemaProjudi) && 
			Funcoes.StringToInt(movimentacaoDt.getMovimentacaoTipoCodigo()) != MovimentacaoTipoDt.AUDIENCIA_PUBLICADA &&
			(Funcoes.StringToInt(movimentacaoDt.getMovimentacaoTipoCodigo()) != MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO && 
			 movimentacaoDt.getComplemento() != null && 
			 !movimentacaoDt.getComplemento().trim().equalsIgnoreCase("Histórico Processo Físico"))) {
			stRetorno = "Movimentação gerada pelo Sistema Projudi.";
		} else {
			//verifia se foi um magistrado que gerou a movimentação
			boolean isMagistrado = new UsuarioServentiaNe().consultarGrupoUsuarioServentia(movimentacaoDt.getId_UsuarioRealizador());

			if (isMagistrado && Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU && Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.JUIZ_TURMA && Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
				stRetorno = "Movimentação gerada por Magistrado só pode ser bloqueada ou desbloqueado por Magistrado.";
			}
		}
		
		if ( movimentacaoDt.getCodigoTemp() != null && movimentacaoDt.getCodigoTemp().equalsIgnoreCase(String.valueOf(MovimentacaoArquivoDt.ACESSO_SOMENTE_MAGISTRADO)) &&
				 (usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.JUIZ_AUXILIAR && usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.JUIZ_TURMA 
					&& usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU && usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)
				){
			stRetorno = "Não foi possível alterar a visibilidade da Movimentação, essa movimentação possui nível de visibilidade Magistrado, somente o magistrado pode alterar a visibilidade da mesma.";
		}

		if (stRetorno.length() > 0) stRetorno = "Movimentação não pode ter Status Modificado. Motivo: " + stRetorno;
		
		return stRetorno;
	}

	/**
	 * Método que verifica se uma análise de conclusão pode ser efetuada de acordo com as pendências marcadas para serem geradas, pois se houver alguma restrição não pode permitir que análise seja concluída.
	 * 
	 * Ex.: Enviar para turma recursal, se tiver audiências em aberto não pode enviar
	 * 
	 * @param analisePendenciaDt
	 *            , dados da análise que está sendo efetuada pelo juiz
	 * @param grupoCodigo
	 *            , grupo do usuário que está analisando
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String podeAnalisarConclusao(AnaliseConclusaoDt analisePendenciaDt, String grupoCodigo, String grupoTipoCodigo) throws Exception{
		String stRetorno = "";
		
		List pendenciasFechar = analisePendenciaDt.getListaPendenciasFechar();
		List pendenciasGerar = analisePendenciaDt.getListaPendenciasGerar();

		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		if (pendenciasFechar!=null){			
			for (int i = 0; i < pendenciasFechar.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);
				ProcessoDt processoDt = pendenciaDt.getProcessoDt();
				if (processoDt == null) processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo());

				if (pendenciasGerar != null && pendenciasGerar.size() > 0) stRetorno += pendenciaNe.verificaPendenciasProcesso(processoDt, pendenciasGerar, grupoCodigo, true);

				if (grupoTipoCodigo != null && (grupoTipoCodigo.trim().equalsIgnoreCase(String.valueOf(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)) || grupoTipoCodigo.trim().equalsIgnoreCase(String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE)) || grupoTipoCodigo.trim().equalsIgnoreCase(String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO)) || grupoTipoCodigo.trim().equalsIgnoreCase(String.valueOf(GrupoTipoDt.ASSESSOR_DESEMBARGADOR)) || grupoTipoCodigo.trim().equalsIgnoreCase(String.valueOf(GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU))) && processoDt != null && processoDt.getServentiaTipoCodigo() != null && processoDt.getServentiaTipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU))
						&& pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO))) {

					List<AudienciaProcessoDt> audienciasProcessoDt = audienciaProcessoNe.consultarSessoesMarcadas(processoDt.getId());
					if (pendenciaDt.getCodigoTemp() != null && pendenciaDt.getCodigoTemp().trim().equalsIgnoreCase(String.valueOf(PendenciaDt.VOTO_VENCIDO_RELATOR)) && audienciasProcessoDt.size() > 0) {
						stRetorno += "O voto / ementa do redator ainda não foi inserido. \n";
					}
				}
			}
		}

		return stRetorno;
	}

	/**
	 * Método para salvar uma movimentação e que recebe conexão como parâmetro
	 * 
	 * @author msapaula
	 */
	public void salvar(MovimentacaoDt dados, FabricaConexao obFabricaConexao)throws Exception{
		LogDt obLogDt;
		try{
			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Movimentacao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Movimentacao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			dados.setId("");
			throw e;
		}
	}
	
	/**
	 * Consulta as movimentações de um determinado processo Cria conexão internamente
	 */
	public List consultarMovimentacoesProcesso(UsuarioDt usuarioDt, String id_processo, int inNivelAcessoUsuario)throws Exception{
		ProcessoDt processoDt = this.consultarProcessoId(id_processo);
		return this.consultarMovimentacoesProcesso(usuarioDt, processoDt, false, inNivelAcessoUsuario);
	}

	/**
	 * Consulta as movimentações de um determinado processo Cria conexão internamente
	 * 
	 * @param id_Processo
	 *            : identificação do processo
	 * 
	 * @author jrcorrea
	 */
	public List consultarMovimentacoesProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, boolean acessoOutraServentiaOuCodigoDeAcesso,int inNivelAcessoUsuario )throws Exception{
		List tempList = null;
		boolean possuiAutorizacaoParaBaixarVisualizarVideo = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());			
			
			possuiAutorizacaoParaBaixarVisualizarVideo =  new ProcessoNe().podeAcessarProcesso(usuarioDt,processoDt, obFabricaConexao);
			//possuiAutorizacaoParaBaixarVisualizarVideo = podeVisualizarBaixarVideo(usuarioDt, processoDt, acessoOutraServentiaOuCodigoDeAcesso);
			
			tempList = obPersistencia.consultarMovimentacoesProcesso(processoDt.getId(), possuiAutorizacaoParaBaixarVisualizarVideo, inNivelAcessoUsuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta as movimentações de um determinado processo. Cria conexão internamente
	 * 
	 * @param id_Processo
	 *            : identificação do processo
	 * 
	 * @author fogomes
	 */
	public List<MovimentacaoDt> consultarMovimentacoesProcesso(String idProcesso)throws Exception{
		
		List<MovimentacaoDt> tempList = null;
		boolean possuiAutorizacaoParaBaixarVisualizarVideo = false;
		int inNivelAcessoUsuario = 0;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarMovimentacoesProcesso(idProcesso, possuiAutorizacaoParaBaixarVisualizarVideo, inNivelAcessoUsuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return tempList;
	}

	/**
	 * Consulta as movimentações de um determinado processo
	 * 
	 * @param id_Processo
	 *            : identificação do processo
	 * @param conexao
	 *            , utiliza conexão existente para efetuar consulta
	 * 
	 * @author msapaula
	 */
	public List consultarMovimentacoesProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, boolean acessoOutraServentiaOuCodigoDeAcesso, FabricaConexao obFabricaConexao, int nivelAcesso) throws Exception {
		List tempList = null;
		boolean possuiAutorizacaoParaBaixarVisualizarVideo = false;
		
		MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());	
		possuiAutorizacaoParaBaixarVisualizarVideo =  new ProcessoNe().podeAcessarProcesso(usuarioDt,processoDt, obFabricaConexao);
		//possuiAutorizacaoParaBaixarVisualizarVideo = podeVisualizarBaixarVideo(usuarioDt, processoDt, acessoOutraServentiaOuCodigoDeAcesso);			
		tempList = obPersistencia.consultarMovimentacoesProcesso(processoDt.getId(), possuiAutorizacaoParaBaixarVisualizarVideo, nivelAcesso);
		
		return tempList;
	}
	
	/**
	 * Consulta as movimentações de sentenca de processo físico
	 * 
	 * @param id_Processo
	 *            : identificação do processo
	 * @param conexao
	 *            , utiliza conexão existente para efetuar consulta
	 * 
	 * @author acbloureiro
	 */
	public List consultarMovimentacoesProcessoFisico(UsuarioDt usuarioDt, ProcessoDt processoDt, boolean acessoOutraServentiaOuCodigoDeAcesso, FabricaConexao obFabricaConexao, int nivelAcesso) throws Exception {
		List tempList = null;
		
		MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());	
					
		tempList = obPersistencia.consultarMovimentacoesProcessoFisico(processoDt.getId(), nivelAcesso);
		
		return tempList;
	}

	/**
	 * Método que verifica se processo possui movimentação do tipo NÃO CONHECIDO
	 * 
	 * @param id_Processo
	 *            , identificador do processo
	 * @author lsbernardes
	 */
	public boolean possuiMovimentacaoNaoConhecido(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		
		MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.possuiMovimentacaoNaoConhecido(id_Processo);
		
		return retorno;
	}
	
	/**
	 * Consulta as movimentações de um determinado processo
	 * 
	 * @param id_Processo
	 *            : identificação do processo, descricao: descrição da movimentação do processo
	 * 
	 * @author kbsriccioppo
	 */
	public List consultarMovimentacoesProcesso(UsuarioDt usuarioDt, String id_processo, String descricao, String posicaoPaginaAtual, int inNivelAcessoUsuario)throws Exception{
		ProcessoDt processoDt = this.consultarProcessoId(id_processo);
		return this.consultarMovimentacoesProcesso(usuarioDt, processoDt, false, inNivelAcessoUsuario);
	}

	/**
	 * Consulta as movimentações de um determinado processo
	 * 
	 * @param id_Processo
	 *            : identificação do processo, descricao: descrição da movimentação do processo
	 * 
	 * @author kbsriccioppo
	 */
	public List consultarMovimentacoesProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, boolean acessoOutraServentiaOuCodigoDeAcesso, String descricao, String posicaoPaginaAtual)throws Exception{
		List tempList = null;
		boolean possuiAutorizacaoParaBaixarVisualizarVideo = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			
			possuiAutorizacaoParaBaixarVisualizarVideo =  new ProcessoNe().podeAcessarProcesso(usuarioDt,processoDt, obFabricaConexao);
			//possuiAutorizacaoParaBaixarVisualizarVideo = podeVisualizarBaixarVideo(usuarioDt, processoDt, acessoOutraServentiaOuCodigoDeAcesso);
			
			tempList = obPersistencia.consultarMovimentacoesProcesso(processoDt.getId(), possuiAutorizacaoParaBaixarVisualizarVideo, descricao, posicaoPaginaAtual);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Salva movimentação genérica de um processo, fazendo chamadas para atualizar classificador do processo, salvar os arquivos inseridos e gerar as pendências solicitadas
	 * 
	 * @param movimentacaoDt
	 *            : objeto com dados da movimentação a serem persistidas
	 * @param usuarioDt
	 *            : usuário que está realizando a movimentação
	 * 
	 * @author msapaula
	 */
	public void salvarMovimentacaoGenerica(MovimentacaoProcessoDt movimentacaoDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		List arquivos = null;
		List arquivosExistentes = null;
		List arquivosDeprecado = null;
		List pendenciasGerar = null;
		List movimentacoes = new ArrayList();
		ProcessoNe processoNe = new ProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			LogDt logDt = new LogDt(usuarioDt.getId(), movimentacaoDt.getIpComputadorLog());

			List processos = movimentacaoDt.getListaProcessos();
			arquivos = movimentacaoDt.getListaArquivos();
			pendenciasGerar = movimentacaoDt.getListaPendenciasGerar();

			if (movimentacaoDt.getDevolucaoPrecatoria().length() > 0 && (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverProcessoPrecatoria") || movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverDocumentoPrecatoria") || movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverDocumentoOficio"))) {

				if (arquivos != null && arquivos.size() > 0) {
					arquivosExistentes = new ArrayList();
					arquivosDeprecado = new ArrayList();
					//pegar arquivos deprecante
					for (Iterator iterator = arquivos.iterator(); iterator.hasNext();) {
						ArquivoDt arquivo = (ArquivoDt) iterator.next();
						if (arquivo.getId() != null && arquivo.getId().length() > 0) arquivosExistentes.add(arquivo);
					}
					movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
					//pegar arquivos deprecado
					for (Iterator iterator = arquivos.iterator(); iterator.hasNext();) {
						ArquivoDt arquivo = (ArquivoDt) iterator.next();
						boolean aux = true;
						for (Iterator iterator2 = arquivosExistentes.iterator(); iterator2.hasNext();) {
							ArquivoDt arquivo2 = (ArquivoDt) iterator2.next();
							if (arquivo.getId().equals(arquivo2.getId())) {
								aux = false;
								continue;
							}
						}
						if (aux) arquivosDeprecado.add(arquivo);
					}
				}

				List movimentacoesDeprecante = new ArrayList();
				List movimentacoesDeprecado = new ArrayList();
				for (int i = 0; i < processos.size(); i++) {
					ProcessoDt processo = (ProcessoDt) processos.get(i);

					if (arquivosDeprecado != null && arquivosDeprecado.size() > 0) {
						//Salvando movimentação para cada processo deprecado
						MovimentacaoDt movimentacaoDeprecado = new MovimentacaoDt();
						movimentacaoDeprecado.setId_MovimentacaoTipo(movimentacaoDt.getId_MovimentacaoTipo());
						movimentacaoDeprecado.setMovimentacaoTipo(movimentacaoDt.getMovimentacaoTipo());
						movimentacaoDeprecado.setComplemento(movimentacaoDt.getComplemento());
						movimentacaoDeprecado.setId_Processo(processo.getId());
						movimentacaoDeprecado.setProcessoNumero(processo.getProcessoNumero());
						movimentacaoDeprecado.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
						movimentacaoDeprecado.setId_UsuarioLog(logDt.getId_Usuario());
						movimentacaoDeprecado.setIpComputadorLog(logDt.getIpComputador());

						this.salvar(movimentacaoDeprecado, obFabricaConexao);
						movimentacoesDeprecado.add(movimentacaoDeprecado);

						String visibilidade=null;
						if (processo.isSegredoJustica()){
							visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
						}
						// Salvando vínculo entre movimentação e arquivos inseridos deprecado
						movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivosDeprecado, movimentacaoDeprecado.getId(),visibilidade, logDt, obFabricaConexao);

						// Salvando pendências da movimentação do processo deprecado
						if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
							pendenciaNe.gerarPendencias(processo, pendenciasGerar, movimentacaoDeprecado, arquivos, usuarioDt, null, movimentacaoDt.getId_ClassificadorPendencia(), logDt, obFabricaConexao);
						}

//						// Gera recibo para arquivos de movimentações deprecado e deprecante
//						movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivosDeprecado, movimentacoesDeprecado, obFabricaConexao);

					} else {
						//mesmo se não tiver movimentação gera a pendência de arquivamento
						if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
							for (int j = 0; j < pendenciasGerar.size(); j++) {
								dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(j);
								if (Funcoes.StringToInt(dt.getCodPendenciaTipo()) != PendenciaTipoDt.ARQUIVAMENTO &&
									Funcoes.StringToInt(dt.getCodPendenciaTipo()) != PendenciaTipoDt.ATIVAR_PROCESSO_CALCULO) {
									throw new MensagemException("Para gerar o(s) tipo(s) de pendência(s) escolhida(s) é necessário gerar Movimentação no \"Passo 1\".");
								}
							}
							pendenciaNe.gerarPendencias(processo, pendenciasGerar, null, arquivos, usuarioDt, null, movimentacaoDt.getId_ClassificadorPendencia(), logDt, obFabricaConexao);
						}
					}

					// Salvando movimentação para cada processo deprecante
					MovimentacaoDt movimentacaoDeprecante = new MovimentacaoDt();

					if (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverProcessoPrecatoria")) {
						movimentacaoDeprecante.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.PRECATORIA_DEVOLVIDA));
						movimentacaoDeprecante.setComplemento(movimentacaoDt.getComplementoDevolucaoPrecatoria());
					} else if (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverDocumentoPrecatoria")) {
						movimentacaoDeprecante.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO));
						movimentacaoDeprecante.setComplemento(movimentacaoDt.getComplementoDevolucaoPrecatoria());
					} else if (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverDocumentoOficio")) {
						movimentacaoDeprecante.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO));
						movimentacaoDeprecante.setComplemento(movimentacaoDt.getComplementoDevolucaoPrecatoria());
					}

					movimentacaoDeprecante.setId_Processo(processo.getId_ProcessoPrincipal());
					movimentacaoDeprecante.setProcessoNumero(processo.getProcessoNumeroPrincipal());
					movimentacaoDeprecante.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
					movimentacaoDeprecante.setId_UsuarioLog(logDt.getId_Usuario());
					movimentacaoDeprecante.setIpComputadorLog(logDt.getIpComputador());

					this.salvar(movimentacaoDeprecante, obFabricaConexao);

					if (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverProcessoPrecatoria")) {
						//copiar movimentações de audiência publicada do processo deprecado para o deprecante*********************************
						this.copieTodasAudienciasPublicadasProcessoCartaPrecatoria(processo.getId(), processo.getId_ProcessoPrincipal(), obFabricaConexao);
						//*********************************************************************************************************************
					}

					movimentacoesDeprecante.add(movimentacaoDeprecante);
					//movimentacoesDeprecado.add(movimentacaoDeprecante);

					String visibilidade=null;
					if (processo.isSegredoJustica()){
						visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
					}
					// Salvando vínculo entre movimentação e arquivos inseridos deprecante
					movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDeprecante.getId(),visibilidade, logDt, obFabricaConexao);

					//Gerar pendência verificar devolução carta precatória
					String id_ServentiaProcessoDeprecante = processoNe.consultarIdServentiaProcesso(processo.getId_ProcessoPrincipal(), obFabricaConexao);

					if (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverProcessoPrecatoria")) {
						pendenciaNe.gerarPendenciaVerificarDevolucaoPrecatoria(movimentacaoDeprecante, usuarioDt, id_ServentiaProcessoDeprecante, logDt, obFabricaConexao);
					} else if (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverDocumentoPrecatoria")) {
						pendenciaNe.gerarPendenciaVerificarPrecatoria(movimentacaoDeprecante, usuarioDt, id_ServentiaProcessoDeprecante, logDt, obFabricaConexao);
					} else if (movimentacaoDt.getDevolucaoPrecatoria().equalsIgnoreCase("DevolverDocumentoOficio")) {
						pendenciaNe.gerarPendenciaVerificarOficioNotificacao(movimentacaoDeprecante, usuarioDt, id_ServentiaProcessoDeprecante, logDt, obFabricaConexao);
					}
				}

//				// Gera recibo para arquivos de movimentações deprecado e deprecante
//				movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivosExistentes, movimentacoesDeprecante, obFabricaConexao);

			} else {

				if (arquivos != null && arquivos.size() > 0) movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);

				for (int i = 0; i < processos.size(); i++) {
					ProcessoDt processo = (ProcessoDt) processos.get(i);

					// Salvando movimentação para cada processo
					MovimentacaoDt novaMovimentacao = new MovimentacaoDt();
					novaMovimentacao.setId_MovimentacaoTipo(movimentacaoDt.getId_MovimentacaoTipo());
					novaMovimentacao.setMovimentacaoTipo(movimentacaoDt.getMovimentacaoTipo());
					novaMovimentacao.setComplemento(movimentacaoDt.getComplemento());
					novaMovimentacao.setId_Processo(processo.getId());
					novaMovimentacao.setProcessoNumero(processo.getProcessoNumeroCompleto());
					novaMovimentacao.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
					novaMovimentacao.setId_UsuarioLog(logDt.getId_Usuario());
					novaMovimentacao.setIpComputadorLog(logDt.getIpComputador());
					if (movimentacaoDt.getAcessoOutraServentia().length() > 0 && Funcoes.StringToInt(movimentacaoDt.getAcessoOutraServentia()) == PendenciaTipoDt.CARTA_PRECATORIA) {
						novaMovimentacao.setComplemento("(Carta Precatória)");
					}
					this.salvar(novaMovimentacao, obFabricaConexao);
					movimentacoes.add(novaMovimentacao);

					//Se tratar de um recurso inominado consulta também os dados desse para gerar pendências posteriormente
					if (processo.getId_Recurso() != null && processo.getId_Recurso().length() > 0) {
						processo.setRecursoDt(this.consultarRecursoId(processo.getId_Recurso()));
					}

					// Atualiza Classificador processo
					if (movimentacaoDt.getId_Classificador().length() > 0) {
						processoNe.alterarClassificadorProcesso(processo.getId_Processo(), processo.getClassificador(), movimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);
					}
					
					String visibilidade=null;
					if (processo.isSegredoJustica()){
						visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
					}
					// Salvando vínculo entre movimentação e arquivos inseridos
					movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, novaMovimentacao.getId(),visibilidade, logDt, obFabricaConexao);

					// Salvando pendências da movimentação
					if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
						pendenciaNe.gerarPendencias(processo, pendenciasGerar, novaMovimentacao, arquivos, usuarioDt, null, movimentacaoDt.getId_ClassificadorPendencia(), logDt, obFabricaConexao);
					}
					
					//Se for serventia de plantão, enviará um email aos analistas 
					ServentiaDt serventiaDt = new ServentiaNe().consultarId(processo.getId_Serventia());
					if ( (serventiaDt.isPlantaoPrimeiroGrau() || serventiaDt.isPlantaoSegundoGrau())  
							&& usuarioDt.isMagistrado()){				
						enviarEmailAnalistas(serventiaDt, processo.getProcessoNumeroCompleto());
					}
				}

//				// Gera recibo para arquivos de movimentações
//				movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		} catch(MensagemException m) {
			obFabricaConexao.cancelarTransacao();
			if (movimentacaoDt!=null)
				cancelarMovimentacaoGenerica(movimentacaoDt);
			throw m;
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			if (movimentacaoDt!=null)
				cancelarMovimentacaoGenerica(movimentacaoDt);
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	
	/**
	 * Salva movimentação genérica de um processo, fazendo chamadas para atualizar classificador do processo, salvar os arquivos inseridos e gerar as pendências solicitadas
	 * 
	 * @param movimentacaoDt
	 *            : objeto com dados da movimentação a serem persistidas
	 * @param usuarioDt
	 *            : usuário que está realizando a movimentação
	 * @param fabrica
	 *            : fabrica de conexão para transação
	 * 
	 * @author jrcorrea 250/05/2018
	 */
	public void salvarMovimentacaoGenerica(ProcessoDt processo, MovimentacaoProcessoDt movimentacaoDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		List arquivos = null;
		List pendenciasGerar = null;
		List movimentacoes = new ArrayList();		

		try{

			LogDt logDt = new LogDt(usuarioDt.getId(), movimentacaoDt.getIpComputadorLog());
			
			arquivos = movimentacaoDt.getListaArquivos();
			pendenciasGerar = movimentacaoDt.getListaPendenciasGerar();

			// Salva arquivos inseridos
			if (arquivos != null && arquivos.size() > 0){
				movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
			}					

			// Salvando movimentação para cada processo
			MovimentacaoDt novaMovimentacao = new MovimentacaoDt();
			novaMovimentacao.setId_MovimentacaoTipo(movimentacaoDt.getId_MovimentacaoTipo());
			novaMovimentacao.setMovimentacaoTipoCodigo(movimentacaoDt.getMovimentacaoTipoCodigo());
			novaMovimentacao.setMovimentacaoTipo(movimentacaoDt.getMovimentacaoTipo());
			novaMovimentacao.setComplemento(movimentacaoDt.getComplemento());
			novaMovimentacao.setId_Processo(processo.getId());
			novaMovimentacao.setProcessoNumero(processo.getProcessoNumeroCompleto());
			novaMovimentacao.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
			novaMovimentacao.setId_UsuarioLog(logDt.getId_Usuario());
			novaMovimentacao.setIpComputadorLog(logDt.getIpComputador());
			if (movimentacaoDt.getAcessoOutraServentia().length() > 0 && Funcoes.StringToInt(movimentacaoDt.getAcessoOutraServentia()) == PendenciaTipoDt.CARTA_PRECATORIA) {
				novaMovimentacao.setComplemento("(Carta Precatória)");
			}
			this.salvar(novaMovimentacao, obFabricaConexao);
			if (Funcoes.StringToInt(movimentacaoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_RETORNAR_DA_CONCILIACAO_OU_MEDIACAO_CEJUSC) {
				movimentacaoDt.setId(novaMovimentacao.getId());
			}
			movimentacoes.add(novaMovimentacao);

			//Se tratar de um recurso inominado consulta também os dados desse para gerar pendências posteriormente
			if (processo.getId_Recurso() != null && processo.getId_Recurso().length() > 0) {
				processo.setRecursoDt(this.consultarRecursoId(processo.getId_Recurso()));
			}

			String visibilidade=null;
			if (processo.isSegredoJustica()){
				visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
			}
			// Salvando vínculo entre movimentação e arquivos inseridos
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, novaMovimentacao.getId(),visibilidade, logDt, obFabricaConexao);

			// Salvando pendências da movimentação
			if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
				PendenciaNe pendenciaNe = new PendenciaNe();
				pendenciaNe.gerarPendencias(processo, pendenciasGerar, novaMovimentacao, arquivos, usuarioDt, null, logDt, obFabricaConexao);
			}
		
//			// Gera recibo para arquivos de movimentações
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);

		} catch(MensagemException m) {
			cancelarMovimentacaoGenerica(movimentacaoDt);
			throw m;
		} catch(Exception e) {
			cancelarMovimentacaoGenerica(movimentacaoDt);
			throw e;
		}
	}

	
	/**
	 * Salva movimentação genérica de um processo, fazendo chamadas para atualizar classificador do processo, salvar os arquivos inseridos e gerar as pendências solicitadas
	 * 
	 * @param movimentacaoDt
	 *            : objeto com dados da movimentação a serem persistidas
	 * @param usuarioDt
	 *            : usuário que está realizando a movimentação
	 * @param fabrica
	 *            : fabrica de conexão para transação
	 * 
	 * @author lsbernardes
	 */
	public void salvarMovimentacaoGenerica(MovimentacaoProcessoDt movimentacaoDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		List arquivos = null;
		List pendenciasGerar = null;
		List movimentacoes = new ArrayList();		

		try{

			LogDt logDt = new LogDt(usuarioDt.getId(), movimentacaoDt.getIpComputadorLog());

			List processos = movimentacaoDt.getListaProcessos();
			arquivos = movimentacaoDt.getListaArquivos();
			pendenciasGerar = movimentacaoDt.getListaPendenciasGerar();

			// Salva arquivos inseridos
			if (arquivos != null && arquivos.size() > 0) movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);

			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processo = (ProcessoDt) processos.get(i);

				// Salvando movimentação para cada processo
				MovimentacaoDt novaMovimentacao = new MovimentacaoDt();
				novaMovimentacao.setId_MovimentacaoTipo(movimentacaoDt.getId_MovimentacaoTipo());
				novaMovimentacao.setMovimentacaoTipoCodigo(movimentacaoDt.getMovimentacaoTipoCodigo());
				novaMovimentacao.setMovimentacaoTipo(movimentacaoDt.getMovimentacaoTipo());
				novaMovimentacao.setComplemento(movimentacaoDt.getComplemento());
				novaMovimentacao.setId_Processo(processo.getId());
				novaMovimentacao.setProcessoNumero(processo.getProcessoNumeroCompleto());
				novaMovimentacao.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
				novaMovimentacao.setId_UsuarioLog(logDt.getId_Usuario());
				novaMovimentacao.setIpComputadorLog(logDt.getIpComputador());
				if (movimentacaoDt.getAcessoOutraServentia().length() > 0 && Funcoes.StringToInt(movimentacaoDt.getAcessoOutraServentia()) == PendenciaTipoDt.CARTA_PRECATORIA) {
					novaMovimentacao.setComplemento("(Carta Precatória)");
				}
				this.salvar(novaMovimentacao, obFabricaConexao);
				if (Funcoes.StringToInt(movimentacaoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_RETORNAR_DA_CONCILIACAO_OU_MEDIACAO_CEJUSC) {
					movimentacaoDt.setId(novaMovimentacao.getId());
				}
				movimentacoes.add(novaMovimentacao);

				//Se tratar de um recurso inominado consulta também os dados desse para gerar pendências posteriormente
				if (processo.getId_Recurso() != null && processo.getId_Recurso().length() > 0) {
					processo.setRecursoDt(this.consultarRecursoId(processo.getId_Recurso()));
				}

				String visibilidade=null;
				if (processo.isSegredoJustica()){
					visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
				}
				// Salvando vínculo entre movimentação e arquivos inseridos
				movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, novaMovimentacao.getId(),visibilidade, logDt, obFabricaConexao);

				// Salvando pendências da movimentação
				if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
					PendenciaNe pendenciaNe = new PendenciaNe();
					pendenciaNe.gerarPendencias(processo, pendenciasGerar, novaMovimentacao, arquivos, usuarioDt, null, logDt, obFabricaConexao);
				}
			}

//			// Gera recibo para arquivos de movimentações
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);

		} catch(MensagemException m) {
			cancelarMovimentacaoGenerica(movimentacaoDt);
			throw m;
		} catch(Exception e) {
			cancelarMovimentacaoGenerica(movimentacaoDt);
			throw e;
		}
	}

	
	/**
	 * Método responsável em efetuar o peticionamento num processo existente. Uma movimentação será gerada, os arquivos salvos, e uma pendência do tipo "Verificar Petição" é criada para a serventia do processo.
	 * 
	 * @param peticionamentoDt
	 *            : objeto com os dados a serem persistidos
	 * @param usuuarioDt
	 *            : usuário que está realizando o peticionamento
	 * @author msapaula
	 */
	public String salvarPeticionamento(PeticionamentoDt peticionamento, UsuarioDt usuarioDt)throws Exception{
		MovimentacaoArquivoNe arquivoNe = new MovimentacaoArquivoNe();
		String pedidoUrgencia = null;
		List arquivos = null;
		List movimentacoes = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		String id_Movimentacao = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			LogDt logDt = new LogDt(usuarioDt.getId(), peticionamento.getIpComputadorLog());

			List processos = peticionamento.getListaProcessos();
			arquivos = peticionamento.getListaArquivos();

			// Salva arquivos inseridos
			if (arquivos != null && arquivos.size() > 0) {
				arquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
			}

			// Para cada processo, salva movimentação, vincula com arquivo e gera pendência "Verificar Petição"
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processo = (ProcessoDt) processos.get(i);
				//Processos das serventias de execução da penal podem ser movimentados apenas para arquivamento. BO: 2019/13635 - 2019/14873
				//Se o processo for de uma classe de execução penal e for de assunto diferente de Acordo de não persecução penal
				if(processo.isExecucaoPenal() && !processo.isAcordoNaoPersecucaoPenal()){
					ServentiaDt serventiaDt = this.consultarServentiaProcesso(processo.getId_Serventia());
					//e for de uma serventia do subtipo execução penal
					if(serventiaDt != null && serventiaDt.isSubTipoExecucaoPenal()) {
						throw new MensagemException("Processos de execução penal não podem ser movimentados. Decreto judiciário n.º 2029/2019.");
					}
				}
				
				// Salvando movimentação
				MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId_MovimentacaoTipo(peticionamento.getId_MovimentacaoTipo());
				movimentacaoDt.setComplemento(peticionamento.getComplemento());
				movimentacaoDt.setId_Processo(processo.getId());
				movimentacaoDt.setProcessoNumero(processo.getProcessoNumero());
				movimentacaoDt.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
				if(peticionamento.isSegredoJustica()){
					movimentacaoDt.setCodigoTemp(String.valueOf(MovimentacaoArquivoDt.ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO));
				}
				movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
				movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());
				this.salvar(movimentacaoDt, obFabricaConexao);
				movimentacoes.add(movimentacaoDt);
				id_Movimentacao = movimentacaoDt.getId();
				
				if (processos.size() == 1)
					peticionamento.setId(movimentacaoDt.getId());

				// Salvando vínculo entre movimentação e arquivos inseridos
				if (arquivos != null) {
					MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
					if(peticionamento.isSegredoJustica()){
						movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), String.valueOf(MovimentacaoArquivoDt.ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO), logDt, obFabricaConexao);
					} else {
						movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL), logDt, obFabricaConexao);
					}
				}

				// Gera pendência do Tipo Verificar Petição
				PendenciaNe pendenciaNe = new PendenciaNe();
				if (peticionamento.isPedidoUrgencia()) pedidoUrgencia = String.valueOf(ProcessoPrioridadeDt.LIMINAR);
				
				//Se o processo não for sigiloso, gerar o verificar
				if (!processo.isSigiloso()) {
    				//Se for advogado, gerar "Verificar Petição" para cartório
    				if ( usuarioDt.isAdvogado()) {
    					pendenciaNe.gerarPendenciaVerificarPeticao(processo, UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), processo.getId_Serventia(), pedidoUrgencia, arquivos, logDt, obFabricaConexao);
    				} else if (usuarioDt.isMp()) {
    					//Para promotor, gera "Verificar Parecer" para cartório
    					pendenciaNe.gerarPendenciaVerificarParecer(processo, UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), processo.getId_Serventia(), pedidoUrgencia, arquivos, logDt, obFabricaConexao);
    				} else if (usuarioDt.isAutoridadePolicial()) {
    					//Para autoridade policial, gera "Verificar Documento" para cartório
    					pendenciaNe.gerarPendenciaVerificarDocumento(processo.getId(), UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), processo.getId_Serventia(), pedidoUrgencia, arquivos, logDt, obFabricaConexao);
    				}
				}
				
				//Se for serventia de plantão, enviará um email aos analistas 
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(processo.getId_Serventia());
				if (serventiaDt.isPlantaoPrimeiroGrau() || serventiaDt.isPlantaoSegundoGrau()){				
					enviarEmailPeticionamentoAnalistas(serventiaDt ,processo.getProcessoNumeroCompleto());
				}
				
				//Se o protocolo for sigiloso e o protocolo for realizado em uma UPJ, deve ser criado a pendência de verificação
				//correspondente na tela inicial do magistrado responsável pelo processo
				if(processo.isSigiloso() && serventiaDt.isUPJs()) {
					ServentiaCargoDt juizUPJResponsavel = new ProcessoResponsavelNe().consultarJuizUPJ(processo.getId(), obFabricaConexao);
					if (usuarioDt.isMp()) {
    					//Para promotor, gera "Verificar Parecer" para o magistrado
    					pendenciaNe.gerarPendenciaVerificarParecerProcessoSigiloso(processo, UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), juizUPJResponsavel.getId(), pedidoUrgencia, arquivos, logDt, obFabricaConexao);
    				} else if (usuarioDt.isAutoridadePolicial()) {
    					//Para autoridade policial, gera "Verificar Documento" para o magistrado
    					pendenciaNe.gerarPendenciaVerificarDocumentoProcessoSigiloso(processo.getId(), UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), juizUPJResponsavel.getId(), pedidoUrgencia, arquivos, logDt, obFabricaConexao);
    				}
				}
				
				//Se usuário que está peticionando é um promotor e o processo não possui um promotor responsável, adiciona ele como responsável pelo processo
				ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
				ServentiaCargoDt promotorResponsavel = responsavelNe.getPromotorResponsavelProcesso(processo.getId(), usuarioDt.getId_Serventia(), obFabricaConexao);
				int GrupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
				if ((GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo ==GrupoDt.MP_TCE) && promotorResponsavel == null) {
					new ProcessoResponsavelNe().salvarProcessoResponsavel(processo.getId(), usuarioDt.getId_ServentiaCargo(), true, CargoTipoDt.MINISTERIO_PUBLICO, logDt, obFabricaConexao);
				}
			}
//			// Gera recibo para arquivos de movimentações
//			arquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return id_Movimentacao;
	}

	/**
	 * Salva análise de autos conclusos, fazendo chamadas para gerar movimentação, atualizar classificador do processo, salvar os arquivos inseridos e gerar as pendências solicitadas. Suporta análise múltipla.
	 * 
	 * @author msapaula
	 */
	public void salvarAnaliseConclusao(AnaliseConclusaoDt analisePendenciaDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
		ProcessoDt processoDt = null;
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		List arquivos = null;
		List movimentacoes = new ArrayList(); // Lista é necessária para gerar recibo
		FabricaConexao obFabricaConexao = null;

		//super.SalvarLogNoConsole(usuarioDt, "Início.");
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			LogDt logDt = new LogDt(usuarioDt.getId(), analisePendenciaDt.getIpComputadorLog());

			List pendenciasFechar = analisePendenciaDt.getListaPendenciasFechar(); // Resgata pendências que estão sendo analisadas
			arquivos = analisePendenciaDt.getListaArquivos(); // Resgata arquivos inseridos
			
			//super.SalvarLogNoConsole(usuarioDt, "Inserindo arquivos.");
			if (ValidacaoUtil.isNaoVazio(arquivos)) {
				movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
			} else {
				throw new MensagemException("Não foi possível finalizar a conclusão, pois a lista de arquivos está vazia");
			}
			//super.SalvarLogNoConsole(usuarioDt, "Arquivos inseridos.");

			// Trata cada pendência a ser fechada
			if (pendenciasFechar!=null){
				//super.SalvarLogNoConsole(usuarioDt, "Fechando " + pendenciasFechar.size() + " pendências.");
				for (int i = 0; i < pendenciasFechar.size(); i++) {					
					PendenciaDt objPendencia = (PendenciaDt) pendenciasFechar.get(i);
					// Recupera dados do processo, pois será necessário para gerar pendências
					processoDt = objPendencia.getProcessoDt();
					if (processoDt == null) {
						//super.SalvarLogNoConsole(usuarioDt, "Consultando processo início.");
						processoDt = new ProcessoNe().consultarIdCompleto(objPendencia.getId_Processo());
						//super.SalvarLogNoConsole(usuarioDt, "Consultando processo fim.");
					}
	
					//Se tratar de um recurso inominado consulta também os dados desse para gerar pendências posteriormente
					if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
						//super.SalvarLogNoConsole(usuarioDt, "Consultando recurso início.");
						processoDt.setRecursoDt(this.consultarRecursoId(processoDt.getId_Recurso()));
						//super.SalvarLogNoConsole(usuarioDt, "Consultando recurso fim.");
					}
	
					// Salvando movimentação do processo
					MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
					movimentacaoDt.setId_Processo(processoDt.getId());
					movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
					movimentacaoDt.setId_MovimentacaoTipo(analisePendenciaDt.getId_MovimentacaoTipo());
					movimentacaoDt.setMovimentacaoTipo(analisePendenciaDt.getMovimentacaoTipo());
					movimentacaoDt.setComplemento(analisePendenciaDt.getComplementoMovimentacao());
					movimentacaoDt.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
					movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
					movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());
					//super.SalvarLogNoConsole(usuarioDt, "Salvando movimentação início.");
					this.salvar(movimentacaoDt, obFabricaConexao);
					//super.SalvarLogNoConsole(usuarioDt, "Salvando movimentação fim.");
					movimentacoes.add(movimentacaoDt);
	
					//super.SalvarLogNoConsole(usuarioDt, "Salvando tipo de movimentação início.");
					//verifica se a movimentação é não conhecida e se é um desembargador que está realizando, caso positivo retira o redator
					String codMovTipo = new MovimentacaoTipoNe().consultarCodigoMovimentacaoTipo(analisePendenciaDt.getId_MovimentacaoTipo(), obFabricaConexao);
					//super.SalvarLogNoConsole(usuarioDt, "Salvando tipo de movimentação fim.");
					if (codMovTipo != null && codMovTipo.length() > 0 && Funcoes.StringToInt(codMovTipo) == MovimentacaoTipoDt.NAO_CONHECIDO && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.DESEMBARGADOR) {
						//super.SalvarLogNoConsole(usuarioDt, "Desativando o redator início.");
						new ProcessoResponsavelNe().desativarRedatorProcesso(processoDt.getId(), obFabricaConexao);
						//super.SalvarLogNoConsole(usuarioDt, "Desativando o redator fim.");
					}
	
					// Atualiza Classificador processo
					if (analisePendenciaDt.getId_Classificador().length() > 0) {
						//super.SalvarLogNoConsole(usuarioDt, "Alterando o classificador início.");
						ProcessoNe processoNe = new ProcessoNe();
						processoNe.alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), analisePendenciaDt.getId_Classificador(), logDt, obFabricaConexao);	
						//super.SalvarLogNoConsole(usuarioDt, "Alterando o classificador fim.");
					}
	
					if (arquivos != null && arquivos.size() > 0) {
						//super.SalvarLogNoConsole(usuarioDt, "Vinculando pendências e arquivos início.");
						// Salvando vínculo entre a pendência e arquivos (resolução da pendência)
						PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
						pendenciaArquivoNe.vincularArquivos(objPendencia, arquivos, true, obFabricaConexao);
						//super.SalvarLogNoConsole(usuarioDt, "Vinculando pendências e arquivos fim.");
	
						//super.SalvarLogNoConsole(usuarioDt, "Vinculando movimentação e arquivos início.");
						// Salvando vínculo entre a movimentação gerada e arquivos inseridos
						//Conforme Definição Qualquer ato do Juiz, salvo processo em segredo de justiça, é público (Jesus)
						if (processoDt.getSegredoJustica() != null && processoDt.getSegredoJustica().equals("false")) {
							movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), String.valueOf(MovimentacaoArquivoDt.ACESSO_PUBLICO), logDt, obFabricaConexao);
						} else {
							movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL), logDt, obFabricaConexao);							
						}
						//super.SalvarLogNoConsole(usuarioDt, "Vinculando movimentação e arquivos fim.");
					}
					
					if (analisePendenciaDt.getPedidoAssistencia() != null && analisePendenciaDt.getPedidoAssistencia().length() > 0){
						GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
						GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
						guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoInicialAguardandoDeferimento(processoDt.getId_Processo(), obFabricaConexao);
						
						if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length() > 0){
							if (analisePendenciaDt.getPedidoAssistencia().equalsIgnoreCase("1")){
								guiaEmissaoNe.atualizarStatusGuiaInicialAguardandoDeferimento(guiaEmissaoDt.getId(), String.valueOf(GuiaStatusDt.BAIXADA_COM_ASSISTENCIA), obFabricaConexao);
						
							} else if (analisePendenciaDt.getPedidoAssistencia().equalsIgnoreCase("0")){
								guiaEmissaoNe.atualizarStatusGuiaInicialAguardandoDeferimento(guiaEmissaoDt.getId(), String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO), obFabricaConexao);
								
								ProcessoNe processoNe = new ProcessoNe();
								processoNe.alterarCustaTipoProcesso(processoDt.getId_Processo(), processoDt.getId_Custa_Tipo(), String.valueOf(ProcessoDt.COM_CUSTAS), logDt, obFabricaConexao);
							
								if (!guiaEmissaoNe.consultarGuiaPagaBancos(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao)) {
									pendenciaNe.gerarPendenciaVerificarGuiaPendente(processoDt, UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoDt.getId(), arquivos, logDt, obFabricaConexao);
								}
							}
						}
					}
	
					// Salva pendências que foram marcadas para serem geradas.
					// Se juiz não marcou nenhuma pendência, gera uma pendência do tipo "Verificar Processo" para serventia.
					if (analisePendenciaDt.getListaPendenciasGerar() != null && analisePendenciaDt.getListaPendenciasGerar().size() > 0) {
						//super.SalvarLogNoConsole(usuarioDt, "Gerando pendências início.");
						pendenciaNe.gerarPendencias(processoDt, analisePendenciaDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, objPendencia, logDt, obFabricaConexao);
						//super.SalvarLogNoConsole(usuarioDt, "Gerando pendências fim.");
					} else if (!processoDt.isSigiloso() && !analisePendenciaDt.isNaoGerarVeficarProcesso()) {
						//super.SalvarLogNoConsole(usuarioDt, "Gerando pendência de verificar processo início.");
						switch (Integer.valueOf(processoDt.getProcessoTipoCodigo())) {
							case ProcessoTipoDt.HABEAS_CORPUS:
							case ProcessoTipoDt.HABEAS_CORPUS_CF_LIVRO_III:
							case ProcessoTipoDt.HABEAS_CORPUS_CPP:
								pendenciaNe.gerarPendenciaVerificarProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoDt.getId(), arquivos, objPendencia, logDt, obFabricaConexao, new ProcessoPrioridadeNe().consultarProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.LIMINAR)).getId());
								break;
							default:
								pendenciaNe.gerarPendenciaVerificarProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoDt.getId(), arquivos, objPendencia, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());
								break;
						}						
						//super.SalvarLogNoConsole(usuarioDt, "Gerando pendência de verificar processo fim.");
					}
					
					// Fecha pendência com status Cumprida
					objPendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
					objPendencia.setDataFim(Funcoes.DataHora(new Date()));
					//super.SalvarLogNoConsole(usuarioDt, "Fechando pendência início.");
					objPendencia.setId_UsuarioLog(logDt.getId_Usuario());
					objPendencia.setIpComputadorLog(logDt.getIpComputador());
					pendenciaNe.fecharPendencia(objPendencia, usuarioDt, obFabricaConexao);
					//super.SalvarLogNoConsole(usuarioDt, "Fechando pendência fim.");
				}
				//super.SalvarLogNoConsole(usuarioDt, "Pendências finalizadas.");
			}

			//super.SalvarLogNoConsole(usuarioDt, "Gerando recibos início.");
//			// Gera recibo para arquivos de movimentações
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
			//super.SalvarLogNoConsole(usuarioDt, "Gerando recibos fim.");

			// Cria publicação dos arquivos da Conclusão
			// Decreto 1.684 / 2020, Art 2o e 3o
			if (!processoDt.isSigiloso()) {
				//super.SalvarLogNoConsole(usuarioDt, "Salvando publicação início.");
				pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
				//super.SalvarLogNoConsole(usuarioDt, "Salvando publicação fim.");
			}
			
			//Se for serventia de plantão, enviará um email aos analistas 
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());
			if (serventiaDt.isPlantaoPrimeiroGrau() || serventiaDt.isPlantaoSegundoGrau()){				
				enviarEmailAnalistas(serventiaDt,processoDt.getProcessoNumeroCompleto());
			}

			if (analisePendenciaDt.getJulgadoMeritoProcessoPrincipal() != null && analisePendenciaDt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")) {
				//super.SalvarLogNoConsole(usuarioDt, "Registrando mérito processo principal início.");
				new ProcessoNe().registrarJulgamentoMeritoProcessoPrincipal(processoDt.getId(), true, obFabricaConexao);
				//super.SalvarLogNoConsole(usuarioDt, "Registrando mérito processo principal fim.");
			}

			obFabricaConexao.finalizarTransacao();
			
			//super.SalvarLogNoConsole(usuarioDt, "Finalização com sucesso.");
		} catch(MensagemException m) {
			obFabricaConexao.cancelarTransacao();
			cancelarAnaliseConclusao(analisePendenciaDt);
			//super.SalvarLogNoConsole(usuarioDt, "Finalização com inconsistência " + m.getMessage() + ".");
			throw m;
		} catch (SQLIntegrityConstraintViolationException e) {
			obFabricaConexao.cancelarTransacao();
			cancelarAnaliseConclusao(analisePendenciaDt);
			if (processoDt != null) 
				throw new MensagemException("A pré-analise do processo " + processoDt.getProcessoNumero() + " está inconsistente, favor refazê-la.");
			//super.SalvarLogNoConsole(usuarioDt, "Finalização com erro: " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			throw e;
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarAnaliseConclusao(analisePendenciaDt);
			//super.SalvarLogNoConsole(usuarioDt, "Finalização com erro: " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável por enviar um email de alerta aos analistas da serventia informando que um processo foi movimentado pelo magistrado.
	 * @param serventia - serventia do processo
	 * @param numeroProcesso - número do processo
	 * @author hmgodinho
	 */
	private void enviarEmailAnalistas(ServentiaDt serventia, String numeroProcesso) {
		String mensagemEmail = "<table width='98%' border='0' cellspacing='2' cellpadding='2'>";
		mensagemEmail += "  <tr><td colspan='2'>Um processo foi movimentado pelo magistrado.</td> </tr>";
		mensagemEmail += "  <tr> <td width='80'>Número:</td> <td><b> "+ numeroProcesso + "</b> </td> </tr> ";
		mensagemEmail += "  <tr> <td>Serventia: </td> <td> <b> "+ serventia.getServentia() + "</b></td> </tr>";
		mensagemEmail += "  <tr> <td>Data:</td> <td><b> "+ Funcoes.FormatarDataHora(new Date()) + "</b></td> </tr>";
		mensagemEmail += "</table>";
		mensagemEmail += "<br />Por favor acesse o Sistema de Processo Eletrônico e verifique o processo. <br/>";   
		
		new GerenciadorEmail(serventia.getServentia(), serventia.getEmail(), "Processo movimentado pelo magistrado", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
	}
	
	/**
	 * Método responsável por enviar um email de alerta aos analistas da serventia informando que um processo foi movimentado pelo magistrado.
	 * @param serventia - serventia do processo
	 * @param numeroProcesso - número do processo
	 * @author hmgodinho
	 */
	private void enviarEmailPeticionamentoAnalistas(ServentiaDt serventia, String numeroProcesso) {
		String mensagemEmail = "<table width='98%' border='0' cellspacing='2' cellpadding='2'>";
		mensagemEmail += "  <tr><td colspan='2'>Um processo foi peticionado.</td> </tr>";
		mensagemEmail += "  <tr> <td width='80'>Número:</td> <td><b> "+ numeroProcesso + "</b> </td> </tr> ";
		mensagemEmail += "  <tr> <td>Serventia: </td> <td> <b> "+ serventia.getServentia() + "</b></td> </tr>";
		mensagemEmail += "  <tr> <td>Data:</td> <td><b> "+ Funcoes.FormatarDataHora(new Date()) + "</b></td> </tr>";
		mensagemEmail += "</table>";
		mensagemEmail += "<br />Por favor acesse o Sistema de Processo Eletrônico e verifique o processo. <br/>";   
				
		new GerenciadorEmail(serventia.getServentia(), serventia.getEmail(), "Processo peticionado", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
	}
	
	/**
	 * Método responsável em cancelar a análise de autos conslusos. Apaga os id's que tenham sido setados para os objetos
	 */
	private void cancelarAnaliseConclusao(AnalisePendenciaDt analisePendenciaDt){
		List arquivos = analisePendenciaDt.getListaArquivos();
		if (arquivos != null) {
			ElasticSearchNe elasticSearchNe = new ElasticSearchNe();
			for (int i = 0; i < arquivos.size(); i++) {
				ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
				elasticSearchNe.excluirArquivoPublicado(arquivoDt.getId());
				arquivoDt.setId("");
			}
			elasticSearchNe = null;
		}
	}

	/**
	 * Salva a geração de pendências em uma movimentação já existente.
	 * 
	 * @param movimentacaoDt
	 *            , objeto com dados das pendências a serem persistidas
	 * @param usuarioDt
	 *            , usuário que está realizando a movimentação
	 * 
	 * @author msapaula
	 */
	public void salvarPendenciaMovimentacao(MovimentacaoProcessoDt movimentacaoDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LogDt logDt = new LogDt(usuarioDt.getId(), movimentacaoDt.getIpComputadorLog());
			ProcessoNe processoNe = new ProcessoNe();
			List pendenciasGerar = movimentacaoDt.getListaPendenciasGerar();
			ProcessoDt processoDt = movimentacaoDt.getPrimeiroProcessoLista();
			//Captura arquivos da movimentação para vincular às pendências que serão geradas
			List arquivos = new ArquivoNe().consultarArquivosMovimentacao( movimentacaoDt.getId());

			obFabricaConexao.iniciarTransacao();
			
			// Salvando pendências da movimentação
			if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
				PendenciaNe pendenciaNe = new PendenciaNe();
				pendenciaNe.gerarPendencias(processoDt, pendenciasGerar, movimentacaoDt, arquivos, usuarioDt, null, movimentacaoDt.getId_ClassificadorPendencia(), logDt, obFabricaConexao);
			}
			
			//limpando o Classificador do processo caso seja uma das pendências abaixo
			for(int j = 0; j < pendenciasGerar.size(); j ++) {
				dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(j);
				if(Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.ARQUIVAMENTO
						|| Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.RETORNAR_SERVENTIA_ORIGEM
						|| Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.ENVIAR_INSTANCIA_SUPERIOR
						|| Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.ATIVAR_PROCESSO_CALCULO) {
					processoNe.limparClassificadorProcesso(processoDt, obFabricaConexao);
					movimentacaoDt.setId_Classificador("null");
				}
			}

			// Atualiza Classificador processo
			if (movimentacaoDt.getId_Classificador().length() > 0) {
				new ProcessoNe().alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), movimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();
		} catch(MensagemException m) {
			obFabricaConexao.cancelarTransacao();
			throw m;
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Gera uma movimentacao
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/12/2008 16:23
	 * @param MovimentacaoDt
	 *            movimentacaoDt, vo de movimentacao
	 * @param List
	 *            arquivosVincular, lista de arquivos a vincular
	 * @param LogDt
	 *            logDt, vo de log
	 * @param FabricaConexxao
	 *            fabConexao, fabrica de conexao
	 * @throws Exception
	 */
	public boolean gerarMovimentacao(MovimentacaoDt movimentacaoDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {
		movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
		movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());

		return this.gerarMovimentacao(movimentacaoDt, arquivosVincular, fabConexao);
	}

	/**
	 * Gravar log
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 15/12/2008 11:49
	 * @param dados
	 *            pacote de dados
	 * @param conexao
	 *            conexao
	 * @return true, se consegui gravar, false se nao consegui gravar
	 * @throws Exception
	 */
	private boolean gravarLog(MovimentacaoDt dados, FabricaConexao conexao) throws Exception{
		LogDt obLogDt;
		
		if (dados.getId().trim().equalsIgnoreCase("") || dados.getId() == null) {
			obLogDt = new LogDt("Movimentacao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obLogDt = new LogDt("Movimentacao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		this.obLog.salvar(obLogDt, conexao);
		
		return true;
	}

	/**
	 * Valida uma movimentação passada. Seta CodigoTemp como 0 - válida
	 * 
	 * @param movimentacaoDt
	 *            , dt com dados da movimentação
	 * @author msapaula
	 */
	public void validarMovimentacao(MovimentacaoDt movimentacaoDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_Movimentacao:" + movimentacaoDt.getId() + ";CodigoTemp:" +movimentacaoDt.getCodigoTemp() + "]";
			String valorNovo = "[Id_Movimentacao:" + movimentacaoDt.getId() + ";CodigoTemp:" + MovimentacaoArquivoDt.ACESSO_NORMAL + "]";

			obLogDt = new LogDt("Movimentacao", movimentacaoDt.getId(), movimentacaoDt.getId_UsuarioLog(), movimentacaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.AlteracaoVisibilidadeMovimentacao), valorAtual, valorNovo);
			obPersistencia.alterarStatusMovimentacao(movimentacaoDt.getId(), MovimentacaoArquivoDt.ACESSO_NORMAL);

			// Consulta arquivos da movimentação para também validá-los
			MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
			movimentacaoArquivoNe.validarArquivosMovimentacao(movimentacaoDt, obFabricaConexao);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Invalida uma movimentação passada. Seta CodigoTemp com valor de 1 - inválida
	 * 
	 * @param movimentacaoDt
	 *            , dt com dados da movimentação
	 * @author msapaula
	 */
	public void invalidarMovimentacao(MovimentacaoDt movimentacaoDt, int constanteAcesso) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_Movimentacao:" + movimentacaoDt.getId() + ";CodigoTemp:" +movimentacaoDt.getCodigoTemp() + "]";
			String valorNovo = "[Id_Movimentacao:" + movimentacaoDt.getId() + ";CodigoTemp:" + constanteAcesso + "]";

			obLogDt = new LogDt("Movimentacao", movimentacaoDt.getId(), movimentacaoDt.getId_UsuarioLog(), movimentacaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.AlteracaoVisibilidadeMovimentacao), valorAtual, valorNovo);
			obPersistencia.alterarStatusMovimentacao(movimentacaoDt.getId(), constanteAcesso);

			// Consulta arquivos da movimentação para também invalidá-los
			MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
			movimentacaoArquivoNe.invalidarArquivosMovimentacao(movimentacaoDt, constanteAcesso, obFabricaConexao);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Invalida uma movimentação passada. Seta CodigoTemp com valor de 1 - inválida
	 * 
	 * @param movimentacaoDt
	 *            , dt com dados da movimentação
	 */
	public void invalidarMovimentacao(MovimentacaoDt movimentacaoDt, FabricaConexao obFabricaConexao, LogDt obLogDt) throws Exception {
		
		obFabricaConexao.iniciarTransacao();
		MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());

		String valorAtual = "[Id_Movimentacao:" + movimentacaoDt.getId() + ";CodigoTemp:" + MovimentacaoDt.VALIDA + "]";
		String valorNovo = "[Id_Movimentacao:" + movimentacaoDt.getId() + ";CodigoTemp:" + MovimentacaoDt.INVALIDA + "]";

		obLogDt = new LogDt("Movimentacao", movimentacaoDt.getId(), movimentacaoDt.getId_UsuarioLog(), movimentacaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Invalidacao), valorAtual, valorNovo);
		obPersistencia.alterarStatusMovimentacao(movimentacaoDt.getId(), MovimentacaoDt.INVALIDA);

		// Consulta arquivos da movimentação para também invalidá-los
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		movimentacaoArquivoNe.invalidarArquivosMovimentacao(movimentacaoDt, MovimentacaoArquivoDt.ACESSO_BLOQUEADO, obFabricaConexao);

		obLog.salvar(obLogDt, obFabricaConexao);
		obFabricaConexao.finalizarTransacao();
			
	}

	/**
	 * Gera uma movimentacao
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/12/2008 16:23
	 * @param MovimentacaoDt
	 *            movimentacaoDt, vo de movimentacao
	 * @param List
	 *            arquivosVincular, lista de arquivos a vincular
	 * @param FabricaConexxao
	 *            fabConexao, fabrica de conexao
	 * @throws Exception
	 */
	public boolean gerarMovimentacao(MovimentacaoDt movimentacaoDt, List arquivosVincular, FabricaConexao obFabricaConexao) throws Exception {

		if (movimentacaoDt != null) {
			// Verifica se a conexao sera criada internamente

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());

			obPersistencia.inserir(movimentacaoDt);
			this.gravarLog(movimentacaoDt, obFabricaConexao);

			LogDt logDt = new LogDt();
			logDt.setId_Usuario(movimentacaoDt.getId_UsuarioLog());
			logDt.setIpComputador(movimentacaoDt.getIpComputadorLog());

			// Se possui objetos na lista de arquivos a vincular			
			MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivosVincular, movimentacaoDt.getId(), null, logDt, obFabricaConexao);
			
			// gerar os complementos definidos na MovimentoComplementoNe para cada movimentação
			//this.gerarMovimentosComplementos(movimentacaoDt, obFabricaConexao);
			
		} else {
			throw new MensagemException("Erro ao Gerar Movimentação. O sistema não conseguiu determinar os dados da movimentacao.");
		}
		// Operacoes realizadas com sucesso
		return true;
	}

	/**
	 * Gera movimentacao generica
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/12/2008 17:22
	 * @param ProcessoDt
	 *            processoDt, vo do processo
	 * @param int MovimentacaoTipoCodigo, codigo do tipo de movimentacao
	 * @param String
	 *            complemento, complemento
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario que solicita a operacao
	 * @param List
	 *            arquivosVincular, lista de arquivos a serem vinculados a movimentacao
	 * @param LogDt
	 *            logDt, vo de log
	 * @param FabricaConexao
	 *            conexao, conexao para permitir transacoes
	 * @return boolean
	 * @throws CMSException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws CertificateNotYetValidException 
	 * @throws CertificateExpiredException 
	 * @throws Exception
	 */
	public boolean gerarMovimentacaoGenerica(String id_processo, String numeroProcesso, int movimentacaoTipoCodigo, String complemento, UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao conexao) throws Exception{
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		
		movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(movimentacaoTipoCodigo));
		movimentacaoDt.setId_Processo(id_processo);
		movimentacaoDt.setProcessoNumero(numeroProcesso);
		movimentacaoDt.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
		movimentacaoDt.setComplemento(complemento);
		movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
		movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());
		this.gerarMovimentacao(movimentacaoDt, arquivosVincular, conexao);

		// Cria recibos para a lista de arquivos
		//this.criarReciboArquivos(movimentacaoDt.getId(), movimentacaoDt.getProcessoNumero(), arquivosVincular, conexao);
		
		return true;
	}

//	/**
//	 * Cria recibo de arquivos
//	 * 
//	 * @author Ronneesley Moura Teles
//	 * @since 03/02/2009 14:21
//	 * @param movimentacaoDt
//	 *            movimentacao a qual sera criado o recibo
//	 * @param arquivos
//	 *            lista de arquivos
//	 * @throws CMSException 
//	 * @throws NoSuchProviderException 
//	 * @throws NoSuchAlgorithmException 
//	 * @throws CertificateNotYetValidException 
//	 * @throws CertificateExpiredException 
//	 * @throws Exception
//	 */
//	private void criarReciboArquivos(String id_Movimentacao, String numeroProcesso, List arquivos, FabricaConexao conexao) throws Exception{
//		if (arquivos != null) {
//			Iterator itArquivo = arquivos.iterator();
//			while (itArquivo.hasNext()) {
//				ArquivoDt arquivo = (ArquivoDt) itArquivo.next();
//				this.criarAlterarReciboArquivo(id_Movimentacao, numeroProcesso, arquivo, conexao);
//			}
//		}
//	}

//	/**
//	 * Criar/Alterar recibo do arquivo
//	 * 
//	 * @author Ronneesley Moura Teles
//	 * @since 03/02/2009 14:19
//	 * @param movimentacaoDt
//	 *            movimentacao a qual sera criado o recibo
//	 * @param arquivoDt
//	 *            arquivo a ser vinculado
//	 * @throws Exception
//	 */
//	private void criarAlterarReciboArquivo(String id_Movimentacao, String numeroProcesso, ArquivoDt arquivoDt, FabricaConexao conexao) throws Exception{
//		ArquivoNe arquivoNe = new ArquivoNe();
//
//		if (arquivoDt.conteudoBytes() == null || arquivoDt.conteudoBytes().length == 0) arquivoDt = arquivoNe.consultarId(arquivoDt.getId(), conexao.getConexao());
//
//		if (arquivoDt.getRecibo().equalsIgnoreCase("true")) {
//			// ALTERA RECIBO EXISTENTE
//			Signer.alterarRecibo(id_Movimentacao, numeroProcesso, arquivoDt);
//		} else {
//			// Gera Recibo Assinado Projudi
//			Signer.gerarRecibo(id_Movimentacao, numeroProcesso, arquivoDt);
//		}
//
//		arquivoNe.atualizaConteudoRecibo(arquivoDt, conexao);
//	}

	/**
	 * Gera movimentacao de devolucao sem leitura
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 07/01/2009 10:12
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 * @throws CMSException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws CertificateNotYetValidException 
	 * @throws CertificateExpiredException 
	 * @throws Exception
	 */
	public boolean gerarMovimentacaoDevolucaoSemLeitura(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception{
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.DEVOLUCAO_SEM_LEITURA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de citacao expedida
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 16/12/2008 16:30
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 * @throws CMSException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws CertificateNotYetValidException 
	 * @throws CertificateExpiredException 
	 * @throws Exception
	 */
	public boolean gerarMovimentacaoCitacaoExpedida(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception{
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.CITACAO_EXPEDIDA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Requisicao de pequeno valor
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 * @throws Exception
	 */
	public boolean gerarMovimentacaoRequisicaoPequenoValor(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao)throws Exception{
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.REQUISICAO_PEQUENO_VALOR, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gerar a movimentacao de citacao lida
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 16/12/2008 13:56
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param fabrica
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 * @throws Exception
	 */
	public boolean gerarMovimentacaoCitacaoEfetivada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao)throws Exception{
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.CITACAO_EFETIVADA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gerar a movimentacao de citacao não efetivada
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param fabrica
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 */
	public boolean gerarMovimentacaoCitacaoNaoEfetivada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao)throws Exception{
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.CITACAO_NAO_EFETIVADA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de intimacao expedida
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 06/01/2009 10:30 [antes]
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 * @throws Exception
	 */
	public boolean gerarMovimentacaoIntimacaoExpedida(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.INTIMACAO_EXPEDIDA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de intimacao via telefone efetivada
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean gerarMovimentacaoIntimacaoViaTelefoneEfetivada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.INTIMACAO_VIA_TELEFONE_EFETIVADA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de intimacao via telefone não efetivada
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean gerarMovimentacaoIntimacaoViaTelefoneNaoEfetivada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.INTIMACAO_VIA_TELEFONE_NAO_EFETIVADA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de intimacao efetivada
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 06/01/2009 16:30
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 */
	public boolean gerarMovimentacaoIntimacaoEfetivada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.INTIMACAO_LIDA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao do tipo Intimação Efetivada presencial ou on-line em virtude do cadastro de processo e audiência marcada
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoIntimacaoEfetivadaOnLine(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.INTIMACAO_LIDA, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao do tipo Intimação Efetivada presencial ou on-line em virtude do cadastro de processo e audiência marcada
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada	 
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoIntimacaoCitacaoEfetivadaDiarioEletronico(PendenciaDt pendenciaDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		
		if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO) {
			return gerarMovimentacao(pendenciaDt.getId_Processo(), MovimentacaoTipoDt.CITACAO_EFETIVADA, pendenciaDt.getId_UsuarioCadastrador(), complemento, logDt, conexao);
		} else {
			return gerarMovimentacao(pendenciaDt.getId_Processo(), MovimentacaoTipoDt.INTIMACAO_EFETIVADA, pendenciaDt.getId_UsuarioCadastrador(), complemento, logDt, conexao);
		}
	}

	/**
	 * Gerar movimentacao de intimacao não efetivada
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @return true se a movimentacao foi criada com sucesso, false se nao foi possivel criar a movimentacao
	 */
	public boolean gerarMovimentacaoIntimacaoNaoEfetivada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.INTIMACAO_NAO_EFETIVADA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Ofício Expedido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoOficioExpedido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.OFICIO_EXPEDIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Ofício Efetivado
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoOficioEfetivado(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.OFICIO_EFETIVADO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Ofício Efetivado em Parte
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoOficioEfetivadoParcialmente(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.OFICIO_EFETIVADO_EM_PARTE, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Ofício Não Efetivado
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoOficioNaoEfetivado(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.OFICIO_NAO_EFETIVADO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Mandado Expedido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoMandadoExpedido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.MANDADO_EXPEDIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Mandado Cumprido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoMandadoCumprido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.MANDADO_CUMPRIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Mandado Cumprido em Parte
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoMandadoCumpridoParcialmente(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.MANDADO_CUMPRIDO_EM_PARTE, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Mandado Não Cumprido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoMandadoNaoCumprido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.MANDADO_NAO_CUMPRIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Penhora Realizada
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoPenhoraRealizada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.PENHORA_REALIZADA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Penhora Realizada Parcialmente
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoPenhoraRealizadaParcialmente(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.PENHORA_REALIZADA_EM_PARTE, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Penhora Não Realizada
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoPenhoraNaoRealizada(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.PENHORA_NAO_REALIZADA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera a movimentacao de alvara expedido
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 10/12/2008 15:09
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param FabricaConexao
	 *            conexao, conexao para casos de transacao
	 */
	public boolean gerarMovimentacaoAlvaraExpedido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.ALVARA_EXPEDIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	
	/**
	 * Gera a movimentacao de alvará de soltura expedido.
	 * 
	 * @author hmgodinho
	 * @param id_Processo - processo no qual a movimentacao sera criada
	 * @param numeroProcesso 
	 * @param arquivos - lista de arquivos a serem anexados
	 * @param usuarioDt - usuario que solicita a movimentacao
	 * @param complemento - complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt - objeto de log
	 * @param conexao, conexao para casos de transacao
	 */
	public boolean gerarMovimentacaoAlvaraSolturaExpedido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.ALVARA_SOLTURA_EXPEDIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Alvará Entregue
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoAlvaraEntregue(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.ALVARA_ENTREGUE, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Alvará de soltura Entregue
	 * 
	 * @author hmgodinho
	 * @param id_Processo - processo no qual a movimentacao sera criada
	 * @param numeroProcesso 
	 * @param arquivos - lista de arquivos a serem anexados
	 * @param usuarioDt - usuario que solicita a movimentacao
	 * @param complemento - complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt - objeto de log
	 * @param conexao - fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public String gerarMovimentacaoAlvaraSolturaEntregue(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.ALVARA_SOLTURA_ENTREGUE));
		movimentacaoDt.setId_Processo(id_Processo);
		movimentacaoDt.setProcessoNumero(numeroProcesso);
		movimentacaoDt.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
		movimentacaoDt.setComplemento(complemento);
		movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
		movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());
		this.gerarMovimentacao(movimentacaoDt, arquivos, conexao);

//		// Cria recibos para a lista de arquivos
//		this.criarReciboArquivos(movimentacaoDt.getId(), movimentacaoDt.getProcessoNumero(), arquivos, conexao);
		
		return movimentacaoDt.getId();
	}

	/**
	 * Gera movimentacao de Alvará Cancelado
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoAlvaraCancelado(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.ALVARA_CANCELADO, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Alvará de Soltura Cancelado
	 * 
	 * @author hmgodinho
	 * @param id_Processo - processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos - lista de arquivos a serem anexados
	 * @param usuarioDt - usuario que solicita a movimentacao
	 * @param complemento - complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt - objeto de log
	 * @param conexao - fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoAlvaraSolturaCancelado(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.ALVARA_SOLTURA_CANCELADO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera a movimentacao de Carta Precatória Expedida
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param FabricaConexao
	 *            conexao, conexao para casos de transacao
	 */
	public boolean gerarMovimentacaoCartaPrecatoriaExpedida(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.CARTA_PRECATORIA_EXPEDIDA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Carta Precatória Cumprida
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoCartaPrecatoriaCumprida(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.CARTA_PRECATORIA_CUMPRIDA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Carta Precatória Não Cumprida
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoCartaPrecatoriaNaoCumprida(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.CARTA_PRECATORIA_NAO_CUMPRIDA, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera a movimentacao de Documento Expedido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param FabricaConexao
	 *            conexao, conexao para casos de transacao
	 */
	public boolean gerarMovimentacaoDocumentoExpedido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.DOCUMENTO_EXPEDIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Documento Cumprido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoDocumentoCumprido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.DOCUMENTO_CUMPRIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Documento Cumprido Parcialmente
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoDocumentoCumpridoParcialmente(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.DOCUMENTO_CUMPRIDO_EM_PARTE, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Documento Não Cumprido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoDocumentoNaoCumprido(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.DOCUMENTO_NAO_CUMPRIDO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Processo Distribuído
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoDistribuido(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_DISTRIBUIDO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Processo Redistrbuído
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoRedistribuido(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_REDISTRIBUIDO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Troca de Responsável.
	 * 
	 * @param id_Processo - processo no qual a movimentação será criada
	 * @param id_UsuarioServentia - usuário que solicita a movimentação
	 * @param complemento - texto complementar da movimentação
	 * @param logDt - objeto de log
	 * @param conexao - fábrica para continuar a conexão, caso necessário, senão passe nulo
	 * @author hmgodinho
	 */
	public MovimentacaoDt gerarMovimentacaoTrocaResponsavel(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.TROCAR_RESPONSAVEL_PROCESSO, id_UsuarioServentia, complemento, logDt, conexao);
	}

//	/**
//	 * Gera movimentacao de Processo remetido
//	 * 
//	 * @author jrcorrea
//	 * @param id_Processo
//	 *            processo no qual a movimentacao sera criada
//	 * @param id_UsuarioServentia
//	 *            usuario que solicita a movimentacao
//	 * @param complemento
//	 *            texto complementar
//	 * @param logDt
//	 *            objeto de log
//	 * @param conexao
//	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
//	 */
//	public MovimentacaoDt gerarMovimentacaoProcessoRemetido(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.REMESSA, id_UsuarioServentia, complemento, logDt, conexao);
//	}
//	
	/**
	 * Gera movimentacao de Processo Encaminhado
	 * 
	 * @author hrrosa
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoDevolvidoEncaminhamento(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_DEVOLVIDO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de troca de número de processo. Método criado especificamente para o serviço de troca automática de
	 * números de processos que foram duplicados no SEEU.
	 * 
	 * @author hrrosa
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoNumeroProcessoAlterado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.TROCA_NUMERO_PROCESSO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Processo Devolvido
	 * 
	 * @author hrrosa
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoEncaminhado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_ENCAMINHADO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de "Autos Distribuídos na Turma Recursal"
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoRecursoDistribuido(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.RECURSO_DISTRIBUIDO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de "Autos Distribuídos". Será utilizado para Segundo Grau
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoAutosDistribuidos(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUTOS_DISTRIBUIDOS, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Autos Devolvidos da Turma Recursal
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoAutosDevolvidosTurmaRecursal(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUTOS_DEVOLVIDOS_TURMA_RECURSAL, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Autos Devolvidos do Segundo Grau
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoAutosDevolvidosSegundoGrau(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUTOS_DEVOLVIDOS_SEGUNDO_GRAU, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Processo Arquivado
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoArquivado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_ARQUIVADO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Processo Arquivado Provisoriamente
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoArquivadoProvisoriamente(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_ARQUIVADO_PROVISORIAMENTE, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Processo Desarquivado
	 * 
	 * @author msapaula
	 * @param processoDt
	 *            processo no qual a movimentacao sera criada
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoDesarquivado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_DESARQUIVADO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Processo Suspenso
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoSuspenso(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_SUSPENSO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Término da Supensão de Processo
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoTerminoSuspensaoProcesso(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.TERMINO_SUSPENSAO_PROCESSO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Recurso Autuado
	 * 
	 * @author msapaula
	 * @param processoDt
	 *            processo no qual a movimentacao sera criada
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoRecursoAutuado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.RECURSO_AUTUADO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Petição Enviada. Em alguns casos, a petição enviada adotará outro nome, como é o caso de processos cadastrados por
	 * autoridades policiais.
	 * 
	 * @author msapaula
	 * @param id_Processo - ID do processo que está sendo movimentado
	 * @param serventiaIsJuizado - true se serventia for juizado, false se não for. Só será usado em caso de o usuário que estiver movimentando seja autoridade policial.
	 * @param usuarioDt - usuario que solicita a movimentacao
	 * @param logDt - objeto de log
	 * @param conexao - fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoPeticaoEnviadaCriminal(String id_Processo, String id_ProcessoTipo, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		String complemento = "";
		if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.AUTORIDADES_POLICIAIS) {
			switch (Funcoes.StringToInt(id_ProcessoTipo)) {
				case ProcessoTipoDt.ID_TCO:
					complemento = "TCO";
					break;
				case ProcessoTipoDt.ID_APF:
					complemento = "APF";
					break;
				case ProcessoTipoDt.ID_BOC:
					complemento = "BOC";
					break;
				case ProcessoTipoDt.ID_AAF:
					complemento = "AAF";
					break;
				case ProcessoTipoDt.ID_IP:
					complemento = "IP";
					break;
				case ProcessoTipoDt.ID_AI:
					complemento = "AI";
					break;
				default:
					complemento = "Outros";
					break;
			}
			return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.RECEBIDO, usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);	
		}
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PETICAO_ENVIADA, usuarioDt.getId_UsuarioServentia(), "", logDt, conexao);
	}

	/**
	 * Gera movimentacao de Guia Recolhimento Inserida - Execução Penal
	 * 
	 * @author msapaula
	 * @param processoDt
	 *            processo no qual a movimentacao sera criada
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoGuiaRecolhimentoInserida(String id_Processo, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.GUIA_RECOLHIMENTO_INSERIDA, usuarioDt.getId_UsuarioServentia(), "", logDt, conexao);
	}

	/**
	 * Gera movimentacao de Guia Recolhimento Inserida - Execução Penal - com o usuário "SISTEMA PROJUDI" - para ser utilizado na importação dos dados do execpen
	 * 
	 * @author msapaula
	 * @param processoDt
	 *            processo no qual a movimentacao sera criada
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @throws Exception 
	 */
	public MovimentacaoDt gerarMovimentacaoGuiaRecolhimentoInserida_UsuarioSistemaProjudi(String id_Processo, String id_UsuarioServentia, LogDt logDt, FabricaConexao conexao, String complemento) throws Exception{
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.GUIA_RECOLHIMENTO_INSERIDA, id_UsuarioServentia, complemento, logDt, conexao);
	}


	/**
	 * Gera movimentacao do tipo Autos Conclusos Genérico
	 * 
	 * @param processoDt
	 *            , processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            , usuário que está concluindo o processo
	 * @param logDt
	 *            , objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author msapaula
	 */
	public MovimentacaoDt gerarMovimentacaoConclusaoGenerica(String id_Processo, String id_UsuarioServentia, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUTOS_CONCLUSOS, id_UsuarioServentia, "", logDt, conexao);
	}

	/**
	 * Gera movimentacao de Documento Não Cumprido
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoCalculo(String id_Processo, String numeroProcesso, int movimentacaoTipo,  List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, movimentacaoTipo, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao com Resposta da equipe multidisciplinar
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoRespostaPedidoLaudo(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao com Resposta da equipe multidisciplinar
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoRespostaPedidoCENOPES(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao com Resposta da 
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoRespostaCamaraSaude(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	/**
	 * Gera movimentacao do tipo Autos Conclusos e, de acordo com o tipo da conclusão monta o complemento adequado
	 * 
	 * @param processoDt
	 *            , processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            , usuário que está concluindo o processo
	 * @param pendenciaTipoCodigo
	 *            , tipo da conclusão que está sendo gerada
	 * @param logDt
	 *            , objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author msapaula
	 */
	public MovimentacaoDt gerarMovimentacaoConclusao(String id_Processo, String id_UsuarioServentia, String pendenciaTipoCodigo, LogDt logDt, FabricaConexao conexao) throws Exception {
		String complemento = "";

		switch (Funcoes.StringToInt(pendenciaTipoCodigo)) {
		case PendenciaTipoDt.CONCLUSO_DECISAO:
		case PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA:
			complemento = "P/ DECISÃO";
			break;
		case PendenciaTipoDt.CONCLUSO_DESPACHO:
			complemento = "P/ DESPACHO";
			break;
		case PendenciaTipoDt.CONCLUSO_SENTENCA:
			complemento = "P/ SENTENÇA";
			break;
		case PendenciaTipoDt.CONCLUSO_PRESIDENTE:
			complemento = "P/ O PRESIDENTE";
			break;
		case PendenciaTipoDt.CONCLUSO_RELATOR:
			complemento = "P/ O RELATOR";
			break;
		case PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE:
			complemento = "P/ DESPACHO DO PRESIDENTE";
			break;
		case PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR:
			complemento = "P/ DESPACHO DO RELATOR";
			break;
		case PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO:
			complemento = "P/ O PRESIDENTE DO TRIBUNAL DE JUSTIÇA";
			break;
		case PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO:
			complemento = "P/ O VICE PRESIDENTE DO TRIBUNAL DE JUSTIÇA";
			break;
		}
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUTOS_CONCLUSOS, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao do tipo Autos Conclusos e, de acordo com o tipo da conclusão monta o complemento adequado
	 * 
	 * @param processoDt
	 *            , processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            , usuário que está concluindo o processo
	 * @param pendenciaTipoCodigo
	 *            , tipo da conclusão que está sendo gerada
	 * @param logDt
	 *            , objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author lsbernardes
	 */
	public MovimentacaoDt gerarMovimentacaoConclusaoSegundoGrau(String id_Processo, String id_UsuarioServentia, String pendenciaTipoCodigo, LogDt logDt, FabricaConexao conexao) throws Exception {
		String complemento = "";

		switch (Funcoes.StringToInt(pendenciaTipoCodigo)) {
		case PendenciaTipoDt.CONCLUSO_DECISAO:
			complemento = "P/ DECISÃO";
			break;
		case PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA:
			complemento = "COM PEDIDO DE BENEFÍCIO DE ASSISTÊNCIA";
			break;
		case PendenciaTipoDt.CONCLUSO_DESPACHO:
			complemento = "P/ DESPACHO";
			break;
		case PendenciaTipoDt.CONCLUSO_SENTENCA:
			complemento = "P/ SENTENÇA";
			break;
		case PendenciaTipoDt.CONCLUSO_PRESIDENTE:
			complemento = "P/ O PRESIDENTE";
			break;
		case PendenciaTipoDt.CONCLUSO_RELATOR:
			complemento = "P/ O RELATOR";
			break;
		case PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE:
			complemento = "P/ DESPACHO DO PRESIDENTE";
			break;
		case PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR:
			complemento = "P/ DESPACHO DO RELATOR";
			break;
		case PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO:
			complemento = "P/ O PRESIDENTE DO TRIBUNAL DE JUSTIÇA";
			break;
		case PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO:
			complemento = "P/ O VICE PRESIDENTE DO TRIBUNAL DE JUSTIÇA";
			break;
		}
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUTOS_CONCLUSOS, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Una Marcada
	 */
	//	public MovimentacaoDt gerarMovimentacaoAudienciaUnaMarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_INSTRUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Conciliação Marcada
	 */
	//	public MovimentacaoDt gerarMovimentacaoAudienciaConciliacaoMarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Instrução Marcada
	 */
	//	public MovimentacaoDt gerarMovimentacaoAudienciaInstrucaoMarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - pode ser usado para todos os tipos de audiência.
	 * 
	 * @author hmgodinho
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaMarcada(String id_Processo, String id_UsuarioServentia, String complemento, String audienciaTipoCodigo, LogDt logDt, FabricaConexao conexao) throws Exception {
		AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
		tipoCodigo = tipoCodigo.getCodigo(Funcoes.StringToInt(audienciaTipoCodigo));
		MovimentacaoDt movimentacaoRetorno = null;
		switch (tipoCodigo) {
		case CONCILIACAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE CONCILIAÇÃO MARCADA");
			break;
		case INSTRUCAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INSTRUÇÃO MARCADA");
			break;
		case INSTRUCAO_JULGAMENTO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_JULGAMENTO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INSTRUÇÃO E JULGAMENTO MARCADA");
			break;
		case UNA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_INSTRUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA CONCILIAÇÃO/INSTRUÇÃO MARCADA");
			break;
		case PRELIMINAR:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_PRELIMINAR_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA PRELIMINAR MARCADA");
			break;
		case ADMONITORIA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_ADMONITORIA_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA ADMONITÓRIA MARCADA");
			break;
		case INTERROGATORIO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INTERROGATORIO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INTERROGATÓRIO MARCADA");
			break;
		case JUSTIFICACAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_JUSTIFICACAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA JUSTIFICAÇÃO MARCADA");
			break;
		case EXECUCAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_EXECUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA EXECUÇÃO MARCADA");
			break;
		case INICIAL:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INICIAL_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INICIAL MARCADA");
			break;
		case JULGAMENTO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_JULGAMENTO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA JULGAMENTO MARCADA");
			break;
		case SINE_DIE:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SINE_DIE_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA SINE DIE MARCADA");
			break;
		case INQUIRICAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INQUIRICAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INQUIRIÇÃO MARCADA");
			break;
		case SUSPENSAO_CONDICIONAL:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SUSPENSAO_CONDICIONAL_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA SUSPENSÃO CONDICIONAL MARCADA");
			break;
		case INQUIRICAO_TESTEMUNHA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INQUIRICAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INQUIRIÇÃO DE TESTEMUNHA MARCADA");
			break;
		case CONCILIACAO_CEJUSC:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_CEJUSC_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE CONCILIAÇÃO CEJUSC MARCADA");
			break;
		case CONCILIACAO_CEJUSC_DPVAT:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC_DPVAT, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE CONCILIAÇÃO CEJUSC DPVAT MARCADA");
			break;
		case MEDIACAO_CEJUSC:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_MEDIACAO_CEJUSC_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE MEDIAÇÃO CEJUSC MARCADA");
			break;
		case PRELIMINAR_CONCILIADOR:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_PRELIMINAR_CONCILIADOR_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA PRELIMINAR/CONCILIADOR MARCADA");
			break;
		case CUSTODIA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CUSTODIA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA CUSTÓDIA MARCADA");
			break;
		case ART334:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_ART334, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA ARTIGO 334 CPC MARCADA");
			break;
		default:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA MARCADA");
			break;

		}
		return movimentacaoRetorno;
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - pode ser usado para todos os tipos de audiência.
	 * 
	 * @author acbloureiro
	 */

	public MovimentacaoDt gerarMovimentacaoAudienciaStatus(String id_Processo, String id_UsuarioServentia, String complemento, AudienciaDt audienciaDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		
		AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
		tipoCodigo = tipoCodigo.getCodigo(Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()));
		MovimentacaoDt movimentacaoRetorno = null;
		switch (tipoCodigo) {
		case CONCILIACAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE CONCILIAÇÃO");
			break;
		case INSTRUCAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INSTRUÇÃO");
			break;
		case INSTRUCAO_JULGAMENTO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_JULGAMENTO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INSTRUÇÃO E JULGAMENTO");
			break;
		case UNA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_INSTRUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA CONCILIAÇÃO/INSTRUÇÃO");
			break;
		case PRELIMINAR:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_PRELIMINAR_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA PRELIMINAR");
			break;
		case ADMONITORIA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_ADMONITORIA_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA ADMONITÓRIA");
			break;
		case INTERROGATORIO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INTERROGATORIO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INTERROGATÓRIO");
			break;
		case JUSTIFICACAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_JUSTIFICACAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA JUSTIFICAÇÃO");
			break;
		case EXECUCAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_EXECUCAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA EXECUÇÃO");
			break;
		case INICIAL:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INICIAL_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INICIAL");
			break;
		case JULGAMENTO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_JULGAMENTO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA JULGAMENTO");
			break;
		case SINE_DIE:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SINE_DIE_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA SINE DIE");
			break;
		case INQUIRICAO:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INQUIRICAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INQUIRIÇÃO");
			break;
		case SUSPENSAO_CONDICIONAL:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SUSPENSAO_CONDICIONAL_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA SUSPENSÃO CONDICIONAL");
			break;
		case INQUIRICAO_TESTEMUNHA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INQUIRICAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA INQUIRIÇÃO DE TESTEMUNHA");
			break;
		case CONCILIACAO_CEJUSC:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_CEJUSC_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE CONCILIAÇÃO CEJUSC");
			break;
		case CONCILIACAO_CEJUSC_DPVAT:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC_DPVAT, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE CONCILIAÇÃO CEJUSC DPVAT");
			break;
		case MEDIACAO_CEJUSC:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_MEDIACAO_CEJUSC_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA DE MEDIAÇÃO CEJUSC");
			break;
		case PRELIMINAR_CONCILIADOR:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_PRELIMINAR_CONCILIADOR_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA PRELIMINAR/CONCILIADOR");
			break;
		case CUSTODIA:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_CUSTODIA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA CUSTÓDIA");
			break;
		case ART334:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_ART334, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA ARTIGO 334 CPC");
			break;
		default:
			movimentacaoRetorno = gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
			gerarMovimentosComplementos(movimentacaoRetorno, audienciaDt, conexao);
			movimentacaoRetorno.setMovimentacaoTipo("AUDIÊNCIA");
			break;

		}
		return movimentacaoRetorno;
	}

	
	
	
	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Instrução e Julgamento Marcada
	 */
	//	public MovimentacaoDt gerarMovimentacaoAudienciaInstrucaoJulgamentoMarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_JULGAMENTO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Preliminar Marcada
	 */
	//	public MovimentacaoDt gerarMovimentacaoAudienciaPreliminarMarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_PRELIMINAR_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	/**
	 * Gera movimentação do tipo "Incluído em pauta", para sessões de 2º grau. Realiza chamada ao método genérico gerarMovimentacao()
	 * 
	 * @param processoDt
	 *            , processto relacionado a movimentação
	 * @param id_UsuarioServentia
	 *            , usuário que está marcando a sessão
	 * @param complemento
	 *            , data e hora da movimentação
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaSessaoMarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SESSAO_MARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentação do tipo "Retirado de pauta", para sessões de 2º grau. Realiza chamada ao método genérico gerarMovimentacao()
	 * 
	 * @param id_Processo
	 *            , processo relacionado a movimentação
	 * @param id_UsuarioServentia
	 *            , usuário que está desmarcando a sessão
	 * @param complemento
	 *            , data e hora da movimentação
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaSessaoDesmarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SESSAO_DESMARCADA_PAUTA, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentação do tipo "Julgamento Desmarcado", para sessões de 2º grau. Realiza chamada ao método genérico gerarMovimentacao()
	 * 
	 * @param id_Processo
	 *            , processo relacionado a movimentação
	 * @param id_UsuarioServentia
	 *            , usuário que está desmarcando a sessão
	 * @param complemento
	 *            , data e hora da movimentação
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaSessaoRetirada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SESSAO_RETIRADA_PAUTA, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Desmarcada
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaDesmarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_DESMARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Negativa
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaNegativa(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_NEGATIVA, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Remarcada
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaRemarcada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_REMARCADA, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Realizada com Sentença com Mérito
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaRealizadaSentencaComMerito(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_COM_SENTENCA_COM_MERITO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Realizada com Sentença sem Mérito
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaRealizadaSentencaSemMerito(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_COM_SENTENCA_SEM_MERITO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Realizada sem Sentença
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaRealizadaSemSentenca(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SEM_SENTENCA, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Relacionada ao Tipo da Audiência (substitui os métodos acima) 
	 * acbloureiro
	 */
	public MovimentacaoDt gerarMovimentacaoAudiencia(String id_Processo, String id_UsuarioServentia, String complemento, AudienciaDt audDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo,Integer.parseInt(audDt.getAudienciaTipoCodigo()), id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Realizada sem Sentença
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaRealizada(String id_Processo, String id_UsuarioServentia, String complemento, int movimentacaoTipo, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, movimentacaoTipo, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - Audiência Realizada com Sentença de Homologação
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaRealizadaSentencaHomologacao(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_SENTENCA_HOMOLOGACAO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Mandado de Prisão
	 * 
	 * @param processoDt
	 *            processo no qual a movimentacao sera criada
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoMandadoPrisao(String id_Processo, int movimentacaoTipo, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {

			return gerarMovimentacao(id_Processo, movimentacaoTipo, usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
		
		
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - CONHECIDO EM PARTE O RECURSO DE PARTE E NÃO-PROVIDO
	 */
	public MovimentacaoDt gerarMovimentacaoSessao(String id_Processo, String id_UsuarioServentia, String complemento, boolean houveAlteracaoExtratoAta, LogDt logDt, int audienciaStatus, FabricaConexao conexao) throws Exception {
		int inMovimentacaoTipo = 0;

		switch (audienciaStatus) {
		case AudienciaProcessoStatusDt.CONHECIDO_EM_PARTE_NAO_PROVIDO:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONHECIDO_EM_PARTE_NAO_PROVIDO;
			//complemento="CONHECIDO EM PARTE E NÃO-PROVIDO";
			break;
		case AudienciaProcessoStatusDt.CONHECIDO_EM_PARTE_PROVIDO:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONHECIDO_EM_PARTE_PROVIDO;
			//complemento="CONHECIDO EM PARTE E PROVIDO";
			break;
		case AudienciaProcessoStatusDt.CONHECIDO_EM_PARTE_PROVIDO_EM_PARTE:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONHECIDO_EM_PARTE_PROVIDO_EM_PARTE;
			///complemento="CONHECIDO EM PARTE E PROVIDO EM PARTE";
			break;
		case AudienciaProcessoStatusDt.CONHECIDO_NAO_PROVIDO:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONHECIDO_NAO_PROVIDO;
			//complemento="CONHECIDO E NÃO-PROVIDO";
			break;
		case AudienciaProcessoStatusDt.CONHECIDO_PROVIDO:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONHECIDO_PROVIDO;
			//complemento="CONHECIDO E PROVIDO";
			break;
		case AudienciaProcessoStatusDt.CONHECIDO_PROVIDO_EM_PARTE:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONHECIDO_PROVIDO_EM_PARTE;
			//complemento="CONHECIDO E PROVIDO EM PARTE";
			break;

		case AudienciaProcessoStatusDt.CONHECIDO_PROVIDO_MONOCRATICO:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONHECIDO_PROVIDO_MONOCRATICO;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.SEGURANCA_CONCEDIDA:
			inMovimentacaoTipo = MovimentacaoTipoDt.SEGURANCA_CONCEDIDA;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.SEGURANCA_DENEGADA:
			inMovimentacaoTipo = MovimentacaoTipoDt.SEGURANCA_DENEGADA;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.SEGURANCA_CONCEDIDA_EM_PARTE:
			inMovimentacaoTipo = MovimentacaoTipoDt.SEGURANCA_CONCEDIDA_EM_PARTE;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.EMBARGOS_ACOLHIDOS:
			inMovimentacaoTipo = MovimentacaoTipoDt.EMBARGOS_ACOLHIDOS;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.EMBARGOS_ACOLHIDOS_EM_PARTE:
			inMovimentacaoTipo = MovimentacaoTipoDt.EMBARGOS_ACOLHIDOS_EM_PARTE;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.EMBARGOS_NAO_ACOLHIDOS:
			inMovimentacaoTipo = MovimentacaoTipoDt.EMBARGOS_NAO_ACOLHIDOS;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.RECURSO_PREJUDICADO:
			inMovimentacaoTipo = MovimentacaoTipoDt.RECURSO_PREJUDICADO;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.PETICAO_INDEFERIDA:
			inMovimentacaoTipo = MovimentacaoTipoDt.PETICAO_INDEFERIDA;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.EXTINTO_AUSENCIA_PRESSUPOSTOS:
			inMovimentacaoTipo = MovimentacaoTipoDt.EXTINTO_AUSENCIA_PRESSUPOSTOS;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.EXTINTO_AUSENCIA_CONDICOES:
			inMovimentacaoTipo = MovimentacaoTipoDt.EXTINTO_AUSENCIA_CONDICOES;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.EXTINTO_DESISTENCIA:
			inMovimentacaoTipo = MovimentacaoTipoDt.EXTINTO_DESISTENCIA;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.HOMOLOGADO_DESISTENCIA:
			inMovimentacaoTipo = MovimentacaoTipoDt.HOMOLOGADO_DESISTENCIA;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.NAO_CONHECIDO: {
			inMovimentacaoTipo = MovimentacaoTipoDt.NAO_CONHECIDO;
			new ProcessoResponsavelNe().desativarRedatorProcesso(id_Processo, conexao);
			break;
		}
		case AudienciaProcessoStatusDt.ARGUICAO_INCONSTITUCIONALIDADE_ACOLHIDA: {
			inMovimentacaoTipo = MovimentacaoTipoDt.ARGUICAO_INCONSTITUCIONALIDADE_ACOLHIDA;
			break;
		}
		case AudienciaProcessoStatusDt.ARGUICAO_INCONSTITUCIONALIDADE_NAO_ACOLHIDA: {
			inMovimentacaoTipo = MovimentacaoTipoDt.ARGUICAO_INCONSTITUCIONALIDADE_NAO_ACOLHIDA;
			break;
		}
		case AudienciaProcessoStatusDt.COMPETENCIA_DECLINADA: {
			inMovimentacaoTipo = MovimentacaoTipoDt.COMPETENCIA_DECLINADA;
			break;
		}
		case AudienciaProcessoStatusDt.PROCESSO_SUSPENSO_DEPENDENCIA_JULGAMENTO_OUTRA_CAUSA: {
			inMovimentacaoTipo = MovimentacaoTipoDt.PROCESSO_SUSPENSO_DEPENDENCIA_JULGAMENTO_OUTRA_CAUSA;
			break;
		}
		case AudienciaProcessoStatusDt.A_SER_REALIZADA:
			if (houveAlteracaoExtratoAta) inMovimentacaoTipo = MovimentacaoTipoDt.SESSAO_ATA_JULGAMENTO_INSERIDA_CORRECAO;
			else inMovimentacaoTipo = MovimentacaoTipoDt.SESSAO_ATA_JULGAMENTO_INSERIDA;
			//complemento="NÃO CONHECIDO";
			break;
		case AudienciaProcessoStatusDt.JULGAMENTO_ADIADO:
			inMovimentacaoTipo = MovimentacaoTipoDt.SESSAO_JULGAMENTO_ADIADO;
			break;
		case AudienciaProcessoStatusDt.JULGAMENTO_INICIADO:
			inMovimentacaoTipo = MovimentacaoTipoDt.SESSAO_JULGAMENTO_INICIADO;
			break;
		case AudienciaProcessoStatusDt.CAUTELAR_DEFERIDA:
			inMovimentacaoTipo = MovimentacaoTipoDt.CAUTELAR_DEFERIDA;
			//complemento="CAUTELAR DEFERIDA";
			break;			
		case AudienciaProcessoStatusDt.CAUTELAR_INDEFERIDA:
			inMovimentacaoTipo = MovimentacaoTipoDt.CAUTELAR_INDEFERIDA;
			//complemento="CAUTELAR INDEFERIDA";
			break;
		case AudienciaProcessoStatusDt.EXTINTO_SEM_RESOLUCAO_DO_MERITO:
			inMovimentacaoTipo = MovimentacaoTipoDt.EXTINTO_SEM_RESOLUCAO_DO_MERITO;
			//complemento="EXTINTO SEM RESOLUÇÃO DO MÉRITO";
			break;
		case AudienciaProcessoStatusDt.ADMITIDO_IRDR:
			inMovimentacaoTipo = MovimentacaoTipoDt.ADMITIDO_IRDR;
			//complemento="ADMITIDO IRDR";
			break;
		case AudienciaProcessoStatusDt.NAO_ADMITIDO_IRDR:
			inMovimentacaoTipo = MovimentacaoTipoDt.NAO_ADMITIDO_IRDR;
			//complemento="NAO ADMITIDO IRDR";
			break;
		case AudienciaProcessoStatusDt.ADMITIDO_IAC:
			inMovimentacaoTipo = MovimentacaoTipoDt.ADMITIDO_IAC;
			//complemento="ADMITIDO IAC";
			break;
		case AudienciaProcessoStatusDt.NAO_ADMITIDO_IAC:
			inMovimentacaoTipo = MovimentacaoTipoDt.NAO_ADMITIDO_IAC;
			//complemento="NAO ADMITIDO IAC";
			break;
		case AudienciaProcessoStatusDt.CONCESSAO:
			inMovimentacaoTipo = MovimentacaoTipoDt.CONCESSAO;
			//complemento="Concessão";
			break;
		case AudienciaProcessoStatusDt.NAO_CONCESSAO:
			inMovimentacaoTipo = MovimentacaoTipoDt.NAO_CONCESSAO;
			//complemento="Não Concessão";
			break;
		case AudienciaProcessoStatusDt.EXCECAO_IMPEDIMENTO_SUSPEICAO_REJEITADO:
			inMovimentacaoTipo = MovimentacaoTipoDt.EXCECAO_IMPEDIMENTO_SUSPEICAO_REJEITADO;
			//complemento="Exceção de Impedimento ou Suspeição Rejeitado";
			break;
		case AudienciaProcessoStatusDt.EXCECAO_IMPEDIMENTO_SUSPEICAO_ACOLHIDO:
			inMovimentacaoTipo = MovimentacaoTipoDt.EXCECAO_IMPEDIMENTO_SUSPEICAO_ACOLHIDO;
			//complemento="Exceção de Impedimento ou Suspeição Acolhido";
			break;
		case AudienciaProcessoStatusDt.JUIZO_DE_RETRATACAO_EFETIVADO:
			inMovimentacaoTipo = MovimentacaoTipoDt.JUIZO_DE_RETRATACAO_EFETIVADO;
			//complemento="Retratação Efetivada";
			break;
		case AudienciaProcessoStatusDt.JUIZO_DE_RETRATACAO_NAO_EFETIVADO:
			inMovimentacaoTipo = MovimentacaoTipoDt.JUIZO_DE_RETRATACAO_NAO_EFETIVADO;
			//complemento="Retratação Não Efetivada";
			break;
		case AudienciaProcessoStatusDt.JUIZO_DE_RETRATACAO_EFETIVADO_PARCIALMENTE:
			inMovimentacaoTipo = MovimentacaoTipoDt.JUIZO_DE_RETRATACAO_EFETIVADO_PARCIALMENTE;
			//complemento="Retratação Efetivada Parcialmente";
			break;
		}
		
		if (inMovimentacaoTipo == 0) {
			throw new MensagemException(String.format("Tipo de Movimentação não foi identificado para o status do julgamento inserido no extrato da ata de julgamento (%s), solicite à câmara que faça a correção do status do julgamento ou entre em contato com o suporte.", audienciaStatus));
		}

		return gerarMovimentacao(id_Processo, inMovimentacaoTipo, id_UsuarioServentia, complemento, logDt, conexao);
	}

	//	/**
	//	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - CONHECIDO EM PARTE O RECURSO DE PARTE E PROVIDO
	//	 */
	//	public MovimentacaoDt gerarMovimentacaoConhecimentoParteProvimento(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.CONHECIDO_EM_PARTE_PROVIDO, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	//
	//	/**
	//	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - CONHECIDO EM PARTE O RECURSO DE PARTE E PROVIDO EM PARTE
	//	 */
	//	public MovimentacaoDt gerarMovimentacaoConhecimentoParteProvimentoParte(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.CONHECIDO_EM_PARTE_PROVIDO_EM_PARTE, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	//
	//	/**
	//	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - CONHECIDO O RECURSO DE PARTE E PROVIDO
	//	 */
	//	public MovimentacaoDt gerarMovimentacaoProvimento(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.CONHECIDO_PROVIDO, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	//
	//	/**
	//	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - CONHECIDO O RECURSO DE PARTE E PROVIDO EM PARTE
	//	 */
	//	public MovimentacaoDt gerarMovimentacaoProvimentoParte(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.CONHECIDO_PROVIDO_EM_PARTE, id_UsuarioServentia, complemento, logDt, conexao);
	//	}
	//
	//	/**
	//	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - CONHECIDO O RECURSO DE PARTE E NÃO-PROVIDO
	//	 */
	//	public MovimentacaoDt gerarMovimentacaoNaoProvimento(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao){
	//		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.CONHECIDO_NAO_PROVIDO, id_UsuarioServentia, complemento, logDt, conexao);
	//	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - PEDIDO DE VISTA
	 */
	public MovimentacaoDt gerarMovimentacaoPedidoVista(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PEDIDO_VISTA, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Realiza chamada ao método genérico gerarMovimentacao(), passando o tipo da Movimentação - PEDIDO DE VISTA
	 */
	public MovimentacaoDt gerarMovimentacaoConclusaoGenerica(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUTOS_CONCLUSOS, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de Intimação Expedida no caso em que a intimação vai diretamente para um advogado da parte, sem ter que ser expedida pelo cartório
	 * 
	 * @param pendenciaDt
	 *            , dados da pendência
	 * @param intimacaoAudiencia
	 *            , define se intimação foi realizada em audiência/cartório
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @author msapaula
	 */
	public void gerarMovimentacaoIntimacaoOuCitacaoAdvogado(PendenciaDt pendenciaDt, boolean intimacaoAudiencia, FabricaConexao conexao) throws Exception {
		int tipoMovimentacao;
		String complemento = "";
		LogDt logDt = new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog());

		if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO) {
			tipoMovimentacao = MovimentacaoTipoDt.CITACAO_EXPEDIDA;
			complemento = "On-line para Advgs. de " + pendenciaDt.getNomeParte() + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
		} else {
			if (intimacaoAudiencia) {
				tipoMovimentacao = MovimentacaoTipoDt.INTIMACAO_REALIZADA_AUDIENCIA;
				complemento = "Para Advgs. de " + pendenciaDt.getNomeParte() + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
			} else {
				tipoMovimentacao = MovimentacaoTipoDt.INTIMACAO_EXPEDIDA;
				complemento = "On-line para Advgs. de " + pendenciaDt.getNomeParte() + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
			}
		}

		this.gerarMovimentacao(pendenciaDt.getId_Processo(), tipoMovimentacao, pendenciaDt.getId_UsuarioCadastrador(), complemento, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de Intimação Expedida ou Citação Expedida no caso em que a esta vai diretamente para uma parte cadastrada (on-line), sem ter que ser expedida pelo cartório
	 * 
	 * @param pendenciaDt
	 *            , objeto com dados a serem usados para gerar a movimentação
	 * @param intimacaoAudiencia
	 *            , define se intimação foi realizada em audiência/cartório
	 * @param conexao
	 *            fabrica para continuar a conexao
	 * 
	 * @author msapaula
	 */
	public void gerarMovimentacaoIntimacaoOuCitacaoParte(PendenciaDt pendenciaDt, boolean intimacaoAudiencia, FabricaConexao conexao) throws Exception {
		int tipoMovimentacao;
		String complemento = "";
		LogDt logDt = new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog());

		if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO) {
			tipoMovimentacao = MovimentacaoTipoDt.CITACAO_EXPEDIDA;
			complemento = "On-line para " + pendenciaDt.getNomeParte() + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
		} else {
			if (intimacaoAudiencia) {
				tipoMovimentacao = MovimentacaoTipoDt.INTIMACAO_REALIZADA_AUDIENCIA;
				complemento = "Para " + pendenciaDt.getNomeParte() + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
			} else {
				tipoMovimentacao = MovimentacaoTipoDt.INTIMACAO_EXPEDIDA;
				complemento = "On-line para " + pendenciaDt.getNomeParte() + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
			}
		}

		this.gerarMovimentacao(pendenciaDt.getId_Processo(), tipoMovimentacao, UsuarioServentiaDt.SistemaProjudi, complemento, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de Intimação Expedida no caso em que a esta vai diretamente para uma Promotoria, sem ter que ser expedida pelo cartório
	 * 
	 * @param pendenciaDt
	 *            , objeto com dados a serem usados para gerar a movimentação
	 * @param descricaoPromotoria
	 *            , promotoria para a qual está sendo gerada a intimação
	 * @param intimacaoAudiencia
	 *            , define se intimação foi realizada em audiência/cartório
	 * @param conexao
	 *            fabrica para continuar a conexao
	 * 
	 * @author msapaula
	 */
	public void gerarMovimentacaoIntimacaoMinisterioPublico(PendenciaDt pendenciaDt, String descricaoPromotoria, boolean intimacaoAudiencia, FabricaConexao conexao) throws Exception {
		int tipoMovimentacao;
		String complemento = null;
		LogDt logDt = new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog());

		if (intimacaoAudiencia) {
			tipoMovimentacao = MovimentacaoTipoDt.INTIMACAO_REALIZADA_AUDIENCIA;
			complemento = "Para " + descricaoPromotoria + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
		} else {
			tipoMovimentacao = MovimentacaoTipoDt.INTIMACAO_EXPEDIDA;
			complemento = "On-line para " + descricaoPromotoria + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
		}

		this.gerarMovimentacao(pendenciaDt.getId_Processo(), tipoMovimentacao, UsuarioServentiaDt.SistemaProjudi, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao do tipo Intimação Efetivada presencial para uma parte em virtude do cadastramento de um processo com audiência marcada
	 * 
	 * @param id_Processo
	 *            , processo no qual a movimentacao sera criada
	 * @param nomeParte
	 *            , parte vinculada a intimação
	 * @param movimentacao
	 *            , descrição da movimentação que originou a intimação
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * @author msapaula
	 */
	public MovimentacaoDt gerarMovimentacaoIntimacaoEfetivadaParteProcesso(String id_Processo, String nomeParte, String movimentacao, LogDt logDt, FabricaConexao conexao) throws Exception {
		String complemento = "Presencial para " + nomeParte + " (Referente à Mov. " + movimentacao + ")";
		return this.gerarMovimentacaoIntimacaoEfetivadaOnLine(id_Processo, UsuarioServentiaDt.SistemaProjudi, complemento, logDt, conexao);
	}

	/**
	 * Gerar movimentacao de Intimação Realizada em Audiência/Cartório no caso de ser para a Serventia
	 * 
	 * @param pendenciaDt
	 *            , objeto com dados a serem usados para gerar a movimentação
	 * @param intimacaoAudiencia
	 *            , define se intimação foi realizada em audiência/cartório
	 * @param conexao
	 *            fabrica para continuar a conexao
	 * 
	 * @author msapaula
	 */
	public void gerarMovimentacaoIntimacaoAudienciaProcessoParte(PendenciaDt pendenciaDt, boolean intimacaoAudiencia, FabricaConexao conexao) throws Exception {
		LogDt logDt = new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog());
		int tipoMovimentacao = MovimentacaoTipoDt.INTIMACAO_REALIZADA_AUDIENCIA;

		String complemento = "Para " + pendenciaDt.getNomeParte() + " (Referente à Mov. " + pendenciaDt.getMovimentacao() + ")";
		this.gerarMovimentacao(pendenciaDt.getId_Processo(), tipoMovimentacao, UsuarioServentiaDt.SistemaProjudi, complemento, logDt, conexao);
	}

	/**
	 * Método genérico para gerar quaisquer movimentações
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param movimentacaoTipoCodigo
	 *            , tipo da movimentação a ser realizada
	 * @param id_UsuarioRealizador
	 *            , usuário que está gerando a movimentação
	 * @param complemento
	 *            , complemento da movimentação
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 */
	public MovimentacaoDt gerarMovimentacao(String id_Processo, int movimentacaoTipoCodigo, String id_UsuarioRealizador, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		
		movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(movimentacaoTipoCodigo));

		//Nesses casos deve setar a descrição da movimentação para que o complemento saia correto
		/*
		 * if (movimentacaoTipoCodigo == MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_MARCADA || movimentacaoTipoCodigo == MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_MARCADA || movimentacaoTipoCodigo == MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_INSTRUCAO_MARCADA || movimentacaoTipoCodigo == MovimentacaoTipoDt.AUDIENCIA_PRELIMINAR_MARCADA || movimentacaoTipoCodigo == MovimentacaoTipoDt.AUDIENCIA_SESSAO_MARCADA) {movimentacaoDt.setMovimentacao("AUDIÊNCIA MARCADA"); }
		 */
		movimentacaoDt.setId_Processo(id_Processo);
		movimentacaoDt.setId_UsuarioRealizador(id_UsuarioRealizador);
		movimentacaoDt.setComplemento(complemento);
		movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
		movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());

		salvar(movimentacaoDt, conexao);
		
		return obDados;
	}

	/**
	 * Método genérico para gerar quaisquer movimentações
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param movimentacaoTipoCodigo
	 *            , tipo da movimentação a ser realizada
	 * @param id_UsuarioRealizador
	 *            , usuário que está gerando a movimentação
	 * @param complemento
	 *            , complemento da movimentação
	 * @param ip
	 *            do computador do usuario, objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author jrcorrea
	 */
	public MovimentacaoDt gerarMovimentacao(String id_Processo, int movimentacaoTipoCodigo, String id_UsuarioRealizador, String complemento, String ip, String id_usuarioLog, FabricaConexao conexao) throws Exception {
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		
		movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(movimentacaoTipoCodigo));

		movimentacaoDt.setId_Processo(id_Processo);
		movimentacaoDt.setId_UsuarioRealizador(id_UsuarioRealizador);
		movimentacaoDt.setComplemento(complemento);
		movimentacaoDt.setId_UsuarioLog(id_usuarioLog);
		movimentacaoDt.setIpComputadorLog(ip);

		salvar(movimentacaoDt, conexao);

		return obDados;
	}

	/**
	 * Chama PendenciaTipoNe para montar lista de destinatários possíveis para pendências
	 */
	public List montarListaDestinatarios(String tipoPendencia, ProcessoDt processoDt, String serventiaSubTipoCodigo, String id_ServentiaLogada) throws Exception {
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		List tempList = null;

		tempList = pendenciaTipoNe.montarListaDestinatarios(tipoPendencia, processoDt, serventiaSubTipoCodigo, id_ServentiaLogada);		
		
		pendenciaTipoNe = null;
		return tempList;
	}

	/**
	 * Efetua chamada a PendenciaArquivoNe para salvar a pré-analise da pendência
	 * 
	 * @param preAnaliseConclusaoDt
	 *            : dados da pré-analise
	 * @param usuarioDt
	 *            : usuário responsável
	 * @return String, retorna o id_Aquivo da pré-analise, informação importante para distribuir uma pre-analise multipla 
	 * @throws Exception
	 */
	public String salvarPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		String retorno = arquivoNe.salvarPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt);
		arquivoNe = null;
		return retorno;
	}

	/**
	 * Efetua chamada a PendenciaArquivoNe para salvar a pré-analise da pendência
	 * 
	 * @param preAnalisePendenciaDt
	 *            , dados da pré-analise
	 * @param usuarioDt
	 *            , usuário responsável
	 * @throws Exception
	 */
	public void salvarPreAnalisePendencia(AnalisePendenciaDt preAnalisePendenciaDt, UsuarioDt usuarioDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		arquivoNe.salvarPreAnalisePendencia(preAnalisePendenciaDt, usuarioDt);
		
		arquivoNe = null;
	}

	/**
	 * Verifica os dados obrigatórios em uma análise de conclusões. É obrigatório que seja especificado o tipo da movimentação e um arquivo seja inserido.
	 * 
	 * @param dados
	 */
	public String verificarAnaliseConclusao(AnaliseConclusaoDt dados, UsuarioNe usuarioSessao) {
		String stRetorno = "";
		if (dados.getId_MovimentacaoTipo() == null || dados.getId_MovimentacaoTipo().equalsIgnoreCase("null") || dados.getId_MovimentacaoTipo().length() == 0) stRetorno += "Selecione o Tipo da Movimentação. \n";
		if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) stRetorno += "É necessário inserir um arquivo. \n";

		if (dados.isVisualizarPedidoAssistencia()) {
			if (dados.getPedidoAssistencia() == null || dados.getPedidoAssistencia().equalsIgnoreCase("null") || dados.getPedidoAssistencia().length() == 0) {
				stRetorno += "Informe se o Pedido de Assistência foi Deferido ou não. \n";
			}
		}
		
		List pendenciasGerar = dados.getListaPendenciasGerar();
		if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
			for (int i = 0; i < pendenciasGerar.size(); i++) {
				dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(i);
				//Valida se Carta de Citação, Intimação ou Mandado possuem parte vinculada
				if (Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.CARTA_CITACAO || Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.MANDADO || Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.INTIMACAO) {
					if (dt.getCodDestinatario() == null || dt.getCodDestinatario().length() == 0) {
						stRetorno = "Citação, Intimação ou Mandado sem Destinatário. Efetue a correção das pendências no Passo 2.";
						break;
					}
				}
			}
		}else {
			if(usuarioSessao.isGabineteUpj()) {
				stRetorno = "É obrigatório a geração de, ao menos, uma pendência.";
			}
		}
		
		return stRetorno;

	}

	/**
	 * Verifica os dados obrigatórios em uma análise de pendências. É obrigatório que seja especificado o tipo da movimentação e um arquivo seja inserido.
	 * 
	 * @param dados
	 */
	public String verificarAnalisePendencia(AnalisePendenciaDt dados) {
		String stRetorno = "";
		if (dados.getId_MovimentacaoTipo() == null || dados.getId_MovimentacaoTipo().equalsIgnoreCase("null") || dados.getId_MovimentacaoTipo().length() == 0) stRetorno += "Selecione o Tipo da Movimentação. \n";
		if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) stRetorno += "É necessário inserir um arquivo. \n";
		return stRetorno;

	}

	/**
	 * Verifica os dados obrigatórios em uma pré-análise de conclusões. É obrigatório que seja especificado o tipo da movimentação, tipo do arquivo e o texto da pré-análise.
	 * 
	 * @param dados
	 * @throws Exception 
	 */
	public String verificarPreAnaliseConclusao(AnaliseConclusaoDt dados, String idProcessoAba, UsuarioNe usuarioSessao) throws Exception {
		String stRetorno = "";
		if (dados.getId_MovimentacaoTipo() == null || dados.getId_MovimentacaoTipo().equalsIgnoreCase("null") || dados.getId_MovimentacaoTipo().length() == 0) {
			stRetorno += "Selecione o Tipo da Movimentação. \n";
		} else {
			MovimentacaoTipoDt movimentacaoTipoDt = new MovimentacaoTipoNe().consultarId(dados.getId_MovimentacaoTipo());
			if (movimentacaoTipoDt == null) stRetorno += "O Tipo da Movimentação selecionado não está mais cadastrado, favor selecionar outro. \n";
		}
		if (dados.getId_ArquivoTipo().length() == 0) {
			stRetorno += "Selecione o Tipo do Arquivo. \n";
		} else {
			ArquivoTipoDt arquivoTipoDt = new ArquivoTipoNe().consultarId(dados.getId_ArquivoTipo());
			if (arquivoTipoDt == null) stRetorno += "O Tipo do Arquivo selecionado não está mais cadastrado, favor selecionar outro. \n";
		}
		if (dados.getTextoEditor() == null || dados.getTextoEditor().length() == 0) stRetorno += "É necessário redigir o texto da pré-análise. \n";
		
		if (dados.getId_Classificador() != null && dados.getId_Classificador().trim().length() > 0) {
			ClassificadorDt classificadorDt = new ClassificadorNe().consultarId(dados.getId_Classificador());
			if (classificadorDt == null) stRetorno += "O Classificador selecionado não está mais cadastrado, favor selecionar outro. \n";
		}
		
		if (dados.isVisualizarPedidoAssistencia()) {
			if (dados.getPedidoAssistencia() == null || dados.getPedidoAssistencia().equalsIgnoreCase("null") || dados.getPedidoAssistencia().length() == 0) {
				stRetorno += "Informe se o Pedido de Assistência foi Deferido ou não. \n";
			}
		}
		
		if (dados.getListaArquivos() != null && dados.getListaArquivos().size() > 0) stRetorno += "Para essa ação não é possível inserir arquivos assinados. \n";

		List pendenciasGerar = dados.getListaPendenciasGerar();
		if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
			for (int i = 0; i < pendenciasGerar.size(); i++) {
				dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(i);
				//Valida se Carta de Citação, Intimação ou Mandado possuem parte vinculada
				if (Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.CARTA_CITACAO || Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.MANDADO || Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.INTIMACAO) {
					if (dt.getCodDestinatario() == null || dt.getCodDestinatario().length() == 0) {
						stRetorno = "Citação, Intimação ou Mandado sem Destinatário. Efetue a correção das pendências no Passo 2.";
						break;
					}
				}
			}
		} else {
			if(usuarioSessao.isGabineteUpj()) {
				stRetorno = "É obrigatório a geração de, ao menos, uma pendência.";
			}
		}
		
		if (stRetorno.trim().length() == 0 && !dados.isMultipla() && idProcessoAba != null && idProcessoAba.trim().length() > 0) {
			PendenciaDt pendencia = null;			
			if(dados.getListaPendenciasFechar() != null && dados.getListaPendenciasFechar().size() > 0) {
				pendencia = (PendenciaDt) dados.getListaPendenciasFechar().get(0);
				if (pendencia != null && 
					pendencia.getProcessoDt() != null && 
					pendencia.getProcessoDt().getId() != null && 
					!pendencia.getProcessoDt().getId().trim().equalsIgnoreCase(idProcessoAba.trim())){
					throw new ConflitoDeAbasException();
				}
			}			
		}
		
		return stRetorno;
	}

	/**
	 * Verifica os dados obrigatórios em uma pré-análise de conclusões. É obrigatório que seja especificado o texto da pré-análise.
	 * 
	 * @param dados
	 */
	public String verificarPreAnaliseConclusaoSalvarTextoParcial(AnaliseConclusaoDt dados) {
		String stRetorno = "";

		if (dados.getId_ArquivoTipo().length() == 0) stRetorno += "Selecione o Tipo do Arquivo. \n";

		if (dados.getTextoEditor() == null || dados.getTextoEditor().length() == 0) stRetorno += "É necessário redigir o texto da pré-análise. \n";

		return stRetorno;
	}

	/**
	 * Verifica os dados obrigatórios em uma pré-análise de pendências. É obrigatório que seja especificado o tipo da movimentação, tipo do arquivo e o texto da pré-análise.
	 * 
	 * @param dados
	 * @throws ConflitoDeAbasException 
	 */
	public String verificarPreAnalisePendencia(AnalisePendenciaDt dados, String idProcessoAba) throws ConflitoDeAbasException {
		String stRetorno = "";
		if (dados.getId_MovimentacaoTipo() == null || dados.getId_MovimentacaoTipo().equalsIgnoreCase("null") || dados.getId_MovimentacaoTipo().length() == 0) stRetorno += "Selecione o Tipo da Movimentação. \n";
		if (dados.getId_ArquivoTipo() == null || dados.getId_ArquivoTipo().equalsIgnoreCase("null") || dados.getId_ArquivoTipo().length() == 0) stRetorno += "Selecione o Tipo do Arquivo. \n";
		if (dados.getTextoEditor() == null || dados.getTextoEditor().equalsIgnoreCase("null") || dados.getTextoEditor().length() == 0) stRetorno += "É necessário redigir o texto da pré-análise. \n";
		
		if (stRetorno.trim().length() == 0 && !dados.isMultipla() && idProcessoAba != null && idProcessoAba.trim().length() > 0) {
			PendenciaDt pendencia = null;			
			if(dados.getListaPendenciasFechar() != null && dados.getListaPendenciasFechar().size() > 0) {
				pendencia = (PendenciaDt) dados.getListaPendenciasFechar().get(0);
				if (pendencia != null && 
					pendencia.getProcessoDt() != null && 
					pendencia.getProcessoDt().getId() != null && 
					!pendencia.getProcessoDt().getId().trim().equalsIgnoreCase(idProcessoAba.trim())){
					throw new ConflitoDeAbasException();
				}
			}			
		}
		
		return stRetorno;
	}

	/**
	 * Verifica dados obrigatórios no peticionamento. É obrigatório que seja informado o tipo da moviemntação e ao menos um arquivo seja inserido.
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String verificarPeticionamento(PeticionamentoDt dados) throws Exception{
		String stRetorno = "";
		if (dados.getId_MovimentacaoTipo() == null || dados.getId_MovimentacaoTipo().equalsIgnoreCase("null") || dados.getId_MovimentacaoTipo().length() == 0) 
			stRetorno += "Selecione o Tipo da Movimentação. \n";
		if (!dados.isContemArquivos()) 
			stRetorno += "É necessário inserir um arquivo. \n";
		if (!dados.isContemProcessos()) 
			stRetorno += "É necessário processo(s) para o peticionamento. \n";
		
		if (dados.getListaProcessos() != null && dados.getListaProcessos().size()>0){
			for (Iterator iterator = dados.getListaProcessos().iterator(); iterator.hasNext();) {
				ProcessoDt processoDt = (ProcessoDt) iterator.next();
				if (processoDt != null && processoDt.getDataArquivamento() != null && processoDt.getDataArquivamento().length()>0){
					//if(Funcoes.StringToInt(processoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA){
					if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA))
							|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPC))
							|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPP))
							|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE))
							|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFRACIONAL))
						){
							
						stRetorno += "Não é possível peticionar em processo(s) de carta precatória arquivado. \n";
						stRetorno += "Processo(s): \n";
						stRetorno += processoDt.getProcessoNumero();
					}
					
					MovimentacaoTipoNe movimentacaoTipoNe = new MovimentacaoTipoNe();
					MovimentacaoTipoDt moviTipoDt = movimentacaoTipoNe.consultarId(dados.getId_MovimentacaoTipo());
					if(!moviTipoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.JUNTADA_DE_PETICAO))){
						stRetorno = "Movimentação não permitida para processo ARQUIVADO.\n\n &nbsp;A ÚNICA movimentação Permitida para processo Arquivado é \"JUNTADA DE PETIÇÃO\" \n\n";
						stRetorno += "Processo(s): \n";
						stRetorno += processoDt.getProcessoNumero();
					}
				}if (processoDt.isErroMigracao()){
					stRetorno += "Não é possível peticionar em processo(s) com ERRO DE MIGRAÇÃO\n";
				}
			}
		}
		
		return stRetorno;

	}

	/**
	 * Método que verifica se a movimentação pode ser realizada para o processo passado
	 * 
	 * @param processoDt
	 *            , processo que será movimentado
	 * @param usuarioDt
	 *            , usuário que está movimentando o processo
	 * @param acessoOutraServentia
	 *            , define se deve ser liberada a movimentação para processos de outras serventias
	 * 
	 * @author msapaula
	 */
	public String podeMovimentar(ProcessoDt processoDt, UsuarioDt usuarioDt, String acessoOutraServentia) throws Exception {
		String stMensagem = "";
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();

		//Se o usuário for Responsável pelo processo, ele poderá movimentá-lo
		if (processoResponsavelNe.isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId())) {
			return "";
		}else if(usuarioDt.isAssistenteGabineteComFluxo()){
			//se for assistente de gabinete com fluxo e o processo for do gabinete ele pode movimentar.
			ProcessoNe ne = new ProcessoNe();
			if (!ne.isGabineteUpjResposavelProcesso(usuarioDt.getId_Serventia(), processoDt.getId())) {
				stMensagem += "Sem permissão para movimentar processo de outra serventia. \n";
			}
		} else {
			// Se usuário for de serventia diferente do processo, não poderá movimentar
			if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia()) && acessoOutraServentia.equals("")) {
				stMensagem += "Sem permissão para movimentar processo de outra serventia. \n";
			}
			if (stMensagem.trim().length() == 0 && (usuarioDt.getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU)) || usuarioDt.getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.JUIZ_TURMA)))) {
				if ((new PendenciaNe().verificaConclusoesAbertasProcessoServentiaCargo(processoDt.getId(), usuarioDt.getId_ServentiaCargo(), null))) {
					stMensagem += "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " já está Concluso para " + usuarioDt.getNome() + ". Favor movimentar pela conclusão. \n";
				}
			}
		}

		return stMensagem;
	}

	/**
	 * Método que verifica se podem ser geradas pendências em uma movimentação
	 * 
	 * @param movimentacaoProcessoDt
	 *            , dados da geração de pendências
	 * @param usuarioDt
	 *            , usuário que está gerando pendências
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String podeGerarPendenciasMovimentacao(MovimentacaoProcessoDt movimentacaoProcessoDt, UsuarioDt usuarioDt) throws Exception{
		String stMensagem = "";
		ProcessoDt processoDt = movimentacaoProcessoDt.getPrimeiroProcessoLista();

		if (processoDt != null) {
			if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo(), 0) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
				ProcessoNe ne = new ProcessoNe();
				if (!ne.isGabineteUpjResposavelProcesso(usuarioDt.getId_Serventia(), processoDt.getId())){
					stMensagem += "Sem permissão para gerar Pendências em Processo de outra serventia.";
				}
			} else {
				if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia()) && movimentacaoProcessoDt.getAcessoOutraServentia().equals("")) {
					stMensagem += "Sem permissão para gerar Pendências em Processo de outra serventia.";
				}
			}
		} else {
			stMensagem += "Não foi possível encontrar o processo para gerar pendência(s).";
		}

		if (movimentacaoProcessoDt.getId_UsuarioRealizador().equals(UsuarioServentiaDt.SistemaProjudi)) {
			stMensagem += "Não é possível gerar pendências em movimentação gerada pelo Sistema Projudi.";
		}

		return stMensagem;
	}

	/**
	 * Método responsável em cancelar a movimentação de processo(s). Apaga os id's que tenham sido setados para os objetos
	 * @throws Exception 
	 */
	public void cancelarMovimentacaoGenerica(MovimentacaoProcessoDt movimentacaoDt) throws Exception{
		// Limpa id's dos objetos
		List arquivos = movimentacaoDt.getListaArquivos();
		if (arquivos != null) {
			for (int i = 0; i < arquivos.size(); i++) {
				ArquivoDt dt = (ArquivoDt) arquivos.get(i);
				dt.setId("");
				if (dt.getRecibo().equalsIgnoreCase("true") && dt.isConteudo()) {
					dt.setRecibo("false");
					dt.setArquivo(Signer.extrairP7sRecibo(dt.conteudoBytes()));
				}
			}
		}
	}

	/**
	 * Salva análise de pendências fazendo chamadas para fechar a pendência, gerar movimentação e salvar os arquivos inseridos. Suporta análise múltipla.
	 * 
	 * @author msapaula
	 */
	public void salvarAnalisePendencia(AnalisePendenciaDt analisePendenciaDt, UsuarioDt usuarioDt) throws Exception {
		ProcessoDt processoDt = null;
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		List arquivos = null;
		List movimentacoes = new ArrayList(); // Lista é necessária para gerar recibo
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			LogDt logDt = new LogDt(usuarioDt.getId(), analisePendenciaDt.getIpComputadorLog());

			List pendenciasFechar = analisePendenciaDt.getListaPendenciasFechar(); // Resgata pendências que estão sendo analisadas
			arquivos = analisePendenciaDt.getListaArquivos(); // Resgata arquivos inseridos

			if (arquivos != null) {
				movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
			}

			// Trata cada pendência a ser fechada
			for (int i = 0; i < pendenciasFechar.size(); i++) {
				PendenciaDt objPendencia = (PendenciaDt) pendenciasFechar.get(i);
				// Recupera dados do processo, pois será necessário para gerar pendências
				processoDt = objPendencia.getProcessoDt();
				if (processoDt == null) processoDt = new ProcessoNe().consultarId(objPendencia.getId_Processo());

				// Salvando movimentação do processo
				MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId_Processo(processoDt.getId());
				movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumero());
				movimentacaoDt.setId_MovimentacaoTipo(analisePendenciaDt.getId_MovimentacaoTipo());
				movimentacaoDt.setMovimentacaoTipo(analisePendenciaDt.getMovimentacaoTipo());
				movimentacaoDt.setComplemento(analisePendenciaDt.getComplementoMovimentacao());
				movimentacaoDt.setId_UsuarioRealizador(usuarioDt.getId_UsuarioServentia());
				movimentacaoDt.setId_UsuarioLog(logDt.getId_Usuario());
				movimentacaoDt.setIpComputadorLog(logDt.getIpComputador());

				if (Funcoes.StringToInt(objPendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.PRE_ANALISE_PRECATORIA) {
					movimentacaoDt.setComplemento(movimentacaoDt.getComplemento() + " (Carta Precatória)");
				}

				this.salvar(movimentacaoDt, obFabricaConexao);

				movimentacoes.add(movimentacaoDt);

				if (arquivos != null && arquivos.size() > 0) {
					// Salvando vínculo entre a pendência e arquivos (resolução da pendência)
					PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
					pendenciaArquivoNe.vincularArquivos(objPendencia, arquivos, true, obFabricaConexao);

					String visibilidade=null;
					if (processoDt.isSegredoJustica()){
						visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
					}
					// Salvando vínculo entre a movimentação gerada e arquivos inseridos
					movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(),visibilidade, logDt, obFabricaConexao);
				}

				//Para caso de Relatório deve gerar Revisão, após a análise
				//				if (objPendencia.getPendenciaTipoCodigo().length() > 0 && (Funcoes.StringToInt(objPendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.RELATORIO)) {
				//					pendenciaNe.gerarPendenciaFilhaRelatorio(objPendencia, usuarioDt, obFabricaConexao);
				//				}
				//Se o processo não for sigiloso, gerar o verificar
				if (!processoDt.isSigiloso()) {
    				//Se for advogado, gerar "Verificar Petição" para cartório
    				if (usuarioDt.isAdvogado()) {
    					pendenciaNe.gerarPendenciaVerificarPeticao(processoDt, UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), processoDt.getId_Serventia(), processoDt.getProcessoPrioridadeCodigo(), arquivos, logDt, obFabricaConexao);
    				} else if (usuarioDt.isMp()) {
    					//Para promotor, gera "Verificar Parecer" para cartório
    					pendenciaNe.gerarPendenciaVerificarParecer(processoDt, UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), processoDt.getId_Serventia(), processoDt.getProcessoPrioridadeCodigo(), arquivos, logDt, obFabricaConexao);
    				}
				}
				
				//Se for serventia de plantão, enviará um email aos analistas 
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());
				if (serventiaDt.isPlantaoPrimeiroGrau() || serventiaDt.isPlantaoSegundoGrau()){				
					enviarEmailPeticionamentoAnalistas(serventiaDt,processoDt.getProcessoNumeroCompleto());
				}				
 
				// Fecha pendência com status Cumprida
				objPendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
				objPendencia.setDataFim(df.format(new Date()));
				objPendencia.setDataVisto(df.format(new Date()));
				pendenciaNe.fecharPendencia(objPendencia, usuarioDt, obFabricaConexao);
				
			}

//			// Gera recibo para arquivos de movimentações
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarAnaliseConclusao(analisePendenciaDt);
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Efetua chamada ao método para consultar as pré-analises registradas para uma conclusão
	 * 
	 * @param pendenciaDt
	 *            : pendencia para a qual serão procuradas as pré-análises
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPreAnalisesConclusaoAnteriores(PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception{
		List preAnalises = new ArrayList();
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			preAnalises = neObjeto.consultarPreAnalisesConclusaoAnteriores(pendenciaDt, usuarioSessao);
		
		
		neObjeto = null;
		return preAnalises;
	}

	/**
	 * Efetua chamada ao método para consultar as pré-analises registradas para uma conclusão
	 * 
	 * @param pendenciaDt
	 *            : pendencia para a qual serão procuradas as pré-análises
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPreAnalisesConclusaoFinalizadaAnteriores(PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception{
		List preAnalises = new ArrayList();
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			preAnalises = neObjeto.consultarPreAnalisesConclusaoFinalizadaAnteriores(pendenciaDt, usuarioSessao);
		
		
		neObjeto = null;
		return preAnalises;
	}
	
	/**
	 * Efetua chamada ao método para consultar as pré-analises registradas para uma pendência passada.
	 * 
	 * @param pendenciaDt
	 *            : pendencia para a qual serão procuradas as pré-análises
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPreAnalisesAnteriores(PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception{
		List preAnalises = new ArrayList();
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			preAnalises = neObjeto.consultarPreAnalisesAnteriores(pendenciaDt, usuarioSessao);
		
		
		neObjeto = null;
		return preAnalises;
	}

	public List consultarPreAnalisesConclusoesMultiplas(UsuarioDt usuarioDt, String numeroProcesso) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarPreAnalisesConclusoesMultiplas(usuarioDt, numeroProcesso);
		
		
		neObjeto = null;
		return tempList;
	}

	/**
	 * Consultar dados da movimentação para permitir a geração de pendências
	 * 
	 * @param id_Movimentacao
	 *            , identificação da movimentação
	 * 
	 * @author msapaula
	 */
	public MovimentacaoProcessoDt consultarDadosMovimentacao(String id_Movimentacao) throws Exception {

		MovimentacaoProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarDadosMovimentacao(id_Movimentacao);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consultar movimentações diarias
	 * 
	 * @param data
	 * 
	 * @author acbloureiro
	 */
	public List consultarMovimentacaoDiariaComComplemento(String data) throws Exception {
		
		List <MovimentacaoDt> dtRetorno;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarMovimentacaoDiariaComComplemento(data);
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	
	public void gerarMovimentosComplementos() throws Exception {
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			MovimentacaoProcessoDt movimentacaoProcessoDt;
			
			MovimentacaoComplementoPs movimentacaoComplemento = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
			
			String id_Movimentacao = movimentacaoComplemento.consultarUltimoIdMov();
					
			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			movimentacaoProcessoDt = obPersistencia.consultarDadosMovimentacao(id_Movimentacao);
			
			// pega a data da última movimentação gravada
			String data = movimentacaoProcessoDt.getDataRealizacao();
				
			List <MovimentacaoDt> lista = consultarMovimentacaoDiariaComComplemento(data);
							
			for (MovimentacaoDt movimentacaoDt : lista) {
				MovimentacaoComplementoNe movimentacaoComplementoNe = new MovimentacaoComplementoNe();
				movimentacaoComplementoNe.geraMovimentoComplemento(movimentacaoDt, obFabricaConexao);
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void gerarMovimentosComplementos(MovimentacaoDt movimentacaoDt, AudienciaDt audDt, FabricaConexao obFabricaConexao) throws Exception {
		
		MovimentacaoComplementoNe movimentacaoComplementoNe = new MovimentacaoComplementoNe();
		movimentacaoComplementoNe.geraMovimentoComplementoIndividual(movimentacaoDt, audDt, obFabricaConexao);
		
	}


	public List consultarPendencias(String id_Arquivo) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarPendencias(id_Arquivo);
		
		
		neObjeto = null;
		return tempList;
	}

	public MovimentacaoTipoDt consultaMovimentacaoTipoCodigo(int movimentacaoTipoCodigo) throws Exception{
		MovimentacaoTipoDt dtRetorno = null;
		MovimentacaoTipoNe neObjeto = new MovimentacaoTipoNe();
			dtRetorno = neObjeto.consultaMovimentacaoTipoCodigo(movimentacaoTipoCodigo);
		
		
		neObjeto = null;
		return dtRetorno;
	}

	public PreAnaliseConclusaoDt getPreAnaliseConclusao(PendenciaArquivoDt pendenciaArquivoDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		PreAnaliseConclusaoDt preAnaliseConclusaoDt = null;
			preAnaliseConclusaoDt = arquivoNe.getPreAnaliseConclusao(pendenciaArquivoDt, null);
		
		
		arquivoNe = null;
		return preAnaliseConclusaoDt;
	}
	
	public PreAnaliseConclusaoDt getPreAnaliseConclusaoFinalizada(PendenciaArquivoDt pendenciaArquivoDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		PreAnaliseConclusaoDt preAnaliseConclusaoDt = null;
			preAnaliseConclusaoDt = arquivoNe.getPreAnaliseConclusaoFinalizada(pendenciaArquivoDt, null);
		
		
		arquivoNe = null;
		return preAnaliseConclusaoDt;
	}

	public PreAnalisePendenciaDt getPreAnalisePendencia(PendenciaArquivoDt pendenciaArquivoDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		PreAnalisePendenciaDt preAnalisePendenciaDt = null;
			preAnalisePendenciaDt = arquivoNe.getPreAnalisePendencia(pendenciaArquivoDt);
		
		
		arquivoNe = null;
		return preAnalisePendenciaDt;
	}

	public List consultarPreAnalisesConclusaoFinalizadas(String numeroProcesso, String dataInicial, String dataFinal, UsuarioDt usuarioDt) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarPreAnalisesConclusaoFinalizadas(numeroProcesso, dataInicial, dataFinal, usuarioDt);
		
		
		neObjeto = null;
		return tempList;
	}

	public List consultarPreAnalisesFinalizadas(String numeroProcesso, String dataInicial, String dataFinal, UsuarioDt usuarioDt) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarPreAnalisesFinalizadas(numeroProcesso, dataInicial, dataFinal, usuarioDt);
		
		
		neObjeto = null;
		return tempList;
	}

	public PendenciaArquivoDt getArquivoPreAnaliseConclusao(String id_Pendencia) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			pendenciaArquivoDt = neObjeto.getArquivoPreAnaliseConclusao(id_Pendencia);
		
		
		neObjeto = null;
		return pendenciaArquivoDt;
	}

	public PendenciaArquivoDt getArquivoPreAnalisePendencia(String id_Pendencia) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			pendenciaArquivoDt = neObjeto.getArquivoPreAnalise(id_Pendencia);
		
		
		neObjeto = null;
		return pendenciaArquivoDt;
	}

	public void descartarPreAnalise(List pendencias, UsuarioDt usuarioDt, LogDt logDt) throws Exception{
		if (usuarioDt.isGabinetePresidenciaTjgo() || usuarioDt.isGabineteVicePresidenciaTjgo() || usuarioDt.isGabineteUPJ()){
			this.descartarPreAnaliseGabineteFluxo(pendencias, logDt);
		} else {
			PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			neObjeto.descartarPreAnalise(pendencias, usuarioDt, logDt);
			neObjeto = null;
		}
	}

	public void descartarAnalise(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception{
		PendenciaNe neObjeto = new PendenciaNe();
		neObjeto.descartarPendencia(pendenciaDt, usuarioDt);
		
		neObjeto = null;
	}
	
	/**
	 * Metodo para desvincular arquivo de pré-analise de uma pendência que será retirada da pré-anlise multipa nos gabientes com fluxo (presidencia, vice-presidencia e upj)
	 * 
	 * @author lsbernardes
	 * @param lista com objetos pendenciaDt vo de pendencia
	 * @param LogDt vo de logDt
	 * @throws Exception
	 */
	public void descartarPreAnaliseGabineteFluxo(List pendencias, LogDt logDt) throws Exception{
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		neObjeto.desvincularArquivosPreAnalise(pendencias, logDt, null);
		neObjeto = null;
	}
	
	public List consultarConclusoesPendentes(UsuarioNe usuarioSessao, String idServentia, String filtroNumeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada, boolean isVirtual) throws Exception{
		List tempList = null;
		PendenciaNe neObjeto = new PendenciaNe();
		//lrcampos 28/10/2019 11:51 - Condição para caso for virtual chamar método específico
		if(isVirtual)
			tempList = neObjeto.consultarConclusoesPendentesVirtual(usuarioSessao, idServentia, filtroNumeroProcesso, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada);
		else
			tempList = neObjeto.consultarConclusoesPendentesProjudi(usuarioSessao, filtroNumeroProcesso, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada);
		
		neObjeto = null;
		return tempList;
	}
	
	public List consultarConclusoesPendentes(UsuarioNe usuarioSessao, String filtroNumeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception{
		List tempList = null;
		PendenciaNe neObjeto = new PendenciaNe();
		tempList = neObjeto.consultarConclusoesPendentesProjudi(usuarioSessao, filtroNumeroProcesso, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada);
		neObjeto = null;
		return tempList;
	}
	public List consultarConclusoesPendentes(UsuarioNe usuarioSessao, String filtroNumeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido) throws Exception{
		return consultarConclusoesPendentes(usuarioSessao, filtroNumeroProcesso, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, false);
	}

	public List consultarConclusoesPendentesAssistenteGabinete(UsuarioNe usuarioSessao, String filtroNumeroProcesso, String id_Classificador, String id_PendenciaTipo, String id_ServentiaGrupo, boolean ehVoto, boolean ehVotoVencido) throws Exception{
		List tempList = null;
		PendenciaNe neObjeto = new PendenciaNe();
		tempList = neObjeto.consultarConclusoesPendentesAssistenteGabinete(usuarioSessao, filtroNumeroProcesso, id_Classificador, id_PendenciaTipo, id_ServentiaGrupo, ehVoto, ehVotoVencido);
		neObjeto = null;
		return tempList;
	}

	public List consultarPendenciasNaoAnalisadas(UsuarioDt usuarioDt, String filtroNumeroProcesso, String id_PendenciaTipo) throws Exception{
		List tempList = null;
		PendenciaNe neObjeto = new PendenciaNe();
			tempList = neObjeto.consultarPendenciasNaoAnalisadas(usuarioDt, filtroNumeroProcesso, id_PendenciaTipo);
		
		
		neObjeto = null;
		return tempList;
	}

	public List consultarConclusoesFinalizadas(String id_ServentiaCargo, String numeroProcesso, String dataInicial, String dataFinal) throws Exception{
		List tempList = null;
		PendenciaNe neObjeto = new PendenciaNe();
			tempList = neObjeto.consultarConclusoesFinalizadas(id_ServentiaCargo, numeroProcesso, dataInicial, dataFinal);
		
		
		neObjeto = null;
		return tempList;
	}

	public List consultarPendenciasAnalisadas(UsuarioDt usuarioDt, String numeroProcesso, String dataInicial, String dataFinal) throws Exception{
		List tempList = null;
		PendenciaNe neObjeto = new PendenciaNe();
			tempList = neObjeto.consultarPendenciasAnalisadas(usuarioDt, numeroProcesso, dataInicial, dataFinal);
		
		
		neObjeto = null;
		return tempList;
	}

	/**
	 * Efetua chamada a PendenciaNe para efetuar a consulta
	 * @throws Exception 
	 */
	public List consultarArquivosRespostaConclusao(String id_Pendencia, UsuarioNe usuarioNe) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarArquivosRespostaConclusao(id_Pendencia, usuarioNe);
		
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Efetua chamada a PendenciaNe para efetuar a consulta
	 * @author jrcorrea 
	 * 26/08/2014
	 * @throws Exception 
	 */
	public List consultarArquivosRespostaConclusaoFinalizada(String id_Pendencia, UsuarioNe usuarioNe) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarArquivosRespostaConclusaoFinalizada(id_Pendencia, usuarioNe);
		
		
		neObjeto = null;
		return tempList;
	}

	/**
	 * Efetua chamada a PendenciaNe para efetuar a consulta
	 * @throws Exception 
	 */
	public List consultarArquivosRespostaPendencia(String id_Pendencia, UsuarioNe usuarioNe) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarArquivosRespostaPendencia(id_Pendencia, usuarioNe);
		
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Efetua chamada a PendenciaNe para efetuar a consulta
	 * @throws Exception 
	 */
	public List consultarArquivosRespostaPendenciaFinalizada(String id_Pendencia, UsuarioNe usuarioNe) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarArquivosRespostaPendenciaFinalizada(id_Pendencia, usuarioNe);
		
		
		neObjeto = null;
		return tempList;
	}

	public List consultarGrupoArquivoTipo(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		tempList = neObjeto.consultarGrupoArquivoTipo(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	public List consultarGrupoArquivoTipo(UsuarioDt usuarioDt, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		tempList = neObjeto.consultarGrupoArquivoTipo(usuarioDt, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	public List consultarGrupoMovimentacaoTipo(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		MovimentacaoTipoNe neObjeto = new MovimentacaoTipoNe();
		
		tempList = neObjeto.consultarGrupoMovimentacaoTipo(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	public List consultarGrupoMovimentacaoTipo(UsuarioDt ususarioDt, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		MovimentacaoTipoNe neObjeto = new MovimentacaoTipoNe();
		
		tempList = neObjeto.consultarGrupoMovimentacaoTipo(ususarioDt, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	public List consultarModelo(UsuarioDt usuarioDt, String id_ArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ModeloNe neObjeto = new ModeloNe();
		
		tempList = neObjeto.consultarModelos(tempNomeBusca, posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	public ModeloDt consultarModeloId(String id_Modelo, ProcessoDt processodt, UsuarioDt usuarioDt) throws Exception {
		ModeloDt modeloDt = null;
		ModeloNe modeloNe = new ModeloNe();
		
		modeloDt = modeloNe.consultarId(id_Modelo);
		modeloDt.setTexto(modeloNe.montaConteudo(id_Modelo, processodt, usuarioDt, ""));
		
		modeloNe = null;
		return modeloDt;
	}

	public ProcessoDt consultarProcessoIdCompleto(String id_Processo) throws Exception {
		ProcessoDt processodt = null;
		ProcessoNe processoNe = new ProcessoNe();
			processodt = processoNe.consultarIdCompleto(id_Processo);
		
		
		processoNe = null;
		return processodt;
	}

	public ProcessoDt consultarProcessoId(String id_Processo) throws Exception {
		ProcessoDt processodt = null;
		ProcessoNe processoNe = new ProcessoNe();
			processodt = processoNe.consultarId(id_Processo);
		
		
		processoNe = null;
		return processodt;
	}

	public PendenciaDt consultarPendenciaId(String id_Pendencia) throws Exception {
		PendenciaDt pendenciaDt = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
			pendenciaDt = pendenciaNe.consultarId(id_Pendencia);
		
		
		pendenciaNe = null;
		return pendenciaDt;
	}
	
	public PendenciaDt consultarFinalizadaId(String id_Pendencia) throws Exception {
		PendenciaDt pendenciaDt = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
			pendenciaDt = pendenciaNe.consultarFinalizadaId(id_Pendencia);
		
		
		pendenciaNe = null;
		return pendenciaDt;
	}

	public ArquivoDt consultarArquivoId(String id_Arquivo) throws Exception {
		ArquivoDt arquivoDt = null;
		ArquivoNe arquivoNe = new ArquivoNe();
			arquivoDt = arquivoNe.consultarId(id_Arquivo);
		
		
		arquivoNe = null;
		return arquivoDt;
	}

	public AnaliseConclusaoDt getPreAnaliseConclusao(String id_Pendencia) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		AnaliseConclusaoDt preAnaliseConclusaoDt = null;
		preAnaliseConclusaoDt = arquivoNe.getPreAnaliseConclusao(id_Pendencia);
		arquivoNe = null;
		return preAnaliseConclusaoDt;
	}

	public AnaliseConclusaoDt getPreAnaliseConclusao(String id_Pendencia, FabricaConexao obFabricaConexao) throws Exception {
		return (new PendenciaArquivoNe()).getPreAnaliseConclusao(id_Pendencia, obFabricaConexao); // jvosantos - 02/09/2019 15:04 - Tratamento de Exception
	}
	
	public AnalisePendenciaDt getPreAnalisePendencia(String id_Pendencia) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		AnalisePendenciaDt preAnalisePendenciaDt = null;
		preAnalisePendenciaDt = arquivoNe.getPreAnalisePendencia(id_Pendencia);
		arquivoNe = null;
		return preAnalisePendenciaDt;
	}

	public String consultarServentiaCargo(String id_UsuarioServentiaChefe) throws Exception {
		String stRetorno = null;
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
			stRetorno = serventiaCargoNe.consultarServentiaCargo(id_UsuarioServentiaChefe);
		
		
		serventiaCargoNe = null;
		return stRetorno;
	}

	public List consultarPreAnalisesConclusaoSimplesVirtual(UsuarioNe usuarioSessao, String idServentia, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean EhVoto, boolean EhVotoVencido, boolean isIniciada) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesConclusaoSimplesVirtual(usuarioSessao, idServentia, numeroProcesso, id_Classificador, id_PendenciaTipo, EhVoto, EhVotoVencido, isIniciada);
		neObjeto = null;
		return tempList;
	}
	

	public List consultarPreAnalisesAguardandoInicioVirtual(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean EhVoto, boolean EhVotoVencido) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesAguardandoInicioVirtual(usuarioSessao, numeroProcesso, id_Classificador, id_PendenciaTipo, EhVoto, EhVotoVencido);
		neObjeto = null;
		return tempList;
	}
	
	public List consultarPreAnalisesConclusaoSimples(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean EhVoto, boolean EhVotoVencido) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesConclusaoSimples(usuarioSessao, numeroProcesso, id_Classificador, id_PendenciaTipo, EhVoto, EhVotoVencido);
		neObjeto = null;
		return tempList;
	}
	
	public List consultarPreAnalisesConclusaoSimplesPJD(UsuarioNe usuarioSessao, String idServentia, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean EhVoto, boolean EhVotoVencido) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesConclusaoSimplesPJD(usuarioSessao, idServentia, numeroProcesso, id_Classificador, id_PendenciaTipo, EhVoto, EhVotoVencido);
		neObjeto = null;
		return tempList;
	}
	
	public List consultarPreAnalisesConclusaoSimplesAssistenteGabinete(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, String id_ServentiaGrupo, boolean EhVoto, boolean EhVotoVencido) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
			tempList = neObjeto.consultarPreAnalisesConclusaoSimplesAssistenteGabinete(usuarioSessao, numeroProcesso, id_Classificador, id_PendenciaTipo, id_ServentiaGrupo, EhVoto, EhVotoVencido);
		
		
		neObjeto = null;
		return tempList;
	}

	public List consultarPreAnalisesSimples(UsuarioDt usuarioDt, String numeroProcesso, String id_PendenciaTipo) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesSimples(usuarioDt, numeroProcesso, id_PendenciaTipo);
				
		neObjeto = null;
		return tempList;
	}

	public List consultarPreAnalisesMultiplas(UsuarioDt usuarioDt, String numeroProcesso) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesMultiplas(usuarioDt, numeroProcesso);
				
		neObjeto = null;
		return tempList;
	}

	public List consultarClassificador(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		List tempList = null;
		ClassificadorNe neClassificador = new ClassificadorNe();
		
		tempList = neClassificador.consultarClassificadorServentia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = neClassificador.getQuantidadePaginas();
		
		neClassificador = null;
		return tempList;

	}

	public List consultarTiposPendenciaMovimentacao(UsuarioDt usuarioDt) throws Exception {
		List tempList = null;
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			tempList = pendenciaTipoNe.consultarGrupoPendenciaTipo("", usuarioDt, "0", false);
		
		
		return tempList;
	}

	public void consultaResponsavelPreAnalise(PendenciaArquivoDt pendenciaArquivoDt) throws Exception {
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			pendenciaArquivoNe.consultaResponsavelPreAnalise(pendenciaArquivoDt);
		
		
		pendenciaArquivoNe = null;
	}
	/**
	 * @author jrcorrea
	 * @param 
	 * @param id_pendenci
	 * @return list dos resmposaveis
	 * @throws Exception
	 * 26/08/2014
	 */
	public List consultaResponsavelPreAnaliseFinalizada(String id_pendencia) throws Exception {
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		List tempList = null;
			tempList = pendenciaArquivoNe.consultaResponsavelPreAnaliseFinalizada(id_pendencia);
		
		
		return  tempList;
	}

	public List consultarFilhasPendencia(PendenciaDt pendencia) throws Exception {
		List tempList = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
			tempList = pendenciaNe.consultarFilhas(pendencia);
		
		
		pendenciaNe = null;
		return tempList;
	}

	public List consultarPendenciasFilhas(PendenciaDt pendencia, UsuarioNe usuarioNe) throws Exception {
		List tempList = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		tempList = pendenciaNe.consultarFilhasAutosArquivosRecursivas(pendencia, usuarioNe);
		pendenciaNe = null;
		return tempList;
	}
	
    /**
     * Consultar as pendencias filhas finalizadas que sao autos de uma pendencia
     * recursivamente adicionando a lista de responsaveis
     * 
     * @author Jesus Rodrigo
     * @since 25/08/2014
     * @param pendenciaDt  vo de pendencia
     * @return lista de pendencias filhas que sao autos com os arquivos
     * @throws Exception
     */
	
	public List consultarPendenciasFinalizadasFilhas(PendenciaDt pendencia, UsuarioNe usuarioNe) throws Exception {
		List tempList = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		tempList = pendenciaNe.consultarFilhasFinalizadasAutosArquivosRecursivas(pendencia, usuarioNe);
		pendenciaNe = null;
		return tempList;
	}

	public List getPendenciasRelacionadas(String codPendenciaTipo, String id_Serventia) throws Exception {
		List tempList = null;
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		tempList = pendenciaTipoNe.getPendenciasRelacionadas(codPendenciaTipo, id_Serventia);
		pendenciaTipoNe = null;
		return tempList;
	}

	public List consultarSessoesAbertas(String id_Serventia, String grupoTipoCodigo, boolean ordemDataInversa) throws Exception {
		List tempList = null;
		AudienciaNe audienciaNe = new AudienciaNe();
		tempList = audienciaNe.consultarSessoesAbertas(id_Serventia, grupoTipoCodigo, ordemDataInversa);
				
		audienciaNe = null;
		return tempList;
	}
	
	public List consultarSessoesVirtuaisAbertas(String id_Serventia, String grupoTipoCodigo, boolean ordemDataInversa) throws Exception {
		return consultarSessoesVirtuaisAbertas(id_Serventia, grupoTipoCodigo, ordemDataInversa, false);
	}
	
	public List consultarSessoesVirtuaisAbertas(String id_Serventia, String grupoTipoCodigo, boolean ordemDataInversa, boolean isMarcarSessao) throws Exception {
		List tempList = null;
		AudienciaNe audienciaNe = new AudienciaNe();
		tempList = audienciaNe.consultarSessoesVirtuaisAbertas(id_Serventia, grupoTipoCodigo, ordemDataInversa, isMarcarSessao);
				
		audienciaNe = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta de um recurso
	 * @throws Exception 
	 */
	public RecursoDt consultarRecursoId(String id_Recurso) throws Exception{
		RecursoDt recursoDt = null;
		RecursoNe recursoNe = new RecursoNe();
		recursoDt = recursoNe.consultarId(id_Recurso);
			
		recursoNe = null;
		return recursoDt;
	}

	/**
	 * Verifica se um processo possui uma pendencia de peticionamento em andamento após uma determinada data
	 * 
	 * @param usuarioSessao
	 * @param numero_processo
	 * @param dataHoraInicio
	 * @return
	 * @throws Exception
	 */
	public boolean existePeticionamentoPendente(UsuarioNe usuarioNe, String numero_processo, String dataHoraInicio, String Id_Serventia) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();

		List lista = pendenciaNe.consultarPeticoesEmAndamento(usuarioNe, numero_processo, dataHoraInicio, Id_Serventia);

		return (lista.size() > 0);
	}
	
	/**
	 * Verifica se um processo possui uma pendencia de peticionamento em andamento após uma determinada data
	 * 
	 * @param usuarioSessao
	 * @param numero_processo
	 * @param dataHoraInicio
	 * @return
	 * @throws Exception
	 */
	public boolean existePeticionamentoPendente(UsuarioNe usuarioNe, String numero_processo, String dataHoraInicio, String Id_Serventia, FabricaConexao obFabricaConexao) throws Exception{

			PendenciaNe pendenciaNe = new PendenciaNe();
			List lista = pendenciaNe.consultarPeticoesEmAndamentoPJD(usuarioNe, numero_processo, dataHoraInicio, Id_Serventia, obFabricaConexao);
			
			return (lista.size() > 0);
	}
	
	/**
	 * Verifica se o processo possui Guia em aberto
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 */
	public boolean verificarGuiasPendentesProcesso(String idProcesso) throws Exception {
		return new GuiaEmissaoNe().verificarGuiasPendentesProcesso(idProcesso);
	}

	/**
	 * Consulta as pré análises pendentes de assinatura do tipo conclusão
	 * 
	 * @param usuarioDt
	 * @param numeroProcesso
	 * @param id_Classificador
	 * @param id_PendenciaTipo
	 * @return
	 * @throws Exception
	 */
	public List consultarPreAnalisesConclusaoSimplesPendentesAssinatura(UsuarioDt usuarioDt, String numeroProcesso, String id_Classificador, String id_PendenciaTipo) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesConclusaoSimplesPendentesAssinatura(usuarioDt, numeroProcesso, id_Classificador, id_PendenciaTipo);		
		
		neObjeto = null;
		return tempList;
	}

	public void criarPendenciaDesembargador(PendenciaDt pendenciaDesembargadorDt, String pendenciaTipoCodigo, UsuarioDt usuarioDt) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		String idAudienciaProcessoSessaoSegundoGrau = null;
		String idServentiaCargoRelatorSessao = null;
		if (pendenciaDesembargadorDt != null) {
			AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeVoto(pendenciaDesembargadorDt.getId());
			if (audienciaProcessoDt != null) {
				idAudienciaProcessoSessaoSegundoGrau = audienciaProcessoDt.getId();
				idServentiaCargoRelatorSessao = audienciaProcessoDt.getId_ServentiaCargo();
			} else {
				audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeEmenta(pendenciaDesembargadorDt.getId());
				if (audienciaProcessoDt != null) {
					idAudienciaProcessoSessaoSegundoGrau = audienciaProcessoDt.getId();
					idServentiaCargoRelatorSessao = audienciaProcessoDt.getId_ServentiaCargo();
				} else {
					audienciaProcessoDt = audienciaProcessoNe.consultarAudienciaPendenteProcesso(pendenciaDesembargadorDt.getId_Processo(), null, null);
					if (audienciaProcessoDt != null)  {
						idAudienciaProcessoSessaoSegundoGrau = audienciaProcessoDt.getId();
						idServentiaCargoRelatorSessao = audienciaProcessoDt.getId_ServentiaCargo();
					}
				}				
			}
		}
		
		pendenciaNe.criarPendenciaDesembargador(pendenciaDesembargadorDt, pendenciaTipoCodigo, usuarioDt, idAudienciaProcessoSessaoSegundoGrau, idServentiaCargoRelatorSessao);
	}

	/**
	 * Descarta o status 'Aguardando assinatura' das pendências do tipo conclusão
	 * 
	 * @param assinarConclusaoDt
	 * @param usuarioDt
	 * @throws Exception
	 * 
	 * @author mmgomes
	 */
	public void descarteStatusPreAnalisesConclusaoAguardandoAssinatura(AssinarConclusaoDt assinarConclusaoDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		arquivoNe.alterarStatusPendenciaArquivo(assinarConclusaoDt.getListaPendenciaArquivo(), String.valueOf(PendenciaArquivoDt.NORMAL));
				
		arquivoNe = null;
	}

	public List consultarPreAnalisesSimplesPendentesAssinatura(UsuarioDt usuarioDt, String numeroProcesso, String id_PendenciaTipo) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		tempList = neObjeto.consultarPreAnalisesSimplesPendentesAssinatura(usuarioDt, numeroProcesso, id_PendenciaTipo);
		
		neObjeto = null;
		return tempList;
	}

	/**
	 * Descarta o status 'Aguardando assinatura' das pendências do tipo simples
	 * 
	 * @param assinarConclusaoDt
	 * @param usuarioDt
	 * @throws Exception
	 * 
	 * @author mmgomes
	 */
	public void descarteStatusPreAnalisesAguardandoAssinatura(AssinarPendenciaDt assinarPendenciaDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
			arquivoNe.alterarStatusPendenciaArquivo(assinarPendenciaDt.getListaPendenciaArquivo(), String.valueOf(PendenciaArquivoDt.NORMAL));
		
		
		arquivoNe = null;
	}

	/**
	 * Gera movimentacao de Audiência Publicada (Video)
	 * 
	 * @author mmgomes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoAudienciaPublicada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.AUDIENCIA_PUBLICADA, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Mídia Publicada (Video/Audio)
	 * 
	 * @author mmgomes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoMidiaPublicada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.MIDIA_PUBLICADA, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Consulta o idMovimentacao
	 * 
	 * @param movimentacaoTipoCodigo
	 *            : tipo da movimentação
	 * @param tcoNumero
	 *            : número do Execpen
	 * @author wcsilva
	 */
	public String consultarIdMovimentacao(String movimentacaoTipoCodigo, String procNumero, String digito, String ano, String tcoNumero) throws Exception {
		String idMovimentacao = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			idMovimentacao = obPersistencia.consultarIdMovimentacao(movimentacaoTipoCodigo, procNumero, digito, ano, tcoNumero);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return idMovimentacao;
	}

	/**
	 * Método para consultar Interlocutórias do advogado no processo.
	 * 
	 * @param String
	 *            id_processo
	 * @return int
	 * @throws Exception
	 */
	public int consultarInterlocutoriasAdvogado(String id_processo) throws Exception {
		int listaMovimentacaoQuantidade = 0;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());

			if (id_processo != null) {
				listaMovimentacaoQuantidade = obPersistencia.consultarInterlocutoriasAdvogado(id_processo);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return listaMovimentacaoQuantidade;
	}

	/**
	 * Método para consultar Interlocutórias do advogado no processo + PETIÇÃO INICIAL
	 * 
	 * @param String
	 *            id_processo
	 * @return int
	 * @throws Exception
	 */
	public int consultarInterlocutoriasAdvogadoCompleto(String id_processo) throws Exception {
		int listaMovimentacaoQuantidade = 0;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());

			if (id_processo != null) {
				listaMovimentacaoQuantidade = obPersistencia.consultarInterlocutoriasAdvogadoCompleto(id_processo);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return listaMovimentacaoQuantidade;
	}

	/**
	 * Método para verificar se um processo possui um tipo de movimentação específica.
	 * 
	 * @param String
	 *            id_processo
	 * @param String
	 *            movimentacaoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean possuiMovimentacaoTipo(String id_processo, String movimentacaoTipoCodigo) throws Exception {
		boolean retorno = false;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());

			if (id_processo != null && movimentacaoTipoCodigo != null) {
				retorno = obPersistencia.possuiMovimentacaoTipo(id_processo, movimentacaoTipoCodigo);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}

	/**
	 * Gera movimentacao de Mudanca de Classe no Processo
	 * 
	 * @author mmgomes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoMudancaClasseProcesso(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.MUDANCA_CLASSE_PROCESSO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Mudanca de Assunto no Processo
	 * 
	 * @author acbloureiro
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author acbloureiro
	 */
	public MovimentacaoDt gerarMovimentacaoMudancaAssuntoProcesso(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.MUDANCA_ASSUNTO_PROCESSO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Mudanca de Classe no Processo
	 * 
	 * @author jrcorea
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 */
	public MovimentacaoDt gerarMovimentacaoRecursoConvertidoProcesso(String id_processo, String id_UsuarioServentia, String ipComputadorLog, String id_usuarioLog, FabricaConexao obFabricaConexao) throws Exception {
		return gerarMovimentacao(id_processo, MovimentacaoTipoDt.RECURSO_CONVERTIDO_EM_PROCESSO, id_UsuarioServentia, "", ipComputadorLog, id_usuarioLog, obFabricaConexao);
	}

	/**
	 * Gera movimentacao de converção de processo em Recurso
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoConvertidoRecurso(String id_processo, String id_UsuarioServentia, String ipComputadorLog, String id_usuarioLog, FabricaConexao obFabricaConexao) throws Exception {
		return gerarMovimentacao(id_processo, MovimentacaoTipoDt.PROCESSO_CONVERTIDO_EM_RECURSO, id_UsuarioServentia, "", ipComputadorLog, id_usuarioLog, obFabricaConexao);
	}

	/**
	 * Obtem a lista de Tipos de Movimentação configuradas para o usuário e grupo do usuário logado.
	 * 
	 * @author mmgomes
	 * @param usuarioDt
	 * @return
	 * @throws Exception
	 */
	public List consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioDt usuarioDt) throws Exception {
		UsuarioMovimentacaoTipoNe usuarioMovimentacaoTipoNe = new UsuarioMovimentacaoTipoNe();
		try{
			return usuarioMovimentacaoTipoNe.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(usuarioDt);
		
		} finally{
			usuarioMovimentacaoTipoNe = null;
		}
	}

	/**
	 * Gera movimentacao de Sessão de Julgamento Iniciada
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoJulgamentoIniciado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_JULGAMENTO_INICIADO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Sessão de Julgamento Adiada
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoJulgamentoAdiada(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_JULGAMENTO_ADIADO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Sessão de Julgamento Mantido Adiado
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	
	public MovimentacaoDt gerarMovimentacaoSessaoMantidoJulgamentoAdiado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_JULGAMENTO_MANTIDO_ADIADO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de marcação de sessão extra pauta (Em Mesa para julgamento)
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoEmMesaParaJulgamento(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_EM_MESA_JULGAMENTO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Verifica os dados obrigatórios em uma pré-análise de conclusões do tipo Voto / Ementa. É obrigatório que seja especificado o tipo do arquivo e o texto da pré-análise.
	 * 
	 * @param dados
	 * @author mmgomes
	 */
	public String verificarPreAnaliseVotoEmenta(AnaliseConclusaoDt dados) {
		String stRetorno = "";
		if (dados.getId_ArquivoTipo().length() == 0) stRetorno += "Selecione o Tipo do Arquivo Relatório e Voto. \n";
		//if (dados.getTextoEditor() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditor())) stRetorno += "É necessário redigir o texto da pré-análise do Relatório e Voto. \n";

		if (dados.getId_ArquivoTipoEmenta().length() == 0) stRetorno += "Selecione o Tipo do Arquivo Ementa. \n";
		if (dados.getTextoEditorEmenta() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditorEmenta())) stRetorno += "É necessário redigir o texto da pré-análise da Ementa. \n";

		List pendenciasGerar = dados.getListaPendenciasGerar();
		if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
			for (int i = 0; i < pendenciasGerar.size(); i++) {
				dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(i);
				//Valida se Carta de Citação, Intimação ou Mandado possuem parte vinculada
				if (Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.CARTA_CITACAO || Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.MANDADO || Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.INTIMACAO) {
					if (dt.getCodDestinatario() == null || dt.getCodDestinatario().length() == 0) {
						stRetorno = "Citação, Intimação ou Mandado sem Destinatário. Efetue a correção das pendências no Passo 2.";
						break;
					}
				}
			}
		}
		return stRetorno;
	}

	/**
	 * Consulta um arquivo tipo Voto de sessão.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public ArquivoTipoDt consultarArquivoTipoVotoSessao() throws Exception{
			return consultarArquivoTipoCodigo(ArquivoTipoDt.RELATORIO_VOTO);
		
		
	}

	/**
	 * Consulta um arquivo tipo Ementa de sessão.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public ArquivoTipoDt consultarArquivoTipoEmentaSessao() throws Exception{
			return consultarArquivoTipoCodigo(ArquivoTipoDt.EMENTA);
		
		
	}

	/**
	 * Consulta um arquivo tipo
	 * 
	 * @param arquivoTipoCodigo
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	private ArquivoTipoDt consultarArquivoTipoCodigo(int arquivoTipoCodigo) throws Exception{
		ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();

		String id = "";
		ArquivoTipoDt arquivoTipoDt = null;

		List ids = arquivoTipoNe.consultarPeloArquivoTipoCodigo(String.valueOf(arquivoTipoCodigo));
		if (ids != null && ids.size() > 0) id = (String) ids.get(0);

		if (id != "") arquivoTipoDt = arquivoTipoNe.consultarId(id);

		arquivoTipoNe = null;

		return arquivoTipoDt;

	}

	/**
	 * Método responsável por consultar a pendência do tipo ementa em um processo para um serventia cargo
	 * 
	 * @param String
	 *            id_ServentiaCargo
	 * @param String
	 *            id_Processo
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public PendenciaArquivoDt consultarEmentaDesembargador(String id_ServentiaCargo, String id_Processo, String id_ProcessoTipoSessao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarSessaoProcesso(id_Processo, id_ProcessoTipoSessao);
		pendenciaArquivoDt = pendenciaArquivoNe.consultarEmentaDesembargador(id_ServentiaCargo, audienciaProcessoDt, id_ProcessoTipoSessao);
				
		pendenciaArquivoNe = null;
		return pendenciaArquivoDt;
	}
	
	public void salvarPreAnaliseVotoEmentaSessaoPresencial(AnaliseConclusaoDt preAnaliseConclusaoDt, AudienciaProcessoDt audienciaProcessoDt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();	
		
			salvarPreAnaliseVotoEmentaSessaoPresencial(preAnaliseConclusaoDt, audienciaProcessoDt, usuarioDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void salvarPreAnaliseVotoEmentaSessaoPresencial(AnaliseConclusaoDt preAnaliseConclusaoDt, AudienciaProcessoDt audienciaProcessoDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
		// Voto
		String id_PendenciaVoto = "";
		if (preAnaliseConclusaoDt.getArquivoPreAnalise() != null) {
			id_PendenciaVoto = preAnaliseConclusaoDt.getArquivoPreAnalise().getId_Pendencia();
		} else if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null &&
				   preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0) {
			PendenciaDt pendenciaVoto = (PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0);
			if (pendenciaVoto != null && pendenciaVoto.getId() != null && pendenciaVoto.getId().trim().length() > 0) {
				id_PendenciaVoto = pendenciaVoto.getId();
			}
		}
		String textoEditorVoto = preAnaliseConclusaoDt.getTextoEditor();
		String nomeArquivoVoto = preAnaliseConclusaoDt.getNomeArquivo();
		String id_ArquivoTipoVoto = preAnaliseConclusaoDt.getId_ArquivoTipo();
		String arquivoTipoVoto = preAnaliseConclusaoDt.getArquivoTipo();
		
		String julgadoMeritoProcessoPrincipalVoto = preAnaliseConclusaoDt.getJulgadoMeritoProcessoPrincipal();
		List listaPendenciasGerarVoto = preAnaliseConclusaoDt.getListaPendenciasGerar();
		String id_ClassificadorVoto = preAnaliseConclusaoDt.getId_Classificador();
		boolean pendenteAssinatura = preAnaliseConclusaoDt.isPendenteAssinatura();
		
		// Ementa
		String id_PendenciaEmenta = "";
		if (preAnaliseConclusaoDt.getArquivoPreAnaliseEmenta() != null){
			id_PendenciaEmenta = preAnaliseConclusaoDt.getArquivoPreAnaliseEmenta().getId_Pendencia();
		}
		String textoEditorEmenta = preAnaliseConclusaoDt.getTextoEditorEmenta();
		String nomeArquivoEmentaEditor = preAnaliseConclusaoDt.getNomeArquivoEmenta();
		String id_ArquivoTipoEmenta = preAnaliseConclusaoDt.getId_ArquivoTipoEmenta();
		String arquivoTipoEmenta = preAnaliseConclusaoDt.getArquivoTipoEmenta();
		
		AudienciaNe audienciaNe = new AudienciaNe();
		
		String id_pendenciaEmentaGerada = audienciaNe.salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauPreAnaliseDesembargadorAssistente(audienciaProcessoDt, 
																							              id_PendenciaVoto, 
																							              textoEditorVoto, 
																							              nomeArquivoVoto, 
																							              id_ArquivoTipoVoto,
																							              arquivoTipoVoto, 
																							              julgadoMeritoProcessoPrincipalVoto, 
																							              listaPendenciasGerarVoto, 
																							              id_ClassificadorVoto,
																							              pendenteAssinatura, 
																							              id_PendenciaEmenta, 
																							              textoEditorEmenta, 
																							              nomeArquivoEmentaEditor,
																							              id_ArquivoTipoEmenta, 
																							              arquivoTipoEmenta, 
																							              usuarioDt, 
																							              obFabricaConexao);
		
		preAnaliseConclusaoDt.setId_PendenciaEmentaGerada(id_pendenciaEmentaGerada);
		// é relator...
		if (Funcoes.StringToLong(id_pendenciaEmentaGerada) == Funcoes.StringToLong(audienciaProcessoDt.getId_PendenciaEmentaRelator())) {
			preAnaliseConclusaoDt.setId_PendenciaVotoGerada(audienciaProcessoDt.getId_PendenciaVotoRelator());
			preAnaliseConclusaoDt.setId_ServentiaCargoVotoEmentaGerada(audienciaProcessoDt.getId_ServentiaCargo());
		} else {
			preAnaliseConclusaoDt.setId_PendenciaVotoGerada(audienciaProcessoDt.getId_PendenciaVotoRedator());
			preAnaliseConclusaoDt.setId_ServentiaCargoVotoEmentaGerada(audienciaProcessoDt.getId_ServentiaCargoRedator());
		}
	}

	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt) throws Exception {
		String id_PendenciaVoto = "";
		if (preAnaliseConclusaoDt.getArquivoPreAnalise() != null) {
			id_PendenciaVoto = preAnaliseConclusaoDt.getArquivoPreAnalise().getId_Pendencia();
		} else if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null &&
				   preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0) {
			PendenciaDt pendenciaVoto = (PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0);
			if (pendenciaVoto != null && pendenciaVoto.getId() != null && pendenciaVoto.getId().trim().length() > 0) {
				id_PendenciaVoto = pendenciaVoto.getId();
			}
		}
		
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeVoto(id_PendenciaVoto);
		
		if (audienciaProcessoDt == null || audienciaProcessoDt.isSessaoVirtual()) {
			salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, false);
		}
		
		salvarPreAnaliseVotoEmentaSessaoPresencial(preAnaliseConclusaoDt, audienciaProcessoDt, usuarioDt);
	}
	
	// jvosantos - 04/09/2019 17:01 - Passar flag de "Voto / Ementa - Aguardando Assinatura"
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, boolean virtual) throws Exception {
		salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, virtual, false);
	}
	
	// jvosantos - 04/09/2019 17:01 - Passar flag de "Voto / Ementa - Aguardando Assinatura"
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, boolean virtual, boolean isVotoEmenta) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		VotoNe votoNe = new VotoNe();			
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		String idAudienciaProcessoSessaoSegundoGrau = null;
		if (preAnaliseConclusaoDt != null && preAnaliseConclusaoDt.getProcessoDt() != null && preAnaliseConclusaoDt.getProcessoDt().getId() != null && preAnaliseConclusaoDt.getProcessoDt().getId().trim().length() > 0) {
			AudienciaProcessoDt audienciaProcessoDt = virtual ? audienciaProcessoNe.consultarIdCompleto((new AudienciaProcessoPendenciaNe()).consultarPorIdPend(preAnaliseConclusaoDt.getPendenciaDt().getId())) : audienciaProcessoNe.consultarAudienciaPendenteProcesso(preAnaliseConclusaoDt.getProcessoDt().getId(), null, preAnaliseConclusaoDt.getId_ProcessoTipoSessao());
			if (audienciaProcessoDt != null) {
				idAudienciaProcessoSessaoSegundoGrau = audienciaProcessoDt.getId();
				audienciaProcessoNe.alterarStatusAudienciaTemp(idAudienciaProcessoSessaoSegundoGrau, preAnaliseConclusaoDt.getAudienciaStatusCodigo()); // jvosantos - 04/09/2019 17:05 - Correção de possível NullPointer
			}
		}
		arquivoNe.salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, idAudienciaProcessoSessaoSegundoGrau, virtual);
		if(preAnaliseConclusaoDt.isIniciarVotacao() && !isVotoEmenta) {  // jvosantos - 04/09/2019 17:01 - Não inicia a votação no caso de "Voto / Ementa - Aguardando Assinatura"
			votoNe.iniciarVotacaoSessaoVirtual(preAnaliseConclusaoDt.getPendenciaDt().getId(), usuarioDt, false);
		} 
		else if(preAnaliseConclusaoDt.isAguardarIniciarVotacao()) {
			PendenciaNe pendenciaNe = new PendenciaNe();
			List pendGeradasLista = preAnaliseConclusaoDt.getListaPendenciasGeradas();
			PendenciaDt pendenciaDt =  pendGeradasLista == null ? preAnaliseConclusaoDt.getPendenciaDt() : (PendenciaDt) pendGeradasLista.get(0);
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
			pendenciaNe.alterarStatus(pendenciaDt);
		}
		arquivoNe = null;
	}
	
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, List<ArquivoDt> arquivos) throws Exception {
		salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, arquivos, false);
	}
	
	// jvosantos - 04/09/2019 17:01 - Passar flag de "Voto / Ementa - Aguardando Assinatura"
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, List<ArquivoDt> arquivos, boolean virtual) throws Exception {
		salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, arquivos, virtual, false);	
	}
	
	// jvosantos - 04/09/2019 17:01 - Passar flag de "Voto / Ementa - Aguardando Assinatura"
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, List<ArquivoDt> arquivos, boolean virtual, boolean isVotoEmenta) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		preAnaliseConclusaoDt.setIniciarVotacao(CollectionUtils.isNotEmpty(arquivos));
		
		if(preAnaliseConclusaoDt.isIniciarVotacao()) {
			// jvosantos - 27/12/2019 14:03 - Usar método extraido
			audienciaProcessoNe.tratarArquivosAnaliseConclusao(preAnaliseConclusaoDt, arquivos, usuarioDt);
		}
		
		PendenciaDt pendenciaDt = new PendenciaDt();
		String idPendConcluso = null;
		if(preAnaliseConclusaoDt.getListaPendenciasFechar().get(0) != null)
			  idPendConcluso = ((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0)).getId(); 
		
		salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, virtual, isVotoEmenta); // jvosantos - 04/09/2019 17:01 - Passar flag de "Voto / Ementa - Aguardando Assinatura"
		if(preAnaliseConclusaoDt.isIniciarVotacao()) {
			AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(new AudienciaProcessoPendenciaNe().consultarPorIdPend(idPendConcluso));
			pendenciaDt.setId(audienciaProcessoDt.getId_PendenciaVotoRelator());
			new PendenciaArquivoNe().inserirArquivos(pendenciaDt, arquivos, false, false, new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()));
			
			// jvosantos - 04/09/2019 17:01 - No caso de "Voto / Ementa - Aguardando Assinatura", altera o status da pendência filha
			if(isVotoEmenta) {
				PendenciaDt pendFilha = ((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasGeradas().get(0));
				pendFilha.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
				(new PendenciaNe()).alterarStatus(pendFilha);
			}
		}
	}
	

	/**
	 * Consultar as pendências do processo de um determinado tipo
	 * 
	 * @author mmgomes
	 * @param id_Processo
	 * @param pendenciaTipoCodigo
	 * @return
	 * @throws Exception
	 * 
	 */
	public void inicializaVotacaoEmLote(String[] listaIdProcessos, UsuarioDt usuarioDt) throws Exception {
		VotoNe votoNe = new VotoNe();
		for(String idProcesso: listaIdProcessos) {
			votoNe.iniciarVotacaoSessaoVirtual(idProcesso, usuarioDt, false);
		}
	}
	
	
	public List consultarPendenciasNaoAnalisadasProcesso(String id_Processo, String pendenciaTipoCodigo) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		List pendencias = null;
			pendencias = pendenciaNe.consultarPendenciasNaoAnalisadasProcesso(id_Processo, pendenciaTipoCodigo);
		
		
		pendenciaNe = null;
		return pendencias;
	}

	/**
	 * Consultar uma pendência tipo através do id
	 * 
	 * @param id_pendenciatipo
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public PendenciaTipoDt consultarPendenciaTipo(String id_pendenciatipo) throws Exception {
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipo = null;
			pendenciaTipo = pendenciaTipoNe.consultarId(id_pendenciatipo);
		
		
		pendenciaTipoNe = null;
		return pendenciaTipo;
	}

	/**
	 * Gera movimentacao de Sessão de Julgamento Iniciado com Alterações
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoJulgamentoIniciadoAlteracoes(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_JULGAMENTO_INICIADO_CORRECAO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	/**
	 * Gera movimentacao de Sessão de Julgamento Adiado com Alterações
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoJulgamentoAdiadoAlteracoes(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_JULGAMENTO_ADIADO_CORRECAO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		stTemp = neObjeto.consultarGrupoArquivoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return stTemp;
	}

	/**
	 * Copia todas as movimentações de audiências publicadas de um processo de origem para um processo de destino Utilizado inicialmente no retorno do processo da vara de precatória
	 * 
	 * @param id_processo_origem
	 * @param id_processo_destino
	 * @param obFabricaConexao
	 * @throws Exception
	 * @author mmgomes
	 */
	public void copieTodasAudienciasPublicadasProcessoCartaPrecatoria(String id_processo_origem, String id_processo_destino, FabricaConexao obFabricaConexao) throws Exception {
		
		MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
		obPersistencia.copieTodasAudienciasPublicadasProcesso(id_processo_origem, id_processo_destino, " - Em Carta Precatória ");

	}

	public String consultarModeloJSON(UsuarioDt usuarioDt, String id_ArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		ModeloNe neObjeto = new ModeloNe();
		stTemp = neObjeto.consultarModelosJSON(tempNomeBusca,  posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		
		
		neObjeto = null;
		return stTemp;
	}

	public String consultarGrupoMovimentacaoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		MovimentacaoTipoNe neObjeto = new MovimentacaoTipoNe();
		stTemp = neObjeto.consultarGrupoMovimentacaoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		
		
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarGrupoMovimentacaoTipoSentencaJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		MovimentacaoTipoNe neObjeto = new MovimentacaoTipoNe();
		stTemp = neObjeto.consultarGrupoMovimentacaoTipoSentencaJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		
		
		neObjeto = null;
		return stTemp;
	}

	public String consultarClassificadorJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		String stTemp = "";
		ClassificadorNe neClassificador = new ClassificadorNe();
		stTemp = neClassificador.consultarClassificadorServentiaJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		
		
		neClassificador = null;
		return stTemp;

	}
	
	/**
     * Distribuir carta precatória
     * 
     * @author mmgomes
     * @since 18/10/2013
     * 
     * @param analisePendenciaDt
     * @param usuarioDt
     * 
     * @throws Exception
     */
    public void distribuirCartaPrecatoria(AnalisePendenciaDt analisePendenciaDt, UsuarioDt usuarioDt) throws Exception {
    	PendenciaNe pendenciaNe = new PendenciaNe();
    	ComarcaNe comarcaNe = new ComarcaNe();
    	ComarcaDt comarcaDt = new ComarcaDt();
    	PendenciaDt pendenciaDt = null;
    	String id_Comarca = obtenhaValorMetadadoPrimeiroArquivo(analisePendenciaDt.getListaArquivos(), "Id_Comarca");
    	
    	if (id_Comarca != null && id_Comarca.trim().length() > 0)
    		comarcaDt = comarcaNe.consultarId(id_Comarca);
    	
    	pendenciaDt = pendenciaNe.consultarId(analisePendenciaDt.getArquivoPreAnalise().getId_Pendencia());    	
    	pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
    	pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
    	
    	pendenciaNe.distribuirCartaPrecatoria(pendenciaDt, usuarioDt, analisePendenciaDt.getListaArquivos(), comarcaDt);
    }
	
	/**
     * Responde pendência com arquivos
     * 
     * @author mmgomes
     * @since 18/10/2013
     * 
     * @param analisePendenciaDt             
     * @param usuarioDt 
     * 
     * @throws Exception
     */
    public void responderPendencia(AnalisePendenciaDt analisePendenciaDt, UsuarioDt usuarioDt) throws Exception {
    	PendenciaNe pendenciaNe = new PendenciaNe();
    	ServentiaNe serventiaNe = new ServentiaNe();
    	ServentiaDt serventiaDt = new ServentiaDt();
    	PendenciaDt pendenciaDt = null;
    	String id_Serventia = obtenhaValorMetadadoPrimeiroArquivo(analisePendenciaDt.getListaArquivos(), "Id_ServentiaExpedir");
    	
    	if (id_Serventia != null && id_Serventia.trim().length() > 0)
    		serventiaDt = serventiaNe.consultarId(id_Serventia);
    	
    	pendenciaDt = pendenciaNe.consultarId(analisePendenciaDt.getArquivoPreAnalise().getId_Pendencia());
    	pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
    	pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
    	
    	pendenciaNe.responderPendencia(pendenciaDt, usuarioDt, analisePendenciaDt.getListaArquivos(), serventiaDt);    	
    }
    
    private String obtenhaValorMetadadoPrimeiroArquivo(List arquivos, String nomeDoCampo)
    {
    	if (arquivos == null || arquivos.size() == 0) return null;
    	
    	ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
    	
    	return obtenhaValorMetadadoArquivo(arquivoDt, nomeDoCampo);
    }
    
    private String obtenhaValorMetadadoArquivo(ArquivoDt arquivoDt, String nomeDoCampo)
    {
    	if (arquivoDt == null || arquivoDt.getArquivo() == null || !arquivoDt.getArquivo().contains("")) return null;
    	
    	int p1 = arquivoDt.getArquivo().indexOf("<!--Projudi");
    	int p2 = arquivoDt.getArquivo().indexOf("Projudi-->");
    	
    	if (p1 == -1 || p2 == -1 || p1+11 > p2) return null;
        
    	String texto2 = arquivoDt.getArquivo().substring(p1+11, p2);
        
        String[] partes = texto2.split(";");    
		
        for (int i = 0; i < partes.length; i++){                                 
        	String[] valor =partes[i].split(":");
        	
        	if (valor != null && valor.length == 2 && valor[0].trim().equalsIgnoreCase(nomeDoCampo))
        	{
                return valor[1].trim();
    		}
       }
    	
		return null;
    }
    

//    private boolean podeVisualizarBaixarVideo(UsuarioDt usuarioDt, ProcessoDt processoDt, boolean acessoOutraServentiaOuCodigoDeAcesso) throws Exception{
//    	
//    	if (usuarioDt == null) return acessoOutraServentiaOuCodigoDeAcesso;
//    	
//		ProcessoParteAdvogadoNe advogadoNe = new ProcessoParteAdvogadoNe();
//		ProcessoParteNe parteNe = new ProcessoParteNe();
//		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();		
//		ServentiaRelacionadaNe servRelNe = new ServentiaRelacionadaNe();
//		PendenciaNe pendeciaNe = new PendenciaNe();
//		
//		int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());		
//		if(grupo == GrupoDt.ADMINISTRADORES || grupo == GrupoDt.ADMINISTRADOR_LOG){
//			return true;
//		} else {
//			if (((usuarioDt.isMp() || usuarioDt.isAdvogado()) && advogadoNe.isAdvogadoProcesso(usuarioDt.getId_UsuarioServentia(), processoDt.getId())) 
//					|| (grupo == GrupoDt.ASSESSOR_ADVOGADOS && advogadoNe.isAdvogadoProcesso(usuarioDt.getId_UsuarioServentiaChefe(), processoDt.getId())) 
//					|| (grupo == GrupoDt.PARTES && parteNe.isParteProcesso(usuarioDt.getId_UsuarioServentia(), processoDt.getId())) 
//					|| ((processoDt.getId_Serventia().length() != 0 ) && (usuarioDt.getId_Serventia().equalsIgnoreCase(processoDt.getId_Serventia()))) 
//					|| (grupo == GrupoDt.DISTRIBUIDOR_GABINETE && validarAcessoVisualizarBaixarVideoDistribuidorGabinete(usuarioDt, processoDt))
//					|| (responsavelNe.isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId())) 
//					|| (grupo == GrupoDt.ASSESSOR_MP && responsavelNe.isResponsavelProcesso(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId()))
//					|| (processoDt.getId_ServentiaOrigem().length() > 0 && processoDt.getId_ServentiaOrigem().equals(usuarioDt.getId_Serventia()))
//					|| acessoOutraServentiaOuCodigoDeAcesso
//					|| ((grupo == GrupoDt.DESEMBARGADOR || grupo == GrupoDt.ASSESSOR_DESEMBARGADOR) && servRelNe.isServentiaRelacionada(processoDt.getId_Serventia(), usuarioDt.getId_Serventia()))
//				    || ((grupo == GrupoDt.DESEMBARGADOR || grupo == GrupoDt.ASSISTENTE_GABINETE || grupo == GrupoDt.ESTAGIARIO_GABINETE) && pendeciaNe.isResponsavelConclusoesPendentes(usuarioDt.getId_ServentiaCargo(), processoDt.getId()))
//					|| ((grupo == GrupoDt.JUIZES_UPJ || grupo == GrupoDt.ASSISTENTE_GABINETE_FLUXO) && servRelNe.isServentiaRelacionada(processoDt.getId_Serventia(), usuarioDt.getId_Serventia()))) {
//				return true;
//			}	
//		}
//		return false;
//	}

	
	private boolean validarAcessoVisualizarBaixarVideoDistribuidorGabinete(UsuarioDt usuarioDt, ProcessoDt processoDt) throws Exception{
		UsuarioNe usuarioNe = new UsuarioNe();
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		
		String idServentiaCargoDesembargador = usuarioNe.consultarIdServentiaCargoDesembargadorServentia(usuarioDt.getId_Serventia());

		if(!idServentiaCargoDesembargador.equalsIgnoreCase("")){
			return responsavelNe.isResponsavelProcesso(idServentiaCargoDesembargador, processoDt.getId());
		} else {
			return false;
		}
	}

	public String consultarArquivosJSON(String id, UsuarioNe usuarioSessao) throws Exception {
		String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarArquivosJSON(id, usuarioSessao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
	}

	public String consultarDescricaoClassificadorJSON(String stNomeBusca1, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		String stTemp = "";
		ClassificadorNe neObjeto = new ClassificadorNe();
			stTemp = neObjeto.consultarClassificadorServentiaJSON(stNomeBusca1, posicaoPaginaAtual, id_Serventia);
		
		
		neObjeto = null;
		return stTemp;
	}
	
	/**
	 * Consulta e retorna a serventia de origem do processo que foi enviado para conciliação / mediação CEJUSC.
	 * 
	 * @param String
	 *            id_processo
	 * @return ServentiaDt
	 * @throws Exception
	 */
	public ServentiaDt consultarServentiaOrigemMovimentacaoAudienciaConciliacaoEMediacaoCEJUSC(String id_Processo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			
			String id_serventia = obPersistencia.consultarIdServentiaOrigemMovimentacaoAudienciaConciliacaoEMediacaoCEJUSC(id_Processo);

			ServentiaDt serventia = null;
			if (id_serventia != null && id_serventia.trim().length() > 0) {
				serventia = new ServentiaNe().consultarId(id_serventia, obFabricaConexao);
			}
			
			return serventia;
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	public List<ProcessoTipoDt> consultarProcessoTipoServentiaRecurso(String descricao, String idRecurso, boolean somenteRecursosAtivos) throws Exception {
		return new ProcessoTipoNe().consultarProcessoTipoServentiaRecurso(descricao, idRecurso, somenteRecursosAtivos);
	}
	
	public AudienciaProcessoDt consultarAudienciaProcessoPelaPendenciaDeVoto(String id_PendenciaVoto) throws Exception {
		return new AudienciaProcessoNe().consultarCompletoPelaPendenciaDeVoto(id_PendenciaVoto);
	}
	
	public void atualizeStatusPreAnalisesSimplesParaPendentesAssinatura(String Id_PendenciaArquivo) throws Exception{
		FabricaConexao obFabricaConexao = null;
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			arquivoNe.atualizeStatusPreAnalisesSimplesParaPendentesAssinatura(Id_PendenciaArquivo, obFabricaConexao);
						
			obFabricaConexao.finalizarTransacao();
		
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Gera movimentacao de SESSÃO JULGAMENTO RETIRADA DO ACÓRDÃO/EMENTA E EXTRATO DA ATA DE JULGAMENTO
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoRetirarAcordaoEmentaExtratoAta(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_RETIRAR_ACORDAO_EMENTA_EXTRATO_ATA, id_UsuarioServentia, complemento, logDt, conexao);		
	}
	
	/**
	 * Gera movimentacao de SESSÃO JULGAMENTO RETIRADA DO EXTRATO DA ATA DE JULGAMENTO
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoRetirarExtratoAta(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_RETIRAR_EXTRATO_ATA, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de SESSÃO JULGAMENTO RETORNAR PROCESSO PARA SESSAO DE JULGAMENTO
	 * 
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 * 
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoSessaoRetornarProcessoSessaoDeJulgamento(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.SESSAO_RETORNAR_PROCESSO_SESSAO_JULGAMENTO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	
	/**
	 * Gera movimentacao de Processo Arquivado Provisoriamente
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoSigiloso(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao, int movimentacaoTipo) throws Exception {
		return gerarMovimentacao(id_Processo, movimentacaoTipo, id_UsuarioServentia, complemento, logDt, conexao);
	}


//*******************************************************************************************
// 10/05/2019
// NAO APAGAR ESTES METODOS COMENTADOS.
// QUALQUER DÚVIDA, POR FAVOR, PROCURAR MARCIO OU FRED.	
//*******************************************************************************************
//	public String consultarMovimentacaoAnaliseRepasses(String idProcesso) throws Exception {
//		String retorno = "100";
//		FabricaConexao obFabricaConexao = null;
//		
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
//			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
//			
//			if( idProcesso != null ) {
//				List<MovimentacaoDt> listaMovimentacaoDt = obPersistencia.consultarMovimentacaoAnaliseRepasses(idProcesso);
//				
//				if( listaMovimentacaoDt != null && listaMovimentacaoDt.size() > 0 ) {
//					for( MovimentacaoDt movimentacaoDt: listaMovimentacaoDt ) {
//						
//						if( movimentacaoDt.getMovimentacaoTipoCodigo() != null && movimentacaoDt.getId_MovimentacaoTipo() != null ) {
//							String porcentagemMovimentacao = porcentagemMovimentacao(Funcoes.StringToLong(movimentacaoDt.getMovimentacaoTipoCodigo()), Funcoes.StringToLong(movimentacaoDt.getId_MovimentacaoTipo()), movimentacaoDt.getComplemento());
//							
//							Integer inteiroRetorno = Funcoes.StringToInt(retorno);
//							Integer inteiroPorcentagemMovimentacao = Funcoes.StringToInt(porcentagemMovimentacao);
//							
//							if( inteiroPorcentagemMovimentacao < inteiroRetorno ) {
//								retorno = String.valueOf(inteiroPorcentagemMovimentacao);
//							}
//							
//							if( inteiroRetorno == 0 ) {
//								//Stop o loop. Quando é identificado 0% não há necessidade mais de ir até o final do loop
//								break;
//							}
//							
//						}
//						
//					}
//				}
//			}
//		}
//		finally {
//			obFabricaConexao.fecharConexao();
//		}
//		
//		return retorno;
//	}
//	
//	
//	private String porcentagemMovimentacao(long moviTipoCodigo, long idCNJMovi, String complemento) throws Exception {
//		
//		String percentualRepasse = "100";
//		boolean possuiSentenca = false;
//		boolean possuiCitacaoIntimacao = false;
//		
//		if (ehSentenca(moviTipoCodigo)) {
//			possuiSentenca = true;
//			percentualRepasse = "0";
//		} else if (ehCitacao(moviTipoCodigo)) {
//			possuiCitacaoIntimacao = true;
//			percentualRepasse = "50";
//		} else if (ehDistribuicao(idCNJMovi) || ehRedistribuicao(idCNJMovi) && complemento != null && complemento.trim().length() > 0) {
//			
//			if (ehDistribuicao(idCNJMovi)) {
//				percentualRepasse = "100";
//			} else if (ehRedistribuicao(idCNJMovi)) {
//				if (possuiSentenca) {
//					percentualRepasse = "0";
//				} else if (possuiCitacaoIntimacao) {
//					percentualRepasse = "50";
//				} 
//			}
//		}
//		
//		return percentualRepasse;
//	}
//	
//	private boolean ehDistribuicao(long idCNJMovi) {
//		if (idCNJMovi == 26) return true;
//		return false;
//	}
//	
//	private boolean ehRedistribuicao(long idCNJMovi) {
//		if (idCNJMovi == 36) return true;
//		return false;
//	}
//	
//	private boolean ehSentenca(long moviTipoCodigo) {
//		if (moviTipoCodigo == 20219) return true;
//		if (moviTipoCodigo == 20220) return true;
//		if (moviTipoCodigo == 20221) return true;
//		if (moviTipoCodigo == 20373) return true;
//		if (moviTipoCodigo == 20400) return true;
//		if (moviTipoCodigo == 20402) return true;
//		if (moviTipoCodigo == 20404) return true;
//		if (moviTipoCodigo == 20455) return true;
//		if (moviTipoCodigo == 20456) return true;
//		if (moviTipoCodigo == 20457) return true;
//		if (moviTipoCodigo == 20458) return true;
//		if (moviTipoCodigo == 20460) return true;
//		if (moviTipoCodigo == 20462) return true;
//		if (moviTipoCodigo == 20464) return true;
//		if (moviTipoCodigo == 20466) return true;
//		if (moviTipoCodigo == 20471) return true;
//		if (moviTipoCodigo == 20821) return true;
//		if (moviTipoCodigo == 20973) return true;
//		if (moviTipoCodigo == 21042) return true;
//		if (moviTipoCodigo == 21044) return true;
//		if (moviTipoCodigo == 21045) return true;
//		if (moviTipoCodigo == 21046) return true;
//		if (moviTipoCodigo == 21047) return true;
//		if (moviTipoCodigo == 21048) return true;
//		return false;
//	}
//	
//	private boolean ehCitacao(long moviTipoCodigo) {
//		if (moviTipoCodigo == 2287) return true;
//		
//		//Em conversa com a Ana e Maria de Fátima no dia 07/11/2018 às 14hs na sala da Ana, fui informado 
//		//que este 12500 que é o código cnj 60 não poderia ser contado como citação pq ela foi somente expedida
//		//e não tem confirmação de que foi efetivada.
//		//Ela pediu para adicionar a Citação Lida
//		
//		//if (moviTipoCodigo == 12500) return true;
//		if (moviTipoCodigo == 7) return true;
//		
//		return false;
//	}
//	
//	//TODO: Método foi criado pelo Márcio nos arquivos de processamento que criamos, mas não foi utilizado. Porém copiei e mantenho as alterações neste método para uma futura utilização.
//	private boolean ehCitacaoOuIntimacao(long moviTipoCodigo) {
//		if (moviTipoCodigo == 11) return true;
//		if (moviTipoCodigo == 11346) return true;
//		if (moviTipoCodigo == 11347) return true;
//		if (moviTipoCodigo == 11341) return true;
//		if (moviTipoCodigo == 11339) return true;
//		if (moviTipoCodigo == 11340) return true;
//		if (moviTipoCodigo == 2287) return true;
//		if (moviTipoCodigo == 11338) return true;
//		
//		//Em conversa com a Ana e Maria de Fátima no dia 07/11/2018 às 14hs na sala da Ana, fui informado 
//		//que este 12500 que é o código cnj 60 não poderia ser contado como citação pq ela foi somente expedida
//		//e não tem confirmação de que foi efetivada.
//		//Ela pediu para adicionar a Citação Lida
//		
//		//if (moviTipoCodigo == 12500) return true;
//		if (moviTipoCodigo == 7) return true;
//		
//		return false;
//	}
//*******************************************************************************************
// 10/05/2019
// NAO APAGAR ESTES METODOS COMENTADOS.
// QUALQUER DÚVIDA, POR FAVOR, PROCURAR MARCIO OU FRED.	
//*******************************************************************************************	
	
	/**
	 * Gera movimentacao com a carca do processo
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoCargaProcesso(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.ENTREGA_EM_CARGA_VISTA, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao com a devolução dos Autos
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoDevolucaoAutos(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.DEVOLVIDOS_OS_AUTOS, complemento, usuarioDt, arquivos, logDt, conexao);
	}

	public String[] consultarTodosProcessosClassificadoServentia(String id_Classificador, String id_Serventia, String id_juiz) throws Exception {
		List lis =  new ProcessoNe().consultarTodosProcessosClassificadosServentia(id_Classificador, id_Serventia, id_juiz);
		String[] stTemp = null;
		if (lis!=null){
			stTemp = new String[lis.size()];
			for ( int i=0; i<lis.size(); i++){
				stTemp[i] = (String) lis.get(i);
			}
		}
		return stTemp;
	}
	
	public boolean isAudienciaVirtualIniciada(String idPendencia, UsuarioDt usuario) throws Exception {
		//lrcampos 11/02/2020 14:14 - Corrigindo busca da audiProc pelo idPend e não pelo idProc.
		AudienciaProcessoPendenciaNe audiProcNe = new AudienciaProcessoPendenciaNe();
		AudienciaProcessoNe audienciaProcessoNeNe = new AudienciaProcessoNe();
		String idAudiProc = audiProcNe.consultarPorIdPend(idPendencia);
		AudienciaProcessoDt audiProc = audienciaProcessoNeNe.consultarIdCompleto(idAudiProc);
		if(audiProc != null) {
			return new AudienciaNe().isAudienciaVirtualIniciada(audiProc.getId_Audiencia());
		}
		return false;
	}
	
	public boolean isAudienciaVirtual(String idPendencia, UsuarioDt usuario) throws Exception {
		//lrcampos 11/02/2020 14:14 - Corrigindo busca da audiProc pelo idPend e não pelo idProc.
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoPendenciaNe audiProcPendNe = new AudienciaProcessoPendenciaNe();
		String idAudiProc = audiProcPendNe.consultarPorIdPend(idPendencia);
		AudienciaProcessoDt audiProc = audienciaProcessoNe.consultarIdCompleto(idAudiProc);
		if(audiProc != null) {
			return new AudienciaNe().isAudienciaVirtual(audiProc.getId_Audiencia());
		}
		return false;
	}
	
	public AudienciaProcessoStatusDt consultarStatusAudienciaTemp(String idAudienciaProcesso) throws Exception {
		return new AudienciaProcessoNe().consultarStatusAudienciaTemp(idAudienciaProcesso);
	}
	
	public AudienciaProcessoStatusDt consultarStatusAudienciaTemp(String idAudienciaProcesso, FabricaConexao obFabricaConexao) throws Exception {
		return new AudienciaProcessoNe().consultarStatusAudienciaTemp(idAudienciaProcesso, obFabricaConexao);
	}
	public AnaliseConclusaoDt getPreAnaliseConclusaoVotoEmenta(String id_Pendencia, UsuarioNe usuario) throws Exception {
		AnaliseConclusaoDt preAnaliseConclusao = getPreAnaliseConclusao(id_Pendencia);
		AudienciaProcessoDt audiProc = (new AudienciaProcessoNe()).buscarAudienciaProcessoPendentePorPendenciaOuProcesso(id_Pendencia, usuario); // jvosantos - 13/09/2019 14:45 - Correção busca de audiencia por pendência e depois por processo
		// jvosantos - 14/10/2019 16:30 - Simplificar lógica
		if(audiProc != null && preAnaliseConclusao != null) {
			AudienciaProcessoStatusDt statusAudienciaTemp = consultarStatusAudienciaTemp(audiProc.getId());
			if(statusAudienciaTemp != null) {
				preAnaliseConclusao.setAudienciaStatus(statusAudienciaTemp.getAudienciaProcessoStatus());
				preAnaliseConclusao.setAudienciaStatusCodigo(statusAudienciaTemp.getAudienciaProcessoStatusCodigo());
			}
		}
		
		// jvosantos - 14/10/2019 16:30 - Levar a responsabilidade de instanciar AnaliseConclusaoDt do CT para o NE
		if(preAnaliseConclusao == null)
			 preAnaliseConclusao = new AnaliseConclusaoDt();
		
		// jvosantos - 14/10/2019 16:30 - Usar AduienciaProcesso para setar flag de é virtual
		if(audiProc != null) 
			preAnaliseConclusao.setVirtual((new AudienciaNe()).isAudienciaVirtual(audiProc.getId_Audiencia()));
		
		return preAnaliseConclusao;
	}
	
	public AnaliseConclusaoDt getPreAnaliseConclusaoVotoEmentaVirtual(String id_Pendencia, UsuarioNe usuario)
			throws Exception {
		FabricaConexao obFabricaConexao = null;
		AnaliseConclusaoDt preAnaliseConclusao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			preAnaliseConclusao = getPreAnaliseConclusao(id_Pendencia, obFabricaConexao);
			AudienciaProcessoDt audiProc = new AudienciaProcessoNe().consultarId(
					new AudienciaProcessoPendenciaNe().consultarPorIdPend(id_Pendencia, obFabricaConexao),
					obFabricaConexao);
			if ((audiProc != null) && (preAnaliseConclusao != null)) {
				AudienciaProcessoStatusDt statusAudienciaTemp = consultarStatusAudienciaTemp(audiProc.getId(),
						obFabricaConexao);
				if (statusAudienciaTemp != null) {
					preAnaliseConclusao.setAudienciaStatus(statusAudienciaTemp.getAudienciaProcessoStatus());
					preAnaliseConclusao
							.setAudienciaStatusCodigo(statusAudienciaTemp.getAudienciaProcessoStatusCodigo());
				}
			}
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return preAnaliseConclusao;
	}

	public AnaliseConclusaoDt getPreAnaliseConclusaoVotoEmenta(String id_Pendencia, UsuarioNe usuario,
			FabricaConexao obFabricaConexao) throws Exception {

			AnaliseConclusaoDt preAnaliseConclusao = getPreAnaliseConclusao(id_Pendencia, obFabricaConexao);
			AudienciaProcessoDt audiProc = new AudienciaProcessoNe()
					.consultarId(new AudienciaProcessoPendenciaNe().consultarPorIdPend(id_Pendencia, obFabricaConexao), obFabricaConexao);
			if ((audiProc != null) && (preAnaliseConclusao != null)) {
				AudienciaProcessoStatusDt statusAudienciaTemp = consultarStatusAudienciaTemp(audiProc.getId(), obFabricaConexao);
				if (statusAudienciaTemp != null) {
					preAnaliseConclusao.setAudienciaStatus(statusAudienciaTemp.getAudienciaProcessoStatus());
					preAnaliseConclusao
							.setAudienciaStatusCodigo(statusAudienciaTemp.getAudienciaProcessoStatusCodigo());
				}
			}
			return preAnaliseConclusao;		
	}
	
	public boolean podeIniciarVotacao(String idPend, UsuarioDt usuario) throws Exception {
		AudienciaProcessoNe audienciaNe = new AudienciaProcessoNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		AudienciaProcessoDt audiProc = audienciaNe.consultarIdCompleto(audienciaProcessoPendenciaNe.consultarPorIdPend(idPend));
		if(audiProc != null && new AudienciaNe().isAudienciaVirtualIniciada(audiProc.getId_Audiencia())) {
//			List list = new PendenciaNe().consultarPendenciasProcessoPorTipo(idProcesso, PendenciaTipoDt.VERIFICAR_IMPEDIMENTO);
//			List listVerificarVotantes = new PendenciaNe().consultarPendenciasProcessoPorTipo(idProcesso, PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES);
//			return (list == null || list.size() == 0) && (listVerificarVotantes == null || listVerificarVotantes.size() == 0);
			return true;
		}
		return false;
	}
	
	public boolean podeIniciarVotacao(String idPend, UsuarioDt usuario, String idAudi, AudienciaNe audienciaNe,
			FabricaConexao conexao) throws Exception {

		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null)
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else
				obFabricaConexao = conexao;

			if (StringUtils.isNotEmpty(idAudi) && audienciaNe.isAudienciaVirtualIniciada(idAudi)) {
				return true;
			}
			return false;

		} finally {
			if (conexao == null)
				obFabricaConexao.fecharConexao();
		}

	}
	
	public List consultarPodeIniciarVotacao(List tempList, UsuarioDt usuario) throws Exception {
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			List listaPodeIniciar = new ArrayList();
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			AudienciaNe audienciaNe = new AudienciaNe();
			AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
			AudienciaProcessoDt audiProc = new AudienciaProcessoDt();

			for (int i = 0; i < tempList.size(); i++) {
				PendenciaDt pend = ((PendenciaArquivoDt) tempList.get(i)).getPendenciaDt();
				audiProc = audienciaProcessoNe
						.consultarIdCompleto(audienciaProcessoPendenciaNe.consultarPorIdPend(pend.getId()));
				listaPodeIniciar
						.add(podeIniciarVotacao(pend.getId(), usuario, audiProc.getId_Audiencia(), audienciaNe, obFabricaConexao));
			}
			obFabricaConexao.finalizarTransacao();
			return listaPodeIniciar;
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	
	public String consultarProcessoTipoRecursoSecundario(String idProcesso) throws Exception {
		return new AudienciaNe().consultarProcessoTipoRecursoSecundarioIdProcesso(idProcesso);
	}

	public void assinarVotoEmenta(Map<String, ArquivoDt> map, UsuarioDt usuario) throws Exception {
		FabricaConexao obFabricaConexao = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			VotoNe votoNe = new VotoNe();
			Set<String> keys = map.keySet();
			for (String key : keys) {
				String[] tokens = key.split("/");
				String id = tokens[0];
				String idTipo = tokens[1];
				ArquivoDt arquivo = map.get(key);
				arquivo.setNomeArquivo(tokens[2]);
				arquivo.setId_ArquivoTipo(idTipo);
				
				// jvosantos - 08/10/2019 15:39 - Gerar erro caso arquivo não esteja assinado, pode não funcionar
				if(StringUtils.isEmpty(arquivo.getUsuarioAssinador()))
					throw new Exception("Arquivo não assinado");
				
				PendenciaDt pendenciaDt = pendenciaNe.consultarId(id, obFabricaConexao);

				pendenciaArquivoNe.inserirArquivos(pendenciaDt, Arrays.asList(arquivo), false, false,
						new LogDt(usuario.getId(), usuario.getIpComputadorLog()), obFabricaConexao);
				votoNe.iniciarVotacaoSessaoVirtual(pendenciaDt.getId(), usuario, false, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Gera movimentacao de Recurso Excluído
	 * 
	 * @author mmgomes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia	 *            
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoRecursoExcluido(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.RECURSO_EXCLUIDO, id_UsuarioServentia, complemento, logDt, conexao);
	}
	
	/**
	 * Gera movimentacao de Recurso Inserido
	 * 
	 * @author mmgomes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia	 *            
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoRecursoInserido(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.RECURSO_INSERIDO, id_UsuarioServentia, complemento, logDt, conexao);
	}	
	
	/**
	 * Método que verifica se processo possui movimentação do tipo AGUARDANDO_DIGITALIZACAO_PARA_DISTRIBUICAO_2G de codigo 40067
	 * 
	 * @param id_Processo
	 *            , identificador do processo
	 * @author lsbernardes
	 */
	public boolean possuiMovimentacaoAguardandoDigitalizacao(String id_Processo) throws Exception {
		boolean retorno = false;
		
		FabricaConexao obFabricaConexao = null;

		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.possuiMovimentacaoAguardandoDigitalizacao(id_Processo);
			return retorno;
		
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Gera movimentacao de Processo de Cálculo Ativado
	 * 
	 * @author msapaula
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param id_UsuarioServentia
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            texto complementar
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public MovimentacaoDt gerarMovimentacaoProcessoCalculoAtivado(String id_Processo, String id_UsuarioServentia, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return gerarMovimentacao(id_Processo, MovimentacaoTipoDt.PROCESSO_CALCULO_ATIVADO, id_UsuarioServentia, complemento, logDt, conexao);
	}

	public boolean isAudienciaVirtualIniciadaPorIdPend(String idPend, UsuarioDt usuarioDt) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		AudienciaProcessoDt audiProc = audienciaProcessoNe.consultarIdCompleto(audienciaProcessoPendenciaNe.consultarPorIdPend(idPend));
		
		if(audiProc != null) {
			return new AudienciaNe().isAudienciaVirtualIniciada(audiProc.getId_Audiencia());
		}
		return false;
	}

	public PendenciaArquivoDt consultarEmentaDesembargadorPorId(String id_ServentiaCargo, String idAudiProc) throws Exception {
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		return pendenciaArquivoNe.consultarEmentaDesembargadorPorId(id_ServentiaCargo, idAudiProc);
	}
	
	public MovimentacaoDt consultarMovimentacaoJuntadaDeDocumentoHistoricoProcessoFisico(String id_Processo, FabricaConexao conexao) throws Exception {
		MovimentacaoPs obPersistencia = new  MovimentacaoPs(conexao.getConexao());
		return obPersistencia.consultarMovimentacaoJuntadaDeDocumentoHistoricoProcessoFisico(id_Processo);
	}
	
	/**
	 * Efetua chamada a PendenciaArquivoNe para salvar o texto parcial da pré-analise da pendência
	 * 
	 * @param preAnaliseConclusaoDt
	 *            : dados da pré-analise
	 * @param usuarioDt
	 *            : usuário responsável
	 * @throws Exception
	 */
	public PendenciaDt salvarTextoParcialPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt) throws Exception {
		PendenciaArquivoNe arquivoNe = new PendenciaArquivoNe();
		PendenciaDt pendenciaDt = null;
		pendenciaDt = arquivoNe.salvarTextoParcialPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt);		
		arquivoNe = null;
		return pendenciaDt;
	}

	public ServentiaDt consultarServentiaProcesso(String idServentia) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		return serventiaNe.consultarId(idServentia);
	}
	
	public Map<String,List<VotanteDt>> consultarVotantesPorMovimentacaoExtratoAtaInserida(List<MovimentacaoDt> movimentacoes) throws Exception{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			MovimentacaoPs movimentacaoPs = new  MovimentacaoPs(obFabricaConexao.getConexao());
			Map<String,List<VotanteDt>> temp = movimentacoes.stream().collect(Collectors.toMap(MovimentacaoDt::getId, m -> {
				try {
					return movimentacaoPs.consultarVotantesPorMovimentacaoExtratoAtaInserida(m.getId());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}));
			if (temp != null) {
				return temp;
			}else {
				return new HashMap<String, List<VotanteDt>>();
			}
		
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}


	/**
	 * Recebe o id do processo e, caso o processo esteja arquivado, retorna o id da última
	 * movimentação de arquivamento feita no processo. Caso o processo não esteja arquivado
	 * ou não hava movimentação de arquivamento, retorna null.
	 * @param String idProc, FabricaConexao fabricaConexao
	 * @return String
	 * @throws Exception
	 * 
	 * @author hrrosa
	 */
	public String consultarIdMoviUltimoArquivamento(String idProc, FabricaConexao fabricaConexao) throws Exception {
		if(idProc != null && !idProc.isEmpty() && fabricaConexao != null) {
			
			MovimentacaoPs movimentacaoPs = new  MovimentacaoPs(fabricaConexao.getConexao());
			return movimentacaoPs.consultarIdMoviUltimoArquivamento(idProc);
		
		} else {
			throw new MensagemException("Parâmetros insuficientes"); 
		}
	}
	
	public Map<String, List<VotanteDt>> consultaListaVotantesPorMovimentacaoExtratoAtaInserido(List<MovimentacaoDt> movimentacoes) throws Exception {
		Map<String, List<VotanteDt>> listaVotantesPorIdMovi = new HashMap<String, List<VotanteDt>>();
		List<MovimentacaoDt> movimentacoesExtratoAtaInserida = movimentacoes.stream().filter(m -> String
				.valueOf(MovimentacaoTipoDt.SESSAO_ATA_JULGAMENTO_INSERIDA).contentEquals(m.getMovimentacaoTipoCodigo()))
				.collect(Collectors.toList());
		listaVotantesPorIdMovi = consultarVotantesPorMovimentacaoExtratoAtaInserida(movimentacoesExtratoAtaInserida);
		return listaVotantesPorIdMovi;
	}
	
	/**
	 * Gera movimentacao com Resposta da equipe multidisciplinar
	 * 
	 * @author lsbernardes
	 * @param id_Processo
	 *            processo no qual a movimentacao sera criada
	 * @param numeroProcesso
	 * @param arquivos
	 *            lista de arquivos a serem anexados
	 * @param usuarioDt
	 *            usuario que solicita a movimentacao
	 * @param complemento
	 *            , complemento especificando o tipo de documento que está sendo expedido
	 * @param logDt
	 *            objeto de log
	 * @param conexao
	 *            fabrica para continuar a conexao, caso necessario, senao passe nulo
	 */
	public boolean gerarMovimentacaoRespostOficioDelegacia(String id_Processo, String numeroProcesso, List arquivos, UsuarioDt usuarioDt, String complemento, LogDt logDt, FabricaConexao conexao) throws Exception {
		return this.gerarMovimentacaoGenerica(id_Processo, numeroProcesso, MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO, complemento, usuarioDt, arquivos, logDt, conexao);
	}
	
	public MovimentacaoDt consultarId(String id_movimentacao, FabricaConexao obFabricaConexao) throws Exception {

		MovimentacaoDt dtRetorno=null;

		MovimentacaoPs obPersistencia = new MovimentacaoPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_movimentacao ); 
		obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}

	public String consultarMovimentacaoProcessoJSON(String descricaoMovimentacaoTipo, String id_processo, String posicaoPaginaAtual) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		if (id_processo==null || id_processo.isEmpty()) {
			throw new MensagemException("Não foi possivel definir o Processo"); 	
		}
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoPs obPersistencia = new MovimentacaoPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarMovimentacaoProcessoJSON(descricaoMovimentacaoTipo, id_processo,  posicaoPaginaAtual);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public boolean temPendenciaAberta(String idServ, String idProcesso, String idServCargo) throws Exception {
		return new PendenciaNe().temPendenciaAberta(idServ, idProcesso, idServCargo);
	}
	
	/**
	 * Ocorrência 2020/9734
	 * Método para validar pedido de arquivamento e adicionar mensagens de alertas.
	 * Verifica se a movimentação tipo é Arquivamento Requerido.
	 * Ou
	 * Se a lista de pendencia a ser gerada possui o pend-tipo arquivamento ou arquivamento provisório.
	 * 
	 * @param MovimentacaoProcessoDt movimentacaoProcessoDt
	 * @param ProcessoDt processoDt
	 * @return String 
	 * @throws Exception
	 * @author fasoares
	 */
	public String validaPedidoArquivamento(MovimentacaoProcessoDt movimentacaoProcessoDt, ProcessoDt processoDt) throws Exception {
		String mensagem = "";
		boolean tipoArquivamento = false;
		
		if( processoDt != null && processoDt.getId() != null ) {
		
			//Valida Movimentação Tipo
			if( movimentacaoProcessoDt != null && movimentacaoProcessoDt.getId_MovimentacaoTipo() != null && Funcoes.StringToInt(movimentacaoProcessoDt.getId_MovimentacaoTipo()) == MovimentacaoTipoDt.ARQUIVAMENTO_REQUERIDO ) {
				if( processoDt.getId() != null && !processoDt.getId().isEmpty() ) {
					tipoArquivamento = true;
				}
			}
			//Ou pela lista de pendencias que será gerada
			if( !tipoArquivamento ) {
				if(movimentacaoProcessoDt != null && movimentacaoProcessoDt.getListaPendenciasGerar() != null) {
					for(int i = 0; i < movimentacaoProcessoDt.getListaPendenciasGerar().size(); i++) {
						dwrMovimentarProcesso dt = (dwrMovimentarProcesso) movimentacaoProcessoDt.getListaPendenciasGerar().get(i);
						
						switch ( Funcoes.StringToInt(dt.getCodPendenciaTipo()) ) {
				            case PendenciaTipoDt.ARQUIVAMENTO:
				            case PendenciaTipoDt.ARQUIVAMENTO_PROVISORIO: {
				            	tipoArquivamento = true;
								break;
							}
						}
					}
				}
			}
			
			if(tipoArquivamento) {
				if( new GuiaEmissaoNe().isProcessoPossuiGuiaFinal_FinalZeroAguardandoPagamento(processoDt.getId()) ) {
					mensagem = "Este processo possui <font color='red'>Guia Final</font> ou <font color='red'>Guia Final Zero</font> aguardando pagamento.";
				}
			}
		
		}
		
		return mensagem;
	}
	
	public MovimentacaoProcessoDt consultarMovimentacaoECarta(String id_Processo, String codigoRastreamento) throws Exception {

		MovimentacaoProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarMovimentacaoECarta(id_Processo, codigoRastreamento);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public MovimentacaoProcessoDt consultarMovimentacaoArquivoECarta(String id_Processo, String codigoRastreamento) throws Exception {

		MovimentacaoProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			MovimentacaoPs obPersistencia = new  MovimentacaoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarMovimentacaoArquivoECarta(id_Processo, codigoRastreamento);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
}
