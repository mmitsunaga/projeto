package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAssuntoDt;
import br.gov.go.tj.projudi.ps.ProcessoParteAssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoParteAssuntoNe extends ProcessoParteAssuntoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 8966651147389275506L;

	//---------------------------------------------------------
	public  String Verificar(ProcessoParteAssuntoDt dados ) {

		String stRetorno="";

		if (dados.getProcessoParteNome().length()==0)
			stRetorno += "O Campo Nome é obrigatório.";
		if (dados.getDataInclusao().length()==0)
			stRetorno += "O Campo DataInclusao é obrigatório.";

		return stRetorno;

	}
	
//---------------------------------------------------------
	public void salvar(ProcessoParteAssuntoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAssuntoPs obPersistencia = new ProcessoParteAssuntoPs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParteAssunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoParteAssunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			//tento salvar o assunto no processo, so salvará se não existir
			ProcessoAssuntoNe assunto= new ProcessoAssuntoNe();
			ProcessoAssuntoDt dtAsuntoProc = new ProcessoAssuntoDt();
			dtAsuntoProc.setId_Assunto(dados.getId_Assunto());
			dtAsuntoProc.setId_Processo(dados.getId_Processo());
			dtAsuntoProc.setId_UsuarioLog(dados.getId_UsuarioLog());
			dtAsuntoProc.setIpComputadorLog(dados.getIpComputadorLog());
			assunto.inserirAssuntoProcesso(dtAsuntoProc, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> ProcessoParteAssuntoNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	//---------------------------------------------------------

	public void excluir(ProcessoParteAssuntoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAssuntoPs obPersistencia = new ProcessoParteAssuntoPs(obFabricaConexao.getConexao());
			ProcessoAssuntoNe assunto = new ProcessoAssuntoNe();

			obLogDt = new LogDt("ProcessoParteAssunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());
			assunto.excluirAssuntoProcesso(dados.getId_Processo(), dados.getId_Assunto(), dados.getId_ProcessoParte(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), obFabricaConexao);

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			
			
			obFabricaConexao.finalizarTransacao();
		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
		
	public String consultarAssuntosJSON(String id_processo_parte ) throws Exception {
	
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoParteAssuntoPs obPersistencia = new ProcessoParteAssuntoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarAssuntosJSON(id_processo_parte);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
	
	public String consultarDescricaoAssuntoJSON(String descricao, String cnjCodigo, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new AssuntoNe()).consultarDescricaoArtigoJSON(descricao, cnjCodigo, PosicaoPaginaAtual);
		return stTemp;
	}


}
