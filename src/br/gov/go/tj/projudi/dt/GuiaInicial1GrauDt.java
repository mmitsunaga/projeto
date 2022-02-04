package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

/**
 * Essa classe será utilizada para o cadastro de guias iniciais
 */
public class GuiaInicial1GrauDt extends GuiaEmissaoDt{

	private static final long serialVersionUID = -712525402496437479L;
	
	public static final int CodigoPermissaoGuiaInicial1Grau = 598;
	
	private String id_Comarca;
	private String comarca;
	private String comarcaCodigo;

	private String areaDistribuicaoCodigo;
	private String id_ServentiaSubTipo;
	private String id_Assunto;
	private String assunto;

	private String id_ArquivoTipo;
	private String arquivoTipo;
	private String id_Modelo;
	private String modelo;
	private String textoEditor;
	private String codigoInstituicao; //Variável para auxiliar na importação de processos

	private ProcessoDt processoDependenteDt; //Armazenar processo dependente
	private AudienciaDt audienciaDt; //Armazenar audiencia marcada 
	private ServentiaDt serventiaDt; //Serventia para a qual processo foi distribuído

	private List listaArquivos; //Arquivos inseridos no processo
	private List listaAdvogados; //Advogados no processo
	private List listaPreventos; //Possíveis preventos do processo

	private List listaPartesIntimadas; //Partes que saem intimadas no momento do cadastro do processo (ex.delegacia)

	private boolean marcarAudiencia; //Variável para definir se audiência deve ser marcada automaticamente no cadastro
	private boolean mandarConcluso; //Variável para definir se processo deve ser remetido concluso ao cadastrar
	private boolean processoDependente; //Variável para definir se um processo é dependente de outro

	//Passos para cadastramento
	private String passo1;
	private String passo2;
	private String passo3;

	public void limpar() {
		super.limpar();
		//super.setSegredoJustica("false");
		id_Comarca = "";
		comarca = "";
		comarcaCodigo = "";
		areaDistribuicaoCodigo = "";
		id_ServentiaSubTipo = "";
		id_Assunto = "";
		assunto = "";
		id_ArquivoTipo = "";
		arquivoTipo = "";
		id_Modelo = "";
		modelo = "";
		textoEditor = "";
		codigoInstituicao = "";
		processoDependenteDt = null;
		audienciaDt = null;
		listaArquivos = null;
		listaAdvogados = null;
		listaPreventos = null;
		marcarAudiencia = true;
		mandarConcluso = true;
		processoDependente = false;
		passo1 = "Passo 1";
		passo2 = "";
		passo3 = "";
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		if (comarca != null) if (comarca.equalsIgnoreCase("null")) this.comarca = "";
		else if (!comarca.equalsIgnoreCase("")) this.comarca = comarca;
	}

	public String getId_Comarca() {
		return id_Comarca;
	}

	public void setId_Comarca(String id_Comarca) {
		if (id_Comarca != null) if (id_Comarca.equalsIgnoreCase("null")) {
			this.id_Comarca = "";
			this.comarca = "";
		} else if (!id_Comarca.equalsIgnoreCase("")) this.id_Comarca = id_Comarca;
	}

	public String getComarcaCodigo() {
		return comarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		if (comarcaCodigo != null) this.comarcaCodigo = comarcaCodigo;
	}

	/**
	 * Sobrescrevendo método do Gen
	 */
	public void setId_AreaDistribuicao(String id_AreaDistribuicao) {
		if (id_AreaDistribuicao != null) if (id_AreaDistribuicao.equalsIgnoreCase("null")) {
			//super.setId_AreaDistribuicao("");
			//super.setAreaDistribuicao("");
		} //else if (!id_AreaDistribuicao.equalsIgnoreCase("")) super.setId_AreaDistribuicao(id_AreaDistribuicao);
	}

	public boolean MarcarAudiencia() {
		return marcarAudiencia;
	}

	public void setMarcarAudiencia(String naoMarcarAudiencia) {
		if (naoMarcarAudiencia != null) this.marcarAudiencia = Funcoes.StringToBoolean(naoMarcarAudiencia);
		else this.marcarAudiencia = true; //Por padrão deve marcar audiência
	}

	public boolean MandarConcluso() {
		return mandarConcluso;
	}

	public void setMandarConcluso(String naoMandarConcluso) {
		if (naoMandarConcluso != null) this.mandarConcluso = Funcoes.StringToBoolean(naoMandarConcluso);
		else this.mandarConcluso = true; //Por padrão deve mandar concluso
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
		if (textoEditor != null) if (!textoEditor.equalsIgnoreCase("")) this.textoEditor = textoEditor.replaceAll("\r\n", "");
		else this.textoEditor = "";
	}

