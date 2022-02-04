package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.ps.EstadoCivilPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

public class EstadoCivilNe extends EstadoCivilNeGen {
	private static final long serialVersionUID = 7111966532357783955L;

	public String Verificar(EstadoCivilDt dados) {
		String stRetorno = "";
		if (dados.getEstadoCivil().trim().equalsIgnoreCase("")) {
			return stRetorno += "O campo 'Estado Civil' é obrigatório!";
		}
		return stRetorno;
	}

	public String consultarId(String descricao, FabricaConexao obFabricaConexao) throws Exception {
		String id = "";
		
		EstadoCivilPs obPersistencia = new  EstadoCivilPs(obFabricaConexao.getConexao());
		id = obPersistencia.consultarIdEstadoCivil(descricao);
		
		return id;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);  
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp ="";
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstadoCivilPs obPersistencia = new EstadoCivilPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
                        
        } finally {
            obFabricaConexao.fecharConexao();
        }
        
        return stTemp;
	}
}
