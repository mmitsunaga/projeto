package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;

public class TrocaResponsavelParam {

	public static class Builder {
		
		TrocaResponsavelParam params;
		
		public Builder() {
			params = new TrocaResponsavelParam();
		}
		
		public Builder setRequest(HttpServletRequest request) {
			params.setRequest(request);
			return this;
		}
		
		public Builder setUsuarioSessao(UsuarioNe usuarioSessao) {
			params.setUsuarioSessao(usuarioSessao);
			return this;
		}
		
		public Builder setPendenciaDt(PendenciaDt pendenciaDt) {
			params.setPendenciaDt(pendenciaDt);
			return this;
		}
		
		public Builder setListaDePendenciasDt(List<PendenciaDt> listaDePendenciasDt) {
			params.setListaDePendenciasDt(listaDePendenciasDt);
			return this;
		}
		
		public Builder setPendenciaResponsavelDt(PendenciaResponsavelDt pendenciaResponsavelDt) {
			params.setPendenciaResponsavelDt(pendenciaResponsavelDt);
			return this;
		}
		
		public Builder setPendenciaResponsavelNe(PendenciaResponsavelNe pendenciaResponsavelNe) {
			params.setPendenciaResponsavelNe(pendenciaResponsavelNe);
			return this;
		}
		
		public Builder setDistribuicao(boolean distribuicao) {
			params.setDistribuicao(distribuicao);
			return this;
		}
		
		public Builder setMensagem(String mensagem) {
			params.setMensagem(mensagem);
			return this;
		}
		
		public Builder setProcessoResponsavelNe(ProcessoResponsavelNe processoResponsavelNe) {
			params.setProcessoResponsavelNe(processoResponsavelNe);
			return this;
		}

		public Builder setMovimentacaoNe(MovimentacaoNe movimentacaoNe) {
			params.setMovimentacaoNe(movimentacaoNe);
			return this;
		}
		
		public Builder setProcessoNe(ProcessoNe processoNe) {
			params.setProcessoNe(processoNe);
			return this;
		}
		
		public TrocaResponsavelParam build() {
			return this.params;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}

	HttpServletRequest request;
	UsuarioNe usuarioSessao;
	PendenciaDt pendenciaDt;
	List<PendenciaDt> listaDePendenciasDt;
	PendenciaResponsavelDt pendenciaResponsavelDt;
	PendenciaResponsavelNe pendenciaResponsavelNe;
	ProcessoResponsavelNe processoResponsavelNe;
	MovimentacaoNe movimentacaoNe;
	ProcessoNe processoNe;
	boolean distribuicao;
	String mensagem;

	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public UsuarioNe getUsuarioSessao() {
		return usuarioSessao;
	}
	
	public void setUsuarioSessao(UsuarioNe usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}
	
	public PendenciaDt getPendenciaDt() {
		return pendenciaDt;
	}
	
	public void setPendenciaDt(PendenciaDt pendenciaDt) {
		this.pendenciaDt = pendenciaDt;
	}
	
	public List<PendenciaDt> getListaDePendenciasDt() {
		return listaDePendenciasDt;
	}
	
	public void setListaDePendenciasDt(List<PendenciaDt> listaDePendenciasDt) {
		this.listaDePendenciasDt = listaDePendenciasDt;
	}
	
	public PendenciaResponsavelDt getPendenciaResponsavelDt() {
		return pendenciaResponsavelDt;
	}
	
	public void setPendenciaResponsavelDt(PendenciaResponsavelDt pendenciaResponsavelDt) {
		this.pendenciaResponsavelDt = pendenciaResponsavelDt;
	}
	
	public PendenciaResponsavelNe getPendenciaResponsavelNe() {
		return pendenciaResponsavelNe;
	}
	
	public void setPendenciaResponsavelNe(PendenciaResponsavelNe pendenciaResponsavelNe) {
		this.pendenciaResponsavelNe = pendenciaResponsavelNe;
	}
	
	public boolean isDistribuicao() {
		return distribuicao;
	}
	
	public void setDistribuicao(boolean distribuicao) {
		this.distribuicao = distribuicao;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public MovimentacaoNe getMovimentacaoNe() {
		return movimentacaoNe;
	}

	public void setMovimentacaoNe(MovimentacaoNe movimentacaoNe) {
		this.movimentacaoNe = movimentacaoNe;
	}

	public ProcessoNe getProcessoNe() {
		return processoNe;
	}

	public void setProcessoNe(ProcessoNe processoNe) {
		this.processoNe = processoNe;
	}

	public ProcessoResponsavelNe getProcessoResponsavelNe() {
		return processoResponsavelNe;
	}

	public void setProcessoResponsavelNe(ProcessoResponsavelNe processoResponsavelNe) {
		this.processoResponsavelNe = processoResponsavelNe;
	}


}
