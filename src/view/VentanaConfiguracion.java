package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.awt.Component;
import java.awt.Font;

public class VentanaConfiguracion extends JFrame {

	private ControladorConfiguracion controlador;
	private String mode;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNickname;

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
		JButton btnRegistrarse;
		if (this.mode == Utils.MODO_CONFIG) {
			btnIngresar = new JButton("Ingresar");
			btnIngresar.setActionCommand(Utils.INGRESAR);
			
			btnRegistrarse = new JButton("Registrarse");
			btnRegistrarse.setActionCommand(Utils.REGISTRARSE);
			btnRegistrarse.addActionListener(this.controlador);
			panelS.add(btnRegistrarse);
			
		}else {
			btnIngresar = new JButton("Agregar contacto");
			btnIngresar.setActionCommand(Utils.CREAR_CONTACTO);
		}
		
		btnIngresar.addActionListener(this.controlador);
		
		panelS.add(btnIngresar);
		
		JPanel panel_6 = new JPanel();
		panel_6.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		contentPane.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new GridLayout(0, 2, 3, 0));
		
		JPanel panelIp = new JPanel();
		panel_6.add(panelIp);
		
		JPanel panelTxtIp = new JPanel();
		panel_6.add(panelTxtIp);
		
		JPanel panelPuerto = new JPanel();
		panel_6.add(panelPuerto);
		
		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panelPuerto.add(lblNickname);
		
		JPanel panelTxtPuerto = new JPanel();
		panel_6.add(panelTxtPuerto);
		
		textFieldNickname = new JTextField();
		panelTxtPuerto.add(textFieldNickname);
		textFieldNickname.setColumns(10);
		
		JPanel panelNickname = new JPanel();
		panel_6.add(panelNickname);
		
		JPanel panelTxtNickname = new JPanel();
		panel_6.add(panelTxtNickname);
	}
	
	public String getIp() {
		return "";
	}
	
	public String getPuerto() {
		return "1234";
	}
	
	public String getNickname() {
		return this.textFieldNickname.getText();
	}
	
}
