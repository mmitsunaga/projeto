package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


//---------------------------------------------------------
public class EnderecoPs extends EnderecoPsGen{

	public EnderecoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * 
     */
    private static final long serialVersionUID = -363682049654565909L;

	public EnderecoDt consultarCompleto(String id_Bairro, String logradouro, String numero, String complemento, String cep) throws Exception {

		String stSql="";
		EnderecoDt obTemp=null;
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * FROM PROJUDI.VIEW_ENDERECO ";
		stSql += " WHERE Id_bairro = ? "; 				ps.adicionarLong(id_Bairro);
		stSql += " and LOGRADOURO = ?  ";				ps.adicionarString(logradouro);
		if (numero!=null && !numero.isEmpty()) {
			stSql += " and numero = ?  ";				ps.adicionarString(numero);
		} else {
			stSql += " and trim(numero) is null  ";				 
		}
		if (complemento!=null && !complemento.isEmpty()) {
			stSql += " and complemento = ?  ";				ps.adicionarString(complemento);
		} else {
			stSql += " and trim(complemento) is null";			 
		}
			
		if (cep!=null && !cep.isEmpty()) {
			stSql += " and cep = ?  ";						ps.adicionarLong(Funcoes.desformataNumeroProcesso(cep));
		} else {
			stSql += " and (cep is null or cep = 0)";			
		}
		
		try{
			////System.out.println("..ps-ConsultaDescricaoEndereco  " + stSql);
			rs1 = consultar(stSql, ps);
			////System.out.println("....Execução Query OK"  );

			if (rs1.next()) {
				obTemp = new EnderecoDt();
				associarDt(obTemp,rs1);
			}
			
			////System.out.println("..EnderecoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return obTemp; 
	}
	

}
