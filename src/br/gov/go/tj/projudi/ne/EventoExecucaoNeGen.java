package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EventoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class EventoExecucaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1273643055204819674L;	
	protected  EventoExecucaoDt obDados;


	public EventoExecucaoNeGen() {
		
		obLog = new LogNe(); 
		obDados = new EventoExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(EventoExecucaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neEventoExecucaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EventoExecucaoPs obPersistencia = new EventoExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("EventoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("EventoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(EventoExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(EventoExecucaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EventoExecucaoPs obPersistencia = new EventoExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("EventoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public EventoExecucaoDt consultarId(String id_eventoexecucao ) throws Exception {

		EventoExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_EventoExecucao" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoPs obPersistencia = new EventoExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_eventoexecucao ); 
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
		//////System.out.println("..ne-ConsultaEventoExecucao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoPs obPersistencia = new EventoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			if (tempList != null){
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);	
			} else QuantidadePaginas = 0;
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoEventoExecucaoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		EventoExecucaoTipoNe EventoExecucaoTipone = new EventoExecucaoTipoNe(); 
		tempList = EventoExecucaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = EventoExecucaoTipone.getQuantidadePaginas();
		EventoExecucaoTipone = null;
		
		return tempList;
	}
	

	public String consultarDescricaoEventoExecucaoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		EventoExecucaoTipoNe EventoExecucaoTipone = new EventoExecucaoTipoNe(); 
		stTemp = EventoExecucaoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	public List consultarDescricaoEventoExecucaoStatus(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			EventoExecucaoStatusNe EventoExecucaoStatusne = new EventoExecucaoStatusNe(); 
			tempList = EventoExecucaoStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = EventoExecucaoStatusne.getQuantidadePaginas();
			EventoExecucaoStatusne = null;
		
		return tempList;
	}

	}
