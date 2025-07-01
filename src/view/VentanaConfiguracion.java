package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Utils;

public class VentanaConfiguracion extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboBoxTipoArchivo = null;
	private JComboBox<String> comboBoxTecnicaEncriptado = null;
	private JTextField textFieldClave;
	private JButton botonAceptar;
	
	public VentanaConfiguracion(String title,String modo) {
		setTitle(title);
		setResizable(false);
		setLayout(new BorderLayout());
		setModal(true);

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		// Agregar borde al panel principal
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

		if(modo.equals(Utils.BTN_REGISTRARSE)) {
			JLabel tipoArchivo = new JLabel("Seleccione tipo de archivo: ");
			panelPrincipal.add(tipoArchivo);
			
			comboBoxTipoArchivo = new JComboBox<String>(Utils.TIPOS_ARCHIVO);
			comboBoxTipoArchivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			// Agregar espacio después del comboBox
			panelPrincipal.add(comboBoxTipoArchivo);
			panelPrincipal.add(Box.createVerticalStrut(10));
		}

		JLabel tecnicaEncriptado = new JLabel("Seleccione tecnica de encriptado: ");
		panelPrincipal.add(tecnicaEncriptado);

		comboBoxTecnicaEncriptado = new JComboBox<String>(Utils.TECNICAS_ENCRIPTADO);
		comboBoxTecnicaEncriptado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		// Agregar espacio después del comboBox
		panelPrincipal.add(comboBoxTecnicaEncriptado);
		panelPrincipal.add(Box.createVerticalStrut(10));

		JLabel clave = new JLabel("Ingrese clave de encriptado: ");
		panelPrincipal.add(clave);

		textFieldClave = new JTextField();
		textFieldClave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		textFieldClave.setPreferredSize(new Dimension(200, 30));
		panelPrincipal.add(textFieldClave);
		panelPrincipal.add(Box.createVerticalStrut(15));

		// Panel para centrar el botón
		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		botonAceptar = new JButton("Aceptar");
		botonAceptar.setActionCommand(Utils.BTN_ACCEPT_CONFIG_REGISTRO);
		panelBoton.add(botonAceptar);

		panelPrincipal.add(panelBoton);

		setContentPane(panelPrincipal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
	}
	
	public void setControlador(ActionListener controlador) {
		botonAceptar.addActionListener(controlador);
	}
	
	public String getClave() {
		return textFieldClave.getText();
	}
	
	public String getTecnica() {
		return comboBoxTecnicaEncriptado.getSelectedItem().toString();
	}
	
	public String getTipoArchivo() {
		if (comboBoxTipoArchivo == null) {
			return null; // Si no se seleccionó tipo de archivo, retornar null
		}
		return comboBoxTipoArchivo.getSelectedItem().toString();
	}
}	
