package br.gov.go.tj.projudi.ne;


import com.softwareag.common.resourceutilities.message.MessageException;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.ps.ProcessoPartePrisaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoPartePrisaoNe extends ProcessoPartePrisaoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -4905259340470893956L;

	//---------------------------------------------------------
	public  String Verificar(ProcessoPartePrisaoDt dados )  {

		String stRetorno="";
		
		if (dados.getId_ProcessoParte().length()==0)
			stRetorno += "Não foi possivel identificar a parte para lançar a prisão.";
		
		if (dados.getDataPrisao().length()==0)
			stRetorno += "O Campo Data Prisao é obrigatório.";
		
		if (dados.getId_LocalCumpPena().length()==0)
			stRetorno += "O Campo Local da Prisão é obrigatório.";
		
		if (dados.getId_PrisaoTipo().length()==0)
			stRetorno += "O Campo Tipo de Prisão é obrigatório.";
		
		try {
			if (!dados.isTemId()  && this.consultarUltimaPrisoesParte(dados.getId_ProcessoParte()) != null){
				stRetorno += "Não é possível lançar uma nova prisão, pois a parte já está presa. Verifique a castro de prisão da parte!";
			}
		} catch (Exception e) {
		}
		
		return stRetorno;

	}
	
	public  String VerificarFinalizacaoAlvaraSoltura(ProcessoPartePrisaoDt dados )  {

		String stRetorno="";
		
		if (dados.getId_ProcessoParte().length()==0)
			stRetorno += "Não foi possivel identificar a parte para lançar a prisão.";
		
		if (dados.getDataPrisao().length()==0)
			stRetorno += "O Campo Data Prisao é obrigatório.";
		
		if (dados.getId_LocalCumpPena().length()==0)
			stRetorno += "O Campo Local da Prisão é obrigatório.";
		
		if (dados.getId_PrisaoTipo().length()==0)
			stRetorno += "O Campo Tipo de Prisão é obrigatório.";
		
		if(dados.getDataEvento().length() == 0)
			stRetorno += "O Campo Data Evento é obrigatório.";
		
		if (dados.getId_EventoTipo().length()==0)
			stRetorno += "O Campo Tipo de Evento é obrigatório.";
		
		
		return stRetorno;

	}
	
	 /**

	  * Método para lista as area processuais

	 * @author jrcorrea */

		public String consultarPrisoesParteJSON(String id_proc_parte ) throws Exception {
			FabricaConexao obFabricaConexao = null;
			String stTemp = "";
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao()); 

					stTemp = obPersistencia.consultarPrisoesParteJSON(id_proc_parte);
				} finally {
					obFabricaConexao.fecharConexao();
				}
			return stTemp;
		}
		
		public void excluir(String id_processo_parte_prisao, String id_usuario, String ip_computador) throws Exception {

			LogDt obLogDt;

			FabricaConexao obFabricaConexao = null;
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
				ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao());
				ProcessoPartePrisaoDt dados = obPersistencia.consultarId(id_processo_parte_prisao);
				
				if (dados==null) {
					throw new MessageException("Não foi possível localizar a prisão da parte");
				}

				obLogDt = new LogDt("ProcessoPartePrisao",dados.getId(), id_usuario,ip_computador, String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
				obPersistencia.excluir(dados.getId());

				dados.limpar();
				obLog.salvar(obLogDt, obFabricaConexao);
				obFabricaConexao.finalizarTransacao();

			}finally{
				obFabricaConexao.fecharConexao();
			}
		}
		
		public ProcessoPartePrisaoDt consultarUltimaPrisoesParte(String id_proc_parte ) throws Exception {
			FabricaConexao obFabricaConexao = null;
			ProcessoPartePrisaoDt stTemp = null;
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(obFabricaConexao.getConexao()); 

					stTemp = obPersistencia.consultarUltimaPrisoesParte(id_proc_parte);
				} finally {
					obFabricaConexao.fecharConexao();
				}
			return stTemp;
		}


		public void salvar(ProcessoPartePrisaoDt dados, FabricaConexao fabrica ) throws Exception {
			LogDt obLogDt;
			ProcessoPartePrisaoPs obPersistencia = new ProcessoPartePrisaoPs(fabrica.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoPartePrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoPartePrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, fabrica);
		}
}
