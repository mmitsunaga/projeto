package br.gov.go.tj.projudi.dt;

public class ForumDt extends ForumDtGen{
	
	private static final long serialVersionUID = -1316462248033684878L;
    public static final int CodigoPermissao=167;
    
    private EnderecoDt EnderecoForum;
    
    public void copiar(ForumDt objeto){
    	super.copiar(objeto);		
		EnderecoForum = objeto.getEnderecoForum();
	}

	public void limpar(){
		super.limpar();
		EnderecoForum = new EnderecoDt();
	}
	
	public EnderecoDt getEnderecoForum() {
		return EnderecoForum;
	}

	public void setEnderecoForum(EnderecoDt enderecoForum) {
		EnderecoForum = enderecoForum;
	}


}
