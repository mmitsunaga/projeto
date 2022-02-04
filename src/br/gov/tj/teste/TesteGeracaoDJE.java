package br.gov.tj.teste;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.go.tj.projudi.dt.MovimentacaoIntimacaoDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.utils.ConexaoBD;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * Classe responsável em gerar o arquivo pdf do diário eletrônico
 * @author mmitsunaga
 *
 */
public class TesteGeracaoDJE {
	
	/**
	 * Modo de usar: Especificar a data de publicação e opção de geração. Os arquivos serão gerados em D:\
	 */
	
	private final static int POOL_SIZE = 25;
	
	private final int CODIGO_OPCAO_PUBLICACAO_2_GRAU = 1;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL = 2;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR = 3;
	
	private final String dataPublicacao = "23/04/2021";
	
	private final int opcaoPublicacao = 2;
	
	private FabricaConexao fabricaConexaoConsulta = null;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		OracleDataSource dataSourceOracle = new OracleDataSource();
		dataSourceOracle.setURL("jdbc:oracle:thin:@sv-bdo-h00.tjgo.ldc:1521/homolog.tjgo.gov");
		dataSourceOracle.setUser("projudi");
		dataSourceOracle.setPassword("oracle");
		ConexaoBD.setDatasourceConsulta(dataSourceOracle);
		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
		
	}
	
	@Test
	public void init(){
		String dataInicial = dataPublicacao + " 00:00:00";
		String dataFinal = dataPublicacao + " 23:59:59";		
		try {
			System.out.println("Consultando dados para o filtro: [DataInicio=" + dataInicial + ", DataFim: " + dataFinal + ", Opcao=" + opcaoPublicacao + "]");
			PendenciaNe pendenciaNe = new PendenciaNe();		
			List<MovimentacaoIntimacaoDt> intimacoes = pendenciaNe.consultarIntimacoesParaPublicacao(dataInicial, dataFinal, opcaoPublicacao);
			if (intimacoes.size() != 0){
				System.out.println("Iniciando a geração do arquivo: " + intimacoes.size() + " registros encontrados...");				
				FileOutputStream fos = new FileOutputStream(new File("D:/" + definirNomeArquivoPDF(dataPublicacao, opcaoPublicacao)));				
				HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) new CssAppliersImpl(new XMLWorkerFontProvider()));				
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				Document document = new Document(PageSize.A4, 50, 50, 50, 50);
				try {					
					int index = 0;
					int count = 0;
					this.fabricaConexaoConsulta = new FabricaConexao(FabricaConexao.CONSULTA);					
					String serventia = "";					
					ArrayList<HashMap<String, Object>> bookmarks = new ArrayList<HashMap<String, Object>>();					
					PdfSmartCopy copy = new PdfSmartCopy(document, bos);					
					document.open();					
					for (MovimentacaoIntimacaoDt m : intimacoes){
						try {
							if (count < POOL_SIZE){
					    		count++;
					    	} else {
					    		count = 0;
					    		fabricaConexaoConsulta.fecharConexao();
					    		fabricaConexaoConsulta = new FabricaConexao(FabricaConexao.CONSULTA);					    	
					    		bos.flush();
					    		fos.flush();
					    	}							
							pendenciaNe.gerarPdfIntimacaoPorData(m, hpc, "D:/Projetos/trabalho/projudi/WebContent/", copy, bookmarks, (!serventia.equalsIgnoreCase(m.getProcessoDt().getServentia())));
							serventia = m.getProcessoDt().getServentia();							
						} catch (Exception ex){
							continue;
						}						
						index++;						
						System.out.println(index + "/" + intimacoes.size() + " - " + m.getProcessoDt().getProcessoNumeroCompleto() + " ... ok");						
					}					
					copy.setOutlines(bookmarks);
					document.close();
					try{if (document!=null) document.close(); } catch(Exception ex ) {};
					System.out.println("Terminado.");
				} catch (Exception e){
					try{if (bos!=null) bos.close(); } catch(Exception ex ) {};
					throw e;
				}
				fos.flush();
				fos.close();	
				bos.close();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (this.fabricaConexaoConsulta != null)
				try {
					this.fabricaConexaoConsulta.fecharConexao();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private String definirNomeArquivoPDF(String dataPublicacao, int opcaoPublicacao){
		String tipo = "";
		if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_2_GRAU){
			tipo = "_SI_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL){
			tipo = "_SII_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR){
			tipo = "_SIII_";
		}
		return "Intimacao" + tipo + Funcoes.FormatarDataSemBarra(dataPublicacao) + ".pdf";
	}
	
}


