package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
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
	private JTextField textFieldIp;
	private JTextField textFieldPuerto;
	private JTextField textFieldNickname;

	/**
	 * Launch the application.
	 */
	
	  public static void main(String[] args) { 
		  //TESTING
		  
		  EventQueue.invokeLater(new Runnable() { public void run() { try { VentanaConfiguracion frame = new VentanaConfiguracion("Sistema de mensajeria instantanea",new ControladorConfiguracion());
		  //frame.setVisible(true);
		  } catch (Exception e) {
			  e.printStackTrace(); }
		  } }
		  );
	  }
	 

	/**
	 * Create the frame.
	 */
	public VentanaConfiguracion(String titulo,ControladorConfiguracion controlador) {
		this.controlador = controlador;
		
		this.setMinimumSize(new Dimension(250,175));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 314, 211);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setTitle(titulo);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelS = new JPanel();
		contentPane.add(panelS, BorderLayout.SOUTH);
		
		JButton btnIngresar = new JButton("Ingresar");
		panelS.add(btnIngresar);
		btnIngresar.addActionListener(controlador);
		btnIngresar.setActionCommand("INGRESAR");
		
		JPanel panel_6 = new JPanel();
		contentPane.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new GridLayout(0, 2, 3, 0));
		
		JPanel panelIp = new JPanel();
		panel_6.add(panelIp);
		
		JLabel lblIp = new JLabel("IP");
		panelIp.add(lblIp);
		
		JPanel panelTxtIp = new JPanel();
		panel_6.add(panelTxtIp);
		
		textFieldIp = new JTextField();
		textFieldIp.setColumns(10);
		panelTxtIp.add(textFieldIp);
		
		JPanel panelPuerto = new JPanel();
		panel_6.add(panelPuerto);
		
		JLabel lblPuerto = new JLabel("Puerto");
		panelPuerto.add(lblPuerto);
		
		JPanel panelTxtPuerto = new JPanel();
		panel_6.add(panelTxtPuerto);
		
		textFieldPuerto = new JTextField();
		textFieldPuerto.setColumns(10);
		panelTxtPuerto.add(textFieldPuerto);
		
		JPanel panelNickname = new JPanel();
		panel_6.add(panelNickname);
		
		JLabel lblNickname = new JLabel("Nickname");
		panelNickname.add(lblNickname);
		
		JPanel panelTxtNickname = new JPanel();
		panel_6.add(panelTxtNickname);
		
		textFieldNickname = new JTextField();
		textFieldNickname.setColumns(10);
		panelTxtNickname.add(textFieldNickname);
	}
	
	public String getIp() {
		return this.textFieldIp.getText();
	}
	
	public String getPuerto() {
		return this.textFieldPuerto.getText();
	}
	
	public String getNickname() {
		return this.textFieldNickname.getText();
	}
}
