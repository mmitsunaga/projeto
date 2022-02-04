package br.gov.go.tj.projudi.ne;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import br.gov.go.tj.projudi.ps.EstatisticaProdutividadeItemPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Relatorios;

//---------------------------------------------------------
public class EstatisticaProdutividadeItemNe extends EstatisticaProdutividadeItemNeGen{

	private static final long serialVersionUID = -3471783819317784901L;

    //---------------------------------------------------------
	public  String Verificar(EstatisticaProdutividadeItemDt dados ) {

		String stRetorno="";

		//System.out.println("..neEstatisticaProdutividadeItemVerificar()");
		return stRetorno;

	}
	
	/**
	 * Método que lista todos os registros da tabela EstatisticaProdutividadeItem a partir de uma descrição. Esta consulta não aplica limitação de tela para paginação.
	 * 
	 * @param descricao - descrição do registro (não obrigatório)
	 * @return lista de Estatistica Produtividade Item
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarEstatisticasProdutividadeItem(String descricao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProdutividadeItemPs obPersistencia = new EstatisticaProdutividadeItemPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.listarEstatisticasProdutividadeItem(descricao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * 
	 * @param descricao
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List consultarEstatisticaProdutividadeItem(String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProdutividadeItemPs obPersistencia = new EstatisticaProdutividadeItemPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarEstatisticaProdutividadeItem(descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Método que cria o relatório PDF ou TXT da listagem de Estatistica Produtividade Item.
	 * 
	 * @param diretorioProjeto - diretório onde se encontra o arquivo jasper compilado
	 * @param nomeBusca - nome usado na consulta de estatistica
	 * @param tipoArquivo - tipo de arquivo selecionado (txt ou pdf)
	 * @param usuarioResponsavelRelatorio - usuário que está solicitando a emissão do relatório
	 * @return relatório
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relEstatisticaProdutividade(String diretorioProjeto, String nomeBusca, Integer tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			List listaEstatisticas = listarEstatisticasProdutividadeItem(nomeBusca);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "listagemEstatisticaProdutividadeItem";

			// Tipo Arquivo == 1 é PDF , TipoArquivo == 2 é TXT
			if (tipoArquivo != null && tipoArquivo.equals(1)) {

				// PARÂMETROS DO RELATÓRIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Listagem de Estatísticas de Produtividade");
				parametros.put("parametroConsulta", nomeBusca.toUpperCase());
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

				temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaEstatisticas);
				

			} else {
				String conteudoArquivo = "";
				Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < listaEstatisticas.size(); i++) {
					EstatisticaProdutividadeItemDt obTemp = (EstatisticaProdutividadeItemDt) listaEstatisticas.get(i);
					conteudoArquivo += obTemp.getEstatisticaProdutividadeItem() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
	/**
	 * Consulta EstatisticaProdutividadeItem (utilizando filtro por descricao).
	 * 
	 * @param descricao - descrição do item a ser buscado
	 * @param posicao - posição da página
	 * @return lista de itens
	 * @throws Exception
	 * @author hmgodinho
	 */
    public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
        String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstatisticaProdutividadeItemPs obPersistencia = new  EstatisticaProdutividadeItemPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
                        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
}
