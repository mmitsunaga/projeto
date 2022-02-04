package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.dt.SinalDt;
import br.gov.go.tj.projudi.ps.ProcessoParteSinalPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoParteSinalNe extends ProcessoParteSinalNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -8947699879500042558L;

    //---------------------------------------------------------
	public  String Verificar(ProcessoParteSinalDt dados ) {

		String stRetorno="";

		if (dados.getNome().length()==0)
			stRetorno += "O Campo Nome é obrigatório.";
		if (dados.getSinal().length()==0)
			stRetorno += "O Campo Sinal é obrigatório.";
		return stRetorno;

	}

	public void salvar(ProcessoParteSinalDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;

		ProcessoParteSinalPs obPersistencia = new  ProcessoParteSinalPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("" ) ) {		
			SinalDt sinal = new SinalDt();
			sinal.setSinal(dados.getSinal());
			sinal.setId_UsuarioLog(dados.getId_UsuarioLog());
			sinal.setIpComputadorLog(dados.getIpComputadorLog());
			new SinalNe().salvar(sinal);
			dados.setId_Sinal(sinal.getId());
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoParteSinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {	
			SinalDt sinal = new SinalNe().consultarId(dados.getId_Sinal());
			if (!sinal.getSinal().equalsIgnoreCase(dados.getSinal())){
				sinal = new SinalDt();
				sinal.setSinal(dados.getSinal());
				sinal.setId_UsuarioLog(dados.getId_UsuarioLog());
				sinal.setIpComputadorLog(dados.getIpComputadorLog());
				new SinalNe().salvar(sinal);
				dados.setId_Sinal(sinal.getId());
			}
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoParteSinal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	
	}
	
	public List listarSinal(String idProcessoParte) throws Exception {
		List retorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteSinalPs obPersistencia = new  ProcessoParteSinalPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.listarSinal(idProcessoParte); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno ;
	}
	
}
