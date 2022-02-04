package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class CertidaoNegativaPositivaDt extends Dados {

	/**
	 * 
	 */
	public static final int NEGATIVA_CRIMINAL_FISICA_MODELO_CODIGO = 126;
	public static final int POSITIVA_CRIMINAL_FISICA_MODELO_CODIGO = 124;
	public static final int NEGATIVA_CRIMINAL_JURIDICA_MODELO_CODIGO = 129;
	public static final int POSITIVA_CRIMINAL_JURIDICA_MODELO_CODIGO = 130;
	public static final int NEGATIVA_CIVEL_FISICA_MODELO_CODIGO = 121;
	public static final int POSITIVA_CIVEL_FISICA_MODELO_CODIGO = 112;
	public static final int NEGATIVA_CIVEL_JURIDICA_MODELO_CODIGO = 127;
	public static final int POSITIVA_CIVEL_JURIDICA_MODELO_CODIGO = 128;
	
	public static final int NEGATIVA_CIVEL_FISICA_PUBLICA_GRATUITA_MODELO_CODIGO = 161;
	public static final int NEGATIVA_CIVEL_FISICA_PUBLICA_MODELO_CODIGO = 149;	
	public static final int NEGATIVA_CRIMINAL_FISICA_PUBLICA_MODELO_CODIGO = 151;
	public static final int NEGATIVA_CIVEL_JURIDICA_PUBLICA_MODELO_CODIGO = 153;	
	public static final int NEGATIVA_CRIMINAL_JURIDICA_PUBLICA_MODELO_CODIGO = 154;
	public static final int NEGATIVA_CIVEL_FISICA_PUBLICA_GUIA_MODELO_CODIGO = 159;
	public static final int NEGATIVA_CIVEL_JURIDICA_PUBLICA_GUIA_MODELO_CODIGO = 160;
	
	public static final int NEGATIVA_2G_CIVEL_FISICA_PUBLICA_MODELO_CODIGO = 200;
	public static final int NEGATIVA_2G_CRIMINAL_FISICA_PUBLICA_MODELO_CODIGO = 201;
	public static final int NEGATIVA_2G_CIVEL_JURIDICA_PUBLICA_MODELO_CODIGO = 202;	
	public static final int NEGATIVA_2G_CRIMINAL_JURIDICA_PUBLICA_MODELO_CODIGO = 203;
	
	public static final String TAG_NOME_COMARCA_TITULO = "${NomeComarca}";
	public static final String TAG_FINALIDADE = "${Finalidade}";
	public static final String TAG_NOME_COMARCA_TEXTO = "${NomeComarcaTexto}";
	public static final String TAG_NUMERO_GUIA_VALIDACAO = "${CodigoVerificacao}";
	
	private static final long serialVersionUID = -278963347217651347L;
	
	public static final int CodigoPermissao = 447;
	
	private String numeroGuia;
	private String texto;
	private String nome;
	private String cpfCnpj;
	private String tipoPessoa;
	private String dataNascimento;
	private String nomeMae;
	private String nomePai;
	private String sexo;
	private String identidade;
	private String estadoCivil;
	private String profissao;
	private String domicilio;
	private String nacionalidade;
	private String serventia;
	private String comarca;
	private String id_comarca;
	private List listaProcessos;
	private List listaProcessosRequerente;
	private String comarcaCodigo;
	private boolean fimJudical;
	private String valorCertidao;
	private String abrange;
	private String valorTaxa;
	private String valortotal;
	private String dataApresentacao;
	private String areaCodigo;
	private String cnpjCompleto;
	private String dataPagamento = "";
	
	public void setCnpjCompleto(String cnpj) {
		cnpjCompleto = cnpj;
	}
	
	public String getRadicalCnpj() {
		String radical = "";
		if(!this.cnpjCompleto.isEmpty())
			radical = this.cnpjCompleto.split("/")[0];
		radical = radical.replaceAll("\\.", "");
		radical = radical.trim();
		long cnpjRadical = Long.parseLong(radical);
		return String.valueOf(cnpjRadical);
	}
	
	
	public void setComarcaCodigo(String comarcaCodigo) {
		if (comarcaCodigo != null)
			this.comarcaCodigo = comarcaCodigo;
	}

	public String getValorCertidao() {

		String novoValor = valorCertidao;
		if (novoValor == null) novoValor = "";
		novoValor = Funcoes.removeEspacosExcesso(novoValor);
		if(novoValor.length() < 4) {
			do {
				novoValor = "0" +novoValor;
			} while(novoValor.length()< 4);
		}
		int tamanho = novoValor.length();
		int ultimaPos = tamanho - 1;
		String retorno = (String)novoValor.subSequence(0, ultimaPos - 1);
		retorno+="," + novoValor.substring(ultimaPos - 1);
		return "R$ " +retorno;
	}

	public String getDataApresentacao() {
		return dataApresentacao;
	}

	public void setDataApresentacao(String dataApresentacao) {
		this.dataApresentacao = dataApresentacao;
	}

	public void setValorCertidao(String valorCertidao) {
		
		this.valorCertidao = valorCertidao;
	}

	public String getAbrange() {
	
		return abrange;
	}

	public void setAbrange(String abrange) {
		this.abrange = abrange;
	}

	public String getValorTaxa() {
		
		if (valorTaxa == null){
			valorTaxa = "0,00";
		}
		return valorTaxa;
	}
	
	public String getValorTaxaFormatado() {

		String novoValor = valorTaxa;
		if (novoValor == null) novoValor = "";
		novoValor = Funcoes.removeEspacosExcesso(novoValor);
		if(novoValor.length() < 4) {
			do {
				novoValor = "0" +novoValor;
			} while(novoValor.length()< 4);
		}
		int tamanho = novoValor.length();
		int ultimaPos = tamanho - 1;
		String retorno = (String)novoValor.subSequence(0, ultimaPos - 1);
		retorno+="," + novoValor.substring(ultimaPos - 1);
		return "R$ " +retorno;
	}

	public void setValorTaxa(String valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public String getValortotal() {
		
		if (valortotal == null){
			valortotal = "0,0";
		}
		
		return valortotal;
	}
	
	public String getValortotalFormatado() {

		String novoValor = valortotal;
		if (novoValor == null) novoValor = "";
		novoValor = Funcoes.removeEspacosExcesso(novoValor);
		if(novoValor.length() < 4) {
			do {
				novoValor = "0" +novoValor;
			} while(novoValor.length()< 4);
		}
		int tamanho = novoValor.length();
		int ultimaPos = tamanho - 1;
		String retorno = (String)novoValor.subSequence(0, ultimaPos - 1);
		retorno+="," + novoValor.substring(ultimaPos - 1);
		return "R$ " +retorno;
	}

	public void setValortotal(String valortotal) {
		this.valortotal = valortotal;
	}

	public CertidaoNegativaPositivaDt() {
		this.limpar();
	}
	
	public CertidaoNegativaPositivaDt(String nome, String cpfCnpj, String tipoPessoa, String dataNascimento, String nomeMae, String nomePai, String sexo, String identidade, String estadoCivil, String profissao, String domicilio, String nacionalidade, String areaCodigo, String serventia, String comarca) {
		super();
		this.nome = nome;
		this.cpfCnpj = cpfCnpj;
		this.tipoPessoa = tipoPessoa;
		this.dataNascimento = dataNascimento;
		this.nomeMae = nomeMae;
		this.nomePai = nomePai;
		this.sexo = sexo;
		this.identidade = identidade;
		this.estadoCivil = estadoCivil;
		this.profissao = profissao;
		this.domicilio = domicilio;
		this.nacionalidade = nacionalidade;
		this.areaCodigo = areaCodigo;
		this.serventia = serventia;
		this.comarca = comarca;
		this.listaProcessos = new ArrayList(10);
		this.listaProcessosRequerente = new ArrayList(10);
		this.id_comarca = "";
		this.cnpjCompleto = "";

	}
	
	public void limparParcial() {	
		this.numeroGuia = "";
		this.tipoPessoa = "";
		this.nomePai = "";
		this.sexo = "";
		this.identidade = "";
		this.estadoCivil = "";
		this.profissao = "";
		this.domicilio = "";
		this.nacionalidade = "";		
		this.serventia = "";
		this.comarca = "";
		this.texto = "";
		this.listaProcessos = new ArrayList(10);
		this.listaProcessosRequerente = new ArrayList(10);
		this.fimJudical = false;
		this.id_comarca = "";
		this.areaCodigo = "";
		this.cnpjCompleto = "";
		this.guiaEmissaoCertidao = null;
	}

	public void limpar() {
		this.limparParcial();
		this.nome = "";
		this.cpfCnpj = "";		
		this.dataNascimento = "";
		this.nomeMae = "";
	}
	
	public boolean temFimJudical() {
		return fimJudical;
	}
	
	public void setFimJudicial(boolean fimJudicial) {
		this.fimJudical = fimJudicial;		
	}
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		if (texto != null)
		this.texto = texto;
	}

	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		if (numeroGuia != null)
		this.numeroGuia = numeroGuia;
	}

	public List getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(List listaProcessos) {
		if (listaProcessos != null)
			this.listaProcessos = listaProcessos;
	}
	
	public void addListaProcessos(List listaProcessos) {
		if (listaProcessos != null)
		{
			if (this.listaProcessos == null) 
				this.listaProcessos = listaProcessos;			
			else			
				this.listaProcessos.addAll(listaProcessos);			
		}		
	}
	
	public List getlistaProcessosRequerente() {
		return listaProcessosRequerente;
	}

	public void setlistaProcessosRequerente(List listaProcessos) {
		if (listaProcessos != null)
			this.listaProcessosRequerente = listaProcessos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome != null)
			this.nome = nome;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		if (cpfCnpj != null)
			this.cpfCnpj = cpfCnpj;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		if (tipoPessoa != null)
			this.tipoPessoa = tipoPessoa;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		if (dataNascimento != null)
			this.dataNascimento = dataNascimento;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		if (nomeMae != null)
			this.nomeMae = nomeMae;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		if (nomePai != null)
			this.nomePai = nomePai;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		if (sexo != null)
			this.sexo = sexo;
	}

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		if (identidade != null)
			this.identidade = identidade;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		if (estadoCivil != null)
			this.estadoCivil = estadoCivil;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		if (profissao != null)
			this.profissao = profissao;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		if (nacionalidade != null)
			this.nacionalidade = nacionalidade;
	}

