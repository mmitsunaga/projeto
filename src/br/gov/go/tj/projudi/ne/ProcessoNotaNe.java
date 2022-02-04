package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ProcessoNotaDt;
import br.gov.go.tj.projudi.ps.ProcessoNotaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoNotaNe extends ProcessoNotaNeGen{

	private static final long serialVersionUID = 3645117640729750463L;

	//---------------------------------------------------------
	public  String Verificar(ProcessoNotaDt dados ) {

		String stRetorno="";

		if (dados.getNome().length()==0)
			stRetorno += "O Campo Nome é obrigatório.";

		return stRetorno;

	}
	
	public String consultarNotasJSON(String id_proc, String id_usuarioServentia, String id_Serventia ) throws Exception {
		
		String stTemp ="";
		FabricaConexao obFabricaConexao = null; 
	        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ProcessoNotaPs obPersistencia = new ProcessoNotaPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarNotasJSON(id_proc, id_usuarioServentia, id_Serventia);                        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
						
	}

	public void excluirNota(String id_nota, String id_UsuarioServentia, String isPrivada, String id_Serventia) throws Exception {
		FabricaConexao obFabricaConexao = null; 
		try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ProcessoNotaPs obPersistencia = new ProcessoNotaPs(obFabricaConexao.getConexao());
            obPersistencia.excluirNota( id_nota,  id_UsuarioServentia, isPrivada, id_Serventia);                        
        } finally{
            obFabricaConexao.fecharConexao();
        }
		
	}

}
