package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.MensagemException;

/**
 * Dt criado para manipular os dados necessários na análise ou pré-análise de pendências
 * @author msapaula
 *
 */
public class AnalisePendenciaDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6110426964489069033L;

	public static final int CodigoPermissao = 561;

	//Varíaveis para auxiliar na análise de conclusões
	private String id_MovimentacaoTipo;
	private String movimentacaoTipo;
	private String complementoMovimentacao;

	private List listaArquivos; //Arquivos inseridos
	private List listaPendenciasFechar; //Pendências que serão fechadas

	//Varíaveis para auxiliar na inserção de arquivos
	private String id_ArquivoTipo;
	private String arquivoTipo;
	private String nomeArquivo;
	private String id_Modelo;
	private String modelo;
	private String textoEditor;

	private boolean multipla; //Define se análise é multipla

	private PendenciaDt pendenciaDt; //Armazena pendência analisada

	//Variáveis para auxiliar nos redirecionamentos
	private int fluxo; //Esta variável armazena a paginaAtual utilizada na consulta para que o retorno seja correto ao finalizar a ação
	private String id_PendenciaTipo; //Armazena o tipo da pendência escolhido pelo usuário na página inicial
	private String unidadeTrabalho; //Armazena a unidade de trabalho da pendência escolhido pelo usuário na página inicial
	private boolean preAnalise; //Armazena se a análise que está sendo efetuada provém de uma pré-analise

	//Variáveis para guardar informações da pré-análise
	private String usuarioPreAnalise;
	private String dataPreAnalise;

	//Filtros para consulta
	private String dataInicial;
	private String dataFinal;
	private String numeroProcesso;

	//Variável para guardar o arquivo da pré-analise
	private PendenciaArquivoDt arquivoPreAnalise;

	//Lista criada para armazenar o histórico da pendência
	private List historicoPendencia;

	//Passos para auxiliar na análise
	private String passo1;
	private String passo2;

	private String id_UsuarioLog;
	private String ipComputadorLog;
	
	private boolean pendenteAssinatura;
	
	private List listaTiposMovimentacaoConfigurado;
	
	private String julgadoMeritoProcessoPrincipal;
	
	//Varíaveis para auxiliar na inserção de arquivos Ementa
	private String id_ArquivoTipoEmenta;
	private String arquivoTipoEmenta;
	private String nomeArquivoEmenta;
	private String id_ModeloEmenta;
	private String modeloEmenta;
	private String textoEditorEmenta;	
	private String Id_PendenciaVotoGerada;
	private String Id_PendenciaEmentaGerada;
	private String Id_ServentiaCargoVotoEmentaGerada;
	
	//Variável para guardar o arquivo da pré-analise
	private PendenciaArquivoDt arquivoPreAnaliseEmenta;
	
	private ProcessoDt processoDt;
	
	private List listaPendenciasGeradas; //Pendências que serão fechadas

	public AnalisePendenciaDt() {
		limpar();
	}
	
	/*
	 *Jesus
	 *Teste para evitar null
	 *retorna numero processo
	 */
	public String getNumeroPrimeiroProcessoListaFechar(){
		String stTemp="";
		if (listaPendenciasFechar!=null && listaPendenciasFechar.size()>0){
			PendenciaDt dt = (PendenciaDt)listaPendenciasFechar.get(0);
			ProcessoDt proc = dt.getProcessoDt();
			if (proc!=null) stTemp = proc.getProcessoNumero();
		}
		if ((stTemp == null || stTemp.trim().length() == 0) && this.pendenciaDt != null && this.pendenciaDt.getProcessoNumero() != null) {
			stTemp = this.pendenciaDt.getProcessoNumero();
		}
		return stTemp;
	}

	/*
	 *Jesus
	 *Teste para evitar null
	 *retorna o processo dt
	 */
	public ProcessoDt getPrimeiroProcessoListaFechar(){
		ProcessoDt proc=null;
		if (listaPendenciasFechar!=null && listaPendenciasFechar.size()>0){
			PendenciaDt dt = (PendenciaDt)listaPendenciasFechar.get(0);
			proc = dt.getProcessoDt();			
		}
		return proc;
	}
	
	public PendenciaDt getPrimeiraPendenciaListaFechar(){
		if (listaPendenciasFechar!=null && listaPendenciasFechar.size()>0){
			return (PendenciaDt)listaPendenciasFechar.get(0);						
		}
		return null;
	}
	
	public String getClassificadorPrimeiraPendenciaListaFechar(){
		PendenciaDt pendenciaDt = getPrimeiraPendenciaListaFechar();
		if (pendenciaDt == null || pendenciaDt.getClassificador() == null) return "";
		return pendenciaDt.getClassificador();
	}
	
	public AnalisePendenciaDt(int fluxo, String tipoConclusao, String unidadeTrabalho) {
		limpar();
		this.fluxo = fluxo;
		this.id_PendenciaTipo = tipoConclusao;
		this.unidadeTrabalho = unidadeTrabalho;
	}

	public void limpar() {
		id_MovimentacaoTipo = "";
		movimentacaoTipo = "";
		complementoMovimentacao = "";
		id_ArquivoTipo = "";
		nomeArquivo = "";
		arquivoTipo = "";
		id_Modelo = "";
		modelo = "";
		textoEditor = "";
		usuarioPreAnalise = "";
		dataPreAnalise = "";
		dataInicial = "";
		dataFinal = "";
		numeroProcesso = "";
		passo1 = "Passo 1";
		passo2 = "";
		multipla = false;
		listaArquivos = null;
		listaPendenciasFechar = null;
		pendenteAssinatura= false;
		listaTiposMovimentacaoConfigurado = new ArrayList();
		julgadoMeritoProcessoPrincipal = "";
		id_ArquivoTipoEmenta = "";
		nomeArquivoEmenta = "";
		arquivoTipoEmenta = "";
		id_ModeloEmenta = "";
		modeloEmenta = "";
		textoEditorEmenta = "";
		listaPendenciasGeradas = null;
		Id_PendenciaVotoGerada = "";
		Id_PendenciaEmentaGerada = "";
		Id_ServentiaCargoVotoEmentaGerada = "";
	}

	public void copiar(AnalisePendenciaDt objeto) {
		id_MovimentacaoTipo = objeto.getId_MovimentacaoTipo();
		movimentacaoTipo = objeto.getMovimentacaoTipo();
		complementoMovimentacao = objeto.getComplementoMovimentacao();
		id_ArquivoTipo = objeto.getId_ArquivoTipo();
		nomeArquivo = objeto.getNomeArquivo();
		arquivoTipo = objeto.getArquivoTipo();
		id_Modelo = objeto.getId_Modelo();
		modelo = objeto.getModelo();
		textoEditor = objeto.getTextoEditor();
		usuarioPreAnalise = objeto.getUsuarioPreAnalise();
		dataPreAnalise = objeto.getDataPreAnalise();
		multipla = objeto.isMultipla();
		listaArquivos = objeto.getListaArquivos();
		listaPendenciasFechar = objeto.getListaPendenciasFechar();
		julgadoMeritoProcessoPrincipal = objeto.getJulgadoMeritoProcessoPrincipal();
		id_ArquivoTipoEmenta = objeto.getId_ArquivoTipoEmenta();
		nomeArquivoEmenta = objeto.getNomeArquivoEmenta();
		arquivoTipoEmenta = objeto.getArquivoTipoEmenta();
		id_ModeloEmenta = objeto.getId_ModeloEmenta();
		modeloEmenta = objeto.getModeloEmenta();
		textoEditorEmenta = objeto.getTextoEditorEmenta();
		processoDt = objeto.getProcessoDt();
		listaPendenciasGeradas = objeto.getListaPendenciasGeradas();
		Id_PendenciaVotoGerada = objeto.getId_PendenciaVotoGerada();
		Id_PendenciaEmentaGerada = objeto.getId_PendenciaEmentaGerada();
		Id_ServentiaCargoVotoEmentaGerada = objeto.getId_ServentiaCargoVotoEmentaGerada();
	}

	public List getListaArquivos() {
		return listaArquivos;
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

	public void setListaArquivos(List listaArquivos) {
		if (listaArquivos == null) this.listaArquivos = new ArrayList();
		this.listaArquivos = listaArquivos;
	}

	public String getTextoEditor() {
		
		String regex = "<body(.*?)>";
		String texto = this.textoEditor.replaceAll(regex, "");
		texto = texto.replaceAll("<\\/body>", "");
		return texto;
		 		
	}

	public void setTextoEditor(String textoEditor) {
		if (textoEditor != null) if (!textoEditor.equalsIgnoreCase("")) this.textoEditor = textoEditor.replaceAll("\r\n", "");
		else this.textoEditor = "";
	}

	public List getListaPendenciasFechar() {
		return listaPendenciasFechar;
	}

	public void setListaPendenciasFechar(List listaPendenciasFechar) {
		this.listaPendenciasFechar = listaPendenciasFechar;
	}

	public String getId_MovimentacaoTipo() {
		return id_MovimentacaoTipo;
	}

	public void setId_MovimentacaoTipo(String id_MovimentacaoTipo) {
		if (id_MovimentacaoTipo != null) this.id_MovimentacaoTipo = id_MovimentacaoTipo;
	}

	public String getMovimentacaoTipo() {
		return movimentacaoTipo;
	}

	public void setMovimentacaoTipo(String movimentacaoTipo) {
		if (movimentacaoTipo != null) this.movimentacaoTipo = movimentacaoTipo;
	}

	public void addPendenciasFechar(PendenciaDt pendencia) {
		if (listaPendenciasFechar == null) this.listaPendenciasFechar = new ArrayList();
		listaPendenciasFechar.add(pendencia);
	}

	public String getId_UsuarioLog() {
		return id_UsuarioLog;
	}

	public void setId_UsuarioLog(String id_UsuarioLog) {
		if (id_UsuarioLog != null) this.id_UsuarioLog = id_UsuarioLog;
	}

	public String getIpComputadorLog() {
		return ipComputadorLog;
	}

	public void setIpComputadorLog(String ipComputadorLog) {
		if (ipComputadorLog != null) this.ipComputadorLog = ipComputadorLog;
	}

	public boolean isMultipla() {
		return multipla;
	}

	public void setMultipla(boolean multipla) {
		this.multipla = multipla;
	}

	public int getFluxo() {
		return fluxo;
	}

	public void setFluxo(int fluxo) {
		this.fluxo = fluxo;
	}

	public boolean isTemPendencia(){
		if (pendenciaDt!=null) return true;
		else return false;
	}
	
	public PendenciaDt getPendenciaDt() {
		return pendenciaDt;
	}

	public void setPendenciaDt(PendenciaDt pendenciaDt) {
		this.pendenciaDt = pendenciaDt;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		if (numeroProcesso != null) if (numeroProcesso.equalsIgnoreCase("null")) {
			this.numeroProcesso = "";
		} else if (!numeroProcesso.equalsIgnoreCase("")) this.numeroProcesso = numeroProcesso;
	}

	public boolean isPreAnalise() {
		return preAnalise;
	}

	public void setPreAnalise(boolean preAnalise) {
		this.preAnalise = preAnalise;
	}

	public void setDataPreAnalise(String dataInsercao) {
		if (dataInsercao != null) dataPreAnalise = dataInsercao;
	}

	public String getDataPreAnalise() {
		return dataPreAnalise;
	}

	public String getUsuarioPreAnalise() {
		return usuarioPreAnalise;
	}

	public void setUsuarioPreAnalise(String usuarioPreAnalise) {
		if (usuarioPreAnalise != null) this.usuarioPreAnalise = usuarioPreAnalise;
	}

	public PendenciaArquivoDt getArquivoPreAnalise() {
		return arquivoPreAnalise;
	}

	public void setArquivoPreAnalise(PendenciaArquivoDt arquivoPreAnalise) {
		this.arquivoPreAnalise = arquivoPreAnalise;
	}

	public List getHistoricoPendencia() {
		return historicoPendencia;
	}

	public void setHistoricoPendencia(List historicoPendencia) {
		this.historicoPendencia = historicoPendencia;
	}

	public String getId_PendenciaTipo() {
		return id_PendenciaTipo;
	}

	public void setId_TipoPendencia(String id_pendenciatipo) {
		if (id_pendenciatipo != null) {
			if (id_pendenciatipo.equalsIgnoreCase("null") ||  id_pendenciatipo.equalsIgnoreCase("todas")) {		
				this.id_PendenciaTipo = "";
			} else if (!id_pendenciatipo.equalsIgnoreCase("")){
				this.id_PendenciaTipo = id_pendenciatipo;
			}
		}
	}
	
	public String getUnidadeTrabalho() {
		return unidadeTrabalho;
	}

	public void setUnidadeTrabalho(String unidadeTrabalho) {
		if (unidadeTrabalho != null) if (unidadeTrabalho.equalsIgnoreCase("null")) {
			this.unidadeTrabalho = "";
		} else if (!unidadeTrabalho.equalsIgnoreCase("")) this.unidadeTrabalho = unidadeTrabalho;
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

	public String getComplementoMovimentacao() {
		return complementoMovimentacao;
	}

	public void setComplementoMovimentacao(String complementoMovimentacao) {
		if (complementoMovimentacao != null) this.complementoMovimentacao = complementoMovimentacao;
	}

	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	

	public void setNomeArquivo(String nomeArquivo) {
		if (nomeArquivo != null) this.nomeArquivo = nomeArquivo;
	}
	
	public void setPendenteAssinatura(boolean pendenteAssinatura) {
		this.pendenteAssinatura = pendenteAssinatura;
	}

	public boolean isPendenteAssinatura() {
		return pendenteAssinatura;
	}
	
	public void setListaTiposMovimentacaoConfigurado(List listaTiposMovimentacaoConfigurado) {		
		this.listaTiposMovimentacaoConfigurado = listaTiposMovimentacaoConfigurado;
	}
	
	public List getListaTiposMovimentacaoConfigurado(){		
		return this.listaTiposMovimentacaoConfigurado;
	}
	
	public void addListaTiposMovimentacaoConfigurado(UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt) {	
		this.listaTiposMovimentacaoConfigurado.add(usuarioMovimentacaoTipoDt);
	}
	
	public String getId_ArquivoTipoEmenta() {
		return id_ArquivoTipoEmenta;
	}

	public String getJulgadoMeritoProcessoPrincipal() {
		return julgadoMeritoProcessoPrincipal;
	}

	public void setJulgadoMeritoProcessoPrincipal(String julgadoMeritoProcessoPrincipal) {
		//if (julgadoMeritoProcessoPrincipal != null) 
			this.julgadoMeritoProcessoPrincipal = julgadoMeritoProcessoPrincipal;
	}
	
	public void setId_ArquivoTipoEmenta(String id_ArquivoTipoEmenta) {
		if (id_ArquivoTipoEmenta != null) if (id_ArquivoTipoEmenta.equalsIgnoreCase("null")) {
			id_ArquivoTipoEmenta = "";
			arquivoTipoEmenta = "";
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
		return this.textoEditorEmenta;
	}

	public void setTextoEditorEmenta(String textoEditorEmenta) {
		if (textoEditorEmenta != null) if (!textoEditorEmenta.equalsIgnoreCase("")) this.textoEditorEmenta = textoEditorEmenta.replaceAll("\r\n", "");
		else this.textoEditorEmenta = "";
	}
	
	public String getNomeArquivoEmenta() {
		return nomeArquivoEmenta;
	}
	

	public void setNomeArquivoEmenta(String nomeArquivoEmenta) {
		if (nomeArquivoEmenta != null) this.nomeArquivoEmenta = nomeArquivoEmenta;
	}
	
	public PendenciaArquivoDt getArquivoPreAnaliseEmenta() {
		return arquivoPreAnaliseEmenta;
	}

	public void setArquivoPreAnaliseEmenta(PendenciaArquivoDt arquivoPreAnaliseEmenta) {
		this.arquivoPreAnaliseEmenta = arquivoPreAnaliseEmenta;
	}
	
	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}
	
	public PendenciaDt getPrimeiraPendenciaGerada(){
		PendenciaDt pendencia = null;
		if (listaPendenciasGeradas!=null && listaPendenciasGeradas.size()>0){
			pendencia = (PendenciaDt)listaPendenciasGeradas.get(0);			
		}
		return pendencia;
	}
	
	public List getListaPendenciasGeradas() {
		return listaPendenciasGeradas;
	}

	public void setListaPendenciasGeradas(List listaPendenciasGeradas) {
		this.listaPendenciasGeradas = listaPendenciasGeradas;
	}
	
	public void addPendenciasGeradas(PendenciaDt pendencia) {
		if (listaPendenciasGeradas == null) this.listaPendenciasGeradas = new ArrayList();
		listaPendenciasGeradas.add(pendencia);
	}
	
	public boolean isMesmaMovimentacaoTipo(String movimentacaoTipo) {
		if (id_MovimentacaoTipo != null && movimentacaoTipo!=null && movimentacaoTipo.equals(id_MovimentacaoTipo)) {
			return true;
		}
		return false;
	}
	
	public String getId_Pendencia() throws MensagemException {
		if (getArquivoPreAnalise()!= null && getArquivoPreAnalise().getId_Pendencia() != null){
			return getArquivoPreAnalise().getId_Pendencia();
		} else {
			throw new MensagemException("Existe inconsistência na Pré-Analise");
		}
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
