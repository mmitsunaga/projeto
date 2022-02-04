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
			stRetorno += "O campo C�digo � obrigat�rio.";
		}
		if (dados.getProcessoTipo().equalsIgnoreCase("")) {
			stRetorno += "O campo Descri��o � obrigat�rio.";
		}
		if (dados.getCnjCodigo() == null || dados.getCnjCodigo().equalsIgnoreCase("")) {
			stRetorno += "O campo CNJ C�digo � obrigat�rio.";
		}
		return stRetorno;
	}

	/**
	 * M�todo respons�vel por abrir uma conex�o com o banco de dados para buscar os tipos de processo (tipos de a��o) existentes
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
	 * M�todo respons�vel por abrir a conex�o com o banco de dados para buscar o tipo de processo (tipo de a��o) solicitado
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
	 * M�todo respons�vel por abrir a conex�o com o banco de dados para buscar o tipo de processo (tipo de a��o) solicitado
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
	 * M�todo que chama ServentiaSubTipoProcessoTipoNe para consultar os tipos de a��es (Processos Tipos) dispon�veis para uma determinada Serventia Subtipo
	 * 
	 * @param id_ServentiaSubtipo:
	 *            id da Serventia Subtipo
	 * @return: lista de tipos de a��o (Processos Tipos) dispon�veis
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
	 * M�todo para consultar lista de processo tipo pela descri��o.
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
	 * M�todo que checa se o c�digo � de um tipo de a��o de execu��o
	 * @param codigo C�digo do tipo de a��o
	 * @return True se o c�digo � referente a um a��o de execu��o, false caso contr�rio
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
	 * M�todo que checa se o c�digo � de um tipo de a��o de execu��o ou monit�ria
	 * @param codigo C�digo do tipo de a��o
	 * @return True se o c�digo � referente a um a��o de execu��o, false caso contr�rio
	 * @throws Exception 
	 */
	public boolean AcaoExecucaoOuMonitoria(String processoTipoCodigo) throws Exception{
		int codigo = Funcoes.StringToInt(processoTipoCodigo);
		if (codigo == ProcessoTipoDt.ACAO_MONITORIA) return true;
		else 
		 return AcaoExecucao(processoTipoCodigo);
	}
	
	/**
	 * M�todo para consultar o c�digo pelo id.
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
	 * M�todo que gera o relat�rio PDF ou TXT resultante da listagem de Processo Tipo.
	 * 
	 * @param diretorioProjeto - diret�rio do projeto onde est� o arquivo jasper compilado
	 * @param nomeBusca - nome de busca de processo tipo
	 * @param tipoArquivo - tipo de arquivo do relat�rio selecionado (txt ou pdf)
	 * @param usuarioResponsavelRelatorio - usu�rio que est� solicitando o relat�rio
	 * @return relat�rio
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

			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals(1)) {

				// PAR�METROS DO RELAT�RIO
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
	 * M�todo que consulta os Tipos de Processo ligados � Serventia atrav�s de suas �reas de Distribui��o.
	 * @param descricao - descri��o do tipo de processo
	 * @param listaAreaDistribuicao - lista de IDs das �reas de Distribui��o vinculadas � Serventia
	 * @param posicaoPaginaAtual - posi��o da pagina��o
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
	 * M�todo para consultar o c�digo CNJ pelo id.
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
	 * M�todo para auxiliar na verifica��o se o processo tipo c�digo � de um dos tipos de mandado de seguran�a.
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
	 * Ocorr�ncia 2019/6291
	 * 
	 * M�todo para verificar se o proctipoCodigo � o definido na ocorr�ncia para cadastro de custa em processo
	 * de mandado de seguran�a para a turma recursal.
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
	 * M�todo para auxiliar na verifica��o se o processo tipo c�digo � de um dos tipos de mandado de div�rcio.
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
	 * M�todo para verificar se o processo tipo c�digo informado � de algum tipo de busca e apreens�o.
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
	 * M�todo para verificar se o processo tipo c�digo informado � de algum tipo de carta precat�ria.
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
	 * M�todo para auxiliar na verifica��o se o processo tipo c�digo � do tipo Div�rcio Litigiosa.
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
