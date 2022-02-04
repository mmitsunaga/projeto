package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt;
import br.gov.go.tj.projudi.ps.PonteiroLogCompensarPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class PonteiroLogCompensarNe extends PonteiroLogCompensarNeGen{

	private static final long serialVersionUID = -2307148477809020121L;

	public  String Verificar(PonteiroLogCompensarDt dados ) throws MensagemException {

		String stRetorno="";
		if (dados.getJustificativa().length()==0)
			stRetorno += "O campo Justificativa é obrigatório. \n";
		if (dados.getAreaDistribuicao_O().length()==0)
			stRetorno += "O campo Área de Distribuição Origem é obrigatório. \n";
		if (dados.getServentia_O().length()==0)
			stRetorno += "O campo Serventia Origem é obrigatório. \n";
		if (dados.getServentiaCargo_O().length()==0)
			stRetorno += "O campo Serventia Cargo Origem é obrigatório. \n";
		if (dados.getAreaDistribuicao_D().length()==0)
			stRetorno += "O campo Área de Distribuição Origem é obrigatório. \n";
		if (dados.getServentia_D().length()==0)
			stRetorno += "O campo Serventia Destino é obrigatório. \n";
		if (dados.getServentiaCargo_D().length()==0)
			stRetorno += "O campo Serventia Cargo Destino é obrigatório. \n";
		if (dados.getQtd().length()==0)
			stRetorno += "O campo Quantidade é obrigatório. \n";
		if (dados.getDataInicio().length()==0) {
			stRetorno += "O campo Data Início é obrigatório. \n";
		} else if(dados.getDataInicio().length() > 0){
			try{
				boolean resposta = Funcoes.isDataMaiorIgualDataAtual(dados.getDataInicio());
				if(!resposta)
					stRetorno += "A Data de Início deve ser maior ou igual a data atual. \n";
			} catch (Exception e){
				throw new MensagemException("Erro ao verificar o campo Data de Início. Favor verificar.");
			}
		}
		return stRetorno;
	}
	
	public void finalizarPonteiro(PonteiroLogCompensarDt dados, String idUsuarioServentiaFinalizador) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PonteiroLogCompensarPs obPersistencia = new PonteiroLogCompensarPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("PonteiroLogCompensar",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),dados.getPropriedades(),"");
			obPersistencia.finalizarPonteiro(dados.getId(), idUsuarioServentiaFinalizador);

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoServentiaCargoJSON(String nomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception { 
		String stTemp = (new ServentiaCargoNe()).consultarServentiaCargosJSON(nomeBusca, id_Serventia, posicaoPaginaAtual);
		return stTemp;
	}

	public List consultarPonteiroCompensar(String id_AreaDistribuicao, String id_Serv, String id_ServentiaCargo, FabricaConexao obFabricaConexao2) throws Exception {
        List tempList = null;
                                   
        PonteiroLogCompensarPs obPersistencia = new  PonteiroLogCompensarPs(obFabricaConexao2.getConexao());
        tempList = obPersistencia.consultarPonteiroCompensar(id_AreaDistribuicao, id_Serv, id_ServentiaCargo);
                      
        return tempList;
	}

}
