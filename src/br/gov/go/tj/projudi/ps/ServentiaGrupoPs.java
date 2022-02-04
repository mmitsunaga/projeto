package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaGrupoPs extends ServentiaGrupoPsGen {

	private static final long serialVersionUID = -3763589528415807081L;

	public ServentiaGrupoPs(Connection conexao){
		Conexao = conexao;
	}

	public String consultarServentiaGrupoIdCargo(String id_Cargo, String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		int qtdeColunas = 1;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_SERV_GRUPO as id, SERV_GRUPO as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO";
		stSqlFrom += " WHERE SERV_GRUPO LIKE ? AND ID_SERV_CARGO = ?";
		stSqlOrder = " ORDER BY SERV_GRUPO ";
		ps.adicionarString( descricao +"%");
		ps.adicionarLong(id_Cargo);

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}

	
	public String consultarDescricaoOrdenacaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) // ordenação do PROJUDI
			ordenacao = " SERV_GRUPO ";
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_GRUPO as id, SERV_GRUPO as descricao1, SERV as descricao2";
		stSqlFrom = " FROM PROJUDI.VIEW_SERV_GRUPO";
		stSqlFrom += " WHERE SERV_GRUPO LIKE ?";
		stSqlOrder = " ORDER BY " + ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql= "SELECT Count(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarDescricaoServentiaGrupoDoisParametrosBuscaJSON(String tempNomeBusca, String tempNomeBusca2, int grupoCodigo, String posicao) throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT  SG.ID_SERV_GRUPO as id,  SG.SERV_GRUPO as descricao1,  SG.SERV as descricao2";
		stSqlFrom = " FROM PROJUDI.VIEW_SERV_GRUPO  SG";
		stSqlFrom += " INNER JOIN VIEW_SERV S ON S.ID_SERV = SG.ID_SERV";
		stSqlFrom += " WHERE  SG.SERV_GRUPO LIKE ?"; ps.adicionarString("%"+tempNomeBusca+"%"); 
		
		if (tempNomeBusca2 != null && tempNomeBusca2.length()>0){
			stSqlFrom += " AND  SG.SERV LIKE ?"; ps.adicionarString("%"+tempNomeBusca2+"%");
		}
		
		if (grupoCodigo == GrupoDt.CADASTRADOR_MASTER){
			stSqlFrom += " AND S.SERV_SUBTIPO_CODIGO = ? "; ps.adicionarLong(ServentiaSubtipoDt.GABINETE_FLUXO_UPJ); 
		} else {
			stSqlFrom += " AND S.SERV_SUBTIPO_CODIGO <> ? "; ps.adicionarLong(ServentiaSubtipoDt.GABINETE_FLUXO_UPJ); 
		}
		
		stSqlOrder = " ORDER BY  SG.SERV,  SG.SERV_GRUPO";

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarDescricaoJSON(String funcao, String serventia, int grupoCodigo, String posicao) throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT SG.ID_SERV_GRUPO as id,  SG.SERV as descricao1, SG.SERV_GRUPO as descricao2 ";
		stSqlFrom = " FROM PROJUDI.VIEW_SERV_GRUPO  SG";
		stSqlFrom += " INNER JOIN VIEW_SERV S ON S.ID_SERV = SG.ID_SERV";
		stSqlFrom += " WHERE  SG.SERV_GRUPO LIKE ?"; ps.adicionarString("%"+funcao+"%"); 
		
		if (serventia != null && !serventia.isEmpty()){
			stSqlFrom += " AND  SG.SERV LIKE ?"; ps.adicionarString("%"+serventia+"%"); 
		}
		
		if (grupoCodigo == GrupoDt.CADASTRADOR_MASTER){
			stSqlFrom += " AND S.SERV_SUBTIPO_CODIGO = ? "; ps.adicionarLong(ServentiaSubtipoDt.GABINETE_FLUXO_UPJ); 
		}
