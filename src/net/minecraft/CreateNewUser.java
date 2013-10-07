package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CreateNewUser extends JDialog {

	private static final long serialVersionUID = 2714033386321718482L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;
	private JComboBox<String> comboBox = new JComboBox<String>();
	private JCheckBox chckbxDefaultpasscheck;
	private ArrayList<String> rankList = new ArrayList<String>();
	private JLabel infoLbl;
	private String username = "";
	private String password = "";
	private String group = "";

	/**
	 * Create the dialog.
	 * @param userRank 
	 */
	public CreateNewUser(String userRank) {
		setBounds(100, 100, 530, 145);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			{
				JLabel lblUsername = new JLabel("Username");
				panel.add(lblUsername);
			}
			{
				textField = new JTextField();
				panel.add(textField);
				textField.setColumns(10);
			}
			{
				JLabel lblPassword = new JLabel("Password");
				panel.add(lblPassword);
			}
			{
				passwordField = new JPasswordField();
				passwordField.setEnabled(false);
				passwordField.setColumns(10);
				panel.add(passwordField);
			}
			{
				JLabel lblGroup = new JLabel("Group");
				panel.add(lblGroup);
			}
			{
				if (userRank.equals("Adm"))
					rankList.add("Developper");
				rankList.add("Administration");
				rankList.add("Communication");
				String[] rankArray = new String[rankList.size()];
				rankArray = rankList.toArray(rankArray);
				comboBox.setModel(new DefaultComboBoxModel<String>(rankArray));
				panel.add(comboBox);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			{
				chckbxDefaultpasscheck = new JCheckBox("Default password (\"null\")");
				chckbxDefaultpasscheck.setSelected(true);
				panel.add(chckbxDefaultpasscheck);
				chckbxDefaultpasscheck.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED)
							passwordField.setEnabled(false);
						else
							passwordField.setEnabled(true);
					}
				});
			}
			{
				JCheckBox showPassword = new JCheckBox("Show password");
				showPassword.setToolTipText("Afficher le mot de passe en clair");
				showPassword.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							passwordField.setEchoChar((char) 0);
						}else {
							passwordField.setEchoChar('•');
						}
					}
				});
				panel.add(showPassword);
			}
		}
		{
			{
				{
					JPanel panel = new JPanel();
					getContentPane().add(panel);
					{
						JComboBox comboBox_1 = new JComboBox();
						panel.add(comboBox_1);
					}
					{
						JButton btnDelete = new JButton("Delete");
						panel.add(btnDelete);
					}
					{
						JLabel label = new JLabel("");
						panel.add(label);
					}
				}
				{
					JPanel panel = new JPanel();
					getContentPane().add(panel);
					{
						JButton okButton = new JButton("OK");
						panel.add(okButton);
						getRootPane().setDefaultButton(okButton);
						JButton cancelButton = new JButton("Cancel");
						panel.add(cancelButton);
						JPanel buttonPane = new JPanel();
						buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
						getContentPane().add(buttonPane);
						{
							infoLbl = new TransparentLabel("");
							buttonPane.add(infoLbl);
						}
						cancelButton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								setVisible(false);
								dispose();
							}
						});
						okButton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								username = textField.getText();
								if (chckbxDefaultpasscheck.isSelected())
									password = "null";
								else
									password = new String(passwordField.getPassword());
								group = comboBox.getSelectedItem().toString();
								
								if (username.isEmpty())
									JOptionPane.showMessageDialog(null, "Username invalid !","Erreur", JOptionPane.ERROR_MESSAGE);
								if (password.isEmpty())
									JOptionPane.showMessageDialog(null, "Password invalid !","Erreur", JOptionPane.ERROR_MESSAGE);
								if (username.isEmpty() || password.isEmpty())
									return;
								System.out.println("Creating user..");
								if (MySQL.checkIfUserExistsInDb(username)) {
									infoLbl.setForeground(Color.RED);
									infoLbl.setText("This user already exist!");
									return ;
								}
								if (MySQL.createUser(username, Util.md5(password), group)) {
									System.out.println("Username => " + username);
									System.out.println("Password => " + Util.md5(password));
									System.out.println("Group    => " + group);
								}else {
									infoLbl.setForeground(Color.RED);
									infoLbl.setText("Can't create this user, try again later!");
									return ;
								}
								infoLbl.setForeground(Color.GREEN);
								infoLbl.setText("User '" + username + "' created in group '" + group + "' !");
								new Thread() {
									public void run() {
										try {
											Thread.sleep(2500);
											setVisible(false);
											dispose();
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}.start();
								setVisible(false);
								dispose();
							}
						});
					}
				}
			}
		}
		pack();
	}

}
