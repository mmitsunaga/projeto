package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.ps.CrimeExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class CrimeExecucaoNe extends CrimeExecucaoNeGen{

    private static final long serialVersionUID = 8215903290364910899L;

	public  String Verificar(CrimeExecucaoDt dados ) {

		String stRetorno="";

		if (dados.getCrimeExecucao().length()==0)
			stRetorno += "O Campo CrimeExecucao é obrigatório.";
		return stRetorno;

	}
	
    public CrimeExecucaoDt consultarCodigo(String codigo_crime ) throws Exception {
    	CrimeExecucaoDt dtRetorno=null;
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CrimeExecucaoPs obPersistencia = new CrimeExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarCodigo(codigo_crime ); 
			if (dtRetorno!=null) obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
    
    public List listarCrime(String descricao, String lei, String artigo, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CrimeExecucaoPs obPersistencia = new CrimeExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.listarCrime( descricao, lei, artigo, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

    
	public String consultarDescricaoJSON(String crime, String lei, String artigo, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				CrimeExecucaoPs obPersistencia = new CrimeExecucaoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(crime, lei, artigo, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
    
	public String consultarDescricaoJSONPJD(String crime, String posicao, String ordenacao, String quantidadeRegistros ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				CrimeExecucaoPs obPersistencia = new CrimeExecucaoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSONPJD(crime, posicao, ordenacao, quantidadeRegistros);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
}
