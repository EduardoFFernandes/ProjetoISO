package modules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.Main;
import util.Constantes;

/**
 * Interface do sistema e terminal.
 * 
 */
public class Interface extends JFrame implements ActionListener {
	
	private Main mainListener;

	private static final long serialVersionUID = 1L;
	private String icone = "java.png";
	private DefaultStyledDocument terminalView;
	private JTextPane painelTerminal;
	private JScrollPane scrollTerminal;
	private JScrollBar scrollVertical;
	private JFileChooser selecionador;
	
	JMenuBar menuBar = new JMenuBar(); 
	JMenu menu = new JMenu("Menu");
	JMenuItem itemAddProcesso, itemAddArquivo, iniciar;

	private StyleContext contextoDeEstilo;
	private Style estiloTerminal;

	
	public final static Color GREEN = new Color(51,169,54);
	public final static Color RED = Color.RED;
	/**
	 * Inicia a tela e recebe o listener da main
	 * @param main 
	 * @throws BadLocationException
	 * @see Jframe
	 */
	public Interface(Main main) throws BadLocationException {
		this.mainListener = main;
		initialize();
	}

	/**
	 * Inicializa o conte�do da tela
	 * 
	 * @throws BadLocationException
	 */
	private void initialize() throws BadLocationException {
		
		// TERMINAL
		terminalView = new DefaultStyledDocument();
		painelTerminal = new JTextPane(terminalView);
		scrollTerminal = new JScrollPane(painelTerminal);
		scrollVertical = scrollTerminal.getVerticalScrollBar();
		
		// MENU
		iniciar = new JMenuItem(Main.INICIAR); 
		iniciar.addActionListener(this);
		iniciar.setActionCommand(Main.INICIAR);
		menu.add(iniciar);
		
		itemAddProcesso = new JMenuItem(Main.PROCESSOS);  
		itemAddProcesso.addActionListener(this);
		itemAddProcesso.setActionCommand(Main.PROCESSOS);
		menu.add(itemAddProcesso); 
		
		itemAddArquivo = new JMenuItem(Main.ARQUIVOS); 
		itemAddArquivo.addActionListener(this);
		itemAddArquivo.setActionCommand(Main.ARQUIVOS);
        menu.add(itemAddArquivo);
        menuBar.add(menu);
		
		contextoDeEstilo = new StyleContext();
		estiloTerminal = contextoDeEstilo.addStyle("a", null);
		
		painelTerminal.setEditable(false);
		painelTerminal.setPreferredSize(new Dimension(200, 200));
		painelTerminal.setBackground(Color.black);
		
		// JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(Constantes.TITULO);
		setJMenuBar(menuBar);
		getContentPane().add(scrollTerminal, BorderLayout.CENTER);
		setMinimumSize(new Dimension(500, 300));
		setDefaultLookAndFeelDecorated(true);
		
		// ICONE
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(icone)));
		pack();

	}

	
	/**
	 *  Metodo para verificar qual botao foi pressionado
	 * @see ActionEvent
	 * */
	@Override
	public void actionPerformed(ActionEvent source) {
		switch (source.getActionCommand()) {
		case Main.PROCESSOS:
			File processo = selecionaArquivo((JMenuItem) source.getSource());
			if(processo != null){
				mainListener.valida(processo,source.getActionCommand());
			} else {
				mainListener.invalida(source.getActionCommand());
				logMessage(Constantes.SELECIONAR_CANCELADO,RED);
			}
			break;
		case Main.ARQUIVOS:
			File arquivo = selecionaArquivo((JMenuItem) source.getSource());
			if(arquivo != null){
				mainListener.valida(arquivo,source.getActionCommand());
			} else {
				mainListener.invalida(source.getActionCommand());
				logMessage(Constantes.SELECIONAR_CANCELADO,RED);
			}
			break;
		case Main.INICIAR:
			mainListener.iniciar();
			break;
		default:
			break;
		}
	}


	/**
	 * Escreve no terminal a String recebida com a cor selecionada
	 * 
	 * @param	texto	texto a ser escrito no terminal
	 * @param	cor		cor a ser colocada no terminal
	 * */
	synchronized public void logMessage(String texto, Color cor){
		EventQueue.invokeLater(new Runnable() {
			
			@Override
				public void run() {
					try {
						StyleConstants.setForeground(estiloTerminal, cor);
						terminalView.insertString(terminalView.getLength(),texto+Constantes.NEWLINE, estiloTerminal);
						revalidate();
						scrollVertical.setValue(scrollVertical.getMaximum()+1);
						revalidate();
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * Escreve no terminal a String recebida com a cor padrao
	 * 
	 * @param	texto	texto a ser escrito no terminal
	 * */
	public void logMessage(String texto){
		logMessage(texto,Color.WHITE);
	}
	
	/**
	 * Abre o selecionador de arquivos do Java e mostra apenas os com extensao txt.
	 * 
	 * @param	botao	botao clicado na tela
	 * @return	Um arquivo se o filepicker voltou com sucesso, null caso contrario
	 * */
	public File selecionaArquivo(Component botao){
		selecionador = new JFileChooser();
	    FileNameExtensionFilter filtro = new FileNameExtensionFilter(null,"txt");//TODO: retirar a string daqui.
	    selecionador.setFileFilter(filtro);
		int retorno = selecionador.showDialog(botao, Constantes.SELECIONAR);
		
        if (retorno == JFileChooser.APPROVE_OPTION) {
            return selecionador.getSelectedFile();
        } else {
        	return null;
        }
	}
}
