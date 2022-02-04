package br.gov.go.tj.projudi.temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;

public class CertificadosImportarRunnable implements Runnable {
    
    public void run() {
        CertificadosImportar obCertificadosImportar= null;
        CertificadoDt atualizacaoDt= null;    
        String stCaminho = "";
            
        
            
            try{
                obCertificadosImportar= CertificadosImportar.getInstacia();
            } catch(Exception e1) {
                obCertificadosImportar.removeThread();
                
                e1.printStackTrace();
                return;
            }
            
            AtualizacaoNe atualizacaoNe = new AtualizacaoNe();                            
                                                                         
            do {
                                
                 try{
                
                     atualizacaoDt = obCertificadosImportar.getData();                
                    
                    if (atualizacaoDt==null){
                        //System.out.println("Erro Não Existe Mais Certificados");
                        break;
                    }
        
                    stCaminho = "/arquivosprojudi/certs/usuarios";
                    //stCaminho = "d:/certs/usuarios";
                    
                    //System.out.println("Lendo o Arquivo " + stCaminho + atualizacaoDt.getCodigoTemp());
                    
                    // CONTEUDO DOS CERTIFICADOS INSERIDOS NO PROJUDI
                    File arquivo = new File(stCaminho + atualizacaoDt.getCodigoTemp());
                    
                    if (!arquivo.exists()) {
                        
                        //System.out.println("Arquivo Não existe..: " + stCaminho + atualizacaoDt.getCodigoTemp());
                        
                        continue;
                    }
                    
                    InputStream inputStream;
                    
                    inputStream = new FileInputStream(arquivo);
                                           
                    byte[] bytes = new byte[(int) arquivo.length()];
                    
                    //qtd Bytes
                    obCertificadosImportar.addQtdBytes(arquivo.length());
                    
                    // Read in the bytes
                    int offset = 0;
                    int numRead = 0;
                    int inTamanhoLeitura = (1024 <= bytes.length) ? 1024 : bytes.length;
                            
                    while ((numRead = inputStream.read(bytes, offset, inTamanhoLeitura)) > 0) {
                        offset += numRead;
                        inTamanhoLeitura = (1024 <= (bytes.length - offset)) ? 1024 : bytes.length - offset;
                    }
                    
                    inputStream.close();
                    
                    atualizacaoDt.setConteudo(bytes);
        
                    atualizacaoDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
                    atualizacaoDt.setIpComputadorLog("Servidor");
        
                    //System.out.println("Certificado ..: " +atualizacaoDt.getId() + " caminho :" + atualizacaoDt.getCodigoTemp());
                    
                    atualizacaoDt.setCodigoTemp("");
                    
                    atualizacaoNe.migracaoConteudoArquivoP12((CertificadoDt) atualizacaoDt);
                    
                    } catch(FileNotFoundException e) {
                        
                        //System.out.println("ErroArquivo o Arquivo " + stCaminho + atualizacaoDt.getCodigoTemp());
                        ////System.out.println("ERRO....: AquivosImportarRunnable.run() " + e.getMessage(), e);
                    }catch(IOException e) {
                        
                        //System.out.println("ErroArquivo o Arquivo " + stCaminho + atualizacaoDt.getCodigoTemp());
                       ////System.out.println("ERRO....: AquivosImportarRunnable.run() " + e.getMessage(), e);
                    }catch(Exception e) {
                        //System.out.println("ErroGeral :  | "  + stCaminho + atualizacaoDt.getCodigoTemp() + " | " + e.getMessage());                
                    }   
        
                    obCertificadosImportar.aumentarPronto();
                
                                                       
            } while (obCertificadosImportar.isContinuar());
                         
        
            obCertificadosImportar.removeThread();

    }

    
}
