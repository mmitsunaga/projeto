package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe criada para auxiliar na movimentação de processos
 * @author msapaula
 * 13/01/2010 14:06:40
 */
public class MovimentacaoProcessoDt extends MovimentacaoDt {

	private static final long serialVersionUID = -2223676092268229654L;

	public static final int CodigoPermissao = 269;
	public static final int CodigoPermissaoGerarPendencias = 480;
	public static final int CodigoPermissaoSalvarEvento = 414;

	private boolean isMultiplo;

	// Variáveis auxiliares na movimentação de processos
	private String id_Classificador;
	private String classificador;
	private String id_ClassificadorPendencia;
	private String classificadorPendencia;
	private List listaArquivos;
	private List listaPendenciasGerar;
	private List listaPendenciaTipos;
	private List listaProcessos;

	// Varíaveis para auxiliar na inserção de arquivos
	private String id_ArquivoTipo;
	private String arquivoTipo;
	private String id_Modelo;
	private String modelo;
	private String textoEditor;

	// Passos para auxiliar na movimentação
	private String passo1;
	private String passo2;
	private String passo3;
	
	//Atributo para definir se movimentação é decorrente de acesso de outra Serventia, nessa variável será guardado o tipo da pendência que originou o acesso de outra serventia para posteriores tratamentos
	private String acessoOutraServentia="";
	private String devolucaoPrecatoria="";
	private String complementoDevolucaoPrecatoria="";
	private String redirecionaOutraServentia="";
	private List listaTiposMovimentacaoConfigurado;
	private boolean ocultaTiposMovimentacao;
	private String idRedirecionaOutraServentia="";
	private boolean ocultaComplemento;

	public MovimentacaoProcessoDt() {
		limpar();
	}

	public void limpar() {
		super.limpar();
		id_Classificador = "";
		classificador = "";
		id_ClassificadorPendencia = "";
		classificadorPendencia = "";
		id_ArquivoTipo = "";
		arquivoTipo = "";
		id_Modelo = "";
		modelo = "";
		textoEditor = "";
		listaArquivos = null;
		listaPendenciasGerar = null;
		listaPendenciaTipos = null;
		listaProcessos = null;
		passo1 = "Passo 1";
		passo2 = "";
		passo3 = "";
		acessoOutraServentia = "";
		devolucaoPrecatoria = "";
		complementoDevolucaoPrecatoria = "";
		redirecionaOutraServentia = "";
		listaTiposMovimentacaoConfigurado = new ArrayList();
		setOcultaTiposMovimentacao(false);
		setOcultaComplemento(false);
		setIdRedirecionaOutraServentia("");
	}

	public void copiar(MovimentacaoProcessoDt objeto) {
		super.copiar(objeto);
		id_Classificador = objeto.getId_Classificador();
		classificador = objeto.getClassificador();
	}

	public List getListaPendenciaTipos() {
		return listaPendenciaTipos;
	}

	public void setListaPendenciaTipos(List listaPendenciaTipos) {
		this.listaPendenciaTipos = listaPendenciaTipos;
	}

	public String getId_Classificador() {
		return id_Classificador;
	}

	public void setId_Classificador(String id_Classificador) {
		if (id_Classificador != null) if (id_Classificador.equalsIgnoreCase("null")) {
			this.id_Classificador = "";
			this.classificador = "";
		} else if (!id_Classificador.equalsIgnoreCase("")) this.id_Classificador = id_Classificador;
	}

	public String getClassificador() {
		return classificador;
	}

	public void setClassificador(String classificador) {
		if (classificador != null) this.classificador = classificador;
	}
	
	public String getId_ClassificadorPendencia() {
		return id_ClassificadorPendencia;
	}

	public void setId_ClassificadorPendencia(String id_ClassificadorPendencia) {
		if (id_ClassificadorPendencia != null) if (id_ClassificadorPendencia.equalsIgnoreCase("null")) {
			this.id_ClassificadorPendencia = "";
			this.classificadorPendencia = "";
		} else if (!id_ClassificadorPendencia.equalsIgnoreCase("")) this.id_ClassificadorPendencia = id_ClassificadorPendencia;
	}

	public String getClassificadorPendencia() {
		return classificadorPendencia;
	}

	public void setClassificadorPendencia(String classificadorPendencia) {
		if (classificadorPendencia != null) this.classificadorPendencia = classificadorPendencia;
	}

	public List getListaArquivos() {
		return listaArquivos;
	}

	public String getId_ArquivoTipo() {
		return id_ArquivoTipo;
	}
	
	public boolean isContemArquivos(){
		if (listaArquivos != null && listaArquivos.size()>0) return true;
		return false;
	}
	
	public boolean isContemProcessos(){
		if (listaProcessos != null && listaProcessos.size()>0) return true;
		return false;
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
		return textoEditor;
	}

	public void setTextoEditor(String textoEditor) {
		if (textoEditor != null) this.textoEditor = textoEditor;
	}

