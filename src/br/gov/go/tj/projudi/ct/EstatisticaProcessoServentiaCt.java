package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaProcessoServentiaDt;
import br.gov.go.tj.projudi.ne.EstatisticaProcessoServentiaNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.DiaHoraEventos;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaProcessoServentiaCt extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -7005216073000114230L;

    public  EstatisticaProcessoServentiaCt() {

	} 
	
	public int Permissao() {
		return EstatisticaProcessoServentiaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		//****************
		EstatisticaProcessoServentiaDt EstatisticaProcessoServentiadt;
		EstatisticaProcessoServentiaNe EstatisticaProcessoServentiane;
		ServentiaNe serventiaNe;
		ProcessoTipoNe processoTipoNe;
		//******************************

		List tempList = null;
		byte[] byTemp = null;

		request.setAttribute("tempPrograma","EstatisticaProcessoServentia");
		request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
		request.setAttribute("tempBuscaServentia","Serventia");
		request.setAttribute("tempBuscaId_ProcessoTipo","Id_ProcessoTipo");
		request.setAttribute("tempBuscaProcessoTipo","ProcessoTipo");

		EstatisticaProcessoServentiane =(EstatisticaProcessoServentiaNe)request.getSession().getAttribute("EstatisticaProcessoServentiane");
		if (EstatisticaProcessoServentiane == null )  
			EstatisticaProcessoServentiane = new EstatisticaProcessoServentiaNe();  

		EstatisticaProcessoServentiadt =(EstatisticaProcessoServentiaDt)request.getSession().getAttribute("EstatisticaProcessoServentiadt");
		if (EstatisticaProcessoServentiadt == null )  
			EstatisticaProcessoServentiadt = new EstatisticaProcessoServentiaDt();
		
		EstatisticaProcessoServentiadt.setId_Serventia( request.getParameter("Id_Serventia")); 
		EstatisticaProcessoServentiadt.setServentia( request.getParameter("Serventia"));
		EstatisticaProcessoServentiadt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo"));
		EstatisticaProcessoServentiadt.setProcessoTipo( request.getParameter("ProcessoTipo"));


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("Curinga","vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Imprimir:
				if (EstatisticaProcessoServentiadt.getId_Serventia().length() > 0){
					
					//QUANTDIADE DE PROCESSOS ATIVOS DA SERVENTIA COM TIPO PROCESSO SELECIONADO*******************
					int qtdProcAtivosServentiaTP = EstatisticaProcessoServentiane.consultaQtdProcessosAtivos(EstatisticaProcessoServentiadt.getId_Serventia(),EstatisticaProcessoServentiadt.getId_ProcessoTipo());
					
					//QUANTIDADE TOTAL DE PROCESSOS ATIVOS DA SERVENTIA*******************************************
					int qtdProcAtivosServentia = EstatisticaProcessoServentiane.consultaQtdProcessosAtivos(EstatisticaProcessoServentiadt.getId_Serventia(),"");
					EstatisticaProcessoServentiadt.setQtdProcAtivosTotal(String.valueOf(qtdProcAtivosServentia));
					
					//PROCESSOS ATIVOS*****************************************************************************
					EstatisticaProcessoServentiadt.setQtdProcAtivos(String.valueOf(qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcAtivosMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcAtivosServentiaTP, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcAtivosTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcAtivosServentiaTP, qtdProcAtivosServentia));

					//PARTES MAIORES DE 60 ANOS *** 1 - CODIGO_PRIORIDADE_MAIOR_60_ANOS****************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setPrioridadeProcesso("1");
					EstatisticaProcessoServentiadt.setProcessoStatus("2"); //2 - ATIVO
					int qtdProcessoMaiores60Anos = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcMaiores60(String.valueOf(qtdProcessoMaiores60Anos));
					EstatisticaProcessoServentiadt.setPercProcMaiores60MesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessoMaiores60Anos, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcMaiores60TodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessoMaiores60Anos, qtdProcAtivosServentia));
					
					//SEGREDO DE JUSTIÇA ***************************************************************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setSegredoJustica("1");
					EstatisticaProcessoServentiadt.setProcessoStatus("2"); //2 - ATIVO
					int qtdProcessoSegredoJustica = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcSegredoJustica(String.valueOf(qtdProcessoSegredoJustica));
					EstatisticaProcessoServentiadt.setPercProcSegredoJusticaMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessoSegredoJustica,qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcSegredoJusticaTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessoSegredoJustica,qtdProcAtivosServentia));
					
					//FASE DE CONHECIMENTO 1 - CODIGO_FASE_CONHECIMENTO *********************************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setFaseProcesso("1");
					EstatisticaProcessoServentiadt.setProcessoStatus("2"); //2 - ATIVO
					int qtdProcessoFaseConhecimento = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcFaseConhecimento(String.valueOf(qtdProcessoFaseConhecimento));
					EstatisticaProcessoServentiadt.setPercProcFaseConhecimentoMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessoFaseConhecimento, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcFaseConhecimentoTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessoFaseConhecimento, qtdProcAtivosServentia));

					//FASE DE EXECUÇÃO 2 - CODIGO_FASE_EXECUÇÃO ******************************************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setFaseProcesso("2");
					EstatisticaProcessoServentiadt.setProcessoStatus("2"); //2 - ATIVO
					int qtdProcessoFaseExecucao = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcFaseExecucao(String.valueOf(qtdProcessoFaseExecucao));
					EstatisticaProcessoServentiadt.setPercProcFaseExecucaoMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessoFaseExecucao, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcFaseExecucaoTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessoFaseExecucao, qtdProcAtivosServentia));
					
					//SUSPENSO SEM PRAZO******************************************************************************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setSuspensoPrazo("0"); //AUSENCIA_DE_PRAZO = 0;	
					int qtdProcessosSemPrazo = EstatisticaProcessoServentiane.consultaQtdProcessosSuspensos(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcSuspensoSemPrazo(String.valueOf(qtdProcessosSemPrazo));
					EstatisticaProcessoServentiadt.setPercProcSuspensoSemPrazoMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessosSemPrazo, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcSuspensoSemPrazoTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessosSemPrazo, qtdProcAtivosServentia));
					
					//SUSPENSO COM PRAZO****************************************************************************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setSuspensoPrazo("1"); //PRESENCA_DE_PRAZO = 1;	
					int qtdProcessosComPrazo = EstatisticaProcessoServentiane.consultaQtdProcessosSuspensos(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcSuspensoPrazo(String.valueOf(qtdProcessosComPrazo));
					EstatisticaProcessoServentiadt.setPercProcSuspensoPrazoMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessosComPrazo, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcSuspensoPrazoTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessosComPrazo, qtdProcAtivosServentia));
					
					//PROCESSOS SEMI-PARALISADOS - 20 DIAS PROCESSO SEMI_PARALISADO************************************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setDiasParalisados("20");
					int qtdProcessoSemiParalisados = EstatisticaProcessoServentiane.consultaQtdProcessosProcessosParalisados(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcSemiParalisados(String.valueOf(qtdProcessoSemiParalisados));
					EstatisticaProcessoServentiadt.setPerctProcSemiParalisadoMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessoSemiParalisados, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPerctProcSemiParalisadoTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessoSemiParalisados, qtdProcAtivosServentia));
					
					//PROCESSOS PARALISADOS - 30 DIAS PROCESSO PARALISADO**********************************************************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setDiasParalisados("30");
					int qtdProcessoParalisados = EstatisticaProcessoServentiane.consultaQtdProcessosProcessosParalisados(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setQtdProcParalisados(String.valueOf(qtdProcessoParalisados));
					EstatisticaProcessoServentiadt.setPercProcParalisadoMesmoTipo(EstatisticaProcessoServentiane.getPercentual(qtdProcessoParalisados, qtdProcAtivosServentiaTP));
					EstatisticaProcessoServentiadt.setPercProcParalisadoTodosTipos(EstatisticaProcessoServentiane.getPercentual(qtdProcessoParalisados, qtdProcAtivosServentia));
					
					//SEGUNDA PARTE DO RELATÓRIO ******************************************************************************************
					DiaHoraEventos dataInicioHoje = DiaHoraEventos.getDataInicioDiaHoje();
					DiaHoraEventos dataFimHoje = DiaHoraEventos.getDataFimDiaHoje();

					DiaHoraEventos dataInicioSemana = DiaHoraEventos.getDataInicioSemana();
					DiaHoraEventos dataFimSemana = DiaHoraEventos.getDataFimSemana();

					DiaHoraEventos dataInicioMes = DiaHoraEventos.getDataInicioMes();
					DiaHoraEventos dataFimMes = DiaHoraEventos.getDataFimMes();
					
					DiaHoraEventos dataInicioAno = DiaHoraEventos.getDataInicioAno();
					DiaHoraEventos dataFimAno= DiaHoraEventos.getDataFimAno();
					
					// CABEÇALHO DA SEGUANDA PARTE DO RELATÓRIO****************************************************************************
					EstatisticaProcessoServentiadt.setHoje("("+dataInicioHoje.dataNacionalCurto()+")");
					EstatisticaProcessoServentiadt.setSemana("("+dataInicioSemana.dataNacionalCurto()+" - "+dataFimSemana.dataNacionalCurto()+")");
					EstatisticaProcessoServentiadt.setMes("("+dataInicioMes.dataNacionalCurto()+" - "+dataFimMes.dataNacionalCurto()+")");
					EstatisticaProcessoServentiadt.setAno("("+String.valueOf(dataInicioAno.getAno())+")");
					
					//PROCESSOS DITRIBUÍDOS**************************************************************************
					//HOJE**********
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setDataInicioDistribuicao(dataInicioHoje);
					EstatisticaProcessoServentiadt.setDataFinalDistribuicao(dataFimHoje);
					int qtdProcessosDistribuidosHoje = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcDistribuidoHoje(String.valueOf(qtdProcessosDistribuidosHoje));
					//SEMANA*********
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setDataInicioDistribuicao(dataInicioSemana);
					EstatisticaProcessoServentiadt.setDataFinalDistribuicao(dataFimSemana);
					int qtdProcessosDistribuidosSemana = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcDistribuidoSemana(String.valueOf(qtdProcessosDistribuidosSemana));
					//MES*************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setDataInicioDistribuicao(dataInicioMes);
					EstatisticaProcessoServentiadt.setDataFinalDistribuicao(dataFimMes);
					int qtdProcessosDistribuidosMes = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcDistribuidoMes(String.valueOf(qtdProcessosDistribuidosMes));
					//ANO*************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setDataInicioDistribuicao(dataInicioAno);
					EstatisticaProcessoServentiadt.setDataFinalDistribuicao(dataFimAno);
					int qtdProcessosDistribuidosAno = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcDistribuidoAno(String.valueOf(qtdProcessosDistribuidosAno));
					
					//PROCESSOS ARQUIVADOS************************************************************************************
					//HOJE**********
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioHoje);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimHoje);
					int qtdProcessosArquivadosHoje = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcArquivadoHoje(String.valueOf(qtdProcessosArquivadosHoje));
					//SEMANA*********
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioSemana);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimSemana);
					int qtdProcessosArquivadosSemana = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcArquivadoSemana(String.valueOf(qtdProcessosArquivadosSemana));
					//MES*************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioMes);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimMes);
					int qtdProcessosArquivadosMes = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcArquivadoMes(String.valueOf(qtdProcessosArquivadosMes));
					//ANO*************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioAno);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimAno);
					int qtdProcessosArquivadosAno = EstatisticaProcessoServentiane.consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setProcArquivadoAno(String.valueOf(qtdProcessosArquivadosAno));
					
					// MÉDIA DE TRAMITAÇÃO DE PROCESSOS************************************************************************************
					//HOJE**********
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioHoje);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimHoje);
					String mediaDiasEmTramitacaoArquivadosHoje = EstatisticaProcessoServentiane.mediaDiasEmTramitacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setTempoMedioTramitacaoHoje(mediaDiasEmTramitacaoArquivadosHoje);
					//SEMANA**********
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioSemana);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimSemana);
					String mediaDiasEmTramitacaoArquivadosSemana = EstatisticaProcessoServentiane.mediaDiasEmTramitacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setTempoMedioTramitacaoSemana(mediaDiasEmTramitacaoArquivadosSemana);
					//MÊS****************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioMes);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimMes);
					String mediaDiasEmTramitacaoArquivadosMes = EstatisticaProcessoServentiane.mediaDiasEmTramitacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setTempoMedioTramitacaoMes(mediaDiasEmTramitacaoArquivadosMes);
					//ANO****************
					EstatisticaProcessoServentiadt.limparParametrosConsulta();
					EstatisticaProcessoServentiadt.setProcessoStatus("1"); //1 - ARQUIVADO
					EstatisticaProcessoServentiadt.setDataInicioArquivamento(dataInicioAno);
					EstatisticaProcessoServentiadt.setDataFinalArquivamento(dataFimAno);
					String mediaDiasEmTramitacaoArquivadosAno = EstatisticaProcessoServentiane.mediaDiasEmTramitacao(EstatisticaProcessoServentiadt);
					EstatisticaProcessoServentiadt.setTempoMedioTramitacaoAno(mediaDiasEmTramitacaoArquivadosAno);
					
					//CÁLCULANDO A BALANÇAJUDICIARIA (ARQUIVADOS/ARQUIVADOS+DISTRIBUIDOS)
					EstatisticaProcessoServentiadt.setPercBalancoJudiciarioHoje(EstatisticaProcessoServentiane.getPercentual(qtdProcessosArquivadosHoje, qtdProcessosDistribuidosHoje));
					EstatisticaProcessoServentiadt.setPercBalancoJudiciarioSemana(EstatisticaProcessoServentiane.getPercentual(qtdProcessosArquivadosSemana, qtdProcessosDistribuidosSemana));
					EstatisticaProcessoServentiadt.setPercBalancoJudiciarioMes(EstatisticaProcessoServentiane.getPercentual(qtdProcessosArquivadosMes, qtdProcessosDistribuidosMes));
					EstatisticaProcessoServentiadt.setPercBalancoJudiciarioAno(EstatisticaProcessoServentiane.getPercentual(qtdProcessosArquivadosAno, qtdProcessosDistribuidosAno));
								
					byTemp = EstatisticaProcessoServentiane.relProcessosServentia( ProjudiPropriedades.getInstance().getCaminhoAplicacao() , EstatisticaProcessoServentiadt);										
					
					enviarPDF(response,byTemp,"Relatório");
										
					return;
				} else{
					request.setAttribute("MensagemErro", "Uma Serventia deve ser selecionada");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.LocalizarDWR: 
				break;
			case Configuracao.Novo: 
				EstatisticaProcessoServentiadt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case (((ServentiaDt.CodigoPermissao - EstatisticaProcessoServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (EstatisticaProcessoServentiaDt.CodigoPermissao * Configuracao.QtdPermissao))):
				serventiaNe = new ServentiaNe();
				tempList = serventiaNe.consultarServentiasAtivas(tempNomeBusca, PosicaoPaginaAtual,"1");
				if (tempList.size()>0){
					request.setAttribute("ListaServentia", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", serventiaNe.getQuantidadePaginas());
					request.setAttribute("Curinga", "ServentiaVara");
					EstatisticaProcessoServentiadt.limparProcessoTipo();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case (((ProcessoTipoDt.CodigoPermissao - EstatisticaProcessoServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (EstatisticaProcessoServentiaDt.CodigoPermissao * Configuracao.QtdPermissao))):
				processoTipoNe = new ProcessoTipoNe(); 
				if (EstatisticaProcessoServentiadt.getId_Serventia().length() > 0){
					serventiaNe = new ServentiaNe();
					ServentiaDt obTemp = serventiaNe.consultarId(EstatisticaProcessoServentiadt.getId_Serventia());
					tempList = processoTipoNe.consultarDescricaoProcessoTipos(tempNomeBusca,obTemp.getId_ServentiaSubtipo(),PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaProcessoTipo", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", processoTipoNe.getQuantidadePaginas());
						request.setAttribute("Id_ServentiaSubtipo", obTemp.getId_ServentiaSubtipo());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
				} else{
					request.setAttribute("MensagemErro", "Uma Serventia deve ser selecionada");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
		}

		request.getSession().setAttribute("EstatisticaProcessoServentiadt",EstatisticaProcessoServentiadt );
		request.getSession().setAttribute("EstatisticaProcessoServentiane",EstatisticaProcessoServentiane );

		RequestDispatcher dis =	request.getRequestDispatcher("/WEB-INF/jsptjgo/EstatisticaProcessoServentia.jsp");
		dis.include(request, response);
	}
}
