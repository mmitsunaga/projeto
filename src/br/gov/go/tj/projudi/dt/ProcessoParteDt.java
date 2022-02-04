package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class ProcessoParteDt extends ProcessoParteDtGen {

	private static final long serialVersionUID = 1583589801884545413L;

	public static final int CodigoPermissao = 101;

	private EnderecoDt enderecoParte;
	private LocomocaoDt locomocaoDt;
	private String siglaOrgaoExpedidor;
	private boolean parteNaoPersonificavel;
	private List listaAdvogados;
	private List listaAlcunhaParte;
	private List listaSinalParte;
	private String Id_Usuario;
	private String Id_Alcunha;
	private String Alcunha;
	private String Id_Sinal;
	private String Sinal;
	private String NomeSimplificado;
	private String culpado;
	private boolean checkbox;
	private boolean parteEnderecoDesconhecido;
	private boolean parteLocomocoes;
	private String Idade;
	private String excluido;	
	private String siglaCtpsUf;
	private Integer ordemParte; // jvosantos - 12/06/2019 11:10 - Adicionar Ordem da Parte
	public static final int EXCLUIDO = 1;

	public static final int  ID_POLO_ATIVO = 2;
	public static final int  ID_POLO_PASSIVO = 3;
	
	private boolean isenta;
	private String DataSentenca;
	private String DataPronuncia;

	private boolean ReuPreso = false;

	public ProcessoParteDt() {
		limpar();
	}

	public String getId_ProcessoParte() {
		return super.getId();
	}

	public void setId_ProcessoParte(String valor) {
		if (valor != null) super.setId(valor);
	}

	public void limpar() {
		super.limpar();
		siglaOrgaoExpedidor = "";
		Id_Usuario = "";
		Id_Alcunha = "";
		Alcunha = "";
		Id_Sinal = "";
		Sinal = "";
		enderecoParte = new EnderecoDt();
		locomocaoDt = new LocomocaoDt();
		listaAlcunhaParte = null;
		listaSinalParte = null;
		listaAdvogados = null;
		parteNaoPersonificavel = false;
		NomeSimplificado = "";
		culpado = "";
		Idade = "";
		setCheckbox(false);
		isenta = false;
		setParteEnderecoDesconhecido("false");
		setParteLocomocoes("false");
		excluido = "";
		DataSentenca = "";
		DataPronuncia = "";
	}

	public EnderecoDt getEnderecoParte() {
		return enderecoParte;
	}

	public void setEnderecoParte(EnderecoDt endereco) {
		this.enderecoParte = endereco;
	}
	
	public LocomocaoDt getLocomocaoDt() {
		return locomocaoDt;
	}

	public void setLocomocaoDt(LocomocaoDt locomocaoDt) {
		this.locomocaoDt = locomocaoDt;
	}
	
	/**
	 * Método que administra a exibição de CPF e CNPJ formatado
	 */
	
	public String getCpfCnpjFormatado() {
		if (!(getCnpj().length() == 0)) return Funcoes.formataCNPJ(getCnpj());
		return Funcoes.formataCPF(getCpf());
	}
	
	
	public String getCpfFormatado() {
		return Funcoes.formataCPF(getCpf());
	}

	/**Método que administra se será exibido CNPJ ou CPF*/
	public String getCpfCnpj() {
		if (getCnpj().length() > 0) return getCnpj();
		return getCpf();
	}

	public String getSiglaOrgaoExpedidor() {
		return siglaOrgaoExpedidor;
	}

	public void setSiglaOrgaoExpedidor(String valor) {
		if (valor != null) siglaOrgaoExpedidor = valor;
	}

	public String getId_Usuario() {
		return Id_Usuario;
	}

	public void setId_Usuario(String id_Usuario) {
		if (id_Usuario != null) Id_Usuario = id_Usuario;
	}

	/**
	 * Retorna  a lista de advogados da parte
	 * @return
	 */
	public List getAdvogados() {
		return listaAdvogados;
	}
	
	/**
	 * Retorna  a lista de advogados da parte 
	 */
	public List setAdvogados(List listaAdvogados) {
		return this.listaAdvogados = listaAdvogados;
	}
	
	/**
	 * Retorna  o primeiro advogado da parte
	 * @return
	 */
	public ProcessoParteAdvogadoDt getAdvogadoDt() {
		if (listaAdvogados != null && listaAdvogados.size() > 0) {return (ProcessoParteAdvogadoDt) listaAdvogados.get(0); }
		return null;
	}

	public void setAdvogadoDt(ProcessoParteAdvogadoDt advogadoDt) {
		if (listaAdvogados == null) listaAdvogados = new ArrayList();
		listaAdvogados.add(advogadoDt);
	}

	public void setOrgaoExpedidor(String sigla, String uf) {
		if (sigla != null && uf != null) setRgOrgaoExpedidor(sigla + "-" + uf);
	}

	public String getId_Alcunha() {
		return Id_Alcunha;
	}

	public void setId_Alcunha(String id_Alcunha) {
		if (id_Alcunha != null) if (id_Alcunha.equalsIgnoreCase("null")) {
			Id_Alcunha = "";
			Alcunha = "";
		} else if (!id_Alcunha.equalsIgnoreCase("")) Id_Alcunha = id_Alcunha;
	}

	public String getAlcunha() {
		return Alcunha;
	}

	public void setAlcunha(String alcunha) {
		if (alcunha != null) if (alcunha.equalsIgnoreCase("null")) Alcunha = "";
		else if (!alcunha.equalsIgnoreCase("")) Alcunha = alcunha;
	}

	public String getId_Sinal() {
		return Id_Sinal;
	}

	public void setId_Sinal(String id_Sinal) {
		if (id_Sinal != null) if (id_Sinal.equalsIgnoreCase("null")) {
			Id_Sinal = "";
			Sinal = "";
		} else if (!id_Sinal.equalsIgnoreCase("")) Id_Sinal = id_Sinal;
	}

	public String getSinal() {
		return Sinal;
	}

	public void setSinal(String sinal) {
		if (sinal != null) if (sinal.equalsIgnoreCase("null")) Sinal = "";
		else if (!sinal.equalsIgnoreCase("")) Sinal = sinal;
	}

	public List getListaAlcunhaParte() {
		return listaAlcunhaParte;
	}

	public void setListaAlcunhaParte(List listaAlcunhaParte) {
		this.listaAlcunhaParte = listaAlcunhaParte;
	}

	public void addListaAlcunhaParte(ProcessoParteAlcunhaDt dt) {
		if (listaAlcunhaParte == null) listaAlcunhaParte = new ArrayList();
		this.listaAlcunhaParte.add(dt);
	}

	public List getListaSinalParte() {
		return listaSinalParte;
	}

	public void setListaSinalParte(List listaSinalParte) {
		this.listaSinalParte = listaSinalParte;
	}

	public void addListaSinalParte(ProcessoParteSinalDt dt) {
		if (listaSinalParte == null) listaSinalParte = new ArrayList();
		this.listaSinalParte.add(dt);
	}

	public boolean ParteNaoPersonificavel() {
		return parteNaoPersonificavel;
	}

	public void setParteNaoPersonificavel(boolean parteNaoPersonificavel) {
		this.parteNaoPersonificavel = parteNaoPersonificavel;
	}

	public List getListaAdvogados() {
		return listaAdvogados;
	}

	public void setListaAdvogados(List listaAdvogados) {
		this.listaAdvogados = listaAdvogados;
	}

	/**
	 * Retorna a informação se parte é Contumaz ou Revel
	 * @return
	 */
	public String getAusenciaProcessoParte() {
		String tipoAusencia = "";
		if (getId_ProcessoParteAusencia() != null && getId_ProcessoParteAusencia().length() > 0){
			if (Funcoes.StringToInt(getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) tipoAusencia = "Contumaz";
			else if (Funcoes.StringToInt(getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) tipoAusencia = "Revel";
		}
		return tipoAusencia;
	}

    public String getNomeSimplificado() {
        return NomeSimplificado;
    }

    public void setNomeSimplificado(String nomeSimplificado) {
        NomeSimplificado = nomeSimplificado;
    }

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public boolean isCheckbox() {
		return checkbox;
	}
	

	public String getCulpado() {
		return culpado;
	}

	public void setCulpado(String culpado) {
		if (culpado != null)
			this.culpado = culpado;
		else
			this.culpado = "";
	}

	public String getExcluido() {
		return excluido;
	}

	public void setExcluido(String excluido) {
		this.excluido = excluido;
	}

	public void setParteEnderecoDesconhecido(String valorParteEnderecoDesconhecido) {
		if (valorParteEnderecoDesconhecido == null || valorParteEnderecoDesconhecido.trim().length() == 0) return;
		this.parteEnderecoDesconhecido = Funcoes.BancoLogicoBoolean(valorParteEnderecoDesconhecido);		
		if (this.parteEnderecoDesconhecido){
			super.setId_Endereco("");
			super.setEndereco("");
			enderecoParte = new EnderecoDt();
			locomocaoDt = new LocomocaoDt();
		}
	}

	public boolean isParteEnderecoDesconhecido() {
		return parteEnderecoDesconhecido;
	}
	
	public void setParteLocomocoes(String valorParteLocomocoes) {
		if (valorParteLocomocoes == null || valorParteLocomocoes.trim().length() == 0) return;
		this.parteLocomocoes= Funcoes.BancoLogicoBoolean(valorParteLocomocoes);		
	}

	public boolean isParteLocomocoes() {
		return parteLocomocoes;
	}
	
	public void setParteIsenta(boolean isenta) {
		this.isenta= isenta;
	}

	public boolean isParteIsenta() {
		return this.isenta;
	}

	public String getIdade() {
		return this.Idade;
	}

	public void setIdade(String idade) {
		if(idade != null)
			this.Idade = idade;
		else
			this.Idade = "";
	}
	
	/**
     * Extrai a parte de uma linha contendo o separador __@--
     * 
     * @author mmgomes
     * @param linhaComposta
     * @return
     */
	public static ProcessoParteDt obtenhaProcessoParte(String linhaComposta) {
		ProcessoParteDt parteDt = null;
		if (linhaComposta != null && linhaComposta.contains("__@--")) {
			String vetorParte[] = linhaComposta.split("__@--");
			if (vetorParte.length >= 3) {
				parteDt = new ProcessoParteDt();
				parteDt.setId(vetorParte[0]);
				parteDt.setNome(vetorParte[1]);
				parteDt.setProcessoParteTipo(vetorParte[2]);
			}
		}
		return parteDt;
	}	
	
	public String getDescricaoListaAlcunha(){
		String retorno = "";
       	if (this.listaAlcunhaParte != null && this.listaAlcunhaParte.size() > 0){
       		for (int i=0; i< listaAlcunhaParte.size(); i++) {
       			ProcessoParteAlcunhaDt alcunha = (ProcessoParteAlcunhaDt) this.listaAlcunhaParte.get(i);
       			retorno += alcunha.getAlcunha();
				if (i < listaAlcunhaParte.size()-1) retorno += ", ";
			}
       	}
		return retorno;
	}
	
	public String getDescricaoListaSinal(){
		String retorno = "";
       	if (this.listaSinalParte != null && this.listaSinalParte.size() > 0){
       		for (int i=0; i< listaSinalParte.size(); i++) {
       			ProcessoParteSinalDt sinal = (ProcessoParteSinalDt) this.listaSinalParte.get(i);
       			retorno += sinal.getSinal();
				if (i < listaSinalParte.size()-1) retorno += ", ";
			}
       	}
		return retorno;
	}
	public boolean isMasculino(){
		if( getSexo()!= null && getSexo().equalsIgnoreCase("M") ) return true;
		return false;
	}
	public boolean isFeminino(){
		if( getSexo()!= null && getSexo().equalsIgnoreCase("F") ) return true;
		return false;
	}
	
	public boolean isMesmoProcesso(String id) {
		if (getId_Processo().equals(id)) return true;
		return false;
	}
	
	public String getSiglaCtpsUf() {
		return siglaCtpsUf;
	}

	public void setSiglaCtpsUf(String siglaCtpsUf) {
		if (null != siglaCtpsUf){
			if (siglaCtpsUf.equalsIgnoreCase("null")){
				Sinal = "";
			}else if (!siglaCtpsUf.equalsIgnoreCase("")) this.siglaCtpsUf = siglaCtpsUf;
		}
	}
	
	public String getLogradouroNumeroComplemento(){
		return getEnderecoParte().getLogradouro() + " nº " + getEnderecoParte().getNumero() + " " + getEnderecoParte().getComplemento();
	}
	
	public String geBairroCidadeUf(){
		return getEnderecoParte().getBairro() + " " + getEnderecoParte().getCidade() + " " + getEnderecoParte().getUf();
	}
	
	public String getCepEmailTelefone(){
		return getEnderecoParte().getCep() + " " + getEMail() + " " + getTelefone();
	}	
	public boolean isPaciente(){
		int tipo = Funcoes.StringToInt(getProcessoParteTipoCodigo());
		if (tipo == ProcessoParteTipoDt.PACIENTE){
			return true;
		}
		return false;
	}
	public boolean isLitisconsorteAtivo(){
		int tipo = Funcoes.StringToInt(getProcessoParteTipoCodigo());
		if (tipo == ProcessoParteTipoDt.LITIS_CONSORTE_ATIVO){
			return true;
		}
		return false;
	}
	public boolean isLitisconsortePassivo(){
		int tipo = Funcoes.StringToInt(getProcessoParteTipoCodigo());
		if (tipo == ProcessoParteTipoDt.LITIS_CONSORTE_PASSIVO){
			return true;
		}
		return false;
	}
	public boolean isSubstitutoProcessual(){
		int tipo = Funcoes.StringToInt(getProcessoParteTipoCodigo());
		if (tipo == ProcessoParteTipoDt.SUBSTITUTO_PROCESSUAL){
			return true;
		}
		return false;
	}
	public boolean isVitima(){
		int tipo = Funcoes.StringToInt(getProcessoParteTipoCodigo());
		if (tipo == ProcessoParteTipoDt.VITIMA){
			return true;
		}
		return false;
	}
	public boolean isTestemunha(){
		int tipo = Funcoes.StringToInt(getProcessoParteTipoCodigo());
		if (tipo == ProcessoParteTipoDt.TESTEMUNHA){
			return true;
		}
		return false;
	}
	public boolean isComunicante(){
		int tipo = Funcoes.StringToInt(getProcessoParteTipoCodigo());
		if (tipo == ProcessoParteTipoDt.COMUNICANTE){
			return true;
		}
		return false;
	}
	public int getIndiceOutrasPartes(){	
		if ( isLitisconsorteAtivo()){
			return 0;
		} else if ( isLitisconsortePassivo()){
			return 1;
		} else if ( isSubstitutoProcessual()){
			return 2;
		} else if (isPaciente()){   	
			return 3;
		} else if(isVitima()){   	
			return 4;
		}
		//outros 
		return 5;
	}

	// jvosantos - 12/06/2019 11:10 - Adicionar Ordem da Parte
	public Integer getOrdemParte() {
		return ordemParte;
	}
	
	// jvosantos - 12/06/2019 11:10 - Adicionar Ordem da Parte
	public void setOrdemParte(Integer ordemParte) {
		this.ordemParte = ordemParte;
	}
	
//	@Override
//	public boolean equals(Object processoParteObj) {		
//		if (processoParteObj == null || ! (processoParteObj instanceof ProcessoParteDt)) return false; 
//		
//		ProcessoParteDt processoParteDt = (ProcessoParteDt) processoParteObj;
//		
//		if (processoParteDt.getId() == null && this.getId() == null) return true;
//		
//		if (processoParteDt.getId() == null || this.getId() == null) return false;
//		
//		return processoParteDt.getId().equalsIgnoreCase(this.getId());
//	}
	public String getNomeAbreviado() {
		if (Funcoes.StringToInt(getProcessoParteTipoCodigo(),-999)==ProcessoParteTipoDt.VITIMA) {
			return Funcoes.iniciaisNome(getNome());
		}
		return getNome();
	}

	public String getDataSentenca()  {return DataSentenca;}

	public void setDataSentenca(String valor ) {if (valor!=null) DataSentenca = valor;}

	public String getDataPronuncia()  {return DataPronuncia;}

	public void setDataPronuncia(String valor ) {if (valor!=null) DataPronuncia = valor;}
	
	public String getReuPreso() {
		String stTemp="";
		
		if (ReuPreso ) {
			stTemp= "RÉU PRESO";
		}
		
		return stTemp;
	}

	public void setReuPreso(String id_prisao) {
		ReuPreso = false;
		if(id_prisao!=null && !id_prisao.isEmpty()) {
			ReuPreso = true;
		}		
	}
	
	public boolean isTemEmail(){
		String email = getEMail();
		if (!email.isEmpty() && email != null){
			return true;
		}
		return false;
	}
	
	public boolean isTemTelefone(){
		String telefone = getTelefone();
		if (!telefone.isEmpty() && telefone != null){
			return true;
		}
		return false;
	}
}

