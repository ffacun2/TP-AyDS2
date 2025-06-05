package controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;


import monitor.Monitor;
import utils.Utils;
import view.VentanaMonitor;

public class ControladorMonitor implements WindowListener {
	
	
	private VentanaMonitor ventanaMonitor;
	private Monitor monitor;
	
	
	public ControladorMonitor() {
		ventanaMonitor = new VentanaMonitor();
		
	}
	
	public void iniciar() {
		try {
			ventanaMonitor.setTitle("Monitor");
			ventanaMonitor.addWindowsListener(this);
			ventanaMonitor.setVisible(true);
			monitor = new Monitor();
		} catch (Exception e) {
			Utils.mostrarError("No se pudo iniciar el monitor: " + e.getMessage(), ventanaMonitor);
			ventanaMonitor.dispose();
		}
	}



	@Override
	public void windowClosing(WindowEvent e) {
		int rta = ventanaMonitor.mostrarConfirmacion();
		
		if (rta == JOptionPane.YES_OPTION) {
			this.monitor.cerrarMonitor();
			ventanaMonitor.dispose();
		}
	}
	
	

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}


	@Override
	public void windowIconified(WindowEvent e) {
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
	}


	@Override
	public void windowActivated(WindowEvent e) {
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	
	
	
	
}
