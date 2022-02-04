package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class VotanteTipoDt extends Dados {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3236165623593590853L;
	
	public static final int RELATOR = 1;
	public static final int VOTANTE = 2;
	public static final int MINISTERIO_PUBLICO = 3;
	public static final int PRESIDENTE_CAMARA = 4;
	public static final int INTEGRANTE_CAMARA = 5;
	public static final int PRESIDENTE_SESSAO = 6;
	
	private String Id_VotanteTipo;
	private String VotanteTipoCodigo;
	private String VotanteTipo;

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		Id_VotanteTipo = id;		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return Id_VotanteTipo;
	}

	public String getVotanteTipoCodigo() {
		return VotanteTipoCodigo;
	}
	
	public int getVotanteTipoCodigoInt() {
		return Funcoes.StringToInt(VotanteTipoCodigo);
	}

	public void setVotanteTipoCodigo(String votanteTipoCodigo) {
		VotanteTipoCodigo = votanteTipoCodigo;
	}

	public String getVotanteTipo() {
		return VotanteTipo;
	}

	public void setVotanteTipo(String votanteTipo) {
		VotanteTipo = votanteTipo;
	}
	
	public void copiar(VotanteTipoDt objeto){
		Id_VotanteTipo = objeto.getId();
		VotanteTipo = objeto.getVotanteTipo();
		VotanteTipoCodigo = objeto.getVotanteTipoCodigo();
	}

	public void limpar(){
		Id_VotanteTipo="";
		VotanteTipo="";
		VotanteTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_VotanteTipo:" + Id_VotanteTipo + ";VotanteTipo:" + VotanteTipo + ";VotanteTipoCodigo:" + VotanteTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}

}