// Aplicação Desktop para gerar os arquivos.
// Exportar o projeto projudi com esta classe indicada como inicial
// copiar a pasta webcontent/images para o mesmo direitorio do jar executavel
/*public class DJEForms extends javax.swing.JFrame implements PropertyChangeListener {

   private static final long serialVersionUID = 7886009259033513099L;
   
   private final static int POOL_SIZE = 50;
   
   private final int CODIGO_OPCAO_PUBLICACAO_2_GRAU = 1;	
   private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL = 2;	
   private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR = 3;
   
   public DJEForms() {
    initComponents();
    txtConsole.append(getDiretorioWebContent() + "\n");		
   }

   @SuppressWarnings("unchecked")                             
    private void initComponents() {

    	jfcSaida = new javax.swing.JFileChooser();
        btnBordaExterna = new javax.swing.JPanel();	
        lblData = new javax.swing.JLabel();
        txtDataIntimacao = new javax.swing.JFormattedTextField();
        lblOpcao = new javax.swing.JLabel();
        cboOpcao = new javax.swing.JComboBox<>();
        lblCaminhoSaida = new javax.swing.JLabel();
        txtCaminhoSaida = new javax.swing.JTextField();
        btnSelecionarPastaSaida = new javax.swing.JButton();
        pgBar = new javax.swing.JProgressBar();
        scpConsole = new javax.swing.JScrollPane();
        txtConsole = new javax.swing.JTextArea();
        btnCancelar = new javax.swing.JButton();
        btnIniciar = new javax.swing.JButton();
        lblTempo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TJGO - Diário de Justiça Eletrônico - Gerador de Arquivos");
        setResizable(false);

        btnBordaExterna.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblData.setText("Informe a data da intimação:");

        try {
            txtDataIntimacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblOpcao.setText("Opção de geração:");
        lblOpcao.setToolTipText("");

        cboOpcao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas as Publicações", "Publicações de 2o Grau", "Publicações de 1o Grau - Capital", "Publicações de 1o Grau - Interior" }));

        lblCaminhoSaida.setText("Informe a pasta onde os arquivos serão salvos:");

        txtCaminhoSaida.setEditable(false);

        btnSelecionarPastaSaida.setText("Selecione");
        btnSelecionarPastaSaida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarPastaSaidaActionPerformed(evt);
            }
        });

        pgBar.setStringPainted(true);

        txtConsole.setEditable(false);
        txtConsole.setBackground(new java.awt.Color(0, 0, 0));
        txtConsole.setColumns(20);
        txtConsole.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        txtConsole.setForeground(new java.awt.Color(255, 255, 255));
        txtConsole.setRows(5);
        scpConsole.setViewportView(txtConsole);

        javax.swing.GroupLayout btnBordaExternaLayout = new javax.swing.GroupLayout(btnBordaExterna);
        btnBordaExterna.setLayout(btnBordaExternaLayout);
        btnBordaExternaLayout.setHorizontalGroup(
            btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnBordaExternaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scpConsole)
                    .addGroup(btnBordaExternaLayout.createSequentialGroup()
                        .addGroup(btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDataIntimacao)
                            .addComponent(lblData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(btnBordaExternaLayout.createSequentialGroup()
                                .addComponent(lblOpcao)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cboOpcao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pgBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(btnBordaExternaLayout.createSequentialGroup()
                        .addGroup(btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaminhoSaida)
                            .addGroup(btnBordaExternaLayout.createSequentialGroup()
                                .addComponent(txtCaminhoSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelecionarPastaSaida)))
                        .addGap(0, 36, Short.MAX_VALUE)))
                .addContainerGap())
        );
        btnBordaExternaLayout.setVerticalGroup(
            btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnBordaExternaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblData)
                    .addComponent(lblOpcao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDataIntimacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboOpcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCaminhoSaida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(btnBordaExternaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaminhoSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelecionarPastaSaida))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpConsole, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgBar, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnCancelar.setText("Fechar");
        btnCancelar.setActionCommand("btnFechar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnIniciar.setText("iniciar");
        btnIniciar.setActionCommand("btnIniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        
        lblTempo.setFont(new java.awt.Font("Dialog", 1, 10));
        lblTempo.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBordaExterna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIniciar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBordaExterna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTempo))
                .addContainerGap())
        );

        pack();
    }                        

    private void btnSelecionarPastaSaidaActionPerformed(java.awt.event.ActionEvent evt) {                                                        
        jfcSaida = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    	jfcSaida.setDialogTitle("Escolha o diretório de gravação dos arquivos de saída:");
    	jfcSaida.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);        
        int returnValue = jfcSaida.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfcSaida.getSelectedFile().isDirectory()) {
            	txtCaminhoSaida.setText(jfcSaida.getSelectedFile().toString());                
            }
        }
    }                                                       

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        
        inicializarControles();
        
        String dataIntimacaoSemFormatacao = txtDataIntimacao.getText().replaceAll("/","");
    	
    	if (ValidacaoUtil.isVazio(dataIntimacaoSemFormatacao)){
    		JOptionPane.showMessageDialog(null, "Por favor, informe a data de intimação dos processos!!");
    		txtDataIntimacao.requestFocus();
    	
    	} else if (cboOpcao.getSelectedIndex() <= 0){
    		JOptionPane.showMessageDialog(null, "Por favor, escolha uma opção de geração !!");
    		cboOpcao.requestFocus();
    	    		
    	} else if (ValidacaoUtil.isVazio(txtCaminhoSaida.getText())){
    		JOptionPane.showMessageDialog(null, "Por favor, informe a pasta onde os arquivos serão gravados !!");
            btnSelecionarPastaSaida.requestFocus();
            
    	} else {    		
    		travarControles();
    		task = new Task();
    		task.addPropertyChangeListener(this);
    		task.execute();	    	
    	}
    	
    }                                          

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }              
    
    private void inicializarControles() {    	
    	btnIniciar.setEnabled(true);
    	btnCancelar.setText("Fechar");    	
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
    }
        
    private void travarControles() {
    	lblTempo.setText("");
    	txtConsole.setText("");
    	btnIniciar.setEnabled(false);
    	btnCancelar.setText("Cancelar");
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private String definirNomeArquivoPDF(String dataPublicacao, int opcaoPublicacao){
		String tipo = "";
		if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_2_GRAU){
			tipo = "_SI_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL){
			tipo = "_SII_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR){
			tipo = "_SIII_";
		}
		return "Intimacao" + tipo + Funcoes.FormatarDataSemBarra(dataPublicacao) + ".pdf";
	}
    
    private static String getDataHoraLog(){
    	DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formato.format(new Date());
	}
    
    private String getTempoExecucao(long inicio){
    	long millis = System.currentTimeMillis() - inicio;
    	return String.format("%d min, %d seg", 
    			TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
		);
    }
    
    @Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			pgBar.setValue(progress);
		}		
	}
    
    private String getDiretorioWebContent(){    	
    	 try {
			return new File(".").getCanonicalPath() + File.separator + "WebContent" + File.separator;
		} catch (IOException e) {			
			e.printStackTrace();
		}
    	return "";
    }
    
    class Task extends SwingWorker<Void, Void> {
    	
    	private String dataPublicacao = "";
    	
    	private int opcaoPublicacao = -1;
    	
    	private String pastaSaida = "";
    	
    	private FabricaConexao fabricaConexaoConsulta = null;
    	    	
    	@Override
    	protected Void doInBackground() throws Exception {
    		
    		OracleDataSource dataSourceOracle = new OracleDataSource();	
    		dataSourceOracle.setURL("jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.200.119)(PORT = 1521)) (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.200.119)(PORT = 1521)) (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = DG_PROJUDI) (UR=A) ) )");
    		dataSourceOracle.setUser("mmitsunaga");
    		dataSourceOracle.setPassword("Ak8642*!");
    		ConexaoBD.setDatasourceConsulta(dataSourceOracle);
    		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
    		
    		dataPublicacao = txtDataIntimacao.getText();
    		opcaoPublicacao = cboOpcao.getSelectedIndex();
    		pastaSaida = txtCaminhoSaida.getText() + File.separator;
    		
    		String dataInicial = dataPublicacao + " 00:00:00";
    		String dataFinal = dataPublicacao + " 23:59:59";
    		
    		long tempoInicial = System.currentTimeMillis();
    		       		    		
    		try {
    			txtConsole.append(getDataHoraLog() + " Filtro: [DataInicio=" + dataInicial + ", DataFim: " + dataFinal + ", Opcao=" + opcaoPublicacao + "]\n");
    			PendenciaNe pendenciaNe = new PendenciaNe();		
    			List<MovimentacaoIntimacaoDt> intimacoes = pendenciaNe.consultarIntimacoesParaPublicacao(dataInicial, dataFinal, opcaoPublicacao);    			
    			setProgress(0);
    			int total = intimacoes.size();    			
    			if (intimacoes.size() != 0){
    				txtConsole.append(getDataHoraLog() + " Iniciando a geração do arquivo: " + intimacoes.size() + " registros encontrados...\n");				
    				FileOutputStream fos = new FileOutputStream(new File(pastaSaida + definirNomeArquivoPDF(dataPublicacao, opcaoPublicacao)));				
    				HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) new CssAppliersImpl(new XMLWorkerFontProvider()));				
    				BufferedOutputStream bos = new BufferedOutputStream(fos);
    				Document document = new Document(PageSize.A4, 50, 50, 50, 50);
    				try {					
    					int index = 0;
    					int count = 0;
    					int progress = 0;    					    					
    					this.fabricaConexaoConsulta = new FabricaConexao(FabricaConexao.CONSULTA);					
    					String serventia = "";					
    					ArrayList<HashMap<String, Object>> bookmarks = new ArrayList<HashMap<String, Object>>();					
    					PdfSmartCopy copy = new PdfSmartCopy(document, bos);					
    					document.open();					
    					for (MovimentacaoIntimacaoDt m : intimacoes){
    						try {
    							if (count < POOL_SIZE){
    					    		count++;
    					    	} else {
    					    		count = 0;
    					    		fabricaConexaoConsulta.fecharConexao();
    					    		fabricaConexaoConsulta = new FabricaConexao(FabricaConexao.CONSULTA);					    	
    					    		bos.flush();
    					    		fos.flush();
    					    	}							
    							pendenciaNe.gerarPdfIntimacaoPorData(m, hpc, getDiretorioWebContent(), copy, bookmarks, (!serventia.equalsIgnoreCase(m.getProcessoDt().getServentia())));
    							serventia = m.getProcessoDt().getServentia();							
    						} catch (Exception ex){
    							continue;
    						}						    						
    						index++;						    						
    						progress = (index * 100) / total;
    						setProgress(Math.min(progress, 100));    						
    						txtConsole.append(getDataHoraLog() + " - " + index + "/" + intimacoes.size() + " - " + m.getProcessoDt().getProcessoNumeroCompleto() + "\n");						
    					}					
    					copy.setOutlines(bookmarks);
    					document.close();
    					try{if (document!=null) document.close(); } catch(Exception ex ) {};
    					txtConsole.append(getDataHoraLog() + " Terminado.");
    				} catch (Exception e){
    					try{if (bos!=null) bos.close(); } catch(Exception ex ) {};
    					throw e;
    				}
    				fos.flush();
    				fos.close();	
    				bos.close();
    			}
    			
    		} catch (Exception e) {    			
    			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		}    		
    		
    		lblTempo.setText("Tempo gasto na geração: " + getTempoExecucao(tempoInicial));    		
    		
    		return null;    		
    	}
    	
    	@Override
    	protected void done() {
    		setProgress(0);
			inicializarControles();
    	}
    }
        
    public static void main(String args[]) {        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DJEForms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DJEForms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DJEForms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DJEForms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

         Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {            	
                new DJEForms().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify 
    private Task task;
    private javax.swing.JPanel btnBordaExterna;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnSelecionarPastaSaida;
    private javax.swing.JComboBox<String> cboOpcao;
    private javax.swing.JFileChooser jfcSaida;
    private javax.swing.JLabel lblCaminhoSaida;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblOpcao;
    private javax.swing.JLabel lblTempo;
    private javax.swing.JProgressBar pgBar;
    private javax.swing.JScrollPane scpConsole;
    private javax.swing.JTextField txtCaminhoSaida;
    private javax.swing.JTextArea txtConsole;
    private javax.swing.JFormattedTextField txtDataIntimacao;
    // End of variables declaration                   
}*/



