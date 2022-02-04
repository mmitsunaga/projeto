package br.gov.go.tj.projudi.ne;

import java.io.UnsupportedEncodingException;

import br.gov.go.tj.projudi.dt.ArquivoPalavraDt;
import br.gov.go.tj.projudi.dt.PalavraDt;
import br.gov.go.tj.projudi.ps.ArquivoPalavraPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ArquivoPalavraNe extends ArquivoPalavraNeGen {

    /**
     * 
     */
    private static final long serialVersionUID = 3101030193777512445L;

    //
    private String retirarPalavras(String palavras) {
                        
                       
        //retiro algumas preposições/ artigos / pronomes 
        palavras= palavras.replace(" pelo "," ").replace(" pela "," ").replace(" ao "," ").replace(" aonde"," ").replace(" do "," ").replace(" de "," ");
        palavras= palavras.replace(" dos "," ").replace(" da "," ").replace(" das "," ").replace(" dum "," ").replace(" duns "," ");       
        palavras= palavras.replace(" duma "," ").replace(" dumas "," ").replace(" no "," ").replace(" nos "," ").replace(" na "," ");
        palavras= palavras.replace(" nas "," ").replace(" num "," ").replace(" numa "," ").replace(" nuns "," ").replace(" numas "," ");
        palavras= palavras.replace(" a "," ").replace(" as "," ").replace(" pelo "," ").replace(" pelos "," ").replace(" pela "," ");
        palavras= palavras.replace(" pelas "," ").replace(" dele "," ").replace(" deles "," ").replace(" dela "," ").replace(" delas "," ");
        palavras= palavras.replace(" deste "," ").replace(" destes "," ").replace(" desta "," ").replace(" destas "," ").replace(" desse "," ");                                                                                       
        palavras= palavras.replace(" desses "," ").replace(" dessa "," ").replace(" dessas "," ").replace(" daquele "," ").replace(" daqueles "," ");               
        palavras= palavras.replace(" daquela "," ").replace(" daquelas "," ").replace(" disto "," ").replace(" disso"," ").replace(" daquilo "," ");
        palavras= palavras.replace(" daqui "," ").replace(" dai "," ").replace(" dali "," ").replace(" doutro "," ").replace(" doutros "," ");
        palavras= palavras.replace(" doutras "," ").replace(" doutras "," ").replace(" neste "," ").replace(" nestes "," ").replace(" nestas "," ");
        palavras= palavras.replace(" naquele "," ").replace(" naqueles "," ").replace(" naquela "," ").replace(" naquelas "," ").replace(" nisto "," ").replace(" nesta "," ");
        palavras= palavras.replace(" nisso "," ").replace(" naquilo "," ").replace(" aquele "," ").replace(" aqueles "," ").replace(" aquela "," ");
        palavras= palavras.replace(" aquelas "," ").replace(" aquilo "," ").replace(" o "," ").replace(" e "," ").replace(" nesse "," ").replace(" nesses "," ");
        palavras= palavras.replace(" em "," ") .replace(" por "," ").replace(" com "," ").replace(" pois "," ").replace(" quanto "," ").replace(" como "," ");
                               
        palavras= palavras.replace(" os "," ").replace(" as "," ").replace(" um "," ").replace(" uns "," ").replace(" seu "," ").replace(" seus "," ");       
        palavras= palavras.replace(" uma "," ").replace(" umas "," ").replace(" onde "," ").replace(" outra "," ").replace(" outras "," ");
        palavras= palavras.replace(" a "," ").replace(" as "," ").replace(" pelo "," ").replace(" pelos "," ").replace(" pela "," ").replace(" pelas "," ");
        palavras= palavras.replace(" ela "," ").replace(" elas "," ").replace(" ele "," ").replace(" eles "," ").replace(" se "," ");
        palavras= palavras.replace(" este "," ").replace(" estes "," ").replace(" esta "," ").replace(" estas "," ").replace(" ja "," ");                                                                                       
        palavras= palavras.replace(" esse "," ").replace(" esses "," ").replace(" essa "," ").replace(" essas "," ").replace(" aquele "," ").replace(" aqueles "," ");               
        palavras= palavras.replace(" aquela "," ").replace(" aquelas "," ").replace(" isto "," ").replace(" isso "," ").replace(" sua "," ").replace(" suas "," ");
        palavras= palavras.replace(" aqui "," ").replace(" ai "," ").replace(" ali "," ").replace(" outro "," ").replace(" outros "," ");
        palavras= palavras.replace(" que "," ").replace(" para "," ").replace(" aos "," ").replace(" nao "," ").replace(" sim "," ");
        
        //retiro os espaços em branco        
        palavras= palavras.replace("     "," ").replace("    "," ").replace("   "," ").replace("  "," ");
        
        
        return palavras;
    }
                                                                                             

    // ---------------------------------------------------------
    public String Verificar(ArquivoPalavraDt dados) {

        String stRetorno = "";

        if (dados.getNomeArquivo().length() == 0)
            stRetorno += "O Campo NomeArquivo é obrigatório.";
        if (dados.getPalavra1().length() == 0)
            stRetorno += "O Campo Palavra é obrigatório.";
        if (dados.getPalavra2().length() == 0)
            stRetorno += "O Campo Palavra é obrigatório.";
        ////System.out.println("..neArquivoPalavraVerificar()");
        return stRetorno;

    }

    public void salvar(String id_arquivo, String palavras, FabricaConexao obFabricaConexao) throws Exception {
        
    	String[ ] stPalavras = limparTexto(palavras).split(" ");
        
        ArquivoPalavraPs obPersistencia = new ArquivoPalavraPs(obFabricaConexao.getConexao());
        
        PalavraNe tempPalavraNe = new PalavraNe(); 
        
        PalavraDt tempPalavraDt1 = null;            
        PalavraDt tempPalavraDt2 = null;
                   
        
        for (int i=0; i<stPalavras.length-1; i++)   
        	try {
        		
        		String palavraLimpa1 = Funcoes.convertePalavraSimplificada(stPalavras[i]);
        		String palavraLimpa2 = Funcoes.convertePalavraSimplificada(stPalavras[i+1]);
                
        		if (palavraLimpa1.trim().length() > 0 && palavraLimpa2.trim().length() > 0){//crio o objeto palavra
                    if (tempPalavraDt2!=null){
                        tempPalavraDt1.copiar(tempPalavraDt2);
                        tempPalavraDt2 = null;
                    }else{                         
                        tempPalavraDt1 = tempPalavraNe.buscarSalvar(palavraLimpa1.trim(), obFabricaConexao);
                    }
                    
                    //busco ou salvo a nova palavra
                    //mando buscarSalvar                    
                    tempPalavraDt2 = tempPalavraNe.buscarSalvar(palavraLimpa2.trim(), obFabricaConexao);
                    
                    ArquivoPalavraDt tempArquivoPalavra = new ArquivoPalavraDt();
                    tempArquivoPalavra.setId_Arquivo(id_arquivo);
                    tempArquivoPalavra.setId_Palavra1(tempPalavraDt1.getId());
                    tempArquivoPalavra.setId_Palavra2(tempPalavraDt2.getId());
                    
                    obPersistencia.inserir(tempArquivoPalavra);        
                }	
        	}catch (Exception ex) {
        		//Se não conseguir inserir a palavra, o processamento não será interrompido.
        	}
    }

    public String limparTexto(String texto) {
    	
    	//sempre minusculas
    	texto = texto.toLowerCase();
        
        //sempre sem acentos
    	//texto= texto.replaceAll("[åáàãâä]","a").replaceAll("[éèêë]","e").replaceAll("[íìîï]","i").replaceAll("[øóòõôö]","o").replaceAll("[úùûü]","u");        
        
        //caracteres especiais
//    	texto= texto.replaceAll("[æð€¡¢£¤¥¦§©ª¬®¯°±²³µ¶·¸¹º»¼½¾¿Ð×Þþßñ÷&$@#%‰^~¨´`'_|;=]"," ");                       
//        
//    	texto= texto.replace("{"," ").replace("}"," ").replace("("," ").replace(")"," ").replace("["," ").replace("]"," ").replace("*"," ");                      
//    	texto= texto.replace("\""," ").replace("."," ").replace(","," ").replace(":"," ").replace("!"," ").replace("?"," ");
//    	texto= texto.replace(">"," ").replace("<"," ").replace("/"," ").replace("+"," ").replace("-"," ");
//    	texto= texto.replace("\\"," ").replace("\n"," ").replace("\r"," ").replace("\t"," ");
        
    	texto = texto.replaceAll("[^a-z\\séèêëiíìîïýÿáàãâäóòõôöúùûü]","");		
    	
        //retiro tos os caracteres soutos 
    	texto= texto.replaceAll("\\s\\w\\s"," ");
        
    	//retiro os espaços em branco        
    	texto= texto.replaceAll("\\s\\s*\\s"," ").trim();        
    	
//    	//retiro todos os numeros
//    	texto = texto.replaceAll("[0123456789]","");
    	
    	String txtUTF8;
		try {
			txtUTF8 = new String(texto.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			txtUTF8 =texto;
		}
    	
        return retirarPalavras(txtUTF8);
    }
}
