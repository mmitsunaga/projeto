package br.gov.go.tj.projudi.dt;

public class ProcessoNotaDtGen extends Dados{

	private static final long serialVersionUID = -6434430362349104368L;
	protected String Id_ProcessoNota;
	protected String Nome;

	protected String Id_UsuarioServentia;

	protected String Id_Processo;
	protected String ProcNumero;
	protected String ProcessoNota;
	protected String DataCriacao;
	protected String CodigoTemp;

	public ProcessoNotaDtGen() {
		limpar();
	}

	public String getId()  {return Id_ProcessoNota;}
	public void setId(String valor ) { if(valor!=null) Id_ProcessoNota = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) { if (valor!=null) Nome = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuarioServentia = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Processo = ""; ProcNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcNumero()  {return ProcNumero;}
	public void setProcNumero(String valor ) { if (valor!=null) ProcNumero = valor;}
	public String getProcessoNota()  {return ProcessoNota;}
	public void setProcessoNota(String valor ) { if (valor!=null) ProcessoNota = valor;}
	public String getDataCriacao()  {return DataCriacao;}
	public void setDataCriacao(String valor ) { if (valor!=null) DataCriacao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoNotaDt objeto){
		 if (objeto==null) return;
		Id_ProcessoNota = objeto.getId();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		Nome = objeto.getNome();
		Id_Processo = objeto.getId_Processo();
		ProcNumero = objeto.getProcNumero();
		ProcessoNota = objeto.getProcessoNota();
		DataCriacao = objeto.getDataCriacao();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoNota="";
		Id_UsuarioServentia="";
		Nome="";
		Id_Processo="";
		ProcNumero="";
		ProcessoNota="";
		DataCriacao="";
		CodigoTemp="";
	}

} 
