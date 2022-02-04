package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ps.ProcessoDebitoPs;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoParteDebitoNe extends ProcessoParteDebitoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -8803132948006626457L;

    /**
	 * Verifica dados obrigat�rios para o cadastro de D�bitos
	 */
	public String Verificar(ProcessoParteDebitoDt dados) {
		String stRetorno = "";

		if (dados.getId_ProcessoParte() == null || dados.getId_ProcessoParte().length() == 0 || dados.getNome() == null || dados.getNome().length() == 0) stRetorno += "Selecione uma Parte para o D�bito. \n";
		if (dados.getId_ProcessoDebito() == null || dados.getId_ProcessoDebito().length() == 0) stRetorno += "Selecione o Tipo de D�bito. \n ";
		if (dados.getProcessoDebitoStatus() == null || dados.getProcessoDebitoStatus().length() == 0) stRetorno += "Selecione o Status do D�bito. \n ";
		if (dados.isBaixado() && (dados.getDataBaixa() == null || dados.getDataBaixa().length() == 0)) stRetorno += "Informe a Data Baixa do D�bito. \n ";
		if (obDados != null && !obDados.isBaixado() && dados.isBaixado() && dados.getId_GuiaEmissao() != null && dados.getId_GuiaEmissao().trim().length() > 0) {
			try {
				if ((new ProcessoParteDebitoCadinNe()).isGuiaEnviadaCadin(dados.getId_GuiaEmissao())) {
					stRetorno += "Imposs�vel baixar, pois o d�bito j� foi enviado ao CADIN. \n ";	
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return stRetorno;
	}
	
	public String VerificarCadastroServentia(ProcessoParteDebitoDt dados, UsuarioNe usuarioSessao) {
		String stRetorno = "";

		if (dados.getDividaSolidaria() == null || dados.getDividaSolidaria().length() == 0) stRetorno += "Selecione uma op��o para D�vida Solid�ria. \n";
		if (dados.getTipoParte() == null || dados.getTipoParte().length() == 0) stRetorno += "Selecione uma op��o para Tipo de Parte. \n";
		if (dados.NotIsDividaSolidaria() && (dados.getId_ProcessoParte() == null || dados.getId_ProcessoParte().length() == 0 || dados.getNome() == null || dados.getNome().length() == 0)) stRetorno += "Selecione uma Parte para o D�bito. \n";
		if (dados.getId_ProcessoDebito() == null || dados.getId_ProcessoDebito().length() == 0) stRetorno += "Selecione o Tipo de D�bito. \n ";
		if (dados.getProcessoDebitoStatus() == null || dados.getProcessoDebitoStatus().length() == 0) stRetorno += "Selecione uma Guia Final. \n ";
		
		if (stRetorno.trim().length() == 0) {
			if (!usuarioSessao.isAnalistaFinanceiro()) {
				if (Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.NENHUM &&
					Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.NOVO &&
					Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.DEVOLVIDO_PELO_FINANCEIRO) {
					stRetorno += "Somente status novo e devolvido pelo financeiro poder� ser atualizado pela serventia. \n ";
				}
			}
		}
		
		if (stRetorno.length() == 0) {
			if (dados.getListaPartesComDebito() == null) dados.setListaPartesComDebito(new ArrayList());
			
			List listaPartesPoloAtivoPassivo;
			if (dados.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO))) {
				listaPartesPoloAtivoPassivo = dados.getListaPartesPromoventes();
			} else {
				listaPartesPoloAtivoPassivo = dados.getListaPartesPromovidas();
			}				
			
			for (Object parteObj : listaPartesPoloAtivoPassivo) {
				ProcessoParteDt parteDt = (ProcessoParteDt)parteObj;
				
				boolean possuiDebito = false;
				for (Object processoParteObj : dados.getListaPartesComDebito()) {
					ProcessoParteDebitoDt processoParteDt = (ProcessoParteDebitoDt)processoParteObj;
					
					if (processoParteDt.getId_ProcessoParte() != null && parteDt.getId() != null &&
					    processoParteDt.getId_GuiaEmissao() != null && dados.getId_GuiaEmissao() != null &&
					    processoParteDt.getId_ProcessoParte().equalsIgnoreCase(parteDt.getId()) &&
					    processoParteDt.getId_GuiaEmissao().equalsIgnoreCase(dados.getId_GuiaEmissao())) {
						possuiDebito = true;
					}
				}
				
				if (possuiDebito) {
					stRetorno += "D�bito j� cadastrado para essa parte, favor verificar. \n ";
					break;
				}
			}
		}
		
		return stRetorno;
	}
	
	public String VerificarFinanceiro(ProcessoParteDebitoDt dados) {
		String stRetorno = "";

		if (dados.getId_ProcessoDebitoStatus() == null || Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.NENHUM) {
			stRetorno += "Selecione o status do d�bito. \n ";
		} else if ((Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.DEVOLVIDO_PELO_FINANCEIRO ||
				    Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.SUSPENSO ||
				    Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.BAIXADO) &&
				   (dados.getObservacaoProcessoDebitoStatus() == null || dados.getObservacaoProcessoDebitoStatus().trim().length() == 0)) {
			stRetorno += "Favor inserir uma observa��o para o status selecionado. \n ";
		}
		
		return stRetorno;
	}
	
	public String VerificaCadastro(ProcessoParteDebitoDt dados) throws Exception {
		String stRetorno = "";
		
		//consulta debitos do processo
		List<ProcessoParteDebitoDt> listaProcessoParteDebitoDt = this.consultarPartesComDebitos(dados.getId_Processo());
		
		if( listaProcessoParteDebitoDt != null && !listaProcessoParteDebitoDt.isEmpty() ) {
			
			boolean mesmaParteMesmaGuia = false;
			
			for(ProcessoParteDebitoDt processoParteDebitoDt: listaProcessoParteDebitoDt) {
				if( processoParteDebitoDt.getId_GuiaEmissao() != null && dados != null && dados.getId_GuiaEmissao() != null ) {
					//J� possui cadastro para mesma parte?
					if( dados.getId_ProcessoParte() != null 
						&& dados.getId_ProcessoParte().equals(processoParteDebitoDt.getId_ProcessoParte()) ) {
						
						mesmaParteMesmaGuia = true;
						break;
					}
					
				}
			}
			
			if( mesmaParteMesmaGuia ) {
				stRetorno += "Parte j� possui d�bito cadastrado. ";
			}
			
		}
		
		return stRetorno;
	}
	
	public String VerificarExclusao(ProcessoParteDebitoDt dados, UsuarioNe usuarioSessao) throws Exception {
		String stRetorno = "";
		
		if (dados == null) {
			stRetorno += "Nenhum D�bito foi selecionado para exclus�o ou n�o existe mais, favor consultar novamente. \n ";
		} else if (dados.getId_GuiaEmissao() != null && dados.getId_GuiaEmissao().trim().length() > 0) {
			if (!(dados.getId_Serventia().equals(usuarioSessao.getUsuarioDt().getId_Serventia()) ||
				 Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA)) {
				stRetorno += "N�o � poss�vel excluir um D�bito de Processo de outra Serventia.";
			} else if ((new ProcessoParteDebitoCadinNe()).isGuiaEnviadaCadin(dados.getId_GuiaEmissao())) {
				stRetorno += "Imposs�vel excluir, pois o d�bito j� foi enviado ao CADIN. \n ";	
			} else if (Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.NOVO &&
					   Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.DEVOLVIDO_PELO_FINANCEIRO) {
				stRetorno += "Imposs�vel excluir, somente status novo e devolvido pelo financeiro poder� ser exclu�do pela serventia. \n ";
			}					
		}
		return stRetorno;
	}

	/**
	 * Salva D�bito para partes de processo
	 * 
	 * @param processoParteBeneficioDt, objeto com dados dos benef�cios
	 * @author msapaula
	 */

	public void salvarDebitoPartes(ProcessoParteDebitoDt processoParteDebitodt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			if (processoParteDebitodt.isDividaSolidaria()) {
				if (processoParteDebitodt.getListaPartesComDebito() == null) processoParteDebitodt.setListaPartesComDebito(new ArrayList());
				
				List listaPartesPoloAtivoPassivo;
				if (processoParteDebitodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO))) {
					listaPartesPoloAtivoPassivo = processoParteDebitodt.getListaPartesPromoventes();
				} else {
					listaPartesPoloAtivoPassivo = processoParteDebitodt.getListaPartesPromovidas();
				}				
				
				for (Object parteObj : listaPartesPoloAtivoPassivo) {
					ProcessoParteDt parteDt = (ProcessoParteDt)parteObj;
					
					boolean possuiDebito = false;
					for (Object processoParteObj : processoParteDebitodt.getListaPartesComDebito()) {
						ProcessoParteDebitoDt processoParteDt = (ProcessoParteDebitoDt)processoParteObj;
						
						if (processoParteDt.getId_ProcessoParte() != null && parteDt.getId() != null &&
						    processoParteDt.getId_GuiaEmissao() != null && processoParteDebitodt.getId_GuiaEmissao() != null &&
						    processoParteDt.getId_ProcessoParte().equalsIgnoreCase(parteDt.getId()) &&
						    processoParteDt.getId_GuiaEmissao().equalsIgnoreCase(processoParteDebitodt.getId_GuiaEmissao())) {
							possuiDebito = true;
						}
					}
					
					if (!possuiDebito) {
						ProcessoParteDebitoDt processoParteDebitoInserirdt = new ProcessoParteDebitoDt();
						processoParteDebitoInserirdt.copiar(processoParteDebitodt);
						processoParteDebitoInserirdt.setId_UsuarioLog(processoParteDebitodt.getId_UsuarioLog());
						processoParteDebitoInserirdt.setIpComputadorLog(processoParteDebitodt.getIpComputadorLog());
						processoParteDebitoInserirdt.setId("");
						processoParteDebitoInserirdt.setId_ProcessoParte(parteDt.getId());
						
						this.salvar(processoParteDebitoInserirdt, obFabricaConexao);
					}
				}
			} else {
				this.salvar(processoParteDebitodt, obFabricaConexao);	
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva um D�bito baseado em uma conex�o j� existente
	 */
	public void salvar(ProcessoParteDebitoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
		if (dados.getId().length() == 0) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoParteDebito", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoParteDebito",dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	/**
	 * Consulta os d�bitos cadastrados baseado nos par�metros passados
	 * 
	 * @param nomeParte, filtro para nome da parte
	 * @param cpfCnpjParte, filtro para cpf/cnpj da parte
	 * @param processoNumero, filtro para n�mero de processo
	 * @param posicao, par�metro para pagina��o
	 * 
	 * @author msapaula, hmgodinho
	 */
	public List consultarDebitosProcessoParte(String nomeParte, String cpfCnpjParte, String processoNumero, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		
		String cpfParte = "", cnpjParte = "";
		if(!cpfCnpjParte.equals("")) {
			if(cpfCnpjParte.length() == 11) {
				cpfParte = cpfCnpjParte;
			}
			if(cpfCnpjParte.length() == 14) {
				cnpjParte = cpfCnpjParte;
			}
		}
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDebitosProcessoParte(nomeParte, cpfParte, cnpjParte, processoNumero, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta os d�bitos cadastrados baseado nos par�metros passados usando JSON
	 * 
	 * @param nomeParte, filtro para nome da parte
	 * @param cpfCnpjParte, filtro para cpf/cnpj da parte
	 * @param processoNumero, filtro para n�mero de processo
	 * @param posicao, par�metro para pagina��o
	 * 
	 * @author hmgodinho
	 * @author fasoares (Adicionei o parametro 6 e 7 tipoDebito e status)
	 */
	public String consultarDebitosProcessoParteJSON(String nomeParte, String cpfCnpjParte, String processoNumero, String numeroGuia, String serventia, String tipoDebito, String status, String posicao) throws Exception {
		String stTemp = null;
		FabricaConexao obFabricaConexao = null;
		
		if(!nomeParte.equals("")){
			nomeParte = Funcoes.converteNomeSimplificado(nomeParte);
		}
		
		String cpfParte = "", cnpjParte = "";
		if(!cpfCnpjParte.equals("")) {
			if(cpfCnpjParte.length() == 11) {
				cpfParte = cpfCnpjParte;
			}
			if(cpfCnpjParte.length() == 14) {
				cnpjParte = cpfCnpjParte;
			}
		}
		
		String procNumero = "", digitoVerificador = "";
		if(!processoNumero.equals("")){
			String[] temp = processoNumero.split("[-\\.]");
			procNumero = temp[0];
			if (temp.length > 1)
				digitoVerificador = temp[1];
		}
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDebitosProcessoParteJSON(nomeParte, cpfParte, cnpjParte, procNumero, digitoVerificador, numeroGuia, serventia, tipoDebito, status, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Chama m�todo para consultar dados de processo
	 * @param processoNumero
	 * @return processoBuscaDt, objeto com dados b�sicos de processo
	 */
	public ProcessoDt consultarProcessoNumero(String processoNumero) throws Exception {
		
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.consultarProcessoNumeroCompleto(processoNumero,null);
		
	}
	
	/**
	 * Chama m�todo para consultar dados de processo
	 * @param processoNumero
	 * @return processoBuscaDt, objeto com dados b�sicos de processo
	 */
	public ProcessoDt consultarIdCompleto(String id_processo) throws Exception {
		
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.consultarIdCompleto(id_processo);
		
	}	
	
	public String consultarDescricaoProcessoDebitoJSON(String descricao, String posicao) throws Exception {
		return new ProcessoDebitoNe().consultarDescricaoProcessoDebitoJSON(descricao, posicao);
	}
	
	public List consultarProcessoDebito(String descricao) throws Exception {
		return new ProcessoDebitoNe().consultarProcessoDebito(descricao);
	}
	
	public List consultarProcessoDebitoStatus(String descricao) throws Exception {
		return new ProcessoDebitoStatusNe().consultarProcessoDebitoStatus(descricao);
	}
	
	public List consultarPartesComDebitos(String id_Processo ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarPartesComDebitos(id_Processo);		
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public List<ProcessoParteDebitoDt> consultarPartesComDebitos(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPartesComDebitos(id_Processo);   
	}
	
	private List consultarGuias(String id_Processo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		GuiaEmissaoNe guiaEmissaone = new GuiaEmissaoNe();

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			tempList = guiaEmissaone.consultarGuiaEmissao(obFabricaConexao, id_Processo, guiaEmissaone.consultarListaId_GuiaTipo(null));;			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	/**
	 * Ocorr�ncia 2020/5098
	 * Alterado m�todo para permitir cadastrar debito para guia GRS simplificada gen�rica.
	 * 
	 * ALTERA��O - Ap�s as fiscaliza��es realizadas pela Central de Arrecada��o da Diretoria Financeira, � constante as Contadorias Judiciais ou escrivania, fazer a emiss�o da Guia GRS - Guia de Recolhimento Simplificado dentro do sistema Projud.

Desta forma, fazemos a seguintes solicita��o:

1) - Processos que possui Guia GRS (Guia de Recolhimento Simplificada) sem pagamento, adicionar o valor da guia ao c�lculo da guia de custas finais;

2) - Permitir fazer o cadastro de d�bito para custas em aberto para a guia tipo (GRS - Simplificada). Essa situa��o pode ocorrer para processos que possui guia de custas finais emitidas e, ap�s a fiscaliza��o foi observado que existe remanescente a ser pago; Pode tamb�m ocorre em processo que n�o existe pend�ncias ou previs�o de custas, mas por determina��o do magistrado faz-se necess�rio emitir a referida guia para fazer pagamento. Desta forma, caso a mesma n�o seja paga, � objeto de fazer o seu registro no cadastro de d�bito.
	 * 
	 * @param String id_Processo
	 * @return 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public List consultarGuiasFinais_GRSSimplificada_AguardandoPagamento(String id_Processo) throws Exception {
		List tempList = this.consultarGuias(id_Processo);
		List listaGuiaEmissaoDt = new ArrayList();
		
		if (tempList != null) {
			for(int i = 0; i < tempList.size(); i++) {
				GuiaEmissaoDt guiaEmissao = (GuiaEmissaoDt) tempList.get(i);
				if (guiaEmissao != null && guiaEmissao.isGuiaAguardandoPagamento() && (guiaEmissao.isGuiaFinal() || guiaEmissao.isGuiaFinalSPG() || guiaEmissao.isGuiaGRSGenerica())) {
					listaGuiaEmissaoDt.add(guiaEmissao);
				}
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public ProcessoParteDebitoDt consultarProcessoNumeroProad(String numeroProcessoProad) throws Exception {
		ProcessoParteDebitoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarProcessoNumeroProad(numeroProcessoProad );
			if (dtRetorno != null) obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ProcessoParteDebitoDt consultarProcessoNumeroProad(String numeroProcessoProad, FabricaConexao obFabricaConexao) throws Exception {

		ProcessoParteDebitoDt dtRetorno=null;
		
		ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarProcessoNumeroProad(numeroProcessoProad ); 
		if (dtRetorno != null) obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}
	
	public ProcessoDebitoStatusDt consultarProcessoDebitoStatusId(String idProcessoDebitoStatus) throws Exception {
		FabricaConexao obFabricaConexao = null;
		ProcessoDebitoStatusNe processoDebitoStatusNe = new ProcessoDebitoStatusNe();

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return processoDebitoStatusNe.consultarId(idProcessoDebitoStatus);
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List<ProcessoParteDebitoDt> consultarListaNumeroProcessoCompleto(String numeroProcessoCompleto, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarListaNumeroProcessoCompleto(numeroProcessoCompleto );		
	}
	
	public List<ProcessoParteDebitoDt> consultarListaIdProcesso(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarListaIdProcesso(idProcesso );		
	}
	
	public void atualizeDadosAnteriores(ProcessoParteDebitoDt obDados) {
		super.obDados = obDados;
	}
	
	public List<ProcessoParteDebitoDt> consultarListaLiberadoCadinAindaNaoEnvido(FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarListaLiberadoCadinAindaNaoEnvido();		
	}
	
	/**
	 * M�todo para consulta processo-parte-debito pelo id-guia-emis.
	 * 
	 * @param String idGuiaEmissao
	 * @return ProcessoParteDebitoDt
	 * @throws Exception
	 * @author fasoares
	 */
	public ProcessoParteDebitoDt consultarProcessoParteDebitoIdGuiaEmissao(String idGuiaEmissao) throws Exception {
		ProcessoParteDebitoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			
			if( idGuiaEmissao != null && !idGuiaEmissao.isEmpty() ) {
				ProcessoParteDebitoPs obPersistencia = new ProcessoParteDebitoPs(obFabricaConexao.getConexao());
				dtRetorno = obPersistencia.consultarProcessoParteDebitoIdGuiaEmissao(idGuiaEmissao);
			}
		
		 }finally {
			obFabricaConexao.fecharConexao();
		}
		
		return dtRetorno;
	}
}
