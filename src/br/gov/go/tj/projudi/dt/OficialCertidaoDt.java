package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

//---------------------------------------------------------
public class OficialCertidaoDt extends OficialCertidaoDtGen{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4133054732546455265L;
//	public static final int CodigoPermissao=4103;
	public static final int CodigoPermissao=742;
//
	
	private String HoraDiligencia;
	private String ProcessoNumero;
	private String Serventia;
	private String ProcessoPromoventeNome;
	private String ProcessoPromovidoNome;
	private String PromoventeNome;
	private String DiligenciaNomeIntimado;
	private String DiligenciaRGIntimado;
	private String DiligenciaEndereco;
	private String PromoventeEnderecoLogradouro;
	private String PromoventeEnderecoNumero;
	private String PromoventeEnderecoComplemento;
	private String PromoventeEnderecoQuadra;
	private String PromoventeEnderecoLote;
	private String PromoventeEnderecoBairro;
	private String PromoventeEnderecoCEP;
	private String PromoventeEnderecoCidade;
	private String PromoventeEnderecoUf;
	private String DataEmissao;
	private String Usuario;
	private String Cargo;
	private String id_Modelo;
	private String modelo;
	private List<OficialCertidaoDt> ListaMandados;
	private String SegredoJustica;
	
	public void limpar() {
		super.limpar();
		HoraDiligencia = "";
		ProcessoNumero = "";
		Serventia = "";
		ProcessoPromoventeNome = "";
		ProcessoPromovidoNome = "";
		PromoventeNome = "";
		DiligenciaNomeIntimado = "";
		DiligenciaRGIntimado = "";
		DiligenciaEndereco = "";
		PromoventeEnderecoLogradouro = "";
		PromoventeEnderecoNumero = "";
		PromoventeEnderecoComplemento = "";
		PromoventeEnderecoQuadra = "";
		PromoventeEnderecoLote = "";
		PromoventeEnderecoBairro = "";
		PromoventeEnderecoCEP = "";
		PromoventeEnderecoCidade = "";
		PromoventeEnderecoUf = "";
		DataEmissao = "";
		Usuario = "";
		Cargo = "";
		id_Modelo = "";
		modelo = "";
		ListaMandados = null;
		SegredoJustica="";
	}

	
	
	public String getHoraDiligencia()  {return HoraDiligencia;}
	public void setHoraDiligencia(String valor ) {if(valor!=null) HoraDiligencia = valor;}
	
	
	
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if(valor!=null) ProcessoNumero = valor;}
	
	
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if(valor!=null) Serventia = valor;}
	
	
	public String getProcessoPromoventeNome()  {return ProcessoPromoventeNome;}
	public void setProcessoPromoventeNome(String valor ) {if(valor!=null) ProcessoPromoventeNome = valor;}
	
	
	public String getProcessoPromovidoNome()  {return ProcessoPromovidoNome;}
	public void setProcessoPromovidoNome(String valor ) {if(valor!=null) ProcessoPromovidoNome = valor;}
	
	public String getPromoventeNome()  {return PromoventeNome;}
	public void setPromoventeNome(String valor ) {if(valor!=null) PromoventeNome = valor;}
	
	public String getDiligenciaNomeIntimado()  {return DiligenciaNomeIntimado;}
	public void setDiligenciaNomeIntimado(String valor ) {if(valor!=null) DiligenciaNomeIntimado = valor;}
	
	public String getDiligenciaRGIntimado()  {return DiligenciaRGIntimado;}
	public void setDiligenciaRGIntimado(String valor ) {if(valor!=null) DiligenciaRGIntimado = valor;}
	
	public String getDiligenciaEndereco()  {return DiligenciaEndereco;}
	public void setDiligenciaEndereco(String valor ) {if(valor!=null) DiligenciaEndereco = valor;}

	public String getPromoventeEnderecoLogradouro()  {return PromoventeEnderecoLogradouro;}
	public void setPromoventeEnderecoLogradouro(String valor ) {if(valor!=null) PromoventeEnderecoLogradouro = valor;}

	public String getPromoventeEnderecoNumero()  {return PromoventeEnderecoNumero;}
	public void setPromoventeEnderecoNumero(String valor ) {if(valor!=null) PromoventeEnderecoNumero = valor;}

	public String getPromoventeEnderecoComplemento()  {return PromoventeEnderecoComplemento;}
	public void setPromoventeEnderecoComplemento(String valor ) {if(valor!=null) PromoventeEnderecoComplemento = valor;}

	public String getPromoventeEnderecoQuadra()  {return PromoventeEnderecoQuadra;}
	public void setPromoventeEnderecoQuadra(String valor ) {if(valor!=null) PromoventeEnderecoQuadra = valor;}

	public String getPromoventeEnderecoLote()  {return PromoventeEnderecoLote;}
	public void setPromoventeEnderecoLote(String valor ) {if(valor!=null) PromoventeEnderecoLote = valor;}

	public String getPromoventeEnderecoBairro()  {return PromoventeEnderecoBairro;}
	public void setPromoventeEnderecoBairro(String valor ) {if(valor!=null) PromoventeEnderecoBairro = valor;}

	public String getPromoventeEnderecoCEP()  {return PromoventeEnderecoCEP;}
	public void setPromoventeEnderecoCEP(String valor ) {if(valor!=null) PromoventeEnderecoCEP = valor;}
	
	public String getPromoventeEnderecoCidade()  {return PromoventeEnderecoCidade;}
	public void setPromoventeEnderecoCidade(String valor ) {if(valor!=null) PromoventeEnderecoCidade = valor;}
	
	public String getPromoventeEnderecoUf()  {return PromoventeEnderecoUf;}
	public void setPromoventeEnderecoUf(String valor ) {if(valor!=null) PromoventeEnderecoUf = valor;}
	
	public String getDataEmissao()  {return DataEmissao;}
	public void setDataEmissao(String valor ) {if(valor!=null) DataEmissao = valor;}
	
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if(valor!=null) Usuario = valor;}
	
	public String getCargo()  {return Cargo;}
	public void setCargo(String valor ) {if(valor!=null) Cargo = valor;}
	
	public String getId_Modelo() {return id_Modelo;}
	public void setId_Modelo(String id_Modelo) {
		if (id_Modelo != null) if (id_Modelo.equalsIgnoreCase("null")) {
			this.id_Modelo = "";
			this.modelo = "";
		} else if (!id_Modelo.equalsIgnoreCase("")) this.id_Modelo = id_Modelo;
	}
	
	public String getModelo() {	return modelo;	}
	public void setModelo(String valor ) {if(valor!=null) modelo = valor;}
//	public void setModelo(String modelo) {
//		if (modelo != null) if (modelo.equalsIgnoreCase("null")) this.modelo = "";
//		else if (!modelo.equalsIgnoreCase("")) this.modelo = modelo;
//	}
	
	public List getListaMandados() {
		return ListaMandados;
	}
	public void setListaMandados(List<OficialCertidaoDt> ListaMandados) {
		if (ListaMandados != null) 
			this.ListaMandados = ListaMandados;
	}
	
	public void addMandado(OficialCertidaoDt oficialCertidaoDt) {
		if (ListaMandados == null)
			ListaMandados = new ArrayList<OficialCertidaoDt>();
		ListaMandados.add(oficialCertidaoDt);
	}
	public String getSegredoJustica()  {return SegredoJustica;}
	public void setSegredoJustica(String valor ) {if(valor!=null) SegredoJustica = valor;}
	
}
