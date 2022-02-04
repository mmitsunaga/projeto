package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCriminalDt;
import br.gov.go.tj.projudi.ps.ProcessoCriminalPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoCriminalNe extends ProcessoCriminalNeGen{

	private static final long serialVersionUID = 3963013568794548005L;

	//---------------------------------------------------------
	public  String Verificar(ProcessoCriminalDt dados ) {

		String stRetorno="";

		if (dados.getProcessoNumero().length()==0)
			stRetorno += "O Campo ProcessoNumero é obrigatório.";
		//System.out.println("..neProcessoCriminalVerificar()");
		return stRetorno;

	}
	
	public ProcessoCriminalDt consultarIdProcesso(String idProcesso, FabricaConexao obFabricaConexao ) throws Exception {

		ProcessoCriminalDt dtRetorno=null;

		ProcessoCriminalPs obPersistencia = new ProcessoCriminalPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarIdProcesso(idProcesso ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
	
	public void salvar(ProcessoCriminalDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;

		ProcessoCriminalPs obPersistencia = new ProcessoCriminalPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().length()==0) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoCriminal",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("ProcessoCriminal",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
}
