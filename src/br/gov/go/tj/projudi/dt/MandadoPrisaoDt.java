package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class MandadoPrisaoDt extends MandadoPrisaoDtGen{

	private static final long serialVersionUID = 57259988819872035L;
	public static final int CodigoPermissao=740;

//	private boolean boRecaptura;
	private boolean boSigilo;
	private String processoNumeroCompleto;
	private String tempoPenaAno;
	private String tempoPenaMes;
	private String tempoPenaDia;
	private String tempoPenaTotalDias;
	private String tempoPenaTotalAnos;
	private List listaMandadoPrisaoOrigem;
	private List listaMandadoPrisaoStatus;
	private List listaPrisaoTipo;
	private List listaRegime;
	private List listaArquivo;
//	private String assuntoDelitoPrincipal;
	private String mandadoPrisaoOrigemCodigo;
	private String mandadoPrisaoStatusCodigo;
	private String prisaoTipoCodigo;
	private String dataEmissao;
	private String dataImpressao;
	private String dataPrisao;
	private String idUsuarioServentiaEmissao;
	private String idUsuarioServentiaExpedicao;
	private String idUsuarioServentiaImpressao;
	private String idUsuarioServentiaCumprimento;
	private String forumCodigo;
	private String enderecoCompleto;
	private String idEndereco;
	
	// Varíaveis para auxiliar na inserção de arquivos
	private String idArquivoTipo;
	private String arquivoTipo;
	private String textoEditor;
	private String idModelo;
	private String modelo;
	
	private ProcessoPartePrisaoDt processoPartePrisaoDt;
	
	public MandadoPrisaoDt(){
		this.limpar();
	}

	public void limpar(){
		super.limpar();
//		boRecaptura = false;
		boSigilo = false;
		processoNumeroCompleto = "";
		tempoPenaAno = "";
		tempoPenaMes = "";
		tempoPenaDia = "";
		tempoPenaTotalDias = "";
		tempoPenaTotalAnos = "";
		listaMandadoPrisaoOrigem = null;
		listaMandadoPrisaoStatus = null;
		listaPrisaoTipo = null;
		listaRegime = null;
		listaArquivo = null;
//		assuntoDelitoPrincipal = "";
		mandadoPrisaoOrigemCodigo = "";
		prisaoTipoCodigo = "";
		dataEmissao = "";
		idUsuarioServentiaEmissao = "";
		idUsuarioServentiaExpedicao = "";
		mandadoPrisaoStatusCodigo = "";
		forumCodigo = "";
		enderecoCompleto = "";
		idEndereco = "";
		idArquivoTipo = "";
		arquivoTipo = "";
		textoEditor = "";
		dataImpressao = "";
		dataPrisao = "";
		idUsuarioServentiaImpressao = "";
		idUsuarioServentiaCumprimento = "";
		idModelo = "";
		modelo = "";
		setProcessoPartePrisaoDt(new ProcessoPartePrisaoDt());
	}
	
	public String getForumCodigo() {
		return forumCodigo;
	}

	public void setForumCodigo(String forumCodigo) {
		this.forumCodigo = forumCodigo;
	}

//	public boolean isRecaptura() {
//		return boRecaptura;
//	}
//
//    public void setRecaptura(String valor) {
//    	if (valor != null){
//			super.setRecaptura(valor);
//	        if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("t") || valor.equalsIgnoreCase("1"))
//	            boRecaptura = true;
//	        else
//	            boRecaptura = false;
//    	}
//    }

	public boolean isSigilo() {
		return boSigilo;
	}

    public void setSigilo(String valor) {
    	if (valor != null){
			super.setSigilo(valor);
	        if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("t") || valor.equalsIgnoreCase("1"))
	            boSigilo = true;
	        else
	            boSigilo = false;
    	}
    }
    
	public String getProcessoNumeroCompleto() {
		return processoNumeroCompleto;
	}

	public void setProcessoNumeroCompleto(String processoNumeroCompleto) {
		this.processoNumeroCompleto = processoNumeroCompleto;
	}

	public String getTempoPenaAno() {
		return tempoPenaAno;
	}

	public void setTempoPenaAno(String tempoPenaAno) {
		if (tempoPenaAno != null) this.tempoPenaAno = tempoPenaAno;
	}

	public String getTempoPenaMes() {
		return tempoPenaMes;
	}

	public void setTempoPenaMes(String tempoPenaMes) {
		if (tempoPenaMes != null) this.tempoPenaMes = tempoPenaMes;
	}

	public String getTempoPenaDia() {
		return tempoPenaDia;
	}

	public void setTempoPenaDia(String tempoPenaDia) {
		if (tempoPenaDia != null) this.tempoPenaDia = tempoPenaDia;
	}

	public String getTempoPenaTotalDias() {
		return tempoPenaTotalDias;
	}

	public void setTempoPenaTotalDias(String tempoPenaTotalDias) {
		this.tempoPenaTotalDias = tempoPenaTotalDias;
		super.setPenaImposta(tempoPenaTotalDias);
		setTempoPenaTotalAnos(tempoPenaTotalDias);
	}
	
	public String getTempoPenaTotalAnos() {
		return tempoPenaTotalAnos;
	}

	public void setTempoPenaTotalAnos(String tempoPenaTotalAnos) {
		this.tempoPenaTotalAnos = tempoPenaTotalAnos;
		if (tempoPenaTotalAnos.length() > 0) this.tempoPenaTotalAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoPenaTotalAnos));
		else this.tempoPenaTotalAnos = "";
	}
	
	public List getListaMandadoPrisaoOrigem() {
		return listaMandadoPrisaoOrigem;
	}

	public void setListaMandadoPrisaoOrigem(List listaMandadoPrisaoOrigem) {
		this.listaMandadoPrisaoOrigem = listaMandadoPrisaoOrigem;
	}
	
	public void addListMandadoPrisaoOrigem(MandadoPrisaoOrigemDt dt) {
		if (listaMandadoPrisaoOrigem == null) listaMandadoPrisaoOrigem = new ArrayList();
		this.listaMandadoPrisaoOrigem.add(dt);
	}

