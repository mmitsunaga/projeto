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
	 * M�todo respons�vel por verificar se os campos obrigat�rios na tela do cadastro de permiss�o foram
	 * preenchidos
	 * 
	 * @author Keila Sousa Silva
	 * @param permissaoDt
	 * @return mensagemRetorno
	 * 
	 */
	public String Verificar(PermissaoDt permissaoDt) {
		String mensagemRetorno = "";
		// Verificar se o campo obrigat�rio "Permiss�o" foi preenchido
		if (permissaoDt.getPermissao().equalsIgnoreCase("")) {
			mensagemRetorno += "O campo 'Permiss�o' � obrigat�rio";
		}
		return mensagemRetorno;
	}

	/**
	 * M�todo respons�vel por consultar as permiss�es do tipo fun��o pertencentes � permiss�o pai passada como
	 * par�metro. Esse m�todo ir� retornar somente os valores de cada fun��o das permiss�es do tipo fun��o
	 * consultadas
	 * 
	 * @author Keila Sousa Silva
	 * @param id_PermissaoPai
	 * @param permissaoCodigoPai
	 * @return arrayFuncoesPermissao = array contendo os valores de cada fun��o das permiss�es do tipo fun��o
	 *         pertencentes � permiss�o pai passada como par�metro
	 * @throws Exception
	 */
	public String[] consultarFuncoesPermissao(String id_PermissaoPai, String permissaoPaiCodigo) throws Exception {
		String[] arrayFuncoesPermissao;
		List listaPermissoesTipoFuncao = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// Criar conex�o com o banco de dados
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			// Consultar as permiss�es do tipo fun��o pertencentes � permiss�o pai passada como par�metro
			listaPermissoesTipoFuncao = obPersistencia.consultarFuncoesPermissao(id_PermissaoPai, permissaoPaiCodigo);
			// Instanciar o objeto "arrayFuncoesPermissao"
			if (listaPermissoesTipoFuncao.size() > 0) {
				arrayFuncoesPermissao = new String[listaPermissoesTipoFuncao.size()];
			} else {
				arrayFuncoesPermissao = null;
			}
			/*
			 * Capturar o valor (0,1,2,3,4,5,6,7,8,9) da fun��o de cada permiss�o do tipo fun��o pertencente �
			 * permiss�o pai passada como par�metro
			 */
			for (int i = 0; i < listaPermissoesTipoFuncao.size(); i++) {
				PermissaoDt permissaoDtFuncao = (PermissaoDt) listaPermissoesTipoFuncao.get(i);
				/*
				 * Para capturar o c�digo da fun��o da permiss�o do tipo fun��o, pega-se o campo
				 * "PermissaoCodigo" da permiss�o do tipo fun��o e captura o �ltimo caracter desse c�digo. A
				 * posi��o do �ltimo caracter desse c�digo � o tamanho do c�digo da permiss�o pai
				 * (permissaoPaiCodigo.length()). Por exemplo: PermissaoCodigo do pai = 185 [ TAMANHO DA
				 * STRING (LENGTH) = 3; posi��o 0: 1; posi��o 1: 8; posi��o 2: 5]; PermissaoCodigo da
				 * permiss�o do tipo fun��o excluir = 1850 [ tamanho (length) = 4; posi��o 0: 1; posi��o 1: 8;
				 * posi��o 2: 5; POSI��O 3: 0 (0 = C�DGIO DA FUN��O EXCLUIR) ]
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
	 * M�todo respons�vel por buscar e retornar o pr�ximo c�digo de permiss�o (PermissaoCodigo) a ser
	 * utilizado para uma nova permiss�o
	 * 
	 * @author Keila Sousa Silva
	 * @return permissaoCodigoProximo = maior c�digo das permiss�es n�o fun��es + 1, que nos d� o pr�ximo
	 *         c�digo (PermissaoCodigo) a ser utilizado para uma nova permiss�o
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
	 * M�todo respons�vel por salvar (INSERIR E SALVAR) permiss�es. O m�todo para salvar (INSERIR)permiss�es
	 * do tipo fun��o � invocado nesse m�todo. Em resumo esse m�todo executa as opera��es de INSER��O de
	 * permiss�es n�o fun��o e ALTERA��O de permiss�es que s�o ou que n�o s�o do tipo fun��o. J� a opera��o de
	 * inser��o de permiss�es do tipo fun��o � feita em outro m�todo [salvarFuncoesPermissao(PermissaoDt
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
			// Criar conex�o com o banco de dados
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			// O id do objeto ser� usado para saber se ele j� existe ou n�o
			if (permissaoDt.getId().equalsIgnoreCase("")) {
				// Definir o valor do c�digo da permiss�o (PermissaoCodigo)
				permissaoDt.setPermissaoCodigo(getPermissaoCodigoProximo());
				// Preparar o objeto de log (permissaoDt = valor novo = valor a ser inserido)
				logDt = new LogDt("Permissao", permissaoDt.getId(), permissaoDt.getId_UsuarioLog(), permissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", permissaoDt.getPropriedades());
				// SALVAR PERMISS�O
				obPersistencia.inserir(permissaoDt);
				// SALVAR FUN��ES DA PERMISS�O
				salvarFuncoesPermissao(permissaoDt.getPermissaoCodigo(),permissaoDt.getPermissao(), permissaoDt.getId() , permissaoDt.getId_UsuarioLog(), permissaoDt.getIpComputadorLog(), obFabricaConexao);
			} else {
				// Preparar o objeto de log (obDAdos = valor atual; permissaoDt = valor novo)
				logDt = new LogDt("Permissao", permissaoDt.getId(), permissaoDt.getId_UsuarioLog(), permissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), permissaoDt.getPropriedades());
				// ALTERAR PERMISS�O
				obPersistencia.alterar(permissaoDt);
			}
			// SALVAR LOG DO SALVAR DA PERMISS�O
			obDados.copiar(permissaoDt);
			obLog.salvar(logDt, obFabricaConexao);
			// FINALIZAR TRANSA��O
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSA��O
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHAR CONEX�O
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * M�todo respons�vel por salvar (INSERIR) as permiss�es do tipo fun��o
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
	 * M�todo respons�vel por verificar se a permiss�o passada como par�metro possui depend�ncias com tuplas
	 * das tabelas "Permissao", "GrupoPermissao" e "UsuarioServentiaPermissao". Caso haja depend�ncias a
	 * permiss�o n�o poder� ser exclu�da
	 * 
	 * @author Keila Sousa Silva
	 * @param permissaoDt
	 * @return mensagemRetorno
	 * @throws Exception
	 */
	public String verificarDependenciasPermissao(PermissaoDt permissaoDt) throws Exception{
		String mensagemRetorno = "";
		
		/*
		 * Verificar se a permiss�o a ser exclu�da possui depend�ncias com tuplas da tabela "Permissao",
		 * ou seja, ela possui permiss�es filhas
		 */
		String[] arrayPermissoesFilhas = consultarFuncoesPermissao(permissaoDt.getId(), permissaoDt.getPermissaoCodigo());
		if (arrayPermissoesFilhas != null) {
			mensagemRetorno += " Permiss�o possui depend�ncias com a tabela Permiss�o. ";
		}

		/*
		 * Verificar se a permiss�o a ser exclu�da possui depend�ncias com tuplas da tabela
		 * "GrupoPermissao"
		 */
		GrupoPermissaoNe grupoPermissaoNe = new GrupoPermissaoNe();
		List listaGruposPermissao = grupoPermissaoNe.consultarGruposPermissao(permissaoDt.getId());
		if (listaGruposPermissao.size() != 0) {
			mensagemRetorno += " Permiss�o possui depend�ncias com a tabela GrupoPermiss�o. ";
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
