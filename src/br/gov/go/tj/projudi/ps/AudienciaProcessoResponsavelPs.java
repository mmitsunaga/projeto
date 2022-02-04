package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaProcessoResponsavelPs extends AudienciaProcessoResponsavelPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -8603213743268886131L;

    public AudienciaProcessoResponsavelPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método inserir
	 */
	public void inserir(AudienciaProcessoResponsavelDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "INSERT INTO PROJUDI.AUDI_PROC_RESP (";
		if (!(dados.getId_AudienciaProcesso().length() == 0))
			Sql += "ID_AUDI_PROC ";
		if (!(dados.getId_UsuarioServentia().length() == 0))
			Sql += ",ID_USU_SERV ";
		if ((dados.getId_CargoTipo().length() > 0) || (dados.getCargoTipoCodigo().length() > 0))
			Sql += ",ID_CARGO_TIPO ";
		Sql += ") ";
		Sql += " Values (?";
		if (!(dados.getId_AudienciaProcesso().length() == 0)) {
			//Sql += " ?";
			String str = dados.getId_AudienciaProcesso();
			if (str.isEmpty()) {
				ps.adicionarLongNull();
			} else {
				ps.adicionarLong(str);
			}
		}
		if (!(dados.getId_UsuarioServentia().length() == 0)) {
			Sql += ", ?";
			String str = dados.getId_UsuarioServentia();
			if (str.isEmpty()) {
				ps.adicionarLongNull();
			} else {
				ps.adicionarLong(str);
			}
		}
		if (!(dados.getId_CargoTipo().length() == 0)) {
			Sql += ", ?";
			String str = dados.getId_CargoTipo();
			if (str.isEmpty()) {
				ps.adicionarLongNull();
			} else {
				ps.adicionarLong(str);
			}
		} else if (dados.getCargoTipoCodigo().length() > 0) {
			Sql += ", (SELECT ID_CARGO_TIPO FROM PROJUDI.CARGO_TIPO WHERE CARGO_TIPO_CODIGO = ?)";
			String str = dados.getCargoTipoCodigo();
			if (str.isEmpty()) {
				ps.adicionarLongNull();
			} else {
				ps.adicionarLong(str);
			}
		}

		Sql += ")";
		Sql = Sql.replace("(,", "(");

		dados.setId(executarInsert(Sql, "ID_AUDI_PROC_RESPONSAVEL", ps));
		
	}

	/**
	 * Consulta os responsáveis por uma "AudienciaProcesso".
	 * Retorna todas os usuários que realizaram uma audiência de processo.
	 * 
	 * @author msapaula
	 */

	public List consultarResponsaveisAudienciaProcesso(String id_AudienciaProcesso) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT ID_AUDI_PROC_RESPONSAVEL, ID_USU_SERV, NOME, ID_CARGO_TIPO, CARGO_TIPO FROM PROJUDI.VIEW_AUDI_PROC_RESP";
		sql += " WHERE ID_AUDI_PROC = ?";
		ps.adicionarLong(id_AudienciaProcesso);
		sql += " ORDER BY CARGO_TIPO ";
		
		
		try{
			rs1 = consultar(sql,ps);

			while (rs1.next()) {
				AudienciaProcessoResponsavelDt obTemp = new AudienciaProcessoResponsavelDt();
				obTemp.setId(rs1.getString("ID_AUDI_PROC_RESPONSAVEL"));
				obTemp.setId_CargoTipo(rs1.getString("ID_CARGO_TIPO"));
				obTemp.setCargoTipo(rs1.getString("CARGO_TIPO"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setNome(rs1.getString("NOME"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}

	/**
	 * Retorna o Id_UsuarioServentia de um tipo de Responsável pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo, identificação de processo
	 * @param id_serventia, serventia para a qual quer encontrar o juiz responsável pelo processo
	 * @param cargoTipoCodigo, tipo do responsável a ser retornado
	 * @author msapaula
	 */
	public String consultarJuizResponsavelAudiencia(String id_AudienciaProcesso) throws Exception {
		String sql;
		String stRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = " SELECT us.ID_USU_SERV FROM PROJUDI.AUDI_PROC ap ";
		sql += " LEFT JOIN PROJUDI.SERV_CARGO sc on((ap.ID_SERV_CARGO = sc.ID_SERV_CARGO)) ";
		sql += " LEFT JOIN PROJUDI.CARGO_TIPO ct on((sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO)) ";
		sql += " LEFT JOIN PROJUDI.USU_SERV_GRUPO usg on((sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO)) ";
		sql += " LEFT JOIN PROJUDI.USU_SERV us on((usg.ID_USU_SERV = us.ID_USU_SERV)) ";
		sql += " LEFT JOIN PROJUDI.USU u on((us.ID_USU = u.ID_USU)) ";
		sql += " WHERE  ap.ID_AUDI_PROC =  ?";
		ps.adicionarLong(id_AudienciaProcesso); 
		sql += " AND ( ct.CARGO_TIPO_CODIGO = ?";
		ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU); 
		sql += " OR  ct.CARGO_TIPO_CODIGO =  ?)";
		ps.adicionarLong(CargoTipoDt.JUIZ_EXECUCAO) ;
		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) stRetorno = rs1.getString("ID_USU_SERV");
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}
}
