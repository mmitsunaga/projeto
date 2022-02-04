package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.HistoricoLotacaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class HistoricoLotacaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -581527459520444271L;

	//---------------------------------------------------------
	public HistoricoLotacaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(HistoricoLotacaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psHistoricoLotacaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.HIS_LOTACAO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataInicio());  

			stVirgula=",";
		}
		if ((dados.getDataFim().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataFim());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_HIS_LOTACAO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(HistoricoLotacaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psHistoricoLotacaoalterar()");

		stSql= "UPDATE PROJUDI.HIS_LOTACAO SET  ";
		stSql+= "ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql+= ",DATA_INICIO = ?";		 ps.adicionarDate(dados.getDataInicio());  

		stSql+= ",DATA_FIM = ?";		 ps.adicionarDate(dados.getDataFim());  

		stSql += " WHERE ID_HIS_LOTACAO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psHistoricoLotacaoexcluir()");

		stSql= "DELETE FROM PROJUDI.HIS_LOTACAO";
		stSql += " WHERE ID_HIS_LOTACAO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public HistoricoLotacaoDt consultarId(String id_historicolotacao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		HistoricoLotacaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_HistoricoLotacao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_HIS_LOTACAO WHERE ID_HIS_LOTACAO = ?";		ps.adicionarLong(id_historicolotacao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_HistoricoLotacao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new HistoricoLotacaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( HistoricoLotacaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_HIS_LOTACAO"));
		Dados.setUsuarioServentia(rs.getString("USU_SERV"));
		Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
		Dados.setDataInicio( Funcoes.FormatarData(rs.getDateTime("DATA_INICIO")));
		Dados.setDataFim( Funcoes.FormatarData(rs.getDateTime("DATA_FIM")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoHistoricoLotacao()");

		stSql= "SELECT ID_HIS_LOTACAO, USU_SERV FROM PROJUDI.VIEW_HIS_LOTACAO WHERE USU_SERV LIKE ?";
		stSql+= " ORDER BY USU_SERV ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoHistoricoLotacao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				HistoricoLotacaoDt obTemp = new HistoricoLotacaoDt();
				obTemp.setId(rs1.getString("ID_HIS_LOTACAO"));
				obTemp.setUsuarioServentia(rs1.getString("USU_SERV"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_HIS_LOTACAO WHERE USU_SERV LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..HistoricoLotacaoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
