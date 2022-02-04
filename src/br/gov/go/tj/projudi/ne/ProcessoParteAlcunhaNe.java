package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.AlcunhaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.ps.ProcessoParteAlcunhaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoParteAlcunhaNe extends ProcessoParteAlcunhaNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 2101371534331258091L;

    //---------------------------------------------------------
	public  String Verificar(ProcessoParteAlcunhaDt dados ) {

		String stRetorno="";

		if (dados.getNome().length()==0)
			stRetorno += "O Campo Nome é obrigatório.";
		if (dados.getAlcunha().length()==0)
			stRetorno += "O Campo Alcunha é obrigatório.";
		////System.out.println("..neProcessoParteAlcunhaVerificar()");
		return stRetorno;

	}

	public void salvar(ProcessoParteAlcunhaDt dados, FabricaConexao obFabricaConexao ) throws Exception {
		LogDt obLogDt;

		ProcessoParteAlcunhaPs obPersistencia = new ProcessoParteAlcunhaPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("" ) ) {
			AlcunhaDt alcunha = new AlcunhaDt();
			alcunha.setAlcunha(dados.getAlcunha());
			alcunha.setId_UsuarioLog(dados.getId_UsuarioLog());
			alcunha.setIpComputadorLog(dados.getIpComputadorLog());
			new AlcunhaNe().salvar(alcunha);
			dados.setId_Alcunha(alcunha.getId());
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoParteAlcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {			
			AlcunhaDt alcunha = new AlcunhaNe().consultarId(dados.getId_Alcunha());
			if (!alcunha.getAlcunha().equalsIgnoreCase(dados.getAlcunha())){
				alcunha = new AlcunhaDt();
				alcunha.setAlcunha(dados.getAlcunha());
				alcunha.setId_UsuarioLog(dados.getId_UsuarioLog());
				alcunha.setIpComputadorLog(dados.getIpComputadorLog());
				new AlcunhaNe().salvar(alcunha);
				dados.setId_Alcunha(alcunha.getId());
			}
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoParteAlcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	
	}
	
	public List listarAlcunha(String idProcessoParte) throws Exception {
		List retorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAlcunhaPs obPersistencia = new ProcessoParteAlcunhaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.listarAlcunha(idProcessoParte); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno ;
	}
	
}
