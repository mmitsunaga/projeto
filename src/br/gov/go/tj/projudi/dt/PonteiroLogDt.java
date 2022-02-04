package br.gov.go.tj.projudi.dt;

import java.util.Date;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class PonteiroLogDt extends PonteiroLogDtGen{
	
	public static final int CodigoPermissao=891;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2250213034344197391L;

	
	public PonteiroLogDt(String id_areaDistribuicao, int id_ponteiroLogTipo, String id_processo, String id_Serventia,  String id_usuario, String id_usuario_serventia, Date data, String id_ServentiaCargo) {
		
		this.setId_AreaDistribuicao(id_areaDistribuicao);
		this.setId_PonteiroLogTipo(String.valueOf(id_ponteiroLogTipo));
		this.setId_Proc(id_processo);
		this.setId_Serventia(id_Serventia);
		this.setId_UsuarioLog(id_usuario);
		this.setId_UsuarioServentia(id_usuario_serventia);
		this.setData(Funcoes.DataHora(data));			
		this.setId_ServentiaCargo(id_ServentiaCargo);
		// esse método será utilizado somente para a distribuição e redistribuição
		// logo a  quatidade é respectivamente 1 e -1
		if (id_ponteiroLogTipo==PonteiroLogTipoDt.DESAPENSAR || id_ponteiroLogTipo==PonteiroLogTipoDt.DISTRIBUICAO || id_ponteiroLogTipo==PonteiroLogTipoDt.GANHO_RESPONSABILIDADE){
			this.setQtd("1");
		}else{
			this.setQtd("-1");
		}
								
	}

	public PonteiroLogDt(String id_areaDistribuicao, int id_ponteiroLogTipo, String id_processo, String id_Serventia,  String id_usuario, String id_usuario_serventia, Date data, String justificativa, int qtd_distribuicao, String id_ServentiaCargo) {
		
		this.setId_AreaDistribuicao(id_areaDistribuicao);
		this.setId_PonteiroLogTipo(String.valueOf(id_ponteiroLogTipo));
		this.setId_Proc(id_processo);
		this.setId_Serventia(id_Serventia);
		this.setId_UsuarioLog(id_usuario);
		this.setId_UsuarioServentia(id_usuario_serventia);
		this.setData(Funcoes.DataHora(data));
		this.setJustificativa(justificativa);
		this.setQtd(String.valueOf(qtd_distribuicao));
		this.setId_ServentiaCargo(id_ServentiaCargo);		
		
	}

	public PonteiroLogDt() {
	}

	public PonteiroLogDt(PonteiroLogCompensarDt plcDt) {
		setId_AreaDistribuicao(plcDt.getId_AreaDistribuicao_D());
		setId_Serventia(plcDt.getId_Serventia_D());
		setId_ServentiaCargo(plcDt.getId_ServentiaCargo_D());
		setQtd(plcDt.getQtd());
		setJustificativa(plcDt.getJustificativa());		
	}

	public void setQtd(int valor) {
		Qtd = String.valueOf(valor);		
	}

}
