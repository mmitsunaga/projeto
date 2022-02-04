package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoResponsavelPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class AudienciaProcessoResponsavelNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -956900009180515258L;	
	protected  AudienciaProcessoResponsavelDt obDados;


	public AudienciaProcessoResponsavelNeGen() {

		

		obLog = new LogNe(); 

		obDados = new AudienciaProcessoResponsavelDt(); 

	}


//---------------------------------------------------------
	public void salvar(AudienciaProcessoResponsavelDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 
		
		//////System.out.println("..neAudienciaProcessoResponsavelsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaProcessoResponsavelPs obPersistencia = new AudienciaProcessoResponsavelPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AudienciaProcessoResponsavel", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AudienciaProcessoResponsavel", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AudienciaProcessoResponsavelDt dados ); 


//---------------------------------------------------------

	public void excluir(AudienciaProcessoResponsavelDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaProcessoResponsavelPs obPersistencia = new AudienciaProcessoResponsavelPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("AudienciaProcessoResponsavel", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public AudienciaProcessoResponsavelDt consultarId(String id_audienciaprocessoresponsavel ) throws Exception {

		AudienciaProcessoResponsavelDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_AudienciaProcessoResponsavel" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoResponsavelPs obPersistencia = new AudienciaProcessoResponsavelPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_audienciaprocessoresponsavel ); 
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
		//////System.out.println("..ne-ConsultaAudienciaProcessoResponsavel" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AudienciaProcessoResponsavelPs obPersistencia = new AudienciaProcessoResponsavelPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoAudienciaProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		AudienciaProcessoNe AudienciaProcessone = new AudienciaProcessoNe(); 
		tempList = AudienciaProcessone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = AudienciaProcessone.getQuantidadePaginas();
		AudienciaProcessone = null;
		
		return tempList;
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
			tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
			UsuarioServentiane = null;
		
		return tempList;
	}

	public List consultarDescricaoCargoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			CargoTipoNe CargoTipone = new CargoTipoNe(); 
			tempList = CargoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = CargoTipone.getQuantidadePaginas();
			CargoTipone = null;
		
		return tempList;
	}

	}
