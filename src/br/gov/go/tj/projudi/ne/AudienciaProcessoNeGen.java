package br.gov.go.tj.projudi.ne;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class AudienciaProcessoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3087756312896748848L;
	
	protected  AudienciaProcessoDt obDados;


	public AudienciaProcessoNeGen() {
		

		obLog = new LogNe(); 

		obDados = new AudienciaProcessoDt(); 

	}


//---------------------------------------------------------
	public void salvar(AudienciaProcessoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neAudienciaProcessosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AudienciaProcesso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AudienciaProcesso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AudienciaProcessoDt dados ); 


//---------------------------------------------------------

	public void excluir(AudienciaProcessoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("AudienciaProcesso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public AudienciaProcessoDt consultarId(String id_audienciaprocesso ) throws Exception {
		if(StringUtils.isEmpty(id_audienciaprocesso)) return null;
		AudienciaProcessoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_AudienciaProcesso" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_audienciaprocesso ); 
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
		//////System.out.println("..ne-ConsultaAudienciaProcesso" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoAudiencia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		AudienciaNe Audienciane = new AudienciaNe(); 
		tempList = Audienciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Audienciane.getQuantidadePaginas();
		Audienciane = null;
		
		return tempList;
	}

	public List consultarDescricaoAudienciaProcessoStatus(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AudienciaProcessoStatusNe AudienciaProcessoStatusne = new AudienciaProcessoStatusNe(); 
			tempList = AudienciaProcessoStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AudienciaProcessoStatusne.getQuantidadePaginas();
			AudienciaProcessoStatusne = null;
		
		return tempList;
	}

	public List consultarDescricaoServentiaCargo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe(); 
			tempList = ServentiaCargone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
			ServentiaCargone = null;
		
		return tempList;
	}

	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoNe Processone = new ProcessoNe(); 
			tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Processone.getQuantidadePaginas();
			Processone = null;
		
		return tempList;
	}

	}
