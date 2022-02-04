package br.gov.go.tj.projudi.temp;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.Certificado.SignerException;

public class ArquivosImportarRunnable implements Runnable {

        
    public void run() {
        String stCaminho = "";
        ArquivosImportar obArquivosImportar= null;
        AtualizacaoDt atualizacaoDt = null;
        ArquivoNe arquivoNe = new ArquivoNe();
        InputStream inputStream = null;
       
        try{
            obArquivosImportar= ArquivosImportar.getInstacia();
        } catch(Exception e1) {
            obArquivosImportar.removeThread();
            e1.printStackTrace();
            return;
        }
        
        do {
            try{
                long loTempoInicial = 0;
                
                atualizacaoDt = obArquivosImportar.getData();
                
                if (atualizacaoDt==null) {
                    //System.out.println("Erro Não Existe Mais arquivos");
                    break;
                }
    
                //stCaminho = "/arquivosprojudi/documentos";
                stCaminho = "/arquivosprojudi/documentos";
    
                if (atualizacaoDt.getTabelaOrigem().equalsIgnoreCase("1")) stCaminho = "/arquivosprojudi/processos";
    
    //            String stCaminho = "/arquivosprojudi/documentos/";
    //
    //            if (atualizacaoDt.getTabelaOrigem().equalsIgnoreCase("1")) stCaminho = "/arquivosprojudi/processos/";
                
                ////System.out.println("Lendo o Arquivo " + stCaminho + atualizacaoDt.getCaminho());
                // CONTEUDO DOS ARQUIVOS INSERIDOS NO PROJUDI
                File arquivo = new File(stCaminho + atualizacaoDt.getCaminho());
                
                if (!arquivo.exists()) {
                    //System.out.println("Arquivo Não existe..: " + stCaminho + atualizacaoDt.getCaminho());
                    continue;
                }
                
                // LÊ CONTEÚDO DO ARQUIVO ZIPADO E CRIA UM CmsSignedData
                inputStream = new FileInputStream(arquivo);
    
                // Arquivo_Documento
                if (atualizacaoDt.getTabelaOrigem().equals("1")) {
                    StringBuffer strRecibo = new StringBuffer("1;#;" + atualizacaoDt.getNomeArquivo().substring(0, atualizacaoDt.getNomeArquivo().length() - 4) + ";#;" + atualizacaoDt.getDataInsercao() + ";#;");
                    strRecibo.append("@#@" + Funcoes.formataNumeroProcesso(atualizacaoDt.getProcessoNumeroCompleto()) + "@#@" + atualizacaoDt.getId_Movimentacao());
                    strRecibo.append("@#@&#&Recibo comprovando o recebimento de arquivo no Sistema Projudi.&#&#@&!%");
                    // ********************************************************************************************************************************
    
                    // CONTEUDO ARQUIVO
                    // **************************************************************************************
                    ////System.out.println(" antes do CMSSignedData");
                    //leitura de arquivo
                    loTempoInicial = System.currentTimeMillis();
                    CMSSignedData dadosConteudo = new CMSSignedData(inputStream);
                    obArquivosImportar.addTempoLeitura(System.currentTimeMillis() - loTempoInicial);
                    ////System.out.println(" depois do CMSSignedData");

                    //Assinatura de arquivo                    
                    loTempoInicial = System.currentTimeMillis();
                    byte[] result = Signer.signBuffer(Signer.concat(strRecibo.toString().getBytes(), dadosConteudo.getEncoded()), obArquivosImportar.getParse().getCertificate(), obArquivosImportar.getParse().getPrivateKey(), obArquivosImportar.getParse().getCertificateChain());
                    obArquivosImportar.addTempoAssinatura(System.currentTimeMillis() - loTempoInicial);
                    ////System.out.println(" depois da assinatura");
                    
    //                if (!Signer.verifySig(result)) {
    //                    throw new MensagemException(" Erro: ... Assinatura Inválida!" );
    //                }
                    
                    atualizacaoDt.setRecibo("true");
                    atualizacaoDt.setArquivo(result);
                    //qtd Bytes
                    obArquivosImportar.addQtdBytes(result.length);
                    dadosConteudo = null;
                    
    
                } else {
                    byte[] bytes = new byte[(int) arquivo.length()];
                    
                    //qtd Bytes
                    obArquivosImportar.addQtdBytes(arquivo.length());
                    
                    // Read in the bytes
                    int offset = 0;
                    int numRead = 0;
                    int inTamanhoLeitura = (1024 <= bytes.length) ? 1024 : bytes.length;
                    //leitura de arquivo                    
                    loTempoInicial = System.currentTimeMillis();
                    while ((numRead = inputStream.read(bytes, offset, inTamanhoLeitura)) > 0) {
                        offset += numRead;
                        inTamanhoLeitura = (1024 <= (bytes.length - offset)) ? 1024 : bytes.length - offset;
                    }
                    obArquivosImportar.addTempoLeitura(System.currentTimeMillis() - loTempoInicial);
                    atualizacaoDt.setRecibo("false");
                    atualizacaoDt.setArquivo(bytes);
                }
                                
                
                atualizacaoDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
                atualizacaoDt.setIpComputadorLog("Servidor");
    
                ////System.out.println("Arquivo ..: " +atualizacaoDt.getId() + " caminho :" + atualizacaoDt.getCaminho());
                
                atualizacaoDt.setNomeArquivo("");                                     
                atualizacaoDt.setId_ArquivoTipo("");
                atualizacaoDt.setContentType("");
                atualizacaoDt.setCaminho("");
                atualizacaoDt.setUsuarioAssinador("");
                atualizacaoDt.setDataInsercao("");
                atualizacaoDt.setCodigoTemp("Null");
                ////System.out.println(" antes do salvar");     
                //Gravação de Arquivo                
                loTempoInicial = System.currentTimeMillis();
                arquivoNe.salvarImportacao((ArquivoDt) atualizacaoDt);
                ////System.out.println(" depois do salvar");
                obArquivosImportar.addTempoEscrita(System.currentTimeMillis() - loTempoInicial);
                obArquivosImportar.aumentarPronto();
                ////System.out.println(" Antes ... do Sleep..............");
                // Thread.sleep(60000);
                ////System.out.println(" depois ... do Sleep..............");
                
            }catch(FileNotFoundException e) {
                //System.out.println("ErroArquivo no Arquivo " + stCaminho + atualizacaoDt.getCaminho() + " | " + atualizacaoDt.getId() + " Não foi encontrado | " + e.getMessage());                
            }catch(EOFException e) {              
                //System.out.println("ErroArquivo no Arquivo " + stCaminho + atualizacaoDt.getCaminho() + " | " + atualizacaoDt.getId() + " está com defeito | " + e.getMessage());               
            }catch(IOException e) {               
                //System.out.println("ErroArquivo no Arquivo " + stCaminho + atualizacaoDt.getCaminho() + " | " + atualizacaoDt.getId() + " erro na leitura do arquivo | " + e.getMessage());
            } catch(SignerException e) {
                //System.out.println("ErroArquivo no Arquivo " + stCaminho + atualizacaoDt.getCaminho() + " | " + atualizacaoDt.getId() + " erro na assintura do arquivo | " + e.getMessage());               
            } catch(CMSException e) {
                //System.out.println("ErroArquivo no Arquivo " + stCaminho + atualizacaoDt.getCaminho() + " | " + atualizacaoDt.getId() + " erro na leitura do p7s | " + e.getMessage());
            } catch(IllegalStateException e){
                //System.out.println("ErroArquivo no Arquivo " + stCaminho + atualizacaoDt.getCaminho() + " | " + atualizacaoDt.getId() + " erro na leitura do p7s | " + e.getMessage());
            }catch(Exception e) {
                e.printStackTrace();
                //System.out.println("ErroGeral :  | "  + stCaminho + atualizacaoDt.getCaminho() + " | " + e.getMessage());
                break;
            }finally{
                try{
                    inputStream.close();
                } catch(IOException e) {
                    
                    e.printStackTrace();
                    //System.out.println("Erro a tentar fechar os arquivo "  + stCaminho + atualizacaoDt.getCaminho() + " | " + atualizacaoDt.getId() + " | ");
                }
            }
                                               
        } while (obArquivosImportar.isContinuar());
        
        obArquivosImportar.removeThread();
    
        //obArquivosImportar.setStatus("Parado");            

    }

    
}
