package br.gov.go.tj.projudi.dt;

public class UsuarioMovimentacaoTipoDt extends Dados {
	
	private static final long serialVersionUID = -823251249937064286L;
	
	public static final int CodigoPermissao = 684;
	
	private String Id_UsuarioMovimentacaoTipo;
	
	private String Id_MovimentacaoTipo;
	private String MovimentacaoTipo;
	private String MovimentacaoTipoCodigo;
	
	public UsuarioMovimentacaoTipoDt()
	{
		limpar();
	}

	@Override
	public String getId() {
		return Id_UsuarioMovimentacaoTipo;
	}

	@Override
	public void setId(String id) {
		if(id!=null){
			Id_UsuarioMovimentacaoTipo = id;
		}
	}

	public void setId_MovimentacaoTipo(String Id_MovimentacaoTipo) {
		if (Id_MovimentacaoTipo != null){
			if (Id_MovimentacaoTipo.equalsIgnoreCase("null")) {
				this.Id_MovimentacaoTipo = ""; 
				this.MovimentacaoTipo = "";
			}else if (!Id_MovimentacaoTipo.equalsIgnoreCase("")){
				this.Id_MovimentacaoTipo = Id_MovimentacaoTipo;
			}
		}		
	}

	public String getId_MovimentacaoTipo() {
		return Id_MovimentacaoTipo;
	}

	public void setMovimentacaoTipo(String MovimentacaoTipo) {
		if (MovimentacaoTipo!=null) this.MovimentacaoTipo = MovimentacaoTipo;
	}

	public String getMovimentacaoTipo() {
		return MovimentacaoTipo;
	}
	
	public String getMovimentacaoTipoCodigo() {
		return MovimentacaoTipoCodigo;
	}

	public void setMovimentacaoTipoCodigo(String movimentacaoTipoCodigo) {
		MovimentacaoTipoCodigo = movimentacaoTipoCodigo;
	}

	public void limpar(){
		super.limpar();
		Id_UsuarioMovimentacaoTipo="";
		Id_MovimentacaoTipo="";
		MovimentacaoTipo="";
		MovimentacaoTipoCodigo="";
	}
	
	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_MovimentacaoTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getMovimentacaoTipo() + "</strong>  </li>  ";
		return stTemp;
	}

	public String getPropriedades(String id_Usuario, String GrupoCodigo) {
		return "[Id_UsuarioMovimentacaoTipo:" + Id_UsuarioMovimentacaoTipo + ";id_Usuario:" + id_Usuario + ";GrupoCodigo:" + GrupoCodigo + ";Id_MovimentacaoTipo:" + Id_MovimentacaoTipo + ";MovimentacaoTipo:" + MovimentacaoTipo + "]";
	}

}
