package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.LocalCumprimentoPenaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class LocalCumprimentoPenaNe extends LocalCumprimentoPenaNeGen {

	private static final long serialVersionUID = -8367836930548614813L;

	public String Verificar(LocalCumprimentoPenaDt dados) {

		String stRetorno = "";

		if (dados.getLocalCumprimentoPena().length() == 0)
			stRetorno += "O campo Nome é obrigatório.";
//		if (dados.getEnderecoLocal().getLogradouro().length() == 0)
//			stRetorno += "O campo Logradouro é obrigatório.";
//		if (dados.getEnderecoLocal().getBairro().length() == 0)
//			stRetorno += "O campo Bairro é obrigatório.";
//		if (dados.getEnderecoLocal().getCep().length() == 0)
//			stRetorno += "O campo CEP é obrigatório.";

		return stRetorno;

	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoBairro(String tempNomeBusca, String cidade, String uf, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		BairroNe neObjeto = new BairroNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, cidade, uf, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();

		neObjeto = null;
		return tempList;

	}

	public void salvar(LocalCumprimentoPenaDt dados) throws Exception {

		LogDt obLogDt;
		EnderecoNe enderecoNe = new EnderecoNe();
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LocalCumprimentoPenaPs obPersistencia = new  LocalCumprimentoPenaPs(obFabricaConexao.getConexao());

			// Salva endereço do local
			if (dados.getEndereco() != null && dados.getEndereco().length() > 0){
				enderecoNe.salvar(dados.getEnderecoLocal(), obFabricaConexao);
				dados.setId_Endereco(dados.getEnderecoLocal().getId());	
			}

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("LocalCumprimentoPena", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("LocalCumprimentoPena", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Consulta o local de cumprimento de pena atual, com endereço
	 * 
	 * @param idProcesso:
	 *            identificação do processo
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoComEndereco(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stRetorno = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LocalCumprimentoPenaPs obPersistencia = new  LocalCumprimentoPenaPs(obFabricaConexao.getConexao());
			stRetorno = obPersistencia.consultarDescricaoComEndereco(idProcesso);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LocalCumprimentoPenaPs obPersistencia = new  LocalCumprimentoPenaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";

		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);

		return stTemp;
	}
	
	public BairroDt consultarDescricaoBairroId(String idBairro) throws Exception {
		BairroDt bairroDt = new BairroDt();

		BairroNe Bairrone = new BairroNe();
		bairroDt = Bairrone.consultarId(idBairro);

		return bairroDt;
	}
	
	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LocalCumprimentoPenaPs obPersistencia = new LocalCumprimentoPenaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			if (posicao.length() > 0){
				if (tempList != null){
					QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
					tempList.remove(tempList.size()-1);
				} else QuantidadePaginas = 0;	
			}

		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

}