//		} else {
//			stSqlFrom += " AND S.SERV_SUBTIPO_CODIGO <> ? "; ps.adicionarLong(ServentiaSubtipoDt.GABINETE_FLUXO_UPJ); 
//		}
		
		stSqlOrder = " ORDER BY  SG.SERV,  SG.SERV_GRUPO ";

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
public String consultarDescricaoServentiaGrupoProximaFuncaoJSON(String descricao, String posicao, String id_serventiagrupo, String id_serventia) throws Exception {
		
		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_GRUPO as id, SERV_GRUPO as descricao1 FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";
		ps.adicionarString("%"+descricao+"%"); 
		if (id_serventiagrupo != null && id_serventiagrupo.length()>0){
			stSql+=" AND ID_SERV_GRUPO <> ? ";
			ps.adicionarLong(id_serventiagrupo);
		}
		
		if (id_serventia != null && id_serventia.length()>0){
			stSql+=" AND ID_SERV = ? ";
			ps.adicionarLong(id_serventia);
		}
		
		stSql+= " ORDER BY SERV_GRUPO " ;
		

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ? ";
			
			if (id_serventiagrupo != null && id_serventiagrupo.length()>0){
				stSql+=" AND ID_SERV_GRUPO <> ? ";
			}
			
			if (id_serventia != null && id_serventia.length()>0){
				stSql+=" AND ID_SERV <> ? ";
			}
			
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

	public ServentiaGrupoDt consultarSeventiaGrupoDistribuidorId(String id_ServentiaCargo )  throws Exception {
	
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaGrupoDt Dados=null;
	
		stSql= " select ";
		stSql+= "  sg.* ";
		stSql+= " from ";
		stSql+= "  PROJUDI.VIEW_SERV_GRUPO sg ";
		stSql+= "  inner join PROJUDI.SERV_CARGO_SERV_GRUPO scsg on sg.ID_SERV_GRUPO = scsg.ID_SERV_GRUPO ";
		stSql+= "  inner join PROJUDI.SERV_CARGO sc on sc.ID_SERV_CARGO = scsg.ID_SERV_CARGO ";
		stSql+= "  inner join PROJUDI.CARGO_TIPO ct on ct.ID_CARGO_TIPO = sc.ID_CARGO_TIPO ";
		stSql+= " where ";
		stSql+= "  ct.CARGO_TIPO_CODIGO = ? and  sc.ID_SERV_CARGO = ? "; 	
		ps.adicionarLong(CargoTipoDt.DISTRIBUIDOR_GABINETE) ;
		ps.adicionarLong(id_ServentiaCargo);
		
		try{
	
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaGrupoDt();
				associarDt(Dados, rs1);
			}
	
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	
	public ServentiaGrupoDt consultarSeventiaGrupoMagistradoId(String id_ServentiaCargo )  throws Exception {
		
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaGrupoDt Dados=null;
	
		stSql= " select ";
		stSql+= "  sg.* ";
		stSql+= " from ";
		stSql+= "  PROJUDI.VIEW_SERV_GRUPO sg ";
		stSql+= "  inner join PROJUDI.SERV_CARGO_SERV_GRUPO scsg on sg.ID_SERV_GRUPO = scsg.ID_SERV_GRUPO ";
		stSql+= "  inner join PROJUDI.SERV_CARGO sc on sc.ID_SERV_CARGO = scsg.ID_SERV_CARGO ";
		stSql+= "  inner join PROJUDI.CARGO_TIPO ct on ct.ID_CARGO_TIPO = sc.ID_CARGO_TIPO ";
		stSql+= " where ";
		stSql+= "  ct.CARGO_TIPO_CODIGO = ? and  sc.ID_SERV_CARGO = ? "; 	
		ps.adicionarLong(CargoTipoDt.JUIZ_UPJ) ;
		ps.adicionarLong(id_ServentiaCargo);
		
		try{
	
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaGrupoDt();
				associarDt(Dados, rs1);
			}
	
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

}
