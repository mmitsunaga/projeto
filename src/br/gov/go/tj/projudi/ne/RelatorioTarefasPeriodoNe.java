package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.utils.TJDataHora;

public class RelatorioTarefasPeriodoNe extends Negocio{


    /**
	 * 
	 */
	private static final long serialVersionUID = 5635644302620510840L;
	
	public List consultarDescricaoProjeto(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ProjetoNe Projetone = new ProjetoNe(); 
		tempList = Projetone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Projetone.getQuantidadePaginas();
		Projetone = null;

		return tempList;
	}
	
	public List consultarServentiaCargos(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		tempList = ServentiaCargone.consultarServentiaCargos(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
		ServentiaCargone = null;
		
		return tempList;
	}

	/**
	 * Retorna uma lista de tarefas para emissão de relatório em tela 
	 * 
	 * @param periodoInicialUtilizado
	 * @param periodoFinalUtilizado
	 * @param idProjeto
	 * @param idServentiaCargo
	 * @author Márcio Gomes
	 * @return
	 */
	public List obtenhaRelatorioListaTarefas(TJDataHora periodoInicialUtilizado, TJDataHora periodoFinalUtilizado, String idProjeto, String idServentiaCargo) throws Exception {
		List tempList=null;
		
		TarefaNe tarefaNe = new TarefaNe(); 
		tempList = tarefaNe.obtenhaRelatorioListaTarefas(periodoInicialUtilizado, periodoFinalUtilizado, idProjeto, idServentiaCargo);			
		tarefaNe = null;

		return tempList;
	}

}
