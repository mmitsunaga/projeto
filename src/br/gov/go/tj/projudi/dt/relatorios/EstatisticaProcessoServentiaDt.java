package br.gov.go.tj.projudi.dt.relatorios;

import java.io.Serializable;

import br.gov.go.tj.utils.DiaHoraEventos;

public class EstatisticaProcessoServentiaDt implements Serializable  {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2341728484635231604L;

    public static final int CodigoPermissao = 143;
    
    //VARIAVEIS DO RELATÓRIO
    private String qtdProcAtivosTotal;
    private String qtdProcAtivos;
    private String percProcAtivosMesmoTipo;
    private String percProcAtivosTodosTipos;
    
    private String qtdProcMaiores60;
    private String percProcMaiores60MesmoTipo;
    private String percProcMaiores60TodosTipos;
    
    private String qtdProcSegredoJustica;
    private String percProcSegredoJusticaMesmoTipo;
    private String percProcSegredoJusticaTodosTipos;
   
    private String qtdProcSuspensoPrazo;
    private String percProcSuspensoPrazoMesmoTipo;
    private String percProcSuspensoPrazoTodosTipos;
    
    private String qtdProcSuspensoSemPrazo;
    private String percProcSuspensoSemPrazoMesmoTipo;
    private String percProcSuspensoSemPrazoTodosTipos;
    
    private String qtdProcContador;
    private String percProcContadorMesmoTipo;
    private String percProcContadorTodosTipos;
    
    private String qtdProcTurmaRecursais;
    private String percProcTurmaRecursaisMesmoTipo;
    private String PercProcTurmaRecursaisTodosTipos;
    
    private String qtdProcFaseConhecimento;
    private String percProcFaseConhecimentoMesmoTipo;
    private String percProcFaseConhecimentoTodosTipos;
    
    private String qtdProcFaseExecucao;
    private String percProcFaseExecucaoMesmoTipo;
    private String percProcFaseExecucaoTodosTipos;
    
    private String qtdProcSemiParalisados;
    private String perctProcSemiParalisadoMesmoTipo;
    private String perctProcSemiParalisadoTodosTipos;
    
    private String qtdProcParalisados;
    private String percProcParalisadoMesmoTipo;
    private String percProcParalisadoTodosTipos;
    
    //VARIAVEIS SEGUNDA PARTE DO RELATÓRIO
    private String hoje;
	private String semana;
	private String mes;
	private String ano;
	
	private String procDistribuidoHoje;
	private String procDistribuidoSemana;
	private String procDistribuidoMes;
	private String procDistribuidoAno;
	
	private String procArquivadoHoje;
	private String procArquivadoSemana;
	private String procArquivadoMes;
	private String procArquivadoAno;
	
	private String tempoMedioTramitacaoHoje;
	private String tempoMedioTramitacaoSemana;
	private String tempoMedioTramitacaoMes;
	private String tempoMedioTramitacaoAno;
	
	private String percBalancoJudiciarioHoje;
	private String percBalancoJudiciarioSemana;
	private String percBalancoJudiciarioMes;
	private String percBalancoJudiciarioAno;
    
    //VARIAVEIS DE CONTROLE
    private String id_Serventia;
	private String serventia;
	private String id_ProcessoTipo;
	private String processoTipo;
	private String processoStatus;
	private String prioridadeProcesso;
	private String segredoJustica;
	private String faseProcesso;
	private String diasParalisados;
	private String suspensoPrazo;
	
	 //VARIAVEIS DE CONTROLE SEGUANDA PARTE DO RELATÓRIO
	private DiaHoraEventos dataInicioDistribuicao;
	private DiaHoraEventos dataFinalDistribuicao;
	private DiaHoraEventos dataInicioArquivamento;
	private DiaHoraEventos dataFinalArquivamento;
	
	//---------------------------------------------------------   
    public EstatisticaProcessoServentiaDt() {
		limpar();
	}
    
