package br.gov.go.tj.projudi.ne;

import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.DistribuirMandadoPs;
import br.gov.go.tj.projudi.util.DistribuicaoMandado;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;


public class DistribuirMandadoNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5634196403861430289L;
	
	public static final int SUCESSO 				= -1;
	public static final int SEM_SUCESSO 			= -2;
	public static final int JA_EXISTE 				= -3;
	public static final int NAO_ENCONTRADO_ESCALA 	= -4;
	public static final int ERRO 					= -5;
	
	/** ***************** */
	public static final int NAO_TEM_GUIA 						= -6; //Não possui Guia OU possui Guia não paga  
	public static final int GUIAS_SEM_LOCOMOCAO_LIVRE 			= -7; //Guias não possui itens de locomoção NÃO UTILIZADA
	public static final int ENDERECO_LOCOMOCAO_DIFERENTE 		= -8; //As locomoções são diferentes com o endereço do mandado judicial
	/** ***************** */
	
	protected DistribuirMandadoPs obPersistencia;
	
	public MandadoJudicialDt obDados;
	
    private EscalaNe escalaNe;
    
    private PrazoSuspensoNe prazoSuspensoNe;
    
    private ServentiaNe serventiaNe;
    
    private BairroNe bairroNe;
    
    private ServentiaCargoEscalaNe ServentiaCargoEscalaNe;
    
    private GuiaEmissaoNe guiaEmissaoNe;
    
    private LocomocaoNe locomocaoNe;
    
    private MandadoJudicialStatusNe mandadoJudicialStatusNe;
    
    private ProcessoNe processoNe;
    
    private MandadoTipoNe mandadoTipoNe;
    
    private MandadoJudicialNe mandadoJudicialNe;
	
    /**
     * Construtor
     */
    public DistribuirMandadoNe() {
    	this.obPersistencia 			= new DistribuirMandadoPs();    	
    	this.obDados 					= new MandadoJudicialDt();
    	this.escalaNe 					= new EscalaNe();
        this.prazoSuspensoNe 			= new PrazoSuspensoNe();
        this.serventiaNe 				= new ServentiaNe();
        this.bairroNe 					= new BairroNe();
        this.ServentiaCargoEscalaNe 	= new ServentiaCargoEscalaNe();
        this.guiaEmissaoNe 				= new GuiaEmissaoNe();
        this.locomocaoNe 				= new LocomocaoNe();
        this.mandadoJudicialStatusNe 	= new MandadoJudicialStatusNe();
        this.processoNe 				= new ProcessoNe();
        this.mandadoTipoNe 				= new MandadoTipoNe();
        this.mandadoJudicialNe 			= new MandadoJudicialNe();
    }
    
    /**
     * Método para consultar descrição do módulo de UsusarioServentiaEscala.
     * @param String descricao
     * @param String posicao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoServentiaCargoEscalaLocalizar(String descricao, String posicao) throws Exception {
    	List tempList = null;
    	
    	tempList = this.ServentiaCargoEscalaNe.consultarDescricao(descricao, posicao);
    	
    	return tempList;
    }
    
    /**
     * Método para consultar quantidade de páginas de UsuárioServentiaEscala.
     * @return long
     */
    public long getQuantidadePaginasServentiaCargoEscala() {
    	return ServentiaCargoEscalaNe.getQuantidadePaginas();
    }
    
    /**
     * Método para consultar Processo Parte por Id.
     * @param String id_ProcessoParte
     * @return ProcessoParteDt
     * @throws Exception
     */
    public ProcessoParteDt consultarProcessoParteId(String id_ProcessoParte) throws Exception {
    	ProcessoParteDt processoParteDt = null;
    	
    	processoParteDt = this.processoNe.consultarProcessoParteId(id_ProcessoParte);
    	
    	return processoParteDt;
    }
    
    /**
     * Método para consultar Processo por Id.
     * @param String id_processo
     * @return ProcessoDt
     * @throws Exception
     */
    public ProcessoDt consultarProcessoId(String id_processo ) throws Exception {
    	ProcessoDt processoDt = null;
    	
    	processoDt = this.processoNe.consultarId(id_processo);
    	
    	return processoDt;
    }
    
    /**
     * Método para consultar a lista de Status de Mandado.
     * @param List listaMandadoStatusExcluir
     * @return List
     * @throws Exception
     */
    public List consultarListaMandadoStatus(List listaMandadoStatusExcluir) throws Exception {
    	List tempList = null;
    	
    	tempList = this.mandadoJudicialStatusNe.consultarListaStatus(listaMandadoStatusExcluir);
    	
    	return tempList;
    }
    
    /**
     * Método para consultar lista de Tipos de Mandados.
     * @return List
     * @throws Exception
     */
    public List consultarListaTiposMandados() throws Exception {
    	List tempList = null;
    	
    	tempList = this.mandadoTipoNe.consultarListaTiposMandados();
    	
    	return tempList;
    }
    
	/**
	 * Recarregar lista de distribuição de mandado judicial.
	 * @throws Exception
	 */
	public void recarregarListaDistribuicaoMandado() throws Exception{
		
		DistribuicaoMandado.getInstance().lerDados();
		
	}
	
	/**
	 * Consultar Mandado Judicial por Id da Pendência.
	 * @param String idPendencia
	 * @return MandadoJudicialDt
	 * @throws Exception
	 */
	public MandadoJudicialDt consultarPorIdPendencia(String idPendencia) throws Exception {
		MandadoJudicialDt mandadoJudicialDt = null;

		mandadoJudicialDt = this.mandadoJudicialNe.consultarPorIdPendencia(idPendencia);
		
		return mandadoJudicialDt;
	}
	
	/**
	 * Método que consulta a pendencia e valida se é do tipo Mandado de acordo com o idPendencia passado como parâmetro.
	 * @param String idPendencia
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt consultarPendenciaTipoMandado(Integer idPendencia) throws Exception {
		PendenciaDt pendenciaDt = null;

		pendenciaDt = this.mandadoJudicialNe.consultarPendenciaTipoMandado(idPendencia);
				
		return pendenciaDt;
	}
	
	/**
	 * Método para alterar o status do mandado judicial
	 * @param MandadoJudicialDt mandadoJudicialDt
	 * @param Integer novoStatus
	 * @return boolean
	 * @throws Exception
	 */
	public boolean alterarStatusMandadoJudicial(MandadoJudicialDt mandadoJudicialDt, Integer novoStatus) throws Exception {
		boolean retorno = false;
		
		retorno = this.mandadoJudicialNe.alterarStatusMandadoJudicial(mandadoJudicialDt, novoStatus);
		
		return retorno;
	}
	
	/** ********************************
	 * Verifica se tem locomoção disponível.
	 * Arquivo: "Fluxograma - Distribuir Mandado Judicial.odg"
	 * Caminho: D:\Projetos\Documentacao\Projudi-Novo-TJ-GO\Documentação\Mandados\2010
	 * *********************************
	 * 
     * Método para verificação da guia paga e se tem locomoção disponível para o endereço necessário do mandado judicial.
     * @param ProcessoDt processoDt
     * @param EscalaDt escalaDt
     * @return int
     * @throws Exception
     */
    public int distribuirMandadoLocomocaoPaga(ProcessoDt processoDt, EscalaDt escalaDt){
    	int retorno = ERRO;
    	
    	    		
			/** *******************
			 * Existe Guia com locomoção NÃO utilizada e PAGA?
			 */
			List listaGuiasEmissaoProcesso = null;
			List listaLocomocao = null;
			
			//Consulta todas as guias pagas
    		//listaGuiasEmissaoProcesso = this.guiaEmissaoNe.consultaGuiasPagas(processoDt);
    		
//    		if( listaGuiasEmissaoProcesso != null && listaGuiasEmissaoProcesso.size() > 0 ) {
//    			
//    			//Consulta locomoções das guias paga
//    			listaLocomocao = this.locomocaoNe.consultaLocomocoesProcessoNaoUtilizadas(listaGuiasEmissaoProcesso);
//    			
//    			/** *******************
//        		 * A locomoção é do mesmo endereço?
//        		 */
//        		if( listaLocomocao != null && listaLocomocao.size() > 0 ) {
//        			String idLocomocaoEncontrada = null;
//        			
//        			for(int i=0; i < listaLocomocao.size(); i++) {
//        				LocomocaoDt locomocaoDt = (LocomocaoDt)listaLocomocao.get(i);
//        				
//        				//Endereço IGUAL, pega a primeira locomoção encontrada
//        				if( locomocaoDt.getBairroDt().getId().equals(escalaDt.getId_Bairro()) &&
//        					locomocaoDt.getZonaDt().getId().equals(escalaDt.getId_Zona()) && 
//        					locomocaoDt.getAreaDt().getId().equals(escalaDt.getId_Area()) ) {
//        					
//        					idLocomocaoEncontrada = locomocaoDt.getId();
//        					break;
//        				}
//        			}
//        			
//        			//Locomoção foi encontrada?
//        			if( idLocomocaoEncontrada != null )
//	        			//Retorna o ID da locomoção que irá ser utilizada nesta distribuição para ser marcada como utilizada e vinculada ao Mandado Judicial.
//        				//Aqui é o retorno com sucesso, ou seja, foi encontrado uma locomoção.
//	        			retorno = Funcoes.StringToInt(idLocomocaoEncontrada);
//        			else
//        				retorno = ENDERECO_LOCOMOCAO_DIFERENTE;
//        			
//        		}
//        		else
//        			retorno = GUIAS_SEM_LOCOMOCAO_LIVRE;
//        		
//    		}
//    		else
    			retorno = NAO_TEM_GUIA;
    		    	
    	
    	return retorno;
    }
    
    /**
	 * Método para distribuir o mandado para o próximo oficial da fila da escala
	 * @param PendenciaDt
	 * @param ProcessoParteDt
	 * @param ProcessoDt
	 * @param LogDt
	 * @param String idMandadoTipo
	 * @param String prazoMandadoJudicial
	 * @param String oficialCompanheiro
	 * @param Integer assistencia
	 * @return int
	 * @throws Exception
	 */
	public int distribuirMandado(PendenciaDt pendenciaDt, ProcessoParteDt processoParteDt, ProcessoDt processoDt, LogDt logDt, String idMandadoTipo, String prazoMandadoJudicial, String oficialCompanheiro, Integer assistencia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
    	int retorno = SEM_SUCESSO;
    	Integer idLocomocao = null;
    	
    	boolean distribuido = false;
    	String oficialMandado_1 = null; //Oficial Principal que receberá a distribuição
    	String oficialMandado_2 = null; //Oficial Companheiro que receberá a distribuição
    	
    	EscalaDt escalaDt = null;
    	
    	try{
    		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    		//TODO Fred: obFabricaConexao.iniciarTransacao();
    		
    		/** ********************************
    		 * Lógica de distribuição do mandado
    		 */
    		escalaDt = escalaNe.consultarEscala(processoParteDt.getEnderecoParte().getId_Bairro(), pendenciaDt.getResponsavel().getId_Serventia(), idMandadoTipo);
    		
    		/** *******************
    		 * É de assistência?
    		 * Se não for de assistência verificar se tem guia com locomoção para o endereço solicitado.
    		 */
    		if( assistencia != null ) {
	    		if( assistencia == MandadoJudicialDt.NAO_ASSISTENCIA ) {
	    			retorno = this.distribuirMandadoLocomocaoPaga(processoDt, escalaDt);
	    			if( retorno > 0 )
	    				idLocomocao = new Integer(retorno);
	    		}
	    		else
	    			retorno = 1; //Libera para distribuir
    		}
    		else {
    			retorno = ERRO;
    		}
    		
    		/** *******************
    		* Se retorno maior que zero então foi encontrado uma locomoção para o endereço desejado, e o valor em 'retorno' será o id da locomoção
    		* para ser marcada como utilizada depois de distribuído o mandado para o oficial.
    		*/
    		if( retorno > 0 ) {
	    		/** ********************************
	    		 * Distribuir Mandado para oficial
	    		 */
	    		if( escalaDt != null ) {
		    		if( oficialCompanheiro.equals(String.valueOf(MandadoJudicialDt.SIM_OFICIAL_COMPANHEIRO)) ) {
		    			//Oficial Companheiro: Mandado Judicial deve ter 2 oficiais
		    			//DistribuicaoMandado.getInstance().setDistribuicaoFixa(mandadoJudicialDt.getEscalaDt().getId(), mandadoJudicialDt.getServentiaCargoEscalaDt_1().getId());
		    			oficialMandado_1 = DistribuicaoMandado.getInstance().getDistribuicao(escalaDt.getId(), null);
		    			oficialMandado_2 = DistribuicaoMandado.getInstance().getDistribuicao(escalaDt.getId(), Funcoes.StringToInt(oficialMandado_1));
		    			if( oficialMandado_1 != null && oficialMandado_2 != null )
		    				distribuido = true;
		    		}
		    		else {
		    			//Mandado Judicial deve ter 1 oficial
		    			oficialMandado_1 = DistribuicaoMandado.getInstance().getDistribuicao(escalaDt.getId(), null);
		    			if( oficialMandado_1 != null )
		    				distribuido = true;
		    		}
		    		
		    		//Consulta Serventia para obter a comarca
		    		ServentiaDt serventiaDt = serventiaNe.consultarId(pendenciaDt.getResponsavel().getId_Serventia());
		    		
		    		//Consulta o Bairro para obter o Id_Cidade do Bairro
		    		BairroDt bairroDt = bairroNe.consultarId(processoParteDt.getEnderecoParte().getId_Bairro());
		    		
		    		//String dataLimite = prazoSuspensoNe.getProximoDiaUtil(Funcoes.dateToStringSoData(new Date()), Funcoes.StringToInt(prazoMandadoJudicial), serventiaDt.getId_Comarca(), bairroDt.getId_Cidade(), escalaDt.getId_Serventia(), obFabricaConexao);
		    		
		    		//Atualiza Mandado Judicial para alterar limite do prazo, oficial de justiça, e do Status
		    		MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
		    		
		    		//Cria o Mandado Status para salvar status inicial do mandado judicial
		    		mandadoJudicialDt.setId_MandadoJudicialStatus(String.valueOf(MandadoJudicialStatusDt.DISTRIBUIDO));
		    		
		    		if( oficialCompanheiro.equals(String.valueOf(MandadoJudicialDt.SIM_OFICIAL_COMPANHEIRO)) ) {
		    			if( oficialMandado_1 != null && oficialMandado_2 != null ) {
			    			//Adiciona os 2 oficiais(Principal e Companheiro)
		    				UsuarioServentiaDt mandadoOficialPrincipal = new UsuarioServentiaDt();
				    		mandadoOficialPrincipal.setId(oficialMandado_1);
				    		mandadoJudicialDt.setUsuarioServentiaDt_1(mandadoOficialPrincipal);
				    		
				    		UsuarioServentiaDt mandadoOficialCompanheiro = new UsuarioServentiaDt();
				    		mandadoOficialCompanheiro.setId(oficialMandado_2);
				    		mandadoJudicialDt.setUsuarioServentiaDt_2(mandadoOficialCompanheiro);
		    			}
		    		}
		    		else {
		    			//Adiciona somente o oficial principal
		    			UsuarioServentiaDt mandadoOficialPrincipal = new UsuarioServentiaDt();
		    			mandadoOficialPrincipal.setId(oficialMandado_1);
			    		mandadoJudicialDt.setUsuarioServentiaDt_1(mandadoOficialPrincipal);
		    		}
		    		
		    		mandadoJudicialDt.setId_MandadoTipo(idMandadoTipo);
		    		mandadoJudicialDt.setId_ProcessoParte(processoParteDt.getId_ProcessoParte());
		    		mandadoJudicialDt.setId_EnderecoParte(processoParteDt.getEnderecoParte().getId());
		    		mandadoJudicialDt.setId_Pendencia(pendenciaDt.getId());
		    		
		    		//HELLENO
		    		// Verificar se nesta tabela realmente será guardado
		    		// o id_area ou se também receberá id_escala_tipo
		    		//mandadoJudicialDt.setId_Area(escalaDt.getId_Area());
		    		mandadoJudicialDt.setId_Area(processoDt.getId_Area());
		    		
		    		mandadoJudicialDt.setId_Zona(escalaDt.getId_Zona());
		    		mandadoJudicialDt.setId_Regiao(escalaDt.getId_Regiao());
		    		mandadoJudicialDt.setId_Bairro(escalaDt.getId_Bairro());
		    		mandadoJudicialDt.setId_Escala(escalaDt.getId());
		    		mandadoJudicialDt.setValor(processoDt.getValor());
		    		mandadoJudicialDt.setAssistencia(String.valueOf(assistencia));
		    		mandadoJudicialDt.setDataDistribuicao(Funcoes.dateToStringSoData(new Date()));
//		    		mandadoJudicialDt.setDataLimite(dataLimite);
		    		mandadoJudicialDt.setId_MandadoJudicialStatus(String.valueOf(MandadoJudicialStatusDt.DISTRIBUIDO));
		    		
		    		mandadoJudicialDt.setId_UsuarioLog(logDt.getId_Usuario());
		    		mandadoJudicialDt.setIpComputadorLog(logDt.getIpComputador());
		    		
		    		//Salva Mandado Distribuído
		    		this.mandadoJudicialNe.salvar(mandadoJudicialDt);
		    		
		    		/** *******************
		    		 * IMPORTANTE
		    		 * Vincula o mandado judicial na locomoçao e marca esta locomoção como utilizada.
		    		 */
		    		if( idLocomocao != null && idLocomocao > 0 ) {
			    		LocomocaoDt locomocaoDt = new LocomocaoNe().consultarId(String.valueOf(idLocomocao));
			    		if (locomocaoDt != null) {
			    			locomocaoDt.setMandadoJudicialDt(mandadoJudicialDt);
			    		}
			    		locomocaoNe.salvar(locomocaoDt);
		    		}
		    		
		    		//TODO Fred: obFabricaConexao.finalizarTransacao();
		    		retorno = SUCESSO;
	    		}
	    		else {
	        		retorno = NAO_ENCONTRADO_ESCALA;
	    		}
    		}// Fim 'if' do retorno da locomoção 
    	}
    	catch(Exception e) {
    		//TODO Fred: obFabricaConexao.cancelarTransacao();
    		retorno = ERRO;
    		
    		//Se oficialMandado != null, então o erro deve fazer voltar a fila de distribuição.
    		if( distribuido ) {
    			if( escalaDt != null ) {
	    			if( oficialMandado_1 != null )
	        			DistribuicaoMandado.getInstance().setDistribuicaoFixa(escalaDt.getId(), oficialMandado_1);
	    			if( oficialMandado_2 != null )
	    				DistribuicaoMandado.getInstance().setDistribuicaoFixa(escalaDt.getId(), oficialMandado_2);
    			}
    		}
    		
    		//Identifica se o mandado já está cadastrado/distribuído.
    		String message = e.getMessage();
    		if( message.contains("Duplicate") ) {
    			retorno = JA_EXISTE;
    		}
    		else {
    			//Somente lança a exception se for erro mesmo
    			throw e;
    		}
    	}
    	finally{
    		obFabricaConexao.fecharConexao();
    	}
    	
    	return retorno;
    }
}
