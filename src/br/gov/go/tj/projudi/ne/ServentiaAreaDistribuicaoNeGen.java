package br.gov.go.tj.projudi.ne;

import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ServentiaAreaDistribuicaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ServentiaAreaDistribuicaoNeGen extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9063006947048059541L;
	protected  ServentiaAreaDistribuicaoDt obDados;

	public ServentiaAreaDistribuicaoNeGen() {	

		obLog = new LogNe(); 

		obDados = new ServentiaAreaDistribuicaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ServentiaAreaDistribuicaoDt dados, UsuarioDt usuarioDt ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		////System.out.println("..neServAreaDistsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaAreaDistribuicaoPs obPersistencia = new ServentiaAreaDistribuicaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServAreaDist",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
				
				/* ---------- PONTEIRO ----------------*/
				///salvo o ponteiro
				PonteiroLogDt ponteiroLogDt = new PonteiroLogDt(dados.getId_AreaDist(), PonteiroLogTipoDt.INCLUSAO_PROBABILIDADE_DISTRIBUICAO, "", dados.getId_Serv(), usuarioDt.getId(),usuarioDt.getId_UsuarioServentia(),  new Date(), "Probabilidade Definida para:"+dados.getQuantidadeDistribuicaoDescricao(), 0, "");
				new PonteiroLogNe().salvar(ponteiroLogDt,obFabricaConexao );
				/* ---------- PONTEIRO ----------------*/
				
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ServAreaDist",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
				
				/* ---------- PONTEIRO ----------------*/
				///salvo o ponteiro
				PonteiroLogDt ponteiroLogDt = new PonteiroLogDt(dados.getId_AreaDist(), PonteiroLogTipoDt.ALTERACAO_PROBABILIDADE_DISTRIBUICAO, "", dados.getId_Serv(),  usuarioDt.getId(),usuarioDt.getId_UsuarioServentia(),  new Date(), "Probabilidade Definida para:"+dados.getQuantidadeDistribuicaoDescricao(), 0, "");
				new PonteiroLogNe().salvar(ponteiroLogDt,obFabricaConexao );
				/* ---------- PONTEIRO ----------------*/
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaAreaDistribuicaoDt dados ); 


//---------------------------------------------------------

	public void excluir(ServentiaAreaDistribuicaoDt dados, UsuarioDt usuarioDt) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaAreaDistribuicaoPs obPersistencia = new ServentiaAreaDistribuicaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServAreaDist",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			
			/* ---------- PONTEIRO ----------------*/
			///salvo o ponteiro
			PonteiroLogDt ponteiroLogDt = new PonteiroLogDt(dados.getId_AreaDist(), PonteiroLogTipoDt.ALTERACAO_PROBABILIDADE_DISTRIBUICAO, "", dados.getId_Serv(), usuarioDt.getId(),usuarioDt.getId_UsuarioServentia(),  new Date(), "Probabilidade De :"+dados.getQuantidadeDistribuicaoDescricao()+" foi Excluída!", 0, "");
			new PonteiroLogNe().salvar(ponteiroLogDt,obFabricaConexao );
			/* ---------- PONTEIRO ----------------*/
			
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ServentiaAreaDistribuicaoDt consultarId(String id_servareadist ) throws Exception {

		ServentiaAreaDistribuicaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-ConsultaId_ServAreaDist" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaAreaDistribuicaoPs obPersistencia = new ServentiaAreaDistribuicaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_servareadist ); 
			obDados.copiar(dtRetorno);
		} finally {
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
		////System.out.println("..ne-ConsultaServAreaDist" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaAreaDistribuicaoPs obPersistencia = new ServentiaAreaDistribuicaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoServ(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ServentiaNe Servne = new ServentiaNe(); 
		tempList = Servne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Servne.getQuantidadePaginas();
		Servne = null;
		return tempList;
	}

	public List consultarDescricaoAreaDist(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		AreaDistribuicaoNe AreaDistne = new AreaDistribuicaoNe(); 
		tempList = AreaDistne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = AreaDistne.getQuantidadePaginas();
		AreaDistne = null;
		return tempList;
	}

}
