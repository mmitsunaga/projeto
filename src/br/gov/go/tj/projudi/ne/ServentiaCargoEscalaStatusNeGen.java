package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.ps.ServentiaCargoEscalaStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class ServentiaCargoEscalaStatusNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8383160735251731864L;
	
	protected  ServentiaCargoEscalaStatusDt obDados;


	public ServentiaCargoEscalaStatusNeGen() {

		

		obLog = new LogNe(); 

		obDados = new ServentiaCargoEscalaStatusDt(); 

	}


//---------------------------------------------------------
	public void salvar(ServentiaCargoEscalaStatusDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaStatusPs obPersistencia = new ServentiaCargoEscalaStatusPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("UsuarioServentiaEscalaStatus",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("UsuarioServentiaEscalaStatus",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaCargoEscalaStatusDt dados ); 


//---------------------------------------------------------

	public void excluir(ServentiaCargoEscalaStatusDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaStatusPs obPersistencia = new ServentiaCargoEscalaStatusPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServentiaCargoEscalaStatus",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ServentiaCargoEscalaStatusDt consultarId(String id_usuarioserventiaescalastatus ) throws Exception {

		ServentiaCargoEscalaStatusDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoEscalaStatusPs obPersistencia = new ServentiaCargoEscalaStatusPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_usuarioserventiaescalastatus ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoEscalaStatusPs obPersistencia = new ServentiaCargoEscalaStatusPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

}
