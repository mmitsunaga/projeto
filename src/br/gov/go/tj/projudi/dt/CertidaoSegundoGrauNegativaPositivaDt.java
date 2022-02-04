package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class CertidaoSegundoGrauNegativaPositivaDt extends Dados {


	/**
	 * 
	 */
	
	public static final int CodigoPermissao = 447;
	private static final long serialVersionUID = -4832212926214636144L;
	public static final int NEGATIVA_CRIMINAL_FISICA_SEGUNDO_GRAU_MODELO_CODIGO = 131;
	public static final int POSITIVA_CRIMINAL_FISICA_SEGUNDO_GRAU_MODELO_CODIGO = 132;
	public static final int NEGATIVA_CRIMINAL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO = 133;
	public static final int POSITIVA_CRIMINAL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO = 134;
	public static final int NEGATIVA_CIVEL_FISICA_MODELO_SEGUNDO_GRAU_CODIGO = 135;
	public static final int POSITIVA_CIVEL_FISICA_MODELO_SEGUNDO_GRAU_CODIGO = 136;
	public static final int NEGATIVA_CIVEL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO = 137;
	public static final int POSITIVA_CIVEL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO = 143;
	public static final int POSITIVA_FISICA_SEGUNDO_GRAU_MODELO_CODIGO = 144;
	public static final int NEGATIVA_FISICA_SEGUNDO_GRAU_MODELO_CODIGO = 145;
	public static final int POSITIVA_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO = 146;
	public static final int NEGATIVA_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO = 147;
	
	
	private String nome;
	private String nomeMae;
	private String dataNascimento;
	private String area;
	private String dataInicial;
	private String dataFinal;
	private String feito;
	private String cpfCnpj;
	private List listaProcesso;
	private String pessoaTipo;
	
	
	private String texto;
	

	
	public CertidaoSegundoGrauNegativaPositivaDt(String nome, String nomeMae,
			String dataNascimento, String area, String dataInicial,
			String dataFinal, String feito, String cpfCnpj, List listaProcesso,
			String pessoaTipo, String texto) {
		super();
		this.nome = nome;
		this.nomeMae = nomeMae;
		this.dataNascimento = dataNascimento;
		this.area = area;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.feito = feito;
		this.cpfCnpj = cpfCnpj;
		this.listaProcesso = listaProcesso;
		this.pessoaTipo = pessoaTipo;
		this.texto = texto;
	}
	
	public CertidaoSegundoGrauNegativaPositivaDt() {
		super();
		this.nome = "";
		this.nomeMae ="";
		this.dataNascimento = "";
		this.area = "";
		this.dataInicial = "";
		this.dataFinal = "";
		this.feito = "";
		this.cpfCnpj = "";
		this.listaProcesso = new ArrayList();
		this.pessoaTipo = "";
		this.texto = "";
	}


	public List getListaProcesso() {
		return listaProcesso;
	}


	public String getPessoaTipo() {
		return pessoaTipo;
	}


	public void setPessoaTipo(String pessoaTipo) {
		if(pessoaTipo != null)
		this.pessoaTipo = pessoaTipo;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public void setListaProcesso(List listaProcesso) {
		this.listaProcesso = listaProcesso;
	}


	public String getCpfCnpj() {
		return cpfCnpj;
	}


	public void setCpfCnpj(String cpfCnpj) {
		if(cpfCnpj != null)
		this.cpfCnpj = cpfCnpj;
	}


	public void setId(String id) {
		
	}

	
	public String getId() {

		return null;
	}


	public void setNome(String parameter) {
		if (parameter != null)
		this.nome = parameter;
	}


	public String getNomeMae() {
		return nomeMae;
	}


	public void setNomeMae(String nomeMae) {
		if(nomeMae != null)
		this.nomeMae = nomeMae;
	}


	public String getDataNascimento() {
		return dataNascimento;
	}


	public void setDataNascimento(String dataNascimento) {
		if(dataNascimento != null)
		this.dataNascimento = dataNascimento;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		if(area != null)
		this.area = area;
	}


	public String getDataInicial() {
		return dataInicial;
	}


	public void setDataInicial(String dataInicial) {
		if(dataInicial != null)
		this.dataInicial = dataInicial;
	}


	public String getDataFinal() {
		return dataFinal;
	}


	public void setDataFinal(String dataFinal) {
		if( dataFinal != null)
		this.dataFinal = dataFinal;
	}


	public String getFeito() {
		return feito;
	}

	public void setFeito(String feito) {
		if (feito != null)
			this.feito = feito;
	}


	public String getNome() {
		return nome;
	}
	
	


	public void removeProcesso(int posicao) {
		this.listaProcesso.remove(posicao);
	}
	
	
	public int getModeloCodigo() {

		if (this.area.matches("[cC][iíIÍ][vV][eE][lL]")) {
			if (this.getPessoaTipo().matches("[fF][iIíÍ][sS][iI][cC][aA]|[Ff]")) {
				return this.getListaProcesso().size() > 0 ? CertidaoSegundoGrauNegativaPositivaDt.POSITIVA_CIVEL_FISICA_MODELO_SEGUNDO_GRAU_CODIGO
						: CertidaoSegundoGrauNegativaPositivaDt.NEGATIVA_CIVEL_FISICA_MODELO_SEGUNDO_GRAU_CODIGO;
			} else {
				return this.getListaProcesso().size() > 0 ? CertidaoSegundoGrauNegativaPositivaDt.POSITIVA_CIVEL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO
						: CertidaoSegundoGrauNegativaPositivaDt.NEGATIVA_CIVEL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO;
			}
		} else if (this.area.matches("[cC][rR][iíIÍ][mM][iI][nN][aA][lL]")) {
			if (this.getPessoaTipo().matches("[fF][iIíÍ][sS][iI][cC][aA]|[fF]")) {
				return this.getListaProcesso().size() > 0 ? CertidaoSegundoGrauNegativaPositivaDt.POSITIVA_CRIMINAL_FISICA_SEGUNDO_GRAU_MODELO_CODIGO
						: CertidaoSegundoGrauNegativaPositivaDt.NEGATIVA_CRIMINAL_FISICA_SEGUNDO_GRAU_MODELO_CODIGO;
			} else {
				return this.getListaProcesso().size() > 0 ? CertidaoSegundoGrauNegativaPositivaDt.POSITIVA_CRIMINAL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO
						: CertidaoSegundoGrauNegativaPositivaDt.NEGATIVA_CRIMINAL_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO;
			}
		} else {
			if (this.getPessoaTipo().matches("[fF][iIíÍ][sS][iI][cC][aA]|[fF]")) {
				return this.getListaProcesso().size() > 0 ? CertidaoSegundoGrauNegativaPositivaDt.POSITIVA_FISICA_SEGUNDO_GRAU_MODELO_CODIGO :
						CertidaoSegundoGrauNegativaPositivaDt.NEGATIVA_FISICA_SEGUNDO_GRAU_MODELO_CODIGO;
			} else {
				return this.getListaProcesso().size() > 0 ? CertidaoSegundoGrauNegativaPositivaDt.POSITIVA_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO
						: CertidaoSegundoGrauNegativaPositivaDt.NEGATIVA_JURIDICA_SEGUNDO_GRAU_MODELO_CODIGO;
			}
		}
	}
	
	public boolean isCivel(String areaCodigo) {
		if (areaCodigo != null && areaCodigo.equals(AreaDt.CIVEL))
			return true;
		return false;
	}
	
	public boolean isCriminal(String areaCodigo) {
		if (areaCodigo != null && areaCodigo.equals(AreaDt.CRIMINAL))
			return true;
		return false;
	}
}
