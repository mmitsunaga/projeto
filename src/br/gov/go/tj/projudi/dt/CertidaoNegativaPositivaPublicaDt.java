package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class CertidaoNegativaPositivaPublicaDt extends CertidaoNegativaPositivaDt{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2289224591515387465L;
		
	public CertidaoNegativaPositivaPublicaDt() {
		this.limpar();
	}
	
	private String nome;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome != null)
			this.nome = nome;
	}
	
	private String cpfCnpj;
	
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	
	public void setCpfCnpj(String cpfCnpj) {
		if (cpfCnpj != null)
			this.cpfCnpj = cpfCnpj;
	}
	
	private String nomeMae;
	
	public String getNomeMae() {
		return nomeMae;
	}
	
	public void setNomeMae(String nomeMae) {
		if (nomeMae != null)
			this.nomeMae = nomeMae;
	}
	
	private String dataNascimento;
	
	public String getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(String dataNascimento) {
		if (dataNascimento != null)
			this.dataNascimento = dataNascimento;
	}
	
	private String areaCodigo = "";	
	
	public void setAreaCodigo(String areaCodigo) {
		if (areaCodigo != null)
			this.areaCodigo = areaCodigo;
	}
	
	public String getAreaCodigo() {
		return this.areaCodigo;
	}
	
	private boolean ehPessoaJuridica;	
	
	public void setEhPessoaJuridica(boolean ehPessoaJuridica) {
		this.ehPessoaJuridica = ehPessoaJuridica;			
	}
	
	public boolean isEhPessoaJuridica() {
		return this.ehPessoaJuridica;
	}
	
	private String texto;
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		if (texto != null)
		this.texto = texto;
	}
	
	private List<String> listaNomesComarcasComProcessos;
	
	public List<String> getListaNomesComarcasComProcessos() {
		return listaNomesComarcasComProcessos;
	}

	public void setListaNomesComarcasComProcessos(List<String> listaNomesComarcasComProcessos) {
		if (listaNomesComarcasComProcessos != null)
			this.listaNomesComarcasComProcessos = listaNomesComarcasComProcessos;
	}
	
	public void addListaNomesComarcasComProcessos(List<String> listaNomesComarcasComProcessos) {
		if (listaNomesComarcasComProcessos != null)
		{
			if (this.listaNomesComarcasComProcessos == null) {
				this.listaNomesComarcasComProcessos = listaNomesComarcasComProcessos;			
			} else {		
				for (String nomeComarca : listaNomesComarcasComProcessos) {
					if (!this.listaNomesComarcasComProcessos.contains(nomeComarca))
						this.listaNomesComarcasComProcessos.add(nomeComarca);
				}
			}
		}		
	}	
	
	private String InteressePessoal = "";
	
	public String getInteressePessoal() {
		return InteressePessoal;
	}

	public void setInteressePessoal(String interessePessoal) {
		if (interessePessoal != null)
			this.InteressePessoal = interessePessoal;
	}
	
	private String NumeroGuiaCertidao;
	
	public String getNumeroGuiaCertidao() {
		return NumeroGuiaCertidao;
	}

	public void setNumeroGuiaCertidao(String numeroGuiaCertidao) {
		if (numeroGuiaCertidao != null)
			this.NumeroGuiaCertidao = numeroGuiaCertidao;
	}
	
	private String Territorio = "";
	
	public String getTerritorio() {
		return Territorio;
	}

	public void setTerritorio(String territorio) {
		if (territorio != null)
			this.Territorio = territorio;
	}
	
	private String Id_Comarca;
	
	public String getId_Comarca() {
		return Id_Comarca;
	}

	public void setId_Comarca(String id_Comarca) {
		if (id_Comarca != null)
			Id_Comarca = id_Comarca;
	}
	
	private String Comarca;

	public String getComarca() {
		return Comarca;
	}

	public void setComarca(String comarca) {
		if (comarca != null)
			Comarca = comarca;
	}
	
	private String ComarcaCodigo;
	
	public String getComarcaCodigo() {
		return ComarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		if (comarcaCodigo != null)
			ComarcaCodigo = comarcaCodigo;
	}
	
	private String Finalidade = "";
	
	public String getFinalidade() {
		return Finalidade;
	}

	public void setFinalidade(String finalidade) {
		if (finalidade != null)
			this.Finalidade = finalidade;
	}
	
	public void limparParcial() {	
		this.texto = "";
		this.listaNomesComarcasComProcessos = new ArrayList<String>();	
		this.setEhPessoaJuridica(false);
	}
	
	public void limpar() {
		this.limparParcial();
		this.nome = "";
		this.cpfCnpj = "";		
		this.dataNascimento = "";
		this.nomeMae = "";
		this.setEhPessoaJuridica(false);
		this.NumeroGuiaCertidao = "";
		this.Id_Comarca = "";
		this.Comarca = "";
		this.ComarcaCodigo = "";
		this.certidaoEmitida = null;
		this.documento = null;
		this.nomeDocumento = null;
		this.guiaEmissaoCertidao = null;
	}
	
	private CertidaoValidacaoDt certidaoEmitida;
	
	public CertidaoValidacaoDt getCertidaoEmitida() {
		return certidaoEmitida;
	}

	public void setCertidaoEmitida(CertidaoValidacaoDt certidaoEmitida) {
		this.certidaoEmitida = certidaoEmitida;
	}
	
	private byte[] documento;
	
	public byte[] getDocumento() {
		return documento;
	}

	public void setDocumento(byte[] documento) {
		if (documento != null && documento.length > 0)
			this.documento = documento;
	}
	
	public void limparDocumento() {
		this.documento = null;
	}
	
	private String nomeDocumento;
	
	public String getNomeDocumento() {
		return nomeDocumento;
	}

	public void setNomeDocumento(String nomeDocumento) {
		if (nomeDocumento != null)
			this.nomeDocumento = nomeDocumento;
	}
	
	private GuiaEmissaoDt guiaEmissaoCertidao;
	
	public GuiaEmissaoDt getGuiaEmissaoCertidao() {
		return guiaEmissaoCertidao;
	}

	public void setGuiaEmissaoCertidao(GuiaEmissaoDt guiaEmissaoCertidao) {
		this.guiaEmissaoCertidao = guiaEmissaoCertidao;
	}

	public boolean isCivel() {
		if (getAreaCodigo()!=null && getAreaCodigo().equals(AreaDt.CIVEL))
			return true;
		return false;
	}
	
	public boolean isCriminal() {
		if (getAreaCodigo()!=null && getAreaCodigo().equals(AreaDt.CRIMINAL))
			return true;
		return false;
	}
}