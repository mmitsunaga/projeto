package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.ps.ClassificadorPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ClassificadorNe extends ClassificadorNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -728278615861502408L;

    /**
	 * Verfica campos obrigatórios
	 */
	public String Verificar(ClassificadorDt dados) {
		String stRetorno = "";

		if (dados.getClassificador().trim().equalsIgnoreCase("")) stRetorno += "Descrição é campo obrigatório. \n";
		if (dados.getPrioridade().trim().equalsIgnoreCase("")) stRetorno += "Prioridade é campo obrigatório. \n";
		if (dados.getId_Serventia().trim().equalsIgnoreCase("")) stRetorno += "É necessário selecionar uma Serventia. \n";

		return stRetorno;
	}

	/**
	 * Verfica campos obrigatórios no cadastro de Classificador
	 * 
	 * @param dados, dt com dados do cadastro
	 * @param grupoCodigo, usuário que está cadastrando
	 * 
	 * @author msapaula
	 */
/*	public String verificarDados(ClassificadorDt dados, String grupoCodigo) {
		String stRetorno = "";

		if (dados.getClassificador().trim().equalsIgnoreCase("")) stRetorno += "Descrição é campo obrigatório. \n";
		if (Funcoes.StringToInt(grupoCodigo) == GrupoDt.ADMINISTRADORES && dados.getId_Serventia().trim().equalsIgnoreCase("")) stRetorno += "É necessário selecionar uma Serventia. \n";

		return stRetorno;
	}*/

	/**
	 * Método que consulta os classificadores disponíveis para uma determinada serventia
	 * 
	 * @param descricao, filtro para descrição do classificador
	 * @param posicao, parâmetro para paginação
	 * @param id_Serventia, identificação da serventia do usuário
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarClassificadorServentia(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ClassificadorPs obPersistencia = new ClassificadorPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarClassificadorServentia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Método que consulta o classificador de um processo
	 * 
	 * @param id_Processo, identificação do processo
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public ClassificadorDt consultarClassificadorProcesso(String id_Processo) throws Exception{
		ClassificadorDt classificadorDt = null;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ClassificadorPs obPersistencia = new ClassificadorPs(obFabricaConexao.getConexao());
			classificadorDt = obPersistencia.consultarClassificadorProcesso(id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return classificadorDt;
	}

	/**
	 * Consulta de Classificadores cadastrados. Se grupo é Administrador, lista todos os tipos cadastrados.
	 * Caso contrário, serão listados os classificadores de acordo com a serventia.
	 * 
	 * @param descricao, filtro para descrição do classificador
	 * @param posicao, parâmetro para paginação
	 * @param id_Serventia, identificação da serventia do usuário
	 * @param grupoCodigo, código do grupo do usuário
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarClassificador(String descricao, String posicao, String id_Serventia, String grupoCodigo) throws Exception{
		List tempList = null;
		
		switch (Funcoes.StringToInt(grupoCodigo)) {
			case GrupoDt.ADMINISTRADORES:
				tempList = consultarDescricao(descricao, posicao);
				break;
			default:
				tempList = consultarClassificadorServentia(descricao, posicao, id_Serventia);
				QuantidadePaginas = getQuantidadePaginas();
				break;
		}

		return tempList;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String idServentia) throws Exception{
		return consultarDescricaoJSON(descricao, posicao, idServentia, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String idServentia, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null; 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ClassificadorPs obPersistencia = new  ClassificadorPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, idServentia, ordenacao, quantidadeRegistros);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarClassificadorServentiaJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ClassificadorPs obPersistencia = new ClassificadorPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarClassificadorServentiaJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Método que valida a exclusão do classificador informando se há processos vinculados ao mesmo.
	 * @param classificadorDt
	 * @return mensagem com a quantidade de processos vinculados ao classificador
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String validarExclusaoClassificador(ClassificadorDt classificadorDt) throws Exception {
		String stRetorno = "";
		ProcessoNe processoNe = new ProcessoNe();
		
		try {
			int quantidade = processoNe.consultarQuantidadeProcessosAtivosClassificador(classificadorDt.getId());
			int quantidadeArquivado = processoNe.consultarQuantidadeProcessosArquivadosClassificador(classificadorDt.getId());

			if(quantidade > 0) {
				stRetorno += "Há " + quantidade + " processo(s) ativo(s) e " + quantidadeArquivado + " processo(s) arquivado(s) vinculados a esse classificador. \n Certifique-se que o mesmo pode ser excluído, pois a exclusão não poderá ser desfeita. \n\n Clique em Confirmar Excluir caso deseje efetuar a exclusão.";
			} else {
				stRetorno += "Não há processos vinculados a esse Classificador. \n Entretanto, certifique-se que o mesmo pode ser excluído, pois a exclusão não poderá ser desfeita.";
			}
		} catch (Exception e) {}
		
		return stRetorno;
	}
			
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ClassificadorPs obPersistencia = new ClassificadorPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarDescricaoCombo(descricao);
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public static boolean podeClassificar(UsuarioNe UsuarioSessao) {
		switch(UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt()) {
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
			case GrupoTipoDt.PRESIDENCIA:
			case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU :
			case GrupoTipoDt.ASSISTENTE_GABINETE :
			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO :
			case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				return true;
			default: return false;
		}
	}

	public int classificarProcessos(String cpfPoloAtivo,boolean boPoloAtivoNull, String cpfPoloPassivel, boolean boPoloaPassivoNull, String id_proc_tipo, String id_assunto, String id_classificador, String maxValor, String minValor, String id_classificarAlteracao, String id_serv) throws Exception {
		int qtdRetorno = 0;

		qtdRetorno = (new ProcessoNe()).classificarProcessos(cpfPoloAtivo, boPoloAtivoNull, cpfPoloPassivel, boPoloaPassivoNull, id_proc_tipo, id_assunto, id_classificador, maxValor, minValor, id_classificarAlteracao, id_serv);

		return qtdRetorno;
	}

//	public List<?> consultarProcessosClassificar(String cpfPoloAtivo, boolean boPoloAtivoNull,  String cpfPoloPassivel, boolean boPoloaPassivoNull, String id_proc_tipo, String id_assunto, String id_classificador, String maxValor, String minValor, String id_serv, String posicao) throws Exception {
//		List temList = null;
//		ProcessoNe  procNe = new ProcessoNe();
//		temList = procNe.consultarProcessosClassificar(cpfPoloAtivo,boPoloAtivoNull, cpfPoloPassivel,boPoloaPassivoNull , id_proc_tipo, id_assunto, id_classificador, maxValor, minValor, id_serv, posicao);
//		QuantidadePaginas = procNe.getQuantidadePaginas();
//		return temList;
//	}

}
