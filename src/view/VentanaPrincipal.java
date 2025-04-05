package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.ControladorPrincipal;
import model.Contacto;
import utils.Utils;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMensaje;
	private JTextArea textAreaConv;

	private JButton btnAgrContacto;
	private JButton btnNueConv;
	private JButton btnEnviar;
	
	private JPanel panelBotonesConversaciones;
	private ControladorPrincipal controlador;
	
	/**
	 * Launch the application.
	 */
	
	 public static void main(String[] args) { EventQueue.invokeLater(new
	  Runnable() { public void run() { try { VentanaPrincipal frame = new
	  VentanaPrincipal("Sistema de mensajeria instantanea");
	  frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	  }

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(String title) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 503, 312);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setTitle(title);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		splitPane.setMinimumSize(new Dimension(100,100));
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelCuerpo = new JPanel();
		panel.add(panelCuerpo, BorderLayout.SOUTH);
		panelCuerpo.setLayout(new BorderLayout(0, 0));
		
		JPanel panelMensaje = new JPanel();
		panelCuerpo.add(panelMensaje, BorderLayout.CENTER);
		panelMensaje.setLayout(new BorderLayout(0, 0));
		
		textFieldMensaje = new JTextField();
		panelMensaje.add(textFieldMensaje, BorderLayout.NORTH);
		textFieldMensaje.setColumns(10);
		
		JPanel panelBtnEnviar = new JPanel();
		panelCuerpo.add(panelBtnEnviar, BorderLayout.EAST);
		panelBtnEnviar.setLayout(new BorderLayout(0, 0));
		
		JButton btnEnviar = new JButton("Enviar");
		panelBtnEnviar.add(btnEnviar, BorderLayout.NORTH);
		btnEnviar.setActionCommand(Utils.ENVIAR_MENSAJE);
		this.btnEnviar = btnEnviar;
		
		JScrollPane scrollPaneConv = new JScrollPane();
		panel.add(scrollPaneConv, BorderLayout.CENTER);
		
		textAreaConv = new JTextArea();
		scrollPaneConv.setViewportView(textAreaConv);
		
		JPanel panelIzq = new JPanel();
		splitPane.setLeftComponent(panelIzq);
		panelIzq.setLayout(new BorderLayout(0, 0));
		
		JPanel panelOpt = new JPanel();
		panelIzq.add(panelOpt, BorderLayout.NORTH);
		panelOpt.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panelAgrCont = new JPanel();
		panelOpt.add(panelAgrCont);
		panelAgrCont.setLayout(new BorderLayout(0, 0));
		
		JButton btnAgrContacto = new JButton("Agregar contacto");
		panelAgrCont.add(btnAgrContacto, BorderLayout.CENTER);
		btnAgrContacto.setActionCommand(Utils.CREAR_CONTACTO);
		this.btnAgrContacto = btnAgrContacto;
		
		JPanel panelNueConv = new JPanel();
		panelOpt.add(panelNueConv);
		panelNueConv.setLayout(new BorderLayout(0, 0));
		
		JButton btnNueConv = new JButton("Nueva conversacion");
		panelNueConv.add(btnNueConv, BorderLayout.CENTER);
		btnNueConv.setActionCommand(Utils.CREAR_CONVERSACION);
		this.btnNueConv = btnNueConv;
		
		JSeparator separator = new JSeparator();
		panelNueConv.add(separator, BorderLayout.SOUTH);
		
		JPanel panelConversacione = new JPanel();
		panelIzq.add(panelConversacione, BorderLayout.CENTER);
		panelConversacione.setLayout(new BorderLayout(0, 0));
		
		JLabel labelConversaciones = new JLabel("Conversaciones");
		labelConversaciones.setHorizontalAlignment(SwingConstants.CENTER);
		panelConversacione.add(labelConversaciones, BorderLayout.NORTH);
		
		JPanel panelBotonesConversaciones = new JPanel();
		this.panelBotonesConversaciones = panelBotonesConversaciones;
		
		JScrollPane scrollPane = new JScrollPane(panelBotonesConversaciones);
		panelBotonesConversaciones.setLayout(new BoxLayout(panelBotonesConversaciones, BoxLayout.Y_AXIS));
		panelConversacione.add(scrollPane, BorderLayout.CENTER);
		
	}
	
	public void setControlador(ControladorPrincipal controlador) {
		this.controlador = controlador;
		this.btnAgrContacto.addActionListener(controlador);
		this.btnNueConv.addActionListener(controlador);
		this.btnEnviar.addActionListener(controlador);
	}

	public String getMensaje() {
		return this.textFieldMensaje.getText();
	}
	
	public void bloqueoAgrContacto(boolean cond) {
		this.btnAgrContacto.setEnabled(!cond);
	}

	public void bloqueoNueConv(boolean cond) {
		this.btnNueConv.setEnabled(!cond);
	}
	
	public void agregarNuevoBotonConversacion(Contacto contacto) {

		JButton boton = new JButton(contacto.toString());
		boton.addActionListener(controlador);
		boton.setActionCommand(Utils.MENSAJE);
		boton.putClientProperty("contacto", contacto);
		boton.setPreferredSize(new Dimension(this.panelBotonesConversaciones.getWidth()-6, 25));
		boton.setMinimumSize(new Dimension(10, 25));
		boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		
		this.panelBotonesConversaciones.add(boton,0);
		this.panelBotonesConversaciones.repaint();
		this.panelBotonesConversaciones.revalidate();
		
	}
	
	public void cargarConversacion(String nicknameUsuario, Contacto contacto) {
		/**
		 * Aca se deveria cargar la conversacion correspondiente al contacto
		 * puse nicknameusuario como parametro para hacer un formato de chat tipo
		 * "Contacto:......"
		 * "Usuario:......"
		 */
		this.textAreaConv.setText("");
		int recept = contacto.getConversacion().getMensajesContacto().size() - 1;
		int emisor = contacto.getConversacion().getMensajesUsuario().size() - 1;
		
		while (recept >= 0 && emisor >= 0) {
//			if (contacto.getConversacion().getMensajesContacto().get(recept).getHora()
//					.isBefore(contacto.getConversacion().getMensajesUsuario().get(emisor).getHora())) 
//			{
				textAreaConv.append(contacto.getNickname()+":"+contacto.getConversacion().getMensajesContacto().get(recept).getCuerpo() + "\n");
				recept--;
//			}
//			else {
				textAreaConv.append(nicknameUsuario+":"+contacto.getConversacion().getMensajesUsuario().get(emisor).getCuerpo() + "\n");
				emisor--;
//			}
		}
		if (recept >= 0) {
			while (recept >= 0) {
				textAreaConv.append(contacto.getNickname()+":"+contacto.getConversacion().getMensajesContacto().get(recept).getCuerpo() + "\n");
				recept--;
			}
		}
		if (emisor >= 0) {
			while (emisor >= 0) {
				textAreaConv.append(nicknameUsuario+":"+contacto.getConversacion().getMensajesUsuario().get(emisor).getCuerpo() + "\n");
				emisor--;
			}
		}
	}
	
	public void agregarMensaje(String mensaje) {
		this.textAreaConv.append(mensaje + "\n"); 
		this.textFieldMensaje.setText("");
	}
	
	public void setNuevaConversacion() {
		this.textAreaConv.setText("");
	}
	
	/**
	 * Marca el boton correspondiente al contacto con un "*"
	 * @param contacto: Contacto con el cual ya se tiene una conversacion. 
	 */
	public void notificacion(Contacto contacto) {
		Component[] botones = this.panelBotonesConversaciones.getComponents();
		JButton botonActual = (JButton) botones[0];
		int i=0; 
		
		while((i<botones.length) && (!botonActual.getClientProperty("contacto").equals(contacto))) {
			i++;
		}
		
		if(botonActual.getClientProperty("contacto").equals(contacto)) {
			this.panelBotonesConversaciones.remove(i);
			this.agregarNuevoBotonConversacion(contacto);
			botonActual = (JButton)this.panelBotonesConversaciones.getComponent(0);
			botonActual.setText("*"+ contacto);
		}
	}
}
