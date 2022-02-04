package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.ImportaEscalaOficiaisDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.EscalaMgDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.projudi.ps.ImportaDadosSPGPs;
import br.gov.go.tj.projudi.ps.EscalaMgPs;
import br.gov.go.tj.projudi.ps.UsuarioPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class EscalaMgNe extends EscalaMgNeGen {

	private static final long serialVersionUID = -1160880153661271645L;

	public void CadastraEscalaMg(EscalaMgDt escalaMgDt) throws Exception {
		escalaMgDt.setDataInicio(Funcoes.DataHora(new Date()));
		super.salvar(escalaMgDt);
	}

	public void AlteraEscalaMg(String idEscalaMg, UsuarioNe usuarioSessao, String ip)
			throws Exception {
		EscalaMgDt escalaMgDt = new EscalaMgDt();
		escalaMgDt = consultarId(idEscalaMg);
		escalaMgDt.setDataFim(Funcoes.DataHora(new Date()));
		escalaMgDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		escalaMgDt.setIpComputadorLog(ip);
		super.salvar(escalaMgDt);
	}

	public List ListaTodosEscala(String id_serv) throws Exception {

		List listaEscalaMg= new ArrayList<>();
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EscalaMgPs obPersistencia = new EscalaMgPs(obFabricaConexao.getConexao());

			listaEscalaMg = obPersistencia.listaTodosEscala(id_serv);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaEscalaMg;
	}


	public EscalaMgDt consultaPorIdUsuarioAtivo(String idUsuario) throws Exception {

		EscalaMgDt escalaMgDt = null;
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EscalaMgPs obPersistencia = new EscalaMgPs(obFabricaConexao.getConexao());

			escalaMgDt = obPersistencia.consultaPorIdUsuarioAtivo(idUsuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return escalaMgDt;
	}
	
	public EscalaMgDt consultaPorIdEscala(String idEscala) throws Exception {

		EscalaMgDt escalaMgDt = null;
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EscalaMgPs obPersistencia = new EscalaMgPs(obFabricaConexao.getConexao());

			escalaMgDt = obPersistencia.consultaPorIdEscala(idEscala);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return escalaMgDt;
	}
	
	public List listaCenopsParaMandadoGratuito() throws Exception {

		List listaEscalaMg= new ArrayList<>();
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EscalaMgPs obPersistencia = new EscalaMgPs(obFabricaConexao.getConexao());

			listaEscalaMg = obPersistencia.listaCenopsParaMandadoGratuito();

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaEscalaMg;
	}



	@Override
	public String Verificar(EscalaMgDt dados) {
		// TODO Auto-generated method stub
		return null;
	}
}