    public void limpar(){
    	//DADOS DO RELATÓRIO
    	qtdProcAtivosTotal = "";
    	qtdProcAtivos = "";
    	percProcAtivosMesmoTipo = "";
    	percProcAtivosTodosTipos = "";
    	
    	qtdProcMaiores60 = "";
    	percProcMaiores60MesmoTipo = "";
    	percProcMaiores60TodosTipos = "";
    	
    	qtdProcSegredoJustica = "";
    	percProcSegredoJusticaMesmoTipo = "";
    	percProcSegredoJusticaTodosTipos = "";
    	
    	qtdProcSuspensoPrazo = "";
    	percProcSuspensoPrazoMesmoTipo = "";
    	percProcSuspensoPrazoTodosTipos = "";
    	
    	qtdProcSuspensoSemPrazo = "";
    	percProcSuspensoSemPrazoMesmoTipo = "";
    	percProcSuspensoSemPrazoTodosTipos = "";
    	
    	qtdProcContador = "";
    	percProcContadorMesmoTipo="";
    	percProcContadorTodosTipos="";
    	
    	qtdProcTurmaRecursais="";
        percProcTurmaRecursaisMesmoTipo="";
        PercProcTurmaRecursaisTodosTipos="";
    	
    	qtdProcFaseConhecimento = "";
    	percProcFaseConhecimentoMesmoTipo = "";
    	percProcFaseConhecimentoTodosTipos = "";
    	
    	qtdProcFaseExecucao = "";
    	percProcFaseExecucaoMesmoTipo= "";
    	percProcFaseExecucaoTodosTipos= "";
    	
    	qtdProcSemiParalisados = "";
    	perctProcSemiParalisadoMesmoTipo = "";
    	perctProcSemiParalisadoTodosTipos = "";
    	
    	qtdProcParalisados = "";
        percProcParalisadoMesmoTipo = "";
        percProcParalisadoTodosTipos = "";
        
        //DADOS SEGUNDA PARTE DO RELATÓRIO
        hoje = "";
    	semana = "";
    	mes = "";
    	ano = "";
    	
    	procDistribuidoHoje = "";
    	procDistribuidoSemana = "";
    	procDistribuidoMes = "";
    	procDistribuidoAno = "";
    	
    	procArquivadoHoje = "";
    	procArquivadoSemana = "";
    	procArquivadoMes = "";
    	procArquivadoAno = "";
    	
    	tempoMedioTramitacaoHoje = "";
    	tempoMedioTramitacaoSemana = "";
    	tempoMedioTramitacaoMes = "";
    	tempoMedioTramitacaoAno = "";
    	
    	percBalancoJudiciarioHoje = "";
    	percBalancoJudiciarioSemana = "";
    	percBalancoJudiciarioMes = "";
    	percBalancoJudiciarioAno = "";
    	
    	//VARIAVEIS DE CONTROLE
    	id_Serventia = "";
    	serventia = "";
    	id_ProcessoTipo = "";
    	processoTipo = "";
    	processoStatus = "";
    	prioridadeProcesso = "";
    	segredoJustica = "";
    	suspensoPrazo = "";
    	faseProcesso = "";
    	diasParalisados = "";
    	
    	//VARIAVEIS DE CONTROLE DA SEGUNDA PARTE DO RELATÓRIO
    	dataInicioDistribuicao = null;
    	dataFinalDistribuicao = null;
    	dataInicioArquivamento = null;
    	dataFinalArquivamento = null;
    	
	}
    
    public void limparParametrosConsulta(){
    	processoStatus = "";
    	prioridadeProcesso = "";
    	segredoJustica = "";
    	suspensoPrazo = "";
    	faseProcesso = "";
    	diasParalisados = "";
    	dataInicioDistribuicao = null;
    	dataFinalDistribuicao = null;
    	dataInicioArquivamento = null;
    	dataFinalArquivamento = null;
    }
    
    public void limparProcessoTipo(){
    	id_ProcessoTipo = "";
    	processoTipo = "";
	}
    
    // GET E SET DOS CAMPOS DO RELATORIO ***************************************************
	public String getQtdProcAtivosTotal() {
		return qtdProcAtivosTotal;
	}

	public void setQtdProcAtivosTotal(String qtdProcAtivosTotal) {
		if (qtdProcAtivosTotal != null)
			this.qtdProcAtivosTotal = qtdProcAtivosTotal;
	}
    
    public String getQtdProcAtivos() {
		return qtdProcAtivos;
	}

