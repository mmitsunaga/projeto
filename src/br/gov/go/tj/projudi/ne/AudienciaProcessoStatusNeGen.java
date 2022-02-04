package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoStatusPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class AudienciaProcessoStatusNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 200909358928064759L;
	
	protected  AudienciaProcessoStatusDt obDados;


	public AudienciaProcessoStatusNeGen() {

		

		obLog = new LogNe(); 

		obDados = new AudienciaProcessoStatusDt(); 

	}


//---------------------------------------------------------
	public void salvar(AudienciaProcessoStatusDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neAudienciaProcessoStatussalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaProcessoStatusPs obPersistencia = new AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AudienciaProcessoStatus", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AudienciaProcessoStatus", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AudienciaProcessoStatusDt dados ); 


//---------------------------------------------------------

	public void excluir(AudienciaProcessoStatusDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaProcessoStatusPs obPersistencia = new AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("AudienciaProcessoStatus", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public AudienciaProcessoStatusDt consultarId(String id_audienciaprocessostatus ) throws Exception {

		AudienciaProcessoStatusDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_AudienciaProcessoStatus" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_audienciaprocessostatus ); 
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
		//////System.out.println("..ne-ConsultaAudienciaProcessoStatus" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoServentiaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ServentiaTipoNe ServentiaTipone = new ServentiaTipoNe(); 
		tempList = ServentiaTipone.consultarDescricao(tempNomeBusca);
		QuantidadePaginas = ServentiaTipone.getQuantidadePaginas();
		ServentiaTipone = null;
		
		return tempList;
	}

	}
