package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TemaAssuntoDt;
import br.gov.go.tj.projudi.ps.TemaAssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.ValidacaoUtil;

public class TemaAssuntoNe extends TemaAssuntoNeGen {

	private static final long serialVersionUID = -1993980466982787291L;

	public String Verificar(TemaAssuntoDt dados) {

		String stRetorno = "";

		if (dados.getTemaCodigo().length() == 0)
			stRetorno += "O Campo TemaCodigo é obrigatório.";

		return stRetorno;

	}
	
	public void salvarAssuntos(List listaAssuntos, String idTema, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		TemaAssuntoDt temaAssuntoDt = null;
		if (listaAssuntos != null){
			for (int i = 0; i < listaAssuntos.size(); i++) {
				TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 
				temaAssuntoDt = (TemaAssuntoDt) listaAssuntos.get(i);
				temaAssuntoDt.setId_Tema(idTema);
				/* use o id do objeto para saber se os dados ja estão ou não salvos */
				if (temaAssuntoDt.getId().length()==0) {
					obPersistencia.inserir(temaAssuntoDt);
					obLogDt = new LogDt("Tema",temaAssuntoDt.getId(), temaAssuntoDt.getId_UsuarioLog(),temaAssuntoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",temaAssuntoDt.getPropriedades());
				}else {
					obPersistencia.alterar(temaAssuntoDt); 
					obLogDt = new LogDt("Tema",temaAssuntoDt.getId(), temaAssuntoDt.getId_UsuarioLog(),temaAssuntoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),temaAssuntoDt.getPropriedades());
				}
			}
		}
	}
	
	public List consultarAssuntosId(String idTema) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 
			tempList=obPersistencia.consultarAssuntosId(idTema);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public void excluirAssuntos(List listaAssuntos, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		TemaAssuntoDt temaAssuntoDt = null;

		for (int i = 0; i < listaAssuntos.size(); i++) {
			TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 
			temaAssuntoDt = (TemaAssuntoDt) listaAssuntos.get(i);
			obPersistencia.excluir(temaAssuntoDt.getId()); 
			obLogDt = new LogDt("Tema",temaAssuntoDt.getId(), temaAssuntoDt.getId_UsuarioLog(),temaAssuntoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDados.getPropriedades(),temaAssuntoDt.getPropriedades());
		}
	}
	
	/**
	 * Consulta os assuntos de um determinado ID tema
	 * @param id - identificador do TEMA
	 * @return Lista de Id_cnj_assunto da tabela Assunto
	 */
	public List<Integer> consultarAssuntosCodigoPorIdTema(String id) throws Exception {
		List<Integer> lista = new ArrayList<>();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TemaAssuntoPs obPersistencia = new TemaAssuntoPs(obFabricaConexao.getConexao()); 
			List<TemaAssuntoDt> listaTemaAssunto = obPersistencia.consultarAssuntosId(id);
			for (TemaAssuntoDt item : listaTemaAssunto){
				if (ValidacaoUtil.isNaoVazio(item.getAssuntoCodigo())){
					lista.add(Integer.valueOf(item.getAssuntoCodigo()));
				}
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
}
