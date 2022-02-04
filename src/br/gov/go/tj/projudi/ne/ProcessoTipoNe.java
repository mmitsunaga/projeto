package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.ps.ProcessoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoTipoNe extends ProcessoTipoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6007654980654694123L;

	public String Verificar(ProcessoTipoDt dados) {
		String stRetorno = "";
		if (dados.getProcessoTipoCodigo().equalsIgnoreCase("")) {
			stRetorno += "O campo Código é obrigatório.";
		}
		if (dados.getProcessoTipo().equalsIgnoreCase("")) {
			stRetorno += "O campo Descrição é obrigatório.";
		}
		if (dados.getCnjCodigo() == null || dados.getCnjCodigo().equalsIgnoreCase("")) {
			stRetorno += "O campo CNJ Código é obrigatório.";
		}
		return stRetorno;
	}

	/**
	 * Método responsável por abrir uma conexão com o banco de dados para buscar os tipos de processo (tipos de ação) existentes
	 * 
	 * @author Keila
	 * @return listaProcessoTipos: lista contendo os tipos de processos existentes
	 * @throws Exception
	 */
	public Object getProcessoTipos() throws Exception {
		List listaProcessoTipos = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			listaProcessoTipos = obPersistencia.getProcessoTipos();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaProcessoTipos;
	}

	/**
	 * Método responsável por abrir a conexão com o banco de dados para buscar o tipo de processo (tipo de ação) solicitado
	 * 
	 * @author Keila
	 * @param processoTipoCodigo
	 * @return processoTipoDt: tipo de processo solicitado
	 * @throws Exception
	 */
	public ProcessoTipoDt consultarProcessoTipoCodigo(String processoTipoCodigo) throws Exception {
		ProcessoTipoDt processoTipoDt = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			processoTipoDt = obPersistencia.consultarProcessoTipoCodigo(processoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return processoTipoDt;
	}

	/**
	 * Método responsável por abrir a conexão com o banco de dados para buscar o tipo de processo (tipo de ação) solicitado
	 * 
	 * @author Keila
	 * @param processoTipoCodigo
	 * @return processoTipoDt: tipo de processo solicitado
	 * @throws Exception
	 */
	public String consultarIdProcessoTipo(String processoTipoCodigo, FabricaConexao obFabricaConexao) throws Exception {
		String id = null;
		
		ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
		id = obPersistencia.consultarIdProcessoTipo(processoTipoCodigo);
		
		return id;
	}
	
	/**
	 * Método que chama ServentiaSubTipoProcessoTipoNe para consultar os tipos de ações (Processos Tipos) disponíveis para uma determinada Serventia Subtipo
	 * 
	 * @param id_ServentiaSubtipo:
	 *            id da Serventia Subtipo
	 * @return: lista de tipos de ação (Processos Tipos) disponíveis
	 */
	public List consultarDescricaoProcessoTipos(String descricao, String id_ServentiaSubtipo, String posicao) throws Exception {
		List tempList = null;
		
		ServentiaSubtipoProcessoTipoNe ne = new ServentiaSubtipoProcessoTipoNe();
		tempList = ne.consultarDescricaoProcessoTipos(id_ServentiaSubtipo, descricao, posicao);
		QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
		tempList.remove(tempList.size() - 1);
		
		return tempList;
	}
	
	/**
	 * Método para consultar lista de processo tipo pela descrição.
	 * @param String descricao
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricao(String descricao) throws Exception {
		List listaProcessoTipoDt = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			listaProcessoTipoDt = obPersistencia.consultarDescricao(descricao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaProcessoTipoDt;
	}

	/**
	 * Método que checa se o código é de um tipo de ação de execução
	 * @param codigo Código do tipo de ação
	 * @return True se o código é referente a um ação de execução, false caso contrário
	 * @throws Exception 
	 */
	public boolean AcaoExecucao(String processoTipoCodigo) throws Exception {
		int codigo = Funcoes.StringToInt(processoTipoCodigo);
		ProcessoTipoProcessoSubtipoNe ptpsNe = new ProcessoTipoProcessoSubtipoNe();
		boolean retorno = false;
		retorno = ptpsNe.isExecucao(codigo);
		return retorno;
	}

	/**
	 * Método que checa se o código é de um tipo de ação de execução ou monitória
	 * @param codigo Código do tipo de ação
	 * @return True se o código é referente a um ação de execução, false caso contrário
	 * @throws Exception 
	 */
	public boolean AcaoExecucaoOuMonitoria(String processoTipoCodigo) throws Exception{
		int codigo = Funcoes.StringToInt(processoTipoCodigo);
		if (codigo == ProcessoTipoDt.ACAO_MONITORIA) return true;
		else 
		 return AcaoExecucao(processoTipoCodigo);
	}
	
	/**
	 * Método para consultar o código pelo id.
	 * @param String idProcessoTipo
	 * @return String 
	 * @throws Exception
	 */
	public String consultarCodigo(String idProcessoTipo) throws Exception {
		String processoTipoCodigo = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			processoTipoCodigo = obPersistencia.consultarCodigo(idProcessoTipo);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return processoTipoCodigo;
	}

	/**
	 * Método que gera o relatório PDF ou TXT resultante da listagem de Processo Tipo.
	 * 
	 * @param diretorioProjeto - diretório do projeto onde está o arquivo jasper compilado
	 * @param nomeBusca - nome de busca de processo tipo
	 * @param tipoArquivo - tipo de arquivo do relatório selecionado (txt ou pdf)
	 * @param usuarioResponsavelRelatorio - usuário que está solicitando o relatório
	 * @return relatório
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relProcessoTipo(String diretorioProjeto, String nomeBusca, Integer tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());

			List listaProcessosTipo = obPersistencia.consultarProcessoTipoDescricao(nomeBusca);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "listagemProcessoTipo";

			// Tipo Arquivo == 1 é PDF , TipoArquivo == 2 é TXT
			if (tipoArquivo != null && tipoArquivo.equals(1)) {

				// PARÂMETROS DO RELATÓRIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Listagem de Tipos de Processo");
				parametros.put("parametroConsulta", nomeBusca.toUpperCase());
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

					temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessosTipo);

			} else {
				String conteudoArquivo = "";
				Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < listaProcessosTipo.size(); i++) {
					ProcessoTipoDt obTemp = (ProcessoTipoDt) listaProcessosTipo.get(i);
					conteudoArquivo += obTemp.getProcessoTipo() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	/**
	 * Método que consulta os Tipos de Processo ligados à Serventia através de suas Áreas de Distribuição.
	 * @param descricao - descrição do tipo de processo
	 * @param listaAreaDistribuicao - lista de IDs das Áreas de Distribuição vinculadas à Serventia
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de tipos de processo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessoTipoServentia(String descricao, List listaAreaDistribuicao, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarProcessoTipoServentia(descricao, listaAreaDistribuicao, posicaoPaginaAtual);
			
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarProcessoTipoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessoTipoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarProcessoTipoServentiaJSON(String descricao, String idServentia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessoTipoServentiaJSON(descricao, idServentia, posicaoPaginaAtual);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarProcessoTipoPublicoServentiaJSON(String descricao, String idServentia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessoTipoPublicoServentiaJSON(descricao, idServentia, posicaoPaginaAtual);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Método para consultar o código CNJ pelo id.
	 * @param String idProcessoTipo
	 * @return String 
	 * @throws Exception
	 */
	public String consultarCodigoCNJ(String idProcessoTipo) throws Exception {
		String processoTipoCodigoCNJ = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			processoTipoCodigoCNJ = obPersistencia.consultarCodigoCNJ(idProcessoTipo);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return processoTipoCodigoCNJ;
	}
	
	public String consultarCodigoCNJ(String idProcessoTipo, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarCodigoCNJ(idProcessoTipo);
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao, String id_AreaDistribuicao, String id_Serventia, boolean ehAdvogado) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarDescricaoCombo(descricao, id_AreaDistribuicao, id_Serventia, ehAdvogado);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método para auxiliar na verificação se o processo tipo código é de um dos tipos de mandado de segurança.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoMandado(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		switch(processoTipoCodigo) {
			case ProcessoTipoDt.MANDADO_SEGURANCA_420 :
			case ProcessoTipoDt.MANDADO_SEGURANCA_101 :
			case ProcessoTipoDt.MANDADO_SEGURANCA_1531 :
			case ProcessoTipoDt.MANDADO_SEGURANCA_CIVEL :
			case ProcessoTipoDt.MANDADO_SEGURANCA_8069 :
			case ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO : {
				
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Ocorrência 2019/6291
	 * 
	 * Método para verificar se o proctipoCodigo é o definido na ocorrência para cadastro de custa em processo
	 * de mandado de segurança para a turma recursal.
	 * 
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoMandadoTurmaRecursalGeraGuia(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		switch(processoTipoCodigo) {
			case ProcessoTipoDt.MANDADO_SEGURANCA_CIVEL : {
				
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para auxiliar na verificação se o processo tipo código é de um dos tipos de mandado de divórcio.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoConversaoSeparacaoEmDivorcio(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		switch(processoTipoCodigo) {
			case ProcessoTipoDt.CONVERSAO_SEPARACAO_EM_DIVORCIO :
			case ProcessoTipoDt.DIVORCIO_CONSENSUAL : {
				
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se o processo tipo código informado é de algum tipo de busca e apreensão.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoBuscaApreensao(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		switch(processoTipoCodigo) {
			case ProcessoTipoDt.BUSCA_APREENSAO_ALIENACAO_FIDUCIARIA :
			case ProcessoTipoDt.BUSCA_APREENSAO :
			case ProcessoTipoDt.BUSCA_APREENSAO_CPC : {
				
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se o processo tipo código informado é de algum tipo de carta precatória.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoCartaPrecatoria(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		switch(processoTipoCodigo) {
			case ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE :
			case ProcessoTipoDt.CARTA_PRECATORIA_INFRACIONAL :
			case ProcessoTipoDt.CARTA_PRECATORIA_CPP :
			case ProcessoTipoDt.CARTA_PRECATORIA_CPC :
			case ProcessoTipoDt.CARTA_PRECATORIA : {
				
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para auxiliar na verificação se o processo tipo código é do tipo Divórcio Litigiosa.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoDivorcioLitigiosaLE(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		if( processoTipoCodigo == ProcessoTipoDt.DIVORCIO_LITIGIOSO_LE ) {
			retorno = true;
		}
		
		return retorno;
	}
	
	public String consultarProcessoTipoServentiaRecursoJSON(String descricao, String idRecurso, boolean somenteAtivos, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessoTipoServentiaRecursoJSON(descricao, idRecurso, somenteAtivos, posicaoPaginaAtual);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public List<ProcessoTipoDt> consultarProcessoTipoServentiaRecurso(String descricao, String idRecurso, boolean somenteRecursosAtivos) throws Exception {
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarProcessoTipoServentiaRecurso(descricao, idRecurso, somenteRecursosAtivos);
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	public String consultarDescricaoPoloAtivoPassivoJSON(String idProcTipo) throws Exception {
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarDescricaoPoloAtivoPassivoJSON(idProcTipo);
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	public List<ProcessoTipoDt> consultarPorCodigos(List<Integer> codigos)  throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoPs obPersistencia = new ProcessoTipoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarPorCodigos(codigos);
		} finally{
			obFabricaConexao.fecharConexao();
		}
 
	}
}
