package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ExpedirMandadoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.utils.FabricaConexao;
//import br.gov.go.tj.projudi.ps.ExpedirMandadoPs;

//---------------------------------------------------------
public abstract class ExpedirMandadoNeGen extends Negocio{


	//protected ExpedirMandadoPs obPersistencia; 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3335316585651219079L;
	protected  ExpedirMandadoDt obDados;
	
	 
	public ExpedirMandadoNeGen() {

//		obPersistencia = new ExpedirMandadoPs(); 
 
		obLog = new LogNe(); 

		obDados = new ExpedirMandadoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ExpedirMandadoDt dados ) throws Exception {
		
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neExpedirMandadosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
//			ob obPersistencia = new ob( obFabricaConexao);
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getCodDocumento().equalsIgnoreCase("" ) ) {
				obLogDt = new LogDt("ExpedirMandado", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
//				obPersistencia.inserir(dados);
			}else {
				obLogDt = new LogDt("ExpedirMandado", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
//				obPersistencia.alterar(dados); 
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		

	}


//---------------------------------------------------------
	 public abstract String Verificar(ExpedirMandadoDt dados ); 


//---------------------------------------------------------

	public void excluir(ExpedirMandadoDt dados) throws Exception {
	
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
//			ob obPersistencia = new ob( obFabricaConexao);
			obLogDt = new LogDt("ExpedirMandado", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
//			obPersistencia.excluir(dados.getCodDocumento());

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		

	}

 //---------------------------------------------------------

	public ExpedirMandadoDt consultarId(String coddocumento ) throws Exception {

		ExpedirMandadoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaCodDocumento" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

//			ob obPersistencia = new ob( obFabricaConexao);
//			dtRetorno= obPersistencia.consultarId(coddocumento ); 
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
}
