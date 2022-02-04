package br.gov.go.tj.projudi.dt;

public class ProcessoDtGen extends Dados{

    private static final long serialVersionUID = 1228380010282766467L;
    private String Id_Processo;
	private String ProcessoNumero;
	private String Id_ProcessoPrincipal;
	private String ProcessoNumeroPrincipal;
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String Id_ProcessoFase;
	private String ProcessoFase;
	private String Id_ProcessoStatus;
	private String ProcessoStatus;
	private String Id_ProcessoPrioridade;
	private String ProcessoPrioridade;
	private String Id_Serventia;
	private String Serventia;
	private String Id_ServentiaOrigem;
	private String ServentiaOrigem;
	private String Id_Area;
	private String Area;
	private String Id_ObjetoPedido;
	private String ObjetoPedido;
	private String Id_Classificador;
	private String Classificador;
	private String SegredoJustica;
	private String Sigiloso;
	private String ProcessoDiretorio;
	private String TcoNumero;
	private String Valor;
	private String DataRecebimento;
	private String DataArquivamento;
	private String DataPrescricao;
	private String Apenso;
	private String Ano;
	private String ForumCodigo;
	private String Id_AreaDistribuicao;
	private String ProcessoTipoCodigo;
	private String ProcessoFaseCodigo;
	private String ProcessoStatusCodigo;
	private String ProcessoPrioridadeCodigo;
	private String ServentiaCodigo;
	private String ServentiaOrigemCodigo;
	private String AreaCodigo;
	private String ObjetoPedidoCodigo;
	private String DigitoVerificador;
	private String EfeitoSuspensivo;
	private String Penhora;
	private String DataTransitoJulgado;
	private String Julgado2Grau;
	private String ValorCondenacao;
	protected String Id_Custa_Tipo;

