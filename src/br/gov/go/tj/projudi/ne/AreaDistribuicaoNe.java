package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AreaDistribuicaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AreaDistribuicaoNe extends AreaDistribuicaoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1542447038406473002L;

	public String Verificar(AreaDistribuicaoDt dados) {

		String stRetorno = "";

		if (dados.getAreaDistribuicao().equalsIgnoreCase("")) stRetorno += "O campo Descri��o � obrigat�rio.";
		if (dados.getAreaDistribuicaoCodigo().equalsIgnoreCase("")) stRetorno += "O campo C�digo � obrigat�rio.";
		if (dados.getServentiaSubtipo().equalsIgnoreCase("")) stRetorno += "O campo Subtipo de Serventia � obrigat�rio.";
		if (dados.getForum().equalsIgnoreCase("")) stRetorno += "O campo F�rum � obrigat�rio.";
		return stRetorno;

	}

	/**
	 * Sobrescrevendo m�todo salvar, para que seja setado o Status no momento da inser��o
	 * @throws Exception 
	 */
	public void salvar(AreaDistribuicaoDt dados) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			if (dados.getId().length() == 0) {
				dados.setCodigoTemp(String.valueOf(AreaDistribuicaoDt.ATIVO));
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AreaDistribuicao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AreaDistribuicao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();

			//-----------CONTROLE DE DISTRIBUI��O--------------------------------------------------------------------                                   
			//Verifico se a �rea de distribui��o e nova
//			if (boNovo) {
//				DistribuicaoProcesso.getInstance().lerDados(dados.getId());
//			}
			// -------------------------------------------------------------------------------

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Consulta geral de �reas de Distribui��o dispon�veis em uma Comarca, filtrando de acordo com a �rea do processo
	 * 
	 * @param descricao, descri��o para filtro
	 * @param id_comarca, identifica��o da comarca
	 * @param areaCodigo, define a �rea do processo (c�vel ou criminal)
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicao(String descricao, String id_comarca, String areaCodigo, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			switch (areaCodigo) {
				case AreaDt.CIVEL:
					tempList = obPersistencia.consultarAreasDistribuicaoCivel(descricao, id_comarca, posicao);
					break;

				case AreaDt.CRIMINAL:
					tempList = obPersistencia.consultarAreasDistribuicaoCriminal(descricao, id_comarca, posicao);
					break;

				default:
					break;
			}

			if (tempList != null && tempList.size() > 0) {
				QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
				tempList.remove(tempList.size() - 1);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta �reas de Distribui��o em uma Comarca.
	 * @param descricao - descri��o da �rea de distribui��o (opcional)
	 * @param id_comarca - identifica��o da comarca
	 * 
	 * @author hmgodinho
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoComarcaServentiaSubTipo(String descricao, String idComarca, String idServentiaSubTipo, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			tempList = obPersistencia.consultarAreasDistribuicaoComarcaServentiaSubTipo(descricao, idComarca, idServentiaSubTipo, posicao);

			if (tempList != null && tempList.size() > 0) {
				QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
				tempList.remove(tempList.size() - 1);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta de �reas de Distribui��o C�veis dispon�veis em uma Comarca
	 * 
	 * @param nome, descri��o para filtro
	 * @param id_comarca, identifica��o da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoCivel(String descricao, String id_Comarca, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAreasDistribuicaoCivel(descricao, id_Comarca, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consultar �reas de distribui��o criminais de uma determinada comarca
	 * 
	 * @param nome, descri��o para filtro
	 * @param id_comarca, identifica��o da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoCriminal(String descricao, String id_Comarca, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAreasDistribuicaoCriminal(descricao, id_Comarca, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consultar �reas de distribui��o de execu��o penal de uma determinada comarca
	 * @param descricao: descri��o para filtro
	 * @param id_comarca: identifica��o da comarca
	 * @param posicao: pagina solicitada
	 * 
	 * @author wcsilva
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoExecucao(String descricao, String id_Comarca, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAreasDistribuicaoExecucao(descricao, id_Comarca, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarAreasDistribuicaoExecucaoJSON(String descricao, String id_Comarca, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAreasDistribuicaoExecucaoJSON(descricao, id_Comarca, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * M�todo que retorna todas as �reas de Distribuic�o ativas do sistema, 
	 * ordenadas pela descri��o e c�digo
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoAtivas() throws Exception{
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarAreasDistribuicaoAtivas();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Retorna todas as �reas de distribui��o ativas dispon�veis para redistribui��o.
	 * Dependendo do tipo da serventia, ir� filtrar as �reas de distribui��o correspondentes.
	 * 
	 * @param descricao filtro para pesquisa
	 * @param posicao parametro para pagina��o
	 * @param serventiaTipoCodigo, tipo da serventia do usu�rio que est� realizando a consulta
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List consultarAreasDistribuicaoRedistribuicaoAtivas(String descricao, String posicao) throws Exception{
		return consultarAreasDistribuicaoAtivas(descricao, posicao);
	}

	/**
	 * Recupera todas as �reas de distribui��o de processos ativas.
	 * 
	 * @param descricao
	 * @param posicao
	 * @return List
	 * @throws Exception 
	 */
	private List consultarAreasDistribuicaoAtivas(String descricao, String posicao) throws Exception{
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarAreasDistribuicaoAtivas(descricao, posicao);
			QuantidadePaginas = (Long) liTemp.get(liTemp.size() - 1);
			liTemp.remove(liTemp.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Consulta as �reas de distribui��o ativas para Segundo Grau
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoSegundoGrauAtivas(String descricao, String posicao) throws Exception{
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarAreasDistribuicaoSegundoGrauAtivas(descricao, posicao);
			QuantidadePaginas = (Long) liTemp.get(liTemp.size() - 1);
			liTemp.remove(liTemp.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Retorna a �rea de distribui��o relacionada a �rea passada.
	 * No primeiro momento essa �rea de distribui��o relacionada ser� somente do tipo recursal.
	 * 
	 * @param id_AreaDistribuicao, identifica��o da �rea principal
	 * @author msapaula
	 */
	/*public AreaDistribuicaoDt getAreaDistribuicaoRelacionada(String id_AreaDistribuicao){
		AreaDistribuicaoDt areaRelacionada = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			areaRelacionada = obPersistencia.getAreaDistribuicaoRelacionada(id_AreaDistribuicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return areaRelacionada;
	}/*/

	/**
	 * Consulta a �reas de Distribui��o do tipo Precat�ria dispon�vel em uma Comarca
	 * 
	 * @param id_comarca, identifica��o da comarca
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String consultarAreaDistribuicaoPrecatoria(String id_Comarca) throws Exception{
		String id_AreaDistribuicao = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			id_AreaDistribuicao = obPersistencia.consultarAreaDistribuicaoPrecatoria(id_Comarca);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return id_AreaDistribuicao;
	}
	
	/**
	 * Consulta as �reas de Distribui��o de acordo com o SubTipo da Serventia e Comarca passados
	 * 
	 * @param serventiaSubTipoCodigo, identifica��o do sub-tipo da serventia
	 * @param id_comarca, identifica��o da comarca
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoServentiaSubTipo(String serventiaSubTipoCodigo, String id_Comarca) throws Exception{
		List listaAreasDistribuicao = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			listaAreasDistribuicao = obPersistencia.consultarAreasDistribuicaoServentiaSubTipo(serventiaSubTipoCodigo, id_Comarca);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAreasDistribuicao;
	}

	/**
	 * Busca uma �rea de distribui��o baseado no c�digo passado
	 * 
	 * @param areaDistribuicaoCodigo, c�digo da �rea a ser pesquisada
	 * @return AreaDistribuicaoDt: �rea de distribui��o solicitada
	 * 
	 * @author msapaula
	 * @throws Exception 
	 * 
	 */
	public AreaDistribuicaoDt consultarAreaDistribuicaoCodigo(String areaDistribuicaoCodigo) throws Exception{
		AreaDistribuicaoDt areaDistribuicaoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			areaDistribuicaoDt = obPersistencia.consultarAreaDistribuicaoCodigo(areaDistribuicaoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return areaDistribuicaoDt;
	}
	
	public String consultarIdAreaDistribuicao(String areaDistribuicaoCodigo, FabricaConexao obFabricaConexao) throws Exception {
		String id = "";
		
		AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
		id = obPersistencia.consultarIdAreaDistribuicao(areaDistribuicaoCodigo);
		
		return id;
	}
	
	public AreaDistribuicaoDt consultarIdAreaDistribuicaoRecursal(String id_AreaDistribuicao, FabricaConexao obFabricaConexao) throws Exception{
		AreaDistribuicaoDt areaDistribuicaoDt = null;
		
		AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
		areaDistribuicaoDt = obPersistencia.consultarId(id_AreaDistribuicao);
		
		return areaDistribuicaoDt;
	}
	
	/**
	 * Consulta geral de �reas de Distribui��o de Segundo Grau dispon�veis em uma Comarca, filtrando de acordo com a �rea do processo
	 * 
	 * @param descricao, descri��o para filtro
	 * @param id_comarca, identifica��o da comarca
	 * @param areaCodigo, define a �rea do processo (c�vel ou criminal)
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoSegundoGrau(String descricao, String id_Comarca, String areaCodigo, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			switch (areaCodigo) {
				case AreaDt.CIVEL:
					tempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCivel(descricao, id_Comarca, posicao);
					break;

				case AreaDt.CRIMINAL:
					tempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCriminal(descricao, id_Comarca, posicao);
					break;

				default:
					break;
			}

			if (tempList != null && tempList.size() > 0) {
				QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
				tempList.remove(tempList.size() - 1);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta de �reas de Distribui��o Ativas dispon�veis em uma Comarca
	 * 
	 * @param nome, descri��o para filtro
	 * @param id_Comarca, identifica��o da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author M�rcio Gomes
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicao(String descricao, String id_comarca, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			
			tempList = obPersistencia.consultarAreasDistribuicao(descricao, id_comarca, posicao);	

			if (tempList != null && tempList.size() > 0) {
				QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
				tempList.remove(tempList.size() - 1);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;	
	}
	
	/**
	 * Consulta a descri��o do f�rum a partir do idAreaDistribuicao
	 * @author wcsilva
	 * @throws Exception 
	 */
	public String consultarDescricaoForum(String idAreaDistribuicao) throws Exception{
		String retorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarDescricaoForum(idAreaDistribuicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
	 * Consulta codigo do f�rum a partir do idAreaDistribuicao
	 * @author lsbernardes
	 */
	public String consultarCodigoForum(String idAreaDistribuicao, FabricaConexao obFabricaConexao) throws Exception {
		String retorno = "";
		
		AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.consultarCodigoForum(idAreaDistribuicao);		
		
		return retorno;
	}
	
	/**
	 * M�todo que consulta as �reas de Distribui��o relacionadas � Serventia.
	 * @param idServentia - ID da Serventia
	 * @return lista de �reas de Distribui��o
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAreasDistribuicaoServentia(String idServentia) throws Exception{
		List listaAreasDistribuicao = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			listaAreasDistribuicao = obPersistencia.consultarAreasDistribuicaoServentia(idServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAreasDistribuicao;
	}
	
	/**
	 * Ativa uma area de distribui��o
	 * @param idAreaDistribuicao
	 * @throws Exception
	 * @author mmgomes
	 */
	public void ativarAreaDistribuicao(AreaDistribuicaoDt dados) throws Exception{
		atualizarStatus(dados, String.valueOf(AreaDistribuicaoDt.ATIVO));
	}
	
	/**
	 * Inativa uma area de distribui��o
	 * @param idAreaDistribuicao
	 * @throws Exception
	 * @author mmgomes
	 */
	public void inativarAreaDistribuicao(AreaDistribuicaoDt dados) throws Exception{
		atualizarStatus(dados, String.valueOf(AreaDistribuicaoDt.INATIVO));
	}
	
	/**
	 * Bloqueia uma area de distribui��o
	 * @param idAreaDistribuicao
	 * @throws Exception
	 * @author mmgomes
	 */
	public void bloquearAreaDistribuicao(AreaDistribuicaoDt dados) throws Exception{
		atualizarStatus(dados, String.valueOf(AreaDistribuicaoDt.BLOQUEADO));
	}
	
	/**
	 * Altera o status de uma area de distribui��o para ativo, inativo ou bloqueado.
	 * @param idAreaDistribuicao
	 * @param status
	 * @throws Exception
	 * @author mmgomes
	 */
	private void atualizarStatus(AreaDistribuicaoDt dados, String status) throws Exception{
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			
			obPersistencia.atualizarStatus(dados.getId(), status);
			
			String valorAtual = "[Id_AreaDistribuicao:" + dados.getId() + ";CodigoTemp:" + dados.getCodigoTemp() + "]";
			String valorNovo = "[Id_AreaDistribuicao:" + dados.getId() + ";CodigoTemp:" + status + "]";
					
			obLogDt = new LogDt("AreaDistribuicao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			
			obDados.setCodigoTemp(status);
			
			obLog.salvar(obLogDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
		
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	/**
	 * Consulta �rea Distribuicao (utilizando filtro por �rea distribui��o,
	 * F�rum e Subtipo de Serventia) e considerando um campo de ordena��o
	 * 
	 * @param descricao
	 * @param ordenacao
	 * @param posicao
	 * @param quantidadeRegistros
	 * @return
	 * @throws Exception
	 */
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	
	public String consultarDescricaoForumJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ForumNe Forumne = new ForumNe(); 
		stTemp = Forumne.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoServentiaSubtipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
		stTemp = ServentiaSubtipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoComarcaServentiaSubTipoJSON(String descricao, String idComarca, String idServentiaSubTipo, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAreasDistribuicaoComarcaServentiaSubTipoJSON(descricao, idComarca, idServentiaSubTipo, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauAtivasJSON(String descricao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAreasDistribuicaoSegundoGrauAtivasJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoAtivasJSON(String descricao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAreasDistribuicaoAtivasJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoPreprocessualAtivasJSON(String descricao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAreasDistribuicaoPreprocessualAtivasJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoCivelJSON(String descricao, String id_comarca, String id_Serventia_Usuario,  String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
		
			stTemp = obPersistencia.consultarAreasDistribuicaoCivelJSON(descricao, id_comarca, id_Serventia_Usuario, posicao);			

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * M�todo que consulta �reas de distribui��o criminais de primeiro grau.
	 * @param nome - nome da �rea de distribui��o
	 * @param id_comarca - Id da comarca da �rea de distribui��o
	 * @param posicao - posi��o da pagina��o
	 * @return lista de �reas de distribui��o criminais de primeiro grau.
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarAreasDistribuicaoPrimeiroGrauCriminalJSON(String descricao, String id_comarca, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
	
			stTemp = obPersistencia.consultarAreasDistribuicaoPrimeiroGrauCriminalJSON(descricao, id_comarca, posicao);
					
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoCriminalJSON(String descricao, String id_comarca, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);		
		
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
	
			stTemp = obPersistencia.consultarAreasDistribuicaoCriminalJSON(descricao, id_comarca, posicao);
					
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauCriminalJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual) throws Exception{
		String stTempList = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCriminalJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTempList;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauCriminalJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception{
		String stTempList = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCriminalJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual, turmaJulgadora);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTempList;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauCivelJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual) throws Exception{
		String stTempList = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCivelJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTempList;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauCivelJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception{
		String stTempList = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCivelJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual, turmaJulgadora);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTempList;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauJSON(String tempNomeBusca, String id_Comarca, String areaCodigo, String posicaoPaginaAtual) throws Exception{
		String stTempList = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			switch (areaCodigo) {
				case AreaDt.CIVEL:
					stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCivelJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
					break;

				case AreaDt.CRIMINAL:
					stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCriminalJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
					break;

				default:
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTempList;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauJSON(String tempNomeBusca, String id_Comarca, String areaCodigo, String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception{
		String stTempList = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());

			switch (areaCodigo) {
				case AreaDt.CIVEL:
					stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCivelJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual, turmaJulgadora);
					break;

				case AreaDt.CRIMINAL:
					stTempList = obPersistencia.consultarAreasDistribuicaoSegundoGrauCriminalJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual, turmaJulgadora);
					break;

				default:
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTempList;
	}
	
	public String consultarDescricaoServentiaJSON(String descricao, String idServentia, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoServentiaJSON(descricao, idServentia, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
	
	public List consultarListaServentias(String idAreaDistribuicao) throws Exception {
		List tempList = null;
		ServentiaNe neObjeto = new ServentiaNe();
		
		tempList = neObjeto.consultarListaServentias(idAreaDistribuicao);
				
		neObjeto = null;
		return tempList;
	}

	public List<DescritorComboDt> consultarDescricaoCombo(String descricao, String id_comarca, boolean civeis, boolean primeiroGrau) throws Exception {
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarDescricaoCombo(descricao, id_comarca, civeis, primeiroGrau);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public boolean isAreaDistribuicaoComCusta(String id_areadistribuicao ) throws Exception{

		 boolean retorno = false;
		 FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new AreaDistribuicaoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isAreaDistribuicaoComCusta(id_areadistribuicao); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}

	public String consultarAreasDistribuicaoCivelJSON(String descricao, String id_comarca, String id_Serventia_Usuario, boolean comCusta,  String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new  AreaDistribuicaoPs(obFabricaConexao.getConexao());
		
			stTemp = obPersistencia.consultarAreasDistribuicaoCivelJSON(descricao, id_comarca, id_Serventia_Usuario, comCusta, posicao);			

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public AreaDistribuicaoDt consultarId(String id_areadistribuicao, FabricaConexao obFabricaConexao) throws Exception{
		AreaDistribuicaoDt dtRetorno=null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		AreaDistribuicaoPs obPersistencia = new AreaDistribuicaoPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_areadistribuicao); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
}
