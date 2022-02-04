package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.FabricaConexao;

public class TrocaResponsavelService {

	HttpServletRequest request;
	UsuarioNe UsuarioSessao;
	PendenciaResponsavelDt pendenciaResponsavelDt;
	PendenciaDt pendenciaDt;
	PendenciaResponsavelNe pendenciaResponsavelne;
	String Mensagem;
	boolean boDistribuicao;

	private void preencheVariaveis(TrocaResponsavelParam parameters) {
		request = parameters.getRequest();
		UsuarioSessao = parameters.getUsuarioSessao();
		pendenciaResponsavelDt = parameters.getPendenciaResponsavelDt();
		pendenciaDt = parameters.getPendenciaDt();
		pendenciaResponsavelne = parameters.getPendenciaResponsavelNe();
		Mensagem = parameters.getMensagem();
		boDistribuicao = parameters.isDistribuicao();
	}

	@SuppressWarnings("unchecked")
	public void execute(TrocaResponsavelParam parameters) throws Exception {
		preencheVariaveis(parameters);
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			fabrica.iniciarTransacao();
			List<PendenciaDt> listaPendenciasDistribuidas = new ArrayList<PendenciaDt>();
			if (request.getSession().getAttribute("listaPendenciasDistribuicao") != null)
				listaPendenciasDistribuidas.addAll(
						(Collection<PendenciaDt>) request.getSession().getAttribute("listaPendenciasDistribuicao"));

			TrocaResponsavelFactory factoryPend = new TrocaResponsavelFactoryPendencia();
			TrocaResponsavelFactory factoryProc = new TrocaResponsavelFactoryProcesso();

			TrocaResponsavel trocadorRespPend;
			TrocaResponsavel trocadorRespProc;

			// lrcampos 23/07/2019 - Alteração para possibilitar distribuir em lote
			if (listaPendenciasDistribuidas != null && !listaPendenciasDistribuidas.isEmpty()) {
				for (PendenciaDt pendenciaDtLista : listaPendenciasDistribuidas) {
					pendenciaResponsavelDt.setId_Pendencia(pendenciaDtLista.getId());
					parameters.setPendenciaDt(pendenciaDtLista);
					parameters.setPendenciaResponsavelDt(pendenciaResponsavelDt);
					trocadorRespPend = factoryPend.criarTrocaResponsavel(parameters);
					trocadorRespPend.trocarResponsavel();

					if (request.getSession().getAttribute("TrocaResponsavelProcesso") != null
							&& (boolean) request.getSession().getAttribute("TrocaResponsavelProcesso")) {
						trocadorRespProc = factoryProc.criarTrocaResponsavel(parameters);
						parameters.setListaDePendenciasDt(listaPendenciasDistribuidas);
						trocadorRespProc.trocarResponsavel();
					}
				}
			} else {
				parameters.setPendenciaDt(pendenciaDt);
				trocadorRespPend = factoryPend.criarTrocaResponsavel(parameters);
				trocadorRespPend.trocarResponsavel();

				trocadorRespProc = factoryProc.criarTrocaResponsavel(parameters);
				parameters.setListaDePendenciasDt(listaPendenciasDistribuidas);
				trocadorRespProc.trocarResponsavel();
			}
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
		}finally {
			fabrica.fecharConexao();
		}
	}

}
