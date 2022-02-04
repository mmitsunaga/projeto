package br.gov.go.tj.projudi.dt;

public class PonteiroLogCompensarDtGen extends Dados{

	protected String Id_PonteiroLogCompensar;
	protected String Justificativa;

	protected String Id_AreaDistribuicao_O;
	protected String AreaDistribuicao_O;
	protected String Id_Serventia_O;
	protected String Serventia_O;
	protected String Id_ServentiaCargo_O;
	protected String ServentiaCargo_O;
	protected String Id_AreaDistribuicao_D;
	protected String AreaDistribuicao_D;
	protected String Id_Serventia_D;
	protected String Serventia_D;
	protected String Id_ServentiaCargo_D;
	protected String ServentiaCargo_D;
	protected String Qtd;
	protected String Id_UsuarioServentia_I;
	protected String Usuario_I;
	protected String Id_UsuarioServentia_F;
	protected String Usuario_F;
	protected String DataInicio;
	protected String DataFinal;

	protected String CodigoTemp;

//---------------------------------------------------------
	public PonteiroLogCompensarDtGen() {

		limpar();

	}

	public String getId()  {return Id_PonteiroLogCompensar;}
	public void setId(String valor ) { if(valor!=null) Id_PonteiroLogCompensar = valor;}
	public String getJustificativa()  {return Justificativa;}
	public void setJustificativa(String valor ) { if (valor!=null) Justificativa = valor;}
	public String getId_AreaDistribuicao_O()  {return Id_AreaDistribuicao_O;}
	public void setId_AreaDistribuicao_O(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_AreaDistribuicao_O = ""; AreaDistribuicao_O = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicao_O = valor;}
	public String getAreaDistribuicao_O()  {return AreaDistribuicao_O;}
	public void setAreaDistribuicao_O(String valor ) { if (valor!=null) AreaDistribuicao_O = valor;}
	public String getId_Serventia_O()  {return Id_Serventia_O;}
	public void setId_Serventia_O(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Serventia_O = ""; Serventia_O = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia_O = valor;}
	public String getServentia_O()  {return Serventia_O;}
	public void setServentia_O(String valor ) { if (valor!=null) Serventia_O = valor;}
	public String getId_ServentiaCargo_O()  {return Id_ServentiaCargo_O;}
	public void setId_ServentiaCargo_O(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServentiaCargo_O = ""; ServentiaCargo_O = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo_O = valor;}
	public String getServentiaCargo_O()  {return ServentiaCargo_O;}
	public void setServentiaCargo_O(String valor ) { if (valor!=null) ServentiaCargo_O = valor;}
	public String getId_AreaDistribuicao_D()  {return Id_AreaDistribuicao_D;}
	public void setId_AreaDistribuicao_D(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_AreaDistribuicao_D = ""; AreaDistribuicao_D = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicao_D = valor;}
	public String getAreaDistribuicao_D()  {return AreaDistribuicao_D;}
	public void setAreaDistribuicao_D(String valor ) { if (valor!=null) AreaDistribuicao_D = valor;}
	public String getId_Serventia_D()  {return Id_Serventia_D;}
	public void setId_Serventia_D(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Serventia_D = ""; Serventia_D = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia_D = valor;}
	public String getServentia_D()  {return Serventia_D;}
	public void setServentia_D(String valor ) { if (valor!=null) Serventia_D = valor;}
	public String getId_ServentiaCargo_D()  {return Id_ServentiaCargo_D;}
	public void setId_ServentiaCargo_D(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServentiaCargo_D = ""; ServentiaCargo_D = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo_D = valor;}
	public String getServentiaCargo_D()  {return ServentiaCargo_D;}
	public void setServentiaCargo_D(String valor ) { if (valor!=null) ServentiaCargo_D = valor;}
	public String getQtd()  {return Qtd;}
	public void setQtd(String valor ) { if (valor!=null) Qtd = valor;}
	public String getId_UsuarioServentia_I()  {return Id_UsuarioServentia_I;}
	public void setId_UsuarioServentia_I(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuarioServentia_I = ""; Usuario_I = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia_I = valor;}
	public String getUsuario_I()  {return Usuario_I;}
	public void setUsuario_I(String valor ) { if (valor!=null) Usuario_I = valor;}
	public String getId_UsuarioServentia_F()  {return Id_UsuarioServentia_F;}
	public void setId_UsuarioServentia_F(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuarioServentia_F = ""; Usuario_F = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia_F = valor;}
	public String getUsuario_F()  {return Usuario_F;}
	public void setUsuario_F(String valor ) { if (valor!=null) Usuario_F = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) { if (valor!=null) DataInicio = valor;}
	public String getDataFinal()  {return DataFinal;}
	public void setDataFinal(String valor ) { if (valor!=null) DataFinal = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(PonteiroLogCompensarDt objeto){
		 if (objeto==null) return;
		Id_PonteiroLogCompensar = objeto.getId();
		Id_AreaDistribuicao_O = objeto.getId_AreaDistribuicao_O();
		AreaDistribuicao_O = objeto.getAreaDistribuicao_O();
		Id_Serventia_O = objeto.getId_Serventia_O();
		Serventia_O = objeto.getServentia_O();
		Id_ServentiaCargo_O = objeto.getId_ServentiaCargo_O();
		ServentiaCargo_O = objeto.getServentiaCargo_O();
		Id_AreaDistribuicao_D = objeto.getId_AreaDistribuicao_D();
		AreaDistribuicao_D = objeto.getAreaDistribuicao_D();
		Id_Serventia_D = objeto.getId_Serventia_D();
		Serventia_D = objeto.getServentia_D();
		Id_ServentiaCargo_D = objeto.getId_ServentiaCargo_D();
		ServentiaCargo_D = objeto.getServentiaCargo_D();
		Qtd = objeto.getQtd();
		Id_UsuarioServentia_I = objeto.getId_UsuarioServentia_I();
		Usuario_I = objeto.getUsuario_I();
		Id_UsuarioServentia_F = objeto.getId_UsuarioServentia_F();
		Usuario_F = objeto.getUsuario_F();
		DataInicio = objeto.getDataInicio();
		DataFinal = objeto.getDataFinal();
		Justificativa = objeto.getJustificativa();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PonteiroLogCompensar="";
		Id_AreaDistribuicao_O="";
		AreaDistribuicao_O="";
		Id_Serventia_O="";
		Serventia_O="";
		Id_ServentiaCargo_O="";
		ServentiaCargo_O="";
		Id_AreaDistribuicao_D="";
		AreaDistribuicao_D="";
		Id_Serventia_D="";
		Serventia_D="";
		Id_ServentiaCargo_D="";
		ServentiaCargo_D="";
		Qtd="";
		Id_UsuarioServentia_I="";
		Usuario_I="";
		Id_UsuarioServentia_F="";
		Usuario_F="";
		DataInicio="";
		DataFinal="";
		Justificativa="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PonteiroLogCompensar:" + Id_PonteiroLogCompensar + ";Id_AreaDistribuicao_O:" + Id_AreaDistribuicao_O + ";AreaDistribuicao_O:" + AreaDistribuicao_O + ";Id_Serventia_O:" + Id_Serventia_O + ";Serventia_O:" + Serventia_O + ";Id_ServentiaCargo_O:" + Id_ServentiaCargo_O + ";ServentiaCargo_O:" + ServentiaCargo_O + ";Id_AreaDistribuicao_D:" + Id_AreaDistribuicao_D + ";AreaDistribuicao_D:" + AreaDistribuicao_D + ";Id_Serventia_D:" + Id_Serventia_D + ";Serventia_D:" + Serventia_D + ";Id_ServentiaCargo_D:" + Id_ServentiaCargo_D + ";ServentiaCargo_D:" + ServentiaCargo_D + ";Qtd:" + Qtd + ";Id_UsuarioServentia_I:" + Id_UsuarioServentia_I + ";Usuario_I:" + Usuario_I + ";Id_UsuarioServentia_F:" + Id_UsuarioServentia_F + ";Usuario_F:" + Usuario_F + ";DataInicio:" + DataInicio + ";DataFinal:" + DataFinal + ";Justificativa:" + Justificativa + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
