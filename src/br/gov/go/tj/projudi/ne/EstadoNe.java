package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.ps.EstadoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class EstadoNe extends EstadoNeGen {

    private static final long serialVersionUID = 5431607171202963052L;

    public String Verificar(EstadoDt dados) {
		String stRetorno = "";

		if (dados.getEstado().equalsIgnoreCase("")) {
			stRetorno += "Descrição é é obrigatório.";
		}
		if (dados.getEstadoCodigo().equalsIgnoreCase("")) {
			stRetorno += "Código é é obrigatório.";
		}

		if (dados.getId_Pais().equalsIgnoreCase("")) {
			stRetorno += "É necessário selecionar um País.";
		}
		return stRetorno;
	}

	/**
	 * Método responsável em consultar uma estado, baseado no Uf passado
	 */
	public EstadoDt buscaEstado(String ufEstado) throws Exception {
		EstadoDt estadoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstadoPs obPersistencia = new EstadoPs(obFabricaConexao.getConexao());

			estadoDt = obPersistencia.buscaEstado(ufEstado);
			
		
		} finally{
			
			obFabricaConexao.fecharConexao();
		}
		return estadoDt;
	}

	public String consultarDescricaoJSON(String descricao,  String posicao) throws Exception {
        String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstadoPs obPersistencia = new EstadoPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricao2JSON(descricao,  posicao);
                        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
	
	public String consultarDescricaoUfJSON(String descricao,  String posicao) throws Exception {
        String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstadoPs obPersistencia = new EstadoPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoUfJSON(descricao,  posicao);
                        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
	
	public String consultarDescricaoUfJSON (String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            EstadoPs obPersistencia = new EstadoPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoUfJSON(descricao, posicao, ordenacao, quantidadeRegistros);
                        
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
	}
	
	
	 /**
     * Consulta Países (utilizando filtro por descricao do pais)
     */
    public String consultarDescricaoPaisJSON(String descricao, String posicao) throws Exception {
        String stTemp ="";               

        stTemp = (new PaisNe()).consultarDescricaoJSON(descricao,  posicao);                       
         
        return stTemp;
    }
    
    public List consultarIdPais(String idPais) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstadoPs obPersistencia = new EstadoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarIdPais(idPais);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
    
    public EstadoDt consultarEstadoCodigo(String codigoEstado ) throws Exception {

		EstadoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstadoPs obPersistencia = new EstadoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarEstadoCodigo(codigoEstado); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
    
    public EstadoDt consultarId(String id_estado, FabricaConexao obFabricaConexao) throws Exception {
		EstadoDt dtRetorno=null;
		EstadoPs obPersistencia = new EstadoPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_estado ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}

}