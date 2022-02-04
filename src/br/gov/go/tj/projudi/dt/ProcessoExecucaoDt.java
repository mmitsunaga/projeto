package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoExecucaoDt extends ProcessoExecucaoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 6838231630725384757L;

    public static final int CodigoPermissao=381;
    public static final int PROCESSO_PROJUDI = 1;
    public static final int PROCESSO_FISICO = 2;
    public static final int PROCESSO_CALCULO = 3;
    
	
	private ProcessoDt processoDt; //Armazena os dados do processo de execucao penal, referente ao ProcessoDt
	private List listaCondenacoes; //Armazena as condenacoes do processo
	private List listaPrisoesProvisorias; //Armazena as datas de prisão provisória
	private List listaLiberdadesProvisorias; //Armazena as datas de liberdade provisória
	private List listaModalidade;
	
	private String tempoTotalCondenacaoDias;
	private String tempoTotalCondenacaoAnos;
	
	/**
	 * Atributos para auxiliar no cadastro de processo
	 */
	
//	public static final int CadastroTipo_GuiaRecolhimento = 1;
//	public static final int CadastroTipo_ProcessoFisico = 2;
//	public static final int CadastroTipo_CalculoLiquidacao = 3;
	private int cadastroTipo; //define o tipo de cadastro
	
	private String idCrimeExecucao;
	private String crimeExecucao;
	private String dataFato;
	private String tempoPenaDias; //tempo da condenação em dias
	private String tempoCondenacaoAno; 
	private String tempoCondenacaoMes;
	private String tempoCondenacaoDia;
	private String idCondenacaoExecucaoSituacao;
	private String observacaoCondenacao;
	
	private String dataLiberdadeProvisoria;
	private String dataPrisaoProvisoria;
	private String dataPrimeiroRegime;
	
	private String idRegimeExecucao;
	private String regimeExecucao;
	private String idEventoRegime;
	private String idLocalCumprimentoPena;
	private String localCumprimentoPena;
	private String idEventoLocal;
	private String idPenaExecucaoTipo;
	private String penaExecucaoTipo;
	private String idModalidade;
	private String modalidade;
	
	private ProcessoEventoExecucaoDt eventoSursisDt;
