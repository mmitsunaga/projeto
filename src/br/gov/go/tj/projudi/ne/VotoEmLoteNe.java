package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AnaliseVotoSessaoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ps.ArquivoPs;
import br.gov.go.tj.projudi.ps.VotoEmLotePs;
import br.gov.go.tj.projudi.ps.VotoPs;
import br.gov.go.tj.projudi.types.PendenciaStatusTipo;
import br.gov.go.tj.projudi.types.PendenciaTipo;
import br.gov.go.tj.projudi.types.VotoTipo;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

@SuppressWarnings("all")
public class VotoEmLoteNe {
	
	VotoNe votoNe = new VotoNe();

	public List<VotoSessaoLocalizarDt> consultaVotosPreAnalisadosEmLote(HttpServletRequest request, UsuarioNe usuario)
			throws Exception {
		request.setAttribute("tituloPagina", "Aguardando o Voto - Pr&eacute;-Analisadas (M&uacute;ltiplas)");
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoEmLotePs votoEmLotePs = new VotoEmLotePs(fabricaConexao.getConexao());
			ArquivoPs arquivoPs = new ArquivoPs(fabricaConexao.getConexao());
			String pendStatusCodigo = Integer.toString(PendenciaStatusTipo.ID_EM_ANDAMENTO.getValue());
			String pendTipoCodigo = Integer.toString(PendenciaTipo.VOTO_SESSAO.getValue());
			List<VotoSessaoLocalizarDt> votosEmLote = votoEmLotePs.consultaVotosPreAnaliseEmLoteOtimizado(
					usuario.getId_ServentiaCargo(), Integer.parseInt(pendStatusCodigo), Integer.parseInt(pendTipoCodigo),
					arquivoPs, usuario);
			votoNe.consultarVotosRelacionados(usuario, votosEmLote, fabricaConexao);
			return votosEmLote;
		} finally {
			fabricaConexao.fecharConexao();
		}
	}

	public int consultarQuantidadeVotosPreAnalisadosEmLote(String idServentiaCargo) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		int quantidade = 0; 
		try {
			VotoEmLotePs votoPs = new VotoEmLotePs(fabricaConexao.getConexao());
			int pendStatusCodigo = PendenciaStatusTipo.ID_EM_ANDAMENTO.getValue();
			int pendTipoCodigo = PendenciaTipo.VOTO_SESSAO.getValue();
			 quantidade = votoPs.consultaQtdeVotosPreAnaliseEmLote(idServentiaCargo, pendStatusCodigo, pendTipoCodigo);
		} finally{
			fabricaConexao.fecharConexao();
		}
		return quantidade;
	}

	public void montaParaSalvarAguardandoVotoPreAnalisadasMultiplas(HttpServletRequest request, UsuarioNe usuario) {
		boolean isRenovarVoto = Funcoes.objectToBoolean(request.getAttribute("renovar"));
		boolean isAguardandoAssinatura = Funcoes.objectToBoolean(request.getAttribute("guardarAssinar"));
		List<AnaliseVotoSessaoDt> listaAnaliseVotoSessao = (List<AnaliseVotoSessaoDt>) request.getSession()
				.getAttribute("listaAnaliseVotoSessao");
		List<ArquivoDt> lista = Funcoes.converterMapParaList((Map) request.getSession().getAttribute("ListaArquivos"));
		ArquivoDt arquivo = new ArquivoDt();

		if (!CollectionUtils.isEmpty(lista)) {
			arquivo = (ArquivoDt) lista.get(0);
			arquivo.setId_UsuarioLog(usuario.getId_Usuario());
		} else {
			arquivo = null;
		}

		for (AnaliseVotoSessaoDt analiseVoto : listaAnaliseVotoSessao) {
			analiseVoto.setRenovarVoto(isRenovarVoto);
			analiseVoto.setAguardandoAssinatura(isAguardandoAssinatura);
			analiseVoto.setTextoVoto(request.getParameter("TextoEditor"));
			analiseVoto.setNomeArquivo(request.getParameter("nomeArquivo"));
			analiseVoto.setIdArquivoTipo(request.getParameter("Id_ArquivoTipo"));
			analiseVoto.setRenovarVoto(Funcoes.StringToBoolean(request.getParameter("renovar")));
			analiseVoto.setCapaDoProcesso(StringUtils.defaultIfEmpty(request.getParameter("capaDoProcesso"), ""));
			VotoDt voto = analiseVoto.getVoto();
			if (voto == null) {
				voto = new VotoDt();
				analiseVoto.setVoto(voto);
			}
			voto.setIdVotoTipo(request.getParameter("idVotoTipo"));
			voto.setVotoTipo(request.getParameter("votoTipo"));
			voto.setId_UsuarioLog(usuario.getId_Usuario());
			voto.setVotoTipoCodigo(request.getParameter("votoTipoCodigo"));
			voto.setIpComputadorLog(usuario.getIpComputadorLog());
			try {
				voto.setArquivo(arquivo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void salvarVotosDeAguardandoVotoPreAnalisadasMultiplas(HttpServletRequest request, UsuarioNe usuario,
			boolean isUpdatePreAnalise) {
		List<AnaliseVotoSessaoDt> listaAnaliseVotoSessao = (List<AnaliseVotoSessaoDt>) request.getSession()
				.getAttribute("listaAnaliseVotoSessao");
		ArquivoDt arquivo = new ArquivoDt();
		String idArquivo = arquivo.getId();
		VotoNe votoNe = new VotoNe();
		VotoDt voto = new VotoDt();
		for (AnaliseVotoSessaoDt analiseVoto : listaAnaliseVotoSessao) {
			voto = analiseVoto.getVoto();
			boolean hasArquivo = false;
			if (voto.getArquivo() != null) {
				hasArquivo = true;
			}
			if (StringUtils.isNotEmpty(idArquivo)) {
				voto.setArquivo(arquivo);
			}
			try {
				if (hasArquivo) {
					salvarVotoSessaoEmLote(voto, usuario.getUsuarioDt());
				} else {
					salvarPreAnaliseEmLote(analiseVoto, usuario, isUpdatePreAnalise);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			arquivo = voto.getArquivo();
			idArquivo = arquivo.getId();
		}
	}

	public void salvarVotoSessaoEmLote(VotoDt voto, UsuarioDt usuario) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
			inserirArquivos(voto, usuario, fabrica, true);
			votoNe.inserirVotoDesativandoAntigo(voto, fabrica);
			votoNe.finalizarPendenciaVoto(voto.getPendenciaDt(), voto, voto.getIdProcesso(), usuario, fabrica);
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}

	public void salvarPreAnaliseEmLote(AnaliseVotoSessaoDt analiseVoto, UsuarioNe usuario, boolean isUpdatePreAnalise)
			throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
			VotoDt voto = analiseVoto.getVoto();
			ArquivoDt arquivo = votoNe.montarArquivoPreAnalise(analiseVoto, usuario, voto.getArquivo());
			if (voto.getArquivo() == null || StringUtils.isEmpty(voto.getArquivo().getId())) {
				voto.setArquivo(arquivo);
			}
			inserirArquivos(voto, usuario.getUsuarioDt(), fabrica, isUpdatePreAnalise);
			if (analiseVoto.isRenovarVoto()) {
				if (analiseVoto.isAguardandoAssinatura()) {
					voto.getPendenciaDt().setCodigoTemp(String
							.valueOf(PendenciaStatusTipo.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP.getValue()));
					new PendenciaNe().AlterarCodigoTempPendencia(voto.getPendenciaDt(), fabrica);
				} else {
					voto.getPendenciaDt()
							.setCodigoTemp(String.valueOf(PendenciaStatusTipo.ID_PRE_ANALISADA.getValue()));
					new PendenciaNe().AlterarCodigoTempPendencia(voto.getPendenciaDt(), fabrica);
				}
			} else {
				if (analiseVoto.isAguardandoAssinatura()) {
					voto.getPendenciaDt()
							.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusTipo.ID_PRE_ANALISADA.getValue()));
					new PendenciaNe().alterarStatus(voto.getPendenciaDt());
				} else {
					voto.getPendenciaDt()
							.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusTipo.ID_EM_ANDAMENTO.getValue()));
					new PendenciaNe().alterarStatus(voto.getPendenciaDt());
				}
			}
			votoNe.inserirVotoDesativandoAntigo(voto, fabrica);
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}

	public boolean verificaRelatoriaDiferente(List<AnaliseVotoSessaoDt> listaAnaliseVotoSessao) {
		if (CollectionUtils.isEmpty(listaAnaliseVotoSessao) || listaAnaliseVotoSessao.get(0) == null) {
			return false;
		} else {
			String relator1 = listaAnaliseVotoSessao.get(0).getNomeRelator();
			return listaAnaliseVotoSessao.stream().map(AnaliseVotoSessaoDt::getNomeRelator)
					.filter(relator2 -> !relator1.contentEquals(relator2)).findAny().isPresent();
		}
	}

	public List<ArquivoDt> inserirArquivos(VotoDt voto, UsuarioDt usuario, FabricaConexao fabrica,
			boolean isUpdatePreAnalise) throws Exception {
		List<ArquivoDt> listaArquivos = new ArrayList<>();
		if (!isUpdatePreAnalise) {
			listaArquivos.add(voto.getArquivo());
			if (voto.getEmenta() != null) {
				listaArquivos.add(voto.getEmenta());
			}
			if (voto.getConfiguracao() != null) {
				listaArquivos.add(voto.getConfiguracao());
			}
			new PendenciaNe().inserirArquivos(voto.getPendenciaDt(), listaArquivos, false, usuario, fabrica);
		} else {
			ArquivoNe arquivoNe = new ArquivoNe();
			arquivoNe.salvar(voto.getArquivo(), fabrica);
			if (voto.getEmenta() != null) {
				arquivoNe.salvar(voto.getEmenta(), fabrica);
			}
			if (voto.getConfiguracao() != null) {
				arquivoNe.salvar(voto.getConfiguracao(), fabrica);
			}
		}
		return listaArquivos;
	}

	public ModeloDt consultarModeloId(String idModelo, List<ProcessoDt> listaProcessoDt, UsuarioDt usuarioDt)
			throws Exception {
		ModeloNe modeloNe = new ModeloNe();
		ModeloDt modeloDt = modeloNe.consultarId(idModelo);
		String texto = montaTexto(idModelo, listaProcessoDt, usuarioDt, modeloNe);
		modeloDt.setTexto(texto);
		return modeloDt;
	}

	private String montaTexto(String idModelo, List<ProcessoDt> listaProcessoDt, UsuarioDt usuarioDt, ModeloNe modeloNe)
			throws Exception {
		String texto = "";
		if (idModelo.contentEquals("25912")) {
			texto += "<p>\n Eu, ${usuario.chefe}, ACOMPANHO O RELATOR no julgamento do(s) seguinte(s) processo(s):\n";
		} else if (idModelo.contentEquals("26026")) {
			texto += "<p>\n Eu, ${usuario.chefe}, ACOMPANHO A DIVERGÊNCIA, Des(a). ${audienciaSessao.redator} no julgamento do(s) seguinte(s) processo(s):\n";
		} else if (idModelo.contentEquals("26060")) {
			texto += "<p>\n Eu, Des(a). ${usuario.chefe}, por motivo de foro íntimo, dou-me por suspeito(a) para o julgamento do(s) seguinte(s) processo(s):\n";
		}

		texto += ("<table>\n" + "	<tr>\n" + "		<td>Processo</td>\n" + "		<td>Classe</td>\n"
				+ "		<td>Data/Hora do In&iacute;cio da Sess&atilde;o</td>\n"
				+ "		<td>Data/Hora do Fim da Sess&atilde;o</td>\n" + "	</tr>\n" + "	<tr>\n"
				+ "		<td>${processo.numero}</td>\n" + "		<td>${processo.classe.cnj}</td>\n"
				+ "		<td>${cumprimento.audiencia.data} ${cumprimento.audiencia.hora}</td>\n"
				+ "		<td>${sessao.horaFinal}</td>\n" + "	</tr>\n" + "</table>\n" + "${data} ${hora}\n" + "</p>");

		String LISTA_PROCESSO_AUDIENCIA_SESSAO = "\n	<tr>\n" + "		<td>${processo.numero}</td>\n"
				+ "		<td>${processo.classe.cnj}</td>\n"
				+ "		<td>${cumprimento.audiencia.data} ${cumprimento.audiencia.hora}</td>\n"
				+ "		<td>${sessao.horaFinal}</td>\n" + "	</tr>";
		String DATA_HORA = "\n</table>\n${data} ${hora}\n</p>";

		texto = texto.replace(DATA_HORA, "");
		texto = modeloNe.montaUsuario(usuarioDt, texto);

		for (ProcessoDt processoDt : listaProcessoDt) {
			texto = modeloNe.montaProcesso(processoDt, texto, "");
			texto = modeloNe.montaAudiencia(processoDt.getId_Processo(), texto);
			texto += LISTA_PROCESSO_AUDIENCIA_SESSAO;
		}
		texto = texto.replace(LISTA_PROCESSO_AUDIENCIA_SESSAO, "");
		texto += DATA_HORA;
		texto = modeloNe.montaOutros(texto);
		texto = modeloNe.montaRedator(listaProcessoDt.get(0), texto);
		return texto;
	}

	public static List<Integer> montarListaIgnorarPreAnaliseEmLote() throws Exception {
		List<Integer> listaIgnorar = new ArrayList<Integer>();
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoEmLotePs consultor = new VotoEmLotePs(fabrica.getConexao());
			listaIgnorar = consultor.consultaTiposVoto();
			listaIgnorar.remove(new Integer(VotoTipo.IMPEDIDO.getValue()));
			listaIgnorar.remove(new Integer(VotoTipo.SUSPEICAO.getValue()));
			listaIgnorar.remove(new Integer(VotoTipo.ACOMPANHA_RELATOR.getValue()));
			listaIgnorar.remove(new Integer(VotoTipo.PEDIDO_VISTA.getValue()));
			return listaIgnorar;
		} finally {
			fabrica.fecharConexao();
		}
	}

	public AnaliseVotoSessaoDt corrigeClasseProcesso(AnaliseVotoSessaoDt analiseVoto) throws Exception {
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		String idAudiProc = analiseVoto.getVoto().getAudienciaProcessoDt().getId();
		String tipoClasseAudiProc = processoParteNe.consultaClasseProcessoIdAudiProc(idAudiProc);
		analiseVoto.getVoto().getAudienciaProcessoDt().getProcessoDt().setProcessoTipo(tipoClasseAudiProc);
		return analiseVoto;
	}
}
