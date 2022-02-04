package br.gov.go.tj.projudi.ne;


import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoSituacaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CondenacaoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class CondenacaoExecucaoNe extends CondenacaoExecucaoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 4230074388393127700L;

    //---------------------------------------------------------
	public  String Verificar(CondenacaoExecucaoDt dados ) {

		String stRetorno="";

		if (dados.getCrimeExecucao().length()==0)
			stRetorno += "O Campo CrimeExecucao é obrigatório.";
		////System.out.println("..neCondenacaoExecucaoVerificar()");
		return stRetorno;

	}

	/**
	 * Salva lista de condenações de um processo
	 * 
	 * @param listaCondenacoes: lista de Condenações a serem salvas
	 * @param id_ProcessoExecucao: identificação do processo execução.
	 * @param logDt: identificação do usuário. Se for null, o objeto já deve conter o Id_Usuario e IpComputador
	 * @param obFabricaConexao: conexão
	 * 
	 * @author wcsilva
	 */
	public void salvarListaCondenacao(List listaCondenacoes, String id_ProcessoExecucao, LogDt logDt, FabricaConexao obFabricaConexao)
			throws Exception {
		//Salvando condenações da lista
		if (listaCondenacoes != null) {
			for (int i = 0; i < listaCondenacoes.size(); i++) {
				CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacoes.get(i);
				if (id_ProcessoExecucao != null) condenacaoExecucaoDt.setId_ProcessoExecucao(id_ProcessoExecucao);
				if (logDt != null){
					condenacaoExecucaoDt.setId_UsuarioLog(logDt.getId_Usuario());
					condenacaoExecucaoDt.setIpComputadorLog(logDt.getIpComputador());
				}
				if (condenacaoExecucaoDt.getId_CondenacaoExecucaoSituacao().equals(String.valueOf(CondenacaoExecucaoSituacaoDt.NAO_APLICA))
						|| condenacaoExecucaoDt.getId_CondenacaoExecucaoSituacao().equals(String.valueOf(CondenacaoExecucaoSituacaoDt.EXTINTA_CUMPRIMENTO))){
					condenacaoExecucaoDt.setTempoCumpridoExtintoDias("");
				}
				this.salvar(condenacaoExecucaoDt, obFabricaConexao);
			}
		}	
	}
	
	public void salvar(CondenacaoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().equalsIgnoreCase("" ) ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("CondenacaoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("CondenacaoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 * Lista as condenacoes de um Trânsito em Julgado
	 * @param id_ProcessoExecucao: identificação do ProcessoExecução
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 */
	public List listarCondenacaoDaAcaoPenal(String id_ProcessoExecucao) throws Exception{
		List lista;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarCondenacaoDaAcaoPenal(id_ProcessoExecucao); 

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	public String listarCondenacaoDaAcaoPenalJSON(String id_ProcessoExecucao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.listarCondenacaoDaAcaoPenalJSON(id_ProcessoExecucao, posicao); 

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Lista as condenacoes NÃO EXTINTAS de um Trânsito em Julgado
	 * @param id_ProcessoExecucao: identificação do ProcessoExecução
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 */
	public List listarCondenacaoNaoExtintaDaAcaoPenal(String id_ProcessoExecucao) throws Exception{
		List lista;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarCondenacaoNaoExtintaDaAcaoPenal(id_ProcessoExecucao); 

		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return lista;
	}
	
	/**
	 * lista todas as condenações não extintas do processo de execução penal
	 * @param id_Processo: identificação do processo de execução penal
	 * @return listaCondenacao: lista com as condenações
	 * @throws Exception
	 */
	public List listarCondenacaoNaoExtintaDoProcesso(String id_Processo) throws Exception{
		List lista;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarCondenacaoNaoExtintaDoProcesso(id_Processo); 

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Exclui lista de condenações de um processo
	 * @param listaCondenacoes: lista de Condenações a serem excluidas
	 * @param usuarioDt: usuário que está efetuando a exclusão (Ps.: Parâmetro inexistente)
	 * @author wcsilva
	 */
	public void excluirListaCondenacao(List listaCondenacoes) throws Exception {
		////System.out.println("..ne-excluirListaCondenacao" );
	    FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			if (listaCondenacoes != null) {
				for(int i=0; i < listaCondenacoes.size(); i++) {
					CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt)listaCondenacoes.get(i);
					this.excluir(condenacaoExecucaoDt, obFabricaConexao);
				}
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Exclui lista de condenações de um processo
	 * @param listaCondenacoes: lista de Condenações a serem salvas
	 * @param obFabricaConexao: conexão
	 * @author wcsilva
	 */
	public void excluirListaCondenacao(List listaCondenacoes, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		if (listaCondenacoes != null) {
			for (int i = 0; i < listaCondenacoes.size(); i++) {
				CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) listaCondenacoes.get(i);
				condenacaoExecucaoDt.setId_UsuarioLog(logDt.getId_Usuario());
				condenacaoExecucaoDt.setIpComputadorLog(logDt.getIpComputador());
				this.excluir(condenacaoExecucaoDt, obFabricaConexao);
			}
		}
	}
	
	public void excluir(CondenacaoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("CondenacaoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); dados.limpar();
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 *  Verifica se o crime da condenação é considerado hediondo para cálculo do livramento condicional e progressão de regime. 
	 * @return listaCondenacao: lista com as condenações
	 * @author wcsilva
	 */
	public List verificarParametroCrime(List listaCondenacao) throws Exception {
		////System.out.println("..ne-verificarParametroCrime" );
	    FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			
			if (listaCondenacao != null) {
				listaCondenacao = obPersistencia.setParametroCrime(listaCondenacao);
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaCondenacao;
	}
	
	/**
	 * Consulta o tempo total da condenação em dias, de um Trânsito em Julgado ou Guia de Recolhimento Provisória (apenas as condenações não extintas)
	 * @param id_ProcessoExecucao, identificação do ProcessoExecucao
	 * @author wcsilva
	 * @return retorna o total das condenações em dias
	 */
	public String consultarTempoTotalCondenacaoDias(String id_ProcessoExecucao) throws Exception {
		String diasCondenacao = "";
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			diasCondenacao = obPersistencia.consultarTempoTotalCondenacaoDias(id_ProcessoExecucao); 
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return diasCondenacao;
	}
	
	/**
     * Consulta os dados do crime das condenações através do idProcessoExecucao, INDEPENDENTE SE O idProcessoExecução É DE UMA AÇÃO PENAL ATIVA OU NÃO.
     * @param idProcessoExecucao: id do processo execucao
     * @return lista de condenações de uma ação penal
     * @author kbsriccioppo
     */
	public List listarCrimeCondenacao(String idProcessoExecucao) throws Exception{
		List listaCondenacao = new ArrayList();
		listaCondenacao = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			listaCondenacao = obPersistencia.listarCrimeCondenacao(idProcessoExecucao); 

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaCondenacao;
	}
	
	/**
	 * Consulta a descricao da situação da condenacao
	 * @param id_situacao
	 * @author kbsriccioppo
	 * @return a descrição da situação
	 */
	public String consultarCondenacaoSituacao(String id_situacao) throws Exception {
		String descricaoSituacao = "";
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			descricaoSituacao = obPersistencia.consultarCondenacaoSituacao(id_situacao); 
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return descricaoSituacao;
	}
	
	/**
	 * consulta os crimes do processo de execução penal
	 * @param idProcesso: identificação do processo de execução penal
	 * @return String
	 * @throws Exception
	 */
	public String consultarCrimesAtivosProcesso(String idProcesso) throws Exception{
		String crimes = "";
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CondenacaoExecucaoPs obPersistencia = new CondenacaoExecucaoPs(obFabricaConexao.getConexao());
			crimes = obPersistencia.consultarCrimesAtivosProcesso(idProcesso); 
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return crimes;
	}
}
