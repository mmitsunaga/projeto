package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.relatorios.ResultadoRelatorioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;

import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RelatorioDiversosPs extends Persistencia {

	private static final long serialVersionUID = -2960393308645568341L;
	
	private static String SEPARADOR_CSV = ";";

	public RelatorioDiversosPs(Connection conexao){
		Conexao = conexao;
	}
	
	public ProcessoAssuntoDt consultarID(String id_assunto) throws Exception {

		String stSql = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoAssuntoDt Dados = null;

		stSql = "SELECT * FROM PROJUDI.VIEW_ASSUNTO WHERE ID_ASSUNTO = ?";
		ps.adicionarLong(id_assunto);

		try{
			rs = consultar(stSql, ps);
			if (rs.next()) {
				Dados = new ProcessoAssuntoDt();
				Dados.setId_Assunto(rs.getString("ID_ASSUNTO"));
				Dados.setAssunto(rs.getString("ASSUNTO"));
			}
		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return Dados;
	}	

	public ResultadoRelatorioDt consultarRelatorioDeIntimacoesPendentesCoordenadorPromotoria(UsuarioNe usuarioSessao) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();	
		String nomeDoArquivo = "IntimacoesCoordenadorDePromotoria_" + Funcoes.getGereNomeArquivo("CSV");
		
		String sSql = "SELECT s.serv AS SERVENTIA,";
		sSql += "     (lpad(pro.proc_numero, 7, '0')";
		sSql += "     || '-'";
		sSql += "     || lpad(pro.digito_verificador, 2, '0')";
		sSql += "     || '.'";
		sSql += "     || lpad(pro.ano, 4, '0')";
		sSql += "     || '.'";
		sSql += "     || '8'";
		sSql += "     || '.'";
		sSql += "     || '09'";
		sSql += "     || '.'";
		sSql += "     || lpad(pro.forum_codigo, 4, '0')) AS PROCESSO,";
		sSql += "     pp.nome AS NOME_PARTE,";
		sSql += "     mt.movi_tipo AS TIPO_MOVIMENTACAO,";
		sSql += "     to_char(p.data_inicio, 'DD/MM/YYYY HH24:MI:SS') AS DATA_INICIO,";
		sSql += "     to_char(p.data_limite, 'DD/MM/YYYY HH24:MI:SS') AS DATA_LIMITE,";
		sSql += "     NVL(prot.id_cnj_classe, '') AS CODIGO_CNJ,";
		sSql += "     NVL(protcnj.cnj_classe, '') AS CLASSE_CNJ,";
		sSql += "     NVL(prot.proc_tipo, '') AS CLASSE_TJGO,";	
		sSql += "     pro.ID_PROC AS ID_PROCESSO,";
		sSql += "     s.id_serv AS ID_SERVENTIA,";
		sSql += "     pro.ID_PROC AS HASH_PROCESSO,";
		sSql += "     P.ID_PEND AS ID_PENDENCIA,";
		sSql += "     p.ID_PEND AS HASH_PENDENCIA";
		sSql += " FROM projudi.serv s";
		sSql += "     JOIN projudi.serv_cargo        sc ON s.id_serv = sc.id_serv";
		sSql += "     JOIN projudi.usu_serv_grupo    usg ON usg.id_usu_serv_grupo = sc.id_usu_serv_grupo";
		sSql += "     JOIN projudi.usu_serv          us ON usg.id_usu_serv = us.id_usu_serv";
		sSql += "     JOIN projudi.usu               u ON us.id_usu = u.id_usu";
		sSql += "     JOIN projudi.pend_resp         pr ON pr.id_serv_cargo = sc.id_serv_cargo";
		sSql += "                                  OR us.id_usu_serv = pr.id_usu_resp";
		sSql += "     JOIN projudi.pend              p ON pr.id_pend = p.id_pend";
		sSql += "     LEFT JOIN projudi.proc_parte   pp ON p.id_proc_parte = pp.id_proc_parte";
		sSql += "     JOIN projudi.pend_tipo         pt ON p.id_pend_tipo = pt.id_pend_tipo";
		sSql += "     JOIN projudi.proc              pro ON pro.id_proc = p.id_proc";
		sSql += "     JOIN projudi.proc_tipo         prot ON pro.id_proc_tipo = prot.id_proc_tipo";
		sSql += "     LEFT JOIN projudi.cnj_classe   protcnj ON prot.id_cnj_classe = protcnj.id_cnj_classe";
		sSql += "     JOIN projudi.movi              m ON m.id_movi = p.id_movi";
		sSql += "     JOIN projudi.movi_tipo         mt ON mt.id_movi_tipo = m.id_movi_tipo";
		sSql += "     JOIN projudi.pend_status       ps ON ps.id_pend_status = p.id_pend_status";
		sSql += " WHERE   pt.pend_tipo_codigo = ? "; ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sSql += " AND ( p.codigo_temp <> ? "; ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		sSql += "       OR p.codigo_temp IS NULL )";
		sSql += " AND p.data_fim IS NULL";
		sSql += " AND EXISTS (SELECT 1";
		sSql += "               FROM projudi.usu u2";
		sSql += "              INNER JOIN projudi.usu_serv          us2 ON us2.id_usu = u2.id_usu";
		sSql += "              INNER JOIN projudi.usu_serv_grupo    usg2 ON usg2.id_usu_serv = us2.id_usu_serv";
		sSql += "              INNER JOIN projudi.grupo             g2 ON g2.id_grupo = usg2.id_grupo";
		sSql += "             WHERE u2.ativo = ?"; ps.adicionarLong(UsuarioDt.ATIVO);
		sSql += "         AND us2.ativo = ?"; ps.adicionarLong(UsuarioServentiaDt.ATIVO);
		sSql += "         AND usg2.ativo = ?"; ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
		sSql += "         AND g2.grupo_codigo = ?"; ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		//sSql += "         --AND u2.usu = TRIM(to_char(01215516100, '00000000000'))";
		sSql += "         AND u2.id_usu = ?"; ps.adicionarLong(usuarioSessao.getUsuarioDt().getId());
		sSql += "         AND us2.id_serv = s.id_serv";
		sSql += " ) ORDER BY s.serv, p.data_inicio";
		
		return consultarRelatorioCSV(nomeDoArquivo, sSql, ps, true, usuarioSessao);
	}
	
	private ResultadoRelatorioDt consultarRelatorioCSV(String nomeRelatorio, 
			                                           String consultaSQL, 
			                                           PreparedStatementTJGO ps, 
			                                           boolean geraTitulo, 
			                                           UsuarioNe usuarioSessao) throws Exception {
		ResultSetTJGO rs = null;
		StringBuilder conteudoArquivo = new StringBuilder();
		
		try{
			rs = consultar(consultaSQL, ps);
			
			int quantidadeDeCampos = rs.getColumnCount();
			long quantidadeDeRegistros = 0;
			while (rs.next()) {
				if (geraTitulo) {
					for(int i = 1; i <= quantidadeDeCampos; i++) {
						conteudoArquivo.append(getValorCampoCSV(rs.getColumnName(i), i == quantidadeDeCampos));
					}
					geraTitulo = false;
				}
				for(int i = 1; i <= quantidadeDeCampos; i++) {
					if (rs.getColumnName(i).toLowerCase().contains("hash")) {
						conteudoArquivo.append(getValorCampoCSVHash(rs.getString(i), i == quantidadeDeCampos, usuarioSessao));
					} else {
						conteudoArquivo.append(getValorCampoCSV(rs.getString(i), i == quantidadeDeCampos));
					}					
				}
				quantidadeDeRegistros += 1;
			}
			
			return ResultadoRelatorioDt.CrieCSV(nomeRelatorio, conteudoArquivo.toString().getBytes("Cp1252"), quantidadeDeRegistros);
		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
	}
	
	private String getValorCampoCSVHash(String valor, boolean isUltimoCampo, UsuarioNe usuarioSessao) throws Exception {
		return usuarioSessao.getCodigoHashWebServiceMNI(valor) + (isUltimoCampo ? "\n" : SEPARADOR_CSV);
	}
	
	private String getValorCampoCSV(String valor, boolean isUltimoCampo) {
		return getValorCSV(valor) + (isUltimoCampo ? "\n" : SEPARADOR_CSV);
	}
	
	private String getValorCSV(String valor) {
		if (valor == null) return "";		
		return valor.replaceAll(SEPARADOR_CSV, "");
	}
}
