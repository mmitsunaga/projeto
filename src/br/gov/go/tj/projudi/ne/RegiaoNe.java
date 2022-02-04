package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.ps.RegiaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class RegiaoNe extends RegiaoNeGen {

	private static final long serialVersionUID = -2958300550127271706L;

	public String Verificar(RegiaoDt dados) {
		String stRetorno = "";

		if (dados.getRegiao().equalsIgnoreCase("")) {
			stRetorno += "Descri��o � � obrigat�rio.";
		}
		if (dados.getRegiaoCodigo().equalsIgnoreCase("")) {
			stRetorno += "C�digo � � obrigat�rio.";
		}
		if (dados.getId_Comarca().equalsIgnoreCase("")) {
			stRetorno += "Comarca � � obrigat�rio.";
		}		

		return stRetorno;

	}
	
	public String VerificarCodigo(RegiaoDt dados) throws Exception {
		RegiaoDt regiaoCadastrada = this.consultarCodigo(dados.getRegiaoCodigo());
		
		if (regiaoCadastrada != null) {
			if ((dados.getId() == null || dados.getId().trim().length() == 0) ||
				(!dados.getId().trim().equalsIgnoreCase(regiaoCadastrada.getId().trim()))) {
				return "O c�digo informado j� est� sendo utilizado pela regi�o " + regiaoCadastrada.getRegiao() + ".";	
			} 
		}
		
		return "";
	}

	public String consultarDescricaoJSON(String descricao, String comarca, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegiaoPs obPersistencia = new  RegiaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, comarca, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consulta uma Regi�o a partir do nome exato de uma Comarca.
	 * @param regiao - nome da regi�o
	 * @param comarca - nome da comarca
	 * @param posicao - posi��o da pagina��o
	 * @return nomes das regi�es encontradas
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarDescricaoPorComarcaJSON(String regiao, String idcomarca, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegiaoPs obPersistencia = new  RegiaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoPorComarcaJSON(regiao, idcomarca, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe();
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public RegiaoDt consultarCodigo(String codigoRegiao ) throws Exception {

		RegiaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;

		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			RegiaoPs obPersistencia = new RegiaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarCodigo(codigoRegiao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Recebe o id da comarca e retorna o id da regi�o de menor c�digo dela.
	 * Utilizado para preencher automaticamente o campo de regi�o no caso das
	 * escalas que forem de plant�o pois, nestes casos, este campo ser� preenchido
	 * apenas para evitar problema com a restri��o do banco de dados.
	 * @param idComarca
	 * @return idRegiao
	 * @throws Exception
	 */
	public String consultarIdPrimeraRegiaoComarca(String idComarca) throws Exception {
		String idRegiao = null;
		FabricaConexao obFabricaConexao =null;
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			RegiaoPs obPersistencia = new RegiaoPs(obFabricaConexao.getConexao());
			idRegiao = obPersistencia.consultarIdPrimeraRegiaoComarca(idComarca);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return idRegiao;
	}

}