	public void setQtdProcAtivos(String qtdProcAtivos) {
		if (qtdProcAtivos != null)
			this.qtdProcAtivos = qtdProcAtivos;
	}

	public String getPercProcAtivosMesmoTipo() {
		return percProcAtivosMesmoTipo;
	}

	public void setPercProcAtivosMesmoTipo(String percProcAtivosMesmoTipo) {
		if (percProcAtivosMesmoTipo != null)
			this.percProcAtivosMesmoTipo = percProcAtivosMesmoTipo;
	}

	public String getPercProcAtivosTodosTipos() {
		return percProcAtivosTodosTipos;
	}

	public void setPercProcAtivosTodosTipos(String percProcAtivosTodosTipos) {
		if (percProcAtivosTodosTipos != null)
			this.percProcAtivosTodosTipos = percProcAtivosTodosTipos;
	}

	public String getQtdProcMaiores60() {
		return qtdProcMaiores60;
	}

	public void setQtdProcMaiores60(String qtdProcMaiores60) {
		if (qtdProcMaiores60 != null)
			this.qtdProcMaiores60 = qtdProcMaiores60;
	}

	public String getPercProcMaiores60MesmoTipo() {
		return percProcMaiores60MesmoTipo;
	}

	public void setPercProcMaiores60MesmoTipo(String percProcMaiores60MesmoTipo) {
		if (percProcMaiores60MesmoTipo != null)
			this.percProcMaiores60MesmoTipo = percProcMaiores60MesmoTipo;
	}

	public String getPercProcMaiores60TodosTipos() {
		return percProcMaiores60TodosTipos;
	}

	public void setPercProcMaiores60TodosTipos(String percProcMaiores60TodosTipos) {
		if (percProcMaiores60TodosTipos != null)
			this.percProcMaiores60TodosTipos = percProcMaiores60TodosTipos;
	}
	
	public String getQtdProcSegredoJustica() {
		return qtdProcSegredoJustica;
	}

	public void setQtdProcSegredoJustica(String qtdProcSegredoJustica) {
		if (qtdProcSegredoJustica != null)
			this.qtdProcSegredoJustica = qtdProcSegredoJustica;
	}

	public String getPercProcSegredoJusticaMesmoTipo() {
		return percProcSegredoJusticaMesmoTipo;
	}

	public void setPercProcSegredoJusticaMesmoTipo(
			String percProcSegredoJusticaMesmoTipo) {
		if (percProcSegredoJusticaMesmoTipo != null)
			this.percProcSegredoJusticaMesmoTipo = percProcSegredoJusticaMesmoTipo;
	}

	public String getPercProcSegredoJusticaTodosTipos() {
		return percProcSegredoJusticaTodosTipos;
	}

	public void setPercProcSegredoJusticaTodosTipos(
			String percProcSegredoJusticaTodosTipos) {
		if (percProcSegredoJusticaTodosTipos != null)
			this.percProcSegredoJusticaTodosTipos = percProcSegredoJusticaTodosTipos;
	}
	
	public String getQtdProcSuspensoPrazo() {
		return qtdProcSuspensoPrazo;
	}

	public void setQtdProcSuspensoPrazo(String qtdProcSuspensoPrazo) {
		if (qtdProcSuspensoPrazo != null)
			this.qtdProcSuspensoPrazo = qtdProcSuspensoPrazo;
	}

	public String getPercProcSuspensoPrazoMesmoTipo() {
		return percProcSuspensoPrazoMesmoTipo;
	}

	public void setPercProcSuspensoPrazoMesmoTipo(
			String percProcSuspensoPrazoMesmoTipo) {
		if (percProcSuspensoPrazoMesmoTipo != null)
			this.percProcSuspensoPrazoMesmoTipo = percProcSuspensoPrazoMesmoTipo;
	}

	public String getPercProcSuspensoPrazoTodosTipos() {
		return percProcSuspensoPrazoTodosTipos;
	}

	public void setPercProcSuspensoPrazoTodosTipos(
			String percProcSuspensoPrazoTodosTipos) {
		if (percProcSuspensoPrazoTodosTipos != null)
			this.percProcSuspensoPrazoTodosTipos = percProcSuspensoPrazoTodosTipos;
	}
	
