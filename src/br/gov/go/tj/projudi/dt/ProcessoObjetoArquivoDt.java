package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoObjetoArquivoDt extends ProcessoObjetoArquivoDtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2419407789868264111L;
	public static final int CodigoPermissao=346;
	
	private static final int PODER_JUDICIARIO = 0;
	private static final int UNIAO = 1;	
	private static final int ESTADO_GOIAS = 2;
	private static final int JUSTICA_FEDERAL = 3;
	
	protected String Id_ProcessoObjetoArquivoMovimentacao;
	protected String ProcessoObjetoArquivoMovimentacao;
	protected String DataMovimentacao;
	protected String DataRetorno;
	
	public String toJson(){
		return "{\"Id\":\"" + getId() 
		+ "\",\"ProcObjetoArq\":\"" + getProcObjetoArq() 
		+ "\",\"ObjetoSubtipo\":\"" + getObjetoSubtipo() 
		+ "\",\"DataEntrada\":\"" + getDataEntrada() 
		+ "\",\"StatusBaixa\":\"" + getStatusBaixa() 
		+ "\",\"DataBaixa\":\"" + getDataBaixa() 
		+ "\",\"NomeDepositante\":\"" + getNomeDepositante()
		+ "\",\"Delegacia\":\"" + getDelegacia() 
		+ "\",\"Serventiaarquivo\":\"" + getServentiaarquivo() 
		+ "\",\"ProcNumero\":\"" + getProcNumero() 
		+ "\",\"Inquerito\":\"" + getInquerito()
		+ "\",\"CodigoLote\":\"" + getCodigoLote() 
		+ "\",\"Placa\":\"" + getPlaca() 
		+ "\",\"Chassi\":\"" + getChassi() 
		+ "\",\"Modulo\":\"" + getModulo() 
		+ "\",\"Perfil\":\"" + getPerfil() 
		+ "\",\"Nivel\":\"" + getNivel() 
		+ "\",\"Unidade\":\"" + getUnidade() 
		+ "\",\"Leilao\":\"" + getLeilao() 
		+ "\",\"StatusLeilao\":\"" + getStatusLeilao() 
		+ "\",\"NumeroRegistro\":\"" + getNumeroRegistro() 
		+ "\",\"NomeRecebedor\":\"" + getNomeRecebedor() 
		+ "\",\"CpfRecebedor\":\"" + getCpfRecebedor() 
		+ "\",\"RgRecebedor\":\"" + getRgRecebedor() 
		+ "\",\"RgOrgaoExp\":\"" + getRgOrgaoExp() 
		+ "\",\"Logradouro\":\"" + getLogradouro() 
		+ "\",\"CodigoTemp\":\"" + getCodigoTemp() 
		+ "\",\"Numero\":\"" + getNumero() 
		+ "\",\"Complemento\":\"" + getComplemento() 
		+ "\",\"Cep\":\"" + getCep() 
		+ "\",\"Bairro\":\"" + getBairro() 
		+ "\",\"Cidade\":\"" + getCidade() 
		+ "\",\"Uf\":\"" + getUf() 
		+ "\",\"Id_RgOrgaoExpRecebedor\":\"" + getId_RgOrgaoExpRecebedor() 
		+ "\",\"Id_Proc\":\"" + getId_Processo() 
		+ "\",\"Id_Delegacia\":\"" + getId_Delegacia() 
		+ "\",\"Id_EnderecoRecebedor\":\"" + getId_EnderecoRecebedor() 
		+ "\",\"Id_ServArquivo\":\"" + getId_ServArquivo() 
		+ "\",\"Id_ObjetoSubtipo\":\"" + getId_ObjetoSubtipo() 
		+ "\"}";
	}

	
	
	public ProcessoObjetoArquivoDt() {

		limpar();

	}
	
	public void limpar(){
		Id_ProcessoObjetoArquivoMovimentacao = "";
		ProcessoObjetoArquivoMovimentacao = "";
		DataMovimentacao = "";
		DataRetorno = "";
		super.limpar();
	}
	
	public String get_Id_ProcessoObjetoArquivoMovimentacao()  {
		return Id_ProcessoObjetoArquivoMovimentacao;
	}
	
	public void setId_Id_ProcessoObjetoArquivoMovimentacao(String valor ) { 
		if(valor!=null) {
			Id_ProcessoObjetoArquivoMovimentacao = valor;
		}
	}
	
	public String getProcessoObjetoArquivoMovimentacao()  {
		return ProcessoObjetoArquivoMovimentacao;
	}
	
	public void setProcessoObjetoArquivoMovimentacao(String valor ) { 
		if (valor!=null) {
			ProcessoObjetoArquivoMovimentacao = valor;
		}
	}
	
	public String getDataMovimentacao()  {
		return DataMovimentacao;
	}
	
	public void setDataMovimentacao(String valor ) { 
		if (valor!=null) {
			DataMovimentacao = valor;
		}
	}
	
	public String getDataRetorno()  {
		return DataRetorno;
	}
	
	public void setDataRetorno(String valor ) { 
		if (valor!=null) {
			DataRetorno = valor;
		}
	}

	public boolean isTemNomeRecebedor() {
		return NomeRecebedor!= null && !NomeRecebedor.isEmpty();
	}



	public boolean isTemCpf() {
		return CpfRecebedor!= null && !CpfRecebedor.isEmpty();
	}



	public boolean isTemRg() {
		return RgRecebedor!= null && !RgRecebedor.isEmpty();
	}



	public boolean isTemOrgaoExpedidor() {
		return Id_RgOrgaoExpRecebedor!= null && !Id_RgOrgaoExpRecebedor.isEmpty();
	}

	public boolean isEmLeilao() {
		return Funcoes.StringToBoolean(getStatusLeilao());
	}
	
	public String getFavorecidoLeilao() {
		
		int codigoFavorecido = Funcoes.StringToInt(getLeilao(),-1);
		
		switch (codigoFavorecido) {
			case PODER_JUDICIARIO: 	return "Poder Judiciário"; 
			case UNIAO:				return "União";
			case ESTADO_GOIAS:		return "Estado de Goiás";
			case JUSTICA_FEDERAL:	return "Justiça Federal";
			default: return "";
		}
				
	}
}
