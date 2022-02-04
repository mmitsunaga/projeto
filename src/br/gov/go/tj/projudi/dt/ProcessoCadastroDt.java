package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Essa classe será utilizado no cadastro de processos.
 */
public class ProcessoCadastroDt extends ProcessoDt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3273277858936100343L;
	public static final int CodigoPermissaoProcessoCivel = 150; //Permissão para cadastro de processos cíveis
	public static final int CodigoPermissaoProcessoCriminal = 109; //Permissão para cadastro de processos criminais
	public static final int CodigoPermissaoProcessoSegundoGrau = 325; //Permissão para cadastro de processos de 2º grau
	
	// @proad
	public static final int CodigoPermissaoProcessoAdministrativo= 685; //Permissão para cadastro de processos administrativos
	
	
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
	
	//Variáveis para auxiliar na unificação da tela de cadastro de processos.
	private String grauProcesso;
	private String tipoProcesso;
	private String assistenciaProcesso;
	private String dependenciaProcesso;

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
	private boolean processoFisico; //Variável para definir se um processo é físico 

	//Passos para cadastramento
	private String passo1;
	private String passo2;
	private String passo3;
	
	private String processoNumeroFisico; //Armazenar o número do processo Físico
	private String id_ServentiaCargo; //Armazenar o id da serventia cargo (Juíz Responsável pelo processo)
	private String serventiaCargo; //Armazenar a descrição da serventia cargo (Juíz Responsável pelo processo)
	private String naturezaSPG; // Natureza SPG
	private boolean marcarConciliacao=false;
	
	public boolean isMarcarAudienciaConciliacao(){
		return marcarConciliacao;
	}

	public void limpar() {
		super.limpar();
		super.setSegredoJustica("false");
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
		mandarConcluso = false;
		processoDependente = false;
		processoFisico = false;
		passo1 = "Passo 1";
		passo2 = "";
		passo3 = "";
		processoNumeroFisico = "";
		id_ServentiaCargo = "";
		serventiaCargo = "";
		naturezaSPG = "";
		grauProcesso = "";
		tipoProcesso = "";
		assistenciaProcesso = "";
		dependenciaProcesso = "";
	}
	
	public void limparProcessoTrocarCustaTipo() {
		super.limpar();
		super.setSegredoJustica("false");
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
		mandarConcluso = false;
		processoDependente = false;
		processoFisico = false;
		passo1 = "Passo 1";
		passo2 = "";
		passo3 = "";
		processoNumeroFisico = "";
		id_ServentiaCargo = "";
		serventiaCargo = "";
		naturezaSPG = "";
		dependenciaProcesso = "";
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
			super.setId_AreaDistribuicao("null");
			super.setAreaDistribuicao("null");
		} else if (!id_AreaDistribuicao.equalsIgnoreCase("")) super.setId_AreaDistribuicao(id_AreaDistribuicao);
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
		else this.mandarConcluso = false; //Por padrão deve mandar concluso
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
		return  (processoDependenteDt !=null);
	}
	
	public boolean isProcessoDependenteVariavel() {
		return  processoDependente;
	}

	public void setProcessoDependente(boolean processoDependente) {
		this.processoDependente = processoDependente;
	}
	
	public boolean isProcessoFisico() {
		return processoFisico;
	}

	public void setProcessoFisico(boolean processoFisico) {
		this.processoFisico = processoFisico;
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
	public String getId_ServentiaProcessoDependente(){
		if (processoDependenteDt!=null){
			return processoDependenteDt.getId_Serventia();
		}
		return "";
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

		if (getListaOutrasPartes() == null) setListaOutrasPartes(new ArrayList());
		this.getListaOutrasPartes().add(parteDt);
	}

	public void addListaComunicantes(ProcessoParteDt parteDt) {
		parteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.COMUNICANTE));

		if (getListaOutrasPartes() == null) setListaOutrasPartes(new ArrayList());
		this.getListaOutrasPartes().add(parteDt);
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
		this.setId_Serventia(serventiaDt.getId());
		this.setServentia(serventiaDt.getServentia());
		this.serventiaDt = serventiaDt;
	}

	/**
	 * Sobrescrevendo método para limpar também o "ProcessoPrioridadeCodigo" quando limpar o Id e Descrição
	 */
	public void setId_ProcessoPrioridade(String valor) {
		if (valor != null) if (valor.equalsIgnoreCase("null")) {
			super.setId_ProcessoPrioridade("null");
			super.setProcessoPrioridadeCodigo("");
		} else if (!valor.equalsIgnoreCase("")) super.setId_ProcessoPrioridade(valor);
	}

	public String getId_ServentiaSubTipo() {
		return id_ServentiaSubTipo;
	}

	public void setId_ServentiaSubTipo(String id_ServentiaSubTipo) {
		if (id_ServentiaSubTipo != null) this.id_ServentiaSubTipo = id_ServentiaSubTipo;
	}

	public void setProcessoNumeroFisico(String processoNumeroFisico) {
		if (processoNumeroFisico!=null && processoNumeroFisico.trim() != "") this.processoNumeroFisico = Funcoes.completarZeros(processoNumeroFisico,25);
	}
	
	public void limpeProcessoNumeroFisico(){
		this.processoNumeroFisico = "";
	}

	public String getProcessoNumeroFisico() {		
		return this.processoNumeroFisico;
	}
	
	/**
	 * Extrai do Número do Processo Físico a composição do processo, sendo:
	 * ano, número do processo, dígito do processo e código do Fórum. 
	 * @author Márcio Gomes.
	 */
	public void atualizeNumeroProcessoFisico(){
		if (this.processoNumeroFisico == null) return;
		if (this.processoNumeroFisico.length() != 25) return;
		
		String ano = this.processoNumeroFisico.substring(11, 15);
		String numero = this.processoNumeroFisico.substring(0, 7);
		String digito = this.processoNumeroFisico.substring(8, 10);
		
		super.setAnoProcessoNumero(ano + Funcoes.completarZeros(numero,7) + digito);
		super.setForumCodigo(this.getNumeroProcessoFisicoForum());
	}

	//Extrai do Número do processo físico o código do fórum.
	private String getNumeroProcessoFisicoForum() {
		return String.valueOf(Funcoes.StringToLong(this.processoNumeroFisico.substring(21, 25)));
	}

	public void setId_ServentiaCargo(String id_ServentiaCargo) {
		if(id_ServentiaCargo!=null) this.id_ServentiaCargo = id_ServentiaCargo;
	}

	public String getId_ServentiaCargo() {
		return this.id_ServentiaCargo;
	}

	public void setServentiaCargo(String serventiaCargo) {
		if(serventiaCargo!=null) this.serventiaCargo = serventiaCargo;
	}

	public String getServentiaCargo() {
		return this.serventiaCargo;
	}
	
	public void setNaturezaSPG(String naturezaSPG) {
		if(naturezaSPG!=null) this.naturezaSPG = naturezaSPG;
	}

	public String getNaturezaSPG() {
		return this.naturezaSPG;
	}
	
	public String getId_ServentiaOrigemRecursoProcessoDependente() {
		String retorno = "";
		if (processoDependenteDt != null)
			retorno = processoDependenteDt.getId_ServentiaOrigemRecurso();
		return retorno;
	}

	public boolean temServentiaOrigemRecursoProcessoDependente() {
		if (processoDependenteDt != null && processoDependenteDt.getId_ServentiaOrigemRecurso() != null && processoDependenteDt.getId_ServentiaOrigemRecurso().length() > 0){
			return true;	
		}
		return false;
	}

	public void setDataFato(String dataFato) {
		if(getProcessoCriminalDt() != null) {
			getProcessoCriminalDt().setDataFato(dataFato);
		} else {
			setProcessoCriminalDt(new ProcessoCriminalDt());
			getProcessoCriminalDt().setDataFato(dataFato);
		}
	}
	public String getDataFato() {
		String retorno = "";
		if(getProcessoCriminalDt() != null) {
			retorno = getProcessoCriminalDt().getDataFato();
		}
		return retorno;
	}

	public void setMarcarAudienciaConciliacao(String marcar) {
		if (marcar != null) this.marcarConciliacao = Funcoes.StringToBoolean(marcar);
		else this.marcarConciliacao = false; //Por padrão deve mandar concluso
		
	}
	
	 public boolean isProcessoDependenteDt(){
			if (this.getProcessoDependenteDt().getId()!=null && this.getProcessoDependenteDt().getId().length() > 0){
				return true;
			}
			
			return false;
	}

	public String getGrauProcesso() {
		return grauProcesso;
	}

	public void setGrauProcesso(String grauProcesso) {
		if(grauProcesso!=null) this.grauProcesso = grauProcesso;
	}

	public String getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(String tipoProcesso) {
		if(tipoProcesso!=null) this.tipoProcesso = tipoProcesso;
	}

	public String getAssistenciaProcesso() {
		return assistenciaProcesso;
	}

	public void setAssistenciaProcesso(String assistenciaProcesso) {
		if(assistenciaProcesso!=null) this.assistenciaProcesso = assistenciaProcesso;
	}

	public String getDependenciaProcesso() {
		return dependenciaProcesso;
	}

	public void setDependenciaProcesso(String dependenciaProcesso) {
		if(dependenciaProcesso!=null) this.dependenciaProcesso = dependenciaProcesso;
	}
	
	public boolean isMesmoGrauJurisdicao() {
		if(processoDependenteDt != null) {
			if(Funcoes.StringToInt(processoDependenteDt.getServentiaTipoCodigo(),-3) == ServentiaTipoDt.SEGUNDO_GRAU && getGrauProcesso().equals("2")) {
				return true;
			} else if(Funcoes.StringToInt(processoDependenteDt.getServentiaTipoCodigo(),-3) == ServentiaTipoDt.VARA && getGrauProcesso().equals("1")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isProcessoMesmaArea() {
		if(processoDependenteDt != null) {
			if(processoDependenteDt.getAreaCodigo().equals(AreaDt.CIVEL) && getTipoProcesso().equals("1")) {
				return true;
			} else if(processoDependenteDt.getAreaCodigo().equals(AreaDt.CRIMINAL) && getTipoProcesso().equals("2")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isAssistencia() {
		String assistencia = getAssistenciaProcesso();
		if (assistencia != null && (assistencia.equalsIgnoreCase("true") || assistencia.equals("1"))){
			return true;
		}
		return false;
	}
	
	public boolean isIsento() {
		String isento = getAssistenciaProcesso();
		if (isento != null && (isento.equalsIgnoreCase("true") || isento.equals("3"))){
			return true;
		}
		return false;
	}
	
}
