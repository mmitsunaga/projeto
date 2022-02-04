package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ps.ServentiaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class ServentiaTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5754675014219290135L;
	protected  ServentiaTipoDt obDados;
	protected static List lisDadosCache = null; 

	public ServentiaTipoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ServentiaTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ServentiaTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..neServentiaTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServentiaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ServentiaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			lisDadosCache = null; 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(ServentiaTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServentiaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			lisDadosCache = null; 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ServentiaTipoDt consultarId(String id_serventiatipo ) throws Exception {

		ServentiaTipoDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_ServentiaTipo" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_serventiatipo ); 
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

	public List consultarDescricao(String descricao ) throws Exception {
		List tempList=null;
		//////System.out.println("..ne-ConsultaServentiaTipo" ); 
		FabricaConexao obFabricaConexao = null;
		if(lisDadosCache != null){
			tempList=lisDadosCache;
		}else{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao);
				lisDadosCache=tempList;
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		}
		return tempList;   
		}

	}
