package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.Utils;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMensaje;

	private JButton btnAgrContacto;
	private JButton btnNueConv;
	
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
		
		JScrollPane scrollPaneConv = new JScrollPane();
		panel.add(scrollPaneConv, BorderLayout.CENTER);
		
		JTextArea textAreaConv = new JTextArea();
		scrollPaneConv.setViewportView(textAreaConv);
		
		JPanel panelIzq = new JPanel();
		splitPane.setLeftComponent(panelIzq);
		panelIzq.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelIzq.add(scrollPane, BorderLayout.CENTER);
		
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
	}
	
	public void setActionListener(ActionListener controlador) {
		this.btnAgrContacto.addActionListener(controlador);
		this.btnNueConv.addActionListener(controlador);
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
}
