package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ps.ProcessoAssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.jus.cnj.intercomunicacao.beans.AssuntoProcessual;

public class ProcessoAssuntoNe extends ProcessoAssuntoNeGen {

    /**
     * 
     */
    private static final long serialVersionUID = -4504282021675416598L;

    public String Verificar(ProcessoAssuntoDt dados) {

        String stRetorno = "";

        if (dados.getProcessoNumero().length() == 0) stRetorno += "O Campo ProcessoNumero é obrigatório.";
        return stRetorno;

    }

    /**
     * Sobrecarga do método salvar, que recebe uma conexão por parâmetro
     */
    public void salvar(ProcessoAssuntoDt dados, FabricaConexao obFabricaConexao) throws Exception {
        LogDt obLogDt;
        try{
            // Salva/Altera dados do assunto
            ProcessoAssuntoPs obPersistencia = new ProcessoAssuntoPs(obFabricaConexao.getConexao());
            if (dados.getId().equalsIgnoreCase("")) {               
                obPersistencia.inserir(dados);
                obLogDt = new LogDt("ProcessoAssunto", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), dados.getId(), dados.getPropriedades());
            } else {               
                obPersistencia.alterar(dados);
                obLogDt = new LogDt("ProcessoAssunto", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
            }
            obDados.copiar(dados);
            obLog.salvar(obLogDt, obFabricaConexao);
        } catch(Exception e) {
            dados.setId("");
            throw e;
        }
    }

	/**
	 * Método utilizado no cadastro de processo para salvar todas os assuntos de um processo
	 * 
	 * @param listaAssuntos: lista de Assuntos a serem salvos
	 * @param id_Processo: identificação do processo
	 * @param obFabricaConexao: conexão
	 * 
	 * @author msapaula
	 */
	public void salvarProcessoAssunto(List listaAssuntos, String id_Processo, LogDt logDt, FabricaConexao obFabricaConexao)
			throws Exception {
		//Salvando assuntos da lista
		if (listaAssuntos != null) {
			for (int i = 0; i < listaAssuntos.size(); i++) {
				ProcessoAssuntoDt processoAssuntoDt = (ProcessoAssuntoDt) listaAssuntos.get(i);
				processoAssuntoDt.setId_Processo(id_Processo);
				processoAssuntoDt.setId_UsuarioLog(logDt.getId_Usuario());
				processoAssuntoDt.setIpComputadorLog(logDt.getIpComputador());
				this.salvar(processoAssuntoDt, obFabricaConexao);
			}
		}
	}

