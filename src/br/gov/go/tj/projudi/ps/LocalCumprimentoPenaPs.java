package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class LocalCumprimentoPenaPs extends LocalCumprimentoPenaPsGen{
	
	/**
     * 
     */
    private static final long serialVersionUID = -8392646894237594837L;

    public LocalCumprimentoPenaPs(Connection conexao){
    	Conexao = conexao;
	}

	public LocalCumprimentoPenaDt consultarId(String id_localcumprimentopena )  throws Exception {
		String Sql;
		LocalCumprimentoPenaDt Dados=null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_LOCAL_CUMP_PENA WHERE ID_LOCAL_CUMP_PENA = ? ";
		ps.adicionarLong(id_localcumprimentopena);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new LocalCumprimentoPenaDt();
				associarDados(Dados, rs1);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}

	public void associarDados( LocalCumprimentoPenaDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_LOCAL_CUMP_PENA"));
		Dados.setLocalCumprimentoPena(rs1.getString("LOCAL_CUMP_PENA"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		
		//Endereco
		Dados.getEnderecoLocal().setId(rs1.getString("ID_ENDERECO"));
		Dados.getEnderecoLocal().setLogradouro(rs1.getString("LOGRADOURO"));
		Dados.getEnderecoLocal().setNumero(rs1.getString("NUMERO"));
		Dados.getEnderecoLocal().setComplemento(rs1.getString("COMPLEMENTO"));
		Dados.getEnderecoLocal().setCep(rs1.getString("CEP"));
		Dados.getEnderecoLocal().setId_Bairro(rs1.getString("ID_BAIRRO"));
		Dados.getEnderecoLocal().setBairro(rs1.getString("BAIRRO"));
		Dados.getEnderecoLocal().setId_Cidade(rs1.getString("ID_CIDADE"));
		Dados.getEnderecoLocal().setCidade(rs1.getString("CIDADE"));
		Dados.getEnderecoLocal().setUf(rs1.getString("ESTADO"));
		
	}
	
	/**
	 * Consulta o local de cumprimento de pena atual, com endereço
	 * @param idProcesso: identificação do processo
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoComEndereco(String idProcesso) throws Exception{
		String retorno = "";
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql.append("SELECT MAX(pee.DATA_INICIO), l.LOCAL_CUMP_PENA, l.LOGRADOURO, l.NUMERO, l.COMPLEMENTO, l.BAIRRO, l.CIDADE , l.ESTADO, l.CEP FROM PROJUDI.VIEW_LOCAL_CUMP_PENA l");
		sql.append(" INNER JOIN PROJUDI.VIEW_EVENTO_LOCAL el ON el.ID_LOCAL_CUMP_PENA = l.ID_LOCAL_CUMP_PENA");
		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EVENTO_EXE pee ON el.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
		sql.append(" INNER JOIN PROJUDI.VIEW_MOVI mov ON mov.ID_MOVI = pee.ID_MOVI");
		sql.append(" WHERE mov.ID_PROC = ? ");
		sql.append(" GROUP BY l.LOCAL_CUMP_PENA, l.LOGRADOURO, l.NUMERO, l.COMPLEMENTO, l.BAIRRO, l.CIDADE , l.ESTADO, l.CEP ");
		ps.adicionarLong(idProcesso);

		try{
			rs = consultar(sql.toString(),ps);
			if (rs.next()) {
				retorno += rs.getString("LOCAL_CUMP_PENA");
				if(rs.getString("LOGRADOURO") != null){
					retorno += " - " + rs.getString("LOGRADOURO");
					if(rs.getString("NUMERO")!= null) retorno += ", " + rs.getString("NUMERO");
					if(rs.getString("COMPLEMENTO")!= null) retorno += ", " + rs.getString("COMPLEMENTO");
					if(rs.getString("BAIRRO")!= null) retorno += ", " + rs.getString("BAIRRO");
					if(rs.getString("CIDADE")!= null) retorno += ", " + rs.getString("CIDADE");
					if(rs.getString("ESTADO")!= null) retorno += ", " + rs.getString("ESTADO");
					if(rs.getString("CEP")!= null) retorno += ". CEP:" + rs.getString("CEP");
				}
			}
		
		}finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        }  
		return retorno;
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_LOCAL_CUMP_PENA as id , LOCAL_CUMP_PENA as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_LOCAL_CUMP_PENA";
		stSqlFrom += " WHERE LOCAL_CUMP_PENA LIKE ?";
		stSqlOrder = " ORDER BY LOCAL_CUMP_PENA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_LOCAL_CUMP_PENA, LOCAL_CUMP_PENA";
		stSqlFrom = " FROM PROJUDI.VIEW_LOCAL_CUMP_PENA";
		stSqlFrom += " WHERE LOCAL_CUMP_PENA LIKE ?";
		stSqlOrder = " ORDER BY LOCAL_CUMP_PENA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			if (posicao.length() > 0)
				rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			else rs1 = consultar(stSql + stSqlFrom + stSqlOrder, ps);

			while (rs1.next()) {
				LocalCumprimentoPenaDt obTemp = new LocalCumprimentoPenaDt();
				obTemp.setId(rs1.getString("ID_LOCAL_CUMP_PENA"));
				obTemp.setLocalCumprimentoPena(rs1.getString("LOCAL_CUMP_PENA"));
				liTemp.add(obTemp);
			}
			if (posicao.length() > 0){
				stSql= "SELECT COUNT(*) as QUANTIDADE";
				rs2 = consultar(stSql + stSqlFrom, ps);
				if (rs2.next()) {
					liTemp.add(rs2.getLong("QUANTIDADE"));
				}	
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
}
