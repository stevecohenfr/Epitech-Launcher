package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.border.EmptyBorder;
import java.awt.Component;

public class RedefinePassword extends JDialog {

	private static final long serialVersionUID = 4970202812674787120L;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JLabel lblInfo;

	/*
	 * Create the dialog.
	 * @param launcherFrame 
	 */
	public RedefinePassword(final String login, final LauncherFrame launcherFrame) {
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
						if (pass1.equals(pass2)) {
							if (pass1.isEmpty() || pass2.isEmpty())
							{
								lblInfo.setForeground(Color.RED);
								lblInfo.setText("Vous devez remplir tout les champs.");
								pack();
								return;
							}
							MySQL.changePassword(login, Util.md5(pass1));
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
						} else {
							lblInfo.setForeground(Color.RED);
							lblInfo.setText("Les mots de passe ne sont pas les mêmes.");
							pack();
						}
					}
				});
			}
		}
	}

}
