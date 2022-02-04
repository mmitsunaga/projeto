package br.gov.go.tj.projudi.dt;

import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class AudienciaMovimentacaoDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6063416280976216079L;
	public static final int CodigoPermissao = 403;
	public static final int CodigoPermissaoAudienciaProcesso = 395; //Permissão para Movimentação de "AudienciaProcesso"

	//Audiência que está sendo movimentada
	private AudienciaDt audienciaDt;
	
	private PendenciaArquivoDt pendenciaArquivoDt;

	//Variáveis para guardar o status selecionado
	private String audienciaStatusCodigo;
	private String audienciaStatus;

	// Varíaveis para auxiliar na inserção de arquivos durante a movimentação de audiências
	private String id_ArquivoTipo;
	private String arquivoTipo;
	private String id_Modelo;
	private String modelo;
	private String textoEditor;
	private List listaArquivos;
	private String id_Classificador;
	private String classificador;
	private String id_Relatorio;
	private String hashRelatorio;
	//Lista dos possíveis status que uma audiência pode assumir
	private List listaAudienciaProcessoStatus;
	//Lista dos tipos de pendências que o usuário poderá gerar
	private List listaPendenciaTipos;
	//Lista das pendências que foram marcadas para serem geradas 
	private List listaPendenciasGerar;

	//No caso de uma Sessão de 2º que está sendo remarcada, deve guardar o id da nova Sessão
	private String id_NovaSessao;
	private String dataNovaSessao;
	private List listaSessoesAbertas;

	// Passos para auxiliar na movimentação de audiências
	private String passo1;
	private String passo2;
	private String passo3;

	//Variável para permitir retorno à tela de Consulta de Audiências
	private String menuAcionado;
	
	private PendenciaArquivoDt pendenciaArquivoDtEmenta;
	private String id_ArquivoTipoEmenta;
	private String idProximaAudiencia;
	private String arquivoTipoEmenta;
	private String id_ModeloEmenta;
	private String modeloEmenta;
	private String textoEditorEmenta;
	private String nomeArquivo;
	private String nomeArquivoEmenta;
	private String ehPreAnalise;
	private boolean nomeArquivoSomenteLeitura;
	private boolean arquivoTipoSomenteLeitura;
	private String id_MovimentacaoAtaAdiamento;	
	private String ehMovimentacaoSessaoAdiada;
	private String ehMovimentacaoSessaoIniciada;
	private String pendenciaTipoCodigo;
	private boolean pendenciaTipoSomenteLeitura;
	private boolean audienciaProcessoStatusSomenteLeitura;
	private String julgadoMeritoProcessoPrincipal;
	private boolean ignoraEtapa2Pendencias;
	private boolean votoPorMaioria;
	private String id_ServentiaCargoRedator;
	private String serventiaCargoRedator;
	private boolean alteracaoExtratoAta;
	private String Id_ServentiaCargoPresidente="";
	private String ServentiaCargoPresidente="";
	private String Id_ServentiaMP="";
	private String ServentiaMp="";
	private String Id_ServentiaCargoMp="";
	private String ServentiaCargoMp="";
	private boolean modeloSomenteLeitura;
	private boolean ehGuardarParaAssinar;
	private String isMovimentacaoSessaoDesmarcada;
	private String isMovimentacaoSessaoRetiradaDePauta;
	private boolean encaminhamento;
	private boolean verificarProcesso;
	private boolean isApreciados; // jvosantos - 03/06/2019 17:16 - Adicionar variavel para saber se é apreciados
	private boolean isMarcarDataAgendada;
	private String Id_PendenciaVotoGerada;
	private String Id_PendenciaEmentaGerada;
	private String Id_ServentiaCargoVotoEmentaGerada;
	
	public AudienciaMovimentacaoDt() {
		limpar();
	}

	public void limpar() {
		audienciaStatusCodigo = "";
		audienciaStatus = "";
		id_ArquivoTipo = "";
		arquivoTipo = "";
		id_Modelo = "";
		id_NovaSessao = "";
		dataNovaSessao = "";
		modelo = "";
		textoEditor = "";
		listaArquivos = null;
		listaPendenciasGerar = null;
		listaPendenciaTipos = null;
		listaAudienciaProcessoStatus = null;
		listaSessoesAbertas = null;
		id_Classificador = "";
		classificador = "";
		id_Relatorio = "";
		hashRelatorio = "";
		passo1 = "Passo 1";
		passo2 = "";
		passo3 = "";
		pendenciaArquivoDtEmenta = null;
		id_ArquivoTipoEmenta = "";
		arquivoTipoEmenta = "";
		id_ModeloEmenta = "";
		modeloEmenta = "";
		textoEditorEmenta = "";
		nomeArquivo = "";
		nomeArquivoEmenta = "";
		ehPreAnalise = "N";		
		nomeArquivoSomenteLeitura = false;
		arquivoTipoSomenteLeitura = false;
		id_MovimentacaoAtaAdiamento = "";
		ehMovimentacaoSessaoAdiada = "N";
		ehMovimentacaoSessaoIniciada = "N";
		pendenciaTipoCodigo = "";
		pendenciaTipoSomenteLeitura = false;
		setAudienciaProcessoStatusSomenteLeitura(false);
		julgadoMeritoProcessoPrincipal = "";
		ignoraEtapa2Pendencias = false;		
		votoPorMaioria = false;
		id_ServentiaCargoRedator = "";
		serventiaCargoRedator = "";
		alteracaoExtratoAta = false;
		modeloSomenteLeitura = false;
		Id_ServentiaCargoPresidente="";
		ServentiaCargoPresidente="";
		Id_ServentiaMP="";
		ServentiaMp="";
		Id_ServentiaCargoMp="";
		ServentiaCargoMp="";
		setPendenteAssinatura(false);
		isMovimentacaoSessaoDesmarcada = "N";
		isMovimentacaoSessaoRetiradaDePauta = "N";
		idProximaAudiencia = "";
		idProximaAudiencia = "";
		setEncaminhamento(false);
		setVerificarProcesso(true);
		Id_PendenciaVotoGerada = "";
		Id_PendenciaEmentaGerada = "";
		Id_ServentiaCargoVotoEmentaGerada = "";
	}

	public String getId_ArquivoTipo() {
		return id_ArquivoTipo;
	}

	public void setId_ArquivoTipo(String id_ArquivoTipo) {
		if (id_ArquivoTipo != null) if (id_ArquivoTipo.equalsIgnoreCase("null")) {
			this.id_ArquivoTipo = "";
			this.arquivoTipo = "";
		} else if (!id_ArquivoTipo.equalsIgnoreCase("")) this.id_ArquivoTipo = id_ArquivoTipo;
	}

	public String getArquivoTipo() {
		return arquivoTipo;
	}

	public void setArquivoTipo(String arquivoTipo) {
		if (arquivoTipo != null) if (arquivoTipo.equalsIgnoreCase("null")) this.arquivoTipo = "";
		else if (!arquivoTipo.equalsIgnoreCase("")) this.arquivoTipo = arquivoTipo;
	}

	public String getId_Modelo() {
		return id_Modelo;
	}

	public void setId_Modelo(String id_Modelo) {
		if (id_Modelo != null) if (id_Modelo.equalsIgnoreCase("null")) {
			this.id_Modelo = "";
			this.modelo = "";
		} else if (!id_Modelo.equalsIgnoreCase("")) this.id_Modelo = id_Modelo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		if (modelo != null) if (modelo.equalsIgnoreCase("null")) this.modelo = "";
		else if (!modelo.equalsIgnoreCase("")) this.modelo = modelo;
	}

	public String getTextoEditor() {
		return textoEditor;
	}

	public void setTextoEditor(String textoEditor) {
		if (textoEditor != null) this.textoEditor = textoEditor;
	}

	public List getListaArquivos() {
		return listaArquivos;
	}

	public void setListaArquivos(List listaArquivos) {
		this.listaArquivos = listaArquivos;
	}

	public String getPasso1() {
		return passo1;
	}

	public void setPasso1(String passo1) {
		if (passo1 != null) this.passo1 = passo1;
	}

	public String getPasso2() {
		return passo2;
	}

	public void setPasso2(String passo2) {
		if (passo2 != null) this.passo2 = passo2;
	}

	public String getPasso3() {
		return passo3;
	}

	public void setPasso3(String passo3) {
		if (passo3 != null) this.passo3 = passo3;
	}

	public List getListaPendenciasGerar() {
		return listaPendenciasGerar;
	}

	public void setListaPendenciasGerar(List listaPendenciasGerar) {
		this.listaPendenciasGerar = listaPendenciasGerar;
	}

	public AudienciaDt getAudienciaDt() {
		return audienciaDt;
	}

	public void setAudienciaDt(AudienciaDt audienciaDt) {
		this.audienciaDt = audienciaDt;
	}
	
	public PendenciaArquivoDt getPendenciaArquivoDt() {
		return pendenciaArquivoDt;
	}

	public void setPendenciaArquivoDt(PendenciaArquivoDt pendenciaArquivoDt) {
		this.pendenciaArquivoDt = pendenciaArquivoDt;
	}

	public List getListaPendenciaTipos() {
		return this.listaPendenciaTipos;
	}

	public void setListaPendenciaTipos(List listaPendenciaTipos) {
		this.listaPendenciaTipos = listaPendenciaTipos;
	}

	public String getAudienciaStatus() {
		return this.audienciaStatus;
	}

	public void setAudienciaStatus(String audienciaStatus) {
		if (audienciaStatus != null) this.audienciaStatus = audienciaStatus;
	}

	public List getListaAudienciaProcessoStatus() {
		return listaAudienciaProcessoStatus;
	}

	public void setListaAudienciaProcessoStatus(List listaAudienciaProcessoStatus) {
		this.listaAudienciaProcessoStatus = listaAudienciaProcessoStatus;
	}

	public String getAudienciaStatusCodigo() {
		return audienciaStatusCodigo;
	}

	public void setAudienciaStatusCodigo(String audienciaStatusCodigo) {
		if (audienciaStatusCodigo != null) this.audienciaStatusCodigo = audienciaStatusCodigo;
	}

	public String getfluxo() {
		return menuAcionado;
	}

	public void setfluxo(String menuAcionado) {
		if (menuAcionado != null) this.menuAcionado = menuAcionado;
	}

	public String getId_NovaSessao() {
		return this.id_NovaSessao;
	}

	public void setId_NovaSessao(String id_NovaSessao) {
		if (id_NovaSessao != null) this.id_NovaSessao = id_NovaSessao;
	}

	public String getDataNovaSessao() {
		return dataNovaSessao;
	}

	public void setDataNovaSessao(String dataNovaSessao) {
		if (dataNovaSessao != null) this.dataNovaSessao = dataNovaSessao;
	}

	public List getListaSessoesAbertas() {
		return listaSessoesAbertas;
	}

	public void setListaSessoesAbertas(List listaSessoesAbertas) {
		this.listaSessoesAbertas = listaSessoesAbertas;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		

	}

	public String getId_Classificador() {
		return id_Classificador;
	}

	public void setId_Classificador(String id_Classificador) {
		if (id_Classificador != null){
			if (id_Classificador.equalsIgnoreCase("null")) {		
				this.id_Classificador = "";
				this.classificador = "";
			} else if (!id_Classificador.equalsIgnoreCase("")) this.id_Classificador = id_Classificador;
		}
	}

	public String getClassificador() {
		return classificador;
	}

	public void setClassificador(String classificador) {
		if (classificador != null) this.classificador = classificador;
	}

	
	public String getId_Relatorio() {
		return id_Relatorio;
	}

	public void setId_Relatorio(String id_Relatorio) {
		if (id_Relatorio != null) this.id_Relatorio = id_Relatorio;
	}

	public String getHashRelatorio() {
		return hashRelatorio;
	}

	public void setHashRelatorio(String hashRelatorio) {
		if (hashRelatorio != null) this.hashRelatorio = hashRelatorio;
	}

	public PendenciaArquivoDt getPendenciaArquivoDtEmenta() {
		return pendenciaArquivoDtEmenta;
	}

	public void setPendenciaArquivoDtEmenta(PendenciaArquivoDt pendenciaArquivoDtEmenta) {
		this.pendenciaArquivoDtEmenta = pendenciaArquivoDtEmenta;
	}
	
	public String getId_ArquivoTipoEmenta() {
		return id_ArquivoTipoEmenta;
	}

	public void setId_ArquivoTipoEmenta(String id_ArquivoTipoEmenta) {
		if (id_ArquivoTipoEmenta != null) if (id_ArquivoTipoEmenta.equalsIgnoreCase("null")) {
			this.id_ArquivoTipoEmenta = "";
			this.arquivoTipoEmenta = "";
		} else if (!id_ArquivoTipoEmenta.equalsIgnoreCase("")) this.id_ArquivoTipoEmenta = id_ArquivoTipoEmenta;
	}
	
	public String getArquivoTipoEmenta() {
		return arquivoTipoEmenta;
	}

	public void setArquivoTipoEmenta(String arquivoTipoEmenta) {
		if (arquivoTipoEmenta != null) if (arquivoTipoEmenta.equalsIgnoreCase("null")) this.arquivoTipoEmenta = "";
		else if (!arquivoTipoEmenta.equalsIgnoreCase("")) this.arquivoTipoEmenta = arquivoTipoEmenta;
	}
	
	public String getId_ModeloEmenta() {
		return id_ModeloEmenta;
	}

	public void setId_ModeloEmenta(String id_ModeloEmenta) {
		if (id_ModeloEmenta != null) if (id_ModeloEmenta.equalsIgnoreCase("null")) {
			this.id_ModeloEmenta = "";
			this.modeloEmenta = "";
		} else if (!id_ModeloEmenta.equalsIgnoreCase("")) this.id_ModeloEmenta = id_ModeloEmenta;
	}

	public String getModeloEmenta() {
		return modeloEmenta;
	}

	public void setModeloEmenta(String modeloEmenta) {
		if (modeloEmenta != null) if (modeloEmenta.equalsIgnoreCase("null")) this.modeloEmenta = "";
		else if (!modeloEmenta.equalsIgnoreCase("")) this.modeloEmenta = modeloEmenta;
	}

	public String getTextoEditorEmenta() {
		return textoEditorEmenta;
	}

	public void setTextoEditorEmenta(String textoEditorEmenta) {
		if (textoEditorEmenta != null) this.textoEditorEmenta = textoEditorEmenta;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		if (nomeArquivo != null) this.nomeArquivo = nomeArquivo;
	}
	
	public String getNomeArquivoEmenta() {
		return nomeArquivoEmenta;
	}

	public void setNomeArquivoEmenta(String nomeArquivoEmenta) {
		if (nomeArquivoEmenta != null) this.nomeArquivoEmenta = nomeArquivoEmenta;
	}
	
	public void setNomeArquivoSomenteLeitura(boolean nomeArquivoSomenteLeitura) {
		this.nomeArquivoSomenteLeitura = nomeArquivoSomenteLeitura;		
	}
	
	public boolean isNomeArquivoSomenteLeitura() {
		return this.nomeArquivoSomenteLeitura;		
	}

	public void setArquivoTipoSomenteLeitura(boolean arquivoTipoSomenteLeitura) {
		this.arquivoTipoSomenteLeitura = arquivoTipoSomenteLeitura;		
	}
	
	public boolean isArquivoTipoSomenteLeitura() {
		return this.arquivoTipoSomenteLeitura;		
	}
	
	public void setId_MovimentacaoAtaAdiamento(String id_MovimentacaoAtaAdiamento){
		if (id_MovimentacaoAtaAdiamento != null) this.id_MovimentacaoAtaAdiamento = id_MovimentacaoAtaAdiamento;
	}
	
	public String getId_MovimentacaoAtaAdiamento(){
		return this.id_MovimentacaoAtaAdiamento;
	}
	
	public void setTipoAudienciaProcessoMovimentacao(String tipoAudienciaProcessoMovimentacao) {
		if (tipoAudienciaProcessoMovimentacao != null ) {			
			if (tipoAudienciaProcessoMovimentacao.trim().equalsIgnoreCase("1"))  this.ehPreAnalise = "S";
			else if (tipoAudienciaProcessoMovimentacao.trim().equalsIgnoreCase("3")) this.ehMovimentacaoSessaoIniciada = "S";
			else if (tipoAudienciaProcessoMovimentacao.trim().equalsIgnoreCase("4")) this.ehMovimentacaoSessaoAdiada = "S";
			else if (tipoAudienciaProcessoMovimentacao.trim().equalsIgnoreCase("5")) this.isMovimentacaoSessaoRetiradaDePauta = "S";
			else if (tipoAudienciaProcessoMovimentacao.trim().equalsIgnoreCase("6")) this.isMovimentacaoSessaoDesmarcada = "S";
		}
	}
	
	public boolean isPreAnalise() {
		if(this.ehPreAnalise == null) return false;
		return this.ehPreAnalise.trim().equalsIgnoreCase("S");
	}
	
	public boolean isMovimentacaoSessaoAdiada(){
		if(this.ehMovimentacaoSessaoAdiada == null) return false;
		return this.ehMovimentacaoSessaoAdiada.trim().equalsIgnoreCase("S");
	}
	
	public boolean isMovimentacaoSessaoIniciada(){
		if(this.ehMovimentacaoSessaoIniciada == null) return false;
		return this.ehMovimentacaoSessaoIniciada.trim().equalsIgnoreCase("S");
	}	
	
	public boolean isMovimentacaoSessaoRetiradaDePauta(){
		if(this.isMovimentacaoSessaoRetiradaDePauta == null) return false;
		return this.isMovimentacaoSessaoRetiradaDePauta.trim().equalsIgnoreCase("S");
	}
	
	public boolean isMovimentacaoSessaoDesmarcada(){
		if(this.isMovimentacaoSessaoDesmarcada == null) return false;
		return this.isMovimentacaoSessaoDesmarcada.trim().equalsIgnoreCase("S");
	}
	
	public void setPendenciaTipoCodigo(String pendenciaTipoCodigo){
		if (pendenciaTipoCodigo != null) this.pendenciaTipoCodigo = pendenciaTipoCodigo;
	}
	
	public String getPendenciaTipoCodigo(){
		return this.pendenciaTipoCodigo;
	}
	
	public void setPendenciaTipoSomenteLeitura(boolean pendenciaTipoSomenteLeitura){
		this.pendenciaTipoSomenteLeitura = pendenciaTipoSomenteLeitura;
	}
	
	public boolean isPendenciaTipoSomenteLeitura(){
		return this.pendenciaTipoSomenteLeitura;
	}

	public void setAudienciaProcessoStatusSomenteLeitura(boolean audienciaProcessoStatusSomenteLeitura) {
		this.audienciaProcessoStatusSomenteLeitura = audienciaProcessoStatusSomenteLeitura;
	}

	public boolean isAudienciaProcessoStatusSomenteLeitura() {
		return audienciaProcessoStatusSomenteLeitura;
	}
	
	public String getJulgadoMeritoProcessoPrincipal() {
		return julgadoMeritoProcessoPrincipal;
	}

	public void setJulgadoMeritoProcessoPrincipal(String julgadoMeritoProcessoPrincipal) {
		if (julgadoMeritoProcessoPrincipal != null) this.julgadoMeritoProcessoPrincipal = julgadoMeritoProcessoPrincipal;
	}

	public boolean isIgnoraEtapa2Pendencias() {
		return ignoraEtapa2Pendencias;
	}

	public void setIgnoraEtapa2Pendencias(boolean ignoraEtapa2Pendencias) {
		this.ignoraEtapa2Pendencias = ignoraEtapa2Pendencias;
	}
	
	public void setVotoPorMaioria(boolean votacaoPorMaioria) {
		this.votoPorMaioria = votacaoPorMaioria;
		if (!votacaoPorMaioria){
			this.id_ServentiaCargoRedator = "";
			this.serventiaCargoRedator = "";
		} 
	}

	public boolean isVotoPorMaioria() {
		return this.votoPorMaioria;
	}

	public String getId_ServentiaCargoRedator() {
		return id_ServentiaCargoRedator;
	}

	public void setId_ServentiaCargoRedator(String idServentiaCargoRedator) {
		if (idServentiaCargoRedator != null) this.id_ServentiaCargoRedator = idServentiaCargoRedator;
		if (this.id_ServentiaCargoRedator.trim().length() == 0) this.serventiaCargoRedator = "";
		this.votoPorMaioria = (this.id_ServentiaCargoRedator.trim().length() > 0);
	}

	public String getServentiaCargoRedator() {
		return serventiaCargoRedator;
	}

	public void setServentiaCargoRedator(String serventiaCargoRedator) {
		if(serventiaCargoRedator != null) this.serventiaCargoRedator = serventiaCargoRedator;
	}
	
	public void setAlteracaoExtratoAta(boolean alteracaoExtratoAta){
		this.alteracaoExtratoAta = alteracaoExtratoAta;
	}
	
	public boolean isAlteracaoExtratoAta(){
		return this.alteracaoExtratoAta;
	}

	public int getAudienciaTipoCodigo() {
		if (audienciaDt!=null) return Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo(),-1);
		return -1;
	}

	public void setAcordo(String acordo) {
		if (acordo != null && this.audienciaDt != null && this.audienciaDt.getAudienciaProcessoDt() != null) this.audienciaDt.getAudienciaProcessoDt().setAcordo(acordo);		
	}
	
	public boolean isHouveAcordo() {
		if (this.audienciaDt != null && this.audienciaDt.getAudienciaProcessoDt() != null) return this.audienciaDt.getAudienciaProcessoDt().isHouveAcordo();
		return false;
	}	
	
	public boolean isNaoHouveAcordo() {
		if (this.audienciaDt != null && this.audienciaDt.getAudienciaProcessoDt() != null) return this.audienciaDt.getAudienciaProcessoDt().isNaoHouveAcordo();
		return false;
	}

	public void setValorAcordo(String valorAcordo) {
		if (valorAcordo != null && this.audienciaDt != null && this.audienciaDt.getAudienciaProcessoDt() != null) this.audienciaDt.getAudienciaProcessoDt().setValorAcordo(valorAcordo);		
	}

	public String getValorAcordo() {
		if (this.audienciaDt != null && this.audienciaDt.getAudienciaProcessoDt() != null) return this.audienciaDt.getAudienciaProcessoDt().getValorAcordo();
		return "";
	}

	public void setId_ServentiaCargoPresidente(String id_Presidente) {
		if(id_Presidente != null && id_Presidente.length()>0){
			Id_ServentiaCargoPresidente = id_Presidente;
		}
		
	}	
	
	public void setModeloSomenteLeitura(boolean modeloSomenteLeitura) {
		this.modeloSomenteLeitura = modeloSomenteLeitura;		
	}
	
	public boolean isModeloSomenteLeitura() {
		return this.modeloSomenteLeitura;		
	}
	
	public String getId_ServentiaCargoPresidente() {
		return Id_ServentiaCargoPresidente ;
		
	}

	public void setServentiaCargoPresidente(String presidente) {
		if(presidente != null && presidente.length()>0){
			ServentiaCargoPresidente = presidente;
		}
	}	
	public String getServentiaCargoPresidente() {
		return ServentiaCargoPresidente;		
	}

	public void setId_ServentiaMp(String id_serventiaMp) {
		if (id_serventiaMp != null && id_serventiaMp.length()>0){
			Id_ServentiaMP = id_serventiaMp;
		}
		
	}	
	public String getId_ServentiaMp() {
		return Id_ServentiaMP;
		
	}

	public void setServentiaMp(String serventiaMp) {
		if(serventiaMp!=null && serventiaMp.length()>0){
			ServentiaMp = serventiaMp;
		}
	}
	
	public String getServentiaMp() {
		return ServentiaMp;		
	}

	public void setId_ServentiaCargoMp(String id_serventiaCargoMp) {
		if(id_serventiaCargoMp !=null && id_serventiaCargoMp.trim().length() > 0){
			Id_ServentiaCargoMp = id_serventiaCargoMp;
		}
	}	
	public String getId_ServentiaCargoMp() {
		return Id_ServentiaCargoMp ;		
	}

	public void setServentiaCargoMp(String seventiaCargoMp) {
		if(seventiaCargoMp!=null && seventiaCargoMp.trim().length() > 0){
			ServentiaCargoMp = seventiaCargoMp;
		}
	}
	
	public void limparServentiaCargoMp() {
		Id_ServentiaCargoMp = "";
		ServentiaCargoMp = "";
	}
	
	public String getServentiaCargoMp() {
		return ServentiaCargoMp ;		
	}

	public boolean temPresidente() {
		if (Id_ServentiaCargoPresidente!=null && Id_ServentiaCargoPresidente.length()>0){
			return true; 
		}					
					
		return false;
	}

	public boolean temMpResponsavel() {
		if (Id_ServentiaCargoMp!=null && Id_ServentiaCargoMp.length()>0){
			return true;
		}
		
		return false;
	}

	public boolean temServentiaMp() {
		if (Id_ServentiaMP != null && Id_ServentiaMP.trim().length()>0){
			return true;
		}
		return false;
	}

	public boolean isPendenteAssinatura() {
		return ehGuardarParaAssinar;
	}

	public void setPendenteAssinatura(boolean ehGuardarParaAssinar) {
		this.ehGuardarParaAssinar = ehGuardarParaAssinar;
	}

	public void LimparIndicadorDePreAnalise() {
		this.ehPreAnalise = "N";		
	}
		
	public boolean isRetiradaDePauta(){
		int statusCodigo = Funcoes.StringToInt(getAudienciaStatusCodigo());	
		if( statusCodigo == AudienciaProcessoStatusDt.REMARCADA  || statusCodigo == AudienciaProcessoStatusDt.DESMARCAR_PAUTA || statusCodigo == AudienciaProcessoStatusDt.RETIRAR_PAUTA ){
			return true;
		}else return false;		
	}

	public String getIdProximaAudiencia() {
		return idProximaAudiencia;
	}

	public void setIdProximaAudiencia(String idProximaAudiencia) {
		if(idProximaAudiencia != null) {
			this.idProximaAudiencia = idProximaAudiencia;
		}
	}

	public boolean isEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(boolean encaminhamento) {
		this.encaminhamento = encaminhamento;
	}

	public boolean isVerificarProcesso() {
		return verificarProcesso;
	}

	public void setVerificarProcesso(boolean verificarProcesso) {
		this.verificarProcesso = verificarProcesso;
	}
 	
	// jvosantos - 03/06/2019 17:16 - Adicionar variavel para saber se é apreciados
	public boolean isApreciados() {
		return isApreciados;
	}

	public void setApreciados(boolean isApreciados) {
		this.isApreciados = isApreciados;
	}

	public boolean isMarcarDataAgendada() {
		return isMarcarDataAgendada;
	}

	public void setMarcarDataAgendada(boolean isMarcarDataAgendada) {
		this.isMarcarDataAgendada = isMarcarDataAgendada;
	}
	
	public boolean isRelatorio() {
		return this.getId_Relatorio() != null && !this.getId_Relatorio().equals("");
	}	
	
	public String getIdPendenciaVotoRelator() {
		if (getAudienciaDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRelator() != null) {
			return getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRelator();
		}		
		return "";		
	}
	
	public String getIdPendenciaVotoRedator() {
		if (getAudienciaDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRedator() != null) {
			return getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRedator();
		}		
		return "";	
	}
	
	public String getIdPendenciaEmentaRelator() {
		if (getAudienciaDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRelator() != null) {
			return getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRelator();
		}		
		return "";		
	}
	
	public String getIdPendenciaEmentaRedator() {
		if (getAudienciaDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt() != null &&
			getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRedator() != null) {
			return getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRedator();
		}		
		return "";	
	}
	
	public void setId_PendenciaVotoGerada(String Id_PendenciaVotoGerada) {
		this.Id_PendenciaVotoGerada = Id_PendenciaVotoGerada;		
	} 
	
	public String getId_PendenciaVotoGerada() {
		return this.Id_PendenciaVotoGerada;		
	} 
	
	public void setId_PendenciaEmentaGerada(String Id_PendenciaEmentaGerada) {
		this.Id_PendenciaEmentaGerada = Id_PendenciaEmentaGerada;		
	} 
	
	public String getId_PendenciaEmentaGerada() {
		return this.Id_PendenciaEmentaGerada;		
	}

	public String getId_ServentiaCargoVotoEmentaGerada() {
		return Id_ServentiaCargoVotoEmentaGerada;
	}

	public void setId_ServentiaCargoVotoEmentaGerada(String id_ServentiaCargoVotoEmentaGerada) {
		Id_ServentiaCargoVotoEmentaGerada = id_ServentiaCargoVotoEmentaGerada;
	} 
}