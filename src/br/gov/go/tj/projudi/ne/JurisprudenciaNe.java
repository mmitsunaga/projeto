package br.gov.go.tj.projudi.ne;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PropriedadeDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.jurisprudencia.Jurisprudencia;
import br.gov.go.tj.projudi.jurisprudencia.JurisprudenciaService;
import br.gov.go.tj.projudi.jurisprudencia.TaminoService;
import br.gov.go.tj.projudi.ps.JurisprudenciaPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

public class JurisprudenciaNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7195177559833744528L;

	public void indexarEmentaJurisprudenciaTamino() throws Exception {
		String lastIndexedEmentaId = this.getLastIndexedEmentaIdProjudi();
		this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("[Propriedade.TAMINO_LAST_INDEXED]: " + lastIndexedEmentaId);		
		this.doIndexation(lastIndexedEmentaId);
	}
		
	public void indexarEmentaJurisprudenciaTaminoPorDia() throws Exception {
		Date data3DiasAtras = Funcoes.calculePrimeraHora(Funcoes.get3DiasAtras());
		Date dataOntem = Funcoes.calculeUltimaHora(Funcoes.getDiaAnterior());		
		String dataInicio = Funcoes.FormatarData(data3DiasAtras, "dd/MM/yyyy HH:mm:ss");
		String dataFinal = Funcoes.FormatarData(dataOntem, "dd/MM/yyyy HH:mm:ss");		
		this.doIndexationPorDia(dataInicio, dataFinal);
	}
		
	private void doIndexation(String lastIndexedEmentaId) throws Exception {
		Object[] idsEmentas = null;
		Jurisprudencia juri = null;
		try {
			idsEmentas = this.getIdsEmmenta(lastIndexedEmentaId);
			this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("[getIdsEmmenta(lastIndexedEmentaId)]: " + idsEmentas.length);
		} catch (Exception e1) {
			throw e1;
		}

		Map<String, Object> jurisNoTamino = null;
		
		for (Object oId : idsEmentas){
			try {
				String id = String.valueOf(oId);
				juri = this.getJurisprudencia(id);
				this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("[getJurisprudencia(id)] Consultado dados da ementa: " + id);
				jurisNoTamino = this.consultarJurisprudenciaPorNumeroProcesso(juri.getNr_recurso(), juri.getParte8());
				if (jurisNoTamino.isEmpty()){
					if (ValidacaoUtil.isNaoVazio(juri.getDs_ementa())){
						JurisprudenciaService.inserirJurisprudencia(juri);
						this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("[inserirJurisprudencia(juri)] Jurisprudência gravada no tamino com sucesso.");
						this.atualizaLastIndexedProperiedade(id);
						this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("[atualizaLastIndexedProperiedade(id)] Alterado propriedade TAMINO_LAST_INDEXED com " + id);
					}
				}
			} catch (Exception e) {
				throw new Exception("Erro ao tentar inserir a Jurisprudencia da ementa: " + String.valueOf(oId), e);
			}
		}
	}
	
	/**
	 * Faz a gravação de uma lista de jurisprudências de 3 dias atrás.
	 * Corrigir problema de juris remanascentes que não foram gravadas no job original
	 * Verifica se já existe no tamino antes de gravar.
	 * @param dataInicio
	 * @param dataFinal
	 * @throws Exception
	 */
	private void doIndexationPorDia(String dataInicio, String dataFinal) throws Exception {
		List<String> idsEmentas = null;
		Jurisprudencia juri = null;
		int i = 0;
		int j = 0;
		try {
			idsEmentas = this.getArrayIdEmentasPorPeriodo(dataInicio, dataFinal);
			this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("[getArrayIdEmentasPorPeriodo(" + dataInicio + ", " + dataFinal + ")]: " + idsEmentas.size());
		} catch (Exception e1) {
			throw e1;
		}
		Map<String, Object> jurisNoTamino = null;		
		for (String lista : idsEmentas){
			try {
				// Quebra a string id_arq (ementa) ; número completo do processo
				String[] partes = lista.split(";"); 
				// Verifica se já está inserido no tamino
				jurisNoTamino = this.consultarJurisprudenciaPorNumeroProcesso(partes[1], partes[0]);
				if (jurisNoTamino.isEmpty()){
					String id = partes[0];
					juri = this.getJurisprudencia(id);					
					if (ValidacaoUtil.isNaoVazio(juri.getDs_ementa())){
						JurisprudenciaService.inserirJurisprudencia(juri);
						this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("[inserirJurisprudencia(juri)] Jurisprudência " + partes[0] + " gravada no tamino com sucesso");						
					}
				}
				i+= jurisNoTamino.isEmpty() ? 1 : 0;
				j+= !jurisNoTamino.isEmpty() ? 1 : 0;
			} catch (Exception e) {
				throw new Exception("Erro ao tentar inserir a Jurisprudencia do Processo: " + juri.getNr_recurso(), e);
			}
		}
		this.tenteGravarLogJobIndexacaoJurisprudenciaTamino("Resumo: {total: " + idsEmentas.size() + ", Novas: " + i + ", Encontradas: " + j + "}");		
	}
	

	private void atualizaLastIndexedProperiedade(String id) throws Exception {
		PropriedadeNe pne = new PropriedadeNe();
		PropriedadeDt last = pne.consultarCodigo(String.valueOf(ProjudiPropriedades.TAMINO_LAST_INDEXED));
		last.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		last.setValor(id);
		pne.salvar(last);
	}

	private Object[] getIdsEmmenta(String lastIndexedEmentaId) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			JurisprudenciaPs obPersistencia = new JurisprudenciaPs(obFabricaConexao.getConexao());
			return obPersistencia.getArrayIdEmentas(lastIndexedEmentaId);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	private String getLastIndexedEmentaIdProjudi() throws Exception {
		PropriedadeNe propriedadeNe = new PropriedadeNe();
		PropriedadeDt pdt = null;
		pdt = propriedadeNe.consultarCodigo(String.valueOf(ProjudiPropriedades.TAMINO_LAST_INDEXED));
		return pdt.getValor();
	}

	public Jurisprudencia getJurisprudencia(String idEmenta) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

			JurisprudenciaPs obPersistencia = new JurisprudenciaPs(obFabricaConexao.getConexao());

			Jurisprudencia juris = obPersistencia.getJurisprudencia(idEmenta);
			
			// Tratamento para buscar o conteúdo da ementa no storage
			if (ValidacaoUtil.isNaoNulo(juris)){
				if (ValidacaoUtil.isVazio(juris.getDs_ementa())){
					// Consulta o arquivo da ementa, id fica no campo parte8
					ArquivoDt ementa = new ArquivoNe().consultarId(juris.getParte8());
					juris.generateEmentaTexto(juris.getContentType(), ementa.getConteudo(), juris.isTemRecibo());					
				}
			}
			
			return juris;

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * 
	 * @param dataAudiencia
	 * @return
	 * @throws Exception
	 */
	public List<Jurisprudencia> consultarJurisprudenciasTurmaRecursalPorData(String dataAudiencia) throws Exception {
		List<Jurisprudencia> lista = null;
		FabricaConexao obFabricaConexao = null;
		try { 
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			JurisprudenciaPs obPersistencia = new JurisprudenciaPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarJurisprudenciasTurmaRecursalPorData(dataAudiencia);		
		} catch(Exception e){
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Cadastra as jurisprudências das turmas recursais do projudi no banco tamino (base geral de jurisprudencia)
	 * @throws Exception
	 */
	public void enviarJurisprudenciasTurmaRecursalHojeProjudiParaTamino() throws Exception {
		String programa = "enviarJurisprudenciasTurmaRecursalHojeProjudiParaTamino";
		String dataHoje = Funcoes.FormatarData(new Date(), "YYYYMMDD");
		List<Jurisprudencia> lista = this.consultarJurisprudenciasTurmaRecursalPorData(dataHoje);
		LogNe obLog = new LogNe();
		for (Jurisprudencia juris : lista){
			try {
				JurisprudenciaService.inserirJurisprudencia(juris);
			} catch(Exception ex){
				obLog.salvar(new LogDt(programa, "", UsuarioDt.SistemaProjudi, "Scheduling", String.valueOf(LogTipoDt.Erro), "[param: + dataHoje +]" , ex.getMessage()));
				continue;
			}			
		}		
	}
	
	/**
	 * Faz a indexação da jurisprudência do projudi para o tamino
	 * @param listaIds - lista de identificadores dos arquivos para migração
	 * @throws Exception
	 */
	public void indexarEmentaJurisprudenciaTaminoPorId(List<String> listaIds) throws Exception {
		Jurisprudencia juri = null;		
		System.out.println("[" + getDataHoraLog() + "] - [init] Iniciando leitura de arquivo de entrada: " + listaIds.size() + " registros encontrados..");
		for (int i=0;i<listaIds.size();i++){
			String item = listaIds.get(i);
			try {				
				juri = this.getJurisprudencia(item);
				System.out.println("[" + getDataHoraLog() + "][" + (i+1) + "/" + listaIds.size() + "]" + juri.getNr_recurso() + " - " + item);
				if (ValidacaoUtil.isNaoVazio(juri.getDs_ementa())){					
					JurisprudenciaService.inserirJurisprudencia(juri);					
					this.atualizaLastIndexedProperiedade(item);					
				}
			} catch (Exception ex){
				// Se der erro em algum, continuar
				continue;
			}
		}			
		System.out.println("[" + getDataHoraLog() + "] - Processamento finalizado com sucesso.");
	}
	
	
	/**
	 * Consulta as jurisprudências de determinado período para ser importadas no TAMINO
	 * @param dataInicio
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */
	public List<String> getArrayIdEmentasPorPeriodo(String dataInicio, String dataFinal) throws Exception {
		FabricaConexao obFabricaConexao = null;		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			JurisprudenciaPs obPersistencia = new JurisprudenciaPs(obFabricaConexao.getConexao());			
			return obPersistencia.getArrayIdEmentasPorPeriodo(dataInicio, dataFinal);
		} finally {
			obFabricaConexao.fecharConexao();
		} 		
	}
	
	
	/**
	 * Verifica se a jurisprudencia já está no TAMINO
	 * @param numeroRecurso
	 * @param id_ementa
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> consultarJurisprudenciaPorNumeroProcesso (String numeroRecurso, String id_ementa) throws Exception {
		return TaminoService.consultarJurisprudenciaPorNumeroProcesso(numeroRecurso, id_ementa);
	}
		
	private static String getDataHoraLog(){
		DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formato.format(new Date());
	}
	
	private void tenteGravarLogJobIndexacaoJurisprudenciaTamino(String Mensagem) throws Exception{	
		tenteGravarLog("ExecuçãoAutomaticaIndexarJurisprudencia", Mensagem);
	}
	
	private void tenteGravarLog(String Tabela, String Mensagem) throws Exception {	
		LogDt logDt = new LogDt(Tabela, "", UsuarioDt.SistemaProjudi, "Servidor", "", Mensagem, "");
		logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomatica));			
		new LogNe().salvar(logDt);
	}
	
}
