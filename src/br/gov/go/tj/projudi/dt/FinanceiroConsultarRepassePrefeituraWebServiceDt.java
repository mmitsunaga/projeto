package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import br.gov.go.tj.utils.TJDataHora;

public class FinanceiroConsultarRepassePrefeituraWebServiceDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7910622186684823742L;

	public static final int CodigoPermissao = 822;
	
	private TJDataHora dataMovimento;
	private RelatorioRepassePrefeituraWebServiceDt relatorio;
	private String Id_Usuario;
	private String IpComputadorLog;
		
	public FinanceiroConsultarRepassePrefeituraWebServiceDt(){
		limpar();
	}
	
	public void limpar(){
		dataMovimento = new TJDataHora();
		dataMovimento.adicioneDia(-10);
	}

	public TJDataHora getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(String dataRepasse) throws Exception{
		if (dataRepasse == null || dataRepasse.trim().equalsIgnoreCase("")) return;
		this.dataMovimento = new TJDataHora();
		this.dataMovimento.setDataddMMaaaa(dataRepasse);
		this.dataMovimento.atualizePrimeiraHoraDia();
	}
	
	public void setDataMovimento(TJDataHora dataMovimento){
		this.dataMovimento = dataMovimento;
		if (dataMovimento == null) return;
		this.dataMovimento.atualizePrimeiraHoraDia();
	}
	
	public boolean possuiRelatorio()
	{
		return this.relatorio != null;
	}
	
	public RelatorioRepassePrefeituraWebServiceDt getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(RelatorioRepassePrefeituraWebServiceDt relatorio) {
		this.relatorio = relatorio;
	}
	
	public void limpeRelatorio()
	{
		if (this.possuiRelatorio()) {
			relatorio.limpar();
			relatorio = null;
		}
	}

	public String getId_Usuario() {
		return Id_Usuario;
	}

	public void setId_Usuario(String id_Usuario) {
		Id_Usuario = id_Usuario;
	}

	public String getIpComputadorLog() {
		return IpComputadorLog;
	}

	public void setIpComputadorLog(String ipComputadorLog) {
		IpComputadorLog = ipComputadorLog;
	}
}