	/**
	 * Método que retorna os assuntos de um determinado processo.
	 * Conexão será criada internamente
	 * 
	 * @param id_Processo: processo para o qual serão consultados os assuntos
	 * @author msapaula
	 * @author jrcorrea
	 */
	public List consultarAssuntosProcesso(String id_Processo) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		List tempList = null;
		try{
			 obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			ProcessoAssuntoPs obPersistencia = new ProcessoAssuntoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAssuntosProcesso(id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Método que retorna os assuntos de um determinado processo
	 * 
	 * @param id_Processo: processo para o qual serão consultados os assuntos
	 * @param conexao, realiza consulta baseada em conexão já existente
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAssuntosProcesso(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {	    
		List tempList = null;
								
		ProcessoAssuntoPs obPersistencia = new ProcessoAssuntoPs(obFabricaConexao.getConexao());
		tempList = obPersistencia.consultarAssuntosProcesso(id_Processo);
		
		return tempList;
	}


	/**
	 * Método que salva múltiplos assuntos de um processo.
	 * Compara a lista de assuntos editada com a lista anterior, e então exclui ou insere novos assuntos.
	 * 
	 * @param processoDt: dados do processo com lista de assuntos atual
	 * @param listaAnterior: lista de assuntos antes da alteração 
	 * @param logDt: objeto log
	 * @param conexao: conexão
	 * 
	 * @author msapaula
	 */
	public void salvarAssuntosProcesso(ProcessoDt processoDt, List listaAnterior, LogDt logDt, FabricaConexao obFabricaConexao)			throws Exception {
		
		ProcessoAssuntoPs obPersistencia = new ProcessoAssuntoPs(obFabricaConexao.getConexao());

		List listaAtual = processoDt.getListaAssuntos();
		List listaExcluir = new ArrayList(); //Lista dos assuntos a serem excluídos
		List listaIncluir = new ArrayList(); //Lista dos novos assuntos a serem salvos

        // Pega a lista anterior dos assuntos e procura se algum foi
        // excluído
        for (int i = 0; i < listaAnterior.size(); i++) {
            ProcessoAssuntoDt assuntoAnterior = (ProcessoAssuntoDt) listaAnterior.get(i);
            boolean boEncontrado = false;
            // Verifica qual assunto saiu da lista
            for (int j = 0; j < listaAtual.size(); j++) {
                ProcessoAssuntoDt assuntoAtual = (ProcessoAssuntoDt) listaAtual.get(j);
                if (assuntoAnterior.getId_Assunto() == assuntoAtual.getId_Assunto()) {
                    boEncontrado = true;
                    break;
                }
            }
            // se o id do assunto não foi encontrado na lista editada coloco
            // o objeto para ser excluido
            if (!boEncontrado) listaExcluir.add(assuntoAnterior);
        }

        // Verifica os assuntos a serem incluídos
        for (int i = 0; i < listaAtual.size(); i++) {
            ProcessoAssuntoDt assuntoAtual = (ProcessoAssuntoDt) listaAtual.get(i);
            boolean boEncontrado = false;
            for (int j = 0; j < listaAnterior.size(); j++) {
                ProcessoAssuntoDt assuntoAnterior = (ProcessoAssuntoDt) listaAnterior.get(j);
                if (assuntoAnterior.getId_Assunto() == assuntoAtual.getId_Assunto()) {
                    boEncontrado = true;
                    break;
                }
            }
            if (!boEncontrado) listaIncluir.add(assuntoAtual);
        }

        for (int i = 0; i < listaIncluir.size(); i++) {
            ProcessoAssuntoDt obDt = (ProcessoAssuntoDt) listaIncluir.get(i);
            obDt.setId_Processo(processoDt.getId());                
            obPersistencia.inserir(obDt);
            LogDt obLogDt = new LogDt("ProcessoAssunto", obDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Incluir), obDt.getId(), obDt.getPropriedades());
            obLog.salvar(obLogDt, obFabricaConexao);
        }

        for (int i = 0; i < listaExcluir.size(); i++) {
            ProcessoAssuntoDt obDtTemp = (ProcessoAssuntoDt) listaExcluir.get(i);
            LogDt obLogDt = new LogDt("ProcessoAssunto", obDtTemp.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Excluir), obDtTemp.getPropriedades(), "");
            obPersistencia.excluir(obDtTemp.getId());
            obLog.salvar(obLogDt, obFabricaConexao);
        }
        

    }

	public void excluirAssuntoProcesso(String id_Processo, String id_Assunto, String id_ProcessoParte, String Id_UsuarioLog, String IpComputadorLog,FabricaConexao obFabricaConexao) throws Exception {
		
		ProcessoAssuntoPs obPersistencia = new ProcessoAssuntoPs(obFabricaConexao.getConexao());
		
		if (obPersistencia.excluirAssuntoProcesso( id_Processo,  id_Assunto, id_ProcessoParte)>0) {
			String stValorAtual = "{id_proc:" + id_Processo + ", id_assunto:" + id_Assunto + ", id_processoParte:"+id_ProcessoParte+"}";
			
			LogDt obLogDt = new LogDt("ProcessoAssunto",id_Assunto, Id_UsuarioLog,IpComputadorLog, String.valueOf(LogTipoDt.Excluir),stValorAtual,"");
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		
	}

	public void inserirAssuntoProcesso(ProcessoAssuntoDt dt, 	FabricaConexao obFabricaConexao) throws Exception {
		ProcessoAssuntoPs obPersistencia = new ProcessoAssuntoPs(obFabricaConexao.getConexao());
		List lisAssunto = obPersistencia.consultarAssuntosProcesso(dt.getId_Processo());
		boolean boSalvar= true;
		
		for (int i=0;lisAssunto!=null && i<lisAssunto.size(); i++) {
			ProcessoAssuntoDt dt1 = (ProcessoAssuntoDt) lisAssunto.get(i);
			if (dt1.getId_Assunto().equals(dt.getId_Assunto())) {
				boSalvar =false;
				break;
			}
		}
			
		if(boSalvar) {			
			salvar(dt, obFabricaConexao);
		}
	}
}