	public String getQtdProcSuspensoSemPrazo() {
		return qtdProcSuspensoSemPrazo;
	}

	public void setQtdProcSuspensoSemPrazo(String qtdProcSuspensoSemPrazo) {
		if (qtdProcSuspensoSemPrazo != null)
			this.qtdProcSuspensoSemPrazo = qtdProcSuspensoSemPrazo;
	}

	public String getPercProcSuspensoSemPrazoMesmoTipo() {
		return percProcSuspensoSemPrazoMesmoTipo;
	}

	public void setPercProcSuspensoSemPrazoMesmoTipo(
			String percProcSuspensoSemPrazoMesmoTipo) {
		if (percProcSuspensoSemPrazoMesmoTipo != null)
			this.percProcSuspensoSemPrazoMesmoTipo = percProcSuspensoSemPrazoMesmoTipo;
	}

	public String getPercProcSuspensoSemPrazoTodosTipos() {
		return percProcSuspensoSemPrazoTodosTipos;
	}

	public void setPercProcSuspensoSemPrazoTodosTipos(
			String percProcSuspensoSemPrazoTodosTipos) {
		if (percProcSuspensoSemPrazoTodosTipos != null)
			this.percProcSuspensoSemPrazoTodosTipos = percProcSuspensoSemPrazoTodosTipos;
	}
	
	public String getQtdProcFaseConhecimento() {
		return qtdProcFaseConhecimento;
	}

	public void setQtdProcFaseConhecimento(String qtdProcFaseConhecimento) {
		if (qtdProcFaseConhecimento != null)
			this.qtdProcFaseConhecimento = qtdProcFaseConhecimento;
	}

	public String getPercProcFaseConhecimentoMesmoTipo() {
		return percProcFaseConhecimentoMesmoTipo;
	}

	public void setPercProcFaseConhecimentoMesmoTipo(
			String percProcFaseConhecimentoMesmoTipo) {
		if (percProcFaseConhecimentoMesmoTipo != null)
			this.percProcFaseConhecimentoMesmoTipo = percProcFaseConhecimentoMesmoTipo;
	}

	public String getPercProcFaseConhecimentoTodosTipos() {
		return percProcFaseConhecimentoTodosTipos;
	}

	public void setPercProcFaseConhecimentoTodosTipos(
			String percProcFaseConhecimentoTodosTipos) {
		if (percProcFaseConhecimentoTodosTipos != null)
			this.percProcFaseConhecimentoTodosTipos = percProcFaseConhecimentoTodosTipos;
	}
	
	public String getQtdProcFaseExecucao() {
		return qtdProcFaseExecucao;
	}

	public void setQtdProcFaseExecucao(String qtdProcFaseExecucao) {
		if (qtdProcFaseExecucao != null)
			this.qtdProcFaseExecucao = qtdProcFaseExecucao;
	}

	public String getPercProcFaseExecucaoMesmoTipo() {
		return percProcFaseExecucaoMesmoTipo;
	}

	public void setPercProcFaseExecucaoMesmoTipo(
			String percProcFaseExecucaoMesmoTipo) {
		if (percProcFaseExecucaoMesmoTipo != null)
			this.percProcFaseExecucaoMesmoTipo = percProcFaseExecucaoMesmoTipo;
	}

	public String getPercProcFaseExecucaoTodosTipos() {
		return percProcFaseExecucaoTodosTipos;
	}

	public void setPercProcFaseExecucaoTodosTipos(
			String percProcFaseExecucaoTodosTipos) {
		if (percProcFaseExecucaoTodosTipos != null)
			this.percProcFaseExecucaoTodosTipos = percProcFaseExecucaoTodosTipos;
	}
	
	public String getQtdProcSemiParalisados() {
		return qtdProcSemiParalisados;
	}

	public void setQtdProcSemiParalisados(String qtdProcSemiParalisados) {
		if (qtdProcSemiParalisados != null)
			this.qtdProcSemiParalisados = qtdProcSemiParalisados;
	}

	public String getPerctProcSemiParalisadoMesmoTipo() {
		return perctProcSemiParalisadoMesmoTipo;
	}

