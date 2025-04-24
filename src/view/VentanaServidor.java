package view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

public class VentanaServidor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField portTextField;
	private JPanel mainPanel;
	private JPanel startPanel;
	private JButton btnStart;
	private JLabel infoLabel;
	private int port;


	public VentanaServidor() {
		super("Servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		this.mainPanel = new JPanel();
		contentPane.add(this.mainPanel);
		this.mainPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel LabelPanel = new JPanel();
		this.mainPanel.add(LabelPanel);
		FlowLayout fl_LabelPanel = (FlowLayout) LabelPanel.getLayout();
		fl_LabelPanel.setVgap(30);
		
		JLabel mainLabel = new JLabel("Configuración del Servidor");
		mainLabel.setFont(new Font("Calibri", Font.BOLD, 18));
		LabelPanel.add(mainLabel);
		
		JPanel panel = new JPanel();
		this.mainPanel.add(panel);
		panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel InputPanel = new JPanel();
		panel.add(InputPanel);
		
		JLabel portLabel = new JLabel("Puerto:");
		portLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		InputPanel.add(portLabel);
		
		this.portTextField = new JTextField();
		InputPanel.add(portTextField);
		this.portTextField.setColumns(10);
		
		JPanel bottonPanel = new JPanel();
		panel.add(bottonPanel);
		
		this.btnStart = new JButton("Iniciar");
		this.btnStart.setActionCommand("INICIAR_SERVIDOR");
		this.btnStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bottonPanel.add(this.btnStart);
		
		this.startPanel = new JPanel();
		startPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel startLabel = new JLabel("Servidor en Ejecución ...");
		startLabel.setHorizontalAlignment(SwingConstants.CENTER);
		startLabel.setFont(new Font("Calibri", Font.BOLD, 24));
		this.startPanel.add(startLabel);
		
		this.infoLabel = new JLabel("escuchando en puerto: "+this.port);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.infoLabel.setFont(new Font("Calibri",Font.PLAIN, 16));
		this.startPanel.add(infoLabel);
	}
	
	public void setActionListener(ActionListener control) {
		this.btnStart.addActionListener(control);
	}

	public void startServer(int port) {
		this.port = port;
		this.infoLabel.setText("escuchando en puerto: "+port);
		contentPane.remove(this.mainPanel);
		contentPane.add(this.startPanel);
		this.revalidate();
		this.repaint();
	}
	
	public int getPort() throws NumberFormatException{
		return Integer.parseInt(this.portTextField.getText());
	}
}
