package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RedefineSpecificPassword extends JDialog {

	private static final long serialVersionUID = 4970202812674787120L;
	private final JPanel contentPanel = new JPanel();
	private JTextField loginField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JLabel lblInfo;
	private String userLogin;

	/*
	 * Create the dialog.
	 * @param launcherFrame 
	 */
	public RedefineSpecificPassword(final LauncherFrame launcherFrame) {
		setAlwaysOnTop(true);
		setBounds(100, 100, 373, 149);
		setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel Login = new JLabel("Type user login");
				panel.add(Login);
			}
			{
				loginField = new JTextField();
				loginField.setColumns(10);
				panel.add(loginField);
			}
			{
				JLabel Password = new JLabel("Type password");
				panel.add(Password);
			}
			{
				passwordField = new JPasswordField();
				passwordField.setColumns(10);
				panel.add(passwordField);
			}
			{
				JLabel RePassword = new JLabel("Retype password");
				panel.add(RePassword);
			}
			{
				passwordField_1 = new JPasswordField();
				passwordField_1.setColumns(10);
				panel.add(passwordField_1);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			{
				final JCheckBox showPassword = new JCheckBox("Afficher mot de passe");
				showPassword.setToolTipText("Afficher le mot de passe en clair");
				showPassword.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							passwordField.setEchoChar((char) 0);
							passwordField_1.setEchoChar((char) 0);
						}else if (e.getStateChange() == ItemEvent.DESELECTED) {
							passwordField.setEchoChar('•');
							passwordField_1.setEchoChar('•');
						}
					}
				});
				panel.add(showPassword);
			}
		}
		{
			lblInfo = new JLabel("");
			lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPanel.add(lblInfo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Valider");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String pass1 = new String(passwordField.getPassword());
						String pass2 = new String(passwordField_1.getPassword());
						userLogin = new String(loginField.getText());
						if (userLogin.isEmpty() == true) {
							lblInfo.setForeground(Color.RED);
							lblInfo.setText("Veuillez spécifier un login valide.");
							System.err.println("Invalid Username");
							pack();
							return;
						}
						else if (pass1.equals(pass2)) {
							if (MySQL.checkIfUserExistsInDb(loginField.getText()) == false)
								return;
							MySQL.changePassword(userLogin, Util.md5(pass1));
							lblInfo.setForeground(Color.GREEN);
							loginPanel.forceRedefinePassword = false;
							lblInfo.setText("Mot de passe changé avec succès.");
							pack();
							new Thread() {
								public void run() {
									try {
										Thread.sleep(2500);
										setVisible(false);
										dispose();
										launcherFrame.setFocusable(true);
										launcherFrame.requestFocusInWindow();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}.start();
						}else {
							JOptionPane.showMessageDialog(null, "Les mots de passe ne sont pas les mêmes !","Erreur", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
			}
		}
	}

}
