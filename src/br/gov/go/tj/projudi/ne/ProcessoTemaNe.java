package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ProcessoTemaPs;
import br.gov.go.tj.projudi.ps.TemaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.jus.cnj.bnpr.model.Tupla;

public class ProcessoTemaNe extends ProcessoTemaNeGen{

	private static final long serialVersionUID = 3917856882207385842L;

	public String Verificar(List<ProcessoTemaDt>listaTemas) {
		return (ValidacaoUtil.isVazio(listaTemas) ? "Nada para Salvar. Lista vazia." : "");
	}
	
	public List<ProcessoTemaDt> consultarTemasProcessoPorIdProcesso(String idProcesso, UsuarioNe usuarioSessao) throws Exception {
		List<ProcessoTemaDt> tempList = null;
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            ProcessoTemaPs obPersistencia = new  ProcessoTemaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarTemasProcessoPorIdProcesso(idProcesso, usuarioSessao);
        }  finally {
            obFabricaConexao.fecharConexao();
        }
        return tempList;
	}
	
	public void salvar(List listaTemas) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		ProcessoTemaDt processoTemaDt = null;

		try{

			for (int i = 0; i < listaTemas.size(); i++) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
				ProcessoTemaPs obPersistencia = new  ProcessoTemaPs(obFabricaConexao.getConexao()); 
				processoTemaDt = (ProcessoTemaDt) listaTemas.get(i);
				/* use o iu do objeto para saber se os dados ja estão ou não salvos */
				if (processoTemaDt.getId().length()==0) {
					obPersistencia.inserir(processoTemaDt);
					obLogDt = new LogDt("ProcessoTema",processoTemaDt.getId(), processoTemaDt.getId_UsuarioLog(),processoTemaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",processoTemaDt.getPropriedades());
				}else {
					obPersistencia.alterar(processoTemaDt); 
					obLogDt = new LogDt("ProcessoTema",processoTemaDt.getId(), processoTemaDt.getId_UsuarioLog(),processoTemaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),processoTemaDt.getPropriedades());
				}
				obDados.copiar(processoTemaDt);
				obLog.salvar(obLogDt, obFabricaConexao);
				obFabricaConexao.finalizarTransacao();
			}


		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoTemaOrigemJSON(String descricao, String posicao) throws Exception {
		String stTemp ="";
		TemaOrigemNe temaOrigem = new TemaOrigemNe();
		                        
		stTemp = temaOrigem.consultarDescricaoJSON(descricao, posicao);                       
		
		return stTemp;
	}
	
	public String consultarDescricaoTemaTipoJSON(String descricao, String posicao) throws Exception {
		String stTemp ="";
		TemaTipoNe temaTipo = new TemaTipoNe();
		
		stTemp = temaTipo.consultarDescricaoJSON(descricao, posicao);                       
		
		return stTemp;
	}
	
	public String consultarDescricaoTemaSituacaoJSON(String descricao, String posicao) throws Exception {
		String stTemp ="";
		TemaSituacaoNe temaSituacao = new TemaSituacaoNe();
		                        
		stTemp = temaSituacao.consultarDescricaoJSON(descricao, posicao);                       
		
		return stTemp;
	}
	
	@Override
	public String Verificar(ProcessoTemaDt dados) {
		
		return null;
	}

	public List consultarProcessosTema(String idTema, String id_TemaOrigem, String id_TemaSituacao, String id_TemaTipo, String temaCodigo, String id_Serv) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoTemaPs obPersistencia = new  ProcessoTemaPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosTema(idTema, id_TemaOrigem, id_TemaSituacao, id_TemaTipo, temaCodigo, id_Serv);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public String consultarTemasValidosPorDescricaoJSON(String descricao, String origem, String situacao, String codigo, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try{
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao()); 
				stTemp = obPersistencia.consultarTemasValidosPorDescricaoJSON(descricao, origem, situacao, codigo, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
		
	/**
	 * Verifica se o processo está com status SUSPENSO
	 * @param id_Processo
	 * @return
	 * @throws Exception
	 */
	public boolean isProcessoSuspenso(String id_Processo) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.isProcessoSuspenso(id_Processo);
	}
	
	
	public void salvarESuspender(String idProcesso, List listaTemas, UsuarioNe usuarioSessao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		ProcessoTemaDt processoTemaDt = null;
				
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			for (int i = 0; i < listaTemas.size(); i++) {
				ProcessoTemaPs obPersistencia = new  ProcessoTemaPs(obFabricaConexao.getConexao()); 
				processoTemaDt = (ProcessoTemaDt) listaTemas.get(i);
				if (processoTemaDt.getId().length()==0) {
					obPersistencia.inserir(processoTemaDt);
					obLogDt = new LogDt("ProcessoTema", processoTemaDt.getId(), processoTemaDt.getId_UsuarioLog(),processoTemaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",processoTemaDt.getPropriedades());
				} else {
					obPersistencia.alterar(processoTemaDt); 
					obLogDt = new LogDt("ProcessoTema", processoTemaDt.getId(), processoTemaDt.getId_UsuarioLog(),processoTemaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),processoTemaDt.getPropriedades());
				}
				obDados.copiar(processoTemaDt);
				obLog.salvar(obLogDt, obFabricaConexao);
			}
						
			LogDt logDt = new LogDt(usuarioSessao.getId_Usuario(), usuarioSessao.getIpComputadorLog());
			ProcessoNe processoNe = new ProcessoNe();
			processoNe.sobrestarProcesso(idProcesso, logDt, usuarioSessao.getUsuarioDt().getId_UsuarioServentia() , obFabricaConexao);
						
			obFabricaConexao.finalizarTransacao();

		} catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * Consulta as serventias para o filtro de processo por tema
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @param usuarioDt
	 * @return
	 * @throws Exception
	 */
	public String consultarServentiaJSON(String tempNomeBusca, String posicaoPaginaAtual, UsuarioDt usuarioDt) throws Exception {
		String stTemp ="";
		ServentiaNe neObjeto = new ServentiaNe();
		stTemp = neObjeto.consultarServentiaJSON(tempNomeBusca, posicaoPaginaAtual, usuarioDt);
		return stTemp;
	}
	
	/**
	 * Consulta os processos sobrestados no período
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */
	public List<Tupla> listarProcessosSobrestadosPorPeriodo (Date dataInicial, Date dataFinal) throws Exception{
		FabricaConexao obFabricaConexao = null;
		List<Tupla> lista = new ArrayList<>(); 
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoTemaPs obPersistencia = new ProcessoTemaPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarProcessosSobrestadosPorPeriodo(dataInicial, dataFinal);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Consulta os processos que estão sobrestados no SSG
	 * @return
	 * @throws Exception
	 */
	public List<Tupla> listarProcessosSobrestados_SSG() throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<Tupla> lista = new ArrayList<>(); 
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			ProcessoTemaPs obPersistencia = new ProcessoTemaPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarProcessosSobrestados_SSG();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}	
	
	/**
	 * Consulta os processos que saíram do sobrestamento no dia de ontem
	 * @param dataOntem
	 * @return
	 * @throws Exception
	 */
	public List<Tupla> listarProcessosNaoSobrestadosDiaAnterior (Date dataOntem) throws Exception{
		FabricaConexao obFabricaConexao = null;
		List<Tupla> lista = new ArrayList<>(); 
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoTemaPs obPersistencia = new ProcessoTemaPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarProcessosNaoSobrestadosDiaAnterior(dataOntem);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	
	/**	 
	 * Consulta os registros da relação Temas x Processo que estão em aberto, isto é
	 * com status TRANSITO_JULGADO, TRANSITADO_JULGADO ou CANCELADO
	 * @return
	 * @throws Exception
	 */
	public List<ProcessoTemaDt> consultarProcessoTemaComSituacaoTransitadoOuCancelado() throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<ProcessoTemaDt> lista = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoTemaPs obPersistencia = new ProcessoTemaPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarProcessoTemaComSituacaoTransitadoOuCancelado();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Verifica se todos os temas válidos de um processo já estão em transito julgado ou cancelado
	 * Método usado no projeto de execução automática do projudi.
	 * @return
	 * @throws Exception
	 */
	public List<ProcessoDt> consultarProcessoComTodosTemasTransitadoJulgado() throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<ProcessoDt> lista = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoTemaPs obPersistencia = new ProcessoTemaPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarProcessoComTodosTemasTransitadoJulgado();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * 
	 * @param id_processo
	 */
	public void finalizarProcessoTemas(FabricaConexao fabricaConexao, String id_processo, boolean isAcordo) throws Exception {
		ProcessoTemaPs obPersistencia = new ProcessoTemaPs(fabricaConexao.getConexao());
		List<ProcessoTemaDt> lista = obPersistencia.consultarTemasProcessoPorIdProcesso(id_processo, null);
		if (ValidacaoUtil.isNaoVazio(lista)){
			if (isAcordo){
				for (ProcessoTemaDt oItem : lista){
					obPersistencia.alterarJulgamentoMeritoQuandoTeseFirmadaAplicada (oItem.getId(), "0", "1");
				}
			} else {
				if (isAplicadaTeseFirmada(lista)){
					for (ProcessoTemaDt oItem : lista){
						oItem.setAplicaTese("1");
						obPersistencia.alterarJulgamentoMeritoQuandoTeseFirmadaAplicada (oItem.getId(), oItem.getAplicaTese(), oItem.getJulgaMeritoParaTeseFirmada());
					}
				}
			}
			obPersistencia.alterarDataSobrestamentoFinalTemas(id_processo);
		}		
	}
	
	/**
	 * Verificar se todos os temas estão TRANSITO_JULGADO, TRANSITADO_JULGADO OU CANCELADO.
	 * Com isso, ocorreu o cenário de aplicação de tese firmada. A coluna L será 'S' e columa M será 'S' 
	 * @param lista
	 * @return
	 */
	private boolean isAplicadaTeseFirmada(List<ProcessoTemaDt> lista){
		return lista.stream().filter(obj ->  !obj.getTemaSituacaoCnj().equals("TRANSITO_JULGADO") && !obj.getTemaSituacaoCnj().equals("TRANSITADO_JULGADO") && !obj.getTemaSituacaoCnj().equals("CANCELADO")).count() == 0;		
	}
	
	/**
	 * Consulta a lista de processo tema cujo campo Data Sobrestado final não é nulo, isto é, foi fechado em 
	 * decorrência do par de movimentação que sobresta o processo e a movimentação que suspende o sobrestamento. 
	 * @param idProcesso
	 * @return
	 * @throws Exception
	 */
	public List<ProcessoTemaDt> consultarTemasProcessoComDataSobrestadoFinalNaoNulo (String idProcesso) throws Exception {
		List<ProcessoTemaDt> tempList = null;
        FabricaConexao obFabricaConexao = null;        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            ProcessoTemaPs obPersistencia = new  ProcessoTemaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarTemasProcessoComDataSobrestadoFinalNaoNulo (idProcesso);
        }  finally {
            obFabricaConexao.fecharConexao();
        }
        return tempList;
	}
	
}
