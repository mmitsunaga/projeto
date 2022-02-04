package br.gov.go.tj.projudi.dt;

public class ServentiaDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6718043864444107087L;
	private String Id_Serventia;
	private String Serventia;


	private String ServentiaCodigo;
	private String ServentiaCodigoExterno;
	private String Id_ServentiaTipo;
	private String ServentiaTipo;
	private String Id_ServentiaSubtipo;
	private String ServentiaSubtipo;
	private String Id_Area;
	private String Area;
	private String Id_Comarca;
	private String Comarca;
	private String Id_AreaDistribuicao;
	private String AreaDistribuicao;
	private String Id_EstadoRepresentacao;
	private String EstadoRepresentacao;
	private String Id_AudienciaTipo;
	private String AudienciaTipo;
	protected String QuantidadeDistribuicao;	
	
	private String Telefone;
	private String Online;
	private String CodigoTemp;
	
	private String ServentiaTipoCodigo;
	private String ServentiaSubtipoCodigo;
	private String AreaCodigo;
	private String ComarcaCodigo;
	private String DataCadastro;
	private String DataImplantacao;

//---------------------------------------------------------
	public ServentiaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Serventia;}
	public void setId(String valor ) {if(valor!=null) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	public String getServentiaCodigoExterno()  {return ServentiaCodigoExterno;}
	public void setServentiaCodigoExterno(String valor ) {if (valor!=null) ServentiaCodigoExterno = valor;}
	public String getId_ServentiaTipo()  {return Id_ServentiaTipo;}
	public void setId_ServentiaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaTipo = ""; ServentiaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaTipo = valor;}
	public String getServentiaTipo()  {return ServentiaTipo;}
	public void setServentiaTipo(String valor ) {if (valor!=null) ServentiaTipo = valor;}
	public String getId_ServentiaSubtipo()  {return Id_ServentiaSubtipo;}
	public void setId_ServentiaSubtipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaSubtipo = ""; ServentiaSubtipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaSubtipo = valor;}
	public String getServentiaSubtipo()  {return ServentiaSubtipo;}
	public void setServentiaSubtipo(String valor ) {if (valor!=null) ServentiaSubtipo = valor;}
	public String getId_Area()  {return Id_Area;}
	public void setId_Area(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Area = ""; Area = "";}else if (!valor.equalsIgnoreCase("")) Id_Area = valor;}
	public String getArea()  {return Area;}
	public void setArea(String valor ) {if (valor!=null) Area = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getId_AreaDistribuicao()  {return Id_AreaDistribuicao;}
	public void setId_AreaDistribuicao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AreaDistribuicao = ""; AreaDistribuicao = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicao = valor;}
	public String getAreaDistribuicao()  {return AreaDistribuicao;}
	public void setAreaDistribuicao(String valor ) {if (valor!=null) AreaDistribuicao = valor;}
	public String getId_EstadoRepresentacao()  {return Id_EstadoRepresentacao;}
	public void setId_EstadoRepresentacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EstadoRepresentacao = ""; EstadoRepresentacao = "";}else if (!valor.equalsIgnoreCase("")) Id_EstadoRepresentacao = valor;}
	public String getEstadoRepresentacao()  {return EstadoRepresentacao;}
	public void setEstadoRepresentacao(String valor ) {if (valor!=null) EstadoRepresentacao = valor;}
	public String getId_AudienciaTipo()  {return Id_AudienciaTipo;}
	public void setId_AudienciaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AudienciaTipo = ""; AudienciaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_AudienciaTipo = valor;}
	public String getAudienciaTipo()  {return AudienciaTipo;}
	public void setAudienciaTipo(String valor ) {if (valor!=null) AudienciaTipo = valor;}
	public String getQuantidadeDistribuicao()  {return QuantidadeDistribuicao;}
	public void setQuantidadeDistribuicao(String valor ) {if (valor!=null) QuantidadeDistribuicao = valor;}
	

	public String getTelefone()  {return Telefone;}
	public void setTelefone(String valor ) {if (valor!=null) Telefone = valor;}
	public String getOnline()  {return Online;}
	public void setOnline(String valor ) {if (valor!=null) Online = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public String getServentiaTipoCodigo()  {return ServentiaTipoCodigo;}
	public void setServentiaTipoCodigo(String valor ) {if (valor!=null) ServentiaTipoCodigo = valor;}
	public String getServentiaSubtipoCodigo()  {return ServentiaSubtipoCodigo;}
	public void setServentiaSubtipoCodigo(String valor ) {if (valor!=null) ServentiaSubtipoCodigo = valor;}
	public String getAreaCodigo()  {return AreaCodigo;}
	public void setAreaCodigo(String valor ) {if (valor!=null) AreaCodigo = valor;}
	public String getComarcaCodigo()  {return ComarcaCodigo;}
	public void setComarcaCodigo(String valor ) {if (valor!=null) ComarcaCodigo = valor;}
	public String getDataCadastro()  {return DataCadastro;}
	public void setDataCadastro(String valor ) {if (valor!=null) DataCadastro = valor;}
	public String getDataImplantacao()  {return DataImplantacao;}
	public void setDataImplantacao(String valor ) {if (valor!=null) DataImplantacao = valor;}


	public void copiar(ServentiaDt objeto){
		 if (objeto==null) return;
		Id_Serventia = objeto.getId();
		Serventia = objeto.getServentia();
		ServentiaCodigo = objeto.getServentiaCodigo();
		ServentiaCodigoExterno = objeto.getServentiaCodigoExterno();
		Id_ServentiaTipo = objeto.getId_ServentiaTipo();
		ServentiaTipo = objeto.getServentiaTipo();
		Id_ServentiaSubtipo = objeto.getId_ServentiaSubtipo();
		ServentiaSubtipo = objeto.getServentiaSubtipo();
		Id_Area = objeto.getId_Area();
		Area = objeto.getArea();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		Id_AreaDistribuicao = objeto.getId_AreaDistribuicao();
		AreaDistribuicao = objeto.getAreaDistribuicao();
		Id_EstadoRepresentacao = objeto.getId_EstadoRepresentacao();
		EstadoRepresentacao = objeto.getEstadoRepresentacao();
		Id_AudienciaTipo = objeto.getId_AudienciaTipo();
		AudienciaTipo = objeto.getAudienciaTipo();
		QuantidadeDistribuicao = objeto.getQuantidadeDistribuicao();		
		Telefone = objeto.getTelefone();
		Online = objeto.getOnline();
		CodigoTemp = objeto.getCodigoTemp();		
		ServentiaTipoCodigo = objeto.getServentiaTipoCodigo();
		ServentiaSubtipoCodigo = objeto.getServentiaSubtipoCodigo();
		AreaCodigo = objeto.getAreaCodigo();
		ComarcaCodigo = objeto.getComarcaCodigo();
		DataCadastro = objeto.getDataCadastro();
		DataImplantacao = objeto.getDataImplantacao();
	}

	public void limpar(){
		Id_Serventia="";
		Serventia="";
		ServentiaCodigo="";
		ServentiaCodigoExterno="";
		Id_ServentiaTipo="";
		ServentiaTipo="";
		Id_ServentiaSubtipo="";
		ServentiaSubtipo="";
		Id_Area="";
		Area="";
		Id_Comarca="";
		Comarca="";
		Id_AreaDistribuicao="";
		AreaDistribuicao="";
		Id_EstadoRepresentacao="";
		EstadoRepresentacao="";
		Id_AudienciaTipo="";
		AudienciaTipo="";
		QuantidadeDistribuicao="";		
		Telefone="";
		Online="";
		CodigoTemp="";		
		ServentiaTipoCodigo="";
		ServentiaSubtipoCodigo="";
		AreaCodigo="";
		ComarcaCodigo="";
		DataCadastro="";
		DataImplantacao="";
	}

} 
