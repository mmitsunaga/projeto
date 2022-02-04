package br.gov.go.tj.projudi.ne;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.axis.utils.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaProcessoPendenciaDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoPendenciaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AudienciaProcessoPendenciaNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1958092340947538353L;

	public String salvar(AudienciaProcessoPendenciaDt dados, FabricaConexao obFabricaConexao) throws Exception {
		if (dados == null)
			return null;

		AudienciaProcessoPendenciaPs obPersistencia = new AudienciaProcessoPendenciaPs(obFabricaConexao.getConexao());

		return obPersistencia.inserir(dados);
	}

	public String salvar(AudienciaProcessoPendenciaDt dados) throws Exception {
		if (dados == null)
			return null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			String tmp = salvar(dados, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();

			return tmp;
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String salvar(String idPend, String idAudiProc, FabricaConexao obFabricaConexao) throws Exception {
		return salvar(new AudienciaProcessoPendenciaDt(idPend, idAudiProc), obFabricaConexao);
	}

	public String salvar(String idPend, String idAudiProc) throws Exception {
		return salvar(new AudienciaProcessoPendenciaDt(idPend, idAudiProc));
	}

	public void salvar(List<AudienciaProcessoPendenciaDt> lista, FabricaConexao obFabricaConexao) throws Exception {
		if (lista == null || lista.isEmpty())
			return;
		AudienciaProcessoPendenciaPs obPersistencia = new AudienciaProcessoPendenciaPs(obFabricaConexao.getConexao());
		
		for(AudienciaProcessoPendenciaDt app : lista) // jvosantos - 12/09/2019 17:50 - Correção inserir lista
			obPersistencia.inserir(app);
	}

	public void salvar(List<AudienciaProcessoPendenciaDt> lista) throws Exception {
		if (lista == null || lista.isEmpty())
			return;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			salvar(lista, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public void salvar(List<String> pendencia, String idAudiProc, FabricaConexao obFabricaConexao) throws Exception {
		if (pendencia == null || pendencia.isEmpty())
			return;

		AudienciaProcessoPendenciaPs obPersistencia = new AudienciaProcessoPendenciaPs(obFabricaConexao.getConexao());

		List<AudienciaProcessoPendenciaDt> lista = pendencia.stream()
				.map(x -> new AudienciaProcessoPendenciaDt(x, idAudiProc)).collect(Collectors.toList());

		for(AudienciaProcessoPendenciaDt app : lista) // jvosantos - 12/09/2019 17:50 - Correção inserir lista
			obPersistencia.inserir(app);
	}

	public void salvar(List<String> pendencia, String idAudiProc) throws Exception {
		if (pendencia == null || pendencia.isEmpty())
			return;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			salvar(pendencia, idAudiProc, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String consultarPorIdPend(String idPend) throws Exception {
		if (StringUtils.isEmpty(idPend))
			return null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarPorIdPend(idPend, obFabricaConexao);
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 06/11/2019 12:20 - Método para buscar a audiencia_processo que tem a pendencia e está em andamento
	public String consultarPorIdPendEmAndamento(String idPend) throws Exception {
		if (StringUtils.isEmpty(idPend))
			return null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarPorIdPendEmAndamento(idPend, obFabricaConexao);
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String consultarPorIdPend(String idPend, FabricaConexao obFabricaConexao) throws Exception {
		if (StringUtils.isEmpty(idPend))
			return null;
		AudienciaProcessoPendenciaPs obPersistencia = new AudienciaProcessoPendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPorIdPend(idPend);
	}

	// jvosantos - 06/11/2019 12:20 - Método para buscar a audiencia_processo que tem a pendencia e está em andamento
	public String consultarPorIdPendEmAndamento(String idPend, FabricaConexao obFabricaConexao) throws Exception {
		if (StringUtils.isEmpty(idPend))
			return null;
		AudienciaProcessoPendenciaPs obPersistencia = new AudienciaProcessoPendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPorIdPendEmAndamento(idPend);
	}

}
