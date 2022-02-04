package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt;
import br.gov.go.tj.projudi.ps.ServentiaAreaDistribuicaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ServentiaAreaDistribuicaoNe extends ServentiaAreaDistribuicaoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 8702313010226015566L;

    //---------------------------------------------------------
	public  String Verificar(ServentiaAreaDistribuicaoDt dados ) {

		String stRetorno="";

		if (dados.getServAreaDist().length()==0)
			stRetorno += "O Campo ServAreaDist é obrigatório.";
		if (dados.getServ().length()==0)
			stRetorno += "O Campo Serv é obrigatório.";
		if (dados.getAreaDist().length()==0)
			stRetorno += "O Campo AreaDist é obrigatório.";
		//System.out.println("..neServAreaDistVerificar()");
		return stRetorno;

	}

    public void salvar(ServentiaAreaDistribuicaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
        LogDt obLogDt;
        
        ServentiaAreaDistribuicaoPs obPersistencia = new ServentiaAreaDistribuicaoPs(obFabricaConexao.getConexao());
        if (dados.getId().equalsIgnoreCase("")) {               
            obPersistencia.inserir(dados);
            obLogDt = new LogDt("ServAreaDist", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
        } else {               
            obPersistencia.alterar(dados);
            obLogDt = new LogDt("ServAreaDist", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
        }

        obDados.copiar(dados);
        obLog.salvar(obLogDt, obFabricaConexao);
        
    }
    
	
}
