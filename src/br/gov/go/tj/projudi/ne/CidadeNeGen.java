package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CidadePs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class CidadeNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6227800687195985080L;
	
	protected  CidadeDt obDados;


	public CidadeNeGen() {
		
		obLog = new LogNe(); 

		obDados = new CidadeDt(); 

	}


//---------------------------------------------------------
	public void salvar(CidadeDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//////System.out.println("..neCidadesalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Cidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Cidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			//throw new MensagemException("erro bla bla bal");

//		}catch(Exception e){
//			obFabricaConexao.cancelarTransacao();
//			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CidadeDt dados ); 


//---------------------------------------------------------

	public void excluir(CidadeDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Cidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public CidadeDt consultarId(String id_cidade ) throws Exception {

		CidadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_Cidade" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_cidade ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaCidade" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoEstado(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		EstadoNe Estadone = new EstadoNe(); 
		tempList = Estadone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Estadone.getQuantidadePaginas();
		Estadone = null;
		
		return tempList;
	}

}
