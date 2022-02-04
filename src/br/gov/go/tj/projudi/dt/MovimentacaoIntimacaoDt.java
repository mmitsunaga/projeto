package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.ValidacaoUtil;

public class MovimentacaoIntimacaoDt implements Serializable {

	private static final long serialVersionUID = -7777120838440691515L;
	
	private String id;
	
	private String tipoMovimentacao;
	
	private String dataRealizacao;
	
	private String complemento;
	
	private ProcessoDt processoDt;	
	
	private List<PendenciaDt> pendencias;
	
	private List<ArquivoDt> arquivos;
	
	public MovimentacaoIntimacaoDt(){
		
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getTipoMovimentacao() {
		return tipoMovimentacao;
	}
	
	public void setTipoMovimentacao(String tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public String getDataRealizacao() {
		return dataRealizacao;
	}
	
	public void setDataRealizacao(String dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}
	
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public List<PendenciaDt> getPendencias(){
		return pendencias;
	}

	public void setPendencias(List<PendenciaDt> pendencias){
		this.pendencias = pendencias;
	}
	
	public void addPendencia(PendenciaDt novaPendencia){
		if (pendencias == null) pendencias = new ArrayList<PendenciaDt>();
		this.pendencias.add(novaPendencia);
	}

	public List<ArquivoDt> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<ArquivoDt> arquivos) {
		this.arquivos = arquivos;
	}
	
	public void addArquivo(ArquivoDt novoArquivo){
		if (arquivos == null) arquivos = new ArrayList<ArquivoDt>();
		arquivos.add(novoArquivo);
	}
	
	public boolean isIntimacaoNormal(){
		return ValidacaoUtil.isNaoVazio(this.pendencias);
	}
	
	public boolean isIntimacaoAdvogadoExterno(){
		return ValidacaoUtil.isVazio(this.pendencias);
	}

}
