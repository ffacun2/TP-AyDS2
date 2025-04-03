package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.ControladorConfiguracion;

public class VentanaConfiguracion extends JFrame {

	private ControladorConfiguracion controlador;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldIP;
	private JTextField textFieldPuerto;
	private JTextField textFieldNickname;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { VentanaConfiguracion frame = new
	 * VentanaConfiguracion("Sistema de mensajeria instantanea");
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

	/**
	 * Create the frame.
	 */
	public VentanaConfiguracion(String titulo,ControladorConfiguracion controlador) {
		this.controlador = controlador;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 311, 215);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setTitle(titulo);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelN = new JPanel();
		contentPane.add(panelN, BorderLayout.NORTH);
		
		JLabel lblTitulo = new JLabel("Sistema de mensajeria");
		panelN.add(lblTitulo);
		
		JPanel panelS = new JPanel();
		contentPane.add(panelS, BorderLayout.SOUTH);
		
		JButton btnIngresar = new JButton("Ingresar");
		panelS.add(btnIngresar);
		btnIngresar.addActionListener(controlador);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel panelIzq = new JPanel();
		splitPane.setLeftComponent(panelIzq);
		panelIzq.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panelIzq.add(panel);
		
		JLabel lblIp = new JLabel("IP");
		panel.add(lblIp);
		
		JPanel panel_1 = new JPanel();
		panelIzq.add(panel_1);
		
		JLabel lblPuerto = new JLabel("Puerto");
		panel_1.add(lblPuerto);
		
		JPanel panel_2 = new JPanel();
		panelIzq.add(panel_2);
		
		JLabel lblNickname = new JLabel("Nickname");
		panel_2.add(lblNickname);
		
		JPanel panelDer = new JPanel();
		splitPane.setRightComponent(panelDer);
		panelDer.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel_3 = new JPanel();
		panelDer.add(panel_3);
		
		textFieldIP = new JTextField();
		panel_3.add(textFieldIP);
		textFieldIP.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		panelDer.add(panel_4);
		
		textFieldPuerto = new JTextField();
		panel_4.add(textFieldPuerto);
		textFieldPuerto.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panelDer.add(panel_5);
		
		textFieldNickname = new JTextField();
		panel_5.add(textFieldNickname);
		textFieldNickname.setColumns(10);
	}
	
	public String getIp() {
		return this.textFieldIP.getText();
	}
	
	public String getPuerto() {
		return this.textFieldPuerto.getText();
	}
	
	public String getNickname() {
		return this.textFieldNickname.getText();
	}
}
