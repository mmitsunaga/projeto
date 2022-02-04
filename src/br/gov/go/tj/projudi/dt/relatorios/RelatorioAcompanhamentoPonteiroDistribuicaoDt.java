package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioAcompanhamentoPonteiroDistribuicaoDt extends Dados {

	private static final long serialVersionUID = 1518781199839892639L;

	public static final int CodigoPermissaoPonteiroDistribuicao = 731;

	private String idTipoProcesso;
	private String tipoProcesso;
	private String nomeServentia;
	private long[] quantidade = new long[30];
	private long[] distribuicao = new long[30];
	private long[] redistribuicao = new long[30];
	private long[] cadastro = new long[30];
	private long[] ganhoResponsabilidade = new long[30];
	private long[] perdaResponsabilidade = new long[30];
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioAcompanhamentoPonteiroDistribuicaoDt() {
		limpar();
	}

	public void limpar() {
		idTipoProcesso = "";
		tipoProcesso = "";
		nomeServentia = "";
		quantidade = new long[30];
		distribuicao = new long[30];
		redistribuicao = new long[30];
		ganhoResponsabilidade = new long[30];
		perdaResponsabilidade = new long[30];
		cadastro = new long[30];
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public String getIdTipoProcesso() {
		return idTipoProcesso;
	}

	public void setIdTipoProcesso(String idTipoProcesso) {
		this.idTipoProcesso = idTipoProcesso;
	}

	public String getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(String tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public String getNomeServentia() {
		return nomeServentia;
	}

	public void setNomeServentia(String nomeServentia) {
		this.nomeServentia = nomeServentia;
	}

	public void setQuantidade(int indice, long valor) {
		this.quantidade[indice] = valor;
	}
	
	public long getQuantidade0() {
		return quantidade[0];
	}
	
	public long getQuantidade1() {
		return quantidade[1];
	}
	
	public long getQuantidade2() {
		return quantidade[2];
	}
	
	public long getQuantidade3() {
		return quantidade[3];
	}
	
	public long getQuantidade4() {
		return quantidade[4];
	}
	
	public long getQuantidade5() {
		return quantidade[5];
	}
	
	public long getQuantidade6() {
		return quantidade[6];
	}
	
	public long getQuantidade7() {
		return quantidade[7];
	}
	
	public long getQuantidade8() {
		return quantidade[8];
	}
	
	public long getQuantidade9() {
		return quantidade[9];
	}
	
	public long getQuantidade10() {
		return quantidade[10];
	}
	
	public long getQuantidade11() {
		return quantidade[11];
	}
	
	public long getQuantidade12() {
		return quantidade[12];
	}
	
	public long getQuantidade13() {
		return quantidade[13];
	}
	
	public long getQuantidade14() {
		return quantidade[14];
	}
	
	public long getQuantidade15() {
		return quantidade[15];
	}
	
	public long getQuantidade16() {
		return quantidade[16];
	}
	
	public long getQuantidade17() {
		return quantidade[17];
	}
	
	public long getQuantidade18() {
		return quantidade[18];
	}
	
	public long getQuantidade19() {
		return quantidade[19];
	}
	
	public long getQuantidade20() {
		return quantidade[20];
	}
	
	public long getQuantidade21() {
		return quantidade[21];
	}
	
	public long getQuantidade22() {
		return quantidade[22];
	}
	
	public long getQuantidade23() {
		return quantidade[23];
	}
	
	public long getQuantidade24() {
		return quantidade[24];
	}
	
	public long getQuantidade25() {
		return quantidade[25];
	}
	
	public long getQuantidade26() {
		return quantidade[26];
	}
	
	public long getQuantidade27() {
		return quantidade[27];
	}
	
	public long getQuantidade28() {
		return quantidade[28];
	}
	
	public long getQuantidade29() {
		return quantidade[29];
	}
	
	public long getQuantidadeTotal(){
		return quantidade[0] + quantidade[1] + quantidade[2] + quantidade[3] + quantidade[4] + quantidade[5] + quantidade[6] + quantidade[7] + quantidade[8] + quantidade[9] + quantidade[10] + quantidade[11] + quantidade[12] + quantidade[13] + quantidade[14] + quantidade[15] + quantidade[16] + quantidade[17] + quantidade[18] + quantidade[19] + quantidade[20] + quantidade[21] + quantidade[22] + quantidade[23] + quantidade[24] + quantidade[25] + quantidade[26] + quantidade[27] + quantidade[28] + quantidade[29];
	}
	
	public void setRedistribuicao(int indice, long valor) {
		this.redistribuicao[indice] = valor;
	}
	
	public long getRedistribuicao0() {
		return redistribuicao[0];
	}
	
	public long getRedistribuicao1() {
		return redistribuicao[1];
	}
	
	public long getRedistribuicao2() {
		return redistribuicao[2];
	}
	
	public long getRedistribuicao3() {
		return redistribuicao[3];
	}
	
	public long getRedistribuicao4() {
		return redistribuicao[4];
	}
	
	public long getRedistribuicao5() {
		return redistribuicao[5];
	}
	
	public long getRedistribuicao6() {
		return redistribuicao[6];
	}
	
	public long getRedistribuicao7() {
		return redistribuicao[7];
	}
	
	public long getRedistribuicao8() {
		return redistribuicao[8];
	}
	
	public long getRedistribuicao9() {
		return redistribuicao[9];
	}
	
	public long getRedistribuicao10() {
		return redistribuicao[10];
	}
	
	public long getRedistribuicao11() {
		return redistribuicao[11];
	}
	
	public long getRedistribuicao12() {
		return redistribuicao[12];
	}
	
	public long getRedistribuicao13() {
		return redistribuicao[13];
	}
	
	public long getRedistribuicao14() {
		return redistribuicao[14];
	}
	
	public long getRedistribuicao15() {
		return redistribuicao[15];
	}
	
	public long getRedistribuicao16() {
		return redistribuicao[16];
	}
	
	public long getRedistribuicao17() {
		return redistribuicao[17];
	}
	
	public long getRedistribuicao18() {
		return redistribuicao[18];
	}
	
	public long getRedistribuicao19() {
		return redistribuicao[19];
	}
	
	public long getRedistribuicao20() {
		return redistribuicao[20];
	}
	
	public long getRedistribuicao21() {
		return redistribuicao[21];
	}
	
	public long getRedistribuicao22() {
		return redistribuicao[22];
	}
	
	public long getRedistribuicao23() {
		return redistribuicao[23];
	}
	
	public long getRedistribuicao24() {
		return redistribuicao[24];
	}
	
	public long getRedistribuicao25() {
		return redistribuicao[25];
	}
	
	public long getRedistribuicao26() {
		return redistribuicao[26];
	}
	
	public long getRedistribuicao27() {
		return redistribuicao[27];
	}
	
	public long getRedistribuicao28() {
		return redistribuicao[28];
	}
	
	public long getRedistribuicao29() {
		return redistribuicao[29];
	}
	
	public long getRedistribuicaoTotal(){
		return redistribuicao[0] + redistribuicao[1] + redistribuicao[2] + redistribuicao[3] + redistribuicao[4] + redistribuicao[5] + redistribuicao[6] + redistribuicao[7] + redistribuicao[8] + redistribuicao[9] + redistribuicao[10] + redistribuicao[11] + redistribuicao[12] + redistribuicao[13] + redistribuicao[14] + redistribuicao[15] + redistribuicao[16] + redistribuicao[17] + redistribuicao[18] + redistribuicao[19] + redistribuicao[20] + redistribuicao[21] + redistribuicao[22] + redistribuicao[23] + redistribuicao[24] + redistribuicao[25] + redistribuicao[26] + redistribuicao[27] + redistribuicao[28] + redistribuicao[29];
	}
	
	public void setDistribuicao(int indice, long valor) {
		this.distribuicao[indice] = valor;
	}
	
	public long getDistribuicao0() {
		return distribuicao[0];
	}
	
	public long getDistribuicao1() {
		return distribuicao[1];
	}
	
	public long getDistribuicao2() {
		return distribuicao[2];
	}
	
	public long getDistribuicao3() {
		return distribuicao[3];
	}
	
	public long getDistribuicao4() {
		return distribuicao[4];
	}
	
	public long getDistribuicao5() {
		return distribuicao[5];
	}
	
	public long getDistribuicao6() {
		return distribuicao[6];
	}
	
	public long getDistribuicao7() {
		return distribuicao[7];
	}
	
	public long getDistribuicao8() {
		return distribuicao[8];
	}
	
	public long getDistribuicao9() {
		return distribuicao[9];
	}
	
	public long getDistribuicao10() {
		return distribuicao[10];
	}
	
	public long getDistribuicao11() {
		return distribuicao[11];
	}
	
	public long getDistribuicao12() {
		return distribuicao[12];
	}
	
	public long getDistribuicao13() {
		return distribuicao[13];
	}
	
	public long getDistribuicao14() {
		return distribuicao[14];
	}
	
	public long getDistribuicao15() {
		return distribuicao[15];
	}
	
	public long getDistribuicao16() {
		return distribuicao[16];
	}
	
	public long getDistribuicao17() {
		return distribuicao[17];
	}
	
	public long getDistribuicao18() {
		return distribuicao[18];
	}
	
	public long getDistribuicao19() {
		return distribuicao[19];
	}
	
	public long getDistribuicao20() {
		return distribuicao[20];
	}
	
	public long getDistribuicao21() {
		return distribuicao[21];
	}
	
	public long getDistribuicao22() {
		return distribuicao[22];
	}
	
	public long getDistribuicao23() {
		return distribuicao[23];
	}
	
	public long getDistribuicao24() {
		return distribuicao[24];
	}
	
	public long getDistribuicao25() {
		return distribuicao[25];
	}
	
	public long getDistribuicao26() {
		return distribuicao[26];
	}
	
	public long getDistribuicao27() {
		return distribuicao[27];
	}
	
	public long getDistribuicao28() {
		return distribuicao[28];
	}
	
	public long getDistribuicao29() {
		return distribuicao[29];
	}
	
	public long getDistribuicaoTotal(){
		return distribuicao[0] + distribuicao[1] + distribuicao[2] + distribuicao[3] + distribuicao[4] + distribuicao[5] + distribuicao[6] + distribuicao[7] + distribuicao[8] + distribuicao[9] + distribuicao[10] + distribuicao[11] + distribuicao[12] + distribuicao[13] + distribuicao[14] + distribuicao[15] + distribuicao[16] + distribuicao[17] + distribuicao[18] + distribuicao[19] + distribuicao[20] + distribuicao[21] + distribuicao[22] + distribuicao[23] + distribuicao[24] + distribuicao[25] + distribuicao[26] + distribuicao[27] + distribuicao[28] + distribuicao[29];
	}
	
	public void setGanhoResponsabilidade(int indice, long valor) {
		this.ganhoResponsabilidade[indice] = valor;
	}
	
	public long getGanhoResponsabilidade0() {
		return ganhoResponsabilidade[0];
	}
	
	public long getGanhoResponsabilidade1() {
		return ganhoResponsabilidade[1];
	}
	
	public long getGanhoResponsabilidade2() {
		return ganhoResponsabilidade[2];
	}
	
	public long getGanhoResponsabilidade3() {
		return ganhoResponsabilidade[3];
	}
	
	public long getGanhoResponsabilidade4() {
		return ganhoResponsabilidade[4];
	}
	
	public long getGanhoResponsabilidade5() {
		return ganhoResponsabilidade[5];
	}
	
	public long getGanhoResponsabilidade6() {
		return ganhoResponsabilidade[6];
	}
	
	public long getGanhoResponsabilidade7() {
		return ganhoResponsabilidade[7];
	}
	
	public long getGanhoResponsabilidade8() {
		return ganhoResponsabilidade[8];
	}
	
	public long getGanhoResponsabilidade9() {
		return ganhoResponsabilidade[9];
	}
	
	public long getGanhoResponsabilidade10() {
		return ganhoResponsabilidade[10];
	}
	
	public long getGanhoResponsabilidade11() {
		return ganhoResponsabilidade[11];
	}
	
	public long getGanhoResponsabilidade12() {
		return ganhoResponsabilidade[12];
	}
	
	public long getGanhoResponsabilidade13() {
		return ganhoResponsabilidade[13];
	}
	
	public long getGanhoResponsabilidade14() {
		return ganhoResponsabilidade[14];
	}
	
	public long getGanhoResponsabilidade15() {
		return ganhoResponsabilidade[15];
	}
	
	public long getGanhoResponsabilidade16() {
		return ganhoResponsabilidade[16];
	}
	
	public long getGanhoResponsabilidade17() {
		return ganhoResponsabilidade[17];
	}
	
	public long getGanhoResponsabilidade18() {
		return ganhoResponsabilidade[18];
	}
	
	public long getGanhoResponsabilidade19() {
		return ganhoResponsabilidade[19];
	}
	
	public long getGanhoResponsabilidade20() {
		return ganhoResponsabilidade[20];
	}
	
	public long getGanhoResponsabilidade21() {
		return ganhoResponsabilidade[21];
	}
	
	public long getGanhoResponsabilidade22() {
		return ganhoResponsabilidade[22];
	}
	
	public long getGanhoResponsabilidade23() {
		return ganhoResponsabilidade[23];
	}
	
	public long getGanhoResponsabilidade24() {
		return ganhoResponsabilidade[24];
	}
	
	public long getGanhoResponsabilidade25() {
		return ganhoResponsabilidade[25];
	}
	
	public long getGanhoResponsabilidade26() {
		return ganhoResponsabilidade[26];
	}
	
	public long getGanhoResponsabilidade27() {
		return ganhoResponsabilidade[27];
	}
	
	public long getGanhoResponsabilidade28() {
		return ganhoResponsabilidade[28];
	}
	
	public long getGanhoResponsabilidade29() {
		return ganhoResponsabilidade[29];
	}
	
	public long getGanhoResponsabilidadeTotal(){
		return ganhoResponsabilidade[0] + ganhoResponsabilidade[1] + ganhoResponsabilidade[2] + ganhoResponsabilidade[3] + ganhoResponsabilidade[4] + ganhoResponsabilidade[5] + ganhoResponsabilidade[6] + ganhoResponsabilidade[7] + ganhoResponsabilidade[8] + ganhoResponsabilidade[9] + ganhoResponsabilidade[10] + ganhoResponsabilidade[11] + ganhoResponsabilidade[12] + ganhoResponsabilidade[13] + ganhoResponsabilidade[14] + ganhoResponsabilidade[15] + ganhoResponsabilidade[16] + ganhoResponsabilidade[17] + ganhoResponsabilidade[18] + ganhoResponsabilidade[19] + ganhoResponsabilidade[20] + ganhoResponsabilidade[21] + ganhoResponsabilidade[22] + ganhoResponsabilidade[23] + ganhoResponsabilidade[24] + ganhoResponsabilidade[25] + ganhoResponsabilidade[26] + ganhoResponsabilidade[27] + ganhoResponsabilidade[28] + ganhoResponsabilidade[29];
	}
	
	public void setPerdaResponsabilidade(int indice, long valor) {
		this.perdaResponsabilidade[indice] = valor;
	}
	
	public long getPerdaResponsabilidade0() {
		return perdaResponsabilidade[0];
	}
	
	public long getPerdaResponsabilidade1() {
		return perdaResponsabilidade[1];
	}
	
	public long getPerdaResponsabilidade2() {
		return perdaResponsabilidade[2];
	}
	
	public long getPerdaResponsabilidade3() {
		return perdaResponsabilidade[3];
	}
	
	public long getPerdaResponsabilidade4() {
		return perdaResponsabilidade[4];
	}
	
	public long getPerdaResponsabilidade5() {
		return perdaResponsabilidade[5];
	}
	
	public long getPerdaResponsabilidade6() {
		return perdaResponsabilidade[6];
	}
	
	public long getPerdaResponsabilidade7() {
		return perdaResponsabilidade[7];
	}
	
	public long getPerdaResponsabilidade8() {
		return perdaResponsabilidade[8];
	}
	
	public long getPerdaResponsabilidade9() {
		return perdaResponsabilidade[9];
	}
	
	public long getPerdaResponsabilidade10() {
		return perdaResponsabilidade[10];
	}
	
	public long getPerdaResponsabilidade11() {
		return perdaResponsabilidade[11];
	}
	
	public long getPerdaResponsabilidade12() {
		return perdaResponsabilidade[12];
	}
	
	public long getPerdaResponsabilidade13() {
		return perdaResponsabilidade[13];
	}
	
	public long getPerdaResponsabilidade14() {
		return perdaResponsabilidade[14];
	}
	
	public long getPerdaResponsabilidade15() {
		return perdaResponsabilidade[15];
	}
	
	public long getPerdaResponsabilidade16() {
		return perdaResponsabilidade[16];
	}
	
	public long getPerdaResponsabilidade17() {
		return perdaResponsabilidade[17];
	}
	
	public long getPerdaResponsabilidade18() {
		return perdaResponsabilidade[18];
	}
	
	public long getPerdaResponsabilidade19() {
		return perdaResponsabilidade[19];
	}
	
	public long getPerdaResponsabilidade20() {
		return perdaResponsabilidade[20];
	}
	
	public long getPerdaResponsabilidade21() {
		return perdaResponsabilidade[21];
	}
	
	public long getPerdaResponsabilidade22() {
		return perdaResponsabilidade[22];
	}
	
	public long getPerdaResponsabilidade23() {
		return perdaResponsabilidade[23];
	}
	
	public long getPerdaResponsabilidade24() {
		return perdaResponsabilidade[24];
	}
	
	public long getPerdaResponsabilidade25() {
		return perdaResponsabilidade[25];
	}
	
	public long getPerdaResponsabilidade26() {
		return perdaResponsabilidade[26];
	}
	
	public long getPerdaResponsabilidade27() {
		return perdaResponsabilidade[27];
	}
	
	public long getPerdaResponsabilidade28() {
		return perdaResponsabilidade[28];
	}
	
	public long getPerdaResponsabilidade29() {
		return perdaResponsabilidade[29];
	}
	
	public long getPerdaResponsabilidadeTotal(){
		return perdaResponsabilidade[0] + perdaResponsabilidade[1] + perdaResponsabilidade[2] + perdaResponsabilidade[3] + perdaResponsabilidade[4] + perdaResponsabilidade[5] + perdaResponsabilidade[6] + perdaResponsabilidade[7] + perdaResponsabilidade[8] + perdaResponsabilidade[9] + perdaResponsabilidade[10] + perdaResponsabilidade[11] + perdaResponsabilidade[12] + perdaResponsabilidade[13] + perdaResponsabilidade[14] + perdaResponsabilidade[15] + perdaResponsabilidade[16] + perdaResponsabilidade[17] + perdaResponsabilidade[18] + perdaResponsabilidade[19] + perdaResponsabilidade[20] + perdaResponsabilidade[21] + perdaResponsabilidade[22] + perdaResponsabilidade[23] + perdaResponsabilidade[24] + perdaResponsabilidade[25] + perdaResponsabilidade[26] + perdaResponsabilidade[27] + perdaResponsabilidade[28] + perdaResponsabilidade[29];
	}
	
	public void setCadastro(int indice, long valor) {
		this.cadastro[indice] = valor;
	}
	
	public long getCadastro0() {
		return cadastro[0];
	}
	
	public long getCadastro1() {
		return cadastro[1];
	}
	
	public long getCadastro2() {
		return cadastro[2];
	}
	
	public long getCadastro3() {
		return cadastro[3];
	}
	
	public long getCadastro4() {
		return cadastro[4];
	}
	
	public long getCadastro5() {
		return cadastro[5];
	}
	
	public long getCadastro6() {
		return cadastro[6];
	}
	
	public long getCadastro7() {
		return cadastro[7];
	}
	
	public long getCadastro8() {
		return cadastro[8];
	}
	
	public long getCadastro9() {
		return cadastro[9];
	}
	
	public long getCadastro10() {
		return cadastro[10];
	}
	
	public long getCadastro11() {
		return cadastro[11];
	}
	
	public long getCadastro12() {
		return cadastro[12];
	}
	
	public long getCadastro13() {
		return cadastro[13];
	}
	
	public long getCadastro14() {
		return cadastro[14];
	}
	
	public long getCadastro15() {
		return cadastro[15];
	}
	
	public long getCadastro16() {
		return cadastro[16];
	}
	
	public long getCadastro17() {
		return cadastro[17];
	}
	
	public long getCadastro18() {
		return cadastro[18];
	}
	
	public long getCadastro19() {
		return cadastro[19];
	}
	
	public long getCadastro20() {
		return cadastro[20];
	}
	
	public long getCadastro21() {
		return cadastro[21];
	}
	
	public long getCadastro22() {
		return cadastro[22];
	}
	
	public long getCadastro23() {
		return cadastro[23];
	}
	
	public long getCadastro24() {
		return cadastro[24];
	}
	
	public long getCadastro25() {
		return cadastro[25];
	}
	
	public long getCadastro26() {
		return cadastro[26];
	}
	
	public long getCadastro27() {
		return cadastro[27];
	}
	
	public long getCadastro28() {
		return cadastro[28];
	}
	
	public long getCadastro29() {
		return cadastro[29];
	}
	
	public long getCadastroTotal(){
		return cadastro[0] + cadastro[1] + cadastro[2] + cadastro[3] + cadastro[4] + cadastro[5] + cadastro[6] + cadastro[7] + cadastro[8] + cadastro[9] + cadastro[10] + cadastro[11] + cadastro[12] + cadastro[13] + cadastro[14] + cadastro[15] + cadastro[16] + cadastro[17] + cadastro[18] + cadastro[19] + cadastro[20] + cadastro[21] + cadastro[22] + cadastro[23] + cadastro[24] + cadastro[25] + cadastro[26] + cadastro[27] + cadastro[28] + cadastro[29];
	}
	
}