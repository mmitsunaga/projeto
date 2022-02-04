package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.ps.PermissaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class PermissaoNe extends PermissaoNeGen {

    private static final long serialVersionUID = -916183956279798225L;

    /**
	 * Método responsável por verificar se os campos obrigatórios na tela do cadastro de permissão foram
	 * preenchidos
	 * 
	 * @author Keila Sousa Silva
	 * @param permissaoDt
	 * @return mensagemRetorno
	 * 
	 */
	public String Verificar(PermissaoDt permissaoDt) {
		String mensagemRetorno = "";
		// Verificar se o campo obrigatório "Permissão" foi preenchido
		if (permissaoDt.getPermissao().equalsIgnoreCase("")) {
			mensagemRetorno += "O campo 'Permissão' é obrigatório";
		}
		return mensagemRetorno;
	}

	/**
	 * Método responsável por consultar as permissões do tipo função pertencentes à permissão pai passada como
	 * parâmetro. Esse método irá retornar somente os valores de cada função das permissões do tipo função
	 * consultadas
	 * 
	 * @author Keila Sousa Silva
	 * @param id_PermissaoPai
	 * @param permissaoCodigoPai
	 * @return arrayFuncoesPermissao = array contendo os valores de cada função das permissões do tipo função
	 *         pertencentes à permissão pai passada como parâmetro
	 * @throws Exception
	 */
	public String[] consultarFuncoesPermissao(String id_PermissaoPai, String permissaoPaiCodigo) throws Exception {
		String[] arrayFuncoesPermissao;
		List listaPermissoesTipoFuncao = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// Criar conexão com o banco de dados
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			// Consultar as permissões do tipo função pertencentes à permissão pai passada como parâmetro
			listaPermissoesTipoFuncao = obPersistencia.consultarFuncoesPermissao(id_PermissaoPai, permissaoPaiCodigo);
			// Instanciar o objeto "arrayFuncoesPermissao"
			if (listaPermissoesTipoFuncao.size() > 0) {
				arrayFuncoesPermissao = new String[listaPermissoesTipoFuncao.size()];
			} else {
				arrayFuncoesPermissao = null;
			}
			/*
			 * Capturar o valor (0,1,2,3,4,5,6,7,8,9) da função de cada permissão do tipo função pertencente à
			 * permissão pai passada como parâmetro
			 */
			for (int i = 0; i < listaPermissoesTipoFuncao.size(); i++) {
				PermissaoDt permissaoDtFuncao = (PermissaoDt) listaPermissoesTipoFuncao.get(i);
				/*
				 * Para capturar o código da função da permissão do tipo função, pega-se o campo
				 * "PermissaoCodigo" da permissão do tipo função e captura o último caracter desse código. A
				 * posição do último caracter desse código é o tamanho do código da permissão pai
				 * (permissaoPaiCodigo.length()). Por exemplo: PermissaoCodigo do pai = 185 [ TAMANHO DA
				 * STRING (LENGTH) = 3; posição 0: 1; posição 1: 8; posição 2: 5]; PermissaoCodigo da
				 * permissão do tipo função excluir = 1850 [ tamanho (length) = 4; posição 0: 1; posição 1: 8;
				 * posição 2: 5; POSIÇÃO 3: 0 (0 = CÓDGIO DA FUNÇÃO EXCLUIR) ]
				 */
				arrayFuncoesPermissao[i] = String.valueOf(permissaoDtFuncao.getPermissaoCodigo().charAt(permissaoPaiCodigo.length()));
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return arrayFuncoesPermissao;
	}
	
	public PermissaoDt[] consultaFuncoesPermissao(String permissaoPaiCodigo) throws Exception {
		PermissaoDt[] listaPermissoesTipoFuncao;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			listaPermissoesTipoFuncao = obPersistencia.consultaFuncoesPermissao(permissaoPaiCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaPermissoesTipoFuncao;
	}

	/**
	 * Método responsável por buscar e retornar o próximo código de permissão (PermissaoCodigo) a ser
	 * utilizado para uma nova permissão
	 * 
	 * @author Keila Sousa Silva
	 * @return permissaoCodigoProximo = maior código das permissões não funções + 1, que nos dá o próximo
	 *         código (PermissaoCodigo) a ser utilizado para uma nova permissão
	 * @throws Exception
	 */
	public String getPermissaoCodigoProximo() throws Exception {
		int permissaoCodigoProximo = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			permissaoCodigoProximo = Funcoes.StringToInt(obPersistencia.consultarProximaPermissaoCodigo());
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return String.valueOf(permissaoCodigoProximo);
	}

	/**
	 * Método responsável por salvar (INSERIR E SALVAR) permissões. O método para salvar (INSERIR)permissões
	 * do tipo função é invocado nesse método. Em resumo esse método executa as operações de INSERÇÃO de
	 * permissões não função e ALTERAÇÃO de permissões que são ou que não são do tipo função. Já a operação de
	 * inserção de permissões do tipo função é feita em outro método [salvarFuncoesPermissao(PermissaoDt
	 * permissaoDt, UsuarioDt usuarioDt, FabricaConexao fabricaConexao)] invocado aqui
	 * 
	 * @author Keila Sousa Silva
	 * @param permissaoDt
	 * @throws Exception
	 */
	public void salvar(PermissaoDt permissaoDt) throws Exception {
		LogDt logDt;
		FabricaConexao obFabricaConexao = null;
		try{
			// Criar conexão com o banco de dados
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			// O id do objeto será usado para saber se ele já existe ou não
			if (permissaoDt.getId().equalsIgnoreCase("")) {
				// Definir o valor do código da permissão (PermissaoCodigo)
				permissaoDt.setPermissaoCodigo(getPermissaoCodigoProximo());
				// Preparar o objeto de log (permissaoDt = valor novo = valor a ser inserido)
				logDt = new LogDt("Permissao", permissaoDt.getId(), permissaoDt.getId_UsuarioLog(), permissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", permissaoDt.getPropriedades());
				// SALVAR PERMISSÃO
				obPersistencia.inserir(permissaoDt);
				// SALVAR FUNÇÕES DA PERMISSÃO
				salvarFuncoesPermissao(permissaoDt.getPermissaoCodigo(),permissaoDt.getPermissao(), permissaoDt.getId() , permissaoDt.getId_UsuarioLog(), permissaoDt.getIpComputadorLog(), obFabricaConexao);
			} else {
				// Preparar o objeto de log (obDAdos = valor atual; permissaoDt = valor novo)
				logDt = new LogDt("Permissao", permissaoDt.getId(), permissaoDt.getId_UsuarioLog(), permissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), permissaoDt.getPropriedades());
				// ALTERAR PERMISSÃO
				obPersistencia.alterar(permissaoDt);
			}
			// SALVAR LOG DO SALVAR DA PERMISSÃO
			obDados.copiar(permissaoDt);
			obLog.salvar(logDt, obFabricaConexao);
			// FINALIZAR TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSAÇÃO
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHAR CONEXÃO
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável por salvar (INSERIR) as permissões do tipo função
	 * 
	 * @author Keila Sousa Silva
	 * @param permissaoDt
	 * @param fabricaConexao
	 * @throws Exception
	 */
	public void salvarFuncoesPermissao(String permissaoCodigoPai, String permissaoPai, String Id_Permissao, String Id_UsuarioLog, String IpComputadorLog, FabricaConexao obFabricaConexao) throws Exception {
		LogDt logDt = null;
		PermissaoDt permissaoDtFuncao = null;
		
		PermissaoPs obPersistencia = new  PermissaoPs(obFabricaConexao.getConexao());
		for (int i = 0; i <= 9; i++) {
			String permissaoCodigoFuncao = String.valueOf(Funcoes.StringToInt(permissaoCodigoPai) * 10 + i);
			permissaoDtFuncao = obPersistencia.consultarPermissaoCodigo(permissaoCodigoFuncao);
			if (permissaoDtFuncao == null) {
				permissaoDtFuncao = new PermissaoDt();
				permissaoDtFuncao.setId_PermissaoPai(Id_Permissao);
				permissaoDtFuncao.setPermissaoCodigo(String.valueOf(Funcoes.StringToInt(permissaoCodigoPai) * 10 + i));
				permissaoDtFuncao.setPermissao(permissaoPai + " " + permissaoDtFuncao.getNomeFuncao(i));
				permissaoDtFuncao.setEMenu("false");
				permissaoDtFuncao.setLink("#");
				permissaoDtFuncao.setTitulo("");
				permissaoDtFuncao.setIrPara("userMainFrame");
				permissaoDtFuncao.setOrdenacao("1000");
				permissaoDtFuncao.setCodigoTemp("");
				logDt = new LogDt("Permissao", Id_Permissao, Id_UsuarioLog, IpComputadorLog, String.valueOf(LogTipoDt.Incluir), "", permissaoDtFuncao.getPropriedades());
				obPersistencia.inserir(permissaoDtFuncao);
			}
		}
		// SALVAR LOG
		if (permissaoDtFuncao != null) {
			obDados.copiar(permissaoDtFuncao);
		}
		if (logDt != null) {
			obLog.salvar(logDt, obFabricaConexao);
		}
		
	}

	/**
	 * Método responsável por verificar se a permissão passada como parâmetro possui dependências com tuplas
	 * das tabelas "Permissao", "GrupoPermissao" e "UsuarioServentiaPermissao". Caso haja dependências a
	 * permissão não poderá ser excluída
	 * 
	 * @author Keila Sousa Silva
	 * @param permissaoDt
	 * @return mensagemRetorno
	 * @throws Exception
	 */
	public String verificarDependenciasPermissao(PermissaoDt permissaoDt) throws Exception{
		String mensagemRetorno = "";
		
		/*
		 * Verificar se a permissão a ser excluída possui dependências com tuplas da tabela "Permissao",
		 * ou seja, ela possui permissões filhas
		 */
		String[] arrayPermissoesFilhas = consultarFuncoesPermissao(permissaoDt.getId(), permissaoDt.getPermissaoCodigo());
		if (arrayPermissoesFilhas != null) {
			mensagemRetorno += " Permissão possui dependências com a tabela Permissão. ";
		}

		/*
		 * Verificar se a permissão a ser excluída possui dependências com tuplas da tabela
		 * "GrupoPermissao"
		 */
		GrupoPermissaoNe grupoPermissaoNe = new GrupoPermissaoNe();
		List listaGruposPermissao = grupoPermissaoNe.consultarGruposPermissao(permissaoDt.getId());
		if (listaGruposPermissao.size() != 0) {
			mensagemRetorno += " Permissão possui dependências com a tabela GrupoPermissão. ";
		}

		
		return mensagemRetorno;
	}

	public List consultarPermissoes() throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarPermissoes();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List consultarTodasPermissoes(String stGrupoId) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarTodasPermissoes(stGrupoId);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List getMenu(String id_UsuarioServentia, String id_Grupo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarMenusUsuarioGrupo(id_UsuarioServentia, id_Grupo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List getMenuEspecial(String id_UsuarioServentia, String id_Grupo, int permissaoEspecialCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarMenusEspecialUsuarioGrupo(id_UsuarioServentia, id_Grupo, permissaoEspecialCodigo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	public void excluirFuncoes(PermissaoDt permissaoDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Permissao", permissaoDt.getId(), permissaoDt.getId_UsuarioLog(),permissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),permissaoDt.getPropriedades(),"");
			obPersistencia.excluirFuncoes(permissaoDt.getId()); 
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoJSON(String codigo, String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new  PermissaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(codigo, descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoPermissaoEspecialJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		PermissaoEspecialNe PermissaoEspecialne = new PermissaoEspecialNe(); 
		stTemp = PermissaoEspecialne.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
}
