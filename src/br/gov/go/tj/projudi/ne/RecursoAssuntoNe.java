package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RecursoAssuntoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.ps.RecursoAssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class RecursoAssuntoNe extends RecursoAssuntoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -4515020366630095466L;

    public String Verificar(RecursoAssuntoDt dados) {
		String stRetorno = "";

		////System.out.println("..neRecursoAssuntoVerificar()");
		return stRetorno;
	}

	/**
	 * Método utilizado na autuação de recurso para salvar todos os assuntos de um recurso
	 * 
	 * @param listaAssuntos: lista de Assuntos a serem salvos
	 * @param id_Recurso: identificação do recurso
	 * @param obFabricaConexao: conexão
	 * 
	 * @author msapaula
	 */
	public void salvarRecursoAssunto(List listaAssuntos, String id_Recurso, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		//Salvando assuntos da lista
		for (int i = 0; i < listaAssuntos.size(); i++) {
			RecursoAssuntoDt recursoAssuntoDt = (RecursoAssuntoDt) listaAssuntos.get(i);
			recursoAssuntoDt.setId_Recurso(id_Recurso);
			recursoAssuntoDt.setId_UsuarioLog(logDt.getId_Usuario());
			recursoAssuntoDt.setIpComputadorLog(logDt.getIpComputador());
			this.salvar(recursoAssuntoDt, obFabricaConexao);
		}
	}

	/**
	 * Sobrecarga do método salvar, que recebe uma conexão por parâmetro
	 */
	public void salvar(RecursoAssuntoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try{
			// Salva/Altera dados do assunto
			RecursoAssuntoPs obPersistencia = new RecursoAssuntoPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("RecursoAssuntoDt", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("RecursoAssuntoDt", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			dados.setId("");
			throw e;
		}
	}
	
	/**
	 * Exclui assuntos de recurso quer será convertido em processo
	 * @param id_Recurso: identificação do recurso
	 * 
	 * @author lsbernardades
	 */
	public void excluirRecursoAssunto(String id_recurso, FabricaConexao obFabricaConexao) throws Exception {		
			
		RecursoAssuntoPs obPersistencia = new RecursoAssuntoPs(obFabricaConexao.getConexao());
		obPersistencia.excluirRecursoAssunto(id_recurso);
		
	}

	/**
	 * Método que retorna os assuntos de um determinado recurso
	 * 
	 * @param id_Recurso: recruso para o qual serão consultados os assuntos
	 * @author msapaula
	 */
	public List getAssuntosRecurso(String id_Recurso) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoAssuntoPs obPersistencia = new RecursoAssuntoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.getAssuntosRecurso(id_Recurso);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public List getAssuntosRecurso(String id_Recurso, FabricaConexao obFabricaConexao) throws Exception {
		RecursoAssuntoPs obPersistencia = new RecursoAssuntoPs(obFabricaConexao.getConexao());
		return obPersistencia.getAssuntosRecurso(id_Recurso);
	}

	/**
	 * Método que salva múltiplos assuntos de um recurso.
	 * Compara a lista de assuntos editada com a lista anterior, e então exclui ou insere novos assuntos.
	 * 
	 * @param recursoDt: dados do processo com lista de assuntos atual
	 * @param listaAnterior: lista de assuntos antes da alteração 
	 * @param logDt: objeto log
	 * @param conexao: conexão
	 * 
	 * @author msapaula
	 */
	public void salvarAssuntosRecurso(RecursoDt recursoDt, List listaAnterior, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		RecursoAssuntoPs obPersistencia = new RecursoAssuntoPs(obFabricaConexao.getConexao());

		List listaAtual = recursoDt.getListaAssuntos();
		List listaExcluir = new ArrayList(); //Lista dos assuntos a serem excluídos
		List listaIncluir = new ArrayList(); //Lista dos novos assuntos a serem salvos

		//Pega a lista anterior dos assuntos e procura se algum foi excluído
		for (int i = 0; i < listaAnterior.size(); i++) {
			RecursoAssuntoDt assuntoAnterior = (RecursoAssuntoDt) listaAnterior.get(i);
			boolean boEncontrado = false;
			//Verifica qual assunto saiu da lista
			for (int j = 0; j < listaAtual.size(); j++) {
				RecursoAssuntoDt assuntoAtual = (RecursoAssuntoDt) listaAtual.get(j);
				if (assuntoAnterior.getId_Assunto() == assuntoAtual.getId_Assunto()) {
					boEncontrado = true;
					break;
				}
			}
			//se o id do assunto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) listaExcluir.add(assuntoAnterior);
		}

		//Verifica os assuntos a serem incluídos
		for (int i = 0; i < listaAtual.size(); i++) {
			RecursoAssuntoDt assuntoAtual = (RecursoAssuntoDt) listaAtual.get(i);
			boolean boEncontrado = false;
			for (int j = 0; j < listaAnterior.size(); j++) {
				RecursoAssuntoDt assuntoAnterior = (RecursoAssuntoDt) listaAnterior.get(j);
				if (assuntoAnterior.getId_Assunto() == assuntoAtual.getId_Assunto()) {
					boEncontrado = true;
					break;
				}
			}
			if (!boEncontrado) listaIncluir.add(assuntoAtual);
		}

		for (int i = 0; i < listaIncluir.size(); i++) {
			RecursoAssuntoDt obDt = (RecursoAssuntoDt) listaIncluir.get(i);
			obDt.setId_Recurso(recursoDt.getId());				
			obPersistencia.inserir(obDt);
			LogDt obLogDt = new LogDt("RecursoAssunto", obDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
		}

		for (int i = 0; i < listaExcluir.size(); i++) {
			RecursoAssuntoDt obDtTemp = (RecursoAssuntoDt) listaExcluir.get(i);
			LogDt obLogDt = new LogDt("RecursoAssunto", obDtTemp.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Excluir), obDtTemp.getPropriedades(), "");
			obPersistencia.excluir(obDtTemp.getId());
			obLog.salvar(obLogDt, obFabricaConexao);
		}


	}
}
