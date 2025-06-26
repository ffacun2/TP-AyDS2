package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.ControladorConfiguracion;
import utils.Utils;

public class DialogConfiguracion extends JDialog{
	private JFrame ventana;
	private JComboBox<String> comboBoxTipoArchivo = null;
	private JComboBox<String> comboBoxTecnicaEncriptado;
	private JTextField textFieldClave;
	private JButton botonAceptar;
	private ControladorConfiguracion controlador;
	
	public DialogConfiguracion(JFrame ventana, ControladorConfiguracion controlador, String modo) {
		this.ventana = ventana;
		this.controlador = controlador;
		setTitle("Configuracion");
		setResizable(false);
		setLocationRelativeTo(ventana);
		setLayout(new BorderLayout());
		setModal(true);

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		// Agregar borde al panel principal
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

		if(modo.equals(Utils.REGISTRARSE)) {
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
		botonAceptar.addActionListener(controlador);
		botonAceptar.setActionCommand(Utils.CONFIG_REGISTRO);
		panelBoton.add(botonAceptar);

		panelPrincipal.add(panelBoton);

		setContentPane(panelPrincipal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
	}
	
	public void getConfiguracion() {
		String clave = textFieldClave.getText();
		if(!clave.equals("")) {
			String tecnica = this.comboBoxTecnicaEncriptado.getSelectedItem().toString();
			
			String tipoArchivo = null;
			if(this.comboBoxTipoArchivo != null) {
				tipoArchivo = this.comboBoxTipoArchivo.getSelectedItem().toString();
			}
			
			this.controlador.setConfiguracion(clave, tecnica, tipoArchivo);
			this.dispose();
		}else {
			Utils.mostrarError("Error: ingresar clave valida", ventana);
		}
		
	}
}	
