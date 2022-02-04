package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

public class ProcessoParteDebitoCadinDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -677578855949405962L;
	
	private String Id_ProcessoParteDebitoCadin;
	private String Id_ProcessoParteDebito;	
	private String NumeroLote;
	private String DataGeracao;
	private String TipoDocumento;
	private String NumeroDocumento;
	private String NomePessoa;
	private String NumeroProcesso;
	private String NaturezaPendencia;
	private String CategoriaPendencia;
	private String DataVencimentoDebito;
	private String ValorDevido;	
	private String MeioEnvioComunicado;
	private String DataEnvioComunicado;
	private String DataCienciaComunicado;
	private String TipoLogradouro;
	private String NomeLogradouro;
	private String NumeroEndereco;
	private String NumeroLoteEndereco;
	private String NumeroQuadraEndereco;
	private String DescricaoComplementoEndereco;
	private String NomeBairro;
	private String CodigoCep;
	private String MunicipioEndereco;
	private String UFEndereco;
	private String ObservacaoPendencia;
	private boolean Fisico;
//---------------------------------------------------------
	public ProcessoParteDebitoCadinDt() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteDebitoCadin;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteDebitoCadin = valor;}
	public String getId_ProcessoParteDebito()  {return Id_ProcessoParteDebito;}
	public void setId_ProcessoParteDebito(String valor ) {if(valor!=null) Id_ProcessoParteDebito = valor;}
	public String getNumeroLote()  {return NumeroLote;}
	public void setNumeroLote(String valor ) {if (valor!=null) NumeroLote = valor;}
	public String getDataGeracao()  {return DataGeracao;}
	public void setDataGeracao(String valor ) {if (valor!=null) DataGeracao = valor;}
	public String getTipoDocumento()  {return TipoDocumento;}
	public void setTipoDocumento(String valor ) {if (valor!=null) TipoDocumento = valor;}
	public String getNumeroDocumento()  {return NumeroDocumento;}
	public void setNumeroDocumento(String valor ) {if (valor!=null) NumeroDocumento = valor;}
	public String getNomePessoa()  {return NomePessoa;}
	public void setNomePessoa(String valor ) {if (valor!=null) NomePessoa = valor;}
	public String getNumeroProcesso()  {return NumeroProcesso;}
	public void setNumeroProcesso(String valor ) {if (valor!=null) NumeroProcesso = valor;}
	public String getNaturezaPendencia()  {return NaturezaPendencia;}
	public void setNaturezaPendencia(String valor ) {if (valor!=null) NaturezaPendencia = valor;}
	public String getCategoriaPendencia()  {return CategoriaPendencia;}
	public void setCategoriaPendencia(String valor ) {if (valor!=null) CategoriaPendencia = valor;}
	public String getDataVencimentoDebito()  {return DataVencimentoDebito;}
	public void setDataVencimentoDebito(String valor ) {if (valor!=null) DataVencimentoDebito = valor;}
	public String getValorDevido()  {return ValorDevido;}
	public void setValorDevido(String valor ) {if (valor!=null) ValorDevido = valor;}
	public String getMeioEnvioComunicado()  {return MeioEnvioComunicado;}
	public void setMeioEnvioComunicado(String valor ) {if (valor!=null) MeioEnvioComunicado = valor;}
	public String getDataEnvioComunicado()  {return DataEnvioComunicado;}
	public void setDataEnvioComunicado(String valor ) {if (valor!=null) DataEnvioComunicado = valor;}
	public String getDataCienciaComunicado()  {return DataCienciaComunicado;}
	public void setDataCienciaComunicado(String valor ) {if (valor!=null) DataCienciaComunicado = valor;}
	public String getTipoLogradouro()  {return TipoLogradouro;}
	public void setTipoLogradouro(String valor ) {if (valor!=null) TipoLogradouro = valor;}
	public String getNomeLogradouro()  {return NomeLogradouro;}
	public void setNomeLogradouro(String valor ) {if (valor!=null) NomeLogradouro = valor;}
	public String getNumeroEndereco()  {return NumeroEndereco;}
	public void setNumeroEndereco(String valor ) {if (valor!=null) NumeroEndereco = valor;}
	public String getNumeroLoteEndereco()  {return NumeroLoteEndereco;}
	public void setNumeroLoteEndereco(String valor ) {if (valor!=null) NumeroLoteEndereco = valor;}
	public String getNumeroQuadraEndereco()  {return NumeroQuadraEndereco;}
	public void setNumeroQuadraEndereco(String valor ) {if (valor!=null) NumeroQuadraEndereco = valor;}
	public String getDescricaoComplementoEndereco()  {return DescricaoComplementoEndereco;}
	public void setDescricaoComplementoEndereco(String valor ) {if (valor!=null) DescricaoComplementoEndereco = valor;}
	public String getNomeBairro()  {return NomeBairro;}
	public void setNomeBairro(String valor ) {if (valor!=null) NomeBairro = valor;}
	public String getCodigoCep()  {return CodigoCep;}
	public void setCodigoCep(String valor ) {if (valor!=null) CodigoCep = valor;}
	public String getMunicipioEndereco()  {return MunicipioEndereco;}
	public void setMunicipioEndereco(String valor ) {if (valor!=null) MunicipioEndereco = valor;}
	public String getUFEndereco()  {return UFEndereco;}
	public void setUFEndereco(String valor ) {if (valor!=null) UFEndereco = valor;}
	public String getObservacaoPendencia()  {return ObservacaoPendencia;}
	public void setObservacaoPendencia(String valor ) {if (valor!=null) ObservacaoPendencia = valor;}
	
	public boolean isFisico() {
		return Fisico;
	}

	public void setFisico(boolean valor) {
		this.Fisico = valor;
	}

	public void copiar(ProcessoParteDebitoCadinDt objeto){
		Id_ProcessoParteDebitoCadin = objeto.getId();
		Id_ProcessoParteDebito = objeto.getId_ProcessoParteDebito();
		DataGeracao = objeto.getDataGeracao();
		NumeroLote = objeto.getNumeroLote();
		TipoDocumento = objeto.getTipoDocumento();
		NumeroDocumento = objeto.getNumeroDocumento();
		NomePessoa = objeto.getNomePessoa();
		NumeroProcesso = objeto.getNumeroProcesso();
		NaturezaPendencia = objeto.getNaturezaPendencia();
		CategoriaPendencia = objeto.getCategoriaPendencia();
		DataVencimentoDebito = objeto.getDataVencimentoDebito();
		ValorDevido = objeto.getValorDevido();
		MeioEnvioComunicado = objeto.getMeioEnvioComunicado();
		DataEnvioComunicado = objeto.getDataEnvioComunicado();
		DataCienciaComunicado = objeto.getDataCienciaComunicado();
		TipoLogradouro = objeto.getTipoLogradouro();
		NomeLogradouro = objeto.getNomeLogradouro();
		NumeroEndereco = objeto.getNumeroEndereco();
		NumeroLoteEndereco = objeto.getNumeroLoteEndereco();
		NumeroQuadraEndereco = objeto.getNumeroQuadraEndereco();
		DescricaoComplementoEndereco = objeto.getDescricaoComplementoEndereco();
		NomeBairro = objeto.getNomeBairro();
		CodigoCep = objeto.getCodigoCep();
		MunicipioEndereco = objeto.getMunicipioEndereco();
		UFEndereco = objeto.getUFEndereco();
		ObservacaoPendencia = objeto.getObservacaoPendencia();
		Fisico = objeto.isFisico();		
	}

	public void limpar(){
		Id_ProcessoParteDebitoCadin="";
		DataGeracao="";
		NumeroLote="";
		TipoDocumento="";
		NumeroDocumento="";
		NomePessoa="";
		NumeroProcesso = "";
		NaturezaPendencia = "";
		CategoriaPendencia = "";
		DataVencimentoDebito = "";
		ValorDevido = "";
		MeioEnvioComunicado = "";
		DataEnvioComunicado = "";
		DataCienciaComunicado = "";
		TipoLogradouro = "";
		NomeLogradouro = "";
		NumeroEndereco = "";
		NumeroLoteEndereco = "";
		NumeroQuadraEndereco = "";
		DescricaoComplementoEndereco = "";
		NomeBairro = "";
		CodigoCep = "";
		MunicipioEndereco = "";
		UFEndereco = "";
		ObservacaoPendencia = "";
		Fisico = false;
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteDebitoCadin:" + Id_ProcessoParteDebitoCadin + ";Id_ProcessoParteDebito:" + Id_ProcessoParteDebito + ";DataGeracao:" + DataGeracao + 
				";NumeroLote:" + NumeroLote + ";TipoDocumento:" + TipoDocumento + ";NumeroDocumento:" + NumeroDocumento + ";NomePessoa:" + NomePessoa + 
				";NumeroProcesso:" + NumeroProcesso + ";NaturezaPendencia:" + NaturezaPendencia + ";CategoriaPendencia:" + CategoriaPendencia + 
				";DataVencimentoDebito:" + DataVencimentoDebito + ";ValorDevido:" + ValorDevido  + ";MeioEnvioComunicado:" + MeioEnvioComunicado + 
				";DataEnvioComunicado:" + DataEnvioComunicado + ";DataCienciaComunicado:" + DataCienciaComunicado + ";TipoLogradouro:" + TipoLogradouro +
				";NomeLogradouro:" + NomeLogradouro + ";NumeroEndereco:" + NumeroEndereco + ";NumeroLoteEndereco:" + NumeroLoteEndereco + 
				";NumeroQuadraEndereco:" + NumeroQuadraEndereco + ";DescricaoComplementoEndereco:" + DescricaoComplementoEndereco + ";NomeBairro:" + NomeBairro + 
				";CodigoCep:" + CodigoCep + ";MunicipioEndereco:" + MunicipioEndereco + ";UFEndereco:" + UFEndereco + ";ObservacaoPendencia:" + ObservacaoPendencia + "]";
		
	}
	
	public String getLinhaArquivo() throws Exception {
		StringBuilder linha = new StringBuilder();
		linha.append(this.getTipoDocumento());
		linha.append(Funcoes.completarZeros(this.NumeroDocumento, 14));
		linha.append(obtenhaTexto(this.getNomePessoa(), 60));
		linha.append(obtenhaTexto(this.getNumeroProcesso(), 25));
		linha.append(Funcoes.completarZeros(this.getNaturezaPendencia(), 2));
		linha.append(Funcoes.completarZeros(this.getCategoriaPendencia(), 2));
		linha.append(obtenhaData(this.getDataVencimentoDebito()));
		linha.append(Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(this.getValorDevido()), 14));
		linha.append(Funcoes.completarZeros(this.getMeioEnvioComunicado(), 2));
		linha.append(obtenhaData(this.getDataEnvioComunicado()));
		linha.append(obtenhaData(this.getDataCienciaComunicado()));
		linha.append(obtenhaTexto(this.getTipoLogradouro(), 3));
		linha.append(obtenhaTexto(this.getNomeLogradouro(), 60));
		linha.append(obtenhaTexto(this.getNumeroEndereco(), 10));
		linha.append(obtenhaTexto(this.getNumeroLoteEndereco(), 9));
		linha.append(obtenhaTexto(this.getNumeroQuadraEndereco(), 10));
		linha.append(obtenhaTexto(this.getDescricaoComplementoEndereco(), 80));
		linha.append(obtenhaTexto(this.getNomeBairro(), 60));
		linha.append(Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(this.getCodigoCep()), 8));
		linha.append(obtenhaTexto(this.getMunicipioEndereco(), 60));
		linha.append(obtenhaTexto(this.getUFEndereco(), 2));
		linha.append(obtenhaTexto(this.getObservacaoPendencia(), 150));
		linha.append("\n");
		return linha.toString();
	}
	
	private String obtenhaData(String data) throws Exception {
		if (data == null || data.trim().length() == 0) {
			return Funcoes.completarZeros("0", 8);
		}
		TJDataHora dataVencimento = new TJDataHora();
		dataVencimento.setDataaaaa_MM_ddHHmmss(data);
		return dataVencimento.getDataFormatadaddMMyyyySemSeparador();
	}
	
	private String obtenhaTexto(String valor, int tamanho) {
		if (valor.length() > tamanho) {
			return valor.substring(0, tamanho);
		} else {
			return Funcoes.completarEspacosDireita(valor, tamanho);
		}
	}
} 
