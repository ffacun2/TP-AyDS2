package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ControladorPrincipal;
import model.Contacto;
import utils.Utils;

public class DialogSeleccionarContacto extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> comboBox;
	private JButton botonAceptar;
	
	public DialogSeleccionarContacto(JFrame ventana, ControladorPrincipal controlador, ArrayList<String> listaContactos, String mode) {
		if (!mode.equals(Utils.MOSTRAR_AGENDA))
			setTitle("Seleccione Contacto");
		else
			setTitle("Agenda");
		setSize(300,100);
		setLocationRelativeTo(ventana);
		setLayout(new BorderLayout());	
		setModal(true);
		
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		
		comboBox = new JComboBox<String>();
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		
		for(String nickContacto: listaContactos) {
			comboBox.addItem(nickContacto);
		}
		
		panelPrincipal.add(comboBox);
		
		if (!mode.equals(Utils.MOSTRAR_AGENDA)) {
			botonAceptar = new JButton("Aceptar");
			botonAceptar.addActionListener(controlador);
			if(mode.equals(Utils.CREAR_CONVERSACION))
				botonAceptar.setActionCommand(Utils.CONFIRMAR_CONTACTO);
			if(mode.equals(Utils.MODO_AGR_CONTACTO))
				botonAceptar.setActionCommand(Utils.AGREGAR_CONTACTO);
			panelPrincipal.add(botonAceptar);
		}
		setContentPane(panelPrincipal);
	}
	
	public String getContactoElegido() {
		return (String)this.comboBox.getSelectedItem();
	}

}
