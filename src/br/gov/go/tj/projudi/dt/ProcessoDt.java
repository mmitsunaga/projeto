package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class ProcessoDt extends ProcessoDtGen {

    private static final long serialVersionUID = 8367773681888975498L;
    
    public static final int CodigoPermissaoPDF_COMPLETO = 286;
    
	private String id_Assunto;
	private String assunto;
	private String comarcaCodigo;
	private String comarca;
	private String serventiaSubTipo;
	private String serventiaSubTipoCodigo;	

	private List listaMovimentacoes; // Lista de Movimentações do processo
	private List listaAdvogados;

	private ProcessoDt processoDependenteDt; // Armazenar processo originário

	private boolean temApensos; // Variável para validar se processo possui apensos
	
	private boolean processoPrecatoriaExpedidaOnline; // Variável para validar se processo é do tipo precatória on-line gerada por um magistrado
	
	private List listaMovimentacoesFisico; // Lista de Movimentações relacionadas às sentenças do processo físico
	
    //Permissões para processos para não ter necessida de criar um Dt para cada caso
	public static final int CodigoPermissao = 243; //Permissão geral para consulta de processos
	public static final int CodigoPermissaoBuscaUsuarioExterno = 277; //Permissão para consulta por usuários externos
	
	//Refere-se aos id's da tabela ProcCustaTipo
	public static final int COM_CUSTAS = 1;
	public static final int ASSISTENCIA_JURIDICA = 2;
	public static final int ISENTO = 3;
	
	//Proc_status
	public static final int ATIVO 		= 1;
	public static final int ARQUIVADO 	= 2;
	public static final int ERRO_MIGRACAO = 7;

	public static final String MISTO_SPG = "SPM";
	public static final String MISTO_SSG = "SSM";
	public static final String MISTO_SPG_PENDENTE_LIMPEZA_SPG = "SPMP";
	public static final String MISTO_SPG_CONCLUIDA_LIMPEZA_SPG = "SPMC";
	

	//Variáveis utilizadas na busca de processos
	private String poloPassivo;
	private String poloAtivo;
	private String cpfCnpjParte;
	private String Id_ServentiaOrigemRecurso;
	private String ServentiaOrigemRecurso;
	private String Localizador;
	
	private List listaPolosAtivos;
	private List listaPolosPassivos;
	private List listaOutrasPartes;
	private List listaProcessoParteAdvogado;

	//Variáveis utilizadas na busca de processos por advogado
	private String oabNumero;
	private String oabComplemento;
	private String oabUf;
	private String situacaoAdvogadoProcesso;

	private String hash; //Atributo para validação

	private String id_Recurso; //armazena recurso vinculado ao processo
	private String areaDistribuicao; //variável utilizada no cadastro com dependência
	private String serventiaTipoCodigo;

	private RecursoDt recursoDt; //Armazenar recurso inominado ativo

	private ServentiaCargoDt serventiaCargoResponsavelDt; //Juiz responsável pelo processo
	
	private List listaAssuntos; // Lista de assuntos do processo, necessária na autuação de recursos
	
	//atributo utilizado na funcionalide ... para indicar se o processo possui peticionamento após conclusão
	private boolean existePeticaoPendente;
	
	//atributo utilizado na funcionalide ... para indicar se o processo possui guias pendentes de pagamento
	private boolean existeGuiasPendentes;
	
	private ServentiaCargoDt serventiaCargoRevisorResponsavelDt; //Desembargador responsável pelo processo de 2º Gruau como Revisor	
	private ServentiaCargoDt serventiaCargoVogalResponsavelDt; //Desembargador responsável pelo processo de 2º Gruau como Vogal
	private ProcessoCriminalDt processoCriminalDt;
	
	private String processoNumeroAntigoTemp;
	private String tabelaOrigemTemp;
	private String dataDigitalizacao;
	private String Id_AreaDistribuicaoRecursal;
	
	private String descricaoPoloAtivo;
	private String descricaoPoloPassivo;
	
	private String usuarioServentia;
	private String id_UsuarioServentia;
	
	//Variável genérica criada para armazenar alguma descrição/informação
	//relacionada ao processo.
	private String outraDescricaoGenerica;
	
	private String IdCNJClasse;
	
	public boolean isTemPoloAtivo(){
		if (listaPolosAtivos!=null && listaPolosAtivos.size()>0)
			return true;
		return false;
	}
	
	
	//Variáveis utilizadas na busca de processos físicos SSG OU SPG
	private String processoFisicoTipo;
	private String processoFisicoNumero;
	private String processoFisicoComarcaNome;
	private String processoFisicoComarcaCodigo;

	private String NomeParteBusca = null;
	private String ParteTipoBusca = null;
	private String ParteTipoCodigoBusca = null;
	private String Id_ParteTipoBusca=null;

	private boolean bo100Digital = false;
	
	public String getId_AreaDistribuicaoRecursal()  {return Id_AreaDistribuicaoRecursal;}
	public void setId_AreaDistribuicaoRecursal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AreaDistribuicaoRecursal = ""; }else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicaoRecursal = valor;}
	
	public void limpar() {
		super.limpar();
		super.setSegredoJustica("false");
		poloAtivo = "";
		poloPassivo = "";
		cpfCnpjParte = "";
		id_Recurso = "";
		areaDistribuicao = "";
		Id_ServentiaOrigemRecurso = "";
		ServentiaOrigemRecurso = "";
		Id_AreaDistribuicaoRecursal = ""; 
		recursoDt = null;
		listaPolosAtivos = null;
		listaPolosPassivos = null;
		listaOutrasPartes = null;
		listaProcessoParteAdvogado = null;
		listaAssuntos = null;
		serventiaTipoCodigo = "";
		serventiaCargoResponsavelDt = null;
		oabNumero = "";
		oabComplemento = "";
		oabUf = "";
		situacaoAdvogadoProcesso = "";
		existePeticaoPendente = false;
		existeGuiasPendentes = false;
		serventiaCargoRevisorResponsavelDt = null;
		serventiaCargoVogalResponsavelDt = null;
		processoCriminalDt = null;
		processoNumeroAntigoTemp = null;
		tabelaOrigemTemp = null;
		descricaoPoloAtivo = "";
		descricaoPoloPassivo = "";
		setDataDigitalizacao("");
		id_Assunto = "";
		assunto = "";
		comarcaCodigo = "";
		comarca = "";
		serventiaSubTipo = "";
		serventiaSubTipoCodigo = "";
		listaAdvogados = null;
		listaMovimentacoes = null;
		processoDependenteDt = null;
		temApensos = false;
		processoPrecatoriaExpedidaOnline = false;
		usuarioServentia = "";
		id_UsuarioServentia = "";
		outraDescricaoGenerica = "";
		processoFisicoTipo = "";
		processoFisicoNumero = "";
		processoFisicoComarcaNome = "";
		processoFisicoComarcaCodigo = "";
		IdCNJClasse = "";
		listaMovimentacoesFisico = null;
	}

	public String getId_Processo() {
		return super.getId();
	}

	public void setId_Processo(String valor) {
		if (valor != null) super.setId(valor);
	}
	
	public List getListaProcessoParteAdvogado() {
		return listaProcessoParteAdvogado;
	}

	public void setListaProcessoParteAdvogado(List listaProcessoParteAdvogado) {
		this.listaProcessoParteAdvogado = listaProcessoParteAdvogado;
	}
	
	public ProcessoParteDt getPoloAtivo(int posicao) throws MensagemException {
		if (listaPolosAtivos!=null && listaPolosAtivos.size()>posicao){
			return (ProcessoParteDt) listaPolosAtivos.get(posicao);
		}
		throw new MensagemException("Não foi possível localizar o promovente, volte ao início e tente novamente.");
	}
	
	public ProcessoParteDt getPoloPassivo(int posicao) throws MensagemException {
		if (listaPolosPassivos!=null && listaPolosPassivos.size()>posicao){
			return (ProcessoParteDt) listaPolosPassivos.get(posicao);
		}
		throw new MensagemException("Não foi possível localizar o promovente, volte ao início e tente novamente.");
	}
	
	public List getListaPolosAtivos() {
		return listaPolosAtivos;
	}

	public void setListaPolosAtivos(List listaPolosAtivos) {
		this.listaPolosAtivos = listaPolosAtivos;
	}

	public void addListaPoloAtivo(ProcessoParteDt parteDt) {
		parteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));

		if (listaPolosAtivos == null) listaPolosAtivos = new ArrayList();
		this.listaPolosAtivos.add(parteDt);
	}

	/*
	 * Jesus 
	 * retorna o primeiro promovente da lista 
	 * testando null
	 */
	public String getPrimeiroPoloAtivoNome(){
		String stTemp="";
		if(listaPolosAtivos!=null && listaPolosAtivos.size()>0){
			ProcessoParteDt parte = (ProcessoParteDt) listaPolosAtivos.get(0);
			if (parte!=null) stTemp = parte.getNome();
		}
		return stTemp;
	}
	/*
	 * Jesus 
	 * retorna o primeiro promovido da lista 
	 * testando null
	 */
	public String getPrimeiroPoloPassivoNome(){
		String stTemp="";
		if(listaPolosPassivos!=null && listaPolosPassivos.size()>0){
			ProcessoParteDt parte = (ProcessoParteDt) listaPolosPassivos.get(0);
			if (parte!=null) stTemp = parte.getNome();
		}
		return stTemp;
	}
	/*
	 * Jesus 
	 * retorna o primeiro promovido da lista 
	 * testando null
	 */
	public String getPrimeiroPoloPassivoDataNascimento(){
		String stTemp="";
		if(listaPolosPassivos!=null && listaPolosPassivos.size()>0){
			ProcessoParteDt parte = (ProcessoParteDt) listaPolosPassivos.get(0);
			if (parte!=null) stTemp = parte.getDataNascimento();
		}
		return stTemp;
	}
	
	/*
	 * Jesus 
	 * retorna o primeiro promovido da lista 
	 * testando null
	 */
	public String getPrimeiroPoloPassivoNomeMae(){
		String stTemp="";
		if(listaPolosPassivos!=null && listaPolosPassivos.size()>0){
			ProcessoParteDt parte = (ProcessoParteDt) listaPolosPassivos.get(0);
			if (parte!=null) stTemp = parte.getNomeMae();
		}
		return stTemp;
	}
	
	public ProcessoParteDt getPrimeiroPoloAtivo(){
		ProcessoParteDt processoParteDt = null;
		if(listaPolosAtivos!=null && listaPolosAtivos.size()>0){
			ProcessoParteDt parte = (ProcessoParteDt) listaPolosAtivos.get(0);
			if (parte!=null) processoParteDt = parte;
		}
		return processoParteDt;
	}
	
	public ProcessoParteDt getPrimeiroPoloPassivo(){
		ProcessoParteDt processoParteDt = null;
		if(listaPolosPassivos!=null && listaPolosPassivos.size()>0){
			ProcessoParteDt parte = (ProcessoParteDt) listaPolosPassivos.get(0);
			if (parte!=null) processoParteDt = parte;
		}
		return processoParteDt;
	}
	
	public ProcessoParteDt getPrimeiraVitima(){
		ProcessoParteDt processoParteDt = null;
		
		for (int i=0; listaOutrasPartes!=null && i<listaOutrasPartes.size(); i++) {
			ProcessoParteDt parte = (ProcessoParteDt) listaOutrasPartes.get(i);
			if (parte!=null && parte.isVitima()) {
				processoParteDt = parte;
				break;
			}
		}
		return processoParteDt;
	}
	
	public List getListaPolosPassivos() {
		return listaPolosPassivos;
	}

	public void setListaPolosPassivos(List listaPromovidos) {
		this.listaPolosPassivos = listaPromovidos;
	}

	public void addListaPolosPassivos(ProcessoParteDt parteDt) {
		parteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));

		if (listaPolosPassivos == null) listaPolosPassivos = new ArrayList();
		this.listaPolosPassivos.add(parteDt);
	}

	public List getPartesAtivasPassivas() {
		List lista = new ArrayList();
		if (listaPolosAtivos != null) lista.addAll(listaPolosAtivos);
		if (listaPolosPassivos != null) lista.addAll(listaPolosPassivos);
		return lista;
	}
	
	public ProcessoParteDt getParte(String id_processoParte) {
		if (id_processoParte == null || id_processoParte.trim().length() == 0) return null;		
		List listaPartes = getPartesAtivasPassivas();		
		if (listaPartes != null) {
			for (int i=0;i < listaPartes.size();i++){
				ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
				if (parteDt != null && Funcoes.StringToLong(parteDt.getId()) == Funcoes.StringToLong(id_processoParte)) {
					return parteDt;
				}				
			}	
		}
		return null;		
	}

	public String getPoloAtivo() {
		return poloAtivo;
	}

	public void setPoloAtivo(String poloAtivo) {
		if (poloAtivo != null) this.poloAtivo = poloAtivo.trim();
	}

	public String getPoloPassivo() {
		return poloPassivo;
	}

	public void setPoloPassivo(String promovido) {
		if (promovido != null) this.poloPassivo = promovido.trim();
	}

	public String getCpfCnpjParte() {
		return cpfCnpjParte;
	}

	public void setCpfCnpjParte(String cpfCnpj) {
		if (cpfCnpj != null){
			this.cpfCnpjParte = cpfCnpj.trim().replaceAll("[/.-]", "");
		}
	}

	/**
	 * Retorna o número padrão de um processo, ou seja, o número mais o dígito verificador
	 * Ex.: 17.18
	 */
	public String getProcessoNumero() {
		String stRetorno = super.getProcessoNumero();
		if (getDigitoVerificador().length() > 0) stRetorno += "-" + getDigitoVerificador();
		return stRetorno;
	}

	/**
	 * Retorna o número completo de um processo, obedecendo a padronização do CNJ
	 */
	public String getProcessoNumeroCompleto() {
		return (Funcoes.completarZeros(super.getProcessoNumero(), 7) + "-" + Funcoes.completarZeros(getDigitoVerificador(), 2) + "." + Funcoes.completarZeros(getAno(),4) + "." + Configuracao.JTR + "." + Funcoes.completarZeros(getForumCodigo(), 4));
	}
	
	public String getProcessoNumeroAntigoCompleto() {
		return (Funcoes.completarZeros(super.getProcessoNumero(), 7) + "." + Funcoes.completarZeros(getDigitoVerificador(), 2) + "." + Funcoes.completarZeros(getAno(),4) + "." + Configuracao.JTR + "." + Funcoes.completarZeros(getForumCodigo(), 4));
	}
	
	public NumeroProcessoDt getNumeroProcessoDt() {
		try {
			return new NumeroProcessoDt(this.getProcessoNumeroCompleto());
		} catch (MensagemException e) {
			return null;
		}
	}

	/**
	 * Retorna somente o número do processo
	 */
	public String getProcessoNumeroSimples() {
		return super.getProcessoNumero();
	}

	public void setAnoProcessoNumero(String valor) {
		setAno(valor.substring(0, 4));
		setProcessoNumero(valor.substring(4, 11));
		setDigitoVerificador(valor.substring(11, 13));
	}

	public List getPartesProcesso() {
		List lista = new ArrayList();
		if (listaPolosAtivos != null) lista.addAll(listaPolosAtivos);
		if (listaPolosPassivos != null) lista.addAll(listaPolosPassivos);
		if (listaOutrasPartes != null) lista.addAll(listaOutrasPartes);
		return lista;
	}

	public String getId_Recurso() {
		return id_Recurso;
	}

	public void setId_Recurso(String id_Recurso) {
		if (id_Recurso != null) if (id_Recurso.equalsIgnoreCase("null")) this.id_Recurso = "";
		else if (!id_Recurso.equalsIgnoreCase("")) this.id_Recurso = id_Recurso;
	}

	public String getId_ServentiaOrigemRecurso() {
		return Id_ServentiaOrigemRecurso;
	}

	public void setId_ServentiaOrigemRecurso(String idServentiaOrigemRecurso) {
		if(idServentiaOrigemRecurso != null)
			Id_ServentiaOrigemRecurso = idServentiaOrigemRecurso;
	}
	

	public String getServentiaOrigemRecurso() {
		return ServentiaOrigemRecurso;
	}

	public void setServentiaOrigemRecurso(String serventiaOrigemRecurso) {
		if (serventiaOrigemRecurso != null)
			ServentiaOrigemRecurso = serventiaOrigemRecurso;
	}

	/*
	 * Jesus
	 * Pego o primeiro assunto da lista
	 */
	public ProcessoAssuntoDt getPrimeiroAssuntoLista(){
		ProcessoAssuntoDt assunto = null;
		if(listaAssuntos!=null && listaAssuntos.size()>0)
			assunto =(ProcessoAssuntoDt) listaAssuntos.get(0);
		
		return assunto;
	}
	public void removeAssunto(int indice){
		if (listaAssuntos!=null ){
			listaAssuntos.remove(indice);
		}
	}
	public List getListaAssuntos() {
		return listaAssuntos;
	}

	public void setListaAssuntos(List listaAssuntos) {
		this.listaAssuntos = listaAssuntos;
	}

	public void addListaAssuntos(ProcessoAssuntoDt dt) {
		if (listaAssuntos == null) listaAssuntos = new ArrayList();
		this.listaAssuntos.add(dt);
	}

	public String getServentiaTipoCodigo() {
		return serventiaTipoCodigo;
	}

	public void setServentiaTipoCodigo(String serventiaTipoCodigo) {
		if (serventiaTipoCodigo != null) this.serventiaTipoCodigo = serventiaTipoCodigo;
	}

	public String getAreaDistribuicao() {
		return areaDistribuicao;
	}

	public void setAreaDistribuicao(String areaDistribuicao) {
		if (areaDistribuicao != null) 
			if (areaDistribuicao.equalsIgnoreCase("null")) 
				this.areaDistribuicao = "";
		else if (!areaDistribuicao.equalsIgnoreCase("")) 
			this.areaDistribuicao = areaDistribuicao;
	}

	public RecursoDt getRecursoDt() {
		return recursoDt;
	}

	public void setRecursoDt(RecursoDt recursoDt) {
		this.recursoDt = recursoDt;
	}

	public ServentiaCargoDt getServentiaCargoResponsavelDt() {
		return serventiaCargoResponsavelDt;
	}
	
	public String getIdServentiaCargoResponsavelDt() {
		if( serventiaCargoResponsavelDt != null ) 
			return serventiaCargoResponsavelDt.getId();
		else
			return null;
	}

	public void setServentiaCargoResponsavelDt(ServentiaCargoDt serventiaCargoResponsavelDt) {
		this.serventiaCargoResponsavelDt = serventiaCargoResponsavelDt;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public List getListaOutrasPartes() {
		return listaOutrasPartes;
	}

	public void setListaOutrasPartes(List listaOutrasPartes) {
		this.listaOutrasPartes = listaOutrasPartes;
	}

	public void addListaOutrasPartes(ProcessoParteDt parteDt) {
		if (listaOutrasPartes == null) listaOutrasPartes = new ArrayList();
		this.listaOutrasPartes.add(parteDt);
	}

	public String getOabNumero() {
		return oabNumero;
	}

	public void setOabNumero(String oabNumero) {
		if (oabNumero != null) {
			this.oabNumero = oabNumero;

		}
	}

	public String getOabComplemento() {
		return oabComplemento;
	}

	public void setOabComplemento(String oabComplemento) {
		if(oabComplemento != null){
			this.oabComplemento = oabComplemento;
		}
	}

	public String getOabUf() {
		return oabUf;
	}

	public void setOabUf(String oabUf) {
		if(oabUf != null){
			this.oabUf = oabUf;
		}
	}
	
	public void setExistePeticaoPendente(boolean existePeticaoPendente) {
		this.existePeticaoPendente = existePeticaoPendente;
	}

	public boolean getExistePeticaoPendente() {
		return existePeticaoPendente;
	}
	
	public void setExisteGuiasPendentes(boolean existeGuiasPendentes) {
		this.existeGuiasPendentes = existeGuiasPendentes;
	}

	public boolean getExisteGuiasPendentes() {
		return existeGuiasPendentes;
	}
	
	public String getMensagemPeticaoPendente(){		
		return "Existe petição neste processo posterior à conclusão.";
	}
	
	public String getMensagemGuiasPendentes(){		
		return "Existe guias pendentes de pagamento neste processo.";
	}

	public void setServentiaCargoRevisorResponsavelDt(ServentiaCargoDt serventiaCargoRevisorResponsavelDt) {
		this.serventiaCargoRevisorResponsavelDt = serventiaCargoRevisorResponsavelDt;
	}

	public ServentiaCargoDt getServentiaCargoRevisorResponsavelDt() {
		return serventiaCargoRevisorResponsavelDt;
	}

	public void setServentiaCargoVogalResponsavelDt(ServentiaCargoDt serventiaCargoVogalResponsavelDt) {
		this.serventiaCargoVogalResponsavelDt = serventiaCargoVogalResponsavelDt;
	}

	public ServentiaCargoDt getServentiaCargoVogalResponsavelDt() {
		return serventiaCargoVogalResponsavelDt;
	}

	public ProcessoCriminalDt getProcessoCriminalDt() {
		return processoCriminalDt;
	}

	public void setProcessoCriminalDt(ProcessoCriminalDt processoCriminalDt) {
		if (processoCriminalDt == null)
			this.processoCriminalDt = new ProcessoCriminalDt();
		else
			this.processoCriminalDt = processoCriminalDt;
	}

	public void setPrimeiroPromovente(String primeiroPromovente) {
		ProcessoParteDt parteDt = ProcessoParteDt.obtenhaProcessoParte(primeiroPromovente);
		if (parteDt != null){
			if (listaPolosAtivos != null) listaPolosAtivos.clear();			
			this.addListaPoloAtivo(parteDt);
		}					
	}
	
	public void setPrimeiroPromovido(String primeiroPromovido) {
		ProcessoParteDt parteDt = ProcessoParteDt.obtenhaProcessoParte(primeiroPromovido);
		if (parteDt != null){
			if (listaPolosPassivos != null) listaPolosPassivos.clear();			
			this.addListaPolosPassivos(parteDt);
		}		
	}
	
	public List getListaPromoventesAtivos() {
		List lista = new ArrayList();
		if (this.listaPolosAtivos != null){
			for(int i = 0; i < this.listaPolosAtivos.size(); i++)
			{
				ProcessoParteDt processoParteDt = (ProcessoParteDt)this.listaPolosAtivos.get(i);
				if (processoParteDt.getDataBaixa().length() == 0) lista.add(processoParteDt);
			}
		}
		return lista;
	}
	
	public List getListaPromovidosAtivos() {
		List lista = new ArrayList();
		if (this.listaPolosPassivos != null){
			for(int i = 0; i < this.listaPolosPassivos.size(); i++)
			{
				ProcessoParteDt processoParteDt = (ProcessoParteDt)this.listaPolosPassivos.get(i);
				if (processoParteDt.getDataBaixa().length() == 0) lista.add(processoParteDt);
			}
		}
		return lista;
	}
	
	public String isMaior60Anos(){
		String stTemp = "NAO";
		if (getProcessoPrioridadeCodigo() != null && !getProcessoPrioridadeCodigo().equals(""))
			if ( Funcoes.StringToInt(getProcessoPrioridadeCodigo())==ProcessoPrioridadeDt.MAIOR_60_ANOS)
				stTemp =  "SIM";
		
		return stTemp;
		
	}
	
	public String isMaior80Anos(){
		String stTemp = "NAO";
		if (getProcessoPrioridadeCodigo() != null && !getProcessoPrioridadeCodigo().equals(""))
			if (Funcoes.StringToInt(getProcessoPrioridadeCodigo()) == ProcessoPrioridadeDt.MAIOR_80_ANOS)
				stTemp =  "SIM";
		
		return stTemp;
		
	}
	
	public String isPessoaComDeficiencia(){
		String stTemp = "NAO";
		if (getProcessoPrioridadeCodigo() != null && !getProcessoPrioridadeCodigo().equals(""))
			if (Funcoes.StringToInt(getProcessoPrioridadeCodigo()) == ProcessoPrioridadeDt.PESSOA_COM_DEFICIENCIA)
				stTemp =  "SIM";
		
		return stTemp;
		
	}
	
	/*public String isSegredoJustica(){
		String stTemp = "NAO";
		if (getSegredoJustica() != null && getSegredoJustica().equals("true"))
			stTemp =  "SIM";
		
		return stTemp;
	}*/
	
	public boolean isDependente() {
		if(getId_ProcessoPrincipal() != null && !getId_ProcessoPrincipal().equalsIgnoreCase(""))
			return true;
		else
			return false;
	}
	
	public String getIpComputadorLogCriminal() {
		if(getProcessoCriminalDt() != null) {
			return getProcessoCriminalDt().getIpComputadorLog();
		} else {
			return "";
		}
	}
	
	public void setIpComputadorLogCriminal(String valor) {
		if (valor != null && getProcessoCriminalDt() != null) {
			 getProcessoCriminalDt().setIpComputadorLog(valor);	
		}
	}
	
	public String getId_UsuarioLogCriminal() {
		if(getProcessoCriminalDt() != null) {
			return getProcessoCriminalDt().getId_UsuarioLog();
		} else {
			return "";
		}
	}   
	
    public void setId_UsuarioLogCriminal(String valor) {
    	if (valor != null && getProcessoCriminalDt() != null) {
			 getProcessoCriminalDt().setId_UsuarioLog(valor);
		}
    }
    
	public String getId_ProcessoCriminal() {
		if(getProcessoCriminalDt() != null) {
			return getProcessoCriminalDt().getId_Processo();
		} else {
			return "";
		}
	}   
	
    public void setId_ProcessoCriminal(String valor) {
    	if (valor != null && getProcessoCriminalDt() != null) {
			 getProcessoCriminalDt().setId_Processo(valor);
		}
    }
	
	public String getPropriedades(){
		return "[Id_Processo:" + this.getId_Processo() + ";ProcessoNumero:" + this.getProcessoNumero() + ";DigitoVerificador:" + this.getDigitoVerificador() + ";Id_ProcessoPrincipal:" + this.getId_ProcessoPrincipal() + ";ProcessoNumeroPrincipal:" + this.getProcessoNumeroPrincipal() + ";Id_ProcessoTipo:" + this.getId_ProcessoTipo() + ";ProcessoTipo:" + this.getProcessoTipo() + ";Id_ProcessoFase:" + this.getId_ProcessoFase() + ";ProcessoFase:" + this.getProcessoFase() + ";Id_ProcessoStatus:" + this.getId_ProcessoStatus() + ";ProcessoStatus:" + this.getProcessoStatus() + ";Id_ProcessoPrioridade:" + this.getId_ProcessoPrioridade() + ";ProcessoPrioridade:" + this.getProcessoPrioridade() + ";Id_Serventia:" + this.getId_Serventia() + ";Serventia:" + this.getServentia() + ";Id_ServentiaOrigem:" + this.getId_ServentiaOrigem() + ";ServentiaOrigem:" + this.getServentiaOrigem() + ";Id_Area:" + this.getId_Area() + ";Area:" + this.getArea() + ";Id_ObjetoPedido:" + this.getId_ObjetoPedido() + ";ObjetoPedido:" + this.getObjetoPedido() + ";Id_Classificador:" + this.getId_Classificador() + ";Classificador:" + this.getClassificador() + ";SegredoJustica:" + this.getSegredoJustica() + ";ProcessoDiretorio:" + this.getProcessoDiretorio() + ";TcoNumero:" + this.getTcoNumero() + ";Valor:" + this.getValor() + ";DataRecebimento:" + this.getDataRecebimento() + ";DataArquivamento:" + this.getDataArquivamento() + ";DataPrescricao:" + this.getDataPrescricao() + ";Apenso:" + this.getApenso() + ";Ano:" + this.getAno() + ";ForumCodigo:" + this.getForumCodigo() + ";CodigoTemp:" + this.getCodigoTemp() + ";Id_AreaDistribuicao:" + this.getId_AreaDistribuicao() + ";ProcessoTipoCodigo:" + this.getProcessoTipoCodigo() + ";ProcessoFaseCodigo:" + this.getProcessoFaseCodigo() + ";ProcessoStatusCodigo:" + this.getProcessoStatusCodigo() + ";ProcessoPrioridadeCodigo:" + this.getProcessoPrioridadeCodigo() + ";ServentiaCodigo:" + this.getServentiaCodigo() + ";ServentiaOrigemCodigo:" + this.getServentiaOrigemCodigo() + ";AreaCodigo:" + this.getAreaCodigo() + ";ObjetoPedidoCodigo:" + this.getObjetoPedidoCodigo() + ";EfeitoSuspensivo:" + this.getEfeitoSuspensivo() + ";Penhora:" + this.getPenhora() + ";DataTransitoJulgado:" + this.getDataTransitoJulgado() + ";Julgado2Grau:" + this.getJulgado2Grau() + ";ValorCondenacao:" + this.getValorCondenacao() + ";Id_Custa_Tipo:" + this.getId_Custa_Tipo() + ";TabelaOrigemTemp:" + this.getTabelaOrigemTemp() + ";Digital100:" + (this.is100Digital()?"1":"0") + "]";
	}
	
	
	public boolean isPrioridade(){
		
		int prioridade =Funcoes.StringToInt(getProcessoPrioridadeCodigo(),-1);

		if (prioridade == ProcessoPrioridadeDt.LIMINAR ||prioridade == ProcessoPrioridadeDt.ANTECIPACAO_TUTELA ||prioridade == ProcessoPrioridadeDt.MAIOR_80_ANOS ||prioridade == ProcessoPrioridadeDt.MAIOR_60_ANOS ||prioridade == ProcessoPrioridadeDt.DOENCA_GRAVE || prioridade == ProcessoPrioridadeDt.REU_PRESO || prioridade == ProcessoPrioridadeDt.DESTITUICAO_PODER_FAMILIAR || prioridade == ProcessoPrioridadeDt.PESSOA_COM_DEFICIENCIA){ 
			return true;
		}
		
		return false;
	}
	
	public boolean isLiminar(){
		int prioridade =Funcoes.StringToInt(getProcessoPrioridadeCodigo(),-1);
		if (prioridade == ProcessoPrioridadeDt.LIMINAR){ 
			return true;
		}
		
		return false;
	}

	public boolean isAntecipacaoTutela(){
		int prioridade =Funcoes.StringToInt(getProcessoPrioridadeCodigo(),-1);
		if (prioridade == ProcessoPrioridadeDt.ANTECIPACAO_TUTELA){ 
			return true;
		}
		return false;
	}
	
	public boolean isCriminal(){
		if (getAreaCodigo() != null && getAreaCodigo().equals(AreaDt.CRIMINAL)){
			return true;			
		}
		return false;
	}
		
	public boolean temExecucaoPenal(){
		if ( getProcessoTipoCodigo() != null 
				&& Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.EXECUCAO_DA_PENA
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA){
			return true;
		}
		return false;
	}
	
	public String getCustaTipo(){
		String stTemp = "";
		if(getId_Custa_Tipo() != null){
			if (getId_Custa_Tipo().equalsIgnoreCase("1")){
				stTemp= "Com Custas";
			}else if(getId_Custa_Tipo().equalsIgnoreCase("2")){
				stTemp="Assist. Judiciária";
			}else if(getId_Custa_Tipo().equalsIgnoreCase("3")){
				stTemp="Isento";
			}
		}
		
		return stTemp;
	}
	
	public boolean isSegredoJustica(){		
		if (getSegredoJustica() != null && getSegredoJustica().equals("true")) {
			return  true;
		} else{
			return false;
		}	
	}
	
	public String mostrarSegredoJustica(){
		 if (getSegredoJustica() != null && getSegredoJustica().equalsIgnoreCase("true") ){
			 return "Sim";
		 } else {
			 return  "Não";
		 }
	}
	
	public String getProcessoNumeroAntigoTemp() {
		return processoNumeroAntigoTemp;
	}

	public void setProcessoNumeroAntigoTemp(String processoNumeroAntigoTemp) {
		this.processoNumeroAntigoTemp = processoNumeroAntigoTemp;
	}

	public String getTabelaOrigemTemp() {
		if (tabelaOrigemTemp == null) return "";
		return tabelaOrigemTemp;
	}

	public void setTabelaOrigemTemp(String tabelaOrigemTemp) {
		this.tabelaOrigemTemp = tabelaOrigemTemp;
	}
	
	public boolean isProcessoImportadoSPG() {
		return this.processoNumeroAntigoTemp != null && ((this.tabelaOrigemTemp != null && this.tabelaOrigemTemp.trim().equalsIgnoreCase("SPG")) || (this.dataDigitalizacao != null && this.dataDigitalizacao.trim().length() > 0));
	}
	
	public boolean isGuiaInicialSPG() {
		return this.tabelaOrigemTemp != null && this.tabelaOrigemTemp.trim().equalsIgnoreCase("GUIAINICIALSPG");
	}	
	
	public boolean temId_Processo(){
		if (this.getId_Processo() != null && this.getId_Processo().length() > 0) {
			return true;	
		}
		return false;
	}

	public String getDescricaoPoloAtivo() {
		if (descricaoPoloAtivo == null || descricaoPoloAtivo.trim().length() == 0) return "Promovente";
		return descricaoPoloAtivo;
	}

	public void setDescricaoPoloAtivo(String descricaoPoloAtivo) {
		this.descricaoPoloAtivo = descricaoPoloAtivo;
	}

	public String getDescricaoPoloPassivo() {
		if (descricaoPoloPassivo == null || descricaoPoloPassivo.trim().length() == 0) return "Promovido";
		return descricaoPoloPassivo;
	}

	public void setDescricaoPoloPassivo(String descricaoPoloPassivo) {
		this.descricaoPoloPassivo = descricaoPoloPassivo;
	}

	public boolean isSegundoGrau() {
		if (Funcoes.StringToInt(getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU) return true;
		return false;
	}
	
	public boolean isTurmaRecursal() {
		return ServentiaSubtipoDt.isTurma(getServentiaSubTipoCodigo());
	}

	public boolean temNumero() {
		if (this.getProcessoNumeroSimples() != null && this.getProcessoNumeroSimples().length() > 0){
			return true;
		}		 
		return false;
	}
	
	public void setProcessoNumero(String valor ) {
		if (valor!=null) {
			if (Funcoes.StringToInt(valor.replaceAll("[-.,]", ""),-3)!=-3){
				super.setProcessoNumero(valor.replaceAll("[-,]", ""));
			}
		}
	}
	
	public boolean isProcessoArquivado() {
		if (getId_ProcessoStatus() != null && !getId_ProcessoStatus().isEmpty() && Funcoes.StringToInt(getId_ProcessoStatus()) == ProcessoDt.ARQUIVADO) 
			return true;
		
		return false;
	}
	
	public boolean isProcessoErroMigracao() {
		if (getId_ProcessoStatus() != null && !getId_ProcessoStatus().isEmpty() && Funcoes.StringToInt(getId_ProcessoStatus()) == ProcessoDt.ERRO_MIGRACAO) 
			return true;
		
		return false;
	}
	
	public boolean isPodeMostrarSegredoJustica(String serventiaUsuario, boolean desembargador) {
		//conform ata 11º Reunião (Extraordinária) – Comissão de Informatização (05/02/2018)
		// a ata esta salva na documentação do sistema Ata da 11ª Reunião Extraordinária - Comissão de Informatização.pdf
		if (desembargador){
			return true;
		}else if (!isSegredoJustica()){ 
			return true;
		}else if (  serventiaUsuario.equals(getId_Serventia())  ){
			return true;
		}
		
		return false;
	}	

	public String getDataDigitalizacao() {
		return dataDigitalizacao;
	}

	public void setDataDigitalizacao(String dataDigitalizacao) {
		this.dataDigitalizacao = dataDigitalizacao;
	}

	public boolean isApenso() {
		String apenso = getApenso();
		if (apenso != null && (apenso.equalsIgnoreCase("true") || apenso.equals("1"))){
			return true;
		}
		return false;
	}

	public boolean isCivel() {
		 if ( getAreaCodigo()!=null && getAreaCodigo().equals(AreaDt.CIVEL))
			 return true;
		return false;
	}

	
	/*
	 * Jesus Retorna a primeira movimentação da lista testa null e tamanho
	 */
	public MovimentacaoDt getPrimeiraMovimentacaoLista() {
		MovimentacaoDt movi = null;
		if (listaMovimentacoes != null && listaMovimentacoes.size() > 0) {
			movi = (MovimentacaoDt) listaMovimentacoes.get(0);
		}
		return movi;
	}

	public int getTamanhoListaMovimentacoes() {
		if (listaMovimentacoes != null)
			return listaMovimentacoes.size();
		return 0;
	}

	public List getListaMovimentacoes() {
		return listaMovimentacoes;
	}
	
	public List getListaMovimentacoesFisico() {
		return listaMovimentacoesFisico;
	}

	public void setListaMovimentacoes(List listaMovimentacoes) {
		this.listaMovimentacoes = listaMovimentacoes;
	}
	
	public void setListaMovimentacoesFisico(List listaMovimentacoesFisico) {
		this.listaMovimentacoesFisico = listaMovimentacoesFisico;
	}


	public List getListaAdvogados() {
		return listaAdvogados;
	}

	public void setListaAdvogados(List listaAdvogados) {
		this.listaAdvogados = listaAdvogados;
	}

	public String getId_Assunto() {
		return id_Assunto;
	}

	public void setId_Assunto(String id_Assunto) {
		if (id_Assunto != null)
			if (id_Assunto.equalsIgnoreCase("null")) {
				this.id_Assunto = "";
				this.assunto = "";
			} else if (!id_Assunto.equalsIgnoreCase(""))
				this.id_Assunto = id_Assunto;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		if (assunto != null)
			if (assunto.equalsIgnoreCase("null"))
				this.assunto = "";
			else if (!assunto.equalsIgnoreCase(""))
				this.assunto = assunto;
	}

	public ProcessoDt getProcessoDependenteDt() {
		return processoDependenteDt;
	}

	public void setProcessoDependenteDt(ProcessoDt processoDependenteDt) {
		this.processoDependenteDt = processoDependenteDt;
	}

	public boolean temApensos() {
		return temApensos;
	}

	public void setTemApensos(boolean temApensos) {
		this.temApensos = temApensos;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		if (comarca != null)
			this.comarca = comarca;
	}

	public String getServentiaSubTipo() {
		return serventiaSubTipo;
	}

	public void setServentiaSubTipo(String serventiaSubTipo) {
		if (serventiaSubTipo != null)
			this.serventiaSubTipo = serventiaSubTipo;
	}

	public String getComarcaCodigo() {
		return comarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		if (comarcaCodigo != null)
			this.comarcaCodigo = comarcaCodigo;
	}

	public String getServentiaSubTipoCodigo() {
		return serventiaSubTipoCodigo;
	}

	public void setServentiaSubTipoCodigo(String serventiaSubTipoCodigo) {
		if (serventiaSubTipoCodigo != null)
			this.serventiaSubTipoCodigo = serventiaSubTipoCodigo;
	}
	
	public String getUsuarioServentia() {
		return usuarioServentia;
	}

	public void setUsuarioServentia(String usuarioServentia) {
		this.usuarioServentia = usuarioServentia;
	}

	public String getId_UsuarioServentia() {
		return id_UsuarioServentia;
	}

	public void setId_UsuarioServentia(String idUsuarioServentia) {
		this.id_UsuarioServentia = idUsuarioServentia;
	}

	public boolean isProcessoDependente() {
		if (this.getId_ProcessoPrincipal() != null && this.getId_ProcessoPrincipal().length() > 0) {
			return true;
		}

		return false;
	}

	public boolean isMesmaServentia(String id_Serventia) {
		if (getId_Serventia()!=null && getId_Serventia().equals(id_Serventia)){
			return true;
		}
		return false;
	}
		
	public boolean temLiminar(){
		if (ProcessoPrioridadeDt.LIMINAR == Funcoes.StringToInt(getProcessoPrioridadeCodigo())  ){
			return true;
		}
		return false;
	}
	public boolean temAntecipacaoTutela(){
		if (ProcessoPrioridadeDt.ANTECIPACAO_TUTELA == Funcoes.StringToInt(getProcessoPrioridadeCodigo())  ){
			return true;
		}
		return false;
	}
	
	public String getProcessoPrioridadeCodigoTexto(){
		int prioridade = Funcoes.StringToInt(getProcessoPrioridadeCodigo());
		
		if (prioridade == ProcessoPrioridadeDt.MAIOR_80_ANOS){				
			return  "Maior de 80 anos";
		}else if (prioridade == ProcessoPrioridadeDt.MAIOR_60_ANOS){				
			return  "Maior de 60 anos";
		}else if (prioridade == ProcessoPrioridadeDt.DOENCA_GRAVE){				
			return  "Doença Grave";
		}else if (prioridade == ProcessoPrioridadeDt.DESTITUICAO_PODER_FAMILIAR){				
			return  "Destituição do Poder Familiar";
		}else if (prioridade == ProcessoPrioridadeDt.PESSOA_COM_DEFICIENCIA){				
			return  "Pessoa com Deficiência";
		}else if (prioridade == ProcessoPrioridadeDt.LIMINAR){				
			return  "Pedido de Liminar";
		}else if (prioridade == ProcessoPrioridadeDt.ANTECIPACAO_TUTELA){				
			return  "Antecipação de Tutela";
		}else if (prioridade == ProcessoPrioridadeDt.REU_PRESO){				
			return  "Réu Preso";
		}else if (prioridade == ProcessoPrioridadeDt.NORMAL){				
			return  "Normal";
		}
		return "";		
	}

	public boolean isSigiloso() {
		if (Funcoes.StringToLong(getProcessoStatusCodigo(),-1)==ProcessoStatusDt.SIGILOSO 
				|| (getSigiloso()!= null && getSigiloso().equalsIgnoreCase("true"))){
			//a segunda validação do if foi incluída devido a forma como os dados vêm no cadastro de processo
			return true;
		}
		return false;
	}
	
	
	public boolean isCalculo() {
		if (Funcoes.StringToLong(getProcessoStatusCodigo(),-1)==ProcessoStatusDt.CALCULO){
			return true;
		}
		return false;
	}
	
	public boolean isAtivo(){
		if (Funcoes.StringToLong(getProcessoStatusCodigo(),-1)==ProcessoStatusDt.ATIVO){
			return true;
		}
		return false;
	}
	
	public String getNumeroImagemPrioridade(){
		String stTemp="0";
		switch (getProcessoPrioridadeCodigo()) {
			case "1":
			case "2":
			case "3":
			case "4":
			case "8":
			case "9":
				stTemp="3";
				break;
			case "5":
			case "6":
				stTemp="4";
			break;

		default:
			stTemp="0";
		}
		return stTemp;
	}

	public boolean isArquivado() {
		if (Funcoes.StringToLong(getProcessoStatusCodigo(),-1)==ProcessoStatusDt.ARQUIVADO){
			return true;
		}
		return false;
	}
	public boolean isArquivadoProvisoriamente() {
		if (Funcoes.StringToLong(getProcessoStatusCodigo(),-1)==ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE){
			return true;
		}
		return false;
	}
	public boolean isErroMigracao() {
		if (Funcoes.StringToLong(getProcessoStatusCodigo(),-1)==ProcessoStatusDt.ERRO_MIGRACAO){
			return true;
		}
		return false;
	}
	
	public boolean isCalculoLiquidacao() {
		if (Funcoes.StringToLong(getProcessoTipoCodigo(),-1)==ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA){
			return true;
		}
		return false;
	}

	public String getOutraDescricaoGenerica() {
		return outraDescricaoGenerica;
	}

	public void setOutraDescricaoGenerica(String outraDescricaoGenerica) {
		this.outraDescricaoGenerica = outraDescricaoGenerica;
	}
	
	public void limparSegredoJustica() {
		/* Campos comentados são os únicos que podem aparecer na consulta pública */
//		setId_Processo("");
//		setProcessoNumero("");
//		setId_ProcessoPrincipal("");
		setProcessoNumeroPrincipal("");
//		setId_ProcessoTipo("");
		setProcessoTipo("");
//		setId_ProcessoFase("");
		setProcessoFase("");
//		setId_ProcessoStatus("");
		setProcessoStatus("");
//		setId_ProcessoPrioridade("");
		setProcessoPrioridade("");
//		setId_Serventia("");
//		setServentia("");
//		setId_ServentiaOrigem("");
		setServentiaOrigem("");
//		setId_Area("");
//		setArea("");
//		setId_ObjetoPedido("");
		setObjetoPedido("");
//		setId_Classificador("");
		setClassificador("");
//		setSegredoJustica("");
		setProcessoDiretorio("");
		setTcoNumero("");
		setValor("");
		setDataRecebimento("");
		setDataArquivamento("");
		setApenso("");
//		setAno("");
//		setForumCodigo("");
//		setDigitoVerificador("");
//		setId_AreaDistribuicao("");
		setProcessoTipoCodigo("");
		setProcessoFaseCodigo("");
//		setProcessoStatusCodigo("");
		setProcessoPrioridadeCodigo("");
		setServentiaCodigo("");
		setServentiaOrigemCodigo("");
		setAreaCodigo("");
		setObjetoPedidoCodigo("");
		setEfeitoSuspensivo("");
		setPenhora("");
		setDataTransitoJulgado("");
		setJulgado2Grau("");
		setValorCondenacao("");
//		setId_Custa_Tipo("");
		poloAtivo = "";
		poloPassivo = "";
		cpfCnpjParte = "";
//		id_Recurso = "";
		areaDistribuicao = "";
//		Id_ServentiaOrigemRecurso = "";
		ServentiaOrigemRecurso = "";
//		Id_AreaDistribuicaoRecursal = ""; 
		recursoDt = null;
//		listaPromoventes = null;
//		listaPromovidos = null;
//		listaOutrasPartes = null;
		listaProcessoParteAdvogado = null;
		listaAssuntos = null;
//		serventiaTipoCodigo = "";
		serventiaCargoResponsavelDt = null;
		oabNumero = "";
		oabComplemento = "";
		oabUf = "";
		situacaoAdvogadoProcesso = "";
		existePeticaoPendente = false;
		existeGuiasPendentes = false;
		serventiaCargoRevisorResponsavelDt = null;
		serventiaCargoVogalResponsavelDt = null;
		processoCriminalDt = null;
		processoNumeroAntigoTemp = null;
		tabelaOrigemTemp = null;
		descricaoPoloAtivo = "";
		descricaoPoloPassivo = "";
		dataDigitalizacao = "";
//		id_Assunto = "";
		assunto = "";
		comarcaCodigo = "";
		comarca = "";
		serventiaSubTipo = "";
		serventiaSubTipoCodigo = "";
		listaAdvogados = null;
		listaMovimentacoes = null;
		processoDependenteDt = null;
		temApensos = false;
		usuarioServentia = "";
//		id_UsuarioServentia = "";
		outraDescricaoGenerica = "";
		setHash("");
		for (int j = 0; j < getPartesProcesso().size(); j++) {
			ProcessoParteDt parteDt = (ProcessoParteDt) getPartesProcesso().get(j);
			String nomeParteSimplicado = Funcoes.iniciaisNome(parteDt.getNome());
			parteDt.limpar();
			parteDt.setNome(nomeParteSimplicado);
			
		}
	}
	public String getProcessoFisicoTipo() {
		return processoFisicoTipo;
	}
	public void setProcessoFisicoTipo(String processoFisicoTipo) {
		this.processoFisicoTipo = processoFisicoTipo;
	}
	public String getProcessoFisicoNumero() {
		return processoFisicoNumero;
	}
	public void setProcessoFisicoNumero(String processoFisicoNumero) {
		this.processoFisicoNumero = processoFisicoNumero;
	}
	public String getProcessoFisicoComarcaNome() {
		return processoFisicoComarcaNome;
	}
	public void setProcessoFisicoComarcaNome(String processoFisicoComarcaNome) {
		this.processoFisicoComarcaNome = processoFisicoComarcaNome;
	}
	public String getProcessoFisicoComarcaCodigo() {
		return processoFisicoComarcaCodigo;
	}
	public void setProcessoFisicoComarcaCodigo(String processoFisicoComarcaCodigo) {
		this.processoFisicoComarcaCodigo = processoFisicoComarcaCodigo;
	}
	public String getLocalizador() {
		if (this.Localizador == null || this.Localizador.equals("Null")){
			return "";
		} else{
			return Localizador;
		}
		
	}
	public void setLocalizador(String localizador) {
		Localizador = localizador;
	}
	
	public boolean isProcessoHibrido() {
		if (MISTO_SPG.equals(processoFisicoTipo) || MISTO_SSG.equals(processoFisicoTipo)) {
			return true;
		}
		return false;
	}
	
	public boolean isProcessoJaFoiHibrido() {
		if (MISTO_SPG_PENDENTE_LIMPEZA_SPG.equals(processoFisicoTipo) || MISTO_SPG_CONCLUIDA_LIMPEZA_SPG.equals(processoFisicoTipo)) {
			return true;
		}
		return false;
	}
	 
	public boolean isComCusta() {
		if (this.Id_Custa_Tipo != null && this.Id_Custa_Tipo.length()>0 && this.Id_Custa_Tipo.equalsIgnoreCase(String.valueOf(COM_CUSTAS)) ) {
			return true;
		}
		return false;
	}
	public String getIdPrimeiroPoloPassivo() {
		if(listaPolosPassivos != null && listaPolosPassivos.size() >0) {
			return ((ProcessoParteDt)listaPolosPassivos.get(0)).getId();
			
		}
		return null;
	}

	public ProcessoParteDt getOutraParte(int posicao) throws MensagemException {
		if (listaOutrasPartes!=null && listaOutrasPartes.size()>posicao){
			return (ProcessoParteDt) listaOutrasPartes.get(posicao);
		}
		throw new MensagemException("Não foi possível localizar a outra parte, volte ao início e tente novamente.");
	}
	
	public boolean isExecucaoPenal() {
		if ((Funcoes.StringToInt(getProcessoTipoCodigo())) == ProcessoTipoDt.EXECUCAO_DA_PENA
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.AGRAVO_EXECUCAO_PENAL
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA_CPP
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.CONFLITO_COMPETENCIA_PRIMEIRO_GRAU
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.CONFLITO_COMPETENCIA
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.HABEAS_CORPUS
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.INCIDENTE_SANIDADE_MENTAL
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.OUTROS_INCIDENTES_EXECUCAO_INCICIADOS_OFICIO
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.OUTROS_INCIDENTES_EXECUCAO_INCICIADOS_OFICIO_2
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.ROTEIRO_PENA
				|| Funcoes.StringToInt(getProcessoTipoCodigo()) == ProcessoTipoDt.TRANSFERENCIA_ENTRE_ESTABELECIMENTOS_PENAIS
				){
			return true;
		}
		return false;
	}
	
	public boolean isAcordoNaoPersecucaoPenal() {
		if (getListaAssuntos() != null) {
			for (int i = 0; i < getListaAssuntos().size(); i++) {
				ProcessoAssuntoDt procAssuntoDt = (ProcessoAssuntoDt)getListaAssuntos().get(i);
				if (procAssuntoDt.getAssuntoCodigo().equalsIgnoreCase(String.valueOf(AssuntoDt.ACORDO_NAO_PERSECUCAO_PENAL))) {
					return true;
				}
			}
		}		
		return false;
	}
	
	public void setProcessoPrecatoriaExpedidaOnline(boolean processoPrecatoriaExpedidaOnline) {
		this.processoPrecatoriaExpedidaOnline = processoPrecatoriaExpedidaOnline;
	}
	
	public boolean isProcessoPrecatoriaExpedidaOnline() {
		return processoPrecatoriaExpedidaOnline;
	}
	
	public boolean isProcessoTipoPrecatorias() {
		boolean retorno = false;
		if( getProcessoTipoCodigo() != null && !getProcessoTipoCodigo().isEmpty() ) {
			switch( Funcoes.StringToInt(getProcessoTipoCodigo()) ) {
				case ProcessoTipoDt.CARTA_PRECATORIA:
				case ProcessoTipoDt.CARTA_PRECATORIA_CPC :
				case ProcessoTipoDt.CARTA_PRECATORIA_CPP :
				case ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE:
				case ProcessoTipoDt.CARTA_PRECATORIA_INFRACIONAL:{
					retorno = true;
					break;
				}
			}
		}
		return retorno;
	}
	
	public String getSituacaoAdvogadoProcesso() {
		return situacaoAdvogadoProcesso;
	}
	public void setSituacaoAdvogadoProcesso(String situacaoAdvogadoProcesso) {
		this.situacaoAdvogadoProcesso = situacaoAdvogadoProcesso;
	}
	
	public boolean temEfeitoSuspensivo() {
		return getEfeitoSuspensivo() != null && Funcoes.StringToBoolean(getEfeitoSuspensivo());
	}
	public boolean temPenhora() {
		 return getPenhora() != null && Funcoes.StringToBoolean(getPenhora());
	}
	
	public boolean isJulgado2Grau() {
		return getJulgado2Grau()!=null &&  Funcoes.StringToBoolean(getJulgado2Grau()); 
	}
	public boolean temPrescricao() {
		 return getDataPrescricao() != null && getDataPrescricao().length()>0;
	}
	public boolean temValor() {
		return  getValor() != null && getValor().length()>0;
	}
	public boolean temLocalizador() {
		return getLocalizador()!=null &&  getLocalizador().length()>0;
	}
	public boolean temRecurso() {
		return getRecursoDt()!=null && getRecursoDt().getId()!=null;
	}
	public void setNomeParteBusca(String parte) {
		NomeParteBusca = parte;		
	}
	public String getNomeParteBusca() {
		return NomeParteBusca;
	}
	public boolean isBuscaParte() {
		return (NomeParteBusca !=null && !NomeParteBusca.isEmpty());
	}
	public void setParteTipoBusca(String parteTipoBusca) {
		ParteTipoBusca  = parteTipoBusca;
		
	}
	public String getParteTipoBusca() {
		return ParteTipoBusca;
	}
	public void setId_ParteTipoBusca(String id_ParteTipoBusca) {
		Id_ParteTipoBusca = id_ParteTipoBusca;		
	}
	public String getParteTipoCodigoBusca() {
		return ParteTipoCodigoBusca;
	}
	public void setParteTipoCodigoBusca(String id_ParteTipoCodigoBusca) {
		ParteTipoCodigoBusca = id_ParteTipoCodigoBusca;		
	}	
	public String getId_ParteTipoBusca() {
		return Id_ParteTipoBusca ;		
	}
	public boolean isMesmaParteBusca(String id_parte) {
		if (Id_ParteTipoBusca==null) {
			return false;
		}
		return Id_ParteTipoBusca.equals(id_parte);
	}
	
	public String getNomeParteBuscaAbreviado() {
		if (Funcoes.StringToInt(ParteTipoCodigoBusca,-999)==ProcessoParteTipoDt.VITIMA) {
			return Funcoes.iniciaisNome(NomeParteBusca);
		}
		return NomeParteBusca;
	}
	
	public boolean hasVitima() {
		if (this.listaOutrasPartes != null) {
			for (int j = 0; j < this.listaOutrasPartes.size(); j++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) this.listaOutrasPartes.get(j);
				if (parteDt.isVitima()) return true;
			}
		}		
		return false;
	}
	
	public String getIdCNJClasse()  {return IdCNJClasse;}
	public void setIdCNJClasse(String valor ) {if (valor!=null) IdCNJClasse = valor;}
	
	public String getClasseCNJ() {
    	String classeToString = "";
    	if (IdCNJClasse != null && IdCNJClasse.trim().length() > 0) classeToString += this.getIdCNJClasse().trim() + " - ";
    	classeToString += this.getProcessoTipo();
    	return classeToString;
    }
	public List getListaVitimas() {
		List tempList = null;		
		for (int i=0; listaOutrasPartes!=null && i<listaOutrasPartes.size(); i++) {
			ProcessoParteDt parte = (ProcessoParteDt) listaOutrasPartes.get(i);
			if (parte!=null && parte.isVitima()) {
				if (tempList==null) {
					tempList = new ArrayList();
				}
				tempList.add(parte);				
			}
		}
		return tempList;
	}
	
	public boolean isCertificouNaoInconsistencia() {
		if(processoCriminalDt==null) {
			return false;
		}
		if (processoCriminalDt.getIdUsuCertificaInconsistencias() != null ) {
			return true;
		}
		
		return false;		
				
	}
	public String getCertidao() {
		String stTemp = "Certifico e dou fé que os campos acima foram deixados em branco em razão de inexistirem dentro do processo.";
		if(processoCriminalDt!=null && processoCriminalDt.getNomeUsuCertificaInconsistencias()!=null) {			
			stTemp = "O usuário " + processoCriminalDt.getNomeUsuCertificaInconsistencias() +  " certificou e deu fé que os campos acima foram deixados em branco em razão de inexistirem dentro do processo.";
		}
		return stTemp;
	}
	
	public boolean is100Digital() {
		return bo100Digital ;
	}
	
	public void set100Digital(boolean valor) {
		bo100Digital = valor;
	}
	
	public String getClassCorProcesso() {
		if(isProcessoHibrido()) {
			return "field_processo_misto";
		}else if(is100Digital()) {
			return "field_processo_100Digital";
		}
		return "field_processo";		
	}
}
