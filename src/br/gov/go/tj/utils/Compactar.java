package br.gov.go.tj.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Compactar {

	private static final int TAMANHO_BUFFER = 2048; // 2 bytes
	
	    public byte[] descompactarBytes(byte[] bytes) { 
	             Inflater decompressor = new Inflater();
	               
	             decompressor.setInput(bytes);  
	               
	             ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);  
	               
	             // Decompress the data  
	             byte[] buf = new byte[1024];  
	             while (!decompressor.finished()) { 
	                 try{ 
	                     int count = decompressor.inflate(buf);  
	                     bos.write(buf, 0, count);  
	                 } catch(DataFormatException e) { 
	                 }  
	             }  
	             try{ 
	                 bos.close();  
	                 return bos.toByteArray();  
	             } catch(IOException e) { 
	                 return null;  
	             }  
	     }  
	       
         public byte[] compactarBytes(byte[] bytes) { 
             Deflater compressor = new Deflater();  
             compressor.setLevel(Deflater.BEST_COMPRESSION);  
               
             compressor.setInput(bytes);  
             compressor.finish();  
               
             ByteArrayOutputStream bos = new  ByteArrayOutputStream(bytes.length);  
               
             byte[] buf = new byte[1024];  
             while (!compressor.finished()) { 
                 int count = compressor.deflate(buf);  
                 bos.write(buf, 0, count);  
             }  
             try{ 
                 bos.close();  
                 return bos.toByteArray();             
             } catch(Exception e) { 
                 ////System.out.println("Erro: "+e.getMessage());  
                 e.printStackTrace();
                 return null;  
             }         
             // Get the compressed data                        
         }//fim compactar()     
	    
   

    
	public void criarZip(byte[] dados, File arquivoZip) throws Exception {

		if (dados == null || dados.length < 1) {throw new ZipException("<{Adicione ao menos um byte.}> Local Exception: " + this.getClass().getName() + ".criarZip()"); }

		ZipOutputStream zos = null;
		try{
		    
			zos = new ZipOutputStream(new FileOutputStream(arquivoZip));
			//Nome do arquivo dentro do zip
			String nomeArquivo = arquivoZip.getName();

			ZipEntry entrada = new ZipEntry(nomeArquivo);
			zos.putNextEntry(entrada);
			zos.setMethod(ZipOutputStream.DEFLATED);
			
			zos.write(dados);
			
		
		} finally{
			if (zos != null) {
				try{
					zos.close();
				} catch(Exception e) {
				}
			}
		}

	}
	
	
	public InputStream lerZip(File arquivoZip) throws Exception {

		InputStream is = null;
		
		// cria diretório informado, caso não exista
		if (!arquivoZip.exists()) {throw new MensagemException("Informe um arquivo válido"); }
		try(ZipFile zip = new ZipFile(arquivoZip)){
			Enumeration e = zip.entries();
			while (e.hasMoreElements()) {
				ZipEntry entrada = (ZipEntry) e.nextElement();
	
				// lê o arquivo do zip e grava em disco
				is = zip.getInputStream(entrada);
	
			}
			return is;
		}
	}

	public List listarEntradasZip(File arquivo) throws Exception {
		List entradasDoZip = new ArrayList();
		ZipFile zip = null;
		try{
			zip = new ZipFile(arquivo);
			Enumeration e = zip.entries();
			ZipEntry entrada = null;
			while (e.hasMoreElements()) {
				entrada = (ZipEntry) e.nextElement();
				entradasDoZip.add(entrada);
			}
			setArquivoZipAtual(arquivo);
		
		} finally{
			if (zip != null) {
				zip.close();
			}
		}
		return entradasDoZip;
	}

	public void extrairZip(File diretorio) throws Exception{
		extrairZip(this.getArquivoZipAtual(), diretorio);
	}

	public void extrairZip(File arquivoZip, File diretorio) throws Exception {
		ZipFile zip = null;
		File arquivo = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[TAMANHO_BUFFER];
		try{
			// cria diretório informado, caso não exista
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			if (!diretorio.exists() || !diretorio.isDirectory()) {throw new MensagemException("Informe um diretório válido."); }
			zip = new ZipFile(arquivoZip);
			Enumeration e = zip.entries();
			while (e.hasMoreElements()) {
				ZipEntry entrada = (ZipEntry) e.nextElement();
				arquivo = new File(diretorio, entrada.getName());
				// se for diretório inexistente, cria a estrutura
				// e pula pra próxima entrada
				if (entrada.isDirectory() && !arquivo.exists()) {
					arquivo.mkdirs();
					continue;
				}
				// se a estrutura de diretórios não existe, cria
				if (!arquivo.getParentFile().exists()) {
					arquivo.getParentFile().mkdirs();
				}
				try{
					// lê o arquivo do zip e grava em disco
					is = zip.getInputStream(entrada);
					os = new FileOutputStream(arquivo);
					int bytesLidos = 0;
					if (is == null) {throw new MensagemException("Erro ao ler a entrada do zip: " + entrada.getName()); }
					while ((bytesLidos = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesLidos);
					}
				} finally{
					if (is != null) {
						try{
							is.close();
						} catch(Exception ex) {
						}
					}
					if (os != null) {
						try{
							os.close();
						} catch(Exception ex) {
						}
					}
				}
			}		
		} finally{
			try{if (zip!=null) zip.close(); } catch(Exception ex ) {};
			try{if (os!=null) os.close(); } catch(Exception ex ) {};
			try{if (is!=null) is.close(); } catch(Exception ex ) {};
		}
	}

	public List criarZip(File arquivoZip, File[] arquivos) throws Exception {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		setArquivoZipAtual(null);
		try{
			// adiciona a extensão .zip no arquivo, caso não exista
			if (!arquivoZip.getName().toLowerCase().endsWith(".zip")) {
				arquivoZip = new File(arquivoZip.getAbsolutePath() + ".zip");
			}
			fos = new FileOutputStream(arquivoZip);
			bos = new BufferedOutputStream(fos, TAMANHO_BUFFER);
			List listaEntradasZip = criarZip(bos, arquivos);
			setArquivoZipAtual(arquivoZip);
			return listaEntradasZip;
		
		} finally{
			if (bos != null) {
				try{
					bos.close();
				} catch(Exception e) {
				}
			}
			if (fos != null) {
				try{
					fos.close();
				} catch(Exception e) {
				}
			}
		}
	}

	public List criarZip(OutputStream os, File[] arquivos) throws Exception {
		if (arquivos == null || arquivos.length < 1) {throw new MensagemException("<{Adicione ao menos um arquivo ou diretório.}> Local Exception: " + this.getClass().getName() + ".criarZip()"); }
		List listaEntradasZip = new ArrayList();
		ZipOutputStream zos = null;
		try{
			zos = new ZipOutputStream(os);
			for (int i = 0; i < arquivos.length; i++) {
				String caminhoInicial = arquivos[i].getParent();
				List novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
				if (novasEntradas != null) {
					listaEntradasZip.addAll(novasEntradas);
				}
			}
		} catch(Exception e) {
			try{if (zos!=null) zos.close(); } catch(Exception ex ) {};
			throw e;
		} finally{
			if (zos != null) {
				try{
					zos.close();
				} catch(Exception e) {
				}
			}
		}
		return listaEntradasZip;
	}

	private List adicionarArquivoNoZip(ZipOutputStream zos, File arquivo, String caminhoInicial) throws IOException{
		List listaEntradasZip = new ArrayList();
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		byte buffer[] = new byte[TAMANHO_BUFFER];
		try{
			//diretórios não são adicionados
			if (arquivo.isDirectory()) {
				//recursivamente adiciona os arquivos dos diretórios abaixo
				File[] arquivos = arquivo.listFiles();
				for (int i = 0; i < arquivos.length; i++) {
					List novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
					if (novasEntradas != null) {
						listaEntradasZip.addAll(novasEntradas);
					}
				}
				return listaEntradasZip;
			}
			String caminhoEntradaZip = null;
			int idx = arquivo.getAbsolutePath().indexOf(caminhoInicial);
			if (idx >= 0) {
				//calcula os diretórios a partir do diretório inicial
				//isso serve para não colocar uma entrada com o caminho completo
				caminhoEntradaZip = arquivo.getAbsolutePath();
				caminhoEntradaZip = caminhoEntradaZip.replace(caminhoInicial, "");
			}
			ZipEntry entrada = new ZipEntry(caminhoEntradaZip);
			zos.putNextEntry(entrada);
			zos.setMethod(ZipOutputStream.DEFLATED);
			fis = new FileInputStream(arquivo);
			bis = new BufferedInputStream(fis, TAMANHO_BUFFER);
			int bytesLidos = 0;
			while ((bytesLidos = bis.read(buffer, 0, TAMANHO_BUFFER)) != -1) {
				zos.write(buffer, 0, bytesLidos);
			}
			listaEntradasZip.add(entrada);

		
		} finally{
			if (bis != null) {
				try{
					bis.close();
				} catch(Exception e) {
				}
			}
			if (fis != null) {
				try{
					fis.close();
				} catch(Exception e) {
				}
			}
		}
		return listaEntradasZip;
	}

	public void fecharZip() {
		setArquivoZipAtual(null);
	}

	public File getArquivoZipAtual() {
		return arquivoZipAtual;
	}

	private void setArquivoZipAtual(File arquivoZipAtual) {
		this.arquivoZipAtual = arquivoZipAtual;
	}

	private File arquivoZipAtual;
}