	public void setPerctProcSemiParalisadoMesmoTipo(
			String perctProcSemiParalisadoMesmoTipo) {
		if (perctProcSemiParalisadoMesmoTipo != null)
			this.perctProcSemiParalisadoMesmoTipo = perctProcSemiParalisadoMesmoTipo;
	}

	public String getPerctProcSemiParalisadoTodosTipos() {
		return perctProcSemiParalisadoTodosTipos;
	}

	public void setPerctProcSemiParalisadoTodosTipos(
			String perctProcSemiParalisadoTodosTipos) {
		if (perctProcSemiParalisadoTodosTipos != null)
			this.perctProcSemiParalisadoTodosTipos = perctProcSemiParalisadoTodosTipos;
	}
	public String getQtdProcParalisados() {
		return qtdProcParalisados;
	}

	public void setQtdProcParalisados(String qtdProcParalisados) {
		if (qtdProcParalisados != null)
			this.qtdProcParalisados = qtdProcParalisados;
	}

	public String getPercProcParalisadoMesmoTipo() {
		return percProcParalisadoMesmoTipo;
	}

	public void setPercProcParalisadoMesmoTipo(String percProcParalisadoMesmoTipo) {
		if (percProcParalisadoMesmoTipo != null)
			this.percProcParalisadoMesmoTipo = percProcParalisadoMesmoTipo;
	}

	public String getPercProcParalisadoTodosTipos() {
		return percProcParalisadoTodosTipos;
	}

	public void setPercProcParalisadoTodosTipos(String percProcParalisadoTodosTipos) {
		if (percProcParalisadoTodosTipos != null)
			this.percProcParalisadoTodosTipos = percProcParalisadoTodosTipos;
	}
	
	//GET E SET DOS CAMPOS DA SEGUNDA PARTE DO RELATÓRIO **********************************************************
	public String getHoje() {
		return hoje;
	}

	public void setHoje(String hoje) {
		if (hoje != null)
			this.hoje = hoje;
	}

	public String getSemana() {
		return semana;
	}

	public void setSemana(String semana) {
		if (semana != null)
			this.semana = semana;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		if (mes != null)
			this.mes = mes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		if (ano != null)
			this.ano = ano;
	}
	

	public String getProcDistribuidoHoje() {
		return procDistribuidoHoje;
	}

	public void setProcDistribuidoHoje(String procDistribuidoHoje) {
		if (procDistribuidoHoje != null)
			this.procDistribuidoHoje = procDistribuidoHoje;
	}
	

	public String getProcDistribuidoSemana() {
		return procDistribuidoSemana;
	}

	public void setProcDistribuidoSemana(String procDistribuidoSemana) {
		if (procDistribuidoSemana != null)
			this.procDistribuidoSemana = procDistribuidoSemana;
	}

	public String getProcDistribuidoMes() {
		return procDistribuidoMes;
	}

	public void setProcDistribuidoMes(String procDistribuidoMes) {
		if (procDistribuidoMes != null)
			this.procDistribuidoMes = procDistribuidoMes;
	}

	public String getProcDistribuidoAno() {
		return procDistribuidoAno;
	}

	public void setProcDistribuidoAno(String procDistribuidoAno) {
		if (procDistribuidoAno != null)
			this.procDistribuidoAno = procDistribuidoAno;
	}
	
	public String getProcArquivadoHoje() {
		return procArquivadoHoje;
	}

	public void setProcArquivadoHoje(String procArquivadoHoje) {
		if (procArquivadoHoje != null)
			this.procArquivadoHoje = procArquivadoHoje;
	}

	public String getProcArquivadoSemana() {
		return procArquivadoSemana;
	}

	public void setProcArquivadoSemana(String procArquivadoSemana) {
		if (procArquivadoSemana != null)
			this.procArquivadoSemana = procArquivadoSemana;
	}

	public String getProcArquivadoMes() {
		return procArquivadoMes;
	}

	public void setProcArquivadoMes(String procArquivadoMes) {
		if (procArquivadoMes != null)
			this.procArquivadoMes = procArquivadoMes;
	}

	public String getProcArquivadoAno() {
		return procArquivadoAno;
	}

	public void setProcArquivadoAno(String procArquivadoAno) {
		if (procArquivadoAno != null)
			this.procArquivadoAno = procArquivadoAno;
	}
	
