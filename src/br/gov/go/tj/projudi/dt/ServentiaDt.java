package br.gov.go.tj.projudi.dt;

import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class ServentiaDt extends ServentiaDtGen {

	private static final long serialVersionUID = 1847448656806944582L;

	public static final int CodigoPermissao = 168;
	
	public static final Integer SERV_PRIMEIRA_SECAO_CIVEL = 1492;
	public static final Integer SERV_SEGUNDA_SECAO_CIVEL = 1493;
	public static final Integer SERV_ORGAO_ESPECIAL = 1496;
	public static final Integer SERV_CONSELHO_SUPERIOR_MAGISTRATURA = 3375;
	public static final Integer SERV_SESSAO_CRIMINAL = 4601;
	
	public static final int INATIVO = -1;
	public static final int ATIVO = 0;
	public static final int VARA_CODIGO = 16;
	public static final int PUBLICA_CODIGO = 2619;
	public static final int DIVISAO_DISTRIBUICAO_TRIBUNAL_ID = 1873;
	public static final String MENSAGEM_AUSENCIA_SERVENTIA_RELACIONADA_CEJUSC = "Não foi localizada uma serventia relacionada do tipo Preprocessual (CEJUSC).";

	public static final String FLUXO_SERVENTIA = "1";
	public static final String FLUXO_AREA_DISTRIBUICAO = "2";
	public static final String FLUXO_SERVENTIA_RELACIONADA = "3";
	public static final String FLUXO_SERVENTIA_BLOQUEIO = "4";
	public static final String FLUXO_SERVENTIA_DESBLOQUEIO = "5";
	public static final String CODIGO_CENTRAL_MANDADO_GOIANIA = "39099";
	public static final String CODIGO_INTERNO_SENADOR_CANEDO_CENTRAL_MANDADO = "122012";
	public static final String ID_DIRETORIA_FINANCEIRA= "71";
	public static final String CENTRAL_MANDADOS_TIPO_SERVENTIA_CODIGO = "2";
	
	public static final String GERENCIAMENTO_SISTEMA_PRODUDI = "707";
	
	//public static final int SistemaProjudi = 1;
	public static final int Parte = 2;

	public static final String ID_GABINETE_PRESIDENCIA = "1837"; // jvosantos - 29/08/2019 16:15 - Adicionando váriavel para identificar o gabinete do Presidente
	
	//Atributo para verificar se serventia é on-line
	private String online;
	private EnderecoDt enderecoDt = new EnderecoDt();
	//******************************************************************************************************
	
	private List<ServentiaRelacionadaDt> listaServentiasRelacoes;
	private List listaAreasDistribuicoes;
	private String conclusoDireto;	
	private String substitutoSegundoGrau;	
	private String presidencia;
	private String id_ServentiaSubstituicao;
	private String serventiaSubstituicao;
	private String dataInicialSubstituicao;
	private String dataFinalSubstituicao;
	private String recebeProcesso;	
	private ServentiaRelacionadaDt serventiaDtRelacaoEdicao; 
	private String id_CNJServentia;
	private String email;

	private String Id_AreaDistribuicaoSecundaria; //referente ao campo '2ª Área de Distribuição'
	private String AreaDistribuicaoSecundaria; //referente ao campo '2ª Área de Distribuição'
	
	public String getId_ServentiaRelacaoEdicao() {
		if (serventiaDtRelacaoEdicao!=null) {
			return serventiaDtRelacaoEdicao.getId_ServentiaRelacao();
		}
		return null;
	}

	public void setId_ServentiaRelacaoEdicao(String id_ServentiaRelacionada) {
		if (serventiaDtRelacaoEdicao!=null && id_ServentiaRelacionada!=null) {
			 serventiaDtRelacaoEdicao.setId_ServentiaRelacao(id_ServentiaRelacionada);
		}		
	}

	public List<ServentiaRelacionadaDt> getListaServentiasRelacoes() {
		return listaServentiasRelacoes;
	}

	public void setListaServentiasRelacoes(List<ServentiaRelacionadaDt> listaServentiasRelacoes) {
		if (listaServentiasRelacoes != null) this.listaServentiasRelacoes = listaServentiasRelacoes;
	}

	public List getListaAreasDistribuicoes() {
		return listaAreasDistribuicoes;
	}

	public void setListaAreasDistribuicoes(List listaAreasDistribuicoes) {
		this.listaAreasDistribuicoes = listaAreasDistribuicoes;
	}

	//********************************************************************************************************
	public EnderecoDt getEnderecoDt() {
		return enderecoDt;
	}

	public void setEnderecoDt(EnderecoDt enderecoDt) {
		this.enderecoDt = enderecoDt;
	}

	public void limpar() {
		super.limpar();
		this.serventiaDtRelacaoEdicao = null;
		this.listaServentiasRelacoes = null;
		this.listaAreasDistribuicoes = null;
		this.online = "";		
		this.conclusoDireto = "false";
		this.presidencia = "false";		
		this.substitutoSegundoGrau = "";
		this.id_ServentiaSubstituicao = "";
		this.serventiaSubstituicao = "";
		this.dataInicialSubstituicao = "";
		this.dataFinalSubstituicao = "";
		this.recebeProcesso = "false";		
		this.id_CNJServentia = "";
		enderecoDt = new EnderecoDt();
		this.Id_AreaDistribuicaoSecundaria = "";
		this.AreaDistribuicaoSecundaria = "";
		this.email = "";
				
	}

	public void copiar(ServentiaDt objeto) {
		super.copiar(objeto);
		enderecoDt.copiar(objeto.getEnderecoDt());
		
		this.online = objeto.online;			
		this.enderecoDt = objeto.getEnderecoDt();		
		/*if (objeto.listaServentiasRelacoes != null) this.listaServentiasRelacoes = new ArrayList(objeto.listaServentiasRelacoes);
		else this.listaServentiasRelacoes = null;		
		if (objeto.listaAreasDistribuicoes != null) this.listaAreasDistribuicoes = new ArrayList(objeto.listaAreasDistribuicoes);
		else this.listaAreasDistribuicoes = null;*/
		this.conclusoDireto = objeto.getConclusoDireto();
		this.presidencia = objeto.getPresidencia();		
		this.substitutoSegundoGrau = objeto.getSubstitutoSegundoGrau();
		this.id_ServentiaSubstituicao = objeto.getId_ServentiaSubstituicao();
		this.serventiaSubstituicao = objeto.getServentiaSubstituicao();
		this.dataInicialSubstituicao = objeto.getDataInicialSubstituicao();
		this.dataFinalSubstituicao = objeto.getDataFinalSubstituicao();
		this.recebeProcesso = objeto.getRecebeProcesso();		 
		/*if (objeto.serventiaDtRelacaoEdicao != null) {
			this.serventiaDtRelacaoEdicao = new ServentiaDt();
			this.serventiaDtRelacaoEdicao.copiar(objeto.serventiaDtRelacaoEdicao);
		} else this.serventiaDtRelacaoEdicao = null;*/
		this.id_CNJServentia = objeto.getId_CNJServentia();
		this.Id_AreaDistribuicaoSecundaria = objeto.getId_AreaDistribuicaoSecundaria();
		this.AreaDistribuicaoSecundaria = objeto.getAreaDistribuicaoSecundaria();
		this.email = objeto.getEmail();
	}
	
	public String getId_Cidade(){		
		return enderecoDt.getId_Cidade();				
	}	
	
	public String getOnline() {
		return this.online;
	}

	public void setOnline(String online) {
		if (online != null) this.online = online;
	}

	public String getConclusoDireto() {
		return this.conclusoDireto;
	}

	public void setConclusoDireto(String conclusoDireto) {
		if(conclusoDireto != null) this.conclusoDireto = conclusoDireto;
	}

	public boolean isConclusoDireto() {
		if(conclusoDireto.equalsIgnoreCase("1") || conclusoDireto.equalsIgnoreCase("true"))
			return true;
		else return false;
	}
	
	public String getPresidencia() {
		return this.presidencia;
	}

	public void setPresidencia(String presidencia) {
		if(presidencia != null) this.presidencia = presidencia;
	}

	public boolean isPresidencia() {
		if (presidencia == null) return false;
		return this.presidencia.equalsIgnoreCase("true");		
	}

	public String getQuantidadeDistribuicaoDescricao() {
		if(QuantidadeDistribuicao!="") {
			return String.valueOf((int)(Float.parseFloat(getQuantidadeDistribuicao().replaceAll(",", "."))*100) + "%");
		}
		return "0%";
	}
	
	public boolean isAtivo(){
		if (this.getCodigoTemp() == null) return false;
		return (this.getCodigoTemp().trim().equalsIgnoreCase(String.valueOf(ATIVO)));
	}	
	
	public String getSubstitutoSegundoGrau() {
		return this.substitutoSegundoGrau;
	}
	
	public void setSubstitutoSegundoGrau(String substitutoSegundoGrau)
	{
		if(substitutoSegundoGrau != null) this.substitutoSegundoGrau = substitutoSegundoGrau;
	}

	public boolean isSubstitutoSegundoGrau() {
		if (this.substitutoSegundoGrau != null && this.substitutoSegundoGrau.trim().length() > 0) return this.substitutoSegundoGrau.equalsIgnoreCase("true");
		return (this.id_ServentiaSubstituicao != null && this.id_ServentiaSubstituicao.trim().length() > 0);		
	}
	
	public void setId_ServentiaSubstituicao(String id_ServentiaSubstituicao) {
		if (id_ServentiaSubstituicao != null) 
			this.id_ServentiaSubstituicao = id_ServentiaSubstituicao;
	}

	public String getId_ServentiaSubstituicao() {
		return this.id_ServentiaSubstituicao;
	}
	
	public void setServentiaSubstituicao(String serventiaSubstituicao) {
		if (serventiaSubstituicao != null) 
			this.serventiaSubstituicao = serventiaSubstituicao;
	}

	public String getServentiaSubstituicao() {
		return this.serventiaSubstituicao;		
	}
	
	public String getDataInicialSubstituicao() {
		return this.dataInicialSubstituicao;
	}

	public void setDataInicialSubstituicao(String dataInicialSubstituicao) {
		if (dataInicialSubstituicao != null) 
			this.dataInicialSubstituicao = dataInicialSubstituicao;
	}

	public String getDataFinalSubstituicao() {
		return this.dataFinalSubstituicao;
	}

	public void setDataFinalSubstituicao(String dataFinalSubstituicao) {
		if (dataFinalSubstituicao != null)
			this.dataFinalSubstituicao = dataFinalSubstituicao;
	}
	
	public String getRecebeProcesso() {
		return this.recebeProcesso;
	}
	
	public void setRecebeProcesso(String recebeProcesso)
	{
		if(recebeProcesso != null) this.recebeProcesso = recebeProcesso;
	}

	public boolean isRecebeProcesso() {
		if (this.recebeProcesso == null) return false;
		return this.recebeProcesso.equalsIgnoreCase("true");		
	}
	
	public void limparServentiaRelacionadaEdicao() {
		this.serventiaDtRelacaoEdicao = null;
	}	
	
	public void setDadosServentiaRelacionadaEdicao(String Id_ServentiaRelacionada) {
		if (Id_ServentiaRelacionada != null){
			this.limparServentiaRelacionadaEdicao();
			this.serventiaDtRelacaoEdicao = new ServentiaRelacionadaDt();
			this.serventiaDtRelacaoEdicao.setId_ServentiaRelacao(Id_ServentiaRelacionada);
		}
	}
	
	public void limparDadosServentiaRelacionadaEdicao() {
		this.substitutoSegundoGrau = "false";
		this.id_ServentiaSubstituicao = "";
		this.serventiaSubstituicao = "";
		this.dataInicialSubstituicao = "";
		this.dataFinalSubstituicao = "";		
		limparServentiaRelacionadaEdicao();		
	}
	
	public void limparId_ServentiaRelacionadaEdicao() {
		if (this.serventiaDtRelacaoEdicao != null) {
			this.serventiaDtRelacaoEdicao.setId_ServentiaRelacao("");
		}
	}
	
	public void setServentiaDtRelacaoEdicao(ServentiaRelacionadaDt serventiaDtRelacaoEdicao){
		this.serventiaDtRelacaoEdicao = serventiaDtRelacaoEdicao;
	}
	
	public ServentiaRelacionadaDt getServentiaDtRelacaoEdicao() {
		return this.serventiaDtRelacaoEdicao;
	}
	
	public boolean isEdicaoServentiaRelacionada() {
		return (this.serventiaDtRelacaoEdicao != null && !this.serventiaDtRelacaoEdicao.getId().isEmpty());
	}
				
	public boolean isSegundoGrau() {
		if (getServentiaTipoCodigo() == null || getServentiaTipoCodigo().trim().length() == 0 || getServentiaSubtipoCodigo() == null || getServentiaSubtipoCodigo().trim().length() == 0 || Funcoes.StringToInt(getServentiaTipoCodigo()) != ServentiaTipoDt.SEGUNDO_GRAU) return false;
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo== ServentiaSubtipoDt.CAMARA_CIVEL || 
				serventiaSubTipoCodigo == ServentiaSubtipoDt.CAMARA_CRIMINAL ||
				serventiaSubTipoCodigo == ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU || 
				serventiaSubTipoCodigo == ServentiaSubtipoDt.SECAO_CIVEL ||
				serventiaSubTipoCodigo == ServentiaSubtipoDt.SECAO_CRIMINAL ||
				serventiaSubTipoCodigo == ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA ||		
				serventiaSubTipoCodigo == ServentiaSubtipoDt.CORTE_ESPECIAL);
	}
	
	public boolean isCamaraCriminal() {
		if (getServentiaTipoCodigo() == null || getServentiaTipoCodigo().trim().length() == 0 || getServentiaSubtipoCodigo() == null || getServentiaSubtipoCodigo().trim().length() == 0 || Funcoes.StringToInt(getServentiaTipoCodigo()) != ServentiaTipoDt.SEGUNDO_GRAU) return false;
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo == ServentiaSubtipoDt.CAMARA_CRIMINAL);
	}
	
	public boolean isTurma() {
		if (getServentiaTipoCodigo() == null || getServentiaTipoCodigo().trim().length() == 0 || getServentiaSubtipoCodigo() == null || getServentiaSubtipoCodigo().trim().length() == 0 || Funcoes.StringToInt(getServentiaTipoCodigo()) != ServentiaTipoDt.SEGUNDO_GRAU) return false;
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo== ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL || 
				serventiaSubTipoCodigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL || 
				//serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL || 
				serventiaSubTipoCodigo == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
	}
	
	public boolean isGabineteSegundoGrau() {
		if (getServentiaTipoCodigo() == null || getServentiaTipoCodigo().trim().length() == 0 || getServentiaSubtipoCodigo() == null || getServentiaSubtipoCodigo().trim().length() == 0) return false;
		
		int serventiaTipoCodigo = Funcoes.StringToInt(getServentiaTipoCodigo());
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return ((serventiaTipoCodigo == ServentiaTipoDt.SEGUNDO_GRAU 		   &&   serventiaSubTipoCodigo== ServentiaSubtipoDt.GABINETE_SEGUNDO_GRAU) ||
				(serventiaTipoCodigo == ServentiaTipoDt.GABINETE &&   ( serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_SEGUNDO_GRAU ||   serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO || serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO)));
	}
	
	public static boolean isPresidenciaVicePresidencia(String serventiaSubTipoCodigo){
		boolean retorno = false;
		
		if (serventiaSubTipoCodigo != null && serventiaSubTipoCodigo.length()>0 && 
				( Funcoes.StringToInt(serventiaSubTipoCodigo)== ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO ||
				Funcoes.StringToInt(serventiaSubTipoCodigo)== ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO))
			retorno = true;
		
		return retorno;
	}
	
	public static boolean isGabineteUPJ(String servSubTipoCodigo) {
		boolean retorno = false;
		int serventiaSubTipoCodigo = Funcoes.StringToInt(servSubTipoCodigo);
		if ( serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ){
			retorno = true;
		}
		return retorno;
	}

	public String getId_CNJServentia() {
		return id_CNJServentia;
	}

	public void setId_CNJServentia(String id_CNJServentia) {
		if (id_CNJServentia != null) 
		this.id_CNJServentia = id_CNJServentia;
	}

	public String getPropriedades(){
		
		if (this.getEnderecoDt() != null && this.getEnderecoDt().getId() != null && this.getEnderecoDt().getId().length() > 0) {
			
			String retorno;
			
			retorno = "[Id_Serventia:" + getId() + ";Serventia:" + getServentia() + ";ServentiaCodigo:" + getServentiaCodigo() + ";ServentiaCodigoExterno:" + getServentiaCodigoExterno() + ";Id_ServentiaTipo:" + getId_ServentiaTipo(); 
			retorno += ";ServentiaTipo:" + getServentiaTipo() + ";Id_ServentiaSubtipo:" + getId_ServentiaSubtipo() + ";ServentiaSubtipo:" + getServentiaSubtipo() + ";Id_Area:" + getId_Area() + ";Area:" + getArea();
			retorno += ";Id_Comarca:" + getId_Comarca() + ";Comarca:" + getComarca() + ";Id_AreaDistribuicao:" + getId_AreaDistribuicao() + ";AreaDistribuicao:" + getAreaDistribuicao() + ";Id_EstadoRepresentacao:" + getId_EstadoRepresentacao(); 
			retorno += ";EstadoRepresentacao:" + getEstadoRepresentacao() + ";Id_AudienciaTipo:" + getId_AudienciaTipo() + ";AudienciaTipo:" + getAudienciaTipo() + ";QuantidadeDistribuicao:" + getQuantidadeDistribuicao();
			retorno += ";Id_Endereco:" + this.getEnderecoDt().getId() + ";Logradouro:" + this.getEnderecoDt().getLogradouro() + ";Numero:" + this.getEnderecoDt().getNumero() + ";Complemento:" + this.getEnderecoDt().getComplemento();					
			retorno += ";Id_Bairro:" + this.getEnderecoDt().getId_Bairro() + ";Bairro:" + this.getEnderecoDt().getBairro() + ";Id_Cidade:" + this.getEnderecoDt().getId_Cidade() + ";Cidade:" + this.getEnderecoDt().getCidade();
			retorno	+= ";Estado:" + this.getEnderecoDt().getUf() + ";Cep:" + this.getEnderecoDt().getCep() + ";Telefone:" + getTelefone() + ";Online:" + getOnline() + ";CodigoTemp:" + getCodigoTemp();
			retorno += ";ServentiaTipoCodigo:" + getServentiaTipoCodigo() + ";ServentiaSubtipoCodigo:" + getServentiaSubtipoCodigo() + ";AreaCodigo:" + getAreaCodigo() + ";ComarcaCodigo:" + getComarcaCodigo();
			retorno += ";DataCadastro:" + getDataCadastro() + ";DataImplantacao:" + getDataImplantacao() + ";conclusoDireto:" + conclusoDireto;
			retorno	+= ";substitutoSegundoGrau:" + substitutoSegundoGrau + ";presidencia:" + presidencia + ";id_ServentiaSubstituicao:" + id_ServentiaSubstituicao + ";dataInicialSubstituicao:" + dataInicialSubstituicao;
			retorno += ";dataFinalSubstituicao:" + dataFinalSubstituicao + ";recebeProcesso:" + recebeProcesso + ";id_CNJServentia:" + id_CNJServentia + ";Id_AreaDistribuicaoSecundaria:" + Id_AreaDistribuicaoSecundaria + ";AreaDistribuicaoSecundaria:" + AreaDistribuicaoSecundaria + "]";
			
			return retorno;
			
		} else{
			
			String retorno;
			
			retorno = "[Id_Serventia:" + getId() + ";Serventia:" + getServentia() + ";ServentiaCodigo:" + getServentiaCodigo() + ";ServentiaCodigoExterno:" + getServentiaCodigoExterno() + ";Id_ServentiaTipo:" + getId_ServentiaTipo(); 
			retorno += ";ServentiaTipo:" + getServentiaTipo() + ";Id_ServentiaSubtipo:" + getId_ServentiaSubtipo() + ";ServentiaSubtipo:" + getServentiaSubtipo() + ";Id_Area:" + getId_Area() + ";Area:" + getArea();
			retorno += ";Id_Comarca:" + getId_Comarca() + ";Comarca:" + getComarca() + ";Id_AreaDistribuicao:" + getId_AreaDistribuicao() + ";AreaDistribuicao:" + getAreaDistribuicao() + ";Id_EstadoRepresentacao:" + getId_EstadoRepresentacao(); 
			retorno += ";EstadoRepresentacao:" + getEstadoRepresentacao() + ";Id_AudienciaTipo:" + getId_AudienciaTipo() + ";AudienciaTipo:" + getAudienciaTipo() + ";QuantidadeDistribuicao:" + getQuantidadeDistribuicao();
			retorno += ";Id_Endereco:" + getId_Endereco() + ";Logradouro:" + getLogradouro() + ";Numero:" + getNumero() + ";Complemento:" + getComplemento() + ";Id_Bairro:" + getId_Bairro() + ";Bairro:" + getBairro() + ";Id_Cidade:" + getId_Cidade();
			retorno += ";Cidade:" + getCidade() + ";Estado:" + enderecoDt.getUf() + ";Cep:" + getCep() + ";Telefone:" + getTelefone() + ";Online:" + getOnline() + ";CodigoTemp:" + getCodigoTemp();
			retorno += ";ServentiaTipoCodigo:" + getServentiaTipoCodigo() + ";ServentiaSubtipoCodigo:" + getServentiaSubtipoCodigo() + ";AreaCodigo:" + getAreaCodigo() + ";ComarcaCodigo:" + getComarcaCodigo();
			retorno += ";DataCadastro:" + getDataCadastro() + ";DataImplantacao:" + getDataImplantacao() + ";conclusoDireto:" + conclusoDireto;
			retorno += ";substitutoSegundoGrau:" + substitutoSegundoGrau + ";presidencia:" + presidencia + ";id_ServentiaSubstituicao:" + id_ServentiaSubstituicao + ";dataInicialSubstituicao:" + dataInicialSubstituicao;
			retorno += ";dataFinalSubstituicao:" + dataFinalSubstituicao + ";recebeProcesso:" + recebeProcesso + ";id_CNJServentia:" + id_CNJServentia + ";Id_AreaDistribuicaoSecundaria:" + Id_AreaDistribuicaoSecundaria + ";AreaDistribuicaoSecundaria:" + AreaDistribuicaoSecundaria + "]";
			
			return retorno;
		}
		
	}

	public boolean temAudienciaTipo() {
		if (getId_AudienciaTipo() != null && getId_AudienciaTipo().length()>0)
			return true;
		return false;
	}
	
	public boolean temCodigoExterno(){
		if ( getServentiaCodigoExterno() != null && getServentiaCodigoExterno().trim().length() > 0 && !getServentiaCodigoExterno().trim().equalsIgnoreCase("null")) {
			return true;
		}				
		return false;
	}

	public boolean isVara() {
		if (Funcoes.StringToInt( getServentiaTipoCodigo(),-1) == ServentiaTipoDt.VARA) return true;
		return false;
	}
	
	public boolean isServentiaTipo2Grau() {
		if (Funcoes.StringToInt(getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU) {
			return true;
		}

		return false;
	}
	
	public boolean isGabineteServentiaRelacionada(){
		
		if (getServentiaDtRelacaoEdicao()!=null && ServentiaTipoDt.isGabineteDesembargador( getServentiaDtRelacaoEdicao().getServentiaTipoCodigoRelacionada())){
			return true;
		}
		return false;	
	}

	public boolean isPlantaoSegundoGrau() {
		return ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU == Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1);		
	}

	public boolean isPlantaoPrimeiroGrau() {
		return ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU == Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1);
	}
	
	public boolean isPlantaoAudienciaCustodia() {
		return ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA == Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1);
	}
	
	public boolean isJuizadoInfanciaJuventudeCivel() {
		return ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL == Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1);
	}
	
	public boolean isCentralMandados() {
		return ServentiaSubtipoDt.CENTRAL_MANDADOS == Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1);
	}
	
	public boolean isJuizadoInfanciaJuventudeInfracional() {
		return ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL == Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1);
	}
	
	public boolean isJuizado() {
		if ( Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL ||
	        	Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL ||
	            Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL ||
	            Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL ||
	            Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL ||            
	            Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA ||
	            Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA ||
	            Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA ||
	            Funcoes.StringToInt(getServentiaSubtipoCodigo(),-1)== ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA)
	            return true;
	       return false;
	}
	
	public void setId_Endereco(String id_endereco){
		enderecoDt.setId(id_endereco);
	}
	
	public void setLogradouro(String LOGRADOURO){
		enderecoDt.setLogradouro(LOGRADOURO);
	}
	public void setNumero(String NUMERO){
		enderecoDt.setNumero(NUMERO);
	}
	public void setComplemento(String COMPLEMENTO){
		enderecoDt.setComplemento(COMPLEMENTO);
	}
	public void setCep(String CEP){
		enderecoDt.setCep(CEP);
	}
	public void setId_Bairro(String id_bairro){
		enderecoDt.setId_Bairro(id_bairro);
	}
	public void setBairro(String BAIRRO){
		enderecoDt.setBairro(BAIRRO);
	}
	public void setCidade(String CIDADE){
		enderecoDt.setCidade(CIDADE);
	}
	public void setEstado(String ESTADO){
		enderecoDt.setEstado(ESTADO);
	}
	
	public String getId_Bairro(){		
		return enderecoDt.getId_Bairro();		
	}
	public String getBairro(){		
		return enderecoDt.getBairro();		
	}
	public String getBairroCodigo(){		
		return enderecoDt.getBairroCodigo();		
	}
	public String getCep(){		
		return enderecoDt.getCep();				
	}
	public String getCidade(){		
		return enderecoDt.getCidade();									
	}	
	public String getCidadeCodigo(){		
		return enderecoDt.getCidadeCodigo();									
	}
	public String getComplemento(){		
		return enderecoDt.getComplemento();						
	}
	public String getEstado(){		
		return enderecoDt.getEstado();						
	}
	public String getEstadoCodigo(){		
		return enderecoDt.getEstadoCodigo();													
	}
	public String getLogradouro(){		
		return enderecoDt.getLogradouro();							
	}
	public String getNumero(){		
		return enderecoDt.getNumero();								
	}
	public String getUf(){		
		return enderecoDt.getUf();															
	}
	
	public void limparId_Endereco( ) {
		enderecoDt.setId("");		
	}

	public void setId_Cidade(String valor) {
		enderecoDt.setId_Cidade(valor);	
	}
	public void setUf(String valor) {
		enderecoDt.setUf(valor);	
	}
	public String getId_Endereco()  {
		return enderecoDt.getId();
	}
	
	public void setId_UsuarioLog(String id_UsuarioLog) {
		if(id_UsuarioLog!=null) {
			Id_UsuarioLog = id_UsuarioLog;
			enderecoDt.setId_UsuarioLog(id_UsuarioLog);
		}
	}
	public void setIpComputadorLog(String valor) {
		if (valor != null){
			IpComputadorLog = valor;	
			enderecoDt.setIpComputadorLog(valor);
		}
	}

	public String getId_AreaDistribuicaoSecundaria() {
		
		return Id_AreaDistribuicaoSecundaria;
	}
	
	public void setId_AreaDistribuicaoSecundaria(String valor) {
		
		if (valor!=null) {
			if (valor.equalsIgnoreCase("null")) {
				
				Id_AreaDistribuicaoSecundaria = "";
				AreaDistribuicaoSecundaria = "";
			}
			else if (!valor.equalsIgnoreCase("")) {
				
				Id_AreaDistribuicaoSecundaria = valor;
			}
		}
	}
	
	public String getAreaDistribuicaoSecundaria() {
		
		return AreaDistribuicaoSecundaria;
	}
	
	public void setAreaDistribuicaoSecundaria(String valor) {
		
		if (valor!=null){
			
			AreaDistribuicaoSecundaria = valor;
		}
	}

	public String getEmail() {		
		return email;
	}

	public void setEmail(String email) {
		if(email != null) {
			this.email = email;
		}
	}

	public boolean isCriminal() {
		int codigo = Funcoes.StringToInt(this.getServentiaSubtipoCodigo(),-1);
		if (codigo == ServentiaSubtipoDt.CAMARA_CRIMINAL ||
			codigo == ServentiaSubtipoDt.SECAO_CRIMINAL ||
			codigo == ServentiaSubtipoDt.VARA_CRIMINAL ||
			codigo == ServentiaSubtipoDt.UPJ_CRIMINAL ||
			codigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA ||
			codigo == ServentiaSubtipoDt.UPJ_CUSTODIA ||
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL ||
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL || 
			codigo == ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA ||
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA ||  
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL ||
			codigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL ||
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL)
		{
			return true;
		}
		return false;
	}
	
	public boolean isSubTipoExecucaoPenal() {
		int codigo = Funcoes.StringToInt(this.getServentiaSubtipoCodigo(),-1);
		if (codigo == ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL ){
			return true;
		}
		return false;
	}
	
	public boolean isComCustas() {
		
		int codigo = Funcoes.StringToInt(this.getServentiaSubtipoCodigo(),-1);
		if (codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL ||
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL ||
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL || 
			codigo == ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA ||
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA ||  
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA ||
			codigo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA ||
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL ||
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL ||
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL ||
			codigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL ||
			codigo == ServentiaSubtipoDt.PREPROCESSUAL ||
			codigo == ServentiaSubtipoDt.CAMARA_CRIMINAL ||
			codigo == ServentiaSubtipoDt.SECAO_CRIMINAL || 
			codigo == ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL ||
			codigo == ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA ||
			codigo == ServentiaSubtipoDt.VARA_CRIMINAL ||
			codigo == ServentiaSubtipoDt.UPJ_CRIMINAL ||
			codigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA ||
			codigo == ServentiaSubtipoDt.UPJ_CUSTODIA
			)
		{
			return false;
		}
		return true;
	}

	public boolean isUtilizaContagemPrazoNovoCPC() {
		
		int codigo = Funcoes.StringToInt(this.getServentiaSubtipoCodigo(),-1);
		if (codigo == ServentiaSubtipoDt.VARAS_CIVEL ||
			codigo == ServentiaSubtipoDt.FAMILIA_CAPITAL ||
			codigo == ServentiaSubtipoDt.FAMILIA_INTERIOR ||
			codigo == ServentiaSubtipoDt.UPJ_FAMILIA ||
			codigo == ServentiaSubtipoDt.UPJ_SUCESSOES ||
			codigo == ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL || 
			codigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL ||
			codigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL ||
			codigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR ||
			codigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL ||
			codigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL ||
			codigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL ||
			codigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR ||
			codigo == ServentiaSubtipoDt.CAMARA_CIVEL ||
			codigo == ServentiaSubtipoDt.SECAO_CIVEL ||
			codigo == ServentiaSubtipoDt.CORTE_ESPECIAL 			
			)
		{
			return true;
		}
		return false;
	}

	public static boolean isBloqueada(String id_serventia) {

		//bo 2019/10090 e decreto judiciário n.º 2029/2019
		//1124	39011	Goiânia - 1ª Vara de Execução Penal
		//1126	1126	39242	Goiânia - 2ª Vara de Execução Penal
		//4596	39014	39292	Goiânia - 3ª Vara de Execução Penal
		//1125	1125	39013	Goiânia - Vara de Execução de Penas e Medidas Alternativas
		if ("1124".equals(id_serventia) ||
			"1126".equals(id_serventia) ||
			"4596".equals(id_serventia) ||
			"1125".equals(id_serventia)){
			 return true;
		}
		return false;
	}
	
	public boolean isSeventiaRelacionadaEdicao(ServentiaRelacionadaDt objTemp) {
		if(objTemp==null) return false;
		if(serventiaDtRelacaoEdicao==null) return false;
		if (objTemp.getId_ServentiaRelacao().equals(serventiaDtRelacaoEdicao.getId_ServentiaRelacao())) {
			return true;
		}
		return false;
		
	}
	
	public String getId_ServentiaRelacacionadaEdicao() {
		if (serventiaDtRelacaoEdicao!=null) {
			return serventiaDtRelacaoEdicao.getId();
		}
		return "";
	}
	public String getServentiaRelacacionadaEdicao() {
		if (serventiaDtRelacaoEdicao!=null) {
			return serventiaDtRelacaoEdicao.getServentiaRelacao();
		}
		return "";
	}

	public void removerServentiaRelacionadaLista(ServentiaRelacionadaDt serventiaRelacionadaDt) {
		for(int i=0;listaServentiasRelacoes!=null && i< listaServentiasRelacoes.size(); i++) {
			ServentiaRelacionadaDt serventiaRelacionada = listaServentiasRelacoes.get(i);
			if (serventiaRelacionada != null && serventiaRelacionada.isMesmoId(serventiaRelacionadaDt.getId())){
				listaServentiasRelacoes.remove(i);
				break;
			} 
		}
	}
	public boolean isUpjFamilia() {				
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo== ServentiaSubtipoDt.UPJ_FAMILIA);	
	}
	
	public boolean isUpjSucessoes() {				
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo== ServentiaSubtipoDt.UPJ_SUCESSOES);	
	}
	
	public boolean isUpjCriminal() {				
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo== ServentiaSubtipoDt.UPJ_CRIMINAL
				|| serventiaSubTipoCodigo== ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA
				|| serventiaSubTipoCodigo== ServentiaSubtipoDt.UPJ_CUSTODIA
				);	
	}
	
	public boolean isUpjJuizadoEspecialFazendaPublica() {
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);	
	}
	
	public boolean isUpjTurmaRecursal() {
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);	
	}
	
	public boolean isUPJs() {				
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_FAMILIA 
				|| serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_CRIMINAL
				|| serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA
				|| serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_SUCESSOES
				|| serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_CUSTODIA
				|| serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL
				|| serventiaSubTipoCodigo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);	
	}

	public boolean isGabineteVicePresidenciaTjgo() {
		int codigo = Funcoes.StringToInt(this.getServentiaSubtipoCodigo(),-1);
		return codigo == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO;
	}

	public boolean isGabinete() {
		int codigo = Funcoes.StringToInt(this.getServentiaTipoCodigo(),-1);
		return codigo == ServentiaTipoDt.GABINETE;
	}

	public boolean isServentiaAdvogadoProcurador() {		
		int serventiaTipoCodigo = Funcoes.StringToInt(getServentiaTipoCodigo(),-1);
		return (serventiaTipoCodigo== ServentiaTipoDt.ESCRITORIO_JURIDICO ||
				serventiaTipoCodigo== ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS ||
				serventiaTipoCodigo== ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL ||
				serventiaTipoCodigo== ServentiaTipoDt.DEFENSORIA_PUBLICA ||
				serventiaTipoCodigo== ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO || 
				serventiaTipoCodigo == ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL || 
				serventiaTipoCodigo == ServentiaTipoDt.PROCURADORIA_UNIAO);
	}

	public boolean isUpjCustodia() {
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo== ServentiaSubtipoDt.UPJ_CUSTODIA);	
	}

	public boolean isUpjViolenciaDomestica() {
		int serventiaSubTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo());
		return (serventiaSubTipoCodigo== ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);	
	}

}