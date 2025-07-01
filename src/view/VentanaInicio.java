package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.Utils;
import java.awt.Component;
import java.awt.Font;

public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNickname;
	private JButton btnIngresar;
	private JButton btnRegistrarse;

	/**
	 * Crea una instancia de JFrame de configuracion de usuario
	 * 
	 * @param titulo: 
	 */
	public VentanaInicio(String titulo) {
		this.setTitle(titulo);
		
		this.setMinimumSize(new Dimension(250,175));
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 314, 211);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelS = new JPanel();
		contentPane.add(panelS, BorderLayout.SOUTH);
		
		
		btnIngresar = new JButton("Ingresar");
		btnIngresar.setActionCommand(Utils.BTN_INGRESAR);
		
		btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setActionCommand(Utils.BTN_REGISTRARSE);
		
		panelS.add(btnRegistrarse);
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
	
	public void setControlador(ActionListener controlador) {
		this.btnIngresar.addActionListener(controlador);
		this.btnRegistrarse.addActionListener(controlador);
	}
	
	
	public String getNickname() {
		return this.textFieldNickname.getText();
	}
	
}
