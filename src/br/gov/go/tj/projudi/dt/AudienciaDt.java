package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class AudienciaDt extends AudienciaDtGen {
	/**
     * 
     */
    private static final long serialVersionUID = 8358371851198344803L;

    // CÓDIGO DE SEGURANÇA DA CLASSE AUDIÊNCIADT
	public static final int CodigoPermissao = 116;

	// Variáveis para controlar possíveis status de AUDIENCIA (CodigoTemp)
	public static final int ABERTA = 0;
	public static final int FINALIZADA = 1;
	public static final int DESATIVADA = -1;
	
	public static final int QUANTIDADE_MINIMA_DIAS_ALERTA_MARCAR_SESSAO = 17;

	// LISTA CONTENDO OS PROCESSOS DA AUDIÊNCIA, OU SEJA, CONTENDO OBJETOS DO TIPO "AUDIÊNCIAPROCESSODT"
	protected List listaAudienciaProcessoDt = new ArrayList();

	// DATA UTILIZADA PARA CONSULTA DE AUDIÊNCIAS UTILIZANDO FILTROS
	private String dataInicialConsulta;

	// DATA UTILIZADA PARA CONSULTA DE AUDIÊNCIAS UTILIZANDO FILTROS
	private String dataFinalConsulta;

	/*
	 * Prazo para Agendamento de Audiênncia = é o número de dias que será somado à data corrente, fornecendo assim uma data mínima a partir da qual será
	 * procurada uma audiência livre para ser utilizada no agendamneto de audiência do processo em questão.
	 */
	private String prazoAgendamentoAudiencia;

	//Atributo para definir se movimentação é decorrente de acesso de outra Serventia, nessa variável será guardado o tipo
	//da pendência que originou o acesso de outra serventia para posteriores tratamentos
	private String acessoOutraServentia;
	
	private String id_ArquivoFinalizacaoSessao;
	
	private boolean virtual;
	
	private boolean sessaoIniciada;
	
	private String observacoes;

	/**
	 * Método responsável por limpar algumas das propriedades do objeto do tipo "AudiênciaDt". Serão limpas as seguintes
	 * propriedades: dataInicialConsulta, dataFinalConsulta e prazoAgendamentoAudiencia.
	 * 
	 * @author Keila Sousa silva
	 */
	public void limpar() {
		super.limpar();
		dataInicialConsulta = "";
		dataFinalConsulta = "";
		prazoAgendamentoAudiencia = "0";
		listaAudienciaProcessoDt = new ArrayList();
		acessoOutraServentia = "";	
		id_ArquivoFinalizacaoSessao = "";
		setObservacoes("");
		setVirtual(false);
		setSessaoIniciada(false);
	}	

	/**
	 * Método responsável por retornar a data inicial informada pelo usuário como parâmetro da consulta de audiências
	 * utilizando filtros.
	 * 
	 * @author Keila Sousa Silva
	 * @return String dataInicialConsulta
	 */
	public String getDataInicialConsulta() {
		return dataInicialConsulta;
	}

	/**
	 * Método responsável por "alimentar" a propriedade que representa a data inicial que o usuário irá fornecer como
	 * parâmetro da consulta de audiências utilização de filtros.
	 * 
	 * @author Keila Sousa Silva
	 * @param dataInicialConsulta
	 */
	public void setDataInicialConsulta(String dataInicialConsulta) {
		if (dataInicialConsulta != null) {
			this.dataInicialConsulta = dataInicialConsulta;
		}
	}

	/**
	 * Método responsável por retornar a data final informada pelo usuário como parâmetro da consulta de audiências
	 * utilizando filtros.
	 * 
	 * @author Keila Sousa Silva
	 * @return String dataFinalConsulta
	 */
	public String getDataFinalConsulta() {
		return dataFinalConsulta;
	}

	/**
	 * Método responsável por "alimentar" a propriedade que representa a data final que o usuário irá fornecer como
	 * parâmetro da consulta de audiências utilização de filtros.
	 * 
	 * @author Keila Sousa Silva
	 * @param dataFinalConsulta
	 */
	public void setDataFinalConsulta(String dataFinalConsulta) {
		if (dataFinalConsulta != null) {
			this.dataFinalConsulta = dataFinalConsulta;
		}
	}

	/**
	 * Método responsável por retornar o prazo utilizado no agendamento de audiência.
	 * 
	 * @author Keila Sousa Silva
	 * @return prazoAgendamentoAudiencia
	 */
	public String getPrazoAgendamentoAudiencia() {
		return prazoAgendamentoAudiencia;
	}

	/**
	 * Método responsável por "alimentar" a propriedade que representa o prazo utilizado no agendamento de audiência.
	 * 
	 * @author Keila Sousa Silva
	 * @param prazoAgendamentoAudiencia
	 */
	public void setPrazoAgendamentoAudiencia(String prazoAgendamentoAudiencia) {
		if (prazoAgendamentoAudiencia != null) {
			this.prazoAgendamentoAudiencia = prazoAgendamentoAudiencia;
		}
	}

	/**
	 * Método responsável por retornar a lista pertencente ao objeto do tipo "AudiênciaDt" que contêm objetos do tipo
	 * "AudiênciaProcessoDt"
	 * 
	 * @author Keila Sousa Silva
	 * @return List listaAudienciaProcessoDt
	 */
	public List<AudienciaProcessoDt> getListaAudienciaProcessoDt() { // jvosantos - 30/09/2019 18:13 - Tipar lista
		return this.listaAudienciaProcessoDt;
	}

	/**
	 * Métogo responsável por retornar uma lista contendo os objetos do tipo "AudienciaProcessoDt" que serão
	 * persistidos. Os objetos desta lista são pertencentes a um objeto do tipo "AudienciaDt" já persistido. Neste
	 * método serão feitos "sets" dos atributos provenientes do objeto do tipo "AudienciaDt" que fôra persistido e que
	 * são necessários para a persistência dos objetos do tipo "AudienciaProcessoDt" . Por exemplo, O atributo
	 * "Id_Audiencia" (gerado na persistência do objeto do tipo "AudienciaDt") presisará ser "setado" pois será
	 * utilizado na persistência dos seus objetos do tipo "AudienciaProcessoDt". Os atributos "Id_ServentiaCargo" e
	 * "Id_Processo", provenientes do objeto do tipo "AudienciaProcessoDt", também necessários para a execução da
	 * persistência dos objetos do tipo "AudienciaProcessoDt, " já estão no objeto passado como parâmetro e não precisam
	 * ser "setados" novamente.
	 * 
	 * @author Keila Sousa Silva
	 * @since 23/03/2009
	 * @param audienciaDt
	 * @return List audienciaDt.getListaAudienciaProcessoDt()
	 */
	public List<AudienciaProcessoDt> getListaAudienciaProcessoDt(AudienciaDt audienciaDt) { // jvosantos - 30/09/2019 18:13 - Tipar lista
		for (int i = 0; i < audienciaDt.getListaAudienciaProcessoDt().size(); i++) {
			audienciaDt.getListaAudienciaProcessoDt().get(i).setId("");
			audienciaDt.getListaAudienciaProcessoDt().get(i).setId_Audiencia(audienciaDt.getId());
			audienciaDt.getListaAudienciaProcessoDt().get(i).setId_UsuarioLog(audienciaDt.getId_UsuarioLog());
			audienciaDt.getListaAudienciaProcessoDt().get(i).setIpComputadorLog(audienciaDt.getIpComputadorLog());
		}
		return audienciaDt.getListaAudienciaProcessoDt();
	}

	/**
	 * Método responsável por adicionar à lista, pertencente ao objeto do tipo "AudiênciaDt", objeto do tipo
	 * "AudiênciaProcessoDt" que representa o processo vinculado à audiência em questão. Caso o objeto do tipo
	 * "AudiênciaDt" ainda não possua uma lista de objetos do tipo "AudiênciaProcessDt" esta será instanciada e depois
	 * "alimentada" como o objeto do tipo "AudiênciaProcessoDt" passado como parâmetro.
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaProcessoDt
	 */
	public void addListaAudienciaProcessoDt(AudienciaProcessoDt audienciaProcessoDt) {
		if (this.listaAudienciaProcessoDt == null) {
			this.listaAudienciaProcessoDt = new ArrayList();
		}
		this.listaAudienciaProcessoDt.add(audienciaProcessoDt);
	}

	/**
	 * Método responsável por zerar a lista, pertencente ao objeto do tipo "AudiênciaDt", que contêm objetos do tipo
	 * "AudiênciaProcessoDt", instanciá-la novamente e por fim, "alimentá-la" com a lista passada como parâmetro.
	 * 
	 * @author Keila Sousa Silva
	 * @since 05/082009
	 * @param listaAudienciaProcessoDt
	 */
	public void setListaAudienciaProcessoDt(List listaAudienciaProcessoDt) {
		this.listaAudienciaProcessoDt = listaAudienciaProcessoDt;
	}

	/**
	 * Encapsulamento para retornar primeiro registro de AudienciaProcessoDt vinculado a Audiencia,
	 * pois na maioria dos casos uma audiencia estará vinculado somente a um processo, exceto no caso de sessões do 2º grau
	 * 
	 * @author msapaula
	 * @return AudienciaProcessoDt
	 */
	public AudienciaProcessoDt getAudienciaProcessoDt() {
		if (this.listaAudienciaProcessoDt == null) {
			this.listaAudienciaProcessoDt = new ArrayList();
			this.listaAudienciaProcessoDt.add(new AudienciaProcessoDt());
		} else if (this.listaAudienciaProcessoDt.size() == 0) this.listaAudienciaProcessoDt.add(new AudienciaProcessoDt());

		return (AudienciaProcessoDt) this.listaAudienciaProcessoDt.get(0);
	}

	public String getAcessoOutraServentia() {
		return acessoOutraServentia;
	}

	public void setAcessoOutraServentia(String acessoOutraServentia) {
		if (acessoOutraServentia != null) this.acessoOutraServentia = acessoOutraServentia;
	}
	
	/**
	 * Método responsável por retornar a lista pertencente ao objeto do tipo "AudiênciaDt" que contêm objetos do tipo
	 * "AudiênciaProcessoDt" com status de audiência adiada e Iniciada
	 * 
	 * @author Márcio Gomes
	 * @return List getListaAudienciaProcessoDtAdiados
	 */
	public List getListaAudienciaProcessoDtAdiadosIniciados() {
		return getListaAudienciaProcessoDtAdiadosIniciados(true);
	}

	
	/**
	 * Método responsável por retornar a lista pertencente ao objeto do tipo "AudiênciaDt" que contêm objetos do tipo
	 * "AudiênciaProcessoDt" com status de audiência adiada e Iniciada
	 * 
	 * @author Márcio Gomes
	 * @return List getListaAudienciaProcessoDtAdiados
	 */
	public List getListaAudienciaProcessoDtAdiadosIniciados(boolean consideraRetiradaDePauta) {
		if (this.listaAudienciaProcessoDt == null || this.listaAudienciaProcessoDt.size() == 0) return this.listaAudienciaProcessoDt;
		List listaAudienciaProcessoDtIniciadosAdiados = new ArrayList();
		AudienciaProcessoDt audienciaProcessoDt;
		for(int i = 0 ; i < this.listaAudienciaProcessoDt.size();i++) {
			audienciaProcessoDt = (AudienciaProcessoDt)this.listaAudienciaProcessoDt.get(i);
			if (audienciaProcessoDt!=null && (audienciaProcessoDt.isJulgamentoAdiado() || audienciaProcessoDt.isJulgamentoIniciado() || audienciaProcessoDt.isJulgamentoAdiadoSustentacaoOral())&& ((Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.RETIRAR_PAUTA && Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.JULGAMENTO_ADIADO && Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.JULGAMENTO_INICIADO) || consideraRetiradaDePauta)) listaAudienciaProcessoDtIniciadosAdiados.add(audienciaProcessoDt);
		}
		return listaAudienciaProcessoDtIniciadosAdiados;
	}
	
	/**
	 * Método responsável por retornar a lista pertencente ao objeto do tipo "AudiênciaDt" que contêm objetos do tipo
	 * "AudiênciaProcessoDt" com status de audiência não iniciada e não adiada
	 * 
	 * @author Márcio Gomes
	 * @return List listaAudienciaProcessoDtPautaDia
	 */
	public List getListaAudienciaProcessoDtPautaDia() {		
		return getListaAudienciaProcessoDtPautaDia(true);
	}
	
	/**
	 * Método responsável por retornar a lista pertencente ao objeto do tipo "AudiênciaDt" que contêm objetos do tipo
	 * "AudiênciaProcessoDt" com status de audiência não iniciada e não adiada
	 * 
	 * @author Márcio Gomes
	 * @return List listaAudienciaProcessoDtPautaDia
	 */
	public List getListaAudienciaProcessoDtPautaDia(boolean consideraRetiradaDePauta) {		
		if (this.listaAudienciaProcessoDt == null || this.listaAudienciaProcessoDt.size() == 0) return this.listaAudienciaProcessoDt;
		List listaAudienciaProcessoDtPautaDia = new ArrayList();
		AudienciaProcessoDt audienciaProcessoDt;
		for(int i = 0 ; i < this.listaAudienciaProcessoDt.size();i++) {
			audienciaProcessoDt = (AudienciaProcessoDt)this.listaAudienciaProcessoDt.get(i);
			if (audienciaProcessoDt!=null && !audienciaProcessoDt.isJulgamentoIniciado() && !audienciaProcessoDt.isJulgamentoAdiado() && !audienciaProcessoDt.isJulgamentoEmMesaParaJulgamento() && ((Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.RETIRAR_PAUTA) || consideraRetiradaDePauta) && (Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.REMARCADA || consideraRetiradaDePauta)) listaAudienciaProcessoDtPautaDia.add(audienciaProcessoDt);
		}
		return listaAudienciaProcessoDtPautaDia;
	}	
	
	public boolean possuiSessaoVinculada(){			
		return (this.listaAudienciaProcessoDt != null && this.listaAudienciaProcessoDt.size() > 0);
	}
	
	/**
	 * Método responsável por retornar a lista pertencente ao objeto do tipo "AudiênciaDt" que contêm objetos do tipo
	 * "AudiênciaProcessoDt" com status de audiência Em Mesa Para Julgamento
	 * 
	 * @author Márcio Gomes
	 * @return List getListaAudienciaProcessoDtEmMesaParaJulgamento
	 */
	public List getListaAudienciaProcessoDtEmMesaParaJulgamento() {
		return getListaAudienciaProcessoDtEmMesaParaJulgamento(true);
	}
	
	/**
	 * Método responsável por retornar a lista pertencente ao objeto do tipo "AudiênciaDt" que contêm objetos do tipo
	 * "AudiênciaProcessoDt" com status de audiência Em Mesa Para Julgamento
	 * 
	 * @author Márcio Gomes
	 * @return List getListaAudienciaProcessoDtEmMesaParaJulgamento
	 */
	public List getListaAudienciaProcessoDtEmMesaParaJulgamento(boolean consideraRetiradaDePauta) {
		if (this.listaAudienciaProcessoDt == null || this.listaAudienciaProcessoDt.size() == 0) return this.listaAudienciaProcessoDt;
		List getListaAudienciaProcessoDtEmMesaParaJulgamento = new ArrayList();
		AudienciaProcessoDt audienciaProcessoDt;
		for(int i = 0 ; i < this.listaAudienciaProcessoDt.size();i++) {
			audienciaProcessoDt = (AudienciaProcessoDt)this.listaAudienciaProcessoDt.get(i);
			if (audienciaProcessoDt!=null && audienciaProcessoDt.isJulgamentoEmMesaParaJulgamento()  && ((Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) != AudienciaProcessoStatusDt.RETIRAR_PAUTA) || consideraRetiradaDePauta)) getListaAudienciaProcessoDtEmMesaParaJulgamento.add(audienciaProcessoDt);
		}
		return getListaAudienciaProcessoDtEmMesaParaJulgamento;
	}	
	
	public String getId_ArquivoFinalizacaoSessao() {
		return this.id_ArquivoFinalizacaoSessao;
	}

	public void setId_ArquivoFinalizacaoSessao(String idArquivoFinalizacaoSessao) {
		if (idArquivoFinalizacaoSessao != null) this.id_ArquivoFinalizacaoSessao = idArquivoFinalizacaoSessao;
	}
	
	public boolean devePossuirIndicadorDeAcordo() {
		int audienciaTipoCodigo = Funcoes.StringToInt(this.getAudienciaTipoCodigo());
		
		return (audienciaTipoCodigo == AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo() ||
				audienciaTipoCodigo == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
			    audienciaTipoCodigo == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
				audienciaTipoCodigo == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo());
	}
	
	public boolean temAudienciaTipo() {
		if( Funcoes.StringToInt(getId_AudienciaTipo(), -1) != -1) {
			return true;
		}
		return false;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	
	public void setVirtual(String virtual) {
		this.virtual = Funcoes.StringToBoolean(virtual);
	}

	public boolean isSessaoIniciada() {
		return sessaoIniciada;
	}

	public void setSessaoIniciada(boolean sessaoIniciada) {
		this.sessaoIniciada = sessaoIniciada;
	}
	
	public void setSessaoIniciada(String sessaoIniciada) {
		this.sessaoIniciada = Funcoes.StringToBoolean(sessaoIniciada);
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
}