	public String getTempoMedioTramitacaoHoje() {
		return tempoMedioTramitacaoHoje;
	}

	public void setTempoMedioTramitacaoHoje(String tempoMedioTramitacaoHoje) {
		if (tempoMedioTramitacaoHoje != null)
			this.tempoMedioTramitacaoHoje = tempoMedioTramitacaoHoje;
	}

	public String getTempoMedioTramitacaoSemana() {
		return tempoMedioTramitacaoSemana;
	}

	public void setTempoMedioTramitacaoSemana(String tempoMedioTramitacaoSemana) {
		if (tempoMedioTramitacaoSemana != null)
			this.tempoMedioTramitacaoSemana = tempoMedioTramitacaoSemana;
	}

	public String getTempoMedioTramitacaoMes() {
		return tempoMedioTramitacaoMes;
	}

	public void setTempoMedioTramitacaoMes(String tempoMedioTramitacaoMes) {
		if (tempoMedioTramitacaoMes != null)
		this.tempoMedioTramitacaoMes = tempoMedioTramitacaoMes;
	}

	public String getTempoMedioTramitacaoAno() {
		return tempoMedioTramitacaoAno;
	}

	public void setTempoMedioTramitacaoAno(String tempoMedioTramitacaoAno) {
		if (tempoMedioTramitacaoAno != null)
			this.tempoMedioTramitacaoAno = tempoMedioTramitacaoAno;
	}
	
	public String getPercBalancoJudiciarioHoje() {
		return percBalancoJudiciarioHoje;
	}

	public void setPercBalancoJudiciarioHoje(String percBalancoJudiciarioHoje) {
		if (percBalancoJudiciarioHoje != null)
			this.percBalancoJudiciarioHoje = percBalancoJudiciarioHoje;
	}

	public String getPercBalancoJudiciarioSemana() {
		return percBalancoJudiciarioSemana;
	}

	public void setPercBalancoJudiciarioSemana(String percBalancoJudiciarioSemana) {
		if (percBalancoJudiciarioSemana != null)
			this.percBalancoJudiciarioSemana = percBalancoJudiciarioSemana;
	}

	public String getPercBalancoJudiciarioMes() {
		return percBalancoJudiciarioMes;
	}

	public void setPercBalancoJudiciarioMes(String percBalancoJudiciarioMes) {
		if (percBalancoJudiciarioMes != null)
			this.percBalancoJudiciarioMes = percBalancoJudiciarioMes;
	}

	public String getPercBalancoJudiciarioAno() {
		return percBalancoJudiciarioAno;
	}

	public void setPercBalancoJudiciarioAno(String percBalancoJudiciarioAno) {
		if (percBalancoJudiciarioAno != null)
			this.percBalancoJudiciarioAno = percBalancoJudiciarioAno;
	}
	
	//GET E SET VARIAVEIS DE CONTROLE DO RELATÓRIO***********************************************