//	public String getAssuntoDelitoPrincipal() {
//		return assuntoDelitoPrincipal;
//	}
//
//	public void setAssuntoDelitoPrincipal(String assuntoDelitoPrincipal) {
//		if (assuntoDelitoPrincipal != null) this.assuntoDelitoPrincipal = assuntoDelitoPrincipal;
//	}
	
	public List getListaMandadoPrisaoStatus() {
		return listaMandadoPrisaoStatus;
	}

	public void setListaMandadoPrisaoStatus(List listaMandadoPrisaoStatus) {
		this.listaMandadoPrisaoStatus = listaMandadoPrisaoStatus;
	}
	
	public void addListMandadoPrisaoStatus(MandadoPrisaoStatusDt dt) {
		if (listaMandadoPrisaoStatus == null) listaMandadoPrisaoStatus = new ArrayList();
		this.listaMandadoPrisaoStatus.add(dt);
	}
	
	public List getListaPrisaoTipo() {
		return listaPrisaoTipo;
	}

	public void setListaPrisaoTipo(List listaPrisaoTipo) {
		this.listaPrisaoTipo = listaPrisaoTipo;
	}
	
	public void addListPrisaoTipo(PrisaoTipoDt dt) {
		if (listaPrisaoTipo == null) listaPrisaoTipo = new ArrayList();
		this.listaPrisaoTipo.add(dt);
	}
	
	public List getListaRegime() {
		return listaRegime;
	}

	public void setListaRegime(List listaRegime) {
		this.listaRegime = listaRegime;
	}
	
	public void addListRegime(RegimeExecucaoDt dt) {
		if (listaRegime == null) listaRegime = new ArrayList();
		this.listaRegime.add(dt);
	}

	public List getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List listaArquivo) {
		this.listaArquivo = listaArquivo;
	}
	
	public void addListArquivo(RegimeExecucaoDt dt) {
		if (listaArquivo == null) listaArquivo = new ArrayList();
		this.listaArquivo.add(dt);
	}
	
	public String getMandadoPrisaoOrigemCodigo() {
		return mandadoPrisaoOrigemCodigo;
	}

	public void setMandadoPrisaoOrigemCodigo(String mandadoPrisaoOrigemCodigo) {
		if (mandadoPrisaoOrigemCodigo != null) this.mandadoPrisaoOrigemCodigo = mandadoPrisaoOrigemCodigo;
	}

	public String getPrisaoTipoCodigo() {
		return prisaoTipoCodigo;
	}

	public void setPrisaoTipoCodigo(String prisaoTipoCodigo) {
		if (prisaoTipoCodigo != null) this.prisaoTipoCodigo = prisaoTipoCodigo;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getIdUsuarioServentiaEmissao() {
		return idUsuarioServentiaEmissao;
	}

	public void setIdUsuarioServentiaEmissao(String idUsuarioServentiaEmissao) {
		this.idUsuarioServentiaEmissao = idUsuarioServentiaEmissao;
	}

	public String getIdUsuarioServentiaExpedicao() {
		return idUsuarioServentiaExpedicao;
	}

	public void setIdUsuarioServentiaExpedicao(String idUsuarioServentiaExpedicao) {
		this.idUsuarioServentiaExpedicao = idUsuarioServentiaExpedicao;
	}

	public String getMandadoPrisaoStatusCodigo() {
		return mandadoPrisaoStatusCodigo;
	}

	public void setMandadoPrisaoStatusCodigo(String mandadoPrisaoStatusCodigo) {
		if (mandadoPrisaoStatusCodigo != null) this.mandadoPrisaoStatusCodigo = mandadoPrisaoStatusCodigo;
	}

	public String getEnderecoCompleto() {
		return enderecoCompleto;
	}

	public void setEnderecoCompleto(String enderecoCompleto) {
		this.enderecoCompleto = enderecoCompleto;
	}

	public String getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(String idEndereco) {
		this.idEndereco = idEndereco;
	}

	public String getIdArquivoTipo() {
		return idArquivoTipo;
	}

	public void setIdArquivoTipo(String idArquivoTipo) {
		if (idArquivoTipo != null) if (idArquivoTipo.equalsIgnoreCase("null")) {
			this.idArquivoTipo = "";
			this.arquivoTipo = "";
		} else if (!idArquivoTipo.equalsIgnoreCase("")) this.idArquivoTipo = idArquivoTipo;
	}

	public String getArquivoTipo() {
		return arquivoTipo;
	}

	public void setArquivoTipo(String arquivoTipo) {
		if (arquivoTipo != null) if (arquivoTipo.equalsIgnoreCase("null")) this.arquivoTipo = "";
		else if (!arquivoTipo.equalsIgnoreCase("")) this.arquivoTipo = arquivoTipo;
	}

	public String getTextoEditor() {
		return textoEditor;
	}

	public void setTextoEditor(String textoEditor) {
		if (textoEditor != null) this.textoEditor = textoEditor;
	}

	public String getDataImpressao() {
		return dataImpressao;
	}

	public void setDataImpressao(String dataImpressao) {
		this.dataImpressao = dataImpressao;
	}

	public String getIdUsuarioServentiaImpressao() {
		return idUsuarioServentiaImpressao;
	}

	public void setIdUsuarioServentiaImpressao(String idUsuarioServentiaImpressao) {
		this.idUsuarioServentiaImpressao = idUsuarioServentiaImpressao;
	}

	public String getDataPrisao() {
		return dataPrisao;
	}

	public void setDataPrisao(String dataPrisao) {
		if (dataPrisao != null) if (dataPrisao.equalsIgnoreCase("null")) this.dataPrisao = "";
		else if (!dataPrisao.equalsIgnoreCase("")) this.dataPrisao = dataPrisao;
	}

	public String getIdUsuarioServentiaCumprimento() {
		return idUsuarioServentiaCumprimento;
	}

	public void setIdUsuarioServentiaCumprimento(String idUsuarioServentiaCumprimento) {
		this.idUsuarioServentiaCumprimento = idUsuarioServentiaCumprimento;
	}
	
	public String getIdModelo() {
		return idModelo;
	}

	public void setIdModelo(String idModelo) {
		if (idModelo != null) if (idModelo.equalsIgnoreCase("null")) {
			this.idModelo = "";
			this.modelo = "";
		} else if (!idModelo.equalsIgnoreCase("")) this.idModelo = idModelo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		if (modelo != null) if (modelo.equalsIgnoreCase("null")) this.modelo = "";
		else if (!modelo.equalsIgnoreCase("")) this.modelo = modelo;

	}
	public ProcessoPartePrisaoDt getProcessoPartePrisaoDt() {
		return processoPartePrisaoDt;
	}

	public void setProcessoPartePrisaoDt(ProcessoPartePrisaoDt processoPartePrisaoDt) {
		this.processoPartePrisaoDt = processoPartePrisaoDt;
	}
}
