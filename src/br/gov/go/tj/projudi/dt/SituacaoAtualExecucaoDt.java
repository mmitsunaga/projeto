package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class SituacaoAtualExecucaoDt extends Dados{

	private static final long serialVersionUID = -349323780630867063L;
	private String idSituacaoAtualExecucao;
	private String idLocalCumprimentoPena;
	private String localCumprimentoPena;
	private String idRegime;
	private String regime;
	private String idEventoExecucaoStatus;
	private String eventoExecucaoStatus;
	private String idFormaCumprimento;
	private String formaCumprimento;
	private List listaSituacaoAtualModalidadeDt;
	private List listaSituacaoAtualTipoPenaDt;
	private String dataAlteracao;
	private String idPenaExecucaoTipo;
	private String penaExecucaoTipo;
	private String idModalidade;
	private String modalidade;
	private String idProcesso;
	private String dataFuga;
	
	//para consulta
	private List listaIdLocal;
	private List listaIdRegime;
	private List listaIdModalidade;
	private List listaIdStatus;
	private List listaIdFormaCumprimento;
	private boolean excluirPRD;

	public SituacaoAtualExecucaoDt() {
		limpar();
	}
	public String getId()  {return idSituacaoAtualExecucao;}
	public void setId(String valor ) {if(valor!=null) idSituacaoAtualExecucao = valor;}
	
	public String getIdLocalCumprimentoPena() {return idLocalCumprimentoPena; }
	public void setIdLocalCumprimentoPena(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.idLocalCumprimentoPena = "";
		else if (!descricao.equalsIgnoreCase("")) this.idLocalCumprimentoPena = descricao;
	}
	
	public String getLocalCumprimentoPena() {return localCumprimentoPena; }
	public void setLocalCumprimentoPena(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.localCumprimentoPena = "";
		else if (!descricao.equalsIgnoreCase("")) this.localCumprimentoPena = descricao;
	}
	
	public String getIdRegime() {return idRegime; }
	public void setIdRegime(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.idRegime = "";
		else if (!descricao.equalsIgnoreCase("")) this.idRegime = descricao;
	}
	
	public String getRegime() {return regime; }
	public void setRegime(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.regime = "";
		else if (!descricao.equalsIgnoreCase("")) this.regime = descricao;
	}
	
	public String getIdEventoExecucaoStatus() {return idEventoExecucaoStatus; }
	public void setIdEventoExecucaoStatus(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.idEventoExecucaoStatus = "";
		else if (!descricao.equalsIgnoreCase("")) this.idEventoExecucaoStatus = descricao;
	}
	
	public String getEventoExecucaoStatus() {return eventoExecucaoStatus; }
	public void setEventoExecucaoStatus(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.eventoExecucaoStatus = "";
		else if (!descricao.equalsIgnoreCase("")) this.eventoExecucaoStatus = descricao;
	}
	
	public String getIdFormaCumprimento() {return idFormaCumprimento; }
	public void setIdFormaCumprimento(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.idFormaCumprimento = "";
		else if (!descricao.equalsIgnoreCase("")) this.idFormaCumprimento = descricao;
	}
	
	public String getFormaCumprimento() {return formaCumprimento; }
	public void setFormaCumprimento(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.formaCumprimento = "";
		else if (!descricao.equalsIgnoreCase("")) this.formaCumprimento = descricao;
	}
	
	public List getListaSituacaoAtualModalidadeDt() {return listaSituacaoAtualModalidadeDt; }
	public void setListaSituacaoAtualModalidadeDt( List lista) {
		this.listaSituacaoAtualModalidadeDt = lista;
	}
	public void addListaSituacaoAtualModalidadeDt(SituacaoAtualModalidadeDt modalidade) {
		if (this.listaSituacaoAtualModalidadeDt == null){
			this.listaSituacaoAtualModalidadeDt = new ArrayList();
		}
		this.listaSituacaoAtualModalidadeDt.add(modalidade);
	}
	
	public List getListaSituacaoAtualTipoPenaDt() {return listaSituacaoAtualTipoPenaDt; }
	public void setListaSituacaoAtualTipoPenaDt(List lista) {
		this.listaSituacaoAtualTipoPenaDt = lista;
	}
	public void addListaSituacaoAtualTipoPenaDt(SituacaoAtualTipoPenaDt pena) {
		if (this.listaSituacaoAtualTipoPenaDt == null){
			this.listaSituacaoAtualTipoPenaDt = new ArrayList();
		}
		this.listaSituacaoAtualTipoPenaDt.add(pena);
	}
	
	public String getDataAlteracao() {return dataAlteracao; }
	public void setDataAlteracao(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.dataAlteracao = "";
		else if (!descricao.equalsIgnoreCase("")) this.dataAlteracao = descricao;
	}
	
	public String getDataFuga() {return dataFuga; }
	public void setDataFuga(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.dataFuga = "";
		else this.dataFuga = descricao;
	}
	
	public String getIdPenaExecucaoTipo() {
		return idPenaExecucaoTipo;
	}
	public void setIdPenaExecucaoTipo(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.idPenaExecucaoTipo = "";
		else if (!descricao.equalsIgnoreCase("")) this.idPenaExecucaoTipo = descricao;
	}
	public String getPenaExecucaoTipo() {
		return penaExecucaoTipo;
	}
	public void setPenaExecucaoTipo(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.penaExecucaoTipo = "";
		else if (!descricao.equalsIgnoreCase("")) this.penaExecucaoTipo = descricao;
	}
	public String getIdModalidade() {
		return idModalidade;
	}
	public void setIdModalidade(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.idModalidade = "";
		else if (!descricao.equalsIgnoreCase("")) this.idModalidade = descricao;
	}
	public String getModalidade() {
		return modalidade;
	}
	public void setModalidade(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.modalidade = "";
		else if (!descricao.equalsIgnoreCase("")) this.modalidade = descricao;
	}
	
	public String getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(String descricao) {
		if (descricao != null) if (descricao.equalsIgnoreCase("null")) this.idProcesso = "";
		else if (!descricao.equalsIgnoreCase("")) this.idProcesso = descricao;
	}
	
	
	public List getListaIdLocal() {
		return listaIdLocal;
	}
	public void setListaIdLocal(List listaIdLocal) {
		this.listaIdLocal = listaIdLocal;
	}
	public void addListaIdLocal(String id){
		if (this.listaIdLocal == null) this.listaIdLocal = new ArrayList();
		listaIdLocal.add(id);
	}
	
	public List getListaIdRegime() {
		return listaIdRegime;
	}
	public void setListaIdRegime(List listaIdRegime) {
		this.listaIdRegime = listaIdRegime;
	}
	public void addListaIdRegime(String id){
		if (this.listaIdRegime == null) this.listaIdRegime = new ArrayList();
		listaIdRegime.add(id);
	}
	
	public List getListaIdModalidade() {
		return listaIdModalidade;
	}
	public void setListaIdModalidade(List listaIdModalidade) {
		this.listaIdModalidade = listaIdModalidade;
	}
	public void addListaIdModalidade(String id){
		if (this.listaIdModalidade == null) this.listaIdModalidade = new ArrayList();
		listaIdModalidade.add(id);
	}
	
	public List getListaIdStatus() {
		return listaIdStatus;
	}
	public void setListaIdStatus(List listaIdStatus) {
		this.listaIdStatus = listaIdStatus;
	}
	public void addListaIdStatus(String id){
		if (this.listaIdStatus == null) this.listaIdStatus = new ArrayList();
		listaIdStatus.add(id);
	}
	
	public List getListaIdFormaCumprimento() {
		return listaIdFormaCumprimento;
	}
	public void setListaIdFormaCumprimento(List listaIdFormaCumprimento) {
		this.listaIdFormaCumprimento = listaIdFormaCumprimento;
	}
	public void addListaIdFormaCumprimento(String id){
		if (this.listaIdFormaCumprimento == null) this.listaIdFormaCumprimento = new ArrayList();
		listaIdFormaCumprimento.add(id);
	}
	
	public boolean isExcluirPRD() {
		return excluirPRD;
	}
	public void setExcluirPRD(boolean excluirPRD) {
		this.excluirPRD = excluirPRD;
	}
	
	public void copiar(SituacaoAtualExecucaoDt objeto){
		idSituacaoAtualExecucao = objeto.getId();
		idLocalCumprimentoPena = objeto.getIdLocalCumprimentoPena();
		idRegime = objeto.getIdRegime();
		idEventoExecucaoStatus = objeto.getIdEventoExecucaoStatus();
		idFormaCumprimento = objeto.getIdFormaCumprimento();
		dataAlteracao = objeto.getDataAlteracao();
		idProcesso = objeto.getIdProcesso();
		dataFuga = objeto.getDataFuga();
	}

	public void limpar(){
		idSituacaoAtualExecucao = "";
		idLocalCumprimentoPena = "";
		localCumprimentoPena = "";
		idRegime = "";
		regime = "";
		idEventoExecucaoStatus = "";
		eventoExecucaoStatus = "";
		idFormaCumprimento = "";
		formaCumprimento = "";
		listaSituacaoAtualModalidadeDt = null;
		listaSituacaoAtualTipoPenaDt = null;
		dataAlteracao = "";
		idProcesso = "";
		idPenaExecucaoTipo = "";
		idModalidade = "";
		modalidade = "";
		penaExecucaoTipo = "";
		dataFuga = "";
		listaIdLocal = null;
		listaIdRegime = null;
		listaIdModalidade = null;
		listaIdStatus = null;
		listaIdFormaCumprimento = null;
		excluirPRD = false;
	}

	public String getPropriedades(){
		return "[idSituacaoAtualExecucao:" + idSituacaoAtualExecucao 
				+ ";idLocalCumprimentoPena:" + idLocalCumprimentoPena 
				+ ";idRegime:" + idRegime 
				+ ";idEventoExecucaoStatus:" + idEventoExecucaoStatus
				+ ";idFormaCumprimento:" + idFormaCumprimento
				+ ";dataAlteracao:" + dataAlteracao
				+ ";idProcesso:" + idProcesso
				+ ";dataFuga:" + dataFuga
				+ "]";
	}
} 
