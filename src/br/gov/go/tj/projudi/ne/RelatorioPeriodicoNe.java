package br.gov.go.tj.projudi.ne;

import java.io.Serializable;

import br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt;
import br.gov.go.tj.projudi.ps.AreaDistribuicaoPs;
import br.gov.go.tj.projudi.ps.RelatorioPeriodicoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RelatorioPeriodicoNe extends RelatorioPeriodicoNeGen implements Serializable {

	private static final long serialVersionUID = 8848217326270406671L;

	public String Verificar(RelatorioPeriodicoDt dados) {
		String stRetorno = "";
		if (dados.getRelatorioPeriodico().length() == 0)
			stRetorno += "O campo Nome é obrigatório.";
		if (dados.getCodigoSql().length() == 0)
			stRetorno += "O campo Código Sql é obrigatório.";
		return stRetorno;

	}

	/**
	 * Método que gera o Relatório Periódico baseado nos valores informados em tela.
	 * 
	 * @param relatorioPeriodicoDt - relatório periódico preenchido em tela
	 * @return conteúdo do arquivo texto
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] gerarRelatorioPeriodico(RelatorioPeriodicoDt relatorioPeriodicoDt) throws Exception {
		byte[] temp = null;
		ResultSetTJGO rs = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RelatorioPeriodicoPs obPersistencia = new RelatorioPeriodicoPs(obFabricaConexao.getConexao());

			rs = obPersistencia.gerarRelatorioPeriodico(relatorioPeriodicoDt.getCodigoSql());

			String conteudoArquivo = "";
			String separadorTxt = Relatorios.getSeparadorRelatorioTxt();

			while (rs.next()) {
				if (!conteudoArquivo.equals("")) {
					conteudoArquivo += "\n";
				}
				if (rs.getString(1) != null)
					conteudoArquivo += rs.getString(1);
				int totalColunas = rs.getColumnCount();
				for (int i = 2; i <= totalColunas; i++) {
					if (rs.getString(i) != null) {
						conteudoArquivo += separadorTxt + rs.getString(i);
					} else {
						conteudoArquivo += separadorTxt + " ";
					}
				}
			}
			temp = conteudoArquivo.getBytes();

		
		} finally{
			if (rs != null) rs.close();
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				RelatorioPeriodicoPs obPersistencia = new  RelatorioPeriodicoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
}
