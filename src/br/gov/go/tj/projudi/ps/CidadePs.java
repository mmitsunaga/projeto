package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class CidadePs extends CidadePsGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4742994803280791960L;


	
	public CidadePs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Consulta geral de cidades
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		String Sql="";
		
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		Sql = "SELECT ID_CIDADE,CIDADE,UF, CODIGO_SPG FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
		Sql += " ORDER BY CIDADE";
		ps.adicionarString("%"+ descricao +"%");
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				CidadeDt obTemp = new CidadeDt();
				obTemp.setId(rs1.getString("ID_CIDADE"));
				obTemp.setCidade(rs1.getString("CIDADE"));
				obTemp.setUf(rs1.getString("UF"));
				obTemp.setCodigoSPG(rs1.getString("CODIGO_SPG"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
			rs2 = consultar(Sql, ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta geral de cidades
	 */
	public String consultarDescricaoJSON(String descricao,String uf, String posicao) throws Exception {
		String Sql="";
		String Sql1="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
				
		Sql = "SELECT ID_CIDADE as id , CIDADE as descricao1, UF AS descricao2  FROM projudi.VIEW_CIDADE" ;
		Sql += " WHERE CIDADE LIKE ?"; ps.adicionarString("%"+ descricao +"%");
		if (uf != null && uf.length()>0){
			Sql1= " AND UF = ?"; ps.adicionarString(uf);
		}
		Sql+=Sql1;
		
		Sql += " ORDER BY CIDADE";
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			
			ps = new PreparedStatementTJGO();

			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";  ps.adicionarString("%"+ descricao +"%");
			if (Sql1.length()>0) ps.adicionarString(uf);
			
			Sql +=Sql1;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	/**
	 * Consulta de cidades com filtros por estado
	 * @param descricao, filtro para descrição da cidade
	 * @param uf, filtro para estado
	 * @param posicao, parametro para paginação
	 */
	public List consultarDescricao(String descricao, String uf, String posicao) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_CIDADE,CIDADE, UF, CODIGO_SPG FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
		ps.adicionarString("%"+descricao+"%");
		Sql += " AND UF LIKE ?";
		ps.adicionarString("%"+uf+"%");
		Sql += " ORDER BY CIDADE";
		
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				CidadeDt obTemp = new CidadeDt();
				obTemp.setId(rs1.getString("ID_CIDADE"));
				obTemp.setCidade(rs1.getString("CIDADE"));
				obTemp.setUf(rs1.getString("UF"));
				obTemp.setCodigoSPG(rs1.getString("CODIGO_SPG"));
				liTemp.add(obTemp);
			}
			
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
			Sql += " AND UF LIKE ?";
			rs2 = consultar(Sql, ps);
			if(rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			
		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta os Id da Cidade
	 * @param descricao: descrição da cidade
	 * @param uf: sigla do estado
	 */
	public String consultarIdCidade(String descricaoCidade, String uf) throws Exception {
		String sql;
		String id = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT ID_CIDADE,CIDADE, UF FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
		sql += " AND UF LIKE ?";
		sql += " ORDER BY CIDADE";
		ps.adicionarString( descricaoCidade +"%");
		ps.adicionarString( uf +"%");
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				id = rs1.getString("ID_CIDADE");
			}
		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return id;
	}
	
	/**
	 * Método responsável em consultar uma cidade, baseado na descrição e Uf passados
	 */
	public CidadeDt buscaCidade(String descricaoCidade, String estadoCidade) throws Exception {
		CidadeDt cidadeDt = null;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try{

			sql = "SELECT * FROM projudi.VIEW_CIDADE c WHERE c.CIDADE = ?";
			ps.adicionarString(descricaoCidade);
			if (estadoCidade.length() > 0) {
				sql += " AND c.UF = ?";
				ps.adicionarString(estadoCidade);
			}
			
			 rs1 = consultar(sql, ps);
			if (rs1.next()) {
				cidadeDt = new CidadeDt();
				cidadeDt.setId(rs1.getString("ID_CIDADE"));
				cidadeDt.setCidade(rs1.getString("CIDADE"));
				cidadeDt.setUf(rs1.getString("UF"));
				cidadeDt.setCodigoSPG(rs1.getString("CODIGO_SPG"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return cidadeDt;
	}
	
	/**
	 * Método responsável por criar um relatório de listagem das cidades em PDF.
	 * @return
	 * @throws Exception
	 */
	public List relListagemCidades() throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		////System.out.println("..ps-relListagemCidade()");

		Sql= "SELECT CIDADE_CODIGO, CIDADE, UF, CODIGO_SPG FROM PROJUDI.VIEW_CIDADE";
		Sql+= " ORDER BY CIDADE";		
		try{
			////System.out.println("..ps-relListagemCidade  " + Sql);

			rs1 = consultarSemParametros(Sql);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CidadeDt obTemp = new CidadeDt();
				obTemp.setCidadeCodigo(rs1.getString("CIDADE_CODIGO"));
				obTemp.setCidade(rs1.getString("CIDADE"));
				obTemp.setUf(rs1.getString("UF"));
				obTemp.setCodigoSPG(rs1.getString("CODIGO_SPG"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..CidadePsGen.relListagemCidade() Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return liTemp; 
	}
	
	/**
	 * ATENÇÃO: NUNCA UTILIZAR!
	 * Fred!
	 */
	public List consultarCidadesGoias() throws Exception {
		List listaCidadeDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			String sql = "SELECT ID_CIDADE FROM projudi.VIEW_CIDADE "
						+"WHERE ID_ESTADO = ? AND ID_CIDADE NOT IN (?,?,?,?,?)";
			ps.adicionarLong(EstadoDt.ESTADOCODIGOGOIAS);
			ps.adicionarLong(4109);
			ps.adicionarLong(4266);
			ps.adicionarLong(3906);
			ps.adicionarLong(4019);
			ps.adicionarLong(3904);
			rs1 = consultar(sql,ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaCidadeDt == null )
						listaCidadeDt = new ArrayList();
					
					CidadeDt obTemp = new CidadeDt();
					obTemp.setId(rs1.getString("ID_CIDADE"));
					
					listaCidadeDt.add(obTemp);
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return listaCidadeDt;
	}
	
	public List consultarIdEstado(String idEstado) throws Exception {
		String Sql="";
		
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		Sql = "SELECT ID_CIDADE,CIDADE,UF FROM projudi.VIEW_CIDADE WHERE ID_ESTADO = ?";
		Sql += " ORDER BY CIDADE";
		ps.adicionarLong(idEstado);
		try{

			rs = consultar(Sql, ps);
			while (rs.next()) {
				CidadeDt obTemp = new CidadeDt();
				obTemp.setId(rs.getString("ID_CIDADE"));
				obTemp.setCidade(rs.getString("CIDADE"));
				obTemp.setUf(rs.getString("UF"));
				liTemp.add(obTemp);
			}
		
        } finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public CidadeDt consultarCidadeCodigo(String codigoCidade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CidadeDt Dados=null;
		////System.out.println("....ps-ConsultaId_Cidade)");

		stSql= "SELECT * FROM projudi.VIEW_CIDADE WHERE CIDADE_CODIGO = ?";		ps.adicionarLong(codigoCidade); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Cidade  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CidadeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}
	
	public CidadeDt consultarCodigoSPG(String codigoSPG) throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CidadeDt Dados=null;

		stSql= "SELECT * FROM projudi.VIEW_CIDADE WHERE CODIGO_SPG = ?";		ps.adicionarLong(codigoSPG); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CidadeDt();
				associarDt(Dados, rs1);
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	
	public String consultarDescricaoPorIdUFJSON(String descricao, String uf, String posicao) throws Exception {
		String Sql = "";
		String Sql1 = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_CIDADE as id , CIDADE as descricao1, UF AS descricao2, ID_ESTADO  FROM projudi.VIEW_CIDADE";
		Sql += " WHERE CIDADE LIKE ?";
		ps.adicionarString("%" + descricao + "%");
		if (uf != null && uf.length() > 0) {
			Sql1 = " AND ID_ESTADO = ?";
			ps.adicionarString(uf);
		}
		Sql += Sql1;

		Sql += " ORDER BY CIDADE";

		try {
			rs1 = consultarPaginacao(Sql, ps, posicao);

			ps = new PreparedStatementTJGO();

			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
			ps.adicionarString("%" + descricao + "%");
			if (Sql1.length() > 0)
				ps.adicionarString(uf);

			Sql += Sql1;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
			}
			try {
				if (rs2 != null)
					rs2.close();
			} catch (Exception e1) {
			}
		}
		return stTemp;
	}

	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String Sql = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_CIDADE as id , CIDADE as descricao1, UF AS descricao2  FROM projudi.VIEW_CIDADE";
		Sql += " WHERE (CIDADE LIKE ? OR UF LIKE ?) ORDER BY " + ordenacao;
		ps.adicionarString("%" + descricao + "%");
		ps.adicionarString("%" + descricao + "%");

		try {
			rs1 = consultarPaginacao(Sql, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM projudi.VIEW_CIDADE WHERE (CIDADE LIKE ? OR UF LIKE ?)";

			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
			}
			try {
				if (rs2 != null)
					rs2.close();
			} catch (Exception e1) {
			}
		}
		return stTemp;
	}
}
