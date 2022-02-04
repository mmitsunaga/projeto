package br.gov.go.tj.projudi.dt;

public class TemaDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3134185722189535006L;
	protected String Id_Tema;
	protected String Titulo;


	protected String TemaCodigo;
	protected String QuesDireito;
	protected String Vinculantes;
	protected String Id_TemaSituacao;
	protected String Id_TemaOrigem;
	protected String Id_TemaTipo;
	protected String TemaSituacao;
	protected String TemaOrigem;
	protected String TemaTipo;	
	protected String dataTransito;
	
	protected String TeseFirmada;
	protected String InfoLegislativa;
	protected String NumeroIrdrCnj;	
	protected String DataAdmissao;
	protected String Suspensao;	
	protected String TemaSituacaoCnj;
	protected String TemaTipoCnj;
	protected String CodigoTemp;
	protected String OpcaoProcessual;
	
//---------------------------------------------------------
	public TemaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Tema;}
	public void setId(String valor ) {if(valor!=null) Id_Tema = valor;}
	public String getTitulo()  {return Titulo;}
	public void setTitulo(String valor ) {if (valor!=null) Titulo = valor;}
	public String getTemaCodigo()  {return TemaCodigo;}
	public void setTemaCodigo(String valor ) {if (valor!=null) TemaCodigo = valor;}
	public String getQuesDireito()  {return QuesDireito;}
	public void setQuesDireito(String valor ) {if (valor!=null) QuesDireito = valor;}
	public String getVinculantes()  {return Vinculantes;}
	public void setVinculantes(String valor ) {if (valor!=null) Vinculantes = valor;}
	public String getId_TemaSituacao()  {return Id_TemaSituacao;}
	public void setId_TemaSituacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_TemaSituacao = ""; Id_TemaOrigem = "";}else if (!valor.equalsIgnoreCase("")) Id_TemaSituacao = valor;}
	public String getId_TemaOrigem()  {return Id_TemaOrigem;}
	public void setId_TemaOrigem(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_TemaOrigem = ""; Id_TemaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_TemaOrigem = valor;}
	public String getId_TemaTipo()  {return Id_TemaTipo;}
	public void setId_TemaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_TemaTipo = ""; TemaSituacao = "";}else if (!valor.equalsIgnoreCase("")) Id_TemaTipo = valor;}
	public String getTemaSituacao()  {return TemaSituacao;}
	public void setTemaSituacao(String valor ) {if (valor!=null) TemaSituacao = valor;}
	public String getTemaOrigem()  {return TemaOrigem;}
	public void setTemaOrigem(String valor ) {if (valor!=null) TemaOrigem = valor;}
	public String getTemaTipo()  {return TemaTipo;}
	public void setTemaTipo(String valor ) {if (valor!=null) TemaTipo = valor;}
	public String getDataTransito() {return dataTransito;}
	public void setDataTransito(String dataTransito) {if (dataTransito != null) this.dataTransito = dataTransito;}
	public String getTeseFirmada() { return TeseFirmada;}
	public void setTeseFirmada(String valor) {if (valor!=null) TeseFirmada = valor;}
	public String getInfoLegislativa() {return InfoLegislativa;}
	public void setInfoLegislativa(String valor) {if (valor!=null) InfoLegislativa = valor;}
	public String getNumeroIrdrCnj() {return NumeroIrdrCnj;}
	public void setNumeroIrdrCnj(String valor) {if (valor != null) NumeroIrdrCnj = valor;}	
	public String getDataAdmissao() {return DataAdmissao;}
	public void setDataAdmissao(String valor) {if (valor!=null) DataAdmissao = valor;}
	public String getSuspensao() {return Suspensao;}
	public void setSuspensao(String valor) {if (valor!=null) Suspensao = valor;}	
	public String getTemaSituacaoCnj() { return TemaSituacaoCnj;}
	public void setTemaSituacaoCnj(String valor) { if (valor!=null) TemaSituacaoCnj = valor; }	
	public String getTemaTipoCnj() { return TemaTipoCnj; }
	public void setTemaTipoCnj(String valor) { if (valor!=null) TemaTipoCnj = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getOpcaoProcessual()  {return OpcaoProcessual;}
	public void setOpcaoProcessual(String valor ) {if (valor!=null) OpcaoProcessual = valor;}
	
	public void copiar(TemaDt objeto){
		if (objeto==null) return;
		Id_Tema = objeto.getId();
		Titulo = objeto.getTitulo();
		TemaCodigo = objeto.getTemaCodigo();
		QuesDireito = objeto.getQuesDireito();
		Vinculantes = objeto.getVinculantes();
		Id_TemaSituacao = objeto.getId_TemaSituacao();
		Id_TemaOrigem = objeto.getId_TemaOrigem();
		Id_TemaTipo = objeto.getId_TemaTipo();
		TemaSituacao = objeto.getTemaSituacao();
		TemaOrigem = objeto.getTemaOrigem();
		TemaTipo = objeto.getTemaTipo();
		dataTransito = objeto.getDataTransito();
		TeseFirmada = objeto.getTeseFirmada();
		InfoLegislativa = objeto.getInfoLegislativa();
		NumeroIrdrCnj = objeto.getNumeroIrdrCnj();		
		DataAdmissao = objeto.getDataAdmissao();
		Suspensao = objeto.getSuspensao();
		TemaSituacaoCnj = objeto.getTemaSituacaoCnj();
		TemaTipoCnj = objeto.getTemaTipoCnj();
		CodigoTemp = objeto.getCodigoTemp();
		OpcaoProcessual = objeto.getOpcaoProcessual();
	}

	public void limpar(){
		Id_Tema="";
		Titulo="";
		TemaCodigo="";
		QuesDireito="";
		Vinculantes="";
		Id_TemaSituacao="";
		Id_TemaOrigem="";
		Id_TemaTipo="";
		TemaSituacao="";
		TemaOrigem="";
		TemaTipo="";
		dataTransito="";
		TeseFirmada="";
		InfoLegislativa="";
		NumeroIrdrCnj="";		
		DataAdmissao="";
		Suspensao="";
		TemaSituacaoCnj="";
		TemaTipoCnj="";
		CodigoTemp="";
		OpcaoProcessual="";
	}

	public String getPropriedades(){
		return "[Id_Tema:" + Id_Tema + 
				";Titulo:" + Titulo + 
				";TemaCodigo:" + TemaCodigo + 
				";QuesDireito:" + QuesDireito + 
				";Vinculantes:" + Vinculantes + 
				";Id_TemaSituacao:" + Id_TemaSituacao + 
				";Id_TemaOrigem:" + Id_TemaOrigem + 
				";Id_TemaTipo:" + Id_TemaTipo + 
				";TemaSituacao:" + TemaSituacao + 
				";TemaOrigem:" + TemaOrigem + 
				";TemaTipo:" + TemaTipo +
				";DataTransito:" + dataTransito + 
				";TeseFirmada:" + TeseFirmada +  
				";InfoLegislativa:" + InfoLegislativa +  
				";NumeroIrdrCnj" + NumeroIrdrCnj + 				
				";DataAdmissao" + DataAdmissao + 
				";Suspensao" + Suspensao + 
				";TemaSituacaoCnj" + TemaSituacaoCnj +
				";TemaTipoCnj" + TemaTipoCnj +
				";CodigoTemp:" + CodigoTemp +
				";OpcaoProcessual:" + OpcaoProcessual + "]";
	}


} 
