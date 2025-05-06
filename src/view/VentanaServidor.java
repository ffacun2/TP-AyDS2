package view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utils.Utils;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Component;

public class VentanaServidor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel startPanel;
	private JLabel infoLabel;
	private JLabel startLabel;
	private JPanel panel;
	private JButton btnStart;
	private JButton btnStop;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;

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
		
		this.startLabel = new JLabel("Servidor Detenido...");
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
		
		panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(1, 2, 0, 0));
		
		panel_2 = new JPanel();
		panel_4.add(panel_2);
		
		btnStart = new JButton("Iniciar");
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_2.add(btnStart);
		btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel_5 = new JPanel();
		panel_4.add(panel_5);
		
		btnStop = new JButton("Finalizar");
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_5.add(btnStop);
	}
	
	
	public void setInfoLabel(String text) {
		this.infoLabel.setText(text);
	}
	
	public void setStartLabel(String text) {
		this.startLabel.setText(text);
	}
	
	public void setControlador(ActionListener control) {
		this.btnStart.addActionListener(control);
		this.btnStart.setActionCommand(Utils.INICIAR_SERVER);
		this.btnStop.addActionListener(control);
		this.btnStop.setActionCommand(Utils.DETENER_SERVER);
	}
	
}
