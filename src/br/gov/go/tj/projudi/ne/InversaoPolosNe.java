package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.InversaoPolosDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class InversaoPolosNe extends Negocio {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5027224091314223617L;
	
	/**
	 * Realiza a validação de regras da tela.
	 * 
	 * @param dados
	 * @return
	 * @author mmgomes
	 */
	public String Verificar(InversaoPolosDt dados) {
		String stRetorno = "";
		
		if(dados.getMovimentacaoProcessoDt() == null) 
			stRetorno += "Para inverter os Pólos do processo/recurso, é necessário fazer uma certidão justificando o motivo. Favor acessar a capa do processo novamente e escolher a opção Inverter Pólos \n";
				
		if (dados.getListaPromoventesInversaoPolos().size() == 0 && dados.getListaPromovidosInversaoPolos().size() == 0 && dados.getListaRecorrentesInversaoPolos().size() == 0 && dados.getListaRecorridosInversaoPolos().size() == 0)
			stRetorno += "Selecione pelo menos uma parte para realizar a inversão de Pólos. \n";		
		
		if (stRetorno == "")
		{					
			if (dados.isRecurso())
			{
				int quantidadeRecorrentes = dados.getRecursoDt().getListaRecorrentesAtivos().size() - dados.getListaRecorrentesInversaoPolos().size() + dados.getListaRecorridosInversaoPolos().size();
				if (quantidadeRecorrentes == 0) stRetorno += "É necessário permanecer pelo menos um recorrente no recurso.";
				
				int quantidadeRecorridos = dados.getRecursoDt().getListaRecorridosAtivos().size() - dados.getListaRecorridosInversaoPolos().size() + dados.getListaRecorrentesInversaoPolos().size();
				if (quantidadeRecorridos == 0) stRetorno += "É necessário permanecer pelo menos um recorrido no recurso.";
			}			
			int quantidadePromoventes = dados.getProcessoCompletoDt().getListaPromoventesAtivos().size() - dados.getListaPromoventesInversaoPolos().size() + dados.getListaPromovidosInversaoPolos().size();
			if (quantidadePromoventes == 0) stRetorno += "É necessário permanecer pelo menos um promovente no processo.";
			
			int quantidadePromovidos = dados.getProcessoCompletoDt().getListaPromovidosAtivos().size() - dados.getListaPromovidosInversaoPolos().size() + dados.getListaPromoventesInversaoPolos().size();
			if (quantidadePromovidos == 0) stRetorno += "É necessário permanecer pelo menos um promovido no processo.";
		}
		
		return stRetorno;
	}

	/**
	 * Inverter pólos do recurso / processo.
	 * 
	 * @param inversaoPolosdt
	 * @param usuarioDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public void InverterPolos(InversaoPolosDt inversaoPolosdt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = null;						
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			
						
			if (inversaoPolosdt.isRecurso() && (inversaoPolosdt.getListaRecorrentesInversaoPolos().size() > 0 || inversaoPolosdt.getListaRecorridosInversaoPolos().size() > 0))
			{	
				RecursoParteNe recursoParteNe = new RecursoParteNe();
				// Inversão de Pólos dos Recorridos
				this.InverterPolosRecurso(inversaoPolosdt.getListaRecorrentesInversaoPolos(), recursoParteNe, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, inversaoPolosdt.getId_UsuarioLog(), inversaoPolosdt.getIpComputadorLog(), obFabricaConexao);								
				// Inversão de Pólos dos Recorridos
				this.InverterPolosRecurso(inversaoPolosdt.getListaRecorridosInversaoPolos(), recursoParteNe, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, inversaoPolosdt.getId_UsuarioLog(), inversaoPolosdt.getIpComputadorLog(), obFabricaConexao);				
			}	
			
			if (inversaoPolosdt.getListaPromoventesInversaoPolos().size() > 0 || inversaoPolosdt.getListaPromovidosInversaoPolos().size() > 0)				
			{
				ProcessoParteNe processoParteNe = new ProcessoParteNe();
				
				// Inversão de Pólos dos Promoventes
				this.InverterPolosProcesso(inversaoPolosdt.getListaPromoventesInversaoPolos(), processoParteNe, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, inversaoPolosdt.getId_UsuarioLog(), inversaoPolosdt.getIpComputadorLog(), obFabricaConexao);
				
				// Inversão de Pólos dos Promovidos
				this.InverterPolosProcesso(inversaoPolosdt.getListaPromovidosInversaoPolos(), processoParteNe, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, inversaoPolosdt.getId_UsuarioLog(), inversaoPolosdt.getIpComputadorLog(), obFabricaConexao);
				
			}						
			
			if (inversaoPolosdt.getMovimentacaoProcessoDt().getRedirecionaOutraServentia() != null && !inversaoPolosdt.getMovimentacaoProcessoDt().getRedirecionaOutraServentia().equals("")){
				MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
				movimentacaoNe.salvarMovimentacaoGenerica(inversaoPolosdt.getMovimentacaoProcessoDt(), usuarioDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
			
		
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * Inverte os pólos do recurso.
	 * 
	 * 
	 * @param listaPartes
	 * @param recursoParteNe
	 * @param processoParteTipoCodigo
	 * @param id_UsuarioLog
	 * @param ipComputadorLog
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	private void InverterPolosRecurso(List listaPartes, RecursoParteNe recursoParteNe, int processoParteTipoCodigo, String id_UsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao) throws Exception
	{
		RecursoParteDt recursoParteDt = null;
		if (listaPartes != null){
			for(int i = 0; i < listaPartes.size(); i++)
			{
				recursoParteDt = recursoParteNe.consultarId(((RecursoParteDt) listaPartes.get(i)).getId(), obFabricaConexao);
				
				recursoParteDt.setId_UsuarioLog(id_UsuarioLog);
				recursoParteDt.setIpComputadorLog(ipComputadorLog);
				recursoParteDt.setProcessoParteTipoCodigo(String.valueOf(processoParteTipoCodigo));
				recursoParteDt.setId_ProcessoParteTipo("null");
				recursoParteDt.setProcessoParteTipo("null");
				
				recursoParteNe.alterar(recursoParteDt, obFabricaConexao);						
			}
		}	
	}
	
	/**
	 * Inverte os pólos do processo.
	 * 
	 * @param listaPartes
	 * @param processoParteNe
	 * @param processoParteTipoCodigo
	 * @param id_UsuarioLog
	 * @param ipComputadorLog
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	private void InverterPolosProcesso(List listaPartes, ProcessoParteNe processoParteNe, int processoParteTipoCodigo, String id_UsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao) throws Exception
	{
		ProcessoParteDt processoParteDt = null;
		if (listaPartes != null){
			for(int i = 0; i < listaPartes.size(); i++)
			{
				processoParteDt = processoParteNe.consultarId(((ProcessoParteDt) listaPartes.get(i)).getId(), obFabricaConexao);
				
				processoParteDt.setId_UsuarioLog(id_UsuarioLog);
				processoParteDt.setIpComputadorLog(ipComputadorLog);
				processoParteDt.setProcessoParteTipoCodigo(String.valueOf(processoParteTipoCodigo));
				processoParteDt.setId_ProcessoParteTipo("null");
				processoParteDt.setProcessoParteTipo("null");
				
				processoParteNe.alterar(processoParteDt, obFabricaConexao);						
			}
		}
	}
}