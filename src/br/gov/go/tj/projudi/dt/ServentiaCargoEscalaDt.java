package br.gov.go.tj.projudi.dt;

public class ServentiaCargoEscalaDt extends ServentiaCargoEscalaDtGen{

    private static final long serialVersionUID = 8682927625670160370L;
    public static final int CodigoPermissao=190;

    public static final Integer ATIVO = 1;
    public static final Integer INATIVO = 0;
    
    public String dataVinculacao;
    public ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt;
    public String quantidadeDistribuicao;

    public String nomeUsuario;
    public String serventiaUsuario;
    
    public ServentiaCargoEscalaDt() {
    	this.limpar();
	}
    
    public void limpar(){
    	dataVinculacao="";
    	serventiaCargoEscalaStatusDt = new ServentiaCargoEscalaStatusDt();
    	quantidadeDistribuicao="";
    	nomeUsuario="";
    	serventiaUsuario="";
    	super.limpar();
	}
    
    
    public String getDataVinculacao() {
		return dataVinculacao;
	}

	public void setDataVinculacao(String dataVinculacao) {
		this.dataVinculacao = dataVinculacao;
	}

	public ServentiaCargoEscalaStatusDt getServentiaCargoEscalaStatusDt() {
		return serventiaCargoEscalaStatusDt;
	}

	public void setServentiaCargoEscalaStatusDt(ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt) {
		this.serventiaCargoEscalaStatusDt = serventiaCargoEscalaStatusDt;
	}

	public String getQuantidadeDistribuicao() {
        return quantidadeDistribuicao;
    }
    
    public void setQuantidadeDistribuicao(String valor) {
    	this.quantidadeDistribuicao = valor;
    }

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getServentiaUsuario() {
		return serventiaUsuario;
	}

	public void setServentiaUsuario(String serventiaUsuario) {
		this.serventiaUsuario = serventiaUsuario;
	}

}