	public String getId_Serventia() {
		return id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null)
			this.id_Serventia = id_Serventia;
	}
	
	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null)
			this.serventia = serventia;
	}
	
	public String getId_ProcessoTipo() {
		return id_ProcessoTipo;
	}

	public void setId_ProcessoTipo(String id_ProcessoTipo) {
		if (id_ProcessoTipo != null)
			this.id_ProcessoTipo = id_ProcessoTipo;
	}

	public String getProcessoTipo() {
		if (this.id_ProcessoTipo.length() > 0)
			return this.processoTipo;
		else
			return "Todos os Tipos";
	}

	public void setProcessoTipo(String processoTipo) {
		if (processoTipo != null)
			this.processoTipo = processoTipo;
	}
	

	public String getProcessoStatus() {
		return processoStatus;
	}

	public void setProcessoStatus(String processoStatus) {
		if (processoStatus != null)
			this.processoStatus = processoStatus;
	}
	
	public String getPrioridadeProcesso() {
		return prioridadeProcesso;
	}

	public void setPrioridadeProcesso(String prioridadeProcesso) {
		if (prioridadeProcesso != null)
			this.prioridadeProcesso = prioridadeProcesso;
	}
	
	public String getSegredoJustica() {
		return segredoJustica;
	}

	public void setSegredoJustica(String segredoJustica) {
		if (segredoJustica != null)
			this.segredoJustica = segredoJustica;
	}
	
	public String getSuspensoPrazo() {
		return suspensoPrazo;
	}

	public void setSuspensoPrazo(String suspensoPrazo) {
		if (suspensoPrazo != null)
			this.suspensoPrazo = suspensoPrazo;
	}
	
	public String getFaseProcesso() {
		return faseProcesso;
	}

	public void setFaseProcesso(String faseProcesso) {
		if (faseProcesso != null)
			this.faseProcesso = faseProcesso;
	}
	
	public String getDiasParalisados() {
		return diasParalisados;
	}

	public void setDiasParalisados(String diasParalisados) {
		if (diasParalisados != null)
			this.diasParalisados = diasParalisados;
	}
	
	
	
	public String getQtdProcContador() {
		return qtdProcContador;
	}

	public void setQtdProcContador(String qtdProcContador) {
		if (qtdProcContador != null)
			this.qtdProcContador = qtdProcContador;
	}

	public String getPercProcContadorMesmoTipo() {
		return percProcContadorMesmoTipo;
	}

	public void setPercProcContadorMesmoTipo(String percProcContadorMesmoTipo) {
		if (percProcContadorMesmoTipo != null)
			this.percProcContadorMesmoTipo = percProcContadorMesmoTipo;
	}

	public String getPercProcContadorTodosTipos() {
		return percProcContadorTodosTipos;
	}

	public void setPercProcContadorTodosTipos(String percProcContadorTodosTipos) {
		if (percProcContadorTodosTipos != null)
			this.percProcContadorTodosTipos = percProcContadorTodosTipos;
	}
	

	public String getQtdProcTurmaRecursais() {
		return qtdProcTurmaRecursais;
	}

	public void setQtdProcTurmaRecursais(String qtdProcTurmaRecursais) {
		if (qtdProcTurmaRecursais != null)
			this.qtdProcTurmaRecursais = qtdProcTurmaRecursais;
	}

	public String getPercProcTurmaRecursaisMesmoTipo() {
		return percProcTurmaRecursaisMesmoTipo;
	}

	public void setPercProcTurmaRecursaisMesmoTipo(String percProcTurmaRecursaisMesmoTipo) {
		if (percProcTurmaRecursaisMesmoTipo != null)
			this.percProcTurmaRecursaisMesmoTipo = percProcTurmaRecursaisMesmoTipo;
	}

	public String getPercProcTurmaRecursaisTodosTipos() {
		return PercProcTurmaRecursaisTodosTipos;
	}

	public void setPercProcTurmaRecursaisTodosTipos(String percProcTurmaRecursaisTodosTipos) {
		if (percProcTurmaRecursaisTodosTipos != null)
			PercProcTurmaRecursaisTodosTipos = percProcTurmaRecursaisTodosTipos;
	}

	//GET E SET VARIAVEIS DE CONTROLE DA SEGUNDA PARTE DO RELATÓRIO***********************************************
	public DiaHoraEventos getDataInicioDistribuicao() {
		return dataInicioDistribuicao;
	}

	public void setDataInicioDistribuicao(DiaHoraEventos dataInicioDistribuicao) {
		if (dataInicioDistribuicao != null)
			this.dataInicioDistribuicao = dataInicioDistribuicao;
	}

	public DiaHoraEventos getDataFinalDistribuicao() {
		return dataFinalDistribuicao;
	}

	public void setDataFinalDistribuicao(DiaHoraEventos dataFinalDistribuicao) {
		if (dataFinalDistribuicao != null)
			this.dataFinalDistribuicao = dataFinalDistribuicao;
	}

	public DiaHoraEventos getDataInicioArquivamento() {
		return dataInicioArquivamento;
	}

	public void setDataInicioArquivamento(DiaHoraEventos dataInicioArquivamento) {
		if (dataInicioArquivamento != null)
			this.dataInicioArquivamento = dataInicioArquivamento;
	}

	public DiaHoraEventos getDataFinalArquivamento() {
		return dataFinalArquivamento;
	}

	public void setDataFinalArquivamento(DiaHoraEventos dataFinalArquivamento) {
		if (dataFinalArquivamento != null)
			this.dataFinalArquivamento = dataFinalArquivamento;
	}

}
