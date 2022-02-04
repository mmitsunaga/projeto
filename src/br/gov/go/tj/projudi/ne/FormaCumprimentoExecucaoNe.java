package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.FormaCumprimentoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class FormaCumprimentoExecucaoNe extends Negocio{

	private static final long serialVersionUID = -3886050954312164363L;
	protected  FormaCumprimentoExecucaoDt obDados;


	public FormaCumprimentoExecucaoNe() {
		
		obLog = new LogNe(); 
		obDados = new FormaCumprimentoExecucaoDt(); 
	}

	public void salvar(FormaCumprimentoExecucaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			FormaCumprimentoExecucaoPs obPersistencia = new FormaCumprimentoExecucaoPs(obFabricaConexao.getConexao());
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("FormaCumprimentoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("FormaCumprimentoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public  String Verificar(FormaCumprimentoExecucaoDt dados ) {
		String stRetorno="";

		if (dados.getFormaCumprimentoExecucao().length()==0)
			stRetorno += "O Campo FormaCumprimentoExecucao é obrigatório.";
		if (dados.getFormaCumprimentoExecucaoCodigo().length()==0)
			stRetorno += "O Campo FormaCumprimentoExecucaoCodigo é obrigatório.";
		return stRetorno;

	}
		
	public void excluir(FormaCumprimentoExecucaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			FormaCumprimentoExecucaoPs obPersistencia = new FormaCumprimentoExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("FormaCumprimentoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


	public FormaCumprimentoExecucaoDt consultarId(String id_eventoexecucaostatus ) throws Exception {

		FormaCumprimentoExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			FormaCumprimentoExecucaoPs obPersistencia = new FormaCumprimentoExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_eventoexecucaostatus ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}


	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			FormaCumprimentoExecucaoPs obPersistencia = new FormaCumprimentoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			if (posicao.length() > 0){
				if (tempList != null){
					QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
					tempList.remove(tempList.size()-1);					
				} else QuantidadePaginas = 0;	
			}
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

    public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
        String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            FormaCumprimentoExecucaoPs obPersistencia = new  FormaCumprimentoExecucaoPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
                        
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
}
