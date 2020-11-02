package modules;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPanel;
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

/**
 * Classe que implementa a tela de visualização do Dispacher
 * 
 * @author tulio.matias		-	06/06/2018
 */
public class Interface extends JFrame implements ActionListener {
	
	private Main mainListener;

	private static final long serialVersionUID = 1L;
	private DefaultStyledDocument terminalView;
	private JTextPane painelTerminal;
	private JScrollPane scrollTerminal;
	private JScrollBar scrollVertical;
	private JPanel painelBotoes;
	private JFileChooser selecionador;

	private JButton botaoAddProcesso;
	private JButton botaoAddArquivo;
	private JButton botaoIniciarSO;

	private StyleContext contextoDeEstilo;
	private Style estiloTerminal;

	
	public final static Color DARK_GREEN = new Color(0, 153,0);
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
	 * Inicializa o conteúdo da tela
	 * 
	 * @throws BadLocationException
	 */
	private void initialize() throws BadLocationException {
		terminalView = new DefaultStyledDocument();
		painelTerminal = new JTextPane(terminalView);
		scrollTerminal = new JScrollPane(painelTerminal);
		scrollVertical = scrollTerminal.getVerticalScrollBar();
		painelBotoes = new JPanel();
		contextoDeEstilo = new StyleContext();
		Dimension btnTamanho = new Dimension(100, 40);
		botaoAddProcesso = new JButton(Constantes.BOTAO_ADICIONAR_PROCESSO);
		botaoAddArquivo = new JButton(Constantes.BOTAO_ADICIONAR_ARQUIVOS);
		botaoIniciarSO = new JButton(Constantes.BOTAO_INICIAR);
		// cria o estilo do terminal
		estiloTerminal = contextoDeEstilo.addStyle("estiloTerminal", null);

		painelTerminal.setEditable(false);
		
		botaoAddProcesso.addActionListener(this);
		botaoAddProcesso.setActionCommand(Main.ARQ_PROCESSOS);
		botaoAddArquivo.addActionListener(this);
		botaoAddArquivo.setActionCommand(Main.ARQ_OPERACAO);
		botaoIniciarSO.addActionListener(this);
		botaoIniciarSO.setActionCommand(Main.INICIAR);
		botaoAddProcesso.setPreferredSize(btnTamanho);
		botaoAddArquivo.setPreferredSize(btnTamanho);
		botaoIniciarSO.setPreferredSize(btnTamanho);
		painelTerminal.setPreferredSize(new Dimension(200, 200));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(Constantes.TELA_PRINCIPAL_TITULO);
		getContentPane().add(scrollTerminal, BorderLayout.CENTER);
		getContentPane().add(painelBotoes, BorderLayout.NORTH);
		painelBotoes.add(botaoAddProcesso);
		painelBotoes.add(botaoAddArquivo);
		painelBotoes.add(botaoIniciarSO);
		setMinimumSize(new Dimension(500, 300));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("java.png")));
		this.pack();

	}

	
	/**
	 *  Método para verificar qual botão foi pressionado
	 * @see ActionEvent
	 * */
	@Override
	public void actionPerformed(ActionEvent source) {
//		public static final String ARQ_PROCESSOS = "Processos";
//		public static final String ARQ_OPERACAO = "Arquivos";
//		public static final String INICIAR = "Iniciar";
		String sourceName = source.getActionCommand();
		switch (source.getActionCommand()) {
		case Main.ARQ_PROCESSOS:
			mainListener.valida
			break;
		case Main.ARQ_OPERACAO:
			break;
		case Main.INICIAR:
			break;
		default:
			break;
		}
		if(!sourceName.equals(Main.INICIAR)){
			File resposta = selecionaArquivo((JButton) source.getSource());
			if(resposta != null){	
				mainListener.validaArquivo(resposta,sourceName);
			}else{
				mainListener.invalidaArquivo(sourceName);
				printaNoTerminal(Constantes.SELECIONAR_CANCELADO,RED);
			}		
		}else{
			mainListener.iniciar();		
		}
	}


	/**
	 * Escreve no terminal a String recebida com a cor selecionada
	 * 
	 * @param	texto	texto a ser escrito no terminal
	 * @param	cor		cor a ser colocada no terminal
	 * */
	synchronized public void printaNoTerminal(String texto, Color cor){
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
	 * Escreve no terminal a String recebida com a cor padrão
	 * 
	 * @param	texto	texto a ser escrito no terminal
	 * */
	public void printaNoTerminal(String texto){
		printaNoTerminal(texto,Color.BLACK);
	}
	
	/**
	 * Abre o selecionador de arquivos do Java e mostra apenas os com extensão txt.
	 * 
	 * @param	botao	botao clicado na tela
	 * @return	Um arquivo se o filepicker voltou com sucesso, null caso contrário
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
