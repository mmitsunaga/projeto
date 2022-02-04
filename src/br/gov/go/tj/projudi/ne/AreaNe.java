package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.ps.AreaPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

public class AreaNe extends AreaNeGen{

    private static final long serialVersionUID = 5523513940489160777L;

	public  String Verificar(AreaDt dados ) {
		String stRetorno="";
		if (dados.getArea().equalsIgnoreCase("")){
			stRetorno += "Área é é obrigatório.";
		}
		if (dados.getAreaCodigo().equalsIgnoreCase("")){
			stRetorno += "Código é é obrigatório.";
		}
		return stRetorno;
	}
	
	/**
	 * Método que obtém o objeto Area corresponde ao código passado
	 */
	public AreaDt consultarAreaCodigo(int areaCodigo) throws Exception {
		AreaDt areaDt = new AreaDt();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaPs obPersistencia = new  AreaPs(obFabricaConexao.getConexao());
			areaDt = obPersistencia.consultarAreaCodigo(areaCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return areaDt;
	}

	/**
	 * Método para lista as area processuais
	 * @author jrcorrea
	 */
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros ) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		String stTemp = "";
		
		try{

			AreaPs obPersistencia = new AreaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
}