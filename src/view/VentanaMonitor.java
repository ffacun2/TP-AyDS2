package view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowListener;

import javax.swing.SwingConstants;

public class VentanaMonitor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public VentanaMonitor() {
		super("Monitor de Servidores");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 300, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel titleLabel = new JLabel("Monitor");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(titleLabel);
	}

	
	public void addWindowsListener(WindowListener listener) {
		this.addWindowListener(listener);
	}
	
	public int mostrarConfirmacion() {
		return JOptionPane.showConfirmDialog(this, 
				"¿Desea salir del monitor?", 
				"Salir", 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE);
	}
}