//	public int getArea() {
//		if (this.areaCodigo > -1) return this.areaCodigo;
//		if (area == null || area.isEmpty()) return AreaDt.CIVEL;
//		return area.matches("[1]") || area.matches("[cC][iI][vV][eE][lL]") ?  AreaDt.CIVEL : AreaDt.CRIMINAL ;
//	}

//	public void setArea(String area) {
//		if (area != null)
//			this.area = area;
//	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null)
			this.serventia = serventia;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		if (comarca != null)
			this.comarca = comarca;
	}
	
	public String getId_Comarca() {
		return id_comarca;
	}
	
	public void setId_Comarca(String id_comarca) {
		if (id_comarca != null)
			this.id_comarca = id_comarca;
	}

	public String getId() {
		return null;
	}

	public void setId(String id) {
	}
	
	public String getComarcaCodigo() {
		
		return comarcaCodigo;
	}

	public void removeProcesso(int posicao) {
		listaProcessos.remove(posicao);	
	}

	public void setAreaCodigo(String areaCodigo) {
		if (areaCodigo != null)
			this.areaCodigo = areaCodigo;
	}
	
	public String getAreaCodigo() {
		return this.areaCodigo;
	}
	
	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	private GuiaEmissaoDt guiaEmissaoCertidao;
	
	public GuiaEmissaoDt getGuiaEmissaoCertidao() {
		return guiaEmissaoCertidao;
	}

	public void setGuiaEmissaoCertidao(GuiaEmissaoDt guiaEmissaoCertidao) {
		this.guiaEmissaoCertidao = guiaEmissaoCertidao;
	}

	public boolean isCivel() {
		if (getAreaCodigo()!=null && getAreaCodigo().equals(AreaDt.CIVEL))
			return true;
		return false;
	}
	
	public boolean isCriminal() {
		if (getAreaCodigo()!=null && getAreaCodigo().equals(AreaDt.CRIMINAL))
			return true;
		return false;
	}
}