	public String getId_Assunto() {
		return id_Assunto;
	}

	public void setId_Assunto(String id_Assunto) {
		if (id_Assunto != null) if (id_Assunto.equalsIgnoreCase("null")) {
			this.id_Assunto = "";
			this.assunto = "";
		} else if (!id_Assunto.equalsIgnoreCase("")) this.id_Assunto = id_Assunto;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		if (assunto != null) if (assunto.equalsIgnoreCase("null")) this.assunto = "";
		else if (!assunto.equalsIgnoreCase("")) this.assunto = assunto;
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

	public boolean isProcessoDependente() {
		return processoDependente;
	}

	public void setProcessoDependente(boolean processoDependente) {
		this.processoDependente = processoDependente;
	}

	public List getListaArquivos() {
		return listaArquivos;
	}

	public void setListaArquivos(List listaArquivos) {
		this.listaArquivos = listaArquivos;
	}

	public void addListaArquivos(ArquivoDt dt) {
		if (listaArquivos == null) listaArquivos = new ArrayList();
		this.listaArquivos.add(dt);
	}

	public List getListaAdvogados() {
		return listaAdvogados;
	}

	public void setListaAdvogados(List listaAdvogados) {
		this.listaAdvogados = listaAdvogados;
	}

	public void addListaAdvogados(UsuarioServentiaOabDt dt) {
		if (listaAdvogados == null) listaAdvogados = new ArrayList();
		this.listaAdvogados.add(dt);
	}

	public ProcessoDt getProcessoDependenteDt() {
		return processoDependenteDt;
	}

	public void setProcessoDependenteDt(ProcessoDt processoDependenteDt) {
		this.processoDependenteDt = processoDependenteDt;
	}

	public AudienciaDt getAudienciaDt() {
		return audienciaDt;
	}

	public void setAudienciaDt(AudienciaDt audienciaDt) {
		this.audienciaDt = audienciaDt;
	}

	public List getListaPreventos() {
		return listaPreventos;
	}

	public void setListaPreventos(List listaPreventos) {
		this.listaPreventos = listaPreventos;
	}

	public void addListaTestemunhas(ProcessoParteDt parteDt) {
		parteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.TESTEMUNHA));

		//if (getListaOutrasPartes() == null) setListaOutrasPartes(new ArrayList());
		//this.getListaOutrasPartes().add(parteDt);
	}

	public void addListaComunicantes(ProcessoParteDt parteDt) {
		parteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.COMUNICANTE));

		//if (getListaOutrasPartes() == null) setListaOutrasPartes(new ArrayList());
		//this.getListaOutrasPartes().add(parteDt);
	}

	public String getCodigoInstituicao() {
		return codigoInstituicao;
	}

	public void setCodigoInstituicao(String codigoInstituicao) {
		this.codigoInstituicao = codigoInstituicao;
	}

	public String getAreaDistribuicaoCodigo() {
		return areaDistribuicaoCodigo;
	}

	public void setAreaDistribuicaoCodigo(String areaDistribuicaoCodigo) {
		this.areaDistribuicaoCodigo = areaDistribuicaoCodigo;
	}

	public List getListaPartesIntimadas() {
		return listaPartesIntimadas;
	}

	public void setListaPartesIntimadas(List listaPartesIntimadas) {
		this.listaPartesIntimadas = listaPartesIntimadas;
	}

	public void addListaPartesIntimadas(ProcessoParteDt dt) {
		if (listaPartesIntimadas == null) listaPartesIntimadas = new ArrayList();
		this.listaPartesIntimadas.add(dt);
	}

	public ServentiaDt getServentiaDt() {
		return serventiaDt;
	}

	public void setServentiaDt(ServentiaDt serventiaDt) {
		this.serventiaDt = serventiaDt;
	}

	/**
	 * Sobrescrevendo método para limpar também o "ProcessoPrioridadeCodigo" quando limpar o Id e Descrição
	 */
	public void setId_ProcessoPrioridade(String valor) {
		if (valor != null) if (valor.equalsIgnoreCase("null")) {
			//super.setId_ProcessoPrioridade("null");
			//super.setProcessoPrioridadeCodigo("");
		} //else if (!valor.equalsIgnoreCase("")) super.setId_ProcessoPrioridade(valor);
	}

	public String getId_ServentiaSubTipo() {
		return id_ServentiaSubTipo;
	}

	public void setId_ServentiaSubTipo(String id_ServentiaSubTipo) {
		if (id_ServentiaSubTipo != null) this.id_ServentiaSubTipo = id_ServentiaSubTipo;
	}

}
