package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;

public abstract class TrocaResponsavelPendencia implements TrocaResponsavel {

	TrocaResponsavelParam params;
	PendenciaResponsavelDt pendenciaResponsavelDt;
	PendenciaResponsavelNe pendenciaResponsavelNe;
	PendenciaDt pendenciaDt;
	UsuarioNe usuarioSessao;
	HttpServletRequest request;
	String mensagem;
	
	protected void preencheParametros(TrocaResponsavelParam params) {
		this.pendenciaResponsavelDt = params.getPendenciaResponsavelDt();
		this.pendenciaResponsavelNe = params.getPendenciaResponsavelNe();
		this.pendenciaDt = params.getPendenciaDt();
		this.usuarioSessao = params.getUsuarioSessao();
		this.request = params.getRequest();
		this.mensagem = params.getMensagem();
	}

}
