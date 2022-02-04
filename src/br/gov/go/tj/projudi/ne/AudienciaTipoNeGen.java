package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AudienciaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class AudienciaTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8962140319965753734L;


	
	
	protected  AudienciaTipoDt obDados;
	
	 


	public AudienciaTipoNeGen() {

		

		obLog = new LogNe(); 

		obDados = new AudienciaTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(AudienciaTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neAudienciaTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaTipoPs obPersistencia = new AudienciaTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AudienciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AudienciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AudienciaTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(AudienciaTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaTipoPs obPersistencia = new AudienciaTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("AudienciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public AudienciaTipoDt consultarId(String id_audienciatipo ) throws Exception {

		AudienciaTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_AudienciaTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaTipoPs obPersistencia = new AudienciaTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_audienciatipo ); 
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
		//////System.out.println("..ne-ConsultaAudienciaTipo" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AudienciaTipoPs obPersistencia = new AudienciaTipoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	}