//	private String tempoTotalSursisDias;  -- armazenado no em eventoSursisDt.quantidadeAnos
//	private String tempoTotalSursisAnos;  -- armazenado no em eventoSursisDt.quantidade
	private String tempoSursisAno;
	private String tempoSursisMes;
	private String tempoSursisDia;
	private String dataInicioSursis;
	
	private String guiaRecolhimento; //utilizada no cadastro do processo - armazena se é guia de recolhimento definitiva ou provisória
	private String numeroGuiaRecolhimento; //utilizada no cadastro do processo - armazena o número da guia
	private String anoGuiaRecolhimento; //utilizada no cadastro do processo - armazena o ano da guia
	private String reincidente;
	
	private String processoFisicoNumero;
	private boolean processoNovo;
	private boolean medidaSeguranca;
	private boolean podeCadastrarProcessoFisico;
	private boolean isIniciouCumprimentoPena;
	
	/** 
	 * variáveis existentes do ProcessoCadastroDt (que foram disvinculadas do ProcessoDt)
	 *  também necessárias para o cadastro do ProcessoExecução
	 */
	private String idComarca;
	private String comarca;
	private String comarcaCodigo;
	private String areaDistribuicaoCodigo;
	private String idAssunto;
	private String assunto;
	
	private String idServentiaCargo;
	private String serventiaCargo;

	private String idArquivoTipo;
	private String arquivoTipo;
	private String idModelo;
	private String modelo;
	private String textoEditor;
	
	private ServentiaDt serventiaDt; //Serventia para a qual processo foi distribuído
	
	private List listaArquivos; //Arquivos inseridos no processo
	
	//passos para cadastramento
	private String passo1;
	private String passo2;
	private String passo3;
	private String passo4; 
	
	
	public ProcessoExecucaoDt(){
		limpar();
		processoDt = new ProcessoDt();
	}
	
	public void limpar(){
		super.limpar();
		processoDt = new ProcessoDt();
		cadastroTipo = 0;
		listaCondenacoes = null;
		listaLiberdadesProvisorias = null;
		listaPrisoesProvisorias = null;
		listaArquivos = null;
		listaModalidade = null;
		idCrimeExecucao = "";
		crimeExecucao = "";
		dataFato = "";
		tempoPenaDias = "";
		tempoCondenacaoAno = "";
		tempoCondenacaoMes = "";
		tempoCondenacaoDia = "";
		idCondenacaoExecucaoSituacao = "";
		dataLiberdadeProvisoria = "";
		dataPrisaoProvisoria = "";
		dataPrimeiroRegime = "";
		idRegimeExecucao = "";
		regimeExecucao = "";
		idEventoRegime = "";
		idLocalCumprimentoPena = "";
		localCumprimentoPena = "";
		idEventoLocal = "";
		penaExecucaoTipo = "";
		idPenaExecucaoTipo = "";
		idModalidade = "";
		modalidade = "";
		observacaoCondenacao = "";
		
		passo1 = "Passo 1";
		passo2 = "";
		passo3 = "";
		passo4 = "";
		
		idComarca = "";
		comarca = "";
		comarcaCodigo = "";

		areaDistribuicaoCodigo = "";
		idAssunto = "";
		assunto = "";
		idArquivoTipo = "";
		arquivoTipo = "";
		idModelo = "";
		modelo = "";
		textoEditor = "";
		idServentiaCargo = "";
		serventiaCargo = "";
		
		numeroGuiaRecolhimento = "";
		anoGuiaRecolhimento = "";
		guiaRecolhimento = "";
		reincidente = "";
		processoFisicoNumero = "";
		
		processoNovo = true;
		medidaSeguranca = true;
		podeCadastrarProcessoFisico = false;
		isIniciouCumprimentoPena = true;
		
		eventoSursisDt = null;
		tempoSursisDia = "";
		tempoSursisMes = "";
		tempoSursisAno = "";
		dataInicioSursis = "";
	}
	
	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public List getListaCondenacoes() {
		return listaCondenacoes;
	}

	public void setListaCondenacoes(List listaCondenacoes) {
		this.listaCondenacoes = listaCondenacoes;
	}
	
	public void addListaCondenacoes(CondenacaoExecucaoDt dt) {
		if (this.listaCondenacoes == null) this.listaCondenacoes = new ArrayList();
		this.listaCondenacoes.add(dt);
	}

	public List getListaModalidade() {
		return listaModalidade;
	}

	public void setListaModalidade(List listaModalidade) {
		this.listaModalidade = listaModalidade;
	}
	
	public void addListaModalidade(ProcessoEventoExecucaoDt dt) {
		if (listaModalidade == null) listaModalidade = new ArrayList();
		this.listaModalidade.add(dt);
	}

	public String getId_CrimeExecucao() {
		return idCrimeExecucao;
	}

	public void setId_CrimeExecucao(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")){
			idCrimeExecucao = "";
			crimeExecucao = "";
		} else if (!id.equalsIgnoreCase("")) idCrimeExecucao = id;
	}
	
	public String getCrimeExecucao() {
		return crimeExecucao;
	}

	public void setCrimeExecucao(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) crimeExecucao = "";
		else if (!descricao.equalsIgnoreCase("")) crimeExecucao = descricao;
	}

	public String getDataFato() {
		return dataFato;
	}

	public void setDataFato(String data) {
		if (data != null) if (data.equalsIgnoreCase("null")){
			dataFato = "";
		} else dataFato = data;
	}

	public String getTempoPenaEmDias() {
		return tempoPenaDias;
	}

	public void setTempoPenaEmDias(String tempoPena) {
		if (tempoPena != null) if (tempoPena.equalsIgnoreCase("null"))	this.tempoPenaDias = "";
		else if (!tempoPena.equalsIgnoreCase("")) this.tempoPenaDias = tempoPena;
	}

	public String getTempoCondenacaoAno() {
		return tempoCondenacaoAno;
	}

	public void setTempoCondenacaoAno(String tempoCondenacaoAno) {
		if (tempoCondenacaoAno != null) if (tempoCondenacaoAno.equalsIgnoreCase("null")) this.tempoCondenacaoAno = "";
		else if (!tempoCondenacaoAno.equalsIgnoreCase("")) this.tempoCondenacaoAno = tempoCondenacaoAno.trim();
	}

	public String getTempoCondenacaoMes() {
		return tempoCondenacaoMes;
	}

	public void setTempoCondenacaoMes(String tempoCondenacaoMes) {
		if (tempoCondenacaoMes != null) if (tempoCondenacaoMes.equalsIgnoreCase("null")) this.tempoCondenacaoMes = "";
		else if (!tempoCondenacaoMes.equalsIgnoreCase("")) this.tempoCondenacaoMes = tempoCondenacaoMes.trim();
	}

	public String getTempoCondenacaoDia() {
		return tempoCondenacaoDia;
	}

	public void setTempoCondenacaoDia(String tempoCondenacaoDia) {
		if (tempoCondenacaoDia != null) if (tempoCondenacaoDia.equalsIgnoreCase("null")) this.tempoCondenacaoDia = "";
		else if (!tempoCondenacaoDia.equalsIgnoreCase("")) this.tempoCondenacaoDia = tempoCondenacaoDia.trim();
	}
	
	public String getIdCondenacaoExecucaoSituacao() {
		return idCondenacaoExecucaoSituacao;
	}

	public void setIdCondenacaoExecucaoSituacao(String idCondenacaoExecucaoSituacao) {
		this.idCondenacaoExecucaoSituacao = idCondenacaoExecucaoSituacao;
	}

	public List getListaPrisoesProvisorias() {
		return listaPrisoesProvisorias;
	}

	public void setListaPrisoesProvisorias(List listaPrisoesProvisorias) {
		this.listaPrisoesProvisorias = listaPrisoesProvisorias;
	}

	public List getListaLiberdadesProvisorias() {
		return listaLiberdadesProvisorias;
	}

	public void setListaLiberdadesProvisorias(List listaLiberdadesProvisorias) {
		this.listaLiberdadesProvisorias = listaLiberdadesProvisorias;
	}

	public void addListaPrisoesProvisorias(String data) {
		if (listaPrisoesProvisorias == null) listaPrisoesProvisorias = new ArrayList();
		this.listaPrisoesProvisorias.add(data);
	}
	
	public void addListaLiberdadesProvisorias(String data) {
		if (listaLiberdadesProvisorias == null) listaLiberdadesProvisorias = new ArrayList();
		this.listaLiberdadesProvisorias.add(data);
	}
	
	public String getDataLiberdadeProvisoria() {
		return dataLiberdadeProvisoria;
	}

	public void setDataLiberdadeProvisoria(String data) {
		if (data != null) if (data.equalsIgnoreCase("null")) dataLiberdadeProvisoria = "";
		else dataLiberdadeProvisoria = data;
	}
	
	public String getDataPrisaoProvisoria() {
		return dataPrisaoProvisoria;
	}

	public void setDataPrisaoProvisoria(String data) {
		if (data != null) if (data.equalsIgnoreCase("null")) dataPrisaoProvisoria = "";
		else dataPrisaoProvisoria = data;
	}
	
	public String getDataPrimeiroRegime() {
		return dataPrimeiroRegime;
	}

	public void setDataPrimeiroRegime(String data) {
		if (data != null) if (data.equalsIgnoreCase("null")) dataPrimeiroRegime = "";
		else dataPrimeiroRegime = data;
	}
	
	public String getId_RegimeExecucao() {
		return idRegimeExecucao;
	}

	public void setId_RegimeExecucao(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")){
			idRegimeExecucao = "";
			regimeExecucao = "";
		} else if (!id.equalsIgnoreCase("")) idRegimeExecucao = id;
	}
	
	public String getRegimeExecucao() {
		return regimeExecucao;
	}

	public void setRegimeExecucao(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) regimeExecucao = "";
		else if (!descricao.equalsIgnoreCase("")) regimeExecucao = descricao;
	}

	public String getId_LocalCumprimentoPena() {
		return idLocalCumprimentoPena;
	}

	public void setId_LocalCumprimentoPena(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")){
			idLocalCumprimentoPena = "";
			localCumprimentoPena = "";
		} else if (!id.equalsIgnoreCase("")) idLocalCumprimentoPena = id;
	}
	
	public String getLocalCumprimentoPena() {
		return localCumprimentoPena;
	}

	public void setLocalCumprimentoPena(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) localCumprimentoPena = "";
		else if (!descricao.equalsIgnoreCase("")) localCumprimentoPena = descricao;
	}
	
	public String getId_PenaExecucaoTipo() {
		return idPenaExecucaoTipo;
	}

	public void setId_PenaExecucaoTipo(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")){
			idPenaExecucaoTipo = "";
			penaExecucaoTipo = "";
		} else if (!id.equalsIgnoreCase("")) idPenaExecucaoTipo = id;
	}
	
	public String getPenaExecucaoTipo() {
		return penaExecucaoTipo;
	}

	public void setPenaExecucaoTipo(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) penaExecucaoTipo = "";
		else if (!descricao.equalsIgnoreCase("")) penaExecucaoTipo = descricao;
	}
	
	public String getId_Modalidade() {
		return idModalidade;
	}
	
	public void setId_Modalidade(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")){
			idModalidade = "";
			modalidade = "";
		} else if (!id.equalsIgnoreCase("")) idModalidade = id;
	}
	
	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) modalidade = "";
		else if (!descricao.equalsIgnoreCase("")) modalidade = descricao;
	}
	
	public String getPasso1() {
		return passo1;
	}

	public void setPasso1(String passo1) {
		if (passo1 != null) this.passo1 = passo1;
	}
	
	public String getPasso2() {
		return passo2;
	}

	public void setPasso2(String passo2) {
		if (passo2 != null) this.passo2 = passo2;
	}
	
	public String getPasso3() {
		return passo3;
	}

	public void setPasso3(String passo3) {
		if (passo3 != null) this.passo3 = passo3;
	}
	
	public String getPasso4() {
		return passo4;
	}

	public void setPasso4(String passo4) {
		if (passo4 != null) this.passo4 = passo4;
	}
	
	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		if (comarca != null) if (comarca.equalsIgnoreCase("null")) this.comarca = "";
		else if (!comarca.equalsIgnoreCase("")) this.comarca = comarca;
	}

	public String getId_Comarca() {
		return idComarca;
	}

	public void setId_Comarca(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")) {
			this.idComarca = "";
			this.comarca = "";
		} else if (!id.equalsIgnoreCase("")) this.idComarca = id;
	}

	public String getComarcaCodigo() {
		return comarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		if (comarcaCodigo != null) this.comarcaCodigo = comarcaCodigo;
	}
	
	/**
	 * Sobrescrevendo método do Gen
	 */
	public void setId_AreaDistribuicao(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")) {
			processoDt.setId_AreaDistribuicao("");
			processoDt.setAreaDistribuicao("");
		} else if (!id.equalsIgnoreCase("")) processoDt.setId_AreaDistribuicao(id);
	}
	
	public String getAreaDistribuicaoCodigo() {
		return areaDistribuicaoCodigo;
	}

	public void setAreaDistribuicaoCodigo(String areaDistribuicaoCodigo) {
		this.areaDistribuicaoCodigo = areaDistribuicaoCodigo;
	}
	
	public String getId_Assunto() {
		return idAssunto;
	}

	public void setId_Assunto(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")) {
			this.idAssunto = "";
			this.assunto = "";
		} else if (!id.equalsIgnoreCase("")) this.idAssunto = id;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		if (assunto != null) if (assunto.equalsIgnoreCase("null")) this.assunto = "";
		else if (!assunto.equalsIgnoreCase("")) this.assunto = assunto;
	}
	
	public String getId_ServentiaCargo() {
		return idServentiaCargo;
	}

	public void setId_ServentiaCargo(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")) {
			this.idServentiaCargo = "";
			this.serventiaCargo = "";
		} else if (!id.equalsIgnoreCase("")) this.idServentiaCargo = id;
	}

	public String getServentiaCargo() {
		return serventiaCargo;
	}

	public void setServentiaCargo(String serventiaCargo) {
		if (serventiaCargo != null) if (serventiaCargo.equalsIgnoreCase("null")) this.serventiaCargo = "";
		else if (!serventiaCargo.equalsIgnoreCase("")) this.serventiaCargo = serventiaCargo;
	}
	
	public String getId_ArquivoTipo() {
		return idArquivoTipo;
	}

	public void setId_ArquivoTipo(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")) {
			this.idArquivoTipo = "";
			this.arquivoTipo = "";
		} else if (!id.equalsIgnoreCase("")) this.idArquivoTipo = id;
	}

	public String getArquivoTipo() {
		return arquivoTipo;
	}

	public void setArquivoTipo(String arquivoTipo) {
		if (arquivoTipo != null) if (arquivoTipo.equalsIgnoreCase("null")) this.arquivoTipo = "";
		else if (!arquivoTipo.equalsIgnoreCase("")) this.arquivoTipo = arquivoTipo;
	}

	public String getId_Modelo() {
		return idModelo;
	}

	public void setId_Modelo(String id) {
		if (id != null) if (id.equalsIgnoreCase("null")) {
			this.idModelo = "";
			this.modelo = "";
		} else if (!id.equalsIgnoreCase("")) this.idModelo = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		if (modelo != null) if (modelo.equalsIgnoreCase("null")) this.modelo = "";
		else if (!modelo.equalsIgnoreCase("")) this.modelo = modelo;
	}

	public String getTextoEditor() {
		return textoEditor;
	}

	public void setTextoEditor(String textoEditor) {
		if (textoEditor != null) if (!textoEditor.equalsIgnoreCase("")) this.textoEditor = textoEditor.replaceAll("\r\n", "");
		else this.textoEditor = "";
	}
	
	public ServentiaDt getServentiaDt() {
		return serventiaDt;
	}

	public void setServentiaDt(ServentiaDt serventiaDt) {
		this.serventiaDt = serventiaDt;
	}

	public List getListaArquivos() {
		return listaArquivos;
	}

	public void setListaArquivos(List listaArquivos) {
		this.listaArquivos = listaArquivos;
	}
	
	public String getNumeroGuiaRecolhimento() {
		return numeroGuiaRecolhimento;
	}

	public String getAnoGuiaRecolhimento() {
		return anoGuiaRecolhimento;
	}

	public void setAnoGuiaRecolhimento(String anoGuiaRecolhimento) {
		if (anoGuiaRecolhimento != null) if (anoGuiaRecolhimento.equalsIgnoreCase("null")) this.anoGuiaRecolhimento = "";
		else if (!anoGuiaRecolhimento.equalsIgnoreCase("")) this.anoGuiaRecolhimento = anoGuiaRecolhimento;
	}

	public void setNumeroGuiaRecolhimento(String numeroGuiaRecolhimento) {
		if (numeroGuiaRecolhimento != null) if (numeroGuiaRecolhimento.equalsIgnoreCase("null")) this.numeroGuiaRecolhimento = "";
		else if (!numeroGuiaRecolhimento.equalsIgnoreCase("")) this.numeroGuiaRecolhimento = numeroGuiaRecolhimento;
	}

	public String getGuiaRecolhimento() {
		return guiaRecolhimento;
	}

	public void setGuiaRecolhimento(String guiaRecolhimento) {
		this.guiaRecolhimento = guiaRecolhimento;
	}

	public String getReincidente() {
		return reincidente;
	}

	public void setReincidente(String reincidente) {
		this.reincidente = reincidente;
	}
	
	public String getTempoTotalCondenacaoAnos() {
		return tempoTotalCondenacaoAnos;
	}

	public void setTempoTotalCondenacaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalCondenacaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalCondenacaoAnos = "";
	}
	
	public String getTempoTotalCondenacaoDias() {
		return this.tempoTotalCondenacaoDias;
	}

	public void setTempoTotalCondenacaoDias(String tempoEmDias) {
		if (tempoEmDias == null) this.tempoTotalCondenacaoDias = "0";
		else this.tempoTotalCondenacaoDias = tempoEmDias;
		setTempoTotalCondenacaoAnos(this.tempoTotalCondenacaoDias);
	}

	public String getIdEventoRegime() {
		return idEventoRegime;
	}

	public void setIdEventoRegime(String idEventoRegime) {
		this.idEventoRegime = idEventoRegime;
	}

	public String getIdEventoLocal() {
		return idEventoLocal;
	}

	public void setIdEventoLocal(String idEventoLocal) {
		this.idEventoLocal = idEventoLocal;
	}
	
	public void setProcessoNovo(boolean processoNovo){
		this.processoNovo = processoNovo;
	}

	public boolean isProcessoNovo(){
		return this.processoNovo;
	}
	
	public void setMedidaSeguranca(boolean medidaSeguranca){
		this.medidaSeguranca = medidaSeguranca;
	}

	public boolean isMedidaSeguranca(){
		return this.medidaSeguranca;
	}
	
	public void setPodeCadastrarProcessoFisico(boolean podeCadastrarProcessoFisico){
		this.podeCadastrarProcessoFisico = podeCadastrarProcessoFisico;
	}

	public boolean isPodeCadastrarProcessoFisico(){
		return this.podeCadastrarProcessoFisico;
	}

	public boolean isIniciouCumprimentoPena() {
		return isIniciouCumprimentoPena;
	}

	public void setIniciouCumprimentoPena(boolean isIniciouCumprimentoPena) {
		this.isIniciouCumprimentoPena = isIniciouCumprimentoPena;
	}

	public boolean isAtivo() {
		if (super.getCodigoTemp().equalsIgnoreCase("null") || super.getCodigoTemp().equalsIgnoreCase("") 
				|| super.getCodigoTemp().equalsIgnoreCase("true") || super.getCodigoTemp().equalsIgnoreCase("1"))
			return true;
		else return false; 
	}

    public void setAtivo(String valor) {
    	if (valor.equalsIgnoreCase("null") || valor.equalsIgnoreCase("") 
    			|| valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("1"))
    		super.setCodigoTemp("1");
    	else super.setCodigoTemp("0");
    }
	
	public ProcessoEventoExecucaoDt getEventoSursisDt() {
		return this.eventoSursisDt;
	}

	public void setEventoSursisDt(ProcessoEventoExecucaoDt eventoSursisDt) {
		if (this.eventoSursisDt == null) this.eventoSursisDt = new ProcessoEventoExecucaoDt();
		this.eventoSursisDt = eventoSursisDt;
	}
	
	public String getTempoSursisAno() {
		return tempoSursisAno;
	}

	public void setTempoSursisAno(String tempoSursisAno) {
		if (tempoSursisAno != null) if (tempoSursisAno.equalsIgnoreCase("null")) this.tempoSursisAno = "";
		else this.tempoSursisAno = tempoSursisAno.trim();
	}
	
	public String getTempoSursisMes() {
		return tempoSursisMes;
	}

	public void setTempoSursisMes(String tempoSursisMes) {
		if (tempoSursisMes != null) if (tempoSursisMes.equalsIgnoreCase("null")) this.tempoSursisMes = "";
		else this.tempoSursisMes = tempoSursisMes.trim();
	}
	
	public String getTempoSursisDia() {
		return tempoSursisDia;
	}

	public void setTempoSursisDia(String tempoSursisDia) {
		if (tempoSursisDia != null) if (tempoSursisDia.equalsIgnoreCase("null")) this.tempoSursisDia = "";
		else this.tempoSursisDia = tempoSursisDia.trim();
	}
	
	public String getTempoTotalSursisAnos() {
		if (this.eventoSursisDt != null) return this.eventoSursisDt.getQuantidadeAnos();
		else return "";
	}

	public void setTempoTotalSursisAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) {
			String[] tempoAnos = this.eventoSursisDt.getQuantidadeAnos().split(" - ");
	        this.setTempoSursisAno(tempoAnos[0]);
	        this.setTempoSursisMes(tempoAnos[1]);
	        this.setTempoSursisDia(tempoAnos[2]);	
		}
		else {
			this.setTempoSursisAno("");
	        this.setTempoSursisMes("");
	        this.setTempoSursisDia("");
		}
	}
	
	public String getTempoTotalSursisDias() {
		return this.eventoSursisDt.getQuantidade();
	}

	public void setTempoTotalSursisDias(String tempoEmDias) {
		if (this.eventoSursisDt == null) this.eventoSursisDt = new ProcessoEventoExecucaoDt();
		this.eventoSursisDt.setQuantidade(tempoEmDias);
		setTempoTotalSursisAnos(tempoEmDias);
	}
	
	public String getProcessoFisicoNumero() {
		return processoFisicoNumero;
	}

	public void setProcessoFisicoNumero(String processoFisicoNumero) {
		if (processoFisicoNumero != null) if (processoFisicoNumero.equalsIgnoreCase("null")) this.processoFisicoNumero = "";
		else if (!processoFisicoNumero.equalsIgnoreCase("")) this.processoFisicoNumero = processoFisicoNumero;
	}

	public int getCadastroTipo() {
		return cadastroTipo;
	}

	public void setCadastroTipo(int cadastroTipo) {
		this.cadastroTipo = cadastroTipo;
	}

	public String getObservacaoCondenacao() {
		return observacaoCondenacao;
	}

	public void setObservacaoCondenacao(String observacaoCondenacao) {
		if (observacaoCondenacao != null) if (observacaoCondenacao.equalsIgnoreCase("null"))	this.observacaoCondenacao = "";
		else if (!observacaoCondenacao.equalsIgnoreCase("")) this.observacaoCondenacao = observacaoCondenacao;
	}

	public String getDataInicioSursis() {
		return dataInicioSursis;
	}

	public void setDataInicioSursis(String data) {
		if (data != null) if (data.equalsIgnoreCase("null")){
			dataInicioSursis = "";
		} else dataInicioSursis = data;
	}

	public void validarMedidaSeguranca() {
        if ((idPenaExecucaoTipo != null && idPenaExecucaoTipo.equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))
        		|| idPenaExecucaoTipo.length() == 0) && (listaCondenacoes == null || listaCondenacoes.size() == 0)){
        	this.setMedidaSeguranca(true);
        } else {
        	this.setMedidaSeguranca(false);
        }
	}
	
}
