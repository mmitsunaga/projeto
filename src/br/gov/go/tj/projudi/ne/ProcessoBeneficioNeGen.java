package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoBeneficioDt;
import br.gov.go.tj.projudi.ps.ProcessoBeneficioPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class ProcessoBeneficioNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6021397468833860118L;	
	protected  ProcessoBeneficioDt obDados;


	public ProcessoBeneficioNeGen() {		 	

		obLog = new LogNe(); 

		obDados = new ProcessoBeneficioDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoBeneficioDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neProcessoBeneficiosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoBeneficioPs obPersistencia = new ProcessoBeneficioPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoBeneficio", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoBeneficio", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoBeneficioDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoBeneficioDt dados) throws Exception {

		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoBeneficioPs obPersistencia = new ProcessoBeneficioPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoBeneficio", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoBeneficioDt consultarId(String id_processobeneficio ) throws Exception {

		ProcessoBeneficioDt dtRetorno=null;
		
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_ProcessoBeneficio" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoBeneficioPs obPersistencia = new ProcessoBeneficioPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processobeneficio ); 
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
		//////System.out.println("..ne-ConsultaProcessoBeneficio" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoBeneficioPs obPersistencia = new ProcessoBeneficioPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	}
