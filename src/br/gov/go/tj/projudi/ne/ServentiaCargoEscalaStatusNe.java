package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.ps.ServentiaCargoEscalaStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ServentiaCargoEscalaStatusNe extends ServentiaCargoEscalaStatusNeGen {

	private static final long serialVersionUID = 160521438561111142L;

	
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaStatusPs obPersistencia = new ServentiaCargoEscalaStatusPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarUsuarioServentiaEscalaStatus(descricao, posicao);
            //stUltimaConsulta = descricao;
            setQuantidadePaginas(tempList);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
	}

    @Override
    public String Verificar(ServentiaCargoEscalaStatusDt dados) {
    	String mensagemErro = "";
    	if (dados.getServentiaCargoEscalaStatus() == null || dados.getServentiaCargoEscalaStatus().equals("")){
    		mensagemErro = "Campo Status é obrigatório. ";
    	} 
    	return mensagemErro;
    }
    
    public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
    	String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaCargoEscalaStatusPs obPersistencia = new  ServentiaCargoEscalaStatusPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
    
    /**
	 * Consulta o ID do UsuarioServentiaEscalaStatus pelo código.
	 * @param statusCodigo - codigo do status
	 * @return ID do status
	 * @throws Exception
	 * @author hmgodinho
	 */
    public String consultarIdStatusPorCodigo(String statusCodigo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String idStatus = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaStatusPs obPersistencia = new ServentiaCargoEscalaStatusPs(obFabricaConexao.getConexao());
            idStatus = obPersistencia.consultarIdStatusPorCodigo(statusCodigo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return idStatus;
	}

}