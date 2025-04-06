package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.ControladorConfiguracion;
import utils.Utils;

public class VentanaConfiguracion extends JFrame {

	private ControladorConfiguracion controlador;
	private String mode;
	
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
		  
		  EventQueue.invokeLater(new Runnable() { public void run() { 
		  try { VentanaConfiguracion frame = new VentanaConfiguracion("Sistema de mensajeria instantanea",new ControladorConfiguracion(),Utils.MODO_CONFIG);
		  //frame.setVisible(true);
		  } catch (Exception e) {
			  e.printStackTrace(); }
		  } }
		  );
	  }
	 


	/**
	 * Crea una instancia de JFrame de configuracion de usuario o para agregar un contacto
	 * 
	 * @param titulo: 
	 * @param controlador
	 * @param mode
	 */
	public VentanaConfiguracion(String titulo,ControladorConfiguracion controlador,String mode) {
		this.controlador = controlador;
		this.mode = mode;
		
		this.setMinimumSize(new Dimension(250,175));
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(mode.equals(Utils.MODO_AGR_CONTACTO))
					controlador.cerrar();
				dispose();
			}
		});
		setBounds(100, 100, 314, 211);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setTitle(titulo);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelS = new JPanel();
		contentPane.add(panelS, BorderLayout.SOUTH);
		
		JButton btnIngresar;
		if (this.mode == Utils.MODO_CONFIG) {
			btnIngresar = new JButton("Ingresar");
			btnIngresar.setActionCommand(Utils.INGRESAR);
		}else {
			btnIngresar = new JButton("Agregar contacto");
			btnIngresar.setActionCommand(Utils.CREAR_CONTACTO);
		}
		
		btnIngresar.addActionListener(this.controlador);
		
		panelS.add(btnIngresar);
		
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
