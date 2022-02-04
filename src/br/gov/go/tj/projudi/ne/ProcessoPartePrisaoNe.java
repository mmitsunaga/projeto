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
			stRetorno += "N�o foi possivel identificar a parte para lan�ar a pris�o.";
		
		if (dados.getDataPrisao().length()==0)
			stRetorno += "O Campo Data Prisao � obrigat�rio.";
		
		if (dados.getId_LocalCumpPena().length()==0)
			stRetorno += "O Campo Local da Pris�o � obrigat�rio.";
		
		if (dados.getId_PrisaoTipo().length()==0)
			stRetorno += "O Campo Tipo de Pris�o � obrigat�rio.";
		
		try {
			if (!dados.isTemId()  && this.consultarUltimaPrisoesParte(dados.getId_ProcessoParte()) != null){
				stRetorno += "N�o � poss�vel lan�ar uma nova pris�o, pois a parte j� est� presa. Verifique a castro de pris�o da parte!";
			}
		} catch (Exception e) {
		}
		
		return stRetorno;

	}
	
	public  String VerificarFinalizacaoAlvaraSoltura(ProcessoPartePrisaoDt dados )  {

		String stRetorno="";
		
		if (dados.getId_ProcessoParte().length()==0)
			stRetorno += "N�o foi possivel identificar a parte para lan�ar a pris�o.";
		
		if (dados.getDataPrisao().length()==0)
			stRetorno += "O Campo Data Prisao � obrigat�rio.";
		
		if (dados.getId_LocalCumpPena().length()==0)
			stRetorno += "O Campo Local da Pris�o � obrigat�rio.";
		
		if (dados.getId_PrisaoTipo().length()==0)
			stRetorno += "O Campo Tipo de Pris�o � obrigat�rio.";
		
		if(dados.getDataEvento().length() == 0)
			stRetorno += "O Campo Data Evento � obrigat�rio.";
		
		if (dados.getId_EventoTipo().length()==0)
			stRetorno += "O Campo Tipo de Evento � obrigat�rio.";
		
		
		return stRetorno;

	}
	
	 /**

	  * M�todo para lista as area processuais

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
					throw new MessageException("N�o foi poss�vel localizar a pris�o da parte");
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

			/* use o id do objeto para saber se os dados ja est�o ou n�o salvos */
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
