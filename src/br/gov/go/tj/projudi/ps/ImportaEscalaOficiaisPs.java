package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.spi.Resolver;

import br.gov.go.tj.projudi.dt.ContaUsuarioDt;
import br.gov.go.tj.projudi.dt.ImportaDadosSPGDt;
import br.gov.go.tj.projudi.dt.ImportaEscalaOficiaisDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ImportaEscalaOficiaisPs extends Persistencia {

	private static final long serialVersionUID = -2548786806822055395L;

	public ImportaEscalaOficiaisPs(Connection conexao) {
		Conexao = conexao;
	}

	public List consultaRegiaoGenerica(int idComarca) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_regiao from projudi.regiao where regiao = 'Região Genérica' and id_comarca = ?");

		ps.adicionarLong(idComarca);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdRegiao(rs.getInt("id_regiao"));
		} else {
			objDt.setIdRegiao(0);
		}
		lista.add(objDt);
		return lista;
	}

	public int cadastraRegiaoGenerica(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = "INSERT INTO projudi.regiao (regiao_codigo, regiao, id_comarca, codigo_temp) VALUES (0, 'Região Genérica', ?, 90990)";

		ps.adicionarLong(objDt.getIdComarca());

		int id = Integer.parseInt(executarInsert(sqlS, "ID_REGIAO", ps));

		return id;
	}

	public List consultaZonaBairroRegiao(int idBairro) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append(
				"select id_zona_bairro_regiao, codigo_temp from projudi.zona_bairro_regiao" + " where id_bairro = ?");

		ps.adicionarLong(idBairro);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdZonaBairroRegiao(rs.getInt("id_zona_bairro_regiao"));
			objDt.setCodigoTemp(rs.getString("codigo_temp"));
		} else {
			objDt.setIdZonaBairroRegiao(0);
		}
		lista.add(objDt);
		return lista;
	}

	public List consultaZona(int idComarca) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_zona from projudi.zona where id_comarca = ? AND zona like 'URBAN%'");

		ps.adicionarLong(idComarca);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdZona(rs.getInt("id_zona"));
		} else {
			objDt.setIdZona(0);
		}
		lista.add(objDt);
		return lista;
	}

	public void cadastraZonaBairroRegiao(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = "INSERT INTO projudi.zona_bairro_regiao (id_zona, id_bairro, id_regiao, codigo_temp)"
				+ " values (?, ?, ?, 90990)";

		ps.adicionarLong(objDt.getIdZona());
		ps.adicionarLong(objDt.getIdBairro());
		ps.adicionarLong(objDt.getIdRegiao());

		objDt.setId(executarInsert(sqlS, "ID_ZONA_BAIRRO_REGIAO", ps));

	}

	public void alteraZonaBairroRegiao(int idZonaBairroRegiao, int idRegiao) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = " UPDATE projudi.zona_bairro_regiao SET id_regiao = ?, codigo_temp = 90991 WHERE id_zona_bairro_regiao = ?";
		ps.adicionarLong(idRegiao);
		ps.adicionarLong(idZonaBairroRegiao);
		executarUpdateDelete(sql, ps);
	}

	public List consultaRegiao(String nomeRegiao, int idComarca) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();
		sql.append(
				"select r.id_regiao as idRegiao, r.regiao as nomeRegiao, c.comarca as nomeComarca from projudi.regiao r"
						+ " inner join projudi.comarca c on c.id_comarca = r.id_comarca"
						+ " where r.regiao = ? and r.id_comarca = ?");

		ps.adicionarString(nomeRegiao);
		ps.adicionarLong(idComarca);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setNomeRegiao(rs.getString("nomeRegiao"));
			objDt.setNomeComarca(rs.getString("nomeComarca"));
			objDt.setIdRegiao(rs.getInt("idRegiao"));
		} else {
			objDt.setIdRegiao(0);
		}
		lista.add(objDt);
		return lista;
	}

	public int importaRegiao(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = "INSERT INTO projudi.regiao (regiao_codigo, regiao, id_comarca, codigo_temp) VALUES (0, ?, ?, 90990)";

		ps.adicionarString(objDt.getNomeRegiao());
		ps.adicionarLong(objDt.getIdComarca());

		int id = Integer.parseInt(executarInsert(sqlS, "ID_REGIAO", ps));

		return id;

	}

	public void alteraRegiao(int idRegiao) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = " UPDATE projudi.regiao SET regiao_codigo = ? WHERE id_regiao = ? AND codigo_temp = 90990";
		ps.adicionarLong(idRegiao);
		ps.adicionarLong(idRegiao);
		executarUpdateDelete(sql, ps);
	}

	public List consultaServentiaCargo(ImportaEscalaOficiaisDt ObjDt) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_serv_cargo from projudi.serv_cargo where id_serv = ? and id_usu_serv_grupo = ?");

		ps.adicionarLong(ObjDt.getIdServentia());

		ps.adicionarLong(ObjDt.getIdUsuServGrupo());

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdServentiaCargo(rs.getInt("id_serv_cargo"));
		} else {
			objDt.setIdServentiaCargo(0);
		}
		lista.add(objDt);
		return lista;
	}

	public void cadastraServentiaCargo(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = "INSERT INTO projudi.serv_cargo (id_serv, id_cargo_tipo, serv_cargo, id_usu_serv_grupo, quantidade_dist, codigo_temp,"
				+ " grupo_temp, serv_cargo_temp, prazo_agenda, id_serv_subtipo, data_inicial_substituicao,"
				+ " data_final_substituicao, probabilidade)"
				+ " values (?, ?, 'Oficial de Justiça', ?, 0, 0, 'Oficial de Justiça', 'Oficial de Justiça - Importado', 20, null, null, null, 0.8)";

		//// codigo_temp nao pode 90990 - campo usuado para saber se cargo esta ocupado
		//// ou nao
		ps.adicionarLong(objDt.getIdServentia());
		ps.adicionarLong(ImportaEscalaOficiaisDt.ID_CARGO_TIPO_PROD);
		ps.adicionarLong(objDt.getIdUsuServGrupo());
		objDt.setId(executarInsert(sqlS, "ID_SERV_CARGO", ps));
	}

	public List consultaUsuarioServentiaGrupo(ImportaEscalaOficiaisDt ObjDt) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_usu_serv_grupo from projudi.usu_serv_grupo where id_usu_serv = ? and id_grupo = ?");

		ps.adicionarLong(ObjDt.getIdUsuServ());

		ps.adicionarLong(ImportaEscalaOficiaisDt.ID_GRUPO_PROD);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdUsuServGrupo(rs.getInt("id_usu_serv_grupo"));
		} else {
			objDt.setIdUsuServGrupo(0);
		}
		lista.add(objDt);
		return lista;
	}

	public int cadastraUsuarioServentiaGrupo(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = "INSERT INTO projudi.usu_serv_grupo (id_usu_serv, id_grupo, ativo, codigo_temp, codigo_grupo_temp)"
				+ " values (?, ?, 1, 90990, null)";

		ps.adicionarLong(objDt.getIdUsuServ());

		ps.adicionarLong(ImportaEscalaOficiaisDt.ID_GRUPO_PROD);

		return Integer.parseInt(executarInsert(sqlS, "ID_USU_SERV_GRUPO", ps));

	}

	public List consultaUsuarioServentia(int idUsuario, int idServentia) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_usu_serv from projudi.usu_serv where id_usu = ? and id_serv = ?");

		ps.adicionarLong(idUsuario);
		ps.adicionarLong(idServentia);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdUsuServ(rs.getInt("id_usu_serv"));
		} else {
			objDt.setIdUsuServ(0);
		}
		lista.add(objDt);
		return lista;
	}

	public int cadastraUsuarioServentia(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = "INSERT INTO projudi.usu_serv (id_usu, id_serv, ativo, id_usu_serv_chefe, codigo_temp, codigo_grupo_temp, data_fim_temp,"
				+ " pode_guardar_assinar_usuchefe)" + " values (?, ?, 1, null, 90990, null, null,0)";

		ps.adicionarLong(objDt.getIdUsuario());
		ps.adicionarLong(objDt.getIdServentia());

		return Integer.parseInt(executarInsert(sqlS, "ID_USU_SERV", ps));

	}

	public List consultaServentia(String nomeServentia) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_serv, serv from projudi.serv where serv = ?");

		ps.adicionarString(nomeServentia);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdServentia(rs.getInt("id_serv"));
			objDt.setNomeServentia("serv");
		} else {
			objDt.setIdServentia(0);
			objDt.setNomeServentia("");
		}
		lista.add(objDt);
		return lista;
	}

	public int cadastraServentia(ImportaEscalaOficiaisDt objDt) throws Exception { // codigo temp = 0 serventia ativa
																					// nao usei 90990

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = " INSERT INTO projudi.serv (serv_codigo, serv_codigo_externo, serv, id_serv_tipo, id_serv_subtipo, id_audi_tipo,"
				+ " on_line, id_area, id_comarca, id_area_dist, id_estado_representacao, id_endereco, telefone, codigo_temp,"
				+ " tabela_origem_temp, serv_codigo_antigo_temp, data_cadastro, data_implantacao, concluso_direto,"
				+ " quantidade_dist,  id_cnj_serv, id_area_dist_2, email)"
				+ " values (?, ?, ?, ?, ?, null, null, ?, ?, null, ?, ?, null, 0, null,"
				+ " null, ?, ?, 0, 0, null,  null, null)";

		ps.adicionarString(objDt.getCodigoServentia());
		ps.adicionarString(objDt.getCodigoServentia());
		ps.adicionarString(objDt.getNomeServentia());
		ps.adicionarLong(ImportaEscalaOficiaisDt.ID_SERV_TIPO_PROD);
		ps.adicionarLong(ImportaEscalaOficiaisDt.ID_SERV_SUBTIPO_PROD);
		ps.adicionarLong(ImportaEscalaOficiaisDt.ID_AREA_PROD);
		ps.adicionarLong(objDt.getIdComarca());

		ps.adicionarLong(1); // estado goias

		ps.adicionarLong(objDt.getIdEndereco());

		ps.adicionarDateTime(new Date());
		ps.adicionarDateTime(new Date());

		return Integer.parseInt(executarInsert(sqlS, "ID_SERV", ps));

	}

	public List consultaBanco(String codigo) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();
		sql.append("select id_banco, banco  from projudi.banco where banco_codigo = ?");
		ps.adicionarString(codigo);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			objDt.setIdBanco(rs.getInt("id_banco"));
			objDt.setNomeBanco(rs.getString("banco"));
		} else {
			objDt.setIdBanco(0);
			objDt.setNomeBanco("");
		}
		lista.add(objDt);
		return lista;
	}

	public void importaBancos(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps;

		ps = new PreparedStatementTJGO();

		String sqlS = "";

		sqlS = "INSERT INTO projudi.banco (banco_codigo, banco, codigo_temp) VALUES (?, ?,90990)";

		ps.adicionarLong(objDt.getCodigoBanco());

		ps.adicionarString(objDt.getNomeBanco());

		objDt.setId(executarInsert(sqlS, "ID_BANCO", ps));

	}

	public List consultaUsuario(String cpf) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_usu, nome from projudi.usu where cpf = ?");
		ps.adicionarString(cpf);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			objDt.setIdUsuario(rs.getInt("id_usu"));
			objDt.setNomeUsuario(rs.getString("nome"));
		} else {
			objDt.setIdUsuario(0);
			objDt.setNomeUsuario("");
		}
		lista.add(objDt);
		return lista;
	}

	public void importaUsuarios(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sqlS = "";

		sqlS = "INSERT INTO projudi.usu (USU, SENHA, MATRICULA_TJGO, NUMERO_CONCILIADOR, DATA_CADASTRO, ATIVO,"
				+ " DATA_ATIVO, DATA_EXPIRACAO, NOME, SEXO, DATA_NASCIMENTO, ID_NATURALIDADE, ID_ENDERECO, RG, "
				+ " ID_RG_ORGAO_EXP, RG_DATA_EXPEDICAO, CPF, TITULO_ELEITOR, TITULO_ELEITOR_ZONA, TITULO_ELEITOR_SECAO,"
				+ "	CTPS, CTPS_SERIE, ID_CTPS_UF, PIS, EMAIL, TELEFONE, CELULAR, CODIGO_TEMP, CODIGO_GRUPO_TEMP, ID_NATURALIDADE_ANTIGO_TEMP)"
				+ "	VALUES (?, ?, 0, 0, ?, 1, ?,  null, ?,"
				+ "	null, '01/01/0001', null, ?, null, null, null, ?, null, 0, 0, 0, 0, null, 0, null, 0, 0, 0, 0, 0)";

		ps.adicionarString(objDt.getCpfUsuario());

		ps.adicionarString(Funcoes.SenhaMd5("12345"));

		ps.adicionarDateTime(new Date());

		ps.adicionarDateTime(new Date());

		ps.adicionarString(objDt.getNomeUsuario());

		ps.adicionarLong(objDt.getIdEndereco());

		ps.adicionarString(objDt.getCpfUsuario());

		objDt.setId(executarInsert(sqlS, "ID_USU", ps));
	}

	public int consultaContasBancarias(String agencia, String conta, String conta_dv, int idUsuario, int idBanco)
			throws Exception {
		int id = 0;

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT cu.id_conta_usu FROM projudi.conta_usu cu "
				+ " INNER JOIN projudi.agencia a ON a.id_agencia = cu.id_agencia"
				+ " INNER JOIN projudi.banco b ON b.id_banco = a.id_banco" + " WHERE cu.id_usu = ? AND "
				+ " b.id_banco = ? AND a.agencia_codigo = ? AND cu.conta_usu = ? AND cu.conta_usu_dv = ?");
		ps.adicionarLong(idUsuario);
		ps.adicionarLong(idBanco);
		ps.adicionarString(agencia);
		ps.adicionarString(conta);
		ps.adicionarString(conta_dv);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			id = (rs.getInt("id_conta_usu"));
		}

		return id;
	}

	public int consultaContasBancarias(int idUsuario) throws Exception {
		int id = 0;

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("select id_conta_usu from projudi.conta_usu where id_usu = ?");
		ps.adicionarLong(idUsuario);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			id = (rs.getInt("id_conta_usu"));
		}

		return id;
	}

	public int consultaAgencia(ImportaEscalaOficiaisDt objDt) throws Exception {
		int id = 0;

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT id_agencia FROM projudi.agencia WHERE agencia_codigo = ? AND agencia = ? AND id_banco = ?");
		ps.adicionarLong(objDt.getAgencia());
		ps.adicionarString(objDt.getNomeAgencia());
		ps.adicionarLong(objDt.getIdBanco());
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			id = (rs.getInt("id_agencia"));
		}

		return id;
	}

	public int importaAgencia(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "";

		sql = "INSERT INTO projudi.agencia (agencia_codigo, agencia, id_banco, codigo_temp)"
				+ " VALUES (?, ?, ?, 90990)";
		ps.adicionarLong(objDt.getAgencia());
		ps.adicionarString(objDt.getNomeAgencia());
		ps.adicionarLong(objDt.getIdBanco());

		int id = Integer.parseInt(executarInsert(sql, "ID_AGENCIA", ps));

		return id;

	}

	public void importaContaBancaria(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "";

		sql = "INSERT INTO projudi.conta_usu (conta_usu, id_usu, id_agencia, ativa, codigo_temp, conta_usu_dv,"
				+ " conta_usu_operacao)" + " VALUES (?, ?, ?, ?, 90990, ?, ?)";
		ps.adicionarLong(objDt.getConta());
		ps.adicionarLong(objDt.getIdUsuario());
		ps.adicionarLong(objDt.getIdAgencia());
		ps.adicionarLong(ContaUsuarioDt.ATIVO); ////// LEMBRAR NA IMPORTACAO QUAL VEM ATIVA SPG
		ps.adicionarString(objDt.getContaDv());
		ps.adicionarLong(objDt.getOperacao());
		objDt.setId(executarInsert(sql, "ID_CONTA_USU", ps));
	}

	public void alteraContaBancaria(int idUsuario) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = " UPDATE projudi.conta_usu SET ativa = 0 WHERE id_usu = ?";
		ps.adicionarLong(idUsuario);
		executarUpdateDelete(sql, ps);
	}

	public int consultaEscala(int idMandTipo, int idEscalaTipo, int idRegiao, int idServ) throws Exception {
		int id = 0;
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append(
				"SELECT id_esc FROM projudi.esc WHERE id_mand_tipo = ? and id_escala_tipo = ? and id_regiao = ? and id_serv = ?");
		ps.adicionarLong(idMandTipo);
		ps.adicionarLong(idEscalaTipo);
		ps.adicionarLong(idRegiao);
		ps.adicionarLong(idServ);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			id = (rs.getInt("id_esc"));
		}
		return id;
	}

	public int importaEscala(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "";

		sql = "INSERT INTO projudi.esc (esc, id_mand_tipo, id_escala_tipo, id_regiao, id_serv, quantidade_mand,"
				+ " codigo_temp, ativo, tipo_especial, assistencia)" + " VALUES (?, ?, ?, ?, ?, ?, 90990, 1, 0, 0)";

		ps.adicionarString(objDt.getNomeEscala());
		ps.adicionarLong(objDt.getIdTipoMandado());
		ps.adicionarLong(objDt.getIdTipoEscala());
		ps.adicionarLong(objDt.getIdRegiao());
		ps.adicionarLong(objDt.getIdServentia());
		ps.adicionarLong(objDt.getQuantMandados());
		return Integer.parseInt(executarInsert(sql, "ID_ESC", ps));

	}

	public int consultaServCargoEscala(int idServCargo, int idEscala) throws Exception {
		int id = 0;
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT id_serv_cargo_esc FROM projudi.serv_cargo_esc WHERE id_serv_cargo = ? and id_esc = ? ");
		ps.adicionarLong(idServCargo);
		ps.adicionarLong(idEscala);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			id = (rs.getInt("id_serv_cargo_esc"));
		}
		return id;
	}

	public void importaServCargoEscala(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "";

		sql = "INSERT INTO projudi.serv_cargo_esc (id_serv_cargo, id_esc, codigo_temp, id_serv_cargo_esc_status, data_vinculacao)"
				+ " VALUES (?, ?, 90990, 4, ?)";

		ps.adicionarLong(objDt.getIdServentiaCargo());
		ps.adicionarLong(objDt.getIdEscala());
		ps.adicionarDateTime(new Date());
		objDt.setId(executarInsert(sql, "ID_SERV_CARGO_ESC", ps));
	}

	public String consultaArea(int idArea) throws Exception {
		String nome = "";
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append("SELECT area FROM projudi.area WHERE id_area = ?");
		ps.adicionarLong(idArea);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			nome = (rs.getString("area"));
		}

		return nome;
	}

	public List consultaBairro(String idBairro) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append(
				"select b.id_bairro as idBairro, b.bairro as nomeBairro, c.cidade as nomeCidade from projudi.bairro b"
						+ " inner join projudi.cidade c on c.id_cidade = b.id_cidade" + " where b.id_bairro = ?");

		ps.adicionarString(idBairro);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setNomeBairro(rs.getString("nomeBairro"));
			objDt.setNomeCidade(rs.getString("nomeCidade"));
			objDt.setIdBairro(rs.getInt("idBairro"));
		} else {
			objDt.setIdBairro(0);
		}
		lista.add(objDt);
		return lista;
	}

	public List consultaTipoMandado(String codigo) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_mand_tipo, mand_tipo  from projudi.mand_tipo where mand_tipo_codigo = ?");

		ps.adicionarLong(codigo);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdTipoMandado(rs.getInt("id_mand_tipo"));
			objDt.setNomeTipoMandado(rs.getString("mand_tipo"));
		} else {
			objDt.setIdTipoMandado(0);
			objDt.setNomeTipoMandado("");
		}
		lista.add(objDt);
		return lista;
	}

	public List consultaTipoCargo(ImportaEscalaOficiaisDt ObjDt) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_cargo_tipo, cargo_tipo from projudi.cargo_tipo where cargo_tipo = ?");

		ps.adicionarString(ObjDt.getCargoTipo());

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdCargoTipo(rs.getInt("id_cargo_tipo"));
			objDt.setNomeCargoTipo(rs.getString("cargo_tipo"));
		} else {
			objDt.setIdCargoTipo(0);
			objDt.setNomeCargoTipo("");
		}
		lista.add(objDt);
		return lista;
	}

	public List consultaGrupo(ImportaEscalaOficiaisDt ObjDt) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();

		sql.append("select id_grupo, grupo_codigo, grupo from projudi.grupo where grupo = ?");

		ps.adicionarString(ObjDt.getNomeGrupo());

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			objDt.setIdGrupo(rs.getInt("id_grupo"));
			objDt.setCodigoGrupo(rs.getInt("grupo_codigo"));
			objDt.setNomeGrupo(rs.getString("grupo"));
		} else {
			objDt.setIdGrupo(0);
			objDt.setCodigoGrupo(0);
			objDt.setNomeGrupo("");
		}
		lista.add(objDt);
		return lista;
	}

	public List consultaComarca(String codigo) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ImportaEscalaOficiaisDt objDt = new ImportaEscalaOficiaisDt();
		sql.append("select id_comarca, comarca from projudi.comarca where comarca_codigo = ?");
		ps.adicionarString(codigo);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			objDt.setIdComarca(rs.getInt("id_comarca"));
			objDt.setNomeComarca(rs.getString("comarca"));
		} else {
			objDt.setIdComarca(0);
			objDt.setNomeComarca("");
		}
		lista.add(objDt);
		return lista;
	}

	//
	// metodos controle importacao guia saldo.
	//

	//
	// consulta para ver se ja existe no projudi guia de saldo importada do spg.
	//

	public String  consultaGuiaSaldo(String numeroGuiaCompleto) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		String idGuiaEmis = "";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT id_guia_emis FROM projudi.guia_emis WHERE numero_guia_completo = ?" + " AND guia_emis = ?");

		ps.adicionarLong(numeroGuiaCompleto);
		ps.adicionarString("Guia Saldo");

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			idGuiaEmis = rs.getString("id_guia_emis");
		} else {
			idGuiaEmis = "";
		}

		return idGuiaEmis;
	}

	public String consultaGuia(String numeroGuiaCompleto) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		String idGuiaEmis = "";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT id_guia_emis FROM projudi.guia_emis WHERE numero_guia_completo = ?");

		ps.adicionarString(numeroGuiaCompleto);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			idGuiaEmis = rs.getString("id_guia_emis");
		} else {
			idGuiaEmis = "";
		}
		return idGuiaEmis;
	}

	//
	// consulta para saber se o processo da guia de saldo spg existe no projudi
	//

	public int consultaProcesso(String processoNumero, String digitoVerificador, String anoProcesso) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;

		int idProcNumero = 0;

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT id_proc FROM projudi.proc WHERE proc_numero = ? AND digito_verificador = ? AND ANO = ?");

		ps.adicionarLong(processoNumero);
		ps.adicionarLong(digitoVerificador);
		ps.adicionarLong(anoProcesso);

		rs = consultar(sql.toString(), ps);

		if (rs.next()) {
			idProcNumero = rs.getInt("id_proc");
		} else {
			idProcNumero = 0;
		}

		return idProcNumero;
	}

	//
	// grava no projudi a guia de saldo vinda do spg
	//

	public void importaGuiaSaldo(ImportaEscalaOficiaisDt objDt) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "";

		sql = "INSERT INTO projudi.guia_emis (id_proc, valor_acao, data_recebimento, data_emis, numero_guia_completo,"
				+ " guia_saldo_status, id_usu, data_vencimento, guia_emis, guia_saldo_valor_atualizado) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?)";
		ps.adicionarLong(objDt.getIdProc());
		ps.adicionarDouble(objDt.getGuiaSaldoValor());
		ps.adicionarDateTime(new Date());
		ps.adicionarDateTime(new Date());
		ps.adicionarString(objDt.getNumeroGuiaCompleto());
		ps.adicionarString(objDt.getGuiaSaldoStatus());
		ps.adicionarString("1");
		ps.adicionarDateTime(new Date());
		ps.adicionarString("Guia Saldo");
		ps.adicionarDouble(objDt.getGuiaSaldoValor());
		objDt.setId(executarInsert(sql, "ID_GUIA_EMIS", ps));
	}

	//
	// altera guias viculadas a locomocao do processo para não usar mais as
	// locomocoes.
	//

	public void alteraStatusGuiaProjudi(String numeroGuiaCompleto, String numeroGuiaSaldo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = " UPDATE projudi.guia_emis SET guia_saldo_status = ? , guia_saldo_numero = ? WHERE numero_guia_completo = ?";   
		ps.adicionarLong(ImportaDadosSPGDt.GUIA_SALDO_STATUS_NAO_LIBERADA);
		ps.adicionarLong(numeroGuiaSaldo);
		ps.adicionarLong(numeroGuiaCompleto);
//////////////////////////////////////////////////////////////////////////////		executarUpdateDelete(sql, ps);
	}	
}