	//---------------------------------------------------------
	public ProcessoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Processo;}
	public void setId(String valor ) {if(valor!=null) Id_Processo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getDigitoVerificador()  {return DigitoVerificador;}
    public void setDigitoVerificador(String valor ) {if (valor!=null) DigitoVerificador = valor;}
	public String getId_ProcessoPrincipal()  {return Id_ProcessoPrincipal;}
	public void setId_ProcessoPrincipal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoPrincipal = ""; ProcessoNumeroPrincipal = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoPrincipal = valor;}
	public String getProcessoNumeroPrincipal()  {return ProcessoNumeroPrincipal;}
	public void setProcessoNumeroPrincipal(String valor ) {if (valor!=null) ProcessoNumeroPrincipal = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getId_ProcessoFase()  {return Id_ProcessoFase;}
	public void setId_ProcessoFase(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoFase = ""; ProcessoFase = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoFase = valor;}
	public String getProcessoFase()  {return ProcessoFase;}
	public void setProcessoFase(String valor ) {if (valor!=null) ProcessoFase = valor;}
	public String getId_ProcessoStatus()  {return Id_ProcessoStatus;}
	public void setId_ProcessoStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoStatus = ""; ProcessoStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoStatus = valor;}
	public String getProcessoStatus()  {return ProcessoStatus;}
	public void setProcessoStatus(String valor ) {if (valor!=null) ProcessoStatus = valor;}
	public String getId_ProcessoPrioridade()  {return Id_ProcessoPrioridade;}
	public void setId_ProcessoPrioridade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoPrioridade = ""; ProcessoPrioridade = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoPrioridade = valor;}
	public String getProcessoPrioridade()  {return ProcessoPrioridade;}
	public void setProcessoPrioridade(String valor ) {if (valor!=null) ProcessoPrioridade = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_ServentiaOrigem()  {return Id_ServentiaOrigem;}
	public void setId_ServentiaOrigem(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaOrigem = ""; ServentiaOrigem = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaOrigem = valor;}
	public String getServentiaOrigem()  {return ServentiaOrigem;}
	public void setServentiaOrigem(String valor ) {if (valor!=null) ServentiaOrigem = valor;}
	public String getId_Area()  {return Id_Area;}
	public void setId_Area(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Area = ""; Area = "";}else if (!valor.equalsIgnoreCase("")) Id_Area = valor;}
	public String getArea()  {return Area;}
	public void setArea(String valor ) {if (valor!=null) Area = valor;}
	public String getId_ObjetoPedido()  {return Id_ObjetoPedido;}
	public void setId_ObjetoPedido(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ObjetoPedido = ""; ObjetoPedido = "";}else if (!valor.equalsIgnoreCase("")) Id_ObjetoPedido = valor;}
	public String getObjetoPedido()  {return ObjetoPedido;}
	public void setObjetoPedido(String valor ) {if (valor!=null) ObjetoPedido = valor;}
	public String getId_Classificador()  {return Id_Classificador;}
	public void setId_Classificador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Classificador = ""; Classificador = "";}else if (!valor.equalsIgnoreCase("")) Id_Classificador = valor;}
	public String getClassificador()  {return Classificador;}
	public void setClassificador(String valor ) {if (valor!=null) Classificador = valor;}
	public String getSegredoJustica()  {return SegredoJustica;}
	public void setSegredoJustica(String valor ) {if (valor!=null) SegredoJustica = valor; else SegredoJustica="false";}
	public String getProcessoDiretorio()  {return ProcessoDiretorio;}
	public void setProcessoDiretorio(String valor ) {if (valor!=null) ProcessoDiretorio = valor;}
	public String getTcoNumero()  {return TcoNumero;}
	public void setTcoNumero(String valor ) {if (valor!=null) TcoNumero = valor;}
	public String getValor()  {return Valor;}
	public void setValor(String valor ) {if (valor!=null) Valor = valor;}
	public String getDataRecebimento()  {return DataRecebimento;}
	public void setDataRecebimento(String valor ) {if (valor!=null) DataRecebimento = valor;}
	public String getDataArquivamento()  {return DataArquivamento;}
	public void setDataArquivamento(String valor ) {if (valor!=null) DataArquivamento = valor;}
	public String getApenso()  {return Apenso;}
	public void setApenso(String valor ) {if (valor!=null) Apenso = valor;}
	public String getAno()  {return Ano;}
	public void setAno(String valor ) {if (valor!=null) Ano = valor;}
	public String getForumCodigo()  {return ForumCodigo;}
	public void setForumCodigo(String valor ) {if (valor!=null) ForumCodigo = valor;}
	public String getId_AreaDistribuicao()  {return Id_AreaDistribuicao;}
	public void setId_AreaDistribuicao(String valor ) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				Id_AreaDistribuicao = ""; 
				ProcessoTipoCodigo = "";
			}else if (!valor.equalsIgnoreCase("")) 
				Id_AreaDistribuicao = valor;
		}
	public String getProcessoTipoCodigo()  {return ProcessoTipoCodigo;}
	public void setProcessoTipoCodigo(String valor ) {if (valor!=null) ProcessoTipoCodigo = valor;}
	public String getProcessoFaseCodigo()  {return ProcessoFaseCodigo;}
	public void setProcessoFaseCodigo(String valor ) {if (valor!=null) ProcessoFaseCodigo = valor;}
	public String getProcessoStatusCodigo()  {return ProcessoStatusCodigo;}
	public void setProcessoStatusCodigo(String valor ) {if (valor!=null) ProcessoStatusCodigo = valor;}
	public String getProcessoPrioridadeCodigo()  {return ProcessoPrioridadeCodigo;}
	public void setProcessoPrioridadeCodigo(String valor ) {if (valor!=null) ProcessoPrioridadeCodigo = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	public String getServentiaOrigemCodigo()  {return ServentiaOrigemCodigo;}
	public void setServentiaOrigemCodigo(String valor ) {if (valor!=null) ServentiaOrigemCodigo = valor;}
	public String getAreaCodigo()  {return AreaCodigo;}
	public void setAreaCodigo(String valor ) {if (valor!=null) AreaCodigo = valor;}
	public String getObjetoPedidoCodigo()  {return ObjetoPedidoCodigo;}
	public void setObjetoPedidoCodigo(String valor ) {if (valor!=null) ObjetoPedidoCodigo = valor;}
	public String getEfeitoSuspensivo()  {return EfeitoSuspensivo;}
	public void setEfeitoSuspensivo(String valor ) {if (valor!=null) EfeitoSuspensivo = valor;}
	public String getPenhora()  {return Penhora;}
	public void setPenhora(String valor ) {if (valor!=null) Penhora = valor;}	
	public String getDataTransitoJulgado()  {return DataTransitoJulgado;}
	public void setDataTransitoJulgado(String valor ) {if (valor!=null) DataTransitoJulgado = valor;}
	public String getJulgado2Grau() {return Julgado2Grau;}
	public void setJulgado2Grau(String valor) {if (valor!=null)Julgado2Grau = valor;}
	public String getValorCondenacao() {return ValorCondenacao;}	
	public void setValorCondenacao(String valor) {if (valor!=null) ValorCondenacao = valor;}
	public String getId_Custa_Tipo() {return Id_Custa_Tipo;}
	public void setId_Custa_Tipo(String valor) {if (valor!=null) Id_Custa_Tipo = valor;}
	public String getSigiloso() { return Sigiloso; }
	public void setSigiloso(String valor) { if (valor!=null) Sigiloso = valor;  else Sigiloso = "false";}
	
	
	public String getDataPrescricao()  {
		return DataPrescricao;
	}
	
	public void setDataPrescricao(String valor ) {
		if (valor!=null) 
			DataPrescricao = valor;
	}

	public void copiar(ProcessoDt objeto){
		if (objeto==null){
			return;
		}
		Id_Processo = objeto.getId();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_ProcessoPrincipal = objeto.getId_ProcessoPrincipal();
		ProcessoNumeroPrincipal = objeto.getProcessoNumeroPrincipal();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
		Id_ProcessoFase = objeto.getId_ProcessoFase();
		ProcessoFase = objeto.getProcessoFase();
		Id_ProcessoStatus = objeto.getId_ProcessoStatus();
		ProcessoStatus = objeto.getProcessoStatus();
		Id_ProcessoPrioridade = objeto.getId_ProcessoPrioridade();
		ProcessoPrioridade = objeto.getProcessoPrioridade();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_ServentiaOrigem = objeto.getId_ServentiaOrigem();
		ServentiaOrigem = objeto.getServentiaOrigem();
		Id_Area = objeto.getId_Area();
		Area = objeto.getArea();
		Id_ObjetoPedido = objeto.getId_ObjetoPedido();
		ObjetoPedido = objeto.getObjetoPedido();
		Id_Classificador = objeto.getId_Classificador();
		Classificador = objeto.getClassificador();
		SegredoJustica = objeto.getSegredoJustica();
		ProcessoDiretorio = objeto.getProcessoDiretorio();
		TcoNumero = objeto.getTcoNumero();
		Valor = objeto.getValor();
		DataRecebimento = objeto.getDataRecebimento();
		DataArquivamento = objeto.getDataArquivamento();
		DataPrescricao = objeto.getDataPrescricao();
		Apenso = objeto.getApenso();
		Ano = objeto.getAno();
		ForumCodigo = objeto.getForumCodigo();
		CodigoTemp = objeto.getCodigoTemp();
		Id_AreaDistribuicao = objeto.getId_AreaDistribuicao();
		ProcessoTipoCodigo = objeto.getProcessoTipoCodigo();
		ProcessoFaseCodigo = objeto.getProcessoFaseCodigo();
		ProcessoStatusCodigo = objeto.getProcessoStatusCodigo();
		ProcessoPrioridadeCodigo = objeto.getProcessoPrioridadeCodigo();
		ServentiaCodigo = objeto.getServentiaCodigo();
		ServentiaOrigemCodigo = objeto.getServentiaOrigemCodigo();
		AreaCodigo = objeto.getAreaCodigo();
		ObjetoPedidoCodigo = objeto.getObjetoPedidoCodigo();
		EfeitoSuspensivo = objeto.getEfeitoSuspensivo();
		Penhora = objeto.getPenhora();
		DataTransitoJulgado = objeto.getDataTransitoJulgado();
		Julgado2Grau = objeto.getJulgado2Grau();
		ValorCondenacao = objeto.getValorCondenacao();
		Id_Custa_Tipo = objeto.getId_Custa_Tipo();
	}

	public void limpar(){
		Id_Processo="";
		ProcessoNumero="";
		Id_ProcessoPrincipal="";
		ProcessoNumeroPrincipal="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		Id_ProcessoFase="";
		ProcessoFase="";
		Id_ProcessoStatus="";
		ProcessoStatus="";
		Id_ProcessoPrioridade="";
		ProcessoPrioridade="";
		Id_Serventia="";
		Serventia="";
		Id_ServentiaOrigem="";
		ServentiaOrigem="";
		Id_Area="";
		Area="";
		Id_ObjetoPedido="";
		ObjetoPedido="";
		Id_Classificador="";
		Classificador="";
		SegredoJustica="";
		ProcessoDiretorio="";
		TcoNumero="";
		Valor="";
		DataRecebimento="";
		DataArquivamento="";
		DataPrescricao = "";
		Apenso="";
		Ano="";
		ForumCodigo="";
		DigitoVerificador="";
		CodigoTemp="";
		Id_AreaDistribuicao="";
		ProcessoTipoCodigo="";
		ProcessoFaseCodigo="";
		ProcessoStatusCodigo="";
		ProcessoPrioridadeCodigo="";
		ServentiaCodigo="";
		ServentiaOrigemCodigo="";
		AreaCodigo="";
		ObjetoPedidoCodigo="";
		EfeitoSuspensivo = "";
		Penhora = "";
		DataTransitoJulgado = "";
		Julgado2Grau = "";
		ValorCondenacao = "";
		Id_Custa_Tipo = "";
	}

	public String getPropriedades(){
		return "[Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";DigitoVerificador:" + DigitoVerificador + ";Id_ProcessoDependente:" + Id_ProcessoPrincipal + ";ProcessoNumeroDependente:" + ProcessoNumeroPrincipal + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";Id_ProcessoFase:" + Id_ProcessoFase + ";ProcessoFase:" + ProcessoFase + ";Id_ProcessoStatus:" + Id_ProcessoStatus + ";ProcessoStatus:" + ProcessoStatus + ";Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";ProcessoPrioridade:" + ProcessoPrioridade + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_ServentiaOrigem:" + Id_ServentiaOrigem + ";ServentiaOrigem:" + ServentiaOrigem + ";Id_Area:" + Id_Area + ";Area:" + Area + ";Id_ObjetoPedido:" + Id_ObjetoPedido + ";ObjetoPedido:" + ObjetoPedido + ";Id_Classificador:" + Id_Classificador + ";Classificador:" + Classificador + ";SegredoJustica:" + SegredoJustica + ";ProcessoDiretorio:" + ProcessoDiretorio + ";TcoNumero:" + TcoNumero + ";Valor:" + Valor + ";DataRecebimento:" + DataRecebimento + ";DataArquivamento:" + DataArquivamento +";DataPrescricao:"+ DataPrescricao + ";Apenso:" + Apenso + ";Ano:" + Ano + ";ForumCodigo:" + ForumCodigo + ";CodigoTemp:" + CodigoTemp + ";Id_AreaDistribuicao:" + Id_AreaDistribuicao + ";ProcessoTipoCodigo:" + ProcessoTipoCodigo + ";ProcessoFaseCodigo:" + ProcessoFaseCodigo + ";ProcessoStatusCodigo:" + ProcessoStatusCodigo + ";ProcessoPrioridadeCodigo:" + ProcessoPrioridadeCodigo + ";ServentiaCodigo:" + ServentiaCodigo + ";ServentiaOrigemCodigo:" + ServentiaOrigemCodigo + ";AreaCodigo:" + AreaCodigo + ";ObjetoPedidoCodigo:" + ObjetoPedidoCodigo + ";EfeitoSuspensivo:" + EfeitoSuspensivo + ";Penhora:" + Penhora + ";DataTransitoJulgado:" + DataTransitoJulgado + ";Julgado2Grau:" + Julgado2Grau + ";ValorCondenacao:" + ValorCondenacao + ";Id_Custa_Tipo:" + Id_Custa_Tipo + "]";
	}

} 
