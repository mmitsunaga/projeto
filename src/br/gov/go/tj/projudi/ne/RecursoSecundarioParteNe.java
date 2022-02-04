package br.gov.go.tj.projudi.ne;

import java.util.List;

import org.apache.axis.utils.StringUtils;

import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.projudi.ps.RecursoSecundarioPartePs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

/**
 * @author jvosantos
 * @since 22/11/2019 14:00
 */
public class RecursoSecundarioParteNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1261385876555118658L;

	/**
	 * Salva um Recurso Secundario. Altera o ID do Objeto
	 * @param ob
	 * @param conexao
	 * @throws Exception
	 * @author jvosantos
	 * @since 22/11/2019 14:09
	 * @implNote Não foi implementado um meio de atualizar um Recurso Secundario 
	 */
	public void salvar(RecursoSecundarioParteDt ob, FabricaConexao conexao) throws Exception {
		RecursoSecundarioPartePs recursoSecundarioPartePs = new RecursoSecundarioPartePs(conexao.getConexao());
		
		if(StringUtils.isEmpty(ob.getId())) {
			ob.setId(recursoSecundarioPartePs.inserir(ob));
		}else {
			throw new MensagemException("Não foi implementado um meio de atualizar um Recurso Secundario");
		}
	}
	
	/**
	 * Consulta a lista de todos os Recursos Secundario de uma AUDI_PROC
	 * @param idAudienciaProcesso
	 * @param conexao
	 * @return Lista de Recursos Secundarios da AUDI_PROC
	 * @throws Exception
	 * @author jvosantos
	 * @since 22/11/2019 14:08
	 */
	public List<RecursoSecundarioParteDt> consultarPorAudienciaProcesso(String idAudienciaProcesso, FabricaConexao conexao) throws Exception{
		RecursoSecundarioPartePs recursoSecundarioPartePs = new RecursoSecundarioPartePs(conexao.getConexao());
		
		return recursoSecundarioPartePs.consultarPorAudienciaProcesso(idAudienciaProcesso);
	}
	
	
	/**
	 * Copia o Recurso Secundario da Audiencia Antiga para a Nova (Altera apenas o ID_AUDI_PROC)
	 * @param idAudienciaProcessoAntigo
	 * @param idAudienciaProcessoNovo
	 * @param conexao
	 * @throws Exception
	 * @author jvosantos
	 * @since 22/11/2019 14:07
	 */
	public void copiarDeAudienciaProcessoParaAudienciaProcessoNova(String idAudienciaProcessoAntigo, String idAudienciaProcessoNovo, FabricaConexao conexao) throws Exception {
		List<RecursoSecundarioParteDt> porAudienciaProcesso = consultarPorAudienciaProcesso(idAudienciaProcessoAntigo, conexao);
		
		for(RecursoSecundarioParteDt rec : porAudienciaProcesso) {
			rec.setId(null);
			rec.setId_AudienciaProcesso(idAudienciaProcessoNovo);
			salvar(rec, conexao);
		}
	}
	
}
