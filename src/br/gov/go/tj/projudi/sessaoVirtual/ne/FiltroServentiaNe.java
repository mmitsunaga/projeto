package br.gov.go.tj.projudi.sessaoVirtual.ne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;

public class FiltroServentiaNe {
	
	public List<ServentiaDt> criaListaFiltroServCargo(List lista) {
		List<ServentiaDt> listaServentias = new ArrayList<ServentiaDt>();
		List<VotoSessaoLocalizarDt> listaVotos = (List<VotoSessaoLocalizarDt>)lista;
		listaVotos.stream().forEach(voto -> {
			preencheListaFiltroServentia(listaServentias, voto);
		});
		return listaServentias;
	}
	
	private void preencheListaFiltroServentia(List<ServentiaDt> listaServentias, VotoSessaoLocalizarDt voto) {
		ServentiaDt serventia = new ServentiaDt();
		serventia.setId(voto.getPendenciaDt().getCodigoTemp().split("@")[0]);
		serventia.setServentia(voto.getPendenciaDt().getCodigoTemp().split("@")[1]);
		boolean contemServentia = false;
		for(ServentiaDt a : listaServentias) {
			if(a.getId().equals(serventia.getId())) {
				contemServentia = true;
			}
		}
		if(!contemServentia) {
			listaServentias.add(serventia);
		}
	}
	
	public List<ServentiaDt> criaListaFiltroServCargoMinuta(List lista) {
		List<ServentiaDt> listaServentias = new ArrayList<ServentiaDt>();
		List<PendenciaDt> listaPend = (List<PendenciaDt>)lista;
		listaPend.stream().forEach(pend -> {
			preencheListaFiltroServentiaMinuta(listaServentias, pend);
		});
		return listaServentias;
	}
	
	private void preencheListaFiltroServentiaMinuta(List<ServentiaDt> listaServentias, PendenciaDt pend) {
		ServentiaDt serventia = new ServentiaDt();
		List<String> dadosServentia = new ArrayList<String>();
		dadosServentia.addAll(Arrays.asList(pend.getProcessoDt().getCodigoTemp().split("@")));
		serventia.setId(dadosServentia.get(0));
		serventia.setServentia(dadosServentia.get(1));
		boolean contemServentia = false;
		for(ServentiaDt a : listaServentias) {
			if(a.getId().equals(serventia.getId())) {
				contemServentia = true;
			}
		}
		if(!contemServentia) {
			listaServentias.add(serventia);
		}
	}
	
	public List<ServentiaDt> criaListaFiltroServCargoMinutaPreAnalisada(List lista) {
		List<ServentiaDt> listaServentias = new ArrayList<ServentiaDt>();
		List<PendenciaArquivoDt> listaPend = (List<PendenciaArquivoDt>)lista;
		listaPend.stream().forEach(pend -> {
			preencheListaFiltroServentiaMinutaPreAnalisada(listaServentias, pend);
		});
		return listaServentias;
	}
	
	private void preencheListaFiltroServentiaMinutaPreAnalisada(List<ServentiaDt> listaServentias, PendenciaArquivoDt pend) {
		ServentiaDt serventia = new ServentiaDt();
		serventia.setId(pend.getPendenciaDt().getProcessoDt().getCodigoTemp().split("@")[0]);
		serventia.setServentia(pend.getPendenciaDt().getProcessoDt().getCodigoTemp().split("@")[1]);
		boolean contemServentia = false;
		for(ServentiaDt a : listaServentias) {
			if(a.getId().equals(serventia.getId())) {
				contemServentia = true;
			}
		}
		if(!contemServentia) {
			listaServentias.add(serventia);
		}
	}
	
	
	
//	public List<ServentiaDt> consultarListaServentiaMinutaNaoAnalisada(UsuarioNe usuarioSessao, String posicaoPaginaAtual, Boolean isIniciada) throws Exception {
//		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//		List<ServentiaDt> listaServentias = new ArrayList<ServentiaDt>();
//		String idServCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
//		try{
//			if(Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
//				idServCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
//			}
//			FiltroServentiaPs filtroServentiaPs = new FiltroServentiaPs(fabricaConexao.getConexao());
//			filtroServentiaPs.consultarListaServentiaMinutaNaoAnalisada(posicaoPaginaAtual, idServCargo, isIniciada)
//					.ifPresent(c -> listaServentias.addAll(c));
//			return listaServentias;
//		} finally{
//			fabricaConexao.fecharConexao();
//		}
//	}

//	public List<ServentiaDt> buscaServentiasAguardandoVotoNaoAnalisadas(UsuarioNe usuarioSessao,
//			String posicaoPaginaAtual, boolean isRenovarVoto, boolean isPreAnalise) throws Exception{
//		Integer pendStatusCodigo, codigoTemp = null;
//		if(isRenovarVoto){
//			pendStatusCodigo = PendenciaStatusDt.ID_CORRECAO;
//			codigoTemp = isPreAnalise ? PendenciaStatusDt.ID_PRE_ANALISADA : PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO;
//		}else {
//			pendStatusCodigo = isPreAnalise ? PendenciaStatusDt.ID_EM_ANDAMENTO : PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO;
//		}
//		return consultarServentiaVoto(usuarioSessao, posicaoPaginaAtual, pendStatusCodigo, codigoTemp);
//	}
//	
//	public List<ServentiaDt> consultarServentiaVoto(UsuarioNe usuarioSessao, String posicaoPaginaAtual,
//			Integer pendStatusCodigo, Integer codigoTemp) throws Exception {
//		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//		List<ServentiaDt> listaServentias = new ArrayList<ServentiaDt>();
//		String idServCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
//		try {
//			if (Funcoes.StringToInt(
//					usuarioSessao.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
//				idServCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
//			}
//			FiltroServentiaPs filtroServentiaPs = new FiltroServentiaPs(fabricaConexao.getConexao());
//			filtroServentiaPs.consultarServentiaVoto(posicaoPaginaAtual, idServCargo, pendStatusCodigo, codigoTemp)
//					.ifPresent(c -> listaServentias.addAll(c));
//			return listaServentias;
//		} finally {
//			fabricaConexao.fecharConexao();
//		}
//
//	}
}
