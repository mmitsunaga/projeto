package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.PalavraDt;
import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class PalavraPs extends PalavraPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 8880655155714712274L;

    public PalavraPs(Connection conexao){
    	Conexao = conexao;
	}

	public PalavraDt consultarDescricao(String palavra) throws Exception {
		String Sql;
		PalavraDt Dados = null;
		////System.out.println("....ps-ConsultaDescricao)");
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PALAVRA WHERE PALAVRA = ?";
		ps.adicionarString(Funcoes.convertePalavraSimplificada(palavra));

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new PalavraDt();
				associarDt(Dados, rs1);
			}			            
		} finally{
			try{ if (rs1 != null) rs1.close(); } catch(Exception e) {}
		}

		if (Dados == null) throw new MensagemException("A consulta não encontrou a palavra: " + palavra + "+");

		return Dados;

	}

	public void inserir(PalavraDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		if ((dados.getPalavra().length() == 0)) return;
		
		Sql = "INSERT INTO PROJUDI.Palavra (PALAVRA) Values (?)";			ps.adicionarString(dados.getPalavra());
					
		dados.setId(executarInsert(Sql, "ID_PALAVRA", ps));			
		
	}

//---------------------------------------------------------
	public void alterar(PalavraDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.Palavra SET  ";
		Sql += "PALAVRA  = ?";
		ps.adicionarString(dados.getPalavra());		
		Sql = Sql + " WHERE ID_PALAVRA  = ?";
		ps.adicionarLong(dados.getId());	

		executarUpdateDelete(Sql, ps);			

	}

//

}