	public List getListaPendenciasGerar() {
		return listaPendenciasGerar;
	}

	public void setListaPendenciasGerar(List listaPendenciasGerar) {
		this.listaPendenciasGerar = listaPendenciasGerar;
	}

	public void addPendenciasGerar(PendenciaDt pendencia) {
		if (listaPendenciasGerar == null) this.listaPendenciasGerar = new ArrayList();
		listaPendenciasGerar.add(pendencia);
	}
	/*
	 * Jesus
	 * Pego o id do primeiro processo da lista
	 * testando null e lista vazia
	 */
	public String getIdPrimeiroProcessoLista(){
		String id="";
		if (listaProcessos!=null && listaProcessos.size()>0){
			ProcessoDt proc = (ProcessoDt)listaProcessos.get(0);
			if(proc!=null) id = proc.getId();
		}
		return id;
	}
	/*
	 * Jesus
	 * Pego o primeiro processo da lista
	 * testando null e lista vazia
	 */
	public ProcessoDt getPrimeiroProcessoLista(){
		ProcessoDt proc = null;
		if (listaProcessos!=null && listaProcessos.size()>0){
			 proc = (ProcessoDt)listaProcessos.get(0);			
		}
		return proc;
	}
	public List getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(List listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public void addListaProcessos(ProcessoDt processo) {
		if (listaProcessos == null) this.listaProcessos = new ArrayList();
		this.listaProcessos.add(processo);
	}

	public void addListaArquivos(ArquivoDt arquivoDt) {
		if (listaArquivos == null) this.listaArquivos = new ArrayList();
		this.listaArquivos.add(arquivoDt);
	}

	public boolean isMultiplo() {
		return isMultiplo;
	}

	public void setMultiplo(boolean isMultiplo) {
		this.isMultiplo = isMultiplo;
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

	public String getAcessoOutraServentia() {
		return acessoOutraServentia;
	}

	public void setAcessoOutraServentia(String acessoOutraServentia) {
		if (acessoOutraServentia != null) this.acessoOutraServentia = acessoOutraServentia;
	}
	
	public String getDevolucaoPrecatoria() {
		return devolucaoPrecatoria;
	}

	public void setDevolucaoPrecatoria(String devolucaoPrecatoria) {
		if (devolucaoPrecatoria != null) this.devolucaoPrecatoria = devolucaoPrecatoria;
	}
	
	public String getComplementoDevolucaoPrecatoria() {
		return complementoDevolucaoPrecatoria;
	}

	public void setComplementoDevolucaoPrecatoria(String complementoDevolucaoPrecatoria) {
		if (complementoDevolucaoPrecatoria != null)
			this.complementoDevolucaoPrecatoria = complementoDevolucaoPrecatoria;
	}

	public String getRedirecionaOutraServentia() {
		return redirecionaOutraServentia;
	}

	public void setRedirecionaOutraServentia(String redirecionaOutraServentia) {
		if (redirecionaOutraServentia != null) this.redirecionaOutraServentia = redirecionaOutraServentia;
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

	public void setOcultaTiposMovimentacao(boolean ocultaTiposMovimentacao) {
		this.ocultaTiposMovimentacao = ocultaTiposMovimentacao;
	}

	public boolean isOcultaTiposMovimentacao() {
		return ocultaTiposMovimentacao;
	}

	public String getIdRedirecionaOutraServentia() {
		return idRedirecionaOutraServentia;
	}

	public void setIdRedirecionaOutraServentia(String idRedirecionaOutraServentia) {
		this.idRedirecionaOutraServentia = idRedirecionaOutraServentia;
	}
	
	public void copiarMovimentacaoDt(MovimentacaoDt movimentacaoDt, List<ProcessoDt> listaProcessos, List<ArquivoDt> listaArquivos, List<PendenciaDt> listaPendenciasGerar) {
		if(movimentacaoDt != null){
			this.setIpComputadorLog(movimentacaoDt.getIpComputadorLog());
			this.setListaProcessos(listaProcessos);
			this.setListaArquivos(listaArquivos);
			this.setListaPendenciasGerar(listaPendenciasGerar);
			this.setId_MovimentacaoTipo(movimentacaoDt.getId_MovimentacaoTipo());
			this.setMovimentacaoTipoCodigo(movimentacaoDt.getMovimentacaoTipoCodigo());
			this.setMovimentacaoTipo(movimentacaoDt.getMovimentacaoTipo());
			this.setComplemento(movimentacaoDt.getComplemento());
			this.setAcessoOutraServentia("");
			this.setMovimentacaoTipoCodigo(movimentacaoDt.getMovimentacaoTipoCodigo());
		}
	}

	public boolean isOcultaComplemento() {
		return ocultaComplemento;
	}

	public void setOcultaComplemento(boolean ocultaComplemento) {
		this.ocultaComplemento = ocultaComplemento;
	}
}
