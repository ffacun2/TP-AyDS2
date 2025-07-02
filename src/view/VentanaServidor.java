package view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import java.awt.GridLayout;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class VentanaServidor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel startPanel;
	private JLabel infoLabel;
	private JLabel startLabel;
	private JPanel panel;
	private JPanel panel_3;

	public VentanaServidor(int puerto) {
		super("Servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		this.startPanel = new JPanel();
		startPanel.setLayout(new GridLayout(3, 1, 0, 0));
		
		this.startLabel = new JLabel("Servidor");
		this.startLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.startLabel.setFont(new Font("Calibri", Font.BOLD, 26));
		this.startPanel.add(startLabel);
		
		this.infoLabel = new JLabel("escuchando en puerto: "+puerto);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.infoLabel.setFont(new Font("Calibri",Font.PLAIN, 16));
		this.startPanel.add(infoLabel);
		contentPane.add(startPanel);
		
		panel = new JPanel();
		startPanel.add(panel);
		panel.setLayout(new GridLayout(2, 2, 0, 0));
		
		panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
	}
	
	public void setWindowsListener(WindowListener listener) {
		this.addWindowListener(listener);
	}
	
	public void setInfoLabel(String text) {
		this.infoLabel.setText(text);
	}
	
	public void setStartLabel(String text) {
		this.startLabel.setText(text);
	}
	
}
