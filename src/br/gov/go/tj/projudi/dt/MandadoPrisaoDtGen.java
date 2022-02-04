package br.gov.go.tj.projudi.dt;

public class MandadoPrisaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5636999557316709536L;
	private String Id_MandadoPrisao;
	private String MandadoPrisaoNumero;


//	private String DataDelito;
	private String DataExpedicao;
//	private String MandadoPrisaoNumeroAnterior;
	private String PenaImposta;
	private String PrazoPrisao;
//	private String Recaptura;
	private String Sigilo;
	private String SinteseDecisao;
	private String Id_MandadoPrisaoStatus;
	private String MandadoPrisaoStatus;
	private String ValorFianca;
	private String Id_RegimeExecucao;
	private String RegimeExecucao;
	private String Id_PrisaoTipo;
	private String PrisaoTipo;
	private String Id_ProcessoParte;
	private String ProcessoParte;
	private String Id_Assunto;
	private String Assunto;
	private String DataValidade;
	private String Origem;
	private String Id_MandadoPrisaoOrigem;
	private String MandadoPrisaoOrigem;
	private String CodigoTemp;
	private String Id_Processo;
	private String ProcessoTipo;
	private String ProcessoNumero;
	private String DigitoVerificador;
	private String Ano;
	private String DataNascimento;
	private String NomeMae;
	private String NomePai;
	private String ufNaturalidade;
	private String Sexo;
	private String Cpf;
	private String Naturalidade;
	private String DataAtualizacao;
	private String LocalRecolhimento;
	private String NumeroOrigem;
	private String DataCumprimento;

//---------------------------------------------------------
	public MandadoPrisaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_MandadoPrisao;}
	public void setId(String valor ) {if(valor!=null) Id_MandadoPrisao = valor;}
	public String getMandadoPrisaoNumero()  {return MandadoPrisaoNumero;}
	public void setMandadoPrisaoNumero(String valor ) {if (valor!=null) MandadoPrisaoNumero = valor;}
//	public String getDataDelito()  {return DataDelito;}
//	public void setDataDelito(String valor ) {if (valor!=null) DataDelito = valor;}
	public String getDataExpedicao()  {return DataExpedicao;}
	public void setDataExpedicao(String valor ) {if (valor!=null) DataExpedicao = valor;}
//	public String getMandadoPrisaoNumeroAnterior()  {return MandadoPrisaoNumeroAnterior;}
//	public void setMandadoPrisaoNumeroAnterior(String valor ) {if (valor!=null) MandadoPrisaoNumeroAnterior = valor;}
	public String getPenaImposta()  {return PenaImposta;}
	public void setPenaImposta(String valor ) {if (valor!=null) PenaImposta = valor;}
	public String getPrazoPrisao()  {return PrazoPrisao;}
	public void setPrazoPrisao(String valor ) {if (valor!=null) PrazoPrisao = valor;}
