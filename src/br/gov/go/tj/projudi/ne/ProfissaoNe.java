package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.ps.AreaDistribuicaoPs;
import br.gov.go.tj.projudi.ps.ProfissaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProfissaoNe extends ProfissaoNeGen {

	private static final long serialVersionUID = -1753782750228379413L;

	public String Verificar(ProfissaoDt dados) {

		String stRetorno = "";

		if (dados.getProfissao().trim().equalsIgnoreCase("")) {
			return stRetorno += "O campo 'Profissão' é obrigatório!";
		}
		return stRetorno;
	}

	public ProfissaoDt consultarCodigo(String codigo_profissao) throws Exception {
		ProfissaoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProfissaoPs obPersistencia = new  ProfissaoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarCodigo(codigo_profissao);
			if (dtRetorno != null)
				obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public String consultarIdProfissao(String descricao, FabricaConexao obFabricaConexao) throws Exception {
		String retorno = null;

		ProfissaoPs obPersistencia = new  ProfissaoPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.consultarIdProfissao(descricao);

		return retorno;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProfissaoPs obPersistencia = new  ProfissaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}
