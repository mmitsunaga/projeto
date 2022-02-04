package br.gov.go.tj.projudi.ne;

import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ps.PonteiroLogPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class PonteiroLogNe extends PonteiroLogNeGen{

	private static final long serialVersionUID = -6546726246310173585L;

	public  String Verificar(PonteiroLogDt dados ) {

		String stRetorno="";

		if (dados.getPonteiroLogTipo() == null || dados.getPonteiroLogTipo().length()==0)
			stRetorno += "O campo Ponteiro Log Tipo é obrigatório. ";
		
		if (dados.getAreaDistribuicao() == null || dados.getAreaDistribuicao().length()==0)
			stRetorno += "O campo Área de Distribuição é obrigatório. ";
		
		if (dados.getServ() == null || dados.getServ().length()==0)
			stRetorno += "O campo Serventia é obrigatório. ";
		
		if (dados.getNome() == null || dados.getNome().length()==0)
			stRetorno += "O campo Usuário Responsável é obrigatório. ";
		
		if (dados.getData() == null || dados.getData().length()==0)
			stRetorno += "O campo Data de Registro é obrigatório. ";
		
		if (dados.getJustificativa() == null || dados.getJustificativa().length()==0)
			stRetorno += "O campo Justificativa é obrigatório. ";
		
		if (dados.getQtd() == null || dados.getQtd().length()==0)
			stRetorno += "O campo Quantidade é obrigatório. ";

		return stRetorno;

	}
	public  String consultarServentiasAreaDistribuicao(String id_areadistribuicao) throws Exception{
		return consultarServentiasAreaDistribuicao(id_areadistribuicao,"");
	}
	
	public  String consultarServentiasAreaDistribuicao(String id_areadistribuicao, String id_serventia_ignorada) throws Exception{
		
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PonteiroLogPs obPersistencia = new PonteiroLogPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiasAreaDistribuicao(id_areadistribuicao,  id_serventia_ignorada);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
		
		
	}
	
	public String consultarServentiaCargosJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		String stTemp = "";
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		stTemp = ServentiaCargone.consultarServentiaCargosJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		ServentiaCargone = null;
		return stTemp;
	}
	
	
	public void salvarPonteiroApensarDesapensar(ProcessoDt processoDt, int ponteiroLogTipo, UsuarioNe usuarioSessao) throws Exception{
		
		Date dtAgora = new Date();		
		FabricaConexao obFabricaConexao = null;
		
		ServentiaCargoDt servCargo = new ProcessoResponsavelNe().getJuizResponsavelProcesso(processoDt.getId(), processoDt.getId_Serventia());
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			this.salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(),ponteiroLogTipo, processoDt.getId(), processoDt.getId_Serventia(), usuarioSessao.getUsuarioDt().getId(),  usuarioSessao.getUsuarioDt().getId_UsuarioServentia(),dtAgora, servCargo.getId() ), obFabricaConexao );				
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
				
	}
	

	public String consultarServentiaCargoServentias(String id_serventia, String id_serv_cargo, String id_area_distribuicao) throws Exception {
		
		String obRetorno ;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PonteiroLogPs obPersistencia = new PonteiroLogPs(obFabricaConexao.getConexao());
			obRetorno = obPersistencia.consultarServentiaCargoServentias( id_serventia, id_serv_cargo, id_area_distribuicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return obRetorno;
	}
	
	public String consultarServentiasAreaDistribuicao(String id_areadistribuicao, String id_serventia_ignorada,	FabricaConexao obFabricaConexao) throws Exception {
		
		String stTemp = "";
						
		PonteiroLogPs obPersistencia = new PonteiroLogPs(obFabricaConexao.getConexao());
		stTemp = obPersistencia.consultarServentiasAreaDistribuicao(id_areadistribuicao,  id_serventia_ignorada);		
		
		return stTemp;
	}
	
	
	public void salvar(PonteiroLogDt dados ) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);		
		try{								
			PonteiroLogPs obPersistencia = new  PonteiroLogPs(obFabricaConexao.getConexao());			
			obFabricaConexao.iniciarTransacao();									
			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				//verifico que este ponteiro tem alguma compesação
				compensar(dados, obPersistencia, obFabricaConexao);				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PonteiroLog",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
				obDados.copiar(dados);
				obLog.salvar(obLogDt, obFabricaConexao);

				obFabricaConexao.finalizarTransacao();
			}else{
				 throw new Exception("Não foi possivel salvar o ponteiro");
			}
		
		}finally{
			obFabricaConexao.fecharConexao();
		}					
	}
	
	public void salvar(PonteiroLogDt dados, FabricaConexao obFabricaConexao ) throws Exception {
		
		LogDt obLogDt;
		
		PonteiroLogPs obPersistencia = new  PonteiroLogPs(obFabricaConexao.getConexao()); 
		
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().length()==0) {			
			//verifico que este ponteiro tem alguma compesação
			compensar(dados, obPersistencia, obFabricaConexao);			
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("PonteiroLog",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		}else{
			throw new Exception("Não foi possivel salvar o ponteiro");
		}
	}
	
	private void compensar(PonteiroLogDt dados, PonteiroLogPs obPersistencia , FabricaConexao obFabricaConexao) throws Exception{
		
		List lisCompensar =  (new PonteiroLogCompensarNe()).consultarPonteiroCompensar(dados.getId_AreaDistribuicao(), dados.getId_Serv(), dados.getId_ServentiaCargo(), obFabricaConexao);
		//faço todas as compeçações encontradas
		for (int i=0; lisCompensar!=null && i<lisCompensar.size();i++){
			PonteiroLogCompensarDt plcDt = (PonteiroLogCompensarDt) lisCompensar.get(i);
			
			PonteiroLogDt dadosAtual = new PonteiroLogDt( plcDt);
			dadosAtual.setId_PonteiroLogTipo(String.valueOf(PonteiroLogTipoDt.COMPENSACAO));
			dadosAtual.setId_Proc(dados.getId_Proc());		
			dadosAtual.setId_UsuarioServentia(dados.getId_UsuarioServentia());					
			dadosAtual.setData(dados.getData());
			
			switch (Funcoes.StringToInt(dados.getId_PonteiroLogTipo(),-1)) {
				case PonteiroLogTipoDt.DISTRIBUICAO:
				case PonteiroLogTipoDt.GANHO_RESPONSABILIDADE:					
					dadosAtual.setQtd(Funcoes.StringToInt(plcDt.getQtd()) );
					obPersistencia.inserir(dadosAtual);					
					break;
				case PonteiroLogTipoDt.REDISTRIBUICAO:
				case PonteiroLogTipoDt.PERDA_RESPONSABILIDADE:					
					dadosAtual.setQtd(Funcoes.StringToInt(plcDt.getQtd())* -1);
					obPersistencia.inserir(dadosAtual);
					break;					
			}
		}
	}
	
	public void atualizarProbabilidadePonteiros() throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try 
		{
			PonteiroLogPs obPersistencia = new  PonteiroLogPs(obFabricaConexao.getConexao()); 
			obPersistencia.atualizarProbabilidadePonteiros();	
		} finally {
			obFabricaConexao.fecharConexao();
		}	
	}
	
	/**
	 * Método verifica se houve registro de qualquer ponteiro de distribuição para o cargo informado.
	 * @param idServentiaCargo - ID do serventia cargo
	 * @return true se houver registro ou false se não houver registro
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean haDistribuicaoServentiaCargo(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
		try 
		{
			PonteiroLogPs obPersistencia = new  PonteiroLogPs(obFabricaConexao.getConexao()); 
			return obPersistencia.haDistribuicaoServentiaCargo(idServentiaCargo);	
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que retorna o id da serventia para a qual o ponteiro do processo está apontando.
	 * @param String idProc
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String ondeEstaPonteiroProcesso(String idProc) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
		try 
		{
			PonteiroLogPs ponteiroLogPs = new PonteiroLogPs(obFabricaConexao.getConexao());
			String idServ = null;
			if(idProc != null && !idProc.isEmpty()) {
				idServ = ponteiroLogPs.ondeEstaPonteiroProcesso(idProc);
			}
			return idServ;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public boolean temPonteiroCriado(String id_area_dist, String id_proc, String id_Serventia,String id_serv_cargo) throws Exception {
		boolean boRetorno =false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
		try 
		{
			PonteiroLogPs ponteiroLogPs = new PonteiroLogPs(obFabricaConexao.getConexao());		
			
			boRetorno = ponteiroLogPs.temPonteiroCriado( id_area_dist,  id_proc,  id_Serventia, id_serv_cargo);
			
			return boRetorno;	
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoProcessoJSON(String descricao, String PosicaoPaginaAtual) throws Exception {
		
		String stTemp = (new ProcessoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}
}
