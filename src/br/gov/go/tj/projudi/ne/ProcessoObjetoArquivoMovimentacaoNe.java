package br.gov.go.tj.projudi.ne;


import com.softwareag.common.resourceutilities.message.MessageException;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoMovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.ps.ProcessoObjetoArquivoMovimentacaoPs;
import br.gov.go.tj.projudi.ps.ProcessoObjetoArquivoPs;
import br.gov.go.tj.projudi.ps.ProcessoPartePrisaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoObjetoArquivoMovimentacaoNe extends ProcessoObjetoArquivoMovimentacaoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//---------------------------------------------------------
	public  String Verificar(ProcessoObjetoArquivoMovimentacaoDt dados ) {

		String stRetorno="";

		if (dados.getProcessoObjetoArquivoMovimentacao().length()==0)
			stRetorno += "O Campo ProcessoObjetoArquivoMovimentacao é obrigatório.";
		if (dados.getDataMovimentacao().length()==0)
			stRetorno += "O Campo DataMovimentacao é obrigatório.";

		return stRetorno;

	}

	public String consultarListaMovimentosObjetoJSON(String stId_Obejto) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try {
				ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao()); 
				
				stTemp = obPersistencia.consultarListaMovimentosObjetoJSON(stId_Obejto);
				
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}

	public void exlcuir(String id_excluir, String id_Usuario, String ip_computador) throws Exception {
		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao());
			ProcessoObjetoArquivoMovimentacaoDt dados = obPersistencia.consultarId(id_excluir);
			
			if (dados==null) {
				throw new MessageException("Não foi possível localizar a movimentação do objeoto");
			}

			obLogDt = new LogDt("ProcessoObejtoArquivoMovimentacao",dados.getId(), id_Usuario,ip_computador, String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();        

		}finally{
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	//---------------------------------------------------------
		public void salvar(ProcessoObjetoArquivoMovimentacaoDt dados, FabricaConexao obFabricaConexao  ) throws Exception {

			LogDt obLogDt;
											
			ProcessoObjetoArquivoMovimentacaoPs obPersistencia = new ProcessoObjetoArquivoMovimentacaoPs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoObjetoArquivoMovimentacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoObjetoArquivoMovimentacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			

			
		}

}
