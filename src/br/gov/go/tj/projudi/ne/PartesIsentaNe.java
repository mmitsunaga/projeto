package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PartesIsentaDt;
import br.gov.go.tj.projudi.ps.PartesIsentaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class PartesIsentaNe extends PartesIsentaNeGen{

//

//---------------------------------------------------------
	public  String Verificar(PartesIsentaDt dados ) {

		String stRetorno="";

		if (dados.getNome().length()==0)
			stRetorno += "O Campo Nome é é obrigatório.";
		if (dados.getCnpj().length()==0)
			stRetorno += "O Campo CNPJ é é obrigatório.";

		return stRetorno;

	}
	
	public  String verificarExclusao(PartesIsentaDt dados ) {

		String stRetorno="";

		if (dados.getDataBaixa().length() > 0)
			stRetorno += "A parte isenta já foi baixada!.";

		return stRetorno;

	}
	
	public String consultarPartesIsentaslJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PartesIsentaPs obPersistencia = new PartesIsentaPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarPartesIsentaslJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	public void BaixarParteIsenta(PartesIsentaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PartesIsentaPs obPersistencia = new PartesIsentaPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("PartesIsenta",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDados.getPropriedades(), dados.getPropriedades());
			obPersistencia.BaixarParteIsenta(dados);

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public boolean isParteIsenta(String CNPJ) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PartesIsentaPs obPersistencia = new PartesIsentaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.isParteIsenta(CNPJ);
		} finally {
				obFabricaConexao.fecharConexao();
		}
		return retorno;
	}

 //---------------------------------------------------------

}