//	public String getRecaptura()  {return Recaptura;}
//	public void setRecaptura(String valor ) {if (valor!=null) Recaptura = valor;}
	public String getSigilo()  {return Sigilo;}
	public void setSigilo(String valor ) {if (valor!=null) Sigilo = valor;}
	public String getSinteseDecisao()  {return SinteseDecisao;}
	public void setSinteseDecisao(String valor ) {if (valor!=null) SinteseDecisao = valor;}
	public String getId_MandadoPrisaoStatus()  {return Id_MandadoPrisaoStatus;}
	public void setId_MandadoPrisaoStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MandadoPrisaoStatus = ""; MandadoPrisaoStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_MandadoPrisaoStatus = valor;}
	public String getMandadoPrisaoStatus()  {return MandadoPrisaoStatus;}
	public void setMandadoPrisaoStatus(String valor ) {if (valor!=null) MandadoPrisaoStatus = valor;}
	public String getValorFianca()  {return ValorFianca;}
	public void setValorFianca(String valor ) {if (valor!=null) ValorFianca = valor;}
	public String getId_RegimeExecucao()  {return Id_RegimeExecucao;}
	public void setId_RegimeExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_RegimeExecucao = ""; RegimeExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_RegimeExecucao = valor;}
	public String getRegimeExecucao()  {return RegimeExecucao;}
	public void setRegimeExecucao(String valor ) {if (valor!=null) RegimeExecucao = valor;}
	public String getId_PrisaoTipo()  {return Id_PrisaoTipo;}
	public void setId_PrisaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PrisaoTipo = ""; PrisaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PrisaoTipo = valor;}
	public String getPrisaoTipo()  {return PrisaoTipo;}
	public void setPrisaoTipo(String valor ) {if (valor!=null) PrisaoTipo = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; ProcessoParte = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getProcessoParte()  {return ProcessoParte;}
	public void setProcessoParte(String valor ) {if (valor!=null) ProcessoParte = valor;}
	public String getId_Assunto()  {return Id_Assunto;}
	public void setId_Assunto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Assunto = ""; Assunto = "";}else if (!valor.equalsIgnoreCase("")) Id_Assunto = valor;}
	public String getAssunto()  {return Assunto;}
	public void setAssunto(String valor ) {if (valor!=null) Assunto = valor;}
	public String getDataValidade()  {return DataValidade;}
	public void setDataValidade(String valor ) {if (valor!=null) DataValidade = valor;}
	public String getOrigem()  {return Origem;}
	public void setOrigem(String valor ) {if (valor!=null) Origem = valor;}
	public String getId_MandadoPrisaoOrigem()  {return Id_MandadoPrisaoOrigem;}
	public void setId_MandadoPrisaoOrigem(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MandadoPrisaoOrigem = ""; MandadoPrisaoOrigem = "";}else if (!valor.equalsIgnoreCase("")) Id_MandadoPrisaoOrigem = valor;}
	public String getMandadoPrisaoOrigem()  {return MandadoPrisaoOrigem;}
	public void setMandadoPrisaoOrigem(String valor ) {if (valor!=null) MandadoPrisaoOrigem = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getDigitoVerificador()  {return DigitoVerificador;}
	public void setDigitoVerificador(String valor ) {if (valor!=null) DigitoVerificador = valor;}
	public String getAno()  {return Ano;}
	public void setAno(String valor ) {if (valor!=null) Ano = valor;}
	public String getDataNascimento()  {return DataNascimento;}
	public void setDataNascimento(String valor ) {if (valor!=null) DataNascimento = valor;}
	public String getNomeMae()  {return NomeMae;}
	public void setNomeMae(String valor ) {if (valor!=null) NomeMae = valor;}
	public String getNomePai()  {return NomePai;}
	public void setNomePai(String valor ) {if (valor!=null) NomePai = valor;}
	public String getUfNaturalidade()  {return ufNaturalidade;}
	public void setUfNaturalidade(String valor ) {if (valor!=null) ufNaturalidade = valor;}
	public String getSexo()  {return Sexo;}
	public void setSexo(String valor ) {if (valor!=null) Sexo = valor;}
	public String getCpf()  {return Cpf;}
	public void setCpf(String valor ) {if (valor!=null) Cpf = valor;}
	public String getNaturalidade()  {return Naturalidade;}
	public void setNaturalidade(String valor ) {if (valor!=null) Naturalidade = valor;}
	public String getDataAtualizacao()  {return DataAtualizacao;}
	public void setDataAtualizacao(String valor ) {if (valor!=null) DataAtualizacao = valor;}
	public String getLocalRecolhimento()  {return LocalRecolhimento;}
	public void setLocalRecolhimento(String valor ) {if (valor!=null) LocalRecolhimento = valor;}
	public String getNumeroOrigem()  {return NumeroOrigem;}
	public void setNumeroOrigem(String valor ) {if (valor!=null) NumeroOrigem = valor;}
	public String getDataCumprimento() {return DataCumprimento;}
	public void setDataCumprimento(String dataCumprimento) {DataCumprimento = dataCumprimento;}

	public void copiar(MandadoPrisaoDt objeto){
		 if (objeto==null) return;
		Id_MandadoPrisao = objeto.getId();
		MandadoPrisaoNumero = objeto.getMandadoPrisaoNumero();
//		DataDelito = objeto.getDataDelito();
		DataExpedicao = objeto.getDataExpedicao();
//		MandadoPrisaoNumeroAnterior = objeto.getMandadoPrisaoNumeroAnterior();
		PenaImposta = objeto.getPenaImposta();
		PrazoPrisao = objeto.getPrazoPrisao();
//		Recaptura = objeto.getRecaptura();
		Sigilo = objeto.getSigilo();
		SinteseDecisao = objeto.getSinteseDecisao();
		Id_MandadoPrisaoStatus = objeto.getId_MandadoPrisaoStatus();
		MandadoPrisaoStatus = objeto.getMandadoPrisaoStatus();
		ValorFianca = objeto.getValorFianca();
		Id_RegimeExecucao = objeto.getId_RegimeExecucao();
		RegimeExecucao = objeto.getRegimeExecucao();
		Id_PrisaoTipo = objeto.getId_PrisaoTipo();
		PrisaoTipo = objeto.getPrisaoTipo();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		ProcessoParte = objeto.getProcessoParte();
		Id_Assunto = objeto.getId_Assunto();
		Assunto = objeto.getAssunto();
		DataValidade = objeto.getDataValidade();
		Origem = objeto.getOrigem();
		Id_MandadoPrisaoOrigem = objeto.getId_MandadoPrisaoOrigem();
		MandadoPrisaoOrigem = objeto.getMandadoPrisaoOrigem();
		CodigoTemp = objeto.getCodigoTemp();
		Id_Processo = objeto.getId_Processo();
		ProcessoTipo = objeto.getProcessoTipo();
		ProcessoNumero = objeto.getProcessoNumero();
		DigitoVerificador = objeto.getDigitoVerificador();
		Ano = objeto.getAno();
		DataNascimento = objeto.getDataNascimento();
		NomeMae = objeto.getNomeMae();
		NomePai = objeto.getNomePai();
		ufNaturalidade = objeto.getUfNaturalidade();
		Sexo = objeto.getSexo();
		Cpf = objeto.getCpf();
		Naturalidade = objeto.getNaturalidade();
		DataAtualizacao = objeto.getDataAtualizacao();
		LocalRecolhimento = objeto.getLocalRecolhimento();
		NumeroOrigem = objeto.getNumeroOrigem();
		DataCumprimento = objeto.getDataCumprimento();
	}

	public void limpar(){
		Id_MandadoPrisao="";
		MandadoPrisaoNumero="";
//		DataDelito="";
		DataExpedicao="";
//		MandadoPrisaoNumeroAnterior="";
		PenaImposta="";
		PrazoPrisao="";
//		Recaptura="";
		Sigilo="";
		SinteseDecisao="";
		Id_MandadoPrisaoStatus="";
		MandadoPrisaoStatus="";
		ValorFianca="";
		Id_RegimeExecucao="";
		RegimeExecucao="";
		Id_PrisaoTipo="";
		PrisaoTipo="";
		Id_ProcessoParte="";
		ProcessoParte="";
		Id_Assunto="";
		Assunto="";
		DataValidade="";
		Origem="";
		Id_MandadoPrisaoOrigem="";
		MandadoPrisaoOrigem="";
		CodigoTemp="";
		Id_Processo="";
		ProcessoTipo="";
		ProcessoNumero="";
		DigitoVerificador="";
		Ano="";
		DataNascimento="";
		NomeMae="";
		NomePai="";
		ufNaturalidade="";
		Sexo="";
		Cpf="";
		Naturalidade="";
		DataAtualizacao="";
		LocalRecolhimento="";
		NumeroOrigem="";
		DataCumprimento = "";
	}


	public String getPropriedades(){
		return "[Id_MandadoPrisao:" + Id_MandadoPrisao 
				+ ";MandadoPrisaoNumero:" + MandadoPrisaoNumero 
//				+ ";DataDelito:" + DataDelito 
				+ ";DataExpedicao:" + DataExpedicao 
//				+ ";MandadoPrisaoNumeroAnterior:" + MandadoPrisaoNumeroAnterior 
				+ ";PenaImposta:" + PenaImposta 
				+ ";PrazoPrisao:" + PrazoPrisao 
//				+ ";Recaptura:" + Recaptura
				+ ";Sigilo:" + Sigilo 
				+ ";SinteseDecisao:" + SinteseDecisao 
				+ ";Id_MandadoPrisaoStatus:" + Id_MandadoPrisaoStatus 
				+ ";MandadoPrisaoStatus:" + MandadoPrisaoStatus 
				+ ";ValorFianca:" + ValorFianca 
				+ ";Id_RegimeExecucao:" + Id_RegimeExecucao 
				+ ";RegimeExecucao:" + RegimeExecucao 
				+ ";Id_PrisaoTipo:" + Id_PrisaoTipo 
				+ ";PrisaoTipo:" + PrisaoTipo 
				+ ";Id_ProcessoParte:" + Id_ProcessoParte 
				+ ";ProcessoParte:" + ProcessoParte 
				+ ";Id_Assunto:" + Id_Assunto 
				+ ";Assunto:" + Assunto 
				+ ";DataValidade:" + DataValidade 
				+ ";Origem:" + Origem 
				+ ";Id_MandadoPrisaoOrigem:" + Id_MandadoPrisaoOrigem 
				+ ";MandadoPrisaoOrigem:" + MandadoPrisaoOrigem 
				+ ";CodigoTemp:" + CodigoTemp 
				+ ";Id_Processo:" + Id_Processo 
				+ ";ProcessoTipo:" + ProcessoTipo 
				+ ";ProcessoNumero:" + ProcessoNumero 
				+ ";DigitoVerificador:" + DigitoVerificador 
				+ ";Ano:" + Ano 
				+ ";DataNascimento:" + DataNascimento 
				+ ";NomeMae:" + NomeMae 
				+ ";NomePai:" + NomePai 
				+ ";Profissao:" + ufNaturalidade 
				+ ";Sexo:" + Sexo 
				+ ";Cpf:" + Cpf 
				+ ";Naturalidade:" + Naturalidade 
				+ ";DataAtualizacao:" + DataAtualizacao 
				+ ";LocalRecolhimento:" + LocalRecolhimento
				+ ";DataCumprimento:" + DataCumprimento
				+ ";NumeroOrigem:" + NumeroOrigem + "]";
	}


} 
