package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import br.gov.go.tj.projudi.ps.TarefaStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class TarefaStatusNe extends TarefaStatusNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -7355807312856670610L;

    //---------------------------------------------------------
	public  String Verificar(TarefaStatusDt dados ) {
		String stRetorno="";

		if (dados.getTarefaStatus().length()==0)
			stRetorno += "O Campo TarefaStatusPs é obrigatório.";
		//System.out.println("..neTarefaStatusVerificar()");
		return stRetorno;

	}
	
	/**
	 * Recupera o identificador do registro que indica o Status de
	 * abertura de tarefa.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String consultarIdStatusAberto(FabricaConexao cn) throws Exception {
		FabricaConexao obFabricaConexao = null;
		//System.out.println("..ne-ConsultaId_TarefaStatus" );
		try{
			if (cn == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = cn;
			TarefaStatusPs obPersistencia = new TarefaStatusPs(obFabricaConexao.getConexao());
			
			return obPersistencia.consultarIdStatusAberto(); 
		} finally {
			if (cn == null)
				obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Recupera o identificador do registro que indica o Status de
	 * abertura de tarefa.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String consultarIdStatusAguardandoVisto(FabricaConexao cn) throws Exception {
		FabricaConexao obFabricaConexao = null;
		//System.out.println("..ne-ConsultaId_TarefaStatus" );
		try{
			if (cn == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = cn;
			TarefaStatusPs obPersistencia = new TarefaStatusPs(obFabricaConexao.getConexao());
			
			return obPersistencia.consultarIdStatusAguardandoVisto(); 
		} finally {
			if (cn == null)
				obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Recupera o identificador do registro que indica o Status de
	 * tarefa EM ANDAMENTO.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String consultarIdStatusEmAndamento(FabricaConexao cn) throws Exception {
		FabricaConexao obFabricaConexao = null;
		//System.out.println("..ne-ConsultaId_TarefaStatus" );
		try{
			if (cn == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = cn;
			TarefaStatusPs obPersistencia = new TarefaStatusPs(obFabricaConexao.getConexao());
			
			return obPersistencia.consultarIdStatusEmAndamento(); 
		} finally {
			if (cn == null)
				obFabricaConexao.fecharConexao();
		}
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaStatusPs obPersistencia = new TarefaStatusPs(obFabricaConexao.getConexao());
			stTemp=obPersistencia.consultarDescricaoJSON( descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
