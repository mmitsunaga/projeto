package br.gov.go.tj.projudi.ne;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import br.gov.go.tj.projudi.dt.relatorios.InfosegDt;
import br.gov.go.tj.projudi.ps.InfosegPs;
import br.gov.go.tj.utils.FabricaConexao;

public class InfosegNe extends Negocio {

	private static final long serialVersionUID = 1382191779971832861L;
	
	protected LogNe obLog;
	protected InfosegDt obDados;

	public InfosegNe() {
			
		obLog = new LogNe();
		obDados = new InfosegDt();
	}

	// ---------------------------------------------------------
	public String verificar(String tipoArquivo) throws ParseException {
		String stRetorno = "";

		if (tipoArquivo.length() == 0) stRetorno += "Informe o arquivo a ser gerado! \n";
		
		return stRetorno;
	}
	// ---------------------------------------------------------
	
	/**
	 * Consulta dados do sentenciado e gera o arquivo .txt
	 * método utilizado na classe JobArquivosInfoseg do projeto scheduling
	 * @return lista sentenciado
	 * @throws Exception
	 */
	public void gerarArquivoSentenciado(String nomeArquivo) throws Exception {
		List listaSentenciado = null;
		FabricaConexao obFabricaConexao = null;
	
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			InfosegPs obPersistencia = new InfosegPs(obFabricaConexao.getConexao());

			listaSentenciado = obPersistencia.consultarDadosSentenciado();

			////************************************///
			//DADOS DO PRIMEIRO ARQUIVO: INFOSEG_DADOS
//				numero_execpen (8) --número do processo (25)
//				nome_sentenciado (70)
//				nome_pai (70)
//				nome_mae (70)
//				cidade_nascimento (35)
//				estado_nascimento (2)
//				nacionalidade (12) --retirado
//				numero_identidade (15)
//				sigla_orgao_expedidor (8)
//				numero_cpf (11)
//				foradigo (sim ou não) (3)
			////************************************///
			
			
			FileWriter outFile = new FileWriter(nomeArquivo);
			PrintWriter out = new PrintWriter(outFile);
			String separadorTxt = ";";

			for (InfosegDt dados : (List<InfosegDt>)listaSentenciado) {
				out.println(completarEspacos(dados.getNumeroCompletoProcesso(), 25) + separadorTxt + 
							completarEspacos(dados.getNomeSentenciado(), 70) + separadorTxt +
							completarEspacos(dados.getNomePai(), 70) + separadorTxt +
							completarEspacos(dados.getNomeMae(), 70) + separadorTxt +
							completarEspacos(dados.getCidadeNascimento(), 35) + separadorTxt +
							completarEspacos(dados.getUfNascimento(), 2) + separadorTxt +
							completarEspacos(dados.getNumeroRg(), 15) + separadorTxt +
							completarEspacos(dados.getSiglaRg(), 8) + separadorTxt +
							completarEspacos(dados.getCpf(), 11) + separadorTxt +
							completarEspacos(dados.getForagido(), 3));
			}
			
			out.close();
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta dados da condenação dos sentenciados e gera o arquivo .txt
	 * método utilizado na classe JobArquivosInfoseg do projeto scheduling
	 * @return lista sentenciado
	 * @throws Exception
	 */
	public void gerarArquivoProcesso(String nomeArquivo) throws Exception {
		List listaProcesso = null;
		FabricaConexao obFabricaConexao = null;
	
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			InfosegPs obPersistencia = new InfosegPs(obFabricaConexao.getConexao());

			listaProcesso = obPersistencia.consultarDadosProcesso();

			////************************************///
			//DADOS DO PRIMEIRO ARQUIVO: INFOSEG_DADOS
//				numero_execpen (8)  --retirado
//				numero_processo(20) --número do processo (25)
//				ano_pena (3)
//				mes_pena (2)
//				dia_pena (2)
			////************************************///
			
			FileWriter outFile = new FileWriter(nomeArquivo);
			PrintWriter out = new PrintWriter(outFile);
			
			String separadorTxt = ";";
			for (InfosegDt dados : (List<InfosegDt>)listaProcesso) {
				out.println(completarEspacos(dados.getNumeroCompletoProcesso(), 25) + separadorTxt + 
							completarEspacos(dados.getTempoPenaAno(), 3) + separadorTxt +
							completarEspacos(dados.getTempoPenaMes(), 2) + separadorTxt +
							completarEspacos(dados.getTempoPenaDia(), 2));
			}
			out.close();
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public static String completarEspacos(String valor, int qtd) {
		StringBuffer stTemp = new StringBuffer(valor);

		for (int i=0; i<(qtd - valor.length()); i++)
			stTemp.insert(0, " ");

		return stTemp.toString();
	}
	
}
