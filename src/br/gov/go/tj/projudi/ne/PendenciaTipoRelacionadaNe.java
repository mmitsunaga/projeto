package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoRelacionadaDt;
import br.gov.go.tj.projudi.ps.PendenciaTipoRelacionadaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class PendenciaTipoRelacionadaNe extends PendenciaTipoRelacionadaNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7327637415782548915L;

	protected static Map mapPendenciasRelacionadas = null;

	public String Verificar(PendenciaTipoRelacionadaDt dados) {

		String stRetorno = "";

		if (dados.getId_PendenciaTipoPrincipal().length() == 0) stRetorno += "O Campo Id_PendenciaTipoPrincipal � obrigat�rio.";
		//System.out.println("..nePendenciaTipoRelacionadaVerificar()");
		return stRetorno;

	}

	/**
	 * Consulta os relacionamentos poss�veis de pend�ncias para fazer o tratamento na hora de gerar pend�ncias,
	 * liberando a gera��o de uma ou mais pend�ncias simultaneamente.
	 * 
	 * @return Map que ficar� em mem�ria (n�o sendo necess�rio realizar acesso ao banco toda vez que necess�rio)
	 * @author msapaula
	 */
	public Map consultaPendenciaTipoRelacionadas() throws Exception {
		Map mapPendencias;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());

			if (mapPendenciasRelacionadas != null) {
				mapPendencias = mapPendenciasRelacionadas;
			} else {
				// Consulta as pend�ncias relacionadas
				mapPendencias = obPersistencia.consultaPendenciaTipoRelacionadas();
				mapPendenciasRelacionadas = mapPendencias;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return mapPendencias;
	}

	/**
	 * M�todo para atualizar o map das pend�ncias relacionadas.
	 * Ser� utilizado ap�s qualquer altera��o na tabela para que o map fique na mem�ria atualizado.
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public void atualizarPendenciaTipoRelacionadas() throws Exception{
		
		mapPendenciasRelacionadas = null;
		this.consultaPendenciaTipoRelacionadas();
		
	}

	/**
	 * M�todo que faz a verifica��o se um pend�ncia pode ser gerada juntamente com outra
	 * 
	 * @param id_PendenciaTipoPrincipal, pend�ncia principal
	 * @param id_PendenciaTipoRelacao, pend�ncia relacionada
	 * @return true, se pend�ncias podem ser adicionadas ao mesmo tempo
	 */
	public boolean verificaRelacionamentoPendencia(String id_PendenciaTipoPrincipal, String id_PendenciaTipoRelacao) {
		boolean boRelacionamento = false;

		List pendenciasRelacionadas = (List) mapPendenciasRelacionadas.get(id_PendenciaTipoPrincipal);
		if (pendenciasRelacionadas != null) {
			if (pendenciasRelacionadas.contains(id_PendenciaTipoRelacao)) boRelacionamento = true;
		}
		return boRelacionamento;
	}

	/**
	 * Sobrescrevendo m�todo do Gen para que ao final atualize o Map de pend�ncias que fica em mem�ria
	 * 
	 * @author msapaula
	 */
	public void salvarMultiplo(PendenciaTipoRelacionadaDt dados, String[] listaEditada) throws Exception {
		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt) lisGeral.get(i);
			boolean boEncontrado = false;
			//se tiver id vejo que o mesmo n�o esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")) {
				//verifico qual id saiu da lista editada
				for (int j = 0; j < listaEditada.length; j++)
					if (obDt.getId_PendenciaTipoRelacao().equalsIgnoreCase((String) listaEditada[j])) {
						boEncontrado = true;
						break;
					}
				//se o id do objeto n�o foi encontrado na lista editada coloco o objeto para ser excluido
				if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for (int j = 0; j < lisGeral.size(); j++) {
				PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt) lisGeral.get(j);
				if (obDt.getId_PendenciaTipoRelacao().equalsIgnoreCase((String) listaEditada[i]) && obDt.getId().equalsIgnoreCase("")) {
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			for (int i = 0; i < lisIncluir.size(); i++) {
				PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt) lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("PendenciaTipoRelacionada", obDt.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for (int i = 0; i < lisExcluir.size(); i++) {
				PendenciaTipoRelacionadaDt obDtTemp = (PendenciaTipoRelacionadaDt) lisExcluir.get(i);

				obLogDt = new LogDt("PendenciaTipoRelacionada", obDtTemp.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), obDtTemp.getPropriedades(), "");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();
			
			//Atualiza Map de pend�ncias relacionadas
			this.atualizarPendenciaTipoRelacionadas();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoPendenciaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		PendenciaTipoNe PendenciaTipone = new PendenciaTipoNe(); 
		stTemp = PendenciaTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = PendenciaTipone.getQuantidadePaginas();
		PendenciaTipone = null;
		
		return stTemp;
	}
	
	public List consultarPendenciaTipoPendenciaTipo(String id_pendenciatipoprincipal ) throws Exception {

		lisGeral=null;
		////System.out.println("..ne-consultarPendenciaTipoPendenciaTipoUlLiCheckBoxPendenciaTipoRelacionada" ); 
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			lisGeral=obPersistencia.consultarPendenciaTipoPendenciaTipoGeral( id_pendenciatipoprincipal);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return lisGeral;   
	}
	
	public void incluirMultiplo(PendenciaTipoRelacionadaDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt)lisGeral.get(j);
				if (obDt.getId_PendenciaTipoRelacao().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("PendenciaTipoRelacionada", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluirMultiplo(PendenciaTipoRelacionadaDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		List lisExcluir = new ArrayList();
		for (int i = 0; i < lisGeral.size(); i++) {
			PendenciaTipoRelacionadaDt obDt = (PendenciaTipoRelacionadaDt)lisGeral.get(i);
			//se tiver id vejo que o mesmo n�o esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("") && obDt.getId_PendenciaTipoRelacao().equalsIgnoreCase(id)){
				lisExcluir.add(obDt);
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaTipoRelacionadaPs obPersistencia = new PendenciaTipoRelacionadaPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisExcluir.size(); i++) {
				PendenciaTipoRelacionadaDt obDtTemp = (PendenciaTipoRelacionadaDt)lisExcluir.get(i); 

				obLogDt = new LogDt("PendenciaTipoRelacionada", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

}
