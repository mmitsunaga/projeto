package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CustaValorDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.ps.AreaDistribuicaoPs;
import br.gov.go.tj.projudi.ps.CustaValorPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class CustaValorNe extends CustaValorNeGen{


/**
     * 
     */
    private static final long serialVersionUID = -8203252570199073161L;

    //---------------------------------------------------------
	public  String Verificar(CustaValorDt dados ) {

		String stRetorno="";

//		if (dados.getCustaValor().length()==0)
//			stRetorno += "O Campo Descri��o � obrigat�rio.";
//		if (dados.getCustaValorCodigo().length()==0)
//			stRetorno += "O Campo C�digo do Item � obrigat�rio.";
		if (dados.getId_Custa().length()==0)
			stRetorno += "O Campo Custa � obrigat�rio.";
		if (dados.getLimiteMin().length()==0)
			stRetorno += "O Campo Limite M�nimo � obrigat�rio.";
		if (dados.getLimiteMax().length()==0)
			stRetorno += "O Campo Limite M�ximo � obrigat�rio.";
		if (dados.getValorCusta().length()==0)
			stRetorno += "O Campo Valor do Item � obrigat�rio.";
		if (dados.getCusta().length()==0)
			stRetorno += "O Campo Custa � obrigat�rio.";
		return stRetorno;
	}
	
	/**
	 * Consultar Por Descri��o
	 * @param String descricao
	 * @param String idCusta
	 * @param String posicao
	 * @return List
	 * @throws Exception
	 */
	public List consultarPorDescricao(String descricao, String idCusta, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				CustaValorPs obPersistencia = new CustaValorPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarPorDescricao( descricao, idCusta, posicao);
				
				for(int i = 0; i < (tempList.size()-1); i++) {
					CustaValorDt custaValorDt = (CustaValorDt)tempList.get(i);
					
					custaValorDt.setValorCusta( Funcoes.FormatarDecimal(custaValorDt.getValorCusta()) );
					custaValorDt.setLimiteMin( Funcoes.FormatarDecimal(custaValorDt.getLimiteMin()) );
					custaValorDt.setLimiteMax( Funcoes.FormatarDecimal(custaValorDt.getLimiteMax()) );
				}
				
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}
	
	public String consultarPorDescricaoJSON(String descricao, String idCusta, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CustaValorPs obPersistencia = new CustaValorPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarPorDescricaoJSON( descricao, idCusta, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * M�todo Gen�rico para consultar a lista de valores a serem cobrados de acordo com o intervalo do valor de refer�ncia que pode ser <<VALOR DA CAUSA>> ou <<VALOR DOS BENS>> ou <<OUTROS>>.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List consultaValorIntevaloCusta(List listaCustaDt, String valorReferenciaCalculo) throws Exception {
		List listaGuiaItemDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CustaValorPs obPersistencia = new CustaValorPs(obFabricaConexao.getConexao());
			listaGuiaItemDt = obPersistencia.consultaValorIntevaloCusta(listaCustaDt, valorReferenciaCalculo);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * M�todo de consulta do valor da custa.
	 * @param String idCusta
	 * @return GuiaItemDt
	 * @throws Exception
	 */
	public GuiaItemDt consultaValorCusta(String idCusta) throws Exception {
		GuiaItemDt guiaItemDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CustaValorPs obPersistencia = new CustaValorPs(obFabricaConexao.getConexao());
			guiaItemDt = obPersistencia.consultaValorCusta(idCusta);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return guiaItemDt;
	}
	
	/**
	 * M�todo para consultar valor espec�fico de um dado c�digo de regimento.
	 * @param String codigoRegimento
	 * @return String valor
	 * @throws Exception
	 */
	public String consultaValorEspecificoCodigoRegimento(String codigoRegimento) throws Exception {
		String valor = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CustaValorPs obPersistencia = new CustaValorPs(obFabricaConexao.getConexao());
			valor = obPersistencia.consultaValorEspecificoCodigoRegimento(codigoRegimento);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return valor;
	}
	

	
	public String consultarDescricaoCustaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		CustaNe Custane = new CustaNe();  
		stTemp = Custane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
}