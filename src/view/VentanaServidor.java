package view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utils.Utils;

import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

public class VentanaServidor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel startPanel;
	private JLabel infoLabel;

	public VentanaServidor() {
		super("Servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		this.startPanel = new JPanel();
		startPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel startLabel = new JLabel("Servidor en Ejecución ...");
		startLabel.setHorizontalAlignment(SwingConstants.CENTER);
		startLabel.setFont(new Font("Calibri", Font.BOLD, 24));
		this.startPanel.add(startLabel);
		
		this.infoLabel = new JLabel("escuchando en puerto: "+Utils.PUERTO_SERVER);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.infoLabel.setFont(new Font("Calibri",Font.PLAIN, 16));
		this.startPanel.add(infoLabel);
		contentPane.add(startPanel);
	}
	
}
