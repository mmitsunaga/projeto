package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDisponivelDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GuiaItemPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaItemNe extends GuiaItemNeGen {

	private static final long serialVersionUID = 7156594508174858934L;
	
	@Override
    public String Verificar(GuiaItemDt dados) {
        
        return null;
    }
	
	/**
	 * Método que consulta os itens de uma guia emitida.
	 * @param FabricaConexao obFabricaConexao
	 * @param GuiaEmissaoDt
	 * @return List
	 * @throws Exception
	 */
	public List consultaItensGuiaGuiaComplementar(FabricaConexao obFabricaConexao, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		List listaItensGuia = null;
		FabricaConexao conexao = null;
		
		try{
			GuiaItemPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaItemPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			}
			
			listaItensGuia = obPersistencia.consultaItensGuia(guiaEmissaoDt);
			
			if( listaItensGuia != null && listaItensGuia.size() > 0 ) {
				LocomocaoNe locomocaoNe = new LocomocaoNe();
				
				int quantidade = listaItensGuia.size();
				for(int i = 0; i < quantidade; i++) {
					GuiaItemDt guiaItemDt = (GuiaItemDt) listaItensGuia.get(i);
					
					guiaItemDt.setGuiaEmissaoDt(guiaEmissaoDt);
					
//					if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) || guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL)) ) {
//						LocomocaoDt locomocaoDt = locomocaoNe.consultarIdGuiaItem(guiaItemDt.getId());
//						
//						double valor;
//						
//						//Sim, Foi utilizada, então zera e não é devolvido.
//						if( locomocaoDt != null && locomocaoDt.getUtilizada().equals(LocomocaoDt.SIM_UTILIZADA.toString()) ) {
//							valor = 0.0D;
//							guiaItemDt.setValorCalculado(String.valueOf(valor));
//						}
//						//Não foi utilizada, devolve!
//						else {
//							valor = Funcoes.StringToDouble(guiaItemDt.getValorCalculado()) * (-1);
//							guiaItemDt.setValorCalculado(String.valueOf(valor));
//							if( locomocaoDt != null ) {
//								locomocaoDt.setUtilizada("1");//desabilita locomocoes para evitar de ser utilizada sem poder
//							}
//						}
//						
//						guiaItemDt.setLocomocaoDt(locomocaoDt);
//						listaItensGuia.remove(i);
//						listaItensGuia.add(i, guiaItemDt);
//					}
				}
			}
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return listaItensGuia;
	}
	
	/**
	 * Método que consulta os itens de uma guia emitida com base na arrecadacao Custa
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idArrecadacaoCusta
	 * @return List
	 * @throws Exception
	 */
	public List consultaItensGuia(GuiaEmissaoDt guiaEmissaoDt, String idArrecadacaoCusta) throws Exception {
		List listaItensGuia = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			listaItensGuia = obPersistencia.consultaItensGuia(guiaEmissaoDt);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaItensGuia;
	}
	
	/**
	 * Método para consultar itens da guia emitida.
	 * @param String idGuiaEmissao
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuia(String idGuiaEmissao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			return consultarItensGuia(idGuiaEmissao, obFabricaConexao);
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para consultar itens da guia emitida.
	 * @param String idGuiaEmissao
	 * @param FabricaConexao obFabricaConexao
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuia(String idGuiaEmissao, FabricaConexao obFabricaConexao) throws Exception {
		List<GuiaItemDt> listaItensGuia = null;
		
		GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
		
		GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
		guiaEmissaoDt.setId(idGuiaEmissao);
		listaItensGuia = obPersistencia.consultaItensGuia(guiaEmissaoDt);
		
		//Preenche com a locomoção
		if( listaItensGuia != null && !listaItensGuia.isEmpty() ) {
			LocomocaoNe locomocaoNe = new LocomocaoNe();
			BairroNe bairroNe = new BairroNe();
			ZonaNe zonaNe = new ZonaNe();
			for( GuiaItemDt guiaItemDt: listaItensGuia ) {
				guiaItemDt.setLocomocaoDt( locomocaoNe.consultarIdGuiaItem(guiaItemDt.getId(), obFabricaConexao) );
				
				if( guiaItemDt.getLocomocaoDt() != null ) {
					if( guiaItemDt.getLocomocaoDt().getBairroDt() != null ) {
						guiaItemDt.getLocomocaoDt().setBairroDt( bairroNe.consultarId(guiaItemDt.getLocomocaoDt().getBairroDt().getId(), obFabricaConexao) );
					}
					
					if( guiaItemDt.getLocomocaoDt().getZonaDt() != null ) {
						guiaItemDt.getLocomocaoDt().setZonaDt( zonaNe.consultarId(guiaItemDt.getLocomocaoDt().getZonaDt().getId(), obFabricaConexao) );
					}
				}
				
				if( guiaItemDt != null && guiaItemDt.getId_ArrecadacaoCustaGenerica() != null ) {
					ArrecadacaoCustaDt arrecadacaoCustaDt = new ArrecadacaoCustaNe().consultarId(guiaItemDt.getId_ArrecadacaoCustaGenerica());
					if( arrecadacaoCustaDt != null ) {
						guiaItemDt.setCodigoArrecadacao(arrecadacaoCustaDt.getCodigoArrecadacao());
						guiaItemDt.getCustaDt().setCodigoArrecadacao(arrecadacaoCustaDt.getCodigoArrecadacao());
					}
				}
			}
		}
		
		return listaItensGuia;
	}
	
	/**
	 * Consulta os GuiaItem de uma Guia Emitida em uma Dia específico.
	 * @param String dataEmissaoGuia
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List consultaItensGuia(String dataEmissaoGuia) throws Exception {
		List listaItensGuia = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			listaItensGuia = obPersistencia.consultaItensGuia(dataEmissaoGuia);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaItensGuia;
	}
	
	/**
	 * Método de Consulta do GuiaItem por Id.
	 * @param String idGuiaItem
	 * @return GuiaItemDt
	 * @throws Exception
	 */
	public GuiaItemDt consultaId(String idGuiaItem) throws Exception {
		GuiaItemDt guiaItemDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			guiaItemDt = obPersistencia.consultarId(idGuiaItem);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return guiaItemDt;
	}
	
	/**
	 * Método para salvar lista de itens de guia calculados.
	 * @param FabricaConexao obFabricaConexao
	 * @param List listaGuiaItemDt
	 * @param String idGuiaEmissao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void salvar(FabricaConexao obFabricaConexao, List<GuiaItemDt> listaGuiaItemDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {

		LocomocaoNe locomocaoNe = new LocomocaoNe();
			
		GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			
		obPersistencia.excluirItensDaGuiaEmissao(guiaEmissaoDt.getId());
		
		this.salvar(obFabricaConexao, obPersistencia, listaGuiaItemDt, guiaEmissaoDt, false);
		
		this.salvar(obFabricaConexao, obPersistencia, listaGuiaItemDt, guiaEmissaoDt, true);
		
		// Salva as locomoções...
		for(GuiaItemDt guiaItemDt : listaGuiaItemDt) {
			if (guiaItemDt.getLocomocaoDt() != null) {
				guiaItemDt.getLocomocaoDt().setGuiaItemDt(guiaItemDt);	//aqui leandro
				guiaItemDt.getLocomocaoDt().setId_UsuarioLog(guiaEmissaoDt.getId_Usuario());
				guiaItemDt.getLocomocaoDt().setIpComputadorLog(guiaEmissaoDt.getIpComputadorLog());
				
				guiaItemDt.getLocomocaoDt().setQuantidadeAcrescimo(Funcoes.StringToInt(guiaEmissaoDt.getQuantidadeAcrescimo()));
				guiaItemDt.getLocomocaoDt().setCitacaoHoraCerta(guiaEmissaoDt.isCitacaoHoraCerta());
				guiaItemDt.getLocomocaoDt().setForaHorarioNormal(guiaEmissaoDt.isForaHorarioNormal());
				guiaItemDt.getLocomocaoDt().setFinalidadeCodigo(Funcoes.StringToInt(guiaEmissaoDt.getFinalidade()));
				guiaItemDt.getLocomocaoDt().setPenhora(guiaEmissaoDt.isPenhora());
				guiaItemDt.getLocomocaoDt().setIntimacao(guiaEmissaoDt.isIntimacao());
				
				locomocaoNe.salvar(guiaItemDt.getLocomocaoDt(), obFabricaConexao);
			}

			// Atualiza as locomoções não utilizadas com a nova guia gerada como guia complementar...
			if (guiaEmissaoDt.isLocomocaoComplementar() && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt() != null && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt().size() > 0) {
				for(LocomocaoDt locomocaoDt : guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt()) {
					locomocaoDt = locomocaoNe.consultarIdCompleto(locomocaoDt.getId());
					if (locomocaoDt != null) {
						locomocaoDt.setId_GuiaEmissaoComplementar(guiaEmissaoDt.getId());	
						locomocaoDt.setId_UsuarioLog(guiaEmissaoDt.getId_Usuario());
						locomocaoDt.setIpComputadorLog(guiaEmissaoDt.getIpComputadorLog());
						
						locomocaoNe.salvar(locomocaoDt, obFabricaConexao);	
					}					
				}
			}
		}
		
		//Salva itens de despesa postal na tabela guia-item-disponivel
		salvarGuiaItemDisponivel(obFabricaConexao, listaGuiaItemDt, guiaEmissaoDt);
	}

	private void salvarGuiaItemDisponivel(FabricaConexao obFabricaConexao, List<GuiaItemDt> listaGuiaItemDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		//Inicialmente, não deixar cadastrar itens de guias do SPG
		if( guiaEmissaoDt != null && guiaEmissaoDt.getNumeroGuiaSerie() != null && ( guiaEmissaoDt.isSerie02() || guiaEmissaoDt.isSerie06() || guiaEmissaoDt.isSerie09() ) ) {
			return;
		}
		
		/*
		GuiaItemDisponivelNe guiaItemDisponivelNe = new GuiaItemDisponivelNe();
		
		if( !guiaEmissaoDt.isGuiaParcelada() ) {
			for(GuiaItemDt guiaItemDt : listaGuiaItemDt) {
				if( guiaItemDt.isGuiaItemDespesaPostal() ) {
					
					if( guiaItemDt.getQuantidade() != null && !guiaItemDt.getQuantidade().isEmpty() && Funcoes.StringToInt(guiaItemDt.getQuantidade()) > 0 ) {
						
						//Se o item tiver a quantidade 3, cadastra 3 vezes
						for(int quantidade = 0; quantidade < Funcoes.StringToInt(guiaItemDt.getQuantidade()); quantidade++ ) {
							GuiaItemDisponivelDt guiaItemDisponivelDt = new GuiaItemDisponivelDt();
							
							guiaItemDisponivelDt.setGuiaItemDt(guiaItemDt);
							
							guiaItemDisponivelDt.setId_UsuarioLog(guiaEmissaoDt.getId_Usuario());
							guiaItemDisponivelDt.setIpComputadorLog(guiaEmissaoDt.getIpComputadorLog());
							
							guiaItemDisponivelNe.salvar(guiaItemDisponivelDt, obFabricaConexao);
						}
					}
				}
			}
		}
		*/
		
		GuiaItemDisponivelNe guiaItemDisponivelNe = new GuiaItemDisponivelNe();
		
		//Se a guia for parcelada, então deleta os itens da guia refencia que nao estiver sendo utilizada
		//em seguida, cadastra os itens da guia parcelada
		
		//ATENCAO: O sql nao pode ser executado ate a atualização pq pode vincular com algum item errado
		//ou duplicar os itens:
		/*
--select guia.* from projudi.guia_emis guia
select 'insert into projudi.guia_item_disponivel (id_guia_item) values ('||item.id_guia_item||');' from projudi.guia_emis guia
inner join projudi.guia_item item on item.id_guia_emis = guia.id_guia_emis
--inner join projudi.guia_item_disponivel dis on dis.id_guia_item <> item.id_guia_item
where item.id_custa in (9029, 9009, 189)
and guia.data_emis >= '01/02/2020'
--and guia.data_emis >= '29/01/2021'
and guia.tipo_guia_desconto_parcelament = 1
and guia.parcela_atual = 1
and item.id_guia_item not in ( select id_guia_item from projudi.guia_item_disponivel where id_guia_item in (select id_guia_item from projudi.guia_item where id_guia_emis in (select id_guia_emis from projudi.guia_emis where id_proc = guia.id_proc)) )
		 */
		
		//vincularGuiaItemDepesaPostalPendencia
		//Retirar o status de parcelamento pago depois dessa alteração.
		
		//Adicionar passo para Deletar itens das guias de referencia 
		
		if( !guiaEmissaoDt.isGuiaParcelada()  
			|| 
			( guiaEmissaoDt.isGuiaParcelada() && guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento() != null && guiaEmissaoDt.getParcelaAtual() != null && guiaEmissaoDt.getParcelaAtual().equals("1") ) ) {
			
			for(GuiaItemDt guiaItemDt : listaGuiaItemDt) {
				if( guiaItemDt.isGuiaItemDespesaPostal() ) {
					
					if( guiaItemDt.getQuantidade() != null && !guiaItemDt.getQuantidade().isEmpty() && Funcoes.StringToInt(guiaItemDt.getQuantidade()) > 0 ) {
						
						//Se o item tiver a quantidade 3, cadastra 3 vezes
						for(int quantidade = 0; quantidade < Funcoes.StringToInt(guiaItemDt.getQuantidade()); quantidade++ ) {
							GuiaItemDisponivelDt guiaItemDisponivelDt = new GuiaItemDisponivelDt();
							
							guiaItemDisponivelDt.setGuiaItemDt(guiaItemDt);
							
							guiaItemDisponivelDt.setId_UsuarioLog(guiaEmissaoDt.getId_Usuario());
							guiaItemDisponivelDt.setIpComputadorLog(guiaEmissaoDt.getIpComputadorLog());
							
							guiaItemDisponivelNe.salvar(guiaItemDisponivelDt, obFabricaConexao);
						}
					}
				}
			}
			
		}
	}
	
	/**
	 * Método para salvar lista de itens de guia calculados.
	 * @param FabricaConexao obFabricaConexao
	 * @param List listaGuiaItemDt
	 * @param String idGuiaEmissao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void salvarNOVO(FabricaConexao obFabricaConexao, List<GuiaItemDt> listaGuiaItemDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {

		LocomocaoNe locomocaoNe = new LocomocaoNe();
			
		GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			
		obPersistencia.excluirItensDaGuiaEmissao(guiaEmissaoDt.getId());
		
		this.salvar(obFabricaConexao, obPersistencia, listaGuiaItemDt, guiaEmissaoDt, false);
		
		this.salvar(obFabricaConexao, obPersistencia, listaGuiaItemDt, guiaEmissaoDt, true);
		
		// Salva as locomoções...
		for(GuiaItemDt guiaItemDt : listaGuiaItemDt) {
			if (guiaItemDt.getLocomocaoDt() != null) {
				guiaItemDt.getLocomocaoDt().setGuiaItemDt(guiaItemDt);	//aqui leandro
				guiaItemDt.getLocomocaoDt().setId_UsuarioLog(guiaEmissaoDt.getId_Usuario());
				guiaItemDt.getLocomocaoDt().setIpComputadorLog(guiaEmissaoDt.getIpComputadorLog());
				//AGORA ESSAS VARIAVES SERÃO PREENCHIDOS POR LOCOMOÇÃO NÃO SERÁ MAIS GERAL
//				guiaItemDt.getLocomocaoDt().setQuantidadeAcrescimo(Funcoes.StringToInt(guiaEmissaoDt.getQuantidadeAcrescimo()));
//				guiaItemDt.getLocomocaoDt().setCitacaoHoraCerta(guiaEmissaoDt.isCitacaoHoraCerta());
//				guiaItemDt.getLocomocaoDt().setForaHorarioNormal(guiaEmissaoDt.isForaHorarioNormal());
//				guiaItemDt.getLocomocaoDt().setFinalidadeCodigo(Funcoes.StringToInt(guiaEmissaoDt.getFinalidade()));
//				guiaItemDt.getLocomocaoDt().setPenhora(guiaEmissaoDt.isPenhora());
//				guiaItemDt.getLocomocaoDt().setIntimacao(guiaEmissaoDt.isIntimacao());
				
				locomocaoNe.salvar(guiaItemDt.getLocomocaoDt(), obFabricaConexao);
			}

			// Atualiza as locomoções não utilizadas com a nova guia gerada como guia complementar...
			if (guiaEmissaoDt.isLocomocaoComplementar() && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt() != null && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt().size() > 0) {
				for(LocomocaoDt locomocaoDt : guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt()) {
					locomocaoDt = locomocaoNe.consultarIdCompleto(locomocaoDt.getId());
					if (locomocaoDt != null) {
						locomocaoDt.setId_GuiaEmissaoComplementar(guiaEmissaoDt.getId());	
						locomocaoDt.setId_UsuarioLog(guiaEmissaoDt.getId_Usuario());
						locomocaoDt.setIpComputadorLog(guiaEmissaoDt.getIpComputadorLog());
						
						locomocaoNe.salvar(locomocaoDt, obFabricaConexao);	
					}					
				}
			}
		}
		
		//Salva itens de despesa postal na tabela guia-item-disponivel
		salvarGuiaItemDisponivel(obFabricaConexao, listaGuiaItemDt, guiaEmissaoDt);
	}
	
	/**
	 * Método para salvar lista de itens de guia calculados.
	 * @param FabricaConexao obFabricaConexao
	 * @param List listaGuiaItemDt
	 * @param String idGuiaEmissao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	private void salvar(FabricaConexao obFabricaConexao, GuiaItemPs obPersistencia, List<GuiaItemDt> listaGuiaItemDt, GuiaEmissaoDt guiaEmissaoDt, boolean somenteComItemVinculado) throws Exception {
		if( listaGuiaItemDt == null ) {
			throw new MensagemException("Lista de itens da guia zerada. Por favor, acesse novamente a funcionalidade.");
		}
		
		for(GuiaItemDt guiaItemDt : listaGuiaItemDt) {			
			if ((somenteComItemVinculado && guiaItemDt.getGuiaItemVinculadoDt() != null) || (!somenteComItemVinculado && guiaItemDt.getGuiaItemVinculadoDt() == null)) {
				
				obPersistencia.salvar(guiaItemDt, guiaEmissaoDt.getId(), guiaEmissaoDt);
				
				LogDt obLogDt = new LogDt("GuiaItem",guiaItemDt.getId(), guiaEmissaoDt.getId_Usuario(), guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",guiaItemDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}	
		}
	}
	
	/**
	 * Método para consultar o total das guias.
	 * 
	 * @param List listaGuiaEmissaoDt
	 * @return String
	 * @throws Exception
	 */
	public String consultarTotalGuias(List listaGuiaEmissaoDt) throws Exception {
		String retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			
			List listaIdGuias = new ArrayList();
			
			for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
				
				listaIdGuias.add( guiaEmissaoDt.getId() );
			}
			
			retorno = obPersistencia.consultarTotalGuias(listaIdGuias);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar itens de guias parceladas que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasParcelasAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
				
				listaGuiaItemDt = obPersistencia.consultarItensGuiasParcelasAguardandoPagamento(idProcesso);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para consultar itens de guias de serviços que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasServicosAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
				
				listaGuiaItemDt = obPersistencia.consultarItensGuiasServicosAguardandoPagamento(idProcesso);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para consultar itens de guias complementares que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasComplementaresAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
				
				listaGuiaItemDt = obPersistencia.consultarItensGuiasComplementaresAguardandoPagamento(idProcesso);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para consultar itens de guias complementares que estão aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarItensGuiasGRSGenericaAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
				
				listaGuiaItemDt = obPersistencia.consultarItensGuiasGRSGenericaAguardandoPagamento(idProcesso);
				if( listaGuiaItemDt != null ) {
					ArrecadacaoCustaNe xxx = new ArrecadacaoCustaNe();
					for(GuiaItemDt guiaItemDt: listaGuiaItemDt) {
						if( guiaItemDt != null && guiaItemDt.getCustaDt() != null && ( guiaItemDt.getCustaDt().getCodigoArrecadacao().isEmpty() || guiaItemDt.getCustaDt().getCodigoArrecadacao().equals("0") ) ) {
							if( !guiaItemDt.getId_ArrecadacaoCustaGenerica().isEmpty() ) {
								ArrecadacaoCustaDt arrecadacaoCustaDt = xxx.consultarId(guiaItemDt.getId_ArrecadacaoCustaGenerica());
								guiaItemDt.getCustaDt().setCodigoArrecadacao(arrecadacaoCustaDt.getCodigoArrecadacao());
							}
						}
					}
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método que consulta se guia tem item de despesa postal.
	 * @param String idGuiaEmissao
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean guiaPossuiItemDespesaPostal(String idGuiaEmissao, FabricaConexao obFabricaConexao) throws Exception {
		FabricaConexao conexaoLocal = (obFabricaConexao == null ? new FabricaConexao(FabricaConexao.CONSULTA) : obFabricaConexao);
		try
		{
			GuiaItemPs obPersistencia = new GuiaItemPs(obFabricaConexao.getConexao());
			return obPersistencia.guiaPossuiItemDespesaPostal(idGuiaEmissao);
		} finally {
			if (obFabricaConexao == null) conexaoLocal.fecharConexao();
		}		
	}
}