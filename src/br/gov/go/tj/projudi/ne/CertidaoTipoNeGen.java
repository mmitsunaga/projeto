package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CertidaoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CertidaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class CertidaoTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 9023992508661166524L;
	
	protected  CertidaoTipoDt obDados;


	public CertidaoTipoNeGen() {

		

		obLog = new LogNe(); 

		obDados = new CertidaoTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(CertidaoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//////System.out.println("..neCertidaoTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertidaoTipoPs obPersistencia = new CertidaoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("CertidaoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("CertidaoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CertidaoTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(CertidaoTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertidaoTipoPs obPersistencia = new CertidaoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("CertidaoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public CertidaoTipoDt consultarId(String id_certidaotipo ) throws Exception {

		CertidaoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_CertidaoTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoTipoPs obPersistencia = new CertidaoTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_certidaotipo ); 
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
		//////System.out.println("..ne-ConsultaCertidaoTipo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoTipoPs obPersistencia = new CertidaoTipoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	}
