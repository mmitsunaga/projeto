package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt;
import br.gov.go.tj.projudi.ps.ParametroCrimeExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ParametroCrimeExecucaoNe extends ParametroCrimeExecucaoNeGen{

    private static final long serialVersionUID = -1920932387359837173L;

	public  String Verificar(ParametroCrimeExecucaoDt dados ) {

		String stRetorno="";

		if (dados.getData().length()==0)
			stRetorno += "O Campo Data é obrigatório.";
		if (dados.getCrimeExecucao().length()==0)
			stRetorno += "O Campo CrimeExecucao é obrigatório.";
		return stRetorno;

	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroCrimeExecucaoPs obPersistencia = new ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			if (tempList != null){
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);	
			} else QuantidadePaginas = 0;
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	/**
	 * Lista os ParâmetrosCrimeExecução
	 * @param descricao: filtro de pesquisa (descrição do crime)
	 * @param lei: filtro de pesquisa 
	 * @param artigo: filtro de pesquisa
	 * @param posicao
	 * @return List
	 * @throws Exception
	 */
	public List listarParametroCrimeExecucao(String descricao, String lei, String artigo, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroCrimeExecucaoPs obPersistencia = new ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.listarParametroCrimeExecucao(descricao, lei, artigo, posicao);
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

				ParametroCrimeExecucaoPs obPersistencia = new  ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(crime, lei, artigo, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}

	public String consultarDescricaoCrimeExecucaoJSON(String crime, String lei, String artigo, String posicao ) throws Exception{
        String stTemp ="";               
                                
        stTemp = new CrimeExecucaoNe().consultarDescricaoJSON(crime, lei, artigo, posicao);                       
        
        return stTemp;
    }
}
