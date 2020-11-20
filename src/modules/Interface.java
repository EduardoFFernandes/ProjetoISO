package modules;

import static util.Constantes.ARQUIVOS;
import static util.Constantes.INICIAR;
import static util.Constantes.JAVA_PNG;
import static util.Constantes.MENU;
import static util.Constantes.PROCESSOS;
import static util.Constantes.SELECIONAR;
import static util.Constantes.SELECIONAR_CANCELADO;
import static util.Constantes.TITULO;
import static util.Constantes.TXT;

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

import pseudoSO.PseudoSO;

/**
 * Interface do sistema e terminal.
 * 
 */
public class Interface extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private PseudoSO mainListener;
	private String icone = JAVA_PNG;
	private DefaultStyledDocument terminalView;
	private JTextPane painelTerminal;
	private JScrollPane scrollTerminal;
	private JScrollBar scrollVertical;
	private JFileChooser selecionador;

	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu(MENU);
	JMenuItem itemAddProcesso, itemAddArquivo, iniciar;

	private StyleContext contextoDeEstilo;
	private Style estiloTerminal;

	/**
	 * Atribui o objeto Main e os parametros para a Interface.
	 * 
	 * 
	 */
	public Interface(PseudoSO pseudoSO) throws BadLocationException {
		this.mainListener = pseudoSO;
		initialize();
	}

	/**
	 * Esse metodo vem da interface ActionListener foi sobreescrevido para lidar com
	 * os eventos dentro da interface, e aqui que e tratado a questão de selecionar
	 * os arquivos e iniciar o Pseudo SO.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent source) {
		switch (source.getActionCommand()) {
		case PROCESSOS:
			File processo = selecionaArquivo((JMenuItem) source.getSource());
			if (processo != null) {
				mainListener.valida(processo, source.getActionCommand());
			} else {
				mainListener.invalida(source.getActionCommand());
				logMessage(SELECIONAR_CANCELADO);
			}
			break;
		case ARQUIVOS:
			File arquivo = selecionaArquivo((JMenuItem) source.getSource());
			if (arquivo != null) {
				mainListener.valida(arquivo, source.getActionCommand());
			} else {
				mainListener.invalida(source.getActionCommand());
				logMessage(SELECIONAR_CANCELADO);
			}
			break;
		case INICIAR:
			mainListener.iniciar();
			break;
		default:
			break;
		}
	}

	/**
	 * Esse metodo controla tudo que sera escrito no terminal da aplicacao.
	 * 
	 */
	synchronized public void logMessage(String texto) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					StyleConstants.setForeground(estiloTerminal, Color.WHITE);
					terminalView.insertString(terminalView.getLength(), texto + "\n", estiloTerminal);
					revalidate();
					scrollVertical.setValue(scrollVertical.getMaximum() + 1);
					revalidate();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Metodo que abre o dialogo de busca de arquivos.
	 */
	public File selecionaArquivo(Component component) {
		selecionador = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(null, TXT);
		selecionador.setFileFilter(filtro);
		int retorno = selecionador.showDialog(component, SELECIONAR);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			return selecionador.getSelectedFile();
		} else {
			return null;
		}
	}
	
	/**
	 * Atribui os valores da Interface do sistema.
	 * 
	 * 
	 */
	private void initialize() throws BadLocationException {

		// TERMINAL
		terminalView = new DefaultStyledDocument();
		painelTerminal = new JTextPane(terminalView);
		scrollTerminal = new JScrollPane(painelTerminal);
		scrollVertical = scrollTerminal.getVerticalScrollBar();

		// MENU
		iniciar = new JMenuItem(INICIAR);
		iniciar.addActionListener(this);
		iniciar.setActionCommand(INICIAR);
		menu.add(iniciar);

		itemAddProcesso = new JMenuItem(PROCESSOS);
		itemAddProcesso.addActionListener(this);
		itemAddProcesso.setActionCommand(PROCESSOS);
		menu.add(itemAddProcesso);

		itemAddArquivo = new JMenuItem(ARQUIVOS);
		itemAddArquivo.addActionListener(this);
		itemAddArquivo.setActionCommand(ARQUIVOS);
		menu.add(itemAddArquivo);
		menuBar.add(menu);

		contextoDeEstilo = new StyleContext();
		estiloTerminal = contextoDeEstilo.addStyle(null, null);

		painelTerminal.setEditable(false);
		painelTerminal.setPreferredSize(new Dimension(200, 200));
		painelTerminal.setBackground(Color.black);

		// JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(TITULO);
		setJMenuBar(menuBar);
		getContentPane().add(scrollTerminal, BorderLayout.CENTER);
		setMinimumSize(new Dimension(500, 300));
		setDefaultLookAndFeelDecorated(true);

		// ICONE
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(icone)));
		pack();

	}
}
