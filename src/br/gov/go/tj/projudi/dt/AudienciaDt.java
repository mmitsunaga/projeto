package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class AudienciaDt extends AudienciaDtGen {
	/**
     * 
     */
    private static final long serialVersionUID = 8358371851198344803L;

    // C�DIGO DE SEGURAN�A DA CLASSE AUDI�NCIADT
	public static final int CodigoPermissao = 116;

	// Vari�veis para controlar poss�veis status de AUDIENCIA (CodigoTemp)
	public static final int ABERTA = 0;
	public static final int FINALIZADA = 1;
	public static final int DESATIVADA = -1;
	
	public static final int QUANTIDADE_MINIMA_DIAS_ALERTA_MARCAR_SESSAO = 17;

	// LISTA CONTENDO OS PROCESSOS DA AUDI�NCIA, OU SEJA, CONTENDO OBJETOS DO TIPO "AUDI�NCIAPROCESSODT"
	protected List listaAudienciaProcessoDt = new ArrayList();

	// DATA UTILIZADA PARA CONSULTA DE AUDI�NCIAS UTILIZANDO FILTROS
	private String dataInicialConsulta;

	// DATA UTILIZADA PARA CONSULTA DE AUDI�NCIAS UTILIZANDO FILTROS
	private String dataFinalConsulta;

	/*
	 * Prazo para Agendamento de Audi�nncia = � o n�mero de dias que ser� somado � data corrente, fornecendo assim uma data m�nima a partir da qual ser�
	 * procurada uma audi�ncia livre para ser utilizada no agendamneto de audi�ncia do processo em quest�o.
	 */
	private String prazoAgendamentoAudiencia;

	//Atributo para definir se movimenta��o � decorrente de acesso de outra Serventia, nessa vari�vel ser� guardado o tipo
	//da pend�ncia que originou o acesso de outra serventia para posteriores tratamentos
	private String acessoOutraServentia;
	
	private String id_ArquivoFinalizacaoSessao;
	
	private boolean virtual;
	
	private boolean sessaoIniciada;
	
	private String observacoes;

	/**
	 * M�todo respons�vel por limpar algumas das propriedades do objeto do tipo "Audi�nciaDt". Ser�o limpas as seguintes
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
	 * M�todo respons�vel por retornar a data inicial informada pelo usu�rio como par�metro da consulta de audi�ncias
	 * utilizando filtros.
	 * 
	 * @author Keila Sousa Silva
	 * @return String dataInicialConsulta
	 */
	public String getDataInicialConsulta() {
		return dataInicialConsulta;
	}

	/**
	 * M�todo respons�vel por "alimentar" a propriedade que representa a data inicial que o usu�rio ir� fornecer como
	 * par�metro da consulta de audi�ncias utiliza��o de filtros.
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
	 * M�todo respons�vel por retornar a data final informada pelo usu�rio como par�metro da consulta de audi�ncias
	 * utilizando filtros.
	 * 
	 * @author Keila Sousa Silva
	 * @return String dataFinalConsulta
	 */
	public String getDataFinalConsulta() {
		return dataFinalConsulta;
	}

	/**
	 * M�todo respons�vel por "alimentar" a propriedade que representa a data final que o usu�rio ir� fornecer como
	 * par�metro da consulta de audi�ncias utiliza��o de filtros.
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
	 * M�todo respons�vel por retornar o prazo utilizado no agendamento de audi�ncia.
	 * 
	 * @author Keila Sousa Silva
	 * @return prazoAgendamentoAudiencia
	 */
	public String getPrazoAgendamentoAudiencia() {
		return prazoAgendamentoAudiencia;
	}

	/**
	 * M�todo respons�vel por "alimentar" a propriedade que representa o prazo utilizado no agendamento de audi�ncia.
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
	 * M�todo respons�vel por retornar a lista pertencente ao objeto do tipo "Audi�nciaDt" que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt"
	 * 
	 * @author Keila Sousa Silva
	 * @return List listaAudienciaProcessoDt
	 */
	public List<AudienciaProcessoDt> getListaAudienciaProcessoDt() { // jvosantos - 30/09/2019 18:13 - Tipar lista
		return this.listaAudienciaProcessoDt;
	}

	/**
	 * M�togo respons�vel por retornar uma lista contendo os objetos do tipo "AudienciaProcessoDt" que ser�o
	 * persistidos. Os objetos desta lista s�o pertencentes a um objeto do tipo "AudienciaDt" j� persistido. Neste
	 * m�todo ser�o feitos "sets" dos atributos provenientes do objeto do tipo "AudienciaDt" que f�ra persistido e que
	 * s�o necess�rios para a persist�ncia dos objetos do tipo "AudienciaProcessoDt" . Por exemplo, O atributo
	 * "Id_Audiencia" (gerado na persist�ncia do objeto do tipo "AudienciaDt") presisar� ser "setado" pois ser�
	 * utilizado na persist�ncia dos seus objetos do tipo "AudienciaProcessoDt". Os atributos "Id_ServentiaCargo" e
	 * "Id_Processo", provenientes do objeto do tipo "AudienciaProcessoDt", tamb�m necess�rios para a execu��o da
	 * persist�ncia dos objetos do tipo "AudienciaProcessoDt, " j� est�o no objeto passado como par�metro e n�o precisam
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
	 * M�todo respons�vel por adicionar � lista, pertencente ao objeto do tipo "Audi�nciaDt", objeto do tipo
	 * "Audi�nciaProcessoDt" que representa o processo vinculado � audi�ncia em quest�o. Caso o objeto do tipo
	 * "Audi�nciaDt" ainda n�o possua uma lista de objetos do tipo "Audi�nciaProcessDt" esta ser� instanciada e depois
	 * "alimentada" como o objeto do tipo "Audi�nciaProcessoDt" passado como par�metro.
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
	 * M�todo respons�vel por zerar a lista, pertencente ao objeto do tipo "Audi�nciaDt", que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt", instanci�-la novamente e por fim, "aliment�-la" com a lista passada como par�metro.
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
	 * pois na maioria dos casos uma audiencia estar� vinculado somente a um processo, exceto no caso de sess�es do 2� grau
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
	 * M�todo respons�vel por retornar a lista pertencente ao objeto do tipo "Audi�nciaDt" que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt" com status de audi�ncia adiada e Iniciada
	 * 
	 * @author M�rcio Gomes
	 * @return List getListaAudienciaProcessoDtAdiados
	 */
	public List getListaAudienciaProcessoDtAdiadosIniciados() {
		return getListaAudienciaProcessoDtAdiadosIniciados(true);
	}

	
	/**
	 * M�todo respons�vel por retornar a lista pertencente ao objeto do tipo "Audi�nciaDt" que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt" com status de audi�ncia adiada e Iniciada
	 * 
	 * @author M�rcio Gomes
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
	 * M�todo respons�vel por retornar a lista pertencente ao objeto do tipo "Audi�nciaDt" que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt" com status de audi�ncia n�o iniciada e n�o adiada
	 * 
	 * @author M�rcio Gomes
	 * @return List listaAudienciaProcessoDtPautaDia
	 */
	public List getListaAudienciaProcessoDtPautaDia() {		
		return getListaAudienciaProcessoDtPautaDia(true);
	}
	
	/**
	 * M�todo respons�vel por retornar a lista pertencente ao objeto do tipo "Audi�nciaDt" que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt" com status de audi�ncia n�o iniciada e n�o adiada
	 * 
	 * @author M�rcio Gomes
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
	 * M�todo respons�vel por retornar a lista pertencente ao objeto do tipo "Audi�nciaDt" que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt" com status de audi�ncia Em Mesa Para Julgamento
	 * 
	 * @author M�rcio Gomes
	 * @return List getListaAudienciaProcessoDtEmMesaParaJulgamento
	 */
	public List getListaAudienciaProcessoDtEmMesaParaJulgamento() {
		return getListaAudienciaProcessoDtEmMesaParaJulgamento(true);
	}
	
	/**
	 * M�todo respons�vel por retornar a lista pertencente ao objeto do tipo "Audi�nciaDt" que cont�m objetos do tipo
	 * "Audi�nciaProcessoDt" com status de audi�ncia Em Mesa Para Julgamento
	 * 
	 * @author M�rcio Gomes
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