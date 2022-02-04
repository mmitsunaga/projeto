package br.gov.go.tj.projudi.ne;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ListaConclusaoDt;
import br.gov.go.tj.projudi.dt.PaginaInicialDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.utils.Funcoes;

public class PaginaInicialPJDNe extends PaginaInicialNe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3600966320482770268L;

	@Override
	public PaginaInicialDt consultarDados(UsuarioNe usuarioNe) throws Exception {
		PaginaInicialDt paginaInicialDt = super.consultarDados(usuarioNe);
		
		// lrcampos - 14/10/2019 09:26 Configura os dados das sessões virtuais
		configurarDadosSessaoVirtual(paginaInicialDt, usuarioNe);
		
		configurarPendenciasSessaoVirtual(paginaInicialDt, usuarioNe);
	
		configurarDadosSessaoAguardandoAcordaoVirtual(paginaInicialDt, usuarioNe);
		
		return paginaInicialDt;
	}
	
	/**
	 * lrcampos 14/10/2019 14:10 - Prepara dados das sessões Virtuais e adiciona ao dt da PaginaInicial
	 * 
	 * @author lrcampos
	 * @param paginaInicialDt
	 * @param usuarioNe
	 * @throws Exception 
	 */
	
	protected void configurarDadosSessaoVirtual(PaginaInicialDt paginaInicialDt, UsuarioNe usuarioSessao) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();		
		
		List listaConclusoesSessaoNaoAnalisadasVirtual = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadasVirtual(usuarioSessao, true, false, true);
		List listaConclusoesSessaoPreAnalisadasVirtual = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasVirtual(usuarioSessao, true, false, true);
		List listaConclusoesSessaoAguardandoIniciarVirtual = pendenciaNe.consultarQuantidadeConclusoesAguardandoInicioSessaoVirtual(usuarioSessao, true, false, true);
		List listaConclusoesSessaoNaoAnalisadasVirtualNaoIniciada = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadasVirtual(usuarioSessao, true, false, false);
		List listaConclusoesSessaoPreAnalisadasVirtualNaoIniciada = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasVirtual(usuarioSessao, true, false, false);
		List listaConclusoesSessaoPreAnalisadasPresencial = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasVirtual(usuarioSessao, true, false, false, true);
		
		ListaConclusaoDt objConclusaoPresencial = new ListaConclusaoDt();
		if(!paginaInicialDt.getConclusoesSessao().isEmpty()) 
			objConclusaoPresencial =  (ListaConclusaoDt) paginaInicialDt.getConclusoesSessao().get(0);
		
		Integer qtdBoxPresencialVirtual = 0;
		if(paginaInicialDt.getQtdeTotalBoxPresencialSessaoVirtual() != null)
			qtdBoxPresencialVirtual = paginaInicialDt.getQtdeTotalBoxPresencialSessaoVirtual();
			

		for (int i = 0; i < listaConclusoesSessaoPreAnalisadasPresencial.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesSessaoPreAnalisadasPresencial.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoesSessao().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesSessao().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadasPresencialNaoInicilizada(Funcoes.StringToInt(conclusao[2]));
					qtdBoxPresencialVirtual += Funcoes.StringToInt(conclusao[2]);
					
				}
			}

			//Se não tinha uma conclusão analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo(conclusao[1]);
				objConclusao.setQtdePreAnalisadasVirtualNaoInicializada(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusaoSessaoVirtualNaoIniciada(objConclusao);
			}
		}
		
		for (int i = 0; i < listaConclusoesSessaoNaoAnalisadasVirtualNaoIniciada.size(); i++) {
			String[] conclusao = (String[]) listaConclusoesSessaoNaoAnalisadasVirtualNaoIniciada.get(i);			
			// Incializa o objeto para a lista de conclusões
			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
			// Id do tipo da pendencia, para poder montar os links
			objConclusao.setIdTipo(conclusao[0]);
			// Modifica o titulo da lista de conclusões
			objConclusao.setTitulo(conclusao[1]);
			objConclusao.setQtdeNaoAnalisadasVirtualNaoInicilizada(Funcoes.StringToInt(conclusao[2]));
			qtdBoxPresencialVirtual += Funcoes.StringToInt(conclusao[2]);
			// Adiciona a lista
			
			paginaInicialDt.adicionarConclusaoSessaoVirtualNaoIniciada(objConclusao);
		}
		
		for (int i = 0; i < listaConclusoesSessaoPreAnalisadasVirtualNaoIniciada.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesSessaoPreAnalisadasVirtualNaoIniciada.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoesSessaoVirtualNaoIniciada().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesSessaoVirtualNaoIniciada().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadasVirtualNaoInicializada(Funcoes.StringToInt(conclusao[2]));
					qtdBoxPresencialVirtual +=  Funcoes.StringToInt(conclusao[2]);
					
				}
			}

			//Se não tinha uma conclusão analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo(conclusao[1]);
				objConclusao.setQtdePreAnalisadasVirtualNaoInicializada(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusaoSessaoVirtualNaoIniciada(objConclusao);
			}
		}
		
		for (int i = 0; i < listaConclusoesSessaoNaoAnalisadasVirtual.size(); i++) {
			String[] conclusao = (String[]) listaConclusoesSessaoNaoAnalisadasVirtual.get(i);			
			// Incializa o objeto para a lista de conclusões
			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
			// Id do tipo da pendencia, para poder montar os links
			objConclusao.setIdTipo(conclusao[0]);
			// Modifica o titulo da lista de conclusões
			objConclusao.setTitulo(conclusao[1]);
			objConclusao.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[2]));
			// Adiciona a lista
			paginaInicialDt.adicionarConclusaoSessaoVirtual(objConclusao);
		}
		
		for (int i = 0; i < listaConclusoesSessaoAguardandoIniciarVirtual.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesSessaoAguardandoIniciarVirtual.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoesSessaoVirtual().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesSessaoVirtual().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se não tinha uma conclusão analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo(conclusao[1]);
				objConclusao.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusaoSessaoVirtual(objConclusao);
			}
		}
		
		for (int i = 0; i < listaConclusoesSessaoPreAnalisadasVirtual.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesSessaoPreAnalisadasVirtual.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoesSessaoVirtual().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesSessaoVirtual().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se não tinha uma conclusão analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo(conclusao[1]);
				objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusaoSessaoVirtual(objConclusao);
			}
		}
		ListaConclusaoDt objConclusaoSessaoVirtualNaoIniciada = new ListaConclusaoDt();
		if(!paginaInicialDt.getConclusoesSessaoVirtualNaoIniciada().isEmpty()) 
			objConclusaoSessaoVirtualNaoIniciada =  (ListaConclusaoDt) paginaInicialDt.getConclusoesSessaoVirtualNaoIniciada().get(0);
		
		paginaInicialDt.setQtdeTotalBoxPresencialSessaoVirtual(qtdBoxPresencialVirtual);

		// jvosantos - 08/01/2020 17:28 - Buscar contadores da pendência de erro material
		paginaInicialDt.setQtdeErroMaterialNaoAnalisadas(pendenciaNe.consultarQtdErroMaterial(false, usuarioSessao));
		paginaInicialDt.setQtdeErroMaterialPreAnalisadas(pendenciaNe.consultarQtdErroMaterial(true, false, usuarioSessao));
		paginaInicialDt.setQtdeErroMaterialAguardandoAssinatura(pendenciaNe.consultarQtdErroMaterial(true, true, usuarioSessao));
	}
	
	private void configurarPendenciasSessaoVirtual(PaginaInicialDt paginaInicialDt, UsuarioNe usuarioNe) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();
		int grupoTipo = usuarioNe.getGrupoTipoCodigoToInt();
		int qtde = 0;
		
		if (grupoTipo == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU ||grupoTipo == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU ||grupoTipo == GrupoTipoDt.JUIZ_TURMA || grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE || grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				|| grupoTipo == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipo == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU || grupoTipo == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
			String idServentiaCargo = Funcoes.getIdServentiaCargo(usuarioNe.getUsuarioDt());

			qtde = pendenciaNe.consultarQuantidadeAguardandoVotoSessaoVirtual(idServentiaCargo);

			paginaInicialDt.setQtdeVotoNaoAnalisadas(qtde);

			List pendencias = pendenciaNe.consultarQuantidadeSessaoVirtualTomarConhecimento(idServentiaCargo, null, 
					PendenciaTipoDt.PEDIDO_VISTA_SESSAO, PendenciaTipoDt.RESULTADO_UNANIME, PendenciaTipoDt.RETIRAR_PAUTA, PendenciaTipoDt.ADIADO_PELO_RELATOR, PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA, PendenciaTipoDt.RESULTADO_VOTACAO);
			paginaInicialDt.setTomarConhecimento(pendencias);

			VotoNe votoNe = new VotoNe();
			VotoEmLoteNe votoEmLoteNe = new VotoEmLoteNe();
			AudienciaProcessoNe audiProcNe = new AudienciaProcessoNe();
			PendenciaFinalizarVotoNe pendenciaFinalizarVotoNe = new PendenciaFinalizarVotoNe();
			
			qtde = votoNe.consultarQuantidadeVotosPreAnalisados(idServentiaCargo);
			paginaInicialDt.setQtdeVotoPreAnalisadas(qtde);
			qtde = votoEmLoteNe.consultarQuantidadeVotosPreAnalisadosEmLote(idServentiaCargo);
			paginaInicialDt.setQtdeVotosPreAnalisadosEmLote(qtde);
			qtde = votoNe.consultarQuantidadeVotosAguardandoAssinatura(idServentiaCargo);
			paginaInicialDt.setQtdeVotoAguardandoAssinatura(qtde);
			
			qtde = votoNe.consultarQuantidadePendenciasConhecimento(idServentiaCargo);
			paginaInicialDt.setQtdeConhecimentoNaoAnalisadas(qtde);
			qtde = votoNe.consultarQuantidadePendenciasConhecimentoPreAnalisadas(idServentiaCargo);
			paginaInicialDt.setQtdeConhecimentoPreAnalisadas(qtde);
			
			qtde = pendenciaFinalizarVotoNe.consultarQuantidadeFinalizarVoto(idServentiaCargo);
			paginaInicialDt.setQtdeFinalizarVotoNaoAnalisadas(qtde);
			qtde = pendenciaFinalizarVotoNe.consultarQuantidadeFinalizarVotoPreAnalisadas(idServentiaCargo);
			paginaInicialDt.setQtdeFinalizarVotoPreAnalisadas(qtde);
			qtde = pendenciaFinalizarVotoNe.consultarQuantidadeFinalizarVotoAssinatura(idServentiaCargo, usuarioNe);
			paginaInicialDt.setQtdeFinalizarVotoAguardandoAssinatura(qtde);
			
			// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
			qtde = pendenciaNe.consultarQuantidadeVerificarResultadoVotacao(idServentiaCargo);
			paginaInicialDt.setQtdeVerificarResultadoVotacaoNaoAnalisadas(qtde);
			qtde = pendenciaNe.consultarQuantidadeVerificarResultadoVotacaoPreAnalisadas(idServentiaCargo);
			paginaInicialDt.setQtdeVerificarResultadoVotacaoPreAnalisadas(qtde);
			qtde = pendenciaNe.consultarQuantidadeVerificarResultadoVotacaoAssinatura(idServentiaCargo);
			paginaInicialDt.setQtdeVerificarResultadoVotacaoAguardandoAssinatura(qtde);
			
			// jvosantos - 04/06/2019 10:05 - Adicionar consultas de quantidade de pendencias de renovar voto
			qtde = votoNe.consultarQuantidadeVotosRenovarNaoAnalisadas(idServentiaCargo);
			paginaInicialDt.setQtdeVotoRenovarNaoAnalisadas(qtde);
			qtde = votoNe.consultarQuantidadeVotosRenovarPreAnalisados(idServentiaCargo);
			paginaInicialDt.setQtdeVotoRenovarPreAnalisadas(qtde);
			qtde = votoNe.consultarQuantidadeVotosRenovarAguardandoAssinatura(idServentiaCargo);
			paginaInicialDt.setQtdeVotoRenovarAguardandoAssinatura(qtde);
			
			
			qtde = pendenciaNe.consultarQtdSustentacaoOral(idServentiaCargo);
			paginaInicialDt.setQtdSustentacaoOral(qtde);
			
			paginaInicialDt.setQtdeSessaoVirtualEmVotacao(audiProcNe.consultarQuantidadeEmVotacaoSessaoVirtual(idServentiaCargo));
			paginaInicialDt.setQtdeSessaoVirtualAcompanharVotacao(audiProcNe.consultarQuantidadeAcompanharVotacaoSessaoVirtual(idServentiaCargo) + audiProcNe.consultarQuantidadeAcompanharVotacaoSessaoVirtualErroMaterial(idServentiaCargo));
			paginaInicialDt.setQtdeSessaoVirtualVotoEmentaAguardandoAssinatura(audiProcNe.consultarQuantidadeVotoEmentaAguardandoAssinaturaSessaoVirtual(idServentiaCargo)); // jvosantos - 03/09/2019 14:26 - Adicionar contador de quantidade de sessões em andamento que o desembargador já votou
		}
	}
	
	private void configurarDadosSessaoAguardandoAcordaoVirtual(PaginaInicialDt paginaInicialDt, UsuarioNe usuarioNe) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();
		
		switch (usuarioNe.getGrupoTipoCodigoToInt()) {
			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSISTENTE_GABINETE:
			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				// jvosantos - 04/06/2019 10:05 - Adicionar consultas de quantidade de pendencias de apreciados
				long qtdeSessoesAguardandoAcordaoPreAnalisadosVirtual = audienciaNe.consultarQuantidadeSessoesVirtuaisPendentes(usuarioNe.getUsuarioDt(), "", true, false, true);
				long qtdeSessoesAguardandoAcordaoNaoAnalisadosVirtual = audienciaNe.consultarQuantidadeSessoesVirtuaisPendentes(usuarioNe.getUsuarioDt(), "", true, false, false);
				long qtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual = audienciaNe.consultarQuantidadeSessoesVirtuaisPendentes(usuarioNe.getUsuarioDt(), "", true, true, true);

				// jvosantos - 04/06/2019 10:05 - Adicionar consultas de quantidade de pendencias de apreciados
				paginaInicialDt.setQtdeSessoesAguardandoAcordaoNaoAnalisadasVirtual(qtdeSessoesAguardandoAcordaoNaoAnalisadosVirtual);
				paginaInicialDt.setQtdeSessoesAguardandoAcordaoPreAnalisadasVirtual(qtdeSessoesAguardandoAcordaoPreAnalisadosVirtual);
				paginaInicialDt.setQtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual(qtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual);
				break;		
				
		}
	}
	
}
