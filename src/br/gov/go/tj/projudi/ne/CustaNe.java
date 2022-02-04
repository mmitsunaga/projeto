package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.ps.CustaPs;
import br.gov.go.tj.projudi.ps.ForumPs;
import br.gov.go.tj.utils.FabricaConexao;

public class CustaNe extends CustaNeGen{

/**
     * 
     */
    private static final long serialVersionUID = 3295142321588637174L;

	public  String Verificar(CustaDt dados ) {

		String stRetorno="";

		if (dados.getCusta().length()==0)
			stRetorno += "O Campo Custa � obrigat�rio.";
		if (dados.getCodigoRegimento().length()==0)
			stRetorno += "O Campo CodigoRegimento � obrigat�rio.";
		if (dados.getCodigoRegimentoValor().length()==0)
			stRetorno += "O Campo CodigoRegimentoValor � obrigat�rio.";
		if (dados.getPorcentagem().length()==0)
			stRetorno += "O Campo Porcentagem � obrigat�rio.";
		return stRetorno;

	}
	
	public CustaDt consultarId(String id_custa ) throws Exception {

		CustaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_custa); 
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * M�todo para consultar Custa e Arrecada��o.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @param String codigoRegimento
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public CustaDt consultarItemGuiaPorCodigoRegimento(String codigoRegimento) throws Exception {
		CustaDt custaDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			
			custaDt = obPersistencia.consultarItemGuiaPorCodigoRegimento(codigoRegimento);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return custaDt;
	}
	
	/**
	 * M�todo para consultar Custa pelo c�digo da arrecada��o.
	 * @param codigoArrecadacao
	 * @return
	 * @throws Exception
	 */
	public CustaDt consultarCodigoArrecadacao(String codigoArrecadacao) throws Exception {

		CustaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarCodigoArrecadacao(codigoArrecadacao);
		}
		finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * M�todo para consultar Custa pelo c�digo da arrecada��o.
	 * @param codigoArrecadacao
	 * @return
	 * @throws Exception
	 */
	public CustaDt consultarCodigoArrecadacao(String codigoArrecadacao, FabricaConexao obFabricaConexao) throws Exception {

		CustaDt dtRetorno = null;
		CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarCodigoArrecadacao(codigoArrecadacao);
		return dtRetorno;
	}
	
	/**
	 * M�todo para consultar itens de Custa para o C�lculo.
	 * @param List listaCustaDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricao(List listaCustaDt) throws Exception {
		
		List listaGuiaCustaModelo = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			
			listaGuiaCustaModelo = obPersistencia.consultarDescricao(listaCustaDt);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaCustaModelo;
	}
	
    public String consultarArrecadacaoCustaJSON(String descricao, String posicao) throws Exception {
        String stTemp ="";
        ArrecadacaoCustaNe arrecadacaoCustaNe = new ArrecadacaoCustaNe();
                                
        stTemp = arrecadacaoCustaNe.consultarPorDescricaoJSON(descricao,  posicao);                       
        
        return stTemp;
    }
    
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}
